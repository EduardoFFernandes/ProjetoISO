package main;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import models.ArquivoVO;
import models.Constantes;
import modulos.ModuloSO;
import util.ManipuladorDeArquivos;
import view.DispatcherWindow;

/**
 * Classe Inicial do programa
 * 
 * @author tulio.matias		-	06/06/2018
 * 
 * */
public class Main {
	private static DispatcherWindow telaPrincipal;
	private File arquivoDeProcessos = null;
	private File arquivoEstruturaArquivos = null;

	private ArrayList<?> processos;
	private ArrayList<?> operacoesEstruturaArq;
	private ArrayList<ArquivoVO> arquivosEmDisco;
	
	private ManipuladorDeArquivos manipulador;
	
	public static final String ARQ_PROCESSOS = "Processos";
	public static final String ARQ_ESTRUTURA_ARQUIVOS = "Arquivos";
	public static final String INICIAR_SO = "iniciarSO";
	
	private Thread soThread;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Main main = new Main();
					telaPrincipal = new DispatcherWindow(main);
					telaPrincipal.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public void closeWindow(){
		telaPrincipal.dispatchEvent(new WindowEvent(telaPrincipal, WindowEvent.WINDOW_CLOSING));
	}

	public void iniciarSO(){
		if(arquivoDeProcessos == null){
			telaPrincipal.printaNoTerminal(Constantes.NAO_SELECIONADO_ARQ_PROCESSOS.getTexto(),DispatcherWindow.RED);
			return;
		}
		if(arquivoEstruturaArquivos == null){
			telaPrincipal.printaNoTerminal(Constantes.NAO_SELECIONADO_ARQ_ARQUIVOS.getTexto(),DispatcherWindow.RED);
			return;
		}
		if(arquivoDeProcessos.equals(arquivoEstruturaArquivos)){
			telaPrincipal.printaNoTerminal("Arquivos Iguais.",DispatcherWindow.RED);//TODO: retirar a string daqui.
			return;
		}
		telaPrincipal.printaNoTerminal("Iniciando SO...",DispatcherWindow.DARK_GREEN);//TODO: retirar a string daqui.
		
		ModuloSO SO = new ModuloSO("SO",0, processos, operacoesEstruturaArq, arquivosEmDisco, telaPrincipal, manipulador.getQtdBlocosDisco());
		soThread = new Thread(SO);
		soThread.setDaemon(true);
		soThread.start();
	}
	
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
				telaPrincipal.printaNoTerminal(Constantes.arquivoValidado(aSerValidado.getName()), DispatcherWindow.DARK_GREEN);
			}else{
				invalidaArquivo(tipoArquivo);
				telaPrincipal.printaNoTerminal(Constantes.arquivoNaoValido(aSerValidado.getName()), DispatcherWindow.RED);	
			}
		} catch (Exception e) {
			invalidaArquivo(tipoArquivo);
			telaPrincipal.printaNoTerminal(Constantes.arquivoNaoValido(aSerValidado.getName()), DispatcherWindow.RED);	
		} 
	}
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
