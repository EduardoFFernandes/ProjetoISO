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

public class LeitorDeArquivos extends Util {

	private String tipoArquivo;
	private File arquivo;
	private ArrayList<Arquivo> arquivosValidados;
	private ArrayList<Processo> processosValidados;
	private ArrayList<Operacao> operacoesValidados;
	private int blocosDisco;
	private boolean validado;

	
	public LeitorDeArquivos(File arquivo, String tipoArquivo) {
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
			if (!textoValido(linha)) {
				return false;
			}
			String[] valores = linha.split(", ");
			for (int i = 0; i < 7; i++) {
				if (!textoValido(valores[i])) {
					return false;
				}
			}
			Processo processo = new Processo();
			processo.setTempoInicializacao(Integer.parseInt(valores[0]));
			processo.setPrioridade(Integer.parseInt(valores[1]));
			processo.setTempo(Integer.parseInt(valores[2]));
			processo.setBlocosMemoria(Integer.parseInt(valores[3]));
			processo.setImpressora(Integer.parseInt(valores[4]));
			processo.setScanner(Integer.parseInt(valores[5]));
			processo.setModem(Integer.parseInt(valores[6]));
			processo.setDisco(Integer.parseInt(valores[7]));
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
		if (!textoValido(linha)) {
			return false;
		}
		blocosDisco = Integer.parseInt(linha);
		linha = buffRead.readLine();// quantidade de Arquivos/segmentos em disco
		if (!textoValido(linha)) {
			return false;
		}
		int segmentos = Integer.parseInt(linha);
		for (int i = 0; i < segmentos; i++) { // percorre todos os segmentos
			String[] valores = buffRead.readLine().split(", ");
			if (!textoValido(valores[0]) || !textoValido(valores[1]) || !textoValido(valores[2])) {
				return false;
			}

			Arquivo arquivo = new Arquivo();
			arquivo.setNomeArquivo(valores[0]);
			arquivo.setPrimeiroBloco(Integer.parseInt(valores[1]));
			arquivo.setBlocosOcupados(Integer.parseInt(valores[2]));
			arquivo.setIdProcesso(ARQUIVO_PADRAO);
			arquivosValidados.add(arquivo);
		}

		while ((linha = buffRead.readLine()) != null) {
			Operacao operacao = null;
			String[] valores = linha.split(", ");
			if (!textoValido(valores[0]) || !textoValido(valores[1]) || !textoValido(valores[2])) {
				return false;
			}
			if (valores.length == 4) {// operacao de adicao de arquivos
				if (!textoValido(valores[3])) {
					return false;
				}
				operacao = new Operacao();
				operacao.setIdProcesso(Integer.parseInt(valores[0]));
				operacao.setCodOperacao(Integer.parseInt(valores[1]));
				operacao.setNomeArquivo(valores[2]);
				operacao.setBlocosNecessarios(Integer.parseInt(valores[3]));

			} else {
				operacao = new Operacao();
				operacao.setIdProcesso(Integer.parseInt(valores[0]));
				operacao.setCodOperacao(Integer.parseInt(valores[1]));
				operacao.setNomeArquivo(valores[2]);
			}
			operacoesValidados.add(operacao);
		}

		validado = true;
		return validado;
	}

}
