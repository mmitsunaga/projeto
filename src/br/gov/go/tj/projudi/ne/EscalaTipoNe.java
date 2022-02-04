package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EscalaTipoDt;

//---------------------------------------------------------
public class EscalaTipoNe extends EscalaTipoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 7084234438516325687L;

	//---------------------------------------------------------
	public  String Verificar(EscalaTipoDt dados ) {

		String stRetorno="";

		if (dados.getEscalaTipoCodigo().length()==0)
			stRetorno += "O Campo EscalaTipoCodigo é obrigatório.";

		return stRetorno;

	}

}
