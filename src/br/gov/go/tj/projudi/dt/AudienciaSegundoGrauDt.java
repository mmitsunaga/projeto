package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaSegundoGrauDt extends AudienciaDt {

	/**
     * 
     */
    private static final long serialVersionUID = -2881595660542377064L;

    public static final int CodigoPermissao = 401;

	private String data;

	private String hora;

	public void limpar() {
		super.limpar();
		data = "";
		hora = "";
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		if (data != null) this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		if (hora != null) this.hora = hora;
	}	
}