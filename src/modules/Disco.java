package modules;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import static util.Constantes.*;
import static util.Util.*;

public class Disco {

    private int qtdBlocosDisco;
    private ArrayList<Arquivo> arquivos;
    private GerenciadorDeFilas listenerSO;
    private String[] blocos;

    public Disco(int qtdBlocosDisco, GerenciadorDeFilas listenerSO, ArrayList<Arquivo> arquivos) {
        this.arquivos = arquivos;
        this.qtdBlocosDisco = qtdBlocosDisco;
        this.listenerSO = listenerSO;
        blocos = new String[qtdBlocosDisco];
        processaArquivos();
    }

    public boolean createFile(Operacao operacao) {
        boolean cabe, salvou = false;
        int qtdBlocosOp = operacao.getQtdBlocos();
        int blocoInicioArq = -1;
        for (int i = 0; i < qtdBlocosDisco; i++) {
            cabe = true;
            if (i + qtdBlocosOp > qtdBlocosDisco) {
                break;
            }
            if (blocos[i] == "0") {
                for (int y = 0; y < qtdBlocosOp; y++) {
                    if (blocos[i + y] != "0") {
                        cabe = false;
                        break;
                    }
                }
                if (cabe) {
                    blocoInicioArq = i;
                    for (int y = 0; y < qtdBlocosOp; y++) {
                        blocos[blocoInicioArq + y] = operacao.getNomeArquivo();
                    }
                    salvou = true;
                    break;
                }
            }
        }

        if (salvou) {
            listenerSO.escreveNaTela(salvouArquivo(operacao, blocoInicioArq));
        } else {
            listenerSO.escreveNaTela(naoSalvouArquivo(operacao), Color.RED);
        }
        return salvou;
    }

    public boolean deleteFile(Arquivo arquivo) {
        for (int i = 0; i < arquivo.getQtdBlocosArq(); i++) {
            blocos[arquivo.getPosPrimeiroBloco() + i] = "0";
        }

        return true;
    }

    public void printSituacaoDisco() {

        StringBuilder sb = new StringBuilder();
        int printaNaTelaComQuebraDeLinha;

        sb.append(NEWLINE);
        sb.append(DISCO_MAPA_OCUPACAO);
        sb.append(NEWLINE);

        for (int i = 0; i < qtdBlocosDisco; i++) {
            printaNaTelaComQuebraDeLinha = i % 10;
            if (printaNaTelaComQuebraDeLinha != 0) {
                sb.append("| " + blocos[i] + " |");
            } else {
                sb.append(NEWLINE);
                sb.append("| " + blocos[i] + " |");
            }
        }
        listenerSO.escreveNaTela(sb.toString());
    }

    public void executaOperacao(Operacao operacao) {
        if (!listenerSO.isProcessoValido(operacao.getIdProcesso())) {
            listenerSO.escreveNaTela(NAO_EXISTE_PROCESSO);
            return;
        }
        if (operacao.getCodOperacao() == OP_CRIAR) {
            // operacao de criar arquivo
            createFile(operacao);
        } else {
            // operacao de excluir arquivo
            Arquivo arq = procuraArquivoNoDisco(operacao.getNomeArquivo());
            if (arq == null) {
                // se nao encontrou arquivo.
                listenerSO.escreveNaTela(arqNaoEncontrado(operacao.getNomeArquivo()));
            }
            // encontrou arquivo
            if (operacao.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
                // processo e o mesmo que criou o arquivo
                deleteFile(arq);
                listenerSO.escreveNaTela(excluiuArq(operacao));
            } else if (listenerSO.isProcessoTempoReal(operacao.getIdProcesso())) {
                // processo nao e o que criou o arquivo mas e de tempo real
                deleteFile(arq);
                listenerSO.escreveNaTela(excluiuArq(operacao));
            } else {
                // processo nao e o que criou o arquivo e nao e de tempo real
                listenerSO.escreveNaTela(procSemPermissaoExcluirArq(operacao.getIdProcesso(), operacao.getNomeArquivo()),
                        Interface.RED);
            }

        }
    }

    private void processaArquivos() {
        for (Arquivo arquivo : arquivos) {
            int i;
            for (i = 0; i < arquivo.getQtdBlocosArq(); i++) {
                blocos[arquivo.getPosPrimeiroBloco() + i] = arquivo.getNomeArquivo();
            }
        }
        for (int i = 0; i < qtdBlocosDisco; i++) {
            if (blocos[i] == null) {
                blocos[i] = "0";
            }
        }
    }

    private Arquivo procuraArquivoNoDisco(String nome) {
        for (Arquivo arquivo : arquivos) {
            if (arquivo.getNomeArquivo().equals(nome)) {
                return arquivo;
            }
        }
        return null;
    }
}
