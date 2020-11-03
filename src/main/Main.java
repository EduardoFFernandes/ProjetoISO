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
	
	public static final String ARQ_PROCESSOS = "Processos";
	public static final String ARQ_OPERACAO = "Arquivos";
	public static final String INICIAR = "Iniciar";
	
	private Thread soThread;

	
	/**
	 * Fun��o inicializadora do programa, cria o objeto da tela principal e a coloca como visivel
	 * 
	 * @param	args	argumentos inciais do programa
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
	 * Fun��o que verifica se os arquivos j� foram selecionados. Caso verdadeiro, inicia uma Thread contendo o moduloSO, que � respons�vel
	 * por iniciar os outros m�dulos e executar os comandos passados pelos arquivos de input
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
		telaPrincipal.logMessage(Constantes.INICIANDO,Interface.DARK_GREEN);
		
		GerenciadorDeFilas SO = new GerenciadorDeFilas(processos, operacoes, arquivosEmDisco, telaPrincipal, manipulador.getQtdBlocosDisco());
		soThread = new Thread(SO);
		soThread.setDaemon(true);
		soThread.start();
	}
	
	
	/**
	 * Fun��o Callback da telaPrincipal. Recebe um File que ser� manipulado, populando uma das estruturas de array caso o mesmo seja v�lido
	 * se n�o printa na tela que o arquivo � invalido.
	 * 
	 * @param	aSerValidado	arquivo que ser� validado pelo manipulador de arquivos
	 * @param	tipoArquivo		tipo do Arquivo, definido pelas static strings
	 * */
	
	public void validaArquivo(File aSerValidado,String tipoArquivo){
		manipulador = new ManipuladorDeArquivos(aSerValidado,tipoArquivo);
		try {
			if(aSerValidado.exists() && manipulador.validaArquivo()){
				ArrayList<?> validados = manipulador.getObjetosValidados();
				if(tipoArquivo.equals(ARQ_PROCESSOS)){				
					this.arquivoDeProcessos = aSerValidado;
					this.processos = validados;
				}else{
					this.arquivoDeOperacao = aSerValidado;
					this.operacoes = validados;
					this.arquivosEmDisco = manipulador.getArquivosValidados();
				}
				telaPrincipal.logMessage(Constantes.arquivoValidado(aSerValidado.getName()), Interface.DARK_GREEN);
			}else{
				invalidaArquivo(tipoArquivo);
				telaPrincipal.logMessage(Constantes.arquivoNaoValido(aSerValidado.getName()), Interface.RED);	
			}
		} catch (Exception e) {
			invalidaArquivo(tipoArquivo);
			telaPrincipal.logMessage(Constantes.arquivoNaoValido(aSerValidado.getName()), Interface.RED);	
		} 
	}
	
	
	/**
	 * Fun��o para invalidar qualquer uma das sele��es de arquivo.
	 * 
	 * @param	tipoArquivo		tipo do Arquivo, definido pelas static strings
	 * */
	public void invalidaArquivo(String tipoArquivo){
		if(tipoArquivo.equals(ARQ_PROCESSOS)){				
			this.arquivoDeProcessos = null;
			this.processos = null;
		}else{
			this.arquivoDeOperacao = null;
			this.operacoes = null;
			this.arquivosEmDisco = null;
		}
	}
	
}
