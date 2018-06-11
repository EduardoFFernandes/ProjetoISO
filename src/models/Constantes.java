package models;

/**
 * Enum com as constantes gerais do projeto.
 * 
 * @author tulio.matias		-	06/06/2018
 */
public enum Constantes {

	TELA_PRINCIPAL_TITULO("Pseudo SO"),
	
	FC_SELECIONAR("Selecionar"),
	FC_SELECIONAR_CANCELADO("Selecionar arquivo, cancelado."),
	
	ARQUIVO_SELECIONADO("Arquivo: "),
	ARQUIVO_VALIDO(" validado com sucesso."),
	ARQUIVO_NAOVALIDO(" não é válido."),
	
	BOTAO_TXT_ADICIONAR_PROCESSO("<html>Adicionar<br/>Processos</html>"),
	BOTAO_TXT_ADICIONAR_ARQUIVOS("<html>Adicionar<br/>Arquivos</html>"),
	BOTAO_TXT_INICIAR_SO("<html>Iniciar<br/>SO</html>"),
	
	NAO_SELECIONADO_ARQ_PROCESSOS("Não foi selecionado um arquivo de Processos."),
	NAO_SELECIONADO_ARQ_ARQUIVOS("Não foi selecionado um arquivo da Estrutura de Arquivos.")
	
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
}
