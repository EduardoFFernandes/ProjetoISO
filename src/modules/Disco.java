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

	public boolean cria(Operacao operacao) {
		boolean cabe, salvou = false;
		int qtdBlocosOp = operacao.getQtdBlocos();
		int blocoInicioArq = -1;
		for (int i = 0; i < blocosDisco; i++) {
			cabe = true;
			if (i + qtdBlocosOp > blocosDisco) {
				break;
			}
			if (blocos[i] == "0") {
				for (int j = 0; j < qtdBlocosOp; j++) {
					if (blocos[i + j] != "0") {
						cabe = false;
						break;
					}
				}
				if (cabe) {
					blocoInicioArq = i;
					for (int k = 0; k < qtdBlocosOp; k++) {
						blocos[blocoInicioArq + k] = operacao.getNomeArquivo();
					}
					salvou = true;
					break;
				}
			}
		}

		if (salvou) {
			gerenciadorDeFilas.getTelaPrincipal().logMessage(salvouArquivo(operacao, blocoInicioArq));
		} else {
			gerenciadorDeFilas.getTelaPrincipal().logMessage(naoSalvouArquivo(operacao), Color.RED);
		}
		return salvou;
	}

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

	public void executaOperacao(Operacao operacao) {
		if (!gerenciadorDeFilas.isProcessoValido(operacao.getIdProcesso())) {
			gerenciadorDeFilas.getTelaPrincipal().logMessage(NAO_EXISTE_PROCESSO);
			return;
		}
		if (operacao.getCodOperacao() == OP_CRIAR) {
			// operacao de criar arquivo
			cria(operacao);
		} else {
			// operacao de excluir arquivo
			Arquivo arq = procuraArquivo(operacao.getNomeArquivo());
			if (arq == null) {
				// se nao encontrou arquivo.
				gerenciadorDeFilas.getTelaPrincipal().logMessage(arqNaoEncontrado(operacao.getNomeArquivo()));
			} else if (operacao.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
				// processo e o mesmo que criou o arquivo
				deleta(arq);
				gerenciadorDeFilas.getTelaPrincipal().logMessage(excluiuArq(operacao));
			} else if (gerenciadorDeFilas.isProcessoTempoReal(operacao.getIdProcesso())) {
				// processo nao e o que criou o arquivo mas e de tempo real
				deleta(arq);
				gerenciadorDeFilas.getTelaPrincipal().logMessage(excluiuArq(operacao));
			} else {
				// processo nao e o que criou o arquivo e nao e de tempo real
				gerenciadorDeFilas.getTelaPrincipal().logMessage(
						procSemPermissaoExcluirArq(operacao.getIdProcesso(), operacao.getNomeArquivo()), Interface.RED);
			}

		}
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
