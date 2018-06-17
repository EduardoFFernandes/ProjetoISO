package modulos;

import java.util.ArrayList;
import java.util.LinkedList;

import models.ProcessoVO;

public class ModuloProcessos {
	

	LinkedList<ProcessoVO> filaProcessosProntos;
	
	LinkedList<ProcessoVO> filaTempoReal;
	LinkedList<ProcessoVO> filaProcessosUsuario;
	

	LinkedList<ProcessoVO> filaProcessosUsuarioPrioridade1;
	LinkedList<ProcessoVO> filaProcessosUsuarioPrioridade2;
	LinkedList<ProcessoVO> filaProcessosUsuarioPrioridade3;
	
	ProcessoVO processoEmExecucao;
	
//	public ModuloProcessos(ArrayList<ProcessoVO> processos) {
	public ModuloProcessos() {
		
		inicializaFilas();
		
//		processos.forEach((pr)->{
//			adicionaProcesso(pr);
//		});
		
	}
	
	
	private void inicializaFilas() {
		filaProcessosUsuarioPrioridade1 = new LinkedList<ProcessoVO>();
		filaProcessosUsuarioPrioridade2 = new LinkedList<ProcessoVO>();
		filaProcessosUsuarioPrioridade3 = new LinkedList<ProcessoVO>();
		
		filaProcessosUsuario = new LinkedList<ProcessoVO>();
		filaTempoReal = new LinkedList<ProcessoVO>();
		
		filaProcessosProntos = new LinkedList<ProcessoVO>();
	}
	
	public ProcessoVO pegaProximoProcesso() {
		atualizaFilaPrioridades();
		atualizaFilaUsuario();
		atualizaFilaProcessosProntos();
		
		processoEmExecucao = filaProcessosProntos.removeFirst();
		
		return processoEmExecucao;
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
		
//		if(pr.getPrioridade()==0) {
//			filaTempoReal.remove(pr);
//			return true;
//		}else if(pr.getPrioridade()==1) {
//			filaProcessosUsuarioPrioridade1.remove(pr);
//			return true;
//		}else if(pr.getPrioridade()==2) {
//			filaProcessosUsuarioPrioridade2.remove(pr);
//			return true;
//		}else if(pr.getPrioridade()==3) {
//			filaProcessosUsuarioPrioridade3.remove(pr);
//			return true;
//		}
		this.processoEmExecucao = null;
	}
	
	private void atualizaFilaPrioridades() {
		
	}
	
	private void atualizaFilaUsuario() {
		
	}
	
	private void atualizaFilaProcessosProntos() {
		
	}
	
	public void atualizaPrioridadeProcessos() {
		
	}
}



