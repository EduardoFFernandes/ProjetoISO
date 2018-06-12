package gerenciador;

import java.util.concurrent.Semaphore;

import models.Gerenciador;
import models.ProcessoVO;

public class ModuloCPU extends Gerenciador {

	private Semaphore bloqueiaSO;
	
	ModuloCPU(String nome,int uid,Semaphore bloqueiaSO) {
		super(nome, uid);
		this.bloqueiaSO = bloqueiaSO;
	}

	@Override
	public void run() {
		
		
	}
	
	
	public void setProcesso(ProcessoVO p){
		
	}
}
