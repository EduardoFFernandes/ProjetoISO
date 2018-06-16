package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.Main;
import models.ArquivoVO;
import models.OperacaoNaEstruturaArquivosVO;
import models.ProcessoVO;

public class ManipuladorDeArquivos {

	private String tipoArquivo;
	private File aSerValidado;
	@SuppressWarnings("rawtypes")
	private ArrayList objetosValidados;
	private ArrayList<ArquivoVO> arquivosValidados;
	private int qtdBlocosDisco;
	private int qtdArqEmDisco;
	private boolean fileValidated;
	/**
	 * Construtor, recebe o arquivo e o tipo do mesmo.
	 * 
	 * @param	aSerValidado	arquivo que será utilizado para obter as informações.
	 * @param	tipoArquivo		tipo do Arquivo, definido na Main
	 * @return	Um arquivo se o filepicker voltou com sucesso, null caso contrário
	 * */
	public ManipuladorDeArquivos(File aSerValidado, String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
		this.aSerValidado = aSerValidado;
		this.fileValidated = false;
	}
	/**
	 * Dado as informações recebidas no construtor, valida o arquivo e constroi 
	 * as estruturas de dados a partir do mesmo
	 * 
	 * 
	 * @return	Se o arquivo selecionado é valido ou não.
	 
	 * */
	public boolean validaArquivo() throws IOException {

		if (tipoArquivo.equals(Main.ARQ_PROCESSOS)) {
			objetosValidados = new ArrayList<ProcessoVO>();
			return validaProcessos();
		} else {
			arquivosValidados = new ArrayList<ArquivoVO>();
			objetosValidados = new ArrayList<OperacaoNaEstruturaArquivosVO>();
			return validaEstruturaArquivos();
		}

	}
	/**
	 * Valida especificamente arquivos de processos.
	 * 
	 * @return	Se o arquivo de processo é valido ou não.
	 * */
	@SuppressWarnings("unchecked")
	private boolean validaProcessos() throws IOException {
		BufferedReader leitorBuffer = getBufferedReaderFromFile(aSerValidado);
		int processId = 1;

		String linha;
		while ((linha = leitorBuffer.readLine()) != null) {
			if (linha == "") {// Se for linha vazia pula para a proxima linha.
				continue;
			}
			if(!isValidString(linha)){
				return false;
			}
			String[] valores = linha.split(", ");
			for (int i = 0; i < 7; i++) {
				if(!isValidString(valores[i])){
					return false;
				}
			}
			ProcessoVO processo = new ProcessoVO(getInt(valores[0]), getInt(valores[1]), getInt(valores[2]),
					getInt(valores[3]), getInt(valores[4]), getInt(valores[5]), getInt(valores[6]), getInt(valores[7]), processId);
			objetosValidados.add(processo);
			processId++;
		}

		leitorBuffer.close();
		fileValidated = true;
		return fileValidated;
	}
	/**
	 * Valida especificamente o arquivo de estrutura de arquivos.
	 * 
	 * @return	Se o arquivo de estruturas é valido ou não.
	 * */
	@SuppressWarnings("unchecked")
	private boolean validaEstruturaArquivos() throws IOException {
		BufferedReader leitorBuffer = getBufferedReaderFromFile(aSerValidado);
		String linha;

		linha = leitorBuffer.readLine();// quantidade total de blocos no disco
		if(!isValidString(linha)){
			return false;
		}
		qtdBlocosDisco = Integer.parseInt(linha);
		linha = leitorBuffer.readLine();// quantidade de Arquivos/segmentos em disco
		if(!isValidString(linha)){
			return false;
		}
		int segmentos = Integer.parseInt(linha);
		for (int i = 0; i < segmentos; i++) { //percorre todos os segmentos
			String[] valores = leitorBuffer.readLine().split(", ");
			if(!isValidString(valores[0]) || !isValidString(valores[1]) || !isValidString(valores[2]) ){
				return false;
			}
			ArquivoVO arquivo = new ArquivoVO(valores[0], getInt(valores[1]), getInt(valores[2]), ArquivoVO.ARQUIVO_PADRAO);
			arquivosValidados.add(arquivo);
		}

		while ((linha = leitorBuffer.readLine()) != null) {
			OperacaoNaEstruturaArquivosVO operacao = null;
			String[] valores = linha.split(", ");
			if(!isValidString(valores[0]) || !isValidString(valores[1]) || !isValidString(valores[2])){
				return false;
			}
			if(valores.length == 4){//operação de adição de arquivos
				if(!isValidString(valores[3]) ){
					return false;
				}
				operacao = new OperacaoNaEstruturaArquivosVO(getInt(valores[0]), getInt(valores[1]), valores[2]);
				operacao.setQtdBlocos(getInt(valores[3]));
			}else{
				operacao = new OperacaoNaEstruturaArquivosVO(getInt(valores[0]), getInt(valores[1]), valores[2]);
			}
			objetosValidados.add(operacao);
		}
		
		fileValidated = true;
		return fileValidated;
	}
	/**
	 * Retorna os objetos validados pelo método validaArquivo
	 * 
	 * @return	Array com os objetos validados, null se o arquivo é inválido
	 * */
	public ArrayList<?> getObjetosValidados(){
		return objetosValidados;
	}	
	/**
	 * Retorna os objetos validados pelo método validaArquivo
	 * 
	 * @return	Array com os ArquivosVO validados, null se o arquivo é inválido
	 * */
	public ArrayList<ArquivoVO> getArquivosValidados(){
		return arquivosValidados;
	}
	/**
	 * Verifica se o arquivo enviado é valido.
	 * 
	 * @return	true se é valido, false caso contrário.
	 * */
	public boolean isFileValidated(){
		return fileValidated;
	}
	/**
	 * Retorna a quantidade de blocos que o disco possui.
	 * 
	 * @return	qtdBlocosDisco se o arquivo foi validado e o arquivo enviado for do tipo Estrutura_Arquivos
	 * 			se não, -1.
	 * */
	public int getQtdBlocosDisco(){
		if(fileValidated && tipoArquivo == Main.ARQ_ESTRUTURA_ARQUIVOS){
			return qtdBlocosDisco;
		}
		return -1;
	}
	/**
	 * Retorna a quantidade de arquivos em disco.
	 * 
	 * @return	qtdArqEmDisco se o arquivo foi validado e o arquivo enviado for do tipo Estrutura_Arquivos
	 * 			se não, -1.
	 * */
	public int getQtdArqEmDisco(){
		if(fileValidated && tipoArquivo == Main.ARQ_ESTRUTURA_ARQUIVOS){
			return qtdArqEmDisco;
		}
		return -1;
	}
	/**
	 * Converte uma String s em inteiro.
	 * 
	 * @param	s	string a ser convertida
	 * @return	valor inteiro da string recebida.
	 * */
	private int getInt(String s) {
		return Integer.parseInt(s);
	}
	/**
	 * Verifica se a string s não é nulla e se ela não é vazia
	 * 
	 * @param	s	string a ser verificada
	 * @return	true se a string é valida, false caso contrário.
	 * */
	private boolean isValidString(String s){
		if(s == null || s .equals("")){
			return false;			
		}
		return true;
	}
	/**
	 * Recebe um arquivo e retorna um BufferedReader para o mesmo
	 * 
	 * @param	s	arquivo a ser manuseado.
	 * @return	BufferedReader do arquivo enviado para a função.
	 * @throws FileNotFoundException caso não consiga abrir o arquivo.
	 * */
	private BufferedReader getBufferedReaderFromFile(File s) throws FileNotFoundException {
		FileReader fileReader = null;
		fileReader = new FileReader(s);
		return new BufferedReader(fileReader);
	}
}
