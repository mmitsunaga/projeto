package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.ps.GuiaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class GuiaTipoNe extends GuiaTipoNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 4663990918162899731L;

    //---------------------------------------------------------
	public  String Verificar(GuiaTipoDt dados ) {

		String stRetorno="";

		if (dados.getGuiaTipo().length()==0)
			stRetorno += "O Campo GuiaTipo é obrigatório.";
		if (dados.getGuiaTipoCodigo().length()==0)
			stRetorno += "O Campo GuiaTipoCodigo é obrigatório.";
		return stRetorno;

	}
	
	/**
	 * Método para retornar uma lista de id de guia tipo.
	 * @return
	 * @throws Exception
	 */
	public List consultarListaId_GuiaTipo(String itemExcluir) throws Exception {
		List listaId_GuiaTipo = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaTipoPs obPersistencia = new GuiaTipoPs(obFabricaConexao.getConexao());
			
			listaId_GuiaTipo = obPersistencia.consultarListaId_GuiaTipo(itemExcluir);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaId_GuiaTipo;
	}
	
	/**
	 * Método para consultar somente a Descrição pelo Id.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricao(FabricaConexao obFabricaConexao, String idGuiaTipo) throws Exception {
		String guiaTipo = null;
		
		FabricaConexao conexao = null;
		try {
			GuiaTipoPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaTipoPs(conexao.getConexao());
			}
			else {
				obPersistencia = new  GuiaTipoPs(obFabricaConexao.getConexao());
			}
			
			guiaTipo = obPersistencia.consultarDescricao(idGuiaTipo);
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return guiaTipo;
	}
	
	/**
	 * Método para consultar a lista de tipos de guias.
	 * @return
	 * @throws Exception
	 */
	public List consultarDescricao() throws Exception {
		List listaGuiaTipoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaTipoPs obPersistencia = new GuiaTipoPs(obFabricaConexao.getConexao());
			
			listaGuiaTipoDt = obPersistencia.consultarDescricao();
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaTipoDt;
	}	
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaTipoPs obPersistencia = new GuiaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON( descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarIdCodigo(String guiaTipoCodigo) throws Exception{
		
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaTipoPs obPersistencia = new GuiaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarIdCodigo(guiaTipoCodigo);		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
