package util;

import models.Operacao;
import models.Processo;

public enum Constantes {
	
	TELA_PRINCIPAL_TITULO("Pseudo SO"),
	INICIANDO_SO("Iniciando SO..."),
	
	SELECIONAR("Selecionar"),
	SELECIONAR_CANCELADO("Selecionar arquivo, cancelado."),
	
	ARQUIVO_SELECIONADO("Arquivo: "),
	ARQUIVO_VALIDO(" validado com sucesso."),
	ARQUIVO_INVALIDO(" invalido."),
	ARQUIVO_IGUAIS("Arquivos Iguais."),
	
	BOTAO_TXT_ADICIONAR_PROCESSO("<html>Adicionar<br/>Processos</html>"),
	BOTAO_TXT_ADICIONAR_ARQUIVOS("<html>Adicionar<br/>Arquivos</html>"),
	BOTAO_TXT_INICIAR_SO("<html>Iniciar<br/>SO</html>"),
	
	NAO_SELECIONADO_ARQ_PROCESSOS("Não foi selecionado um arquivo de Processos."),
	NAO_SELECIONADO_ARQ_ARQUIVOS("Não foi selecionado um arquivo da Estrutura de Arquivos."),
	
	DISCO_PROCESSO_SEM_PERMISSAO(" não possui permissão para excluir o arquivo: "),
	DISCO_MAPA_OCUPACAO("Mapa de ocupação do disco: "),
	
	PROCESSO("Processo "),
	SEM_PROCESSO_EXECUTAR("Sem processos a executar"),
	NAO_EXISTE_PROCESSO("Não existe o processo."),
	
	SYS_ARQ("Sistema de arquivos =>"),
	
	NEWLINE("\n"),
	VIRGULA(", ");
	
	
	private String texto;
	private Constantes(String a){
		this.texto = a;
	}
	
	public String getTexto(){
		return texto;
	}
	
	
	public static String arquivoValidado(String nomeArquivo){
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO.getTexto());
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_VALIDO.getTexto());	
		return sb.toString();
	}
	
	public static String arquivoNaoValido(String nomeArquivo){
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO.getTexto());
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_INVALIDO.getTexto());	
		return sb.toString();
	}
	
	public static String printaClock(int CLOCK) {
		return "===================="+CLOCK+"====================";
	}
	
	public static String faltaRAM(int PID) {
		return PROCESSO.getTexto() +PID+" nao foi inicializado por falta de Memória Principal.";
	}
	
	public static String faltaRecursos(int PID) {
		return PROCESSO.getTexto()+PID+" nao foi inicializado por falta de Recursos.";
	}
	public static String faltaEspacoGerenciadorDeProcessos(int PID) {
		return PROCESSO.getTexto()+PID+" nao foi inicializado por falta espaço no gerenciador de processos.";
	}
	
	public static String executandoProc(int PID) {
		return PROCESSO.getTexto()+PID+" =>";
	}
	
	public static String procFinalizado(int PID) {
		return PROCESSO.getTexto() +PID+" foi finalizado.";
	}
	public static String procSemPermissaoExcluirArq(int procId,String arq) {
		return PROCESSO.getTexto() +procId+DISCO_PROCESSO_SEM_PERMISSAO.getTexto() +arq;
	}
	public static String arqNaoEncontrado(String arq) {
		return "Arquivo: "+arq+", não encontrado.";
	}
	public static String operacoesDoSistema(int op) {
		return "Operação " + op +" =>";
	}
	public static String sistemaDeArquivos() {
		return NEWLINE.getTexto()+SYS_ARQ.getTexto() + NEWLINE.getTexto();
	}
	
	
	public static String salvouArq(Operacao op,int inicio) {
		int i;
		StringBuilder sb = new StringBuilder();
		sb.append(PROCESSO.getTexto());
		sb.append(op.getIdProcesso());
		sb.append(" criou o arquivo ");
		sb.append(op.getNomeArquivo());
		sb.append("(blocos: ");
		if(op.getQtdBlocos() <= 6) {
			sb.append(String.valueOf(inicio));
			for(i = inicio + 1; i < inicio+op.getQtdBlocos() - 1; i++) {
				sb.append(VIRGULA.getTexto() + String.valueOf(i));
			}
			sb.append(" e " + String.valueOf(inicio+op.getQtdBlocos() - 1));
		} else {
			sb.append(inicio);
			sb.append(" até ");
			sb.append(inicio+op.getQtdBlocos() - 1);
		}
		sb.append(")");
		
		return sb.toString();
	}
	public static String naoSalvouArq(Operacao op) {
		return PROCESSO.getTexto()+ op.getIdProcesso()+ " não pode criar o arquivo "+op.getNomeArquivo()+" (falta de espaço)";
	}
	public static String excluiuArq(Operacao op) {
		return PROCESSO.getTexto()+ op.getIdProcesso()+ " deletou o arquivo "+op.getNomeArquivo();
	}
	
	public static String dispatcher(Processo pr) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dispatcher =>");
		sb.append(NEWLINE.getTexto());
		sb.append("   PID: ").append(pr.getPID());
		sb.append(NEWLINE.getTexto());
		sb.append("   offset: ").append(pr.getInicioProcessoRAM());
		sb.append(NEWLINE.getTexto());
		sb.append("   blocos: ").append(pr.getBlocosEmMemoriaRAM());
		sb.append(NEWLINE.getTexto());
		sb.append("   prioridade: ").append(pr.getPrioridade());
		sb.append(NEWLINE.getTexto());
		sb.append("   tempo de processador: ").append(pr.getTempoProcessador());
		sb.append(NEWLINE.getTexto());
		sb.append("   impressoras: ").append(pr.getReqCodImpressora());
		sb.append(NEWLINE.getTexto());
		sb.append("   scanners: ").append(pr.getReqScanner());
		sb.append(NEWLINE.getTexto());
		sb.append("   modems: ").append(pr.getReqModem());
		sb.append(NEWLINE.getTexto());
		sb.append("   disco rígido: ").append(pr.getReqCodDisco());
		sb.append(NEWLINE.getTexto());
		
		return sb.toString();
	}
}
