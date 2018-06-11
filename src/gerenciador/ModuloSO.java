package gerenciador;

import java.awt.Color;
import java.util.ArrayList;

import models.ArquivoVO;
import models.Gerenciador;
import models.OperacaoNaEstruturaArquivosVO;
import models.ProcessoVO;
import view.DispatcherWindow;

public class ModuloSO extends Gerenciador {

	private ArrayList<ProcessoVO> processos;
	private ArrayList<OperacaoNaEstruturaArquivosVO> operacoesEstruturaArq;

	private DispatcherWindow telaPrincipal;
	private volatile ModuloDisco HD1;

	@SuppressWarnings("unchecked")
	public ModuloSO(String nome, int uid, ArrayList<?> processos, ArrayList<?> operacoesEstruturaArq,
			ArrayList<ArquivoVO> arquivosEmDisco, DispatcherWindow telaPrincipal, int qtdBlocosDisco) {
		super(nome, uid);
		this.telaPrincipal = telaPrincipal;
		this.processos = (ArrayList<ProcessoVO>) processos;
		this.operacoesEstruturaArq = (ArrayList<OperacaoNaEstruturaArquivosVO>) operacoesEstruturaArq;
		HD1 = new ModuloDisco("HD1", 1, qtdBlocosDisco, this, arquivosEmDisco);
		
	}

	@Override
	public void run() {
		HD1.run();
		
		//inicia HD;
		this.operacoesEstruturaArq.forEach(op->{HD1.executaOperacao(op);});
		HD1.printSituacaoDisco();
		//inicia RAM;
		
		//inicia recursos;
		

		//inicia a fila de processos
		//inicia o contador de tempo
		//inicia o processador -> loop até a fila de processos acabar
		
	}

	synchronized public void escreveNaTela(String toPrint,Color cor) {
		telaPrincipal.printaNoTerminal(toPrint,cor);
	}
	
	synchronized public void escreveNaTela(String toPrint) {
		telaPrincipal.printaNoTerminal(toPrint);
	}
	
	synchronized public boolean isProcessoTempoReal(int idProcesso){
		//verifica aqui se o processo é de tempo real
		
		for (ProcessoVO processo : processos){
			if(processo.getPID() == idProcesso){
				if(processo.getPrioridade() == 0)
					return true;
				break;
			}
		}
		return false;
	}
}
