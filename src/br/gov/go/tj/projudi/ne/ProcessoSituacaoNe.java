package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.ProcessoSituacaoDt;

//---------------------------------------------------------
public class ProcessoSituacaoNe extends ProcessoSituacaoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -9065076541527752864L;

    //---------------------------------------------------------
	public  String Verificar(ProcessoSituacaoDt dados ) {

		String stRetorno="";

		if (dados.getProcessoSituacao().equalsIgnoreCase(""))
			stRetorno += "O Campo ProcessoSituacao � obrigat�rio.";
		if (dados.getProcessoSituacaoCodigo().equalsIgnoreCase(""))
			stRetorno += "O Campo ProcessoSituacaoCodigo � obrigat�rio.";
		////System.out.println("..neProcessoSituacaoVerificar()");
		return stRetorno;

	}

}
