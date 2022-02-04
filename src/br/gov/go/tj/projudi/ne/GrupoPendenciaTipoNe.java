package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoPendenciaTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.GrupoPendenciaTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoPendenciaTipoNe extends GrupoPendenciaTipoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5075276878682289683L;

    public String Verificar(GrupoPendenciaTipoDt dados) {

		String stRetorno = "";

		if (dados.getGrupo().length() == 0) stRetorno += "O Campo Grupo é obrigatório.";
		return stRetorno;

	}

	/**
	 * Chama método para retornar tipos de pendência definidos para um grupo com limite na consulta
	 */
	public List consultarGrupoPendenciaTipo(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		return this.consultarGrupoPendenciaTipo(descricao, usuarioDt, posicao, true);
	}

	/**
	 * Consultar os tipos de pendência definidos para um determinado grupo de usuários
	 * 
	 * @param descricao, filtro para grupo
	 * @param grupoCodigo, código do grupo para filtrar as pendências que esse pode utilizar
	 * @param posicao, parametro para paginação
	 * @param limite, booleano que define se consulta deve ser limitada
	 * 
	 * 
	 * @author msapaula
	 */
	public List consultarGrupoPendenciaTipo(String descricao, UsuarioDt usuarioDt, String posicao, boolean limite) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());

			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_VARA || Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU) {
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
				tempList = obPersistencia.consultarGrupoPendenciaTipo(descricao, usuarioDt.getGrupoUsuarioChefe(), posicao, limite);
			} else tempList = obPersistencia.consultarGrupoPendenciaTipo(descricao, usuarioDt.getGrupoCodigo(), posicao, limite);
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
	
	public void excluirMultiplo(GrupoPendenciaTipoDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoPendenciaTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GrupoPendenciaTipoDt obDt = (GrupoPendenciaTipoDt)lisGeral.get(i);
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_PendenciaTipo().equalsIgnoreCase(id)){
				lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisExcluir.size(); i++) {
				GrupoPendenciaTipoDt obDtTemp = (GrupoPendenciaTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GrupoPendenciaTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();			 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void incluirMultiplo(GrupoPendenciaTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoPendenciaTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				GrupoPendenciaTipoDt obDt = (GrupoPendenciaTipoDt)lisGeral.get(j);
				if (obDt.getId_PendenciaTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GrupoPendenciaTipoDt obDt = (GrupoPendenciaTipoDt)lisIncluir.get(i);
				
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("GrupoPendenciaTipo", obDt.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();			 
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarPendenciaTipoGrupo(String id_grupo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				GrupoPendenciaTipoPs obPersistencia = new GrupoPendenciaTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarPendenciaTipoGrupoGeral( id_grupo);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return lisGeral;
	}
}
