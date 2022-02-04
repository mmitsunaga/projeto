package br.gov.go.tj.projudi.webservice;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSSignedData;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PeticionamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.ArquivoTipoNe;
import br.gov.go.tj.projudi.ne.AssuntoNe;
import br.gov.go.tj.projudi.ne.ForumNe;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.MovimentacaoArquivoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.RelatoriosDiversosNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerenciaArquivo;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Base64Utils;
import br.gov.go.tj.utils.Certificado.VerificaP7s;
import br.jus.cnj.intercomunicacao.beans.Assinatura;
import br.jus.cnj.intercomunicacao.beans.AssuntoLocal;
import br.jus.cnj.intercomunicacao.beans.AssuntoProcessual;
import br.jus.cnj.intercomunicacao.beans.AvisoComunicacaoPendente;
import br.jus.cnj.intercomunicacao.beans.CabecalhoProcessual;
import br.jus.cnj.intercomunicacao.beans.CadastroIdentificador;
import br.jus.cnj.intercomunicacao.beans.CadastroOAB;
import br.jus.cnj.intercomunicacao.beans.ComunicacaoProcessual;
import br.jus.cnj.intercomunicacao.beans.ConfirmacaoRecebimento;
import br.jus.cnj.intercomunicacao.beans.Data;
import br.jus.cnj.intercomunicacao.beans.DataHora;
import br.jus.cnj.intercomunicacao.beans.DocumentoIdentificacao;
import br.jus.cnj.intercomunicacao.beans.DocumentoProcessual;
import br.jus.cnj.intercomunicacao.beans.Identificador;
import br.jus.cnj.intercomunicacao.beans.ManifestacaoProcessual;
import br.jus.cnj.intercomunicacao.beans.ModalidadeDocumentoIdentificador;
import br.jus.cnj.intercomunicacao.beans.ModalidadeGeneroPessoa;
import br.jus.cnj.intercomunicacao.beans.ModalidadePoloProcessual;
import br.jus.cnj.intercomunicacao.beans.ModalidadeRepresentanteProcessual;
import br.jus.cnj.intercomunicacao.beans.ModalidadeVinculacaoProcesso;
import br.jus.cnj.intercomunicacao.beans.MovimentacaoProcessual;
import br.jus.cnj.intercomunicacao.beans.MovimentoLocal;
import br.jus.cnj.intercomunicacao.beans.MovimentoNacional;
import br.jus.cnj.intercomunicacao.beans.NumeroUnico;
import br.jus.cnj.intercomunicacao.beans.ObjectFactory;
import br.jus.cnj.intercomunicacao.beans.OrgaoJulgador;
import br.jus.cnj.intercomunicacao.beans.Parametro;
import br.jus.cnj.intercomunicacao.beans.Parte;
import br.jus.cnj.intercomunicacao.beans.Pessoa;
import br.jus.cnj.intercomunicacao.beans.PoloProcessual;
import br.jus.cnj.intercomunicacao.beans.ProcessoJudicial;
import br.jus.cnj.intercomunicacao.beans.RepresentanteProcessual;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultaAlteracao;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultaAvisosPendentes;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultaProcesso;
import br.jus.cnj.intercomunicacao.beans.RequisicaoConsultarTeorComunicacao;
import br.jus.cnj.intercomunicacao.beans.RespostaConfirmacaoRecebimento;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaAlteracao;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaAvisosPendentes;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaProcesso;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultarTeorComunicacao;
import br.jus.cnj.intercomunicacao.beans.RespostaManifestacaoProcessual;
import br.jus.cnj.intercomunicacao.beans.TipoComunicacao;
import br.jus.cnj.intercomunicacao.beans.TipoPrazo;
import br.jus.cnj.intercomunicacao.beans.TipoQualificacaoPessoa;
import br.jus.cnj.intercomunicacao.beans.VinculacaoProcessual;

