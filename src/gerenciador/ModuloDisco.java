package gerenciador;

import java.awt.Color;
import java.util.ArrayList;

import models.ArquivoVO;
import models.Gerenciador;
import models.OperacaoNaEstruturaArquivosVO;
import view.DispatcherWindow;

public class ModuloDisco extends Gerenciador  {

	private int qtdBlocosDisco;
	private ArrayList<ArquivoVO> arquivos;
	private ModuloSO listenerSO;
	private int[] blocos;
	
	
	public ModuloDisco(String nome, int uid,int qtdBlocosDisco,ModuloSO listenerSO,ArrayList<ArquivoVO> arquivos) {
		super(nome, uid);
		this.arquivos = arquivos;
		qtdBlocosDisco = qtdBlocosDisco;
		this.listenerSO = listenerSO;
		
		blocos = new int[qtdBlocosDisco];
	}
	
	
	
	public boolean createFile(String nomeArq, int qtdBlocos){
		return false;
	}
	
	public boolean deleteFile(String nomeArq){
		return false;
	}

	public String printSituacaoDisco(){
		//transformar o array de int em uma string de forma que fique interessante no terminal
		return "";
	}
	
	public boolean executaOperacao(OperacaoNaEstruturaArquivosVO op){
		
		listenerSO.screveNaTela("deu pau", DispatcherWindow.RED);
		return false;
	}
}
