package br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.amazonaws.services.kms.model.InvalidArnException;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoStatusNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaNe;
import br.gov.go.tj.projudi.ps.AudienciaPs;
import br.gov.go.tj.projudi.sessaoVirtual.votacao.VotacaoUtils;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class FinalizacaoNormal extends FinalizacaoExtratoAta {

	public FinalizacaoNormal(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt,
			FabricaConexao fabricaConexao) {
		super(audienciaMovimentacaoDt, usuarioDt, logDt, fabricaConexao);
	}
	
	@Override
	public void make() throws Exception {
		List<ArquivoDt> arquivosVencidos = null;
		List<MovimentacaoDt> movimentacoes = new ArrayList<MovimentacaoDt>();
		ResultadoVotacaoSessao resultadoVotacao = null;
		Queue<VotoDt> ressalvas = new LinkedList<VotoDt>();
		
        AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
        AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
        
		List<VotoDt> votos = votoNe.consultarVotosSessaoComData(audienciaProcessoDt.getId(), obFabricaConexao);  // jvosantos - 10/10/2019 15:21 - Mover lista de votos para reutilizar
		
		System.out.println("[TIME] Antes de Resultado Votacao = "+System.currentTimeMillis());
		
		// jvosantos - 10/10/2019 15:22 - Tratamento de Pedido de Vista
		boolean temPedidoVista = votos.stream().map(VotoDt::getVotoCodigoInt).filter(x -> x == VotoTipoDt.PEDIDO_VISTA).findAny().isPresent();

		if (!temPedidoVista) {
			resultadoVotacao = VotacaoUtils.calculaResultado(votos);

			finalizarPendenciaResultadoVotacaoSecretario();
		}
		
		System.out.println("[TIME] Depois de Resultado Votacao = "+System.currentTimeMillis());

		System.out.println("[TIME] Antes de CORPO = "+System.currentTimeMillis());
		
		// Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
		ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
				
        MovimentacaoDt movimentacaoDt = null;
		MovimentacaoDt movimentacaoRelator = null;
        String idAudienciaProcesso = audienciaDt.getAudienciaProcessoDt().getId();		
					
		String Id_ArquivoAta = tratarArquivos();
		
		//Vincula os arquivos, dependendo do tipo da movimenta��o
		
		//Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia e inserir respons�veis pela audi�ncia
		
		votoNe.finalizarPendenciasVotacao(audienciaProcessoDt.getProcessoDt().getId(), audienciaProcessoDt.getId(), usuarioDt, obFabricaConexao, PENDENCIA_TIPO_FINALIZAR);
		movimentacaoDt = new MovimentacaoDt();
		movimentacaoDt.copiar(audienciaNe.movimentarAudienciaAnalistaSegundoGrau(audienciaDt, audienciaProcessoDt.getAudienciaProcessoStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), audienciaMovimentacaoDt.getAudienciaStatus() ,audienciaMovimentacaoDt.getAudienciaStatusCodigo(), Id_ArquivoAta, audienciaMovimentacaoDt.getId_ServentiaCargoPresidente(), audienciaMovimentacaoDt.getId_ServentiaCargoMp(), audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), usuarioDt, logDt, obFabricaConexao));

		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

		switch (resultadoVotacao) {
			case MAIORIA_DIVERGE:
				for(VotoDt voto : votos) {
					if(!StringUtils.equals(voto.getVotoTipoCodigo(), String.valueOf(VotoTipoDt.DIVERGE))) continue;
					
					audienciaMovimentacaoDt.setId_ServentiaCargoRedator(voto.getIdServentiaCargo());
					break;
				}
		
				arquivosVencidos = finalizacaoVotoVencido(audienciaDt, processoDt, idAudienciaProcesso, movimentacaoNe);
				break;
			case MAIORIA_RELATOR:
				movimentacaoRelator = finalizacaoMaioriaRelator(ressalvas, votos, audienciaDt, processoDt, movimentacaoNe);
				break;
			case UNANIMIDADE:
				finalizacaoUnanimidade(ressalvas, votos);
				break;
			default:
				throw new Exception("Resultado da vota��o n�o mapeado. Resultado = "+resultadoVotacao);
		}
	
		operacoesGerais(movimentacoes, processoDt, movimentacaoDt);
		
		List<ArquivoDt> arquivosPublicacao = null;
		
        // jvosantos - 20/11/2019 16:00 - N�o realizar esse if, caso seja voto vencido e esteja adiando
		// Se for voto vencido iremos gerar uma conclus�o do tipo voto/ementa para o desembargador redator poder pr�-analisar
        if (resultadoVotacao == ResultadoVotacaoSessao.MAIORIA_DIVERGE){
        	finalizacaoVotoVencido(arquivosVencidos, audienciaDt, movimentacaoDt, idAudienciaProcesso);
        } else {
	        arquivosPublicacao = finalizacaoSemVotoVencido(resultadoVotacao, ressalvas, audienciaDt, processoDt, movimentacaoDt,	movimentacaoRelator);
        }

        finalizarPendenciasVotoEmenta(audienciaDt);

        // Devem ser as �ltimas a��es, caso aconte�a algum erro antes, n�o � chamado
        realizarPublicacoes(processoDt, audienciaMovimentacaoDt.getListaArquivos());
        
        if(arquivosPublicacao != null && !arquivosPublicacao.isEmpty())
        	realizarPublicacoes(processoDt, arquivosPublicacao);
	}

	protected List<ArquivoDt> finalizacaoSemVotoVencido(ResultadoVotacaoSessao resultadoVotacao, Queue<VotoDt> ressalvas,
			AudienciaDt audienciaDt, ProcessoDt processoDt, MovimentacaoDt movimentacaoDt,
			MovimentacaoDt movimentacaoRelator) throws Exception, MensagemException {
		// Gerar pend�ncia de voto para o desembargador inserir o voto, caso n�o exista...
		// jvosantos - 11/12/2019 12:55 - Corre��o para julgamento iniciado
		List<ArquivoDt> arquivos = null;
		
		if(audienciaNe.isAudienciaVirtualIniciada(audienciaMovimentacaoDt.getAudienciaDt().getId())) {
			arquivos = salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnaliseDesembargadorVirtual(audienciaMovimentacaoDt, usuarioDt, resultadoVotacao, movimentacaoRelator, logDt, obFabricaConexao);
			
			// jvosantos - 14/08/2019 15:26 - Alterar a ordem da gera��o das movimenta��es, ressalvas por �ltimo no caso de Unanimidade
			if(resultadoVotacao == ResultadoVotacaoSessao.UNANIMIDADE && !ressalvas.isEmpty())
				audienciaNe.salvarRessalvas(ressalvas, audienciaDt, processoDt, usuarioDt, logDt, obFabricaConexao);
		}
		
		return arquivos;
	}

	protected void finalizacaoVotoVencido(List arquivosVencidos, AudienciaDt audienciaDt, MovimentacaoDt movimentacaoDt,
			String idAudienciaProcesso) throws Exception {
		// Finalizar a pend�ncia de ementa do desembargador com voto vencido
		audienciaNe.finalizarPendenciaEmentaRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), pendenciaNe, usuarioDt, audienciaDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
		// jvosantos - 04/06/2019 09:50 - Prote��o para n�o executar o codigo caso seja o novo fluxo
		if(arquivosVencidos == null || arquivosVencidos.isEmpty()) {
			// Alterar pend�ncia de voto para o desembargador inserir o voto vencido
			audienciaNe.gerarPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), true, pendenciaNe, usuarioDt, obFabricaConexao, null, idAudienciaProcesso);
			// Gerar pend�ncia de voto para o desembargador inserir o voto vencedor
			audienciaNe.gerarPendenciaVoto(audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, null, idAudienciaProcesso);					
		}
	}
	
	protected List<ArquivoDt> finalizacaoVotoVencido(AudienciaDt audienciaDt, ProcessoDt processoDt,
			String idAudienciaProcesso, MovimentacaoNe movimentacaoNe) throws Exception {
		List<ArquivoDt> arquivosVencidos;
		List<ArquivoDt> arquivosRedator;
		MovimentacaoDt movimentacaoVenciada;
		MovimentacaoDt movimentacaoRes;
		String idStatus = votoNe.consultarIdStatusVencidoProclamacao(idAudienciaProcesso);
		String idPendencia = votoNe.consultarIdProclamacao(idAudienciaProcesso);
		String codigoStatusAud = null;
		
		if(idStatus != null) {
			AudienciaProcessoStatusNe audienciaProcessoStatusNe = new AudienciaProcessoStatusNe();
			codigoStatusAud = audienciaProcessoStatusNe.consultarId(idStatus).getAudienciaProcessoStatusCodigo();
			
			// jvosantos - 26/11/2019 14:19 - Alterar o USU_REALIZADOR das movimenta��es.
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			
			// jvosantos - 08/07/2019 15:39 - Adicionar complemento e altera��o na ordem das inser��es.
			movimentacaoRes = new MovimentacaoDt();
			String idProcesso = audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId();
			movimentacaoRes.copiar(movimentacaoNe.gerarMovimentacaoSessao(idProcesso, usuarioServentiaNe.consultarIdUsuarioServentiaPorIdServentiaCargo(audienciaMovimentacaoDt.getId_ServentiaCargoRedator()), "Voto Prevalecente", false, new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), Integer.parseInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo()), obFabricaConexao));

			movimentacaoVenciada = new MovimentacaoDt();						
			movimentacaoVenciada.copiar(movimentacaoNe.gerarMovimentacaoSessao(idProcesso, usuarioServentiaNe.consultarIdUsuarioServentiaPorIdServentiaCargo(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_ServentiaCargo()), "Voto Vencido", false, new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), Integer.parseInt(codigoStatusAud), obFabricaConexao));
			
			String idPendenciaVoto = audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator();

			arquivosVencidos = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(idPendencia);
			arquivosRedator = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(idPendenciaVoto);
			
			if(CollectionUtils.isEmpty(arquivosRedator)) {
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaNe.consultarId(idPendenciaVoto), usuarioDt, obFabricaConexao);
				arquivosRedator = Optional.ofNullable(pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(idPendenciaVoto, obFabricaConexao))
						.orElse(pendenciaArquivoNe.consultarArquivosPendencia(idPendenciaVoto));
			}
			
			// jvosantos - 12/08/2019 16:40 - Quando for voto vencido, salva apenas o arquivo de Voto na movimenta��o
			arquivosVencidos = arquivosVencidos.stream().filter(x -> !StringUtils.equals(x.getArquivoTipoCodigo(), String.valueOf(ArquivoTipoDt.EMENTA))).collect(Collectors.toList());

			validaArquivos(arquivosVencidos);
			validaArquivos(arquivosRedator);
			
			movimentacaoArquivoNe.inserirArquivos(arquivosVencidos, logDt, obFabricaConexao);
			movimentacaoArquivoNe.inserirArquivos(arquivosRedator, logDt, obFabricaConexao);
			
			String visibilidadeMovimentacaoArquivo = processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null;
			
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosVencidos, movimentacaoVenciada.getId(), visibilidadeMovimentacaoArquivo, logDt, obFabricaConexao);
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosRedator, movimentacaoRes.getId(), visibilidadeMovimentacaoArquivo, logDt, obFabricaConexao);
			
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivosVencidos, Arrays.asList(movimentacaoVenciada), obFabricaConexao);
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivosRedator, Arrays.asList(movimentacaoRes), obFabricaConexao);

			AudienciaProcessoDt audiProc = audienciaProcessoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getId());
			
			audiProc.setDataMovimentacao(Funcoes.DataHora(new Date()));

			audiProc.setId_Processo(idProcesso);
			Funcoes.preencheUsuarioLog(audiProc, usuarioDt);
			
			audiProc.setAudienciaProcessoStatus(audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigoAnalista());
			audienciaProcessoNe.salvar(audiProc, obFabricaConexao);
			audienciaProcessoNe.alterarStatusAudiencia(audiProc, Integer.parseInt(audienciaProcessoStatusNe.consultarAudienciaProcessoStatusPorCodigo(audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigoAnalista()).getId()), new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), obFabricaConexao);
		}else
			throw new Exception("N�o encontrou o voto do relator");
		return arquivosVencidos;
	}

	private void validaArquivos(List<ArquivoDt> arquivos) throws Exception {
		if(CollectionUtils.isEmpty(arquivos))
			throw new Exception("Lista de arquivos obrigat�rios est� vazia.");
	}

	protected MovimentacaoDt finalizacaoMaioriaRelator(Queue<VotoDt> ressalvas, List<VotoDt> votos,
			AudienciaDt audienciaDt, ProcessoDt processoDt, MovimentacaoNe movimentacaoNe) throws Exception {
		MovimentacaoDt movimentacaoRelator;
		
		ServentiaCargoDt servCargoRelator = new ServentiaCargoNe().consultarId(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo());

		movimentacaoRelator = new MovimentacaoDt();
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		movimentacaoRelator.copiar(movimentacaoNe.gerarMovimentacaoSessao(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), usuarioServentiaNe.consultarIdUsuarioServentiaPorIdServentiaCargo(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_ServentiaCargo()), "Voto Prevalecente" + servCargoRelator.getNomeUsuario(), false, new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), Integer.parseInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo()), obFabricaConexao));
		
		votos = audienciaNe.ordenarVotosPorData(votos);
		
		for(VotoDt v : votos) {   
			switch (v.getVotoCodigoInt()) {
			case VotoTipoDt.DIVERGE:
				MovimentacaoDt movimentacaoDivergencia = movimentacaoNe.gerarMovimentacao(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), MovimentacaoTipoDt.DECISAO, usuarioServentiaNe.consultarIdUsuarioServentiaPorIdServentiaCargo(v.getIdServentiaCargo()), "Voto Divergente "+v.getNomeVotante(), new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), obFabricaConexao);						
				List<ArquivoDt> arquivosDivergencia = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(v.getIdPendencia());

				movimentacaoArquivoNe.inserirArquivos(arquivosDivergencia, logDt, obFabricaConexao);
				
				movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosDivergencia, movimentacaoDivergencia.getId(), processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null , logDt, obFabricaConexao);
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivosDivergencia, Arrays.asList(movimentacaoDivergencia), obFabricaConexao);
				break;
				
			case VotoTipoDt.ACOMPANHA_RELATOR_RESSALVA:
				ressalvas.add(v);
				break;
			}
		}
		
		if(!ressalvas.isEmpty()) audienciaNe.salvarRessalvas(ressalvas, audienciaDt, processoDt, usuarioDt, logDt, obFabricaConexao);
		
		int idAudienciaProcessoStatus = getIdAudienciaProcessoStatus();
		
		audienciaProcessoNe.alterarStatusAudiencia(audienciaDt.getAudienciaProcessoDt(), idAudienciaProcessoStatus, logDt, obFabricaConexao);
		
		AudienciaProcessoDt audiProc = audienciaProcessoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getId());
		
		audiProc.setDataMovimentacao(Funcoes.DataHora(new Date()));
		
		Funcoes.preencheUsuarioLog(audiProc, usuarioDt);
		
		audiProc.setAudienciaProcessoStatusCodigo(audienciaMovimentacaoDt.getAudienciaStatusCodigo());
		audiProc.setId_AudienciaProcessoStatus(String.valueOf(idAudienciaProcessoStatus));
		
		audienciaProcessoNe.salvar(audiProc, obFabricaConexao);
		return movimentacaoRelator;
	}

	protected int getIdAudienciaProcessoStatus() throws Exception {
		return Integer.parseInt((new AudienciaProcessoStatusNe()).consultarAudienciaProcessoStatusPorCodigo(audienciaMovimentacaoDt.getAudienciaStatusCodigo()).getId());
	}

	protected void finalizarPendenciaResultadoVotacaoSecretario() throws Exception {
		List<PendenciaDt> pendenciaResultadoVotacaoSecretario = pendenciaNe.consultarPendenciasAudienciaProcessoPorListaTipo(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), obFabricaConexao, audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId_Serventia(), usuarioDt.getId_Serventia(), PendenciaTipoDt.RESULTADO_VOTACAO, PendenciaTipoDt.RESULTADO_UNANIME);		
		if(pendenciaResultadoVotacaoSecretario != null && !pendenciaResultadoVotacaoSecretario.isEmpty()) {
			pendenciaResultadoVotacaoSecretario.forEach(x -> {
				try {
					x.setDataFim(Funcoes.DataHora(new Date()));
					
					x.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_DESCARTADA));
					
					//lrcampos 20/02/2020 17:14 Incluindo data visto caso n�o tenha.
					if(StringUtils.isEmpty(x.getDataVisto()))
						x.setDataVisto(Funcoes.DataHora(new Date()));
					
					pendenciaNe.finalizar(x, usuarioDt, obFabricaConexao);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	protected void finalizacaoUnanimidade(Queue<VotoDt> ressalvas, List<VotoDt> votos) {
		// jvosantos - 12/08/2019 17:11 - Gera movimenta��o de Ressalvas quando resultado for Unanimidade
		votos = audienciaNe.ordenarVotosPorData(votos);

		ressalvas.addAll(
				votos.stream().filter(v -> StringUtils.equals(v.getIdVotoTipo(), String.valueOf(3)))
						.collect(Collectors.toList()));
	}
	

	//jvosantos - 08/07/2019 15:37 - Adicionar argumentos de resultado da vota��o e movimenta��o, para n�o gerar movimenta��o errada
	public List<ArquivoDt> salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnaliseDesembargadorVirtual(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, ResultadoVotacaoSessao resultadoVotacao, MovimentacaoDt movimentacaoRel, LogDt logDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		List<MovimentacaoDt> movimentacoes = new ArrayList<MovimentacaoDt>(); // jvosantos - 15/10/2019 15:03 - Tipar lista
		
		AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
		
		// Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
		ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
		
		//Consulta Voto 2� Grau
		PendenciaDt pendenciaMinuta = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator());
		
		//Consulta Ementa 2� Grau
		PendenciaDt pendenciaDtEmenta = null;
		if (audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta() != null)
			pendenciaDtEmenta = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId_Pendencia());
		
		String idUltimaFinalizacao = votoNe.consultarIdUltimaFinalizacao(processoDt.getId(), obFabricaConexao);
		PendenciaDt pendenciaFinalizacao = pendenciaNe.consultarFinalizadaId(idUltimaFinalizacao);
		List<PendenciaArquivoDt> pendenciaArquivos = null;
		if(pendenciaFinalizacao != null) {
			LocalDateTime dataFinalizacao = Funcoes.parseData(pendenciaFinalizacao.getDataInicio());
			LocalDateTime dataMinuta = Funcoes.parseData(pendenciaMinuta.getDataInicio());
			if(dataFinalizacao.isAfter(dataMinuta)) {
				pendenciaArquivos = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(pendenciaFinalizacao, null, true, true, obFabricaConexao);
			}
		}
		// jvosantos - 10/10/2019 16:07 - Verificar se lista esta vazia
		if(pendenciaArquivos == null || pendenciaArquivos.isEmpty()) {
			pendenciaArquivos = pendenciaArquivoNe.consultarArquivosPendencia(pendenciaMinuta, null, true, true, obFabricaConexao);
		}
		if(CollectionUtils.isEmpty(pendenciaArquivos)) { // jvosantos - 30/08/2019 18:21 - Adicionando exception
			throw new Exception("N�o foram encontrados arquivos para pend�ncia de Minuta ou Finaliza��o (ID_PEND_FINALIZACAO = "+ (pendenciaFinalizacao != null ? pendenciaFinalizacao.getId() : "NULL") + ", ID_PEND_MINUTA = " + (pendenciaMinuta != null ? pendenciaMinuta.getId() : "NULL") + ")"); // jvosantos - 10/10/2019 16:07 - Melhorar mensagem de erro
		}
		List<ArquivoDt> arquivos = pendenciaArquivos.stream().map(PendenciaArquivoDt::getArquivoDt).collect(Collectors.toList());
		ArquivoDt arquivoDtEmenta = arquivos.stream().filter(arquivo -> Funcoes.StringToInt(arquivo.getArquivoTipoCodigo()) == ArquivoTipoDt.EMENTA).findAny().orElse(null);
		
		// Salva arquivos inseridos
		if (arquivos != null && arquivos.size() > 0) {
			movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
		}
		
		AudienciaProcessoStatusDt statusAudienciaTemp = audienciaProcessoNe.consultarStatusAudienciaTemp(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId());
		
		
		// jvosantos - 15/10/2019 15:06 - S� gera a movimenta��o se n�o for movimenta��o de julgamento iniciado
		// jvosantos - 08/07/2019 15:37 - S� gera a movimenta��o se o resultado n�o for maioria_relator (que gera a movimenta��o no m�todo anterior)
		MovimentacaoDt movimentacaoDt = movimentacaoRel;
		if((resultadoVotacao == null || resultadoVotacao != ResultadoVotacaoSessao.MAIORIA_RELATOR) && !audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada()) {
			// Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia
			// e inserir respons�veis pela audi�ncia
			movimentacaoDt = audienciaNe.movimentarAudiencia(audienciaDt, statusAudienciaTemp.getAudienciaProcessoStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
			// jvosantos - 15/10/2019 15:09 - Mover c�digo para dentro do if
			movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
			movimentacoes.add(movimentacaoDt);
		}
		
		//Fechar pendencia do Tipo Voto 2� Grau
		if (pendenciaMinuta != null){
			pendenciaMinuta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			pendenciaNe.fecharPendencia(pendenciaMinuta, usuarioDt, obFabricaConexao);
		}
		
		//Fechar pendencia do Tipo Ementa 2� Grau
		if (pendenciaDtEmenta != null){
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);
		}

		// Salvando v�nculo entre movimenta��o e arquivos inseridos
		if (arquivos != null && arquivos.size() > 0){
			String visibilidade = processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null;
			
			if(movimentacaoDt != null)
				movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);
			
			if (arquivoDtEmenta != null) {
				AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao()); // jvosantos - 11/12/2019 14:54 - Evitar instanciar um novo PS para cara Arquivo
				// Iremos incluir o v�nculo entre os arquivos de ac�rd�o com o arquivo de ementa...
				for (int i=0; i < arquivos.size(); i++){
					ArquivoDt arquivoDt = arquivos.get(i); // jvosantos - 15/10/2019 15:10 - Remover cast desnecess�rio			
					if (arquivoDt != null && !arquivoDtEmenta.getId().trim().equalsIgnoreCase(arquivoDt.getId().trim())){
						obPersistencia.vincularEmentaAcordaoSegundoGrau(audienciaDt.getAudienciaProcessoDt().getId(), arquivoDtEmenta.getId(), arquivoDt.getId());
					}
				}
				
			}					
		}

		// jvosantos - 21/11/2019 15:50 - Adicionar valida��o para corrigir NullPointer no Julgamento Iniciado
		// Salvando pend�ncias da movimenta��o
		if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null && audienciaMovimentacaoDt.getListaPendenciasGerar().size() > 0 && movimentacaoDt != null) {
			pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
		}

		// Atualiza Classificador processo
		if (audienciaMovimentacaoDt.getId_Classificador().length() > 0) {
			processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
		}

//		// Gera recibo para arquivos de movimenta��es
//		movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
		
        // Registra no processo o indicador de julgamento do m�rito do processo principal 
        if (audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal() != null && audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")){
			processoNe.registrarJulgamentoMeritoProcessoPrincipal(processoDt.getId(), true, obFabricaConexao);
		}
	    
		
		return arquivos;
	}
}
