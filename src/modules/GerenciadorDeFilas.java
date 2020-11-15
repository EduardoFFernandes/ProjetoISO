package modules;

import static util.Constantes.INICIO;
import static util.Constantes.INSTRUCAO;
import static util.Constantes.PROCESSO;
import static util.Constantes.RETURN_SIGINT;
import static util.Constantes.SEM_PROCESSO_EXECUTAR;
import static util.Constantes.NEWLINE;
import static util.Constantes.QUANTUM;
import static util.Util.dispatcher;
import static util.Util.erroEspacoGerenciadorDeProcessos;
import static util.Util.erroMemoria;
import static util.Util.executandoProc;
import static util.Util.operacoesDoSistema;
import static util.Util.procFinalizado;
import static util.Util.sistemaDeArquivos;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;


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

	public GerenciadorDeFilas(ArrayList<Processo> processos, ArrayList<Operacao> operacoes, ArrayList<Arquivo> arquivos,
			Interface telaPrincipal, int qtdBlocosDisco) {

		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<Processo>) processos;
		this.processosIniciais = (ArrayList<Processo>) processos;
		this.operacoesEstruturaArq = (ArrayList<Operacao>) operacoes;
		this.setDaemon(true);

		gerenciadorDeProcessos = new Processos(processosIniciais);
		gerenciadorDoDisco = new Disco(qtdBlocosDisco, this, arquivos);
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
		escreveNaTela(PROCESSO + processo.getPID() + INICIO);
		for (int i = 1; i <= 3; i++) {
			escreveNaTela(PROCESSO + processo.getPID() + INSTRUCAO + i);
		}
		wait(300);
		escreveNaTela(PROCESSO + processo.getPID() + RETURN_SIGINT);
	}
	
	@Override
	public void run() {

		try {
			fila();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.escreveNaTela(sistemaDeArquivos());
		for (int i = 0; i < this.operacoesEstruturaArq.size(); i++) {
			escreveNaTela(operacoesDoSistema(i + 1));
			gerenciadorDoDisco.executaOperacao(operacoesEstruturaArq.get(i));
		}
		gerenciadorDoDisco.printSituacaoDisco();

	}

	public void fila() throws InterruptedException {
		Processo processo = null;
		verificaProcessoInicializandoAgora();
		telaPrincipal.logMessage(NEWLINE);
		while ((processo = gerenciadorDeProcessos.proximoProcesso()) != null || !processos.isEmpty()) {
			if (processo == null) {
				telaPrincipal.logMessage(SEM_PROCESSO_EXECUTAR);
				clockTick();
				continue;
			}
			if (!gerenciadorDaMemoriaPrincipal.isProcessoEmMemoria(processo)) {
				if (!gerenciadorDaMemoriaPrincipal.alocaMemoria(processo.getPrioridade() == 0, processo)) {
					gerenciadorDeProcessos.ultimoProcessoFila(processo);
					telaPrincipal.logMessage(erroMemoria(processo.getPID()), Interface.RED);
					continue;
				}
			}
			if (!processo.isRecursosAlocados()) {
				Semaforo.alocaRecurso(gerenciadorDeProcessos, gerenciadorDeRecursos, processo, telaPrincipal);
				continue;
			}
			telaPrincipal.logMessage(dispatcher(processo));
			telaPrincipal.logMessage(executandoProc(processo.getPID()));
			printDispatcher(processo);
			processo.setTempo(processo.getTempo() - 1);
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
					telaPrincipal.logMessage(erroEspacoGerenciadorDeProcessos(processo.getPID()),
							Interface.RED);
				}
			}
		});
		processos = novaLista;
	}

	private void verificaContinuidadeDoProcesso(Processo processo) {
		// se entrou aqui significa que o processo foi executado
		if (processo.getTempo() < 1) {// se o processo acabou
			gerenciadorDeProcessos.removeProcesso(processo);
			gerenciadorDaMemoriaPrincipal.desalocaMemoria(processo);
			Semaforo.desalocaRecursos(gerenciadorDeRecursos, processo);
			telaPrincipal.logMessage(procFinalizado(processo.getPID()), Interface.GREEN);
		} else {
			gerenciadorDeProcessos.diminuiPrioridade(processo);
		}
	}
}
