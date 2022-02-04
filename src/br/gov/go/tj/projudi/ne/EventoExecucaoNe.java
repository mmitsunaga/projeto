package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.ps.AreaDistribuicaoPs;
import br.gov.go.tj.projudi.ps.EventoExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class EventoExecucaoNe extends EventoExecucaoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 5943048711508924467L;

    //---------------------------------------------------------
	public  String Verificar(EventoExecucaoDt dados ) {

		String stRetorno="";

		if (dados.getEventoExecucaoCodigo().length()==0)
			stRetorno += "O Campo EventoExecucaoCodigo é obrigatório.";
		if (dados.getEventoExecucao().length()==0)
			stRetorno += "O Campo EventoExecucao é obrigatório.";
		if (dados.getEventoExecucaoTipo().length()==0)
			stRetorno += "O Campo EventoExecucaoTipo é obrigatório.";
		////System.out.println("..neEventoExecucaoVerificar()");
		return stRetorno;

	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			if (tempList != null){
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);	
			} else QuantidadePaginas = 0;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON( descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarIdJSON(String id) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EventoExecucaoPs obPersistencia = new EventoExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarIdJSON(id);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoEventoExecucaoStatusJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		EventoExecucaoStatusNe EventoExecucaoStatusne = new EventoExecucaoStatusNe();
		stTemp = EventoExecucaoStatusne.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
//	/**
//	 * Consulta a descrição dos eventos, exceto o evento Trânsito em Julgado e o evento Guia de Recolhimento Provisória.
//	 * @author wcsilva
//	 */
//	public List consultarDescricaoExcetoTransito(String descricao, String posicao ){
//		List tempList=null;
//			try{
//				FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//				ob obPersistencia = new ob( obFabricaConexao);
//				tempList=obPersistencia.consultarDescricao( descricao, posicao);
//				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
//				tempList.remove(tempList.size()-1);
//			}finally{
//				obFabricaConexao.fecharConexao();
//			}
//		return tempList;   
//	}
}
