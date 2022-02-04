package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ServentiaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ServentiaTipoNe extends ServentiaTipoNeGen {

//

/**
 * 
 */
    private static final long serialVersionUID = -3870966284535911331L;

    // ---------------------------------------------------------
	public String Verificar(ServentiaTipoDt dados) {

		String stRetorno = "";
		if (dados.getServentiaTipo().equalsIgnoreCase("")) {
			stRetorno += "Descrição é é obrigatório.";
		}
		return stRetorno;

	}

	public String consultarCodigoServentiaTipo(String id_serventiatipo) throws Exception {

		String serventiaTipoCodigo = null;
		FabricaConexao obFabricaConexao = null;
		// //System.out.println("..ne-consultarServentiaTipoCodigo");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			serventiaTipoCodigo = obPersistencia.consultarCodigoServentiaTipo(id_serventiatipo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return serventiaTipoCodigo;
	}
	
	public ServentiaTipoDt consultarServentiaTipoCodigo(int codigo_serventiatipo) throws Exception {
		ServentiaTipoDt serventiaTipoDt = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			serventiaTipoDt = obPersistencia.consultarServentiaTipoCodigo(codigo_serventiatipo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return serventiaTipoDt;
	}

	/**
	 * Consulta tipos de serventia para solucionar pendencia
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            descricao, descricao do tipo serventia
	 * @return List
	 * @throws Exception
	 */

	public List consultarServentiaTipo(String descricao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiaTipo(descricao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta tipos de serventia para solucionar pendencia
	 * 
	 * @author asrocha
	 * @param String
	 *            id_ServentiaTipo, Id do tipo de serventia
	 * @return List
	 * @throws Exception
	 */

	public List consultarServentiaTipoId(String id_ServentiaTipo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiaTipoId(id_ServentiaTipo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public List consultarServentiaTipo(String descricao, UsuarioDt usuarioDt) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiaTipo(descricao, usuarioDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDescricaoJSON(String descricao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON( descricao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp; 

	}
	
	public String consultarDescricaoJSONPJD(String descricao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaTipoPs obPersistencia = new ServentiaTipoPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSONPJD(descricao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp; 

	}
}
