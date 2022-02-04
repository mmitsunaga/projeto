package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;

public class TrocaResponsavelProcesso implements TrocaResponsavel {

	PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
	ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
	MovimentacaoNe Movimentacaone = new MovimentacaoNe();
	ProcessoNe processoNe = new ProcessoNe();
	HttpServletRequest request;
	UsuarioNe usuarioSessao;
	PendenciaDt pendenciaDt;
	List<PendenciaDt> listaDePendenciasDt;
	PendenciaResponsavelDt pendenciaResponsavelDt;

	protected void preencheParametros(TrocaResponsavelParam params) {
		this.pendenciaResponsavelDt = params.getPendenciaResponsavelDt();
		this.pendenciaResponsavelNe = params.getPendenciaResponsavelNe();
		this.pendenciaDt = params.getPendenciaDt();
		this.usuarioSessao = params.getUsuarioSessao();
		this.request = params.getRequest();
	}

	@Override
	public void trocarResponsavel() throws Exception {
		PendenciaResponsavelDt pendenciaResponsavelDtAtual = null;
		List<PendenciaResponsavelDt> listaResponsavelPendencia = pendenciaResponsavelNe.consultarResponsaveisDetalhado(
				pendenciaResponsavelDt.getId_Pendencia(), usuarioSessao.getUsuarioDt().getGrupoCodigo(), null);
		if (listaResponsavelPendencia != null && listaDePendenciasDt.size() > 0) {
			pendenciaResponsavelDtAtual = configuraPendRespDtAtual(pendenciaResponsavelDtAtual,
					listaResponsavelPendencia);
		}
		ProcessoDt processoDt = processoNe.consultarId(pendenciaDt.getId_Processo());
		List<ServentiaCargoDt> listaPromotoresResponsaveis = processoResponsavelNe
				.consultarResponsavelProcessoPromotores(processoDt.getId());
		ServentiaCargoDt serventiaCargoDt = null;
		if (listaPromotoresResponsaveis != null && listaPromotoresResponsaveis.size() > 0) {
			serventiaCargoDt = configuraServentiaCargoDt(pendenciaResponsavelDtAtual, listaPromotoresResponsaveis,
					serventiaCargoDt);
		}
		String id_ServentiaCargoAnterior = "";
		if (serventiaCargoDt != null) {
			id_ServentiaCargoAnterior = serventiaCargoDt.getId();
		}
		if (!id_ServentiaCargoAnterior.equalsIgnoreCase(pendenciaResponsavelDt.getId_ServentiaCargo())) {
			salvaMovimentacao(processoDt, id_ServentiaCargoAnterior);
		}
	}

	private PendenciaResponsavelDt configuraPendRespDtAtual(PendenciaResponsavelDt pendenciaResponsavelDtAtual,
			List<PendenciaResponsavelDt> listaResponsavelPendencia) {
		for (PendenciaResponsavelDt temp : listaResponsavelPendencia) {
			if (temp.getCargoTipoCodigo() != null
					&& temp.getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.MINISTERIO_PUBLICO))) {
				pendenciaResponsavelDtAtual = temp;
				break;
			}
		}
		if (pendenciaResponsavelDtAtual == null) {
			pendenciaResponsavelDtAtual = (PendenciaResponsavelDt) listaResponsavelPendencia.get(0);
		}
		return pendenciaResponsavelDtAtual;
	}

	private void salvaMovimentacao(ProcessoDt processoDt, String id_ServentiaCargoAnterior) throws Exception {
		LogDt log = new LogDt(usuarioSessao.getId_Usuario(), usuarioSessao.getIpComputadorLog());
		processoResponsavelNe.salvarTrocaResponsavelPromotor(pendenciaResponsavelDt.getId_ServentiaCargo(),
				id_ServentiaCargoAnterior, pendenciaResponsavelDt.getId_UsuarioResponsavel(),
				usuarioSessao.getUsuarioDt().getGrupoCodigo(), Arrays.asList(processoDt),
				String.valueOf(CargoTipoDt.MINISTERIO_PUBLICO), log);
		String nomeResponsavelAnterior = new UsuarioNe().consultarNomeUsuarioServentiaCargo(id_ServentiaCargoAnterior);
		if (nomeResponsavelAnterior == null)
			nomeResponsavelAnterior = "";
		MovimentacaoDt movimentacaoDt = montaMovimentacao(processoDt, log, nomeResponsavelAnterior);
		Movimentacaone.salvar(movimentacaoDt);
	}

	private MovimentacaoDt montaMovimentacao(ProcessoDt processoDt, LogDt log, String nomeResponsavelAnterior)
			throws Exception {
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		movimentacaoDt.setId_Processo(processoDt.getId());
		movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
		movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.TROCAR_RESPONSAVEL_PROCESSO));
		movimentacaoDt.setMovimentacaoTipo("Troca de Responsável");
		movimentacaoDt.setComplemento("MP Responsável Anterior: " + nomeResponsavelAnterior + " <br> "
				+ "MP Responsável Atual: "
				+ new UsuarioNe().consultarNomeUsuarioServentiaCargo(pendenciaResponsavelDt.getId_ServentiaCargo()));
		movimentacaoDt.setId_UsuarioRealizador(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
		movimentacaoDt.setId_UsuarioLog(log.getId_Usuario());
		movimentacaoDt.setIpComputadorLog(log.getIpComputador());
		return movimentacaoDt;
	}

	private ServentiaCargoDt configuraServentiaCargoDt(PendenciaResponsavelDt pendenciaResponsavelDtAtual,
			List<ServentiaCargoDt> listaPromotoresResponsaveis, ServentiaCargoDt serventiaCargoDt) {
		for (ServentiaCargoDt promotor : listaPromotoresResponsaveis) {
			if (pendenciaResponsavelDt.getId_ServentiaCargo().equalsIgnoreCase(promotor.getId())) {
				serventiaCargoDt = promotor;
				break;
			}
		}
		if (serventiaCargoDt == null && pendenciaResponsavelDtAtual != null
				&& pendenciaResponsavelDtAtual.getId_ServentiaCargo() != null
				&& pendenciaResponsavelDtAtual.getId_ServentiaCargo().trim().length() > 0) {
			for (ServentiaCargoDt promotor : listaPromotoresResponsaveis) {
				if (pendenciaResponsavelDtAtual.getId_ServentiaCargo().equalsIgnoreCase(promotor.getId())) {
					serventiaCargoDt = promotor;
					break;
				}
			}
		}
		if (serventiaCargoDt == null) {
			for (ServentiaCargoDt promotor : listaPromotoresResponsaveis) {
				if (Funcoes.StringToInt(promotor.getCodigoTemp()) == ProcessoResponsavelDt.ATIVO) {
					serventiaCargoDt = promotor;
					break;
				}
			}
		}
		return serventiaCargoDt;
	}

}
