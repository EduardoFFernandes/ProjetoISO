package modules;

import static util.Constantes.NAO_EXISTE_PROCESSO;
import static util.Constantes.OP_CRIAR;
import static util.Util.arquivoNaoEncontrado;
import static util.Util.excluiuArq;
import static util.Util.naoSalvouArquivo;
import static util.Util.operacoesDoSistema;
import static util.Util.processoErroDelete;
import static util.Util.salvouArquivo;

import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;

public class Disco {

    private ArrayList<Arquivo> arquivos;
    private Filas filas;
    private String discoAsString;
    
    /**
     * Metodo que cria os arquivos do disco.
     */
    public boolean cria(Operacao operacao, int posicaoOperacao) {
        int indiceDisponivel;
        String espacoNecessario = new String();
        for (int i = 0; i < operacao.getBlocosNecessarios(); i++) {
            espacoNecessario = espacoNecessario.concat("0");
        }
        if ((indiceDisponivel = discoAsString.indexOf(espacoNecessario)) != -1) {

            discoAsString = discoAsString.substring(0, indiceDisponivel)
                    .concat(espacoNecessario.replaceAll("0", operacao.getNomeArquivo()))
                    .concat(discoAsString.substring(indiceDisponivel + operacao.getBlocosNecessarios()));
            filas.getTerminal().logMessage(operacoesDoSistema(posicaoOperacao, true));
            filas.getTerminal().logMessage(salvouArquivo(operacao, indiceDisponivel));
            return true;
        } else {
            filas.getTerminal().logMessage(operacoesDoSistema(posicaoOperacao, false));
            filas.getTerminal().logMessage(naoSalvouArquivo(operacao));
            return false;
        }
    }

    /**
     * Metodo que executa as operacoes recebidas.
     */
    public void executaOperacoes(ArrayList<Operacao> operacoes, Interface terminal) {
        int posicaoOperacao = 1;
        for (Operacao operacao : operacoes) {

            if (operacao.getCodOperacao() == OP_CRIAR) {

                cria(operacao, posicaoOperacao);
            } else {
                Arquivo arquivo = procuraArquivo(operacao.getNomeArquivo());
                if (arquivo == null) {
                    terminal.logMessage(operacoesDoSistema(posicaoOperacao, false));
                    filas.getTerminal().logMessage(arquivoNaoEncontrado(operacao.getNomeArquivo()));
                } else if (operacao.getIdProcesso() == arquivo.getIdProcesso()) {
                    terminal.logMessage(operacoesDoSistema(posicaoOperacao, true));
                    deleta(arquivo);
                    filas.getTerminal().logMessage(excluiuArq(operacao));
                } else if (filas.processoTempoReal(operacao.getIdProcesso())) {
                    terminal.logMessage(operacoesDoSistema(posicaoOperacao, true));
                    deleta(arquivo);
                    filas.getTerminal().logMessage(excluiuArq(operacao));
                } else {
                    terminal.logMessage(operacoesDoSistema(posicaoOperacao, false));
                    filas.getTerminal().logMessage(
                            processoErroDelete(operacao.getIdProcesso(), operacao.getNomeArquivo()));
                }

            }
            if (!filas.processoValidado(operacao.getIdProcesso())) {
                terminal.logMessage(operacoesDoSistema(posicaoOperacao, false));
                filas.getTerminal().logMessage(NAO_EXISTE_PROCESSO);
                return;
            }
            posicaoOperacao++;
        }
    }

    /**
     * Metodo que exclui arquivos do disco.
     */
    public boolean deleta(Arquivo arquivo) {

        String espacoLivre = new String();
        for (int i = 0; i < arquivo.getBlocosOcupados(); i++) {
            espacoLivre = espacoLivre.concat("0");
        }
        discoAsString = discoAsString.substring(0, arquivo.getPrimeiroBloco()).concat(espacoLivre)
                .concat(discoAsString.substring(arquivo.getPrimeiroBloco() + arquivo.getBlocosOcupados()));
        return true;
    }

    /**
     * Metodo que insere os arquivos passados manualmente pelo arquivo de texto no disco.
     */
    public void processaArquivos() {
        for (Arquivo arquivo : arquivos) {
            int i;
            String espacoLivre = new String();
            for (i = 0; i < arquivo.getBlocosOcupados(); i++) {
                espacoLivre = espacoLivre.concat(arquivo.getNomeArquivo());
            }
            discoAsString = discoAsString.substring(0, arquivo.getPrimeiroBloco()).concat(espacoLivre)
                    .concat(discoAsString.substring(arquivo.getPrimeiroBloco() + arquivo.getBlocosOcupados()));

        }
    }
    
    /**
     * Metodo de busca.
     */
    private Arquivo procuraArquivo(String nome) {
        for (Arquivo arquivo : arquivos) {
            if (arquivo.getNomeArquivo().equals(nome)) {
                return arquivo;
            }
        }
        return null;
    }

    public String getDiscoAsString() {
        return discoAsString;
    }

    public void setDiscoAsString(String discoAsString) {
        this.discoAsString = discoAsString;
    }

    public ArrayList<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(ArrayList<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public Filas getFilas() {
        return filas;
    }

    public void setFilas(Filas filas) {
        this.filas = filas;
    }

}
