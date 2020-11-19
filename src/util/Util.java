package util;

import static util.Constantes.ARQUIVO_INVALIDO;
import static util.Constantes.ARQUIVO_SELECIONADO;
import static util.Constantes.ARQUIVO_VALIDO;
import static util.Constantes.DISCO_MAPA_OCUPACAO;
import static util.Constantes.DISCO_PROCESSO_SEM_PERMISSAO;
import static util.Constantes.PROCESSO;
import static util.Constantes.SISTEMA_DE_ARQUIVOS;

import models.Operacao;
import models.Processo;
import modules.Disco;
import modules.Interface;

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

	public static String operacoesDoSistema(int posicaoOperacao, boolean status) {
		return "Operacao " + posicaoOperacao + " =>" + ((status) ? " Sucesso" : " Falha");
	}

	public static String sistemaDeArquivos() {
		return "\n" + SISTEMA_DE_ARQUIVOS + "\n";
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
				sb.append(", " + String.valueOf(i));
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
		return PROCESSO + operacao.getIdProcesso() + " nao pode criar o arquivo " + operacao.getNomeArquivo()
				+ " (falta de espaco)";
	}

	public static String excluiuArq(Operacao operacao) {
		return PROCESSO + operacao.getIdProcesso() + " deletou o arquivo " + operacao.getNomeArquivo();
	}

	public static void resultadoDisco(final Interface telaPrincipal, final Disco gerenciadorDoDisco) {

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(DISCO_MAPA_OCUPACAO);

		for (int i = 0; i < gerenciadorDoDisco.getBlocosDisco(); i++) {
			if (i % 10 != 0) {
				sb.append(gerenciadorDoDisco.getDiscoAsString().charAt(i) + " | ");
			} else {
				sb.append("\n");
				sb.append("| " + gerenciadorDoDisco.getDiscoAsString().charAt(i) + " | ");
			}
		}
		telaPrincipal.logMessage(sb.toString());
	}

	public static String dispatcher(Processo processo) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dispatcher =>");
		sb.append("\n");
		sb.append("PID: ").append(processo.getPID());
		sb.append("\n");
		sb.append("offset: ").append(processo.getInicioProcessoMemoria());
		sb.append("\n");
		sb.append("blocos: ").append(processo.getBlocosMemoria());
		sb.append("\n");
		sb.append("prioridade: ").append(processo.getPrioridade());
		sb.append("\n");
		sb.append("tempo de processador: ").append(processo.getTempo());
		sb.append("\n");
		sb.append("impressoras: ").append(processo.getImpressora());
		sb.append("\n");
		sb.append("scanners: ").append(processo.getScanner());
		sb.append("\n");
		sb.append("modems: ").append(processo.getModem());
		sb.append("\n");
		sb.append("disco rigido: ").append(processo.getDisco());
		sb.append("\n");

		return sb.toString();
	}

	// ERROS//
	public static String erroMemoria(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta de Memoria Principal.";
	}

	public static String erroRecursos(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta de Recursos.";
	}

	public static String erroFaltaEspacoProcessos(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta espaco no gerenciador de processos.";
	}
	
	// Metodos que criam a logica do disco e memoria
	public static String memoriaEmBranco(String blocoMemoria) {
	    blocoMemoria = new String();
	    for (int i = 0; i < 1024; i++) {
	        blocoMemoria = blocoMemoria.concat("E");
	    }
	   return blocoMemoria;
	}
	
	public static String discoEmBranco(String blocoDisco, int tamanho) {
	    blocoDisco = new String();
        for (int i = 0; i < tamanho; i++) {
            blocoDisco = blocoDisco.concat("0");
        }
       return blocoDisco;
    }
}
