package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class GuiaModeloDt extends GuiaModeloDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -9061004354659418913L;
    public static final int CodigoPermissao=539;

    private String FlagGrau;
    private String nomeModeloAlternativo;

	public String getNomeModeloAlternativo() {
		return nomeModeloAlternativo;
	}

	public void setNomeModeloAlternativo(String nomeModeloAlternativo) {
		this.nomeModeloAlternativo = nomeModeloAlternativo;
	}

	public String getFlagGrau() {
		return FlagGrau;
	}

	public void setFlagGrau(String flagGrau) {
		FlagGrau = flagGrau;
	}

	public boolean isIdGuiaTipo(String idGuiaTipo) {
		if (getId_GuiaTipo()!=null && getId_GuiaTipo().equals(idGuiaTipo))
			return true;
		return false;
	}
}
