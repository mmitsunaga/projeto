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
			stRetorno += "O Campo Data é obrigatório.";
		if (dados.getServentia().equalsIgnoreCase(""))
			stRetorno += "O Campo Serventia é obrigatório.";
		if (dados.getParte().equalsIgnoreCase(""))
			stRetorno += "O Campo Parte é obrigatório.";
		if (dados.getCpfCnpj().equalsIgnoreCase(""))
			stRetorno += "O Campo CpfCnpj é obrigatório.";
		if (dados.getLogradouro().equalsIgnoreCase(""))
			stRetorno += "O Campo Logradouro é obrigatório.";
		if (dados.getNumero().equalsIgnoreCase(""))
			stRetorno += "O Campo Numero é obrigatório.";
		if (dados.getCodCidade().equalsIgnoreCase(""))
			stRetorno += "O Campo CodCidade é obrigatório.";
		if (dados.getCidade().equalsIgnoreCase(""))
			stRetorno += "O Campo Cidade é obrigatório.";
		if (dados.getMandadoTipo().equalsIgnoreCase(""))
			stRetorno += "O Campo MandadoTipo é obrigatório.";
		if (dados.getAssistencia().equalsIgnoreCase(""))
			stRetorno += "O Campo Assistencia é obrigatório.";
	
		////System.out.println("..neExpedirMandadoVerificar()");
		return stRetorno;

	}

}
