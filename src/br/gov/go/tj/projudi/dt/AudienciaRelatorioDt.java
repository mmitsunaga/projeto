package br.gov.go.tj.projudi.dt;


public class AudienciaRelatorioDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1389180065200494840L;
	private String tipoAudiencia;
	private String hora;
	private String numeroProcesso;
	private String promoventes;
	private String promovidos;
	private String status;
	
	
	public String getTipoAudiencia() {
		return tipoAudiencia;
	}
	public void setTipoAudiencia(String tipoAudiencia) {
		this.tipoAudiencia = tipoAudiencia;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getNumeroProcesso() {
		return numeroProcesso;
	}
	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	public String getPromoventes() {
		return promoventes;
	}
	public void setPromoventes(String promoventes) {
		this.promoventes = promoventes;
	}
	public String getPromovidos() {
		return promovidos;
	}
	public void setPromovidos(String promovidos) {
		this.promovidos = promovidos;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getId() {
		
		return null;
	}
	@Override
	public void setId(String id) {
		
		
	}
}
