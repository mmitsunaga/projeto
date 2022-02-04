package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.projudi.ps.EventoExecucaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class EventoExecucaoTipoNe extends EventoExecucaoTipoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -428765378228876658L;

    //---------------------------------------------------------
	public  String Verificar(EventoExecucaoTipoDt dados ) {

		String stRetorno="";

		if (dados.getEventoExecucaoTipoCodigo().length()==0)
			stRetorno += "O Campo EventoExecucaoTipoCodigo é obrigatório.";
		if (dados.getEventoExecucaoTipo().length()==0)
			stRetorno += "O Campo EventoExecucaoTipo é obrigatório.";
		////System.out.println("..neEventoExecucaoTipoVerificar()");
		return stRetorno;

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoTipoPs obPersistencia = new EventoExecucaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
