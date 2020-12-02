package modules;

import java.util.ArrayList;
import static util.Constantes.TAMANHO_MAXIMO;

import models.Processo;

/**
 * Essa classe controla a prioridade das filas dos processos, esta ligado a
 * logica do semaforo tambem, quando a competicao de recursos o processo vai
 * perdendo prioridade, conforme os clocks passam os processos vao subindo de
 * prioridade.
 */
public class Processos {
    ArrayList<Processo> filaProcessos;
    ArrayList<ArrayList<Processo>> filas;

    public Processos() {
    	super();
    }

    /**
     * Reune todos os processos com todas as prioridades em uma lista e retorta o
     * proximo processo.
     */
    public Processo proximoProcesso() {
        for (ArrayList<Processo> fila : filas) {
            if (!fila.isEmpty()) {
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
     * Diminui a prioridade do processo.
     */
    public void diminuiPrioridade(Processo processo) {
        if (processo.recursoBloqueado()) {
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
     * Atualiza o processo bloqueado trazendo ele para o primeiro da fila.
     */
    public void atualizaProcesso(Processo processoBloqueado) {
        for (Processo processo : filaProcessos) {
            if (processo.getPID() == processoBloqueado.getPID()) {

                processo.setRecursoBlocante(true);

                if (processoBloqueado.getPrioridade() == 1) {
                    removeProcesso(processo);
                    filas.get(1).add(0, processo);
                    processo.setPrioridade(processoBloqueado.getPrioridade());
                }
                if (processoBloqueado.getPrioridade() == 2) {
                    removeProcesso(processo);
                    filas.get(2).add(0, processo);
                    processo.setPrioridade(processoBloqueado.getPrioridade());
                }
            }
        }
        return;
    }

    /**
     * O processo vira o ultimo da fila de acordo com a sua prioridade.
     */
    public void ultimoProcessoFila(Processo processo) {
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
     * Remove o processo da sua a fila.
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

    private boolean verificaTamanho(ArrayList<Processo> fila) {
        return fila.size() < TAMANHO_MAXIMO;
    }

	public ArrayList<ArrayList<Processo>> getFilas() {
		return filas;
	}

	public void setFilas(ArrayList<ArrayList<Processo>> filas) {
		this.filas = filas;
	}

	public ArrayList<Processo> getFilaProcessos() {
		return filaProcessos;
	}

	public void setFilaProcessos(ArrayList<Processo> filaProcessos) {
		this.filaProcessos = filaProcessos;
	}
}
