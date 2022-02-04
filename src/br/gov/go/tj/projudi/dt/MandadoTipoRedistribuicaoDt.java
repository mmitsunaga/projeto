package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class MandadoTipoRedistribuicaoDt {	 
 
	private String idMandTipoRedist;
	private String mandTipoRedist;
	private String mandTipoRedistCodigo;
	private String codigoTemp;
 
 
	public MandadoTipoRedistribuicaoDt() {
		limpar();
	}
	
	public void limpar() {
		idMandTipoRedist = "";
		mandTipoRedist = "";
		mandTipoRedistCodigo = "";
		codigoTemp = "";
	}

	public String getIdMandTipoRedist() {
		return idMandTipoRedist;
	}

	public void setIdMandTipoRedist(String idMandTipoRedist) {
		this.idMandTipoRedist = idMandTipoRedist;
	}

	public String getMandTipoRedist() {
		return mandTipoRedist;
	}

	public void setMandTipoRedist(String mandTipoRedist) {
		this.mandTipoRedist = mandTipoRedist;
	}

	public String getMandTipoRedistCodigo() {
		return mandTipoRedistCodigo;
	}

	public void setMandTipoRedistCodigo(String mandTipoRedistCodigo) {
		this.mandTipoRedistCodigo = mandTipoRedistCodigo;
	}

	public String getCodigoTemp() {
		return codigoTemp;
	}

	public void setCodigoTemp(String codigoTemp) {
		this.codigoTemp = codigoTemp;
	}

}
