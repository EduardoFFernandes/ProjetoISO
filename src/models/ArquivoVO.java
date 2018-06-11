package models;

public class ArquivoVO {

	private String nomeArquivo;
	private int posPrimeiroBloco;
	private int qtdBlocosArq;
	
	public ArquivoVO(String nomeArquivo, int posPrimeiroBloco, int qtdBlocosArq) {
		this.nomeArquivo = nomeArquivo;
		this.posPrimeiroBloco = posPrimeiroBloco;
		this.qtdBlocosArq = qtdBlocosArq;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public int getPosPrimeiroBloco() {
		return posPrimeiroBloco;
	}

	public int getQtdBlocosArq() {
		return qtdBlocosArq;
	}
}
