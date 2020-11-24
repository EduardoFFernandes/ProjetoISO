package modules;

import static util.Constantes.SEM_PROCESSO_EXECUTAR;
import static util.Util.dispatcher;
import static util.Util.processoInfo;
import static util.Util.erroFaltaEspacoProcessos;
import static util.Util.erroMemoria;
import static util.Util.processoExecutado;
import static util.Util.processoFinalizado;
import static util.Util.resultadoDisco;
import static util.Util.sistemaDeArquivos;

import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;

public class Filas extends Thread {
	private Interface terminal;
	private Disco gerenciadorDoDisco;
	private Memoria gerenciadorDaMemoriaPrincipal;
	private Recursos gerenciadorDeRecursos;
	private Processos gerenciadorDeProcessos;
	private ArrayList<Processo> lstProcessos; // Essa lista serve para comparacoes
	private ArrayList<Processo> processos; // Essa lista vai ser manipulavel
	private ArrayList<Operacao> operacoes;
	private int clock;

	public Filas(ArrayList<Processo> lstProcessos, ArrayList<Operacao> operacoes, ArrayList<Arquivo> arquivos,
			Interface terminal, int blocosDisco) {
		gerenciadorDeProcessos = new Processos(lstProcessos);
		gerenciadorDoDisco = new Disco(blocosDisco, this, arquivos);
		gerenciadorDoDisco.processaArquivos();
		gerenciadorDaMemoriaPrincipal = new Memoria();
		gerenciadorDeRecursos = new Recursos();
		this.setTerminal(terminal);
		this.setLstProcessos(lstProcessos);

		this.setProcessos(lstProcessos);
		this.setOperacoes(operacoes);
		;
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
		for (Processo processo : processos) {
			if (processo.getPID() == idProcesso) {
				if (processo.getPrioridade() == 0)
					return true;
				break;
			}
		}
		return false;
	}

	/**
	 * Metodo que controla o fluxo de acoes do sistema operacional.
	 */
	public void executaFilas() throws InterruptedException {
		clock = 0;
		Processo processo = null;
		tempoInicializacaoProcessos();
		terminal.logMessage("\n");
		while ((processo = gerenciadorDeProcessos.proximoProcesso()) != null || !lstProcessos.isEmpty()) {
			if (processo == null) {
				terminal.logMessage((clock + 1) + " º clock, " + SEM_PROCESSO_EXECUTAR  );
				clock();
				continue;
			}
			if (!gerenciadorDaMemoriaPrincipal.getProcessos().contains(processo)) {
				if (!gerenciadorDaMemoriaPrincipal.aloca(processo.getPrioridade() == 0, processo)) {
					gerenciadorDeProcessos.ultimoProcessoFila(processo);
					terminal.logMessage(erroMemoria(processo.getPID()));
					continue;
				}
			}
			if (!processo.isRecursosAlocados()) {
				Semaforo.alocaRecurso(gerenciadorDeProcessos, gerenciadorDeRecursos, processo, terminal);
				continue;
			}
			terminal.logMessage(dispatcher(processo));
			terminal.logMessage(processoExecutado(processo.getPID()));
			processoInfo(terminal,processo);
			processo.setTempo(processo.getTempo() - 1);
			tempoProcesso(processo);
			clock();
		}
		terminal.logMessage(sistemaDeArquivos());
		gerenciadorDoDisco.executaOperacoes(operacoes, terminal);
		resultadoDisco(terminal, gerenciadorDoDisco);
	}

	public boolean isProcessoValido(int PID) {
		for (Processo processo : processos) {
			if (processo.getPID() == PID) {
				return true;
			}
		}

		return false;
	}

	public ArrayList<Processo> getLstProcessos() {
		return lstProcessos;
	}

	public void setLstProcessos(ArrayList<Processo> processos) {
		this.lstProcessos = processos;
	}

	public void setProcessos(ArrayList<Processo> processosIniciais) {
		this.processos = processosIniciais;
	}

	public ArrayList<Operacao> getOperacoesEstruturaArq() {
		return operacoes;
	}

	public void setOperacoes(ArrayList<Operacao> operacoesEstruturaArq) {
		this.operacoes = operacoesEstruturaArq;
	}

	public Interface getTerminal() {
		return terminal;
	}

	public void setTerminal(Interface terminal) {
		this.terminal = terminal;
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
	 * Verifica a inicializacao dos processos, se estiver no tempo do clock correto,
	 * adiciona o processo ao gerenciador de Processos
	 */
	private void tempoInicializacaoProcessos() {
		ArrayList<Processo> processosAux = new ArrayList<>();
		processosAux.addAll(lstProcessos);
		lstProcessos.forEach((processo) -> {
			if (processo.getTempoInicializacao() == clock) {
				if (gerenciadorDeProcessos.adicionaProcesso(processo)) {
					processosAux.remove(processo);
				} else {
					terminal.logMessage(erroFaltaEspacoProcessos(processo.getPID()));
				}
			}
		});
		lstProcessos = processosAux;
	}

	/**
	 * Verifica se o tempo do processo chegou a 0, nesse caso ele remove o processo
	 * da logica da aplicacao, caso nao seja, o processo perde prioridade para os
	 * outros.
	 */
	private void tempoProcesso(Processo processo) {
		if (processo.getTempo() == 0) {
			gerenciadorDeProcessos.removeProcesso(processo);
			gerenciadorDaMemoriaPrincipal.desaloca(processo);
			Semaforo.desalocaRecursos(gerenciadorDeRecursos, processo);
			terminal.logMessage(processoFinalizado(processo.getPID()));
		} else {
			gerenciadorDeProcessos.diminuiPrioridade(processo);
		}
	}

	synchronized private void clock() throws InterruptedException {
		clock++;
		wait(1000);
		terminal.logMessage("\n");
		tempoInicializacaoProcessos();
	}

}
