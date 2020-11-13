package models;

public class Processo {
    private int tempoInicializacao;
    private int prioridade;
    private int tempo;
    private int blocosMemoria;
    private int inicioProcessoMemoria;
    private int impressora;
    private int scanner;
    private int modem;
    private int disco;
    private int PID;
    private boolean recursosAlocados;
    private boolean recursoBlocante;

    public Processo() {
        super();
    }

    public int getTempoInicializacao() {
        return tempoInicializacao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public int getTempo() {
        return tempo;
    }

    public int getBlocosMemoria() {
        return blocosMemoria;
    }

    public int getInicioProcessoMemoria() {
        return inicioProcessoMemoria;
    }

    public int getImpressora() {
        return impressora;
    }

    public int getScanner() {
        return scanner;
    }

    public int getModem() {
        return modem;
    }

    public int getDisco() {
        return disco;
    }

    public int getPID() {
        return PID;
    }

    public boolean isRecursosAlocados() {
        return recursosAlocados;
    }

    public boolean isRecursoBlocante() {
        return recursoBlocante;
    }

    public void setTempoInicializacao(int tempoInicializacao) {
        this.tempoInicializacao = tempoInicializacao;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public void setBlocosMemoria(int blocosMemoria) {
        this.blocosMemoria = blocosMemoria;
    }

    public void setInicioProcessoMemoria(int inicioProcessoMemoria) {
        this.inicioProcessoMemoria = inicioProcessoMemoria;
    }

    public void setImpressora(int impressora) {
        this.impressora = impressora;
    }

    public void setScanner(int scanner) {
        this.scanner = scanner;
    }

    public void setModem(int modem) {
        this.modem = modem;
    }

    public void setDisco(int disco) {
        this.disco = disco;
    }

    public void setPID(int pID) {
        PID = pID;
    }

    public void setRecursosAlocados(boolean recursosAlocados) {
        this.recursosAlocados = recursosAlocados;
    }

    public void setRecursoBlocante(boolean recursoBlocante) {
        this.recursoBlocante = recursoBlocante;
    }

}
