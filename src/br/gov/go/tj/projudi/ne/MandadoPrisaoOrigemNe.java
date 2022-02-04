package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;

//---------------------------------------------------------
public class MandadoPrisaoOrigemNe extends MandadoPrisaoOrigemNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 5321296147612173802L;

	//---------------------------------------------------------
	public  String Verificar(MandadoPrisaoOrigemDt dados ) {

		String stRetorno="";

		if (dados.getMandadoPrisaoOrigemCodigo().length()==0)
			stRetorno += "O Campo MandadoPrisaoOrigemCodigo � obrigat�rio.";
		if (dados.getMandadoPrisaoOrigem().length()==0)
			stRetorno += "O Campo MandadoPrisaoOrigem � obrigat�rio.";

		return stRetorno;

	}

}
