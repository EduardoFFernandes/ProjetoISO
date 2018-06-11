package models;

public class OperacaoNaEstruturaArquivosVO {
	private String nomeArquivo;
	private int idProcesso;
	private int codOperacao;
	private int qtdBlocos;
	
	public OperacaoNaEstruturaArquivosVO(int idProcesso, int codOperacao,String nomeArquivo) {
		this.idProcesso = idProcesso;
		this.codOperacao = codOperacao;
		this.nomeArquivo = nomeArquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public int getQtdBlocos() {
		return qtdBlocos;
	}

	public void setQtdBlocos(int qtdBlocos) {
		this.qtdBlocos = qtdBlocos;
	}

	public int getIdProcesso() {
		return idProcesso;
	}

	public int getCodOperacao() {
		return codOperacao;
	}
}
