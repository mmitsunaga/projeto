package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoMovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ps.MovimentacaoTipoMovimentacaoTipoClassePs;
import br.gov.go.tj.utils.FabricaConexao;

public class MovimentacaoTipoMovimentacaoTipoClasseNe extends MovimentacaoTipoMovimentacaoTipoClasseNeGen {

	private static final long serialVersionUID = 7852752503194253357L;

	public String Verificar(MovimentacaoTipoMovimentacaoTipoClasseDt dados) {

		String stRetorno = "";

		if (dados.getId_MovimentacaoTipoClasse().length() == 0)
			stRetorno += "O Campo Identificador é obrigatório.";

		return stRetorno;

	}

	public void salvarMultiplo(MovimentacaoTipoMovimentacaoTipoClasseDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;

		// verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		// pego a lista geral e procuro os que tem id
		// somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisGeral.get(i);
			boolean boEncontrado = false;
			// se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")) {
				// se a listaEditada for null é porque todos os itens estão
				// desmarcados e devem ser excluídos os registros que existem
				if (listaEditada != null) {
					// verifico qual id saiu da lista editada
					for (int j = 0; j < listaEditada.length; j++)
						if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String) listaEditada[j])) {
							boEncontrado = true;
							break;
						}
					// se o id do objeto não foi encontrado na lista editada
					// coloco o objeto para ser excluido
					if (!boEncontrado)
						lisExcluir.add(obDt);
				} else {
					lisExcluir.add(obDt);
				}

			}
		}

		// verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		// se a listaEditada é null, não há o que incluir
		if (listaEditada != null) {
			for (int i = 0; i < listaEditada.length; i++)
				for (int j = 0; j < lisGeral.size(); j++) {
					MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisGeral.get(j);
					if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String) listaEditada[i]) && obDt.getId().equalsIgnoreCase("")) {
						lisIncluir.add(obDt);
						break;
					}
				}
		}

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(obFabricaConexao.getConexao());
			for (int i = 0; i < lisIncluir.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse", obDt.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for (int i = 0; i < lisExcluir.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDtTemp = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisExcluir.get(i);

				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse", obDtTemp.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), obDtTemp.getPropriedades(), "");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoMovimentacaoTipoClasseJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		MovimentacaoTipoClasseNe MovimentacaoTipoClassene = new MovimentacaoTipoClasseNe(); 
		stTemp = MovimentacaoTipoClassene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public void incluirMultiplo(MovimentacaoTipoMovimentacaoTipoClasseDt dados, String[] listaEditada)
			throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		// verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		// verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não
		// precisa prosseguir
		if (listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++) {
				for (int j = 0; j < lisGeral.size(); j++) {
					MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisGeral
							.get(j);
					if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase((String) listaEditada[i])
							&& obDt.getId().equalsIgnoreCase("")) {
						lisIncluir.add(obDt);
						break;
					}
				}
			}

		}
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(
					obFabricaConexao.getConexao());
			for (int i = 0; i < lisIncluir.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisIncluir
						.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluir(MovimentacaoTipoMovimentacaoTipoClasseDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		// verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		// pego a lista geral e procuro os que tem id
		// somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			MovimentacaoTipoMovimentacaoTipoClasseDt obDt = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisGeral.get(i);
			// se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")) {
				if (obDt.getId_MovimentacaoTipo().equalsIgnoreCase(id)) {
					lisExcluir.add(obDt);
					break;
				}
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(
					obFabricaConexao.getConexao());

			for (int i = 0; i < lisExcluir.size(); i++) {
				MovimentacaoTipoMovimentacaoTipoClasseDt obDtTemp = (MovimentacaoTipoMovimentacaoTipoClasseDt) lisExcluir
						.get(i);

				obLogDt = new LogDt("MovimentacaoTipoMovimentacaoTipoClasse", dados.getId(), dados.getId_UsuarioLog(),
						dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), obDtTemp.getPropriedades(), "");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarMovimentacaoTipoMovimentacaoTipoClasse(String idMovimentacaoTipoMovimentacaoTipoClasse)
			throws Exception {

		lisGeral = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MovimentacaoTipoMovimentacaoTipoClassePs obPersistencia = new MovimentacaoTipoMovimentacaoTipoClassePs(
					obFabricaConexao.getConexao());
			lisGeral = obPersistencia
					.consultarMovimentacaoTipoMovimentacaoTipoClasseGeral(idMovimentacaoTipoMovimentacaoTipoClasse);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lisGeral;
	}

}
