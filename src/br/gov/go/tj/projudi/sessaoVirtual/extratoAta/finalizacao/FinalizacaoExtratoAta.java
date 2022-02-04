package br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoPendenciaNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoStatusNe;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaNe;
import br.gov.go.tj.projudi.ne.VotoNe;
import br.gov.go.tj.projudi.ps.AudienciaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public abstract class FinalizacaoExtratoAta {

	protected static final List<Integer> VOTANTE_TIPO_GERAR_PENDENCIA_VOTACAO_NAO_INICIADA = Arrays
			.asList(VotanteTipoDt.RELATOR);
	
	protected static final List<Integer> VOTANTE_TIPO_GERAR_PENDENCIA_VOTACAO_INICIADA = Stream.concat(
			Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.VOTANTE, VotanteTipoDt.PRESIDENTE_CAMARA,VotanteTipoDt.PRESIDENTE_SESSAO).stream(),
			VOTANTE_TIPO_GERAR_PENDENCIA_VOTACAO_NAO_INICIADA.stream())
			.collect(Collectors.toList());

	protected static final Integer[] PENDENCIA_TIPO_FINALIZAR = {
				PendenciaTipoDt.VERIFICAR_IMPEDIMENTO,
				PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES,
				PendenciaTipoDt.VOTO_SESSAO,
				PendenciaTipoDt.PROCLAMACAO_VOTO,
				PendenciaTipoDt.SESSAO_CONHECIMENTO,
				PendenciaTipoDt.PEDIDO_VISTA_SESSAO,
				PendenciaTipoDt.ADIAR_JULGAMENTO
			};

	protected static final Integer[] PENDENCIA_TIPO_FINALIZAR_ADIAMENTO = {
				PendenciaTipoDt.VOTO_SESSAO,
				PendenciaTipoDt.SESSAO_CONHECIMENTO,
				PendenciaTipoDt.VERIFICAR_IMPEDIMENTO,
				PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_MP,
				PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO,
				PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA,
				PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL,
				PendenciaTipoDt.CONCLUSO_EMENTA,
				PendenciaTipoDt.APRECIADOS,
				PendenciaTipoDt.RESULTADO_VOTACAO
			};
	
	protected MovimentacaoArquivoNe movimentacaoArquivoNe;
	protected VotoNe votoNe;
	protected AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe;
	protected PendenciaArquivoNe pendenciaArquivoNe;
	protected PendenciaNe pendenciaNe;
	protected AudienciaProcessoNe audienciaProcessoNe;
	protected PendenciaResponsavelNe pendenciaResponsavelNe;
	protected ArquivoNe arquivoNe;
	protected AudienciaNe audienciaNe;
	
	protected AudienciaMovimentacaoDt audienciaMovimentacaoDt;
	protected FabricaConexao obFabricaConexao;
	protected UsuarioDt usuarioDt;
	protected LogDt logDt;
	protected ProcessoNe processoNe;

	protected AudienciaPs audienciaPs;

	public FinalizacaoExtratoAta(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao fabricaConexao) {
		obFabricaConexao = fabricaConexao;
		
		movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		votoNe = new VotoNe();
		pendenciaArquivoNe = new PendenciaArquivoNe();
		audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		pendenciaNe = new PendenciaNe();
		audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaNe = new AudienciaNe();
		pendenciaResponsavelNe = new PendenciaResponsavelNe();
		arquivoNe = new ArquivoNe();
		processoNe = new ProcessoNe();
		
		audienciaPs = new AudienciaPs(obFabricaConexao.getConexao());
		
		this.audienciaMovimentacaoDt = audienciaMovimentacaoDt;
		this.usuarioDt = usuarioDt;
		this.logDt = logDt;
	}

	public abstract void make() throws Exception;

	protected void operacoesGerais(List<MovimentacaoDt> movimentacoes, ProcessoDt processoDt,
			MovimentacaoDt movimentacaoDt) throws Exception, MensagemException {
		movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
		movimentacoes.add(movimentacaoDt);
		
		// Salvando vínculo entre movimentação e arquivos inseridos
		// jvosantos - 04/06/2019 09:50 - Vincula arquivos as movimentações, caso exista.
		movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(audienciaMovimentacaoDt.getListaArquivos(), movimentacaoDt.getId(), processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null, logDt, obFabricaConexao);			

		// Salvando pendências da movimentação
		if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) 
			pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, audienciaMovimentacaoDt.getListaArquivos(), usuarioDt, null, logDt, obFabricaConexao);			

		// Atualiza Classificador processo
		if (StringUtils.isNotEmpty(audienciaMovimentacaoDt.getId_Classificador()))
			processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);			

