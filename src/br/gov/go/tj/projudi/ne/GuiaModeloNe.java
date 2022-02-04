package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.ps.GuiaModeloPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class GuiaModeloNe extends GuiaModeloNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -987983634353937809L;

    //---------------------------------------------------------
	public  String Verificar(GuiaModeloDt dados ) {

		String stRetorno="";

		if (dados.getGuiaModelo().length()==0)
			stRetorno += "O Campo GuiaModelo é obrigatório.";
		if (dados.getGuiaModeloCodigo().length()==0)
			stRetorno += "O Campo GuiaModeloCodigo é obrigatório.";
		if (dados.getGuiaTipo().length()==0)
			stRetorno += "O Campo GuiaTipo é obrigatório.";
		return stRetorno;

	}

	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idProcessoTipo.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloProcessoTipo(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		GuiaModeloDt dtRetorno = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaModeloPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaModeloPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaModeloPs(obFabricaConexao.getConexao());
			}
			
			dtRetorno = obPersistencia.consultarGuiaModelo(idGuiaTipo, idProcessoTipo); 
			obDados.copiar(dtRetorno);
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return dtRetorno;
	}
	
	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idProcessoTipo para novo Regimento de custa. (PROAD: 201703000030747).
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloProcessoTipoNovoRegimento(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		GuiaModeloDt dtRetorno = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaModeloPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaModeloPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaModeloPs(obFabricaConexao.getConexao());
			}
			
			dtRetorno = obPersistencia.consultarGuiaModeloNovoRegimento(idGuiaTipo, idProcessoTipo); 
			obDados.copiar(dtRetorno);
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return dtRetorno;
	}
	
	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idNaturezaSPG.
	 * @param String idGuiaTipo
	 * @param String idNaturezaSPG
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloNaturezaSPG(FabricaConexao obFabricaConexao, String idGuiaTipo, String idNaturezaSPG) throws Exception {
		GuiaModeloDt dtRetorno = null;
		FabricaConexao conexao = null;
		
		try{
			GuiaModeloPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaModeloPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaModeloPs(obFabricaConexao.getConexao());
			}
			
			dtRetorno = obPersistencia.consultarGuiaModeloNaturezaSPG(idGuiaTipo, idNaturezaSPG); 
			obDados.copiar(dtRetorno);
		
		}
		finally{
			if( obFabricaConexao == null ) {
				conexao.fecharConexao();
			}
		}
		
		return dtRetorno;
	}
	
	/**
	 * Método para consultar guiaModeloDt em transação.
	 * @param String idGuiaModelo
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarIdGuiaModelo(String idGuiaModelo, FabricaConexao obFabricaConexao) throws Exception {
		GuiaModeloPs obPersistencia = new GuiaModeloPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarId(idGuiaModelo);
	}
	
	public GuiaModeloDt consultarIdGuiaModelo(String idGuiaModelo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaModeloPs obPersistencia = new GuiaModeloPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarId(idGuiaModelo);
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	/**
	 * Método para consultar lista de GuiaModelo pelo idGuiaTipo.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaModelo(String idGuiaTipo) throws Exception {
		List listaGuiaModelo = null;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaModeloPs obPersistencia = new GuiaModeloPs(obFabricaConexao.getConexao());
			listaGuiaModelo = obPersistencia.consultarGuiaModelo(idGuiaTipo); 
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaModelo;
	}
	
	public String consultarDescricaoJSON(String descricao, String guiaTipo, String processoTipo, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaModeloPs obPersistencia = new  GuiaModeloPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, guiaTipo, processoTipo, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoGuiaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		GuiaTipoNe GuiaTipone = new GuiaTipoNe(); 
		stTemp = GuiaTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoProcessoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ProcessoTipoNe ProcessoTipone = new ProcessoTipoNe(); 
		stTemp = ProcessoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
}
