package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.MensagemException;

public class TrocaResponsavelPendenciaComDistribuidor extends TrocaResponsavelPendencia {

	public void trocarResponsavel() throws Exception {
		mensagem = pendenciaResponsavelNe.verificarTrocaResponsavelConclusao(pendenciaResponsavelDt);
		if (mensagem.length() == 0) {
			realizaTrocaResponsavel(pendenciaResponsavelDt, pendenciaResponsavelNe, pendenciaDt, usuarioSessao,
					request);
		} else
			request.setAttribute("MensagemErro", mensagem);
	}

	private void realizaTrocaResponsavel(PendenciaResponsavelDt pendenciaResponsavelDt,
			PendenciaResponsavelNe pendenciaResponsavelNe, PendenciaDt pendenciaDt, UsuarioNe usuarioSessao,
			HttpServletRequest request) throws Exception, MensagemException {
		if (usuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
			if (pendenciaResponsavelNe.isReDistribuirConclusaoUnidadeTrabalho(pendenciaDt,
					usuarioSessao.getUsuarioDt())) {
				pendenciaDt = tratarTrocaERedistribuirUnidadeTrabalho(pendenciaResponsavelDt, pendenciaResponsavelNe,
						pendenciaDt, usuarioSessao);
			} else {
				pendenciaDt = tratarTrocaSemRedistribuirUnidadeTrabalho(pendenciaResponsavelDt, pendenciaResponsavelNe,
						pendenciaDt, usuarioSessao);
				request.getSession().setAttribute("DistribuicaoEfetuada", "DE");
			}
			request.setAttribute("MensagemOk", "A distribuição foi realizada com sucesso!");

			atualizaListaResponsavelPendencia(pendenciaResponsavelNe, pendenciaDt, usuarioSessao);
		} else
			throw new MensagemException("Não foi possível visualizar pendência. executar()");
	}

	private void atualizaListaResponsavelPendencia(PendenciaResponsavelNe pendenciaResponsavelNe,
			PendenciaDt pendenciaDt, UsuarioNe usuarioSessao) throws Exception {
		PendenciaResponsavelDt pendenciaResponsavelDt;
		List<PendenciaResponsavelDt> listaResponsavelPendencia = pendenciaResponsavelNe
				.consultarResponsaveisDetalhado(pendenciaDt.getId(), usuarioSessao.getUsuarioDt().getGrupoCodigo());
		if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0) {
			pendenciaDt.addResponsavel((PendenciaResponsavelDt) listaResponsavelPendencia.get(0));
			pendenciaResponsavelDt = new PendenciaResponsavelDt();
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
			pendenciaResponsavelDt.setListaHistoricoPendencia(
					pendenciaResponsavelNe.consultarHistoricosPendencia(pendenciaDt.getId()));
		}
	}

	private PendenciaDt tratarTrocaSemRedistribuirUnidadeTrabalho(PendenciaResponsavelDt pendenciaResponsavelDt,
			PendenciaResponsavelNe pendenciaResponsavelNe, PendenciaDt pendenciaDt, UsuarioNe usuarioSessao)
			throws Exception {
		pendenciaResponsavelNe.distribuirConclusaoUnidadeTrabalho(pendenciaResponsavelDt, usuarioSessao.getUsuarioDt(),
				pendenciaDt.getId_Processo());
		pendenciaDt = pendenciaResponsavelNe.consultaSimplesId(usuarioSessao, pendenciaDt.getId());
		pendenciaDt.setHash("");
		return pendenciaDt;
	}

	private PendenciaDt tratarTrocaERedistribuirUnidadeTrabalho(PendenciaResponsavelDt pendenciaResponsavelDt,
			PendenciaResponsavelNe pendenciaResponsavelNe, PendenciaDt pendenciaDt, UsuarioNe usuarioSessao)
			throws Exception {
		pendenciaResponsavelNe.reDistribuirConclusaoUnidadeTrabalho(pendenciaResponsavelDt,
				usuarioSessao.getUsuarioDt(), pendenciaDt, "");
		pendenciaDt = pendenciaResponsavelNe.consultaSimplesId(usuarioSessao, pendenciaDt.getId());
		pendenciaResponsavelDt
				.setListaHistoricoPendencia(pendenciaResponsavelNe.consultarHistoricosPendencia(pendenciaDt.getId()));
		if (pendenciaResponsavelDt.getListaHistoricoPendencia() != null
				&& pendenciaResponsavelDt.getListaHistoricoPendencia().size() > 0) {
			PendenciaResponsavelHistoricoDt obTempHistorico = (PendenciaResponsavelHistoricoDt) pendenciaResponsavelDt
					.getListaHistoricoPendencia().get(pendenciaResponsavelDt.getListaHistoricoPendencia().size() - 1);
			ServentiaGrupoDt obTempServentiaGrupo = pendenciaResponsavelNe
					.consultarSeventiaGrupoId(obTempHistorico.getId_ServentiaGrupo());
			pendenciaResponsavelDt.setId_ServentiaGrupo(obTempServentiaGrupo.getId_ServentiaGrupoProximo());
			pendenciaResponsavelDt.setServentiaGrupo(obTempServentiaGrupo.getServentiaGrupoProximo());

			if (obTempHistorico.getDataFim() != null && obTempHistorico.getDataFim().length() == 0) {
				pendenciaDt.setId_ServentiaGrupo(obTempHistorico.getId_ServentiaGrupo());
				pendenciaDt.setServentiaGrupo(obTempHistorico.getServentiaGrupo());
			}
		}
		return pendenciaDt;
	}

}
