package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ps.ServentiaSubtipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaSubtipoNe extends ServentiaSubtipoNeGen {

	private static final long serialVersionUID = -6748202308261478227L;

	public String Verificar(ServentiaSubtipoDt dados) {

		String stRetorno = "";

		if (dados.getServentiaSubtipo().equalsIgnoreCase(""))
			stRetorno += "O Campo ServentiaSubtipo é obrigatório.";
		if (dados.getServentiaSubtipoCodigo().equalsIgnoreCase(""))
			stRetorno += "O Campo ServentiaSubtipoCodigo é obrigatório.";
		return stRetorno;

	}

	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoPs obPersistencia = new ServentiaSubtipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_AreaDistribuicao
	 *            , id da Area Distribuicao
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoAreaDistribuicao(String id_AreaDistribuicao ) throws Exception {

		String id_ServentiaSubTipo = null;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoPs obPersistencia = new ServentiaSubtipoPs(obFabricaConexao.getConexao());
			id_ServentiaSubTipo = obPersistencia.consultarServentiaSubTipoAreaDistribuicao(id_AreaDistribuicao ); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return id_ServentiaSubTipo;
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_Serventia
	 *            , id da Serventia
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoServentia(String id_Serventia ) throws Exception {

		String id_ServentiaSubTipo = null;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoPs obPersistencia = new ServentiaSubtipoPs(obFabricaConexao.getConexao());
			id_ServentiaSubTipo = obPersistencia.consultarServentiaSubTipoServentia(id_Serventia ); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return id_ServentiaSubTipo;
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo da serventia origem de um recurso
	 * 
	 * @param id_Reurso
	 *            , id do recurso
	  * @return String
	 *            , codigo do subtipo da serventia 
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoServentiaOrigemRecurso(String id_Recurso) throws Exception {

		String serventiaSubTipoCodigo = null;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoPs obPersistencia = new ServentiaSubtipoPs(obFabricaConexao.getConexao());
			serventiaSubTipoCodigo = obPersistencia.consultarServentiaSubTipoServentiaOrigemRecurso(id_Recurso); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return serventiaSubTipoCodigo;
	}

}
