package br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.sessaoVirtual.votacao.VotacaoUtils;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class FinalizacaoRetirarPauta extends FinalizacaoExtratoAta {
	
	protected static final List<Integer> PENDENCIA_TIPO_ADICIONAL_FINALIZAR_RETIRAR_PAUTA = Arrays.asList(PendenciaTipoDt.CONCLUSO_VOTO, PendenciaTipoDt.CONCLUSO_EMENTA);

	public FinalizacaoRetirarPauta(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt,
			FabricaConexao fabricaConexao) {
		super(audienciaMovimentacaoDt, usuarioDt, logDt, fabricaConexao);
	}
	
	@Override
	public void make() throws Exception {
		AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
        AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
        
		int codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());	     
        
        boolean wasVotacaoIniciada = audienciaProcessoNe.verificarVotacaoIniciada(audienciaProcessoDt.getId(), obFabricaConexao);
		audienciaNe.salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, null, usuarioDt, obFabricaConexao);
		
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_PendenciaVotoRelator())) {
			PendenciaDt pendenciaDtVoto = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaVotoRelator());
			if (pendenciaDtVoto != null) {
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaDtVoto, usuarioDt, PendenciaStatusDt.ID_CANCELADA, obFabricaConexao);
			}
		}
		
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_PendenciaEmentaRelator())) {
			PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaEmentaRelator());
			if (pendenciaDtEmenta != null) {
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaDtEmenta, usuarioDt, PendenciaStatusDt.ID_CANCELADA, obFabricaConexao);
			}
		}
		
		if(codigoStatus == AudienciaProcessoStatusDt.RETIRAR_PAUTA) {
			// Apaga pendencias de aguardando voto e para conhecimento
			votoNe.finalizarTodasPendenciasVotacao(audienciaProcessoDt, usuarioDt, obFabricaConexao, PENDENCIA_TIPO_ADICIONAL_FINALIZAR_RETIRAR_PAUTA);

			// Cria pendencias de retirar de pauta
			List<String> votantesMp = votoNe.consultarIdsIntegrantesSessaoPorTipo(audienciaProcessoDt.getId(),
					getListaVotantesTiposGerarPendencia(wasVotacaoIniciada));
			
			// jvosantos - 16/12/2019 14:49 - Adicionar Pendencia de Retirar de Pauta na AUDI_PROC_PEND
			List<String> idPends = new ArrayList<String>();
			for (String id : votantesMp) {
				PendenciaDt pendenciaRetirarPauta = pendenciaNe.gerarPendenciaRetirarPauta(id, null, usuarioDt, getIdProcesso(audienciaProcessoDt), audienciaProcessoDt.getId(), obFabricaConexao);
				if(pendenciaRetirarPauta != null) idPends.add(pendenciaRetirarPauta.getId());
			}
			
			if(!idPends.isEmpty())
				audienciaProcessoPendenciaNe.salvar(idPends, audienciaProcessoDt.getId(), obFabricaConexao);
		}
	}
}
