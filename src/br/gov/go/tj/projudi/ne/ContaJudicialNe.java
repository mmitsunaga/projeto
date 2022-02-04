package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ContaJudicialDt;

//---------------------------------------------------------
public class ContaJudicialNe extends ContaJudicialNeGen{

//

//---------------------------------------------------------
	public  String Verificar(ContaJudicialDt dados ) {

		String stRetorno="";

		if (dados.getProcNumero().length()==0)
			stRetorno += "O Campo ProcNumero � obrigat�rio.";
		if (dados.getBanco().length()==0)
			stRetorno += "O Campo Banco � obrigat�rio.";
		if (dados.getContaJudicialNumero().length()==0)
			stRetorno += "O Campo ContaJudicialNumero � obrigat�rio.";
		if (dados.getServ().length()==0)
			stRetorno += "O Campo Serv � obrigat�rio.";

		return stRetorno;

	}

}
