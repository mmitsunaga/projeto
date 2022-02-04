package br.gov.go.tj.projudi.dt;

public class SajaDocumentoDt extends Dados {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8025211315428805478L;
	
	private String ISNSajaDocumentos;
	private String NumeroDocumento;
	private String NumeroSerieDocumento;
	private String TipoDeDocumento;
	private String CodigoServentia;
	private String CodigoComarca;
	private String CodigoNatureza;
	private String ValorCausa;
	private String TipoCustas;
	private String CodigoDocumento;

	@Override
	public String getId() {
		return ISNSajaDocumentos;
	}

	@Override
	public void setId(String id) {
		ISNSajaDocumentos = id;
	}

	public String getNumeroDocumento() {
		return NumeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		NumeroDocumento = numeroDocumento;
	}

	public String getNumeroSerieDocumento() {
		return NumeroSerieDocumento;
	}

	public void setNumeroSerieDocumento(String numeroSerieDocumento) {
		NumeroSerieDocumento = numeroSerieDocumento;
	}

	public String getTipoDeDocumento() {
		return TipoDeDocumento;
	}

	public void setTipoDeDocumento(String tipoDeDocumento) {
		TipoDeDocumento = tipoDeDocumento;
	}

	public String getCodigoServentia() {
		return CodigoServentia;
	}

	public void setCodigoServentia(String codigoServentia) {
		CodigoServentia = codigoServentia;
	}

	public String getCodigoComarca() {
		return CodigoComarca;
	}

	public void setCodigoComarca(String codigoComarca) {
		CodigoComarca = codigoComarca;
	}

	public String getCodigoNatureza() {
		return CodigoNatureza;
	}

	public void setCodigoNatureza(String codigoNatureza) {
		CodigoNatureza = codigoNatureza;
	}

	public String getValorCausa() {
		return ValorCausa;
	}

	public void setValorCausa(String valorCausa) {
		ValorCausa = valorCausa;
	}

	public String getTipoCustas() {
		return TipoCustas;
	}

	public void setTipoCustas(String tipoCustas) {
		TipoCustas = tipoCustas;
	}

	public String getCodigoDocumento() {
		return CodigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		CodigoDocumento = codigoDocumento;
	}	
}