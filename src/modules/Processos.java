package modules;

import java.util.ArrayList;

import models.Processo;

/**
 *  Essa classe controla a prioridade das filas dos processos(Eu gosto de pensar que sao processamentos), esta ligado a
 *  logica do semáforo também, quando a competicao de recursos o processo vai perdendo prioridade, conforme os clocks passam
 *  os processos vao subindo de prioridade.
 */
public class Processos {
	ArrayList<Processo> filaProcessos;
	ArrayList<ArrayList<Processo>> filas;
	
	private static int TAMANHO_MAXIMO = 1000;
	
	public Processos(ArrayList<Processo> filaProcessos) {
		this.filaProcessos = filaProcessos;
		this.filas = new ArrayList<ArrayList<Processo>>();
		for (int i = 0; i <= 3; i++) {
			filas.add(new ArrayList<Processo>());
		}
	}
	/**
	 * Reune todos os processos com todas as prioridades em uma lista e retorta o proximo processo.
	 */
	public Processo proximoProcesso() {
		for (ArrayList<Processo> fila : filas) {
			if(!fila.isEmpty()) {
				return fila.get(0);
			}
		}
		return null;
	}
	/**
	 * Adiciona o processo para a fila correspondente a sua prioridade
	 */
	public boolean adicionaProcesso(Processo processo) {
		if (processo.getPrioridade() == 0) {
			if (verificaTamanho(filas.get(0))) {
				filas.get(0).add(processo);
				return true;
			}
			return false;
		} else if (processo.getPrioridade() == 1) {
			if (verificaTamanho(filas.get(1))) {
				filas.get(1).add(processo);
				return true;
			}
			return false;
		} else if (processo.getPrioridade() == 2) {
			if (verificaTamanho(filas.get(2))) {
				filas.get(2).add(processo);
				return true;
			}
			return false;
		} else if (processo.getPrioridade() == 3) {
			if (verificaTamanho(filas.get(3))) {
				filas.get(3).add(processo);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Aumenta a prioridade do processo.
	 */
	public void aumentaPrioridade(Processo processo) {
		if (processo.getPrioridade() == 0 || processo.getPrioridade() == 1) {
			return;
		} else if (processo.getPrioridade() == 2) {
			filas.get(2).remove(processo);
			processo.setPrioridade(processo.getPrioridade() - 1);
			filas.get(1).add(processo);
		} else if (processo.getPrioridade() == 3) {
			filas.get(3).remove(processo);
			processo.setPrioridade(processo.getPrioridade() - 1);
			filas.get(2).remove(processo);
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
			filas.get(0).add(processo);
			filas.get(0).remove(processo);
		} else if (processo.getPrioridade() == 1) {
			filas.get(1).remove(processo);
			processo.setPrioridade(processo.getPrioridade() + 1);
			filas.get(2).remove(processo);
		} else if (processo.getPrioridade() == 2) {
			filas.get(2).remove(processo);
			processo.setPrioridade(processo.getPrioridade() + 1);
			filas.get(3).add(processo);
		} else if (processo.getPrioridade() == 3) {
			filas.get(3).remove(processo);
			filas.get(3).add(processo);
		}
	}
	
	/**
	 * Atualiza 
	 */
	public void atualizaProcesso(Processo processoBloqueado) {
		Processo processoBlocante = null;
		for (Processo processo : filaProcessos) {
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
			filas.get(1).add(0, processoBlocante);
			processoBlocante.setPrioridade(processoBloqueado.getPrioridade());
		} 
		if(processoBloqueado.getPrioridade() == 2) {
			removeProcesso(processoBlocante);
			filas.get(2).add(0, processoBlocante);
			processoBlocante.setPrioridade(processoBloqueado.getPrioridade());
		} 
	}
	
	/**
	 * O processo vira o ultimo da fila de acordo com a sua prioridade.
	 */
	public void ultimoProcesso(Processo processo) {
		if (processo.getPrioridade() == 0) {
			filas.get(0).remove(0);
			filas.get(0).add(processo);
		} else if (processo.getPrioridade() == 1) {
			filas.get(1).remove(processo);
			filas.get(1).add(processo);
		} else if (processo.getPrioridade() == 2) {
			filas.get(2).remove(processo);
			filas.get(2).add(processo);
		} else if (processo.getPrioridade() == 3) {
			filas.get(3).remove(processo);
			filas.get(3).remove(processo);
		}
	}
	
	/**
	 * Remove o processo para a fila correspondente a sua prioridade
	 */
	public void removeProcesso(Processo processo) {

		if (processo.getPrioridade() == 0) {
			filas.get(0).remove(processo);
		} else if (processo.getPrioridade() == 1) {
			filas.get(1).remove(processo);
		} else if (processo.getPrioridade() == 2) {
			filas.get(2).remove(processo);
		} else if (processo.getPrioridade() == 3) {
			filas.get(3).remove(processo);
		}
	}
	
	public boolean verificaTamanho (ArrayList<Processo> fila) {
		return fila.size() < TAMANHO_MAXIMO;
	}
}
