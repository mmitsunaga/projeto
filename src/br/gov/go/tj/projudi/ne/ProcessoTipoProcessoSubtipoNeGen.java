package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoProcessoSubtipoDt;
import br.gov.go.tj.projudi.ps.ProcessoTipoProcessoSubtipoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ProcessoTipoProcessoSubtipoNeGen extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8362863304690354366L;

	protected  ProcessoTipoProcessoSubtipoDt obDados;

	protected List lisGeral = null; 
	public ProcessoTipoProcessoSubtipoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ProcessoTipoProcessoSubtipoDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(ProcessoTipoProcessoSubtipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;


		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		if (listaEditada == null) 
			listaEditada = new String[0];
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ProcessoTipoProcessoSubtipoDt obDt = (ProcessoTipoProcessoSubtipoDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifico qual id saiu da lista editada
			for(int j=0; j< listaEditada.length; j++)
					if (obDt.getId_ProcessoTipo().equalsIgnoreCase((String)listaEditada[j])){
						boEncontrado = true;
						break;
					}
			//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
			if (!boEncontrado) lisExcluir.add(obDt);
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		for (int i = 0; i < listaEditada.length; i++)
			for(int j=0; j< lisGeral.size(); j++){
				ProcessoTipoProcessoSubtipoDt obDt = (ProcessoTipoProcessoSubtipoDt)lisGeral.get(j);
				if (obDt.getId_ProcessoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
					lisIncluir.add(obDt);
					break;
				}
			}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				ProcessoTipoProcessoSubtipoDt obDt = (ProcessoTipoProcessoSubtipoDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("ProcessoTipoProcessoSubtipo", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				ProcessoTipoProcessoSubtipoDt obDtTemp = (ProcessoTipoProcessoSubtipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("ProcessoTipoProcessoSubtipo", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarProcessoTipoProcessoSubtipoUlLiCheckBox(String id_processosubtipo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
		String tempUlLi="<ul>";


			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarProcessoTipoProcessoSubtipoGeral( id_processosubtipo);
				for(int i = 0; i < lisGeral.size(); i++) {
					ProcessoTipoProcessoSubtipoDt obDtTemp = (ProcessoTipoProcessoSubtipoDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(ProcessoTipoProcessoSubtipoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoTipoProcessoSubtipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoTipoProcessoSubtipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ProcessoTipoProcessoSubtipoDt dados ); 


//---------------------------------------------------------

	public void excluir(ProcessoTipoProcessoSubtipoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoTipoProcessoSubtipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ProcessoTipoProcessoSubtipoDt consultarId(String id_processotipoprocessosubtipo ) throws Exception {

		ProcessoTipoProcessoSubtipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_processotipoprocessosubtipo ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;


			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoProcessoSubtipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoSubtipoNe ProcessoSubtipone = new ProcessoSubtipoNe(); 
		tempList = ProcessoSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoSubtipone.getQuantidadePaginas();
		ProcessoSubtipone = null;
		return tempList;
	}

	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		ProcessoTipoNe ProcessoTipone = new ProcessoTipoNe(); 
		tempList = ProcessoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoTipone.getQuantidadePaginas();
		ProcessoTipone = null;
		return tempList;
	}
}
