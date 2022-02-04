package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.dt.Dados;

public class UsuarioCandidatoComarcaTurnoDt extends Dados {
	
	public static final String CODIGO_SEGUNDA 	= "1";
	public static final String CODIGO_TERCA 	= "2";
	public static final String CODIGO_QUARTA 	= "3";
	public static final String CODIGO_QUINTA 	= "4";
	public static final String CODIGO_SEXTA 	= "5";
	
	public static final int SEGUNDA = 1;
	public static final int TERCA 	= 2;
	public static final int QUARTA 	= 3;
	public static final int QUINTA 	= 4;
	public static final int SEXTA 	= 5; 

	private String id;
	private String codigoDia;
	private String codigoTurnoMatutino;
	private String codigoTurnoVespertino;
	private UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCodigoDia() {
		return codigoDia;
	}
	public void setCodigoDia(String codigoDia) {
		this.codigoDia = codigoDia;
	}
	public String getCodigoTurnoMatutino() {
		return codigoTurnoMatutino;
	}
	public void setCodigoTurnoMatutino(String codigoTurnoMatutino) {
		this.codigoTurnoMatutino = codigoTurnoMatutino;
	}
	public String getCodigoTurnoVespertino() {
		return codigoTurnoVespertino;
	}
	public void setCodigoTurnoVespertino(String codigoTurnoVespertino) {
		this.codigoTurnoVespertino = codigoTurnoVespertino;
	}
	public UsuarioCandidatoComarcaDt getUsuarioCandidatoComarcaDt() {
		return usuarioCandidatoComarcaDt;
	}
	public void setUsuarioCandidatoComarcaDt(UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt) {
		this.usuarioCandidatoComarcaDt = usuarioCandidatoComarcaDt;
	}
	
	public String getPropriedades() {
		return "UsuarioCandidatoComarcaTurnoDt [id=" + id + ", codigoDia=" + codigoDia + ", codigoTurnoMatutino="
				+ codigoTurnoMatutino + ", codigoTurnoVespertino=" + codigoTurnoVespertino
				+ ", usuarioCandidatoComarcaDt=" + usuarioCandidatoComarcaDt + "]";
	}
}
