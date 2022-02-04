package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.ps.GuiaCustaModeloPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class GuiaCustaModeloNe extends GuiaCustaModeloNeGen{

//
/**
     * 
     */
    private static final long serialVersionUID = -2482759845226948766L;

    //---------------------------------------------------------
	public  String Verificar(GuiaCustaModeloDt dados ) {

		String stRetorno="";

		if (dados.getId_GuiaModelo().length()==0)
			stRetorno += "O Campo Id_GuiaModelo é obrigatório.";
		return stRetorno;

	}
	
	public String consultarCustaGuiaModeloUlLiCheckBox(String id_GuiaModelo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi="<ul>";

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GuiaCustaModeloPs obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarCustaGuiaModeloGeral( id_GuiaModelo);
				for(int i = 0; i < lisGeral.size(); i++) {
					GuiaCustaModeloDt obDtTemp = (GuiaCustaModeloDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}
	
	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuiaProcessoTipo(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		List listaGuiaCustaModelo = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaCustaModeloPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaCustaModeloPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			}
			
			listaGuiaCustaModelo = obPersistencia.consultarItensGuiaProcessoTipo(idGuiaTipo, idProcessoTipo);
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return listaGuiaCustaModelo;
	}
	
	/**
	 * Método criado para o novo regimento.
	 * 
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuiaProcessoTipoNovoRegimento(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		List listaGuiaCustaModelo = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaCustaModeloPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaCustaModeloPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			}
			
			if( idGuiaTipo != null && idProcessoTipo != null ) {
				listaGuiaCustaModelo = obPersistencia.consultarItensGuiaProcessoTipoNovoRegimento(idGuiaTipo, idProcessoTipo);
			}
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return listaGuiaCustaModelo;
	}
	
	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia pela natureza SPG.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @param String idNaturezaSPG
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuiaNaturezaSPG(FabricaConexao obFabricaConexao, String idGuiaTipo, String idNaturezaSPG) throws Exception {
		List listaGuiaCustaModelo = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaCustaModeloPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaCustaModeloPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaCustaModeloPs(obFabricaConexao.getConexao());
			}
			
			listaGuiaCustaModelo = obPersistencia.consultarItensGuiaNaturezaSPG(idGuiaTipo, idNaturezaSPG);
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return listaGuiaCustaModelo;
	}
}
