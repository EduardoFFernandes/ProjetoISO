package pseudoSO;

import static modules.Semaforo.SEMAFORO_ABERTO;
import static util.Constantes.ARQUIVO_IGUAIS;
import static util.Constantes.INICIANDO;
import static util.Constantes.NAO_SELECIONADO_ARQUIVOS;
import static util.Constantes.NAO_SELECIONADO_PROCESSOS;
import static util.Constantes.PROCESSOS;
import static util.Util.arquivoInvalido;
import static util.Util.arquivoValidado;
import static util.Util.memoriaEmBranco;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import modules.Disco;
import modules.Filas;
import modules.Interface;
import modules.LeitorDeArquivos;
import modules.Memoria;
import modules.Processos;
import modules.Recursos;
import util.Util;

// Lucas da Silva Souza                        16/0013020
// Anne Carolina Borges Gontijo Gomes          14/0016546
// Eduardo Freire Fernandes                    16/0027136

public class PseudoSO {
    private Interface terminal;
    private File arquivoDeProcessos = null;
    private File arquivoDeOperacao = null;
    private ArrayList<Processo> processos;
    private ArrayList<Operacao> operacoes;
    private ArrayList<Arquivo> arquivos;
    private LeitorDeArquivos manipulador;

    /**
     * Funcao inicializadora do programa, cria a tela principal da aplicacao.
     * importante ressaltar o metodo invokeLater, e utilizado nessa funcao para
     * garantir que o terminal seja criado de forma assincrona evitando a condicao
     * de corrida que acusa um systemException.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    PseudoSO pseudoSO = new PseudoSO();
                    pseudoSO.setTerminal(new Interface());
                    pseudoSO.getTerminal().setMainListener(pseudoSO);
                    pseudoSO.getTerminal().initialize();
                    pseudoSO.getTerminal().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sistema Operacional, aqui a classe GerenciadorDeFilas extende Threads, isso
     * significa que um processo paralelo vai ser iniciado assim que o objeto
     * gerenciadorDeFilas e iniciado(java.lang.Thread.start()) tendo como argumentos
     * os processos carregados, operacoes carregadas, arquivos carregados, a tela
     * principal(JPanel), e a quantidade de blocos no arquivo carregado.
     */
    public void iniciar() {
        if (arquivoDeProcessos == null) {
            terminal.logMessage(NAO_SELECIONADO_PROCESSOS);
            return;
        }
        if (arquivoDeOperacao == null) {
            terminal.logMessage(NAO_SELECIONADO_ARQUIVOS);
            return;
        }
        if (arquivoDeProcessos.equals(arquivoDeOperacao)) {
            terminal.logMessage(ARQUIVO_IGUAIS);
            return;
        }
        terminal.logMessage(INICIANDO);
        Filas filas = criaContexto();
        filas.setDaemon(true);
        filas.start();
    }

    /**
     * Funcao que valida os arquivos selecionados.
     */
    public void valida(File arquivo, String tipoArquivo) {
        manipulador = new LeitorDeArquivos(arquivo, tipoArquivo);
        try {
            if (arquivo.exists() && manipulador.validaArquivo()) {
                if (tipoArquivo.equals(PROCESSOS)) {
                    this.arquivoDeProcessos = arquivo;
                    this.processos = manipulador.getProcessosValidados();
                } else {
                    this.arquivoDeOperacao = arquivo;
                    this.operacoes = manipulador.getOperacoesValidadas();
                    this.arquivos = manipulador.getArquivosValidados();
                }
                terminal.logMessage(arquivoValidado(arquivo.getName()));
            } else {
                invalida(tipoArquivo);
                terminal.logMessage(arquivoInvalido(arquivo.getName()));
            }
        } catch (Exception e) {
            invalida(tipoArquivo);
            terminal.logMessage(arquivoInvalido(arquivo.getName()));
        }
    }

    /**
     * Funcao que trata arquivos invalidos, setando nulo as variaveis da Classe
     * Main.
     */
    public void invalida(String tipoArquivo) {
        if (tipoArquivo == PROCESSOS) {
            this.arquivoDeProcessos = null;
            this.processos = null;
            return;
        }
        this.arquivoDeOperacao = null;
        this.operacoes = null;
        this.arquivos = null;
    }

    /**
     * Esse metodo cria o contexto da thread de acordo com os arquivos de texto
     * recebidos
     */
    private Filas criaContexto() {
        Filas filas = new Filas();

        // MEMORIA PRINCIPAL
        filas.setGerenciadorDaMemoriaPrincipal(new Memoria());
        filas.getGerenciadorDaMemoriaPrincipal().setMemoriaAsString(memoriaEmBranco());
        filas.getGerenciadorDaMemoriaPrincipal().setProcessos(new ArrayList<>());

        // RECURRSOS
        filas.setGerenciadorDeRecursos(new Recursos());
        filas.getGerenciadorDeRecursos().setScanner(SEMAFORO_ABERTO);
        filas.getGerenciadorDeRecursos().setModem(SEMAFORO_ABERTO);
        filas.getGerenciadorDeRecursos().setEstruturaImpressoras(new int[2]);
        filas.getGerenciadorDeRecursos().setImpressoras(0, SEMAFORO_ABERTO);
        filas.getGerenciadorDeRecursos().setImpressoras(1, SEMAFORO_ABERTO);
        filas.getGerenciadorDeRecursos().setEstruturaDiscoRigido(new int[2]);
        filas.getGerenciadorDeRecursos().setDiscoRigido(0, SEMAFORO_ABERTO);
        filas.getGerenciadorDeRecursos().setDiscoRigido(1, SEMAFORO_ABERTO);

        // DISCO
        filas.setGerenciadorDoDisco(new Disco());
        filas.getGerenciadorDoDisco().setDiscoAsString(Util.discoEmBranco(manipulador.getBlocosDisco()));
        filas.getGerenciadorDoDisco().setArquivos(arquivos);
        filas.getGerenciadorDoDisco().setFilas(filas);
        filas.getGerenciadorDoDisco().processaArquivos();
        // PROCESSOS
        filas.setGerenciadorDeProcessos(new Processos());
        ArrayList<ArrayList<Processo>> filasPrioridade = new ArrayList<ArrayList<Processo>>();
        for (int i = 0; i <= 3; i++) {
            filasPrioridade.add(new ArrayList<Processo>());
        }
        filas.getGerenciadorDeProcessos().setFilas(filasPrioridade);
        filas.getGerenciadorDeProcessos().setFilaProcessos(processos);

        // TERMINAL
        filas.setTerminal(terminal);
        filas.setLstProcessos(processos);
        filas.setProcessos(processos);
        filas.setOperacoes(operacoes);
        return filas;
    }

    public Interface getTerminal() {
        return terminal;
    }

    public void setTerminal(Interface terminal) {
        this.terminal = terminal;
    }
}
