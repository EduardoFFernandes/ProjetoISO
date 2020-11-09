package modules;

import models.Processo;
import util.Constantes;

/**
 * Por padrão todo recurso é inicializado com o SEMAFORO_ABERTO o que indica que ele está livre para ser alocado.
 * @author Dudu
 */
public class Semaforo {
	
	private static int SEMAFORO_ABERTO = 0;
	private static int SEMAFORO_FECHADO = 1;
	
	
	/**
	 * Essa função implementa a lógica de semáforo, atribui o recurso ao processamento e 
	 * bloqueia aquele recurso temporariamente para os outros processamentos. Aqui o semaforo fica fechado.
	 * @author Dudu
	 */
	public static void alocaRecurso(Processos gerenciadorDeProcessos, Recursos gerenciadorDeRecursos, Processo processoAtual, Interface telaPrincipal) {
		if (processoAtual.getModem() > 0) {
			if (gerenciadorDeRecursos.getModem() == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setModem(SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcesso(processoAtual);
				telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
			}
		}
		if (processoAtual.getScanner() > 0) {
			if (gerenciadorDeRecursos.getScanner() == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setScanner(SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcesso(processoAtual);
				telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
			}
		}
		if (processoAtual.getImpressora() == 1) {
			if (gerenciadorDeRecursos.getImpressoras()[0] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setImpressoras(0, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcesso(processoAtual);
				telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
			}
		} else if (processoAtual.getImpressora() == 2) {
			if (gerenciadorDeRecursos.getImpressoras()[1] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setImpressoras(1, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcesso(processoAtual);
				telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
			}
		}
		if (processoAtual.getDisco() == 1) {
			if (gerenciadorDeRecursos.getDiscoRigido()[0] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setDiscoRigido(0, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcesso(processoAtual);
				telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
			}
		} else if (processoAtual.getDisco() == 2) {
			if (gerenciadorDeRecursos.getDiscoRigido()[1] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setDiscoRigido(1, SEMAFORO_FECHADO);
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processoAtual);
				gerenciadorDeProcessos.atualizaProcesso(processoAtual);
				gerenciadorDeProcessos.ultimoProcesso(processoAtual);
				telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
			}
		}
		processoAtual.setRecursosAlocados(true);
	}
	
	/**
	 * Essa função implementa a lógica de semáforo, retira o recurso do processamento para libera-lo para os outros. Aqui o semaforo fica aberto.
	 * @author Dudu
	 */
	public static boolean desalocaRecursos(Recursos gerenciadorDeRecursos, Processo processoAtual) {
		if (processoAtual.getModem() > 0 && gerenciadorDeRecursos.getModem() == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setModem(SEMAFORO_ABERTO);
		}

		if (processoAtual.getScanner() > 0 && gerenciadorDeRecursos.getScanner() == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setScanner(SEMAFORO_ABERTO);
		}

		if (processoAtual.getImpressora() == 1 && gerenciadorDeRecursos.getImpressoras()[0] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setImpressoras(0,SEMAFORO_ABERTO);
		} else if (processoAtual.getImpressora() == 2 && gerenciadorDeRecursos.getImpressoras()[1] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setImpressoras(1,SEMAFORO_ABERTO);
		}

		if (processoAtual.getDisco() == 1 && gerenciadorDeRecursos.getDiscoRigido()[0] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setDiscoRigido(0,SEMAFORO_ABERTO);
		} else if (processoAtual.getDisco() == 2 && gerenciadorDeRecursos.getDiscoRigido()[1] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setDiscoRigido(1,SEMAFORO_ABERTO);
		}
		processoAtual.setRecursosAlocados(false);
		return true;
	}
}