@WebService(serviceName = "IntercomunicacaoService",
		    targetNamespace = "http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/",
		    endpointInterface = "br.gov.go.tj.projudi.webservice.IntercomunicacaoTJGO",
		    portName="IntercomunicacaoServicePort")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class IntercomunicacaoService implements IntercomunicacaoTJGO {

	private static String URL_CONSULTAR_PROCESSO;	
	
	private String getUrlConsultarProcesso(){
		if(URL_CONSULTAR_PROCESSO == null){
			URL_CONSULTAR_PROCESSO = ProjudiPropriedades.getInstance().getEnderecoWebSiteConsultaProcessoSPG_SSG()+"?cnj=";	
		}
		return URL_CONSULTAR_PROCESSO;
	}
	
	private static final String SEPARADOR_PROCESSO = ";@;";
	private static final String SEPARADOR_PARTES = ";#;";
	private static final String SEPARADOR_PARTES_TIPO_RG = "#";
	private static final String SEPARADOR_ADVOGADOS = ";%;";
	private static final String SEPARADOR_DADOS_ADVOGADOS = ";";
	
	private static final String IDENTIFICADOR_POLO_ATIVO = "AT";
	private static final String IDENTIFICADOR_POLO_PASSIVO = "PA";
	private static final String IDENTIFICADOR_POLO_OUTROS = "TC";
	
	//private static final String IDENTIFICADOR_ABERTURA_TAG_XML = "<DADOS>";
	//private static final String IDENTIFICADOR_FECHAMENTO_TAG_XML = "</DADOS>";
	
	private static final int CEP_GERAL_GOIANIA = 74000000;
	
	private enum EnumDadosProcesso
	{
		SUCESS , //retorna se teve sucesso(1) ou não(0)
		MENSAGEM , //eventual mensagem
		CODG_NATUREZA , //código da natureza do processo no SPG
		DESC_NATUREZA , //descrição da natureza no SPG      
		DESC_COMARCA , //comarca no SPG
		CODG_SERVENTIA , //código da serventia no SPG
		DESC_SERVENTIA , //nome da mesma
		COMPETENCIA , //1 - Cível, 2 - Criminal
		SEGREDO , //1 - Segredo, 0 - não
		VALOR_CAUSA , //Valor da ação
		CODG_FASE , //Código da Fase no SPG
		DESC_FASE , //descrição da fase no SPG
		CODG_MOVIMENTO_CNJ , //Código nacional da movimentação CNJ
		INFO_MOVIMENTO_01 , //Informação da movimentação
		INFO_MOVIMENTO_02 , //Informação da movimentação
		INFO_MOVIMENTO_03 , //Informação da movimentação
		INFO_MOVIMENTO_04 , //Informação da movimentação
		INFO_MOVIMENTO_05 , //Informação da movimentação
		INFO_MOVIMENTO_06 , //Informação da movimentação
		INFO_MOVIMENTO_07 , //Informação da movimentação
		INFO_MOVIMENTO_08 , //Informação da movimentação
		INFO_MOVIMENTO_09 , //Informação da movimentação
		INFO_MOVIMENTO_10 , //Informação da movimentação
		INFO_MOVIMENTO_11 , //Informação da movimentação
		INFO_MOVIMENTO_12 , //Informação da movimentação
		INFO_MOVIMENTO_13 , //Informação da movimentação
		INFO_MOVIMENTO_14 , //Informação da movimentação
		INFO_MOVIMENTO_15 , //Informação da movimentação
		INFO_MOVIMENTO_16 , //Informação da movimentação
		INFO_MOVIMENTO_17 , //Informação da movimentação
		INFO_MOVIMENTO_18 , //Informação da movimentação
		INFO_MOVIMENTO_19 , //Informação da movimentação
		INFO_MOVIMENTO_20 , //Informação da movimentação
		DATA_MOVIMENTO , //Data da última movimentação
		HORA_MOVIMENTO , //Hora da última movimentação
		CODG_ASSUNTO_CNJ , //Código nacional do assunto CNJ
		DESC_ASSUNTO , //Descrição do Assunto
		CODG_ASSUNTO_PAI_CNJ , //Código do assunto Pai
		DESC_ASSUNTO_PAI,   //Descrição do Assunto Pai
		INICIO_PARTES, //Partes do Processo
		QUANTIDADE_MINIMA
	}
	
	private enum EnumDadosParte
	{
		VET_POLO_PARTES2 , //Polo da Parte (AT - Ativo, PA - Passivo, TC - Outros)
		VET_PESSOA_NATURALIDADE2 , //Naturalidade 
		VET_PESSOA_NASCIMENTO2 , //Data Nascimento AAAAMMDD
		VET_PESSOA_NOME2 , //Nome da Parte     
		VET_PESSOA_PAI2 , //Pai
		VET_PESSOA_MAE2 , //Mãe
		VET_PESSOA_CPF2 , //CPF/CNPJ
		VET_PESSOA_SEXO2 , //Sexo
		VET_PESSOA_TIPO2 , //F- Física, J- Jurídica, separador # entre tipo e RG
		//VET_PESSOA_RG2 , //Identidade
		VET_PESSOA_ORGAO2 , //Órgão Expedidor
		INICIO_ADVOGADOS,
		QUANTIDADE_MINIMA
	}
	
	private enum EnumDadosParteTipoRG
	{	VET_PESSOA_TIPO2 , //F- Física, J- Jurídica
		VET_PESSOA_RG2 //Identidade
	}
	
	private enum EnumDadosAdvogado
	{
		VET_ADV_OAB2 , //Oab
		VET_ADV_NOME2, //Nome do Advogado
		QUANTIDADE_MINIMA
	}
	
	private UsuarioNe UsuarioSessao;
	private ProcessoNe processoNe;
	private RelatoriosDiversosNe relatoriosDiversosNe;
	private LogNe logNe;
	private ProcessoTipoNe processoTipoNe;
	private ServentiaNe serventiaNe;
	private ForumNe forumNe;
	private AreaDistribuicaoNe areaDistribuicaoNe;
	private MovimentacaoArquivoNe movimentacaoArquivoNe;
	private ArquivoNe arquivoNe;
	private MovimentacaoNe movimentacaoNe;
	private MovimentacaoTipoNe movimentacaoTipoNe;
	private AssuntoNe assuntoNe;
	private ProcessoParteNe processoParteNe;
	private PendenciaNe pendenciaNe;
	private ArquivoTipoNe arquivoTipoNe;
				
	public IntercomunicacaoService()
	{
		this.UsuarioSessao = new UsuarioNe();
		this.processoNe = new ProcessoNe();
		this.relatoriosDiversosNe = new RelatoriosDiversosNe();
		this.logNe = new LogNe();
		this.processoTipoNe = new ProcessoTipoNe();
		this.serventiaNe = new ServentiaNe();
		this.forumNe = new ForumNe();
		this.areaDistribuicaoNe = new AreaDistribuicaoNe();
		this.movimentacaoArquivoNe = new MovimentacaoArquivoNe();
		this.arquivoNe = new ArquivoNe();
		this.movimentacaoNe = new MovimentacaoNe();
		this.movimentacaoTipoNe = new MovimentacaoTipoNe();
		this.assuntoNe = new AssuntoNe();
		this.processoParteNe = new ProcessoParteNe();
		this.pendenciaNe = new PendenciaNe();
		this.arquivoTipoNe = new ArquivoTipoNe();
	}
	
	@Resource
	private WebServiceContext ctx;
		
	protected HttpServletRequest getRequest() throws MensagemException{
		if (requestDegub != null) return requestDegub;
		
		HttpServletRequest request = (HttpServletRequest) this.ctx.getMessageContext().get(MessageContext.SERVLET_REQUEST);		
		if (request == null) throw new MensagemException("Request não pôde ser recuperado.");
		return request;
	}

	public RespostaConsultaAlteracao consultarAlteracao(RequisicaoConsultaAlteracao requisicao) {
		ObjectFactory fabrica = new ObjectFactory();
		RespostaConsultaAlteracao resposta = fabrica.createRespostaConsultaAlteracao();	
		FabricaConexao obFabricaConexaoPJD = null;
		
		try
		{
			valideRequisicaoNula(requisicao);
			obFabricaConexaoPJD = obtenhaConexaoPersistencia();
			valideEExecuteLogon(requisicao.getIdConsultante(), requisicao.getSenhaConsultante(), obFabricaConexaoPJD);		
			valideConsultarAlteracao(requisicao, obFabricaConexaoPJD);
			
			consultarAlteracao(requisicao, resposta, obFabricaConexaoPJD);
		}
		catch (MensagemException ex) {
			resposta.setSucesso(false);
			resposta.setMensagem(ex.getMessage());
		}
		catch(Exception ex)
		{
			resposta.setSucesso(false);
			resposta.setMensagem(executeTratamentoExceptionForUser(ex, "consultarAlteracao"));
		}
		finally
		{
			executeLogoff(obFabricaConexaoPJD);
		}
				
		return resposta;
	}

	public RespostaConsultaAvisosPendentes consultarAvisosPendentes(RequisicaoConsultaAvisosPendentes requisicao) {
		ObjectFactory fabrica = new ObjectFactory();
		RespostaConsultaAvisosPendentes resposta = fabrica.createRespostaConsultaAvisosPendentes();
		FabricaConexao obFabricaConexaoPJD = null;
		
		try
		{
			valideRequisicaoNula(requisicao);
			obFabricaConexaoPJD = obtenhaConexaoPersistencia();
			valideEExecuteLogon(requisicao.getIdConsultante(), requisicao.getSenhaConsultante(), obFabricaConexaoPJD);
			
			consultarAvisosPendentes(requisicao, resposta, obFabricaConexaoPJD);
		}
		catch (MensagemException ex) {
			resposta.setSucesso(false);
			resposta.setMensagem(ex.getMessage());
		}
		catch(Exception ex)
		{
			resposta.setSucesso(false);
			resposta.setMensagem(executeTratamentoExceptionForUser(ex, "consultarAvisosPendentes"));
			resposta.getAviso().clear();
		}
		finally
		{
			executeLogoff(obFabricaConexaoPJD);
		}
						
		return resposta;
	}

	public RespostaConsultaProcesso consultarProcesso(RequisicaoConsultaProcesso requisicao) {
		ObjectFactory fabrica = new ObjectFactory();
		RespostaConsultaProcesso resposta = fabrica.createRespostaConsultaProcesso();
		FabricaConexao obFabricaConexaoPJD = null;
		
		try
		{			
			valideRequisicaoNula(requisicao);	
			obFabricaConexaoPJD = obtenhaConexaoPersistencia();
			valideEExecuteLogon(requisicao.getIdConsultante(), requisicao.getSenhaConsultante(), obFabricaConexaoPJD);
			valideConsultarProcesso(requisicao, obFabricaConexaoPJD);
			
			resposta.setProcesso(obtenhaProcessoJudicialProjudi(obtenhaNumeroProcesso(requisicao.getNumeroProcesso()), 
					                                            (requisicao.isSetIncluirCabecalho() && requisicao.isIncluirCabecalho()), 
					                                            (requisicao.isSetIncluirDocumentos() && requisicao.isIncluirDocumentos()), 
					                                            (requisicao.isSetMovimentos() && requisicao.isMovimentos()),
					                                            getDataReferencia(getDataReferenciaString(requisicao)),
					                                            requisicao.getDocumento(),
					                                            obFabricaConexaoPJD));
			
			// Se não existir o processo no projudi iremos consultar no SPG...
			if (!resposta.isSetProcesso())
				resposta.setProcesso(obtenhaProcessoJudicialSPG(obtenhaNumeroProcesso(requisicao.getNumeroProcesso()), 
						             (requisicao.isSetMovimentos() && requisicao.isMovimentos())));
						
			resposta.setSucesso(resposta.isSetProcesso());
			
			if (!resposta.isSetProcesso())
				resposta.setMensagem("Processo não localizado.");
			
		}
		catch (MensagemException ex) {
			resposta.setSucesso(false);
			resposta.setMensagem(ex.getMessage());
		}
		catch(Exception ex)
		{
			resposta.setSucesso(false);
			resposta.setMensagem(executeTratamentoExceptionForUser(ex, "consultarProcesso"));
		}
		finally
		{
			executeLogoff(obFabricaConexaoPJD);
		}
				
		return resposta;
	}
	
	public RespostaConfirmacaoRecebimento confirmarRecebimento(ConfirmacaoRecebimento requisicao) {
		ObjectFactory fabrica = new ObjectFactory();
		RespostaConfirmacaoRecebimento resposta = fabrica.createRespostaConfirmacaoRecebimento();
		
		resposta.setSucesso(false);
		resposta.setMensagem("Serviço não implementado. Operação destinada exclusivamente a tribunais em sua intercomunicação que tem por objetivo permitir que um tribunal que tenha sido objeto de uma operação de entrega de manifestação processual (4) confirme junto ao tribunal que enviou a manifestação que a recebeu integralmente.");
		
		return resposta;
	}
	
	public RespostaConsultarTeorComunicacao consultarTeorComunicacao(RequisicaoConsultarTeorComunicacao requisicao)
	{
		ObjectFactory fabrica = new ObjectFactory();
		RespostaConsultarTeorComunicacao resposta = fabrica.createRespostaConsultarTeorComunicacao();		
		FabricaConexao obFabricaConexaoPJD = null;
		
		try
		{
			valideRequisicaoNula(requisicao);
			obFabricaConexaoPJD = obtenhaConexaoPersistencia();
			valideEExecuteLogon(requisicao.getIdConsultante(), requisicao.getSenhaConsultante(), obFabricaConexaoPJD);
			valideConsultarTeorComunicacaoTJGO(requisicao, obFabricaConexaoPJD);
			
			consultarTeorComunicacaoTJGO(requisicao, resposta, obFabricaConexaoPJD);
		}
		catch (MensagemException ex) {
			resposta.setSucesso(false);
			resposta.setMensagem(ex.getMessage());
		}
		catch(Exception ex)
		{
			resposta.setSucesso(false);
			resposta.setMensagem(executeTratamentoExceptionForUser(ex, "consultarTeorComunicacao"));
		}
		finally
		{
			executeLogoff(obFabricaConexaoPJD);
		}
				
		return resposta;
	}
	
	public RespostaManifestacaoProcessual entregarManifestacaoProcessual(ManifestacaoProcessual requisicao) {
		ObjectFactory fabrica = new ObjectFactory();
		RespostaManifestacaoProcessual resposta = fabrica.createRespostaManifestacaoProcessual();
		FabricaConexao obFabricaConexaoPJD = null;
		
		try
		{
			valideRequisicaoNula(requisicao);
			obFabricaConexaoPJD = obtenhaConexaoPersistencia();
			valideEExecuteLogon(requisicao.getIdManifestante(), requisicao.getSenhaManifestante(), obFabricaConexaoPJD);
			valideEntregarManifestacaoProcessual(requisicao, obFabricaConexaoPJD);
			
			entregarManifestacaoProcessual(requisicao, resposta, obFabricaConexaoPJD);
		}
		catch (MensagemException ex) {
			resposta.setSucesso(false);
			resposta.setMensagem(ex.getMessage());
		}
		catch(Exception ex)
		{
			resposta.setSucesso(false);
			resposta.setMensagem(executeTratamentoExceptionForUser(ex, "entregarManifestacaoProcessual"));
		}
		finally
		{
			executeLogoff(obFabricaConexaoPJD);
		}
				
		return resposta;
	}

	public RespostaConsultarRelatorioDeIntimacoesTJGO consultarRelatorioDeIntimacoesTJGO(RequisicaoCredenciaisTJGO requisicao) {
		RespostaConsultarRelatorioDeIntimacoesTJGO resposta = new RespostaConsultarRelatorioDeIntimacoesTJGO();
		FabricaConexao obFabricaConexaoPJD = null;		
		try
		{
			valideRequisicaoNula(requisicao);
			obFabricaConexaoPJD = obtenhaConexaoPersistencia();
			valideEExecuteLogon(requisicao.getLoginConsultante(), requisicao.getSenhaConsultante(), requisicao, true, obFabricaConexaoPJD);
			
			resposta.setRelatorio(relatoriosDiversosNe.consultarRelatorioDeIntimacoesPendentes(UsuarioSessao, obFabricaConexaoPJD));
		}
		catch (MensagemException ex) {
			resposta.setSucesso(false);
			resposta.setMensagem(ex.getMessage());
		}
		catch(Exception ex)
		{
			resposta.setSucesso(false);
			resposta.setMensagem(executeTratamentoExceptionForUser(ex, "consultarRelatorioDeIntimacoesTJGO"));
		}
		finally
		{
			executeLogoff(obFabricaConexaoPJD);
		}
				
		return resposta;
	}

	private FabricaConexao obtenhaConexaoPersistencia() throws Exception {
		return new FabricaConexao(FabricaConexao.PERSISTENCIA);
	}	
	
	private String executeTratamentoExceptionForUser(Exception ex, String methodName) {
		String id_log = "";
		try {		
		    if (UsuarioSessao != null && UsuarioSessao.isTemIdUsuario()){
		        logNe.salvarErro(new LogDt("IntercomunicacaoService."+methodName, "", UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), String.valueOf(LogTipoDt.Erro), "",  Funcoes.obtenhaConteudoLog(ex, UsuarioSessao)));		         	  
		    } else {
		    	logNe.salvarErro(new LogDt("IntercomunicacaoService."+methodName, "UsuarioVazio", UsuarioDt.SistemaProjudi, Funcoes.getIpCliente(getRequest()), String.valueOf(LogTipoDt.Erro), "",  obtenhaConteudoExcecao(ex)));		        
		    }
			id_log = logNe.getId_Log();
			
			String mensagem = Funcoes.getMensagemExceptionUser(ex);
			
			if (mensagem == null || mensagem.trim().length() < 10) mensagem = ex.toString();
			
			return "Erro n. " + id_log + " - " + mensagem;		
			
		} catch(Exception e) {
			long inData = System.currentTimeMillis();
			Logger logger = Logger.getLogger(Controle.class);
    		logger.warn("Indice do erro no arquivo de log do servidor n.º " + inData + ". Erro no tratamento de uma Exception.", e);
    		logger.warn("Erro capturado não processado no catch(Exception) devido à exceção anterior.", ex);
			return "Erro:" + inData + "\nOcorreu um erro, e não foi possível salvar no log. \nFavor tentar novamente mais tarde e se o erro persistir entrar em contato com o suporte." ;		
		}
	}
	
	private void valideRequisicaoNula(Object requisicao) throws MensagemException
	{
		if (requisicao == null) throw new MensagemException("O parâmetro requisição não foi informado");
	}
	
	private void valideEExecuteLogon(String usuario, String senha, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		valideEExecuteLogon(usuario, senha, null, false, obFabricaConexaoPJD);
	}
	
	private void valideEExecuteLogon(String usuario, String senha, RequisicaoCredenciaisTJGO requisicaoCredenciaisTJGO, boolean validaServentiaGrupo, FabricaConexao obFabricaConexaoPJD) throws Exception
	{	
		String mensagem = "";
			
		if (usuario == null || usuario.trim().equalsIgnoreCase("") || senha == null || senha.trim().equalsIgnoreCase(""))
		{
			mensagem = "Usuário ou Senha vazia.";
		} 
		else
		{	
			if (Funcoes.StringToLong(usuario) > 0) {
				usuario = Funcoes.completaCPFZeros(usuario);
			}
			if (!valideUsuarioESenha(usuario, senha))
			{
				mensagem = "Usuário ou Senha inválida.";
			}
			else
			{
				if (getRequest().getSession(false) != null) getRequest().getSession(false).invalidate();
				getRequest().getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());

				UsuarioSessao.getUsuarioDt().setDataEntrada(Funcoes.DataHora(getRequest().getSession().getCreationTime()));
				UsuarioSessao.getUsuarioDt().setIpComputadorLog(Controle.getIpCliente(getRequest()));

				if (!validaServentiaGrupo) {	
					// já fica na sessão a lista das serventias grupos disponiveis
					UsuarioSessao.consultarServentiasGrupos(obFabricaConexaoPJD);
				} else {
					// já fica na sessão a lista das serventias grupos disponiveis
					List serventiaGrupos = UsuarioSessao.consultarServentiasGrupos(requisicaoCredenciaisTJGO.getId_usuarioServentia(),
							                                                       requisicaoCredenciaisTJGO.getGrupoCodigo(),
							                                                       requisicaoCredenciaisTJGO.getId_serventiaCargo(),
							                                                       requisicaoCredenciaisTJGO.getId_serventiaCargoUsuarioChefe(),
							                                                       requisicaoCredenciaisTJGO.getId_UsuarioServentiaChefe());
					
					if (serventiaGrupos == null) {
						mensagem = "Serventia Grupo não disponível com os parâmetros informados.";
					} else {
						UsuarioDt usuarioDt = null;
						for (int i = 0; i < serventiaGrupos.size(); i++) {
							usuarioDt = (UsuarioDt)serventiaGrupos.get(i);
							
							if ((usuarioDt.getGrupoCodigo().equalsIgnoreCase(requisicaoCredenciaisTJGO.getGrupoCodigo())) 
								&& (usuarioDt.getId_UsuarioServentia().equalsIgnoreCase(requisicaoCredenciaisTJGO.getId_usuarioServentia())) 
								&& (usuarioDt.getId_ServentiaCargo().equalsIgnoreCase(requisicaoCredenciaisTJGO.getId_serventiaCargo())) 
								&& (usuarioDt.getId_ServentiaCargoUsuarioChefe().equalsIgnoreCase(requisicaoCredenciaisTJGO.getId_serventiaCargoUsuarioChefe()))
								&& (usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase(requisicaoCredenciaisTJGO.getId_UsuarioServentiaChefe()))) {
								break;
							}
							usuarioDt = null;
						}
						
						if (usuarioDt==null){
							mensagem = "Grupo não escolhido.";
						} else {
							switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {
								case GrupoTipoDt.ASSESSOR:
								case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
								case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
								case GrupoTipoDt.ASSESSOR_ADVOGADO:
								case GrupoTipoDt.ASSESSOR_MP:
								case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
									String mensagemErro = UsuarioSessao.validarAcessoAssistentes(usuarioDt);
									if(mensagemErro != null && !mensagemErro.equalsIgnoreCase("")){
										mensagem = mensagemErro;
									}
							}
							
							if (mensagem.length() == 0) {
								usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
								usuarioDt.setDataEntrada(UsuarioSessao.getUsuarioDt().getDataEntrada());
								usuarioDt.setCodigoTemp(UsuarioSessao.getUsuarioDt().getCodigoTemp());
								UsuarioSessao.setUsuarioDt(usuarioDt);
								usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
								
								String stMensagem = "Usuário " + usuarioDt.getUsuario() + " fez logon no sistema a partir de " + getRequest().getRemoteAddr() + " com o papel de " + usuarioDt.getGrupo() + " na Serventia: " + usuarioDt.getServentia();
								//UsuarioSessao.salvarLogAcesso(stMensagem);
								
								if (UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe() != null && UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe().trim().length() > 0) {
									UsuarioServentiaDt usuarioServentiaAssistente = UsuarioSessao.buscaUsuarioServentiaId(usuarioDt.getId(), usuarioDt.getId_Serventia(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentiaChefe().trim());
									if (usuarioServentiaAssistente != null) UsuarioSessao.getUsuarioDt().setPodeGuardarAssinarUsuarioServentiaChefe(usuarioServentiaAssistente.getPodeGuardarAssinarUsuarioServentiaChefe());
								}
							}
						}
					}
				}

				getRequest().getSession().setAttribute("UsuarioSessao", UsuarioSessao);			
			}			
		}
				
		if (mensagem.length() > 0) throw new MensagemException(mensagem);		
	}
	
	private boolean valideUsuarioESenha(String usuario, String senha) throws Exception
	{
		if (UsuarioSessao.logarUsuarioSenha(usuario, senha)) return true;
		
		if (UsuarioSessao.consultaUsuarioSenhaMD5(usuario, senha)) return true;
		
		return false;
	}
	
	private void valideConsultarProcesso(RequisicaoConsultaProcesso requisicao, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		if (requisicao.getNumeroProcesso() == null || 
			!requisicao.getNumeroProcesso().isSetValue() || 
			requisicao.getNumeroProcesso().getValue().trim().length() == 0) {
			throw new MensagemException("O parâmetro NumeroProcesso não foi informado.");
		}		
		validaNumeroProcesso(requisicao.getNumeroProcesso().getValue(), obFabricaConexaoPJD);
		
		if (isDataReferenciaInformada(requisicao)) {
			if (Funcoes.validaDataYYYYMMDD(getDataReferenciaString(requisicao))) return;
			
			if (Funcoes.validaDataYYYYMMDDHHMMSS(getDataReferenciaString(requisicao))) return;
			
			throw new MensagemException("O parâmetro DataReferencia é opcional, mas quando informado deve possuir o formato yyyyMMdd ou yyyyMMddhhmmss, onde yyyy = ano com 4 dígitos, MM = mês com 2 dígitos, dd = dia com 2 dígitos, hh = hora com 2 dígitos, mm = minuto com 2 dígitos e ss = segundos com 2 dígitos. Ex: 20190715 ou 20190715125300.");
		}		
	}
	
	private boolean isDataReferenciaInformada(RequisicaoConsultaProcesso requisicao) {
		return (requisicao.getDataReferencia() != null &&
				requisicao.isSetDataReferencia() &&
				requisicao.getDataReferencia() != null &&
				requisicao.getDataReferencia().isSetValue() &&
				requisicao.getDataReferencia().getValue() != null &&
				requisicao.getDataReferencia().getValue().trim().length() > 0);
	}
	
	private String getDataReferenciaString(RequisicaoConsultaProcesso requisicao) {
		if (isDataReferenciaInformada(requisicao)) {
			return requisicao.getDataReferencia().getValue().trim();
		}
		return "";
	}
	
	private long getDataReferencia(String dataReferenciaString) {
		if (dataReferenciaString != null && dataReferenciaString.trim().length() > 0) {
			if (Funcoes.validaDataYYYYMMDD(dataReferenciaString)) return Funcoes.StringToLong(dataReferenciaString + "000000");
			
			if (Funcoes.validaDataYYYYMMDDHHMMSS(dataReferenciaString)) return Funcoes.StringToLong(dataReferenciaString);
		}		
		return 0;	
	}
	
	private void validaNumeroProcesso(String numeroProcesso, FabricaConexao obFabricaConexaoPJD) throws Exception {
		String numeroFormatado = Funcoes.formataNumeroCompletoProcesso(numeroProcesso);
		if (!Funcoes.validaProcessoNumero(numeroFormatado)) {
			ProcessoDt processoDt = processoNe.consultarProcessoNumero(numeroProcesso, obFabricaConexaoPJD);			
			if (processoDt == null) {
				throw new MensagemException("O parâmetro NumeroProcesso está inválido. O formato deve ser: NNNNNNN.DD.AAAA.JTR.OOOO, onde NNNNNNN = Número do processo, DD = Dígito, AAAA = Ano, JTR = Constante 8.09 e OOOO = Código do Forum. Ex: 5000280.28.2010.8.09.0059.");
			}	
		}
	}	
	
	private void valideConsultarAlteracao(RequisicaoConsultaAlteracao requisicao, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		if (requisicao.getNumeroProcesso() == null || requisicao.getNumeroProcesso().trim().length() == 0) 
			throw new MensagemException("O parâmetro NumeroProcesso não foi informado.");		
		
		validaNumeroProcesso(requisicao.getNumeroProcesso(), obFabricaConexaoPJD);
	}
	
	private void valideConsultarTeorComunicacaoTJGO(RequisicaoConsultarTeorComunicacao requisicao, FabricaConexao obFabricaConexaoPJD) throws Exception
	{ 
		if (requisicao.getIdentificadorAviso() == null || !requisicao.getIdentificadorAviso().isSetValue()) 
			throw new MensagemException("O parâmetro IdentificadorAviso não foi informado.");
		
		if (requisicao.getNumeroProcesso() == null || !requisicao.getNumeroProcesso().isSetValue()) 
			throw new MensagemException("O parâmetro NumeroProcesso não foi informado.");
		
		validaNumeroProcesso(requisicao.getNumeroProcesso().getValue(), obFabricaConexaoPJD);
	}
	
	private void valideEntregarManifestacaoProcessual(ManifestacaoProcessual requisicao, FabricaConexao obFabricaConexaoPJD) throws Exception
	{ 
		if (requisicao.getDocumento() == null || requisicao.getDocumento().size() == 0) 
			throw new MensagemException("O parâmetro Documento não foi informado, é necessário enviar pelo menos um documento.");
		
		if (requisicao.getDadosBasicos() == null && (requisicao.getNumeroProcesso() == null || !requisicao.getNumeroProcesso().isSetValue())) 
			throw new MensagemException("O parâmetro DadosBasicos e NumeroProcesso não foram informados.");
		
		if (requisicao.getDadosBasicos() != null && requisicao.getNumeroProcesso() != null && requisicao.getNumeroProcesso().isSetValue())
			throw new MensagemException("O parâmetro DadosBasicos e NumeroProcesso não devem ser informados simultaneamente, utilize somente o parâmetro NumeroProcesso para processos existentes e DadosBasicos para cadastro de um novo processo (Petição Inicial). ");
		
		if (requisicao.getNumeroProcesso().isSetValue()) {
			validaNumeroProcesso(requisicao.getNumeroProcesso().getValue(), obFabricaConexaoPJD);
		}
	}
	
	private String obtenhaConteudoExcecao(Exception ex){
		String mensagemCompleta = ex.toString();
    	try{    		
    		return Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
    	}catch(Exception e){
    		return ex.getMessage() + mensagemCompleta;
    	}    	
    }
	
	private void executeLogoff(FabricaConexao obFabricaConexaoPJD)
	{
		try
		{
			getRequest().getSession().removeAttribute("UsuarioSessao");
			getRequest().getSession(false).invalidate();
			if (obFabricaConexaoPJD != null) obFabricaConexaoPJD.fecharConexao();
		}
		catch (Exception e) {}
	}
	
	private String obtenhaNumeroProcesso(NumeroUnico numeroProcesso) {
		if (numeroProcesso == null || numeroProcesso.getValue() == null) return "";
		return obtenhaNumeroProcesso(numeroProcesso.getValue());
	}
	
	private String obtenhaNumeroProcesso(String numeroProcesso) {
		numeroProcesso = Funcoes.retiraVirgulaPonto(numeroProcesso);		
		numeroProcesso = Funcoes.formataNumeroCompletoProcesso(numeroProcesso);		
		return numeroProcesso;
	}
	
	private ProcessoJudicial obtenhaProcessoJudicialProjudi(String numeroProcesso, 
			                                                boolean incluirCabecalho,
			                                                boolean incluiDocumentos, 
			                                                boolean incluiMovimentos, 
			                                                long dataReferencia,
			                                                List<String> tiposDeDocumentos,
			                                                FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		ProcessoDt processoDt = processoNe.consultarProcessoNumero(numeroProcesso, obFabricaConexaoPJD);
		
		if (processoDt == null) return null;
		
		processoDt = processoNe.consultarDadosProcessoMNI(processoDt.getId(), UsuarioSessao.getUsuarioDt(), UsuarioSessao.getNivelAcesso(), incluiMovimentos, obFabricaConexaoPJD);
		
		return obtenhaProcessoJudicialProjudi(processoDt, incluirCabecalho, incluiDocumentos, incluiMovimentos, dataReferencia, tiposDeDocumentos, obFabricaConexaoPJD);
	}
	
	private ProcessoJudicial obtenhaProcessoJudicialProjudi(ProcessoDt processoDt, 
			                                                boolean incluirCabecalho,
			                                                boolean incluiDocumentos, 
			                                                boolean incluiMovimentos, 
			                                                long dataReferencia,
			                                                List<String> tiposDeDocumentos,
			                                                FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		if (processoDt.getId_Recurso().length() > 0)
			processoDt.setRecursoDt(processoNe.consultarDadosRecurso(processoDt.getId_Recurso(), obFabricaConexaoPJD));
		
		return efetueMapeamentoProcessoJudicialProjudi(processoDt, incluirCabecalho, incluiDocumentos, incluiMovimentos, dataReferencia, tiposDeDocumentos, obFabricaConexaoPJD);
	}
	
	private ProcessoJudicial efetueMapeamentoProcessoJudicialProjudi(ProcessoDt processoCompleto, 
			                                                         boolean incluirCabecalho,
			                                                         boolean incluiDocumentos, 
			                                                         boolean incluiMovimentos,
			                                                         long dataReferencia,
			                                                         List<String> tiposDeDocumentos,
			                                                         FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		ProcessoJudicial processoCompletoWs = new ProcessoJudicial();
		
		if (incluirCabecalho) {
			processoCompletoWs.setDadosBasicos(efetueMapeamentoCabecalhoProcessualProjudi(processoCompleto, obFabricaConexaoPJD));	
		} else {
			CabecalhoProcessual cabecalhoProcessualWs = new CabecalhoProcessual();
			
			NumeroUnico numeroProcessoCompleto = new NumeroUnico();
			numeroProcessoCompleto.setValue(processoCompleto.getProcessoNumeroCompleto());
			
			cabecalhoProcessualWs.setNumero(numeroProcessoCompleto);
			cabecalhoProcessualWs.setClasseProcessual(obtenhaClasseProcessual(processoCompleto, obFabricaConexaoPJD));
			cabecalhoProcessualWs.setNivelSigilo(obtenhaNivelSigilo(processoCompleto));
			
			processoCompletoWs.setDadosBasicos(cabecalhoProcessualWs);
		}
		
		if (incluiDocumentos) {
			processoCompletoWs.getDocumento().addAll(efetueMapeamentoDocumentosProcessuaisProjudi(processoCompleto, processoCompletoWs.getDadosBasicos().getNivelSigilo(), dataReferencia, tiposDeDocumentos));
		}				
		
		if (incluiMovimentos) {
			processoCompletoWs.getMovimento().addAll(efetueMapeamentoMovimentacoesProcessuaisProjudi(processoCompleto, processoCompletoWs.getDadosBasicos().getNivelSigilo(), dataReferencia));
		}			
		
		return processoCompletoWs;
	}
	
	private int obtenhaClasseProcessual(ProcessoDt processoCompleto, FabricaConexao obFabricaConexaoPJD) throws Exception {
		String classeCNJProcesso = processoTipoNe.consultarCodigoCNJ(processoCompleto.getId_ProcessoTipo(), obFabricaConexaoPJD);
		
		if (classeCNJProcesso != null && classeCNJProcesso.length() > 0)
			return Funcoes.StringToInt(classeCNJProcesso);
		else		
			return Funcoes.StringToInt(processoCompleto.getProcessoTipoCodigo());
	}
	
	private int obtenhaNivelSigilo(ProcessoDt processoCompleto) {
		if (processoCompleto.isSegredoJustica() ||
			processoCompleto.isSigiloso())
			return 1;
		else
			return 0;
	}
	
	private CabecalhoProcessual efetueMapeamentoCabecalhoProcessualProjudi(ProcessoDt processoCompleto, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		CabecalhoProcessual cabecalhoProcessualWs = new CabecalhoProcessual();
		
		cabecalhoProcessualWs.setClasseProcessual(obtenhaClasseProcessual(processoCompleto, obFabricaConexaoPJD));
		
		AreaDistribuicaoDt areaDistribuicaoDt = null;		
		if (processoCompleto.getId_AreaDistribuicao() != null && processoCompleto.getId_AreaDistribuicao().length() > 0)
			areaDistribuicaoDt = areaDistribuicaoNe.consultarId(processoCompleto.getId_AreaDistribuicao(), obFabricaConexaoPJD);
		
		if (areaDistribuicaoDt != null)
			cabecalhoProcessualWs.setCodigoLocalidade(areaDistribuicaoDt.getAreaDistribuicaoCodigo());
		else
			cabecalhoProcessualWs.setCodigoLocalidade(processoCompleto.getId_AreaDistribuicao());
		
		OrgaoJulgador orgaoJulgador = new OrgaoJulgador();
		orgaoJulgador.setCodigoOrgao(processoCompleto.getServentiaCodigo());
		orgaoJulgador.setNomeOrgao(processoCompleto.getServentia());
		
		if (Funcoes.StringToLong(processoCompleto.getId_Serventia()) > 0) {
			ServentiaDt serventiaDoProcesso = serventiaNe.consultarId(processoCompleto.getId_Serventia(), obFabricaConexaoPJD);
			if (serventiaDoProcesso != null && 
				serventiaDoProcesso.getCep() != null && 
				serventiaDoProcesso.getCep().trim().length() > 0) {
				orgaoJulgador.setCodigoMunicipioIBGE(Funcoes.StringToInt(serventiaDoProcesso.getCep().trim()));	
			}	
		}			
		
		if (orgaoJulgador.getCodigoMunicipioIBGE() <= 0 && Funcoes.StringToLong(processoCompleto.getForumCodigo()) > 0) {
			ForumDt forumDoProcesso = forumNe.consultarForumCodigo(processoCompleto.getForumCodigo(), obFabricaConexaoPJD);
			if (forumDoProcesso != null && 
				forumDoProcesso.getEnderecoForum() != null && 
				forumDoProcesso.getEnderecoForum().getCep() != null && 
				forumDoProcesso.getEnderecoForum().getCep().trim().length() > 0) {
				orgaoJulgador.setCodigoMunicipioIBGE(Funcoes.StringToInt(forumDoProcesso.getEnderecoForum().getCep().trim()));
			}	
		}
		
		if (orgaoJulgador.getCodigoMunicipioIBGE() <= 0) {
			orgaoJulgador.setCodigoMunicipioIBGE(CEP_GERAL_GOIANIA);
		}
				
		cabecalhoProcessualWs.setOrgaoJulgador(orgaoJulgador);
						
		cabecalhoProcessualWs.setCompetencia(Funcoes.StringToInt(processoCompleto.getAreaCodigo()));
		
		cabecalhoProcessualWs.setNivelSigilo(obtenhaNivelSigilo(processoCompleto));		
		
		NumeroUnico numeroProcessoCompleto = new NumeroUnico();
		numeroProcessoCompleto.setValue(processoCompleto.getProcessoNumeroCompleto());
		cabecalhoProcessualWs.setNumero(numeroProcessoCompleto);
		
		cabecalhoProcessualWs.setTamanhoProcesso(0);		
		
		cabecalhoProcessualWs.setValorCausa(Funcoes.StringToDouble(processoCompleto.getValor().replace(".", "").replace(",", ".")));
		
		cabecalhoProcessualWs.getAssunto().addAll(efetueMapeamentoAssuntosProcessuais(processoCompleto, obFabricaConexaoPJD));
		
		efetueMapeamentoMagistradosAtuantesEIndicadorDoMP(processoCompleto, cabecalhoProcessualWs);		
		
		cabecalhoProcessualWs.getOutroParametro().addAll(efetueMapeamentoOutrosParametros(processoCompleto));
		
		cabecalhoProcessualWs.getPolo().addAll(efetueMapeamentoPolosProcessuais(processoCompleto, obFabricaConexaoPJD));
		
		cabecalhoProcessualWs.getPrioridade().addAll(efetueMapeamentoPrioridades(processoCompleto));
		
		cabecalhoProcessualWs.getProcessoVinculado().addAll(efetueMapeamentoVinculosProcessuais(processoCompleto));
		
		return cabecalhoProcessualWs;
	}
	
	private List<DocumentoProcessual> efetueMapeamentoDocumentosProcessuaisProjudi(ProcessoDt processoCompleto, int nivelSigiloProcesso, long dataReferencia, List<String> tiposDeDocumentos) throws Exception
	{
		List<DocumentoProcessual> listaDeDocumentos = new ArrayList<DocumentoProcessual>();	
		
		List listaMovimentacoes = processoCompleto.getListaMovimentacoes();
		
		if (listaMovimentacoes != null)
   	    {
   	    	for (int i=listaMovimentacoes.size()-1; i>=0; i--)
   	    	{
   	    		MovimentacaoDt movimentacaoDt = (MovimentacaoDt)listaMovimentacoes.get(i);			  	
			  	if (movimentacaoDt != null && movimentacaoDt.isValida() && movimentacaoDt.temArquivos())
			  	{
			  		List listaDeArquivos = movimentacaoArquivoNe.consultarArquivosMovimentacaoWebServiceComHash(movimentacaoDt.getId(), UsuarioSessao);
		  			if (listaDeArquivos != null)
		  			{
		  				for (int j = 0; j < listaDeArquivos.size(); j++) {
		  					MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) listaDeArquivos.get(j);
		  					
		  					movimentacaoArquivoDt.setArquivoDt(arquivoNe.consultarId(movimentacaoArquivoDt.getArquivoDt().getId()));
		  					
		  					if (movimentacaoArquivoDt.getValido() && 
		  						movimentacaoArquivoDt.getArquivoDt().conteudoBytes() != null && 
		  						movimentacaoArquivoDt.getArquivoDt().conteudoBytes().length > 0) {
		  						
		  						DocumentoProcessual documento = obtenhaDocumentoProcessual(movimentacaoArquivoDt, movimentacaoDt, nivelSigiloProcesso);
		  						
		  						if (Funcoes.StringToLong(documento.getDataHora().getValue()) >= dataReferencia) {
		  							if (tiposDeDocumentos != null && 
		  								tiposDeDocumentos.size() > 0 &&
		  								documento.getTipoDocumento() != null &&
		  								tiposDeDocumentos.contains(documento.getTipoDocumento())) {
		  								listaDeDocumentos.add(documento);	
		  							}	
		  						}		  						
		  					}
		  				}
		  			}	
			  	}
   	    	}   	    	
   	    }
		
		return listaDeDocumentos;
	}
	
	private DocumentoProcessual obtenhaDocumentoProcessual(MovimentacaoArquivoDt movimentacaoArquivoDt, MovimentacaoDt movimentacaoDt, int nivelSigiloProcesso) throws Exception
	{
		DocumentoProcessual documentoProcessual = new DocumentoProcessual();
		
		documentoProcessual.setDataHora(obtenhaDataHora(movimentacaoArquivoDt.getArquivoDt().getDataInsercao()));
		
		//documentoProcessual.setHash(movimentacaoArquivoDt.getHash());
		documentoProcessual.setHash(UsuarioSessao.getCodigoHashWebServiceMNI(movimentacaoArquivoDt.getId()));
		
		documentoProcessual.setIdDocumento(movimentacaoArquivoDt.getArquivoDt().getId());
		
		documentoProcessual.setMimetype(movimentacaoArquivoDt.getArquivoDt().getContentType());
		
		documentoProcessual.setMovimento(Funcoes.StringToInt(movimentacaoDt.getId()));
		
		documentoProcessual.setNivelSigilo(nivelSigiloProcesso);
		
		documentoProcessual.setTipoDocumento(movimentacaoArquivoDt.getArquivoDt().getArquivoTipoCodigo());
		
		Assinatura assinatura = new Assinatura();
		assinatura.setAlgoritmoHash("SHA1");
		assinatura.setDataAssinatura(obtenhaDataHora(movimentacaoDt.getDataRealizacao()));
		assinatura.setAssinatura(new String(Base64Utils.base64Encode(movimentacaoArquivoDt.getArquivoDt().conteudoBytes())));
		assinatura.setCadeiaCertificado(obtenhaCadeiaCertificados(assinatura.getAssinatura()));
		//assinatura.setCodificacaoCertificado(CodificacaoCertificado.PEM.getValor());		
		documentoProcessual.getAssinatura().add(assinatura);  						  					
		
		documentoProcessual.getOutroParametro().add(obtenhaParametro("NomeArquivo", movimentacaoArquivoDt.getArquivoDt().getNomeArquivoFormatado()));
		documentoProcessual.getOutroParametro().add(obtenhaParametro("ArquivoTipo", movimentacaoArquivoDt.getArquivoDt().getArquivoTipo()));
		
		return documentoProcessual;
	}
	
	private String obtenhaCadeiaCertificados(String assinaturaBase64) throws Exception
	{
		CMSSignedData conteudoAssinado = GerenciaArquivo.getInstancia().getSignedData(assinaturaBase64);
		
		List<X509Certificate> certificates = new VerificaP7s().getCertificadosAssinantes(conteudoAssinado);		
		
		StringBuilder resultado = new StringBuilder();
						
		for (X509Certificate certificate : certificates) {
			byte[] bytes = certificate.getEncoded();
			resultado.append("-----BEGIN CERTIFICATE-----");
			resultado.append("\n");
			//resultado.append(new Base64(64).encodeToString(bytes));
			resultado.append(new String(Base64Utils.base64Encode(bytes)));
			resultado.append("-----END CERTIFICATE-----");
			resultado.append("\n");
		}
		
		resultado.deleteCharAt(resultado.length()-1);
		return resultado.toString();		
	}
	
	private List<MovimentacaoProcessual> efetueMapeamentoMovimentacoesProcessuaisProjudi(ProcessoDt processoCompleto, int nivelSigiloProcesso, long dataReferencia) throws Exception
	{
		List<MovimentacaoProcessual> listaDeMovimentacoesProcessuais = new ArrayList<MovimentacaoProcessual>();
		
		List listaMovimentacoes = processoCompleto.getListaMovimentacoes();
	    
   	    if (listaMovimentacoes != null)
   	    {
   	    	for (int i=listaMovimentacoes.size()-1; i>=0; i--)
   	    	{
			  	MovimentacaoDt movimentacaoDt = (MovimentacaoDt)listaMovimentacoes.get(i);			  	
			  	if (movimentacaoDt != null && movimentacaoDt.isValida())
			  	{
			  		MovimentacaoProcessual movimentacaoProcessual = new MovimentacaoProcessual();
			  		movimentacaoProcessual.setDataHora(obtenhaDataHora(movimentacaoDt.getDataRealizacao()));
			  		
			  		if (Funcoes.StringToLong(movimentacaoProcessual.getDataHora().getValue()) >= dataReferencia) {
			  			movimentacaoProcessual.setNivelSigilo(nivelSigiloProcesso);
				  		if (movimentacaoDt.temArquivos())
				  		{
				  			List listaDeArquivos = movimentacaoArquivoNe.consultarArquivosMovimentacaoWebServiceComHash(movimentacaoDt.getId(), UsuarioSessao);
				  			if (listaDeArquivos != null)
				  			{
				  				for (int j = 0; j < listaDeArquivos.size(); j++) {
				  					MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) listaDeArquivos.get(j);
				  					movimentacaoProcessual.getIdDocumentoVinculado().add(movimentacaoArquivoDt.getArquivoDt().getId());
				  				}
				  			}			  			
				  		}
				  		
				  		movimentacaoDt = movimentacaoNe.consultarId(movimentacaoDt.getId());
				  		
				  		movimentacaoProcessual.setIdentificadorMovimento(movimentacaoDt.getId());
				  		movimentacaoProcessual.setMovimentoLocal(efetueMapeamentoMovimentoLocal(Funcoes.StringToInt(movimentacaoDt.getMovimentacaoTipoCodigo()), movimentacaoDt.getMovimentacao()));
				  		movimentacaoProcessual.getComplemento().add(movimentacaoDt.getComplemento());
				  		
				  		MovimentacaoTipoDt movimentacaoTipoDt = movimentacaoTipoNe.consultarId(movimentacaoDt.getId_MovimentacaoTipo());			  		
				  		if (movimentacaoTipoDt != null)
				  		{	
				  			if (movimentacaoTipoDt.getId_CNJ() != null && movimentacaoTipoDt.getId_CNJ().trim().length() > 0)
				  			{
				  				MovimentoNacional movimentacaoNacional = new MovimentoNacional();
						  		movimentacaoNacional.setCodigoNacional(Funcoes.StringToInt(movimentacaoTipoDt.getId_CNJ().trim()));
						  		//movimentacaoNacional.getComplemento().addAll(c); //Não mapeado...
						  		movimentacaoProcessual.setMovimentoNacional(movimentacaoNacional);
				  			}
				  		}
				  		
				  		listaDeMovimentacoesProcessuais.add(movimentacaoProcessual);
			  		}
			  	}
   	    	}
   	    }
		
		return listaDeMovimentacoesProcessuais;
	}
	
	private List<AssuntoProcessual> efetueMapeamentoAssuntosProcessuais(ProcessoDt processoCompleto, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		List<AssuntoProcessual> listaDeAssuntos = new ArrayList<AssuntoProcessual>();
		
		boolean possuiAssuntoPrincipal = false;
		
		if (processoCompleto.getListaAssuntos() != null)
		{
			for (int i = 0; i < processoCompleto.getListaAssuntos().size(); i++) 
			{
				ProcessoAssuntoDt processoAssunto = (ProcessoAssuntoDt) processoCompleto.getListaAssuntos().get(i);
				
				AssuntoDt assuntoDt = assuntoNe.consultarId(processoAssunto.getId_Assunto(), obFabricaConexaoPJD);
				
				AssuntoLocal assuntoLocal = new AssuntoLocal();
				assuntoLocal.setDescricao(processoAssunto.getAssunto());
				
				AssuntoProcessual assuntoProcessual = new AssuntoProcessual();
				assuntoProcessual.setAssuntoLocal(assuntoLocal);
				
				if (assuntoDt != null)
				{
					assuntoProcessual.setCodigoNacional(Funcoes.StringToInt(assuntoDt.getAssuntoCodigo()));
					assuntoLocal.setCodigoAssunto(Funcoes.StringToInt(assuntoDt.getId()));
										
					if (Funcoes.StringToInt(assuntoDt.getId_AssuntoPai()) > 0)
					{
						AssuntoLocal assuntoLocalPai = new AssuntoLocal();
						assuntoLocalPai.setCodigoAssunto(Funcoes.StringToInt(assuntoDt.getId_AssuntoPai()));				
						assuntoLocalPai.setDescricao(assuntoDt.getAssuntoPai());
						
						assuntoLocal.setAssuntoLocalPai(assuntoLocalPai);	
					}
				} else {
					assuntoLocal.setCodigoAssunto(Funcoes.StringToInt(processoAssunto.getId_Assunto()));
					assuntoProcessual.setCodigoNacional(Funcoes.StringToInt(processoAssunto.getCodigoTemp()));
				}
				
				if (processoCompleto.getId_Assunto() != null && processoCompleto.getId_Assunto().trim().equalsIgnoreCase(assuntoDt.getId().trim())) {
					assuntoProcessual.setPrincipal(true);
					possuiAssuntoPrincipal = true;
				} else {
					assuntoProcessual.setPrincipal(false);
				}
					
				
				listaDeAssuntos.add(assuntoProcessual);
			}
		}
		
		if (!possuiAssuntoPrincipal && listaDeAssuntos.size() > 0) listaDeAssuntos.get(0).setPrincipal(true);
		
		return listaDeAssuntos;
	}
	
	private void efetueMapeamentoMagistradosAtuantesEIndicadorDoMP(ProcessoDt processoCompleto, CabecalhoProcessual cabecalhoProcessualWs) throws Exception
	{				
		List listaResponsaveis = processoNe.consultarResponsaveisProcesso(processoCompleto.getId(), processoCompleto.getId_Serventia(), String.valueOf(GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL));
		
		cabecalhoProcessualWs.setIntervencaoMP(false);
		if (listaResponsaveis != null && listaResponsaveis.size() > 0)
		{
  			for (int i=0; i < listaResponsaveis.size(); i++)
  			{  				
  				ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) listaResponsaveis.get(i);
  				
  				if (serventiaCargoDt.isMagistrado())
  				{
  					CadastroIdentificador responsavel = new CadastroIdentificador();
  	  				
  	  				responsavel.setValue(serventiaCargoDt.getCargoTipo() + "-" + serventiaCargoDt.getNomeUsuario() + "-" + serventiaCargoDt.getServentia());
  	  				
  	  				cabecalhoProcessualWs.getMagistradoAtuante().add(responsavel);	
  	  				
  	  				if (Funcoes.StringToLong(serventiaCargoDt.getId_Serventia()) > 0 &&
  	  					serventiaCargoDt.getServentia() != null &&
  	  					serventiaCargoDt.getServentia().trim().length() > 0) {
  	  					cabecalhoProcessualWs.getOrgaoJulgador().setCodigoOrgao(serventiaCargoDt.getId_Serventia());
  	  					cabecalhoProcessualWs.getOrgaoJulgador().setNomeOrgao(serventiaCargoDt.getServentia());
  	  				}
  				}
  				else if (serventiaCargoDt.isMP())  				
  					cabecalhoProcessualWs.setIntervencaoMP(true);  				
  			}
		}
	}
	
	private List<Parametro> efetueMapeamentoOutrosParametros(ProcessoDt processoCompleto) throws Exception
	{
		List<Parametro> listaDeOutrosParametros = new ArrayList<Parametro>();
		
		listaDeOutrosParametros.add(obtenhaParametro("Area", processoCompleto.getArea()));		
		listaDeOutrosParametros.add(obtenhaParametro("ForumCodigo", processoCompleto.getForumCodigo()));
		listaDeOutrosParametros.add(obtenhaParametro("ProcessoFase", processoCompleto.getProcessoFase()));
		listaDeOutrosParametros.add(obtenhaParametro("ProcessoStatus", processoCompleto.getProcessoStatus()));
		listaDeOutrosParametros.add(obtenhaParametro("ProcessoTipo", processoCompleto.getProcessoTipo()));
		listaDeOutrosParametros.add(obtenhaParametro("Serventia", processoCompleto.getServentia()));
		listaDeOutrosParametros.add(obtenhaParametro("ServentiaCodigo", processoCompleto.getServentiaCodigo()));
		listaDeOutrosParametros.add(obtenhaParametro("ServentiaTipoCodigo", processoCompleto.getServentiaTipoCodigo()));
		listaDeOutrosParametros.add(obtenhaParametro("ProcessoStatusCodigo", processoCompleto.getProcessoStatusCodigo()));		
		listaDeOutrosParametros.add(obtenhaParametro("EfeitoSuspensivo", processoCompleto.getEfeitoSuspensivo()));
		listaDeOutrosParametros.add(obtenhaParametro("Penhora", processoCompleto.getPenhora()));
		listaDeOutrosParametros.add(obtenhaParametro("ValorCondenacao", processoCompleto.getValorCondenacao()));
		if (processoCompleto.getDataRecebimento() != null && processoCompleto.getDataRecebimento().trim().length() > 0)
		{
			Data dataDistribuicao = obtenhaData(processoCompleto.getDataRecebimento());
			if (dataDistribuicao != null) listaDeOutrosParametros.add(obtenhaParametro("DataDistribuicao", dataDistribuicao.getValue()));	
		}
		if (processoCompleto.getDataTransitoJulgado() != null && processoCompleto.getDataTransitoJulgado().trim().length() > 0)
		{
			DataHora dataTransitoEmJulgado = obtenhaDataHora(processoCompleto.getDataTransitoJulgado());
			if (dataTransitoEmJulgado != null) listaDeOutrosParametros.add(obtenhaParametro("DataTransitoJulgado", dataTransitoEmJulgado.getValue()));	
		}	
		listaDeOutrosParametros.add(obtenhaParametro("Sistema", "1"));
		listaDeOutrosParametros.add(obtenhaParametro("IdProcesso", processoCompleto.getId()));	
		listaDeOutrosParametros.add(obtenhaParametro("HashProcesso", UsuarioSessao.getCodigoHashWebServiceMNI(processoCompleto.getId())));
		return listaDeOutrosParametros;
	}
	
	private List<PoloProcessual> efetueMapeamentoPolosProcessuais(ProcessoDt processoCompleto, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		List<PoloProcessual> listaDePolosProcessuais = new ArrayList<PoloProcessual>();
		
		HashMap<Long, List<ProcessoParteAdvogadoDt>> dicAdvogados = processoNe.consultarAdvogadosProcessoMNI(processoCompleto.getId(), obFabricaConexaoPJD);
		
		PoloProcessual poloProcessualPromoventes = new PoloProcessual();		
		poloProcessualPromoventes.setPolo(ModalidadePoloProcessual.AT);
		for (int i = 0; i < processoCompleto.getListaPromoventesAtivos().size(); i++)
			poloProcessualPromoventes.getParte().add(obtenhaParte((ProcessoParteDt) processoCompleto.getListaPromoventesAtivos().get(i), dicAdvogados, true, obFabricaConexaoPJD));
		listaDePolosProcessuais.add(poloProcessualPromoventes);
		
		PoloProcessual poloProcessualPromovidos = new PoloProcessual();		
		poloProcessualPromovidos.setPolo(ModalidadePoloProcessual.PA);
		for (int i = 0; i < processoCompleto.getListaPromovidosAtivos().size(); i++)
			poloProcessualPromovidos.getParte().add(obtenhaParte((ProcessoParteDt) processoCompleto.getListaPromovidosAtivos().get(i), dicAdvogados, true, obFabricaConexaoPJD));
		listaDePolosProcessuais.add(poloProcessualPromovidos);
		
		if (processoCompleto.getListaOutrasPartes() != null && processoCompleto.getListaOutrasPartes().size() > 0)
		{
			PoloProcessual poloProcessualOutrasPartes = new PoloProcessual();		
			poloProcessualOutrasPartes.setPolo(ModalidadePoloProcessual.TC);
			for (int i = 0; i < processoCompleto.getListaOutrasPartes().size(); i++)
				poloProcessualOutrasPartes.getParte().add(obtenhaParte((ProcessoParteDt) processoCompleto.getListaOutrasPartes().get(i), dicAdvogados, false, obFabricaConexaoPJD));
			listaDePolosProcessuais.add(poloProcessualOutrasPartes);	
		}
		
		return listaDePolosProcessuais;
	}
	
	private List<String> efetueMapeamentoPrioridades(ProcessoDt processoCompleto)
	{
		List<String> listaDePrioridades = new ArrayList<String>();
		
		if (processoCompleto.getProcessoPrioridade() != null && processoCompleto.getProcessoPrioridade().trim().length() > 0)
			listaDePrioridades.add(processoCompleto.getProcessoPrioridadeCodigo() + "-" + processoCompleto.getProcessoPrioridade());
		
		return listaDePrioridades;
	}
	
	private List<VinculacaoProcessual> efetueMapeamentoVinculosProcessuais(ProcessoDt processoCompleto)
	{
		List<VinculacaoProcessual> listaDeVinculosProcessuais = new ArrayList<VinculacaoProcessual>();
		
		if (processoCompleto.getProcessoDependenteDt() != null)
		{
			VinculacaoProcessual processoVinculado = new VinculacaoProcessual();
			
			NumeroUnico numeroProcessoDependente = new NumeroUnico();			
			numeroProcessoDependente.setValue(processoCompleto.getProcessoDependenteDt().getProcessoNumeroCompleto());
			processoVinculado.setNumeroProcesso(numeroProcessoDependente);
			
			processoVinculado.setVinculo(ModalidadeVinculacaoProcesso.DP);
		}
		
		return listaDeVinculosProcessuais;
	}
	
	private Parte obtenhaParte(ProcessoParteDt processoParteDt, 
			                   HashMap<Long, List<ProcessoParteAdvogadoDt>> dicAdvogados, 
			                   boolean isRecarregaParte,
			                   FabricaConexao obFabricaConexaoPJD) throws Exception
	{	
		if (processoParteDt == null) return null;
		if (isRecarregaParte && processoParteDt.getId() != null && processoParteDt.getId().trim().length() > 0) {
			ProcessoParteDt processoParteCompletaDt = processoParteNe.consultarId(processoParteDt.getId(), obFabricaConexaoPJD);
			if (processoParteCompletaDt != null) processoParteDt = processoParteCompletaDt;
		}		
		
		Parte parte = new Parte();		
		
		Pessoa pessoa = new Pessoa();
		pessoa.setCidadeNatural(processoParteDt.getCidadeNaturalidade());
		if (processoParteDt.getDataNascimento() != null && processoParteDt.getDataNascimento().trim().length() > 0)
		{
			pessoa.setDataNascimento(obtenhaData(processoParteDt.getDataNascimento()));	
		}
		
		pessoa.setNome(processoParteDt.getNome());
		pessoa.setNomeGenitor(processoParteDt.getNomePai());
		pessoa.setNomeGenitora(processoParteDt.getNomeMae());
		
		CadastroIdentificador documentoPrincipal = new CadastroIdentificador();
		documentoPrincipal.setValue(processoParteDt.getCpfCnpj());
		pessoa.setNumeroDocumentoPrincipal(documentoPrincipal);
					
		if (processoParteDt.getSexo().equalsIgnoreCase("F"))						
			pessoa.setSexo(ModalidadeGeneroPessoa.F);		
		else if (processoParteDt.getSexo().equalsIgnoreCase("M"))
			pessoa.setSexo(ModalidadeGeneroPessoa.M);			
		else
			pessoa.setSexo(ModalidadeGeneroPessoa.D);
		
		if (processoParteDt.getCpfCnpj().trim().length() > 11)				
			pessoa.setTipoPessoa(TipoQualificacaoPessoa.JURIDICA);
		else 
			pessoa.setTipoPessoa(TipoQualificacaoPessoa.FISICA);
		
		if (processoParteDt.getRg() != null && processoParteDt.getRg().trim().length() > 0)
		{
			DocumentoIdentificacao documentoIdentidade = new DocumentoIdentificacao();
			documentoIdentidade.setTipoDocumento(ModalidadeDocumentoIdentificador.CI);
			documentoIdentidade.setCodigoDocumento(processoParteDt.getRg());
			documentoIdentidade.setEmissorDocumento(processoParteDt.getRgOrgaoExpedidor());
			documentoIdentidade.setNome("RG");
			pessoa.getDocumento().add(documentoIdentidade);	
		}
		
		if (processoParteDt.getCtps() != null && processoParteDt.getCtps().trim().length() > 0)
		{
			DocumentoIdentificacao ctps = new DocumentoIdentificacao();
			ctps.setTipoDocumento(ModalidadeDocumentoIdentificador.CT);
			ctps.setCodigoDocumento(processoParteDt.getCtps());
			ctps.setEmissorDocumento(processoParteDt.getCtpsSerie());
			ctps.setNome("CTPS");
			pessoa.getDocumento().add(ctps);	
		}
		
		if (processoParteDt.getPis() != null && processoParteDt.getPis().trim().length() > 0)
		{
			DocumentoIdentificacao pis = new DocumentoIdentificacao();
			pis.setTipoDocumento(ModalidadeDocumentoIdentificador.PIS_PASEP);
			pis.setCodigoDocumento(processoParteDt.getPis());			
			pis.setNome("PIS");
			pessoa.getDocumento().add(pis);
		}
		
		if (processoParteDt.getTituloEleitor() != null && processoParteDt.getTituloEleitor().trim().length() > 0)
		{
			DocumentoIdentificacao tituloEleitoral = new DocumentoIdentificacao();
			tituloEleitoral.setTipoDocumento(ModalidadeDocumentoIdentificador.TE);
			tituloEleitoral.setCodigoDocumento(processoParteDt.getTituloEleitor());
			tituloEleitoral.setEmissorDocumento(processoParteDt.getTituloEleitorSecao() + " - " + processoParteDt.getTituloEleitorZona());
			tituloEleitoral.setNome("TITULO_ELEITORAL");
			pessoa.getDocumento().add(tituloEleitoral);	
		}
		
		parte.setPessoa(pessoa);
		
		if (dicAdvogados != null) {
			List<ProcessoParteAdvogadoDt> listaAdvogados = dicAdvogados.get(Funcoes.StringToLong(processoParteDt.getId().trim()));
			if (listaAdvogados != null)
			{
				for (ProcessoParteAdvogadoDt advogado : listaAdvogados) {
					RepresentanteProcessual representanteProcessual = new RepresentanteProcessual();
					
					CadastroOAB cadastroOAB = new CadastroOAB();
					cadastroOAB.setValue(advogado.getOabNumero() + "-" + advogado.getOabComplemento() + "-" + advogado.getEstadoOabUf());				
					representanteProcessual.setInscricao(cadastroOAB);
					
					representanteProcessual.setIntimacao(advogado.getRecebeIntimacao());
					representanteProcessual.setNome(advogado.getNomeAdvogado());
					representanteProcessual.setNumeroDocumentoPrincipal(advogado.getUsuarioAdvogado());
					representanteProcessual.setTipoRepresentante(ModalidadeRepresentanteProcessual.A);
					
					parte.getAdvogado().add(representanteProcessual);				
				}			
			}
		}
		
		return parte;
	}
	
	private Data obtenhaData(String dataProjudiStr) throws Exception
	{
		Date dataProjudi = Funcoes.StringToDate(dataProjudiStr);
  		SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMdd");
  		Data dataMovimentacao = new Data();
  		dataMovimentacao.setValue(formatoData.format(dataProjudi));
  		
  		return dataMovimentacao;
	}
	
	private DataHora obtenhaDataHora(String dataHoraProjudi)
	{
		try {
			if (dataHoraProjudi == null || dataHoraProjudi.trim().length() == 0) return null;
			
			Date dataHoraMovimentacaoProjudi = Funcoes.DataHora(dataHoraProjudi);
	  		SimpleDateFormat formatoData = new SimpleDateFormat("yyyyMMddHHmmss");
	  		DataHora dataHoraMovimentacao = new DataHora();
	  		dataHoraMovimentacao.setValue(formatoData.format(dataHoraMovimentacaoProjudi));
	  		
	  		return dataHoraMovimentacao;
		} catch (Exception e) {
			return new DataHora();
		}
	}
	
	private Parametro obtenhaParametro(String chave, String valor)
	{
		Parametro parametro = new Parametro();
		parametro.setNome(chave);
		parametro.setValor(valor);
		return parametro;
	}
	
	private void consultarAlteracao(RequisicaoConsultaAlteracao requisicao, RespostaConsultaAlteracao resposta, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		ProcessoDt processoDt = processoNe.consultarProcessoNumeroCompleto(obtenhaNumeroProcesso(requisicao.getNumeroProcesso()), obFabricaConexaoPJD);
		
		if (processoDt == null) 
			throw new MensagemException("Processo não cadastrado.");
		
		//Obtendo a última alteração do processo...
		List listalogAteracaoProcesso = logNe.consultarLog(null, "Processo", null, null, null, String.valueOf(LogTipoDt.Alterar), "0", processoDt.getId());
		TJDataHora dataHoraAlteracaoProcesso = null;
		if (listalogAteracaoProcesso != null)
		{
			for (int i = 0; i < listalogAteracaoProcesso.size(); i++)
			{
				LogDt logDt = (LogDt)listalogAteracaoProcesso.get(i);
				
				TJDataHora dataHoraAlteracaoProcessoAtual = new TJDataHora(logDt.getData(), Funcoes.FormatarHora(logDt.getHora()));
				
				if (dataHoraAlteracaoProcesso == null || dataHoraAlteracaoProcessoAtual.ehApos(dataHoraAlteracaoProcesso))
					dataHoraAlteracaoProcesso = dataHoraAlteracaoProcessoAtual;
					
			}
		}
		
		if (dataHoraAlteracaoProcesso == null)					
		{
			dataHoraAlteracaoProcesso = new TJDataHora();
			dataHoraAlteracaoProcesso.setDataddMMaaaaHHmmss(processoDt.getDataRecebimento());
		}
		resposta.setHashCabecalho(dataHoraAlteracaoProcesso.getDataHoraFormatadayyyyMMddHHmmss());
		
		processoDt =  processoNe.consultarDadosProcessoMNI(processoDt.getId(), UsuarioSessao.getUsuarioDt(), UsuarioSessao.getNivelAcesso(), true, obFabricaConexaoPJD);
		if (processoDt.getListaMovimentacoes() != null)
   	    {
			//Obtendo a última alteração de movimentação e de arquivo...
			TJDataHora dataHoraAlteracaoMovimentacao = null;
			TJDataHora dataHoraAlteracaoArquivo = null;
			
   	    	for (int i = processoDt.getListaMovimentacoes().size()-1; i>=0; i--)
   	    	{	
			  	MovimentacaoDt movimentacaoDt = (MovimentacaoDt)processoDt.getListaMovimentacoes().get(i);			  	
			  	if (movimentacaoDt != null && movimentacaoDt.isValida())
			  	{
			  		TJDataHora dataHoraAlteracaoMovimentacaoAtual = new TJDataHora();
			  		dataHoraAlteracaoMovimentacaoAtual.setDataddMMaaaaHHmmss(movimentacaoDt.getDataRealizacao());
			  		
			  		if (dataHoraAlteracaoMovimentacao == null || dataHoraAlteracaoMovimentacaoAtual.ehApos(dataHoraAlteracaoMovimentacao))
			  			dataHoraAlteracaoMovimentacao = dataHoraAlteracaoMovimentacaoAtual;
			  		
			  		if (movimentacaoDt.temArquivos())
			  		{
			  			List listaDeArquivos = movimentacaoArquivoNe.consultarArquivosMovimentacaoWebServiceComHash(movimentacaoDt.getId(), UsuarioSessao);
			  			if (listaDeArquivos != null)
			  			{
			  				for (int j = 0; j < listaDeArquivos.size(); j++) {
			  					MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) listaDeArquivos.get(j);
			  					
			  					TJDataHora dataHoraAlteracaoArquivoAtual = new TJDataHora();
			  					dataHoraAlteracaoArquivoAtual.setDataddMMaaaaHHmmss(movimentacaoArquivoDt.getArquivoDt().getDataInsercao());
			  					
			  					if (dataHoraAlteracaoArquivo == null || dataHoraAlteracaoArquivoAtual.ehApos(dataHoraAlteracaoArquivo))
			  						dataHoraAlteracaoArquivo = dataHoraAlteracaoArquivoAtual;
			  				}
			  			}			  			
			  		}			  		
			  	}
   	    	}
   	    	
   	    	if (dataHoraAlteracaoMovimentacao != null)
   	    		resposta.setHashMovimentacoes(dataHoraAlteracaoMovimentacao.getDataHoraFormatadayyyyMMddHHmmss());
   	    	
   	    	if (dataHoraAlteracaoArquivo != null)
   	    		resposta.setHashDocumentos(dataHoraAlteracaoArquivo.getDataHoraFormatadayyyyMMddHHmmss());
   	    }
		
		resposta.setSucesso(true);
	}
	
	private void consultarAvisosPendentes(RequisicaoConsultaAvisosPendentes requisicao, RespostaConsultaAvisosPendentes resposta, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		if (requisicao.getIdRepresentado() != null && requisicao.getIdRepresentado().trim().length() > 0) {
			UsuarioDt usuarioRepresentado = UsuarioSessao.consultarUsuarioLogin(requisicao.getIdRepresentado(), obFabricaConexaoPJD);
			
			if (usuarioRepresentado == null)
				throw new MensagemException("Usuário representado não cadastrado.");
			
			inicializaUsuarioNaSessao(usuarioRepresentado, true, obFabricaConexaoPJD);	
		}
		
		List listadeServentias = (List) UsuarioSessao.getListaServentiasGruposUsuario();		
		if (listadeServentias != null)
		{
			UsuarioDt usuarioServentiaDt = null;
			for (int i = 0; i < UsuarioSessao.getListaServentiasGruposUsuario().size(); i++) {	
				usuarioServentiaDt = (UsuarioDt) listadeServentias.get(i);
				
				inicializaUsuarioNaSessao(usuarioServentiaDt, false, obFabricaConexaoPJD);
				
				consultarAvisosPendentesUsuarioCorrente(resposta, getDataReferencia(getDataReferenciaString(requisicao)), pendenciaNe.consultarTodasPendenciasSemDataFim(usuarioServentiaDt.getId_ServentiaCargo()), obFabricaConexaoPJD);
				
				consultarAvisosPendentesUsuarioCorrente(resposta, getDataReferencia(getDataReferenciaString(requisicao)), pendenciaNe.consultarTodasPendenciasSemDataFimUsuarioServentia(usuarioServentiaDt.getId_UsuarioServentia()), obFabricaConexaoPJD);
			}	
		}
	
		resposta.setSucesso(true);
	}
	
	private boolean isDataReferenciaInformada(RequisicaoConsultaAvisosPendentes requisicao) {
		return (requisicao.getDataReferencia() != null &&
				requisicao.isSetDataReferencia() &&
				requisicao.getDataReferencia() != null &&
				requisicao.getDataReferencia().isSetValue() &&
				requisicao.getDataReferencia().getValue() != null &&
				requisicao.getDataReferencia().getValue().trim().length() > 0);
	}
	
	private String getDataReferenciaString(RequisicaoConsultaAvisosPendentes requisicao) {
		if (isDataReferenciaInformada(requisicao)) {
			return requisicao.getDataReferencia().getValue().trim();
		}
		return "";
	}
	
	private void consultarAvisosPendentesUsuarioCorrente(RespostaConsultaAvisosPendentes resposta, long dataReferencia, List listaDePendencias, FabricaConexao obFabricaConexaoPJD) throws Exception
	{			
		ProcessoDt processoDt = null;
		
		if (listaDePendencias != null)
		{
			PendenciaDt pendenciaDt = null;
			for (int i = 0; i < listaDePendencias.size(); i++) 
			{	
				pendenciaDt = (PendenciaDt) listaDePendencias.get(i);
				
				AvisoComunicacaoPendente avisoPendente = new AvisoComunicacaoPendente();				
					
				avisoPendente.setDataDisponibilizacao(obtenhaDataHora(pendenciaDt.getDataInicio()));
				
				TipoComunicacao tipoComunicacao = new TipoComunicacao();
				//tipoComunicacao.setValue(pendenciaDt.getPendencia() + " - " + pendenciaDt.getMovimentacao() + " - " + pendenciaDt.getPendenciaStatus());
				tipoComunicacao.setValue(getTipoComunicacao(pendenciaDt.getPendenciaTipoCodigoToInt()));
				avisoPendente.setTipoComunicacao(tipoComunicacao);
								
				Identificador aviso = new Identificador();
				aviso.setValue(pendenciaDt.getId());		
				avisoPendente.setIdAviso(aviso);
								
				processoDt = null;
				if (pendenciaDt.getId_Processo() != null && pendenciaDt.getId_Processo().trim().length() > 0)	
				{
					processoDt = processoNe.consultarDadosProcessoMNI(pendenciaDt.getId_Processo(), UsuarioSessao.getUsuarioDt(), UsuarioSessao.getNivelAcesso(), false, obFabricaConexaoPJD);					
					if (processoDt != null)
					{
						ProcessoJudicial processo = obtenhaProcessoJudicialProjudi(processoDt, true, false, false, dataReferencia, new ArrayList<String>(), obFabricaConexaoPJD);
						
						if (processo != null) avisoPendente.setProcesso(processo.getDadosBasicos());
					}
				}
				
				Parte parte = obtenhaPartePendencia(pendenciaDt, processoDt, obFabricaConexaoPJD);
				if (parte != null)
					avisoPendente.setDestinatario(parte);
				
				resposta.getAviso().add(avisoPendente);
			}
		}
	}
	
	//Tipo de comunicação pendente de ciência. São valores possíveis: 
	//- CIT: citação 
	//- INT: intimação 
	//- NOT: notificação 
	//- VIS: vista para manifestação 
	//- URG: urgente 
	//- PTA: pauta de julgamento/audiência 
	private String getTipoComunicacao(int pendenciaTipoCodigo) {
		String tipoComunicacao = "";
		
		switch (pendenciaTipoCodigo) {
			case PendenciaTipoDt.CARTA_CITACAO:
				tipoComunicacao = "CIT";
				break;
				
			case PendenciaTipoDt.CARTA_NOTIFICACAO:
				tipoComunicacao = "NOT";
				break;
				
			case PendenciaTipoDt.PEDIDO_MANIFESTACAO:
			case PendenciaTipoDt.PEDIDO_VISTA:
				tipoComunicacao = "VIS";
				break;			
				
			default:
				tipoComunicacao = "INT";
				break;
		}
		
		return tipoComunicacao;
	}
	
	private Parte obtenhaPartePendencia(PendenciaDt pendenciaDt, ProcessoDt processoDt, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		if (pendenciaDt.getId_ProcessoParte() != null && pendenciaDt.getId_ProcessoParte().trim().length() > 0 && processoDt != null)
		{
			HashMap<Long, List<ProcessoParteAdvogadoDt>> dicAdvogados = processoNe.consultarAdvogadosProcessoMNI(processoDt.getId(), obFabricaConexaoPJD);
			
			ProcessoParteDt processoParteDt = null;
			for (int j = 0; j < processoDt.getListaPromoventesAtivos().size(); j++)
			{
				processoParteDt = (ProcessoParteDt) processoDt.getListaPromoventesAtivos().get(j);						
				if (processoParteDt != null && processoParteDt.getId() != null && processoParteDt.getId().trim().equals(pendenciaDt.getId_ProcessoParte().trim()))
					return obtenhaParte(processoParteDt, dicAdvogados, true, obFabricaConexaoPJD);
			}
			
			for (int j = 0; j < processoDt.getListaPromovidosAtivos().size(); j++)
			{
				processoParteDt = (ProcessoParteDt) processoDt.getListaPromovidosAtivos().get(j);						
				if (processoParteDt != null && processoParteDt.getId() != null && processoParteDt.getId().trim().equals(pendenciaDt.getId_ProcessoParte().trim()))
					return obtenhaParte(processoParteDt, dicAdvogados, true, obFabricaConexaoPJD);
			}
				
			if (processoDt.getListaOutrasPartes() != null && processoDt.getListaOutrasPartes().size() > 0)
			{
				for (int j = 0; j < processoDt.getListaOutrasPartes().size(); j++)
				{
					processoParteDt = (ProcessoParteDt) processoDt.getListaOutrasPartes().get(j);						
					if (processoParteDt != null && processoParteDt.getId() != null && processoParteDt.getId().trim().equals(pendenciaDt.getId_ProcessoParte().trim()))
						return obtenhaParte(processoParteDt, dicAdvogados, false, obFabricaConexaoPJD);
				}
			}
		}
		
		return null;
	}
	
	private void consultarTeorComunicacaoTJGO(RequisicaoConsultarTeorComunicacao requisicao, RespostaConsultarTeorComunicacao resposta, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		ProcessoDt processoDt = null;
		PendenciaDt pendenciaDt = null;
		
		pendenciaDt = pendenciaNe.consultarId(requisicao.getIdentificadorAviso().getValue());
		if(pendenciaDt == null)
			throw new MensagemException("Pendência não localizada com o IdentificadorAviso informado.");
		
		processoDt = processoNe.consultarDadosProcessoMNI(pendenciaDt.getId_Processo(), UsuarioSessao.getUsuarioDt(), UsuarioSessao.getNivelAcesso(), false, obFabricaConexaoPJD);	
		if(processoDt == null)
			throw new MensagemException("Processo não localizado vinculado a ocorrência.");
		
		if (!processoDt.getProcessoNumeroCompleto().trim().equalsIgnoreCase(obtenhaNumeroProcesso(requisicao.getNumeroProcesso()).trim()))
			throw new MensagemException("O processo informado no parâmetro NumeroProcesso não está vinculado à pendência informada no parâmetro IdentificadorAviso.");
		
		ComunicacaoProcessual comunicacaoProcessual = new ComunicacaoProcessual();
		
		ProcessoJudicial processo = obtenhaProcessoJudicialProjudi(processoDt, true, false, false, 0, new ArrayList<String>(), obFabricaConexaoPJD);
		
		comunicacaoProcessual.setDataReferencia(obtenhaDataHora(pendenciaDt.getDataInicio()));
		
		Parte parte = obtenhaPartePendencia(pendenciaDt, processoDt, obFabricaConexaoPJD);
		if (parte != null)
			comunicacaoProcessual.setDestinatario(parte);		
		
		Identificador idPendencia = new Identificador();
		idPendencia.setValue(pendenciaDt.getId());
		comunicacaoProcessual.setId(idPendencia);
		
		comunicacaoProcessual.setNivelSigilo(processo.getDadosBasicos().getNivelSigilo());
		
		comunicacaoProcessual.setProcesso(processoDt.getProcessoNumeroCompleto());
		
		comunicacaoProcessual.setTeor(pendenciaDt.getMovimentacao());
		
		TipoComunicacao tipoComunicacao = new TipoComunicacao();
		tipoComunicacao.setValue(pendenciaDt.getPendenciaTipoCodigo());
		comunicacaoProcessual.setTipoComunicacao(tipoComunicacao);
		
		comunicacaoProcessual.setTipoPrazo(TipoPrazo.SEMPRAZO);
		if (pendenciaDt.getDataLimite() != null && pendenciaDt.getDataLimite().trim().length() > 0)
		{
			TJDataHora prazo = new TJDataHora();
			prazo.setDataddMMaaaaHHmmss(pendenciaDt.getDataLimite());
			comunicacaoProcessual.setPrazo(Funcoes.StringToInt(prazo.getDataHoraFormatadayyyyMMdd()));
			comunicacaoProcessual.setTipoPrazo(TipoPrazo.DATA_CERTA);
		} 
		else if (Funcoes.StringToInt(pendenciaDt.getPrazo()) > 0)
		{
			comunicacaoProcessual.setPrazo(Funcoes.StringToInt(pendenciaDt.getPrazo()));
			comunicacaoProcessual.setTipoPrazo(TipoPrazo.DIA);
		}
		
		comunicacaoProcessual.getParametro().add(pendenciaDt.getPendencia());
		comunicacaoProcessual.getParametro().add(pendenciaDt.getPendenciaStatus());
		
		if (pendenciaDt.getId_Movimentacao() != null && pendenciaDt.getId_Movimentacao().trim().length() > 0)
		{
			if (processoDt.getListaMovimentacoes() != null)
	   	    {	
	   	    	for (int i = processoDt.getListaMovimentacoes().size()-1; i>=0; i--)
	   	    	{	
				  	MovimentacaoDt movimentacaoDt = (MovimentacaoDt)processoDt.getListaMovimentacoes().get(i);			  	
				  	if (movimentacaoDt != null && movimentacaoDt.isValida() && movimentacaoDt.getId().equals(pendenciaDt.getId_Movimentacao()) && movimentacaoDt.temArquivos())
				  	{		
				  		List listaDeArquivos = movimentacaoArquivoNe.consultarArquivosMovimentacaoWebServiceComHash(movimentacaoDt.getId(), UsuarioSessao);
			  			if (listaDeArquivos != null)
			  			{
			  				for (int j = 0; j < listaDeArquivos.size(); j++) {
			  					MovimentacaoArquivoDt movimentacaoArquivoDt = (MovimentacaoArquivoDt) listaDeArquivos.get(j);
			  					
			  					comunicacaoProcessual.getDocumento().add(obtenhaDocumentoProcessual(movimentacaoArquivoDt, movimentacaoDt, processo.getDadosBasicos().getNivelSigilo()));
			  				}
			  			}			  		
				  	}
	   	    	}	   	    	
	   	    }
		}
		
		resposta.getComunicacao().add(comunicacaoProcessual);
		
		resposta.setSucesso(true);
	}
	
	private void entregarManifestacaoProcessual(ManifestacaoProcessual requisicao, RespostaManifestacaoProcessual resposta, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		inicializaUsuarioNaSessaoNaPrimeiraServentia(obFabricaConexaoPJD);
		
		if (requisicao.getNumeroProcesso() != null && requisicao.getNumeroProcesso().isSetValue()) {
			String idMovimentacaoTipo = obtenhaValorDoParametro(requisicao.getParametros(), "ID_MOVIMENTACAO_TIPO");
			String complemento = obtenhaValorDoParametro(requisicao.getParametros(), "COMPLEMENTO");
			if (complemento != null && complemento.trim().length() > 255)
				complemento = complemento.substring(0, 254);
			
			if (idMovimentacaoTipo == null || idMovimentacaoTipo.trim().length() == 0)
				throw new MensagemException("O parâmetro ID_MOVIMENTACAO_TIPO não foi informado.");
			
			MovimentacaoTipoDt movimentacaoTipoDt = movimentacaoTipoNe.consultarId(idMovimentacaoTipo);
			if (movimentacaoTipoDt == null) 
				throw new MensagemException("Movimentação tipo não localizado.");
			entregarManifestacaoProcessualPeticaoProcessoExistente(requisicao, resposta, idMovimentacaoTipo, complemento);
		} else {
			entregarManifestacaoProcessualPeticaoInicialCadastroProcesso(requisicao, resposta);
		}
		
		TJDataHora dataHoraAtual = new TJDataHora();
		DataHora dataHoraMovimentacao = new DataHora();
  		dataHoraMovimentacao.setValue(dataHoraAtual.getDataHoraFormatadayyyyMMddHHmmss());
		resposta.setDataOperacao(dataHoraMovimentacao);
		
		resposta.setSucesso(true);
	}
	
	private void entregarManifestacaoProcessualPeticaoInicialCadastroProcesso(ManifestacaoProcessual requisicao, RespostaManifestacaoProcessual resposta) throws Exception
	{
		throw new MensagemException("O método para petição inicial ainda não foi implementado.");
		
		//requisicao.getDadosBasicos()
	}
	
	private void entregarManifestacaoProcessualPeticaoProcessoExistente(ManifestacaoProcessual requisicao, RespostaManifestacaoProcessual resposta, String idMovimentacaoTipo, String complemento) throws Exception
	{
		ProcessoDt processoDt = processoNe.consultarProcessoNumeroCompleto(obtenhaNumeroProcesso(requisicao.getNumeroProcesso()),null);		
		if (processoDt == null) 
			throw new MensagemException("Processo não localizado.");
		
		// Monta dados para peticionamento		
		PeticionamentoDt peticionamentoDt = new PeticionamentoDt();
		peticionamentoDt.setId_MovimentacaoTipo(idMovimentacaoTipo);		
		peticionamentoDt.addListaProcessos(processoDt);		
		peticionamentoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		String pedidoUrgencia = obtenhaValorDoParametro(requisicao.getParametros(), "INDICADOR_PEDIDO_URGENCIA");
		if (pedidoUrgencia != null && pedidoUrgencia.equalsIgnoreCase("S")) peticionamentoDt.setPedidoUrgencia(true);
		if (complemento != null && complemento.trim().length() > 0) peticionamentoDt.setComplemento(complemento.trim());
		
		for (DocumentoProcessual documento : requisicao.getDocumento())
		{
			if (documento == null || documento.getConteudo() == null || Funcoes.getTamanho(documento.getConteudo()) == 0)
				throw new MensagemException("Arquivo sem conteudo, por favor digite o conteudo deste arquivo ou selecione verifique se o arquivo está vazio.");
			
			String nomeArquivo = obtenhaValorDoParametro(documento.getOutroParametro(), "NOME_ARQUIVO");
			String idArquivoTipo = obtenhaValorDoParametro(documento.getOutroParametro(), "ID_ARQUIVO_TIPO");
			
			if (idArquivoTipo == null || idArquivoTipo.trim().length() == 0)
				throw new MensagemException("O parâmetro ID_ARQUIVO_TIPO não foi informado no documento " + documento.getIdDocumento() + ".");
			
			ArquivoTipoDt ArquivoTipoDt = arquivoTipoNe.consultarId(idArquivoTipo);
			if (ArquivoTipoDt == null) 
				throw new MensagemException("Arquivo tipo não localizado.");
			
			// Monta arquivo			
			ArquivoDt arquivoDt = new ArquivoDt();
			arquivoDt.setId_ArquivoTipo(ArquivoTipoDt.getId());
			arquivoDt.setArquivoTipo(ArquivoTipoDt.getArquivoTipo());
			arquivoDt.setNomeArquivo(nomeArquivo);			
			arquivoDt.setArquivo(Base64Utils.base64Encode(Funcoes.converterParaBytes(documento.getConteudo())));
			arquivoDt.setAssinado(true);
			
			try {
				
				//Resgata P7s. Esse será adicionado a lista de arquivos.
				GerenciaArquivo.getInstancia().getArquivoP7s(arquivoDt);
				
			} catch(GeneralSecurityException ex) {
				throw new MensagemException(ex.getMessage());
			}						

			//Se for arquivo on-line devolve o nome setado por padrão
			if (arquivoDt.getNomeArquivo().equals("")) 
				arquivoDt.setNomeArquivo("online.html");			
			
			peticionamentoDt.addListaArquivos(arquivoDt);	
		}
		
		movimentacaoNe.salvarPeticionamento(peticionamentoDt, UsuarioSessao.getUsuarioDt());
				
		resposta.setProtocoloRecebimento(peticionamentoDt.getId());
	}
	
	private String obtenhaValorDoParametro(List<Parametro> listaParametros, String chave)
	{
		for (Parametro parametro : listaParametros)
		{
			if (parametro.getNome() != null && !parametro.getNome().isEmpty() && parametro.getNome().trim().equalsIgnoreCase(chave))
				return parametro.getValor();
		}
		
		return null;
	}
	
	private void inicializaUsuarioNaSessaoNaPrimeiraServentia(FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		List listadeServentias = (List) UsuarioSessao.getListaServentiasGruposUsuario();
		if (listadeServentias == null || listadeServentias.size() == 0)
			throw new MensagemException("Não existem serventias habilitadas para esse usuário.");
		
		UsuarioDt usuarioServentiaDt = (UsuarioDt) listadeServentias.get(0);
		inicializaUsuarioNaSessao(usuarioServentiaDt, false, obFabricaConexaoPJD);		
	}
	
	private void inicializaUsuarioNaSessao(UsuarioDt usuarioDt, boolean ehParaConsultarUsuario, FabricaConexao obFabricaConexaoPJD) throws Exception
	{
		if (ehParaConsultarUsuario) usuarioDt = UsuarioSessao.consultarId(usuarioDt.getId(), obFabricaConexaoPJD);
		UsuarioSessao.atualizaUsuarioLogado(usuarioDt);		
		
		usuarioDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		usuarioDt.setDataEntrada(UsuarioSessao.getUsuarioDt().getDataEntrada());
		UsuarioSessao.setUsuarioDt(usuarioDt);
		
		UsuarioSessao.getMenu();		
		String stMensagem = "Usuário " + usuarioDt.getUsuario() + " fez logon no webservice Intercomunicacao com o papel de " + usuarioDt.getGrupo() + " na Serventia: " + usuarioDt.getServentia();
		UsuarioSessao.salvarLogAcesso(stMensagem);	
	}

	private HttpServletRequest requestDegub;
	public void setRequestDebug(HttpServletRequest request)
	{			
		this.requestDegub = request;		
	}
		
	private ProcessoJudicial obtenhaProcessoJudicialSPG(String numeroProcesso, boolean incluiMovimentos) throws Exception
	{	
		
		if (numeroProcesso == null)
			return null;
		
		if (numeroProcesso.contains(".")) {
			String[] vetorNumeroProcesso = numeroProcesso.split("\\.");
			
			if (vetorNumeroProcesso == null || vetorNumeroProcesso.length < 2)
				return null;
			
			numeroProcesso = (vetorNumeroProcesso[0].trim() + vetorNumeroProcesso[1].trim()).trim();
		} 
		
		String textoRetornoSPG = getTextoResposta(getUrlConsultarProcesso() + numeroProcesso);
		
		if (textoRetornoSPG == null || textoRetornoSPG.trim().length() == 0)
			return null;		
		
		String[] vetorPrincipalSPG = textoRetornoSPG.split(SEPARADOR_PROCESSO);		
		if (vetorPrincipalSPG == null || vetorPrincipalSPG.length == 0)
			throw new MensagemException("Ocorreu um erro ao consultar o processo no SPG. Retorno: " + textoRetornoSPG.trim());	
		
		if (Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.SUCESS.ordinal()].trim(), -1) == -1)
		{
			throw new Exception("Ocorreu um erro ao consultar o processo no SPG.", new Exception(textoRetornoSPG));
		}
		
		if (Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.SUCESS.ordinal()].trim()) == 0)
		{
			if (vetorPrincipalSPG[EnumDadosProcesso.MENSAGEM.ordinal()] == null || vetorPrincipalSPG[EnumDadosProcesso.MENSAGEM.ordinal()].trim().length() == 0)
				return null;
			
			throw new MensagemException("Ocorreu um erro ao consultar o processo no SPG. " + vetorPrincipalSPG[EnumDadosProcesso.MENSAGEM.ordinal()].trim());
		}
		
		if (vetorPrincipalSPG.length < EnumDadosProcesso.QUANTIDADE_MINIMA.ordinal())
			throw new MensagemException("Ocorreu um erro ao consultar o processo no SPG. Retorno: " + textoRetornoSPG.trim());
		
		return efetueMapeamentoProcessoJudicialSPG(numeroProcesso, vetorPrincipalSPG, incluiMovimentos);	
	}
	
	@SuppressWarnings("deprecation")
	private String getTextoResposta(String HttpGetUrl) throws Exception {
		
		try(org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient()){
			HttpGet httpget = new HttpGet(HttpGetUrl);
		
			HttpResponse response;
			String textoResposta = null;
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) textoResposta = EntityUtils.toString(entity);
				
			return textoResposta;
		}
	}
	
	private ProcessoJudicial efetueMapeamentoProcessoJudicialSPG(String numeroProcesso, String[] vetorPrincipalSPG, boolean incluiMovimentos) throws Exception
	{
		ProcessoJudicial processoCompletoWs = new ProcessoJudicial();
		
		processoCompletoWs.setDadosBasicos(efetueMapeamentoCabecalhoProcessualSPG(numeroProcesso, vetorPrincipalSPG));	

		if (incluiMovimentos)
			processoCompletoWs.getMovimento().addAll(efetueMapeamentoMovimentacoesProcessuaisSPG(vetorPrincipalSPG, processoCompletoWs.getDadosBasicos().getNivelSigilo()));
		
		return processoCompletoWs;
	}
	
	private CabecalhoProcessual efetueMapeamentoCabecalhoProcessualSPG(String numeroProcesso, String[] vetorPrincipalSPG) throws Exception
	{
		CabecalhoProcessual cabecalhoProcessualWs = new CabecalhoProcessual();
		
		cabecalhoProcessualWs.setClasseProcessual(Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.CODG_NATUREZA.ordinal()]));
		
		OrgaoJulgador orgaoJulgador = new OrgaoJulgador();
		orgaoJulgador.setCodigoOrgao(vetorPrincipalSPG[EnumDadosProcesso.CODG_SERVENTIA.ordinal()]);
		orgaoJulgador.setNomeOrgao(vetorPrincipalSPG[EnumDadosProcesso.DESC_SERVENTIA.ordinal()]);				
		cabecalhoProcessualWs.setOrgaoJulgador(orgaoJulgador);
			
		cabecalhoProcessualWs.setCompetencia(Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.COMPETENCIA.ordinal()]));

		cabecalhoProcessualWs.setNivelSigilo(Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.SEGREDO.ordinal()]));
		
		NumeroUnico numeroProcessoCompletoSPG = new NumeroUnico();
		numeroProcessoCompletoSPG.setValue(numeroProcesso);
		cabecalhoProcessualWs.setNumero(numeroProcessoCompletoSPG);
		
		cabecalhoProcessualWs.setTamanhoProcesso(0);	
		
		double valorCausa = Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.VALOR_CAUSA.ordinal()]);		
		if (valorCausa > 0) cabecalhoProcessualWs.setValorCausa((valorCausa / 100));		
		
		cabecalhoProcessualWs.getAssunto().addAll(efetueMapeamentoAssuntosProcessuaisSPG(vetorPrincipalSPG));
		
		cabecalhoProcessualWs.getOutroParametro().addAll(efetueMapeamentoOutrosParametrosSPG(vetorPrincipalSPG));
		
		cabecalhoProcessualWs.getPolo().addAll(efetueMapeamentoPolosProcessuaisSPG(vetorPrincipalSPG));
		
		return cabecalhoProcessualWs;
	}
	
	private List<AssuntoProcessual> efetueMapeamentoAssuntosProcessuaisSPG(String[] vetorPrincipalSPG) throws Exception
	{
		List<AssuntoProcessual> listaDeAssuntos = new ArrayList<AssuntoProcessual>();
		
		int codigoAssuntoCNJ = Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.CODG_ASSUNTO_CNJ.ordinal()]);
		
		int codigoAssuntoPaiCNJ = Funcoes.StringToInt(vetorPrincipalSPG[EnumDadosProcesso.CODG_ASSUNTO_PAI_CNJ.ordinal()]);
		
		if (codigoAssuntoCNJ > 0)
		{
			AssuntoLocal assuntoLocal = new AssuntoLocal();
			assuntoLocal.setCodigoAssunto(codigoAssuntoCNJ);
			assuntoLocal.setDescricao(vetorPrincipalSPG[EnumDadosProcesso.DESC_ASSUNTO.ordinal()]);			
			
			AssuntoProcessual assuntoProcessual = new AssuntoProcessual();
			assuntoProcessual.setAssuntoLocal(assuntoLocal);
			
			if (codigoAssuntoPaiCNJ > 0)
			{
				AssuntoLocal assuntoLocalPai = new AssuntoLocal();
				assuntoLocalPai.setCodigoAssunto(codigoAssuntoPaiCNJ);				
				assuntoLocalPai.setDescricao(vetorPrincipalSPG[EnumDadosProcesso.DESC_ASSUNTO_PAI.ordinal()]);
				
				assuntoLocal.setAssuntoLocalPai(assuntoLocalPai);	
			}
			
			assuntoProcessual.setCodigoNacional(codigoAssuntoCNJ);
			assuntoProcessual.setPrincipal(true);	
			
			listaDeAssuntos.add(assuntoProcessual);
		}
		
		return listaDeAssuntos;
	}
	
	private List<Parametro> efetueMapeamentoOutrosParametrosSPG(String[] vetorPrincipalSPG) throws Exception
	{
		List<Parametro> listaDeOutrosParametros = new ArrayList<Parametro>();
		
		listaDeOutrosParametros.add(obtenhaParametro("ProcessoFaseCodigo", vetorPrincipalSPG[EnumDadosProcesso.CODG_FASE.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("ProcessoFaseDescricao", vetorPrincipalSPG[EnumDadosProcesso.DESC_FASE.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("ComarcaDescricao", vetorPrincipalSPG[EnumDadosProcesso.DESC_COMARCA.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("ServentiaCodigo", vetorPrincipalSPG[EnumDadosProcesso.CODG_SERVENTIA.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("ServentiaDescricao", vetorPrincipalSPG[EnumDadosProcesso.DESC_SERVENTIA.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("NaturezaCodigo", vetorPrincipalSPG[EnumDadosProcesso.CODG_NATUREZA.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("NaturezaDescricao", vetorPrincipalSPG[EnumDadosProcesso.DESC_NATUREZA.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("DataUltimaMovimentacao", vetorPrincipalSPG[EnumDadosProcesso.DATA_MOVIMENTO.ordinal()]));
		listaDeOutrosParametros.add(obtenhaParametro("HoraUltimaMovimentacao", vetorPrincipalSPG[EnumDadosProcesso.HORA_MOVIMENTO.ordinal()]));		
		listaDeOutrosParametros.add(obtenhaParametro("Sistema", "2"));
		return listaDeOutrosParametros;
	}
	
	private List<PoloProcessual> efetueMapeamentoPolosProcessuaisSPG(String[] vetorPrincipalSPG) throws Exception
	{
		List<PoloProcessual> listaDePolosProcessuais = new ArrayList<PoloProcessual>();
		
		PoloProcessual poloProcessualPromoventes = new PoloProcessual();		
		poloProcessualPromoventes.setPolo(ModalidadePoloProcessual.AT);
		
		PoloProcessual poloProcessualPromovidos = new PoloProcessual();		
		poloProcessualPromovidos.setPolo(ModalidadePoloProcessual.PA);
		
		PoloProcessual poloProcessualOutrasPartes = new PoloProcessual();		
		poloProcessualOutrasPartes.setPolo(ModalidadePoloProcessual.TC);
		
		for (int i = EnumDadosProcesso.INICIO_PARTES.ordinal(); i < vetorPrincipalSPG.length; i++)
		{
			String[] vetorPartesSPG = vetorPrincipalSPG[i].split(SEPARADOR_PARTES);
			if (vetorPartesSPG != null && vetorPartesSPG.length >= EnumDadosParte.QUANTIDADE_MINIMA.ordinal())
			{
				if (vetorPartesSPG[EnumDadosParte.VET_POLO_PARTES2.ordinal()].trim().equalsIgnoreCase(IDENTIFICADOR_POLO_ATIVO))
					poloProcessualPromoventes.getParte().add(obtenhaParteSPG(vetorPartesSPG));
				else if (vetorPartesSPG[EnumDadosParte.VET_POLO_PARTES2.ordinal()].trim().equalsIgnoreCase(IDENTIFICADOR_POLO_PASSIVO))
					poloProcessualPromovidos.getParte().add(obtenhaParteSPG(vetorPartesSPG));
				else if (vetorPartesSPG[EnumDadosParte.VET_POLO_PARTES2.ordinal()].trim().equalsIgnoreCase(IDENTIFICADOR_POLO_OUTROS))
					poloProcessualOutrasPartes.getParte().add(obtenhaParteSPG(vetorPartesSPG));
			}		
		}
		
		if (poloProcessualPromoventes.getParte().size() > 0)
			listaDePolosProcessuais.add(poloProcessualPromoventes);
		
		if (poloProcessualPromovidos.getParte().size() > 0)
			listaDePolosProcessuais.add(poloProcessualPromovidos);
		
		if (poloProcessualOutrasPartes.getParte().size() > 0)
			listaDePolosProcessuais.add(poloProcessualOutrasPartes);
		
		return listaDePolosProcessuais;
	}
	
	private Parte obtenhaParteSPG(String[] vetorPartesSPG) throws Exception
	{	
		Pessoa pessoa = new Pessoa();
		pessoa.setCidadeNatural(vetorPartesSPG[EnumDadosParte.VET_PESSOA_NATURALIDADE2.ordinal()]);
		
		Data dataNascimento = new Data();
		dataNascimento.setValue(vetorPartesSPG[EnumDadosParte.VET_PESSOA_NASCIMENTO2.ordinal()]);
		pessoa.setDataNascimento(dataNascimento);
		
		pessoa.setNome(vetorPartesSPG[EnumDadosParte.VET_PESSOA_NOME2.ordinal()]);
		pessoa.setNomeGenitor(vetorPartesSPG[EnumDadosParte.VET_PESSOA_PAI2.ordinal()]);
		pessoa.setNomeGenitora(vetorPartesSPG[EnumDadosParte.VET_PESSOA_MAE2.ordinal()]);
		
		CadastroIdentificador documentoPrincipal = new CadastroIdentificador();
		documentoPrincipal.setValue(vetorPartesSPG[EnumDadosParte.VET_PESSOA_CPF2.ordinal()]);
		pessoa.setNumeroDocumentoPrincipal(documentoPrincipal);
					
		if (vetorPartesSPG[EnumDadosParte.VET_PESSOA_SEXO2.ordinal()].trim().equalsIgnoreCase("F"))						
			pessoa.setSexo(ModalidadeGeneroPessoa.F);		
		else if (vetorPartesSPG[EnumDadosParte.VET_PESSOA_SEXO2.ordinal()].trim().equalsIgnoreCase("M"))
			pessoa.setSexo(ModalidadeGeneroPessoa.M);			
		else
			pessoa.setSexo(ModalidadeGeneroPessoa.D);
		
		String[] vetorTipoPessoaNumeroRG = vetorPartesSPG[EnumDadosParte.VET_PESSOA_TIPO2.ordinal()].split(SEPARADOR_PARTES_TIPO_RG);
		
		if (vetorTipoPessoaNumeroRG != null)
		{
			if (vetorTipoPessoaNumeroRG.length > EnumDadosParteTipoRG.VET_PESSOA_TIPO2.ordinal())
			{
				if (vetorTipoPessoaNumeroRG[EnumDadosParteTipoRG.VET_PESSOA_TIPO2.ordinal()].trim().equalsIgnoreCase("F"))
					pessoa.setTipoPessoa(TipoQualificacaoPessoa.FISICA);
				else if (vetorTipoPessoaNumeroRG[EnumDadosParteTipoRG.VET_PESSOA_TIPO2.ordinal()].trim().equalsIgnoreCase("J"))
					pessoa.setTipoPessoa(TipoQualificacaoPessoa.JURIDICA);
			}
			
			if (vetorTipoPessoaNumeroRG.length > EnumDadosParteTipoRG.VET_PESSOA_RG2.ordinal())
			{
				DocumentoIdentificacao documentoIdentidade = new DocumentoIdentificacao();
				documentoIdentidade.setTipoDocumento(ModalidadeDocumentoIdentificador.CI);
				documentoIdentidade.setCodigoDocumento(vetorTipoPessoaNumeroRG[EnumDadosParteTipoRG.VET_PESSOA_RG2.ordinal()]);
				documentoIdentidade.setEmissorDocumento(vetorPartesSPG[EnumDadosParte.VET_PESSOA_ORGAO2.ordinal()]);
				documentoIdentidade.setNome("RG");
				pessoa.getDocumento().add(documentoIdentidade);	
			}
		}
		
		Parte parte = new Parte();		
		parte.setPessoa(pessoa);
		
		String[] vetorAdvogados = vetorPartesSPG[EnumDadosParte.INICIO_ADVOGADOS.ordinal()].split(SEPARADOR_ADVOGADOS);
		
		if (vetorAdvogados != null)
		{
			for (int i = 0; i < vetorAdvogados.length; i++)
			{				
				if (vetorAdvogados[i] != null)
				{
					String[] vetorDadosAdvogado = vetorAdvogados[i].split(SEPARADOR_DADOS_ADVOGADOS);
					
					if (vetorDadosAdvogado != null && vetorDadosAdvogado.length >= EnumDadosAdvogado.QUANTIDADE_MINIMA.ordinal())
					{
						RepresentanteProcessual representanteProcessual = new RepresentanteProcessual();
						
						CadastroOAB cadastroOAB = new CadastroOAB();
						cadastroOAB.setValue(vetorDadosAdvogado[EnumDadosAdvogado.VET_ADV_OAB2.ordinal()]);				
						representanteProcessual.setInscricao(cadastroOAB);
						
						representanteProcessual.setNome(vetorDadosAdvogado[EnumDadosAdvogado.VET_ADV_NOME2.ordinal()]);
						representanteProcessual.setNumeroDocumentoPrincipal(vetorDadosAdvogado[EnumDadosAdvogado.VET_ADV_OAB2.ordinal()]);
						representanteProcessual.setTipoRepresentante(ModalidadeRepresentanteProcessual.A);
													
						parte.getAdvogado().add(representanteProcessual);	
					}
				}
			}
		}
		
		return parte;
	}
	
	private List<MovimentacaoProcessual> efetueMapeamentoMovimentacoesProcessuaisSPG(String[] vetorPrincipalSPG, int nivelSigiloProcesso) throws Exception
	{
		List<MovimentacaoProcessual> listaDeMovimentacoesProcessuais = new ArrayList<MovimentacaoProcessual>();
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_01.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_01.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_01.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_02.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_02.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_02.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_03.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_03.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_03.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_04.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_04.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_04.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_05.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_05.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_05.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_06.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_06.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_06.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_07.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_07.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_07.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_08.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_08.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_08.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_09.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_09.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_09.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_10.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_10.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_10.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_11.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_11.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_11.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_12.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_12.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_12.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_13.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_13.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_13.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_14.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_14.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_14.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_15.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_15.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_15.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_16.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_16.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_16.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_17.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_17.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_17.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_18.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_18.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_18.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_19.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_19.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_19.ordinal()], nivelSigiloProcesso));
		
		if (vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_20.ordinal()] != null && vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_20.ordinal()].trim().length() > 0)
			listaDeMovimentacoesProcessuais.add(efetueMapeamentoMovimentacaoProcessualSPG(vetorPrincipalSPG[EnumDadosProcesso.INFO_MOVIMENTO_20.ordinal()], nivelSigiloProcesso));		
		
		return listaDeMovimentacoesProcessuais;
	}
	
	private MovimentacaoProcessual efetueMapeamentoMovimentacaoProcessualSPG(String movimentacao, int nivelSigiloProcesso)
	{
		MovimentacaoProcessual movimentacaoProcessual = new MovimentacaoProcessual();	  		
  		movimentacaoProcessual.setNivelSigilo(nivelSigiloProcesso);	
  		movimentacaoProcessual.setMovimentoLocal(efetueMapeamentoMovimentoLocal(0, movimentacao));
  		return movimentacaoProcessual;
	}	
	
	private MovimentoLocal efetueMapeamentoMovimentoLocal(int codigoMovimento, String movimentacao) {
		MovimentoLocal movimentoLocal = new MovimentoLocal();
		
		movimentoLocal.setCodigoMovimento(codigoMovimento);
		movimentoLocal.setDescricao(movimentacao);
		
		return movimentoLocal;
	}	
}
