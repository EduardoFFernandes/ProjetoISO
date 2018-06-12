package models;

import java.util.ArrayList;


//TODO: excluir essa classe.
//ou implementar multiplos discos
public class EstruturaArquivosVO {

	private ArrayList<ArquivoVO> arquivos;
	private String[] blocos;
	private int tamanhoDisco;
	
	public EstruturaArquivosVO(ArrayList<ArquivoVO> arquivos,String[] blocos,int tamanhoDisco){
		this.tamanhoDisco = tamanhoDisco;
		this.arquivos = arquivos;
		this.blocos = blocos;
	}
	
	
	synchronized public boolean salvaArquivo(OperacaoNaEstruturaArquivosVO op,int posInicial){
		if(posInicial+op.getQtdBlocos() > tamanhoDisco){
			return false;
		}
		for (int y = 0; y < op.getQtdBlocos(); y++) {
			blocos[posInicial + y] = op.getNomeArquivo();
		}
		return true;
	}
	
	synchronized public boolean deleteFile(ArquivoVO arquivo) {
		if(arquivo.getPosPrimeiroBloco()+arquivo.getQtdBlocosArq()> tamanhoDisco){
			return false;
		}
		for (int i = 0; i < arquivo.getQtdBlocosArq(); i++) {
			blocos[arquivo.getPosPrimeiroBloco() + i] = "0";
		}

		return true;
	}
}
