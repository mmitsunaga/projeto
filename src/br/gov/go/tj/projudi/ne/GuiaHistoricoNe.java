package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.GuiaHistoricoDt;

//---------------------------------------------------------
public class GuiaHistoricoNe extends GuiaHistoricoNeGen{

//

//---------------------------------------------------------
	public  String Verificar(GuiaHistoricoDt dados ) {

		String stRetorno="";

		if (dados.getNumeroGuiaCompleto().length()==0)
			stRetorno += "O Campo NumeroGuiaCompleto é obrigatório.";

		return stRetorno;

	}

}
