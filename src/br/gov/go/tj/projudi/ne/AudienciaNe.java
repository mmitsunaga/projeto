package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssinarSessaoSegundoGrauDt;
import br.gov.go.tj.projudi.dt.AudienciaAgendaDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ImpedimentoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.dt.RelatorioAudienciaProcesso;
import br.gov.go.tj.projudi.dt.RelatorioAudienciaProcessoParteAdvogado;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.SessaoSegundoGrauProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaPs;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao.FinalizacaoAdiamento;
import br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao.FinalizacaoExtratoAta;
import br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao.FinalizacaoNormal;
import br.gov.go.tj.projudi.sessaoVirtual.extratoAta.finalizacao.FinalizacaoRetirarPauta;
import br.gov.go.tj.projudi.sessaoVirtual.votante.AlterarIntegranteTemplate;
import br.gov.go.tj.projudi.sessaoVirtual.votante.AlterarMPTemplate;
import br.gov.go.tj.projudi.sessaoVirtual.votante.AlterarPresidenteTemplate;
import br.gov.go.tj.projudi.util.AudienciaAgendaValidacao;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.utils.DiaDaSemana;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;
import br.gov.go.tj.utils.pdf.GerarPDF;
import br.gov.go.tj.utils.pdf.InterfaceJasper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

@SuppressWarnings("all")
public class AudienciaNe extends AudienciaNeGen {

	private static final long serialVersionUID = 3136279605157829821L;

	private String retornoValidacao = "";

	protected String ultimaConsulta = "%";

	private String logHorariosDuracao = "";
	
	public static String prefixoArquivoEmenta = "ASSINATURA_EMENTA_";
	public static String nomeArquivoEmenta = "Ementa.html";	
	
	/**
	 * CRIAÇÃO DE AGENDA(S) PARA AUDIÊNCIAS
	 * 
	 * Método responsável por abrir uma conexão com o banco de dados para inserir ou alterar uma lista de objetos do tipo "AudienciaDt" cada qual
	 * contendo objeto(s) do tipo "AudienciaProcessoDt", ou seja, criar agenda(s) para audiências dos tipos: conciliação, instrução, preliminar, una.
	 * 
	 * @author Keila Sousa Silva
	 * @param listaAudienciaDt
	 * @param audienciaAgendaDt
	 * @throws Exception
	 */
	public void salvarAgendaAudiencia(AudienciaAgendaDt audienciaAgendaDt, String[] horariosDuracao) throws Exception {
		LogDt logDt = null;
		FabricaConexao obFabricaConexao = null;
		try{
			// Criar objeto do tipo "AudienciaProcessoNe"
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();

			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			// INICIAR TRANSAÇÃO
			obFabricaConexao.iniciarTransacao();

			// SET CONEXÃO
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// GERAR DATAS PARA AGENDAS DE AUDIÊNCIAS
			List listaDatasAgendasAudiencias = this.gerarDatasAgendasAudiencias(audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getDataFinal(), audienciaAgendaDt.getQuantidadeAudienciasSimultaneas(), horariosDuracao);

			// GERAR AGENDAS DE AUDIÊNCIAS
			//List listaAgendasAudienciasGeradas = this.buscarAgendasAudienciasGeradas(listaDatasAgendasAudiencias, audienciaAgendaDt);
			
			Iterator iterator = listaDatasAgendasAudiencias.iterator();
			while (iterator.hasNext()) {
				// AUDIÊNCIADT
				AudienciaDt audienciaDtGerar = new AudienciaDt();
				// Tipo da audiência
				audienciaDtGerar.setId_AudienciaTipo(audienciaAgendaDt.getId_AudienciaTipo());
				// Data da agenda (Data da audiência livre)
				audienciaDtGerar.setDataAgendada(Funcoes.DataHora((Date) iterator.next()));
				// Id da Serventia
				audienciaDtGerar.setId_Serventia(audienciaAgendaDt.getId_Serventia());
				// Log
				audienciaDtGerar.setId_UsuarioLog(audienciaAgendaDt.getId_UsuarioLog());
				audienciaDtGerar.setIpComputadorLog(audienciaAgendaDt.getIpComputadorLog());

				// AUDIENCIAPROCESSODT
				/*
				 * No caso de geração manual de agendas de audiências, cada audiência possui apenas uma referência à tabela "AudienciaProcesso", ou seja,
				 * a lista de objetos do tipo "AudienciaProcessoDt" contida no objeto do tipo "AudienciaDt" possuirá apenas um registro do tipo
				 * "AudienicaProcessoDt". Isso se deve ao fato de que somente audiências do tipo "Sessão de 2º Grau" possuem várias referências à tabela
				 * "AudienciaProcesso" e esse tipo de agenda de audiência será criada automaticamente.
				 */
																
				// LISTA DE OBJETOS DO TIPO "AUDIENCIADT" A SER RETORNADA
				//listaAgendasAudienciasGeradas.add(audienciaDtGerar);
				
				obPersistencia.inserir(audienciaDtGerar);		
				
				AudienciaProcessoDt audienciaProcessoDt = audienciaAgendaDt.getAudienciaProcessoDt();
				audienciaProcessoDt.setId("");
				audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.LIVRE));							
				audienciaProcessoDt.setId_Audiencia(audienciaDtGerar.getId());
				audienciaProcessoDt.setId_UsuarioLog(audienciaDtGerar.getId_UsuarioLog());
				audienciaProcessoDt.setIpComputadorLog(audienciaDtGerar.getIpComputadorLog());
				
				audienciaProcessoNe.salvarAudienciaProcesso(audienciaProcessoDt, obFabricaConexao);
				
				logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaProcessoDt.getPropriedades());
				obLog.salvar(logDt, obFabricaConexao);
				
				// LOG Preparação do objeto de log referente à inserção de audiência(s)
				logDt = new LogDt("Audiencia", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaProcessoDt.getPropriedades());
				// SALVAR LOG DA INSERÇÃO OU ATUALIZAÇÃO DO OBJETO DO TIPO "AUDIENCIADT"
				obLog.salvar(logDt, obFabricaConexao);
			}

//			// Percorrer a lista contendo objeto(s) do tipo "AudienciaDt"
//			Iterator iteratorAudienciaDt = listaAgendasAudienciasGeradas.iterator();
//			while (iteratorAudienciaDt.hasNext()) {
//				AudienciaDt audienciaDt = (AudienciaDt) iteratorAudienciaDt.next();
//
//				// INSERÇÃO (AUDIENCIADT) (CRIAÇÃO DE AGENDA(S) PARA AUDIÊNCIAS)
//				if (audienciaDt.getId().equalsIgnoreCase("")) {
//					// Inserir o(s) objeto(s) do tipo "AUDIENCIADT"
//					obPersistencia.inserir(audienciaDt);
//
//					// Inserir o(s) objeto(s) do tipo "AUDIENCIAPROCESSODT"
//					for (int i = 0; i < audienciaDt.getListaAudienciaProcessoDt().size(); i++) {
//						AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) audienciaDt.getListaAudienciaProcessoDt(audienciaDt).get(i);
//						audienciaProcessoNe.salvarAudienciaProcesso(audienciaProcessoDt, obFabricaConexao);
//						logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaProcessoDt.getPropriedades());
//						obLog.salvar(logDt, obFabricaConexao);
//					}
//				}
//				// COPIAR OBJETO DO TIPO "AUDIENCIADT" QUE FOI INSERIDO OU ATUALIZADO
//				obDados.copiar(audienciaDt);
//				// LOG Preparação do objeto de log referente à inserção de audiência(s)
//				logDt = new LogDt("Audiencia", audienciaDt.getId(), audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaDt.getPropriedades());
//				// SALVAR LOG DA INSERÇÃO OU ATUALIZAÇÃO DO OBJETO DO TIPO "AUDIENCIADT"
//				obLog.salvar(logDt, obFabricaConexao);
//			}// Fim while

			// FINALIZAR TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSAÇÃO
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável por agendar automaticamente uma audiência para um processo cível. 
	 * Depois do agendamento, serão geradas as pendências do tipo "Intimação Expedida" para o advogado ou partes promoventes(atermação), 
	 * e pendência do tipo "Citação" para as partes promovidas
	 * 
	 * @author msapaula
	 * @param processoCivelDt, objeto com dados do processo
	 * @param serventiaDt, objeto com dados da serventia
	 * @param usuarioDt, objeto com dados do usuário
	 * @param atermacao, booleano para indicar se trata-se de atermação
	 */
	public AudienciaDt agendarAudienciaCadastroProcessoCivel(ProcessoCadastroDt processoCivelDt, ServentiaDt serventiaDt, UsuarioDt usuarioDt, boolean atermacao, FabricaConexao fabricaConexao) throws Exception {
		AudienciaDt audienciaDtAgendada = null;
		AudienciaTipoNe audienciaTipoNe = new AudienciaTipoNe();
		String audienciaTipoCodigo = null;
		
		LogDt logDtProcessoCivel = new LogDt(processoCivelDt.getId_UsuarioLog(), processoCivelDt.getIpComputadorLog());

		// Se serventia é mista, Cível e Criminal, para processo cível buscará audiência do tipo Conciliação
		if (Funcoes.StringToInt(serventiaDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL) audienciaTipoCodigo = String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo());
		//Senão busca o tipo de audiência definido no cadastro da serventia
		else if (serventiaDt.getId_AudienciaTipo() != null && serventiaDt.getId_AudienciaTipo().length() > 0) audienciaTipoCodigo = audienciaTipoNe.consultarId(serventiaDt.getId_AudienciaTipo()).getAudienciaTipoCodigo();

		if (audienciaTipoCodigo != null) {
			// AGENDAR AUDIÊNCIA PARA O PROCESSO
			audienciaDtAgendada = agendarAudienciaProcessoAutomaticamente(audienciaTipoCodigo, processoCivelDt.getId(), processoCivelDt.getId_Serventia(), fabricaConexao);

			// GERA MOVIMENTAÇÃO E PENDÊNCIAS PARA PROCESSO CÍVEL
			if (audienciaDtAgendada != null) {
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoCivelDt, audienciaDtAgendada.getAudienciaTipoCodigo(), UsuarioServentiaDt.SistemaProjudi, audienciaDtAgendada, logDtProcessoCivel, fabricaConexao);
				gerarPendenciasAudienciaCivel(processoCivelDt, movimentacaoAudiencia, usuarioDt, atermacao, fabricaConexao);
			}
		}
				
		return audienciaDtAgendada;
	}

	public String consultarAudienciasProcessoDescricao(String numeroProcesso) throws Exception {
		String statusDescricao = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// Consultar descrição do status da audiencia de um processo.
			statusDescricao = obPersistencia.consultarAudienciasProcessoDescricao(numeroProcesso);
		
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		return statusDescricao;
	}
	
	public String consultarAudiProcStatusDescricaoPeIdAudiProc(String idAudiProc) throws Exception {
		String statusDescricao = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// Consultar descrição do status da audiencia de um processo.
			statusDescricao = obPersistencia.consultarAudiProcStatusDescricaoPeIdAudiProc(idAudiProc);
		
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		return statusDescricao;
	}

	public List<AudienciaProcessoStatusDt> consultarAudienciaProcesso() throws Exception {
		List<AudienciaProcessoStatusDt> statusAudiencia = new ArrayList<AudienciaProcessoStatusDt>();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// Consultar descrição do status da audiencia de um processo.
			statusAudiencia = obPersistencia.consultarAudienciaProcesso();
		
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		return statusAudiencia;
	}
	
	/**
	 * Método responsável por agendar automaticamente uma audiência para um processo criminal. 
	 * Depois do agendamento, serão geradas as pendências do tipo "Intimação Expedida" para as partes promoventes (atermação) 
	 * ou para o advogado, e pendência do tipo "Intimação" para as partes promovidas
	 * 
	 * @author msapaula
	 * @param processoCriminalDt, objeto com dados do processo
	 * @param serventiaDt, objeto com dados da serventia
	 * @param usuarioDt, objeto com dados do usuário
	 * @param atermacao, booleano para indicar se trata-se de atermação
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaCadastroProcessoCriminal(ProcessoCadastroDt processoCriminalDt, ServentiaDt serventiaDt, UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;
		AudienciaTipoNe audienciaTipoNe = new AudienciaTipoNe();
		String audienciaTipoCodigo = null;
		
		LogDt logDt = new LogDt(processoCriminalDt.getId_UsuarioLog(), processoCriminalDt.getIpComputadorLog());

		// Se serventia é mista, Cível e Criminal, para processo criminal buscará audiência do tipo Preliminar
		if (Funcoes.StringToInt(serventiaDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL) audienciaTipoCodigo = String.valueOf(AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo());
		//Senão busca o tipo de audiência definido no cadastro da serventia
		else if (serventiaDt.getId_AudienciaTipo() != null && serventiaDt.getId_AudienciaTipo().length() > 0) audienciaTipoCodigo = audienciaTipoNe.consultarId(serventiaDt.getId_AudienciaTipo()).getAudienciaTipoCodigo();

		if (audienciaTipoCodigo != null) {
			// AGENDAR AUDIÊNCIA PARA O PROCESSO
			audienciaDtAgendada = agendarAudienciaProcessoAutomaticamente(audienciaTipoCodigo, processoCriminalDt.getId(), processoCriminalDt.getId_Serventia(), fabricaConexao);

			// GERA MOVIMENTAÇÃO E PENDÊNCIAS PARA PROCESSO CRIMINAL
			if (audienciaDtAgendada != null) {
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoCriminalDt, audienciaDtAgendada.getAudienciaTipoCodigo(), UsuarioServentiaDt.SistemaProjudi, audienciaDtAgendada, logDt, fabricaConexao);
				gerarPendenciasAudienciaCriminal(processoCriminalDt, movimentacaoAudiencia, usuarioDt, fabricaConexao);
			}
		}
				
		return audienciaDtAgendada;
	}
	
	/**
	 * Método responsável por agendar automaticamente uma audiência preliminar para um processo criminal. 
	 * Depois do agendamento, serão geradas as pendências do tipo "Intimação Expedida" para as partes promoventes (atermação) 
	 * ou para o advogado, e pendência do tipo "Intimação" para as partes promovidas
	 * 
	 * @author jrcorrea 17/10/2016
	 * @param processoCriminalDt, objeto com dados do processo
	 * @param serventiaDt, objeto com dados da serventia
	 * @param usuarioDt, objeto com dados do usuário
	 * @param atermacao, booleano para indicar se trata-se de atermação
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaPreliminarConciliadorCadastroProcessoCriminal(ProcessoCadastroDt processoCriminalDt,  UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;		
		String audienciaTipoCodigo = null;
		
		LogDt logDt = new LogDt(processoCriminalDt.getId_UsuarioLog(), processoCriminalDt.getIpComputadorLog());

		// defino que a audiencia sera de conciliacao
		audienciaTipoCodigo = String.valueOf(AudienciaTipoDt.Codigo.PRELIMINAR_CONCILIADOR.getCodigo());

		if (audienciaTipoCodigo != null) {
			// AGENDAR AUDIÊNCIA PARA O PROCESSO
			audienciaDtAgendada = agendarAudienciaProcessoAutomaticamente(audienciaTipoCodigo, processoCriminalDt.getId(), processoCriminalDt.getId_Serventia(), fabricaConexao);

			// GERA MOVIMENTAÇÃO E PENDÊNCIAS PARA PROCESSO CRIMINAL
			if (audienciaDtAgendada != null) {
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoCriminalDt, audienciaDtAgendada.getAudienciaTipoCodigo(), UsuarioServentiaDt.SistemaProjudi, audienciaDtAgendada, logDt, fabricaConexao);
				gerarPendenciasAudienciaCriminal(processoCriminalDt, movimentacaoAudiencia, usuarioDt, fabricaConexao);
			}
		}
				
		return audienciaDtAgendada;
	}

	/**
	 * Método responsável por agendar automaticamente uma audiência para um processo.
	 * Busca a próxima audiência livre, já faz um UPDATE na tabela para garantir que outro usuário não pegue a mesma
	 * e retorna a agenda utilizada
	 * 
	 * @author msapaula
	 * 
	 * @param audienciaTipoCodigo, tipo da audiência a ser marcada
	 * @param id_Processo, processo para o qual será marcada audiência
	 * @param id_Serventia, serventia do processo
	 * @param fabricaConexao, conexão ativa
	 * @throws Exception 
	 */
	private AudienciaDt agendarAudienciaProcessoAutomaticamente(String audienciaTipoCodigo, String id_Processo, String id_Serventia, FabricaConexao fabricaConexao) throws Exception{
		AudienciaDt audienciaDtParaAgendar = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();	
				
		AudienciaPs obPersistencia = new AudienciaPs(fabricaConexao.getConexao());
		
		if (audienciaProcessoNe.agendarAudienciaProcessoAutomatico(audienciaTipoCodigo, id_Processo, id_Serventia, fabricaConexao)){
			audienciaDtParaAgendar = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaTipoCodigo, id_Processo);
		}
							
		return audienciaDtParaAgendar;
	}
	
	/**
	 * Método responsável por agendar audiência automaticamente para um processo já cadastrado.
	 * 
	 * @param audienciaDt, dados para agendamento de Audiência
	 * @param usuarioDt, usuário que está agendando audiência
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaAutomaticamenteProcesso(AudienciaDt audienciaDt, UsuarioDt usuarioDt) throws Exception{
		return this.agendarAudienciaAutomaticamenteProcesso(audienciaDt, usuarioDt, null);
	}

	/**
	 * Método responsável por agendar audiência automaticamente para um processo já cadastrado.
	 * 
	 * @param audienciaDt, dados para agendamento de Audiência
	 * @param usuarioDt, usuário que está agendando audiência
	 * @param fabConexao, fabrica para poder continuar uma trasacao
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaAutomaticamenteProcesso(AudienciaDt audienciaDt, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;
		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()));

		switch (tipoCodigo) {
			//Audiências que precisam ficar vinculadas a um juiz
			case INSTRUCAO:
			case INSTRUCAO_JULGAMENTO:
			case JULGAMENTO: {
				// Será localizada a próxima audiência livre para o cargo do juiz do processo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentiaCargo(audienciaDt, usuarioDt, fabConexao);
				break;
			}
			//audiências que não precisam ficar vinculadas a juiz
			default: {
				// Será localizada próxima audiência livre independente do Cargo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentia(audienciaDt, usuarioDt, fabConexao);
				break;
			}
		}

		return audienciaDtAgendada;
	}

	/**
	 * Método responsável por agendar automaticamente uma audiência para um processo já cadastrado.
	 * Busca a próxima audiência livre conforme o tipo passado e na serventia, pegando assim qualquer audiência que seja
	 * a próxima não importando a ordem de distribuição, ou se a pauta é de juiz ou conciliador.
	 * Já faz um UPDATE na tabela para garantir que outro usuário não pegue a mesma e retorna a agenda utilizada.
	 * 
	 * @param audienciaDt, objeto com dados da audiência a ser agendada
	 * @param usuarioDt, usuário que está agendando audiência
	 * @param fabConexao, fabrica para poder continuar uma trasacao
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private AudienciaDt agendarAudienciaAutomaticamenteServentia(AudienciaDt audienciaDt, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		FabricaConexao obFabricaConexao = null;
		try{
			LogDt logDt = new LogDt(audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog());

			// Verifica se a conexao sera criada internamente
            if (fabConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = fabConexao;
            }
			
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
			ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();

			// AGENDAR AUDIÊNCIA PARA O PROCESSO
			if (audienciaProcessoNe.agendarAudienciaProcessoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoDt.getId(), audienciaDt.getId_Serventia(), obFabricaConexao)){				
				audienciaDtAgendada = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoDt.getId());

				if (audienciaDtAgendada != null) {
					// Gera Movimentação Audiência Marcada
					MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoDt, audienciaDtAgendada.getAudienciaTipoCodigo(), usuarioDt.getId_UsuarioServentia(), audienciaDtAgendada, logDt, obFabricaConexao);
					// PENDÊNCIA(S)
					String id_serventia = null;
					if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo(), 0) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
						id_serventia = processoDt.getId_Serventia();
					} else{
						id_serventia = usuarioDt.getId_Serventia();
					}
					gerarPendenciasAudiencia(processoDt, movimentacaoAudiencia, usuarioDt.getId_UsuarioServentia(), id_serventia, logDt, obFabricaConexao);
				}			
			}
			
			 if (fabConexao == null) {
				 obFabricaConexao.finalizarTransacao();
			 }
			
		} catch(Exception e) {
			 // Se a conexao foi criada dentro do metodo, entao cancela a conexao
			 if (fabConexao == null) {
				  obFabricaConexao.cancelarTransacao();
			 }
			throw e;
		} finally{
			// Se a conexao foi criada dentro do metodo, entao finaliza a conexao
            if (fabConexao == null) {
            	obFabricaConexao.fecharConexao();
            }
		}
		return audienciaDtAgendada;
	}

	/**
	 * Método responsável por agendar automaticamente uma audiência para um processo já cadastrado.
	 * Busca a próxima audiência livre para o ServentiaCargo passado. 
	 * 
	 * @param audienciaDt, objeto com dados da audiência a ser agendada
	 * @param usuarioDt, usuário que está agendando audiência
	 * @param fabConexao, fabrica para poder continuar uma trasacao
	 * 
	 * @author msapaula, hmgodinho
	 * @throws Exception 
	 */
	private AudienciaDt agendarAudienciaAutomaticamenteServentiaCargo(AudienciaDt audienciaDt, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();	
		FabricaConexao obFabricaConexao = null;
		try{
			// Verifica se a conexao sera criada internamente
            if (fabConexao == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else {
                // Caso da conexao criada em um nivel superior
                obFabricaConexao = fabConexao;
            }
			
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			LogDt logDt = new LogDt(audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog());

			AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
			ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();

			//JUIZ RESPONSÁVEL - Serão localizadas as audiências livres referentes a esse cargo serventia do juiz responsável pelo processo.
			ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
			ServentiaCargoDt serventiaCargoDt = processoResponsavelNe.getJuizResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia());
			//AGENDAR AUDIÊNCIA PARA O JUIZ DO PROCESSO
			if (audienciaProcessoNe.agendarAudienciaProcessoAutomaticoServentiaCargo(serventiaCargoDt.getId(), audienciaDt.getAudienciaTipoCodigo(), processoDt.getId(), obFabricaConexao)){
				audienciaDtAgendada = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoDt.getId());

				if (audienciaDtAgendada != null) {
					// Gera Movimentação Audiência Marcada
					MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoDt, audienciaDtAgendada.getAudienciaTipoCodigo(), usuarioDt.getId_UsuarioServentia(), audienciaDtAgendada, logDt, obFabricaConexao);
					// PENDÊNCIA(S)
					String id_serventia = null;
					if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo(), 0) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
						id_serventia = processoDt.getId_Serventia();
					} else{
						id_serventia = usuarioDt.getId_Serventia();
					}
					gerarPendenciasAudiencia(processoDt, movimentacaoAudiencia, usuarioDt.getId_UsuarioServentia(), id_serventia, logDt, obFabricaConexao);
				}				
			}
			
			if (fabConexao == null) {
				obFabricaConexao.finalizarTransacao();	
			}
			
		} catch(Exception e) {
			 // Se a conexao foi criada dentro do metodo, entao cancela a conexao
            if (fabConexao == null) {
            	obFabricaConexao.cancelarTransacao();
            }
			throw e;
		} finally{
			 // Se a conexao foi criada dentro do metodo, entao finaliza a conexao
            if (fabConexao == null) {
            	obFabricaConexao.fecharConexao();
            }
		}
		return audienciaDtAgendada;
	}

	/**
	 * Método responsável por agendar audiência para um processo manualmente. 
	 * 
	 * @param audienciaDtAgendar, objeto com dados da audiência a ser agendada
	 * @param usuarioDt, usuário que está agendando audiência
	 * 
	 * @author msapaula
	 */
	public boolean agendarAudienciaManualmente(AudienciaDt audienciaDtAgendar, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		boolean resultado = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			AudienciaProcessoDt audienciaProcessoDt = audienciaDtAgendar.getAudienciaProcessoDt();
			ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();

			// Seta Id_Processo e Status em AudienciaProcesso
			audienciaProcessoDt.setId_Processo(processoDt.getId());
			audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA));

			// SALVA ATUALIZAÇÃO DE "AUDIENCIAPROCESSODT"
			if (audienciaProcessoNe.agendarAudienciaProcessoManual(audienciaProcessoDt, obFabricaConexao)){
				
				resultado = true;
				
				// PREPARA OBJETO DO TIPO LOGDT
				LogDt logDt = new LogDt(audienciaDtAgendar.getId_UsuarioLog(), audienciaDtAgendar.getIpComputadorLog());

				// Gera Movimentação Audiência Marcada
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoDt, audienciaDtAgendar.getAudienciaTipoCodigo(), usuarioDt.getId_UsuarioServentia(), audienciaDtAgendar, logDt, obFabricaConexao);

				// PENDÊNCIA(S)
				String id_serventia = null;
				if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo(), 0) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
					id_serventia = processoDt.getId_Serventia();
				} else{
					id_serventia = usuarioDt.getId_Serventia();
				}
				gerarPendenciasAudiencia(processoDt, movimentacaoAudiencia, usuarioDt.getId_UsuarioServentia(), id_serventia, logDt, obFabricaConexao);			

			}
			
			// COMMIT TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
						
			return resultado;
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}

	/**
	 * Método responsável por criar uma conexão com o banco de dados e consultar audiências livres e válidas para o agendamento/reagendamento manual
	 * 
	 * @author Keila Sousa Silva, hmgodinho
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * 
	 * @return AudienciaDt listaAudienciasLivres
	 * @throws Exception
	 */
	public List consultarAudienciasLivresAgendamentoManual(AudienciaDt audienciaDt, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		List listaAudienciasLivres = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			AudienciaTipoDt.Codigo codigoTipo = AudienciaTipoDt.Codigo.NENHUM;
			codigoTipo = codigoTipo.getCodigo(Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()));
			
			if(codigoTipo == null) {
				throw new Exception("Não foi possível localizar o mapeamento do tipo de audiência " + audienciaDt.getAudienciaTipo() + ". Favor entrar em contato com o suporte.");	
			}

			// CONSULTAR AUDIÊNCIAS LIVRES
			switch (codigoTipo) {
				// Audiências que precisam ficar vinculadas a um juiz
				case INSTRUCAO:
				case JULGAMENTO:
				case INSTRUCAO_JULGAMENTO: {
					listaAudienciasLivres = obPersistencia.consultarAudienciasLivresAgendamentoManual(audienciaDt, id_Serventia,true, posicaoPaginaAtual);
					break;
				}
				// As demais audiências que não precisam ficar vinculadas a um juiz
				default: {
					listaAudienciasLivres = obPersistencia.consultarAudienciasLivresAgendamentoManual(audienciaDt, id_Serventia, false, posicaoPaginaAtual);
					break;
				}
			}

			// DEFINIR QUANTIDADE DE PÁGINAS
			QuantidadePaginas = (Long) listaAudienciasLivres.get(listaAudienciasLivres.size() - 1);

			// REMOVER DA LISTA DE AUDIÊNCIAS LIVRES CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE PÁGINAS
			listaAudienciasLivres.remove(listaAudienciasLivres.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasLivres;
	}

	/**
	 * Método responsável por validar o agendamento/reagendamento de uma audiência para um dado processo, ou seja, verificar se o processo possui uma
	 * audiência pendente (Status da audiência pendente = "A Ser Realizada" = 1) cujo tipo da audiência seja o mesmo da audiência livre selecionada.
	 * Caso já exista essa audiência, o agendamento/reagendamento dessa audiência livre para esse processo não poderá ocorrer. Se for retornada uma
	 * audiência, ou seja, quantidade de audiência existente será igual a 1(um) isso significa que existe uma audiência pendente semelhante à
	 * audiência livre e o agendamento/reagendamento não poderá ocorrer
	 * 
	 * @author Keila Sousa Silva
	 * @author mmgomes
	 * @param audienciaDtAgendar
	 * @param CriarFabricaConexao
	 * @return audienciaInexistente: boolean que indicará se foi retornada alguma audiência
	 * @throws Exception
	 */
	public String validarAudienciaAgendamento(AudienciaDt audienciaDtAgendar, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";
		int quantidadeAudienciaExistente = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			
			if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo(), 0) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
				ProcessoNe processoNe = new ProcessoNe();
				if (!processoNe.podeAcessarProcesso(usuarioDt, processoDt, obFabricaConexao)){
					stRetorno += " Sem permissão para Agendar Audiência em processo de outra serventia.";
				}
			
			} else {
				// Se usuário for de serventia diferente do processo, não poderá agendar
				if (processoDt.getId_Serventia() != null && usuarioDt.getId_Serventia() != null && 
					!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia()) && 
				    audienciaDtAgendar.getAcessoOutraServentia()!= null && audienciaDtAgendar.getAcessoOutraServentia().equals("") &&
				    !(Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.PREPROCESSUAL && 
				       (Funcoes.StringToInt(audienciaDtAgendar.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
						Funcoes.StringToInt(audienciaDtAgendar.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
				        Funcoes.StringToInt(audienciaDtAgendar.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()))) {
					stRetorno += " Sem permissão para Agendar Audiência em processo de outra serventia.";
				}
			}

			if (audienciaDtAgendar.getAudienciaTipoCodigo() == null || audienciaDtAgendar.getAudienciaTipoCodigo().equals("")) {
				stRetorno += " O tipo de Audiência não foi informado. \n";
			}

			// Verifica se já existe audiência marcada
			quantidadeAudienciaExistente = obPersistencia.validarAudienciaAgendamento(processoDt.getId());
			if (quantidadeAudienciaExistente > 0) {
				stRetorno += "Agendamento de audiência não pode ser realizado. O processo já possui uma audiência com status pendente.";
			}		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Método responsável por gerar a movimentação referente à audiência marcada. De acordo com o tipo da audiência que está sendo marcada, faz
	 * chamada ao método que irá gerar a movimentação correspondente.
	 * 
	 * @param processoDt
	 *            , dt do processo que terá uma audiência marcada
	 * @param audienciaTipoCodigo
	 *            , tipo da audiência que está sendo marcada
	 * @param id_UsuarioRealizador
	 *            , usuário que está marcando a audiência
	 * @param audienciaDtParaAgendar
	 *            , dt da audiência que está sendo marcada
	 * @param logDt
	 *            , dados do log
	 * @param fabricaConexao
	 *            , conexão ativa
	 * 
	 * @author msapaula, hmgodinho
	 * @throws Exception 
	 */
	private MovimentacaoDt gerarMovimentacaoAudienciaMarcada(ProcessoDt processoDt, String audienciaTipoCodigo, String id_UsuarioRealizador, AudienciaDt audienciaDtParaAgendar, LogDt logDt, FabricaConexao fabricaConexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		MovimentacaoDt movimentacaoAudiencia = null;

		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(audienciaTipoCodigo));
		String complemento = "(Agendada para " + audienciaDtParaAgendar.getDataAgendada() + ")";
		if (!audienciaDtParaAgendar.getAcessoOutraServentia().equals("") && Funcoes.StringToInt(audienciaDtParaAgendar.getAcessoOutraServentia()) == PendenciaTipoDt.CARTA_PRECATORIA) {
			complemento += " - Carta Precatória";
		}

		movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaMarcada(processoDt.getId(), id_UsuarioRealizador, complemento, audienciaTipoCodigo, logDt, fabricaConexao);
		movimentacaoNe.gerarMovimentosComplementos(movimentacaoAudiencia, audienciaDtParaAgendar, fabricaConexao);
		return movimentacaoAudiencia;
	}

	/**
	 * Método utilizado no cadastro de processo cível para gerar pendência(s) referente(s) à audiência marcada
	 * 
	 * @param processoDt, processo que está sendo cadastrado
	 * @param movimentacaoAudiencia, movimentação vinculada a audiência
	 * @param usuarioDt, usuário que está cadastrando o processo
	 * @param atermacao, define se trata de atermação
	 * @param fabricaConexao, conexão ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private void gerarPendenciasAudienciaCivel(ProcessoDt processoDt, MovimentacaoDt movimentacaoAudiencia, UsuarioDt usuarioDt, boolean atermacao, FabricaConexao fabricaConexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();

		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());

		if (atermacao) {
			// Gera intimação para cada uma das partes promoventes
			List listaPromoventes = processoDt.getListaPolosAtivos();
			for (int i = 0; i < listaPromoventes.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaPromoventes.get(i);
				movimentacaoNe.gerarMovimentacaoIntimacaoEfetivadaParteProcesso(processoDt.getId(), processoParteDt.getNome(), movimentacaoAudiencia.getMovimentacaoTipo(), logDt, fabricaConexao);
			}
		} else {
			// Se advogado, gera intimação somente para esse
			pendenciaNe.gerarIntimacaoEfetivadaAdvogado(processoDt, movimentacaoAudiencia, UsuarioServentiaDt.SistemaProjudi, "", usuarioDt, logDt, fabricaConexao);
		}

		// Gera pendência do tipo "Carta de Citação" para todas as partes promovidas
		List listaPromovidos = processoDt.getListaPolosPassivos();
		if (listaPromovidos != null) {
			for (int i = 0; i < listaPromovidos.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaPromovidos.get(i);
				pendenciaNe.gerarCitacao(processoDt, movimentacaoAudiencia.getId(), UsuarioServentiaDt.SistemaProjudi, processoParteDt.getId_ProcessoParte(), processoDt.getId_Serventia(), logDt, fabricaConexao);
			}
		}

	}

	/**
	 * Método utilizado no cadastro de processo criminal para gerar pendência(s) referente(s) à audiência marcada
	 * 
	 * @param processoDt, processo que está sendo cadastrado
	 * @param movimentacaoAudiencia, movimentação vinculada a audiência
	 * @param usuarioDt, usuário que está cadastrando o processo
	 * @param atermacao, define se trata de atermação
	 * @param fabricaConexao, conexão ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private void gerarPendenciasAudienciaCriminal(ProcessoCadastroDt processoCriminalDt, MovimentacaoDt movimentacaoAudiencia, UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();

		LogDt logDt = new LogDt(processoCriminalDt.getId_UsuarioLog(), processoCriminalDt.getIpComputadorLog());

		// Lista com Partes promoventes e promovidas
		List partesProcesso = processoCriminalDt.getPartesAtivasPassivas();
		// Lista com partes intimadas em delegacia
		List partesIntimadas = processoCriminalDt.getListaPartesIntimadas();

		for (int i = 0; i < partesProcesso.size(); i++) {
			boolean intimacaoDelegacia = false;
			ProcessoParteDt processoParteDt = (ProcessoParteDt) partesProcesso.get(i);
			if (partesIntimadas != null && partesIntimadas.size() > 0) {
				for (int j = 0; j < partesIntimadas.size(); j++) {
					ProcessoParteDt parteIntimadaDt = (ProcessoParteDt) partesIntimadas.get(j);
					if (processoParteDt.getId().equals(parteIntimadaDt.getId())) {
						intimacaoDelegacia = true;
						break;
					}
				}
			}

			// Se parte está sendo intimada na Delegacia, gera movimentação do tipo "Intimação Efetivada"
			if (intimacaoDelegacia) {
				// Gera movimentação do tipo "Intimação Efetivada"
				movimentacaoNe.gerarMovimentacaoIntimacaoEfetivadaParteProcesso(processoCriminalDt.getId(), processoParteDt.getNome(), movimentacaoAudiencia.getMovimentacaoTipo(), logDt, fabricaConexao);
			} else {
				// Gera pendência do Tipo "Intimação" para cartório expedir
				pendenciaNe.gerarIntimacaoServentia(processoCriminalDt, movimentacaoAudiencia.getId(), UsuarioServentiaDt.SistemaProjudi, processoParteDt.getId_ProcessoParte(), processoCriminalDt.getId_Serventia(), logDt, fabricaConexao);
			}
		}

	}

	/**
	 * Método utilizado no agendamento/reagendamento manual ou automático de audiências para gerar pendência(s) referente(s) à audiência marcada para
	 * cada uma das partes do processo
	 * 
	 * @param processoDt
	 *            , dt de processo
	 * @param movimentacaoAudiencia
	 *            , dt da movimentação vinculada a audiência
	 * @param usuarioDt
	 *            , usuário
	 * @param fabricaConexao
	 *            , conexão ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	private void gerarPendenciasAudiencia(ProcessoDt processoDt, MovimentacaoDt movimentacaoAudiencia, String id_UsuarioServentia, String id_Serventia, LogDt logDt, FabricaConexao fabricaConexao) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();

		// Lista com Partes promoventes e promovidas
		List partesProcesso = processoDt.getPartesAtivasPassivas();
		for (int i = 0; i < partesProcesso.size(); i++) {
			ProcessoParteDt parteDt = (ProcessoParteDt) partesProcesso.get(i);
			pendenciaNe.gerarPendenciasAgendamentoAudiencia(processoDt, parteDt, movimentacaoAudiencia, id_UsuarioServentia, id_Serventia, logDt, fabricaConexao);
		}
	}

	/**
	 * Método responsável por abrir a conexão com o banco de dados e consultar as audiências pendentes. A consulta pode ter sido requisitada por
	 * serventuários, advogados, conciliadores ou juizes. Audiências Pendentes Tipo X = (Id_AudiênciaTipo = Tipo de Audiência definido de acordo com o
	 * menu selecionado) + (Id_Processo IS NOT NULL) + (Data da Realizacao IS NULL) + (Id_ServentiaCargo = Cargo da Serventia selecionado pelo
	 * usuário)
	 * 
	 * @author Keila Sousa Silva
	 * @author hrrosa
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciasPendentes
	 * @throws Exception
	 */
	public List consultarAudienciasPendentes(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual) throws Exception {
		return consultarAudienciasPendentes(usuarioDt, audienciaDt, posicaoPaginaAtual, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public List consultarAudienciasPendentes(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
		List listaAudienciasPendentes = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
				//case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:
					listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentesAdvogado(usuarioDt.getId_UsuarioServentia(), audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				//case GrupoDt.MINISTERIO_PUBLICO:
				case GrupoTipoDt.MP:
					listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentesPromotor(usuarioDt.getId_ServentiaCargo(), audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				//case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
				case GrupoTipoDt.ASSESSOR_MP:
					listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentesAdvogado(usuarioDt.getId_UsuarioServentiaChefe(), audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				//case GrupoDt.CONCILIADORES_VARA:
				//case GrupoDt.JUIZES_VARA:
				case GrupoTipoDt.CONCILIADOR_VARA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
					// Consuultar audiências pendentes do usuário (conciliador ou juiz togado)
					listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentesUsuario(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				default:
					// Consultar audiências pendentes do serventuário
					listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentes(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);
					break;
			}

			QuantidadePaginas = (Long) listaAudienciasPendentes.get(listaAudienciasPendentes.size() - 1);
			listaAudienciasPendentes.remove(listaAudienciasPendentes.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasPendentes;
	}
	
	/**
	 * Método responsável em consultar todas as audiências pendentes em uma serventia.
	 * 
	 * @author msapaula
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciasPendentes
	 * @throws Exception
	 */
	public List consultarAudienciasPendentesServentia(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual) throws Exception {
		return consultarAudienciasPendentesServentia(usuarioDt, audienciaDt, posicaoPaginaAtual, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public List consultarAudienciasPendentesServentia(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
		List listaAudienciasPendentes = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// Consultar audiências pendentes do serventuário
			listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentes(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);

			QuantidadePaginas = (Long) listaAudienciasPendentes.get(listaAudienciasPendentes.size() - 1);
			listaAudienciasPendentes.remove(listaAudienciasPendentes.size() - 1);
		
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasPendentes;
	}

	/**
	 * Consulta as Sessões de 2º grau pendentes de acordo com o grupo do usuário passado.
	 * 
	 * @param usuarioDt, identificação do cargo do relator para filtro na pesquisa
	 * @param id_Audiencia, identificação de audiência no caso de retornar somente os processos a serem julgados em uma sessão
	 * @param posicaoPaginaAtual, parâmetro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarSessoesPendentes(UsuarioDt usuarioDt, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {
		List listaSessoesPendentes = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

//			switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {

//				case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:
					if (usuarioDt.getId_UsuarioServentia() != null && usuarioDt.getId_UsuarioServentia().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesAdvogado(usuarioDt.getId_UsuarioServentia());
					break;

//				case GrupoDt.PROMOTORES:
				case GrupoTipoDt.MP:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesPromotor(usuarioDt.getId_ServentiaCargo());
					break;

//				case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
				case GrupoTipoDt.ASSESSOR_MP:
					if (usuarioDt.getId_UsuarioServentiaChefe() != null && usuarioDt.getId_UsuarioServentiaChefe().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesAdvogado(usuarioDt.getId_UsuarioServentiaChefe());
					break;

				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorDesembargador(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.JUIZ_TURMA:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorJuizTurma(usuarioDt.getId_ServentiaCargo(), id_Audiencia);
					break;
					
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesAssistenteGabinete(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;

				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorDesembargador(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePreAnalisadas);
					break;
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorJuizTurma(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia);
					break;

			}
			
			if (listaSessoesPendentes != null)
			{
				for (int i = 0; i < listaSessoesPendentes.size(); i++)
				{
					AudienciaProcessoDt audienciaProcessoDt = ((AudienciaDt) listaSessoesPendentes.get(i)).getAudienciaProcessoDt();
					List listaPromotores = processoResponsavelNe.getListaPromotoresResponsavelProcesso(audienciaProcessoDt.getProcessoDt().getId(), null , obFabricaConexao);
					if(listaPromotores != null && listaPromotores.size() > 0){
						ServentiaCargoDt promotor = (ServentiaCargoDt) listaPromotores.get(0);
						audienciaProcessoDt.setId_ServentiaCargoMP(promotor.getId());
						audienciaProcessoDt.setServentiaCargoMP(promotor.getServentiaCargo());
						audienciaProcessoDt.setNomeMPProcesso(promotor.getNomeUsuario());						
					}
				}
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesPendentes;
	}

	/**
	 * Método responsável por abrir a conexão com o banco de dados e consultar as audiências agendadas para a data corrente. A consulta pode ter sido
	 * requisitada por serventuários, advogados, conciliadores ou juizes. Este método tratará as requisições de consultas provenientes dos
	 * serventuários.
	 * 
	 * @author Keila Sousa Silva
	 * @author hrrosa
	 * @param ordenacao
	 * @param qtdRegistros
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciasParaHoje
	 * @throws Exception
	 */
	public List consultarAudienciasParaHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual) throws Exception {
		return consultarAudienciasParaHoje(usuarioDt, audienciaDt, posicaoPaginaAtual, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public List consultarAudienciasParaHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
		List listaAudienciasParaHoje = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
			    case GrupoTipoDt.ADVOGADO: {
					// Consultar audiências para hoje do advogado
			    	if(audienciaDt.getAudienciaTipoCodigo().equals(String.valueOf(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()))) {
			    		listaAudienciasParaHoje = obPersistencia.consultarAudienciasSegundoGrauParaHojeAdvogado(usuarioDt, audienciaDt, posicaoPaginaAtual);
			    	} else {
			    		listaAudienciasParaHoje = obPersistencia.consultarAudienciasParaHojeAdvogado(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
			    	}
					break;
				}

			    case GrupoTipoDt.CONCILIADOR_VARA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU: {
					// Consultar audiências para hoje do usuário (conciliador ou juiz togado)
					listaAudienciasParaHoje = obPersistencia.consultarAudienciasParaHojeUsuario(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				}

				default: {
					// Consultar audiências para hoje do serventuário
					listaAudienciasParaHoje = obPersistencia.consultarAudienciasParaHoje(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);
					break;
				}
			}

			// DEFINIR A QUANTIDADE DE PÁGINAS
			QuantidadePaginas = (Long) listaAudienciasParaHoje.get(listaAudienciasParaHoje.size() - 1);
			// REMOVER DA LISTA DE AUDIÊNCIAS PARA HOJE CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE PÁGINAS
			listaAudienciasParaHoje.remove(listaAudienciasParaHoje.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasParaHoje;
	}

	/**
	 * Método responsável por abrir ou não a conexão com o banco de dado, dependendo do parâmetro de conexão informado, e consultar a quantidade das
	 * audiências para hoje de um dado cargo da serventia de um juiz ou de um conciliador e dos seguintes tipos: conciliação, instrução, preliminar e
	 * una.
	 * 
	 * Audiências Para Hoje = Audiências pendentes, ou seja, audiências cujo status é "A Ser Realizada", cujo id do processo is not null e cuja data
	 * de agendamento é a data corrente.
	 * 
	 * @author Keila Sousa Silva
	 * @since 21/08/2009
	 * @param id_ServentiaCargo
	 * @param fabricaConexao
	 * @return List listaTipoAudienciaQuantidadeAudienciasParaHoje = lista contendo um array de string que possuirá os seguintes valores, de acordo
	 *         com a ordem a seguir: "AudienciaTipoCodigo", "AudienciaTipo" e "Quantidade" (Quantidade de audiências pendentes para hoje)
	 * @throws Exception
	 */
	public List consultarQuantidadeAudienciasParaHoje(String id_ServentiaCargo, FabricaConexao fabricaConexao) throws Exception {
		List listaTipoAudienciaQuantidadeAudienciasParaHoje = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// INICIAR CONEXÃO COM O BANCO DE DADOS
			if (fabricaConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabricaConexao;
			}
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// CONSULTAR AUDIÊNCIAS PARA HOJE
			listaTipoAudienciaQuantidadeAudienciasParaHoje = obPersistencia.consultarQuantidadeAudienciasParaHoje(id_ServentiaCargo);
		} finally {
			// FECHAR CONEXÃO
			if (fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		// RETURN
		return listaTipoAudienciaQuantidadeAudienciasParaHoje;
	}

	/**
	 * Método responsável por abrir ou não a conexão com o banco de dado, dependendo do parâmetro de conexão informado, e consultar a quantidade das
	 * audiências para hoje de advogados e dos seguintes tipos: conciliação, instrução, preliminar e una.
	 * 
	 * Audiências Para Hoje Advogado = Audiências pendentes, ou seja, audiências cujo status é "A Ser Realizada", cujo id do processo is not null,
	 * cuja data de agendamento é a data corrente e cujo processo possui o advogado em questão como advogado de uma das partes deste processo.
	 * 
	 * @author Keila Sousa Silva
	 * @since 27/08/2009
	 * @param usuarioDt
	 * @param fabricaConexao
	 * @return List listaTipoAudienciaQuantidadeAudienciasParaHoje = lista contendo um array de string que possuirá os seguintes valores, de acordo
	 *         com a ordem a seguir: "AudienciaTipoCodigo", "AudienciaTipo" e "Quantidade" (Quantidade de audiências pendentes para hoje)
	 * @throws Exception
	 */
	public List consultarQuantidadeAudienciasParaHojeAdvogado(UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception {
		List listaTipoQuantidadeAudienciasParaHojeAdvogado = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// INICIAR CONEXÃO COM O BANCO DE DADOS
			if (fabricaConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabricaConexao;
			}
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// CONSULTAR AUDIÊNCIAS PARA HOJE
			listaTipoQuantidadeAudienciasParaHojeAdvogado = obPersistencia.consultarQuantidadeAudienciasParaHojeAdvogado(usuarioDt);
		} finally {
			// FECHAR CONEXÃO
			if (fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return listaTipoQuantidadeAudienciasParaHojeAdvogado;
	}

	/**
	 * Método responsável por abrir a conexão com o banco de dados e consultar audiências que foram movimentadas na data corrente. A consulta pode ter
	 * sido requisitada por serventuários, advogados, conciliadores ou juizes. Movimentar uma audiência significa alterar seu status de
	 * "1 = A Ser Realizada" para alguns dos sequintes status: "2 = Cancelada", "3 = Negativada", "4 = Realizada", "5 = Realizada com Conciliação", "6
	 * = Redesignada"
	 * 
	 * @author Keila Sousa Silva
	 * @author hrrosa
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @param ordenacao
	 * @param qtdRegistros
	 * @return List listaAudienciasMovimentadasHoje
	 * @throws Exception
	 */
	public List consultarAudienciasMovimentadasHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual) throws Exception {
		return consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, posicaoPaginaAtual, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public List consultarAudienciasMovimentadasHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
		List listaAudienciasMovimentadasHoje = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
				// PERFIL ADVOGADO
//				case GrupoDt.ADVOGADOS: {
			    case GrupoTipoDt.ADVOGADO: {
					// Consultar as audiências movimentadas hoje do advogado
					listaAudienciasMovimentadasHoje = obPersistencia.consultarAudienciasMovimentadasHojeAdvogado(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				}// FIM CASE ADVOGADO

					// PERFIL CONCILIADOR OU JUIZ TOGADO
//				case GrupoDt.CONCILIADORES_VARA:
//				case GrupoDt.JUIZES_VARA: {
			    case GrupoTipoDt.CONCILIADOR_VARA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU: {
					// Consultar as audiências movimentadas hoje do usuário(conciliador ou juiz togado)
					listaAudienciasMovimentadasHoje = obPersistencia.consultarAudienciasMovimentadasHojeUsuario(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				} // FIM CASE PERFIL CONCILIADOR OU JUIZ TOGADO

					// PERFIS SERVENTUÁRIOS DO JUIZADO
				default: {
					// Consultar as audiências movimentadas hoje do serventuário
					listaAudienciasMovimentadasHoje = obPersistencia.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);
					break;
				} // FIM CASE PERFIS SERVENTUÁRIOS DO JUIZADO
			}// FIM SWITCH

			// DEFINIR A QUANTIDADE DE PÁGINAS
			QuantidadePaginas = (Long) listaAudienciasMovimentadasHoje.get(listaAudienciasMovimentadasHoje.size() - 1);
			// REMOVER DA LISTA DE AUDIÊNCIAS PARA HOJE CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE PÁGINAS
			listaAudienciasMovimentadasHoje.remove(listaAudienciasMovimentadasHoje.size() - 1);
		
		} finally{
			// FECHAR CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasMovimentadasHoje;
	}

	/**
	 * Método responsável por abrir a conexão com o banco de dados e por consultar audiências de acordo com os parâmetros informados pelo usuário na
	 * consulta por filtros. A consulta pode ter sido requisitada por serventuários, advogados, conciliadores ou juizes.
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciasComFiltro
	 * @throws Exception
	 */
	public List consultarAudienciasFiltro(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual) throws Exception{
		return consultarAudienciasFiltro(usuarioDt, audienciaDt, posicaoPaginaAtual, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public List consultarAudienciasFiltro(UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception, MensagemException {

		List listaAudienciasComFiltro = new ArrayList();
		String numeroProcesso = null, digitoVerificador = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			String[] stTemp = audienciaDt.getAudienciaProcessoDt().getProcessoNumero().split("\\.");
			if (stTemp.length >= 1) numeroProcesso = stTemp[0];
			else numeroProcesso = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];
		
			String idServentiaAudiencia = usuarioDt.getId_Serventia();
			String idServentiaProcesso = null;
			if (Funcoes.StringToLong(audienciaDt.getId_Serventia()) > 0 && 
				Funcoes.StringToLong(audienciaDt.getId_Serventia()) != Funcoes.StringToLong(usuarioDt.getId_Serventia()) &&
				(Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
                 Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
                 Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
				idServentiaAudiencia = audienciaDt.getId_Serventia();
				idServentiaProcesso = usuarioDt.getId_Serventia();
            }
			
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
				case GrupoTipoDt.ADVOGADO:
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltroAdvogado(usuarioDt.getId_UsuarioServentia(), audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				
				case GrupoTipoDt.MP:
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltroPromotor(usuarioDt.getId_ServentiaCargo(), audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual);
					break;
					
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
				case GrupoTipoDt.ASSESSOR_MP:
					
					if (ValidacaoUtil.isNaoVazio(usuarioDt.getId_ServentiaCargoUsuarioChefe())){
						listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltroPromotor(usuarioDt.getId_ServentiaCargoUsuarioChefe(), audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual);						
					} else {
						listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltroAdvogado(usuarioDt.getId_UsuarioServentiaChefe(), audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);						
					}
					break;

				case GrupoTipoDt.CONCILIADOR_VARA:
					// Consultar as audiências do usuário(conciliador) de acordo com os parâmetros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltroUsuario(usuarioDt.getId_ServentiaCargo(), audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:					
					// Consultar as audiências do usuário(juiz togado) de acordo com os parâmetros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltro(idServentiaAudiencia, idServentiaProcesso, audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				default:
					// Consultar as audiências de acordo com os parâmetros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltro(idServentiaAudiencia, idServentiaProcesso, audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
			}

			QuantidadePaginas = (Long) listaAudienciasComFiltro.get(listaAudienciasComFiltro.size() - 1);
			listaAudienciasComFiltro.remove(listaAudienciasComFiltro.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		// Retornando a lista contendo as audiências consultadas de acordo com os parâmetros de consulta definidos pelo usuário
		return listaAudienciasComFiltro;
	}

	/**
	 * Método responsável por consultar Sessões de 2º grau de acordo com os parâmetros informados pelo usuário na consulta por filtros.
	 * 
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciasComFiltro
	 * 
	 * @author msapaula
	 */
	public List consultarSessoesFiltro(String id_Serventia, String grupoTipoCodigo, AudienciaDt audienciaDt, String statusAudiencia, String posicaoPaginaAtual) throws Exception {
		List listaAudienciasComFiltro = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//int grupo = Funcoes.StringToInt(grupoCodigo);
			int grupoTipo = Funcoes.StringToInt(grupoTipoCodigo);
			switch (grupoTipo) {
//			case GrupoDt.DESEMBARGADOR:
//			case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
					// Consultar as audiências de acordo com os parâmetros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarSessoesFiltroCamaras(id_Serventia, audienciaDt, statusAudiencia, posicaoPaginaAtual);
					break;

				default:
					listaAudienciasComFiltro = obPersistencia.consultarSessoesFiltro(id_Serventia, audienciaDt, statusAudiencia, posicaoPaginaAtual);
					break;
			}
			if (listaAudienciasComFiltro != null && listaAudienciasComFiltro.size() > 0) {
				QuantidadePaginas = (Long) listaAudienciasComFiltro.get(listaAudienciasComFiltro.size() - 1);
				listaAudienciasComFiltro.remove(listaAudienciasComFiltro.size() - 1);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasComFiltro;
	}
	
	/**
	 * Método responsável por consultar Sessões de 2º grau de acordo com os parâmetros informados pelo usuário na consulta por filtros e retorna JSON.
	 * 
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return String listaAudienciasComFiltro
	 * 
	 * @author gschiquini
	 */
	public String consultarSessoesFiltroJSON(String id_Serventia, String grupoTipoCodigo, AudienciaDt audienciaDt, String statusAudiencia, String posicaoPaginaAtual, String campoOrdenacao, String quantidadeRegistros, String tipoOrdenacao) throws Exception {
		String listaAudienciasComFiltro = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//int grupo = Funcoes.StringToInt(grupoCodigo);
			int grupoTipo = Funcoes.StringToInt(grupoTipoCodigo);
			switch (grupoTipo) {
//			case GrupoDt.DESEMBARGADOR:
//			case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
					// Consultar as audiências de acordo com os parâmetros informados no filtro
					//método inexistente
					listaAudienciasComFiltro = obPersistencia.consultarSessoesFiltroCamarasJSON(id_Serventia, audienciaDt, statusAudiencia, posicaoPaginaAtual, campoOrdenacao, quantidadeRegistros, tipoOrdenacao);
					break;

				default:
					listaAudienciasComFiltro = obPersistencia.consultarSessoesFiltroJSON(id_Serventia, audienciaDt, statusAudiencia, posicaoPaginaAtual, campoOrdenacao, quantidadeRegistros, tipoOrdenacao);
					break;
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasComFiltro;
	}

	/**
	 * Método responsável por validar os dados informados pelo usuário no cadastro de agendas de audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaAgendaDt
	 * @param geracaoAgendaAudiencia
	 * @return String retornoValidacao: mensagem contendo as inconsistências encontradas nos dados informados pelo usuário
	 * @throws Exception
	 */
	public String validarDadosGeracaoAgendasAudiencias(AudienciaAgendaDt audienciaAgendaDt) throws Exception{
		// MENSAGEM CONTENDO OS ERROS ENCONTRADOS NA VALIDAÇÃO DOS DADOS INFORMADOS PELO USUÁRIO
		retornoValidacao = "";
		logHorariosDuracao = "";

		// CARGO DA SERVENTIA
		if (audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) {
			retornoValidacao += "O campo CARGO DA SERVENTIA é obrigatório." + " \n";
		}

		// TIPO DE AUDIÊNCIA
		if (audienciaAgendaDt.getId_AudienciaTipo().equalsIgnoreCase("")) {
			retornoValidacao += "O campo TIPO DA AUDIÊNCIA é obrigatório.\n";
		}

		// QUANTIDADE DE AUDIÊNCIAS SIMULTÂNEAS
		validarQuantidadeAudienciasSimultaneas(audienciaAgendaDt);

		// DATAS INICIAL E FINAL
		if (validarDatasInicialFinal(audienciaAgendaDt)) {
			// HORÁRIOS INICIAL E FINAL / DURAÇÃO DAS AUDIÊNCIAS
			if (validarHorariosInicialFinalDuracao(audienciaAgendaDt) > 0) {
				// VERIFICAR COMPATIBILIDADADE DOS DIAS DA SEMANA DO PERÍODO INFORMADO (DATAS INICIAL E FINAL) COM OS DIAS DA SEMANA INFORMADOS
				validarCompatibilidadeDiasDaSemanaPeriodoDiasDaSemanaInformados(audienciaAgendaDt);
			}
		}
		
		if (retornoValidacao.length() == 0) {
			AudienciaTipoDt audienciaTipoDt = new AudienciaTipoNe().consultarId(audienciaAgendaDt.getId_AudienciaTipo());			
			if (audienciaTipoDt != null && 
			    (Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() || 
			     Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() || 
			     Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
				
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(audienciaAgendaDt.getId_Serventia());		
				
				if (serventiaDt != null && Funcoes.StringToInt(serventiaDt.getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL) {
					retornoValidacao += "O campo TIPO DA AUDIÊNCIA selecionado só é permitido para serventias do tipo Preprocessual (CEJUSC).\n";
				} else if (Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeAudienciasSimultaneas()) > 1) {
					ServentiaCargoDt serventiaCargoDt = new ServentiaCargoNe().consultarId(audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo());
					if (serventiaCargoDt != null && Funcoes.StringToInt(serventiaCargoDt.getCargoTipoCodigo()) != CargoTipoDt.JUIZ_1_GRAU) {
						retornoValidacao += "O campo QUANTIDADE só pode ser maior que 1 quando o TIPO DO CARGO DA SERVENTIA for igual a Juiz.\n";
					}
				}
			}
		}
		
		return retornoValidacao;
	}

	/**
	 * Método responsável por validar os dados informados pelo usuário no cadastro de audiências de segundo grau
	 * 
	 * @param audienciaSegundoGrauDt
	 * @return String retornoValidacao: mensagem contendo as inconsistências encontradas nos dados informados pelo usuário
	 * 
	 * @author msapaula
	 */
	public String validarDadosGeracaoAudienciaSegundoGrau(AudienciaSegundoGrauDt audienciaSegundoGrauDt){
		String retornoValidacao = "";

		if (audienciaSegundoGrauDt.getData().equalsIgnoreCase("")) retornoValidacao += "Data é campo obrigatório. \n";
		if (audienciaSegundoGrauDt.getHora().equalsIgnoreCase("")) retornoValidacao += "Hora é campo obrigatório. \n";

		return retornoValidacao;
	}

	/**
	 * Método responsável por validar a exclusão de uma Audiência: uma audiência só poderá ser excluída se não houver 
	 * nenhum processo com sessão pendente vinculado à essa.
	 * 
	 * @param id_Audiencia
	 * 
	 * @author msapaula
	 */
	public String validarExclusaoAudiencia(String id_Audiencia, UsuarioDt usuarioDt) throws Exception {
		String retornoValidacao = "";

		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		List listaProcessos = audienciaProcessoNe.consultarAudienciaProcessosPendentes(id_Audiencia, usuarioDt);
		if (listaProcessos != null && listaProcessos.size() > 0) retornoValidacao += "Sessão não pode ser excluída. Existem processos vinculados.";

		return retornoValidacao;
	}

	/**
	 * Método responsável por verificar a compatibilidade dos dias da semana referentes ao período (datas inicial e final) selecionado com os dias da
	 * semana [domingo (horários inicial e final e duração da audiência), segunda (horários inicial e final e duração da audiência), terça (horários
	 * inicial e final e duração da audiência), quarta (horários inicial e final e duração da audiência), quinta (horários inicial e final e duração
	 * da audiência), sexta (horários inicial e final e duração da audiência) e sábado (horários inicial e final e duração da audiência)] informados
	 * pelo usuário
	 * 
	 * @author Keila Sousa Silva
	 * @param dataInicial
	 * @param dataFinal
	 * @param horariosDuracaoInformados
	 * @return int Dias da semana incompatíveis com os dias da semana do período informado
	 * @throws Exception
	 */
	private void validarCompatibilidadeDiasDaSemanaPeriodoDiasDaSemanaInformados(AudienciaAgendaDt audienciaAgendaDt) throws Exception{
		/*
		 * Dias da semana informados: domingo = 0, segunda = 1, terça = 2, quarta = 3, quinta = 4, sexta = 5, sábado = 6
		 */
		List diasDaSemanaInformados = new ArrayList();
		List diasDaSemanaPeriodo = new ArrayList();
		List diasDaSemanaPeriodoFinal = new ArrayList();
		List diasDaSemanaIncompativeis = new ArrayList();

		// Construíndo um array somente com os dias da semana informados
		if (!audienciaAgendaDt.getHorariosDuracao()[0].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("0");
		}
		if (!audienciaAgendaDt.getHorariosDuracao()[3].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("3");
		}
		if (!audienciaAgendaDt.getHorariosDuracao()[6].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("6");
		}
		if (!audienciaAgendaDt.getHorariosDuracao()[9].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("9");
		}
		if (!audienciaAgendaDt.getHorariosDuracao()[12].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("12");
		}
		if (!audienciaAgendaDt.getHorariosDuracao()[15].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("15");
		}
		if (!audienciaAgendaDt.getHorariosDuracao()[18].equalsIgnoreCase("")) {
			diasDaSemanaInformados.add("18");
		}

		// Construíndo um array com os dias da semana do período informado
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Funcoes.StringToDate(audienciaAgendaDt.getDataInicial()));
		while (calendar.getTimeInMillis() <= Funcoes.StringToDate(audienciaAgendaDt.getDataFinal()).getTime()) {
			/*
			 * Para o 'Calendar' os dias da semana são: domingo = 1, segunda = 2, terça = 3, quarta = 4, quinta = 5, sexta = 6, sábado = 7. Para
			 * adequar os valores aos "index" dos dias da semana da jsp, será feita a subtração por 1
			 */
			diasDaSemanaPeriodo.add(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}

		// Limpando o array com os dias da semana do período informado, retirando os dias das semana repetidos
		if (diasDaSemanaPeriodo!=null && diasDaSemanaPeriodo.size() > 0) {
			diasDaSemanaPeriodoFinal.add(diasDaSemanaPeriodo.get(0));
			for (int i = 0; i < diasDaSemanaPeriodo.size(); i++) {
				boolean jaExiste = false;
				for (int j = 0; j < diasDaSemanaPeriodoFinal.size(); j++) {
					if (diasDaSemanaPeriodo.get(i) == diasDaSemanaPeriodoFinal.get(j)) {
						jaExiste = true;
						break;
					}
				}
				if (!jaExiste) {
					diasDaSemanaPeriodoFinal.add(diasDaSemanaPeriodo.get(i));
				}
			}

			/*
			 * Atualizando os dias da semana do período informado de acordo com o array dos dias da semana ({0,3,6,9,12,15,18})
			 */
			for (int i = 0; i < diasDaSemanaPeriodoFinal.size(); i++) {
				// SWITCH
				switch (Funcoes.StringToInt((String) diasDaSemanaPeriodoFinal.get(i))) {

					// SEGUNDA
					case 2: {
						diasDaSemanaPeriodoFinal.set(i, "0");
						break;
					}
						// TERÇA
					case 3: {
						diasDaSemanaPeriodoFinal.set(i, "3");
						break;
					}
						// QUARTA
					case 4: {
						diasDaSemanaPeriodoFinal.set(i, "6");
						break;
					}
						// QUINTA
					case 5: {
						diasDaSemanaPeriodoFinal.set(i, "9");
						break;
					}
						// SEXTA
					case 6: {
						diasDaSemanaPeriodoFinal.set(i, "12");
						break;
					}
						// SÁBADO
					case 7: {
						diasDaSemanaPeriodoFinal.set(i, "15");
						break;
					}
						// DOMINGO
					case 1: {
						diasDaSemanaPeriodoFinal.set(i, "18");
						break;
					}
				}// FIM SWITCH
			}
		}

		/*
		 * Comparando os dias da semana informados com os dias da semana do período informado de forma a verificar se existem dias da semana
		 * incompatíveis
		 */
		for (int i = 0; i < diasDaSemanaInformados.size(); i++) {
			boolean compativel = false;
			for (int j = 0; j < diasDaSemanaPeriodoFinal.size(); j++) {
				if (((String) diasDaSemanaInformados.get(i)).equalsIgnoreCase((String) diasDaSemanaPeriodoFinal.get(j))) {
					compativel = true;
					break;
				}
			}
			if (!compativel) {
				diasDaSemanaIncompativeis.add(diasDaSemanaInformados.get(i));
			}
		}

		// Mensagem caso haja algum dia da semana incompatível
		if (diasDaSemanaIncompativeis.size() > 0) {
			retornoValidacao += "Dia(s) da semana informado(s) incompatível(éis) com o período informado (datas inicial e final).\n";
		}
	}

	/**
	 * Método responsável por validar a quantidade de audiências simultâneas informada pelo usuário no cadastro de agendas para audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaAgendaDt
	 */
	private void validarQuantidadeAudienciasSimultaneas(AudienciaAgendaDt audienciaAgendaDt) {
		/*
		 * Validar se o campo da quantidade de audiências simultâneas foi preenchido
		 */
		if ((!audienciaAgendaDt.getQuantidadeAudienciasSimultaneas().trim().equals("")) && (!audienciaAgendaDt.getQuantidadeAudienciasSimultaneas().trim().equals("0"))) {
			/*
			 * Validar se a quantidade de audiências simultâneas informada é um número válido, ou seja, maior ou igual a 1
			 */
			if (!(Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeAudienciasSimultaneas()) > 0)) {
				retornoValidacao += "O campo QUANTIDADE é obrigatório.\n";
			}
			if (Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeAudienciasSimultaneas()) > Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas())) {
				retornoValidacao += "O campo QUANTIDADE não pode ser maior que " + audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas() + ".\n";
			}
			
		} else {
			retornoValidacao += "O valor a ser informado no campo QUANTIDADE deve ser igual ou superior a 1 e igual ou inferior a " + audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas() + ".\n";
		}
	}

	/**
	 * Método responsável por validar as dadas inicial e final informadas pelo usuário no cadastro de agendas para audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param dataInicial
	 * @param dataFinal
	 * @throws ParseException 
	 * @throws Exception
	 */
	private boolean validarDatasInicialFinal(AudienciaAgendaDt audienciaAgendaDt) throws ParseException{
		AudienciaAgendaValidacao audienciaAgendaValidacao = new AudienciaAgendaValidacao();
		boolean dataInicialValida = false;
		boolean dataFinalValida = false;

		// DATA INICIAL
		// Validar se o campo 'data inicial' foi informado
		if (audienciaAgendaValidacao.validarDataObrigatoria(audienciaAgendaDt.getDataInicial())) {
			/*
			 * Validar se o valor do campo 'data inicial' é igual ou superior à data corrente
			 */
			if (audienciaAgendaValidacao.validarDataIgualSuperiorCorrente(audienciaAgendaDt.getDataInicial())) {
				dataInicialValida = true;
			} else {
				dataInicialValida = false;
				retornoValidacao += "O valor do campo DATA INICIAL deve ser igual ou superior à data corrente.\n";
			}
		} else {
			dataInicialValida = false;
			retornoValidacao += "O campo DATA INICIAL é obrigatório.\n";
		}

		// DATA FINAL
		// Validar se o campo 'data final' foi informado
		if (audienciaAgendaValidacao.validarDataObrigatoria(audienciaAgendaDt.getDataFinal())) {
			/*
			 * Validar se o valor do campo 'data final' é igual ou superior à data corrente
			 */
			if (audienciaAgendaValidacao.validarDataIgualSuperiorCorrente(audienciaAgendaDt.getDataFinal())) {
				dataFinalValida = true;
			} else {
				dataInicialValida = false;
				retornoValidacao += "O valor do campo DATA FINAL deve ser igual ou superior à data corrente.\n";
			}
		} else {
			dataInicialValida = false;
			retornoValidacao += "O campo DATA FINAL é obrigatório.\n";
		}

		// DATAS INICIAL E FINAL
		// Se as datas inicial e final são válidas verifica-se se o período é consistente
		if (dataInicialValida & dataFinalValida) {
			/*
			 * Validar se as datas de um período informado são consistentes, ou seja, a data final é igual ou superior à data inicial
			 */
			String mensagemRetorno = audienciaAgendaValidacao.validarDatasPeriodo(audienciaAgendaDt);
			if (mensagemRetorno.equalsIgnoreCase("")) {
				dataFinalValida = true;
			} else {
				dataFinalValida = false;
				retornoValidacao += mensagemRetorno + "\n";
			}
		}

		return dataInicialValida & dataFinalValida;
	}

	/**
	 * Método responsável por validar os horários inicial e final e a duração das audiências informados pleo usuário no cadastro de agendas para
	 * audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaAgendaDt
	 * @return int horariosDuracaoInformadosValidos
	 * @throws Exception
	 */
	private int validarHorariosInicialFinalDuracao(AudienciaAgendaDt audienciaAgendaDt) throws Exception{
		logHorariosDuracao = "";

		/*
		 * Atributo que possuirá a quantidade de dias da semana onde os horários inicial e final e duração foram informados de forma válida
		 */
		int horariosDuracaoInformadosValidos = 0;

		/*
		 * Verificar se os horários inicial e final e a duração das audiências, dos dias da semana do período desejado, foram informados e são valores
		 * válidos. Os dados horários inicial e final e a duração das audiências serão validados apenas quando pelo menos um deles tenha sido
		 * informado
		 */

		// SEGUNGA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[0].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[1].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[2].equals("")))) {
			if (validarHorariosDuracao("1", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[0], audienciaAgendaDt.getHorariosDuracao()[1], audienciaAgendaDt.getHorariosDuracao()[2])) {
				logHorariosDuracao += "Segunda: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[0] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[1] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[2] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// TERÇA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[3].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[4].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[5].equals("")))) {
			if (validarHorariosDuracao("2", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[3], audienciaAgendaDt.getHorariosDuracao()[4], audienciaAgendaDt.getHorariosDuracao()[5])) {
				logHorariosDuracao += "Terça: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[3] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[4] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[5] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// QUARTA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[6].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[7].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[8].equals("")))) {
			if (validarHorariosDuracao("3", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[6], audienciaAgendaDt.getHorariosDuracao()[7], audienciaAgendaDt.getHorariosDuracao()[8])) {
				logHorariosDuracao += "Quarta: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[6] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[7] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[8] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// QUINTA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[9].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[10].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[11].equals("")))) {
			if (validarHorariosDuracao("4", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[9], audienciaAgendaDt.getHorariosDuracao()[10], audienciaAgendaDt.getHorariosDuracao()[11])) {
				logHorariosDuracao += "Quinta: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[9] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[10] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[11] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// SEXTA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[12].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[13].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[14].equals("")))) {
			if (validarHorariosDuracao("5", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[12], audienciaAgendaDt.getHorariosDuracao()[13], audienciaAgendaDt.getHorariosDuracao()[14])) {
				logHorariosDuracao += "Sexta: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[12] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[13] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[14] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// SÁBADO
		if ((!(audienciaAgendaDt.getHorariosDuracao()[15].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[16].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[17].equals("")))) {
			if (validarHorariosDuracao("6", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[15], audienciaAgendaDt.getHorariosDuracao()[16], audienciaAgendaDt.getHorariosDuracao()[17])) {
				logHorariosDuracao += "Sábado: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[15] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[16] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[17] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// DOMINGO
		/* Verificar se os campos (horários inicial e final e duração) foram preenchidos */
		if ((!(audienciaAgendaDt.getHorariosDuracao()[18].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[19].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[20].equals("")))) {
			// Verificar se os campos (horários inicial e final e duração) são valores válidos
			if (validarHorariosDuracao("7", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[18], audienciaAgendaDt.getHorariosDuracao()[19], audienciaAgendaDt.getHorariosDuracao()[20])) {
				// Preparar o log com os dados informados pelo usuário no cadastro de agendas para audiências
				logHorariosDuracao += "Domingo: Horário Inicial: " + audienciaAgendaDt.getHorariosDuracao()[18] + "; Horário Final: " + audienciaAgendaDt.getHorariosDuracao()[19] + "; Duração: " + audienciaAgendaDt.getHorariosDuracao()[20] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}

		// SET LOG HORÁRIOS/DURAÇÃO
		audienciaAgendaDt.setLogHorariosDuracao(logHorariosDuracao);

		// Mensagem caso nenhum dia da semana tenha sido informado
		if (horariosDuracaoInformadosValidos == 0) {
			retornoValidacao += "Informe os dias da semana (horários inicial e final e duração) de acordo com o período informado (datas inicial e final). ";
		}

		return horariosDuracaoInformadosValidos;
	}

	/**
	 * Método responsável por validar os horários inicial e final e a duração das audiências informados pelo usuário no cadastro de agendas de
	 * audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param diaDaSemana
	 * @param dataInicial
	 * @param horarioInicial
	 * @param horarioFinal
	 * @param duracaoAudiencia
	 * @return boolean horarioInicialValido & horarioFinalValido
	 * @throws Exception
	 */
	private boolean validarHorariosDuracao(String diaDaSemana, String dataInicial, String horarioInicial, String horarioFinal, String duracaoAudiencia) throws Exception{
		AudienciaAgendaValidacao validacaoHoraAgenda = new AudienciaAgendaValidacao();
		boolean horarioInicialValido = false;
		boolean horarioFinalValido = false;

		// HORÁRIO INICIAL
		// Verificar se o campo do horário inicial foi informado
		if (validacaoHoraAgenda.validarHorarioObrigatorio(horarioInicial)) {
			/*
			 * Verificar se o horário inicial informado possui o formato correto: hh:MM, ou seja, ele possui 4 dígitos
			 */
			if (validacaoHoraAgenda.validarFormatoHorario(horarioInicial)) {
				/*
				 * Verificar se o valor do horário inicial é consistente, ou seja, é igual ou superior ao horário corrente
				 */
				if (validacaoHoraAgenda.validarHorarioInicialConsistente(diaDaSemana, dataInicial, horarioInicial)) {
					horarioInicialValido = true;
				} else {
					horarioInicialValido = false;
					retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HORÁRIO INICIAL deve ser igual ou superior ao horário corrente.\n";
				}
			} else {
				horarioInicialValido = false;
				retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HORÁRIO INICIAL não possui o formato hh:mm.\n";
			}
		} else {
			horarioInicialValido = false;
			retornoValidacao += "O campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HORÁRIO INICIAL é obrigatório.\n";
		}

		// DURAÇÃO DAS AUDIÊNCIAS
		boolean duracaoAudienciaOk = false;
		// Verificar se a audiência foi informada e é consistente
		if (verificarDuracaoAudiencia(diaDaSemana, duracaoAudiencia, horarioInicial, horarioFinal)) {
			duracaoAudienciaOk = true;
		}

		// HORÁRIO FINAL
		// Verificar se o horário final foi informado
		if (validacaoHoraAgenda.validarHorarioObrigatorio(horarioFinal)) {
			/*
			 * Verificar se o horário final informado possui o formato correto:hh: MM, ou seja, ele possui 4 dígitos
			 */
			if (validacaoHoraAgenda.validarFormatoHorario(horarioFinal)) {
				if (duracaoAudienciaOk) {
					/*
					 * Verificar se o horário final é igual ou superior ao seguinte somatório: horário inicial + duração das audiências
					 */
					if (validacaoHoraAgenda.validarHorarioFinalConsistente(horarioInicial, horarioFinal, duracaoAudiencia)) {
						horarioFinalValido = true;
					} else {
						horarioFinalValido = false;
						retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HORÁRIO FINAL deve ser igual ou superior ao seguinte somatório: horário inicial + duração.\n";
					}
				}
			} else {
				horarioInicialValido = false;
				retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HORÁRIO FINAL não possui o formato hh:mm.\n";
			}
		} else {
			horarioInicialValido = false;
			retornoValidacao += "O campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HORÁRIO FINAL é obrigatório.\n";
		}
		return horarioInicialValido & horarioFinalValido;
	}

	/**
	 * Método responsável por validar a duração das audiências informada pelo usuário no cadastro de agendas de audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param diaDaSemana
	 * @param duracaoAudiencia
	 * @param horarioInicial
	 * @param horarioFinal
	 * @return boolean duracaoAudienciaValida
	 */
	private boolean verificarDuracaoAudiencia(String diaDaSemana, String duracaoAudiencia, String horarioInicial, String horarioFinal) {
		boolean duracaoAudienciaValida = false;
		AudienciaAgendaValidacao validacaoDuracaoAudiencia = new AudienciaAgendaValidacao();
		/*
		 * Verificar se a duração das audiências precisa ser verificada, ou seja, se pelo menos um dos horários, inicial ou final, tenha sido
		 * informado
		 */
		if ((!(horarioInicial.length() == 0)) && (!(horarioFinal.length() == 0))) {
			// Verificar se a duração da audiência foi informada corretamente
			if (validacaoDuracaoAudiencia.validarDuracaoConsistente(duracaoAudiencia)) {
				duracaoAudienciaValida = true;			
			} else {
				retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - DURAÇÃO deve ser igual ou superior a 15(quinze).\n";
			}
		}
		return duracaoAudienciaValida;
	}

	/**
	 * Método responsável por abrir uma conexão com o banco de dados para buscar as audiências livres de um dado tipo e de um cargo da serventia
	 * (serventia na qual o usuário corrente está logado)
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciasLivres
	 * @throws Exception
	 */
	public List consultarAudienciasLivres(String id_AudienciaTipo, String id_ServentiaCargo, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		List listaAudienciasLivres = null;
		FabricaConexao obFabricaConexao = null;

		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			// CONSULTAR AUDIÊNCIAS LIVRES DADO O CARGO DA SERVENTIA E O TIPO DA AUDIÊNCIA			
			listaAudienciasLivres = obPersistencia.consultarAudienciasLivres(id_AudienciaTipo, id_ServentiaCargo, id_Serventia, posicaoPaginaAtual);
			// DEFINIR A QUANTIDADE DE PÁGINAS
			QuantidadePaginas = (Long) listaAudienciasLivres.get(listaAudienciasLivres.size() - 1);
			// REMOVER DA LISTA DE AUDIÊNCIAS CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE PÁGINAS
			listaAudienciasLivres.remove(listaAudienciasLivres.size() - 1);
		} finally {
			// FECHER CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasLivres;
	}

	/**
	 * Método responsável por consultar os cargos de uma serventia, para o qual podem sem criadas agendas de audiências
	 * 
	 * @author Keila Sousa Silva
	 * @param nomeBusca
	 * @param posicaoPaginaAtual
	 * @return List listaServentiaCargos: lista de cargos da serventia do usuário
	 * @throws Exception
	 */
	public List consultarServentiaCargosAgendaAudiencia(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		List listaServentiaCargos = new ArrayList();
		listaServentiaCargos = serventiaCargoNe.consultarServentiaCargosAgendaAudiencia(nomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();
		serventiaCargoNe = null;
		return listaServentiaCargos;
	}

	/**
	 * Método responsável em consultar as audiências do tipo Sessão do 2º Grau que estão abertas, ou seja, aquelas onde a DataMovimentacao é nula.
	 * 
	 * @param id_Serventia, serventia do usuário que está consultando as sessões
	 * @param grupoCodigo, código do grupo do usuário logado
	 * @param ordem inversa de data
	 * @return List listaSessoes
	 * 
	 * @author msapaula
	 */
	public List consultarSessoesAbertas(String id_Serventia, String grupoTipoCodigo, boolean ordemDataInversa) throws Exception {
		List listaSessoesAbertas = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			int grupoTipo = Funcoes.StringToInt(grupoTipoCodigo);
			switch (grupoTipo) {
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					// Consultar sessões abertas nas serventias que tenham a serventia do usuário como relacionada
					listaSessoesAbertas = obPersistencia.consultarSessoesAbertasCamaras(id_Serventia, ordemDataInversa);
					break;

				default:
					// Consultar sessões abertas de acordo com serventia do usuário
					listaSessoesAbertas = obPersistencia.consultarSessoesAbertas(id_Serventia, ordemDataInversa);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesAbertas;
	}
	
	/**
	 * Método responsável em consultar as audiências, diferenciando as Virtuais, do tipo Sessão do 2º Grau que estão abertas, ou seja, aquelas onde a DataMovimentacao é nula.
	 * 
	 * @param id_Serventia, serventia do usuário que está consultando as sessões
	 * @param grupoCodigo, código do grupo do usuário logado
	 * @param ordem inversa de data
	 * @return List listaSessoes
	 * 
	 * @author aamoraes
	 */
	public List consultarSessoesVirtuaisAbertas(String id_Serventia, String grupoTipoCodigo, boolean ordemDataInversa, boolean naoTrazerIniciadas) throws Exception {
		List listaSessoesAbertas = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			int grupoTipo = Funcoes.StringToInt(grupoTipoCodigo);
			switch (grupoTipo) {
//				case GrupoDt.DESEMBARGADOR:
//				case GrupoDt.ASSISTENTES_GABINETE:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					// Consultar sessões abertas nas serventias que tenham a serventia do usuário como relacionada
					listaSessoesAbertas = obPersistencia.consultarSessoesAbertasCamaras(id_Serventia, ordemDataInversa);
					break;

				default:
					// Consultar sessões abertas de acordo com serventia do usuário
					listaSessoesAbertas = obPersistencia.consultarSessoesVirtuaisAbertas(id_Serventia, ordemDataInversa, naoTrazerIniciadas);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesAbertas;
	}

	/**
	 * Método responsável por gerar as datas de acordo com os dados fornecidos pelo usuário no cadastro de agendas de audiências. Essas datas serão
	 * utilizadas para gerar as agendas para audiências, ou seja, as audiências livres
	 * 
	 * @author Jesus Rodrigo
	 * @author Keila Sousa Silva
	 * @param dataInicial
	 * @param dataFinal
	 * @param quantidadeAudenciasSimultaneas
	 * @param horariosDuracao
	 * @return List listaDatas: lista com as datas geradas com os dados informados pelo usuário no cadastro de agendas para audiências
	 * @throws Exception
	 */
	private List gerarDatasAgendasAudiencias(String dataInicial, String dataFinal, String quantidadeAudenciasSimultaneas, String[] horariosDuracaoInformados) throws Exception{
		List listaDatas = new ArrayList();
		Date dataFim = Funcoes.StringToDate(dataFinal);
		/*
		 * 0 = Segunda, 3 = Terça, 6 = Quarta, 9 = Quinta, 12 = Sexta, 15 = Sábado, 18 = Domingo
		 */
		int[] posicao = {0, 3, 6, 9, 12, 15, 18 };

		Calendar calendarioGeral = Calendar.getInstance();
		Calendar calendarioInicial = Calendar.getInstance();
		Calendar calendarioFinal = Calendar.getInstance();
		Calendar calendarioLimiteMaximo = Calendar.getInstance();

		// Set datas inicial e final
		calendarioGeral.setTime(Funcoes.StringToDate(dataInicial));
		calendarioInicial.setTime(Funcoes.StringToDate(dataInicial));
		calendarioLimiteMaximo.setTime(Funcoes.StringToDate(dataInicial));
		calendarioFinal.setTime(Funcoes.StringToDate(dataFinal));

		int diaDaSemana = 0;

		do {
			/*
			 * Buscar no array "posição" o dia da semana a ser tratado (Segunda = 0, Terça = 3, Quarta = 6, Quinta = 9, Sexta = 12, Sábado = 15,
			 * Domingo = 18)
			 */
			if ((calendarioGeral.get(Calendar.DAY_OF_WEEK)) != 1) {
				diaDaSemana = posicao[(calendarioGeral.get(Calendar.DAY_OF_WEEK)) - 2];
			} else {
				diaDaSemana = posicao[(calendarioGeral.get(Calendar.DAY_OF_WEEK)) + 5];
			}

			// Verificar se os horários inicial e final e a duração do dia da semana em questão foram informados
			if (!horariosDuracaoInformados[diaDaSemana].equals("")) {
				calendarioInicial.setTime(calendarioGeral.getTime());
				calendarioLimiteMaximo.setTime(calendarioGeral.getTime());
				calendarioFinal.setTime(calendarioGeral.getTime());
				
				//setando 00:00h para evitar conflitos com horário de verão
				calendarioInicial.set(Calendar.HOUR_OF_DAY, 0);
				calendarioLimiteMaximo.set(Calendar.HOUR_OF_DAY, 0);
				calendarioFinal.set(Calendar.HOUR_OF_DAY, 0);

				// Set horários inicial e final e duração da audiência
				String horarioInicial = horariosDuracaoInformados[diaDaSemana];
				String horarioFinal = horariosDuracaoInformados[diaDaSemana + 1];
				int duracaoAudiencia = Funcoes.StringToInt(horariosDuracaoInformados[diaDaSemana + 2]);
				// Set horários inicial e final e duração da audiência
				calendarioInicial.add(Calendar.MINUTE, Funcoes.horaToMinuto((horarioInicial)));
				calendarioLimiteMaximo.add(Calendar.MINUTE, Funcoes.horaToMinuto((horarioInicial)));
				calendarioFinal.add(Calendar.MINUTE, Funcoes.horaToMinuto(horarioFinal));

				// Verificar se a data e horário da próxima agenda de audiência estouraram o limite definido na data final e no horário final
				while (calendarioLimiteMaximo.getTime().before(calendarioFinal.getTime())) {
					// Gerar agendas. Podem haver agendas simultâneas ou não
					for (int j = 0; j < Funcoes.StringToInt(quantidadeAudenciasSimultaneas); j++) {
						listaDatas.add(new Date(calendarioInicial.getTime().getTime()));
					}
					// Preparar o próximo horário de agenda de audiência
					calendarioInicial.add(Calendar.MINUTE, duracaoAudiencia);
					calendarioLimiteMaximo.add(Calendar.MINUTE, duracaoAudiencia);
				}
			}
			calendarioGeral.add(Calendar.DAY_OF_WEEK, 1);
		} while (calendarioGeral.getTimeInMillis() <= dataFim.getTime());

		return listaDatas;
	}

	/**
	 * Método responsável por reservar/desreservar a(s) audiência(s) livre(s) [agenda(s) livre(s)] selecionadas. Reservar uma agenda livre para
	 * audiência significa alterar seu campo 'Reservada' para '1(true)'. Desreservar uma agenda livre para audiência significa alterar seu campo
	 * 'Reservada' para '0(false)'
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciasAgendasReservar
	 * @param logDt
	 * @throws Exception 
	 */
	public void reservarAudienciaAgendaLivre(List audienciasAgendasReservar, LogDt logDt) throws Exception{
		AudienciaDt audienciaDtReservar = null;

		// Preparar para reservar ou desreservar a audiência livre
		for (int i = 0; i < audienciasAgendasReservar.size(); i++) {
			audienciaDtReservar = (AudienciaDt) audienciasAgendasReservar.get(i);
			if (audienciaDtReservar.getReservada().equalsIgnoreCase("false")) {
				audienciaDtReservar.setReservada("1");
			} else {
				audienciaDtReservar.setReservada("0");
			}

			// Log
			audienciaDtReservar.setId_UsuarioLog(logDt.getId_Usuario());
			audienciaDtReservar.setIpComputadorLog(logDt.getIpComputador());

			// Salvar = Alterar audiência = Reservar/Desreservar agenda livre
			salvar(audienciaDtReservar);
		}
	}

//	/**
//	 * Método responsável por preparar as agendas para audiências, ou seja, as audiências livres que serão criadas. Nesse método serão criadas
//	 * instâncias do objeto AudienciaDt, de acordo com a quantidade de datas geradas com os dados informados pelo usuário no cadastro de agendas para
//	 * audiências. Esses objetos serão preparados e retornados numa lista para poderem ser salvos
//	 * 
//	 * @author Keila Sousa Silva
//	 * @param listaDatas
//	 * @param audienciaDt
//	 * @return List listaAgendasAudienciasGeradas: lista das agendas para audiências geradas
//	 * @throws Exception
//	 */
//	private List buscarAgendas AudienciasGeradas(List listaDatas, AudienciaAgendaDt audienciaDt){
//		List listaAgendasAudienciasGeradas = new ArrayList();
//		Iterator iterator = listaDatas.iterator();
//		while (iterator.hasNext()) {
//			// AUDIÊNCIADT
//			AudienciaDt audienciaDtGerar = new AudienciaDt();
//			// Tipo da audiência
//			audienciaDtGerar.setId_AudienciaTipo(audienciaDt.getId_AudienciaTipo());
//			// Data da agenda (Data da audiência livre)
//			audienciaDtGerar.setDataAgendada(Funcoes.DataHora((Date) iterator.next()));
//			// Id da Serventia
//			audienciaDtGerar.setId_Serventia(audienciaDt.getId_Serventia());
//			// Log
//			audienciaDtGerar.setId_UsuarioLog(audienciaDt.getId_UsuarioLog());
//			audienciaDtGerar.setIpComputadorLog(audienciaDt.getIpComputadorLog());
//
//			// AUDIENCIAPROCESSODT
//			/*
//			 * No caso de geração manual de agendas de audiências, cada audiência possui apenas uma referência à tabela "AudienciaProcesso", ou seja,
//			 * a lista de objetos do tipo "AudienciaProcessoDt" contida no objeto do tipo "AudienciaDt" possuirá apenas um registro do tipo
//			 * "AudienicaProcessoDt". Isso se deve ao fato de que somente audiências do tipo "Sessão de 2º Grau" possuem várias referências à tabela
//			 * "AudienciaProcesso" e esse tipo de agenda de audiência será criada automaticamente.
//			 */
//			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
//			audienciaDtGerar.setListaAudienciaProcessoDt(audienciaProcessoNe.criarListaAudienciaProcessoDt(AudienciaProcessoStatusDt.LIVRE, audienciaDt));
//
//			// LISTA DE OBJETOS DO TIPO "AUDIENCIADT" A SER RETORNADA
//			listaAgendasAudienciasGeradas.add(audienciaDtGerar);
//		}
//		return listaAgendasAudienciasGeradas;
//	}

	/**
	 * Método responsável por excluir a(s) audiência(s) livre(s) [agenda(s) livre(s)] selecionadas
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciasAgendasExcluir
	 * @param logDt
	 * @throws Exception
	 */
	public void excluirAudienciaAgendaLivre(List audienciasAgendasExcluir, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			for (int i = 0; i < audienciasAgendasExcluir.size(); i++) {
				/*
				 * SET AUDIENCIADT A SER EXCLUÍDA
				 */
				AudienciaDt audienciaDtExcluir = (AudienciaDt) audienciasAgendasExcluir.get(i);
				audienciaDtExcluir.setId_UsuarioLog(logDt.getId_Usuario());
				audienciaDtExcluir.setIpComputadorLog(logDt.getIpComputador());

				/*
				 * EXCLUIR AUDIENCIAPROCESSODT
				 */
				AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
				audienciaProcessoNe.excluirAudienciaProcessoLivre(audienciaDtExcluir, obFabricaConexao);

				/*
				 * EXCLUIR AUDIENCIADT
				 */
				// Excluir audiência(s) livre(s) [agenda(s) livre(s)]
				obPersistencia.excluir(audienciaDtExcluir.getId());
				
			}
			
			// FINALIZAR TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSAÇÃO
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEXÃO
			obFabricaConexao.fecharConexao();
		}
	}	

	/**
	 * Consulta data da última audiência de um processo de um deteminado tipo de audiência
	 * 
	 * @author Ronneesley
	 * @param String
	 *            id_processo, id do processo
	 * @param int audienciaTipoCodigo, codigo do tipo de audiencia
	 * @return String
	 * @throws Exception
	 */
	public Date consultarDataUltimaAudiencia(String id_Processo) throws Exception {
		Date data = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			data = obPersistencia.consultarDataUltimaAudiencia(id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return data;
	}

	/**
	 * @param audienciaDt
	 * @return String
	 */
	public String Verificar(AudienciaDt audienciaDt) {
		return null;
	}

	/**
	 * Consulta os status disponíveis para audiência de segundo grau
	 * 
	 * @author Keila Sousa Silva
	 * @since 04/08/2009
	 * @param nomeBusca
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciaProcessoStatusDt: lista contendo objetos do tipo "AudienciaProcessoStatusDt" cada qual contendo a descrição do
	 *         status da audiência de processo
	 * @throws Exception
	 */
	public List consultarDescricaoAudienciaProcessoStatus(String nomeBusca, String serventiaTipoCodigo, String posicao) throws Exception {
		AudienciaProcessoStatusNe audienciaProcessoStatusNe = new AudienciaProcessoStatusNe();
		List listaAudienciaProcessoStatusDt = new ArrayList();
		listaAudienciaProcessoStatusDt = audienciaProcessoStatusNe.consultarAudienciaProcessoStatus(nomeBusca, serventiaTipoCodigo, posicao);
		QuantidadePaginas = audienciaProcessoStatusNe.getQuantidadePaginas();
		audienciaProcessoStatusNe = null;
		return listaAudienciaProcessoStatusDt;
	}
	
	public String consultarDescricaoAudienciaProcessoStatusJSON(String nomeBusca, String serventiaTipoCodigo, String posicao) throws Exception {
		String stTemp = "";
		
		AudienciaProcessoStatusNe audienciaProcessoStatusNe = new AudienciaProcessoStatusNe(); 
		stTemp = audienciaProcessoStatusNe.consultarAudienciaProcessoStatusJSON(nomeBusca, serventiaTipoCodigo, posicao);
		
		return stTemp;
	}

	/**
	 * Médodo responsável em agendar uma audiência do tipo Sessão do 2º grau para um processo passado. Chama método para vincular o processo a
	 * audiência e gera a movimentação de inclusão em pauta
	 * 
	 * @param processoDt
	 *            , processo a ser vinculado com a sessão
	 * @param id_Sessao
	 *            , audiência a ser vinculada com processo
	 * @param dataAgendada
	 *            , data da nova audiência a ser marcada
	 * @param id_ProcessoTipo
	 *            , id da classe (processoTipo) da nova audiência a ser marcada
	 * @param processoTipo
	 *            , processo tipo da nova audiência a ser marcada
	 * @param id_UsuarioServentia
	 *            , usuário que está marcando a sessão
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public AudienciaProcessoDt marcarSessao(ProcessoDt processoDt, String id_Sessao, String dataAgendada, String id_ProcessoTipo, String processoTipo, String id_UsuarioServentia, LogDt logDt, FabricaConexao conexao, UsuarioDt usuarioDt, String id_ServentiaCargoResponsavel) throws Exception {
		// Chama metodo para vincular o processo a audiencia
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		if (id_ProcessoTipo == null || id_ProcessoTipo.trim().length() == 0 || Funcoes.StringToLong(id_ProcessoTipo, 0) == 0) {
			if (processoDt.getId_Recurso() == null || processoDt.getId_Recurso().trim().length() == 0) {
				id_ProcessoTipo = processoDt.getId_ProcessoTipo();
				processoTipo = processoDt.getProcessoTipo();	
			} else if (processoDt.getRecursoDt() != null) {
				id_ProcessoTipo = processoDt.getRecursoDt().getId_ProcessoTipo();
				processoTipo = processoDt.getRecursoDt().getProcessoTipo();
			}
		}
		return audienciaProcessoNe.marcarSessaoProcesso(id_Sessao, dataAgendada, id_ProcessoTipo, processoTipo, processoDt, id_UsuarioServentia, logDt, conexao, false, usuarioDt, id_ServentiaCargoResponsavel);
	}

	/**
	 * Médodo responsável em remarcar uma audiência do tipo Sessão do 2º grau para um processo passado. 
	 * Ao remarcar uma sessão, o status da sessão é modificado para "Remarcada" e uma nova sessão é criada. 
	 * Ao final gera as movimentações "Retirado de pauta" e "Incluído em Pauta" novamente
	 * 
	 * @param processoDt, processo a ser vinculado com a sessão
	 * @param id_NovaAudiencia, nova audiência a ser vinculada com processo
	 * @param novaData, nova data da audiência a ser marcada
	 * @param id_ProcessoTipo
	 *            , id da classe (processoTipo) da nova audiência a ser marcada
	 * @param processoTipo
	 *            , processo tipo da nova audiência a ser marcada
	 * @param usuarioDt, usuário que está marcando a sessão
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * @author msapaula
	 */
	public void remarcarSessao(ProcessoDt processoDt, String id_NovaAudiencia, String id_ProcessoTipo, String processoTipo, String novaData, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao, String id_ServentiaCargoResponsavel) throws MensagemException,Exception {
		
		// Resgata a sessão marcada para o processo e redesigna
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoNe.desmarcarSessaoProcesso(processoDt, id_ProcessoTipo, AudienciaProcessoStatusDt.REMARCADA, usuarioDt, logDt, conexao);

		// Marca nova audiência
		this.marcarSessao(processoDt, id_NovaAudiencia, novaData, id_ProcessoTipo, processoTipo, usuarioDt.getId_UsuarioServentia(), logDt, conexao, usuarioDt, id_ServentiaCargoResponsavel);

	}

	/**
	 * Médodo responsável em desmarcar uma audiência do tipo Sessão do 2º grau para um processo passado. 
	 * Modifica o status da audiência para "Cancelada" e gera movimentação "Retirado de Pauta"
	 * 
	 * @param processoDt, processo a ser vinculado com a sessão
	 * @param id_ProcessoTipo, identificação do processo tipo
	 * @param usuarioDt, usuário que está desmarcando a sessão
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * @author msapaula
	 */
	public void desmarcarSessao(ProcessoDt processoDt, String id_ProcessoTipo, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
		
		// Resgata a sessão marcada para o processo e desmarca
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoNe.desmarcarSessaoProcesso(processoDt, id_ProcessoTipo, AudienciaProcessoStatusDt.DESMARCAR_PAUTA, usuarioDt, logDt, conexao);

	}
	
	/**
	 * Médodo responsável em retirar uma audiência do tipo Sessão do 2º grau para um processo passado. 
	 * Modifica o status da audiência para "Cancelada" e gera movimentação "Desmarcar Sessão"
	 * 
	 * @param processoDt, processo a ser vinculado com a sessão
	 * @param id_ProcessoTipo, identificação do processo tipo
	 * @param usuarioDt, usuário que está desmarcando a sessão
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * @author msapaula
	 */
	public void retirarSessao(ProcessoDt processoDt, String id_ProcessoTipo, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
		
		// Resgata a sessão marcada para o processo e desmarca
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoNe.retirarSessaoProcesso(processoDt, id_ProcessoTipo, AudienciaProcessoStatusDt.RETIRAR_PAUTA, usuarioDt, logDt, conexao);

	}

	/**
	 * Salva uma nova audiência do tipo Sessão 2º Grau, conforme data e hora passados
	 * 
	 * @param audienciaSegundoGrauDt
	 *            , objeto com dados da sessão a ser criada
	 * @author msapaula
	 * @throws Exception 
	 */
	public void agendarAudienciaSegundoGrau(AudienciaSegundoGrauDt audienciaSegundoGrauDt) throws Exception{
		// Concatena data e hora da audiência
		audienciaSegundoGrauDt.setDataAgendada(audienciaSegundoGrauDt.getData() + " " + audienciaSegundoGrauDt.getHora());
		audienciaSegundoGrauDt.setAudienciaTipoCodigo(String.valueOf(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()));
		audienciaSegundoGrauDt.setReservada("false");
		this.salvar(audienciaSegundoGrauDt);
	}

	/**
	 * Método responsável por estabelecer uma conexão com o banco de dados, consultar e retornar uma instância completa do objeto do tipo
	 * "AudiênciaDt", de acordo com o parâmetro passado que indica o id da audiência desejada. Como a instância será completa, estára vinculado a este
	 * objeto do tipo "AudiênciaDt" a lista contendo objeto(s) do tipo "AudiênciaProcessoDt". Esse método será utilizado para consultar dados
	 * completos de uma audiência livre, não retornando dados como processo e partes.
	 * 
	 * @author Keila Sousa Silva
	 * @since 19/08/2009
	 * @param id_Audiencia
	 * @return AudienciaDt audienciaDtCompleta
	 * @throws Exception
	 */
	public AudienciaDt consultarAudienciaLivreCompleta(String id_Audiencia) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = null;

		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaLivreCompleta(id_Audiencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}

	/**
	 * Método responsável em consultar e retornar uma instância completa do objeto do tipo "AudiênciaDt", de acordo com o parâmetro passado que indica
	 * o id da audiência desejada. Como a instância será completa, estára vinculado a este objeto uma lista contendo objeto(s) do tipo
	 * "AudiênciaProcessoDt", onde cada processo terá suas partes também vinculadas.
	 * 
	 * @param id_Audiencia
	 * @return AudienciaDt audienciaDtCompleta
	 * @author msapaula
	 */
	public AudienciaDt consultarAudienciaCompleta(String id_Audiencia, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
		
			audienciaDtCompleta = obPersistencia.consultarAudienciaCompleta(id_Audiencia);
		
			//TODO: Sem necessidade, pois no ps do método anterior já seta essas duas informações através do método associarAudienciaDt, chamado pelo método getListaAudienciaAgendadaCompletaSessaoVirtual 
			//mmgomes
			/*if(audienciaDtCompleta != null) {
				audienciaDtCompleta.setVirtual(obPersistencia.isVirtual(id_Audiencia));
				audienciaDtCompleta.setSessaoIniciada(obPersistencia.isVirtualIniciada(id_Audiencia));
			}*/
		
		return audienciaDtCompleta;
	}

	public AudienciaDt consultarAudienciaCompletaSessaoVirtual(String id_Audiencia, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
		
			audienciaDtCompleta = obPersistencia.consultarAudienciaCompleta(id_Audiencia);
		
			//TODO: Sem necessidade, pois no ps do método anterior já seta essas duas informações através do método associarAudienciaDt, chamado pelo método getListaAudienciaAgendadaCompletaSessaoVirtual 
			//mmgomes
			/*if(audienciaDtCompleta != null) {
				audienciaDtCompleta.setVirtual(obPersistencia.isVirtual(id_Audiencia));
				audienciaDtCompleta.setSessaoIniciada(obPersistencia.isVirtualIniciada(id_Audiencia));
			}*/
		
		return audienciaDtCompleta;
	}

	// jvosantos - 24/09/2019 11:40 - Extrair método para usar uma única conexão
	public AudienciaDt consultarAudienciaCompleta(String id_Audiencia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarAudienciaCompleta(id_Audiencia, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método que verifica se os dados obrigatórios em uma movimentação de audiência foram preenchidos. Na movimentação de audiência (finalização de
	 * Sessão de 2º grau é obrigatório inserir um arquivo)
	 * 
	 * @param dados
	 *            , objeto com dados da movimentação a ser verificada
	 * @author msapaula
	 */
	public String verificarMovimentacaoAudiencia(AudienciaMovimentacaoDt dados){
		String stRetorno = "";
		if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) stRetorno += "É necessário inserir um arquivo. \n";

		return stRetorno;
	}

	/**
	 * Método que verifica se os dados obrigatórios em uma movimentação de audiência foram preenchidos
	 * 
	 * @param dados
	 *            , objeto com dados da movimentação a ser verificada
	 * @author msapaula
	 * @throws Exception 
	 */
	public String verificarMovimentacaoAudienciaProcesso(AudienciaMovimentacaoDt dados, UsuarioDt usuarioDt) throws Exception{
		String stRetorno = "";
		if (dados.getAudienciaStatusCodigo() != null && dados.getAudienciaStatusCodigo().length() > 0 && dados.getAudienciaStatusCodigo().equals("-1")) {
			stRetorno += "Selecione o Status da Audiência. \n";
		}
		// Se for uma Sessão de 2º grau que está sendo remarcada, deve ser selecionada a data da nova sessão
		else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) == AudienciaProcessoStatusDt.REMARCADA && Funcoes.StringToInt(dados.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && dados.getId_NovaSessao().length() == 0) {
			stRetorno += "Selecione a Data da nova Sessão a ser marcada. \n";
		} else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA  &&  dados.getAudienciaDt().devePossuirIndicadorDeAcordo()) {
			if (!(dados.isHouveAcordo() || dados.isNaoHouveAcordo())){
				stRetorno += "Selecione a opção Houve Acordo? \n";	
			} else if (dados.isHouveAcordo() && dados.getValorAcordo().length() == 0) {
				stRetorno += "Valor do acordo deve ser maior que zero. \n";				
			}
		}	
		int grupoTipoUsuarioLogado = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		if (dados.isPreAnalise()){
			
			if (dados.getListaArquivos() != null && 
				dados.getListaArquivos().size() > 0) {
				stRetorno = "Não é possível salvar uma pré-análise com arquivos assinados. \n";
			} else {
				if (dados.getId_ArquivoTipo() == null || dados.getId_ArquivoTipo().trim().length() == 0 || dados.getArquivoTipo() == null || dados.getArquivoTipo().trim().length() == 0 ){
					stRetorno += "Selecione o Tipo do Arquivo: Acórdão, Relatório e Voto. Favor reabrir a funcionalidade para recarregar os tipos.\n";
				}				
				/*if(dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())){
					stRetorno += "É necessário redigir o texto da pré-análise: Acórdão, Relatório e Voto. \n";
				}*/
				
				
				if( (grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA || usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL)){
					
					String ementa = "class=\"ementa\"";
					
					
					if(dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())){
						stRetorno += "É necessário redigir o texto da pré-análise. \n";
					}else{
						if( !dados.getTextoEditor().contains(ementa)){
							stRetorno += "Para redigir pré-análise é necessário definir a ementa \n";
						}
					}
					

				} else{

					if (dados.getId_ArquivoTipoEmenta() == null || dados.getId_ArquivoTipoEmenta().trim().length() == 0 || dados.getArquivoTipoEmenta() == null || dados.getArquivoTipoEmenta().trim().length() == 0 ){
						stRetorno += "Selecione o Tipo do Arquivo: Ementa. \n";
					}				
					if(dados.getTextoEditorEmenta() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditorEmenta())){
						stRetorno += "É necessário redigir o texto da pré-análise: Ementa. \n";
					}
					
				}
			}

		} else {
				if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) {
					stRetorno += "É necessário inserir um arquivo. \n";
				} else 
					if( (grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA || usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL ) &&  !dados.isRetiradaDePauta() ){						
						ArquivoDt arquivo = (ArquivoDt)dados.getListaArquivos().get(0);
						String conteudo = new String(arquivo.getConteudo());
						
						String ementa = "class=\"ementa\"";
						
						if(!conteudo.contains(ementa)){
							stRetorno += "Para concluir audiência é necessário definir a ementa. \n";
						}
					}
					
					if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU && dados.getListaArquivos()  != null && dados.getListaArquivos().size() > 1){
					if(!(usuarioDt.getServentiaSubtipoCodigo() != null &&
							(Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL)))
						stRetorno = "É necessário inserir apenas um arquivo correspondente à Ata. \n";			
				} else if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
					int quantidadeEmenta = 0;
					ArquivoDt arquivoDt = null;
					if (dados != null && dados.getListaArquivos() != null) {
						for (int i=0; i < dados.getListaArquivos().size(); i++){
							arquivoDt = (ArquivoDt) dados.getListaArquivos().get(i);
							if (arquivoDt != null){
								if (arquivoDt.getArquivoTipoCodigo() != null && arquivoDt.getArquivoTipoCodigo().equalsIgnoreCase(String.valueOf(ArquivoTipoDt.EMENTA))) {
									quantidadeEmenta += 1;
								} 								
								else if (arquivoDt.getNomeArquivo().trim().toUpperCase().startsWith(nomeArquivoEmenta.toUpperCase()))
								{
									quantidadeEmenta += 1;
								}
															else if (arquivoDt.getNomeArquivo().trim().length() >= prefixoArquivoEmenta.length() &&
										 arquivoDt.getNomeArquivo().trim().substring(0, prefixoArquivoEmenta.length()).equalsIgnoreCase(prefixoArquivoEmenta))
								{
									quantidadeEmenta += 1;
								}	
							}
						}
					}							
					if (quantidadeEmenta == 0) stRetorno = "É necessário inserir um arquivo assinado correspondente à Ementa. \n";
					else if (quantidadeEmenta > 1) stRetorno = "É permitido inserir apenas um arquivo correspondente à Ementa. \n";					
				}
		}		
		
		return stRetorno;
	}

	/**
	 * Consulta os dados de uma AudiênciaProcesso necessário para movimentação, devendo retornar dados da Audiencia, da AudienciaProcesso, Processo e
	 * suas partes
	 * 
	 * @param id_AudienciaProcesso
	 *            , identificação da audiência que está sendo movimentada
	 * @return AudienciaDt audienciaCompleta
	 * @author msapaula
	 */
	public AudienciaDt consultarAudienciaProcessoCompleta(String id_AudienciaProcesso) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaProcessoCompleta(id_AudienciaProcesso);
			if (audienciaDtCompleta == null) throw new MensagemException("Não foi possível localizar a audiência do processo com o id igual a " + id_AudienciaProcesso + ".");
			audienciaDtCompleta.setVirtual(obPersistencia.isVirtual(audienciaDtCompleta.getId()));
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}

	/**
	 * Verifica se uma movimentação pode ser efetuada, baseada nas pendências selecionadas para serem geradas Ex.: Autos Conclusos - deve verificar se
	 * processo já está concluso
	 * 
	 * @param audienciaMovimentacaoDt
	 *            , objeto com dados da movimentação
	 * @param grupoCodigo
	 *            , grupo do usuário que está movimentando a audiência
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public String podeMovimentarAudiencia(AudienciaMovimentacaoDt audienciaMovimentacaoDt, String grupoCodigo, String idServentiaUsuarioLogado) throws Exception{
		String stRetorno = "";
		
		AudienciaProcessoDt audienciaProcessoDt = audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt();
		
		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(audienciaMovimentacaoDt.getAudienciaTipoCodigo());	
		
		if (tipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC || tipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT || tipoCodigo == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC) {			
			int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
			codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());			
			if (codigoStatus != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && codigoStatus != AudienciaProcessoStatusDt.RETIRAR_PAUTA) {
				AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
				if (audienciaDt != null && Funcoes.StringToLong(audienciaDt.getId_Serventia()) != Funcoes.StringToLong(idServentiaUsuarioLogado)){
					stRetorno = "A movimentação só é permitida na serventia CEJUSC da audiência. Na serventia do processo só é permitida a opção Desmarcada / Retirada de Pauta.";
				}
			}
		}
		
		if (stRetorno.length() == 0) {
			List pendenciasGerar = audienciaMovimentacaoDt.getListaPendenciasGerar();
			// Para cada pendência marcada para ser gerada verifica se há alguma restrição
			if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
				PendenciaNe pendenciaNe = new PendenciaNe();
				ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();
				stRetorno = pendenciaNe.verificaPendenciasProcesso(processoDt, pendenciasGerar, grupoCodigo, false);
			}	
		}
				
		return stRetorno;
	}

	/**
	 * Salva movimentação de audiência. Para Audiências de 1º grau refere-se a movimentação de "Audiencia" e "AudienciaProcesso", já no caso de
	 * sessões de 2º grau essa movimentação refere-se somente a um processo da sessão. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimentação correspondente e pendências de acordo com o que foi selecionado pelo usuário
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt, usuário que está realizando a movimentação
	 * 
	 * @author msapaula
	 */
	
	public void salvarMovimentacaoAudienciaProcesso(AudienciaMovimentacaoDt audienciaMovimentacaoDt, List listaProcessos, UsuarioDt usuarioDt) throws MensagemException, Exception {
		salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, listaProcessos, usuarioDt, null);
	}
	
	public void salvarMovimentacaoAudienciaProcesso(AudienciaMovimentacaoDt audienciaMovimentacaoDt, List listaProcessos, UsuarioDt usuarioDt, FabricaConexao fabrica) throws MensagemException, Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		List arquivos = null;
		List movimentacoes = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			if (fabrica == null) {
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                obFabricaConexao.iniciarTransacao();
            } else
                obFabricaConexao = fabrica;
			
			if(listaProcessos != null && listaProcessos.size() > 0) {
				
				LogDt logDt = new LogDt(usuarioDt.getId(), audienciaMovimentacaoDt.getIpComputadorLog());
				AudienciaDt audienciaDt = null;
				
				arquivos = audienciaMovimentacaoDt.getListaArquivos();
				// Salva arquivos inseridos
				if (arquivos != null && arquivos.size() > 0) {
					movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
				}

				for (int i = 0; i < listaProcessos.size(); i++) {
					ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(((ProcessoDt)listaProcessos.get(i)).getId());
					
					AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
					AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarAudienciaProcessoPendente(processoDt.getId_Processo(), usuarioDt);
					
					AudienciaNe audienciaNe = new AudienciaNe();
					audienciaDt = audienciaNe.consultarAudienciaProcessoCompleta(audienciaProcessoDt.getId()); //audienciaMovimentacaoDt.getAudienciaDt();
					
					// Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
					audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);

					// Chama método para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimentação correspondente a movimentação da audiência
					// e inserir responsáveis pela audiência
					MovimentacaoDt movimentacaoDt = movimentarAudiencia(audienciaDt, audienciaMovimentacaoDt.getAudienciaStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
					movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
					movimentacoes.add(movimentacaoDt);
					String visibilidade=null;
					if (processoDt.isSegredoJustica()){
						visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
					}
					// Salvando vínculo entre movimentação e arquivos inseridos					
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);					

					// Salvando pendências da movimentação
					if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) {
						pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
					}

				}
				
//				// Gera recibo para arquivos de movimentações
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);

				// Dependendo do tipo da Audiência chama método para gerar Movimentação correspondente
				int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
				codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());
				if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal()) {
					if (codigoStatus!=AudienciaProcessoStatusDt.DESMARCAR_PAUTA && codigoStatus!=AudienciaProcessoStatusDt.RETIRAR_PAUTA && codigoStatus != AudienciaProcessoStatusDt.REMARCADA){
						pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);						
					}
				}
				
			} else {
				AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
				
				// Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
				ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
				audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
				
				//Consulta Voto 2º Grau ou Elaboracao de Voto
				PendenciaDt pendenciaDt = null;
				if (audienciaMovimentacaoDt.getPendenciaArquivoDt() != null)
					pendenciaDt = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia());
						

				LogDt logDt = new LogDt(usuarioDt.getId(), audienciaMovimentacaoDt.getIpComputadorLog());
				
				arquivos = audienciaMovimentacaoDt.getListaArquivos();
				// Salva arquivos inseridos
				if (arquivos != null && arquivos.size() > 0) {
					movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
				}

				// Chama método para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimentação correspondente a movimentação da audiência
				// e inserir responsáveis pela audiência
				MovimentacaoDt movimentacaoDt = movimentarAudiencia(audienciaDt, audienciaMovimentacaoDt.getAudienciaStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
				movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
				movimentacoes.add(movimentacaoDt);
				
				//Fechar pendencia do Tipo Voto 2º Grau ou Elaboracao de Voto
				if (pendenciaDt != null){
					pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
			        pendenciaDt.setDataFim(df.format(new Date()));
					pendenciaNe.fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
				}
				
				boolean elaboracaoVoto = pendenciaNe.verificarExistenciaElaboracaoVoto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());

				if (elaboracaoVoto == true){
					pendenciaDt = this.obtemElaboracaoDeVotoSessao(processoDt);
					pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
					pendenciaNe.fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
				}
				
				

				String visibilidade=null;
				if (processoDt.isSegredoJustica()){
					visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
				}
				// Salvando vínculo entre movimentação e arquivos inseridos
				movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade,logDt, obFabricaConexao);

				// Salvando pendências da movimentação
				if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) {
					pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
				}

				// Atualiza Classificador processo
				if (audienciaMovimentacaoDt.getId_Classificador().length() > 0) {
					new ProcessoNe().alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
				}

//				// Gera recibo para arquivos de movimentações
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
				
				//--segundo grau gerar publicação das decisões
			    int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
		        codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());

		        // Dependendo do tipo da Audiência chama método para gerar Movimentação correspondente
		        if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal()) {
		        	if (codigoStatus!=AudienciaProcessoStatusDt.DESMARCAR_PAUTA && codigoStatus!=AudienciaProcessoStatusDt.RETIRAR_PAUTA && codigoStatus !=AudienciaProcessoStatusDt.REMARCADA){
		        		if (processoDt != null){
		        			if (!processoDt.isSigiloso()) {
			        			pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
			        		}
		        		}		        		
		        	}
		        } 
			}
			

			if (fabrica == null) obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			if (fabrica == null) obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			if (fabrica == null)
				obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva movimentação de audiência, ou melhor, a finalização de Sessões de 2º Grau, pois estas podem ser finalizadas ou movimentadas separadamente
	 * de cada processo a ser julgado na sessão.
	 * 
	 * @param audienciaMovimentacaoDt
	 *            , objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt
	 *            , usuário que está realizando a movimentação
	 * 
	 * @author msapaula
	 */
	public void salvarMovimentacaoAudienciaFinalizacaoSessaoSegundoGrau(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt) throws Exception {
		List arquivos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			PendenciaNe pendenciaNe = new PendenciaNe();

			LogDt logDt = new LogDt(usuarioDt.getId(), audienciaMovimentacaoDt.getIpComputadorLog());

			AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
			arquivos = audienciaMovimentacaoDt.getListaArquivos();
			
			//lrcampos 06/11/2019 16:40 - Caso a sessão for virtual finalizar pendencias de Conhecimento
			if(audienciaDt.isVirtual()) {
				pendenciaNe.finalizarPendenciasParaConhecimentoSessaoVirtual(audienciaDt, usuarioDt, obFabricaConexao);
			}
			//Alteração devido a unificação de metódos de gerar publicao do Jesus do dia 25/02/2019***
			Iterator itArquivos = arquivos.iterator();
  			ArquivoNe arquivoNe = new ArquivoNe();
  			//salvo todos os arquivos que serão publicados
  			while (itArquivos.hasNext()) {
   				// Pega o arquivo da lista
   				ArquivoDt arquivo = (ArquivoDt) itArquivos.next();

   				// Inseri o novo arquivo
   				arquivoNe.inserir(arquivo, logDt, obFabricaConexao);
  			}
			//**********************************************************************
  			
			// Cria publicação dos arquivos da Sessão
			pendenciaNe.salvarPublicacao(new PendenciaDt(),arquivos, usuarioDt, logDt, obFabricaConexao);
			
			String id_ArquivoFinalizacao = ""; 
			if(arquivos !=null && arquivos.size() > 0){
				ArquivoDt arquivo = (ArquivoDt)arquivos.get(0);
				if (arquivo != null && arquivo.getId() != null && arquivo.getId().trim().length() > 0) id_ArquivoFinalizacao = arquivo.getId();
			}
			
			// Atualiza dados em "Audiencia"
			String valorAtual = audienciaDt.getPropriedades();
			// Seta da movimentação com a data atual
			audienciaDt.setDataMovimentacao(Funcoes.DataHora(new Date()));
			// Seta o id do arquivo de finalização da sessão de segundo grau
			if (id_ArquivoFinalizacao.trim().length() > 0){
				audienciaDt.setId_ArquivoFinalizacaoSessao(id_ArquivoFinalizacao);
			}
			// Captura novos dados para log
			String valorNovo = audienciaDt.getPropriedades();

			LogDt obLogDt = new LogDt("Audiencia", audienciaDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.MovimentacaoAudiencia), valorAtual, valorNovo);
			obPersistencia.alterarAudienciaMovimentacao(audienciaDt);
			obLog.salvar(obLogDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarMovimentacaoAudiencia(audienciaMovimentacaoDt);
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável em efetuar ações comuns em uma movimentação de audiência, valendo também para sessões de 2º grau. Chama método para
	 * Atualizar Dados em "Audiencia", "AudienciaProcesso", gera movimentação correspondente a movimentação da audiência e insere responsáveis pela
	 * audiência
	 * 
	 * @param audienciaDt
	 * @param audienciaStatusCodigo
	 * @param usuarioDt
	 * @param logDt
	 * @param conexao
	 * @return
	 * @throws Exception
	 */
	public MovimentacaoDt movimentarAudiencia(AudienciaDt audienciaDt, String audienciaStatusCodigo, String id_NovaAudiencia, String novaData, UsuarioDt usuarioDt, List arquivos, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
		MovimentacaoDt movimentacaoAudiencia = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();

		// Atualiza dados em "Audiencia"
		String dataMovimentacao = Funcoes.DataHora(new Date());
		this.alterarAudienciaMovimentacao(audienciaDt, dataMovimentacao, logDt, conexao);

		// Atualiza dados em "AudienciaProcesso"
		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		audienciaProcessoNe.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt, dataMovimentacao, audienciaStatusCodigo, logDt, conexao);

		// Insere responsáveis pela audiência, ou seja, quem realizou
		AudienciaProcessoResponsavelNe audienciaProcessoResponsavelNe = new AudienciaProcessoResponsavelNe();
		
		// jvosantos - 25/07/2019 17:42 - Se a sessão for virtual, usa o relator da AUDI_PROC
		if(audienciaDt.isVirtual()) {
			ServentiaCargoDt relator = (new ServentiaCargoNe()).consultarId(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), conexao);
			audienciaProcessoResponsavelNe.inserirAudienciaProcessoResponsavel(audienciaDt.getAudienciaProcessoDt().getId(), String.valueOf(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU), relator.getId_UsuarioServentia(), logDt, conexao);
		} else {
			audienciaProcessoResponsavelNe.salvarAudienciaProcessoResponsavel(audienciaProcessoDt.getId(), audienciaDt.getAudienciaTipoCodigo(), audienciaProcessoDt.getProcessoDt(), usuarioDt, logDt, conexao);
		}

		int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
		codigoStatus = Funcoes.StringToInt(audienciaStatusCodigo);

		// Dependendo do tipo da Audiência chama método para gerar Movimentação correspondente -- confirmar com JESUS
		if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && !usuarioDt.isUPJTurmaRecursal()) {
			movimentacaoAudiencia = this.movimentarSessao(audienciaDt, codigoStatus, id_NovaAudiencia, novaData, usuarioDt, false, false, logDt, conexao);
		} else {
			movimentacaoAudiencia = this.movimentarAudienciaPrimeiroGrau(audienciaDt, codigoStatus, usuarioDt, logDt, conexao);
			
			if (movimentacaoAudiencia != null && audienciaProcessoDt.getProcessoDt() != null &&
			    (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.ordinal() ||
				 Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.ordinal() ||
			     Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.ordinal()) &&
				Funcoes.StringToLong(audienciaDt.getId_Serventia()) != Funcoes.StringToLong(audienciaProcessoDt.getProcessoDt().getId_Serventia())) {
				String prioridadeProcesso = new ProcessoNe().getPrioridade(audienciaProcessoDt.getProcessoDt().getId(), conexao);
				
				// Testa se é uma situação de encaminhamento de audiências para o Cejusc por meio de pendência. Se for, gera uma pendência "retornar" diferente.
				AudienciaProcessoDt audiProcDt = audienciaDt.getAudienciaProcessoDt();
				if(isAudienciaCejuscProcessoExterno(audiProcDt.getProcessoDt(), usuarioDt)) {
					new PendenciaNe().gerarPendenciaVerificarHomologacaoAudienciaCEJUSC(audienciaProcessoDt.getProcessoDt().getId(), UsuarioServentiaDt.SistemaProjudi, audienciaProcessoDt.getProcessoDt().getId_Serventia(), movimentacaoAudiencia.getId(), arquivos, null, logDt, conexao,prioridadeProcesso);
				} else {
					new PendenciaNe().gerarPendenciaVerificarProcesso(audienciaProcessoDt.getProcessoDt().getId(), UsuarioServentiaDt.SistemaProjudi, audienciaProcessoDt.getProcessoDt().getId_Serventia(), movimentacaoAudiencia.getId(), arquivos, null, logDt, conexao,prioridadeProcesso);
				}
			}
		}

		return movimentacaoAudiencia;
	}
	
	
	public boolean isAudienciaCejuscProcessoExterno(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt) throws Exception {
		if(audienciaMovimentacaoDt != null && audienciaMovimentacaoDt.getAudienciaDt() != null && audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null) {
			return isAudienciaCejuscProcessoExterno(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt(), usuarioDt);
		}
		else {
			return false;
		}
	}
	
	public boolean isAudienciaCejuscProcessoExterno(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		boolean retorno = false;
		
		if(processoDt != null && processoDt.getId_Serventia() != null && usuarioDt != null && usuarioDt.getId_Serventia() != null && usuarioDt.getServentiaSubtipoCodigo() != null) {
			if(processoDt.getId_Serventia() != usuarioDt.getId_Serventia() && Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.PREPROCESSUAL ){
				retorno = true;
			}
		}
		
		return retorno;
	}

	/**
	 * Método responsável em gerar movimentação em um processo de acordo com o Status da Audiência de 1º Grau. Ainda de acordo com o status, a
	 * movimentação poderá ser gerada para o juiz para efeito de estatística.
	 * 
	 * @param audienciaDt
	 *            , audiência que está sendo movimentada
	 * @param codigoStatus
	 *            , status selecionado para audiência
	 * @param usuarioDt
	 *            , usuário que está movimentando audiência
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private MovimentacaoDt movimentarAudienciaPrimeiroGrau(AudienciaDt audienciaDt, int codigoStatus, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		MovimentacaoDt movimentacaoAudiencia = null;
		String id_JuizResponsavelProcesso = null;

		ProcessoDt processoDt = audienciaDt.getAudienciaProcessoDt().getProcessoDt();
		String complemento = audienciaDt.getDataAgendada();
		
		boolean conciliacao = false;
		
		//se audiencia de conciliação conta para quem movimentou, exceto quando tem sentença
		if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.ordinal() ||
				Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.ordinal() ||
				Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.ordinal() || 
				Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO.ordinal() ||
				Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.PRELIMINAR_CONCILIADOR.ordinal()) {
			
				conciliacao = true;
			
			
				//int resp = Funcoes.StringToInt((new ServentiaCargoNe()).consultarId(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), conexao).getCargoTipoCodigo());
				//if (!(resp == CargoTipoDt.JUIZ_1_GRAU || resp == CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL ||
				//	resp == CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL || resp == CargoTipoDt.JUIZ_UPJ	)) {
				//	conciliacao = true;
				//}
				
		}
			
		//+ " - Local: " + audienciaDt.getServentia() + " - Dirigida por: " + audienciaDt.getAudienciaProcessoDt().getServentiaCargo();
//		String complemento = "(" + audienciaDt.getAudienciaTipo() + " - " + audienciaDt.getDataAgendada() + ")";
		if (!audienciaDt.getAcessoOutraServentia().equals("") && Funcoes.StringToInt(audienciaDt.getAcessoOutraServentia()) == PendenciaTipoDt.CARTA_PRECATORIA) {
			complemento += " - Carta Precatória";
		}
		// De acordo com o status da audiência realiza tratamentos
		switch (codigoStatus) {
			case AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO:
				complemento = "Realizada sem Acordo - " + complemento;
				
				if (conciliacao) {
					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				} else {			
					id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				}
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento,  MovimentacaoTipoDt.AUDIENCIA_REALIZADA_SEM_ACORDO, logDt, conexao);
				//Setando descrição da movimentação para gerar complemento corretamente
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada sem Acordo");
				
				break;
				
			case AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO:				
				complemento = "Realizada com Acordo - " + complemento;
				
				if (conciliacao) {
						movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
					} else {
						id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
						movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
					}
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento,  MovimentacaoTipoDt.AUDIENCIA_REALIZADA_COM_ACORDO, logDt, conexao);
				//Setando descrição da movimentação para gerar complemento corretamente
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada com Acordo");
				break;
				
			case AudienciaProcessoStatusDt.DESMARCAR_PAUTA:
			case AudienciaProcessoStatusDt.RETIRAR_PAUTA:
				complemento = "Desmarcada - " + complemento;
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaDesmarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
				// Setando descrição da movimentação para gerar complemento corretamente
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Desmarcada");
				break;

			case AudienciaProcessoStatusDt.REMARCADA:
				complemento = "Remarcada - " + complemento;
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRemarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Remarcada");
				break;

			case AudienciaProcessoStatusDt.NEGATIVADA:
				complemento = "Não Realizada - " + complemento;
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaNegativa(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Negativa");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO:
				complemento = "Realizada com Sentença com Mérito - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaComMerito(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada com Sentença com Mérito");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO:
				complemento = "Realizada com Sentença sem Mérito - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaSemMerito(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada com Sentença sem Mérito");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO:
				complemento = "Realizada com Homologação - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaHomologacao(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada com Sentença de Homologação");
				break;
				
			case AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA:
				complemento = "Realizada com Sentença - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaHomologacao(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada com Sentença de Homologação");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA:
				complemento = "Realizada sem Sentença - " + complemento;
//				if ((usuarioDt.getServentiaSubtipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL))
//						|| usuarioDt.getServentiaSubtipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL)))
//						&& processoDt.getAreaCodigo().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))){
					/*ROAD 186830 - Determinando que seja contabilizado na Estatística do Magistrado as "Audiências realizadas sem sentença",
					 *  mesmo que a movimentação seja realizada por servidores, nos moldes do que já ocorre com as  "audiências realizadas com sentença" 
					 */
				
					if (conciliacao) {
						movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
					} else {
						id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
						movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
					}
						//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSemSentenca(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
					//movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada sem Sentença");
//				} else {
//					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSemSentenca(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
//					movimentacaoAudiencia.setMovimentacaoTipo("Audiência Realizada sem Sentença");
//				}
				break;
		}

		return movimentacaoAudiencia;

	}

	/**
	 * Método responsável em gerar movimentação em um processo de acordo com o Status da Sessão que está sendo julgada. Ainda de acordo com o status,
	 * a movimentação poderá ser gerada para o Relator do processo para efeito de estatística.
	 * 
	 * @param audienciaDt
	 *            , sessão que está sendo movimentada
	 * @param codigoStatus
	 *            , status selecionado para sessão
	 * @param id_NovaSessao
	 *            , id da nova sessão a ser marcada
	 * @param novaDataSessao
	 *            , nova data da sessão a ser marcada
	 * @param usuarioDt
	 *            , usuário que está movimentando a sessão
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private MovimentacaoDt movimentarSessao(AudienciaDt audienciaDt, int codigoStatus, String id_NovaSessao, String novaDataSessao, UsuarioDt usuarioDt, boolean EhInsercaoExtratoAta, boolean houveAlteracaoExtratoAta, LogDt logDt, FabricaConexao conexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

		MovimentacaoDt movimentacaoAudiencia = null;
		String id_ServentiaCargoResponsavelSessao = null;
		
		ProcessoDt processoDt = audienciaDt.getAudienciaProcessoDt().getProcessoDt();		

		// De acordo com o status da audiência realiza tratamentos
		switch (codigoStatus) {
			case AudienciaProcessoStatusDt.RETIRAR_PAUTA:
			case AudienciaProcessoStatusDt.REMARCADA:
				if (audienciaDt.isVirtual()) {
					AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();	
					ProcessoParteNe processoParteNe = new ProcessoParteNe();
					String tipoAudi = processoParteNe.consultaClasseProcessoIdAudiProc(audienciaDt.getAudienciaProcessoDt().getId(), conexao);
					
					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaSessaoRetirada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), audienciaProcessoNe.getDescricaoMovimentacaoSessaoVirtual("Sessão do dia", audienciaDt.getDataAgendada(), tipoAudi), logDt, conexao);
				} else {
					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaSessaoRetirada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), "(Sessão do dia " + audienciaDt.getDataAgendada() + ")", logDt, conexao);
				}				
				
				// Setando descrição da movimentação para gerar complemento corretamente
				movimentacaoAudiencia.setMovimentacaoTipo("RETIRADO DE PAUTA");
				break;
			case AudienciaProcessoStatusDt.DESMARCAR_PAUTA:			
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaSessaoDesmarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), "(Sessão do dia " + audienciaDt.getDataAgendada() + ")", logDt, conexao);
				// Setando descrição da movimentação para gerar complemento corretamente
				movimentacaoAudiencia.setMovimentacaoTipo("DESMARCADO DE PAUTA");
				break;
			default: 												
				// Consulta o relator responsável pelo processo, pois ele será responsável pela movimentação da audiência
				if(audienciaDt.isVirtual() && !EhInsercaoExtratoAta) { // jvosantos - 25/07/2019 16:18 - Correção para pegar o ID_Serv_cargo do relator da sessão
					id_ServentiaCargoResponsavelSessao = (new ServentiaCargoNe()).consultarId(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), conexao).getId_UsuarioServentia();
				} else { 
					id_ServentiaCargoResponsavelSessao = usuarioDt.getId_UsuarioServentia();
				}
				
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoSessao(processoDt.getId(), id_ServentiaCargoResponsavelSessao, "(Sessão do dia " + audienciaDt.getDataAgendada() + ")",houveAlteracaoExtratoAta, logDt, codigoStatus , conexao);									
				
		}

		return movimentacaoAudiencia;

	}

	/**
	 * Método responsável em capturar o usuário que será responsável pela movimentação de uma Audiência. Isso deve ser feito pois se um Analista
	 * movimenta uma audiência, a movimentação deve contar nas estatísticas para o juiz do processo ou o relator.
	 * 
	 * @param id_Processo, identificação do processo
	 * @param audienciaTipoCodigo, tipo da audiência que está sendo movimentada
	 * @param usuarioDt, usuário que está movimentando
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private String getResponsavelMovimentacaoAudiencia(ProcessoDt processoDt, String audienciaTipoCodigo, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		String id_UsuarioServentia = null;

		if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal()) {
			
			// Se usuário que está movimentando uma sessão não é o juiz, a movimentação deve ser para ele
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.JUIZES_TURMA_RECURSAL) {
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.JUIZ_TURMA && Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
				
				//Captura a serventia do processo
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());

				if ( ServentiaSubtipoDt.isSegundoGrau(serventiaDt.getServentiaSubtipoCodigo()) ||  ServentiaSubtipoDt.isUPJTurmaRecursal(serventiaDt.getServentiaSubtipoCodigo())) {
					// Quando se tratar de sessão de 2º em Câmaras, captura o relator na tabela ProcessoResponsavel com CargoTipo Relator
					id_UsuarioServentia = responsavelNe.getUsuarioRelatorResponsavelCargoTipo(processoDt.getId(), conexao);
				} else {
					// Consulta o relator responsável pelo processo, pois ele será responsável pela movimentação da audiência
					id_UsuarioServentia = responsavelNe.getUsuarioRelatorResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), conexao);
				}
				if (id_UsuarioServentia == null) throw new MensagemException("Cargo responsável pelo processo está vazio. Não é possível movimentar Sessão.");
			} else id_UsuarioServentia = usuarioDt.getId_UsuarioServentia();
		} else {

			// Se usuário que está movimentando uma Audiência de 1º Grau não é o juiz, a movimentação deve ser para ele
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.JUIZES_VARA) {
			  if (!usuarioDt.isMagistrado()) {
				// Consulta o juiz responsável pelo processo, pois ele será responsável pela movimentação da audiência
				  if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.ordinal() ||
					  Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.ordinal() ||
					  Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.ordinal()) {
					  
					  ServentiaDt serventia = new ServentiaNe().consultarId(processoDt.getId_Serventia(), conexao);					  
					  if (serventia.isSegundoGrau()) {
						  String id_ServentiaCargo = responsavelNe.consultarRelatorProcesso(processoDt.getId(), conexao);						  
						  if (id_ServentiaCargo != null) {
							  ServentiaCargoDt serventiaCargoDt = new ServentiaCargoNe().consultarId(id_ServentiaCargo, conexao);
							  if (serventiaCargoDt != null)
								  id_UsuarioServentia = serventiaCargoDt.getId_UsuarioServentia();
						  }					  
					  } else {
						  id_UsuarioServentia = responsavelNe.getUsuarioJuizResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia(), conexao);  
					  }
					  
				  } else {
					  id_UsuarioServentia = responsavelNe.getUsuarioJuizResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), conexao);	  
				  }				
				if (id_UsuarioServentia == null){
					throw new MensagemException("Cargo responsável pelo processo está vazio. Não é possível movimentar Audiência.");
				}
			} else{
				id_UsuarioServentia = usuarioDt.getId_UsuarioServentia();
			}
		}
		return id_UsuarioServentia;
	}

	/**
	 * Atualiza dados em "Audiencia" em virtude da movimentação. Quando uma audiência é movimentada deverá armazenar na tabela "Audiencia" a data da
	 * movimentação, quando não for sessão de 2º grau, pois essa será finalizada em um momento posterior.
	 * 
	 * @param audienciaDt
	 *            , objeto com dados da audiencia
	 * @param logDt
	 *            , dados do log
	 * @param fabricaConexao
	 *            , conexão ativa
	 * @throws Exception 
	 */
	private void alterarAudienciaMovimentacao(AudienciaDt audienciaDt, String dataMovimentacao, LogDt logDt, FabricaConexao fabricaConexao) throws Exception{

		// Se não for sessão de 2º grau seta data da movimentação
		if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) != AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()) {
			// Guarda dados do objeto antes da alteração
			String valorAtual = audienciaDt.getPropriedades();

			// Seta da movimentação
			audienciaDt.setDataMovimentacao(dataMovimentacao);

			// Captura novos dados para log
			String valorNovo = audienciaDt.getPropriedades();

			LogDt obLogDt = new LogDt("Audiencia", audienciaDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.MovimentacaoAudiencia), valorAtual, valorNovo);
			AudienciaPs obPersistencia = new AudienciaPs(fabricaConexao.getConexao()); 
			obPersistencia.alterarAudienciaMovimentacao(audienciaDt);

			obLog.salvar(obLogDt, fabricaConexao);
		}
	}

	/**
	 * Consulta os dados de uma audiência e seus processos vinculados que ainda não foram movimentados
	 * 
	 * @param stId, identificação da audiência
	 * 
	 * @author msapaula
	 */
	public AudienciaDt consultarAudienciaProcessosPendentes(String id_Audiencia, UsuarioDt usuarioDt) throws Exception {
		AudienciaDt audienciaDt = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		audienciaDt = this.consultarId(id_Audiencia);
		audienciaDt.setListaAudienciaProcessoDt(audienciaProcessoNe.consultarAudienciaProcessosPendentes(id_Audiencia, usuarioDt));			
				
		audienciaProcessoNe = null;
		return audienciaDt;
	}

	/**
	 * Consulta todos os processos vinculados a uma audiência, independente do status.
	 * 
	 * @param stId, identificação da audiência
	 * 
	 * @author msapaula
	 */
	public AudienciaDt consultarAudienciaProcessos(String id_Audiencia, UsuarioDt usuarioDt, boolean isSegundoGrau) throws Exception {
		AudienciaDt audienciaDt = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		audienciaDt = this.consultarId(id_Audiencia);
		audienciaDt.setListaAudienciaProcessoDt(audienciaProcessoNe.consultarAudienciaProcessos(id_Audiencia, usuarioDt, isSegundoGrau));			
		
		audienciaProcessoNe = null;
		return audienciaDt;
	}

	/**
	 * Método que verifica se uma sessão de 2º grau pode ser finalizada, devendo verificar se existe algum processo vinculado que ainda não foi
	 * julgado
	 * 
	 * @param id_Audiencia
	 */
	public String podeFinalizarSessao(String id_Audiencia, UsuarioDt usuarioDt) throws Exception {
		String stMensagem = "";

		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		List audienciaProcessos = audienciaProcessoNe.consultarAudienciaProcessosPendentes(id_Audiencia, usuarioDt);
		if (audienciaProcessos != null && audienciaProcessos.size() > 0) {
			stMensagem += "Sessão não pode ser finalizada. Existem processos não julgados nesta Sessão.";
		}
		return stMensagem;
	}

	/**
	 * Método responsável em excluir ou desativar uma sessão de segundo grau.
	 * Se sessão possuir algum processo vinculado, mesmo que não esteja pendente deve apenas desativar a sessão.
	 * Caso não possua nenhum processo, deve excluir.
	 * 
	 * @param audienciaDt, objeto com dados da audiência
	 * @author msapaula
	 */
	public void excluirSessaoSegundoGrau(AudienciaDt audienciaDt, UsuarioDt usuarioDt) throws Exception {
		LogDt obLogDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			List listaProcessos = new AudienciaProcessoNe().consultarAudienciaProcessos(audienciaDt.getId(), usuarioDt, true);
			if (listaProcessos != null && listaProcessos.size() > 0) {
				//Desativa sessão
				String valorAtual = "[Id_Audiencia:" + audienciaDt.getId() + ";CodigoTemp:" + String.valueOf(AudienciaDt.ABERTA) + "]";
				String valorNovo = "[Id_Audiencia:" + audienciaDt.getId() + ";CodigoTemp:" + String.valueOf(AudienciaDt.DESATIVADA) + "]";

				obLogDt = new LogDt("Audiencia", audienciaDt.getId(), audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Invalidacao), valorAtual, valorNovo);
				obPersistencia.desativarAudiencia(audienciaDt.getId());
			} else {
				//Exclui sessão
				obLogDt = new LogDt("Audiencia", audienciaDt.getId(), audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), audienciaDt.getPropriedades(), "");
				obPersistencia.excluir(audienciaDt.getId());
				audienciaDt.limpar();
			}
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Chama método para consultar Responsáveis por uma AudienciaProcesso
	 * 
	 * @param id_AudienciaProcesso
	 * @return
	 * @throws Exception
	 */
	public List consultarResponsaveisAudienciaProcesso(String id_AudienciaProcesso) throws Exception{
		List tempList = null;
		AudienciaProcessoResponsavelNe neObjeto = new AudienciaProcessoResponsavelNe();
		
		tempList = neObjeto.consultarResponsaveisAudienciaProcesso(id_AudienciaProcesso);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Método responsável em cancelar a movimentação de audiência(s). Apaga os id's que tenham sido setados para os objetos
	 */
	private void cancelarMovimentacaoAudiencia(AudienciaMovimentacaoDt audienciaMovimentacaoDt){
		// Limpa id's dos objetos
		List arquivos = audienciaMovimentacaoDt.getListaArquivos();
		if (arquivos != null) {
			for (int i = 0; i < arquivos.size(); i++) {
				ArquivoDt dt = (ArquivoDt) arquivos.get(i);
				dt.setId("");
			}
		}
	}

	/**
	 * Realiza chamada a Objeto que efetuará a consulta
	 * 
	 * @param grupoCodigo
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 */
	public List consultarGrupoArquivoTipo(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		tempList = neObjeto.consultarGrupoArquivoTipo(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada a Objeto que efetuará a consulta
	 * 
	 * @param usuarioDt
	 * @param id_ArquivoTipo
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 */
	public List consultarModelo(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		List tempList = null;
		ModeloNe neObjeto = new ModeloNe();
		
		tempList = neObjeto.consultarModelos(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
				
		neObjeto = null;
		return tempList;
	}
	
	public String consultarModeloJSON(UsuarioDt usuarioDt, String id_ArquivoTipo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		
		ModeloNe neObjeto = new ModeloNe(); 
		stTemp = neObjeto.consultarModelosJSON(tempNomeBusca, posicaoPaginaAtual, id_ArquivoTipo, usuarioDt);
		
		return stTemp;
	}

	/**
	 * Realiza chamada a Objeto que efetuará a consulta
	 * 
	 * @param grupoCodigo
	 * @return
	 * @throws Exception
	 */
	public List consultarTiposPendenciaMovimentacao(UsuarioDt usuarioDt) throws Exception {
		List tempList = null;
		PendenciaTipoNe pendenciaTipoNe = new PendenciaTipoNe();
		
		tempList = pendenciaTipoNe.consultarGrupoPendenciaTipo("", usuarioDt, "0", false);		
		
		pendenciaTipoNe = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public ModeloDt consultarModeloId(String id_Modelo, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		ModeloDt modeloDt = null;
		ModeloNe modeloNe = new ModeloNe();
		
		modeloDt = modeloNe.consultarId(id_Modelo);
		modeloDt.setTexto(modeloNe.montaConteudo(id_Modelo, processoDt, usuarioDt, ""));
				
		modeloNe = null;
		return modeloDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarStatusAudiencia(String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		AudienciaProcessoStatusNe audienciaProcessoStatusNe = new AudienciaProcessoStatusNe();
		
		tempList = audienciaProcessoStatusNe.consultarAudienciaProcessoStatusMovimentacao(serventiaSubtipoCodigo);
				
		audienciaProcessoStatusNe = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarServentiaCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		tempList = serventiaCargoNe.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();

		serventiaCargoNe = null;
		return tempList;
	}
	
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarServentiaCargosJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		ServentiaCargone = null;
		return stTemp;
	}

	/**
	 * Método responsável por criar o relatório de listagem das Audiências no formato PDF.
	 * @param stCaminho
	 * @param usuarioDt
	 * @param audienciaDtConsulta
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] relListagemAudiencias(String stCaminho, UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, Integer fluxo) throws Exception {
		byte[] temp = null;
		ByteArrayOutputStream baos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			Map parametros = new HashMap();
			parametros.put("caminhoLogo", stCaminho + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("data", new Date());

			if (audienciaDtConsulta.getAudienciaTipoCodigo() != null) {
				if (!audienciaDtConsulta.getAudienciaTipoCodigo().equals("")) {
					parametros.put("tipoAudiencia", AudienciaTipoDt.Codigo.getDescricao(audienciaDtConsulta.getAudienciaTipoCodigo()));
				} else {
					parametros.put("tipoAudiencia", "TODAS");
				}
			}

			List liAudiencias = null;
			String pathJasper = null;
			switch (fluxo) {
				case 1:
					parametros.put("titulo", "Agenda de Audiências");
					liAudiencias = obPersistencia.relListagemAudienciasParaHoje(usuarioDt, audienciaDtConsulta);

					pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "listagemAudiencias.jasper";
					break;
				case 2:
					parametros.put("titulo", "Audiências Pendentes");
					liAudiencias = obPersistencia.relListagemAudienciasPendentes(usuarioDt, audienciaDtConsulta);

					pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "listagemAudienciasPendentesMovidas.jasper";
					break;
				case 3:
					parametros.put("titulo", "Audiências Movimentadas Hoje");
					liAudiencias = obPersistencia.relListagemAudienciasMovimentadasHoje(usuarioDt, audienciaDtConsulta);

					pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "listagemAudienciasPendentesMovidas.jasper";
					break;
			}

			InterfaceJasper ei = new InterfaceJasper(liAudiencias);

			JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);

			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			temp = baos.toByteArray();
			baos.close();

		} catch(Exception e) {
			try{if (baos!=null) baos.close(); } catch(Exception k){}
		} finally{
			obFabricaConexao.fecharConexao();
			baos = null;
		}

		return temp;
	}

	public ProcessoDt consultarProcessoIdCompleto(String id_Processo) throws Exception {
		ProcessoDt processodt = null;
		ProcessoNe processoNe = new ProcessoNe();
		
		processodt = processoNe.consultarIdCompleto(id_Processo);
				
		processoNe = null;
		return processodt;
	}

	public List consultarClassificador(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		List tempList = null;
		ClassificadorNe neClassificador = new ClassificadorNe();
		
		tempList = neClassificador.consultarClassificadorServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = neClassificador.getQuantidadePaginas();
				
		neClassificador = null;
		return tempList;

	}

	/**
	 * Método responsável por consultar a pendência do tipo voto em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param String idProcessoTipo
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 */
	public PendenciaArquivoDt consultarVotoDesembargador(String id_ServentiaCargo, AudienciaProcessoDt audienciaProcessoDt, String idProcessoTipo) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		
		if (idProcessoTipo != null && idProcessoTipo.trim().length() > 0) {
			pendenciaArquivoDt = pendenciaArquivoNe.consultarVotoDesembargador(id_ServentiaCargo, audienciaProcessoDt.getId(), idProcessoTipo, null);	
		} else {
			ProcessoDt processoDt = new ProcessoNe().consultarId(audienciaProcessoDt.getId_Processo());		
			if (processoDt != null){
				pendenciaArquivoDt = pendenciaArquivoNe.consultarVotoDesembargador(id_ServentiaCargo, audienciaProcessoDt.getId(), processoDt.getId_ProcessoTipo(), null);
			}
			
			if (pendenciaArquivoDt == null) {
				pendenciaArquivoDt = pendenciaArquivoNe.consultarVotoDesembargador(id_ServentiaCargo, audienciaProcessoDt.getId() , null, null);	
			}
		}
		
		pendenciaArquivoNe = null;
		return pendenciaArquivoDt;
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo voto em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param String id_PendenciaVoto
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 */
	public PendenciaArquivoDt consultarVotoDesembargadorPendencia(String id_ServentiaCargo, String idAudiProc, String id_PendenciaVoto) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		
		pendenciaArquivoDt = pendenciaArquivoNe.consultarVotoDesembargadorPendencia(id_ServentiaCargo, idAudiProc, id_PendenciaVoto);		
				
		pendenciaArquivoNe = null;
		return pendenciaArquivoDt;
	}
	
	public PendenciaArquivoDt consultarVotoDesembargadorPendencia(String id_ServentiaCargo, String idAudiProc, String id_PendenciaVoto, FabricaConexao obFabricaConexao) throws Exception{
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		return pendenciaArquivoNe.consultarVotoDesembargadorPendencia(id_ServentiaCargo, idAudiProc, id_PendenciaVoto, obFabricaConexao);	
	}
	
	/**
	 * Método responsável por consultar as pendências a gerar para uma conclusção do tipo voto
	 * @param PendenciaArquivoDt pendenciaArquivoDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarPendenciasVotoEmenta(PendenciaArquivoDt pendenciaArquivoDt) throws Exception{
		List pendenciasAGerar = null;
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		
		pendenciasAGerar = pendenciaArquivoNe.consultarPendenciasVotoEmenta(pendenciaArquivoDt, null);
				
		pendenciaArquivoNe = null;
		return pendenciasAGerar;
	}

	/**
	 * Método responsável por consultar último relatório inserido no processo por um determinado usuário
	 * @param String id_Processo
	 * @param UsuarioNe usuarioNe
	 * @return List
	 * @throws Exception
	 */
	public MovimentacaoArquivoDt consultarRelatorioProcesso(String id_Processo, UsuarioNe usuarioNe ) throws Exception{
		MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoDt();
		ArquivoNe arquivoNe = new ArquivoNe();
		
		movimentacaoArquivoDt.setId(arquivoNe.consultarRelatorioProcesso(id_Processo,usuarioNe.getUsuarioDt().getId_UsuarioServentia()));
		
		if (movimentacaoArquivoDt.getId() != null && !movimentacaoArquivoDt.getId().equals("") )
			movimentacaoArquivoDt.setHash(usuarioNe.getCodigoHash(movimentacaoArquivoDt.getId() + id_Processo));
					
		arquivoNe = null;
		return movimentacaoArquivoDt;
	}

	
	/**
	 * Método responsável por gerar uma movimentação de julgamento adiado
	 * 
	 * @param audienciaProcessoDt
	 * @param dataSessao
	 * @param Id_UsuarioServentia
	 * @param Id_UsuarioLog
	 * @param IpComputadorLog
	 * @throws Exception
	 * @author mmgomes
	 */
	public String gerarMovimentacaoJulgamentoManterAdiado(AudienciaDt audienciaDt, String Id_UsuarioServentia, String Id_UsuarioLog, String IpComputadorLog) throws Exception
	{				
		FabricaConexao obFabricaConexao = null;		
		String retorno = "";
		
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
						
			// Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
			ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
			audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);	
			
			// Obtem a nova sessão...
			AudienciaDt audienciaNovaDt = obPersistencia.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), audienciaDt.isVirtual());
			
			if (audienciaNovaDt != null) {
				LogDt logDt = new LogDt(Id_UsuarioLog, IpComputadorLog);
				// Cria a nova Audiência Processo
				obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, false, audienciaNovaDt, logDt, obFabricaConexao);
				// Gera a movimentação no Processo
				gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, Id_UsuarioServentia, false, true, false, audienciaNovaDt, logDt , obFabricaConexao);
			} else {
				retorno = "Não foi localizada uma sessão aberta com data posterior.";
			}				
			
			obFabricaConexao.finalizarTransacao();
			
			return retorno;
			
		} catch(Exception e) {			
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{			
			obFabricaConexao.fecharConexao();
		}			
	}
	
	public MovimentacaoDt gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(AudienciaDt audienciaDt, String Id_UsuarioServentia, boolean ehJulgamentoIniciado, boolean ehManterJulgamentoAdiado, boolean ehAlteracaoExtratoAta, AudienciaDt audienciaNovaDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		return gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, Id_UsuarioServentia, ehJulgamentoIniciado, ehManterJulgamentoAdiado, ehAlteracaoExtratoAta, false, audienciaNovaDt, logDt, obFabricaConexao);
	}
	
	/**
	 * Método responsável em gerar as movimentações sessão de julgamento adiada ou iniciada,  
	 * bem como levar esta sessão automaticamente para a próxima pauta aberta em uma data diferente da data atual.
	 * 
	 * @param audienciaDt
	 * @param Id_UsuarioServentia	 
	 * @param ehJulgamentoIniciado
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public MovimentacaoDt gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(AudienciaDt audienciaDt, String Id_UsuarioServentia, boolean ehJulgamentoIniciado, boolean ehManterJulgamentoAdiado, boolean ehAlteracaoExtratoAta, boolean ehAdiadoComPedidoSO, AudienciaDt audienciaNovaDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
				
		MovimentacaoDt retorno = null;
	
		audienciaDt.getAudienciaProcessoDt().setAudienciaDt(audienciaDt);
		
		String dataAudienciaOriginal = audienciaDt.getDataAgendada();
		if(audienciaDt.getAudienciaProcessoDt().getDataAudienciaOriginal().trim().length() > 0) dataAudienciaOriginal = audienciaDt.getAudienciaProcessoDt().getDataAudienciaOriginal();
		//lrcampos 28/06/2019 correção no complemento quando a movimentação do adiamento for por motivo de sutentação oral deferida.
		String textoAdiadoComPedidoSo = ehAdiadoComPedidoSO ? "(Adiado em razão do Pedido de Sustentação Oral Deferido na sessão de: %s - Próxima sessão prevista: %s)" : "(Adiado na sessão de: %s - Próxima sessão prevista: %s)";
		String complemento = String.format(textoAdiadoComPedidoSo, dataAudienciaOriginal, audienciaNovaDt.getDataAgendada());			
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();	
		
		if (ehJulgamentoIniciado){		
			if (ehAlteracaoExtratoAta) retorno = movimentacaoNe.gerarMovimentacaoSessaoJulgamentoIniciadoAlteracoes(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
			else retorno = movimentacaoNe.gerarMovimentacaoSessaoJulgamentoIniciado(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
		}
		else {
			if(ehAlteracaoExtratoAta)
				retorno = movimentacaoNe.gerarMovimentacaoSessaoJulgamentoAdiadoAlteracoes(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
			else if(ehManterJulgamentoAdiado)
				retorno = movimentacaoNe.gerarMovimentacaoSessaoMantidoJulgamentoAdiado(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);					
			else {
				retorno = movimentacaoNe.gerarMovimentacaoSessaoJulgamentoAdiada(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
				if(ehAdiadoComPedidoSO) {
					AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
					audienciaProcessoStatusDt = new AudienciaProcessoStatusNe().consultarAudienciaProcessoStatusPorCodigo(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL));
					new AudienciaProcessoNe().alterarStatusAudiencia(audienciaDt.getAudienciaProcessoDt(), Integer.valueOf(audienciaProcessoStatusDt.getId()) , logDt, obFabricaConexao);
				}
			}
		}	
		
		movimentacaoNe = null;
		
		return retorno;			
	}
	
	// jvosantos - 11/12/2019 15:51 - Refatorar para copiar status da antiga para a nova
	/***
	 * Obtém uma nova Audiencia Processo para ser usada na movimentação julgamento iniciado / adiado 
	 * 
	 * @param audienciaDt
	 * @param ehJulgamentoIniciado
	 * @param audienciaNovaDt
	 * @param logDt
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public AudienciaProcessoDt obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(AudienciaDt audienciaDt, boolean ehJulgamentoIniciado, AudienciaDt audienciaNovaDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception{
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		AudienciaProcessoDt audienciaProcessoDtNova = null;
		AudienciaProcessoDt audienciaProcessoDtAntigo = audienciaDt.getAudienciaProcessoDt();
		
		audienciaProcessoDtAntigo.setAudienciaDt(audienciaDt);
		
		audienciaProcessoDtNova = ehJulgamentoIniciado
				? audienciaProcessoNe.alterarAudienciaProcessoJulgamentoIniciado(audienciaProcessoDtAntigo,
						audienciaNovaDt, logDt, obFabricaConexao)
				: audienciaProcessoNe.alterarAudienciaProcessoJulgamentoAdiado(audienciaProcessoDtAntigo,
						audienciaNovaDt, logDt, obFabricaConexao);
		
		// jvosantos - 11/12/2019 15:23 - Copiar os status da AUDI_PROC analista (da antiga para nova)
		if(StringUtils.isNotEmpty(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoAnalista())) {
			audienciaProcessoNe.alterarStatusAnalista(audienciaProcessoDtNova.getId(), Integer.parseInt(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoAnalista()), obFabricaConexao);
			
			audienciaProcessoDtNova.setAudienciaProcessoStatusAnalista(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoAnalista());
			audienciaProcessoDtNova.setAudienciaProcessoStatusCodigoAnalista(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoAnalista());
		}
		
		// jvosantos - 11/12/2019 15:23 - Copiar os status da AUDI_PROC temp (da antiga para nova)
		if(StringUtils.isNotEmpty(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoTemp())) {
			audienciaProcessoNe.alterarStatusTemp(audienciaProcessoDtNova.getId(), Integer.parseInt(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoTemp()), obFabricaConexao);		
			
			audienciaProcessoDtNova.setAudienciaProcessoStatusTemp(audienciaProcessoDtAntigo.getAudienciaProcessoStatusTemp());
			audienciaProcessoDtNova.setAudienciaProcessoStatusCodigoTemp(audienciaProcessoDtAntigo.getAudienciaProcessoStatusCodigoTemp());
		}else {
			AudienciaProcessoStatusDt statusAudienciaTemp = audienciaProcessoNe.consultarStatusAudienciaTemp(audienciaProcessoDtAntigo.getId(), obFabricaConexao);
			if(statusAudienciaTemp != null) audienciaProcessoNe.alterarStatusAudienciaTemp(audienciaProcessoDtNova.getId(), statusAudienciaTemp.getAudienciaProcessoStatusCodigo(), obFabricaConexao);
		}
				
		if(audienciaProcessoDtNova.getAudienciaDt().isVirtual()) {
			boolean permiteSO = audienciaProcessoNe.consultarPodeSustentacaoOral(audienciaProcessoDtAntigo.getId());
			audienciaProcessoDtNova.setPermiteSustentacaoOral(permiteSO);
			audienciaProcessoNe.alterarPedidoSustentacaoOral(audienciaProcessoDtNova.getId(), permiteSO, obFabricaConexao);
			
			new RecursoSecundarioParteNe().copiarDeAudienciaProcessoParaAudienciaProcessoNova(audienciaProcessoDtAntigo.getId(), audienciaProcessoDtNova.getId(), obFabricaConexao);
			
			Funcoes.preencheLogDt(audienciaProcessoDtNova, logDt);
			
			copiarPendenciasVotoEmentaAudienciaProcessoVirtual(audienciaProcessoDtNova, audienciaProcessoDtAntigo, logDt, obFabricaConexao);

			AudienciaPs audienciaPs = new AudienciaPs(obFabricaConexao.getConexao());
			AudienciaNe audienciaNe = new AudienciaNe();
			VotoNe votoNe = new VotoNe();
			
			// Copiar votantes da sessão antiga para a nova.
			List<AudienciaProcessoVotantesDt> votantes = audienciaPs.consultarVotantesSessaoVirtual(audienciaProcessoDtAntigo);
			
			cadastrarVotantes(audienciaProcessoDtNova, audienciaPs, votantes);

			if (audienciaNe.isAudienciaVirtualIniciada(audienciaProcessoDtNova.getAudienciaDt().getId()) && audienciaNe.isAudienciaVirtualIniciada(audienciaProcessoDtAntigo.getAudienciaDt().getId()))
				votoNe.atualizarMPPresidenteSessao(audienciaProcessoDtNova, obFabricaConexao);
		}
		
		return audienciaProcessoDtNova;
	}
	
	public void copiarPendenciasVotoEmentaAudienciaProcessoVirtual(AudienciaProcessoDt audienciaProcessoNovaDt, AudienciaProcessoDt audienciaProcessoDtAntigo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		PendenciaDt pendenciaVoto = pendenciaNe.consultarId(audienciaProcessoNovaDt.getId_PendenciaVotoRelator(), obFabricaConexao);
		
		List<PendenciaArquivoDt> pendArqCopiar = getListPendenciaArquivoDtParaCopiar(pendenciaVoto, obFabricaConexao);
		List<PendenciaResponsavelDt> responsaveis = pendenciaResponsavelNe.consultarResponsaveis(pendenciaVoto.getId(), obFabricaConexao);
		
		String idNovaPend = criarPendenciaFilha(pendenciaVoto, audienciaProcessoNovaDt.getId(), pendArqCopiar,
				responsaveis, logDt, obFabricaConexao);

		audienciaProcessoNovaDt.setId_PendenciaVotoRelator(idNovaPend);
		
		boolean temEmenta = false;
		
		if(StringUtils.isNotEmpty(audienciaProcessoNovaDt.getId_PendenciaEmentaRelator())) {
			PendenciaDt pendenciaEmenta = pendenciaNe.consultarId(audienciaProcessoNovaDt.getId_PendenciaEmentaRelator(), obFabricaConexao);
			if(pendenciaEmenta != null) {
				pendArqCopiar = getListPendenciaArquivoDtParaCopiar(pendenciaEmenta, obFabricaConexao);
				responsaveis = pendenciaResponsavelNe.consultarResponsaveis(pendenciaEmenta.getId(), obFabricaConexao);
				
				idNovaPend = criarPendenciaFilha(pendenciaEmenta, audienciaProcessoNovaDt.getId(), pendArqCopiar,
							responsaveis, logDt, obFabricaConexao);

				audienciaProcessoNovaDt.setId_PendenciaEmentaRelator(idNovaPend);
				temEmenta = true; // jvosantos - 16/12/2019 15:39 - Simplificar lógica
			}
		}
		
		audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoNovaDt, obFabricaConexao);
		
		if(!temEmenta)
			audienciaProcessoNe.limpaVinculoPendenciaEmenta(audienciaProcessoNovaDt, obFabricaConexao);
	}
	
	protected List<PendenciaArquivoDt> getListPendenciaArquivoDtParaCopiar(PendenciaDt pendencia, FabricaConexao obFabricaConexao) throws Exception {
		List<PendenciaArquivoDt> pendArqCopiar = Optional.ofNullable(new PendenciaArquivoNe().consultarPendencia(pendencia, true, obFabricaConexao)).orElse(Collections.emptyList()); // jvosantos - 26/12/2019 15:25 - Buscar o relacionamento de pendencia com arquivos 
		pendArqCopiar = pendArqCopiar.stream().filter(x -> StringUtils.isEmpty(x.getArquivoDt().getUsuarioAssinador())).collect(Collectors.toList());
		return pendArqCopiar;
	}

	protected String criarPendenciaFilha(PendenciaDt pendencia, String idAudienciaProcesso,
			List<PendenciaArquivoDt> pendArqCopiar, List<PendenciaResponsavelDt> responsaveisCopiar, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		
		Funcoes.preencheLogDt(pendencia, logDt);
		
		pendencia.setCodigoTemp("");
		if(pendencia.getPendenciaStatusCodigoToInt() == PendenciaStatusDt.ID_PRE_ANALISADA) {
			pendencia.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
			pendencia.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
		}
		
		String idNovaPend = pendenciaNe.gerarPendenciaFilha(pendencia, responsaveisCopiar, null, null, false, false, false, obFabricaConexao);
		// jvosantos - 26/12/2019 15:26 - Copiar arquivos e relacionamento para nova pendencia
		copiarArquivosEPendenciaArquivo(idNovaPend, pendArqCopiar, arquivoNe, pendenciaArquivoNe, logDt, obFabricaConexao);

		// jvosantos - 05/12/2019 18:00 - Correção salvar na AUDI_PROC_PEND a nova pendencia
		audienciaProcessoPendenciaNe.salvar(idNovaPend, idAudienciaProcesso, obFabricaConexao);
		
		pendenciaNe.limparCodigoTempPendencia(idNovaPend, obFabricaConexao);
		return idNovaPend;
	}
	
	// jvosantos - 26/12/2019 15:23 - Método para copiar arquivos e relacionamento com pendencia para a nova pendencia
	public static void copiarArquivosEPendenciaArquivo(String idNovaPend, List<PendenciaArquivoDt> pendArqCopiar,
			ArquivoNe arquivoNe, PendenciaArquivoNe pendenciaArquivoNe, LogDt logDt,
			FabricaConexao obFabricaConexao) throws Exception {
		if(pendArqCopiar == null || pendArqCopiar.isEmpty()) return;
		for (PendenciaArquivoDt pendArq : pendArqCopiar) {
			ArquivoDt arq = arquivoNe.consultarId(pendArq.getId_Arquivo(), obFabricaConexao.getConexao());
			
			if(arq == null)
				throw new Exception("Não foi encontrado o Arquivo de ID ("+pendArq.getId_Arquivo()+") da PEND_ARQ ("+pendArq.getId()+").");
			
			arq.setId(StringUtils.EMPTY);
			Funcoes.preencheLogDt(arq, logDt);
			arquivoNe.salvar(arq, obFabricaConexao);
			
			pendArq.setId(StringUtils.EMPTY);
			pendArq.setId_Pendencia(idNovaPend);
			pendArq.setId_Arquivo(arq.getId());
			Funcoes.preencheLogDt(pendArq, logDt);
			
			pendenciaArquivoNe.salvar(pendArq, obFabricaConexao);
		}
	}

	protected void cadastrarVotantes(AudienciaProcessoDt audienciaProcessoNovaDt, AudienciaPs audienciaPs,
			List<AudienciaProcessoVotantesDt> votantes) throws Exception {
		VotanteTipoNe votanteTipoNe = new VotanteTipoNe();
		for (AudienciaProcessoVotantesDt votante : votantes) {
			int codigoVotante = votante.getVotanteTipoCodigoInt();

			if(codigoVotante == -1 && StringUtils.isNotEmpty(votante.getId_VotanteTipo()))
				codigoVotante = Optional.ofNullable(votanteTipoNe.consultarVotanteTipoId(votante.getId_VotanteTipo())).orElse(-1);

			if (Arrays.asList(VotanteTipoDt.MINISTERIO_PUBLICO, VotanteTipoDt.PRESIDENTE_SESSAO, -1).contains(codigoVotante)) continue;

			votante.setId_AudienciaProcesso(audienciaProcessoNovaDt.getId());
			audienciaPs.cadastrarVotantesSessaoVirtual(votante);
		}
	}
	
	/**
	 * Salva movimentação de sessões de 2º grau essa movimentação refere-se somente a um processo da sessão. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimentação correspondente e pendências de acordo com o que foi selecionado pelo usuário
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt, usuário que está realizando a movimentação 
	 * 
	 * @author mmgomes
	 */
	public String salvarMovimentacaoAudienciaProcessoSessaoSegundoGrau(AudienciaMovimentacaoDt audienciaMovimentacaoDt,  UsuarioDt usuarioDt) throws MensagemException, Exception {
		
		FabricaConexao obFabricaConexao = null;		
		String retorno = "";	
		try{						
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			LogDt logDt = new LogDt(usuarioDt.getId(), audienciaMovimentacaoDt.getIpComputadorLog());
			
			int grupoTipoUsuarioLogado = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
			if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU){
				
				if (!(audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada() || 
					  audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || 
					  audienciaMovimentacaoDt.isMovimentacaoSessaoRetiradaDePauta() ||
					  audienciaMovimentacaoDt.isMovimentacaoSessaoDesmarcada())) {
					if (!audienciaMovimentacaoDt.temPresidente()) retorno += "O Presidente deve ser informado. \n";
					
					// jvosantos - 02/09/2019 12:02 - Verificar se a quantidade de votantes bate com a de votos, gera uma Exception caso não
					if(audienciaMovimentacaoDt.getAudienciaDt().isVirtual() && audienciaMovimentacaoDt.getAudienciaDt().isSessaoIniciada()) {
						VotoNe votoNe = new VotoNe();
						
						// jvosantos - 16/10/2019 19:24 - Desativar validação para Serventias Especiais por causa das regras de 2/3
						if(!votoNe.isServentiaEspecial(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId_Serventia()) && !votoNe.verificarQuantidadeVotos(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), obFabricaConexao))
							throw new Exception("A quantidade de votos realizados é menor que a quantidade de votantes para essa audiência!");
					}
					
					//Consultando a serventia para saber se é alguma serventia que não necessita MP para inserir extrato de ata.
					//Atualmente, somente o Conselho de Magistratura tem essa exceção.
					ServentiaDt serventiaDt = new ServentiaNe().consultarId(usuarioDt.getId_Serventia());
					if(!serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA))){
						if (!audienciaMovimentacaoDt.temServentiaMp()) retorno += "A Serventia do MP Responsável deve ser informada. \n";
						if (!audienciaMovimentacaoDt.temMpResponsavel()) retorno += "O MP Responsável deve ser informado. \n";
					}
					if (audienciaMovimentacaoDt.isVotoPorMaioria()){
						if (audienciaMovimentacaoDt.getId_ServentiaCargoRedator().trim().length() == 0){
							retorno += "O Redator deve ser informado. \n";
						} 
						//else {	
							//processoResponsavelNe = new ProcessoResponsavelNe(); 
							//ServentiaCargoDt relatorProcesso = processoResponsavelNe.consultarRelatorResponsavelProcessoSegundoGrau(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), obFabricaConexao);
							
							//if (relatorProcesso != null && audienciaMovimentacaoDt.getId_ServentiaCargoRedator().trim().equalsIgnoreCase(relatorProcesso.getId())) retorno += "O Redator não deve ser o Relator. \n";
							//votoVencido = (relatorProcesso != null && !audienciaMovimentacaoDt.getId_ServentiaCargoRedator().trim().equalsIgnoreCase(relatorProcesso.getId())) ;
						//}
					}	
				}				
				
				if (retorno.length() == 0) {
					if (audienciaMovimentacaoDt.getAudienciaDt() != null && 
						isVirtual(audienciaMovimentacaoDt.getAudienciaDt().getId(), obFabricaConexao)) {
						retorno = salvarMovimentacaoAudienciaProcessoSessaoVirtualSegundoGrauAnalista(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
					} else if (audienciaMovimentacaoDt.isMarcarDataAgendada()) {
						retorno = salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnalistaTemp(audienciaMovimentacaoDt, audienciaMovimentacaoDt.getId_ServentiaCargoPresidente(), audienciaMovimentacaoDt.getId_ServentiaCargoMp(), audienciaMovimentacaoDt.isVotoPorMaioria(), usuarioDt, logDt, obFabricaConexao);						
					} else {
						retorno = salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnalista(audienciaMovimentacaoDt, audienciaMovimentacaoDt.isVotoPorMaioria(), usuarioDt, logDt, obFabricaConexao);
					}
				}				
			} else if (grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){
				salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
			} else if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU){
				if (audienciaMovimentacaoDt.isPreAnalise()){
					salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
				} else {
					salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnaliseDesembargador(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
				}			
			} else {
				retorno = "Tipo de usuário não mapeado para executar esta função.";
			}			
			
			obFabricaConexao.finalizarTransacao();
			
			return retorno;
		} catch(MensagemException m){
			obFabricaConexao.cancelarTransacao();
			cancelarMovimentacaoAudiencia(audienciaMovimentacaoDt);
	        throw m;
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarMovimentacaoAudiencia(audienciaMovimentacaoDt);
			throw e;
		} finally{		
			obFabricaConexao.fecharConexao();			
		}
	}
	
	private boolean isVirtual(String idAudiencia, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaPs audienciaPs = new AudienciaPs(obFabricaConexao.getConexao());
		return audienciaPs.isVirtual(idAudiencia);
	}
	
	/**
	 * Salva movimentação de sessões de 2º grau essa movimentação refere-se somente a um processo da sessão. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimentação correspondente e pendências de acordo com o que foi selecionado pelo usuário
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt, usuário que está realizando a movimentação
	 * 
	 * @author mmgomes
	 */
	private String salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnalista(AudienciaMovimentacaoDt audienciaMovimentacaoDt,
			                                                                    boolean isVotoPorMaioria,
			                                                                    UsuarioDt usuarioDt, 
			                                                                    LogDt logDt, 
			                                                                    FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoNe processoNe = new ProcessoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		List arquivos = null;
		List movimentacoes = new ArrayList();
		String retornoMensagemInconsistencia = "";
					
		int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
        codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());	        
        
        if (codigoStatus == AudienciaProcessoStatusDt.DESMARCAR_PAUTA || 
        	codigoStatus == AudienciaProcessoStatusDt.RETIRAR_PAUTA || 
        	codigoStatus == AudienciaProcessoStatusDt.REMARCADA){
        	
        	salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, null, usuarioDt);
        	
        	if (audienciaMovimentacaoDt != null &&
        	    audienciaMovimentacaoDt.getAudienciaDt() != null &&
        	    audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null &&
        	    (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoEEmentaRelator() ||
        	     audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoEEmentaRedator())) {
        		
        		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoRelator()) {
        			PendenciaDt pendenciaDtVoto = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRelator());
        			if (pendenciaDtVoto != null) {
        				pendenciaNe.descartarPendencia(pendenciaDtVoto, usuarioDt);
        			}
        		}
        		
        		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiEmentaRelator()) {
        			PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator());
        			if (pendenciaDtEmenta != null) {
        				pendenciaNe.descartarPendencia(pendenciaDtEmenta, usuarioDt);
        			}
        		}
        		
        		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoRedator()) {
        			PendenciaDt pendenciaDtVotoRedator = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRedator());
        			if (pendenciaDtVotoRedator != null) {
        				pendenciaNe.descartarPendencia(pendenciaDtVotoRedator, usuarioDt);
        			}
        		}
        		
        		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiEmentaRedator()) {
        			PendenciaDt pendenciaDtEmentaRedator = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRedator());
        			if (pendenciaDtEmentaRedator != null) {
        				pendenciaNe.descartarPendencia(pendenciaDtEmentaRedator, usuarioDt);
        			}
        		}
        	}
        	
        	return retornoMensagemInconsistencia;
        } 
        else if (codigoStatus == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO)
        {
        	audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("4");
        } 
        else if (codigoStatus == AudienciaProcessoStatusDt.JULGAMENTO_INICIADO)
        {
        	audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("3");
        }
        
        AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
        
        // Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
        ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
				
        MovimentacaoDt movimentacaoDt = null;
        AudienciaProcessoDt audienciaProcessoNovaDt = null;
        String idAudienciaProcesso = audienciaDt.getAudienciaProcessoDt().getId();
        // Chama método para gerar a movimentação de início ou adiamento de sessão...			
		if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
			AudienciaDt audienciaNovaDt = null;
			if (!audienciaMovimentacaoDt.isAlteracaoExtratoAta()) 
			{
				//Obtem a nova sessão...
				AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
				audienciaNovaDt = obPersistencia.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), false);
				// Verifica se existe uma Próxima Sessão Aberta.
				if (audienciaNovaDt == null){
					retornoMensagemInconsistencia = "Não foi localizada uma sessão aberta com data posterior.";
					return retornoMensagemInconsistencia;
				}			
				// Cria a nova Audiência Processo
				audienciaProcessoNovaDt = obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaNovaDt, logDt, obFabricaConexao);
				idAudienciaProcesso = audienciaProcessoNovaDt.getId();
			} else {
				audienciaNovaDt = audienciaDt;
				audienciaProcessoNovaDt = audienciaDt.getAudienciaProcessoDt();
			}
			// Gera a movimentação no Processo			
			movimentacaoDt = gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), audienciaNovaDt, logDt, obFabricaConexao);				
		}							
					
		arquivos = audienciaMovimentacaoDt.getListaArquivos();
		String Id_ArquivoAta = "";
		// Salva arquivos inseridos
		if (arquivos != null && arquivos.size() > 0) {
			movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
			// Salvando vínculos do arquivo de Ata com a Audiência Processo
			Id_ArquivoAta = arquivoDt.getId();										
		}		
		
		//Vincula os arquivos, dependendo do tipo da movimentação
		if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
			//Vincula o arquivo inserido á Audiência Processo
			audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoNovaDt, "", "", "", Id_ArquivoAta, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada(), "", "", "",logDt, obFabricaConexao);
		} else {
			//Chama método para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimentação correspondente a movimentação da audiência e inserir responsáveis pela audiência
			movimentacaoDt = movimentarAudienciaAnalistaSegundoGrau(audienciaDt, 
					                                                audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo(), 
					                                                audienciaMovimentacaoDt.getId_NovaSessao(), 
					                                                audienciaMovimentacaoDt.getDataNovaSessao(), 
					                                                audienciaMovimentacaoDt.getAudienciaStatus(), 
					                                                audienciaMovimentacaoDt.getAudienciaStatusCodigo(), 
					                                                Id_ArquivoAta, 
					                                                audienciaMovimentacaoDt.getId_ServentiaCargoPresidente(), 
					                                                audienciaMovimentacaoDt.getId_ServentiaCargoMp(), 
					                                                audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), 
					                                                usuarioDt, 
					                                                logDt, 
					                                                obFabricaConexao);				
		}			
		movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
		movimentacoes.add(movimentacaoDt);	 					
		
		String visibilidade = null;
		if (processoDt.isSegredoJustica()){
			visibilidade = String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
		}
		
		// Salvando vínculo entre movimentação e arquivos inseridos
		movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);			

		// Salvando pendências da movimentação
		if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) 
			pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);			

		// Atualiza Classificador processo
		if (audienciaMovimentacaoDt.getId_Classificador().length() > 0)
			processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);			

//		// Gera recibo para arquivos de movimentações
//		movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
					
		// Cria publicação
		// Decreto 1.684 / 2020, Art 2o e 3o
		if (!processoDt.isSigiloso()) {
			pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
		}
		
		 // Se for voto vencido iremos gerar uma conclusão do tipo voto/ementa para o desembargador redator poder pré-analisar
        if (isVotoPorMaioria) {        	
        	if (usuarioDt.isUPJSegundoGrau()) {
        		PendenciaDt pendenciaDtVotoRelatorSessao = null;
        		if (audienciaDt.getAudienciaProcessoDt().possuiVotoRelator()) {
        			pendenciaDtVotoRelatorSessao = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator(), 
        					                                               obFabricaConexao);
        			
        			if (pendenciaDtVotoRelatorSessao == null) {
        				PendenciaDt pendenciaDtVotoRelatorSessaoDescartada = pendenciaNe.consultarFinalizadaId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator(),
        						                                                                               obFabricaConexao);
    					
            			if (pendenciaDtVotoRelatorSessaoDescartada != null) {
    						pendenciaNe.reaberturaAutomaticaPendencia(pendenciaDtVotoRelatorSessaoDescartada, usuarioDt, obFabricaConexao);
    						pendenciaDtVotoRelatorSessao = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator(), 
                                                                                   obFabricaConexao);
    					}
        			}	
        		}
        		
        		if (pendenciaDtVotoRelatorSessao == null) {
    				pendenciaDtVotoRelatorSessao = criarNovaPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), 
												    					  movimentacaoDt.getId(), 
												    					  audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(),
												    					  audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), 
																          null, 
																          true, 
																          usuarioDt, 
																          obFabricaConexao);
    			} else if(!pendenciaDtVotoRelatorSessao.isIndicadorVotoVencido()) {
    				pendenciaDtVotoRelatorSessao.setCodigoTemp(String.valueOf(PendenciaDt.VOTO_VENCIDO_RELATOR));
    				pendenciaNe.AlterarCodigoTempPendencia(pendenciaDtVotoRelatorSessao, obFabricaConexao);
    			}
        		
        		if (pendenciaDtVotoRelatorSessao != null) {
	        		audienciaDt.getAudienciaProcessoDt().setId_PendenciaVotoRelator(pendenciaDtVotoRelatorSessao.getId());
	        	}
        		
        	} else {        		
	        	
	        	
	        	// Alterar pendência de voto para o desembargador inserir o voto vencido
	        	PendenciaDt pendenciaDtVotoRelatorSessao = gerarPendenciaVotoRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(),
												        			                        movimentacaoDt.getId(), 
												        			                        audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), 
												        			                        audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), 
												        			                        null,
												        			                        idAudienciaProcesso,
												        			                        true, 
												        			                        usuarioDt, 
												        			                        obFabricaConexao);
	        	
	        	if (pendenciaDtVotoRelatorSessao != null) {
	        		audienciaDt.getAudienciaProcessoDt().setId_PendenciaVotoRelator(pendenciaDtVotoRelatorSessao.getId());
	        	}    	
        	}  
        	
        	// Finalizar a pendência de ementa do desembargador com voto vencido
        	finalizarPendencia(audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), 
        			           pendenciaNe, 
        			           usuarioDt, 
        			           obFabricaConexao);
        	
        	// Gerar pendência de voto para o desembargador inserir o voto vencedor
        	PendenciaDt pendenciaDtVotoRedatorSessao = null;
        	if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoRedator()) {
        		pendenciaDtVotoRedatorSessao = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRedator(), obFabricaConexao);    			
    		}
        	
        	if (pendenciaDtVotoRedatorSessao == null) {
        		pendenciaDtVotoRedatorSessao = gerarPendenciaVotoRedatorSessao(audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), 
														                        movimentacaoDt.getId(), 
														                        audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), 
														                        audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), 
														                        null, 
														                        idAudienciaProcesso, 
														                        usuarioDt, 
														                        obFabricaConexao);	
        	}        	 
        	
        	if (pendenciaDtVotoRedatorSessao != null) {
        		audienciaDt.getAudienciaProcessoDt().setId_PendenciaVotoRedator(pendenciaDtVotoRedatorSessao.getId());
        	}   
        } else {
        	PendenciaDt pendenciaDtVotoRelatorSessao = null;
        	if (usuarioDt.isUPJSegundoGrau()) {
        		if (audienciaDt.getAudienciaProcessoDt().possuiVotoRelator()) {
        			pendenciaDtVotoRelatorSessao = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator(), 
        					                                               obFabricaConexao);
        			
        			if (pendenciaDtVotoRelatorSessao == null) {
        				PendenciaDt pendenciaDtVotoRelatorSessaoDescartada = pendenciaNe.consultarFinalizadaId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator(),
        						                                                                               obFabricaConexao);
    					
            			if (pendenciaDtVotoRelatorSessaoDescartada != null) {
    						pendenciaNe.reaberturaAutomaticaPendencia(pendenciaDtVotoRelatorSessaoDescartada, usuarioDt, obFabricaConexao);
    						pendenciaDtVotoRelatorSessao = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator(), 
                                                                                   obFabricaConexao);
    					}
        			}	
        		}
        		
        		if (pendenciaDtVotoRelatorSessao == null) {
    				pendenciaDtVotoRelatorSessao = criarNovaPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), 
												    					  movimentacaoDt.getId(), 
												    					  audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(),
												    					  audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), 
																          null, 
																          false, 
																          usuarioDt, 
																          obFabricaConexao);
        		} else if(pendenciaDtVotoRelatorSessao.isIndicadorVotoVencido()) {
    				pendenciaDtVotoRelatorSessao.limparCodigoTemp();
    				pendenciaNe.AlterarCodigoTempPendencia(pendenciaDtVotoRelatorSessao, obFabricaConexao);
    			}
        	} else {
        		// Gerar pendência de voto para o desembargador inserir o voto, caso não exista...
            	pendenciaDtVotoRelatorSessao = gerarPendenciaVotoRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(),
    																	       movimentacaoDt.getId(), 
    																	       audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), 
    																	       audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), 
    																	       null,
    																	       audienciaDt.getAudienciaProcessoDt().getId(),
    																	       false, 
    																	       usuarioDt, 
    																	       obFabricaConexao);
        	}
        	
        	if (pendenciaDtVotoRelatorSessao != null) {
        		audienciaDt.getAudienciaProcessoDt().setId_PendenciaVotoRelator(pendenciaDtVotoRelatorSessao.getId());
        	}
        	
        	if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiEmentaRelator()) {
        		
        		if (usuarioDt.isUPJSegundoGrau()) {
        			PendenciaDt pendenciaEmentaRelator = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), obFabricaConexao);
        			if (pendenciaEmentaRelator == null) {
        				pendenciaEmentaRelator = pendenciaNe.consultarFinalizadaId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), obFabricaConexao);
        				if (pendenciaEmentaRelator != null) {
        					pendenciaNe.reaberturaAutomaticaPendencia(pendenciaEmentaRelator, usuarioDt, obFabricaConexao);
        					pendenciaEmentaRelator = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), obFabricaConexao);
        				}
        			}  
        			
        			if (pendenciaEmentaRelator == null) {
    					audienciaDt.getAudienciaProcessoDt().setId_PendenciaEmentaRelator("null");
    				}
        		} else {        			
        			PendenciaArquivoDt pendenciaArquivoDtEmenta = this.consultarEmentaDesembargadorEmentaPendencia(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), 
        				                                                                                       audienciaDt.getAudienciaProcessoDt().getId(), 
        				                                                                                       audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), obFabricaConexao);
			
					if (pendenciaArquivoDtEmenta == null) {
						PendenciaDt pendenciaEmentaDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), obFabricaConexao);
						if (pendenciaEmentaDtDescartada != null) {
							pendenciaNe.reaberturaAutomaticaPendencia(pendenciaEmentaDtDescartada, usuarioDt, obFabricaConexao);
							pendenciaArquivoDtEmenta = this.consultarEmentaDesembargadorEmentaPendencia(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), 
	                                                                                                    audienciaDt.getAudienciaProcessoDt().getId(), 
	                                                                                                    audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), obFabricaConexao);
						}
					}	
					
					if (pendenciaArquivoDtEmenta == null) {
						audienciaDt.getAudienciaProcessoDt().setId_PendenciaEmentaRelator("null");
					}
        		}
        	}
        	
        	if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoRedator()) {
    			PendenciaDt pendenciaDtVotoRedator = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRedator(), obFabricaConexao);
    			if (pendenciaDtVotoRedator != null) {
    				pendenciaNe.descartarPendencia(pendenciaDtVotoRedator, usuarioDt, obFabricaConexao);
    			}
    		}
    		
    		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiEmentaRedator()) {
    			PendenciaDt pendenciaDtEmentaRedator = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRedator(), obFabricaConexao);
    			if (pendenciaDtEmentaRedator != null) {
    				pendenciaNe.descartarPendencia(pendenciaDtEmentaRedator, usuarioDt, obFabricaConexao);
    			}
    		}
        }
        
        audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaDt.getAudienciaProcessoDt(), obFabricaConexao);
		
		return retornoMensagemInconsistencia;		
	}
	
	/**
	 * Salva movimentação de sessões de 2º grau essa movimentação refere-se somente a um processo da sessão. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimentação correspondente e pendências de acordo com o que foi selecionado pelo usuário
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt, usuário que está realizando a movimentação
	 * 
	 * @author mmgomes
	 */
	private String salvarMovimentacaoAudienciaProcessoSessaoVirtualSegundoGrauAnalista(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		FinalizacaoExtratoAta finalizacao = null;

		if(audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada())
			finalizacao = new FinalizacaoAdiamento(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
		else if (Arrays
				.asList(AudienciaProcessoStatusDt.DESMARCAR_PAUTA, AudienciaProcessoStatusDt.RETIRAR_PAUTA,
						AudienciaProcessoStatusDt.REMARCADA)
				.contains(Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo())))
			finalizacao = new FinalizacaoRetirarPauta(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
		else
			finalizacao = new FinalizacaoNormal(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
		
		try {
			finalizacao.make();
		} catch (MensagemException e) {
			return e.getMessage();
		}
		
		return "";
	}

	public List<VotoDt> ordenarVotosPorData(List<VotoDt> votos) {
		votos = votos.stream().sorted(Comparator.comparing(VotoDt::getDataVoto, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
		return votos;
	}
	
	// jvosantos - 08/07/2019 15:37 - Método que salva as movimentações de ressalvas
	public void salvarRessalvas(Queue<VotoDt> ressalvas, AudienciaDt audienciaDt, ProcessoDt processoDt,
			UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		while(!ressalvas.isEmpty()) {
			VotoDt v = ressalvas.remove();
			
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(v.getIdServentiaCargo(), obFabricaConexao);
			
			// jvosantos - 21/11/2019 15:55 - Alterar o USU_REALIZADOR da movimentação para o Votante
			MovimentacaoDt movimentacaoRessalva = movimentacaoNe.gerarMovimentacao(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), MovimentacaoTipoDt.DECISAO, serventiaCargoDt.getId_UsuarioServentia(), "Declaração de Voto " + v.getNomeVotante(), new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), obFabricaConexao);
			List<ArquivoDt> arquivosRessalva = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(v.getIdPendencia());
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosRessalva, movimentacaoRessalva.getId(), processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null , logDt, obFabricaConexao);
			
			movimentacaoArquivoNe.inserirArquivos(arquivosRessalva, logDt, obFabricaConexao);
			
//			// jvosantos - 15/07/2019 11:48 - Gerar Recibo na movimentação de ressalva
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivosRessalva, Arrays.asList(movimentacaoRessalva), obFabricaConexao);
		}
	}

	private void deletarArquivosVotoEmenta(AudienciaDt audienciaDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		ArrayList <ArquivoDt> listPendArquivos =  (ArrayList<ArquivoDt>) pendenciaArquivoNe.consultarArquivosPendencia(audienciaProcessoDt.getId_PendenciaVotoRelator(), obFabricaConexao);
		ArquivoNe arquivoNe = new ArquivoNe();
		if( listPendArquivos != null && !listPendArquivos.isEmpty()) {
			for(ArquivoDt pendArq : listPendArquivos){
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt = pendenciaArquivoNe.consultarPendenciaArquivo(pendArq.getId());
				arquivoNe.excluir(pendArq, obFabricaConexao);
				pendenciaArquivoNe.excluir(pendenciaArquivoDt, obFabricaConexao);
			}
		}
		
		
	}

	/**
	 * Salva movimentação de sessões de 2º grau essa movimentação refere-se somente a um processo da sessão. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimentação correspondente e pendências de acordo com o que foi selecionado pelo usuário
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt, usuário que está realizando a movimentação
	 * 
	 * @author mmgomes
	 */
	private void salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnaliseDesembargador(AudienciaMovimentacaoDt audienciaMovimentacaoDt, 
			                                                                              UsuarioDt usuarioDt, 
			                                                                              LogDt logDt, 
			                                                                              FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();		
		ProcessoNe processoNe = new ProcessoNe();
		
		List arquivos = null;
		List movimentacoes = new ArrayList();
		
		AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
		
		// Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
		ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
		
		boolean isRelator = audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase(usuarioDt.getId_ServentiaCargo());
		
		//Consulta Voto 2º Grau
		PendenciaDt pendenciaDtVoto = null;
		if (audienciaMovimentacaoDt.getPendenciaArquivoDt() != null){
			pendenciaDtVoto = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia());
		} else if (isRelator && audienciaDt.getAudienciaProcessoDt().possuiVotoRelator()) {
			pendenciaDtVoto = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRelator());
		} else if (!isRelator && audienciaDt.getAudienciaProcessoDt().possuiVotoRedator()) {
			pendenciaDtVoto = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaVotoRedator());
		} else {
			List listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), usuarioDt.getId_ServentiaCargo(), String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), audienciaDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
			if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
		}
		
		//Consulta Ementa 2º Grau
		PendenciaDt pendenciaDtEmenta = null;
		if (audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta() != null){
			pendenciaDtEmenta = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId_Pendencia());
		} else if (isRelator && audienciaDt.getAudienciaProcessoDt().possuiEmentaRelator()) {
			pendenciaDtEmenta = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRelator());
		} else if (!isRelator && audienciaDt.getAudienciaProcessoDt().possuiEmentaRedator()) {
			pendenciaDtEmenta = pendenciaNe.consultarId(audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRedator());
		} else {
			List listaDePendenciasEmenta = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), usuarioDt.getId_ServentiaCargo(), String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), audienciaDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
			if (listaDePendenciasEmenta != null && listaDePendenciasEmenta.size() > 0) pendenciaDtEmenta = (PendenciaDt) listaDePendenciasEmenta.get(0);
		}
							
		arquivos = audienciaMovimentacaoDt.getListaArquivos();
		ArquivoDt arquivoDtEmenta = null;
		// Salva arquivos inseridos
		if (arquivos != null && arquivos.size() > 0) {				
			for (int i=0; i < arquivos.size(); i++){
				ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);
				if (arquivoDt != null)
				{
					if (arquivoDt.getNomeArquivo().trim().toUpperCase().startsWith(nomeArquivoEmenta.toUpperCase())) {
						arquivoDtEmenta = arquivoDt;						
					} else if (arquivoDt.getNomeArquivo().trim().length() >= prefixoArquivoEmenta.length() &&
							   arquivoDt.getNomeArquivo().trim().substring(0, prefixoArquivoEmenta.length()).equalsIgnoreCase(prefixoArquivoEmenta)) {
						arquivoDtEmenta = arquivoDt;
						// Retira da nomenclatura do arquivo a descrição correspondente à ementa...
						arquivoDtEmenta.setNomeArquivo(arquivoDt.getNomeArquivo().substring(prefixoArquivoEmenta.length()));
					}
				}
			}			
			movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
		}

		// Chama método para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimentação correspondente a movimentação da audiência
		// e inserir responsáveis pela audiência
		MovimentacaoDt movimentacaoDt = movimentarAudiencia(audienciaDt, audienciaMovimentacaoDt.getAudienciaStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
		movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
		movimentacoes.add(movimentacaoDt);
		
		//Fechar pendencia do Tipo Voto 2º Grau
		if (pendenciaDtVoto != null){
			pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String data = formatter.format(LocalDateTime.now());
			pendenciaDtVoto.setDataFim(data);
			pendenciaDtVoto.setDataVisto(data);
			pendenciaNe.fecharPendencia(pendenciaDtVoto, usuarioDt, obFabricaConexao);
		}
		
		//Fechar pendencia do Tipo Ementa 2º Grau
		if (pendenciaDtEmenta != null){
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String data = formatter.format(LocalDateTime.now());
			pendenciaDtEmenta.setDataFim(data);
			pendenciaDtEmenta.setDataVisto(data);
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);
		}

		// Salvando vínculo entre movimentação e arquivos inseridos
		if (arquivos != null && arquivos.size() > 0){
			String visibilidade=null;
			if (processoDt.isSegredoJustica()){
				visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
			}
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(),visibilidade, logDt, obFabricaConexao);
			
			if (arquivoDtEmenta != null) {
			
				// Iremos incluir o vínculo entre os arquivos de acórdão com o arquivo de ementa...
				for (int i=0; i < arquivos.size(); i++){
					ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);				
					if (arquivoDt != null && !arquivoDtEmenta.getId().trim().equalsIgnoreCase(arquivoDt.getId().trim())){
						AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao());
						obPersistencia.vincularEmentaAcordaoSegundoGrau(audienciaDt.getAudienciaProcessoDt().getId(), arquivoDtEmenta.getId(), arquivoDt.getId());
					}
				}
				
			}					
		}

		// Salvando pendências da movimentação
		if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null && audienciaMovimentacaoDt.getListaPendenciasGerar().size() > 0) {
			pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
		}

		// Atualiza Classificador processo
		if (audienciaMovimentacaoDt.getId_Classificador().length() > 0) {
			processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
		}

//		// Gera recibo para arquivos de movimentações
//		movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
		
		// Cria publicação
		// Decreto 1.684 / 2020, Art 2o e 3o
		if (!processoDt.isSigiloso()) {
			pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
		}
        
        // Registra no processo o indicador de julgamento do mérito do processo principal 
        if (audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal() != null && audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")){
        	processoNe.registrarJulgamentoMeritoProcessoPrincipal(processoDt.getId(), true, obFabricaConexao);
		}
        
        // Se for voto do redator iremos gerar uma conclusão do tipo voto para o desembargador relator inserir o voto vencido
        if (!isRelator){
        	
        	// Finalizar a pendência de ementa do desembargador com voto vencido
        	finalizarPendenciaEmentaRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), 
        			                              pendenciaNe, 
        			                              usuarioDt, 
        			                              audienciaDt.getAudienciaProcessoDt().getId(), 
        			                              obFabricaConexao);
        	
        	// Alterar pendência de voto para o desembargador inserir o voto vencido
        	PendenciaDt pendenciaDtVotoRelatorSessao = gerarPendenciaVotoRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(),
											        			                       movimentacaoDt.getId(), 
											        			                       audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), 
											        			                       audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), 
											        			                       null,
											        			                       audienciaDt.getAudienciaProcessoDt().getId(),
											        			                       true, 
											        			                       usuarioDt, 
											        			                       obFabricaConexao);
        	
        	if (pendenciaDtVotoRelatorSessao != null) {
        		audienciaDt.getAudienciaProcessoDt().setId_PendenciaVotoRelator(pendenciaDtVotoRelatorSessao.getId());
        	}	        			
        }    
        
        if (!processoDt.isSigiloso() && audienciaMovimentacaoDt.isVerificarProcesso()) {
			pendenciaNe.gerarPendenciaVerificarProcesso(processoDt.getId(), 
					                                    UsuarioServentiaDt.SistemaProjudi, 
					                                    processoDt.getId_Serventia(), 
					                                    movimentacaoDt.getId(), 
					                                    arquivos, 
					                                    pendenciaDtVoto, 
					                                    logDt, 
					                                    obFabricaConexao, 
					                                    processoDt.getId_ProcessoPrioridade());
		}
	}
	
	public PendenciaDt gerarPendenciaVotoRelatorSessao(String id_ServentiaCargo, 
										               String id_Movimentacao, 
										               String id_Processo, 
										               String id_ProcessoPrioridade,
										               ServentiaCargoDt serventiaCargoAssistente, 
										               String idAudienciaProcessoSessaoSegundoGrau,
										               boolean isRelatorVotoVencido, 
										               UsuarioDt usuarioDt, 
										               FabricaConexao obFabricaConexao) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		PendenciaDt pendenciaDtVoto = pendenciaNe.consultarPendenciaVotoRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, 
				                                                                      id_ServentiaCargo, 
				                                                                      obFabricaConexao);
		
		if (pendenciaDtVoto == null) {
			pendenciaDtVoto = criarNovaPendenciaVoto(id_ServentiaCargo, 
										            id_Movimentacao, 
										            id_Processo, 
										            id_ProcessoPrioridade, 
										            serventiaCargoAssistente, 
										            isRelatorVotoVencido, 
										            usuarioDt, 
										            obFabricaConexao);
		} else {
			if(pendenciaDtVoto.isIndicadorVotoVencido() && !isRelatorVotoVencido) {
				pendenciaDtVoto.limparCodigoTemp(); 
				pendenciaNe.limparCodigoTempPendencia(pendenciaDtVoto.getId(), obFabricaConexao);
			} else if(!pendenciaDtVoto.isIndicadorVotoVencido() && isRelatorVotoVencido) {
				pendenciaDtVoto.setCodigoTemp(String.valueOf(PendenciaDt.VOTO_VENCIDO_RELATOR));
				pendenciaNe.AlterarCodigoTempPendencia(pendenciaDtVoto, obFabricaConexao);
			}
		} 

		return pendenciaDtVoto;
	}
	
	public PendenciaDt gerarPendenciaVotoRedatorSessao(String id_ServentiaCargo, 
			                                           String id_Movimentacao, 
			                                           String id_Processo, 
			                                           String id_ProcessoPrioridade,
			                                           ServentiaCargoDt serventiaCargoAssistente, 
			                                           String idAudienciaProcessoSessaoSegundoGrau,
			                                           UsuarioDt usuarioDt, 
			                                           FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		PendenciaDt pendenciaDtVoto = pendenciaNe.consultarPendenciaVotoRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
	
		if (pendenciaDtVoto == null) {
			pendenciaDtVoto = criarNovaPendenciaVoto(id_ServentiaCargo, 
					                                 id_Movimentacao, 
					                                 id_Processo, 
					                                 id_ProcessoPrioridade, 
					                                 serventiaCargoAssistente, 
					                                 false, 
					                                 usuarioDt, 
					                                 obFabricaConexao);
		} 
		
		return pendenciaDtVoto;
	}
	
	public PendenciaDt gerarPendenciaEmentaRelatorSessao(String id_ServentiaCargo, 
												         String id_Movimentacao, 
												         String id_Processo, 
												         String id_ProcessoPrioridade,
												         ServentiaCargoDt serventiaCargoAssistente, 
												         String idAudienciaProcessoSessaoSegundoGrau,
												         UsuarioDt usuarioDt, 
												         FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();

		PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarPendenciaEmentaRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);

		if (pendenciaDtEmenta == null) {
			pendenciaDtEmenta = criarNovaPendenciaEmenta(id_ServentiaCargo, 
												         id_Movimentacao, 
												         id_Processo, 
												         id_ProcessoPrioridade, 
												         serventiaCargoAssistente, 
												         usuarioDt, 
												         obFabricaConexao);
		} 

		return pendenciaDtEmenta;
	}
	
	public PendenciaDt gerarPendenciaEmentaRedatorSessao(String id_ServentiaCargo, 
												         String id_Movimentacao, 
												         String id_Processo, 
												         String id_ProcessoPrioridade,
												         ServentiaCargoDt serventiaCargoAssistente, 
												         String idAudienciaProcessoSessaoSegundoGrau,
												         UsuarioDt usuarioDt, 
												         FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarPendenciaEmentaRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
		
		if (pendenciaDtEmenta == null) {
			pendenciaDtEmenta = criarNovaPendenciaEmenta(id_ServentiaCargo, 
												         id_Movimentacao, 
												         id_Processo, 
												         id_ProcessoPrioridade, 
												         serventiaCargoAssistente, 
												         usuarioDt, 
												         obFabricaConexao);
		} 

		return pendenciaDtEmenta;
	}
	
	public PendenciaDt gerarPendenciaVoto(String id_ServentiaCargo, 
			                              String id_Movimentacao, 
			                              String id_Processo, 
			                              String id_ProcessoPrioridade, 
			                              boolean isRelatorVotoVencido, 
			                              PendenciaNe pendenciaNe, 
			                              UsuarioDt usuarioDt, 
			                              FabricaConexao obFabricaConexao, 
			                              ServentiaCargoDt serventiaCargoAssistente, 
			                              String idAudienciaProcessoSessaoSegundoGrau) throws Exception{
		//Consulta Voto 2º Grau
		PendenciaDt pendenciaDtVoto = null;
		List listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(id_Processo, id_ServentiaCargo, String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), idAudienciaProcessoSessaoSegundoGrau, obFabricaConexao);
		if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) {
			pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
		} else if (!isRelatorVotoVencido) {
			listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(id_Processo, null, String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), idAudienciaProcessoSessaoSegundoGrau, obFabricaConexao);
			if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) {
				pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
			} 
		}
				
		if (pendenciaDtVoto == null){
			pendenciaDtVoto = criarNovaPendenciaVoto(id_ServentiaCargo, id_Movimentacao, id_Processo, id_ProcessoPrioridade, serventiaCargoAssistente, isRelatorVotoVencido, usuarioDt, obFabricaConexao);			
		} else {
			if(pendenciaDtVoto.isIndicadorVotoVencido() && !isRelatorVotoVencido) {
				pendenciaDtVoto.setCodigoTemp("");
				pendenciaNe.AlterarCodigoTempPendencia(pendenciaDtVoto, obFabricaConexao);
			} else if(!pendenciaDtVoto.isIndicadorVotoVencido() && isRelatorVotoVencido) {
				pendenciaDtVoto.setCodigoTemp(String.valueOf(PendenciaDt.VOTO_VENCIDO_RELATOR));
				pendenciaNe.AlterarCodigoTempPendencia(pendenciaDtVoto, obFabricaConexao);
			}
		} 
			
		return pendenciaDtVoto;
	}
	
	private PendenciaDt criarNovaPendenciaVoto(String id_ServentiaCargo, 
									            String id_Movimentacao, 
									            String id_Processo,
									            String id_ProcessoPrioridade, 
									            ServentiaCargoDt serventiaCargoAssistente,
									            boolean isRelatorVotoVencido, 
									            UsuarioDt usuarioDt,
									            FabricaConexao obFabricaConexao) throws Exception, MensagemException {
		return criarNovaPendenciaVotoEmenta(id_ServentiaCargo, id_Movimentacao, id_Processo, id_ProcessoPrioridade, serventiaCargoAssistente, true, isRelatorVotoVencido, usuarioDt, obFabricaConexao);
	}
	
	private PendenciaDt criarNovaPendenciaEmenta(String id_ServentiaCargo, 
                                                 String id_Movimentacao, 
									             String id_Processo,
									             String id_ProcessoPrioridade, 
									             ServentiaCargoDt serventiaCargoAssistente,
									             UsuarioDt usuarioDt,
									             FabricaConexao obFabricaConexao) throws Exception, MensagemException {
		return criarNovaPendenciaVotoEmenta(id_ServentiaCargo, id_Movimentacao, id_Processo, id_ProcessoPrioridade, serventiaCargoAssistente, false, false, usuarioDt, obFabricaConexao);
	}

	private PendenciaDt criarNovaPendenciaVotoEmenta(String id_ServentiaCargo, 
			                                         String id_Movimentacao, 
			                                         String id_Processo,
			                                         String id_ProcessoPrioridade, 
			                                         ServentiaCargoDt serventiaCargoAssistente,
			                                         boolean isVoto,
			                                         boolean isRelatorVotoVencido, 
			                                         UsuarioDt usuarioDt,
			                                         FabricaConexao obFabricaConexao) throws Exception, MensagemException {
		
		PendenciaNe pendenciaNe = new PendenciaNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe(); 
        ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		
		PendenciaDt pendenciaDtVoto = new PendenciaDt();
		
		// Configura o cadastrador
		pendenciaDtVoto.setId_UsuarioCadastrador(usuarioDt.getId_UsuarioServentia());
		pendenciaDtVoto.setId_UsuarioFinalizador("null");
		// Configura outras informações
		pendenciaDtVoto.setPendenciaTipoCodigo((isVoto ? String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO) :  String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA)));
		pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_EM_ANDAMENTO));
		pendenciaDtVoto.setId_Movimentacao(id_Movimentacao);
		pendenciaDtVoto.setId_Processo(id_Processo);
		pendenciaDtVoto.setId_ProcessoPrioridade(id_ProcessoPrioridade);
		pendenciaDtVoto.setDataVisto(Funcoes.DataHora(new Date()));
		pendenciaDtVoto.setId_UsuarioLog(usuarioDt.getId());
		pendenciaDtVoto.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		
		if (isVoto && isRelatorVotoVencido) pendenciaDtVoto.setCodigoTemp(String.valueOf(PendenciaDt.VOTO_VENCIDO_RELATOR));
		
		ServentiaCargoDt serventiaCargoResponsavel = serventiaCargoNe.consultarId(id_ServentiaCargo);
		
		PendenciaResponsavelDt responsavel = null;
		
		if (usuarioDt.isUPJSegundoGrau() || usuarioDt.isGabineteUPJ()) {
			// Configura o distribuidor do gabinete como responsável
			responsavel = new PendenciaResponsavelDt();
			if (serventiaCargoAssistente != null) {
				responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());
			} else {
				ServentiaCargoDt serventiaCargoDistribuidor = serventiaCargoNe.getDistribuidorGabinete(serventiaCargoResponsavel.getId_Serventia(), obFabricaConexao);
				if(serventiaCargoDistribuidor == null) throw new MensagemException("Não foi possível identificar o Cargo de Distribuidor do Gabinete da serventia " + serventiaCargoResponsavel.getId_Serventia() + ".");
				responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
			}
			pendenciaDtVoto.addResponsavel(responsavel);
		} else {
			// Configura o desembargador como responsável
			responsavel = new PendenciaResponsavelDt();
			responsavel.setId_ServentiaCargo(id_ServentiaCargo);
			// Adiciona o desembargador
			pendenciaDtVoto.addResponsavel(responsavel);
			
			responsavel = new PendenciaResponsavelDt();
			// Tenta obter um assistente relacionado ao processo, caso o assistente não tenha sido informado...
			if (serventiaCargoAssistente == null)
				serventiaCargoAssistente = processoResponsavelNe.getAssistenteGabineteResponsavelProcesso(id_Processo, serventiaCargoResponsavel.getId_Serventia(), obFabricaConexao);
			
		    if (serventiaCargoAssistente != null) 
		    	responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
		    else {
		    	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como responsável pela pendência
		    	ServentiaCargoDt serventiaCargoDistribuidor = serventiaCargoNe.getDistribuidorGabinete(serventiaCargoResponsavel.getId_Serventia(), obFabricaConexao);
		    	if(serventiaCargoDistribuidor == null) throw new MensagemException("Não foi possível identificar o Cargo de Distribuidor do Gabinete da serventia " + serventiaCargoResponsavel.getId_Serventia() + ".");
		    	responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());	
		    }
		    pendenciaDtVoto.addResponsavel(responsavel); 
		}   	
		
		pendenciaNe.gerarPendencia(pendenciaDtVoto, pendenciaDtVoto.getResponsaveis(), null, pendenciaDtVoto.getListaArquivos(), false, false, obFabricaConexao);
		return pendenciaDtVoto;
	}
	
	
	
	
	/**
	 * Salva movimentação de sessões de 2º grau essa movimentação refere-se somente a um processo da sessão. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimentação correspondente e pendências de acordo com o que foi selecionado pelo usuário
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimentação de audiência a serem persistidos
	 * @param usuarioDt, usuário que está realizando a movimentação
	 * 
	 * @author mmgomes
	 */
	private void salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(AudienciaMovimentacaoDt audienciaMovimentacaoDt, 
			                                                                                           UsuarioDt usuarioDt, 
			                                                                                           LogDt logDt, 
			                                                                                           FabricaConexao obFabricaConexao) throws Exception {
		AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		
		// Voto
		String id_PendenciaVoto = "";
		if (audienciaMovimentacaoDt.getPendenciaArquivoDt() != null) {
			id_PendenciaVoto = audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia();
		}
		String textoEditorVoto = audienciaMovimentacaoDt.getTextoEditor();
		String nomeArquivoVoto = audienciaMovimentacaoDt.getNomeArquivo();
		String id_ArquivoTipoVoto = audienciaMovimentacaoDt.getId_ArquivoTipo();
		String arquivoTipoVoto = audienciaMovimentacaoDt.getArquivoTipo();
		
		String julgadoMeritoProcessoPrincipalVoto = audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal();
		List listaPendenciasGerarVoto = audienciaMovimentacaoDt.getListaPendenciasGerar();
		String id_ClassificadorVoto = audienciaMovimentacaoDt.getId_Classificador();
		boolean pendenteAssinatura = audienciaMovimentacaoDt.isPendenteAssinatura();
		
		// Ementa
		String id_PendenciaEmenta = "";
		if (audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta() != null){
			id_PendenciaEmenta = audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId_Pendencia();
		}
		String textoEditorEmenta = audienciaMovimentacaoDt.getTextoEditorEmenta();
		String nomeArquivoEmentaEditor = audienciaMovimentacaoDt.getNomeArquivoEmenta();
		String id_ArquivoTipoEmenta = audienciaMovimentacaoDt.getId_ArquivoTipoEmenta();
		String arquivoTipoEmenta = audienciaMovimentacaoDt.getArquivoTipoEmenta();
						
		String id_pendenciaEmentaGerada = salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(audienciaProcessoDt, 
				                                                                              id_PendenciaVoto, 
				                                                                              textoEditorVoto, 
				                                                                              nomeArquivoVoto, 
				                                                                              id_ArquivoTipoVoto,
				                                                                              arquivoTipoVoto, 
				                                                                              julgadoMeritoProcessoPrincipalVoto, 
				                                                                              listaPendenciasGerarVoto, 
				                                                                              id_ClassificadorVoto,
				                                                                              pendenteAssinatura, 
				                                                                              id_PendenciaEmenta, 
				                                                                              textoEditorEmenta, 
				                                                                              nomeArquivoEmentaEditor,
				                                                                              id_ArquivoTipoEmenta, 
				                                                                              arquivoTipoEmenta, 
				                                                                              usuarioDt, 
				                                                                              obFabricaConexao);
		
		audienciaMovimentacaoDt.setId_PendenciaEmentaGerada(id_pendenciaEmentaGerada);
		// é relator...
		if (Funcoes.StringToLong(id_pendenciaEmentaGerada) == Funcoes.StringToLong(audienciaProcessoDt.getId_PendenciaEmentaRelator())) {
			audienciaMovimentacaoDt.setId_PendenciaVotoGerada(audienciaProcessoDt.getId_PendenciaVotoRelator());
			audienciaMovimentacaoDt.setId_ServentiaCargoVotoEmentaGerada(audienciaProcessoDt.getId_ServentiaCargo());
		} else {
			audienciaMovimentacaoDt.setId_PendenciaVotoGerada(audienciaProcessoDt.getId_PendenciaVotoRedator());
			audienciaMovimentacaoDt.setId_ServentiaCargoVotoEmentaGerada(audienciaProcessoDt.getId_ServentiaCargoRedator());
		}
	}

	public String salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(AudienciaProcessoDt audienciaProcessoDt, 
																									   String id_PendenciaVoto,
																									   String textoEditorVoto, 
																									   String nomeArquivoVoto, 
																									   String id_ArquivoTipoVoto, 
																									   String arquivoTipoVoto,
																									   String julgadoMeritoProcessoPrincipalVoto, 
																									   List listaPendenciasGerarVoto, 
																									   String id_ClassificadorVoto,
																									   boolean pendenteAssinatura, 
																									   String id_PendenciaEmenta, 
																									   String textoEditorEmenta,
																									   String nomeArquivoEmentaEditor,
																									   String id_ArquivoTipoEmenta, 
																									   String arquivoTipoEmenta,
																									   UsuarioDt usuarioDt, 
			                                                                                           FabricaConexao obFabricaConexao) throws Exception, MensagemException {
		
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		ProcessoNe processoNe = new ProcessoNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaProcessoDt.getProcessoDt().getId());
		audienciaProcessoDt.setProcessoDt(processoDt);
		
		String Id_ServentiaCargo = usuarioDt.getId_ServentiaCargo();
		ServentiaCargoDt serventiaCargoDtAssistente = null;
		
		boolean isRelatorSessao = true;
		if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU ||
			Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU) {
			isRelatorSessao = audienciaProcessoDt.getId_ServentiaCargo().equalsIgnoreCase(Id_ServentiaCargo);
		} else if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_DESEMBARGADOR || 
				   Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA) {
			Id_ServentiaCargo = usuarioDt.getId_ServentiaCargoUsuarioChefe();
		} else if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE || 
				   Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO) {
			serventiaCargoDtAssistente = serventiaCargoNe.consultarId(usuarioDt.getId_ServentiaCargo(), obFabricaConexao);
			if (id_PendenciaVoto != null && id_PendenciaVoto.trim().length() > 0) {
				isRelatorSessao = !(Funcoes.StringToLong(id_PendenciaVoto) > 0 && 
						            Funcoes.StringToLong(id_PendenciaVoto) == Funcoes.StringToLong(audienciaProcessoDt.getId_PendenciaVotoRedator()));
			} else if (audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) {
				ServentiaCargoDt serventiaCargoRedatorDt = serventiaCargoNe.consultarId(audienciaProcessoDt.getId_ServentiaCargoRedator(), obFabricaConexao);
				isRelatorSessao = !(serventiaCargoRedatorDt != null && serventiaCargoRedatorDt.getId_Serventia() != null && 
						            serventiaCargoDtAssistente != null && serventiaCargoDtAssistente.getId_Serventia() != null &&
						            serventiaCargoRedatorDt.getId_Serventia().trim().equalsIgnoreCase(serventiaCargoDtAssistente.getId_Serventia().trim()));
			}			
			if (isRelatorSessao) {
				Id_ServentiaCargo = audienciaProcessoDt.getId_ServentiaCargo();
			} else {
				Id_ServentiaCargo = audienciaProcessoDt.getId_ServentiaCargoRedator();
			}
		} else {
			throw new MensagemException("Fluxo não mapeado para o perfil do usuário logado.");
		}
		
		//Consulta Voto 2º Grau
		PendenciaDt pendenciaDtVoto = null;
		if (id_PendenciaVoto != null && id_PendenciaVoto.trim().length() > 0){
			pendenciaDtVoto = pendenciaNe.consultarId(id_PendenciaVoto);
		} else if (isRelatorSessao && audienciaProcessoDt.possuiVotoRelator()) {
			pendenciaDtVoto = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaVotoRelator());
		} else if (!isRelatorSessao && audienciaProcessoDt.possuiVotoRedator()) {
			pendenciaDtVoto = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaVotoRedator());
		} else {
			List listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), Id_ServentiaCargo, String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), audienciaProcessoDt.getId(), obFabricaConexao);
			if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
		}
		
		if (pendenciaDtVoto != null && pendenciaDtVoto.temDataFim()) {
			pendenciaNe.moverPendenciaParaPendFinal(pendenciaDtVoto, obFabricaConexao);
			pendenciaDtVoto = null;
		}
		
		//Se não existir uma pendência do tipo Voto 2º Grau, iremos criá-la, e consultá-la
		if (pendenciaDtVoto == null) {
			if (isRelatorSessao) {
				pendenciaDtVoto = gerarPendenciaVotoRelatorSessao(Id_ServentiaCargo, 
						                                          null, 
						                                          processoDt.getId(), 
						                                          processoDt.getId_ProcessoPrioridade(), 
						                                          serventiaCargoDtAssistente, 
						                                          audienciaProcessoDt.getId(), 
						                                          false, 
						                                          usuarioDt, 
						                                          obFabricaConexao);
				
				audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaDtVoto.getId());
			} else {
				pendenciaDtVoto = gerarPendenciaVotoRedatorSessao(Id_ServentiaCargo, 
											                      null, 
											                      processoDt.getId(), 
											                      processoDt.getId_ProcessoPrioridade(), 
											                      serventiaCargoDtAssistente, 
											                      audienciaProcessoDt.getId(), 
											                      usuarioDt, 
											                      obFabricaConexao);

				audienciaProcessoDt.setId_PendenciaVotoRedator(pendenciaDtVoto.getId());
			}
		} 
		
		//Obtem a conclusão do tipo Voto
		AnaliseConclusaoDt analiseConclusaoDtVoto = pendenciaArquivoNe.getPreAnaliseConclusao(pendenciaDtVoto.getId(), obFabricaConexao);
		
		if (analiseConclusaoDtVoto == null) analiseConclusaoDtVoto = new AnaliseConclusaoDt();
			
		analiseConclusaoDtVoto.addPendenciasFechar(pendenciaDtVoto);
		analiseConclusaoDtVoto.setId_TipoPendencia(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO));
		analiseConclusaoDtVoto.setTextoEditor(textoEditorVoto);
		analiseConclusaoDtVoto.setNomeArquivo(nomeArquivoVoto);
		analiseConclusaoDtVoto.setId_ArquivoTipo(id_ArquivoTipoVoto);
		analiseConclusaoDtVoto.setArquivoTipo(arquivoTipoVoto);	
		analiseConclusaoDtVoto.setId_UsuarioLog(usuarioDt.getId());
		analiseConclusaoDtVoto.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		analiseConclusaoDtVoto.setJulgadoMeritoProcessoPrincipal(julgadoMeritoProcessoPrincipalVoto);
		
		analiseConclusaoDtVoto.setListaPendenciasGerar(listaPendenciasGerarVoto);
		
		if (id_ClassificadorVoto == null || id_ClassificadorVoto.trim().length() == 0){
			analiseConclusaoDtVoto.setId_Classificador("null");
			analiseConclusaoDtVoto.setClassificador("null");
		} else {
			analiseConclusaoDtVoto.setId_Classificador(id_ClassificadorVoto);
			//analiseConclusaoDtVoto.setClassificador(audienciaMovimentacaoDt.getClassificador());
		}	
		
		//Guarda para assinar, se for o caso..
		analiseConclusaoDtVoto.setPendenteAssinatura(pendenteAssinatura);
		
		//Salva a conclusão do tipo Voto
		pendenciaArquivoNe.salvarPreAnaliseConclusao(analiseConclusaoDtVoto, usuarioDt, obFabricaConexao);		
				
		//Consulta Ementa 2º Grau
		PendenciaDt pendenciaDtEmenta = null;
		if (id_PendenciaEmenta != null && id_PendenciaEmenta.trim().length() > 0){
			pendenciaDtEmenta = pendenciaNe.consultarId(id_PendenciaEmenta);
		} else if (isRelatorSessao && audienciaProcessoDt.possuiEmentaRelator()) {
			pendenciaDtEmenta = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaEmentaRelator());
		} else if (!isRelatorSessao && audienciaProcessoDt.possuiEmentaRedator()) {
			pendenciaDtEmenta = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaEmentaRedator());
		} else {
			List listaDePendenciasEmenta = pendenciaNe.consultarPendenciasVotoEmentaProcesso(processoDt.getId(), Id_ServentiaCargo, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), audienciaProcessoDt.getId(), obFabricaConexao);
			if (listaDePendenciasEmenta != null && listaDePendenciasEmenta.size() > 0) pendenciaDtEmenta = (PendenciaDt) listaDePendenciasEmenta.get(0);
		}
		
		if (pendenciaDtEmenta != null && pendenciaDtEmenta.temDataFim()) {
			pendenciaNe.moverPendenciaParaPendFinal(pendenciaDtEmenta, obFabricaConexao);
			pendenciaDtEmenta = null;
		}
		
		//Se não existir uma pendência do tipo Ementa 2º Grau, iremos criá-la
		if (pendenciaDtEmenta == null) {
			if (isRelatorSessao) {
				pendenciaDtEmenta = gerarPendenciaEmentaRelatorSessao(Id_ServentiaCargo, 
												                      null, 
												                      processoDt.getId(), 
												                      processoDt.getId_ProcessoPrioridade(), 
												                      serventiaCargoDtAssistente, 
												                      audienciaProcessoDt.getId(), 
												                      usuarioDt, 
												                      obFabricaConexao);
				
				audienciaProcessoDt.setId_PendenciaEmentaRelator(pendenciaDtEmenta.getId());
			} else {
				pendenciaDtEmenta = gerarPendenciaEmentaRedatorSessao(Id_ServentiaCargo, 
											                          null, 
											                          processoDt.getId(), 
											                          processoDt.getId_ProcessoPrioridade(), 
											                          serventiaCargoDtAssistente, 
											                          audienciaProcessoDt.getId(), 
											                          usuarioDt, 
											                          obFabricaConexao);

				audienciaProcessoDt.setId_PendenciaEmentaRedator(pendenciaDtEmenta.getId());
			}
			audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
		}		
		
		//Obtem a conclusão do tipo Ementa
		AnaliseConclusaoDt analiseConclusaoDtEmenta = pendenciaArquivoNe.getPreAnaliseConclusao(pendenciaDtEmenta.getId(), obFabricaConexao);
		if (analiseConclusaoDtEmenta == null) analiseConclusaoDtEmenta = new AnaliseConclusaoDt();
		
		analiseConclusaoDtEmenta.addPendenciasFechar(pendenciaDtEmenta);
		analiseConclusaoDtEmenta.setId_TipoPendencia(String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA));		
		analiseConclusaoDtEmenta.setTextoEditor(textoEditorEmenta);		
		analiseConclusaoDtEmenta.setNomeArquivo(nomeArquivoEmentaEditor);
		analiseConclusaoDtEmenta.setId_ArquivoTipo(id_ArquivoTipoEmenta);
		analiseConclusaoDtEmenta.setArquivoTipo(arquivoTipoEmenta);
		analiseConclusaoDtEmenta.setId_UsuarioLog(usuarioDt.getId());
		analiseConclusaoDtEmenta.setIpComputadorLog(usuarioDt.getIpComputadorLog());
				
		//Salva a conclusão do tipo Ementa
		pendenciaArquivoNe.salvarPreAnaliseConclusao(analiseConclusaoDtEmenta, usuarioDt, obFabricaConexao);
		
		//Atualiza as pendências na sessão, caso tenha gerado uma pendência filha...
		if (analiseConclusaoDtVoto.getPrimeiraPendenciaGerada() != null || analiseConclusaoDtEmenta.getPrimeiraPendenciaGerada() != null) {
			
			if (analiseConclusaoDtVoto.getPrimeiraPendenciaGerada() != null) {
				if (isRelatorSessao) {
					audienciaProcessoDt.setId_PendenciaVotoRelator(analiseConclusaoDtVoto.getPrimeiraPendenciaGerada().getId());
				} else {
					audienciaProcessoDt.setId_PendenciaVotoRedator(analiseConclusaoDtVoto.getPrimeiraPendenciaGerada().getId());
				}
			}				
			
			if (analiseConclusaoDtEmenta.getPrimeiraPendenciaGerada() != null) {
				if (isRelatorSessao) {
					audienciaProcessoDt.setId_PendenciaEmentaRelator(analiseConclusaoDtEmenta.getPrimeiraPendenciaGerada().getId());
				} else {
					audienciaProcessoDt.setId_PendenciaEmentaRedator(analiseConclusaoDtEmenta.getPrimeiraPendenciaGerada().getId());
				}
			}				
		}
		
		audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
		
		if (isRelatorSessao) {
			return audienciaProcessoDt.getId_PendenciaEmentaRelator();
		} else {
			return audienciaProcessoDt.getId_PendenciaEmentaRedator();
		}
	}
	
	/**
	 * Executa o Método consultar id da classe ArquivoNe
	 * 
	 * @param idArquivo, objeto audienciaMovimentacao 
	 * 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public ArquivoDt consultarArquivo(String idArquivo) throws Exception{
		ArquivoNe arquivoNe = null;
		ArquivoDt arquivoDt = null;
		
		arquivoNe = new ArquivoNe();
		arquivoDt = arquivoNe.consultarId(idArquivo);
				
		arquivoNe = null;
		return arquivoDt;
	}
	
	/**
	 * Executa o Método baixar arquivo da classe ArquivoNe, inicialmente será utilizado apenas para baixar o arquivo de Ata pelo desembargador ou pelo seu assistente
	 * 
	 * @param idArquivo, objeto audienciaMovimentacao	  
	 * @param response, objeto response do request
	 * @param idUsuario, identificador do usuário logado
	 * @param ipComputadorLog, ip do computador do usuário logado
	 * 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public void baixarArquivo(String idArquivo, HttpServletResponse response, String idUsuario, String ipComputadorLog) throws Exception{									
		LogDt logDt = new LogDt(idUsuario, ipComputadorLog);
		ArquivoNe arquivoNe = new ArquivoNe();
		
		arquivoNe = new ArquivoNe();
		arquivoNe.baixarArquivo(idArquivo, response, logDt, false);
				
		arquivoNe = null;
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo ementa em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public PendenciaArquivoDt consultarEmentaDesembargador(String id_ServentiaCargo, AudienciaProcessoDt audienciaProcessoDt, String id_ProcessoTipoSessao) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		
		pendenciaArquivoDt = pendenciaArquivoNe.consultarEmentaDesembargador(id_ServentiaCargo, audienciaProcessoDt, id_ProcessoTipoSessao);
				
		pendenciaArquivoNe = null;
		return pendenciaArquivoDt;
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo ementa em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param String id_Pendencia
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public PendenciaArquivoDt consultarEmentaDesembargadorEmentaPendencia(String id_ServentiaCargo, String idAudiProc, String id_Pendencia) throws Exception{
		PendenciaArquivoDt pendenciaArquivoDt = null;
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		
		pendenciaArquivoDt = pendenciaArquivoNe.consultarEmentaDesembargadorPendencia(id_ServentiaCargo, idAudiProc, id_Pendencia);
				
		pendenciaArquivoNe = null;
		return pendenciaArquivoDt;
	}
	
	public PendenciaArquivoDt consultarEmentaDesembargadorEmentaPendencia(String id_ServentiaCargo, String idAudiProc, String id_Pendencia, FabricaConexao obFabricaConexao) throws Exception{
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		return pendenciaArquivoNe.consultarEmentaDesembargadorPendencia(id_ServentiaCargo, idAudiProc, id_Pendencia, obFabricaConexao);
	}
	
	/**
	 * Método responsável em efetuar ações comuns em uma movimentação de audiência, valendo também para sessões de 2º grau. Chama método para
	 * Atualizar Dados em "Audiencia", "AudienciaProcesso", gera movimentação correspondente a movimentação da audiência e insere responsáveis pela
	 * audiência
	 * 
	 * @param audienciaDt
	 * @param audienciaStatusCodigo
	 * @param usuarioDt
	 * @param logDt
	 * @param conexao
	 * @return
	 * @throws Exception
	 */
	public MovimentacaoDt movimentarAudienciaAnalistaSegundoGrau(AudienciaDt audienciaDt, 
			                                                     String audienciaStatusCodigo, 
			                                                     String id_NovaAudiencia, 
			                                                     String novaData, 
			                                                     String audienciaProcessoStatusAnalista, 
			                                                     String audienciaProcessoStatusCodigoAnalista, 
			                                                     String idArquivoAta, 
			                                                     String Id_ServentiaCargoPresidente, 
			                                                     String Id_ServentiaCargoMP, 
			                                                     String Id_ServentiaCargoRedator, 
			                                                     UsuarioDt usuarioDt, 
			                                                     LogDt logDt, 
			                                                     FabricaConexao conexao) throws Exception{
		MovimentacaoDt movimentacaoAudiencia = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();

		// Atualiza dados em "Audiencia"
		String dataMovimentacao = Funcoes.DataHora(new Date());
		this.alterarAudienciaMovimentacao(audienciaDt, dataMovimentacao, logDt, conexao);
		
		int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
		codigoStatus = Funcoes.StringToInt(audienciaStatusCodigo);
		
		// Atualiza dados em "AudienciaProcesso"
		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		
		boolean houveAlteracaoExtratoAta = false;
		if (audienciaProcessoDt.getId_ArquivoAta() != null && audienciaProcessoDt.getId_ArquivoAta().trim().length() > 0 && idArquivoAta != null && idArquivoAta.trim().length() > 0 && (!audienciaProcessoDt.getId_ArquivoAta().trim().equalsIgnoreCase(id_NovaAudiencia.trim()))){
			houveAlteracaoExtratoAta = true;
		}
		
		audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoDt, audienciaStatusCodigo, audienciaProcessoStatusAnalista, audienciaProcessoStatusCodigoAnalista, idArquivoAta, false, false, Id_ServentiaCargoPresidente,  Id_ServentiaCargoMP, Id_ServentiaCargoRedator,  logDt, conexao);
		
		// Dependendo do tipo da Audiência chama método para gerar Movimentação correspondente
		movimentacaoAudiencia = this.movimentarSessao(audienciaDt, codigoStatus, id_NovaAudiencia, novaData, usuarioDt, true, houveAlteracaoExtratoAta, logDt, conexao);
		
		return movimentacaoAudiencia;
	}
	
	/***
	 * Método responsável por consultar as Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
	 * 
	 * @param usuarioDt
	 * @param id_Audiencia
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public List consultarSessoesPendentesPendentesAcordao(UsuarioDt usuarioDt, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {
		List listaSessoesPendentes = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {

				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorPendentesAcordaoDesembargador(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorPendentesAcordaoJuizTurma(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura);
					break;
					
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesAssistenteGabinetePendentesAcordao(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;				

				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorPendentesAcordaoDesembargador(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:	
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorPendentesAcordaoJuizTurma(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePendentesAssinatura);
					break;
			}
			
			if (listaSessoesPendentes != null)
			{
				for (int i = 0; i < listaSessoesPendentes.size(); i++)
				{
					AudienciaDt audienciaDt = ((AudienciaDt) listaSessoesPendentes.get(i));
					
					for (int j = 0; j < audienciaDt.getListaAudienciaProcessoDt().size(); j++)
					{
						AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) audienciaDt.getListaAudienciaProcessoDt().get(j);
						List listaPromotores = processoResponsavelNe.getListaPromotoresResponsavelProcesso(audienciaProcessoDt.getProcessoDt().getId(), null , obFabricaConexao);
						if(listaPromotores != null && listaPromotores.size() > 0){
							ServentiaCargoDt promotor = (ServentiaCargoDt) listaPromotores.get(0);
							audienciaProcessoDt.setId_ServentiaCargoMP(promotor.getId());
							audienciaProcessoDt.setServentiaCargoMP(promotor.getServentiaCargo());
							audienciaProcessoDt.setNomeMPProcesso(promotor.getNomeUsuario());						
						}	
					}
				}
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesPendentes;
	}	
	
	/***
	 * Método responsável por consultar as Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
	 * 
	 * @param usuarioDt
	 * @param id_Audiencia
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 * @author mmgomes - jvosantos
	 */
	public List consultarSessoesVirtuaisPendentesPendentesAcordao(UsuarioDt usuarioDt, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {
		List listaSessoesPendentes = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		 
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {

				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesVirtuaisPendentesRelatorPendentesAcordaoDesembargador(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.JUIZ_TURMA:
//					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
//						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorPendentesAcordaoJuizTurma(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura);
					break;
					
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
//					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0)
//						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesAssistenteGabinetePendentesAcordao(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;				

				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0)
						listaSessoesPendentes = obPersistencia.consultarSessoesVirtuaisPendentesRelatorPendentesAcordaoDesembargador(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePendentesAssinatura, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:	
//					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0)
//						listaSessoesPendentes = obPersistencia.consultarSessoesPendentesRelatorPendentesAcordaoJuizTurma(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePendentesAssinatura);
					break;
			}
			
			if (listaSessoesPendentes != null)
			{
				AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
				for (int i = 0; i < listaSessoesPendentes.size(); i++)
				{
					AudienciaDt audienciaDt = ((AudienciaDt) listaSessoesPendentes.get(i));
					
					for (int j = 0; j < audienciaDt.getListaAudienciaProcessoDt().size(); j++)
					{
						AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) audienciaDt.getListaAudienciaProcessoDt().get(j);
						Optional<AudienciaProcessoStatusDt> audienciaProcessoStatusDt = Optional.ofNullable(audienciaProcessoNe.consultarStatusAudienciaTemp(audienciaProcessoDt.getId()));

						if(audienciaProcessoStatusDt.isPresent()) {
							audienciaProcessoDt.setAudienciaProcessoStatusTemp(audienciaProcessoStatusDt.get().getAudienciaProcessoStatus());
							audienciaProcessoDt.setAudienciaProcessoStatusCodigoTemp(audienciaProcessoStatusDt.get().getAudienciaProcessoStatusCodigo());
						}

						List listaPromotores = processoResponsavelNe.getListaPromotoresResponsavelProcesso(audienciaProcessoDt.getProcessoDt().getId(), null , obFabricaConexao);
						if(listaPromotores != null && listaPromotores.size() > 0){
							ServentiaCargoDt promotor = (ServentiaCargoDt) listaPromotores.get(0);
							audienciaProcessoDt.setId_ServentiaCargoMP(promotor.getId());
							audienciaProcessoDt.setServentiaCargoMP(promotor.getServentiaCargo());
							audienciaProcessoDt.setNomeMPProcesso(promotor.getNomeUsuario());						
						}	
					}
				}
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesPendentes;
	}	

	/***
	 * Método responsável por consultar a quantidade de Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
	 * 
	 * @param usuarioDt
	 * @param id_Audiencia
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public long consultarQuantidadeSessoesPendentes(UsuarioDt usuarioDt, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {
		long quantidade = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {

				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:				
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0) 
						quantidade = obPersistencia.consultarQuantidadeSessoesPendentesPendentesAcordao(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0) 
						quantidade = obPersistencia.consultarQuantidadeSessoesPendentesPendentesAcordaoAssistenteGabinete(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas);
					break;

				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:	
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0) 
						quantidade = obPersistencia.consultarQuantidadeSessoesPendentesPendentesAcordao(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return quantidade;
	}
	
	/***
	 * Método responsável por consultar a quantidade de Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
	 * 
	 * @param usuarioDt
	 * @param id_Audiencia
	 * @param posicaoPaginaAtual
	 * @return
	 * @throws Exception
	 * @author mmgomes - jvosantos
	 */
	public long consultarQuantidadeSessoesVirtuaisPendentes(UsuarioDt usuarioDt, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {
		long quantidade = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {

				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:				
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0) 
						quantidade = obPersistencia.consultarQuantidadeSessoesVirtuaisPendentesPendentesAcordao(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas);
					break;
					
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().trim().length() > 0) 
						quantidade = obPersistencia.consultarQuantidadeSessoesVirtuaisPendentesPendentesAcordaoAssistenteGabinete(usuarioDt.getId_ServentiaCargo(), id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas);
					break;

				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:	
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
					if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && usuarioDt.getId_ServentiaCargoUsuarioChefe().trim().length() > 0) 
						quantidade = obPersistencia.consultarQuantidadeSessoesVirtuaisPendentesPendentesAcordao(usuarioDt.getId_ServentiaCargoUsuarioChefe(), id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return quantidade;
	}
	/**
	 * Consulta um arquivo tipo Extrato de Ata de sessão.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoExtratoAtaSessao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.EXTRATO_ATA_SESSAO);      
				
	}
	
	/**
	 * Consulta um arquivo tipo Voto de sessão.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoVotoSessao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.RELATORIO_VOTO);      
				
	}
	
	/**
	 * Consulta um arquivo tipo Ementa de sessão.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoEmentaSessao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.EMENTA);      
		
	}
	
	/**
	 * Consulta um arquivo tipo Extrato de Ata de sessão.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoCertidao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.CERTIDAO);      
				
	}
	
	/**
	 * Consulta um arquivo tipo
	 * 
	 * @param arquivoTipoCodigo
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private ArquivoTipoDt consultarArquivoTipoCodigo(int arquivoTipoCodigo) throws Exception{
		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
		
    	String id = "";
    	ArquivoTipoDt arquivoTipoDt = null;
    		
    	List ids = arquivoTipoNe.consultarPeloArquivoTipoCodigo(String.valueOf(arquivoTipoCodigo));
    	if (ids!=null && ids.size() > 0) id = (String) ids.get(0);
   	
    	if (id != "") arquivoTipoDt = arquivoTipoNe.consultarId(id);
    	
    	arquivoTipoNe = null;

        return arquivoTipoDt;	        
			
	}
	
	/**
	 * Médodo responsável em agendar uma audiência do tipo Sessão do 2º grau para um processo passado. Chama método para vincular o processo a
	 * audiência e gera a movimentação de inclusão em pauta
	 * 
	 * @param processoDt
	 *            , processo a ser vinculado com a sessão
	 * @param id_Sessao
	 *            , audiência a ser vinculada com processo
	 * @param dataAgendada
	 *            , data da nova audiência a ser marcada
	 * @param id_ProcessoTipo
	 *            , id da classe (processoTipo) da nova audiência a ser marcada
	 * @param processoTipo
	 *            , processo tipo da nova audiência a ser marcada
	 * @param id_UsuarioServentia
	 *            , usuário que está marcando a sessão
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author mmgomes
	 */
	public AudienciaProcessoDt marcarSessaoEmMesaParaJulgamento(ProcessoDt processoDt, String id_Sessao, String dataAgendada, String id_ProcessoTipo, String processoTipo, String id_UsuarioServentia, LogDt logDt, FabricaConexao conexao, UsuarioDt usuarioDt, String id_ServentiaCargoResponsavel) throws Exception {
		
		// Chama metodo para vincular o processo a audiencia
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		return audienciaProcessoNe.marcarSessaoProcesso(id_Sessao, dataAgendada, id_ProcessoTipo, processoTipo, processoDt, id_UsuarioServentia, logDt, conexao, true, usuarioDt, id_ServentiaCargoResponsavel);

	}
	
	/**
	 * Método responsável em realizar a consulta de uma audiencia processo status através do tipo da serventia e do código.
	 * 
	 * @param serventiaTipoCodigo
	 * @param AudienciaProcessoStatusCodigo
	 * @return
	 * 
	 * @author mmgomes
	 * @throws Exception
	 */
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusCodigoMovimentacao(String serventiaTipoCodigo, String AudienciaProcessoStatusCodigo) throws Exception{
		AudienciaProcessoStatusNe audienciaProcessoStatusNe; 
		AudienciaProcessoStatusDt audienciaProcessoStatusDt;
		
		audienciaProcessoStatusNe = new AudienciaProcessoStatusNe();
		
		audienciaProcessoStatusDt = audienciaProcessoStatusNe.consultarAudienciaProcessoStatusCodigoMovimentacao(serventiaTipoCodigo, AudienciaProcessoStatusCodigo);
		
		audienciaProcessoStatusNe = null;
		
		return audienciaProcessoStatusDt;
		
	}
	
	/**
	 * Método responsável e consultar a pré-análise de uma conclusão
	 * 
	 * @param id_Pendencia
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public AnaliseConclusaoDt getPreAnaliseConclusao(String id_Pendencia) throws Exception {
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AnaliseConclusaoDt preAnaliseConclusaoDt = null;
		
		preAnaliseConclusaoDt = pendenciaArquivoNe.getPreAnaliseConclusao(id_Pendencia);
		
		pendenciaArquivoNe = null;
		return preAnaliseConclusaoDt;
	}
	
	/**
	 * Consulta os dados de uma audiência processo que ainda não foi movimentado
	 * 
	 * @param id_Processo, identificação do processo
	 * @param usuarioDt, usuário logado
	 * 
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarAudienciaProcessoPendente(String id_Processo, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoDt audienciaprocessoDt = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		audienciaprocessoDt = audienciaProcessoNe.consultarAudienciaProcessoPendente(id_Processo, usuarioDt);			

		// alsqueiroz 01/10/2019 * Instrução desnecessária removida
		return audienciaprocessoDt;
	}	
	
	/**
     * Retorna a serventia relacionada do tipo Promotoria
     * 
     * @param id_Serventia
     * 
     * @author mmgomes
     */
	public ServentiaDt consultarPromotoriaRelacionada(String id_Serventia) throws Exception {       
              	
    	ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
    	return serventiaRelacionadaNe.consultarPromotoriaRelacionada(id_Serventia);        	
		
	}
	
	/**
	 * Retorna as serventias com base na descrição, página atual, Tipo da Serventia e SubTipo da Serventia
	 * @param descricao
	 * @param posicao
	 * @param serventiaTipoCodigo
	 * @param serventiaSubTipoCodigo
	 * 
	 *  @author mmgomes
	 */
	public List consultarServentiasAtivas(String descricao, String posicao, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		List tempList=null;
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		tempList = Serventiane.consultarServentiasAtivas(descricao,  posicao, serventiaTipoCodigo, null, serventiaSubTipoCodigo);
		QuantidadePaginas = Serventiane.getQuantidadePaginas();
		Serventiane = null;
		
		return tempList;
	}
	
	public String consultarServentiasAtivasJSON(String descricao, String posicao, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		
		ServentiaNe ServentiaNe = new ServentiaNe(); 
		stTemp = ServentiaNe.consultarServentiasAtivasJSON(descricao,  posicao, serventiaTipoCodigo, null, serventiaSubTipoCodigo);
		
		return stTemp;
	}
	
	/***
	 * Consulta o presidênte da Câmara
	 * 
	 * @param id_Serventia
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarPresidenteSegundoGrau(String id_Serventia) throws Exception {		
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		ServentiaCargoDt retornoCargoDt = null;

		retornoCargoDt = serventiaCargoNe.getPresidenteSegundoGrau(id_Serventia, null);			

		serventiaCargoNe = null;
		return retornoCargoDt;
	}
	
	/***
	 * Gera o arquivo ODT contendo a listagem dos processos presentes na sessão, pauta do dia
	 * 
	 * @param diretorioProjetos
	 * @param audienciaCompleta
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	public byte[] relAudienciaProcessoPautaDia(String diretorioProjeto, AudienciaDt audienciaCompleta, UsuarioDt usuario) throws Exception{
		return relSessaoSegundoGrau(diretorioProjeto, audienciaCompleta, usuario, null);
	}
	
	/***
	 * Gera o arquivo PDF contendo a listagem dos processos presentes na sessão, adiados
	 * 
	 * @param diretorioProjetos
	 * @param audienciaCompleta
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	public byte[] relAudienciaProcessoAdiados(String diretorioProjeto, AudienciaDt audienciaCompleta, UsuarioDt usuario) throws Exception{
		List<String> tiposDeAudiencia = new ArrayList<String>();
		tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_ADIADO);
		tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_INICIADO);
		return relSessaoSegundoGrau(diretorioProjeto, audienciaCompleta, usuario, tiposDeAudiencia);
	}
	
	/***
	 * Gera o arquivo PDF contendo a listagem dos processos presentes na sessão, adiados
	 * 
	 * @param diretorioProjetos
	 * @param audienciaCompleta
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	
	 public List listarPautaSessao(AudienciaDt audienciaCompleta, String tipoDeAudiencia) throws Exception {
		 List<String> tiposDeAudiencia = new ArrayList<String>();
		 FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		 List <RelatorioAudienciaProcesso> retorno = null;
		 List <RelatorioAudienciaProcesso> retornoBoxAdiados = null;
		 
		 try {
			 tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_ADIADO);
			 tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_INICIADO);
			 AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao());
			 retornoBoxAdiados = obPersistencia.listarPautaSessao(audienciaCompleta.getId(), tiposDeAudiencia);
			 retorno = obPersistencia.listarPautaSessao(audienciaCompleta.getId(), null);
			 
			 for(RelatorioAudienciaProcesso relAudiProc : retornoBoxAdiados) {
				 retorno.add(relAudiProc);
			 }
			 
		 }finally {
			 obFabricaConexao.fecharConexao();
		}
		 return retorno;
	 }
	 
	 
	public byte[] relAudienciaProcessoAdiadosMagistrado(String diretorioProjeto, AudienciaDt audienciaCompleta, UsuarioDt usuario, HtmlPipelineContext hpc) throws Exception{
		List<String> tiposDeAudiencia = new ArrayList<String>();
		FabricaConexao obFabricaConexao = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] retorno = null;
		
		try {
			tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_ADIADO);
			tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_INICIADO);
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao());
			
			Date dataHoraSessao = Funcoes.StringToDateTime(audienciaCompleta.getDataAgendada());
			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm");
			String dataJulgamento = "DATA DO JULGAMENTO " + FormatoData.format(dataHoraSessao) + " AS " + FormatoHora.format(dataHoraSessao) + " HORAS OU NAS SESSÕES POSTERIORES";
			
			List<RelatorioAudienciaProcesso> listaProcessos = (List<RelatorioAudienciaProcesso>) obPersistencia.listarPautaSessao(audienciaCompleta.getId(), tiposDeAudiencia);
			
			Document document = new Document();
			try {
				PdfCopy copy = new PdfCopy(document, bos);
				document.open();
				for (RelatorioAudienciaProcesso relatorio: listaProcessos){
					byte[] temp = gerarPdfAdiadosMagistrado(relatorio, hpc, diretorioProjeto, dataJulgamento);
					if (temp != null) copy = ConcatenatePDF.concatPDFs(copy, temp);				
				}
				document.close();
				bos.flush();
				retorno = bos.toByteArray();
			} catch (Exception e){
				try{if (document!=null) document.close(); } catch(Exception ex ) {};			
				try{if (bos!=null) bos.close(); } catch(Exception ex ) {};
				throw e;
			}			
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	private byte[] gerarPdfAdiadosMagistrado(RelatorioAudienciaProcesso relatorio, HtmlPipelineContext hpc, String diretorioProjeto, String dataJulgamento) throws Exception {
    	
    	byte[] arrTemp = null;
    	
    	if (relatorio.getTextoAtaAdiadoIniciado() != null){
			
    		String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
    		
			byte[] out = relatorio.getTextoAtaAdiadoIniciado();
									
			try {
				out = ConverterHtmlPdf.converteHtmlPDF(out, hpc);
			} catch (Exception e) {
				out = GerarPDF.gerarMensagemPDF("Extrato da Ata da sessão do processo " + relatorio.getProcessoNumero(), "");
			}
			
			arrTemp = escreverTextoPDFAdiadosMagistrado(out, pathImage, relatorio, dataJulgamento);
		}
    	
    	return arrTemp;    	
    }
	
	private byte[] escreverTextoPDFAdiadosMagistrado(byte[] input, String pathImage, RelatorioAudienciaProcesso relatorio, String dataJulgamento)throws Exception {	
		ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp= null;
		try{
			input = GerarCabecalhoProcessoPDF.redimensionar(input);
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader,TextoPDF);
			
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {				
                over = pdfStamper.getOverContent(i);
                over.beginText();
                
                imprimaCabecalhoNaPaginaAdiadosMagistrado(over, reader, bf, relatorio, i, dataJulgamento);
               
                over.endText();
			}
			pdfStamper.close();		
			TextoPDF.close();
			reader.close();
			temp = TextoPDF.toByteArray();
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	private void imprimaCabecalhoNaPaginaAdiadosMagistrado(PdfContentByte over, PdfReader reader, BaseFont bf, RelatorioAudienciaProcesso relatorio, int numeroPagina, String dataJulgamento)
	{
		over.setFontAndSize(bf, 11);
        over.setColorFill(BaseColor.RED);
        over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Processo: "+ relatorio.getProcessoNumero() + " - " + relatorio.getServentia().toUpperCase(), 40, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 20), 0);
        
        over.setFontAndSize(bf, 11);
        over.setColorFill(BaseColor.RED);
        over.showTextAligned(PdfContentByte.ALIGN_LEFT, "JULGAMENTOS ADIADOS", 40, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 32), 0);
        
        over.setFontAndSize(bf, 11);
        over.setColorFill(BaseColor.RED);
        over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataJulgamento, 40, (reader.getPageSize(numeroPagina).getHeight() + reader.getPageSize(numeroPagina).getBottom() - 44), 0);		
	}
	
	/***
	 * Gera o arquivo PDF contendo a listagem dos processos presentes na sessão, Em Mesa Para Julgamento
	 * 
	 * @param diretorioProjetos
	 * @param audienciaCompleta
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	public byte[] relAudienciaProcessoEmMesaParaJulgamento(String diretorioProjeto, AudienciaDt audienciaCompleta, UsuarioDt usuario) throws Exception{
		List<String> tiposDeAudiencia = new ArrayList<String>();
		tiposDeAudiencia.add(AudienciaProcessoDt.STATUS_JULGAMENTO_EM_MESA_EXTRA_PAUTA);
		return relSessaoSegundoGrau(diretorioProjeto, audienciaCompleta, usuario, tiposDeAudiencia);
	}

	public byte[] relSessaoSegundoGrau(String diretorioProjeto, AudienciaDt audienciaCompleta, UsuarioDt usuario, List<String> tiposDeAudiencia) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao());
			
			List listaProcessos = obPersistencia.listarPautaSessao(audienciaCompleta.getId(), tiposDeAudiencia);
			
			// PATH PARA OS ARQUIVOS JASPER
			String pathJasper = Relatorios.getPathRelatorio(diretorioProjeto);
			
			String titulo = "PAUTA DO DIA";
			if (tiposDeAudiencia != null && 
				tiposDeAudiencia.size() > 0 &&
				!tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_PAUTA_DIA)) {
				if (tiposDeAudiencia.size() == 1 && tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_EM_MESA_EXTRA_PAUTA)) {
					titulo = "EM MESA PARA JULGAMENTO";
				} else { 
					titulo = "JULGAMENTOS ADIADOS";					
				}
			}				
			
			Date dataHoraSessao = Funcoes.StringToDateTime(audienciaCompleta.getDataAgendada());
			SimpleDateFormat FormatoData = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat FormatoHora = new SimpleDateFormat("HH:mm");
			String dataJulgamento = "DATA DO JULGAMENTO " + FormatoData.format(dataHoraSessao) + " AS " + FormatoHora.format(dataHoraSessao) + " HORAS OU NAS SESSÕES POSTERIORES";		

			// PARÂMETROS DO RELATÓRIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("titulo", titulo);
			parametros.put("dataAtual", new Date());
			parametros.put("SUBREPORT_DIR", pathJasper);
			parametros.put("serventia", audienciaCompleta.getServentia().toUpperCase());
			
			parametros.put("dataJulgamento", dataJulgamento);
			parametros.put("nomeSolicitante", usuario.getNome());
			
			temp = Relatorios.gerarRelatorioOdt(pathJasper, "listagemSessao2Grau", parametros, listaProcessos);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp;
	}

public RelatorioAudienciaProcesso ObtenhaRelatorioAudienciaProcesso(AudienciaDt audienciaCompleta,
														                AudienciaProcessoDt audienciaProcessoDt, 
														                boolean consultaAdvogados) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
				
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try
		{
			ProcessoParteDt primeiroPromovente = audienciaProcessoDt.getProcessoDt().getPrimeiroPoloAtivo();
			ProcessoParteDt primeiroPromovido = audienciaProcessoDt.getProcessoDt().getPrimeiroPoloPassivo();			
			if (consultaAdvogados && 
				primeiroPromovente != null && (primeiroPromovente.getAdvogados() == null || primeiroPromovente.getAdvogados().size() == 0) && 
			    primeiroPromovido != null && (primeiroPromovido.getAdvogados() == null || primeiroPromovido.getAdvogados().size() == 0)) {
				
				List advogadosDoProcesso = new ProcessoParteAdvogadoNe().consultarAdvogadosProcesso(audienciaProcessoDt.getProcessoDt().getId());
				
				if (advogadosDoProcesso != null && advogadosDoProcesso.size() > 0) {				
					for (Object processoParteAdvogadoObj : advogadosDoProcesso) {
						ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt)processoParteAdvogadoObj;
						if (processoParteAdvogadoDt.getId_ProcessoParte().equalsIgnoreCase(primeiroPromovente.getId())) {
							primeiroPromovente.setAdvogadoDt(processoParteAdvogadoDt);
						}
						if (processoParteAdvogadoDt.getId_ProcessoParte().equalsIgnoreCase(primeiroPromovido.getId())) {
							primeiroPromovido.setAdvogadoDt(processoParteAdvogadoDt);
						}
					}
				}
			}
			
			return this.ObtenhaRelatorioAudienciaProcesso(audienciaCompleta, serventiaNe, processoResponsavelNe, obFabricaConexao, audienciaProcessoDt);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	private ServentiaDt serventiaDtRelatorioAudienciaProcesso = null;
	private RelatorioAudienciaProcesso ObtenhaRelatorioAudienciaProcesso(AudienciaDt audienciaCompleta,
			                                                             ServentiaNe serventiaNe, 
			                                                             ProcessoResponsavelNe processoResponsavelNe, 
			                                                             FabricaConexao obFabricaConexao,
			                                                             AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		RelatorioAudienciaProcesso audiProc = new RelatorioAudienciaProcesso();
		
		String processoTipo = audienciaProcessoDt.getProcessoDt().getProcessoTipo();
		String descricaoPoloAtivo = audienciaProcessoDt.getProcessoDt().getDescricaoPoloAtivo();
		String descricaoPoloPassivo = audienciaProcessoDt.getProcessoDt().getDescricaoPoloPassivo();
		if (audienciaProcessoDt.getProcessoDt().getRecursoDt() != null) {
			processoTipo = audienciaProcessoDt.getProcessoDt().getRecursoDt().getProcessoTipo();
			descricaoPoloAtivo = audienciaProcessoDt.getProcessoDt().getRecursoDt().getDescricaoPoloAtivo();
			descricaoPoloPassivo = audienciaProcessoDt.getProcessoDt().getRecursoDt().getDescricaoPoloPassivo();
		}
		if (audienciaProcessoDt.getId_ProcessoTipo() != null && audienciaProcessoDt.getId_ProcessoTipo().trim().length() > 0 && audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0) {
			if (audienciaProcessoDt.getProcessoDt().getRecursoDt() != null) {
				if (audienciaProcessoDt.getProcessoDt().getRecursoDt().getId_ProcessoTipo() != null && 
				    audienciaProcessoDt.getId_ProcessoTipo() != null && 
				    !audienciaProcessoDt.getProcessoDt().getRecursoDt().getId_ProcessoTipo().equalsIgnoreCase(audienciaProcessoDt.getId_ProcessoTipo())) {								
					processoTipo = audienciaProcessoDt.getProcessoTipo() + " (Recurso Principal: " + audienciaProcessoDt.getProcessoDt().getRecursoDt().getProcessoTipo() + ")";
				} else {
					processoTipo = audienciaProcessoDt.getProcessoTipo() + " (Recurso Principal/Mérito)";	
				}
			} else {
				processoTipo = audienciaProcessoDt.getProcessoTipo();	
			}						
			descricaoPoloAtivo = audienciaProcessoDt.getDescricaoPoloAtivo();
			descricaoPoloPassivo = audienciaProcessoDt.getDescricaoPoloPassivo();
		}
		
		String complementoSegredoJustica = "";
		if (!(audienciaProcessoDt.getProcessoDt().getSegredoJustica() == null || audienciaProcessoDt.getProcessoDt().getSegredoJustica().equalsIgnoreCase("false"))) complementoSegredoJustica = " - SEGREDO DE JUSTIÇA";				
		audiProc.setProcessoTipo(processoTipo + complementoSegredoJustica);
		
		audiProc.setDescricaoPoloAtivo(descricaoPoloAtivo);
		audiProc.setDescricaoPoloPassivo(descricaoPoloPassivo);
		
		audiProc.setProcessoNumero(audienciaProcessoDt.getProcessoDt().getProcessoNumeroCompleto());
		
		if (audienciaProcessoDt.getProcessoDt().getRecursoDt() != null && audienciaProcessoDt.getProcessoDt().getRecursoDt().getId_ServentiaOrigem() != null && audienciaProcessoDt.getProcessoDt().getRecursoDt().getId_ServentiaOrigem().trim().length() > 0) {
			if (serventiaDtRelatorioAudienciaProcesso == null || serventiaDtRelatorioAudienciaProcesso.getId() != audienciaProcessoDt.getProcessoDt().getRecursoDt().getId_ServentiaOrigem())
				serventiaDtRelatorioAudienciaProcesso = serventiaNe.consultarId(audienciaProcessoDt.getProcessoDt().getRecursoDt().getId_ServentiaOrigem());
		} else {
			if (serventiaDtRelatorioAudienciaProcesso == null || serventiaDtRelatorioAudienciaProcesso.getId() != audienciaProcessoDt.getProcessoDt().getId_Serventia())
				serventiaDtRelatorioAudienciaProcesso = serventiaNe.consultarId(audienciaProcessoDt.getProcessoDt().getId_Serventia());	
		}
		
		if (serventiaDtRelatorioAudienciaProcesso != null && serventiaDtRelatorioAudienciaProcesso.getComarca() != null && serventiaDtRelatorioAudienciaProcesso.getComarca().trim().length() > 0)
			audiProc.setComarca(serventiaDtRelatorioAudienciaProcesso.getComarca().toUpperCase());
		else audiProc.setComarca("");
		
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		// Relator					
		ServentiaCargoDt relator = null;
		if (audienciaProcessoDt.getId_ServentiaCargo() != null && audienciaProcessoDt.getId_ServentiaCargo().trim().length() > 0) {
			relator = serventiaCargoNe.consultarId(audienciaProcessoDt.getId_ServentiaCargo());
			if (relator != null && relator.getCargoTipoCodigo() != null && 
			    !(Funcoes.StringToInt(relator.getCargoTipoCodigo().trim()) == CargoTipoDt.DESEMBARGADOR)) {
				relator = null;
			}
		}
		
		boolean incluiServentiaCargo = true;
		if (relator == null) {
			relator = processoResponsavelNe.consultarRelator2Grau(audienciaProcessoDt.getProcessoDt().getId(), obFabricaConexao);
			//------------------------------------------------------------------------------------------------------------------------------------------
			// Consulta se há substituição de gabinete para o relator. Caso haja, apresenta o nome do substituto com a devida confirmação de substituição 
			ServentiaCargoDt responsavelProcesso = relator;			
			if (responsavelProcesso==null) {
				 throw new MensagemException("Não foi possível encontar o relator"); 
			}
			ServentiaDt servProc = serventiaNe.consultarId(audienciaProcessoDt.getProcessoDt().getId_Serventia());
			ServentiaCargoDt serventiaCargoSubstituto = null;
			// Consulta gabinete substituto (se existir)
			ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
			ServentiaDt serventiaSubstitutaDt = serventiaRelacionadaNe.consultarGabineteSubstitutoAtualSegundoGrau(audienciaCompleta.getId_Serventia(), responsavelProcesso.getId_Serventia()); 			
			// testa para verificar se possui gabinete substituto, caso exista consulta o cargo de desembargador ativo (com quantidade de distribuição) deste gabinete 
			ServentiaCargoDt serventiaCargoTitularServentiaSubstituta = null;
			if (serventiaSubstitutaDt != null) {
				serventiaCargoTitularServentiaSubstituta = serventiaCargoNe.getDesembargadorTitular(serventiaSubstitutaDt.getId(), null);
			}
			if (serventiaCargoTitularServentiaSubstituta != null) {
				serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(serventiaCargoTitularServentiaSubstituta.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
			} else {
				serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(responsavelProcesso.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
			}
			if (serventiaCargoSubstituto == null) {
				serventiaCargoSubstituto = serventiaCargoTitularServentiaSubstituta;
			}
			if (serventiaCargoSubstituto != null 
					&& serventiaCargoSubstituto.getId() != null && serventiaCargoSubstituto.getId().length()>0
					&& responsavelProcesso.getQuantidadeDistribuicao() != null && responsavelProcesso.getQuantidadeDistribuicao().length()>0
					&& Funcoes.StringToInt(responsavelProcesso.getQuantidadeDistribuicao()) > 0 ){
				relator.setNomeUsuario((serventiaCargoSubstituto.getNomeUsuario() +" substituindo "+responsavelProcesso.getNomeUsuario()).toUpperCase());
				incluiServentiaCargo = false;
			} 
		}
		//------------------------------------------------------------------------------------------------------------------------------------------					
		
		if (relator != null && relator.getId().trim().length() > 0){
			audiProc.setRelator(relator.getNomeUsuario().toUpperCase() + (incluiServentiaCargo ? " (" + relator.getServentiaCargo().toUpperCase() + ")" : ""));
		} else {
			audiProc.setRelator("Não há - Verifique distribuição".toUpperCase());
		}					
		
		// Promoventes				
		audiProc.setPromoventes(obtenhaListaRelatorioAudienciaProcessoParteAdvogado(audienciaProcessoDt.getProcessoDt().getListaPolosAtivos()));
		audiProc.setListaPromoventesSemRecurso(audiProc.getPromoventes());
		
		// Promovidos					
		audiProc.setPromovidos(obtenhaListaRelatorioAudienciaProcessoParteAdvogado(audienciaProcessoDt.getProcessoDt().getListaPolosPassivos()));
		audiProc.setListaPromovidosSemRecurso(audiProc.getPromovidos());
		
		// jvosantos - 28/06/2019 17:53 - Adicionar bloco de código para buscar o recurso (não secundario) já que a busca usando "audienciaProcessoDt.getProcessoDt().getRecursoDt()" não funciona
		RecursoNe recursoNe = new RecursoNe();
		String idRecurso = recursoNe.consultarRecursoMaisNovo(audienciaProcessoDt.getProcessoDt().getId());
		
		if(StringUtils.isNotBlank(idRecurso)) {
			RecursoDt recursoDt = recursoNe.consultarIdCompleto(idRecurso);
			
			audiProc.setPoloAtivoRecurso(obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecurso(recursoDt.getListaRecorrentesAtivos(recursoDt.getId_ProcessoTipo()), audienciaProcessoDt.getProcessoDt().getSegredoJustica().equals("true")));
			audiProc.setPoloPassivoRecurso(obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecurso(recursoDt.getListaRecorridosAtivos(recursoDt.getId_ProcessoTipo()), audienciaProcessoDt.getProcessoDt().getSegredoJustica().equals("true")));
		}
		
		if (audienciaProcessoDt.getProcessoDt().getRecursoDt() != null) {
			audienciaProcessoDt.getProcessoDt().setRecursoDt(recursoNe.consultarIdCompleto(audienciaProcessoDt.getProcessoDt().getRecursoDt().getId()));
			
			// Recorrentes				
			audiProc.setPromoventes(obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecurso(audienciaProcessoDt.getProcessoDt().getRecursoDt().getListaRecorrentesAtivos(audienciaProcessoDt.getId_ProcessoTipo()), audienciaProcessoDt.getProcessoDt().getSegredoJustica().equals("true")));
			
			// Recorridos					
			audiProc.setPromovidos(obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecurso(audienciaProcessoDt.getProcessoDt().getRecursoDt().getListaRecorridosAtivos(audienciaProcessoDt.getId_ProcessoTipo()), audienciaProcessoDt.getProcessoDt().getSegredoJustica().equals("true")));
			
			if(audiProc.getPromoventes().isEmpty() && audiProc.getPromovidos().isEmpty()) {
				VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
				RecursoSecundarioParteDt recursoSecundario = votoPs
						.consultarRecursoSecundarioIdProcesso(audienciaProcessoDt.getProcessoDt().getId());
				
				if(recursoSecundario != null) {
					List<ProcessoParteDt> partesRecursoSecundario = votoPs
							.consultarPartesRecursoSecundario(audienciaProcessoDt.getId());
					if(partesRecursoSecundario != null) {
						List<ProcessoParteDt> poloAtivo = partesRecursoSecundario
								.stream()
								.filter(
										parte -> Funcoes.StringToInt(
												parte.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_ATIVO_CODIGO)
								.collect(Collectors.toCollection(ArrayList::new));
						
						List<ProcessoParteDt> poloPassivo = partesRecursoSecundario
								.stream()
								.filter(
										parte -> Funcoes.StringToInt(
												parte.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)
								.collect(Collectors.toCollection(ArrayList::new));
	
						audiProc.setPromoventes(obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecursoParteProcesso(poloAtivo, audienciaProcessoDt.getProcessoDt().getSegredoJustica().equals("true")));
						audiProc.setPromovidos(obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecursoParteProcesso(poloPassivo, audienciaProcessoDt.getProcessoDt().getSegredoJustica().equals("true")));
						audiProc.setDescricaoPoloAtivo(recursoSecundario.getDescricaoPoloAtivo().toUpperCase());
						audiProc.setDescricaoPoloPassivo(recursoSecundario.getDescricaoPoloPassivo().toUpperCase());
						
						int maiorOrdem = partesRecursoSecundario.stream().mapToInt(parte -> parte.getOrdemParte()).reduce(Integer::max).orElse(0) + 1;
						
						audiProc.setOrdemMaxima(maiorOrdem);

						HashMap<Integer, List<ProcessoParteDt>> poloAtivoOrdem = new HashMap<Integer, List<ProcessoParteDt>>();
						HashMap<Integer, List<ProcessoParteDt>> poloPassivoOrdem = new HashMap<Integer, List<ProcessoParteDt>>();

						for(int i = 0; i < maiorOrdem; ++i) {
							final int t = i;
							poloAtivo = partesRecursoSecundario
									.stream()
									.filter(
											parte -> Funcoes.StringToInt(parte.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_ATIVO_CODIGO && parte.getOrdemParte() == t)
									.collect(Collectors.toCollection(ArrayList::new));
							if(!poloAtivo.isEmpty()) poloAtivoOrdem.put(i, poloAtivo);
						}
						
						for(int i = 0; i < maiorOrdem; ++i) {
							final int t = i;
							poloPassivo = partesRecursoSecundario
									.stream()
									.filter(
											parte -> Funcoes.StringToInt(parte.getProcessoParteTipoCodigo()) == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO && parte.getOrdemParte() == t)
									.collect(Collectors.toCollection(ArrayList::new));
							if(!poloPassivo.isEmpty()) poloPassivoOrdem.put(i, poloPassivo);
						}

						audiProc.setPoloAtivoOrdem(poloAtivoOrdem);
						audiProc.setPoloPassivoOrdem(poloPassivoOrdem);
					}
				}
			}
		}
		
		//Atualiza a primeira parte e advogagados
		if (audiProc.getPromoventes().size() > 0) {
			RelatorioAudienciaProcessoParteAdvogado primeiroPoloAtivo = (RelatorioAudienciaProcessoParteAdvogado)audiProc.getPromoventes().get(0); 
			audiProc.setNomePoloAtivo(primeiroPoloAtivo.getNomeParte());
			audiProc.setAdvogadoPoloAtivo(primeiroPoloAtivo.getNomesAdvogados());
		}
		if (audiProc.getPromovidos().size() > 0) {
			RelatorioAudienciaProcessoParteAdvogado primeiroPoloPassivo = (RelatorioAudienciaProcessoParteAdvogado)audiProc.getPromovidos().get(0); 
			audiProc.setNomePoloPassivo(primeiroPoloPassivo.getNomeParte());
			audiProc.setAdvogadoPoloPassivo(primeiroPoloPassivo.getNomesAdvogados());
		}
		
		// Representante do MP (verifica se existe)
		if(audienciaProcessoDt.getNomeMPProcesso() != null && audienciaProcessoDt.getNomeMPProcesso().trim().length() > 0)
			audiProc.setProcuradorJustica(audienciaProcessoDt.getNomeMPProcesso().toUpperCase());
		else audiProc.setProcuradorJustica("Não há".toUpperCase());
		return audiProc;
	}

	/**
	 * 
	 * 
	 * @param listaPartes
	 * @return
	 */
	private List obtenhaListaRelatorioAudienciaProcessoParteAdvogado(List listaPartes)
	{
		List promoventes = new ArrayList();
		if (listaPartes != null)
		{
			for (int y = 0; y < listaPartes.size(); y++)
			{
				ProcessoParteDt parteProcesso = (ProcessoParteDt) listaPartes.get(y);
				RelatorioAudienciaProcessoParteAdvogado parteRelatorio = new RelatorioAudienciaProcessoParteAdvogado();
				parteRelatorio.setNomeParte(parteProcesso.getNome().toUpperCase());				
				if (parteProcesso.getAdvogados() != null)
				{	
					for (int z = 0; z < parteProcesso.getAdvogados().size(); z++)
					{
						ProcessoParteAdvogadoDt advogado = (ProcessoParteAdvogadoDt) parteProcesso.getAdvogados().get(z);
						parteRelatorio.addNomeAdvogado(advogado.getNomeAdvogado().toUpperCase());
					}												
				}
				promoventes.add(parteRelatorio);
			}
		}	
		
		return promoventes;
	}
	
	public void finalizarPendencia(String id_pendencia, PendenciaNe pendenciaNe, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception{
		PendenciaDt pendenciaDt = pendenciaNe.consultarId(id_pendencia, obFabricaConexao);			
		// Se existir, iremos finalizá-la
		if (pendenciaDt != null) {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);	
		}
	}
	
	/**
	 * Finaliza uma pendência do tipo Relatorio/Voto do relator da sessão
	 * 
	 * @param id_ServentiaCargo	 
	 * @param pendenciaNe
	 * @param usuarioDt
	 * @param idAudienciaProcessoSessaoSegundoGrau
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void finalizarPendenciaVotoRelator(String id_ServentiaCargo, PendenciaNe pendenciaNe, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, FabricaConexao obFabricaConexao) throws Exception{
		//Consulta Voto 2º Grau
		PendenciaDt pendenciaDtVoto = pendenciaNe.consultarPendenciaVotoRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finalizá-la
		if (pendenciaDtVoto != null) {
			pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtVoto, usuarioDt, obFabricaConexao);	
		}
	}

	/**
	 * Finaliza uma pendência do tipo Ementa do relator da sessão
	 * 
	 * @param id_ServentiaCargo	 
	 * @param pendenciaNe
	 * @param usuarioDt
	 * @param idAudienciaProcessoSessaoSegundoGrau
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void finalizarPendenciaEmentaRelatorSessao(String id_ServentiaCargo, PendenciaNe pendenciaNe, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, FabricaConexao obFabricaConexao) throws Exception{
		//Consulta Ementa 2º Grau
		PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarPendenciaEmentaRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finalizá-la
		if (pendenciaDtEmenta != null) {
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);	
		}
	}
	
	
	/**
	 * Finaliza uma pendência do tipo Relatorio/Voto do redator da sessão
	 * 
	 * @param id_ServentiaCargo	 
	 * @param pendenciaNe
	 * @param usuarioDt
	 * @param idAudienciaProcessoSessaoSegundoGrau
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void finalizarPendenciaVotoRedator(String id_ServentiaCargo, PendenciaNe pendenciaNe, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, FabricaConexao obFabricaConexao) throws Exception{
		//Consulta Voto 2º Grau
		PendenciaDt pendenciaDtVoto = pendenciaNe.consultarPendenciaVotoRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finalizá-la
		if (pendenciaDtVoto != null) {
			pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtVoto, usuarioDt, obFabricaConexao);	
		}
	}

	/**
	 * Finaliza uma pendência do tipo Ementa do redator da sessão
	 * 
	 * @param id_ServentiaCargo	 
	 * @param pendenciaNe
	 * @param usuarioDt
	 * @param idAudienciaProcessoSessaoSegundoGrau
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void finalizarPendenciaEmentaRedator(String id_ServentiaCargo, PendenciaNe pendenciaNe, UsuarioDt usuarioDt, String idAudienciaProcessoSessaoSegundoGrau, FabricaConexao obFabricaConexao) throws Exception{
		//Consulta Ementa 2º Grau
		PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarPendenciaEmentaRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finalizá-la
		if (pendenciaDtEmenta != null) {
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);	
		}
	}
	
	
	/**
	 * Consulta todos os extratos da ata de julgamento de todos os processos da sessão para montar o texto do editor de finalização da sessão
	 * 
	 * @param id_Audiencia
	 * @param usuarioDt
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public String obtenhaTextoTodosExtratosAtaJulgamentoFinalizarSessao(String id_Audiencia, UsuarioDt usuarioDt, boolean isSegundoGrau) throws Exception {
		ArquivoNe arquivoNe = new ArquivoNe();
		String textoExtrato = "";
		AudienciaDt audienciaDt = this.consultarAudienciaProcessos(id_Audiencia, usuarioDt, isSegundoGrau);
		
		if (audienciaDt != null && audienciaDt.getListaAudienciaProcessoDt() != null && audienciaDt.getListaAudienciaProcessoDt().size() > 0)
		{
			for (int i = 0; i < audienciaDt.getListaAudienciaProcessoDt().size(); i++)
			{
				AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) audienciaDt.getListaAudienciaProcessoDt().get(i);
				
				String id_arquivo = "";
				if (audienciaProcessoDt.getId_ArquivoAta() != null && audienciaProcessoDt.getId_ArquivoAta().trim().length() > 0) id_arquivo = audienciaProcessoDt.getId_ArquivoAta().trim();
				else if (audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada() != null && audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().trim().length() > 0) id_arquivo = audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().trim();
				else if (audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada() != null && audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().trim().length() > 0) id_arquivo = audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().trim();
				
				if (id_arquivo != null && id_arquivo.trim().length() > 0) {
					ArquivoDt arquivoDt = arquivoNe.consultarId(id_arquivo);
					if (arquivoDt != null && arquivoDt.isArquivoHtml()) {
						//if (textoExtrato.length() > 0) textoExtrato += "<BR /> \n";
						textoExtrato += new String(arquivoDt.getConteudo());
					}
				}
				
			}
		}
		
		return textoExtrato;
	}
	
	
	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoAudienciaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		AudienciaTipoNe AudienciaTipone = new AudienciaTipoNe(); 
		stTemp = AudienciaTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	public String consultarServentiaCargosAgendaAudienciaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaSubTipoCodigo) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		String stTemp = "";
		
		stTemp = serventiaCargoNe.consultarServentiaCargosAgendaAudienciaJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, serventiaSubTipoCodigo);
		serventiaCargoNe = null;
		
		return stTemp;
	}
	
	public String consultarAudienciasLivresJSON(String id_AudienciaTipo, String id_ServentiaCargo, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAudienciasLivresJSON(id_AudienciaTipo, id_ServentiaCargo, id_Serventia, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public void reservarAudienciaLivre(String[] id, String id_UsuarioLog, String ip_UsuarioLog) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		LogDt obLogDt = null;
		
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			obPersistencia.reservar(id);
			
			for(int i=0; i< id.length; i++) {
				obLogDt = new LogDt("Audiencia",id[i],id_UsuarioLog, ip_UsuarioLog, String.valueOf(LogTipoDt.Alterar),"[Reservar:0]", "[Reservar:1]");				
			}		
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluirAudienciaLivre(String[] id, String id_UsuarioLog, String ip_UsuarioLog) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		LogDt obLogDt = null;
	
		try{

			obFabricaConexao.iniciarTransacao();
			
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			audienciaProcessoNe.excluirAudienciaProcessoLivre(id, obFabricaConexao, id_UsuarioLog, ip_UsuarioLog);
			
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			obPersistencia.excluir(id);
			
			for(int i=0; i< id.length; i++) {
				obLogDt = new LogDt("Audiencia",id[i],id_UsuarioLog, ip_UsuarioLog, String.valueOf(LogTipoDt.Excluir), "", "");				
			}		
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw  e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void liberarAudienciaLivre(String[] id, String id_UsuarioLog, String ip_UsuarioLog) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		LogDt obLogDt = null;
	
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			obPersistencia.liberar(id);
			
			for(int i=0; i< id.length; i++) {
				obLogDt = new LogDt("Audiencia",id[i],id_UsuarioLog, ip_UsuarioLog, String.valueOf(LogTipoDt.Alterar),"[Reservar:1]", "[Reservar:0]");				
			}		
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public String consultarDescricaoClassificadorJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		String stTemp = "";
		ClassificadorNe neObjeto = new ClassificadorNe();
		
		stTemp = neObjeto.consultarClassificadorServentiaJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
				
		neObjeto = null;
		return stTemp;
	}
	
	/**
  	 * Método que conta quantos processos estão vinculados a uma sessão.
  	 * @param idAudiencia - id da sessão
  	 * @return número de processos
  	 * @throws Exception
  	 * @author hmgodinho
  	 */
	public int contarProcessosSessao(String idAudiencia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		LogDt obLogDt = null;
		int qtdeProcessosSessao;
	
		try{

			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			
			qtdeProcessosSessao = obPersistencia.contarProcessosSessao(idAudiencia);
	
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return qtdeProcessosSessao;
	}
	
	/**
	 * Método responsável por consultar a pendência elaboração de Voto
	 * @param PendenciaArquivoDt pendenciaArquivoDt
	 * @return List
	 * @throws Exception
	 */
	public PendenciaDt consultarPendenciaElaboracaoVoto(String id_Pendencia) throws Exception{
		PendenciaDt pendenciaDt = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		pendenciaDt = pendenciaNe.consultarId(id_Pendencia);
				
		pendenciaNe = null;
		
		return pendenciaDt;
	}
	
	public AudienciaTipoDt consultarIdAudienciaTipo(String idAudienciaTipo) throws Exception {
		return new AudienciaTipoNe().consultarId(idAudienciaTipo);
	}
	
	/**
     * Retorna a serventia relacionada do tipo Preprocessual
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author mmgomes
     * @throws Exception 
     */
    public ServentiaDt consultarServentiaPreprocessualRelacionada(String id_Serventia) throws Exception{
        return new ServentiaRelacionadaNe().consultarServentiaPreprocessualRelacionada(id_Serventia);
    }
    
    public void salvar(AudienciaDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		
		AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Audiencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("Audiencia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
    
    public ServentiaDt consultarIdSeventia(String idServentia) throws Exception {
		return new ServentiaNe().consultarId(idServentia);
	}
    
	/**
  	 * Busca pautas para uma data especifica, por tipo de audiencia
  	 * @param idAudiencia - id da sessão
  	 * @return lista com a pauta {id_serv, id_serv_cargo, periodo }
  	 * @throws Exception
  	 * @author jrcorrea
  	 */
	public List buscarPauta(Date data, String id_serventia) throws Exception {
		FabricaConexao obFabricaConexao = null; 
		
		List listPauta;
		
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			
			listPauta = obPersistencia.buscarPauta(data,id_serventia);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listPauta;
	}
	
	/**
	 * 
	 * 
	 * @param listaPartes
	 * @return
	 * @throws Exception 
	 */
	private List obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecurso(List listaPartes, boolean ehSegredoJustica) throws Exception
	{
		List promoventes = new ArrayList();
		if (listaPartes != null)
		{
			for (int y = 0; y < listaPartes.size(); y++)
			{
				RecursoParteDt recursoParte = (RecursoParteDt)listaPartes.get(y);
				ProcessoParteDt parteProcesso = (ProcessoParteDt) recursoParte.getProcessoParteDt();
				RelatorioAudienciaProcessoParteAdvogado parteRelatorio = new RelatorioAudienciaProcessoParteAdvogado();
				parteRelatorio.setNomeParte(ehSegredoJustica ? Funcoes.iniciaisNome(parteProcesso.getNome()) : parteProcesso.getNome().toUpperCase());				
				parteProcesso.setAdvogados(new ProcessoParteAdvogadoNe().consultarAdvogadosParte(parteProcesso.getId()));				
				if (parteProcesso.getAdvogados() != null)
				{	
					for (int z = 0; z < parteProcesso.getAdvogados().size(); z++)
					{
						ProcessoParteAdvogadoDt advogado = (ProcessoParteAdvogadoDt) parteProcesso.getAdvogados().get(z);
						parteRelatorio.addNomeAdvogado(advogado.getNomeAdvogado().toUpperCase());
					}												
				}
				promoventes.add(parteRelatorio);
			}
		}	
		
		return promoventes;
	}
	
	/**
	 * 
	 * 
	 * @param listaPartes
	 * @return
	 * @throws Exception 
	 */
	private List obtenhaListaRelatorioAudienciaProcessoParteAdvogadoRecursoParteProcesso(List<ProcessoParteDt> listaPartes, boolean ehSegredoJustica) throws Exception
	{
		List promoventes = new ArrayList();
		if (listaPartes != null)
		{
			for (int y = 0; y < listaPartes.size(); y++)
			{
				ProcessoParteDt parteProcesso = (ProcessoParteDt) listaPartes.get(y);
				RelatorioAudienciaProcessoParteAdvogado parteRelatorio = new RelatorioAudienciaProcessoParteAdvogado();
				parteRelatorio.setNomeParte(ehSegredoJustica ? Funcoes.iniciaisNome(parteProcesso.getNome()) : parteProcesso.getNome().toUpperCase());				
				parteProcesso.setAdvogados(new ProcessoParteAdvogadoNe().consultarAdvogadosParte(parteProcesso.getId()));				
				if (parteProcesso.getAdvogados() != null)
				{	
					for (int z = 0; z < parteProcesso.getAdvogados().size(); z++)
					{
						ProcessoParteAdvogadoDt advogado = (ProcessoParteAdvogadoDt) parteProcesso.getAdvogados().get(z);
						parteRelatorio.addNomeAdvogado(advogado.getNomeAdvogado().toUpperCase());
					}												
				}
				promoventes.add(parteRelatorio);
			}
		}	
		
		return promoventes;
	}
	/**
	 * Método que verifica se os dados obrigatórios em uma movimentação de audiência foram preenchidos
	 * 
	 * @param dados
	 *            , objeto com dados da movimentação a ser verificada
	 * @author gschiquini
	 */
	public String verificarMovimentacaoAudienciaProcessoPJD(AudienciaMovimentacaoDt dados, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";
		if (dados.getAudienciaStatusCodigo().length() > 0 && dados.getAudienciaStatusCodigo().equals("-1")) stRetorno += "Selecione o Status da Audiência. \n";
		// Se for uma Sessão de 2º grau que está sendo remarcada, deve ser selecionada a data da nova sessão
		else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) == AudienciaProcessoStatusDt.REMARCADA && Funcoes.StringToInt(dados.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && dados.getId_NovaSessao().length() == 0) {
			stRetorno += "Selecione a Data da nova Sessão a ser marcada. \n";
		}
		int grupoTipoUsuarioLogado = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		if (dados.isPreAnalise()){
			if (dados.getId_ArquivoTipo() == null || dados.getId_ArquivoTipo().trim().length() == 0 || dados.getArquivoTipo() == null || dados.getArquivoTipo().trim().length() == 0 ){
				stRetorno += "Selecione o Tipo do Arquivo: Acórdão, Relatório e Voto. \n";
			}				
			/*if(dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())){
				stRetorno += "É necessário redigir o texto da pré-análise: Acórdão, Relatório e Voto. \n";
			}*/
			if (dados.getId_ArquivoTipoEmenta() == null || dados.getId_ArquivoTipoEmenta().trim().length() == 0 || dados.getArquivoTipoEmenta() == null || dados.getArquivoTipoEmenta().trim().length() == 0 ){
				stRetorno += "Selecione o Tipo do Arquivo: Ementa. \n";
			}				
			if(dados.getTextoEditorEmenta() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditorEmenta())){
				stRetorno += "É necessário redigir o texto da pré-análise: Ementa. \n";
			}			

		} else {
				if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) {
					stRetorno += "É necessário inserir um arquivo. \n";
				} else if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU && dados.getListaArquivos().size() > 1){
					if(!(usuarioDt.getServentiaSubtipoCodigo() != null &&
							(Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL
						     || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL)))
						stRetorno = "É necessário inserir apenas um arquivo correspondente à Ata. \n";			
				} else if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
					int quantidadeEmenta = 0;
					ArquivoDt arquivoDt = null;
					for (int i=0; i < dados.getListaArquivos().size(); i++){
						arquivoDt = (ArquivoDt) dados.getListaArquivos().get(i);
						if(arquivoDt != null && arquivoDt.getArquivoTipo().equals(dados.getArquivoTipoEmenta())){
							quantidadeEmenta += 1;
						}
					}		
					if (quantidadeEmenta == 0) stRetorno = "É necessário inserir um texto correspondente à Ementa no Editor. \n";
					else if (quantidadeEmenta > 1) stRetorno = "É permitido inserir apenas um arquivo correspondente à Ementa. \n";					
				}
		}		
		
		return stRetorno;
	}
	
	public ModeloDt consultarModeloCodigo( String ModeloCodigo) throws Exception{
		ModeloDt tempMod = null;
		ModeloNe neObjeto = new ModeloNe();
		
		tempMod = neObjeto.consultarModeloCodigo(ModeloCodigo);
		
		neObjeto = null;
		return tempMod;
	}
	
	public String montaModelo(AudienciaMovimentacaoDt audienciaMovimentacaoDt, ModeloDt modeloDt, UsuarioNe usuarioNe) throws Exception {
        ModeloNe modeloNe = new ModeloNe();

        return modeloNe.montaConteudoPorModelo(audienciaMovimentacaoDt, usuarioNe.getUsuarioDt(), modeloDt);
    }
	
	// jvosantos - 04/06/2019 09:50 - Método responsavel por descar a pre analise dos apreciados
	public void descarteStatusPreAnalisesConclusaoVirtualAguardandoAssinatura(AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt) throws Exception {	
		PendenciaNe pendenciaNe = new PendenciaNe();
		VotoNe votoNe = new VotoNe();
		
		
		for (SessaoSegundoGrauProcessoDt sessao : assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau()) {
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(votoNe.consultarIdPendenciaApreciadosPorAudienciaProcesso(sessao.getAudienciaProcessoDt().getId()));
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA));
			pendenciaNe.alterarStatus(pendenciaDt);
		}
		
		descarteStatusPreAnalisesConclusaoAguardandoAssinatura(assinarSessaoSegundoGrauDt);
	}
	
	public void descarteStatusPreAnalisesConclusaoAguardandoAssinatura(AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt) throws Exception {		
		List listaPendenciaArquivodt = new ArrayList();
		
		for (SessaoSegundoGrauProcessoDt sessao : assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau()) {
			if (sessao.getPendenciaArquivoDtRelatorioEVoto() != null) {
				sessao.getPendenciaArquivoDtRelatorioEVoto().setId_UsuarioLog(assinarSessaoSegundoGrauDt.getId_UsuarioLog());
				sessao.getPendenciaArquivoDtRelatorioEVoto().setIpComputadorLog(assinarSessaoSegundoGrauDt.getIpComputadorLog());
				listaPendenciaArquivodt.add(sessao.getPendenciaArquivoDtRelatorioEVoto());		
			}
			if (sessao.getPendenciaArquivoDtEmenta() != null) {
				sessao.getPendenciaArquivoDtEmenta().setId_UsuarioLog(assinarSessaoSegundoGrauDt.getId_UsuarioLog());
				sessao.getPendenciaArquivoDtEmenta().setIpComputadorLog(assinarSessaoSegundoGrauDt.getIpComputadorLog());
				listaPendenciaArquivodt.add(sessao.getPendenciaArquivoDtEmenta());
			}			
		}
		
		if(listaPendenciaArquivodt.size() > 0) {
			new PendenciaArquivoNe().alterarStatusPendenciaArquivo(listaPendenciaArquivodt, String.valueOf(PendenciaArquivoDt.NORMAL));
		}
	}
	
	public AudienciaMovimentacaoDt obtenhaAudienciaMovimentacaoPeloIdAudienciaProcesso(HttpServletRequest request, String idAudienciaProcesso, UsuarioNe UsuarioSessao, String tipoAudienciaProcessoMovimentacao, String fluxo, boolean isAlteracaoDoExtratoDaAta) throws Exception {
		List tempList = null;
		int grupoTipoUsuarioLogado = UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
		
			AudienciaMovimentacaoDt audienciaMovimentacaoDt = new AudienciaMovimentacaoDt();
			audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao(tipoAudienciaProcessoMovimentacao);	
			//Captura o menu acionado para permitir retorno para consulta correta
			audienciaMovimentacaoDt.setfluxo(fluxo);
			
			AudienciaDt audienciaDt = this.consultarAudienciaProcessoCompleta(idAudienciaProcesso);
			
			if (audienciaDt == null) {
				AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
				AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarId(idAudienciaProcesso, obFabricaConexao);

				if (audienciaProcessoDt == null) {
					throw new MensagemException("Audiência não encontrada, entre em contato com o suporte!");
				}
				
				ProcessoNe processoNe = new ProcessoNe();
				ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaProcessoDt.getId_Processo(), obFabricaConexao);
				
				if (processoDt == null) {
					throw new MensagemException("Processo não encontrado, entre em contato com o suporte!");
				}
				
				String novo_Id_ProcessoTipo = processoDt.getId_ProcessoTipo();
				
				RecursoNe recursoNe = new RecursoNe();				
				String idRecurso = recursoNe.consultarRecursoMaisNovo(audienciaProcessoDt.getId_Processo(), obFabricaConexao);
				if (idRecurso != null && Funcoes.StringToLong(idRecurso) > 0) {
					RecursoDt recursoDt = recursoNe.consultarId(idRecurso, obFabricaConexao);					
					if (recursoDt != null && (recursoDt.getDataRetorno() == null || recursoDt.getDataRetorno().trim().length() == 0)) {
						novo_Id_ProcessoTipo = recursoDt.getId_ProcessoTipo();
					}
				}
				
				LogDt logDt = new LogDt();
				logDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				logDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
				audienciaProcessoNe.alterarProcessoTipoSessaoSegundoSegundoGrau(audienciaProcessoDt, novo_Id_ProcessoTipo, logDt, obFabricaConexao);
				
				audienciaDt = this.consultarAudienciaProcessoCompleta(idAudienciaProcesso);				
				if (audienciaDt == null) {
					throw new MensagemException("Audiência não encontrada, entre em contato com o suporte!");
				}
			}
			if(isAlteracaoDoExtratoDaAta) {
				AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusNe().consultarId(audienciaDt.getAudienciaProcessoDt().getId_AudienciaProcessoStatusAnalista());
				audienciaMovimentacaoDt.setAudienciaStatusCodigo(audienciaProcessoStatusDt.getAudienciaProcessoStatusCodigo());
			}
			if (!isAlteracaoDoExtratoDaAta && (tipoAudienciaProcessoMovimentacao == null || tipoAudienciaProcessoMovimentacao.trim().equalsIgnoreCase("null")) && audienciaDt != null && audienciaDt.getAudienciaProcessoDt() != null) {
				if (audienciaDt.getAudienciaProcessoDt().isJulgamentoAdiado()) {
					audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("4");
				} else if (audienciaDt.getAudienciaProcessoDt().isJulgamentoIniciado()) {
					audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("3");
				}
			}
	
			// Consulta dados completos do processo com as partes para permitir por exemplo Intimação de Partes e Testemunhas
			audienciaDt.getAudienciaProcessoDt().setProcessoDt(this.consultarProcessoIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId()));
			AudienciaPs audienciaPs = new AudienciaPs(obFabricaConexao.getConexao());
			String procTipo = audienciaPs.consultarProcessoTipoRecursoSecundarioIdAudienciaProcesso(audienciaDt.getAudienciaProcessoDt().getId());
			ProcessoDt processoDt = audienciaDt.getAudienciaProcessoDt().getProcessoDt();
			if(processoDt != null) {
				processoDt.setRecursoDt(new RecursoDt());
				processoDt.getRecursoDt().setProcessoTipoRecursoParteAtual(procTipo);
			}
			// Seta o processo
			audienciaMovimentacaoDt.setAudienciaDt(audienciaDt);
	
			//Seta status de audiência possíveis
			audienciaMovimentacaoDt.setListaAudienciaProcessoStatus(this.consultarStatusAudiencia(processoDt.getServentiaSubTipoCodigo()));
			//audienciaMovimentacaoDt.setListaAudienciaProcessoStatus(this.consultarStatusAudiencia(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
			
			// Seta tipos de pendências que poderão ser geradas
			audienciaMovimentacaoDt.setListaPendenciaTipos(this.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
			
			//seta o voto se existir o mesmo para o processo (Tratamento para o segundo Grau)					
			if (UsuarioSessao.getUsuarioDt().getId_ServentiaCargo() != null && !UsuarioSessao.getUsuarioDt().getId_ServentiaCargo().equals("")){						
				MovimentacaoArquivoDt movimentacaoArquivoDt = this.consultarRelatorioProcesso(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), UsuarioSessao);
				//busca o relatório se existir o mesmo para o processo (Tratamento para o segundo Grau)
				if (movimentacaoArquivoDt != null && movimentacaoArquivoDt.getId() != null && !movimentacaoArquivoDt.getId().equals("")){
					audienciaMovimentacaoDt.setId_Relatorio(movimentacaoArquivoDt.getId());
					audienciaMovimentacaoDt.setHashRelatorio(movimentacaoArquivoDt.getHash());
				}
			}
			
			//Armazena o redator da sessão, caso exista
			audienciaMovimentacaoDt.setId_ServentiaCargoRedator(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargoRedator());
			audienciaMovimentacaoDt.setServentiaCargoRedator(audienciaDt.getAudienciaProcessoDt().getServentiaCargoRedator());
			
			if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && UsuarioSessao.getUsuarioDt().isTurmaJulgadora()){
				String stIdPendencia = request.getParameter("Id_Pendencia");
				if (stIdPendencia != null && !stIdPendencia.equals("")) {
					PendenciaArquivoDt pendenciaAquDt = new PendenciaArquivoDt();
					pendenciaAquDt.setId_Pendencia(stIdPendencia);
					audienciaMovimentacaoDt.setPendenciaArquivoDt(pendenciaAquDt);
				}
			}
			
			if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ() )) {
				
				// se for desembargador ou assistente devemos selecinar o Status da Audiência escolhido pelo analista
				if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE  || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO  || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){
					
					String Id_ServentiaCargo = UsuarioSessao.getUsuarioDt().getId_ServentiaCargo();
					
					if ((UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)) Id_ServentiaCargo = UsuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
					
					audienciaMovimentacaoDt.setPendenciaArquivoDt(this.consultarVotoDesembargador(Id_ServentiaCargo, audienciaDt.getAudienciaProcessoDt(), audienciaDt.getAudienciaProcessoDt().getId_ProcessoTipo()));
					
					// seta atributos necessários Voto (Tratamento para o segundo Grau)
					if (audienciaMovimentacaoDt.getPendenciaArquivoDt() != null ){						
						audienciaMovimentacaoDt.setId_ArquivoTipo(audienciaMovimentacaoDt.getPendenciaArquivoDt().getArquivoDt().getId_ArquivoTipo());
						request.setAttribute("Id_ArquivoTipo", audienciaMovimentacaoDt.getId_ArquivoTipo());								
						audienciaMovimentacaoDt.setArquivoTipo(audienciaMovimentacaoDt.getPendenciaArquivoDt().getArquivoDt().getArquivoTipo());						
						request.setAttribute("ArquivoTipo", audienciaMovimentacaoDt.getArquivoTipo());
						audienciaMovimentacaoDt.setArquivoTipoSomenteLeitura(true);
						
						audienciaMovimentacaoDt.setNomeArquivo(audienciaMovimentacaoDt.getPendenciaArquivoDt().getArquivoDt().getNomeArquivo());
						audienciaMovimentacaoDt.setNomeArquivoSomenteLeitura(true);
						
						audienciaMovimentacaoDt.setTextoEditor(audienciaMovimentacaoDt.getPendenciaArquivoDt().getArquivoDt().getArquivo());
						request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getPendenciaArquivoDt().getArquivoDt().getArquivo());
						
						//lista de pendências a gerar de um determinado voto
						audienciaMovimentacaoDt.setListaPendenciasGerar(this.consultarPendenciasVotoEmenta(audienciaMovimentacaoDt.getPendenciaArquivoDt()));
						
						// Obtendo o Classificador escolhido durante a pré-análise do Voto								
						AnaliseConclusaoDt analiseConclusaoDt = this.getPreAnaliseConclusao(audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia());								
						if (analiseConclusaoDt != null){
							audienciaMovimentacaoDt.setId_Classificador(analiseConclusaoDt.getId_Classificador());
							audienciaMovimentacaoDt.setClassificador(analiseConclusaoDt.getClassificador());
							audienciaMovimentacaoDt.setJulgadoMeritoProcessoPrincipal(analiseConclusaoDt.getJulgadoMeritoProcessoPrincipal());
						}
						
					} else {
						ArquivoTipoDt arquivoTipoDtVotoSessao = this.consultarArquivoTipoVotoSessao();
						if (arquivoTipoDtVotoSessao != null){								
							audienciaMovimentacaoDt.setId_ArquivoTipo(arquivoTipoDtVotoSessao.getId());		
							request.setAttribute("Id_ArquivoTipo", audienciaMovimentacaoDt.getId_ArquivoTipo());
							audienciaMovimentacaoDt.setArquivoTipo(arquivoTipoDtVotoSessao.getArquivoTipo());
							request.setAttribute("ArquivoTipo", audienciaMovimentacaoDt.getArquivoTipo());
							audienciaMovimentacaoDt.setArquivoTipoSomenteLeitura(true);											
						}								
						// Seta o nome do arquivo de voto
						audienciaMovimentacaoDt.setNomeArquivo("Relatorio_Voto_Acordao.html");
						audienciaMovimentacaoDt.setNomeArquivoSomenteLeitura(true);
					}
					
					audienciaMovimentacaoDt.setPendenciaArquivoDtEmenta(this.consultarEmentaDesembargador(Id_ServentiaCargo, audienciaDt.getAudienciaProcessoDt(), audienciaDt.getAudienciaProcessoDt().getId_ProcessoTipo()));
												
					// seta atributos necessários Ementa (Tratamento para o segundo Grau)
					if (audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta() != null ){						
						audienciaMovimentacaoDt.setId_ArquivoTipoEmenta(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getArquivoDt().getId_ArquivoTipo());
						request.setAttribute("Id_ArquivoTipoEmenta", audienciaMovimentacaoDt.getId_ArquivoTipoEmenta());
						audienciaMovimentacaoDt.setArquivoTipoEmenta(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getArquivoDt().getArquivoTipo());						
						request.setAttribute("ArquivoTipoEmenta", audienciaMovimentacaoDt.getArquivoTipoEmenta());
														
						audienciaMovimentacaoDt.setNomeArquivoEmenta(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getArquivoDt().getNomeArquivo());
						
						audienciaMovimentacaoDt.setTextoEditorEmenta(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getArquivoDt().getArquivo());
						request.setAttribute("TextoEditorEmenta",audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getArquivoDt().getArquivo());												
					} else {
						// Consulta do Tipo de Arquivo Ementa
						ArquivoTipoDt arquivoTipoDtEmentaSessao = this.consultarArquivoTipoEmentaSessao();
						if (arquivoTipoDtEmentaSessao != null){								
							audienciaMovimentacaoDt.setId_ArquivoTipoEmenta(arquivoTipoDtEmentaSessao.getId());			
							request.setAttribute("Id_ArquivoTipoEmenta", audienciaMovimentacaoDt.getId_ArquivoTipoEmenta());
							audienciaMovimentacaoDt.setArquivoTipoEmenta(arquivoTipoDtEmentaSessao.getArquivoTipo());
							request.setAttribute("ArquivoTipoEmenta", audienciaMovimentacaoDt.getArquivoTipoEmenta());
						}								
						// Seta o nome do arquivo de ementa
						audienciaMovimentacaoDt.setNomeArquivoEmenta("Ementa.html");		
					}
					
					// Seta na sessão a lista de pendências do tipo voto e ementa...
					if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) {
						request.getSession().setAttribute("ListaPendencias", Funcoes.converterListParaSet(audienciaMovimentacaoDt.getListaPendenciasGerar()));
						request.getSession().setAttribute("Id_ListaDadosMovimentacao", audienciaMovimentacaoDt.getListaPendenciasGerar().size());
					}						
					
					if (audienciaMovimentacaoDt != null && audienciaMovimentacaoDt.getAudienciaDt() != null && audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null){
						AudienciaProcessoDt audienciaProcessoDt = audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt();
						
						if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo() != null && audienciaProcessoDt.getId_AudienciaProcessoStatusAnalista() != null){								
							audienciaMovimentacaoDt.setAudienciaStatus(audienciaProcessoDt.getAudienciaProcessoStatusAnalista());
							audienciaMovimentacaoDt.setAudienciaStatusCodigo(audienciaProcessoDt.getAudienciaProcessoStatusCodigoAnalista());
							audienciaMovimentacaoDt.setAudienciaProcessoStatusSomenteLeitura(true);
						}
						
						if (audienciaProcessoDt.getId_ArquivoAta() != null && audienciaProcessoDt.getId_ArquivoAta().length() > 0){								
							ArquivoDt arquivoAtaDt = this.consultarArquivo(audienciaProcessoDt.getId_ArquivoAta());
							if(arquivoAtaDt !=null) audienciaProcessoDt.setNomeArquivoAta(arquivoAtaDt.getNomeArquivoFormatado());
							else audienciaProcessoDt.setId_ArquivoAta("");									
						}
					}	
					
				} else if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU){										
					ArquivoTipoDt arquivoTipoDtExtratoAtaDeJulgamento = null;
					if (audienciaMovimentacaoDt.isMovimentacaoSessaoRetiradaDePauta() ||
						audienciaMovimentacaoDt.isMovimentacaoSessaoDesmarcada()) {
						arquivoTipoDtExtratoAtaDeJulgamento = this.consultarArquivoTipoCertidao();
					} else {
						arquivoTipoDtExtratoAtaDeJulgamento = this.consultarArquivoTipoExtratoAtaSessao();	
					}
					
					if (arquivoTipoDtExtratoAtaDeJulgamento != null){								
						audienciaMovimentacaoDt.setId_ArquivoTipo(arquivoTipoDtExtratoAtaDeJulgamento.getId());																					
						audienciaMovimentacaoDt.setArquivoTipo(arquivoTipoDtExtratoAtaDeJulgamento.getArquivoTipo());
						
						if (!audienciaMovimentacaoDt.isMovimentacaoSessaoRetiradaDePauta() && !audienciaMovimentacaoDt.isMovimentacaoSessaoDesmarcada()) {
							audienciaMovimentacaoDt.setArquivoTipoSomenteLeitura(true);	
						}				
						
						request.setAttribute("Id_ArquivoTipo", audienciaMovimentacaoDt.getId_ArquivoTipo());
						request.setAttribute("ArquivoTipo", audienciaMovimentacaoDt.getArquivoTipo());								
					}						
					
					String nomeArquivoExtratoAta = "";
					AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
					audienciaMovimentacaoDt.setAlteracaoExtratoAta((request.getParameter("EhAlteracaoExtratoAta") != null && String.valueOf(request.getParameter("EhAlteracaoExtratoAta")).equalsIgnoreCase("S")));
					if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada()) {
						nomeArquivoExtratoAta = "Extrato Ata de Inicio de Julgamento - Sessao %s.html";
						audienciaProcessoStatusDt = this.consultarAudienciaProcessoStatusCodigoMovimentacao(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_INICIADO));
						audienciaMovimentacaoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.PEDIDO_VISTA));
						audienciaMovimentacaoDt.setPendenciaTipoSomenteLeitura(true);
					} else if (audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()) {
						nomeArquivoExtratoAta = "Extrato Ata de Adiamento de Julgamento - Sessao %s.html";
						audienciaProcessoStatusDt = this.consultarAudienciaProcessoStatusCodigoMovimentacao(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO));
						audienciaMovimentacaoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.PEDIDO_VISTA));
						audienciaMovimentacaoDt.setPendenciaTipoSomenteLeitura(true);
					} else if (audienciaMovimentacaoDt.isMovimentacaoSessaoRetiradaDePauta()) {
						nomeArquivoExtratoAta = "Retirada de Pauta - Sessao %s.html";
						audienciaProcessoStatusDt = this.consultarAudienciaProcessoStatusCodigoMovimentacao(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), String.valueOf(AudienciaProcessoStatusDt.RETIRAR_PAUTA));					
					} else if (audienciaMovimentacaoDt.isMovimentacaoSessaoDesmarcada()) {
						nomeArquivoExtratoAta = "Desmarcada - Sessao %s.html";
						audienciaProcessoStatusDt = this.consultarAudienciaProcessoStatusCodigoMovimentacao(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), String.valueOf(AudienciaProcessoStatusDt.DESMARCAR_PAUTA));	
					} else {
						nomeArquivoExtratoAta = "Extrato Ata de Julgamento - Sessao %s.html";
						audienciaMovimentacaoDt.setIgnoraEtapa2Pendencias(true);

						if(audienciaDt != null && StringUtils.isEmpty(audienciaMovimentacaoDt.getId_ServentiaCargoPresidente())) {
							String idPresidente = "";
							
							for(AudienciaProcessoDt audiProcDt : (List<AudienciaProcessoDt>) audienciaDt.getListaAudienciaProcessoDt()) {
								if(audiProcDt != null && !audiProcDt.getId().equals(idAudienciaProcesso)) continue;
								idPresidente = audiProcDt.getId_ServentiaCargoPresidente();
							}
							
							audienciaMovimentacaoDt.setId_ServentiaCargoPresidente(idPresidente);
						}
						
						// Obtem o desembargador presidente da câmara, se o atributo da sessão não estiver preenchido
						if (!audienciaMovimentacaoDt.temPresidente()){						
							ServentiaCargoDt cargoPresidente = this.consultarPresidenteSegundoGrau(UsuarioSessao.getUsuarioDt().getId_Serventia());									
							if (cargoPresidente != null) {
								tempList = this.consultarServentiaCargos("", "0",  UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
								if (tempList != null && tempList.size() > 0) {
									ServentiaCargoDt cargoDesmbargador = null;
									for (int i = 0; i < tempList.size(); i++) {
										ServentiaCargoDt serventiaCargoDtTemp = (ServentiaCargoDt) tempList.get(i);
										if (serventiaCargoDtTemp.getNomeUsuario().trim().equalsIgnoreCase(cargoPresidente.getNomeUsuario().trim())){
											cargoDesmbargador = serventiaCargoDtTemp;
											break;
										}
									}
									if (cargoDesmbargador != null) {
										audienciaMovimentacaoDt.setId_ServentiaCargoPresidente(cargoDesmbargador.getId());
										audienciaMovimentacaoDt.setServentiaCargoPresidente(cargoDesmbargador.getNomeUsuario());
									}
								}	
							}							
						}
						
						//Obtem o representante do ministério público vinculado à câmara, se o atributo da sessão não estiver preenchido
						if (!audienciaMovimentacaoDt.temMpResponsavel()){ 							
							ServentiaDt promotoriaRelacionada = this.consultarPromotoriaRelacionada(UsuarioSessao.getUsuarioDt().getId_Serventia());      
				            if(promotoriaRelacionada != null){
				            	audienciaMovimentacaoDt.setId_ServentiaMp(promotoriaRelacionada.getId());
				            	audienciaMovimentacaoDt.setServentiaMp(promotoriaRelacionada.getServentia());
								tempList = this.consultarServentiaCargos("", "0",  promotoriaRelacionada.getId(), String.valueOf(ServentiaTipoDt.PROMOTORIA), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
								if (tempList != null && tempList.size() > 0) {
									ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) tempList.get(0);
									audienciaMovimentacaoDt.setId_ServentiaCargoMp(serventiaCargoDt.getId());
									audienciaMovimentacaoDt.setServentiaCargoMp( serventiaCargoDt.getServentiaCargo() + " - " + serventiaCargoDt.getNomeUsuario());											
									}
				            }	
						}
					}
					
					if (audienciaProcessoStatusDt != null){								
						audienciaMovimentacaoDt.setAudienciaStatus(audienciaProcessoStatusDt.getAudienciaProcessoStatus());
						audienciaMovimentacaoDt.setAudienciaStatusCodigo(audienciaProcessoStatusDt.getAudienciaProcessoStatusCodigo());
						audienciaMovimentacaoDt.setAudienciaProcessoStatusSomenteLeitura(true);
					}
					
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH-mm");
					Date dataSessao = Funcoes.DataHoraMinuto(audienciaDt.getDataAgendada());
					audienciaMovimentacaoDt.setNomeArquivo(String.format(nomeArquivoExtratoAta, df.format(dataSessao)));
					if (!audienciaMovimentacaoDt.isMovimentacaoSessaoRetiradaDePauta() && !audienciaMovimentacaoDt.isMovimentacaoSessaoDesmarcada()) {
						audienciaMovimentacaoDt.setNomeArquivoSomenteLeitura(true);	
					}				
					
	//				if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()) {
	//					ModeloDt modeloDt = audienciaNe.consultarModeloCodigo(audienciaProcessoDt.EXTRATO_ATA_JULGAMENTO_ADIADO_INICIADO_MODELO_CODIGO);								
	//					if (modeloDt != null) {
	//						audienciaMovimentacaoDt.setTextoEditor(audienciaNe.montaModelo(audienciaMovimentacaoDt, modeloDt, UsuarioSessao));
	//						
	//						audienciaMovimentacaoDt.setId_Modelo(modeloDt.getId());
	//						audienciaMovimentacaoDt.setModelo(modeloDt.getModelo());
	//						audienciaMovimentacaoDt.setModeloSomenteLeitura(true);
	//						
	//						request.setAttribute("Id_Modelo", modeloDt.getId());
	//						request.setAttribute("Modelo", modeloDt.getModelo());
	//						request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getTextoEditor());
	//					}
	//				}														
				}
			}
			
			return audienciaMovimentacaoDt;
		
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Retirar o extrato da ata de julgamento de uma sessão do segundo grau.
	 * 
	 * @param usuarioDt
	 * @param movimentacaoProcessodt
	 * @throws Exception
	 */
	public ProcessoDt retirarExtratoAtaJulgamentoOuAcordaoOuRetornarAudienciaProcessoSessaoSegundoGrauAnalista(UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessodt) throws Exception {
		
		AudienciaDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			
			if (movimentacaoProcessodt == null || 
				movimentacaoProcessodt.getIdRedirecionaOutraServentia() == null || 
				movimentacaoProcessodt.getIdRedirecionaOutraServentia().trim().length() == 0) {			
				
				int statusOriginal = Funcoes.StringToInt(audienciaDtCompleta.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo()); 
				
				if (statusOriginal == AudienciaProcessoStatusDt.DESMARCAR_PAUTA ||
					statusOriginal == AudienciaProcessoStatusDt.RETIRAR_PAUTA ||
					statusOriginal == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO ||
					statusOriginal == AudienciaProcessoStatusDt.JULGAMENTO_INICIADO) {
					throw new MensagemException("Para retornar o processo para a Sessão de Julgamento é necessário inserir uma certidão justificando o motivo.");
				} else if (statusOriginal != AudienciaProcessoStatusDt.A_SER_REALIZADA) {
					throw new MensagemException("Para a retirada do Acórdão/Ementa e Extrato da Ata de Julgamento é necessário inserir uma certidão justificando o motivo.");	
				} else {
					throw new MensagemException("Para a retirada do Extrato da Ata de Julgamento é necessário inserir uma certidão justificando o motivo.");	
				}
			}				
						
			if (movimentacaoProcessodt.getListaProcessos() == null || 
				movimentacaoProcessodt.getListaProcessos().size() == 0 ||
				movimentacaoProcessodt.getListaProcessos().get(0) == null)
				throw new MensagemException("Processo não localizado na movimentação.");
			
			ProcessoDt processoMovimentacao = (ProcessoDt) movimentacaoProcessodt.getListaProcessos().get(0);
			
			audienciaDtCompleta = obPersistencia.consultarAudienciaProcessoCompleta(movimentacaoProcessodt.getIdRedirecionaOutraServentia());
			if (audienciaDtCompleta == null)
				throw new MensagemException("Sessão não localizada, favor repetir a operação.");	
						
			AudienciaProcessoDt audienciaProcessoDt = audienciaDtCompleta.getAudienciaProcessoDt();			
			if (audienciaProcessoDt == null || audienciaProcessoDt.getProcessoDt() == null)
				throw new MensagemException("Processo não localizado na sessão, favor repetir a operação.");
			
			if (!processoMovimentacao.getId().equalsIgnoreCase(audienciaProcessoDt.getProcessoDt().getId()))
				throw new MensagemException("Processo da movimentação não é o mesmo da sessão, verifique se existem mais de uma aba aberta e repita a operação.");
			
			if (!(Funcoes.StringToInt(audienciaDtCompleta.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && 
			     (usuarioDt.isSegundoGrau() || usuarioDt.isGabineteSegundoGrau() || usuarioDt.isGabineteUPJ()) &&
				 Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU))
				throw new MensagemException("Ação permitida somente para o perfil Analistas Judiciários 2º Grau em sessões de segundo grau.");
			
			try {
				obFabricaConexao.iniciarTransacao();
				
				// Ajusta audiência movimentação e pendências, e gera movimentação...
				new AudienciaProcessoNe().retirarExtratoAtaJulgamentoOuAcordaoOuRetornar(usuarioDt, audienciaDtCompleta, movimentacaoProcessodt, obFabricaConexao);
							
				obFabricaConexao.finalizarTransacao();
			} catch (Exception ex) {
				obFabricaConexao.cancelarTransacao();
				throw ex;
			} 
			
			return processoMovimentacao;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável por excluir a(s) audiência(s) livre(s) [agenda(s) livre(s)] Não utilizadas
	 * 
	 * @author lsbernardes
	 * @throws Exception
	 */
	public void excluirPautasAudienciasNaoUtilizadas() throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			/*
			 *   EXCLUI REGISTROS DA TABELA AUDI_PROC & EXCLUI REGISTROS DA TABELA AUDI 
			 */
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			
			obPersistencia.excluirAudienciasProcessosNaoUtilizadas();
			obPersistencia.excluirPautasAudienciasNaoUtilizadas();
			
			// FINALIZAR TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
			
		} catch(Exception e) {
			// CANCELAR TRANSAÇÃO
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEXÃO
			obFabricaConexao.fecharConexao();
		}
	}

	public boolean temAudienciaProcessoServentiaPendente(String id_Processo, String id_Serventia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean tempDt = false;
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			
			tempDt = obPersistencia.temAudienciaProcessoServentiaPendente(id_Processo,id_Serventia);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempDt;		
	}
	/**
	 * Método utilizado na redistribuição de processo do distribuidor cível para alterar a serventia de uma audiencia.
	 * Obrigatoriamente deve ser passada uma fábrica de conexão.
	 * 
	 * @param idProc
	 * @param idServentiaNova
	 * @param obFabricaConexao
	 * @throws Exception
	 * 
	 * @author hrrosa
	 */
	public void alterarServentiaAudienciaRedistribuicaoLoteDistribuidor(String idProc, String idServentiaAtual, String idServentiaNova, String idServCargoNovo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaDt audienciaDt = null;
		
		if(obFabricaConexao == null) {
			throw new MensagemException("Conexão inválida.");
		}
		if(Funcoes.StringToLong(idProc) == 0 || Funcoes.StringToLong(idServentiaNova) == 0) {
			throw new MensagemException("Parâmetros inválidos.");
		}
		
		AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
		
		if(Funcoes.StringToLong(idServCargoNovo) == 0){
			throw new MensagemException("Não foi possível destacar um novo responsável pela audiência relacionada ao processo.");
		}
		
		String valorAtual = "[Id_Processo:" + idProc + ";Id_AudienciaProcesso: LOTE_DISTRIBUIDOR;Id_Audi: LOTE_DISTRIBUIDOR]";
		String valorNovo = "[Id_AudienciaProcesso:" + idProc + ";Id_AudienciaProcesso: LOTE_DISTRIBUIDOR;Id_Audi: LOTE_DISTRIBUIDOR]";
		obPersistencia.alterarServentiaResponsavelAudienciaRedistribuicaoLoteDistribuidor(idProc, idServentiaAtual, idServentiaNova, idServCargoNovo);
		LogDt obLogDt = new LogDt("Processo",idProc, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Redistribuicao), valorAtual, valorNovo);
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}	
	
	public PendenciaDt obtemElaboracaoDeVotoSessao(ProcessoDt processoDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		PendenciaDt pendenciaDt = null;
		ArquivoDt arquivoDt = null;
		
		try{
			
			// ESTABELECER CONEXÃO COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			PendenciaNe pendNe = new PendenciaNe();
			ArquivoNe arquivoNe = new ArquivoNe();
			
	        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
			
			List<PendenciaDt> pendencias = obPersistencia.consultarPendenciasProcessoPorTipo(processoDt.getId(), PendenciaTipoDt.ELABORACAO_VOTO);
			
			// O SISTEMA SÓ PERMITE A CRIAÇÃO DE UMA ELABORAÇÃO DE VOTO POR PROCESSO, POR ISSO PODE-SE PEGAR A PRIMEIRA PENDÊNCIA DA LISTA.
			pendenciaDt = pendencias.get(0);
			PendenciaArquivoNe pendArqNe = new PendenciaArquivoNe();	
			
			List arquivos = pendArqNe.consultarArquivosPendencia(pendenciaDt.getId());
			
			if(arquivos != null) {
				
				int temp = 0;
				for(int i = 0; i < arquivos.size(); i++){				
					arquivoDt =	(ArquivoDt)arquivos.get(i);
					Integer id = Integer.valueOf(arquivoDt.getId());
					if(id > temp && !arquivoDt.isArquivoConfiguracao()){
						temp = id;
					}
				}
				
				arquivoDt = arquivoNe.consultarArquivoNaoAssinado(String.valueOf(temp));				
				List listaArquivos = new ArrayList();
				
				listaArquivos.add(arquivoDt);
					
				pendenciaDt.setListaArquivos(listaArquivos);
				
			}
			
			
			// FINALIZAR TRANSAÇÃO
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSAÇÃO
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEXÃO
			obFabricaConexao.fecharConexao();
		}


		return pendenciaDt;		
	}
	
	
	
	public List getConfiguracaoElaboracaoDeVoto(String id_pendencia) throws Exception{
		PendenciaArquivoNe pendArqNe = new PendenciaArquivoNe();	
		
		List pendenciasGerar = pendArqNe.getArquivoConfiguracaoElaboracaoVoto(id_pendencia);

		return pendenciasGerar;
	}
	
	public void descartarPreAnalise(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		   
		try{
			obFabricaConexao.iniciarTransacao();			

			descartarPreAnalise(audienciaMovimentacaoDt, usuarioDt, logDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void descartarPreAnalise(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		PendenciaResponsavelHistoricoNe penHistoricoNe = new PendenciaResponsavelHistoricoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		PendenciaDt pendenciaVoto = null;
		if (audienciaMovimentacaoDt.getPendenciaArquivoDt() != null && 
			audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia() != null && 
			audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia().trim().length() > 0) {
			pendenciaVoto = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia(), obFabricaConexao);
		}
		
		PendenciaDt pendenciaEmenta = null;
		if (audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta() != null && 
			audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId_Pendencia() != null && 
			audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId_Pendencia().trim().length() > 0) {
			pendenciaEmenta = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDtEmenta().getId_Pendencia(), obFabricaConexao);
		}	
		
		AudienciaProcessoDt audienciaProcessoDt = null;
		
		if (audienciaMovimentacaoDt.getAudienciaDt() != null &&
			audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null &&
			audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId() != null &&
			audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId().trim().length() > 0) {
			audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), obFabricaConexao);
		}
		
		if (pendenciaVoto != null) {
			if (audienciaProcessoDt == null) audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeVoto(pendenciaVoto.getId());
			
			if (pendenciaVoto.getResponsaveis() == null || pendenciaVoto.getResponsaveis().size() == 0) 
				pendenciaVoto.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaVoto));
			
			PendenciaDt pendenciaFilhaVoto = pendenciaArquivoNe.criarPendenciaFilha(pendenciaVoto, 
										                                         usuarioDt, 
										                                         logDt, 
										                                         obFabricaConexao);			
			
			if (audienciaProcessoDt != null) {
				if (pendenciaVoto.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargo())) {
					audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaFilhaVoto.getId());
					
				} else if (pendenciaVoto.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargoRedator())) {
					audienciaProcessoDt.setId_PendenciaVotoRedator(pendenciaFilhaVoto.getId());
				}
			}
						
			if (usuarioDt.isGabinetePresidenciaTjgo() || 
				usuarioDt.isGabineteVicePresidenciaTjgo() || 
				usuarioDt.isGabineteUPJ()){
				// Atualiza o Historico para pendência Filha
				penHistoricoNe.atualizaHistoricoPendenciaFilha(pendenciaFilhaVoto.getId_PendenciaPai(), 
						                                       pendenciaFilhaVoto.getId(), 
						                                       logDt.getId_Usuario(), 
						                                       logDt.getIpComputador(), 
						                                       obFabricaConexao);
			}
		}
		
		if (pendenciaEmenta != null) {
			if (audienciaProcessoDt == null) audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeEmenta(pendenciaEmenta.getId());
			
			if (pendenciaEmenta.getResponsaveis() == null || pendenciaEmenta.getResponsaveis().size() == 0) 
				pendenciaEmenta.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaEmenta));
			
			PendenciaDt pendenciaFilhaEmenta = pendenciaArquivoNe.criarPendenciaFilha(pendenciaEmenta, 
										                                              usuarioDt, 
										                                              logDt, 
										                                              obFabricaConexao);			
			
			if (pendenciaEmenta.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargo())) {
				audienciaProcessoDt.setId_PendenciaEmentaRelator(pendenciaFilhaEmenta.getId());
				
			} else if (pendenciaEmenta.isResponsavelServentiaCargo(audienciaProcessoDt.getId_ServentiaCargoRedator())) {
				audienciaProcessoDt.setId_PendenciaEmentaRedator(pendenciaFilhaEmenta.getId());
			}
						
			if (usuarioDt.isGabinetePresidenciaTjgo() || 
				usuarioDt.isGabineteVicePresidenciaTjgo() || 
				usuarioDt.isGabineteUPJ()){
				// Atualiza o Historico para pendência Filha
				penHistoricoNe.atualizaHistoricoPendenciaFilha(pendenciaFilhaEmenta.getId_PendenciaPai(), 
						                                       pendenciaFilhaEmenta.getId(), 
						                                       logDt.getId_Usuario(), 
						                                       logDt.getIpComputador(), 
						                                       obFabricaConexao);
			}
		}
		
		audienciaProcessoNe.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
	}

	public void descartarPreAnalise(PendenciaArquivoDt pendenciaArquivoVotoDt, PendenciaArquivoDt pendenciaArquivoEmentaDt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		PendenciaDt pendenciaVoto = null;
		if (pendenciaArquivoVotoDt != null && pendenciaArquivoVotoDt.getId_Pendencia() != null && pendenciaArquivoVotoDt.getId_Pendencia().trim().length() > 0) {
			pendenciaVoto = pendenciaNe.consultarId(pendenciaArquivoVotoDt.getId_Pendencia());
		}
		
		PendenciaDt pendenciaEmenta = null;
		if (pendenciaArquivoEmentaDt != null && pendenciaArquivoEmentaDt.getId_Pendencia() != null && pendenciaArquivoEmentaDt.getId_Pendencia().trim().length() > 0) {
			pendenciaEmenta = pendenciaNe.consultarId(pendenciaArquivoEmentaDt.getId_Pendencia());
		}		
		
		List pendenciasDescartar = new ArrayList();
		if (pendenciaVoto != null) pendenciasDescartar.add(pendenciaVoto);		
		if (pendenciaEmenta != null) pendenciasDescartar.add(pendenciaEmenta);
		
		if (pendenciasDescartar.size() > 0) {
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();	
			
			pendenciaArquivoNe.descartarPreAnalise(pendenciasDescartar, usuarioDt, logDt);	
		}
	}
	
	public void descartarPreAnalise(PendenciaArquivoDt pendenciaArquivoVotoDt, PendenciaArquivoDt pendenciaArquivoEmentaDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao fabrica) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		PendenciaDt pendenciaVoto = null;
		if (pendenciaArquivoVotoDt != null && pendenciaArquivoVotoDt.getId_Pendencia() != null && pendenciaArquivoVotoDt.getId_Pendencia().trim().length() > 0) {
			pendenciaVoto = pendenciaNe.consultarId(pendenciaArquivoVotoDt.getId_Pendencia());
		}
		
		PendenciaDt pendenciaEmenta = null;
		if (pendenciaArquivoEmentaDt != null && pendenciaArquivoEmentaDt.getId_Pendencia() != null && pendenciaArquivoEmentaDt.getId_Pendencia().trim().length() > 0) {
			pendenciaEmenta = pendenciaNe.consultarId(pendenciaArquivoEmentaDt.getId_Pendencia());
		}		
		
		List pendenciasDescartar = new ArrayList();
		if (pendenciaVoto != null) pendenciasDescartar.add(pendenciaVoto);		
		if (pendenciaEmenta != null) pendenciasDescartar.add(pendenciaEmenta);
		
		if (pendenciasDescartar.size() > 0) {
			PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();	
			
			pendenciaArquivoNe.descartarPreAnalise(pendenciasDescartar, usuarioDt, logDt, fabrica);	
		}	
	}

	public boolean isAudienciaVirtualIniciada(String idAudiencia) throws Exception {
		FabricaConexao obFabricaConexao = null;
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			
			return obPersistencia.isVirtualIniciada(idAudiencia);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public boolean isAudienciaVirtual(String idAudiencia) throws Exception {
		FabricaConexao obFabricaConexao = null;
						
		try {		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);			
			return this.isAudienciaVirtual(idAudiencia, obFabricaConexao);				
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public boolean isAudienciaVirtual(String idAudiencia, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
		return obPersistencia.isVirtual(idAudiencia);
	}
	
	// jvosantos - 10/01/2020 12:29 - Extrair código repetido
	private boolean existeVotante(List<VotanteDt> integrantes, String idServCargo, int votanteTipo) {
		return integrantes.stream().anyMatch(integrante -> integrante
				.getIdServentiaCargo().contains(idServCargo)
				&& integrante.getVotanteTipoDt().getVotanteTipoCodigoInt() == votanteTipo);
	}
	
	public void iniciarJulgamentoVirtual(AudienciaDt audienciaDt, UsuarioDt usuarioDt, String idServCargoPresidenteSessao, String idServCargoMp) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			obPersistencia.iniciarJulgamentoVirtual(audienciaDt);
			
			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
					
			AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
			List<AudienciaProcessoDt> listaAudienciaProcessoDt = audienciaProcessoNe.consultarAudienciaProcessos(audienciaDt.getId(), usuarioDt, true);
			VotoNe votoNe = new VotoNe();		
			VotanteTipoNe votanteTipoNe = new VotanteTipoNe();
			ImpedimentoTipoNe impedimentoTipoNe = new ImpedimentoTipoNe();
			AudienciaProcessoVotantesDt audienciaProcessoVotantesDt;
			String idVotanteTipoPresidente = votanteTipoNe.consultarVotanteTipoCodigo(String.valueOf(VotanteTipoDt.PRESIDENTE_SESSAO)).getId();
			String idVotanteTipoMinisterioPublico = votanteTipoNe.consultarVotanteTipoCodigo(String.valueOf(VotanteTipoDt.MINISTERIO_PUBLICO)).getId();
			
			for (AudienciaProcessoDt audienciaProcessoDt : listaAudienciaProcessoDt) {
				audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoDt, audienciaProcessoDt.getAudienciaProcessoStatusCodigo(), 
						audienciaProcessoDt.getAudienciaProcessoStatusAnalista(), audienciaProcessoDt.getAudienciaProcessoStatusCodigoAnalista(), audienciaProcessoDt.getId_ArquivoAta(), 
						false, false, idServCargoPresidenteSessao, idServCargoMp, audienciaProcessoDt.getId_ServentiaCargoRedator(), logDt, obFabricaConexao);
				//lrcampos 19/12/2019 15:54 - Refatorando o código duplicado, e adicionando condição para salvar Presidente e Ministerio Publico.
				List<VotanteDt> integrantes = votoNe.consultarTodosVotantesSessaoCompletoDeVerdade(audienciaProcessoDt.getId(), obFabricaConexao);
				boolean existePresidenteSessao = existeVotante(integrantes, idServCargoPresidenteSessao, VotanteTipoDt.PRESIDENTE_SESSAO);
				boolean existeMinisterioPublico = existeVotante(integrantes, idServCargoMp, VotanteTipoDt.MINISTERIO_PUBLICO);
				
				if (!existePresidenteSessao){
					adicionaVotante(idServCargoPresidenteSessao, obPersistencia, votanteTipoNe,
							impedimentoTipoNe, audienciaProcessoDt, idVotanteTipoPresidente);
				}			
				if (!existeMinisterioPublico){
					// jvosantos - 10/01/2020 12:23 - Correção para adicionar o MP e não o Presidente
					adicionaVotante(idServCargoMp, obPersistencia, votanteTipoNe,
							impedimentoTipoNe, audienciaProcessoDt, idVotanteTipoMinisterioPublico);
				}			
			}				
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSAÇÃO
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEXÃO
			obFabricaConexao.fecharConexao();
		}
		
	}
	/** 
	 * Método para adicionar um votante e automaticamente o torna não impedido.
	 * @author lrcampos
	 * @since 19/12/2019 15:57
	 * @param servCargo
	 * @param audienciaPs
	 * @param votanteTipoNe
	 * @param impedimentoTipoNe
	 * @param audienciaProcessoDt
	 * @param tipo
	 * @throws Exception
	 */
	private void adicionaVotante(String servCargo, AudienciaPs audienciaPs, VotanteTipoNe votanteTipoNe,
			ImpedimentoTipoNe impedimentoTipoNe, AudienciaProcessoDt audienciaProcessoDt, String votanteTipo) throws Exception {
		AudienciaProcessoVotantesDt audienciaProcessoVotantesDt;
		audienciaProcessoVotantesDt = new AudienciaProcessoVotantesDt();
		audienciaProcessoVotantesDt.setId_AudienciaProcesso(audienciaProcessoDt.getId());
		audienciaProcessoVotantesDt.setId_ServentiaCargo(servCargo);
		audienciaProcessoVotantesDt.setRelator(false);
		audienciaProcessoVotantesDt.setId_ImpedimentoTipo(impedimentoTipoNe.consultarImpedimentoTipoCodigo(String.valueOf(ImpedimentoTipoDt.NAO_IMPEDIDO)).getId());
		audienciaProcessoVotantesDt.setOrdemVotante(String.valueOf(99));
		audienciaProcessoVotantesDt.setId_VotanteTipo(votanteTipo);
		audienciaPs.cadastrarVotantesSessaoVirtual(audienciaProcessoVotantesDt);
	}
	
	public void finalizarVotosPendentes(String[] ids, UsuarioNe usuario) throws Exception {
		VotoNe votoNe = new VotoNe();
		for(String id : ids) {
			votoNe.finalizarVotosPendentes(id, usuario.getUsuarioDt());
		}
	}

	public void finalizarVotosPendentes(AudienciaProcessoDt audienciaProcesso, UsuarioNe usuario) throws Exception {
		VotoNe votoNe = new VotoNe();
		votoNe.finalizarVotosPendentes(audienciaProcesso, usuario.getUsuarioDt());
	}

	public AudienciaDt consultarAudienciaCompleta(String id_Audiencia, UsuarioNe usuarioNe) throws Exception {
		AudienciaDt audienciaDt = consultarAudienciaCompleta(id_Audiencia);
		if(usuarioNe.isAnalistaVara() && audienciaDt.isVirtual()) {
			new VotoNe().verificarPermissoes(audienciaDt);
			
		}
		return audienciaDt;
		
	}
	
	public List consultarAudienciasPresenciais(String idServentia) throws Exception {
		List listaAudienciasPresenciais = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			listaAudienciasPresenciais =  obPersistencia.consultarAudienciasPresenciais(idServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasPresenciais;
	}

	public void remarcarSessaoSusOral(AudienciaDt novaAudiencia, String idAudiProc, AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuarioDt, String idPendenciaSusOral) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			LogDt logDt = new LogDt(usuarioDt.getId(), novaAudiencia.getId_UsuarioLog());
			
			audienciaMovimentacaoDt.setAudienciaStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL));
			PendenciaNe pendenciaNe = new PendenciaNe();
				
			audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("4");
			AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
		    
			ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
			audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
					
			// Cria a nova Audiência Processo
			obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), novaAudiencia, logDt, obFabricaConexao);
			// Gera a movimentação no Processo
			gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), true, novaAudiencia, logDt, obFabricaConexao);				

			VotoNe votoNe = new VotoNe();
			//mrbatista - 11/10/2019 14:06 - Finalizando as pendencias de SO PELO ID_AUDI_PROC.
			//mrbatista - 26/09/2019 12:01 - Finalizando as pendencias de SO PELO ID_PROC.
			votoNe.finalizarPendenciasSO(audienciaDt.getAudienciaProcessoDt().getId(), usuarioDt, obFabricaConexao);

			//mrbatista - 11/10/2019 14:04 - Finalizar todas as pendencias da votação
			votoNe.finalizarTodasPendenciasVotacao(audienciaDt.getAudienciaProcessoDt(), usuarioDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public List consultarProximasAudiencias(String idServentia) throws Exception {
		List listaAudiencias = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			listaAudiencias =  obPersistencia.consultarProximasAudiencias(idServentia);
			return listaAudiencias;
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public AudienciaDt consultarId(String id_Audiencia, FabricaConexao fabrica) throws Exception {
		AudienciaPs obPersistencia = new AudienciaPs(fabrica.getConexao());
		return obPersistencia.consultarId(id_Audiencia);
	}

	public String consultarObservacoes(String id_Audiencia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarObservacoes(id_Audiencia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}	
	
	public String consultarProcessoTipoRecursoSecundarioIdProcesso(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarProcessoTipoRecursoSecundarioIdProcesso(idProcesso);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}

	public List<ProcessoTipoDt> consultarDescricaoProcessoTipoRecursoSecundario(String areaCodigo, String serventiaSubtipoCodigo) throws Exception {
		
		List<Integer> tipos = new ArrayList<>();
		tipos.addAll(Arrays.asList( ProcessoTipoDt.EMBARGOS_DECLARACAO ));
		boolean isEspecial = Funcoes.StringToInt(serventiaSubtipoCodigo) == ServentiaSubtipoDt.CORTE_ESPECIAL;
		
		if(areaCodigo.equals(AreaDt.CIVEL) || isEspecial) {
			tipos.add((ProcessoTipoDt.AGRAVO_INTERNO));
		} 
		if(areaCodigo.equals(AreaDt.CRIMINAL) || isEspecial) {
			tipos.add(ProcessoTipoDt.AGRAVO_REGIMENTAL);
			// alteração dos tipos para recurso secundário
		}
		return new ProcessoTipoNe().consultarPorCodigos(tipos);
	}

	public AudienciaDt consultarAudienciaCompletaExcetoFinalizados(String id_Audiencia, UsuarioNe usuarioNe) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaCompletaExcetoFinalizados(id_Audiencia);			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}
	
	// jvosantos - 05/07/2019 15:29 - Adicionar parâmetro para corrigir o erro de não trazer alguns processo
	public AudienciaDt consultarAudienciaCompletaExcetoFinalizadosSessaoVirtual(String id_Audiencia, UsuarioNe usuarioNe) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaCompletaExcetoFinalizadosSessaoVirtual(id_Audiencia);
			new VotoNe().setDadosAudienciaCompleta(audienciaDtCompleta, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}

	// jvosantos - 04/06/2019 09:50 - Método que consulta o arquivo da ementa
	public ArquivoDt consultarArquivoEmenta(String idAudiProc) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		VotoNe votoNe = new VotoNe();
		
		try{

			String idPend = votoNe.consultarIdProclamacao(idAudiProc);
			List<ArquivoDt> arquivos = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(idPend);
			
			if(arquivos != null) {
				for(ArquivoDt arq : arquivos) {
					if(StringUtils.equals(arq.getArquivoTipoCodigo(), String.valueOf(ArquivoTipoDt.EMENTA))) return arq;
				}
			}
			
			return null;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public ArquivoDt consultarArquivoVoto(String idAudiProc) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		VotoNe votoNe = new VotoNe();
		
		try{

			String idPend = votoNe.consultarIdProclamacao(idAudiProc);
			List<ArquivoDt> arquivos = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(idPend);

			if(arquivos != null) {
				for(ArquivoDt arq : arquivos) {
					if(StringUtils.equals(arq.getArquivoTipoCodigo(), String.valueOf(ArquivoTipoDt.RELATORIO_VOTO))) return arq;
				}
			}
			
			return null;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String salvarApreciadosAudienciaVirtualProcessoSessaoSegundoGrau(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioNe usuario) throws Exception {
		return salvarApreciadosAudienciaVirtualProcessoSessaoSegundoGrau(audienciaMovimentacaoDt, usuario, null);
	}

	// jvosantos - 04/06/2019 09:50 - Método que salva os apreciados
	public String salvarApreciadosAudienciaVirtualProcessoSessaoSegundoGrau(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioNe usuario, FabricaConexao fabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		VotoNe votoNe = new VotoNe();
		
		List arquivos = audienciaMovimentacaoDt.getListaArquivos();
		FabricaConexao fabrica = null;
		
		if(!audienciaMovimentacaoDt.isApreciados()) return "Não é apreciados.";
		
		if(!audienciaMovimentacaoDt.isPreAnalise() && (arquivos == null || arquivos.isEmpty())) return "É necessário enviar um voto e uma ementa.";
		if(StringUtils.isEmpty(audienciaMovimentacaoDt.getAudienciaStatusCodigo())) return "É necessário selecionar uma situação.";

		if(fabricaConexao != null)
			fabrica = fabricaConexao;
		else {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
		}

		try {
			PendenciaDt pendenciaApreciados = pendenciaNe.consultarPendenciaApreciados(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), fabrica);
			//lrcampos 13/03/2020 18:00 - Verifica usuario logado é responsavel pela pendencia
			if(arquivos != null)
				pendenciaNe.verificaResponsavelPendencia(pendenciaApreciados.getId(), usuario.getUsuarioDt());
			
			if(pendenciaApreciados == null) return "Essa pendência de apreciados não existe mais.";
			
			if(arquivos != null) pendenciaArquivoNe.inserirArquivos(pendenciaApreciados, arquivos, false, audienciaMovimentacaoDt.isPendenteAssinatura(), new LogDt(usuario.getId_Usuario(), usuario.getIpComputadorLog()), fabrica);
			
			audienciaProcessoNe.alterarStatusAudienciaTemp(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), audienciaMovimentacaoDt.getAudienciaStatusCodigo(), fabrica);
			audienciaProcessoNe.alterarPendenciaVotoEmenta(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), pendenciaApreciados.getId(), pendenciaApreciados.getId(), fabrica);
			
			if(!audienciaMovimentacaoDt.isPreAnalise()) {
				Funcoes.preencheUsuarioLog(pendenciaApreciados, usuario.getUsuarioDt());
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String data = formatter.format(LocalDateTime.now());
				pendenciaApreciados.setDataFim(data);
				pendenciaApreciados.setDataVisto(data);
				pendenciaApreciados.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
				pendenciaApreciados.setId_UsuarioFinalizador(usuario.getId_UsuarioServentia());
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaApreciados, usuario.getUsuarioDt(), fabrica);
				
				//AudienciaProcessoStatusDt status = (new AudienciaProcessoStatusNe()).consultarAudienciaProcessoStatusPorCodigo(audienciaMovimentacaoDt.getAudienciaStatusCodigo());
				
				//if(status == null) throw new Exception("Não foi encontrado o status selecionado.");
				
				//audienciaProcessoNe.alterarStatusAudiencia(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt(), Integer.parseInt(status.getId()), new LogDt(usuario.getId_Usuario(), usuario.getIpComputadorLog()), fabrica);
				
				votoNe.gerarResultadoVotacao(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt(), usuario.getUsuarioDt(), fabrica);
				
				if(audienciaMovimentacaoDt.isVerificarProcesso()) pendenciaNe.gerarPendenciaVerificarProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId(),  usuario.getId_UsuarioServentia(), audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId_Serventia(), null, null, null, usuario.getId_Usuario(), usuario.getIpComputadorLog(), fabrica, null);
				
			}else {
				pendenciaApreciados.setPendenciaStatusCodigo(!audienciaMovimentacaoDt.isPendenteAssinatura() ? String.valueOf(PendenciaStatusDt.ID_PRE_ANALISADA) : String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));
				
				pendenciaNe.alterarStatus(pendenciaApreciados, fabrica);
				
				salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauPreAnaliseDesembargadorAssistente(audienciaMovimentacaoDt, usuario.getUsuarioDt(), new LogDt(usuario.getId_Usuario(), usuario.getIpComputadorLog()), fabrica);
			}
			
			if(fabricaConexao == null)
				fabrica.finalizarTransacao();
		} catch (MensagemException e) {
			if(fabricaConexao == null)
				fabrica.cancelarTransacao();
			return e.getMessage();
		}catch (Exception e) {
			if(fabricaConexao == null)
				fabrica.cancelarTransacao();
			throw e;
		} finally {
			if(fabricaConexao == null)
				fabrica.fecharConexao();
		}
		return "";
	}

	public void desfazerPreAnaliseApreciados(String idAudienciaProcesso, UsuarioNe usuarioNe) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		VotoNe votoNe = new VotoNe();
		
		FabricaConexao fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			fabrica.iniciarTransacao();
			
			AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(idAudienciaProcesso, fabrica);
			
			if(isVirtual(audienciaProcessoDt.getId_Audiencia(), fabrica))
				desfazerPreAnaliseApreciadosVirtual(idAudienciaProcesso, pendenciaNe, audienciaProcessoNe, fabrica);
			else
				desfazerPreAnaliseApreciadosPresencial(audienciaProcessoDt, usuarioNe, pendenciaNe, pendenciaArquivoNe, fabrica);
			
			fabrica.finalizarTransacao();
		} catch (Exception e) {
			fabrica.cancelarTransacao();
			throw e;
		} finally {
			fabrica.fecharConexao();
		}
		return;
	}

	protected void desfazerPreAnaliseApreciadosPresencial(AudienciaProcessoDt audienciaProcessoDt, UsuarioNe usuarioNe,
			                                              PendenciaNe pendenciaNe, PendenciaArquivoNe pendenciaArquivoNe, FabricaConexao fabrica) throws Exception {
		LogDt logDt = new LogDt(usuarioNe.getId_Usuario(), usuarioNe.getIpComputadorLog());
		
		PendenciaDt voto = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaVotoRelator());
		PendenciaDt ementa = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaEmentaRelator());
		
		PendenciaArquivoDt votoPendArq = consultarPendenciaArquivosDoTipo(voto, pendenciaArquivoNe, ArquivoTipoDt.RELATORIO_VOTO);
		PendenciaArquivoDt ementaPendArq = consultarPendenciaArquivosDoTipo(ementa, pendenciaArquivoNe, ArquivoTipoDt.EMENTA);

		descartarPreAnalise(votoPendArq, ementaPendArq, usuarioNe.getUsuarioDt(), logDt, fabrica);
	}

	protected PendenciaArquivoDt consultarPendenciaArquivosDoTipo(PendenciaDt voto,
			PendenciaArquivoNe pendenciaArquivoNe, int arquivoTipoCodigo) throws Exception {
		return (PendenciaArquivoDt) pendenciaArquivoNe.consultarPendencia(voto, true).stream().filter(filtrarArquivoTipo(arquivoTipoCodigo)).findAny().orElse(null);
	}

	private Predicate<? super PendenciaArquivoDt> filtrarArquivoTipo(int arquivoTipoCodigo) {
		return x -> Funcoes.StringToInt(x.getArquivoDt().getArquivoTipoCodigo()) == arquivoTipoCodigo;
	}

	protected void desfazerPreAnaliseApreciadosVirtual(String idAudienciaProcesso, PendenciaNe pendenciaNe,
			AudienciaProcessoNe audienciaProcessoNe, FabricaConexao fabrica) throws Exception {
		PendenciaDt pendenciaApreciados = pendenciaNe.consultarPendenciaApreciados(idAudienciaProcesso, fabrica);
		
		if(pendenciaApreciados == null) return;
		
		pendenciaApreciados.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
		
		audienciaProcessoNe.alterarStatusAudienciaTemp(idAudienciaProcesso, String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA), fabrica);
		
		// jvosantos - 19/11/2019 16:56 - Remover código errado e morto (exposto pela tipagem da lista PendenciaArquivoNe.consultarArquivosPendencia)

		pendenciaNe.alterarStatus(pendenciaApreciados, fabrica);
	}
	
	// jvosantos - 24/09/2019 15:56 - Método responsavel por alterar o presidente de uma audiência
	public void alterarPresidenteSessao(String id_Audiencia, String Id_NovoServentiaCargoPresidente, UsuarioNe usuarioNe) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		if(StringUtils.isEmpty(id_Audiencia) || StringUtils.isEmpty(Id_NovoServentiaCargoPresidente))
			throw new Exception(" ID_Audi ou ID_Serv_Cargo do presidente está/estão vazio(s). ID_AUDI = "+id_Audiencia+"; ID_SERV_CARGO_PRESIDENTE = "+Id_NovoServentiaCargoPresidente);
		
		try {
			fabricaConexao.iniciarTransacao();
			
			AudienciaDt audienciaDt = consultarAudienciaCompletaSessaoVirtual(id_Audiencia, fabricaConexao);
			
			if(audienciaDt == null)
				throw new Exception("A audiência não foi encontrada! ID_AUDI = "+id_Audiencia);
			
			if(!audienciaDt.isVirtual())
				throw new Exception("Essa audiência não é virtual! ID_AUDI = "+id_Audiencia);
			
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(Id_NovoServentiaCargoPresidente, fabricaConexao);
			
			if(serventiaCargoDt == null)
				throw new Exception("O novo presidente não foi encontrado! ID_SERV_CARGO_PRESIDENTE = "+Id_NovoServentiaCargoPresidente);
			
			
			
			
			AlterarIntegranteTemplate alterarIntegranteTemplate = new AlterarPresidenteTemplate(fabricaConexao, usuarioNe);
			
			for(AudienciaProcessoDt ap : audienciaDt.getListaAudienciaProcessoDt()) { // jvosantos - 30/09/2019 18:15 - Remover cast
				
				alterarIntegranteTemplate.alterar(ap, serventiaCargoDt);
				
			}
			fabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();			
		}
	}

	// jvosantos - 24/09/2019 15:56 - Método responsavel por alterar o presidente de uma audiência processo
	private void alterarPresidenteAudienciaProcesso(AudienciaProcessoDt ap, ServentiaCargoDt novoPresidente, UsuarioNe usuarioNe, FabricaConexao fabricaConexao) throws Exception {
		if(StringUtils.equals(ap.getId_ServentiaCargoPresidente(), novoPresidente.getId())) return;
		VotoNe votoNe = new VotoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		boolean criarPendencia = false;
		
		List<VotoDt> votos = votoNe.consultarTodosVotosSessao(ap.getId(), fabricaConexao);
				
		List<VotoDt> observacoes = votos != null ? votos.stream().filter(x -> StringUtils.equals(x.getVotoTipoCodigo(), String.valueOf(VotoTipoDt.OBSERVACAO))).collect(Collectors.toList()) : null;
		
		if(observacoes != null && !observacoes.isEmpty()) {   
			
			for(VotoDt ob : observacoes) {
				List<PendenciaResponsavelDt> responsaveis = pendenciaResponsavelNe.consultarResponsaveis(ob.getPendenciaDt().getId(), fabricaConexao);
				List responsaveisFinais = pendenciaResponsavelNe.consultarResponsaveisFinais(ob.getPendenciaDt().getId(), fabricaConexao);
				
				if(responsaveisFinais != null) {
					responsaveis.addAll(responsaveisFinais);
					responsaveisFinais.clear();
					responsaveisFinais = null;
				}
				
				if(responsaveis.stream().filter(x -> StringUtils.equals(x.getId_ServentiaCargo(), ap.getId_ServentiaCargoPresidente())).findAny().isPresent()) {
					votoNe.excluirVoto(ob.getId(), fabricaConexao);
					criarPendencia = true;
				}
			}
		}
		
		List<PendenciaDt> pendenciasAudienciaProcesso = pendenciaNe.consultarPendenciasProcessoPorTipo(ap.getProcessoDt().getId(), PendenciaTipoDt.SESSAO_CONHECIMENTO);
		
		for(PendenciaDt p : pendenciasAudienciaProcesso) {
			List<PendenciaResponsavelDt> responsaveis = pendenciaResponsavelNe.consultarResponsaveis(p.getId(), fabricaConexao);

			if(responsaveis.stream().filter(x -> StringUtils.equals(x.getId_ServentiaCargo(), ap.getId_ServentiaCargoPresidente())).findAny().isPresent()) {
				votoNe.setInfoPendenciaFinalizar(p, usuarioNe.getUsuarioDt(), fabricaConexao);
				criarPendencia = true;
			}
		}

		AudienciaProcessoVotantesDt presidenteNovo = new AudienciaProcessoVotantesDt();
		
		List<VotanteDt> votantes = votoNe.consultarTodosVotantesSessaoCompletoDeVerdade(ap.getId(), fabricaConexao);
		Optional<VotanteDt> presidenteAntigo = votantes != null ? votantes.stream().filter(x -> x.getVotanteTipoDt().getVotanteTipoCodigoInt() == VotanteTipoDt.PRESIDENTE_SESSAO).findAny() : Optional.empty();
		
		if(presidenteAntigo.isPresent()) 
			votoNe.excluirVotante(presidenteAntigo.get().getId(), fabricaConexao);
		
		presidenteNovo.setId_AudienciaProcesso(ap.getId());
		presidenteNovo.setId_ServentiaCargo(novoPresidente.getId());
		presidenteNovo.setOrdemVotante("99");
		presidenteNovo.setConvocado(false);
		presidenteNovo.setRelator(false);
		presidenteNovo.setVotanteTipoCodigo(String.valueOf(VotanteTipoDt.PRESIDENTE_SESSAO));
		
		audienciaProcessoNe.cadastrarVotante(presidenteNovo, fabricaConexao);
		
		ap.setId_ServentiaCargoPresidente(novoPresidente.getId());
		
		// jvosantos - 27/09/2019 11:27 - Adicionar informações de Log
		ap.setIpComputadorLog(usuarioNe.getIpComputadorLog());
		ap.setId_UsuarioLog(usuarioNe.getId_Usuario());
		
		audienciaProcessoNe.salvar(ap, fabricaConexao);
		
		if(criarPendencia)
			pendenciaNe.gerarPendenciaSessaoConhecimento(novoPresidente.getId(), usuarioNe.getUsuarioDt(), ap.getId_Processo(), ap.getId(), fabricaConexao);
	}
	
	// mrbatista - 24/09/2019 15:56 - Método responsavel por alterar o presidente de uma audiência
	public void alterarResponsavelMp(String id_Audiencia, String Id_NovoServentiaCargoMP, UsuarioNe usuarioNe) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		if(StringUtils.isEmpty(id_Audiencia) || StringUtils.isEmpty(Id_NovoServentiaCargoMP))
			throw new Exception(" ID_Audi ou ID_Serv_Cargo do responsável do mp está/estão vazio(s). ID_AUDI = "+id_Audiencia+"; ID_SERV_CARGO_MP = "+Id_NovoServentiaCargoMP);
		
		try {
			fabricaConexao.iniciarTransacao();
			
			AudienciaDt audienciaDt = consultarAudienciaCompletaSessaoVirtual(id_Audiencia, fabricaConexao);
			
			if(audienciaDt == null)
				throw new Exception("A audiência não foi encontrada! ID_AUDI = "+id_Audiencia);
			
			if(!audienciaDt.isVirtual())
				throw new Exception("Essa audiência não é virtual! ID_AUDI = "+id_Audiencia);
			
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(Id_NovoServentiaCargoMP, fabricaConexao);
			
			if(serventiaCargoDt == null)
				throw new Exception("O novo responsável do MP não foi encontrado! ID_SERV_CARGO_MP = "+Id_NovoServentiaCargoMP);
			
			
			
			AlterarIntegranteTemplate alterarIntegranteTemplate = new AlterarMPTemplate(fabricaConexao, usuarioNe);
			
			for(AudienciaProcessoDt ap : audienciaDt.getListaAudienciaProcessoDt()) { 
				
				alterarIntegranteTemplate.alterar(ap, serventiaCargoDt);
				
			}
			fabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();			
		}
	}

	private String salvarMovimentacaoAudienciaProcessoSessaoSegundoGrauAnalistaTemp(AudienciaMovimentacaoDt audienciaMovimentacaoDt, String Id_ServentiaCargoPresidente, String Id_ServentiaCargoMP, boolean ehVotoVencido ,UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		List arquivos = null;
		List movimentacoes = new ArrayList();
		String retornoMensagemInconsistencia = "";
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();	
		try{
			
			int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
	        codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());	        
	        if (codigoStatus == AudienciaProcessoStatusDt.DESMARCAR_PAUTA || codigoStatus == AudienciaProcessoStatusDt.RETIRAR_PAUTA || codigoStatus == AudienciaProcessoStatusDt.REMARCADA){
	        	salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, null, usuarioDt);
	        	
	        	if (audienciaMovimentacaoDt != null &&
	        	    audienciaMovimentacaoDt.getAudienciaDt() != null &&
	        	    audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null &&
	        	    (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoEEmentaRelator())) {
	        		
	        		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiVotoRelator()) {
	        			PendenciaDt pendenciaDtVoto = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaVotoRelator());
	        			if (pendenciaDtVoto != null) {
	        				pendenciaNe.descartarPendencia(pendenciaDtVoto, usuarioDt);
	        			}
	        		}
	        		
	        		if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().possuiEmentaRelator()) {
	        			PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarId(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_PendenciaEmentaRelator());
	        			if (pendenciaDtEmenta != null) {
	        				pendenciaNe.descartarPendencia(pendenciaDtEmenta, usuarioDt);
	        			}
	        		}
	        	}
	        	
	        	return retornoMensagemInconsistencia;
	        } 
	        else if (codigoStatus == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO)
	        {
	        	audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("4");
	        } 
	        else if (codigoStatus == AudienciaProcessoStatusDt.JULGAMENTO_INICIADO)
	        {
	        	audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("3");
	        } 
	        AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
	        
	        // Consulta dados completos do processo, pois será necessário ao salvar responsáveis pela audiência e ao gerar pendências
	        ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
			audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
					
	        MovimentacaoDt movimentacaoDt = null;
	        AudienciaProcessoDt audienciaProcessoNovaDt = null;
	        String idAudienciaProcesso = audienciaDt.getAudienciaProcessoDt().getId();
	        // Chama método para gerar a movimentação de início ou adiamento de sessão...			
			if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
				AudienciaDt audienciaNovaDt = null;
				if (!audienciaMovimentacaoDt.isAlteracaoExtratoAta()) 
				{
					//Obtem a nova sessão...
					AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
					audienciaNovaDt = Optional.ofNullable(StringUtils.isEmpty(audienciaMovimentacaoDt.getIdProximaAudiencia()) ?
							obPersistencia.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), true) :
								obPersistencia.consultarId(audienciaMovimentacaoDt.getIdProximaAudiencia()))
							.orElseThrow(() -> new MensagemException("Não foi localizada uma sessão aberta com data posterior."));
					// Verifica se existe uma Próxima Sessão Aberta.
					if (audienciaNovaDt == null){
						retornoMensagemInconsistencia = "Não foi localizada uma sessão aberta com data posterior.";
						return retornoMensagemInconsistencia;
					}			
					// Cria a nova Audiência Processo
					audienciaProcessoNovaDt = obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaNovaDt, logDt, obFabricaConexao);
					idAudienciaProcesso = audienciaProcessoNovaDt.getId();
				} else {
					audienciaNovaDt = audienciaDt;
					audienciaProcessoNovaDt = audienciaDt.getAudienciaProcessoDt();
				}
				// Gera a movimentação no Processo			
				movimentacaoDt = gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), audienciaNovaDt, logDt, obFabricaConexao);				
			}							
						
			arquivos = audienciaMovimentacaoDt.getListaArquivos();
			String Id_ArquivoAta = "";
			// Salva arquivos inseridos
			if (arquivos != null && arquivos.size() > 0) {
				movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
				ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
				// Salvando vínculos do arquivo de Ata com a Audiência Processo
				Id_ArquivoAta = arquivoDt.getId();										
			}		
			
			//Vincula os arquivos, dependendo do tipo da movimentação
			if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
				//Vincula o arquivo iserido á Audiência Processo
				audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoNovaDt, "", "", "", Id_ArquivoAta, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada(), "", "", "",logDt, obFabricaConexao);
			} else {
				//Chama método para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimentação correspondente a movimentação da audiência e inserir responsáveis pela audiência
				movimentacaoDt = movimentarAudienciaAnalistaSegundoGrau(audienciaDt, audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), audienciaMovimentacaoDt.getAudienciaStatus() ,audienciaMovimentacaoDt.getAudienciaStatusCodigo(), Id_ArquivoAta, Id_ServentiaCargoPresidente, Id_ServentiaCargoMP, audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), usuarioDt, logDt, obFabricaConexao);				
			}			
			movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
			movimentacoes.add(movimentacaoDt);	 					
			
			String visibilidade=null;
			if (processoDt.isSegredoJustica()){
				visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
			}
			
			// Salvando vínculo entre movimentação e arquivos inseridos
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);			

			// Salvando pendências da movimentação
			if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) 
				pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);			

			// Atualiza Classificador processo
			if (audienciaMovimentacaoDt.getId_Classificador().length() > 0)
				(new ProcessoNe()).alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);			

//			// Gera recibo para arquivos de movimentações
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
						
			// Cria publicação
			// Decreto 1.684 / 2020, Art 2o e 3o
			if (!processoDt.isSigiloso()) {
				pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
			}
			
    		 // Se for voto vencido iremos gerar uma conclusão do tipo voto/ementa para o desembargador redator poder pré-analisar
	        if (ehVotoVencido){
	        	// Finalizar a pendência de ementa do desembargador com voto vencido
	        	finalizarPendenciaEmentaRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), pendenciaNe, usuarioDt, audienciaDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
	        	// Alterar pendência de voto para o desembargador inserir o voto vencido
				gerarPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), true, pendenciaNe, usuarioDt, obFabricaConexao, null, idAudienciaProcesso);
	        	// Gerar pendência de voto para o desembargador inserir o voto vencedor
				gerarPendenciaVoto(audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, null, idAudienciaProcesso);					
	        } else {
		        // Gerar pendência de voto para o desembargador inserir o voto, caso não exista...
				gerarPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, null, audienciaDt.getAudienciaProcessoDt().getId());
	        }
    		
    		return retornoMensagemInconsistencia;
		} catch(MensagemException m){
	        throw m;
		
		}
	}
}
