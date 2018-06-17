package modulos;


import models.ProcessoVO;

public class ModuloRecursos {
	private int scanner;
	private int modem;
	private int[] impressoras;
	private int[] satas;

	public ModuloRecursos() {
		this.scanner = 0;
		this.modem = 0;
		impressoras[0] = impressoras[1] = satas[0] = satas[1] = 0;
	}

	public boolean alocaTodosOsRecursosParaProcesso(ProcessoVO pr) {
		if(pr.getReqModem()>0) {
			if (modem == 0) {
				modem = pr.getPID();//modem alocado para o processoID
			}else {				
				return false;
			}
		}
		if(pr.getReqScanner()>0) {
			if (scanner == 0) {
				scanner = pr.getPID();//scanner alocado para o processoID
			}else {
				desacolaTodosOsRecursosDoProcesso(pr);
				return false;
			}
		}
		if(pr.getReqCodImpressora()==1) {
			if (impressoras[0] == 0) {
				impressoras[0] = pr.getPID();//impressora 0 alocada para o processoID 
			}else {				
				desacolaTodosOsRecursosDoProcesso(pr);
				return false;
			}
		}else if(pr.getReqCodImpressora()==2) {
			if (impressoras[1] == 0) {
				impressoras[1] = pr.getPID();//impressora 1 alocada para o processoID
			}else {		
				desacolaTodosOsRecursosDoProcesso(pr);
				return false;
			}
		}
		if(pr.getReqCodDisco()==1) {
			if (satas[0] == 0) {
				satas[0] = pr.getPID();//sata 0 alocado para o processoID 
			}else {	
				desacolaTodosOsRecursosDoProcesso(pr);
				return false;
			}
		}else if(pr.getReqCodDisco()==2) {
			if (satas[1] == 0) {
				satas[1] = pr.getPID();//sata 1 alocado para o processoID
			}else {	
				desacolaTodosOsRecursosDoProcesso(pr);
				return false;
			}
		}
		return true;
	}
	
	public boolean desacolaTodosOsRecursosDoProcesso(ProcessoVO pr) {
		if(pr.getReqModem()>0) {
			if (modem == pr.getPID()) {
				modem = 0;//modem alocado para o processoID
			}else {				
				return false;
			}
		}
		if(pr.getReqScanner()>0) {
			if (scanner == pr.getPID()) {
				scanner = 0;//scanner alocado para o processoID
			}else {				
				return false;
			}
		}
		if(pr.getReqCodImpressora()==1) {
			if (impressoras[0] == pr.getPID()) {
				impressoras[0] = 0;//impressora 0 alocada para o processoID 
			}else {				
				return false;
			}
		}else if(pr.getReqCodImpressora()==2) {
			if (impressoras[1] == pr.getPID()) {
				impressoras[1] = 0;//impressora 1 alocada para o processoID
			}else {				
				return false;
			}
		}
		if(pr.getReqCodDisco()==1) {
			if (satas[0] == pr.getPID()) {
				satas[0] = 0;//sata 0 alocado para o processoID 
			}else {				
				return false;
			}
		}else if(pr.getReqCodDisco()==2) {
			if (satas[1] == pr.getPID()) {
				satas[1] = 0;//sata 1 alocado para o processoID
			}else {				
				return false;
			}
		}
		return true;
	}
}
