package pseudoSO;

import static util.Constantes.ARQUIVO_IGUAIS;
import static util.Constantes.INICIANDO;
import static util.Constantes.NAO_SELECIONADO_ARQUIVOS;
import static util.Constantes.NAO_SELECIONADO_PROCESSOS;
import static util.Constantes.PROCESSOS;
import static util.Util.arquivoInvalido;
import static util.Util.arquivoValidado;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import modules.Filas;
import modules.Interface;
import modules.LeitorDeArquivos;

public class PseudoSO {
	private static Interface terminal;
	private File arquivoDeProcessos = null;
	private File arquivoDeOperacao = null;
	private ArrayList<Processo> processos;
	private ArrayList<Operacao> operacoes;
	private ArrayList<Arquivo> arquivos;
	private LeitorDeArquivos manipulador;

	/**
	 * Funcao inicializadora do programa, cria a tela principal da aplicacao.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PseudoSO pseudoSO = new PseudoSO();
					terminal = new Interface(pseudoSO);
					terminal.setVisible(true);
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

		Filas filas = new Filas(processos, operacoes, arquivos, terminal,manipulador.getBlocosDisco());
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
}
