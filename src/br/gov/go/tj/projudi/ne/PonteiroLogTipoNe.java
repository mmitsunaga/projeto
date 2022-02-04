package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;

//---------------------------------------------------------
public class PonteiroLogTipoNe extends PonteiroLogTipoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 5736497269140947561L;

	//---------------------------------------------------------
	public  String Verificar(PonteiroLogTipoDt dados ) {

		String stRetorno="";

		if (dados.getPonteiroLogTipo().length()==0)
			stRetorno += "O Campo PonteiroLogTipo é obrigatório.";

		return stRetorno;

	}

}
