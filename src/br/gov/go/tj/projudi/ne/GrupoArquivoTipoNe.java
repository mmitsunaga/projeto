package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.GrupoArquivoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class GrupoArquivoTipoNe extends GrupoArquivoTipoNeGen {

	//

	/**
     * 
     */
    private static final long serialVersionUID = -392223209910511410L;

    // ---------------------------------------------------------
	public String Verificar(GrupoArquivoTipoDt dados) {

		String stRetorno = "";

		if (dados.getId_Grupo().length() == 0) stRetorno += "É necessário selecionar um Grupo.";
//		if (dados.getId_ArquivoTipo().length() == 0) stRetorno += "É necessário selecionar um Tipo de Arquivo.";
		return stRetorno;

	}

	/**
	 * Consultar os tipos de arquivo definidos para um determinado grupo de usuários
	 */
	public List consultarGrupoArquivoTipo(String grupoCodigo, String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoArquivoTipoPs obPersistencia = new GrupoArquivoTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarGrupoArquivoTipo(grupoCodigo, descricao, posicao);
			//stUltimaConsulta = descricao;
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoArquivoTipoPs obPersistencia = new GrupoArquivoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarGrupoArquivoTipoJSON(grupoCodigo, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		GrupoNe Grupone = new GrupoNe(); 
		stTemp = Grupone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public List consultarArquivoTipoGrupo(String id_grupo) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoArquivoTipoPs obPersistencia = new GrupoArquivoTipoPs(obFabricaConexao.getConexao());
			lisGeral=obPersistencia.consultarArquivoTipoGrupoGeral( id_grupo);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lisGeral;
	}
	
	public void salvarMultiplo(GrupoArquivoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neGrupoArquivoTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GrupoArquivoTipoDt obDt = (GrupoArquivoTipoDt) lisGeral.get(i);
			boolean boEncontrado = false;
			// se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")) {
				// verifica se a listaEditada é nula, se for é pq ficou vazia na
				// tela e não precisa prosseguir
				if (listaEditada != null && listaEditada.length > 0) {

					// verifico qual id saiu da lista editada
					for (int j = 0; j < listaEditada.length; j++) {
						if (obDt.getId_ArquivoTipo().equalsIgnoreCase(
								(String) listaEditada[j])) {
							boEncontrado = true;
							break;
						}
					}
				}
				// se o id do objeto não foi encontrado na lista editada coloco
				// o objeto para ser excluido
				if (!boEncontrado) {
					lisExcluir.add(obDt);
				}
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
		if(listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++) {
				for(int j=0; j< lisGeral.size(); j++){
					GrupoArquivoTipoDt obDt = (GrupoArquivoTipoDt)lisGeral.get(j);
					if (obDt.getId_ArquivoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
			}
			
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoArquivoTipoPs obPersistencia = new GrupoArquivoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GrupoArquivoTipoDt obDt = (GrupoArquivoTipoDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
                obLogDt = new LogDt("GrupoArquivoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				GrupoArquivoTipoDt obDtTemp = (GrupoArquivoTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GrupoArquivoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void incluirMultiplo(GrupoArquivoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
		if(listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++) {
				for(int j=0; j< lisGeral.size(); j++){
					GrupoArquivoTipoDt obDt = (GrupoArquivoTipoDt)lisGeral.get(j);
					if (obDt.getId_ArquivoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
			}
			
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoArquivoTipoPs obPersistencia = new GrupoArquivoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				GrupoArquivoTipoDt obDt = (GrupoArquivoTipoDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
                obLogDt = new LogDt("GrupoArquivoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(GrupoArquivoTipoDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			GrupoArquivoTipoDt obDt = (GrupoArquivoTipoDt) lisGeral.get(i);
			// se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")) {
				if (obDt.getId_ArquivoTipo().equalsIgnoreCase(id)) {
					lisExcluir.add(obDt);
					break;
				}
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GrupoArquivoTipoPs obPersistencia = new GrupoArquivoTipoPs(obFabricaConexao.getConexao());

			for(int i = 0; i < lisExcluir.size(); i++) {
				GrupoArquivoTipoDt obDtTemp = (GrupoArquivoTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("GrupoArquivoTipo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
}
