package br.gov.go.tj.projudi.ne;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.CertidaoExecucaoCPCDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ListaDadosServentiaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.NaturezaSPGDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.ProcessoNotaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoAssuntoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.ps.ProcessoPs;
import br.gov.go.tj.projudi.util.DistribuicaoProcesso;
import br.gov.go.tj.projudi.util.DistribuicaoProcessoServentiaCargo;
import br.gov.go.tj.projudi.util.EscreverTextoPDF;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.projudi.util.GerarCapaProcessoPDF;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProcessoNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverteImagemPDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;
import br.gov.go.tj.utils.pdf.GerarPDF;

//---------------------------------------------------------
public class ProcessoNe extends ProcessoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8918252658149779767L;
					
	private String codigoServentiaParaRollback;
	private String codigoComarcaCodigoParaRollback;
	private String numrProcessoParaRollback;
	private String idUsuarioLogRollback;
	private String idComputadorLogRollback;

	private BuscaProcessoDt objBuscaProcessoDt = new BuscaProcessoDt();

	private String stUltimoId_Serventia="";
	private String stUltimaOpcaoPeriodo="";
	private String stUltimoId_UsuServ="";
	private String stUltimoUsuarioRelator="";
	private String stUltimaOpcaoPeriodoSemMovimentacao="";
	private String stUltimoId_Comarca="";

	
	private static String ID_1_VARA_DA_FAZENDA_PUBLICA_MUNICIPAL_DE_GOIANIA;
	
	static boolean isProcessoDa1aVaraDaFazendaPublicaMunicipalDeGoiania(ProcessoDt processoDt) throws Exception {
		if(ID_1_VARA_DA_FAZENDA_PUBLICA_MUNICIPAL_DE_GOIANIA == null){
			ServentiaNe ne = new ServentiaNe();
			ID_1_VARA_DA_FAZENDA_PUBLICA_MUNICIPAL_DE_GOIANIA = ne.consultarServentiaCodigo("613").getId();
		}
		return processoDt.getId_Serventia().equals(ID_1_VARA_DA_FAZENDA_PUBLICA_MUNICIPAL_DE_GOIANIA);
	}
	
	private static String ID_PROC_TIPO_EXECUCAO_FISCAL;
	
	private static boolean isProcessoExecucaoFiscal(ProcessoDt processoDt) throws Exception {
		if(ID_PROC_TIPO_EXECUCAO_FISCAL == null){
			ProcessoTipoNe ne = new ProcessoTipoNe();
			ID_PROC_TIPO_EXECUCAO_FISCAL = ne.consultarProcessoTipoCodigo(Integer.toString(ProcessoTipoDt.EXECUCAO_FISCAL)).getId();
		}
		return processoDt.getId_ProcessoTipo().equals(ID_PROC_TIPO_EXECUCAO_FISCAL);
	}
	
	public static String getIdTipoExecucaoFiscal() throws Exception {
		if(ID_PROC_TIPO_EXECUCAO_FISCAL == null){
			ProcessoTipoNe ne = new ProcessoTipoNe();
			ID_PROC_TIPO_EXECUCAO_FISCAL = ne.consultarProcessoTipoCodigo(Integer.toString(ProcessoTipoDt.EXECUCAO_FISCAL)).getId();
		}
		return ID_PROC_TIPO_EXECUCAO_FISCAL;
	}
	
	
	/**
	 * Consulta a quantidade de processos paralizados com pendencias a mais de
	 * 30 dias 03/12/2008 - 17:00: Decidido que será realizado depois estes
	 * casos
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 03/12/2008 16:00
	 * @param UsuarioDt
	 *            usuarioDt
	 * @return
	 * @throws Exception
	 */
	/*
	 * public int consultarQuantidadeParalizados30dias(UsuarioDt usuarioDt)
	 * throws Exception {int grupo =
	 * Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
	 * 
	 * switch (grupo) {case GrupoDt.ADVOGADOS: break; case GrupoDt.JUIZES_VARA:
	 * case GrupoDt.JUIZES_TURMA_RECURSAL: case GrupoDt.PROMOTORES: case
	 * GrupoDt.CONTADORES_VARA: case GrupoDt.AUTORIDADES_POLICIAIS:
	 * 
	 * break; case GrupoDt.ANALISTAS_JUDICIARIOS_VARA: case
	 * GrupoDt.TECNICOS_JUDICIARIOS_VARA: case
	 * GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL: case
	 * GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL: case GrupoDt.CONSULTORES:
	 * break; }
	 * 
	 * return -1; }
	 */

	/**
	 * Método responsável em gravar no banco os dados (inserção ou alteração)
	 * @throws Exception 
	 */
	public void salvar(ProcessoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try {
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			if (dados.getId_Processo().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Processo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Processo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch (Exception e) {
			dados.setId("");
			throw e;
		}
	}

	/**
	 * Método para validar dados de um processo no momento da modificação de
	 * dados
	 */
	// TODO jpcpresa: Alterar o metodo para usar ProcessoTipoProcessoSubtipo
	public String verificarDadosAlteracao(ProcessoDt dados) throws Exception {
		String stRetorno = "";

		ProcessoTipoDt processoTipoDt;
		ProcessoDt processoAtual = this.consultarId(dados.getId());
		
		if (!dados.isCriminal() && !dados.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))
				&& !dados.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
			if (dados.getListaPolosAtivos() == null || dados.getListaPolosAtivos().size() == 0){
				stRetorno += "Insira ao menos uma parte promovente. \n";
			}
		}

		//Valida se está sendo feita alteração do Processo Fase. Se estiver fazendo essa alteração, é preciso bloquear a alteração para a fase
		//RECURSO se o tipo da Serventia for Vara.
		if(!processoAtual.getId_ProcessoFase().equals(dados.getId_ProcessoFase())){
			if (Funcoes.StringToInt(dados.getId_ProcessoFase()) == ProcessoFaseDt.RECURSO && Funcoes.StringToInt(dados.getServentiaTipoCodigo()) == ServentiaTipoDt.VARA) {
				stRetorno += "Em Serventias de 1º Grau não é possível alterar Fase Processual para Recurso.";
			}
		}
		
		if (dados.getId_ProcessoTipo().length() == 0 || dados.getProcessoTipo().length() == 0) stRetorno += "Selecione a Classe. \n";
		else {
			// Casos de não ter a parte promovida
			processoTipoDt = new ProcessoTipoNe().consultarId(dados.getId_ProcessoTipo());

			// Verifica se o processo Não é contencioso caso não seja
			ProcessoTipoProcessoSubtipoNe ptpst = new ProcessoTipoProcessoSubtipoNe();
			boolean naoContencioso = ptpst.isNaoContecioso(Funcoes.StringToInt(dados.getProcessoTipoCodigo()));
			if ((dados.getListaPolosPassivos() == null || dados.getListaPolosPassivos().size() == 0) && (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo())) != ProcessoTipoDt.ALVARA_JUDICIAL && (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo())) != ProcessoTipoDt.HOMOLOGACAO_ACORDO && (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo())) != ProcessoTipoDt.CONVERSAO_SEPARACAO_EM_DIVORCIO && !naoContencioso && (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo())) != ProcessoTipoDt.DIVORCIO_CONSENSUAL && (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo())) != ProcessoTipoDt.SEPARACAO_CONSENSUAL && (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo())) != ProcessoTipoDt.RETIFICACAO_REGISTRO) {
				stRetorno += "Insira ao menos uma parte promovida. \n";
			}
		}

		if (dados.isCivel() && dados.isSegundoGrau()) {
			if (dados.getValor().trim().length() == 0) stRetorno += "Valor da causa deve ser maior que zero. \n";
		}
		
		if (dados.getId_Custa_Tipo() != null && dados.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))
				&& !processoAtual.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))){
			AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			if (areaDistribuicaoNe.isAreaDistribuicaoComCusta(dados.getId_AreaDistribuicao())){
				PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
				boolean possuiParteIsenta = false;
				for (Iterator iterator = dados.getListaPolosAtivos().iterator(); iterator.hasNext();) {
					ProcessoParteDt processoParteDt = (ProcessoParteDt) iterator.next();
					if (processoParteDt.getCnpj() != null && processoParteDt.getCnpj().length()>0 && partesIsentaNe.isParteIsenta(processoParteDt.getCnpj())){
						possuiParteIsenta = true;
						break;
					}
				}
				
				//Validação da modelo da guia
				boolean classeComCusta;
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, dados.getId_ProcessoTipo()) != null ) {
					classeComCusta = true;
				} else if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, dados.getId_ProcessoTipo()) != null ) {
					classeComCusta = true;	
				} else {
					classeComCusta = false;
				}
				
				if (!possuiParteIsenta && classeComCusta){
					stRetorno += "O processo com custa Tipo Isento deve possuir pelo menos uma parte isenta ou uma classe sem custa processual. \n";
				} 
			}
		}
		
		return stRetorno;
	}

	/**
	 * Método genérico para validar dados de um processo
	 */
	public String Verificar(ProcessoDt dados) {

		String stRetorno = "";

		if (dados.getId_ProcessoTipo().length() == 0) stRetorno += "Especifique o tipo da ação. \n";
		if (dados.getId_ProcessoFase().length() == 0) stRetorno += "Especifique a fase processual. \n";
		if (dados.getValor().trim().length() == 0) stRetorno += "Valor da causa deve ser maior que zero. \n";
		if (dados.getListaAssuntos() == null || dados.getListaAssuntos().size() == 0) stRetorno += "Especifique ao menos um Assunto para o processo.";

		return stRetorno;
	}

	/**
	 * Método responsável em alterar os dados de um processo
	 * 
	 * @param processoDt
	 *            : dados do processo
	 * @param listaAssuntosAnterior
	 *            : lista de assuntos do processo antes da alteração
	 * 
	 * @author msapaula
	 */
	public void alterarDadosProcesso(ProcessoDt processoDt, List listaAssuntosAnterior, String Id_UsuarioServentia) throws Exception {
		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Dados anteriores do processo
			obDados = this.consultarId(processoDt.getId(), obFabricaConexao);
			
			// Salva alterações do processo
			if (!processoDt.getId_Processo().equalsIgnoreCase("")) {
				logDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoDt.getPropriedades());
				obPersistencia.alterar(processoDt);
				
				if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
					processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.CALCULO));
					this.alterarStatusProcesso(processoDt, ProcessoStatusDt.CALCULO);
				}
				
				if(processoDt.isCriminal()) {
					ProcessoCriminalNe processoCriminalNe = new ProcessoCriminalNe();
					processoCriminalNe.salvar(processoDt.getProcessoCriminalDt(), obFabricaConexao);
				}
				
				//se houve alteração de custa tipo do processo isento para outra custa, altera o status guia baixada com isenção (B.O 2018/11787)
				if (obDados.getId_Custa_Tipo().trim().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO)) 
						&& !obDados.getId_Custa_Tipo().trim().equalsIgnoreCase(processoDt.getId_Custa_Tipo().trim())){
					
					GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
					GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoInicialBaixadaIsencao(processoDt.getId_Processo(), obFabricaConexao);
					
					if (guiaEmissaoDt != null && guiaEmissaoDt.getId()!=null && guiaEmissaoDt.getId().length()>0){
						if (processoDt.getId_Custa_Tipo().trim().equalsIgnoreCase(String.valueOf(ProcessoDt.COM_CUSTAS))){
							guiaEmissaoNe.atualizarStatusGuiaInicialAguardandoDeferimento(guiaEmissaoDt.getId(), String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO), obFabricaConexao);
							
						} else if (processoDt.getId_Custa_Tipo().trim().equalsIgnoreCase(String.valueOf(ProcessoDt.ASSISTENCIA_JURIDICA))){
							guiaEmissaoNe.atualizarStatusGuiaInicialAguardandoDeferimento(guiaEmissaoDt.getId(), String.valueOf(GuiaStatusDt.AGUARDANDO_DEFERIMENTO), obFabricaConexao);
						}
					}
					
				}
				
				// Se houve alteração na classe, insere uma movimentação
				if (!obDados.getId_ProcessoTipo().trim().equalsIgnoreCase(processoDt.getId_ProcessoTipo().trim())){				
					String complemento = String.format("Houve uma mudança da classe \"%s-%s\" para a classe \"%s-%s\"", obDados.getId_ProcessoTipo().trim(), obDados.getProcessoTipo(), processoDt.getId_ProcessoTipo().trim(), processoDt.getProcessoTipo());
					(new MovimentacaoNe()).gerarMovimentacaoMudancaClasseProcesso(processoDt.getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
				}	
				
				// Se houve alteração em assuntos, insere uma movimentação
				String listaAnterior = "";
				String listaAtual = "";
				
				for (int j = 0; j < listaAssuntosAnterior.size(); j++) {								
					listaAnterior = listaAnterior + ((ProcessoAssuntoDt) listaAssuntosAnterior.get(j)).getId_Assunto();
				}
				
				for (int j = 0; j < processoDt.getListaAssuntos().size(); j++) {								
					listaAtual = listaAtual + ((ProcessoAssuntoDt) processoDt.getListaAssuntos().get(j)).getId_Assunto();
				}
						
				if (!listaAnterior.equals(listaAtual)){			
					String complemento = "";
					(new MovimentacaoNe()).gerarMovimentacaoMudancaAssuntoProcesso(processoDt.getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
				}	
				
			}
			if(isProcessoExecucaoFiscal(processoDt) && processoDt.getValor() != null && obDados != null && obDados.getValor() != null && !processoDt.getValor().trim().equalsIgnoreCase(obDados.getValor().trim())){
				efetueAlteracaoDeGuiaUnicaDeFazendaPublica(processoDt, obFabricaConexao);
			}

			// ***************************
			// TODO Fred: Aqui Gera a Guia Complementar
			// ***************************
//			GuiaComplementarNe guiaComplementarNe = new GuiaComplementarNe();
//			if( processoDt.getListaPromovidos() != null ) {
//				guiaComplementarNe.setQuantidadeRequeridos( processoDt.getListaPromovidos().size() );
//			}
//			guiaComplementarNe.setProcessoTipoCodigo( Funcoes.StringToInt(processoDt.getProcessoTipoCodigo()) );
//			guiaComplementarNe.emitirGuiaComplementarAutomatica( obFabricaConexao, processoDt.getId(), processoDt.getId_Serventia(), obDados.getValor(), processoDt.getValor(), obDados.getId_ProcessoTipo(), processoDt.getId_ProcessoTipo() );
//			
//			//Gerar a Pendência para Verificar a Guia 
//			if( guiaComplementarNe.isGuiaEmitidaComSucesso() ) {
//				PendenciaNe pendenciaNe = new PendenciaNe();
//				pendenciaNe.gerarPendenciaVerificarGuia(processoDt, Id_UsuarioServentia, processoDt.getId_Serventia(), "", null, null, logDt, obFabricaConexao); 
//			}
			// ***************************
			// ***************************

			obDados.copiar(processoDt);
			obLog.salvar(logDt, obFabricaConexao);

			// Salva assuntos do processo, insere os novos os exclui os que não
			// existem mais
			ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
			processoAssuntoNe.salvarAssuntosProcesso(processoDt, listaAssuntosAnterior, logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método responsável em alterar os dados de um processo vinculado a um recurso
	 * 
	 * @param processoDt
	 *            : dados do processo
	 * 
	 * @author lsbernardes
	 */
	public void alterarDadosProcessoRecurso(ProcessoDt processoDt, String Id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
		
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Salva alterações do processo
		if (!processoDt.getId_Processo().equalsIgnoreCase("")) {
			logDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoDt.getPropriedades());
			obPersistencia.alterarProcessoRecurso(processoDt);
		}

		obDados.copiar(processoDt);
		obLog.salvar(logDt, obFabricaConexao);	}

	/**
	 * Método responsável em validar os dados de um Processo Cível
	 */
	public String verificarProcessoCivel(ProcessoCadastroDt dados, UsuarioDt usuarioDt) throws Exception {

		String stRetorno = "";
		if (!dados.isProcessoFisico()) {
			if(dados.getGrauProcesso().equals("")  ||  dados.getTipoProcesso().equals("") || dados.getAssistenciaProcesso().equals("")){
				stRetorno += "Escolha o Grau, Tipo e Assistência do Processo. \n";
			} else {
				if(!dados.getAssistenciaProcesso().equals("2") && dados.getDependenciaProcesso().equals("")){
					stRetorno += "Escolha a Dependência do Processo. \n";
				}
			}
		}
		
		if (dados.isProcessoFisico()) {
			// Valida número do processo físico.
			if (dados.getProcessoNumeroFisico() == null || dados.getProcessoNumeroFisico().trim().length() == 0) stRetorno += "Insira o número do processo. \n";
			else {
				if (!Funcoes.validaProcessoNumero(dados.getProcessoNumeroFisico())) {
					stRetorno += "Número do processo inválido. \n";
					dados.limpeProcessoNumeroFisico();
				}
				else {
					if(!Funcoes.validarJTRProcesso(dados.getProcessoNumeroFisico())) {
						stRetorno += "Processo de outro estado/órgão não pode ser recadastrado. Realize o cadastramento do processo. \n";
						dados.limpeProcessoNumeroFisico();
					}
					else {
						if( this.consultarProcessoNumeroCompletoDigitoAno(dados.getProcessoNumeroFisico()) != null ) {
							stRetorno += "Número do processo SPG/SSG informado já existe. \n";
						}
					}
				}
			}

			// Valida se o Juiz Responsável foi informado.
			if (dados.getId_ServentiaCargo() == null || dados.getId_ServentiaCargo().trim().length() == 0 || dados.getServentiaCargo() == null || dados.getServentiaCargo().trim().length() == 0) stRetorno += "Selecione o Juiz Responsável. \n";
		}

		if (dados.getListaPolosAtivos() == null || dados.getListaPolosAtivos().size() == 0) stRetorno += "Insira ao menos uma parte promovente. \n";
		
		if (dados.getId_ProcessoTipo() != null && dados.getId_ProcessoTipo().length()>0){
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(dados.getId_ProcessoTipo());
			//Se o processo for do tipo HABEAS CORPUS é preciso que seja cadastrada uma parte do tipo PACIENTE
			if(processoTipoDt.isHabeasCorpus()) {
				
//				//Se processo for HABEAR CORPUS é preciso cadastrar processo dependente.
//				if(!dados.isProcessoDependenteVariavel()){
//					stRetorno += "A opção PROCESSO DEPENDENTE deve ser marcada. \n";
//				} else{
					if (dados.getListaOutrasPartes() == null || dados.getListaOutrasPartes().size() == 0) {
						stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
					} else {
						boolean temPaciente = false;
						for (int i = 0; i < dados.getListaOutrasPartes().size(); i++) {
							ProcessoParteDt parteDt = (ProcessoParteDt)dados.getListaOutrasPartes().get(i);
							ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarId(parteDt.getId_ProcessoParteTipo());
							if(processoParteTipoDt.isPaciente()) {
								temPaciente = true;
								break;
							}
							
						}
						if(!temPaciente) {
							stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
						}
					}
//				}
			}
		}
		
		if (dados.getId_ProcessoPrioridade() == null || dados.getId_ProcessoPrioridade().trim().length() == 0) stRetorno += "Escolha uma prioridade. \n";

		if (dados.getId_ProcessoTipo().length() == 0 || dados.getProcessoTipo().length() == 0){ 
			stRetorno += "Selecione a Classe. \n";
		
		} else if (dados.getId_ProcessoTipo() != null && dados.getId_ProcessoTipo().length()>0){
			// Casos de não ter a parte promovida
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(dados.getId_ProcessoTipo());
			// Verifica se o processo Não é contencioso caso não seja
			ProcessoTipoProcessoSubtipoNe ptpst = new ProcessoTipoProcessoSubtipoNe();
			boolean naoContencioso = ptpst.isNaoContecioso(Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo()));
			if (!naoContencioso && (dados.getListaPolosPassivos() == null || dados.getListaPolosPassivos().size() == 0)) {
				stRetorno += "Insira ao menos uma parte promovida. \n";
			}
		}

		if (dados.getListaAssuntos() == null || dados.getListaAssuntos().size() == 0) stRetorno += "Insira ao menos um Assunto para o processo. \n";

		if (!dados.isProcessoDependente() && (dados.getId_Serventia().equals("")) && (dados.getId_AreaDistribuicao().length() == 0 || dados.getAreaDistribuicao().length() == 0)) stRetorno += "Selecione a Área de Distribuição. \n";

		if (dados.getValor().trim().length() == 0 || Funcoes.StringToDouble(dados.getValor().trim()) < 0D ) stRetorno += "Valor da causa deve ser maior que zero. \n";

		if (dados.isProcessoDependente()) {
			if (dados.getProcessoNumeroPrincipal() != null && dados.getProcessoNumeroPrincipal().length() > 0) {
				ProcessoDt dependenteDt = this.consultarProcessoNumeroCompleto(dados.getProcessoNumeroPrincipal(),null);
				if (dependenteDt != null && dependenteDt.getId_Processo() != null && dependenteDt.getId_Processo().length() > 0) {
					//trecho comentado por conta do BO 2018/14511 - erro para localizar serventia para processo dependente
//					ServentiaDt serventiaRecursoOrigem = this.consultarServentiaOrigemRecurso(dependenteDt.getId());
//					
//					if (serventiaRecursoOrigem != null && serventiaRecursoOrigem.getId() != null && serventiaRecursoOrigem.getId().length()>0){
//						dependenteDt.setId_ServentiaOrigemRecurso(serventiaRecursoOrigem.getId());
//						dependenteDt.setServentiaOrigemRecurso(serventiaRecursoOrigem.getServentia());
//					}
					////////////////
					dados.setProcessoDependenteDt(dependenteDt);
				} else {
					stRetorno += "Processo Principal inexistente. Número do processo informado: "+ dados.getProcessoNumeroPrincipal() +" \n";
				}
			} else
				stRetorno += "Número do Processo Principal deve ser informado. \n";
		}
		
		if ((dados.getDataRecebimento().length() > 0) && (!Funcoes.validaData(dados.getDataRecebimento()))) {
			stRetorno += "Data de Recebimento em formato incorreto. \n";
		}
		
		if (dados.getId_Custa_Tipo() != null && dados.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))){
			AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			if (areaDistribuicaoNe.isAreaDistribuicaoComCusta(dados.getId_AreaDistribuicao()) && !usuarioDt.isMp()){
				PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
				boolean possuiParteIsenta = false;
				for (Iterator iterator = dados.getListaPolosAtivos().iterator(); iterator.hasNext();) {
					ProcessoParteDt processoParteDt = (ProcessoParteDt) iterator.next();
					if (processoParteDt.getCnpj() != null && processoParteDt.getCnpj().length()>0 && partesIsentaNe.isParteIsenta(processoParteDt.getCnpj())){
						possuiParteIsenta = true;
						break;
					}
				}
				
				//Validação da modelo da guia
				boolean classeComCusta;
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, dados.getId_ProcessoTipo()) != null ) {
					classeComCusta = true;
				} else {
					classeComCusta = false;
				}
				
				if (!possuiParteIsenta && classeComCusta){
					stRetorno += "O processo com custa Tipo Isento deve possuir pelo menos uma parte isenta ou uma classe sem custa processual. \n";
				} 
			}
			
		}
		
		return stRetorno;

	}

	/**
	 * Método responsável em validar os dados de um Processo Criminal
	 * 
	 * @param dados
	 */
	public String verificarProcessoCriminal(ProcessoCadastroDt dados, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";

		if (!dados.isProcessoFisico()) {
			if(dados.getGrauProcesso().equals("")  ||  dados.getTipoProcesso().equals("") || dados.getAssistenciaProcesso().equals("")){
				stRetorno += "Escolha o Grau, Tipo e Assistência do Processo. \n";
			} else {
				if(!dados.getAssistenciaProcesso().equals("2") && dados.getDependenciaProcesso().equals("")){
					stRetorno += "Escolha a Dependência do Processo. \n";
				}
			}
		}
		
		if (dados.isProcessoFisico()) {
			// Valida número do processo físico.
			if (dados.getProcessoNumeroFisico() == null || dados.getProcessoNumeroFisico().trim().length() == 0) stRetorno += "Insira o número do processo. \n";
			else {
				if (!Funcoes.validaProcessoNumero(dados.getProcessoNumeroFisico())) {
					stRetorno += "Número do processo inválido. \n";
					dados.limpeProcessoNumeroFisico();
				} else {
					if(!Funcoes.validarJTRProcesso(dados.getProcessoNumeroFisico())) {
						stRetorno += "Processo de outro estado/órgão não pode ser recadastrado. Realize o cadastramento do processo. \n";
						dados.limpeProcessoNumeroFisico();
					}
					else {
						if( this.consultarProcessoNumeroCompletoDigitoAno(dados.getProcessoNumeroFisico()) != null ) {
							stRetorno += "Número do processo SPG/SSG informado já existe. \n";
						}
					}
				}
			}

			// Valida se o Juiz Responsável foi informado.
			if (dados.getId_ServentiaCargo() == null || dados.getId_ServentiaCargo().trim().length() == 0 || dados.getServentiaCargo() == null || dados.getServentiaCargo().trim().length() == 0) stRetorno += "Selecione o Juiz Responsável. \n";
		}
		
		
		if (dados.getListaPolosAtivos() == null || dados.getListaPolosAtivos().size() == 0) stRetorno += "Insira ao menos uma parte promovente. \n";
		if (dados.getListaPolosPassivos() == null || dados.getListaPolosPassivos().size() == 0) stRetorno += "Insira ao menos uma parte promovida. \n";
		
		if (StringUtils.isNotEmpty(dados.getId_ProcessoTipo())){
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(dados.getId_ProcessoTipo());
			//Se o processo for do tipo HABEAS CORPUS é preciso que seja cadastrada uma parte do tipo PACIENTE
			if(processoTipoDt.isHabeasCorpus()) {
				
				//Se processo for HABEAR CORPUS é preciso cadastrar processo dependente.
	//			if(!dados.isProcessoDependenteVariavel()){
	//				stRetorno += "A opção PROCESSO DEPENDENTE deve ser marcada. \n";
	//			}else{
					if (dados.getListaOutrasPartes() == null || dados.getListaOutrasPartes().size() == 0) {
						stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
					} else {
						boolean temPaciente = false;
						for (int i = 0; i < dados.getListaOutrasPartes().size(); i++) {
							ProcessoParteDt parteDt = (ProcessoParteDt)dados.getListaOutrasPartes().get(i);
							ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarId(parteDt.getId_ProcessoParteTipo());
							if(processoParteTipoDt.isPaciente()) {
								temPaciente = true;
								break;
							}
							
						}
						if(!temPaciente) {
							stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
						}
					}
	//			}
			}
		}
		
		if (dados.getId_ProcessoTipo().length() == 0 || dados.getProcessoTipo().length() == 0) stRetorno += "Selecione a Classe. \n";
		if (dados.getListaAssuntos() == null || dados.getListaAssuntos().size() == 0) stRetorno += "Insira ao menos um Assunto para o processo. \n";
		if (!dados.isProcessoDependente() && (dados.getId_Serventia().equals("")) && (dados.getId_AreaDistribuicao().length() == 0)) stRetorno += "Selecione a Área de Distribuição. \n";
		if (dados.isProcessoDependente()) {
			if (dados.getProcessoNumeroPrincipal().length() > 0) {
				ProcessoDt dependenteDt = this.consultarProcessoPrincipal(dados.getProcessoNumeroPrincipal(),null);
				if (dependenteDt.getId_Processo().length() > 0) dados.setProcessoDependenteDt(dependenteDt);
				else {
					stRetorno += "Processo Principal inexistente. \n";
				}
			} else
				stRetorno += "Número do Processo Principal deve ser informado. \n";
		}
		
		if (dados.getId_Custa_Tipo() != null && dados.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))){
			AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			if (areaDistribuicaoNe.isAreaDistribuicaoComCusta(dados.getId_AreaDistribuicao()) && !usuarioDt.isMp()){
				PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
				boolean possuiParteIsenta = false;
				if (dados.getListaPolosAtivos() != null){
					for (Iterator iterator = dados.getListaPolosAtivos().iterator(); iterator.hasNext();) {
						ProcessoParteDt processoParteDt = (ProcessoParteDt) iterator.next();
						if (processoParteDt.getCnpj() != null && processoParteDt.getCnpj().length()>0 && partesIsentaNe.isParteIsenta(processoParteDt.getCnpj())){
							possuiParteIsenta = true;
							break;
						}
					}
				}
				
				//Validação da modelo da guia
				boolean classeComCusta;
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, dados.getId_ProcessoTipo()) != null ) {
					classeComCusta = true;
				} else {
					classeComCusta = false;
				}
				
				if (!possuiParteIsenta && classeComCusta){
					stRetorno += "O processo com custa Tipo Isento deve possuir pelo menos uma parte isenta ou uma classe sem custa processual. \n";
				} 
			}
		}
		
		if (dados.getId_ProcessoPrioridade() == null || dados.getId_ProcessoPrioridade().trim().length() == 0) stRetorno += "Escolha uma prioridade. \n";
		
		return stRetorno;
	}

	/**
	 * Método responsável em validar os dados de um Processo originário de
	 * Importação
	 * 
	 * @param dados
	 */
	public String verificarProcessoImportacao(ProcessoCadastroDt dados, int tipoLeiaute) throws Exception {
		StringBuilder stRetorno = new StringBuilder();

		// Validação da Área de Distribuição
		if (!dados.getAreaDistribuicaoCodigo().equals("")) {
			if (!Funcoes.validaNumerico(dados.getAreaDistribuicaoCodigo())) stRetorno.append("Código da Área de Atuação informado é inválido: " + dados.getAreaDistribuicaoCodigo() + ". \n");
			else {
				AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
				AreaDistribuicaoDt areaDistribuicaoDt = areaDistribuicaoNe.consultarAreaDistribuicaoCodigo(dados.getAreaDistribuicaoCodigo());
				if (areaDistribuicaoDt == null) stRetorno.append("Código da Área de Atuação inexistente: " + dados.getAreaDistribuicaoCodigo() + ". \n");
				else if (!areaDistribuicaoDt.getCodigoTemp().equals(String.valueOf(AreaDistribuicaoDt.ATIVO))) stRetorno.append("Área de Distribuição bloqueada para cadastro de processos: " + dados.getAreaDistribuicaoCodigo() + ". \n");
				else {
					dados.setId_AreaDistribuicao(areaDistribuicaoDt.getId());
					dados.setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());
					dados.setForumCodigo(areaDistribuicaoDt.getForumCodigo());
					dados.setId_Comarca(areaDistribuicaoDt.getId_Comarca());
					dados.setId_ServentiaSubTipo(areaDistribuicaoDt.getId_ServentiaSubtipo());
				}
			}
		} else
			stRetorno.append("É necessário informar o código da Área de Atuação. \n");

		// Validação do Tipo de Ação
		if (!dados.getProcessoTipoCodigo().equals("")) {

			if (!Funcoes.validaNumerico(dados.getProcessoTipoCodigo())) stRetorno.append("Codigo da Ação informado é inválido: " + dados.getProcessoTipoCodigo() + ". \n");
			else {
				ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
				ProcessoTipoDt processoTipoDt = processoTipoNe.consultarProcessoTipoCodigo(dados.getProcessoTipoCodigo());
				if (processoTipoDt == null) stRetorno.append("Código da Ação inexistente: " + dados.getProcessoTipoCodigo() + ". \n");
				else {
					dados.setId_ProcessoTipo(processoTipoDt.getId());
					dados.setProcessoTipo(processoTipoDt.getProcessoTipo());
				}
			}
		} else
			stRetorno.append("É necessário informar o código da ação. \n");
		
		if (dados.getProcessoPrioridadeCodigo() == null || dados.getProcessoPrioridadeCodigo().trim().length() == 0) stRetorno.append("Escolha uma prioridade. \n");

		// Validação do Valor da Ação
		if (!dados.getValor().equals("")) {
			if (!Funcoes.validaDouble(dados.getValor())) stRetorno.append("Valor da Causa em formato incorreto: " + dados.getValor() + ". \n");
			else
				dados.setValor(dados.getValor().replace(".", ","));
		}

		// Validação de Prioridade
		if (!dados.getProcessoPrioridadeCodigo().equals("")) {
			ProcessoPrioridadeNe prioridadeNe = new ProcessoPrioridadeNe();
			ProcessoPrioridadeDt prioridadeDt = prioridadeNe.consultarProcessoPrioridadeCodigo(dados.getProcessoPrioridadeCodigo());
			if (prioridadeDt != null) {
				dados.setId_ProcessoPrioridade(prioridadeDt.getId());
				dados.setProcessoPrioridade(prioridadeDt.getProcessoPrioridade());
			}
		}

		// Validação de Instituição 1 – SSP 2 – Procuradoria Estadual 3 –
		// Procuradoria Municipal 4 – Advogado Particular
		if (!dados.getCodigoInstituicao().equals("")) {

			if (!Funcoes.validaNumerico(dados.getCodigoInstituicao())) stRetorno.append("Código da Instituição informado é inválido: " + dados.getCodigoInstituicao() + ". \n");
			// deve ser 123 ou 4
			else if ("1234".indexOf(dados.getCodigoInstituicao()) == -1) stRetorno.append("Código da Instituição inexistente: " + dados.getCodigoInstituicao() + ". \n");
		} else
			stRetorno.append("É necessário informar o código da Instituição. \n");

		// Validações específicas para SSP
		if (tipoLeiaute == 1 || dados.getCodigoInstituicao().equals("1")) {
			ServentiaNe serventiaNe = new ServentiaNe();

			// Validação de Codigo da Delegacia
			if (!dados.getServentiaOrigemCodigo().equals("")) {

				if (!Funcoes.validaNumerico(dados.getServentiaOrigemCodigo())) stRetorno.append("Código da Delegacia informado é inválido: " + dados.getServentiaOrigemCodigo() + ". \n");
				else {
					ServentiaDt serventiaDelegaciaDt = serventiaNe.consultarServentiaCodigoExterno(dados.getServentiaOrigemCodigo());
					if (serventiaDelegaciaDt == null) stRetorno.append("Código da Delegacia inexistente: " + dados.getServentiaOrigemCodigo() + ". \n");
					else {
						dados.setId_ServentiaOrigem(serventiaDelegaciaDt.getId());
						dados.setServentiaOrigem(serventiaDelegaciaDt.getServentia());
					}
				}
			} else
				stRetorno.append("É necessário informar o código da Delegacia. \n");

			// Validação Número do Procedimento
			if ((!dados.getTcoNumero().equals("")) && (!dados.getTcoNumero().equals("\r"))) {

				if (!Funcoes.validaNumerico(dados.getTcoNumero())) stRetorno.append("Número do procedimento informado é inválido: " + dados.getTcoNumero() + ". \n");
				if (this.verificarTcoCadastrado(dados.getId_ServentiaOrigem(), dados.getTcoNumero())) stRetorno.append("Procedimento já cadastrado no sistema. \n");
			} else
				stRetorno.append("É necessário informar o número do procedimento. \n");
		}
		return stRetorno.toString();
	}

	/**
	 * Método responsável em validar os dados de um Processo de Segundo Grau
	 * Cível
	 * 
	 * @param processoDt
	 */
	public String verificarProcessoSegundoGrauCivel(ProcessoCadastroDt processoDt, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";
		
		if (!processoDt.isProcessoFisico()) {
			if(processoDt.getGrauProcesso().equals("")  ||  processoDt.getTipoProcesso().equals("") || processoDt.getAssistenciaProcesso().equals("")){
				stRetorno += "Escolha o Grau, Tipo e Assistência do Processo. \n";
			} else {
				if(!processoDt.getAssistenciaProcesso().equals("2") && processoDt.getDependenciaProcesso().equals("")){
					stRetorno += "Escolha a Dependência do Processo. \n";
				}
			}
		}
		
		if (processoDt.isProcessoDependente()) {
			// No caso de ser processo de segundo grau com vínculo
			if (processoDt.getListaPolosAtivos() == null || processoDt.getListaPolosAtivos().size() == 0) stRetorno += "Insira ao menos uma parte recorrente. \n";
			if (processoDt.getListaPolosPassivos() == null || processoDt.getListaPolosPassivos().size() == 0) stRetorno += "Insira ao menos uma parte recorrida. \n";
			if (processoDt.getProcessoDependenteDt() == null && processoDt.getProcessoDependenteDt().getId().equals("")) stRetorno += "Processo de 1º Grau deve ser informado. \n";
		} else {
			if (processoDt.getListaPolosAtivos() == null || processoDt.getListaPolosAtivos().size() == 0) stRetorno += "Insira ao menos uma parte promovente. \n";
			if (processoDt.getListaPolosPassivos() == null || processoDt.getListaPolosPassivos().size() == 0) stRetorno += "Insira ao menos uma parte promovida. \n";
			if (processoDt.getId_AreaDistribuicao().length() == 0 || processoDt.getAreaDistribuicao().length() == 0) stRetorno += "Selecione a Área de Distribuição. \n";
		}
		
		
		if(StringUtils.isNotEmpty(processoDt.getId_ProcessoTipo())){
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(processoDt.getId_ProcessoTipo());
			//Se o processo for do tipo HABEAS CORPUS é preciso que seja cadastrada uma parte do tipo PACIENTE
			if(processoTipoDt.isHabeasCorpus()) {
				//Se processo for HABEAR CORPUS é preciso cadastrar processo dependente.
	//			if(!processoDt.isProcessoDependenteVariavel()){
	//				stRetorno += "A opção PROCESSO DEPENDENTE deve ser marcada. \n";
	//			}else{
					if (processoDt.getListaOutrasPartes() == null || processoDt.getListaOutrasPartes().size() == 0) {
						stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
					} else {
						boolean temPaciente = false;
						for (int i = 0; i < processoDt.getListaOutrasPartes().size(); i++) {
							ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaOutrasPartes().get(i);
							ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarId(parteDt.getId_ProcessoParteTipo());
							if(processoParteTipoDt.isPaciente()) {
								temPaciente = true;
								break;
							}
							
						}
						if(!temPaciente) {
							stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
						}
					}
	//			}
			}
		}
		
		if (processoDt.getId_ProcessoTipo().length() == 0 || processoDt.getProcessoTipo().length() == 0) stRetorno += "Selecione a Classe. \n";
		//BO 2016/22150 - Márcia Perillo - Solicitou que fosse liberado
		//o cadastro de agravo de instrumento, mesmo sem possuir o número do processo pai
		//Quando os processos estiverem todos digitalizados, pode liberar o código.
//		else if (!processoDt.isProcessoDependente()) {
//			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(processoDt.getId_ProcessoTipo());
//			if (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.AGRAVO_INSTRUMENTO_CPC) stRetorno += "Para Classes do tipo \"Agravo de Instrumento\", o processo relacionado deve ser informado. Escolha a opção \"Criar Com Vínculo\". \n";
//		}

		if (processoDt.getListaAssuntos() == null || processoDt.getListaAssuntos().size() == 0) stRetorno += "Insira ao menos um Assunto para o processo. \n";
		if (processoDt.getValor().trim().length() == 0) stRetorno += "Valor da causa deve ser maior que zero. \n";
		
		if (processoDt.getId_ProcessoPrioridade() == null || processoDt.getId_ProcessoPrioridade().trim().length() == 0) stRetorno += "Escolha uma prioridade. \n";
		
		
		//ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoCiveldt.ISENTO));
		if (processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))){
			AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			if (areaDistribuicaoNe.isAreaDistribuicaoComCusta(processoDt.getId_AreaDistribuicao())){
			    PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
				boolean possuiParteIsenta = false;
				if( processoDt.getListaPolosAtivos() != null ) {
					for (Iterator iterator = processoDt.getListaPolosAtivos().iterator(); iterator.hasNext();) {
						ProcessoParteDt processoParteDt = (ProcessoParteDt) iterator.next();
						if (processoParteDt.getCnpj() != null && processoParteDt.getCnpj().length()>0 && partesIsentaNe.isParteIsenta(processoParteDt.getCnpj())){
							possuiParteIsenta = true;
							break;
						}
					}
				}
				
				//Validação da modelo da guia
				boolean classeComCusta;
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, processoDt.getId_ProcessoTipo()) != null ) {
					if (this.isIsentoJuizoConflitoCompetencia(usuarioDt.getGrupoCodigo(), processoDt.getId_ProcessoTipo()) || usuarioDt.isMp()) {//B.O2018/13014
						classeComCusta = false;
					} else {
						classeComCusta = true;
					}
				} else {
					classeComCusta = false;
				}
				
				if (!possuiParteIsenta && classeComCusta){
					stRetorno += "O processo com custa Tipo Isento deve possuir pelo menos uma parte isenta ou uma classe sem custa processual. \n";
				} 
			}
		}
		
//		if (processoDt.getListaPromoventes() != null && processoDt.getListaPromoventes().size()>0){
//		processoDt.getListaPromoventes().removeAll(processoDt.getListaPromoventes());
//		request.getSession().removeAttribute("Enderecodt");
//		request.getSession().removeAttribute("ProcessoPartedt");
//		ProcessoPartedt = new ProcessoParteDt();
//		passoEditar=-1;
//	}
		
		return stRetorno;

	}

	/**
	 * Método responsável em validar os dados de um Processo de Segundo Grau
	 * Cível
	 * 
	 * @param processoDt
	 */
	public String verificarProcessoSegundoGrauCriminal(ProcessoCadastroDt processoDt, UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";
		
		if (!processoDt.isProcessoFisico()) {
			if(processoDt.getGrauProcesso().equals("")  ||  processoDt.getTipoProcesso().equals("") || processoDt.getAssistenciaProcesso().equals("")){
				stRetorno += "Escolha o Grau, Tipo e Assistência do Processo. \n";
			} else {
				if(!processoDt.getAssistenciaProcesso().equals("2") && processoDt.getDependenciaProcesso().equals("")){
					stRetorno += "Escolha a Dependência do Processo. \n";
				}
			}
		}
		if (processoDt.isProcessoDependente()) {
			// No caso de ser processo de segundo grau com vínculo
			if (processoDt.getListaPolosAtivos() == null || processoDt.getListaPolosAtivos().size() == 0) stRetorno += "Insira ao menos uma parte recorrente. \n";
			if (processoDt.getListaPolosPassivos() == null || processoDt.getListaPolosPassivos().size() == 0) stRetorno += "Insira ao menos uma parte recorrida. \n";
			if (processoDt.getProcessoDependenteDt() == null && processoDt.getProcessoDependenteDt().getId().equals("")) stRetorno += "Processo de 1º Grau deve ser informado. \n";
		} else {
			if (processoDt.getListaPolosAtivos() == null || processoDt.getListaPolosAtivos().size() == 0) stRetorno += "Insira ao menos uma parte promovente. \n";
			if (processoDt.getListaPolosPassivos() == null || processoDt.getListaPolosPassivos().size() == 0) stRetorno += "Insira ao menos uma parte promovida. \n";
			if (processoDt.getId_AreaDistribuicao().length() == 0 || processoDt.getAreaDistribuicao().length() == 0) stRetorno += "Selecione a Área de Distribuição. \n";
		}

		ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(processoDt.getId_ProcessoTipo());
		//Se o processo for do tipo HABEAS CORPUS é preciso que seja cadastrada uma parte do tipo PACIENTE
		if(processoTipoDt.isHabeasCorpus()) {
			//Se processo for HABEAR CORPUS é preciso cadastrar processo dependente.
//			if(!processoDt.isProcessoDependenteVariavel()){
//				stRetorno += "A opção PROCESSO DEPENDENTE deve ser marcada. \n";
//			} else{
				if (processoDt.getListaOutrasPartes() == null || processoDt.getListaOutrasPartes().size() == 0) {
					stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
				} else {
					boolean temPaciente = false;
					for (int i = 0; i < processoDt.getListaOutrasPartes().size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaOutrasPartes().get(i);
						ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarId(parteDt.getId_ProcessoParteTipo());
						if(processoParteTipoDt != null && processoParteTipoDt.isPaciente()) {
							temPaciente = true;
							break;
						}
						
					}
					if(!temPaciente) {
						stRetorno += "Insira ao menos uma OUTRA PARTE do tipo PACIENTE. \n";
					}
				}
//			}
		}
		
		if (processoDt.getId_ProcessoTipo().length() == 0 || processoDt.getProcessoTipo().length() == 0) stRetorno += "Selecione a Classe. \n";
		else if (!processoDt.isProcessoDependente()) {
			if (Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.AGRAVO_INSTRUMENTO_CPC) stRetorno += "Para Classes do tipo \"Agravo de Instrumento\", o processo relacionado deve ser informado. Escolha a opção \"Criar Com Vínculo\". \n";
		}
		if (processoDt.getListaAssuntos() == null || processoDt.getListaAssuntos().size() == 0) stRetorno += "Insira ao menos um Assunto para o processo. \n";
		
		if (processoDt.getId_ProcessoPrioridade() == null || processoDt.getId_ProcessoPrioridade().trim().length() == 0) stRetorno += "Escolha uma prioridade. \n";
		
		if (processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase(String.valueOf(ProcessoDt.ISENTO))){
			AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			if (areaDistribuicaoNe.isAreaDistribuicaoComCusta(processoDt.getId_AreaDistribuicao())){
				PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
				boolean possuiParteIsenta = false;
				for (Iterator iterator = processoDt.getListaPolosAtivos().iterator(); iterator.hasNext();) {
					ProcessoParteDt processoParteDt = (ProcessoParteDt) iterator.next();
					if (processoParteDt.getCnpj() != null && processoParteDt.getCnpj().length()>0 && partesIsentaNe.isParteIsenta(processoParteDt.getCnpj())){
						possuiParteIsenta = true;
						break;
					}
				}
				
				//Validação da modelo da guia
				boolean classeComCusta;
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				if( guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, processoDt.getId_ProcessoTipo()) != null ) {
					if (this.isIsentoJuizoConflitoCompetencia(usuarioDt.getGrupoCodigo(), processoDt.getId_ProcessoTipo()) || usuarioDt.isMp()) {//B.O2018/13014
						classeComCusta = false;
					} else {
						classeComCusta = true;
					}
				} else {
					classeComCusta = false;
				}
				
				if (!possuiParteIsenta && classeComCusta){
					stRetorno += "O processo com custa Tipo Isento deve possuir pelo menos uma parte isenta ou uma classe sem custa processual. \n";
				} 
			}
		}
		
		return stRetorno;

	}

	/**
	 * Método que realiza o cadastro de um processo Cível
	 * 
	 * @param processoCiveldt
	 *            , dt ProcessoCivel com dados do processo a ser cadastrado
	 * @param usuarioDt
	 *            , usuário que está efetuando o cadasttro
	 * 
	 * @author msapaula
	 */
	public void cadastrarProcessoCivel(ProcessoCadastroDt processoCivelDt, UsuarioDt usuarioDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		String id_Serventia = null;
		boolean boAtermacao = false;
		String[] id_ServentiaPrevencaoDataArquivamento = null;
		String id_ServentiaCargo = null;
		String codigoServentiaParaRollback = null;
		String numrProcessoParaRollback = null;
		String idUsuarioLogRollback = null;
		String idComputadorLogRollback = null;
		String codigoComarcaCodigoParaRollback = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		obFabricaConexao.iniciarTransacao();
		
		try {
			String complemento = "";

			LogDt logDt = new LogDt(processoCivelDt.getId_UsuarioLog(), processoCivelDt.getIpComputadorLog());
			// Se usuário não é advogado, trata-se de atermação
			if (!usuarioDt.isAdvogado()) {
				boAtermacao = true;
			}
			
			
			//Se ele preenche o campo de processo 100%Digital o Email e o Telefone do Polo Ativo devem estar preenchidos
			if(processoCivelDt.is100Digital()) {
				
				for (Iterator iterator = processoCivelDt.getListaPolosAtivos().iterator(); iterator.hasNext();) {
					ProcessoParteDt procParteDt = (ProcessoParteDt) iterator.next();
					if (!procParteDt.isTemEmail()){
						throw new MensagemException("Para processos 100% Digitais o Email dos Polos ATIVOS devem estar preenchidos.");
					}
					if (!procParteDt.isTemTelefone()){
						throw new MensagemException("Para processos 100% Digitais o Telefone dos Polos ATIVOS devem estar preenchidos.");
					}
				}
			}
			
			// Distribuiçao processo cível. Se processo é dependente, a serventia será a mesma do processo principal.
			if (processoCivelDt.isProcessoDependente()) {
				
				//Validações que impedem o cadastro de processo de 1º grau. Estão ligadas diretamente ao fato de 
				//o processo pai ser um processo originário do 2º grau.
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoCivelDt.getId_ServentiaProcessoDependente());
				if(serventiaDt.isSegundoGrau() || serventiaDt.isTurma()){
					//Consulta o primeiro recurso ativo do processo pai para posterior verificação.
					String idRecursoPai = new RecursoNe().consultarRecursoAtivoMaisAntigo(processoCivelDt.getProcessoDependenteDt().getId());

					//CENÁRIO DE BLOQUEIO DE PROTOCOLO 1:
					//Se o processo pai for um processo que está no 2º grau e possuir recurso ativo. Precisa consultar a serventia de ORIGEM do recurso e ela não pode
					//ser uma serventia de 2º grau. Se for uma serventia de 2º grau, quer dizer que o processo pai é um processo originário de 2º grau. Logo, o processo filho 
					//não poderá ser distribuído no 1º grau. O protocolo deve ser feito diretamente como processo de 2º grau.					
					if(idRecursoPai != null && !idRecursoPai.equalsIgnoreCase("")){
						RecursoDt recursoProcessoPaiDt = new RecursoNe().consultarIdCompleto(idRecursoPai);
						serventiaDt = new ServentiaNe().consultarId(recursoProcessoPaiDt.getId_ServentiaOrigem());
						if(serventiaDt.isSegundoGrau() || serventiaDt.isTurma()){
							throw new MensagemException("Não é possível cadastrar processo de 1º Grau quando o processo dependente é um processo originário de 2º grau.");
						}
						id_Serventia = serventiaDt.getId();
					} else {
						//CENÁRIO DE BLOQUEIO DE PROTOCOLO 2:
						//Se o processo pai for um processo originário do 2º grau e não possuir recurso ativo, o processo filho não poderá 
						//ser distribuído no 1º grau. Neste caso, o protocolo deve ser feito diretamente como processo de 2º grau.
						throw new MensagemException("Não é possível cadastrar processo de 1º Grau quando o processo dependente é um processo originário de 2º grau.");
					}
				} else {
					//Se a serventia do processo pai for uma serventia de 1º grau, distribui o novo processo para a mesma serventia.
					id_Serventia = processoCivelDt.getId_ServentiaProcessoDependente();
				}
				
				complemento = " (Dependente)";
				processoCivelDt.setId_ProcessoPrincipal(processoCivelDt.getProcessoDependenteDt().getId());
				//é preciso setar no processoCivelDt o id_area_dist do processo pai ou do recurso mais novo do processo pai (quando este estiver em instâncias superiores)
				RecursoNe recursoNe = new RecursoNe();
				String idRecursoMaisAntigo = recursoNe.consultarRecursoAtivoMaisAntigo(processoCivelDt.getProcessoDependenteDt().getId());
				if (idRecursoMaisAntigo != null && !idRecursoMaisAntigo.equalsIgnoreCase("")){
					//é preciso setar no processoCivelDt o id_area_dist do recurso mais novo do processo pai
					RecursoDt recursoDt = recursoNe.consultarId(idRecursoMaisAntigo);
					processoCivelDt.setId_AreaDistribuicao(recursoDt.getId_AreaDistribuicaoOrigem());
				} else {
					//é preciso setar no processoCivelDt o id_area_dist do processo pai
					processoCivelDt.setId_AreaDistribuicao(processoCivelDt.getProcessoDependenteDt().getId_AreaDistribuicao());
				}
				
				ServentiaCargoDt serventiaCargoProcessoPrincipalDt = null;
				// Verifica se o processo principal tem juiz responsável 
				if (serventiaDt.isUPJs()){
					serventiaCargoProcessoPrincipalDt = responsavelNe.consultarProcessoResponsavel2Grau(processoCivelDt.getId_ProcessoPrincipal(), CargoTipoDt.JUIZ_UPJ, true, obFabricaConexao); 
				} else{
					serventiaCargoProcessoPrincipalDt = ((ServentiaCargoDt)responsavelNe.consultarServentiaCargoResponsavelProcessoAtivo(processoCivelDt.getId_ProcessoPrincipal(), id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), obFabricaConexao));
				}
				
				if(serventiaCargoProcessoPrincipalDt == null){
					serventiaCargoProcessoPrincipalDt = ((ServentiaCargoDt)responsavelNe.consultarServentiaCargoResponsavelProcessoInativo(processoCivelDt.getId_ProcessoPrincipal(), id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), obFabricaConexao));
				}
				
				if (serventiaCargoProcessoPrincipalDt != null) {
					id_ServentiaCargo = serventiaCargoProcessoPrincipalDt.getId();
				} else {
					ProcessoDt processoPrincipalDt = consultarId(processoCivelDt.getId_ProcessoPrincipal(), obFabricaConexao);
					throw new MensagemException("Não foi possível encontrar o Magistrado atual do processo " + processoPrincipalDt.getProcessoNumero() +  ", favor entrar em contato com o suporte.");
				}	
				
			} else if (processoCivelDt.isProcessoFisico()) {
				id_Serventia = processoCivelDt.getId_Serventia();
				id_ServentiaCargo = processoCivelDt.getId_ServentiaCargo();
			} else {
				//alteração para cumprir o provimento de 16/2012 CGJ
				//se não houver dependência, a distribuição segue as regras do ponteiro de distribuição
				id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoCivelDt.getId_AreaDistribuicao());
				
				//distribuindo entre os juízes responsáveis da serventia e área de distribuição informados
				id_ServentiaCargo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia,processoCivelDt.getId_AreaDistribuicao());
			}
			
			//Caso não localize serventia ativa na área de distribuição informada, interrompe-se o cadastro do processo e retorna erro
			if (id_Serventia == null || id_Serventia.length() == 0) {
				throw new MensagemException("Não foi possível localizar uma serventia ativa para cadastrar este processo. Entre em contato com o suporte.");
			}
			// Consulta dados da serventia
			processoCivelDt.setServentiaDt(new ServentiaNe().consultarId(id_Serventia));
			
			// Consulta dados do processo prioridade
			if( processoCivelDt.getId_ProcessoPrioridade() != null ) {
				ProcessoPrioridadeDt processoPrioridadeDt = new ProcessoPrioridadeNe().consultarId(processoCivelDt.getId_ProcessoPrioridade());
				if( processoPrioridadeDt != null && !processoPrioridadeDt.getProcessoPrioridadeCodigo().isEmpty() ) {
					processoCivelDt.setProcessoPrioridadeCodigo(processoPrioridadeDt.getProcessoPrioridadeCodigo());
				}
			}
			
			//Se o processo for protocolado nos juizados de infância civel, deve ser colocado segredo de justiça obrigatoriamente
			if(processoCivelDt.getServentiaDt().isJuizadoInfanciaJuventudeCivel()) {
				processoCivelDt.setSegredoJustica("true");
			}
		
			//o plantao deve funcionar somente após as 19h ou em dia não útil
			if (processoCivelDt.getServentiaDt().isPlantaoPrimeiroGrau() || processoCivelDt.getServentiaDt().isPlantaoAudienciaCustodia()){								
				PrazoSuspensoNe ps = new PrazoSuspensoNe();
				Calendar dataAtual = Calendar.getInstance();
				if (dataAtual.get(Calendar.HOUR_OF_DAY)>12 && dataAtual.get(Calendar.HOUR_OF_DAY)<19  && dataAtual.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && dataAtual.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
					
					//verifico se é uma data valida na comarca, na cidade e serventia
					//o metodo isDataValidaProtocolo foi cridado devido o bo 2018/53
					//passo a hora e os minutos  para 0 hora/minutos do dia
					
					//Atualizacao 07/01/2021 - Turmo único das 12 as 19 horas
					dataAtual.set(Calendar.HOUR_OF_DAY,0);
					dataAtual.set(Calendar.MINUTE,0);
					dataAtual.set(Calendar.SECOND,0);
					dataAtual.set(Calendar.MILLISECOND, 0);
					boolean boData = ps.isDataValidaProtocolo(dataAtual.getTime(), processoCivelDt.getServentiaDt());
					if (boData){
						throw new MensagemException("O plantão deve ser utilizado entre as 19h e 12h ou em dia não útil para o Poder Judiciário.");
					}					
				}			
			}
			// Se é cadastro com dependência o fórum será o mesmo do processo principal
			if (processoCivelDt.isProcessoDependente()) {
				processoCivelDt.setForumCodigo(processoCivelDt.getProcessoDependenteDt().getForumCodigo());
			}

			if (processoCivelDt.isProcessoFisico()) {
				// Atualiza o número do processo de acordo com o informado pelo usuário.
				processoCivelDt.atualizeNumeroProcessoFisico();
			} else {
				//Valida se já existe um processo com o número gerado
				do{
					//Captura próxima numeração de processo, baseado no código do fórum.
					String numero = ProcessoNumero.getInstance().getProcessoNumero(processoCivelDt.getForumCodigo());
					processoCivelDt.setAnoProcessoNumero(numero);
				}while (validaExistenciaProcessoNumeroCompleto(processoCivelDt, obFabricaConexao));									
			}
			
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(processoCivelDt.getId_ProcessoTipo());

			// Seta fase do processo
			if (processoTipoNe.AcaoExecucao(processoTipoDt.getProcessoTipoCodigo())){
				processoCivelDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.EXECUCAO));
			}else{
				processoCivelDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
			}

			// Seta status como ativo ou sigiloso caso tenha sido essa a opção escolhida no cadastro do processo
			if(processoCivelDt.getSigiloso() != null && processoCivelDt.getSigiloso().equals("true")){
				processoCivelDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.SIGILOSO));
			}else{
				processoCivelDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
			}
			
			//Antes de salvar o processo é preciso consultar se haverá conexão com algum outro processo.
			//Se fizer essa verificação depois de salvar o processo, a verificação encontrará o processo que está sendo cadastrado.
			id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoCivelDt, obFabricaConexao);
			
			//Se for um processo cadastrado com uso de guia
			if(guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().equalsIgnoreCase("")){
				if (guiaEmissaoDt.isGuiaEmitidaSPG()) {
					processoCivelDt.setTabelaOrigemTemp("GUIAINICIALSPG");
				} else {
					processoCivelDt.setTabelaOrigemTemp("GUIAINICIALPROJUDI");
				}
				processoCivelDt.setCodigoTemp(guiaEmissaoDt.getId());
			}
			
			//validação de cadastro com guia
			validacaoAntiFraudeDadosGuia(processoCivelDt, guiaEmissaoDt, obFabricaConexao);
			
			// Salva processo
			salvar(processoCivelDt, obFabricaConexao);
			
			// Salvando partes do processo
			new ProcessoParteNe().salvarPartesProcesso(processoCivelDt.getPartesProcesso(), processoCivelDt.getId(), logDt, obFabricaConexao);

			// Salvando advogados incluídos
			new ProcessoParteAdvogadoNe().salvarAdvogadosPartesPromoventes(processoCivelDt.getListaPolosAtivos(), processoCivelDt.getListaAdvogados(), logDt, obFabricaConexao);

			// Salvando assuntos do processo
			processoAssuntoNe.salvarProcessoAssunto(processoCivelDt.getListaAssuntos(), processoCivelDt.getId(), logDt, obFabricaConexao);

			//Salva o juiz como responsável
			if(id_ServentiaCargo != null && id_ServentiaCargo.length() > 0) {
				if (processoCivelDt.getServentiaDt().isUPJs()) {
					responsavelNe.salvarProcessoResponsavel(processoCivelDt.getId(), id_ServentiaCargo, true, CargoTipoDt.JUIZ_UPJ, logDt, obFabricaConexao);					
				}else {
					responsavelNe.salvarProcessoResponsavel(processoCivelDt.getId(), id_ServentiaCargo, true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
				}
			} else {
				throw new MensagemException("Não foi possível localizar um relator ativo vinculado à serventia " + processoCivelDt.getServentiaDt().getServentia() + " para vincular a este processo. Entre em contato com o suporte.");
			}
			
			/* ---------- PONTEIRO ----------------*/
			//Recadastramento de processo físico não deve constar no ponteiro
			if(!processoCivelDt.isProcessoFisico()){
				///salvo o ponteiro
				new PonteiroLogNe().salvar(new PonteiroLogDt(processoCivelDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.DISTRIBUICAO, processoCivelDt.getId(), processoCivelDt.getServentiaDt().getId(), UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi, new Date(), id_ServentiaCargo), obFabricaConexao);
			}
			/* ---------- PONTEIRO ----------------*/
			
			// Se usuário que está cadastrando é um promotor adiciona ele como responsável pelo processo
			if (usuarioDt.isMp()) {
				responsavelNe.salvarProcessoResponsavel(processoCivelDt.getId(), usuarioDt.getId_ServentiaCargo(), true, CargoTipoDt.MINISTERIO_PUBLICO, logDt, obFabricaConexao);
			}

			// Gera movimentação PETIÇÃO ENVIADA
			MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacao(processoCivelDt.getId(), MovimentacaoTipoDt.PETICAO_ENVIADA, usuarioDt.getId_UsuarioServentia(), "", logDt, obFabricaConexao);

			// Salva Arquivos já com recibo
			MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();
			if(processoCivelDt.getListaArquivos() != null & !processoCivelDt.getListaArquivos().isEmpty()) {
				arquivoNe.inserirArquivosSemRecibo(movimentacaoPeticao.getId(), processoCivelDt.getProcessoNumeroCompleto(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
			} else {
				throw new MensagemException("Processo não possui documentos anexados para peticionamento. Favor rever o Passo 2 do cadastro do processo.");
			}

			// Gera movimentação PROCESSO DISTRIBUÍDO
			if (complemento.length() == 0){
				complemento = " (Normal)";
				if (processoCivelDt.isProcessoFisico()) {
					complemento = " (Sem Regra de Redistribuição - Processo Físico)";
				}
			}
			ServentiaCargoDt responsavel = new ServentiaCargoNe().consultarId(id_ServentiaCargo);
			complemento += " - Distribuído para: " + responsavel.getNomeUsuario();
			movimentacaoNe.gerarMovimentacaoProcessoDistribuido(processoCivelDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getServentiaDt().getServentia() + complemento, logDt, obFabricaConexao);

			String id_ServentiaCargoConclusao = id_ServentiaCargo;			
			ServentiaCargoDt serventiaCargoResponsavel = null;
			boolean boGerarHistorico = false;
			
			//Se for UPJ deve seguir as regras abaixo para encontrar o responsável pela conclusão no gabinete
			if(processoCivelDt.getServentiaDt().isUPJs()) {					
				if(processoCivelDt.isSigiloso()) {
					serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);		
					boGerarHistorico = true;
				}else {				
					//ServentiaCargoDt distribuidor = new ServentiaCargoNe().getDistribuidorGabinete(responsavel.getId_Serventia(), obFabricaConexao);
					serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.DISTRIBUIDOR_GABINETE, obFabricaConexao);
					if (serventiaCargoResponsavel==null) {
						serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.ASSISTENTE_GABINETE_FLUXO, obFabricaConexao);
						if (serventiaCargoResponsavel==null) {
							serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);							
						} 
						boGerarHistorico = true;
					}		
				}
				if (serventiaCargoResponsavel==null) {
					throw new MensagemException("Não foi possível determinar o Responsável no gabinete com fluxo.");								
				}				
				id_ServentiaCargoConclusao = serventiaCargoResponsavel.getId();
			}
			//se for sigiloso deve ir concluso direto
			//não deve gerar pendencias de verificar processo e nem marcar audiencia
			if (processoCivelDt.isSigiloso()){
				pendenciaNe.gerarConclusaoDecisao(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
				//no cadastro de processo pre-processual e no plantao de segundo grau
				//não deve-se marcar audiencia e nem mandar concluso
			}else if (!usuarioDt.isAnalistaPreProcessual() &&  !processoCivelDt.getServentiaDt().isPlantaoPrimeiroGrau() ) {
								
				// Caso seja Homologação de Acordo ou Ação Execução, gera Pendência (Concluso ao juiz)
				// não se marca audiencia para ações execução/homologação e monitoria, mas manda concluso
				if (processoTipoDt.isHomologacaoAcordo()  || processoTipoNe.AcaoExecucaoOuMonitoria(processoTipoDt.getProcessoTipoCodigo()) ) {
					if (processoCivelDt.isAssistencia() && processoCivelDt.getServentiaDt().isComCustas()){
						pendenciaNe.gerarConclusaoDecisaoPedidoAssistencia(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					} else {
						pendenciaNe.gerarConclusaoDecisao(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					}					
					//Se deve marcar audiência, essa é agendada, a serventia deve ter uma audicia tipo configurada
				
				} else if (processoCivelDt.MarcarAudiencia() && processoCivelDt.getServentiaDt().temAudienciaTipo()) {
					// Tenta Agendar audiência automaticamente
					processoCivelDt.setAudienciaDt(audienciaNe.agendarAudienciaCadastroProcessoCivel(processoCivelDt, processoCivelDt.getServentiaDt(), usuarioDt, boAtermacao, obFabricaConexao));
	
					if (processoCivelDt.getAudienciaDt() == null) {
						// Se não conseguiu marcar audiência gera Pendência do tipo "Marcar Audiência"
						//Propositalmente foi usado outra instância de pendenciaNe para diferenciar dos outros pendenciaNe que geram conclusão
						new PendenciaNe().gerarPendenciaMarcarAudienciaProcesso(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getServentiaDt().getId(), movimentacaoPeticao.getId(), processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					}
					// Se possui pedido de urgência gera pendência (Concluso ao juiz)
					if (processoCivelDt.isPrioridade() && processoCivelDt.isAssistencia()  && processoCivelDt.getServentiaDt().isComCustas()) {
						pendenciaNe.gerarConclusaoDecisaoPedidoAssistencia(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					} else if (processoCivelDt.isPrioridade()) {
						pendenciaNe.gerarConclusaoDecisao(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);						
					}
					
					// Se não deve marcar audiência gera pendência Concluso ao juiz
					// quando a serventia está com essa configuração
					// ou o processo tem pedido de prioridade
				}else if(!isCartaPrecatoria(processoCivelDt.getId_ProcessoTipo()) && (processoCivelDt.isPrioridade() || processoCivelDt.MandarConcluso() || processoCivelDt.getServentiaDt().isConclusoDireto())){
					
					if (processoCivelDt.isAssistencia() && processoCivelDt.getServentiaDt().isComCustas()){
						pendenciaNe.gerarConclusaoDecisaoPedidoAssistencia(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					} else {
						pendenciaNe.gerarConclusaoDecisao(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCivelDt.getProcessoPrioridadeCodigo(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					}

				} 
				if (!processoCivelDt.getServentiaDt().isComCustas() || !processoCivelDt.isAssistencia()){
					//Gera pendência de novo processo
					//Propositalmente foi usado outra instância de pendenciaNe para diferenciar dos outros pendenciaNe que geram conclusão
					new PendenciaNe().gerarPendenciaVerificarNovoProcesso(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getServentiaDt().getId(), movimentacaoPeticao.getId(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					// Gera pendência Verificar prevenção
					if ( id_ServentiaPrevencaoDataArquivamento != null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
						//Propositalmente foi usado outra instância de pendenciaNe para diferenciar dos outros pendenciaNe que geram conclusão
						new PendenciaNe().gerarPendenciaVerificarConexao(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getId_Serventia(), movimentacaoPeticao.getId(), logDt, obFabricaConexao);
					}
				}
			}

			//se for para upj e a conclusão não for para o distribuidor tem que gerar o historioc
			if(boGerarHistorico && pendenciaNe.isGerouConclusao()) {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
		        String data_inicio = df.format(new Date());
				new PendenciaResponsavelHistoricoNe().salvarHistorioco(pendenciaNe.getIdPendencia(), data_inicio, serventiaCargoResponsavel.getId(), serventiaCargoResponsavel.getId_ServentiaGrupo(), logDt.getId_Usuario(), logDt.getIpComputador(), obFabricaConexao);
			}
			
			//Se for um processo cadastrado com uso de guia
			if(guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().equalsIgnoreCase("")){
				if( processoCivelDt.getProcessoNumero() != null ) {
					guiaEmissaoDt.setNumeroProcesso(processoCivelDt.getProcessoNumeroCompleto());
				}
				
				/** ***Rollback*** **/
				codigoServentiaParaRollback = Funcoes.completarZeros(processoCivelDt.getServentiaDt().getServentiaCodigoExterno(),3);
				codigoComarcaCodigoParaRollback = processoCivelDt.getServentiaDt().getComarcaCodigo();
				numrProcessoParaRollback = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
				
				idUsuarioLogRollback = processoCivelDt.getId_UsuarioLog();
				idComputadorLogRollback = processoCivelDt.getIpComputadorLog();
				/** ****** **/
				
				this.vinculaGuiaProcesso(guiaEmissaoDt, processoCivelDt.getId_Processo(), null, processoCivelDt.getServentiaDt().getId(), processoCivelDt.getId_UsuarioLog(), processoCivelDt.getIpComputadorLog(), obFabricaConexao, null);
				
				if (!guiaEmissaoNe.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao)) {
					pendenciaNe.gerarPendenciaVerificarGuiaPendente(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getServentiaDt().getId(), movimentacaoPeticao.getId(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
				}
				
			} else if(isProcessoExecucaoFiscal(processoCivelDt)) {
				// Se for processo de execução fiscal
				efetueGeracaoGuiaUnicaFazendaPublica(processoCivelDt, obFabricaConexao);
			} else if (processoCivelDt.getServentiaDt().isComCustas() && processoCivelDt.isAssistencia()){
				//Salvar GuiaEmissão
				this.inicializarGuiaInicial(guiaEmissaoDt, processoCivelDt);
				this.gerarGuiaInicialProcesso(guiaEmissaoDt, processoCivelDt, usuarioDt);
								
				guiaEmissaoNe.salvarGuiaCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, usuarioDt.getId_UsuarioServentia(), obFabricaConexao);
				
				if(guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().equalsIgnoreCase("")){
					if (!guiaEmissaoNe.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) && !processoCivelDt.isAssistencia()) {
						pendenciaNe.gerarPendenciaVerificarGuiaPendente(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getServentiaDt().getId(), movimentacaoPeticao.getId(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
					} else if (!guiaEmissaoNe.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) && processoCivelDt.isAssistencia()) {
						//Gera pendência de novo processo
						pendenciaNe.gerarPendenciaVerificarNovoProcessoPedidoAssistencia(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getServentiaDt().getId(), movimentacaoPeticao.getId(), processoCivelDt.getListaArquivos(), logDt, obFabricaConexao);
						// Gera pendência Verificar prevenção
						if ( id_ServentiaPrevencaoDataArquivamento != null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
							pendenciaNe.gerarPendenciaVerificarConexao(processoCivelDt, UsuarioServentiaDt.SistemaProjudi, processoCivelDt.getId_Serventia(), movimentacaoPeticao.getId(), logDt, obFabricaConexao);
						}
					}
				}
			} else if (processoCivelDt.getServentiaDt().isComCustas() && processoCivelDt.isIsento()){
				//Salvar GuiaEmissão
				this.inicializarGuiaInicial(guiaEmissaoDt, processoCivelDt);
				if (this.gerarGuiaInicialProcesso(guiaEmissaoDt, processoCivelDt, usuarioDt)) {
					guiaEmissaoNe.salvarGuiaCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, usuarioDt.getId_UsuarioServentia(), obFabricaConexao);
				}
			}
			
			
			if (processoCivelDt.isProcessoFisico()) {
				// Tenta atualizar as audiências do DRS, caso existam...
				try 
				{
					(new AudienciaPublicadaNe()).executeProcessamentoAudienciaPublicadaImportacao(processoCivelDt.getId(), processoCivelDt.getProcessoNumeroCompleto(), usuarioDt, logDt, obFabricaConexao);
				}  catch (Exception e) {}				
			} 
			
			obFabricaConexao.finalizarTransacao();
			
			if (processoCivelDt.getServentiaDt().isPlantaoPrimeiroGrau() || processoCivelDt.getServentiaDt().isPlantaoSegundoGrau()){				
				enviarEmailAnalistas(processoCivelDt.getServentiaDt(),processoCivelDt.getProcessoNumeroCompleto());
			}
			
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarCadastroProcesso(processoCivelDt);
			
			//Método para fazer o tratamento de rollback
			//Se for um processo cadastrado com uso de guia
			if(guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().equalsIgnoreCase("")){
				guiaEmissaoNe.rollbackAlteracoesGuiaSPGCadastroProcesso(guiaEmissaoDt, codigoServentiaParaRollback, numrProcessoParaRollback, codigoComarcaCodigoParaRollback, idUsuarioLogRollback, idComputadorLogRollback);
			}
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	
	 private void enviarEmailAnalistas(ServentiaDt serventia, String numeroProcesso) {
												
			String mensagemEmail = "<table width='98%' border='0' cellspacing='2' cellpadding='2'>";
			mensagemEmail += "  <tr><td colspan='2'>Um novo processo foi recebido.</td> </tr>";
			mensagemEmail += "  <tr> <td width='80'>Número:</td> <td><b> "+ numeroProcesso + "</b> </td> </tr> ";
			mensagemEmail += "  <tr> <td>Serventia: </td> <td> <b> "+ serventia.getServentia() + "</b></td> </tr>";
			mensagemEmail += "  <tr> <td>Data:</td> <td><b> "+ Funcoes.FormatarDataHora(new Date()) + "</b></td> </tr>";
			mensagemEmail += "</table>";
			mensagemEmail += "<br />Por favor acesse o Sistema de Processo Eletrônico e verifique o novo processo. <br/>";   
			
			new GerenciadorEmail(serventia.getServentia(), serventia.getEmail(), "Novo Processo Recebido", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			
	}

	/**
	 * Efetua cadastro de um processo criminal
	 * 
	 * @param processoCriminaldt
	 *            , dt com os dados do processo
	 * @param usuarioDt
	 *            , usuario que está cadastrando o processo
	 * 
	 * @author msapaula
	 */
	public void cadastrarProcessoCriminal(ProcessoCadastroDt processoCriminalDt, UsuarioDt usuarioDt) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		String id_Serventia = null;
		String[] id_ServentiaPrevencaoDataArquivamento = null;
		String id_ServentiaCargo = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(processoCriminalDt.getId_UsuarioLog(), processoCriminalDt.getIpComputadorLog());
			
			//Em determinação ao BO 2013/82139 aberto pela Corregedoria em cumprimento ao despacho 3227/2013.
			//if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO)
			//	valideQuantidadeAcoesCadastroProcessoAdvogado(processoCriminalDt, usuarioDt, obFabricaConexao);

			// Distribuiçao processo cível. Se processo é dependente, a
			// serventia será a mesma do processo principal.
			if (processoCriminalDt.isProcessoDependente()) {
				id_Serventia = processoCriminalDt.getProcessoDependenteDt().getId_Serventia();
				//é preciso setar no processoCivelDt o id_area_dist do processo pai ou do recurso mais novo do processo pai (quando este
				//estiver em estâncias superiores)
				RecursoNe recursoNe = new RecursoNe();
				String idRecursoMaisAntigo = recursoNe.consultarRecursoAtivoMaisAntigo(processoCriminalDt.getProcessoDependenteDt().getId());
				if (idRecursoMaisAntigo != null && !idRecursoMaisAntigo.equalsIgnoreCase("")){
					//é preciso setar no processoCivelDt o id_area_dist do recurso mais novo do processo pai
					RecursoDt recursoDt = recursoNe.consultarId(idRecursoMaisAntigo);
					processoCriminalDt.setId_AreaDistribuicao(recursoDt.getId_AreaDistribuicaoOrigem());
				} else {
					//é preciso setar no processoCivelDt o id_area_dist do processo pai
					processoCriminalDt.setId_AreaDistribuicao(processoCriminalDt.getProcessoDependenteDt().getId_AreaDistribuicao());
				}
			} else if (processoCriminalDt.isProcessoFisico()) {
				id_Serventia = processoCriminalDt.getId_Serventia();
			} else {
				// Resgata possíveis preventos e consulta a serventia que deverá receber o processo
				//ALTERAÇÃO leandro**************************************************************************
				//para cumpri o provimento 16/2012 da CGJ
				//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoCriminalDt.getId_AreaDistribuicao(), processoCriminalDt.getId_ProcessoTipo());
				id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoCriminalDt.getId_AreaDistribuicao());
				// Consulta dados da serventia
				ServentiaDt serventiaProcesso = new ServentiaNe().consultarId(id_Serventia);
				
				if (serventiaProcesso.isSegundoGrau()){
					id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoCriminalDt, obFabricaConexao);

					if (id_ServentiaPrevencaoDataArquivamento!=null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
						id_Serventia = id_ServentiaPrevencaoDataArquivamento[0];
					}else{
						//para cumpri o provimento 16/2012 da CGJ
						//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoCriminalDt.getId_AreaDistribuicao(), processoCriminalDt.getId_ProcessoTipo());
						id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoCriminalDt.getId_AreaDistribuicao());
					}
				} else {
					id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoCriminalDt, obFabricaConexao);
				}
				
//				id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoCriminalDt, obFabricaConexao);
//
//				if (id_ServentiaPrevencaoDataArquivamento != null && id_ServentiaPrevencaoDataArquivamento[0]!=null) 
//					id_Serventia = id_ServentiaPrevencaoDataArquivamento[0];
//				else
//					id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoCriminalDt.getId_AreaDistribuicao(), processoCriminalDt.getId_ProcessoTipo());

				//ALTERAÇÃO leandro**********************************************************************************************
			}
			//processoCriminalDt.setId_Serventia(id_Serventia);

			// Consulta dados da serventia
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(id_Serventia);
			if(serventiaDt==null) {
				throw new MensagemException("Não foi possível defiir a Serventia");
			}
			//o set id_serv sera feito quanto atribuir no o serventiaDt no processo criminal
			processoCriminalDt.setServentiaDt(serventiaDt);
			
			//Se o processo for protocolado nos juizados de infância infracional, deve ser colocado segredo de justiça obrigatoriamente
			if(serventiaDt.isJuizadoInfanciaJuventudeInfracional()) {
				processoCriminalDt.setSegredoJustica("true");
			}

			//o plantao deve funcionar somente após as 19h ou em dia não útil
			if (serventiaDt.isPlantaoPrimeiroGrau() || serventiaDt.isPlantaoSegundoGrau() || serventiaDt.isPlantaoAudienciaCustodia()){
				PrazoSuspensoNe ps = new PrazoSuspensoNe();
				Calendar dataAtual = Calendar.getInstance();
				if (dataAtual.get(Calendar.HOUR_OF_DAY)>12 && dataAtual.get(Calendar.HOUR_OF_DAY)<19 && dataAtual.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && dataAtual.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
					
					//verifico se é uma data valida na comarca, na cidade e serventia
					//o metodo isDataValidaProtocolo foi cridado devido o bo 2018/53
					//passo a hora e os minutos  para 0 hora/minutos do dia
					
					//Atualizacao 07/01/2021 - Turmo único das 12 as 19 horas
					dataAtual.set(Calendar.HOUR_OF_DAY,0);
					dataAtual.set(Calendar.MINUTE,0);
					dataAtual.set(Calendar.SECOND,0);
					dataAtual.set(Calendar.MILLISECOND, 0);
					boolean boData = ps.isDataValidaProtocolo(dataAtual.getTime(), serventiaDt);
					if (boData){
						throw new MensagemException("O plantão deve ser utilizado após as 19h ou em dia não útil para o Poder Judiciário");
					}					
				}			
			}
			
			// Se é cadastro com dependência o fórum será o mesmo do processo
			// principal
			if (processoCriminalDt.isProcessoDependente()) {
				processoCriminalDt.setForumCodigo(processoCriminalDt.getProcessoDependenteDt().getForumCodigo());
			}

			if (processoCriminalDt.isProcessoFisico()) {
				// Atualiza o número do processo de acordo com o informado pelo
				// usuário.
				processoCriminalDt.atualizeNumeroProcessoFisico();
			} else {
				// Captura próxima numeração de processo, baseado no código da
				// comarca
				String numero = ProcessoNumero.getInstance().getProcessoNumero(processoCriminalDt.getForumCodigo());
				processoCriminalDt.setAnoProcessoNumero(numero);
			}
			
			// Valida se já existe um processo com o número gerado / informado
			validaExistenciaProcessoNumeroCompleto(processoCriminalDt, obFabricaConexao);

			// Seta fase do processo
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(processoCriminalDt.getId_ProcessoTipo());
			if (processoTipoNe.AcaoExecucao(processoTipoDt.getProcessoTipoCodigo())) {
				processoCriminalDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.EXECUCAO));
			}else {
				processoCriminalDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
			}

			// Seta status como ativo ou sigiloso caso tenha sido essa a opção escolhida no cadastro do processo
			if(processoCriminalDt.isSigiloso()){
				processoCriminalDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.SIGILOSO));
			} else{
				processoCriminalDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));	
			}		
			
			processoCriminalDt.setAreaCodigo(String.valueOf(AreaDt.CRIMINAL));

			// Salva processo
			salvar(processoCriminalDt, obFabricaConexao);
			
			// Salva dados criminais
			if(processoCriminalDt.getProcessoCriminalDt() != null) {
				ProcessoCriminalNe processoCriminalNe = new ProcessoCriminalNe();
				processoCriminalDt.setId_ProcessoCriminal(processoCriminalDt.getId());
				processoCriminalDt.setIpComputadorLogCriminal(processoCriminalDt.getIpComputadorLog());
				processoCriminalDt.setId_UsuarioLogCriminal(processoCriminalDt.getId_UsuarioLog());
				processoCriminalNe.salvar(processoCriminalDt.getProcessoCriminalDt(), obFabricaConexao);
			}

			// Salva partes do Processo
			new ProcessoParteNe().salvarPartesProcesso(processoCriminalDt.getPartesProcesso(), processoCriminalDt.getId(), logDt, obFabricaConexao);

			// Salvando advogados incluídos
			new ProcessoParteAdvogadoNe().salvarAdvogadosPartesPromoventes(processoCriminalDt.getListaPolosAtivos(), processoCriminalDt.getListaAdvogados(), logDt, obFabricaConexao);

			// Salvando assuntos do processo
			processoAssuntoNe.salvarProcessoAssunto(processoCriminalDt.getListaAssuntos(), processoCriminalDt.getId(), logDt, obFabricaConexao);

			if (processoCriminalDt.isProcessoFisico()) {
				// Obtem o juiz responsável do processo físico
				id_ServentiaCargo = processoCriminalDt.getId_ServentiaCargo();
			} else {
				//para atender o provimento 16/2012
				// Distribui processo para um juiz
				//id_ServentiaCargo = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicao1Grau(id_Serventia, processoCriminalDt.getId_ProcessoTipo());
				id_ServentiaCargo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(processoCriminalDt.getId_Serventia(),processoCriminalDt.getId_AreaDistribuicao());
			}
			if(serventiaDt.isSegundoGrau()) {
				responsavelNe.salvarProcessoResponsavel(processoCriminalDt.getId(), id_ServentiaCargo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
			}else {
				// Verifica se o processo principal tem juiz responsável 
				if (serventiaDt.isUPJs()) {
					if(serventiaDt.isUpjTurmaRecursal()) {
						responsavelNe.salvarProcessoResponsavel(processoCriminalDt.getId(), id_ServentiaCargo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
					} else {
						responsavelNe.salvarProcessoResponsavel(processoCriminalDt.getId(), id_ServentiaCargo, true, CargoTipoDt.JUIZ_UPJ, logDt, obFabricaConexao);
					}
				}else {
					responsavelNe.salvarProcessoResponsavel(processoCriminalDt.getId(), id_ServentiaCargo, true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
				}
			}

			/* ---------- PONTEIRO ----------------*/
			//Recadastramento de processo físico não deve constar no ponteiro
			if(!processoCriminalDt.isProcessoFisico()){
				///salvo o ponteiro
				new PonteiroLogNe().salvar(new PonteiroLogDt(processoCriminalDt.getId_AreaDistribuicao(),PonteiroLogTipoDt.DISTRIBUICAO,processoCriminalDt.getId(),processoCriminalDt.getId_Serventia(),UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi, new Date(), id_ServentiaCargo  ),obFabricaConexao );
			}
			/* ---------- PONTEIRO ----------------*/
			
			// Se usuário que está cadastrando é um promotor adiciona ele como
			// responsável pelo processo
			if (usuarioDt.isMp()) {
				responsavelNe.salvarProcessoResponsavel(processoCriminalDt.getId(), usuarioDt.getId_ServentiaCargo(), true, CargoTipoDt.MINISTERIO_PUBLICO, logDt, obFabricaConexao);
			} else {
				// Caso contrário, já distribue o processo para um promotor responsável Busca promotoria relacionada
				ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
				//codigo antigo
				//ServentiaDt promotoriaRelacionada = serventiaRelacionadaNe.consultarPromotoriaRelacionada(processoCriminalDt.getId_Serventia());
				ServentiaDt promotoriaRelacionada = null;
				
				if (processoCriminalDt.getServentiaDt() != null && processoCriminalDt.getServentiaDt().getServentiaSubtipoCodigo() != null
						&& processoCriminalDt.getServentiaDt().getServentiaSubtipoCodigo().length()>0){
					
					String serventiaSubtipoCodigo = processoCriminalDt.getServentiaDt().getServentiaSubtipoCodigo();
					switch (Funcoes.StringToInt(serventiaSubtipoCodigo)) {
					
					case ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL:
					case ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL:
//					case ServentiaSubtipoDt.PLANTAO:
					case ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA:
					case ServentiaSubtipoDt.UPJ_CRIMINAL:
					case ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA:
					case ServentiaSubtipoDt.UPJ_CUSTODIA:
						promotoriaRelacionada = serventiaRelacionadaNe.consultarServentiaRelacionadaSubTipo(processoCriminalDt.getId_Serventia(), String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU));
				        break;
					
					case ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL:
					case ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL:
					case ServentiaSubtipoDt.UPJ_TURMA_RECURSAL:
						promotoriaRelacionada = serventiaRelacionadaNe.consultarServentiaRelacionadaSubTipo(processoCriminalDt.getId_Serventia(), String.valueOf(ServentiaSubtipoDt.MP_TURMA_JULGADORA));
						break;
					
					case ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU:
					case ServentiaSubtipoDt.CAMARA_CRIMINAL:
					case ServentiaSubtipoDt.SECAO_CRIMINAL:
						promotoriaRelacionada = serventiaRelacionadaNe.consultarServentiaRelacionadaSubTipo(processoCriminalDt.getId_Serventia(), String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU));
						break;

					default:
						break;
					}
					
				}
				
				if (promotoriaRelacionada != null) {
					// Distribue processo para um Promotor responsável
					// (serventia Cargo) e salva como responsável pelo processo
					String id_ServentiaCargoPromotor = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicaoPromotor(promotoriaRelacionada.getId(), processoCriminalDt.getId_ProcessoTipo());
					if (id_ServentiaCargoPromotor != null && id_ServentiaCargoPromotor.length() > 0) {
						responsavelNe.salvarProcessoResponsavel(processoCriminalDt.getId(), id_ServentiaCargoPromotor, true, CargoTipoDt.MINISTERIO_PUBLICO , logDt, obFabricaConexao);
					}
				}
			}

			// Gera movimentação PETIÇÃO ENVIADA ou TCO ENVIADO
			MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacaoPeticaoEnviadaCriminal(processoCriminalDt.getId(), processoCriminalDt.getId_ProcessoTipo(), usuarioDt, logDt, obFabricaConexao);

			// Salva Arquivos já com recibo
			MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();
			if(processoCriminalDt.getListaArquivos() != null & !processoCriminalDt.getListaArquivos().isEmpty()) {
				arquivoNe.inserirArquivosSemRecibo(movimentacaoPeticao.getId(), processoCriminalDt.getProcessoNumeroCompleto(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
			} else {
				throw new MensagemException("Processo não possui documentos anexados para peticionamento. Favor rever o Passo 2 do cadastro do processo.");
			}

			// Gera movimentação PROCESSO DISTRIBUÍDO
			String complemento = " (Normal)";
			
			if (processoCriminalDt.isProcessoFisico()) { 
				complemento = " (Sem Regra de Redistribuição - Processo Físico)";
			}
			
			//ALTERAÇÃO leandro**************************************************************************
			if (serventiaDt.isServentiaTipo2Grau()){
				if (id_ServentiaPrevencaoDataArquivamento!=null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
					if ( id_ServentiaPrevencaoDataArquivamento[1]==null ||  id_ServentiaPrevencaoDataArquivamento[1].length()==0) {
						complemento = " (Conexão)";
					}else { 
						complemento = " (Prevenção)";
					}
				}
			}
			//ALTERAÇÃO leandro**************************************************************************
			
			ServentiaCargoDt responsavel = new ServentiaCargoNe().consultarId(id_ServentiaCargo);
			complemento += " - Distribuído para: " + responsavel.getNomeUsuario();
			movimentacaoNe.gerarMovimentacaoProcessoDistribuido(processoCriminalDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoCriminalDt.getServentia() + complemento, logDt, obFabricaConexao);

			boolean boGerarHistorico=false;	
			String id_ServentiaCargoConclusao = id_ServentiaCargo;			
			ServentiaCargoDt serventiaCargoResponsavel = null;

			if(serventiaDt.isUPJs()) {					
				if(processoCriminalDt.isSigiloso()) {
					serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);	
					boGerarHistorico = true;
				}else {				
					//ServentiaCargoDt distribuidor = new ServentiaCargoNe().getDistribuidorGabinete(responsavel.getId_Serventia(), obFabricaConexao);
					serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.DISTRIBUIDOR_GABINETE, obFabricaConexao);
					if (serventiaCargoResponsavel==null) {
						serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.ASSISTENTE_GABINETE_FLUXO, obFabricaConexao);
						if (serventiaCargoResponsavel==null) {
							serventiaCargoResponsavel = new ServentiaCargoNe().consultarServentiaCargpoDistribuicaoGabineteFluxo(responsavel.getId_Serventia(), CargoTipoDt.JUIZ_UPJ, obFabricaConexao);							
						} 
						boGerarHistorico = true;
					}		
				}
				if (serventiaCargoResponsavel==null) {
					throw new MensagemException("Não foi possível determinar o Responsável no gabinete com fluxo.");								
				}
				
				id_ServentiaCargoConclusao = serventiaCargoResponsavel.getId();
			}
			
			//se for sigiloso deve ir concluso direto
			//não deve gerar pendencias de verificar processo e nem marcar audiencia
			if (processoCriminalDt.isSigiloso()){
				pendenciaNe.gerarConclusaoDecisao(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCriminalDt.getProcessoPrioridadeCodigo(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
				//no cadastro de processo pre-processual e no plantao de segundo grau
				//não deve-se marcar audiencia e nem mandar concluso
			//no cadastro de processo pre-processual e no plantao de segundo grau
			//não deve-se marcar audiencia e nem mandar concluso
			}else if (!serventiaDt.isPlantaoPrimeiroGrau() && !serventiaDt.isPlantaoAudienciaCustodia() ) { 
				// Caso seja Homologação de Acordo ou Ação Execução, gera Pendência
				// (Concluso ao juiz)
				// não se marca audiencia para ações execução/homologação e monitoria, mas manda concluso
				if (processoTipoDt.isHomologacaoAcordo() || processoTipoNe.AcaoExecucaoOuMonitoria(processoTipoDt.getProcessoTipoCodigo())  ) {
					pendenciaNe.gerarConclusaoDecisao(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCriminalDt.getProcessoPrioridadeCodigo(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
					
					//Se deve marcar audiência, essa é agendada, a serventia deve ter uma audicia tipo configurada	
				} else if (processoCriminalDt.MarcarAudiencia() && serventiaDt.temAudienciaTipo()) {
					// Tenta Agendar audiência automaticamente
					if (processoCriminalDt.isMarcarAudienciaConciliacao()){
						processoCriminalDt.setAudienciaDt(audienciaNe.agendarAudienciaPreliminarConciliadorCadastroProcessoCriminal(processoCriminalDt,  usuarioDt, obFabricaConexao));
					}else{
						processoCriminalDt.setAudienciaDt(audienciaNe.agendarAudienciaCadastroProcessoCriminal(processoCriminalDt, processoCriminalDt.getServentiaDt(), usuarioDt, obFabricaConexao));
					}
	
					if (processoCriminalDt.getAudienciaDt() == null) {
						// Se não conseguiu marcar audiência gera Pendência do tipo
						// "Marcar Audiência"
						//Propositalmente foi usado outra instância de pendenciaNe para diferenciar dos outros pendenciaNe que geram conclusão
						new PendenciaNe().gerarPendenciaMarcarAudienciaProcesso(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, processoCriminalDt.getId_Serventia(), movimentacaoPeticao.getId(), processoCriminalDt.getProcessoPrioridadeCodigo(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
					}
					// Se possui pedido de urgência gera pendência (Concluso ao juiz)
					if (processoCriminalDt.isPrioridade()) {					
						pendenciaNe.gerarConclusaoDecisao(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCriminalDt.getProcessoPrioridadeCodigo(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
					}
					// Se não deve marcar audiência gera pendência Concluso ao juiz
				// quando a serventia está com essa configuração
				// ou o processo tem pedido de prioridade
				} else if (!isCartaPrecatoria(processoCriminalDt.getId_ProcessoTipo()) && ( processoCriminalDt.isPrioridade() || processoCriminalDt.MandarConcluso() || serventiaDt.isConclusoDireto() )){
					pendenciaNe.gerarConclusaoDecisao(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoConclusao, processoCriminalDt.getProcessoPrioridadeCodigo(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
				} 

				//Propositalmente foi usado outra instância de pendenciaNe para diferenciar dos outros pendenciaNe que geram conclusão
				new PendenciaNe().gerarPendenciaVerificarNovoProcesso(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, processoCriminalDt.getId_Serventia(), movimentacaoPeticao.getId(), processoCriminalDt.getListaArquivos(), logDt, obFabricaConexao);
				
				// Gera pendência Verificar prevenção
				if (id_ServentiaPrevencaoDataArquivamento != null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
					//Propositalmente foi usado outra instância de pendenciaNe para diferenciar dos outros pendenciaNe que geram conclusão
					new PendenciaNe().gerarPendenciaVerificarConexao(processoCriminalDt, UsuarioServentiaDt.SistemaProjudi, processoCriminalDt.getId_Serventia(), movimentacaoPeticao.getId(), logDt, obFabricaConexao);
				}
			}
			
			if(boGerarHistorico && pendenciaNe.isGerouConclusao()) {
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	            String data_inicio = df.format(new Date());
				new PendenciaResponsavelHistoricoNe().salvarHistorioco(pendenciaNe.getIdPendencia(), data_inicio, serventiaCargoResponsavel.getId(), serventiaCargoResponsavel.getId_ServentiaGrupo(), logDt.getId_Usuario(), logDt.getIpComputador(), obFabricaConexao);
			}
			
			
			if (processoCriminalDt.isProcessoFisico()) {
				try 
				{
					// Tenta atualizar as audiências do DRS, caso existam...
					(new AudienciaPublicadaNe()).executeProcessamentoAudienciaPublicadaImportacao(processoCriminalDt.getId(), processoCriminalDt.getProcessoNumeroCompleto(), usuarioDt, logDt, obFabricaConexao);					
				}  catch (Exception e) {}
			}

			obFabricaConexao.finalizarTransacao();
			
			if (serventiaDt.isPlantaoPrimeiroGrau() || serventiaDt.isPlantaoSegundoGrau()){				
				enviarEmailAnalistas(serventiaDt,processoCriminalDt.getProcessoNumeroCompleto());
			}
			
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarCadastroProcesso(processoCriminalDt);
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	/**
	 * Efetua cadastro de um processo proveniente de importação. Dependendo do
	 * código da instituição de onde está vindo o processo, será chamado um
	 * método correspondente para cadastrar o processo. Os códigos de
	 * instituição disponíveis são: 1 – SSP 2 – Procuradoria Estadual 3 –
	 * Procuradoria Municipal 4 – Advogado Particular 5 - Procuradoria da União
	 * 6 - Gestor Juridico
	 * 
	 * @param processodt
	 *            , dt com os dados do processo
	 * @param usuarioDt
	 *            , usuario que está cadastrando o processo
	 * 
	 * @author msapaula
	 */
	public void cadastrarProcessoImportacao(ProcessoCadastroDt processodt, UsuarioDt usuarioDt) throws Exception {

		switch (Funcoes.StringToInt(processodt.getCodigoInstituicao())) {
		case 1: // 1–SSP
			processodt.setAreaCodigo(String.valueOf(AreaDt.CRIMINAL));
			this.cadastrarProcessoCriminal(processodt, usuarioDt);
			return;

		default: // 2–Procuradoria Estadual 3–Procuradoria Municipal 4–Advogado Particular
			processodt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
			this.cadastrarProcessoCivel(processodt, usuarioDt, null);
			return;
		}
	}
	
	/**
	 * Método que realiza o cadastro de um processo carta precatória
	 * 
	 * @param processoCivelDt
	 *            , dt ProcessoCadastroDt com dados do processo a ser cadastrado
	 * @param usuarioDt
	 *            , usuário que está efetuando o cadasttro
	 * 
	 * @author lsbernardes
	 */
	public void cadastrarProcessoPrecatoria(ProcessoDt processoPrecatoriaDt, List arquivos, UsuarioDt usuarioDt, String id_ServentiaProcessoPrincipal, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		String id_ServentiaCargo = null;
		
		LogDt logDt = new LogDt(processoPrecatoriaDt.getId_UsuarioLog(), processoPrecatoriaDt.getIpComputadorLog());

		ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoPrecatoriaDt.getId_Serventia());
		String numero = ProcessoNumero.getInstance().getProcessoNumero(processoPrecatoriaDt.getForumCodigo());
		processoPrecatoriaDt.setAnoProcessoNumero(numero);
		//processoPrecatoriaDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA));
		processoPrecatoriaDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
		processoPrecatoriaDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
		processoPrecatoriaDt.setProcessoFisicoTipo(null);

		ServentiaDt serventiaProcessoPrincipalDt = new ServentiaNe().consultarId(id_ServentiaProcessoPrincipal);
		if (processoPrecatoriaDt.isCivel()){
			if (serventiaProcessoPrincipalDt != null &&  Funcoes.StringToInt(serventiaProcessoPrincipalDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL){
				processoPrecatoriaDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE));
			} else {
				processoPrecatoriaDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPC));
			}
		} else if (processoPrecatoriaDt.isCriminal()){
			if (serventiaProcessoPrincipalDt != null &&  Funcoes.StringToInt(serventiaProcessoPrincipalDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL){
				processoPrecatoriaDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL));
			} else {
				processoPrecatoriaDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPP));
			}
		}

		// Salva processo ok
		salvar(processoPrecatoriaDt, obFabricaConexao);

		// Salvando partes do processo ok
		new ProcessoParteNe().salvarPartesProcesso(processoPrecatoriaDt.getPartesProcesso(), processoPrecatoriaDt.getId(), logDt, obFabricaConexao);

		// Salvando assuntos do processo Ok
		new ProcessoAssuntoNe().salvarProcessoAssunto(processoPrecatoriaDt.getListaAssuntos(), processoPrecatoriaDt.getId(), logDt, obFabricaConexao);

		//para atender o provimento 16/2012
		// Distribui processo para um juiz ok
		//id_ServentiaCargo = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicao1Grau(processoPrecatoriaDt.getId_Serventia(), processoPrecatoriaDt.getId_ProcessoTipo());
		id_ServentiaCargo =DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(processoPrecatoriaDt.getId_Serventia(), processoPrecatoriaDt.getId_AreaDistribuicao());

		// Salva o juiz como responsável ok
		new ProcessoResponsavelNe().salvarProcessoResponsavel(processoPrecatoriaDt.getId(), id_ServentiaCargo, true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);

		/* ---------- PONTEIRO ----------------*/
		///salvo o ponteiro
		new PonteiroLogNe().salvar(new PonteiroLogDt(processoPrecatoriaDt.getId_AreaDistribuicao(),PonteiroLogTipoDt.DISTRIBUICAO,processoPrecatoriaDt.getId(),processoPrecatoriaDt.getId_Serventia(),UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi, new Date(), id_ServentiaCargo  ),obFabricaConexao );
		/* ---------- PONTEIRO ----------------*/
		
		// Gera movimentação PETIÇÃO ENVIADA ok
		MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacao(processoPrecatoriaDt.getId(), MovimentacaoTipoDt.PETICAO_ENVIADA, usuarioDt.getId_UsuarioServentia(), "", logDt, obFabricaConexao);
		//MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacaoPeticaoEnviada(processoPrecatoriaDt.getId(), serventiaDt.isJuizado(), usuarioDt, logDt, obFabricaConexao);

		String visibilidade=null;
		if (processoPrecatoriaDt.isSegredoJustica()){
			visibilidade=String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL);
		}
		// Vincula arquivos da pendencia ao novo processo ok
		new MovimentacaoArquivoNe().vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoPeticao.getId(), visibilidade, logDt, obFabricaConexao);

		// Gera movimentação PROCESSO DISTRIBUÍDO ok
		movimentacaoNe.gerarMovimentacaoProcessoDistribuido(processoPrecatoriaDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaDt.getServentia() + " - Normal", logDt, obFabricaConexao);

		// Gera pendência verificar novo processo ok
		new PendenciaNe().gerarPendenciaVerificarNovaPrecatoria(processoPrecatoriaDt, UsuarioServentiaDt.SistemaProjudi, processoPrecatoriaDt.getId_Serventia(), movimentacaoPeticao.getId(), arquivos, logDt, obFabricaConexao);
	}

	/**
	 * Método responsável em retornar os dados completos de um Processo de
	 * acordo com o Id passado. Consulta dados do processo, das partes, os
	 * assuntos e movimentações de um processo.
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @author msapaula
	 */
	public ProcessoDt consultarDadosProcesso(String id_Processo, UsuarioDt usuarioDt, boolean ehConsultaPublica, boolean acessoOutraServentiaOuCodigoDeAcesso, int nivelAcesso) throws Exception {
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoDt processoDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));			

			processoDt = this.consultarIdCompleto(id_Processo, obFabricaConexao);			

			// Verifica se processo possui apensos
			processoDt.setTemApensos(this.verificaProcessosApensos(id_Processo, obFabricaConexao));
			
			if (processoDt.hasVitima()) {
				if(ehConsultaPublica || 
				   acessoOutraServentiaOuCodigoDeAcesso ||
				   !this.podeAcessarProcesso(usuarioDt, processoDt, obFabricaConexao)){
				
					if (processoDt.getListaOutrasPartes() != null) {
						for (int j = 0; j < processoDt.getListaOutrasPartes().size(); j++) {
							ProcessoParteDt parteDt = (ProcessoParteDt) processoDt.getListaOutrasPartes().get(j);
							if (parteDt.isVitima()){
								parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
							}
						}
					}
				}
			}
			
			// Captura lista de assuntos
			processoDt.setListaAssuntos(processoAssuntoNe.consultarAssuntosProcesso(id_Processo, obFabricaConexao));

			// Captura movimentações do processo
			List movimentacoes = movimentacaoNe.consultarMovimentacoesProcesso(usuarioDt, processoDt, acessoOutraServentiaOuCodigoDeAcesso, obFabricaConexao, nivelAcesso);
			processoDt.setListaMovimentacoes(movimentacoes);
			
			if (processoDt.getDataDigitalizacao() != null && processoDt.getDataDigitalizacao().trim().length() > 0) {
			// Captura sentenças do processo físico
				List movimentacoesFisico = movimentacaoNe.consultarMovimentacoesProcessoFisico(usuarioDt, processoDt, acessoOutraServentiaOuCodigoDeAcesso, obFabricaConexao, nivelAcesso);
				processoDt.setListaMovimentacoesFisico(movimentacoesFisico);
			}
						
			
			
			// Verifica se é processo criminal e consulta informações adicionais
			if (processoDt != null && processoDt.isCriminal()) {
				ProcessoCriminalNe processoCriminalNe = new ProcessoCriminalNe();
				processoDt.setProcessoCriminalDt(processoCriminalNe.consultarIdProcesso(id_Processo, obFabricaConexao));
				processoDt.getProcessoCriminalDt().setId_Processo(processoDt.getId());
			}
			
			//passando a lista de movimentações do processo para o request
			processoDt = this.prepararListaMovimentacoesProcesso(processoDt, new UsuarioNe(), true);
			
			if (processoDt != null && (processoDt.isSegredoJustica() || processoDt.isSigiloso())){
				if(!this.podeAcessarProcesso(usuarioDt, processoDt, obFabricaConexao)) {
					processoDt.limparSegredoJustica();
					ServentiaDt serventiaDt = this.consultarIdServentia(processoDt.getId_Serventia());
					ServentiaCargoDt serventiaCargoResponsavel = new ServentiaCargoDt();
					if(serventiaDt.isSegundoGrau()) {
						serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarRelatorResponsavelProcessoSegundoGrau(processoDt.getId(), obFabricaConexao);	
					} else if(serventiaDt.isTurma()) {
						serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarServentiaCargoResponsavelProcesso(processoDt.getId(), null, String.valueOf(GrupoTipoDt.JUIZ_TURMA), true);
					} else {
						serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarServentiaCargoResponsavelProcesso(processoDt.getId(), null, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), true);
					}
					processoDt.setServentiaCargoResponsavelDt(serventiaCargoResponsavel);
				}
			}
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return processoDt;
	}
	
	/**
	 * Método responsável em retornar os dados completos de um Processo de
	 * acordo com o Id passado. Consulta dados do processo, das partes, os
	 * assuntos e movimentações de um processo.
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @author msapaula
	 */
	public ProcessoDt consultarDadosProcessoPrecatoria(String id_Processo, FabricaConexao conexao) throws Exception {		
		ProcessoDt processoDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else
				obFabricaConexao = conexao;

			processoDt = this.consultarIdCompleto(id_Processo, obFabricaConexao);
			
			processoDt.setId_ProcessoPrincipal(processoDt.getId());
			processoDt.setId("");
			processoDt.setProcessoNumero("");
			processoDt.setDigitoVerificador("");
			processoDt.setAno("");
			processoDt.setId_Serventia("null");
			processoDt.setId_ServentiaOrigem("null");
			processoDt.setId_ProcessoTipo("null");
			processoDt.setId_ProcessoFase("null");
			processoDt.setId_ProcessoStatus("null");
			processoDt.setId_AreaDistribuicao("null");
			processoDt.setComarcaCodigo("");
			processoDt.setForumCodigo("");
			processoDt.setDataRecebimento("");
			
			List listapartes = processoDt.getPartesProcesso();
			processoDt.setListaPolosAtivos(new ArrayList());
			processoDt.setListaPolosPassivos(new ArrayList());
			processoDt.setListaOutrasPartes(new ArrayList());
			
			for (Iterator iterator = listapartes.iterator(); iterator.hasNext();) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) iterator.next();
				
				// Captura advogados da parte antes de limpar o id da mesma
				List listaAdvogadosParte =  this.consultarAdvogadoParteProcesso(id_Processo, processoParteDt.getId(), obFabricaConexao);
				for (Iterator iteratorAdvogados = listaAdvogadosParte.iterator(); iteratorAdvogados.hasNext();) {
					if (processoParteDt.getAdvogados() == null) processoParteDt.setListaAdvogados(new ArrayList());
					ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) iteratorAdvogados.next();
					processoParteAdvogadoDt.setId("");
					processoParteAdvogadoDt.setId_ProcessoParte("null");
					processoParteDt.getAdvogados().add(processoParteAdvogadoDt);
				}
				
				processoParteDt.setId("");
				processoParteDt.getEnderecoParte().setId("");
				
				int tipo = Funcoes.StringToInt(processoParteDt.getProcessoParteTipoCodigo());
				switch (tipo) {
					case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
						processoDt.addListaPoloAtivo(processoParteDt);
						break;
					case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
						processoDt.addListaPolosPassivos(processoParteDt);
						break;
					default:
						processoDt.addListaOutrasPartes(processoParteDt);
						break;
				}
				
			}

			// Captura lista de assuntos
//			List listaAssuntos = processoAssuntoNe.consultarAssuntosProcesso(id_Processo, obFabricaConexao);
//			processoDt.setListaAssuntos(new ArrayList());
//			for (Iterator iteratorAssuntos = listaAssuntos.iterator(); iteratorAssuntos.hasNext();) {
//				ProcessoAssuntoDt processoAssuntoDt = (ProcessoAssuntoDt) iteratorAssuntos.next();
//				processoAssuntoDt.setId("");
//				processoDt.getListaAssuntos().add(processoAssuntoDt);
//			}

			// Captura advogados processo
//			List listaAdvogadosParte =  this.consultarAdvogadosProcesso(id_Processo, obFabricaConexao);
//			processoDt.setListaAdvogados(new ArrayList());
//			for (Iterator iteratorAdvogados = listaAdvogadosParte.iterator(); iteratorAdvogados.hasNext();) {
//				ProcessoParteAdvogadoDt advogadoParte = (ProcessoParteAdvogadoDt) iteratorAdvogados.next();
//				advogadoParte.setId("");
//				
//			}
//			
//			processoDt.setListaAdvogados(this.consultarAdvogadosProcesso(id_Processo, obFabricaConexao));

		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return processoDt;
	}

	/**
	 * Método responsável em retornar os dados completos de um Processo de
	 * acordo com o Id passado. Essa consulta se trata de acesso externo devendo
	 * verificar os processos Segredo de Justiça
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @author msapaula
	 */
	public ProcessoDt consultarDadosProcessoAcessoExterno(String id_Processo, UsuarioDt usuarioDt, boolean ehConsultaPublica, boolean acessoOutraServentiaOuCodigoDeAcesso, int nivelAcesso) throws Exception {
		ProcessoDt processoDt = this.consultarDadosProcesso(id_Processo, usuarioDt, ehConsultaPublica, acessoOutraServentiaOuCodigoDeAcesso, nivelAcesso);
		return processoDt;
	}

	/**
	 * Consulta os dados de um processo e suas partes baseado no Id passado.
	 * Cria conexão internamente
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @author msapaula, jrcorrea 14/10/2015
	 */
	public ProcessoDt consultarIdCompleto(String id_Processo) throws Exception {
		return this.consultarIdCompleto(id_Processo, null);
	}

	/**
	 * Consulta os dados completos de um processo e suas partes baseado no Id
	 * passado. Esse método será utilizado pelo WebService Servico04, operação
	 * Ler Dados de Processo, pois deve retornar dados adicionais como a Comarca
	 * e SubTipo da Serventia
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @author msapaula
	 */
	public ProcessoDt consultarIdCompletoWebService(String id_Processo) throws Exception {
		ProcessoDt processoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			

			processoDt = this.consultarIdCompleto(id_Processo, obFabricaConexao);

			// Dados completos da serventia, para obter Comarca e SubTipo da Serventia
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia(), obFabricaConexao);
			processoDt.setComarcaCodigo(serventiaDt.getComarcaCodigo());
			processoDt.setComarca(serventiaDt.getComarca());
			processoDt.setServentiaSubTipoCodigo(serventiaDt.getServentiaSubtipoCodigo());
			processoDt.setServentiaSubTipo(serventiaDt.getServentiaSubtipo());

			// Captura lista de assuntos
			processoDt.setListaAssuntos(new ProcessoAssuntoNe().consultarAssuntosProcesso(id_Processo, obFabricaConexao));

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return processoDt;
	}

	/**
	 * Consulta os dados de um processo e suas partes baseado no Id passado.
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param conexao
	 *            , utiliza conexão existente para efetuar consulta
	 * @author msapaula
	 */
	public ProcessoDt consultarIdCompleto(String id_Processo, FabricaConexao conexao) throws Exception {
		ProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}else{
				obFabricaConexao = conexao;
			}
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdCompleto(id_Processo);
			
			if (dtRetorno==null){
				throw new MensagemException("Não foi possível localizar o processo, reinicie todo o procedimento de consulta.");
			}
			
			obDados.copiar(dtRetorno);
			

		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta os dados básicos de um processo e devolve um objeto do tipo
	 * ProcessoDt com esses dados
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @author msapaula
	 */
	public ProcessoDt consultarIdSimples(String id_Processo) throws Exception {
		ProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_Processo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarDataDistribuicao(String id_Processo, FabricaConexao fabConexao) throws Exception {
		String stTemp = "";
		
		ProcessoPs obPersistencia = new ProcessoPs(fabConexao.getConexao());
		stTemp = obPersistencia.consultarDataDistribuicao(id_Processo);
		
		return stTemp;
	}
	
	/**
	 * Consulta os dados de um processo e suas partes baseado no número de
	 * processo passado
	 * 
	 * @param numeroProcesso - número do processo desejado
	 * @author msapaula, hmgodinho
	 */
	public ProcessoDt consultarProcessoNumeroCompleto(String numeroProcessoCompleto) throws Exception {
		return consultarProcessoNumeroCompleto(numeroProcessoCompleto, null);
	}
	
	/**
	 * Consulta os dados de um processo e suas partes baseado no número de
	 * processo passado
	 * 
	 * @param numeroProcesso - número do processo desejado
	 * @param fabricaConexao - conexão com o banco (não obrigatório)
	 * @author msapaula, hmgodinho
	 */
	public ProcessoDt consultarProcessoNumeroCompleto(String numeroProcessoCompleto, FabricaConexao fabricaConexao) throws Exception {
		ProcessoDt dtRetorno = null;
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		FabricaConexao obFabricaConexao = null;
		try {
			
			if(fabricaConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabricaConexao;
			}
			
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			// Divide número de processo do dígito verificador
			String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
			if (stTemp.length >= 1) numero = stTemp[0];			
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];
			if (stTemp.length >= 3) ano = stTemp[2];
			if (stTemp.length >= 6) forumCodigo = stTemp[5];

			dtRetorno = obPersistencia.consultarProcessoNumeroCompleto(numero, digitoVerificador, ano, forumCodigo);			
		} finally {
			//Se a conexão não tiver sido passada como parâmetro
			//fecha a conexão. Se for passada como parâmetro, deixa
			//a conexão como veio.
			if(fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta os dados de um processo e suas partes baseado no número de
	 * processo passado
	 * 
	 * @param numeroProcesso - número do processo desejado
	 * @param fabricaConexao - conexão com o banco (não obrigatório)
	 * @author asrocha
	 */
	public ProcessoDt consultarProcessoPrincipal(String numeroProcessoCompleto, FabricaConexao fabricaConexao) throws Exception {
		ProcessoDt dtRetorno = null;
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		FabricaConexao obFabricaConexao = null;
		try {
			
			if(fabricaConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			} else {
				obFabricaConexao = fabricaConexao;
			}
			
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			// Divide número de processo do dígito verificador
			String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
			if (stTemp.length >= 1) numero = stTemp[0];			
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];
			if (stTemp.length >= 3) ano = stTemp[2];
			if (stTemp.length >= 6) forumCodigo = stTemp[5];

			dtRetorno = obPersistencia.consultarProcessoPrincipal(numero, digitoVerificador, ano, forumCodigo);			
		} finally {
			if(fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta os dados de um processo e suas partes baseado no número de
	 * processo passado
	 * 
	 * @param numeroProcesso
	 *            , número do processo desejado
	 * @author fasoares
	 */
	public ProcessoDt consultarProcessoNumeroCompletoDigitoAno(String numeroProcessoCompleto) throws Exception {
		ProcessoDt dtRetorno = null;
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			// Divide número de processo do dígito verificador
			String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
			if (stTemp.length >= 1) numero = stTemp[0];
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];
			if (stTemp.length >= 3) ano = stTemp[2];
			if (stTemp.length >= 6) forumCodigo = stTemp[5];

			dtRetorno = obPersistencia.consultarProcessoNumeroCompleto(numero, digitoVerificador, ano, forumCodigo);
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
//	public List<ProcessoDt> consultarProcessoNumeroDigito(String numeroDigitoProcesso, FabricaConexao fabricaConexao) throws Exception {
//		List<ProcessoDt> dtRetorno = null;
//		String numero = null;
//		String digitoVerificador = null;
//		FabricaConexao obFabricaConexao = null;
//		try {
//			
//			if(fabricaConexao == null) {
//				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			} else {
//				obFabricaConexao = fabricaConexao;
//			}
//			
//			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
//			
//			// Divide número de processo do dígito verificador
//			String[] stTemp = numeroDigitoProcesso.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
//			if (stTemp.length >= 1) numero = stTemp[0];			
//			if (stTemp.length >= 2) digitoVerificador = stTemp[1];
//			
//			dtRetorno = obPersistencia.consultarIdCompleto(numero, digitoVerificador);			
//		} finally {
//			//Se a conexão não tiver sido passada como parâmetro
//			//fecha a conexão. Se for passada como parâmetro, deixa
//			//a conexão como veio.
//			if(fabricaConexao == null) {
//				obFabricaConexao.fecharConexao();
//			}
//		}
//		return dtRetorno;
//	}
	
	/**
	 * Consulta os dados de um processo e suas partes baseado no número de
	 * processo passado
	 * 
	 * @param numeroProcesso
	 *            , número do processo desejado
	 * @author mmgomes
	 */
	public ProcessoDt consultarProcessoNumeroCompletoDigitoAno(String numeroProcessoCompleto, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoDt dtRetorno = null;
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		
		// Divide número de processo do dígito verificador
		String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
		if (stTemp.length >= 1) numero = stTemp[0];			
		if (stTemp.length >= 2) digitoVerificador = stTemp[1];
		if (stTemp.length >= 3) ano = stTemp[2];
		if (stTemp.length >= 6) forumCodigo = stTemp[5];

		dtRetorno = obPersistencia.consultarProcessoNumeroCompleto(numero, digitoVerificador, ano, forumCodigo);
		return dtRetorno;
	}
	
	/**
	 * Modificação no método acima que retorna apenas o ID do processo utilizando como parâmetro para a consutla o número completo
	 * @param numeroProcessoCompleto
	 * @param obFabricaConexao
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarIdProcessoNumeroCompletoDigitoAno(String numeroProcessoCompleto, FabricaConexao obFabricaConexao) throws Exception {
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		
		// Divide número de processo do dígito verificador
		String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
		if (stTemp.length >= 1) numero = stTemp[0];			
		if (stTemp.length >= 2) digitoVerificador = stTemp[1];
		if (stTemp.length >= 3) ano = stTemp[2];
		if (stTemp.length >= 6) forumCodigo = stTemp[5];

		return obPersistencia.consultarIdProcessoNumeroCompleto(numero, digitoVerificador, ano, forumCodigo);
	}
	
	/**
	 * ATENÇÃO: ESTE MÉTODO FOI DESENVOLVIDO ESPECIFICAMENTE PARA O SERVIÇO DE TROCA AUTOMÁTICA DE NÚMERO DE
	 * PROCESSOS DE EXECUÇÃO PENAL.
	 * 
	 * Retorna um ProcessoDt preenchido apenas com as informações do número do processo. 
	 * 
	 * @param numeroProcessoCompleto
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoNumeroCompletoDigitoAnoIdProc(String idProc, String numeroProcessoCompleto, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoDt dtRetorno = null;
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		
		// Divide número de processo do dígito verificador
		String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
		if (stTemp.length >= 1) numero = stTemp[0];			
		if (stTemp.length >= 2) digitoVerificador = stTemp[1];
		if (stTemp.length >= 3) ano = stTemp[2];
		if (stTemp.length >= 6) forumCodigo = stTemp[5];

		dtRetorno = obPersistencia.consultarProcessoNumeroCompletoIdProc(idProc, numero, digitoVerificador, ano, forumCodigo);
		return dtRetorno;
	}
	
	/**
	 * Método responsável por consultar a serventia origem de um recurso
	 * @param id_processo, id do processo
	 * @author lsbernardes
	 */
	public ServentiaDt consultarServentiaOrigemRecurso(String id_processo) throws Exception {
		ServentiaDt serventiaDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			serventiaDt = obPersistencia.consultarServentiaOrigemRecurso(id_processo);			
		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return serventiaDt;
	}
	
	
	/**
	 * lista os processos existentes de acordo o número passado
	 * @param digitoVerificador
	 * @param numeroProcesso
	 * @param ano
	 * @author wcsilva
	 */
	public List listarProcessoNumeroCompleto(String numeroCompletoProcesso, boolean ehConsultaPublica) throws Exception {
		List dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stDigito = "";
			String stAno = "";
			
			if (numeroCompletoProcesso != null && numeroCompletoProcesso.length() > 0) {
				String[] stTemp = numeroCompletoProcesso.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
				if (stTemp.length >= 1) numeroCompletoProcesso = stTemp[0];
				else
					numeroCompletoProcesso = "";
				if (stTemp.length >= 2) stDigito = stTemp[1];
				if (stTemp.length >= 3) stAno = stTemp[2];
			}
			
			dtRetorno = obPersistencia.listarProcessoNumeroCompleto(numeroCompletoProcesso, stDigito, stAno);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}


	/**
	 * Consulta geral de processos para usuário externos. Realiza os tratamentos
	 * para cada grupo e devolve a lista de processos que esse pode visualizar.
	 * 
	 * @param buscaProcessoDt
	 *            Dt com os dados da pesquisa
	 * @param usuarioDt
	 *            usuário que está realizando a consulta
	 * @param pesquisarNomeExato
	 *            , determina se a pesquisa de nome parte será por nome exato ou
	 *            não
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarTodosProcessosUsuariosExternos(BuscaProcessoDt buscaProcessoDt, UsuarioDt usuarioDt, String posicao) throws Exception {
		List listaProcessos = null;
		int grupo = -1;
		if (usuarioDt != null) grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		buscaProcessoDt.setSegredoJustica("true");
		// Se foi digitado número do processo ou nome da parte permite a
		// consulta pelos processos com segredo de justiça
		if (buscaProcessoDt.temNumero()){ 
			buscaProcessoDt.setSegredoJustica("");
		}

		switch (grupo) {
		// Contador pode acessar todos os processos ativos nas serventias de
		// sua comarca
		case GrupoDt.CONTADORES_VARA:
			listaProcessos = this.consultarProcessos(buscaProcessoDt, usuarioDt, posicao);
			break;

		// Para outros usuários externos não poderão ver os processos que
		// são Segredo de Justiça
		default:
			buscaProcessoDt.setId_Comarca(null);
			listaProcessos = this.consultarProcessos(buscaProcessoDt, usuarioDt, posicao);
			break;
		}

		// Se processo é Segredo de Justiça retorna somente as inicias do
		// nome
		if (listaProcessos != null) {
			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt objProcesso = (ProcessoDt) listaProcessos.get(i);
				if (objProcesso.getSegredoJustica().equals("true")) {
					for (int j = 0; j < objProcesso.getPartesProcesso().size(); j++) {
						ProcessoParteDt parteDt = (ProcessoParteDt) objProcesso.getPartesProcesso().get(j);
						parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
					}
				}
			}
		}
		return listaProcessos;
	}

	/**
	 * Consulta os processos próprios de usuários externos (advogados,
	 * promotores, contadores e delegados). Realiza os tratamentos para cada
	 * grupo e devolve a lista de processos que esse pode visualizar.
	 * 
	 * @param processoDt
	 *            , Dt com os dados da pesquisa
	 * @param usuarioDt
	 *            , usuário que está realizando a consulta
	 * @param pesquisarNomeExato
	 *            , determina se a pesquisa de nome parte será por nome exato ou
	 *            não
	 * @param posicao
	 *            , parametro para paginação
	 * @param quantidadeRegistrosPagina
	 *            , quantidade de registros por página
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosUsuariosExternos(BuscaProcessoDt processoDt, UsuarioDt usuarioDt, String posicao, String quantidadeRegistrosPagina) throws Exception {
		List listaProcessos = null;
		int grupoTipo = -1;
		if (usuarioDt != null)
		// grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		switch (grupoTipo) {

		// Delegado pode visualizar os processos onde a serventia de origem
		// é a serventia do usuário
		// case GrupoDt.AUTORIDADES_POLICIAIS:
		case GrupoTipoDt.AUTORIDADE_POLICIAL:
			listaProcessos = this.consultarProcessosDelegacia(processoDt, posicao);
			break;

		// Promotor visualiza os processos em que ele é responsável
		// case GrupoDt.PROMOTORES:
		case GrupoTipoDt.MP:
			processoDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargo());
			listaProcessos = this.consultarProcessosPromotor(processoDt, posicao);
			break;

		case GrupoTipoDt.COORDENADOR_PROMOTORIA:
			listaProcessos = this.consultarProcessosPromotor(processoDt, posicao);
			break;

		case GrupoTipoDt.COORDENADOR_DEFENSORIA_PUBLICA:
		case GrupoTipoDt.COORDENADOR_PROCURADORIA:
		case GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO:
		case GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA:
			processoDt.setId_UsuarioServentia("");
			listaProcessos = this.consultarProcessosAdvogado(processoDt , posicao, quantidadeRegistrosPagina);
			break;
		
		/*case GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO:
			listaProcessos = this.consultarProcessosEscritorioJuridico(processoDt.getPromovente(), pesquisarNomeExato, processoDt.getCpfCnpjParte(), processoDt.getProcessoNumeroSimples(), processoDt.getProcessoStatusCodigo(), usuarioDt.getId_Serventia(), processoDt.getId_Classificador(), posicao);
			break;
		*/
			
		// Advogado visualiza os processos onde ele atua
		// case GrupoDt.ADVOGADOS:
		case GrupoTipoDt.ADVOGADO:
			listaProcessos = this.consultarProcessosAdvogado(processoDt, posicao, quantidadeRegistrosPagina);
			break;

		// Assistente do advogado visualiza os processos onde seu chefe atua
		// case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
		case GrupoTipoDt.ASSESSOR_MP:
			if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
				processoDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentiaChefe());
				listaProcessos = this.consultarProcessosPromotor(processoDt, posicao);
			}
			break;
		case GrupoTipoDt.ASSESSOR_ADVOGADO:
			if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
				processoDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentiaChefe());
				listaProcessos = this.consultarProcessosAdvogado(processoDt, posicao, quantidadeRegistrosPagina);						
			}
			break;

		// Outros assistentes deve ver somente os processos do chefe
		// case GrupoDt.ASSISTENTES:
		case GrupoTipoDt.ASSESSOR:
			if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
				switch (Funcoes.StringToInt(usuarioDt.getGrupoUsuarioChefe())) {
				/*
				 * case GrupoDt.MINISTERIO_PUBLICO: listaProcessos =
				 * this.consultarProcessosPromotor
				 * (processoDt.getPromovente(), pesquisarNomeExato,
				 * processoDt.getCpfCnpjParte(),
				 * processoDt.getProcessoNumeroSimples(),
				 * usuarioDt.getId_ServentiaCargoUsuarioChefe(),
				 * processoDt.getProcessoStatusCodigo(),
				 * processoDt.getId_Serventia(), posicao); break;
				 */
				case GrupoDt.AUTORIDADES_POLICIAIS:
					processoDt.setId_ServentiaCargo( usuarioDt.getGrupoUsuarioChefe());
					listaProcessos = this.consultarProcessosDelegacia(processoDt, posicao);
					break;
				}
			}
			break;

		}
		
		return listaProcessos;
	}
	
	/**
	 * Consulta os processos próprios de usuários externos (advogados,
	 * promotores, contadores e delegados). Realiza os tratamentos para cada
	 * grupo e devolve a lista de processos que esse pode visualizar.
	 * 
	 * @param processoDt
	 *            , Dt com os dados da pesquisa
	 * @param usuarioDt
	 *            , usuário que está realizando a consulta
	 * @param pesquisarNomeExato
	 *            , determina se a pesquisa de nome parte será por nome exato ou
	 *            não
	 * @param posicao
	 *            , parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosUsuariosExternos(BuscaProcessoDt processoDt, UsuarioDt usuarioDt,  String posicao) throws Exception {
		return consultarProcessosUsuariosExternos(processoDt, usuarioDt,   posicao, null);
	}


//	public List consultar Processos(String nomeParte, String pesquisarNomeExato, String cpfCnpjParte, String processoNumero, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String id_Comarca, String SegredoJustica, String id_Classificador, UsuarioDt usuarioDt, String posicao) throws Exception {
//		List listaProcessos = null;
//		FabricaConexao obFabricaConexao = null;
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			listaProcessos = consultarProcessos(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, statusCodigo, id_ProcessoTipo, id_Serventia, id_Comarca, SegredoJustica, id_Classificador, usuarioDt, posicao, obFabricaConexao);
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//		return listaProcessos;
//	}
	
//	/**
//	 * Método que consulta os processos ativos de determinada pessoa no sistema SSP.
//	 * @param idPessoa - ID da pessoa
//	 * @param conexao - conexão do banco
//	 * @return lista de processos
//	 * @throws Exception
//	 * @author hmgodinho
//	 */
//	public List consultarProcessosAtivosPessoa(String idPessoa, Connection conexao) throws Exception {
//		List listaProcessos = null;
//		ProcessoPs obPersistencia = new ProcessoPs(conexao);
//		listaProcessos = obPersistencia.consultarProcessosAtivosPessoa(idPessoa);
//		return listaProcessos;
//	}
	
	/**
	 * Método responsável em consultar os processos de acordo com parâmetros
	 * passados. Armazena os parâmetros em variáveis locais, para que ao usar a
	 * paginação a consulta não perca os filtros preenchidos pelo usuario.
	 * 
	 * @param nomeParte
	 *            , filtro de nome da parte
	 * @param pesquisarNomeExato
	 *            , determina se a pesquisa de nome parte será por nome exato ou
	 *            não
	 * @param cpfCnpjParte
	 *            , filtro do cpf ou cnj da parte
	 * @param processoNumero
	 *            , filtro do número do processo
	 * @param statusCodigo
	 *            , filtro de status do processo
	 * @param id_ProcessoTipo
	 *            , filtro de tipo de processo
	 * @param id_Serventia
	 *            , identificação da serventia do processo que deseja consultar
	 * @param id_Comarca
	 *            , identificação da comarca para filtrar os processos
	 * @param SegredoJustica
	 *            , define se devem ser retornados os processos com segredo de
	 *            justiça
	 * @param id_Classificador
	 *            , filtro para classificador do processo
	 * @param usuarioDt
	 *            , usuário que está efetuando a consulta
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula, hmgodinho, jrcorrea 20/11/2019
	 */
	public List consultarProcessos(BuscaProcessoDt buscaProcesso, UsuarioDt usuarioDt, String posicao) throws Exception {
		List listaProcessos = null;
		ServentiaDt serventiaDt = null;

		int grupoTipo = -1;
		if (usuarioDt != null) grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());				
			buscaProcesso.validarEntradas(objBuscaProcessoDt);
											
			switch (grupoTipo) {
		
    			case GrupoTipoDt.DISTRIBUIDOR:
    				// Distribuidor vê apenas processos da sua comarca
    				listaProcessos = obPersistencia.consultarProcessos(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), buscaProcesso.getId_Classificador(), posicao);
    				break;
    			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
    				 if( usuarioDt.isGabineteUPJ() ) {
    					 listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargo(), buscaProcesso.getId_Classificador(), posicao);
    				 } else {
    					 listaProcessos = obPersistencia.consultarProcessosGabineteSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargo(), buscaProcesso.getId_Classificador(), ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO,CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, posicao);
    				 }
    				break;
    			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:				
    				listaProcessos = obPersistencia.consultarProcessosGabineteSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargoUsuarioChefe(), buscaProcesso.getId_Classificador(),ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, posicao);		        
    				break;				
    			case GrupoTipoDt.ASSISTENTE_GABINETE:
    				listaProcessos = obPersistencia.consultarProcessosGabineteSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargo(), buscaProcesso.getId_Classificador(),ProcessoResponsavelDt.REDATOR_NAO, CargoTipoDt.ASSISTENTE_GABINETE, posicao);
    				break;
    			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
    				listaProcessos = obPersistencia.consultarProcessosGabineteSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargo(), buscaProcesso.getId_Classificador(),ProcessoResponsavelDt.REDATOR_NAO, CargoTipoDt.ASSISTENTE_GABINETE_FLUXO, posicao);
    				break;
    			case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
    					listaProcessos = obPersistencia.consultarTodosProcessosGabinete(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), usuarioDt.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(),  buscaProcesso.getId_Classificador(), posicao);				
    				break;
    			case GrupoTipoDt.ESTAGIARIO:
    				int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
    				switch (grupo) {
    					case GrupoDt.ESTAGIARIO_GABINETE:
    						listaProcessos = obPersistencia.consultarTodosProcessosGabinete(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), usuarioDt.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(),  buscaProcesso.getId_Classificador(), posicao);
    						break;
    					default:
    						if (usuarioDt.isServentiaTipo2Grau() || usuarioDt.isGabineteUPJ() ) {
    							if(verificarParametrosConsultaProcessoSegundoGrau(buscaProcesso,  "", usuarioDt)) {
    								listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), "", buscaProcesso.getId_Classificador(), posicao);
    							}
    						} else {
    							listaProcessos = obPersistencia.consultarProcessos(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), null, buscaProcesso.getSegredoJustica(), buscaProcesso.getId_Classificador(), posicao);
    						}
    						break;
    				}
				break;
		
			default:
				if (usuarioDt.isServentiaTipo2Grau() ) { 
					if(verificarParametrosConsultaProcessoSegundoGrau(buscaProcesso,  "", usuarioDt)) {
						listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), "", buscaProcesso.getId_Classificador(), posicao);
					}
				}else if( usuarioDt.isGabineteUPJ() ) {
					if (usuarioDt.isConciliador()) {
						//não vai usar o o id_serventia_cargo
						listaProcessos = obPersistencia.consultarProcessosServentiaRelacionadaConciliador(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), usuarioDt.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(),  buscaProcesso.getId_Classificador(), posicao);						
					}else {
						if(verificarParametrosConsultaProcessoSegundoGrau(buscaProcesso,  usuarioDt.getId_ServentiaCargo(), usuarioDt)) {
							listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_Comarca(), buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargo(), buscaProcesso.getId_Classificador(), posicao);
						}
					}
				}else {
					listaProcessos = obPersistencia.consultarProcessos(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), null, buscaProcesso.getSegredoJustica(), buscaProcesso.getId_Classificador(), posicao);
				}
				break;
			}
				
			setQuantidadePaginas(listaProcessos);
						
			return listaProcessos;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método que verifica se os campos preenchidos na tela de consulta de processos de segundo grau foram preenchidos corretamente.
	 * @param buscaProcesso - dados preenchidos na tela
	 * @param processoNumero - número do processo
	 * @param id_ServentiaCargoResponsavel - id do serv cargo responsável pelo processo
	 * @param usuarioSessao - usuário que está consultando
	 * @return true se estiver tudo certo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean verificarParametrosConsultaProcessoSegundoGrau(BuscaProcessoDt buscaProcesso,  String id_ServentiaCargoResponsavel, UsuarioDt usuarioSessao) throws Exception {
		
		//BO 2020/4246 - Foi soilicitado que os distribuidores das câmaras possam consultar processos sem informar o status. Motivo: eles fazem muitas consultas de processos devido a prevenção/conexão que,
		//na maioria das vezes não tem o status conhecido, o que os força a realiazar a mesma consulta algumas vezes até acertar o status. 
		if(!usuarioSessao.isDistribuidorCamara()) {
			if (buscaProcesso.getProcessoStatusCodigo() == null || buscaProcesso.getProcessoStatusCodigo().length() == 0){
				throw new MensagemException("É necessário informar o Status do Processo.");
			}
		}
		
		if((buscaProcesso.getNomeParte() == null || buscaProcesso.getNomeParte().length() == 0) &&
				(buscaProcesso.getProcessoNumeroToInt() == -1) &&
				(id_ServentiaCargoResponsavel == null || id_ServentiaCargoResponsavel.length() == 0) &&				
				(buscaProcesso.getCpfCnpjParteToLong() == -1)  &&
				(buscaProcesso.getId_Serventia() == null || buscaProcesso.getId_Serventia().length() == 0)){
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		
		return true;
	}
	
	/**
	 * Método responsável em consultar os processos de acordo com parâmetros
	 * passados. Armazena os parâmetros em variáveis locais, para que ao usar a
	 * paginação a consulta não perca os filtros preenchidos pelo usuario.
	 * 
	 * @param id_Serventia
	 *            , identificação da serventia do processo que deseja consultar
	 * @param id_Comarca
	 *            , identificação da comarca para filtrar os processos
	 * @param usuarioDt
	 *            , usuário que está efetuando a consulta
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author lsbernardes
	 */
	public List consultarProcessosPossivelPrescricao(String id_Serventia, String id_Comarca, UsuarioDt usuarioDt, String posicao) throws Exception {
		List listaProcessos = null;
		int grupoTipo = -1;
		if (usuarioDt != null) grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			if (id_Serventia == null || id_Serventia.length() == 0) id_Serventia = stUltimoId_Serventia;
			if (id_Comarca == null || id_Comarca.length() == 0) id_Comarca = stUltimoId_Comarca;

			switch (grupoTipo) {
			case GrupoTipoDt.DISTRIBUIDOR:
				// Distribuidor vê apenas processos da sua comarca
				listaProcessos = obPersistencia.consultarProcessosPossivelPrescricao(null, id_Comarca, posicao);
				break;

			default:
				listaProcessos = obPersistencia.consultarProcessosPossivelPrescricao(id_Serventia, null, posicao);
				break;
			}

			// Armazena variáveis da última busca
			stUltimoId_Serventia = id_Serventia;
			stUltimoId_Comarca = id_Comarca;

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	public List consultarTodosProcessos(BuscaProcessoDt buscaProcesso , UsuarioDt usuarioDt, String posicao) throws Exception, MensagemException {
		List listaProcessos = null;		

		if (usuarioDt != null)
			Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
					
			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;
												
			//tratamento de segredo de justiça
			if (buscaProcesso.isMesmaServentia(usuarioDt.getId_Serventia()) || buscaProcesso.getProcessoNumeroToInt()==-1){
				buscaProcesso.setSegredoJustica("");
			}
			
			// Consulta os processos			
			if (usuarioDt.isServentiaTipo2Grau() || usuarioDt.isGabineteUPJ()) {
				if(verificarParametrosConsultaProcessoSegundoGrau(buscaProcesso, "", usuarioDt)) {
					listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), null, buscaProcesso.getSegredoJustica(), "", buscaProcesso.getId_Classificador(), posicao);
				}
			} else {
				listaProcessos = obPersistencia.consultarProcessos(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), null, buscaProcesso.getSegredoJustica(), buscaProcesso.getId_Classificador(), posicao);
			}

			setQuantidadePaginas(listaProcessos);

			if (listaProcessos != null) {
				for (int i = 0; i < listaProcessos.size(); i++) {
					ProcessoDt objProcesso = (ProcessoDt) listaProcessos.get(i);
					
					if (objProcesso != null && (objProcesso.isSegredoJustica() || objProcesso.isSigiloso() || objProcesso.hasVitima())) {
						if(!this.podeAcessarProcesso(usuarioDt, objProcesso,obFabricaConexao)) {
							for (int j = 0; j < objProcesso.getPartesProcesso().size(); j++) {
								ProcessoParteDt parteDt = (ProcessoParteDt) objProcesso.getPartesProcesso().get(j);
								if((objProcesso.isSegredoJustica() || objProcesso.isSigiloso() || parteDt.isVitima())) {
									parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
								}
							}
						}
					}
				}
			}
			
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public List consultarTodosProcessosWebservice(String processoNumero, UsuarioNe usuario) throws Exception, MensagemException {
		List listaProcessos = null;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stDigito = "";
			String stAno = "";

			processoNumero = processoNumero.replaceAll("-", ".").replaceAll(",", ".");
			String[] stTemp = processoNumero.split("\\.");
			if (stTemp.length >= 1) processoNumero = stTemp[0];
			else
				processoNumero = "";
			if (stTemp.length >= 2) stDigito = stTemp[1];
			if (stTemp.length >= 3) stAno = stTemp[2];
			listaProcessos = obPersistencia.consultarProcessos("", "", -1,Funcoes.StringToInt(processoNumero,-1), Funcoes.StringToInt(stDigito,-1), Funcoes.StringToInt(stAno,-1), "", "", "", "", "", "", "");
			setQuantidadePaginas(listaProcessos);
			
			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);
				processoDt.setHash(usuario.getCodigoHash(processoDt.getId()));
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Método responsável em consultar os processos de um Juiz de acordo com
	 * parâmetros passados.
	 * 
	 * @param nomeParte
	 *            , filtro de nome da parte
	 * @param pesquisarNomeExato
	 *            , determina se a pesquisa de nome parte será por nome exato ou
	 *            não
	 * @param cpfCnpjParte
	 *            , filtro do cpf ou cnj da parte
	 * @param processoNumero
	 *            , filtro do número do processo
	 * @param statusCodigo
	 *            , filtro de status do processo
	 * @param id_ProcessoTipo
	 *            , filtro de tipo de processo
	 * @param id_Serventia
	 *            , identificação da serventia do processo que deseja consultar
	 * @param id_Comarca
	 *            , identificação da comarca para filtrar os processos
	 * @param SegredoJustica
	 *            , define se devem ser retornados os processos com segredo de
	 *            justiça
	 * @param serventiaTipoCodigo
	 *            , tipo da serventia
	 * @param grupoCodigo
	 *            , grupo do usuário que está efetuando a consulta
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosJuiz(BuscaProcessoDt buscaProcesso, UsuarioDt usuarioDt, String posicao) throws Exception {
		List listaProcessos = null;		

		// int grupo = -1;
		int grupoTipo = -1;
		if (usuarioDt != null) grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
						
			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;
						
			switch (grupoTipo) {
    			// case GrupoDt.JUIZES_VARA:
    			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
    				listaProcessos = obPersistencia.consultarProcessosJuiz(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), usuarioDt.getId_ServentiaCargo(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), posicao);
    				break;
    
    			// case GrupoDt.JUIZES_TURMA_RECURSAL:
    			case GrupoTipoDt.JUIZ_TURMA:
    				if(verificarParametrosConsultaProcessoSegundoGrau(buscaProcesso,  usuarioDt.getId_ServentiaCargo(), usuarioDt)) {
    					listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), null, buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargo(), "", posicao);
    				}
    				break;
    
    			// case GrupoDt.ASSISTENTES_JUIZES_VARA:
    			// listaProcessos = obPersistencia.consultarProcessosJuiz(nomeParte,
    			// pesquisarNomeExato, cpfCnpjParte, processoNumero, stDigito,
    			// stAno, usuarioDt.getId_ServentiaCargoUsuarioChefe(),
    			// statusCodigo, id_Serventia, posicao);
    			// break;
    			//
    			// case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
    			// listaProcessos =
    			// obPersistencia.consultarProcessosSegundoGrau(nomeParte,
    			// pesquisarNomeExato, cpfCnpjParte, processoNumero, stDigito,
    			// stAno, statusCodigo, id_Serventia, null, SegredoJustica,
    			// usuarioDt.getId_ServentiaCargoUsuarioChefe(), null, posicao);
    			// break;
    			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:			
    				switch (Funcoes.StringToInt(usuarioDt.getGrupoUsuarioChefe())) {
    				case GrupoDt.JUIZES_VARA:
    				case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
    				case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
    				case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:					
    				case GrupoDt.JUIZ_EXECUCAO_PENAL:
    					listaProcessos = obPersistencia.consultarProcessosJuiz(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), usuarioDt.getId_ServentiaCargoUsuarioChefe(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), posicao);
    					break;
    				case GrupoDt.JUIZES_TURMA_RECURSAL:
    					if(verificarParametrosConsultaProcessoSegundoGrau(buscaProcesso,  usuarioDt.getId_ServentiaCargoUsuarioChefe(), usuarioDt)) {
    						listaProcessos = obPersistencia.consultarProcessosSegundoGrau(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_ProcessoTipo(), buscaProcesso.getId_Serventia(), null, buscaProcesso.getSegredoJustica(), usuarioDt.getId_ServentiaCargoUsuarioChefe(), "", posicao);
    					}
    					break;
    				}

				break;
			}

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Método responsável em consultar os processos de acordo com parâmetros
	 * passados. Armazena os parâmetros em variáveis locais, para que ao usar a
	 * paginação a consulta não perca os filtros preenchidos pelo usuario.
	 * 
	 * @param nomeParte
	 *            , filtro de nome da parte
	 * @param cpfCnpjParte
	 *            , filtro do cpf ou cnj da parte
	 * @param processoNumero
	 *            , filtro do número do processo
	 * @param statusCodigo
	 *            , filtro de status do processo
	 * @param serventiaTipoCodigo
	 *            , tipo da serventia
	 * @param pesquisarNomeParteExato
	 *            , boolean que determina se a busca do nome da parte deve ser
	 *            ou não exato
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula, tamaralsantos
	 */
	public List consultarProcessosPublica(BuscaProcessoDt buscaProcesso, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
						
			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;
			
			//NÃO SERÁ MAIS UTILIZADO VIEW_BUSCA_PROC TRAZ TODOS OS PROCESSOS.
			///if (serventiaTipoCodigo == ServentiaTipoDt.SEGUNDO_GRAU)
				//listaProcessos = obPersistencia.consultarProcessosPublicaSegundoGrau(nomeParte, cpfCnpjParte, buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(),  segredoJustica, pesquisarNomeParteExato, posicao);
			//else
			listaProcessos = obPersistencia.consultarProcessosPublica(buscaProcesso.getNomeParte(),  buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(),   buscaProcesso.getPesquisarNomeExato(), posicao);

			setQuantidadePaginas(listaProcessos);

			// Se processo é Segredo de Justiça retorna somente as inicias do
			// nome
			if (listaProcessos != null) {
				for (int i = 0; i < listaProcessos.size(); i++) {
					ProcessoDt objProcesso = (ProcessoDt) listaProcessos.get(i);
					if (objProcesso.getSegredoJustica().equals("true")) {
						for (int j = 0; j < objProcesso.getPartesProcesso().size(); j++) {
							ProcessoParteDt parteDt = (ProcessoParteDt) objProcesso.getPartesProcesso().get(j);
							parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
						}
						ServentiaDt serventiaDt = this.consultarIdServentia(objProcesso.getId_Serventia());
						ServentiaCargoDt serventiaCargoResponsavel = new ServentiaCargoDt();
						if(serventiaDt.isSegundoGrau()) {
							serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarRelatorResponsavelProcessoSegundoGrau(objProcesso.getId(), obFabricaConexao);	
						} else if(serventiaDt.isTurma()) {
							serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarServentiaCargoResponsavelProcesso(objProcesso.getId(), null, String.valueOf(GrupoTipoDt.JUIZ_TURMA), true);
						} else {
							serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarServentiaCargoResponsavelProcesso(objProcesso.getId(), null, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), true);
						}
						objProcesso.setServentiaCargoResponsavelDt(serventiaCargoResponsavel);
					}
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processo de acordo com parâmetros
	 * passado.
	 * 
	 * @param processoNumero
	 *            , filtro do número do processo
	 * 
	 * @author lsbernardes
	 */
	public boolean verificarExistenciaProcesso(String processoNumero, boolean desconsideraExecPen) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stDigito = "";
			String stAno = "";

			String[] stTemp = processoNumero.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
			
			if (stTemp.length < 2){
				throw new MensagemException("Erro ao Consultar Processo.");
			} else {
				processoNumero = stTemp[0];
				stDigito = stTemp[1];
				stAno = stTemp[2];
			} 

			retorno = obPersistencia.verificarExistenciaProcesso( processoNumero, stDigito, stAno, desconsideraExecPen);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

	/**
	 * Consulta os processos de um advogado. Retorna os processos em que o
	 * advogado (id_UsuarioServentia passado como parâmetro) está habilitado
	 * seja como Advogado Particular, Defensor ou Procurador.
	 * 
	 * @param id_UsuarioServentia
	 *            identificação do usuário advogado
	 * @param nomeParte
	 *            filtro de nome da parte
	 * @param pesquisarNomeParteExato
	 *            , boolean que determina se a busca do nome da parte deve ser
	 *            ou não exato
	 * @param cpfCnpjParte
	 *            filtro do cpf ou cnj da parte
	 * @param processoNumero
	 *            filtro do número do processo
	 * @param statusCodigo
	 *            código do status do processo
	 * @param id_Serventia
	 *            identificação da serventia do processo que deseja consultar
	 * @param id_ServentiaSubTipo
	 *            identificação do subTipo de serventia a ser filtrado
	 * @param posicao
	 *            parametro para paginação
	 * @param quantidadeRegistrosPagina
	 * 			  quantidade de registros por página
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosAdvogado(BuscaProcessoDt buscaProcesso, String posicao, String quantidadeRegistrosPagina) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String processoNumero="";
			String stDigito = "";
			String stAno = "";

 			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;
						
			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosAdvogado(buscaProcesso.getId_UsuarioServentia(), buscaProcesso.getId_ServentiaUsuario() , buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_ServentiaSubTipo(), posicao, quantidadeRegistrosPagina);

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Consulta os processos ativos em uma delegacia, que são aqueles onde a
	 * serventia de origem é a delegacia do usuário e o tipo do processo ainda é
	 * TCO.
	 * 
	 * @param nomeParte
	 *            filtro de nome da parte
	 * @param pesquisarNomeParteExato
	 *            , boolean que determina se a busca do nome da parte deve ser
	 *            ou não exato
	 * @param cpfCnpjParte
	 *            filtro do cpf ou cnj da parte
	 * @param processoNumero
	 *            filtro do número do processo
	 * @param tcoNumero
	 *            filtro do número do TCO
	 * @param id_Serventia
	 *            identificação da delegacia
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosDelegacia(BuscaProcessoDt buscaProcesso, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
 
 			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;					

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosDelegacia(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getTcoNumero(), buscaProcesso.getId_ServentiaUsuario(), buscaProcesso.getId_Classificador(), posicao);

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Consulta os processos que um promotor pode visualizar, ou seja, aqueles
	 * onde ele é responsável
	 * 
	 * @param nomeParte
	 *            filtro de nome da parte
	 * @param pesquisarNomeParteExato
	 *            , boolean que determina se a busca do nome da parte deve ser
	 *            ou não exato
	 * @param cpfCnpjParte
	 *            filtro do cpf ou cnj da parte
	 * @param processoNumero
	 *            filtro do número do processo
	 * @param id_ServentiaCargo
	 *            serventiaCargo do promotor
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula, tamaralsantos
	 */
	public List consultarProcessosPromotor(BuscaProcessoDt buscaProcesso, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
 
 			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosPromotor(buscaProcesso.getNomeParte(), buscaProcesso.getPesquisarNomeExato(), buscaProcesso.getCpfCnpjParteToLong(), buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), buscaProcesso.getId_ServentiaCargo(), buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_Serventia(), buscaProcesso.getId_ServentiaUsuario(), posicao);

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public List consultarProcessosDefensorPublicoProcuradorAdvogado(String idUsuarioServentia) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosDefensorPublicoProcuradorAdvogado(idUsuarioServentia);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/* Não será mais utilizado, vai usar a consulta de promotor.
	public List consultarProcessosPromotoria(String nomeParte, String pesquisarNomeExato, String cpfCnpjParte, String processoNumero, String statusCodigo, String id_Promotoria, String id_Classificador, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stDigito = "";
			String stAno = "";

			if (nomeParte == null || nomeParte.length() == 0) nomeParte = stUltimoNomeParte;
			if (pesquisarNomeExato == null || pesquisarNomeExato.length() == 0) pesquisarNomeExato = stUltimoPesquisarNomeExato;
			if (cpfCnpjParte == null || cpfCnpjParte.length() == 0) cpfCnpjParte = stUltimoCpfCnpjParte;
			if (processoNumero == null || processoNumero.length() == 0) processoNumero = stUltimoProcessoNumero;
			else {
				String[] stTemp = processoNumero.split("\\.");
				if (stTemp.length >= 1) processoNumero = stTemp[0];
				else
					processoNumero = "";
				if (stTemp.length >= 2) stDigito = stTemp[1];
				if (stTemp.length >= 3) stAno = stTemp[2];
			}
			if (statusCodigo == null || statusCodigo.length() == 0) statusCodigo = stUltimoProcessoStatusCodigo;
			if (id_Promotoria == null || id_Promotoria.length() == 0) id_Promotoria = stUltimoId_Serventia;

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosPromotoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), statusCodigo, id_Promotoria, id_Classificador, posicao);

			// Armazena variáveis da última busca
			stUltimoNomeParte = nomeParte;
			stUltimoCpfCnpjParte = cpfCnpjParte;
			stUltimoProcessoNumero = processoNumero;
			stUltimoDigito = stDigito;
			stUltimoAno = stAno;
			stUltimoId_Serventia = id_Promotoria;
			stUltimoProcessoStatusCodigo = statusCodigo;
			stUltimoPesquisarNomeExato = pesquisarNomeExato;

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	*/

	/* Não será mais utilizado, vai usar a consulta de advogado.
	public List consultarProcessosProcuradoria(String nomeParte, String pesquisarNomeExato, String cpfCnpjParte, String processoNumero, String statusCodigo, String id_Promotoria, String id_Classificador, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stDigito = "";
			String stAno = "";

			if (nomeParte == null || nomeParte.length() == 0) nomeParte = stUltimoNomeParte;
			if (pesquisarNomeExato == null || pesquisarNomeExato.length() == 0) pesquisarNomeExato = stUltimoPesquisarNomeExato;
			if (cpfCnpjParte == null || cpfCnpjParte.length() == 0) cpfCnpjParte = stUltimoCpfCnpjParte;
			if (processoNumero == null || processoNumero.length() == 0) processoNumero = stUltimoProcessoNumero;
			else {
				String[] stTemp = processoNumero.split("\\.");
				if (stTemp.length >= 1) processoNumero = stTemp[0];
				else
					processoNumero = "";
				if (stTemp.length >= 2) stDigito = stTemp[1];
				if (stTemp.length >= 3) stAno = stTemp[2];
			}
			if (statusCodigo == null || statusCodigo.length() == 0) statusCodigo = stUltimoProcessoStatusCodigo;
			if (id_Promotoria == null || id_Promotoria.length() == 0) id_Promotoria = stUltimoId_Serventia;

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosProcuradoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), statusCodigo, id_Promotoria, id_Classificador, posicao);

			// Armazena variáveis da última busca
			stUltimoNomeParte = nomeParte;
			stUltimoCpfCnpjParte = cpfCnpjParte;
			stUltimoProcessoNumero = processoNumero;
			stUltimoDigito = stDigito;
			stUltimoAno = stAno;
			stUltimoId_Serventia = id_Promotoria;
			stUltimoProcessoStatusCodigo = statusCodigo;
			stUltimoPesquisarNomeExato = pesquisarNomeExato;

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	*/

	/**
	 * Consulta os processos ativos no Escritório Jurídico.
	 * 
	 * @param nomeParte
	 * @param pesquisarNomeExato
	 * @param cpfCnpjParte
	 * @param processoNumero
	 * @param statusCodigo
	 * @param id_Promotoria
	 * @param posicao
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho tamaralsantos
	 */
	/* NÃO SERÁ MAIS UTILIZADO
	public List consultarProcessosEscritorioJuridico(String nomeParte, String pesquisarNomeExato, String cpfCnpjParte, String processoNumero, String statusCodigo, String id_Promotoria, String id_Classificador, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stDigito = "";
			String stAno = "";

			if (nomeParte == null || nomeParte.length() == 0) nomeParte = stUltimoNomeParte;
			if (pesquisarNomeExato == null || pesquisarNomeExato.length() == 0) pesquisarNomeExato = stUltimoPesquisarNomeExato;
			if (cpfCnpjParte == null || cpfCnpjParte.length() == 0) cpfCnpjParte = stUltimoCpfCnpjParte;
			if (processoNumero == null || processoNumero.length() == 0) processoNumero = stUltimoProcessoNumero;
			else {
				String[] stTemp = processoNumero.split("\\.");
				if (stTemp.length >= 1) processoNumero = stTemp[0];
				else
					processoNumero = "";
				if (stTemp.length >= 2) stDigito = stTemp[1];
				if (stTemp.length >= 3) stAno = stTemp[2];
			}
			if (statusCodigo == null || statusCodigo.length() == 0) statusCodigo = stUltimoProcessoStatusCodigo;
			if (id_Promotoria == null || id_Promotoria.length() == 0) id_Promotoria = stUltimoId_Serventia;

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosEscritorioJuridico(nomeParte, pesquisarNomeExato, cpfCnpjParte, buscaProcesso.getProcessoNumeroToInt(), buscaProcesso.getDigito(), buscaProcesso.getAno(), statusCodigo, id_Promotoria, id_Classificador, posicao);

			// Armazena variáveis da última busca
			stUltimoNomeParte = nomeParte;
			stUltimoCpfCnpjParte = cpfCnpjParte;
			stUltimoProcessoNumero = processoNumero;
			stUltimoDigito = stDigito;
			stUltimoAno = stAno;
			stUltimoId_Serventia = id_Promotoria;
			stUltimoProcessoStatusCodigo = statusCodigo;
			stUltimoPesquisarNomeExato = pesquisarNomeExato;

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	*/

	/**
	 * Consulta os processos onde um ServentiaCargo é responsável
	 * 
	 * @param usuarioNe
	 *            , usuário logado, foi necessário passar o NE para gerar código
	 *            hash
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosServentiaCargo(UsuarioNe usuarioNe, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosServentiaCargo(usuarioNe, posicao);

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Consulta os processos onde um Advogado é responsável
	 * 
	 * @param usuarioNe
	 *            , usuário logado, foi necessário passar o NE para gerar código
	 *            hash
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula, tamaralsantos
	 */
	/* NÃO SERÁ MAIS UTILIZADO, SERÁ UTILIZADO DO WEBSERVICE
	 * public List consultarProcessosAdvogado(UsuarioNe usuarioNe, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosAdvogadoWebService(usuarioNe, posicao);

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}*/

	/**
	 * Consulta os processos onde um ServentiaCargo ou Advogado é responsável
	 * 
	 * @param usuarioNe
	 *            , usuário logado, foi necessário passar o NE para gerar código
	 *            hash
	 * @param posicao
	 *            parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosPromotorAdvogado(UsuarioNe usuarioNe, String posicao) throws Exception {
		List listaProcessos = null;
		if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo()) == GrupoDt.MINISTERIO_PUBLICO ) {
			listaProcessos = this.consultarProcessosServentiaCargo(usuarioNe, posicao);
		} else if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO) {
			listaProcessos = this.consultarProcessosAdvogadoWebService(usuarioNe, "", "", posicao);
		}
		return listaProcessos;
	}

	/**
	 * Verifica se existem processos onde o ServentiaCargo passado é responsável
	 * 
	 * @param id_serventiaCargo
	 *            , identificação do cargo
	 * 
	 * @author msapaula
	 */
	public boolean verificaProcessosServentiaCargo(String id_ServentiaCargo, FabricaConexao obFabricaConexao) throws Exception {
		boolean boRetorno = false;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		int qtde = obPersistencia.consultarProcessosServentiaCargo(id_ServentiaCargo);
		if (qtde > 0) boRetorno = true;		
		return boRetorno;
	}

	/**
	 * Método que consulta a última serventia para a qual um processo foi
	 * distribuído
	 * 
	 * @author jrcorrea
	 */
	public int[] consultarUltimaDistribuicao(String id_AreaDistribuicao) throws Exception {
		int inUltimo[];
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			inUltimo = obPersistencia.consultarUltimaDistribuicao(id_AreaDistribuicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return inUltimo;
	}

	/**
	 * Método responsável em consultar o último número de processo no banco de
	 * dados e devolver a próxima numeração que deve ser usada
	 * 
	 * @author jrcorrea
	 */
	public String consultarUltimoNumeroProcesso() throws Exception {
		String ultimoNumero;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			ultimoNumero = obPersistencia.consultarUltimoProcesso();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return ultimoNumero;
	}

	/**
	 * Método que verifica se usuário pode modificar dados de processo
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @param usuarioDt
	 *            usuário que vai modificar dados
	 * 
	 * @author msapaula
	 */
	public String podeModificarDados(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		String stMensagem = "";
		//Verifica se o usuário é responsável pelo processo.
		if(!this.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId())) {
			// Se o usuário não for responsável pelo processo e for de serventia diferente do processo, não poderá modificar dados
			if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) {
				stMensagem += "Sem permissão para modificar dados de processo de outra serventia.";
			}
		}
		/**
         * Solicitação de verificação de permissão para alteração quando um processo está arquivado somente para processos cíveis
         * feito por Jesus para Cássio Nogueira em 8/9/2020
         */
		if (processoDt.isArquivado() && !processoDt.isCriminal()){
			stMensagem += " Não é possível executar essa ação, processo está arquivado. \n";
		}else if (processoDt.isErroMigracao()){
			stMensagem += " Não é possível executar essa ação, processo está com ERRO DE MIGRAÇÃO. \n";
		}
		
//		if (Funcoes.StringToInt(processoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA) stMensagem += "Não é possível executar essa ação. Motivo: Processo físico!\n";

		return stMensagem;
	}

	/**
	 * Método que verifica se usuário pode apensar um processo
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @param usuarioDt
	 *            usuário que vai apensar processo
	 * 
	 * @author msapaula
	 */
	public String podeApensarProcesso(ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		String stMensagem = "";
		// Se usuário for de serventia diferente do processo, não poderá
		// modificar dados
		if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia()) && !isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId())) {
			stMensagem += "Sem permissão para apensar esse processo.";
		}
		/**
         * Solicitação de verificação de permissão para alteração quando um processo está arquivado somente para processos cíveis
         * feito por Jesus para Cássio Nogueira em 8/9/2020
         */
		if (processoDt.isArquivado() && !processoDt.isCriminal()){
			stMensagem += " Não é possível executar essa ação, processo está arquivado. \n";
		}else if (processoDt.isErroMigracao()){
			stMensagem += " Não é possível executar essa ação, processo está com ERRO DE MIGRAÇÃO. \n";
		}

		return stMensagem;
	}

	/**
	 * Limpa as variavéis de consulta de processos
	 */
	public void limparVariaveisConsulta() {
		if (objBuscaProcessoDt==null) {
			objBuscaProcessoDt = new BuscaProcessoDt();	
		}else {
			objBuscaProcessoDt.limpar();
		}				
	}

	/**
	 * Consulta quais os processos são apensos de um processo passado por parâmetro.
	 * 
	 * @param String id_processo, id do processo
	 * @return lista de processos apensos
	 * @author msapaula, hmgodinho
	 */
	public List consultarProcessosApensos(String id_processo) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosApensosDependentes(id_processo, true);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * Consulta quais os processos são apensos E dependentes de um processo passado por parâmetro.
	 * 
	 * @param String id_processo, id do processo
	 * @return lista de processos apensos e dependentes
	 * @author msapaula, hmgodinho
	 */
	public List consultarProcessosApensosEDependentes(String id_processo) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosDependentes(id_processo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	
	/**
	 * Consulta os processos apensos passíveis de serem redistribuídos. 
	 * @param id_processo - ID do processo pai
	 * @return lista de processos apensos que podem ser redistribuídos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosApensosRedistribuicao(String id_processo) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosApensosRedistribuicao(id_processo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	
	/**
	 * Método que realiza consulta de processos apensos ao processo informado.
	 * @param id_processo - id do processo
	 * @return lista de processos dependentes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosApensos(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		List listaProcessos = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		listaProcessos = obPersistencia.consultarProcessosApensosDependentes(id_processo, true);
		return listaProcessos;
	}
	
	/**
	 * Método que realiza consulta de processos dependentes ao processo informado.
	 * @param id_processo - id do processo
	 * @param obFabricaConexao - conexão
	 * @return lista de processos dependentes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosDependentes(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		List listaProcessos = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		listaProcessos = obPersistencia.consultarProcessosApensosDependentes(id_processo, false);
		return listaProcessos;
	}
	
	/**
	 * Método que realiza consulta de processos dependentes ao processo informado.
	 * @param id_processo - id do processo
	 * @return lista de processos dependentes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosDependentes(String id_processo) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosDependentes(id_processo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	
	/**
	 * Método que verifica se um processo possui um ou mais processos apensos
	 * 
	 * @author msapaula
	 * @param String
	 *            id_processo, id do processo
	 */
	public boolean verificaProcessosApensos(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		boolean temApensos = false;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		temApensos = obPersistencia.verificaProcessosApensos(id_processo);
		return temApensos;
	}

	/**
	 * Realiza chamada ao método responsável em apensar/criar dependência de um processo a outro
	 * 
	 * @param id_Processo: processo apenso
	 * @param id_ProcessoPrincipal: processo principal
	 * @author msapaula, hmgodinho
	 */
	public void apensarCriarDependenciaProcesso(ProcessoDt processoDt, String id_ProcessoPrincipal, boolean apenso) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		obDados = this.consultarId(processoDt.getId());
		registrarProcessoApensoDependente(processoDt.getId(), id_ProcessoPrincipal, apenso);
		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			if (!processoDt.getId_Processo().equalsIgnoreCase("")) {				
				logDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoDt.getPropriedades());
			}
			obDados.copiar(processoDt);
			obLog.salvar(logDt, obFabricaConexao);			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Realiza chamada ao método responsável por desapensar/cancelar dependência de um processo
	 * 
	 * @param id_ProcessoApenso: id do processo o qual deseja desapensar, ou seja retirar do 
	 *            banco o id_ProcessoDependente e marcar Apenso como false
	 * @author msapaula, hmgodinho
	 */
	public void desapensarCancelarDependenciaProcesso(ProcessoDt processoDt, String id_ProcessoPrincipal, boolean apenso) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		obDados = this.consultarId(processoDt.getId());
		registrarProcessoApensoDependente(processoDt.getId(), null, false);
		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();		
			if (!processoDt.getId_Processo().equalsIgnoreCase("")) {				
				logDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoDt.getPropriedades());
			}
			obDados.copiar(processoDt);
			obLog.salvar(logDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que realiza o apensamento/desapensamento de um processo
	 * 
	 * @param id_Processo: processo a ser apensado ou desapensado
	 * @param id_ProcessoPrincipal: processo q irá receber o apenso
	 * @author msapaula, hmgodinho
	 */
	public void registrarProcessoApensoDependente(String id_Processo, String id_ProcessoPrincipal, boolean apenso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			obPersistencia.registrarProcessoApensoDependente(id_Processo, id_ProcessoPrincipal, apenso);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método para verificar se determinado processo já está apensado a um outro
	 * 
	 * @param id_Processo
	 *            : id do processo a verificar se já é apensado
	 * 
	 * @author msapaula
	 */
	public boolean verificaProcessoApensado(String id_Processo) throws Exception {
		boolean apenso = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			String stTemp = obPersistencia.verificaProcessoApensado(id_Processo);
			if (stTemp.equalsIgnoreCase("true")) apenso = true;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return apenso;
	}

	/**
	 * Método que verifica se um determinado tco já está cadastrado para a
	 * serventia externa passada
	 * 
	 * @param id_Serventia
	 *            , id_Serventia da delegacia a verificar o tco
	 * @param tcoNumero
	 *            , numero do tco a ser verificado
	 * 
	 * @author msapaula
	 */
	public boolean verificarTcoCadastrado(String id_Serventia, String tcoNumero) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.verificarTcoCadastrado(id_Serventia, tcoNumero);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Método responsável em cancelar o cadastro de um processo. Apaga os id's
	 * que tenham sido setados para os objetos
	 */
	protected void cancelarCadastroProcesso(ProcessoCadastroDt processoDt) throws Exception {
		// Limpa id's dos objetos
		processoDt.setId("");
		if (processoDt.getPartesProcesso() != null) {
			List lista = processoDt.getPartesProcesso();
			for (int i = 0; i < lista.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) lista.get(i);
				parteDt.setId("");
				if (parteDt.getEnderecoParte() != null && !parteDt.ParteNaoPersonificavel()) parteDt.getEnderecoParte().setId("");
				if (parteDt.getAdvogadoDt() != null) parteDt.getAdvogadoDt().setId("");
			}
		}
		if (processoDt.getListaAssuntos() != null) {
			List lista = processoDt.getListaAssuntos();
			for (int i = 0; i < lista.size(); i++) {
				ProcessoAssuntoDt processoAssuntoDt = (ProcessoAssuntoDt) lista.get(i);
				processoAssuntoDt.setId("");
			}
		}

		if (processoDt.getListaArquivos() != null) {
			List lista = processoDt.getListaArquivos();
			for (int i = 0; i < lista.size(); i++) {
				ArquivoDt arquivoDt = (ArquivoDt) lista.get(i);
				arquivoDt.setId("");
				if (arquivoDt.getRecibo().equalsIgnoreCase("true")) {
					arquivoDt.setRecibo("false");
					arquivoDt.setArquivo(Signer.extrairP7sRecibo(arquivoDt.conteudoBytes()));
				}
			}
		}
	}

	//para o cadastro sem guias (Sobrecarga)
	public void cadastrarProcessoSegundoGrau(ProcessoCadastroDt processoDt, UsuarioDt usuarioDt) throws Exception {
		//para o cadastro
		cadastrarProcessoSegundoGrau(processoDt,usuarioDt, null);
	}
	/**
	 * Método que realiza o cadastro de ações do 2º grau
	 * 
	 * @param processoDt
	 *            , dt com dados da ação a ser cadastrada
	 * @param usuarioDt
	 *            , usuário que está efetuando o cadasttro
	 * 
	 * @author msapaula, jrcorrea 30/10/2017
	 */
	
	public void cadastrarProcessoSegundoGrau(ProcessoCadastroDt processoDt, UsuarioDt usuarioDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		ServentiaDt serventiaSegundoGrauDt = null;
		String id_Serventia = null;
		String id_ServentiaCargoRelator = null;		
		String stComplemento = " - Normal ";
		String[] id_ServentiaPrevencaoDataArquivamento = null;
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		obFabricaConexao.iniciarTransacao();
		
		try {						
			
			LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
			
			if (processoDt.isProcessoDependente()) {
				//Se for processo dependente, busca o relator do processo pai para ativá-lo como relator do processo filho
				// para cumprir o provimento 16/2012 da CGJ
				id_Serventia = 	processoDt.getProcessoDependenteDt().getId_Serventia();
				processoDt.setId_ProcessoPrincipal(processoDt.getProcessoDependenteDt().getId());
				serventiaSegundoGrauDt = new ServentiaNe().consultarId(id_Serventia);
				
				ServentiaCargoDt serventiaCargoRelator = null;
				stComplemento = " (Normal)";
				
				if(!serventiaSegundoGrauDt.isTurma() && !serventiaSegundoGrauDt.isSegundoGrau()){
					//CENÁRIO 1:
					//Se a serventia do processo pai não for Câmara nem Turma Recursal, o sorteio da distribuição é feito
					//considerando a área de distribuição recursal do processo pai.
					//Neste caso, o processo não será distribuido por dependência (será distribuição normal) e o sorteio do relator será feito mais embaixo no código.
					id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao());
				} else {
					//CENÁRIO 2:
					//O processo pai está numa Câmara ou Turma Recursal. Neste caso o processo é distribuído por Dependência para a mesma
					//serventia do processo pai e para o mesmo relator.
					serventiaCargoRelator = new ProcessoResponsavelNe().consultarRelator2Grau(processoDt.getProcessoDependenteDt().getId(), obFabricaConexao);
					if (serventiaCargoRelator != null && serventiaCargoRelator.getId().length()>0){
						id_ServentiaCargoRelator = serventiaCargoRelator.getId();
						stComplemento = " (Dependencia)";
					}
				}
					
			} else {
				//Se não houver dependência a distribuição será normal
				//para cumprir o provimento  16/2012 da CGJ
				id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao());
			}
			
			// Consulta dados do processo prioridade
			if( processoDt.getId_ProcessoPrioridade() != null ) {
				ProcessoPrioridadeDt processoPrioridadeDt = new ProcessoPrioridadeNe().consultarId(processoDt.getId_ProcessoPrioridade());
				if( processoPrioridadeDt != null && !processoPrioridadeDt.getProcessoPrioridadeCodigo().isEmpty() ) {
					processoDt.setProcessoPrioridadeCodigo(processoPrioridadeDt.getProcessoPrioridadeCodigo());
				}
			}
			
			// Consulta dados da serventia
			if (id_Serventia != null) {
				processoDt.setServentiaDt(new ServentiaNe().consultarId(id_Serventia));
			}else{
				throw new MensagemException("Não foi possível localizar uma serventia ativa para cadastrar este processo. Entre em contato com o suporte.");
			}
									
			//o plantao deve funcionar somente após as 19h ou em dia não útil
			if (processoDt.getServentiaDt().isPlantaoSegundoGrau()){				
				PrazoSuspensoNe ps = new PrazoSuspensoNe();
				Calendar dataAtual = Calendar.getInstance();
				if (dataAtual.get(Calendar.HOUR_OF_DAY)>12 && dataAtual.get(Calendar.HOUR_OF_DAY)<19 && dataAtual.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY && dataAtual.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
					
					//verifico se é uma data valida na comarca, na cidade e serventia
					// o metodo isDataValidaProtocolo foi cridado devido o bo 2018/53
					//passo a hora e os minutos  para 0 hora/minutos do dia
					
					//Atualizacao 07/01/2021 - Turmo único das 12 as 19 horas
					dataAtual.set(Calendar.HOUR_OF_DAY,0);
					dataAtual.set(Calendar.MINUTE,0);	
					dataAtual.set(Calendar.SECOND,0);
					dataAtual.set(Calendar.MILLISECOND,0);
					boolean boData = ps.isDataValidaProtocolo(dataAtual.getTime(), processoDt.getServentiaDt());
					if (boData){
						throw new MensagemException("O plantão deve ser utilizado após as 19h ou em dia não útil para o Poder Judiciário.");
					}					
				}			
			}
			
			// Captura próxima numeração de processo, baseado no código da comarca
			String numero = ProcessoNumero.getInstance().getProcessoNumero(processoDt.getForumCodigo());
			processoDt.setAnoProcessoNumero(numero);
			
			// Valida se já existe um processo com o número gerado / informado
			validaExistenciaProcessoNumeroCompleto(processoDt, obFabricaConexao);

			// Seta dados do processo: fase conhecimento, status Ativo
			processoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
			processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
			
			//Antes de salvar o processo é preciso consultar se haverá conexão com algum outro processo.
			//Se fizer essa verificação depois de salvar o processo, a verificação encontrará o processo que está sendo cadastrado.
			id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoDt, obFabricaConexao); 

			// Seta status do como ativo ou sigiloso caso tenha sido essa a opção escolhida no cadastro do processo
			if(processoDt.getSigiloso().equals("true")){
				processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.SIGILOSO));
			}else{
				processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
			}
			
			//validação de cadastro com guia
			validacaoAntiFraudeDadosGuia(processoDt, guiaEmissaoDt, obFabricaConexao);
			
			// Salva processo
			salvar(processoDt, obFabricaConexao);

			// Salvando partes do processo
			new ProcessoParteNe().salvarPartesProcesso(processoDt.getPartesProcesso(), processoDt.getId(), logDt, obFabricaConexao);

			// Salvando advogados incluídos
			new ProcessoParteAdvogadoNe().salvarAdvogadosPartesPromoventes(processoDt.getListaPolosAtivos(), processoDt.getListaAdvogados(), logDt, obFabricaConexao);

			// Salvando assuntos do processo
			processoAssuntoNe.salvarProcessoAssunto(processoDt.getListaAssuntos(), processoDt.getId(), logDt, obFabricaConexao);

			// Distribui recurso para um Relator (ServentiaCargo)
			if (id_ServentiaCargoRelator == null) {
				//para atender o provimento 16/2012
				id_ServentiaCargoRelator = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(processoDt.getId_Serventia(),processoDt.getId_AreaDistribuicao());
			}
			//Salvando a distribuição ou retornando erro caso não consiga encontrar um relator para ser responsável pelo processo
			if (id_ServentiaCargoRelator != null) {
				responsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargoRelator, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
			} else {
				throw new MensagemException("Não foi possível localizar um relator ativo vinculado à serventia " + processoDt.getServentia() + " para vincular a este processo. Entre em contato com o suporte.");
			}

			// Se usuário que está cadastrando é um promotor adiciona ele como responsável pelo processo
			if (usuarioDt.isMp()) {
				responsavelNe.salvarProcessoResponsavel(processoDt.getId(), usuarioDt.getId_ServentiaCargo(), true, CargoTipoDt.MINISTERIO_PUBLICO , logDt, obFabricaConexao);
			}
			
			/* ---------- PONTEIRO ----------------*/
			///salvo o ponteiro
			new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(),PonteiroLogTipoDt.DISTRIBUICAO,processoDt.getId(),processoDt.getId_Serventia(),UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi, new Date(), id_ServentiaCargoRelator  ),obFabricaConexao );
			/* ---------- PONTEIRO ----------------*/
			
			// Gera movimentação PETIÇÃO ENVIADA
			MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacao(processoDt.getId(), MovimentacaoTipoDt.PETICAO_ENVIADA, usuarioDt.getId_UsuarioServentia(), "", logDt, obFabricaConexao);
			//MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacaoPeticaoEnviada(processoDt.getId(), false, usuarioDt, logDt, obFabricaConexao);

			// Salva Arquivos
			MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();
			if(processoDt.getListaArquivos() != null & !processoDt.getListaArquivos().isEmpty()) {
				arquivoNe.inserirArquivosSemRecibo(movimentacaoPeticao.getId(), processoDt.getProcessoNumeroCompleto(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
			} else {
				throw new MensagemException("Processo não possui documentos anexados para peticionamento. Favor rever o Passo 2 do cadastro do processo.");
			}
			
			ServentiaCargoDt relator = new ServentiaCargoNe().consultarId(id_ServentiaCargoRelator);
			stComplemento += " - Distribuído para: " + relator.getNomeUsuario();

			// Gerando a movimentação específica na capa do processo
			if (processoDt.getServentiaDt().getServentiaSubtipoCodigo() != null && !processoDt.getServentiaDt().getServentiaSubtipoCodigo().equals("")) {
				if (processoDt.getServentiaDt().isSegundoGrau()) {
					// Gera movimentação AUTOS DISTRIBUÍDOS
					movimentacaoNe.gerarMovimentacaoAutosDistribuidos(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoDt.getServentiaDt().getServentia() + stComplemento, logDt, obFabricaConexao);
				} else {
					// Gera movimentação AUTOS DISTRIBUÍDOS TURMA RECURSAL
					movimentacaoNe.gerarMovimentacaoRecursoDistribuido(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, processoDt.getServentiaDt().getServentia() + stComplemento, logDt, obFabricaConexao);
				}
			} else {
				throw new MensagemException("Não foi possível determinar o Sub-Tipo da Serventia de Segundo Grau.");
			}

			//se for serventia de plantão faz concluso direto e não cria pendencia de verificar distribuição e verificar novo processo
			if (processoDt.getServentiaDt().isPlantaoSegundoGrau() || processoDt.isSigiloso()){
				
				pendenciaNe.gerarConclusaoDecisao(processoDt, UsuarioServentiaDt.SistemaProjudi, id_ServentiaCargoRelator, processoDt.getProcessoPrioridadeCodigo(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
											
			}else{
				// Gera pendência Verificar Distribuição - no caso das serventias de segundo grau
				if (processoDt.getServentiaDt().isSegundoGrau()) {
					pendenciaNe.gerarPendenciaVerificarDistribuicao(processoDt, UsuarioServentiaDt.SistemaProjudi, movimentacaoPeticao.getId(), logDt, processoDt.getListaArquivos(), obFabricaConexao);
				} else {
					// Gera pendência Verificar Novo Processo
					pendenciaNe.gerarPendenciaVerificarNovoProcesso(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoPeticao.getId(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
				}
				// Gera pendência Verificar prevenção
				if (id_ServentiaPrevencaoDataArquivamento != null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
					pendenciaNe.gerarPendenciaVerificarConexao(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoPeticao.getId(), logDt, obFabricaConexao);
				}
				
				//Se for processo de execução fiscal, deve gerar guia alterar
				if(isProcessoExecucaoFiscal(processoDt)) {
					efetueGeracaoGuiaUnicaFazendaPublica(processoDt, obFabricaConexao);
				}
			}
			
			
			if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().equalsIgnoreCase("")){
				
				if( processoDt.getProcessoNumero() != null) {
					guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				}
				
				/** ***Rollback*** **/
				codigoServentiaParaRollback = Funcoes.completarZeros(processoDt.getServentiaDt().getServentiaCodigoExterno(),3);
				codigoComarcaCodigoParaRollback = processoDt.getServentiaDt().getComarcaCodigo();
				numrProcessoParaRollback = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
				
				idUsuarioLogRollback = processoDt.getId_UsuarioLog();
				idComputadorLogRollback = processoDt.getIpComputadorLog();
				/** ****** **/			
			
				//this.vinculaGuiaProcesso(guiaEmissaoDt, processoDt.getId_Processo(), null, id_Serventia, processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), obFabricaConexao, null);
				
				guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
				
				
				GuiaEmissaoDt guiaSPG = new GuiaSPGNe().consultarGuiaSPGCapitalInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
				GuiaEmissaoDt guiaSSG = new GuiaSSGNe().consultarGuiaEmissaoSSG(guiaEmissaoDt.getNumeroGuiaCompleto());
				if( guiaSPG != null && guiaSSG == null ) {
					this.vinculaGuiaProcesso(guiaEmissaoDt, processoDt.getId_Processo(), null, processoDt.getId_Serventia(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), obFabricaConexao, null);
				} else {
					if( guiaSPG == null && guiaSSG != null ) {
						this.vinculaGuiaSSGProcesso(guiaEmissaoDt, processoDt.getId_Processo(), null, processoDt.getId_Serventia(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), obFabricaConexao, null);
					}
				}								
				
				if (!guiaEmissaoNe.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao)) {
					pendenciaNe.gerarPendenciaVerificarGuiaPendente(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoPeticao.getId(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
				}
			
			}  else if (processoDt.getServentiaDt().isComCustas() && processoDt.isAssistencia() && !processoDt.isCriminal()){
				//Salvar GuiaEmissão
				this.inicializarGuiaInicial(guiaEmissaoDt, processoDt);
				if (this.gerarGuiaInicialProcesso(guiaEmissaoDt, processoDt, usuarioDt)) {
					guiaEmissaoNe.salvarGuiaCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, usuarioDt.getId_UsuarioServentia(), obFabricaConexao);
				}
				
			} else if (processoDt.getServentiaDt().isComCustas() && processoDt.isIsento() && !processoDt.isCriminal()){
				//Salvar GuiaEmissão
				this.inicializarGuiaInicial(guiaEmissaoDt, processoDt);
				if (this.gerarGuiaInicialProcesso(guiaEmissaoDt, processoDt, usuarioDt)) {
					guiaEmissaoNe.salvarGuiaCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, usuarioDt.getId_UsuarioServentia(), obFabricaConexao);
				}
			} else if (processoDt.getServentiaDt().isTurma() && !processoDt.isCriminal() ) { //Ocorrência 2019/6291
				this.inicializarGuiaInicial(guiaEmissaoDt, processoDt);
				if (this.gerarGuiaInicialProcesso(guiaEmissaoDt, processoDt, usuarioDt)) {
					guiaEmissaoNe.salvarGuiaCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, usuarioDt.getId_UsuarioServentia(), obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
			
			if (processoDt.getServentiaDt().isPlantaoSegundoGrau()){				
				enviarEmailAnalistas(processoDt.getServentiaDt(),processoDt.getProcessoNumeroCompleto());
			}
			
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarCadastroProcesso(processoDt);
			if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null){
				//Método para fazer o tratamento de rollback
				guiaEmissaoNe.rollbackAlteracoesGuiaSPGCadastroProcesso(guiaEmissaoDt, codigoServentiaParaRollback, numrProcessoParaRollback, codigoComarcaCodigoParaRollback, idUsuarioLogRollback, idComputadorLogRollback);
			}
			
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}
	
//	/**
//	 * Método que realiza o cadastro de ações do 2º grau
//	 * 
//	 * @param processoDt
//	 *            , dt com dados da ação a ser cadastrada
//	 * @param usuarioDt
//	 *            , usuário que está efetuando o cadasttro
//	 *            
//	 * @param guiaEmissaoDt
//	 *            , guia inicial
//	 *            
//	 * @author lsbernardes
//	 */
//	public void cadastrarProcessoSegundoGrauSemAssistencia(ProcessoCadastroDt processoDt, UsuarioDt usuarioDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
//		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
//		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
//		PendenciaNe pendenciaNe = new PendenciaNe();
//		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
//		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
//		ServentiaDt serventiaSegundoGrauDt = null;
//		String id_Serventia = null;
//		String id_ServentiaCargoRelator = null;		
//		String stComplemento = " - Normal";
//		String stTipoConexaoPrevencao="Serventia";
//		String[] id_ServentiaPrevencaoDataArquivamento = null;
//		ServentiaCargoDt cargosDistribuicao = null;
//		
//		String codigoServentiaParaRollback = null;
//		String numrProcessoParaRollback = null;
//		String idUsuarioLogRollback = null;
//		String idComputadorLogRollback = null;
//		String codigoComarcaCodigoParaRollback = null;
//
//		FabricaConexao obFabricaConexao = null;
//
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			obFabricaConexao.iniciarTransacao();
//
//			LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
//			
//			//Em determinação ao BO 2013/82139 aberto pela Corregedoria em cumprimento ao despacho 3227/2013.
//			//if (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO)
//			//	valideQuantidadeAcoesCadastroProcessoAdvogado(processoDt, usuarioDt, obFabricaConexao);
//			
//			if (processoDt.getProcessoDependenteDt() != null && processoDt.getProcessoDependenteDt().getId_Serventia().length()>0){
//				id_Serventia = 	processoDt.getProcessoDependenteDt().getId_Serventia();
//								
//				// [0] id do processo [1] data de arquivamento [2] numero do processo
//				//se achou o processo, pego o responsável
//				if (processoDt.isProcessoDependenteDt() ){
//					stComplemento = " (Dependencia)"; 
//					ServentiaCargoDt serventiaCargoRelator = new ProcessoResponsavelNe().consultarRelator2Grau(processoDt.getProcessoDependenteDt().getId(), obFabricaConexao);
//					if (serventiaCargoRelator != null && serventiaCargoRelator.getId().length()>0){
//						id_ServentiaCargoRelator = serventiaCargoRelator.getId();
//						stTipoConexaoPrevencao= "Relator";
//						//se hover valor na possicao 1 o processo está arquivado						
//					} else{
//						// Resgata possíveis preventos e consulta a serventia que deverá
//						// receber o processo
//						id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoDt, obFabricaConexao);
//
//						if (id_ServentiaPrevencaoDataArquivamento!=null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
//							
//							if ( id_ServentiaPrevencaoDataArquivamento[1]==null ||  id_ServentiaPrevencaoDataArquivamento[1].length()==0)
//								stComplemento = " (Conexão "+id_ServentiaPrevencaoDataArquivamento[2]+")";
//							else stComplemento = " (Prevenção "+id_ServentiaPrevencaoDataArquivamento[2]+")";
//							
//							id_Serventia = id_ServentiaPrevencaoDataArquivamento[0];
//							
//							if(id_ServentiaPrevencaoDataArquivamento[3] != null){
//								ServentiaDt serventiaRecursoOrigem = this.consultarServentiaOrigemRecurso(id_ServentiaPrevencaoDataArquivamento[3]);
//								if (serventiaRecursoOrigem != null && serventiaRecursoOrigem.getId() != null && serventiaRecursoOrigem.getId().length() > 0)
//									id_Serventia = serventiaRecursoOrigem.getId();
//							}
//						
//						
//						}else{
//							stComplemento = " (Normal)";
//							// para cumprir o provimento 16/2012 da CGJ
//							//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao(), processoDt.getId_ProcessoTipo());
//							id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao());
//						}
//						id_ServentiaPrevencaoDataArquivamento = null;
//					}				
//				}
//			} else {
//
//				//consulta a área distribuição recursal vinculada a serventia do processo
//				AreaDistribuicaoDt areaDistribuicao = new AreaDistribuicaoNe().consultarId(processoDt.getId_AreaDistribuicao());
//				//se for turma não deve verificar conexao e nem prevensão
//				//se for turma não verificar conexao ou prevensao (despacho 1944/2012 corregedoria)
//				if (ServentiaSubtipoDt.isTurma(areaDistribuicao.getServentiaSubtipoCodigo())){
//					//para cumprir o provimento  16/2012 da CGJ
//					//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao(), processoDt.getId_ProcessoTipo());
//					id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao());
//				}else{
//					// Resgata possíveis preventos e consulta a serventia que deverá
//					// receber o processo
//					id_ServentiaPrevencaoDataArquivamento = verificaConexaoProcessoOriginario(processoDt, obFabricaConexao);
//	
//					if (id_ServentiaPrevencaoDataArquivamento!=null && id_ServentiaPrevencaoDataArquivamento[0] != null) {
//						if ( id_ServentiaPrevencaoDataArquivamento[1]==null ||  id_ServentiaPrevencaoDataArquivamento[1].length()==0)
//							stComplemento = " (Conexão "+id_ServentiaPrevencaoDataArquivamento[2]+")";
//						else stComplemento = " (Prevenção "+id_ServentiaPrevencaoDataArquivamento[2]+")";
//						id_Serventia = id_ServentiaPrevencaoDataArquivamento[0];
//					}else{
//						stComplemento = " (Normal)";
//						//para cumprir o provimento  16/2012 da CGJ
//						//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao(), processoDt.getId_ProcessoTipo());
//						id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(processoDt.getId_AreaDistribuicao());						
//					}
//				}
//			}
//
//			// Consulta dados da serventia
//			if (id_Serventia != null) {
//				processoDt.setId_Serventia(id_Serventia);
//
//				serventiaSegundoGrauDt = new ServentiaNe().consultarId(id_Serventia);
//				processoDt.setServentiaDt(serventiaSegundoGrauDt);
//
//				// Captura próxima numeração de processo, baseado no código da
//				// comarca
//				String numero = ProcessoNumero.getInstance().getProcessoNumero(processoDt.getForumCodigo());
//				processoDt.setAnoProcessoNumero(numero);
//			} 
//			
//			if (id_Serventia == null || serventiaSegundoGrauDt == null) throw new MensagemException("Não foi possível localizar uma serventia ativa para cadastrar este processo. Entre em contato com o suporte.");
//			
//			// Valida se já existe um processo com o número gerado / informado
//			valideExistenciaProcessoNumeroCompleto(processoDt, obFabricaConexao);
//
//			// Seta dados do processo: fase conhecimento, status Ativo
//			processoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
//			processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
//			
//			// Salva processo
//			salvar(processoDt, obFabricaConexao);
//
//			// Salvando partes
//			salvarPartesProcessoSegundoGrau(processoDt, logDt, obFabricaConexao);
//
//			// Salvando advogados incluídos
//			new ProcessoParteAdvogadoNe().salvarAdvogadosPartesPromoventes(processoDt.getListaPromoventes(), processoDt.getListaAdvogados(), logDt, obFabricaConexao);
//
//			// Salvando assuntos do processo
//			processoAssuntoNe.salvarProcessoAssunto(processoDt.getListaAssuntos(), processoDt.getId(), logDt, obFabricaConexao);
//
//			// Distribui recurso para um Relator, Revisor e Vogal
//			// (ServentiaCargo)
//			if (id_ServentiaCargoRelator == null) {
//
//				if ( ServentiaSubtipoDt.isSegundoGrau(serventiaSegundoGrauDt.getServentiaSubtipoCodigo())){
//					//para atender o provimento 16/2012
//					//cargosDistribuicao = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia, processoDt.getId_ProcessoTipo());
//					id_ServentiaCargoRelator = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia,processoDt.getId_AreaDistribuicaoRecursal());
//
//	
//				} else {
//					//para atemder ao provimento 16/2012 da CGJ
//					//id_ServentiaCargoRelator = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicaoTurma(id_Serventia, processoDt.getId_ProcessoTipo());
//					id_ServentiaCargoRelator = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia,processoDt.getId_AreaDistribuicaoRecursal());
//				}
//			}
//
//			if (id_ServentiaCargoRelator != null) responsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_ServentiaCargoRelator, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
//			else throw new MensagemException("Não foi possível localizar um relator ativo vinculado à serventia " + serventiaSegundoGrauDt.getServentia() + " para vincular a este processo. Entre em contato com o suporte.");
//
//			// Se usuário que está cadastrando é um promotor adiciona ele como
//			// responsável pelo processo
//			if (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.PROMOTORES) {
//				responsavelNe.salvarProcessoResponsavel(processoDt.getId(), usuarioDt.getId_ServentiaCargo(), true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
//			}
//
//			/* ---------- PONTEIRO ----------------*/
//			///salvo o ponteiro
//			new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(),PonteiroLogTipoDt.DISTRIBUICAO,processoDt.getId(),processoDt.getId_Serventia(),UsuarioDt.SistemaProjudi, new Date(), id_ServentiaCargoRelator  ),obFabricaConexao );
//			/* ---------- PONTEIRO ----------------*/
//			
//			// Gera movimentação PETIÇÃO ENVIADA
//			MovimentacaoDt movimentacaoPeticao = movimentacaoNe.gerarMovimentacaoPeticaoEnviada(processoDt.getId(), usuarioDt, logDt, obFabricaConexao);
//
//			// Salva Arquivos
//			MovimentacaoArquivoNe arquivoNe = new MovimentacaoArquivoNe();
//			arquivoNe.inserirArquivosComRecibo(movimentacaoPeticao.getId(), processoDt.getProcessoNumeroCompleto(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
//
//			// Se for Câmara Cível gera movimentação específica
//			if (serventiaSegundoGrauDt.getServentiaSubtipoCodigo() != null && !serventiaSegundoGrauDt.getServentiaSubtipoCodigo().equals("")) {
//				//dadosConexao têm as informações do processo conexo, na posição 3 esta a data do retorno, se retornou é prevensão, caso contrario conexão. na posicao 4 está o número do processo conexo 
//				if (id_ServentiaPrevencaoDataArquivamento!=null && id_ServentiaPrevencaoDataArquivamento[0] != null) {										
//					stComplemento = " (Conexão " + stTipoConexaoPrevencao +") ";														
//					if (id_ServentiaPrevencaoDataArquivamento[0]!=null && id_ServentiaPrevencaoDataArquivamento[1].length()>0) 
//						stComplemento = " (Prevenção " + stTipoConexaoPrevencao +") ";
//				}
//				if (serventiaSegundoGrauDt.isSegundoGrau()){
//					// Gera movimentação AUTOS DISTRIBUÍDOS
//					movimentacaoNe.gerarMovimentacaoAutosDistribuidos(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento, logDt, obFabricaConexao);
//				} else {
//					// Gera movimentação AUTOS DISTRIBUÍDOS TURMA RECURSAL
//					movimentacaoNe.gerarMovimentacaoRecursoDistribuido(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento, logDt, obFabricaConexao);
//				}
//			} else
//				throw new MensagemException("Não foi possível determinar o Sub-Tipo da Serventia de Segundo Grau.");
//
//			// Gera pendência Verificar prevenção
//			//if (dadosConexao != null) pendenciaNe.gerarPendenciaVerificarConexao(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoPeticao.getId(), logDt, obFabricaConexao);
//
//			// Gera pendência Verificar Distribuição - no caso das Câmaras
//			//if (serventiaSegundoGrauDt.getServentiaSubtipoCodigo() != null && Funcoes.StringToInt(serventiaSegundoGrauDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.CAMARA_CIVEL) {
//			if (serventiaSegundoGrauDt.isSegundoGrau()) {
//				pendenciaNe.gerarPendenciaVerificarDistribuicao(processoDt, UsuarioServentiaDt.SistemaProjudi, movimentacaoPeticao.getId(), logDt, processoDt.getListaArquivos(), obFabricaConexao);
//			} else {
//				// Gera pendência Verificar Novo Processo
//				pendenciaNe.gerarPendenciaVerificarNovoProcesso(processoDt, UsuarioServentiaDt.SistemaProjudi, id_Serventia, movimentacaoPeticao.getId(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
//			}
//			
//			// Gera pendência Verificar prevenção
//			if (id_ServentiaPrevencaoDataArquivamento != null && id_ServentiaPrevencaoDataArquivamento[0] != null) pendenciaNe.gerarPendenciaVerificarConexao(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), movimentacaoPeticao.getId(), logDt, obFabricaConexao);
//			
//			if(isProcessoExecucaoFiscal(processoDt))
//				efetueGeracaoDeGuiaUnicaDeFazendaPublica(processoDt, obFabricaConexao);
//			
//			if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null){
//				
//				if( processoDt.getProcessoNumero() != null) {
//					guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
//				}
//				
//				/** ***Rollback*** **/
//				codigoServentiaParaRollback = Funcoes.completarZeros(processoDt.getServentiaDt().getServentiaCodigoExterno(),3);
//				codigoComarcaCodigoParaRollback = processoDt.getServentiaDt().getComarcaCodigo();
//				numrProcessoParaRollback = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
//				
//				idUsuarioLogRollback = processoDt.getId_UsuarioLog();
//				idComputadorLogRollback = processoDt.getIpComputadorLog();
//				/** ****** **/
//			
//			
//				this.vinculaGuiaProcesso(guiaEmissaoDt, processoDt.getId_Processo(), null, id_Serventia, processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), obFabricaConexao);
//				
//				if (!guiaEmissaoNe.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao)) {
//					pendenciaNe.gerarPendenciaVerificarGuiaPendente(processoDt, UsuarioServentiaDt.SistemaProjudi, id_Serventia, movimentacaoPeticao.getId(), processoDt.getListaArquivos(), logDt, obFabricaConexao);
//				}
//			
//			}
//
//			obFabricaConexao.finalizarTransacao();
//		} catch (Exception e) {
//			obFabricaConexao.cancelarTransacao();
//			cancelarCadastroProcesso(processoDt);
//			
//			//Método para fazer o tratamento de rollback
//			guiaEmissaoNe.rollbackAlteracoesGuiaSPGCadastroProcesso(guiaEmissaoDt, codigoServentiaParaRollback, numrProcessoParaRollback, codigoComarcaCodigoParaRollback, idUsuarioLogRollback, idComputadorLogRollback);
//			
//			throw e;
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//
//	}

	/**
	 * Método que consulta o último ServentiaCargo para o qual um processo foi
	 * distribuído. De acordo com o tipo da Serventia será definido qual Tipo de
	 * Cargo será consultado.
	 * 
	 * @param serventiaDt
	 *            : objeto com dados da serventia do processo
	 * 
	 * @author jrcorrea
	 */
	public int[] consultarUltimaDistribuicaoServentiaCargo(String Id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		int inUltimo[] = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
			case ServentiaTipoDt.VARA:
				// consulta o ultimo juiz a receber um processo, e quantos ele
				// recebeu
				inUltimo = obPersistencia.consultarUltimaDistribuicaoServentiaCargo(Id_Serventia, CargoTipoDt.JUIZ_1_GRAU);
				break;

			case ServentiaTipoDt.SEGUNDO_GRAU:
				// consulta o ultimo relator a receber um processo, e quantos
				// ele recebeu
				if (serventiaSubtipoCodigo.length() > 0 && ServentiaSubtipoDt.isSegundoGrau(serventiaSubtipoCodigo)){
					inUltimo = obPersistencia.consultarUltimaDistribuicaoServentiaCargo(Id_Serventia, CargoTipoDt.DESEMBARGADOR);
				} else
					inUltimo = obPersistencia.consultarUltimaDistribuicaoServentiaCargo(Id_Serventia, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
				break;

			case ServentiaTipoDt.PROMOTORIA:
				// consulta o ultimo promotor a receber um processo, e quantos
				// ele recebeu
				inUltimo = obPersistencia.consultarUltimaDistribuicaoServentiaCargo(Id_Serventia, CargoTipoDt.MINISTERIO_PUBLICO);
				break;

			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return inUltimo;
	}

	/**
	 * Método que consulta o último ServentiaCargo do tipo Conciliador para o
	 * qual um processo foi distribuído
	 * 
	 * @param id_Serventia
	 *            : serventia do processo
	 * 
	 * @author jrcorrea
	 */
	public int[] consultarUltimaDistribuicaoConciliador(String id_serventia) throws Exception {
		int inUltimo[];
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			inUltimo = obPersistencia.consultarUltimaDistribuicaoServentiaCargo(id_serventia, GrupoDt.CONCILIADORES_VARA);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return inUltimo;
	}
	
	public String podeRedistribuir(ProcessoDt processoDt, String id_ServentiaUsuario, String idServentiaCargo, String grupoCodigoUsuario) throws Exception {
		FabricaConexao obFabricaConexaoAtual = null;
		String stTemp = null;
		try{
			obFabricaConexaoAtual = new FabricaConexao(FabricaConexao.PERSISTENCIA);			
			stTemp= podeRedistribuir( processoDt, id_ServentiaUsuario, idServentiaCargo,  grupoCodigoUsuario,  obFabricaConexaoAtual) ;			
		}finally{
			obFabricaConexaoAtual.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Método que verifica se um processo pode ser redistribuído. Verifica se: -
	 * Não há conclusões abertas - Não há audiências em aberto - Status do
	 * Processo é ativo - Não há pendências em aberto
	 * 
	 * @param processoDt dt de processo
	 * @param id_ServentiaUsuario - id da serventia do usuário que está realizando a redistribuição
	 * @param idUsuarioServentia - id do usuário serventia do usuário que está realizando a redistribuição
	 * @param grupoCodigoUsuario - código do grupo do usuário que está realizando a redistribuição
	 * @author msapaula, hmgodinho
	 * @return
	 */
	public String podeRedistribuir(ProcessoDt processoDt, String id_ServentiaUsuario, String idServentiaCargo, String grupoCodigoUsuario, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		String stRetorno = "";					
			
		//Se processo está ativo.
		//A validação do verificarProcessoApenso é para garantir que processos apensos, mesmo ARQUIVADOS, sejam redistribuídos
		//junto do processo principal.
		if ( ! (processoDt.isAtivo() || processoDt.isCalculo() || processoDt.isSigiloso()  || processoDt.isErroMigracao() )) {
			return " Não é possível redistribuir, processo que não está ativo. \n";
		}
		
		// Se usuário for de serventia diferente do processo, não poderá movimentar.
		//Entretanto, se o usuário for o relator do processo, poderá redistribuir sem realizar esta validação.
		boolean isRelator = false;
		ServentiaCargoDt relatorProcessoDt = null;
		ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
		if(serventiaDt.isUPJs()) {
			if(serventiaDt.isUpjTurmaRecursal()) {
				relatorProcessoDt = new ProcessoResponsavelNe().consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, true, obFabricaConexao);
			} else {
				relatorProcessoDt = new ProcessoResponsavelNe().consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.JUIZ_UPJ, true, obFabricaConexao); 
			}
		} else {
			relatorProcessoDt = new ProcessoResponsavelNe().consultarRelatorResponsavelProcessoSegundoGrau(processoDt.getId(), obFabricaConexao);
		}
		if(relatorProcessoDt != null && idServentiaCargo != null && idServentiaCargo.equals(relatorProcessoDt.getId())) {
			isRelator = true;
		}
		if (!isRelator && !processoDt.getId_Serventia().equals(id_ServentiaUsuario)) {
			return "Sem permissão para redistribuir processo ("+ processoDt.getProcessoNumero() +") de outra serventia.";
		}

		// Se não conclusões em aberto
		if (pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
			stRetorno = "Há Autos Conclusos em Aberto. \n";
		}

		// Se não há audiências em aberto
		if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
			stRetorno += "Há Audiências em Aberto. \n";
		}

		// Se não há outras pendências em aberto
		if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) {
			stRetorno += "Há pendência(s) em aberto. \n";
		}
		
		if (grupoCodigoUsuario.equals(String.valueOf(GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO)) 
				&& processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
			stRetorno += "Sem permissão. Motivo: Processo Digital! \n";
		}

		// Se processo não está com dados completos, deve consultar
		if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) {
			processoDt = consultarId(processoDt.getId());
		}
		
		// Regra para evitar que o processo seja redistribuído quando tiver sido recebido através de encaminhamento:
		// Se não tiver registro do processo no ponteiro não foi recebido através do encaminhamento pois só deixa
		// encaminhar se tiver. Se tiver registro do processo no ponteiro é possível que ele tenha sido recebio através
		// do encaminhamento, portanto verifica se o id_serv do proc é o mesmo do ponteioro. Se for, deixa
		// redistribuir pois está na origem. Se não for, não deixa pois foi recebido através do encaminhamento.

		//String idServPonteiroProc = ponteiroLogNe.ondeEstaPonteiroProcesso( processoDt.getId() );
		
		boolean podeEncaminhar =  (new ProcessoEncaminhamentoNe()).podeEncaminhar(processoDt.getId());
		
		//if( idServPonteiroProc != null && !idServPonteiroProc.equals(processoDt.getId_Serventia() ) ){
		if(!podeEncaminhar){
			stRetorno += "Não foi possível redistribuir pois o processo foi recebido através de Encaminhamento. \n";
		} 						
		
		if (stRetorno.length() > 0) {
			stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " - " + stRetorno;
		}		
	
		if (processoDt.isProcessoHibrido() && !GrupoDt.isDistribuidor(grupoCodigoUsuario)){//B.O 2019/9175 e 2020/8523
			stRetorno += "Este processo não poderá ser redistribuído, em conformidade ao Decreto 1.374/2019 artigo 5º. \n";
		}
		
		return stRetorno;
	}
	
	/**
	 * Verifica se o processo pode ser encaminhado para outra serventia. Quando chamado de outra classe, por padrão,
	 * o parâmetro isApensoFilho é false. Quando chamado de dentro desta classe existe a possibilidade de passar este
	 * parâmetro como true. Este parâmetro indica que está verificando o apenso de um processo principal que foi encaminhado.
	 * Neste caso, não deve bloquear pelo fato de ele ser um apenso. Contudo, se o processo principal for um apenso
	 * deve bloquear. Este recurso é utilizado porque o método que salva o encaminhamento chama a si mesmo recursivamente
	 * passando como parâmetro os apensos do processo principal e o método podeEncaminhar deve se comportar de maneira 
	 * diferente quando se tratar de um apenso sendo verificado recursivamente ou não.
	 * 
	 * @param processoDt
	 * @param usuarioDt
	 * @author hrrosa
	 * @return String
	 * @throws Exception
	 */
	public String podeEncaminhar(ProcessoDt processoDt, UsuarioDt usuarioDt, String idPendenciaIgnorar) throws Exception {
		return this.podeEncaminhar(processoDt, usuarioDt, idPendenciaIgnorar, false);
	}
	/** Leia a descriação do método acima. */
	private String podeEncaminhar(ProcessoDt processoDt, UsuarioDt usuarioDt, String idPendenciaIgnorar, boolean isApensoFilho) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			// Se usuário for de serventia diferente do processo, não poderá
			// movimentar
			if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) {
				return "Sem permissão para enviar processo de outra serventia.";
			}

			// Se não conclusões em aberto
			if(idPendenciaIgnorar != null) {
				if (pendenciaNe.verificaConclusoesAbertasComExcecao(processoDt.getId(), idPendenciaIgnorar, obFabricaConexao)) {
					stRetorno = "Há Autos Conclusos em Aberto. \n";
				}
			} else {
				if (pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
					stRetorno = "Há Autos Conclusos em Aberto. \n";
				}
			}

			// Se não há audiências em aberto
			if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há Audiências em Aberto. \n";
			}

			// Se não há outras pendências em aberto
			if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) {
				stRetorno += "Há pendência(s) em aberto. \n";
			}
			
			if (usuarioDt.getGrupoCodigo().equals(String.valueOf(GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO)) 
					&& processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
				stRetorno += "Sem permissão. Motivo: Processo Digital! \n";
			}

			// Se processo não está com dados completos, deve consultar
			if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) {
				processoDt = consultarId(processoDt.getId());
			}
			
			ProcessoEncaminhamentoNe processoEncaminhamentoNe = new ProcessoEncaminhamentoNe();
			boolean podeEncaminhar =  processoEncaminhamentoNe.podeEncaminhar(processoDt.getId());
			
			if (!podeEncaminhar){
				stRetorno += "Este processo foi recebido através de Encaminhamento e portanto não pode ser encaminhado novamente. Se estiver tentando devolver o processo, "
							+ "faça uma movimentação e selecione a opção \"Retornar para a serventia de origem\".\n";
			}

			// Esta verificação para o encaminhamento acompanha a regra de redistribuição de processos. O método ProcessoNe.verificarRedistribuicao teve o trecho desta verificação comentado.
			// Portanto, mantendo a mesma verificação comentada neste método. Caso a regra se torne definitiva na redistribuição, este trecho também poderá ser excluído.
			// Mudança feita em atendimento ao BO 2020/6887.
			//	if(processoDt.isApenso() && !isApensoFilho){
			//		stRetorno += "Antes de encaminhar exclua o apensamento ou, se preferir, encaminhe o processo principal. \n";
			//	}
			
			if (stRetorno.length() > 0) {
				stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " - " + stRetorno;
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	

	/**
	 * Método que verifica se um processo pode ser encaminhado a turma recursal.
	 * 
	 * Verifica se: - Não há audiências em aberto - Não há pendências em aberto
	 * - Não há autos conclusos
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 * @return
	 */
	public String podeEnviarInstanciaSuperior(ProcessoDt processoDt, String grupoCodigo, boolean finalizandoConclusao) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			

			// Se não há audiências em aberto
			if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há audiência(s) em aberto. \n";
			}

			// Se não há pendências em aberto
			if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) {
				stRetorno += "Há pendência(s) em aberto.";
			}
			 			
			boolean podeEncaminhar =  (new ProcessoEncaminhamentoNe()).podeEncaminhar(processoDt.getId());
			
			if (!podeEncaminhar){
				stRetorno += "Não foi possível enviar a instância superior, pois o processo foi recebido através de encaminhamento. \n";
			}
			
			 
//			// Regra para evitar que o processo seja enviado para instância superior quando tiver sido recebido através de encaminhamento:
//			// Se não tiver registro do processo no ponteiro não foi recebido através do encaminhamento pois só deixa
//			// encaminhar se tiver. Se tiver registro do processo no ponteiro é possível que ele tenha sido recebio através
//			// do encaminhamento, portanto verifica se o id_serv do proc é o mesmo do ponteioro. Se for, deixa
//			// redistribuir pois está na origem. Se não for, não deixa pois foi recebido através do encaminhamento.
//			String idServPonteiroProc = ponteiroLogNe.ondeEstaPonteiroProcesso( processoDt.getId() );
//			if( idServPonteiroProc != null && !idServPonteiroProc.equals(processoDt.getId_Serventia() ) ){
//				stRetorno += "Não foi possível redistribuir pois o processo foi recebido através de Encaminhamento. \n";
//			} 
			
			// Verifica se há conclusões em aberto
			if  (!finalizandoConclusao && pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
				stRetorno = "Há autos conclusos/relatório/voto/ementa em aberto. \n";
			}

			if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode ser encaminhado para instância superior. " + stRetorno;

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Método que verifica se uma sessão pode ser marcadada para o processo
	 * passado. Verifica se: - Não há uma sessão já marcada para o processo
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String podeMarcarSessao(ProcessoDt processoDt, int codPendenciaTipo, String DataSessao, String IdProcessoTipo) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			if (codPendenciaTipo != PendenciaTipoDt.REMARCAR_SESSAO) {
				if (audienciaProcessoNe.verificaSessoesPendentesProcesso(processoDt.getId(), IdProcessoTipo, obFabricaConexao)) stRetorno += "Há Sessões em Aberto. \n";
				if (stRetorno.length() > 0) stRetorno = "Não pode marcar sessão para processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + "." + stRetorno;
			}			
			if (stRetorno.length() == 0) {				
				if (codPendenciaTipo == PendenciaTipoDt.MARCAR_SESSAO || codPendenciaTipo == PendenciaTipoDt.REMARCAR_SESSAO) {
					ServentiaDt serventiaProcessodt = (new ServentiaNe()).consultarId(processoDt.getId_Serventia());
					if (serventiaProcessodt.isSegundoGrau() && ProjudiPropriedades.getInstance().getQuantidadeMinimaDiasMarcarSessao2Grau() > 0) {														    
					    TJDataHora dataSessaoAgendada = new TJDataHora(DataSessao);
					    dataSessaoAgendada.atualizePrimeiraHoraDia();
					    
					    TJDataHora dataMinimaSessao = new TJDataHora();
					    dataMinimaSessao.adicioneDia(ProjudiPropriedades.getInstance().getQuantidadeMinimaDiasMarcarSessao2Grau()); 
					    dataMinimaSessao.atualizePrimeiraHoraDia();
					   					      
//					    if (dataMinimaSessao.ehApos(dataSessaoAgendada)) stRetorno += String.format("A Data da sessão deve ser igual ou posterior à %s, pois a quantidade mínima de antecedência é %s dias. \n", dataMinimaSessao.getDataFormatadaddMMyyyy(), ProjudiPropriedades.getInstance().getQuantidadeMinimaDiasMarcarSessao2Grau()) ;				    
					}
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	/**
	 * Método que verifica se uma sessão pode ser marcadada para o processo
	 * passado. Verifica se: - Não há uma sessão já marcada para o processo
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String obtemAlertaMovimentarProcesso(ProcessoDt processoDt, int codPendenciaTipo, String DataSessao, String idProcessoTipo) throws Exception {
		String stRetorno = "";
		String stRetornoValidacao = podeMarcarSessao(processoDt, codPendenciaTipo, DataSessao, idProcessoTipo);	
		if (stRetornoValidacao.length() == 0) {				
			if (codPendenciaTipo == PendenciaTipoDt.MARCAR_SESSAO || codPendenciaTipo == PendenciaTipoDt.REMARCAR_SESSAO) {
				ServentiaDt serventiaProcessodt = (new ServentiaNe()).consultarId(processoDt.getId_Serventia());
				if (serventiaProcessodt.isSegundoGrau() && AudienciaDt.QUANTIDADE_MINIMA_DIAS_ALERTA_MARCAR_SESSAO > 0) {														    
				    TJDataHora dataSessaoAgendada = new TJDataHora(DataSessao);
				    dataSessaoAgendada.atualizePrimeiraHoraDia();
				    
				    TJDataHora dataMinimaSessao = new TJDataHora();
				    dataMinimaSessao.adicioneDia(AudienciaDt.QUANTIDADE_MINIMA_DIAS_ALERTA_MARCAR_SESSAO); 
				    dataMinimaSessao.atualizePrimeiraHoraDia();
				   					      
				    if (dataMinimaSessao.ehApos(dataSessaoAgendada)) stRetorno += String.format("Atenção! A data escolhida possui menos de %s dias de antecedência. Para corrigir exclua a pendência Marcar Sessão da lista e selecione uma nova data, caso contrário Clique para confirmar a Geração de Pendência(s).\n", AudienciaDt.QUANTIDADE_MINIMA_DIAS_ALERTA_MARCAR_SESSAO) ;				    
				}
			}
		}
		return stRetorno;
	}

	/**
	 * Método que verifica se uma sessão pode ser desmarcadada para o processo
	 * passado. Verifica se: - Há uma sessão já marcada para o processo
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String podeDesmarcarSessao(ProcessoDt processoDt) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		String stRetorno = "";
		String idProcessoTipo = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			
			// Se há sessões marcadas
			if (processoDt.getId_Recurso() == null || processoDt.getId_Recurso().trim().length() == 0) idProcessoTipo = processoDt.getId_ProcessoTipo();
			if (!audienciaProcessoNe.verificaSessoesPendentesProcesso(processoDt.getId(), idProcessoTipo, obFabricaConexao)) stRetorno += "Não há Sessão Marcada. \n";
			if (stRetorno.length() > 0) stRetorno = "Não pode remarcar/desmarcar sessão para processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + ". " + stRetorno;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Verifica dados obrigatórios na redistribuição
	 * 
	 * @author msapaula
	 */
	public String verificarRedistribuicao(RedistribuicaoDt dados, UsuarioDt usuarioDt) throws Exception {
		StringBuilder stRetorno = new StringBuilder();
		if (dados.getId_AreaDistribuicao().length() == 0 && dados.getProcessoNumeroDependente().length() == 0 && dados.getId_Serventia().length() == 0) 
			stRetorno.append("Deve ser indicada a forma de redistribuição, seja para uma Área de Distribuição ou serventia específica. \n");
		if (dados.getListaProcessos() == null || dados.getListaProcessos().size() == 0) 
			stRetorno.append("Nenhum Processo foi selecionado para redistribuição. \n");
		else {
			List processos = dados.getListaProcessos();
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				
				//TODO trecho de código comentado por conta da modificação (realizada em janeiro de 2018) no tratamento de processos apensos.
				//Manter o trecho até o começo de 2019. Caso não haja uma reviravolta no tratamento dos apensos, pode excluir esse trecho de código.
				//Se o processo for do tipo CARTA PRECATÓRIA ele poderá ser redistribuído independente de existir apensamento/dependência.
//				if(!processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA)) && 
//						!processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPC)) && 
//						!processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPP))) {
//					//Se o processo NÃO for do tipo CARTA PRECATÓRIA ele só poderá ser redistribuído desde que 
//					//não esteja apensado a outro processo.
//					if(processoDt.isApenso()){
//						stRetorno.append("Antes de redistribuir exclua o apensamento ao processo "+ processoDt.getProcessoNumeroPrincipal() +". \n");
//					}
//				}
				
				MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
				//se o processo é hibrido e tem a movimentação tipo AGUARDANDO_DIGITALIZACAO_PARA_DISTRIBUICAO_2G de codigo 40067 não pode ser redistribuído e nem uma hipotese
				if (processoDt.isProcessoHibrido() && movimentacaoNe.possuiMovimentacaoAguardandoDigitalizacao(processoDt.getId_Processo())){
					stRetorno.append("Processo não pode ser redistribuído. Aguardando digitalização para distribuição no 2º Grau. \n");
				} else if (processoDt.isProcessoHibrido() && !GrupoDt.isDistribuidor(usuarioDt.getGrupoCodigo())){//B.O 2019/9175 e 2020/8523
					stRetorno.append("Este processo não poderá ser redistribuído, em conformidade ao Decreto 1.374/2019 artigo 5º. \n");
				}
				
				//Se tiver recurso autuado entra no if
				if (processoDt.getRecursoDt() != null && processoDt.getRecursoDt().getId() != null && !processoDt.getRecursoDt().getId().equals("")){
					
					try {
						//quando for selecionada Área de distribuição 
						if (dados != null && dados.getId_AreaDistribuicao() != null && !dados.getId_AreaDistribuicao().equals("")){
							AreaDistribuicaoDt areaDistribuicaoDt = new AreaDistribuicaoDt();
							AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
							areaDistribuicaoDt = areaDistribuicaoNe.consultarId(dados.getId_AreaDistribuicao());							
							if (!ServentiaSubtipoDt.isSegundoGrau( areaDistribuicaoDt.getServentiaSubtipoCodigo()) && !ServentiaSubtipoDt.isTurma( areaDistribuicaoDt.getServentiaSubtipoCodigo()) ){
								stRetorno.append("Recurso não pode ser redistribuido para uma serventia de 1º Grau. \n");
							}
						}
						//quando for selecionada Serventia
						if (dados != null && dados.getId_Serventia() != null && !dados.getId_Serventia().equals("")){
							ServentiaDt serventiaDt = new ServentiaDt();
							ServentiaNe serventiaNe = new ServentiaNe();
							serventiaDt = serventiaNe.consultarId(dados.getId_Serventia());							
							if (!ServentiaSubtipoDt.isSegundoGrau( serventiaDt.getServentiaSubtipoCodigo()) && !ServentiaSubtipoDt.isTurma( serventiaDt.getServentiaSubtipoCodigo()) ){
								stRetorno.append("Recurso não pode ser redistribuido para uma serventia de 1º Grau. \n");
							}
						}
						
					} catch (Exception e) {
					   throw new MensagemException("Erro ao verificar Redistribuição.");
					}
				} else {
					//Se não tiver recurso autuado, é preciso verificar se há recurso não autuado. Nesse caso, não pode redistribuir.
					RecursoNe recursoNe = new RecursoNe();
					if(recursoNe.existeRecursoNaoAutuadoProcesso(processoDt.getId(), processoDt.getId_Serventia())) {
						stRetorno.append("Processo não pode ser redistribuído. Há recurso aguardando autuação. \n");
					}
					
					if (stRetorno.toString().length() == 0) {
						List<String[]> audicienciasPendentes = new AudienciaProcessoNe().consultarAudienciasPendentesProcesso(processoDt.getId(), false);
						for (String[] audicienciaPendente : audicienciasPendentes) {
						if (audicienciaPendente != null && audicienciaPendente.length >= 9 &&  audicienciaPendente[7] != null && audicienciaPendente[6] != null && audicienciaPendente[8] != null && 
							(Funcoes.StringToInt(audicienciaPendente[6]) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo()||
							 Funcoes.StringToInt(audicienciaPendente[6]) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
							 Funcoes.StringToInt(audicienciaPendente[6]) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
							
							if (dados.getId_Serventia().length() == 0 ||
							    Funcoes.StringToInt(dados.getId_Serventia()) != Funcoes.StringToInt(audicienciaPendente[7])) {
								stRetorno.append("Existe uma audiência/mediação CEJUSC pendente, portanto deve ser indicada a serventia específica onde a audiência/mediação foi marcada (" + audicienciaPendente[8]  + ") \n");
							}
						}
					}
				}
			}
		}
		}
		
		return stRetorno.toString();
	}

	/**
	 * Verifica dados obrigatórios no encaminhamento
	 * 
	 * @author hrrosa
	 */
	public String verificarEncaminhamento(RedistribuicaoDt dados, UsuarioDt usuarioDt) throws Exception {
		StringBuilder stRetorno = new StringBuilder();

		if (dados.getListaProcessos() == null || dados.getListaProcessos().size() == 0) 
			stRetorno.append("Nenhum Processo foi selecionado para encaminhamento. \n");
		else {
			List processos = dados.getListaProcessos();
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				
				stRetorno.append( this.podeEncaminhar(processoDt, usuarioDt, null) );
			}
		}
		
		return stRetorno.toString();
	}
	
	/**
	 * Salva redistribuição de um ou mais processos.
	 * 
	 * @param movimentacaoDt: objeto com dados da movimentação a serem persistidas
	 * @param usuarioDt: usuário que está realizando a movimentação
	 * @param movimentacaoProcessoDt: movimentação que será gerada ao redistribuir o processo
	 * @param boVerificarPrevensaoConexao - considerar prevenção/conexão no ato da redistribuicao
	 * @param arquivarProcesso - se vai realizar arquivamento do processo no final da redistribuição (ato realizado comumente nos CEJUSC)
	 * @param ignorarPonteiro - se o ponteiro de distribuição deve ou não ser alterado
	 * @param CriarFabricaConexao - NÃO É OBRIGATÓRIO - o atributo fabricaConexao será informado se o método for chamado para redistribuir processos apensados a outro processo que foi
	 * 							redistribuido anteriormente. Motivo: usar a mesma conexão.
	 * 
	 * @author hmgodinho, jrcorrea
	 */
	public void salvarRedistribuicao(RedistribuicaoDt redistribuicaoDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt, boolean boVerificarPrevensaoConexao, boolean arquivarProcesso   ) throws MensagemException, Exception {
		
		ServentiaNe serventiaNe = new ServentiaNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		ServentiaDt serventiaNovaDt = null;
		ServentiaDt serventiaAtualDt = null;
		MovimentacaoDt movimentacao = null;
		String complemento = "";
		ServentiaCargoDt serventiaCargoDt = null;		
		
		FabricaConexao obFabricaConexao = null;

		try {

			LogDt logDt = new LogDt(usuarioDt.getId(), redistribuicaoDt.getIpComputadorLog());
			List processos = redistribuicaoDt.getListaProcessos();

			//o atributo fabricaConexao será informado se o método for chamado
			//para redistribuir processos apensados a outro processo que foi
			//redistribuido anteriormente. Motivo: usar a mesma conexão.
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			//está aqui fora do laço pois deve-se gerar um unico recibo para todos os processos;
			if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")){
				movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
			}
			
			String logIdProcessoRedistribuicaoEmLote = "(";
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				if(processoDt != null && processoDt.getId() != null) {
//					String processoDigitalizado = "NAO(Porcentagem Automática)";
//					if( processoDt.isProcessoImportadoSPG() ) {
//						processoDigitalizado = "SIM(Menu Porcentagem Aberto)";
//					}
//					logIdProcessoRedistribuicaoEmLote += "id_proc:" + processoDt.getId() + ";digitalizado:" + processoDigitalizado + ";";
					logIdProcessoRedistribuicaoEmLote += "id_proc:" + processoDt.getId() + ";";
				}
			}
			logIdProcessoRedistribuicaoEmLote += ")";
			logIdProcessoRedistribuicaoEmLote = logIdProcessoRedistribuicaoEmLote.replace(";)", ")");
			
			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				
				String mensagemErro = "";
				//se não for redistribuição em lote do distribuidor cível
				if (!usuarioDt.isDistribuidor()) {									
					mensagemErro = this.podeRedistribuir(processoDt, usuarioDt.getId_Serventia(), usuarioDt.getId_ServentiaCargo(), usuarioDt.getGrupoCodigo(), obFabricaConexao);
				}
				
				if (mensagemErro!= null && mensagemErro.length() > 0){
					throw new MensagemException(mensagemErro);
				}

				// Redistribuição com regra (deve redistribuir na área selecionada)
				
				String id_serventiaCargo_atual = "";
				String id_serventia_atual = processoDt.getId_Serventia();
				
				String id_area_distribuicao_atual = processoDt.getId_AreaDistribuicao();
				String id_area_distribuicao_nova = "";
				String id_serventiaCargo_novo= "";
				String id_serventia_nova = null;
				//Variável criada para armazenar o ID do usuário serventia que está realizando a movimentação.
				//Foi criada para atender solicitação do 2º grau de ter o nome do novo responsável na descrição da 
				//movimentação de processo redistribuído.
				String idUsuServResponsavelDistribuicao = usuarioDt.getId_UsuarioServentia();
				
				ProcessoDt processoPaiDt = null;
				
				//Redistribuição normal do processo, informando a nova área de distribuição - OPÇÃO 1
				if (redistribuicaoDt.isOpcaoRedistribuicao("1")) {
					String stComplemento = " (Normal)";
					id_area_distribuicao_nova = redistribuicaoDt.getId_AreaDistribuicao();
					
					if (boVerificarPrevensaoConexao){
						//Verifica se o processo já esteve em uma serventia da area de distribuicao e retorna a serventia pela qual o processo passou.
						id_serventia_nova = serventiaNe.consultarServentiaAreaDistribuicaoPassada(redistribuicaoDt.getId_AreaDistribuicao(), processoDt.getId_Processo(), processoDt.getId_Serventia() );
					}
					
					//Se não encontrar serventia na consulta anterior, realiza o sorteio da nova serventia
					if (id_serventia_nova == null ) {
						//para cumprir o provimento 16/2012 da CGJ
						id_serventia_nova = DistribuicaoProcesso.getInstance().getDistribuicao(redistribuicaoDt.getId_AreaDistribuicao(),  processoDt.getId_Serventia(), obFabricaConexao);
					} else {
						//Se tiver encontrado, o complemento deixa de ser NORMAL para ser RETORNO
						stComplemento = " (Retorno)";
					}
						
					// Se seguir não encontrando uma nova serventia para o processo, lança exceção. É preciso corrigir o cadastro da área de distribuição.
					if (id_serventia_nova == null) {
						throw new MensagemException("Não é possível redistribuir o processo nº " + processoDt.getProcessoNumero() + "  não existe serventia disponível nesta área de distribuição.");
					}
					
					serventiaNovaDt = serventiaNe.consultarId(id_serventia_nova);
					
					if (boVerificarPrevensaoConexao  ){
						//Verifica se já não havia um relator responsável pelo processo na nova serventia
						if (serventiaNovaDt.isTurma()) {
							serventiaCargoDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoInativo(processoDt.getId(), id_serventia_nova, String.valueOf(GrupoTipoDt.JUIZ_TURMA), obFabricaConexao);
							// Verifica se já não havia um juiz responsável na nova serventia
						}else if( serventiaNovaDt.isUPJs()) {
							if(serventiaNovaDt.isUpjTurmaRecursal()) {
								serventiaCargoDt = processoResponsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, false, obFabricaConexao); 
							} else {
								serventiaCargoDt = processoResponsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.JUIZ_UPJ, false, obFabricaConexao); 
							}
						}else {
							if ( processoDt.isSegundoGrau() ) {
								//Se for redistribuição por prevenção/conexão, tenta localizar o responsável pelo processo pai para colocá-lo como responsável pelo processo filho							
								serventiaCargoDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoSegundoGrauInativo(processoDt.getId(), id_serventia_nova, obFabricaConexao);
							} else {
								serventiaCargoDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoInativo(processoDt.getId(), id_serventia_nova, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), obFabricaConexao);
							}
						}
					}
					
					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
						stComplemento = "(EXECPENWEB)";
					}
				
					//O Processo de precatória não pode ser redistribuido para a sua serventia de origem.*****************************
					if (processoDt.isProcessoPrecatoriaExpedidaOnline()){
						//consultar processo com Dependencia para descobrir serventia de origem da precatoria
						ProcessoDt processoPrincipal = this.consultarProcessoSimplificado(processoDt.getId_ProcessoPrincipal());
						if (id_serventia_nova.equalsIgnoreCase(processoPrincipal.getId_Serventia())){
							throw new MensagemException("O Processo de precatória não pode ser redistribuido para a sua serventia de origem.");
						}
					}
					//****************************************************************************************************************					
					complemento = serventiaNovaDt.getServentia() + stComplemento;
				
				// Redistribuição sem regra informando o número do processo (por prevenção/conexão/dependência) - OPÇÃO 2
				}else if (redistribuicaoDt.isOpcaoRedistribuicao("2")) {
					processoPaiDt = consultarProcessoNumeroCompleto(redistribuicaoDt.getProcessoNumeroDependente(),obFabricaConexao);
					ServentiaDt serventiaFilhoDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
					String idRecursoAtivoMaisAntigo = new RecursoNe().consultarRecursoAtivoMaisAntigo(processoPaiDt.getId());
					RecursoDt recursoProcessoPaiDt = new RecursoNe().consultarIdCompleto(idRecursoAtivoMaisAntigo);
					
					//Se o processo a ser redistribuído for de 2º grau ou Turma
					if(serventiaFilhoDt.isSegundoGrau() || serventiaFilhoDt.isTurma()) {
						//Se o processo filho é recurso e o processo pai está no primeiro grau, o processo filho não pode ser redistribuído. 
						if(processoDt.getId_Recurso() != null && !processoDt.getId_Recurso().equals("")) {
							ServentiaDt serventiaDestinoDt = new ServentiaNe().consultarId(processoPaiDt.getId_Serventia());
							if(!serventiaDestinoDt.isSegundoGrau()){
								throw new MensagemException("O recurso de 2º Grau não pode ser redistribuído para serventia de 1º Grau.");
							}
						}
						//pega informações do processo pai para fazer a redistribuição
						id_serventia_nova = processoPaiDt.getId_Serventia();
						id_area_distribuicao_nova = processoPaiDt.getId_AreaDistribuicao();						
					} else {
						//Se o processo a ser redistribuído for de 1º grau
						if(idRecursoAtivoMaisAntigo != null && !idRecursoAtivoMaisAntigo.equalsIgnoreCase("")){
							//Se o processo pai possui recurso ativo, pega-se a serventia origem e área de distribuição origem 
							//do recurso ativo de data mais antiga. Isso vai garantir que o processo filho vá para o 
							//1º grau.
							id_serventia_nova = recursoProcessoPaiDt.getId_ServentiaOrigem();
							id_area_distribuicao_nova = recursoProcessoPaiDt.getId_AreaDistribuicaoOrigem();
						} else {
							//Se não houver recurso no processo pai, pega-se a serventia e área de distribuição do
							//processo pai.
							id_serventia_nova = processoPaiDt.getId_Serventia();
							id_area_distribuicao_nova = processoPaiDt.getId_AreaDistribuicao();
						}
					}
					
					if (id_serventia_nova.length()==0 ||id_area_distribuicao_nova.length()==0 ){
						throw new MensagemException("Não foi possível encontrar a Serventia ou A Área de Distribuição pelo processo informado.");
					}
					
					serventiaNovaDt = serventiaNe.consultarId(id_serventia_nova);
										
					//Se for redistribuição por prevenção/conexão, tenta localizar o responsável pelo processo pai para colocá-lo como responsável pelo processo filho						
					if (serventiaNovaDt.isTurma()) {
						serventiaCargoDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoAtivo(processoPaiDt.getId(), processoPaiDt.getId_Serventia(), String.valueOf(GrupoTipoDt.JUIZ_TURMA), obFabricaConexao);
					}else if(serventiaNovaDt.isUPJs()) {
						serventiaCargoDt = processoResponsavelNe.consultarProcessoResponsavel2Grau(processoPaiDt.getId(), CargoTipoDt.JUIZ_UPJ, true, obFabricaConexao); 
					} else {
						//Verifica se já não havia um relator responsável pelo processo na nova serventia
						if ( processoDt.isSegundoGrau() ) {
							serventiaCargoDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoSegundoGrauAtivo(processoPaiDt.getId(), obFabricaConexao);
						}else {
							serventiaCargoDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoAtivo(processoPaiDt.getId(), id_serventia_nova, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), obFabricaConexao);								
						}	
					}
											
										
					if (serventiaCargoDt == null ){
						throw new MensagemException("Não foi possível encontrar o responsável pelo processo dependente.");
					}
					
					//O Processo de precatória não pode ser redistribuido para a sua serventia de origem.*****************************
					if (processoDt.isProcessoPrecatoriaExpedidaOnline()){
						//consultar processo com Dependencia para descobrir serventia de origem da precatoria
						if (processoDt.getId_ProcessoPrincipal().equalsIgnoreCase(processoPaiDt.getId())){
							throw new MensagemException("O Processo de precatória não pode ser redistribuido para a sua serventia de origem.");
						}
					}
					//****************************************************************************************************************
					
					serventiaNovaDt = serventiaNe.consultarId(id_serventia_nova);
					
					complemento = serventiaNovaDt.getServentia() + " (Indicação de Prevenção ou Conexão)";					
					
					//Salvando a dependência no processo que está sendo redistribuído, caso ele ainda não seja dependente.
					if(!processoDt.isDependente()){
						processoDt.setId_ProcessoPrincipal(processoPaiDt.getId());
						processoDt.setId_UsuarioLog(redistribuicaoDt.getId_UsuarioLog());
						processoDt.setIpComputadorLog(redistribuicaoDt.getIpComputadorLog());						
					}

				// Redistribuição direcionada informando novos área de distribuição, serventia e responsável do processo - OPÇÃO 3
				}else if (redistribuicaoDt.isOpcaoRedistribuicao("3")) {
					//pega as informações da tela para efetuar a redistribuição
					id_serventia_nova = redistribuicaoDt.getId_Serventia();
					id_area_distribuicao_nova = redistribuicaoDt.getId_AreaDistribuicao();
					id_serventiaCargo_novo = redistribuicaoDt.getId_ServentiaCargo();
					
					serventiaNovaDt = serventiaNe.consultarId(id_serventia_nova);
					if (!(Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC ||
							  Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC_DPVAT ||
							  Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_MEDIACAO_CEJUSC ||
							 Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_RETORNAR_DA_CONCILIACAO_OU_MEDIACAO_CEJUSC)) {						
						complemento = serventiaNovaDt.getServentia() + " (Direcionada";
					}
					
					if(id_serventiaCargo_novo == null || id_serventiaCargo_novo.equalsIgnoreCase("")){
						//quando o id_serventiaCargo_novo não for informado na tela, deverá ser sorteado e informar no complemento da movimentação
						//que a distribuição foi direcionada à Serventia e não ao magistrado.
						id_serventiaCargo_novo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_serventia_nova, id_area_distribuicao_nova);
						complemento += " Serventia)";
					} else {
						complemento += " Magistrado)";
					}
					serventiaCargoDt = new ServentiaCargoNe().consultarId(id_serventiaCargo_novo);	
					//setando o relator no redistribuicaoDt para futuras validações
					redistribuicaoDt.setId_ServentiaCargo(id_serventiaCargo_novo);
					
					if (id_serventia_nova.length()==0 ||id_area_distribuicao_nova.length()==0 || id_serventiaCargo_novo.length() == 0){
						throw new MensagemException("Não foi possível encontrar a Serventia, a Área de Distribuição ou o novo Responsável.");
					}
										
					//O Processo de precatória não pode ser redistribuido para a sua serventia de origem.*****************************
					if (processoDt.isProcessoPrecatoriaExpedidaOnline()){
						//consultar processo com Dependencia para descobrir serventia de origem da precatoria
						ProcessoDt processoPrincipal = this.consultarProcessoSimplificado(processoDt.getId_ProcessoPrincipal());
						if (id_serventia_nova.equalsIgnoreCase(processoPrincipal.getId_Serventia())){
							throw new MensagemException("O Processo de precatória não pode ser redistribuido para a sua serventia de origem.");
						}
					}				
				
				}				
				
				//Se não for retorno a serventia ainda não foi consultada.
				if(serventiaNovaDt == null){
					serventiaNovaDt = serventiaNe.consultarId(id_serventia_nova);
				}
				
				//Se não for retorno a serventia ainda não foi consultada.
				if(serventiaAtualDt == null){
					serventiaAtualDt = serventiaNe.consultarId(id_serventia_atual);
				}
				
				//Consultando o responsável atual para ajustar o ponteiro de distribuição.
				ServentiaCargoDt serventiaCargoAtualDt = null;				
				if (serventiaAtualDt.isTurma()) {
					//Turma					
					serventiaCargoAtualDt = ((ServentiaCargoDt)processoResponsavelNe.consultarServentiaCargoResponsavelProcessoAtivo(processoDt.getId(), id_serventia_atual, String.valueOf(GrupoTipoDt.JUIZ_TURMA), obFabricaConexao));					
				} else	if (serventiaAtualDt.isUpjTurmaRecursal()) {
					//turma upj
					serventiaCargoAtualDt = ((ServentiaCargoDt)processoResponsavelNe.consultarRelator2Grau(processoDt.getId(), obFabricaConexao));
				} else if (serventiaAtualDt.isSegundoGrau()) {
					//2º grau					
					serventiaCargoAtualDt = ((ServentiaCargoDt)processoResponsavelNe.consultarRelator2Grau(processoDt.getId(), obFabricaConexao));
				} else {
					//1º grau	
					if(serventiaAtualDt.isUPJs()) {
						serventiaCargoAtualDt = processoResponsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.JUIZ_UPJ, true, obFabricaConexao); 
					} else {
						serventiaCargoAtualDt = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoAtivo(processoDt.getId(), id_serventia_atual, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), obFabricaConexao);
					}
				}
				
				if (serventiaCargoAtualDt != null) {
					id_serventiaCargo_atual = serventiaCargoAtualDt.getId();
				} else {
					throw new MensagemException("Não foi possível encontrar o Magistrado atual do processo " + processoDt.getProcessoNumero() +   ", favor entrar em contato com o suporte.");
				}											
				
				// Ao redistribuir os responsáveis pelo processo devem ser desativados
				processoResponsavelNe.desativarResponsaveisProcesso(processoDt, obFabricaConexao);

				// Se não encontrou um responsável deve sortear usando o ponteiro e salvar como responsável, não foi direcionado
				if (serventiaCargoDt == null ) {
					
					id_serventiaCargo_novo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_serventia_nova, id_area_distribuicao_nova);
					
					if (id_serventiaCargo_novo.length()==0 ){
						throw new MensagemException("Não foi possível encontrar o Magistrado para a Serventia:" + id_serventia_nova + " na Área de Distribuição: " + id_area_distribuicao_nova );
					}
					
					if (serventiaNovaDt.isVara()) {								
						// 1º grau
						//Se for redistribuição direcionada (opção 3) deve ignorar esta busca pois o novo responsável já foi selecionado	
						if (serventiaNovaDt.isUPJs()) {									
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.JUIZ_UPJ, logDt, obFabricaConexao);
						} else {									
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
						}
					} else {
						// Turma
						// 2 grau
						//Se for redistribuição direcionada (opção 3) deve ignorar esta busca pois o novo responsável já foi selecionado						
						processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);						
					}
					
				} else {
					//Aqui sempre será redistribuição direcionada (opção 3) deve ignorar a busca anterior pois o novo responsável já foi selecionado					
					id_serventiaCargo_novo = serventiaCargoDt.getId();
					
					// Se for redistribuição por prevenção/conexão, o responsável pelo processo pai já terá sido consultado e
					// deve ser setado como responsável no processo filho.
					//redistribuicaoDt.getProcessoNumeroDependente().length() > 0  => garante q é a opção 2
					//redistribuicaoDt.getId_ServentiaCargo().length() > 0 => garante q é a opção 3
					if (redistribuicaoDt.getOpcaoRedistribuicao().equalsIgnoreCase("2") || redistribuicaoDt.getOpcaoRedistribuicao().equalsIgnoreCase("3")) {
						if (serventiaNovaDt.isUpjTurmaRecursal()) {	
							//turma upj
							//reativando todos os responsáveis, exceto desembargador
							processoResponsavelNe.reativarResponsaveisProcesso(processoDt.getId(), id_serventia_nova, obFabricaConexao);
							//reativando desembargador responsável
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
					    } else if (serventiaNovaDt.isUPJs()) {									
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.JUIZ_UPJ, logDt, obFabricaConexao);
						} else if (serventiaNovaDt.isVara()) {									
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
						} else {
							// Turma						
							// 2 grau							
							//reativando todos os responsáveis, exceto desembargador
							processoResponsavelNe.reativarResponsaveisProcesso(processoDt.getId(), id_serventia_nova, obFabricaConexao);
							//reativando desembargador responsável
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
						}
					} else {
						// Ao retornar a uma serventia que já esteve anteriormente, os responsáveis pelo processo/recurso devem ser ativados
						//reativando todos os responsáveis, exceto DESEMBARGADOR e JUIZ_UPJ
						processoResponsavelNe.reativarResponsaveisProcesso(processoDt.getId(), id_serventia_nova, obFabricaConexao);
						//reativando desembargador responsável
						processoResponsavelNe.reativarMagistradoResposavelProcesso(processoDt.getId(), id_serventiaCargo_novo, obFabricaConexao);
						processoResponsavelNe.ativarRedatorProcesso(processoDt.getId(), id_serventia_nova, obFabricaConexao);
					}
				}
				
				// Se processo que está sendo redistribuído tem um recurso ativo, esse também deve ter a serventia alterada
				RecursoNe recursoNe = new RecursoNe();
				String id_RecursoAtivo = recursoNe.getRecursoAtivo(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
				if (id_RecursoAtivo != null) {
					recursoNe.alterarServentiaRecurso(id_RecursoAtivo, processoDt.getId_Serventia(), id_serventia_nova, logDt, obFabricaConexao);
				}

				/* ---------- PONTEIRO ----------------*/				
				Date dtAgora = new Date();
				new PonteiroLogNe().salvar(new PonteiroLogDt(id_area_distribuicao_atual,PonteiroLogTipoDt.REDISTRIBUICAO,processoDt.getId(),id_serventia_atual,UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi, dtAgora, id_serventiaCargo_atual  ),obFabricaConexao );				
				new PonteiroLogNe().salvar(new PonteiroLogDt(id_area_distribuicao_nova,PonteiroLogTipoDt.DISTRIBUICAO,processoDt.getId(),id_serventia_nova,UsuarioDt.SistemaProjudi, UsuarioServentiaDt.SistemaProjudi, dtAgora, id_serventiaCargo_novo  ),obFabricaConexao );				
				/* ---------- PONTEIRO ----------------*/
				
				//Atualiza a descrição do complemento da movimentação gerada acima inserindo o nome do novo relator
				ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
				ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(id_serventiaCargo_novo);
				movimentacao = new MovimentacaoDt();
				
				movimentacao = movimentacaoNe.gerarMovimentacaoProcessoRedistribuido(processoDt.getId(), idUsuServResponsavelDistribuicao, complemento + " - Distribuído para: " + novoResponsavelDt.getNomeUsuario(), logDt, obFabricaConexao);				
				
				//Ocorrencia 2020/10919 - Erro de nul ao salvar o id_usu no log no método abaixo de new PendenciaNe().gerarConclusaoDecisao
				processoDt.setId_UsuarioLog(redistribuicaoDt.getId_UsuarioLog());
				processoDt.setIpComputadorLog(redistribuicaoDt.getIpComputadorLog());
				
				//Arquiva processo depois de redistribuir o mesmo. Se sim, não gera novas pendências.
				//Se não for arquivar, deve gerar as devidas pendências.
				if(arquivarProcesso) {
					processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ARQUIVADO));
					processoDt.setDataArquivamento(Funcoes.DataHora(new Date()));
					movimentacaoNe.gerarMovimentacaoProcessoArquivado(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), "", logDt, obFabricaConexao);
				} else {
					if (serventiaNovaDt.isSegundoGrau()){
						if(processoDt.isSigiloso()){
							// Gera pendência para a nova serventia Verificar Distribuicao
							new PendenciaNe().gerarConclusaoDecisao(processoDt, UsuarioServentiaDt.SistemaProjudi, id_serventiaCargo_novo, processoDt.getProcessoPrioridadeCodigo(), null, logDt, obFabricaConexao);						
						} else {
							// Gera pendência para a nova serventia Verificar Distribuicao
							new PendenciaNe().gerarPendenciaVerificarRedistribuicaoProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, id_serventia_nova, movimentacao.getId(), null, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
						}
					
					} else {
						if (!(Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC ||
						      Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC_DPVAT ||
							  Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_ENVIAR_PARA_MEDIACAO_CEJUSC)) {
							
							if (Funcoes.StringToInt(movimentacaoProcessoDt.getMovimentacaoTipoCodigo()) == MovimentacaoTipoDt.AUDIENCIA_RETORNAR_DA_CONCILIACAO_OU_MEDIACAO_CEJUSC) {
								// Gera pendência para a nova serventia Verificar Processo
								new PendenciaNe().gerarPendenciaVerificarProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, id_serventia_nova, movimentacaoProcessoDt.getId(), null, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
							} else if (!processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))) {
								// Gera pendência para a nova serventia Verificar Processo
								new PendenciaNe().gerarPendenciaVerificarRedistribuicaoProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, id_serventia_nova, movimentacao.getId(), null, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
							}		
						}					
					}
				}
				
				// redistribuição em lote do distribuidor cível, nessa reditribuição as pendencias são movidas para a proxima serventia.
				if (usuarioDt.isDistribuidor()){
					PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
					//Altera também o responsável das pendências vinculadas ao cargo
					pendenciaResponsavelNe.atualizarResponsaveisPendenciasProcessoDistribuidor(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_serventiaCargo_novo, logDt, obFabricaConexao);
					//Altera o responsável por conclusões vinculadas ao cargo
					pendenciaResponsavelNe.atualizarResponsavelConclusao(processoDt.getId(), processoDt.getServentiaCargoResponsavelDt().getId(), id_serventiaCargo_novo, logDt, obFabricaConexao);
					
					// SE NÃO HÁ PENDÊNCIAS EM ABERTO PARA A SERVENTIA
					PendenciaNe pendenciaNe = new PendenciaNe();
					List pendenciasAbertas = pendenciaNe.consultarPendenciasProcessoRedistribuicaoLote(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
					if (pendenciasAbertas != null) {
						for (Iterator iterator = pendenciasAbertas.iterator(); iterator.hasNext();) {
							String[] pendencias = (String[]) iterator.next();
							//Altera o responsável por pendencias vinculadas a serventia
							pendenciaResponsavelNe.alterarResponsavelPendenciaRedistribuicaoLote(pendencias[0], pendencias[1], pendencias[2], serventiaNovaDt.getId(), logDt, obFabricaConexao);
						}
					}
					
					//Caso hajam audiências abertas, serão enviadas para a nova serventia.
					//Estará neste if apenas se for o distribuidor cível redistribuindo em lote.
//					AudienciaProcessoNe audiProcNe = new AudienciaProcessoNe();
//					audiProcNe.alterarServentiaResponsavelAudienciaRedistribuicaoLoteDistribuidor(processoDt.getId(), id_serventia_nova, id_area_distribuicao_nova, logDt, obFabricaConexao);
					AudienciaNe audienciaNe = new AudienciaNe();
					audienciaNe.alterarServentiaAudienciaRedistribuicaoLoteDistribuidor(processoDt.getId(), processoDt.getId_Serventia(), id_serventia_nova, id_serventiaCargo_novo, logDt, obFabricaConexao);
				}
				
				//Alterar serventia responsável pela pendência tipo (Verificar Guia Pendente) B.O 2018/13138
				PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
				pendenciaResponsavelNe.atualizarServentiaResponsavelPendenciaVerificarGuiaPendente(processoDt.getId(), processoDt.getId_Serventia(), id_serventia_nova, obFabricaConexao);
				
				//salvo todas as alterações do processo uma unica vez
				processoDt.setId_Serventia(id_serventia_nova);
				processoDt.setId_AreaDistribuicao(id_area_distribuicao_nova);
				// Atualiza Classificador processo
				if (movimentacaoProcessoDt.getId_Classificador().length() > 0) {
					processoDt.setId_Classificador( movimentacaoProcessoDt.getId_Classificador());
				}else{
					processoDt.setId_Classificador("null");
				}
				
				processoDt.setId_UsuarioLog(redistribuicaoDt.getId_UsuarioLog());
				processoDt.setIpComputadorLog(redistribuicaoDt.getIpComputadorLog());
				
				this.salvar(processoDt,obFabricaConexao);
				
				//TODO trecho de código comentado por conta da modificação (realizada em janeiro de 2018) no tratamento de processos apensos.
				//Manter o trecho até o começo de 2019. Caso não haja uma reviravolta no tratamento dos apensos, pode excluir esse trecho de código.
				//Verifica se o processo tem apenso. Se tiver, realiza a redistribuição dele.
//				List listaProcessosApensos = this.consultarProcessosApensosRedistribuicao(processoDt.getId());
//				if(listaProcessosApensos != null && listaProcessosApensos.size() > 0){	
//					List processoPassados = new ArrayList();
//					processoPassados.add(processoDt.getId());
//					redistribuirApenso(listaProcessosApensos, usuarioDt.getId_Serventia(), usuarioDt.getGrupoCodigo(), idUsuServResponsavelDistribuicao,id_serventia_atual, id_serventia_nova, id_area_distribuicao_atual, id_area_distribuicao_nova, serventiaNovaDt.getServentia(), processoDt.getProcessoNumeroCompleto(), id_serventiaCargo_atual, id_serventiaCargo_novo,id_cargoTipo,logDt, obFabricaConexao, processoPassados );					
//				}
				
				//Cadastra REPASSE no SPG
				if( processoDt != null 
					&& processoDt.getId() != null 
					&& processoDt.getId().length() > 0 
					&& !processoDt.isSegundoGrau() //Ocorrência 2018/14628
					&& serventiaNovaDt != null 
					&& serventiaNovaDt.getServentiaCodigoExterno() != null 
					&& serventiaNovaDt.getServentiaCodigoExterno().length() > 0 ) {
					
					if( redistribuicaoDt != null && redistribuicaoDt.getListaIdProcessoPorcentagemRepasse() != null && redistribuicaoDt.getListaIdProcessoPorcentagemRepasse().containsKey(processoDt.getId()) ) {
						String porcentagemRepasse = redistribuicaoDt.getListaIdProcessoPorcentagemRepasse().get(processoDt.getId()).toString();
						
						guiaSPGNe.inserirInfoRepasseRedistribuicaoProcesso(obFabricaConexao, processoDt.getId(), serventiaNovaDt.getServentiaCodigoExterno(), porcentagemRepasse, logDt.getId_UsuarioLog(), logDt.getIpComputadorLog(), logIdProcessoRedistribuicaoEmLote);
					}
					
				}
			}
			
			obFabricaConexao.finalizarTransacao();
			
		} catch (Exception e) {			
			obFabricaConexao.cancelarTransacao();			
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();			
		}
	}
			
//	public void redistribuirApenso(List processoApensos,String id_ServentiaUsuario, String grupoCodigoUsuario, String id_UsuarioServentiaResponsavelDistribuicao, String id_serventia_atual, String id_serventia_nova, String id_area_distribuicao_atual, String id_area_distribuicao_nova, String serventia, String numeroProcessoPrincipal,String id_serventiaCargo_atual, String id_serventiaCargo_novo, int id_cargoTipo, LogDt logDt, FabricaConexao obFabricaConexao, List processoPassados ) throws Exception{
//		
//		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();		
//		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
//		
//		String mensagemErro="";
//		
//		for (int i=0; processoApensos!=null && i<processoApensos.size(); i++){
//			ProcessoDt processoApensoDt = (ProcessoDt)processoApensos.get(i);
//			if (processoPassados.contains(processoApensoDt.getId())){	
//				throw new MensagemException("Foi detectado que este processo encontra-se em apensamento circular. É preciso corrigir o apensamento antes de realizar este ato.");
//			}else{				
//				processoPassados.add(processoApensoDt.getId());
//			}
//			mensagemErro += this.podeRedistribuir(processoApensoDt, id_ServentiaUsuario, grupoCodigoUsuario, obFabricaConexao);
//		}
//		
//		if (mensagemErro!= null && mensagemErro.length() > 0){
//			throw new MensagemException(mensagemErro);
//		}
//		
//		for (int i=0; processoApensos!=null && i<processoApensos.size(); i++){
//			ProcessoDt processoApensoDt = (ProcessoDt)processoApensos.get(i);
//			
//			//Ao remeter o processo os responsáveis devem ser desativados
//			processoResponsavelNe.desativarResponsaveisProcesso(processoApensoDt, processoApensoDt.getId_Serventia(), obFabricaConexao);
//			//Salvando nova serventia do processov
//			this.alterarServentiaProcesso(processoApensoDt.getId(), id_ServentiaUsuario, processoApensoDt.getId_Serventia(), id_serventia_nova, id_area_distribuicao_nova, logDt, obFabricaConexao);
//			
//			if (processoResponsavelNe.ativarResponsaveisProcesso(processoApensoDt.getId(), id_serventia_nova, obFabricaConexao)==0){							
//				//salva o novo responsavel pelo processo
//				processoResponsavelNe.salvarProcessoResponsavel(processoApensoDt.getId(), id_serventiaCargo_novo, true,  id_cargoTipo, logDt, obFabricaConexao);
//			}
// 	
//			movimentacaoNe.gerarMovimentacaoProcessoRedistribuido(processoApensoDt.getId(), id_UsuarioServentiaResponsavelDistribuicao,  serventia + " (Apenso ao " + numeroProcessoPrincipal + " )", logDt, obFabricaConexao);
//	
//			/* ---------- PONTEIRO ----------------*/				
//			Date dtAgora = new Date();
//			new PonteiroLogNe().salvar(new PonteiroLogDt(id_area_distribuicao_atual,PonteiroLogTipoDt.REDISTRIBUICAO,processoApensoDt.getId(),id_serventia_atual,UsuarioDt.SistemaProjudi, dtAgora, id_serventiaCargo_atual  ),obFabricaConexao );				
//			new PonteiroLogNe().salvar(new PonteiroLogDt(id_area_distribuicao_nova,PonteiroLogTipoDt.DISTRIBUICAO,processoApensoDt.getId(),id_serventia_nova,UsuarioDt.SistemaProjudi, dtAgora, id_serventiaCargo_novo  ),obFabricaConexao );			
//			
//			List listaProcessosApensos = this.consultarProcessosApensosRedistribuicao(processoApensoDt.getId());
//			if(listaProcessosApensos != null && listaProcessosApensos.size() > 0){
//				redistribuirApenso(listaProcessosApensos, id_ServentiaUsuario, grupoCodigoUsuario, id_UsuarioServentiaResponsavelDistribuicao,id_serventia_atual, id_serventia_nova, id_area_distribuicao_atual, id_area_distribuicao_nova, serventia, processoApensoDt.getProcessoNumeroCompleto(),id_serventiaCargo_atual, id_serventiaCargo_novo,id_cargoTipo,logDt, obFabricaConexao, processoPassados);
//			}
//		}
//			
//	}
	
	/**
	 * Utilizado para encaminhar processos de uma serventia para outra. Encaminhamentos feitos por esta funcionalidade não levam em consideração
	 * ou alteram o ponteiro de distribuição. Este método foi inspirado e é semelhante ao salvarRedistribuicao.
	 * 
	 * @param redistribuicaoDt
	 * @param usuarioDt
	 * @param movimentacaoProcessoDt
	 * @param idPendenciaIgnorar
	 * @param fabricaConexao
	 * @throws MensagemException
	 * @throws Exception
	 * 
	 * @author hrrosa
	 */
	
	public void salvarEncaminhamentoProcesso(RedistribuicaoDt redistribuicaoDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt, String idPendenciaIgnorar, FabricaConexao fabricaConexao) throws MensagemException, Exception {
		this.salvarEncaminhamentoProcesso(redistribuicaoDt, usuarioDt, movimentacaoProcessoDt, idPendenciaIgnorar, false, fabricaConexao);
	}
	
	private void salvarEncaminhamentoProcesso(RedistribuicaoDt redistribuicaoDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt, String idPendenciaIgnorar, Boolean isApensoFilho, FabricaConexao fabricaConexao) throws MensagemException, Exception {
		
		ServentiaNe serventiaNe = new ServentiaNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		//ProcessoNe processoNe = new ProcessoNe();
		ProcessoEncaminhamentoNe procEncaminhamentoNe = new ProcessoEncaminhamentoNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		MovimentacaoDt movimentacaoDt = null;
		//ServentiaCargoDt serventiaCargoDt = null;		
		ServentiaDt serventiaNovaDt = null;
		ProcessoDt processoDt = null;
		//PonteiroLogNe ponteiroLogNe = null;
		ProcessoEncaminhamentoDt procEncaminhamentoDt = null;
		String complemento = "";
		String mensagemErro = "";
		String id_serventiaCargo_novo = "";
		
		FabricaConexao obFabricaConexao = null;

		try {
			
			// Tratar o caso de vir mais de um processo se for utilizar para lote. Atualmente está pegando o último
			// pois a funcionalidade está em produção para apenas um processo por vez.
			if( redistribuicaoDt.getListaProcessos() != null ) {
				
				
				//o atributo fabricaConexao será informado se o método for chamado
				//para redistribuir processos apensados a outro processo que foi
				//redistribuido anteriormente. Motivo: usar a mesma conexão.
				if(fabricaConexao == null) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					obFabricaConexao.iniciarTransacao();
					if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")){
						movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
					}
				} else {
					obFabricaConexao = fabricaConexao;
				}
				
				for(int i = 0 ;  i < redistribuicaoDt.getListaProcessos().size() ; i++) {
					processoDt = (ProcessoDt) redistribuicaoDt.getListaProcessos().get(i);
			

					LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
					
					mensagemErro = "";
					
					mensagemErro = this.podeEncaminhar(processoDt, usuarioDt, idPendenciaIgnorar, isApensoFilho);
					
					if (mensagemErro!= null && mensagemErro.length() > 0){
						throw new MensagemException(mensagemErro);
					}
					
					serventiaNovaDt = serventiaNe.consultarId(redistribuicaoDt.getId_Serventia());
		
					id_serventiaCargo_novo = "";
					complemento = serventiaNovaDt.getServentia();
							
					if(serventiaNovaDt != null && serventiaNovaDt.getId() != null ){
						//quando o id_serventiaCargo_novo não for informado na tela, deverá ser sorteado e informar no complemento da movimentação
						//que a distribuição foi direcionada à Serventia e não ao magistrado.
						if(redistribuicaoDt.isOpcaoRedistribuicao("2")) {
							id_serventiaCargo_novo = redistribuicaoDt.getId_ServentiaCargo();
						} 
						else {
							id_serventiaCargo_novo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(serventiaNovaDt.getId(), redistribuicaoDt.getId_AreaDistribuicao());
						}
						
						//serventiaCargoDt = serventiaCargoNe.consultarId(id_serventiaCargo_novo);	
						if (serventiaNovaDt.getId().length()==0 ||redistribuicaoDt.getId_AreaDistribuicao().length()==0 || id_serventiaCargo_novo.length() == 0){
							throw new MensagemException("Não foi possível encontrar a Serventia, a Área de Distribuição ou o novo Responsável.");
						}
					} else {
						throw new MensagemException("Não foi possível encontrar a Serventia, a Área de Distribuição ou o novo Responsável.");
					}	
						
					// Ao redistribuir os responsáveis pelo processo devem ser desativados
					processoResponsavelNe.desativarResponsaveisProcesso(processoDt, obFabricaConexao);
					
					// TODO: Modificar para utilizar os mesmos métodos da redistribuição. Utilizar o retorno para a serventia de origem
					// como exemplo porque lá já foi modificado.
					if( processoResponsavelNe.temMagistradoInativoProcessoServentia(processoDt.getId(), serventiaNovaDt.getId()) ) {
						// Ao retornar à Serventia que já esteve anteriormente, os responsáveis pelo processo/recurso devem ser ativados
						processoResponsavelNe.ativarResponsaveisProcessoExcetoServentia(processoDt.getId(), serventiaNovaDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
						processoResponsavelNe.ativarRedatorProcessoExcetoServentia(processoDt.getId(), serventiaNovaDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
					} else {					
						if (serventiaNovaDt.isVara()) {
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
						} else {
							// Turma 2 grau
							processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
						}						
					}															
					
					// Se processo que está sendo redistribuído tem um recurso ativo, esse também deve ter a serventia alterada
					RecursoNe recursoNe = new RecursoNe();
					String id_RecursoAtivo = recursoNe.getRecursoAtivo(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
					if (id_RecursoAtivo != null) 
						recursoNe.alterarServentiaRecurso(id_RecursoAtivo, processoDt.getId_Serventia(), serventiaNovaDt.getId(), logDt, obFabricaConexao);
					
					// Consultando se há algum registro deste processo ter sido recebido por encaminhamento.
					// Caso positivo, trata-se de uma devolução e deverá apenas ser alterado o registro correspondente na tabela proc_encaminhamento.
					// Caso negativo, trata-se de um encaminhamento novo e um registro na tabela proc_encaminhamento será criado.
					String idProcEncaminhamento = procEncaminhamentoNe.consultarEncaminhamentoSemDevolucao(processoDt.getId());
					
					
					if( idProcEncaminhamento != null && !idProcEncaminhamento.isEmpty() ) {
						// Retorno
						procEncaminhamentoDt = procEncaminhamentoNe.consultarId(idProcEncaminhamento);
						procEncaminhamentoDt.setId_UsuServRetorno(usuarioDt.getId_UsuarioServentia());
						procEncaminhamentoDt.setDataRetorno( new TJDataHora().getDataHoraFormatadaaaaa_MM_ddHHmmss() );
		
					}
					else {
						// Encaminhamento
						procEncaminhamentoDt = new ProcessoEncaminhamentoDt();
						procEncaminhamentoDt.setId_ServEncaminhamento(serventiaNovaDt.getId());
						procEncaminhamentoDt.setId_UsuServEncaminhamento(usuarioDt.getId_UsuarioServentia());
						procEncaminhamentoDt.setDataEncaminhamento( new TJDataHora().getDataHoraFormatadaaaaa_MM_ddHHmmss() );
						procEncaminhamentoDt.setId_Proc(processoDt.getId());
						procEncaminhamentoDt.setId_ServOrigem(processoDt.getId_Serventia());
					}
					
		
					procEncaminhamentoDt.setId_UsuarioLog(usuarioDt.getId());
					procEncaminhamentoNe.salvar(procEncaminhamentoDt, obFabricaConexao);
					
					//Atualiza a descrição do complemento da movimentação gerada acima inserindo o nome do novo relator
					ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(id_serventiaCargo_novo);
					movimentacaoDt = new MovimentacaoDt();
					movimentacaoDt = movimentacaoNe.gerarMovimentacaoProcessoEncaminhado(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento + " (Encaminhado para: " + novoResponsavelDt.getNomeUsuario() + ")", logDt, obFabricaConexao);
					
					//Arquiva processo depois de redistribuir o mesmo. Se sim, não gera novas pendências.
					//Se não for arquivar, deve gerar as devidas pendências. 
					
					pendenciaNe.gerarPendenciaVerificarProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaNovaDt.getId(), movimentacaoDt.getId(), null, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
					
					
					//salvo todas as alterações do processo uma unica vez
					processoDt.setId_Serventia( serventiaNovaDt.getId());					
					// Atualiza Classificador processo
					if (movimentacaoProcessoDt.getId_Classificador().length() > 0) {
						processoDt.setId_Classificador( movimentacaoProcessoDt.getId_Classificador());
					}else{
						processoDt.setId_Classificador("null");
					}
					//Salvando nova serventia do processo
					//this.alterarServentiaProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), processoDt.getId_Serventia(), serventiaNovaDt.getId(), processoDt.getId_AreaDistribuicao(), logDt, obFabricaConexao);
					processoDt.setId_UsuarioLog(usuarioDt.getId());
					this.salvar(processoDt,obFabricaConexao);
					
					// TODO: Estre trecho está sendo comentado pelo fato de que os processos encaminhados não
					// terão mais os seus apensos encaminhados também. Retirar este código permantentemente
					// se esta modificação se concretizar.
					//
//					//Verifica se o processo tem apenso. Se tiver, realiza o encaminhamento dele.
//					List listaProcessosApensos = this.consultarProcessosApensosRedistribuicao(processoDt.getId());
//					if(listaProcessosApensos != null && listaProcessosApensos.size() > 0){
//						RedistribuicaoDt redistribuicaoFilhoDt = new RedistribuicaoDt();
//						MovimentacaoProcessoDt movimentacaoProcessoFilhoDt = new MovimentacaoProcessoDt();
//						for (int j = 0; j < listaProcessosApensos.size(); j++) {
//							ProcessoDt processoApensoDt = (ProcessoDt)listaProcessosApensos.get(j);
//							processoApensoDt = this.consultarId(processoApensoDt.getId());
//							listaProcessosApensos.set(j, processoApensoDt);
//						}
//						redistribuicaoFilhoDt.setProcessoNumeroDependente(processoDt.getProcessoNumeroCompleto());
//						redistribuicaoFilhoDt.setListaProcessos(listaProcessosApensos);
//						redistribuicaoFilhoDt.setId_ServentiaCargo(id_serventiaCargo_novo);
//						redistribuicaoFilhoDt.setId_Serventia(serventiaNovaDt.getId());
//						redistribuicaoFilhoDt.setId_AreaDistribuicao(redistribuicaoDt.getId_AreaDistribuicao());
//						// O encaminhamento dos processos apensos é por prevenção/conexão - opção 2
//						redistribuicaoFilhoDt.setOpcaoRedistribuicao("2");
//						movimentacaoProcessoFilhoDt = movimentacaoProcessoDt;
//						movimentacaoProcessoFilhoDt.setListaProcessos(listaProcessosApensos);
//						this.salvarEncaminhamentoProcesso(redistribuicaoFilhoDt, usuarioDt, movimentacaoProcessoFilhoDt, null, true, obFabricaConexao);
//					}
						
				}
			}
			
			if(fabricaConexao == null) {
				obFabricaConexao.finalizarTransacao();
			}
		
		} catch (Exception e) {
			if(fabricaConexao == null) {
				movimentacaoNe.cancelarMovimentacaoGenerica(movimentacaoProcessoDt);
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		} finally {
			if(fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	

	/**
	 * Utilizado para encaminhar processos de um gabinete de segundo grau para outros. Encaminhamentos feitos por esta funcionalidade não levam em consideração
	 * ou alteram o ponteiro de distribuição.
	 * 
	 * @param redistribuicaoDt
	 * @param usuarioDt
	 * @param movimentacaoProcessoDt
	 * @param idPendenciaIgnorar
	 * @param fabricaConexao
	 * @throws MensagemException
	 * @throws Exception
	 * 
	 * @author hrrosa
	 */
		
	public void salvarEncaminhamentoProcessoGabinete(RedistribuicaoDt redistribuicaoDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt, String idPendenciaIgnorar, FabricaConexao fabricaConexao) throws MensagemException, Exception {
		this.salvarEncaminhamentoProcessoGabinete(redistribuicaoDt, usuarioDt, movimentacaoProcessoDt, idPendenciaIgnorar, false, fabricaConexao);
	}
		
	private void salvarEncaminhamentoProcessoGabinete(RedistribuicaoDt redistribuicaoDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt, String idPendenciaIgnorar, Boolean isApensoFilho, FabricaConexao fabricaConexao) throws MensagemException, Exception {
		
		ServentiaNe serventiaNe = new ServentiaNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		//ProcessoNe processoNe = new ProcessoNe();
		ProcessoEncaminhamentoNe procEncaminhamentoNe = new ProcessoEncaminhamentoNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		MovimentacaoDt movimentacaoDt = null;
		//ServentiaCargoDt serventiaCargoDt = null;		
		ServentiaDt serventiaNovaDt = null;
		ProcessoDt processoDt = null;
		//PonteiroLogNe ponteiroLogNe = null;
		ProcessoEncaminhamentoDt procEncaminhamentoDt = null;
		String complemento = "";
		String mensagemErro = "";
		String id_serventiaCargo_novo = "";
		
		FabricaConexao obFabricaConexao = null;

		try {
			
			// Tratar o caso de vir mais de um processo se for utilizar para lote. Atualmente está pegando o último
			// pois a funcionalidade está em produção para apenas um processo por vez.
			if( redistribuicaoDt.getListaProcessos() != null ) {
				
				
				//o atributo fabricaConexao será informado se o método for chamado
				//para redistribuir processos apensados a outro processo que foi
				//redistribuido anteriormente. Motivo: usar a mesma conexão.
				if(fabricaConexao == null) {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					obFabricaConexao.iniciarTransacao();
					if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")){
						movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
					}
				} else {
					obFabricaConexao = fabricaConexao;
				}
				
				for(int i = 0 ;  i < redistribuicaoDt.getListaProcessos().size() ; i++) {
					processoDt = (ProcessoDt) redistribuicaoDt.getListaProcessos().get(i);
			

					LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
					
					mensagemErro = "";
					
					mensagemErro = this.podeEncaminhar(processoDt, usuarioDt, idPendenciaIgnorar, isApensoFilho);
					
					if (mensagemErro!= null && mensagemErro.length() > 0){
						throw new MensagemException(mensagemErro);
					}
					
					serventiaNovaDt = serventiaNe.consultarId(processoDt.getId_Serventia());
		
					id_serventiaCargo_novo = redistribuicaoDt.getId_ServentiaCargo();
					complemento = serventiaNovaDt.getServentia();
					
						
					// Ao redistribuir os responsáveis pelo processo devem ser desativados
					processoResponsavelNe.desativarResponsaveisProcesso(processoDt, obFabricaConexao);
					
					
					if( processoResponsavelNe.reativarMagistradoResposavelProcesso(processoDt.getId(), id_serventiaCargo_novo, obFabricaConexao) == 0){
						//Insere novo responsável.
						processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_serventiaCargo_novo, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
					}
					
					// Se processo que está sendo redistribuído tem um recurso ativo, esse também deve ter a serventia alterada
					RecursoNe recursoNe = new RecursoNe();
					String id_RecursoAtivo = recursoNe.getRecursoAtivo(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
					if (id_RecursoAtivo != null) 
						recursoNe.alterarServentiaRecurso(id_RecursoAtivo, processoDt.getId_Serventia(), serventiaNovaDt.getId(), logDt, obFabricaConexao);
					
					// Consultando se há algum registro deste processo ter sido recebido por encaminhamento.
					// Caso positivo, trata-se de uma devolução e deverá apenas ser alterado o registro correspondente na tabela proc_encaminhamento.
					// Caso negativo, trata-se de um encaminhamento novo e um registro na tabela proc_encaminhamento será criado.
					String idProcEncaminhamento = procEncaminhamentoNe.consultarEncaminhamentoSemDevolucao(processoDt.getId());
					
					
					if( idProcEncaminhamento != null && !idProcEncaminhamento.isEmpty() ) {
						//TODO: Possivelmente nunca esteja entrando neste if. Certificar e retirar se for o caso.
						// Retorno
						procEncaminhamentoDt = procEncaminhamentoNe.consultarId(idProcEncaminhamento);
						procEncaminhamentoDt.setId_UsuServRetorno(usuarioDt.getId_UsuarioServentia());
						procEncaminhamentoDt.setDataRetorno( new TJDataHora().getDataHoraFormatadaaaaa_MM_ddHHmmss() );
		
					}
					else {
						// Encaminhamento
						procEncaminhamentoDt = new ProcessoEncaminhamentoDt();
						procEncaminhamentoDt.setId_ServEncaminhamento(serventiaNovaDt.getId());
						procEncaminhamentoDt.setId_UsuServEncaminhamento(usuarioDt.getId_UsuarioServentia());
						procEncaminhamentoDt.setDataEncaminhamento( new TJDataHora().getDataHoraFormatadaaaaa_MM_ddHHmmss() );
						procEncaminhamentoDt.setId_Proc(processoDt.getId());
						procEncaminhamentoDt.setId_ServOrigem(processoDt.getId_Serventia());
					}
					
		
					procEncaminhamentoDt.setId_UsuarioLog(usuarioDt.getId());
					procEncaminhamentoNe.salvar(procEncaminhamentoDt, obFabricaConexao);
					
					//Atualiza a descrição do complemento da movimentação gerada acima inserindo o nome do novo relator
					ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(id_serventiaCargo_novo);
					movimentacaoDt = new MovimentacaoDt();
					movimentacaoDt = movimentacaoNe.gerarMovimentacaoProcessoEncaminhado(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento + " (Encaminhado para: " + novoResponsavelDt.getNomeUsuario() + ")", logDt, obFabricaConexao);
					
					
					pendenciaNe.gerarPendenciaConclusoRelator(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaNovaDt.getId(), id_serventiaCargo_novo, movimentacaoDt.getId(), null, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());

					
					//salvo todas as alterações do processo uma unica vez
					processoDt.setId_Serventia( serventiaNovaDt.getId());					
					// Atualiza Classificador processo
					if (movimentacaoProcessoDt.getId_Classificador().length() > 0) {
						processoDt.setId_Classificador( movimentacaoProcessoDt.getId_Classificador());
					}else{
						processoDt.setId_Classificador("null");
					}
					//Salvando nova serventia do processo
					//this.alterarServentiaProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), processoDt.getId_Serventia(), serventiaNovaDt.getId(), processoDt.getId_AreaDistribuicao(), logDt, obFabricaConexao);
					processoDt.setId_UsuarioLog(usuarioDt.getId());
					this.salvar(processoDt,obFabricaConexao);
					
				}
			}
			
			if(fabricaConexao == null) {
				obFabricaConexao.finalizarTransacao();
			}
		
		} catch (Exception e) {
			if(fabricaConexao == null) {
				movimentacaoNe.cancelarMovimentacaoGenerica(movimentacaoProcessoDt);
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		} finally {
			if(fabricaConexao == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Retorna o responsável "Ativo" pelo processo de acordo com o grupo do usuário que está consultando.
	 * Se usuário é analista da vara verá o juiz responsável, ou então, se usuário é analista da turma
	 * deverá ver o relator responsável.
	 * 
	 * @param id_Processo, identificação do processo
	 * @param grupoTipoCodigo, determina o grupo do usuário logado
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarResponsavelProcesso(String id_Processo, String grupoTipoCodigo, String serventiaTipoCodigo, String serventiaSubTipoCodigo) throws Exception {
		ServentiaCargoDt serventiaCargo = new ServentiaCargoDt();
		serventiaCargo = new ProcessoResponsavelNe().consultarResponsavelProcesso(id_Processo, grupoTipoCodigo, serventiaTipoCodigo, serventiaSubTipoCodigo);
		return serventiaCargo;
	}
	

	/**
	 * Método que controla a redistribuição de um processo dentro da própria serventia.
	 * 
	 * @param redistribuicaoDt - processo a ser redistribuido
	 * @param usuarioDt - usuário solicitante da redistribuição
	 * @param movimentacaoProcessoDt - movimentação que será gerada ao redistribuir o processo
	 * @param idNovoResponsavelProcesso - ID do relator. Só deve ser utilizado quando o relator já está definido e vai direcionar a redistribuição. É usado para redistribuir processos apensos.
	 * @param CriarFabricaConexao - NÃO É OBRIGATÓRIO - o atributo fabricaConexao será informado se o método for chamado para redistribuir processos apensados a outro processo que foi
	 * 							redistribuido anteriormente. Motivo: usar a mesma conexão.
	 * 
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void salvarRedistribuicaoPropriaServentia(RedistribuicaoDt redistribuicaoDt, UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt, String idNovoResponsavelProcesso) throws MensagemException, Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		
		String complemento = "";
		FabricaConexao obFabricaConexao = null;

		try {
			//o atributo fabricaConexao será informado se o método for chamado para redistribuir processos apensados a outro processo que foi
			//redistribuido anteriormente (na mesma transação). Motivo: usar a mesma conexão.
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			if (movimentacaoProcessoDt.getRedirecionaOutraServentia() != null && !movimentacaoProcessoDt.getRedirecionaOutraServentia().equals("")){
				movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
			}
			
			
			LogDt logDt = new LogDt(usuarioDt.getId(), redistribuicaoDt.getIpComputadorLog());
			List processos = redistribuicaoDt.getListaProcessos();

			for (int i = 0; i < processos.size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) processos.get(i);
				
				// Consultando os dados do responsável atual do processo
				ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
				ServentiaCargoDt responsavelAtualProcesso = new ServentiaCargoDt();
				if(usuarioDt.isServentiaUPJFamilia() || usuarioDt.isServentiaUPJSucessoes() || usuarioDt.isServentiaUPJCriminal()) {
					responsavelAtualProcesso = processoResponsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.JUIZ_UPJ, true, obFabricaConexao);
				} else {
					//se o usuário não for de UPJ, ele será usuário de segundo grau
					responsavelAtualProcesso = processoResponsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, true, obFabricaConexao);
				}
				
				if(responsavelAtualProcesso == null) {
					throw new MensagemException("Não foi possível localizar o responsável atual pelo processo. Entrar em contato com o suporte.");
				}
				
				//para atender o provimento 16/2012 da CGJ
				// buscando cargos para redistribuir o processo
				if(idNovoResponsavelProcesso == null || idNovoResponsavelProcesso.equalsIgnoreCase("")) {
					idNovoResponsavelProcesso = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(processoDt.getId_Serventia(),responsavelAtualProcesso.getId(), responsavelAtualProcesso.getId());
				}

				if (idNovoResponsavelProcesso!= null && idNovoResponsavelProcesso.length()>0) {
					// substitui o responsável atual do processo
					responsavelNe.alterarResponsavelProcesso(processoDt.getId(), responsavelAtualProcesso.getId(), idNovoResponsavelProcesso, logDt, obFabricaConexao, null);
										
					/* ---------- PONTEIRO ----------------*/
					///salvo o ponteiro
					Date dtAgora = new Date();
					new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.GANHO_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, idNovoResponsavelProcesso  ),obFabricaConexao );
					
					new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicao(), PonteiroLogTipoDt.PERDA_RESPONSABILIDADE,processoDt.getId(),processoDt.getId_Serventia(),  usuarioDt.getId(), usuarioDt.getId_UsuarioServentia(), dtAgora, responsavelAtualProcesso.getId()  ),obFabricaConexao );
					/* ---------- PONTEIRO ----------------*/					

				} else {
					throw new MensagemException("Não foi possível localizar um novo relator para o processo. Verifique o cadastro da serventia.");
				}
				
				// Gerando movimentação de processo redistribuído na própria serventia
				ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
				ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(idNovoResponsavelProcesso);
				complemento = "Processo redistribuído na própria Serventia. Distribuído para: " + novoResponsavelDt.getNomeUsuario() + " (" + novoResponsavelDt.getServentia() + ")";
				
				movimentacaoNe.gerarMovimentacaoProcessoRedistribuido(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);				

				// Salvando log da transação
				LogDt obLogDt = new LogDt("Processo", processoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Redistribuicao), processoDt.getPropriedades(), processoDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
				
				//TODO trecho de código comentado por conta da modificação (realizada em janeiro de 2018) no tratamento de processos apensos.
				//Manter o trecho até o começo de 2019. Caso não haja uma reviravolta no tratamento dos apensos, pode excluir esse trecho de código.
				//Verifica se o processo tem apenso. Se tiver, realiza a redistribuição dele.
//				List listaProcessosApensos = this.consultarProcessosApensosRedistribuicao(processoDt.getId());
//				if(listaProcessosApensos != null && listaProcessosApensos.size() > 0){	
//					List processoPassados = new ArrayList();
//					processoPassados.add(processoDt.getId());
//					redistribuirApenso(listaProcessosApensos, usuarioDt.getId_Serventia(), usuarioDt.getGrupoCodigo(), usuarioDt.getId_UsuarioServentia(), processoDt.getId_Serventia(), processoDt.getId_Serventia(), processoDt.getId_AreaDistribuicao(),processoDt.getId_AreaDistribuicao(), processoDt.getServentia(), processoDt.getProcessoNumeroCompleto(),relatorProcesso.getId(), id_ServentiaCargoRelator,Funcoes.StringToInt(novoResponsavelDt.getId_CargoTipo()),logDt, obFabricaConexao, processoPassados );
//				}
				
			}			
			obFabricaConexao.finalizarTransacao();
			
		} catch (Exception e) {			
			obFabricaConexao.cancelarTransacao();		
			throw e;
		} finally {
			
			obFabricaConexao.fecharConexao();			
		}
	}
	
	/**
	 * Método que faz o repasse dos autos do Relator para o Presidente da Unidade de 2º grau (Câmaras, Seção). Esse repasse será feito
	 * sem mexer no ponteiro de distribuição, por isso o método foi desenvolvido separado. 
	 * @param processoDt - Processo que será enviado ao Presidente
	 * @param usuarioDt - usuário que está realizando o envio
	 * @throws MensagemException 
	 * @throws Exception
	 * 
	 * @author hmgodinho
	 */
	public void enviarProcessoPresidenteUnidade(ProcessoDt processoDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		String complemento = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
			
			//Os responsáveis pelo processo que trabalham no gabinete devem ser desativados
			ServentiaCargoDt servCargoRelator = processoResponsavelNe.consultarRelatorResponsavelProcessoSegundoGrau(processoDt.getId(), obFabricaConexao);
			processoResponsavelNe.desativarGabineteResponsavelProcesso(processoDt.getId(), servCargoRelator.getId_Serventia(), obFabricaConexao);

			//Localizando os dados do presidente da unidade
			ServentiaCargoDt servCargoPresidente = serventiaCargoNe.getPresidenteSegundoGrau(processoDt.getId_Serventia(), obFabricaConexao);
				
			if (servCargoPresidente != null) {
				//Consultando se o Presidente já autou no processo
				ServentiaCargoDt presidenteInativo = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoInativo(processoDt.getId(), servCargoPresidente.getId_Serventia(), String.valueOf(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU), obFabricaConexao);
						
				//Se localizou o presidente basta reativar o registro
				if(presidenteInativo != null) {
					processoResponsavelNe.ativarGabineteResponsavelProcesso(processoDt.getId(), presidenteInativo.getId_Serventia(), obFabricaConexao);
					//sobrescreve o servCargoPresidente para usar os dados no restante do método
					servCargoPresidente = presidenteInativo;					
				} else {
					//Se não localizou o presidente, insere um novo registro de responsável com as informações do presidente da unidade
					processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), servCargoPresidente.getId(), true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
				}
			} else {
				throw new MensagemException("Não foi possível encontrar o Presidente da unidade, favor entrar em contato com o suporte.");
			}
				
			// Gerando movimentação de troca de responsável
			ServentiaDt serventiaProcessoDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
			
			//Se a serventia for Câmara Criminal, a movimentação e a fase processual serão diferentes das demais
			complemento = "Para o Presidente da " + serventiaProcessoDt.getServentia() + ".";
			if(serventiaProcessoDt.isCamaraCriminal()) {
				this.alterarFaseProcesso(processoDt, ProcessoFaseDt.COMPETENCIA_PRESIDENCIA_SERVENTIA, obFabricaConexao);
			} else {
				complemento += " (Execução de Acórdão)";
				this.alterarFaseProcesso(processoDt, ProcessoFaseDt.EXECUCAO_ACORDAO, obFabricaConexao);
			}
			movimentacaoNe.gerarMovimentacaoTrocaResponsavel(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);			

			// Salvando log da transação
			LogDt obLogDt = new LogDt("Processo", processoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.TrocaResponsavelProcesso), processoDt.getPropriedades(), processoDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
				
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {			
			obFabricaConexao.cancelarTransacao();		
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();			
		}
	}
	
	/**
	 * Método que faz o retorno dos autos do Presidente para o Relator da Unidade de 2º grau (Câmaras, Seção). Esse repasse será feito
	 * sem mexer no ponteiro de distribuição, por isso o método foi desenvolvido separado. 
	 * @param processoDt - Processo que será retornado ao Relator
	 * @param usuarioDt - usuário que está realizando o envio
	 * @throws MensagemException 
	 * @throws Exception
	 * 
	 * @author hmgodinho
	 */
	public void retornarAutosRelatorProcesso(ProcessoDt processoDt, UsuarioDt usuarioDt) throws MensagemException, Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		String complemento = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
			ServentiaCargoDt novoResponsavelDt = null;

			//Os responsáveis pelo processo que trabalham no gabinete devem ser desativados
			ServentiaCargoDt servCargoRelator = processoResponsavelNe.consultarRelatorResponsavelProcessoSegundoGrau(processoDt.getId(), obFabricaConexao);
			processoResponsavelNe.desativarGabineteResponsavelProcesso(processoDt.getId(), servCargoRelator.getId_Serventia(), obFabricaConexao);

			//Localizando os dados do relator do processo 
			String idServCargoRelator = serventiaCargoNe.getUltimoRelatorProcesso(processoDt.getId_Serventia(), processoDt.getId(), processoDt.getId_AreaDistribuicao());
			if (idServCargoRelator != null) {
				//Reativando os responsáveis no gabinete do relator
				novoResponsavelDt = serventiaCargoNe.consultarId(idServCargoRelator);
				processoResponsavelNe.ativarGabineteResponsavelProcesso(processoDt.getId(), novoResponsavelDt.getId_Serventia(), obFabricaConexao);
			} else {
				throw new MensagemException("Não foi possível encontrar o Relator do processo, favor entrar em contato com o suporte.");
			}
				
			// Gerando movimentação de troca de responsável
			complemento = "Para o Relator do processo: " + novoResponsavelDt.getNomeUsuario() + " (" + novoResponsavelDt.getServentia() + ")";
			movimentacaoNe.gerarMovimentacaoTrocaResponsavel(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);		

			//Atualiza dados do processo
			String idRecursoAtivo = new RecursoNe().getRecursoAtivo(processoDt.getId(), processoDt.getId_Serventia());
			if(idRecursoAtivo != null) {
				this.alterarFaseProcesso(processoDt, ProcessoFaseDt.RECURSO, obFabricaConexao);
			} else {
				this.alterarFaseProcesso(processoDt, ProcessoFaseDt.CONHECIMENTO, obFabricaConexao);
			}

			// Salvando log da transação
			LogDt obLogDt = new LogDt("Processo", processoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.TrocaResponsavelProcesso), processoDt.getPropriedades(), processoDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
				
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {			
			obFabricaConexao.cancelarTransacao();		
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();			
		}
	}

	/**
	 * Método que verifica se um processo(recurso inominado) pode ser retotrnado
	 * à serventia de origem.
	 * 
	 * Verifica se: - Não há sessões em aberto - Não há pendências em aberto -
	 * Não há autos conclusos - Se trata realmente de recurso inominado
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 * @return
	 */
	public String podeRetornarServentiaOrigem(ProcessoDt processoDt, String grupoCodigo) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			

			// Se não há audiências em aberto
			if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há sessão em aberto. \n";
			}

			// Se não há pendências em aberto
			if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) {
				stRetorno += "Há pendência(s) em aberto.";
			}

			// Verifica se há conclusões em aberto
			if (!GrupoDt.isJuiz(grupoCodigo) && 					
					pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há autos conclusos em aberto. \n";
			}
			
			// Só pode permitir retorno para recurso inominado
			// Se tentar retornar um processo que não foi Encaminhado e nem enviado á instância superior
			// também irá entrar neste if.
			if (processoDt.getId_Recurso().length() == 0) {
				stRetorno += " Não é possível retornar à origem um processo de 2º grau. \n";
			}

			if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode retornar à serventia de origem. " + stRetorno;

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	

	/**
	 * Método que verifica se um processo pode ser retotrnado
	 * à serventia de origem no caso de Encaminhamento.
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author hrrosa
	 * @return
	 */
	public String podeRetornarServentiaOrigemEncaminhamento(ProcessoDt processoDt, String grupoCodigo, Boolean isApensoFilho) throws Exception {
		return this.podeRetornarServentiaOrigemEncaminhamento(processoDt, grupoCodigo, isApensoFilho, null);
	}

	public String podeRetornarServentiaOrigemEncaminhamento(ProcessoDt processoDt, String grupoCodigo, Boolean isApensoFilho, FabricaConexao fabrica) throws Exception {
		FabricaConexao obFabricaConexao = null;
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoEncaminhamentoNe processoEncaminhamentoNe = new ProcessoEncaminhamentoNe();
		String stRetorno = "";
		
		
		if (fabrica == null) {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
        } 
		else {
            obFabricaConexao = fabrica;
		}
		
		try {

			// Se não há audiências em aberto
			if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há Audiência ou Sessão em Aberto. \n";
			}

			// Se não há pendências em aberto
			if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) {
				stRetorno += "Há Pendência(s) em aberto. \n";
			}

			// Verifica se há conclusões em aberto
			if (!GrupoDt.isJuiz(grupoCodigo) && 					
					pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há Autos Conclusos em Aberto. \n";
			}
			
			// Se o método podeEncaminhar() trazer true é porque não tem nenhumm encaminhamento sem retorno, em aberto.
			// Portanto, neste caso, não deve deixar retornar.
			if(processoEncaminhamentoNe.podeEncaminhar(processoDt.getId())){
				stRetorno += "Não foi encontrado um encaminhamento registrado para este processo. \n";
			}
			
			// A partir de 30/08/2018 pode retornar diretamente o apenso. Retirar as linhas comentadas se esta nova característica persistir.
			// Verifica se é apenso.
			//if(processoDt.isApenso() && isApensoFilho == false){
			//	stRetorno += "Impossível retornar um apenso, utilize o processo principal " + processoDt.getProcessoNumeroPrincipal() + ".\n";
			//}

			if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode retornar à Serventia de Origem. " + stRetorno;
			
			if (fabrica == null) obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
            if (fabrica == null) obFabricaConexao.cancelarTransacao();
            throw e;
            
        } finally {
			 if (fabrica == null) obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Chama método que realiza a alteração do Classificador de um Processo.
	 * Nesse caso a conexão deve ser criada pois a alteração está sendo feita
	 * isoladamente.
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param id_ClassificadorAnterior
	 *            , classificador antigo
	 * @param id_ClassificadorNovo
	 *            , novo classificador
	 * @param logDt
	 *            , dados do log
	 * 
	 * @author msapaula
	 */
	public void alterarClassificadorProcesso(String id_Processo, String id_ClassificadorAnterior, String id_ClassificadorNovo, LogDt logDt) throws Exception {
		this.alterarClassificadorProcesso(id_Processo, id_ClassificadorAnterior, id_ClassificadorNovo, logDt, null);
	}
	
	/**
	 * Chama método que realiza a alteração do Classificador de uma pendência.
	 * Nesse caso a conexão deve ser criada pois a alteração está sendo feita
	 * isoladamente.
	 * 
	 * @param id_Pendencia
	 *            , identificação do pendencia
	 * @param id_ClassificadorAnterior
	 *            , classificador antigo
	 * @param id_ClassificadorNovo
	 *            , novo classificador
	 * @param logDt
	 *            , dados do log
	 * 
	 * @author lsbernardes
	 */
	public void alterarClassificadorPendencia(String id_Pendencia, String id_ClassificadorAnterior, String id_ClassificadorNovo, LogDt logDt) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		pendenciaNe.alterarClassificadorPendencia(id_Pendencia, id_ClassificadorAnterior, id_ClassificadorNovo, logDt, null);
	}

	/**
	 * Altera o classificador de um processo
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param id_ClassificadorAnterior
	 *            , classificador antigo
	 * @param id_ClassificadorNovo
	 *            , novo classificador
	 * @param logDt
	 *            , dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarClassificadorProcesso(String id_Processo, String id_ClassificadorAnterior, String id_ClassificadorNovo, LogDt logDt, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else
				obFabricaConexao = conexao;
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Processo:" + id_Processo + ";Id_Classificador:" + id_ClassificadorAnterior + "]";
			String valorNovo = "[Id_Processo:" + id_Processo + ";Id_Classificador:" + id_ClassificadorNovo + "]";

			obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			obPersistencia.alterarClassificadorProcesso(id_Processo, id_ClassificadorNovo);

			obLog.salvar(obLogDt, obFabricaConexao);
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Atualiza a serventia de um processo em virtude de redistribuição e gera
	 * log com dados
	 * 
	 * @param id_Processo
	 *            identificação do processo
	 * @param id_Serventia
	 *            nova serventia do processo
	 * @param conexao
	 *            conexão ativa
	 * 
	 * @author msapaula
	 */
//	public void alterarServentiaProcesso(String id_Processo, String id_ServentiaUsuarioLogado, String id_ServentiaAnterior, String id_ServentiaNova, String id_AreaDistribuicaoNova, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
//		LogDt obLogDt;
//		String valorAtual = "[Id_Processo:" + id_Processo + ";Id_Serventia:" + id_ServentiaAnterior + "]";
//		String valorNovo = "[Id_Processo:" + id_Processo + ";Id_Serventia:" + id_ServentiaNova + "]";
//
//		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
//		obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Redistribuicao), valorAtual, valorNovo);
//		obPersistencia.alterarServentiaProcesso(id_Processo, id_ServentiaUsuarioLogado, id_ServentiaNova, id_AreaDistribuicaoNova);
//
//		obLog.salvar(obLogDt, obFabricaConexao);	
//	}
//
//	/**
//	 * Atualiza a serventia de um processo em virtude de redistribuição
//	 * sem verificar se o usuário está na mesma serventia e gera
//	 * log com dados
//	 * ATENÇÃO: ESTA FUNCIONALIDADE FOI FEITA PARA A REDISTRIBUIÇÃO EM LOTE DO DISTRIBUIDOR E DESEVE SER
//	 * USADA COM CAUTELA POIS NÃO VALIDA SE O USUÁRIO ESTÁ REDISTRIBUINDO O PROCESSO DA MESMA SERVENTIA
//	 * EM QUE ELE SE ENCONTRA.
//	 * 
//	 * @param id_Processo
//	 *            identificação do processo
//	 * @param id_Serventia
//	 *            nova serventia do processo
//	 * @param conexao
//	 *            conexão ativa
//	 * 
//	 * @author hrrosa
//	 */
//	public void alterarServentiaProcessoDistribuidor(String id_Processo, String id_ServentiaAnterior, String id_ServentiaNova, String id_AreaDistribuicaoNova, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
//		LogDt obLogDt;
//		String valorAtual = "[Id_Processo:" + id_Processo + ";Id_Serventia:" + id_ServentiaAnterior + "]";
//		String valorNovo = "[Id_Processo:" + id_Processo + ";Id_Serventia:" + id_ServentiaNova + "]";
//
//		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
//		obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Redistribuicao), valorAtual, valorNovo);
//		obPersistencia.alterarServentiaProcessoDistribuidor(id_Processo, id_ServentiaNova, id_AreaDistribuicaoNova);
//
//		obLog.salvar(obLogDt, obFabricaConexao);	
//	}
	
	/**
	 * Atualiza os dados de um processo em virtude de um recurso não originário
	 * e gera log da alteração
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param id_ServentiaAnterior
	 *            , serventia anterior do processo
	 * @param faseAnterior
	 *            , fase anterior
	 * @param conexao
	 *            conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoRecursoNaoOriginario(ProcessoDt processoDt, String id_ServentiaAnterior, String faseAnterior, String id_ProcessoPrioridadeAnterior, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		String valorAtual = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + id_ServentiaAnterior + ";Id_AreaDistribuicaoAnterior:" + processoDt.getId_AreaDistribuicao() + ";ProcessoFaseCodigo:" + faseAnterior + ";Id_ProcessoPrioridadeAnterior:" + id_ProcessoPrioridadeAnterior + "]";
		String valorNovo = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + processoDt.getId_Serventia() + ";Id_AreaDistribuicaoRecursal:" + processoDt.getId_AreaDistribuicaoRecursal() + ";ProcessoFaseCodigo:" + processoDt.getProcessoFaseCodigo() + ";ProcessoPrioridadeCodigo:" + processoDt.getProcessoPrioridadeCodigo() + "]";

		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.EnvioRecurso), valorAtual, valorNovo);
		obPersistencia.alterarProcessoRecursoNaoOriginario(processoDt.getId(), processoDt.getId_Serventia(), processoDt.getId_AreaDistribuicaoRecursal(), processoDt.getProcessoFaseCodigo(), processoDt.getId_ProcessoPrioridade());

		obLog.salvar(obLogDt, obFabricaConexao);	
	}
	
	/**
	 * Método que altera os dados do processo durante o envio para a instância superior.
	 * @param processoDt - processo a ser enviado
	 * @param id_ServentiaOrigem - id da serventia de origem do processo
	 * @param faseAnterior - fase anterior do processo
	 * @param id_ProcessoPrioridadeAnterior - id da prioridade anterior do processo
	 * @param logDt - log da alteração
	 * @param obFabricaConexao
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarProcessoEnvioInstanciaSuperior(ProcessoDt processoDt, String id_ServentiaOrigem, String faseAnterior, String id_ProcessoPrioridadeAnterior, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		String valorAtual = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + id_ServentiaOrigem + ";Id_AreaDistribuicaoAnterior:" + processoDt.getId_AreaDistribuicao() + ";ProcessoFaseCodigo:" + faseAnterior + ";Id_ProcessoPrioridadeAnterior:" + id_ProcessoPrioridadeAnterior + "]";
		String valorNovo = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + processoDt.getId_Serventia() + ";Id_AreaDistribuicaoRecursal:" + processoDt.getId_AreaDistribuicaoRecursal() + ";ProcessoFaseCodigo:" + processoDt.getProcessoFaseCodigo() + ";ProcessoPrioridadeCodigo:" + processoDt.getProcessoPrioridadeCodigo() + "]";

		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.EnvioRecurso), valorAtual, valorNovo);
		obPersistencia.alterarProcessoEnvioInstanciaSuperior(processoDt.getId(), id_ServentiaOrigem, processoDt.getId_Serventia(), processoDt.getId_AreaDistribuicaoRecursal(), processoDt.getProcessoFaseCodigo(), processoDt.getId_ProcessoPrioridade());

		obLog.salvar(obLogDt, obFabricaConexao);	
	}
	
	/**
	 * Atualiza os dados de um processo em virtude de um recurso não originário
	 * e gera log da alteração
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param id_ServentiaAnterior
	 *            , serventia anterior do processo
	 * @param faseAnterior
	 *            , fase anterior
	 * @param conexao
	 *            conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoRecursoNaoOriginarioConversaoProcesso(ProcessoDt processoDt, String id_ServentiaAnterior, String faseAnterior, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		String valorAtual = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + id_ServentiaAnterior + ";Id_AreaDistribuicaoAnterior:" + processoDt.getId_AreaDistribuicao() + ";ProcessoFaseCodigo:" + faseAnterior + "]";
		String valorNovo = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + processoDt.getId_Serventia() + ";Id_AreaDistribuicaoRecursal:" + processoDt.getId_AreaDistribuicaoRecursal() + ";ProcessoFaseCodigo:" + processoDt.getProcessoFaseCodigo() + "]";

		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.ConverterProcessoRecurso), valorAtual, valorNovo);
		obPersistencia.alterarProcessoRecursoNaoOriginario(processoDt.getId(), processoDt.getId_Serventia(), processoDt.getId_AreaDistribuicaoRecursal(), processoDt.getProcessoFaseCodigo(), "");

		obLog.salvar(obLogDt, obFabricaConexao);
	}

	/**
	 * Método que verifica se um processo pode ser arquivado. Verifica se: - Não
	 * há autos conclusos - Não há audiências/sessões em aberto - Não há
	 * pendências em aberto - Processo está ativo - No caso de turmas recursais,
	 * deve verificar se processo é do 2º grau
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String podeArquivar(ProcessoDt processoDt, String grupoCodigo) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			

			// Verifica se há conclusões em aberto
			if ((Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZES_VARA) && 
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU) &&
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZES_TURMA_RECURSAL) &&
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL) && 
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL) && 
					pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) {
				stRetorno = "Há Autos Conclusos em Aberto. \n";
			}

			// Verifica audiências pendentes
			if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
				if (processoDt.getId_Recurso().length() == 0) stRetorno += "Há Audiência(s) em Aberto. \n";
				else
					stRetorno += "Há Sessão em Aberto. \n";
			}

			if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) stRetorno += "Há pendência(s) em aberto. \n";

			// Se processo não está com dados completos, deve consultar
			if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) processoDt = consultarId(processoDt.getId());

			// Se processo está ativo
			if (! (Funcoes.StringToInt(processoDt.getProcessoStatusCodigo()) == ProcessoStatusDt.ATIVO
					|| Funcoes.StringToInt(processoDt.getProcessoStatusCodigo()) == ProcessoStatusDt.CALCULO)) stRetorno += " Processo não está ativo. \n";

			// Só pode permitir arquivamento para ações do 2º grau e não para
			// recurso inominado
			if (processoDt.getId_Recurso().length() > 0) {
				stRetorno += " Não é possível Arquivar um Recurso Inominado. \n";
			}

			//verifica se tem mandado de prisão não cumprido
			if (new MandadoPrisaoNe().isExisteMandadoPrisaoProcesso_NaoCumprido(processoDt.getId(), obFabricaConexao)){
				stRetorno += " Há Mandado(s) de Prisão não Cumprido. \n";
			}
			
			if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode ser arquivado. " + stRetorno;

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	public String podeAtivarProcessoCalculo(ProcessoDt processoDt, String grupoCodigo) throws Exception {
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			

			// Se processo não está com dados completos, deve consultar
			if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) processoDt = consultarId(processoDt.getId());

			// Se processo é execpen
			if (! (Funcoes.StringToInt(processoDt.getProcessoStatusCodigo()) == ProcessoStatusDt.CALCULO)) stRetorno += " Não é possível Ativar um Processo que não seja Cálculo. \n";
			
			if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode ser ativado. " + stRetorno;

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Método que arquiva um processo
	 * 
	 * @param processoDt
	 *            objeto processo a ser arquivado
	 * @param logDt
	 *            objeto com dados do log
	 * @param id_UsuarioServentia
	 *            usuario que esta arquivando o processo
	 * @param obFabricaConexao
	 *            conexao ativa
	 * 
	 * @author msapaula
	 */
	public void arquivarProcesso(ProcessoDt processoDt, LogDt logDt, String id_UsuarioServentia, String complemento, String id_ProcArquivamentoTipo, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		
		//Advogados do processo
		List listAdvogados = this.consultarAdvogadosProcesso(processoDt.getId_Processo(), obFabricaConexao);
		//enviar email avisando do arquivameto para os advogados
		this.enviarEmailArquivamento(processoDt.getServentia(), processoDt.getProcessoNumeroCompleto(), listAdvogados);

		// Seta os valores em processo pois sera necessario na hora de mostrar dados do processo na tela
		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ARQUIVADO));
		processoDt.setDataArquivamento(Funcoes.DataHora(new Date()));

		String valorAtual = "[Id_Processo:" + processoDt.getId() + ", Id_ProcessoArquivamentoTipo: " + "]";
		String valorNovo = "";
		
		obPersistencia.alterarProcessoArquivamento(processoDt);
		
		if(processoDt.isCriminal()){
    		// Verifica se é processo criminal e consulta informações adicionais
			if (processoDt.getProcessoCriminalDt() == null || processoDt.getProcessoCriminalDt().getId().length()==0) {
				ProcessoCriminalNe processoCriminalNe = new ProcessoCriminalNe();
				processoDt.setProcessoCriminalDt(processoCriminalNe.consultarIdProcesso(processoDt.getId(), obFabricaConexao));
				processoDt.getProcessoCriminalDt().setId_Processo(processoDt.getId());
			}
			processoDt.getProcessoCriminalDt().setId_ProcessoArquivamentoTipo(id_ProcArquivamentoTipo);
			processoDt.getProcessoCriminalDt().setId_UsuarioLog(logDt.getId_UsuarioLog());
			processoDt.getProcessoCriminalDt().setIpComputadorLog(logDt.getIpComputador());
			new ProcessoCriminalNe().salvar(processoDt.getProcessoCriminalDt(), obFabricaConexao);
			valorNovo = "[Id_Processo:" + processoDt.getId() + ", Id_ProcessoArquivamentoTipo:"+ processoDt.getProcessoCriminalDt().getId_ProcessoArquivamentoTipo()+"]";
		}
		
		// Atualiza dados do processo e gera log do arquivamento
		LogDt obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Arquivamento), valorAtual, valorNovo);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO ARQUIVADO
		movimentacaoNe.gerarMovimentacaoProcessoArquivado(processoDt.getId(), id_UsuarioServentia, complemento, logDt, obFabricaConexao);
	}
	
	/**
	 * Método que ativa um processo de cálculo
	 * 
	 * @param processoDt
	 *            objeto processo a ser ativado
	 * @param logDt
	 *            objeto com dados do log
	 * @param id_UsuarioServentia
	 *            usuario que esta arquivando o processo
	 * @param obFabricaConexao
	 *            conexao ativa
	 * 
	 * @author msapaula
	 */
	public void ativarProcessoCalculo(ProcessoDt processoDt, LogDt logDt, String id_UsuarioServentia, String complemento, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoStatusNe processoStatusNe = new ProcessoStatusNe();
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		
		obPersistencia.alterarStatusProcessoTipoProcesso(processoDt.getId(), ProcessoStatusDt.ATIVO, ProcessoTipoDt.EXECUCAO_DA_PENA);
		
		// Gera movimentação PROCESSO CALCULO ATIVADO
		movimentacaoNe.gerarMovimentacaoProcessoCalculoAtivado(processoDt.getId(), id_UsuarioServentia, complemento, logDt, obFabricaConexao);
		
		// Seta os valores em processo pois sera necessario na hora de
		// mostrar dados do processo na tela
		String valorAtual = processoDt.getPropriedades();
		
		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
		processoDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA));
		
		ProcessoStatusDt processoStatusDt = processoStatusNe.consultarProcessoStatusCodigo(ProcessoStatusDt.ATIVO);
		if (processoStatusDt != null) {
			processoDt.setProcessoStatus(processoStatusDt.getProcessoStatus());
			processoDt.setId_ProcessoStatus(processoStatusDt.getId());
		}
		
		ProcessoTipoDt processoTipoDt = processoTipoNe.consultarProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA));
		if (processoTipoDt != null) {
			processoDt.setProcessoTipo(processoTipoDt.getProcessoTipo());
			processoDt.setId_ProcessoTipo(processoTipoDt.getId());
		}
		
		String valorNovo = processoDt.getPropriedades();
		
		// Atualiza dados do processo e gera log do arquivamento
		LogDt obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.AtivacaoProcessoExecucao), valorAtual, valorNovo);
		
		obLog.salvar(obLogDt, obFabricaConexao);
	}

	/**
	 * Método que arquiva um processo provisoriamente
	 * 
	 * @param processoDt
	 *            objeto processo a ser arquivado
	 * @param logDt
	 *            objeto com dados do log
	 * @param id_UsuarioServentia
	 *            usuario que esta arquivando o processo
	 * @param obFabricaConexao
	 *            conexao ativa
	 * 
	 * @author msapaula
	 */
	public void arquivarProcessoProvisoriamente(ProcessoDt processoDt, LogDt logDt, String id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Seta os valores em processo pois sera necessario na hora de
		// mostrar dados do processo na tela
		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE));
		processoDt.setDataArquivamento(Funcoes.DataHora(new Date()));

		String valorAtual = "[Id_Processo:" + processoDt.getId() + ";ProcessoStatus:Arquivado Provisoriamente]";
		// Atualiza dados do processo e gera log do arquivamento
		LogDt obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Arquivamento), valorAtual, "");
		obPersistencia.alterarProcessoArquivamento(processoDt);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO ARQUIVADO PROVISORIAMENTE
		movimentacaoNe.gerarMovimentacaoProcessoArquivadoProvisoriamente(processoDt.getId(), id_UsuarioServentia, "", logDt, obFabricaConexao);	}

	/**
	 * Método que verifica se um processo pode ser desarquivado Verifica se: -
	 * Processo está arquivado - Data de Arquivamento é diferente de null
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String podeDesarquivar(ProcessoDt processoDt) throws Exception {
		String stRetorno = "";
		// Se processo não está com dados completos, deve consultar
		if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) processoDt = this.consultarId(processoDt.getId());

		// Se processo está arquivado
		if (!processoDt.isArquivado() && !processoDt.isArquivadoProvisoriamente()) {
			stRetorno += " Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não está arquivado. Se está sendo gerada uma pendência de DESARQUIVAMENTO a partir de uma pré-análise, é preciso retirá-la. \n";
		}

		// Só pode permitir desarquivamento para ações do 2º grau e não para
		// recurso inominado
		if (processoDt.getId_Recurso().length() > 0) {
			stRetorno += " Não é possível Desarquivar um Recurso Inominado. \n";
		}
		return stRetorno;
	}

	/**
	 * Método que desarquiva um processo
	 * 
	 * @param processoDt
	 *            objeto processo a ser desarquivado
	 * @param logDt
	 *            objeto com dados do log
	 * @param id_UsuarioServentia
	 *            usuario que esta desarquivando o processo
	 * @param obFabricaConexao
	 *            conexao ativa
	 * 
	 * @author msapaula
	 */
	public void desarquivarProcesso(ProcessoDt processoDt, LogDt logDt, String id_UsuarioServentia, String complemento, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Seta os valores em processo pois sera necessario na hora de
		// mostrar dados do processo na tela
		if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA)))
			processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.CALCULO));
		else processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
		processoDt.setDataArquivamento("");

		// Atualiza dados do processo e gera log do arquivamento
		LogDt obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Desarquivamento), "", "");
		obPersistencia.alterarProcessoArquivamento(processoDt);
		
		obLog.salvar(obLogDt, obFabricaConexao);
		
		if(processoDt.isCriminal()){
			if (processoDt.getProcessoCriminalDt() == null || processoDt.getProcessoCriminalDt().getId().length()==0) {
				ProcessoCriminalNe processoCriminalNe = new ProcessoCriminalNe();
				processoDt.setProcessoCriminalDt(processoCriminalNe.consultarIdProcesso(processoDt.getId(), obFabricaConexao));
				processoDt.getProcessoCriminalDt().setId_Processo(processoDt.getId());
			}
			processoDt.getProcessoCriminalDt().setId_ProcessoArquivamentoTipo("null");
			processoDt.getProcessoCriminalDt().setId_UsuarioLog(logDt.getId_UsuarioLog());
			processoDt.getProcessoCriminalDt().setIpComputadorLog(logDt.getIpComputador());
			new ProcessoCriminalNe().salvar(processoDt.getProcessoCriminalDt(), obFabricaConexao);
		}
		

		// Gera movimentação PROCESSO DESARQUIVADO
		movimentacaoNe.gerarMovimentacaoProcessoDesarquivado(processoDt.getId(), id_UsuarioServentia, complemento, logDt, obFabricaConexao);
	}

	/**
	 * Atualiza os dados de um processo em virtude do retorno à serventia de
	 * origem de um recurso não originário e gera log da alteração
	 * 
	 * @param id_Processo
	 *            identificação do processo
	 * @param serventiaAnterior
	 *            serventia anterior do processo
	 * @param faseAnterior
	 *            fase anterior do processo
	 * @param classificadorAnterior
	 * @param conexao
	 *            conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoRetornoRecursoNaoOriginario(ProcessoDt processoDt, String serventiaAnterior, String faseAnterior, String classificadorAnterior, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		String valorAtual = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + serventiaAnterior + ";ProcessoFaseCodigo:" + faseAnterior + ";Id_Classificador:" + classificadorAnterior + "]";
		String valorNovo = "[Id_Processo:" + processoDt.getId() + ";Id_Serventia:" + processoDt.getId_Serventia() + ";ProcessoFaseCodigo:" + processoDt.getProcessoFaseCodigo() + ";Id_Classificador:" + processoDt.getId_Classificador() + "]";

		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.RetornoRecurso), valorAtual, valorNovo);
		obPersistencia.alterarProcessoRetornoRecursoNaoOriginario(processoDt.getId(), processoDt.getId_Serventia(), processoDt.getId_AreaDistribuicao(), processoDt.getProcessoFaseCodigo(), processoDt.getId_Classificador());

		obLog.salvar(obLogDt, obFabricaConexao);
	}

	/**
	 * Método que verifica se um processo pode ser suspenso Verifica se: - Não
	 * há autos conclusos - Não há audiências/sessões em aberto - Não há
	 * pendências ativas - Processo está ativo
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String podeSuspender(ProcessoDt processoDt, String grupoCodigo) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String stRetorno = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			

			// Verifica se há conclusões em aberto
			if ((Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZES_VARA) && 
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU) &&
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL) && 
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL) && 
					(Funcoes.StringToInt(grupoCodigo) != GrupoDt.JUIZES_TURMA_RECURSAL)	&& 
					pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)) stRetorno = "Há Autos Conclusos em Aberto. \n";

			// Verifica audiências pendentes
			if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
				if (processoDt.getId_Recurso().length() == 0) stRetorno += "Há Audiência(s) em Aberto. \n";
				else
					stRetorno += "Há Sessão em Aberto. \n";
			}

			if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) stRetorno += "Há pendência(s) em aberto. \n";

			// Se processo não está com dados completos, deve consultar
			if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) processoDt = consultarId(processoDt.getId());

			// Se processo está ativo
			if (Funcoes.StringToInt(processoDt.getProcessoStatusCodigo()) != ProcessoStatusDt.ATIVO) stRetorno += " Processo não está ativo. \n";

			if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode ser suspenso. " + stRetorno;

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Método que verifica se um processo pode ter a suspensão finalizada.
	 * Verifica se: - Processo está suspenso
	 * 
	 * @param processoDt
	 *            , dt de processo
	 * @author msapaula
	 */
	public String podeFinalizarSuspensao(ProcessoDt processoDt) throws Exception {
		String stRetorno = "";		
		// Se processo não está com dados completos, deve consultar
		if (processoDt.getProcessoStatusCodigo() == null || processoDt.getProcessoStatusCodigo().length() == 0) processoDt = consultarId(processoDt.getId());

		// Se processo não está suspenso
		if (Funcoes.StringToInt(processoDt.getProcessoStatusCodigo()) != ProcessoStatusDt.SUSPENSO) {
			stRetorno += " Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não está suspenso. \n";
		}
		return stRetorno;
	}

//	/**
//	 * Método que verifica se um processo pode ser concluso. Verifica se
//	 * processo já não está concluso ao juiz
//	 * 
//	 * @param processoDt
//	 *            dt de processo
//	 * @author msapaula
//	 */
//	public String podeGerarConclusao(ProcessoDt processoDt, String Id_ServentiaCargo) throws Exception {
//		PendenciaNe pendenciaNe = new PendenciaNe();
//		String stRetorno = "";
//		FabricaConexao obFabricaConexao = null;
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);			
//
//			// Verifica se há conclusões em aberto
//			if (pendenciaNe.verificaConclusoesAbertasProcessoServentiaCargo(processoDt.getId(), Id_ServentiaCargo, obFabricaConexao)) {
//				stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " já está Concluso. ";
//			}
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//		return stRetorno;
//	}

	/**
	 * Método que suspende um processo
	 * 
	 * @param id_Processo
	 *            , processo a ser suspenso
	 * @param logDt
	 *            objeto com dados do log
	 * @param id_UsuarioServentia
	 *            usuario que esta arquivando o processo
	 * @param obFabricaConexao
	 *            conexao ativa
	 * 
	 * @author msapaula
	 */
	public void suspenderProcesso(String id_Processo, String prazo, LogDt logDt, String id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Muda status do processo para suspenso
		int novoStatus = ProcessoStatusDt.SUSPENSO;

		String valorAtual = "[Id_Processo:" + id_Processo + "]";

		// Atualiza dados do processo e gera log da suspensão
		LogDt obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.SuspensaoProcesso), valorAtual, "");
		obPersistencia.alterarProcessoSuspensao(id_Processo, novoStatus);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO SUSPENSO
		movimentacaoNe.gerarMovimentacaoProcessoSuspenso(id_Processo, id_UsuarioServentia, " (Por " + prazo + " dias)", logDt, obFabricaConexao);
	}
	

	/**
	 * Método que suspende um processo
	 * 
	 * @param id_Processo
	 *            , processo a ser suspenso
	 * @param logDt
	 *            objeto com dados do log
	 * @param id_UsuarioServentia
	 *            usuario que esta arquivando o processo
	 * @param obFabricaConexao
	 *            conexao ativa
	 * 
	 * @author msapaula
	 */
	public void suspenderProcessoAlterandoFaseProcessual(String id_Processo, String prazo, LogDt logDt, String id_UsuarioServentia, String codFaseProcesso, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Muda status do processo para suspenso
		int novoStatus = ProcessoStatusDt.SUSPENSO;

		if (codFaseProcesso == null || codFaseProcesso.equalsIgnoreCase("-1")){
			codFaseProcesso = "";
		}
		
		String valorAtual = "[Id_Processo:" + id_Processo + ", Cod_FaseProcesso:" + codFaseProcesso + "]";

		// Atualiza dados do processo e gera log da suspensão
		LogDt obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.SuspensaoProcesso), valorAtual, "");
		obPersistencia.suspenderProcessoAlterandoFaseProcessual(id_Processo, novoStatus, codFaseProcesso);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO SUSPENSO
		movimentacaoNe.gerarMovimentacaoProcessoSuspenso(id_Processo, id_UsuarioServentia, " (Por " + prazo + " dias)", logDt, obFabricaConexao);
	}

	/**
	 * Método que finaliza a suspensão de um processo através da movimentação
	 * processual, devendo também fechar a pendência do tipo
	 * "Suspensao Processo"
	 * 
	 * @param id_Processo
	 *            , objeto processo a ser ativado
	 * @param logDt
	 *            , objeto com dados do log
	 * @param usuarioDt
	 *            , usuario que esta ativando o processo
	 * @param obFabricaConexao
	 *            , conexão ativa
	 * 
	 * @author msapaula
	 */
	public void finalizaSuspensaoProcessoMovimentacao(String id_Processo, LogDt logDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Muda status do processo para Ativo
		int novoStatus = ProcessoStatusDt.ATIVO;

		String valorAtual = "[Id_Processo:" + id_Processo + "]";

		// Atualiza dados do processo e gera log da suspensão
		LogDt obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TerminoSuspensaoProcesso), valorAtual, "");
		obPersistencia.alterarProcessoSuspensao(id_Processo, novoStatus);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação TÉRMINO DA SUSPENSÃO DE PROCESSO
		movimentacaoNe.gerarMovimentacaoTerminoSuspensaoProcesso(id_Processo, usuarioDt.getId_UsuarioServentia(), "", logDt, obFabricaConexao);

		// Finaliza pendência Suspensão Processo com status "Cumprida"
		PendenciaDt pendenciaDt = new PendenciaNe().consultarPendenciaSuspensaoProcesso(id_Processo, obFabricaConexao);
		if (pendenciaDt != null && pendenciaDt.getId().length() > 0) {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			new PendenciaNe().fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
		}
		
		// Preenche o campo data_sobrestado_final dos temas associados ao processo com o data de realização da movimentação
		new ProcessoTemaNe().finalizarProcessoTemas(obFabricaConexao, id_Processo, false);
	}
	
	/**
	 * 
	 * @param id_Processo
	 * @param logDt
	 * @param usuarioDt
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void finalizaSuspensaoProcessoPorAcordoMovimentacao(String id_Processo, LogDt logDt, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Muda status do processo para Ativo
		int novoStatus = ProcessoStatusDt.ATIVO;

		String valorAtual = "[Id_Processo:" + id_Processo + "]";

		// Atualiza dados do processo e gera log da suspensão
		LogDt obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TerminoSuspensaoProcesso), valorAtual, "");
		obPersistencia.alterarProcessoSuspensao(id_Processo, novoStatus);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação TÉRMINO DA SUSPENSÃO DE PROCESSO
		String complemento = "Por Decisão de homologação, extinção, desistência ou retratação";
		movimentacaoNe.gerarMovimentacaoTerminoSuspensaoProcesso(id_Processo, usuarioDt.getId_UsuarioServentia(), complemento, logDt, obFabricaConexao);

		// Finaliza pendência Suspensão Processo com status "Cumprida"
		PendenciaDt pendenciaDt = new PendenciaNe().consultarPendenciaSuspensaoProcesso(id_Processo, obFabricaConexao);
		if (pendenciaDt != null && pendenciaDt.getId().length() > 0) {
			pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			new PendenciaNe().fecharPendencia(pendenciaDt, usuarioDt, obFabricaConexao);
		}
		
		// Preenche o campo data_sobrestado_final dos temas associados ao processo com o data de realização da movimentação
		new ProcessoTemaNe().finalizarProcessoTemas(obFabricaConexao, id_Processo, true);
	}

	/**
	 * Método que finaliza a suspensão de um processo. Somente irá retornar o
	 * status para Ativo e gerar movimentação de Término de Suspensão.
	 * 
	 * @param id_Processo
	 *            , objeto processo a ser ativado
	 * @param logDt
	 *            , objeto com dados do log
	 * @param id_UsuarioServentia
	 *            , usuario que esta ativando o processo
	 * 
	 * @author msapaula
	 */
	public void finalizaSuspensaoProcesso(String id_Processo, LogDt logDt, String id_UsuarioServentia) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Muda status do processo para Ativo
			int novoStatus = ProcessoStatusDt.ATIVO;

			String valorAtual = "[Id_Processo:" + id_Processo + "]";

			// Atualiza dados do processo e gera log da suspensão
			LogDt obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TerminoSuspensaoProcesso), valorAtual, "");
			obPersistencia.alterarProcessoSuspensao(id_Processo, novoStatus);
			obLog.salvar(obLogDt, obFabricaConexao);

			// Gera movimentação TÉRMINO DA SUSPENSÃO DE PROCESSO
			movimentacaoNe.gerarMovimentacaoTerminoSuspensaoProcesso(id_Processo, id_UsuarioServentia, "", logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Consulta os processos que devem ter a Suspensão finalizada
	 * automaticamente. Esse método é chamado pela Execução automática, e deve
	 * mudar o status dos processos para "Ativo" e gerar movimentação
	 * "Término da Suspensão do Processo".
	 * 
	 * @author msapaula
	 * @throws Exception
	 */
	public void finalizarSuspensaoProcessosAutomatica() throws Exception {
		LogDt logDt = new LogDt(UsuarioDt.SistemaProjudi, "Servidor");

		// Consulta os processos que tem q ter a suspensão finalizada no dia
		// atual
		List processos = this.consultarProcessosFinalizarSuspensao();
		if (processos != null) {
			for (int i = 0; i < processos.size(); i++) {
				String id_Processo = (String) processos.get(i);
				// Para cada processo, abre uma transação e alterar dados
				this.finalizaSuspensaoProcesso(id_Processo, logDt, UsuarioServentiaDt.SistemaProjudi);
			}
		}
	}

	/**
	 * Consulta processos que devem ter a Suspensão Finalizada.
	 * 
	 * @author msapaula
	 * @throws Exception
	 */
	private List consultarProcessosFinalizarSuspensao() throws Exception {
		List processos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			processos = obPersistencia.consultarProcessosFinalizarSuspensao();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return processos;
	}

	/**
	 * Realiza chamada ao objeto que salvará os dados
	 * 
	 * @param processoPartedt
	 * @throws Exception
	 */
	public void salvarProcessoParte(ProcessoParteDt processoPartedt) throws Exception {
		ProcessoParteNe parteNe = new ProcessoParteNe();
		parteNe.salvar(processoPartedt);
	}

	/**
	 * Realiza tratamentos referente a prevenção de processos. Consulta os
	 * possíveis preventos para um processo passado, e caso encontre algum será
	 * gerada uma pendência do tipo Verificar Prevenção, onde o arquivo dessa
	 * pendência tem o detalhamento dos preventos.
	 * 
	 * @param processo
	 *            , obj para o qual serão consultados os possíveis preventos
	 * @param fabricaConexao
	 *            , conexão ativa
	 * 
	 * @return id_Serventia para onde o processo deve ser distribuído
	 * @author msapaula
	 */
	protected String[] verificaConexaoProcessoOriginario(ProcessoCadastroDt processoDt, FabricaConexao fabricaConexao) throws Exception {
		List lisConexo = null;
		String[] id_ServentiaPrevencao = {null,null,"",null};
		lisConexo = this.consultarConexaoProcessoOriginario(processoDt, processoDt.getId_Comarca(), processoDt.getId_ServentiaSubTipo(), fabricaConexao);

		if (lisConexo != null && lisConexo.size() > 0) {
			processoDt.setListaPreventos(lisConexo);
			ProcessoDt tempProcessoDt = (ProcessoDt) lisConexo.get(0);

			// Se encontrou algum prevento, resgata a primeira serventia
			// encontrada para que o processo vá para a mesma
			id_ServentiaPrevencao[0] = tempProcessoDt.getId_Serventia();
			id_ServentiaPrevencao[1] = tempProcessoDt.getDataArquivamento();
			id_ServentiaPrevencao[2] = tempProcessoDt.getProcessoNumero();
			id_ServentiaPrevencao[3] = tempProcessoDt.getId();

		}
		return id_ServentiaPrevencao;
	}

	/**
	 * Consulta dados do processo baseado em uma conexão já existente
	 * 
	 * @param id_processo
	 * @param conexao
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarId(String id_processo, FabricaConexao conexao) throws Exception {

		ProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}else{
				obFabricaConexao = conexao;
			}
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_processo);
			obDados.copiar(dtRetorno);
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta os possíveis preventos para um processo passado.
	 * 
	 * @param processoDt
	 *            , obj processo para o qual serão consultados os possíveis
	 *            preventos
	 * @param conexao
	 *            , conexão ativa ou não
	 * @author msapaula
	 * @author jrcorrea
	 */
	public List consultarConexaoProcessoOriginario(ProcessoDt processoDt, String id_Comarca, String id_ServentiaSubTipo) throws Exception {
		List preventos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			preventos = obPersistencia.consultarConexaoProcessoOriginario(processoDt, id_Comarca, id_ServentiaSubTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return preventos;
	}
	
	/**
	 * Consulta os possíveis preventos para um processo passado.
	 * 
	 * @param processoDt
	 *            , obj processo para o qual serão consultados os possíveis
	 *            preventos
	 * @param conexao
	 *            , conexão ativa ou não
	 * @author lsbernardes
	 */
	public List consultarConexaoProcessoOriginario(ProcessoDt processoDt, String id_Comarca, String id_ServentiaSubTipo, FabricaConexao conexao) throws Exception {
		List preventos = null;
		ProcessoPs obPersistencia = new ProcessoPs(conexao.getConexao());
		preventos = obPersistencia.consultarConexaoProcessoOriginario(processoDt, id_Comarca, id_ServentiaSubTipo); 
		
		return preventos;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List<String[]> consultarAudienciasPendentes(String id_Processo, boolean ehConsultaPublica) throws Exception {
		List<String[]> audienciaPendente = null;
		AudienciaProcessoNe neObjeto = new AudienciaProcessoNe();
		audienciaPendente = neObjeto.consultarAudienciasPendentesProcesso(id_Processo, ehConsultaPublica);
		neObjeto = null;
		return audienciaPendente;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List[] consultarPendenciasProcesso(String id_Processo, boolean ehConsultaPublica) throws Exception {
		List[] listaPendencias = null;
		PendenciaNe neObjeto = new PendenciaNe();
		listaPendencias = neObjeto.consultarPendenciasProcesso(id_Processo, ehConsultaPublica, null);
		neObjeto = null;
		return listaPendencias;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List[] consultarPendenciasProcesso(String id_Processo, boolean ehConsultaPublica, UsuarioDt usuarioDt) throws Exception {
		List[] listaPendencias = null;
		PendenciaNe neObjeto = new PendenciaNe();
		listaPendencias = neObjeto.consultarPendenciasProcesso(id_Processo, ehConsultaPublica, usuarioDt);
		neObjeto = null;
		return listaPendencias;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public ArquivoDt consultarArquivoPendenciaId(String idArquivo) throws Exception {
		ArquivoNe arquivoNe = new ArquivoNe();
		ArquivoDt arquivoDt = arquivoNe.consultarId(idArquivo);
		arquivoNe = null;
		return arquivoDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List[] consultarPendenciasProcessoHash(String id_Processo, UsuarioNe UsuarioSessao) throws Exception {
		List[] listaPendencias = null;
		PendenciaNe neObjeto = new PendenciaNe();
		listaPendencias = neObjeto.consultarPendenciasProcessoHash(id_Processo, UsuarioSessao);
		neObjeto = null;
		return listaPendencias;
	}

	public void descartarPendencias(String[] ids_Pendencias, UsuarioNe UsuarioSessao) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		neObjeto.descartarPendencias(ids_Pendencias, UsuarioSessao);
		neObjeto = null;
	}

	public void finalizarPendenciaIntimacaoAguardandoParecer(String id_Pendencia) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		neObjeto.finalizarPendenciaIntimacaoAguardandoParecer(id_Pendencia);
		neObjeto = null;
	}

	public void marcarPendenciaIntimacaoAguardandoParecer(String id_Pendencia, UsuarioDt usuarioDt, boolean finalizada) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		if (finalizada)
			pendenciaDt = neObjeto.consultarFinalizadaId(id_Pendencia);
		else
			pendenciaDt = neObjeto.consultarId(id_Pendencia);
		
		neObjeto.gerarAguardandoParecer(pendenciaDt, usuarioDt);
		pendenciaDt = null;
		neObjeto = null;
	}
	
	public PendenciaDt gerarPreAnalisePrecatoria(String id_Pendencia, UsuarioDt usuarioDt) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		pendenciaDt = neObjeto.gerarPreAnalisePrecatoria(neObjeto.consultarId(id_Pendencia), usuarioDt);
		neObjeto = null;
		return pendenciaDt;
	}

	public void gerarPendeciaLiberarAcessoProcesso(UsuarioDt usuarioDt, String id_Processo, boolean segredoJustica) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		neObjeto.gerarPendenciaLiberaAcesso(usuarioDt, id_Processo, segredoJustica);
		neObjeto = null;
	}
	
	public PendenciaDt gerarPendenciaAguardandoParecer(UsuarioDt usuarioDt, String id_Processo) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaDt pendenciaDt = neObjeto.gerarAguardandoParecer(usuarioDt, id_Processo);
		neObjeto = null;
		return pendenciaDt;
	}
	
	public String verificarElaboracaoVoto (UsuarioDt usuarioDt, String id_Processo) throws Exception {
		String msg = "";
		  
		ServentiaCargoDt serventiaCargoDt = new ProcessoResponsavelNe().consultarRelator2GrauElaboracaoVoto(id_Processo);
		if (serventiaCargoDt != null && serventiaCargoDt.getId() != null && !serventiaCargoDt.getId().equals(usuarioDt.getId_ServentiaCargo()))
			msg  += "Usuário não é Relator no Processo!";
		
		if (new PendenciaNe().verificarExistenciaElaboracaoVoto(id_Processo))
			msg  += "\n Processo já possui uma pendência de Elaboração de Voto!";
		
		AudienciaProcessoDt audienciaProcessoDt =  new PendenciaNe().consultarAudienciaProcessoPendente(id_Processo, usuarioDt);
		if (audienciaProcessoDt == null ||  audienciaProcessoDt.getId() == null || audienciaProcessoDt.getId().length() == 0 ){
			msg += "\n Processo  não consta em pauta!";
		}
		
		return msg;
	}
	
	
	public String verificarEditarElaboracaoVoto (UsuarioDt usuarioDt, String id_Processo) throws Exception {
		String msg = "";
		  
		ServentiaCargoDt serventiaCargoDt = new ProcessoResponsavelNe().consultarRelator2GrauElaboracaoVoto(id_Processo);
		if (serventiaCargoDt != null && serventiaCargoDt.getId() != null && !serventiaCargoDt.getId().equals(usuarioDt.getId_ServentiaCargo()))
			msg  += "Usuário não é Relator no Processo!";
		
		AudienciaProcessoDt audienciaProcessoDt =  new PendenciaNe().consultarAudienciaProcessoPendente(id_Processo, usuarioDt);
		if (audienciaProcessoDt == null ||  audienciaProcessoDt.getId() == null || audienciaProcessoDt.getId().length() == 0 ){
			msg += "\n Processo  não consta em pauta!";
		}
		
		return msg;
	}
	
	public String verificarElaboracaoVotoPreAnalise (AudienciaMovimentacaoDt audienciaMovimentacaoDt) throws Exception {
		String msg = "";
		
		if(audienciaMovimentacaoDt.getId_ArquivoTipo() == null || audienciaMovimentacaoDt.getId_ArquivoTipo().equals("")){
			msg  += "\n Tipo de arquivo não foi selecionado!";		
		}  
		
		if(audienciaMovimentacaoDt.getTextoEditor() == null || audienciaMovimentacaoDt.getTextoEditor().equals("") ){
			msg  += "\n Texto da Pré-Análise não foi escrito!";
		}else{
			String ementa = "class=\"ementa\"";
			if( !audienciaMovimentacaoDt.getTextoEditor().contains(ementa)){
				msg += "Para redigir pré-análise é necessário definir a ementa \n";
			}
		}
		
		return msg;
	}
	
		
	public PendenciaDt gerarPendenciaElaboracaoVoto(UsuarioDt usuarioDt, String id_Processo) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaDt pendenciaDt = neObjeto.gerarElaboracaoVoto(usuarioDt, id_Processo);
		neObjeto = null;
		return pendenciaDt;
	}
	

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarConclusoesPendentesProcessoPublico(String id_Processo, boolean ehConsultaPublica) throws Exception {
		String[] conclusao = null;
		List retornoConclusoesPendentes = new ArrayList();
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		String nomeResponsavel = "";
		List conclusoesPendentes = neObjeto.consultarConclusoesPendentesProcessoPublico(id_Processo, ehConsultaPublica);
		if(conclusoesPendentes != null && conclusoesPendentes.size() > 0){
			Iterator iteratorConclusoes = conclusoesPendentes.iterator();
			while (iteratorConclusoes.hasNext()) {
				conclusao = (String[]) iteratorConclusoes.next();
				if (conclusao != null) {
					List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(conclusao[0], null);		    	
			    	if (responsaveis != null){
			    		Iterator iteratorResponsavel = responsaveis.iterator();
			    		while (iteratorResponsavel.hasNext()) {
			    			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
			    			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DESEMBARGADOR)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.JUIZ_1_GRAU)))){
			    				nomeResponsavel = dados.getServentiaCargo() + "-" + dados.getNomeUsuarioServentiaCargo();
			    			}
			    		}
			    	}
			    	retornoConclusoesPendentes.add(new String[] { conclusao[0], conclusao[1], conclusao[2], conclusao[3], conclusao[4] , nomeResponsavel});				    	
				}    	
			}			   					
		}
		neObjeto = null;
		pendenciaResponsavelNe = null;
		return retornoConclusoesPendentes;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarConclusoesPendentesProcessoHash(String id_Processo, UsuarioNe usuarioSessao) throws Exception {
		List retornoConclusoesPendentes = new ArrayList();
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		String nomeResponsavel = "";
		String[] conclusaoPendente = null;
		List conclusoes = neObjeto.consultarConclusoesPendentesProcessoHash(id_Processo, usuarioSessao);
		if(conclusoes != null){
			Iterator iteratorConclusao = conclusoes.iterator();
    		while (iteratorConclusao.hasNext()) {
    			conclusaoPendente = (String[])iteratorConclusao.next();
    			String idPendencia = conclusaoPendente[0].split("@#!@")[0];
    			List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(idPendencia, null);		    	
    			if (responsaveis != null){		    		
		    		Iterator iteratorResponsavel = responsaveis.iterator();
		    		while (iteratorResponsavel.hasNext()) {
		    			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
		    			if(dados.getId_ServentiaCargo() != null && !dados.getId_ServentiaCargo().equalsIgnoreCase("")) {
		    				ServentiaCargoDt servCargoDt = new ServentiaCargoNe().consultarId(dados.getId_ServentiaCargo());
		    				if (dados != null && servCargoDt != null && servCargoDt.isMagistrado()){
		    					nomeResponsavel = servCargoDt.getServentiaCargo() + " - " + servCargoDt.getNomeUsuario();
		    					break;
		    				} else {
		    					nomeResponsavel = "Não disponível";
		    				}
		    			}
		    		}
		    	}
				// jvosantos - 16/07/2019 17:04 - Adicionar campo para passar ID_Pend e ID_Proc para a tela da capa do processo
		    	retornoConclusoesPendentes.add(new String[] { conclusaoPendente[0], conclusaoPendente[1], conclusaoPendente[2], conclusaoPendente[3], conclusaoPendente[4] , nomeResponsavel, conclusaoPendente[5], conclusaoPendente[6], idPendencia + "," + id_Processo});
    		}				   					
		}
		neObjeto = null;
		pendenciaResponsavelNe = null;
		return retornoConclusoesPendentes;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoEstado(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		EstadoNe neObjeto = new EstadoNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public ServentiaDt consultarIdServentia(String id_Serventia) throws Exception {
		ServentiaDt serventiaDt = null;
		ServentiaNe neObjeto = new ServentiaNe();
		serventiaDt = neObjeto.consultarId(id_Serventia);
		neObjeto = null;
		return serventiaDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarOutrosTiposPartes(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ProcessoParteTipoNe neObjeto = new ProcessoParteTipoNe();
		tempList = neObjeto.consultarOutrosTiposPartes(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoBairro(String tempNomeBusca, String cidade, String uf, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		BairroNe neObjeto = new BairroNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, cidade, uf, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;

	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoCidade(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		CidadeNe neObjeto = new CidadeNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoEstadoCivil(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoProfissao(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ProfissaoNe neObjeto = new ProfissaoNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoOrgaoExpedidor(String tempNomeBusca, String sigla, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		RgOrgaoExpedidorNe neObjeto = new RgOrgaoExpedidorNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, sigla, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoClassificador(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List tempList = null;
		ClassificadorNe neObjeto = new ClassificadorNe();
		tempList = neObjeto.consultarClassificadorServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoComarca(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ComarcaNe neObjeto = new ComarcaNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public ProcessoParteDt consultarParte(String cpfCnpj, String rg, String ctps, String tituloEleitor, String pis) throws Exception {
		ProcessoParteDt processoParteDt = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		processoParteDt = neObjeto.consultarParte(cpfCnpj, rg, ctps, tituloEleitor, pis);
		neObjeto = null;
		return processoParteDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public String verificaCpfCnpjParte(String cpfCnpj) throws Exception {
		String stMensagem = "";
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		stMensagem = neObjeto.VerificarCpfCnpjParte(cpfCnpj);
		neObjeto = null;
		return stMensagem;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public String VerificarParteProcesso(ProcessoParteDt processoPartedt) throws Exception {
		return new ProcessoParteNe().Verificar(processoPartedt);
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
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
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarModelo(UsuarioDt usuarioDt, String id_ArquivoTipo, String descricao, String posicao) throws Exception {
		List tempList = null;
		ModeloNe neObjeto = new ModeloNe();
		tempList = neObjeto.consultarModelos(descricao, posicao, id_ArquivoTipo, usuarioDt);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta modelo para código de
	 * acesso
	 */
	public ModeloDt consultarModeloCodigoAcesso(String id_ArquivoTipo) throws Exception {
		ModeloDt modeloDt = null;
		ModeloNe modeloNe = new ModeloNe();
		modeloDt = modeloNe.consultarModeloCodigoAcesso(id_ArquivoTipo);
		modeloNe = null;
		return modeloDt;
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
	public ModeloDt consultarModeloId(String id_Modelo, UsuarioNe UsuarioSessao, ProcessoDt processo, String id_Parte) throws Exception {
		ModeloDt modeloDt = null;
		ModeloNe modeloNe = new ModeloNe();
		modeloDt = modeloNe.consultarId(id_Modelo);
		modeloDt.setTexto(modeloNe.montaConteudoAcessoCodigo(id_Modelo, UsuarioSessao, processo, id_Parte));
		modeloNe = null;
		return modeloDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarPromoventes(String id_Processo) throws Exception {
		List lista = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		lista = neObjeto.consultarPromoventes(id_Processo);
		neObjeto = null;
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarPromoventesAtivos(String id_Processo) throws Exception {
		List lista = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		lista = neObjeto.consultarPromoventesAtivos(id_Processo);
		neObjeto = null;
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarPromovidos(String id_Processo) throws Exception {
		List lista = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		lista = neObjeto.consultarPromovidos(id_Processo);
		neObjeto = null;
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarOutrasPartes(String id_Processo) throws Exception {
		List lista = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		lista = neObjeto.consultarOutrasPartes(id_Processo);
		neObjeto = null;
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarPromovidosAtivos(String id_Processo) throws Exception {
		List lista = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		lista = neObjeto.consultarPromovidosAtivos(id_Processo);
		neObjeto = null;
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarOutrasPartesAtivas(String id_Processo) throws Exception {
		List lista = null;
		ProcessoParteNe neObjeto = new ProcessoParteNe();
		lista = neObjeto.consultarOutrasPartesAtivas(id_Processo);
		neObjeto = null;
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a restauração de uma parte do
	 * processo
	 */
	public void restauraParteProcesso(ProcessoParteDt processoPartedt) throws Exception {
		ProcessoParteNe parteNe = new ProcessoParteNe();
		parteNe.restauraParteProcesso(processoPartedt);
		parteNe = null;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a desabilitação de uma parte do
	 * processo
	 */
	public void desabilitaParteProcesso(ProcessoParteDt processoPartedt) throws Exception {
		ProcessoParteNe parteNe = new ProcessoParteNe();
		parteNe.desabilitaParteProcesso(processoPartedt);
		parteNe = null;

	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta de uma parte
	 */
	public ProcessoParteDt consultarProcessoParteId(String id_ProcessoParte) throws Exception {
		ProcessoParteDt parteDt = null;
		ProcessoParteNe parteNe = new ProcessoParteNe();
		parteDt = parteNe.consultarId(id_ProcessoParte);
		parteNe = null;
		return parteDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta de um recurso
	 */
	public RecursoDt consultarDadosRecurso(String id_Recurso, boolean ehConsultaPublica) throws Exception {
		RecursoDt recursoDt = null;
		RecursoNe recursoNe = new RecursoNe();
		recursoDt = recursoNe.consultarDadosRecurso(id_Recurso, ehConsultaPublica);
		recursoNe = null;
		return recursoDt;
	}
	
	public RecursoDt consultarDadosRecurso(String id_Recurso, FabricaConexao obFabricaConexao) throws Exception {
		RecursoDt recursoDt = null;
		RecursoNe recursoNe = new RecursoNe();
		recursoDt = recursoNe.consultarDadosRecurso(id_Recurso, obFabricaConexao);
		recursoNe = null;
		return recursoDt;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta de um recurso
	 */
	public RecursoDt consultarDadosRecurso(String id_Recurso, ProcessoDt processoDt, UsuarioDt usuarioDt, boolean ehConsultaPublica) throws Exception {
		RecursoDt recursoDt = null;
		RecursoNe recursoNe = new RecursoNe();
		recursoDt = recursoNe.consultarDadosRecurso(id_Recurso, ehConsultaPublica);
		recursoNe = null;
		
		if ( processoDt.isSegredoJustica() || processoDt.isSigiloso() || processoDt.hasVitima()) {
			if(!this.podeAcessarProcesso(usuarioDt, processoDt, null)) {
				for (int j = 0; j < recursoDt.getPartesRecorrentesRecorridas().size(); j++) {
					RecursoParteDt parteDt = (RecursoParteDt) recursoDt.getPartesRecorrentesRecorridas().get(j);
					if (processoDt.isSegredoJustica() || processoDt.isSigiloso() || parteDt.getProcessoParteDt().isVitima()) {
						parteDt.getProcessoParteDt().setNome(Funcoes.iniciaisNome(parteDt.getProcessoParteDt().getNome()));
					}					
				}
			}
		}
		
		return recursoDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará o download de um arquivo
	 */
	public void baixarArquivo(String id_Arquivo, HttpServletResponse response, LogDt logDt) throws Exception {
		ArquivoNe arquivoNe = new ArquivoNe();
		arquivoNe.baixarArquivo(id_Arquivo, response, logDt, false);
		arquivoNe = null;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta das movimentações do
	 * processo
	 */
	public List consultarMovimentacoesProcesso(UsuarioDt usuarioDt, String id_Processo, boolean acessoOutraServentiaOuCodigoDeAcesso, int inNivelAcessoUsuario) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		List lista = null;
		ProcessoDt processoDt;
		processoDt = this.consultarId(id_Processo);
		lista = movimentacaoNe.consultarMovimentacoesProcesso(usuarioDt, processoDt, acessoOutraServentiaOuCodigoDeAcesso, inNivelAcessoUsuario);
		movimentacaoNe = null;
		return lista;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta das movimentações do
	 * processo
	 */
	public List<MovimentacaoDt> consultarMovimentacoesProcesso(String id_Processo) throws Exception {

		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		List<MovimentacaoDt> lista = null;
		lista = movimentacaoNe.consultarMovimentacoesProcesso(id_Processo);
		movimentacaoNe = null;
		
		return lista;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarServentia(String descricao, String posicao, UsuarioDt usuarioDt) throws Exception {
		List tempList = null;
		ServentiaNe Serventiane = new ServentiaNe();
		tempList = Serventiane.consultarServentia(descricao, posicao, usuarioDt);
		QuantidadePaginas = Serventiane.getQuantidadePaginas();
		Serventiane = null;
		return tempList;
	}
	
	public String consultarServentiaJSON(String tempNomeBusca, String posicaoPaginaAtual, UsuarioDt usuarioDt) throws Exception{
		String stTemp ="";
		ServentiaNe neObjeto = new ServentiaNe();
    		stTemp = neObjeto.consultarServentiaJSON(tempNomeBusca, posicaoPaginaAtual, usuarioDt);
		return stTemp;
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	/**
	 * Realiza chamada ao objeto que efetuará o download do arquivo
	 */
	public boolean baixarArquivoMovimentacao(String id_MovimentacaoArquivo, ProcessoDt processoDt, UsuarioDt usuarioDt, boolean recibo, HttpServletResponse response, LogDt logDt, boolean ehConsultaPublica) throws Exception, MensagemException {		
		boolean boRetorno = false;
		boRetorno = (new MovimentacaoArquivoNe()).baixarArquivoMovimentacao(id_MovimentacaoArquivo, processoDt, usuarioDt, recibo, response, logDt, ehConsultaPublica);
		return boRetorno;
	}

	public String consultarIdArquivo(String id_MovimentacaoArquivo) throws Exception{
		String idArquivo = "";
		idArquivo = new MovimentacaoArquivoNe().consultarIdArquivo(id_MovimentacaoArquivo);
		return idArquivo;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoAssunto(String tempNomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		List areasDistribuicoes = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(id_Serventia);
		tempList = neObjeto.consultarAssuntosAreasDistribuicoes(areasDistribuicoes, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String id_AreaDistribuicao, UsuarioDt usuarioDt, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ServentiaSubtipoProcessoTipoNe neObjeto = new ServentiaSubtipoProcessoTipoNe();
		if (usuarioDt != null && usuarioDt.getGrupoTipoCodigo() != null 
				&& usuarioDt.getGrupoTipoCodigo().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ADVOGADO)))
			tempList = neObjeto.consultarProcessoTiposPublicos(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		else
			tempList = neObjeto.consultarProcessoTipos(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Método que consulta os Tipos de Processo ligados à Serventia através de
	 * suas Áreas de Distribuição.
	 * 
	 * @param descricao
	 *            - descrição do tipo de processo
	 * @param idServentia
	 *            - ID da Serventia
	 * @param posicaoPaginaAtual
	 *            - posição da paginação
	 * @return lista de tipos de processo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessoTipoServentia(String descricao, String idServentia, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		List listaAreaDistribuicao = this.consultarAreasDistribuicaoServentia(idServentia);
		if (!listaAreaDistribuicao.isEmpty()) {
			List listaIdsAreaDistribuicao = new ArrayList();
			for (int i = 0; i < listaAreaDistribuicao.size(); i++) {
				AreaDistribuicaoDt areaTemp = (AreaDistribuicaoDt) listaAreaDistribuicao.get(i);
				listaIdsAreaDistribuicao.add(areaTemp.getId());
			}
			tempList = processoTipoNe.consultarProcessoTipoServentia(descricao, listaIdsAreaDistribuicao, posicaoPaginaAtual);
			QuantidadePaginas = processoTipoNe.getQuantidadePaginas();
		}
		processoTipoNe = null;
		return tempList;
	}

	
	
	/**
	 * Consulta o número do proecesso baseado no id do mesmo.
	 */
	public String getNumeroDoProcesso(String id_Processo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			return obPersistencia.getNumeroDoProcesso( id_Processo );
		} finally {
    		obFabricaConexao.fecharConexao();
    	}
	}	
	
	public String getIdDoProcesso(String numeroCompletoDoProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			return obPersistencia.getIdDoProcesso(numeroCompletoDoProcesso);
		} finally {
    		obFabricaConexao.fecharConexao();
    	}
	}		
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List getAssuntosProcesso(String id_Processo) throws Exception {
		List tempList = null;
		ProcessoAssuntoNe neObjeto = new ProcessoAssuntoNe();
		tempList = neObjeto.consultarAssuntosProcesso(id_Processo);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	public ServentiaCargoDt getPresidenteTurmaRecursal(String id_Serventia) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ServentiaCargoNe neObjeto = new ServentiaCargoNe();
		dtRetorno = neObjeto.getPresidenteTurmaRecursal(id_Serventia);
		neObjeto = null;
		return dtRetorno;
	}

	public ServentiaCargoDt getRelatorResponsavelProcesso(String id_Processo, String id_Serventia) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		dtRetorno = neObjeto.getRelatorResponsavelProcesso(id_Processo, id_Serventia, null);
		neObjeto = null;
		return dtRetorno;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarAreasDistribuicao(String tempNomeBusca, String id_Comarca, String areaCodigo, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		tempList = neObjeto.consultarAreasDistribuicao(tempNomeBusca, id_Comarca, areaCodigo, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Método que consulta as Áreas de Distribuição relacionadas à Serventia.
	 * 
	 * @param idServentia
	 *            - ID da Serventia
	 * @return lista de Áreas de Distribuição
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAreasDistribuicaoServentia(String idServentia) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		tempList = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(idServentia);
		areaDistribuicaoNe = null;
		return tempList;
	}
	
	public AreaDistribuicaoDt consultarPrimeiraAreaDistribuicaoServentia(String idServentia) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		AreaDistribuicaoDt areaDistribuicaoDt = null;
		tempList = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(idServentia);
		areaDistribuicaoDt = (AreaDistribuicaoDt) tempList.get(0);
		areaDistribuicaoNe = null;
		return areaDistribuicaoDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarAreasDistribuicaoSegundoGrau(String tempNomeBusca, String id_Comarca, String areaCodigo, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		tempList = neObjeto.consultarAreasDistribuicaoSegundoGrau(tempNomeBusca, id_Comarca, areaCodigo, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarResponsaveisProcesso(String id_Processo, String id_serventiaProcesso, String grupoCodigo) throws Exception {
		List tempList = null;
		List listRetorno = new ArrayList();
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		tempList = neObjeto.consultarResponsaveisProcesso(id_Processo, grupoCodigo);
		
		if (tempList != null && tempList.size()>0){
			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			ServentiaNe serventiaNe = new ServentiaNe();
			ServentiaDt servProc = serventiaNe.consultarId(id_serventiaProcesso);
			for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
				ServentiaCargoDt responsavelProcesso = (ServentiaCargoDt) iterator.next();
				
				//alteração leandro******************************************************************************************************************************************************
				ServentiaCargoDt serventiaCargoSubstituto = null;
				// Consulta gabinete substituto (se existir)
	    			ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
	    			ServentiaDt serventiaSubstitutaDt = serventiaRelacionadaNe.consultarGabineteSubstitutoAtualSegundoGrau(id_serventiaProcesso, responsavelProcesso.getId_Serventia()); 			
	    			// testa para verificar se possui gabinete substituto, caso exista consulta o cargo de desembargador ativo (com quantidade de distribuição) deste gabinete 
	    			ServentiaCargoDt serventiaCargoTitularServentiaSubstituta = null;

	    			if (serventiaSubstitutaDt != null) {
	    				serventiaCargoTitularServentiaSubstituta = serventiaCargoNe.getDesembargadorTitular(serventiaSubstitutaDt.getId(), null);
	    			}
    			
	    			if (serventiaCargoTitularServentiaSubstituta != null)
	    				serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(serventiaCargoTitularServentiaSubstituta.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
	    			else
	    				serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(responsavelProcesso.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
				
	    			if (serventiaCargoSubstituto == null)
	    				serventiaCargoSubstituto = serventiaCargoTitularServentiaSubstituta;
	    			//**************************************************************************************************************************************************************************
				
				//ServentiaCargoDt serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(responsavelProcesso.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
				if (serventiaCargoSubstituto != null 
						&& serventiaCargoSubstituto.getId() != null && serventiaCargoSubstituto.getId().length()>0
						&& responsavelProcesso.getQuantidadeDistribuicao() != null && responsavelProcesso.getQuantidadeDistribuicao().length()>0
						&& Funcoes.StringToInt(responsavelProcesso.getQuantidadeDistribuicao()) > 0 ){
					responsavelProcesso.setNomeUsuario(serventiaCargoSubstituto.getNomeUsuario() +" substituindo "+responsavelProcesso.getNomeUsuario());
					responsavelProcesso.setCargoTipo(serventiaCargoSubstituto.getServentiaCargo() +" / "+responsavelProcesso.getCargoTipo());
					responsavelProcesso.setServentia(responsavelProcesso.getServentia());
					listRetorno.add(responsavelProcesso);
				}  else listRetorno.add(responsavelProcesso);
			}
		}
		neObjeto = null;
		
		if (listRetorno != null && listRetorno.size()>0)
			return listRetorno;
		else
			return tempList;
	}
	
	/**
	 * Consulta os responsáveis desabilitados de um processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_serventiaProcesso , id da serventia do processo
	 * @return lista de responsáveis
	 * @author hmgodinho
	 */
	public List consultarResponsaveisDesabilitadosProcesso(String id_Processo, String id_serventiaProcesso) throws Exception {
		List tempList = null;
		List listRetorno = new ArrayList();
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		tempList = neObjeto.consultarResponsaveisDesabilitadosProcesso(id_Processo);
		
		if (tempList != null && tempList.size()>0){
			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			ServentiaNe serventiaNe = new ServentiaNe();
			ServentiaDt servProc = serventiaNe.consultarId(id_serventiaProcesso);
			for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
				ServentiaCargoDt responsavelProcesso = (ServentiaCargoDt) iterator.next();
				
				//alteração leandro******************************************************************************************************************************************************
				ServentiaCargoDt serventiaCargoSubstituto = null;
				// Consulta gabinete substituto (se existir)
	    			ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
	    			ServentiaDt serventiaSubstitutaDt = serventiaRelacionadaNe.consultarGabineteSubstitutoAtualSegundoGrau(id_serventiaProcesso, responsavelProcesso.getId_Serventia()); 			
	    			// testa para verificar se possui gabinete substituto, caso exista consulta o cargo de desembargador ativo (com quantidade de distribuição) deste gabinete 
	    			ServentiaCargoDt serventiaCargoTitularServentiaSubstituta = null;

	    			if (serventiaSubstitutaDt != null) {
	    				serventiaCargoTitularServentiaSubstituta = serventiaCargoNe.getDesembargadorTitular(serventiaSubstitutaDt.getId(), null);
	    			}
    			
	    			if (serventiaCargoTitularServentiaSubstituta != null)
	    				serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(serventiaCargoTitularServentiaSubstituta.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
	    			else
	    				serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(responsavelProcesso.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
				
	    			if (serventiaCargoSubstituto == null)
	    				serventiaCargoSubstituto = serventiaCargoTitularServentiaSubstituta;
	    			//**************************************************************************************************************************************************************************
				
				//ServentiaCargoDt serventiaCargoSubstituto = serventiaCargoNe.getDesembargadorSubstituto(responsavelProcesso.getId_Serventia(), servProc.getServentiaSubtipoCodigo());
				if (serventiaCargoSubstituto != null 
						&& serventiaCargoSubstituto.getId() != null && serventiaCargoSubstituto.getId().length()>0
						&& responsavelProcesso.getQuantidadeDistribuicao() != null && responsavelProcesso.getQuantidadeDistribuicao().length()>0
						&& Funcoes.StringToInt(responsavelProcesso.getQuantidadeDistribuicao()) > 0 ){
					responsavelProcesso.setNomeUsuario(serventiaCargoSubstituto.getNomeUsuario() +" substituindo "+responsavelProcesso.getNomeUsuario());
					responsavelProcesso.setCargoTipo(serventiaCargoSubstituto.getServentiaCargo() +" / "+responsavelProcesso.getCargoTipo());
					responsavelProcesso.setServentia(responsavelProcesso.getServentia());
					listRetorno.add(responsavelProcesso);
				}  else listRetorno.add(responsavelProcesso);
			}
		}
		neObjeto = null;
		
		if (listRetorno != null && listRetorno.size()>0)
			return listRetorno;
		else
			return tempList;
	}

	public List consultarJuizesSegundoGrauResponsaveisProcesso(String id_Processo) throws Exception {
		List tempList = null;
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		tempList = neObjeto.consultarJuizesSegundoGrauResponsaveisProcesso(id_Processo);
		neObjeto = null;
		return tempList;
	}

	public List consultarJuizesTurma(String id_Serventia) throws Exception {
		List tempList = null;
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		tempList = neObjeto.consultarJuizesTurma(id_Serventia);
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarAdvogadosProcessoPublico(String id_Processo) throws Exception {

		List tempList = null;
		ProcessoParteAdvogadoNe neObjeto = new ProcessoParteAdvogadoNe();

		tempList = neObjeto.consultarAdvogadosProcesso(id_Processo);
		// Se processo é Segredo de Justiça retorna somente as inicias do
		// nome
		if (tempList != null) {
			for (int i = 0; i < tempList.size(); i++) {
				ProcessoParteAdvogadoDt objProcesso = (ProcessoParteAdvogadoDt) tempList.get(i);
				if (objProcesso.getSegredoJustica()) {
					objProcesso.setNomeParte(Funcoes.iniciaisNome(objProcesso.getNomeParte()));
				}
			}
		}
		neObjeto = null;
		return tempList;

	}

	public List consultarAdvogadosProcesso(String id_Processo) throws Exception {
		List tempList = null;
		ProcessoParteAdvogadoNe neObjeto = new ProcessoParteAdvogadoNe();

		tempList = neObjeto.consultarAdvogadosProcesso(id_Processo);
		neObjeto = null;
		return tempList;
	}	
	
	/**
	 * Consulta os advogados que foram habilitados E desabilitados no processo.
	 * @param id_processo - ID do processo
	 * @return lista de advogados desabilitados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAdvogadosDesabilitadosProcesso(String id_Processo) throws Exception {
		List tempList = null;
		ProcessoParteAdvogadoNe neObjeto = new ProcessoParteAdvogadoNe();

		tempList = neObjeto.consultarAdvogadosDesabilitadosProcesso(id_Processo);
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarAdvogadoParteProcesso(String Id_Processo, String Id_ProcessoParte, FabricaConexao obFabricaConexao) throws Exception {
		List tempList = null;
		ProcessoParteAdvogadoNe neObjeto = new ProcessoParteAdvogadoNe();

		tempList = neObjeto.consultarAdvogadoParteProcesso(Id_Processo, Id_ProcessoParte, obFabricaConexao);
		neObjeto = null;
		
		return tempList;
	}
	
	/**
	 * Método que consulta um recurso ativo do processo e retorna se foi ou não encontrado.
	 * @param idProcesso - ID do processo
	 * @return boolean - true se encontrou e false se não encontrou recurso
	 * @author hmgodinho
	 */
	public boolean temRecursoAtivo(String idProcesso) throws Exception {
		List tempList = null;
		RecursoNe neObjeto = new RecursoNe();
		String idRecurso  = neObjeto.consultarRecursoAtivoMaisAntigo(idProcesso);
		if(idRecurso != null && idRecurso.length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarAreasDistribuicaoAtivas(String tempNomeBusca, String posicaoPaginaAtual, String serventiaTipoCodigo) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		tempList = neObjeto.consultarAreasDistribuicaoRedistribuicaoAtivas(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public AreaDistribuicaoDt getAreaDistribuicaoRecursal(String id_AreaDistribuicao) throws Exception {
		AreaDistribuicaoDt areaRelacionada = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		// areaRelacionada =
		// neObjeto.getAreaDistribuicaoRelacionada(id_AreaDistribuicao);
		areaRelacionada = neObjeto.consultarId(id_AreaDistribuicao);		
		neObjeto = null;
		return areaRelacionada;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public AreaDistribuicaoDt consultarAreaDistribuicaoId(String id_AreaDistribuicao) throws Exception {
		AreaDistribuicaoDt areaDistribuicaoDt = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		areaDistribuicaoDt = neObjeto.consultarId(id_AreaDistribuicao);
		neObjeto = null;
		return areaDistribuicaoDt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarServentiasRedistribuicao(String tempNomeBusca, String posicaoPaginaAtual, String serventiaTipoCodigo) throws Exception {
		List tempList = null;
		ServentiaNe neObjeto = new ServentiaNe();
		tempList = neObjeto.consultarServentiasRedistribuicao(tempNomeBusca, posicaoPaginaAtual, serventiaTipoCodigo);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarServentiasAtivasRedistribuicaoLote(String descricao, String posicao, String serventiaTipoCodigo, String id_Comarca) throws Exception {
		List tempList = null;
		ServentiaNe neObjeto = new ServentiaNe();
		tempList = neObjeto.consultarServentiasAtivasRedistribuicaoLote(descricao, posicao, serventiaTipoCodigo, id_Comarca);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}
	
	public String consultarServentiasAtivasRedistribuicaoLoteJSON(String descricao, String posicao, UsuarioNe usuarioSessao) throws Exception {
		String stTemp = "";
		
		ServentiaNe neObjeto = new ServentiaNe();
		stTemp = neObjeto.consultarServentiasAtivasRedistribuicaoLoteJSON(descricao, posicao, usuarioSessao);
		
		return stTemp;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarProcesssosRedistribuicaoLote(String id_Serventia, String id_ServentiaCargo, String id_ProcessoTipo, String arquivado, String id_Classificador, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			tempList =  obPersistencia.consultarProcessosRedistribuicaoLote(id_Serventia, id_ServentiaCargo, id_ProcessoTipo, arquivado, id_Classificador, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta a quantidade de Processos Ativos com tratamentos diferenciados
	 * dependendo do grupo do usuário. Consulta será utilizada para montar o
	 * quantitativo de processos na página inicial dos usuários.
	 * 
	 * @param UsuarioDt
	 *            usuario logado
	 */
	public List consultarQuantidadeAtivos(UsuarioDt usuarioDt) throws Exception {
		List liTemp = null;
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			switch (grupoTipo) {
    			case GrupoTipoDt.ADVOGADO:
    				liTemp = obPersistencia.consultarQuantidadeAtivosAdvogado(usuarioDt.getId_UsuarioServentia());
    				break;
    			case GrupoTipoDt.ASSESSOR_MP:
    				if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equalsIgnoreCase("")) {
    					liTemp = obPersistencia.consultarQuantidadeAtivosPromotor(usuarioDt.getId_ServentiaCargoUsuarioChefe());
    				} else {
    					// Se o usuário chefe do assistente de promotor não
    					// tiver Serventia Cargo cadastrado
    					// deve ser gerado um registro na lista que diz ter
    					// 0 processos cadastados. Esse formato
    					// de registro é gerado para gerar link
    					// "não funcional" na tela inicial do assistente
    					liTemp = new ArrayList();
    					liTemp.add(new String[] { "0", "Ativos", "0" });
    				}
    				break;	
    			case GrupoTipoDt.ASSESSOR_ADVOGADO:
    				if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
    					liTemp = obPersistencia.consultarQuantidadeAtivosAdvogado(usuarioDt.getId_UsuarioServentiaChefe());					
    				}
    				break;
    			case GrupoTipoDt.AUTORIDADE_POLICIAL:
    				liTemp = obPersistencia.consultarQuantidadeAtivosDelegacia(usuarioDt.getId_Serventia());
    				break;
    			case GrupoTipoDt.MP:
    				if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")) {
    					liTemp = obPersistencia.consultarQuantidadeAtivosPromotor(usuarioDt.getId_ServentiaCargo());
    				}
    				break;
    			// Quando for assistente dependendo do tipo do usuário chefe faz a consulta específica
    			case GrupoTipoDt.ASSESSOR:
    				if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
    					switch (Funcoes.StringToInt(usuarioDt.getGrupoUsuarioChefe())) {
    					case GrupoDt.AUTORIDADES_POLICIAIS:
    						liTemp = obPersistencia.consultarQuantidadeAtivosDelegacia(usuarioDt.getId_Serventia());
    						break;
    					default:
    						liTemp = obPersistencia.consultarQuantidadeProcessosServentia(usuarioDt.getId_Serventia());
    						break;
    					}
    				}
    				break;
    			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
    				if (usuarioDt.isJuizUPJ()) {
    					liTemp = obPersistencia.consultarListaAtivosJuizUPJ(usuarioDt.getId_ServentiaCargo());
    				} else {
    					liTemp = obPersistencia.consultarQuantidadeProcessosServentia(usuarioDt.getId_Serventia());
    				}
    				break;
    			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
    				if (usuarioDt.isJuizUPJ()) {
    					liTemp = obPersistencia.consultarListaAtivosJuizUPJ(usuarioDt.getId_ServentiaCargo());
    				} else {
    					liTemp = obPersistencia.consultarQuantidadeAtivosDesembargador(usuarioDt.getId_ServentiaCargo());
    				}
    				break;
    			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
    				liTemp = obPersistencia.consultarQuantidadeAtivosDesembargador(usuarioDt.getId_ServentiaCargoUsuarioChefe());
    				break;
    			case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
    				liTemp = obPersistencia.consultarQuantidadeAtivosDistribuidor(usuarioDt.getId_Serventia());
    				break;
    			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
    			case GrupoTipoDt.ASSISTENTE_GABINETE:
    				if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equalsIgnoreCase("")) {
    					liTemp = obPersistencia.consultarQuantidadeAtivosAssistenteGabinete(usuarioDt.getId_ServentiaCargo());
    				}			
    				break;			
    			case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
    				break;
    			case GrupoTipoDt.GERAL:
    			case GrupoTipoDt.CONTADOR:
    			case GrupoTipoDt.CONSULTOR:
    			case GrupoTipoDt.JUIZ_AUXILIAR:
    			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
    			case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:			
    				break;
    			case GrupoTipoDt.COORDENADOR_PROMOTORIA:
    				liTemp = obPersistencia.consultarQuantidadeAtivosServentiaPromotoria(usuarioDt.getId_Serventia());
    				break;
    			//Foi adicionado o grupo do escritório, pois o funcionamento das serventias é idêntico
    			case GrupoTipoDt.COORDENADOR_DEFENSORIA_PUBLICA:
    			case GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO:
    			case GrupoTipoDt.COORDENADOR_PROCURADORIA:
    			case GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA:
    				liTemp = obPersistencia.consultarQuantidadeAtivosServentiaProcuradoria(usuarioDt.getId_Serventia());
    				break;
    			case GrupoTipoDt.ESTAGIARIO:
    				int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
    				switch (grupo) {
    					case GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU:
    					case GrupoDt.ESTAGIARIO_SEGUNDO_GRAU:
    						if (usuarioDt != null && usuarioDt.getServentiaTipoCodigo() != null && usuarioDt.isServentiaTipo2Grau()) {
    							liTemp = obPersistencia.consultarQuantidadeAtivosSegundoGrau(usuarioDt.getId_Serventia());
    						} else {
    							liTemp = obPersistencia.consultarQuantidadeProcessosServentia(usuarioDt.getId_Serventia());
    						}
    						break;
    					case GrupoDt.ESTAGIARIO_GABINETE:
    						liTemp = obPersistencia.consultarQuantidadeAtivosDistribuidor(usuarioDt.getId_Serventia());
    						break;
    				}
    				break;
    			default:
    				// Consulta os processos
    				if (usuarioDt != null && usuarioDt.getServentiaTipoCodigo() != null && usuarioDt.isServentiaTipo2Grau()) {
    					liTemp = obPersistencia.consultarQuantidadeAtivosSegundoGrau(usuarioDt.getId_Serventia());
    				} else {
    					liTemp = obPersistencia.consultarQuantidadeProcessosServentia(usuarioDt.getId_Serventia());
    				}
    				break;
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de Processos com possível prescrição com tratamentos diferenciados
	 * dependendo do grupo do usuário. Consulta será utilizada para montar o
	 * quantitativo de processos na página inicial dos usuários.
	 * 
	 * @param UsuarioDt
	 *            usuario logado
	 */
	public long consultarQuantidadePossiveisPrescritos(UsuarioDt usuarioDt) throws Exception {
		long loQuantidade = 0;
		int grupoTipo = -1;
		if (usuarioDt != null) grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			switch (grupoTipo) {
			case GrupoTipoDt.DISTRIBUIDOR:
//				loQuantidade = obPersistencia.consultarQuantidadePossiveisPrescritos(null, usuarioDt.getId_Comarca());
				break;
			default:
				loQuantidade = obPersistencia.consultarQuantidadePossiveisPrescritos(usuarioDt.getId_Serventia(), null);
				break;	
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	/**
	 * Consulta a quantidade de Processos de Cálculo. Consulta será utilizada para montar o
	 * quantitativo de processos na página inicial dos usuários.
	 * 
	 * @param UsuarioDt: usuario logado
	 */
	public List consultarQuantidadeCalculo(String idServentia) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			liTemp = obPersistencia.consultarQuantidadeCalculoServentia(idServentia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}
	
	/**
	 * 
	 * @param processoDt
	 * @param diretorioProjeto
	 * @param nomeUsuarioLogado
	 * @param codigosArquivos
	 * @param codigosMovimentacoes
	 * @param servletOutputStream
	 * @throws Exception
	 */
	public void gerarPdfProcessoIncompleto(ProcessoDt processoDt, String diretorioProjeto, String nomeUsuarioLogado	, String[] codigosArquivos, String[] codigosMovimentacoes, OutputStream servletOutputStream, UsuarioNe usuarioNe) throws Exception {
		
		ArquivoNe arquivoNe = new ArquivoNe();
		
		MovimentacaoArquivoNe movimentacaoArquivo = new MovimentacaoArquivoNe();
		
		ProjudiPropriedades projudiPropriedades = ProjudiPropriedades.getInstance();
		
		// Faz as verificações iniciais de inconsistência
		if (processoDt == null) throw new MensagemException("O processo não existe.");
		if (processoDt.getListaMovimentacoes() == null) throw new MensagemException("O processo não possui movimentações.");
		
		String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
		
		// Aceitar comunicacao https
		Signer.acceptSSL();
				
		HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) new CssAppliersImpl(new XMLWorkerFontProvider()));
		
    	// Cria o buffer de saída do pdf gerado
    	BufferedOutputStream bos = new BufferedOutputStream(servletOutputStream);
    	
    	// Cria uma instância do PDF final (com todas as páginas concatenadas)
    	Document document = new Document();
    	
    	// Cria o copiador de páginas de pdf, onde irá conter todas as páginas do documento final
    	PdfCopy pdfCopy = new PdfCopy(document, bos);
			
    	// Abre o documento pdf
    	document.open();
    	
    	// Gera a capa do processo; 1a página do arquivo PDF
    	byte[] capaProcesso = GerarCapaProcessoPDF.gerarCapaProcessoPDF(processoDt);
    	pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, capaProcesso);
    	
    	Iterator itMovimentacoes = processoDt.getListaMovimentacoes().iterator();
    	
    	int contMovimentacao = 0;
    	int countArqFisico = 0;
    	
    	List listMovimentacoes = Arrays.asList(codigosMovimentacoes);
    	
    	ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();
    	
    	//ando em todas as movimentaçoes
    	while (itMovimentacoes.hasNext()) {
			
			MovimentacaoDt movimentacao = (MovimentacaoDt) itMovimentacoes.next();
			contMovimentacao += 1;
			
			//retorno se a movimentação nao foi selecionada
			if (!listMovimentacoes.contains(movimentacao.getId())){
				continue;
			}
			// Gerar pdf com mensagem de movimentação inválida		
			if (movimentacao.getCodigoTemp().equals("6")) {
				String strMovimentacao = movimentacao.getDescricaoMovimentacaoTipoComplemento();
				
				// Cria um pdf apenas com 1 página e com a mensagem de inconsistência
				byte[] movInvalida = GerarPDF.gerarPDF(strMovimentacao, "Não será possível mostrar os \"Arquivos\" da movimentação: " + strMovimentacao + ", pois a mesma está Bloqueada.");
				
				// Cria um pdf com o cabeçalho do processo e a página de inconsistência
				byte[] arquivoCabecalho = GerarCabecalhoProcessoPDF.geraCabecalhoProcessoSemArquivoPDF(processoDt, nomeUsuarioLogado, contMovimentacao+" : "+ movimentacao.getMovimentacaoTipo(), movInvalida);
				
				// Concatena os arquivos no pdf final
				pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, arquivoCabecalho);
				
			//Movimentação válida	
			}else {
				List listMovimentacaoArquivo = null;
				// Verifica se a movimentação não tem arquivo
				if (!movimentacao.temArquivos()) {
					// Gerar pdf com mensagem de movimentação sem arquivos
					//gerar pdf com mensagem de movimentação sem arquivo
					String strMovimentacao = movimentacao.getDescricaoMovimentacaoTipoComplemento();
					
					byte[] movSemArquivo = GerarPDF.gerarPDF(movimentacao.getMovimentacaoTipo(), "A movimentação: ( "+ strMovimentacao+" ) do dia " + movimentacao.getDataRealizacao() + " não possui \"Arquivos\".");
					
					byte[] arquivoCabecalho = GerarCabecalhoProcessoPDF.geraCabecalhoProcessoSemArquivoPDF(processoDt, nomeUsuarioLogado, contMovimentacao+" : "+ movimentacao.getMovimentacaoTipo(), movSemArquivo);
					
					// Concatena os arquivos no pdf final
					pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, arquivoCabecalho);
				
				//movimentação válida e com arquivos					
				}else{
					listMovimentacaoArquivo = movimentacaoArquivo.consultarConteudoArquivosMovimentacoes(movimentacao.getId(), usuarioNe.getNivelAcesso());					
					Iterator iteratorMovimentacaoArquivo = listMovimentacaoArquivo.iterator();
					int contArquivo = 0;
					
					while (iteratorMovimentacaoArquivo.hasNext()) {
						
						MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) iteratorMovimentacaoArquivo.next();
						
						if (!movimentacaoArquivo.podeBaixarArquivo(usuarioNe.getUsuarioDt(),processoDt, movimentacaoArquivoDt)) {
							// Gerar pdf com mensagem de arquivo inválida ou bloqueado							 							
							String strMovimentacao = movimentacao.getDescricaoMovimentacaoTipoComplemento();							
							// Cria um pdf apenas com 1 página e com a mensagem de inconsistência
							byte[] movInvalida = GerarPDF.gerarPDF(strMovimentacao, "Não será possível mostrar o \"Arquivo\" da movimentação: " + strMovimentacao + ", pois o seu nível de acesso é insuficiente.");							
							// Cria um pdf com o cabeçalho do processo e a página de inconsistência
							byte[] arquivoCabecalho = GerarCabecalhoProcessoPDF.geraCabecalhoProcessoSemArquivoPDF(processoDt, nomeUsuarioLogado, contMovimentacao+" : "+ movimentacao.getMovimentacaoTipo(), movInvalida);							
							// Concatena os arquivos no pdf final
							pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, arquivoCabecalho);
						}else{
							
							byte[] conteudoArquivo = null;
							byte[] arquivoCabecalho = null;
							
							List listArquivos = Arrays.asList(codigosArquivos);
							//verifica se o arquivo foi selecionado
							if(listArquivos.contains(movimentacaoArquivoDt.getId_Arquivo())){
								contArquivo ++;
								conteudoArquivo = null;
								arquivoCabecalho = null;
								
								String nomeArquivo = movimentacaoArquivoDt.getArquivoDt().getNomeArquivo().toLowerCase();
								String extensaoFinal = Funcoes.retorneExtencaoDoArquivo(nomeArquivo);
								
								byte[] byTemp = null;
								boolean processoMigradoPje = false;
								
								bookmarks.add(Funcoes.addLink(nomeArquivo, (countArqFisico == 0) ? 2 : pdfCopy.getCurrentPageNumber()));
								
								if (movimentacaoArquivoDt.getCodigoTemp() != null && movimentacaoArquivoDt.getCodigoTemp().equalsIgnoreCase(String.valueOf(MovimentacaoArquivoDt.PJE_MIGRADO))){
									byTemp = movimentacaoArquivoDt.getArquivoDt().getConteudoSemAssinar();
									processoMigradoPje = true;
									
								} else if (movimentacaoArquivoDt.getCodigoTemp() != null && movimentacaoArquivoDt.getCodigoTemp().equalsIgnoreCase(String.valueOf(MovimentacaoArquivoDt.OBJECT_STORAGE))){
																											
									byTemp = null;
									
									try {
										
										//bookmarks.add(Funcoes.addLink(nomeArquivo, (countArqFisico == 0) ? 2 : pdfCopy.getCurrentPageNumber()));
										
										String path = movimentacaoArquivoDt.getArquivoDt().getCaminho();
										
										if (path != null && path.indexOf("/") != 3) {
											path = Funcoes.obtenhaSomenteNumeros(path).substring(9, 13) + "/" + Funcoes.obtenhaSomenteNumeros(path).substring(0, 3) + "/" + path;
										}
										
										InputStream in = arquivoNe.obtenhaStreamObjectStorageDigitalizacao(path);
										
										BufferedInputStream bis = new BufferedInputStream(in);
										
										PdfReader reader = new PdfReader(bis);
										
										int numberOfPages = reader.getNumberOfPages();
										
										reader.removeUnusedObjects();
																				
										for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++){
											
											PdfImportedPage page = pdfCopy.getImportedPage(reader, pageNumber);
											
											PdfCopy.PageStamp pageStamp = pdfCopy.createPageStamp(page);
											
											PdfContentByte over = pageStamp.getOverContent();
											
											over.beginText();
											
											Rectangle pageSize = reader.getPageSize(pageNumber);
											
											GerarCabecalhoProcessoPDF.imprimirEstampaLateralPagina(over
													, pageSize.getWidth(), pageSize.getHeight(), pageSize.getBottom()
													, processoDt
													, nomeUsuarioLogado
													, movimentacao.getDescricaoMovimentacaoTipoComplemento()
													, nomeArquivo);
											
											over.endText();
											
											pageStamp.alterContents();
											
											pdfCopy.addPage(page);
																						
										}
										
										pdfCopy.freeReader(reader);
																														
										reader.close();
										bis.close();
																				
									} catch (MensagemException ex){
										conteudoArquivo = GerarPDF.gerarPDF(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado(), ex.getMessage());
										pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, conteudoArquivo);
									}
									
									countArqFisico++;
									
								} else{
									try {
										ArquivoDt arquivoDt = movimentacaoArquivoDt.getArquivoDt();
										arquivoDt = arquivoNe.consultarId(arquivoDt.getId());
										if (arquivoDt.isECarta()) {
											byTemp = arquivoDt.obterConteudoECarta();
										} else {
											byTemp = arquivoDt.getConteudo();
										}
									}catch (Exception e) {
										conteudoArquivo = GerarPDF.gerarMensagemPDF(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado(), "Não foi possível abrir arquivo "+contArquivo+" da Movimentação "+contMovimentacao+". Verifique o arquivo no processo, pois o mesmo pode estar corrompido.");
									}
								}
								
								if (byTemp != null || conteudoArquivo != null){
									
									if (extensaoFinal.equalsIgnoreCase("html")){
										try {
											conteudoArquivo = ConverterHtmlPdf.converteHtmlPDF(byTemp, hpc);
										} catch (Exception e) {
											conteudoArquivo = GerarPDF.gerarMensagemPDF(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado(), "Não foi possível abrir arquivo "+contArquivo+" da Movimentação "+contMovimentacao+". Verifique o arquivo no processo, pois o mesmo pode estar corrompido.");
										}
									} else if (extensaoFinal.equalsIgnoreCase("jpg")){
										conteudoArquivo = ConverteImagemPDF.gerarPdfImagem(byTemp);
										
									} else if (!(extensaoFinal.equalsIgnoreCase("pdf"))){
										conteudoArquivo = GerarPDF.gerarPDF(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado(), "Este arquivo não pode ser aberto, pois não está no formato \".pdf\" ou \".html\". Utilize a \"navegação pelo processo\" para abrir o arquivo.");
									}
									
									if (conteudoArquivo != null){
										arquivoCabecalho = GerarCabecalhoProcessoPDF.geraCabecalhoProcessoPDF(processoDt, pathImage, movimentacaoArquivoDt.getArquivoDt()
												, nomeUsuarioLogado,contMovimentacao+" : "+ movimentacao.getMovimentacaoTipo(), contArquivo, conteudoArquivo, processoMigradoPje);
										pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, arquivoCabecalho);		
										
									} else {
										
										try {
											arquivoCabecalho = GerarCabecalhoProcessoPDF.geraCabecalhoProcessoPDF(processoDt, pathImage, movimentacaoArquivoDt.getArquivoDt()
													, nomeUsuarioLogado,contMovimentacao+" : "+ movimentacao.getMovimentacaoTipo(), contArquivo, byTemp,processoMigradoPje);
										} catch (Exception e){
											arquivoCabecalho = GerarPDF.gerarMensagemPDF(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado()
													, "Não foi possível abrir arquivo "+contArquivo+" da Movimentação "+contMovimentacao+". Verifique o arquivo no processo, pois o mesmo pode estar corrompido.");
										}
										
										if (arquivoCabecalho != null && arquivoCabecalho.length > 0) 											
											pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, arquivoCabecalho);										
										else {											
											byte[] arquivoSenha = GerarPDF.gerarPDF(movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado(), "Este arquivo não pode ser aberto, pois o advogado inseriu o mesmo com senha que não permite a alteração, assim não é possível colocar a nota lateral com as informações do processo.");
											arquivoCabecalho = GerarCabecalhoProcessoPDF.geraCabecalhoProcessoPDF(processoDt, pathImage, movimentacaoArquivoDt.getArquivoDt(), nomeUsuarioLogado, contMovimentacao+" : "+ movimentacao.getMovimentacaoTipo(), contArquivo, arquivoSenha,processoMigradoPje);
											pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, arquivoCabecalho);
											arquivoSenha = null;
										}
									}
									
								}
							}							
						}
						
					}//while arquivos
				}//movimentacao com arquivos
			}//movimentacao aberta
		}//while
		
    	pdfCopy.setOutlines(bookmarks);
    	pdfCopy.close();
    	
    	// Fecha o documento e libera recursos
		document.close();
		bos.close();
		
	}
	
	/**
	 * Consulta a quantidade de processos ativos para um Juiz.
	 * 
	 * @param id_ServentiaCargo
	 *            , identificação do cargo na serventia
	 * @param id_Serventia
	 *            , identificação da serventia
	 * @author msapaula
	 */
	public int consultarQuantidadeAtivosJuiz(UsuarioDt usuarioDt) throws Exception {
		int qtde = 0;
		// int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			switch (grupoTipo) {
			// case GrupoDt.JUIZES_VARA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				if (usuarioDt.isJuizUPJ()) {
					qtde = obPersistencia.consultarQuantidadeAtivosJuizUPJ(usuarioDt.getId_ServentiaCargo(), usuarioDt.getId_Serventia());
				}else {
    				if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().length() > 0) {
    					qtde = obPersistencia.consultarQuantidadeAtivosJuiz(usuarioDt.getId_ServentiaCargo(), usuarioDt.getId_Serventia());
    				}
				}
				break;

			// case GrupoDt.JUIZES_TURMA_RECURSAL:
			case GrupoTipoDt.JUIZ_TURMA:
				if (usuarioDt.getId_ServentiaCargo() != null && usuarioDt.getId_ServentiaCargo().length() > 0) {
					qtde = obPersistencia.consultarQuantidadeAtivosJuizTurmaRecursal(usuarioDt.getId_ServentiaCargo(), usuarioDt.getId_Serventia());
				}
				break;

			// case GrupoDt.ASSISTENTES_JUIZES_VARA:
			// if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null &&
			// !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
			// qtde =
			// obPersistencia.consultarQuantidadeAtivosJuiz(usuarioDt.getId_ServentiaCargoUsuarioChefe(),
			// usuarioDt.getId_Serventia());
			// }
			// break;
			//
			// case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
			// if (usuarioDt.getId_ServentiaCargoUsuarioChefe() != null &&
			// !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")) {
			// qtde =
			// obPersistencia.consultarQuantidadeAtivosJuizTurmaRecursal(usuarioDt.getId_ServentiaCargoUsuarioChefe(),
			// usuarioDt.getId_Serventia());
			// }
			// break;

			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
				switch (Funcoes.StringToInt(usuarioDt.getGrupoUsuarioChefe())) {
    				case GrupoDt.JUIZES_VARA:
    				case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
    				case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
    				case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
    				case GrupoDt.JUIZ_EXECUCAO_PENAL:
    					qtde = obPersistencia.consultarQuantidadeAtivosJuiz(usuarioDt.getId_ServentiaCargoUsuarioChefe(), usuarioDt.getId_Serventia());
    					break;
    				case GrupoDt.JUIZES_TURMA_RECURSAL:
    					qtde = obPersistencia.consultarQuantidadeAtivosJuizTurmaRecursal(usuarioDt.getId_ServentiaCargoUsuarioChefe(), usuarioDt.getId_Serventia());
    					break;
				}

				break;
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return qtde;
	}

	/**
	 * Método de Consulta Processo(s) com um tipo de pendência específico.
	 * 
	 * @param String
	 *            numeroProcesso
	 * @param Integer
	 *            tipoPendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarProcessoPendencia(String numeroProcesso, Integer tipoPendencia) throws Exception {
		List listTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listTemp = obPersistencia.consultarProcessoPendencia(numeroProcesso, tipoPendencia);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return listTemp;
	}

	/**
	 * Consulta o classificador do processo
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 */

	public ClassificadorDt consultarClassificadorProcesso(String id_Processo) throws Exception {
		ClassificadorDt classificadorDt = null;
		ClassificadorNe neObjeto = new ClassificadorNe();
		classificadorDt = neObjeto.consultarClassificadorProcesso(id_Processo);
		neObjeto = null;
		return classificadorDt;
	}

	/**
	 * Chama método que irá efetuar a consulta por UsuarioServentiaOab
	 */
	public UsuarioServentiaOabDt consultarUsuarioServentiaOab(String oabNumero, String oabComplemento, String oabUf) throws Exception {
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		UsuarioServentiaOabNe neObjeto = new UsuarioServentiaOabNe();
		usuarioServentiaOabDt = neObjeto.consultarUsuarioServentiaOab(oabNumero, oabComplemento, oabUf);
		neObjeto = null;
		return usuarioServentiaOabDt;
	}
	
	/**
	 * Chama método que irá efetuar a consulta por UsuarioServentiaOab
	 */
	public UsuarioServentiaOabDt consultarUsuarioServentiaID(String id_UsuarioServentiaOab) throws Exception {
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		UsuarioServentiaOabNe neObjeto = new UsuarioServentiaOabNe();
		usuarioServentiaOabDt = neObjeto.consultarId(id_UsuarioServentiaOab);
		neObjeto = null;
		return usuarioServentiaOabDt;
	}

	/**
	 * Método responsável por consultar os processos semi-paralisados, podendo
	 * ser informado o Id da Serventia.
	 * 
	 * @param idServentia
	 *            - Id da Serventia
	 * @param posicaoPaginaAtual
	 *            - página atual da paginação
	 * @author hmgodinho
	 */
//	public List consultarProcessosSemiParalisadosServentia(String idServentia, String id_ServentiaCargo, String posicaoPaginaAtual) throws Exception {
//		List listTemp = null;
//		FabricaConexao obFabricaConexao = null;
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
//			listTemp = obPersistencia.consultarProcessosSemiParalisadosServentia(idServentia, id_ServentiaCargo, posicaoPaginaAtual);
//			QuantidadePaginas = (Long) listTemp.get(listTemp.size() - 1);
//			listTemp.remove(listTemp.size() - 1);
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//		return listTemp;
//	}

	/**
	 * Método responsável por consultar os processos paralisados, podendo ser
	 * informado o Id da Serventia.
	 * 
	 * @param idServentia
	 *            - Id da Serventia
	 * @param posicaoPaginaAtual
	 *            - página atual da paginação
	 * @author hmgodinho
	 */

	public List consultarProcessosParalisadosServentia(String idServentia, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		return consultarProcessosParalisadosServentia(idServentia, opcaoPeriodo, "", posicaoPaginaAtual);
	}

	public List consultarProcessosParalisadosServentia(String idServentia, String opcaoPeriodo, String id_ServentiaCargo, String posicaoPaginaAtual) throws Exception {
		List listTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listTemp = obPersistencia.consultarProcessosParalisadosServentia(idServentia, opcaoPeriodo, id_ServentiaCargo, posicaoPaginaAtual);
			QuantidadePaginas = (Long) listTemp.get(listTemp.size() - 1);
			listTemp.remove(listTemp.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listTemp;
	}

	/**
	 * Método responsável em realizar a consulta de processos de acordo com o Juiz ou
	 * Classificador passados.
	 * 
	 * @param id_Classificador , identificação do classificador
	 * @param id_Juiz , identificação do juiz responsavel pelo processo
	 * @param id_Serventia , identificação da serventia
	 * @author msapaula, hmgodinho
	 */
	public String consultarProcessosJuizClassificadorJSON(UsuarioDt usuarioDt, String id_Classificador, String id_Juiz, String id_proc_tipo, String id_assunto, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		String id_Serventia = null;
		if (usuarioDt.isGabineteSegundoGrau()) {
			if (usuarioDt.isDesembargador()) {
				id_Juiz=usuarioDt.getId_ServentiaCargo();
			}
		}else {
			id_Serventia = usuarioDt.getId_Serventia();
		}
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessosJuizClassificadorJSON(id_Classificador, id_Juiz, id_Serventia, id_proc_tipo, id_assunto, posicaoPaginaAtual);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public List consultarProcessosJuizClassificador(String id_Classificador, String id_Juiz, String id_Serventia) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosJuizClassificador(id_Classificador, id_Juiz, id_Serventia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Método responsável em consultar os processos de acordo com os dados do
	 * Advogado. Armazena os parâmetros em variáveis locais, para que ao usar a
	 * paginação a consulta não perca os filtros preenchidos pelo usuario.
	 * 
	 * @param statusProcessoCodigo
	 *            - status do processo
	 * @param idServentia
	 *            - ID da Serventia
	 * @param oabNumero
	 *            - número OAB do advogado
	 * @param oabComplemento
	 *            - complemento da OAB do advogado
	 * @param oabUf
	 *            - uf da OAB do advogado
	 * @param posicaoPaginaAtual
	 *            - posição da página atual, para paginação
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosDadosAdvogado(BuscaProcessoDt buscaProcesso, UsuarioDt usuarioDt, String posicaoPaginaAtual) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			buscaProcesso.validarEntradas(objBuscaProcessoDt);
			//objBuscaProcessoDt=buscaProcesso;
			
			if ( 	!buscaProcesso.temOabNumero()  || 
					!buscaProcesso.temComplementoOab()  || 
					!buscaProcesso.temUfOab() )  {
				throw new MensagemException("Deve-se informar o número da OAB, o complemento e o UF");
			}
			
			listaProcessos = obPersistencia.consultarProcessosDadosAdvogado(buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_Serventia(), buscaProcesso.getOabNumero(), buscaProcesso.getOabComplemento(), buscaProcesso.getOabUf(), buscaProcesso.getSituacaoAdvogadoProcesso(), posicaoPaginaAtual);

			// Se processo é Segredo de Justiça retorna somente as inicias do nome
			if (listaProcessos != null) {
				// Retira o ultimo item da lista (quantidade) para evitar erro de conversão para ProcessoDt
				setQuantidadePaginas(listaProcessos);
				for (int i = 0; i < listaProcessos.size(); i++) {
					if (listaProcessos.get(i) instanceof ProcessoDt){
						ProcessoDt objProcesso = (ProcessoDt) listaProcessos.get(i);
						if ( objProcesso.isSegredoJustica() || objProcesso.isSigiloso() || objProcesso.hasVitima()) {
							if(!this.podeAcessarProcesso(usuarioDt, objProcesso, obFabricaConexao)) {
								for (int j = 0; j < objProcesso.getPartesProcesso().size(); j++) {
									ProcessoParteDt parteDt = (ProcessoParteDt) objProcesso.getPartesProcesso().get(j);
									if ( objProcesso.isSegredoJustica() || objProcesso.isSigiloso() || parteDt.isVitima()) {
										parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
									}
								}
							}
						}
					}
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processos pelo número do inquérito. Armazena os parâmetros em variáveis locais, para que ao usar a
	 * paginação a consulta não perca os filtros preenchidos pelo usuario.
	 * 
	 * @param statusProcessoCodigo
	 *            - status do processo
	 * @param idServentia
	 *            - ID da Serventia
	 * @param inquerito
	 *            - número do inquerito
	 * @param posicaoPaginaAtual
	 *            - posição da página atual, para paginação
	 * @return lista de processos
	 * @throws Exception
	 * @author acbloureiro
	 */
	public List consultarProcessosInquerito(BuscaProcessoDt buscaProcesso, UsuarioDt usuarioDt, String posicaoPaginaAtual) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			buscaProcesso.validarEntradas(buscaProcesso);
			//objBuscaProcessoDt=buscaProcesso;
			
			if ( 	!buscaProcesso.temInquerito() )  {
				throw new MensagemException("Deve-se informar o número do Inquérito");
			}
			
			listaProcessos = obPersistencia.consultarProcessosInquerito(buscaProcesso.getProcessoStatusCodigo(), buscaProcesso.getId_Serventia(), buscaProcesso.getInquerito(), posicaoPaginaAtual);

			// Se processo é Segredo de Justiça retorna somente as inicias do nome
			if (listaProcessos != null) {
			// Retira o ultimo item da lista (quantidade) para evitar erro de conversão para ProcessoDt
				setQuantidadePaginas(listaProcessos);
				for (int i = 0; i < listaProcessos.size(); i++) {
					if (listaProcessos.get(i) instanceof ProcessoDt){
						ProcessoDt objProcesso = (ProcessoDt) listaProcessos.get(i);
						if ( objProcesso.isSegredoJustica() || objProcesso.isSigiloso() || objProcesso.hasVitima()) {
							if(!this.podeAcessarProcesso(usuarioDt, objProcesso, obFabricaConexao)) {
								for (int j = 0; j < objProcesso.getPartesProcesso().size(); j++) {
									ProcessoParteDt parteDt = (ProcessoParteDt) objProcesso.getPartesProcesso().get(j);
									if ( objProcesso.isSegredoJustica() || objProcesso.isSigiloso() || parteDt.isVitima()) {
										parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
									}
								}
							}
						}
					}
				}
			}
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}



	/**
	 * Método que gerar o código de acesso ao processo que será utilizado pelas
	 * partes em consulta pública
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param processoNumero
	 *            , número do processo
	 * 
	 * @author msapaula
	 */
//	public String gerarCodigoAcessoProcessoAntigo(String id_Processo, String processoNumero, String id_parte) throws Exception {
//		String stTemp = "";
//
//		// Gera código baseado no id
//		stTemp = Cifrar.codificar(id_Processo);
//
//		// Gera Hash do código concatenado com o número do processo
//		stTemp = Cifrar.hashMd5(stTemp + processoNumero + id_parte);
//		return stTemp;
//	}
	
	/**
	 * Método que gerar o código de acesso ao processo que será utilizado pelas
	 * partes em consulta pública
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param processoNumero
	 *            , número do processo
	 * 
	 * @author jrcorrea
	 */
	public String gerarCodigoAcessoProcesso(String id_Processo, String processoNumero, String id_parte) throws Exception {

		// Gera Hash do código concatenado com o número do processo
		String stTemp = Cifrar.converterDecimalBase32(id_Processo + processoNumero + id_parte);
		
		return stTemp;
	}

	/**
	 * Método que gera o código de acesso para a parte na consultar o processos
	 * 
	 * @param processoNumero
	 *            , número do processo
	 * @param id_parte
	 *            , id da parte
	 * 
	 * @return Código de Acesso
	 * 
	 * @author jrcorrea
	 */
	public String gerarCodigoAcessoProcesso(String id_processo, String id_parte) throws Exception {

		String tempCodigoAcesso = "";

		// Consulta processo de acordo com número de processo informado
		ProcessoDt processoDt = this.consultarId(id_processo);

		if (processoDt != null) {

			tempCodigoAcesso = this.gerarCodigoAcessoProcesso(processoDt.getId(), processoDt.getProcessoNumeroSimples(), id_parte);

		}

		return tempCodigoAcesso;
	}

	/**
	 * Método que valida se o código de acesso informado por uma parte na
	 * consulta de processos é válido
	 * 
	 * @param codigoAcesso
	 *            , código de acesso digitado pelo usuário
	 * @param processoNumero
	 *            , número do processo
	 * 
	 * @return Id do Processo encontrado
	 * 
	 * @author msapaula
	 */
	public String verificarCodigoAcessoProcesso(String codigoAcesso, String processoNumero, String ipComputador, boolean ehConsultaPublica) throws Exception {
		String id_Processo = null;

		// Consulta processo de acordo com número de processo informado
		List listaProcessoDt = this.listarProcessoNumeroCompleto(processoNumero, ehConsultaPublica);

		if (listaProcessoDt != null && listaProcessoDt.size()>0) {
			for (Iterator iterator = listaProcessoDt.iterator(); iterator.hasNext();) {
				ProcessoDt processoDt = (ProcessoDt) iterator.next();
				
				// pego todas as partes, o código tem que ser de uma delas
				List tempPartes = new ArrayList();
				if (processoDt.getListaPolosAtivos() != null) tempPartes.addAll(processoDt.getListaPolosAtivos());
				if (processoDt.getListaPolosPassivos() != null) tempPartes.addAll(processoDt.getListaPolosPassivos());
				if (processoDt.getListaOutrasPartes() != null) tempPartes.addAll(processoDt.getListaOutrasPartes());
	
				for (int i = 0; i < tempPartes.size(); i++) {
					ProcessoParteDt tempParte = (ProcessoParteDt) tempPartes.get(i);
					// Gera código novamente de acordo com id encontrado
					String novoCodigo = this.gerarCodigoAcessoProcesso(processoDt.getId(), processoDt.getProcessoNumeroSimples(), tempParte.getId());
					//String antigoCodigo = this.gerarCodigoAcessoProcessoAntigo(processoDt.getId(), processoDt.getProcessoNumeroSimples(), tempParte.getId());
	
					// Se código gerado bate com código informado pelo usuário,
					// código é valido
					if (codigoAcesso.equalsIgnoreCase(novoCodigo) ) {
						id_Processo = processoDt.getId();
						new LogNe().salvar(new LogDt("Processo", processoDt.getId(), UsuarioDt.SistemaProjudi, ipComputador, String.valueOf(LogTipoDt.Acesso), " Parte " + tempParte.getNome(), ""));
						break;
					}
				}
			}

		}
		return id_Processo;
	}
	
	/**
	 * Método que realiza a consulta dos processos originários a um determinado processo apenso e
	 * verifica se o originário (mais alto na hierarquia) e valida se pode ser realizado o peticionamento.
	 * @param idProceso - id do processo que está recebendo a petição
	 * @param idProcessoPrincipal - id do processo originário
	 * @return true ou false liberando o peticionamento
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean podePeticionarProcessoApenso(String idProcesso, String idProcessoPrincipal) throws Exception {
		ProcessoDt processoOriginarioDt = new ProcessoDt();
		boolean seguirConsultando = true;
		List listaIdsConsultados = new ArrayList();

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			do{
				processoOriginarioDt.setId_ProcessoPrincipal(idProcessoPrincipal);
				listaIdsConsultados.add(idProcessoPrincipal);
				processoOriginarioDt = obPersistencia.consultarProcessoApensoOriginario(processoOriginarioDt.getId_ProcessoPrincipal());			
				idProcessoPrincipal = processoOriginarioDt.getId_ProcessoPrincipal();
				if (listaIdsConsultados.contains(idProcessoPrincipal)){
					throw new MensagemException("Foi detectado que este processo encontra-se em apensamento circular. Entre em contato com a Serventia do processo para resolver o problema no apensamento e realizar o peticionamento."); 
				}
			}while (processoOriginarioDt.isApenso()) ;
			
			//TODO o trecho abaixo foi comentado devido às várias mudanças nos tratamentos de apenso ao longo do ano de 2017 e 2018.
			//A partir de 2019, caso esse trecho siga comentado, poderá ser excluído.
//			RecursoNe recursoNe = new RecursoNe();
//			String idRecursoAtivo =  recursoNe.getRecursoAtivo(idProcesso);
//			//se o processo que está recebendo a petição for APENSO e tiver recurso ATIVO, pode peticionar
//			if(idRecursoAtivo != null && !idRecursoAtivo.equalsIgnoreCase("")){
//				return true;
//			}
			
			//se o processo originário possuir recurso ATIVO, só pode peticionar no processo originário
//			idRecursoAtivo = recursoNe.getRecursoAtivo(processoOriginarioDt.getId_Processo());
//			if(idRecursoAtivo != null && !idRecursoAtivo.equalsIgnoreCase("")){
//				return false;
//			}
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return true;
	}

	/**
	 * Método que realiza a consulta dos processos qu estão em Turmas recursais
	 * onde o usuário informado é o Relator.
	 * 
	 * @param idUsuarioRelator
	 *            - ID do usuário que é o relator do processo
	 * @param id_Serventia
	 *            , identificação da serventia em que o usuário está logado
	 * @param idProcessoTipo - ID do tipo de processo 
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 * @author jrcorrea 10/05/2017
	 */
	public List consultarProcessosUsuarioRelator(String idUsuarioRelator, String id_Serventia, String idProcessoTipo) throws Exception {
		List listaProcessos = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			if (idUsuarioRelator == null || idUsuarioRelator.length() == 0) idUsuarioRelator = stUltimoUsuarioRelator;

			listaProcessos = obPersistencia.consultarProcessosRelator(id_Serventia, idUsuarioRelator, idProcessoTipo);

			// Armazena variáveis da última busca para facilitar a paginação
			stUltimoUsuarioRelator = idUsuarioRelator;

			// Se processo é Segredo de Justiça retorna somente as inicias do
			// nome
			if (listaProcessos != null) {
				for (int i = 0; i < listaProcessos.size(); i++) {
					ProcessoDt objProcesso = (ProcessoDt) listaProcessos.get(i);
					if (objProcesso.getSegredoJustica().equals("true")) {
						for (int j = 0; j < objProcesso.getPartesProcesso().size(); j++) {
							ProcessoParteDt parteDt = (ProcessoParteDt) objProcesso.getPartesProcesso().get(j);
							parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
						}
					}
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	public List consultarProcessoCertdiaoNegativaPositiva(CertidaoNegativaPositivaDt certidaoNegativaPositivaDt) throws Exception {
		List listaProcessos = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosCertidaoPositivaNegativa(certidaoNegativaPositivaDt);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Método que realiza a consulta dos processos sem movimentação na
	 * Serventia.
	 * 
	 * @param idServentia
	 *            - ID do usuário que é o relator do processo
	 * @param opcaoPeriodoSemMovimentacao
	 *            - opção de período selecionada
	 * @param posicaoPaginaAtual
	 *            - página atual da pesquisa
	 * @return lista de processos sem movimentação
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosSemMovimentacaoServentia(String idServentia, String idJuiz, String opcaoPeriodoSemMovimentacao, String posicaoPaginaAtual) throws Exception {
		List listaProcessos = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			if (idServentia == null || idServentia.length() == 0) idServentia = stUltimoId_Serventia;
			if (opcaoPeriodoSemMovimentacao == null || opcaoPeriodoSemMovimentacao.length() == 0) opcaoPeriodoSemMovimentacao = stUltimaOpcaoPeriodoSemMovimentacao;

			listaProcessos = obPersistencia.consultarProcessosSemMovimentacaoServentia(idServentia, idJuiz, opcaoPeriodoSemMovimentacao, posicaoPaginaAtual);

			// Armazena variáveis da última busca para facilitar a paginação
			stUltimoId_Serventia = idServentia;
			stUltimaOpcaoPeriodoSemMovimentacao = opcaoPeriodoSemMovimentacao;

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public String consultarProcessosSemMovimentacaoServentiaJSON(String idServentia, String idJuiz, String opcaoPeriodoSemMovimentacao, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			
			if (idServentia == null || idServentia.length() == 0) idServentia = stUltimoId_Serventia;
			if (opcaoPeriodoSemMovimentacao == null || opcaoPeriodoSemMovimentacao.length() == 0) opcaoPeriodoSemMovimentacao = stUltimaOpcaoPeriodoSemMovimentacao;
			
			// Armazena variáveis da última busca para facilitar a paginação
			stUltimoId_Serventia = idServentia;
			stUltimaOpcaoPeriodoSemMovimentacao = opcaoPeriodoSemMovimentacao;
			
			stTemp = obPersistencia.consultarProcessosSemMovimentacaoServentiaJSON(idServentia, idJuiz, opcaoPeriodoSemMovimentacao, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	/**
	 * Método que consulta processos informando também o tempo de vida de cada um, filtrando por ProcessoTipo.
	 * @param idServentia - id da serventia 
	 * @param idProcessoTipo - id do tipo de processo 
	 * @param opcaoPeriodo - opção de tempo de distribuição
	 * @param posicaoPaginaAtual - página atual da paginação
	 * @return lista JSON contendo os processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarProcessosTempoVidaPorTipoJSON(String idServentia, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			
			if (idServentia == null || idServentia.length() == 0) idServentia = stUltimoId_Serventia;
			if (opcaoPeriodo == null || opcaoPeriodo.length() == 0) opcaoPeriodo = stUltimaOpcaoPeriodo;
			
			// Armazena variáveis da última busca para facilitar a paginação
			stUltimoId_Serventia = idServentia;
			stUltimaOpcaoPeriodo = opcaoPeriodo;
			
			stTemp = obPersistencia.consultarProcessosTempoVidaPorTipoJSON(false, idServentia, opcaoPeriodo, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	
	/**
	 * Método que consulta processos informando também o tempo de vida de cada um, filtrando por ProcessoTipo.
	 * @param idServentia - id da serventia 
	 * @param idProcessoTipo - id do tipo de processo 
	 * @param opcaoPeriodo - opção de tempo de distribuição
	 * @param posicaoPaginaAtual - página atual da paginação
	 * @return lista JSON contendo os processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarProcessosTempoVidaPorTipoDesemJSON(String idUsuServ, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			
			if (idUsuServ == null || idUsuServ.length() == 0) idUsuServ = stUltimoId_UsuServ;
			if (opcaoPeriodo == null || opcaoPeriodo.length() == 0) opcaoPeriodo = stUltimaOpcaoPeriodo;
			
			// Armazena variáveis da última busca para facilitar a paginação
			stUltimoId_UsuServ = idUsuServ;
			stUltimaOpcaoPeriodo = opcaoPeriodo;
									
			stTemp = obPersistencia.consultarProcessosTempoVidaPorTipoJSON(true, idUsuServ, opcaoPeriodo, posicaoPaginaAtual);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}	
	

	/**
	 * Consulta os dados para montar o número completo do processo
	 * 
	 * @param idProcesso
	 *            : identificação do processo
	 * @return String numeroCompleto.
	 * @throws Exception
	 * @author wcsilva
	 */
	public String consultarNumeroCompletoDoProcesso(String idProcesso) throws Exception {
		String numeroCompleto = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			numeroCompleto = obPersistencia.consultarNumeroCompletoDoProcesso(idProcesso);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return numeroCompleto;
	}
	
	/**
	 * Consulta os dados para montar o número completo do processo
	 * 
	 * @param idProcesso
	 *            : identificação do processo
	 * @param FabricaConexao obFabricaConexao
	 * @return String numeroCompleto.
	 * @throws Exception
	 * @author wcsilva
	 */
	public String consultarNumeroCompletoDoProcesso(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		String numeroCompleto = obPersistencia.consultarNumeroCompletoDoProcesso(idProcesso);
		
		return numeroCompleto;
	}
	
	/**
	 * Retorna o id de arquivo tipo ao
	 * 
	 * @param arquivoTipoCodigo
	 * @return String
	 * @throws Exception
	 */
	public String consultarArquivoTipoCodigo(int arquivoTipoCodigo) throws Exception {
		String id = "-1";

		ArquivoTipoNe arquivoTipoNe = new ArquivoTipoNe();
		List ids = arquivoTipoNe.consultarPeloArquivoTipoCodigo(String.valueOf(arquivoTipoCodigo));
		if (ids.size() > 0) id = (String) ids.get(0);

		return id;
	}

	/**
	 * Método responsável por consultar os processos paralisados, podendo ser
	 * informado o Id da Serventia.
	 * 
	 * @param idServentia
	 *            - Id da Serventia
	 * @param idServentiaCargo
	 *            - Id da Serventia Cargo do Juiz Responsável
	 * @param posicaoPaginaAtual
	 *            - página atual da paginação
	 * @author jesus
	 * @throws Exception
	 */
	public List consultarProcessosParalisadosConclusao(String id_Serventia, String id_ServentiaCargo, String periodo, String posicaoPaginaAtual) throws Exception {
		List listTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listTemp = obPersistencia.consultarProcessosParalisadosConclusoes(id_Serventia, id_ServentiaCargo, periodo, posicaoPaginaAtual);
			QuantidadePaginas = (Long) listTemp.get(listTemp.size() - 1);
			listTemp.remove(listTemp.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listTemp;
	}
	
	
	
	/**
	 * Método responsável por consultar os processos paralisados do 2 grau, podendo ser
	 * informado o Id da Serventia.
	 * 
	 * @param idServentia
	 *            - Id da Serventia
	 * @param idServentiaCargo
	 *            - Id da Serventia Cargo do Juiz Responsável
	 * @param posicaoPaginaAtual
	 *            - página atual da paginação
	 * @author jesus
	 * @throws Exception
	 */
	public List consultarProcessosParalisadosConclusaoSegundoGrau(String id_Serventia, String id_ServentiaCargo, String periodo, String posicaoPaginaAtual) throws Exception {
		List listTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listTemp = obPersistencia.consultarProcessosParalisadosConclusoesSegundoGrau(id_Serventia, id_ServentiaCargo, periodo, posicaoPaginaAtual);
			QuantidadePaginas = (Long) listTemp.get(listTemp.size() - 1);
			listTemp.remove(listTemp.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listTemp;
	}
	


	/**
	 * Método responsável por consultar a serventia a partir do id_serventia
	 * 
	 * @param idServentia
	 *            - Id da Serventia
	 * @author Márcio Gomes
	 * @throws Exception
	 */
	public ServentiaDt consultarServentia(String id_serventia) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		try {
			return serventiaNe.consultarId(id_serventia);
		} finally {
			serventiaNe = null;
		}
	}

	/**
	 * Método responsável por consultar a Area de Distribuição a partir do
	 * id_areaDistribuicao
	 * 
	 * @param id_areaDistribuicao
	 *            - Id da Area de Distribuicao
	 * @author Márcio Gomes
	 * @throws Exception
	 */
	public AreaDistribuicaoDt consultarAreaDistribuicao(String id_areaDistribuicao) throws Exception {
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		try {
			return areaDistribuicaoNe.consultarId(id_areaDistribuicao);
		} finally {
			areaDistribuicaoNe = null;
		}
	}

	/**
	 * Chama método que realizará a consulta
	 */
	public List consultarServentiaCargos(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		List tempList = null;
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		tempList = ServentiaCargone.consultarServentiaCargos(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		QuantidadePaginas = ServentiaCargone.getQuantidadePaginas();
		ServentiaCargone = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoGovernoTipo(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		GovernoTipoNe neObjeto = new GovernoTipoNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoEmpresaTipo(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;
		EmpresaTipoNe neObjeto = new EmpresaTipoNe();
		tempList = neObjeto.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	/**
	 * 
	 * Método que consulta os Processos aguardando audiência, podendo ser
	 * informado ou não o Id da Serventia.
	 * 
	 * @param idServentia
	 *            - Id da Serventia
	 * @param tipoAudiencia
	 *            - tipo de audiência selecionada
	 * @param opcaoPeriodo
	 *            - opção de tempo selecionada
	 * @param aPartirDa
	 *            - identifica se a busca será a partir de uma última audiência
	 *            ou não
	 * @param tipoAudienciaAnterior
	 *            - identifica qual será o tipo de audiência anterior
	 * @param posicaoPaginaAtual
	 *            - posição da paginação
	 * @return lista de processos aguardando audiência
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosAguardandoAudiencia(String idServentia, String idJuiz, String tipoAudiencia, String opcaoPeriodo, String aPartirDa, String tipoAudienciaAnterior, String posicaoPaginaAtual) throws Exception {
		List listTemp = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			if (tipoAudiencia.equals("999")) {
				// sem audiência
				listTemp = obPersistencia.consultarProcessosSemAudienciaDias(idServentia, idJuiz, opcaoPeriodo, posicaoPaginaAtual);
			} else if (aPartirDa != null && aPartirDa.equals("1")) {
				// com audiência atual, mas não tem audiência anterior
				listTemp = obPersistencia.consultarProcessosAudienciaMarcadaDias(idServentia, idJuiz, tipoAudiencia, opcaoPeriodo, posicaoPaginaAtual);
			} else if (aPartirDa != null && aPartirDa.equals("2")) {
				// com audiência atual e anterior selecionadas
				listTemp = obPersistencia.consultarProcessosAguardandoAudienciaAposOutraRealizada(idServentia, idJuiz, tipoAudiencia, opcaoPeriodo, tipoAudienciaAnterior, posicaoPaginaAtual);
			}
			QuantidadePaginas = (Long) listTemp.get(listTemp.size() - 1);
			listTemp.remove(listTemp.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listTemp;
	}


	/**
	 * Método para consultar a guia inicial paga para cadastrar o processo.
	 * 
	 * @param String
	 *            numeroCompletoGuiaInicial
	 * @return Map
	 * @throws Exception
	 */
	public Map consultarGuiaInicialPrimeiroGrau(String numeroCompletoGuiaInicial) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaEmissaoDt guiaEmissaoDt = null;
		Map partes = new HashMap();

		String numeroGuia = numeroCompletoGuiaInicial.replaceAll("[/.-]", "").trim();
		
		//valida número da guia
		if( Funcoes.isNumeroGuiaProjudiValido(numeroGuia) ) {
			
			guiaEmissaoDt = guiaEmissaoNe.consultarNumeroCompletoGuiaInicial(numeroGuia, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, null);
			
			if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null ) {
				
				//consulta codigo comarca
				if( guiaEmissaoDt.getId_Comarca() != null ) {
					ComarcaDt comarcaDt = new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca());
					
					if( comarcaDt != null ) {
						guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					}
				}
				
				//Verifica se é uma guia inicial
				if( guiaEmissaoNe.isGuiaInicial(guiaEmissaoDt.getId(), null) ) {
					
//					GuiaEmissaoDt guiaEmissaoInicialDtBase = guiaEmissaoNe.consultarGuiaInicialNaoComplementada(numeroGuia);
//					
//					if( guiaEmissaoInicialDtBase == null || (guiaEmissaoInicialDtBase.getId_GuiaEmissaoPrincipal() == null || guiaEmissaoInicialDtBase.getId_GuiaEmissaoPrincipal().isEmpty()) ) {
//						throw new MensagemException("Esta guia "+ Funcoes.FormatarNumeroSerieGuia(numeroGuia) +" já foi complementada por outra Guia. Por favor, informe a guia gerada para complementar esta ou outra guia inicial.");
//					}
					
					if (guiaEmissaoDt.getId_Processo() == null || guiaEmissaoDt.getId_Processo().trim().length() == 0 ) {
						if (!guiaEmissaoNe.verificarGuiaCanceladaIndependentePagamento(numeroGuia)) {
							
							String numeroProcessoSPGGuiaProjudi = new GuiaSPGNe().consultarNumeroProcessoSPGGuiaEmissaoInicialProjudi(numeroGuia);
							
							if (numeroProcessoSPGGuiaProjudi != null && numeroProcessoSPGGuiaProjudi.trim().length() > 0) {
								partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  encontrada, mas já foi utilizada para o cadastro do processo SPG número " + numeroProcessoSPGGuiaProjudi + "." );
							} else {
							
								if (!guiaEmissaoNe.isGuiaPaga(numeroGuia)) {
									partes.put("MENSAGEM", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que não impede o cadastro do processo.");
								}
								
								String partesRequerentes = guiaEmissaoDt.getRequerente();
								String partesRequeridos = guiaEmissaoDt.getRequerido();
								
								// Requerentes
								if (partesRequerentes != null) {
									List listaRequerentes = new ArrayList();
									ProcessoParteDt parteDt = new ProcessoParteDt();
									parteDt.setNome(partesRequerentes);
									parteDt.setCodigoTemp("GuiaInicial");
									parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_ATIVO));
									listaRequerentes.add(parteDt);
									guiaEmissaoDt.setListaRequerentes(listaRequerentes);
								}
								// Requeridos
								if (partesRequeridos != null) {
									List listaRequeridos = new ArrayList();
									ProcessoParteDt parteDt = new ProcessoParteDt();
									parteDt.setNome(partesRequeridos);
									parteDt.setCodigoTemp("GuiaInicial");
									parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_PASSIVO));
									listaRequeridos.add(parteDt);
									guiaEmissaoDt.setListaRequeridos(listaRequeridos);
								}
							}
						}
						else {
							partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  está cancelada.");
						}
					}
					else {
						partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  encontrada, mas já foi utilizada para o cadastro de outro processo.");
					}
				}
				else {
					partes.put("MENSAGEMERROR", "Esta Guia não é uma Guia Inicial!");
				}
			}
			else {
				GuiaEmissaoDt guiaEmissaoDtAuxiliar = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(numeroCompletoGuiaInicial);
				String mensagemAdicional = "";
				
				if( guiaEmissaoDtAuxiliar != null && guiaEmissaoDtAuxiliar.getId_Processo() != null && !guiaEmissaoDtAuxiliar.getId_Processo().isEmpty() ) {
					ProcessoDt processoDt = this.consultarId(guiaEmissaoDtAuxiliar.getId_Processo());
					if( processoDt != null ) {
						mensagemAdicional = "Guia já vinculada ao processo " + processoDt.getProcessoNumero() + ". ";
					}
					else {
						mensagemAdicional = "Guia já vinculada a um processo!";
					}
				}
				else {
					mensagemAdicional = "Guia não encontrada! ";
				}
				
				if( guiaEmissaoDtAuxiliar != null && guiaEmissaoDtAuxiliar.getGuiaModeloDt() != null && guiaEmissaoDtAuxiliar.getGuiaModeloDt().getId_GuiaTipo() != null && guiaEmissaoDtAuxiliar.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU) ) {
					mensagemAdicional = "A guia " + Funcoes.FormatarNumeroSerieGuia(numeroCompletoGuiaInicial) + " é de Segundo Grau, por favor, escolha a opção de cadastro de processo de segundo grau.";
				}
				
				partes.put("MENSAGEMERROR", mensagemAdicional);
			}			
		}
		else {			
			guiaEmissaoDt = new GuiaSPGNe().consultarGuiaEmissaoInicialSPG(numeroGuia, null);
			if (guiaEmissaoDt != null) {
				String partesRequerentes = guiaEmissaoDt.getRequerente();
				String partesRequeridos = guiaEmissaoDt.getRequerido();
				// Requerentes
				if (partesRequerentes != null) {
					List listaRequerentes = new ArrayList();
					ProcessoParteDt parteDt = new ProcessoParteDt();
					parteDt.setNome(partesRequerentes);
					parteDt.setCodigoTemp("GuiaInicial");
					parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_ATIVO));
					listaRequerentes.add(parteDt);
					guiaEmissaoDt.setListaRequerentes(listaRequerentes);
				}
				// Requeridos
				if (partesRequeridos != null) {
					List listaRequeridos = new ArrayList();
					ProcessoParteDt parteDt = new ProcessoParteDt();
					parteDt.setNome(partesRequeridos);
					parteDt.setCodigoTemp("GuiaInicial");
					parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_PASSIVO));
					listaRequeridos.add(parteDt);
					guiaEmissaoDt.setListaRequeridos(listaRequeridos);
				}
				guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
				if (!guiaEmissaoDt.getId_GuiaStatus().equals("3")) {
					partes.put("MENSAGEM", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que não impede o cadastro do processo.");
				}
			} else {
				partes.put("MENSAGEMERROR", "Número de Guia Inválido!");
			}
		}
		
		partes.put("GUIAEMISSAODT", guiaEmissaoDt);
		
		return partes;
	}
	
	/**
	 * Método para consultar a guia inicial paga para cadastrar o processo.
	 * 
	 * @param String
	 *            numeroCompletoGuiaInicial
	 * @return Map
	 * @throws Exception
	 */
	public Map consultarGuiaInicialSegundoGrau(String numeroCompletoGuiaInicial) throws Exception {
//		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
//		GuiaEmissaoDt guiaEmissaoDt = null;
//		Map partes = new HashMap();
//		String numeroGuia = numeroCompletoGuiaInicial.replaceAll("[/.-]", "").trim();
//		
//		guiaEmissaoDt = guiaEmissaoNe.consultarNumeroCompletoGuiaInicial(numeroGuia, String.valueOf(GuiaTipoDt.INICIAL_SEGUNDO_GRAU), null);
//		
//		if (guiaEmissaoDt != null) {
//			if (!guiaEmissaoNe.isGuiaPaga(numeroGuia)) {
//				partes.put("MENSAGEM", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que não impede o cadastro do processo.");
//			}
//			if (guiaEmissaoDt.getId_Processo() == null) {
//				if (!guiaEmissaoNe.verificarGuiaCanceladaIndependentePagamento(numeroGuia)) {
//					String partesRequerentes = guiaEmissaoDt.getRequerente();
//					String partesRequeridos = guiaEmissaoDt.getRequerido();
//					String partesOutras = guiaEmissaoDt.getOutrasPartes();
//					String partesAdvogado = guiaEmissaoDt.getAdvogado();
//					String assuntos = guiaEmissaoDt.getAssuntos();
//					// Leitura com JSON
//					JSONArray jsonArray = null;
//					// Requerentes
//					if (partesRequerentes != null) {
//						List listaRequerentes = new ArrayList();
//						ProcessoParteDt parteDt = new ProcessoParteDt();
//						parteDt.setNome(partesRequerentes);
//						parteDt.setCodigoTemp("GuiaInicial");
//						parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.idPromovente));
//						listaRequerentes.add(parteDt);
//						guiaEmissaoDt.setListaRequerentes(listaRequerentes);
//					}
//					// Requeridos
//					if (partesRequeridos != null) {
//						List listaRequeridos = new ArrayList();
//						ProcessoParteDt parteDt = new ProcessoParteDt();
//						parteDt.setNome(partesRequeridos);
//						parteDt.setCodigoTemp("GuiaInicial");
//						parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.idPromovido));
//						listaRequeridos.add(parteDt);
//						guiaEmissaoDt.setListaRequeridos(listaRequeridos);
//					}
//				} else {
//					partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  está cancelada.");
//				}
//			} else {
//				partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  encontrada, mas já foi utilizada para o cadastro de outro processo.");
//			}
//			
//		} else {
//			partes.put("MENSAGEMERROR", "Guia não encontrada!");
//		}
//		partes.put("GUIAEMISSAODT", guiaEmissaoDt);
//		return partes;
		
		
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		GuiaEmissaoDt guiaEmissaoDt = null;
		Map partes = new HashMap();

		String numeroGuia = numeroCompletoGuiaInicial.replaceAll("[/.-]", "").trim();
		
		//valida número da guia
		if( Funcoes.isNumeroGuiaProjudiValido(numeroGuia) ) {
			
			guiaEmissaoDt = guiaEmissaoNe.consultarNumeroCompletoGuiaInicial(numeroGuia, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, null);
			
			//guia turma recursal?
			if( guiaEmissaoDt == null ) {
				guiaEmissaoDt = guiaEmissaoNe.consultarNumeroCompletoGuiaInicial(numeroGuia, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, null);
				ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.MANDADO_SEGURANCA_CIVEL));
				if( guiaEmissaoDt != null && guiaEmissaoDt.getGuiaModeloDt() != null && guiaEmissaoDt.getGuiaModeloDt().getId_ProcessoTipo() != null && !guiaEmissaoDt.getGuiaModeloDt().getId_ProcessoTipo().equals(processoTipoDt.getId()) ) {
					guiaEmissaoDt = null;
				}
			}
			
			if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null ) {
				
				//consulta codigo comarca
				if( guiaEmissaoDt.getId_Comarca() != null ) {
					ComarcaDt comarcaDt = new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca());
					
					if( comarcaDt != null ) {
						guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					}
				}
				
				//Verifica se é uma guia inicial
				if( guiaEmissaoNe.isGuiaInicial(guiaEmissaoDt.getId(), null) ) {
					
//					GuiaEmissaoDt guiaEmissaoInicialDtBase = guiaEmissaoNe.consultarGuiaInicialNaoComplementada(numeroGuia);
//					
//					if( guiaEmissaoInicialDtBase == null || (guiaEmissaoInicialDtBase.getId_GuiaEmissaoPrincipal() == null || guiaEmissaoInicialDtBase.getId_GuiaEmissaoPrincipal().isEmpty()) ) {
//						throw new MensagemException("Esta guia "+ Funcoes.FormatarNumeroSerieGuia(numeroGuia) +" já foi complementada por outra Guia. Por favor, informe a guia gerada para complementar esta ou outra guia inicial.");
//					}
					
					if (guiaEmissaoDt.getId_Processo() == null || guiaEmissaoDt.getId_Processo().trim().length() == 0 ) {
						if (!guiaEmissaoNe.verificarGuiaCanceladaIndependentePagamento(numeroGuia)) {
							
							String numeroProcessoSSGGuiaProjudi = guiaSSGNe.consultarNumeroProcessoSSGGuiaEmissaoInicialProjudi(numeroGuia);
							
							if (numeroProcessoSSGGuiaProjudi != null && numeroProcessoSSGGuiaProjudi.trim().length() > 0) {
								partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  encontrada, mas já foi utilizada para o cadastro do processo SSG número " + numeroProcessoSSGGuiaProjudi + "." );
							} else {
							
								if (!guiaEmissaoNe.isGuiaPaga(numeroGuia)) {
									partes.put("MENSAGEM", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que não impede o cadastro do processo.");
								}
								
								String partesRequerentes = guiaEmissaoDt.getRequerente();
								String partesRequeridos = guiaEmissaoDt.getRequerido();
								
								// Requerentes
								if (partesRequerentes != null) {
									List listaRequerentes = new ArrayList();
									ProcessoParteDt parteDt = new ProcessoParteDt();
									parteDt.setNome(partesRequerentes);
									parteDt.setCodigoTemp("GuiaInicial");
									parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_ATIVO));
									listaRequerentes.add(parteDt);
									guiaEmissaoDt.setListaRequerentes(listaRequerentes);
								}
								// Requeridos
								if (partesRequeridos != null) {
									List listaRequeridos = new ArrayList();
									ProcessoParteDt parteDt = new ProcessoParteDt();
									parteDt.setNome(partesRequeridos);
									parteDt.setCodigoTemp("GuiaInicial");
									parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_PASSIVO));
									listaRequeridos.add(parteDt);
									guiaEmissaoDt.setListaRequeridos(listaRequeridos);
								}
							}
						}
						else {
							partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  está cancelada.");
						}
					}
					else {
						partes.put("MENSAGEMERROR", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + "</b>  encontrada, mas já foi utilizada para o cadastro de outro processo.");
					}
				}
				else {
					partes.put("MENSAGEMERROR", "Esta Guia não é uma Guia Inicial!");
				}
			}
			else {
				GuiaEmissaoDt guiaEmissaoDtAuxiliar = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(numeroCompletoGuiaInicial);
				String mensagemAdicional = "";
				
				if( guiaEmissaoDtAuxiliar != null && guiaEmissaoDtAuxiliar.getId_Processo() != null && !guiaEmissaoDtAuxiliar.getId_Processo().isEmpty() ) {
					ProcessoDt processoDt = this.consultarId(guiaEmissaoDtAuxiliar.getId_Processo());
					if( processoDt != null ) {
						mensagemAdicional = "Guia já vinculada ao processo " + processoDt.getProcessoNumero() + ". ";
					}
					else {
						mensagemAdicional = "Guia já vinculada a um processo!";
					}
				}
				else {
					mensagemAdicional = "Guia não encontrada! ";
				}
				
				if( guiaEmissaoDtAuxiliar != null && guiaEmissaoDtAuxiliar.getGuiaModeloDt() != null && guiaEmissaoDtAuxiliar.getGuiaModeloDt().getId_GuiaTipo() != null && guiaEmissaoDtAuxiliar.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
					mensagemAdicional += "A guia " + Funcoes.FormatarNumeroSerieGuia(numeroCompletoGuiaInicial) + " é de Primeiro Grau, por favor, escolha a opção de cadastro de processo de primeiro grau.";
				}
				
				partes.put("MENSAGEMERROR", mensagemAdicional);				
			}			
		}
		else {			
			guiaEmissaoDt = guiaSSGNe.consultarGuiaEmissaoSSG(numeroGuia);
			if (guiaEmissaoDt != null) {
				String partesRequerentes = guiaEmissaoDt.getRequerente();
				String partesRequeridos = guiaEmissaoDt.getRequerido();
				// Requerentes
				if (partesRequerentes != null) {
					List listaRequerentes = new ArrayList();
					ProcessoParteDt parteDt = new ProcessoParteDt();
					parteDt.setNome(partesRequerentes);
					parteDt.setCodigoTemp("GuiaInicial");
					parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_ATIVO));
					listaRequerentes.add(parteDt);
					guiaEmissaoDt.setListaRequerentes(listaRequerentes);
				}
				// Requeridos
				if (partesRequeridos != null) {
					List listaRequeridos = new ArrayList();
					ProcessoParteDt parteDt = new ProcessoParteDt();
					parteDt.setNome(partesRequeridos);
					parteDt.setCodigoTemp("GuiaInicial");
					parteDt.setId_ProcessoParteTipo(String.valueOf(ProcessoParteTipoDt.ID_POLO_PASSIVO));
					listaRequeridos.add(parteDt);
					guiaEmissaoDt.setListaRequeridos(listaRequeridos);
				}
				guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
				if (!guiaEmissaoDt.getId_GuiaStatus().equals("3")) {
					partes.put("MENSAGEM", "Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que não impede o cadastro do processo.");
				}
			} else {
				partes.put("MENSAGEMERROR", "Número de Guia Inválido!");
			}
		}
		
		partes.put("GUIAEMISSAODT", guiaEmissaoDt);
		
		return partes;
	}

	/**
	 * Método para consultar processo simplificado para auxilio no calculo de
	 * locomoção.
	 * 
	 * @param String
	 *            idProcesso
	 * @return ProcessoCompletoDt
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoSimplificado(String idProcesso) throws Exception {
		ProcessoDt processoDt = null;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			processoDt = obPersistencia.consultarProcessoSimplificado(idProcesso);
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return processoDt;
	}
	
	/**
	 * Método para consultar processo simplificado Para salvar Mandado no SPG.
	 * @param String idProcesso
	 * @return ProcessoDt
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoSimplificadoMandadoSPG(String idProcesso) throws Exception {
		ProcessoDt processoDt = null;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			processoDt = obPersistencia.consultarProcessoSimplificadoMandadoSPG(idProcesso);
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return processoDt;
	}

	/**
	 * Método para vincular a guia inicial no processo no momento do cadastro.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idProcesso
	 * @param String processoNumeroAntigoTemp
	 * @param String idServentia
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void vinculaGuiaProcesso(GuiaEmissaoDt guiaEmissaoDt, String idProcesso, String processoNumeroAntigoTemp, String idServentia, String idUsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao, String motivoVinculacao) throws Exception {
		
		//Salva log de acesso nessa funcionalidade para rastrear comportamento das guias
		LogDt obLogDt = new LogDt("VINCULAR_GUIA_ACESSADO", idProcesso, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Informacao), "", "[Funcionalidade de vincular guia acessado para este processo;"+ guiaEmissaoDt.getPropriedades() +"]");
		obLog.salvar(obLogDt, obFabricaConexao);
		
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		
		String idComarca = null;
		//Variável utilizada para setar o ambiente CAPTIAL OU REMOTO do SPG de acordo com a guia consultada, se ela foi consultada no REMOTO então os procedimentos serão realizados no REMOTO e não com base no código da comarca que veio da guia.
		String codigoComarcaParaAmbienteREMOTO_CAPITAL = null;
		
		ServentiaDt serventiaDt = new ServentiaNe().consultarId(idServentia, obFabricaConexao);
		if (serventiaDt != null) 
			idComarca = serventiaDt.getId_Comarca();
		
		if (!guiaEmissaoDt.isGuiaEmitidaSPG()) {
			
			boolean isGuiaVinculadaProcesso = guiaEmissaoNe.vinculaGuiaProcesso(guiaEmissaoDt.getId(), guiaEmissaoDt.getNumeroGuiaCompleto(), idProcesso, idServentia, idComarca, idUsuarioLog, ipComputadorLog, obFabricaConexao, motivoVinculacao);
			
			if( !isGuiaVinculadaProcesso ) {
				throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo.");
			}
		}
		
		cadastrarInfoRepasseNoCadastroProcesso(guiaEmissaoDt, idProcesso, processoNumeroAntigoTemp, idServentia, idUsuarioLog, ipComputadorLog, obFabricaConexao, idComarca, true);
	}

	protected void cadastrarInfoRepasseNoCadastroProcesso(GuiaEmissaoDt guiaEmissaoDt, String idProcesso, String processoNumeroAntigoTemp, String idServentia, String idUsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao, String idComarca, boolean executarCodigoVincularGuiaSPG_SSG) throws Exception {
		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		ServentiaNe serventiaNe = new ServentiaNe();
		
		String codigoComarcaParaAmbienteREMOTO_CAPITAL;
		
		//Cadastra na V_SPGAGUIAS_INFO_REPASSE 
		if( guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0 && idProcesso != null && idServentia != null ) {
			
			//Consulta a guia no spg na base capital 
			GuiaEmissaoDt guiaSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoCapital(guiaEmissaoDt.getNumeroGuiaCompleto());
			codigoComarcaParaAmbienteREMOTO_CAPITAL = ComarcaDt.GOIANIA;
			
			//Caso não exista consulta a guia no spg na base interior
			if (guiaSPG == null) {
				guiaSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
				codigoComarcaParaAmbienteREMOTO_CAPITAL = ComarcaDt.APARECIDA_DE_GOIANIA;
			}
			
			//Guia existe no SPG?
			if( guiaSPG != null && guiaSPG.getId() != null && guiaSPG.getId().length() > 0 ) {
				
				//Consulta se número processo projudi presente na guia no SPG existe
				if( guiaSPG.getNumeroProcesso() != null && !guiaSPG.getNumeroProcesso().isEmpty() ) {
					String numero[] = Funcoes.separaNumeroProcessoDigitoAno(guiaSPG.getNumeroProcesso());
					if( numero != null ) {
						String numeroProcessoComPonto = "";
						for( int i = 0; i < numero.length; i++ ) {
							numeroProcessoComPonto += numero[i] + ".";
						}
						numeroProcessoComPonto += ".";
						numeroProcessoComPonto = numeroProcessoComPonto.replace("..", "");
						
						if( executarCodigoVincularGuiaSPG_SSG ) {
							ProcessoDt processoDt = this.consultarProcessoNumeroCompletoDigitoAno(numeroProcessoComPonto, obFabricaConexao);
							if( processoDt != null ) {
								throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo. A guia está no SPG/SSG vinculada ao processo " + processoDt.getProcessoNumeroCompleto());
							}
							//Processo não existe, então apago os dados da guia no SPG para ser utilizada
							else {
								guiaSPGNe.limpaGuiaVinculadaProcesso(guiaSPG.getId(), guiaSPG.getNumeroProcesso(), guiaEmissaoDt.getComarcaCodigo(), idUsuarioLog, ipComputadorLog);
							}
						}
					}
				}
				
				//Número guia no spg é igual mesmo?
				if( guiaSPG.getNumeroGuiaCompleto() != null && Funcoes.StringToInt(guiaSPG.getNumeroGuiaCompleto()) == Funcoes.StringToInt(guiaEmissaoDt.getNumeroGuiaCompleto()) ) {
					
					if( idServentia != null && idServentia.trim().length() > 0 ) {
					
						//**************************************
						//1 - Atualiza dados da guia V_SPGAGUIAS
						ServentiaDt servDt = new ServentiaNe().consultarId(idServentia);
						
						if( servDt == null || !servDt.temCodigoExterno() ) {
							throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. O código da serventia do SPG não encontrado.(1)");
						}
							
						String codigoServentia = Funcoes.completarZeros(servDt.getServentiaCodigoExterno(),3);
						
						if( executarCodigoVincularGuiaSPG_SSG ) {
							
							String numrProcesso = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
							
							boolean isGuiaVinculadaProcessoSPG = guiaSPGNe.atualizaGuiaVinculadaProcesso(guiaSPG.getId(), numrProcesso, processoNumeroAntigoTemp, codigoServentia, codigoComarcaParaAmbienteREMOTO_CAPITAL, idUsuarioLog, ipComputadorLog);
							if( !isGuiaVinculadaProcessoSPG ) {
								throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG.");
							}
						}
						
						//**************************************
						//2 - Verifica se a guia do SPG está no V_SPGAGUIAS_INFO_REPASSE
						String isnInfoRepasse = guiaSPGNe.consultarISNInfoRepasseByISNGuia(guiaSPG.getId(), codigoComarcaParaAmbienteREMOTO_CAPITAL);
						
						//Guia paga? se sim, coloca a data(feito sob supervisão do júnior feitosa)
						TJDataHora dataRepasse = null;
						// Consulta na base do SAJA para verificar se a guia foi paga...
						if(guiaSPGNe.consultarDataDePagamentoGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) != null) {
							//coloca a data de hoje a pedido do júnior feitosa
							dataRepasse = new TJDataHora();
						}
						
						//Não existe, então cadastra
						if( isnInfoRepasse == null || ( isnInfoRepasse != null && isnInfoRepasse.trim().length() == 0) ) {
							//Faz insert
							guiaSPGNe.inserirGuiaInfoRepasse(guiaSPG.getId(), codigoServentia, dataRepasse, codigoComarcaParaAmbienteREMOTO_CAPITAL, guiaSPG.getNumeroGuiaCompleto(), idUsuarioLog, ipComputadorLog);
						}
						//Existe, atualiza
						else {
	
							//boolean infoRepasseAtualizado = guiaSPGNe.atualizaGuiaInfoRepasse(guiaSPG.getId(), codigoServentia, codigoComarcaParaAmbienteREMOTO_CAPITAL, idUsuarioLog, ipComputadorLog);
	
							
							//Não atualizou nada então registra o log para monitorar o que está acontecendo
							/*if( !infoRepasseAtualizado ) {
								
							}*/
						}
						
						//Só vincula se não for uma guia complementar
						if (!guiaEmissaoDt.isGuiaComplementarSPG()) {
							//Vincula a guia inicial ao processo
							ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());					
							obPersistencia.vincularGuiaInicialAoProcesso(idProcesso, guiaEmissaoDt.getId(), guiaEmissaoDt.isGuiaEmitidaSPG());	
						}
						
						//Atualiza data de apresentação no SPG para que a rotina da financeira atualize o repasse corretamente.
						guiaSPGNe.atualizaDataApresentacao(guiaSPG.getId(), codigoComarcaParaAmbienteREMOTO_CAPITAL);
	
	//TODO Fred
	//Comentado para evitar erros que até o momento não consegui mapear
						if (guiaEmissaoDt.isGuiaEmitidaSPG()) {
							//Insere no Projudi uma guia do SPG...
							guiaEmissaoNe.salvarGuiaSPGNoProjudi(guiaEmissaoDt.getNumeroGuiaCompleto(), guiaEmissaoDt.isGuiaComplementarSPG(), guiaEmissaoDt.getId_ProcessoTipo(), idProcesso, idComarca, idUsuarioLog, ipComputadorLog, obFabricaConexao);						
						}
					}
					
				} else {
					throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. A serventia não foi encontrada.");
				}
			}
			else {
				if( executarCodigoVincularGuiaSPG_SSG ) {
					//Consulta a guia no spg na base capital 
					GuiaEmissaoDt guiaSSG = guiaSSGNe.consultarGuiaEmissaoSSG(guiaEmissaoDt.getNumeroGuiaCompleto());
					
					//Guia existe no SSG?
					if( guiaSSG != null && guiaSSG.getId() != null && guiaSSG.getId().length() > 0 ) {
						
						//Consulta se número processo projudi presente na guia no SSG existe
						if( guiaSSG.getNumeroProcesso() != null && !guiaSSG.getNumeroProcesso().isEmpty() ) {
							String numero[] = Funcoes.separaNumeroProcessoDigitoAno(guiaSSG.getNumeroProcesso());
							if( numero != null ) {
								String numeroProcessoComPonto = "";
								for( int i = 0; i < numero.length; i++ ) {
									numeroProcessoComPonto += numero[i] + ".";
								}
								numeroProcessoComPonto += ".";
								numeroProcessoComPonto = numeroProcessoComPonto.replace("..", "");
								
								ProcessoDt processoDt = this.consultarProcessoNumeroCompletoDigitoAno(numeroProcessoComPonto);
								if( processoDt != null ) {
									throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo. A guia está no SPG/SSG vinculada ao processo " + processoDt.getProcessoNumeroCompleto());
								}
								
							}
						}
						
						//Número guia no ssg é igual mesmo?
						if( guiaSSG.getNumeroGuiaCompleto() != null && Funcoes.StringToInt(guiaSSG.getNumeroGuiaCompleto()) == Funcoes.StringToInt(guiaEmissaoDt.getNumeroGuiaCompleto()) && idServentia != null && idServentia.trim().length() > 0 ) {
							
							
							//**************************************
							//1 - Atualiza dados da guia V_SPGAGUIAS
							ServentiaDt servDt = serventiaNe.consultarId(idServentia);
							
							if( servDt == null || !servDt.temCodigoExterno() ) {
								throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. O código da serventia do SPG não encontrado.(2)");
							}
								
							String codigoServentia = Funcoes.completarZeros(servDt.getServentiaCodigoExterno(),3);
							
							String numrProcesso = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
							
							boolean isGuiaVinculadaProcessoSSG = guiaSSGNe.atualizaGuiaVinculadaProcesso(guiaSSG.getId(), numrProcesso, processoNumeroAntigoTemp, codigoServentia, servDt.getComarcaCodigo(), idUsuarioLog, ipComputadorLog);
							if( !isGuiaVinculadaProcessoSSG ) {
								throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SSG.");
							}
							
							//**************************************
							//2 - Verifica se a guia do SSG está no V_SPGAGUIAS_INFO_REPASSE
							//
							
							//Só vincula se não for uma guia complementar
							if (!guiaEmissaoDt.isGuiaComplementarSSG()) {
								//Vincula a guia inicial ao processo
								ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());					
								obPersistencia.vincularGuiaInicialAoProcesso(idProcesso, guiaEmissaoDt.getId(), guiaEmissaoDt.isGuiaEmitidaSPG());	
							}
							
							//Atualiza data de apresentação no SPG para que a rotina da financeira atualize o repasse corretamente.
							//guiaSPGNe.atualizaDataApresentacao(guiaSPG.getId(), codigoComarcaParaAmbienteREMOTO_CAPITAL);
	
							if (guiaEmissaoDt.isGuiaEmitidaSSG()) {
								//Insere no Projudi uma guia do SSG...
								guiaEmissaoNe.salvarGuiaSSGNoProjudi(guiaEmissaoDt.getNumeroGuiaCompleto(), guiaEmissaoDt.isGuiaComplementarSPG(), guiaEmissaoDt.getId_ProcessoTipo(), idProcesso, idComarca, idUsuarioLog, ipComputadorLog, obFabricaConexao);						
							}
							
						}
						else {
							throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SSG. A serventia não foi encontrada.");
						}								
						
					}
					else {
						throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SSG. A guia não foi encontrada.");
					}
				}
			}
		}
	}
	
	/**
	 * Método para vincular a guia inicial do SSG no processo PJD.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idProcesso
	 * @param String processoNumeroAntigoTemp
	 * @param String idServentia
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void vinculaGuiaSSGProcesso(GuiaEmissaoDt guiaEmissaoDt, String idProcesso, String processoNumeroAntigoTemp, String idServentia, String idUsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao, String motivoVinculacao) throws Exception {
		
		//Salva log de acesso nessa funcionalidade para rastrear comportamento das guias
		LogDt obLogDt = new LogDt("VINCULAR_GUIA_ACESSADO", idProcesso, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Informacao), "", "[Funcionalidade de vincular guia acessado para este processo;"+ guiaEmissaoDt.getPropriedades() +"]");
		obLog.salvar(obLogDt, obFabricaConexao);
		
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		ServentiaNe serventiaNe = new ServentiaNe();
		String idComarca = null;
		
		//Variável utilizada para setar o ambiente CAPTIAL OU REMOTO do SPG de acordo com a guia consultada, se ela foi consultada no REMOTO então os procedimentos serão realizados no REMOTO e não com base no código da comarca que veio da guia.
		//String codigoComarcaParaAmbienteREMOTO_CAPITAL = null;
		
		ServentiaDt serventiaDt = serventiaNe.consultarId(idServentia, obFabricaConexao);
		if (serventiaDt != null) 
			idComarca = serventiaDt.getId_Comarca();
		
		if (!guiaEmissaoDt.isGuiaEmitidaSSG()) {
			
			boolean isGuiaVinculadaProcesso = guiaEmissaoNe.vinculaGuiaProcesso(guiaEmissaoDt.getId(), guiaEmissaoDt.getNumeroGuiaCompleto(), idProcesso, idServentia, idComarca, idUsuarioLog, ipComputadorLog, obFabricaConexao, motivoVinculacao);
			
			if( !isGuiaVinculadaProcesso ) {
				throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo.");
			}
		}
		
		if( guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0 ) {
			
			//Consulta a guia no ssg
			GuiaEmissaoDt guiaSSG = guiaSSGNe.consultarGuiaEmissaoSSG(guiaEmissaoDt.getNumeroGuiaCompleto());
			
			//Guia existe no SSG?
			if( guiaSSG != null && guiaSSG.getId() != null && guiaSSG.getId().length() > 0 ) {
				
				//Consulta se número processo projudi presente na guia no SSG existe
				if( guiaSSG.getNumeroProcesso() != null && !guiaSSG.getNumeroProcesso().isEmpty() ) {
					String numero[] = Funcoes.separaNumeroProcessoDigitoAno(guiaSSG.getNumeroProcesso());
					if( numero != null ) {
						String numeroProcessoComPonto = "";
						for( int i = 0; i < numero.length; i++ ) {
							numeroProcessoComPonto += numero[i] + ".";
						}
						numeroProcessoComPonto += ".";
						numeroProcessoComPonto = numeroProcessoComPonto.replace("..", "");
						
						ProcessoDt processoDt = this.consultarProcessoNumeroCompletoDigitoAno(numeroProcessoComPonto);
						if( processoDt != null ) {
							throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo. A guia está no SPG/SSG vinculada ao processo " + processoDt.getProcessoNumeroCompleto());
						}
						
					}
				}
				
				//Número guia no spg é igual mesmo?
				if( guiaSSG.getNumeroGuiaCompleto() != null && guiaSSG.getNumeroGuiaCompleto().equals(guiaEmissaoDt.getNumeroGuiaCompleto()) && idServentia != null && idServentia.trim().length() > 0 ) {
					
					//**************************************
					//1 - Atualiza dados da guia V_SPGAGUIAS
					ServentiaDt servDt = serventiaNe.consultarId(idServentia);
					
					if( servDt == null || !servDt.temCodigoExterno() ) {
						throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. O código da serventia do SPG não encontrado.(3)");
					}
						
					String codigoServentia = Funcoes.completarZeros(servDt.getServentiaCodigoExterno(),3);
					
					String numrProcesso = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
					
					boolean isGuiaVinculadaProcessoSSG = guiaSSGNe.atualizaGuiaVinculadaProcesso(guiaSSG.getId(), numrProcesso, processoNumeroAntigoTemp, codigoServentia, servDt.getComarcaCodigo(), idUsuarioLog, ipComputadorLog);
					if( !isGuiaVinculadaProcessoSSG ) {
						throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SSG.");
					}
					
					//**************************************
					//2 - Verifica se a guia do SPG está no V_SPGAGUIAS_INFO_REPASSE
					//
					
					//Só vincula se não for uma guia complementar
					if (!guiaEmissaoDt.isGuiaComplementarSSG()) {
						//Vincula a guia inicial ao processo
						ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());					
						obPersistencia.vincularGuiaInicialAoProcesso(idProcesso, guiaEmissaoDt.getId(), guiaEmissaoDt.isGuiaEmitidaSSG());	
					}
					
					//Atualiza data de apresentação no SPG para que a rotina da financeira atualize o repasse corretamente.
					//guiaSPGNe.atualizaDataApresentacao(guiaSPG.getId(), codigoComarcaParaAmbienteREMOTO_CAPITAL);

					if (guiaEmissaoDt.isGuiaEmitidaSSG()) {
						//Insere no Projudi uma guia do SSG...
						guiaEmissaoNe.salvarGuiaSSGNoProjudi(guiaEmissaoDt.getNumeroGuiaCompleto(), guiaEmissaoDt.isGuiaComplementarSSG(), guiaEmissaoDt.getId_ProcessoTipo(), idProcesso, idComarca, idUsuarioLog, ipComputadorLog, obFabricaConexao);						
					}
					
				}
				else {
					throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SSG. A serventia não foi encontrada.");
				}								
				
			}
			else {
				throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SSG. A guia não foi encontrada.");
			}
		}		
	}
	
	/**
	 * Método para vincular a guia de locomoção do SPG no processo do PJD.
	 * Este método foi criado a partir da cópia do método "vinculaGuiaProcesso" acima.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idProcesso
	 * @param String processoNumeroAntigoTemp
	 * @param String idServentia
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void vinculaGuiaLocomocaoProcesso(GuiaEmissaoDt guiaEmissaoDt, String idProcesso, String processoNumeroAntigoTemp, String idServentia, String idUsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao) throws Exception {
		
		//Salva log de acesso nessa funcionalidade para rastrear comportamento das guias
		LogDt obLogDt = new LogDt("VINCULAR_GUIA_ACESSADO", idProcesso, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Informacao), "", "[Funcionalidade de vincular guia acessado para este processo;"+ guiaEmissaoDt.getPropriedades() +"]");
		obLog.salvar(obLogDt, obFabricaConexao);
		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		String idComarca = null;
		//Variaável utilizada para setar o ambiente CAPTIAL OU REMOTO do SPG de acordo com a guia consultada, se ela foi consultada no REMOTO então os procedimentos serão realizados no REMOTO e não com base no código da comarca que veio da guia.
		String codigoComarcaParaAmbienteREMOTO_CAPITAL = null;
		
		ServentiaDt serventiaDt = new ServentiaNe().consultarId(idServentia, obFabricaConexao);
		if (serventiaDt != null) 
			idComarca = serventiaDt.getId_Comarca();
		
		//Cadastra na V_SPGAGUIAS_INFO_REPASSE 
		if( guiaEmissaoDt.getNumeroGuiaCompleto() != null && guiaEmissaoDt.getNumeroGuiaCompleto().length() > 0 ) {
			
			//Consulta a guia no spg na base capital 
			GuiaEmissaoDt guiaSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoCapital(guiaEmissaoDt.getNumeroGuiaCompleto());
			codigoComarcaParaAmbienteREMOTO_CAPITAL = ComarcaDt.GOIANIA;
			
			//Caso não exista consulta a guia no spg na base interior
			if (guiaSPG == null) {
				guiaSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
				codigoComarcaParaAmbienteREMOTO_CAPITAL = ComarcaDt.APARECIDA_DE_GOIANIA;
			}
			
			//Guia existe no SPG?
			if( guiaSPG != null && guiaSPG.getId() != null && guiaSPG.getId().length() > 0 ) {
				
				//Consulta se número processo projudi presente na guia no SPG existe
				if( guiaSPG.getNumeroProcesso() != null && !guiaSPG.getNumeroProcesso().isEmpty() ) {
					String numero[] = Funcoes.separaNumeroProcessoDigitoAno(guiaSPG.getNumeroProcesso());
					if( numero != null ) {
						String numeroProcessoComPonto = "";
						for( int i = 0; i < numero.length; i++ ) {
							numeroProcessoComPonto += numero[i] + ".";
						}
						numeroProcessoComPonto += ".";
						numeroProcessoComPonto = numeroProcessoComPonto.replace("..", "");
						
						ProcessoDt processoDt = this.consultarProcessoNumeroCompletoDigitoAno(numeroProcessoComPonto);
						if( processoDt != null ) {
							throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo. A guia está no SPG/SSG vinculada ao processo " + processoDt.getProcessoNumeroCompleto());
						}
						//Processo não existe, então apago os dados da guia no SPG para ser utilizada
						else {
							guiaSPGNe.limpaGuiaVinculadaProcesso(guiaSPG.getId(), guiaSPG.getNumeroProcesso(), guiaEmissaoDt.getComarcaCodigo(), idUsuarioLog, ipComputadorLog);
						}
						
					}
				}
				
				//Número guia no spg é igual mesmo?
				if( guiaSPG.getNumeroGuiaCompleto() != null && guiaSPG.getNumeroGuiaCompleto().equals(guiaEmissaoDt.getNumeroGuiaCompleto()) && idServentia != null && idServentia.trim().length() > 0 ) {
					
					//**************************************
					//1 - Atualiza dados da guia V_SPGAGUIAS
					ServentiaDt servDt = new ServentiaNe().consultarId(idServentia);
					
					if( servDt == null || !servDt.temCodigoExterno() ) {
						throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. O código da serventia do SPG não encontrado.(4)");
					}
						
					String codigoServentia = Funcoes.completarZeros(servDt.getServentiaCodigoExterno(),3);
					
					String numrProcesso = Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(guiaEmissaoDt.getNumeroProcesso()), 21).trim().substring(0, 14);
					
					boolean isGuiaVinculadaProcessoSPG = guiaSPGNe.atualizaGuiaVinculadaProcesso(guiaSPG.getId(), numrProcesso, processoNumeroAntigoTemp, codigoServentia, codigoComarcaParaAmbienteREMOTO_CAPITAL, idUsuarioLog, ipComputadorLog);
					if( !isGuiaVinculadaProcessoSPG ) {
						throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG.");
					}
					
					//Guia paga? se sim, coloca a data(feito sob supervisão do júnior feitosa)
					TJDataHora dataRepasse = null;
					// Consulta na base do SAJA para verificar se a guia foi paga...
					if(guiaSPGNe.consultarDataDePagamentoGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) != null) {
						guiaSPGNe.atualizaDataApresentacao(guiaSPG.getId(), codigoComarcaParaAmbienteREMOTO_CAPITAL);
					}
					
				} else {
					throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. A serventia não foi encontrada.");
				}								
				
			} else {
				throw new MensagemException("Erro ao vincular a guia número " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + " ao processo no SPG. A guia não foi encontrada.");
			}
		}		
	}
	
	public void vincularGuiaInicialAoProcesso(String idProcesso, String idGuia, boolean isGuiaSPG, FabricaConexao obFabricaConexao) throws Exception {
		//Vincula a guia inicial ao processo
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());					
		obPersistencia.vincularGuiaInicialAoProcesso(idProcesso, idGuia, isGuiaSPG);	
	}

	/**
	 * Método que consulta id da Serventia do Processo.
	 * 
	 * @param String
	 *            idProcesso
	 * @return String idServentia
	 * @throws Exception
	 */
	public String consultarIdServentiaProcesso(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		String idServentia = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		idServentia = obPersistencia.consultarIdServentiaProcesso(idProcesso); 
		return idServentia;
	}

	public long consultarQuantidadeProcessosAdvogado(CertidaoPraticaForenseDt certidaoDt) throws Exception {
		long quantidade = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			quantidade = obPersistencia.consultarQuantidadeProcessosAdvogado(certidaoDt.getOabNumero(), certidaoDt.getOabComplemento(), certidaoDt.getOabUfCodigo(), certidaoDt.getDataTimeInicial(), certidaoDt.getDataTimeFinal());
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return quantidade;
	}
	
	public int consultarQuantidadeProcessosProjudiPraticaForenseAdvogado(String oabNumero, String oabComplemento, String id_estadoOABUF, String dataInicial, String dataFinal, String id_Comarca, String id_areaServentiaLogada) throws Exception {
		int quantidade = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			quantidade = obPersistencia.consultarQuantidadeProcessoPraticaForenseAdvogadoPF(oabNumero, oabComplemento, id_estadoOABUF, dataInicial, dataFinal, id_Comarca, id_areaServentiaLogada);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return quantidade;
	}

	public List consultarProcessoPraticaForenseAdvogado(CertidaoPraticaForenseDt certidaoDt) throws Exception {
		List listaProcesso = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcesso = obPersistencia.consultarProcessoPraticaForenseAdvogado(certidaoDt.getOabNumero(), certidaoDt.getOabComplemento(), certidaoDt.getOabUfCodigo(), certidaoDt.getDataTimeInicial(), certidaoDt.getDataTimeFinal());
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return listaProcesso;
	}
	
	public List<ProcessoCertidaoPraticaForenseDt> consultarProcessosProjudiPraticaForenseAdvogado(String oabNumero, String oabComplemento, String id_estadoOABUF, String dataInicial, String dataFinal, String id_Comarca, String id_areaServentiaLogada) throws Exception {
		List listaProcesso = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcesso = obPersistencia.consultarProcessoPraticaForenseAdvogadoPF(oabNumero, oabComplemento, id_estadoOABUF, dataInicial, dataFinal, id_Comarca, id_areaServentiaLogada);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return listaProcesso;
	}

	public CertidaoExecucaoCPCDt getProcessoExecucaoCPC(String numero) throws Exception {
		CertidaoExecucaoCPCDt cepcp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			cepcp = obPersistencia.consultarProcessoExecucaoCPC(numero);
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return cepcp;
	}

	public String salvarProcesso(ProcessoCadastroDt processoDt) throws Exception {

		String conteudo;

		// LEIAUTE
		conteudo = "0@;#5@;#\n";
		// PARTES
		conteudo = lerPartes(processoDt.getListaPolosAtivos(), conteudo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, "");
		conteudo = lerPartes(processoDt.getListaPolosPassivos(), conteudo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, "");
		conteudo = lerPartes(processoDt.getListaOutrasPartes(), conteudo, -1000, "");
		
		// ADVOGADO PROMOVENTE
		if (processoDt.getListaAdvogados() != null) {
			if (!processoDt.getListaAdvogados().isEmpty()) {
				for (Iterator iterator2 = processoDt.getListaAdvogados().iterator(); iterator2.hasNext();) {
					UsuarioServentiaOabDt usuarioServentiaOabDt = (UsuarioServentiaOabDt) iterator2.next();
					conteudo += "12@;#";
					conteudo += usuarioServentiaOabDt.getId_UsuarioServentia() + "@;#";
					conteudo += usuarioServentiaOabDt.getId() + "@;#";
					conteudo += usuarioServentiaOabDt.getNomeUsuario() + "@;#";
					conteudo += usuarioServentiaOabDt.getOabComplemento() + "@;#";
					conteudo += usuarioServentiaOabDt.getOabNumero() + "@;#";
					conteudo += "\n";
				}
			}
		}
		// ASSUNTO
		if (processoDt.getListaAssuntos() != null) {
			for (Iterator iterator = processoDt.getListaAssuntos().iterator(); iterator.hasNext();) {
				ProcessoAssuntoDt assuntoDt = (ProcessoAssuntoDt) iterator.next();
				conteudo += "6@;#";
				conteudo += assuntoDt.getAssunto() + "@;#";
				if (assuntoDt.getCodigoTemp() != null) conteudo += assuntoDt.getCodigoTemp() + "@;#";
				else
					conteudo += "@;#";
				conteudo += assuntoDt.getId_Assunto() + "@;#";
				conteudo += assuntoDt.getId_Processo() + "@;#";
				conteudo += assuntoDt.getId() + "@;#";
				conteudo += assuntoDt.getProcessoNumero() + "@;#";
				conteudo += "\n";
			}
		}
		
		if(processoDt.getProcessoDependenteDt() != null) {
			
			//PARTES PROCESSO DEPENDENTE
			conteudo = lerPartes(processoDt.getProcessoDependenteDt().getListaPolosAtivos(), conteudo, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, "7");
			conteudo = lerPartes(processoDt.getProcessoDependenteDt().getListaPolosPassivos(), conteudo, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, "7");
			conteudo = lerPartes(processoDt.getProcessoDependenteDt().getListaOutrasPartes(), conteudo, -1000, "7");
			
			// INFORMAÇÕES PROCESSO DEPENDENTE
			conteudo += "78@;#";
			conteudo += processoDt.getProcessoDependenteDt().getAno() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getApenso() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getArea() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getAreaCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getAreaDistribuicao() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getClassificador() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getCodigoTemp() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getDataArquivamento() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getDigitoVerificador() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getExistePeticaoPendente() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getForumCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_Area() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_AreaDistribuicao() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_Classificador() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ObjetoPedido() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_Processo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ProcessoPrincipal() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ProcessoFase() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ProcessoPrioridade() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ProcessoStatus() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ProcessoTipo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_Recurso() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_Serventia() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getId_ServentiaOrigem() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getObjetoPedido() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getObjetoPedidoCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoDiretorio() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoFase() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoFaseCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoNumeroSimples() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoNumeroPrincipal() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoPrioridade() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoPrioridadeCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoStatus() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoStatusCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoTipo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getProcessoTipoCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getSegredoJustica() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getServentia() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getServentiaCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getServentiaOrigem() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getServentiaOrigemCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getServentiaTipoCodigo() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getTcoNumero() + "@;#";
			conteudo += processoDt.getProcessoDependenteDt().getValor() + "@;#";
			conteudo += "\n";
		}
		
		// INFORMAÇÕES
		conteudo += "8@;#";
		conteudo += processoDt.getProcessoTipo() + "@;#";
		conteudo += processoDt.getId_ProcessoTipo() + "@;#";
		conteudo += processoDt.getProcessoTipoCodigo() + "@;#";
		conteudo += processoDt.getAreaCodigo() + "@;#";
		conteudo += processoDt.getAreaDistribuicao() + "@;#";
		conteudo += processoDt.getAreaDistribuicaoCodigo() + "@;#";
		conteudo += processoDt.getId_AreaDistribuicao() + "@;#";
		conteudo += processoDt.getComarca() + "@;#";
		conteudo += processoDt.getComarcaCodigo() + "@;#";
		conteudo += processoDt.getId_Comarca() + "@;#";
		conteudo += processoDt.getForumCodigo() + "@;#";
		conteudo += processoDt.getProcessoPrioridade() + "@;#";
		conteudo += processoDt.getProcessoPrioridadeCodigo() + "@;#";
		conteudo += processoDt.getId_ProcessoPrioridade() + "@;#";
		conteudo += processoDt.getValor() + "@;#";
		conteudo += processoDt.getProcessoNumero() + "@;#";
		conteudo += processoDt.getProcessoNumeroPrincipal() + "@;#";
		conteudo += processoDt.getProcessoNumeroFisico() + "@;#";
		conteudo += processoDt.getServentiaCargo() + "@;#";
		conteudo += processoDt.getServentia() + "@;#";
		conteudo += processoDt.getTcoNumero() + "@;#";
		conteudo += (processoDt.isProcessoDependente() == true ? "true": "false") + "@;#";
		conteudo += (processoDt.isProcessoFisico() == true ? "true": "false") + "@;#";
		
		//Variáveis da tela de processo comum (radios)
		conteudo += processoDt.getGrauProcesso() + "@;#";
		conteudo += processoDt.getTipoProcesso() + "@;#";
		conteudo += processoDt.getAssistenciaProcesso() + "@;#";
		conteudo += processoDt.getDependenciaProcesso() + "@;#";
		
		conteudo += "\n";

		// SUMARIZAÇÃO
		conteudo += "9@;#";
		if (processoDt.getListaPolosAtivos() != null) conteudo += processoDt.getListaPolosAtivos().size() + "@;#";	else	conteudo += "0@;#";
		
		if (processoDt.getListaAdvogados() != null) conteudo += processoDt.getListaAdvogados().size() + "@;#";		else	conteudo += "0@;#";
		
		if (processoDt.getListaPolosPassivos() != null) conteudo += processoDt.getListaPolosPassivos().size() + "@;#";		else	conteudo += "0@;#";
		
		if (processoDt.getListaOutrasPartes() != null) conteudo += processoDt.getListaOutrasPartes().size() + "@;#";	else	conteudo += "0@;#";
		
		if (processoDt.getListaAssuntos() != null) conteudo += processoDt.getListaAssuntos().size() + "@;#";			else	conteudo += "0@;#";
		
		conteudo += "\n";
		// HASH
		conteudo += "10@;#" + Funcoes.GeraHashMd5(conteudo) + "@;#";

		return conteudo;
	}

	public String lerPartes(List listaPartes, String conteudo, int processoParteTipoCodigo, String complementoDependente) throws Exception {

		if (listaPartes != null) {
			if (!listaPartes.isEmpty()) {
				for (Iterator iterator = listaPartes.iterator(); iterator.hasNext();) {
					ProcessoParteDt parteDt = (ProcessoParteDt) iterator.next();
					conteudo += complementoDependente + "1@;#";
					conteudo += parteDt.getAlcunha() + "@;#";
					conteudo += parteDt.getCidadeNaturalidade() + "@;#";
					conteudo += parteDt.getCitacaoOnline() + "@;#";
					conteudo += parteDt.getCitada() + "@;#";
					conteudo += parteDt.getCnpj() + "@;#";
					conteudo += parteDt.getCodigoTemp() + "@;#";
					conteudo += parteDt.getCpf() + "@;#";
					conteudo += parteDt.getCtps() + "@;#";
					conteudo += parteDt.getCtpsSerie() + "@;#";
					conteudo += parteDt.getDataBaixa() + "@;#";
					conteudo += parteDt.getDataCadastro() + "@;#";
					conteudo += parteDt.getDataNascimento() + "@;#";
					conteudo += parteDt.getEMail() + "@;#";
					conteudo += parteDt.getEmpresaTipo() + "@;#";
					conteudo += parteDt.getEndereco() + "@;#";
					conteudo += parteDt.getEscolaridade() + "@;#";
					conteudo += parteDt.getEstadoCivil() + "@;#";
					conteudo += parteDt.getEstadoCtpsUf() + "@;#";
					conteudo += parteDt.getGovernoTipo() + "@;#";
					conteudo += parteDt.getId_Alcunha() + "@;#";
					conteudo += parteDt.getId_CtpsUf() + "@;#";
					conteudo += parteDt.getId_EmpresaTipo() + "@;#";
					conteudo += parteDt.getId_Endereco() + "@;#";
					conteudo += parteDt.getId_Escolaridade() + "@;#";
					conteudo += parteDt.getId_EstadoCivil() + "@;#";
					conteudo += parteDt.getId_GovernoTipo() + "@;#";
					conteudo += parteDt.getId_Naturalidade() + "@;#";
					conteudo += parteDt.getId_Processo() + "@;#";
					conteudo += parteDt.getId_ProcessoParte() + "@;#";
					conteudo += parteDt.getId_ProcessoParteAusencia() + "@;#";
					conteudo += parteDt.getId_ProcessoParteTipo() + "@;#";
					conteudo += parteDt.getId_Profissao() + "@;#";
					conteudo += parteDt.getId_RgOrgaoExpedidor() + "@;#";
					conteudo += parteDt.getId_Sinal() + "@;#";
					conteudo += parteDt.getIntimacaoOnline() + "@;#";
					conteudo += parteDt.getNome() + "@;#";
					conteudo += parteDt.getNomeMae() + "@;#";
					conteudo += parteDt.getNomePai() + "@;#";
					conteudo += parteDt.getNomeSimplificado() + "@;#";
					conteudo += parteDt.ParteNaoPersonificavel() + "@;#";
					conteudo += parteDt.getPis() + "@;#";
					conteudo += parteDt.getProcessoNumero() + "@;#";
					conteudo += parteDt.getProcessoParteAusencia() + "@;#";
					conteudo += parteDt.getProcessoParteAusenciaCodigo() + "@;#";
					conteudo += parteDt.getProcessoParteTipo() + "@;#";
					if (processoParteTipoCodigo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO || processoParteTipoCodigo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) conteudo += processoParteTipoCodigo + "@;#";
					else
						conteudo += parteDt.getProcessoParteTipoCodigo() + "@;#";
					conteudo += parteDt.getProfissao() + "@;#";
					conteudo += parteDt.getRecebeEMail() + "@;#";
					conteudo += parteDt.getRg() + "@;#";
					conteudo += parteDt.getRgDataExpedicao() + "@;#";
					conteudo += parteDt.getRgOrgaoExpedidor() + "@;#";
					conteudo += parteDt.getSexo() + "@;#";
					conteudo += parteDt.getSiglaOrgaoExpedidor() + "@;#";
					conteudo += parteDt.getSinal() + "@;#";
					conteudo += parteDt.getTelefone() + "@;#";
					conteudo += parteDt.getTituloEleitor() + "@;#";
					conteudo += parteDt.getTituloEleitorSecao() + "@;#";
					conteudo += parteDt.getTituloEleitorZona() + "@;#";
					conteudo += "\n";
					// ENDEREÇO PARTE
					conteudo += complementoDependente + "11@;#";
					conteudo += parteDt.getEnderecoParte().getBairro() + "@;#";
					conteudo += parteDt.getEnderecoParte().getBairroCodigo() + "@;#";
					conteudo += parteDt.getEnderecoParte().getCep() + "@;#";
					conteudo += parteDt.getEnderecoParte().getCidade() + "@;#";
					conteudo += parteDt.getEnderecoParte().getCidadeCodigo() + "@;#";
					conteudo += parteDt.getEnderecoParte().getCodigoTemp() + "@;#";
					conteudo += parteDt.getEnderecoParte().getComplemento() + "@;#";
					if (parteDt.getEnderecoParte().getEstado() != null) conteudo += parteDt.getEnderecoParte().getEstado();
					conteudo += "@;#";
					conteudo += parteDt.getEnderecoParte().getEstadoCodigo() + "@;#";
					conteudo += parteDt.getEnderecoParte().getId_Bairro() + "@;#";
					conteudo += parteDt.getEnderecoParte().getId_Cidade() + "@;#";
					conteudo += parteDt.getEnderecoParte().getId() + "@;#";
					conteudo += parteDt.getEnderecoParte().getLogradouro() + "@;#";
					conteudo += parteDt.getEnderecoParte().getNumero() + "@;#";
					conteudo += parteDt.getEnderecoParte().getUf() + "@;#";
					conteudo += "\n";
					// ALCUNHA PARTE
					if (parteDt.getListaAlcunhaParte() != null) {
						for (Iterator iterator3 = parteDt.getListaAlcunhaParte().iterator(); iterator3.hasNext();) {
							ProcessoParteAlcunhaDt alcunhaParteDt = (ProcessoParteAlcunhaDt) iterator3.next();
							conteudo += complementoDependente + "4@;#";
							conteudo += alcunhaParteDt.getAlcunha() + "@;#";
							conteudo += alcunhaParteDt.getId() + "@;#";
							conteudo += alcunhaParteDt.getId_Alcunha() + "@;#";
							conteudo += alcunhaParteDt.getId_ProcessoParte() + "@;#";
							conteudo += alcunhaParteDt.getNome() + "@;#";
							conteudo += alcunhaParteDt.getPropriedades() + "@;#";
							conteudo += "\n";
						}
					}
					// SINAL PARTE
					if (parteDt.getListaAlcunhaParte() != null) {
						for (Iterator iterator4 = parteDt.getListaAlcunhaParte().iterator(); iterator4.hasNext();) {
							ProcessoParteSinalDt alcunhaParteDt = (ProcessoParteSinalDt) iterator4.next();
							conteudo += complementoDependente + "5@;#";
							conteudo += alcunhaParteDt.getId() + "@;#";
							conteudo += alcunhaParteDt.getId_ProcessoParte() + "@;#";
							conteudo += alcunhaParteDt.getId_Sinal() + "@;#";
							conteudo += alcunhaParteDt.getNome() + "@;#";
							conteudo += alcunhaParteDt.getPropriedades() + "@;#";
							conteudo += alcunhaParteDt.getSinal() + "@;#";
							conteudo += "\n";
						}
					}
				}
			}
		}
		return conteudo;
		
	}

	public ProcessoCadastroDt carregarProcesso(ProcessoCadastroDt processoDt, String conteudo) throws Exception {

		if (conteudo != null) {

			int aux;
			String[] linha = null; // Vetor que armazena as linhas
			String[] vetor = null; // Vetor que armazena os dados de cada linha
			String[] vetorEndereco = null;
			String[] vetorSinal = null;
			String[] vetorAux = null;

			List listAdvogados = null;
			List listAlcunha = null;
			List listSinal = null;
																
			linha = conteudo.split("\n"); // Separa por quebra de linha

			String conteudoSemHash ="";
			for (int i = 0; i < linha.length; i++) {
				vetor = linha[i].split("@;#", 65);
				if ("10".equals(vetor[0])){
					String testarHash 	= Funcoes.GeraHashMd5(conteudoSemHash);
					if(!vetor[1].equalsIgnoreCase(testarHash)) { //verifica hash							
						throw new MensagemException("Arquivo Corrompido!");
					}
					break;
				}else{
					conteudoSemHash +=linha[i]+"\n";
				}
			}						
				
			for (int i = 0; i < linha.length; i++) {
				vetor = linha[i].split("@;#", 65);
				if (linhaVazia(vetor)) continue;
				if (vetor.length > 1) {
					aux = Funcoes.StringToInt(vetor[0]);// Armazena o primeiro dígito da linha, ou seja, o identificador do registro
					switch (aux) {
					case 0: // Captura o tipo do leiaute: 1 - SSP / 2 - Advogado / 3 - Novo Projudi
						if (!vetor[1].equals(""))
							Funcoes.StringToInt(vetor[1]);
						break;

					case 1: // PARTES
						listAlcunha = new ArrayList();
						listSinal = new ArrayList();
						vetorEndereco = linha[++i].split("@;#", 20); // Próxima é endereço
						vetorAux = linha[++i].split("@;#", 60);

						// ADVOGADO PARTE
						while (vetorAux[0].equals("12")) {
							UsuarioServentiaOabDt usuarioServentiaOabDt = new UsuarioServentiaOabDt();
							usuarioServentiaOabDt.setId_UsuarioServentia(vetorAux[1]);
							usuarioServentiaOabDt.setId(vetorAux[2]);
							usuarioServentiaOabDt.setNomeUsuario(vetorAux[3]);
							usuarioServentiaOabDt.setOabComplemento(vetorAux[4]);
							usuarioServentiaOabDt.setOabNumero(vetorAux[5]);
							processoDt.addListaAdvogados(usuarioServentiaOabDt);
							vetorAux = linha[++i].split("@;#", 20);
						}

						// ALCUNHA PARTE
						while (vetorAux[0].equals("4")) {
							ProcessoParteAlcunhaDt alcunhaParteDt = new ProcessoParteAlcunhaDt();
							alcunhaParteDt.setAlcunha(vetorAux[1]);
							alcunhaParteDt.setId(vetorAux[2]);
							alcunhaParteDt.setId_Alcunha(vetorAux[3]);
							alcunhaParteDt.setId_ProcessoParte(vetorAux[4]);
							alcunhaParteDt.setNome(vetorAux[5]);
							listAlcunha.add(alcunhaParteDt);
							vetorAux = linha[++i].split("@;#", 20);
						}

						// SINAL PARTE
						while (vetorAux[0].equals("5")) {
							ProcessoParteSinalDt alcunhaParteDt = new ProcessoParteSinalDt();
							alcunhaParteDt.setId(vetorAux[1]);
							alcunhaParteDt.setId_ProcessoParte(vetorAux[2]);
							alcunhaParteDt.setId_Sinal(vetorAux[3]);
							alcunhaParteDt.setNome(vetorAux[4]);
							alcunhaParteDt.setSinal(vetorAux[6]);
							listSinal.add(vetorSinal);
							vetorAux = linha[++i].split("@;#", 20);
						}
						i--;

						switch (Funcoes.StringToInt(vetor[46])) {
						case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:
							processoDt.addListaPoloAtivo(carregarParte(vetor, vetorEndereco, listAlcunha, listSinal));
							break;

						case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
							processoDt.addListaPolosPassivos(carregarParte(vetor, vetorEndereco, listAlcunha, listSinal));
							break;

						default:
							processoDt.addListaOutrasPartes(carregarParte(vetor, vetorEndereco, listAlcunha, listSinal));
						}
						break;
						
					case 6: // ASSUNTO
						while (vetor[0].equals("6")) {
							ProcessoAssuntoDt assuntoDt = new ProcessoAssuntoDt();
							assuntoDt.setAssunto(vetor[1]);
							assuntoDt.setCodigoTemp(vetor[2]);
							assuntoDt.setId_Assunto(vetor[3]);
							assuntoDt.setId_Processo(vetor[4]);
							assuntoDt.setId(vetor[5]);
							assuntoDt.setProcessoNumero(vetor[6]);
							processoDt.addListaAssuntos(assuntoDt);
							vetor = linha[++i].split("@;#", 20);
						}
						i--;
						break;
						
					case 71: //PARTES PROCESSO DEPENDENTE
						if(processoDt.getProcessoDependenteDt() == null) {
							ProcessoCadastroDt processoCadastroDt =  new ProcessoCadastroDt();
							processoDt.setProcessoDependenteDt(processoCadastroDt);
						}
						listAlcunha = new ArrayList();
						listSinal = new ArrayList();
						listAdvogados = new ArrayList();
						vetorEndereco = linha[++i].split("@;#", 20); // Próxima é endereço
						vetorAux = linha[++i].split("@;#", 60);

						// ADVOGADO PARTE
						while (vetorAux[0].equals("712")) {
							UsuarioServentiaOabDt usuarioServentiaOabDt = new UsuarioServentiaOabDt();
							usuarioServentiaOabDt.setId_UsuarioServentia(vetorAux[1]);
							usuarioServentiaOabDt.setId(vetorAux[2]);
							usuarioServentiaOabDt.setNomeUsuario(vetorAux[3]);
							usuarioServentiaOabDt.setOabComplemento(vetorAux[4]);
							usuarioServentiaOabDt.setOabNumero(vetorAux[5]);
							listAdvogados.add(usuarioServentiaOabDt);
							vetorAux = linha[++i].split("@;#", 20);
						}

						// ALCUNHA PARTE
						while (vetorAux[0].equals("74")) {
							ProcessoParteAlcunhaDt alcunhaParteDt = new ProcessoParteAlcunhaDt();
							alcunhaParteDt.setAlcunha(vetorAux[1]);
							alcunhaParteDt.setId(vetorAux[2]);
							alcunhaParteDt.setId_Alcunha(vetorAux[3]);
							alcunhaParteDt.setId_ProcessoParte(vetorAux[4]);
							alcunhaParteDt.setNome(vetorAux[5]);
							listAlcunha.add(alcunhaParteDt);
							vetorAux = linha[++i].split("@;#", 20);
						}

						// SINAL PARTE
						while (vetorAux[0].equals("75")) {
							ProcessoParteSinalDt alcunhaParteDt = new ProcessoParteSinalDt();
							alcunhaParteDt.setId(vetorAux[1]);
							alcunhaParteDt.setId_ProcessoParte(vetorAux[2]);
							alcunhaParteDt.setId_Sinal(vetorAux[3]);
							alcunhaParteDt.setNome(vetorAux[4]);
							alcunhaParteDt.setSinal(vetorAux[6]);
							listSinal.add(vetorSinal);
							vetorAux = linha[++i].split("@;#", 20);
						}
						i--;

						switch (Funcoes.StringToInt(vetor[46])) {
						case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:
							processoDt.getProcessoDependenteDt().addListaPoloAtivo(carregarParte(vetor, vetorEndereco, listAlcunha, listSinal));
							break;

						case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
							processoDt.getProcessoDependenteDt().addListaPolosPassivos(carregarParte(vetor, vetorEndereco, listAlcunha, listSinal));
							break;

						default:
							processoDt.getProcessoDependenteDt().addListaOutrasPartes(carregarParte(vetor, vetorEndereco, listAlcunha, listSinal));
						}
						break;
						
					case 78: // INFORMAÇÕES PROCESSO DEPENDENTE
						if(processoDt.getProcessoDependenteDt() == null) {
							ProcessoCadastroDt processoCadastroDt =  new ProcessoCadastroDt();
							processoDt.setProcessoDependenteDt(processoCadastroDt);
						}
						processoDt.getProcessoDependenteDt().setAno(vetor[1]);
						processoDt.getProcessoDependenteDt().setApenso(vetor[2]);
						processoDt.getProcessoDependenteDt().setArea(vetor[3]);
						processoDt.getProcessoDependenteDt().setAreaCodigo(vetor[4]);
						processoDt.getProcessoDependenteDt().setAreaDistribuicao(vetor[5]);
						processoDt.getProcessoDependenteDt().setClassificador(vetor[6]);
						processoDt.getProcessoDependenteDt().setCodigoTemp(vetor[7]);
						processoDt.getProcessoDependenteDt().setDataArquivamento(vetor[8]);
						processoDt.getProcessoDependenteDt().setDigitoVerificador(vetor[9]);
						processoDt.getProcessoDependenteDt().setExistePeticaoPendente(vetor[10].equals("true") ? true : false);
						processoDt.getProcessoDependenteDt().setForumCodigo(vetor[11]);
						processoDt.getProcessoDependenteDt().setId(vetor[12]);
						processoDt.getProcessoDependenteDt().setId_Area(vetor[13]);
						processoDt.getProcessoDependenteDt().setId_AreaDistribuicao(vetor[14]);
						processoDt.getProcessoDependenteDt().setId_Classificador(vetor[15]);
						processoDt.getProcessoDependenteDt().setId_ObjetoPedido(vetor[16]);
						processoDt.getProcessoDependenteDt().setId_Processo(vetor[17]);
						processoDt.getProcessoDependenteDt().setId_ProcessoPrincipal(vetor[18]);
						processoDt.getProcessoDependenteDt().setId_ProcessoFase(vetor[19]);
						processoDt.getProcessoDependenteDt().setId_ProcessoPrioridade(vetor[20]);
						processoDt.getProcessoDependenteDt().setId_ProcessoStatus(vetor[21]);
						processoDt.getProcessoDependenteDt().setId_ProcessoTipo(vetor[22]);
						processoDt.getProcessoDependenteDt().setId_Recurso(vetor[23]);
						processoDt.getProcessoDependenteDt().setId_Serventia(vetor[24]);
						processoDt.getProcessoDependenteDt().setId_ServentiaOrigem(vetor[25]);
						processoDt.getProcessoDependenteDt().setObjetoPedido(vetor[26]);
						processoDt.getProcessoDependenteDt().setObjetoPedidoCodigo(vetor[27]);
						processoDt.getProcessoDependenteDt().setProcessoDiretorio(vetor[28]);
						processoDt.getProcessoDependenteDt().setProcessoFase(vetor[29]);
						processoDt.getProcessoDependenteDt().setProcessoFaseCodigo(vetor[30]);
						processoDt.getProcessoDependenteDt().setProcessoNumero(vetor[31]);
						processoDt.getProcessoDependenteDt().setProcessoNumeroPrincipal(vetor[32]);
						processoDt.getProcessoDependenteDt().setProcessoPrioridade(vetor[33]);
						processoDt.getProcessoDependenteDt().setProcessoPrioridadeCodigo(vetor[34]);
						processoDt.getProcessoDependenteDt().setProcessoStatus(vetor[35]);
						processoDt.getProcessoDependenteDt().setProcessoStatusCodigo(vetor[36]);
						processoDt.getProcessoDependenteDt().setProcessoTipo(vetor[37]);
						processoDt.getProcessoDependenteDt().setProcessoTipoCodigo(vetor[38]);
						processoDt.getProcessoDependenteDt().setSegredoJustica(vetor[39]);
						processoDt.getProcessoDependenteDt().setServentia(vetor[40]);
						processoDt.getProcessoDependenteDt().setServentiaCodigo(vetor[41]);
						processoDt.getProcessoDependenteDt().setServentiaOrigem(vetor[42]);
						processoDt.getProcessoDependenteDt().setServentiaOrigemCodigo(vetor[43]);
						processoDt.getProcessoDependenteDt().setServentiaTipoCodigo(vetor[44]);
						processoDt.getProcessoDependenteDt().setTcoNumero(vetor[45]);
						processoDt.getProcessoDependenteDt().setValor(vetor[46]);
						break;
					
					case 8: // INFORMAÇÕES
						processoDt.setProcessoTipo(vetor[1]);
						processoDt.setId_ProcessoTipo(vetor[2]);
						processoDt.setProcessoTipoCodigo(vetor[3]);
						processoDt.setAreaCodigo(vetor[4]);
						processoDt.setAreaDistribuicao(vetor[5]);
						processoDt.setAreaDistribuicaoCodigo(vetor[6]);
						processoDt.setId_AreaDistribuicao(vetor[7]);
						processoDt.setComarca(vetor[8]);
						processoDt.setComarcaCodigo(vetor[9]);
						processoDt.setId_Comarca(vetor[10]);
						processoDt.setForumCodigo(vetor[11]);
						processoDt.setProcessoPrioridade(vetor[12]);
						processoDt.setProcessoPrioridadeCodigo(vetor[13]);
						processoDt.setId_ProcessoPrioridade(vetor[14]);
						processoDt.setValor(vetor[15]);
						processoDt.setProcessoNumero(vetor[16]);
						processoDt.setProcessoNumeroPrincipal(vetor[17]);
						if (vetor[18].equalsIgnoreCase("")){
							processoDt.limpeProcessoNumeroFisico();
						}else{
							processoDt.setProcessoNumeroFisico(vetor[18]);
						}
						processoDt.setServentiaCargo(vetor[19]);
						processoDt.setServentia(vetor[20]);
						processoDt.setTcoNumero(vetor[21]);
						processoDt.setProcessoDependente(vetor[22].equalsIgnoreCase("true") ? true : false);
						processoDt.setProcessoFisico(vetor[23].equalsIgnoreCase("true") ? true : false);
						processoDt.setGrauProcesso(vetor[24]);
						processoDt.setTipoProcesso(vetor[25]);
						processoDt.setAssistenciaProcesso(vetor[26]);
						processoDt.setDependenciaProcesso(vetor[27]);
						break;							
					default:
						break;
					} // fim switch
				} else {
					// Mensagem +=
					// "Existe uma linha inválida no arquivo. Verifique e tente novamente.";
				}

			} // fim for
			
			

		} else {
			// Mensagem += "Não há um arquivo válido a ser lido.";
		}

		return processoDt;
	}

	public ProcessoParteDt carregarParte(String[] vetor, String[] vetorEndereco, List listAlcunha, List listSinal) throws Exception {

		ProcessoParteDt parteDt = new ProcessoParteDt();
		parteDt.setAlcunha(vetor[1]);
		parteDt.setCidadeNaturalidade(vetor[2]);
		parteDt.setCitacaoOnline(vetor[3]);
		parteDt.setCitada(vetor[4]);
		parteDt.setCnpj(vetor[5]);
		parteDt.setCodigoTemp(vetor[6]);
		parteDt.setCpf(vetor[7]);
		parteDt.setCtps(vetor[8]);
		parteDt.setCtpsSerie(vetor[9]);
		parteDt.setDataBaixa(vetor[10]);
		parteDt.setDataCadastro(vetor[11]);
		parteDt.setDataNascimento(vetor[12]);
		parteDt.setEMail(vetor[13]);
		parteDt.setEmpresaTipo(vetor[14]);
		parteDt.setEndereco(vetor[15]);
		parteDt.setEscolaridade(vetor[16]);
		parteDt.setEstadoCivil(vetor[17]);
		parteDt.setEstadoCtpsUf(vetor[18]);
		parteDt.setGovernoTipo(vetor[19]);
		parteDt.setId_Alcunha(vetor[20]);
		parteDt.setId_CtpsUf(vetor[21]);
		parteDt.setId_EmpresaTipo(vetor[22]);
		parteDt.setId_Endereco(vetor[23]);
		parteDt.setId_Escolaridade(vetor[24]);
		parteDt.setId_EstadoCivil(vetor[25]);
		parteDt.setId_GovernoTipo(vetor[26]);
		parteDt.setId_Naturalidade(vetor[27]);
		parteDt.setId_Processo(vetor[28]);
		parteDt.setId_ProcessoParte(vetor[29]);
		parteDt.setId_ProcessoParteAusencia(vetor[30]);
		parteDt.setId_ProcessoParteTipo(vetor[31]);
		parteDt.setId_Profissao(vetor[32]);
		parteDt.setId_RgOrgaoExpedidor(vetor[33]);
		parteDt.setId_Sinal(vetor[34]);
		parteDt.setIntimacaoOnline(vetor[35]);
		parteDt.setNome(vetor[36]);
		parteDt.setNomeMae(vetor[37]);
		parteDt.setNomePai(vetor[38]);
		parteDt.setNomeSimplificado(vetor[39]);
		parteDt.setParteNaoPersonificavel(vetor[40].equals("true") ? true : false);
		parteDt.setPis(vetor[41]);
		parteDt.setProcessoNumero(vetor[42]);
		parteDt.setProcessoParteAusencia(vetor[43]);
		parteDt.setProcessoParteAusenciaCodigo(vetor[44]);
		parteDt.setProcessoParteTipo(vetor[45]);
		parteDt.setProcessoParteTipoCodigo(vetor[46]);
		parteDt.setProfissao(vetor[47]);
		parteDt.setRecebeEMail(vetor[48]);
		parteDt.setRg(vetor[49]);
		parteDt.setRgDataExpedicao(vetor[50]);
		parteDt.setRgOrgaoExpedidor(vetor[51]);
		parteDt.setSexo(vetor[52]);
		parteDt.setSiglaOrgaoExpedidor(vetor[53]);
		parteDt.setSinal(vetor[54]);
		parteDt.setTelefone(vetor[55]);
		parteDt.setTituloEleitor(vetor[56]);
		parteDt.setTituloEleitorSecao(vetor[57]);
		parteDt.setTituloEleitorZona(vetor[58]);
		
		// ENDEREÇO PARTE
		parteDt.getEnderecoParte().setBairro(vetorEndereco[1]);
		parteDt.getEnderecoParte().setBairroCodigo(vetorEndereco[2]);
		parteDt.getEnderecoParte().setCep(vetorEndereco[3]);
		parteDt.getEnderecoParte().setCidade(vetorEndereco[4]);
		parteDt.getEnderecoParte().setCidadeCodigo(vetorEndereco[5]);
		parteDt.getEnderecoParte().setCodigoTemp(vetorEndereco[6]);
		parteDt.getEnderecoParte().setComplemento(vetorEndereco[7]);
		parteDt.getEnderecoParte().setEstado(vetorEndereco[8]);
		parteDt.getEnderecoParte().setEstadoCodigo(vetorEndereco[9]);
		parteDt.getEnderecoParte().setId_Bairro(vetorEndereco[10]);
		parteDt.getEnderecoParte().setId_Cidade(vetorEndereco[11]);
		parteDt.getEnderecoParte().setId(vetorEndereco[12]);
		parteDt.getEnderecoParte().setLogradouro(vetorEndereco[13]);
		parteDt.getEnderecoParte().setNumero(vetorEndereco[14]);
		parteDt.getEnderecoParte().setUf(vetorEndereco[15]);
		
		return parteDt;
	}

	/**
	 * Método responsável por verificar se determinada linha do arquivo está
	 * vazia
	 */
	private boolean linhaVazia(String[] vetor) {
		for (int i = 1; i < vetor.length - 1; i++) {
			if (!vetor[i].equals("")) return false;
		}
		return true;
	}
	
	/**
	 * Arquiva os processos do tipo Cálculo de Liquidação de Pena. Por se tratar de um processo físico, o arquivamento do processo não é feito através de geração de pendência.
	 * @param processoDt: objeto com os dados do processo
	 * @param usuarioDt: objeto com os dados do usuário da sessão.
	 * @throws Exception
	 */
	public String arquivarProcessoCalculo(ProcessoDt processoDt, UsuarioDt usuarioDt, String complemento) throws Exception {
	    String mensagem = "";
		FabricaConexao obFabricaConexao = null;
		try {
			mensagem = podeArquivar(processoDt, usuarioDt.getGrupoCodigo());
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());

			if (mensagem.length() == 0)
				arquivarProcesso(processoDt, logDt, usuarioDt.getId_UsuarioServentia(), complemento, "", obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return mensagem;
	}
	
	/**
	 * Arquiva os processos do tipo Cálculo de Liquidação de Pena. Por se tratar de um processo físico, o desarquivamento do processo não é feito através de geração de pendência.
	 * @param processoDt: objeto com os dados do processo
	 * @param usuarioDt: objeto com os dados do usuário da sessão.
	 * @throws Exception
	 */
	public String desarquivarProcessoCalculo(ProcessoDt processoDt, UsuarioDt usuarioDt, String complemento) throws Exception {
	    String mensagem = "";
		FabricaConexao obFabricaConexao = null;
		try {
			mensagem = podeDesarquivar(processoDt);
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
			if (mensagem.length() == 0)
				desarquivarProcesso(processoDt, logDt, usuarioDt.getId_UsuarioServentia(), complemento, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return mensagem;
	}
	
	

	public List listarProcessoIdNP(List ids_processo, int processoSubTipoCodigo) throws Exception {
		
		List listaRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaRetorno = obPersistencia.listarProcessosIdFiltroProcessoSubTipo(ids_processo, processoSubTipoCodigo);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaRetorno;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarAssuntosAreaDistribuicao(String tempNomeBusca, String Id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();		
		tempList = neObjeto.consultarAssuntosAreaDistribuicao(Id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		neObjeto = null;
		return tempList;
	}

	public String[] consultarProcessoDependente(String id_Serventia, String id_processo, String id_ProcessoDependente, FabricaConexao obFabricaConexao) throws Exception {
		String[] stRetorno=null;		
		
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarProcessoDependente(id_Serventia, id_processo, id_ProcessoDependente);
		
		return stRetorno;
	}
	
	public String[] consultarRecursosDependente(String id_processo, String id_areaDistribuicao, FabricaConexao obFabricaConexao) throws Exception {
		String[] stRetorno=null;		
		
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		stRetorno = obPersistencia.consultarRecursosDependente(id_processo, id_areaDistribuicao);
		
		return stRetorno;
	}
	
	/**
	 * Método que altera o status de um processo.
	 * @param processoDt - processo
	 * @param novoProcessoStatusCodigo - novo código de status (ProcessoStatusCodigo)
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarStatusProcesso(ProcessoDt processoDt, int novoProcessoStatusCodigo) throws Exception {
		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Salva alterações do processo
			if (!processoDt.getId_Processo().equalsIgnoreCase("")) {
				logDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoDt.getPropriedades());
				obPersistencia.alterarProcessoStatus(processoDt.getId(), novoProcessoStatusCodigo);
			}

			obDados.copiar(processoDt);
			obLog.salvar(logDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que altera a fase de um processo.
	 * @param id_Processo - ID do processo
	 * @param novoProcessoFaseCodigo - novo código de fase (ProcessoFaseCodigo)
	 * @param conexao - conexão de transação (opcional)
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarFaseProcesso(ProcessoDt processoDt, int novoProcessoFaseCodigo, FabricaConexao conexao) throws Exception {
		LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			obFabricaConexao.iniciarTransacao();
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Salva alterações do processo
			if (!processoDt.getId_Processo().equalsIgnoreCase("")) {
				logDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), processoDt.getPropriedades());
				obPersistencia.alterarProcessoFase(processoDt.getId(), novoProcessoFaseCodigo);
			}

			obDados.copiar(processoDt);
			obLog.salvar(logDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Registra o julgamento do Mérito do Processo Principal
	 * @param id_Processo: processo em qual teve o julgamento do Mérito
	 * @param julgado: informa se houve ou não o julgamento de Mérito
	 */
	public void registrarJulgamentoMeritoProcessoPrincipal(String id_Processo, boolean julgado, FabricaConexao conexao ) throws Exception {
		FabricaConexao obFabricaConexao =null;
		try {
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			obPersistencia.registrarJulgamentoMeritoProcessoPrincipal(id_Processo, julgado);
			
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}

	public ProcessoDt consultarProcesso(String processoNumero, String digitoVerificador, String ano, String forumCodigo, String processoStatusCodigo) throws Exception {
		ProcessoDt retorno = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarProcesso(processoNumero, digitoVerificador, ano, forumCodigo, processoStatusCodigo);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * Monta a lista de eventos com os dados dos eventos, dados das condenações e dados do ProcessoExecução - utilizado na consulta pública de processo
	 * @param id_processo: identificador do processo
	 * @param usuarioDt: usuário da sessão
	 * @param calculoLiquidacaoDt: objeto de cálculo de liquidação de pena
	 * @param listaCondenacaoExtinta: lista de condenação extinta
	 * @return map com as listas de evento: 
	 * @throws Exception
	 */
	public HashMap montarListaEventosCompleta(String id_processo, UsuarioDt usuarioDt, CalculoLiquidacaoDt calculoLiquidacaoDt, List listaCondenacaoExtinta) throws Exception{
		HashMap maplista_Evento_Historico = null;
		
		maplista_Evento_Historico = new ProcessoEventoExecucaoNe().montarListaEventosCompleta(id_processo, usuarioDt, calculoLiquidacaoDt, listaCondenacaoExtinta);
		
		return maplista_Evento_Historico;
	}
	
	/**
	 * Verifica se o tipo de pena atual do processo é Pena Restritiva de Direito - utilizado na consulta pública de processo
	 * @param listaEvento: lista com os eventos
	 * @return boolean
	 */
	public boolean isProcessoPenaRestritivaDireito(List listaEvento) throws Exception{
		return new ProcessoEventoExecucaoNe().isProcessoPenaRestritivaDireito(listaEvento);
	}
	
	/**
	 * Método responsável por transformar tratar as regras de negócio que estavam na  JSP de 
	 * dados da capa do processo completo
	 * @param listaMovimentacoesProcesso
	 * @return lista de movimentações
	 * @throws Exception
	 * @author hmgodinho
	 */
	public ProcessoDt prepararListaMovimentacoesProcesso (ProcessoDt processoDt, UsuarioNe usuarioSessao, boolean consultaPublica) throws Exception {
		
		List listaMovimentacoes = new ArrayList();
		MovimentacaoDt movimentacaoDt = null;
		
		boolean permissaoPendencia = false;
		//se for consulta pública, não será permitido gerar pendência
		if(!consultaPublica) {
			permissaoPendencia = usuarioSessao.getVerificaPermissao(MovimentacaoProcessoDt.CodigoPermissaoGerarPendencias);
		}
		
		for (int i = 0; i < processoDt.getTamanhoListaMovimentacoes(); i++) {
			movimentacaoDt = (MovimentacaoDt)processoDt.getListaMovimentacoes().get(i);
			
			//validação para confirmar se deve mostrar a coluna Manter Eventos
			if(processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))
					|| movimentacaoDt.temArquivos()
					|| (movimentacaoDt.getId_UsuarioRealizador() != null && !movimentacaoDt.getId_UsuarioRealizador().equals(UsuarioServentiaDt.SistemaProjudi))) {
				movimentacaoDt.setMostrarColunaManterEventos(true);
			} else {
				movimentacaoDt.setMostrarColunaManterEventos(false);
			}
			
			//validação para confirmar se deve mostrar a coluna Gerar Pendências
			if(permissaoPendencia && (movimentacaoDt.temArquivos() 
					//se não tiver arquivos na movimentação, é preciso verificar se é movimentação indicando que alguma audiência foi marcada.
	  				//se for esse tipo de movimentação, deve liberar o ícone para gerar pendência no processo.
					|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_MARCADA))
	  				|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_MARCADA)) 
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_ADMONITORIA_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_INSTRUCAO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_EXECUCAO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_INICIAL_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_MEDIACAO_CEJUSC_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_CEJUSC_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_INQUIRICAO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_JULGAMENTO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_INSTRUCAO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_INTERROGATORIO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_JULGAMENTO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_JUSTIFICACAO_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_PRELIMINAR_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_SINE_DIE_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_SUSPENSAO_CONDICIONAL_MARCADA))
	  			  	|| movimentacaoDt.getMovimentacaoTipoCodigo().equals(String.valueOf(MovimentacaoTipoDt.AUDIENCIA_SESSAO_MARCADA)) )
	  				&& !movimentacaoDt.getId_UsuarioRealizador().equals(UsuarioServentiaDt.SistemaProjudi)) {
				movimentacaoDt.setMostrarColunaGerarPendencias(true);
			} else {
				movimentacaoDt.setMostrarColunaGerarPendencias(false);
			}
			listaMovimentacoes.add(movimentacaoDt);
		}
		
		//substituindo a lista de movimetações do processo para que as novas informações
		//adicionadas constem em cada movimentação da lista
		processoDt.setListaMovimentacoes(listaMovimentacoes);
		
		return processoDt;
	}
	
	/**
	 * Consultar presidente segundo grau
	 * 
	 * @param id_Serventia
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt getPresidenteSegundoGrau(String id_Serventia) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ServentiaCargoNe neObjeto = new ServentiaCargoNe();
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			dtRetorno = neObjeto.getPresidenteSegundoGrau(id_Serventia, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		neObjeto = null;
		return dtRetorno;
	}
	
	/**
	 * Consultar relator segundo grau
	 * 
	 * @param id_Processo
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarRelator2Grau(String id_Processo) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ProcessoResponsavelNe neObjeto = new ProcessoResponsavelNe();
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			dtRetorno = neObjeto.consultarRelator2Grau(id_Processo, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		neObjeto = null;
		return dtRetorno;
	}
	
	/**
     * Gerar pdf de uma publicação
     * @param String
     *            stIdArquivo, id de um arquivo de uma publicação (pendencia do
     *            tipo publicação)
     * @return byte[] , retorna bytes contendo a publicação em pdf
     * @throws Exception
     */
    public byte[] gerarPdfPublicacao(String diretorioProjeto, String stIdArquivo, String id_MovimentacaoArquivo, ProcessoDt processoDt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
    	byte[] byTemp = null;
        
        MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoNe().consultarIdCompleto(id_MovimentacaoArquivo);
		
		if (!podeBaixarArquivo(usuarioDt, processoDt, movimentacaoArquivoDt)){
			throw new MensagemException("Usuário sem permissão para baixar o arquivo!");
		}
		String stDados = "id_proc = " +  processoDt.getId() + "; proc_numero= " + processoDt.getProcessoNumeroCompleto() + "; id_movi_arq= " + id_MovimentacaoArquivo;
		//Grava o log da requisicao
		LogNe logNe = new LogNe();
		logNe.salvar(new LogDt("Arquivo", movimentacaoArquivoDt.getId_Arquivo(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Download), "", stDados));
        		      
        byTemp = new PendenciaNe().gerarPdfPublicacao(diretorioProjeto, stIdArquivo, usuarioDt, processoDt);
        return byTemp;
    }
    
    /**
	 * lista número completo dos processos de cálculo de liquidação de pena
	 * @author wcsilva
	 */
	public List listarNumeroProcessoCalculo() throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.listarNumeroProcessoCalculo();
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	} 
	
	public DataProvavelDt consultarCalculoLiquidacao(String idProcesso) throws Exception{
		DataProvavelDt objRetorno = null;
		objRetorno = new ProcessoEventoExecucaoNe().consultarCalculoLiquidacao(idProcesso);
		return objRetorno;
	}
	
	public String consultarGrupoArquivoTipoJSON(String grupoCodigo, String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ArquivoTipoNe neObjeto = new ArquivoTipoNe();
		stTemp = neObjeto.consultarGrupoArquivoTipoJSON(grupoCodigo, tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarModeloJSON(UsuarioDt usuarioDt, String id_ArquivoTipo, String descricao, String posicao) throws Exception {
		String stTemp = "";
		ModeloNe neObjeto = new ModeloNe();
		stTemp = neObjeto.consultarModelosJSON(descricao,  posicao, id_ArquivoTipo, usuarioDt);
		neObjeto = null;
		return stTemp;
	}
	
//  Em determinação ao BO 2013/82139 aberto pela Corregedoria em cumprimento ao despacho 3227/2013.
//	
//	/**
//	 * Valida a quantidade máxima de ações vinculadas ao advogado no ano atual 
//	 * 
//	 * @param processoCadastroDt
//	 * @param usuarioDt
//	 * @param conexao
//	 * @throws Exception
//	 */
//	private void valideQuantidadeAcoesCadastroProcessoAdvogado(ProcessoCadastroDt processoCadastroDt, UsuarioDt usuarioDt, FabricaConexao conexao) throws Exception
//	{		
//		ProcessoPs processoPs = new ProcessoPs(conexao);
//		if (usuarioDt.getEstadoRepresentacao() != null && usuarioDt.getServentiaTipoCodigo() != null && usuarioDt.getServentiaTipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL)) && !usuarioDt.getEstadoRepresentacao().trim().equalsIgnoreCase("GO"))
//		{
//			long quantidadeProcessosAtivos = processoPs.consultarQuantidadeProcessosAtivosAdvogadoAnoAtual(usuarioDt.getId_UsuarioServentia());
//			
//			if (quantidadeProcessosAtivos >= ProcessoParteAdvogadoDt.QUANTIDADE_PROCESSOS_FORA_ESTADO){
//				throw new Mensagem(String.format("Limite máximo de %s ações atingido para esse ano. Impossível cadastrar o processo. ", ProcessoParteAdvogadoDt.QUANTIDADE_PROCESSOS_FORA_ESTADO));
//			}
//		}
//	}
	
	/**
	 * Valida existência do processo 
	 * 
	 * @param processoCadastroDt	 
	 * @param conexao
	 * @throws Exception
	 */
	private boolean validaExistenciaProcessoNumeroCompleto(ProcessoCadastroDt processoCadastroDt, FabricaConexao conexao) throws Exception
	{		
		ProcessoPs processoPs = new ProcessoPs(conexao.getConexao());
		ProcessoDt dtRetorno = processoPs.consultarProcessoNumeroCompleto(processoCadastroDt.getProcessoNumeroSimples(), processoCadastroDt.getDigitoVerificador(), processoCadastroDt.getAno(), processoCadastroDt.getForumCodigo());
		if (dtRetorno != null){
			return true;
		}
		return false;		
	}
	
	
	/**
	 * Sobrecarga para o método abaixo, já existente, para que ele possa usar uma conexão passada como parâmetro
	 * @param dados
	 * @throws Exception
	 * @author hrrosa
	 */
	public void alterarNumeroProcesso(ProcessoDt dados) throws Exception {
		this.alterarNumeroProcesso(dados, null);
	}

	public void alterarNumeroProcesso(ProcessoDt dados, FabricaConexao fabricaConexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		try {
			if(fabricaConexao == null){
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else {
				obFabricaConexao = fabricaConexao;
			}
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			obPersistencia.alterarNumeroProcesso(dados);
            obLogDt = new LogDt("Processo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());				

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			
			if(fabricaConexao == null){
				obFabricaConexao.finalizarTransacao();
			}

		}catch(Exception e){ 
			if(fabricaConexao == null){
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}finally{
			if(fabricaConexao == null){
				obFabricaConexao.fecharConexao();
			}
		}
	}

	public boolean podeBaixarArquivo(UsuarioDt usuarioDt, ProcessoDt processoDt, MovimentacaoArquivoDt moviArquivo) throws Exception {
		if (new MovimentacaoArquivoNe().podeBaixarArquivo(usuarioDt, processoDt, moviArquivo))
			return true; 
		return false;
	}
	
	/**
	 * Consultar pendencia
	 * 
	 * @param id_Pendencia
	 * @return
	 * @throws Exception
	 */
	public PendenciaDt consultarPendencia(String id_Pendencia, UsuarioNe usuarioNe) throws Exception, MensagemException {
		PendenciaDt dtRetorno = null;
		PendenciaNe neObjeto = new PendenciaNe();
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			dtRetorno = neObjeto.consultarPendenciaId(id_Pendencia, usuarioNe);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		neObjeto = null;
		return dtRetorno;
	}
	
	
	public String consultarDescricaoAreasDistribuicaoAtivaJSON(String stNomeBusca1, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoAtivasJSON(stNomeBusca1, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoAreasDistribuicaoPreprocessualAtivaJSON(String stNomeBusca1, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoPreprocessualAtivasJSON(stNomeBusca1, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarServentiasRedistribuicaoJSON(String tempNomeBusca, String posicaoPaginaAtual, String serventiaTipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaNe neObjeto = new ServentiaNe();
		stTemp = neObjeto.consultarServentiasRedistribuicaoJSON(tempNomeBusca, posicaoPaginaAtual, serventiaTipoCodigo);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarServentiasAtivasAreaDistribuicaoJSON(String tempNomeBusca, String idAreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaNe neObjeto = new ServentiaNe();
		stTemp = neObjeto.consultarServentiasAtivasAreaDistribuicaoJSON(tempNomeBusca, idAreaDistribuicao, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}

	public String consultarListaServentiaRelacionadaJSON(String tempNomeBusca, String idServentia, int tipoServentia, String posicao, int qtdeColunas) throws Exception {
		String stTemp = "";
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		stTemp = serventiaRelacionadaNe.consultarListaServentiaRelacionadaJSON(idServentia, tipoServentia, posicao, qtdeColunas);
		serventiaRelacionadaNe = null;
		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ComarcaNe neObjeto = new ComarcaNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoCivelJSON(String tempNomeBusca, String id_Comarca, String id_Serventia_Usuario, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoCivelJSON(tempNomeBusca, id_Comarca, id_Serventia_Usuario,  posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoCriminalJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoCriminalJSON(tempNomeBusca, id_Comarca,  posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(tempNomeBusca, id_Comarca,  posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoProcessoPrioridadeJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ProcessoPrioridadeNe ProcessoPrioridadene = new ProcessoPrioridadeNe(); 
		stTemp = ProcessoPrioridadene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		ProcessoPrioridadene = null;
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoJSON(String tempNomeBusca, String codigoCNJ, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		List areasDistribuicoes = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(id_Serventia);
		stTemp = neObjeto.consultarAssuntosAreasDistribuicoesJSON(areasDistribuicoes, tempNomeBusca, codigoCNJ, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoServentiaJSON(String tempNomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AssuntoNe neObjeto = new AssuntoNe();
		stTemp = neObjeto.consultarDescricaoAssuntoServentiaJSON(tempNomeBusca, id_Serventia, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarAssuntosAreaDistribuicaoJSON(String tempNomeBusca, String codigoCNJ, String Id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();		
		stTemp = neObjeto.consultarAssuntosAreaDistribuicaoJSON(Id_AreaDistribuicao, tempNomeBusca, codigoCNJ, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoCidadeJSON(String tempNomeBusca1, String tempNomeBusca2, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		CidadeNe neObjeto = new CidadeNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca1, tempNomeBusca2, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoEstadoCivilJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EstadoCivilNe neObjeto = new EstadoCivilNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoProfissaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProfissaoNe neObjeto = new ProfissaoNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarOutrosTiposPartesJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoParteTipoNe neObjeto = new ProcessoParteTipoNe();
		stTemp = neObjeto.consultarOutrosTiposPartesJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoOrgaoExpedidorJSON(String tempNomeBusca, String sigla, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		RgOrgaoExpedidorNe neObjeto = new RgOrgaoExpedidorNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, sigla, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoEstadoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EstadoNe neObjeto = new EstadoNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public BairroDt consultarDescricaoBairroId(String idBairro) throws Exception {
		BairroDt bairroDt = new BairroDt();
		BairroNe Bairrone = new BairroNe();
		bairroDt = Bairrone.consultarId(idBairro);
		return bairroDt;
	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";
		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);
		return stTemp;
	}
	
	public String consultarDescricaoGovernoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		GovernoTipoNe neObjeto = new GovernoTipoNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoEmpresaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EmpresaTipoNe neObjeto = new EmpresaTipoNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public ProcessoPrioridadeDt consultarProcessoPrioridadeId(String idprocessoPrioridade) throws Exception {
		ProcessoPrioridadeDt dtRetorno = null;
		ProcessoPrioridadeNe neObjeto = new ProcessoPrioridadeNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			dtRetorno = neObjeto.consultarId(idprocessoPrioridade);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		neObjeto = null;
		return dtRetorno;
	}
	
	public boolean isResponsavelProcesso(String id_ServentiaCargo, String id_Processo) throws Exception {
		return new ProcessoResponsavelNe().isResponsavelProcesso(id_ServentiaCargo, id_Processo);		
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_AreaDistribuicao
	 *            , id da Area Distribuicao
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoAreaDistribuicao(String id_AreaDistribuicao) throws Exception {
		String id_ServentiaSubTipo = null;
		ServentiaSubtipoNe serventiaSubtipoNe = new ServentiaSubtipoNe();
		id_ServentiaSubTipo = serventiaSubtipoNe.consultarServentiaSubTipoAreaDistribuicao(id_AreaDistribuicao);
		return id_ServentiaSubTipo;
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_Serventia
	 *            , id da Serventia
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoServentia(String id_Serventia) throws Exception {
		String id_ServentiaSubTipo = null;
		ServentiaSubtipoNe serventiaSubtipoNe = new ServentiaSubtipoNe();
		id_ServentiaSubTipo = serventiaSubtipoNe.consultarServentiaSubTipoServentia(id_Serventia);
		return id_ServentiaSubTipo;
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_Serventia
	 *            , id da Serventia
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public boolean isProcessoTipoValido(String id_ServentiaSubTipo, String id_ProcessoTipo ) throws Exception {
		boolean retorno = false;
		ServentiaSubtipoProcessoTipoNe serventiaSubtipoProcessoTipoNe = new ServentiaSubtipoProcessoTipoNe();
		retorno = serventiaSubtipoProcessoTipoNe.isProcessoTipoValido(id_ServentiaSubTipo, id_ProcessoTipo);
		return retorno;
	}

	
	public String consultarAreasDistribuicaoSegundoGrauCriminalJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual) throws Exception {
		String stTemp="";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauCriminalJSON(tempNomeBusca, id_Comarca,  posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}

	public String consultarAreasDistribuicaoSegundoGrauCriminalJSON(String tempNomeBusca, String id_Comarca,  String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception {
		String stTemp="";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauCriminalJSON(tempNomeBusca, id_Comarca,  posicaoPaginaAtual, turmaJulgadora);
		neObjeto = null;
		return stTemp;
	}

	public String consultarAreasDistribuicaoSegundoGrauJSON(String tempNomeBusca, String id_Comarca, String areaCodigo, String posicaoPaginaAtual) throws Exception {
		String stTemp="";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauJSON(tempNomeBusca, id_Comarca, areaCodigo, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarAreasDistribuicaoSegundoGrauJSON(String tempNomeBusca, String id_Comarca, String areaCodigo, String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception {
		String stTemp="";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauJSON(tempNomeBusca, id_Comarca, areaCodigo, posicaoPaginaAtual, turmaJulgadora);
		neObjeto = null;
		return stTemp;
	}
	
	//consulta dados da ação penal via ajax
	public String consultarIdAcaoPenalJSON(String idProcessoexecucao) throws Exception {
		String retorno=null;
		retorno= new ProcessoExecucaoNe().consultarIdJSON(idProcessoexecucao );
		return retorno;
	}
	
	public String consultarAssuntosServentiaJSON(String stNomeBusca1, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();		
		stTemp = neObjeto.consultarAssuntosServentiaJSON(stNomeBusca1, id_Serventia, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoEscolaridadeJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		EscolaridadeNe neObjeto = new EscolaridadeNe();
		stTemp = neObjeto.consultarDescricaoEscolaridadeJSON( tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoClassificadorJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		String stTemp = "";
		ClassificadorNe neObjeto = new ClassificadorNe();
		stTemp = neObjeto.consultarClassificadorServentiaJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoProcessoFaseJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ProcessoFaseNe ProcessoFasene = new ProcessoFaseNe(); 
		stTemp = ProcessoFasene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		ProcessoFasene = null;
		return stTemp;
	}
	
	public String consultarProcessoTipoServentiaJSON(String descricao, String idServentia, UsuarioDt usuarioDt, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		if (usuarioDt != null && usuarioDt.getGrupoTipoCodigo() != null && usuarioDt.getGrupoTipoCodigo().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ADVOGADO)))
			stTemp = processoTipoNe.consultarProcessoTipoPublicoServentiaJSON(descricao, idServentia, posicaoPaginaAtual);
		else
			stTemp = processoTipoNe.consultarProcessoTipoServentiaJSON(descricao, idServentia, posicaoPaginaAtual);
		processoTipoNe = null;
		return stTemp;
	}
	
	public String consultarServentiaCargosJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarServentiaCargosJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaTipoCodigo, serventiaSubtipoCodigo);
		ServentiaCargone = null;
		return stTemp;
	}
	
	public String consultarMagistradoGabineteJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		ServentiaCargoNe ServentiaCargone = new ServentiaCargoNe();
		stTemp = ServentiaCargone.consultarMagistradoGabineteJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia, serventiaSubTipoCodigo );
		ServentiaCargone = null;
		return stTemp;
	}
	
	public String consultarDescricaoProcessoTipoJSON(String tempNomeBusca, String id_AreaDistribuicao, UsuarioDt usuarioDt, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoProcessoTipoNe neObjeto = new ServentiaSubtipoProcessoTipoNe();
		if (usuarioDt != null && usuarioDt.getGrupoTipoCodigo() != null 
				&& usuarioDt.getGrupoTipoCodigo().equalsIgnoreCase(String.valueOf(GrupoTipoDt.ADVOGADO)))
			stTemp = neObjeto.consultarProcessoTiposPublicosJSON(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		else
			stTemp = neObjeto.consultarProcessoTiposJSON(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	/**
	 * Método responsável por realizar a consulta de processo tipo por descrição, independente da área de distribuição do usuário.
	 * @param tempNomeBusca - descrição do processo tipo
	 * @param posicaoPaginaAtual 
	 * @return lista de processos tipo localizados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarDescricaoProcessoTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		stTemp = processoTipoNe.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}
	
	/**
	 * Método para consultar se processo é de área criminal.
	 * Retorna true caso sim e false caso não.
	 * 
	 * @param String id_Processo
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean isProcessoAreaCriminal(String id_Processo) throws Exception {
		boolean retorno = false;
		
		FabricaConexao obFabricaConexao = null;
		try {
			if( id_Processo != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
				
				retorno = obPersistencia.isProcessoAreaCriminal(id_Processo);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public String consultarIdArea(String id_Processo, FabricaConexao fabConexao) throws Exception {
		String retorno = "";
            
		if( id_Processo != null ) {
			ProcessoPs obPersistencia = new ProcessoPs(fabConexao.getConexao());
			retorno = obPersistencia.consultarIdArea(id_Processo);
		}
		
		return retorno;
	}
	
	public List consultarProcessosAdvogadoWebService(UsuarioNe usuarioNe, String idProcessoTipo, String dataRecebimento, String posicao) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosAdvogadoWebService(usuarioNe, idProcessoTipo, dataRecebimento, posicao);

			setQuantidadePaginas(listaProcessos);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public String consultarArquivosJSON(String id, UsuarioNe usuarioSessao) throws Exception {
		String stTemp =""; 
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		stTemp = movimentacaoNe.consultarArquivosJSON(id,  usuarioSessao); 
        return stTemp;
	}
	
	public String consultarDescricaoProcessoStatusJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception{
		String stTemp ="";
		ProcessoStatusNe neObjeto = new ProcessoStatusNe();
    		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}

	public GuiaEmissaoDt consultarCustas(String idProcesso) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoNe().consultarUltimaGuiaEmissaoFazenda(idProcesso);
		return guiaEmissaoDt;
	}

	public String informarPagamentoCustas(String numProcesso, String numGuia, String valorPago, String dataPagamento, String dataMovimentoBancario, UsuarioDt usuarioDt) throws Exception {
		String numeroProcesso = numProcesso.replaceAll("\\.", "");
		while (numeroProcesso.length() < 20)
			numeroProcesso = "0" + numeroProcesso;
		numeroProcesso = numeroProcesso.substring(0, 7) + "." + numeroProcesso.substring(7, 9) + "." + 
			numeroProcesso.substring(9, 13) + "." + numeroProcesso.substring(13, 14) + "." + 
			numeroProcesso.substring(14, 16) + "." + numeroProcesso.substring(16, 20);
		ProcessoDt processo = consultarProcessoNumeroCompleto(numeroProcesso, null);
		if(processo == null)
			return "<{Número de processo não encontrado.}>";
		GuiaEmissaoDt guia = consultarCustas(processo.getId());
		if(guia == null)
			throw new Exception("<{Processo não possui guia emitida.}>");
		String numeroGuia = numGuia.replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
		if(!guia.getNumeroGuiaCompleto().equals(numeroGuia))
			throw new Exception("<{A guia informada não pertence ao processo informado.}>");
//		FabricaConexao obFabricaConexao = null;
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			obFabricaConexao.iniciarTransacao();			
//
//			LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
//
//			obLog.salvar(logDt, obFabricaConexao);
//			obFabricaConexao.finalizarTransacao();
			return "";
//		} catch (Exception e) {
//			obFabricaConexao.cancelarTransacao();
//			throw e;
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
	}
	
	public byte[] visulizarAtestadoPenaCumprir(DataProvavelDt calculo, String diretorioProjeto) throws Exception{
		byte[] byTemp = null;
		if (calculo.getRelatorioByte() != null) {
			ByteArrayOutputStream out = null;
			
			try {
				out = new ByteArrayOutputStream();  
				out.write(calculo.getRelatorioByte());
	        	
	        	String textoPrimeiraLinhaPDF = "";
	        	String textoSegundaLinhaPDF = "Tribunal de Justiça do Estado de Goiás";
	        	String textoTerceiraLinhaPDF = "Válido apenas para consulta";
	        	String textoQuartaLinhaPDF = "";
				String pathImage = diretorioProjeto + "imagens" + File.separator + "TesteChave.gif";
				byTemp = EscreverTextoPDF.escreverTextoPDF(out.toByteArray(), pathImage, textoPrimeiraLinhaPDF, textoSegundaLinhaPDF, textoTerceiraLinhaPDF, textoQuartaLinhaPDF, false);
	        	out.close();
	        	out = null;
	        	
			} finally {
				try {if (out!=null) out.close(); } catch(Exception ex ) {};				
			}
		}
        return byTemp;
	}
	
	private void efetueGeracaoGuiaUnicaFazendaPublica(ProcessoCadastroDt processoCivelDt, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		String tipoGuia = GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA;
		if (isProcessoDa1aVaraDaFazendaPublicaMunicipalDeGoiania(processoCivelDt)) {
			tipoGuia = GuiaTipoDt.ID_PREFEITURA_AUTOMATICA;
		}
		guiaEmissaoNe.gerarGuiaFazendaPublicaAutomatica(processoCivelDt, String.valueOf(tipoGuia), null, obFabricaConexao);
	}
	
	private void efetueAlteracaoDeGuiaUnicaDeFazendaPublica(ProcessoDt processoCivelDt, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		String tipoGuia = GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA;
		if (isProcessoDa1aVaraDaFazendaPublicaMunicipalDeGoiania(processoCivelDt)) {
			tipoGuia = GuiaTipoDt.ID_PREFEITURA_AUTOMATICA;
		}
		GuiaEmissaoDt ultimaGuiaEmitida = guiaEmissaoNe.consultarUltimaGuiaEmissao(processoCivelDt.getId(), String.valueOf(tipoGuia), obFabricaConexao);
		if (ultimaGuiaEmitida == null || 
			ultimaGuiaEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.GUIA_GERADA_ENVIAR_PREFEITURA)) ||
			ultimaGuiaEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) ||
			ultimaGuiaEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)) ||
			ultimaGuiaEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.GUIA_ESTORNADA_PREFEITURA)) ||			
			ultimaGuiaEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA))) {
			if (ultimaGuiaEmitida != null && !ultimaGuiaEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)))
				guiaEmissaoNe.cancelarGuiaEmitidaFazendaPublicaAutomatica(processoCivelDt, ultimaGuiaEmitida, String.valueOf(tipoGuia));
			guiaEmissaoNe.gerarGuiaFazendaPublicaAutomatica(processoCivelDt, String.valueOf(tipoGuia), null, obFabricaConexao);
		} else {
			GuiaEmissaoDt guiaRecalculada = guiaEmissaoNe.gerarGuiaFazendaPublicaAutomaticaSemSalvar(processoCivelDt, String.valueOf(tipoGuia), null);
			if (guiaRecalculada != null && 
				Funcoes.StringToDouble(guiaRecalculada.getValorTotalGuia()) > Funcoes.StringToDouble(ultimaGuiaEmitida.getValorTotalGuia())) {
				GuiaEmissaoDt ultimaGuiaFinalEmitida = guiaEmissaoNe.consultarUltimaGuiaEmissaoTipoFinal(processoCivelDt.getId(), obFabricaConexao);
				if (ultimaGuiaFinalEmitida != null && !ultimaGuiaFinalEmitida.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)))
					guiaEmissaoNe.cancelarGuiaEmitida(ultimaGuiaFinalEmitida);
			}
		}
	}
	
	public List consultarProcessosFazendaPublicaSemGuiaUnica() throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			// Consulta os processos
			listaProcessos = this.consultarProcessosFazendaPublicaSemGuiaUnica(obFabricaConexao);			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	public List consultarProcessosFazendaPublicaSemGuiaUnica(FabricaConexao obFabricaConexao) throws Exception {
		List listaProcessos;
		ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());

		// Consulta os processos
		listaProcessos = obPersistencia.consultarProcessosFazendaPublicaSemGuiaUnica(); 
		return listaProcessos;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List<String[]> consultarVotoEmentaPendentesProcessoHash(String id_Processo, UsuarioNe usuarioSessao) throws Exception {
		List<String[]> retornoConclusoesPendentes = new ArrayList<String[]>();
		PendenciaNe neObjeto = new PendenciaNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		String nomeResponsavel = "";
		String[] votoEmentaPendente = null;
		List conclusoes = neObjeto.consultarVotoEmentaAbertasHash(id_Processo, usuarioSessao);
		if(conclusoes != null){
			Iterator iteratorVotoEmenta = conclusoes.iterator();
    		while (iteratorVotoEmenta.hasNext()) {
    			votoEmentaPendente = (String[])iteratorVotoEmenta.next();
    			String idPendencia = votoEmentaPendente[0].split("@#!@")[0];
    			
    			List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(idPendencia, null);		    	
		    	if (responsaveis != null){		    		
		    		Iterator iteratorResponsavel = responsaveis.iterator();
		    		while (iteratorResponsavel.hasNext()) {
		    			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
		    			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DESEMBARGADOR)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.JUIZ_1_GRAU)))){
		    				nomeResponsavel = dados.getServentiaCargo() + "-" + dados.getNomeUsuarioServentiaCargo();
		    				break;
		    			} else {
		    				nomeResponsavel = votoEmentaPendente[1].replace("Concluso -", "");
		    			}
		    		}
		    	}
		    	
		    	AudienciaProcessoDt audienciaProcessoDt = null;
				if (Funcoes.StringToInt(votoEmentaPendente[6]) == PendenciaTipoDt.CONCLUSO_VOTO) {
					votoEmentaPendente[1] = "Texto do Relatório e Voto";
					audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeVoto(idPendencia);					
				} else if (Funcoes.StringToInt(votoEmentaPendente[6]) == PendenciaTipoDt.CONCLUSO_EMENTA) {
					votoEmentaPendente[1] = "Texto da Ementa";
					audienciaProcessoDt = audienciaProcessoNe.consultarCompletoPelaPendenciaDeEmenta(idPendencia);					
				}
				
				if (audienciaProcessoDt != null && audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0) {
					votoEmentaPendente[1] += " - " + audienciaProcessoDt.getProcessoTipo();
					if (votoEmentaPendente[8].equalsIgnoreCase("[SEM_SESSAO]")) {
						if (audienciaProcessoDt.isSessaoVirtual()) {
							votoEmentaPendente[8] = "[VIRTUAL]";						
						} else {
							votoEmentaPendente[8] = "[PRESENCIAL]";
						}							
					}
					votoEmentaPendente[1] += " - " + votoEmentaPendente[8];
				}
		    	
		    	retornoConclusoesPendentes.add(new String[] { votoEmentaPendente[0], votoEmentaPendente[1], votoEmentaPendente[2], votoEmentaPendente[3], votoEmentaPendente[4] , nomeResponsavel, votoEmentaPendente[5], votoEmentaPendente[7], votoEmentaPendente[8]});
    		}				   					
		}
		neObjeto = null;
		pendenciaResponsavelNe = null;
		return retornoConclusoesPendentes;
	}

	public String consultarDescricaoArquivoTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception { 
		String stTemp = "";
		ArquivoTipoNe ne = new ArquivoTipoNe(); 
		stTemp = ne.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}
	
	/**
	 * Método para alterar a classe(processo tipo) e/ou valor da causa do processo quando a guia complementar é paga.
	 * 
	 * @param ProcessoDt processoDt
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean alterarProcessoTipoValorCausa(ProcessoDt processoDt, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		
		if( processoDt != null && processoDt.getId() != null ) {
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.alterarProcessoTipoValorCausa(processoDt);
			
			String valorNovoLog = processoDt.getPropriedades();
			if( retorno ) {
				valorNovoLog += ";ATUALIZADO_VIA_GUIA_COMPLEMENTAR_DESTE_PROCESSO]";
				valorNovoLog = valorNovoLog.replace("];", ";");
			}
			
			LogDt obLogDt = new LogDt("Processo", processoDt.getId(), UsuarioDt.SistemaProjudi, processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), valorNovoLog);
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		
		return retorno;
	}
	
	/**
	 * Método que verifica se um usuário pode baixar um arquivo, e caso possa efetua o download
	 * OBS: Este metodo restringue esta camada de negocio ao servlet
	 * @author Márcio Mendonça Gomes
	 * @param String numeroProcessoCompleto, número completo do processo
	 * @param HttpServletResponse response, response do servlet
	 * @throws Exception
	 */
	public void baixarArquivoCompletoObjectStorageDigitalizacao(String numeroProcessoCompleto, HttpServletResponse response) throws Exception{		
		new ArquivoNe().baixarArquivoCompletoObjectStorageDigitalizacao(numeroProcessoCompleto, response);
	}
	
	/**
	 * @author Márcio Mendonça Gomes
	 * @param String numeroProcessoCompleto, número do processo
	 * @param String nomeCompletoDoArquivo, nome completo do processo
	 * @param HttpServletResponse response, response do servlet
	 * @throws Exception
	 */
	public void baixarArquivoObjectStorageDigitalizacao(String numeroProcessoCompleto, String nomeCompletoDoArquivo, HttpServletResponse response) throws Exception{		
		new ArquivoNe().baixarArquivoObjectStorageDigitalizacao(numeroProcessoCompleto, nomeCompletoDoArquivo, response);
	}
	
	/**
	 * Método que consulta a quantidade de processsos ativos vinculados a um classificador.
	 * @param idClassificador 
	 * @return quantidade de processos localizados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public int consultarQuantidadeProcessosAtivosClassificador(String idClassificador) throws Exception {
		int quantidade;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			quantidade = obPersistencia.consultarQuantidadeProcessosAtivosClassificador(idClassificador);			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return quantidade;
		
	}
	
	public int consultarQuantidadeProcessosArquivadosClassificador(String idClassificador) throws Exception {
		int quantidade;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new  ProcessoPs(obFabricaConexao.getConexao());
			quantidade = obPersistencia.consultarQuantidadeProcessosArquivadosClassificador(idClassificador);			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return quantidade;
		
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
    
    /**
     * Retorna a serventia relacionada do tipo Preprocessual
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author mmgomes
     * 
     * @throws Exception 
     */
    public ServentiaDt consultarServentiaOrigemMovimentacaoAudienciaConciliacaoEMediacaoCEJUSC(String id_Processo) throws Exception{
        return new MovimentacaoNe().consultarServentiaOrigemMovimentacaoAudienciaConciliacaoEMediacaoCEJUSC(id_Processo);
    }
    
    /**
	 * Verifica se o processo está em uma serventia que possua um dos subTipos que não se aplica a contagem de prazo do novo CPC
	 * 
	 * @param id_Processo, identificação do processo
	 * @param conexao,
	 *            conexão com o banco de dados
	 * 
	 * @author lsbernardes
	 */
    public boolean isUtilizaPrazoCorrido(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.isUtilizaPrazoCorrido(id_processo);			
		return retorno;	
    }
    
    /**
   	 * Verifica se o processo está em uma serventia que possua subTipo vara de precatorias
   	 * 
   	 * @param id_Processo, identificação do processo
   	 * @param conexao,
   	 *            conexão com o banco de dados
   	 * 
   	 * @author lsbernardes
   	 */
       public boolean isProcessoVaraPrecatoria(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
	   		boolean retorno = false;
	   		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
	   		if (id_processo != null && id_processo.length()>0)
	   			retorno = obPersistencia.isProcessoVaraPrecatoria(id_processo);
	   		
	   		return retorno;	
       }
       
      /**
   	 * Consulta os dados para montar o número completo do processo
   	 * @param numero
   	 * @param digito
   	 * @param ano
   	 * @return
   	 * @throws Exception
   	 */
   	public String consultarNumeroCompletoDoProcesso(String numero, String digito, String ano) throws Exception {
   		String numeroCompleto = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			numeroCompleto = obPersistencia.consultarNumeroCompletoDoProcesso(numero, digito, ano);			

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return numeroCompleto;
   	}
   	
   	public ProcessoFisicoDt consultarProcessoFisicoNumeroCompleto(String numeroProcessoCompleto) throws Exception {
   		return new ProcessoFisicoNe().getProcessoFisicoDadosGerais(Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto));
   	}
   	
   	/**
	 * Método responsável por consultar a serventia a partir do serventiaCodigo
	 * 
	 * @param serventiaCodigo
	 *            - código da Serventia
	 * @author Márcio Gomes
	 * @throws Exception
	 */
	public ServentiaDt consultarServentiaCodigo(String serventiaCodigo) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		try {
			return serventiaNe.consultarServentiaCodigo(serventiaCodigo);
		} finally {
			serventiaNe = null;
		}
	}
	
	/**
	 * Método responsável por consultar a lista de arquivos completos. 
	 * 
	 * @param numeroCompletoDoProcesso
	 * @return
	 * @throws Exception 
	 */
	public List<ArquivoDt> consultarArquivosCompletosFisicos(String numeroCompletoDoProcesso) throws Exception {
		return new ArquivoNe().consultarArquivosCompletosFisicos(numeroCompletoDoProcesso);
	}
	
	/**
	 * Consulta os ids de processos que foram vinculados à guias iniciais do SPG
	 * @return
	 * @throws Exception
	 */
   	public List<String> consultarIdsProcessosGuiaInicialSPG() throws Exception {
   		List<String> ids = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			ids = obPersistencia.consultarIdsProcessosGuiaInicialSPG();			

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return ids;
   	}
   	
   	/**
   	 * Consulta os dados do processo de um arquivo publicado (pend_final)
   	 * @param id_arquivo
   	 * @return
   	 * @throws Exception
   	 */
   	public ProcessoDt consultarPorIdArquivo(String id_arquivo) throws Exception {
   		ProcessoDt dtRetorno = null;
   		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarPorIdArquivo(id_arquivo);			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
   	}
   	
	public List<String> consultarIdsProcessosAtivos() throws Exception {
   		List<String> ids = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			ids = obPersistencia.consultarIdsProcessosAtivos();			

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return ids;
   	}

	public boolean verificaPermissaoVisualizarArquivo(String id_MovimentacaoArquivo, UsuarioDt usuarioDt, ProcessoDt processoDt, boolean acessoOutraServentia) throws Exception {
		boolean retorno = false;
		MovimentacaoArquivoDt movimentacaoArquivoDt = new MovimentacaoArquivoNe().consultarIdCompleto(id_MovimentacaoArquivo);
		retorno = podeBaixarArquivo(usuarioDt, processoDt, movimentacaoArquivoDt);
		return retorno;
	}
   	
	public List<String> consultarIdsProcessos(String id_Serventia, String id_Classificador, FabricaConexao obFabricaConexao) throws Exception, MensagemException {
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarIdsProcessos(id_Serventia, id_Classificador);	
	}

	public String getPrioridade(String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		String dtRetorno = null;
  		
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.getPrioridade(id_processo);			

		return dtRetorno;
	}
	
	/**
	 * Método responsável em consultar seo advogado tem parte no processo.
	 * 
	 * @param processoNumero
	 * @param id_UsuarioServentia
	 * 
	 * @author tamaralsantos
	 */
	public Boolean verificarAdvogadoParteProcesso(String idProcesso, String id_UsuarioServentia, String id_ServentiaUsuario) throws Exception {

		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.verificarAdvogadoParteProcesso(idProcesso, id_UsuarioServentia, id_ServentiaUsuario);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	
	/**
	 * Método responsável em consultar seo promotor tem parte no processo.
	 * 
	 * @param processoNumero
	 * @param id_UsuarioServentia
	 * 
	 * @author tamaralsantos
	 */
	public Boolean verificarPromotorParteProcesso(String idProcesso, String id_ServCargo) throws Exception {

		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.verificarPromotorParteProcesso(idProcesso, id_ServCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	
	/**
	 * Verifica se é o processo é da serventia do usuário interno/externo
	 * @param UsuarioDt, usuarioDt usuario logado
	 * @param processoDt, processo.
	 * @throws Exception 
	 */
	public Boolean podeAcessarProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, FabricaConexao conexao) throws Exception{

		boolean mostraProcesso = false;
		
		FabricaConexao obFabricaConexao = null;
		if (conexao==null) {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		}else {
			obFabricaConexao = conexao;
		}
		try {
			
    		int grupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
    		
    						
    		switch (grupoCodigo) {
    			case GrupoDt.AUTORIDADES_POLICIAIS:
    				//Pode ver processo sigiloso da sua serventia Magistrado/MP/Delegacia
    				mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoDt.getId_ServentiaOrigem());
    				
    				if (!mostraProcesso && !processoDt.isSigiloso()){
    					mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());
    				}
    				
    				break;
    			case GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				
    				mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoDt.getId_Serventia());
    				
    				//Se processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					ProcessoDt processoPrincipalDt = consultarId(processoDt.getId_ProcessoPrincipal());
    					mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoPrincipalDt.getId_Serventia());
    				}
    				
    				//Verifica se os processos dependentes podem ser acessados.
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteServentia(processoDt.getId_Processo(), usuarioDt.getId_Serventia());
    				}
    				
    				break;
    		
    			case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
    			case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL:
    			case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
    			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
    			case GrupoDt.ANALISTA_PRE_PROCESSUAL:
    			case GrupoDt.ANALISTAS_EXECUCAO_PENAL:
    			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
    			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
    			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
    			case GrupoDt.TECNICO_EXECUCAO_PENAL:
    			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
    			case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
    			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
    			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
    			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:				
    			case GrupoDt.DISTRIBUIDOR_CAMARA:	
    			case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL:
    			case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL:
    			case GrupoDt.CONSULTORES:
    				//houver pendencia aberta de processo de outra serventia o acesso fica liberado    				
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());
    				
    				//não pode ver processo sigiloso
    				if (!mostraProcesso && processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				
    				if(!mostraProcesso){								
    					mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoDt.getId_Serventia());	
    				}else {
    					break;
    				}
    				
    				//Verifica se os processos atual tem um processo dependete da serventia do usuario que está fazendo o acesso
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteServentia(processoDt.getId_Processo(), usuarioDt.getId_Serventia());
    				}else {
    					break;
    				}
    				
    				//Se solicita acesso e é liberado (pode baixar arquivo?).
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				//Verifica se os processos esta em recurso e a serventia de origem é a do usuario.
    				if(!mostraProcesso){
    					mostraProcesso = new RecursoNe().isExisteRecurso(processoDt.getId_Processo(), usuarioDt.getId_Serventia());
    				}else {
    					break;
    				}
    				
    				//Se processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					ProcessoDt processoPrincipalDt = consultarId(processoDt.getId_ProcessoPrincipal());
    					mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoPrincipalDt.getId_Serventia());
    				}
    				
    				if (!mostraProcesso && grupoCodigo==GrupoDt.ANALISTA_PRE_PROCESSUAL ){
    					AudienciaNe audi = new AudienciaNe();
    					mostraProcesso = audi.temAudienciaProcessoServentiaPendente(processoDt.getId_Processo(),usuarioDt.getId_Serventia());					
    				}
    				
    				break;
    			case GrupoDt.CONCILIADORES_VARA:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoDt.getId_Serventia());
    				    				
    				//Verifica se os processos atual tem um processo dependete da serventia do usuario que está fazendo o acesso
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteServentia(processoDt.getId_Processo(), usuarioDt.getId_Serventia());
    				}else {
    					break;
    				}
    				
    				//Se solicita acesso e é liberado (pode baixar arquivo?).
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				//Se processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					ProcessoDt processoPrincipalDt = consultarId(processoDt.getId_ProcessoPrincipal());
    					mostraProcesso = usuarioDt.getId_Serventia().equalsIgnoreCase(processoPrincipalDt.getId_Serventia());
    				}
    				
    				//se for conciliador da UPJ 
    				if(!mostraProcesso && usuarioDt.isGabineteUPJ()){
    					mostraProcesso = new ProcessoPs(obFabricaConexao.getConexao()).isGabineteUpjResposavelProcesso(usuarioDt.getId_Serventia(), processoDt.getId());
    				}
    				
    				break;
    			case GrupoDt.ANALISTA_FORENSE:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());  
    				
    				if(!mostraProcesso ){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    			
    			case GrupoDt.CONSULTOR_SISTEMAS_EXTERNOS:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());  
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    			
    			case GrupoDt.ANALISTA_FORENSE_2_GRAU:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}				
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    				
    			case GrupoDt.CONTADORES_VARA:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    									
    			case GrupoDt.DESEMBARGADOR:
    			case GrupoDt.JUIZ_EXECUCAO_PENAL:
    			case GrupoDt.JUIZES_TURMA_RECURSAL:
    			case GrupoDt.JUIZES_VARA:
    			case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
    			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
    			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
    				/*
    				 * O acesso a processo deve se dar quando:
    				 * 0 - Quando não for sigiloso, nesse somente o magistrado e quem criou o processo (mp/delegacia) terão acesso;
    				 * 1 - o usuário é responsável pelo processo;
    				 * 2 - o usuário é responsável por uma conclusão do processo;
    				 * 3 - o processo é dependente de outro e nesse caso a regra 1 e 2 se aplica;
    				 * 4 - o processo é principal de outro e nesse caso a regra 1 e 2 se aplica; 
    				 */
    				//Pode ver processo sigiloso da sua serventia Magistrado/MP/Delegacia
    				mostraProcesso = new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId());
    				
    				//não pode ver processo sigiloso se não for responsável
    				if (!mostraProcesso && processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				//verifica se o usuário tem alguma conclusão no processo
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargo(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				//Se for um processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					mostraProcesso = new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId_ProcessoPrincipal());
    					if (!mostraProcesso){
    						mostraProcesso = podeAcessarProcessoDependenteSeResponsavel(usuarioDt.getId_ServentiaCargo(), processoDt.getId_ProcessoPrincipal());
    					}else {
        					break;
        				}
    					if (!mostraProcesso){
    						mostraProcesso = new PendenciaNe().isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargo(), processoDt.getId_ProcessoPrincipal());
    					}else {
        					break;
        				}
    				}
    				
    				//Verifica se em algum dos processos dependentes o usuário tem acesso.
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavel(usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    				//verificar se existe alguma conclusão nos processos depententes				
    				if (!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavelConclusao( usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().temPendenciaAbertaVotoSessaoVirtual(usuarioDt.getId_ServentiaCargo(), processoDt.getId());
    				}
    				
    				break;
    				
    			case GrupoDt.ASSESSOR:
    			case GrupoDt.ASSESSOR_DESEMBARGADOR:
    			case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
    			case GrupoDt.ASSESSOR_JUIZES_VARA:
    			case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				//1º Verificar se o chefe é responsável pelo processo
    				mostraProcesso =  new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId());
    				
    				//verifica se o usuário chefe tem alguma conclusão no processo
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				//Se for um processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					//1º Verificar se o chefe é responsável pelo processo
    					mostraProcesso = new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId_ProcessoPrincipal());					
    					if (!mostraProcesso){
    						mostraProcesso = podeAcessarProcessoDependenteSeResponsavel(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId_ProcessoPrincipal());
    					}else {
        					break;
        				}
    					if (!mostraProcesso){
    						mostraProcesso = new PendenciaNe().isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId_ProcessoPrincipal());
    					}else {
        					break;
        				}
    				}
    				
    				//Verifica se em algum dos processos dependentes o usuário chefe tem acesso.
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavel(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    				
    				//verificar se existe alguma conclusão nos processos depententes 				
    				if (!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavelConclusao( usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentiaChefe(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().temPendenciaAbertaVotoSessaoVirtual(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId());
    				}
    				
    				break;
    			case GrupoDt.ANALISTA_FINANCEIRO:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    			case GrupoDt.ASSISTENTE_GABINETE_FLUXO:
    			case GrupoDt.ASSISTENTE_GABINETE:
    				
    				//Pode ver processo se for responsável
    				mostraProcesso = new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId());
    				
    				//houver pendencia aberta de processo de outra serventia o acesso fica liberado    				
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());
    				
    				//verifica se o usuário tem alguma conclusão no processo
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargo(), processoDt.getId());
    				}else {
    					break;
    				}
    				    				
    				//não pode ver processo sigiloso
    				if (!mostraProcesso && processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				
    				//Se for um processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					mostraProcesso = new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), processoDt.getId_ProcessoPrincipal());
    					if (!mostraProcesso){
    						mostraProcesso = podeAcessarProcessoDependenteSeResponsavel(usuarioDt.getId_ServentiaCargo(), processoDt.getId_ProcessoPrincipal());
    					}else {
        					break;
        				}
    					if (!mostraProcesso){
    						mostraProcesso = new PendenciaNe().isResponsavelConclusoesPendentes(usuarioDt.getId_ServentiaCargo(), processoDt.getId_ProcessoPrincipal());
    					}else {
        					break;
        				}
    				}
    				
    				//Verifica se em algum dos processos dependentes o usuário tem acesso.
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavel(usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    				if (!mostraProcesso){
    					//verificar se existe alguma conclusão nos processos depententes 
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavelConclusao( usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().temPendenciaAbertaVotoSessaoVirtual(usuarioDt.getId_ServentiaCargo(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				//se for distribuidor de gabinete da UPJ 
    				if(!mostraProcesso && usuarioDt.isGabineteUPJ()){
    					mostraProcesso = new ProcessoPs(obFabricaConexao.getConexao()).isGabineteUpjResposavelProcesso(usuarioDt.getId_Serventia(), processoDt.getId());
    				}
    				
    				break;
    				
    			case GrupoDt.DISTRIBUIDOR_GABINETE:
    				
    				mostraProcesso = new PendenciaNe().isServentiaResponsavelConclusoesPendentes(usuarioDt.getId_Serventia(), processoDt.getId());
    				
    				//não pode ver processo sigiloso
    				if (!mostraProcesso && processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}				
    				
    				if(!mostraProcesso){
    					mostraProcesso = new MovimentacaoArquivoNe().validarAcessoArquivoDistribuidorGabinete(usuarioDt, processoDt);
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso ){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				
    				//se for distribuidor de gabinete da UPJ 
    				if(!mostraProcesso && usuarioDt.isGabineteUPJ()){
    					mostraProcesso = new ProcessoPs(obFabricaConexao.getConexao()).isGabineteUpjResposavelProcesso(usuarioDt.getId_Serventia(), processoDt.getId());
    				}
    				
    				break;
    			
    			case GrupoDt.ESTAGIARIO_GABINETE:
    			case GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU:
    			case GrupoDt.ESTAGIARIO_SEGUNDO_GRAU:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}				
    				mostraProcesso = new PendenciaNe().isServentiaResponsavelConclusoesPendentes(usuarioDt.getId_Serventia(), processoDt.getId());
    				
    				break;
    				
    			case GrupoDt.MINISTERIO_PUBLICO:
    			case GrupoDt.MP_TCE:
    
    				mostraProcesso = this.verificarPromotorParteProcesso(processoDt.getId(), usuarioDt.getId_ServentiaCargo());
    				
    				//não pode ver processo sigiloso se não for responsável
    				if (!mostraProcesso && processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				    				
    				//Verifica se os processos dependentes podem ser acessados.
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeResponsavel( usuarioDt.getId_ServentiaCargo(), processoDt.getId_Processo());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				//Se processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					mostraProcesso = this.verificarPromotorParteProcesso(processoDt.getId_ProcessoPrincipal(), usuarioDt.getId_ServentiaCargo());					
    				}
    				
    				break;
    				
    			case GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL:
    			case GrupoDt.ADVOGADO_PUBLICO_ESTADUAL:
    			case GrupoDt.ADVOGADO_PUBLICO:
    			case GrupoDt.ADVOGADO_PUBLICO_UNIAO:
    			case GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO:
    			case GrupoDt.ADVOGADO_PARTICULAR:
    			case GrupoDt.ADVOGADO_DEFENSOR_PUBLICO:
    				
    				mostraProcesso = this.verificarAdvogadoParteProcesso(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), null);
    				
    				//não pode ver processo sigiloso
    				if (!mostraProcesso && processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				    				    				
    				//Verifica se os processos dependentes podem ser acessados.
    				if(!mostraProcesso){
    					mostraProcesso = podeAcessarProcessoDependenteSeTemParte(processoDt.getId_Processo(), usuarioDt.getId_UsuarioServentia());
    				}else {
    					break;
    				}
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}else {
    					break;
    				}
    				//Se processo Dependente verifica permissão para acessar processo principal.
    				if(!mostraProcesso && processoDt.isDependente()){
    					mostraProcesso = this.verificarAdvogadoParteProcesso(processoDt.getId_ProcessoPrincipal(), usuarioDt.getId_UsuarioServentia(), null);									
    				}
    				
    				break;
    				
    			case GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA:
    			case GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL:
    			case GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL:
    			case GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL:
    			case GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO:
    			case GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA:
    				//não pode ver processo sigiloso				
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = this.verificarAdvogadoParteProcesso(processoDt.getId(), null, usuarioDt.getId_Serventia());
    				
    				if(!mostraProcesso){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    				
    			case GrupoDt.COORDENADOR_PROMOTORIA:
    				//não pode ver processo sigiloso				
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}				
    				mostraProcesso = this.verificarPromotoriaParteProcesso(processoDt.getId(), usuarioDt.getId_Serventia());
    				
    				if(!mostraProcesso && !processoDt.isSigiloso()){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    				
    			case GrupoDt.ASSESSOR_ADVOGADOS:
    			case GrupoDt.ASSESSOR_MP:
    				//não pode ver processo sigiloso				
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = new ProcessoParteAdvogadoNe().isAdvogadoProcesso(usuarioDt.getId_UsuarioServentiaChefe(), processoDt.getId()) || new ProcessoResponsavelNe().isResponsavelProcesso(usuarioDt.getId_ServentiaCargoUsuarioChefe(), processoDt.getId());
    				
    				//Validando se o chefe do assessor possui serventia cargo. Caso possua, o usuário é assessor de promotor e precisa passar o Id_Serventia_Cargo_Chefe. 
    				//Caso não possua, é assessor de advogado e precisa passar o Id_Usuario_Serventia_Chefe
    				if(!mostraProcesso){
    					if(usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")){
    						mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentiaChefe(), processoDt.getId());
    					} else {
    						mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentiaChefe(), processoDt.getId());
    					}
    				}
    				
    				break;
    				
    			case GrupoDt.OFICIAL_JUSTICA:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = new PendenciaNe().temPendenciaAberta(usuarioDt.getId_Serventia(), processoDt.getId(), usuarioDt.getId_ServentiaCargo());  
    				
    				if(!mostraProcesso ){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				
    				break;
    				
    			case GrupoDt.PUBLICO:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = usuarioDt.isValidoAcessoProcesso(processoDt.getId()); 
    				break;
    				
    			case GrupoDt.PARTES:
    				//não pode ver processo sigiloso
    				if (processoDt.isSigiloso()){
    					throw new MensagemException("Este processo é sigiloso e seu acesso é restrito aos magistrados responsáveis, Ministério Público ou Autoridades Policiais.");
    				}
    				mostraProcesso = new ProcessoParteNe().isParteProcesso(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				break;																	
    			default:
    				if (!processoDt.isSigiloso()){
    					mostraProcesso = new PendenciaNe().isAcessoLiberado(usuarioDt.getId_UsuarioServentia(), processoDt.getId());
    				}
    				break;
    		}
		}finally{
			if (conexao==null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return mostraProcesso;
		
	}
	
	private boolean podeAcessarProcessoDependenteServentia(String idProcessoPrincipal, String idServentiaUsuario) throws Exception {
		List dependentes = consultarProcessosDependentes(idProcessoPrincipal);
		if (dependentes != null) {
			for(int i = 0; i < dependentes.size(); i++) {
				ProcessoDt processoDependenteDt = (ProcessoDt) dependentes.get(i);
				if(idServentiaUsuario.equalsIgnoreCase(processoDependenteDt.getId_Serventia())){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean podeAcessarProcessoDependenteSeResponsavel( String idServentiaCargo, String idProcessoPrincipal) throws Exception {
		List dependentes = consultarProcessosDependentes(idProcessoPrincipal);
		if (dependentes != null) {
			for(int i = 0; i < dependentes.size(); i++) {
				ProcessoDt processoDependenteDt = (ProcessoDt) dependentes.get(i);
				if(new ProcessoResponsavelNe().isResponsavelProcesso(idServentiaCargo, processoDependenteDt.getId())){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean podeAcessarProcessoDependenteSeResponsavelConclusao( String idServentiaCargo, String idProcessoPrincipal) throws Exception {
		List dependentes = consultarProcessosDependentes(idProcessoPrincipal);
		if (dependentes != null) {
			for(int i = 0; i < dependentes.size(); i++) {
				ProcessoDt processoDependenteDt = (ProcessoDt) dependentes.get(i);
				if(new PendenciaNe().isResponsavelConclusoesPendentes(idServentiaCargo, processoDependenteDt.getId())){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean podeAcessarProcessoDependenteSeTemParte(String idProcessoPrincipal, String idUsuarioServentia) throws Exception {
		List dependentes = consultarProcessosDependentes(idProcessoPrincipal);
		if (dependentes != null) {
			for(int i = 0; i < dependentes.size(); i++) {
				ProcessoDt processoDependenteDt = (ProcessoDt) dependentes.get(i);
				if(verificarAdvogadoParteProcesso(idProcessoPrincipal, idUsuarioServentia, null)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Verifica se o processo está com status SUSPENSO
	 * @param id_Processo
	 * @return
	 * @throws Exception
	 */
	public boolean isProcessoSuspenso(String id_Processo) throws Exception {
		boolean boTemp = false;
        FabricaConexao obFabricaConexao = null;         
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
            boTemp = obPersistencia.isProcessoSuspenso(id_Processo);                             
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return boTemp;
	}
	
	/**
	 * 
	 * @param id_Processo
	 * @param logDt
	 * @param id_UsuarioServentia
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void sobrestarProcesso(String id_Processo, LogDt logDt, String id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Muda status do processo para suspenso
		int novoStatus = ProcessoStatusDt.SUSPENSO;

		String valorAtual = "[Id_Processo:" + id_Processo + "]";

		// Atualiza dados do processo e gera log da suspensão
		LogDt obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.SuspensaoProcesso), valorAtual, "");
		obPersistencia.alterarProcessoSuspensao(id_Processo, novoStatus);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO SUSPENSO
		movimentacaoNe.gerarMovimentacaoProcessoSuspenso(id_Processo, id_UsuarioServentia, "Processo Suspenso ou Sobrestado por Recurso Repetitivo / Recurso de Repercussão Geral", logDt, obFabricaConexao);
	}
	
	/**
     * Consulta processos (utilizando filtro por número de processo)
     */
    public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
    	
        String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }


	public String consultarNotas(String id_proc, String id_usuarioServentia, String id_Serventia) throws Exception {
		return  (new ProcessoNotaNe()).consultarNotasJSON(id_proc, id_usuarioServentia, id_Serventia);		
	}

	public void excluirNota(String id_nota, String id_UsuarioServentia, String isPrivada, String id_Serventia) throws Exception {
		(new ProcessoNotaNe()).excluirNota(id_nota, id_UsuarioServentia, isPrivada, id_Serventia);		
	}
	
	public void salvarNota(ProcessoNotaDt notaDt) throws Exception {
		(new ProcessoNotaNe()).salvar(notaDt);		
	}

	public boolean validaDependencia(ProcessoDt processoDt, ProcessoDt processoPrincipalDt) throws Exception {	    
			ProcessoDt processoTempDt = processoPrincipalDt;
			List<String> cadeiaDeProcessos = new ArrayList<String>();
			int cont = 0; 
			int i;

	   		while (true) {
	    		if (processoTempDt.isDependente()) {
	    			if (processoTempDt.getId_ProcessoPrincipal().equals(processoDt.getId_Processo()))
	    				return false;
	                
	    			if (cont != 0)
	    				for (i = 0;i < cont;i++)
	    					if (processoTempDt.getId_ProcessoPrincipal().equals(cadeiaDeProcessos.get(i))) {
	    						throw new MensagemException("Dependência circular do processo " + Funcoes.formataNumeroProcesso(processoPrincipalDt.getProcessoNumero()) + " com o processo " + 
	    						  Funcoes.formataNumeroProcesso(processoTempDt.getProcessoNumero()));
	    					}
	    			
	    			cadeiaDeProcessos.add(cont++, processoTempDt.getId_Processo());
	    			processoTempDt = this.consultarIdSimples(processoTempDt.getId_ProcessoPrincipal());
	    		} else return true;
			}	    
	}
	
	public String consultarAreasDistribuicaoSegundoGrauCivelJSON(String stNomeBusca1, String id_Comarca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, id_Comarca,  posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}

	public String consultarAreasDistribuicaoSegundoGrauCivelJSON(String stNomeBusca1, String id_Comarca, String posicaoPaginaAtual, boolean turmaJulgadora) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, id_Comarca,  posicaoPaginaAtual, turmaJulgadora);
		neObjeto = null;
		return stTemp;
	}
    /**
	 * Método que efetua o retorno de um processo encaminhado à serventia de origem.
	 * 
	 * @param processoDt, processo que está sendo retornado à serventia de origem
	 * @param id_Movimentacao, movimentação que gerou o retorno à origem
	 * @param usuarioDt, usuário que está devolvendo processo à origem
	 * @param arquivos, lista de arquivos a ser vinculado com pendência "Verificar Processo" no 1º grau
	 * @param logDt, dados do log
	 * @param obFabricaConexao, conexão ativa
	 * 
	 * @author hrrosa
	 */
	
	public void salvarRetornoServentiaOrigemEncaminhamentoAutomaticoCEJUSC(ProcessoDt processoDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, boolean arquivar, FabricaConexao obFabricaConexao) throws Exception {
		this.salvarRetornoServentiaOrigemEncaminhamento(processoDt, usuarioDt, arquivos, logDt, arquivar, false, true, obFabricaConexao);
	}
	
	public void salvarRetornoServentiaOrigemEncaminhamento(ProcessoDt processoDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, boolean arquivar, FabricaConexao obFabricaConexao) throws Exception {
		this.salvarRetornoServentiaOrigemEncaminhamento(processoDt, usuarioDt, arquivos, logDt, arquivar, false, false, obFabricaConexao);
	}
	
	public void salvarRetornoServentiaOrigemEncaminhamento(ProcessoDt processoDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, boolean arquivar, boolean isApensoFilho, boolean isRetornoAutomaticoCejusc, FabricaConexao obFabricaConexao) throws Exception {
		
		ServentiaNe serventiaNe = new ServentiaNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();		
		String complemento = "";
		ServentiaCargoDt serventiaCargoDt = null;		
		ServentiaDt serventiaNovaDt = null;
		String idServCargo = null;
		
		ProcessoEncaminhamentoNe procEncaminhamentoNe = new ProcessoEncaminhamentoNe();
		ProcessoEncaminhamentoDt procEncaminhamentoDt = null;
		MovimentacaoDt movimentacaoDt = null;
		
		String mensagemErro = "";
		
		processoDt.setId_UsuarioLog(usuarioDt.getId());
    	processoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		
		mensagemErro = this.podeRetornarServentiaOrigemEncaminhamento(processoDt, usuarioDt.getGrupoCodigo(), isApensoFilho, obFabricaConexao);
	
		if (mensagemErro!= null && mensagemErro.length() > 0){
			throw new MensagemException(mensagemErro);
		}		
		
		//Se for um retorno realizado pelo usuário Sistema Projudi, não verifica se ele está logado
		//na mesma serventia para a qual o processo foi encaminhado. Condição adicionada para atender
		//ao retorno automático de processos feito para o 7º Cejusc
		if(usuarioDt != null && usuarioDt.getId().equals(UsuarioDt.SistemaProjudi)){
			serventiaNovaDt = serventiaNe.consultarId(procEncaminhamentoNe.retornaServentiaDevolucaoEncaminhamento(processoDt.getId(), null, obFabricaConexao), obFabricaConexao);
		}
		else {
			serventiaNovaDt = serventiaNe.consultarId(procEncaminhamentoNe.retornaServentiaDevolucaoEncaminhamento(processoDt.getId(), usuarioDt.getId_Serventia(), obFabricaConexao), obFabricaConexao);
		}
			
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		// Ao redistribuir os responsáveis pelo processo devem ser desativados
		processoResponsavelNe.desativarResponsaveisProcesso(processoDt, obFabricaConexao);
		
		// Localiza e reativa o responsável pelo processo na serventia para onde ele está retornando. Modificado no dia 13/04/2021 para
		// usar o ponteiro de redistribuição para localizar o responsável a ser reativado.
		idServCargo = procEncaminhamentoNe.retornaUltimoResponsavelProcessoServentia(processoDt.getId(), serventiaNovaDt.getId(), obFabricaConexao);
		
		if(idServCargo == null) {
			throw new MensagemException("Não foi possível localizar o responsável a ser ativado na nova serventia. Por gentileza, entre em contato com a equipe de suporte.");
		}
		
		serventiaCargoDt = new ServentiaCargoNe().consultarId(idServCargo, obFabricaConexao);
		
		if(serventiaCargoDt == null) {
			throw new MensagemException("Não foi possível localizar o responsável a ser ativado na nova serventia. Por gentileza, entre em contato com a equipe de suporte.");
		}
		processoResponsavelNe.reativaResponsavelProcessoServentia(processoDt.getId(), idServCargo, obFabricaConexao);
		
//		// Ao retornar a uma serventia que já esteve anteriormente, os responsáveis pelo processo/recurso devem ser ativados
//		//reativando todos os responsáveis, exceto desembargador
//		processoResponsavelNe.reativarResponsaveisProcessoExcetoServentia(processoDt.getId(), serventiaNovaDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);

		
		String id_serventiaCargo_novo = "";
		if(isRetornoAutomaticoCejusc){
			complemento += "[RETORNO AUTOMÁTICO] ";
		}
		complemento += serventiaNovaDt.getServentia();
				
		if(serventiaNovaDt != null && serventiaNovaDt.getId() != null ){
			//quando o id_serventiaCargo_novo não for informado na tela, deverá ser sorteado e informar no complemento da movimentação
			//que a distribuição foi direcionada à Serventia e não ao magistrado.
			
			if(serventiaCargoDt == null) {
			
				id_serventiaCargo_novo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(serventiaNovaDt.getId(), processoDt.getId_AreaDistribuicao());
				serventiaCargoDt = new ServentiaCargoNe().consultarId(id_serventiaCargo_novo);
				if (serventiaNovaDt.getId().length()==0 || processoDt.getId_AreaDistribuicao().length()==0 || id_serventiaCargo_novo.length() == 0){
					throw new MensagemException("Não foi possível encontrar a Serventia, a Área de Distribuição ou o novo Responsável.");
				}
			
			} else {
				id_serventiaCargo_novo = serventiaCargoDt.getId();
			}
			
		} else {
			throw new MensagemException("Não foi possível encontrar a Serventia, a Área de Distribuição ou o novo Responsável.");
		}	
						
		// Se processo que está sendo redistribuído tem um recurso ativo, esse também deve ter a serventia alterada
		RecursoNe recursoNe = new RecursoNe();
		String id_RecursoAtivo = recursoNe.getRecursoAtivo(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
		if (id_RecursoAtivo != null) {
			recursoNe.alterarServentiaRecurso(id_RecursoAtivo, processoDt.getId_Serventia(), serventiaNovaDt.getId(), logDt, obFabricaConexao);
		}
			
		// Consultando se há algum registro deste processo ter sido encaminhado e ainda não devolvido.
		String idProcEncaminhamento = procEncaminhamentoNe.consultarEncaminhamentoSemDevolucao(processoDt.getId());
		
		
		if( idProcEncaminhamento != null && !idProcEncaminhamento.isEmpty() ) {
			// Retorno
			procEncaminhamentoDt = procEncaminhamentoNe.consultarId(idProcEncaminhamento);
			procEncaminhamentoDt.setId_UsuServRetorno(usuarioDt.getId_UsuarioServentia());
			procEncaminhamentoDt.setDataRetorno( new TJDataHora().getDataHoraFormatadaaaaa_MM_ddHHmmss() );

		} 
		else {
			throw new MensagemException("Não foi possível retornar o processo. Registro do encaminhamento não encontrado.");
		}

		procEncaminhamentoDt.setId_UsuarioLog(usuarioDt.getId());
		procEncaminhamentoNe.salvar(procEncaminhamentoDt, obFabricaConexao);
		
		//Atualiza a descrição do complemento da movimentação gerada acima inserindo o nome do novo relator
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(id_serventiaCargo_novo);
		movimentacaoDt = movimentacaoNe.gerarMovimentacaoProcessoDevolvidoEncaminhamento(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento + " (Retornado para: " + novoResponsavelDt.getNomeUsuario() + ")", logDt, obFabricaConexao);
		
		// Salvando vínculo entre movimentação e arquivos inseridos
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(),String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL), logDt, obFabricaConexao);
		
		// Se, junto à pendência de "retornar à serventia de origem", tiver selecionado uma pendência de "arquivamento", este método receberá
		// valor "true" na variável "arquivar". Neste caso, não deve gerar a pendência de "verificar processo".
		if(!arquivar) {
			new PendenciaNe().gerarPendenciaVerificarProcesso(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaNovaDt.getId(), movimentacaoDt.getId(), null, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());
		}
		
		//salvo todas as alterações do processo uma unica vez
		processoDt.setId_Serventia( serventiaNovaDt.getId());					

		//Salvando nova serventia do processo
		//this.alterarServentiaProcesso(processoDt.getId(), usuarioDt.getId_Serventia(), processoDt.getId_Serventia(), serventiaNovaDt.getId(), processoDt.getId_AreaDistribuicao(), logDt, obFabricaConexao);					
		this.salvar(processoDt,obFabricaConexao);
		
		// TODO: Estre trecho está sendo comentado pelo fato de que os processos encaminhados não
		// terão mais os seus apensos encaminhados também. Retirar este código permantentemente
		// se esta modificação se concretizar.
		//
		//Verifica se o processo tem apenso. Se tiver, realiza o encaminhamento dele.
//		List listaProcessosApensos = this.consultarProcessosApensosRedistribuicao(processoDt.getId());
//		if(listaProcessosApensos != null && listaProcessosApensos.size() > 0){
//			RedistribuicaoDt redistribuicaoFilhoDt = new RedistribuicaoDt();
//			MovimentacaoProcessoDt movimentacaoProcessoFilhoDt = new MovimentacaoProcessoDt();
//			for (int j = 0; j < listaProcessosApensos.size(); j++) {
//				ProcessoDt processoApensoDt = (ProcessoDt)listaProcessosApensos.get(j);
//				processoApensoDt = this.consultarId(processoApensoDt.getId());
//				this.salvarRetornoServentiaOrigemEncaminhamento(processoApensoDt, usuarioDt, arquivos, logDt, true, obFabricaConexao);
//			}
//		}
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public byte[] relLiminaresDeferidas(String diretorioProjeto,  boolean desembargador, String idServentia, String opcaoPeriodo,String usuarioSistema) throws Exception {
		FabricaConexao obFabricaConexao = null;
		byte[] temp = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			List listaProcessos = obPersistencia.consultarProcessosTempoVidaTipoRelatorio(desembargador, idServentia, opcaoPeriodo);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "listagemLiminarDeferida";
			Map parametros = new HashMap();
			
			ServentiaDt serventia = null;
			
			if(idServentia != null && !idServentia.equals("")){
				serventia = this.consultarIdServentia(idServentia);
				parametros.put("serventia", serventia.getServentia());
			}else{
				parametros.put("serventia", "Todas que possuem processo com liminar deferida");
			}		
			
			String periodo = null;
			if(opcaoPeriodo.equals("1")){
				periodo = "Até 20 dias";
			}else{
				periodo = "Mais de "+ opcaoPeriodo + " dias";
			}
			
			
			parametros.put("caminhoLogo", diretorioProjeto + "imagens" + File.separator + "logoEstadoGoias02.jpg");
			parametros.put("dataAtual", new Date());
			parametros.put("nomeSolicitante", usuarioSistema);
			parametros.put("titulo", "Processos com Liminar Deferida");
			parametros.put("periodo", periodo);
			
						
			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaProcessos);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	//*********************************************************************************************************************************************************************************************************************************
    
	/**
	 * Método que retorna se o processo tem guia inicial e/ou complementar.
	 * 
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaInicial_GuiaComplementarPresente(String idProcesso) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		
		List listaGuiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoInicial_Complementar(idProcesso);
		
		if( listaGuiaEmissaoDt != null && !listaGuiaEmissaoDt.isEmpty() ) {
			return true;
		}
		
		return false;
	}
	
	public void limparClassificadorProcesso(ProcessoDt processoDt, FabricaConexao conexaoRecebida) throws Exception {
		LogDt obLogDt;
		ProcessoPs obPersistencia;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if (conexaoRecebida == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}
			else {
				obFabricaConexao = conexaoRecebida;
			}
			obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			obPersistencia.limparClassificadorProcesso(processoDt.getId());
			obLogDt = new LogDt("Processo", processoDt.getId(), processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), processoDt.getPropriedades(), processoDt.getPropriedades());
		}
		finally {
			if (conexaoRecebida == null) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idGuiaTipo
	 * @param String idNaturezaSPG
	 * @return List<GuiaCustaModeloDt>
	 * @throws Exception
	 */
	public List<GuiaCustaModeloDt> consultarItensGuiaCustaModeloDtProcessoTipo(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt, String idGuiaTipo, String idProcessoTipo) throws Exception {
		
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		List<GuiaCustaModeloDt> listaGuiaCustaModelo = null;
		
		//Consulta itens do cálculo específico
		GuiaCustaModeloNe guiaCustaModeloNe = new GuiaCustaModeloNe();
		
		//listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaProcessoTipo(obFabricaConexao, idGuiaTipo, idProcessoTipo);
		listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaProcessoTipoNovoRegimento(obFabricaConexao, idGuiaTipo, idProcessoTipo);
		
		return listaGuiaCustaModelo;
	}
	
	/**
	 * Mï¿½todo para calcular os itens da guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * 
	 * @return List listaGuiaItemDt
	 * 
	 * @throws Exception
	 */
	public List<GuiaItemDt> calcularItensGuia(GuiaEmissaoDt guiaEmissaoDt, List<GuiaCustaModeloDt> listaGuiaCustaModeloDt, Map valoresReferenciaCalculo) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		return guiaEmissaoNe.calcularItensGuia(guiaEmissaoDt, listaGuiaCustaModeloDt, valoresReferenciaCalculo, null, null);
	}
	
	/**
	 * Mï¿½todo para gerar o nï¿½mero da guia completo.
	 * @param numeroGuia
	 * @return
	 * @throws Exception 
	 */
	public String getNumeroGuiaCompleto(String numeroGuia) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		String digitoNumeroGuia = guiaEmissaoNe.algoritmoDigitoVerificadorNumeroGuia(numeroGuia);
		
		return Funcoes.completarZeros(numeroGuia + digitoNumeroGuia + GuiaEmissaoNe.NUMERO_SERIE_GUIA, 11);
	}	
	
	public boolean isProcessoTipoMandado(int processoTipoCodigo) throws Exception {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		return guiaEmissaoNe.isProcessoTipoMandado(processoTipoCodigo);
	}
	
	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idProcessoTipo.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloProcessoTipo(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		return new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, idGuiaTipo, idProcessoTipo);
	}
	
	protected GuiaEmissaoDt inicializarGuiaInicial(GuiaEmissaoDt guiaEmissaoDt, ProcessoCadastroDt processoDt) {
		List listaBairroDt;
		List listaQuantidadeBairroDt;
		
		guiaEmissaoDt.setRequerente("");
		guiaEmissaoDt.setRequerido("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setAreaDistribuicao("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNumeroProcessoDependente("");
		guiaEmissaoDt.setCodigoArea(AreaDt.CIVEL);
		guiaEmissaoDt.setBensPartilhar(GuiaEmissaoDt.VALOR_NAO);
		
		if (processoDt.getGrauProcesso() != null && processoDt.getGrauProcesso().equalsIgnoreCase("1") ) {
			guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
		} else {
			guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
		}
		
		guiaEmissaoDt.setDistribuidorQuantidade("1");
		guiaEmissaoDt.setContadorQuantidade("1");
		guiaEmissaoDt.setCustasQuantidade("1");
		guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
		guiaEmissaoDt.setEscrivaniaQuantidade("1");
		guiaEmissaoDt.setAtosEscrivaesCivel("1");
		
		guiaEmissaoDt.setId_Processo(processoDt.getId());
		guiaEmissaoDt.setProcessoDt(processoDt);
		guiaEmissaoDt.setNovoValorAcao(processoDt.getValor());
		guiaEmissaoDt.setNovoValorAcaoAtualizado(processoDt.getValor());
		guiaEmissaoDt.setValorAcao(processoDt.getValor());
		
		guiaEmissaoDt.setNumeroImpetrantes("0");
		guiaEmissaoDt.setPenhoraQuantidade("1");
		guiaEmissaoDt.setOrigemEstado("");
		guiaEmissaoDt.setProtocoloIntegrado("");
		
		guiaEmissaoDt.setId_Comarca("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setComarcaCodigo("");
		
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPGCodigo("");
		
		guiaEmissaoDt.setId_AreaDistribuicao("");
		guiaEmissaoDt.setAreaDistribuicao("");
		
		guiaEmissaoDt.setId_ProcessoTipo("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setProcessoTipoCodigo("");
		
		listaBairroDt = new ArrayList();
		listaQuantidadeBairroDt = new ArrayList();
		
		return guiaEmissaoDt;
	}
	
	protected void preparaItensDeLocomocao(List<BairroDt> listaBairroIntimacaoDt, List<Integer> listaBairroIntimacaoDtQuantidade, List<GuiaItemDt> listaGuiaItemLocomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
			GuiaLocomocaoNe guiaLocomocaoNe = new GuiaLocomocaoNe();
			if( listaBairroIntimacaoDt != null && listaBairroIntimacaoDt.size() > 0 ) {
				for(int i = 0; i < listaBairroIntimacaoDt.size(); i++) {
					BairroDt bairroDt = listaBairroIntimacaoDt.get(i);	
					int quantidade = 1;				
					if (listaBairroIntimacaoDtQuantidade != null && listaBairroIntimacaoDt.size() == listaBairroIntimacaoDt.size() && listaBairroIntimacaoDtQuantidade.get(i).intValue() > 1) { 
						quantidade = listaBairroIntimacaoDtQuantidade.get(i);
					}
					for(int j = 0; j < quantidade; j++) {
						guiaLocomocaoNe.calcularGuiaLocomocao(listaGuiaItemLocomocaoDt, bairroDt, guiaEmissaoDt, contaVinculadaSegundoGrau);
					}				
				}										
			}		
		}
	
	protected void preparaItensDeLocomocaoNOVO(List<ProcessoParteDt> listProcessoParteComLocomocaoDt, List<GuiaItemDt> listaGuiaItemLocomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		GuiaLocomocaoNe guiaLocomocaoNe = new GuiaLocomocaoNe();
		if( listProcessoParteComLocomocaoDt != null && listProcessoParteComLocomocaoDt.size() > 0 ) {
			for(int i = 0; i < listProcessoParteComLocomocaoDt.size(); i++) {
				ProcessoParteDt processoParteDt = listProcessoParteComLocomocaoDt.get(i);
				BairroDt bairroLocomocaoDt = new BairroDt();
				bairroLocomocaoDt.setId(processoParteDt.getEnderecoParte().getId_Bairro());
				int quantidade = 1;				
				if (processoParteDt != null && processoParteDt.getLocomocaoDt() != null && processoParteDt.getLocomocaoDt().getQtdLocomocao() > 1) { 
					quantidade =  processoParteDt.getLocomocaoDt().getQtdLocomocao();
				}
				for(int j = 0; j < quantidade; j++) {
					guiaLocomocaoNe.calcularGuiaLocomocaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocaoDt, processoParteDt.getLocomocaoDt(), guiaEmissaoDt, contaVinculadaSegundoGrau);	
				}				
			}										
		}		
	}
	
	protected boolean gerarGuiaInicialProcesso(GuiaEmissaoDt guiaEmissaoDt, ProcessoCadastroDt ProcessoCiveldt, UsuarioDt usuarioDt) throws Exception {
		List listaItensGuiaModelo = null;
		List listaItensGuiaEmissao = new ArrayList();
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		
		if( ProcessoCiveldt != null && ProcessoCiveldt.getProcessoDependenteDt() != null && ProcessoCiveldt.getProcessoDependenteDt().getId() != null && ProcessoCiveldt.getProcessoDependenteDt().getId().length()>0) {
			ServentiaDt serventiaProcessoDependente = this.consultarIdServentia(ProcessoCiveldt.getProcessoDependenteDt().getId_Serventia());
			if (serventiaProcessoDependente != null) {
				if (serventiaProcessoDependente.getId_Comarca() != null && serventiaProcessoDependente.getId_Comarca().trim().length() > 0) {
					guiaEmissaoDt.setId_Comarca(serventiaProcessoDependente.getId_Comarca());
				}
			}						
			if (ProcessoCiveldt.getProcessoDependenteDt().getId_AreaDistribuicao() != null && ProcessoCiveldt.getProcessoDependenteDt().getId_AreaDistribuicao().trim().length() > 0) {
				guiaEmissaoDt.setId_AreaDistribuicao(ProcessoCiveldt.getProcessoDependenteDt().getId_AreaDistribuicao());
			} 
		} else {
			guiaEmissaoDt.setId_Comarca(ProcessoCiveldt.getId_Comarca());
			guiaEmissaoDt.setId_AreaDistribuicao(ProcessoCiveldt.getId_AreaDistribuicao());
		}
		
		if( ProcessoCiveldt != null && ProcessoCiveldt.getId_Serventia() != null ) {
			guiaEmissaoDt.setId_Serventia(ProcessoCiveldt.getId_Serventia());
		}
		
		guiaEmissaoDt.setId_ProcessoTipo(ProcessoCiveldt.getId_ProcessoTipo());
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		guiaEmissaoDt.setValorAcao(ProcessoCiveldt.getValor());
		guiaEmissaoDt.setProcessoTipoCodigo(processoTipoNe.consultarCodigo(ProcessoCiveldt.getId_ProcessoTipo()));
		
		if (ProcessoCiveldt.isAssistencia()) {
			guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.AGUARDANDO_DEFERIMENTO);
		} else if (ProcessoCiveldt.isIsento()){ 
			guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.BAIXADA_COM_ISENCAO);
		} else {
			guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
		}
		
		guiaEmissaoDt.setRequerente(ProcessoCiveldt.getPrimeiroPoloAtivoNome());
		guiaEmissaoDt.setRequerido(ProcessoCiveldt.getPrimeiroPoloPassivoNome());
		
		GuiaModeloDt guiaModeloDt = new GuiaModeloDt();
		if (guiaEmissaoDt.getCodigoGrau() != null && guiaEmissaoDt.getCodigoGrau().equalsIgnoreCase(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU)) {
			//consulta itens guia modelo
			listaItensGuiaModelo = this.consultarItensGuiaCustaModeloDtProcessoTipo(null, guiaEmissaoDt, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
			//consulta guia modelo
			guiaModeloDt = this.consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
		} else {
			if( ProcessoCiveldt.getGrauProcesso() != null && ProcessoCiveldt.getGrauProcesso().equalsIgnoreCase("3") ) {
				
				if( processoTipoNe.isProcessoTipoMandadoTurmaRecursalGeraGuia(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
					
					//Ocorrência 2019/6291
					//Gera a guia inicial para a turma recursal e classe mandado de segurança
					//Ocorrencia 2021/2039
					//Mudei para pesquisar somente "Mandado de Segurança Cível" e criei os modelos para as guias necessárias de primeiro grau. PS.: quando for turma, é gerado os itens pelo método.
					listaItensGuiaModelo = guiaEmissaoNe.gerarGuiaInicialMandadoSegurancaTurmaRecursal(listaItensGuiaModelo, guiaModeloDt, guiaEmissaoDt);
					if( guiaEmissaoDt.getGuiaModeloDt() != null ) {
						guiaModeloDt = guiaEmissaoDt.getGuiaModeloDt();
					}
					
					//Altera o status da guia para Aguardando pagamento
					guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
				}
				
			} else {
				//consulta itens guia modelo
				listaItensGuiaModelo = this.consultarItensGuiaCustaModeloDtProcessoTipo(null, guiaEmissaoDt, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
				//consulta guia modelo
				guiaModeloDt = this.consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
			}
		}
		
		//Validação da modelo da guia
		if(ProcessoCiveldt.isIsento() && (guiaModeloDt == null || guiaModeloDt.getId() == null || guiaModeloDt.getId().length()==0)) {
			return false;
		
		} else if( guiaModeloDt == null || guiaModeloDt.getId() == null || guiaModeloDt.getId().length()==0  ) {
			throw new MensagemException("Modelo da guia não encontrado. Pode ser que esta classe não tenha cálculo homologado(Guia Inicial) ou o processo deve ser cadastrado como ISENTO.");
		}
		
		guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
		
		Map valoresReferenciaCalculo = new HashMap();
		valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcao());
		valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcao());
		
		if( guiaEmissaoDt.getNovoValorAcao().toString().length() == 0 ) {
			valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
		}
		else {
			valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, guiaEmissaoDt.getNovoValorAcao());
		}
		
		//Verifica se é cautelar, contencioso
		guiaEmissaoNe.adicionarItemCautelarContenciosoParaGuiaInicial(guiaEmissaoDt.getCodigoGrau(), listaItensGuiaModelo, guiaEmissaoDt);
		
		//Locomoção com Zona-Bairro
		List valoresIdBairro = null;
		List valoresIdBairroContaVinculada = null;
		List listProcessoParteComLocomocaoDt =  new ArrayList();
		List listaPartesPromoventes = ProcessoCiveldt.getListaPolosAtivos();
		List listaPartesPromovidos = ProcessoCiveldt.getListaPolosPassivos();
		
		if( listaPartesPromoventes != null && listaPartesPromoventes.size() > 0 ) {
			if( valoresIdBairro == null )
				valoresIdBairro = new ArrayList();
			if( valoresIdBairroContaVinculada == null )
				valoresIdBairroContaVinculada = new ArrayList();
			
			for(int i = 0; i < listaPartesPromoventes.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt)  listaPartesPromoventes.get(i);
				EnderecoDt enderecoDt = (EnderecoDt) processoParteDt.getEnderecoParte();
				int quantidadeLocomocao = 0;
				if (enderecoDt.getId_Bairro() != null && enderecoDt.getId().length()>0 && processoParteDt.getLocomocaoDt().getQtdLocomocao() > 0 ){
					quantidadeLocomocao = processoParteDt.getLocomocaoDt().getQtdLocomocao();
					for(int j = 0; j < quantidadeLocomocao; j++) {
						valoresIdBairro.add(enderecoDt.getId_Bairro());
						valoresIdBairroContaVinculada.add(enderecoDt.getId_Bairro());
					}
					listProcessoParteComLocomocaoDt.add(processoParteDt);
				}
			}
		}
		
		if( listaPartesPromovidos != null && listaPartesPromovidos.size() > 0 ) {
			if( valoresIdBairro == null )
				valoresIdBairro = new ArrayList();
			if( valoresIdBairroContaVinculada == null )
				valoresIdBairroContaVinculada = new ArrayList();
			
			for(int i = 0; i < listaPartesPromovidos.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt)  listaPartesPromovidos.get(i);
				EnderecoDt enderecoDt = (EnderecoDt) processoParteDt.getEnderecoParte();
				int quantidadeLocomocao = 0;
				if (enderecoDt.getId_Bairro() != null && enderecoDt.getId().length()>0 && processoParteDt.getLocomocaoDt().getQtdLocomocao() > 0 ){
					quantidadeLocomocao = processoParteDt.getLocomocaoDt().getQtdLocomocao();
					BairroDt bairroAuxDt = new BairroDt();
					bairroAuxDt.setId(enderecoDt.getId_Bairro());
					for(int j = 0; j < quantidadeLocomocao; j++) {
						valoresIdBairro.add(enderecoDt.getId_Bairro());
						valoresIdBairroContaVinculada.add(enderecoDt.getId_Bairro());
					}
					listProcessoParteComLocomocaoDt.add(processoParteDt);
				}
			}
		}
		
		if( valoresIdBairro != null )
			valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
		
		listaItensGuiaEmissao = this.calcularItensGuia(guiaEmissaoDt, listaItensGuiaModelo, valoresReferenciaCalculo);
		List<GuiaItemDt> listaGuiaItemLocomocaoDt = new ArrayList<GuiaItemDt>();
		this.preparaItensDeLocomocaoNOVO(listProcessoParteComLocomocaoDt, listaGuiaItemLocomocaoDt, guiaEmissaoDt, false); //aqui leandro
		
		if (listaGuiaItemLocomocaoDt.size() > 0) {
			listaItensGuiaEmissao.addAll(listaGuiaItemLocomocaoDt);
		}
		
		if( guiaEmissaoDt.getProcessoTipoCodigo() != null && guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && this.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
			valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
			if( guiaEmissaoDt.getNumeroImpetrantes() != null && guiaEmissaoDt.getNumeroImpetrantes().length() > 0 ) {
				valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
			}
			if( guiaEmissaoDt.getProcessoTipoCodigo() != null && Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO ) {
				valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO);
			}
		}
		
		if( guiaEmissaoDt.getId_NaturezaSPG() != null && 
			guiaEmissaoDt.getId_NaturezaSPG().length() > 0 && 
			Integer.parseInt(guiaEmissaoDt.getId_NaturezaSPG()) == NaturezaSPGDt.MANDADO_SEGURANCA ) {
			
			valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + guiaEmissaoDt.getProcessoTipoCodigo());
		}

		guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
		guiaEmissaoDt.setListaGuiaItemDt(listaItensGuiaEmissao);
		
		//nova parte
		if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
			guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
			guiaEmissaoDt.setNumeroGuiaCompleto( this.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
		}
		
		guiaEmissaoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		guiaEmissaoDt.setId_Usuario(usuarioDt.getId());
		return true;
			
	}

	//*********************************************************************************************************************************************************************************************************************************
	public void retirarSigilo(ProcessoDt processoDt, LogDt logDt, String id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Seta os valores em processo pois sera necessario na hora de
		// mostrar dados do processo na tela
		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
		
		String valorNovo = "[ProcessoStatus:Ativo]";
		
		// Atualiza dados do processo e gera log
		LogDt obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), "", valorNovo);
		obPersistencia.alterarProcessoSigilo(processoDt);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO ARQUIVADO RETIRADO SIGILOSO
		movimentacaoNe.gerarMovimentacaoProcessoSigiloso(processoDt.getId(), id_UsuarioServentia, "", logDt, obFabricaConexao, MovimentacaoTipoDt.PROCESSO_RETIRADO_SIGILO);
	}
	
	public void tornarSigiloso(ProcessoDt processoDt, LogDt logDt, String id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

		// Seta os valores em processo pois sera necessario na hora de
		// mostrar dados do processo na tela
		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.SIGILOSO));		
		
		String valorNovo = "[ProcessoStatus:Sigiloso]";		
		
		// Atualiza dados do processo e gera log
		LogDt obLogDt = new LogDt("Processo", processoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), "", valorNovo);
		obPersistencia.alterarProcessoSigilo(processoDt);
		obLog.salvar(obLogDt, obFabricaConexao);

		// Gera movimentação PROCESSO ARQUIVADO TORNADO SIGILOSO
		movimentacaoNe.gerarMovimentacaoProcessoSigiloso(processoDt.getId(), id_UsuarioServentia, "", logDt, obFabricaConexao, MovimentacaoTipoDt.PROCESSO_TORNADO_SIGILOSO);
	}

	public List consultarProcessosSigiloso(  UsuarioDt usuarioDt, String posicao) throws Exception {
		List listaProcessos = null;

		// int grupo = -1;
		int grupoTipo = -1;
		if (usuarioDt != null) grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
								
			switch (grupoTipo) {
				case GrupoTipoDt.AUTORIDADE_POLICIAL:
					listaProcessos = obPersistencia.consultarProcessosSigilososDelegacia( usuarioDt.getId_Serventia(), posicao);
					break;
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
					listaProcessos = obPersistencia.consultarProcessosSigilososSegundoGrau( usuarioDt.getId_ServentiaCargo(), posicao);
					break;
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
					listaProcessos = obPersistencia.consultarProcessosSigiloso(  usuarioDt.getId_ServentiaCargo(), posicao);
					break;
				case GrupoTipoDt.MP:
					listaProcessos = obPersistencia.consultarProcessosSigilosoPromotor( usuarioDt.getId_ServentiaCargo(), posicao);
					break;
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
				case GrupoTipoDt.ASSESSOR_MP:
					listaProcessos = obPersistencia.consultarProcessosSigilosoPromotor( usuarioDt.getId_ServentiaCargoUsuarioChefe(), posicao);
					break;
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
					listaProcessos = obPersistencia.consultarProcessosSigiloso( usuarioDt.getId_ServentiaCargoUsuarioChefe(), posicao);

					break;
			}			

			setQuantidadePaginas(listaProcessos);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}

	/**
	 * Consulta a quantidade de Processos Sigilosos
	 * 
	 * @param UsuarioDt         
	 * @author jrcorrea 21/02/2018
	 */
	public ListaDadosServentiaDt consultarQuantidadeSigiloso(UsuarioDt usuarioDt) throws Exception {
		
		ListaDadosServentiaDt listaDadosServentiaDt = null;
		
		long loQtd = 0;
		// int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			switch (grupoTipo) {
				case GrupoTipoDt.AUTORIDADE_POLICIAL:
					loQtd = obPersistencia.consultarQuantidadeSigilososDelegacia(usuarioDt.getId_Serventia());					
	
					if(loQtd > 0){
						
						listaDadosServentiaDt = new ListaDadosServentiaDt();
						listaDadosServentiaDt.setQuantidade(loQtd);
						listaDadosServentiaDt.setDescricao( "Sigiloso" );
						listaDadosServentiaDt.setCodigo( ProcessoStatusDt.SIGILOSO);
						listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.Curinga7 + "&PassoBusca=4&ProcessoStatusCodigo=" +  ProcessoStatusDt.SIGILOSO);
						
					}
					break;
				
				case GrupoTipoDt.MP:
					loQtd = obPersistencia.consultarQuantidadeSigilosos(usuarioDt.getId_ServentiaCargo());					

					if(loQtd > 0){
						
						listaDadosServentiaDt = new ListaDadosServentiaDt();
						listaDadosServentiaDt.setQuantidade(loQtd);
						listaDadosServentiaDt.setDescricao( "Sigiloso" );
						listaDadosServentiaDt.setCodigo( ProcessoStatusDt.SIGILOSO);
						listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=4&ProcessoStatusCodigo=" +  ProcessoStatusDt.SIGILOSO);
						
					}
					break;
					
				case GrupoTipoDt.ASSESSOR_ADVOGADO:
				case GrupoTipoDt.ASSESSOR_MP:
					
					loQtd = obPersistencia.consultarQuantidadeSigilosos(usuarioDt.getId_ServentiaCargoUsuarioChefe());			
					if(loQtd > 0){
						listaDadosServentiaDt = new ListaDadosServentiaDt();
						listaDadosServentiaDt.setQuantidade(loQtd);
						listaDadosServentiaDt.setDescricao( "Sigiloso" );
						listaDadosServentiaDt.setCodigo( ProcessoStatusDt.SIGILOSO);
						listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=4&ProcessoStatusCodigo=" +  ProcessoStatusDt.SIGILOSO);
					}
					
					break;
				case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:	
					
					
					loQtd = obPersistencia.consultarQuantidadeSigilosos(usuarioDt.getId_ServentiaCargoUsuarioChefe());			
					
					if(loQtd > 0){
						listaDadosServentiaDt = new ListaDadosServentiaDt();
						listaDadosServentiaDt.setQuantidade(loQtd);
						listaDadosServentiaDt.setDescricao( "Sigiloso" );
						listaDadosServentiaDt.setCodigo( ProcessoStatusDt.SIGILOSO);
						listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=2&ProcessoStatusCodigo=" +  ProcessoStatusDt.SIGILOSO);
						
					}
					break;

				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				case GrupoTipoDt.JUIZ_TURMA:
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:

					loQtd = obPersistencia.consultarQuantidadeSigilosos(usuarioDt.getId_ServentiaCargo());					

					if(loQtd > 0){
						listaDadosServentiaDt = new ListaDadosServentiaDt();
						listaDadosServentiaDt.setQuantidade(loQtd);
						listaDadosServentiaDt.setDescricao( "Sigiloso" );
						listaDadosServentiaDt.setCodigo( ProcessoStatusDt.SIGILOSO);
						listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=2&ProcessoStatusCodigo=" +  ProcessoStatusDt.SIGILOSO);
						
					}
					break;
			}			

		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		
		return listaDadosServentiaDt;
	}
	
	
	/**
	 * Método responsável em consultar seo promotor tem parte no processo.
	 * 
	 * @param processoNumero
	 * @param id_UsuarioServentia
	 * 
	 * @author tamaralsantos
	 */
	public Boolean verificarPromotoriaParteProcesso(String idProcesso, String id_serventia) throws Exception {

		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.verificarPromotoriaParteProcesso(idProcesso, id_serventia);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	
	/**
	 * Método para verificar se o processo tipo é carta precatória. 
	 * @return boolean - true para carta precatória
	 * @throws Exception
	 * @author jrcorrea 16/05/2018 
	 */
	public boolean isCartaPrecatoria( String idProcessoTipo) throws Exception {
		 
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		int codigoProcessoTipo = Funcoes.StringToInt(processoTipoNe.consultarCodigo(idProcessoTipo),-999); 
		if (codigoProcessoTipo== ProcessoTipoDt.CARTA_PRECATORIA || codigoProcessoTipo== ProcessoTipoDt.CARTA_PRECATORIA_CPC || codigoProcessoTipo== ProcessoTipoDt.CARTA_PRECATORIA_CPP || codigoProcessoTipo== ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE ){
			return true;
		}
		return false;
	}	

	public long consultarQuantidadeProcessosAdvogado(CertidaoGuiaDt certidaoGuiaDt) throws Exception {
		
		long quantidade = 0;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			quantidade = obPersistencia.consultarQuantidadeProcessosAdvogado(certidaoGuiaDt.getOabNumero(), certidaoGuiaDt.getOabComplemento(), certidaoGuiaDt.getOabUfCodigo(), certidaoGuiaDt.getDataTimeInicial(), certidaoGuiaDt.getDataTimeFinal());
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return quantidade;
	}
	
	public List consultarProcessoPraticaForenseAdvogado(CertidaoGuiaDt certidaoGuiaDt) throws Exception {
		
		List listaProcesso = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcesso = obPersistencia.consultarProcessoPraticaForenseAdvogado(certidaoGuiaDt.getOabNumero(), certidaoGuiaDt.getOabComplemento(), certidaoGuiaDt.getOabUfCodigo(), certidaoGuiaDt.getDataTimeInicial(), certidaoGuiaDt.getDataTimeFinal());
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return listaProcesso;
	}
	
	/**
	 * Altera o tipo de custa vinculado ao processo
	 * 
	 * @param id_Processo
	 *            , identificação do processo
	 * @param id_Custa_Tipo_Anterior
	 *            , custa tipo antiga
	 * @param id_Custa_Tipo_Nova
	 *            , nova Custa tipo
	 * @param logDt
	 *            , dados do log
	 * @param conexao
	 *            , conexão ativa
	 * 
	 * @author lsbernardes
	 */
	public void alterarCustaTipoProcesso(String id_Processo, String id_Custa_Tipo_Anterior, String id_Custa_Tipo_Nova, LogDt logDt, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try {
			if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else
				obFabricaConexao = conexao;
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Processo:" + id_Processo + ";Id_Custa_tipo:" + id_Custa_Tipo_Anterior + "]";
			String valorNovo = "[Id_Processo:" + id_Processo + ";Id_Custa_tipo:" + id_Custa_Tipo_Nova + "]";

			obLogDt = new LogDt("Processo", id_Processo, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			obPersistencia.alterarCustaTipoProcesso(id_Processo, id_Custa_Tipo_Nova);

			obLog.salvar(obLogDt, obFabricaConexao);
		} finally {
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Verifica se o processo está em uma serventia que possua um dos subTipos que não se aplica a contagem de prazo do novo CPC
	 * 
	 * @param id_Processo, identificação do processo
	 * @param conexao,
	 *            conexão com o banco de dados
	 * 
	 * @author lsbernardes
	 */
    public boolean isProcessoJuizadosTurmas(String id_processo, FabricaConexao fabConexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			} 
			else {
				obFabricaConexao = fabConexao;
			}
			
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isProcessoJuizadosTurmas(id_processo);
		}
		finally {
			if( fabConexao == null ) {
				if( obFabricaConexao != null ) {
					obFabricaConexao.fecharConexao();
				}
			}
		}
			
		return retorno;	
    }
    
    /**
	 * Verifica se determinado usuário é substituto em um processo, validando o serventiaCargo do usuário
	 * @param id_Processo, identificação do processo
	 * @param id_serventiaProcesso, identificação da serventia do processo
	 * @param grupoCodigo, codigo do grupo do usuário
	 * @param id_serventiaCargo, identificação do cargo para verificar se o mesmo é substitudo de algum responsável
	 * @return boolean
	 * @author lsbernardes
	 */
    public boolean isSubstitutoProcessoSegundoGrau(String id_Processo, String id_serventiaProcesso, String grupoCodigo, String id_serventiaCargo) throws Exception {
		return new ProcessoResponsavelNe().isSubstitutoProcessoSegundoGrau(id_Processo, id_serventiaProcesso, grupoCodigo, id_serventiaCargo);		
	}
    
    public String consultarPartesIsentaslJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		PartesIsentaNe neObjeto = new PartesIsentaNe();
		stTemp = neObjeto.consultarPartesIsentaslJSON(descricao, posicao);
		neObjeto = null;
		return stTemp;
	}
    
    /**
     * 
     * @param numeroProcessoCompleto
     * @return
     * @throws Exception
     */
    public ProcessoDt consultarIdCnjClassePorProcessoNumeroCompleto(String numeroProcessoCompleto) throws Exception {
		ProcessoDt dtRetorno = null;
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			// Divide número de processo do dígito verificador
			String[] stTemp = numeroProcessoCompleto.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
			if (stTemp.length >= 1) numero = stTemp[0];			
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];
			if (stTemp.length >= 3) ano = stTemp[2];
			if (stTemp.length >= 6) forumCodigo = stTemp[5];
			
			dtRetorno = obPersistencia.consultarIdCnjClassePorProcessoNumeroCompleto(numero, digitoVerificador, ano, forumCodigo);
			
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public boolean isTemAssunto(String id_Processo) throws Exception {
		boolean boRetorno = false;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());			
			
			boRetorno = obPersistencia.isTemAssunto(id_Processo);
			
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	public List consultarProcessosParalisadosConclusaoSegundoGrauPorAssistente(String id_Serventia, String id_ServentiaCargo, String nomeAssistente, String periodo, String posicaoPaginaAtual) throws Exception {
		List listTemp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listTemp = obPersistencia.consultarProcessosParalisadosConclusoesSegundoGrauPorAssistente(id_Serventia, id_ServentiaCargo, nomeAssistente, periodo, posicaoPaginaAtual);
			QuantidadePaginas = (Long) listTemp.get(listTemp.size() - 1);
			listTemp.remove(listTemp.size() - 1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listTemp;
	}
	
	/**
	 * Método para consultar as guias inciais aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @param String idUsuario
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialAguardandoPagamento(String idProcesso, String idUsuario) throws Exception {
		return new GuiaEmissaoNe().consultarGuiaEmissaoInicialAguardandoPagamento(idProcesso,idUsuario);
	}
	
	public String consultarServentiaProcesso(String id_Processo) throws Exception {
		String retorno = "";
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarServentiaProcesso(id_Processo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public boolean alterarProcessoTipo(String idProcesso, String idProcessoTipoNovo, String processoTipoNovo, String processoTipoAtual, String ipComputador) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			if(idProcesso != null && idProcesso != null ) {
				ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
				retorno = obPersistencia.alterarProcessoTipo(idProcesso, idProcessoTipoNovo);
				
				LogDt obLogDt = new LogDt("Processo", idProcesso, UsuarioDt.SistemaProjudi, ipComputador, String.valueOf(LogTipoDt.Alterar), processoTipoAtual, processoTipoNovo);
				obLog.salvar(obLogDt, obFabricaConexao);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se foi selecioanda a classe de segundo grau Conflito de competência e se é um usuário do tjgo que está cadastrando o processo
	 * 
	 * @param String grupo codigo
	 * @param String id processo tipo (classe)
	 * @return boolean 
	 * @throws Exception
	 */
	private boolean isIsentoJuizoConflitoCompetencia(String grupoCodigo, String idProcTipo) throws Exception{
		boolean retorno = false;
		
		ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(idProcTipo);
		if(processoTipoDt.isConflitoCompetencia2Grau() && GrupoDt.isUsuarioPrimeiroSegundoGrauConflitoCompetencia(grupoCodigo)) {
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se foi selecioanda a classe de segundo grau Conflito de competência e se é um usuário do tjgo de primeiro grau que está cadastrando o processo
	 * 
	 * @param String grupo codigo
	 * @param String id processo tipo (classe)
	 * @return boolean 
	 * @throws Exception
	 */
	public boolean isParteIsenta(String CNPJ) throws Exception{
		PartesIsentaNe partesIsentaNe = new PartesIsentaNe();
		return partesIsentaNe.isParteIsenta(CNPJ);
	}
	
	public void gerarPendeciaSolicitarCargaProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt) throws Exception {
		PendenciaNe neObjeto = new PendenciaNe();
		neObjeto.gerarPendeciaSolicitarCargaProcesso(usuarioDt, processoDt);
		neObjeto = null;
	}
	
	/**
	 * Método para verificar se o processo é misto (fisico/eletronico)
	 * 
	 * @param String id_proc
	 * 
	 * @return boolean 
	 * @author jrcorrea
	 * @throws Exception
	 */
	public boolean isMisto(String id_proc) throws Exception{
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.consultarIsMisto(id_proc);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	/**
     * Verifica se o processo já possui uma pendencia do tipo solicitação de carga para o usuário  e o processo passado
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo
     * @param  id_UsuarioServentia, identificador do cadastrador
     * @return boolean
     */
	public boolean possuiPendeciaSolicitarCargaProcesso(String id_Processo, String id_UsuarioServentia) throws Exception {
		boolean retorno = false;
		PendenciaNe neObjeto = new PendenciaNe();
		retorno = neObjeto.possuiPendeciaSolicitarCargaProcesso(id_Processo, id_UsuarioServentia);
		neObjeto = null;
		return retorno;
	}
	
	/**
     * Verifica se o processo já possui uma pendencia do tipo solicitação de carga aguardando Retorno gerara pela serventia
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo
     * @return boolean
     */
	public boolean possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(String id_Processo) throws Exception {
		boolean retorno = false;
		PendenciaNe neObjeto = new PendenciaNe();
		retorno = neObjeto.possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(id_Processo);
		neObjeto = null;
		return retorno;
	}
	
	public List consultarTodosProcessosClassificadosServentia(String id_Classificador, String id_Serventia, String id_juiz) throws Exception {
		List lisRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			lisRetorno = obPersistencia.consultarTodosProcessosClassificadosServentia(id_Classificador,id_Serventia, id_juiz);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lisRetorno;
	}
	
	/**
	 * Método para consultar o processo (fisico/eletronico)
	 * 
	 * @param String processoNumeroVinculo
	 * 
	 * @return ProcessoDt 
	 * @author tathysantos
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoDigitalEFisico(String processoNumeroVinculo) throws Exception {
		
		//Primeiro consulta se existe processo digital
		ProcessoDt processoVinculoDt = this.consultarProcessoPrincipal(processoNumeroVinculo,null);
		
//		if (processoVinculoDt == null) {
//			
//			processoVinculoDt = new ProcessoDt();
//			
//			//Consulta se é um processo físico de primeiro grau;
//			ProcessoSPGDt processoSPGDt = new ProcessoSPGNe().consulteProcesso(processoNumeroVinculo); 
//			if(processoSPGDt != null && processoSPGDt.getNumeroProcessoCompletoDt() != null){
//				processoVinculoDt.setProcessoFisicoTipo("SPG");
//				processoVinculoDt.setProcessoFisicoNumero(processoSPGDt.getNumeroProcessoCompletoDt().getNumeroCompletoDoProcesso());
//				processoVinculoDt.setProcessoFisicoComarcaNome(processoSPGDt.getNomeComarca());
//				processoVinculoDt.setProcessoFisicoComarcaCodigo(processoSPGDt.getCodComarca());
//				
//			} else {
//				
//				//Consulta se é um processo físico de segundo grau;
//				ProcessoSSGDt processoSSGDt = new ProcessoSSGNe().consulteProcesso(processoNumeroVinculo); 
//				
//				if(processoSSGDt != null && processoSSGDt.getNumeroProcessoCompletoDt() != null){
//					processoVinculoDt.setProcessoFisicoTipo("SSG");
//					processoVinculoDt.setProcessoFisicoNumero(processoSSGDt.getNumeroProcessoCompletoDt().getNumeroCompletoDoProcesso());
//					processoVinculoDt.setProcessoFisicoComarcaNome(processoSSGDt.getNomeComarca());
//					processoVinculoDt.setProcessoFisicoComarcaCodigo(processoSSGDt.getCodComarca());
//				} 
//				
//			}
//
//		}
		
		return processoVinculoDt;
	}
	  
    /**
     * Realizar Carga em processo
     * 
     * @param movimentacaoProcessoDt
     *            vo de movimentacao
     * @param usuarioDt
     *            vo de usuario
     * @throws Exception
     */
    public void realizarCargaProcesso(UsuarioDt usuarioDt, MovimentacaoProcessoDt movimentacaoProcessoDt) throws MensagemException, Exception {
        FabricaConexao obFabricaConexao = null;
        try {
        	obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            //Essa operação não será realizada em lote, então sempre havera um processo na lista
            if (movimentacaoProcessoDt != null && movimentacaoProcessoDt.getListaProcessos() != null && movimentacaoProcessoDt.getListaProcessos().size()>0){
	            MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
	            PendenciaNe pendenciaNe = new PendenciaNe();
	            ProcessoDt processoDt = (ProcessoDt) movimentacaoProcessoDt.getListaProcessos().get(0);
	         	movimentacaoNe.salvarMovimentacaoGenerica(movimentacaoProcessoDt, usuarioDt, obFabricaConexao);
	         	pendenciaNe.gerarPendenciaRetornoSolicitarCargaProcesso(processoDt, usuarioDt,  obFabricaConexao);
            } else {
            	throw new MensagemException("Não foi possível localizar o processo.");
            }

            obFabricaConexao.finalizarTransacao();
            
        } catch (Exception e) {
             obFabricaConexao.cancelarTransacao();
            throw e;
        } finally {
             obFabricaConexao.fecharConexao();
        }
    }

    public boolean isAssistencia(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		boolean boTemp = true;
		String stTemp = "";
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		stTemp = obPersistencia.consultarAssistencia(idProcesso);
		
		if(stTemp == null) {
			throw new MensagemException("Não foi possível identificar se o processo é assistência ou não.");
		}
		
		if(stTemp.equalsIgnoreCase(String.valueOf(ProcessoDt.COM_CUSTAS))){
			boTemp = false;
		}
		return boTemp;
	}
    
    public List consultarProcessosTCO(String tcoNumero) throws Exception {
		List listaProcessos;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			// Consulta os processos
			listaProcessos = obPersistencia.consultarProcessosTCO(tcoNumero);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
    
    public String consultarAreasDistribuicaoCivelJSON(String tempNomeBusca, String id_Comarca, String id_Serventia_Usuario, boolean comCusta, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		stTemp = neObjeto.consultarAreasDistribuicaoCivelJSON(tempNomeBusca, id_Comarca, id_Serventia_Usuario, comCusta,  posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
    

	public boolean consultarSessaoVirtual(String idProcesso) throws Exception {
		VotoNe votoNe = new VotoNe();
		return votoNe.consultarSessaoVirtual(idProcesso);
	}
	
	public String pendenciaAbertasAdvSustentacaoOral(String idProcesso, String idUsuLogado, String idServentia, Boolean isSecretario, String idServentiaProc) throws Exception {
		VotoNe votoNe = new VotoNe();
		return votoNe.pendenciaAbertasAdvSustentacaoOral(idProcesso, idUsuLogado, idServentia, isSecretario, idServentiaProc);
	}
	
	public String pendenciaAbertasAdvSustentacaoOralIdPend(String idProcesso, String idUsuLogado, String idServentia) throws Exception {
		VotoNe votoNe = new VotoNe();
		return votoNe.pendenciaAbertasAdvSustentacaoOralIdPend(idProcesso, idUsuLogado, idServentia);
	}
	
	public List pendenciaAbertasSecretarioSustentacaoOral(String idProcesso) throws Exception {
		VotoNe votoNe = new VotoNe();
		return votoNe.pendenciaAbertasSecretarioSustentacaoOral(idProcesso);
	}

	public List consultarAdvogadosProcesso(String id_Processo, FabricaConexao fabrica) throws Exception {
		return new ProcessoParteAdvogadoNe().consultarAdvogadosProcesso(id_Processo, fabrica);
	}
	
	public HashMap<Long, List<ProcessoParteAdvogadoDt>> consultarAdvogadosProcessoMNI(String id_Processo, FabricaConexao fabrica) throws Exception {
		return new ProcessoParteAdvogadoNe().consultarAdvogadosProcessoMNI(id_Processo, fabrica);
	}
	
	private void enviarEmailArquivamento(String serventia, String numeroProcesso, List listAdvogados) {
		if (listAdvogados == null) {
			return;
		}
		for (Iterator iterator = listAdvogados.iterator(); iterator.hasNext();) {
			ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) iterator.next();
			if (processoParteAdvogadoDt.hasEmail()) {
				String mensagemEmail = "<table width='98%' border='0' cellspacing='2' cellpadding='2'>";
				mensagemEmail += "  <tr><td colspan='2'>O processo foi Arquivado.</td> </tr>";
				mensagemEmail += "  <tr> <td width='80'>Número:</td> <td><b> "+ numeroProcesso + "</b> </td> </tr> ";
				mensagemEmail += "  <tr> <td>Serventia: </td> <td> <b> "+ serventia + "</b></td> </tr>";
				mensagemEmail += "  <tr> <td>Data:</td> <td><b> "+ Funcoes.FormatarDataHora(new Date()) + "</b></td> </tr>";
				mensagemEmail += "</table>";
				mensagemEmail += "<br />Se for necessário acesse o Sistema de Processo Eletrônico e verifique o processo. <br/>";   
				new GerenciadorEmail(processoParteAdvogadoDt.getNomeAdvogado(), processoParteAdvogadoDt.getEmail(), "Processo Arquivado", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
			}
		}
	}
	
	/**
	 * Consulta para verificar se um processo está concluso
	 * 
	 * @param id_Processo, id do processo	
	 *  
	 * @return boolean
	 * @author lsbernardes
	 */
	public boolean processoConcluso(String idProcesso) throws Exception {
		return  new PendenciaNe().processoConcluso(idProcesso);
   }
	
	public String consultarAssuntosAreaDistribuicaoPjdJSON(String tempNomeBusca, String Id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();		
		stTemp = neObjeto.consultarAssuntosAreaDistribuicaoPjdJSON(Id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoPjdJSON(String tempNomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
		List areasDistribuicoes = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(id_Serventia);
		stTemp = neObjeto.consultarAssuntosAreasDistribuicoesPjdJSON(areasDistribuicoes, tempNomeBusca, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public ProcessoDt consultarProcessoNumero(String numeroProcesso) throws Exception {
		ProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {
			dtRetorno = consultarProcessoNumero(numeroProcesso, obFabricaConexao);
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ProcessoDt consultarProcessoNumero(String numeroProcesso, FabricaConexao obFabricaConexao) throws Exception {
		String numero = null;
		String digitoVerificador = null;
		String ano = null;
		String forumCodigo = null;
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());				
		String[] stTemp = numeroProcesso.replaceAll("-", ".").replaceAll(",", ".").split("\\.");
		if (stTemp.length >= 1) numero = stTemp[0];			
		if (stTemp.length >= 2) digitoVerificador = stTemp[1];
		if (stTemp.length >= 3) ano = stTemp[2];
		if (stTemp.length >= 6) forumCodigo = stTemp[5];
		return obPersistencia.consultarProcessoNumero(numero, digitoVerificador, ano, forumCodigo);		
	}
	
	public String consultarProcessosDistribuidos(String data, String comarcaCodigo, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarProcessosDistribuidos(data, comarcaCodigo, posicao);
			QuantidadePaginas = Funcoes.StringToLong(stTemp.substring(stTemp.length()-5));
			stTemp = stTemp.substring(0, stTemp.length()-5);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public void atualizarProcessoMistoParaDigitalPendenteLimpezaDataSPG(String id_processo,FabricaConexao obFabricaConexao) throws Exception {
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obPersistencia.atualizarProcessoMistoParaDigitaPendenteLimpezaDataSPG(id_processo);	
	}
	
	public void atualizarProcessoMistoParaDigitalConcluidaLimpezaDataSPG(String id_processo,FabricaConexao obFabricaConexao) throws Exception {
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obPersistencia.atualizarProcessoMistoParaDigitaConcluidaLimpezaDataSPG(id_processo);	
	}
	
	public List<ProcessoDt> consultarProcessosConvertidosMistoParaDigitalPentendeLimpezaDataSPG(FabricaConexao obFabricaConexao, int quantidade) throws Exception {
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarMistoSPGPendenteDeLimpezaSPG(quantidade);	
	}
	
	public void atualizarProcessoDigitalParaMisto(String id_processo,FabricaConexao obFabricaConexao) throws Exception {
		ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
		obPersistencia.atualizarProcessoDigitalParaMisto(id_processo);	
	}

	public List consultarIntimacoesPromotoria(UsuarioNe usuarioSessao) throws Exception {
		return new PendenciaNe().consultarIntimacoesPromotoria(usuarioSessao);
	}

	public List consultarIntimacoesLidasDistribuicaoPromotoria(UsuarioNe usuarioSessao) throws Exception {
		return new PendenciaNe().consultarIntimacoesLidasDistribuicaoPromotoria(usuarioSessao);
	}
	
	private void validacaoAntiFraudeDadosGuia(ProcessoCadastroDt processoCadastroDt, GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexao) throws Exception {
		if( processoCadastroDt != null && guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && guiaEmissaoDt.getNumeroGuiaCompleto() != null && obFabricaConexao != null ) {
			
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			
			if(guiaEmissaoNe.isNumeroGuiaProjudi(guiaEmissaoDt.getNumeroGuiaCompleto())) {
			
				//consulta a guia
				GuiaEmissaoDt guiaBanco = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
				
				if( guiaBanco != null && guiaBanco.getId() != null ) {
					//valida valor da causa
					if( processoCadastroDt.getValor() != null && !processoCadastroDt.getValor().isEmpty() ) {
						if( guiaBanco.getValorAcao() != null && !guiaBanco.getValorAcao().isEmpty() ) {
							BigDecimal valorProcesso = new BigDecimal(processoCadastroDt.getValor().replace(".", "").replace(",",".")).setScale(2, BigDecimal.ROUND_CEILING);
							BigDecimal valorGuia = new BigDecimal(guiaBanco.getValorAcao()).setScale(2, BigDecimal.ROUND_CEILING);
							
							if( valorProcesso.compareTo(valorGuia) != 0 ) {
								throw new MensagemException("Erro ao Validar Dados.(Inconsistência no Valor da Causa)");
							}
						}
					}
					
					//valida nome do requerente
					boolean nomePresente = false;
					for( int i = 0; i < processoCadastroDt.getPartesProcesso().size(); i++ ) {
						ProcessoParteDt parte = (ProcessoParteDt) processoCadastroDt.getPartesProcesso().get(i);
						if( parte != null && parte.getNome() != null && guiaBanco.getRequerente() != null && parte.getNome().toUpperCase().equals(guiaBanco.getRequerente().toUpperCase()) ) {
							nomePresente = true;
							break;
						}
					}
					if( !nomePresente ) {
						throw new MensagemException("Erro ao Validar Dados.(Inconsistência no Nome do Requerente informado na Guia)");
					}
					
					//valida id_proc_tipo
					if( processoCadastroDt.getId_ProcessoTipo() != null && guiaBanco.getId_ProcessoTipo() != null && !processoCadastroDt.getId_ProcessoTipo().equals(guiaBanco.getId_ProcessoTipo()) ) {
						throw new MensagemException("Erro ao Validar Dados.(Inconsistência na Classe informada na Guia e Processo)");
					}
				}
				else {
					throw new MensagemException("Erro ao Validar Dados.(Guia "+ guiaEmissaoDt.getNumeroGuiaCompleto() +" não encontrada na base de dados)");
				}
			}
		}
	}

	/**
	 * Verifica se o processo é híbrido.
	 * @param idProcesso
	 * @return
	 * @throws Exception
	 */
	public boolean verificarProcessoHibrido(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean retorno = false;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.verificarProcessoHibrido(idProcesso);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * A condição adicionada no método consultarId() - condição que trata sobre recursos - impedia que processos
	 * com recursos não autuados aparecessem na consulta, ocasionando erro na troca de MP responsável pelo
	 * processo. Como o coordenador de MP deve poder trocar o MP responsável independente de recurso
	 * autuado ou não, foi criado o método abaixo que não usa o critério novo.  
	 * 
	 * @param id_processo
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoIdTrocaResponsavel(String id_processo ) throws Exception {

		ProcessoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarProcessoIdTrocaResponsavel(id_processo ); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public String getGabineteUpjResposavelProcesso(String id_processo, FabricaConexao conexao) throws Exception {
		String id_serventia= null;
			
		ProcessoPs obPersistencia = new ProcessoPs(conexao.getConexao());
		id_serventia= obPersistencia.getGabineteUpjResposavelProcesso(id_processo ); 
						
		return id_serventia;
	}

	public boolean isGabineteUpjResposavelProcesso(String id_Serventia, String id_proc) throws Exception {
		boolean boRetorno=false;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			boRetorno= obPersistencia.isGabineteUpjResposavelProcesso(id_Serventia, id_proc ); 			
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	
	/**
	 * ATENÇÃO: ESTE MÉTODO É EXCLUSIVO PARA O RETORNO AUTOMÁTICO DE PROCESSOS ENCAMINHADOS AO SÉTIMO CEJUSC.
	 * SERÁ UTILIZADO EXCLUSIVAMENTE PELO SERVIÇO DE EXECUÇÃO AUTOMÁTICA DESENVOLVIDO PARA ESTE FIM. NÃO
	 * UTILIZAR EM OUTRAS SITUAÇÕES. 
	 * 
	 * @author hrrosa
	 * @return
	 * @throws Exception
	 */
	public void retornoAutomaticoCEJUSCServentiaOrigemEncaminhamento(String idArquivo) throws Exception {
		
		// DECLARAÇÃO DE VARIÁVEIS
		List<ProcessoDt> listaProcessos = null;
		String nomeTabelaLog = "FechamentoAutomaticoRetornoCEJUSC";
		List<ArquivoDt> listaArquivos = new ArrayList<ArquivoDt>();
		LogDt logDt = new LogDt();
		UsuarioDt usuarioDt = new UsuarioDt();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoPs processoPs = new ProcessoPs(obFabricaConexao.getConexao());
		
		//Atribui informações do usuário Sistema Projudi e do LogDt
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
		usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");
		
		logDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		
		
		
		// 0) CONSULTA ARQUIVO ASSINADO QUE SERÁ USADO NAS MOVIMENTAÇÕES
		if(Funcoes.StringToLong(idArquivo) == 0){
			throw new MensagemException("Erro: O id do arquivo especificado ("+idArquivo+") é inválido.");
		}
		listaArquivos.add(arquivoNe.consultarId(idArquivo));
		
		// 1) CONSULTA LISTA DOS PROCESSOS A SEREM RETORNADOS
		listaProcessos = processoPs.listarProcessosRetornoAutomaticoCEJUSC();
		
		// 2) CHAMA MÉTODO DE RETORNO PARA CADA ITEM DA LISTA
		for(ProcessoDt processoDt: listaProcessos){
			
			try {
				obFabricaConexao.iniciarTransacao();
				processoDt = this.consultarIdCompleto(processoDt.getId());
				
				//FECHAR PENDÊNCIAS DO PROCESSO
				pendenciaNe.fecharPendenciasRetornoAutomaticoCEJUSC(processoDt, nomeTabelaLog, obFabricaConexao);
				
				//FINALIZAR AS AUDIÊNCIAS DO PROCESSO
				audienciaProcessoNe.fecharAudienciasRetornoAutomaticoCEJUSC(processoDt, nomeTabelaLog, obFabricaConexao);
				
				//FAZER RETORNO DO PROCESSO
				this.salvarRetornoServentiaOrigemEncaminhamentoAutomaticoCEJUSC(processoDt, usuarioDt, listaArquivos, logDt, false, obFabricaConexao);
				
				obFabricaConexao.finalizarTransacao();
			}
			catch(Exception e) {
				obFabricaConexao.cancelarTransacao();
				new LogNe().salvar(new LogDt("JobRetornoAutomaticoEncaminhamentoCEJUSC", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Erro), "ERRO Retorno Automático Do Processo ID " + processoDt.getId() + ": " + Funcoes.DataHora(new Date()), e.getMessage()));
			}
			
		}
		
		obFabricaConexao.fecharConexao();
		
	}

	/**
	 * ATENÇÃO: ESTE MÉTODO É EXCLUSIVO PARA O ARQUIVAMENTO AUTOMÁTICO DOS PROCESSOS DE EXECUÇÃO PENAL.
	 * SERÁ UTILIZADO EXCLUSIVAMENTE PELO SERVIÇO DE EXECUÇÃO AUTOMÁTICA DESENVOLVIDO PARA ESTE FIM. NÃO
	 * UTILIZAR EM OUTRAS SITUAÇÕES. FUNCIONALIDADE FEITA MEDIANTE PROAD 201905000168973.
	 * 
	 * @author hrrosa
	 * @return
	 * @throws Exception
	 */
	public void arquivamentoAutomaticoProcessosExecucaoPenal(String idArquivo, String caminhoArquivoEntrada) throws Exception {
		
		// DECLARAÇÃO DE VARIÁVEIS
		List<String> listaIdProcessos = null;
		String nomeTabelaLog = "ArquivamentoAutomaticoExecucaoPenal";
		String idMoviArquivamento = null;
		String podeArquivar = null;
		String idProcTmp = null;
		List<ArquivoDt> listaArquivos = new ArrayList<ArquivoDt>();
		LogDt logDt = new LogDt();
		LogDt logSumarioDt  = new LogDt();
		UsuarioDt usuarioDt = new UsuarioDt();
		ProcessoDt processoDtTmp = null;
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		boolean checarServentia = caminhoArquivoEntrada.contains("PROAD_201905000168973");
		
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoPs processoPs = new ProcessoPs(obFabricaConexao.getConexao());
		
		//Atribui informações do usuário Sistema Projudi e do LogDt
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
		usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");
		
		logDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		logSumarioDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logSumarioDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		
		
		
		// 0) CONSULTA ARQUIVO ASSINADO QUE SERÁ USADO NAS MOVIMENTAÇÕES
		if(Funcoes.StringToLong(idArquivo) == 0){
			throw new MensagemException("Erro: O id do arquivo especificado ("+idArquivo+") é inválido.");
		}
		listaArquivos.add(arquivoNe.consultarId(idArquivo));
		
		// 1) CONSULTA LISTA DOS PROCESSOS A SEREM ARQUIVADOS
		BufferedReader entrada 	= new BufferedReader(new FileReader(caminhoArquivoEntrada));
		String linha = null;
		
		// 2) CHAMA MÉTODO DE RETORNO PARA CADA ITEM DA LISTA
		linha = entrada.readLine();
		while(linha != null){
			
			try {
				obFabricaConexao.iniciarTransacao();
				//CONSULTA O PROCESSO
				idProcTmp = this.consultarIdProcessoNumeroCompletoDigitoAno(linha, obFabricaConexao);
				processoDtTmp = processoPs.consultarIdCompleto(idProcTmp);

				if(processoDtTmp == null){
					throw new MensagemException("Não foi possível retornar o processo pelo número: " + linha);
				}
				
				//TESTA SE O PROCESSO ENCONTRA-SE EM UMA DAS SERVENTIAS ESPECIFICADAS PARA O PROAD E SE ENCONTRA-SE ATIVO
				podeArquivar = this.checagemPreviaArquivamentoAutomatico(processoDtTmp, checarServentia);
				if( podeArquivar != null && !podeArquivar.isEmpty()) {
					throw new MensagemException("O processo ID " + processoDtTmp.getId() + " não foi arquivado: " + podeArquivar);
				}
				
				//FECHAR PENDÊNCIAS DO PROCESSO
				pendenciaNe.fecharPendenciasArquivamentoAutomaticoExecucaoPenal(processoDtTmp, nomeTabelaLog, obFabricaConexao);
				
				//FINALIZAR AS AUDIÊNCIAS DO PROCESSO
				audienciaProcessoNe.fecharAudienciasArquivamentoAutomaticoExecucaoPenal(processoDtTmp, nomeTabelaLog, obFabricaConexao);
				
				//ARQUIVAR O PROCESSO
				podeArquivar = this.podeArquivarArquivamentoAutomatico(processoDtTmp, obFabricaConexao );
						
				if( podeArquivar == null || podeArquivar.isEmpty()) {
					this.limparClassificadorProcesso(processoDtTmp, obFabricaConexao);
					this.arquivarProcesso(processoDtTmp, logDt, usuarioDt.getId_UsuarioServentia(), "[ARQUIVAMENTO AUTOMÁTICO]", "", obFabricaConexao);
					
				} else {
					throw new MensagemException(podeArquivar);
				}
				
				//VINCULAR MOVIMENTAÇÃO GERADA AO ARQUIVO ASSINADO
				idMoviArquivamento = movimentacaoNe.consultarIdMoviUltimoArquivamento(processoDtTmp.getId(), obFabricaConexao);
				if(Funcoes.StringToLong(idMoviArquivamento) != 0){
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(listaArquivos, idMoviArquivamento, String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL), logDt, obFabricaConexao);
					idMoviArquivamento = null;
				} else{
					throw new MensagemException("Não foi possível localizar a movimentação de arquivamento para vincular o arquivo assinado.");
				}
				
				
				//REGISTRAR LOG				
				logSumarioDt = new LogDt("ArquivamentoAutomaticoExecucaoPenalSumario", processoDtTmp.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), processoDtTmp.getProcessoNumeroCompleto(), processoDtTmp.getPropriedades());
				obLog.salvar(logSumarioDt, obFabricaConexao);
				
				obFabricaConexao.finalizarTransacao();
				
			}
			catch(Exception e) {
				obFabricaConexao.cancelarTransacao();
				new LogNe().salvar(new LogDt("JobArquivamentoAutomaticoExecucaoPenal", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Erro), "ERRO Arquivamento Automático Do Processo Número: " + linha + " - " + Funcoes.DataHora(new Date()), e.getMessage()));
				new LogNe().salvar(new LogDt("JobArquivamentoAutomaticoExecucaoPenal_ERROS", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Erro), "ERRO Arquivamento Automático Do Processo Número: " + linha + " - " + Funcoes.DataHora(new Date()), e.getMessage()));
			}
			finally {
				
				//REINICIA VARIÁVEIS 
				idProcTmp = null;
				processoDtTmp = null;
				podeArquivar = null;
				idMoviArquivamento = null;
				
				//LÊ O PRÓXIMO NÚMERO DE PROCESSO
				linha = entrada.readLine();
			}
			
		}
		
		if(entrada != null){ 
			entrada.close(); 
		}
		obFabricaConexao.fecharConexao();
		
	}
	
	protected String checagemPreviaArquivamentoAutomatico(ProcessoDt processoDt, boolean checarServentia){
		String mensagem = "";
		
		if(processoDt == null) {
			mensagem += "ProcessoDt informado com valor null. ";
		}
		else if(processoDt.isArquivado()) {
			mensagem += "O processo já se encontra arquivado. ";
		}
		
		//Checa se o processo pertence a alguma das serventias especificadas no PROAD
		int idServProc = Funcoes.StringToInt(processoDt.getId_Serventia());
		if(checarServentia && 
				(
				 idServProc != 1124 && //(Goiânia - 1ª Vara de Execução Penal)
				 idServProc != 1126 && //(Goiânia - 2ª Vara de Execução Penal)
				 idServProc != 4596 && //(Goiânia - 3ª Vara de Execução Penal)
				 idServProc != 1915 && //(Anápolis - Execução Penal/EXECPENWEB)
				 idServProc != 4756 && //(Formosa - Vara Regional de Execução Penal)
				 idServProc != 1925    //(Formosa - Execução Penal/EXECPENWEB)
				 )){
			mensagem += "O processo localizado não pertence à nenhuma das serventias especificadas no PROAD. ";
		}
		
		return mensagem;
	} 
	
	/**
	 * ATENÇÃO: ESTE MÉTODO É EXCLUSIVO PARA A TROCA AUTOMÁTICA DE NÚMERO DOS PROCESSOS QUE TIVERAM O SEU NÚMERO DUPLICADO
	 * NO SEEU. SERÁ UTILIZADO EXCLUSIVAMENTE PELO SERVIÇO DE EXECUÇÃO AUTOMÁTICA DESENVOLVIDO PARA ESTE FIM. NÃO
	 * UTILIZAR EM OUTRAS SITUAÇÕES. FUNCIONALIDADE FEITA MEDIANTE PROAD 202004000222292.
	 * 
	 * @author hrrosa
	 * @return
	 * @throws Exception
	 */
	public void trocaAutomaticaNumeroProcesso(String idArquivo, String caminhoArquivoEntrada, String caminhoArquivoCorretos, String caminhoArquivoErros) throws Exception {
		
		// DECLARAÇÃO DE VARIÁVEIS
		List<String> listaIdProcessos = null;
		String nomeTabelaLog = "TrocaAutomaticaNumeroProcesso";
		String idMoviArquivamento = null;
		String podeArquivar = null;
		List<ArquivoDt> listaArquivos = new ArrayList<ArquivoDt>();
		LogDt logDt = new LogDt();
		LogDt logSumarioDt  = new LogDt();
		UsuarioDt usuarioDt = new UsuarioDt();
		MovimentacaoDt movimentacaoDt = null;
		ProcessoDt processoDtTmp = null;
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoPs processoPs = new ProcessoPs(obFabricaConexao.getConexao());
		
		//Atribui informações do usuário Sistema Projudi e do LogDt
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
		usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");
        usuarioDt.setIpComputadorLog("Servidor");
		
		logDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		logSumarioDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logSumarioDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		
		BufferedWriter arquivoCorretos = new BufferedWriter(new FileWriter(caminhoArquivoCorretos));
		BufferedWriter arquivoErros = new BufferedWriter(new FileWriter(caminhoArquivoErros));
		
		// 0) CONSULTA ARQUIVO ASSINADO QUE SERÁ USADO NAS MOVIMENTAÇÕES
		if(Funcoes.StringToLong(idArquivo) == 0){
			throw new MensagemException("Erro: O id do arquivo especificado ("+idArquivo+") é inválido.");
		}
		listaArquivos.add(arquivoNe.consultarId(idArquivo));
		
		// 1) CONSULTA LISTA DOS PROCESSOS A SEREM PROCESSADOS
		BufferedReader entrada 	= new BufferedReader(new FileReader(caminhoArquivoEntrada));
		String[] numeroSeparado = null;
		String numeroNovo = null;
		String numeroAntigo = null;
		String linha = null;
		String linhaNumero = null;
		String linhaIdProc = null;
		String numeroFrac = null;
		String dvFrac = null;
		String anoFrac = null;
		String forumCodigoFrac = null;
		
		
			//LÊ O PRIMEIRO NÚMERO DE PROCESO
			linha = entrada.readLine();
			
			while(linha != null){
				try {
				
					obFabricaConexao.iniciarTransacao();
					
					if(linha.length() == 20) {
						numeroFrac = linha.substring(0, 7);
						dvFrac = linha.substring(7, 9);
						anoFrac = linha.substring(9, 13);
						forumCodigoFrac = linha.substring(16,20);
					} else {
						throw new MensagemException("Linha em formato inválido.");
					}
					
					//CONSULTAR PROCESSO
//					processoDtTmp = this.consultarProcessoNumeroCompletoDigitoAnoIdProc(linhaIdProc, linhaNumero, obFabricaConexao);
					processoDtTmp = consultarProcessoNumeroCompleto(numeroFrac + '-' + dvFrac + '.' + anoFrac + ".8.09." + forumCodigoFrac);
					
					if(processoDtTmp == null){
						throw new MensagemException("Número inválido: " + linha);
					}
									
					processoDtTmp = this.consultarIdCompleto(processoDtTmp.getId(), obFabricaConexao);
					
					//ALTERAR NÚMERO DO PROCESSO
					numeroAntigo = processoDtTmp.getProcessoNumeroCompleto();
					
					
					//Se o processo for do ano corrento, retorna o próximo número do sequence
					//senão retorna o próximo número do ano do processo, utilizando método específico para tal 
					if(Funcoes.StringToInt(processoDtTmp.getAno()) == Calendar.getInstance().get(Calendar.YEAR)){ 
						numeroNovo = ProcessoNumero.getInstance().getProcessoNumero(processoDtTmp.getForumCodigo());
					}
					else {
						numeroNovo = this.retornaProximoProcessoNumeroAnoEspecifico(processoDtTmp.getAno(), processoDtTmp.getForumCodigo(), obFabricaConexao);
					}
					
					processoDtTmp.setAnoProcessoNumero(numeroNovo);
					processoDtTmp.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
					this.alterarNumeroProcesso(processoDtTmp, obFabricaConexao);
					
					//REGISTRAR MOVIMENTAÇÃO
					movimentacaoDt = movimentacaoNe.gerarMovimentacaoNumeroProcessoAlterado(processoDtTmp.getId(), usuarioDt.getId_UsuarioServentia(), "(Número Anterior: " + numeroAntigo + " - Número Novo: " + processoDtTmp.getProcessoNumeroCompleto() + ")", logDt, obFabricaConexao);
					movimentacaoDt = movimentacaoNe.consultarId(movimentacaoDt.getId(), obFabricaConexao);
					
					//VINCULA MOVIMENTAÇÃO AO ARQUIVO ESPECIFICADO
					movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(listaArquivos, movimentacaoDt.getId(),String.valueOf(MovimentacaoArquivoDt.ACESSO_NORMAL), logDt, obFabricaConexao);
					
					
					//INTIMA PARTES
					//Em reunião com o Jesus no dia 29/06/2020 definimos que erros na intimação não impedirão a troca do número do processo.
					try {
						pendenciaNe.processoIntimarPartesJobTrocaAutomaticaNumero(processoDtTmp, listaArquivos, movimentacaoDt, usuarioDt, logDt, obFabricaConexao);
					} catch(MensagemException me) {
						new LogNe().salvar(new LogDt("JobTrocaAutomaticaNumeroProcesso", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Erro), "INFO Uma intimação não foi realizada. Processo Numero: " + linha + " : " + Funcoes.DataHora(new Date()), me.getMessage()));
					}
					
					
					//REGISTRAR LOG				
					logSumarioDt = new LogDt("TrocaNumeroProcessoSumario", processoDtTmp.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), numeroAntigo, processoDtTmp.getProcessoNumeroCompleto());
					obLog.salvar(logSumarioDt, obFabricaConexao);
	
					arquivoCorretos.append(linha + " -> " + processoDtTmp.getProcessoNumeroCompleto() + " (id_proc = " + processoDtTmp.getId() + ")\n");
					
					//FINALIZA TRANSAÇÃO
					obFabricaConexao.finalizarTransacao();
					
				}
				catch(Exception e) {
					idMoviArquivamento = null;
					podeArquivar = null;
					obFabricaConexao.cancelarTransacao();
					new LogNe().salvar(new LogDt("JobTrocaAutomaticaNumeroProcesso", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Erro), "ERRO Troca Automática De Número Do Processo Numero: " + linha + " : " + Funcoes.DataHora(new Date()), e.getMessage()));
					arquivoErros.append(linha + " -- " + Funcoes.DataHora(new Date()) + " -- " + e.getMessage() + '\n');
				}
				finally {
					//REINICIA VARIÁVEIS
					processoDtTmp = null;
					numeroNovo = null;
					numeroAntigo = null;
					movimentacaoDt = null;
					linhaIdProc = null;
					linhaNumero = null;
					numeroFrac = null;
					dvFrac = null;
					anoFrac = null;
					forumCodigoFrac = null;
					
					//LÊ O PRÓXIMO NÚMERO DE PROCESSO
					linha = entrada.readLine();
				}
				
			}
			
		
		if(entrada != null){ 
			entrada.close(); 
		}
		if(arquivoCorretos != null){ 
			arquivoCorretos.close(); 
		}
		if(arquivoErros != null){ 
			arquivoErros.close(); 
		}
		
		obFabricaConexao.fecharConexao();
	}
		
	
	public List consultarProcessosClassificar(BuscaProcessoDt buscaProcesso, String posicao) throws Exception {
		List<?> listaProcessos = null;
				
		buscaProcesso.validarEntradas(objBuscaProcessoDt);
		//objBuscaProcessoDt=buscaProcesso;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			listaProcessos = obPersistencia.consultarProcessosClassificar(	buscaProcesso.getCpfPoloAtivo(),
																			Funcoes.StringToBoolean(buscaProcesso.getPoloAtivoNull()), 
																			buscaProcesso.getCpfPoloPassivo(), 
																			Funcoes.StringToBoolean(buscaProcesso.getStPoloPassivoNull()), 
																			buscaProcesso.getId_ProcessoTipo(),
																			buscaProcesso.getId_Assunto(), 
																			buscaProcesso.getId_Classificador(),
																			buscaProcesso.getMaxValor(), 
																			buscaProcesso.getMinValor(), 
																			buscaProcesso.getId_Serventia(), posicao);
			setQuantidadePaginas(listaProcessos);
		} finally {
			obFabricaConexao.fecharConexao();
		}
			
		return listaProcessos;
	}

	public int classificarProcessos(String cpfPoloAtivo, boolean boPoloAtivoNull, String cpfPoloPassivel, boolean boPoloPassivoNull, String id_proc_tipo, String id_assunto, String id_classificador, String maxValor, String minValor, String id_classificarAlteracao, String id_serv) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			return obPersistencia.classificarProcessos(cpfPoloAtivo, boPoloAtivoNull, cpfPoloPassivel, boPoloPassivoNull, id_proc_tipo, id_assunto, id_classificador, maxValor, minValor, id_classificarAlteracao, id_serv);
		} finally {

			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que verifica se um processo pode ser arquivado. Verifica se: - Não
	 * há autos conclusos - Não há audiências/sessões em aberto - Não há
	 * pendências em aberto - Processo está ativo - No caso de turmas recursais,
	 * deve verificar se processo é do 2º grau
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @author msapaula
	 */
	public String podeArquivarArquivamentoAutomatico(ProcessoDt processoDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		String stRetorno = "";
		
		// Verifica se há conclusões em aberto
		if(pendenciaNe.verificaConclusoesAbertas(processoDt.getId(), obFabricaConexao)){
			stRetorno += "Há Coclusões abertas";
		}

		// Verifica audiências pendentes
		if (audienciaProcessoNe.verificaAudienciasPendentesProcesso(processoDt.getId(), obFabricaConexao)) {
			if (processoDt.getId_Recurso().length() == 0) stRetorno += "Há Audiência(s) em Aberto. \n";
			else
				stRetorno += "Há Sessão em Aberto. \n";
		}

		if (pendenciaNe.verificaPendenciasProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao)) stRetorno += "Há pendência(s) em aberto. \n";

		// Só pode permitir arquivamento para ações do 2º grau e não para
		// recurso inominado
		if (processoDt.getId_Recurso().length() > 0) {
			stRetorno += " Não é possível Arquivar um Recurso Inominado. \n";
		}

		//verifica se tem mandado de prisão não cumprido
		if (new MandadoPrisaoNe().isExisteMandadoPrisaoProcesso_NaoCumprido(processoDt.getId(), obFabricaConexao)){
			stRetorno += " Há Mandado(s) de Prisão não Cumprido. \n";
		}
		
		if (stRetorno.length() > 0) stRetorno = "Processo " + Funcoes.formataNumeroProcesso(processoDt.getProcessoNumero()) + " não pode ser arquivado. " + stRetorno;

		return stRetorno;
	}
	
	/**
	 * ATENÇÃO: ESTE MÉTODO FOI CRIADO ESPECIFICAMENTE PARA O SERVIÇO DE TROCA AUTOMÁTICA DE NÚMERO DE PROCESSOS.
	 * 
	 * Método criado para situações em que se precisa do próximo número de processo disponível para um ano passado,
	 * quando não é mais possível utilizar o sequence que já foi zerado. Retorna o maior número daquele ano e
	 * incrementa 1.
	 *  
	 * @param String ano
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	String retornaProximoProcessoNumeroAnoEspecifico(String ano, String codigoForum, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoPs processoPs = new ProcessoPs(obFabricaConexao.getConexao());
		String numeroNovo = null;
		long loForumCodigo = Funcoes.StringToLong(codigoForum,-9999);
		
		//SITUAÇÕES ESPECIAIS MOSTRARAM QUE PRECISAREMOS PROCESSAR NÚMEROS COM ANO E CODIGOFORUM ZERADOS
//		if(Funcoes.StringToInt(ano) == 0 || Funcoes.StringToInt(codigoForum) == 0){
//			throw new MensagemException("Parâmetros inválidos");
//		}
		
		if(Funcoes.StringToInt(ano) == Calendar.getInstance().get(Calendar.YEAR)){
			throw new MensagemException("Não pode ser utilizado para o ano corrente");
		}
		
		numeroNovo = processoPs.retornaProximoProcessoNumeroAnoEspecifico(ano);
		
		String stDigito = Funcoes.calcula_mod97(Funcoes.StringToLong(numeroNovo), Funcoes.StringToLong(ano), Funcoes.StringToLong(Configuracao.JTR.replace(".", "")),  loForumCodigo );
		numeroNovo = Funcoes.completarZeros(String.valueOf(ano),4) + Funcoes.completarZeros(String.valueOf(numeroNovo),7) + stDigito;

		return numeroNovo;
	}
	
	/**
	 * Consulta a quantidade de processos de um serventia que estão sem assunto 
	 * @param id_Serventia identificação da serventia
	 * @author acbloureiro
	 */
	public long consultarQuantidadeProcessosSemAssunto(String id_Serventia) throws Exception {
		long loQuantidade = 0;
				
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			loQuantidade = obPersistencia.consultarQuantidadeProcessosSemAssunto(id_Serventia);
				
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	public List consultarProcessosSemAssunto(String id_Serventia, String posicao) throws Exception {
		List listaProcessos = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.consultarProcessosSemAssunto(id_Serventia,  posicao);

			if( listaProcessos != null ) {
				setQuantidadePaginas(listaProcessos);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * Consulta a quantidade de processos de um serventia que estão com assunto Pai
	 * @param id_Serventia identificação da serventia
	 * @author acbloureiro
	 */
	public long consultarQuantidadeProcessosComAssuntoPai(String id_Serventia) throws Exception {
		long loQuantidade = 0;
				
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			loQuantidade = obPersistencia.consultarQuantidadeProcessosComAssuntoPai(id_Serventia);
				
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	
	public List consultarProcessosComAssuntoPai(String id_Serventia, String posicao) throws Exception {
		List listaProcessos = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.consultarProcessosComAssuntoPai(id_Serventia,  posicao);

			if( listaProcessos != null ) {
				setQuantidadePaginas(listaProcessos);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * Consulta a quantidade de processos de um serventia que estão com classe Pai 
	 * @param id_Serventia identificação da serventia
	 * @author acbloureiro
	 */
	public long consultarQuantidadeProcessosComClassePai(String id_Serventia) throws Exception {
		long loQuantidade = 0;
				
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			loQuantidade = obPersistencia.consultarQuantidadeProcessosComClassePai(id_Serventia);
				
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	
	public List consultarProcessosComClassePai(String id_Serventia, String posicao) throws Exception {
		List listaProcessos = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.consultarProcessosComClassePai(id_Serventia,  posicao);

			if( listaProcessos != null ) {
				setQuantidadePaginas(listaProcessos);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	/**
	 * Consulta a quantidade de processos criminais de um serventia arquivados que estão sem o motivo do arquivamento 
	 * @param id_Serventia identificação da serventia
	 * @author jrcorrea
	 */
	public long consultarQuantidadeArquivadosSemMovito(String id_Serventia) throws Exception {
		long loQuantidade = 0;
				
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			loQuantidade = obPersistencia.consultarQuantidadeArquivadosSemMovito(id_Serventia);
				
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	public List consultarProcessosArquivadosSemMovito(String id_Serventia, String posicao) throws Exception {
		List listaProcessos = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.consultarProcessosArquivadosSemMovito(id_Serventia,  posicao);

			if( listaProcessos != null ) {
				setQuantidadePaginas(listaProcessos);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public long consultarQuantidadeInconsistenciaPoloPassivo(String id_Serventia) throws Exception {
		long loQuantidade = 0;
				
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			loQuantidade = obPersistencia.consultarQuantidadeInconsistenciaPoloPassivo(id_Serventia);
				
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	public List consultarProcessosInconsistenciaPoloPassivo(String id_Serventia, String posicao) throws Exception {
		List listaProcessos = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.consultarProcessosInconsistenciaPoloPassivo(id_Serventia,  posicao);

			if( listaProcessos != null ) {
				setQuantidadePaginas(listaProcessos);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public long consultarQuantidadePrisaoForaPrazo(String id_Serventia) throws Exception {
		long loQuantidade = 0;
				
		FabricaConexao obFabricaConexao = null;

		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			loQuantidade = obPersistencia.consultarQuantidadePrisaoForaPrazo(id_Serventia);
				
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	public List consultarProcessosPrisaoForaPrazo(String id_Serventia, String posicao) throws Exception {
		List listaProcessos = null;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());

			listaProcessos = obPersistencia.consultarProcessosPrisaoForaPrazo(id_Serventia,  posicao);

			if( listaProcessos != null ) {
				setQuantidadePaginas(listaProcessos);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaProcessos;
	}
	
	public ProcessoDt consultarDadosProcessoMNI(String id_Processo, 
			                                    UsuarioDt usuarioDt, 
			                                    int nivelAcesso,
			                                    boolean incluiMovimentos,
			                                    FabricaConexao obFabricaConexao) throws Exception {
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoDt processoDt = null;

		processoDt = this.consultarIdCompleto(id_Processo, obFabricaConexao);			

		// Verifica se processo possui apensos
		processoDt.setTemApensos(this.verificaProcessosApensos(id_Processo, obFabricaConexao));
		
		if (processoDt.hasVitima()) {
			if(!this.podeAcessarProcesso(usuarioDt, processoDt, obFabricaConexao)){			
				if (processoDt.getListaOutrasPartes() != null) {
					for (int j = 0; j < processoDt.getListaOutrasPartes().size(); j++) {
						ProcessoParteDt parteDt = (ProcessoParteDt) processoDt.getListaOutrasPartes().get(j);
						if (parteDt.isVitima()){
							parteDt.setNome(Funcoes.iniciaisNome(parteDt.getNome()));
						}
					}
				}
			}
		}
		
		// Captura lista de assuntos
		processoDt.setListaAssuntos(processoAssuntoNe.consultarAssuntosProcesso(id_Processo, obFabricaConexao));

		// Verifica se é processo criminal e consulta informações adicionais
		if (processoDt != null && processoDt.isCriminal()) {
			ProcessoCriminalNe processoCriminalNe = new ProcessoCriminalNe();
			processoDt.setProcessoCriminalDt(processoCriminalNe.consultarIdProcesso(id_Processo, obFabricaConexao));
			processoDt.getProcessoCriminalDt().setId_Processo(processoDt.getId());
		}
		
		if (incluiMovimentos) {
			// Captura movimentações do processo
			List movimentacoes = movimentacaoNe.consultarMovimentacoesProcesso(usuarioDt, processoDt, false, obFabricaConexao, nivelAcesso);
			processoDt.setListaMovimentacoes(movimentacoes);
			
			//passando a lista de movimentações do processo para o request
			processoDt = this.prepararListaMovimentacoesProcesso(processoDt, new UsuarioNe(), true);
		}
		
		if (processoDt != null && (processoDt.isSegredoJustica() || processoDt.isSigiloso())){
			if(!this.podeAcessarProcesso(usuarioDt, processoDt, obFabricaConexao)) {
				processoDt.limparSegredoJustica();
				ServentiaDt serventiaDt = this.consultarIdServentia(processoDt.getId_Serventia());
				ServentiaCargoDt serventiaCargoResponsavel = new ServentiaCargoDt();
				if(serventiaDt.isSegundoGrau()) {
					serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarRelatorResponsavelProcessoSegundoGrau(processoDt.getId(), obFabricaConexao);	
				} else if(serventiaDt.isTurma()) {
					serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarServentiaCargoResponsavelProcesso(processoDt.getId(), null, String.valueOf(GrupoTipoDt.JUIZ_TURMA), true);
				} else {
					serventiaCargoResponsavel = new ProcessoResponsavelNe().consultarServentiaCargoResponsavelProcesso(processoDt.getId(), null, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU), true);
				}
				processoDt.setServentiaCargoResponsavelDt(serventiaCargoResponsavel);
			}
		}
	
		return processoDt;
	}
	
	/**
	 * Método responsável em verificar se um processo é uma carta precátoria que teve o origem na expedição de uma pendência "Carta Precatória" realizada por um magistrado
	 * 
	 * @param id_processo, identificador do processo
	 * 
	 * @author lsbernardes
	 */
	public boolean isProcessoPrecatoriaExpedidaOnline(String id_processo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoPs obPersistencia = new ProcessoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isProcessoPrecatoriaExpedidaOnline(id_processo);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará o download do arquivo
	 */
	public byte[] baixarArquivoECarta(String id_PendenciaCorreios, ProcessoDt processoDt, UsuarioDt usuarioDt, HttpServletResponse response, InputStream template, LogDt logDt) throws Exception, MensagemException {		
		byte[] carta = null;
		carta = new PendenciaCorreiosNe().baixarArquivoECarta(id_PendenciaCorreios, processoDt, usuarioDt, response, template, logDt);
		
		return carta;
	}
}

