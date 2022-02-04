package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.HistoricoLotacaoDt;

//---------------------------------------------------------
public class HistoricoLotacaoNe extends HistoricoLotacaoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -7490428902580789524L;

    //---------------------------------------------------------
	public  String Verificar(HistoricoLotacaoDt dados ) {

		String stRetorno="";

		if (dados.getUsuarioServentia().length()==0)
			stRetorno += "O Campo UsuarioServentia � obrigat�rio.";
		if (dados.getDataInicio().length()==0)
			stRetorno += "O Campo DataInicio � obrigat�rio.";
		////System.out.println("..neHistoricoLotacaoVerificar()");
		return stRetorno;

	}

}
