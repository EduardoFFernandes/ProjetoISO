package modules;

import static util.Constantes.ARQUIVOS;
import static util.Constantes.ARQUIVO_PADRAO;
import static util.Constantes.PROCESSOS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import models.Processo;
import util.Util;

public class ManipuladorDeArquivos extends Util {

	private String tipoArquivo;
	private File arquivo;
	private ArrayList<Arquivo> arquivosValidados;
	private ArrayList<Processo> processosValidados;
	private ArrayList<Operacao> operacoesValidados;
	private int blocosDisco;
	private boolean validado;

	
	public ManipuladorDeArquivos(File arquivo, String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
		this.arquivo = arquivo;
		this.validado = false;
	}
	
	public ArrayList<Processo> getProcessosValidados() {
		return processosValidados;
	}
	public ArrayList<Operacao> getOperacoesValidadas() {
		return operacoesValidados;
	}
	public ArrayList<Arquivo> getArquivosValidados() {
		return arquivosValidados;
	}

	public boolean validaArquivo() throws IOException {

		if (tipoArquivo.equals(PROCESSOS)) {
			processosValidados = new ArrayList<Processo>();
			return validaProcessos();
		} else {
			arquivosValidados = new ArrayList<Arquivo>();
			operacoesValidados = new ArrayList<Operacao>();
			return validaArquivos();
		}

	}
	
	public int getBlocosDisco() {
		if (validado && tipoArquivo == ARQUIVOS) {
			return blocosDisco;
		}
		return -1;
	}

	/**
	 * Metodo que faz a leitura do arquivo dos processos.
	 */
	@SuppressWarnings({ "resource" })
	private boolean validaProcessos() throws IOException, FileNotFoundException {
		BufferedReader buffRead = new BufferedReader(new FileReader(arquivo));
		int processId = 0;

		String linha;
		while ((linha = buffRead.readLine()) != null) {
			if (!isValidString(linha)) {
				return false;
			}
			String[] valores = linha.split(", ");
			for (int i = 0; i < 7; i++) {
				if (!isValidString(valores[i])) {
					return false;
				}
			}
			Processo processo = new Processo();
			processo.setTempoInicializacao(getInt(valores[0]));
			processo.setPrioridade(getInt(valores[1]));
			processo.setTempo(getInt(valores[2]));
			processo.setBlocosMemoria(getInt(valores[3]));
			processo.setImpressora(getInt(valores[4]));
			processo.setScanner(getInt(valores[5]));
			processo.setModem(getInt(valores[6]));
			processo.setDisco(getInt(valores[7]));
			processo.setPID(processId);
			processo.setInicioProcessoMemoria(-1);
			processo.setRecursosAlocados(false);
			processo.setRecursoBlocante(false);

			processosValidados.add(processo);
			processId++;
		}
		buffRead.close();
		validado = true;
		return validado;
	}

	/**
	 * Metodo que faz a leitura do arquivo com as operacoes do modulo de disco, assim como a quantidade de blocos a serem alocados.
	 */
	@SuppressWarnings({ "resource" })
	private boolean validaArquivos() throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(arquivo));
		String linha;
		
		linha = buffRead.readLine();// quantidade total de blocos no disco
		if (!isValidString(linha)) {
			return false;
		}
		blocosDisco = Integer.parseInt(linha);
		linha = buffRead.readLine();// quantidade de Arquivos/segmentos em disco
		if (!isValidString(linha)) {
			return false;
		}
		int segmentos = Integer.parseInt(linha);
		for (int i = 0; i < segmentos; i++) { // percorre todos os segmentos
			String[] valores = buffRead.readLine().split(", ");
			if (!isValidString(valores[0]) || !isValidString(valores[1]) || !isValidString(valores[2])) {
				return false;
			}

			Arquivo arquivo = new Arquivo();
			arquivo.setNomeArquivo(valores[0]);
			arquivo.setPosPrimeiroBloco(getInt(valores[1]));
			arquivo.setQtdBlocosArq(getInt(valores[2]));
			arquivo.setIdProcessoCriouArquivo(ARQUIVO_PADRAO);
			arquivosValidados.add(arquivo);
		}

		while ((linha = buffRead.readLine()) != null) {
			Operacao operacao = null;
			String[] valores = linha.split(", ");
			if (!isValidString(valores[0]) || !isValidString(valores[1]) || !isValidString(valores[2])) {
				return false;
			}
			if (valores.length == 4) {// operacao de adicao de arquivos
				if (!isValidString(valores[3])) {
					return false;
				}
				operacao = new Operacao();
				operacao.setIdProcesso(getInt(valores[0]));
				operacao.setCodOperacao(getInt(valores[1]));
				operacao.setNomeArquivo(valores[2]);
				operacao.setQtdBlocos(getInt(valores[3]));

			} else {
				operacao = new Operacao();
				operacao.setIdProcesso(getInt(valores[0]));
				operacao.setCodOperacao(getInt(valores[1]));
				operacao.setNomeArquivo(valores[2]);
			}
			operacoesValidados.add(operacao);
		}

		validado = true;
		return validado;
	}

}
