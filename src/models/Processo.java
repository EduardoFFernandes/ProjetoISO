package models;

public class Processo {
	private int tempoInicializacao;//em qual clock o processo vai inicializar
	private int prioridade;
	private int tempoProcessador;//quantos clocks o processo vai rodar
	private int blocosMemoria;//quantidade de blocos em RAM
	private int inicioProcessoMemoria;//posicao do primeiro bloco na RAM
	private int impressora;
	private int scanner;
	private int modem;
	private int disco;
	private int PID; //quando o SO é iniciado ,é gerado um PID.
	private boolean recursosAlocados;  // indicador que os recursos do processo foram alocados
	private boolean recursoBlocante;
	
	public Processo(int tempoInicializacao, int prioridade, int tempoProcessador, int blocosEmMemoriaRAM,
			int reqCodImpressora, int reqScanner, int reqModem, int reqCodDisco,int PID) {

		this.setTempoInicializacao(tempoInicializacao);
		this.setPrioridade(prioridade);
		this.setTempoProcessador(tempoProcessador);
		this.setBlocosMemoria(blocosEmMemoriaRAM);
		this.setImpressora(reqCodImpressora);
		this.setScanner(reqScanner);
		this.setModem(reqModem);
		this.setDisco(reqCodDisco);
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

	public int getTempoProcessador() {
		return tempoProcessador;
	}

	public void setTempoProcessador(int tempoProcessador) {
		this.tempoProcessador = tempoProcessador;
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
	
