package modulos;

import java.util.ArrayList;
import java.util.Arrays;

import models.Processo;

public class ModuloMemoria {
	private final int TAMANH0_TOTAL_MEMORIA = 1024;
	private final int INICIO_BLOCO_MEM_USUARIO = 64;
	private final int BLOCO_VAZIO = -1;
	private int[] blocos;
	private ArrayList<Processo> processosEmMemoria;
	

	ModuloMemoria() {
		blocos = new int[TAMANH0_TOTAL_MEMORIA];
		processosEmMemoria = new ArrayList<>();
		Arrays.fill(blocos, BLOCO_VAZIO);
	}

	public boolean alocaMemoria(boolean isProcessoTempoReal, Processo processo) {
		int inicioBlocos = INICIO_BLOCO_MEM_USUARIO;
		int fimBlocos = TAMANH0_TOTAL_MEMORIA;
		boolean cabe, salvou = false;
		int qtdBlocosPro = processo.getBlocosEmMemoriaRAM();
		if (isProcessoTempoReal) {
			inicioBlocos = 0;
			fimBlocos = INICIO_BLOCO_MEM_USUARIO;
		}

		for (int i = inicioBlocos; i < fimBlocos; i++) {
			cabe = true;
			if (i + qtdBlocosPro > fimBlocos) {// TODO verificar aqui se o que foi colocado está certo
				break;
			}

			if (blocos[i] == BLOCO_VAZIO) {// se o bloco que estamos avaliando está vazio
				for (int y = 0; y < qtdBlocosPro; y++) {
					if (blocos[i + y] > 0) {// se o valor contido no bloco em questão for diferente de BLOCO_VAZIO então não cabe
						cabe = false;
						break;
					}
				}
				if (cabe) {// se cabe significa que encontrou um espaço grande o suficiente para colocar o
							// processo
					gravaNaRAM(i, qtdBlocosPro, processo.getPID()); 
					processo.setInicioProcessoRAM(i);
					processosEmMemoria.add(processo);
					salvou = true;
					break;
				}
			}
		}

		return salvou;
	}

	public void desalocaMemoria(Processo processo) {
		gravaNaRAM(processo.getInicioProcessoRAM(),processo.getBlocosEmMemoriaRAM(), BLOCO_VAZIO);
		processo.setInicioProcessoRAM(BLOCO_VAZIO);
		processosEmMemoria.remove(processo);

	}

	private void gravaNaRAM(int inicio, int tam, int dado) {
		int fim = inicio+tam;
		for (int y = inicio; y < fim; y++) {
			blocos[y] = dado;
		}
	}

	public boolean isProcessoEmMemoria(Processo pr) {
		if (processosEmMemoria.contains(pr)) {
			return true;
		}
		return false;
	}
}
