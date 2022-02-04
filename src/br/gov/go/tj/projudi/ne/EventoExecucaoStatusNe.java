package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.ps.EventoExecucaoStatusPs;
import br.gov.go.tj.projudi.ps.EventoExecucaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class EventoExecucaoStatusNe extends EventoExecucaoStatusNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 2290428649881818138L;

    //---------------------------------------------------------
	public  String Verificar(EventoExecucaoStatusDt dados ) {

		String stRetorno="";

		if (dados.getEventoExecucaoStatus().length()==0)
			stRetorno += "O Campo EventoExecucaoStatus é obrigatório.";
		if (dados.getEventoExecucaoStatusCodigo().length()==0)
			stRetorno += "O Campo EventoExecucaoStatusCodigo é obrigatório.";
		////System.out.println("..neEventoExecucaoStatusVerificar()");
		return stRetorno;

	}

	public List consultarDescricao(String descricao, String posicao, boolean isMostrarNaoAplica) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoStatusPs obPersistencia = new EventoExecucaoStatusPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao(descricao, posicao, isMostrarNaoAplica);
			if (posicao.length() > 0){
//				if (tempList != null){
//					QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
//					tempList.remove(tempList.size()-1);					
//				} else 
					QuantidadePaginas = 0;	
			}
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoStatusPs obPersistencia = new EventoExecucaoStatusPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao(descricao, posicao, true);
			if (posicao.length() > 0){
				QuantidadePaginas = 0;	
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, boolean isMostrarNaoAplica ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoStatusPs obPersistencia = new EventoExecucaoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, isMostrarNaoAplica);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoStatusPs obPersistencia = new EventoExecucaoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, true);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
