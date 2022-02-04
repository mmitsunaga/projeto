package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ProcessoEncaminhamentoDt extends ProcessoEncaminhamentoDtGen{

	private static final long serialVersionUID = 4437941423396203042L;
	public static final int CodigoPermissao=840;

	String idAreaDistribuicao;
	String areaDistribuicao;
	String idServentia;
	String serventia;
	String idServentiaCargo;
	String serventiaCargo;
	
	public ProcessoEncaminhamentoDt() {
		limpar();
	}

	public void limpar() {
		super.limpar();
		idAreaDistribuicao = "";
		areaDistribuicao = "";
		idServentia = "";
		serventia = "";
		idServentiaCargo = "";
		serventiaCargo = "";
	}

	public String getIdAreaDistribuicao() {
		return idAreaDistribuicao;
	}

	public void setIdAreaDistribuicao(String idAreaDistribuicao) {
		this.idAreaDistribuicao = idAreaDistribuicao;
	}

	public String getAreaDistribuicao() {
		return areaDistribuicao;
	}

	public void setAreaDistribuicao(String areaDistribuicao) {
		this.areaDistribuicao = areaDistribuicao;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getIdServentiaCargo() {
		return idServentiaCargo;
	}

	public void setIdServentiaCargo(String idServentiaCargo) {
		this.idServentiaCargo = idServentiaCargo;
	}

	public String getServentiaCargo() {
		return serventiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		this.serventiaCargo = serventiaCargo;
	}
	
}
