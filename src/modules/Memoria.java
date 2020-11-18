package modules;

import java.util.ArrayList;
import java.util.Arrays;

import models.Processo;

public class Memoria {
    //TODO Mecher nisso
	private int[] bloco = new int[1024];
	private ArrayList<Processo> processos;
	

	Memoria() {
		processos = new ArrayList<>();
		Arrays.fill(bloco, -1);
	}

	public boolean alocaMemoria(boolean isProcessoTempoReal, Processo processo) {
		int inicioBlocos = 64;
		int fimBlocos = 1024;
		boolean cabe, salvou = false;
		int qtdBlocosPro = processo.getBlocosMemoria();
		if (isProcessoTempoReal) {
			inicioBlocos = 0;
			fimBlocos = 64;
		}

		for (int i = inicioBlocos; i < fimBlocos; i++) {
			cabe = true;
			if (i + qtdBlocosPro > fimBlocos) {// TODO verificar aqui se o que foi colocado esta certo
				break;
			}

			if (bloco[i] == -1) {// se o bloco que estamos avaliando esta vazio
				for (int y = 0; y < qtdBlocosPro; y++) {
					if (bloco[i + y] > 0) {// se o valor contido no bloco em questao for diferente de -1 entao nao cabe
						cabe = false;
						break;
					}
				}
				if (cabe) {// se cabe significa que encontrou um espaco grande o suficiente para colocar o
							// processo
					gravaNaMemoria(i, qtdBlocosPro, processo.getPID()); 
					processo.setInicioProcessoMemoria(i);
					processos.add(processo);
					salvou = true;
					break;
				}
			}
		}

		return salvou;
	}

	public void desalocaMemoria(Processo processo) {
		gravaNaMemoria(processo.getInicioProcessoMemoria(),processo.getBlocosMemoria(), -1);
		processo.setInicioProcessoMemoria(-1);
		processos.remove(processo);

	}

	private void gravaNaMemoria(int inicio, int tam, int dado) {
		int fim = inicio+tam;
		for (int y = inicio; y < fim; y++) {
			bloco[y] = dado;
		}
	}

	public boolean isProcessoEmMemoria(Processo processo) {
		if (processos.contains(processo)) {
			return true;
		}
		return false;
	}
}
