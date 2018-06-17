package modulos;

import java.util.ArrayList;
import java.util.Arrays;

import models.ProcessoVO;


public class ModuloMemoria {
	private final int TAMANH0_TOTAL_MEMORIA = 1024;
	private final int INICIO_BLOCO_MEM_USUARIO = 64;
	private int[] blocos;
	private ArrayList<ProcessoVO> processosEmMemoria;
	
	ModuloMemoria(){
		blocos = new int[TAMANH0_TOTAL_MEMORIA];
		processosEmMemoria = new ArrayList<>();
		Arrays.fill(blocos,0);
	}
	
	
	
	public boolean alocaMemoria(boolean isProcessoTempoReal,ProcessoVO processo){
		int inicioBlocos = INICIO_BLOCO_MEM_USUARIO;
		int fimBlocos = TAMANH0_TOTAL_MEMORIA;
		boolean cabe, salvou = false;
		int qtdBlocosPro = processo.getBlocosEmMemoriaRAM();
		if(isProcessoTempoReal){
			inicioBlocos = 0;
			fimBlocos = INICIO_BLOCO_MEM_USUARIO;
		}
		
		for(int i = inicioBlocos; i< fimBlocos;i++){
			cabe = true;
			if(i + qtdBlocosPro > TAMANH0_TOTAL_MEMORIA){
				break;
			}
			
			if (blocos[i] == 0) {
				for (int y = 0; y < qtdBlocosPro; y++) {
					if (blocos[i + y] > 0) {
						cabe = false;
						break;
					}
				}
				if (cabe) {
					gravaNaRAM(i,qtdBlocosPro,processo.getPID());
					processo.setInicioProcessoRAM(i);
					processosEmMemoria.add(processo);
					salvou = true;
					break;
				}
			}
		}

		return salvou;
	}
	
	public boolean desalocaMemoria(ProcessoVO processo){
		int inicio = processo.getInicioProcessoRAM();
		if(inicio > 0){
			gravaNaRAM(inicio,inicio+processo.getBlocosEmMemoriaRAM(),0);
			processosEmMemoria.remove(processo);
			return true;
		}
		return false;
	}
	
	private void gravaNaRAM(int inicio,int fim,int dado){
		for (int y = inicio; y < fim; y++) {
			blocos[y] = dado;
		}
	}
	
	public boolean isProcessoEmMemoria(ProcessoVO pr){
		if(processosEmMemoria.contains(pr)){
			return true;
		}
		return false;
	}
}
