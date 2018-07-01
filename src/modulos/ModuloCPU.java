package modulos;

import models.ProcessoVO;

public class ModuloCPU {
	private ModuloTelaPrincipal tela;
	private ProcessoVO processo;
	
	ModuloCPU(String nome,int uid,ModuloTelaPrincipal tela) {
		this.tela = tela;
	}
	
	synchronized public void executaProcesso() throws InterruptedException {
		tela.printaNoTerminal("P"+processo.getPID()+ " STARTED");
		tela.printaNoTerminal("P"+processo.getPID()+ " instruction 1");
		wait(300);
		tela.printaNoTerminal("P"+processo.getPID()+ " instruction 2");
		wait(300);
		tela.printaNoTerminal("P"+processo.getPID()+ " instruction 3");
		wait(400);
		tela.printaNoTerminal("P"+processo.getPID()+ " return SIGINT");
	}
	
	
	public void setProcesso(ProcessoVO p){
		this.processo = p ;
		
	}
}
