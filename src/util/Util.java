package util;

import static util.Constantes.ARQUIVO_INVALIDO;
import static util.Constantes.ARQUIVO_SELECIONADO;
import static util.Constantes.ARQUIVO_VALIDO;
import static util.Constantes.DISCO_MAPA_OCUPACAO;
import static util.Constantes.DISCO_PROCESSO_SEM_PERMISSAO;
import static util.Constantes.FALHA;
import static util.Constantes.INICIO;
import static util.Constantes.INSTRUCAO;
import static util.Constantes.PROCESSO;
import static util.Constantes.RETURN_SIGINT;
import static util.Constantes.SISTEMA_DE_ARQUIVOS;
import static util.Constantes.SUCESSO;

import models.Operacao;
import models.Processo;
import modules.Disco;
import modules.Interface;

public class Util {

	public boolean textoValido(String string) {
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

	public static String arquivoInvalido(String nomeArquivo) {
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_INVALIDO);
		return sb.toString();
	}

	public static String processoExecutado(int PID) {
		return PROCESSO + PID + " =>";
	}

	public static String processoFinalizado(int PID) {
		return PROCESSO + PID + " foi finalizado.";
	}

	public static String processoErroDelete(int idProcesso, String nomeArquivo) {
		return PROCESSO + idProcesso + DISCO_PROCESSO_SEM_PERMISSAO + nomeArquivo;
	}

	public static String arquivoNaoEncontrado(String nomeArquivo) {
		return "Arquivo " + nomeArquivo + " nao encontrado.";
	}

	public static String operacoesDoSistema(int posicaoOperacao, boolean status) {

        return "Operacao " + posicaoOperacao + " =>" + ((status) ? SUCESSO : FALHA);
	}

	public static String sistemaDeArquivos() {
		return "\n" + SISTEMA_DE_ARQUIVOS + "\n";
	}

	public static String salvouArquivo(Operacao op, int inicioBloco) {
		int i;
		StringBuilder sb = new StringBuilder();
		sb.append(PROCESSO);
		sb.append(op.getIdProcesso());
		sb.append(" criou o arquivo ");
		sb.append(op.getNomeArquivo());
		sb.append("(blocos: ");
		if (op.getBlocosNecessarios() <= 6) {
			sb.append(String.valueOf(inicioBloco));
			for (i = inicioBloco + 1; i < inicioBloco + op.getBlocosNecessarios() - 1; i++) {
				sb.append(", " + String.valueOf(i));
			}
			sb.append(" e " + String.valueOf(inicioBloco + op.getBlocosNecessarios() - 1));
		} else {
			sb.append(inicioBloco);
			sb.append(" ate ");
			sb.append(inicioBloco + op.getBlocosNecessarios() - 1);
		}
		sb.append(")");

		return sb.toString();
	}

	public static String naoSalvouArquivo(Operacao operacao) {
		return PROCESSO + operacao.getIdProcesso() + " nao pode criar o arquivo " + operacao.getNomeArquivo()
				+ " ,falta de espaco";
	}

	public static String excluiuArq(Operacao operacao) {
		return PROCESSO + operacao.getIdProcesso() + " deletou o arquivo " + operacao.getNomeArquivo();
	}

	public static void resultadoDisco(final Interface terminal, final Disco gerenciadorDoDisco) {

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(DISCO_MAPA_OCUPACAO);
		int count = 0;
		for (char c : gerenciadorDoDisco.getDiscoAsString().toCharArray()) {
			if (count % 10 != 0) {
				sb.append(c + " | ");
			} else {
				sb.append("\n");
				sb.append("| " + c + " | ");
			}
			count++;
		}
		terminal.logMessage(sb.toString());
	}

	synchronized public static void processoInfo(Interface terminal, Processo processo) {
		terminal.logMessage(PROCESSO + processo.getPID() + INICIO);
		for (int i = 1; i <= 3; i++) {
			terminal.logMessage(PROCESSO + processo.getPID() + INSTRUCAO + i);
		}
		terminal.logMessage(PROCESSO + processo.getPID() + RETURN_SIGINT);
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
	public static String memoriaEmBranco() {
		String blocoMemoria = new String();
	    for (int i = 0; i < 1024; i++) {
	        blocoMemoria = blocoMemoria.concat("E");
	    }
	   return blocoMemoria;
	}
	
	public static String discoEmBranco(int tamanho) {
		String blocoDisco = new String();
        for (int i = 0; i < tamanho; i++) {
            blocoDisco = blocoDisco.concat("0");
        }
       return blocoDisco;
    }
}
