package br.gov.go.tj.projudi.ne;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.JulgamentoAdiadoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.SustentacaoOralDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.ps.SustentacaoOralPs;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.projudi.sessaoVirtual.ne.PendenciaSessaoVirtualNe;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Relatorios;

public class SustentacaoOralNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1074875389107550247L;
	private static final int QTD_DIAS_ANTES_AUDIENCIA = -1;
	private static final Integer[] CLASSES_PARA_DEFERIMENTO_RELATOR = new Integer[] {
			ProcessoTipoDt.AGRAVO_INSTRUMENTO_CPC,
			ProcessoTipoDt.AGRAVO_INSTRUMENTO_RECURSO_ESPECIAL_CPC,
			ProcessoTipoDt.AGRAVO_INSTRUMENTO_RECURSO_EXTRAORDINARIO_CPC,
			ProcessoTipoDt.AGRAVO_INSTRUMENTO_RECURSO_ESPECIAL,
			ProcessoTipoDt.AGRAVO_INSTRUMENTO_RECURSO_EXTRAORDINARIO
	};

	//lrcampos 13/09/2019 Insere o vinculo entre AudiProc e Pend de sustentação oral
	public void inserirSustentacaoOral(SustentacaoOralDt sustentacaoOralDt, FabricaConexao fabConexao)
			throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}
			
			SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
			sustentacaoOralPs.inserir(sustentacaoOralDt);

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

	public void alterarStatusSustentacaoOral(SustentacaoOralDt sustentacaoOralDt, FabricaConexao obFabricaConexao)
			throws Exception {

		SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
		sustentacaoOralPs.alterarStatus(sustentacaoOralDt);

	}
	
	public void alterarStatusSustentacaoOral(SustentacaoOralDt sustentacaoOralDt) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
			sustentacaoOralPs.alterarStatus(sustentacaoOralDt);

		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	//lrcampos 13/09/2019 consulta sustentação oral aberta pelo idProcParteAdv
	public SustentacaoOralDt consultarSustentacaoOralAberta(String idProcessoParteAdvogado, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
		try {
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}
			
			SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
			sustentacaoOralPs.consultar(idProcessoParteAdvogado, usuarioDt);

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
		
		return sustentacaoOralDt;
		
	}
	
	//lrcampos 13/09/2019 consulta sustentação oral aberta pelo idPendencia
	public SustentacaoOralDt consultarSustentacaoOralAbertaIdPendencia(String idPendencia, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
		try {
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}
			
			SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
			sustentacaoOralDt = sustentacaoOralPs.consultarSustentacaoOralIdPend(idPendencia);

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
		
		return sustentacaoOralDt;
		
	}
	
	//mrbatista 24/11/2020 consulta Sustencao oral pelo id da audiência processo
	public List<SustentacaoOralDt> consultarSustentacaoOralIdAudiProc(String idAudiProc, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		List<SustentacaoOralDt> listaPedidosSustentacaoOral = new ArrayList<SustentacaoOralDt>();
		try {
			// Se a fabrica nao foi passada
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabConexao;
			}
			
			SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
			listaPedidosSustentacaoOral = sustentacaoOralPs.consultarSustentacaoOralIdAudiProc(idAudiProc);
	
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
		
		return listaPedidosSustentacaoOral;
		
	}

	//lrcampos 13/09/2019 consulta AudienciaProcesso pelo id processo, passando usuarioDt
	public List<AudienciaProcessoDt> consultaPedidoAbertoDeSOPeloUsuario(String idProcesso, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
			List<AudienciaProcessoDt> listaAudienciaProcesso = sustentacaoOralPs.consultaAudienciaProcessoPeloIdProcesso(idProcesso);
			ProcessoParteNe processoParteNe = new ProcessoParteNe();
			AudienciaNe audienciaNe = new AudienciaNe();
			String situacaoPedidoSO;
			for (AudienciaProcessoDt audienciaProcessoDt : listaAudienciaProcesso) {
				situacaoPedidoSO = null;
				audienciaProcessoDt.setProcessoTipo(processoParteNe.consultaClasseProcessoIdAudiProc(audienciaProcessoDt.getId(), obFabricaConexao));
				audienciaProcessoDt.setAudienciaDt(audienciaNe.consultarId(audienciaProcessoDt.getId_Audiencia()));
				situacaoPedidoSO = situacaoPedidoSOIdAudiProc(audienciaProcessoDt.getId(), usuarioDt, obFabricaConexao);
				if(StringUtils.isNotEmpty(situacaoPedidoSO))
					audienciaProcessoDt.setSituacaoPedidoSO(situacaoPedidoSO);
			}
			return listaAudienciaProcesso;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	// alsqueiroz - 20/09/2019 15:12 - Método para consultar os pedidos de sustentacao oral
	public List<SustentacaoOralDt> consultarPedidosSustentacaoOral(String idAudienciaProcesso) throws Exception {
		FabricaConexao fabricaConexao = null;

		try {
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			return (new SustentacaoOralPs(fabricaConexao.getConexao())).consultarPedidosSustentacaoOral(idAudienciaProcesso);
		} finally {
			fabricaConexao.fecharConexao();
		}
	}

	//lrcampos 13/09/2019 consulta situação do pedido de So da audienciaProcesso
	private String situacaoPedidoSOIdAudiProc(String idAudiProc, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		
		SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(obFabricaConexao.getConexao());
		Integer pendTipo = sustentacaoOralPs.consultarSituacaoPedidoSOidAudiProc(idAudiProc, usuarioDt);
		String retornoSituacao = null;
		if(pendTipo != null) {
			if(pendTipo == PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL) 
				retornoSituacao = "Pedido Enviado";
			else if(pendTipo == PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO) 
				retornoSituacao = "Pedido Deferido";
			else if(pendTipo == PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA) 
				retornoSituacao = "Pedido Indeferido";
		}
		
		
		return retornoSituacao;
	}
	

	/**
	 * Método responsável para verificar se um processo de uma sessão presencial, veio adiado através do pedido de S.O de uma audiencia virtual
	 * 
	 * @author lrcampos
	 * 
	 * @param idProcesso, id do processo
	 * @param dataAudienciaOriginal data da audiencia na qual o processo pertencia antes de ser adiado.
	 * @throws Exception 
	 */
	

	/**
	 * Método que verifica se os dados obrigatórios em uma movimentação foram
	 * preenchidos
	 * 
	 * @param idProcesso,
	 *            id do Processo
	 * @param procParte,
	 *            id da Parte que foi realizado o pedido de Sustentação oral
	 * @param procParteAdv,
	 *            id proc parte Advogado que solicitou advogado
	 * @param pedidoPeloSecretario,
	 *            pedido foi realizado pelo secretário.
	 * @author lrcampos
	 */
	public String solicitarSustentacaoOral(String idProcesso, UsuarioNe usuario, String procParte, String procParteAdv, Boolean isVirtual, Boolean pedidoPeloSecretario) throws Exception {
		return solicitarSustentacaoOral(idProcesso, usuario, procParte, procParteAdv, isVirtual, pedidoPeloSecretario, null);
	}

	// mrbatista - 11/10/2019 14:53 - Overload
	public String solicitarSustentacaoOral(String idProcesso, UsuarioNe usuario, String procParte, String procParteAdv,
			Boolean isVirtual, Boolean pedidoPeloSecretario, String idAudiProc) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = null;
		String mensagem = ""; 
		if(idAudiProc == null) {
			audienciaProcessoDt = new AudienciaNe()
					.consultarAudienciaProcessoPendente(idProcesso, usuario.getUsuarioDt());	
		}else {
			audienciaProcessoDt = new AudienciaProcessoNe().consultarId(idAudiProc); 
		}
		
		String idRelator = new VotoNe().consultarIdRelator(audienciaProcessoDt.getId());
		ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
		SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
		setDadosSustentacaoOral(sustentacaoOralDt, audienciaProcessoDt, usuario.getUsuarioDt(), procParteAdv);

		Integer classeAudiProc = new ProcessoParteNe().consultaCodigoProcessoTipoPeloIdAudiProc(audienciaProcessoDt.getId());
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			obFabricaConexao.iniciarTransacao();

			if (Arrays.asList(CLASSES_PARA_DEFERIMENTO_RELATOR).contains(classeAudiProc)) {
				gerarPendenciasVerificarPedidoSO(idProcesso, usuario, procParte, procParteAdv, pedidoPeloSecretario,
						audienciaProcessoDt, obFabricaConexao);
				mensagem = "Pedido de Sustentação Oral enviado para o Relator";
			} else {
				//deferirAutomaticamenteSolicitacaoSO(idProcesso, usuario, procParteAdv, audienciaProcessoDt,
				//		obFabricaConexao);
				gerarPendenciasVerificarPedidoSOAutomatico(idProcesso, usuario, procParte, procParteAdv, pedidoPeloSecretario,
						audienciaProcessoDt, obFabricaConexao);
				//mensagem = "Solicitação de Pedido de S.O. realizada com sucesso!";
				mensagem = "Pedido de Sustentação Oral foi deferido com sucesso!";
				
			}

			obFabricaConexao.finalizarTransacao();
			
			return mensagem;
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}
	


	//lrcampos 12/07/2019 * setar dados no objeto SustentacaoORalDt
	private void setDadosSustentacaoOral(SustentacaoOralDt sustentacaoOralDt, AudienciaProcessoDt audienciaProcessoDt, UsuarioDt usuarioDt,
			String procParteAdv) throws Exception {
		sustentacaoOralDt.setAudienciaProcessoDt(audienciaProcessoDt);
		sustentacaoOralDt.setProcessoParteAdvogadoDt(new ProcessoParteAdvogadoNe().consultarId(procParteAdv));
		sustentacaoOralDt.setUsuarioServentiaDt(new UsuarioServentiaNe().consultarId(usuarioDt.getId_UsuarioServentia()));

	}
	
	public List<VotoSessaoLocalizarDt> consultarSustentacaoOral(UsuarioNe usuario, String processoNumero) throws Exception {
		FabricaConexao fabrica = null;
		VotoNe votoNe = new VotoNe();
		try {
			
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			SustentacaoOralPs ps = new SustentacaoOralPs(fabrica.getConexao());
			// jvosantos - 15/10/2019 17:04 - Modificar para trazer pendencias do chefe quando for assessor
			String idServCargo = usuario.getId_ServentiaCargo();
			
			if(usuario.getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				idServCargo = usuario.getId_ServentiaCargoUsuarioChefe();
			
			List<VotoSessaoLocalizarDt> votos = ps.consultarSustentacaoOral(idServCargo, processoNumero);
			
			votoNe.consultarVotosRelacionados(usuario, votos);
			
			return votos
					.stream()
					.map((voto) -> votoNe.calcularPrazo(voto))
					.collect(Collectors.toList());
		} finally {
			fabrica.fecharConexao();
		}
	}
	
	// mrbatista - 11/10/2019 14:48 - Verificar prazo de pedido de sustentação oral de forma decrescente
	public boolean verificaPrazoPedidoSustentacaoOralDecrescente(String idProcesso, UsuarioDt usuarioDt) throws Exception {
		ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
		return verificaPrazoPedidoSustentacaoOralDecrescente(processoDt, usuarioDt);
	}
	
	public boolean verificaPrazoPedidoSustentacaoOralDecrescente(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao fabrica = null;
		try {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			
			 List<AudienciaProcessoDt> linkedList = audienciaProcessoNe.consultarSessoesMarcadas(processoDt.getId());
			//código java ansi
			linkedList = linkedList.stream().sorted((x,y) -> {
				return Funcoes.parseData(x.getAudienciaDt().getDataAgendada())
						.compareTo(Funcoes.parseData(y.getAudienciaDt().getDataAgendada())) * -1;
			}).collect(Collectors.toList());
			
			Queue<AudienciaProcessoDt> sessoesMarcadas = new LinkedList<AudienciaProcessoDt>(linkedList);
			
			LocalDateTime atual = LocalDateTime.now();
			
			while(!sessoesMarcadas.isEmpty()) {
				AudienciaProcessoDt audienciaProcessoDt = sessoesMarcadas.poll();

				audienciaProcessoDt.setPermiteSustentacaoOral(audienciaProcessoNe.consultarPodeSustentacaoOral(audienciaProcessoDt.getId()));
				if(!audienciaProcessoDt.isPermiteSustentacaoOral() || audienciaProcessoDt.getAudienciaDt().isSessaoIniciada()) continue;
				//lrcampos 11/11/2019 15:36 - Correção no prazo para pedido de S.O,  onde o advogado poderá realizar o pedido as até 10h do dia util anterior a data sessão.
				LocalDateTime dataUmDiaUtilAntesSessao = Funcoes.parseData(Funcoes.removeDiasUteis(audienciaProcessoDt.getAudienciaDt().getDataAgendada(), audienciaProcessoDt.getId_Processo(), QTD_DIAS_ANTES_AUDIENCIA));
				return dataUmDiaUtilAntesSessao.isAfter(atual);
			}
		} finally {
			fabrica.fecharConexao();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param idProcesso
	 * @param usuarioDt
	 * @return situacaoPedidoSustentacaoOral
	 * @since 27/12/2019 12:24
	 * @author lrcampos
	 * @throws Exception
	 */
	public HashMap<String, String> consultaStatusPedidoSO(String idProcesso, UsuarioDt usuarioDt, String idServentia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		HashMap<String, String> situacaoPedidoSusOral = new HashMap<String, String>();
		try {
			SustentacaoOralPs ps = new SustentacaoOralPs(obFabricaConexao.getConexao());
			situacaoPedidoSusOral = ps.consultaStatusPedidoSO(idProcesso, usuarioDt, idServentia);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return situacaoPedidoSusOral;
	}
	
	/**
	 * Consulta Pendencia Sustentação Oral Aberta
	 * 
	 * @param idProcesso
	 * @param codigoPendenciaTipo
	 * @since 27/12/2019 12:32
	 * @author lrcampos
	 * @throws Exception
	 */
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

	/**
	 * Método que finaliza a pendencia de Verificar Pedido de SO
	 * @param idProcesso
	 * @param usuario
	 * @since 27/12/2019 12:32
	 * @author lrcampos
	 * @throws Exception
	 */
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
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario, fabrica);
			}
		} finally {
			fabrica.fecharConexao();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] relSustentacaoOralJulgamentoAdiadosPJD(String diretorioProjeto,  AudienciaDt audiencia , boolean soPeloSecretario, UsuarioDt usuario, List<JulgamentoAdiadoDt> lista, String dataSessao) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		String titulo = "";
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			SustentacaoOralPs obPersistencia = new SustentacaoOralPs(obFabricaConexao.getConexao());
			// PATH PARA OS ARQUIVOS JASPER
			String pathJasper = Relatorios.getPathRelatorio(diretorioProjeto);
			
			Date dataHoraSessao = Funcoes.StringToDateTime(audiencia.getDataAgendada());
			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm");

			
			Map parametros = new HashMap();
			Map subReports = new HashMap();
			
		
			if(soPeloSecretario) {
				 titulo = "Julgamentos Adiados com Pedido de Sustentação Oral";
			}else {
				 titulo = "Julgamentos Adiados";
			}

			
			// PARÂMETROS DO RELATÓRIO
			
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", titulo);
			parametros.put("dataSessao", dataSessao);
			parametros.put("tamanhoLista", lista.size());
			parametros.put("SUBREPORT_DIR", pathJasper);

			parametros.put("dataJulgamento", dataHoraSessao);
			parametros.put("nomeSolicitante", usuario.getNome());
	
		    temp = Relatorios.gerarRelatorioPdf(pathJasper, "julgamentosAdiados" , parametros,
		    		lista);
	

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}
	
		/**
	 * Método que gera as pendencias de verificar pedido de sustentação oral para a serventia e Relator do processo.
	 * 
	 * @param idProcesso
		 * @param usuario
	 * @param procParte
	 * @param procParteAdv
	 * @param pedidoPeloSecretario
	 * @param audienciaProcessoDt
	 * @param obFabricaConexao
	 * @author lrcampos
	 * @since 09/04/2020 - 14:55
	 */
	private void gerarPendenciasVerificarPedidoSO(String idProcesso, UsuarioNe usuario, String procParte,
			String procParteAdv, Boolean pedidoPeloSecretario, 	AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		String idRelator = new VotoNe().consultarIdRelator(audienciaProcessoDt.getId());
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = pendenciaNe.gerarPendenciaPedidoSustentacaoOral(idRelator, null,
				usuario.getUsuarioDt(), idProcesso, procParte, obFabricaConexao);
		ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
		SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
		setDadosSustentacaoOral(sustentacaoOralDt, audienciaProcessoDt, usuario.getUsuarioDt(), procParteAdv);
		sustentacaoOralDt.setPendenciaDt(pendenciaDt);
		inserirSustentacaoOral(sustentacaoOralDt, obFabricaConexao);
	
		// jvosantos 13/03/2020 17:57 - gravando audi_proc_pend na geração do pedido de S.O., para que a pendência possa ser rastreada através do novo fluxo.
		// mrbatista 30/09/2019 10:41 - gravando audi_proc_pende na geração do pedido de S.O., para que a pendência possa ser rastreada através do novo fluxo.
		
		if (pendenciaDt != null)
			audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), audienciaProcessoDt.getId(), obFabricaConexao);
		
		if (!pedidoPeloSecretario) {
			PendenciaDt pendenciaSecretario = pendenciaNe.gerarPendenciaPedidoSustentacaoOral(null,
					processoDt.getId_Serventia(), usuario.getUsuarioDt(), idProcesso, procParte, obFabricaConexao);
			if (pendenciaSecretario != null)
				audienciaProcessoPendenciaNe.salvar(pendenciaSecretario.getId(), audienciaProcessoDt.getId(),
						obFabricaConexao);
	
		}
	}
	
	
		/**
	 * Método que defere um pedido de sustentação oral sem a necessidade da existencia da pendencia de verificar pedido de sustentação oral
	 * 
	 * @param idProcesso
		 * @param usuario
	 * @param procParteAdv
	 * @param audienciaProcessoDt
	 * @param obFabricaConexao
	 * @author lrcampos
	 * @since 09/04/2020 - 14:55
	 */
	private void deferirAutomaticamenteSolicitacaoSO(String idProcesso, UsuarioNe usuario, String procParteAdv,
			AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
	
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoParteAdvogadoDt procParteAdvogadoDt = new ProcessoParteAdvogadoNe().consultarId(procParteAdv);
		PendenciaDt pendenciaDt = pendenciaNe.gerarPendenciaPedidoSustentacaoOralDeferido(
				procParteAdvogadoDt.getId_UsuarioServentiaAdvogado(), null, usuario.getUsuarioDt(), idProcesso,
				obFabricaConexao);
		pendenciaNe.gerarPendenciaPedidoSustentacaoOralDeferido(
				null, usuario.getUsuarioDt().getId_Serventia(), usuario.getUsuarioDt(), idProcesso,
				obFabricaConexao);
		SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
		setDadosSustentacaoOral(sustentacaoOralDt, audienciaProcessoDt, usuario.getUsuarioDt(), procParteAdv);
		sustentacaoOralDt.setPendenciaDt(pendenciaDt);
		inserirSustentacaoOral(sustentacaoOralDt, obFabricaConexao);
	
		if (pendenciaDt != null)
			audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), audienciaProcessoDt.getId(), obFabricaConexao);
	
	}
	
	private void gerarPendenciasVerificarPedidoSOAutomatico(String idProcesso, UsuarioNe usuario, String procParte,
			String procParteAdv, Boolean pedidoPeloSecretario, 	AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		PendenciaSessaoVirtualNe pendenciaSessaoVirtualne = new PendenciaSessaoVirtualNe();
		ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
		if(!usuario.getGrupoCodigo().equals(String.valueOf(GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL))){
			 pendenciaSessaoVirtualne.gerarPendenciaSessaoVirtualPJD(null, null, usuario.getUsuarioDt(), idProcesso, null, audienciaProcessoDt.getId(), PendenciaTipoDt.VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO, obFabricaConexao, null);
			
		}
	
		PendenciaDt pendenciaDt = pendenciaSessaoVirtualne.gerarPendenciaSessaoVirtualPJD(null, processoDt.getId_Serventia(), usuario.getUsuarioDt(), idProcesso, null, audienciaProcessoDt.getId(), PendenciaTipoDt.VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO, obFabricaConexao, null);
		SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
		setDadosSustentacaoOral(sustentacaoOralDt, audienciaProcessoDt, usuario.getUsuarioDt(), procParteAdv);
		sustentacaoOralDt.setPendenciaDt(pendenciaDt);
		inserirSustentacaoOral(sustentacaoOralDt, obFabricaConexao);
	
		// jvosantos 13/03/2020 17:57 - gravando audi_proc_pend na geração do pedido de S.O., para que a pendência possa ser rastreada através do novo fluxo.
		// mrbatista 30/09/2019 10:41 - gravando audi_proc_pende na geração do pedido de S.O., para que a pendência possa ser rastreada através do novo fluxo.
		
		if (pendenciaDt != null)
			audienciaProcessoPendenciaNe.salvar(pendenciaDt.getId(), audienciaProcessoDt.getId(), obFabricaConexao);
		
//		if (!pedidoPeloSecretario) {
//			PendenciaDt pendenciaSecretario = pendenciaNe.gerarPendenciaPedidoSustentacaoOral(null,
//					processoDt.getId_Serventia(), usuario.getUsuarioDt(), idProcesso, procParte, obFabricaConexao);
//			if (pendenciaSecretario != null)
//				audienciaProcessoPendenciaNe.salvar(pendenciaSecretario.getId(), audienciaProcessoDt.getId(),
//						obFabricaConexao);
//	
//		}
	}

}
