package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.ps.PenaExecucaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class PenaExecucaoTipoNe extends PenaExecucaoTipoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 2639295935336512268L;

    //---------------------------------------------------------
	public  String Verificar(PenaExecucaoTipoDt dados ) {

		String stRetorno="";

		if (dados.getPenaExecucaoTipo().length()==0)
			stRetorno += "O Campo PenaExecucaoTipo é obrigatório.";
		return stRetorno;

	}

	public List consultarIds(String id_opcoes) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarIds(id_opcoes);
			QuantidadePaginas = 0;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public String consultarIdsJSON(String id_opcoes) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarIdsJSON(id_opcoes);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PenaExecucaoTipoPs obPersistencia = new PenaExecucaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
