package modulos;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import models.ProcessoVO;

public class ModuloProcessos {
	

	LinkedList<ProcessoVO> filaProcessosProntos;
	
	LinkedList<ProcessoVO> filaTempoReal;

	LinkedList<ProcessoVO> filaProcessosUsuarioPrioridade1;
	LinkedList<ProcessoVO> filaProcessosUsuarioPrioridade2;
	LinkedList<ProcessoVO> filaProcessosUsuarioPrioridade3;
	
	public ModuloProcessos() {
		filaProcessosUsuarioPrioridade1 = new LinkedList<ProcessoVO>();
		filaProcessosUsuarioPrioridade2 = new LinkedList<ProcessoVO>();
		filaProcessosUsuarioPrioridade3 = new LinkedList<ProcessoVO>();
		
		filaTempoReal = new LinkedList<ProcessoVO>();
	}
	

	public ProcessoVO pegaProximoProcesso() {
		filaProcessosProntos = new LinkedList<>();
		filaProcessosProntos.addAll(filaTempoReal);
		filaProcessosProntos.addAll(filaProcessosUsuarioPrioridade1);
		filaProcessosProntos.addAll(filaProcessosUsuarioPrioridade2);
		filaProcessosProntos.addAll(filaProcessosUsuarioPrioridade3);
		
		
		try {
			return filaProcessosProntos.getFirst();
		}catch (NoSuchElementException e) {
			return null;
		}
		
		
	}
	
	public boolean adicionaProcesso(ProcessoVO pr) {
		
		if(pr.getPrioridade()==0) {
			filaTempoReal.add(pr);
			return true;
		}else if(pr.getPrioridade()==1) {
			filaProcessosUsuarioPrioridade1.add(pr);
			return true;
		}else if(pr.getPrioridade()==2) {
			filaProcessosUsuarioPrioridade2.add(pr);
			return true;
		}else if(pr.getPrioridade()==3) {
			filaProcessosUsuarioPrioridade3.add(pr);
			return true;
		}
		
		return false;
	}
	public void removeProcesso(ProcessoVO pr) {
		
		if(pr.getPrioridade()==0) {
			filaTempoReal.remove(pr);
		}else if(pr.getPrioridade()==1) {
			filaProcessosUsuarioPrioridade1.remove(pr);
		}else if(pr.getPrioridade()==2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
		}else if(pr.getPrioridade()==3) {
			filaProcessosUsuarioPrioridade3.remove(pr);
		}
	}
	
	//somente processos de usuario entram nesse metodo
	public void atualizaProcesso(ProcessoVO pr) {
		if(pr.getPrioridade()==0) {//move o processo para o final da fila
			filaTempoReal.remove(pr);
			filaTempoReal.add(pr);
		}else if(pr.getPrioridade()==1) {
			filaProcessosUsuarioPrioridade1.remove(pr);
			pr.setPrioridade(pr.getPrioridade()+1);
			filaProcessosUsuarioPrioridade2.add(pr);
		}else if(pr.getPrioridade()==2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
			pr.setPrioridade(pr.getPrioridade()+1);
			filaProcessosUsuarioPrioridade3.add(pr);
		}else if(pr.getPrioridade()==3) {//move o processo para o final da fila
			filaProcessosUsuarioPrioridade3.remove(pr);
			filaProcessosUsuarioPrioridade3.add(pr);
		}
	}
	
	public void moveParaFinalDaFila(ProcessoVO pr) {
		if(pr.getPrioridade()==0) {//move o processo para o final da fila
			filaTempoReal.remove(pr);
			filaTempoReal.add(pr);
		}else if(pr.getPrioridade()==1) {
			filaProcessosUsuarioPrioridade1.remove(pr);
			filaProcessosUsuarioPrioridade1.add(pr);
		}else if(pr.getPrioridade()==2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
			filaProcessosUsuarioPrioridade2.add(pr);
		}else if(pr.getPrioridade()==3) {
			filaProcessosUsuarioPrioridade3.remove(pr);
			filaProcessosUsuarioPrioridade3.add(pr);
		}
	}
}



