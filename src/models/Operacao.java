package models;

public class Operacao {
    private String nomeArquivo;
    private int idProcesso;
    private int codOperacao;
    private int qtdBlocos;

    public Operacao() {
        super();
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public int getIdProcesso() {
        return idProcesso;
    }

    public int getCodOperacao() {
        return codOperacao;
    }

    public int getQtdBlocos() {
        return qtdBlocos;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void setIdProcesso(int idProcesso) {
        this.idProcesso = idProcesso;
    }

    public void setCodOperacao(int codOperacao) {
        this.codOperacao = codOperacao;
    }

    public void setQtdBlocos(int qtdBlocos) {
        this.qtdBlocos = qtdBlocos;
    }

}
