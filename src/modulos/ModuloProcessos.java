package modulos;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import models.ProcessoVO;
import modulos.ModuloRecursos.RecursoReturn;

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
		} catch (NoSuchElementException e) {
			return null;
		}

	}

	public boolean adicionaProcesso(ProcessoVO pr) {

		if (pr.getPrioridade() == 0) {
			if (filaTempoReal.size() < 1000) {
				filaTempoReal.add(pr);
				return true;
			}
			return false;
		} else if (pr.getPrioridade() == 1) {
			if (filaProcessosUsuarioPrioridade1.size() < 1000) {
				filaProcessosUsuarioPrioridade1.add(pr);
				return true;
			}
			return false;
		} else if (pr.getPrioridade() == 2) {
			if (filaProcessosUsuarioPrioridade2.size() < 1000) {
				filaProcessosUsuarioPrioridade2.add(pr);
				return true;
			}
			return false;
		} else if (pr.getPrioridade() == 3) {
			if (filaProcessosUsuarioPrioridade3.size() < 1000) {
				filaProcessosUsuarioPrioridade3.add(pr);
				return true;
			}
		}

		return false;
	}

	public void removeProcesso(ProcessoVO pr) {

		if (pr.getPrioridade() == 0) {
			filaTempoReal.remove(pr);
		} else if (pr.getPrioridade() == 1) {
			filaProcessosUsuarioPrioridade1.remove(pr);
		} else if (pr.getPrioridade() == 2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
		} else if (pr.getPrioridade() == 3) {
			filaProcessosUsuarioPrioridade3.remove(pr);
		}
	}

	public void diminuiPrioridadeProcesso(ProcessoVO pr) {
		if (pr.getPrioridade() == 0) {// move o processo para o final da fila
			filaTempoReal.remove(pr);
			filaTempoReal.add(pr);
		} else if (pr.getPrioridade() == 1) {
			filaProcessosUsuarioPrioridade1.remove(pr);
			pr.setPrioridade(pr.getPrioridade() + 1);
			filaProcessosUsuarioPrioridade2.add(pr);
		} else if (pr.getPrioridade() == 2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
			pr.setPrioridade(pr.getPrioridade() + 1);
			filaProcessosUsuarioPrioridade3.add(pr);
		} else if (pr.getPrioridade() == 3) {// move o processo para o final da
												// fila
			filaProcessosUsuarioPrioridade3.remove(pr);
			filaProcessosUsuarioPrioridade3.add(pr);
		}
	}

	public void aumentaPrioridadeProcesso(ProcessoVO pr) {
		if (pr.getPrioridade() == 0 || pr.getPrioridade() == 1) {
			return;
		} else if (pr.getPrioridade() == 2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
			pr.setPrioridade(pr.getPrioridade() - 1);
			filaProcessosUsuarioPrioridade1.add(pr);
		} else if (pr.getPrioridade() == 3) {
			filaProcessosUsuarioPrioridade3.remove(pr);
			pr.setPrioridade(pr.getPrioridade() - 1);
			filaProcessosUsuarioPrioridade2.add(pr);
		}
	}

	// TODO verificar se o processo esta realmente indo para o final da fila
	public void moveParaFinalDaFila(ProcessoVO pr) {
		if (pr.getPrioridade() == 0) {// move o processo para o final da fila
			filaTempoReal.removeFirst();
			filaTempoReal.addLast(pr);
		} else if (pr.getPrioridade() == 1) {
			filaProcessosUsuarioPrioridade1.remove(pr);
			filaProcessosUsuarioPrioridade1.addLast(pr);
		} else if (pr.getPrioridade() == 2) {
			filaProcessosUsuarioPrioridade2.remove(pr);
			filaProcessosUsuarioPrioridade2.addLast(pr);
		} else if (pr.getPrioridade() == 3) {
			filaProcessosUsuarioPrioridade3.remove(pr);
			filaProcessosUsuarioPrioridade3.addLast(pr);
		}
	}

	public void atualizaProcessoBlocanteComRecurso(ProcessoVO processoBloqueado, int processoId) {
		ProcessoVO processoBlocante = null;
		for (ProcessoVO pr : filaProcessosProntos) {
			if (pr.getPID() == processoId) {
				processoBlocante = pr;
				break;
			}
		}
		if (processoBlocante == null) {
			return;
		}

		switch (processoBloqueado.getPrioridade()) {
		case 1:
			removeDaFilaAtual(processoBlocante);
			filaProcessosUsuarioPrioridade1.add(processoBloqueado);
			break;
		case 2:
			removeDaFilaAtual(processoBlocante);
			filaProcessosUsuarioPrioridade2.add(processoBloqueado);
			break;
		default:
			break;
		}
	}

	private void removeDaFilaAtual(ProcessoVO pr) {
		switch (pr.getPrioridade()) {
		case 0:
			filaTempoReal.remove(pr);
			break;
		case 1:
			filaProcessosUsuarioPrioridade1.remove(pr);
			break;
		case 2:
			filaProcessosUsuarioPrioridade2.remove(pr);
			break;
		case 3:
			filaProcessosUsuarioPrioridade3.remove(pr);
			break;
		default:
			break;
		}
	}
}
