package modulos;

import models.Processo;

public class ModuloCPU {
	private ModuloSO listenerSO;
	private Processo processo;
	
	private static String PROCESSO = "Processo ";
	private static String INSTRUCAO =  " Instrução ";
	
	ModuloCPU(ModuloSO listenerSO) {
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