//		// Gera recibo para arquivos de movimentações
//		movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(audienciaMovimentacaoDt.getListaArquivos(), movimentacoes, obFabricaConexao);
		
        if (!processoDt.isSigiloso() && audienciaMovimentacaoDt.isVerificarProcesso()) {
			pendenciaNe.gerarPendenciaVerificarProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoDt.getId(), audienciaMovimentacaoDt.getListaArquivos(), null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
		}
	}

	protected void finalizarPendenciasVotoEmenta(AudienciaDt audienciaDt) throws Exception {
		// lrcampos 04/03/2020 11:53  - Refatorado o método de finalizar Pendencias da votaçao.
        // jvosantos - 05/12/2019 12:03 - Finalizar pendencias de minuta e ementa
        votoNe.finalizarPendenciasVotacao(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getId(), usuarioDt, obFabricaConexao,
				PendenciaTipoDt.CONCLUSO_EMENTA, PendenciaTipoDt.CONCLUSO_VOTO);
	}

	protected String tratarArquivos() throws Exception {
		// Salva arquivos inseridos
		if (audienciaMovimentacaoDt.getListaArquivos() != null && !audienciaMovimentacaoDt.getListaArquivos().isEmpty()) {
			movimentacaoArquivoNe.inserirArquivos(audienciaMovimentacaoDt.getListaArquivos(), logDt, obFabricaConexao);
			// Salvando vínculos do arquivo de Ata com a Audiência Processo
			return ((ArquivoDt)(audienciaMovimentacaoDt.getListaArquivos().get(0))).getId();										
		}
		return StringUtils.EMPTY;
	}

	protected String getIdProcesso(AudienciaProcessoDt audienciaProcessoDt) {
		return Optional
				.ofNullable(audienciaProcessoDt.getProcessoDt())
				.map(ProcessoDt::getId)
				.orElse(audienciaProcessoDt.getId_Processo());
	}

	protected List<Integer> getListaVotantesTiposGerarPendencia(boolean wasVotacaoIniciada) {
		return wasVotacaoIniciada
				? VOTANTE_TIPO_GERAR_PENDENCIA_VOTACAO_INICIADA
				: VOTANTE_TIPO_GERAR_PENDENCIA_VOTACAO_NAO_INICIADA;
	}

	protected void realizarPublicacoes(ProcessoDt processoDt, List<ArquivoDt> arquivos) throws Exception {// Cria publicação
		if (processoDt.isSegredoJustica() || processoDt.isSigiloso()) return;
		
		Runnable runnable = () -> {
			try {
				pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt);
			} catch (Exception e) {
				try {
			     	new LogNe().salvarErro(new LogDt("PUBLICACAO_ES" , "", usuarioDt.getId(),
			     			usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Erro),
			     			"Arquivos: "+serializableToString((Serializable) arquivos)+"\nUsuarioDt: "+serializableToString(usuarioDt)+"\nLogDt: "+serializableToString(logDt)+"\n\n",
			     			e.getMessage()));
				} catch (IOException ioEx) {
					throw new RuntimeException("Erro tentando serializar dados.", ioEx);
				} catch (Exception e1) {
					throw new RuntimeException("Erro tentando salvar log.", e1);
				}
			}
		};
		
		Thread t = new Thread(runnable);
		t.start();
	}

	protected String serializableToString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject( o );
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}
}
