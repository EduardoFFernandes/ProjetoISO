package modules;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import static util.Constantes.*;
import static util.Util.*;

public class Disco {
    
	private ArrayList<Arquivo> arquivos;
	private GerenciadorDeFilas gerenciadorDeFilas;
	private String[] blocos;
	private int blocosDisco;

	public Disco(int blocosDisco, GerenciadorDeFilas listenerSO, ArrayList<Arquivo> arquivos) {
		this.arquivos = arquivos;
		this.blocosDisco = blocosDisco;
		this.gerenciadorDeFilas = listenerSO;
		this.blocos = new String[blocosDisco];
	}
	
	
	/**
	 * Metodo que executa as operacoes recebidas.
	 */
	public void executaOperacao(Operacao operacao) {
        if (operacao.getCodOperacao() == OP_CRIAR) {
            cria(operacao);
        } else {
            Arquivo arq = procuraArquivo(operacao.getNomeArquivo());
            if (arq == null) {
                gerenciadorDeFilas.getTelaPrincipal().logMessage(arqNaoEncontrado(operacao.getNomeArquivo()));
            } else if (operacao.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
                deleta(arq);
                gerenciadorDeFilas.getTelaPrincipal().logMessage(excluiuArq(operacao));
            } else if (gerenciadorDeFilas.isProcessoTempoReal(operacao.getIdProcesso())) {
                deleta(arq);
                gerenciadorDeFilas.getTelaPrincipal().logMessage(excluiuArq(operacao));
            } else {
                gerenciadorDeFilas.getTelaPrincipal().logMessage(
                        procSemPermissaoExcluirArq(operacao.getIdProcesso(), operacao.getNomeArquivo()), Interface.RED);
            }

        }
        if (!gerenciadorDeFilas.isProcessoValido(operacao.getIdProcesso())) {
            gerenciadorDeFilas.getTelaPrincipal().logMessage(NAO_EXISTE_PROCESSO);
            return;
        }
    }
	
	/**
	 * Metodo que cria os arquivos do disco.
	 */
	public boolean cria(Operacao operacao) {
	    String blocosAsString = new String();
        String espacoNecessario = new String();
        int indiceDisponivel;
        for (String string : blocos) {
            blocosAsString = blocosAsString.concat(string);
        }
        for (int i = 0; i < operacao.getQtdBlocos(); i++) {
            espacoNecessario = espacoNecessario.concat("0");
        }
        if ((indiceDisponivel = blocosAsString.indexOf(espacoNecessario)) != -1) {
            for (int i = indiceDisponivel; i < indiceDisponivel + operacao.getQtdBlocos(); i++) {
                blocos[i] = operacao.getNomeArquivo();
            }
            gerenciadorDeFilas.getTelaPrincipal().logMessage(salvouArquivo(operacao, indiceDisponivel));
            return true;
        } else {
            gerenciadorDeFilas.getTelaPrincipal().logMessage(naoSalvouArquivo(operacao), Color.RED);
            return false;
        }
	}
	
	
	/**
	 * Metodo que exclui arquivos do disco.
	 */
	public boolean deleta(Arquivo arquivo) {
		for (int i = 0; i < arquivo.getQtdBlocosArq(); i++) {
			blocos[arquivo.getPosPrimeiroBloco() + i] = "0";
		}

		return true;
	}

	public void resultadoDisco() {

		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(DISCO_MAPA_OCUPACAO);
		sb.append("\n");
		sb.append("| ");

		for (int i = 0; i < blocosDisco; i++) {
			if (i % 10 != 0) {
				sb.append(blocos[i] + " |");
			} else {
				sb.append("\n");
				sb.append("| " + blocos[i] + " |");
			}
		}
		gerenciadorDeFilas.getTelaPrincipal().logMessage(sb.toString());
	}

	

	public void processaArquivos() {
		for (Arquivo arquivo : arquivos) {
			int i;
			for (i = 0; i < arquivo.getQtdBlocosArq(); i++) {
				blocos[arquivo.getPosPrimeiroBloco() + i] = arquivo.getNomeArquivo();
			}
		}
		for (int i = 0; i < blocosDisco; i++) {
			if (blocos[i] == null) {
				blocos[i] = "0";
			}
		}
	}

	private Arquivo procuraArquivo(String nome) {
		for (Arquivo arquivo : arquivos) {
			if (arquivo.getNomeArquivo().equals(nome)) {
				return arquivo;
			}
		}
		return null;
	}

	public int getBlocosDisco() {
		return blocosDisco;
	}

	public void setBlocosDisco(int blocosDisco) {
		this.blocosDisco = blocosDisco;
	}

	public ArrayList<Arquivo> getArquivos() {
		return arquivos;
	}

	public void setArquivos(ArrayList<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public GerenciadorDeFilas getListenerSO() {
		return gerenciadorDeFilas;
	}

	public void setListenerSO(GerenciadorDeFilas listenerSO) {
		this.gerenciadorDeFilas = listenerSO;
	}

	public String[] getBlocos() {
		return blocos;
	}

	public void setBlocos(String[] blocos) {
		this.blocos = blocos;
	}
}
