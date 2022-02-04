package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class InversaoPolosDt  extends Dados {	
    /**
	 * 
	 */
	private static final long serialVersionUID = -779289356586960339L;

	public static final int CodigoPermissao = 756;	
	
	private ProcessoDt processoDt;
	
	private MovimentacaoProcessoDt movimentacaoDt;
	
	private List listaRecorrentesInversaoPolos;
	
	private List listaRecorridosInversaoPolos;
	
	private List listaPromoventesInversaoPolos;
	
	private List listaPromovidosInversaoPolos;

	public void limpar() {	
		processoDt = null;
		movimentacaoDt = null;
		setListaRecorrentesInversaoPolos(null);		
		setListaRecorridosInversaoPolos(null);
		setListaPromoventesInversaoPolos(null);
		setListaPromovidosInversaoPolos(null);
	}

	@Override
	public String getId() {
		if (processoDt != null) return processoDt.getId();
		return "";
	}

	@Override
	public void setId(String id) {
				
	}
	
	public ProcessoDt getProcessoCompletoDt() {
		return processoDt;
	}

	public void setProcessoCompletoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}
	
	public MovimentacaoProcessoDt getMovimentacaoProcessoDt() {
		return movimentacaoDt;
	}

	public void setMovimentacaoProcessoDt(MovimentacaoProcessoDt movimentacaoDt) {
		this.movimentacaoDt = movimentacaoDt;
	}
	
	public boolean isRecurso()
	{
		return (this.processoDt != null && this.processoDt.getId_Recurso() != null && this.processoDt.getId_Recurso().trim().length() > 0 && this.processoDt.getRecursoDt() != null);
	}
	
	public RecursoDt getRecursoDt()
	{
		if (this.processoDt != null) return this.processoDt.getRecursoDt();
		return null;
	}

	public void setListaRecorrentesInversaoPolos(List listaRecorrentesInversaoPolos) {
		this.listaRecorrentesInversaoPolos = listaRecorrentesInversaoPolos;
	}

	public List getListaRecorrentesInversaoPolos() {
		if (listaRecorrentesInversaoPolos == null) listaRecorrentesInversaoPolos = new ArrayList();
		return listaRecorrentesInversaoPolos;
	}

	public void setListaRecorridosInversaoPolos(List listaRecorridosInversaoPolos) {
		this.listaRecorridosInversaoPolos = listaRecorridosInversaoPolos;
	}

	public List getListaRecorridosInversaoPolos() {
		if (listaRecorridosInversaoPolos == null) listaRecorridosInversaoPolos = new ArrayList();
		return listaRecorridosInversaoPolos;
	}

	public void setListaPromoventesInversaoPolos(List listaPromoventesInversaoPolos) {
		this.listaPromoventesInversaoPolos = listaPromoventesInversaoPolos;
	}

	public List getListaPromoventesInversaoPolos() {
		if (listaPromoventesInversaoPolos == null) listaPromoventesInversaoPolos = new ArrayList();
		return listaPromoventesInversaoPolos;
	}

	public void setListaPromovidosInversaoPolos(List listaPromovidosInversaoPolos) {
		this.listaPromovidosInversaoPolos = listaPromovidosInversaoPolos;
	}

	public List getListaPromovidosInversaoPolos() {
		if (listaPromovidosInversaoPolos == null) listaPromovidosInversaoPolos = new ArrayList();
		return listaPromovidosInversaoPolos;
	}	
	
}