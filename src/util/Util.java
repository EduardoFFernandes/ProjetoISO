package util;

import static util.Constantes.ARQUIVO_INVALIDO;
import static util.Constantes.ARQUIVO_SELECIONADO;
import static util.Constantes.ARQUIVO_VALIDO;
import static util.Constantes.DISCO_PROCESSO_SEM_PERMISSAO;
import static util.Constantes.NEWLINE;
import static util.Constantes.PROCESSO;
import static util.Constantes.SISTEMA_DE_ARQUIVOS;
import static util.Constantes.VIRGULA;

import models.Operacao;
import models.Processo;

public class Util {
	
	public int getInt(String string) {
		return Integer.parseInt(string);
	}

	public boolean isValidString(String string) {
		if (string == null || string.equals("")) {
			return false;
		}
		return true;
	}
	
	public static String arquivoValidado(String nomeArquivo) {
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_VALIDO);
		return sb.toString();
	}

	public static String arquivoNaoValido(String nomeArquivo) {
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_INVALIDO);
		return sb.toString();
	}

	public static String executandoProc(int PID) {
		return PROCESSO + PID + " =>";
	}

	public static String procFinalizado(int PID) {
		return PROCESSO + PID + " foi finalizado.";
	}

	public static String procSemPermissaoExcluirArq(int procId, String arq) {
		return PROCESSO + procId + DISCO_PROCESSO_SEM_PERMISSAO + arq;
	}

	public static String arqNaoEncontrado(String arq) {
		return "Arquivo: " + arq + ", Nao encontrado.";
	}

	public static String operacoesDoSistema(int op) {
		return "Operacao " + op + " =>";
	}

	public static String sistemaDeArquivos() {
		return NEWLINE + SISTEMA_DE_ARQUIVOS + NEWLINE;
	}

	public static String salvouArquivo(Operacao op, int inicio) {
		int i;
		StringBuilder sb = new StringBuilder();
		sb.append(PROCESSO);
		sb.append(op.getIdProcesso());
		sb.append(" criou o arquivo ");
		sb.append(op.getNomeArquivo());
		sb.append("(blocos: ");
		if (op.getQtdBlocos() <= 6) {
			sb.append(String.valueOf(inicio));
			for (i = inicio + 1; i < inicio + op.getQtdBlocos() - 1; i++) {
				sb.append(VIRGULA + String.valueOf(i));
			}
			sb.append(" e " + String.valueOf(inicio + op.getQtdBlocos() - 1));
		} else {
			sb.append(inicio);
			sb.append(" ate ");
			sb.append(inicio + op.getQtdBlocos() - 1);
		}
		sb.append(")");

		return sb.toString();
	}

	public static String naoSalvouArquivo(Operacao operacao) {
		return PROCESSO + operacao.getIdProcesso() + " Nao pode criar o arquivo " + operacao.getNomeArquivo()
				+ " (falta de espaco)";
	}

	public static String excluiuArq(Operacao operacao) {
		return PROCESSO + operacao.getIdProcesso() + " deletou o arquivo " + operacao.getNomeArquivo();
	}

	public static String dispatcher(Processo processo) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dispatcher =>");
		sb.append(NEWLINE);
		sb.append("PID: ").append(processo.getPID());
		sb.append(NEWLINE);
		sb.append("offset: ").append(processo.getInicioProcessoMemoria());
		sb.append(NEWLINE);
		sb.append("blocos: ").append(processo.getBlocosMemoria());
		sb.append(NEWLINE);
		sb.append("prioridade: ").append(processo.getPrioridade());
		sb.append(NEWLINE);
		sb.append("tempo de processador: ").append(processo.getTempo());
		sb.append(NEWLINE);
		sb.append("impressoras: ").append(processo.getImpressora());
		sb.append(NEWLINE);
		sb.append("scanners: ").append(processo.getScanner());
		sb.append(NEWLINE);
		sb.append("modems: ").append(processo.getModem());
		sb.append(NEWLINE);
		sb.append("disco rigido: ").append(processo.getDisco());
		sb.append(NEWLINE);

		return sb.toString();
	}

	// ERROS//
	public static String erroMemoria(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta de Memoria Principal.";
	}

	public static String erroRecursos(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta de Recursos.";
	}

	public static String erroEspacoGerenciadorDeProcessos(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta espaco no gerenciador de processos.";
	}
}
