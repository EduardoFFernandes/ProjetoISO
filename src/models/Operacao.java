package models;

/**
 * Essa classe mantem a operacao no sistema.
 */
public class Operacao {
    private int idProcesso;
    private int codOperacao;
    private String nomeArquivo;
    private int blocosNecessarios;

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

    public int getBlocosNecessarios() {
        return blocosNecessarios;
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

    public void setBlocosNecessarios(int qtdBlocos) {
        this.blocosNecessarios = qtdBlocos;
    }

}
