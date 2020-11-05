package modules;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import modules.Recursos.RecursoReturn;
import util.Constantes;

public class GerenciadorDeFilas extends Thread{

	private ArrayList<Processo> processos;
	private ArrayList<Processo> processosIniciais;
	private ArrayList<Operacao> operacoesEstruturaArq;

	private Interface telaPrincipal;
	private Disco discoRigido;
	private Memoria memoriaPrincipal;
	private Recursos recursos;
	private Processos gerenciadorDeProcessos;
	
	/**
	 * Para simplificar o problema: o clock aqui significa tanto a quantidade de quantums ocorridos quanto
	 * o tempo inicial que o processo deve iniciar. Ou seja, o processo inicia assim que um quantum estiver para iniciar.
	 * */
	private int CLOCK;
	private static int QUANTUM = 1000; //milisegundos

	@SuppressWarnings("unchecked")
	public GerenciadorDeFilas(ArrayList<?> processos, ArrayList<?> operacoes,
			ArrayList<Arquivo> arquivosEmDisco, Interface telaPrincipal, int qtdBlocosDisco) {
		
		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<Processo>) processos;
		this.processosIniciais = (ArrayList<Processo>) processos;
		this.operacoesEstruturaArq = (ArrayList<Operacao>) operacoes;
		this.setDaemon(true);
		
		gerenciadorDeProcessos = new Processos(processosIniciais);
		discoRigido = new Disco(qtdBlocosDisco, this, arquivosEmDisco);
		memoriaPrincipal = new Memoria();
		recursos = new Recursos();
		CLOCK = 0;

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
		for(int i = 0; i< this.operacoesEstruturaArq.size();i++) {
			discoRigido.executaOperacao(operacoesEstruturaArq.get(i),i+1);
		}
		discoRigido.printSituacaoDisco();
		
	}

	public void fila() throws InterruptedException {
		Processo processo = null;
		verificaProcessoInicializandoAgora();
		telaPrincipal.logMessage(Constantes.clock(CLOCK));
		while ((processo = gerenciadorDeProcessos.pegaProximoProcesso()) != null || !processos.isEmpty()) {
			if(processo == null) {
				telaPrincipal.logMessage(Constantes.SEM_PROCESSO_EXECUTAR);
				clockTick();
				continue;
			}
			if(!memoriaPrincipal.isProcessoEmMemoria(processo)){
				if(!memoriaPrincipal.alocaMemoria(processo.getPrioridade()==0, processo)){
					gerenciadorDeProcessos.moveParaFinalDaFila(processo);
					telaPrincipal.logMessage(Constantes.erroMemoria(processo.getPID()),Interface.RED);
					continue;
				}
			}
			if(!processo.isRecursosAlocados()) {
				//se entrou aqui significa que o processo esta em memoria mas nao teve os seus recursos alocados ainda
				RecursoReturn retorno;
				if((retorno = recursos.alocaRecurso(processo)) != RecursoReturn.OK){
					//se nao conseguiu alocar os recursos, marca o processo com o recursos que ele bloqueia outro processo, e o coloca na fila do processo
					//bloqueado
					gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processo, recursos.getProcessoFromRecursoError(retorno));
					//move o processo atual para o final da fila
					gerenciadorDeProcessos.moveParaFinalDaFila(processo);
					telaPrincipal.logMessage(Constantes.erroRecursos(processo.getPID()),Interface.RED);
					continue;
				}
			}
			telaPrincipal.logMessage(Constantes.dispatcher(processo));
			telaPrincipal.logMessage(Constantes.executandoProc(processo.getPID()));
			printDispatcher(processo);
			processo.diminuiTempoProcessador();		
			// Se o processo terminar, libera os recursos
			verificaContinuidadeDoProcesso(processo);	
			clockTick();
		}
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
		processos.forEach((processo)->{
			if(processo.getTempoInicializacao()==CLOCK) {//se o processo vai iniciar agora
				if(gerenciadorDeProcessos.adicionaProcesso(processo)) {//tenta adicionar o mesmo as filas de processo
					novaLista.remove(processo);// remove o mesmo dos processos que nao foram inicializados
				}else {//se nao conseguiu adicionar, printa na tela
					telaPrincipal.logMessage(Constantes.erroEspacoGerenciadorDeProcessos(processo.getPID()),Interface.RED);
				}
			}
		});
		processos = novaLista;
	}
	
	private void verificaContinuidadeDoProcesso(Processo processo) {
		//se entrou aqui significa que o processo foi executado
		if(processo.getTempoProcessador()<1) {// se o processo acabou
			gerenciadorDeProcessos.removeProcesso(processo);
			memoriaPrincipal.desalocaMemoria(processo);
			recursos.desalocaRecursos(processo);
			telaPrincipal.logMessage(Constantes.procFinalizado(processo.getPID()),Interface.GREEN);
		}else {
			gerenciadorDeProcessos.diminuiPrioridadeProcesso(processo);
		}
	}
	
	synchronized private void clockTick() throws InterruptedException {
		CLOCK++;
		wait(QUANTUM);
		telaPrincipal.logMessage(Constantes.clock(CLOCK));
		verificaProcessoInicializandoAgora();
	}
	
	synchronized public void printDispatcher(Processo processo) throws InterruptedException {
		escreveNaTela(Constantes.PROCESSO + processo.getPID()+ Constantes.INICIO);
		for(int i=1; i<=3; i++) {
			escreveNaTela(Constantes.PROCESSO + processo.getPID()+ Constantes.INSTRUCAO + i);
		}
		wait(300);
		escreveNaTela(Constantes.PROCESSO + processo.getPID()+ Constantes.RETURN_SIGINT);
	}
}

