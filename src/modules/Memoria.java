package modules;

import java.util.ArrayList;

import models.Processo;

public class Memoria {
    private String memoriaAsString;
    private ArrayList<Processo> processos;

    public Memoria() {
        super();
    }
    
    /**
     * Metodo que le a memoria principal e se possivel aloca espaco para o processo ser processado.
     */
    public boolean aloca(boolean isProcessoTempoReal, Processo processo) {
        String espacoNecessario = new String();
        int indiceDisponivel;

        for (int i = 0; i < processo.getBlocosMemoria(); i++) {

            espacoNecessario = espacoNecessario.concat("E");
        }
        if (isProcessoTempoReal) {

            indiceDisponivel = memoriaAsString.substring(0, 64).indexOf(espacoNecessario);
        } else {

            indiceDisponivel = memoriaAsString.substring(64).indexOf(espacoNecessario);
            indiceDisponivel = (indiceDisponivel == -1) ? -1 : indiceDisponivel + 64;
        }
        if (indiceDisponivel != -1) {

            memoriaAsString = memoriaAsString.substring(0, indiceDisponivel)
                    .concat(espacoNecessario.replaceAll("E", String.valueOf(processo.getPID())))
                    .concat(memoriaAsString.substring(indiceDisponivel + processo.getBlocosMemoria()));
            processo.setInicioProcessoMemoria(indiceDisponivel);
            processos.add(processo);
            return true;
        }
        return false;
    }

    /**
     * Metodo que retira o espaco alocado dos processos.
     */
    public void desaloca(Processo processo) {

        String espacoLivre = new String();
        for (int i = 0; i < processo.getBlocosMemoria(); i++) {
            espacoLivre = espacoLivre.concat("E");
        }
        memoriaAsString = memoriaAsString.substring(0, processo.getInicioProcessoMemoria()).concat(espacoLivre)
                .concat(memoriaAsString.substring(processo.getInicioProcessoMemoria() + processo.getBlocosMemoria()));
        processo.setInicioProcessoMemoria(-1);
        processos.remove(processo);

    }

    public ArrayList<Processo> getProcessos() {
        return processos;
    }

	public String getMemoriaAsString() {
		return memoriaAsString;
	}

	public void setMemoriaAsString(String memoriaAsString) {
		this.memoriaAsString = memoriaAsString;
	}

	public void setProcessos(ArrayList<Processo> processos) {
		this.processos = processos;
	}
}
