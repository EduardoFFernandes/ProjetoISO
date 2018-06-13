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
	private volatile ModuloDisco HD1;
	private volatile ModuloCPU CPU0;
	
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

		esperaCPU = new Semaphore(1);
	}

	@Override
	public void run() {

		// inicia HD;
		this.operacoesEstruturaArq.forEach(op -> {
			HD1.executaOperacao(op);
		});
			HD1.printSituacaoDisco();
		// inicia RAM;

		// inicia recursos;

		// inicia a fila de processos

		// inicia o processador -> loop até a fila de processos acabar
		try {
			filaProcessosLoop();
		} catch (InterruptedException e) {
			// A thread do SO foi interrompida por algum motivo
			e.printStackTrace();
		}

	}

	public void filaProcessosLoop() throws InterruptedException {
		boolean filaVazia = false;
		while (!filaVazia) {
			//pedir para a fila de processos calcular qual o proximo processo
			//ProcessoVO p = gerenciadorDeFilas.calculaProximoProcesso();
//			CPU0.setProcesso(p);
			threadCPU = new Thread(CPU0);
			//pegar o proximo processo da fila de processos;
			// verifica os recursos que o processo precisa.
			threadCPU.start();
			esperaCPU.acquire();
			// verifica se a thread foi interrompida
			// pega o timer e aumenta em 1
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
