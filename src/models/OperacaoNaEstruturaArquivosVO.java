package models;

public class OperacaoNaEstruturaArquivosVO {
	private String nomeArquivo;
	private int idProcesso;
	private int codOperacao;
	private int qtdBlocos;
	
//	public static final int OP_EXCLUIR = 1;
//	public static final int OP_CRIAR = 0;
//	public static final int OP_IMPRIMIR = 2;
	public enum Operacoes{
		OP_EXCLUIR(0),OP_CRIAR(1),OP_IMPRIMIR(100),OP_EXECUTAR(200),OP_DESLIGAR(900);
		
		private int i;
		private Operacoes(int i){
			this.i = i;
		}
		
		public int getValue(){
			return i;
		}
	}
	
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
