package modulos;

import models.ProcessoVO;

public class ModuloCPU {
	private ModuloSO listenerSO;
	private ProcessoVO processo;
	
	ModuloCPU(String nome,int uid,ModuloSO listenerSO) {
		this.listenerSO = listenerSO;
	}
	
	synchronized public void executaProcesso() throws InterruptedException {
		listenerSO.escreveNaTela("P"+processo.getPID()+ " STARTED");
		listenerSO.escreveNaTela("P"+processo.getPID()+ " instruction 1");
		wait(300);
		listenerSO.escreveNaTela("P"+processo.getPID()+ " instruction 2");
		wait(300);
		listenerSO.escreveNaTela("P"+processo.getPID()+ " instruction 3");
		wait(400);
		listenerSO.escreveNaTela("P"+processo.getPID()+ " return SIGINT");
	}
	
	
	public void setProcesso(ProcessoVO p){
		this.processo = p ;
		
	}
}
