package br.gov.go.tj.projudi.dt;

public class OficialSSGDt extends Dados {

	private static final long serialVersionUID = 813722138447210043L;
	
	public String nomeOficial;
	public String codigoOficial;

	public String getId() {
		return null;
	}
	
	public void setId(String id) {
	}

	public String getNomeOficial() {
		return nomeOficial;
	}

	public void setNomeOficial(String nomeOficial) {
		this.nomeOficial = nomeOficial;
	}

	public String getCodigoOficial() {
		return codigoOficial;
	}

	public void setCodigoOficial(String codigoOficial) {
		this.codigoOficial = codigoOficial;
	}	
}