package util;

import models.Operacao;
import models.Processo;

public class Constantes {

	public static String TITULO = "Trabalho ISO";
	public static String INICIANDO = "Iniciando...";

	public static String MENU = "Menu";

	public static String SELECIONAR = "Selecionar";
	public static String SELECIONAR_CANCELADO = "Selecionar arquivo, cancelado.";

	public static String ARQUIVO_SELECIONADO = "Arquivo: ";
	public static String ARQUIVO_VALIDO = " validado com sucesso.";
	public static String ARQUIVO_INVALIDO = " invalido.";
	public static String ARQUIVO_IGUAIS = "Arquivos Iguais.";

	public static String ADICIONAR_PROCESSO = "Adicionar Processos";
	public static String ADICIONAR_ARQUIVOS = "Adicionar Arquivos";
	public static String INICIAR = "Iniciar";

	public static String NAO_SELECIONADO_ARQUIVO_PROCESSOS = "Nao foi selecionado um arquivo de Processos.";
	public static String NAO_SELECIONADO_ARQUIVO_ARQUIVOS = "Nao foi selecionado um arquivo da Estrutura de Arquivos.";

	public static String DISCO_PROCESSO_SEM_PERMISSAO = " Nao possui permissao para excluir o arquivo: ";
	public static String DISCO_MAPA_OCUPACAO = "Mapa de ocupacoes do disco: ";

	public static String PROCESSO = "Processo ";
	public static String SEM_PROCESSO_EXECUTAR = "Sem processos a executar";
	public static String NAO_EXISTE_PROCESSO = "Nao existe o processo.";

	public static String SISTEMA_DE_ARQUIVOS = "Sistema de arquivos =>";

	public static String NEWLINE = "\n";
	public static String VIRGULA = ", ";

	public static String INSTRUCAO = " Instrucao ";
	public static String RETURN_SIGINT = " return SIGINT";
	public static String INICIO = " Inicio";
	
    public static final int OP_EXCLUIR = 1;
    public static final int OP_CRIAR = 0;
    
    public static final int ARQUIVO_PADRAO = -1;
	
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
