package gerenciador;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import models.ArquivoVO;
import models.Constantes;
import models.Gerenciador;
import models.OperacaoNaEstruturaArquivosVO;
import view.DispatcherWindow;

public class ModuloDisco extends Gerenciador  {

	private int qtdBlocosDisco;
	private ArrayList<ArquivoVO> arquivos;
	private ModuloSO listenerSO;
	private String[] blocos;
	
	
	public ModuloDisco(String nome, int uid,int qtdBlocosDisco,ModuloSO listenerSO,ArrayList<ArquivoVO> arquivos) {
		super(nome, uid);
		this.arquivos = arquivos;
		this.qtdBlocosDisco = qtdBlocosDisco;
		this.listenerSO = listenerSO;
		
		
		blocos = new String[qtdBlocosDisco];
		
	}
	@Override
	synchronized public void run() {
		processaArquivos();
	}
	
	synchronized private void processaArquivos() {
		for (ArquivoVO arquivo : arquivos){
			int i;
			for(i = 0;i<arquivo.getQtdBlocosArq();i++){
				blocos[arquivo.getPosPrimeiroBloco()+i] = arquivo.getNomeArquivo();
			}
		}
		for(int i = 0; i< qtdBlocosDisco;i++){
			if(blocos[i] == null){
				blocos[i] = "0";
			}
		}
	}


	synchronized public boolean createFile(OperacaoNaEstruturaArquivosVO op){
		
		return false;
	}
	

	synchronized public boolean deleteFile(ArquivoVO arquivo){
		for(int i = 0;i< arquivo.getQtdBlocosArq();i++){
			blocos[arquivo.getPosPrimeiroBloco()+i] = "0";
		}
		
		return true;
	}

	synchronized public void printSituacaoDisco(){
		StringBuilder sb = new StringBuilder();
		int printaNaTelaComQuebraDeLinha;

		sb.append(Constantes.DISCO_MAPA_OCUPACAO.getTexto());
		sb.append(Constantes.NEWLINE.getTexto());
		
		for (int i = 0; i< qtdBlocosDisco;i++){
			printaNaTelaComQuebraDeLinha = i % 10;
			if(printaNaTelaComQuebraDeLinha != 0){
				sb.append("| "+blocos[i]+" |");
			}else{
				sb.append(Constantes.NEWLINE.getTexto());
				sb.append("| "+blocos[i]+" |");				
			}
		}
		listenerSO.escreveNaTela(sb.toString());
	}
	
	synchronized public boolean executaOperacao(OperacaoNaEstruturaArquivosVO op){

		if(op.getCodOperacao() == OperacaoNaEstruturaArquivosVO.OP_CRIAR){
			//operação de criar arquivo
			return createFile(op);
		}else{
			//operação de excluir arquivo
			ArquivoVO arq = procuraArquivoNoDisco(op.getNomeArquivo());
			if(arq == null){
				//se não encontrou arquivo.
				return false;
			}
			//encontrou arquivo
			if(op.getIdProcesso() == arq.getIdProcessoCriouArquivo()){
				//processo é o mesmo que criou o arquivo
				deleteFile(arq);
			}else if(listenerSO.isProcessoTempoReal(op.getIdProcesso())){
				//processo não é o que criou o arquivo mas é de tempo real
				deleteFile(arq);
			}else{
				//processo não é o que criou o arquivo e não é de tempo real
				listenerSO.escreveNaTela(Constantes.DISCO_PROCESSO_SEM_PERMISSAO.getTexto()+op.getNomeArquivo(), DispatcherWindow.RED);
				return false;
			}
				
			
		}
		
		
		return false;
	}
	synchronized private ArquivoVO procuraArquivoNoDisco(String nome){
		for (ArquivoVO arquivo : arquivos){
			if(arquivo.getNomeArquivo().equals(nome)){
				return arquivo;
			}
		}
		return null;
	}
}
