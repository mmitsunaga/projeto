package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que encapsula os dados da intimação para publicação no Diário Oficial Eletrônico
 * @author mmitsunaga
 *
 */
public class ProcessoIntimacaoDt implements Serializable {

	private static final long serialVersionUID = -531895994159165938L;

	private ProcessoDt processoDt = null;
	
	private List<PendenciaDt> listaPendenciaDt = null;

	private List<ArquivoDt> listaArquivoDt = null;
	
	public ProcessoIntimacaoDt() {
		
	}
	
	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public List<PendenciaDt> getListaPendenciaDt() {
		return listaPendenciaDt;
	}

	public void setListaPendenciaDt(List<PendenciaDt> listaPendenciaDt) {
		this.listaPendenciaDt = listaPendenciaDt;
	}
	
	public void addPendenciaDt(PendenciaDt novaPendencia){
		if (listaPendenciaDt == null) listaPendenciaDt = new ArrayList<PendenciaDt>();
		listaPendenciaDt.add(novaPendencia);
	}

	public List<ArquivoDt> getListaArquivoDt() {
		return listaArquivoDt;
	}

	public void setListaArquivoDt(List<ArquivoDt> listaArquivoDt) {
		this.listaArquivoDt = listaArquivoDt;
	}
	
	public void addArquivoDt(ArquivoDt novoArquivo){
		if (listaArquivoDt == null) listaArquivoDt = new ArrayList<ArquivoDt>();
		listaArquivoDt.add(novoArquivo);
	}
	
	public PendenciaDt getPrimeiraPendencia(){
		return (listaPendenciaDt != null) ? listaPendenciaDt.get(0) : null;
	}
	
	public ArquivoDt getArquivoIntimacao(){
		return (listaArquivoDt != null) ? listaArquivoDt.get(0) : null;
	}
	
}

