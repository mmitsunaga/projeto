package br.gov.go.tj.projudi.ne;

import static br.gov.go.tj.utils.Funcoes.StringToInt;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AnaliseVotoSessaoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoDtGen;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssinarVotoSessaoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.ExtratoAtaDt;
import br.gov.go.tj.projudi.dt.FinalizacaoVotoSessaoDt;
import br.gov.go.tj.projudi.dt.JulgamentoAdiadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.SessaoVirtualPublicaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.strategy.geracao_voto.AnaliseErroMaterialVotoStrategy;
import br.gov.go.tj.projudi.ne.strategy.geracao_voto.GeracaoVotoStrategy;
import br.gov.go.tj.projudi.ps.AudienciaProcessoPs;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.projudi.sessaoVirtual.extratoAta.ExtratoAta;
import br.gov.go.tj.projudi.sessaoVirtual.extratoAta.ExtratoAtaJulgamentoIniciado;
import br.gov.go.tj.projudi.sessaoVirtual.utils.AssinaturaArquivoUtils;
import br.gov.go.tj.projudi.sessaoVirtual.votacao.VotacaoUtils;
import br.gov.go.tj.projudi.sessaoVirtual.voto.FiltroVotoTipo;
import br.gov.go.tj.projudi.types.PendenciaStatusTipo;
import br.gov.go.tj.projudi.types.PendenciaTipo;
import br.gov.go.tj.projudi.types.VotoTipo;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.Signer;

// jvosantos - 27/09/2019 14:40 - Corrigir formatação de arquivo
@SuppressWarnings("all")
public class VotoNe extends Negocio {

	public static final String SITUACAO_VOTO_AGUARDANDO_MANIFESTACAO = "Aguardando manifestação"; // jvosantos - 27/09/2019 14:41 - Extrair texto para constante

	public static final String SITUACAO_VOTO_PRAZO_EXPIRADO = SITUACAO_VOTO_AGUARDANDO_MANIFESTACAO+" - Prazo Expirado"; // jvosantos - 27/09/2019 14:41 - Extrair texto para constante
	
