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

    public Processo(int tempoInicializacao, int prioridade, int tempoProcessador, int blocosEmMemoria,
            int impressora, int scanner, int modem, int disco, int PID) {

        this.setTempoInicializacao(tempoInicializacao);
        this.setPrioridade(prioridade);
        this.setTempo(tempoProcessador);
        this.setBlocosMemoria(blocosEmMemoria);
        this.setImpressora(impressora);
        this.setScanner(scanner);
        this.setModem(modem);
        this.setDisco(disco);
        this.setPID(PID);
        this.setInicioProcessoMemoria(-1);
        this.recursosAlocados = false;
        this.recursoBlocante = false;
    }

    public int getTempoInicializacao() {
        return tempoInicializacao;
    }

    public void setTempoInicializacao(int tempoInicializacao) {
        this.tempoInicializacao = tempoInicializacao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getBlocosMemoria() {
        return blocosMemoria;
    }

    public void setBlocosMemoria(int blocosMemoria) {
        this.blocosMemoria = blocosMemoria;
    }

    public int getInicioProcessoMemoria() {
        return inicioProcessoMemoria;
    }

    public void setInicioProcessoMemoria(int inicioProcessoMemoria) {
        this.inicioProcessoMemoria = inicioProcessoMemoria;
    }

    public int getImpressora() {
        return impressora;
    }

    public void setImpressora(int impressoraId) {
        this.impressora = impressoraId;
    }

    public int getScanner() {
        return scanner;
    }

    public void setScanner(int scannerId) {
        this.scanner = scannerId;
    }

    public int getModem() {
        return modem;
    }

    public void setModem(int modemId) {
        this.modem = modemId;
    }

    public int getDisco() {
        return disco;
    }

    public void setDisco(int discoId) {
        this.disco = discoId;
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int pID) {
        PID = pID;
    }

    public boolean isRecursosAlocados() {
        return recursosAlocados;
    }

    public void setRecursosAlocados(boolean recursosAlocados) {
        this.recursosAlocados = recursosAlocados;
    }

    public boolean isPossuiRecursoBlocante() {
        return recursoBlocante;
    }

    public void setRecursoBlocante(boolean recursoBlocante) {
        this.recursoBlocante = recursoBlocante;
    }

}
