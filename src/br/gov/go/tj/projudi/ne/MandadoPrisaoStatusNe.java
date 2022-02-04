package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;

//---------------------------------------------------------
public class MandadoPrisaoStatusNe extends MandadoPrisaoStatusNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 4425727020406850088L;

	//---------------------------------------------------------
	public  String Verificar(MandadoPrisaoStatusDt dados ) {

		String stRetorno="";

		if (dados.getMandadoPrisaoStatusCodigo().length()==0)
			stRetorno += "O Campo MandadoPrisaoStatusCodigo é obrigatório.";
		if (dados.getMandadoPrisaoStatus().length()==0)
			stRetorno += "O Campo MandadoPrisaoStatus é obrigatório.";

		return stRetorno;

	}

}
