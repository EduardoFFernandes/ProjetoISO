package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import main.Main;
import models.Arquivo;
import models.Operacao;
import models.Processo;

public class ManipuladorDeArquivos {

    private String tipoArquivo;
    private File arquivo;
    @SuppressWarnings("rawtypes")
    private ArrayList objetosValidados;
    private ArrayList<Arquivo> arquivosValidados;
    private int qtdBlocosDisco;
    private int qtdArqEmDisco;
    private boolean isValidado;

    /**
     * Retorna os objetos validados pelo metodo validaArquivo
     * 
     * @return Array com os objetos validados, null se o arquivo e invalido
     */
    public ArrayList<?> getObjetosValidados() {
        return objetosValidados;
    }

    /**
     * Retorna os objetos validados pelo metodo validaArquivo
     * 
     * @return Array com os ArquivosVO validados, null se o arquivo e invalido
     */
    public ArrayList<Arquivo> getArquivosValidados() {
        return arquivosValidados;
    }

    /**
     * Verifica se o arquivo enviado e valido.
     * 
     * @return true se e valido, false caso contrario.
     */
    public boolean isFileValidated() {
        return isValidado;
    }

    /**
     * Retorna a quantidade de blocos que o disco possui.
     * 
     * @return qtdBlocosDisco se o arquivo foi validado e o arquivo enviado for do
     *         tipo Estrutura_Arquivos se nao, -1.
     */
    public int getQtdBlocosDisco() {
        if (isValidado && tipoArquivo == Main.ARQUIVOS) {
            return qtdBlocosDisco;
        }
        return -1;
    }

    /**
     * Retorna a quantidade de arquivos em disco.
     * 
     * @return qtdArqEmDisco se o arquivo foi validado e o arquivo enviado for do
     *         tipo Estrutura_Arquivos se nao, -1.
     */
    public int getQtdArqEmDisco() {
        if (isValidado && tipoArquivo == Main.ARQUIVOS) {
            return qtdArqEmDisco;
        }
        return -1;
    }
    
    /**
     * Construtor, recebe o arquivo e o tipo do mesmo.
     * 
     * @param arquivo     arquivo que sera utilizado para obter as informacoes.
     * @param tipoArquivo tipo do Arquivo, definido na Main
     * @return Um arquivo se o filepicker voltou com sucesso, null caso contrario
     */
    public ManipuladorDeArquivos(File arquivo, String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
        this.arquivo = arquivo;
        this.isValidado = false;
    }

    /**
     * Dado as informacoes recebidas no construtor, valida o arquivo e constroi as
     * estruturas de dados a partir do mesmo
     * 
     * 
     * @return Se o arquivo selecionado e valido ou nao.
     * 
     */
    public boolean validaArquivo() throws IOException {

        if (tipoArquivo.equals(Main.PROCESSOS)) {
            objetosValidados = new ArrayList<Processo>();
            return validaProcessos();
        } else {
            arquivosValidados = new ArrayList<Arquivo>();
            objetosValidados = new ArrayList<Operacao>();
            return validaEstruturaArquivos();
        }

    }
    
    /**
     * Valida especificamente arquivos de processos.
     * 
     * @return Se o arquivo de processo e valido ou nao.
     */
    @SuppressWarnings("unchecked")
    private boolean validaProcessos() throws IOException {
        BufferedReader leitorBuffer = getBufferedReaderFromFile(arquivo);
        int processId = 0;

        String linha;
        while ((linha = leitorBuffer.readLine()) != null) {
            if (linha == "") {// Se for linha vazia pula para a proxima linha.
                continue;
            }
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
            
            objetosValidados.add(processo);
            processId++;
        }

        leitorBuffer.close();
        isValidado = true;
        return isValidado;
    }

    /**
     * Valida especificamente o arquivo de estrutura de arquivos.
     * 
     * @return Se o arquivo de estruturas e valido ou nao.
     */
    @SuppressWarnings("unchecked")
    private boolean validaEstruturaArquivos() throws IOException {
        BufferedReader leitorBuffer = getBufferedReaderFromFile(arquivo);
        String linha;

        linha = leitorBuffer.readLine();// quantidade total de blocos no disco
        if (!isValidString(linha)) {
            return false;
        }
        qtdBlocosDisco = Integer.parseInt(linha);
        linha = leitorBuffer.readLine();// quantidade de Arquivos/segmentos em disco
        if (!isValidString(linha)) {
            return false;
        }
        int segmentos = Integer.parseInt(linha);
        for (int i = 0; i < segmentos; i++) { // percorre todos os segmentos
            String[] valores = leitorBuffer.readLine().split(", ");
            if (!isValidString(valores[0]) || !isValidString(valores[1]) || !isValidString(valores[2])) {
                return false;
            }
            Arquivo arquivo = new Arquivo();
            arquivo.setNomeArquivo(valores[0]);
            arquivo.setPosPrimeiroBloco(getInt(valores[1]));
            arquivo.setQtdBlocosArq(getInt(valores[2]));
            arquivo.setIdProcessoCriouArquivo(Constantes.ARQUIVO_PADRAO);
            arquivosValidados.add(arquivo);
        }

        while ((linha = leitorBuffer.readLine()) != null) {
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
            objetosValidados.add(operacao);
        }

        isValidado = true;
        return isValidado;
    }

    /**
     * Converte uma String s em inteiro.
     * 
     * @param s string a ser convertida
     * @return valor inteiro da string recebida.
     */
    private int getInt(String s) {
        return Integer.parseInt(s);
    }

    /**
     * Verifica se a string s nao e nulla e se ela nao e vazia
     * 
     * @param s string a ser verificada
     * @return true se a string e valida, false caso contrario.
     */
    private boolean isValidString(String s) {
        if (s == null || s.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Recebe um arquivo e retorna um BufferedReader para o mesmo
     * 
     * @param s arquivo a ser manuseado.
     * @return BufferedReader do arquivo enviado para a funcao.
     * @throws FileNotFoundException caso nao consiga abrir o arquivo.
     */
    private BufferedReader getBufferedReaderFromFile(File s) throws FileNotFoundException {
        FileReader fileReader = null;
        fileReader = new FileReader(s);
        return new BufferedReader(fileReader);
    }
}
