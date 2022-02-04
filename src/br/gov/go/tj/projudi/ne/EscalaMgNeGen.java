package br.gov.go.tj.projudi.ne; 

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.EscalaMgDt;
import br.gov.go.tj.projudi.ps.EscalaMgPs;

//---------------------------------------------------------
public abstract class EscalaMgNeGen extends Negocio {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8531404640407806018L;

	protected EscalaMgDt obDados;

	public EscalaMgNeGen() {

		obLog = new LogNe();

		obDados = new EscalaMgDt();

	}

	// ---------------------------------------------------------
	public void salvar(EscalaMgDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length() == 0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("EscalaMg", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("EscalaMg", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(),
						dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw new Exception(" <{Erro:.....}> EscalaMgNeGen.salvar() " + e.getMessage());
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------
	public abstract String Verificar(EscalaMgDt dados);

	// ---------------------------------------------------------

	public void excluir(EscalaMgDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			obLogDt = new LogDt("EscalaMg", dados.getId(), dados.getId_UsuarioLog(),
					dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// ---------------------------------------------------------

	public EscalaMgDt consultarId(String id_escala_mg) throws Exception {

		EscalaMgDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			EscalaMgPs obPersistencia = new EscalaMgPs(obFabricaConexao.getConexao());

			dtRetorno = obPersistencia.consultarId(id_escala_mg);
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

}
