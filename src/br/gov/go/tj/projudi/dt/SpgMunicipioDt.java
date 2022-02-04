package br.gov.go.tj.projudi.dt;

public class SpgMunicipioDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8620586534821910902L;
	
	String Municipio ="";
	String MunicipioCodigo ="";
	
	public void setId(String id) {
		MunicipioCodigo = id;
		
	}
	
	public String getMunicipio() {
		return Municipio;
	}

	public void setMunicipio(String municipio) {
		Municipio = municipio;
	}

	public String getMunicipioCodigo() {
		return MunicipioCodigo;
	}

	public void setMunicipioCodigo(String municipioCodigo) {
		MunicipioCodigo = municipioCodigo;
	}

	public String getId() {
		
		return MunicipioCodigo;
	}
	

}
