package modules;

public class Recursos {
	private int scanner;
	private int modem;
	private int[] impressoras;
	private int[] discoRigido;
	
	private int SEMAFORO = 0;

	public Recursos() {
		this.scanner = SEMAFORO;
		this.modem = SEMAFORO;
		this.impressoras = new int[2];
		impressoras[0] = SEMAFORO;
		impressoras[1] = SEMAFORO;
		this.discoRigido = new int[2];
		discoRigido[0] = SEMAFORO;
		discoRigido[1] = SEMAFORO;
	}

	public int getScanner() {
		return scanner;
	}

	public void setScanner(int scanner) {
		this.scanner = scanner;
	}

	public int getModem() {
		return modem;
	}

	public void setModem(int modem) {
		this.modem = modem;
	}

	public int[] getImpressoras() {
		return impressoras;
	}

	public void setImpressoras(int id, int impressoras) {
		this.impressoras[id] = impressoras;
	}

	public int[] getDiscoRigido() {
		return discoRigido;
	}

	public void setDiscoRigido(int id, int discoRigido) {
		this.discoRigido[id] = discoRigido;
	}
}
