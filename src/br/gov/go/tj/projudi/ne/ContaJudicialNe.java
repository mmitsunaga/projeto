package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ContaJudicialDt;

//---------------------------------------------------------
public class ContaJudicialNe extends ContaJudicialNeGen{

//

//---------------------------------------------------------
	public  String Verificar(ContaJudicialDt dados ) {

		String stRetorno="";

		if (dados.getProcNumero().length()==0)
			stRetorno += "O Campo ProcNumero é obrigatório.";
		if (dados.getBanco().length()==0)
			stRetorno += "O Campo Banco é obrigatório.";
		if (dados.getContaJudicialNumero().length()==0)
			stRetorno += "O Campo ContaJudicialNumero é obrigatório.";
		if (dados.getServ().length()==0)
			stRetorno += "O Campo Serv é obrigatório.";

		return stRetorno;

	}

}
