package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.PrisaoTipoDt;

//---------------------------------------------------------
public class PrisaoTipoNe extends PrisaoTipoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -9095365867024907544L;

	//---------------------------------------------------------
	public  String Verificar(PrisaoTipoDt dados ) {

		String stRetorno="";

		if (dados.getPrisaoTipoCodigo().length()==0)
			stRetorno += "O Campo PrisaoTipoCodigo é obrigatório.";
		if (dados.getPrisaoTipo().length()==0)
			stRetorno += "O Campo PrisaoTipo é obrigatório.";

		return stRetorno;

	}

}