	private static final double RAZAO_VOTO_VOTANTE = 2.0 / 3.0;
	private static final long serialVersionUID = 6414492536119919673L;
	private static final int PRAZO_SESSAO_VIRTUAL_RENOVAR_DIAS = 6; // jvosantos - 05/09/2019 14:26 - Alteração do prazo conforme mensagem abaixo:
	public static final int PRAZO_SESSAO_VIRTUAL_DIAS = 4; // jvosantos - 05/09/2019 14:26 - Alteração do prazo conforme mensagem abaixo:
	/*
	 * Alteração do PRAZO_SESSAO_VIRTUAL_RENOVAR_DIAS de 7 para 6, conforme e-mail do Dr. Claudio:
	 * Alteração do PRAZO_SESSAO_VIRTUAL_DIAS de 5 para 4, conforme e-mail do Dr. Claudio:
	 * 
	 * 		Conforme discutido em contato telefônico o §1º do art. 6º da Res. TJGO n. 91/2018 prevê que 
	 * 		“a sessão virtual terá início às 10:00 horas do dia designado e terminará as 18:00 horas do quinto dia útil seguinte”,
	 * 		sendo certo que por não se tratar de prazo processual, não há falar em exclusão do dia de início do prazo e inclusão do dia do término.
	 * 
	 * 		Logo, deve o sistema incluir o dia de início da sessão no cômputo do prazo, de forma que seu encerramento ocorra no quinto dia útil
	 * 		após seu início. A TÍTULO DE EXEMPLO: se a sessão começa na segunda-feira (1º dia), deve encerrar-se na sexta-feira (5º dia).
	 */
	private static final Integer[] TIPOS_PENDENCIA_VOTACAO = new Integer[] {
			PendenciaTipoDt.VERIFICAR_IMPEDIMENTO,
			PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES,
			PendenciaTipoDt.VOTO_SESSAO,
			PendenciaTipoDt.PROCLAMACAO_VOTO,
			PendenciaTipoDt.SESSAO_CONHECIMENTO,
			PendenciaTipoDt.RETIRAR_PAUTA,
			PendenciaTipoDt.PEDIDO_VISTA_SESSAO,
			PendenciaTipoDt.ADIAR_JULGAMENTO,
			PendenciaTipoDt.RESULTADO_UNANIME,
			PendenciaTipoDt.RESULTADO_VOTACAO,
			PendenciaTipoDt.APRECIADOS, 
			PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO,
			PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA,
			PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL,
			PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_MP,
			PendenciaTipoDt.ADIADO_PELO_RELATOR,
			PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL // jvosantos - 03/03/2020 11:18 - Finalizar pendência de erro material

	};
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	public AnaliseVotoSessaoDt consultarAnaliseVoto(String idPendencia, UsuarioNe usuarioSessao) throws Exception {
		FabricaConexao fabricaConexao = null;
		AnaliseVotoSessaoDt analiseVoto = new AnaliseVotoSessaoDt();
		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe(); // jvosantos - 20/08/2019 11:36 - Consultar da tabela de ligação
		String idAudiProc = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();

		try {
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabricaConexao.getConexao());
			PendenciaDt pendencia = new PendenciaNe().consultarPendenciaId(idPendencia, usuarioSessao);
			
			idAudiProc = audienciaProcessoPendenciaNe.consultarPorIdPend(idPendencia);
			
			if(StringUtils.isEmpty(idAudiProc)) {
				System.out.println("[ATENCAO] - Não foi encontrado ID_AUDI_PROC para a PEND de Voto de ID = " + idPendencia);
			}
			
			VotoDt voto = ps.consultarIdPendencia(pendencia.getId());
			boolean isPreAnalise = true;
			if (voto == null) {
				voto = new VotoDt();
				isPreAnalise = false;
			}
			voto.setPendenciaDt(pendencia);

			AudienciaProcessoDt audiProc = StringUtils.isNotEmpty(idAudiProc)
					? audienciaProcessoNe.consultarIdCompleto(idAudiProc)
					: getAudienciaProcessoDtPeloIdProcesso(usuarioSessao.getUsuarioDt(), fabricaConexao,
					pendencia.getId_Processo());

			if(audiProc.getProcessoDt() == null) {
				audiProc.setProcessoDt(new ProcessoNe().consultarId(audiProc.getId_Processo(), fabricaConexao));
			}
			
			voto.setAudienciaProcessoDt(audiProc);
			
			if(voto.getAudienciaProcessoDt() == null) throw new Exception("Não foi encontrado AUDI_PROC para o processo \""+pendencia.getId_Processo()+"\" com estado A REALIZAR");
			
			voto
					.setOrdemVotante(
							ps
									.consultarOrdemVotante(
											Funcoes.getIdServentiaCargo(usuarioSessao.getUsuarioDt()),
											audiProc.getId()));
			ServentiaCargoDt serventiaCargoDt = new ServentiaCargoNe()
					.consultarId(getIdServentiaCargo(usuarioSessao.getUsuarioDt()));

			voto.setNomeVotante(serventiaCargoDt.getNomeUsuario());
			analiseVoto.setVoto(voto);
			List<VotoDt> votos = consultarVotosVotantesIncluindoObservacao(
					audiProc.getId(),
					usuarioSessao,
					fabricaConexao);

			analiseVoto.setVotos(votos);

			Map<String, PendenciaArquivoDt> arquivos = consultarArquivosAnalise(
					usuarioSessao,
					fabricaConexao,
					audiProc);
			analiseVoto.setVotoRelator(arquivos.get("voto"));
			analiseVoto.setEmentaRelator(arquivos.get("ementa"));
			analiseVoto.setNomeRelator(getNomeRelator(audiProc, fabricaConexao));

			consultarArquivosVoto(usuarioSessao, fabricaConexao, voto, isPreAnalise);

			// jvosantos - 09/07/2019 15:14 - Alterar tipo de pendencia de voto
			String idArquivoTipo = arquivoTipoNe.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.VOTO));
			if (idArquivoTipo != null) {
				analiseVoto.setArquivoTipo(arquivoTipoNe.consultarId(idArquivoTipo));
			}
		} catch(Exception e) {
			return null;
		} finally {
			fabricaConexao.fecharConexao();
		}

		return analiseVoto;
	}

	private String getServentiaCargo(UsuarioDt usuario) {
		return StringUtils.defaultIfEmpty(usuario.getId_ServentiaCargoUsuarioChefe(), usuario.getId_ServentiaCargo());
	}

	@Deprecated
	//movido para o Funcoes
	public LocalDateTime parseData(String data) {
		return LocalDateTime.parse(data.substring(0, 16), formatter);
	}

	private void consultarArquivosVoto(UsuarioNe usuarioSessao,
			FabricaConexao fabricaConexao,
			VotoDt voto,
			boolean isPreAnalise) throws Exception {
		if (isPreAnalise) {
			List<PendenciaArquivoDt> arquivosPendencia = new PendenciaArquivoNe()
					.consultarArquivosPendencia(voto.getPendenciaDt(), usuarioSessao, true, false, fabricaConexao);
			if (CollectionUtils.isNotEmpty(arquivosPendencia)) {
				ArquivoDt arquivoDt = arquivosPendencia
						.stream()
						.map(pendenciaArquivo -> pendenciaArquivo.getArquivoDt())
						.findFirst()
						.get();
				arquivoDt = new ArquivoNe().consultarId(arquivoDt.getId(), fabricaConexao.getConexao());
				voto.setArquivo(arquivoDt);
			}
		}
	}

	private List<VotoDt> consultarObservacoes(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao fabricaConexao) throws Exception {
		VotoPs ps = new VotoPs(fabricaConexao.getConexao());
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		List<VotoDt> todosVotos = ps.consultarObservacoes(idAudienciaProcesso);
		for (VotoDt voto : todosVotos) {
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe
					.consultarId(voto.getIdServentiaCargo(), fabricaConexao);
			if (serventiaCargoDt.isMagistrado()) {
				voto.setNomeVotante("Des.(a) " + voto.getNomeVotante());
			}
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			PendenciaDt pendenciaDt = new PendenciaDt();
			pendenciaDt.setId(voto.getIdPendencia());
			List listaArquivos = pendenciaArquivoNe
					.consultarArquivosPendenciaComHash(pendenciaDt, true, false, usuario, fabricaConexao);   // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada
			if (CollectionUtils.isNotEmpty(listaArquivos)) {
				voto.setArquivo(((PendenciaArquivoDt) listaArquivos.get(0)).getArquivoDt());
			}
		}
		return todosVotos;
	}

	// jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto
	private List<VotoDt> consultarVotosVotantesIncluindoObservacao(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao fabricaConexao) throws Exception {
		return consultarVotosVotantesIncluindoObservacao(idAudienciaProcesso, usuario, fabricaConexao, false);
	}

	// jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto
	private List<VotoDt> consultarVotosVotantesIncluindoObservacao(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao fabricaConexao,
			boolean trazerProprioVoto) throws Exception {
		List<VotoDt> votosVotantes = consultarVotosVotantes(idAudienciaProcesso, usuario, fabricaConexao, trazerProprioVoto);
		votosVotantes.addAll(consultarObservacoes(idAudienciaProcesso, usuario, fabricaConexao));
		return votosVotantes;
	}

	// jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto
	private List<VotoDt> consultarVotosVotantes(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao fabricaConexao) throws Exception {
		return consultarVotosVotantes(idAudienciaProcesso, usuario, fabricaConexao, false);
	}

	// jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto
	private List<VotoDt> consultarVotosVotantes(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao fabricaConexao,
			boolean trazerProprioVoto) throws Exception {

		Stream<VotoDt> votos = consultarVotosVotantesIncluindoProprio(idAudienciaProcesso, usuario, fabricaConexao)
				.stream();
		if(!trazerProprioVoto)
			votos = votos.filter(voto -> !voto.getIdServentiaCargo().equals(usuario.getId_ServentiaCargo()));
		
		return votos.collect(Collectors.toList());
	}

	private List<VotoDt> consultarVotosVotantesIncluindoProprio(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao fabricaConexao) throws Exception {
		VotoPs ps = new VotoPs(fabricaConexao.getConexao());
		List<VotoDt> todosVotos = ps.consultarVotosIncluindoVazio(idAudienciaProcesso);
		for (int i = 0; i < todosVotos.size(); i++) {
			VotoDt voto = todosVotos.get(i);
			if (existeOutroVotoServentiaCargo(voto, todosVotos)) {
				todosVotos.remove(voto);
				i--;
			}
			setArquivoVoto(usuario, voto);
			if (voto.getVotoCodigoInt() == VotoTipoDt.PRAZO_EXPIRADO) {
				voto.setVotoTipo(voto.getVotoTipo());
			}
			voto.setNomeVotante(voto.getOrdemVotante() + "º Votante - Des.(a) " + voto.getNomeVotante());
		}
		return todosVotos;
	}

	private boolean existeOutroVotoServentiaCargo(VotoDt voto, List<VotoDt> todosVotos) {
		long count = todosVotos
				.stream()
				.filter(outro -> outro.getIdServentiaCargo().equals(voto.getIdServentiaCargo()))
				.count();
		return count > 1;
	}

	private void setArquivoVoto(UsuarioNe usuario, VotoDt voto) throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt.setId(voto.getIdPendencia());
		List listaArquivos = pendenciaArquivoNe
				.consultarArquivosPendenciaFinalizadaComHash(pendenciaDt, true, true, usuario);
		if (listaArquivos == null) {
			listaArquivos = pendenciaArquivoNe.consultarArquivosPendenciaComHash(pendenciaDt, true, true, usuario);
		}
		if (CollectionUtils.isNotEmpty(listaArquivos)) {
			voto.setArquivo(((PendenciaArquivoDt) listaArquivos.get(0)).getArquivoDt());
		}
	}

	public AudienciaProcessoDt getAudienciaProcessoDtPeloIdProcesso(UsuarioDt usuario, String idProcesso) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		AudienciaProcessoDt audiProc = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			audiProc = getAudienciaProcessoDtPeloIdProcesso(usuario, obFabricaConexao, idProcesso);

		} finally {
			obFabricaConexao.fecharConexao();

		}
		return audiProc;
	}
	
	private AudienciaProcessoDt getAudienciaProcessoDtPeloIdProcesso(UsuarioDt usuario,
			FabricaConexao obFabricaConexao,
			String idProcesso) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoDt audiProc = audienciaProcessoNe
				.consultarAudienciaProcessoPendente(idProcesso, usuario, obFabricaConexao);
		
		if(audiProc == null) return null;
		
		ProcessoNe processoNe = new ProcessoNe();
		AudienciaProcessoStatusDt statusAudienciaTemp = audienciaProcessoNe
				.consultarStatusAudienciaTemp(audiProc.getId());
		if(statusAudienciaTemp != null) {
			audiProc.setAudienciaProcessoStatusCodigoTemp(statusAudienciaTemp.getAudienciaProcessoStatusCodigo());
			audiProc.setAudienciaProcessoStatusTemp(statusAudienciaTemp.getAudienciaProcessoStatus());
		}
		audiProc.setProcessoDt(processoNe.consultarIdCompleto(idProcesso, obFabricaConexao));
		audiProc.getProcessoDt().setListaAdvogados(processoNe.consultarAdvogadosProcesso(idProcesso, obFabricaConexao));
		audiProc
				.getProcessoDt()
				.setServentiaCargoResponsavelDt(
						new ProcessoResponsavelNe().consultarRelator2Grau(idProcesso, obFabricaConexao));
		audiProc.setAudienciaDt(new AudienciaNe().consultarId(audiProc.getId_Audiencia(), obFabricaConexao));
		return audiProc;
	}

	private AudienciaProcessoDt getAudienciaProcessoDt(FabricaConexao fabrica, String idAudienciaProcesso)
			throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		AudienciaProcessoDt audiProc = audienciaProcessoNe.consultarIdCompleto(idAudienciaProcesso, fabrica);
		ProcessoDt processoDt = new ProcessoDt();
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		AudienciaProcessoStatusDt statusAudienciaTemp = audienciaProcessoNe
				.consultarStatusAudienciaTemp(audiProc.getId(), fabrica);
		if (statusAudienciaTemp != null) {
			audiProc.setAudienciaProcessoStatusCodigoTemp(statusAudienciaTemp.getAudienciaProcessoStatusCodigo());
		}
		ProcessoNe processoNe = new ProcessoNe();
		processoDt = processoNe.consultarIdCompleto(audiProc.getId_Processo(), fabrica);
		processoDt.setProcessoTipo(processoParteNe.consultaClasseProcessoIdAudiProc(idAudienciaProcesso));
		audiProc.setProcessoDt(processoDt);
		audiProc
				.getProcessoDt()
				.setListaAdvogados(processoNe.consultarAdvogadosProcesso(audiProc.getId_Processo(), fabrica));
		audiProc
				.getProcessoDt()
				.setServentiaCargoResponsavelDt(
						processoResponsavelNe.consultarRelator2Grau(audiProc.getId_Processo(), fabrica));
		audiProc.setAudienciaDt(new AudienciaNe().consultarId(audiProc.getId_Audiencia(), fabrica));
		return audiProc;
	}

	private Map<String, PendenciaArquivoDt> consultarArquivosAnalise(UsuarioNe usuarioSessao,
			FabricaConexao obFabricaConexao,
			AudienciaProcessoDt audienciaProcesso) throws Exception {
		// jvosantos - 17/10/2019 17:06 - Correção temporaria, não funciona com conexão. Por que?
		PendenciaArquivoNe pendArqNe = new PendenciaArquivoNe();
		// jvosantos - 28/11/2019 16:34 - Filtrar pelo ID da pendência de Ementa
		PendenciaArquivoDt ementa = pendArqNe
				.consultarEmentaDesembargador(
						audienciaProcesso.getId_ServentiaCargo(),
						audienciaProcesso.getId(),
						audienciaProcesso.getId_ProcessoTipo(),
						audienciaProcesso.getId_PendenciaEmentaRelator());
		if (ementa != null) {
			ementa.setHash(usuarioSessao.getCodigoHash(ementa.getId()));
		}

		// jvosantos - 28/11/2019 16:34 - Filtrar pelo ID da pendência de Voto
		PendenciaArquivoDt votoRelator = pendArqNe
				.consultarVotoDesembargador(
						audienciaProcesso.getId_ServentiaCargo(),
						audienciaProcesso.getId(),
						audienciaProcesso.getId_ProcessoTipo(),
						audienciaProcesso.getId_PendenciaVotoRelator(),
						null);
		if (votoRelator != null) {
			votoRelator.setHash(usuarioSessao.getCodigoHash(votoRelator.getId()));

		}
		Map<String, PendenciaArquivoDt> arquivos = new HashMap<>();
		arquivos.put("voto", votoRelator);
		arquivos.put("ementa", ementa);
		return arquivos;
	}

	public AssinarVotoSessaoDt consultarVotosAssinar(String[] idsPendencia) throws Exception {
		FabricaConexao fabricaConexao = null;
		AssinarVotoSessaoDt assinar = new AssinarVotoSessaoDt();
		StringBuilder nomes = new StringBuilder();
		StringBuilder conteudos = new StringBuilder();

		try {
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabricaConexao.getConexao());

			for (String id : idsPendencia) {
				List<ArquivoDt> arquivos = consultarArquivoVoto(fabricaConexao, id)
						.stream()
						.filter(
								(
										arquivo) -> !arquivo
												.getArquivoTipoCodigo()
												.equals(String.valueOf(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)))
						.collect(Collectors.toList());
				for (ArquivoDt arquivo : arquivos) {
					nomes
							.append(id)
							.append("-")
							.append(arquivo.getId())
							.append("-")
							.append(arquivo.getNomeArquivo())
							.append("__@---");
					conteudos.append(new String(arquivo.getConteudoSemAssinar())).append("__@---");
				}
			}
			assinar.setNomeArquivos(nomes.substring(0, nomes.length() - 6));
			assinar.setConteudoArquivos(conteudos.substring(0, conteudos.length() - 6));
			List<VotoSessaoLocalizarDt> lista = ps
					.consultarIdPendenciaMultiplo(idsPendencia)
					.stream()
					.map((voto) -> calcularPrazo(voto))
					.collect(Collectors.toList());

			assinar.setVotos(lista);
			return assinar;
		} finally {
			fabricaConexao.fecharConexao();
		}
	}

	private List<ArquivoDt> consultarArquivoVoto(FabricaConexao fabricaConexao, String id) throws Exception {
		ArquivoNe arquivoNe = new ArquivoNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		List<ArquivoDt> listaArquivos = pendenciaArquivoNe.consultarArquivosPendencia(id);
		List<ArquivoDt> retorno = new ArrayList<ArquivoDt>();
		if (CollectionUtils.isNotEmpty(listaArquivos)) {
			for (ArquivoDt arquivo : listaArquivos) {
				arquivo = arquivoNe.consultarId(arquivo.getId(), fabricaConexao.getConexao());
				retorno.add(arquivo);
			}
		}
		return retorno;
	}

	public List consultarListaStatusAudiencia(UsuarioNe usuario) throws Exception {
		return new AudienciaNe().consultarStatusAudiencia(usuario.getUsuarioDt().getServentiaSubtipoCodigo());
	}

	public String consultarDescricaoVotoTipo(String descricao, String posicao, List<Integer> codigoIgnorar)
			throws Exception {
		FabricaConexao obFabricaConexao = null;
		String retorno = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarDescricaoVotoTipo(descricao, posicao, codigoIgnorar);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return retorno;
	}

	public void salvarVotoSessao(VotoDt voto, UsuarioDt usuario) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			if (StringUtils.equals(voto.getPendenciaDt().getPendenciaStatusCodigo(),
					String.valueOf(PendenciaStatusDt.ID_CORRECAO))) {
				// jvosantos - 14/08/2019 15:13 - Apagar arquivos antigos de
				// voto, caso seja uma pendencia de Renovar ou Modificar
				// voto
				apagarArquivoVoto(voto, usuario, fabrica);
			}
			salvarArquivoVoto(voto, usuario, fabrica);

			inserirVotoDesativandoAntigo(voto, fabrica);
			finalizarPendenciaVoto(voto.getPendenciaDt(), voto, voto.getIdProcesso(), usuario, fabrica);

			fabrica.finalizarTransacao();

		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	// jvosantos - 14/08/2019 15:13 - Apagar arquivos de um voto
	private void apagarArquivoVoto(VotoDt voto, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		
		List<PendenciaArquivoDt> arquivosPendencia = pendenciaArquivoNe.consultarArquivosPendencia(voto.getPendenciaDt(), null, true, false, fabrica);

		if(arquivosPendencia == null) return;
		
		for(PendenciaArquivoDt pendArqDt : arquivosPendencia) {
			ArquivoDt arquivoDt = pendArqDt.getArquivoDt();
			
			pendArqDt.setIpComputadorLog(usuario.getIpComputadorLog());
			pendArqDt.setId_UsuarioLog(usuario.getId());
			
			arquivoDt.setIpComputadorLog(usuario.getIpComputadorLog());
			arquivoDt.setId_UsuarioLog(usuario.getId());
			
			pendenciaArquivoNe.excluirPJD(pendArqDt, fabrica);
			
			arquivoNe.excluir(arquivoDt, fabrica);
		}
	}

	// jvosantos - 04/06/2019 10:09 - Salvar voto do tipo proclamação na tabela de votos
	public void salvarVotoProclamacao(VotoDt voto, UsuarioDt usuario, FabricaConexao obFabrica) throws Exception {
		FabricaConexao fabrica = null;
		try {
			if(obFabrica == null) {
				fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				fabrica.iniciarTransacao();
			}else fabrica = obFabrica;
			
			inserirVotoDesativandoAntigo(voto, fabrica);

			if (obFabrica == null)
				fabrica.finalizarTransacao(); // mrbatista - 04/09/2019 10:00 - Ajuste conexão.
		} catch (Exception e) {
			if (obFabrica == null)
				fabrica.cancelarTransacao();
			throw e;
		} finally {
			if(obFabrica == null) fabrica.fecharConexao();
		}

	}

	private void finalizarVoto(VotoDt voto, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		if (voto.getVotoCodigoInt() == VotoTipoDt.PEDIDO_VISTA) {
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			List<Integer> listaTipos = new ArrayList<>();
			listaTipos.add(VotanteTipoDt.RELATOR);
			List<String> ids = votoPs.consultarIdIntegrantesSessaoPorTipo(voto.getIdAudienciaProcesso(), null);
			ids.remove(usuario.getId_ServentiaCargo());
			gerarPendenciaPedirVista(
					ids,
					voto,
					voto.getIdProcesso(),
					usuario, fabrica);
			finalizarPendenciaVinculadasProc(voto.getIdAudienciaProcesso(), usuario, fabrica); // jvosantos - 20/08/2019 16:54 - Alterar ID_PROC para ID_AUDI_PROC
		} else if ((voto.getVotoCodigoInt() == VotoTipoDt.IMPEDIDO || voto.getVotoCodigoInt() == VotoTipoDt.SUSPEICAO) && !isServentiaEspecial(voto.getAudienciaProcessoDt().getProcessoDt().getId_Serventia())) { // jvosantos - 17/09/2019 13:20 - Alterar o comportamento do voto de Impedimento para as serventias especiais
			gerarPedenciaVotoProximoVotante(voto, usuario, fabrica);
		}
	}

	public String verificarPrazoExpirado(String idAudienciaProcesso, UsuarioDt usuario) throws Exception {
		FabricaConexao fabrica = null;
		String id = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();

			AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarId(idAudienciaProcesso);

			// jvosantos - 04/10/2019 18:33 - Adicinar fabrica no método finalizarVotosPendentes
			finalizarVotosPendentesGerarFinalizacao(audienciaProcessoDt, usuario, fabrica);

			Optional<PendenciaDt> proclamacaoDecisaoJaCriada = pendenciaNe
					.consultarPendenciasAudienciaProcessoPorListaTipo(audienciaProcessoDt.getId(), fabrica,
							PendenciaTipoDt.PROCLAMACAO_VOTO)
					.stream().findFirst();
			
			if (proclamacaoDecisaoJaCriada.isPresent())
				id = proclamacaoDecisaoJaCriada.get().getId();
			else {
				PendenciaDt proclamarVoto = pendenciaNe.gerarPendenciaProclamarVoto(
						audienciaProcessoDt.getId_ServentiaCargo(), usuario, audienciaProcessoDt.getId_Processo(),
						fabrica);
				new AudienciaProcessoPendenciaNe().salvar(proclamarVoto.getId(), audienciaProcessoDt.getId(), fabrica);
				id = proclamarVoto.getId();
			}
			
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
		return id;
	}

	private void gerarPedenciaVotoProximoVotante(VotoDt voto, UsuarioDt usuario, FabricaConexao fabrica)
			throws Exception {
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoDt processo = Optional
				.ofNullable(voto.getAudienciaProcessoDt().getProcessoDt())
				.orElse(new ProcessoNe().consultarId(voto.getIdProcesso()));

		List<VotanteDt> integrantes = consultarIntegrantesSessaoPorTipo(
				voto.getIdAudienciaProcesso(),
				Arrays.asList(VotanteTipoDt.PRESIDENTE_SESSAO, VotanteTipoDt.RELATOR, VotanteTipoDt.VOTANTE));
		List<String> idIntegrantes = integrantes.stream()
				.map(VotanteDt::getIdServentiaCargo)
				.collect(Collectors.toList());
		
		List<ServentiaCargoDt> serventiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem = serventiaCargoNe
				.consultarServentiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem(processo.getId_Serventia(), fabrica);
		
		Optional<ServentiaCargoDt> usuarioImpedido = serventiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem.stream().filter(x -> StringUtils.equals(x.getId(), usuario.getId_ServentiaCargo())).findAny();

		if(usuarioImpedido.isPresent())
			Collections.rotate(serventiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem, -(usuarioImpedido.get().getOrdemTurmaJulgadora()));

		Optional<ServentiaCargoDt> votante = serventiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem
				.stream()
				.filter(possivel -> !idIntegrantes.contains(possivel.getId()))
				.findFirst();
		if (votante.isPresent()) {
			List<PendenciaDt> listaPendencias = pendenciaNe
					.consultarPendenciasAudienciaProcessoPorListaTipo(
							voto.getIdAudienciaProcesso(),
							fabrica,
							votante.get().getId(),
							PendenciaTipoDt.SESSAO_CONHECIMENTO);
			for (PendenciaDt pendenciaDt : listaPendencias) {
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario, PendenciaStatusDt.ID_NAO_CUMPRIDA, fabrica);
				excluirVotoIdPendencia(pendenciaDt.getId(), fabrica);
			}
			OptionalInt maiorOrdem = integrantes.stream()
					.mapToInt(VotanteDt::getOrdem)
					.filter(ordem -> ordem < 90)
					.max();
			int ordem = maiorOrdem.orElse(98) + 1;
			alterarFuncaoVotante(
					voto.getIdAudienciaProcesso(),
					votante.get().getId(),
					VotanteTipoDt.VOTANTE,
					ordem,
					fabrica);
			PendenciaDt pendenciaVotoSessaoVirtual = pendenciaNe
					.gerarPendenciaVotoSessaoVirtual(
							votante.get().getId(),
							usuario,
							voto.getIdProcesso(),
							voto.getPendenciaDt().getDataLimite(),
							voto.getAudienciaProcessoDt().getId(),
							fabrica);

			if (pendenciaVotoSessaoVirtual != null)
				audienciaProcessoPendenciaNe.salvar(pendenciaVotoSessaoVirtual.getId(), voto.getIdAudienciaProcesso(), fabrica);
		} else {
			// jvosantos - 03/03/2020 11:23 - Substituir para unificar chamadas
			finalizarTodasPendenciasVotacao(Optional.ofNullable(voto.getAudienciaProcessoDt())
					.orElse(new AudienciaProcessoNe().consultarId(voto.getIdAudienciaProcesso())), usuario, fabrica);
			PendenciaDt pendenciaVerificarVotantes = pendenciaNe.gerarPendenciaVerificarVotantes(processo, usuario, fabrica);

			if (pendenciaVerificarVotantes != null)
				audienciaProcessoPendenciaNe.salvar(pendenciaVerificarVotantes.getId(), voto.getIdAudienciaProcesso(), fabrica);
		}

	}

	public void alterarFuncaoVotante(String idAudienciaProcesso,
			String idServentiaCargo,
			int votanteTipoCodigo,
			int ordem,
			FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		votoPs.alterarFuncaoVotante(idAudienciaProcesso, idServentiaCargo, votanteTipoCodigo, ordem);
	}

	public void finalizarPendenciaVoto(PendenciaDt pendencia,
			VotoDt voto,
			String idProcesso,
			UsuarioDt usuario,
			FabricaConexao fabrica) throws Exception {

		// jvosantos - 10/07/2019 14:56 - Armazenar se era uma pendencia de "Renovar ou Modificar Voto"
		boolean renovarOuModificar = StringUtils.equals(pendencia.getPendenciaStatusCodigo(), String.valueOf(PendenciaStatusDt.ID_CORRECAO));
		//lrcampos 19/02/2020 16:56 - Adiciona Usuario logado, como o usuario finalizador da pendencia.
		pendencia.setId_UsuarioFinalizador(usuario.getId_UsuarioServentia());

		new PendenciaNe().setInfoPendenciaFinalizar(pendencia, usuario, fabrica);

		// jvosantos - 13/01/2020 13:51 - Adicionar geração de pendencia de erro material
		if(pendencia.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VOTO_SESSAO && voto.getVotoCodigoInt() == VotoTipoDt.ERRO_MATERIAL) {
			gerarPendenciaErroMaterial(voto, usuario, fabrica);
		}else if (pendencia.getPendenciaTipoCodigoToInt() != PendenciaTipoDt.PROCLAMACAO_VOTO && pendencia.getPendenciaTipoCodigoToInt() != PendenciaTipoDt.VERIFICAR_RESULTADO_VOTACAO) {
			finalizarVoto(voto, usuario, fabrica);
			// jvosantos - 18/09/2019 10:28 - Adicionar variavel de voto
			// jvosantos - 20/08/2019 14:29 - Trocar ID_PROC por ID_AUDI_PROC
			verificarGerarFinalizacao(voto.getIdAudienciaProcesso(), renovarOuModificar, usuario, fabrica);
		}
	}

	private void gerarPendenciaErroMaterial(VotoDt voto, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarId(voto.getIdAudienciaProcesso(), fabrica);
		AudienciaDt audienciaDt = new AudienciaNe().consultarId(audienciaProcessoDt.getId_Audiencia(), fabrica);
		
		pendenciaNe.gerarPendenciaErroMaterial(audienciaProcessoDt.getId_ServentiaCargo(), usuario, audienciaProcessoDt.getId_Processo(), audienciaProcessoDt.getId(), calcularPrazo(audienciaDt.getDataAgendada(), audienciaProcessoDt.getId_Processo()), fabrica);
	}

	// jvosantos - 18/09/2019 10:30 - Adicionar variavel de voto
	// jvosantos - 10/07/2019 14:56 - Adicionar flag de "Renovar ou Modificar Voto"
	// jvosantos - 20/08/2019 12:00 - Usar ID_AUDI_PROC no lugar de ID_PROC
	private void verificarGerarFinalizacao(String idAudiProc, boolean renovarOuModificar, UsuarioDt usuario, FabricaConexao fabrica)
			throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe()
				.consultarIdCompleto(idAudiProc);
		// jvosantos - 19/09/2019 18:02 - Corrigir valor passado para consulta de processo por ID
		// jvosantos - 19/09/2019 14:50 - Busca o processo para pegar a serventia
		audienciaProcessoDt.setProcessoDt((new ProcessoNe()).consultarId(audienciaProcessoDt.getId_Processo(), fabrica));

		verificarGerarFinalizacao(audienciaProcessoDt, renovarOuModificar, usuario, fabrica); // jvosantos - 18/09/2019 10:30 - Adicionar variavel de voto
	}
	
	// jvosantos - 06/01/2020 16:35 - Criar método para simplificar lógica
	private boolean existeVotoNaLista(List<VotoDt> votos, int tipoVoto) {
		return votos.stream().filter(x -> x.getVotoCodigoInt() == tipoVoto).findAny().isPresent();
	}

	// jvosantos - 18/09/2019 10:30 - Adicionar variavel de voto
	// jvosantos - 17/09/2019 13:30 - Formatar método 
	// jvosantos - 10/07/2019 14:56 - Adicionar flag de "Renovar ou Modificar Voto"
	private void verificarGerarFinalizacao(AudienciaProcessoDt audienciaProcessoDt, boolean renovarOuModificar,
			UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		// jvosantos - 19/07/2019 09:22 - Flag que indica se a finalização foi gerada por prazo expirado
		// jvosantos - 19/07/2019 09:22 - Remover a flag que indica se a finalização foi gerada por prazo expirado
		ProcessoDt processoDt = Optional.ofNullable(audienciaProcessoDt.getProcessoDt()).orElse(new ProcessoNe().consultarId(audienciaProcessoDt.getId_Processo()));
		
		int quantidadeAbertos = votoPs.verificarVotosAbertos(audienciaProcessoDt.getId()); // jvosantos - 20/08/2019 12:13 - Passar ID_AUDI_PROC para contar votos abertos
		//lrcampos 04/03/2020 12:00 - alterado para idAudiProc, substituindo o idProcesso
		List listProclamacao = pendenciaNe.consultarPendenciasAudienciaProcessoPorListaTipo( // jvosantos - 20/08/2019 14:30 - Trocar método consultarPendenciasProcessoPorListaTipo por consultarPendenciasAudienciaProcessoPorListaTipo
				audienciaProcessoDt.getId(), fabrica, PendenciaTipoDt.PROCLAMACAO_VOTO, PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES);
		if (StringUtils.isNotEmpty(processoDt.getId()) && quantidadeAbertos == 0 && CollectionUtils.isEmpty(listProclamacao)) {
			List<VotoDt> votosOriginais = votoPs.consultarVotosSessao(audienciaProcessoDt.getId()); // jvosantos - 19/09/2019 14:50 - Busca todos os votos
			
			// jvosantos - 06/01/2020 16:29 - Se houver algum voto do tipo ERRO_MATERIAL, deve aguardar a solução do mesmo.
			if(existeVotoNaLista(votosOriginais, VotoTipoDt.ERRO_MATERIAL)) return;			
			
			List<VotoDt> votos = VotacaoUtils.filtraVotosReais(votosOriginais); // jvosantos - 19/09/2019 14:50 - Filtra a lista de todos os votos, para buscar apenas os votos

			// jvosantos - 19/07/2019 09:22 - Seta a flag de prazo expirado, verificando se existe algum voto do tipo Prazo Expirado
			//jvosantos - 19/07/2019 09:22 - Remover a flag de prazoExpirado
			
			// jvosantos - 13/08/2019 16:57 - Seta a flag de existe ressalva, caso exista algum voto do tipo acompanha relator com ressalva
			boolean ressalva = existeVotoNaLista(votos, VotoTipoDt.ACOMPANHA_RELATOR_RESSALVA); // jvosantos - 06/01/2020 16:34 - Substituir por existeVoto
			
			// Se um dos votos for um pedido de vista, não gera pendencias de resultado da votacao ou proclamação de voto
			if (existeVotoNaLista(votos, VotoTipoDt.PEDIDO_VISTA) && !ressalva) // jvosantos - 06/01/2020 16:34 - Substituir por existeVoto
				return;
			
			// jvosantos - 09/10/2019 12:06 - Correção erro de NullPointer
			// jvosantos - 17/09/2019 13:39 - Fluxo especial para as serventias especiais, quando for voto impedido
			// jvosantos - 19/09/2019 14:50 - Correção da lógica
			// jvosantos - 06/01/2020 16:34 - Substituir por existeVoto
			if(isServentiaEspecial(processoDt.getId_Serventia()) && existeVotoNaLista(votosOriginais, VotoTipoDt.IMPEDIDO)) {
				List<String> votantes = consultarVotantesSessao(audienciaProcessoDt.getId(), fabrica);
				if((double)votos.size() / votantes.size() < RAZAO_VOTO_VOTANTE) {
					PendenciaDt pendVerificar = pendenciaNe.gerarPendenciaVerificarVotantes(processoDt, usuario, fabrica); // jvosantos - 23/09/2019 13:38 - Gera pendencia de Verificar Impedimento dos Votantes
					audienciaProcessoPendenciaNe.salvar(pendVerificar.getId(), audienciaProcessoDt.getId(), fabrica); // jvosantos - 26/09/2019 14:13 - Adicionar pendencia de Verificar Votantes na tabela de Relacionamento
					return;
				}
			}

			// jvosantos - 10/07/2019 15:00 - Otimização básica para armazenar o resultado da votação que antes era calculado duas vezes.
			ResultadoVotacaoSessao res = VotacaoUtils.calculaResultado(votos);
			
			boolean precisaProclamar = existeVotoNaLista(votosOriginais, VotoTipoDt.PRAZO_EXPIRADO) || (ResultadoVotacaoSessao.MAIORIA_DIVERGE == res || (ResultadoVotacaoSessao.MAIORIA_RELATOR == res || (ressalva && !renovarOuModificar))) || votos.size() == 0;
			
			// jvosantos - 13/08/2019 16:57 - Não gera resultado da votação caso seja Maioria com Relator e exista voto com Ressalva
			// jvosantos - 13/08/2019 17:06 - Gera resultado da votação caso seja Maioria com Relator, exista Ressalva e seja renovar ou modificar voto
			if (!precisaProclamar) {
				gerarResultadoVotacao(audienciaProcessoDt, usuario, fabrica);
				finalizarPendenciasVotacao(processoDt.getId(), audienciaProcessoDt.getId(), usuario, fabrica,
						PendenciaTipoDt.SESSAO_CONHECIMENTO);
			} else {
				// jvosantos - 10/07/2019 14:59 - Se não vier de "Renovar ou Modificar Voto", gera Proclamação da Decisão (Atualmente, "Divergência Instaurada"), se for, gera "Verificar Resultado"
				// jvosantos - 19/07/2019 09:24 - Se for prazo expirado, gera pendência de Verificar Resultado da Votação
				// jvosantos - 15/08/2019 15:23 - Gerar pendencia de "Verificar Divergência ou Ressalva" para o fluxo novo
				// jvosantos - 19/08/2019 16:41 - Coloca a pendência de "Verificar Divergência ou Ressalva" na tabela AUDI_PROC_PEND
				// jvosantos - 19/08/2019 19:03 - Gerar apreciados quando resultado for Maioria Diverge
				if(!renovarOuModificar) {
					PendenciaDt pendenciaProclamarVoto = pendenciaNe.gerarPendenciaProclamarVoto(
							audienciaProcessoDt.getId_ServentiaCargo(), usuario, processoDt.getId(), fabrica);
					if(pendenciaProclamarVoto != null) {
						//mfssilva - 13/12/2019 15:18 - A fabrica de conexão foi adicionada para usar a mesma transação
						audienciaProcessoPendenciaNe.salvar(pendenciaProclamarVoto.getId(),
								audienciaProcessoDt.getId(), fabrica);
					}
				}else if(res == ResultadoVotacaoSessao.MAIORIA_DIVERGE){
					VotoDt primeiroDivergente = votos.stream().filter(x -> x.getVotoCodigoInt() == VotoTipoDt.DIVERGE).findFirst()
							.orElseThrow(() -> new Exception("Resultado de maioria diverge, mas não encontrou redator"));
					
					audienciaProcessoDt = (new AudienciaProcessoNe()).consultarIdCompleto(audienciaProcessoDt.getId()); // estava faltando informações
					
					reabrirVotacaoApreciados(audienciaProcessoDt, primeiroDivergente.getIdServentiaCargo(),
							usuario, fabrica);
				} else {
					finalizarPendenciasVotacao(processoDt.getId(), audienciaProcessoDt.getId(), usuario, fabrica,
							PendenciaTipoDt.SESSAO_CONHECIMENTO);
					gerarResultadoVotacao(audienciaProcessoDt, usuario, fabrica);
				}
			}
		}
	}

	// jvosantos - 04/06/2019 10:09 - Altera o método "gerarResultadoVotacao" de private para public
	public void gerarResultadoVotacao(AudienciaProcessoDt audienciaProcessoDt,
			UsuarioDt usuario,
			FabricaConexao fabrica) throws Exception {
		AudienciaDt audienciaDt = new AudienciaNe().consultarId(audienciaProcessoDt.getId_Audiencia());
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		String idProcesso = Optional
				.ofNullable(audienciaProcessoDt.getProcessoDt())
				.map(ProcessoDt::getId)
				.orElse(audienciaProcessoDt.getId_Processo());
		List<String> votantesMp = consultarIdsIntegrantesSessaoPorTipo(
				audienciaProcessoDt.getId(),
				Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.VOTANTE, VotanteTipoDt.PRESIDENTE_CAMARA, VotanteTipoDt.PRESIDENTE_SESSAO, VotanteTipoDt.RELATOR));
		for (String id : votantesMp) {
			PendenciaDt pendenciaResultadoVotacao = pendenciaNe.gerarPendenciaResultadoVotacao(id, null, usuario, idProcesso, fabrica);
			if(pendenciaResultadoVotacao != null)
				audienciaProcessoPendenciaNe.salvar(pendenciaResultadoVotacao.getId(), audienciaProcessoDt.getId(), fabrica);
		}
		
		//pendenciaNe.gerarPendenciaResultadoVotacao(audienciaProcessoDt.getId_ServentiaCargo(), null, usuario, idProcesso, fabrica);
		PendenciaDt pendenciaResultadoVotacao = pendenciaNe.gerarPendenciaResultadoVotacao(null, audienciaDt.getId_Serventia(), usuario, idProcesso, fabrica);
		if(pendenciaResultadoVotacao != null)
			audienciaProcessoPendenciaNe.salvar(pendenciaResultadoVotacao.getId(), audienciaProcessoDt.getId(), fabrica);
	}

	public void finalizarPendenciasVotacao(String idAudiProc,
			UsuarioDt usuario,
			FabricaConexao fabrica,
			Integer... tipos) throws Exception {
		List<PendenciaDt> pendencias = new PendenciaNe()
				.consultarPendenciasProcessoPorListaTipo(idAudiProc, fabrica, tipos);
		finalizarPendencias(usuario, fabrica, pendencias);
	}
	
	public void finalizarPendenciaTomarConhecimentoEmLonte(String[] pendencias, UsuarioDt usuario)
			throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			for (String idPendencia : pendencias) {
				finalizarPendenciaTomarConhecimento(idPendencia, usuario, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 20/08/2019 14:30 - Método que finaliza as pendências de uma audi_proc especifica
	public void finalizarPendenciasVotacao(String idProcesso,
			String idAudienciaProcesso,
			UsuarioDt usuario,
			FabricaConexao fabrica,
			Integer... tipos) throws Exception {
		List<PendenciaDt> pendencias = new PendenciaNe()
				.consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, fabrica, tipos);
		finalizarPendencias(usuario, fabrica, pendencias);
	}
	
	// lrcampos - 20/08/2019 14:30 - Método que finaliza as pendências de para conhecimento de uma audi_proc especifica
		public void finalizarPendenciasConhecimentoVotacao(String idProcesso,
				String idAudienciaProcesso,
				UsuarioDt usuario,
				FabricaConexao fabrica,
				Integer... tipos) throws Exception {
			List<PendenciaDt> pendencias = new PendenciaNe()
					.consultarPendenciasAudienciaProcessoParaConhecimento(idAudienciaProcesso, fabrica, tipos);
			finalizarPendencias(usuario, fabrica, pendencias);
		}

	public void finalizarPendenciaTomarConhecimento(String idPendencia, UsuarioDt usuario) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			finalizarPendenciaTomarConhecimento(idPendencia, usuario, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// mrbatista - 24/09/2019 14:30 - OVERLOAD
	public void finalizarPendenciaTomarConhecimento(String idPendencia, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(idPendencia, fabrica);
			pendenciaDt.setId_UsuarioFinalizador(usuario.getId_UsuarioServentia());
			pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario, fabrica);
			
	}
	
	//mfssilva - 13/12/2019 15:18 - A fabrica de conexão foi adicionada para usar a mesma transação
	public void gerarPendenciaPedirVista(List<String> ids,
			VotoDt voto,
			String idProcesso,
			UsuarioDt usuario,
			FabricaConexao fabrica) throws Exception {

		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoDt processoDt = Optional.ofNullable(voto.getAudienciaProcessoDt().getProcessoDt()).orElse(new ProcessoNe().consultarId(idProcesso)); // jvosantos - 02/10/2019 14:20 - Buscar ProcessoDt da AudienciaProcessoDt ou buscar por idProcesso

		for (String usuId : ids) {
			PendenciaDt pendenciaPedirVista = pendenciaNe.gerarPendenciaPedirVista(usuId, null, usuario, idProcesso);
			//	mfssilva - 13/12/2019 15:18 - A fabrica de conexão foi adicionada para usar a mesma transação
			if(pendenciaPedirVista != null) audienciaProcessoPendenciaNe.salvar(pendenciaPedirVista.getId(), voto.getIdAudienciaProcesso(), fabrica);
		}

		// jvosantos - 02/10/2019 14:19 - Correção de erro de NullPointer
		// jvosantos - 20/08/2019 16:55 - Alterar de onde pega o ID_SERV
		PendenciaDt pendenciaPedirVista = pendenciaNe.gerarPendenciaPedirVista(null, processoDt.getId_Serventia(), usuario, idProcesso);
		if(pendenciaPedirVista != null) audienciaProcessoPendenciaNe.salvar(pendenciaPedirVista.getId(), voto.getIdAudienciaProcesso(), fabrica);
	}

	// jvosantos - 20/08/2019 16:57 - Alterar chamada do método para passar ID_AUDI_PROC no lugar de ID_PROC
	private void finalizarPendenciaVinculadasProc(String idAudienciaProcesso, UsuarioDt usuario, FabricaConexao fabrica)
			throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe(); // jvosantos - 20/08/2019 16:57 - Extrair para evitar instanciações desnecessárias
		List<String> idsPendFinalizar = new ArrayList<>();
		idsPendFinalizar = consultarIdsPendAudienciaProcesso(idAudienciaProcesso); // jvosantos - 20/08/2019 16:58 - Alterar método chamado para consultar usando ID_AUDI_PROC
		for (String pendId : idsPendFinalizar) {
			PendenciaDt pendenciaDt = new PendenciaDt();
			pendenciaDt = pendenciaNe.consultarId(pendId); // jvosantos - 20/08/2019 16:57 - Extrair pendenciaNe para evitar instanciações desnecessárias
			setInfoPendenciaFinalizar(pendenciaDt, usuario, fabrica);
		}
	}

	// jvosantos - 20/08/2019 16:58 - Método para consultar pendencias de voto e proclamação de voto usando ID_AUDI_PROC
	private List<String> consultarIdsPendAudienciaProcesso(String idAudienciaProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<String> retorno = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarIdsPendAudienciaProcesso(idAudienciaProcesso);
			return retorno;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	private List<String> consultarIdsPendProcesso(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<String> retorno = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarIdsPendProcesso(idProcesso);
			return retorno;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}
	@Deprecated
	public void setInfoPendenciaFinalizar(PendenciaDt pendencia, UsuarioDt usuario, FabricaConexao fabrica)
			throws Exception {
		setInfoPendenciaFinalizar(pendencia, usuario, PendenciaStatusDt.ID_CUMPRIDA, fabrica);
	}

	@Deprecated
	private void setInfoPendenciaFinalizar(PendenciaDt pendencia,
			UsuarioDt usuario,
			int statusCodigo,
			FabricaConexao fabrica)
			throws Exception {
		new PendenciaNe().setInfoPendenciaFinalizar(pendencia, usuario, fabrica);
	}

	// jvosantos - 06/01/2020 17:09 - Inserir novo voto, desativando o antigo (caso exista)
	public void inserirVotoDesativandoAntigo(VotoDt voto, FabricaConexao fabrica) throws Exception {
		VotoPs ps = new VotoPs(fabrica.getConexao());
		
		if (StringUtils.isNotEmpty(voto.getId())) {
			ps.desativarVoto(voto.getId());
			voto.setId(StringUtils.EMPTY);
		}

		voto.setAtivo(true);
		
		inserirVoto(voto, ps);
	}
	
	public void inserirVoto(VotoDt voto, VotoPs ps) throws Exception {
		// jvosantos - 04/06/2019 10:09 - Adiciona a consulta de tipo do voto caso o ID esteja vazio
		if(StringUtils.isEmpty(voto.getIdVotoTipo()))
			voto.setIdVotoTipo(ps.consultarIdVotoTipo(voto.getVotoTipoCodigo()));

		ps.inserir(voto);
	}

	private void salvarArquivoVoto(VotoDt voto, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		if (voto.getArquivo().getId().isEmpty()) {
			List<ArquivoDt> listaArquivos = new ArrayList<>();
			listaArquivos.add(voto.getArquivo());
			if (voto.getEmenta() != null) {
				listaArquivos.add(voto.getEmenta());
			}
			if (voto.getConfiguracao() != null) {
				listaArquivos.add(voto.getConfiguracao());
			}
			new PendenciaNe().inserirArquivos(voto.getPendenciaDt(), listaArquivos, false, usuario, fabrica);
		} else {
			ArquivoNe arquivoNe = new ArquivoNe();
			arquivoNe.salvar(voto.getArquivo(), fabrica);
			if (voto.getEmenta() != null) {
				arquivoNe.salvar(voto.getEmenta(), fabrica);
			}
			if (voto.getConfiguracao() != null) {
				arquivoNe.salvar(voto.getConfiguracao(), fabrica);
			}
		}
	}
	// jvosantos - 04/06/2019 10:09 - Overload no método para não precisar da flag
	public List<VotoSessaoLocalizarDt> consultarAguardandoVotoSessaoVirtual(String procNumero, UsuarioNe usuario)
			throws Exception {
		return consultarAguardandoVotoSessaoVirtual(procNumero, usuario, false, null);
	}
	// lrcampos - 19/03/2020 10:09 - Adiciona parametro para realizar filtro por serventia
	// jvosantos - 04/06/2019 10:09 - Adiciona flag de "renovar" caso seja a pasta de renovar ou modificar voto
	public List<VotoSessaoLocalizarDt> consultarAguardandoVotoSessaoVirtual(String procNumero, UsuarioNe usuario, boolean renovar, String idServentiaFiltro)
			throws Exception {
		String idServentiaCargo = getIdServentiaCargo(usuario);
		List<VotoSessaoLocalizarDt> lista = !renovar ? consultarPendenciasVoto(
				usuario,
				procNumero,
				PendenciaTipoDt.VOTO_SESSAO,
				PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO, 
				idServentiaFiltro) : consultarVotosRenovarNaoAnalisados(usuario, procNumero, PendenciaTipoDt.VOTO_SESSAO, idServentiaFiltro);
		PendenciaNe pendenciaNe = new PendenciaNe();
		for (VotoSessaoLocalizarDt votoSessaoLocalizarDt : lista) {
			List pendenciasImpedimento = pendenciaNe
					.consultarPendenciasProcessoResponsavelPorTipo(
							votoSessaoLocalizarDt.getIdProcesso(),
							idServentiaCargo,
							PendenciaTipoDt.VERIFICAR_IMPEDIMENTO);
			votoSessaoLocalizarDt.setTemImpedimento(CollectionUtils.isNotEmpty(pendenciasImpedimento));
		}
		return lista;
	}

	public List<VotoSessaoLocalizarDt> consultarEmVotacaoUsuario(UsuarioNe usuario, String processoNumero) throws Exception {
		List<VotoSessaoLocalizarDt> lista = new AudienciaProcessoNe()
				.consultarEmVotacaoSessaoVirtual(usuario.getId_ServentiaCargo(), processoNumero);
		consultarVotosRelacionadosEmVotacao(usuario, lista);

		return lista;
	}

	// jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto
	private void consultarVotosRelacionadosEmVotacao(UsuarioNe usuario, List<VotoSessaoLocalizarDt> lista) throws Exception {
		consultarVotosRelacionadosEmVotacao(usuario, lista, false); 
	}
	
	// jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto
	// alsqueiroz - 23/08/2019 - Melhorias na performance
	private void consultarVotosRelacionadosEmVotacao(UsuarioNe usuario, List<VotoSessaoLocalizarDt> lista, boolean trazerProprioVoto) throws Exception {
		LocalDateTime atual = LocalDateTime.now();

		List<VotoDt> votos;
		List<PendenciaDt> pendencias;
		PendenciaDt pendenciaDt;
		PendenciaNe pendenciaNe = new PendenciaNe();
		FabricaConexao fabricaConexao = null;
		LocalDateTime prazo;
		boolean expirado;
		long dias;
		long horas;
		List<String> texto;
		try { // mrbatista - 03/09/2019 11:00 -Alteração da forma como é controlada a conexão
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			// jsantonelli - 07/11/2019 - adicionar nome relator
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
			for (VotoSessaoLocalizarDt dt : lista) {
				votos = consultarVotosVotantesIncluindoObservacao(dt.getIdAudienciaProcesso(), usuario, fabricaConexao, trazerProprioVoto);
				pendencias = pendenciaNe.consultarPendenciasProcessoPorTipo(dt.getIdProcesso(), PendenciaTipoDt.VOTO_SESSAO, fabricaConexao);
				if (CollectionUtils.isNotEmpty(pendencias)) {
					pendenciaDt = pendencias.get(0);
					prazo = parseData(pendenciaDt.getDataLimite());
					expirado = prazo.isBefore(atual);
					dt.setPodeVotar(expirado);
					dt.setPrazoVotacao(pendenciaDt.getDataLimite());
					if (expirado) {
						dt.setTempoRestante("Prazo Expirado");
					} else {
						dias = ChronoUnit.DAYS.between(atual, prazo);
						horas = ChronoUnit.HOURS.between(atual, prazo) % 24;
						dt.setTempoRestante(String.format("%s dia(s) %s hora(s)", dias, horas));
					}
				}

				texto = votos.stream().map(voto -> textoVotante(voto)).collect(Collectors.toList());
				dt.setVotosRelacionados(texto);
				
				// jsantonelli - 07/11/2019 - adicionar nome relator
				dt.setNomeRelator(votoPs.consultarNomeRelator(dt.getIdAudienciaProcesso()));
			}
		} finally {
			fabricaConexao.fecharConexao();
		}

	}

	public void consultarVotosRelacionados(UsuarioNe usuario, List<VotoSessaoLocalizarDt> lista) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			consultarVotosRelacionados(usuario, lista, fabricaConexao);
			// mrbatista - 02/10/2019 14:42 - Consulta classes de processos
			consultarVotosRelacionadosClasseProcesso(usuario, lista, fabricaConexao);
		} finally {
			fabricaConexao.fecharConexao();
		}
	}

	void consultarVotosRelacionados(UsuarioNe usuario,
			List<VotoSessaoLocalizarDt> lista,
			FabricaConexao fabrica) throws Exception {
		for (VotoSessaoLocalizarDt dt : lista) {
			List<VotoDt> votos = consultarVotosVotantesIncluindoObservacao(
					dt.getIdAudienciaProcesso(),
					usuario,
					fabrica);
			List<String> texto = votos.stream().map(voto -> textoVotante(voto)).collect(Collectors.toList());
			dt.setVotosRelacionados(texto);
		}
	}
	
	// mrbatista - 11/10/2019 14:53 - Consultar classe dos votos relacionados do processo
	private void consultarVotosRelacionadosClasseProcesso(UsuarioNe usuario, List<VotoSessaoLocalizarDt> lista) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			consultarVotosRelacionadosClasseProcesso(usuario, lista, fabricaConexao);
		} finally {
			fabricaConexao.fecharConexao();
		}
	}
	
	// mrbatista - 11/10/2019 14:53 - Consultar classe dos votos relacionados do processo
	private void consultarVotosRelacionadosClasseProcesso(UsuarioNe usuario,
			List<VotoSessaoLocalizarDt> lista,
			FabricaConexao fabrica) throws Exception {
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		for (VotoSessaoLocalizarDt dt : lista) {
			dt.setClasseProcesso(processoParteNe.consultaClasseProcessoIdAudiProc(dt.getIdAudienciaProcesso(), fabrica));
		}
	}

	private String textoVotante(VotoDt voto) {
		String texto = voto.getVotoTipo();
		if (voto.getVotoTipo().isEmpty()) {
			boolean expirado = voto.isExpirado()
					|| StringUtils.isNotEmpty(voto.getPendenciaDt().getDataLimite()) && LocalDateTime.now()
							.isAfter(
									LocalDateTime
											.parse(voto.getPendenciaDt().getDataLimite().substring(0, 16), formatter));
			texto = expirado ? SITUACAO_VOTO_PRAZO_EXPIRADO : SITUACAO_VOTO_AGUARDANDO_MANIFESTACAO;  // jvosantos - 27/09/2019 14:41 - Extrair texto para constante
		}

		return voto.getNomeVotante() + " - " + texto;
	}

	public VotoSessaoLocalizarDt calcularPrazo(VotoSessaoLocalizarDt voto) {
		return calcularPrazo(voto, false);
	}

	public VotoSessaoLocalizarDt calcularPrazo(VotoSessaoLocalizarDt voto, boolean isPrazoRenovar) {
		voto.setDataSessao(voto.getDataSessao().substring(0, 16));

		LocalDateTime atual = LocalDateTime.now();
		LocalDateTime prazo = null;
		
		try {
			voto.setPrazoVotacao(calcularPrazo(voto.getDataSessao(), voto.getIdProcesso(), isPrazoRenovar));
			prazo = LocalDateTime.parse(voto.getPrazoVotacao(), formatter);
		} catch (Exception e) {}
		
		if(prazo == null) {
			voto.setTempoRestante("Erro ao calcular Prazo");
		}else if (atual.isAfter(prazo)) {
			voto.setTempoRestante("Prazo Expirado");
		} else {
			long dias = ChronoUnit.DAYS.between(atual, prazo);
			long horas = ChronoUnit.HOURS.between(atual, prazo) % 24;
			voto.setTempoRestante(String.format("%s dia(s) %s hora(s)", dias, horas));
		}

		return voto;
	}
	
	public String calcularPrazo(String data, String idProcesso) throws Exception {
		return calcularPrazo(data, idProcesso, false);
	}
	
	public String calcularPrazo(String data, String idProcesso, boolean isPrazoRenovar) throws Exception {
		if (StringUtils.isEmpty(data)) {
			return data;
		}

		String atualDiasUteis = addDiasUteis(data, idProcesso, !isPrazoRenovar ? PRAZO_SESSAO_VIRTUAL_DIAS : PRAZO_SESSAO_VIRTUAL_RENOVAR_DIAS);
		LocalDateTime retornoDiasUteis = LocalDateTime.parse(atualDiasUteis.substring(0, 16), formatter);

		return retornoDiasUteis.format(formatter);
	}

	public String addDiasUteis(String data, String idProcesso, int dias) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			ProcessoDt procDt = new ProcessoNe().consultarId(idProcesso);
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(procDt.getId_Serventia());
			return prazoSuspensoNe.getProximaDataValidaNovoCPCComHoraPJD(data, dias, "18:00:00", serventiaDt, obFabricaConexao); // jvosantos - 25/07/2019 11:38 - Correção do método chamado e alteração do horario final 
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String addDiasUteis(String data, String idProcesso, int dias, FabricaConexao conexao) throws Exception {
		FabricaConexao obFabricaConexao = conexao;
		try {
			if(obFabricaConexao == null)
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			ProcessoDt procDt = new ProcessoNe().consultarId(idProcesso, obFabricaConexao);
			PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(procDt.getId_Serventia(), obFabricaConexao);
			return prazoSuspensoNe.getProximaDataValidaNovoCPCComHoraPJD(data, dias, "18:00:00", serventiaDt, obFabricaConexao); // jvosantos - 25/07/2019 11:38 - Correção do método chamado e alteração do horario final 
		} finally {
			if(conexao == null)obFabricaConexao.fecharConexao();
		}
	}

	public List<VotanteDt> consultarVotantesSessaoPeloIdProcesso(String idProcesso, UsuarioDt usuarioDt)
			throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			String id = new AudienciaProcessoNe()
					.consultarAudienciaProcessoPendente(idProcesso, usuarioDt, obFabricaConexao)
					.getId();
			return consultarIntegrantesSessaoPorTipo(id, Arrays.asList(VotanteTipoDt.VOTANTE));
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	public List<String> consultarVotantesSessao(String idAudienciaProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarVotantesSessao(idAudienciaProcesso, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	public List<VotanteDt> consultarVotantesSessaoCompleto(String idAudienciaProcesso) throws Exception {
		return consultarVotantesSessaoCompleto(idAudienciaProcesso, null);
	}
	
	public List<VotanteDt> consultarVotantesSessaoCompleto(String idAudienciaProcesso, FabricaConexao conexao) throws Exception {
		FabricaConexao obFabricaConexao = conexao;
		try {
			if(obFabricaConexao == null)
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
			return votoPs.consultarVotantesSessaoCompleto(idAudienciaProcesso);
		} finally {
			if(conexao == null) obFabricaConexao.fecharConexao();
		}

	}

	private List<String> consultarVotantesSessao(String idAudienciaProcesso, FabricaConexao obFabricaConexao)
			throws Exception {
		List<String> retorno;
		VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.consultarVotantesSessao(idAudienciaProcesso);
		return retorno;
	}

	// jvosantos - 15/08/2019 14:27 - Adicionar flag para trazer o proprio voto
	public AudienciaProcessoVotacaoDt consultarSituacaoEmVotacao(String idAudienciaProcesso, UsuarioNe usuario)
			throws Exception {
		return consultarSituacaoEmVotacao(idAudienciaProcesso, false, usuario);
	}

	// jvosantos - 15/08/2019 14:27 - Adicionar flag para trazer o proprio voto
	public AudienciaProcessoVotacaoDt consultarSituacaoEmVotacao(String idAudienciaProcesso, boolean trazerProprioVoto, UsuarioNe usuario)
				throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			AudienciaProcessoVotacaoDt sessao = new AudienciaProcessoVotacaoDt();
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaProcessoDt audiencia = getAudienciaProcessoDt(obFabricaConexao, idAudienciaProcesso);
			sessao.setAudienciaProcesso(audiencia);
			Map<String, PendenciaArquivoDt> arquivosRelator = consultarArquivosAnalise(
					usuario,
					obFabricaConexao,
					audiencia);
			sessao.setVotoRelator(arquivosRelator.get("voto"));
			sessao.setEmentaRelator(arquivosRelator.get("ementa"));
			sessao.setNomeRelator(getNomeRelator(audiencia, obFabricaConexao));
			
			List<VotoDt> votos = consultarVotosVotantesIncluindoObservacao(
					idAudienciaProcesso,
					usuario,
					obFabricaConexao,
					trazerProprioVoto);

			sessao.setVotos(votos);

			return sessao;

		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	public List<VotoSessaoLocalizarDt> consultarAguardandoFinalizacao(String processoNumero,
			UsuarioNe usuario,
			int status) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		List<VotoSessaoLocalizarDt> lista = null;
		try {
			String idServentiaCargo = getIdServentiaCargo(usuario);
			ServentiaCargoDt serventiaCargoDt = new ServentiaCargoNe().consultarId(idServentiaCargo);
			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
			
			lista = obPersistencia.consultarAguardandoFinalizacao(idServentiaCargo, processoNumero, status);
			
			// jvosantos - 15/08/2019 15:07 - Buscar sessões com prazo expirado
			if (status == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
				List<VotoSessaoLocalizarDt> list = obPersistencia.consultarAguardandoFinalizacaoPrazo(idServentiaCargo,
						processoNumero);
				list.forEach(voto -> voto.setExpirado(true));
				// jvosantos - 16/10/2019 18:37 - Filtra os processos de serventias especiais
				// que atingiram 2/3 dos votos e que não tem divergencia ou ressalva, gera
				// resultado da votação e finaliza a pendencia
				list = list.stream().filter(x -> verificarUnanimidadePrazoExpirado(x, usuario.getUsuarioDt(), obFabricaConexao)).collect(Collectors.toList());
				lista.addAll(list);
			}

			lista.forEach(sessao -> {
				sessao.setNomeRelator(serventiaCargoDt.getNomeUsuario());
			});
			consultarVotosRelacionados(usuario, lista, obFabricaConexao); // mrBatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada

			// jsantonelli - 21/10/2019: adiciona dados dos classificadores
			lista = ordenaClassificadores(lista, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		lista = configuraPrazoRestante(lista, true); 
		return lista;
	}

	private List<VotoSessaoLocalizarDt> configuraPrazoRestante(List<VotoSessaoLocalizarDt> lista) {
		return configuraPrazoRestante(lista, false);
	}

	private List<VotoSessaoLocalizarDt> configuraPrazoRestante(List<VotoSessaoLocalizarDt> lista, boolean isPrazoRenovar) {
		return lista.stream().map(audiencia -> calcularPrazo(audiencia, isPrazoRenovar)).map(sessao -> {
			List<String> list = sessao.getVotosRelacionados()
					.stream()
					.map(voto -> voto.replace(SITUACAO_VOTO_AGUARDANDO_MANIFESTACAO, SITUACAO_VOTO_PRAZO_EXPIRADO))  // jvosantos - 27/09/2019 14:41 - Extrair texto para constante
					.collect(Collectors.toList());
			sessao.setVotosRelacionados(list);
			return sessao;
		}).collect(Collectors.toList());
	}
	
	public void consultarPrazoExpiradoOrgaoEspecial() throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		List<VotoSessaoLocalizarDt> lista = null;
		try {
			obFabricaConexao.iniciarTransacao();
			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
			
			UsuarioNe usuarioNe = new UsuarioNe();
			UsuarioDt usuarioDt = usuarioNe.consultarUsuarioCompletoPJD(UsuarioDt.SistemaProjudi);
		
			lista = obPersistencia.consultarPrazoExpiradoServentiaEspecial();
			
			for(VotoSessaoLocalizarDt vot : lista) {
				vot.setExpirado(true);
				verificarUnanimidadePrazoExpirado(vot, usuarioDt, obFabricaConexao);
			}
			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	// jvosantos - 16/10/2019 18:34 - Método para verificar se uma proclamação de decisão precisa acontecer para as serventias especiais
	private boolean verificarUnanimidadePrazoExpirado(VotoSessaoLocalizarDt proclamacao, UsuarioDt usuario, FabricaConexao obFabricaConexao) {
		ProcessoNe processoNe = new ProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		if(!proclamacao.isExpirado()) return true; // Só é necessário para votos expirados
		
		try {
			ProcessoDt processoDt = processoNe.consultarId(proclamacao.getIdProcesso(), obFabricaConexao);
			
			if(processoDt == null || !isServentiaEspecial(processoDt.getId_Serventia())) return true;
			
			List<VotoDt> votosOriginais = consultarVotosSessaoComData(proclamacao.getIdAudienciaProcesso(), obFabricaConexao);
			
			if(existeVotoNaLista(votosOriginais, VotoTipoDt.ERRO_MATERIAL)) return true;
			
			List<VotoDt> votosSessao = VotacaoUtils.filtraVotosReais(votosOriginais);
			
			long qntVotantesSessao = consultarVotantesSessao(proclamacao.getIdAudienciaProcesso(), obFabricaConexao).size();
			
			// Valida os 2/3, se não deu, continua na lista
			if(((double) votosSessao.size()) / qntVotantesSessao < RAZAO_VOTO_VOTANTE) return true;
			
			// Verifica se existe algum voto que faz necessário ter proclamação da decisão (Divergencia ou Acompanha com ressalva)
			boolean precisaProclamar = votosSessao.stream().filter(x -> (x.getVotoCodigoInt() == VotoTipoDt.DIVERGE || x.getVotoCodigoInt() == VotoTipoDt.ACOMPANHA_RELATOR_RESSALVA)).findAny().isPresent();
			
			// Se não for necessário proclamar a decisão, gera resultado da votação e finaliza essa pendencia
			if(!precisaProclamar) {
				AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarIdCompleto(proclamacao.getIdAudienciaProcesso(), obFabricaConexao);
				
				gerarResultadoVotacao(audienciaProcessoDt, usuario, obFabricaConexao);
				
				// Finalizar pendencias de voto e gerar PEND_VOTO_VIRTUAL
				finalizarVotosPendentes(audienciaProcessoDt, usuario, obFabricaConexao);
				
				// Finalizar pendencia de Proclamação (caso exista, provavelmente não existe)
				PendenciaDt pendenciaDt = new PendenciaNe().consultarId(proclamacao.getIdPendencia(), obFabricaConexao);
					
				if(pendenciaDt != null)
					pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario, obFabricaConexao); 
			}
			
			return precisaProclamar;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private String getIdServentiaCargo(UsuarioNe usuario) {
		return getIdServentiaCargo(usuario.getUsuarioDt());
	}

	private String getIdServentiaCargo(UsuarioDt usuario) {
		return usuario.isAssessorMagistrado() ? usuario.getId_ServentiaCargoUsuarioChefe()
				: usuario.getId_ServentiaCargo();
	}

	public FinalizacaoVotoSessaoDt consultarFinalizacaoVoto(String idAudienciaProcesso, UsuarioNe usuario)
			throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			return consultarFinalizacaoVoto(idAudienciaProcesso, usuario, obFabricaConexao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 28/01/2020 17:43 - Modificações para Erro Material
	public FinalizacaoVotoSessaoDt consultarFinalizacaoVotoComAudienciaProcesso(AudienciaProcessoDt audienciaProcesso, UsuarioNe usuario)
			throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			return consultarFinalizacaoVotoComAudienciaProcesso(audienciaProcesso, usuario, obFabricaConexao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 28/01/2020 17:43 - Modificações para Erro Material
	public FinalizacaoVotoSessaoDt consultarFinalizacaoVoto(String idAudienciaProcesso,
			UsuarioNe usuario,
			FabricaConexao obFabricaConexao)
			throws Exception {
		AudienciaProcessoDt audiencia = getAudienciaProcessoDt(obFabricaConexao, idAudienciaProcesso);
		

		return consultarFinalizacaoVotoComAudienciaProcesso(audiencia, usuario, obFabricaConexao);
	}

	// jvosantos - 28/01/2020 17:43 - Modificações para Erro Material
	private FinalizacaoVotoSessaoDt consultarFinalizacaoVotoComAudienciaProcesso(AudienciaProcessoDt audienciaProcessoDt,
			UsuarioNe usuario, FabricaConexao obFabricaConexao) throws Exception {
		VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
		PendenciaArquivoNe pendArqNe = new PendenciaArquivoNe();
		FinalizacaoVotoSessaoDt finalizacao = new FinalizacaoVotoSessaoDt();
		
		finalizacao.setAudienciaProcesso(audienciaProcessoDt);

		Map<String, PendenciaArquivoDt> arquivosRelator = consultarArquivosAnalise(
				usuario,
				obFabricaConexao,
				audienciaProcessoDt);
		finalizacao.setVoto(arquivosRelator.get("voto"));
		finalizacao.setEmenta(arquivosRelator.get("ementa"));

		finalizacao.setArquivoTipoVoto(consultarArquivoTipo(ArquivoTipoDt.RELATORIO_VOTO, obFabricaConexao));
		finalizacao.setArquivoTipoVotoNovo(consultarArquivoTipo(ArquivoTipoDt.VOTO, obFabricaConexao)); // jvosantos - 18/07/2019 15:00 -  Inicializa  novo tipo de  voto ("Voto")
		finalizacao.setArquivoTipoEmenta(consultarArquivoTipo(ArquivoTipoDt.EMENTA, obFabricaConexao));

		List<VotoDt> votos = obPersistencia.consultarVotosSessao(audienciaProcessoDt.getId());
		for (VotoDt voto : votos) {
			List arquivos = pendArqNe
					.consultarArquivosPendenciaFinalizada(
							voto.getPendenciaDt(),
							usuario,
							true,
							false,
							obFabricaConexao);
			if (CollectionUtils.isNotEmpty(arquivos)) {
				voto.setArquivo(((PendenciaArquivoDt) arquivos.get(0)).getArquivoDt());
			}
		}
		List<VotoDt> votosComObservacao = consultarVotosVotantesIncluindoObservacao(
				audienciaProcessoDt.getId(),
				usuario,
				obFabricaConexao);
		finalizacao.setVotos(votosComObservacao);
		boolean podeFinalizar = votos.stream().map(VotoDt::getVotoCodigoInt)
				.anyMatch(voto -> voto == VotoTipoDt.PRAZO_EXPIRADO || voto == VotoTipoDt.PEDIDO_VISTA);
		
		boolean houvePedidoVista = votos.stream().anyMatch(voto -> voto.getVotoCodigoInt() == VotoTipoDt.PEDIDO_VISTA);
		
		finalizacao.setHouvePedidoVista(houvePedidoVista);
		
		boolean prazoExpirado = votos.stream().map(VotoDt::getVotoCodigoInt)
				.anyMatch(voto -> voto == VotoTipoDt.PRAZO_EXPIRADO);
		
		finalizacao.setExpirado(prazoExpirado);
		
		// jvosantos - 04/10/2019 18:33 - Setar flag de 2/3 dos votos para mostrar a opção de continuar o julgamento
		if(prazoExpirado && isServentiaEspecial(audienciaProcessoDt.getProcessoDt().getId_Serventia())) {
			double votantesCount = consultarVotantesSessaoCompleto(audienciaProcessoDt.getId(), obFabricaConexao).size();
			double votosValidosCount = VotacaoUtils.filtraVotosReais(consultarVotosVotantes(audienciaProcessoDt.getId(), usuario, obFabricaConexao)).size();
			
			finalizacao.setHouveDoisTercosVotos( (votosValidosCount / votantesCount) >= RAZAO_VOTO_VOTANTE );
		}else
			finalizacao.setHouveDoisTercosVotos(false);
		
		finalizacao.getVotos().forEach(voto -> {
			// jvosantos - 24/01/2020 18:16 - Alterar mensagem para não exibir prazo expirado quando não é
			// jvosantos - 27/09/2019 14:41 - Extrair texto para constante
			voto.setVotoTipo(StringUtils.isEmpty(voto.getVotoTipo())
					? (prazoExpirado ? SITUACAO_VOTO_PRAZO_EXPIRADO : SITUACAO_VOTO_AGUARDANDO_MANIFESTACAO)
					: voto.getVotoTipo());
			voto.setVotoTipoCodigo(voto.getVotoCodigoInt() > 0 ? voto.getVotoTipoCodigo() : "999");
		});
		
		finalizacao.setResultado(VotacaoUtils.calculaResultado(votos));

		return finalizacao;
	}

	public void iniciarVotacaoSessaoVirtual(String idPend, UsuarioDt usuarioDt, boolean apenasVotantes)
			throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();
			
			iniciarVotacaoSessaoVirtual(idPend, usuarioDt, apenasVotantes, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void iniciarVotacaoSessaoVirtual(AudienciaProcessoDt audiProc, UsuarioDt usuarioDt, boolean apenasVotantes,
			FabricaConexao obFabricaConexao) throws Exception {

		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		List<String> votantes = consultarVotantesSessao(audiProc.getId(), obFabricaConexao);
		List<String> tomarConhecimento = consultarTomarConhecimento(audiProc.getId(), obFabricaConexao);
		String idServMP = consultarVerificarImpedimento(audiProc.getId(), obFabricaConexao);
		
		if(votantes == null || votantes.isEmpty()) {
			throw new MensagemException("Existe uma sessão com a lista de votantes vazia.", new Exception("ID_AUDI_PROC="+audiProc.getId()+";ID_PROC="+audiProc.getId_Processo()+";"));
		}

		AudienciaNe audienciaNe = new AudienciaNe();

		AudienciaDt audienciaDt = audienciaNe.consultarId(audiProc.getId_Audiencia(), obFabricaConexao);
		for (String idServentiaCargo : votantes) {
			PendenciaDt pendVoto = pendenciaNe.gerarPendenciaVotoSessaoVirtual(
					idServentiaCargo, usuarioDt, audiProc.getId_Processo(),
					getDataLimiteVotacao(audienciaDt, audiProc, obFabricaConexao), audiProc.getId(), obFabricaConexao); // jvosantos - 02/09/2019 14:14 - Deixar fixo o prazo limite do voto até as 18:00
			if (pendVoto == null)
				continue;
			audienciaProcessoPendenciaNe.salvar(pendVoto.getId(), audiProc.getId(), obFabricaConexao);
		}
		if (!apenasVotantes) {
			for (String idServentiaCargo : tomarConhecimento) {
				pendenciaNe.gerarPendenciaSessaoConhecimento(idServentiaCargo,
						usuarioDt, audiProc.getId_Processo(), audiProc.getId(), obFabricaConexao);
			}

			if (StringUtils.isNotEmpty(idServMP)) {
				PendenciaDt pendenciaVerificarImpedimento = pendenciaNe.gerarPendenciaVerificarImpedimento(idServMP,
						usuarioDt, audiProc.getId_Processo(), obFabricaConexao);

				if (pendenciaVerificarImpedimento != null)
					audienciaProcessoPendenciaNe.salvar(pendenciaVerificarImpedimento.getId(), audiProc.getId(),
							obFabricaConexao);
			}
		}
	}

	// jvosantos - 24/01/2020 18:19 - Função para extrair a data limite da votação
	private String getDataLimiteVotacao(AudienciaDt audienciaDt, AudienciaProcessoDt audiProc,
			FabricaConexao obFabricaConexao) throws Exception {
		return addDiasUteis(audienciaDt.getDataAgendada(), audiProc.getId_Processo(), PRAZO_SESSAO_VIRTUAL_DIAS, obFabricaConexao);
	}
	
	// jvosantos - 06/01/2020 11:57 - Extrair método que usa AUDI_PROC no lugar do ID_PEND
	public void iniciarVotacaoSessaoVirtual(String idPendMinuta, UsuarioDt usuarioDt, boolean apenasVotantes, FabricaConexao obFabricaConexao)
			throws Exception {
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		AudienciaProcessoDt audiProc = new AudienciaProcessoNe().consultarId(audienciaProcessoPendenciaNe.consultarPorIdPend(idPendMinuta, obFabricaConexao), obFabricaConexao);
		iniciarVotacaoSessaoVirtual(audiProc, usuarioDt, apenasVotantes, obFabricaConexao);
	}

	private List<String> consultarTomarConhecimento(String idAudienciaProcesso, FabricaConexao obFabricaConexao)
			throws Exception {
			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarTomarConhecimento(idAudienciaProcesso);
	}

	private String consultarVerificarImpedimento(String idAudienciaProcesso, FabricaConexao obFabricaConexao)
			throws Exception {
			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarVerificarImpedimento(idAudienciaProcesso);
	}

	public void baixarArquivo(String id, HttpServletResponse response, UsuarioNe usuario) throws Exception {
		new ArquivoNe()
				.baixarArquivo(id, response, new LogDt(usuario.getId_Usuario(), usuario.getIpComputadorLog()), false);
	}

	public void salvarPreAnalise(AnaliseVotoSessaoDt analiseVoto, UsuarioNe usuario) throws Exception {

		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();

			VotoDt voto = analiseVoto.getVoto();
			//lrcampos 17/12/2019 15:32 - Função para realizar a pré analise da pendencia de voto, mantendo o historico da ultima analise.
			salvarPreAnaliseVotoSessao(analiseVoto, usuario, fabrica);

			// jvosantos - 04/06/2019 10:09 - Bloco de código para armazenar o voto da pasta 'renovar'
			if(analiseVoto.isRenovarVoto()) {
				if(analiseVoto.isAguardandoAssinatura()) {
					voto.getPendenciaDt().setCodigoTemp(String.valueOf(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP));
					new PendenciaNe().AlterarCodigoTempPendencia(voto.getPendenciaDt(), fabrica);
				}else {
					voto.getPendenciaDt().setCodigoTemp(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
					new PendenciaNe().AlterarCodigoTempPendencia(voto.getPendenciaDt(), fabrica);
				}
			}else {
				if (analiseVoto.isAguardandoAssinatura()) {
					voto.getPendenciaDt().setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
					new PendenciaNe().alterarStatus(voto.getPendenciaDt(), fabrica);
				} else {
					voto.getPendenciaDt().setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
					new PendenciaNe().alterarStatus(voto.getPendenciaDt(), fabrica);
				}
			}

			inserirVotoDesativandoAntigo(voto, fabrica);

			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}

	// mfssilva - 11/11/2019 - O metodo (salvarPreAnaliseFinalizarVoto) passa a gerar uma pedencia filha para cada proclamacao salva 
	private void salvarPreAnaliseVotoSessao(AnaliseVotoSessaoDt analiseVoto, UsuarioNe usuario, FabricaConexao fabrica)
			throws Exception {

		PendenciaNe pendenciaNe = new PendenciaNe();
		String idPendenciaFinalizar =  analiseVoto.getVoto().getPendenciaDt().getId();;
		PendenciaDt pendenciaDt = pendenciaNe.consultarId(idPendenciaFinalizar);
		PendenciaDt pendenciaAntiga = new PendenciaDt(); // jvosantos - 12/12/2019 15:14 - Copiar pendencia atual para pendencia antiga para não perder o ID
		pendenciaAntiga.copiar(pendenciaDt);
		
		this.salvarPendenciaVotoSessao(analiseVoto, usuario, pendenciaDt, fabrica);
		new AudienciaProcessoPendenciaNe().salvar(pendenciaDt.getId(),	analiseVoto.getIdAudienciaProcesso(), fabrica); // jvosantos - 12/12/2019 15:18 - Adicionar fabrica de conexão para usar a mesma transação
		pendenciaNe.setInfoPendenciaFinalizar(pendenciaNe.consultarId(idPendenciaFinalizar), usuario.getUsuarioDt(), fabrica);

	}
	
	/**
	 * lrcampos 17/12/2019 15:34 - Finaliza pendencia atual e gera uma pendencia filha com seus arquivos
	 */
	private void salvarPendenciaVotoSessao(AnaliseVotoSessaoDt analiseVoto, UsuarioNe usuario, PendenciaDt pendenciaDt, FabricaConexao fabrica) throws Exception {
		
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();	
		UsuarioDt usuarioDt = usuario.getUsuarioDt();
		preencheUsuarioLogPendPai(pendenciaDt, usuarioDt); // jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
		List<ArquivoDt> arquivos = new ArrayList<ArquivoDt>();
		arquivos.add(montarArquivoPreAnalise(analiseVoto, usuario, new ArquivoDt()));
		pendenciaNe.gerarPendenciaFilha(pendenciaDt,pendenciaResponsavelNe.consultarResponsaveis(pendenciaDt.getId()), new ArrayList<>(), arquivos,
				false, false, false, fabrica);
		analiseVoto.getVoto().setPendenciaDt(pendenciaDt);
	}
	
	ArquivoDt montarArquivoPreAnalise(AnaliseVotoSessaoDt analiseVoto, UsuarioNe usuario, ArquivoDt arquivo) {
		if (arquivo == null) {
			arquivo = new ArquivoDt();
		}
		arquivo.setArquivo(analiseVoto.getTextoVoto());
		arquivo.setId_ArquivoTipo(analiseVoto.getIdArquivoTipo());
		if (StringUtils.isNotBlank(analiseVoto.getNomeArquivo())) {
			arquivo.setNomeArquivo(analiseVoto.getNomeArquivo().replace(".html", "") + ".html");
		} else {
			arquivo.setNomeArquivo("online.html");
		}
		arquivo.setContentType("text/html");
		arquivo.setIpComputadorLog(usuario.getIpComputadorLog());
		arquivo.setId_UsuarioLog(usuario.getId_Usuario());
		return arquivo;
	}

	private ArquivoDt montarArquivoPreAnalise(UsuarioNe usuario,
			ArquivoDt arquivo,
			String texto,
			String idArquivoTipo,
			String nomeArquivo) {
		if (arquivo == null) {
			arquivo = new ArquivoDt();
		}
		arquivo.setArquivo(texto);
		arquivo.setId_ArquivoTipo(idArquivoTipo);
		if (StringUtils.isBlank(nomeArquivo)) {
			arquivo.setNomeArquivo(nomeArquivo.replace(".html", "") + ".html");
		} else {
			arquivo.setNomeArquivo("online.html");
		}
		arquivo.setContentType("text/html");
		arquivo.setIpComputadorLog(usuario.getIpComputadorLog());
		arquivo.setId_UsuarioLog(usuario.getId_Usuario());
		return arquivo;
	}

	// jvosantos - 04/06/2019 10:09 - Método que busca o Voto de tipo Proclamação de uma Audi_Proc
	public String consultarIdProclamacao(String idAudiProc) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarIdProclamacao(idAudiProc);
		} finally {
			fabrica.fecharConexao();
		}
	}

	public List consultarVotosPreAnalisados(UsuarioNe usuario, String processoNumero, int tipo) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs ps = new VotoPs(fabrica.getConexao());
			String idServentiaCargo = getServentiaCargoOuServentiaCargoChefe(usuario); // jvosantos - 15/01/2020 12:11 - Usar método extraido
			List<VotoSessaoLocalizarDt> votos = ps.consultarVotosPreAnalisados(idServentiaCargo, processoNumero, tipo, usuario, fabrica);
			consultarVotosRelacionados(usuario, votos, fabrica); // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada
//			verificarPrazoPendencias(votos, fabrica); // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada

			votos = configuraPrazoRestante(votos);
			// jsantonelli - 21/10/2019: adiciona dados dos classificadores
			votos = ordenaClassificadores(votos, fabrica);
			return votos;

		} finally {
			fabrica.fecharConexao();
		}
	}
	
	public List consultarVotosPreAnalisadosAguardandoVoto(UsuarioNe usuario, String processoNumero, int tipo, String idServentiaFiltro) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs ps = new VotoPs(fabrica.getConexao());
			String idServentiaCargo = (StringUtils.isEmpty(usuario.getUsuarioDt().getId_ServentiaCargoUsuarioChefe())
					? usuario.getId_ServentiaCargo()
					: usuario.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
			List<VotoSessaoLocalizarDt> votos = ps.consultarVotosPreAnalisadosOtimizado(idServentiaCargo, processoNumero, tipo, PendenciaStatusDt.ID_EM_ANDAMENTO, usuario, fabrica, idServentiaFiltro);
			consultarVotosRelacionados(usuario, votos, fabrica); // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada
			verificarPrazoPendencias(votos, fabrica); // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada

			// jsantonelli - 21/10/2019: adiciona dados dos classificadores
			votos = ordenaClassificadores(votos, fabrica);
			return votos;

		} finally {
			fabrica.fecharConexao();
		}
	}


	public List consultarVotosRetirarPauta(UsuarioNe usuario, String processoNumero) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			List<VotoSessaoLocalizarDt> votos = ps
					.consultarRetirarPauta(usuario.getId_ServentiaCargo(), processoNumero);
			consultarVotosRelacionados(usuario, votos);
			return votos
					.stream()
					.map(
							(
									voto) -> calcularPrazo(voto))
					.collect(Collectors.toList());

		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 04/02/2020 11:47 - Tipar lista
	public List<VotoSessaoLocalizarDt> consultarTomarConhecimento(UsuarioNe usuario, String processoNumero, int codigo, boolean isSegundoGrau)
			throws Exception {
		List<VotoSessaoLocalizarDt> votos = new ArrayList<>();
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());

			if (isSegundoGrau) {
				votos = ps.consultarTomarConhecimento(null, usuario.getId_Serventia(), codigo, processoNumero);
			} else {
				votos = ps.consultarTomarConhecimento(usuario.getId_ServentiaCargo(), null, codigo, processoNumero);
			}
			consultarVotosRelacionados(usuario, votos, fabrica);
			for (VotoSessaoLocalizarDt votoSessaoLocalizarDt : votos) {
				Optional<VotoDt> votoProprio = consultarVotosVotantesIncluindoProprio(
						votoSessaoLocalizarDt.getIdAudienciaProcesso(),
						usuario,
						fabrica).stream()
								.filter(
										voto -> voto.getIdServentiaCargo()
												.equals(Funcoes.getIdServentiaCargo(usuario.getUsuarioDt())))
								.findAny();
				if (votoProprio.isPresent()) {
					votoSessaoLocalizarDt.getVotosRelacionados().add(textoVotante(votoProprio.get()));
					votoSessaoLocalizarDt.getVotosRelacionados().sort(null);
				}
			}
			if (codigo == PendenciaTipoDt.RESULTADO_VOTACAO) {
				for (VotoSessaoLocalizarDt votoSessaoLocalizarDt : votos) {
					ResultadoVotacaoSessao resultadoVotacao = consultarResultadoVotacao(
							votoSessaoLocalizarDt.getIdAudienciaProcesso(),
							fabrica);
					votoSessaoLocalizarDt.setResultado(resultadoVotacao.getDescricao());
				}
			}
			return votos
					.stream()
					.map(
							(
									voto) -> calcularPrazo(voto))
					.collect(Collectors.toList());

		} finally {
			fabrica.fecharConexao();
		}
	}

	public ResultadoVotacaoSessao consultarResultadoVotacao(String idAudienciaProcesso) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			return consultarResultadoVotacao(idAudienciaProcesso, fabrica);
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	// jvosantos - 08/07/2019 14:03 - Método para consultar os votos da sessão com data de voto
	public List<VotoDt> consultarVotosSessaoComData(String idAudienciaProcesso, FabricaConexao fabrica)
			throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		List<VotoDt> votosSessao = votoPs.consultarVotosSessaoComData(idAudienciaProcesso);
		return votosSessao;
	}
	
	// jvosantos - 09/12/2019 12:49 - Correção do calculo do resultado da votação
	public ResultadoVotacaoSessao consultarResultadoVotacao(String idAudienciaProcesso, FabricaConexao fabrica)
			throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		List<VotoDt> votosSessao = votoPs.consultarVotosSessao(idAudienciaProcesso);
		return VotacaoUtils.calculaResultado(votosSessao);
	}

	public List consultarResultadoUnanime(UsuarioNe usuario, String processoNumero) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			List<VotoSessaoLocalizarDt> votos = ps
					.consultarResultadoUnanime(usuario.getId_ServentiaCargo(), processoNumero);
			consultarVotosRelacionados(usuario, votos);
			return votos
					.stream()
					.map(
							(
									voto) -> calcularPrazo(voto))
					.collect(Collectors.toList());

		} finally {
			fabrica.fecharConexao();
		}
	}

	public int consultarQuantidadeVotosPreAnalisados(String idServentiaCargo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarQuantidadeVotosPreAnalisadosOtimizado(idServentiaCargo);

		} finally {
			fabrica.fecharConexao();
		}
	}

	public int consultarQuantidadeVotosAguardandoAssinatura(String idServentiaCargo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarQuantidadeVotosAguardandoAssinatura(idServentiaCargo);

		} finally {
			fabrica.fecharConexao();
		}
	}
	
	public void assinarVotoEmenta(UsuarioDt usuario, List<ArquivoDt> arquivos, PendenciaDt pendenciaDt)
			throws Exception {

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			new PendenciaArquivoNe().inserirArquivos(pendenciaDt, arquivos, false, false,new LogDt(usuario.getId(), usuario.getIpComputadorLog()), obFabricaConexao);
			this.iniciarVotacaoSessaoVirtual(pendenciaDt.getId(), usuario, false, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void assinarVotosComIdPendencia(String idPendencia, UsuarioNe usuarioSessao, boolean salvarSenha, String senhaCertificado) throws Exception {
		FabricaConexao fabricaConexao = null;
		List<ArquivoDt> arquivosAssinados = new ArrayList<ArquivoDt>();

		try {
			
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabricaConexao.iniciarTransacao();
			arquivosAssinados.clear();
			
			List<ArquivoDt> arquivos = consultarArquivoVoto(fabricaConexao, idPendencia).stream()
					.filter((arquivo) -> !arquivo.getArquivoTipoCodigo()
					.equals(String.valueOf(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)))
					.collect(Collectors.toList());

			for (ArquivoDt arquivo : arquivos) {
				arquivosAssinados.add(this.gerarDocumentoAssinado(arquivo, usuarioSessao, salvarSenha, senhaCertificado));
			}			
			this.assinar(idPendencia, arquivosAssinados, usuarioSessao, fabricaConexao);

			fabricaConexao.finalizarTransacao();
			
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();
		}
	}
	
	private ArquivoDt gerarDocumentoAssinado(ArquivoDt arquivo , UsuarioNe usuarioSessao,	boolean salvarSenha, String senhaCertificado) throws Exception {
		  return AssinaturaArquivoUtils.assinarArquivo(senhaCertificado, salvarSenha, usuarioSessao, this.gerarArquivoDt(arquivo));						
	}
	
	public ArquivoDt gerarArquivoDt(ArquivoDt arquivo){
	 	ArquivoDt arquivoDt = new ArquivoDt();
	  	arquivoDt.setArquivo(arquivo.getArquivo());
	  	arquivoDt.setNomeArquivo(arquivo.getNomeArquivo());
	  	arquivoDt.setId_ArquivoTipo(arquivo.getId_ArquivoTipo());
	  	arquivoDt.setContentType(arquivo.getContentType());
	  	arquivoDt.setAssinado(true);
	  	arquivoDt.setId(arquivo.getId());
	  	return arquivoDt;
	}

	public List consultarVotosAguardandoAssinatura(String idServentiaCargo,
			String processoNumero,
			UsuarioNe usuario,
			int pendTipo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			List<VotoSessaoLocalizarDt> votos = ps
					.consultarVotosAguardandoAssinatura(idServentiaCargo, processoNumero, pendTipo, usuario, fabrica);
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			PendenciaNe pendenciaNe = new PendenciaNe();
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			for (VotoSessaoLocalizarDt voto : votos) {
				List<PendenciaArquivoDt> arquivos = pendenciaArquivoNe
						.consultarArquivosPendenciaComHash(
								pendenciaNe.consultarId(voto.getIdPendencia()),
								true,
								false,
								usuario, fabrica);  // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada
				
				for (PendenciaArquivoDt pendenciaArquivoDt : arquivos) {
					int codigo = Funcoes.StringToInt(pendenciaArquivoDt.getArquivoDt().getArquivoTipoCodigo());
					
					// jvosantos - 07/10/2019 15:03 - Alterar lógica
					// jvosantos - 07/10/2019 14:37 - Adicionar novo tipo de arquivo de voto
					switch(codigo) {
						case ArquivoTipoDt.VOTO:
							voto.setArquivoVoto(pendenciaArquivoDt.getArquivoDt());
							break;
						case ArquivoTipoDt.RELATORIO_VOTO:
							voto.setArquivoVotoRelator(pendenciaArquivoDt.getArquivoDt());
							break;
						case ArquivoTipoDt.EMENTA:
							voto.setArquivoEmenta(pendenciaArquivoDt.getArquivoDt());
							break;
					}
				}
				
				if(voto.getArquivoVotoRelator() == null || voto.getArquivoEmenta() == null) {
					// jvosantos - 07/10/2019 16:25 - Buscar arquivos de Voto e Ementa do Relator
					AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(voto.getIdAudienciaProcesso(), fabrica);
					
					// jvosantos - 18/10/2019 13:40 - Corrigir NullPointer e extrair método
					List<PendenciaArquivoDt> arquivosRelator = new ArrayList<PendenciaArquivoDt>();
					
					if(voto.getArquivoVotoRelator() == null)
						setarListaArquivosSeExistir(audienciaProcessoDt.getId_PendenciaVotoRelator(), arquivosRelator, pendenciaNe, pendenciaArquivoNe, usuario, fabrica);
					
					if(voto.getArquivoEmenta() == null && StringUtils.isNotEmpty(audienciaProcessoDt.getId_PendenciaEmentaRelator()))
						setarListaArquivosSeExistir(audienciaProcessoDt.getId_PendenciaEmentaRelator(), arquivosRelator, pendenciaNe, pendenciaArquivoNe, usuario, fabrica);
					
					for (PendenciaArquivoDt pendenciaArquivoDt : arquivosRelator) {
						int codigo = Funcoes.StringToInt(pendenciaArquivoDt.getArquivoDt().getArquivoTipoCodigo());
						
						switch (codigo) {
						case ArquivoTipoDt.RELATORIO_VOTO :
							voto.setArquivoVotoRelator(pendenciaArquivoDt.getArquivoDt());
							break;
						case ArquivoTipoDt.EMENTA:
							voto.setArquivoEmenta(pendenciaArquivoDt.getArquivoDt());
							break;
						}
					}
				
				}
			}
			return votos
					.stream()
					.map(
							(
									voto) -> calcularPrazo(voto))
					.collect(Collectors.toList());
		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 18/10/2019 13:41 - Extrair método
	private void setarListaArquivosSeExistir(String idPendencia, List<PendenciaArquivoDt> arquivosRelator, PendenciaNe pendenciaNe,
			PendenciaArquivoNe pendenciaArquivoNe, UsuarioNe usuario, FabricaConexao fabrica) throws Exception {
		PendenciaDt pendenciaDt = pendenciaNe.consultarId(idPendencia);
		if(pendenciaDt != null) {
			List<PendenciaArquivoDt> arquivosTemp = pendenciaArquivoNe.consultarArquivosPendenciaComHash(pendenciaDt, true, true, usuario, fabrica);
			
			if(arquivosTemp != null)
				arquivosRelator.addAll(arquivosTemp);
		}
	}

	private void assinar(String idPendencia, List<ArquivoDt> arquivos, UsuarioNe usuario, FabricaConexao fabrica)
			throws Exception {
		PendenciaDt pendenciaDt = new PendenciaNe().consultarId(idPendencia, fabrica);
		VotoDt voto = consultarVotoCompleto(idPendencia, usuario.getUsuarioDt(), fabrica);

		if (pendenciaDt != null) {
			// jvosantos - 18/10/2019 13:42 - Corrigir Retirar de Pauta no Aguardando Assinatura
			if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.PROCLAMACAO_VOTO && arquivos.size() > 0) {
				finalizarProclamacao(pendenciaDt, fabrica, usuario, arquivos, null);
				finalizarPendenciaVoto(pendenciaDt, voto, pendenciaDt.getId_Processo(), usuario.getUsuarioDt(), fabrica);
			// jvosantos - 23/07/2019 14:26 - Correção ao assinar em lote as pendência de "Verificar Resultado da Votação"
			} else if(pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_RESULTADO_VOTACAO && arquivos.size() > 1) {
				finalizarVerificarResultadoVotacao(pendenciaDt, fabrica, usuario, arquivos, null);
			// jvosantos - 24/01/2020 18:19 - Modificação para o Erro Material
			} else if(pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL && arquivos.size() >= 1) {
				finalizarErroMaterial(pendenciaDt, usuario, arquivos);
			} else {
				finalizarPendenciaVoto(pendenciaDt, voto, pendenciaDt.getId_Processo(), usuario.getUsuarioDt(), fabrica);
			}
		}

		ArquivoNe arquivoNe = new ArquivoNe();
		for (ArquivoDt arquivo : arquivos) {
			Funcoes.preencheUsuarioLog(arquivo, usuario.getUsuarioDt());
			arquivoNe.salvar(arquivo, fabrica);
		}
	}

	private VotoDt consultarVotoCompleto(String idPendencia, UsuarioDt usuario, FabricaConexao fabrica)
			throws Exception {
		VotoDt voto = consultarVoto(idPendencia);
		if (voto != null) {
			voto.setPendenciaDt(new PendenciaNe().consultarId(idPendencia));
			voto.setAudienciaProcessoDt((new AudienciaProcessoNe()).consultarIdCompleto((new AudienciaProcessoPendenciaNe()).consultarPorIdPend(idPendencia)));
			voto.getAudienciaProcessoDt().setProcessoDt((new ProcessoNe()).consultarId(voto.getAudienciaProcessoDt().getId_Processo(), fabrica)); // jvosantos - 19/11/2019 15:33 - Correção NullPointer porque o AudienciaProcessoNe.consultarIdCompleto não preenche o ProcessoDt
		}
		return voto;
	}

	// jvosantos - 17/10/2019 17:07 - Adicionar FabricaConexao em alguns métodos
	// jvosantos - 03/10/2019 18:30 - Formatar e refatorar método
	private void finalizarProclamacao(PendenciaDt pendenciaDt, FabricaConexao fabrica, UsuarioNe usuario,
			List<ArquivoDt> arquivos, FinalizacaoVotoSessaoDt finalizacaoVoto) throws Exception {
		String decisao = "";

		ProcessoDt processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo(), fabrica);
		String idAudiProcPendente = new AudienciaProcessoPendenciaNe().consultarPorIdPend(pendenciaDt.getId(), fabrica);		
		AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarId(idAudiProcPendente, fabrica);
		audienciaProcessoDt.setProcessoDt(processoDt);

		// jvosantos - 04/06/2019 10:09 - Adicionado uma verificação para não realizar a
		// consulta desnecessariamente
		if (finalizacaoVoto == null)
			finalizacaoVoto = consultarFinalizacaoVoto(audienciaProcessoDt.getId(), usuario, fabrica);
				
		int tipoFinalizacao = Optional.ofNullable(finalizacaoVoto).map(FinalizacaoVotoSessaoDt::getTipoAnalise).orElse(FinalizacaoVotoSessaoDt.NORMAL);
		String statusDecisao = finalizacaoVoto.getAudienciaProcesso().getAudienciaProcessoStatusCodigoTemp();
		
		List<ArquivoDt> arquivosPendencia = consultarArquivosPendenciaAbertaFinalizada(pendenciaDt, fabrica);		
		
		Optional<ArquivoDt> configuracao = Optional.ofNullable(arquivosPendencia).flatMap(lista -> {
			return lista.stream()
					.filter((arquivoDt) -> Funcoes
							.StringToInt(arquivoDt.getArquivoTipoCodigo()) == ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)
					.findAny();
		});
		
		if (configuracao.isPresent()) {
			ArquivoDt arquivoDt = new ArquivoNe().consultarId(configuracao.get().getId(), fabrica.getConexao());
			String conteudo = new String(arquivoDt.getConteudoSemAssinar());
			String[] strings = conteudo.split(Configuracao.SEPARDOR03);
			decisao = strings[0];
			tipoFinalizacao = Funcoes.StringToInt(strings[1]);
		}
		
		finalizacaoVoto.setTipoAnalise(tipoFinalizacao);

		if (!finalizacaoVoto.isNormal()) {
			
			if (finalizacaoVoto.isRetirarPauta())
				this.retirarDePauta(finalizacaoVoto, usuario, pendenciaDt, processoDt, audienciaProcessoDt, arquivos, fabrica);
			else if (finalizacaoVoto.isJulgamentoAdiado())
				this.adiarJugamento(finalizacaoVoto, usuario, arquivosPendencia, fabrica, processoDt, audienciaProcessoDt);
		
		} else {			
			finalizacaoVoto.getAudienciaProcesso().setAudienciaProcessoStatusCodigoTemp(StringUtils.defaultIfEmpty(decisao, statusDecisao));
			this.desativarTodosVotos(finalizacaoVoto.getAudienciaProcesso().getId(), fabrica); // jvosantos - 10/02/2020 17:51 - Desativa todos os votos
			this.reativarVotos(finalizacaoVoto.getAudienciaProcesso().getId(), new FiltroVotoTipo(VotoTipoDt.getListaVotoTipoReativar()), fabrica);
			this.gerarVotoProclamacaoDecisao(pendenciaDt, usuario, finalizacaoVoto, fabrica);
			this.reabrirVotacao(finalizacaoVoto, fabrica, usuario, pendenciaDt);
			this.gerarPendenciaVotoEmenta(usuario, arquivos, audienciaProcessoDt, finalizacaoVoto, fabrica);
		}				
	}
		
	private void reativarVotos(String idAudiProc, FiltroVotoTipo filtro, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		
		List<VotoDt> votosSessaoAtivosInativos = votoPs.consultarTodosVotosSessaoAtivosInativos(idAudiProc);
		
		List<VotoDt> votosReativar = filtro.filtrar(votosSessaoAtivosInativos);
		
		votosReativar.stream().map(VotoDt::getId).forEach(ativarVoto(votoPs));
	
	}

	private Consumer<? super String> ativarVoto(VotoPs votoPs) {
		return x -> {
			try {
				votoPs.ativarVoto(x);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	/**
	 * 
	 * Gerar pendecia voto/ementa com os arquivos assinados 
	 * @return 
	 * 
	 */
	public List<PendenciaDt> gerarPendenciaVotoEmenta(UsuarioNe usuario, List<ArquivoDt> arquivos, 
			AudienciaProcessoDt audienciaProcessoDt, FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica)
			throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();

		UsuarioDt usuarioDt = usuario.getUsuarioDt();
			
		ArquivoDt ementa =  montarArquivoAssinado(usuario, extrairEmenta(arquivos));
		ArquivoDt voto =  montarArquivoAssinado(usuario, extrairVoto(arquivos));
		
		AudienciaProcessoDt audienciaProcessoDt2 = audienciaProcessoNe.consultarIdCompleto(audienciaProcessoDt.getId());

		Optional<ArquivoDt> arquivoConfiguracaoPendenciaMinuta = getArquivoConfiguracaoPendencia(pendenciaNe,pendenciaArquivoNe, audienciaProcessoDt2.getId_PendenciaVotoRelator());
		Optional<ArquivoDt> arquivoConfiguracaoPendenciaEmenta = getArquivoConfiguracaoPendencia(pendenciaNe,pendenciaArquivoNe, audienciaProcessoDt2.getId_PendenciaEmentaRelator());
		
		//Salvar arquivos sem pendecia arquivo resposta
		PendenciaDt pendenciaVoto = gerarPendenciaComArquivo(audienciaProcessoDt2.getId_PendenciaVotoRelator(), audienciaProcessoDt.getId(), 
				Arrays.asList(
						montarArquivoPreAnalise(usuario, null, new String(Signer.extrairConteudoP7s(ementa.getConteudoSemAssinar())), String.valueOf(ArquivoTipoDt.EMENTA),  (ementa.getArquivoTipo() + "_online.html").replace(" ", "_")),
						montarArquivoAssinado(usuario, voto),
						montarArquivoAssinado( usuario, ementa),
						arquivoConfiguracaoPendenciaMinuta.orElse(montarArquivoConfiguracaoPreAnaliseFinalizacao(finalizacaoVoto, null, usuario))
					), usuarioDt, false, fabrica);				
    	PendenciaDt pendenciaEmenta = gerarPendenciaComArquivo(audienciaProcessoDt2.getId_PendenciaEmentaRelator(), audienciaProcessoDt.getId(), 
        		Arrays.asList(						
						montarArquivoAssinado(usuario, ementa), 
						arquivoConfiguracaoPendenciaEmenta.orElse(montarArquivoConfiguracaoPreAnaliseFinalizacao(finalizacaoVoto, null, usuario))
					), usuarioDt, false,  fabrica);	
        
		//Salvar arquivos com pendecia arquivo resposta
        pendenciaArquivoNe.inserirArquivos(pendenciaVoto, Arrays.asList(montarArquivoPreAnalise(usuario, null, new String(Signer.extrairConteudoP7s(voto.getConteudoSemAssinar())), String.valueOf(ArquivoTipoDt.RELATORIO_VOTO),  (voto.getArquivoTipo() + "_online.html").replace(" ", "_"))) ,true, false, new LogDt(pendenciaVoto.getId_UsuarioLog(), pendenciaVoto.getIpComputadorLog()), fabrica);
		pendenciaArquivoNe.inserirArquivos(pendenciaEmenta, Arrays.asList(montarArquivoPreAnalise(usuario, null, new String(Signer.extrairConteudoP7s(ementa.getConteudoSemAssinar())), String.valueOf(ArquivoTipoDt.EMENTA), (voto.getArquivoTipo() + "_online.html").replace(" ", "_")))  , true, false, new LogDt(pendenciaVoto.getId_UsuarioLog(), pendenciaVoto.getIpComputadorLog()), fabrica);

		audienciaProcessoNe.alterarPendenciaVotoEmenta(audienciaProcessoDt.getId(), pendenciaVoto.getId(), pendenciaEmenta.getId(), fabrica);

		// jvosantos - 24/01/2020 18:20 - Salvar pendências na AUDI_PROC_PEND
		new AudienciaProcessoPendenciaNe().salvar(Arrays.asList(pendenciaVoto.getId(), pendenciaEmenta.getId()), audienciaProcessoDt.getId(), fabrica);
		
		// Finalizar Pendencia Voto
		pendenciaNe.setInfoPendenciaFinalizar(pendenciaNe.consultarId(audienciaProcessoDt2.getId_PendenciaVotoRelator()), usuario.getUsuarioDt(), fabrica);
		// Finalizar Pendencia Ementa
		pendenciaNe.setInfoPendenciaFinalizar(pendenciaNe.consultarId(audienciaProcessoDt2.getId_PendenciaEmentaRelator()), usuario.getUsuarioDt(), fabrica);

		return Arrays.asList(pendenciaVoto, pendenciaEmenta);
	}

	protected Optional<ArquivoDt> getArquivoConfiguracaoPendencia(PendenciaNe pendenciaNe,
			PendenciaArquivoNe pendenciaArquivoNe, String idPend) throws Exception {
		List<PendenciaArquivoDt> arquivosPendenciaMinuta = pendenciaArquivoNe.consultarPendencia(pendenciaNe.consultarId(idPend), true);
		Optional<ArquivoDt> arquivoConfiguracaoPendenciaMinuta = arquivosPendenciaMinuta.stream().map(PendenciaArquivoDt::getArquivoDt).filter(x -> Funcoes.StringToInt(x.getArquivoTipoCodigo()) == ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE).findFirst();
		return arquivoConfiguracaoPendenciaMinuta;
	}
	/**
	 * @param arquivos
	 * @return arquivo voto
	 */
	private ArquivoDt extrairVoto(List<ArquivoDt> arquivos) {
		return  arquivos.stream().filter(arquivo -> arquivo.getId_ArquivoTipo().equals(String.valueOf(ArquivoTipoDt.RELATORIO_VOTO))).findFirst().get();
	}

	/**
	 * @param arquivos
	 * @return ArquivoEmenta
	 */
	private ArquivoDt extrairEmenta(List<ArquivoDt> arquivos) {
		return arquivos.stream().filter(arquivo -> arquivo.getId_ArquivoTipo().equals(String.valueOf(ArquivoTipoDt.EMENTA))).findFirst().get();
	}
	
	/**
	 * 
	 * Cria uma pendecia filha e vincula audiencia Audiencia_Processo_Pendencia
	 */
	private PendenciaDt gerarPendenciaComArquivo(String idPendencia, String idAudienciaProcesso, List<ArquivoDt> listaArquivoVoto, UsuarioDt usuarioDt, boolean respostaArquivoInserir, FabricaConexao fabrica) throws Exception {

		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		
		pendenciaDt = pendenciaNe.consultarId(idPendencia);
		pendenciaDt.setId_PendenciaStatus(new PendenciaStatusNe().consultarPendenciaStatusCodigo(PendenciaStatusDt.ID_PRE_ANALISADA).getId());
		preencheUsuarioLogPendPai(pendenciaDt, usuarioDt); // jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo

		pendenciaNe.gerarPendenciaFilha(pendenciaDt,
				new PendenciaResponsavelNe().consultarResponsaveis(pendenciaDt.getId()), new ArrayList<>(),
				listaArquivoVoto, false, respostaArquivoInserir, false, fabrica);
		//mfssilva - 13/12/2019 15:18 - A fabrica de conexão foi adicionada para usar a mesma transação
		new AudienciaProcessoPendenciaNe().salvar(pendenciaDt.getId(), idAudienciaProcesso, fabrica);

		return pendenciaDt;
	}
	
	/**
	 * @param usuario
	 * @param arquivoDt
	 * @param arquivoAssinado
	 * @throws Exception
	 */
	private ArquivoDt montarArquivoAssinado(UsuarioNe usuario, ArquivoDt arquivoDt)
			throws Exception {
		ArquivoDt arquivoAssinado = null;
		
		arquivoAssinado = new ArquivoDt();

	    arquivoAssinado.setId("");
		arquivoAssinado.setNomeArquivo(arquivoDt.getNomeArquivo());
		arquivoAssinado.setId_ArquivoTipo(arquivoDt.getId_ArquivoTipo());
		arquivoAssinado.setArquivoTipo(arquivoDt.getArquivoTipo());
		arquivoAssinado.setContentType (arquivoDt.getContentType());
		arquivoAssinado.setArquivo(arquivoDt.getConteudoSemAssinar());
		arquivoAssinado.setCaminho(arquivoDt.getCaminho());
		arquivoAssinado.setDataInsercao (arquivoDt.getDataInsercao());
		arquivoAssinado.setUsuarioAssinador(arquivoDt.getUsuarioAssinador());
		arquivoAssinado.setRecibo(arquivoDt.getRecibo());
		arquivoAssinado.setCodigoTemp(arquivoDt.getCodigoTemp());
		arquivoAssinado.setArquivoTipoCodigo(arquivoDt.getArquivoTipoCodigo());
		arquivoAssinado.setIpComputadorLog(usuario.getIpComputadorLog());
		arquivoAssinado.setId_UsuarioLog(usuario.getId_Usuario());	
		arquivoAssinado.setArquivo(arquivoDt.getConteudoSemAssinar());
		
		return arquivoAssinado;
	}
	
	private void retirarDePauta(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario,  PendenciaDt pendenciaDt, ProcessoDt processoDt, AudienciaProcessoDt audienciaProcessoDt, List<ArquivoDt> listArquivos, FabricaConexao fabrica) throws Exception {
		
		List<Integer> gerarPendenciaVotantesTipo = Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.VOTANTE, VotanteTipoDt.PRESIDENTE_CAMARA, VotanteTipoDt.PRESIDENTE_SESSAO);

		List<String> votantesSessao = new VotoNe().consultarIdsIntegrantesSessaoPorTipo(finalizacaoVoto.getAudienciaProcesso().getId(), gerarPendenciaVotantesTipo);
		//mrbatista
		List<Integer> listaPendenciasConcluso = new ArrayList<Integer>(Arrays.asList(PendenciaTipoDt.CONCLUSO_VOTO, PendenciaTipoDt.CONCLUSO_EMENTA));
		finalizarTodasPendenciasVotacao(audienciaProcessoDt, usuario.getUsuarioDt(), fabrica, listaPendenciasConcluso);
		
		//Limpa o idArquivo da pendencia do voto e adiciona os dados do arquivo novo(movimentação)
		String arquivoTipo = (String)new ArquivoTipoNe().consultarPeloArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.CERTIDAO)).get(0);
		listArquivos.forEach(arquivoDt -> {
			arquivoDt.setId("");
			arquivoDt.setArquivoTipo("Certidão");
			arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.CERTIDAO));
			arquivoDt.setId_ArquivoTipo(arquivoTipo);
			if(!arquivoDt.getUsuarioAssinador().trim().equals(""))arquivoDt.setNomeArquivo("Certidao.html.p7s");			
		});
			
		// jvosantos - 18/12/2019 15:52 - Correção para salvar na AUDI_PROC_PEND apenas uma vez
		finalizacaoRetirarPauta(pendenciaDt, fabrica, usuario, new PendenciaNe(), processoDt, new AudienciaProcessoNe(), audienciaProcessoDt, votantesSessao, listArquivos);
	}
	
	// jvosantos - 22/01/2020 12:23 - Adicionar estrategia
	public void finalizarVotacaoRetirarPauta(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario,
			List<ArquivoDt> listArquivos) throws Exception {

		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();

			PendenciaDt pendenciaDt = new PendenciaNe().consultarId(finalizacaoVoto.getIdPendencia(), fabrica);
			String idAudiProcPendente = new AudienciaProcessoPendenciaNe().consultarPorIdPend(pendenciaDt.getId(), fabrica);
			ProcessoDt processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo(), fabrica);
			AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarId(idAudiProcPendente, fabrica);
			audienciaProcessoDt.setProcessoDt(processoDt);
			
			// jvosantos - 04/12/2019 11:41 - Remover codigo morto
			
			setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabrica);			
			retirarDePauta(finalizacaoVoto, usuario, pendenciaDt, processoDt, audienciaProcessoDt, listArquivos, fabrica);
			
			//gerarVoto(usuario, g, fabrica, pendenciaDt, audienciaProcessoDt);

			fabrica.finalizarTransacao();
			
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	public void finalizarVotacaoJulgamentoAdiado(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario,
			List<ArquivoDt> arquivos) throws Exception {

		FabricaConexao fabrica = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		try {
			
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);			
			fabrica.iniciarTransacao();

			PendenciaDt pendenciaDt = new PendenciaNe().consultarId(finalizacaoVoto.getIdPendencia(), fabrica);
			ProcessoDt processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo(), fabrica);
			String idAudiProcPendente = new AudienciaProcessoPendenciaNe().consultarPorIdPend(pendenciaDt.getId(), fabrica);
			AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarId(idAudiProcPendente, fabrica);
			audienciaProcessoDt.setProcessoDt(processoDt);
			
			if (finalizacaoVoto == null)
				finalizacaoVoto = consultarFinalizacaoVoto(audienciaProcessoDt.getId(), usuario, fabrica);
			
			if(!Arrays.asList(PendenciaTipoDt.CONCLUSO_EMENTA, PendenciaTipoDt.CONCLUSO_VOTO).contains(pendenciaDt.getPendenciaTipoCodigoToInt()))
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabrica);

			adiarJugamento(finalizacaoVoto, usuario, arquivos, fabrica, processoDt, audienciaProcessoDt);			

			finalizarPendenciasVotacao(finalizacaoVoto.getIdProcesso(), finalizacaoVoto.getIdAudienciaProcesso(), usuario.getUsuarioDt(), fabrica, PendenciaTipoDt.VOTO_SESSAO);

			fabrica.finalizarTransacao();
			
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}

	private void gerarVoto(UsuarioNe usuario, GeracaoVotoStrategy g, FabricaConexao fabrica, PendenciaDt pendenciaDt,
			AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		g.setIdAudienciaProcesso(audienciaProcessoDt.getId())
			.setIdPendencia(pendenciaDt.getId())
			.setIdUsuario(usuario.getId_Usuario())
			.setFabrica(fabrica)
			.gerarVoto();
	}
	
	private void adiarJugamento(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario, List<ArquivoDt> arquivos, FabricaConexao fabrica,  ProcessoDt processoDt, AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		
		List<Integer> gerarPendenciaVotantesTipo = Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.VOTANTE, VotanteTipoDt.PRESIDENTE_CAMARA, VotanteTipoDt.PRESIDENTE_SESSAO);		
		List<String> votantesSessao = new VotoNe().consultarIdsIntegrantesSessaoPorTipo(finalizacaoVoto.getAudienciaProcesso().getId(), gerarPendenciaVotantesTipo);

		PendenciaDt pendenciaAdiadoDt = finalizacaoJulgamentoAdiado(fabrica, usuario, arquivos, new PendenciaNe(), processoDt, votantesSessao);

		if(pendenciaAdiadoDt != null)
			new AudienciaProcessoPendenciaNe().salvar(pendenciaAdiadoDt.getId(), audienciaProcessoDt.getId(), fabrica);
	}
	
	private VotoDt gerarVotoProclamacaoDecisao(PendenciaDt pendenciaDt,  UsuarioNe usuario, FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica)
			throws Exception {
		VotoDt proclamacaoVotoDt = new VotoDt();
		
		proclamacaoVotoDt.setVotoTipoCodigo(String.valueOf(VotoTipoDt.PROCLAMACAO_DECISAO));
		proclamacaoVotoDt.setPendenciaDt(pendenciaDt);
		proclamacaoVotoDt.setId_UsuarioLog(usuario.getId_Usuario());
		proclamacaoVotoDt.setAudienciaProcessoDt(finalizacaoVoto.getAudienciaProcesso());
		proclamacaoVotoDt.setIdAudienciaProcessoStatusVencido(new AudienciaProcessoNe().consultarStatusAudienciaTemp(finalizacaoVoto.getAudienciaProcesso().getId(), fabrica).getId());

		inserirVotoDesativandoAntigo(proclamacaoVotoDt, fabrica);
		
		return proclamacaoVotoDt;
	}

	// jvosantos - 11/07/2019 18:13 - Extrair trecho de código usado em dois métodos diferentes
	private PendenciaDt finalizacaoJulgamentoAdiado(FabricaConexao fabrica, UsuarioNe usuario, List<ArquivoDt> arquivos,
			PendenciaNe pendenciaNe, ProcessoDt processoDt, List<String> votantesSessao) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt = pendenciaNe
				.gerarPendenciaAdiarJulgamento(
						processoDt.getId_Serventia(),
						null,
						usuario.getUsuarioDt(),
						processoDt.getId(),
						fabrica,
						arquivos);
		for (String id : votantesSessao) {
			pendenciaNe
					.gerarPendenciaAdiarJulgamento(
							null,
							id,
							usuario.getUsuarioDt(),
							processoDt.getId(),
							fabrica,
							null);
		}
		
		return pendenciaDt;
	}

	// jvosantos - 11/07/2019 16:44 - Extrair método responsável por retirar de pauta
	private PendenciaDt finalizacaoRetirarPauta(PendenciaDt pendenciaDt, FabricaConexao fabrica, UsuarioNe usuario,
			PendenciaNe pendenciaNe, ProcessoDt processoDt, AudienciaProcessoNe audienciaProcessoNe,
			AudienciaProcessoDt audienciaProcessoDt, List<String> votantesSessao, List<ArquivoDt> listArquivos) throws Exception {
		// jvosantos - 18/12/2019 15:52 - Correção para salvar na AUDI_PROC_PEND apenas uma vez		
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		
		PendenciaDt pendenciaRetirarPautaSecretario = pendenciaNe
				.gerarPendenciaRetirarPauta(
						null,
						processoDt.getId_Serventia(),
						usuario.getUsuarioDt(),
						pendenciaDt.getId_Processo(),
						audienciaProcessoDt.getId(),
						fabrica);
		if(pendenciaRetirarPautaSecretario != null) audienciaProcessoPendenciaNe.salvar(pendenciaRetirarPautaSecretario.getId(), audienciaProcessoDt.getId(), fabrica);
		
		for (String id : votantesSessao) {
			PendenciaDt retirarPauta = pendenciaNe.gerarPendenciaRetirarPauta(id, null, usuario.getUsuarioDt(), processoDt.getId(), audienciaProcessoDt.getId(), fabrica);
			if(retirarPauta != null)
				audienciaProcessoPendenciaNe.salvar(retirarPauta.getId(), audienciaProcessoDt.getId(), fabrica);
		}
		
		LogDt logDt = new LogDt();
		String complemento = "(Sessão do dia " + (new AudienciaNe()).consultarAudienciaCompleta(audienciaProcessoDt.getId_Audiencia(), fabrica).getDataAgendada() + ")";
		
		// jvosantos - 11/07/2019 16:44 - Correção de erros de Log
		logDt.setIpComputador(usuario.getIpComputadorLog());
		logDt.setIpComputadorLog(usuario.getIpComputadorLog());
		
		logDt.setId_Usuario(usuario.getId_Usuario());
		logDt.setId_UsuarioLog(usuario.getId_Usuario());
		
		// jvosantos - 11/02/2020 17:31 - Salva os arquivos de retirada de pauta e vincula com a movimentação
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		movimentacaoDt.copiar((new MovimentacaoNe()).gerarMovimentacaoAudienciaSessaoRetirada(
				audienciaProcessoDt.getProcessoDt().getId_Processo(), usuario.getId_UsuarioServentia(), complemento,
				logDt, fabrica));
		
		salvarArquivosMovimentacao(movimentacaoDt, listArquivos, processoDt, logDt, fabrica, new MovimentacaoArquivoNe());
		
		audienciaProcessoNe.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt, Funcoes.DataHora(new Date()), String.valueOf(AudienciaProcessoStatusDt.RETIRAR_PAUTA), logDt, fabrica);
		
		return pendenciaRetirarPautaSecretario;
	}
	
	// jvosantos - 24/01/2020 18:20 - Método para consultar arquivos de pendencias
	private List<ArquivoDt> consultarArquivosPendenciaAbertaFinalizada(PendenciaDt pendenciaDt)
			throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			return consultarArquivosPendenciaAbertaFinalizada(pendenciaDt, fabrica);
		} finally {
			fabrica.fecharConexao();
		}		
	}

	private List<ArquivoDt> consultarArquivosPendenciaAbertaFinalizada(PendenciaDt pendenciaDt, FabricaConexao fabrica)
			throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		List<ArquivoDt> arquivosPendencia = pendenciaArquivoNe.consultarArquivosPendencia(pendenciaDt.getId(), fabrica);
		if (arquivosPendencia == null) {
			arquivosPendencia = Optional
					.ofNullable(
							((List<PendenciaArquivoDt>) pendenciaArquivoNe
							.consultarArquivosPendenciaFinalizada(pendenciaDt, null, true, false, fabrica)))
					.map(
							lista -> lista
									.stream()
									.map(PendenciaArquivoDt::getArquivoDt)
									.collect(Collectors.toList()))
					.orElse(new ArrayList<>());
			for (int i = 0; i < arquivosPendencia.size(); i++) {
				arquivosPendencia
						.set(i, new ArquivoNe().consultarId(arquivosPendencia.get(i).getId(), fabrica.getConexao()));
			}
		}
		return arquivosPendencia;
	}

	public void finalizarVotacao(FinalizacaoVotoSessaoDt finalizacaoVoto,
			UsuarioNe usuario,
			List<ArquivoDt> listArquivos) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			boolean inserirArquivos = true;
			PendenciaArquivoDt pendenciaArquivoDt;
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(finalizacaoVoto.getIdPendencia(), fabrica); // ToDo: Corrigir, quando é prazo expirado e a pendencia é aberta pela primeira vez, ela não está no banco.
			int status = StringToInt(pendenciaDt.getPendenciaStatusCodigo());
			
			// jvosantos - 14/06/2019 12:18 - Mudança no fluxo para convocação de votantes
			if(finalizacaoVoto.isConvocarVotantes()) {
				if(!convocarVotantes(finalizacaoVoto, usuario, fabrica)) throw new MensagemException("Erro ao convocar novos votantes.");
				
				setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabrica);
				
				fabrica.finalizarTransacao();
				return;
			}
			
			if (listArquivos.size() == 1) {
				pendenciaArquivoDt = finalizacaoVoto.getVoto();
				if (pendenciaArquivoDt != null) {
					listArquivos.get(0).setId(pendenciaArquivoDt.getArquivoDt().getId());
					inserirArquivos = false;
				}
			} else {
				for (ArquivoDt arquivoDt2 : listArquivos) {
					if (arquivoDt2.getArquivoTipo().equals("Ementa")) {
						pendenciaArquivoDt = finalizacaoVoto.getEmenta();
					} else {
						pendenciaArquivoDt = finalizacaoVoto.getVoto();
					}
					if (pendenciaArquivoDt != null && status == PendenciaStatusDt.ID_EM_ANDAMENTO) {
						arquivoDt2.setId(pendenciaArquivoDt.getArquivoDt().getId());
						inserirArquivos = false;
					}
				}
			}
			if (inserirArquivos || status != PendenciaStatusDt.ID_EM_ANDAMENTO) {
				pendenciaNe.inserirArquivos(pendenciaDt, listArquivos, false, usuario.getUsuarioDt(), fabrica);
			} else {
				for (ArquivoDt arquivoDt2 : listArquivos) {
					arquivoDt2.setIpComputadorLog(usuario.getIpComputadorLog());
					arquivoDt2.setId_UsuarioLog(usuario.getId_Usuario());
					new ArquivoNe().salvar(arquivoDt2, fabrica);
				}
			}
			pendenciaDt.setId_UsuarioFinalizador(usuario.getId_UsuarioServentia());
			pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabrica);
			finalizarProclamacao(pendenciaDt, fabrica, usuario, listArquivos, finalizacaoVoto);

			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}	
	}

	// jvosantos - 14/06/2019 11:32 - Método para convocar os votantes, adiciona na audi_proc_votantes e gera pendencias para eles.
	private boolean convocarVotantes(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario, FabricaConexao fabrica) throws Exception {
		boolean sucesso = true;
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		
		if(StringUtils.isEmpty(finalizacaoVoto.getIdVotanteConvocado1()) || StringUtils.isEmpty(finalizacaoVoto.getIdVotanteConvocado2())) return false;
		
		sucesso &= adicionarVotante(finalizacaoVoto.getIdVotanteConvocado1(), finalizacaoVoto.getIdAudienciaProcesso(), true, usuario, fabrica);
		sucesso &= adicionarVotante(finalizacaoVoto.getIdVotanteConvocado2(), finalizacaoVoto.getIdAudienciaProcesso(), true, usuario, fabrica);
		
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, 18);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);		
		
		String dataLimite = (new VotoNe()).addDiasUteis(
				df.format(calendar.getTime()),
				finalizacaoVoto.getIdProcesso(),
				2);

		PendenciaDt pendenciaDt = pendenciaNe.gerarPendenciaVotoSessaoVirtual(finalizacaoVoto.getIdVotanteConvocado1(), usuario.getUsuarioDt(), finalizacaoVoto.getIdProcesso(), dataLimite, finalizacaoVoto.getAudienciaProcesso().getId(), fabrica);
		
		sucesso &= pendenciaDt != null;
		//mfssilva - 13/12/2019 15:18 - A fabrica de conexão foi adicionada para usar a mesma transação
		audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), finalizacaoVoto.getAudienciaProcesso().getId(), fabrica);
		
		pendenciaDt = pendenciaNe.gerarPendenciaVotoSessaoVirtual(finalizacaoVoto.getIdVotanteConvocado2(), usuario.getUsuarioDt(), finalizacaoVoto.getIdProcesso(), dataLimite, finalizacaoVoto.getAudienciaProcesso().getId(), fabrica);
		
		sucesso &= pendenciaDt != null;
		//mfssilva - 13/12/2019 15:18 - A fabrica de conexão foi adicionada para usar a mesma transação
		audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), finalizacaoVoto.getAudienciaProcesso().getId(), fabrica);
		
		return sucesso;
	}
	
	private boolean adicionarVotante(String idServCargo, String idAudienciaProcesso, UsuarioNe usuario, FabricaConexao fabrica) throws Exception {
		return adicionarVotante(idServCargo, idAudienciaProcesso, false, usuario, fabrica);
	}
	
	// jvosantos - 14/06/2019 11:32 - Método para adicionar votante na Audi_Proc_Votantes
	private boolean adicionarVotante(String idServCargo, String idAudienciaProcesso, boolean convocado, UsuarioNe usuario, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		
		return votoPs.adicionarVotante(idServCargo, idAudienciaProcesso, convocado, fabrica);
	}

	// jvosantos - 04/06/2019 10:09 - Adição da pendencia no método reabrirVotacao
	private void reabrirVotacao(FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica, UsuarioNe usuario, PendenciaDt pendenciaDt)
			throws Exception {
		AudienciaProcessoNe audiProcNe = new AudienciaProcessoNe();
		Optional<AudienciaProcessoStatusDt> statusAudienciaTemp = Optional
				.ofNullable(audiProcNe.consultarStatusAudienciaTemp(finalizacaoVoto.getIdAudienciaProcesso(), fabrica));
		// jvosantos - 04/06/2019 10:09 - Adição de verificação de se o voto mudou
		// VotoMudou = a decisão mudou || relator acompanhou a divergencia 
		boolean votoMudou =  (!finalizacaoVoto
				.getAudienciaProcesso()
				.getAudienciaProcessoStatusCodigoTemp()
				.equals(
						statusAudienciaTemp
								.map(AudienciaProcessoStatusDt::getAudienciaProcessoStatusCodigo)
								.orElse(""))
				|| finalizacaoVoto.isDivergente());
		boolean renovouVotos = false;
		
		if(finalizacaoVoto.getAudienciaProcesso().getAudienciaDt().isVirtual() && !finalizacaoVoto.isConvocarVotantes() && !finalizacaoVoto.isJulgamentoAdiado() && !finalizacaoVoto.isRetirarPauta()) {
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			List<String> votantesSessao = votoPs.consultarVotantesSessao(finalizacaoVoto.getIdAudienciaProcesso());
			
			votantesSessao = votantesSessao
					.stream()
					.filter(removeVotanteVotouImpedido(finalizacaoVoto.getAudienciaProcesso().getId()))
					.collect(Collectors.toList());
			
			for (String idVotante : votantesSessao) {
				gerarPendenciaReabrirVotacao(finalizacaoVoto, fabrica, usuario, idVotante);
			}
			// jvosantos - 18/07/2019 15:02 - Armazena, na pendência de Ementa, uma flag que indica se houve alteração do voto/decisão do Relator
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();     
			AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(finalizacaoVoto.getAudienciaProcesso().getId());
			
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaDt ementa = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaEmentaRelator());
			
			if(ementa == null) {
				throw new Exception("Pendencia de Ementa não foi encontrada (ID_PEND_EMENTA = "+ audienciaProcessoDt.getId_PendenciaEmentaRelator() + "; ID_AUDI_PROC = " + audienciaProcessoDt.getId() + ";)");
			}
			
			ementa.setCodigoTemp(votoMudou ? "1" : "0");
			
			pendenciaNe.AlterarCodigoTempPendencia(ementa, fabrica);
			
			renovouVotos = true;
		}
		
		if (finalizacaoVoto.isNormal() && votoMudou) {
		// jvosantos - 04/06/2019 10:09 - Definição dos fluxos para quando o voto mudou, foi divergente ou apreciados
			audiProcNe
					.alterarStatusAudienciaTemp(
							finalizacaoVoto.getIdAudienciaProcesso(),
							finalizacaoVoto.getAudienciaProcesso().getAudienciaProcessoStatusCodigoTemp(),
							fabrica);
		} else if(!renovouVotos && finalizacaoVoto.getResultado() == ResultadoVotacaoSessao.MAIORIA_RELATOR) {
			gerarResultadoVotacao(finalizacaoVoto.getAudienciaProcesso(), usuario.getUsuarioDt(), fabrica);
		} else if(!renovouVotos){
			reabrirVotacaoApreciados(finalizacaoVoto, fabrica, usuario, pendenciaDt);
		}
	}

	private Predicate<? super String> removeVotanteVotouImpedido(String idAudienciaProcesso) {
		return idServCargoVotante -> {
			try {
				return !existeVotoDeServCargo(idAudienciaProcesso, idServCargoVotante, VotoTipoDt.VOTOS_DE_IMPEDIMENTO);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

	private boolean existeVotoDeServCargo(String idAudienciaProcesso, String idServCargo,
			int[] votosTipo) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
			
			return votoPs.existeVotoDeServCargo(idAudienciaProcesso, idServCargo, votosTipo);
		}finally {
			fabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 04/06/2019 10:09 - Método responsável por reabrir a votação dos Apreciados
	private void reabrirVotacaoApreciados(FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica, UsuarioNe usuario, PendenciaDt pendenciaDt) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoDt audienciaProcessoDt = finalizacaoVoto.getAudienciaProcesso();
		
		audienciaProcessoNe.alterarStatusAudienciaTemp(audienciaProcessoDt.getId(), null, fabrica);
		
		pendenciaNe.gerarPendenciaApreciados(finalizacaoVoto.getServentiaCargoDivergente(), null, usuario.getUsuarioDt(), finalizacaoVoto.getAudienciaProcesso().getId_Processo(),  finalizacaoVoto.getIdAudienciaProcesso(), fabrica);

		audienciaProcessoDt.setId_ServentiaCargoRedator(finalizacaoVoto.getServentiaCargoDivergente());
		
		audienciaProcessoDt.setId_UsuarioLog(usuario.getId_Usuario());
		audienciaProcessoDt.setIpComputadorLog(usuario.getIpComputadorLog());
		audienciaProcessoDt.setAudienciaProcessoStatusTemp(null);
		
		audienciaProcessoNe.salvar(audienciaProcessoDt, fabrica);		
	}

	// jvosantos - 19/08/2019 18:42 - Método responsável por reabrir a votação dos Apreciados ao finalizar voto do votante
	private void reabrirVotacaoApreciados(AudienciaProcessoDt audienciaProcessoDt, String idServCargoRedator, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		if(StringUtils.isEmpty(audienciaProcessoDt.getId_Processo())) throw new Exception("ID do Processo nulo no AudienciaProcessoDt do reabrirVotacaoApreciados");
		if(StringUtils.isEmpty(idServCargoRedator)) throw new Exception("ID do Redator nulo no reabrirVotacaoApreciados");
		
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		
		audienciaProcessoNe.alterarStatusAudienciaTemp(audienciaProcessoDt.getId(), null, fabrica);
		
		PendenciaDt pendenciaApreciados = pendenciaNe.gerarPendenciaApreciados(idServCargoRedator, null, usuario, audienciaProcessoDt.getId_Processo(),  audienciaProcessoDt.getId(), fabrica);

		audienciaProcessoPendenciaNe.salvar(pendenciaApreciados.getId(), audienciaProcessoDt.getId(), fabrica);

		audienciaProcessoDt.setId_ServentiaCargoRedator(idServCargoRedator);
		
		audienciaProcessoDt.setId_UsuarioLog(usuario.getId());
		audienciaProcessoDt.setIpComputadorLog(usuario.getIpComputadorLog());
		audienciaProcessoDt.setAudienciaProcessoStatusTemp(null);
		
		audienciaProcessoNe.salvar(audienciaProcessoDt, fabrica);		
	}

	// jvosantos - 04/03/2020 16:12 - Refatorar e ajustar para erro material
	private void gerarPendenciaReabrirVotacao(FinalizacaoVotoSessaoDt finalizacaoVoto,
			FabricaConexao fabrica,
			UsuarioNe usuario,
			String idVotante) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		PendenciaDt pendenciaFinalizada = new PendenciaDt();

		String idPendenciaFinalizada = votoPs.consultarIdPendenciaPeloVotante(finalizacaoVoto.getIdAudienciaProcesso(),
				idVotante);
		pendenciaFinalizada.setId(idPendenciaFinalizada);
		
		PendenciaDt pendenciaDt = pendenciaNe
				.gerarPendenciaVotoSessaoVirtual(
						idVotante,
						usuario.getUsuarioDt(),
						finalizacaoVoto.getIdProcesso(),
						addDiasUteis(
							finalizacaoVoto.getAudienciaProcesso().getAudienciaDt().getDataAgendada(),
							finalizacaoVoto.getIdProcesso(),
							PRAZO_SESSAO_VIRTUAL_RENOVAR_DIAS),
						finalizacaoVoto.getAudienciaProcesso().getId(),
						fabrica);
		
		// Copia o voto antigo
		VotoDt votoDt = consultarVoto(idPendenciaFinalizada, fabrica);
		votoDt.getPendenciaDt().setId(pendenciaDt.getId());
		votoDt.setId(null);
		votoDt.setAtivo(false);
		votoDt.setAudienciaProcessoDt(finalizacaoVoto.getAudienciaProcesso());
		inserirVoto(votoDt, votoPs);

		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), finalizacaoVoto.getIdAudienciaProcesso(), fabrica);
		
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CORRECAO)); // jvosantos - 04/06/2019 10:09 - Gera a pendencia de aguardando voto como Correção (pasta de renovar ou modificar)
		pendenciaNe.alterarStatus(pendenciaDt, fabrica);
		
		pendenciaDt.setCodigoTemp(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		pendenciaNe.AlterarCodigoTempPendencia(pendenciaDt, fabrica);
		
		List<PendenciaArquivoDt> arquivosPendencia = new PendenciaArquivoNe()
				.consultarArquivosPendenciaFinalizada(pendenciaFinalizada, usuario, true, true, fabrica);
		
		if (arquivosPendencia != null) {
			Optional<ArquivoDt> optionalArquivo = arquivosPendencia
					.stream()
					.map(PendenciaArquivoDt::getArquivoDt)
					.filter(arquivo -> finalizacaoVoto.getIdsArquivoTipoVoto().contains(arquivo.getId_ArquivoTipo())) // jvosantos - 18/07/2019 15:07 - Adicionar novo tipo de voto no filtro (para funcionar com arquivos antigos e novos)
					.findAny();
			if (optionalArquivo.isPresent()) {
				ArquivoDt novoArquivo = optionalArquivo.get();
				byte[] conteudoArquivo = new ArquivoNe().consultarConteudoArquivo(novoArquivo, fabrica);
				
				try {
					Signer.acceptSSL();
					conteudoArquivo = Signer.extrairConteudoP7s(conteudoArquivo);
				} catch (Exception e) {}
				
				novoArquivo.setArquivo(conteudoArquivo);
				novoArquivo.setContentType("text/html");
				novoArquivo.setIpComputadorLog(usuario.getIpComputadorLog());
				novoArquivo.setId_UsuarioLog(usuario.getId_Usuario());
				novoArquivo.setId("");
				
				pendenciaNe.inserirArquivos(pendenciaDt, Arrays.asList(novoArquivo), false, usuario.getUsuarioDt(), fabrica);
			}
		}
	}
	
	// mfssilva - 11/11/2019 - O metodo (salvarPreAnaliseFinalizarVoto) passa a gerar uma pedencia filha para cada proclamacao salva 
	private void salvarPreAnaliseFinalizarVoto(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario)
			throws Exception {

		PendenciaNe pendenciaNe = new PendenciaNe();

		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA); // jvosantos - 21/01/2020 17:41 - Mover

		String idPendenciaFinalizar = finalizacaoVoto.getIdPendencia();

		try {
			fabrica.iniciarTransacao();

			PendenciaDt pendenciaDt = pendenciaNe.consultarId(finalizacaoVoto.getIdPendencia());
			PendenciaDt pendenciaAntiga = new PendenciaDt(); // jvosantos - 12/12/2019 15:14 - Copiar pendencia atual para pendencia antiga para não perder o ID
			pendenciaAntiga.copiar(pendenciaDt);
			//Refatoracao do metodo
			this.salvarPendenciaProclamacao(finalizacaoVoto, usuario, pendenciaDt, fabrica);

			new AudienciaProcessoPendenciaNe().salvar(pendenciaDt.getId(),
					finalizacaoVoto.getAudienciaProcesso().getId(), fabrica); // jvosantos - 12/12/2019 15:18 - Adicionar fabrica de conexão para usar a mesma transação

			//jsantonelli 06/12/2019 14:48 - altera fluxo para inserir somente voto proclamacao decisao
			this.criarVotoProclamacaoDecisao(finalizacaoVoto, fabrica, pendenciaDt, pendenciaAntiga); // jvosantos - 12/12/2019 15:19 - Passar a pendencia antiga ("atual") para apagar o voto antigo

			pendenciaNe.setInfoPendenciaFinalizar(pendenciaNe.consultarId(idPendenciaFinalizar), usuario.getUsuarioDt(), // jvosantos - 12/12/2019 15:20 - Usar o NE
					fabrica);

			fabrica.finalizarTransacao();

		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	// jvosantos - 21/01/2020 18:41 - Método para salvar pré analise de verificar possivel erro material
	private void salvarPreAnaliseVerificarPossivelErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario)
			throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();

		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		String idPendenciaFinalizar = finalizacaoVoto.getIdPendencia();

		try {
			fabrica.iniciarTransacao();

			PendenciaDt pendenciaDt = pendenciaNe.consultarId(finalizacaoVoto.getIdPendencia());
			PendenciaDt pendenciaAntiga = new PendenciaDt(); // jvosantos - 12/12/2019 15:14 - Copiar pendencia atual para pendencia antiga para não perder o ID
			pendenciaAntiga.copiar(pendenciaDt);

			salvarPendenciaVerificarPossivelErroMaterial(finalizacaoVoto, usuario, pendenciaDt, fabrica);

			new AudienciaProcessoPendenciaNe().salvar(pendenciaDt.getId(),
					finalizacaoVoto.getAudienciaProcesso().getId(), fabrica);

			criarVotoVerificarErroMaterial(finalizacaoVoto, fabrica, pendenciaDt, pendenciaAntiga);

			setInfoPendenciaFinalizar(pendenciaNe.consultarId(idPendenciaFinalizar), usuario.getUsuarioDt(), // jvosantos - 12/12/2019 15:20 - Usar o NE
					fabrica);

			fabrica.finalizarTransacao();

		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	/**
	 * Finaliza pendencia atual e gera uma pendencia filha com seus arquivos
	 * @throws Throwable 
	 */
	private void salvarPendenciaProclamacao(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario, PendenciaDt pendenciaDt, FabricaConexao fabrica) throws Exception {
		
		PendenciaNe pendenciaNe = new PendenciaNe();

		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		
		UsuarioDt usuarioDt = usuario.getUsuarioDt();
		
		pendenciaDt = preenchePendenciaStatusCodigo(finalizacaoVoto, pendenciaDt, PendenciaStatusDt.ID_EM_ANDAMENTO, PendenciaStatusDt.ID_PRE_ANALISADA); // jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
		pendenciaDt = preencheUsuarioLogPendPai(pendenciaDt, usuarioDt); // jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo

		List<ArquivoDt> arquivos = montarArquivo(finalizacaoVoto, usuario);	

		pendenciaNe.gerarPendenciaFilha(pendenciaDt,
				pendenciaResponsavelNe.consultarResponsaveis(pendenciaDt.getId()), null, arquivos,
				false, false, false, fabrica);
	}

	//TODO(jvosantos): Precisa?
	private void salvarPendenciaVerificarPossivelErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario, PendenciaDt pendenciaDt, FabricaConexao fabrica) throws Exception {
		
		PendenciaNe pendenciaNe = new PendenciaNe();

		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		
		UsuarioDt usuarioDt = usuario.getUsuarioDt();
		
		pendenciaDt = preenchePendenciaStatusCodigo(finalizacaoVoto, pendenciaDt, PendenciaStatusDt.ID_PRE_ANALISADA);
		pendenciaDt = preencheUsuarioLogPendPai(pendenciaDt, usuarioDt);
		pendenciaDt = preencheAguardandoAssinaturaCodigoTemp(finalizacaoVoto, pendenciaDt);

		List<ArquivoDt> arquivos = montarArquivo(finalizacaoVoto, usuario);	

		pendenciaNe.gerarPendenciaFilha(pendenciaDt,
				pendenciaResponsavelNe.consultarResponsaveis(pendenciaDt.getId()), new ArrayList<>(), arquivos,
				false, false, false, fabrica);
	}

	// jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
	private PendenciaDt preencheUsuarioLogPendPai(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) {
		pendenciaDt.setId_UsuarioLog(usuarioDt.getId());
		pendenciaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		pendenciaDt.setId_PendenciaPai(pendenciaDt.getId());
		
		return pendenciaDt;
	}
	
	// jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
	private PendenciaDt preencheAguardandoAssinaturaCodigoTemp(FinalizacaoVotoSessaoDt finalizacaoVoto, PendenciaDt pendenciaDt) {
		pendenciaDt.setCodigoTemp(finalizacaoVoto.isAguardandoAssinatura() ? String.valueOf(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP) : null);
		return pendenciaDt;
	}

	// jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
	private PendenciaDt preenchePendenciaStatusCodigo(FinalizacaoVotoSessaoDt finalizacaoVoto, PendenciaDt pendenciaDt, int codigoPreAnalise, int codigoAguardandoAssinatura) throws Exception {
		return preenchePendenciaStatusCodigo(finalizacaoVoto, pendenciaDt, finalizacaoVoto.isAguardandoAssinatura() ? codigoAguardandoAssinatura : codigoPreAnalise );
	}

	// jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
	private PendenciaDt preenchePendenciaStatusCodigo(FinalizacaoVotoSessaoDt finalizacaoVoto, PendenciaDt pendenciaDt, int status) throws Exception {
		PendenciaStatusNe pendenciaStatusNe = new PendenciaStatusNe(); // jvosantos - 12/12/2019 15:14 - Evitar criar o NE duas vezes
		
		pendenciaDt.setId_PendenciaStatus(pendenciaStatusNe.consultarPendenciaStatusCodigo(status).getId());
		
		return pendenciaDt;
	}

	// jvosantos - 12/12/2019 15:19 - Adicionar referencia a pendencia antiga ("atual"), para apagar o voto antigo
	// jsantonelli 06/12/2019 14:48 - altera fluxo para inserir somente voto proclamacao decisao
	private void criarVotoProclamacaoDecisao(FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica, PendenciaDt pendenciaDt, PendenciaDt pendenciaAntigaDt)
			throws Exception {
		criarVoto(finalizacaoVoto, fabrica, pendenciaDt, pendenciaAntigaDt, VotoTipoDt.PROCLAMACAO_DECISAO); // jvosantos - 21/01/2020 17:43 - Extrair trecho
	}
	
	private void criarVotoVerificarErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica, PendenciaDt pendenciaDt, PendenciaDt pendenciaAntigaDt)
			throws Exception {
		criarVoto(finalizacaoVoto, fabrica, pendenciaDt, pendenciaAntigaDt, VotoTipoDt.ANALISE_ERRO_MATERIAL);
	}

	private void criarVoto(FinalizacaoVotoSessaoDt finalizacaoVoto, FabricaConexao fabrica, PendenciaDt pendenciaDt,
			PendenciaDt pendenciaAntigaDt, int votoTipoCodigo) throws Exception {
		VotoDt voto = new VotoDt();
		
		// jvosantos - 12/12/2019 15:19 - Apagar o voto que corresponde a pendencia antiga ou a nova (não deve acontecer).
		voto = Optional.ofNullable(Optional.ofNullable(consultarVoto(pendenciaAntigaDt.getId())).orElse(consultarVoto(pendenciaDt.getId()))).orElse(voto);
		voto.setPendenciaDt(pendenciaDt);
		voto.setAudienciaProcessoDt(finalizacaoVoto.getAudienciaProcesso());
		voto.setIdVotoTipo(consultarIdVotoTipo(Integer.toString(votoTipoCodigo)));

		inserirVotoDesativandoAntigo(voto, fabrica);
	}

	private List<ArquivoDt> montarArquivo(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario) throws Exception {

		if (finalizacaoVoto.getTipoAnalise() != FinalizacaoVotoSessaoDt.NORMAL) {
			return Arrays.asList(montarArquivoPreAnalise(usuario, null, finalizacaoVoto.getConteudoArquivoAdiado(),
					String.valueOf(ArquivoTipoDt.RELATORIO_VOTO), "online.html"),
					montarArquivoConfiguracaoPreAnaliseFinalizacao(finalizacaoVoto, buscarArquivoConfiguracao(finalizacaoVoto, usuario), usuario));
		}

		return Arrays.asList(this.montarArquivoVoto(finalizacaoVoto, usuario),
				this.montarArquivoEmenta(finalizacaoVoto, usuario),
				montarArquivoConfiguracaoPreAnaliseFinalizacao(finalizacaoVoto, this.buscarArquivoConfiguracao(finalizacaoVoto, usuario), usuario));
	}

	private ArquivoDt montarArquivoEmenta(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario) throws Exception {
		// jvosantos - 03/02/2020 18:52 - Correção para copiar conteudo da ementa original quando não houver ementa
		String conteudo = finalizacaoVoto.getConteudoArquivoEmenta();
		
		if(StringUtils.isEmpty(conteudo)) {
			
			ArquivoNe arquivoNe = new ArquivoNe();
			AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarIdCompleto(finalizacaoVoto.getAudienciaProcesso().getId()); // NOTE(jvosantos): Não trazia ID_PEND_EMENTA
			ArquivoDt ementaOriginal;
			try {
				ementaOriginal = (ArquivoDt) new PendenciaArquivoNe()
						.consultarArquivosPendencia(audienciaProcessoDt.getId_PendenciaEmentaRelator()).stream()
						.filter(arq -> StringUtils.equals(((ArquivoDt) arq).getArquivoTipoCodigo(), String.valueOf(ArquivoTipoDt.EMENTA)))
						.map(arq -> {
							try {
								return arquivoNe.consultarId(((ArquivoDtGen) arq).getId());
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						})
						.filter(arq -> StringUtils.isEmpty(arq.getUsuarioAssinador()))
						.findFirst().orElseThrow(() -> new MensagemException(
								"O conteúdo da ementa está vazio e não foi possível encontrar a ementa original."));
			} catch (Throwable e) {
				throw new Exception(e);
			}

			LogDt logDt = new LogDt("AUDI_PROC", finalizacaoVoto.getIdAudienciaProcesso(), usuario.getId_Usuario(), usuario.getIpComputadorLog(), String.valueOf(LogTipoDt.Informacao), null, "Ementa estava vazia na Proclamação da Decisão da AUDI_PROC de ID = (" + finalizacaoVoto.getIdAudienciaProcesso() + ") foi preenchida com o conteúdo do arquivo ID = (" + ementaOriginal.getId() + ").");
			
			new LogNe().salvar(logDt);
			
			conteudo = new String(ementaOriginal.getConteudoSemAssinar());			
		}

		return montarArquivoPreAnalise(usuario, null, conteudo, String.valueOf(ArquivoTipoDt.EMENTA), "online.html"); // jvosantos - 03/02/2020 19:00 - Usar TipoDt no lugar de "magic number"
	}

	private ArquivoDt montarArquivoVoto(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario) {

		return montarArquivoPreAnalise(usuario, null, finalizacaoVoto.getConteudoArquivoVoto(), String.valueOf(ArquivoTipoDt.RELATORIO_VOTO), "online.html"); // jvosantos - 03/02/2020 19:00 - Usar TipoDt no lugar de "magic number"
	}

	private ArquivoDt buscarArquivoConfiguracao(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario)
			throws Exception {

		List<PendenciaArquivoDt> lista = Optional
				.ofNullable(consultarArquivoFinalizarVotoPreAnalise(finalizacaoVoto.getIdPendencia(), usuario))
				.orElse(Collections.emptyList());

		ArquivoDt arquivoDt = lista.stream()
			.filter(pendenciaArquivo -> Funcoes.StringToInt(pendenciaArquivo.getArquivoDt()
					.getArquivoTipoCodigo()) == ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)
			.findAny().map(PendenciaArquivoDt::getArquivoDt).orElse(null);
		
		if(arquivoDt != null) arquivoDt.setId("");
		
		return arquivoDt;
	}
	
	public void atualizaVoto(FinalizacaoVotoSessaoDt finalizacaoVoto,
			UsuarioNe usuario,
			List<PendenciaArquivoDt> listaArquivos) throws Exception {
		    salvarPreAnaliseFinalizarVoto(finalizacaoVoto, usuario);
	}
	
	//TODO(jvosantos): Remover
	public void atualizaVerificarPossivelErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto,
			UsuarioNe usuario,
			List<PendenciaArquivoDt> listaArquivos) throws Exception {
			salvarPreAnaliseVerificarPossivelErroMaterial(finalizacaoVoto, usuario);
	}

	private void salvarVotoSemPreAnalise(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario) throws Exception {
		VotoDt voto = new VotoDt();
		PendenciaDt pendenciaDt = new PendenciaNe().consultarId(finalizacaoVoto.getIdPendencia());
		voto = Optional.ofNullable(consultarVoto(pendenciaDt.getId())).orElse(new VotoDt());
		voto.setPendenciaDt(pendenciaDt);
		voto.setAudienciaProcessoDt(finalizacaoVoto.getAudienciaProcesso());
		voto.setIdVotoTipo(consultaVotoRelator(finalizacaoVoto));

		if (finalizacaoVoto.isJulgamentoAdiado()) {
			voto
					.setArquivo(
							montarArquivoPreAnalise(
									usuario,
									null,
									finalizacaoVoto.getConteudoArquivoAdiado(),
									String.valueOf(ArquivoTipoDt.RELATORIO_VOTO), // jvosantos - 03/02/2020 19:00 - Usar TipoDt no lugar de "magic number"
									"online.html"));
		} else {
			voto
					.setArquivo(
							montarArquivoPreAnalise(
									usuario,
									null,
									finalizacaoVoto.getConteudoArquivoVoto(),
									String.valueOf(ArquivoTipoDt.RELATORIO_VOTO), // jvosantos - 03/02/2020 19:00 - Usar TipoDt no lugar de "magic number"
									"online.html"));
			voto
					.setEmenta(
							montarArquivoPreAnalise(
									usuario,
									null,
									finalizacaoVoto.getConteudoArquivoEmenta(),
									String.valueOf(ArquivoTipoDt.EMENTA), // jvosantos - 03/02/2020 19:00 - Usar TipoDt no lugar de "magic number"
									"online.html"));
		}
		ArquivoDt arquivoDt = montarArquivoConfiguracaoPreAnaliseFinalizacao(finalizacaoVoto, null, usuario);
		voto.setConfiguracao(arquivoDt);

		if (finalizacaoVoto.isAguardandoAssinatura()) {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
		} else {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
		}

		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			fabrica.iniciarTransacao();

			salvarArquivoVoto(voto, usuario.getUsuarioDt(), fabrica);

			inserirVotoDesativandoAntigo(voto, fabrica);
			new PendenciaNe().alterarStatus(pendenciaDt);

			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}

	}

	public List<PendenciaArquivoDt> consultarArquivoFinalizarVotoPreAnalise(String idPendencia, UsuarioNe usuario)
			throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt = new PendenciaNe().consultarId(idPendencia);
		// jvosantos - 25/10/2019 10:11 - Desfazer correção
		// jvosantos - 16/10/2019 18:23 - Correção para trazer os arquivos, usado na tela de Proclamação da Decisão
		List<PendenciaArquivoDt> listArquivoDt = new PendenciaArquivoNe()
				.consultarArquivosPendencia(pendenciaDt, usuario, true, false, null);
		return listArquivoDt;

	}

	private VotoDt consultarVoto(String idPendencia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarVoto(idPendencia, obFabricaConexao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	private VotoDt consultarVoto(String idPendencia, FabricaConexao obFabricaConexao) throws Exception {
		VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());

		VotoDt voto = obPersistencia.consultarIdPendencia(idPendencia);
		return voto;
	}

	private String consultaVotoRelator(FinalizacaoVotoSessaoDt finalizacaoVoto) throws Exception {
		if (finalizacaoVoto.isJulgamentoAdiado()) {
			return consultarIdVotoTipo(Integer.toString(VotoTipoDt.JULGAMENTO_ADIADO));
		}
		if (finalizacaoVoto.isDivergente()) {
			return consultarIdVotoTipo(Integer.toString(VotoTipoDt.ACOMPANHA_DIVERGENCIA));
		}
		// jvosantos - 29/07/2019 11:44 - Seta o tipo de voto para "Verificar Resultado da Votação" caso seja uma finalização de Verificar Resultado da Votação
		if (finalizacaoVoto.isVerificarResultadoVotacao()) {
			return consultarIdVotoTipo(Integer.toString(VotoTipoDt.VERIFICAR_RESULTADO_VOTACAO));
		}
		return consultarIdVotoTipo(Integer.toString(VotoTipoDt.ACOMPANHA_RELATOR));
	}

	public String consultarIdVotoTipo(String codigo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());

			String idVotoTipo = obPersistencia.consultarIdVotoTipo(codigo);
			return idVotoTipo;

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public VotoTipoDt consultarVotoTipo(String codigo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());

			return obPersistencia.consultarVotoTipo(codigo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultaConteudoArquivo(ArquivoDt arquivoDt) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			ArquivoDt dadoArquivo = new ArquivoNe().consultarId(arquivoDt.getId());
			byte[] data = dadoArquivo.getConteudoSemAssinar();
			if (data == null) return StringUtils.EMPTY;
			return new String(data, "UTF-8");
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	// jvosantos - 04/06/2019 10:09 - Overload do metodo de desfazerPreAnalise para não precisar do boolean renovar
	public void desfazerPreAnalise(String[] ids, UsuarioNe usuario) throws Exception {
		desfazerPreAnalise(ids, usuario, false);
	}

	// jvosantos - 25/07/2019 14:07 - Overload do metodo de desfazerPreAnalise para não precisar do boolean verificar resultado
	public void desfazerPreAnalise(String[] ids, UsuarioNe usuario, boolean renovar) throws Exception {
		desfazerPreAnalise(ids, usuario, renovar, false);
	}

	// jvosantos - 04/06/2019 10:22 - Adição boolean renovar para armazenar corretamente quando na pasta de Renovar ou Modificar Voto
	public void desfazerPreAnalise(String[] ids, UsuarioNe usuario, boolean renovar, boolean verificarResultado) throws Exception {
		// TODO(jvosantos): Alterar para criar pendência filha
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		try {
			fabrica.iniciarTransacao();
			for (String id : ids) {

				PendenciaDt pendenciaDt = pendenciaNe.consultarId(id);
				List<PendenciaArquivoDt> arquivos = getArquivosPendenciaOuListaVazia(pendenciaDt, pendenciaNe);
				if(!renovar) {
					for (PendenciaArquivoDt arquivo : arquivos) {
						arquivo = setDadosLog(arquivo, usuario);
		
						pendenciaArquivoNe.excluir(arquivo, fabrica);
						arquivoNe.excluir(arquivo.getArquivoDt(), fabrica);
					}
					excluirVotoIdPendencia(id, fabrica);

					alterarStatusPendencia(id, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
				}else {
					pendenciaDt.setCodigoTemp(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
					pendenciaNe.AlterarCodigoTempPendencia(pendenciaDt, fabrica);
				}
			}
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	

	// jvosantos - 21/01/2020 18:43 - Método para desfazer pre analise
	public void desfazerPreAnaliseErroMaterial(String[] ids, UsuarioNe usuario) throws Exception {
		// TODO(jvosantos): Alterar para criar pendência filha
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		try {
			fabrica.iniciarTransacao();
			for (String id : ids) {

				PendenciaDt pendenciaDt = pendenciaNe.consultarId(id);
				List<PendenciaArquivoDt> arquivos = getArquivosPendenciaOuListaVazia(pendenciaDt, pendenciaNe);
				
				for (PendenciaArquivoDt arquivo : arquivos) {
					arquivo = setDadosLog(arquivo, usuario);
	
					pendenciaArquivoNe.excluir(arquivo, fabrica);
					arquivoNe.excluir(arquivo.getArquivoDt(), fabrica);
				}
				
				excluirVotoIdPendencia(id, fabrica);

				alterarStatusPendencia(id, PendenciaStatusDt.ID_EM_ANDAMENTO);
			}
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
	private List<PendenciaArquivoDt> getArquivosPendenciaOuListaVazia(PendenciaDt pendenciaDt, PendenciaNe pendenciaNe)
			throws Exception {
		return Optional.ofNullable(pendenciaNe.consultarArquivos(pendenciaDt, null)).orElse(new ArrayList<PendenciaArquivoDt>());
	}

	// jvosantos - 21/01/2020 17:43 - Extrair trecho repetitivo
	private PendenciaArquivoDt setDadosLog(PendenciaArquivoDt pendenciaArquivoDt, UsuarioNe usuario) {
		pendenciaArquivoDt.setIpComputadorLog(usuario.getIpComputadorLog());
		pendenciaArquivoDt.setId_UsuarioLog(usuario.getId_Usuario());
		
		if(pendenciaArquivoDt.getArquivoDt() != null) {
			pendenciaArquivoDt.getArquivoDt().setIpComputadorLog(usuario.getIpComputadorLog());
			pendenciaArquivoDt.getArquivoDt().setId_UsuarioLog(usuario.getId_Usuario());
		}
		
		return pendenciaArquivoDt; 
	}

	private void excluirVotoIdPendencia(String id, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		votoPs.excluirIdPendencia(id);
	}

	// jvosantos - 04/06/2019 10:22 - Overload do metodo de desfazerAssinatura para não precisar do boolean renovar
	public void desfazerAssinatura(String[] ids) throws Exception {
		desfazerAssinatura(ids, false);
	}
	
	// jvosantos - 04/06/2019 10:22 - Adição boolean renovar para armazenar corretamente quando na pasta de Renovar ou Modificar Voto
	public void desfazerAssinatura(String[] ids, boolean renovar) throws Exception {
		for (String id : ids) {
			if(!renovar) alterarStatusPendencia(id, PendenciaStatusDt.ID_EM_ANDAMENTO); else alterarStatusPendenciaRenovar(id, PendenciaStatusDt.ID_PRE_ANALISADA);
		}
	}

	private void alterarStatusPendencia(String id, int status) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt.setId(id);
		pendenciaDt.setPendenciaStatusCodigo(String.valueOf(status));
		new PendenciaNe().alterarStatus(pendenciaDt);
	}

	// jvosantos - 04/06/2019 10:22 - Altera o "status" da pendencia de renovar (que é no codigo temp)
	private void alterarStatusPendenciaRenovar(String id, int status) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt.setId(id);
		pendenciaDt.setCodigoTemp(String.valueOf(status));
		new PendenciaNe().AlterarCodigoTempPendencia(pendenciaDt);
	}

	public String consultarIdRelator(String idAudiProc) throws Exception {
		FabricaConexao fabricaConexao = null;

		try {
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabricaConexao.getConexao());
			return ps.consultarIdRelator(idAudiProc);

		} finally {
			fabricaConexao.fecharConexao();
		}
	}

	public List consultarSessaoConhecimento(UsuarioNe usuario, String procNumero) throws Exception {
		return consultarPendenciasVoto(
				usuario,
				procNumero,
				PendenciaTipoDt.SESSAO_CONHECIMENTO,
				PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	}
	
	public List consultarSessaoImpedimento(UsuarioNe usuario, String procNumero) throws Exception {
		return consultarPendenciasVoto(
				usuario,
				procNumero,
				PendenciaTipoDt.VERIFICAR_IMPEDIMENTO,
				PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	}
	
	private List consultarPendenciasVoto(UsuarioNe usuario, String procNumero, int tipo, int status) throws Exception {
		return consultarPendenciasVoto(usuario, procNumero, tipo, status, null); 
	}

	// lrcampos - 19/03/2020 10:09 - Adiciona parametro para realizar filtro por serventia
	private List consultarPendenciasVoto(UsuarioNe usuario, String procNumero, int tipo, int status, String idServentiaFiltro) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			String idServentiaCargo = getServentiaCargoOuServentiaCargoChefe(usuario); // jvosantos - 15/01/2020 12:11 - Usar método extraido
			List<VotoSessaoLocalizarDt> lista = votoPs
					.consultarPendeciasVoto(idServentiaCargo, procNumero, tipo, status, idServentiaFiltro);

			consultarVotosRelacionados(usuario, lista, fabrica); // jvosantos - 20/08/2019 15:00 - Adicionar fabrica na chamada, já que temos uma instanciada
			verificarPrazoPendencias(lista, fabrica);

			// jsantonelli - 21/10/2019: adicionando informacoes sobre classificadores
			lista = ordenaClassificadores(lista, fabrica);
			return lista;
		} finally {
			fabrica.fecharConexao();
		}
	}

	private void verificarPrazoPendencias(List<VotoSessaoLocalizarDt> lista, FabricaConexao fabrica) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		for (VotoSessaoLocalizarDt voto : lista) {
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(voto.getIdPendencia(), fabrica);
			if (StringUtils.isNotEmpty(pendenciaDt.getDataLimite())) {
				LocalDateTime data = parseData(pendenciaDt.getDataLimite());
				// LocalDateTime data = parseData("10/12/2018 23:59:59");
				LocalDateTime atual = LocalDateTime.now();
				voto.setPodeVotar(data.isAfter(atual));
				voto.setPrazoVotacao(voto.isPodeVotar() ? pendenciaDt.getDataLimite() : "Prazo Expirado");

				if (voto.isPodeVotar()) {
					long dias = ChronoUnit.DAYS.between(atual, data);
					long horas = ChronoUnit.HOURS.between(atual, data) % 24;
					voto.setTempoRestante(String.format("%d dia(s) %d hora(s)", dias, horas));
				}
			}
		}
	}

	public boolean verificaPrazoPedidoSustentacaoOral(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao fabrica = null;
		boolean podePedirSusOral = false;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			AudienciaProcessoDt audiProcDt = audienciaProcessoNe
					.consultarAudienciaProcessoPendente(processoDt.getId(), usuarioDt);
			AudienciaDt audienciaDt = new AudienciaNe().consultarId(audiProcDt.getId_Audiencia());
			boolean cabeSustentacaoOral = audienciaProcessoNe.consultarPodeSustentacaoOral(audiProcDt.getId());
			if (cabeSustentacaoOral && StringUtils.isNotEmpty(audienciaDt.getDataAgendada())) {
				//lrcampos 12/07/2019 * Alterado o pedido de S.O para 24 horas antes da sessão
				LocalDateTime dataUmDiaAntesSessao = parseData(audienciaDt.getDataAgendada()).minusDays(1);
				LocalDateTime atual = LocalDateTime.now();
				podePedirSusOral = dataUmDiaAntesSessao.isAfter(atual);
				return podePedirSusOral;

			}
			return podePedirSusOral;
		} finally {
			fabrica.fecharConexao();
		}
	}
	

	/*public AdiarJulgamentoDt consultarAdiarJulgamento(String[] processos, UsuarioNe usuario) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		AdiarJulgamentoDt adiamento = new AdiarJulgamentoDt();

		for (String processo : processos) {
			ProcessoDt processoDt = new ProcessoDt();
			processoDt = processoNe.consultarId(processo);
			processoDt.setProcessoTipo(new ProcessoParteNe().consultaClasseProcesso(processo));
			adiamento.getProcessos().add(processoDt);

		}
		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
		String idArquivoTipo = arquivoTipoNe.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.DESPACHO));
		ArquivoTipoDt arquivoTipoDt = arquivoTipoNe.consultarId(idArquivoTipo);
		adiamento.setArquivoTipo(arquivoTipoDt);

		return adiamento;
	}*/
	
	public int consultarQuantidadePendenciasConhecimento(String idServentiaCargo) throws Exception {
		return consultarQuantidadePendenciasVoto(idServentiaCargo,	PendenciaTipoDt.SESSAO_CONHECIMENTO, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	}

	public int consultarQuantidadePendenciasConhecimentoPreAnalisadas(String idServentiaCargo) throws Exception {
		return consultarQuantidadePendenciasVoto(idServentiaCargo,	PendenciaTipoDt.SESSAO_CONHECIMENTO, PendenciaStatusDt.ID_EM_ANDAMENTO);
	}

	public int consultarQuantidadePendenciaVerificarImpedimento(String idServentiaCargo) throws Exception {
		return consultarQuantidadePendenciasVoto(idServentiaCargo, PendenciaTipoDt.VERIFICAR_IMPEDIMENTO, PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	}

	public int consultarQuantidadePendenciaVerificarImpedimentoPreAnalisadas(String idServentiaCargo) throws Exception {
		return consultarQuantidadePendenciasVoto(idServentiaCargo, PendenciaTipoDt.VERIFICAR_IMPEDIMENTO, PendenciaStatusDt.ID_EM_ANDAMENTO);
	}

	private int consultarQuantidadePendenciasVoto(String idServentiaCargo, int tipo, int status) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarQuantidadePendenciasVoto(idServentiaCargo, tipo, status);

		} finally {
			fabrica.fecharConexao();
		}
	}

	public void salvarConhecimento(AnaliseVotoSessaoDt analise, UsuarioNe usuario) throws Exception {
		ArquivoTipoNe arquivoNe = new ArquivoTipoNe();
		String idArquivoTipo = arquivoNe.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.OUTROS));
		analise.setIdArquivoTipo(idArquivoTipo);
		VotoDt voto = analise.getVoto();
		voto.setIdVotoTipo(consultarIdVotoTipo(String.valueOf(VotoTipoDt.OBSERVACAO)));
		voto.setVotoTipoCodigo(String.valueOf(VotoTipoDt.OBSERVACAO));
		salvarPreAnalise(analise, usuario);
	}

	public ArquivoTipoDt consultarArquivoTipo(int arquivoTipoCodigo, FabricaConexao fabrica) throws Exception {
		ArquivoTipoNe arquivoNe = new ArquivoTipoNe();
		String idArquivoTipo = arquivoNe.consultarIdArquivoTipo(String.valueOf(arquivoTipoCodigo), fabrica);
		return arquivoNe.consultarId(idArquivoTipo, fabrica);
	}

	public List consultarSessaoConhecimentoPreAnalise(UsuarioNe usuario, String procNumero) throws Exception {
		return consultarPendenciasVoto(
				usuario,
				procNumero,
				PendenciaTipoDt.SESSAO_CONHECIMENTO,
				PendenciaStatusDt.ID_EM_ANDAMENTO);
	}

	public List consultarSessaoImpedimentoPreAnalise(UsuarioNe usuario, String idProcesso) throws Exception {
		return consultarPendenciasVoto(
				usuario,
				idProcesso,
				PendenciaTipoDt.SESSAO_CONHECIMENTO,
				PendenciaStatusDt.ID_EM_ANDAMENTO);
	}

	public void finalizarVotosPendentes(String id, UsuarioDt usuario) throws Exception {
		finalizarVotosPendentes(new AudienciaProcessoNe().consultarId(id), usuario);
	}

	// jvosantos - 04/10/2019 18:34 - Overload para usar fabrica já instanciada
	public void finalizarVotosPendentes(AudienciaProcessoDt audienciaProcesso, UsuarioDt usuario) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			finalizarVotosPendentesGerarFinalizacao(audienciaProcesso, usuario, fabrica);
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	// jvosantos - 01/11/2019 10:03 - Renomear método para separar funcionalidade
	// jvosantos - 04/10/2019 18:34 - Overload para usar fabrica já instanciada
	public void finalizarVotosPendentesGerarFinalizacao(AudienciaProcessoDt audienciaProcesso, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		if(finalizarVotosPendentes(audienciaProcesso, usuario, fabrica))
			verificarGerarFinalizacao(audienciaProcesso, false, usuario, fabrica);
		
	}
	
	// jvosantos - 01/11/2019 10:04 - Separar métodos para reutilizar uma funcionalidade
	public boolean finalizarVotosPendentes(AudienciaProcessoDt audienciaProcesso, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		List<PendenciaDt> pendenciasVoto = new PendenciaNe().consultarPendenciasAudienciaProcessoPorListaTipo(audienciaProcesso.getId(),
				PendenciaTipoDt.VOTO_SESSAO);

		if (CollectionUtils.isNotEmpty(pendenciasVoto)) {
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			String idVotoTipo = votoPs.consultarIdVotoTipo(String.valueOf(VotoTipoDt.PRAZO_EXPIRADO));

			for (PendenciaDt pendencia : pendenciasVoto) {
				pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_NAO_CUMPRIDA));
				setInfoPendenciaFinalizar(pendencia, usuario, fabrica);
				VotoDt votoDt = Optional
							.ofNullable(votoPs.consultarIdPendencia(pendencia.getId()))
						.orElse(new VotoDt());
				votoDt.setPendenciaDt(pendencia);
				votoDt.setIdVotoTipo(idVotoTipo);
				votoDt.setAudienciaProcessoDt(audienciaProcesso);
				inserirVotoDesativandoAntigo(votoDt, fabrica);
			}
			return true;
		}
		return false;
	}

	public List<String> consultarIdsIntegrantesSessaoPorTipo(String idAudienciaProcesso, List<Integer> tipos)
			throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarIdIntegrantesSessaoPorTipo(idAudienciaProcesso, tipos);
		} finally {
			fabrica.fecharConexao();
		}
	}

	public List<VotanteDt> consultarIntegrantesSessaoPorTipo(String idAudienciaProcesso, List<Integer> tipos)
			throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarIntegrantesSessaoPorTipo(idAudienciaProcesso, tipos);
		} finally {
			fabrica.fecharConexao();
		}
	}

	public ArquivoDt montarArquivoConfiguracaoPreAnaliseFinalizacao(FinalizacaoVotoSessaoDt finalizacao,
			ArquivoDt arquivoDt,
			UsuarioNe usuario) throws Exception {
		StringBuilder configuracao = new StringBuilder();

		if (arquivoDt == null)
			arquivoDt = new ArquivoDt();

		configuracao
				.append(finalizacao.getAudienciaProcesso().getAudienciaProcessoStatusCodigoTemp())
				.append(Configuracao.SEPARDOR03)
				.append(finalizacao.getTipoAnalise())
				.append(Configuracao.SEPARDOR03)
				.append(finalizacao.isAlterarVotoEmenta())
				.append(Configuracao.SEPARDOR03)
				.append(finalizacao.isAcompanhaDivergencia())
				.append(Configuracao.SEPARDOR03);
		
		String idArquivoTipo = new ArquivoTipoNe()
				.consultarIdArquivoTipo(String.valueOf(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE));

		return montarArquivoPreAnalise(usuario, arquivoDt, configuracao.toString(), idArquivoTipo, "Configuração");
	}

	public boolean consultarSessaoVirtual(String idProcesso) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());

			return votoPs.verificarSessaoVirtual(idProcesso) != null;

		} finally {
			fabrica.fecharConexao();
		}

	}

	public String pendenciaAbertasAdvSustentacaoOral(String idProcesso, String idUsuLogado, String idServentia, Boolean isSecretario, String idServentiaProc)
			throws Exception {
		FabricaConexao fabrica = null;
		String pendenciaTipoAdv = "";
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			pendenciaTipoAdv = votoPs.pendenciaAbertasAdvSustentacaoOral(idProcesso, idUsuLogado, idServentia, isSecretario, idServentiaProc);

			return pendenciaTipoAdv;
		} finally {
			fabrica.fecharConexao();
		}

	}

	public String pendenciaAbertasAdvSustentacaoOralIdPend(String idProcesso, String idUsuLogado, String idServentia)
			throws Exception {
		FabricaConexao fabrica = null;
		String pendenciaTipoAdv = "";
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			pendenciaTipoAdv = votoPs.pendenciaAbertasAdvSustentacaoOralIdPend(idProcesso, idUsuLogado, idServentia);

			return pendenciaTipoAdv;
		} finally {
			fabrica.fecharConexao();
		}

	}

	public List pendenciaAbertasSecretarioSustentacaoOral(String idProcesso) throws Exception {
		FabricaConexao fabrica = null;
		List pendenciaTipoAdv = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			pendenciaTipoAdv = votoPs.pendenciaAbertasSecretarioSustentacaoOral(idProcesso);

			return pendenciaTipoAdv;
		} finally {
			fabrica.fecharConexao();
		}

	}
	
	// alsqueiroz 27/09/2019 * Criação do método consultarOrdemTurmaJulgadora para ser usado também na funcionalidade AcompanharVotacao
	public String consultarOrdemTurmaJulgadora(String idServentiaProcesso, String idServentiaGabinete, FabricaConexao fabricaConexao) throws Exception {
		return (new VotoPs(fabricaConexao.getConexao())).consultarOrdemTurmaJulgadora(idServentiaProcesso, idServentiaGabinete);
	}

	// alsqueiroz 27/09/2019 * Criação do método consultarPartesRecursoSecundario para ser usado também na funcionalidade AcompanharVotacao
	public List<ProcessoParteDt> consultarPartesRecursoSecundario(String idAudienciaProcesso, FabricaConexao fabricaConexao) throws Exception {
		return (new VotoPs(fabricaConexao.getConexao())).consultarPartesRecursoSecundario(idAudienciaProcesso);
	}

	// alsqueiroz 27/09/2019 * Criação do método consultarPartesRecurso para ser usado também na funcionalidade AcompanharVotacao
	public List<ProcessoParteDt> consultarPartesRecurso(String idAudienciaProcesso, FabricaConexao fabricaConexao) throws Exception {
		return (new VotoPs(fabricaConexao.getConexao())).consultarPartesRecurso(idAudienciaProcesso);
	}

	public ExtratoAtaDt getExtratoAta(AudienciaDt audienciaDt,
			AudienciaMovimentacaoDt audienciaMovimentacaoDt,
			UsuarioDt usuario, Boolean mostraPedidoVista) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		try {
			if(audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada())
				return new ExtratoAtaJulgamentoIniciado(audienciaDt, audienciaMovimentacaoDt, usuario, fabrica).make();
			
			return new ExtratoAta(audienciaDt, audienciaMovimentacaoDt, usuario, fabrica).make();
		} finally {
			fabrica.fecharConexao();
		}
	}

	public void verificarPermissoes(AudienciaDt audienciaDt, FabricaConexao fabrica) throws Exception {
		if (audienciaDt.isVirtual()) {
			List<AudienciaProcessoDt> lista = audienciaDt.getListaAudienciaProcessoDtPautaDia();
			// mrbatista - 04/09/2019 10:00 - Ajuste da conexão.
			for (AudienciaProcessoDt audienciaProcesso : lista) {
				setPrazoExpirado(audienciaProcesso, fabrica);
				setPodeEncaminhar(audienciaProcesso, fabrica);
			}
		}
	}

	// mrbatista - 04/09/2019 10:00 - Sobrecarga de metódo para tratar conexão.
	public void verificarPermissoes(AudienciaDt audienciaDt) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			verificarPermissoes(audienciaDt, fabrica);
		} finally {
			fabrica.fecharConexao();
		}
	}

	private void setPodeEncaminhar(AudienciaProcessoDt audienciaProcesso, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		audienciaProcesso
				.setPodeEncaminhar(
						votoPs
								.isVotacaoNaoIniciada(
										audienciaProcesso.getProcessoDt().getId(),
										audienciaProcesso.getId()));
	}

	private void setPrazoExpirado(AudienciaProcessoDt audienciaProcesso, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		boolean votacaoConcluida = votoPs.isVotacaoConcluida(audienciaProcesso.getId());
		boolean passouPrazo = false;
		audienciaProcesso.setPodeInserirExtrato(votacaoConcluida);
		if (!votacaoConcluida) {
			PendenciaNe pendenciaNe = new PendenciaNe();

			audienciaProcesso.setPodeInserirExtrato(votacaoConcluida);
			List<PendenciaDt> pendencias = pendenciaNe
					.consultarPendenciasProcessoPorTipo(
							audienciaProcesso.getProcessoDt().getId(),
							PendenciaTipoDt.VOTO_SESSAO);
			if (CollectionUtils.isNotEmpty(pendencias)) {

				passouPrazo = pendencias
						.stream()
						.filter(
								(
										pendencia) -> passouPrazo(pendencia.getDataLimite()))
						.findAny()
						.isPresent();
			}
		}
		audienciaProcesso.setVotosPrazoExpirado(passouPrazo);

	}
	public void finalizarPendenciaSusOral(String idPendencia, UsuarioDt usuarioDt) throws Exception {
		finalizarPendenciaTomarConhecimento(idPendencia, usuarioDt);

	}
	
	//mrbatista - 26/09/2019 09:39 - Método que finaliza todas as pendências de S.O.
	public void finalizarPendenciasSO(String idAudiProc, UsuarioDt usuario, FabricaConexao fabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		//List pendencias = pendenciaPs.consultarPendenciasProcessoPorTipo(idProcesso, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		
		List pendencias = pendenciaNe.consultarPendenciasAudienciaProcessoPorListaTipo(idAudiProc, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO, 
				PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA, PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL, PendenciaTipoDt.VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO);
		String data = Funcoes.DataHora(Calendar.getInstance().getTime());
		if(pendencias != null) {
			for(int i = 0; i < pendencias.size(); i++) {
				PendenciaDt pendencia = (PendenciaDt) pendencias.get(i);
				pendencia.setId_UsuarioLog(usuario.getId());
				pendencia.setIpComputadorLog(usuario.getIpComputadorLog());
				pendencia.setDataFim(data);
				pendencia.setDataVisto(data);
				pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
				pendenciaNe.finalizar(pendencia, usuario, fabricaConexao);
			}
    
		}   

		
	}

	public void finalizarPendenciaSusOral(String idPendencia, UsuarioDt usuarioDt, FabricaConexao fabrica) throws Exception {
		finalizarPendenciaTomarConhecimento(idPendencia, usuarioDt, fabrica);

	}

	private boolean passouPrazo(String dataFim) {
		if (StringUtils.isNotEmpty(dataFim)) {
			LocalDateTime data = parseData(dataFim);
			return data.isBefore(LocalDateTime.now());
		}
		return false;
	}

	public String consultarOrdemVotante(String idServentiaCargo, String idAudienciaProcesso) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarOrdemVotante(idServentiaCargo, idAudienciaProcesso);
		} finally {
			fabrica.fecharConexao();
		}
	}

	// mrbatista - 11/10/2019 14:53 - Alterar visibilidade do método
	public void finalizarTodasPendenciasVotacao(AudienciaProcessoDt audiProcDt, UsuarioDt usuario, FabricaConexao conexao)
			throws Exception {
		finalizarPendenciasVotacao(audiProcDt.getId(), usuario, conexao, TIPOS_PENDENCIA_VOTACAO);
	}

	public void finalizarTodasPendenciasVotacao(AudienciaProcessoDt audiProcDt, UsuarioDt usuario, FabricaConexao conexao, List<Integer> listaPendenciasAdicionais)
			throws Exception {
		List<Integer> tipos = Stream.concat(
					Arrays.asList(TIPOS_PENDENCIA_VOTACAO).stream(),
					Optional.ofNullable(listaPendenciasAdicionais).orElse(Collections.emptyList()).stream()
				).collect(Collectors.toList());
		
		finalizarPendenciasVotacao(audiProcDt.getProcessoDt().getId(), audiProcDt.getId(), usuario, conexao, tipos.toArray(new Integer[tipos.size()]));
	}

	private void finalizarPendencias(UsuarioDt usuario, FabricaConexao conexao, List<PendenciaDt> pendencias)
			throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		//lrcampos 04/03/2020 10:49 seta usuario finalizador caso seja vazio ou nulo.
		for (PendenciaDt pendencia : pendencias) {
			if (StringUtils.isEmpty(pendencia.getId_UsuarioFinalizador())) {
				pendencia.setId_UsuarioFinalizador(usuario.getId_UsuarioServentia());
			}
			pendenciaNe.setInfoPendenciaFinalizar(pendencia, usuario, PendenciaStatusDt.ID_CANCELADA, conexao);
		}
	}

	public String getNomeDesembargador(UsuarioNe usuario) throws Exception {
		return "Des.(a) " + usuario.consultarNomeUsuarioServentiaCargo(getIdServentiaCargo(usuario));
	}

	public String getNomeRelator(AudienciaProcessoDt audienciaProcesso, FabricaConexao fabrica) throws Exception {
		return "Relator: Des.(a) " +
				new ServentiaCargoNe().consultarId(audienciaProcesso.getId_ServentiaCargo(), fabrica).getNomeUsuario();
	}

	// jvosantos - 04/06/2019 10:22 - Retorna o nome do redator
	public String getNomeRedator(AudienciaProcessoDt audienciaProcesso, FabricaConexao fabrica) throws Exception {
		return "Redator: Des.(a) " +
				new ServentiaCargoNe().consultarId(audienciaProcesso.getId_ServentiaCargoRedator(), fabrica).getNomeUsuario();
	}

	public String consultarIdUltimaFinalizacao(String idProcesso, FabricaConexao conexao) throws Exception {
		return new VotoPs(conexao.getConexao()).consultarIdUltimaFinalizacao(idProcesso);
	}

	public List<AudienciaDt> consultarSessoesPublica(String dataInicial, String dataFinal, String pagina)
			throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarSessoesPublica(dataInicial, dataFinal, pagina);
		} finally {
			fabrica.fecharConexao();
		}
	}

	public long consultarQuantidadeSessoesPublica(String dataInicial, String dataFinal) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarQuantidadeSessoesPublica(dataInicial, dataFinal);
		} finally {
			fabrica.fecharConexao();
		}
	}

	public List<SessaoVirtualPublicaDt> consultarProcessosSessaoPublica(String idAudiencia, UsuarioNe usuario)
			throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			ProcessoNe processoNe = new ProcessoNe();
			List<SessaoVirtualPublicaDt> list = votoPs.consultarProcessosSessaoPublica(idAudiencia);
			for (SessaoVirtualPublicaDt sessaoVirtualPublicaDt : list) {
				sessaoVirtualPublicaDt.getAudienciaProcesso()
						.setProcessoDt(
								processoNe
										.consultarIdCompleto(sessaoVirtualPublicaDt.getProcessoDt().getId(), fabrica));
				
				List<VotanteDt> integrantes = consultarIntegrantesSessaoPorTipo(
						sessaoVirtualPublicaDt.getAudienciaProcesso().getId(),
						Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.RELATOR));
				if (CollectionUtils.isNotEmpty(integrantes)) {
					integrantes.forEach(integrante -> {
						switch (integrante.getVotanteTipoDt().getVotanteTipoCodigoInt()) {
						case VotanteTipoDt.RELATOR:
							sessaoVirtualPublicaDt.setRelator(integrante);
							break;
						case VotanteTipoDt.MINISTERIO_PUBLICO:
							sessaoVirtualPublicaDt.setMp(integrante);
							break;

						default:
							break;
						}
					});

				}
				List<String> votos = consultarVotosVotantes(
						sessaoVirtualPublicaDt.getIdAudienciaProcesso(),
						usuario,
						fabrica)
								.stream()
								.map(this::textoVotante)
								.collect(Collectors.toList());
				sessaoVirtualPublicaDt.setVotos(votos);
			}
			return list;
		} finally {
			fabrica.fecharConexao();
		}
	}

	public void definirSustentacaoOral(AudienciaDt audienciaDt) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			for (AudienciaProcessoDt audiProc : ((List<AudienciaProcessoDt>) audienciaDt
					.getListaAudienciaProcessoDtAdiadosIniciados())) {
				String id = audiProc.getProcessoDt().getId();
				audiProc.setPermiteSustentacaoOral(votoPs.existeSustentacaoOralDeferida(id));
			}

		} finally {
			fabrica.fecharConexao();
		}
	}

	public List<JulgamentoAdiadoDt> consultarAdiadosOriginadosDaVirtual(String idAudiencia, UsuarioDt usuarioDt) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
		AudienciaDt audienciaDt = audienciaNe.consultarAudienciaCompleta(idAudiencia);
		audienciaDt.setListaAudienciaProcessoDt(audienciaDt.getListaAudienciaProcessoDt().stream().filter((x) -> {
            x.setAdiadaPorSustentacaoOral(isOriginadoSessaoVirtual(x, this));
            return StringUtils.isEmpty(x.getDataMovimentacao());}).collect(Collectors.toList()));
		AudienciaProcessoNe  audienciaProcessoNe = new AudienciaProcessoNe();
		List<AudienciaProcessoDt> adiados = audienciaDt.getListaAudienciaProcessoDtAdiadosIniciados();

		//lrcampos 16/10/2019 17:25 - Lista somente os processos que foram adiados de outro audiencia para a audiencia consultada.
		List<AudienciaProcessoDt> listaAudiencias = audienciaDt.getListaAudienciaProcessoDt();
		String dataAudienciaOriginal = null;
		String idAudienciaOriginal = null;
		
		List<JulgamentoAdiadoDt> lista = new ArrayList<>(adiados.size());
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			for (AudienciaProcessoDt audienciaProcessoDt : adiados) {
				JulgamentoAdiadoDt julgamento = new JulgamentoAdiadoDt();
				ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();
				
				julgamento.setProcessoNumero(processoDt.getProcessoNumero());
				julgamento.setRelator(audienciaProcessoDt.getNomeResponsavel());
				RecursoSecundarioParteDt recurso = votoPs.consultarRecursoSecundarioIdProcesso(processoDt.getId());
				String tipo = audienciaProcessoDt.getProcessoTipo();
				if (recurso != null) {
					tipo += " - " + recurso.getProcessoTipoRecursoSecundario();
				}
				// lrcampos 06/08/2019 * Busca as informações do advogado que pediram S.O na Audiencia
				dataAudienciaOriginal =  audienciaProcessoDt.getDataAudienciaOriginal();
				idAudienciaOriginal = audienciaProcessoDt.getId_Audi_Proc_Origem();
				String idAudienciaAntiga = votoPs.buscaAudienciaVirtualOriginalPJD(processoDt.getId(), dataAudienciaOriginal);
				idAudienciaAntiga = idAudienciaOriginal;
				if(StringUtils.isNotBlank(idAudienciaAntiga)) {
					AudienciaProcessoDt  audienciaProcessoOriginalDt = audienciaProcessoNe.consultarId(idAudienciaAntiga);
					if (audienciaProcessoOriginalDt.getAudienciaProcessoStatusCodigo().equals(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL))) {
						List<JulgamentoAdiadoDt> listaAdvPedidoDeferido = votoPs.solicitantePedidoSustentacaoOralPJD(idAudienciaAntiga);
						julgamento.setClasse(tipo);
	
						//lrcampos 25/11/2019 11:28 - Correção para Listar todos os advogados que pediram S.O 
						for (JulgamentoAdiadoDt julgamentoAdiadoDt : listaAdvPedidoDeferido) {
							if(julgamentoAdiadoDt.getAdvogadoSolicitante() == null) {
								julgamento.setAdvogadoSolicitante(votoPs.advogadoSolicitanteSustentacaoOral(processoDt.getId(), false, true));
								//votoPs.definirDadosPedidoSustencaoOral(processoDt.getId(), julgamentoAdiadoDt);
								
								if(julgamentoAdiadoDt.getAdvogadoSolicitante() == null) {
									julgamentoAdiadoDt.setAdvogadoSolicitante("");
									julgamentoAdiadoDt.setParte("");
									julgamentoAdiadoDt.setDataHoraSolicitacao("");
									}
							}
						}
						julgamento.setAdvsPedidoSOdeferido(listaAdvPedidoDeferido);
						lista.add(julgamento);
					}
				}
			}
			if (CollectionUtils.isEmpty(lista)) {
				throw new MensagemException("Não há processos adiados com sustentação oral");
			}
			return lista;
		} finally {
			fabrica.fecharConexao();
		}
	}

	public List<JulgamentoAdiadoDt> consultarProcessosAdiadosNaSessao(String idAudiencia, UsuarioDt usuarioDt) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
		AudienciaDt audienciaDt = audienciaNe.consultarAudienciaCompleta(idAudiencia);
		List<AudienciaProcessoDt> adiadosSO = new ArrayList<AudienciaProcessoDt>();
		List<AudienciaProcessoDt> listaAudiencias = audienciaDt.getListaAudienciaProcessoDt();
		for (AudienciaProcessoDt audienciaProcessoDt : listaAudiencias) {
			if(audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equals(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL))) {
				adiadosSO.add(audienciaProcessoDt);
			}
		}
		
		List<JulgamentoAdiadoDt> lista = new ArrayList<>(adiadosSO.size());
		FabricaConexao fabrica = null;

		try {
			
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			for (AudienciaProcessoDt audienciaProcessoDt : adiadosSO) {
				JulgamentoAdiadoDt julgamento = new JulgamentoAdiadoDt();
				ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();
				
				julgamento.setProcessoNumero(processoDt.getProcessoNumero());
				julgamento.setRelator(audienciaProcessoDt.getNomeResponsavel());
				RecursoSecundarioParteDt recurso = votoPs.consultarRecursoSecundarioIdProcesso(processoDt.getId());
				String tipo = audienciaProcessoDt.getProcessoTipo();
				if (recurso != null) {
					tipo += " - " + recurso.getProcessoTipoRecursoSecundario();
				}
				// lrcampos 06/08/2019 * Busca as informações do advogado que pediram S.O na Audiencia
				List<JulgamentoAdiadoDt> listaAdvPedidoDeferido = votoPs.solicitantePedidoSustentacaoOralPJD(audienciaProcessoDt.getId());
				
				julgamento.setClasse(tipo);
				
				//lrcampos 25/11/2019 11:28 - Correção para Listar todos os advogados que pediram S.O 
				for (JulgamentoAdiadoDt julgamentoAdiadoDt : listaAdvPedidoDeferido) {
					if(julgamentoAdiadoDt.getAdvogadoSolicitante() == null) {
						julgamento.setAdvogadoSolicitante(votoPs.advogadoSolicitanteSustentacaoOral(processoDt.getId(), false, true));
						votoPs.definirDadosPedidoSustencaoOral(processoDt.getId(), julgamento);
						
						if(julgamentoAdiadoDt.getAdvogadoSolicitante() == null) {
							julgamentoAdiadoDt.setAdvogadoSolicitante("");
							julgamentoAdiadoDt.setParte("");
							julgamentoAdiadoDt.setDataHoraSolicitacao("");
							}
					}
				}
				julgamento.setAdvsPedidoSOdeferido(listaAdvPedidoDeferido);
				lista.add(julgamento);
			}
			if (CollectionUtils.isEmpty(lista)) {
				throw new MensagemException("Não há processos adiados com sustentação oral");
			}

			return lista.stream().filter(julgamentoAdiadoDt ->  julgamentoAdiadoDt.getAdvsPedidoSOdeferido().size() > 0).collect(Collectors.toList());
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	public void setDadosAudienciaCompleta(AudienciaDt audienciaDtCompleta, FabricaConexao fabricaConexao) throws Exception {
		if(audienciaDtCompleta != null) { 
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());			
			for (AudienciaProcessoDt processo: ((List<AudienciaProcessoDt>) audienciaDtCompleta.getListaAudienciaProcessoDt())) {
				processo.setPermiteSustentacaoOral(votoPs.existeSustentacaoOral(processo.getProcessoDt().getId()));
			}
		}
	}

	public List<ArquivoDt> consultarTomarConhecimentoVotoDivergente(List<VotoSessaoLocalizarDt> resultados) throws Exception {
		List<ArquivoDt> arquivos = new ArrayList<ArquivoDt>();
		
		for(VotoSessaoLocalizarDt resultado : resultados) {
			if(StringUtils.equalsIgnoreCase(resultado.getResultado(), "Unanimidade")) continue;
			arquivos.add(consultarArquivoVotoDivergente(resultado.getIdAudienciaProcesso()));
		}
		
		return arquivos;
	}
	

	public ArquivoDt consultarArquivoVotoDivergente(String idAudienciaProcesso) throws Exception {
		ArquivoDt arquivo = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			VotoPs obPersistencia = new  VotoPs(obFabricaConexao.getConexao());
			arquivo = obPersistencia.consultarArquivoVotoDivergente(idAudienciaProcesso);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return arquivo;
	}

	public String consultarNomeVotante(AudienciaProcessoDt audiProc, Integer votanteTipo) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String nome = "";
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			VotoPs obPersistencia = new  VotoPs(obFabricaConexao.getConexao());
			nome = obPersistencia.consultarNomeVotante(audiProc.getId(), votanteTipo);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return nome;
	}


	public void salvaPendenciaImpedimento(String idPendencia, UsuarioNe usuario, boolean impedido) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = pendenciaNe.consultarId(idPendencia);
		ProcessoDt processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo());
		//lrcampos 20/08/2019 * Buscar a Audiencia da tabela AudiProcPend
		PendenciaDt pendenciaGerada = new PendenciaDt();
		AudienciaProcessoPendenciaNe audiProcPendNe = new AudienciaProcessoPendenciaNe();
		String idAudiProc = audiProcPendNe.consultarPorIdPend(pendenciaDt.getId());
		
		if (StringUtils.isEmpty(idAudiProc))
			throw new Exception("Nenhuma AUDI_PROC encontrada para o ID_PEND = "+idPendencia+";");

		try {
			obFabricaConexao.iniciarTransacao();

			if (impedido) {
				pendenciaGerada = pendenciaNe.gerarPendenciaVerificarImpedimentoMinisterioPublico(
						processoDt.getId_Serventia(), usuario.getUsuarioDt(), processoDt.getId());

				audiProcPendNe.salvar(pendenciaGerada.getId(), idAudiProc, obFabricaConexao);
			} else {
				pendenciaNe.gerarPendenciaSessaoConhecimento(usuario.getId_ServentiaCargo(),
						usuario.getUsuarioDt(), processoDt.getId_Processo(), idAudiProc, obFabricaConexao);
			}

			pendenciaDt.setId_UsuarioFinalizador(usuario.getId_UsuarioServentia());
			pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void finalizarPendenciaVerificarImpedimentoMP(String idPendencia, UsuarioNe usuario) throws Exception {
		PendenciaDt pendenciaDt = new PendenciaNe().consultarId(idPendencia);

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 27/09/2019 14:19 - Refatorar código
	public boolean existePedidoSustentacaoOralAberto(AudienciaProcessoDt audiProc) throws Exception {
		FabricaConexao fabrica = null;
		Boolean possuiSusOralAberta = false;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			possuiSusOralAberta = consultaIdPendSusOralAberta(audiProc.getProcessoDt().getId(), PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL) != null;

		} finally {
			fabrica.fecharConexao();
		}
		return possuiSusOralAberta;
	}
	//lrcmapos 03/07/2019 * Criado para buscar as condicoes para mostrar funcionalides na tela de sessão
	public HashMap<String, ArrayList<Boolean>> consultaCondicoesTelaSessao(AudienciaDt audienciaDt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao fabrica = null;
		HashMap<String, ArrayList<Boolean>> PedidoSODeferidaRelatorAberto = new HashMap<String, ArrayList<Boolean>>();
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			PedidoSODeferidaRelatorAberto = votoPs.consultaCondicoesTelaSessao(audienciaDt, usuarioDt);

		} finally {
			fabrica.fecharConexao();
		}
		return PedidoSODeferidaRelatorAberto;
	}

	// jvosantos - 27/09/2019 14:17 - Refatorar
	private int isAcompanhoRelator(VotoDt voto) {
		return (voto.getVotoCodigoInt() == VotoTipoDt.ACOMPANHA_RELATOR) ? 1 : 0;
	}

	// jvosantos - 06/08/2019 14:10 - Adicionar método que consulta as condições para mostrar a opção de incluir Extrato de Ata
	public boolean consultarCondicaoExtratoAta(AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			return votoPs.consultarCondicaoExtratoAta(audienciaProcessoDt);
		} finally {
			if(fabrica != null) fabrica.fecharConexao();
		}
	}

	@Deprecated
	//movido para SustentacaoOralNe
	public String consultaIdPendSusOralAberta(String idProcesso, Integer codigoPendenciaTipo) throws Exception {
		FabricaConexao fabrica = null;
		String idRelatorPendSusOralAberta = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs votoPs = new VotoPs(fabrica.getConexao());
			idRelatorPendSusOralAberta = votoPs.consultaIdPendSusOralAberta(idProcesso, codigoPendenciaTipo);

		} finally {
			fabrica.fecharConexao();
		}
		return idRelatorPendSusOralAberta;
	}

	public void finalizarPendenciaSustentacaoOralDeferidaPeloSecretario(String idProcesso, UsuarioDt usuario)
			throws Exception {

		FabricaConexao fabrica = null;

		try { // mrbatista - 03/09/2019 15:00 - Alterado para melhor controle de conexão.
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			List<ProcessoParteAdvogadoDt> listProcParteAdv = new ProcessoParteAdvogadoNe()
					.consultarAdvogadosProcesso(idProcesso);
			PendenciaNe pendenciaNe = new PendenciaNe();

			for (ProcessoParteAdvogadoDt processoParteAdvogadoDt : listProcParteAdv) {
				UsuarioDt usuDt = new UsuarioNe()
						.consultarAdvogadoServentiaId(processoParteAdvogadoDt.getId_UsuarioServentiaAdvogado());
				pendenciaNe.gerarPendenciaPedidoSustentacaoOralDeferido(usuDt.getId(), null, usuario, idProcesso, fabrica);
			}
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
	}
	public void finalizarPendenciaSustentacaoOralIndeferidaPeloSecretario(String idProcesso, UsuarioDt usuario)
			throws Exception {

		FabricaConexao fabrica = null;

		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			String idPend = consultaIdPendSusOralAberta(idProcesso, PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);

			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(idPend);

			if (pendenciaDt != null) {
				pendenciaNe.gerarPendenciaPedidoSustentacaoOralIndeferida(pendenciaDt.getId_UsuarioCadastrador(), null,
						usuario, pendenciaDt.getId_Processo());
				setInfoPendenciaFinalizar(pendenciaDt, usuario, fabrica);
			}
		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 04/06/2019 10:26 - Retorna o Id do status vencido a partir da proclamacao
	public String consultarIdStatusVencidoProclamacao(String idAudienciaProcesso) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarIdStatusVencidoProclamacao(idAudienciaProcesso);
		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 04/06/2019 10:26 - Retorna o Id da pendencia de apreciados para a audi_proc especifica
	public String consultarIdPendenciaApreciadosPorAudienciaProcesso(String idAudienciaProcesso) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarIdPendenciaApreciadosPorAudienciaProcesso(idAudienciaProcesso);
		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 04/06/2019 10:26 - Retorna a Quantidade de votos para renovar não analisados
	public int consultarQuantidadeVotosRenovarNaoAnalisadas(String idServentiaCargo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarQuantidadeVotosRenovarNaoAnalisadas(idServentiaCargo);

		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 04/06/2019 10:26 - Retorna a Quantidade de votos para renovar pre analisados
	public int consultarQuantidadeVotosRenovarPreAnalisados(String idServentiaCargo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarQuantidadeVotosRenovarPreAnalisados(idServentiaCargo);

		} finally {
			fabrica.fecharConexao();
		}
	}

	// jvosantos - 04/06/2019 10:26 - Retorna a Quantidade de votos para renovar aguardando assinatura
	public int consultarQuantidadeVotosRenovarAguardandoAssinatura(String idServentiaCargo) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			return ps.consultarQuantidadeVotosRenovarAguardandoAssinatura(idServentiaCargo);

		} finally {
			fabrica.fecharConexao();
		}
	}

	//lrcampos 19/08/2019 * Verifica se a serventia é de processos especiais.
	public boolean isServentiaEspecial(String idServentia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean isServentiaEspecial = false;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
			isServentiaEspecial = votoPs.isServentiaEspecial(idServentia);
		} finally {
			obFabricaConexao.fecharConexao();
		}	
		
		return isServentiaEspecial;
	}
		
	// jvosantos - 04/06/2019 10:26 - Retorna os votos para renovar aguardando assinatura
	public List consultarVotosRenovarAguardandoAssinatura(String idServentiaCargo,
			String processoNumero,
			UsuarioNe usuario,
			int pendTipo) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs ps = new VotoPs(fabrica.getConexao());
			List<VotoSessaoLocalizarDt> votos = ps
					.consultarVotosRenovarAguardandoAssinatura(idServentiaCargo, processoNumero, pendTipo);
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
			PendenciaNe pendenciaNe = new PendenciaNe();
			ProcessoNe processoNe = new ProcessoNe();
			for (VotoSessaoLocalizarDt voto : votos) {
				List<PendenciaArquivoDt> arquivos = pendenciaArquivoNe
						.consultarArquivosPendenciaComHash(
								pendenciaNe.consultarId(voto.getIdPendencia()),
								true,
								false,
								usuario, fabrica);  // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada
				for (PendenciaArquivoDt pendenciaArquivoDt : arquivos) {
					int codigo = Funcoes.StringToInt(pendenciaArquivoDt.getArquivoDt().getArquivoTipoCodigo());
					if (codigo != ArquivoTipoDt.EMENTA) {
						voto.setArquivoVoto(pendenciaArquivoDt.getArquivoDt());
					}
				}
			}
			
			// jsantonelli - 21/10/2019: adiciona informações sobre classificadores
			votos = ordenaClassificadores(votos, fabrica);
			
			return votos
					.stream()
					.map((voto) -> calcularPrazo(voto))
					.collect(Collectors.toList());
		} finally {
			fabrica.fecharConexao();
		}
	}
	

	// jvosantos - 04/06/2019 10:26 - Retorna os votos para renovar pre analisados
	public List consultarVotosRenovarPreAnalisados(UsuarioNe usuario, String processoNumero, int tipo, String idServentiaFiltro) throws Exception {
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs ps = new VotoPs(fabrica.getConexao());
			String idServentiaCargo = getServentiaCargoOuServentiaCargoChefe(usuario); // jvosantos - 15/01/2020 12:11 - Usar método extraido
			List<VotoSessaoLocalizarDt> votos = ps.consultarVotosRenovarPreAnalisados(idServentiaCargo, processoNumero, tipo, idServentiaFiltro);
			consultarVotosRelacionados(usuario, votos);
			verificarPrazoPendencias(votos, fabrica);
			
			// jsantonelli - 21/10/2019: adiciona dados dos classificadores
			votos = ordenaClassificadores(votos, fabrica);
			return votos;

		} finally {
			fabrica.fecharConexao();
		}
	}

	//lrcampos 23/03/2020 14:15 - Incluindo filtro por serventia.
	// jvosantos - 04/06/2019 10:26 - Retorna os votos para renovar não analisados
	public List<VotoSessaoLocalizarDt> consultarVotosRenovarNaoAnalisados(UsuarioNe usuario, String processoNumero, int tipo, String idServentiaFiltro) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			VotoPs ps = new VotoPs(fabrica.getConexao());
			String idServentiaCargo = getServentiaCargoOuServentiaCargoChefe(usuario); // jvosantos - 15/01/2020 12:11 - Usar método extraido
			List<VotoSessaoLocalizarDt> votos = ps.consultarVotosRenovarNaoAnalisado(idServentiaCargo, processoNumero, tipo, idServentiaFiltro);
			consultarVotosRelacionados(usuario, votos);
			verificarPrazoPendencias(votos, fabrica);
			// jsantonelli - 21/10/2019: adiciona informações sobre classificadores
			votos = ordenaClassificadores(votos, fabrica);
			return votos;

		} finally {
			fabrica.fecharConexao();
		}
	}

	// lrcampos - 01/07/2019 14:40 - Finaliza Pendencia de Pedido Sustentação Oral Deferido/Verificar Pedido Deferido
	public void finalizaPendenciaDeferidoAbertaSecretario(String idProcesso, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String idPendAbertaDeferidaRelator = consultaIdPendSusOralAberta(idProcesso, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		String idPendAbertaVerificarPedidoDeferido = consultaIdPendSusOralAberta(idProcesso, PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			if (idPendAbertaDeferidaRelator != null) {
				PendenciaDt pendenciaDeferida = new PendenciaNe().consultarId(idPendAbertaDeferidaRelator);
				setInfoPendenciaFinalizar(pendenciaDeferida, usuarioDt, obFabricaConexao);
			}
			if(idPendAbertaVerificarPedidoDeferido != null) {
				PendenciaDt PendenciaVerificarPedidoDeferido = new PendenciaNe().consultarId(idPendAbertaVerificarPedidoDeferido);
				setInfoPendenciaFinalizar(PendenciaVerificarPedidoDeferido, usuarioDt, obFabricaConexao);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public List<VotoSessaoLocalizarDt> consultarAcompanharVotacao(UsuarioNe usuario, String processoNumero) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		List<VotoSessaoLocalizarDt> lista = audienciaProcessoNe
				.consultarAcompanharVotacaoSessaoVirtual(usuario.getId_ServentiaCargo(), processoNumero);
		lista.addAll(Optional.ofNullable(audienciaProcessoNe.consultarAcompanharVotacaoSessaoVirtualErroMaterial(usuario.getId_ServentiaCargo(), processoNumero)).orElse(Collections.emptyList()));
		consultarVotosRelacionadosEmVotacao(usuario, lista, true); // jvosantos - 07/08/2019 18:04 - Adicionar flag para trazer o proprio voto

		return lista;
	}
	// jvosantos - 11/07/2019 18:13 - Adicionar pendência de "Verificar Resultado da Votação"
	public List<VotoSessaoLocalizarDt> consultarVerificarResultadoVotacao(String processoNumero, UsuarioNe usuario,
			int status) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<VotoSessaoLocalizarDt> lista = null;
		try {
			String idServentiaCargo = getIdServentiaCargo(usuario);
			ServentiaCargoDt serventiaCargoDt = new ServentiaCargoNe().consultarId(idServentiaCargo);
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			VotoPs obPersistencia = new VotoPs(obFabricaConexao.getConexao());
			
			lista = obPersistencia.consultarVerificarResultadoVotacao(idServentiaCargo, processoNumero, status);
			
			// jvosantos - 15/07/2019 16:49 - Buscar sessões com prazo expirado
			if (status == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
				List<VotoSessaoLocalizarDt> list = obPersistencia.consultarAguardandoFinalizacaoPrazo(idServentiaCargo,
						processoNumero);
				list.forEach(voto -> voto.setExpirado(true));
				lista.addAll(list);
			}
			
			lista.forEach(sessao -> sessao.setNomeRelator(serventiaCargoDt.getNomeUsuario()));
			consultarVotosRelacionados(usuario, lista, obFabricaConexao); // mrbatista - 03/09/2019 11:00 - Adicionar fabrica na chamada, já que temos uma instanciada

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return configuraPrazoRestante(lista);
	}

	// jvosantos - 11/07/2019 18:14 - Adicionar pendência de "Verificar Resultado da Votação"
	public FinalizacaoVotoSessaoDt consultarVerificarResultadoVotacao(String idAudienciaProcesso, UsuarioNe usuario) throws Exception {
		return consultarFinalizacaoVoto(idAudienciaProcesso, usuario);
	}

	// jvosantos - 11/07/2019 18:14 - Implementar método para finalizar a verificação do resultado da votação
	public void finalizarVerificarResultadoVotacao(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario,
			List<ArquivoDt> listArquivos) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
			boolean inserirArquivos = true;
			PendenciaArquivoDt pendenciaArquivoDt;
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(finalizacaoVoto.getIdPendencia(), fabrica);
			int status = StringToInt(pendenciaDt.getPendenciaStatusCodigo());
			
			// jvosantos - 23/07/2019 14:26 - Correção de NullPointer
			if (listArquivos != null && listArquivos.size() == 1) {
				pendenciaArquivoDt = finalizacaoVoto.getVoto();
				if (pendenciaArquivoDt != null) {
					listArquivos.get(0).setId(pendenciaArquivoDt.getArquivoDt().getId());
					inserirArquivos = false;
				}
			} else if(listArquivos != null) {
				for (ArquivoDt arquivoDt2 : listArquivos) {
					if (arquivoDt2.getArquivoTipo().equals("Ementa")) {
						pendenciaArquivoDt = finalizacaoVoto.getEmenta();
					} else {
						pendenciaArquivoDt = finalizacaoVoto.getVoto();
					}
					if (pendenciaArquivoDt != null && status == PendenciaStatusDt.ID_EM_ANDAMENTO) {
						arquivoDt2.setId(pendenciaArquivoDt.getArquivoDt().getId());
						inserirArquivos = false;
					}
				}
			}
			if (inserirArquivos || status != PendenciaStatusDt.ID_EM_ANDAMENTO) {
				pendenciaNe.inserirArquivos(pendenciaDt, listArquivos, false, usuario.getUsuarioDt(), fabrica);
			} else {
				for (ArquivoDt arquivoDt2 : listArquivos) {
					arquivoDt2.setIpComputadorLog(usuario.getIpComputadorLog());
					arquivoDt2.setId_UsuarioLog(usuario.getId_Usuario());
					new ArquivoNe().salvar(arquivoDt2, fabrica);
				}
			}
			setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabrica);

			finalizarVerificarResultadoVotacao(pendenciaDt, fabrica, usuario, listArquivos, finalizacaoVoto);

			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}	
	}

	// jvosantos - 11/07/2019 18:14 - Implementar método para finalizar a verificação do resultado da votação
	private void finalizarVerificarResultadoVotacao(PendenciaDt pendenciaDt, FabricaConexao fabrica, UsuarioNe usuario,
			List<ArquivoDt> arquivos, FinalizacaoVotoSessaoDt finalizacaoVoto) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		int tipoFinalizacao = Optional
				.ofNullable(finalizacaoVoto)
				.map(FinalizacaoVotoSessaoDt::getTipoAnalise)
				.orElse(FinalizacaoVotoSessaoDt.NORMAL);
		ProcessoDt processoDt = new ProcessoNe().consultarId(pendenciaDt.getId_Processo());
		List<ArquivoDt> arquivosPendencia = consultarArquivosPendenciaAbertaFinalizada(pendenciaDt, fabrica);
		Optional<ArquivoDt> configuracao = Optional.ofNullable(arquivosPendencia).flatMap(lista -> {
			return lista
					.stream()
					.filter(
							(
									arquivoDt) -> Funcoes
											.StringToInt(
													arquivoDt
															.getArquivoTipoCodigo()) == ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)
					.findAny();
		});
		
		String decisao = "";
		
		if (configuracao.isPresent()) {
			ArquivoDt arquivoDt = new ArquivoNe().consultarId(configuracao.get().getId(), fabrica.getConexao());
			String conteudo = new String(arquivoDt.getConteudoSemAssinar());
			String[] strings = conteudo.split(Configuracao.SEPARDOR03);
			decisao = strings[0];
			tipoFinalizacao = Funcoes.StringToInt(strings[1]);
		}
		
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe
				.consultarAudienciaProcessoPendente(pendenciaDt.getId_Processo(), usuario.getUsuarioDt(), fabrica);

		// jvosantos - 04/06/2019 10:09 - Adicionado uma verificação para não realizar a consulta desnecessariamente
		if(finalizacaoVoto == null) finalizacaoVoto = consultarFinalizacaoVoto(
			audienciaProcessoDt.getId(),
			usuario,
			fabrica);
		
		String statusDecisao = finalizacaoVoto.getAudienciaProcesso().getAudienciaProcessoStatusCodigoTemp();
		
		finalizacaoVoto.setTipoAnalise(tipoFinalizacao);
		
			
		List<Integer> gerarPendenciaVotantesTipo = null;
			gerarPendenciaVotantesTipo = Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.VOTANTE, VotanteTipoDt.PRESIDENTE_CAMARA, VotanteTipoDt.PRESIDENTE_SESSAO);  
		
		List<String> votantesSessao = (new VotoNe()).consultarIdsIntegrantesSessaoPorTipo(
				finalizacaoVoto.getAudienciaProcesso().getId(),
				gerarPendenciaVotantesTipo);
		
		if (finalizacaoVoto.isRetirarPauta()) {
			finalizacaoRetirarPauta(pendenciaDt, fabrica, usuario, pendenciaNe, processoDt, audienciaProcessoNe,
					audienciaProcessoDt, votantesSessao, arquivos);
		} else if (finalizacaoVoto.isJulgamentoAdiado()) {
			finalizacaoJulgamentoAdiado(fabrica, usuario, arquivos, pendenciaNe, processoDt, votantesSessao);
		} else {
			ResultadoVotacaoSessao resultado = consultarResultadoVotacao(audienciaProcessoDt.getId());
			
			if(resultado == ResultadoVotacaoSessao.MAIORIA_DIVERGE)
				reabrirVotacaoApreciados(finalizacaoVoto, fabrica, usuario, pendenciaDt);
			else
				gerarResultadoVotacao(audienciaProcessoDt, usuario.getUsuarioDt(), fabrica);
		}
	
		
		finalizacaoVoto.getAudienciaProcesso()
				.setAudienciaProcessoStatusCodigoTemp(StringUtils.defaultIfEmpty(statusDecisao, decisao));
		
		Map<String, PendenciaArquivoDt> arquivosAnalise = consultarArquivosAnalise(
				usuario,
				fabrica,
				audienciaProcessoDt);
		for (ArquivoDt arquivoDt : arquivos) {
			ArquivoNe arquivoNe = new ArquivoNe();
			ArquivoDt arquivoSalvo;
			ArquivoDt arquivoDtAntigo = arquivoNe.consultarId(arquivoDt.getId(), fabrica.getConexao());

			if (Funcoes.StringToInt(arquivoDtAntigo.getArquivoTipoCodigo()) == ArquivoTipoDt.EMENTA) {
				arquivoSalvo = arquivosAnalise.get("ementa").getArquivoDt();
			} else {
				arquivoSalvo = arquivosAnalise.get("voto").getArquivoDt();
			}

			arquivoSalvo.setArquivo(Signer.extrairConteudoP7s(arquivoDt.getConteudoSemAssinar()));
			arquivoSalvo.setIpComputadorLog(usuario.getIpComputadorLog());
			arquivoSalvo.setId_UsuarioLog(usuario.getId_Usuario());
			arquivoNe.salvar(arquivoSalvo, fabrica);
		}
	}

	// lrcampos - 09/07/2019 09:46 - Insere MP e Presidente da sessão quando for adiado o Julgamento e a nova sessão já tiver iniciado.
	public void atualizarMPPresidenteSessao(AudienciaProcessoDt audienciaProcessoNovaDt, FabricaConexao fabConexao)
			throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<AudienciaProcessoVotantesDt> listVotante = new ArrayList<AudienciaProcessoVotantesDt>();
		AudienciaProcessoNe audiProcNe = new AudienciaProcessoNe();

		try {
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}
			VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
			AudienciaProcessoPs audienciaProcessoPs = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			AudienciaProcessoVotantesDt votanteMP = votoPs.consultarMPPresidenteSessao(audienciaProcessoNovaDt,
					VotanteTipoDt.MINISTERIO_PUBLICO);
			AudienciaProcessoVotantesDt votantePresidente = votoPs.consultarMPPresidenteSessao(audienciaProcessoNovaDt,
					VotanteTipoDt.PRESIDENTE_SESSAO);
			audienciaProcessoNovaDt.setId_ServentiaCargoPresidente(votantePresidente.getId_ServentiaCargo());
			audienciaProcessoNovaDt.setId_ServentiaCargoMP(votanteMP.getId_ServentiaCargo());
			audienciaProcessoPs.alterarPresidenteMPAudiencia(audienciaProcessoNovaDt);
			votanteMP.setId_AudienciaProcesso(audienciaProcessoNovaDt.getId());
			votantePresidente.setId_AudienciaProcesso(audienciaProcessoNovaDt.getId());
			listVotante.add(votanteMP);
			listVotante.add(votantePresidente);
			audienciaProcessoPs.cadastrarVotantesSessaoVirtual(listVotante);
			// Se a fabrica foi criada no metodo
			if (fabConexao == null) {
				obFabricaConexao.finalizarTransacao();
			}

		} catch (Exception e) {
			// Caso ocorra algum erro a transacao e cancelada, se nao foi
			// passado a fabrica de conexao
			if (fabConexao == null) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		} finally {
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}

	}

	// lrcampos - 11/10/2019 14:45 - Método para verificar se possui várias audi_procs
	public Boolean possuiVariasAudiProc(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		Boolean possuiVariasAudiProc = false;
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
					possuiVariasAudiProc = votoPs.possuiVariasAudiProc(idProcesso);
				}finally {
					obFabricaConexao.fecharConexao();
				}
		return possuiVariasAudiProc;
	}
	
	public boolean verificarSeHouveConvocacao(String idAudiProc) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			VotoPs ps = new VotoPs(fabricaConexao.getConexao());
			return ps.verificarSeHouveConvocacao(idAudiProc);
		}finally {
			fabricaConexao.fecharConexao();
		}
	}
	
	// lrcampos 19/08/2019 * Verifica se mais de 2/3 dos votantes acompanharam o relatorVerifica
	public boolean podeConfirmarExtratoAtaOrgalEspecial(String idAudiProc, UsuarioNe usuario) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean podeInserirAta = false;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
			double qtdVotos = consultarVotosVotantesIncluindoProprio(idAudiProc, usuario, obFabricaConexao).size();

			List<VotoDt> votos = VotacaoUtils.filtraVotosReais(votoPs.consultarVotosSessao(idAudiProc));
			int resultado = votos.stream().mapToInt((voto) -> isAcompanhoRelator(voto)).sum();

			if (resultado > 0)
				podeInserirAta = (resultado/qtdVotos) >= RAZAO_VOTO_VOTANTE;

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return podeInserirAta;
	}
	
	// jvosantos - 13/08/2019 12:59 - Método que realiza a consulta do modelo e substitui a variável do nome do redator
	public ModeloDt consultarModeloId(String idModelo, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		ModeloDt modeloDt = (new MovimentacaoNe()).consultarModeloId(idModelo, processoDt, usuarioDt);
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		VotoPs votoPs = null;
		
		try {
			votoPs = new VotoPs(obFabricaConexao.getConexao());
			
			String idAudienciaProcesso = new AudienciaProcessoNe().consultarAudienciaProcessoDoProcesso(processoDt.getId());
			
			Optional<VotoDt> votoDivergente = votoPs.consultarVotosSessao(idAudienciaProcesso)
					.stream()
					.filter(voto -> StringUtils.equals(voto.getVotoTipoCodigo(), String.valueOf(VotoTipoDt.DIVERGE)))
					.findFirst();
			
			if(votoDivergente.isPresent())
				modeloDt.setTexto(modeloDt.getTexto().replaceAll("[$][{][\\s]*"+ModeloNe.AUDIENCIA_SESSAO_REDATOR+"[\\s]*[}]", votoDivergente.get().getNomeVotante()));
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return modeloDt;
	}
	
	// jvosantos - 02/09/2019 12:02 - Criar método para verificar se a quantidade de votos bate com a quantidade de votantes
	public boolean verificarQuantidadeVotos(String idAudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {
		VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
		
		return votoPs.verificarQuantidadeVotos(idAudienciaProcesso);	
	}
	
	// jvosantos - 24/09/2019 13:19 - Método que retorna a lista de votos de uma AUDI_PROC
	public List<VotoDt> consultarTodosVotosSessao(String idAudienciaProcesso, FabricaConexao fabricaConexao) throws Exception{
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		
		return votoPs.consultarTodosVotosSessao(idAudienciaProcesso);	
	}
	
	public List<VotoDt> consultarTodosVotosSessaoAtivosInativos(String idAudienciaProcesso, FabricaConexao fabricaConexao) throws Exception{
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		
		return votoPs.consultarTodosVotosSessaoAtivosInativos(idAudienciaProcesso);	
	}

	// jvosantos - 24/09/2019 18:02 - Método que apaga um voto
	public void excluirVoto(String idVoto, FabricaConexao fabricaConexao) throws Exception {
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		votoPs.excluir(idVoto);
	}

	// jvosantos - 24/09/2019 18:02 - Método que apaga um votante
	public void excluirVotante(String idAudiProcVotante, FabricaConexao fabricaConexao) throws Exception {
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		votoPs.excluirVotante(idAudiProcVotante);
	}

	// jvosantos - 25/09/2019 13:21 - Consulta todos os "votantes" da AUDI_PROC
	public List<VotanteDt> consultarTodosVotantesSessaoCompletoDeVerdade(String id, FabricaConexao fabricaConexao) throws Exception {
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		return votoPs.consultarTodosVotantesSessaoCompletoDeVerdade(id);
	}
	
	// jvosantos - 25/09/2019 13:21 - Consulta todos os "votantes" da AUDI_PROC
	public List<VotanteDt> consultarTodosVotantesSessaoCompletoDeVerdade(String idAudiProc) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
			return votoPs.consultarTodosVotantesSessaoCompletoDeVerdade(idAudiProc);

		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	// jsantonelli 21/10/2019: adiciona informações sobre os classificadores
	public List<VotoSessaoLocalizarDt> ordenaClassificadores(List<VotoSessaoLocalizarDt> lista, FabricaConexao fabrica){
		lista.sort((x,y) -> Integer.parseInt(x.getIdClassificador()) - Integer.parseInt(y.getIdClassificador()));
		return lista;
	}

	protected void salvarArquivosMovimentacao(MovimentacaoDt movimentacaoDt, List<ArquivoDt> listaArquivos,
			ProcessoDt processoDt, LogDt logDt, FabricaConexao obFabricaConexao,
			MovimentacaoArquivoNe movimentacaoArquivoNe) throws Exception {
		String visibilidade = processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null;

		movimentacaoArquivoNe.inserirArquivos(listaArquivos, logDt, obFabricaConexao);
		movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(listaArquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);
		//movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(listaArquivos, Arrays.asList(movimentacaoDt), obFabricaConexao);
	}

	// jvosantos - 06/01/2020 11:36 - Método para desativar todos os votos de uma AUDI_PROC
	public void desativarTodosVotos(String idAudiProc, FabricaConexao fabricaConexao) throws Exception {
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		
		votoPs.desativarTodosVotos(idAudiProc);
	}
	
	// jvosantos - 06/01/2020 11:36 - Método para desativar todos os votos de uma AUDI_PROC
	public void desativarTodosVotosIdServentiaCargo(String idAudiProc, FabricaConexao fabricaConexao, List<String> idServentiaCargo) throws Exception {
		VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
		
		votoPs.desativarTodosVotosIdServentiaCargo(idAudiProc, idServentiaCargo);
	}

	// jvosantos - 06/01/2020 - 11:41 - Método para finalizar todas as pendencias de votos em aberto
	public void finalizarPendenciasVoto(String idAudienciaProcesso, UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception {
		List<PendenciaDt> pendenciasVoto = new PendenciaNe().consultarPendenciasAudienciaProcessoPorListaTipo(idAudienciaProcesso, fabricaConexao,
				PendenciaTipoDt.VOTO_SESSAO);
		
		for(PendenciaDt pendencia : pendenciasVoto) {
			pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_NAO_CUMPRIDA));
			setInfoPendenciaFinalizar(pendencia, usuarioDt, fabricaConexao);
		}
		
	}

	// jvosantos - 06/01/2020 17:55 - Método para retornar se existe algum voto desativado para aquela AUDI_PROC
	public boolean existeVotoDesativado(String idAudienciaProcesso, int ...tipos) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
			
			return votoPs.existeVotoDesativado(idAudienciaProcesso, tipos);
		}finally {
			fabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 02/03/2020 16:55 - Método para retornar se existe algum voto do SERV_CARGO para aquela AUDI_PROC
	public boolean existeVotoDeServCargo(String idAudienciaProcesso, String idServCargo) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
			
			return votoPs.existeVotoDeServCargo(idAudienciaProcesso, idServCargo);
		}finally {
			fabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 02/03/2020 16:55 - Método para retornar se existe algum voto para aquela AUDI_PROC
	public boolean existeVoto(String idAudienciaProcesso, int ...tipos) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
			
			return votoPs.existeVoto(idAudienciaProcesso, tipos);
		}finally {
			fabricaConexao.fecharConexao();
		}
	}

	//lrcampos 20/03/2020 10:25 - Verifica se a audienciaProcesso original foi adiada por sustenção oral
	public boolean isAdiadoPorSustentacaoOralOriginal(String idProcesso, String dataAudienciaOriginal) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		try {
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());
			String idAudienciaProcesso = votoPs.buscaAudienciaVirtualOriginalPJD(idProcesso, dataAudienciaOriginal);
			if(StringUtils.isEmpty(idAudienciaProcesso)) return false;
			
			AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoNe().consultarId(idAudienciaProcesso);	
			return audienciaProcessoDt.getAudienciaProcessoStatusCodigo().equals(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL));
		}finally {
			fabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 15/01/2020 12:12 - Método para consultar as pendências de erro material
	public List<VotoSessaoLocalizarDt> consultarErroMaterial(String numeroProcessoCompleto, UsuarioNe usuario, boolean preAnalisado,
			boolean aguardandoAssinatura) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		String[] processoNumeroEDigito = null;
		
		if(StringUtils.isNotEmpty(numeroProcessoCompleto))
			processoNumeroEDigito = numeroProcessoCompleto.split("[-\\.]");
		
		try {
			VotoPs votoPs = new VotoPs(fabricaConexao.getConexao());

			String idServentiaCargo = getServentiaCargoOuServentiaCargoChefe(usuario);
			
			List<VotoSessaoLocalizarDt> votos = votoPs.consultarErroMaterial(idServentiaCargo, preAnalisado, aguardandoAssinatura, processoNumeroEDigito, usuario, fabricaConexao);
			
			consultarVotosRelacionados(usuario, votos, fabricaConexao);
			verificarPrazoPendencias(votos, fabricaConexao);

			votos = ordenaClassificadores(votos, fabricaConexao);
			
			return votos;			
		}finally {
			fabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 15/01/2020 12:07 - Extrair trecho de código repetido
	private String getServentiaCargoOuServentiaCargoChefe(UsuarioNe usuario) {
		return StringUtils.isEmpty(usuario.getUsuarioDt().getId_ServentiaCargoUsuarioChefe())
				? usuario.getId_ServentiaCargo()
				: usuario.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
	}

	// jvosantos - 21/01/2020 18:44 - Método para desfazer aguardando assinatura da pendência de erro material
	public void desfazerAssinaturaErroMaterial(String[] ids) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		try {
			fabricaConexao.iniciarTransacao();
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			for (String id : ids) {
				pendenciaNe.alterarCodigoTempPendencia(id, null, fabricaConexao);
			}

			fabricaConexao.finalizarTransacao();
		}catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		}finally {
			fabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 06/01/2020 17:57 - Extrair método auxiliar
	private boolean existeVoto(AnaliseVotoSessaoDt analiseVoto, int tipo) {
		return analiseVoto.getVotos().stream().anyMatch((voto) -> voto.getVotoCodigoInt() == tipo);
	}
	
	// jvosantos - 21/01/2020 19:13 - Lista de votos ignorados por padrão
	public List<Integer> listaIgnorarVotoTipo(){
		return Arrays.asList(
			VotoTipoDt.PRAZO_EXPIRADO,
			VotoTipoDt.JULGAMENTO_ADIADO,
			VotoTipoDt.OBSERVACAO,
			VotoTipoDt.PROCLAMACAO_DECISAO,			// jvosantos - 04/06/2019 11:31 - Remove o tipo de voto Proclamação da Decisão das telas de Voto
			VotoTipoDt.VERIFICAR_RESULTADO_VOTACAO,	// jvosantos - 12/08/2019 16:16 - Remove o tipo de voto Verificar Resultado da Votação das telas de Voto
			VotoTipoDt.ANALISE_ERRO_MATERIAL, 		// jvosantos - 21/01/2020 19:03 - Remove o tipo de voto Analise de Erro Material das telas de Voto
			VotoTipoDt.JULGAMENTO_REINICIADO 		// jvosantos - 03/03/2020 16:28 - Remove o tipo de voto Julgamento Reiniciado das telas de Voto
		);
	}
	
	// jvosantos - 04/02/2020 15:12 - Gera lista de votos que devem ser ignorados para a AnaliseVotoSessaoDt
	public List<Integer> montarListaIgnorarVotoTipo(AnaliseVotoSessaoDt analiseVoto) throws Exception {
		return montarListaIgnorarVotoTipo(analiseVoto, false);
	}
	
	// jvosantos - 21/01/2020 19:13 - Gera lista de votos que devem ser ignorados para a AnaliseVotoSessaoDt
	public List<Integer> montarListaIgnorarVotoTipo(AnaliseVotoSessaoDt analiseVoto, boolean renovarOuModificar) throws Exception {
		List<Integer> listaIgnorar = new ArrayList<Integer>();
		
		listaIgnorar.addAll(listaIgnorarVotoTipo());

		listaIgnorar.add(existeVoto(analiseVoto, VotoTipoDt.DIVERGE) ? VotoTipoDt.DIVERGE : VotoTipoDt.ACOMPANHA_DIVERGENCIA);
		
		// jvosantos - 06/01/2020 17:56 - Verifica se já existe um voto do tipo ERRO_MATERIAL, só pode haver um por AUDI_PROC.
		if(renovarOuModificar || existeVoto(analiseVoto, VotoTipoDt.ERRO_MATERIAL) || existeVotoDesativado(analiseVoto.getIdAudienciaProcesso(), VotoTipoDt.ERRO_MATERIAL))
			listaIgnorar.add(VotoTipoDt.ERRO_MATERIAL);
		
		return listaIgnorar;
	}

	public FinalizacaoVotoSessaoDt consultarConfiguracao(FinalizacaoVotoSessaoDt finalizacaoVoto, List<PendenciaArquivoDt> lista) throws Exception {
		Optional<PendenciaArquivoDt> optionalConfig = lista.stream().filter(pendenciaArquivo -> Funcoes.StringToInt(
				pendenciaArquivo.getArquivoDt().getArquivoTipoCodigo()) == ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)
				.findAny();
		if (optionalConfig.isPresent()) {
			ArquivoDt arquivoDt = optionalConfig.get().getArquivoDt();
			String conteudo = new String(new ArquivoNe().consultarConteudoArquivo(arquivoDt, null));
			String[] strings = Arrays.copyOf(conteudo.split(Configuracao.SEPARDOR03), 5);
			finalizacaoVoto.getAudienciaProcesso().setAudienciaProcessoStatusCodigoTemp(strings[0]);
			finalizacaoVoto.setTipoAnalise(Funcoes.StringToInt(strings[1]));
			finalizacaoVoto.setAlterarVotoEmenta(Funcoes.StringToBoolean(strings[2]));
			
			if(strings.length > 3)
				finalizacaoVoto.setAcompanhaDivergencia(Funcoes.StringToBoolean(strings[3]));
		}
		
		return finalizacaoVoto;
	}

	public FinalizacaoVotoSessaoDt montarConteudoArquivos(FinalizacaoVotoSessaoDt finalizacaoVoto, List<PendenciaArquivoDt> lista) throws Exception {
		VotoNe votoNe = new VotoNe();
		List<PendenciaArquivoDt> listArquivos = lista.stream()
				.filter(pendenciaArquivo -> Funcoes.StringToInt(pendenciaArquivo.getArquivoDt()
						.getArquivoTipoCodigo()) != ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(listArquivos)) {
			finalizacaoVoto.setConteudoArquivoVoto(
					new String(finalizacaoVoto.getVoto().getArquivoDt().getConteudoSemAssinar()));
			finalizacaoVoto.setConteudoArquivoEmenta(
					new String(finalizacaoVoto.getEmenta().getArquivoDt().getConteudoSemAssinar()));
		} else if (finalizacaoVoto.isNormal()) {
			if (Integer
					.valueOf(listArquivos.get(0).getArquivoDt().getId_ArquivoTipo()) == ArquivoTipoDt.RELATORIO_VOTO) {

				finalizacaoVoto.setVoto(listArquivos.get(0));
				finalizacaoVoto
						.setConteudoArquivoVoto(votoNe.consultaConteudoArquivo(listArquivos.get(0).getArquivoDt()));
				finalizacaoVoto.setEmenta(listArquivos.get(1));
				finalizacaoVoto
						.setConteudoArquivoEmenta(votoNe.consultaConteudoArquivo(listArquivos.get(1).getArquivoDt()));
			} else {
				finalizacaoVoto.setEmenta(listArquivos.get(0));
				finalizacaoVoto.setVoto(listArquivos.get(1));
				finalizacaoVoto
						.setConteudoArquivoVoto(votoNe.consultaConteudoArquivo(listArquivos.get(1).getArquivoDt()));
				finalizacaoVoto
						.setConteudoArquivoEmenta(votoNe.consultaConteudoArquivo(listArquivos.get(0).getArquivoDt()));
			}
		} else {
			finalizacaoVoto.setVoto(listArquivos.get(0));
			// jvosantos - 25/10/2019 11:17 - Buscar conteudo do arquivo de Retirar de Pauta
			// jvosantos - 29/07/2019 12:22 - Adicionar verificação para evitar consulta desnecessária
			if(finalizacaoVoto.getTipoAnalise() == FinalizacaoVotoSessaoDt.ADIAR || finalizacaoVoto.getTipoAnalise() == FinalizacaoVotoSessaoDt.RETIRAR)
				finalizacaoVoto
					.setConteudoArquivoAdiado(votoNe.consultaConteudoArquivo(listArquivos.get(0).getArquivoDt()));
			finalizacaoVoto.setConteudoArquivoVoto("");
			finalizacaoVoto.setConteudoArquivoEmenta("");
		}
		
		return finalizacaoVoto;
	}
	
	// jvosantos - 24/01/2020 18:21 - Método para desativar um voto
	public void desativarVoto(String idVoto, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		votoPs.desativarVoto(idVoto);
	}
	
	public void ativarVoto(String idVoto, FabricaConexao fabrica) throws Exception {
		VotoPs votoPs = new VotoPs(fabrica.getConexao());
		votoPs.ativarVoto(idVoto);
	}

	// jvosantos - 24/01/2020 18:21 - Método para criar pendência de voto para o votante que gerou o Erro Material
	public void criarPendenciaVotoErroMaterialNegado(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		try {
			fabricaConexao.iniciarTransacao();
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			List<VotoDt> votosSessao = consultarTodosVotosSessao(finalizacaoVoto.getIdAudienciaProcesso(),
					fabricaConexao);
			
			VotoDt votoErroMaterial = votosSessao.stream()
					.filter(x -> x.getVotoCodigoInt() == VotoTipoDt.ERRO_MATERIAL)
					.findFirst()
					.orElseThrow(() -> new MensagemException("Voto de Erro Material não encontrado"));
			
			desativarVoto(votoErroMaterial.getId(), fabricaConexao);
			
			PendenciaDt pendenciaDt2 = pendenciaNe.gerarPendenciaVotoSessaoVirtual(votoErroMaterial.getIdServentiaCargo(), usuario.getUsuarioDt(),
					finalizacaoVoto.getAudienciaProcesso().getId_Processo(),
					getDataLimiteVotacao(finalizacaoVoto.getAudienciaProcesso().getAudienciaDt(), finalizacaoVoto.getAudienciaProcesso(), fabricaConexao),
					finalizacaoVoto.getIdAudienciaProcesso(), fabricaConexao);
			
			new AudienciaProcessoPendenciaNe().salvar(pendenciaDt2.getId(), finalizacaoVoto.getIdAudienciaProcesso(), fabricaConexao);
			
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(finalizacaoVoto.getIdPendencia(), fabricaConexao);
			
			pendenciaDt.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabricaConexao);
			
			fabricaConexao.finalizarTransacao();
		}catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		}finally {
			fabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 24/01/2020 18:21 - Método para resolver qual opção foi escolhida
	public void finalizarErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario, List<ArquivoDt> lista) throws Exception {
		if(!finalizacaoVoto.isAlterarVotoEmenta())
			criarPendenciaVotoErroMaterialNegado(finalizacaoVoto, usuario);
		else {
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			audienciaProcessoNe.alterarVotoEmentaErroMaterial(finalizacaoVoto, usuario, lista);
		}
	}
	
	// jvosantos - 24/01/2020 18:21 - Método para preparar dados
	public void finalizarErroMaterial(PendenciaDt pendenciaDt, UsuarioNe usuario, List lista) throws Exception {		
		FinalizacaoVotoSessaoDt finalizacaoVoto = consultarFinalizacaoVoto(new AudienciaProcessoPendenciaNe().consultarPorIdPend(pendenciaDt.getId()), usuario);
		
		List<PendenciaArquivoDt> listArquivos = Optional.ofNullable(consultarArquivoFinalizarVotoPreAnalise(pendenciaDt.getId(), usuario)).orElse(new ArrayList<>());
		
		finalizacaoVoto = consultarConfiguracao(finalizacaoVoto, listArquivos);
		
		finalizacaoVoto.setIdPendencia(pendenciaDt.getId());
		
		finalizarErroMaterial(finalizacaoVoto, lista, consultarArquivoFinalizarVotoPreAnalise(finalizacaoVoto.getIdPendencia(), usuario), usuario);
	}

	// jvosantos - 24/01/2020 18:21 - Método para resolver qual opção foi escolhida
	public void finalizarErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto, List listaArquivosAssinados,
			List<PendenciaArquivoDt> listArquivosSalvos, UsuarioNe usuario) throws Exception {
		
		if(CollectionUtils.isNotEmpty(listaArquivosAssinados)) {
			// Assinado
			if (finalizacaoVoto.isRetirarPauta()) 
				finalizarVotacaoRetirarPauta(finalizacaoVoto, usuario, listaArquivosAssinados);
			else if (finalizacaoVoto.isJulgamentoAdiado())
				finalizarVotacaoJulgamentoAdiado(finalizacaoVoto, usuario, listaArquivosAssinados);
			else {
				finalizarErroMaterial(finalizacaoVoto, usuario, listaArquivosAssinados);
			}
		} else {
			atualizaVerificarPossivelErroMaterial(finalizacaoVoto, usuario, listArquivosSalvos);
		}
	}

	private boolean isOriginadoSessaoVirtual(AudienciaProcessoDt x, VotoNe votoNe) {
        try {
            return votoNe.isAdiadoPorSustentacaoOralOriginal(x.getId_Processo(), x.getDataAudienciaOriginal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
