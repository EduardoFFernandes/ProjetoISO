package modules;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import modules.Recursos.RecursoReturn;
import util.Constantes;

public class GerenciadorDeFilas implements Runnable {

	private ArrayList<Processo> processos;
	private ArrayList<Processo> processosIniciais;
	private ArrayList<Operacao> operacoesEstruturaArq;

	private Interface telaPrincipal;
	private Processador processador;
	private Disco discoRigido;
	private Memoria memoriaPrincipal;
	private Recursos recursos;
	private Processos gerenciadorDeProcessos;
	
	/**
	 * Para simplificação do problema: o clock aqui significa tanto a quantidade de quantums ocorridos quanto
	 * o tempo inicial que o processo deve iniciar. Ou seja, o processo inicia assim que um quantum estiver para iniciar.
	 * */
	private int CLOCK;
	private static int QUANTUM = 1000;//milisegundos

	@SuppressWarnings("unchecked")
	public GerenciadorDeFilas(ArrayList<?> processos, ArrayList<?> operacoesEstruturaArq,
			ArrayList<Arquivo> arquivosEmDisco, Interface telaPrincipal, int qtdBlocosDisco) {
		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<Processo>) processos;
		this.processosIniciais = (ArrayList<Processo>) processos;
		this.operacoesEstruturaArq = (ArrayList<Operacao>) operacoesEstruturaArq;
		
		gerenciadorDeProcessos = new Processos(processosIniciais);
		discoRigido = new Disco(qtdBlocosDisco, this, arquivosEmDisco);
		processador = new Processador(this);
		memoriaPrincipal = new Memoria();
		recursos = new Recursos();
		
		CLOCK = 0;

	}

	@Override
	public void run() {

		// inicia o processador -> loop até a fila de processos acabar
		try {
			filaProcessosLoop();
		} catch (InterruptedException e) {
			// A thread do SO foi interrompida por algum motivo
			e.printStackTrace();
		}
		
		// Executa as operações de disco;
		this.escreveNaTela(Constantes.sistemaDeArquivos());
		for(int i = 0; i< this.operacoesEstruturaArq.size();i++) {
			discoRigido.executaOperacao(operacoesEstruturaArq.get(i),i+1);
		}
		discoRigido.printSituacaoDisco();
		
	}

	public void filaProcessosLoop() throws InterruptedException {
		Processo processoAtual = null;
		verificaProcessoInicializandoAgora();
		telaPrincipal.logMessage(Constantes.clock(CLOCK));
		while ((processoAtual = gerenciadorDeProcessos.pegaProximoProcesso()) != null || !processos.isEmpty()) {
			if(processoAtual == null) {
				telaPrincipal.logMessage(Constantes.SEM_PROCESSO_EXECUTAR);
				clockTick();
				continue;
			}
			if(!memoriaPrincipal.isProcessoEmMemoria(processoAtual)){
				if(!memoriaPrincipal.alocaMemoria(processoAtual.getPrioridade()==0, processoAtual)){
					gerenciadorDeProcessos.moveParaFinalDaFila(processoAtual);
					telaPrincipal.logMessage(Constantes.erroMemoria(processoAtual.getPID()),Interface.RED);
					continue;
				}
			}
			if(!processoAtual.isRecursosAlocados()) {
				//se entrou aqui significa que o processo está em memoria mas não teve os seus recursos alocados ainda
				RecursoReturn retorno;
				if((retorno = recursos.alocaTodosOsRecursosParaProcesso(processoAtual)) != RecursoReturn.OK){
					//se não conseguiu alocar os recursos, marca o processo com o recursos que ele bloqueia outro processo, e o coloca na fila do processo
					//bloqueado
					gerenciadorDeProcessos.atualizaProcessoBlocanteComRecurso(processoAtual, recursos.getProcessoFromRecursoError(retorno));
					//move o processo atual para o final da fila
					gerenciadorDeProcessos.moveParaFinalDaFila(processoAtual);
					telaPrincipal.logMessage(Constantes.erroRecursos(processoAtual.getPID()),Interface.RED);
					continue;
				}
			}
			telaPrincipal.logMessage(Constantes.dispatcher(processoAtual));
			telaPrincipal.logMessage(Constantes.executandoProc(processoAtual.getPID()));
			processador.setProcesso(processoAtual);
			processador.executaProcesso();
			
			processoAtual.diminuiTempoProcessador();		
			// Se o processo terminar, libera os recursos
			verificaContinuidadeDoProcesso(processoAtual);	
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
		// verifica aqui se o processo é de tempo real

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
		processos.forEach((pr)->{
			if(pr.getTempoInicializacao()==CLOCK) {//se o processo vai iniciar agora
				if(gerenciadorDeProcessos.adicionaProcesso(pr)) {//tenta adicionar o mesmo às filas de processo
					novaLista.remove(pr);// remove o mesmo dos processos que nao foram inicializados
				}else {//se não conseguiu adicionar, printa na tela
					telaPrincipal.logMessage(Constantes.erroEspacoGerenciadorDeProcessos(pr.getPID()),Interface.RED);
				}
			}
		});
		processos = novaLista;
	}
	
	private void verificaContinuidadeDoProcesso(Processo pr) {
		//se entrou aqui significa que o processo foi executado
		if(pr.getTempoProcessador()<1) {// se o processo acabou
			gerenciadorDeProcessos.removeProcesso(pr);
			memoriaPrincipal.desalocaMemoria(pr);
			recursos.desacolaTodosOsRecursosDoProcesso(pr);
			telaPrincipal.logMessage(Constantes.procFinalizado(pr.getPID()),Interface.DARK_GREEN);
		}else {
			gerenciadorDeProcessos.diminuiPrioridadeProcesso(pr);
		}
	}
	
	synchronized private void clockTick() throws InterruptedException {
		CLOCK++;
		wait(QUANTUM);
		telaPrincipal.logMessage(Constantes.clock(CLOCK));
		verificaProcessoInicializandoAgora();
	}
}

