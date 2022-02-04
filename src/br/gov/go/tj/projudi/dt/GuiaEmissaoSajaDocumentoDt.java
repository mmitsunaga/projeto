package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class GuiaEmissaoSajaDocumentoDt  implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5782131313518187163L;
	
	private GuiaEmissaoDt guiaEmissaoDt;
	private SajaDocumentoDt sajaDocumento;
	
	public GuiaEmissaoDt getGuiaEmissaoDt() {
		return guiaEmissaoDt;
	}
	
	public void setGuiaEmissaoDt(GuiaEmissaoDt guiaEmissaoDt) {
		this.guiaEmissaoDt = guiaEmissaoDt;
	}
	
	public SajaDocumentoDt getSajaDocumento() {
		return sajaDocumento;
	}
	
	public void setSajaDocumento(SajaDocumentoDt sajaDocumento) {
		this.sajaDocumento = sajaDocumento;
	}		
}