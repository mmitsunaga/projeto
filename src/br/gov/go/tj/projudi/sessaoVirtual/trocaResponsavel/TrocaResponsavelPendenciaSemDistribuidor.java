package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class TrocaResponsavelPendenciaSemDistribuidor extends TrocaResponsavelPendencia {

	public void trocarResponsavel() throws Exception {
		mensagem = pendenciaResponsavelNe.verificarTrocaResponsavel(pendenciaResponsavelDt,
				usuarioSessao.getUsuarioDt(),
				(String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")));
		if (mensagem.length() == 0) {
			realizaTrocaResponsavel();
		} else
			request.setAttribute("MensagemErro", mensagem);
		// atualiza objeto da tela
		pendenciaDt = pendenciaResponsavelNe.consultaSimplesId(usuarioSessao, pendenciaDt.getId());
		atualizaListaResponsavelPendencia();
	}

	private void atualizaListaResponsavelPendencia() throws Exception {
		List<PendenciaResponsavelDt> listaResponsavelPendencia = pendenciaResponsavelNe
				.consultarResponsaveisDetalhado(pendenciaDt.getId(), usuarioSessao.getUsuarioDt().getGrupoCodigo());

		if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0) {
			pendenciaDt.addResponsavel((PendenciaResponsavelDt) listaResponsavelPendencia.get(0));
			pendenciaResponsavelDt = new PendenciaResponsavelDt();
			pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
		}
	}

	private void realizaTrocaResponsavel() throws Exception, MensagemException {
		if (usuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
			// Verifica se a pendencia deverá ficar sem o assistente
			boolean ehSemAssistente = false;
			if (usuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes
					.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE) {
				ehSemAssistente = ((request.getParameter("ProcessoSemAssistente") != null
						&& String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")));
			}
			pendenciaResponsavelNe.AtualizarResponsavelPendencia(pendenciaResponsavelDt,
					usuarioSessao.getUsuarioDt().getGrupoCodigo(),
					usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(), pendenciaDt.getId_Processo(),
					ehSemAssistente);
			if (ehSemAssistente)
				request.setAttribute("MensagemOk", "A retirada do assistente foi realizada com sucesso!");
			else
				request.setAttribute("MensagemOk", "A troca de Responsável foi realizada com sucesso!");
			if (usuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes
					.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE) {
				request.getSession().setAttribute("TrocouResponsavel", "S");
			}
		} else
			throw new MensagemException("Não foi possível visualizar pendência. executar()");
	}

}
