package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.projudi.ps.PendenciaCorreiosPs;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class PendenciaCorreiosNeGen extends Negocio {

	private static final long serialVersionUID = -1833865684281173171L;
	protected PendenciaCorreiosDt obDados;

	public PendenciaCorreiosNeGen() {
		obLog = new LogNe();
		obDados = new PendenciaCorreiosDt();
	}

	public void salvar(PendenciaCorreiosDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length() == 0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("PendenciaCorreio", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("PendenciaCorreio", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(),
						dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw new Exception(" <{Erro:.....}> PendenciaCorreioNeGen.salvar() " + e.getMessage());
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public abstract String Verificar(PendenciaCorreiosDt dados);

	public void excluir(PendenciaCorreiosDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());

			obLogDt = new LogDt("PendenciaCorreio", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(),
					String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public PendenciaCorreiosDt consultarId(String id_pendcorreios) throws Exception {

		PendenciaCorreiosDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());

			dtRetorno = obPersistencia.consultarId(id_pendcorreios);
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}


	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}

	public List consultarDescricao(String descricao, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaCorreiosPs obPersistencia = new PendenciaCorreiosPs(obFabricaConexao.getConexao());

			tempList = obPersistencia.consultarDescricao(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarDescricaoPend(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		PendenciaNe Pendne = new PendenciaNe();
		tempList = Pendne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Pendne.getQuantidadePaginas();
		Pendne = null;
		return tempList;
	}

//	public String consultarDescricaoPendJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
//		String stTemp = (new PendenciaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
//		return stTemp;
//	}

}
