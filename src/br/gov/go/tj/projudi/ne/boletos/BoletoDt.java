package br.gov.go.tj.projudi.ne.boletos;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class BoletoDt extends GuiaEmissaoDt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1224275326695504961L;
	
	private SituacaoBoleto situacaoBoleto;
	private String nossoNumero;
	private String numeroDocumento;
	private String dataEmissaoBoleto;
	private String dataVencimentoBoleto;
	private String valorBoleto;
	private String codigoDeBarras;
	private String linhaDigitavel;
	private String urlPdf;
	private PagadorBoleto pagador = new PagadorBoleto();
	private String observacao1;
	private String observacao2;
	private String observacao3;
	private String observacao4;
	private boolean isObservacaoFoiAterada;
	
	public void copiar(BoletoDt boleto) {
		super.copiar(boleto);
		situacaoBoleto = boleto.situacaoBoleto;
		numeroDocumento = boleto.numeroDocumento;
		nossoNumero = boleto.nossoNumero;
		dataEmissaoBoleto = boleto.dataEmissaoBoleto;
		dataVencimentoBoleto = boleto.dataVencimentoBoleto;
		valorBoleto = boleto.valorBoleto;
		codigoDeBarras = boleto.codigoDeBarras;
		linhaDigitavel = boleto.linhaDigitavel;
		urlPdf = boleto.urlPdf;
		pagador.setCpf(boleto.pagador.getCpf());
		pagador.setNome(boleto.pagador.getNome());
		pagador.setCnpj(boleto.pagador.getCnpj());
		pagador.setRazaoSocial(boleto.pagador.getRazaoSocial());
		pagador.setLogradouro(boleto.pagador.getLogradouro());
		pagador.setBairro(boleto.pagador.getBairro());
		pagador.setCidade(boleto.pagador.getCidade());
		pagador.setUf(boleto.pagador.getUf());
		pagador.setCep(boleto.pagador.getCep());
		pagador.setTipoPessoa(boleto.pagador.getTipoPessoa());
		observacao1 = boleto.getObservacao1();
		observacao2 = boleto.getObservacao2();
		observacao3 = boleto.getObservacao3();
		observacao4 = boleto.getObservacao4();		
	}
	
	@Override
	public void limpar() {
		super.limpar();
		situacaoBoleto = SituacaoBoleto.NAO_REGISTRADO;
		numeroDocumento = "";
		nossoNumero = "";
		dataEmissaoBoleto = "";
		dataVencimentoBoleto = "";
		valorBoleto = "";
		codigoDeBarras = "";
		linhaDigitavel = "";
		urlPdf = "";
		pagador = new PagadorBoleto();
		observacao1 = "";
		observacao2 = "";
		observacao3 = "";
		observacao4 = "";
		setObservacaoFoiAterada(false);
	}
	
	public boolean isVencido() throws Exception {
		TJDataHora dataAtual = new TJDataHora();
		dataAtual.atualizeUltimaHoraDia();
		TJDataHora vencimentoBoleto = null;
		if (dataVencimentoBoleto != null && dataVencimentoBoleto.contains("/")) {
			vencimentoBoleto = new TJDataHora(Funcoes.StringToDate(dataVencimentoBoleto));
		} else {
			vencimentoBoleto = new TJDataHora(Funcoes.Stringyyyy_MM_ddToDateTime(dataVencimentoBoleto));
		}		
		vencimentoBoleto.atualizeUltimaHoraDia();
		return dataAtual.ehApos(vencimentoBoleto);
	}
	
	public SituacaoBoleto getSituacaoBoleto() {
		return situacaoBoleto;
	}
	
	public void setSituacaoBoleto(SituacaoBoleto situacaoBoleto) {
		if (situacaoBoleto != null)
			this.situacaoBoleto = situacaoBoleto;
	}
	
	public String getNossoNumero() {
		return nossoNumero;
	}
	
	public void setNossoNumero(String nossoNumero) {
		if (nossoNumero != null)
			this.nossoNumero = nossoNumero;
	}
	
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	
	public void setNumeroDocumento(String numeroDocumento) {
		if (numeroDocumento != null)
			this.numeroDocumento = numeroDocumento;
	}
	
	public String getDataEmissaoBoleto() {
		return dataEmissaoBoleto;
	}
	
	public void setDataEmissaoBoleto(String dataEmissaoBoleto) {
		if (dataEmissaoBoleto != null)
			this.dataEmissaoBoleto = dataEmissaoBoleto;
	}
	
	public String getDataVencimentoBoleto() {
		return dataVencimentoBoleto;
	}
	
	public void setDataVencimentoBoleto(String dataVencimentoBoleto) {
		if (dataVencimentoBoleto != null)
			this.dataVencimentoBoleto = dataVencimentoBoleto;
	}
	
	public String getValorBoleto() {
		return valorBoleto;
	}
	
	public void setValorBoleto(String valorBoleto) {
		if (valorBoleto != null)
			this.valorBoleto = valorBoleto;
	}
	
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}
	
	public void setCodigoDeBarras(String codigoDeBarras) {
		if (codigoDeBarras != null)
			this.codigoDeBarras = codigoDeBarras;
	}
	
	public String getLinhaDigitavel() {
		return linhaDigitavel;
	}
	
	public void setLinhaDigitavel(String linhaDigitavel) {
		if (linhaDigitavel != null)
			this.linhaDigitavel = linhaDigitavel;
	}
	
	public String getUrlPdf() {
		return urlPdf;
	}
	
	public void setUrlPdf(String urlPdf) {
		if (urlPdf != null)
			this.urlPdf = urlPdf;
	}
	
	public PagadorBoleto getPagador() {
		if (pagador == null)
			pagador = new PagadorBoleto();
		return pagador;
	}

	public boolean isMudouPagador() {
		return getPagador().isMudouPagador();
	}
	
	public boolean isValorGuiaDiferenteValorBoleto() throws Exception {
		return Funcoes.StringToDouble(this.getValorBoleto()) != this.getValorTotalGuiaDouble();
	}
	
	public boolean isVencimentoGuiaPosteriorVencimentoBoleto() throws Exception {
		TJDataHora vencimentoBoleto = null;
		if (dataVencimentoBoleto != null && dataVencimentoBoleto.contains("/")) {
			vencimentoBoleto = new TJDataHora(Funcoes.StringToDate(dataVencimentoBoleto));
		} else {
			vencimentoBoleto = new TJDataHora(Funcoes.Stringyyyy_MM_ddToDateTime(dataVencimentoBoleto));
		}		
		vencimentoBoleto.atualizeUltimaHoraDia();
		
		TJDataHora vencimentoGuia = null;
		if (getDataVencimento() != null && getDataVencimento().contains("/")) {
			vencimentoGuia = new TJDataHora(Funcoes.StringToDate(getDataVencimento()));
		} else {
			vencimentoGuia = new TJDataHora(Funcoes.Stringyyyy_MM_ddToDateTime(getDataVencimento()));
		}		
		vencimentoBoleto.atualizeUltimaHoraDia();
		
		return vencimentoGuia.ehApos(vencimentoBoleto);
	}
	
	public String getObservacao1() {
		return observacao1;
	}

	public void setObservacao1(String observacao1) {
		if (observacao1 == null || observacao1.trim().length() == 0) return;
		if (this.observacao1 != null && this.observacao1.trim().length() > 0 && this.observacao1.equalsIgnoreCase(observacao1.trim())) 
			this.setObservacaoFoiAterada(true);
		if ((this.isGuiaEmitidaSPG() || this.isGuiaEmitidaSSG()) && (this.observacao1 == null || this.observacao1.trim().length() == 0))
			this.setObservacaoFoiAterada(true);
		this.observacao1 = observacao1;
	}
	
	public boolean isObservacao1Informada() {		
		return (this.observacao1 != null && this.observacao1.trim().length() > 0);		
	}
	
	public String getObservacao2() {
		return observacao2;
	}

	public void setObservacao2(String observacao2) {
		if (observacao2 == null || observacao2.trim().length() == 0) return;
		if (this.observacao2 != null && this.observacao2.trim().length() > 0 && this.observacao2.equalsIgnoreCase(observacao2.trim())) 
			this.setObservacaoFoiAterada(true);
		if ((this.isGuiaEmitidaSPG() || this.isGuiaEmitidaSSG()) && (this.observacao2 == null || this.observacao2.trim().length() == 0))
			this.setObservacaoFoiAterada(true);
		this.observacao2 = observacao2;
	}
	
	public boolean isObservacao2Informada() {		
		return (this.observacao2 != null && this.observacao2.trim().length() > 0);		
	}
	
	public String getObservacao3() {
		return observacao3;
	}

	public void setObservacao3(String observacao3) {
		if (observacao3 == null || observacao3.trim().length() == 0) return;
		if (this.observacao3 != null && this.observacao3.trim().length() > 0 && this.observacao3.equalsIgnoreCase(observacao3.trim())) 
			this.setObservacaoFoiAterada(true);
		if ((this.isGuiaEmitidaSPG() || this.isGuiaEmitidaSSG()) && (this.observacao3 == null || this.observacao3.trim().length() == 0))
			this.setObservacaoFoiAterada(true);
		this.observacao3 = observacao3;
	}
	
	public boolean isObservacao3Informada() {		
		return (this.observacao3 != null && this.observacao3.trim().length() > 0);		
	}
	
	public String getObservacao4() {
		return observacao4;
	}

	public void setObservacao4(String observacao4) {
		if (observacao4 == null || observacao4.trim().length() == 0) return;
		if (this.observacao4 != null && this.observacao4.trim().length() > 0 && this.observacao4.equalsIgnoreCase(observacao4.trim())) 
			this.setObservacaoFoiAterada(true);
		if ((this.isGuiaEmitidaSPG() || this.isGuiaEmitidaSSG()) && (this.observacao4 == null || this.observacao4.trim().length() == 0))
			this.setObservacaoFoiAterada(true);
		this.observacao4 = observacao4;
	}
	
	public boolean isObservacao4Informada() {		
		return (this.observacao4 != null && this.observacao4.trim().length() > 0);		
	}
	
	public boolean isPossuiObservacao() throws Exception {
		return this.isObservacao1Informada() || isObservacao2Informada() || isObservacao3Informada() || isObservacao4Informada();
	}

	public boolean isObservacaoFoiAterada() {
		return isObservacaoFoiAterada;
	}

	public void setObservacaoFoiAterada(boolean isObservacaoFoiAterada) {
		this.isObservacaoFoiAterada = isObservacaoFoiAterada;
	}
}
