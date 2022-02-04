package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.ps.RegimeExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class RegimeExecucaoNe extends RegimeExecucaoNeGen{

    private static final long serialVersionUID = -260296876104487363L;

    //---------------------------------------------------------
	public  String Verificar(RegimeExecucaoDt dados ) {

		String stRetorno="";

		if (dados.getRegimeExecucao().length()==0)
			stRetorno += "O Campo RegimeExecucao é obrigatório.";
		if (dados.getPenaExecucaoTipo().length()==0)
			stRetorno += "O Campo PenaExecucaoTipo é obrigatório.";
		////System.out.println("..neRegimeExecucaoVerificar()");
		return stRetorno;

	}

	public List consultarDescricao(String descricao, String id_PenaExecucaoTipo, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				RegimeExecucaoPs obPersistencia = new RegimeExecucaoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao(descricao, id_PenaExecucaoTipo, posicao);
				if (posicao.length() > 0){
					QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
					tempList.remove(tempList.size()-1);	
				}
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}
	
	/**
	 * Consulta o regime a partir do id
	 * @param idRegimeExecucao, identificação do regimeExecução
	 * @return RegimeExecucaoDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public RegimeExecucaoDt consultarRegime(String idRegimeExecucao) throws Exception {
		RegimeExecucaoDt dado = null;
		FabricaConexao obFabricaConexao =null;
		////System.out.println("..ne-ConsultaRegime" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegimeExecucaoPs obPersistencia = new RegimeExecucaoPs(obFabricaConexao.getConexao());
			dado = obPersistencia.consultarRegime(idRegimeExecucao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dado;   
	}
	
	public String consultarDescricaoPenaExecucaoTipoJSON(String descricao, String posicao ) throws Exception {
        String stTemp ="";               
                        
        stTemp = new PenaExecucaoTipoNe().consultarDescricaoJSON(descricao, posicao);                       

        return stTemp;
    }
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RegimeExecucaoPs obPersistencia = new  RegimeExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
