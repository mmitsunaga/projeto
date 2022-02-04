package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.ps.ArrecadacaoCustaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ArrecadacaoCustaNe extends ArrecadacaoCustaNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -4527791734646371194L;
    
    //---------------------------------------------------------
	public  String Verificar(ArrecadacaoCustaDt dados ) {

		String stRetorno="";

		if (dados.getArrecadacaoCusta().length()==0)
			stRetorno += "O Campo ArrecadacaoCusta é obrigatório.";
		return stRetorno;

	}
	
	/**
	 * Consulta por Descrição.
	 * @param descricao
	 * @param posicao
	 * @return List
	 * @throws Exception
	 */
	public List consultarPorDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArrecadacaoCustaPs obPersistencia = new ArrecadacaoCustaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarPorDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	/**
	 * Método para consultar a lista de Arrecadacao Custa.
	 * @param String descricao
	 * @return List ArrecadacaoCustaDt
	 * @throws Exception
	 */
	public List consultarPorDescricao(String descricao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArrecadacaoCustaPs obPersistencia = new ArrecadacaoCustaPs(obFabricaConexao.getConexao());
			
			tempList = obPersistencia.consultarPorDescricao(descricao);
		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return tempList;
	}
	
	public String consultarPorDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArrecadacaoCustaPs obPersistencia = new ArrecadacaoCustaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarPorDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public ArrecadacaoCustaDt consultarId(String id_arrecadacaocusta, FabricaConexao obFabricaConexao) throws Exception {
		ArrecadacaoCustaDt dtRetorno=null;		
		ArrecadacaoCustaPs obPersistencia = new ArrecadacaoCustaPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_arrecadacaocusta ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
}
