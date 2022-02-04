package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt;

//---------------------------------------------------------
public class PonteiroCejuscStatuNe extends PonteiroCejuscStatuNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -6737088261584411666L;

	//---------------------------------------------------------
	public  String Verificar(PonteiroCejuscStatuDt dados ) {

		String stRetorno="";

		if (dados.getPonteiroCejuscStatus().length()==0)
			stRetorno += "O Campo PonteiroCejuscStatus é obrigatório.";

		return stRetorno;

	}

}
