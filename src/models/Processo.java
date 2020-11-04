package models;

public class Processo {
	private int tempoInicializacao;//em qual clock o processo vai inicializar
	private int prioridade;
	private int tempoProcessador;//quantos clocks o processo vai rodar
	private int blocosEmMemoriaRAM;//quantidade de blocos em RAM
	private int inicioProcessoRAM;//posicao do primeiro bloco na RAM
	private int ReqCodImpressora;
	private int reqScanner;
	private int reqModem;
	private int reqCodDisco;
	private int PID; //quando o SO e iniciado ,e gerado um PID.
	private boolean recursosAlocados; // indicador que os recursos do processo foram alocados
	private boolean possuiRecursoBlocante;
	
	
	
	
	public Processo(int tempoInicializacao, int prioridade, int tempoProcessador, int blocosEmMemoriaRAM,
			int reqCodImpressora, int reqScanner, int reqModem, int reqCodDisco,int PID) {

		this.tempoInicializacao = tempoInicializacao;
		this.prioridade = prioridade;
		this.tempoProcessador = tempoProcessador;
		this.blocosEmMemoriaRAM = blocosEmMemoriaRAM;
		this.ReqCodImpressora = reqCodImpressora;
		this.reqScanner = reqScanner;
		this.reqModem = reqModem;
		this.reqCodDisco = reqCodDisco;
		this.PID = PID;
		
		this.inicioProcessoRAM = -1;
		this.recursosAlocados = false;
		this.possuiRecursoBlocante = false;
	}
	
	public int getTempoInicializacao() {
		return tempoInicializacao;
	}
	public int getPrioridade() {
		return prioridade;
	}
	public int getTempoProcessador() {
		return tempoProcessador;
	}
	public void diminuiTempoProcessador(){
		this.tempoProcessador--;
	}
	public int getBlocosEmMemoriaRAM() {
		return blocosEmMemoriaRAM;
	}
	public int getReqCodImpressora() {
		return ReqCodImpressora;
	}
	public int getReqScanner() {
		return reqScanner;
	}
	public int getReqModem() {
		return reqModem;
	}
	public int getReqCodDisco() {
		return reqCodDisco;
	}
	public int getPID() {
		return PID;
	}

	public int getInicioProcessoRAM() {
		return inicioProcessoRAM;
	}

	public void setInicioProcessoRAM(int inicioProcessoRAM) {
		this.inicioProcessoRAM = inicioProcessoRAM;
	}

	public boolean isRecursosAlocados() {
		return recursosAlocados;
	}

	public void setRecursosAlocados(boolean recursosAlocados) {
		this.recursosAlocados = recursosAlocados;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public boolean isPossuiRecursoBlocante() {
		return possuiRecursoBlocante;
	}

	public void setPossuiRecursoBlocante(boolean possuiRecursoBlocante) {
		this.possuiRecursoBlocante = possuiRecursoBlocante;
	}

	
}
