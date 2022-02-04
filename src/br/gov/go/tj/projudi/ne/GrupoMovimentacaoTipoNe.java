package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoMovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GrupoMovimentacaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class GrupoMovimentacaoTipoNe extends GrupoMovimentacaoTipoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -9171439621423963574L;

    public String Verificar(GrupoMovimentacaoTipoDt dados) {

		String stRetorno = "";

		if (dados.getId_Grupo().equalsIgnoreCase("")) stRetorno += "É necessário selecionar um Grupo.";
//		if (dados.getId_MovimentacaoTipo().equalsIgnoreCase("")) stRetorno += "É necessário selecionar um Tipo de Movimentação.";

		return stRetorno;

	}

	/**
	 * Consultar tipos de movimentação definidos para um determinado grupo
	 */
	public List consultarGrupoMovimentacaoTipo(String grupoCodigo, String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarGrupoMovimentacaoTipo(grupoCodigo, descricao, posicao);
			//stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;

	}
	
	public String consultarDescricaoGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		GrupoNe Grupone = new GrupoNe(); 
		stTemp = Grupone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarGrupoMovimentacaoTipoJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarGrupoMovimentacaoTipoJSON(grupoCodigo, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;

	}
	
	public String consultarGrupoMovimentacaoTipoSentencaJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarGrupoMovimentacaoTipoSentencaJSON(grupoCodigo, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;

	}
	

	public List consultarMovimentacaoTipoGrupo(String id_grupo) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarMovimentacaoTipoGrupoGeral( id_grupo);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return lisGeral;
	}
	
	public void incluirMultiplo(GrupoMovimentacaoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				GrupoMovimentacaoTipoDt obDt = (GrupoMovimentacaoTipoDt)lisGeral.get(j);
				if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GrupoMovimentacaoTipoDt obDt = (GrupoMovimentacaoTipoDt)lisIncluir.get(i);
				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	public void excluirMultiplo(GrupoMovimentacaoTipoDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoMovimentacaoTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GrupoMovimentacaoTipoDt obDt = (GrupoMovimentacaoTipoDt)lisGeral.get(i);
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_MovimentacaoTipo().equalsIgnoreCase(id)){
				lisExcluir.add(obDt);
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoMovimentacaoTipoPs obPersistencia = new GrupoMovimentacaoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisExcluir.size(); i++) {
				GrupoMovimentacaoTipoDt obDtTemp = (GrupoMovimentacaoTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GrupoMovimentacaoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

}
