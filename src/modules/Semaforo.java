package modules;

import static util.Util.erroRecursos;

import models.Processo;

/**
 * Por padrao todo recurso e inicializado com o SEMAFORO_ABERTO o que indica que
 * ele esta livre para ser alocado.
 * 
 * 
 */
public class Semaforo {

	private static int SEMAFORO_ABERTO = 0;
	private static int SEMAFORO_FECHADO = 1;

	/**
	 * Esse metodo implementa a logica de semaforo, atribui o recurso ao
	 * processamento e bloqueia aquele recurso temporariamente para os outros
	 * processamentos. Aqui o semaforo fica fechado.
	 * 
	 * 
	 */
	public static void alocaRecurso(Processos gerenciadorDeProcessos, Recursos gerenciadorDeRecursos,
			Processo processoAtual, Interface terminal) {
		if (processoAtual.getModem() > 0) {
			if (gerenciadorDeRecursos.getModem() == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setModem(SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos, processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcessoFila(processoAtual);
				terminal.logMessage(erroRecursos(processoAtual.getPID()));
			}
		}
		if (processoAtual.getScanner() > 0) {
			if (gerenciadorDeRecursos.getScanner() == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setScanner(SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos, processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcessoFila(processoAtual);
				terminal.logMessage(erroRecursos(processoAtual.getPID()));
			}
		}
		if (processoAtual.getImpressora() == 1) {
			if (gerenciadorDeRecursos.getImpressoras()[0] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setImpressoras(0, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos, processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcessoFila(processoAtual);
				terminal.logMessage(erroRecursos(processoAtual.getPID()));
			}
		} else if (processoAtual.getImpressora() == 2) {
			if (gerenciadorDeRecursos.getImpressoras()[1] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setImpressoras(1, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos, processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcessoFila(processoAtual);
				terminal.logMessage(erroRecursos(processoAtual.getPID()));
			}
		}
		if (processoAtual.getDisco() == 1) {
			if (gerenciadorDeRecursos.getDiscoRigido()[0] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setDiscoRigido(0, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos, processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcessoFila(processoAtual);
				terminal.logMessage(erroRecursos(processoAtual.getPID()));
			}
		} else if (processoAtual.getDisco() == 2) {
			if (gerenciadorDeRecursos.getDiscoRigido()[1] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setDiscoRigido(1, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos, processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcessoFila(processoAtual);
				terminal.logMessage(erroRecursos(processoAtual.getPID()));
			}
		}
		processoAtual.setRecursosAlocados(true);
	}

	/**
	 * Esse metodo implementa a logica de sem�foro, retira o recurso do
	 * processamento para libera-lo para os outros. Aqui o semaforo fica aberto.
	 */
	public static boolean desalocaRecursos(Recursos gerenciadorDeRecursos, Processo processoAtual) {
		if (processoAtual.getModem() > 0 && gerenciadorDeRecursos.getModem() == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setModem(SEMAFORO_ABERTO);
		}

		if (processoAtual.getScanner() > 0 && gerenciadorDeRecursos.getScanner() == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setScanner(SEMAFORO_ABERTO);
		}

		if (processoAtual.getImpressora() == 1 && gerenciadorDeRecursos.getImpressoras()[0] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setImpressoras(0, SEMAFORO_ABERTO);
		} else if (processoAtual.getImpressora() == 2
				&& gerenciadorDeRecursos.getImpressoras()[1] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setImpressoras(1, SEMAFORO_ABERTO);
		}

		if (processoAtual.getDisco() == 1 && gerenciadorDeRecursos.getDiscoRigido()[0] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setDiscoRigido(0, SEMAFORO_ABERTO);
		} else if (processoAtual.getDisco() == 2 && gerenciadorDeRecursos.getDiscoRigido()[1] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setDiscoRigido(1, SEMAFORO_ABERTO);
		}
		processoAtual.setRecursosAlocados(false);
		return true;
	}
}
