package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoProcessoSubtipoDt;
import br.gov.go.tj.projudi.ps.ProcessoTipoProcessoSubtipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoTipoProcessoSubtipoNe extends ProcessoTipoProcessoSubtipoNeGen{

    private static final long serialVersionUID = -8902650494648920534L;

	public  String Verificar(ProcessoTipoProcessoSubtipoDt dados ) {

		String stRetorno="";

		if (dados.getProcessoSubtipo().length()==0)
			stRetorno += "O Campo ProcessoSubtipo é obrigatório.";
		//if (dados.getProcessoTipo().length()==0)stRetorno += "O Campo ProcessoTipo é obrigatório.";

		return stRetorno;

	}
	
	/**
	 * Consulta se o processo é Não contencioso, ou seja, não precisa da parte promovida
	 * @param processoTipoCodigo
	 * @return
	 * @throws Exception
	 */
	public boolean isNaoContecioso(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarProcessoTipoCodigoProcessoSubtipoCodigo(processoTipoCodigo,ProcessoSubtipoDt.NAO_CONTENCIOSO);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * Consulta se o processo é classificado como o subTipo Execução
	 * @param processoTipoCodigo
	 * @return
	 * @throws Exception
	 */
	public boolean isExecucao(int processoTipoCodigo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarProcessoTipoCodigoProcessoSubtipoCodigo(processoTipoCodigo,ProcessoSubtipoDt.PROCESSO_EXECUCAO);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String consultarDescricaoProcessoSubtipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ProcessoSubtipoNe ProcessoSubtipone = new ProcessoSubtipoNe(); 
		stTemp = ProcessoSubtipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public List consultarProcessoTipoProcessoSubtipo(String id_processosubtipo) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarProcessoTipoProcessoSubtipoGeral(id_processosubtipo);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return lisGeral;
	}
	
	public void incluirMultiplo(ProcessoTipoProcessoSubtipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;


		if (listaEditada == null) 
			listaEditada = new String[0];

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
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	//---------------------------------------------------------
	public void excluir(ProcessoTipoProcessoSubtipoDt dados, String id) throws Exception {

		LogDt obLogDt;


		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			ProcessoTipoProcessoSubtipoDt obDt = (ProcessoTipoProcessoSubtipoDt)lisGeral.get(i);
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_ProcessoTipo().equals(id)){
				lisExcluir.add(obDt);
				break;
			}
		}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoTipoProcessoSubtipoPs obPersistencia = new ProcessoTipoProcessoSubtipoPs(obFabricaConexao.getConexao());
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
}
