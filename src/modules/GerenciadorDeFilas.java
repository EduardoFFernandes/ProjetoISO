package modules;

import static util.Constantes.INICIO;
import static util.Constantes.INSTRUCAO;
import static util.Constantes.PROCESSO;
import static util.Constantes.RETURN_SIGINT;
import static util.Constantes.SEM_PROCESSO_EXECUTAR;
import static util.Util.dispatcher;
import static util.Util.erroFaltaEspacoProcessos;
import static util.Util.erroMemoria;
import static util.Util.executandoProc;
import static util.Util.procFinalizado;
import static util.Util.resultadoDisco;
import static util.Util.sistemaDeArquivos;

import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;


public class GerenciadorDeFilas extends Thread {
	private ArrayList<Processo> processos;
	private ArrayList<Processo> processosIniciais;
	private ArrayList<Operacao> operacoes;

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
		this.processos = processos;
		this.processosIniciais = processos;
		this.operacoes = operacoes;

		gerenciadorDeProcessos = new Processos(processosIniciais);
		gerenciadorDoDisco = new Disco(qtdBlocosDisco, this, arquivos);
		gerenciadorDoDisco.processaArquivos();
		gerenciadorDaMemoriaPrincipal = new Memoria();
		gerenciadorDeRecursos = new Recursos();
		CLOCK = 0;
	}
	
	@Override
	public void run() {
		try {
			executaFilas();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	synchronized public boolean isProcessoTempoReal(int idProcesso) {
		for (Processo processo : processosIniciais) {
			if (processo.getPID() == idProcesso) {
				if (processo.getPrioridade() == 0)
					return true;
				break;
			}
		}
		return false;
	}
	
	synchronized public void printDispatcher(Processo processo) throws InterruptedException {
		telaPrincipal.logMessage(PROCESSO + processo.getPID() + INICIO);
		for (int i = 1; i <= 3; i++) {
			telaPrincipal.logMessage(PROCESSO + processo.getPID() + INSTRUCAO + i);
		}
		wait(300);
		telaPrincipal.logMessage(PROCESSO + processo.getPID() + RETURN_SIGINT);
	}
	
	public void executaFilas() throws InterruptedException {
		Processo processo = null;
		tempoInicializacaoProcessos();
		telaPrincipal.logMessage("\n");
		while ((processo = gerenciadorDeProcessos.proximoProcesso()) != null || !processos.isEmpty()) {
			if (processo == null) {
				telaPrincipal.logMessage(SEM_PROCESSO_EXECUTAR);
				clock();
				continue;
			}
			if (!gerenciadorDaMemoriaPrincipal.getProcessos().contains(processo)) {
				if (!gerenciadorDaMemoriaPrincipal.aloca(processo.getPrioridade() == 0, processo)) {
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
			tempoProcesso(processo);
			clock();
		}
		telaPrincipal.logMessage(sistemaDeArquivos());
		gerenciadorDoDisco.executaOperacoes(operacoes, telaPrincipal);
		resultadoDisco(telaPrincipal,gerenciadorDoDisco);
	}
	
	public boolean isProcessoValido(int PID) {
		for (Processo processo : processosIniciais) {
			if (processo.getPID() == PID) {
				return true;
			}
		}

		return false;
	}
	
	public ArrayList<Processo> getProcessos() {
		return processos;
	}

	public void setProcessos(ArrayList<Processo> processos) {
		this.processos = processos;
	}

	public ArrayList<Processo> getProcessosIniciais() {
		return processosIniciais;
	}

	public void setProcessosIniciais(ArrayList<Processo> processosIniciais) {
		this.processosIniciais = processosIniciais;
	}

	public ArrayList<Operacao> getOperacoesEstruturaArq() {
		return operacoes;
	}

	public void setOperacoesEstruturaArq(ArrayList<Operacao> operacoesEstruturaArq) {
		this.operacoes = operacoesEstruturaArq;
	}

	public Interface getTelaPrincipal() {
		return telaPrincipal;
	}

	public void setTelaPrincipal(Interface telaPrincipal) {
		this.telaPrincipal = telaPrincipal;
	}

	public Disco getGerenciadorDoDisco() {
		return gerenciadorDoDisco;
	}

	public void setGerenciadorDoDisco(Disco gerenciadorDoDisco) {
		this.gerenciadorDoDisco = gerenciadorDoDisco;
	}

	public Memoria getGerenciadorDaMemoriaPrincipal() {
		return gerenciadorDaMemoriaPrincipal;
	}

	public void setGerenciadorDaMemoriaPrincipal(Memoria gerenciadorDaMemoriaPrincipal) {
		this.gerenciadorDaMemoriaPrincipal = gerenciadorDaMemoriaPrincipal;
	}

	public Recursos getGerenciadorDeRecursos() {
		return gerenciadorDeRecursos;
	}

	public void setGerenciadorDeRecursos(Recursos gerenciadorDeRecursos) {
		this.gerenciadorDeRecursos = gerenciadorDeRecursos;
	}

	public Processos getGerenciadorDeProcessos() {
		return gerenciadorDeProcessos;
	}

	public void setGerenciadorDeProcessos(Processos gerenciadorDeProcessos) {
		this.gerenciadorDeProcessos = gerenciadorDeProcessos;
	}

	/**
	 * Verifica a inicializacao dos processos, se estiver no tempo do clock correto, adiciona o processo ao gerenciador de Processos
	 */
	private void tempoInicializacaoProcessos() {
		ArrayList<Processo> processosAux = new ArrayList<>();
		processosAux.addAll(processos);
		processos.forEach((processo) -> {
			if (processo.getTempoInicializacao() == CLOCK) {
				if (gerenciadorDeProcessos.adicionaProcesso(processo)) {
					processosAux.remove(processo);
				} else {
					telaPrincipal.logMessage(erroFaltaEspacoProcessos(processo.getPID()),
							Interface.RED);
				}
			}
		});
		processos = processosAux;
	}
	
	/**
	 * Verifica se o tempo do processo chegou a 0, nesse caso ele remove o processo da logica da aplicacao, caso nao seja,
	 * o processo perde prioridade para os outros.
	 */
	private void tempoProcesso(Processo processo) {
		if (processo.getTempo() == 0) {
			gerenciadorDeProcessos.removeProcesso(processo);
			gerenciadorDaMemoriaPrincipal.desaloca(processo);
			Semaforo.desalocaRecursos(gerenciadorDeRecursos, processo);
			telaPrincipal.logMessage(procFinalizado(processo.getPID()), Interface.GREEN);
		} else {
			gerenciadorDeProcessos.diminuiPrioridade(processo);
		}
	}
	
	synchronized private void clock() throws InterruptedException {
		CLOCK++;
		wait(1000);
		telaPrincipal.logMessage("\n");
		tempoInicializacaoProcessos();
	}

}
