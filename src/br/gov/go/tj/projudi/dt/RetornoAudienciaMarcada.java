package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class RetornoAudienciaMarcada  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1333086496007659177L;
	
	private String tipoAudiencia;
	private String dataAudiencia;
	private String serventia;
	
	public String getTipoAudiencia() {
		return tipoAudiencia;
	}
	public void setTipoAudiencia(String tipoAudiencia) {
		this.tipoAudiencia = tipoAudiencia;
	}
	public String getDataAudiencia() {
		return dataAudiencia;
	}
	public void setDataAudiencia(String dataAudiencia) {
		this.dataAudiencia = dataAudiencia;
	}
	public String getServentia() {
		return serventia;
	}
	public void setServentia(String serventia) {
		this.serventia = serventia;
	}
	
}
