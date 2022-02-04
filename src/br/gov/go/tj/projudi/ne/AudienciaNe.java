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
	 * CRIA��O DE AGENDA(S) PARA AUDI�NCIAS
	 * 
	 * M�todo respons�vel por abrir uma conex�o com o banco de dados para inserir ou alterar uma lista de objetos do tipo "AudienciaDt" cada qual
	 * contendo objeto(s) do tipo "AudienciaProcessoDt", ou seja, criar agenda(s) para audi�ncias dos tipos: concilia��o, instru��o, preliminar, una.
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

			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			// INICIAR TRANSA��O
			obFabricaConexao.iniciarTransacao();

			// SET CONEX�O
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// GERAR DATAS PARA AGENDAS DE AUDI�NCIAS
			List listaDatasAgendasAudiencias = this.gerarDatasAgendasAudiencias(audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getDataFinal(), audienciaAgendaDt.getQuantidadeAudienciasSimultaneas(), horariosDuracao);

			// GERAR AGENDAS DE AUDI�NCIAS
			//List listaAgendasAudienciasGeradas = this.buscarAgendasAudienciasGeradas(listaDatasAgendasAudiencias, audienciaAgendaDt);
			
			Iterator iterator = listaDatasAgendasAudiencias.iterator();
			while (iterator.hasNext()) {
				// AUDI�NCIADT
				AudienciaDt audienciaDtGerar = new AudienciaDt();
				// Tipo da audi�ncia
				audienciaDtGerar.setId_AudienciaTipo(audienciaAgendaDt.getId_AudienciaTipo());
				// Data da agenda (Data da audi�ncia livre)
				audienciaDtGerar.setDataAgendada(Funcoes.DataHora((Date) iterator.next()));
				// Id da Serventia
				audienciaDtGerar.setId_Serventia(audienciaAgendaDt.getId_Serventia());
				// Log
				audienciaDtGerar.setId_UsuarioLog(audienciaAgendaDt.getId_UsuarioLog());
				audienciaDtGerar.setIpComputadorLog(audienciaAgendaDt.getIpComputadorLog());

				// AUDIENCIAPROCESSODT
				/*
				 * No caso de gera��o manual de agendas de audi�ncias, cada audi�ncia possui apenas uma refer�ncia � tabela "AudienciaProcesso", ou seja,
				 * a lista de objetos do tipo "AudienciaProcessoDt" contida no objeto do tipo "AudienciaDt" possuir� apenas um registro do tipo
				 * "AudienicaProcessoDt". Isso se deve ao fato de que somente audi�ncias do tipo "Sess�o de 2� Grau" possuem v�rias refer�ncias � tabela
				 * "AudienciaProcesso" e esse tipo de agenda de audi�ncia ser� criada automaticamente.
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
				
				// LOG Prepara��o do objeto de log referente � inser��o de audi�ncia(s)
				logDt = new LogDt("Audiencia", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaProcessoDt.getPropriedades());
				// SALVAR LOG DA INSER��O OU ATUALIZA��O DO OBJETO DO TIPO "AUDIENCIADT"
				obLog.salvar(logDt, obFabricaConexao);
			}

//			// Percorrer a lista contendo objeto(s) do tipo "AudienciaDt"
//			Iterator iteratorAudienciaDt = listaAgendasAudienciasGeradas.iterator();
//			while (iteratorAudienciaDt.hasNext()) {
//				AudienciaDt audienciaDt = (AudienciaDt) iteratorAudienciaDt.next();
//
//				// INSER��O (AUDIENCIADT) (CRIA��O DE AGENDA(S) PARA AUDI�NCIAS)
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
//				// LOG Prepara��o do objeto de log referente � inser��o de audi�ncia(s)
//				logDt = new LogDt("Audiencia", audienciaDt.getId(), audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaDt.getPropriedades());
//				// SALVAR LOG DA INSER��O OU ATUALIZA��O DO OBJETO DO TIPO "AUDIENCIADT"
//				obLog.salvar(logDt, obFabricaConexao);
//			}// Fim while

			// FINALIZAR TRANSA��O
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSA��O
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * M�todo respons�vel por agendar automaticamente uma audi�ncia para um processo c�vel. 
	 * Depois do agendamento, ser�o geradas as pend�ncias do tipo "Intima��o Expedida" para o advogado ou partes promoventes(aterma��o), 
	 * e pend�ncia do tipo "Cita��o" para as partes promovidas
	 * 
	 * @author msapaula
	 * @param processoCivelDt, objeto com dados do processo
	 * @param serventiaDt, objeto com dados da serventia
	 * @param usuarioDt, objeto com dados do usu�rio
	 * @param atermacao, booleano para indicar se trata-se de aterma��o
	 */
	public AudienciaDt agendarAudienciaCadastroProcessoCivel(ProcessoCadastroDt processoCivelDt, ServentiaDt serventiaDt, UsuarioDt usuarioDt, boolean atermacao, FabricaConexao fabricaConexao) throws Exception {
		AudienciaDt audienciaDtAgendada = null;
		AudienciaTipoNe audienciaTipoNe = new AudienciaTipoNe();
		String audienciaTipoCodigo = null;
		
		LogDt logDtProcessoCivel = new LogDt(processoCivelDt.getId_UsuarioLog(), processoCivelDt.getIpComputadorLog());

		// Se serventia � mista, C�vel e Criminal, para processo c�vel buscar� audi�ncia do tipo Concilia��o
		if (Funcoes.StringToInt(serventiaDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL) audienciaTipoCodigo = String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo());
		//Sen�o busca o tipo de audi�ncia definido no cadastro da serventia
		else if (serventiaDt.getId_AudienciaTipo() != null && serventiaDt.getId_AudienciaTipo().length() > 0) audienciaTipoCodigo = audienciaTipoNe.consultarId(serventiaDt.getId_AudienciaTipo()).getAudienciaTipoCodigo();

		if (audienciaTipoCodigo != null) {
			// AGENDAR AUDI�NCIA PARA O PROCESSO
			audienciaDtAgendada = agendarAudienciaProcessoAutomaticamente(audienciaTipoCodigo, processoCivelDt.getId(), processoCivelDt.getId_Serventia(), fabricaConexao);

			// GERA MOVIMENTA��O E PEND�NCIAS PARA PROCESSO C�VEL
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

			// Consultar descri��o do status da audiencia de um processo.
			statusDescricao = obPersistencia.consultarAudienciasProcessoDescricao(numeroProcesso);
		
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
		return statusDescricao;
	}
	
	public String consultarAudiProcStatusDescricaoPeIdAudiProc(String idAudiProc) throws Exception {
		String statusDescricao = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// Consultar descri��o do status da audiencia de um processo.
			statusDescricao = obPersistencia.consultarAudiProcStatusDescricaoPeIdAudiProc(idAudiProc);
		
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
		return statusDescricao;
	}

	public List<AudienciaProcessoStatusDt> consultarAudienciaProcesso() throws Exception {
		List<AudienciaProcessoStatusDt> statusAudiencia = new ArrayList<AudienciaProcessoStatusDt>();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// Consultar descri��o do status da audiencia de um processo.
			statusAudiencia = obPersistencia.consultarAudienciaProcesso();
		
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
		return statusAudiencia;
	}
	
	/**
	 * M�todo respons�vel por agendar automaticamente uma audi�ncia para um processo criminal. 
	 * Depois do agendamento, ser�o geradas as pend�ncias do tipo "Intima��o Expedida" para as partes promoventes (aterma��o) 
	 * ou para o advogado, e pend�ncia do tipo "Intima��o" para as partes promovidas
	 * 
	 * @author msapaula
	 * @param processoCriminalDt, objeto com dados do processo
	 * @param serventiaDt, objeto com dados da serventia
	 * @param usuarioDt, objeto com dados do usu�rio
	 * @param atermacao, booleano para indicar se trata-se de aterma��o
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaCadastroProcessoCriminal(ProcessoCadastroDt processoCriminalDt, ServentiaDt serventiaDt, UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;
		AudienciaTipoNe audienciaTipoNe = new AudienciaTipoNe();
		String audienciaTipoCodigo = null;
		
		LogDt logDt = new LogDt(processoCriminalDt.getId_UsuarioLog(), processoCriminalDt.getIpComputadorLog());

		// Se serventia � mista, C�vel e Criminal, para processo criminal buscar� audi�ncia do tipo Preliminar
		if (Funcoes.StringToInt(serventiaDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL) audienciaTipoCodigo = String.valueOf(AudienciaTipoDt.Codigo.PRELIMINAR.getCodigo());
		//Sen�o busca o tipo de audi�ncia definido no cadastro da serventia
		else if (serventiaDt.getId_AudienciaTipo() != null && serventiaDt.getId_AudienciaTipo().length() > 0) audienciaTipoCodigo = audienciaTipoNe.consultarId(serventiaDt.getId_AudienciaTipo()).getAudienciaTipoCodigo();

		if (audienciaTipoCodigo != null) {
			// AGENDAR AUDI�NCIA PARA O PROCESSO
			audienciaDtAgendada = agendarAudienciaProcessoAutomaticamente(audienciaTipoCodigo, processoCriminalDt.getId(), processoCriminalDt.getId_Serventia(), fabricaConexao);

			// GERA MOVIMENTA��O E PEND�NCIAS PARA PROCESSO CRIMINAL
			if (audienciaDtAgendada != null) {
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoCriminalDt, audienciaDtAgendada.getAudienciaTipoCodigo(), UsuarioServentiaDt.SistemaProjudi, audienciaDtAgendada, logDt, fabricaConexao);
				gerarPendenciasAudienciaCriminal(processoCriminalDt, movimentacaoAudiencia, usuarioDt, fabricaConexao);
			}
		}
				
		return audienciaDtAgendada;
	}
	
	/**
	 * M�todo respons�vel por agendar automaticamente uma audi�ncia preliminar para um processo criminal. 
	 * Depois do agendamento, ser�o geradas as pend�ncias do tipo "Intima��o Expedida" para as partes promoventes (aterma��o) 
	 * ou para o advogado, e pend�ncia do tipo "Intima��o" para as partes promovidas
	 * 
	 * @author jrcorrea 17/10/2016
	 * @param processoCriminalDt, objeto com dados do processo
	 * @param serventiaDt, objeto com dados da serventia
	 * @param usuarioDt, objeto com dados do usu�rio
	 * @param atermacao, booleano para indicar se trata-se de aterma��o
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaPreliminarConciliadorCadastroProcessoCriminal(ProcessoCadastroDt processoCriminalDt,  UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;		
		String audienciaTipoCodigo = null;
		
		LogDt logDt = new LogDt(processoCriminalDt.getId_UsuarioLog(), processoCriminalDt.getIpComputadorLog());

		// defino que a audiencia sera de conciliacao
		audienciaTipoCodigo = String.valueOf(AudienciaTipoDt.Codigo.PRELIMINAR_CONCILIADOR.getCodigo());

		if (audienciaTipoCodigo != null) {
			// AGENDAR AUDI�NCIA PARA O PROCESSO
			audienciaDtAgendada = agendarAudienciaProcessoAutomaticamente(audienciaTipoCodigo, processoCriminalDt.getId(), processoCriminalDt.getId_Serventia(), fabricaConexao);

			// GERA MOVIMENTA��O E PEND�NCIAS PARA PROCESSO CRIMINAL
			if (audienciaDtAgendada != null) {
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoCriminalDt, audienciaDtAgendada.getAudienciaTipoCodigo(), UsuarioServentiaDt.SistemaProjudi, audienciaDtAgendada, logDt, fabricaConexao);
				gerarPendenciasAudienciaCriminal(processoCriminalDt, movimentacaoAudiencia, usuarioDt, fabricaConexao);
			}
		}
				
		return audienciaDtAgendada;
	}

	/**
	 * M�todo respons�vel por agendar automaticamente uma audi�ncia para um processo.
	 * Busca a pr�xima audi�ncia livre, j� faz um UPDATE na tabela para garantir que outro usu�rio n�o pegue a mesma
	 * e retorna a agenda utilizada
	 * 
	 * @author msapaula
	 * 
	 * @param audienciaTipoCodigo, tipo da audi�ncia a ser marcada
	 * @param id_Processo, processo para o qual ser� marcada audi�ncia
	 * @param id_Serventia, serventia do processo
	 * @param fabricaConexao, conex�o ativa
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
	 * M�todo respons�vel por agendar audi�ncia automaticamente para um processo j� cadastrado.
	 * 
	 * @param audienciaDt, dados para agendamento de Audi�ncia
	 * @param usuarioDt, usu�rio que est� agendando audi�ncia
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaAutomaticamenteProcesso(AudienciaDt audienciaDt, UsuarioDt usuarioDt) throws Exception{
		return this.agendarAudienciaAutomaticamenteProcesso(audienciaDt, usuarioDt, null);
	}

	/**
	 * M�todo respons�vel por agendar audi�ncia automaticamente para um processo j� cadastrado.
	 * 
	 * @param audienciaDt, dados para agendamento de Audi�ncia
	 * @param usuarioDt, usu�rio que est� agendando audi�ncia
	 * @param fabConexao, fabrica para poder continuar uma trasacao
	 * @throws Exception 
	 */
	public AudienciaDt agendarAudienciaAutomaticamenteProcesso(AudienciaDt audienciaDt, UsuarioDt usuarioDt, FabricaConexao fabConexao) throws Exception{
		AudienciaDt audienciaDtAgendada = null;
		AudienciaTipoDt.Codigo tipoCodigo = AudienciaTipoDt.Codigo.NENHUM;
		tipoCodigo = tipoCodigo.getCodigo(Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()));

		switch (tipoCodigo) {
			//Audi�ncias que precisam ficar vinculadas a um juiz
			case INSTRUCAO:
			case INSTRUCAO_JULGAMENTO:
			case JULGAMENTO: {
				// Ser� localizada a pr�xima audi�ncia livre para o cargo do juiz do processo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentiaCargo(audienciaDt, usuarioDt, fabConexao);
				break;
			}
			//audi�ncias que n�o precisam ficar vinculadas a juiz
			default: {
				// Ser� localizada pr�xima audi�ncia livre independente do Cargo
				audienciaDtAgendada = agendarAudienciaAutomaticamenteServentia(audienciaDt, usuarioDt, fabConexao);
				break;
			}
		}

		return audienciaDtAgendada;
	}

	/**
	 * M�todo respons�vel por agendar automaticamente uma audi�ncia para um processo j� cadastrado.
	 * Busca a pr�xima audi�ncia livre conforme o tipo passado e na serventia, pegando assim qualquer audi�ncia que seja
	 * a pr�xima n�o importando a ordem de distribui��o, ou se a pauta � de juiz ou conciliador.
	 * J� faz um UPDATE na tabela para garantir que outro usu�rio n�o pegue a mesma e retorna a agenda utilizada.
	 * 
	 * @param audienciaDt, objeto com dados da audi�ncia a ser agendada
	 * @param usuarioDt, usu�rio que est� agendando audi�ncia
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

			// AGENDAR AUDI�NCIA PARA O PROCESSO
			if (audienciaProcessoNe.agendarAudienciaProcessoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoDt.getId(), audienciaDt.getId_Serventia(), obFabricaConexao)){				
				audienciaDtAgendada = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoDt.getId());

				if (audienciaDtAgendada != null) {
					// Gera Movimenta��o Audi�ncia Marcada
					MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoDt, audienciaDtAgendada.getAudienciaTipoCodigo(), usuarioDt.getId_UsuarioServentia(), audienciaDtAgendada, logDt, obFabricaConexao);
					// PEND�NCIA(S)
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
	 * M�todo respons�vel por agendar automaticamente uma audi�ncia para um processo j� cadastrado.
	 * Busca a pr�xima audi�ncia livre para o ServentiaCargo passado. 
	 * 
	 * @param audienciaDt, objeto com dados da audi�ncia a ser agendada
	 * @param usuarioDt, usu�rio que est� agendando audi�ncia
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

			//JUIZ RESPONS�VEL - Ser�o localizadas as audi�ncias livres referentes a esse cargo serventia do juiz respons�vel pelo processo.
			ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
			ServentiaCargoDt serventiaCargoDt = processoResponsavelNe.getJuizResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia());
			//AGENDAR AUDI�NCIA PARA O JUIZ DO PROCESSO
			if (audienciaProcessoNe.agendarAudienciaProcessoAutomaticoServentiaCargo(serventiaCargoDt.getId(), audienciaDt.getAudienciaTipoCodigo(), processoDt.getId(), obFabricaConexao)){
				audienciaDtAgendada = obPersistencia.getUltimaAudienciaMarcadaAgendamentoAutomatico(audienciaDt.getAudienciaTipoCodigo(), processoDt.getId());

				if (audienciaDtAgendada != null) {
					// Gera Movimenta��o Audi�ncia Marcada
					MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoDt, audienciaDtAgendada.getAudienciaTipoCodigo(), usuarioDt.getId_UsuarioServentia(), audienciaDtAgendada, logDt, obFabricaConexao);
					// PEND�NCIA(S)
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
	 * M�todo respons�vel por agendar audi�ncia para um processo manualmente. 
	 * 
	 * @param audienciaDtAgendar, objeto com dados da audi�ncia a ser agendada
	 * @param usuarioDt, usu�rio que est� agendando audi�ncia
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

			// SALVA ATUALIZA��O DE "AUDIENCIAPROCESSODT"
			if (audienciaProcessoNe.agendarAudienciaProcessoManual(audienciaProcessoDt, obFabricaConexao)){
				
				resultado = true;
				
				// PREPARA OBJETO DO TIPO LOGDT
				LogDt logDt = new LogDt(audienciaDtAgendar.getId_UsuarioLog(), audienciaDtAgendar.getIpComputadorLog());

				// Gera Movimenta��o Audi�ncia Marcada
				MovimentacaoDt movimentacaoAudiencia = gerarMovimentacaoAudienciaMarcada(processoDt, audienciaDtAgendar.getAudienciaTipoCodigo(), usuarioDt.getId_UsuarioServentia(), audienciaDtAgendar, logDt, obFabricaConexao);

				// PEND�NCIA(S)
				String id_serventia = null;
				if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo(), 0) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
					id_serventia = processoDt.getId_Serventia();
				} else{
					id_serventia = usuarioDt.getId_Serventia();
				}
				gerarPendenciasAudiencia(processoDt, movimentacaoAudiencia, usuarioDt.getId_UsuarioServentia(), id_serventia, logDt, obFabricaConexao);			

			}
			
			// COMMIT TRANSA��O
			obFabricaConexao.finalizarTransacao();
						
			return resultado;
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}

	/**
	 * M�todo respons�vel por criar uma conex�o com o banco de dados e consultar audi�ncias livres e v�lidas para o agendamento/reagendamento manual
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
				throw new Exception("N�o foi poss�vel localizar o mapeamento do tipo de audi�ncia " + audienciaDt.getAudienciaTipo() + ". Favor entrar em contato com o suporte.");	
			}

			// CONSULTAR AUDI�NCIAS LIVRES
			switch (codigoTipo) {
				// Audi�ncias que precisam ficar vinculadas a um juiz
				case INSTRUCAO:
				case JULGAMENTO:
				case INSTRUCAO_JULGAMENTO: {
					listaAudienciasLivres = obPersistencia.consultarAudienciasLivresAgendamentoManual(audienciaDt, id_Serventia,true, posicaoPaginaAtual);
					break;
				}
				// As demais audi�ncias que n�o precisam ficar vinculadas a um juiz
				default: {
					listaAudienciasLivres = obPersistencia.consultarAudienciasLivresAgendamentoManual(audienciaDt, id_Serventia, false, posicaoPaginaAtual);
					break;
				}
			}

			// DEFINIR QUANTIDADE DE P�GINAS
			QuantidadePaginas = (Long) listaAudienciasLivres.get(listaAudienciasLivres.size() - 1);

			// REMOVER DA LISTA DE AUDI�NCIAS LIVRES CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE P�GINAS
			listaAudienciasLivres.remove(listaAudienciasLivres.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasLivres;
	}

	/**
	 * M�todo respons�vel por validar o agendamento/reagendamento de uma audi�ncia para um dado processo, ou seja, verificar se o processo possui uma
	 * audi�ncia pendente (Status da audi�ncia pendente = "A Ser Realizada" = 1) cujo tipo da audi�ncia seja o mesmo da audi�ncia livre selecionada.
	 * Caso j� exista essa audi�ncia, o agendamento/reagendamento dessa audi�ncia livre para esse processo n�o poder� ocorrer. Se for retornada uma
	 * audi�ncia, ou seja, quantidade de audi�ncia existente ser� igual a 1(um) isso significa que existe uma audi�ncia pendente semelhante �
	 * audi�ncia livre e o agendamento/reagendamento n�o poder� ocorrer
	 * 
	 * @author Keila Sousa Silva
	 * @author mmgomes
	 * @param audienciaDtAgendar
	 * @param CriarFabricaConexao
	 * @return audienciaInexistente: boolean que indicar� se foi retornada alguma audi�ncia
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
					stRetorno += " Sem permiss�o para Agendar Audi�ncia em processo de outra serventia.";
				}
			
			} else {
				// Se usu�rio for de serventia diferente do processo, n�o poder� agendar
				if (processoDt.getId_Serventia() != null && usuarioDt.getId_Serventia() != null && 
					!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia()) && 
				    audienciaDtAgendar.getAcessoOutraServentia()!= null && audienciaDtAgendar.getAcessoOutraServentia().equals("") &&
				    !(Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.PREPROCESSUAL && 
				       (Funcoes.StringToInt(audienciaDtAgendar.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
						Funcoes.StringToInt(audienciaDtAgendar.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
				        Funcoes.StringToInt(audienciaDtAgendar.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()))) {
					stRetorno += " Sem permiss�o para Agendar Audi�ncia em processo de outra serventia.";
				}
			}

			if (audienciaDtAgendar.getAudienciaTipoCodigo() == null || audienciaDtAgendar.getAudienciaTipoCodigo().equals("")) {
				stRetorno += " O tipo de Audi�ncia n�o foi informado. \n";
			}

			// Verifica se j� existe audi�ncia marcada
			quantidadeAudienciaExistente = obPersistencia.validarAudienciaAgendamento(processoDt.getId());
			if (quantidadeAudienciaExistente > 0) {
				stRetorno += "Agendamento de audi�ncia n�o pode ser realizado. O processo j� possui uma audi�ncia com status pendente.";
			}		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * M�todo respons�vel por gerar a movimenta��o referente � audi�ncia marcada. De acordo com o tipo da audi�ncia que est� sendo marcada, faz
	 * chamada ao m�todo que ir� gerar a movimenta��o correspondente.
	 * 
	 * @param processoDt
	 *            , dt do processo que ter� uma audi�ncia marcada
	 * @param audienciaTipoCodigo
	 *            , tipo da audi�ncia que est� sendo marcada
	 * @param id_UsuarioRealizador
	 *            , usu�rio que est� marcando a audi�ncia
	 * @param audienciaDtParaAgendar
	 *            , dt da audi�ncia que est� sendo marcada
	 * @param logDt
	 *            , dados do log
	 * @param fabricaConexao
	 *            , conex�o ativa
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
			complemento += " - Carta Precat�ria";
		}

		movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaMarcada(processoDt.getId(), id_UsuarioRealizador, complemento, audienciaTipoCodigo, logDt, fabricaConexao);
		movimentacaoNe.gerarMovimentosComplementos(movimentacaoAudiencia, audienciaDtParaAgendar, fabricaConexao);
		return movimentacaoAudiencia;
	}

	/**
	 * M�todo utilizado no cadastro de processo c�vel para gerar pend�ncia(s) referente(s) � audi�ncia marcada
	 * 
	 * @param processoDt, processo que est� sendo cadastrado
	 * @param movimentacaoAudiencia, movimenta��o vinculada a audi�ncia
	 * @param usuarioDt, usu�rio que est� cadastrando o processo
	 * @param atermacao, define se trata de aterma��o
	 * @param fabricaConexao, conex�o ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private void gerarPendenciasAudienciaCivel(ProcessoDt processoDt, MovimentacaoDt movimentacaoAudiencia, UsuarioDt usuarioDt, boolean atermacao, FabricaConexao fabricaConexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();

		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());

		if (atermacao) {
			// Gera intima��o para cada uma das partes promoventes
			List listaPromoventes = processoDt.getListaPolosAtivos();
			for (int i = 0; i < listaPromoventes.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaPromoventes.get(i);
				movimentacaoNe.gerarMovimentacaoIntimacaoEfetivadaParteProcesso(processoDt.getId(), processoParteDt.getNome(), movimentacaoAudiencia.getMovimentacaoTipo(), logDt, fabricaConexao);
			}
		} else {
			// Se advogado, gera intima��o somente para esse
			pendenciaNe.gerarIntimacaoEfetivadaAdvogado(processoDt, movimentacaoAudiencia, UsuarioServentiaDt.SistemaProjudi, "", usuarioDt, logDt, fabricaConexao);
		}

		// Gera pend�ncia do tipo "Carta de Cita��o" para todas as partes promovidas
		List listaPromovidos = processoDt.getListaPolosPassivos();
		if (listaPromovidos != null) {
			for (int i = 0; i < listaPromovidos.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaPromovidos.get(i);
				pendenciaNe.gerarCitacao(processoDt, movimentacaoAudiencia.getId(), UsuarioServentiaDt.SistemaProjudi, processoParteDt.getId_ProcessoParte(), processoDt.getId_Serventia(), logDt, fabricaConexao);
			}
		}

	}

	/**
	 * M�todo utilizado no cadastro de processo criminal para gerar pend�ncia(s) referente(s) � audi�ncia marcada
	 * 
	 * @param processoDt, processo que est� sendo cadastrado
	 * @param movimentacaoAudiencia, movimenta��o vinculada a audi�ncia
	 * @param usuarioDt, usu�rio que est� cadastrando o processo
	 * @param atermacao, define se trata de aterma��o
	 * @param fabricaConexao, conex�o ativa
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

			// Se parte est� sendo intimada na Delegacia, gera movimenta��o do tipo "Intima��o Efetivada"
			if (intimacaoDelegacia) {
				// Gera movimenta��o do tipo "Intima��o Efetivada"
				movimentacaoNe.gerarMovimentacaoIntimacaoEfetivadaParteProcesso(processoCriminalDt.getId(), processoParteDt.getNome(), movimentacaoAudiencia.getMovimentacaoTipo(), logDt, fabricaConexao);
			} else {
				// Gera pend�ncia do Tipo "Intima��o" para cart�rio expedir
				pendenciaNe.gerarIntimacaoServentia(processoCriminalDt, movimentacaoAudiencia.getId(), UsuarioServentiaDt.SistemaProjudi, processoParteDt.getId_ProcessoParte(), processoCriminalDt.getId_Serventia(), logDt, fabricaConexao);
			}
		}

	}

	/**
	 * M�todo utilizado no agendamento/reagendamento manual ou autom�tico de audi�ncias para gerar pend�ncia(s) referente(s) � audi�ncia marcada para
	 * cada uma das partes do processo
	 * 
	 * @param processoDt
	 *            , dt de processo
	 * @param movimentacaoAudiencia
	 *            , dt da movimenta��o vinculada a audi�ncia
	 * @param usuarioDt
	 *            , usu�rio
	 * @param fabricaConexao
	 *            , conex�o ativa
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
	 * M�todo respons�vel por abrir a conex�o com o banco de dados e consultar as audi�ncias pendentes. A consulta pode ter sido requisitada por
	 * serventu�rios, advogados, conciliadores ou juizes. Audi�ncias Pendentes Tipo X = (Id_Audi�nciaTipo = Tipo de Audi�ncia definido de acordo com o
	 * menu selecionado) + (Id_Processo IS NOT NULL) + (Data da Realizacao IS NULL) + (Id_ServentiaCargo = Cargo da Serventia selecionado pelo
	 * usu�rio)
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
					// Consuultar audi�ncias pendentes do usu�rio (conciliador ou juiz togado)
					listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentesUsuario(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				default:
					// Consultar audi�ncias pendentes do serventu�rio
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
	 * M�todo respons�vel em consultar todas as audi�ncias pendentes em uma serventia.
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

			// Consultar audi�ncias pendentes do serventu�rio
			listaAudienciasPendentes = obPersistencia.consultarAudienciasPendentes(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);

			QuantidadePaginas = (Long) listaAudienciasPendentes.get(listaAudienciasPendentes.size() - 1);
			listaAudienciasPendentes.remove(listaAudienciasPendentes.size() - 1);
		
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasPendentes;
	}

	/**
	 * Consulta as Sess�es de 2� grau pendentes de acordo com o grupo do usu�rio passado.
	 * 
	 * @param usuarioDt, identifica��o do cargo do relator para filtro na pesquisa
	 * @param id_Audiencia, identifica��o de audi�ncia no caso de retornar somente os processos a serem julgados em uma sess�o
	 * @param posicaoPaginaAtual, par�metro para pagina��o
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
	 * M�todo respons�vel por abrir a conex�o com o banco de dados e consultar as audi�ncias agendadas para a data corrente. A consulta pode ter sido
	 * requisitada por serventu�rios, advogados, conciliadores ou juizes. Este m�todo tratar� as requisi��es de consultas provenientes dos
	 * serventu�rios.
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
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
			    case GrupoTipoDt.ADVOGADO: {
					// Consultar audi�ncias para hoje do advogado
			    	if(audienciaDt.getAudienciaTipoCodigo().equals(String.valueOf(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()))) {
			    		listaAudienciasParaHoje = obPersistencia.consultarAudienciasSegundoGrauParaHojeAdvogado(usuarioDt, audienciaDt, posicaoPaginaAtual);
			    	} else {
			    		listaAudienciasParaHoje = obPersistencia.consultarAudienciasParaHojeAdvogado(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
			    	}
					break;
				}

			    case GrupoTipoDt.CONCILIADOR_VARA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU: {
					// Consultar audi�ncias para hoje do usu�rio (conciliador ou juiz togado)
					listaAudienciasParaHoje = obPersistencia.consultarAudienciasParaHojeUsuario(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				}

				default: {
					// Consultar audi�ncias para hoje do serventu�rio
					listaAudienciasParaHoje = obPersistencia.consultarAudienciasParaHoje(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);
					break;
				}
			}

			// DEFINIR A QUANTIDADE DE P�GINAS
			QuantidadePaginas = (Long) listaAudienciasParaHoje.get(listaAudienciasParaHoje.size() - 1);
			// REMOVER DA LISTA DE AUDI�NCIAS PARA HOJE CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE P�GINAS
			listaAudienciasParaHoje.remove(listaAudienciasParaHoje.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasParaHoje;
	}

	/**
	 * M�todo respons�vel por abrir ou n�o a conex�o com o banco de dado, dependendo do par�metro de conex�o informado, e consultar a quantidade das
	 * audi�ncias para hoje de um dado cargo da serventia de um juiz ou de um conciliador e dos seguintes tipos: concilia��o, instru��o, preliminar e
	 * una.
	 * 
	 * Audi�ncias Para Hoje = Audi�ncias pendentes, ou seja, audi�ncias cujo status � "A Ser Realizada", cujo id do processo is not null e cuja data
	 * de agendamento � a data corrente.
	 * 
	 * @author Keila Sousa Silva
	 * @since 21/08/2009
	 * @param id_ServentiaCargo
	 * @param fabricaConexao
	 * @return List listaTipoAudienciaQuantidadeAudienciasParaHoje = lista contendo um array de string que possuir� os seguintes valores, de acordo
	 *         com a ordem a seguir: "AudienciaTipoCodigo", "AudienciaTipo" e "Quantidade" (Quantidade de audi�ncias pendentes para hoje)
	 * @throws Exception
	 */
	public List consultarQuantidadeAudienciasParaHoje(String id_ServentiaCargo, FabricaConexao fabricaConexao) throws Exception {
		List listaTipoAudienciaQuantidadeAudienciasParaHoje = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// INICIAR CONEX�O COM O BANCO DE DADOS
			if (fabricaConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabricaConexao;
			}
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// CONSULTAR AUDI�NCIAS PARA HOJE
			listaTipoAudienciaQuantidadeAudienciasParaHoje = obPersistencia.consultarQuantidadeAudienciasParaHoje(id_ServentiaCargo);
		} finally {
			// FECHAR CONEX�O
			if (fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		// RETURN
		return listaTipoAudienciaQuantidadeAudienciasParaHoje;
	}

	/**
	 * M�todo respons�vel por abrir ou n�o a conex�o com o banco de dado, dependendo do par�metro de conex�o informado, e consultar a quantidade das
	 * audi�ncias para hoje de advogados e dos seguintes tipos: concilia��o, instru��o, preliminar e una.
	 * 
	 * Audi�ncias Para Hoje Advogado = Audi�ncias pendentes, ou seja, audi�ncias cujo status � "A Ser Realizada", cujo id do processo is not null,
	 * cuja data de agendamento � a data corrente e cujo processo possui o advogado em quest�o como advogado de uma das partes deste processo.
	 * 
	 * @author Keila Sousa Silva
	 * @since 27/08/2009
	 * @param usuarioDt
	 * @param fabricaConexao
	 * @return List listaTipoAudienciaQuantidadeAudienciasParaHoje = lista contendo um array de string que possuir� os seguintes valores, de acordo
	 *         com a ordem a seguir: "AudienciaTipoCodigo", "AudienciaTipo" e "Quantidade" (Quantidade de audi�ncias pendentes para hoje)
	 * @throws Exception
	 */
	public List consultarQuantidadeAudienciasParaHojeAdvogado(UsuarioDt usuarioDt, FabricaConexao fabricaConexao) throws Exception {
		List listaTipoQuantidadeAudienciasParaHojeAdvogado = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try{
			// INICIAR CONEX�O COM O BANCO DE DADOS
			if (fabricaConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabricaConexao;
			}
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			// CONSULTAR AUDI�NCIAS PARA HOJE
			listaTipoQuantidadeAudienciasParaHojeAdvogado = obPersistencia.consultarQuantidadeAudienciasParaHojeAdvogado(usuarioDt);
		} finally {
			// FECHAR CONEX�O
			if (fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return listaTipoQuantidadeAudienciasParaHojeAdvogado;
	}

	/**
	 * M�todo respons�vel por abrir a conex�o com o banco de dados e consultar audi�ncias que foram movimentadas na data corrente. A consulta pode ter
	 * sido requisitada por serventu�rios, advogados, conciliadores ou juizes. Movimentar uma audi�ncia significa alterar seu status de
	 * "1 = A Ser Realizada" para alguns dos sequintes status: "2 = Cancelada", "3 = Negativada", "4 = Realizada", "5 = Realizada com Concilia��o", "6
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
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			//switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
				// PERFIL ADVOGADO
//				case GrupoDt.ADVOGADOS: {
			    case GrupoTipoDt.ADVOGADO: {
					// Consultar as audi�ncias movimentadas hoje do advogado
					listaAudienciasMovimentadasHoje = obPersistencia.consultarAudienciasMovimentadasHojeAdvogado(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				}// FIM CASE ADVOGADO

					// PERFIL CONCILIADOR OU JUIZ TOGADO
//				case GrupoDt.CONCILIADORES_VARA:
//				case GrupoDt.JUIZES_VARA: {
			    case GrupoTipoDt.CONCILIADOR_VARA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU: {
					// Consultar as audi�ncias movimentadas hoje do usu�rio(conciliador ou juiz togado)
					listaAudienciasMovimentadasHoje = obPersistencia.consultarAudienciasMovimentadasHojeUsuario(usuarioDt, audienciaDt, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
				} // FIM CASE PERFIL CONCILIADOR OU JUIZ TOGADO

					// PERFIS SERVENTU�RIOS DO JUIZADO
				default: {
					// Consultar as audi�ncias movimentadas hoje do serventu�rio
					listaAudienciasMovimentadasHoje = obPersistencia.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, posicaoPaginaAtual, false, ordenacao, qtdRegistros);
					break;
				} // FIM CASE PERFIS SERVENTU�RIOS DO JUIZADO
			}// FIM SWITCH

			// DEFINIR A QUANTIDADE DE P�GINAS
			QuantidadePaginas = (Long) listaAudienciasMovimentadasHoje.get(listaAudienciasMovimentadasHoje.size() - 1);
			// REMOVER DA LISTA DE AUDI�NCIAS PARA HOJE CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE P�GINAS
			listaAudienciasMovimentadasHoje.remove(listaAudienciasMovimentadasHoje.size() - 1);
		
		} finally{
			// FECHAR CONEX�O
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasMovimentadasHoje;
	}

	/**
	 * M�todo respons�vel por abrir a conex�o com o banco de dados e por consultar audi�ncias de acordo com os par�metros informados pelo usu�rio na
	 * consulta por filtros. A consulta pode ter sido requisitada por serventu�rios, advogados, conciliadores ou juizes.
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
					// Consultar as audi�ncias do usu�rio(conciliador) de acordo com os par�metros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltroUsuario(usuarioDt.getId_ServentiaCargo(), audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:					
					// Consultar as audi�ncias do usu�rio(juiz togado) de acordo com os par�metros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltro(idServentiaAudiencia, idServentiaProcesso, audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;

				default:
					// Consultar as audi�ncias de acordo com os par�metros informados no filtro
					listaAudienciasComFiltro = obPersistencia.consultarAudienciasFiltro(idServentiaAudiencia, idServentiaProcesso, audienciaDt, numeroProcesso, digitoVerificador, posicaoPaginaAtual, ordenacao, qtdRegistros);
					break;
			}

			QuantidadePaginas = (Long) listaAudienciasComFiltro.get(listaAudienciasComFiltro.size() - 1);
			listaAudienciasComFiltro.remove(listaAudienciasComFiltro.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		// Retornando a lista contendo as audi�ncias consultadas de acordo com os par�metros de consulta definidos pelo usu�rio
		return listaAudienciasComFiltro;
	}

	/**
	 * M�todo respons�vel por consultar Sess�es de 2� grau de acordo com os par�metros informados pelo usu�rio na consulta por filtros.
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
					// Consultar as audi�ncias de acordo com os par�metros informados no filtro
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
	 * M�todo respons�vel por consultar Sess�es de 2� grau de acordo com os par�metros informados pelo usu�rio na consulta por filtros e retorna JSON.
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
					// Consultar as audi�ncias de acordo com os par�metros informados no filtro
					//m�todo inexistente
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
	 * M�todo respons�vel por validar os dados informados pelo usu�rio no cadastro de agendas de audi�ncias
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaAgendaDt
	 * @param geracaoAgendaAudiencia
	 * @return String retornoValidacao: mensagem contendo as inconsist�ncias encontradas nos dados informados pelo usu�rio
	 * @throws Exception
	 */
	public String validarDadosGeracaoAgendasAudiencias(AudienciaAgendaDt audienciaAgendaDt) throws Exception{
		// MENSAGEM CONTENDO OS ERROS ENCONTRADOS NA VALIDA��O DOS DADOS INFORMADOS PELO USU�RIO
		retornoValidacao = "";
		logHorariosDuracao = "";

		// CARGO DA SERVENTIA
		if (audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) {
			retornoValidacao += "O campo CARGO DA SERVENTIA � obrigat�rio." + " \n";
		}

		// TIPO DE AUDI�NCIA
		if (audienciaAgendaDt.getId_AudienciaTipo().equalsIgnoreCase("")) {
			retornoValidacao += "O campo TIPO DA AUDI�NCIA � obrigat�rio.\n";
		}

		// QUANTIDADE DE AUDI�NCIAS SIMULT�NEAS
		validarQuantidadeAudienciasSimultaneas(audienciaAgendaDt);

		// DATAS INICIAL E FINAL
		if (validarDatasInicialFinal(audienciaAgendaDt)) {
			// HOR�RIOS INICIAL E FINAL / DURA��O DAS AUDI�NCIAS
			if (validarHorariosInicialFinalDuracao(audienciaAgendaDt) > 0) {
				// VERIFICAR COMPATIBILIDADADE DOS DIAS DA SEMANA DO PER�ODO INFORMADO (DATAS INICIAL E FINAL) COM OS DIAS DA SEMANA INFORMADOS
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
					retornoValidacao += "O campo TIPO DA AUDI�NCIA selecionado s� � permitido para serventias do tipo Preprocessual (CEJUSC).\n";
				} else if (Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeAudienciasSimultaneas()) > 1) {
					ServentiaCargoDt serventiaCargoDt = new ServentiaCargoNe().consultarId(audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo());
					if (serventiaCargoDt != null && Funcoes.StringToInt(serventiaCargoDt.getCargoTipoCodigo()) != CargoTipoDt.JUIZ_1_GRAU) {
						retornoValidacao += "O campo QUANTIDADE s� pode ser maior que 1 quando o TIPO DO CARGO DA SERVENTIA for igual a Juiz.\n";
					}
				}
			}
		}
		
		return retornoValidacao;
	}

	/**
	 * M�todo respons�vel por validar os dados informados pelo usu�rio no cadastro de audi�ncias de segundo grau
	 * 
	 * @param audienciaSegundoGrauDt
	 * @return String retornoValidacao: mensagem contendo as inconsist�ncias encontradas nos dados informados pelo usu�rio
	 * 
	 * @author msapaula
	 */
	public String validarDadosGeracaoAudienciaSegundoGrau(AudienciaSegundoGrauDt audienciaSegundoGrauDt){
		String retornoValidacao = "";

		if (audienciaSegundoGrauDt.getData().equalsIgnoreCase("")) retornoValidacao += "Data � campo obrigat�rio. \n";
		if (audienciaSegundoGrauDt.getHora().equalsIgnoreCase("")) retornoValidacao += "Hora � campo obrigat�rio. \n";

		return retornoValidacao;
	}

	/**
	 * M�todo respons�vel por validar a exclus�o de uma Audi�ncia: uma audi�ncia s� poder� ser exclu�da se n�o houver 
	 * nenhum processo com sess�o pendente vinculado � essa.
	 * 
	 * @param id_Audiencia
	 * 
	 * @author msapaula
	 */
	public String validarExclusaoAudiencia(String id_Audiencia, UsuarioDt usuarioDt) throws Exception {
		String retornoValidacao = "";

		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		List listaProcessos = audienciaProcessoNe.consultarAudienciaProcessosPendentes(id_Audiencia, usuarioDt);
		if (listaProcessos != null && listaProcessos.size() > 0) retornoValidacao += "Sess�o n�o pode ser exclu�da. Existem processos vinculados.";

		return retornoValidacao;
	}

	/**
	 * M�todo respons�vel por verificar a compatibilidade dos dias da semana referentes ao per�odo (datas inicial e final) selecionado com os dias da
	 * semana [domingo (hor�rios inicial e final e dura��o da audi�ncia), segunda (hor�rios inicial e final e dura��o da audi�ncia), ter�a (hor�rios
	 * inicial e final e dura��o da audi�ncia), quarta (hor�rios inicial e final e dura��o da audi�ncia), quinta (hor�rios inicial e final e dura��o
	 * da audi�ncia), sexta (hor�rios inicial e final e dura��o da audi�ncia) e s�bado (hor�rios inicial e final e dura��o da audi�ncia)] informados
	 * pelo usu�rio
	 * 
	 * @author Keila Sousa Silva
	 * @param dataInicial
	 * @param dataFinal
	 * @param horariosDuracaoInformados
	 * @return int Dias da semana incompat�veis com os dias da semana do per�odo informado
	 * @throws Exception
	 */
	private void validarCompatibilidadeDiasDaSemanaPeriodoDiasDaSemanaInformados(AudienciaAgendaDt audienciaAgendaDt) throws Exception{
		/*
		 * Dias da semana informados: domingo = 0, segunda = 1, ter�a = 2, quarta = 3, quinta = 4, sexta = 5, s�bado = 6
		 */
		List diasDaSemanaInformados = new ArrayList();
		List diasDaSemanaPeriodo = new ArrayList();
		List diasDaSemanaPeriodoFinal = new ArrayList();
		List diasDaSemanaIncompativeis = new ArrayList();

		// Constru�ndo um array somente com os dias da semana informados
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

		// Constru�ndo um array com os dias da semana do per�odo informado
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Funcoes.StringToDate(audienciaAgendaDt.getDataInicial()));
		while (calendar.getTimeInMillis() <= Funcoes.StringToDate(audienciaAgendaDt.getDataFinal()).getTime()) {
			/*
			 * Para o 'Calendar' os dias da semana s�o: domingo = 1, segunda = 2, ter�a = 3, quarta = 4, quinta = 5, sexta = 6, s�bado = 7. Para
			 * adequar os valores aos "index" dos dias da semana da jsp, ser� feita a subtra��o por 1
			 */
			diasDaSemanaPeriodo.add(String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
			calendar.add(Calendar.DAY_OF_WEEK, 1);
		}

		// Limpando o array com os dias da semana do per�odo informado, retirando os dias das semana repetidos
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
			 * Atualizando os dias da semana do per�odo informado de acordo com o array dos dias da semana ({0,3,6,9,12,15,18})
			 */
			for (int i = 0; i < diasDaSemanaPeriodoFinal.size(); i++) {
				// SWITCH
				switch (Funcoes.StringToInt((String) diasDaSemanaPeriodoFinal.get(i))) {

					// SEGUNDA
					case 2: {
						diasDaSemanaPeriodoFinal.set(i, "0");
						break;
					}
						// TER�A
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
						// S�BADO
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
		 * Comparando os dias da semana informados com os dias da semana do per�odo informado de forma a verificar se existem dias da semana
		 * incompat�veis
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

		// Mensagem caso haja algum dia da semana incompat�vel
		if (diasDaSemanaIncompativeis.size() > 0) {
			retornoValidacao += "Dia(s) da semana informado(s) incompat�vel(�is) com o per�odo informado (datas inicial e final).\n";
		}
	}

	/**
	 * M�todo respons�vel por validar a quantidade de audi�ncias simult�neas informada pelo usu�rio no cadastro de agendas para audi�ncias
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaAgendaDt
	 */
	private void validarQuantidadeAudienciasSimultaneas(AudienciaAgendaDt audienciaAgendaDt) {
		/*
		 * Validar se o campo da quantidade de audi�ncias simult�neas foi preenchido
		 */
		if ((!audienciaAgendaDt.getQuantidadeAudienciasSimultaneas().trim().equals("")) && (!audienciaAgendaDt.getQuantidadeAudienciasSimultaneas().trim().equals("0"))) {
			/*
			 * Validar se a quantidade de audi�ncias simult�neas informada � um n�mero v�lido, ou seja, maior ou igual a 1
			 */
			if (!(Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeAudienciasSimultaneas()) > 0)) {
				retornoValidacao += "O campo QUANTIDADE � obrigat�rio.\n";
			}
			if (Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeAudienciasSimultaneas()) > Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas())) {
				retornoValidacao += "O campo QUANTIDADE n�o pode ser maior que " + audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas() + ".\n";
			}
			
		} else {
			retornoValidacao += "O valor a ser informado no campo QUANTIDADE deve ser igual ou superior a 1 e igual ou inferior a " + audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas() + ".\n";
		}
	}

	/**
	 * M�todo respons�vel por validar as dadas inicial e final informadas pelo usu�rio no cadastro de agendas para audi�ncias
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
			 * Validar se o valor do campo 'data inicial' � igual ou superior � data corrente
			 */
			if (audienciaAgendaValidacao.validarDataIgualSuperiorCorrente(audienciaAgendaDt.getDataInicial())) {
				dataInicialValida = true;
			} else {
				dataInicialValida = false;
				retornoValidacao += "O valor do campo DATA INICIAL deve ser igual ou superior � data corrente.\n";
			}
		} else {
			dataInicialValida = false;
			retornoValidacao += "O campo DATA INICIAL � obrigat�rio.\n";
		}

		// DATA FINAL
		// Validar se o campo 'data final' foi informado
		if (audienciaAgendaValidacao.validarDataObrigatoria(audienciaAgendaDt.getDataFinal())) {
			/*
			 * Validar se o valor do campo 'data final' � igual ou superior � data corrente
			 */
			if (audienciaAgendaValidacao.validarDataIgualSuperiorCorrente(audienciaAgendaDt.getDataFinal())) {
				dataFinalValida = true;
			} else {
				dataInicialValida = false;
				retornoValidacao += "O valor do campo DATA FINAL deve ser igual ou superior � data corrente.\n";
			}
		} else {
			dataInicialValida = false;
			retornoValidacao += "O campo DATA FINAL � obrigat�rio.\n";
		}

		// DATAS INICIAL E FINAL
		// Se as datas inicial e final s�o v�lidas verifica-se se o per�odo � consistente
		if (dataInicialValida & dataFinalValida) {
			/*
			 * Validar se as datas de um per�odo informado s�o consistentes, ou seja, a data final � igual ou superior � data inicial
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
	 * M�todo respons�vel por validar os hor�rios inicial e final e a dura��o das audi�ncias informados pleo usu�rio no cadastro de agendas para
	 * audi�ncias
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaAgendaDt
	 * @return int horariosDuracaoInformadosValidos
	 * @throws Exception
	 */
	private int validarHorariosInicialFinalDuracao(AudienciaAgendaDt audienciaAgendaDt) throws Exception{
		logHorariosDuracao = "";

		/*
		 * Atributo que possuir� a quantidade de dias da semana onde os hor�rios inicial e final e dura��o foram informados de forma v�lida
		 */
		int horariosDuracaoInformadosValidos = 0;

		/*
		 * Verificar se os hor�rios inicial e final e a dura��o das audi�ncias, dos dias da semana do per�odo desejado, foram informados e s�o valores
		 * v�lidos. Os dados hor�rios inicial e final e a dura��o das audi�ncias ser�o validados apenas quando pelo menos um deles tenha sido
		 * informado
		 */

		// SEGUNGA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[0].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[1].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[2].equals("")))) {
			if (validarHorariosDuracao("1", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[0], audienciaAgendaDt.getHorariosDuracao()[1], audienciaAgendaDt.getHorariosDuracao()[2])) {
				logHorariosDuracao += "Segunda: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[0] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[1] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[2] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// TER�A
		if ((!(audienciaAgendaDt.getHorariosDuracao()[3].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[4].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[5].equals("")))) {
			if (validarHorariosDuracao("2", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[3], audienciaAgendaDt.getHorariosDuracao()[4], audienciaAgendaDt.getHorariosDuracao()[5])) {
				logHorariosDuracao += "Ter�a: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[3] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[4] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[5] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// QUARTA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[6].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[7].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[8].equals("")))) {
			if (validarHorariosDuracao("3", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[6], audienciaAgendaDt.getHorariosDuracao()[7], audienciaAgendaDt.getHorariosDuracao()[8])) {
				logHorariosDuracao += "Quarta: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[6] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[7] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[8] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// QUINTA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[9].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[10].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[11].equals("")))) {
			if (validarHorariosDuracao("4", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[9], audienciaAgendaDt.getHorariosDuracao()[10], audienciaAgendaDt.getHorariosDuracao()[11])) {
				logHorariosDuracao += "Quinta: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[9] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[10] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[11] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// SEXTA
		if ((!(audienciaAgendaDt.getHorariosDuracao()[12].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[13].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[14].equals("")))) {
			if (validarHorariosDuracao("5", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[12], audienciaAgendaDt.getHorariosDuracao()[13], audienciaAgendaDt.getHorariosDuracao()[14])) {
				logHorariosDuracao += "Sexta: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[12] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[13] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[14] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// S�BADO
		if ((!(audienciaAgendaDt.getHorariosDuracao()[15].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[16].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[17].equals("")))) {
			if (validarHorariosDuracao("6", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[15], audienciaAgendaDt.getHorariosDuracao()[16], audienciaAgendaDt.getHorariosDuracao()[17])) {
				logHorariosDuracao += "S�bado: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[15] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[16] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[17] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}
		// DOMINGO
		/* Verificar se os campos (hor�rios inicial e final e dura��o) foram preenchidos */
		if ((!(audienciaAgendaDt.getHorariosDuracao()[18].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[19].equals(""))) || (!(audienciaAgendaDt.getHorariosDuracao()[20].equals("")))) {
			// Verificar se os campos (hor�rios inicial e final e dura��o) s�o valores v�lidos
			if (validarHorariosDuracao("7", audienciaAgendaDt.getDataInicial(), audienciaAgendaDt.getHorariosDuracao()[18], audienciaAgendaDt.getHorariosDuracao()[19], audienciaAgendaDt.getHorariosDuracao()[20])) {
				// Preparar o log com os dados informados pelo usu�rio no cadastro de agendas para audi�ncias
				logHorariosDuracao += "Domingo: Hor�rio Inicial: " + audienciaAgendaDt.getHorariosDuracao()[18] + "; Hor�rio Final: " + audienciaAgendaDt.getHorariosDuracao()[19] + "; Dura��o: " + audienciaAgendaDt.getHorariosDuracao()[20] + " - ";
				// Incrementar o atributo horariosDuracaoInformadosValidos
				horariosDuracaoInformadosValidos++;
			}
		}

		// SET LOG HOR�RIOS/DURA��O
		audienciaAgendaDt.setLogHorariosDuracao(logHorariosDuracao);

		// Mensagem caso nenhum dia da semana tenha sido informado
		if (horariosDuracaoInformadosValidos == 0) {
			retornoValidacao += "Informe os dias da semana (hor�rios inicial e final e dura��o) de acordo com o per�odo informado (datas inicial e final). ";
		}

		return horariosDuracaoInformadosValidos;
	}

	/**
	 * M�todo respons�vel por validar os hor�rios inicial e final e a dura��o das audi�ncias informados pelo usu�rio no cadastro de agendas de
	 * audi�ncias
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

		// HOR�RIO INICIAL
		// Verificar se o campo do hor�rio inicial foi informado
		if (validacaoHoraAgenda.validarHorarioObrigatorio(horarioInicial)) {
			/*
			 * Verificar se o hor�rio inicial informado possui o formato correto: hh:MM, ou seja, ele possui 4 d�gitos
			 */
			if (validacaoHoraAgenda.validarFormatoHorario(horarioInicial)) {
				/*
				 * Verificar se o valor do hor�rio inicial � consistente, ou seja, � igual ou superior ao hor�rio corrente
				 */
				if (validacaoHoraAgenda.validarHorarioInicialConsistente(diaDaSemana, dataInicial, horarioInicial)) {
					horarioInicialValido = true;
				} else {
					horarioInicialValido = false;
					retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HOR�RIO INICIAL deve ser igual ou superior ao hor�rio corrente.\n";
				}
			} else {
				horarioInicialValido = false;
				retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HOR�RIO INICIAL n�o possui o formato hh:mm.\n";
			}
		} else {
			horarioInicialValido = false;
			retornoValidacao += "O campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HOR�RIO INICIAL � obrigat�rio.\n";
		}

		// DURA��O DAS AUDI�NCIAS
		boolean duracaoAudienciaOk = false;
		// Verificar se a audi�ncia foi informada e � consistente
		if (verificarDuracaoAudiencia(diaDaSemana, duracaoAudiencia, horarioInicial, horarioFinal)) {
			duracaoAudienciaOk = true;
		}

		// HOR�RIO FINAL
		// Verificar se o hor�rio final foi informado
		if (validacaoHoraAgenda.validarHorarioObrigatorio(horarioFinal)) {
			/*
			 * Verificar se o hor�rio final informado possui o formato correto:hh: MM, ou seja, ele possui 4 d�gitos
			 */
			if (validacaoHoraAgenda.validarFormatoHorario(horarioFinal)) {
				if (duracaoAudienciaOk) {
					/*
					 * Verificar se o hor�rio final � igual ou superior ao seguinte somat�rio: hor�rio inicial + dura��o das audi�ncias
					 */
					if (validacaoHoraAgenda.validarHorarioFinalConsistente(horarioInicial, horarioFinal, duracaoAudiencia)) {
						horarioFinalValido = true;
					} else {
						horarioFinalValido = false;
						retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HOR�RIO FINAL deve ser igual ou superior ao seguinte somat�rio: hor�rio inicial + dura��o.\n";
					}
				}
			} else {
				horarioInicialValido = false;
				retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HOR�RIO FINAL n�o possui o formato hh:mm.\n";
			}
		} else {
			horarioInicialValido = false;
			retornoValidacao += "O campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - HOR�RIO FINAL � obrigat�rio.\n";
		}
		return horarioInicialValido & horarioFinalValido;
	}

	/**
	 * M�todo respons�vel por validar a dura��o das audi�ncias informada pelo usu�rio no cadastro de agendas de audi�ncias
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
		 * Verificar se a dura��o das audi�ncias precisa ser verificada, ou seja, se pelo menos um dos hor�rios, inicial ou final, tenha sido
		 * informado
		 */
		if ((!(horarioInicial.length() == 0)) && (!(horarioFinal.length() == 0))) {
			// Verificar se a dura��o da audi�ncia foi informada corretamente
			if (validacaoDuracaoAudiencia.validarDuracaoConsistente(duracaoAudiencia)) {
				duracaoAudienciaValida = true;			
			} else {
				retornoValidacao += "O valor do campo " + DiaDaSemana.getDiaDaSemanaDescricao(Funcoes.StringToInt(diaDaSemana)) + " - DURA��O deve ser igual ou superior a 15(quinze).\n";
			}
		}
		return duracaoAudienciaValida;
	}

	/**
	 * M�todo respons�vel por abrir uma conex�o com o banco de dados para buscar as audi�ncias livres de um dado tipo e de um cargo da serventia
	 * (serventia na qual o usu�rio corrente est� logado)
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
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			// CONSULTAR AUDI�NCIAS LIVRES DADO O CARGO DA SERVENTIA E O TIPO DA AUDI�NCIA			
			listaAudienciasLivres = obPersistencia.consultarAudienciasLivres(id_AudienciaTipo, id_ServentiaCargo, id_Serventia, posicaoPaginaAtual);
			// DEFINIR A QUANTIDADE DE P�GINAS
			QuantidadePaginas = (Long) listaAudienciasLivres.get(listaAudienciasLivres.size() - 1);
			// REMOVER DA LISTA DE AUDI�NCIAS CONSULTADAS O REGISTRO QUE INDICA A QUANTIDADE DE P�GINAS
			listaAudienciasLivres.remove(listaAudienciasLivres.size() - 1);
		} finally {
			// FECHER CONEX�O
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciasLivres;
	}

	/**
	 * M�todo respons�vel por consultar os cargos de uma serventia, para o qual podem sem criadas agendas de audi�ncias
	 * 
	 * @author Keila Sousa Silva
	 * @param nomeBusca
	 * @param posicaoPaginaAtual
	 * @return List listaServentiaCargos: lista de cargos da serventia do usu�rio
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
	 * M�todo respons�vel em consultar as audi�ncias do tipo Sess�o do 2� Grau que est�o abertas, ou seja, aquelas onde a DataMovimentacao � nula.
	 * 
	 * @param id_Serventia, serventia do usu�rio que est� consultando as sess�es
	 * @param grupoCodigo, c�digo do grupo do usu�rio logado
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
					// Consultar sess�es abertas nas serventias que tenham a serventia do usu�rio como relacionada
					listaSessoesAbertas = obPersistencia.consultarSessoesAbertasCamaras(id_Serventia, ordemDataInversa);
					break;

				default:
					// Consultar sess�es abertas de acordo com serventia do usu�rio
					listaSessoesAbertas = obPersistencia.consultarSessoesAbertas(id_Serventia, ordemDataInversa);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesAbertas;
	}
	
	/**
	 * M�todo respons�vel em consultar as audi�ncias, diferenciando as Virtuais, do tipo Sess�o do 2� Grau que est�o abertas, ou seja, aquelas onde a DataMovimentacao � nula.
	 * 
	 * @param id_Serventia, serventia do usu�rio que est� consultando as sess�es
	 * @param grupoCodigo, c�digo do grupo do usu�rio logado
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
					// Consultar sess�es abertas nas serventias que tenham a serventia do usu�rio como relacionada
					listaSessoesAbertas = obPersistencia.consultarSessoesAbertasCamaras(id_Serventia, ordemDataInversa);
					break;

				default:
					// Consultar sess�es abertas de acordo com serventia do usu�rio
					listaSessoesAbertas = obPersistencia.consultarSessoesVirtuaisAbertas(id_Serventia, ordemDataInversa, naoTrazerIniciadas);
					break;
			}

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaSessoesAbertas;
	}

	/**
	 * M�todo respons�vel por gerar as datas de acordo com os dados fornecidos pelo usu�rio no cadastro de agendas de audi�ncias. Essas datas ser�o
	 * utilizadas para gerar as agendas para audi�ncias, ou seja, as audi�ncias livres
	 * 
	 * @author Jesus Rodrigo
	 * @author Keila Sousa Silva
	 * @param dataInicial
	 * @param dataFinal
	 * @param quantidadeAudenciasSimultaneas
	 * @param horariosDuracao
	 * @return List listaDatas: lista com as datas geradas com os dados informados pelo usu�rio no cadastro de agendas para audi�ncias
	 * @throws Exception
	 */
	private List gerarDatasAgendasAudiencias(String dataInicial, String dataFinal, String quantidadeAudenciasSimultaneas, String[] horariosDuracaoInformados) throws Exception{
		List listaDatas = new ArrayList();
		Date dataFim = Funcoes.StringToDate(dataFinal);
		/*
		 * 0 = Segunda, 3 = Ter�a, 6 = Quarta, 9 = Quinta, 12 = Sexta, 15 = S�bado, 18 = Domingo
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
			 * Buscar no array "posi��o" o dia da semana a ser tratado (Segunda = 0, Ter�a = 3, Quarta = 6, Quinta = 9, Sexta = 12, S�bado = 15,
			 * Domingo = 18)
			 */
			if ((calendarioGeral.get(Calendar.DAY_OF_WEEK)) != 1) {
				diaDaSemana = posicao[(calendarioGeral.get(Calendar.DAY_OF_WEEK)) - 2];
			} else {
				diaDaSemana = posicao[(calendarioGeral.get(Calendar.DAY_OF_WEEK)) + 5];
			}

			// Verificar se os hor�rios inicial e final e a dura��o do dia da semana em quest�o foram informados
			if (!horariosDuracaoInformados[diaDaSemana].equals("")) {
				calendarioInicial.setTime(calendarioGeral.getTime());
				calendarioLimiteMaximo.setTime(calendarioGeral.getTime());
				calendarioFinal.setTime(calendarioGeral.getTime());
				
				//setando 00:00h para evitar conflitos com hor�rio de ver�o
				calendarioInicial.set(Calendar.HOUR_OF_DAY, 0);
				calendarioLimiteMaximo.set(Calendar.HOUR_OF_DAY, 0);
				calendarioFinal.set(Calendar.HOUR_OF_DAY, 0);

				// Set hor�rios inicial e final e dura��o da audi�ncia
				String horarioInicial = horariosDuracaoInformados[diaDaSemana];
				String horarioFinal = horariosDuracaoInformados[diaDaSemana + 1];
				int duracaoAudiencia = Funcoes.StringToInt(horariosDuracaoInformados[diaDaSemana + 2]);
				// Set hor�rios inicial e final e dura��o da audi�ncia
				calendarioInicial.add(Calendar.MINUTE, Funcoes.horaToMinuto((horarioInicial)));
				calendarioLimiteMaximo.add(Calendar.MINUTE, Funcoes.horaToMinuto((horarioInicial)));
				calendarioFinal.add(Calendar.MINUTE, Funcoes.horaToMinuto(horarioFinal));

				// Verificar se a data e hor�rio da pr�xima agenda de audi�ncia estouraram o limite definido na data final e no hor�rio final
				while (calendarioLimiteMaximo.getTime().before(calendarioFinal.getTime())) {
					// Gerar agendas. Podem haver agendas simult�neas ou n�o
					for (int j = 0; j < Funcoes.StringToInt(quantidadeAudenciasSimultaneas); j++) {
						listaDatas.add(new Date(calendarioInicial.getTime().getTime()));
					}
					// Preparar o pr�ximo hor�rio de agenda de audi�ncia
					calendarioInicial.add(Calendar.MINUTE, duracaoAudiencia);
					calendarioLimiteMaximo.add(Calendar.MINUTE, duracaoAudiencia);
				}
			}
			calendarioGeral.add(Calendar.DAY_OF_WEEK, 1);
		} while (calendarioGeral.getTimeInMillis() <= dataFim.getTime());

		return listaDatas;
	}

	/**
	 * M�todo respons�vel por reservar/desreservar a(s) audi�ncia(s) livre(s) [agenda(s) livre(s)] selecionadas. Reservar uma agenda livre para
	 * audi�ncia significa alterar seu campo 'Reservada' para '1(true)'. Desreservar uma agenda livre para audi�ncia significa alterar seu campo
	 * 'Reservada' para '0(false)'
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciasAgendasReservar
	 * @param logDt
	 * @throws Exception 
	 */
	public void reservarAudienciaAgendaLivre(List audienciasAgendasReservar, LogDt logDt) throws Exception{
		AudienciaDt audienciaDtReservar = null;

		// Preparar para reservar ou desreservar a audi�ncia livre
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

			// Salvar = Alterar audi�ncia = Reservar/Desreservar agenda livre
			salvar(audienciaDtReservar);
		}
	}

//	/**
//	 * M�todo respons�vel por preparar as agendas para audi�ncias, ou seja, as audi�ncias livres que ser�o criadas. Nesse m�todo ser�o criadas
//	 * inst�ncias do objeto AudienciaDt, de acordo com a quantidade de datas geradas com os dados informados pelo usu�rio no cadastro de agendas para
//	 * audi�ncias. Esses objetos ser�o preparados e retornados numa lista para poderem ser salvos
//	 * 
//	 * @author Keila Sousa Silva
//	 * @param listaDatas
//	 * @param audienciaDt
//	 * @return List listaAgendasAudienciasGeradas: lista das agendas para audi�ncias geradas
//	 * @throws Exception
//	 */
//	private List buscarAgendas AudienciasGeradas(List listaDatas, AudienciaAgendaDt audienciaDt){
//		List listaAgendasAudienciasGeradas = new ArrayList();
//		Iterator iterator = listaDatas.iterator();
//		while (iterator.hasNext()) {
//			// AUDI�NCIADT
//			AudienciaDt audienciaDtGerar = new AudienciaDt();
//			// Tipo da audi�ncia
//			audienciaDtGerar.setId_AudienciaTipo(audienciaDt.getId_AudienciaTipo());
//			// Data da agenda (Data da audi�ncia livre)
//			audienciaDtGerar.setDataAgendada(Funcoes.DataHora((Date) iterator.next()));
//			// Id da Serventia
//			audienciaDtGerar.setId_Serventia(audienciaDt.getId_Serventia());
//			// Log
//			audienciaDtGerar.setId_UsuarioLog(audienciaDt.getId_UsuarioLog());
//			audienciaDtGerar.setIpComputadorLog(audienciaDt.getIpComputadorLog());
//
//			// AUDIENCIAPROCESSODT
//			/*
//			 * No caso de gera��o manual de agendas de audi�ncias, cada audi�ncia possui apenas uma refer�ncia � tabela "AudienciaProcesso", ou seja,
//			 * a lista de objetos do tipo "AudienciaProcessoDt" contida no objeto do tipo "AudienciaDt" possuir� apenas um registro do tipo
//			 * "AudienicaProcessoDt". Isso se deve ao fato de que somente audi�ncias do tipo "Sess�o de 2� Grau" possuem v�rias refer�ncias � tabela
//			 * "AudienciaProcesso" e esse tipo de agenda de audi�ncia ser� criada automaticamente.
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
	 * M�todo respons�vel por excluir a(s) audi�ncia(s) livre(s) [agenda(s) livre(s)] selecionadas
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciasAgendasExcluir
	 * @param logDt
	 * @throws Exception
	 */
	public void excluirAudienciaAgendaLivre(List audienciasAgendasExcluir, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());

			for (int i = 0; i < audienciasAgendasExcluir.size(); i++) {
				/*
				 * SET AUDIENCIADT A SER EXCLU�DA
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
				// Excluir audi�ncia(s) livre(s) [agenda(s) livre(s)]
				obPersistencia.excluir(audienciaDtExcluir.getId());
				
			}
			
			// FINALIZAR TRANSA��O
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSA��O
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEX�O
			obFabricaConexao.fecharConexao();
		}
	}	

	/**
	 * Consulta data da �ltima audi�ncia de um processo de um deteminado tipo de audi�ncia
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
	 * Consulta os status dispon�veis para audi�ncia de segundo grau
	 * 
	 * @author Keila Sousa Silva
	 * @since 04/08/2009
	 * @param nomeBusca
	 * @param posicaoPaginaAtual
	 * @return List listaAudienciaProcessoStatusDt: lista contendo objetos do tipo "AudienciaProcessoStatusDt" cada qual contendo a descri��o do
	 *         status da audi�ncia de processo
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
	 * M�dodo respons�vel em agendar uma audi�ncia do tipo Sess�o do 2� grau para um processo passado. Chama m�todo para vincular o processo a
	 * audi�ncia e gera a movimenta��o de inclus�o em pauta
	 * 
	 * @param processoDt
	 *            , processo a ser vinculado com a sess�o
	 * @param id_Sessao
	 *            , audi�ncia a ser vinculada com processo
	 * @param dataAgendada
	 *            , data da nova audi�ncia a ser marcada
	 * @param id_ProcessoTipo
	 *            , id da classe (processoTipo) da nova audi�ncia a ser marcada
	 * @param processoTipo
	 *            , processo tipo da nova audi�ncia a ser marcada
	 * @param id_UsuarioServentia
	 *            , usu�rio que est� marcando a sess�o
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conex�o ativa
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
	 * M�dodo respons�vel em remarcar uma audi�ncia do tipo Sess�o do 2� grau para um processo passado. 
	 * Ao remarcar uma sess�o, o status da sess�o � modificado para "Remarcada" e uma nova sess�o � criada. 
	 * Ao final gera as movimenta��es "Retirado de pauta" e "Inclu�do em Pauta" novamente
	 * 
	 * @param processoDt, processo a ser vinculado com a sess�o
	 * @param id_NovaAudiencia, nova audi�ncia a ser vinculada com processo
	 * @param novaData, nova data da audi�ncia a ser marcada
	 * @param id_ProcessoTipo
	 *            , id da classe (processoTipo) da nova audi�ncia a ser marcada
	 * @param processoTipo
	 *            , processo tipo da nova audi�ncia a ser marcada
	 * @param usuarioDt, usu�rio que est� marcando a sess�o
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 */
	public void remarcarSessao(ProcessoDt processoDt, String id_NovaAudiencia, String id_ProcessoTipo, String processoTipo, String novaData, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao, String id_ServentiaCargoResponsavel) throws MensagemException,Exception {
		
		// Resgata a sess�o marcada para o processo e redesigna
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoNe.desmarcarSessaoProcesso(processoDt, id_ProcessoTipo, AudienciaProcessoStatusDt.REMARCADA, usuarioDt, logDt, conexao);

		// Marca nova audi�ncia
		this.marcarSessao(processoDt, id_NovaAudiencia, novaData, id_ProcessoTipo, processoTipo, usuarioDt.getId_UsuarioServentia(), logDt, conexao, usuarioDt, id_ServentiaCargoResponsavel);

	}

	/**
	 * M�dodo respons�vel em desmarcar uma audi�ncia do tipo Sess�o do 2� grau para um processo passado. 
	 * Modifica o status da audi�ncia para "Cancelada" e gera movimenta��o "Retirado de Pauta"
	 * 
	 * @param processoDt, processo a ser vinculado com a sess�o
	 * @param id_ProcessoTipo, identifica��o do processo tipo
	 * @param usuarioDt, usu�rio que est� desmarcando a sess�o
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 */
	public void desmarcarSessao(ProcessoDt processoDt, String id_ProcessoTipo, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
		
		// Resgata a sess�o marcada para o processo e desmarca
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoNe.desmarcarSessaoProcesso(processoDt, id_ProcessoTipo, AudienciaProcessoStatusDt.DESMARCAR_PAUTA, usuarioDt, logDt, conexao);

	}
	
	/**
	 * M�dodo respons�vel em retirar uma audi�ncia do tipo Sess�o do 2� grau para um processo passado. 
	 * Modifica o status da audi�ncia para "Cancelada" e gera movimenta��o "Desmarcar Sess�o"
	 * 
	 * @param processoDt, processo a ser vinculado com a sess�o
	 * @param id_ProcessoTipo, identifica��o do processo tipo
	 * @param usuarioDt, usu�rio que est� desmarcando a sess�o
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 */
	public void retirarSessao(ProcessoDt processoDt, String id_ProcessoTipo, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao conexao) throws MensagemException, Exception {
		
		// Resgata a sess�o marcada para o processo e desmarca
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoNe.retirarSessaoProcesso(processoDt, id_ProcessoTipo, AudienciaProcessoStatusDt.RETIRAR_PAUTA, usuarioDt, logDt, conexao);

	}

	/**
	 * Salva uma nova audi�ncia do tipo Sess�o 2� Grau, conforme data e hora passados
	 * 
	 * @param audienciaSegundoGrauDt
	 *            , objeto com dados da sess�o a ser criada
	 * @author msapaula
	 * @throws Exception 
	 */
	public void agendarAudienciaSegundoGrau(AudienciaSegundoGrauDt audienciaSegundoGrauDt) throws Exception{
		// Concatena data e hora da audi�ncia
		audienciaSegundoGrauDt.setDataAgendada(audienciaSegundoGrauDt.getData() + " " + audienciaSegundoGrauDt.getHora());
		audienciaSegundoGrauDt.setAudienciaTipoCodigo(String.valueOf(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()));
		audienciaSegundoGrauDt.setReservada("false");
		this.salvar(audienciaSegundoGrauDt);
	}

	/**
	 * M�todo respons�vel por estabelecer uma conex�o com o banco de dados, consultar e retornar uma inst�ncia completa do objeto do tipo
	 * "Audi�nciaDt", de acordo com o par�metro passado que indica o id da audi�ncia desejada. Como a inst�ncia ser� completa, est�ra vinculado a este
	 * objeto do tipo "Audi�nciaDt" a lista contendo objeto(s) do tipo "Audi�nciaProcessoDt". Esse m�todo ser� utilizado para consultar dados
	 * completos de uma audi�ncia livre, n�o retornando dados como processo e partes.
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
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaLivreCompleta(id_Audiencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}

	/**
	 * M�todo respons�vel em consultar e retornar uma inst�ncia completa do objeto do tipo "Audi�nciaDt", de acordo com o par�metro passado que indica
	 * o id da audi�ncia desejada. Como a inst�ncia ser� completa, est�ra vinculado a este objeto uma lista contendo objeto(s) do tipo
	 * "Audi�nciaProcessoDt", onde cada processo ter� suas partes tamb�m vinculadas.
	 * 
	 * @param id_Audiencia
	 * @return AudienciaDt audienciaDtCompleta
	 * @author msapaula
	 */
	public AudienciaDt consultarAudienciaCompleta(String id_Audiencia, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
		
			audienciaDtCompleta = obPersistencia.consultarAudienciaCompleta(id_Audiencia);
		
			//TODO: Sem necessidade, pois no ps do m�todo anterior j� seta essas duas informa��es atrav�s do m�todo associarAudienciaDt, chamado pelo m�todo getListaAudienciaAgendadaCompletaSessaoVirtual 
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
		
			//TODO: Sem necessidade, pois no ps do m�todo anterior j� seta essas duas informa��es atrav�s do m�todo associarAudienciaDt, chamado pelo m�todo getListaAudienciaAgendadaCompletaSessaoVirtual 
			//mmgomes
			/*if(audienciaDtCompleta != null) {
				audienciaDtCompleta.setVirtual(obPersistencia.isVirtual(id_Audiencia));
				audienciaDtCompleta.setSessaoIniciada(obPersistencia.isVirtualIniciada(id_Audiencia));
			}*/
		
		return audienciaDtCompleta;
	}

	// jvosantos - 24/09/2019 11:40 - Extrair m�todo para usar uma �nica conex�o
	public AudienciaDt consultarAudienciaCompleta(String id_Audiencia) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarAudienciaCompleta(id_Audiencia, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * M�todo que verifica se os dados obrigat�rios em uma movimenta��o de audi�ncia foram preenchidos. Na movimenta��o de audi�ncia (finaliza��o de
	 * Sess�o de 2� grau � obrigat�rio inserir um arquivo)
	 * 
	 * @param dados
	 *            , objeto com dados da movimenta��o a ser verificada
	 * @author msapaula
	 */
	public String verificarMovimentacaoAudiencia(AudienciaMovimentacaoDt dados){
		String stRetorno = "";
		if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) stRetorno += "� necess�rio inserir um arquivo. \n";

		return stRetorno;
	}

	/**
	 * M�todo que verifica se os dados obrigat�rios em uma movimenta��o de audi�ncia foram preenchidos
	 * 
	 * @param dados
	 *            , objeto com dados da movimenta��o a ser verificada
	 * @author msapaula
	 * @throws Exception 
	 */
	public String verificarMovimentacaoAudienciaProcesso(AudienciaMovimentacaoDt dados, UsuarioDt usuarioDt) throws Exception{
		String stRetorno = "";
		if (dados.getAudienciaStatusCodigo() != null && dados.getAudienciaStatusCodigo().length() > 0 && dados.getAudienciaStatusCodigo().equals("-1")) {
			stRetorno += "Selecione o Status da Audi�ncia. \n";
		}
		// Se for uma Sess�o de 2� grau que est� sendo remarcada, deve ser selecionada a data da nova sess�o
		else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) == AudienciaProcessoStatusDt.REMARCADA && Funcoes.StringToInt(dados.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && dados.getId_NovaSessao().length() == 0) {
			stRetorno += "Selecione a Data da nova Sess�o a ser marcada. \n";
		} else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.DESMARCAR_PAUTA && Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) != AudienciaProcessoStatusDt.RETIRAR_PAUTA  &&  dados.getAudienciaDt().devePossuirIndicadorDeAcordo()) {
			if (!(dados.isHouveAcordo() || dados.isNaoHouveAcordo())){
				stRetorno += "Selecione a op��o Houve Acordo? \n";	
			} else if (dados.isHouveAcordo() && dados.getValorAcordo().length() == 0) {
				stRetorno += "Valor do acordo deve ser maior que zero. \n";				
			}
		}	
		int grupoTipoUsuarioLogado = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		if (dados.isPreAnalise()){
			
			if (dados.getListaArquivos() != null && 
				dados.getListaArquivos().size() > 0) {
				stRetorno = "N�o � poss�vel salvar uma pr�-an�lise com arquivos assinados. \n";
			} else {
				if (dados.getId_ArquivoTipo() == null || dados.getId_ArquivoTipo().trim().length() == 0 || dados.getArquivoTipo() == null || dados.getArquivoTipo().trim().length() == 0 ){
					stRetorno += "Selecione o Tipo do Arquivo: Ac�rd�o, Relat�rio e Voto. Favor reabrir a funcionalidade para recarregar os tipos.\n";
				}				
				/*if(dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())){
					stRetorno += "� necess�rio redigir o texto da pr�-an�lise: Ac�rd�o, Relat�rio e Voto. \n";
				}*/
				
				
				if( (grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA || usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL)){
					
					String ementa = "class=\"ementa\"";
					
					
					if(dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())){
						stRetorno += "� necess�rio redigir o texto da pr�-an�lise. \n";
					}else{
						if( !dados.getTextoEditor().contains(ementa)){
							stRetorno += "Para redigir pr�-an�lise � necess�rio definir a ementa \n";
						}
					}
					

				} else{

					if (dados.getId_ArquivoTipoEmenta() == null || dados.getId_ArquivoTipoEmenta().trim().length() == 0 || dados.getArquivoTipoEmenta() == null || dados.getArquivoTipoEmenta().trim().length() == 0 ){
						stRetorno += "Selecione o Tipo do Arquivo: Ementa. \n";
					}				
					if(dados.getTextoEditorEmenta() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditorEmenta())){
						stRetorno += "� necess�rio redigir o texto da pr�-an�lise: Ementa. \n";
					}
					
				}
			}

		} else {
				if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) {
					stRetorno += "� necess�rio inserir um arquivo. \n";
				} else 
					if( (grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA || usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL ) &&  !dados.isRetiradaDePauta() ){						
						ArquivoDt arquivo = (ArquivoDt)dados.getListaArquivos().get(0);
						String conteudo = new String(arquivo.getConteudo());
						
						String ementa = "class=\"ementa\"";
						
						if(!conteudo.contains(ementa)){
							stRetorno += "Para concluir audi�ncia � necess�rio definir a ementa. \n";
						}
					}
					
					if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU && dados.getListaArquivos()  != null && dados.getListaArquivos().size() > 1){
					if(!(usuarioDt.getServentiaSubtipoCodigo() != null &&
							(Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL)))
						stRetorno = "� necess�rio inserir apenas um arquivo correspondente � Ata. \n";			
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
					if (quantidadeEmenta == 0) stRetorno = "� necess�rio inserir um arquivo assinado correspondente � Ementa. \n";
					else if (quantidadeEmenta > 1) stRetorno = "� permitido inserir apenas um arquivo correspondente � Ementa. \n";					
				}
		}		
		
		return stRetorno;
	}

	/**
	 * Consulta os dados de uma Audi�nciaProcesso necess�rio para movimenta��o, devendo retornar dados da Audiencia, da AudienciaProcesso, Processo e
	 * suas partes
	 * 
	 * @param id_AudienciaProcesso
	 *            , identifica��o da audi�ncia que est� sendo movimentada
	 * @return AudienciaDt audienciaCompleta
	 * @author msapaula
	 */
	public AudienciaDt consultarAudienciaProcessoCompleta(String id_AudienciaProcesso) throws Exception {
		AudienciaDt audienciaDtCompleta = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaPs obPersistencia= new  AudienciaPs(obFabricaConexao.getConexao());
			audienciaDtCompleta = obPersistencia.consultarAudienciaProcessoCompleta(id_AudienciaProcesso);
			if (audienciaDtCompleta == null) throw new MensagemException("N�o foi poss�vel localizar a audi�ncia do processo com o id igual a " + id_AudienciaProcesso + ".");
			audienciaDtCompleta.setVirtual(obPersistencia.isVirtual(audienciaDtCompleta.getId()));
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return audienciaDtCompleta;
	}

	/**
	 * Verifica se uma movimenta��o pode ser efetuada, baseada nas pend�ncias selecionadas para serem geradas Ex.: Autos Conclusos - deve verificar se
	 * processo j� est� concluso
	 * 
	 * @param audienciaMovimentacaoDt
	 *            , objeto com dados da movimenta��o
	 * @param grupoCodigo
	 *            , grupo do usu�rio que est� movimentando a audi�ncia
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
					stRetorno = "A movimenta��o s� � permitida na serventia CEJUSC da audi�ncia. Na serventia do processo s� � permitida a op��o Desmarcada / Retirada de Pauta.";
				}
			}
		}
		
		if (stRetorno.length() == 0) {
			List pendenciasGerar = audienciaMovimentacaoDt.getListaPendenciasGerar();
			// Para cada pend�ncia marcada para ser gerada verifica se h� alguma restri��o
			if (pendenciasGerar != null && pendenciasGerar.size() > 0) {
				PendenciaNe pendenciaNe = new PendenciaNe();
				ProcessoDt processoDt = audienciaProcessoDt.getProcessoDt();
				stRetorno = pendenciaNe.verificaPendenciasProcesso(processoDt, pendenciasGerar, grupoCodigo, false);
			}	
		}
				
		return stRetorno;
	}

	/**
	 * Salva movimenta��o de audi�ncia. Para Audi�ncias de 1� grau refere-se a movimenta��o de "Audiencia" e "AudienciaProcesso", j� no caso de
	 * sess�es de 2� grau essa movimenta��o refere-se somente a um processo da sess�o. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimenta��o correspondente e pend�ncias de acordo com o que foi selecionado pelo usu�rio
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt, usu�rio que est� realizando a movimenta��o
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
					
					// Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
					audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);

					// Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia
					// e inserir respons�veis pela audi�ncia
					MovimentacaoDt movimentacaoDt = movimentarAudiencia(audienciaDt, audienciaMovimentacaoDt.getAudienciaStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
					movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
					movimentacoes.add(movimentacaoDt);
					String visibilidade=null;
					if (processoDt.isSegredoJustica()){
						visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
					}
					// Salvando v�nculo entre movimenta��o e arquivos inseridos					
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);					

					// Salvando pend�ncias da movimenta��o
					if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) {
						pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
					}

				}
				
//				// Gera recibo para arquivos de movimenta��es
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);

				// Dependendo do tipo da Audi�ncia chama m�todo para gerar Movimenta��o correspondente
				int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
				codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());
				if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal()) {
					if (codigoStatus!=AudienciaProcessoStatusDt.DESMARCAR_PAUTA && codigoStatus!=AudienciaProcessoStatusDt.RETIRAR_PAUTA && codigoStatus != AudienciaProcessoStatusDt.REMARCADA){
						pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);						
					}
				}
				
			} else {
				AudienciaDt audienciaDt = audienciaMovimentacaoDt.getAudienciaDt();
				
				// Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
				ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
				audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
				
				//Consulta Voto 2� Grau ou Elaboracao de Voto
				PendenciaDt pendenciaDt = null;
				if (audienciaMovimentacaoDt.getPendenciaArquivoDt() != null)
					pendenciaDt = pendenciaNe.consultarId(audienciaMovimentacaoDt.getPendenciaArquivoDt().getId_Pendencia());
						

				LogDt logDt = new LogDt(usuarioDt.getId(), audienciaMovimentacaoDt.getIpComputadorLog());
				
				arquivos = audienciaMovimentacaoDt.getListaArquivos();
				// Salva arquivos inseridos
				if (arquivos != null && arquivos.size() > 0) {
					movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
				}

				// Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia
				// e inserir respons�veis pela audi�ncia
				MovimentacaoDt movimentacaoDt = movimentarAudiencia(audienciaDt, audienciaMovimentacaoDt.getAudienciaStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
				movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
				movimentacoes.add(movimentacaoDt);
				
				//Fechar pendencia do Tipo Voto 2� Grau ou Elaboracao de Voto
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
				// Salvando v�nculo entre movimenta��o e arquivos inseridos
				movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade,logDt, obFabricaConexao);

				// Salvando pend�ncias da movimenta��o
				if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) {
					pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
				}

				// Atualiza Classificador processo
				if (audienciaMovimentacaoDt.getId_Classificador().length() > 0) {
					new ProcessoNe().alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
				}

//				// Gera recibo para arquivos de movimenta��es
//				movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
				
				//--segundo grau gerar publica��o das decis�es
			    int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
		        codigoStatus = Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo());

		        // Dependendo do tipo da Audi�ncia chama m�todo para gerar Movimenta��o correspondente
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
	 * Salva movimenta��o de audi�ncia, ou melhor, a finaliza��o de Sess�es de 2� Grau, pois estas podem ser finalizadas ou movimentadas separadamente
	 * de cada processo a ser julgado na sess�o.
	 * 
	 * @param audienciaMovimentacaoDt
	 *            , objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt
	 *            , usu�rio que est� realizando a movimenta��o
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
			
			//lrcampos 06/11/2019 16:40 - Caso a sess�o for virtual finalizar pendencias de Conhecimento
			if(audienciaDt.isVirtual()) {
				pendenciaNe.finalizarPendenciasParaConhecimentoSessaoVirtual(audienciaDt, usuarioDt, obFabricaConexao);
			}
			//Altera��o devido a unifica��o de met�dos de gerar publicao do Jesus do dia 25/02/2019***
			Iterator itArquivos = arquivos.iterator();
  			ArquivoNe arquivoNe = new ArquivoNe();
  			//salvo todos os arquivos que ser�o publicados
  			while (itArquivos.hasNext()) {
   				// Pega o arquivo da lista
   				ArquivoDt arquivo = (ArquivoDt) itArquivos.next();

   				// Inseri o novo arquivo
   				arquivoNe.inserir(arquivo, logDt, obFabricaConexao);
  			}
			//**********************************************************************
  			
			// Cria publica��o dos arquivos da Sess�o
			pendenciaNe.salvarPublicacao(new PendenciaDt(),arquivos, usuarioDt, logDt, obFabricaConexao);
			
			String id_ArquivoFinalizacao = ""; 
			if(arquivos !=null && arquivos.size() > 0){
				ArquivoDt arquivo = (ArquivoDt)arquivos.get(0);
				if (arquivo != null && arquivo.getId() != null && arquivo.getId().trim().length() > 0) id_ArquivoFinalizacao = arquivo.getId();
			}
			
			// Atualiza dados em "Audiencia"
			String valorAtual = audienciaDt.getPropriedades();
			// Seta da movimenta��o com a data atual
			audienciaDt.setDataMovimentacao(Funcoes.DataHora(new Date()));
			// Seta o id do arquivo de finaliza��o da sess�o de segundo grau
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
	 * M�todo respons�vel em efetuar a��es comuns em uma movimenta��o de audi�ncia, valendo tamb�m para sess�es de 2� grau. Chama m�todo para
	 * Atualizar Dados em "Audiencia", "AudienciaProcesso", gera movimenta��o correspondente a movimenta��o da audi�ncia e insere respons�veis pela
	 * audi�ncia
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

		// Insere respons�veis pela audi�ncia, ou seja, quem realizou
		AudienciaProcessoResponsavelNe audienciaProcessoResponsavelNe = new AudienciaProcessoResponsavelNe();
		
		// jvosantos - 25/07/2019 17:42 - Se a sess�o for virtual, usa o relator da AUDI_PROC
		if(audienciaDt.isVirtual()) {
			ServentiaCargoDt relator = (new ServentiaCargoNe()).consultarId(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), conexao);
			audienciaProcessoResponsavelNe.inserirAudienciaProcessoResponsavel(audienciaDt.getAudienciaProcessoDt().getId(), String.valueOf(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU), relator.getId_UsuarioServentia(), logDt, conexao);
		} else {
			audienciaProcessoResponsavelNe.salvarAudienciaProcessoResponsavel(audienciaProcessoDt.getId(), audienciaDt.getAudienciaTipoCodigo(), audienciaProcessoDt.getProcessoDt(), usuarioDt, logDt, conexao);
		}

		int codigoStatus = AudienciaProcessoStatusDt.NENHUM;
		codigoStatus = Funcoes.StringToInt(audienciaStatusCodigo);

		// Dependendo do tipo da Audi�ncia chama m�todo para gerar Movimenta��o correspondente -- confirmar com JESUS
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
				
				// Testa se � uma situa��o de encaminhamento de audi�ncias para o Cejusc por meio de pend�ncia. Se for, gera uma pend�ncia "retornar" diferente.
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
	 * M�todo respons�vel em gerar movimenta��o em um processo de acordo com o Status da Audi�ncia de 1� Grau. Ainda de acordo com o status, a
	 * movimenta��o poder� ser gerada para o juiz para efeito de estat�stica.
	 * 
	 * @param audienciaDt
	 *            , audi�ncia que est� sendo movimentada
	 * @param codigoStatus
	 *            , status selecionado para audi�ncia
	 * @param usuarioDt
	 *            , usu�rio que est� movimentando audi�ncia
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conex�o ativa
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
		
		//se audiencia de concilia��o conta para quem movimentou, exceto quando tem senten�a
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
			complemento += " - Carta Precat�ria";
		}
		// De acordo com o status da audi�ncia realiza tratamentos
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
				//Setando descri��o da movimenta��o para gerar complemento corretamente
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada sem Acordo");
				
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
				//Setando descri��o da movimenta��o para gerar complemento corretamente
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada com Acordo");
				break;
				
			case AudienciaProcessoStatusDt.DESMARCAR_PAUTA:
			case AudienciaProcessoStatusDt.RETIRAR_PAUTA:
				complemento = "Desmarcada - " + complemento;
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaDesmarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
				// Setando descri��o da movimenta��o para gerar complemento corretamente
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Desmarcada");
				break;

			case AudienciaProcessoStatusDt.REMARCADA:
				complemento = "Remarcada - " + complemento;
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRemarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Remarcada");
				break;

			case AudienciaProcessoStatusDt.NEGATIVADA:
				complemento = "N�o Realizada - " + complemento;
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaNegativa(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Negativa");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO:
				complemento = "Realizada com Senten�a com M�rito - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaComMerito(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada com Senten�a com M�rito");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO:
				complemento = "Realizada com Senten�a sem M�rito - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaSemMerito(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada com Senten�a sem M�rito");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO:
				complemento = "Realizada com Homologa��o - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaHomologacao(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada com Senten�a de Homologa��o");
				break;
				
			case AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA:
				complemento = "Realizada com Senten�a - " + complemento;
				id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
				//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSentencaHomologacao(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
				//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada com Senten�a de Homologa��o");
				break;

			case AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA:
				complemento = "Realizada sem Senten�a - " + complemento;
//				if ((usuarioDt.getServentiaSubtipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL))
//						|| usuarioDt.getServentiaSubtipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL)))
//						&& processoDt.getAreaCodigo().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))){
					/*ROAD 186830 - Determinando que seja contabilizado na Estat�stica do Magistrado as "Audi�ncias realizadas sem senten�a",
					 *  mesmo que a movimenta��o seja realizada por servidores, nos moldes do que j� ocorre com as  "audi�ncias realizadas com senten�a" 
					 */
				
					if (conciliacao) {
						movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, audienciaDt, logDt, conexao);
					} else {
						id_JuizResponsavelProcesso = getResponsavelMovimentacaoAudiencia(processoDt, audienciaDt.getAudienciaTipoCodigo(), usuarioDt, conexao);
						movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaStatus(processoDt.getId(), id_JuizResponsavelProcesso, complemento, audienciaDt, logDt, conexao);
					}
						//movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSemSentenca(processoDt.getId(), id_JuizResponsavelProcesso, complemento, logDt, conexao);
					//movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada sem Senten�a");
