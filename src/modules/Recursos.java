package modules;

/**
 * Essa classe mantem os recursos do sistema operacional.
 */
public class Recursos {
    private int scanner;
    private int modem;
    private int[] impressoras;
    private int[] discoRigido;

    public Recursos() {
    	super();
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

    public void setEstruturaImpressoras(int[] impressoras) {
		this.impressoras = impressoras;
	}

	public int[] getDiscoRigido() {
        return discoRigido;
    }

    public void setDiscoRigido(int id, int discoRigido) {
        this.discoRigido[id] = discoRigido;
    }

	public void setEstruturaDiscoRigido(int[] discoRigido) {
		this.discoRigido = discoRigido;
	}
}
