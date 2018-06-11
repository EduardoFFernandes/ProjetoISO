package gerenciador;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import models.ArquivoVO;
import models.Gerenciador;
import models.OperacaoNaEstruturaArquivosVO;
import view.DispatcherWindow;

public class ModuloSO extends Gerenciador {

	private List<?> processos;
	private List<OperacaoNaEstruturaArquivosVO> operacoesEstruturaArq;

	private DispatcherWindow telaPrincipal;
	private ModuloDisco HD1;

	public ModuloSO(String nome, int uid, ArrayList processos, ArrayList operacoesEstruturaArq,
			ArrayList<ArquivoVO> arquivosEmDisco, DispatcherWindow telaPrincipal, int qtdBlocosDisco) {
		super(nome, uid);
		this.telaPrincipal = telaPrincipal;
		this.processos = processos;
		this.operacoesEstruturaArq = operacoesEstruturaArq;
		
		//inicia HD;
		HD1 = new ModuloDisco("HD1", 1, qtdBlocosDisco, this, arquivosEmDisco);
		this.operacoesEstruturaArq.forEach(HD1::executaOperacao);
		
		//inicia RAM;
		
		//inicia recursos;
	}

	public void iniciaSO() {
		//inicia a fila de processos
		//inicia o contador de tempo
		//inicia o processador -> loop até a fila de processos acabar
	}

	synchronized public void screveNaTela(String toPrint,Color cor) {
		telaPrincipal.printaNoTerminal(toPrint,cor);
	}
}
