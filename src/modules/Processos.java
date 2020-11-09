package modules;

import java.util.ArrayList;

import models.Processo;

/**
 *  Essa classe controla a prioridade das filas dos processos(Eu gosto de pensar que sao processamentos), esta ligado a
 *  logica do semáforo também, quando a competicao de recursos o processo vai perdendo prioridade, conforme os clocks passam
 *  os processos vao subindo de prioridade.
 */
public class Processos {
	ArrayList<Processo> processosIniciais;
	ArrayList<Processo> filaTempoReal;
	ArrayList<Processo> filaPrioridade1;
	ArrayList<Processo> filaPrioridade2;
	ArrayList<Processo> filaPrioridade3;
	ArrayList<ArrayList<Processo>> filas;
	private static int TAMANHO_MAXIMO = 1000;
	
	public Processos(ArrayList<Processo> processosIniciais) {
		this.processosIniciais = processosIniciais;
		filaPrioridade1 = new ArrayList<Processo>();
		filaPrioridade2 = new ArrayList<Processo>();
		filaPrioridade3 = new ArrayList<Processo>();

		filaTempoReal = new ArrayList<Processo>();
	}
	/**
	 * Reune todos os processos com todas as prioridades em uma lista e retorta o proximo processo.
	 */
	public Processo proximoProcesso() {
		ArrayList<Processo> filaProcessosProntos = new ArrayList<>();
		filaProcessosProntos.addAll(filaTempoReal);
		filaProcessosProntos.addAll(filaPrioridade1);
		filaProcessosProntos.addAll(filaPrioridade2);
		filaProcessosProntos.addAll(filaPrioridade3);

		return filaProcessosProntos.isEmpty() ? null : filaProcessosProntos.get(0);

	}
	/**
	 * Adiciona o processo para a fila correspondente a sua prioridade
	 */
	public boolean adicionaProcesso(Processo processo) {
		if (processo.getPrioridade() == 0) {
			if (verificaTamanho(filaTempoReal)) {
				filaTempoReal.add(processo);
				return true;
			}
			return false;
		} else if (processo.getPrioridade() == 1) {
			if (verificaTamanho(filaPrioridade1)) {
				filaPrioridade1.add(processo);
				return true;
			}
			return false;
		} else if (processo.getPrioridade() == 2) {
			if (verificaTamanho(filaPrioridade2)) {
				filaPrioridade2.add(processo);
				return true;
			}
			return false;
		} else if (processo.getPrioridade() == 3) {
			if (verificaTamanho(filaPrioridade3)) {
				filaPrioridade3.add(processo);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Remove o processo para a fila correspondente a sua prioridade
	 */
	public void removeProcesso(Processo processo) {

		if (processo.getPrioridade() == 0) {
			filaTempoReal.remove(processo);
		} else if (processo.getPrioridade() == 1) {
			filaPrioridade1.remove(processo);
		} else if (processo.getPrioridade() == 2) {
			filaPrioridade2.remove(processo);
		} else if (processo.getPrioridade() == 3) {
			filaPrioridade3.remove(processo);
		}
	}
	
	/**
	 * Aumenta a prioridade do processo.
	 */
	public void aumentaPrioridade(Processo processo) {
		if (processo.getPrioridade() == 0 || processo.getPrioridade() == 1) {
			return;
		} else if (processo.getPrioridade() == 2) {
			filaPrioridade2.remove(processo);
			processo.setPrioridade(processo.getPrioridade() - 1);
			filaPrioridade1.add(processo);
		} else if (processo.getPrioridade() == 3) {
			filaPrioridade3.remove(processo);
			processo.setPrioridade(processo.getPrioridade() - 1);
			filaPrioridade2.add(processo);
		}
	}
	
	/**
	 * Diminui a prioridade do processo.
	 */
	public void diminuiPrioridade(Processo processo) {
		if(processo.isPossuiRecursoBlocante()) {
			return;
		}
		if (processo.getPrioridade() == 0) {
			filaTempoReal.remove(processo);
			filaTempoReal.add(processo);
		} else if (processo.getPrioridade() == 1) {
			filaPrioridade1.remove(processo);
			processo.setPrioridade(processo.getPrioridade() + 1);
			filaPrioridade2.add(processo);
		} else if (processo.getPrioridade() == 2) {
			filaPrioridade2.remove(processo);
			processo.setPrioridade(processo.getPrioridade() + 1);
			filaPrioridade3.add(processo);
		} else if (processo.getPrioridade() == 3) {// move o processo para o final da fila
			filaPrioridade3.remove(processo);
			filaPrioridade3.add(processo);
		}
	}
	
	/**
	 * O processo vira o ultimo da fila de acordo com a sua prioridade.
	 */
	public void ultimoProcesso(Processo processo) {
		if (processo.getPrioridade() == 0) {
			filaTempoReal.remove(0);
			filaTempoReal.add(processo);
		} else if (processo.getPrioridade() == 1) {
			filaPrioridade1.remove(processo);
			filaPrioridade1.add(processo);
		} else if (processo.getPrioridade() == 2) {
			filaPrioridade2.remove(processo);
			filaPrioridade2.add(processo);
		} else if (processo.getPrioridade() == 3) {
			filaPrioridade3.remove(processo);
			filaPrioridade3.add(processo);
		}
	}
	
	/**
	 * Atualiza 
	 */
	public void atualizaProcesso(Processo processoBloqueado) {
		Processo processoBlocante = null;
		for (Processo processo : processosIniciais) {
			if (processo.getPID() == processoBloqueado.getPID()) {
				processoBlocante = processo;
				break;
			}
		}
		if (processoBlocante == null) {
			return;
		}

		processoBlocante.setRecursoBlocante(true);
		
		if(processoBloqueado.getPrioridade() == 1) {
			removeProcesso(processoBlocante);
			filaPrioridade1.add(0, processoBlocante);
			processoBlocante.setPrioridade(processoBloqueado.getPrioridade());
		} 
		if(processoBloqueado.getPrioridade() == 2) {
			removeProcesso(processoBlocante);
			filaPrioridade2.add(0, processoBlocante);
			processoBlocante.setPrioridade(processoBloqueado.getPrioridade());
		} 
	}
	
	public boolean verificaTamanho (ArrayList<Processo> fila) {
		return fila.size() < TAMANHO_MAXIMO;
	}
}
