package modulos;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import models.ArquivoVO;
import models.OperacaoNaEstruturaArquivosVO;
import models.ProcessoVO;

public class ModuloSO implements Runnable {

	private ArrayList<ProcessoVO> processos;
	private ArrayList<OperacaoNaEstruturaArquivosVO> operacoesEstruturaArq;

	private volatile ModuloTelaPrincipal telaPrincipal;
	private volatile ModuloCPU CPU0;
	private ModuloDisco HD1;
	private ModuloMemoria RAM;
	private ModuloRecursos REC;
	private ModuloProcessos gerenciadorDeFilas;
	
	private int CLOCK;
	
	private Thread threadCPU;
	Semaphore esperaCPU;

	@SuppressWarnings("unchecked")
	public ModuloSO(ArrayList<?> processos, ArrayList<?> operacoesEstruturaArq,
			ArrayList<ArquivoVO> arquivosEmDisco, ModuloTelaPrincipal telaPrincipal, int qtdBlocosDisco) {
		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<ProcessoVO>) processos;
		this.operacoesEstruturaArq = (ArrayList<OperacaoNaEstruturaArquivosVO>) operacoesEstruturaArq;
		
		HD1 = new ModuloDisco(qtdBlocosDisco, this, arquivosEmDisco);
		gerenciadorDeFilas = new ModuloProcessos();
		CPU0 = new ModuloCPU("CPU0",1,esperaCPU,telaPrincipal);
		RAM = new ModuloMemoria();
		REC = new ModuloRecursos();
		
		CLOCK = 0;

		esperaCPU = new Semaphore(1);
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
		this.operacoesEstruturaArq.forEach(op -> {
			HD1.executaOperacao(op);
		});
		HD1.printSituacaoDisco();
		
	}

	public void filaProcessosLoop() throws InterruptedException {
		ProcessoVO processoAtual = null;
		verificaProcessoInicializandoAgora();
		while ((processoAtual = gerenciadorDeFilas.pegaProximoProcesso()) != null || !processos.isEmpty()) {
			if(processoAtual == null) {
				//TODO:printa que não existem processos no gerenciador de filas mas nem todos os processos foram inicializados
				clockTick();
				continue;
			}
			if(!RAM.isProcessoEmMemoria(processoAtual)){//Significa que é a primeira vez que processo roda no processador
				if(!RAM.alocaMemoria(processoAtual.getPrioridade()==0, processoAtual)){
					//TODO:printa na tela que o processo não pode ser alocado por falta de RAM
					clockTick();
					continue;
				}
				//aloca recursos
				if(!REC.alocaTodosOsRecursosParaProcesso(processoAtual)){
					//TODO:printa na tela que o processo não pode ser alocado por falta de recursos
					clockTick();
					continue;
				}
			}
			//TODO: printar na tela aqui o `dispatcher=>`
			CPU0.setProcesso(processoAtual);
			threadCPU = new Thread(CPU0);
			threadCPU.start();
			wait(100);
			esperaCPU.acquire();
			
			clockTick();
			processoAtual.diminuiTempoProcessador();		
			// Se o processo terminar, libera os recursos
			verificaContinuidadeDoProcesso(processoAtual);	
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

		for (ProcessoVO processo : processos) {
			if (processo.getPID() == idProcesso) {
				if (processo.getPrioridade() == 0)
					return true;
				break;
			}
		}
		return false;
	}
	
	private void verificaProcessoInicializandoAgora() {
		processos.forEach((pr)->{
			if(pr.getTempoInicializacao()==CLOCK) {//se o processo vai iniciar agora
				gerenciadorDeFilas.adicionaProcesso(pr);//adiciona o mesmo às filas de processo
				processos.remove(pr);// remove o mesmo dos processos que nao foram inicializados
			}
		});
	}
	
	private void verificaContinuidadeDoProcesso(ProcessoVO pr) {
		if(pr.getTempoProcessador()<1) {
			gerenciadorDeFilas.removeProcesso(pr);
			RAM.desalocaMemoria(pr);
			REC.desacolaTodosOsRecursosDoProcesso(pr);
			//TODO: printar na tela que o processo foi finalizado.
		}
	}
	
	private void clockTick() throws InterruptedException {
		CLOCK++;
		//TODO: printa clock
		wait(1000);
		verificaProcessoInicializandoAgora();
	}
}

