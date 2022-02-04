package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt;
import br.gov.go.tj.projudi.ps.ServentiaSubtipoProcessoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ServentiaSubtipoProcessoTipoNe extends ServentiaSubtipoProcessoTipoNeGen {

	/**
     * 
     */
    private static final long serialVersionUID = -3136884105772134834L;

    public String Verificar(ServentiaSubtipoProcessoTipoDt dados) {

		String stRetorno = "";

		if (dados.getServentiaSubtipo().equalsIgnoreCase("")) stRetorno += "O Campo ServentiaSubtipo é obrigatório.";
		return stRetorno;

	}

	/**
	 * Consulta as classes disponíveis a partir da área de distribuição, chegando assim
	 * ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessoTipos(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarProcessoTipos(id_AreaDistribuicao, descricao, posicao);
			
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Consulta as classes disponíveis a partir da área de distribuição, chegando assim
	 * ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 */
	public List consultarProcessoTiposPublicos(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarProcessoTiposPublicos(id_AreaDistribuicao, descricao, posicao);
			
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Método que consulta os tipos de processos disponíveis em uma determinada Serventia Subtipo
	 */
	public List consultarDescricaoProcessoTipos(String id_ServentiaSubtipo, String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoProcessoTipos(id_ServentiaSubtipo, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDescricaoServentiaSubtipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String tempList = "";
		
		ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
		tempList = ServentiaSubtipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return tempList;
	}
	
	/**
	 * Método que verifica se um existe um determinado processo tipo para uma serventia subtipo
	 * 
	 * @param id_ServentiaSubTipo
	 *            , id do subtipo da Serventia
	 * @param id_ProcessoTipo
	 *            , id do processo tipo
	 * @return boolean            
	 * 
	 * @author lsbernardes
	 */	
	public boolean isProcessoTipoValido(String id_ServentiaSubTipo, String id_ProcessoTipo ) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isProcessoTipoValido(id_ServentiaSubTipo, id_ProcessoTipo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String consultarProcessoTiposJSON(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessoTiposJSON(id_AreaDistribuicao, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarProcessoTiposPublicosJSON(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessoTiposPublicosJSON(id_AreaDistribuicao, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public void incluirMultiplo(ServentiaSubtipoProcessoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
		if(listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++) {
				for(int j=0; j< lisGeral.size(); j++){
					ServentiaSubtipoProcessoTipoDt obDt = (ServentiaSubtipoProcessoTipoDt)lisGeral.get(j);
					if (obDt.getId_ProcessoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
			}
		}
			
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ServentiaSubtipoProcessoTipoDt obDt = (ServentiaSubtipoProcessoTipoDt)lisIncluir.get(i);			
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ServentiaSubtipoProcessoTipo", obDt.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	//---------------------------------------------------------
	public void excluir(ServentiaSubtipoProcessoTipoDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		//////System.out.println("..neServentiaSubtipoProcessoTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ServentiaSubtipoProcessoTipoDt obDt = (ServentiaSubtipoProcessoTipoDt)lisGeral.get(i);
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_ProcessoTipo().equalsIgnoreCase(id)){
				//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
				lisExcluir.add(obDt);
				break;
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisExcluir.size(); i++) {
				ServentiaSubtipoProcessoTipoDt obDtTemp = (ServentiaSubtipoProcessoTipoDt)lisExcluir.get(i); 
				obLogDt = new LogDt("ServentiaSubtipoProcessoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarProcessoTipoServentiaSubtipo(String id_serventiasubtipo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao =null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaSubtipoProcessoTipoPs obPersistencia = new ServentiaSubtipoProcessoTipoPs(obFabricaConexao.getConexao());
				lisGeral = obPersistencia.consultarProcessoTipoServentiaSubtipoGeral( id_serventiasubtipo);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return lisGeral;   
	}
}
