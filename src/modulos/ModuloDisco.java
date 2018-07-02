package modulos;

import java.util.ArrayList;

import models.ArquivoVO;
import models.Constantes;
import models.OperacaoNaEstruturaArquivosVO;

public class ModuloDisco  {

	private int qtdBlocosDisco;
	private ArrayList<ArquivoVO> iNodes;
	private ModuloSO listenerSO;
	private String[] blocos;

	public ModuloDisco(int qtdBlocosDisco, ModuloSO listenerSO, ArrayList<ArquivoVO> arquivos) {
		this.iNodes = arquivos;
		this.qtdBlocosDisco = qtdBlocosDisco;
		this.listenerSO = listenerSO;
		blocos = new String[qtdBlocosDisco];
		processaArquivos();
	}


	private void processaArquivos() {
		for (ArquivoVO arquivo : iNodes) {
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

	public void executaOperacao(OperacaoNaEstruturaArquivosVO op, int opNum) {
		listenerSO.escreveNaTela(Constantes.sysArqOp(opNum));
		if(!listenerSO.isProcessoValido(op.getIdProcesso())) {
			listenerSO.escreveNaTela(Constantes.N_EXISTE_PROC.getTexto());
			return;
		}
		 if (op.getCodOperacao() == OperacaoNaEstruturaArquivosVO.OP_CRIAR) {
			// operação de criar arquivo
			 createFile(op);
		} else {
			// operação de excluir arquivo
			ArquivoVO arq = procuraArquivoNoDisco(op.getNomeArquivo());
			if (arq == null) {
				// se não encontrou arquivo.
				listenerSO.escreveNaTela(Constantes.arqNaoEncontrado(op.getNomeArquivo()));
			}
			// encontrou arquivo
			if (op.getIdProcesso() == arq.getIdProcessoCriouArquivo()) {
				// processo é o mesmo que criou o arquivo
				deleteFile(arq);
				listenerSO.escreveNaTela(Constantes.excluiuArq(op));
			} else if (listenerSO.isProcessoTempoReal(op.getIdProcesso())) {
				// processo não é o que criou o arquivo mas é de tempo real
				deleteFile(arq);
				listenerSO.escreveNaTela(Constantes.excluiuArq(op));
			} else {
				// processo não é o que criou o arquivo e não é de tempo real
				listenerSO.escreveNaTela(Constantes.procSemPermissaoExcluirArq(op.getIdProcesso(),op.getNomeArquivo()),
						ModuloTelaPrincipal.RED);
			}

		}
	}

	private ArquivoVO procuraArquivoNoDisco(String nome) {
		for (ArquivoVO arquivo : iNodes) {
			if (arquivo.getNomeArquivo().equals(nome)) {
				return arquivo;
			}
		}
		return null;
	}
}
