package modules;

import models.Processo;
import util.Constantes;

public class Semaforo {
	
	private static int SEMAFORO_ABERTO = 0;
	private static int SEMAFORO_FECHADO = 1;
	
	public static void alocaRecurso(Processos gerenciadorDeProcessos, Recursos gerenciadorDeRecursos, Processo processo, Interface telaPrincipal) {
		if (processo.getModem() > 0) {
			if (gerenciadorDeRecursos.getModem() == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setModem(SEMAFORO_FECHADO);// modem alocado para o processoID
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processo);
				gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, gerenciadorDeRecursos.getModem());
				gerenciadorDeProcessos.moveParaFinalDaFila(processo);
				telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
			}
		}
		if (processo.getScanner() > 0) {
			if (gerenciadorDeRecursos.getScanner() == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setScanner(SEMAFORO_FECHADO);// scanner alocado para o processoID
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processo);
				gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, gerenciadorDeRecursos.getScanner());
				gerenciadorDeProcessos.moveParaFinalDaFila(processo);
				telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
			}
		}
		if (processo.getImpressora() == 1) {
			if (gerenciadorDeRecursos.getImpressoras()[0] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setImpressoras(0, SEMAFORO_FECHADO);// impressora 0 alocada para o processoID
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processo);
				gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, gerenciadorDeRecursos.getImpressoras()[0]);
				gerenciadorDeProcessos.moveParaFinalDaFila(processo);
				telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
			}
		} else if (processo.getImpressora() == 2) {
			if (gerenciadorDeRecursos.getImpressoras()[1] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setImpressoras(1, SEMAFORO_FECHADO);// impressora 0 alocada para o processoID
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processo);
				gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, gerenciadorDeRecursos.getImpressoras()[1]);
				gerenciadorDeProcessos.moveParaFinalDaFila(processo);
				telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
			}
		}
		if (processo.getDisco() == 1) {
			if (gerenciadorDeRecursos.getDiscoRigido()[0] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setDiscoRigido(0, SEMAFORO_FECHADO);// sata 0 alocado para o processoID
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processo);
				gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, gerenciadorDeRecursos.getDiscoRigido()[0]);
				gerenciadorDeProcessos.moveParaFinalDaFila(processo);
				telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
			}
		} else if (processo.getDisco() == 2) {
			if (gerenciadorDeRecursos.getDiscoRigido()[1] == SEMAFORO_ABERTO) {
				gerenciadorDeRecursos.setDiscoRigido(1, SEMAFORO_FECHADO);// sata 1 alocado para o processoID
			} else {
				desalocaRecursos(gerenciadorDeRecursos,processo);
				gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, gerenciadorDeRecursos.getDiscoRigido()[1]);
				gerenciadorDeProcessos.moveParaFinalDaFila(processo);
				telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
			}
		}
		processo.setRecursosAlocados(true);
	}

	public static boolean desalocaRecursos(Recursos gerenciadorDeRecursos, Processo processo) {
		if (processo.getModem() > 0 && gerenciadorDeRecursos.getModem() == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setModem(SEMAFORO_ABERTO);// desaloca
		}

		if (processo.getScanner() > 0 && gerenciadorDeRecursos.getScanner() == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setScanner(SEMAFORO_ABERTO);// desaloca
		}

		if (processo.getImpressora() == 1 && gerenciadorDeRecursos.getImpressoras()[0] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setImpressoras(0,SEMAFORO_ABERTO);// desaloca
		} else if (processo.getImpressora() == 2 && gerenciadorDeRecursos.getImpressoras()[1] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setImpressoras(1,SEMAFORO_ABERTO);// desaloca
		}

		if (processo.getDisco() == 1 && gerenciadorDeRecursos.getDiscoRigido()[0] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setDiscoRigido(0,SEMAFORO_ABERTO);// desaloca
		} else if (processo.getDisco() == 2 && gerenciadorDeRecursos.getDiscoRigido()[1] == SEMAFORO_FECHADO) {
			gerenciadorDeRecursos.setDiscoRigido(1,SEMAFORO_ABERTO);// desaloca
		}
		processo.setRecursosAlocados(false);
		return true;
	}
}
