package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ObjetoTipoDt;

//---------------------------------------------------------
public class ObjetoTipoNe extends ObjetoTipoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 1915165727989896946L;

	//---------------------------------------------------------
	public  String Verificar(ObjetoTipoDt dados ) {

		String stRetorno="";

		if (dados.getObjetoTipo().length()==0)
			stRetorno += "O Campo ObjetoTipo é obrigatório.";

		return stRetorno;

	}

}
