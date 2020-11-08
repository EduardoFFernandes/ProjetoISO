package main;

import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;

import models.Arquivo;
import modules.GerenciadorDeFilas;
import modules.Interface;
import util.Constantes;
import util.ManipuladorDeArquivos;

public class Main {
	private static Interface telaPrincipal;
	private File arquivoDeProcessos = null;
	private File arquivoDeOperacao = null;
	private ArrayList<?> processos;
	private ArrayList<?> operacoes;
	private ArrayList<Arquivo> arquivosEmDisco;
	private ManipuladorDeArquivos manipulador;
	
	public static final String PROCESSOS = "Processos";
	public static final String ARQUIVOS = "Arquivos";
	public static final String INICIAR = "Iniciar";
	
	
	/**
	 * Funcao inicializadora do programa, cria o objeto da tela principal e a coloca como visivel
	 * @author Dudu
	 * */
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
	 * Funcao que verifica se os arquivos foram carregados e inicia o Sistema Operacional.
	 * @author Dudu
	 * 
	 * */
	public void iniciar(){
		if(arquivoDeProcessos == null){
			telaPrincipal.logMessage(Constantes.NAO_SELECIONADO_ARQ_PROCESSOS,Interface.RED);
			return;
		}
		if(arquivoDeOperacao == null){
			telaPrincipal.logMessage(Constantes.NAO_SELECIONADO_ARQ_ARQUIVOS,Interface.RED);
			return;
		}
		if(arquivoDeProcessos.equals(arquivoDeOperacao)){
			telaPrincipal.logMessage(Constantes.ARQUIVO_IGUAIS,Interface.RED);
			return;
		}
		telaPrincipal.logMessage(Constantes.INICIANDO,Interface.GREEN);
		
		GerenciadorDeFilas gerenciadorDeFilas = new GerenciadorDeFilas(processos, operacoes, arquivosEmDisco, telaPrincipal, manipulador.getQtdBlocosDisco());
		gerenciadorDeFilas.start();
	}
	
	/**
	 * Funcao que valida os arquivos selecionados.
	 * @author Dudu
	 * */
	public void valida(File arquivo, String tipoArquivo){
		manipulador = new ManipuladorDeArquivos(arquivo,tipoArquivo);
		try {
			if(arquivo.exists() && manipulador.validaArquivo()){
				ArrayList<?> validados = manipulador.getObjetosValidados();
				if(tipoArquivo.equals(PROCESSOS)){				
					this.arquivoDeProcessos = arquivo;
					this.processos = validados;
				}else{
					this.arquivoDeOperacao = arquivo;
					this.operacoes = validados;
					this.arquivosEmDisco = manipulador.getArquivosValidados();
				}
				telaPrincipal.logMessage(Constantes.arquivoValidado(arquivo.getName()), Interface.GREEN);
			}else{
				invalida(tipoArquivo);
				telaPrincipal.logMessage(Constantes.arquivoNaoValido(arquivo.getName()), Interface.RED);	
			}
		} catch (Exception e) {
			invalida(tipoArquivo);
			telaPrincipal.logMessage(Constantes.arquivoNaoValido(arquivo.getName()), Interface.RED);	
		} 
	}
	
	/**
	 * Funcao que trata arquivos invalidos, setando nulo as variaveis da Classe Main.
	 * @author Dudu
	 * */
	public void invalida (String tipoArquivo){
		if(tipoArquivo == PROCESSOS) {				
			this.arquivoDeProcessos = null;
			this.processos = null;
			return;
		} 
		this.arquivoDeOperacao = null;
		this.operacoes = null;
		this.arquivosEmDisco = null;
		
	}
}
