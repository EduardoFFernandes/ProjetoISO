package modulos;

import java.util.concurrent.Semaphore;

import models.ProcessoVO;

public class ModuloCPU implements Runnable {

	private Semaphore bloqueiaSO;
	private ModuloTelaPrincipal tela;
	private ProcessoVO processo;
	
	ModuloCPU(String nome,int uid,Semaphore bloqueiaSO,ModuloTelaPrincipal tela) {
		this.bloqueiaSO = bloqueiaSO;
		this.tela = tela;
	}

	@Override
	public void run() {
		try {
			bloqueiaSO.acquire();
			tela.printaNoTerminal(processo.getPID()+ ": inst 1");
			wait(300);
			tela.printaNoTerminal(processo.getPID()+ ": inst 2");
			wait(300);
			tela.printaNoTerminal(processo.getPID()+ ": inst 3");
			wait(300);
			bloqueiaSO.release();
			this.finalize();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void setProcesso(ProcessoVO p){
		this.processo = p ;
		
	}
}
