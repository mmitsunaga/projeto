package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ContaJudicialMovimentacaoDt;

//---------------------------------------------------------
public class ContaJudicialMovimentacaoNe extends ContaJudicialMovimentacaoNeGen{

//

//---------------------------------------------------------
	public  String Verificar(ContaJudicialMovimentacaoDt dados ) {

		String stRetorno="";

		if (dados.getNumeroParcela().length()==0)
			stRetorno += "O Campo NumeroParcela � obrigat�rio.";
		if (dados.getId_Deposito().length()==0)
			stRetorno += "O Campo Id_Deposito � obrigat�rio.";

		return stRetorno;

	}

}
