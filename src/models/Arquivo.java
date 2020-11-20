package models;

/**
 * Essa classe mantem o arquivo no sistema.
 */
public class Arquivo {

    private String nomeArquivo;
    private int primeiroBloco;
    private int blocosOcupados;
    private int idProcesso;

    public Arquivo() {
        super();
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public int getPrimeiroBloco() {
        return primeiroBloco;
    }

    public int getBlocosOcupados() {
        return blocosOcupados;
    }

    public int getIdProcesso() {
        return idProcesso;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public void setPrimeiroBloco(int posPrimeiroBloco) {
        this.primeiroBloco = posPrimeiroBloco;
    }

    public void setBlocosOcupados(int qtdBlocosArq) {
        this.blocosOcupados = qtdBlocosArq;
    }

    public void setIdProcesso(int idProcessoCriouArquivo) {
        this.idProcesso = idProcessoCriouArquivo;
    }

}