//				} else {
//					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaRealizadaSemSentenca(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, conexao);
//					movimentacaoAudiencia.setMovimentacaoTipo("Audi�ncia Realizada sem Senten�a");
//				}
				break;
		}

		return movimentacaoAudiencia;

	}

	/**
	 * M�todo respons�vel em gerar movimenta��o em um processo de acordo com o Status da Sess�o que est� sendo julgada. Ainda de acordo com o status,
	 * a movimenta��o poder� ser gerada para o Relator do processo para efeito de estat�stica.
	 * 
	 * @param audienciaDt
	 *            , sess�o que est� sendo movimentada
	 * @param codigoStatus
	 *            , status selecionado para sess�o
	 * @param id_NovaSessao
	 *            , id da nova sess�o a ser marcada
	 * @param novaDataSessao
	 *            , nova data da sess�o a ser marcada
	 * @param usuarioDt
	 *            , usu�rio que est� movimentando a sess�o
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conex�o ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private MovimentacaoDt movimentarSessao(AudienciaDt audienciaDt, int codigoStatus, String id_NovaSessao, String novaDataSessao, UsuarioDt usuarioDt, boolean EhInsercaoExtratoAta, boolean houveAlteracaoExtratoAta, LogDt logDt, FabricaConexao conexao) throws Exception{
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();

		MovimentacaoDt movimentacaoAudiencia = null;
		String id_ServentiaCargoResponsavelSessao = null;
		
		ProcessoDt processoDt = audienciaDt.getAudienciaProcessoDt().getProcessoDt();		

		// De acordo com o status da audi�ncia realiza tratamentos
		switch (codigoStatus) {
			case AudienciaProcessoStatusDt.RETIRAR_PAUTA:
			case AudienciaProcessoStatusDt.REMARCADA:
				if (audienciaDt.isVirtual()) {
					AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();	
					ProcessoParteNe processoParteNe = new ProcessoParteNe();
					String tipoAudi = processoParteNe.consultaClasseProcessoIdAudiProc(audienciaDt.getAudienciaProcessoDt().getId(), conexao);
					
					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaSessaoRetirada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), audienciaProcessoNe.getDescricaoMovimentacaoSessaoVirtual("Sess�o do dia", audienciaDt.getDataAgendada(), tipoAudi), logDt, conexao);
				} else {
					movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaSessaoRetirada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), "(Sess�o do dia " + audienciaDt.getDataAgendada() + ")", logDt, conexao);
				}				
				
				// Setando descri��o da movimenta��o para gerar complemento corretamente
				movimentacaoAudiencia.setMovimentacaoTipo("RETIRADO DE PAUTA");
				break;
			case AudienciaProcessoStatusDt.DESMARCAR_PAUTA:			
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoAudienciaSessaoDesmarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), "(Sess�o do dia " + audienciaDt.getDataAgendada() + ")", logDt, conexao);
				// Setando descri��o da movimenta��o para gerar complemento corretamente
				movimentacaoAudiencia.setMovimentacaoTipo("DESMARCADO DE PAUTA");
				break;
			default: 												
				// Consulta o relator respons�vel pelo processo, pois ele ser� respons�vel pela movimenta��o da audi�ncia
				if(audienciaDt.isVirtual() && !EhInsercaoExtratoAta) { // jvosantos - 25/07/2019 16:18 - Corre��o para pegar o ID_Serv_cargo do relator da sess�o
					id_ServentiaCargoResponsavelSessao = (new ServentiaCargoNe()).consultarId(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), conexao).getId_UsuarioServentia();
				} else { 
					id_ServentiaCargoResponsavelSessao = usuarioDt.getId_UsuarioServentia();
				}
				
				movimentacaoAudiencia = movimentacaoNe.gerarMovimentacaoSessao(processoDt.getId(), id_ServentiaCargoResponsavelSessao, "(Sess�o do dia " + audienciaDt.getDataAgendada() + ")",houveAlteracaoExtratoAta, logDt, codigoStatus , conexao);									
				
		}

		return movimentacaoAudiencia;

	}

	/**
	 * M�todo respons�vel em capturar o usu�rio que ser� respons�vel pela movimenta��o de uma Audi�ncia. Isso deve ser feito pois se um Analista
	 * movimenta uma audi�ncia, a movimenta��o deve contar nas estat�sticas para o juiz do processo ou o relator.
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param audienciaTipoCodigo, tipo da audi�ncia que est� sendo movimentada
	 * @param usuarioDt, usu�rio que est� movimentando
	 * @param conexao, conex�o ativa
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private String getResponsavelMovimentacaoAudiencia(ProcessoDt processoDt, String audienciaTipoCodigo, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception{
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		String id_UsuarioServentia = null;

		if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal()) {
			
			// Se usu�rio que est� movimentando uma sess�o n�o � o juiz, a movimenta��o deve ser para ele
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.JUIZES_TURMA_RECURSAL) {
			if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.JUIZ_TURMA && Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) != GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
				
				//Captura a serventia do processo
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());

				if ( ServentiaSubtipoDt.isSegundoGrau(serventiaDt.getServentiaSubtipoCodigo()) ||  ServentiaSubtipoDt.isUPJTurmaRecursal(serventiaDt.getServentiaSubtipoCodigo())) {
					// Quando se tratar de sess�o de 2� em C�maras, captura o relator na tabela ProcessoResponsavel com CargoTipo Relator
					id_UsuarioServentia = responsavelNe.getUsuarioRelatorResponsavelCargoTipo(processoDt.getId(), conexao);
				} else {
					// Consulta o relator respons�vel pelo processo, pois ele ser� respons�vel pela movimenta��o da audi�ncia
					id_UsuarioServentia = responsavelNe.getUsuarioRelatorResponsavelProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), conexao);
				}
				if (id_UsuarioServentia == null) throw new MensagemException("Cargo respons�vel pelo processo est� vazio. N�o � poss�vel movimentar Sess�o.");
			} else id_UsuarioServentia = usuarioDt.getId_UsuarioServentia();
		} else {

			// Se usu�rio que est� movimentando uma Audi�ncia de 1� Grau n�o � o juiz, a movimenta��o deve ser para ele
			//if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.JUIZES_VARA) {
			  if (!usuarioDt.isMagistrado()) {
				// Consulta o juiz respons�vel pelo processo, pois ele ser� respons�vel pela movimenta��o da audi�ncia
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
					throw new MensagemException("Cargo respons�vel pelo processo est� vazio. N�o � poss�vel movimentar Audi�ncia.");
				}
			} else{
				id_UsuarioServentia = usuarioDt.getId_UsuarioServentia();
			}
		}
		return id_UsuarioServentia;
	}

	/**
	 * Atualiza dados em "Audiencia" em virtude da movimenta��o. Quando uma audi�ncia � movimentada dever� armazenar na tabela "Audiencia" a data da
	 * movimenta��o, quando n�o for sess�o de 2� grau, pois essa ser� finalizada em um momento posterior.
	 * 
	 * @param audienciaDt
	 *            , objeto com dados da audiencia
	 * @param logDt
	 *            , dados do log
	 * @param fabricaConexao
	 *            , conex�o ativa
	 * @throws Exception 
	 */
	private void alterarAudienciaMovimentacao(AudienciaDt audienciaDt, String dataMovimentacao, LogDt logDt, FabricaConexao fabricaConexao) throws Exception{

		// Se n�o for sess�o de 2� grau seta data da movimenta��o
		if (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) != AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()) {
			// Guarda dados do objeto antes da altera��o
			String valorAtual = audienciaDt.getPropriedades();

			// Seta da movimenta��o
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
	 * Consulta os dados de uma audi�ncia e seus processos vinculados que ainda n�o foram movimentados
	 * 
	 * @param stId, identifica��o da audi�ncia
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
	 * Consulta todos os processos vinculados a uma audi�ncia, independente do status.
	 * 
	 * @param stId, identifica��o da audi�ncia
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
	 * M�todo que verifica se uma sess�o de 2� grau pode ser finalizada, devendo verificar se existe algum processo vinculado que ainda n�o foi
	 * julgado
	 * 
	 * @param id_Audiencia
	 */
	public String podeFinalizarSessao(String id_Audiencia, UsuarioDt usuarioDt) throws Exception {
		String stMensagem = "";

		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		List audienciaProcessos = audienciaProcessoNe.consultarAudienciaProcessosPendentes(id_Audiencia, usuarioDt);
		if (audienciaProcessos != null && audienciaProcessos.size() > 0) {
			stMensagem += "Sess�o n�o pode ser finalizada. Existem processos n�o julgados nesta Sess�o.";
		}
		return stMensagem;
	}

	/**
	 * M�todo respons�vel em excluir ou desativar uma sess�o de segundo grau.
	 * Se sess�o possuir algum processo vinculado, mesmo que n�o esteja pendente deve apenas desativar a sess�o.
	 * Caso n�o possua nenhum processo, deve excluir.
	 * 
	 * @param audienciaDt, objeto com dados da audi�ncia
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
				//Desativa sess�o
				String valorAtual = "[Id_Audiencia:" + audienciaDt.getId() + ";CodigoTemp:" + String.valueOf(AudienciaDt.ABERTA) + "]";
				String valorNovo = "[Id_Audiencia:" + audienciaDt.getId() + ";CodigoTemp:" + String.valueOf(AudienciaDt.DESATIVADA) + "]";

				obLogDt = new LogDt("Audiencia", audienciaDt.getId(), audienciaDt.getId_UsuarioLog(), audienciaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Invalidacao), valorAtual, valorNovo);
				obPersistencia.desativarAudiencia(audienciaDt.getId());
			} else {
				//Exclui sess�o
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
	 * Chama m�todo para consultar Respons�veis por uma AudienciaProcesso
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
	 * M�todo respons�vel em cancelar a movimenta��o de audi�ncia(s). Apaga os id's que tenham sido setados para os objetos
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
	 * Realiza chamada a Objeto que efetuar� a consulta
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
	 * Realiza chamada a Objeto que efetuar� a consulta
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
	 * Realiza chamada a Objeto que efetuar� a consulta
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
	 * Realiza chamada ao objeto que efetuar� a consulta
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
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public List consultarStatusAudiencia(String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		AudienciaProcessoStatusNe audienciaProcessoStatusNe = new AudienciaProcessoStatusNe();
		
		tempList = audienciaProcessoStatusNe.consultarAudienciaProcessoStatusMovimentacao(serventiaSubtipoCodigo);
				
		audienciaProcessoStatusNe = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
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
	 * M�todo respons�vel por criar o relat�rio de listagem das Audi�ncias no formato PDF.
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
					parametros.put("titulo", "Agenda de Audi�ncias");
					liAudiencias = obPersistencia.relListagemAudienciasParaHoje(usuarioDt, audienciaDtConsulta);

					pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "listagemAudiencias.jasper";
					break;
				case 2:
					parametros.put("titulo", "Audi�ncias Pendentes");
					liAudiencias = obPersistencia.relListagemAudienciasPendentes(usuarioDt, audienciaDtConsulta);

					pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "listagemAudienciasPendentesMovidas.jasper";
					break;
				case 3:
					parametros.put("titulo", "Audi�ncias Movimentadas Hoje");
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
	 * M�todo respons�vel por consultar a pend�ncia do tipo voto em um processo para um serventia cargo
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
	 * M�todo respons�vel por consultar a pend�ncia do tipo voto em um processo para um serventia cargo
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
	 * M�todo respons�vel por consultar as pend�ncias a gerar para uma conclus��o do tipo voto
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
	 * M�todo respons�vel por consultar �ltimo relat�rio inserido no processo por um determinado usu�rio
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
	 * M�todo respons�vel por gerar uma movimenta��o de julgamento adiado
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
						
			// Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
			ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
			audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);	
			
			// Obtem a nova sess�o...
			AudienciaDt audienciaNovaDt = obPersistencia.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), audienciaDt.isVirtual());
			
			if (audienciaNovaDt != null) {
				LogDt logDt = new LogDt(Id_UsuarioLog, IpComputadorLog);
				// Cria a nova Audi�ncia Processo
				obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, false, audienciaNovaDt, logDt, obFabricaConexao);
				// Gera a movimenta��o no Processo
				gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, Id_UsuarioServentia, false, true, false, audienciaNovaDt, logDt , obFabricaConexao);
			} else {
				retorno = "N�o foi localizada uma sess�o aberta com data posterior.";
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
	 * M�todo respons�vel em gerar as movimenta��es sess�o de julgamento adiada ou iniciada,  
	 * bem como levar esta sess�o automaticamente para a pr�xima pauta aberta em uma data diferente da data atual.
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
		//lrcampos 28/06/2019 corre��o no complemento quando a movimenta��o do adiamento for por motivo de sutenta��o oral deferida.
		String textoAdiadoComPedidoSo = ehAdiadoComPedidoSO ? "(Adiado em raz�o do Pedido de Sustenta��o Oral Deferido na sess�o de: %s - Pr�xima sess�o prevista: %s)" : "(Adiado na sess�o de: %s - Pr�xima sess�o prevista: %s)";
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
	 * Obt�m uma nova Audiencia Processo para ser usada na movimenta��o julgamento iniciado / adiado 
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
			
			// Copiar votantes da sess�o antiga para a nova.
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
				temEmenta = true; // jvosantos - 16/12/2019 15:39 - Simplificar l�gica
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

		// jvosantos - 05/12/2019 18:00 - Corre��o salvar na AUDI_PROC_PEND a nova pendencia
		audienciaProcessoPendenciaNe.salvar(idNovaPend, idAudienciaProcesso, obFabricaConexao);
		
		pendenciaNe.limparCodigoTempPendencia(idNovaPend, obFabricaConexao);
		return idNovaPend;
	}
	
	// jvosantos - 26/12/2019 15:23 - M�todo para copiar arquivos e relacionamento com pendencia para a nova pendencia
	public static void copiarArquivosEPendenciaArquivo(String idNovaPend, List<PendenciaArquivoDt> pendArqCopiar,
			ArquivoNe arquivoNe, PendenciaArquivoNe pendenciaArquivoNe, LogDt logDt,
			FabricaConexao obFabricaConexao) throws Exception {
		if(pendArqCopiar == null || pendArqCopiar.isEmpty()) return;
		for (PendenciaArquivoDt pendArq : pendArqCopiar) {
			ArquivoDt arq = arquivoNe.consultarId(pendArq.getId_Arquivo(), obFabricaConexao.getConexao());
			
			if(arq == null)
				throw new Exception("N�o foi encontrado o Arquivo de ID ("+pendArq.getId_Arquivo()+") da PEND_ARQ ("+pendArq.getId()+").");
			
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
	 * Salva movimenta��o de sess�es de 2� grau essa movimenta��o refere-se somente a um processo da sess�o. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimenta��o correspondente e pend�ncias de acordo com o que foi selecionado pelo usu�rio
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt, usu�rio que est� realizando a movimenta��o 
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
					
					// jvosantos - 02/09/2019 12:02 - Verificar se a quantidade de votantes bate com a de votos, gera uma Exception caso n�o
					if(audienciaMovimentacaoDt.getAudienciaDt().isVirtual() && audienciaMovimentacaoDt.getAudienciaDt().isSessaoIniciada()) {
						VotoNe votoNe = new VotoNe();
						
						// jvosantos - 16/10/2019 19:24 - Desativar valida��o para Serventias Especiais por causa das regras de 2/3
						if(!votoNe.isServentiaEspecial(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId_Serventia()) && !votoNe.verificarQuantidadeVotos(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), obFabricaConexao))
							throw new Exception("A quantidade de votos realizados � menor que a quantidade de votantes para essa audi�ncia!");
					}
					
					//Consultando a serventia para saber se � alguma serventia que n�o necessita MP para inserir extrato de ata.
					//Atualmente, somente o Conselho de Magistratura tem essa exce��o.
					ServentiaDt serventiaDt = new ServentiaNe().consultarId(usuarioDt.getId_Serventia());
					if(!serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA))){
						if (!audienciaMovimentacaoDt.temServentiaMp()) retorno += "A Serventia do MP Respons�vel deve ser informada. \n";
						if (!audienciaMovimentacaoDt.temMpResponsavel()) retorno += "O MP Respons�vel deve ser informado. \n";
					}
					if (audienciaMovimentacaoDt.isVotoPorMaioria()){
						if (audienciaMovimentacaoDt.getId_ServentiaCargoRedator().trim().length() == 0){
							retorno += "O Redator deve ser informado. \n";
						} 
						//else {	
							//processoResponsavelNe = new ProcessoResponsavelNe(); 
							//ServentiaCargoDt relatorProcesso = processoResponsavelNe.consultarRelatorResponsavelProcessoSegundoGrau(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), obFabricaConexao);
							
							//if (relatorProcesso != null && audienciaMovimentacaoDt.getId_ServentiaCargoRedator().trim().equalsIgnoreCase(relatorProcesso.getId())) retorno += "O Redator n�o deve ser o Relator. \n";
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
				retorno = "Tipo de usu�rio n�o mapeado para executar esta fun��o.";
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
	 * Salva movimenta��o de sess�es de 2� grau essa movimenta��o refere-se somente a um processo da sess�o. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimenta��o correspondente e pend�ncias de acordo com o que foi selecionado pelo usu�rio
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt, usu�rio que est� realizando a movimenta��o
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
        
        // Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
        ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
				
        MovimentacaoDt movimentacaoDt = null;
        AudienciaProcessoDt audienciaProcessoNovaDt = null;
        String idAudienciaProcesso = audienciaDt.getAudienciaProcessoDt().getId();
        // Chama m�todo para gerar a movimenta��o de in�cio ou adiamento de sess�o...			
		if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
			AudienciaDt audienciaNovaDt = null;
			if (!audienciaMovimentacaoDt.isAlteracaoExtratoAta()) 
			{
				//Obtem a nova sess�o...
				AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
				audienciaNovaDt = obPersistencia.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), false);
				// Verifica se existe uma Pr�xima Sess�o Aberta.
				if (audienciaNovaDt == null){
					retornoMensagemInconsistencia = "N�o foi localizada uma sess�o aberta com data posterior.";
					return retornoMensagemInconsistencia;
				}			
				// Cria a nova Audi�ncia Processo
				audienciaProcessoNovaDt = obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaNovaDt, logDt, obFabricaConexao);
				idAudienciaProcesso = audienciaProcessoNovaDt.getId();
			} else {
				audienciaNovaDt = audienciaDt;
				audienciaProcessoNovaDt = audienciaDt.getAudienciaProcessoDt();
			}
			// Gera a movimenta��o no Processo			
			movimentacaoDt = gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), audienciaNovaDt, logDt, obFabricaConexao);				
		}							
					
		arquivos = audienciaMovimentacaoDt.getListaArquivos();
		String Id_ArquivoAta = "";
		// Salva arquivos inseridos
		if (arquivos != null && arquivos.size() > 0) {
			movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
			ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
			// Salvando v�nculos do arquivo de Ata com a Audi�ncia Processo
			Id_ArquivoAta = arquivoDt.getId();										
		}		
		
		//Vincula os arquivos, dependendo do tipo da movimenta��o
		if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
			//Vincula o arquivo inserido � Audi�ncia Processo
			audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoNovaDt, "", "", "", Id_ArquivoAta, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada(), "", "", "",logDt, obFabricaConexao);
		} else {
			//Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia e inserir respons�veis pela audi�ncia
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
		
		// Salvando v�nculo entre movimenta��o e arquivos inseridos
		movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);			

		// Salvando pend�ncias da movimenta��o
		if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) 
			pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);			

		// Atualiza Classificador processo
		if (audienciaMovimentacaoDt.getId_Classificador().length() > 0)
			processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);			

//		// Gera recibo para arquivos de movimenta��es
//		movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
					
		// Cria publica��o
		// Decreto 1.684 / 2020, Art 2o e 3o
		if (!processoDt.isSigiloso()) {
			pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
		}
		
		 // Se for voto vencido iremos gerar uma conclus�o do tipo voto/ementa para o desembargador redator poder pr�-analisar
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
	        	
	        	
	        	// Alterar pend�ncia de voto para o desembargador inserir o voto vencido
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
        	
        	// Finalizar a pend�ncia de ementa do desembargador com voto vencido
        	finalizarPendencia(audienciaDt.getAudienciaProcessoDt().getId_PendenciaEmentaRelator(), 
        			           pendenciaNe, 
        			           usuarioDt, 
        			           obFabricaConexao);
        	
        	// Gerar pend�ncia de voto para o desembargador inserir o voto vencedor
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
        		// Gerar pend�ncia de voto para o desembargador inserir o voto, caso n�o exista...
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
	 * Salva movimenta��o de sess�es de 2� grau essa movimenta��o refere-se somente a um processo da sess�o. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimenta��o correspondente e pend�ncias de acordo com o que foi selecionado pelo usu�rio
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt, usu�rio que est� realizando a movimenta��o
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
	
	// jvosantos - 08/07/2019 15:37 - M�todo que salva as movimenta��es de ressalvas
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
			
			// jvosantos - 21/11/2019 15:55 - Alterar o USU_REALIZADOR da movimenta��o para o Votante
			MovimentacaoDt movimentacaoRessalva = movimentacaoNe.gerarMovimentacao(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), MovimentacaoTipoDt.DECISAO, serventiaCargoDt.getId_UsuarioServentia(), "Declara��o de Voto " + v.getNomeVotante(), new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), obFabricaConexao);
			List<ArquivoDt> arquivosRessalva = pendenciaArquivoNe.consultarArquivosPendenciaFinalizada(v.getIdPendencia());
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivosRessalva, movimentacaoRessalva.getId(), processoDt.isSegredoJustica() ? String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL) : null , logDt, obFabricaConexao);
			
			movimentacaoArquivoNe.inserirArquivos(arquivosRessalva, logDt, obFabricaConexao);
			
//			// jvosantos - 15/07/2019 11:48 - Gerar Recibo na movimenta��o de ressalva
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
	 * Salva movimenta��o de sess�es de 2� grau essa movimenta��o refere-se somente a um processo da sess�o. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimenta��o correspondente e pend�ncias de acordo com o que foi selecionado pelo usu�rio
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt, usu�rio que est� realizando a movimenta��o
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
		
		// Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
		ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
		
		boolean isRelator = audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase(usuarioDt.getId_ServentiaCargo());
		
		//Consulta Voto 2� Grau
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
		
		//Consulta Ementa 2� Grau
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
						// Retira da nomenclatura do arquivo a descri��o correspondente � ementa...
						arquivoDtEmenta.setNomeArquivo(arquivoDt.getNomeArquivo().substring(prefixoArquivoEmenta.length()));
					}
				}
			}			
			movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
		}

		// Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia
		// e inserir respons�veis pela audi�ncia
		MovimentacaoDt movimentacaoDt = movimentarAudiencia(audienciaDt, audienciaMovimentacaoDt.getAudienciaStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), usuarioDt, arquivos, logDt, obFabricaConexao);
		movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
		movimentacoes.add(movimentacaoDt);
		
		//Fechar pendencia do Tipo Voto 2� Grau
		if (pendenciaDtVoto != null){
			pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String data = formatter.format(LocalDateTime.now());
			pendenciaDtVoto.setDataFim(data);
			pendenciaDtVoto.setDataVisto(data);
			pendenciaNe.fecharPendencia(pendenciaDtVoto, usuarioDt, obFabricaConexao);
		}
		
		//Fechar pendencia do Tipo Ementa 2� Grau
		if (pendenciaDtEmenta != null){
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String data = formatter.format(LocalDateTime.now());
			pendenciaDtEmenta.setDataFim(data);
			pendenciaDtEmenta.setDataVisto(data);
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);
		}

		// Salvando v�nculo entre movimenta��o e arquivos inseridos
		if (arquivos != null && arquivos.size() > 0){
			String visibilidade=null;
			if (processoDt.isSegredoJustica()){
				visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
			}
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(),visibilidade, logDt, obFabricaConexao);
			
			if (arquivoDtEmenta != null) {
			
				// Iremos incluir o v�nculo entre os arquivos de ac�rd�o com o arquivo de ementa...
				for (int i=0; i < arquivos.size(); i++){
					ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(i);				
					if (arquivoDt != null && !arquivoDtEmenta.getId().trim().equalsIgnoreCase(arquivoDt.getId().trim())){
						AudienciaPs obPersistencia = new AudienciaPs(obFabricaConexao.getConexao());
						obPersistencia.vincularEmentaAcordaoSegundoGrau(audienciaDt.getAudienciaProcessoDt().getId(), arquivoDtEmenta.getId(), arquivoDt.getId());
					}
				}
				
			}					
		}

		// Salvando pend�ncias da movimenta��o
		if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null && audienciaMovimentacaoDt.getListaPendenciasGerar().size() > 0) {
			pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);
		}

		// Atualiza Classificador processo
		if (audienciaMovimentacaoDt.getId_Classificador().length() > 0) {
			processoNe.alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);
		}

//		// Gera recibo para arquivos de movimenta��es
//		movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
		
		// Cria publica��o
		// Decreto 1.684 / 2020, Art 2o e 3o
		if (!processoDt.isSigiloso()) {
			pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
		}
        
        // Registra no processo o indicador de julgamento do m�rito do processo principal 
        if (audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal() != null && audienciaMovimentacaoDt.getJulgadoMeritoProcessoPrincipal().equalsIgnoreCase("true")){
        	processoNe.registrarJulgamentoMeritoProcessoPrincipal(processoDt.getId(), true, obFabricaConexao);
		}
        
        // Se for voto do redator iremos gerar uma conclus�o do tipo voto para o desembargador relator inserir o voto vencido
        if (!isRelator){
        	
        	// Finalizar a pend�ncia de ementa do desembargador com voto vencido
        	finalizarPendenciaEmentaRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), 
        			                              pendenciaNe, 
        			                              usuarioDt, 
        			                              audienciaDt.getAudienciaProcessoDt().getId(), 
        			                              obFabricaConexao);
        	
        	// Alterar pend�ncia de voto para o desembargador inserir o voto vencido
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
		//Consulta Voto 2� Grau
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
		// Configura outras informa��es
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
			// Configura o distribuidor do gabinete como respons�vel
			responsavel = new PendenciaResponsavelDt();
			if (serventiaCargoAssistente != null) {
				responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());
			} else {
				ServentiaCargoDt serventiaCargoDistribuidor = serventiaCargoNe.getDistribuidorGabinete(serventiaCargoResponsavel.getId_Serventia(), obFabricaConexao);
				if(serventiaCargoDistribuidor == null) throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete da serventia " + serventiaCargoResponsavel.getId_Serventia() + ".");
				responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());
			}
			pendenciaDtVoto.addResponsavel(responsavel);
		} else {
			// Configura o desembargador como respons�vel
			responsavel = new PendenciaResponsavelDt();
			responsavel.setId_ServentiaCargo(id_ServentiaCargo);
			// Adiciona o desembargador
			pendenciaDtVoto.addResponsavel(responsavel);
			
			responsavel = new PendenciaResponsavelDt();
			// Tenta obter um assistente relacionado ao processo, caso o assistente n�o tenha sido informado...
			if (serventiaCargoAssistente == null)
				serventiaCargoAssistente = processoResponsavelNe.getAssistenteGabineteResponsavelProcesso(id_Processo, serventiaCargoResponsavel.getId_Serventia(), obFabricaConexao);
			
		    if (serventiaCargoAssistente != null) 
		    	responsavel.setId_ServentiaCargo(serventiaCargoAssistente.getId());	
		    else {
		    	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
		    	ServentiaCargoDt serventiaCargoDistribuidor = serventiaCargoNe.getDistribuidorGabinete(serventiaCargoResponsavel.getId_Serventia(), obFabricaConexao);
		    	if(serventiaCargoDistribuidor == null) throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete da serventia " + serventiaCargoResponsavel.getId_Serventia() + ".");
		    	responsavel.setId_ServentiaCargo(serventiaCargoDistribuidor.getId());	
		    }
		    pendenciaDtVoto.addResponsavel(responsavel); 
		}   	
		
		pendenciaNe.gerarPendencia(pendenciaDtVoto, pendenciaDtVoto.getResponsaveis(), null, pendenciaDtVoto.getListaArquivos(), false, false, obFabricaConexao);
		return pendenciaDtVoto;
	}
	
	
	
	
	/**
	 * Salva movimenta��o de sess�es de 2� grau essa movimenta��o refere-se somente a um processo da sess�o. Faz chamadas para atualizar dados em "Audiencia",
	 * "AudienciaProcesso", salva arquivos inseridos, gera movimenta��o correspondente e pend�ncias de acordo com o que foi selecionado pelo usu�rio
	 * 
	 * @param audienciaMovimentacaoDt, objeto com dados da movimenta��o de audi�ncia a serem persistidos
	 * @param usuarioDt, usu�rio que est� realizando a movimenta��o
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
		// � relator...
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
			throw new MensagemException("Fluxo n�o mapeado para o perfil do usu�rio logado.");
		}
		
		//Consulta Voto 2� Grau
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
		
		//Se n�o existir uma pend�ncia do tipo Voto 2� Grau, iremos cri�-la, e consult�-la
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
		
		//Obtem a conclus�o do tipo Voto
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
		
		//Salva a conclus�o do tipo Voto
		pendenciaArquivoNe.salvarPreAnaliseConclusao(analiseConclusaoDtVoto, usuarioDt, obFabricaConexao);		
				
		//Consulta Ementa 2� Grau
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
		
		//Se n�o existir uma pend�ncia do tipo Ementa 2� Grau, iremos cri�-la
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
		
		//Obtem a conclus�o do tipo Ementa
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
				
		//Salva a conclus�o do tipo Ementa
		pendenciaArquivoNe.salvarPreAnaliseConclusao(analiseConclusaoDtEmenta, usuarioDt, obFabricaConexao);
		
		//Atualiza as pend�ncias na sess�o, caso tenha gerado uma pend�ncia filha...
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
	 * Executa o M�todo consultar id da classe ArquivoNe
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
	 * Executa o M�todo baixar arquivo da classe ArquivoNe, inicialmente ser� utilizado apenas para baixar o arquivo de Ata pelo desembargador ou pelo seu assistente
	 * 
	 * @param idArquivo, objeto audienciaMovimentacao	  
	 * @param response, objeto response do request
	 * @param idUsuario, identificador do usu�rio logado
	 * @param ipComputadorLog, ip do computador do usu�rio logado
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
	 * M�todo respons�vel por consultar a pend�ncia do tipo ementa em um processo para um serventia cargo
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
	 * M�todo respons�vel por consultar a pend�ncia do tipo ementa em um processo para um serventia cargo
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
	 * M�todo respons�vel em efetuar a��es comuns em uma movimenta��o de audi�ncia, valendo tamb�m para sess�es de 2� grau. Chama m�todo para
	 * Atualizar Dados em "Audiencia", "AudienciaProcesso", gera movimenta��o correspondente a movimenta��o da audi�ncia e insere respons�veis pela
	 * audi�ncia
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
		
		// Dependendo do tipo da Audi�ncia chama m�todo para gerar Movimenta��o correspondente
		movimentacaoAudiencia = this.movimentarSessao(audienciaDt, codigoStatus, id_NovaAudiencia, novaData, usuarioDt, true, houveAlteracaoExtratoAta, logDt, conexao);
		
		return movimentacaoAudiencia;
	}
	
	/***
	 * M�todo respons�vel por consultar as Sess�es de 2� grau
     * pendentes por Relator somente as que est�o pendentes de inser��o de ac�rd�o.
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
	 * M�todo respons�vel por consultar as Sess�es de 2� grau
     * pendentes por Relator somente as que est�o pendentes de inser��o de ac�rd�o.
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
	 * M�todo respons�vel por consultar a quantidade de Sess�es de 2� grau
     * pendentes por Relator somente as que est�o pendentes de inser��o de ac�rd�o.
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
	 * M�todo respons�vel por consultar a quantidade de Sess�es de 2� grau
     * pendentes por Relator somente as que est�o pendentes de inser��o de ac�rd�o.
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
	 * Consulta um arquivo tipo Extrato de Ata de sess�o.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoExtratoAtaSessao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.EXTRATO_ATA_SESSAO);      
				
	}
	
	/**
	 * Consulta um arquivo tipo Voto de sess�o.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoVotoSessao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.RELATORIO_VOTO);      
				
	}
	
	/**
	 * Consulta um arquivo tipo Ementa de sess�o.
	 * 
	 * @return
	 * @throws Exception
	 * @author mmgomes 
	 */
	public ArquivoTipoDt consultarArquivoTipoEmentaSessao() throws Exception{				
		
		return consultarArquivoTipoCodigo(ArquivoTipoDt.EMENTA);      
		
	}
	
	/**
	 * Consulta um arquivo tipo Extrato de Ata de sess�o.
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
	 * M�dodo respons�vel em agendar uma audi�ncia do tipo Sess�o do 2� grau para um processo passado. Chama m�todo para vincular o processo a
	 * audi�ncia e gera a movimenta��o de inclus�o em pauta
	 * 
	 * @param processoDt
	 *            , processo a ser vinculado com a sess�o
	 * @param id_Sessao
	 *            , audi�ncia a ser vinculada com processo
	 * @param dataAgendada
	 *            , data da nova audi�ncia a ser marcada
	 * @param id_ProcessoTipo
	 *            , id da classe (processoTipo) da nova audi�ncia a ser marcada
	 * @param processoTipo
	 *            , processo tipo da nova audi�ncia a ser marcada
	 * @param id_UsuarioServentia
	 *            , usu�rio que est� marcando a sess�o
	 * @param logDt
	 *            , objeto com dados do log
	 * @param conexao
	 *            , conex�o ativa
	 * 
	 * @author mmgomes
	 */
	public AudienciaProcessoDt marcarSessaoEmMesaParaJulgamento(ProcessoDt processoDt, String id_Sessao, String dataAgendada, String id_ProcessoTipo, String processoTipo, String id_UsuarioServentia, LogDt logDt, FabricaConexao conexao, UsuarioDt usuarioDt, String id_ServentiaCargoResponsavel) throws Exception {
		
		// Chama metodo para vincular o processo a audiencia
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		return audienciaProcessoNe.marcarSessaoProcesso(id_Sessao, dataAgendada, id_ProcessoTipo, processoTipo, processoDt, id_UsuarioServentia, logDt, conexao, true, usuarioDt, id_ServentiaCargoResponsavel);

	}
	
	/**
	 * M�todo respons�vel em realizar a consulta de uma audiencia processo status atrav�s do tipo da serventia e do c�digo.
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
	 * M�todo respons�vel e consultar a pr�-an�lise de uma conclus�o
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
	 * Consulta os dados de uma audi�ncia processo que ainda n�o foi movimentado
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param usuarioDt, usu�rio logado
	 * 
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarAudienciaProcessoPendente(String id_Processo, UsuarioDt usuarioDt) throws Exception {
		AudienciaProcessoDt audienciaprocessoDt = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		audienciaprocessoDt = audienciaProcessoNe.consultarAudienciaProcessoPendente(id_Processo, usuarioDt);			

		// alsqueiroz 01/10/2019 * Instru��o desnecess�ria removida
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
	 * Retorna as serventias com base na descri��o, p�gina atual, Tipo da Serventia e SubTipo da Serventia
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
	 * Consulta o presid�nte da C�mara
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
	 * Gera o arquivo ODT contendo a listagem dos processos presentes na sess�o, pauta do dia
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
	 * Gera o arquivo PDF contendo a listagem dos processos presentes na sess�o, adiados
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
	 * Gera o arquivo PDF contendo a listagem dos processos presentes na sess�o, adiados
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
			String dataJulgamento = "DATA DO JULGAMENTO " + FormatoData.format(dataHoraSessao) + " AS " + FormatoHora.format(dataHoraSessao) + " HORAS OU NAS SESS�ES POSTERIORES";
			
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
				out = GerarPDF.gerarMensagemPDF("Extrato da Ata da sess�o do processo " + relatorio.getProcessoNumero(), "");
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
	 * Gera o arquivo PDF contendo a listagem dos processos presentes na sess�o, Em Mesa Para Julgamento
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
			String dataJulgamento = "DATA DO JULGAMENTO " + FormatoData.format(dataHoraSessao) + " AS " + FormatoHora.format(dataHoraSessao) + " HORAS OU NAS SESS�ES POSTERIORES";		

			// PAR�METROS DO RELAT�RIO
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
					processoTipo = audienciaProcessoDt.getProcessoTipo() + " (Recurso Principal/M�rito)";	
				}
			} else {
				processoTipo = audienciaProcessoDt.getProcessoTipo();	
			}						
			descricaoPoloAtivo = audienciaProcessoDt.getDescricaoPoloAtivo();
			descricaoPoloPassivo = audienciaProcessoDt.getDescricaoPoloPassivo();
		}
		
		String complementoSegredoJustica = "";
		if (!(audienciaProcessoDt.getProcessoDt().getSegredoJustica() == null || audienciaProcessoDt.getProcessoDt().getSegredoJustica().equalsIgnoreCase("false"))) complementoSegredoJustica = " - SEGREDO DE JUSTI�A";				
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
			// Consulta se h� substitui��o de gabinete para o relator. Caso haja, apresenta o nome do substituto com a devida confirma��o de substitui��o 
			ServentiaCargoDt responsavelProcesso = relator;			
			if (responsavelProcesso==null) {
				 throw new MensagemException("N�o foi poss�vel encontar o relator"); 
			}
			ServentiaDt servProc = serventiaNe.consultarId(audienciaProcessoDt.getProcessoDt().getId_Serventia());
			ServentiaCargoDt serventiaCargoSubstituto = null;
			// Consulta gabinete substituto (se existir)
			ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
			ServentiaDt serventiaSubstitutaDt = serventiaRelacionadaNe.consultarGabineteSubstitutoAtualSegundoGrau(audienciaCompleta.getId_Serventia(), responsavelProcesso.getId_Serventia()); 			
			// testa para verificar se possui gabinete substituto, caso exista consulta o cargo de desembargador ativo (com quantidade de distribui��o) deste gabinete 
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
			audiProc.setRelator("N�o h� - Verifique distribui��o".toUpperCase());
		}					
		
		// Promoventes				
		audiProc.setPromoventes(obtenhaListaRelatorioAudienciaProcessoParteAdvogado(audienciaProcessoDt.getProcessoDt().getListaPolosAtivos()));
		audiProc.setListaPromoventesSemRecurso(audiProc.getPromoventes());
		
		// Promovidos					
		audiProc.setPromovidos(obtenhaListaRelatorioAudienciaProcessoParteAdvogado(audienciaProcessoDt.getProcessoDt().getListaPolosPassivos()));
		audiProc.setListaPromovidosSemRecurso(audiProc.getPromovidos());
		
		// jvosantos - 28/06/2019 17:53 - Adicionar bloco de c�digo para buscar o recurso (n�o secundario) j� que a busca usando "audienciaProcessoDt.getProcessoDt().getRecursoDt()" n�o funciona
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
		else audiProc.setProcuradorJustica("N�o h�".toUpperCase());
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
		// Se existir, iremos finaliz�-la
		if (pendenciaDt != null) {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);	
		}
	}
	
	/**
	 * Finaliza uma pend�ncia do tipo Relatorio/Voto do relator da sess�o
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
		//Consulta Voto 2� Grau
		PendenciaDt pendenciaDtVoto = pendenciaNe.consultarPendenciaVotoRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finaliz�-la
		if (pendenciaDtVoto != null) {
			pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtVoto, usuarioDt, obFabricaConexao);	
		}
	}

	/**
	 * Finaliza uma pend�ncia do tipo Ementa do relator da sess�o
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
		//Consulta Ementa 2� Grau
		PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarPendenciaEmentaRelatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finaliz�-la
		if (pendenciaDtEmenta != null) {
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);	
		}
	}
	
	
	/**
	 * Finaliza uma pend�ncia do tipo Relatorio/Voto do redator da sess�o
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
		//Consulta Voto 2� Grau
		PendenciaDt pendenciaDtVoto = pendenciaNe.consultarPendenciaVotoRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finaliz�-la
		if (pendenciaDtVoto != null) {
			pendenciaDtVoto.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtVoto, usuarioDt, obFabricaConexao);	
		}
	}

	/**
	 * Finaliza uma pend�ncia do tipo Ementa do redator da sess�o
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
		//Consulta Ementa 2� Grau
		PendenciaDt pendenciaDtEmenta = pendenciaNe.consultarPendenciaEmentaRedatorSessao(idAudienciaProcessoSessaoSegundoGrau, id_ServentiaCargo, obFabricaConexao);
				
		// Se existir, iremos finaliz�-la
		if (pendenciaDtEmenta != null) {
			pendenciaDtEmenta.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			
			pendenciaNe.fecharPendencia(pendenciaDtEmenta, usuarioDt, obFabricaConexao);	
		}
	}
	
	
	/**
	 * Consulta todos os extratos da ata de julgamento de todos os processos da sess�o para montar o texto do editor de finaliza��o da sess�o
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
  	 * M�todo que conta quantos processos est�o vinculados a uma sess�o.
  	 * @param idAudiencia - id da sess�o
  	 * @return n�mero de processos
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
	 * M�todo respons�vel por consultar a pend�ncia elabora��o de Voto
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
     *            identifica��o da serventia principal
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
		/* use o iu do objeto para saber se os dados ja est�o ou n�o salvos */
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
  	 * @param idAudiencia - id da sess�o
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
	 * M�todo que verifica se os dados obrigat�rios em uma movimenta��o de audi�ncia foram preenchidos
	 * 
	 * @param dados
	 *            , objeto com dados da movimenta��o a ser verificada
	 * @author gschiquini
	 */
	public String verificarMovimentacaoAudienciaProcessoPJD(AudienciaMovimentacaoDt dados, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";
		if (dados.getAudienciaStatusCodigo().length() > 0 && dados.getAudienciaStatusCodigo().equals("-1")) stRetorno += "Selecione o Status da Audi�ncia. \n";
		// Se for uma Sess�o de 2� grau que est� sendo remarcada, deve ser selecionada a data da nova sess�o
		else if (Funcoes.StringToInt(dados.getAudienciaStatusCodigo()) == AudienciaProcessoStatusDt.REMARCADA && Funcoes.StringToInt(dados.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && dados.getId_NovaSessao().length() == 0) {
			stRetorno += "Selecione a Data da nova Sess�o a ser marcada. \n";
		}
		int grupoTipoUsuarioLogado = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		if (dados.isPreAnalise()){
			if (dados.getId_ArquivoTipo() == null || dados.getId_ArquivoTipo().trim().length() == 0 || dados.getArquivoTipo() == null || dados.getArquivoTipo().trim().length() == 0 ){
				stRetorno += "Selecione o Tipo do Arquivo: Ac�rd�o, Relat�rio e Voto. \n";
			}				
			/*if(dados.getTextoEditor() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditor())){
				stRetorno += "� necess�rio redigir o texto da pr�-an�lise: Ac�rd�o, Relat�rio e Voto. \n";
			}*/
			if (dados.getId_ArquivoTipoEmenta() == null || dados.getId_ArquivoTipoEmenta().trim().length() == 0 || dados.getArquivoTipoEmenta() == null || dados.getArquivoTipoEmenta().trim().length() == 0 ){
				stRetorno += "Selecione o Tipo do Arquivo: Ementa. \n";
			}				
			if(dados.getTextoEditorEmenta() == null || !Funcoes.possuiTextoInformadoEditorHTML(dados.getTextoEditorEmenta())){
				stRetorno += "� necess�rio redigir o texto da pr�-an�lise: Ementa. \n";
			}			

		} else {
				if (dados.getListaArquivos() == null || dados.getListaArquivos().size() == 0) {
					stRetorno += "� necess�rio inserir um arquivo. \n";
				} else if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU && dados.getListaArquivos().size() > 1){
					if(!(usuarioDt.getServentiaSubtipoCodigo() != null &&
							(Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL
						     || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL
							 || Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL)))
						stRetorno = "� necess�rio inserir apenas um arquivo correspondente � Ata. \n";			
				} else if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
					int quantidadeEmenta = 0;
					ArquivoDt arquivoDt = null;
					for (int i=0; i < dados.getListaArquivos().size(); i++){
						arquivoDt = (ArquivoDt) dados.getListaArquivos().get(i);
						if(arquivoDt != null && arquivoDt.getArquivoTipo().equals(dados.getArquivoTipoEmenta())){
							quantidadeEmenta += 1;
						}
					}		
					if (quantidadeEmenta == 0) stRetorno = "� necess�rio inserir um texto correspondente � Ementa no Editor. \n";
					else if (quantidadeEmenta > 1) stRetorno = "� permitido inserir apenas um arquivo correspondente � Ementa. \n";					
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
	
	// jvosantos - 04/06/2019 09:50 - M�todo responsavel por descar a pre analise dos apreciados
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
					throw new MensagemException("Audi�ncia n�o encontrada, entre em contato com o suporte!");
				}
				
				ProcessoNe processoNe = new ProcessoNe();
				ProcessoDt processoDt = processoNe.consultarIdCompleto(audienciaProcessoDt.getId_Processo(), obFabricaConexao);
				
				if (processoDt == null) {
					throw new MensagemException("Processo n�o encontrado, entre em contato com o suporte!");
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
					throw new MensagemException("Audi�ncia n�o encontrada, entre em contato com o suporte!");
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
	
			// Consulta dados completos do processo com as partes para permitir por exemplo Intima��o de Partes e Testemunhas
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
	
			//Seta status de audi�ncia poss�veis
			audienciaMovimentacaoDt.setListaAudienciaProcessoStatus(this.consultarStatusAudiencia(processoDt.getServentiaSubTipoCodigo()));
			//audienciaMovimentacaoDt.setListaAudienciaProcessoStatus(this.consultarStatusAudiencia(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
			
			// Seta tipos de pend�ncias que poder�o ser geradas
			audienciaMovimentacaoDt.setListaPendenciaTipos(this.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
			
			//seta o voto se existir o mesmo para o processo (Tratamento para o segundo Grau)					
			if (UsuarioSessao.getUsuarioDt().getId_ServentiaCargo() != null && !UsuarioSessao.getUsuarioDt().getId_ServentiaCargo().equals("")){						
				MovimentacaoArquivoDt movimentacaoArquivoDt = this.consultarRelatorioProcesso(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), UsuarioSessao);
				//busca o relat�rio se existir o mesmo para o processo (Tratamento para o segundo Grau)
				if (movimentacaoArquivoDt != null && movimentacaoArquivoDt.getId() != null && !movimentacaoArquivoDt.getId().equals("")){
					audienciaMovimentacaoDt.setId_Relatorio(movimentacaoArquivoDt.getId());
					audienciaMovimentacaoDt.setHashRelatorio(movimentacaoArquivoDt.getHash());
				}
			}
			
			//Armazena o redator da sess�o, caso exista
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
				
				// se for desembargador ou assistente devemos selecinar o Status da Audi�ncia escolhido pelo analista
				if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE  || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO  || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){
					
					String Id_ServentiaCargo = UsuarioSessao.getUsuarioDt().getId_ServentiaCargo();
					
					if ((UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)) Id_ServentiaCargo = UsuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
					
					audienciaMovimentacaoDt.setPendenciaArquivoDt(this.consultarVotoDesembargador(Id_ServentiaCargo, audienciaDt.getAudienciaProcessoDt(), audienciaDt.getAudienciaProcessoDt().getId_ProcessoTipo()));
					
					// seta atributos necess�rios Voto (Tratamento para o segundo Grau)
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
						
						//lista de pend�ncias a gerar de um determinado voto
						audienciaMovimentacaoDt.setListaPendenciasGerar(this.consultarPendenciasVotoEmenta(audienciaMovimentacaoDt.getPendenciaArquivoDt()));
						
						// Obtendo o Classificador escolhido durante a pr�-an�lise do Voto								
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
												
					// seta atributos necess�rios Ementa (Tratamento para o segundo Grau)
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
					
					// Seta na sess�o a lista de pend�ncias do tipo voto e ementa...
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
						
						// Obtem o desembargador presidente da c�mara, se o atributo da sess�o n�o estiver preenchido
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
						
						//Obtem o representante do minist�rio p�blico vinculado � c�mara, se o atributo da sess�o n�o estiver preenchido
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
	 * Retirar o extrato da ata de julgamento de uma sess�o do segundo grau.
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
					throw new MensagemException("Para retornar o processo para a Sess�o de Julgamento � necess�rio inserir uma certid�o justificando o motivo.");
				} else if (statusOriginal != AudienciaProcessoStatusDt.A_SER_REALIZADA) {
					throw new MensagemException("Para a retirada do Ac�rd�o/Ementa e Extrato da Ata de Julgamento � necess�rio inserir uma certid�o justificando o motivo.");	
				} else {
					throw new MensagemException("Para a retirada do Extrato da Ata de Julgamento � necess�rio inserir uma certid�o justificando o motivo.");	
				}
			}				
						
			if (movimentacaoProcessodt.getListaProcessos() == null || 
				movimentacaoProcessodt.getListaProcessos().size() == 0 ||
				movimentacaoProcessodt.getListaProcessos().get(0) == null)
				throw new MensagemException("Processo n�o localizado na movimenta��o.");
			
			ProcessoDt processoMovimentacao = (ProcessoDt) movimentacaoProcessodt.getListaProcessos().get(0);
			
			audienciaDtCompleta = obPersistencia.consultarAudienciaProcessoCompleta(movimentacaoProcessodt.getIdRedirecionaOutraServentia());
			if (audienciaDtCompleta == null)
				throw new MensagemException("Sess�o n�o localizada, favor repetir a opera��o.");	
						
			AudienciaProcessoDt audienciaProcessoDt = audienciaDtCompleta.getAudienciaProcessoDt();			
			if (audienciaProcessoDt == null || audienciaProcessoDt.getProcessoDt() == null)
				throw new MensagemException("Processo n�o localizado na sess�o, favor repetir a opera��o.");
			
			if (!processoMovimentacao.getId().equalsIgnoreCase(audienciaProcessoDt.getProcessoDt().getId()))
				throw new MensagemException("Processo da movimenta��o n�o � o mesmo da sess�o, verifique se existem mais de uma aba aberta e repita a opera��o.");
			
			if (!(Funcoes.StringToInt(audienciaDtCompleta.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && 
			     (usuarioDt.isSegundoGrau() || usuarioDt.isGabineteSegundoGrau() || usuarioDt.isGabineteUPJ()) &&
				 Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU))
				throw new MensagemException("A��o permitida somente para o perfil Analistas Judici�rios 2� Grau em sess�es de segundo grau.");
			
			try {
				obFabricaConexao.iniciarTransacao();
				
				// Ajusta audi�ncia movimenta��o e pend�ncias, e gera movimenta��o...
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
	 * M�todo respons�vel por excluir a(s) audi�ncia(s) livre(s) [agenda(s) livre(s)] N�o utilizadas
	 * 
	 * @author lsbernardes
	 * @throws Exception
	 */
	public void excluirPautasAudienciasNaoUtilizadas() throws Exception {
	    FabricaConexao obFabricaConexao = null;
		try{
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			/*
			 *   EXCLUI REGISTROS DA TABELA AUDI_PROC & EXCLUI REGISTROS DA TABELA AUDI 
			 */
			AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
			
			obPersistencia.excluirAudienciasProcessosNaoUtilizadas();
			obPersistencia.excluirPautasAudienciasNaoUtilizadas();
			
			// FINALIZAR TRANSA��O
			obFabricaConexao.finalizarTransacao();
			
		} catch(Exception e) {
			// CANCELAR TRANSA��O
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEX�O
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
	 * M�todo utilizado na redistribui��o de processo do distribuidor c�vel para alterar a serventia de uma audiencia.
	 * Obrigatoriamente deve ser passada uma f�brica de conex�o.
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
			throw new MensagemException("Conex�o inv�lida.");
		}
		if(Funcoes.StringToLong(idProc) == 0 || Funcoes.StringToLong(idServentiaNova) == 0) {
			throw new MensagemException("Par�metros inv�lidos.");
		}
		
		AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
		
		if(Funcoes.StringToLong(idServCargoNovo) == 0){
			throw new MensagemException("N�o foi poss�vel destacar um novo respons�vel pela audi�ncia relacionada ao processo.");
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
			
			// ESTABELECER CONEX�O COM O BANCO DE DADOS
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			
			PendenciaNe pendNe = new PendenciaNe();
			ArquivoNe arquivoNe = new ArquivoNe();
			
	        PendenciaPs obPersistencia = new  PendenciaPs(obFabricaConexao.getConexao());
			
			List<PendenciaDt> pendencias = obPersistencia.consultarPendenciasProcessoPorTipo(processoDt.getId(), PendenciaTipoDt.ELABORACAO_VOTO);
			
			// O SISTEMA S� PERMITE A CRIA��O DE UMA ELABORA��O DE VOTO POR PROCESSO, POR ISSO PODE-SE PEGAR A PRIMEIRA PEND�NCIA DA LISTA.
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
			
			
			// FINALIZAR TRANSA��O
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSA��O
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEX�O
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
				// Atualiza o Historico para pend�ncia Filha
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
				// Atualiza o Historico para pend�ncia Filha
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
	
	// jvosantos - 10/01/2020 12:29 - Extrair c�digo repetido
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
				//lrcampos 19/12/2019 15:54 - Refatorando o c�digo duplicado, e adicionando condi��o para salvar Presidente e Ministerio Publico.
				List<VotanteDt> integrantes = votoNe.consultarTodosVotantesSessaoCompletoDeVerdade(audienciaProcessoDt.getId(), obFabricaConexao);
				boolean existePresidenteSessao = existeVotante(integrantes, idServCargoPresidenteSessao, VotanteTipoDt.PRESIDENTE_SESSAO);
				boolean existeMinisterioPublico = existeVotante(integrantes, idServCargoMp, VotanteTipoDt.MINISTERIO_PUBLICO);
				
				if (!existePresidenteSessao){
					adicionaVotante(idServCargoPresidenteSessao, obPersistencia, votanteTipoNe,
							impedimentoTipoNe, audienciaProcessoDt, idVotanteTipoPresidente);
				}			
				if (!existeMinisterioPublico){
					// jvosantos - 10/01/2020 12:23 - Corre��o para adicionar o MP e n�o o Presidente
					adicionaVotante(idServCargoMp, obPersistencia, votanteTipoNe,
							impedimentoTipoNe, audienciaProcessoDt, idVotanteTipoMinisterioPublico);
				}			
			}				
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			// CANCELAR TRANSA��O
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			// FECHER CONEX�O
			obFabricaConexao.fecharConexao();
		}
		
	}
	/** 
	 * M�todo para adicionar um votante e automaticamente o torna n�o impedido.
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
					
			// Cria a nova Audi�ncia Processo
			obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), novaAudiencia, logDt, obFabricaConexao);
			// Gera a movimenta��o no Processo
			gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), true, novaAudiencia, logDt, obFabricaConexao);				

			VotoNe votoNe = new VotoNe();
			//mrbatista - 11/10/2019 14:06 - Finalizando as pendencias de SO PELO ID_AUDI_PROC.
			//mrbatista - 26/09/2019 12:01 - Finalizando as pendencias de SO PELO ID_PROC.
			votoNe.finalizarPendenciasSO(audienciaDt.getAudienciaProcessoDt().getId(), usuarioDt, obFabricaConexao);

			//mrbatista - 11/10/2019 14:04 - Finalizar todas as pendencias da vota��o
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
			// altera��o dos tipos para recurso secund�rio
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
	
	// jvosantos - 05/07/2019 15:29 - Adicionar par�metro para corrigir o erro de n�o trazer alguns processo
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

	// jvosantos - 04/06/2019 09:50 - M�todo que consulta o arquivo da ementa
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

	// jvosantos - 04/06/2019 09:50 - M�todo que salva os apreciados
	public String salvarApreciadosAudienciaVirtualProcessoSessaoSegundoGrau(AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioNe usuario, FabricaConexao fabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		VotoNe votoNe = new VotoNe();
		
		List arquivos = audienciaMovimentacaoDt.getListaArquivos();
		FabricaConexao fabrica = null;
		
		if(!audienciaMovimentacaoDt.isApreciados()) return "N�o � apreciados.";
		
		if(!audienciaMovimentacaoDt.isPreAnalise() && (arquivos == null || arquivos.isEmpty())) return "� necess�rio enviar um voto e uma ementa.";
		if(StringUtils.isEmpty(audienciaMovimentacaoDt.getAudienciaStatusCodigo())) return "� necess�rio selecionar uma situa��o.";

		if(fabricaConexao != null)
			fabrica = fabricaConexao;
		else {
			fabrica = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			fabrica.iniciarTransacao();
		}

		try {
			PendenciaDt pendenciaApreciados = pendenciaNe.consultarPendenciaApreciados(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId(), fabrica);
			//lrcampos 13/03/2020 18:00 - Verifica usuario logado � responsavel pela pendencia
			if(arquivos != null)
				pendenciaNe.verificaResponsavelPendencia(pendenciaApreciados.getId(), usuario.getUsuarioDt());
			
			if(pendenciaApreciados == null) return "Essa pend�ncia de apreciados n�o existe mais.";
			
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
				
				//if(status == null) throw new Exception("N�o foi encontrado o status selecionado.");
				
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
		
		// jvosantos - 19/11/2019 16:56 - Remover c�digo errado e morto (exposto pela tipagem da lista PendenciaArquivoNe.consultarArquivosPendencia)

		pendenciaNe.alterarStatus(pendenciaApreciados, fabrica);
	}
	
	// jvosantos - 24/09/2019 15:56 - M�todo responsavel por alterar o presidente de uma audi�ncia
	public void alterarPresidenteSessao(String id_Audiencia, String Id_NovoServentiaCargoPresidente, UsuarioNe usuarioNe) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		if(StringUtils.isEmpty(id_Audiencia) || StringUtils.isEmpty(Id_NovoServentiaCargoPresidente))
			throw new Exception(" ID_Audi ou ID_Serv_Cargo do presidente est�/est�o vazio(s). ID_AUDI = "+id_Audiencia+"; ID_SERV_CARGO_PRESIDENTE = "+Id_NovoServentiaCargoPresidente);
		
		try {
			fabricaConexao.iniciarTransacao();
			
			AudienciaDt audienciaDt = consultarAudienciaCompletaSessaoVirtual(id_Audiencia, fabricaConexao);
			
			if(audienciaDt == null)
				throw new Exception("A audi�ncia n�o foi encontrada! ID_AUDI = "+id_Audiencia);
			
			if(!audienciaDt.isVirtual())
				throw new Exception("Essa audi�ncia n�o � virtual! ID_AUDI = "+id_Audiencia);
			
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(Id_NovoServentiaCargoPresidente, fabricaConexao);
			
			if(serventiaCargoDt == null)
				throw new Exception("O novo presidente n�o foi encontrado! ID_SERV_CARGO_PRESIDENTE = "+Id_NovoServentiaCargoPresidente);
			
			
			
			
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

	// jvosantos - 24/09/2019 15:56 - M�todo responsavel por alterar o presidente de uma audi�ncia processo
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
		
		// jvosantos - 27/09/2019 11:27 - Adicionar informa��es de Log
		ap.setIpComputadorLog(usuarioNe.getIpComputadorLog());
		ap.setId_UsuarioLog(usuarioNe.getId_Usuario());
		
		audienciaProcessoNe.salvar(ap, fabricaConexao);
		
		if(criarPendencia)
			pendenciaNe.gerarPendenciaSessaoConhecimento(novoPresidente.getId(), usuarioNe.getUsuarioDt(), ap.getId_Processo(), ap.getId(), fabricaConexao);
	}
	
	// mrbatista - 24/09/2019 15:56 - M�todo responsavel por alterar o presidente de uma audi�ncia
	public void alterarResponsavelMp(String id_Audiencia, String Id_NovoServentiaCargoMP, UsuarioNe usuarioNe) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		if(StringUtils.isEmpty(id_Audiencia) || StringUtils.isEmpty(Id_NovoServentiaCargoMP))
			throw new Exception(" ID_Audi ou ID_Serv_Cargo do respons�vel do mp est�/est�o vazio(s). ID_AUDI = "+id_Audiencia+"; ID_SERV_CARGO_MP = "+Id_NovoServentiaCargoMP);
		
		try {
			fabricaConexao.iniciarTransacao();
			
			AudienciaDt audienciaDt = consultarAudienciaCompletaSessaoVirtual(id_Audiencia, fabricaConexao);
			
			if(audienciaDt == null)
				throw new Exception("A audi�ncia n�o foi encontrada! ID_AUDI = "+id_Audiencia);
			
			if(!audienciaDt.isVirtual())
				throw new Exception("Essa audi�ncia n�o � virtual! ID_AUDI = "+id_Audiencia);
			
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(Id_NovoServentiaCargoMP, fabricaConexao);
			
			if(serventiaCargoDt == null)
				throw new Exception("O novo respons�vel do MP n�o foi encontrado! ID_SERV_CARGO_MP = "+Id_NovoServentiaCargoMP);
			
			
			
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
	        
	        // Consulta dados completos do processo, pois ser� necess�rio ao salvar respons�veis pela audi�ncia e ao gerar pend�ncias
	        ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId());
			audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);			
					
	        MovimentacaoDt movimentacaoDt = null;
	        AudienciaProcessoDt audienciaProcessoNovaDt = null;
	        String idAudienciaProcesso = audienciaDt.getAudienciaProcessoDt().getId();
	        // Chama m�todo para gerar a movimenta��o de in�cio ou adiamento de sess�o...			
			if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
				AudienciaDt audienciaNovaDt = null;
				if (!audienciaMovimentacaoDt.isAlteracaoExtratoAta()) 
				{
					//Obtem a nova sess�o...
					AudienciaPs obPersistencia = new  AudienciaPs(obFabricaConexao.getConexao());
					audienciaNovaDt = Optional.ofNullable(StringUtils.isEmpty(audienciaMovimentacaoDt.getIdProximaAudiencia()) ?
							obPersistencia.consultarProximaSessaoAberta(processoDt.getId_Serventia(), audienciaDt.getDataAgendada(), true) :
								obPersistencia.consultarId(audienciaMovimentacaoDt.getIdProximaAudiencia()))
							.orElseThrow(() -> new MensagemException("N�o foi localizada uma sess�o aberta com data posterior."));
					// Verifica se existe uma Pr�xima Sess�o Aberta.
					if (audienciaNovaDt == null){
						retornoMensagemInconsistencia = "N�o foi localizada uma sess�o aberta com data posterior.";
						return retornoMensagemInconsistencia;
					}			
					// Cria a nova Audi�ncia Processo
					audienciaProcessoNovaDt = obtenhaNovaAudienciaProcessoDtJulgamentoIniciadoAdiado(audienciaDt, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaNovaDt, logDt, obFabricaConexao);
					idAudienciaProcesso = audienciaProcessoNovaDt.getId();
				} else {
					audienciaNovaDt = audienciaDt;
					audienciaProcessoNovaDt = audienciaDt.getAudienciaProcessoDt();
				}
				// Gera a movimenta��o no Processo			
				movimentacaoDt = gerarMovimentacaoJulgamentoIniciadoAdiadoManterAdiado(audienciaDt, usuarioDt.getId_UsuarioServentia(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), false, audienciaMovimentacaoDt.isAlteracaoExtratoAta(), audienciaNovaDt, logDt, obFabricaConexao);				
			}							
						
			arquivos = audienciaMovimentacaoDt.getListaArquivos();
			String Id_ArquivoAta = "";
			// Salva arquivos inseridos
			if (arquivos != null && arquivos.size() > 0) {
				movimentacaoArquivoNe.inserirArquivos(arquivos, logDt, obFabricaConexao);
				ArquivoDt arquivoDt = (ArquivoDt) arquivos.get(0);
				// Salvando v�nculos do arquivo de Ata com a Audi�ncia Processo
				Id_ArquivoAta = arquivoDt.getId();										
			}		
			
			//Vincula os arquivos, dependendo do tipo da movimenta��o
			if (audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada() || audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada()){
				//Vincula o arquivo iserido � Audi�ncia Processo
				audienciaProcessoNe.alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(audienciaProcessoNovaDt, "", "", "", Id_ArquivoAta, audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada(), "", "", "",logDt, obFabricaConexao);
			} else {
				//Chama m�todo para Atualizar Dados em "Audiencia", "AudienciaProcesso", gerar movimenta��o correspondente a movimenta��o da audi�ncia e inserir respons�veis pela audi�ncia
				movimentacaoDt = movimentarAudienciaAnalistaSegundoGrau(audienciaDt, audienciaDt.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo(), audienciaMovimentacaoDt.getId_NovaSessao(), audienciaMovimentacaoDt.getDataNovaSessao(), audienciaMovimentacaoDt.getAudienciaStatus() ,audienciaMovimentacaoDt.getAudienciaStatusCodigo(), Id_ArquivoAta, Id_ServentiaCargoPresidente, Id_ServentiaCargoMP, audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), usuarioDt, logDt, obFabricaConexao);				
			}			
			movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumeroCompleto());
			movimentacoes.add(movimentacaoDt);	 					
			
			String visibilidade=null;
			if (processoDt.isSegredoJustica()){
				visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
			}
			
			// Salvando v�nculo entre movimenta��o e arquivos inseridos
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), visibilidade, logDt, obFabricaConexao);			

			// Salvando pend�ncias da movimenta��o
			if (audienciaMovimentacaoDt.getListaPendenciasGerar() != null) 
				pendenciaNe.gerarPendencias(processoDt, audienciaMovimentacaoDt.getListaPendenciasGerar(), movimentacaoDt, arquivos, usuarioDt, null, logDt, obFabricaConexao);			

			// Atualiza Classificador processo
			if (audienciaMovimentacaoDt.getId_Classificador().length() > 0)
				(new ProcessoNe()).alterarClassificadorProcesso(processoDt.getId_Processo(), processoDt.getClassificador(), audienciaMovimentacaoDt.getId_Classificador(), logDt, obFabricaConexao);			

//			// Gera recibo para arquivos de movimenta��es
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
						
			// Cria publica��o
			// Decreto 1.684 / 2020, Art 2o e 3o
			if (!processoDt.isSigiloso()) {
				pendenciaNe.salvarPublicacao(new PendenciaDt(), arquivos, usuarioDt, logDt, obFabricaConexao);
			}
			
    		 // Se for voto vencido iremos gerar uma conclus�o do tipo voto/ementa para o desembargador redator poder pr�-analisar
	        if (ehVotoVencido){
	        	// Finalizar a pend�ncia de ementa do desembargador com voto vencido
	        	finalizarPendenciaEmentaRelatorSessao(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), pendenciaNe, usuarioDt, audienciaDt.getAudienciaProcessoDt().getId(), obFabricaConexao);
	        	// Alterar pend�ncia de voto para o desembargador inserir o voto vencido
				gerarPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), true, pendenciaNe, usuarioDt, obFabricaConexao, null, idAudienciaProcesso);
	        	// Gerar pend�ncia de voto para o desembargador inserir o voto vencedor
				gerarPendenciaVoto(audienciaMovimentacaoDt.getId_ServentiaCargoRedator(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, null, idAudienciaProcesso);					
	        } else {
		        // Gerar pend�ncia de voto para o desembargador inserir o voto, caso n�o exista...
				gerarPendenciaVoto(audienciaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId(), audienciaDt.getAudienciaProcessoDt().getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, null, audienciaDt.getAudienciaProcessoDt().getId());
	        }
    		
    		return retornoMensagemInconsistencia;
		} catch(MensagemException m){
	        throw m;
		
		}
	}
}
