package modulos;

import java.awt.Color;
import java.util.ArrayList;

import models.ArquivoVO;
import models.Constantes;
import models.OperacaoNaEstruturaArquivosVO;
import models.ProcessoVO;

public class ModuloSO implements Runnable {

	private ArrayList<ProcessoVO> processos;
	private ArrayList<ProcessoVO> processosIniciais;
	private ArrayList<OperacaoNaEstruturaArquivosVO> operacoesEstruturaArq;

	private volatile ModuloTelaPrincipal telaPrincipal;
	private volatile ModuloCPU CPU0;
	private ModuloDisco HD1;
	private ModuloMemoria RAM;
	private ModuloRecursos REC;
	private ModuloProcessos gerenciadorDeFilas;
	
	private int CLOCK;

	@SuppressWarnings("unchecked")
	public ModuloSO(ArrayList<?> processos, ArrayList<?> operacoesEstruturaArq,
			ArrayList<ArquivoVO> arquivosEmDisco, ModuloTelaPrincipal telaPrincipal, int qtdBlocosDisco) {
		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<ProcessoVO>) processos;
		this.processosIniciais = (ArrayList<ProcessoVO>) processos;
		this.operacoesEstruturaArq = (ArrayList<OperacaoNaEstruturaArquivosVO>) operacoesEstruturaArq;
		
		gerenciadorDeFilas = new ModuloProcessos();
		HD1 = new ModuloDisco(qtdBlocosDisco, this, arquivosEmDisco);
		CPU0 = new ModuloCPU("CPU0",1,this);
		RAM = new ModuloMemoria();
		REC = new ModuloRecursos();
		
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
		for(int i = 0; i< this.operacoesEstruturaArq.size();i++) {
			HD1.executaOperacao(operacoesEstruturaArq.get(i),i+1);
		}
		HD1.printSituacaoDisco();
		
	}

	public void filaProcessosLoop() throws InterruptedException {
		ProcessoVO processoAtual = null;
		verificaProcessoInicializandoAgora();
		telaPrincipal.printaNoTerminal(Constantes.printaClock(CLOCK));
		while ((processoAtual = gerenciadorDeFilas.pegaProximoProcesso()) != null || !processos.isEmpty()) {
			if(processoAtual == null) {
				telaPrincipal.printaNoTerminal(Constantes.SEM_PROC_EXEC.getTexto());
				clockTick();
				continue;
			}
			if(!RAM.isProcessoEmMemoria(processoAtual)){
				if(!RAM.alocaMemoria(processoAtual.getPrioridade()==0, processoAtual)){
					gerenciadorDeFilas.moveParaFinalDaFila(processoAtual);
					telaPrincipal.printaNoTerminal(Constantes.faltaRAM(processoAtual.getPID()),ModuloTelaPrincipal.RED);
//					clockTick();
					continue;
				}
			}
			if(!processoAtual.isRecursosAlocados()) {
				//se entrou aqui significa que o processo está em memoria mas não teve os seus recursos alocados ainda
				if(!REC.alocaTodosOsRecursosParaProcesso(processoAtual)){
					gerenciadorDeFilas.moveParaFinalDaFila(processoAtual);
					telaPrincipal.printaNoTerminal(Constantes.faltaRecursos(processoAtual.getPID()),ModuloTelaPrincipal.RED);
					clockTick();
					continue;
				}
			}
			//TODO: printar na tela aqui o `dispatcher=>`
			telaPrincipal.printaNoTerminal(Constantes.dispatcher(processoAtual));
			telaPrincipal.printaNoTerminal(Constantes.executandoProc(processoAtual.getPID()));
			CPU0.setProcesso(processoAtual);
			CPU0.executaProcesso();
			
			processoAtual.diminuiTempoProcessador();		
			// Se o processo terminar, libera os recursos
			verificaContinuidadeDoProcesso(processoAtual);	
			clockTick();
		}
	}

	synchronized public void escreveNaTela(String toPrint, Color cor) {
		telaPrincipal.printaNoTerminal(toPrint, cor);
	}

	synchronized public void escreveNaTela(String toPrint) {
		telaPrincipal.printaNoTerminal(toPrint);
	}

	synchronized public boolean isProcessoTempoReal(int idProcesso) {
		// verifica aqui se o processo é de tempo real

		for (ProcessoVO processo : processosIniciais) {
			if (processo.getPID() == idProcesso) {
				if (processo.getPrioridade() == 0)
					return true;
				break;
			}
		}
		return false;
	}
	public boolean isProcessoValido(int PID) {
		for (ProcessoVO processo : processosIniciais) {
			if (processo.getPID() == PID) {
				return true;
			}
		}
		
		return false;
	}
	private void verificaProcessoInicializandoAgora() {
		ArrayList<ProcessoVO> novaLista = new ArrayList<>(processos);
		processos.forEach((pr)->{
			if(pr.getTempoInicializacao()==CLOCK) {//se o processo vai iniciar agora
				if(gerenciadorDeFilas.adicionaProcesso(pr)) {//tenta adicionar o mesmo às filas de processo
					novaLista.remove(pr);// remove o mesmo dos processos que nao foram inicializados
				}else {//se não conseguiu adicionar, printa na tela
					telaPrincipal.printaNoTerminal(Constantes.faltaEspacoGerenciadorDeProcessos(pr.getPID()),ModuloTelaPrincipal.RED);
				}
			}
		});
		processos = novaLista;
	}
	
	private void verificaContinuidadeDoProcesso(ProcessoVO pr) {
		//se entrou aqui significa que o processo foi executado
		if(pr.getTempoProcessador()<1) {// se o processo acabou
			gerenciadorDeFilas.removeProcesso(pr);
			RAM.desalocaMemoria(pr);
			REC.desacolaTodosOsRecursosDoProcesso(pr);
			telaPrincipal.printaNoTerminal(Constantes.procFinalizado(pr.getPID()),ModuloTelaPrincipal.DARK_GREEN);
		}else {
			gerenciadorDeFilas.atualizaProcesso(pr);
		}
	}
	
	synchronized private void clockTick() throws InterruptedException {
		CLOCK++;
		wait(1000);
		telaPrincipal.printaNoTerminal(Constantes.printaClock(CLOCK));
		verificaProcessoInicializandoAgora();
	}
}

