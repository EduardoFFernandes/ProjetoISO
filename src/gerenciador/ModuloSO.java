package gerenciador;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import models.ArquivoVO;
import models.Gerenciador;
import models.OperacaoNaEstruturaArquivosVO;
import models.ProcessoVO;
import view.DispatcherWindow;

public class ModuloSO extends Gerenciador {

	private ArrayList<ProcessoVO> processos;
	private ArrayList<OperacaoNaEstruturaArquivosVO> operacoesEstruturaArq;

	private volatile DispatcherWindow telaPrincipal;
	private volatile ModuloCPU CPU0;
	private ModuloDisco HD1;
	private ModuloMemoria RAM;
	private int CLOCK;
	
	private Thread threadCPU;
	Semaphore esperaCPU;

	@SuppressWarnings("unchecked")
	public ModuloSO(String nome, int uid, ArrayList<?> processos, ArrayList<?> operacoesEstruturaArq,
			ArrayList<ArquivoVO> arquivosEmDisco, DispatcherWindow telaPrincipal, int qtdBlocosDisco) {
		super(nome, uid);
		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<ProcessoVO>) processos;
		this.operacoesEstruturaArq = (ArrayList<OperacaoNaEstruturaArquivosVO>) operacoesEstruturaArq;
		
		HD1 = new ModuloDisco("HD1", 1, qtdBlocosDisco, this, arquivosEmDisco);
		CPU0 = new ModuloCPU("CPU0",1,esperaCPU);
		RAM = new ModuloMemoria();
		
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
		//pedir para a fila de processos calcular qual o proximo processo
		while(processoAtual != null){
//		while ((processoAtual = gerenciadorDeFilas.calculaProximoProcesso()) != null) {
			//verifica se tem algum processo que vai iniciar nesse exato CLOCK
			////se sim, adiciona o mesmo no gerenciadorDeFilas
			if(!RAM.isProcessoEmMemoria(processoAtual)){//Significa que é a primeira vez que processo roda no processador
				boolean inicializou = false;
				if(!RAM.alocaMemoria(processoAtual.getPrioridade()==0, processoAtual)){
					//printa na tela que o processo não pode ser alocado por falta de RAM
					//recoloca ele no gerenciador de filas
					continue;
				}
				//aloca recursos
				//inicializou = recursos.AlocaRecursos(p);
				if(!inicializou){
					//printa na tela que o processo não pode ser alocado por falta de recursos
					//recoloca ele no gerenciador de filas
					continue;
				}
			}
//			CPU0.setProcesso(p);
			threadCPU = new Thread(CPU0);
			//pegar o proximo processo da fila de processos;
			// verifica os recursos que o processo precisa.
			threadCPU.start();
			esperaCPU.acquire();
			// verifica se a thread foi interrompida
			CLOCK++;
			processoAtual.diminuiTempoProcessador();
			wait(1000);//simula processador lento
			// Se o processo terminar, libera os recursos
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
}
