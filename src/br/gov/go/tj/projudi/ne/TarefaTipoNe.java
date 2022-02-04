package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.TarefaTipoDt;
import br.gov.go.tj.projudi.ps.TarefaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class TarefaTipoNe extends TarefaTipoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -5788360241695210904L;

    //---------------------------------------------------------
	public  String Verificar(TarefaTipoDt dados ) {

		String stRetorno="";

		if (dados.getTarefaTipo().length()==0)
			stRetorno += "O Campo TarefaTipo é obrigatório.";
		//System.out.println("..neTarefaTipoVerificar()");
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaTipoPs obPersistencia = new TarefaTipoPs(obFabricaConexao.getConexao());
				stTemp=obPersistencia.consultarDescricaoJSON( descricao, posicao);
			} finally { 
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}

}
