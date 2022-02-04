package br.gov.go.tj.projudi.dt;

public class EscalaTipoMgDt extends Dados {

	private static final long serialVersionUID = -8127327841052755119L;

	private String idEscalaTipoMg;
	private String escalaTipoMg;
	private String codigoEscalaTipoMg;

	public EscalaTipoMgDt() {
		limpar();
	}

	public void limpar() {
		idEscalaTipoMg = "";
		escalaTipoMg = "";
		codigoEscalaTipoMg = "";

	}

	public String getEscalaTipoMg() {
		return escalaTipoMg;
	}

	public void setEscalaTipoMg(String escalaTipoMg) {
		this.escalaTipoMg = escalaTipoMg;
	}

	public String getCodigoEscalaTipoMg() {
		return codigoEscalaTipoMg;
	}

	public void setCodigoEscalaTipoMg(String codigoEscalaTipoMg) {
		this.codigoEscalaTipoMg = codigoEscalaTipoMg;
	}

	public String getId() {
		return idEscalaTipoMg;
	}

	public void setId(String idEscalaTipoMg) {
		this.idEscalaTipoMg = idEscalaTipoMg;
	}

}
