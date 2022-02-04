package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ps.UsuarioMovimentacaoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class UsuarioMovimentacaoTipoNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -615991667321436153L;
		
	protected UsuarioMovimentacaoTipoDt obDados;

	protected List lisGeral = null;
	
	public static final boolean INCLUIR = true;
	public static final boolean EXCLUIR = false;
	
	private static int NUMERO_MAXIMO_TIPO_MOVIMENTACAO = 10;
	
	public UsuarioMovimentacaoTipoNe() {
		
		obLog = new LogNe();
		obDados = new UsuarioMovimentacaoTipoDt();
	}
	
	public void limpar()
	{
		this.lisGeral = null;
	}


//---------------------------------------------------------
	public void salvarRegistros (UsuarioDt usuarioDt, String[] listaEditada, boolean operacao) throws Exception{
		
		preencheListaGeral(usuarioDt);
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		List lisIncluir = new ArrayList();
		List lisExcluir = new ArrayList();
		
		if (operacao == INCLUIR){
			//verifico os ids as serem incluidos
			if (listaEditada != null){
				for (int i = 0; i < listaEditada.length; i++){
					for(int j=0; j< lisGeral.size(); j++){
						UsuarioMovimentacaoTipoDt obDt = (UsuarioMovimentacaoTipoDt)lisGeral.get(j);
						if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
							lisIncluir.add(obDt);
							break;
						}
					}
				}
			}
		}
		
		if (operacao == EXCLUIR){
			//verifico os ids as serem excluidos
			if (listaEditada != null){
				for (int i = 0; i < listaEditada.length; i++){
					for(int j=0; j< lisGeral.size(); j++){
						UsuarioMovimentacaoTipoDt obDt = (UsuarioMovimentacaoTipoDt)lisGeral.get(j);
						if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[i])/* && obDt.getId().equalsIgnoreCase("")*/){
							lisExcluir.add(obDt);
							break;
						}
					}
				}
			}
		}
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioMovimentacaoTipoPs obPersistencia = new UsuarioMovimentacaoTipoPs( obFabricaConexao.getConexao());
			
			if (operacao == INCLUIR){
				for(int i = 0; i < lisIncluir.size(); i++) {
					UsuarioMovimentacaoTipoDt obDt = (UsuarioMovimentacaoTipoDt)lisIncluir.get(i);
					
					obPersistencia.inserir(usuarioDt.getId(), usuarioDt.getGrupoCodigo(), obDt);
					obLogDt = new LogDt("UsuarioMovimentacaoTipo", obDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades(usuarioDt.getId(), usuarioDt.getGrupoCodigo()));
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			if (operacao == EXCLUIR){
				for(int i = 0; i < lisExcluir.size(); i++) {
					UsuarioMovimentacaoTipoDt obDtTemp = (UsuarioMovimentacaoTipoDt)lisExcluir.get(i); 
					
					obLogDt = new LogDt("UsuarioMovimentacaoTipo", obDtTemp.getId(), usuarioDt.getId(),usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(usuarioDt.getId(), usuarioDt.getGrupoCodigo()),"");
					obPersistencia.excluir(obDtTemp.getId());
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void preencheListaGeral(UsuarioDt usuarioDt) throws Exception {
		lisGeral=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioMovimentacaoTipoPs obPersistencia = new UsuarioMovimentacaoTipoPs( obFabricaConexao.getConexao());
			lisGeral=obPersistencia.consultaTodosGeral(usuarioDt.getId(), usuarioDt.getGrupoCodigo());
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void salvarMultiplo(UsuarioDt usuarioDt, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			UsuarioMovimentacaoTipoDt obDt = (UsuarioMovimentacaoTipoDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
				if (listaEditada != null){
					for(int j=0; j< listaEditada.length; j++)
						if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[j])){
							boEncontrado = true;
							break;
						}
				}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		if (listaEditada != null){
			for (int i = 0; i < listaEditada.length; i++){
				for(int j=0; j< lisGeral.size(); j++){
					UsuarioMovimentacaoTipoDt obDt = (UsuarioMovimentacaoTipoDt)lisGeral.get(j);
					if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioMovimentacaoTipoPs obPersistencia = new UsuarioMovimentacaoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				UsuarioMovimentacaoTipoDt obDt = (UsuarioMovimentacaoTipoDt)lisIncluir.get(i);
				
				obPersistencia.inserir(usuarioDt.getId(), usuarioDt.getGrupoCodigo(), obDt);
				obLogDt = new LogDt("UsuarioMovimentacaoTipo", obDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades(usuarioDt.getId(), usuarioDt.getGrupoCodigo()));
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				UsuarioMovimentacaoTipoDt obDtTemp = (UsuarioMovimentacaoTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("UsuarioMovimentacaoTipo", obDtTemp.getId(), usuarioDt.getId(),usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(usuarioDt.getId(), usuarioDt.getGrupoCodigo()),"");
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

	//Localizar Novo
	public String consultarDescricaoJSON(UsuarioDt usuarioDt, String descricao, String posicao, String ordenacao, String quantidadeRegistros, boolean somenteMarcados, String pesquisa) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioMovimentacaoTipoPs obPersistencia = new UsuarioMovimentacaoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(usuarioDt.getId(), usuarioDt.getGrupoCodigo(), descricao, posicao, ordenacao, quantidadeRegistros, somenteMarcados, pesquisa);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarMovimentacaoTipoGrupoUlLiCheckBox(UsuarioDt usuarioDt) throws Exception {

		lisGeral=null;
		String tempUlLi="";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				UsuarioMovimentacaoTipoPs obPersistencia = new UsuarioMovimentacaoTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultaTodosGeral(usuarioDt.getId(), usuarioDt.getGrupoCodigo());
				if (lisGeral.size() > 0){
					tempUlLi = "<ul>";
					for(int i = 0; i < lisGeral.size(); i++) {
						UsuarioMovimentacaoTipoDt obDtTemp = (UsuarioMovimentacaoTipoDt)lisGeral.get(i); 

						tempUlLi+= obDtTemp.getListaLiCheckBox();
					}
					tempUlLi+="</ul>";	
				}				
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}


	public String Verificar(String[] idsDados) {
		String stRetorno = "";
		if (lisGeral == null || lisGeral.size() == 0){
			stRetorno = "Não existem dados a serem gravados.";
		}else if ((idsDados != null) && (idsDados.length > NUMERO_MAXIMO_TIPO_MOVIMENTACAO)){
			stRetorno = String.format("Selecione no máximo %s Tipos de Movimentações.", NUMERO_MAXIMO_TIPO_MOVIMENTACAO);
		}
		return stRetorno;
	}

	public List consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioMovimentacaoTipoPs obPersistencia = new UsuarioMovimentacaoTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultaTodosMarcados(usuarioDt.getId(), usuarioDt.getGrupoCodigo());								
		} finally {
			obFabricaConexao.fecharConexao();
		}		
	}

}
