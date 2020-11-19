package modules;

import java.awt.Color;
import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import util.Util;

import static util.Constantes.*;
import static util.Util.*;

public class Disco {

    private ArrayList<Arquivo> arquivos;
    private GerenciadorDeFilas gerenciadorDeFilas;
    private String discoAsString;
    private int blocosDisco;

    public Disco(int blocosDisco, GerenciadorDeFilas listenerSO, ArrayList<Arquivo> arquivos) {
        this.arquivos = arquivos;
        this.blocosDisco = blocosDisco;
        this.gerenciadorDeFilas = listenerSO;
        discoAsString = Util.discoEmBranco(discoAsString, blocosDisco);
    }

    /**
     * Metodo que cria os arquivos do disco.
     */
    public boolean cria(Operacao operacao, int posicaoOperacao) {
        int indiceDisponivel;
        String espacoNecessario = new String();
        for (int i = 0; i < operacao.getQtdBlocos(); i++) {
            espacoNecessario = espacoNecessario.concat("0");
        }
        if ((indiceDisponivel = discoAsString.indexOf(espacoNecessario)) != -1) {

            discoAsString = discoAsString.substring(0, indiceDisponivel)
                    .concat(espacoNecessario.replaceAll("0", operacao.getNomeArquivo()))
                    .concat(discoAsString.substring(indiceDisponivel + operacao.getQtdBlocos()));
            gerenciadorDeFilas.getTelaPrincipal().logMessage(operacoesDoSistema(posicaoOperacao, true));
            gerenciadorDeFilas.getTelaPrincipal().logMessage(salvouArquivo(operacao, indiceDisponivel));
            return true;
        } else {
            gerenciadorDeFilas.getTelaPrincipal().logMessage(operacoesDoSistema(posicaoOperacao, false));
            gerenciadorDeFilas.getTelaPrincipal().logMessage(naoSalvouArquivo(operacao), Color.RED);
            return false;
        }
    }

    /**
     * Metodo que executa as operacoes recebidas.
     */
    public void executaOperacoes(ArrayList<Operacao> operacoes, Interface telaPrincipal) {
        int posicaoOperacao = 1;
        for (Operacao operacao : operacoes) {

            if (operacao.getCodOperacao() == OP_CRIAR) {

                cria(operacao, posicaoOperacao);
            } else {
                Arquivo arq = procuraArquivo(operacao.getNomeArquivo());
                if (arq == null) {
                    telaPrincipal.logMessage(operacoesDoSistema(posicaoOperacao, false));
                    gerenciadorDeFilas.getTelaPrincipal().logMessage(arqNaoEncontrado(operacao.getNomeArquivo()));
                } else if (operacao.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
                    telaPrincipal.logMessage(operacoesDoSistema(posicaoOperacao, true));
                    deleta(arq);
                    gerenciadorDeFilas.getTelaPrincipal().logMessage(excluiuArq(operacao));
                } else if (gerenciadorDeFilas.isProcessoTempoReal(operacao.getIdProcesso())) {
                    telaPrincipal.logMessage(operacoesDoSistema(posicaoOperacao, true));
                    deleta(arq);
                    gerenciadorDeFilas.getTelaPrincipal().logMessage(excluiuArq(operacao));
                } else {
                    telaPrincipal.logMessage(operacoesDoSistema(posicaoOperacao, false));
                    gerenciadorDeFilas.getTelaPrincipal().logMessage(
                            procSemPermissaoExcluirArq(operacao.getIdProcesso(), operacao.getNomeArquivo()),
                            Interface.RED);
                }

            }
            if (!gerenciadorDeFilas.isProcessoValido(operacao.getIdProcesso())) {
                telaPrincipal.logMessage(operacoesDoSistema(posicaoOperacao, false));
                gerenciadorDeFilas.getTelaPrincipal().logMessage(NAO_EXISTE_PROCESSO);
                return;
            }
            posicaoOperacao++;
        }
    }

    /**
     * Metodo que exclui arquivos do disco.
     */
    public boolean deleta(Arquivo arquivo) {

        String espacoLivre = new String();
        for (int i = 0; i < arquivo.getQtdBlocosArq(); i++) {
            espacoLivre = espacoLivre.concat("0");
        }
        discoAsString = discoAsString.substring(0, arquivo.getPosPrimeiroBloco()).concat(espacoLivre)
                .concat(discoAsString.substring(arquivo.getPosPrimeiroBloco() + arquivo.getQtdBlocosArq()));
        return true;
    }

    public void processaArquivos() {
        for (Arquivo arquivo : arquivos) {
            int i;
            String espacoLivre = new String();
            for (i = 0; i < arquivo.getQtdBlocosArq(); i++) {
                espacoLivre = espacoLivre.concat(arquivo.getNomeArquivo());
            }
            discoAsString = discoAsString.substring(0, arquivo.getPosPrimeiroBloco()).concat(espacoLivre)
                    .concat(discoAsString.substring(arquivo.getPosPrimeiroBloco() + arquivo.getQtdBlocosArq()));

        }
    }

    private Arquivo procuraArquivo(String nome) {
        for (Arquivo arquivo : arquivos) {
            if (arquivo.getNomeArquivo().equals(nome)) {
                return arquivo;
            }
        }
        return null;
    }

    public String getDiscoAsString() {
        return discoAsString;
    }

    public void setDiscoAsString(String discoAsString) {
        this.discoAsString = discoAsString;
    }

    public int getBlocosDisco() {
        return blocosDisco;
    }

    public void setBlocosDisco(int blocosDisco) {
        this.blocosDisco = blocosDisco;
    }

    public ArrayList<Arquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(ArrayList<Arquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public GerenciadorDeFilas getListenerSO() {
        return gerenciadorDeFilas;
    }

    public void setListenerSO(GerenciadorDeFilas listenerSO) {
        this.gerenciadorDeFilas = listenerSO;
    }

}
