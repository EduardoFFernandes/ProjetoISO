package modulos;

import java.util.ArrayList;

import models.ArquivoVO;
import models.Constantes;
import models.OperacaoNaEstruturaArquivosVO;

public class ModuloDisco  {

	private int qtdBlocosDisco;
	private ArrayList<ArquivoVO> arquivos;
	private ModuloSO listenerSO;
	private String[] blocos;

	public ModuloDisco(int qtdBlocosDisco, ModuloSO listenerSO, ArrayList<ArquivoVO> arquivos) {
		this.arquivos = arquivos;
		this.qtdBlocosDisco = qtdBlocosDisco;
		this.listenerSO = listenerSO;
		blocos = new String[qtdBlocosDisco];
		processaArquivos();
	}


	private void processaArquivos() {
		for (ArquivoVO arquivo : arquivos) {
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

	public boolean createFile(OperacaoNaEstruturaArquivosVO op) {
		boolean cabe, salvou = false;
		int qtdBlocosOp = op.getQtdBlocos();
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
					for (int y = 0; y < qtdBlocosOp; y++) {
						blocos[i + y] = op.getNomeArquivo();
					}
					salvou = true;
					break;
				}
			}
		}
		return salvou;
	}

	public boolean deleteFile(ArquivoVO arquivo) {
		for (int i = 0; i < arquivo.getQtdBlocosArq(); i++) {
			blocos[arquivo.getPosPrimeiroBloco() + i] = "0";
		}

		return true;
	}

	public void printSituacaoDisco() {
		
		StringBuilder sb = new StringBuilder();
		int printaNaTelaComQuebraDeLinha;

		sb.append(Constantes.NEWLINE.getTexto());
		sb.append(Constantes.DISCO_MAPA_OCUPACAO.getTexto());
		sb.append(Constantes.NEWLINE.getTexto());

		for (int i = 0; i < qtdBlocosDisco; i++) {
			printaNaTelaComQuebraDeLinha = i % 10;
			if (printaNaTelaComQuebraDeLinha != 0) {
				sb.append("| " + blocos[i] + " |");
			} else {
				sb.append(Constantes.NEWLINE.getTexto());
				sb.append("| " + blocos[i] + " |");
			}
		}
		listenerSO.escreveNaTela(sb.toString());
	}

	public boolean executaOperacao(OperacaoNaEstruturaArquivosVO op) {

		 if (op.getCodOperacao() == OperacaoNaEstruturaArquivosVO.OP_CRIAR) {
//		if (op.getCodOperacao() == OperacaoNaEstruturaArquivosVO.OP_CRIAR) {
			// operação de criar arquivo
			return createFile(op);
		} else {
			// operação de excluir arquivo
			ArquivoVO arq = procuraArquivoNoDisco(op.getNomeArquivo());
			if (arq == null) {
				// se não encontrou arquivo.
				listenerSO.escreveNaTela("Arquivo: " + op.getNomeArquivo() + ", não encontrado.");
				return false;
			}
			// encontrou arquivo
			if (op.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
				// processo é o mesmo que criou o arquivo
				deleteFile(arq);
			} else if (listenerSO.isProcessoTempoReal(op.getIdProcesso())) {
				// processo não é o que criou o arquivo mas é de tempo real
				deleteFile(arq);
			} else {
				// processo não é o que criou o arquivo e não é de tempo real
				listenerSO.escreveNaTela(Constantes.DISCO_PROCESSO_SEM_PERMISSAO.getTexto() + op.getNomeArquivo(),
						ModuloTelaPrincipal.RED);
				return false;
			}

		}

		return false;
	}

	private ArquivoVO procuraArquivoNoDisco(String nome) {
		for (ArquivoVO arquivo : arquivos) {
			if (arquivo.getNomeArquivo().equals(nome)) {
				return arquivo;
			}
		}
		return null;
	}
}
