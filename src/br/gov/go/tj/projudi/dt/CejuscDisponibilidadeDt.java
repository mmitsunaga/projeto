package br.gov.go.tj.projudi.dt;

public class CejuscDisponibilidadeDt extends CejuscDisponibilidadeDtGen{

	private static final long serialVersionUID = -737956993556232802L;
	public static final int CodigoPermissao=850;
	
	public boolean isDomingoNenhum() {
		return !(this.isDomingoMatutino() || this.isDomingoVespertino() || this.isDomingoMatutinoEVespertino());
	}
	
	public boolean isDomingoMatutino() {
		if (super.getDomingo() == null) return false;
		return super.getDomingo().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isDomingoVespertino() {
		if (super.getDomingo() == null) return false;
		return super.getDomingo().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isDomingoMatutinoEVespertino() {
		if (super.getDomingo() == null) return false;
		return super.getDomingo().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
	
	public boolean isSegundaNenhum() {
		return !(this.isSegundaMatutino() || this.isSegundaVespertino() || this.isSegundaMatutinoEVespertino());
	}
	
	public boolean isSegundaMatutino() {
		if (super.getSegunda() == null) return false;
		return super.getSegunda().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isSegundaVespertino() {
		if (super.getSegunda() == null) return false;
		return super.getSegunda().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isSegundaMatutinoEVespertino() {
		if (super.getSegunda() == null) return false;
		return super.getSegunda().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
	
	public boolean isTercaNenhum() {
		return !(this.isTercaMatutino() || this.isTercaVespertino() || this.isTercaMatutinoEVespertino());
	}
	
	public boolean isTercaMatutino() {
		if (super.getTerca() == null) return false;
		return super.getTerca().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isTercaVespertino() {
		if (super.getTerca() == null) return false;
		return super.getTerca().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isTercaMatutinoEVespertino() {
		if (super.getTerca() == null) return false;
		return super.getTerca().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
	
	public boolean isQuartaNenhum() {
		return !(this.isQuartaMatutino() || this.isQuartaVespertino() || this.isQuartaMatutinoEVespertino());
	}
	
	public boolean isQuartaMatutino() {
		if (super.getQuarta() == null) return false;
		return super.getQuarta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isQuartaVespertino() {
		if (super.getQuarta() == null) return false;
		return super.getQuarta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isQuartaMatutinoEVespertino() {
		if (super.getQuarta() == null) return false;
		return super.getQuarta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
	
	public boolean isQuintaNenhum() {
		return !(this.isQuintaMatutino() || this.isQuintaVespertino() || this.isQuintaMatutinoEVespertino());
	}
	
	public boolean isQuintaMatutino() {
		if (super.getQuinta() == null) return false;
		return super.getQuinta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isQuintaVespertino() {
		if (super.getQuinta() == null) return false;
		return super.getQuinta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isQuintaMatutinoEVespertino() {
		if (super.getQuinta() == null) return false;
		return super.getQuinta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
	
	public boolean isSextaNenhum() {
		return !(this.isSextaMatutino() || this.isSextaVespertino() || this.isSextaMatutinoEVespertino());
	}
	
	public boolean isSextaMatutino() {
		if (super.getSexta() == null) return false;
		return super.getSexta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isSextaVespertino() {
		if (super.getSexta() == null) return false;
		return super.getSexta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isSextaMatutinoEVespertino() {
		if (super.getSexta() == null) return false;
		return super.getSexta().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
	
	public boolean isSabadoNenhum() {
		return !(this.isSabadoMatutino() || this.isSabadoVespertino() || this.isSabadoMatutinoEVespertino());
	}
	
	public boolean isSabadoMatutino() {
		if (super.getSabado() == null) return false;
		return super.getSabado().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_MATUTINO));
	}
	
	public boolean isSabadoVespertino() {
		if (super.getSabado() == null) return false;
		return super.getSabado().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.PERIODO_VESPERTINO));
	}
	
	public boolean isSabadoMatutinoEVespertino() {
		if (super.getSabado() == null) return false;
		return super.getSabado().trim().equalsIgnoreCase(String.valueOf(PonteiroCejuscDt.AMBOS_PERIODOS));
	}
}
