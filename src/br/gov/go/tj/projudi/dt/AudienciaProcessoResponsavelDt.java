package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoResponsavelDt extends AudienciaProcessoResponsavelDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -4910762728472474053L;

    public static final int CodigoPermissao = 7460;

	private String CargoTipoCodigo;

	public void limpar() {
		CargoTipoCodigo = "";
		super.limpar();
	}

	public String getCargoTipoCodigo() {
		return CargoTipoCodigo;
	}

	public void setCargoTipoCodigo(String cargoTipoCodigo) {
		if (cargoTipoCodigo != null) CargoTipoCodigo = cargoTipoCodigo;
	}

}
