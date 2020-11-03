package models;

public class Arquivo {

	private String nomeArquivo;
	private int posPrimeiroBloco;
	private int qtdBlocosArq;
	private int idProcessoCriouArquivo;
	
	public static final int ARQUIVO_PADRAO = -1;
	
	public Arquivo(String nomeArquivo, int posPrimeiroBloco, int qtdBlocosArq, int idProcessoCriouArquivo) {
		this.nomeArquivo = nomeArquivo;
		this.posPrimeiroBloco = posPrimeiroBloco;
		this.qtdBlocosArq = qtdBlocosArq;
		this.idProcessoCriouArquivo = idProcessoCriouArquivo;
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

	public int getIdProcessoCriouArquivo() {
		return idProcessoCriouArquivo;
	}
}