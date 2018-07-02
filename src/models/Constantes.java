package models;

/**
 * Enum com as constantes gerais do projeto.
 * 
 * @author tulio.matias		-	06/06/2018
 */
public enum Constantes {

	TELA_PRINCIPAL_TITULO("Pseudo SO"),
	INICIANDO_SO("Iniciando SO..."),
	
	FC_SELECIONAR("Selecionar"),
	FC_SELECIONAR_CANCELADO("Selecionar arquivo, cancelado."),
	
	ARQUIVO_SELECIONADO("Arquivo: "),
	ARQUIVO_VALIDO(" validado com sucesso."),
	ARQUIVO_NAOVALIDO(" não é válido."),
	ARQUIVO_IGUAIS("Arquivos Iguais."),
	
	BOTAO_TXT_ADICIONAR_PROCESSO("<html>Adicionar<br/>Processos</html>"),
	BOTAO_TXT_ADICIONAR_ARQUIVOS("<html>Adicionar<br/>Arquivos</html>"),
	BOTAO_TXT_INICIAR_SO("<html>Iniciar<br/>SO</html>"),
	
	NAO_SELECIONADO_ARQ_PROCESSOS("Não foi selecionado um arquivo de Processos."),
	NAO_SELECIONADO_ARQ_ARQUIVOS("Não foi selecionado um arquivo da Estrutura de Arquivos."),
	
	DISCO_PROCESSO_SEM_PERMISSAO(" não possui permissão para excluir o arquivo: "),
	DISCO_MAPA_OCUPACAO("Mapa de ocupação do disco: "),
	
	PROC("Processo: "),
	SEM_PROC_EXEC("Sem processos a executar"),
	N_EXISTE_PROC("Não existe o processo."),
	
	
	
	SYS_ARQ("Sistema de arquivos =>"),
	
	NEWLINE("\n")
	
	;
	
	
	private String texto;
	private Constantes(String a){
		this.texto = a;
	}
	
	public String getTexto(){
		return texto;
	}
	
	
	public static String arquivoValidado(String nomeArquivo){
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO.texto);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_VALIDO.texto);	
		return sb.toString();
	}
	
	public static String arquivoNaoValido(String nomeArquivo){
		StringBuilder sb = new StringBuilder();
		sb.append(ARQUIVO_SELECIONADO.texto);
		sb.append(nomeArquivo);
		sb.append(ARQUIVO_NAOVALIDO.texto);	
		return sb.toString();
	}
	
	public static String printaClock(int CLOCK) {
		return "===================="+CLOCK+"====================";
	}
	
	public static String faltaRAM(int PID) {
		return PROC.texto +PID+" nao foi inicializado por falta de RAM.";
	}
	
	public static String faltaRecursos(int PID) {
		return PROC.texto+PID+" nao foi inicializado por falta de Recursos.";
	}
	public static String faltaEspacoGerenciadorDeProcessos(int PID) {
		return PROC.texto+PID+" nao foi inicializado por espaço no gerenciador de processos.";
	}
	
	public static String executandoProc(int PID) {
		return PROC.texto+PID+" =>";
	}
	
	public static String procFinalizado(int PID) {
		return PROC.texto +PID+" foi finalizado.";
	}
	public static String procSemPermissaoExcluirArq(int procId,String arq) {
		return PROC.texto +procId+DISCO_PROCESSO_SEM_PERMISSAO.texto +arq;
	}
	public static String arqNaoEncontrado(String arq) {
		return "Arquivo: "+arq+", não encontrado.";
	}
	public static String sysArqOp(int op) {
		return NEWLINE.texto+SYS_ARQ.texto + NEWLINE.texto +"Operação " + op +" =>";
	}
	
	public static String salvouArq(OperacaoNaEstruturaArquivosVO op,int inicio) {
		StringBuilder sb = new StringBuilder();
		sb.append(PROC.texto);
		sb.append(op.getIdProcesso());
		sb.append(" criou o arquivo ");
		sb.append(op.getNomeArquivo());
		sb.append("(blocos: ");
		sb.append(inicio);
		inicio--;// é necessário considerar que o bloco de inicio também é utilizado
		sb.append(" até ");
		sb.append(inicio+op.getQtdBlocos());
		sb.append(")");
		
		return sb.toString();
	}
	public static String naoSalvouArq(OperacaoNaEstruturaArquivosVO op) {
		return PROC.texto+ op.getIdProcesso()+ " não pode criar o arquivo "+op.getNomeArquivo()+" (falta de espaço)";
	}
	public static String excluiuArq(OperacaoNaEstruturaArquivosVO op) {
		return PROC.texto+ op.getIdProcesso()+ " excluiu o arquivo "+op.getNomeArquivo();
	}
	
	public static String dispatcher(ProcessoVO pr) {
		StringBuilder sb = new StringBuilder();
		sb.append("Dispatcher =>");
		sb.append(NEWLINE.texto);
		sb.append("   PID: ").append(pr.getPID());
		sb.append(NEWLINE.texto);
		sb.append("   offset: ").append(pr.getInicioProcessoRAM());
		sb.append(NEWLINE.texto);
		sb.append("   blocks: ").append(pr.getBlocosEmMemoriaRAM());
		sb.append(NEWLINE.texto);
		sb.append("   priority: ").append(pr.getPrioridade());
		sb.append(NEWLINE.texto);
		sb.append("   time: ").append(pr.getTempoProcessador());
		sb.append(NEWLINE.texto);
		sb.append("   printers: ").append(pr.getReqCodImpressora());
		sb.append(NEWLINE.texto);
		sb.append("   scanners: ").append(pr.getReqScanner());
		sb.append(NEWLINE.texto);
		sb.append("   modems: ").append(pr.getReqModem());
		sb.append(NEWLINE.texto);
		sb.append("   drivers: ").append(pr.getReqCodDisco());
		sb.append(NEWLINE.texto);
		
		return sb.toString();
	}
}
