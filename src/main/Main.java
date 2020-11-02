package main;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import models.Arquivo;
import modulos.ModuloSO;
import modulos.ModuloTelaPrincipal;
import util.Constantes;
import util.ManipuladorDeArquivos;

/**
 * Classe Inicial do programa
 * 
 * @author tulio.matias		-	06/06/2018
 * 
 * */
public class Main {
	private static ModuloTelaPrincipal telaPrincipal;
	private File arquivoDeProcessos = null;
	private File arquivoEstruturaArquivos = null;

	private ArrayList<?> processos;
	private ArrayList<?> operacoesEstruturaArq;
	private ArrayList<Arquivo> arquivosEmDisco;
	
	private ManipuladorDeArquivos manipulador;
	
	public static final String ARQ_PROCESSOS = "Processos";
	public static final String ARQ_ESTRUTURA_ARQUIVOS = "Arquivos";
	public static final String INICIAR_SO = "iniciarSO";
	
	private Thread soThread;

	
	/**
	 * Função inicializadora do programa, cria o objeto da tela principal e a coloca como visivel
	 * 
	 * @param	args	argumentos inciais do programa
	 * */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main main = new Main();
					telaPrincipal = new ModuloTelaPrincipal(main);
					telaPrincipal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Destrutor da tela, não utilizada.
	 * 
	 * */
	public void closeWindow(){
		telaPrincipal.dispatchEvent(new WindowEvent(telaPrincipal, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Função que verifica se os arquivos já foram selecionados. Caso verdadeiro, inicia uma Thread contendo o moduloSO, que é responsável
	 * por iniciar os outros módulos e executar os comandos passados pelos arquivos de input
	 * 
	 * */
	public void iniciarSO(){
		if(arquivoDeProcessos == null){
			telaPrincipal.printaNoTerminal(Constantes.NAO_SELECIONADO_ARQ_PROCESSOS.getTexto(),ModuloTelaPrincipal.RED);
			return;
		}
		if(arquivoEstruturaArquivos == null){
			telaPrincipal.printaNoTerminal(Constantes.NAO_SELECIONADO_ARQ_ARQUIVOS.getTexto(),ModuloTelaPrincipal.RED);
			return;
		}
		if(arquivoDeProcessos.equals(arquivoEstruturaArquivos)){
			telaPrincipal.printaNoTerminal(Constantes.ARQUIVO_IGUAIS.getTexto(),ModuloTelaPrincipal.RED);
			return;
		}
		telaPrincipal.printaNoTerminal(Constantes.INICIANDO_SO.getTexto(),ModuloTelaPrincipal.DARK_GREEN);
		
		ModuloSO SO = new ModuloSO(processos, operacoesEstruturaArq, arquivosEmDisco, telaPrincipal, manipulador.getQtdBlocosDisco());
		soThread = new Thread(SO);
		soThread.setDaemon(true);
		soThread.start();
	}
	
	
	/**
	 * Função Callback da telaPrincipal. Recebe um File que será manipulado, populando uma das estruturas de array caso o mesmo seja válido
	 * se não printa na tela que o arquivo é invalido.
	 * 
	 * @param	aSerValidado	arquivo que será validado pelo manipulador de arquivos
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
					this.arquivoEstruturaArquivos = aSerValidado;
					this.operacoesEstruturaArq = validados;
					this.arquivosEmDisco = manipulador.getArquivosValidados();
				}
				telaPrincipal.printaNoTerminal(Constantes.arquivoValidado(aSerValidado.getName()), ModuloTelaPrincipal.DARK_GREEN);
			}else{
				invalidaArquivo(tipoArquivo);
				telaPrincipal.printaNoTerminal(Constantes.arquivoNaoValido(aSerValidado.getName()), ModuloTelaPrincipal.RED);	
			}
		} catch (Exception e) {
			invalidaArquivo(tipoArquivo);
			telaPrincipal.printaNoTerminal(Constantes.arquivoNaoValido(aSerValidado.getName()), ModuloTelaPrincipal.RED);	
		} 
	}
	
	
	/**
	 * Função para invalidar qualquer uma das seleções de arquivo.
	 * 
	 * @param	tipoArquivo		tipo do Arquivo, definido pelas static strings
	 * */
	public void invalidaArquivo(String tipoArquivo){
		if(tipoArquivo.equals(ARQ_PROCESSOS)){				
			this.arquivoDeProcessos = null;
			this.processos = null;
		}else{
			this.arquivoEstruturaArquivos = null;
			this.operacoesEstruturaArq = null;
			this.arquivosEmDisco = null;
		}
	}
	
}
