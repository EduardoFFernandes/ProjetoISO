package util;

import models.Operacao;
import models.Processo;

public class Constantes {
	
	public static String TITULO = "Trabalho ISO";
	public static String INICIANDO = "Iniciando...";
	
	public static String SELECIONAR = "Selecionar";
	public static String SELECIONAR_CANCELADO = "Selecionar arquivo, cancelado.";
	
	public static String ARQUIVO_SELECIONADO = "Arquivo: ";
	public static String ARQUIVO_VALIDO = " validado com sucesso.";
	public static String ARQUIVO_INVALIDO = " invalido.";
	public static String ARQUIVO_IGUAIS = "Arquivos Iguais.";
	
	public static String BOTAO_ADICIONAR_PROCESSO = "<html>Adicionar<br/>Processos</html>";
	public static String BOTAO_ADICIONAR_ARQUIVOS = "<html>Adicionar<br/>Arquivos</html>";
	public static String BOTAO_INICIAR = "<html>Iniciar</html>";
	
	public static String NAO_SELECIONADO_ARQ_PROCESSOS = "N�o foi selecionado um arquivo de Processos.";
	public static String NAO_SELECIONADO_ARQ_ARQUIVOS = "N�o foi selecionado um arquivo da Estrutura de Arquivos.";
	
	public static String DISCO_PROCESSO_SEM_PERMISSAO = " n�o possui permiss�o para excluir o arquivo: ";
	public static String DISCO_MAPA_OCUPACAO = "Mapa de ocupa��o do disco: ";
	
	public static String PROCESSO = "Processo ";
	public static String SEM_PROCESSO_EXECUTAR = "Sem processos a executar";
	public static String NAO_EXISTE_PROCESSO = "N�o existe o processo.";
	
	public static String SYS_ARQ = "Sistema de arquivos =>";
	
	public static String NEWLINE = "\n";
	public static String VIRGULA = ", ";
	
	public static String arquivoValidado(String nomeArquivo){
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_VALIDO);	
		return sb.toString();
	}
	
	public static String arquivoNaoValido(String nomeArquivo){
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_INVALIDO);	
		return sb.toString();
	}
	
	public static String clock(int CLOCK) {
		return "===================="+CLOCK+"====================";
	}
	
	public static String executandoProc(int PID) {
		return PROCESSO + PID + " =>";
	}
	
	public static String procFinalizado(int PID) {
		return PROCESSO + PID + " foi finalizado.";
	}
	public static String procSemPermissaoExcluirArq(int procId,String arq) {
		return PROCESSO + procId + DISCO_PROCESSO_SEM_PERMISSAO + arq;
	}
	public static String arqNaoEncontrado(String arq) {
		return "Arquivo: " + arq + ", n�o encontrado.";
	}
	public static String operacoesDoSistema(int op) {
		return "Opera��o " + op +" =>";
	}
	public static String sistemaDeArquivos() {
		return NEWLINE + SYS_ARQ + NEWLINE;
	}
	
	
	public static String salvouArq(Operacao op,int inicio) {
		int i;
		StringBuilder sb = new StringBuilder();
		sb.append(PROCESSO);
		sb.append(op.getIdProcesso());
		sb.append(" criou o arquivo ");
		sb.append(op.getNomeArquivo());
		sb.append("(blocos: ");
		if(op.getQtdBlocos() <= 6) {
			sb.append(String.valueOf(inicio));
			for(i = inicio + 1; i < inicio+op.getQtdBlocos() - 1; i++) {
				sb.append(VIRGULA + String.valueOf(i));
			}
			sb.append(" e " + String.valueOf(inicio+op.getQtdBlocos() - 1));
		} else {
			sb.append(inicio);
			sb.append(" at� ");
			sb.append(inicio + op.getQtdBlocos() - 1);
		}
		sb.append(")");
		
		return sb.toString();
	}
	public static String naoSalvouArq(Operacao op) {
		return PROCESSO + op.getIdProcesso() + " n�o pode criar o arquivo "+op.getNomeArquivo()+" (falta de espa�o)";
	}
	public static String excluiuArq(Operacao op) {
		return PROCESSO + op.getIdProcesso() + " deletou o arquivo "+op.getNomeArquivo();
	}
	
	public static String dispatcher(Processo pr) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dispatcher =>");
		sb.append(NEWLINE);
		sb.append("   PID: ").append(pr.getPID());
		sb.append(NEWLINE);
		sb.append("   offset: ").append(pr.getInicioProcessoRAM());
		sb.append(NEWLINE);
		sb.append("   blocos: ").append(pr.getBlocosEmMemoriaRAM());
		sb.append(NEWLINE);
		sb.append("   prioridade: ").append(pr.getPrioridade());
		sb.append(NEWLINE);
		sb.append("   tempo de processador: ").append(pr.getTempoProcessador());
		sb.append(NEWLINE);
		sb.append("   impressoras: ").append(pr.getReqCodImpressora());
		sb.append(NEWLINE);
		sb.append("   scanners: ").append(pr.getReqScanner());
		sb.append(NEWLINE);
		sb.append("   modems: ").append(pr.getReqModem());
		sb.append(NEWLINE);
		sb.append("   disco r�gido: ").append(pr.getReqCodDisco());
		sb.append(NEWLINE);
		
		return sb.toString();
	}
	//ERROS//
	public static String erroMemoria(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta de Mem�ria Principal.";
	}
	
	public static String erroRecursos(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta de Recursos.";
	}
	public static String erroEspacoGerenciadorDeProcessos(int PID) {
		return PROCESSO + PID + " nao foi inicializado por falta espa�o no gerenciador de processos.";
	}
}
