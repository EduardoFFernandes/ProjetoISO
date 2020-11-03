package modules;

import models.Processo;

public class Processador {
	private GerenciadorDeFilas listenerSO;
	private Processo processo;
	
	private static String PROCESSO = "Processo ";
	private static String INSTRUCAO =  " Instru��o ";
	
	Processador(GerenciadorDeFilas listenerSO) {
		this.listenerSO = listenerSO;
	}
	
	synchronized public void executaProcesso() throws InterruptedException {
		listenerSO.escreveNaTela(PROCESSO + processo.getPID()+ " Inicio");
		
		for(int i=1; i<=3; i++) {
			listenerSO.escreveNaTela(PROCESSO + processo.getPID()+ INSTRUCAO + i);
			wait(300);
		}
		wait(400);
		listenerSO.escreveNaTela(PROCESSO + processo.getPID()+ " return SIGINT");
	}
	
	
	public void setProcesso(Processo processo){
		this.processo = processo ;
		
	}
}