package br.gov.go.tj.projudi.dt;

import java.util.List;

//---------------------------------------------------------
public class ProcessoParteDebitoFisicoDt extends ProcessoParteDebitoFisicoDtGen {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3684633137703211470L;

	public static final int CodigoPermissao = 909;

	/**
	 * Foram criadas essas variáveis para auxiliar na manipulação de dados referentes ao processo, pois não compensava ter um atributo 
	 * do tipo ProcessoDt, pois a maioria dos seus atributos não seriam utilizados
	 */
	private List listaPartesPromoventes;
	private List listaPartesPromovidas;
	private List listaPartesComDebito;
	private List listaGuiasProcesso;
	
	private String enderecoPartePROAD;
	private String dataDebitoPROAD;
	private String valorCreditoPROAD;
	private List listaProcessoDebito;
	private String dividaSolidaria;

	public void limpar() {
		super.limpar();		
		listaPartesPromoventes = null;
		listaPartesPromovidas = null;
		listaPartesComDebito = null;
		listaGuiasProcesso = null;
		enderecoPartePROAD = "";
		dataDebitoPROAD = "";
		valorCreditoPROAD = "";
		listaProcessoDebito = null;
	}
	
	public void limparParcial() {
		super.limparParcial();
		dividaSolidaria = "";	
	}
	
	public void copiar(ProcessoParteDebitoFisicoDt objeto) {
		super.copiar(objeto);	
		enderecoPartePROAD = objeto.getEnderecoPartePROAD();
		dataDebitoPROAD = objeto.getDataDebitoPROAD();
		valorCreditoPROAD = objeto.getValorCreditoPROAD();
		dividaSolidaria = objeto.getDividaSolidaria();
	}

	public List getListaPartesPromoventes() {
		return listaPartesPromoventes;
	}

	public void setListaPartesPromoventes(List listaPartesPromoventes) {
		this.listaPartesPromoventes = listaPartesPromoventes;
	}
	
	public List getListaPartesPromovidas() {
		return listaPartesPromovidas;
	}

	public void setListaPartesPromovidas(List listaPartesPromovidas) {
		this.listaPartesPromovidas = listaPartesPromovidas;
	}
	
	public List getListaPartesComDebito() {
		return listaPartesComDebito;
	}

	public void setListaPartesComDebito(List listaPartesComDebito) {
		this.listaPartesComDebito = listaPartesComDebito;
	}
	
	public List getListaGuiasProcesso() {
		return listaGuiasProcesso;
	}

	public void setListaGuiasProcesso(List listaGuiasProcesso) {
		this.listaGuiasProcesso = listaGuiasProcesso;
	}
	
	public String getEnderecoPartePROAD() {
		return enderecoPartePROAD;
	}

	public void setEnderecoPartePROAD(String enderecoPartePROAD) {
		if (enderecoPartePROAD != null) this.enderecoPartePROAD = enderecoPartePROAD;
	}

	public String getDataDebitoPROAD() {
		return dataDebitoPROAD;
	}

	public void setDataDebitoPROAD(String dataDebitoPROAD) {
		if (dataDebitoPROAD != null) this.dataDebitoPROAD = dataDebitoPROAD;
	}

	public String getValorCreditoPROAD() {
		return valorCreditoPROAD;
	}

	public void setValorCreditoPROAD(String valorCreditoPROAD) {
		if (valorCreditoPROAD != null) this.valorCreditoPROAD = valorCreditoPROAD;
	}
	
	public List getListaProcessoDebito() {
		return listaProcessoDebito;
	}

	public void setListaProcessoDebito(List listaProcessoDebito) {
		this.listaProcessoDebito = listaProcessoDebito;
	}
	
	public String getDividaSolidaria() {
		return dividaSolidaria;
	}

	public void setDividaSolidaria(String dividaSolidaria) {
		if (dividaSolidaria != null) this.dividaSolidaria = dividaSolidaria;		
	}
	
	public boolean isDividaSolidaria() {
		return this.dividaSolidaria != null && this.dividaSolidaria.trim().equals("true");
	}
	
	public boolean NotIsDividaSolidaria() {
		return this.dividaSolidaria != null && this.dividaSolidaria.trim().length() > 0  && !isDividaSolidaria();
	}
}
