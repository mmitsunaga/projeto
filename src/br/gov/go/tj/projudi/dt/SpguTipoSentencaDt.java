package br.gov.go.tj.projudi.dt;

public class SpguTipoSentencaDt extends Dados {

	private static final long serialVersionUID = -5428016768069166855L;

	private String id;
	private String codigoSentenca;
	private String descSentenca;
	private String infoStatus;
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodigoSentenca() {
		return codigoSentenca;
	}

	public void setCodigoSentenca(String codigoSentenca) {
		this.codigoSentenca = codigoSentenca;
	}

	public String getDescSentenca() {
		return descSentenca;
	}

	public void setDescSentenca(String descSentenca) {
		this.descSentenca = descSentenca;
	}

	public String getInfoStatus() {
		return infoStatus;
	}

	public void setInfoStatus(String infoStatus) {
		this.infoStatus = infoStatus;
	}
}
