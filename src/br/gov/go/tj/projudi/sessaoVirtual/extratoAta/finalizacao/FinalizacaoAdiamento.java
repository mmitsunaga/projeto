package br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.RecursoSecundarioParteNe;
import br.gov.go.tj.projudi.ne.VotanteTipoNe;
import br.gov.go.tj.projudi.ps.AudienciaPs;
import br.gov.go.tj.projudi.sessaoVirtual.votacao.VotacaoUtils;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class FinalizacaoAdiamento extends FinalizacaoExtratoAta {

	public FinalizacaoAdiamento(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt,
			FabricaConexao fabricaConexao) {
		super(audienciaMovimentacaoDt, usuarioDt, logDt, fabricaConexao);
	}
	
	@Override
	public void make() throws Exception {
		List<MovimentacaoDt> movimentacoes = new ArrayList<MovimentacaoDt>();
        AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
        AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
        
		int codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());	     
        
        boolean wasVotacaoIniciada = audienciaProcessoNe.verificarVotacaoIniciada(audienciaProcessoDt.getId(), obFabricaConexao);
        
        setarTipoMovimentacao(codigoStatus); 
        
        // Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
		ProcessoDt processoDt = processoNe.consultarIdCompleto(getIdProcesso(audienciaProcessoDt));
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
		
        MovimentacaoDt movimentacaoDt = null;
		AudienciaProcessoDt audienciaProcessoNovaDt = null;
        String idAudienciaProcesso = audienciaProcessoDt.getId();

        AudienciaDt audienciaNovaDt = null;
        
		if (audienciaMovimentacaoDt.isAlteracaoExtratoAta()) {
			audienciaNovaDt = audienciaDt;
			audienciaProcessoNovaDt = audienciaDt.getAudienciaProcessoDt();
		} else {
			votoNe.finalizarPendenciasVotacao(idAudienciaProcesso, usuarioDt, obFabricaConexao, PendenciaTipoDt.PROCLAMACAO_VOTO, PendenciaTipoDt.ADIAR_JULGAMENTO);

			//Obtem a nova sessão...
			audienciaNovaDt = buscarProximaAudiencia(audienciaDt, processoDt);
			
			// Cria a nova Audiência Processo
			audienciaProcessoNovaDt = audienciaNe.obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaNovaDt, logDt, obFabricaConexao);
			idAudienciaProcesso = audienciaProcessoNovaDt.getId();
			
			votoNe.finalizarPendenciasVotacao(idAudienciaProcesso, usuarioDt, obFabricaConexao, PendenciaTipoDt.PROCLAMACAO_VOTO, PendenciaTipoDt.ADIAR_JULGAMENTO);
		}
		
		// Gera a movimentação no Processo
		if(codigoStatus == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL) {
			//mrbatista - 28/08/2020 - 14:22 finalizando pendencias de deferimento, pedidos S.O.
			votoNe.finalizarPendenciasVotacao(idAudienciaProcesso, usuarioDt, obFabricaConexao, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
			movimentacaoDt = audienciaNe.gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), true, audienciaNovaDt, logDt, obFabricaConexao);									
		} else {
			movimentacaoDt = audienciaNe.gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), audienciaNovaDt, logDt, obFabricaConexao);									
		}
					
		String Id_ArquivoAta = tratarArquivos();
		
		//Vincula os arquivos, dependendo do tipo da movimentação
		// Vincula o arquivo iserido á Audiência Processo
		audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoNovaDt, "",
				"", "", Id_ArquivoAta, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(),
				audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada(), "", "", "", logDt, obFabricaConexao);
		pendenciaNe.finalizarPendenciasAdiarJulgamento(idAudienciaProcesso, usuarioDt, obFabricaConexao);

		// lrcampos 06/01/2020 16:15 - Finaliza pelo idAudienciaProcesso e não idProcesso.
		// Apaga pendencias de aguardando voto e para conhecimento
		List<PendenciaDt> pendencias = pendenciaNe.consultarPendenciasProcessoPorListaTipo(
				audienciaProcessoDt.getId(), obFabricaConexao, PENDENCIA_TIPO_FINALIZAR_ADIAMENTO);

		if (pendencias != null && !pendencias.isEmpty()) {
			for (PendenciaDt pendenciaDt : pendencias) {
				Funcoes.preencheUsuarioLog(pendenciaDt, usuarioDt);

				if(StringUtils.isEmpty(pendenciaDt.getDataFim()))
					pendenciaDt.setDataFim(Funcoes.DataHora(new Date()));

				if(StringUtils.isEmpty(pendenciaDt.getDataVisto()))
					pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));
				
				pendenciaNe.finalizar(pendenciaDt, usuarioDt, obFabricaConexao);
			}
		}

		// Cria pendencias de julgamento adiado
		String idProcesso = getIdProcesso(audienciaProcessoDt);
		List<String> votantesMp = votoNe.consultarIdsIntegrantesSessaoPorTipo(
				audienciaProcessoDt.getId(),
				getListaVotantesTiposGerarPendencia(wasVotacaoIniciada));

		// jvosantos - 16/12/2019 15:29 - Salvar as pendências na AUDI_PROC_PEND
		List<String> idsPend = new ArrayList<String>();
		for (String id : votantesMp) {
			PendenciaDt pendenciaDt = pendenciaNe.gerarPendenciaAdiadoPeloRelator(null, id, usuarioDt, idProcesso, obFabricaConexao, audienciaMovimentacaoDt.getListaArquivos());
			if (pendenciaDt != null)
				idsPend.add(pendenciaDt.getId());
		}

		if (!idsPend.isEmpty())
			audienciaProcessoPendenciaNe.salvar(idsPend, audienciaDt.getAudienciaProcessoDt().getId(), obFabricaConexao);

		operacoesGerais(movimentacoes, processoDt, movimentacaoDt);
		
		// NOTE(jvosantos): Possível código morto, como sempre copiamos a pendência de voto, sempre existirá e não será gerada uma nova
		// Se for voto vencido iremos gerar uma conclusão do tipo voto/ementa para o desembargador redator poder pré-analisar
		audienciaNe.gerarPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(),
				movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(),
				audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe,
				usuarioDt, obFabricaConexao, null, audienciaDt.getAudienciaProcessoDt().getId());

		if(audienciaNovaDt.isVirtual())
			finalizarPendenciasVotoEmenta(audienciaDt);

        // Deve ser a última ação, caso aconteça algum erro antes, não é chamada
        realizarPublicacoes(processoDt, audienciaMovimentacaoDt.getListaArquivos());
	}

	protected List<PendenciaArquivoDt> getListPendenciaArquivoDtParaCopiar(PendenciaDt pendencia) throws Exception {
		List<PendenciaArquivoDt> pendArqCopiar = pendenciaArquivoNe.consultarPendencia(pendencia, true, obFabricaConexao); // jvosantos - 26/12/2019 15:25 - Buscar o relacionamento de pendencia com arquivos 
		pendArqCopiar = pendArqCopiar.stream().filter(x -> StringUtils.isEmpty(x.getArquivoDt().getUsuarioAssinador())).collect(Collectors.toList());
		return pendArqCopiar;
	}

	protected AudienciaDt buscarProximaAudiencia(AudienciaDt audienciaDt, ProcessoDt processoDt)
			throws MensagemException, Exception {
		return Optional.ofNullable(StringUtils.isEmpty(audienciaMovimentacaoDt.getIdProximaAudiencia()) ?
				audienciaPs.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), true) :
					audienciaPs.consultarId(audienciaMovimentacaoDt.getIdProximaAudiencia()))
				.orElseThrow(() -> new MensagemException("Não foi localizada uma sessão aberta com data posterior."));
	}

	protected void setarTipoMovimentacao(int codigoStatus) {
		if (Arrays.asList(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO, AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL).contains(codigoStatus))
        {
        	audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("4");
        } 
        else if (codigoStatus == AudienciaProcessoStatusDt.JULGAMENTO_INICIADO)
        {
        	audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("3");
        }
	}

}
