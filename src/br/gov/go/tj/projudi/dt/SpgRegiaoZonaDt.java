package br.gov.go.tj.projudi.dt;

public class SpgRegiaoZonaDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5758103749863886621L;
	
	//é o codigo do muncipio mais o de bairro
	String Id="";
	String RegiaoCodigo ="";
	String ZonaCodigo ="";
	
	public String getRegiaoCodigo() {
		return RegiaoCodigo;
	}


	public void setRegiaoCodigo(String RegiaoCodigo) {
		this.RegiaoCodigo = RegiaoCodigo;
	}


	public String getZonaCodigo() {
		return ZonaCodigo;
	}


	public void setZonaCodigo(String ZonaCodigo) {
		this.ZonaCodigo = ZonaCodigo;
	}


	public void setId(String id) {
		Id=id;	
	}

	
	public String getId() {
		return Id;
	}
	
	

}
