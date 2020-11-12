package modules;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import util.Constantes;

public class GerenciadorDeFilas extends Thread {

	private ArrayList<Processo> processos;
	private ArrayList<Processo> processosIniciais;
	private ArrayList<Operacao> operacoesEstruturaArq;

	private Interface telaPrincipal;
	private Disco gerenciadorDoDisco;
	private Memoria gerenciadorDaMemoriaPrincipal;
	private Recursos gerenciadorDeRecursos;
	private Processos gerenciadorDeProcessos;

	/**
	 * Para simplificar o problema: o clock aqui significa tanto a quantidade de
	 * quantums ocorridos quanto o tempo inicial que o processo deve iniciar. Ou
	 * seja, o processo inicia assim que um quantum estiver para iniciar.
	 */
	private int CLOCK;
	private static int QUANTUM = 1000; // milisegundos

	@SuppressWarnings("unchecked")
	public GerenciadorDeFilas(ArrayList<?> processos, ArrayList<?> operacoes, ArrayList<Arquivo> arquivosEmDisco,
			Interface telaPrincipal, int qtdBlocosDisco) {

		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<Processo>) processos;
		this.processosIniciais = (ArrayList<Processo>) processos;
		this.operacoesEstruturaArq = (ArrayList<Operacao>) operacoes;
		this.setDaemon(true);

		gerenciadorDeProcessos = new Processos(processosIniciais);
		gerenciadorDoDisco = new Disco(qtdBlocosDisco, this, arquivosEmDisco);
		gerenciadorDaMemoriaPrincipal = new Memoria();
		gerenciadorDeRecursos = new Recursos();
		CLOCK = 0;
	}
	
	synchronized public void escreveNaTela(String toPrint, Color cor) {
		telaPrincipal.logMessage(toPrint, cor);
	}

	synchronized public void escreveNaTela(String toPrint) {
		telaPrincipal.logMessage(toPrint);
	}

	synchronized public boolean isProcessoTempoReal(int idProcesso) {
		// verifica aqui se o processo e de tempo real

		for (Processo processo : processosIniciais) {
			if (processo.getPID() == idProcesso) {
				if (processo.getPrioridade() == 0)
					return true;
				break;
			}
		}
		return false;
	}
	
	synchronized private void clockTick() throws InterruptedException {
		CLOCK++;
		wait(QUANTUM);
		telaPrincipal.logMessage("\n");
		verificaProcessoInicializandoAgora();
	}

	synchronized public void printDispatcher(Processo processo) throws InterruptedException {
		escreveNaTela(Constantes.PROCESSO + processo.getPID() + Constantes.INICIO);
		for (int i = 1; i <= 3; i++) {
			escreveNaTela(Constantes.PROCESSO + processo.getPID() + Constantes.INSTRUCAO + i);
		}
		wait(300);
		escreveNaTela(Constantes.PROCESSO + processo.getPID() + Constantes.RETURN_SIGINT);
	}
	
	@Override
	public void run() {

		// inicia o processador -> loop ate a fila de processos acabar
		try {
			fila();
		} catch (InterruptedException e) {
			// A thread do SO foi interrompida por algum motivo
			e.printStackTrace();
		}

		// Executa as operacoes de disco;
		this.escreveNaTela(Constantes.sistemaDeArquivos());
		for (int i = 0; i < this.operacoesEstruturaArq.size(); i++) {
			gerenciadorDoDisco.executaOperacao(operacoesEstruturaArq.get(i), i + 1);
		}
		gerenciadorDoDisco.printSituacaoDisco();

	}

	public void fila() throws InterruptedException {
		Processo processo = null;
		verificaProcessoInicializandoAgora();
		telaPrincipal.logMessage("\n");
//		TODO Modificar a forma que os recursos sao compartilhados.
		while ((processo = gerenciadorDeProcessos.proximoProcesso()) != null || !processos.isEmpty()) {
			if (processo == null) {
				telaPrincipal.logMessage(Constantes.SEM_PROCESSO_EXECUTAR);
				clockTick();
				continue;
			}
			if (!gerenciadorDaMemoriaPrincipal.isProcessoEmMemoria(processo)) {
				if (!gerenciadorDaMemoriaPrincipal.alocaMemoria(processo.getPrioridade() == 0, processo)) {
					gerenciadorDeProcessos.ultimoProcessoFila(processo);
					telaPrincipal.logMessage(Constantes.erroMemoria(processo.getPID()), Interface.RED);
					continue;
				}
			}
			if (!processo.isRecursosAlocados()) {
				Semaforo.alocaRecurso(gerenciadorDeProcessos, gerenciadorDeRecursos, processo, telaPrincipal);
				continue;
			}
			telaPrincipal.logMessage(Constantes.dispatcher(processo));
			telaPrincipal.logMessage(Constantes.executandoProc(processo.getPID()));
			printDispatcher(processo);
			processo.setTempoProcessador(processo.getTempoProcessador() - 1);
			// Se o processo terminar, libera os recursos
			verificaContinuidadeDoProcesso(processo);
			clockTick();
		}
	}
	
	public boolean isProcessoValido(int PID) {
		for (Processo processo : processosIniciais) {
			if (processo.getPID() == PID) {
				return true;
			}
		}

		return false;
	}

	private void verificaProcessoInicializandoAgora() {
		ArrayList<Processo> novaLista = new ArrayList<>(processos);
		processos.forEach((processo) -> {
			if (processo.getTempoInicializacao() == CLOCK) {// se o processo vai iniciar agora
				if (gerenciadorDeProcessos.adicionaProcesso(processo)) {// tenta adicionar o mesmo as filas de processo
					novaLista.remove(processo);// remove o mesmo dos processos que nao foram inicializados
				} else {// se nao conseguiu adicionar, printa na tela
					telaPrincipal.logMessage(Constantes.erroEspacoGerenciadorDeProcessos(processo.getPID()),
							Interface.RED);
				}
			}
		});
		processos = novaLista;
	}

	private void verificaContinuidadeDoProcesso(Processo processo) {
		// se entrou aqui significa que o processo foi executado
		if (processo.getTempoProcessador() < 1) {// se o processo acabou
			gerenciadorDeProcessos.removeProcesso(processo);
			gerenciadorDaMemoriaPrincipal.desalocaMemoria(processo);
			Semaforo.desalocaRecursos(gerenciadorDeRecursos, processo);
			telaPrincipal.logMessage(Constantes.procFinalizado(processo.getPID()), Interface.GREEN);
		} else {
			gerenciadorDeProcessos.diminuiPrioridade(processo);
		}
	}
}
