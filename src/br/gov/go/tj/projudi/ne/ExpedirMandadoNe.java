package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ExpedirMandadoDt;

//---------------------------------------------------------
public class ExpedirMandadoNe extends ExpedirMandadoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 2425452845881511616L;

    //---------------------------------------------------------
	public  String Verificar(ExpedirMandadoDt dados ) {

		String stRetorno="";
		if (dados.getData().equalsIgnoreCase(""))
			stRetorno += "O Campo Data � obrigat�rio.";
		if (dados.getServentia().equalsIgnoreCase(""))
			stRetorno += "O Campo Serventia � obrigat�rio.";
		if (dados.getParte().equalsIgnoreCase(""))
			stRetorno += "O Campo Parte � obrigat�rio.";
		if (dados.getCpfCnpj().equalsIgnoreCase(""))
			stRetorno += "O Campo CpfCnpj � obrigat�rio.";
		if (dados.getLogradouro().equalsIgnoreCase(""))
			stRetorno += "O Campo Logradouro � obrigat�rio.";
		if (dados.getNumero().equalsIgnoreCase(""))
			stRetorno += "O Campo Numero � obrigat�rio.";
		if (dados.getCodCidade().equalsIgnoreCase(""))
			stRetorno += "O Campo CodCidade � obrigat�rio.";
		if (dados.getCidade().equalsIgnoreCase(""))
			stRetorno += "O Campo Cidade � obrigat�rio.";
		if (dados.getMandadoTipo().equalsIgnoreCase(""))
			stRetorno += "O Campo MandadoTipo � obrigat�rio.";
		if (dados.getAssistencia().equalsIgnoreCase(""))
			stRetorno += "O Campo Assistencia � obrigat�rio.";
	
		////System.out.println("..neExpedirMandadoVerificar()");
		return stRetorno;

	}

}
