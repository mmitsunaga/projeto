package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class UsuarioFoneDt extends UsuarioFoneDtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3622482517589135795L;
	public static final int CodigoPermissao=291;
//
	public boolean isLiberado() {
		if (DataLiberacao!=null && !DataLiberacao.isEmpty()) {
			return true;
		}
		return false;
	}
	

}
