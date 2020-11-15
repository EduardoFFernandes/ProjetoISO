package main;

import static util.Constantes.ARQUIVO_IGUAIS;
import static util.Constantes.INICIANDO;
import static util.Constantes.NAO_SELECIONADO_ARQUIVOS;
import static util.Constantes.NAO_SELECIONADO_PROCESSOS;
import static util.Util.arquivoNaoValido;
import static util.Util.arquivoValidado;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import modules.GerenciadorDeFilas;
import modules.Interface;
import modules.ManipuladorDeArquivos;

public class Main {
	private static Interface telaPrincipal;
	private File arquivoDeProcessos = null;
	private File arquivoDeOperacao = null;
	private ArrayList<Processo> processos;
	private ArrayList<Operacao> operacoes;
	private ArrayList<Arquivo> arquivos;
	private ManipuladorDeArquivos manipulador;

	public static final String PROCESSOS = "Processos";
	public static final String ARQUIVOS = "Arquivos";
	public static final String INICIAR = "Iniciar";

	/**
	 * Funcao inicializadora do programa, cria a tela principal da aplicacao.
	 * 
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main main = new Main();
					telaPrincipal = new Interface(main);
					telaPrincipal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Sistema Operacional, aqui a classe GerenciadorDeFilas extende Threads, isso
	 * significa que um processo paralelo vai ser iniciado assim que o objeto
	 * gerenciadorDeFilas é iniciado(java.lang.Thread.start()) tendo como argumentos
	 * os processos carregados, operacoes carregadas, arquivos carregados, a tela
	 * principal(JPanel), e a quantidade de blocos no arquivo carregado.
	 * 
	 * 
	 * 
	 */
	public void iniciar() {
		if (arquivoDeProcessos == null) {
			telaPrincipal.logMessage(NAO_SELECIONADO_PROCESSOS, Interface.RED);
			return;
		}
		if (arquivoDeOperacao == null) {
			telaPrincipal.logMessage(NAO_SELECIONADO_ARQUIVOS, Interface.RED);
			return;
		}
		if (arquivoDeProcessos.equals(arquivoDeOperacao)) {
			telaPrincipal.logMessage(ARQUIVO_IGUAIS, Interface.RED);
			return;
		}
		telaPrincipal.logMessage(INICIANDO, Interface.GREEN);

		GerenciadorDeFilas gerenciadorDeFilas = new GerenciadorDeFilas(processos, operacoes, arquivos, telaPrincipal,
				manipulador.getQtdBlocosDisco());
		gerenciadorDeFilas.start();
	}

	/**
	 * Funcao que valida os arquivos selecionados.
	 * 
	 * 
	 */
	public void valida(File arquivo, String tipoArquivo) {
		manipulador = new ManipuladorDeArquivos(arquivo, tipoArquivo);
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
				telaPrincipal.logMessage(arquivoValidado(arquivo.getName()), Interface.GREEN);
			} else {
				invalida(tipoArquivo);
				telaPrincipal.logMessage(arquivoNaoValido(arquivo.getName()), Interface.RED);
			}
		} catch (Exception e) {
			invalida(tipoArquivo);
			telaPrincipal.logMessage(arquivoNaoValido(arquivo.getName()), Interface.RED);
		}
	}

	/**
	 * Funcao que trata arquivos invalidos, setando nulo as variaveis da Classe
	 * Main.
	 * 
	 * 
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
