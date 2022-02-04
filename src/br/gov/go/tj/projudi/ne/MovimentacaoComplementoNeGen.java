package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoComplementoDt;
import br.gov.go.tj.projudi.ps.MovimentacaoComplementoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class MovimentacaoComplementoNeGen extends Negocio {

	private static final long serialVersionUID = -8928437242681126007L;

	protected LogNe obLog;
	protected MovimentacaoComplementoDt obDados;

	public MovimentacaoComplementoNeGen() {
		
		obLog = new LogNe();

		obDados = new MovimentacaoComplementoDt();

	}

	// ---------------------------------------------------------
	public void salvar(MovimentacaoComplementoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		// ////System.out.println("..neMovimentacaoComplementosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------
	public abstract String Verificar(MovimentacaoComplementoDt dados);

	// ---------------------------------------------------------

	public void excluir(MovimentacaoComplementoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("MovimentacaoComplemento", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------

	public MovimentacaoComplementoDt consultarId(String id_movimentacaoComplemento) throws Exception {

		MovimentacaoComplementoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		// ////System.out.println("..ne-ConsultaId_MovimentacaoComplemento" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoComplementoPs obPersistencia = new MovimentacaoComplementoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_movimentacaoComplemento);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	// ---------------------------------------------------------


}
