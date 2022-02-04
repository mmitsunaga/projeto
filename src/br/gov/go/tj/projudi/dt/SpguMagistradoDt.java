package br.gov.go.tj.projudi.dt;

public class SpguMagistradoDt extends Dados {

	private static final long serialVersionUID = -8937051854372649307L;

	private String id;
	private String codigoMagistrado;
	private String numeroMatricula;
	private String nomeMagistrado;
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodigoMagistrado() {
		return codigoMagistrado;
	}

	public void setCodigoMagistrado(String codigoMagistrado) {
		this.codigoMagistrado = codigoMagistrado;
	}

	public String getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	public String getNomeMagistrado() {
		return nomeMagistrado;
	}

	public void setNomeMagistrado(String nomeMagistrado) {
		this.nomeMagistrado = nomeMagistrado;
	}
}