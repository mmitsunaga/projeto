package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.LogPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class LogNeGen extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782105835840272006L;	
	protected LogDt obDados;

	public LogNeGen() {
			
		obDados = new LogDt();
	}

	// ---------------------------------------------------------
	public void salvar(LogDt dados) throws Exception {
	    FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
			} else {
				obPersistencia.alterar(dados);
			}

			obDados.copiar(dados);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------
	public abstract String Verificar(LogDt dados);

	// ---------------------------------------------------------

	public void excluir(LogDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Log", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------

	public LogDt consultarId(String id_log) throws Exception {
		LogDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_Log");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_log);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	// ---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	// ---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao) throws Exception {
		List tempList = null;
		//////System.out.println("..ne-ConsultaLog");
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarDescricaoLogTipo(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		LogTipoNe LogTipone = new LogTipoNe();
		tempList = LogTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = LogTipone.getQuantidadePaginas();
		LogTipone = null;
		return tempList;
	}

	public List consultarDescricaoUsuario(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		try{
			UsuarioNe Usuarione = new UsuarioNe();
			tempList = Usuarione.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Usuarione.getQuantidadePaginas();
			Usuarione = null;
		} catch(Exception e) {

			throw e;
		}
		return tempList;
	}

}
