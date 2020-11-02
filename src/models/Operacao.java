package models;

public class Operacao {
	private String nomeArquivo;
	private int idProcesso;
	private int codOperacao;
	private int qtdBlocos;
	
	public static final int OP_EXCLUIR = 1;
	public static final int OP_CRIAR = 0;
	
	public Operacao(int idProcesso, int codOperacao,String nomeArquivo) {
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
