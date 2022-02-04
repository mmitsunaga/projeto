package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.ps.GuiaStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

public class GuiaStatusNe extends Negocio {

	private static final long serialVersionUID = -4084421117600910067L;
	
	public static final Integer ATIVO 	= 1;
	public static final Integer INATIVO = 0;

	/**
	 * Método para consultar a lista de status de guias.
	 * @return List<GuiaTipoDt>
	 * @throws Exception
	 */
	public List consultarDescricao() throws Exception {
		List listaGuiaStatusDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaStatusPs obPersistencia = new  GuiaStatusPs(obFabricaConexao.getConexao());
			
			listaGuiaStatusDt = obPersistencia.consultarDescricao();
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaStatusDt;
	}
}