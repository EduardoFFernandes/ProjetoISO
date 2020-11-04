package modules;

import java.util.ArrayList;

import models.Arquivo;
import models.Operacao;
import util.Constantes;

public class Disco  {

	private int qtdBlocosDisco;
	private ArrayList<Arquivo> iNodes;
	private GerenciadorDeFilas listenerSO;
	private String[] blocos;

	public Disco(int qtdBlocosDisco, GerenciadorDeFilas listenerSO, ArrayList<Arquivo> arquivos) {
		this.iNodes = arquivos;
		this.qtdBlocosDisco = qtdBlocosDisco;
		this.listenerSO = listenerSO;
		blocos = new String[qtdBlocosDisco];
		processaArquivos();
	}


	private void processaArquivos() {
		for (Arquivo arquivo : iNodes) {
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

	public boolean createFile(Operacao op) {
		boolean cabe, salvou = false;
		int qtdBlocosOp = op.getQtdBlocos();
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
						blocos[blocoInicioArq + y] = op.getNomeArquivo();
					}
					salvou = true;
					break;
				}
			}
		}
		
		if(salvou) {
			listenerSO.escreveNaTela(Constantes.salvouArq(op,blocoInicioArq));
		}else {
			listenerSO.escreveNaTela(Constantes.naoSalvouArq(op));
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

		sb.append(Constantes.NEWLINE);
		sb.append(Constantes.DISCO_MAPA_OCUPACAO);
		sb.append(Constantes.NEWLINE);

		for (int i = 0; i < qtdBlocosDisco; i++) {
			printaNaTelaComQuebraDeLinha = i % 10;
			if (printaNaTelaComQuebraDeLinha != 0) {
				sb.append("| " + blocos[i] + " |");
			} else {
				sb.append(Constantes.NEWLINE);
				sb.append("| " + blocos[i] + " |");
			}
		}
		listenerSO.escreveNaTela(sb.toString());
	}

	public void executaOperacao(Operacao op, int opNum) {
		listenerSO.escreveNaTela(Constantes.operacoesDoSistema(opNum));
		if(!listenerSO.isProcessoValido(op.getIdProcesso())) {
			listenerSO.escreveNaTela(Constantes.NAO_EXISTE_PROCESSO);
			return;
		}
		 if (op.getCodOperacao() == Operacao.OP_CRIAR) {
			// operacao de criar arquivo
			 createFile(op);
		} else {
			// operacao de excluir arquivo
			Arquivo arq = procuraArquivoNoDisco(op.getNomeArquivo());
			if (arq == null) {
				// se nao encontrou arquivo.
				listenerSO.escreveNaTela(Constantes.arqNaoEncontrado(op.getNomeArquivo()));
			}
			// encontrou arquivo
			if (op.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
				// processo e o mesmo que criou o arquivo
				deleteFile(arq);
				listenerSO.escreveNaTela(Constantes.excluiuArq(op));
			} else if (listenerSO.isProcessoTempoReal(op.getIdProcesso())) {
				// processo nao e o que criou o arquivo mas e de tempo real
				deleteFile(arq);
				listenerSO.escreveNaTela(Constantes.excluiuArq(op));
			} else {
				// processo nao e o que criou o arquivo e nao e de tempo real
				listenerSO.escreveNaTela(Constantes.procSemPermissaoExcluirArq(op.getIdProcesso(),op.getNomeArquivo()),
						Interface.RED);
			}

		}
	}

	private Arquivo procuraArquivoNoDisco(String nome) {
		for (Arquivo arquivo : iNodes) {
			if (arquivo.getNomeArquivo().equals(nome)) {
				return arquivo;
			}
		}
		return null;
	}
}
