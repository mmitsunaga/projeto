package br.gov.go.tj.projudi.ne;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ArquivoBancoDt;
import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.BairroGuiaLocomocaoDt;
import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.FinanceiroConsultarGuiasDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoSajaDocumentoDt;
import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloCustaDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ImportaDadosSPGDt;
import br.gov.go.tj.projudi.dt.InfoRepasseSPGDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LocomocaoSPGDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.NaturezaSPGDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.PropriedadeDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.SajaDocumentoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VincularGuiaComplementarProcessoDt;
import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ps.GuiaEmissaoPs;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.bancos.GerenciaArquivo;
import br.gov.go.tj.utils.bancos.GerenciaArquivoCEF;
import br.gov.go.tj.utils.bancos.LeiauteEscritaArquivoCEF;
import br.gov.go.tj.utils.bancos.LeiauteLeituraArquivoCEF;
import br.gov.go.tj.utils.pdf.InterfaceJasper;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

public class GuiaEmissaoNe extends GuiaEmissaoNeGen {
	
	private static final long serialVersionUID = 2545601290803212308L;
	
	private static transient int contador_erro_adabas = 0;
	
	// Bancos 
	private GuiaCalculoNe guiaCalculoNe;
	private CustaNe custaNe;
	private GuiaTipoNe guiaTipoNe;
	private GuiaItemNe guiaItemNe;
	private ServentiaNe serventiaNe;
	private ComarcaNe comarcaNe;
	private NaturezaSPGNe naturezaSPGNe;
	private ProcessoNe processoNe;
	private ProcessoTipoNe processoTipoNe;
	private BairroNe bairroNe;
	private AreaDistribuicaoNe areaDistribuicaoNe;
	private String custasQuantitativas;
	public static final String NUMERO_SERIE_GUIA = "50";

	//private static final String CAMINHO_ARQUIVOS_BB = "D:/fred/teste/bancos/"; //TESTE
//	private static final String CAMINHO_ARQUIVOS_BB 			= File.separator + "opt" + File.separator + "bancos"  + File.separator + "bb"  + File.separator + "retorno"  + File.separator;
//	private static final String CAMINHO_ARQUIVOS_ITAU 			= File.separator + "opt" + File.separator + "bancos"  + File.separator + "itau"  + File.separator + "retorno"  + File.separator;
	private static final String CAMINHO_ARQUIVOS_CAIXA 			= File.separator + "opt" + File.separator + "bancos"  + File.separator + "caixa"  + File.separator + "inbox"  + File.separator;
	//private static final String CAMINHO_ARQUIVOS_CAIXA 			= "D:/ProjetosTJGO/RetornoCEF/";
	private static final String CAMINHO_ARQUIVOS_CONSOLIDADOS 	= File.separator + "opt" + File.separator + "bancos"  + File.separator + "consolidados"  + File.separator;
	
	public static final String LABEL_QUANTIDADE_DEPOSITARIO_ANOS 	= "(ano)";
	public static final String LABEL_QUANTIDADE_DEPOSITARIO_MESES 	= "(meses)";
	
	public static final String APRESENTAR_BOTAO_CANCELAR_GUIA 							= "_0x1";
	public static final String APRESENTAR_BOTAO_VOLTAR 									= "_2w0";
	public static final String APRESENTAR_BOTAO_IMPRIMIR 								= "_3z1";
	public static final String APRESENTAR_LINK_EMITIR_GUIA_LOC_COMPLEMENTAR 			= "_0x34";
	public static final String APRESENTAR_LINK_EMITIR_GUIA_INICIAL_LOC_COMPLEMENTAR 	= "_0x99";

	// Variï¿½veis para os Rateios
	public static final String VARIAVEL_RATEIO_PARTE_VARIAVEL 	= "rateioParteVariavel";
	public static final String VARIAVEL_EMITIR_GUIA_PARTE 		= "emitirGuiaParte";
	
	public static final String VARIAVEL_PERMITE_LOCOMOCAO 		= "permiteLocomocao";

	public static final int BENS_PARTILHAR_SIM = 1;
	public static final int BENS_PARTILHAR_NAO = 0;
	
	public static final int PENHORA_SIM = 1;
	public static final int PENHORA_NAO = 0;
	
	public static final int VALOR_MANUAL_SIM = 2;
	public static final int PROTOCOLO_INTEGRADO_SIM = 1;
	public static final int PROTOCOLO_INTEGRADO_NAO = 0;
	
	public static final int SIM_EXECUCAO_RECAI_BENS = 1; //Execuï¿½ï¿½o recai sobre bens que devam ser penhorados, avaliados e alienados
	public static final int NAO_EXECUCAO_RECAI_BENS = 0;
	
	public static final int SIM_BENS_PENHORADOS_AVALIADOS_ALIENADOS = 1;
	public static final int NAO_BENS_PENHORADOS_AVALIADOS_ALIENADOS = 0;
	
	public static final int PEDIDO_RESTITUICAO_MERCADORIAS 	= 0;
	public static final int IMPUGNACOES_CREDITO 			= 1;
	public static final int PROCESSOS_EXTINCAO 				= 2;
	
	public static final int INVENTARIOS 		= 0;
	public static final int ARROLAMENTOS 		= 1;
	public static final int SOBREPARTILHA 		= 2;
	
	public static final int LOCOMOCAO 								= 40;
	public static final int PENHORA_AVALIACAO_ALIENACAO 			= 41;
	public static final int CITACAO_PENHORA_AVALIACAO_E_ALIENACAO 	= 44;
	public static final int CITACAO_PENHORA_E_PRACA_LEILAO 			= 45;
	public static final int CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO 	= 46;
	public static final int LOCOMOCAO_AVALIACAO                     = 8;
	public static final int LOCOMOCAO_AVALIACAO_PRACA               = 9;
	
	// Tipos de Rateios
	public static final int RATEIO_100_REQUERENTE 	= 101;
	public static final int RATEIO_100_REQUERIDO 	= 202;
	public static final int RATEIO_50_50 			= 303;
	public static final int RATEIO_VARIAVEL 		= 404;
	
	// Atos dos Escrivães
	public static final String EXECUCAO_FISCAL 						= "101";
	public static final String PROCESSOS_CAUTELARES 				= "202";
	public static final String DEMAIS_PROCESSOS 					= "303";
	public static final String ATO_ESCRIVAO_CONTENCIOSO_70_PORCENTO = "404";
	public static final String CARTA_ORDEM 							= "505";
	
	// CONSTANTES DE NATUREZA
	public static final String ALIMENTOS 							= "ALIMENTOS";
	public static final String ALIMENTOS_PROVISIONAIS 				= "ALIMENTOS PROVISIONAIS";
	public static final String CANCELAMENTO_PENSAO_ALIMENTICIA 		= "CANCELAMENTO DE PENSAO ALIMENTICIA";
	public static final String REVISIONAL_PENSAO 					= "REVISIONAL DE PENSAO";
	public static final String REVISIONAL_ALIMENTOS 				= "REVISIONAL DE ALIMENTOS";
	public static final String CONVERSAO_CONSENSUAL 				= "CONSENSUAL";
	public static final String CONVERSAO_LITIGIOSA 					= "LITIGIOSA";
	
	
	// Quantidade máxima de parcelas na funcionalidade de parcelamento
	public static final int QUANTIDADE_MINIMA_PARCELAS = 2;
	//http://www.gabinetecivil.goias.gov.br/leis_ordinarias/2002/lei_14376.htm
	//Alterado após conversa com o contador Luiz Carlos
	//LEI Nº 14.376
	//Acrescido pela pela Lei nº 19.931, de 29-12-2017.
	//Art. 38-B. As custas iniciais podem ser parceladas em até 05 (cinco) vezes, por decisão do juiz competente para conhecer do pedido.
	//Ocorrência 2019/4408 e 4409 : alterado para máximo de parcelas 6 temporariamente
	public static final int QUANTIDADE_MAXIMA_PARCELAS = 5;
	
	public static int getQuantidadeMaximaParcelas(String idGuiaEmis) throws Exception {
		int quantidadeMaximaParcelas = GuiaEmissaoNe.QUANTIDADE_MAXIMA_PARCELAS;
		
		GuiaQuantidadeMaximaParcelasNe guiaQuantidadeMaximaParcelasNe = new GuiaQuantidadeMaximaParcelasNe();
		
		String valorMaximo = guiaQuantidadeMaximaParcelasNe.consultaQuantidadeMaximaParcelamento(idGuiaEmis);
		
		if( valorMaximo != null ) {
			quantidadeMaximaParcelas = Funcoes.StringToInt(valorMaximo);
		}
		
		return quantidadeMaximaParcelas;
	}

	
	@Override
    public String Verificar(GuiaEmissaoDt dados) {
        
        return null;
    }
	
	/**
	 * Mï¿½todo para setar a quantidade para custas que utilizam quantidades como quantidades de pï¿½ginas ou KM.
	 * @param String custasQuantitativas
	 */
	public void setCustasQuantitativas(String custasQuantitativas) {
		this.custasQuantitativas = custasQuantitativas;
	}
	
	/**
	 * Mï¿½todo para setar o valor dos Bens para custas que utilizam o valor do bem como parï¿½metro.
	 * @param String custasValorBens
	 */
	public void setCustasValorBens(String custasValorBens) {
	}
	
	/**
	 * Mï¿½todo para obter o GuiaCalculoNe.
	 * @return GuiaCalculoNe guiaCalculoNe
	 */
	public GuiaCalculoNe getGuiaCalculoNe() {
		if(this.guiaCalculoNe == null) 
			this.guiaCalculoNe = new GuiaCalculoNe();
		return this.guiaCalculoNe;
	}
	
	/**
	 * Mï¿½todo para retornar boolean que defini se botï¿½o de impressï¿½o e de pagamento deve aparecer ou nï¿½o.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return Boolean
	 */
	public Boolean visualizarBotaoImpressaoBotaoPagamento(GuiaEmissaoDt guiaEmissaoDt) {
		Boolean retorno = false;
		
		if( guiaEmissaoDt != null ) {
			if( guiaEmissaoDt.getId_GuiaStatus() != null && 
				(guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) ||
				 guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)))) {
				retorno = true;
			}
			//Caso seja guia inicial
			if( guiaEmissaoDt.getId_GuiaStatus() == null ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Verifica se processo deve cobrar as locomoï¿½ï¿½es em dobro.
	 * @return
	 * @throws Exception
	 */
	public Boolean existeLocomocaoDobro(String processoTipoCodigo) throws Exception {
		Boolean retorno = false;
		

		if( processoTipoCodigo != null && processoTipoCodigo.length() > 0 ) {
			switch(Funcoes.StringToInt(processoTipoCodigo)) {
				case ProcessoTipoDt.SEQUESTRO_CPC :
				case ProcessoTipoDt.SEQUESTRO_CPP :
				case ProcessoTipoDt.DESPEJO_FALTA_PAGAMENTO :
				case ProcessoTipoDt.DESPEJO_FALTA_PAGAMENTO_LE :
				case ProcessoTipoDt.DESPEJO_FALTA_PAGAMENTO_CUMULADO_LE :
				case ProcessoTipoDt.DESPEJO :
				case ProcessoTipoDt.DESPEJO_PEDIDO_LIMINAR :
				case ProcessoTipoDt.DESPEJO_LE :
				case ProcessoTipoDt.IMISSAO_DE_POSSE :
				case ProcessoTipoDt.IMISSAO_NA_POSSE_LE :
				case ProcessoTipoDt.REINTEGRACAO_DE_POSSE :
				case ProcessoTipoDt.REINTEGRACAO_MANUTENCAO_DE_POSSE_CPC :
				case ProcessoTipoDt.ARRESTO :
				case ProcessoTipoDt.ARRESTO_CPC :
				case ProcessoTipoDt.ARRESTO_HIPOTECA_LEGAL : {
					
					retorno = true;
					
					break;
				}
			}
		}
	
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para recalcular o total da guia atravï¿½s da lista de guiaItemDt.
	 * @param List<GuiaItemDt> listaGuiaItemDt
	 * @throws Exception
	 */
	public void recalcularTotalGuia(List listaGuiaItemDt) throws Exception {
		this.getGuiaCalculoNe().recalcularTotalGuia(listaGuiaItemDt);
	}
	
	/**
	 * Mï¿½todo para verificar se o total da guia estï¿½ zerada ou inferior a zero.
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaZeradaOuNegativa() throws Exception {
		boolean retorno = false;
		
		retorno = this.getGuiaCalculoNe().isGuiaZeradaOuNegativa();
		
		return retorno;
	}
	
	public boolean isGuiaZeradaOuNegativa(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		return this.getGuiaCalculoNe().isGuiaZeradaOuNegativa(listaGuiaItemDt);
	}
	
	/**
	 * Recalcular os itens da guia complementar.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaItemDt
	 * @param List listaGuiaItemDtLocomocao
	 * 
	 * @throws Exception
	 */
	public void recalcularGuiaComplementar(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, List listaGuiaItemDtLocomocao) throws Exception {
		this.getGuiaCalculoNe().recalcularGuiaComplementar(guiaEmissaoDt, listaGuiaItemDt, listaGuiaItemDtLocomocao);
	}
	
	/**
	 * Recalcular os itens da guia complementar final.
	 * 
	 * @param List listaGuiaItemDtAtual //itens calculados agora
	 * @param List listaGuiaItemDtPagas //itens pagos
	 * 
	 * @throws Exception
	 * @author Fred
	 */
	public List recalcularGuiaComplementarFinal(List listaGuiaItemDtAtual, List listaGuiaItemDtPagas) throws Exception {
		listaGuiaItemDtAtual = this.getGuiaCalculoNe().recalcularGuiaComplementar(listaGuiaItemDtAtual, listaGuiaItemDtPagas);		
		return listaGuiaItemDtAtual;
	}
	
	/**
	 * Mï¿½todo para calcular o total da guia.
	 * @param List listaGuiaItemDt
	 * @return Double
	 * @throws Exception
	 */
	public Double calcularTotalGuia(List listaGuiaItemDt) throws Exception {
		Double retorno = 0.0D;

		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			retorno = this.getGuiaCalculoNe().calcularTotalGuia(listaGuiaItemDt);
		}
			
		return retorno;
	}
	
//	/**
//	 * Mï¿½todo para consultar o total da guia.
//	 * @param String numeroGuiaCompleto
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double consultarTotalGuiaBancoDados(String numeroGuiaCompleto) throws Exception {
//		Double retorno = 0.0D;
//		FabricaConexao obFabricaConexao = null;
//		
//		try {
//			if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 ) {
//				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
//				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
//				
//				retorno = obPersistencia.consultarTotalGuia(numeroGuiaCompleto);
//			}
//		} finally {
//			if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 ) {
//				obFabricaConexao.fecharConexao();
//			}
//		}
//		
//		return retorno;
//	}
//	
//	/**
//	 * Mï¿½todo para consultar o total da guia.
//	 * @param String numeroGuiaCompleto
//	 * @param FabricaConexao obFabricaConexao
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double consultarTotalGuiaBancoDados(String numeroGuiaCompleto, FabricaConexao fabConexao) throws Exception {
//		Double retorno = 0.0D;
//		FabricaConexao obFabricaConexao = null;
//		
//		try {
//			if (fabConexao == null) {
//				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
//			}
//			else {
//				//Caso da conexao criada em um nivel superior
//				obFabricaConexao = fabConexao;
//			}
//
//			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
//			
//			retorno = obPersistencia.consultarTotalGuia(numeroGuiaCompleto);
//		} finally {
//			if( fabConexao == null ) {
//				obFabricaConexao.fecharConexao();
//			}
//		}
//		
//		return retorno;
//	}
	
	/**
	 * Mï¿½todo para obter da parte de uma lista de partes ( promoventes + promovidos )
	 * @param String idProcessoParte
	 * @param List listaProcessoParteDt
	 * @return String
	 * @throws Exception
	 */
	public String getNomeParte(String idProcessoParte, List listaProcessoParteDt) throws Exception {
		String retorno = "";
		

		for(int i = 0; i < listaProcessoParteDt.size(); i++) {
			ProcessoParteDt parteDt = (ProcessoParteDt)listaProcessoParteDt.get(i);
			
			if( parteDt.getId().equals(idProcessoParte) ) {
				retorno = parteDt.getNome();
				break;
			}
		}
	
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para gerar o nï¿½mero da guia completo.
	 * @param numeroGuia
	 * @return
	 * @throws Exception 
	 */
	public String getNumeroGuiaCompleto(String numeroGuia) throws Exception {
		
		String digitoNumeroGuia = this.algoritmoDigitoVerificadorNumeroGuia(numeroGuia);
		
		return Funcoes.completarZeros(numeroGuia + digitoNumeroGuia + NUMERO_SERIE_GUIA, 11);
	}
	
	/**
	 * Mï¿½todo com o algoritmo do dï¿½gito verificador do nï¿½mero da guia.
	 * @param String numeroGuia
	 * @return String digito
	 * @throws Exception
	 */
	public String algoritmoDigitoVerificadorNumeroGuia(String numeroGuia) throws Exception {
		String digito = "";
		
		numeroGuia = Funcoes.completarZeros(numeroGuia, 8);
		

		if( numeroGuia.length() <= 8 ) {
			int soma = 0;
			for(int i = 1; i <= numeroGuia.length(); i++) {
				soma += Funcoes.StringToInt(String.valueOf(numeroGuia.charAt(i-1))) * (10 - i);
			}
			
			int modulo = soma - (Math.abs(soma / 11) * 11 );
			
			if( modulo > 1 ) {
				modulo = 11 - modulo;
			}
			else {
				if( modulo == 1 )
					modulo = 1;
				else {
					if( modulo == 0 )
						modulo = 0;
				}
			}
			
			digito = String.valueOf(modulo);
		}
		else
			throw new MensagemException("Número da Guia possui mais de 8 posições.");
	
		
		return digito;
	}
	
	/**
	 * Mï¿½todo para gerar o cï¿½digo de Barra da Guia.
	 * @param String totalGuia
	 * @param String numeroGuia
	 * @param String dataVencimento
	 * @param String tipoGuia
	 * @return String codigoBarra
	 * @throws Exception
	 */
	protected String gerarCodigoBarra(String totalGuia, String numeroGuia, String dataVencimento, String tipoGuia) throws Exception {
		String codigoBarra = "";

		//Tratamentos necessï¿½rios
		totalGuia = Funcoes.retiraVirgulaPonto(totalGuia);
		
		if( numeroGuia.length() < 11 ) {
			numeroGuia = Funcoes.completarZeros(numeroGuia, 11);
		}
		
		//Layout Barra de Cï¿½digo
		String ARRECADACAO 			= "8"; 
		String SEGMENTO 			= "5"; //Orgï¿½o Governamental
		String TIPO_MOEDA 			= "6"; //Valor na moeda Real BR
		String DIGITO_VERIFICADOR 	= null; //Dï¿½gito verificador que serï¿½ gerado apartir dos 43 dï¿½gitos do cï¿½digo de barra para compor os 44 dï¿½gitos
		String VALOR_GUIA 			= Funcoes.completarZeros(totalGuia, 11); //Tem que ter 11 dï¿½gitos
		String FEBRABAN_TRIBUNAL 	= "0143"; //Cï¿½digo Febraban(FEDERAï¿½ï¿½O DOS BANCOS)
		String NUMERO_GUIA 			= numeroGuia;
		String DATA_VENCIMENTO 		= Funcoes.FormatarDataCodigoBarraGuia(dataVencimento); //Data vencimento no seguinte formato(sem separador): 20101231
		String VAGO 				= "0000"; //4(Quatro) posiï¿½ï¿½es vagas
		//Tipo de Guia deve ter 2 posiï¿½ï¿½es
		String TIPO_GUIA 			= "";
		
		if( tipoGuia.trim().length() == 1 ) {
			TIPO_GUIA = Funcoes.completarZeros(tipoGuia.trim(),2);
		}
		else {
			if( tipoGuia.trim().length() == 2 ) {
				TIPO_GUIA = tipoGuia.trim();
			}
			else {
				TIPO_GUIA = "00";
			}
		}
		
		codigoBarra = ARRECADACAO + SEGMENTO + TIPO_MOEDA + VALOR_GUIA + FEBRABAN_TRIBUNAL + NUMERO_GUIA + DATA_VENCIMENTO + VAGO + TIPO_GUIA;
		
		DIGITO_VERIFICADOR = this.gerarDigitoCodigoBarra(codigoBarra);
		
		//Inseri o Dï¿½gito verificador na sequencia de caracteres do cï¿½digo de barra
		codigoBarra = codigoBarra.substring(0, 3) + DIGITO_VERIFICADOR + codigoBarra.substring(3);
		
		//Cï¿½digo de barra deve ter 44 dï¿½gitos(sem os 4 dï¿½gitos verificadores de cada 11 da sequencia)
		if( codigoBarra.length() != 44 ) {
			throw new MensagemException("Erro ao Gerar Sequência para a Barra de Código da Guia. Quantidade de Dígitos Errado.");
		}
	
		
		return codigoBarra;
	}
	
	/**
	 * Mï¿½todo que executa algoritmo para gerar dï¿½gitos do cï¿½digo de barra.
	 * @return String codigoBarra
	 * @throws Exception
	 */
	protected String gerarDigitoCodigoBarra(String codigoBarra) throws Exception {
		String digito = null;
		

		//Algoritmo de verificaï¿½ï¿½o do dï¿½gito para a sequï¿½ncia do cï¿½digo de barra passada como parï¿½metro.
		int x = 0;
		int multiplicacao = 0;
		int acumulador = 0;
		
		for(int i = 0; i < codigoBarra.length(); i++) {
			if( x == 0 )
				x = 2;
			multiplicacao = Funcoes.StringToInt(codigoBarra.substring(i, (i+1))) * x;
			x--;
			
			if( multiplicacao > 9 ) 
				acumulador += 1 + (multiplicacao - 10);
			else 
				acumulador += multiplicacao;
		}
		
		int resto = acumulador % 10;
		int resultado = 10 - resto;
		
		digito = String.valueOf(Math.abs(resultado));
		
		if ( resultado >= 10 ) {
			digito = "0";
		}
	
		
		return digito;
	}
	
	/**
	 * Mï¿½todo para extrair o nï¿½mero da guia do cï¿½digo de barra passado como parï¿½metro.
	 * @param String codigoBarra
	 * @return String numeroGuia
	 * @throws Exception
	 */
	public String extrairNumeroGuiaCodigoBarra(String codigoBarra) throws Exception {
		String numeroGuia = null;
	
		if( codigoBarra.length() == 44 ) {
			numeroGuia = codigoBarra.substring(19,30);
		}
	
		return numeroGuia;
	}
	
	/**
	 * Método que verifica se tem Guia emitida para este Processo do tipo GuiaTipo.
	 * @return boolean
	 * @throws Exception
	 */
	public boolean possuiGuiaEmitida(String idProcesso, String idGuiaTipo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && idGuiaTipo != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				retorno = obPersistencia.possuiGuiaEmitida(idProcesso, idGuiaTipo);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que verifica se guia está paga pelo nï¿½mero da Guia.
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(String numeroGuia) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			//Valida se numeroGuia está preenchido
			if( numeroGuia != null && numeroGuia.length() > 0 ) {
				retorno = obPersistencia.isGuiaPaga(numeroGuia);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que verifica se guia está paga pelo nï¿½mero da Guia.
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(String numeroGuia, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		//Valida se numeroGuia está preenchido
		if( numeroGuia != null && numeroGuia.length() > 0 ) {
			retorno = obPersistencia.isGuiaPaga(numeroGuia);
		}
		
		return retorno;
	}
	
	
	/**
	 * Mï¿½todo que verifica se guia estï¿½ paga pelo nï¿½mero da Guia.
	 * @param String numeroGuia
	 * @param FabricaConexao fabConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaRessarcidoPedidoRessarcido(String numeroGuia, FabricaConexao fabConexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		try {
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			} 
			else {
				//Caso da conexao criada em um nivel superior
				obFabricaConexao = fabConexao;
			}			
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			//Valida se numeroGuia estï¿½ preenchido
			if( numeroGuia != null && numeroGuia.length() > 0 ) {
				retorno = obPersistencia.isGuiaRessarcidoPedidoRessarcido(numeroGuia);
			}
		} finally {
			if( fabConexao == null ) {
				if( obFabricaConexao != null ) {
					obFabricaConexao.fecharConexao();
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que verifica se guia estï¿½ paga pelo ID da Guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( guiaEmissaoDt != null ) {
				retorno = obPersistencia.isGuiaPaga(guiaEmissaoDt);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que verifica se guia estï¿½ paga pelo ID da Guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaPaga(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		boolean retorno = false;
		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		if( guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && !guiaEmissaoDt.getId().isEmpty() ) {
			retorno = obPersistencia.isGuiaPaga(guiaEmissaoDt);
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para verificar se a guia estï¿½ com o status de AGUARDANDO PAGAMENTO.
	 * Retorno true se sim e false de nï¿½o.
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaStatusAguardandoPagamento(String numeroGuia) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isGuiaStatusAguardandoPagamento(numeroGuia);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para verificar se a guia estï¿½ com o status de AGUARDANDO PAGAMENTO.
	 * Retorno true se sim e false de nï¿½o.
	 * @param String numeroGuia
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaStatusAguardandoPagamento(String numeroGuia, FabricaConexao fabConexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			} 
			else {
				//Caso da conexao criada em um nivel superior
				obFabricaConexao = fabConexao;
			}
			
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isGuiaStatusAguardandoPagamento(numeroGuia);
		} finally{
			if( fabConexao == null ) {
				if( obFabricaConexao != null ) {
					obFabricaConexao.fecharConexao();
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar ServentiaDt pelo Cï¿½digo da Serventia.
	 * @param String serventiaCodigo
	 * @return ServentiaDt
	 * @throws Exception
	 */
	public ServentiaDt consultarServentiaProcesso(String serventiaCodigo) throws Exception {
		if( serventiaNe == null ) {
			serventiaNe = new ServentiaNe();
		}
		
		return serventiaNe.consultarServentiaCodigo(serventiaCodigo);
	}
	
	/**
	 * Mï¿½todo para consultar as partes litisconsorte do processo.
	 * @param String id_processo
	 * @return List
	 * @throws Exception
	 */
	public List consultarPartesLitisconsorteAtivoPassivo(String id_processo) throws Exception {
		List retorno = null;

		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		
		retorno = processoParteNe.consultarLitisconsorteAtivo(id_processo);
		List litisconsortePassivo = processoParteNe.consultarLitisconsortePassivo(id_processo);
		
		if( retorno != null ) {
			retorno.addAll(litisconsortePassivo);
		}
		else {
			retorno = litisconsortePassivo;
		}
	
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar ComarcaDt pelo id.
	 * @param String id_comarca
	 * @return ComarcaDt
	 * @throws Exception
	 */
	public ComarcaDt consultarComarca(String id_comarca) throws Exception {
		if( comarcaNe == null ) {
			comarcaNe = new ComarcaNe();
		}
		
		return comarcaNe.consultarId(id_comarca);
	}
	
	/**
	 * Método para consultar NaturezaSPGDt pelo id.
	 * @param String id
	 * @return NaturezaSPGDt
	 * @throws Exception
	 */
	public NaturezaSPGDt consultarNaturezaSPG(String id) throws Exception {
		if( naturezaSPGNe == null ) {
			naturezaSPGNe = new NaturezaSPGNe();
		}
		
		return naturezaSPGNe.consultarId(id);
	}
	
	/**
	 * Mï¿½todo para consultar bairros.
	 * @param tempNomeBusca
	 * @param PosicaoPaginaAtual
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricaoBairro(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		if( bairroNe == null ) {
			bairroNe = new BairroNe();
		}
		
		return bairroNe.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
	}
	
	/**
	 * Mï¿½todo para consultar bairros.
	 * @param tempNomeBusca
	 * @param PosicaoPaginaAtual
	 * @return List
	 * @throws Exception
	 */
	public String consultarDescricaoBairroJSON(String tempNomeBusca, String tempCidade, String tempUf, String PosicaoPaginaAtual) throws Exception {
		if( bairroNe == null ) {
			bairroNe = new BairroNe();
		}
		
		return bairroNe.consultarDescricaoLocomocaoJSON(tempNomeBusca, tempCidade, tempUf, PosicaoPaginaAtual);
	}
	
	/**
	 * Mï¿½todo que retorna a quantidade de pï¿½ginas de bairros.
	 * @return long
	 */
	public long getQuantidadePaginasBairro() {
		return bairroNe.getQuantidadePaginas();
	}
	
	/**
	 * Mï¿½todo para consultar custas.
	 * @param String tempNomeBusca
	 * @param String PosicaoPaginaAtual
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricaoCusta(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		if( custaNe == null )
			custaNe = new CustaNe();
		
		return custaNe.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
	}
	
	/**
	 * Mï¿½todo para consultar data de vencimento da guia.
	 * @param String numeroGuiaCompleto
	 * @return Date
	 * @throws Exception
	 */
	public Date consultarDataVencimento(String numeroGuiaCompleto) throws Exception {
		Date retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( numeroGuiaCompleto != null ) {
				retorno = obPersistencia.consultarDataVencimento(numeroGuiaCompleto);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo que retorna a quantidade de pï¿½ginas de custas.
	 * @return long
	 */
	public long getQuantidadePaginasCusta() {
		return custaNe.getQuantidadePaginas();
	}
	
	/**
	 * Mï¿½todo para consultar no CustaNe o CustaDt pelo Id.
	 * @param String id_custa
	 * @return CustaDt
	 * @throws Exception
	 */
	public CustaDt consultarCustaDtPorId(String id_custa) throws Exception {
		if( custaNe == null )
			custaNe = new CustaNe();
		
		return custaNe.consultarId(id_custa);
	}
	
	/**
	 * Mï¿½todo para retornar uma lista de id de guia tipo.
	 * @return
	 * @throws Exception
	 */
	public List consultarListaId_GuiaTipo(String itemExcluir) throws Exception {
		List listaId_GuiaTipo = null;
		
		if( itemExcluir != null && itemExcluir.length() > 0 ) {
			if( guiaTipoNe == null ) {
				guiaTipoNe = new GuiaTipoNe();
			}
			
			listaId_GuiaTipo = guiaTipoNe.consultarListaId_GuiaTipo(itemExcluir);
		}
		
		return listaId_GuiaTipo;
	}
	
	/**
	 * Mï¿½todo de Consulta da GuiaEmissao pelo seu Id.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarId(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idGuiaEmissao != null && idGuiaEmissao.length() > 0 ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				guiaEmissaoDt = obPersistencia.consultarId(idGuiaEmissao);
			}
		} finally {
			if( idGuiaEmissao != null && idGuiaEmissao.length() > 0 ) {
				if( obFabricaConexao != null ) {
					obFabricaConexao.fecharConexao();
				}
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo de Consulta da GuiaEmissao pelo seu Id.
	 * @param String idGuiaEmissao
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarId(String idGuiaEmissao, FabricaConexao obFabricaConexao) throws Exception {
		if( idGuiaEmissao != null && idGuiaEmissao.length() > 0 ) {
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarId(idGuiaEmissao);
		}
		return null;
	}
	
	/**
	 * Mï¿½todo para consultar guias emitidas pelo usuï¿½rio.
	 * @param String Id_Usuario
	 * @param List<GuiaTipoDt>
	 * @return List<GuiaEmissaoDt>
	 * @throws Excetption
	 */
	public List consultarGuiasIdUsuario(String Id_Usuario, List listaGuiaTipoDt) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiasIdUsuario(Id_Usuario, listaGuiaTipoDt);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar guia para a diretoria financeira.
	 * 
	 * @param FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaEmissao(FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());

			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissao(financeiroConsultarGuiasDt);
			
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar guia para a diretoria financeira.
	 * 
	 * @param FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt
	 * @return String
	 * @throws Exception
	 */
	public String consultarGuiaEmissaoJSON(FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		if ((financeiroConsultarGuiasDt.getNumeroGuiaCompleto() == null || financeiroConsultarGuiasDt.getNumeroGuiaCompleto().trim().length() == 0) &&
			(financeiroConsultarGuiasDt.getNumeroProcesso() == null || financeiroConsultarGuiasDt.getNumeroProcesso().trim().length() == 0) &&
			(financeiroConsultarGuiasDt.getDataInicioEmissao() == null || financeiroConsultarGuiasDt.getDataInicioEmissao().trim().length() == 0 ||
			 financeiroConsultarGuiasDt.getDataFimEmissao() == null || financeiroConsultarGuiasDt.getDataFimEmissao().trim().length() == 0) &&
			(financeiroConsultarGuiasDt.getDataInicioRecebimento() == null || financeiroConsultarGuiasDt.getDataInicioRecebimento().trim().length() == 0 ||
			 financeiroConsultarGuiasDt.getDataFimRecebimento() == null || financeiroConsultarGuiasDt.getDataFimRecebimento().trim().length() == 0) &&
			(financeiroConsultarGuiasDt.getDataInicioCancelamento() == null || financeiroConsultarGuiasDt.getDataInicioCancelamento().trim().length() == 0 ||
			 financeiroConsultarGuiasDt.getDataFimCancelamento() == null || financeiroConsultarGuiasDt.getDataFimCancelamento().trim().length() == 0) &&
			(financeiroConsultarGuiasDt.getDataInicioCertidao() == null || financeiroConsultarGuiasDt.getDataInicioCertidao().trim().length() == 0 ||
			 financeiroConsultarGuiasDt.getDataFimCertidao() == null || financeiroConsultarGuiasDt.getDataFimCertidao().trim().length() == 0)) {
			throw new MensagemException("Pelo menos um dos filtros abaixo deve ser informado:<br/>Número da Guia<br/>Número Processo<br/>Data de Emissão (início e fim)<br/>Data de Recebimento (início e fim)<br/>Data de Cancelamento (início e fim)");
		}	
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());

			stTemp = obPersistencia.consultarGuiaEmissaoJSON(financeiroConsultarGuiasDt, PosicaoPaginaAtual);
			
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return stTemp;
	}
	
	/**
	 * Método para consultar as guias iniciais, complementares de 1º grau e finais pagas na transação.
	 * 
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoInicial_ComplementarQualquerStatus(FabricaConexao obFabricaConexao, String idProcesso) throws Exception {
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		return obPersistencia.consultarGuiaEmissaoInicial_ComplementarQualquerStatus(idProcesso);
	}
	
	/**
	 * Método para consultar as guias iniciais e complementares qualquer status.
	 * 
	 * @param String idProcesso
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoInicial_ComplementarQualquerStatus(String idProcesso) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicial_ComplementarQualquerStatus(idProcesso);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais e complemetares pagas.
	 * 
	 * @param String idProcesso
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoInicial_Complementar(String idProcesso) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicial_Complementar(idProcesso);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais paga.
	 * 
	 * @param String idProcesso
	 * @return GuiaEmissaoDt guia inicial paga do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicial(String idProcesso) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicial(idProcesso);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Verifica se tem guia no processo que não seja série 50. Guias importadas é o caso de retornar true.
	 * 
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean processoGuiaNaoEmitidaPJD(String idProcesso) throws Exception {
		boolean retorno = false;
		
		if( idProcesso != null && !idProcesso.isEmpty() ) {
			
			List listaGuiaEmissaoDtProcesso = this.consultarGuiaEmissao(null, idProcesso, null);
			
			if( !listaGuiaEmissaoDtProcesso.isEmpty() ) {
				for( int i = 0; i < listaGuiaEmissaoDtProcesso.size(); i++ ) {
					GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDtProcesso.get(i);
					
					if( guiaEmissaoDt.getNumeroGuiaCompleto() != null ) {
						if( !this.isNumeroGuiaProjudi(guiaEmissaoDt.getNumeroGuiaCompleto()) ) {
							retorno = true;
							break;
						}
					}
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar GuiaEmissao pelo id.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissao(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissao(idGuiaEmissao);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar GuiaEmissao pelo id que está aguardando pagamento.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoAguardandoPagamento(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoAguardandoPagamento(idGuiaEmissao);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo id que está aguardando pagamento e não vencida.
	 * @param String idGuiaEmissao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoAguardandoPagamentoNaoVencida(String idGuiaEmissao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idGuiaEmissao != null && !idGuiaEmissao.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(idGuiaEmissao);
			}
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Consulta guia pelo nï¿½mero da guia.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoNumeroGuia(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		return guiaEmissaoDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoNumeroGuiaNumeroProcesso(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
				
				if (guiaEmissaoDt != null && guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
					guiaEmissaoDt.setNumeroCompletoProcesso(this.consultarNumeroCompletoDoProcesso(guiaEmissaoDt.getId_Processo(), obFabricaConexao));					
					guiaEmissaoDt.setGuiaEnviadaCadin((new ProcessoParteDebitoCadinNe()).isGuiaEnviadaCadin(guiaEmissaoDt.getId()));
					guiaEmissaoDt.setProcessoPossuiGuiaEnviadaCadin((new ProcessoParteDebitoCadinNe()).isProcessoPossuiGuiaEnviadaCadin(guiaEmissaoDt.getId_Processo()));		
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		return guiaEmissaoDt;
	}
	
	/**
	 * Consulta guia pelo nï¿½mero da guia.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoNumeroGuia(String numeroGuiaCompleto, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
		return guiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissao(FabricaConexao obFabricaConexao, String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao conexao = null;
		try {
			GuiaEmissaoPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaEmissaoPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			}
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissao(idProcesso, listaId_GuiaTipo);
			
			if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
				ProcessoParteDebitoCadinNe processoParteDebitoCadinNe = new ProcessoParteDebitoCadinNe();
				
				for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
					GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);					
					
					guiaEmissaoDt.setGuiaEnviadaCadin(processoParteDebitoCadinNe.isGuiaEnviadaCadin(guiaEmissaoDt.getId()));
					guiaEmissaoDt.setProcessoPossuiGuiaEnviadaCadin(processoParteDebitoCadinNe.isProcessoPossuiGuiaEnviadaCadin(idProcesso));
					
					//Verifica se tem locomoção vinculada a oficial
					if( this.isGuiaOficialVinculadoSPG_Mandando(guiaEmissaoDt.getId()) ) {
						if( guiaEmissaoDt.getGuiaModeloDt() != null ) {
							guiaEmissaoDt.getGuiaModeloDt().setGuiaTipo(guiaEmissaoDt.getGuiaModeloDt().getGuiaTipo() + " (Oficial Vinculado)");
						}
					}
				}
			}
		} finally {
			if( obFabricaConexao == null ) {
				if( conexao != null ) {
					conexao.fecharConexao();
				}
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar ultimo dado de porcentagem do info_repasse no SPG.
	 * 
	 * @param List listaGuiaEmissaoDt
	 * @param Map<GuiaEmissaoDt, List<InfoRepasseSPGDt>> listaGuiaRepasses
	 * @throws Exception
	 */
	public void consultarPercentualRepasseCadaGuia(List listaGuiaEmissaoDt, Map<GuiaEmissaoDt, List<InfoRepasseSPGDt>> listaGuiaRepasses) throws Exception {
		if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
			
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			GuiaTipoNe guiaTipoNe = new GuiaTipoNe();
			
			for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
				
//				if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
//					
//					GuiaTipoDt guiaTipoDt = guiaTipoNe.consultarId(guiaEmissaoDt.getId_GuiaTipo());
//					
//					if( guiaTipoDt != null ) {
//						guiaEmissaoDt.setGuiaTipo( guiaTipoDt.getGuiaTipo() );
//					}
//				}
				
				if( !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {
					String codigoComarcaParaAmbienteREMOTO_CAPITAL = null;
					
					GuiaEmissaoDt guiaSPGDt = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoCapital(guiaEmissaoDt.getNumeroGuiaCompleto());
					codigoComarcaParaAmbienteREMOTO_CAPITAL = ComarcaDt.GOIANIA;
					
					if( guiaSPGDt == null ) {
						guiaSPGDt = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
						codigoComarcaParaAmbienteREMOTO_CAPITAL = ComarcaDt.APARECIDA_DE_GOIANIA;
					}
					
					//Consulta último repasse
					guiaEmissaoDt.setPercRepasse(guiaSPGNe.consultarUltimaPorcentagemRepassadaInfoRepasse(guiaEmissaoDt));
					
					//Consulta repasses da guia
					if( guiaSPGDt != null && guiaSPGDt.getId() != null ) {
						listaGuiaRepasses.put(guiaEmissaoDt, guiaSPGNe.consultarListaInfoRepasseByISNGuia(guiaSPGDt.getId(), codigoComarcaParaAmbienteREMOTO_CAPITAL));
					}
				}
			}
		}
	}
	
	/**
	 * Método que consulta a guiaEmissaoDt utilizada como referencia para o desconto e parcelamento.
	 * 
	 * @param List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public void consultarGuiaReferencia(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if( guiaEmissaoDt != null ) {
			if( guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento() != null && !guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento().isEmpty() ) {
				guiaEmissaoDt.setGuiaEmissaoDtReferencia(this.consultarGuiaEmissao(guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento()));
			}
		}
	}
	
	/**
	 * Método para consultar a guiaEmissaoDt Principal. Guia principal é a guia, por exemplo, utilizada como 
	 * base na guia complementar. Ou seja: Guia x tem id 1 e Guia y tem id 2 porém tem id_guia_principal tem id 1.
	 * Isso quer dizer que a guia y complementou a guia x.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void consultarGuiaPrincipal(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if( guiaEmissaoDt != null ) {
			if( guiaEmissaoDt.getId_GuiaEmissaoPrincipal() != null && !guiaEmissaoDt.getId_GuiaEmissaoPrincipal().isEmpty() ) {
				guiaEmissaoDt.setGuiaEmissaoDtPrincipal(this.consultarGuiaEmissao(guiaEmissaoDt.getId_GuiaEmissaoPrincipal()));
			}
		}
	}
	
	/**
	 * Mï¿½todo para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia que estão AGUARDANDO PAGAMENTO.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoAguardandoPagamento(FabricaConexao obFabricaConexao, String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao conexao = null;
		try {
			GuiaEmissaoPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaEmissaoPs ( conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			}
			
			List listaIdGuiaStatus = new ArrayList();
			
			listaIdGuiaStatus.add(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
			listaIdGuiaStatus.add(GuiaStatusDt.ESTORNO_BANCARIO);
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissaoStatus(idProcesso, listaId_GuiaTipo, listaIdGuiaStatus);
			
			if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
				for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
					GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
					
					if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
						guiaEmissaoDt.setGuiaTipo( this.consultarGuiaTipo( obFabricaConexao, guiaEmissaoDt.getId_GuiaTipo() ) );
					}
				}
			}
		} finally {
			if( obFabricaConexao == null ) {
				if( conexao != null ) {
					conexao.fecharConexao();
				}
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia que estão PAGAS.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoPagaParaGuiaComplementar(FabricaConexao obFabricaConexao, String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao conexao = null;
		try {
			GuiaEmissaoPs obPersistencia;
			if( obFabricaConexao == null ) {
				conexao = new FabricaConexao(FabricaConexao.CONSULTA);
				obPersistencia = new GuiaEmissaoPs(conexao.getConexao());
			}
			else {
				obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			}
			
			List listaIdGuiaStatus = new ArrayList();
			
			listaIdGuiaStatus.add(GuiaStatusDt.PAGO);
			listaIdGuiaStatus.add(GuiaStatusDt.PAGO_ON_LINE);
			listaIdGuiaStatus.add(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR);
			listaIdGuiaStatus.add(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR);
			listaIdGuiaStatus.add(GuiaStatusDt.CANCELADA_PAGA);
			listaIdGuiaStatus.add(GuiaStatusDt.PAGA_CANCELADA);
			listaIdGuiaStatus.add(GuiaStatusDt.PAGO_APOS_VENCIMENTO);
			listaIdGuiaStatus.add(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissaoStatus(idProcesso, listaId_GuiaTipo, listaIdGuiaStatus);
			
			List<GuiaEmissaoDt> listaGuiaEmissaoDtGenericaComplementarPagas = obPersistencia.consultarGuiaEmissaoPagaGenericaParaGuiaComplementar(obFabricaConexao, listaGuiaEmissaoDt, idProcesso);
			if( listaGuiaEmissaoDtGenericaComplementarPagas != null && !listaGuiaEmissaoDtGenericaComplementarPagas.isEmpty() ) {
				if( listaGuiaEmissaoDt == null ) {
					listaGuiaEmissaoDt = new ArrayList();
				}
				
				listaGuiaEmissaoDt.addAll(listaGuiaEmissaoDtGenericaComplementarPagas);
			}
			
			if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
				for( int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
					GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
					
					if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
						guiaEmissaoDt.setGuiaTipo( this.consultarGuiaTipo( obFabricaConexao, guiaEmissaoDt.getId_GuiaTipo() ) );
					}
				}
			}
		} finally {
			if( obFabricaConexao == null ) {
				if( conexao != null ) {
					conexao.fecharConexao();
				}
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar Guias Emitidas pelo id_Processo e lista de tipos de guia que Nï¿½O ESTï¿½O CANCELADAS.
	 * @param String idProcesso
	 * @param List listaId_GuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoValidas(String idProcesso, List listaId_GuiaTipo) throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissaoValidas(idProcesso, listaId_GuiaTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar Guias Emitidas que estï¿½o com o status de aguardando pagamento.
	 * @return List listaGuiaEmissaoDt
	 * @throws Exception
	 */
	public List consultarGuiaEmissao() throws Exception {
		List listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiaEmissao();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar GuiaEmissao pelo número da Guia.
	 * @param String numeroGuiaCompleto
	 * @param String idGuiaTipo (pode ser nulo)
	 * @param String idGuiaStatus (pode ser nulo)
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarNumeroCompleto(String numeroGuiaCompleto, String idGuiaTipo, String idGuiaStatus) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = obPersistencia.consultarNumeroCompleto(numeroGuiaCompleto, idGuiaTipo, idGuiaStatus);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar GuiaEmissao(guia inicial) pelo nï¿½mero da Guia
	 * @param String numeroGuiaCompleto
	 * @param String idGuiaTipo (pode ser nulo)
	 * @param String idGuiaStatus (pode ser nulo)
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarNumeroCompletoGuiaInicial(String numeroGuiaCompleto, String idGuiaTipo, String idGuiaStatus) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = obPersistencia.consultarNumeroCompletoGuiaInicial(numeroGuiaCompleto, idGuiaTipo, idGuiaStatus);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método que retorna o último número de guia emitida.
	 * @return String ultimoNumeroGuia
	 * @throws Exception
	 */
	public String consultarUltimoNumeroGuia() throws Exception {
		String ultimoNumeroGuia = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			ultimoNumeroGuia = obPersistencia.consultarUltimoNumeroGuia();
			
			if( ultimoNumeroGuia != null ) {
				int digito_Serie = ultimoNumeroGuia.length() - 3;
				ultimoNumeroGuia = ultimoNumeroGuia.substring(0, digito_Serie);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return ultimoNumeroGuia;
	}
	
	/**
	 * Mï¿½todo para imprimir guia em PDF.
	 * @param String stCaminho
	 * @param String totalGuia
	 * @param String areaDistribuicao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String Id_GuiaTipo
	 * @param String guiaTipo
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] imprimirGuia(String stCaminho, String totalGuia, String areaDistribuicao, GuiaEmissaoDt guiaEmissaoDt, String idGuiaTipo, String guiaTipo) throws Exception {
		byte[] temp = null;
		ByteArrayOutputStream baos = null;
		try {
			//Geração do Código de Barra
			String codigoBarra = this.gerarCodigoBarra(totalGuia, guiaEmissaoDt.getNumeroGuiaCompleto(), guiaEmissaoDt.getDataVencimento(), idGuiaTipo);
			
			InterfaceJasper ei = null;
			if( guiaEmissaoDt.getListaGuiaItemDt() != null && guiaEmissaoDt.getListaGuiaItemDt().size() > 0 ) {
				List lista = new ArrayList();
				lista.add( guiaEmissaoDt.getListaGuiaItemDt().get(0) );
				ei = new InterfaceJasper(lista);
			}
			
			String pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "DUAJ_Inicial.jasper";
			
			//Parâmetros do relatório
			Map parametros = new HashMap();
			
			parametros.put("caminhoLogo", stCaminho + "imagens" + File.separator + "logoEstadoGoias02.jpg" );
			
			// Este trecho estava mostrando o id guia tipo em vez de mostrar a descrição
//			if( guiaEmissaoDt.getGuiaTipo() != null && guiaEmissaoDt.getGuiaTipo().length() > 0 ) {
//				parametros.put("titulo", guiaEmissaoDt.getGuiaTipo() );
//			}
//			else {
//				parametros.put("titulo", guiaTipo );
//			}
			
			//TRECHO ALTERADO - Para mostrar a descrição (guia tipo) no título da certidão, em vez de mostrar o id guia tipo
			if( guiaTipo != null && guiaTipo.length() > 0 ){
				parametros.put("titulo", guiaTipo );
			}
			else if( guiaEmissaoDt.getGuiaTipo() != null && guiaEmissaoDt.getGuiaTipo().length() > 0 ) {
				parametros.put("titulo", guiaEmissaoDt.getGuiaTipo() );
			}
			
			parametros.put("total", totalGuia );
			parametros.put("Numero", Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) );
			parametros.put("NumeroGuiaCompletoSemFormatacao", guiaEmissaoDt.getNumeroGuiaCompleto());
			
			//Data de emissão
			if( guiaEmissaoDt.getDataEmissao() != null && !guiaEmissaoDt.getDataEmissao().isEmpty() ) {
				parametros.put("Emissao", Funcoes.TelaData(guiaEmissaoDt.getDataEmissao()) );
			}
			else {
				parametros.put("Emissao", Funcoes.dateToStringSoData(new Date()) );
			}
			
			parametros.put("Vencimento", Funcoes.TelaData(guiaEmissaoDt.getDataVencimento()) );
			
			//TRECHO ALTERADO - Para mostrar no campo requerente do pdf da guia o nome da pessoa que solicitou a guia de prática forense
			if( idGuiaTipo.equalsIgnoreCase("38") && (guiaEmissaoDt.getRequerente() != null && guiaEmissaoDt.getRequerente().length() > 0) ){
				parametros.put("Requerente", guiaEmissaoDt.getRequerente() );
			}	
			else {			
				if( (guiaEmissaoDt.getApelante() != null && guiaEmissaoDt.getApelante().length() > 0) || (guiaEmissaoDt.getApelado() != null && guiaEmissaoDt.getApelado().length() > 0) ) {
					parametros.put("Requerente", guiaEmissaoDt.getApelante() + " (100%)" );
					parametros.put("Requerido", guiaEmissaoDt.getApelado() );
				}
				else {
					if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 && guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
						
						this.setNomeApelanteNomeApelado(guiaEmissaoDt);
						
						parametros.put("Requerente", guiaEmissaoDt.getApelante() + " (100%)");
						parametros.put("Requerido", guiaEmissaoDt.getApelado() );
					}
					else {
						parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
						parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
					}
				}
				if( guiaEmissaoDt.getRateioCodigo() != null && guiaEmissaoDt.getRateioCodigo().length() > 0 ) {
					if( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_100_REQUERENTE ) {
						if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 &&
							guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
							
							this.setNomeApelanteNomeApelado(guiaEmissaoDt);
							
							parametros.put("Requerente", guiaEmissaoDt.getApelante() + " (100%)");
							parametros.put("Requerido", guiaEmissaoDt.getApelado() );
						}
						else {
							if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelado() == null ) {
								parametros.put("Requerente", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_Apelante() ) + " (100%)");
								parametros.put("Requerido", "" );
							}
							else {
								parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() + " (100%)");
								parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
							}
						}
					}
					else {
						if( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_100_REQUERIDO ) {
							if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 &&
								guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
								
								this.setNomeApelanteNomeApelado(guiaEmissaoDt);
								
								parametros.put("Requerente", guiaEmissaoDt.getApelante() );
								parametros.put("Requerido", guiaEmissaoDt.getApelado() + " (100%)");
							}
							else {
								if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelado() == null ) {
									parametros.put("Requerente", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_Apelante() ) );
									parametros.put("Requerido", "");
								}
								else {
									parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
									parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() + " (100%)");
								}
							}
						}
						else {
							parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() + " (50%)");
							parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() + " (50%)");
						}
					}
					if( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_VARIAVEL || Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalNe.RATEIO_50_50 ) {
						if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
							if( isParteRequerente(guiaEmissaoDt.getId_ProcessoParteResponsavelGuia(), guiaEmissaoDt.getListaRequerentes()) ) {
								if( guiaEmissaoDt.getRequerente() != null && !guiaEmissaoDt.getRequerente().isEmpty() ) {
									parametros.put("Requerente", guiaEmissaoDt.getRequerente() );
								}
								else {
									parametros.put("Requerente", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) );
								}
								parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
							}
							if( isParteRequerido(guiaEmissaoDt.getId_ProcessoParteResponsavelGuia(), guiaEmissaoDt.getListaRequeridos()) ) {
								parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
								if( guiaEmissaoDt.getRequerido() != null && !guiaEmissaoDt.getRequerido().isEmpty() ) {
									parametros.put("Requerido", guiaEmissaoDt.getRequerido() );
								}
								else {
									parametros.put("Requerido", guiaEmissaoDt.getNomeParte( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) );
								}
							}
						}
						else {
							parametros.put("Requerente", guiaEmissaoDt.getNomePrimeiroRequerente() );
							parametros.put("Requerido", guiaEmissaoDt.getNomePrimeiroRequerido() );
						}
					}
				}
			}
				
			parametros.put("ComarcaCodigo", guiaEmissaoDt.getComarcaCodigo());
			parametros.put("Comarca", guiaEmissaoDt.getComarca() );
			parametros.put("AreaDistribuicao", areaDistribuicao );
			parametros.put("ProcessoTipoCodigo","");
			
			String processoTipo = guiaEmissaoDt.getProcessoTipo();
			if( guiaEmissaoDt.getNaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPG().length() > 0 ) {
				processoTipo = guiaEmissaoDt.getNaturezaSPG() + "(SPG)";
			}
			parametros.put("ProcessoTipo", processoTipo);
			
			if( guiaEmissaoDt.getGuiaModeloDt() != null && guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) && guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
				parametros.put("ProcessoNumero", "Vinculado: " + guiaEmissaoDt.getNumeroProcessoDependente());
			}
			else {
				parametros.put("ProcessoNumero", guiaEmissaoDt.getNumeroProcesso());
			}
			
			parametros.put("ProcessoValor", guiaEmissaoDt.getNovoValorAcaoAtualizado() );
			
			parametros.put("CodigoBarraDigito", Funcoes.FormatarCodigoBarraGuiaDigito(codigoBarra, this.gerarDigitoCodigoBarra(codigoBarra.substring(0,11)), this.gerarDigitoCodigoBarra(codigoBarra.substring(11,22)), this.gerarDigitoCodigoBarra(codigoBarra.substring(22,33)), this.gerarDigitoCodigoBarra(codigoBarra.substring(33))));
			parametros.put("CodigoBarra", codigoBarra );
			
			//parametros.put("MensagemBoleto", "Para realizar o pagamento acessar a página inicial do PJD, menu Emitir Guias e opção Gerar Boleto.");
			//parametros.put("MensagemBancos", "Pagável nas agência da CAIXA ECONÔMICA e Casas Lotéricas.");
			
			
			
			List listaGuiaItemDtAux = guiaEmissaoDt.getListaGuiaItemDt();
			if( listaGuiaItemDtAux != null && listaGuiaItemDtAux.size() > 0 ) {
				listaGuiaItemDtAux = guiaEmissaoDt.getListaGuiaItemDtAgrupadosImpressao(listaGuiaItemDtAux);				
				for(int i = 1; i <= listaGuiaItemDtAux.size(); i++ ) {
					GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDtAux.get(i-1);
					
					if( guiaItemDt != null && guiaItemDt.getCustaDt() != null) {
						
						String nomeItemTratado = this.alterarNomeCustaParaImpressao(guiaItemDt.getCustaDt().getId());
						
						String descricaoItemGuia = null;
						if( nomeItemTratado != null ) {
							descricaoItemGuia = nomeItemTratado + guiaItemDt.getCustaDt().getCodigoRegimentoTratamento();
						}
						else {
							descricaoItemGuia = guiaItemDt.getCustaDt().getArrecadacaoCusta() + guiaItemDt.getCustaDt().getCodigoRegimentoTratamento();
						}
						
						if ( guiaItemDt.getLocomocaoDt() != null && 
							   guiaItemDt.getLocomocaoDt().getBairroDt() != null && 
							   guiaItemDt.getLocomocaoDt().getBairroDt().getBairro() != null && 
							   guiaItemDt.getLocomocaoDt().getBairroDt().getBairro().trim().length() > 0) {
							descricaoItemGuia += " [" + guiaItemDt.getLocomocaoDt().getBairroDt().getBairro().trim() + "]"; 
						}
						
						parametros.put("CodigoItemGuia" + i			, guiaItemDt.getCustaDt().getCodigoArrecadacao() );
						parametros.put("DescricaoItemGuia" + i		, descricaoItemGuia );
						parametros.put("QuantidadeItemGuia" + i		, guiaItemDt.getQuantidade() );
						parametros.put("ValorItemGuia" + i			, Funcoes.FormatarDecimal( guiaItemDt.getValorCalculado() ) );
					}
				}
			}
			
			JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);

			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			temp = baos.toByteArray();
			baos.close();
		} finally {
			try {if (baos!=null) baos.close(); } catch(Exception ex ) {};			
		}
		
		return temp;
	}
	
//	/**
//	 * Método para imprimir guia pelo número completo.
//	 * @param String numeroGuiaCompleto
//	 * @return byte[]
//	 * @throws Exception
//	 */
//	public byte[] imprimirGuia(String numeroGuiaCompleto) throws Exception {
//		byte[] retorno = null;
//		if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
//			
//			GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
//			
//			if( guiaEmissaoDt != null ) {
//				String totalGuia = this.consultarTotalGuiaBancoDados(numeroGuiaCompleto).toString();
//				retorno = this.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), totalGuia, guiaEmissaoDt.getServentia(), guiaEmissaoDt, guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo(), guiaEmissaoDt.getGuiaModeloDt().getGuiaTipo());
//			}
//		}
//		return retorno;
//	}
	
	/**
	 * Mï¿½todo para saber se a parte ï¿½ Requerente.
	 * 
	 * @param String idProcessoParte
	 * @param List listaRequerentes
	 * @return boolean
	 */
	public boolean isParteRequerente(String idProcessoParte, List listaRequerentes) {
		boolean retorno = false;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for (int i = 0; i < listaRequerentes.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaRequerentes.get(i);
			    
				if( idProcessoParte.equals(processoParteDt.getId()) ) {
					retorno = true;
					break;
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para saber se a parte ï¿½ Requerido.
	 * 
	 * @param String idProcessoParte
	 * @param List listaRequeridos
	 * @return boolean
	 */
	public boolean isParteRequerido(String idProcessoParte, List listaRequeridos) {
		boolean retorno = false;
		
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for (int i = 0; i < listaRequeridos.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaRequeridos.get(i);
			    
				if( idProcessoParte.equals(processoParteDt.getId()) ) {
					retorno = true;
					break;
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para saber se o processo estï¿½ no segundo grau ou nï¿½o.
	 * @param String id_serventia
	 * @return boolean
	 */
	public boolean isProcessoSegundoGrau(String id_serventia) throws Exception {
		boolean retorno = false;
		
		serventiaNe = new ServentiaNe();
		retorno = serventiaNe.isServentiaSegundoGrau(id_serventia);
		
		return retorno;
	}
	
	/**
	 * Método para consultar se o usuário é contador.
	 * 
	 * @param String idUsuario
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isEmissorContador(String idUsuario) throws Exception {
		boolean retorno = false;
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		retorno = usuarioServentiaNe.isUsuarioContador(idUsuario);
		
		return retorno;
	}
	
	/**
	 * Método para consultar se o usuário é advogado.
	 * 
	 * @param String idUsuario
	 * @param String idServentia
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean isEmissorAdvogado(String idUsuario, String idServentia) throws Exception {
		boolean retorno = false;
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		return usuarioServentiaNe.isUsuarioAdvogado(idUsuario, idServentia);
	}
	
	/**
	 * Método para consultar se o usuário é gerenciamento do Projudi.
	 * 
	 * @param String idUsuario
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isEmissorGerenciamentoProjudi(String idUsuario) throws Exception {
		boolean retorno = false;
		
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		retorno = usuarioServentiaNe.isUsuarioGerenciamentoProjudi(idUsuario);
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para adicionar um item de custa.
	 * @param int Id_Custa
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItem(int Id_Custa) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		if( custaNe == null )
			custaNe = new CustaNe();
		
		CustaDt custaDt = null;
		custaDt = custaNe.consultarId(String.valueOf(Id_Custa));
		
		if( custaDt != null ) {
			
			guiaCustaModeloDt.setCustaDt(custaDt);
		}
		
		return guiaCustaModeloDt;
	}
	
	public void adicionarItemCalculo(GuiaEmissaoDt guiaEmissaoDt, List<GuiaCustaModeloDt> listaItensGuia) throws Exception {
		//Certidão e Acordão
		if( guiaEmissaoDt.getCertidaoAcordao() != null && guiaEmissaoDt.getCertidaoAcordao().length() > 0 && !guiaEmissaoDt.getCertidaoAcordao().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_CERTIDOES_DE_ACORDAO) );
		}
		
		//Traslados e desarquivamento
		if( guiaEmissaoDt.getDesarquivamento() != null && guiaEmissaoDt.getDesarquivamento().length() > 0 && !guiaEmissaoDt.getDesarquivamento().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS) );
		}
		
		//Restauracao
		if( guiaEmissaoDt.getRestauracao() != null && guiaEmissaoDt.getRestauracao().length() > 0 && !guiaEmissaoDt.getRestauracao().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_RESTAURACAO_DE_AUTOS) );
		}
		
		//Documento Publicado
		if( guiaEmissaoDt.getDocumentoPublicadoQuantidade() != null && guiaEmissaoDt.getDocumentoPublicadoQuantidade().length() > 0 && !guiaEmissaoDt.getDocumentoPublicadoQuantidade().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_POR_DOCUMENTO_PUBLICADO_NO_DIARIO_DE_JUSTICA) );
		}
		
		//Porte e Remessa
		if( guiaEmissaoDt.getPorteRemessaQuantidade() != null && guiaEmissaoDt.getPorteRemessaQuantidade().length() > 0 && !guiaEmissaoDt.getPorteRemessaQuantidade().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_PORTE_E_REMESSA_DE_PROCESSOS_FISICOS) );
		}
		
		//Despesa Postal - 4.VI
		if( guiaEmissaoDt.getCorreioQuantidadeReg4VI() != null && guiaEmissaoDt.getCorreioQuantidadeReg4VI().length() > 0 && !guiaEmissaoDt.getCorreioQuantidadeReg4VI().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM) );
		}
		
		//Emissão de documentos - 4.VII
		if( guiaEmissaoDt.getEmissaoDocumentoQuantidade() != null && guiaEmissaoDt.getEmissaoDocumentoQuantidade().length() > 0 && !guiaEmissaoDt.getEmissaoDocumentoQuantidade().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO) );
		}
		
		//atos de constrição - 4.VIII
		if( guiaEmissaoDt.getAtosConstricao() != null && guiaEmissaoDt.getAtosConstricao().length() > 0 && !guiaEmissaoDt.getAtosConstricao().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_ATOS_DE_CONSTRICAO) );
		}
		
		//*********************** Início 16
		
		//Certidão Decisão item 16.I
		if( guiaEmissaoDt.getCertidaoDecisao() != null && guiaEmissaoDt.getCertidaoDecisao().length() > 0 && !guiaEmissaoDt.getCertidaoDecisao().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.CERTIDOES_DAS_DECISOES) );
		}
		
		//Desarquivamento, traslados e certidões item 16.II
		if( guiaEmissaoDt.getDesarquivamento16II() != null && guiaEmissaoDt.getDesarquivamento16II().length() > 0 && !guiaEmissaoDt.getDesarquivamento16II().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES) );
		}
		
		//Ocorrência 2019/8287 - Desfazendo as alterações da ocorrência 2019/6461 abaixo.
//		//Ocorrência 2019/6461 - Separado o item 16.II em dois campos para poder distinguir a adição automática do item da taxa judiciária item 6 do código tributário.
//		//Traslados e certidões item 16.II (inclui o item da taxa judiciária código tributário)
//		if( guiaEmissaoDt.getDesarquivamento16IITaxaJudiciariaCodigoTributario() != null && guiaEmissaoDt.getDesarquivamento16IITaxaJudiciariaCodigoTributario().length() > 0 && !guiaEmissaoDt.getDesarquivamento16IITaxaJudiciariaCodigoTributario().equals("0")) {
//			if( listaItensGuia == null )
//				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
//			
//			listaItensGuia.add( guiaServicosNe.adicionarItem(CustaDt.TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES) );
//			
//			guiaEmissaoDt.setTaxaJudiciariaServicoCertidao(guiaEmissaoDt.getDesarquivamento16IITaxaJudiciariaCodigoTributario());
//			listaItensGuia.add( guiaServicosNe.adicionarItem(CustaDt.TAXA_JUDICIARIA_SERVICO_CERTIDAO_SITE_TJGO_ITEM_6) );
//		}
		
		//restauração atos item 16.III
		if( guiaEmissaoDt.getRestauracaoAtos() != null && guiaEmissaoDt.getRestauracaoAtos().length() > 0 && !guiaEmissaoDt.getRestauracaoAtos().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.RESTAURACAO_DE_AUTOS) );
		}
		
		//Documento diario justiça
		if( guiaEmissaoDt.getDocumentoDiarioJustica() != null && guiaEmissaoDt.getDocumentoDiarioJustica().length() > 0 && !guiaEmissaoDt.getDocumentoDiarioJustica().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.POR_DOCUMENTO_PUBLICADO) );
		}
		
		//Porte remessa 16.V
		if( guiaEmissaoDt.getPorteRemessaQuantidade16V() != null && guiaEmissaoDt.getPorteRemessaQuantidade16V().length() > 0 && !guiaEmissaoDt.getPorteRemessaQuantidade16V().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.PORTE_E_REMESSA_DE_PROCESSOS_FISICOS) );
		}
		
		//Despesa Postal - 16.VI
		if( guiaEmissaoDt.getCorreioQuantidade() != null && guiaEmissaoDt.getCorreioQuantidade().length() > 0 && !guiaEmissaoDt.getCorreioQuantidade().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.DESPESA_POSTAL) );
		}
		
		//Emissão Documentos - 16.VII
		if( guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII() != null && guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII().length() > 0 && !guiaEmissaoDt.getEmissaoDocumentoQuantidade16VII().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO) );
		}
		
		//Atos Constrição - 16.VIII
		if( guiaEmissaoDt.getAtosConstricao16VIII() != null && guiaEmissaoDt.getAtosConstricao16VIII().length() > 0 && !guiaEmissaoDt.getAtosConstricao16VIII().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.PELA_EMISSAO_DOS_ATOS_DE_CONSTRICAO) );
		}
		
		//Formal de partilha - 16.IX
		if( guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX() != null && guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX().length() > 0 && !guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.FORMAL_DE_PARTILHA_CARTA_DE_SENTENCA) );
			
			guiaEmissaoDt.setTaxaJudiciariaServicoCartaArrematacao(guiaEmissaoDt.getFormalPartilhaCartaQuantidade16IX());
			listaItensGuia.add( this.adicionarItem(CustaDt.TAXA_JUDICIARIA_SERVICO_CARTA_ARREMATACAO_SITE_TJGO_ITEM_5) );
		}
		
		//Cumprimento carta precatória
		if( guiaEmissaoDt.getCumprimentoPrecatoria() != null && guiaEmissaoDt.getCumprimentoPrecatoria().length() > 0 && !guiaEmissaoDt.getCumprimentoPrecatoria().equals("0")) {
			if( listaItensGuia == null )
				listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
			
			listaItensGuia.add( this.adicionarItem(CustaDt.CUMPRIMENTO_DE_CARTAS_PRECATORIAS) );
		}
	}
	
	/**
	 * Método para adicionar item de locomoção.
	 * @throws Exception
	 */
	public void calcularGuiaLocomocao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		
		List<GuiaItemDt> listaGuiaItemLocomocaoAtualDt = new ArrayList<GuiaItemDt>();
		
		this.calcularItensGuiaLocomocao(listaGuiaItemLocomocaoAtualDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);	
		
		if (guiaEmissaoDt.isCitacaoHoraCerta()) {
			this.getGuiaCalculoNe().aplicarRegraCitacaoHoraCertaLocomocoesIntimacaoCitacaoNotificacao(listaGuiaItemLocomocaoAtualDt);
		}
		else {
			if (guiaEmissaoDt.isOficialCompanheiro() || guiaEmissaoDt.isForaHorarioNormal()) {
				this.getGuiaCalculoNe().dobrarLocomocoes(listaGuiaItemLocomocaoAtualDt);
			}
		}
		
		if (listaGuiaItemLocomocaoAtualDt.size() > 0) {
			listaGuiaItemLocomocaoDt.addAll(listaGuiaItemLocomocaoAtualDt);
			this.recalcularTotalGuia(listaGuiaItemLocomocaoDt);
		}
	}
	
	public void calcularGuiaLocomocaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroGuiaLocomocaoDt bairroGuiaLocomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {		
		LocomocaoDt locomocaoDt = new LocomocaoDt();
		locomocaoDt.setBairroDt(bairroGuiaLocomocaoDt.getBairroDt());
		locomocaoDt.setCitacaoHoraCerta(guiaEmissaoDt.isCitacaoHoraCerta());
		locomocaoDt.setQtdLocomocao(bairroGuiaLocomocaoDt.getQuantidade());
		locomocaoDt.setFinalidadeCodigo(Funcoes.StringToInt(bairroGuiaLocomocaoDt.getFinalidade()));
		locomocaoDt.setPenhora(bairroGuiaLocomocaoDt.isPenhora());
		locomocaoDt.setIntimacao(bairroGuiaLocomocaoDt.isIntimacao());
		locomocaoDt.setOficialCompanheiro(bairroGuiaLocomocaoDt.isOficialCompanheiro());
		if( bairroGuiaLocomocaoDt != null && bairroGuiaLocomocaoDt.getOficialSPGDt_Principal() != null ) {
			if( bairroGuiaLocomocaoDt.getOficialSPGDt_Principal().getCodigoOficial() != null ) {
				locomocaoDt.setCodigoOficialSPG(bairroGuiaLocomocaoDt.getOficialSPGDt_Principal().getCodigoOficial());
			}
			if( bairroGuiaLocomocaoDt.getIdMandadoJudicial() != null ) {
				MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId(bairroGuiaLocomocaoDt.getIdMandadoJudicial());
				locomocaoDt.setMandadoJudicialDt(mandadoJudicialDt);
			}
		}
		
		calcularGuiaLocomocaoNOVO(listaGuiaItemLocomocaoDt, bairroGuiaLocomocaoDt.getBairroDt(), locomocaoDt, guiaEmissaoDt, contaVinculadaSegundoGrau);
	}
	
	public void calcularGuiaLocomocaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		
		List<GuiaItemDt> listaGuiaItemLocomocaoAtualDt = new ArrayList<GuiaItemDt>();
		
		this.calcularItensGuiaLocomocaoNOVO(listaGuiaItemLocomocaoAtualDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, contaVinculadaSegundoGrau);	
		
		if (locomocaoDt.isCitacaoHoraCerta()) {
			this.getGuiaCalculoNe().aplicarRegraCitacaoHoraCertaLocomocoesIntimacaoCitacaoNotificacao(listaGuiaItemLocomocaoAtualDt);
		}
		else {
			if (locomocaoDt.isOficialCompanheiro() || locomocaoDt.isForaHorarioNormal()) {
				this.getGuiaCalculoNe().dobrarLocomocoes(listaGuiaItemLocomocaoAtualDt);
			}
		}
		
		if (listaGuiaItemLocomocaoAtualDt.size() > 0) {
			for (Iterator iterator = listaGuiaItemLocomocaoAtualDt.iterator(); iterator.hasNext();) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) iterator.next();
				if (guiaItemDt.getLocomocaoDt() != null ){
					guiaItemDt.getLocomocaoDt().setQuantidadeAcrescimo(locomocaoDt.getQuantidadeAcrescimo());
					guiaItemDt.getLocomocaoDt().setCitacaoHoraCerta(locomocaoDt.isCitacaoHoraCerta());
					guiaItemDt.getLocomocaoDt().setForaHorarioNormal(locomocaoDt.isForaHorarioNormal());
					guiaItemDt.getLocomocaoDt().setFinalidadeCodigo(locomocaoDt.getFinalidadeCodigo());
					guiaItemDt.getLocomocaoDt().setPenhora(locomocaoDt.isPenhora());
					guiaItemDt.getLocomocaoDt().setIntimacao(locomocaoDt.isIntimacao());
					if( locomocaoDt != null ) {
						if( locomocaoDt.getCodigoOficialSPG() != null ) {
							guiaItemDt.getLocomocaoDt().setCodigoOficialSPG(locomocaoDt.getCodigoOficialSPG());
						}
						if( locomocaoDt.getMandadoJudicialDt() != null && locomocaoDt.getMandadoJudicialDt().getId() != null ) {
							MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
							mandadoJudicialDt.setId(locomocaoDt.getMandadoJudicialDt().getId());
							guiaItemDt.getLocomocaoDt().setMandadoJudicialDt(mandadoJudicialDt);
						}
					}
				}
			}
			listaGuiaItemLocomocaoDt.addAll(listaGuiaItemLocomocaoAtualDt);
			this.recalcularTotalGuia(listaGuiaItemLocomocaoDt);
		}
	}
	
	/**
	 * Método para adicionar itens de locomoção.
	 * @throws Exception
	 */
	private void calcularItensGuiaLocomocao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		if (guiaEmissaoDt.isLocomocaoParaAvaliacao()) { 
			//Finalidade igual a locomoção para avaliação...
			calcularItensGuiaLocomocaoAvaliacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt);
		} 
		else {
			if (!guiaEmissaoDt.isPenhora() || guiaEmissaoDt.isIntimacao()) { 
				//Opção de penhora desmarcada ou intimação marcada... 
				calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacaoPenhoraAvaliacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
			}
			else {
				//Opção de penhora marcada e intimação desmarcada...			
				calcularItensGuiaLocomocaoPenhoraAvaliacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
			}
		}
	}
	
	private void calcularItensGuiaLocomocaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		if (locomocaoDt.isLocomocaoParaAvaliacao()) { 
			//Finalidade igual a locomoção para avaliação...
			calcularItensGuiaLocomocaoAvaliacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao,locomocaoDt, guiaEmissaoDt);
		} 
		else {
			if (!locomocaoDt.isPenhora() || locomocaoDt.isIntimacao()) { 
				//Opção de penhora desmarcada ou intimação marcada... 
				calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacaoPenhoraAvaliacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, contaVinculadaSegundoGrau);
			}
			else {
				//Opção de penhora marcada e intimação desmarcada...			
				calcularItensGuiaLocomocaoPenhoraAvaliacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, contaVinculadaSegundoGrau);			
			}
		}
	}
	
	private void calcularItensGuiaLocomocaoAvaliacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		// Adicionando custa de Locomoção para Avaliação [1084]
		adicionarGuiaItemLocomocaoAvaliacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, true);
	}
	
	private void calcularItensGuiaLocomocaoAvaliacaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		// Adicionando custa de Locomoção para Avaliação [1084]
		adicionarGuiaItemLocomocaoAvaliacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, true);
	}
	
	private void calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacaoPenhoraAvaliacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		// Adicionando custa de Locomoção [1074]
		GuiaItemDt guiaItemLocomocaoDt = calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, true, contaVinculadaSegundoGrau);
		if (guiaItemLocomocaoDt != null && (guiaEmissaoDt.isPenhora() || guiaEmissaoDt.isFinalidadeCitacaoPenhoraPracaLeilao() || guiaEmissaoDt.isFinalidadeCitacaoPenhoraAvaliacaoPracaLeilao() || guiaEmissaoDt.isFinalidadeCitacaoPenhoraAvaliacaoAlienacao())) {
			//Opção de penhora marcada...
			// Adicionando custa de Locomoção para Penhora [1082]
			GuiaItemDt guiaItemPenhoraDt = adicionarGuiaItemLocomocaoPenhora(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, false); //aqui Fred
			if (guiaItemPenhoraDt != null && (guiaEmissaoDt.isPenhora() || guiaEmissaoDt.isFinalidadeCitacaoPenhoraAvaliacaoPracaLeilao() || guiaEmissaoDt.isFinalidadeCitacaoPenhoraAvaliacaoAlienacao())) {
				guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemSegundoDt(guiaItemPenhoraDt);
				
//              Só poderá ser cobrada uma única conta vinculada para cada bairro selecionado, nesse caso a 1074 já possui, portanto a 1082 não deverá possuir [Marcelo Corregedoria]. 				
//				//Ele somente verifica se o processo é de execução fiscal, caso sim, não faz a cobrança de conta vinculada [ocorrência 2013/40267].
//				if (!guiaEmissaoDt.isProcessoExecucaoFiscal()) {
//					GuiaItemDt guiaItemContaVinculadaPenhoraDt = adicionarGuiaItemLocomocaoContaVinculada(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt);
//					if (guiaItemContaVinculadaPenhoraDt != null) {
//						guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemSegundoContaVinculadaDt(guiaItemContaVinculadaPenhoraDt);
//						guiaItemPenhoraDt.setGuiaItemVinculadoDt(guiaItemContaVinculadaPenhoraDt);
//					}
//				}

				// Adicionando custa de Locomoção para Avaliação [1084]
				if (guiaEmissaoDt.isIntimacao()) {
					GuiaItemDt guiaItemContaAvaliacaoDt = adicionarGuiaItemLocomocaoAvaliacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, false); //aqui Fred
					if (guiaItemContaAvaliacaoDt != null) {
						guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemTerceiroDt(guiaItemContaAvaliacaoDt);	
					}
				}												
			}			
		}
	}
	
	private void calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacaoPenhoraAvaliacaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		// Adicionando custa de Locomoção [1074]
		GuiaItemDt guiaItemLocomocaoDt = calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, true, contaVinculadaSegundoGrau);
		if (guiaItemLocomocaoDt != null && (locomocaoDt.isPenhora() || locomocaoDt.isFinalidadeCitacaoPenhoraPracaLeilao() || locomocaoDt.isFinalidadeCitacaoPenhoraAvaliacaoPracaLeilao() || locomocaoDt.isFinalidadeCitacaoPenhoraAvaliacaoAlienacao())) {
			//Opção de penhora marcada...
			// Adicionando custa de Locomoção para Penhora [1082]
			GuiaItemDt guiaItemPenhoraDt = adicionarGuiaItemLocomocaoPenhoraNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, false); //aqui Fred
			if (guiaItemPenhoraDt != null && (locomocaoDt.isPenhora() || locomocaoDt.isFinalidadeCitacaoPenhoraAvaliacaoPracaLeilao() || locomocaoDt.isFinalidadeCitacaoPenhoraAvaliacaoAlienacao())) {
				guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemSegundoDt(guiaItemPenhoraDt);
				
				// Adicionando custa de Locomoção para Avaliação [1084]
				if (locomocaoDt.isIntimacao()) {
					GuiaItemDt guiaItemContaAvaliacaoDt = adicionarGuiaItemLocomocaoAvaliacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, false); //aqui Fred
					if (guiaItemContaAvaliacaoDt != null) {
						guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemTerceiroDt(guiaItemContaAvaliacaoDt);	
					}
				}												
			}			
		}
	}
	
	private GuiaItemDt calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao, boolean contaVinculadaSegundoGrau) throws Exception {
		// Adicionando custa de Locomoção [1074]
		GuiaItemDt guiaItemLocomocaoDt = adicionarGuiaItemLocomocaoIntimacaoCitacaoNotificacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, geraLocomocao);
		
		//Ele somente verifica se o processo é de execução fiscal, caso sim, não faz a cobrança de conta vinculada [ocorrência 2013/40267].
		if (guiaItemLocomocaoDt != null && !guiaEmissaoDt.isProcessoExecucaoFiscal()) {
			// Adicionando custa de Locomoção Conta Vinculada [1058]
			//Se guia inicial e execução de iptu ou execução fiscal NÃO ADICIONA conta vinculada(email Marcelo letra D 03/06/2016)
			if( guiaEmissaoDt != null && 
				this.isGuiaInicial(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()) &&
				guiaEmissaoDt.getId_NaturezaSPG() != null && 
					( 
						guiaEmissaoDt.getId_NaturezaSPG().equals(String.valueOf(NaturezaSPGDt.EXECUCAO_FISCAL))
							||
						guiaEmissaoDt.getId_NaturezaSPG().equals(String.valueOf(NaturezaSPGDt.EXECUCAO_IPTU))
					)
				) {
				//Em branco
			}
			else {
				GuiaItemDt guiaItemContaVinculadaDt = adicionarGuiaItemLocomocaoContaVinculada(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
				if (guiaItemContaVinculadaDt != null) {
					guiaItemLocomocaoDt.setGuiaItemVinculadoDt(guiaItemContaVinculadaDt);
					if (guiaItemLocomocaoDt.getLocomocaoDt() != null) {
						guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemContaVinculadaDt(guiaItemContaVinculadaDt);
					}
				}
			}		
		}		
		return guiaItemLocomocaoDt;
	}
	
	private GuiaItemDt calcularItensGuiaLocomocaoIntimacaoCitacaoNotificacaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao, boolean contaVinculadaSegundoGrau) throws Exception {
		// Adicionando custa de Locomoção [1074]
		GuiaItemDt guiaItemLocomocaoDt = adicionarGuiaItemLocomocaoIntimacaoCitacaoNotificacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, geraLocomocao);
		
		// Ele somente verifica se o processo é de execução fiscal, caso sim, não faz a cobrança de conta vinculada [ocorrência 2013/40267].
		// Se guia inicial e execução de iptu ou execução fiscal NÃO ADICIONA conta vinculada(email Marcelo letra D 03/06/2016)
		if (guiaItemLocomocaoDt != null && guiaEmissaoDt != null && !guiaEmissaoDt.isProcessoExecucaoFiscal()) {
			// Adicionando custa de Locomoção Conta Vinculada [1058]			
			GuiaItemDt guiaItemContaVinculadaDt = adicionarGuiaItemLocomocaoContaVinculada(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
			if (guiaItemContaVinculadaDt != null) {
				guiaItemLocomocaoDt.setGuiaItemVinculadoDt(guiaItemContaVinculadaDt);
				if (guiaItemLocomocaoDt.getLocomocaoDt() != null) {
					guiaItemLocomocaoDt.getLocomocaoDt().setGuiaItemContaVinculadaDt(guiaItemContaVinculadaDt);
				}
			}	
		}		
		return guiaItemLocomocaoDt;
	}
	
	private void calcularItensGuiaLocomocaoPenhoraAvaliacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		// Adicionando custa de Locomoção para Penhora [1082]
		GuiaItemDt guiaItemPenhoraDt = adicionarGuiaItemLocomocaoPenhora(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, true);
		if (guiaItemPenhoraDt != null && !guiaEmissaoDt.isFinalidadeLocomocao()) {
			//Ele somente verifica se o processo é de execução fiscal, caso sim, não faz a cobrança de conta vinculada [ocorrência 2013/40267].
			if (!guiaEmissaoDt.isProcessoExecucaoFiscal()) {
				// Adicionando custa de Locomoção Conta Vinculada da Penhora [1058]
				GuiaItemDt guiaItemContaVinculadaPenhoraDt = adicionarGuiaItemLocomocaoContaVinculada(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
				if (guiaItemContaVinculadaPenhoraDt != null) {
					guiaItemPenhoraDt.setGuiaItemVinculadoDt(guiaItemContaVinculadaPenhoraDt);
					guiaItemPenhoraDt.getLocomocaoDt().setGuiaItemContaVinculadaDt(guiaItemContaVinculadaPenhoraDt);
				}	
			}			
								
			// Adicionando custa de Locomoção para Avaliação [1084]
			GuiaItemDt guiaItemContaAvaliacaoDt = adicionarGuiaItemLocomocaoAvaliacao(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, false);
			if (guiaItemContaAvaliacaoDt != null) {
				guiaItemPenhoraDt.getLocomocaoDt().setGuiaItemSegundoDt(guiaItemContaAvaliacaoDt);					
			}
		}
	}
	
	private void calcularItensGuiaLocomocaoPenhoraAvaliacaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		// Adicionando custa de Locomoção para Penhora [1082]
		GuiaItemDt guiaItemPenhoraDt = adicionarGuiaItemLocomocaoPenhoraNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, true);
		if (guiaItemPenhoraDt != null && !locomocaoDt.isFinalidadeLocomocao()) {
			//Ele somente verifica se o processo é de execução fiscal, caso sim, não faz a cobrança de conta vinculada [ocorrência 2013/40267].
			//ok não utilizara LocomocaoDt
			if (!guiaEmissaoDt.isProcessoExecucaoFiscal()) {
				// Adicionando custa de Locomoção Conta Vinculada da Penhora [1058]
				//ok não utilizara LocomocaoDt
				GuiaItemDt guiaItemContaVinculadaPenhoraDt = adicionarGuiaItemLocomocaoContaVinculada(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
				if (guiaItemContaVinculadaPenhoraDt != null) {
					guiaItemPenhoraDt.setGuiaItemVinculadoDt(guiaItemContaVinculadaPenhoraDt);
					guiaItemPenhoraDt.getLocomocaoDt().setGuiaItemContaVinculadaDt(guiaItemContaVinculadaPenhoraDt);
				}	
			}			
								
			// Adicionando custa de Locomoção para Avaliação [1084]
			GuiaItemDt guiaItemContaAvaliacaoDt = adicionarGuiaItemLocomocaoAvaliacaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, locomocaoDt, guiaEmissaoDt, false);
			if (guiaItemContaAvaliacaoDt != null) {
				guiaItemPenhoraDt.getLocomocaoDt().setGuiaItemSegundoDt(guiaItemContaAvaliacaoDt);					
			}
		}
	}
	
	/**
	 * Método para calcular o item de custa de locomoção: 1074 - Locomoção.
	 * @param listaGuiaItemLocomocaoDt
	 * @param bairroLocomocao
	 * @param guiaEmissaoDt
	 * @return
	 * @throws Exception
	 */
	private GuiaItemDt adicionarGuiaItemLocomocaoIntimacaoCitacaoNotificacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloLocomocaoIntimacaoCitacaoNotificacao = this.adicionarItemLocomocao(bairroLocomocao);
		GuiaItemDt guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt = null;
		if (guiaCustaModeloLocomocaoIntimacaoCitacaoNotificacao.getCustaDt() != null) {
			int quantidadeAcrescimo = 0;
			if (geraLocomocao) {
				quantidadeAcrescimo = Funcoes.StringToInt(guiaEmissaoDt.getQuantidadeAcrescimo());
			}
			guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoIntimacaoCitacaoNotificacao.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, geraLocomocao, guiaEmissaoDt.isCitacaoHoraCerta());
			if (guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt);
			}
		}
		return guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt;		
	}
	
	/**
	 * Método para calcular o item de custa de locomoção: 1074 - Locomoção.
	 * @param listaGuiaItemLocomocaoDt
	 * @param bairroLocomocao
	 * @param guiaEmissaoDt
	 * @return
	 * @throws Exception
	 */
	private GuiaItemDt adicionarGuiaItemLocomocaoIntimacaoCitacaoNotificacaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloLocomocaoIntimacaoCitacaoNotificacao = this.adicionarItemLocomocao(bairroLocomocao);
		GuiaItemDt guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt = null;
		if (guiaCustaModeloLocomocaoIntimacaoCitacaoNotificacao.getCustaDt() != null) {
			int quantidadeAcrescimo = 0;
			if (geraLocomocao) {
				quantidadeAcrescimo = locomocaoDt.getQuantidadeAcrescimo();
			}
			guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoIntimacaoCitacaoNotificacao.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, geraLocomocao, locomocaoDt.isCitacaoHoraCerta());
			if (guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt);
			}
		}
		return guiaItemLocomocaoIntimacaoCitacaoNotificacaoDt;		
	}
	
	/**
	 * Método para calcular o item de custa de locomoção: 1082 - Oficial de Justiça conta Vinculada.
	 * @param listaGuiaItemLocomocaoDt
	 * @param bairroLocomocao
	 * @param guiaEmissaoDt
	 * @return
	 * @throws Exception
	 */
	private GuiaItemDt adicionarGuiaItemLocomocaoContaVinculada(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		
		GuiaCustaModeloDt guiaCustaModeloLocomocaoContaVinculada = null;
		
		if( contaVinculadaSegundoGrau ) 
			guiaCustaModeloLocomocaoContaVinculada = this.adicionarItemLocomocaoOficialContaVinculadaSegundoGrau(bairroLocomocao);
		else 
			guiaCustaModeloLocomocaoContaVinculada = this.adicionarItemLocomocaoOficialContaVinculada(bairroLocomocao);
		
		GuiaItemDt guiaItemContaVinculadaDt = null;
		if (guiaCustaModeloLocomocaoContaVinculada != null && guiaCustaModeloLocomocaoContaVinculada.getCustaDt() != null) {
			guiaItemContaVinculadaDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoContaVinculada.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), 0, false, false);
			if (guiaItemContaVinculadaDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemContaVinculadaDt);
			}
		}
		return guiaItemContaVinculadaDt;		
	}
	
	/**
	 * Método para calcular o item de custa de locomoção: 1082 - Custas Locomoção para Penhora.
	 * @param listaGuiaItemLocomocaoDt
	 * @param bairroLocomocao
	 * @param guiaEmissaoDt
	 * @param quantidadeAcrescimo
	 * @param geraLocomocao
	 * @return
	 * @throws Exception
	 */
	private GuiaItemDt adicionarGuiaItemLocomocaoPenhora(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloLocomocaoParaPenhora = this.adicionarItemLocomocaoPenhora(bairroLocomocao);
		GuiaItemDt guiaItemPenhoraDt = null;
		if (guiaCustaModeloLocomocaoParaPenhora.getCustaDt() != null) {
			int quantidadeAcrescimo = 0;
			if (geraLocomocao) {
				quantidadeAcrescimo = Funcoes.StringToInt(guiaEmissaoDt.getQuantidadeAcrescimo());
			}
			guiaItemPenhoraDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoParaPenhora.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, geraLocomocao, false);
			if (guiaItemPenhoraDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemPenhoraDt);
			}
		}
		return guiaItemPenhoraDt;		
	}
	
	private GuiaItemDt adicionarGuiaItemLocomocaoPenhoraNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloLocomocaoParaPenhora = this.adicionarItemLocomocaoPenhora(bairroLocomocao);
		GuiaItemDt guiaItemPenhoraDt = null;
		if (guiaCustaModeloLocomocaoParaPenhora.getCustaDt() != null) {
			int quantidadeAcrescimo = 0;
			if (geraLocomocao) {
				quantidadeAcrescimo = locomocaoDt.getQuantidadeAcrescimo();
			}
			guiaItemPenhoraDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoParaPenhora.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, geraLocomocao, false);
			if (guiaItemPenhoraDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemPenhoraDt);
			}
		}
		return guiaItemPenhoraDt;		
	}
	
	/**
	 * Método para calcular o item de custa de locomoção: 1084 - Custas Locomoção para Avaliação.
	 * @param listaGuiaItemLocomocaoDt
	 * @param bairroLocomocao
	 * @param guiaEmissaoDt
	 * @param quantidadeAcrescimo
	 * @param geraLocomocao
	 * @return
	 * @throws Exception
	 */
	private GuiaItemDt adicionarGuiaItemLocomocaoAvaliacao(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloLocomocaoParaAvaliacao = this.adicionarItemLocomocaoAvaliacao(bairroLocomocao);
		GuiaItemDt guiaItemAvaliacaoDt = null;
		if (guiaCustaModeloLocomocaoParaAvaliacao.getCustaDt() != null) {
			int quantidadeAcrescimo = 0;
			if (geraLocomocao) {
				quantidadeAcrescimo = Funcoes.StringToInt(guiaEmissaoDt.getQuantidadeAcrescimo());
			}
			guiaItemAvaliacaoDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoParaAvaliacao.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, geraLocomocao, false);
			if (guiaItemAvaliacaoDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemAvaliacaoDt);
			}
		}
		return guiaItemAvaliacaoDt;		
	}
	
	/**
	 * Método para calcular o item de custa de locomoção: 1084 - Custas Locomoção para Avaliação.
	 * @param listaGuiaItemLocomocaoDt
	 * @param bairroLocomocao
	 * @param guiaEmissaoDt
	 * @param quantidadeAcrescimo
	 * @param geraLocomocao
	 * @return
	 * @throws Exception
	 */
	private GuiaItemDt adicionarGuiaItemLocomocaoAvaliacaoNOVO(List<GuiaItemDt> listaGuiaItemLocomocaoDt, BairroDt bairroLocomocao, LocomocaoDt locomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean geraLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloLocomocaoParaAvaliacao = this.adicionarItemLocomocaoAvaliacao(bairroLocomocao);
		GuiaItemDt guiaItemAvaliacaoDt = null;
		if (guiaCustaModeloLocomocaoParaAvaliacao.getCustaDt() != null) {
			int quantidadeAcrescimo = 0;
			if (geraLocomocao) {
				quantidadeAcrescimo = locomocaoDt.getQuantidadeAcrescimo();
			}
			guiaItemAvaliacaoDt = getGuiaCalculoNe().calcularLocomocaoOficial(guiaCustaModeloLocomocaoParaAvaliacao.getCustaDt(), guiaEmissaoDt.getValorLocomocaoCodigo(), quantidadeAcrescimo, geraLocomocao, false);
			if (guiaItemAvaliacaoDt != null) {
				listaGuiaItemLocomocaoDt.add(guiaItemAvaliacaoDt);
			}
		}
		return guiaItemAvaliacaoDt;		
	}	
	
	/**
	 * Método para adicionar item de locomoção.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocao(BairroDt bairroLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO));
		if (custaDt != null) custaDt.setBairroLocomocao(bairroLocomocao);
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Método para adicionar o item de guia locomoções de acordo com o tipo da guia.
	 * Este método é utilizado para inserir locomoções que já foram cumpridas por algum oficial de justiça (regimento 1155), nesse caso o oficial que a cumpriu é obrigatório, 
	 * pois na guia podem existir mais de uma locomoção e cada locomoção pode ter sido cumprida por um oficial diferente.
	 * [O SERVLET GuiaFinalCt JÁ FOI TRATADO PARA ESSE CASO.]
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	@Deprecated
	public GuiaCustaModeloDt adicionarItemLocomocaoOficial(BairroDt bairroLocomocao) throws Exception {
		return this.adicionarItemLocomocaoOficial(bairroLocomocao, null);
	}
	
	/**
	 * Método para adicionar o item de guia locomoções de acordo com o tipo da guia.
	 * * Este método é utilizado para inserir locomoções que já foram cumpridas por algum oficial de justiça (regimento 1155), nesse caso o oficial que a cumpriu é obrigatório, 
	 * pois na guia podem existir mais de uma locomoção e cada locomoção pode ter sido cumprida por um oficial diferente.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoOficial(BairroDt bairroLocomocao, OficialSPGDt oficialSPGDt) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL));
		if (custaDt != null) {
			custaDt.setOficialSPGDt(oficialSPGDt);
			custaDt.setBairroLocomocao(bairroLocomocao);
		}
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo para adicionar item de locomoï¿½ï¿½o de 2ï¿½ grau.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocao2Grau() throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_2_GRAU));
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo para adicionar o item de guia locomoï¿½ï¿½o oficial adhoc de acordo com o tipo da guia (Usado na Guia Fazenda Municipal).
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoOficialAdHoc() throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL_ADHOC));
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo para adicionar o item de guia locomoï¿½ï¿½o de conta vinculada de acordo com o tipo da guia.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoOficialContaVinculada(BairroDt bairroLocomocao, OficialSPGDt oficialSPGDt) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL));
		
		if (custaDt != null) {
			custaDt.setOficialSPGDt(oficialSPGDt);
			custaDt.setBairroLocomocao(bairroLocomocao);
		}
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Método para adicionar o item de guia locomoção de conta vinculada de acordo com o tipo da guia.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoOficialContaVinculada(BairroDt bairroLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL));
		if (custaDt != null) custaDt.setBairroLocomocao(bairroLocomocao);
						
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Método para adicionar o item de guia locomoção de conta vinculada de SEGUNDO GRAU de acordo com o tipo da guia.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoOficialContaVinculadaSegundoGrau(BairroDt bairroLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL_SEGUNDO_GRAU));
		if (custaDt != null) custaDt.setBairroLocomocao(bairroLocomocao);
						
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo para adicionar o item de guia locomoï¿½ï¿½o de conta vinculada de acordo com o tipo da guia.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoAvaliacao(BairroDt bairroLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_AVALIACAO));
		if (custaDt != null) custaDt.setBairroLocomocao(bairroLocomocao);
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo para adicionar o item de guia locomoï¿½ï¿½o de conta vinculada de acordo com o tipo da guia.
	 * @return GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemLocomocaoPenhora(BairroDt bairroLocomocao) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarId(String.valueOf(CustaDt.LOCOMOCAO_PARA_PENHORA));
		if (custaDt != null) custaDt.setBairroLocomocao(bairroLocomocao);
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo para calcular os itens da guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * @param String reducao50Porcento
	 * @param String citacaoHoraCerta
	 * @param String foraHorarioNormal
	 * 
	 * @return List listaGuiaItemDt
	 * 
	 * @throws Exception
	 */
	public List<GuiaItemDt> calcularItensGuia(GuiaEmissaoDt guiaEmissaoDt, List<GuiaCustaModeloDt> listaGuiaCustaModeloDt, Map valoresReferenciaCalculo, String reducao50Porcento, String citacaoHoraCerta) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;

		//Método adicionado para retirar itens de acordo com regras
		this.retirarItensPorRegra(guiaEmissaoDt, listaGuiaCustaModeloDt);
		
		this.getGuiaCalculoNe().setCustasQuantitativas(custasQuantitativas);
		listaGuiaItemDt = this.getGuiaCalculoNe().calcularItensGuia_Refatorado(guiaEmissaoDt, listaGuiaCustaModeloDt, valoresReferenciaCalculo);
		
		//************
		//Tarefas Pós Cálculos
		
		//Adicionado para o novo regimento e resolução número 81 do dia 22 de novembro de 2017.
		//Parágrafo 2(segundo)
		if( guiaEmissaoDt != null
				&& guiaEmissaoDt.getGuiaModeloDt() != null
				&& guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo() != null
				&& guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_FINAL)
				&& guiaEmissaoDt.getId_Processo() != null ) {
			
			if( this.guiaItemNe == null ) {
				this.guiaItemNe = new GuiaItemNe();
			}
			
			//Itens guias parceladas
			List<GuiaItemDt> listaGuiaItemDtAguardandoPagamento = this.guiaItemNe.consultarItensGuiasParcelasAguardandoPagamento(guiaEmissaoDt.getId_Processo());
			
			if( listaGuiaItemDtAguardandoPagamento != null ) {
				listaGuiaItemDt.addAll(listaGuiaItemDtAguardandoPagamento);
			}
			
			//Alteração a pedido do Marcelo Tiago por telefone.(Adicionando agora as guias complementares e de serviço em aberto)
			//Itens guias de serviços
			listaGuiaItemDtAguardandoPagamento = this.guiaItemNe.consultarItensGuiasServicosAguardandoPagamento(guiaEmissaoDt.getId_Processo());
			
			if( listaGuiaItemDtAguardandoPagamento != null ) {
				listaGuiaItemDt.addAll(listaGuiaItemDtAguardandoPagamento);
			}
			
			//Itens guias complementares
			listaGuiaItemDtAguardandoPagamento = this.guiaItemNe.consultarItensGuiasComplementaresAguardandoPagamento(guiaEmissaoDt.getId_Processo());
			
			if( listaGuiaItemDtAguardandoPagamento != null ) {
				listaGuiaItemDt.addAll(listaGuiaItemDtAguardandoPagamento);
			}
			
			//Itens guias de GRS Emitidas pela guia genérica
			listaGuiaItemDtAguardandoPagamento = this.guiaItemNe.consultarItensGuiasGRSGenericaAguardandoPagamento(guiaEmissaoDt.getId_Processo());
			
			if( listaGuiaItemDtAguardandoPagamento != null ) {
				listaGuiaItemDt.addAll(listaGuiaItemDtAguardandoPagamento);
			}
			
			this.recalcularTotalGuia(listaGuiaItemDt);
		}
		
		//Dobrar valor da guia? Duplico os itens.
		if( valoresReferenciaCalculo.get(CustaDt.DOBRAR_VALOR_GUIA) != null && valoresReferenciaCalculo.get(CustaDt.DOBRAR_VALOR_GUIA).toString().equals(String.valueOf(CustaDt.DOBRAR_VALOR_GUIA)) ) {
			if( listaGuiaItemDt != null ) {
				listaGuiaItemDt.addAll(listaGuiaItemDt);
			}
			this.recalcularTotalGuia(listaGuiaItemDt);
		}
		
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaItemDt.size();) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				//************************************
				//Mudar nome de itens
				//Esta fase aqui foi adicionada depois que se percebeu a necessidade de alterar o nome 
				//de apresentação dos itens depois de conversa com o Marcelo da Corregedoria.
				//Case seja necessário alterar ou adicionar outra mudança, por favor, apenas mudar no 
				//Método GuiaEmissaoNe.alterarNomeCustaParaImpressao() que está sendo chamado neste if abaixo.
				if( guiaItemDt != null && guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null ) {
					String novoNomeLabel = this.alterarNomeCustaParaImpressao(guiaItemDt.getCustaDt().getId());
					if( novoNomeLabel != null ) {
						guiaItemDt.getCustaDt().setArrecadacaoCusta(novoNomeLabel);
					}
				}
				
				//************************************
				//Retirar itens com quantidade zerados
				//É obrigatório todos os itens estarem com quantidade e valor preenchidos.
				String valorCalculado = guiaItemDt.getValorCalculado();
				valorCalculado = valorCalculado.replace(".", "");
				valorCalculado = valorCalculado.replace(",", ".");
				BigDecimal valorCalculadoFormatado 	= new BigDecimal(Funcoes.StringToDouble(valorCalculado));
				
				//if( Funcoes.StringToInt(guiaItemDt.getQuantidade()) == 0 || valorCalculadoFormatado.compareTo(new BigDecimal("0.0")) == 0 ) {
				if( guiaItemDt.getQuantidade() == null || guiaItemDt.getQuantidade().length() == 0 ) {
					listaGuiaItemDt.remove(i);
					if( i == 0) {
						i = 0;
					}
					else {
						i--;
					}
				}
				else {
					i++;
				}
			}
		}
		
		//Art. 18 lei 12832/96 de redução de 50%
		if( reducao50Porcento != null ) {
			if( reducao50Porcento.equals(GuiaCalculoNe.LEI_REDUCAO_50_PORCENTO) ) {
				this.getGuiaCalculoNe().calcularReducaoLei50Porcento(listaGuiaItemDt, reducao50Porcento);
			}
		}
		
		//Desconto 50% pedido do juiz
		if( valoresReferenciaCalculo.get(CustaDt.DESCONTO_PEDIDO_JUIZ) != null && valoresReferenciaCalculo.get(CustaDt.DESCONTO_PEDIDO_JUIZ).toString().length() > 0 ) {
			this.getGuiaCalculoNe().calcularReducao50PorcentoGuia(listaGuiaItemDt);
		}
		
		//Citação por hora certa
		if( citacaoHoraCerta != null ) {
			if( citacaoHoraCerta.equals(GuiaEmissaoDt.VALOR_SIM) ) {
				this.getGuiaCalculoNe().calcularCitacaoHoraCerta(listaGuiaItemDt);
			}
		}
		
		//Ocorrência 2018/1028
		this.retirarItensCalculadosPorRegra(guiaEmissaoDt, listaGuiaItemDt);
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para setar a quantidade 1 para cada item consultado do modelo.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @throws Exception
	 */
	public void colocarQuantidade1ParaItensConsultadosGuiaInicial(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaCustaModeloDt) throws Exception {

		
		if( listaGuiaCustaModeloDt != null && listaGuiaCustaModeloDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
				GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt)listaGuiaCustaModeloDt.get(i);
				CustaDt custaDt = guiaCustaModeloDt.getCustaDt();
				
					guiaEmissaoDt.setAtualizacaoValorNominalQuantidade("1");
					guiaEmissaoDt.setContadorQuantidade("1"); 
					guiaEmissaoDt.setCustasQuantidade("1");
					guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
					guiaEmissaoDt.setRetificacaoCalculosQuantidade("1");
					guiaEmissaoDt.setEscrivaniaQuantidade("1");
					guiaEmissaoDt.setTransformacaoMoedaQuantidade("1");
					guiaEmissaoDt.setCorreioQuantidade("1");
					guiaEmissaoDt.setRetificacaoCustasQuantidade("1");
					guiaEmissaoDt.setDepositarioPublico("1");
					guiaEmissaoDt.setAfixacaoEdital("1");
					guiaEmissaoDt.setLeilaoQuantidade("1");
					guiaEmissaoDt.setAvaliadorQuantidade("1");
					guiaEmissaoDt.setPartidorQuantidade("1");
					guiaEmissaoDt.setPenhoraQuantidade("1");
					guiaEmissaoDt.setDistribuidorQuantidade("1");
					guiaEmissaoDt.setDistribuidorQuantidade("1");
			}
		}
		
	
	}
	
	/**
	 * Mï¿½todo para calcular os itens da guia.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaCustaModeloDt
	 * @param Map valoresReferenciaCalculo
	 * @param String reducao50Porcento
	 * @param String citacaoHoraCerta
	 * @param String foraHorarioNormal
	 * 
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 */
	public List calcularItensGuiaGenerica(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaCustaModeloDt, Map valoresReferenciaCalculo, String reducao50Porcento, String citacaoHoraCerta, String foraHorarioNormal) throws Exception {
		List listaGuiaItemDt = null;

		
		this.getGuiaCalculoNe().setCustasQuantitativas(custasQuantitativas);
		listaGuiaItemDt = this.getGuiaCalculoNe().calcularItensGuiaGenerica(guiaEmissaoDt, listaGuiaCustaModeloDt, valoresReferenciaCalculo);
		
		//Retirar itens com quantidade zerados
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for( int i = 0; i < listaGuiaItemDt.size();) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
				
				if( Funcoes.StringToInt(guiaItemDt.getQuantidade()) == 0 ) {
					listaGuiaItemDt.remove(i);
					if( i == 0) {
						i = 0;
					}
					else {
						i--;
					}
				}
				else {
					i++;
				}
			}
		}
	
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para calcular os itens da guia com rateio.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiaItemDt
	 * @param Double porcentagem
	 * 
	 * @return List listaGuiaItemDt
	 * @throws Exception
	 */
	public List calcularItensGuiaRateio(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, Double porcentagem) throws Exception {
		List listaAuxGuiaItemDt = null;
		
		listaAuxGuiaItemDt = guiaCalculoNe.calcularItensGuiaRateio(guiaEmissaoDt, listaGuiaItemDt, porcentagem);
				
		return listaAuxGuiaItemDt;
	}
	
	public List calcularParcelasGuia(List listaGuiaItemDt, int quantidadeParcelas, boolean calcularTodosItens, int parcelaCorrente) throws Exception {
		List listaAuxGuiaItemDt = null;
		
		listaAuxGuiaItemDt = guiaCalculoNe.calcularParcelasGuia(listaGuiaItemDt, quantidadeParcelas, calcularTodosItens, parcelaCorrente);
		
		return listaAuxGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para adicionar o item de cï¿½digo de regimento.
	 * @param String codigoRegimento
	 * @return GuiaCustaModeloDt GuiaCustaModeloDt
	 * @throws Exception
	 */
	public GuiaCustaModeloDt adicionarItemCodigoRegimento(String codigoRegimento) throws Exception {
		GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
		
		custaNe = new CustaNe();
		CustaDt custaDt = null;
		
		custaDt = custaNe.consultarItemGuiaPorCodigoRegimento(codigoRegimento);
		
		guiaCustaModeloDt.setCustaDt(custaDt);
		
		return guiaCustaModeloDt;
	}
	
	/**
	 * Mï¿½todo que consulta o cï¿½lculo e seus itens trazendo o cï¿½digo de arrecadaï¿½ï¿½o e sua descriï¿½ï¿½o para a apresentaï¿½ï¿½o na guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List<GuiaCustaModeloDt>
	 * @throws Exception
	 */
	public List<GuiaCustaModeloDt> consultarItensGuia(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt, String idGuiaTipo, String idProcessoTipo) throws Exception {
		
		List<GuiaCustaModeloDt> listaGuiaCustaModelo = null;
		
		//Consulta itens do cï¿½lculo especï¿½fico
		GuiaCustaModeloNe guiaCustaModeloNe = new GuiaCustaModeloNe();
		listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaProcessoTipo(obFabricaConexao, idGuiaTipo, idProcessoTipo);
		
		return listaGuiaCustaModelo;
	}
	
	/**
	 * Mï¿½todo que consulta o cï¿½lculo e seus itens trazendo o cï¿½digo de arrecadaï¿½ï¿½o e sua descriï¿½ï¿½o para a apresentaï¿½ï¿½o na guia.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List<GuiaCustaModeloDt>
	 * @throws Exception
	 */
	public List<GuiaCustaModeloDt> consultarItensGuiaNovoRegimento(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		
		List<GuiaCustaModeloDt> listaGuiaCustaModelo = null;
		
		//Consulta itens do cï¿½lculo especï¿½fico
		GuiaCustaModeloNe guiaCustaModeloNe = new GuiaCustaModeloNe();
		listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaProcessoTipoNovoRegimento(obFabricaConexao, idGuiaTipo, idProcessoTipo);
		
		return listaGuiaCustaModelo;
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
	public List<GuiaCustaModeloDt> consultarItensGuiaCustaModeloDtNaturezaSPG(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt, String idGuiaTipo, String idNaturezaSPG) throws Exception {
		
		List<GuiaCustaModeloDt> listaGuiaCustaModelo = null;
		
		//Consulta itens do cálculo específico
		GuiaCustaModeloNe guiaCustaModeloNe = new GuiaCustaModeloNe();
		listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaNaturezaSPG(obFabricaConexao, idGuiaTipo, idNaturezaSPG);
		
		return listaGuiaCustaModelo;
	}
	
	public List<CustaDt> consultarItensGuiaNaturezaSPG(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt, String idGuiaTipo, String idNaturezaSPG) throws Exception {
		
		List<CustaDt> listaCustaDt = null;
		
		//Consulta itens do cálculo específico
		GuiaCustaModeloNe guiaCustaModeloNe = new GuiaCustaModeloNe();
		List listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaNaturezaSPG(obFabricaConexao, idGuiaTipo, idNaturezaSPG);
		if( listaGuiaCustaModelo != null ){
			for(int i = 0; i < listaGuiaCustaModelo.size(); i++) {
				if( listaCustaDt == null ) {
					listaCustaDt = new ArrayList();
				}
				GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt)listaGuiaCustaModelo.get(i);
				
				listaCustaDt.add( guiaCustaModeloDt.getCustaDt() );
			}
		}
		
		return listaCustaDt;
	}
	
	/**
	 * Mï¿½todo para consultar os itens de custa para o cï¿½lculo
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return List<CustaDt>
	 * @throws Exception
	 */
	public List consultarItensGuia(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		List listaCustaDt = null;
		

		GuiaCustaModeloNe guiaCustaModeloNe = new GuiaCustaModeloNe();
		List listaGuiaCustaModelo = guiaCustaModeloNe.consultarItensGuiaProcessoTipo(obFabricaConexao, idGuiaTipo, idProcessoTipo);
		if( listaGuiaCustaModelo != null ){
			for(int i = 0; i < listaGuiaCustaModelo.size(); i++) {
				if( listaCustaDt == null ) {
					listaCustaDt = new ArrayList();
				}
				GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt)listaGuiaCustaModelo.get(i);
				
				listaCustaDt.add( guiaCustaModeloDt.getCustaDt() );
			}
		}
	
		
		return listaCustaDt;
	}
	
	/**
	 * Mï¿½todo para consultar os itens de custa para o cï¿½lculo
	 * @param String idGuiaTipo
	 * @param String idFinalidade
	 * @return List<CustaDt>
	 * @throws Exception
	 */
	public List consultarItensGuiaFinalidade(String idGuiaTipo, String idFinalidade) throws Exception {
		List listaCustaDt = null;
		
		GuiaFinalidadeModeloCustaNe guiaCustaFinalidadeModeloNe = new GuiaFinalidadeModeloCustaNe();
		List listaGuiaCustaModelo = guiaCustaFinalidadeModeloNe.consultarItensGuia(idGuiaTipo, idFinalidade);
		if( listaGuiaCustaModelo != null ){
			for(int i = 0; i < listaGuiaCustaModelo.size(); i++) {
				if( listaCustaDt == null ) {
					listaCustaDt = new ArrayList();
				}
				GuiaFinalidadeModeloCustaDt guiaCustaModeloDt = (GuiaFinalidadeModeloCustaDt)listaGuiaCustaModelo.get(i);
				
				listaCustaDt.add( guiaCustaModeloDt.getCustaDt() );
			}
		}
		
		return listaCustaDt;
	}
	
	/**
	 * Mï¿½todo para retornar lista de itens da guia.
	 * @param String idGuiaEmissao
	 * @param String idGuiaTipo
	 * @param FabricaConexao obFabricaConexao
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarGuiaItens(String idGuiaEmissao, String idGuiaTipo) throws Exception {
		FabricaConexao obFabricaConexao = null;
    	
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    		return this.consultarGuiaItens(idGuiaEmissao, idGuiaTipo, obFabricaConexao);
    	}
    	finally{
    		obFabricaConexao.fecharConexao();
    	}
	}
	
	/**
	 * Mï¿½todo para retornar lista de itens da guia.
	 * @param String idGuiaEmissao
	 * @param String idGuiaTipo
	 * @return List<GuiaItemDt>
	 * @throws Exception
	 */
	public List<GuiaItemDt> consultarGuiaItens(String idGuiaEmissao, String idGuiaTipo, FabricaConexao obFabricaConexao) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt = null;
		LocomocaoNe locomocaoNe = new LocomocaoNe();

		if( guiaItemNe == null ) guiaItemNe = new GuiaItemNe();
		
		listaGuiaItemDt = guiaItemNe.consultarItensGuia(idGuiaEmissao, obFabricaConexao);
		
		if( idGuiaTipo != null && idGuiaTipo.length() > 0 && listaGuiaItemDt != null && listaGuiaItemDt.size() > 0) {
			//Vários tipos de guia possuem locomoção, por exemplo a inicial, complementar, etc...
			for( int k = 0; k < listaGuiaItemDt.size(); k++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(k);
				LocomocaoDt locomocaoDtAnterior = guiaItemDt.getLocomocaoDt();
				LocomocaoDt locomocaoDt = locomocaoNe.consultarIdGuiaItem(guiaItemDt.getId(), obFabricaConexao);
				if( locomocaoDt != null ) {
					guiaItemDt.setLocomocaoDt( locomocaoDt );
				}
				else {
					guiaItemDt.setLocomocaoDt( locomocaoNe.consultarLocomocaoDeItemPenhora_Avaliacao(guiaItemDt.getId()) );
				}
				
				if(guiaItemDt.getLocomocaoDt() != null && locomocaoDtAnterior != null) {
					if (locomocaoDtAnterior.getBairroDt() != null) {
						guiaItemDt.getLocomocaoDt().setBairroDt(locomocaoDtAnterior.getBairroDt());
					}
					if (locomocaoDtAnterior.getZonaDt() != null) {
						guiaItemDt.getLocomocaoDt().setZonaDt(locomocaoDtAnterior.getZonaDt());
					}
					if (locomocaoDtAnterior.getRegiaoDt() != null) {
						guiaItemDt.getLocomocaoDt().setRegiaoDt(locomocaoDtAnterior.getRegiaoDt());
					}
				}
			}
		}
		
		//Para guias genï¿½ricas
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for( int m = 0; m < listaGuiaItemDt.size(); m++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(m);
				
				if( guiaItemDt.getId_ArrecadacaoCustaGenerica() != null && guiaItemDt.getId_ArrecadacaoCustaGenerica().length() > 0 ) {
					
					CustaDt custaDt = guiaItemDt.getCustaDt();
					
					ArrecadacaoCustaDt arrecadacaoCustaDt = new ArrecadacaoCustaNe().consultarId(guiaItemDt.getId_ArrecadacaoCustaGenerica(), obFabricaConexao);
					custaDt.setArrecadacaoCusta(arrecadacaoCustaDt.getArrecadacaoCusta());
					custaDt.setCodigoArrecadacao(arrecadacaoCustaDt.getCodigoArrecadacao());
					
					guiaItemDt.setCustaDt(custaDt);
				}
				
				if (guiaItemDt.isLocomocaoParaOficialDeJustica() && guiaItemDt.getLocomocaoDt() == null) {
					guiaItemDt.setLocomocaoDt(locomocaoNe.consultarIdGuiaItem(guiaItemDt.getId(), obFabricaConexao));
				}
			}
		}
		
		//regras para nomes de itens
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {
				
				if( guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null ) {
					
					String nomeTratado = this.alterarNomeCustaParaImpressao( guiaItemDt.getCustaDt().getId() );
					
					if( nomeTratado != null ) {
						guiaItemDt.getCustaDt().setArrecadacaoCusta(nomeTratado);
					}
					
				}
			}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para consulta de lista de itens para o cï¿½lculo pela lista de ids de custaDt.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaCustaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuiaCustaDt(GuiaEmissaoDt guiaEmissaoDt, List listaCustaDt) throws Exception {
		
		List listaGuiaCustaModelo = null;
		
		custaNe = new CustaNe();
		listaGuiaCustaModelo = custaNe.consultarDescricao(listaCustaDt);
		
		return listaGuiaCustaModelo;
	}
	
	/**
	 * Mï¿½todo para verificar se estï¿½ pago na base do projudi, e em seguida nos bancos conveniados.
	 * @param String numeroGuia
	 * @return
	 * @throws Exception
	 */
	public boolean consultarGuiaPagaBancos(String numeroGuia) throws Exception {
		boolean retorno = false;
		
		//Consulta se guia paga no base do projudi.
		retorno = this.isGuiaPaga(numeroGuia);
		
		return retorno;
	}
	
	/**
	 * Metodo para verificar se a guia esta pago na base do projudi, e em seguida nos bancos conveniados.
	 * @param String numeroGuia
	 * @param FabricaConexao obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public boolean consultarGuiaPagaBancos(String numeroGuia, FabricaConexao obFabricaConexao) throws Exception {
		boolean retorno = false;
		
		//Consulta se guia paga no base do projudi.
		retorno = this.isGuiaPaga(numeroGuia, obFabricaConexao);
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para ler arquivos consolidados(ï¿½nico no dia) de todos os bancos juntos.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void atualizacaoGuiasArquivosConsolidados() throws Exception {
		String nomeArquivo = null;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			File file = new File(CAMINHO_ARQUIVOS_CONSOLIDADOS);

			File ifile[] = file.listFiles();
			
			if( ifile != null ) {
				
				obFabricaConexao.iniciarTransacao();
				
				for( int i = 0; i < ifile.length; i++ ) {
					
					nomeArquivo = ifile[i].getName();
					
					boolean arquivoLido = false;
					
					if( !nomeArquivo.contains(".LIDO") ) {
						
						if( nomeArquivo.length() > 20 ) {
							//*****************************************************
							SAXBuilder builder = new SAXBuilder();
							
							Document doc = null;
							try {
								doc = builder.build(ifile[i]);
							}
							catch( Exception e ) {
								doc = null;
							}
							
							if( doc != null ) {
								Element dadosGuia = doc.getRootElement();
								List rows = dadosGuia.getChildren("guia");
								
								if( rows != null && rows.size() > 0 ) {
									for (int k = 0; k < rows.size(); k++) {
										Element guia = (Element) rows.get(k);
										
										if( guia != null ) {
											String numeroGuia = guia.getChild("numeroGuia").getText();
											String dataPagamento = guia.getChild("dataPagamento").getText();
											
											Date dataPagamentoDate = new Date();
											
											int stAno = Funcoes.StringToInt(dataPagamento.substring(4, 8));
											int stMes = Funcoes.StringToInt(dataPagamento.substring(2, 4));
											int stDia = Funcoes.StringToInt(dataPagamento.substring(0, 2));
											
											dataPagamentoDate.setDate(stDia);
											dataPagamentoDate.setMonth(stMes-1);
											dataPagamentoDate.setYear(stAno-1900);
											
											atualizaGuiaConsolidado(obFabricaConexao, numeroGuia, "", new TJDataHora(dataPagamentoDate));
										}
									}
								}
							}
							
							arquivoLido = true;
							
							//*****************************************************
						}
					}
					
					if( arquivoLido ) {
						ifile[i].renameTo(new File(CAMINHO_ARQUIVOS_CONSOLIDADOS + nomeArquivo + ".LIDO"));
					}
				}
				
				obFabricaConexao.finalizarTransacao();
				
			}
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public boolean isStatusGuiaRessarcidoPedidoRessarcido(GuiaEmissaoDt guia) {
		switch (Integer.parseInt(guia.getId_GuiaStatus())){
			case GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO:
			case GuiaStatusDt.RESSARCIDO:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isStatusGuiaPaga(GuiaEmissaoDt guia) {
		switch (Integer.parseInt(guia.getId_GuiaStatus())){
			case GuiaStatusDt.PAGO:
			case GuiaStatusDt.PAGO_ON_LINE:
			case GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA:
			case GuiaStatusDt.PAGO_COM_VALOR_INFERIOR :
			case GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR:
			case GuiaStatusDt.PAGO_APOS_VENCIMENTO:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isStatusCancelada(GuiaEmissaoDt guia) {
		switch (Integer.parseInt(guia.getId_GuiaStatus())){
			case GuiaStatusDt.CANCELADA:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isStatusCanceladaPaga(GuiaEmissaoDt guia) {
		switch (Integer.parseInt(guia.getId_GuiaStatus())){
			case GuiaStatusDt.CANCELADA_PAGA:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isStatusAguardandoPagamento(GuiaEmissaoDt guia) {
		switch (Integer.parseInt(guia.getId_GuiaStatus())){
			case GuiaStatusDt.AGUARDANDO_PAGAMENTO:
			case GuiaStatusDt.ESTORNO_BANCARIO:				
				return true;
			default:
				return false;
		}
	}
	
	public void atualizaGuiaConsolidado(FabricaConexao obFabricaConexao, String numeroGuia, String idProcesso, TJDataHora dataPagamento) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuia, obFabricaConexao);
		atualizaGuiaConsolidado(obFabricaConexao, guiaEmissaoDt, idProcesso, dataPagamento);
		
		alterarStatusGuiaParcelada(guiaEmissaoDt, obFabricaConexao);
	}

	public void atualizaGuiaConsolidado(FabricaConexao obFabricaConexao, GuiaEmissaoDt guia, String idProcesso, TJDataHora dataPagamento) throws Exception {
		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		Integer idGuiaStatus = GuiaStatusDt.PAGO;

		if(!isStatusGuiaRessarcidoPedidoRessarcido(guia) && !isStatusGuiaPaga(guia) ) {
					
			boolean atualizado = false;
			
			if( isStatusCancelada(guia) ) {
				idGuiaStatus = GuiaStatusDt.CANCELADA_PAGA;
			}
			if( isStatusAguardandoPagamento(guia) ) {
				idGuiaStatus = GuiaStatusDt.PAGO;
			}
			
			Long longDataPagamento = dataPagamento.getDate().getTime();
			Date dataVencimentoGuia = obPersistencia.consultarDataVencimento(guia.getNumeroGuiaCompleto());
			dataVencimentoGuia.setHours( 23 );
			dataVencimentoGuia.setMinutes( 59 );
			dataVencimentoGuia.setSeconds( 59 );
			Long longDataVencimento = dataVencimentoGuia.getTime();
			if( longDataVencimento < longDataPagamento ) {
				idGuiaStatus = GuiaStatusDt.PAGO_APOS_VENCIMENTO;
			}
			
			atualizado = obPersistencia.atualizarPagamento(guia.getNumeroGuiaCompleto(), idGuiaStatus, dataPagamento.getDate());
			
			//**********
			//Gerar a movimentação
			if( atualizado ) {					
				//Registrando log da alteração do pagamento
				GuiaEmissaoDt guiaAtualizada = obPersistencia.consultarId(guia.getId());
				LogDt logDt = new LogDt("GuiaEmissao", guia.getId(), UsuarioDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Alterar), guia.getPropriedades(), guiaAtualizada.getPropriedades());
				new LogNe().salvar(logDt, obFabricaConexao);
				
				//E-Carta
				//Vincular item de postagem
				if( guiaAtualizada != null && guiaAtualizada.getId_Processo() != null && !guiaAtualizada.getId_Processo().isEmpty() ) {
					if( new GuiaItemNe().guiaPossuiItemDespesaPostal(guiaAtualizada.getId_Processo(), obFabricaConexao) ) {
						new CorreiosNe().vincularItemGuiaPostagem(idProcesso, obFabricaConexao);
					}
				}

				//Gerar a pendencia
				this.gerarPendenciaGuiaPaga(guia, idProcesso, obFabricaConexao);
				
				//Guia Complementar: Processo de alteração dos dados do processo
				this.alterarDadosProcessoViaNumeroGuiaComplementar(guia, obFabricaConexao);
			}
		}
	}
	
	/**
	 * Mï¿½todo para ler arquivos da caixa econï¿½mica.
	 * Tipos de registro:
	 * Registro Header de Arquivo (Tipo = 0)
	 * Registro Header de Lote (Tipo = 1)
	 * Registros de detalhe Segmentos (Tipo = 3) -> Segmento T
	 * Registro trailer de lote (Tipo = 5)
	 * Registro trailer de arquivo (Tipo = 9)
	 * @throws Exception
	 */
	public void atualizacaoGuiasCaixa() throws Exception {
		ArquivoBancoNe arquivoBancoNe = new ArquivoBancoNe();
		String nomeArquivo = null;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			File file = new File(CAMINHO_ARQUIVOS_CAIXA);

			File ifile[] = file.listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".ret");
			    }
			});
			
			if( ifile != null ) {
				
				obFabricaConexao.iniciarTransacao();
				
				for( int i = 0; i < ifile.length; i++ ) {
					
					nomeArquivo = ifile[i].getName();
					if(nomeArquivo != null && !nomeArquivo.contains(".LIDO") && !nomeArquivo.contains(".INVA")) {
						
						if( nomeArquivo.length() > 0 ) {
							String sufixoExtensao = ".LIDO";
							if(!arquivoBancoNe.isArquivoJaLido(nomeArquivo)) {								
								if (arquivoCaixaIsValido(nomeArquivo)) {
									
									BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo), "Cp1252"));//new BufferedReader(new FileReader(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo));
									GerenciaArquivo gerenciaArquivo = new GerenciaArquivoCEF(obFabricaConexao);
									
									while (in.ready() ) {
										gerenciaArquivo.lerDados(in.readLine());									
									}
									
									in.close();
									
									arquivoBancoNe.salvarArquivoLido(nomeArquivo, BancoDt.CAIXA_ECONOMICA_FEDERAL, ArquivoBancoDt.ARQUIVO_LIDO_COM_SUCESSO, null, Funcoes.leiaArquivoCompleto(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo, Charset.defaultCharset()));	
								} else {
									sufixoExtensao = ".INVA";
									arquivoBancoNe.salvarArquivoLido(nomeArquivo, BancoDt.CAIXA_ECONOMICA_FEDERAL, ArquivoBancoDt.ARQUIVO_LIDO_COM_ERRO_QTDE_REGISTROS, null, Funcoes.leiaArquivoCompleto(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo, Charset.defaultCharset()));									
								}									
							} else {
								sufixoExtensao = ".JALIDO";
							}
							File fileOriginal = new File(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo);
							File fileRename = new File(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo + sufixoExtensao);
							
							fileOriginal.renameTo(fileRename);
						}
					}
				}
				
				obFabricaConexao.finalizarTransacao();				
			}
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			
			//Caso dê erro na leitura do arquivo
			if( nomeArquivo != null && nomeArquivo.length() > 0 ) {
				arquivoBancoNe.salvarArquivoLido(nomeArquivo, BancoDt.CAIXA_ECONOMICA_FEDERAL, ArquivoBancoDt.ARQUIVO_LIDO_COM_ERRO, Funcoes.obtenhaConteudoExcecao(e), null);
			}
			
			throw e;
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	private boolean arquivoCaixaIsValido(String nomeArquivo) throws Exception {
		long quantidadeDeRegistrosNoDetalhe = 0;
		long quantidadeDeRegistrosNoRodape = 0;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo), "Cp1252"));//new BufferedReader(new FileReader(CAMINHO_ARQUIVOS_CAIXA + nomeArquivo));
		
		while ( in.ready() ) {
			String linha = in.readLine();
			if(linha != null && linha.length() == LeiauteEscritaArquivoCEF.TAMANHO_LINHA) {
				int tipoDeRegistro = Funcoes.StringToInt(linha.substring(LeiauteEscritaArquivoCEF.INICIO_TIPO_REGISTRO, LeiauteEscritaArquivoCEF.FIM_TIPO_REGISTRO).trim(), -1); 
				if (tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_HEADER_ARQUIVO || 
					tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_HEADER_LOTE || 
					tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_DETALHE || 
					tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_TRAILER_LOTE || 
					tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_TRAILER_ARQUIVO) {
					quantidadeDeRegistrosNoDetalhe += 1;
				} 
				if (tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_TRAILER_ARQUIVO) {
					quantidadeDeRegistrosNoRodape = Funcoes.StringToLong(linha.substring(23, 29).trim(), 0);
				}
			}
		}
		
		in.close();
		
		return quantidadeDeRegistrosNoDetalhe == quantidadeDeRegistrosNoRodape;
	}
	
	/**
	 * Método para gerar pendencia de guia paga.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao
	 * 
	 * @throws Exception
	 */
	public void gerarPendenciaGuiaPaga(GuiaEmissaoDt guiaEmissaoDt, String idProcesso, FabricaConexao conexao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( conexao == null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}else{
				obFabricaConexao = conexao;
			}
			
			if (idProcesso != null && idProcesso.trim().length() > 0) guiaEmissaoDt.setId_Processo(idProcesso);
			
			if( guiaEmissaoDt != null && guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
				
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(guiaEmissaoDt.getId_Processo());
				processoDt.setId_Serventia(new ProcessoNe().consultarIdServentiaProcesso(guiaEmissaoDt.getId_Processo(), obFabricaConexao));
				
				LogDt obLogDt = new LogDt();
				obLogDt.setId_Usuario(UsuarioDt.SistemaProjudi);
				obLogDt.setIpComputador("Servidor");
				
				//isso esta no salvar
				PendenciaNe pendenciaNe = new PendenciaNe();
				pendenciaNe.gerarPendenciaVerificarGuiaPaga(processoDt, UsuarioDt.SistemaProjudi, processoDt.getId_Serventia(), "", null, null, obLogDt, obFabricaConexao);
			}
		} finally {
			if( conexao == null ) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Mï¿½todo para atualizar o pagamento do nï¿½mero da guia.
	 * Se o parï¿½metro dataPagamento for null serï¿½ inserido a data com a funï¿½ï¿½o NOW()
	 * @param String numeroGuiaCompleto
	 * @param Integer idGuiaStatus
	 * @param Date dataPagamento //Se for null serï¿½ inserido a data com a funï¿½ï¿½o NOW()
	 * @return boolean
	 * @throws Exception
	 */
	public boolean atualizarPagamento(String numeroGuiaCompleto, Integer idGuiaStatus, Date dataPagamento) throws Exception {
		boolean retorno = false;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			retorno = atualizarPagamento(numeroGuiaCompleto, idGuiaStatus, dataPagamento, obFabricaConexao);
		} finally {
			if( obFabricaConexao != null ) {
			obFabricaConexao.fecharConexao();
		}
		}
		
		return retorno;
	}
	
	/**
	 * Metodo para atualizar o pagamento do numero da guia.
	 * Se o parametro dataPagamento for null sera inserido a data com a funcao NOW()
	 * @param String numeroGuiaCompleto
	 * @param Integer idGuiaStatus
	 * @param Date dataPagamento //Se for null sera inserido a data com a funcao NOW()
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean atualizarPagamento(String numeroGuiaCompleto, Integer idGuiaStatus, Date dataPagamento, FabricaConexao obFabricaConexao) throws Exception {		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
		return obPersistencia.atualizarPagamento(numeroGuiaCompleto, idGuiaStatus, dataPagamento);		
	}
	
	/**
	 * Metodo para atualizar o pagamento do numero da guia.
	 * Se o parametro dataPagamento for null sera inserido a data com a funcao NOW()
	 * @param String numeroGuiaCompleto
	 * @param Integer idGuiaStatus
	 * @param Date dataPagamento //Se for null sera inserido a data com a funcao NOW()
	 * @param Date dataMovimento (Data de Baixa / Repasse do Banco) //Se for null sera inserido a data com a funcao NOW()
	 * @param double valorPagamento
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean atualizarPagamento(String numeroGuiaCompleto, Integer idGuiaStatus, Date dataPagamento, Date dataMovimento, String valorPagamento, FabricaConexao obFabricaConexao) throws Exception {		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
		return obPersistencia.atualizarPagamento(numeroGuiaCompleto, idGuiaStatus, dataPagamento, dataMovimento, valorPagamento);		
	}
	
	/**
	 * Mï¿½todo para veriicar se a guia estï¿½ cancelada ou nï¿½o.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaCancelada(String numeroGuiaCompleto, FabricaConexao fabConexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			// Verifica se a conexao sera criada internamente
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}
			else {
				// Caso da conexao criada em um nivel superior
				obFabricaConexao = fabConexao;
			}
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isGuiaCancelada(numeroGuiaCompleto);
		} finally {
			if( fabConexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para veriicar se a guia estï¿½ cancelada e paga ou nï¿½o.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaCanceladaPaga(String numeroGuiaCompleto, FabricaConexao fabConexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			// Verifica se a conexao sera criada internamente
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}
			else {
				// Caso da conexao criada em um nivel superior
				obFabricaConexao = fabConexao;
			}
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isGuiaCanceladaPaga(numeroGuiaCompleto);
		} finally {
			if( fabConexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se a guia está cancelada ou não, independente de pagamento.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean verificarGuiaCanceladaIndependentePagamento(String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		return retorno = this.verificarGuiaCanceladaIndependentePagamento(numeroGuiaCompleto, null);
	}
	
	/**
	 * Método para verificar se a guia está cancelada ou não, independente de pagamento.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean verificarGuiaCanceladaIndependentePagamento(String numeroGuiaCompleto, FabricaConexao fabConexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			// Verifica se a conexao sera criada internamente
			if (fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			}
			else {
				// Caso da conexao criada em um nivel superior
				obFabricaConexao = fabConexao;
			}
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isGuiaCanceladaIndependentePagamento(numeroGuiaCompleto);
		} finally {
			if( fabConexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para verificar se o nï¿½mero da guia ï¿½ de guia do projudi.
	 * Recebe o nï¿½mero da guia completo com o nï¿½mero + dï¿½gito + sï¿½rie
	 * 
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isNumeroGuiaProjudi(String numeroGuia) throws Exception {
		boolean retorno = false;
		
		int tamanhoNumeroGuia = numeroGuia.length();
		
		numeroGuia = numeroGuia.trim().replaceAll("/", "").replaceAll("-", "").replaceAll("\\.", "").replaceAll(" ", "");
		
		String numero = numeroGuia.substring( 0, tamanhoNumeroGuia - 3 );
		String digito = numeroGuia.substring( tamanhoNumeroGuia - 3, (tamanhoNumeroGuia - 3)+1 );
		String serie = numeroGuia.substring( tamanhoNumeroGuia - 2, tamanhoNumeroGuia );
		
		String digitoValidar = algoritmoDigitoVerificadorNumeroGuia( numero );
		
		//valida se ï¿½ guia do projudi
		if( serie.equals("50") && digito.equals(digitoValidar) && numeroGuia.equals(numero + digitoValidar + serie) ) {
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para validar se numero da guia é da série desejada.
	 * @param String serieDesejada
	 * @param String numeroGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isSerieGuia(String serieDesejada, String numeroGuia) throws Exception {
		boolean retorno = false;
		
		if( serieDesejada != null && numeroGuia != null ) {
			int tamanhoNumeroGuia = numeroGuia.length();
			
			String serie = numeroGuia.substring( tamanhoNumeroGuia - 2, tamanhoNumeroGuia );
			
			//valida se é guia série ${serieDesejada}
			if( serie.equals(serieDesejada) ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para salvar a guia complementar. Decidi criar este método separado para não fazer a lógico do if 
	 * ser executada por todas as guias.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List<GuiaItemDt> listaGuiaItemDt
	 * @param boolean gerarPendencia
	 * @param String idUsuarioServentia
	 * @throws Exception
	 */
	public void salvarGuiaComplementar(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, boolean gerarPendencia, UsuarioDt usuarioDt) throws Exception {

		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			obFabricaConexao.iniciarTransacao();
			
			if (guiaEmissaoDt.getId_Processo() != null && this.isProcessoPossuiGuiaEnviadaCadin(guiaEmissaoDt.getId_Processo())) {
				throw new MensagemException("Impossível gerar essa guia, pois esse processo já possui débito enviado ao <b>CADIN/SEFAZ</b>! <br /> Para mais informações: <b>(62) 3213-1581</b> TELEJUDICIÁRIO!");
			}
			
			//*****************
			//Guia complementar:
			//Caso a guia complementar seja emitida com o valor zerado, é para alterar a classe e/ou valor da causa
			if( guiaEmissaoDt.getGuiaModeloDt() != null && 				
				(	
					guiaEmissaoDt.getGuiaModeloDt().isIdGuiaTipo( GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU )
					||
					guiaEmissaoDt.getGuiaModeloDt().isIdGuiaTipo( GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU )
				) &&
				guiaEmissaoDt.getNumeroGuiaCompleto() != null && 
				!guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty()
				) {
				
				if( this.isGuiaZeradaOuNegativa() ) {
					
					if (usuarioDt.isAdvogado()) {
						throw new MensagemException("Impossível emitir guia complementar com valor zero, favor solicitar a emissão pelo cartório.");
					}
					
					guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
					
					this.alterarDadosProcessoViaGuiaEmissaoDtComplementar(guiaEmissaoDt, obFabricaConexao);
				}
				
			}
			//*****************
			
			salvar(guiaEmissaoDt, listaGuiaItemDt, gerarPendencia, usuarioDt.getId_UsuarioServentia(), obFabricaConexao);
			
			//TODO repasse : Em conversa com o márcio, verifiquei se terei prejuízo neste processo ao retirar o cadastro de repasse para estes casos de sincronizar a guia no SPG
			//A principio não tem prejuízo, então vou comentar este trecho para evitar o problema que ocorreu na ocorrência 2018/15955
			//O problema foi o sequinte: Uma ISN foi encontrada em duas guias no capital e interior. E dentro deste método estava consultando primeiro o repasse pelo ISN. Logo encontrando no capital não consultava no 
			//interior, então a guia que era do interior ficava com o cadastro do repasse errado.
			//insereDataDeApresentacaoEInfoRepasseSemData(guiaEmissaoDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			guiaEmissaoDt.setId("");
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally{
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}

//	//TODO repasse : Comentei a chamada desse método no momento da sincronização da guia entre SPG e PJD.
//	private void insereDataDeApresentacaoEInfoRepasseSemData(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexao) throws Exception {
//		if (guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
//			
//			String codigoComarcaoServentia = null;
//			
//			if (guiaEmissaoDt.getId_Serventia() != null && guiaEmissaoDt.getId_Serventia().trim().length() > 0) {
//				ServentiaDt serventiaDt = new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia(), obFabricaConexao);
//				if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null && serventiaDt.getServentiaCodigoExterno().length() > 0) 
//					codigoComarcaoServentia = Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),3);			
//			} 
//			
//			if (codigoComarcaoServentia == null && guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
//				ProcessoDt processoDt = new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo(), obFabricaConexao);
//				if (processoDt != null) {
//					ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia());
//					if (serventiaDt != null && serventiaDt.getServentiaCodigoExterno() != null && serventiaDt.getServentiaCodigoExterno().length() > 0) 
//						codigoComarcaoServentia = Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),3);
//				}
//			}
//			
//			if (codigoComarcaoServentia != null && codigoComarcaoServentia.trim().length() > 0) {
//				GuiaSPGNe guiaSPGNe = new GuiaSPGNe(); 
//				GuiaEmissaoDt guiaSPG = guiaSPGNe.consultarGuiaEmissaoSPG(guiaEmissaoDt.getNumeroGuiaCompleto());
//				
//				if (guiaSPG != null) {
//					//TODO repasse : Corrigindo o valor da comarca codigo para não errar no momento do cadastro no SPG. Mas comentei a chamada desse método todo na área de sincronizar. Não será mais chamado na sincronização da guia entre o SPG e PJD.
//					//guiaSPGNe.inserirGuiaInfoRepasse(guiaSPG.getId(), codigoComarcaoServentia, null, guiaEmissaoDt.getComarcaCodigo(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog());
//					guiaSPGNe.inserirGuiaInfoRepasse(guiaSPG.getId(), codigoComarcaoServentia, null, guiaSPG.getComarcaCodigo(), guiaEmissaoDt.getNumeroGuiaCompleto(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog());
//					
//					//Atualiza data de apresentação no SPG para que a rotina da financeira atualize o repasse corretamente.
//					//TODO repasse : Corrigindo o valor da comarca codigo para não errar no momento do cadastro no SPG. Mas comentei a chamada desse método todo na área de sincronizar. Não será mais chamado na sincronização da guia entre o SPG e PJD.
//					//guiaSPGNe.atualizaDataApresentacao(guiaSPG.getId(), guiaEmissaoDt.getComarcaCodigo());
//					guiaSPGNe.atualizaDataApresentacao(guiaSPG.getId(), guiaSPG.getComarcaCodigo());
//				}
//			}
//		}
//	}
		
	/**
	 * Método utilizado somente no homolog para o marcelo testar o pagamento de guias
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String numeroGuia
	 * @param String dataPagamento
	 * @author fasoares Fred
	 * @throws Exception
	 */
	public void simularPagamentoGuia_NAO_UTILIZAR(GuiaEmissaoDt guiaEmissaoDt, String numeroGuia, String dataPagamento) throws Exception {

		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

						
			obFabricaConexao.iniciarTransacao();
			
			//*****************
			//Atualiza Pagamento
			Integer idGuiaStatus = GuiaStatusDt.PAGO;
			
			if( isGuiaCancelada(numeroGuia, obFabricaConexao) ) {
				idGuiaStatus = GuiaStatusDt.CANCELADA_PAGA;
			}
			if( this.isGuiaStatusAguardandoPagamento(numeroGuia, obFabricaConexao) ) {
				idGuiaStatus = GuiaStatusDt.PAGO;
			}
			
			Date dataPagamentoDate = Funcoes.StringToDate(dataPagamento);
			
			Long longDataPagamento = dataPagamentoDate.getTime();
			Date dataVencimentoGuia = this.consultarDataVencimento(numeroGuia);
			dataVencimentoGuia.setHours( 23 );
			dataVencimentoGuia.setMinutes( 59 );
			dataVencimentoGuia.setSeconds( 59 );
			Long longDataVencimento = dataVencimentoGuia.getTime();
			if( longDataVencimento < longDataPagamento ) {
				idGuiaStatus = GuiaStatusDt.PAGO_APOS_VENCIMENTO;
			}
			
			boolean guiaAtualizada = atualizarPagamento(numeroGuia, idGuiaStatus, dataPagamentoDate);
			
			if( guiaAtualizada ) {
				LogDt obLogDt = new LogDt("GUIA_HOMOLOGACAO", guiaEmissaoDt.getId(), UsuarioDt.SistemaProjudi, "homologacao", String.valueOf(LogTipoDt.Alterar), "", "Atualizado pagamento via homologação.");
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			//*****************
			
			//*****************
			//Guia complementar:
			//Caso a guia complementar seja emitida com o valor zerado, é para alterar a classe e/ou valor da causa
			if( guiaEmissaoDt.getGuiaModeloDt() != null && 
				(
					guiaEmissaoDt.getGuiaModeloDt().isIdGuiaTipo(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU)
					||
					guiaEmissaoDt.getGuiaModeloDt().isIdGuiaTipo(GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU)
				) && 
				guiaEmissaoDt.getNumeroGuiaCompleto() != null && 
				!guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {
				
				guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA);
				
				this.alterarDadosProcessoViaGuiaEmissaoDtComplementar(guiaEmissaoDt, obFabricaConexao);
				
			}
			//*****************
			
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			guiaEmissaoDt.setId("");
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally{
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	public void salvar(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, boolean gerarPendencia, String idUsuarioServentia) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);		
		obFabricaConexao.iniciarTransacao();
		
		try {
			
			if( guiaEmissaoDt != null ) {
				if( !guiaEmissaoDt.getId().isEmpty() ) {
					throw new MensagemException("Erro ao emitir guia.");
				}
				
				if( guiaEmissaoDt.isValorAcaoNegativo() ) {
					throw new MensagemException("Valor da Causa não pode ser Negativo.");
				}
				
				if( guiaEmissaoDt.getNumeroGuiaCompleto() != null 
					&& ( guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() 
					|| guiaEmissaoDt.getNumeroGuiaCompleto().equals("0")
					|| guiaEmissaoDt.getNumeroGuiaCompleto().equals("null")
					|| Funcoes.StringToLong(guiaEmissaoDt.getNumeroGuiaCompleto()) == 0L
					) 
				) {
					throw new MensagemException("Número da Guia não gerado. Por favor, realizar o procedimento novamente.");
				}
				
				if( listaGuiaItemDt != null && idUsuarioServentia != null ) {
					salvar(guiaEmissaoDt, listaGuiaItemDt, gerarPendencia, idUsuarioServentia, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			guiaEmissaoDt.setId("");
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	private void salvar(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, boolean gerarPendencia, String idUsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		guiaItemNe = new GuiaItemNe();
		processoNe = new ProcessoNe();
		
		try {
			
			if( guiaEmissaoDt.getGuiaModeloDt() == null || guiaEmissaoDt.getGuiaModeloDt().getId() == null || guiaEmissaoDt.getGuiaModeloDt().getId().length() == 0 ) {
				throw new MensagemException("Erro ao identificar o modelo da guia. Por favor, tente novamente. Caso o problema persista entrar em contato.");
			}
			
			if( guiaEmissaoDt.isValorAcaoNegativo() ) {
				throw new MensagemException("Valor da Causa não pode ser Negativo.");
			}
			
			if( !isGuiaGuiaInicialCartaPrecatoriaItemCorreto1041Precatorios(guiaEmissaoDt, listaGuiaItemDt) ) {
				throw new MensagemException("Guia Inicial Carta Precatória gerada com item errado. Por favor, entrar em contato com o suporte.");
			}
			
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( guiaEmissaoDt.getId_Serventia() != null && guiaEmissaoDt.getId_Serventia().length() > 0 ) {
				guiaEmissaoDt.setId_Comarca(new ServentiaNe().consultarId(guiaEmissaoDt.getId_Serventia(), obFabricaConexao).getId_Comarca());
				guiaEmissaoDt.setComarcaCodigo(new ComarcaNe().consultarId(guiaEmissaoDt.getId_Comarca(), obFabricaConexao).getComarcaCodigo());
			}
			
			obPersistencia.inserir(guiaEmissaoDt);
			obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",guiaEmissaoDt.getPropriedades());

			//Para a guia inicial não precisa, pois a guia ao ser emitida não está vinculada a processo
			if( guiaEmissaoDt.getId_Processo() != null ) {
				guiaEmissaoDt.setProcessoDt(new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo(), obFabricaConexao));
			}
			
			//Salva os itens da guia
			guiaItemNe.salvar(obFabricaConexao, listaGuiaItemDt, guiaEmissaoDt);
			
			enviaGuiaParaCadastroNoSPGSSG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
			
			//Consulta guia no SPG ou SSG para cadastrar o info_repasse
			if( !guiaEmissaoDt.isGuiaCertidaoPraticaForense() ) {
				//Não é guia certidão pratica forense
				if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Serventia() != null ) {
					processoNe.cadastrarInfoRepasseNoCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getId_Processo(), null, guiaEmissaoDt.getId_Serventia(), obLogDt.getId_UsuarioLog(), obLogDt.getIpComputador(), obFabricaConexao, guiaEmissaoDt.getId_Comarca(), false);
				}
			}
			
			obDados.copiar(guiaEmissaoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		catch(Exception e) {
			guiaEmissaoDt.setId("");
			throw e;
		}
	}
	
	public void salvarGuiaCadastroProcesso(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt, boolean gerarPendencia, String idUsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		guiaItemNe = new GuiaItemNe();
		processoNe = new ProcessoNe();
		comarcaNe = new ComarcaNe();
		serventiaNe = new ServentiaNe();
		
		try {
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( guiaEmissaoDt.getId_Serventia() != null && guiaEmissaoDt.getId_Serventia().length() > 0 ) {
				guiaEmissaoDt.setId_Comarca(serventiaNe.consultarId(guiaEmissaoDt.getId_Serventia(), obFabricaConexao).getId_Comarca());
				guiaEmissaoDt.setComarcaCodigo(comarcaNe.consultarId(guiaEmissaoDt.getId_Comarca(), obFabricaConexao).getComarcaCodigo());
			}
			
			if (guiaEmissaoDt.getNumeroGuiaCompleto() != null 
					&& (
					guiaEmissaoDt.getNumeroGuiaCompleto().length() == 0 
					|| guiaEmissaoDt.getNumeroGuiaCompleto().equals("0") 
					|| guiaEmissaoDt.getNumeroGuiaCompleto().equals("null") 
					|| Funcoes.StringToLong(guiaEmissaoDt.getNumeroGuiaCompleto()) == 0L
					)
				) {
				throw new MensagemException("Guia gerada sem número.(Erro Cadastro de Processo)");
			}
			
			obPersistencia.inserir(guiaEmissaoDt);
			obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",guiaEmissaoDt.getPropriedades());

			//Para a guia inicial não precisa, pois a guia ao ser emitida não está vinculada a processo
			if( guiaEmissaoDt.getId_Processo() != null ) {
				guiaEmissaoDt.setProcessoDt(processoNe.consultarIdCompleto(guiaEmissaoDt.getId_Processo(), obFabricaConexao));
			}
			
			//Salva os itens da guia
			guiaItemNe.salvarNOVO(obFabricaConexao, listaGuiaItemDt, guiaEmissaoDt);
			
			enviaGuiaParaCadastroNoSPGSSG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
			
			//Consulta guia no SPG ou SSG para cadastrar o info_repasse
			if( !guiaEmissaoDt.isGuiaCertidaoPraticaForense() ) {
				//Não é guia certidão pratica forense
				if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Serventia() != null ) {
					processoNe.cadastrarInfoRepasseNoCadastroProcesso(guiaEmissaoDt, guiaEmissaoDt.getId_Processo(), null, guiaEmissaoDt.getId_Serventia(), obLogDt.getId_UsuarioLog(), obLogDt.getIpComputador(), obFabricaConexao, guiaEmissaoDt.getId_Comarca(), false);
				}
			}
			
			obDados.copiar(guiaEmissaoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		catch(Exception e) {
			guiaEmissaoDt.setId("");
			throw e;
		}
	}
	
	private void enviaGuiaParaCadastroNoSPGSSG(GuiaEmissaoDt guiaEmissaoDt, List listaGuiaItemDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		
		try {
			if (guiaEmissaoDt.getGuiaModeloDt() != null && guiaEmissaoDt.getGuiaModeloDt().getFlagGrau() != null && guiaEmissaoDt.getGuiaModeloDt().getFlagGrau().trim().length() > 0) {
				
				//Ajusta as locomoções para a central de mandados do SPG.
				if (guiaEmissaoDt.isLocomocaoComplementar() && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt() != null && guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt().size() > 0) {
					
					List<LocomocaoDt> listaTodasLocomocoesNaoUtilizadas = new LocomocaoNe().consultarLocomocaoNaoUtilizada(guiaEmissaoDt.getId_Processo(), true);
					
					if (listaTodasLocomocoesNaoUtilizadas != null && listaTodasLocomocoesNaoUtilizadas.size() > 0) {
						for(LocomocaoDt locomocaoDt : guiaEmissaoDt.getListaLocomocaoNaoUtilizadaDt()) {
							for(LocomocaoDt locomocaoCompletaDt : listaTodasLocomocoesNaoUtilizadas) {
								if (locomocaoCompletaDt != null && locomocaoDt != null && locomocaoCompletaDt.getId().trim().equalsIgnoreCase(locomocaoDt.getId().trim())) {
									locomocaoDt.setLocomocaoSPGDt(locomocaoCompletaDt.getLocomocaoSPGDt());
									break;
								}
							}
						}
					}
				}
				
				//Setar código regimento caso o item for de guia genérica
				if (listaGuiaItemDt != null && listaGuiaItemDt.size() > 0) {
					for(int i = 0; i < listaGuiaItemDt.size(); i++) {
						GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(i);
						//Verifica se tem id_arrecadacao_custa_generica preenchida
						if( guiaItemDt != null && guiaItemDt.getId_ArrecadacaoCustaGenerica() != null && !guiaItemDt.getId_ArrecadacaoCustaGenerica().isEmpty() ) {
							//Verifica se o código arrecadacao não está preenchido para preencher
							if( guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getCodigoArrecadacao() != null && guiaItemDt.getCustaDt().getCodigoArrecadacao().equals("0") ) {
								ArrecadacaoCustaDt arreacadacaoCustaDt = new ArrecadacaoCustaNe().consultarId(guiaItemDt.getId_ArrecadacaoCustaGenerica());
								
								if( arreacadacaoCustaDt != null && arreacadacaoCustaDt.getCodigoArrecadacao() != null && !arreacadacaoCustaDt.getCodigoArrecadacao().isEmpty() ) {
									guiaItemDt.getCustaDt().setCodigoArrecadacao(arreacadacaoCustaDt.getCodigoArrecadacao());
								}
							}
						}
					}
				}
				
				//Envia Guia para Cadastro no SPG
				if(Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.PRIMEIRO_GRAU) {
					GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
					guiaSPGNe.inserirGuiaSPG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
					return;
				}
				else {
					if(Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.SEGUNDO_GRAU) {
						GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
						guiaSSGNe.inserirGuiaSSG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
						return;
					}
				}
			}
		}
		catch(Exception e) {
			throw new MensagemException("Erro ao cadastrar guia no SPG: " + e.getMessage());
		}
		
	}
	
	
	/**
	 * Mï¿½todo para adicionar as guias do Projudi que nï¿½o estï¿½o no SPG.
	 * @param String numeroGuia
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void adicionarGuiasSPG(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			
			
			//GuiaEmissaoDt guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuia);
			if( guiaEmissaoDt != null ) {
				List listaGuiaItemDt = new GuiaItemNe().consultarItensGuia( guiaEmissaoDt.getId() );
				if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
					guiaEmissaoDt.setListaGuiaItemDt(listaGuiaItemDt);
					
					//Consulta se a guia existe no SPG, caso nï¿½o exista cadastra ela
					GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
					GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
					if( Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.PRIMEIRO_GRAU ) {
						guiaSPGNe.inserirGuiaSPG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
					}
					else {
						if( Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.SEGUNDO_GRAU ) {
							guiaSSGNe.inserirGuiaSSG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
						}
					}
					
					boolean isGuiaPresenteSPG = guiaSPGNe.isGuiaPresenteSPG(numeroGuia, guiaEmissaoDt);
					if( !isGuiaPresenteSPG ) {
						guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
						
						if( Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.PRIMEIRO_GRAU ) {
							guiaSPGNe = new GuiaSPGNe();
							guiaSPGNe.inserirGuiaSPG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
						}
						else {
							if( Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.SEGUNDO_GRAU ) {
								guiaSSGNe = new GuiaSSGNe();
								guiaSSGNe.inserirGuiaSSG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
							}
						}
						
					}
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Mï¿½todo para atualizar o tipo da guia no spg.
	 * @param String numeroGuia
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void atualizarTipoGuiaSPG(String numeroGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		guiaSPGNe.atualizarTipoGuiaSPG(numeroGuia, guiaEmissaoDt);
	}
	
	/**
	 * Mï¿½todo Alterar o valor da taxa judiciï¿½ria da guia no spg.
	 * @param String numeroGuia
	 * @param String codigoArrecadacao
	 * @param String valorCorreto
	 * @throws Exception
	 */
	public void atualizarTaxaJudiciariaGuiaSPG(String numeroGuia, String codigoArrecadacao, String valorCorreto, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		guiaSPGNe.atualizarTaxaJudiciariaGuiaSPG(numeroGuia, codigoArrecadacao, valorCorreto, guiaEmissaoDt);
	}
	
	/**
	 * Mï¿½todo para setar nome de ProcessoParteDt.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void consultarNomeProcessoParte(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		
		if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getApelante() == null ) {
			ProcessoParteDt parte = processoParteNe.consultarId(guiaEmissaoDt.getId_Apelante());
			if( parte != null ) {
				guiaEmissaoDt.setApelante(parte.getNome());
			}
		}
		if( guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getApelado() == null ) {
			ProcessoParteDt parte = processoParteNe.consultarId(guiaEmissaoDt.getId_Apelado());
			if( parte != null ) {
				guiaEmissaoDt.setApelado(parte.getNome());
			}
		}
	}
	
	/**
	 * Mï¿½todo para auxiliar para mostrar o label do tipo de rateio.
	 * @param rateioCodigo
	 * @return String
	 */
	public static String getNomeRateio(int rateioCodigo) {
		String retorno = "";
		
		switch(rateioCodigo) {
			case GuiaFinalNe.RATEIO_100_REQUERENTE: {
				retorno = "100% Requerente";
				break;
			}
			case GuiaFinalNe.RATEIO_100_REQUERIDO: {
				retorno = "100% Requerido";
				break;
			}
			case GuiaFinalNe.RATEIO_50_50: {
				retorno = "50% Para Cada Parte";
				break;
			}
			case GuiaFinalNe.RATEIO_VARIAVEL: {
				retorno = "Porcentagem Variável";
				break;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para setar o nome do apelante e apelado atravï¿½s da lista de requerentes e requeridos presente neste Dt.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @throws Exception
	 */
	public void setNomeApelanteNomeApelado(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if( guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelante().length() > 0 && guiaEmissaoDt.getId_Apelado() != null && guiaEmissaoDt.getId_Apelado().length() > 0 ) {
			if( guiaEmissaoDt.getListaRequerentes() != null && guiaEmissaoDt.getListaRequerentes().size() > 0 ) {
				for(int k = 0; k < guiaEmissaoDt.getListaRequerentes().size(); k++) {
					ProcessoParteDt processoParteDt = (ProcessoParteDt) guiaEmissaoDt.getListaRequerentes().get(k);
					if( guiaEmissaoDt.getId_Apelante().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelante(processoParteDt.getNome());
					}
					if( guiaEmissaoDt.getId_Apelado().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelado(processoParteDt.getNome());
					}
				}
			}
			if( guiaEmissaoDt.getListaRequeridos() != null && guiaEmissaoDt.getListaRequeridos().size() > 0 ) {
				for(int i = 0; i < guiaEmissaoDt.getListaRequeridos().size(); i++) {
					ProcessoParteDt processoParteDt = (ProcessoParteDt) guiaEmissaoDt.getListaRequeridos().get(i);
					if( guiaEmissaoDt.getId_Apelante().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelante(processoParteDt.getNome());
					}
					if( guiaEmissaoDt.getId_Apelado().equals(processoParteDt.getId()) ) {
						guiaEmissaoDt.setApelado(processoParteDt.getNome());
					}
				}
			}
		}
	}
	
	/**
	 * Mï¿½todo para consultar Interlocutï¿½rias do advogado no processo.
	 * @param FabricaConexao obFabricaConexao
	 * @param String id_processo
	 * @return int
	 * @throws Exception
	 */
	public int consultarInterlocutoriasAdvogado(FabricaConexao obFabricaConexao, String id_processo) throws Exception {
		int protocoloQuantidade = 0;
		

		//Total de movimentaï¿½ï¿½es feitas pelo advogado
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		if( id_processo != null && id_processo.length() > 0 ) {
			protocoloQuantidade = movimentacaoNe.consultarInterlocutoriasAdvogado(id_processo);
		}
		
		
		int protocolosQuantidadePagos = 0;
		
		//Consulta as guias menos a guia inicial pois ela deve contar a cobranï¿½a de um protocolo
		List listaGuiaEmissaoDt = this.consultarGuiaEmissao(obFabricaConexao, id_processo, this.consultarListaId_GuiaTipo(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU));
		
		if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
			
			for(int i = 0; i < listaGuiaEmissaoDt.size(); i++ ) {
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(i);
				
				if( !guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.CANCELADA)) ) {
					boolean guiaPaga = false;
					if( guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO)) || 
						guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO_ON_LINE)) || 
						guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA)) ||
						guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR)) || 
						guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.PAGO_COM_VALOR_INFERIOR)) ) {
						
						guiaPaga = true;
					}
					
					if( guiaPaga ) {
						List listaGuiaItemDt = this.consultarGuiaItens(guiaEmissaoDt.getId(), null);
						for(int k = 0; k < listaGuiaItemDt.size(); k++ ) {
							GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt.get(k);
							
							if( guiaItemDt.getCustaDt().getId().equals(String.valueOf(CustaDt.TAXA_PROTOCOLO)) ) {
								if( guiaItemDt.getQuantidade() != null && guiaItemDt.getQuantidade().length() > 0 ) {
									protocolosQuantidadePagos = protocolosQuantidadePagos + Funcoes.StringToInt(guiaItemDt.getQuantidade());
								}
							}
						}
					}
				}
			}
			
		}
		
		//Quantas deve cobrar?
		protocoloQuantidade = protocoloQuantidade - protocolosQuantidadePagos;
		
		if( protocoloQuantidade < 0 ) {
			protocoloQuantidade = 0;
		}
		
			
		return protocoloQuantidade;
	}
	
	/**
	 * Mï¿½todo para verificar qual o tipo do ato do escrivão do cível.
	 * @param String processoTipoCodigo
	 * @return String
	 * @throws Exception
	 */
	public String verificarAtoEscrivaoCivel(String processoTipoCodigo) throws Exception {
		String retorno = null;
		

		switch(Funcoes.StringToInt(processoTipoCodigo)) {
			//Liberado esse case após a ocorrência 2018/13150.
			//Estava comentado antes por alguma solicitação anterior.
			case ProcessoTipoDt.EXECUCAO_FISCAL : {
				
				retorno = GuiaEmissaoNe.EXECUCAO_FISCAL;
				break;
			}
			
			case ProcessoTipoDt.CARTA_ORDEM_CPC : {
				
				retorno = GuiaEmissaoNe.CARTA_ORDEM;
				break;
			}
			
			case ProcessoTipoDt.TUTELA_CAUTELAR_ANTECEDENTE_32134 :
			case ProcessoTipoDt.TUTELA_CAUTELAR_ANTECEDENTE_32084 :
			case ProcessoTipoDt.ATENTADO :
			case ProcessoTipoDt.CAUCAO :
			case ProcessoTipoDt.CAUTELAR_INOMINADA_CPC :
			case ProcessoTipoDt.CAUTELAR_INOMINADA :
			case ProcessoTipoDt.SEPARACAO_CORPOS :
			case ProcessoTipoDt.EXIBICAO_DOCUMENTOS :
			case ProcessoTipoDt.PRODUCAO_ANTECIPADA_PROVAS :
			case ProcessoTipoDt.PERDA_SUSPENCAO :
			case ProcessoTipoDt.SUSTACAO_PROTESTO : {
				
				retorno = GuiaEmissaoNe.PROCESSOS_CAUTELARES;
				break;
			}
			
			case ProcessoTipoDt.ANULACAO_SUBSTITUICAO_TITULOS_PORTADOR :
			case ProcessoTipoDt.BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA :
			case ProcessoTipoDt.BUSCA_APREENSAO_CPC :
			case ProcessoTipoDt.BUSCA_APREENSAO :
			case ProcessoTipoDt.BUSCA_APREENSAO_CRIMINAL : 
			case ProcessoTipoDt.BUSCA_APREENSAO_6 : 
			case ProcessoTipoDt.BUSCA_APREENSAO_32072 : 
			case ProcessoTipoDt.CONSIGNATORIA :
			case ProcessoTipoDt.CONSIGNATORIA_PAGAMENTO :
			case ProcessoTipoDt.CONSIGNATORIA_ALUGUEIS :
			case ProcessoTipoDt.DEMOLITORIA :
			case ProcessoTipoDt.DEPOSITO :
			case ProcessoTipoDt.DEPOSITO_CPC :
			case ProcessoTipoDt.DEPOSITO_LEI_866_94_LE :
			
			//Ocorrência 2019/8168 - Com este comentário, cairá no case demais_processos e incluíra o item reg. 5
//			case ProcessoTipoDt.DESAPROPRIACAO :
			
			case ProcessoTipoDt.RECONHECIMENTO_EXTINCAO_UNIAO_ESTAVEL :
				
			case ProcessoTipoDt.DIVORCIO_CONSENSUAL :
			case ProcessoTipoDt.EMBARGOS_ADJUDICACAO :
			case ProcessoTipoDt.EMBARGOS_ARREMATACAO :
			case ProcessoTipoDt.EMBARGOS_RETENCAO :
			case ProcessoTipoDt.EMBARGOS_TERCEIRO :
			case ProcessoTipoDt.EMBARGOS_TERCEIRO_CPC :
			case ProcessoTipoDt.EMBARGOS_TERCEIRO_CPP :
			
			// Comentado após contato telefônico com o contador Marcelo Jesus(18/07/2018)
//			case ProcessoTipoDt.EXECUCAO_EXTRAJUDICIAL :
//			case ProcessoTipoDt.EXECUCAO_FISCAL :
				
			case ProcessoTipoDt.SEPARACAO_CONSENSUAL :
			case ProcessoTipoDt.DIVORCIO_LITIGIOSO_LE :
			case ProcessoTipoDt.CONVERSAO_SEPARACAO_EM_DIVORCIO :
				
			case ProcessoTipoDt.MONITORIA_CPC :
			
			case ProcessoTipoDt.HABILITACAO_CREDITO_CONCORDATA :
			case ProcessoTipoDt.HABILITACAO_INCIDENTAL :
			case ProcessoTipoDt.INTERDITO_PROIBITIVO :
			case ProcessoTipoDt.REINTEGRACAO_MANUTENCAO_DE_POSSE_CPC :
			case ProcessoTipoDt.REINTEGRACAO_DE_POSSE :
			case ProcessoTipoDt.NUNCIACAO_OBRA_NOVA :
			case ProcessoTipoDt.RECUPERACAO_JUDICIAL :
			case ProcessoTipoDt.PRESTACAO_CONTAS_OFERECIDAS_CPC :
			case ProcessoTipoDt.PRESTACAO_CONTAS_EXIGIDAS :
			case ProcessoTipoDt.PRESTACAO_CONTAS :
			case ProcessoTipoDt.RESTAURACAO_AUTOS_CPC :
			case ProcessoTipoDt.RESTAURACAO_AUTOS_CPP :
			case ProcessoTipoDt.SOBREPARTILHA :
			case ProcessoTipoDt.USUCAPIAO :

			case ProcessoTipoDt.REQUERIMENTO_REINTEGRACAO_POSSE:
			
			case ProcessoTipoDt.REGULACAO_AVARIA_GROSSA: {
				
				retorno = GuiaEmissaoNe.ATO_ESCRIVAO_CONTENCIOSO_70_PORCENTO;
				break;
			}
			
			default : {
				
				retorno = GuiaEmissaoNe.DEMAIS_PROCESSOS;
				break;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para cancelar a guia caso ela nï¿½o esteja paga.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean cancelarGuiaEmitida(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		boolean retorno = false;
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length() > 0 ) {
				
				if( !this.isGuiaPaga(guiaEmissaoDt) || (guiaEmissaoDt.isLocomocaoComplementar() && guiaEmissaoDt.getValorTotalGuiaDouble() == 0)) {
					
					//valida se guia tem item despesa postal vinculada a pendencia ou movimentacao
					if( new GuiaItemDisponivelNe().isGuiaVinculadaPendenciaOUMovimentacao(guiaEmissaoDt.getId(), obFabricaConexao) ) {
						throw new MensagemException("Guia não pode ser cancelada. Guia possui item de despesa postal utilizado.");
					}
					
					//valida se guia tem locomocao vinculada a mandado
					if( new LocomocaoNe().isGuiaVinculadaLocomocaoMandado(guiaEmissaoDt.getId(), obFabricaConexao) ) {
						throw new MensagemException("Guia não pode ser cancelada. Guia possui item de locomoção utilizado.");
					}
					
					if( this.isGuiaFonteParcelamento(guiaEmissaoDt.getId(), obFabricaConexao) ) {
						throw new MensagemException("Guia não pode ser cancelada. Guia fonte de parcelamento.");
					}
					
					//guia com cadastro de debito?
					if( new ProcessoParteDebitoNe().consultarProcessoParteDebitoIdGuiaEmissao(guiaEmissaoDt.getId()) != null ) {
						throw new MensagemException("Guia não pode ser cancelada. Guia possui cadastro de débito vinculada.");
					}
					
					//Cancela guia
					obPersistencia.cancelarGuiaEmitida(guiaEmissaoDt.getId(), String.valueOf(GuiaStatusDt.CANCELADA));
					
					if (guiaEmissaoDt.isLocomocaoComplementar()) {
						new LocomocaoNe().cancelarGuiaEmitidaLocomocaoComplementar(guiaEmissaoDt, obFabricaConexao);
					}
					
					//SPG
					new GuiaSPGNe().limparNumerosProcessosGuiaSPG(guiaEmissaoDt.getNumeroGuiaCompleto(), true, obFabricaConexao, LogTipoDt.GuiaCancelada, guiaEmissaoDt.getId_Usuario(), guiaEmissaoDt.getIpComputadorLog());
					
					//SSG
					new GuiaSSGNe().limparNumerosProcessosGuiaSSG(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao, LogTipoDt.GuiaCancelada, guiaEmissaoDt.getId_Usuario(), guiaEmissaoDt.getIpComputadorLog());
					
					retorno = true;
					
					//obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.GuiaCancelada),"", guiaEmissaoDt.getPropriedades() );
					obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.GuiaCancelada),"", "[Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_GuiaStatus:" + GuiaStatusDt.CANCELADA +";Id_Usuario:" + guiaEmissaoDt.getId_Usuario()+"]");
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
//	/**
//	 * Mï¿½todo para cancelar a guia caso ela nï¿½o esteja paga.
//	 * @param FabricaConexao obFabricaConexao
//	 * @param GuiaEmissaoDt guiaEmissaoDt
//	 * @return boolean
//	 * @throws Exception
//	 */
//	public boolean cancelarGuiaEmitida(FabricaConexao obFabricaConexao, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
//		boolean retorno = false;
//		
//		LogDt obLogDt;
//
//		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
//		
//		if( guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length() > 0 ) {
//			
//			obPersistencia.cancelarGuiaEmitida(guiaEmissaoDt.getId(), String.valueOf(GuiaStatusDt.CANCELADA));
//			retorno = true;
//			
//			obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.GuiaCancelada),"", "[Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_GuiaStatus:" + GuiaStatusDt.CANCELADA +";Id_Usuario:" + guiaEmissaoDt.getId_Usuario()+"]");
//			obLog.salvar(obLogDt, obFabricaConexao);
//		}
//	
//		return retorno;
//	}
	
	/**
	 * Mï¿½todo para cancelar a guia caso ela nï¿½o esteja paga.
	 * @Param ProcessoDt processoCivelDt
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean cancelarGuiaEmitidaFazendaPublicaAutomatica(ProcessoDt processoDt, GuiaEmissaoDt guiaEmissaoDt, String tipoGuia) throws Exception {
		boolean retorno = false;
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length() > 0 ) {
				
				boolean guiaPaga = this.isGuiaPaga(guiaEmissaoDt);
				
				String idGuiaStatus = String.valueOf(GuiaStatusDt.CANCELADA);
				if( guiaPaga ) {
					idGuiaStatus = String.valueOf(GuiaStatusDt.PAGA_CANCELADA);
				}
				
				if( !guiaPaga ) {
					//Cancela guia
					
					obPersistencia.cancelarGuiaEmitida(guiaEmissaoDt.getId(), idGuiaStatus);
					retorno = true;
					
					obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.GuiaCancelada),"", "[Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_GuiaStatus:" + idGuiaStatus +";Id_Usuario:" + guiaEmissaoDt.getId_Usuario()+"]");
					obLog.salvar(obLogDt, obFabricaConexao);
					
					GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, tipoGuia, processoDt.getId_ProcessoTipo());
					guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
					
					//Envia Guia para Cancelamento no SPG
					if( Funcoes.StringToInt(guiaEmissaoDt.getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.PRIMEIRO_GRAU) {
						GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
						guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
					}
				}
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo utilizado para verificar quando um processo vai para o segundo grau, se o recurso estï¿½ com 
	 * a serventia de origem onde o contador estï¿½ habilitado para poder emitir a guia.
	 * Ou seja, se a serventia de origem ï¿½ a serventia do contador, ele pode emitir.
	 * @param String idProcesso
	 * @param List listaServentiasGruposUsuario
	 * @param String idServentiaUsuario
	 * @return boolean
	 * @throws Exception
	 */
	public boolean consultarAutorizacaoEmitirGuiaRecursoServentiaOrigem(String idProcesso, List listaServentiasGruposUsuario, String idServentiaUsuario) throws Exception {
		boolean podeEmitir = false;
		
		//1 - consulta se tem recurso e pega o mais novo
		if( idProcesso != null ) {
			RecursoNe recursoNe = new RecursoNe();
			
			String idRecurso = recursoNe.consultarRecursoMaisNovo(idProcesso);
			RecursoDt recursoDt = null;
			if( idRecurso != null ) {
				recursoDt = recursoNe.consultarId(idRecurso);
			}
			
			//2 - verifica se o id_serventia_origem ï¿½ do contador
			if( recursoDt != null ) {
				if( listaServentiasGruposUsuario != null && listaServentiasGruposUsuario.size() > 0 ) {
					
					//Ou entï¿½o verifica a serventia relacionada se o contador tem acesso, por exemplo
					//verifica se a serventia de origem do recurso ï¿½ de alguma serventia que ï¿½ atendida 
					//pela contadoria do juizado
					ServentiaDt serventiaDt = new ServentiaRelacionadaNe().consultarContadoriaRelacionada(recursoDt.getId_ServentiaOrigem());
					
					for(int i = 0; i < listaServentiasGruposUsuario.size(); i++ ) {
						UsuarioDt usuarioDt = (UsuarioDt) listaServentiasGruposUsuario.get(i);
						
						if( (usuarioDt != null && usuarioDt.getId_Serventia() != null && usuarioDt.getId_Serventia().equals(recursoDt.getId_ServentiaOrigem())) || 
							( serventiaDt != null && serventiaDt.getId().equals(idServentiaUsuario) ) ) {
							
							podeEmitir = true;
							break;
						}
						
					}
				}
			}
		}
	
		
		return podeEmitir;
	}
	
	/**
	 * Método para verificar se o Bairro está zoneado ou não.
	 * Retorna TRUE se estiver zoneado e FALSE caso não esteja zoneado.
	 * @param List listaIdBairros
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isBairroZoneado(List listaIdBairros) throws Exception {
		boolean retorno = true;
		

		if( listaIdBairros != null && listaIdBairros.size() > 0 ) {
			List<String> idsVerificados = new ArrayList<String>();
			for(int i = 0; i < listaIdBairros.size(); i++) {
				String idBairro = listaIdBairros.get(i).toString();
				
				if (!idsVerificados.contains(idBairro)) {
					if (!isBairroZoneado(idBairro)) {
						retorno = false;
						break;
					}
					idsVerificados.add(idBairro);
				}					
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se o Bairro está zoneado ou não.
	 * Retorna TRUE se estiver zoneado e FALSE caso não esteja zoneado.
	 * @param String idBairro
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isBairroZoneado(String idBairro) throws Exception {
		boolean retorno = true;
		
		ZonaBairroRegiaoNe zonaBairroNe = new ZonaBairroRegiaoNe();
		ZonaBairroRegiaoDt zonaBairroDt = zonaBairroNe.consultarIdBairro(idBairro);
		retorno = zonaBairroDt != null;
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para chamar a GuiaCalculoNe para atualizar o valor da causa deste a data passada por parï¿½metro.
	 * @param String valorCausa
	 * @param Date dataAtualizacao
	 * @return Double
	 * @throws Exception
	 */
	public Double atualizarValorCausaUFR(String valorCausa, Date dataAtualizacao) throws Exception {
		Double retorno = 0.0D;
		
		if( valorCausa != null && valorCausa.length() > 0 && dataAtualizacao != null ) {
			
			retorno = this.getGuiaCalculoNe().calcularAtualizacaoValorCausa(valorCausa, dataAtualizacao);
			
		}
		else {
			retorno = 0.0D;
		}
		
		return retorno;
	}
	
	/**
	 * Método para dobrar somente as intimações.
	 * @param List valoresIdBairro
	 * @param List valoresIdBairroContaVinculada
	 * @param List listaItensGuia
	 * @param String temOficialCompanheiro
	 * @return void
	 * @throws Exception
	 */
	public void dobrarIntimacoes(List listaItensGuia, String temOficialCompanheiro) throws Exception {
		if (GuiaEmissaoDt.VALOR_SIM.equals(temOficialCompanheiro)) {
			if( listaItensGuia != null && listaItensGuia.size() > 0 ) {
				
				List listaAux = new ArrayList();
				
				for( int i = 0; i < listaItensGuia.size(); i++ ) {
					GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt) listaItensGuia.get(i);
					
					if( guiaCustaModeloDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO)) || guiaCustaModeloDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) || guiaCustaModeloDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)) ) {
						listaAux.add(guiaCustaModeloDt);
					}
				}
				
				if( listaAux.size() > 0 ) {
					listaItensGuia.addAll(listaAux);
				}
			}
		}
	}
	
	public void dobrarForaHorarioNormal(List listaItensGuia, String foraHorarioNormal) throws Exception {
		if( GuiaEmissaoDt.VALOR_SIM.equals(foraHorarioNormal) ) {
			List listaAux = new ArrayList();
			
			for( int i = 0; i < listaItensGuia.size(); i++ ) {
				GuiaCustaModeloDt guiaCustaModeloDt = (GuiaCustaModeloDt) listaItensGuia.get(i);
				
				if(guiaCustaModeloDt.getCustaDt().getId().equals(String.valueOf(CustaDt.CUSTAS_LOCOMOCAO)) || guiaCustaModeloDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_OFICIAL)) || guiaCustaModeloDt.getCustaDt().getId().equals(String.valueOf(CustaDt.LOCOMOCAO_PARA_TRIBUNAL)) ) {
					listaAux.add(guiaCustaModeloDt);
				}
			}
			
			if( listaAux.size() > 0 ) {
				listaItensGuia.addAll(listaAux);
			}
		}
	}
	
	/**
	 * Mï¿½todo para calcular as locomoï¿½ï¿½es complementares.
	 * 
	 * @param List listaGuiaItemDt
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @throws Exception
	 */
	public void calcularItensGuiaLocomocaoComplementar(List listaGuiaItemDt, List listaGuiaItemDt_GuiaLocomocaoPaga) throws Exception {
		guiaCalculoNe.calcularItensGuiaLocomocaoComplementar(listaGuiaItemDt, listaGuiaItemDt_GuiaLocomocaoPaga);
	}
	
	/**
	 * Mï¿½todo para calcular as locomoï¿½ï¿½es complementares da guia inicial.
	 * 
	 * @param List listaGuiaItemDt
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @throws Exception
	 */
	public void calcularItensGuiaInicialLocomocaoComplementar(List listaGuiaItemDt, List listaGuiaItemDt_GuiaLocomocaoPaga) throws Exception {
		guiaCalculoNe.calcularItensGuiaInicialLocomocaoComplementar(listaGuiaItemDt, listaGuiaItemDt_GuiaLocomocaoPaga);
	}
	
	/**
	 * Mï¿½todo para retirar os itens com valores zerados.
	 * 
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void retirarItensValorCalculadoZerado(List listaGuiaItemDt) throws Exception {
		guiaCalculoNe.retirarItensValorCalculadoZerado(listaGuiaItemDt);
	}
	
	/**
	 * Mï¿½todo para retirar itens que nï¿½o sï¿½o locomoï¿½ï¿½es.
	 * 
	 * @param List listaGuiaItemDt_GuiaLocomocaoPaga
	 * @return List
	 */
	public List retirarItensNaoLocomocao(List listaGuiaItemDt_GuiaLocomocaoPaga) throws Exception {
		List listaGuiaItemDt = null;
		

		if( listaGuiaItemDt_GuiaLocomocaoPaga != null && listaGuiaItemDt_GuiaLocomocaoPaga.size() > 0 ) {
			for( int i = 0; i < listaGuiaItemDt_GuiaLocomocaoPaga.size(); i++ ) {
				GuiaItemDt guiaItemDt = (GuiaItemDt) listaGuiaItemDt_GuiaLocomocaoPaga.get(i);
				
				switch( Funcoes.StringToInt( guiaItemDt.getCustaDt().getId() ) ) {
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL :
					case CustaDt.LOCOMOCAO_PARA_OFICIAL :
					case CustaDt.LOCOMOCAO_PARA_AVALIACAO :
					case CustaDt.LOCOMOCAO_PARA_PENHORA : {
						
						if( listaGuiaItemDt == null ) {
							listaGuiaItemDt = new ArrayList();
						}
						listaGuiaItemDt.add(guiaItemDt);
						
						break;
					}
				}
			}
		}
		
		return listaGuiaItemDt;
	}
	
	/**
	 * Mï¿½todo para consultar a guia complementar emitida da guia de locomoï¿½ï¿½o.
	 * @param String idGuiaLocomocao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaComplementarGuiaLocomocao(String idGuiaLocomocao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt = obPersistencia.consultarGuiaComplementarGuiaLocomocao(idGuiaLocomocao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Mï¿½todo para consultar o id do usuï¿½rio.
	 * 
	 * @return String usuario
	 * @throws Exception
	 */
	public String consultarUsuario(String idUsuario) throws Exception {
		String retorno = null;		
		
		UsuarioDt usuarioDt = new UsuarioNe().consultarId(idUsuario);
		retorno = usuarioDt.getUsuario();		
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar o nome da guia tipo.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaTipo
	 * @return String
	 * @throws Exception
	 */
	public String consultarGuiaTipo(FabricaConexao obFabricaConexao, String idGuiaTipo) throws Exception {
		String retorno = null;
		
		if( idGuiaTipo != null && idGuiaTipo.length() > 0 ) {
			guiaTipoNe = new GuiaTipoNe();
			retorno = guiaTipoNe.consultarDescricao(obFabricaConexao, idGuiaTipo);
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar o ProcessoTipoDt.
	 * @param String idProcessoTipo
	 * @return ProcessoTipoDt
	 * @throws Exception
	 */
	public ProcessoTipoDt consultarProcessoTipo(String idProcessoTipo) throws Exception {
		ProcessoTipoDt processoTipoDt = null;
		
		if( idProcessoTipo != null && idProcessoTipo.length() > 0 ) {
			processoTipoNe = new ProcessoTipoNe();
			processoTipoDt = processoTipoNe.consultarId(idProcessoTipo);
		}
		
		return processoTipoDt;
	}
	
	/**
	 * Método para consultar o ProcessoDt.
	 * @param String idProcesso
	 * @return ProcessoTipoDt
	 * @throws Exception
	 */
	public ProcessoDt consultarProcesso(String idProcesso) throws Exception {
		ProcessoDt processoDt = null;
		
		if( idProcesso != null && idProcesso.length() > 0 ) {
			ProcessoNe processoNe = new ProcessoNe();
			processoDt = processoNe.consultarId(idProcesso);
		}
		
		return processoDt;
	}	
	
	/**
	 * Verificar se pode emitir locomoï¿½ï¿½o ou nï¿½o.
	 * @param String idProcessoTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean permiteLocomocao(String idProcessoTipo) throws Exception {
		boolean retorno = true;
		
		switch( Funcoes.StringToInt(idProcessoTipo) ) {
		
			case ProcessoTipoDt.EMBARGOS_EXECUCAO : 
			case ProcessoTipoDt.DIVORCIO_CONSENSUAL : {
				
				retorno = false;
				
				break;
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar bairros.
	 * @param String idBairro
	 * @return BairroDt
	 * @throws Exception
	 */
	public BairroDt consultarBairroId(String idBairro) throws Exception {
		if( bairroNe == null ) {
			bairroNe = new BairroNe();
		}
		
		return bairroNe.consultarId(idBairro);
	}
	
	/**
	 * Método para consultar área de distribuição.
	 * @param String idAreaDistribuicao
	 * @return AreaDistribuicaoDt
	 * @throws Exception
	 */
	public AreaDistribuicaoDt consultarAreaDistribuicao(String idBaidAreaDistribuicaoirro) throws Exception {
		if( areaDistribuicaoNe == null ) {
			areaDistribuicaoNe = new AreaDistribuicaoNe();
		}
		
		return areaDistribuicaoNe.consultarId(idBaidAreaDistribuicaoirro);
	}
	
	/**
	 * Mï¿½todo para consultar se existe no processo alguma guia emitida para este tipo especï¿½fico.
	 * @param String idProcesso
	 * @param String idGuiaTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existeGuiaEmitidaMesmoTipo(String idProcesso, String idGuiaTipo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.existeGuiaEmitidaMesmoTipo(idProcesso, idGuiaTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para consultar se existe no processo alguma guia emitida para este tipo especï¿½fico.
	 * @param String idProcesso
	 * @param String idGuiaTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existeGuiaEmitidaMesmoTipoNaoCancelada(String idProcesso, String idGuiaTipo) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.existeGuiaEmitidaMesmoTipoNaoCancelada(idProcesso, idGuiaTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Mï¿½todo para verificar se o processo pode ou nï¿½o emitir locomoï¿½ï¿½o.
	 * Isso ï¿½ necessï¿½rio para a ocorrï¿½ncia 2013/76036.
	 * 
	 * @param String idProcesso
	 * @param String idProcessoTipo
	 * @param int tipoGuia
	 * 
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean podeEmitirLocomocao(String idProcesso, String idProcessoTipo, String tipoGuia) throws Exception {
		boolean retorno = false;
		
		if( idProcessoTipo != null && Funcoes.StringToInt(idProcessoTipo) == ProcessoTipoDt.REPRESENTACAO_CRIMINAL_CPP ) {
			retorno = true;
		}
		else {
			boolean isAreaCriminal = new ProcessoNe().isProcessoAreaCriminal(idProcesso);
			switch( tipoGuia ) {
				case GuiaTipoDt.ID_FINAL_EXECUCAO_QUEIXA_CRIME :
				case GuiaTipoDt.ID_RECURSO_INOMINADO_QUEIXA_CRIME : {
					retorno = true; //pode emitir mas com a cobranï¿½a do cï¿½vel, segundo elizangela da contadoria
					break;
				}
				case GuiaTipoDt.ID_LOCOMOCAO : {
					if( isAreaCriminal ) {
						retorno = false;
					}
					else {
						retorno = true;
					}
					break;
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para testar se a conexão com o SPG está ok para evitar problemas com a emissão da guia.
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 */
	public boolean isConexaoSPG_OK() throws Exception {
		boolean retorno = true;
		
		try {
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			retorno = guiaSPGNe.isConexaoSPG_OK();
			
			if( retorno ) {
				GuiaEmissaoNe.contador_erro_adabas = 0;
			}
			else {
				GuiaEmissaoNe.contador_erro_adabas++;
				if( GuiaEmissaoNe.contador_erro_adabas == 3 ) {					
					new GerenciadorEmail("Olá!" , "fasoares@tjgo.jus.br", "PJD Conexão CONNX", "Olá! Sou o PJD, me ajude pq eu perdi a conexão com o CONNX! (=D).", GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
				}
			}
		}
		catch(Exception e) {
			retorno = false;
		}
		
		return retorno;
	}
	
	/**
	 * Método para gerar as guias automáticas da prefeitura para o projeto da guia única.
	 * 
	 * @param ProcessoDt processoDt
	 * @param String tipoGuia //Este parâmetro é para consultar o modelo da guia e os seus itens.
	 * @param List listaGuiaItemDtCalculado //Caso esteja preenchido, deverá ser utilizado os seus itens
	 * @return FabricaConexao obFabricaConexao
	 * @throws Exception
	 * @author fasoares
	 */
	public GuiaEmissaoDt gerarGuiaFazendaPublicaAutomatica(ProcessoDt processoDt, String tipoGuia, List listaGuiaItemDtCalculado, FabricaConexao obFabricaConexao) throws Exception {
		
		GuiaEmissaoDt guiaEmissaoDt = this.gerarGuiaFazendaPublicaAutomaticaSemSalvar(processoDt, tipoGuia, listaGuiaItemDtCalculado);
		
		if( guiaEmissaoDt != null &&  guiaEmissaoDt.getListaGuiaItemDt() != null && guiaEmissaoDt.getListaGuiaItemDt().size() > 0) {
			this.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioDt.SistemaProjudi, obFabricaConexao);			
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para calcular as guias automáticas da prefeitura para o projeto da guia única.
	 * 
	 * @param processoDt
	 * @param tipoGuia
	 * @param listaGuiaItemDtCalculado
	 * @return
	 * @throws Exception
	 */
	public GuiaEmissaoDt gerarGuiaFazendaPublicaAutomaticaSemSalvar(ProcessoDt processoDt, String tipoGuia, List listaGuiaItemDtCalculado) throws Exception {
		
		List listaItensGuia = null;
		GuiaEmissaoDt guiaEmissaoDt = null;
		

		if( processoDt != null && processoDt.getId() != null ) {
			
			guiaEmissaoDt = new GuiaEmissaoDt();
			
			guiaEmissaoDt.setNovoValorAcaoAtualizado(processoDt.getValor()); //Não atualiza o valor
			
			Map valoresReferenciaCalculo = new HashMap();
			valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, guiaEmissaoDt.getNovoValorAcaoAtualizado() );
			
			//******* Obtem dados dos processos *********
			//Dados do processo
			guiaEmissaoDt.setId_Processo(processoDt.getId());
			guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
			//*******************************************
			
			List listaGuiaItemDt = null;
			
			if( listaGuiaItemDtCalculado == null ) {
				//******* Modelo - Consulta itens pré-cadastrados ************
				listaItensGuia = this.consultarItensGuiaNovoRegimento(null, tipoGuia, processoDt.getId_ProcessoTipo());
				
				//Consulta modelo da guia
				GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, tipoGuia, processoDt.getId_ProcessoTipo());
				guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
				//************************************************************
				
				//******** Adiciona 1 na quantidade de cada item para validar o seu cálculo **********
				//Adiciona 1 na quantidade em cada item
				guiaEmissaoDt.setDistribuidorQuantidade("1");
				guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
				guiaEmissaoDt.setCustasQuantidade("1");
				guiaEmissaoDt.setEscrivaniaQuantidade("1");
				guiaEmissaoDt.setContadorQuantidade("1");
				guiaEmissaoDt.setDividaAtivaQuantidade("1");
//				guiaEmissaoDt.setHonorariosQuantidade("10"); //10%
//				guiaEmissaoDt.setHonorariosValor(guiaEmissaoDt.getNovoValorAcaoAtualizado());					
				//************************************************************************************
				
				//********* Calcula a guia ******************
				listaGuiaItemDt = this.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
				//*******************************************
			} else {
				
				//Consulta modelo da guia
				GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, tipoGuia, processoDt.getId_ProcessoTipo());
				guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
				//************************************************************
				
				listaGuiaItemDt = listaGuiaItemDtCalculado;
			}
			
			//********* Se os itens foram calculados finaliza e gera a guia ***************
			//Calculou certo
			if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0) {
				guiaEmissaoDt.setListaGuiaItemDt(listaGuiaItemDt);
				guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
				guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
				guiaEmissaoDt.setNumeroGuiaCompleto( this.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
				guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
				guiaEmissaoDt.setValorAcao(processoDt.getValor());
				guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
				guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
				guiaEmissaoDt.setId_Usuario(UsuarioDt.SistemaProjudi);
				guiaEmissaoDt.setIpComputadorLog(processoDt.getIpComputadorLog());
				if (tipoGuia.equalsIgnoreCase(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA))
					guiaEmissaoDt.setId_GuiaStatus(String.valueOf(GuiaStatusDt.GUIA_GERADA_ENVIAR_PREFEITURA));
				else 
					guiaEmissaoDt.setId_GuiaStatus(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO));
			}
			//Não trouxe nada na lista de itens calculados
			else {
				guiaEmissaoDt = null;
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias geradas automaticamente para a prefeitura de 1 processo específico.
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List consultarGuiasGeradaPrefeituraProcesso(String idProcesso) throws Exception {
		
		List retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			List listaId_GuiaTipo = new ArrayList();
			listaId_GuiaTipo.add(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);
			
			retorno = obPersistencia.consultarGuiaEmissao(idProcesso, listaId_GuiaTipo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar se a guia é gerada para a prefeitura.
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaGeradaEnviarPrefeitura(String idGuiaEmissao) throws Exception {
		
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idGuiaEmissao != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				retorno = obPersistencia.isGuiaGeradaEnviarPrefeitura(idGuiaEmissao);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Consultar guias pelo status aguardando envio para prefeitura.
	 * @param guiaStatus
	 * @return
	 * @throws Exception
	 */
	public List consultarGuiasAguardandoEnvioPrefeitura() throws Exception {
		return consultarGuiasPeloStatus(String.valueOf(GuiaStatusDt.GUIA_GERADA_ENVIAR_PREFEITURA), 1000);
	}
	
	public List consultarGuiasPeloStatus(String idGuiaStatus, int quantidadeMaxima) throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarGuiasPeloStatus(idGuiaStatus, quantidadeMaxima);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Consultar guias pelo status.
	 * @param guiaStatus
	 * @return
	 * @throws Exception
	 */
	public List consultarGuiasPeloStatus(String idGuiaStatus) throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarGuiasPeloStatus(idGuiaStatus);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao 
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusGuiaEmitidaEnviadaPrefeitura(String idGuiaEmissao) throws Exception {
		atualizarStatusGuiaEmitida(idGuiaEmissao, String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO));
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao
	 * @param String idGuiaStatus
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusGuiaEmitida(String idGuiaEmissao, String idGuiaStatus) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			obPersistencia.atualizarStatusGuiaEmitida(idGuiaEmissao, idGuiaStatus);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método administrativo para cancelar guia.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param String motivoAlteracaoStatus
	 * @param String idNovoStatusGuia
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean atualizarStatusGuia(String numeroGuiaCompleto, String idUsuarioLog, String ipComputadorLog, String motivoAlteracaoStatus, String idNovoStatusGuia) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean retorno = false;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			GuiaEmissaoDt guiaEmissaoDt = consultarNumeroCompleto(numeroGuiaCompleto, null, null);
			
			if( guiaEmissaoDt != null ) {
				retorno = obPersistencia.atualizarStatusGuiaEmitida(guiaEmissaoDt.getId(), idNovoStatusGuia);
				if( retorno ) {
					LogDt obLogDt = new LogDt("GuiaEmissao", guiaEmissaoDt.getId(), idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar),"", "[Status da Guia Alterado pela Funcionalidade Administrativa;Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Motivo:"+motivoAlteracaoStatus+"]");
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para alterar id_comarca e id_area_distribuicao da guia inicial sem vinculo com processo.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param String idAreaDistribuicao
	 * @param String idComarca
	 * @param String motivoAlteracaoComarca
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean atualizarComarcaAreaDistribuicao(String numeroGuiaCompleto, String idUsuarioLog, String ipComputadorLog, String idAreaDistribuicao, String idComarca, String motivoAlteracaoComarca) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean retorno = false;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			GuiaEmissaoDt guiaEmissaoDt = consultarNumeroCompleto(numeroGuiaCompleto, null, null);
			
			if( guiaEmissaoDt != null ) {
				retorno = obPersistencia.atualizarComarcaAreaDistribuicao(guiaEmissaoDt.getId(), idAreaDistribuicao, idComarca);
				if( retorno ) {
					LogDt obLogDt = new LogDt("GuiaEmissao", guiaEmissaoDt.getId(), idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar),"", "[Comarca ou Área de distribuição da Guia Alterado pela Funcionalidade Administrativa;Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_Comarca:"+idComarca+";Id_AreaDistribuicao:"+idAreaDistribuicao+";Motivo:"+motivoAlteracaoComarca+"]");
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao
	 * @param String idGuiaStatus
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusGuiaEmitida(FabricaConexao obFabricaConexao, String idGuiaEmissao, String idGuiaStatus) throws Exception {
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
		obPersistencia.atualizarStatusGuiaEmitida(idGuiaEmissao, idGuiaStatus);
	}
	
	/**
	 * Método para consultar a última Guia Emitida pelo id_Processo do tipo final.
	 * @param String idProcesso	 
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarUltimaGuiaEmissaoTipoFinal(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		return this.consultarUltimaGuiaEmissao(idProcesso, GuiaTipoDt.ID_FINAL, obFabricaConexao);
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e tipo de guia.
	 * @param String idProcesso
	 * @param String id_GuiaTipo
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarUltimaGuiaEmissao(String idProcesso, String id_GuiaTipo) throws Exception {
		GuiaEmissaoDt retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			retorno = this.consultarUltimaGuiaEmissao(idProcesso, id_GuiaTipo, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}	
	
	public GuiaEmissaoDt consultarUltimaGuiaEmissaoFazenda(String idProcesso) throws Exception {
		GuiaEmissaoDt retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			retorno = this.consultarUltimaGuiaEmissaoFazenda(idProcesso, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}	
	
	/**
	 * Método para consultar Guias Emitidas pelo id_Processo e tipo de guia.
	 * @param String idProcesso
	 * @param String id_GuiaTipo
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarUltimaGuiaEmissao(String idProcesso, String id_GuiaTipo, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt retorno = null;
				
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		retorno = obPersistencia.consultarUltimaGuiaEmissao(idProcesso, id_GuiaTipo);
		
		return retorno;
	}
	
	public GuiaEmissaoDt consultarUltimaGuiaEmissaoFazenda(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt retorno = null;
				
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		retorno = obPersistencia.consultarUltimaGuiaEmissaoFazenda(idProcesso);
		
		return retorno;
	}

	public String consultarDescricaoProcessoTipoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		processoTipoNe = new ProcessoTipoNe();
		stTemp = processoTipoNe.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}
	
	/**
	 * Método para estornar o pagamento do número da guia.
	 * @param String numeroGuiaCompleto	 
	 * @throws Exception
	 */
	public void estornarPagamento(String numeroGuiaCompleto) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			estornarPagamento(numeroGuiaCompleto, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para estornar o pagamento do número da guia.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao	 
	 * @throws Exception
	 */
	public void estornarPagamento(String numeroGuiaCompleto, FabricaConexao obFabricaConexao) throws Exception {		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
		obPersistencia.estornarPagamento(numeroGuiaCompleto);		
	}
	
	/**
	 * Método para estornar o pagamento do número da guia da prefeitura.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao obFabricaConexao	 
	 * @throws Exception
	 */
	public void estornarPagamentoPrefeitura(String numeroGuiaCompleto, FabricaConexao obFabricaConexao) throws Exception {		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
		obPersistencia.estornarPagamentoPrefeitura(numeroGuiaCompleto);		
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao 
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusDividaBaixadaPrefeitura(String idGuiaEmissao) throws Exception {
		atualizarStatusGuiaEmitida(idGuiaEmissao, String.valueOf(GuiaStatusDt.DIVIDA_BAIXADA_PREFEITURA));
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao 
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusPagoDareCadin(String idGuiaEmissao) throws Exception {
		atualizarStatusGuiaEmitida(idGuiaEmissao, String.valueOf(GuiaStatusDt.PAGO_DARE_CADIN));
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao 
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusProtocoloNaoCadastradoPrefeitura(String idGuiaEmissao) throws Exception {
		atualizarStatusGuiaEmitida(idGuiaEmissao, String.valueOf(GuiaStatusDt.PROTOCOLO_NAO_CADASTRADO_PREFEITURA));
	}
	
	/**
	 * Método responsável em obter as guias de fazenda municial (antigo formato) que 
	 * foram pagas (momento de transição). 
	 * @return
	 * @throws Exception
	 */
	public List consultarGuiasPrefeiturasQueDevemSerCanceladasPeloPagamentoDaGuiaFazendaMunicipal() throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarGuiasPrefeiturasQueDevemSerCanceladasPeloPagamentoDaGuiaFazendaMunicipal();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public GuiaEmissaoDt consultarGuiaFazendaMunicipal(String idProcesso) throws Exception {
		GuiaEmissaoDt retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarGuiaFazendaMunicipal(idProcesso);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao 
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusProcessoInativoTJGOPrefeitura(String idGuiaEmissao) throws Exception {
		atualizarStatusGuiaEmitida(idGuiaEmissao, String.valueOf(GuiaStatusDt.PROCESSO_INATIVO_TJGO_PREFEITURA));
	}
	
	/**
	 * Método para atualizar status da guia emitida.
	 * @param String idGuiaEmissao 
	 * @return boolean
	 * @throws Exception
	 */
	public void atualizarStatusProcessoForaConvenioPrefeitura(String idGuiaEmissao) throws Exception {
		atualizarStatusGuiaEmitida(idGuiaEmissao, String.valueOf(GuiaStatusDt.PROTOCOLO_FORA_CONVENIO_PREFEITURA));
	}
	
	public List consultarGuiasPrefeiturasCanceladasUsuarioEnvioPendentes() throws Exception {
		List retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarGuiasPrefeiturasCanceladasUsuarioEnvioPendentes();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	public void atualizarGuiasPrefeiturasCanceladasUsuarioPendentesDeEnvio(String idGuiaEmissao) throws Exception {
		FabricaConexao obFabricaConexao = FabricaConexao.criarConexaoPersistencia();
		
		try {
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			obPersistencia.atualizarGuiasPrefeiturasCanceladasUsuarioPendentesDeEnvio(idGuiaEmissao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void atualizarRepasse(String numeroGuiaCompleto, String valorRepasse, TJDataHora dataMovimento, FabricaConexao obFabricaConexao) throws Exception {		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
		obPersistencia.atualizarRepasse(numeroGuiaCompleto, valorRepasse, dataMovimento);		
	}
	
	/**
	 * Em conversa com o Leandro, decidimos usar este método do processoNe para verificar se o processo existe na base 
	 * do projudi. Esta consulta foi feita para a emissão da guia inicial. Existe outro método que faz essa verificação 
	 * se o processo existe, mas ele olha somente o número, digito e ano, este já olha todos os 6 conjunto de números 
	 * do cnj.
	 * 
	 * @param String numeroProcessoCompleto
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoNumeroCompletoExistente(String numeroProcessoCompleto) throws Exception {
		boolean retorno = false;
		
		processoNe = new ProcessoNe();
		if( numeroProcessoCompleto != null && processoNe.consultarProcessoNumeroCompletoDigitoAno(numeroProcessoCompleto.trim()) != null ) {
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de segurança.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoMandado(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoMandado(processoTipoCodigo);
	}
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é de um dos tipos de mandado de divórcio.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoConversaoSeparacaoEmDivorcio(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoConversaoSeparacaoEmDivorcio(processoTipoCodigo);
	}
	
	/**
	 * Método para auxiliar na verificação se o processo tipo código é do tipo Divórcio Litigiosa.
	 * Retorna true se sim.
	 * @param int processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoTipoDivorcioLitigiosaLE(int processoTipoCodigo) throws Exception {
		return new ProcessoTipoNe().isProcessoTipoDivorcioLitigiosaLE(processoTipoCodigo);
	}
	
	/**
	 * Método para consultar o processoDt pelo número do processo completo.
	 * 
	 * @param String numeroProcessoCompleto
	 * @return ProcessoDt
	 */
	public ProcessoDt consultarProcessoNumeroCompleto(String numeroProcessoCompleto) throws Exception {
		ProcessoDt processoDt = null;
		
		if( numeroProcessoCompleto != null ) {
			processoNe = new ProcessoNe();
			processoDt = processoNe.consultarProcessoNumeroCompleto(numeroProcessoCompleto,null);
		}
		
		return processoDt;
	}
	
	/**
	 * Método para verificar se o processo tipo código é do tipo "Execução Fiscal".
	 * 
	 * @param String processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isProcessoTipoExecucaoFiscal(String processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		if( processoTipoCodigo != null ) {
			if(Funcoes.StringToInt(processoTipoCodigo) == ProcessoTipoDt.EXECUCAO_FISCAL ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar locomoções não utilizadas [CENTRAL DE MANDADOS DO PROJUDI].
	 * @return List
	 * @throws Exception
	 */
	public String consultarDescricaoLocomocoesNaoUtilizadasJSON(String idProcesso, String tempNomeBusca, String tempCidade, String tempUf, String tempZona,  String PosicaoPaginaAtual, boolean validaOficialVinculadoLocomocao) throws Exception {
		return new LocomocaoNe().consultarLocomocaoNaoUtilizadaJSON(idProcesso, tempNomeBusca, tempCidade, tempUf, tempZona, PosicaoPaginaAtual, validaOficialVinculadoLocomocao);		
	}
	
	public List<LocomocaoDt> consultarLocomocaoNaoUtilizada(String idProcesso, String idGuiaEmissao, boolean validaOficialVinculadoLocomocao) throws Exception {
		return new LocomocaoNe().consultarLocomocaoNaoUtilizada(idProcesso, idGuiaEmissao, validaOficialVinculadoLocomocao);
	}
	
	public List<LocomocaoDt> consultarLocomocaoUtilizadaGuiaComplementar(String idProcesso, String idGuiaComplementar) throws Exception {
		return new LocomocaoNe().consultarLocomocaoUtilizadaGuiaComplementar(idProcesso, idGuiaComplementar);
	}
	
	/**
	 * Método para consultar locomoção por id.
	 * @param String idLocomocao
	 * @return LocomocaoDt
	 * @throws Exception
	 */
	public LocomocaoDt consultarLocomocaoId(String idLocomocao) throws Exception {
		return new LocomocaoNe().consultarIdCompleto(idLocomocao);
	}
	
	/**

	 * Método para verificar se processo possui guias pendentes de pagamento
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean verificarGuiasPendentesProcesso(String idProcesso) throws Exception {
		boolean retorno = false;
		FabricaConexao conexao = null;
		try {
			GuiaEmissaoPs obPersistencia;
			conexao = new FabricaConexao(FabricaConexao.CONSULTA);
			obPersistencia = new GuiaEmissaoPs(conexao.getConexao());
			retorno = obPersistencia.verificarGuiasPendentesProcesso(idProcesso);
		} finally {
			conexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para vincular a guia inicial ao processo no momento do cadastro.
	 * @param String idGuiaEmissaoDt
	 * @param String numeroGuiaCompleto
	 * @param String idProcesso
	 * @param String idServentia
	 * @param String idComarca
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean vinculaGuiaProcesso(String idGuiaEmissaoDt, String numeroGuiaCompleto, String idProcesso, String idServentia, String idComarca, String idUsuarioLog, String ipComputadorLog, FabricaConexao obFabricaConexao, String motivoVinculacao) throws Exception {
		boolean retorno = false;
		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		//Atualiza o pagamento da guia caso já tenha sido recebida pelo SPG
		TJDataHora tjDataHoraPagamento = new GuiaSPGNe().consultarDataDePagamentoGuia(numeroGuiaCompleto);
		if( tjDataHoraPagamento != null ) {
			//Sim, paga, então atualiza a guia na nossa base.
			atualizaGuiaConsolidado(obFabricaConexao, numeroGuiaCompleto, idProcesso, tjDataHoraPagamento);
		}
		
		retorno = obPersistencia.vinculaGuiaProcesso(idGuiaEmissaoDt, idProcesso, idServentia, idComarca);
		if( retorno ) {
			LogDt obLogDt = new LogDt("GuiaEmissao", idGuiaEmissaoDt, idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Alterar), "", "[Id_GuiaEmissao:"+idGuiaEmissaoDt+";Id_Processo:"+idProcesso+";Id_Serventia:"+idServentia+";Id_Comarca:"+idComarca+";Motivo:"+motivoVinculacao+"]");
			obLog.salvar(obLogDt, obFabricaConexao);
		}
		
		return retorno;
	}
	
	/**
	 * Método que faz todo o trabalho do rollback no momento do cadastro do processo que vincula a guia do projudi.
	 */
	public boolean rollbackAlteracoesGuiaSPGCadastroProcesso(GuiaEmissaoDt guiaEmissaoDt, String codigoServentiaParaRollback, String numrProcessoParaRollback, String codigoComarcaCodigoParaRollback, String idUsuarioLogRollback, String idComputadorLogRollback) throws Exception {
		boolean retorno = false;
		
		if( guiaEmissaoDt != null && guiaEmissaoDt.getNumeroGuiaCompleto() != null && !guiaEmissaoDt.getNumeroGuiaCompleto().isEmpty() ) {
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			
			GuiaEmissaoDt guiaSPGDt = guiaSPGNe.consultarGuiaSPGCapitalInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
			
			if( guiaSPGDt != null && guiaSPGDt.getId() != null && !guiaSPGDt.getId().isEmpty() ) {
				if( codigoServentiaParaRollback != null || numrProcessoParaRollback != null ) {
					
					guiaSPGNe.rollbackAtualizaGuiaVinculadaProcesso(guiaSPGDt.getId(), numrProcessoParaRollback, codigoServentiaParaRollback, codigoComarcaCodigoParaRollback, idUsuarioLogRollback, idComputadorLogRollback);
					
					guiaSPGNe.rollbackInserirGuiaInfoRepasse(guiaSPGDt.getId(), guiaEmissaoDt, idUsuarioLogRollback, idComputadorLogRollback);
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se guia é do tipo complementar.
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao conexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaComplementar(String numeroGuiaCompleto, FabricaConexao conexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			}
			else {
				obFabricaConexao = conexao;
			}
			GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = guiaEmissaoPs.isGuiaComplementar(numeroGuiaCompleto);
		}
		finally {
			if( conexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar se guia é do tipo final ou final zero.
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaFinal_FinalZero(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			if( idGuiaEmissao != null ) {
				GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
				retorno = guiaEmissaoPs.isGuiaFinal_FinalZero(idGuiaEmissao);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar se o processo possui guia final ou final zero AGUARDANDO PAGAMENTO.
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isProcessoPossuiGuiaFinal_FinalZeroAguardandoPagamento(String idProcesso) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
				retorno = guiaEmissaoPs.isProcessoPossuiGuiaFinal_FinalZeroAguardandoPagamento(idProcesso);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se guia é do tipo inicial.
	 * @param String idGuiaEmissao
	 * @param FabricaConexao conexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaInicial(String idGuiaEmissao, FabricaConexao conexao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			}
			else {
				obFabricaConexao = conexao;
			}
			GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			retorno = guiaEmissaoPs.isGuiaInicial(idGuiaEmissao);
		}
		finally {
			if( conexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se a guia antes de emitida é inicial
	 * @param String codigoTipoGuia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaInicial(String codigoTipoGuia) throws Exception {
		boolean retorno = false;
		
		if( codigoTipoGuia != null && 
			(
				codigoTipoGuia.equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU)
				||
				codigoTipoGuia.equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU)
			)
		) {
				
				retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar guiaModeloDt em transação.
	 * @param String idGuiaModelo
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultaGuiaModeloDt(String idGuiaModelo, FabricaConexao obFabricaConexao) throws Exception {
		return new GuiaModeloNe().consultarIdGuiaModelo(idGuiaModelo, obFabricaConexao);
	}
	
	/**
	 * Método para verificar se a guia é complementar e, caso sim, alterar os dados do processo.
	 * 
	 * Fluxo:
	 * 1 - Verifica se é guia complementar
	 * 1.1 - Obtem a classe e o valor do processo
	 * 1.2 - Atualiza o processo com a classe e o valor do processo
	 * 1.3 - Gera a movimentação
	 * 
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao conexao
	 * @throws Exception
	 */
	public void alterarDadosProcessoViaNumeroGuiaComplementar(String numeroGuiaCompleto, FabricaConexao conexao) throws Exception {
		
		//Verifica se é guia complementar
		if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 && this.isGuiaComplementar(numeroGuiaCompleto, conexao) ) {
			
			if( processoNe == null ) {
				processoNe = new ProcessoNe();
			}
			
			GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto, conexao);
			
			alterarDadosProcessoViaNumeroGuiaComplementar(guiaEmissaoDt, conexao);
		}
	}
	
	/**
	 * Método para verificar se a guia é complementar e, caso sim, alterar os dados do processo.
	 * 
	 * Comentário repetido abaixo:
	 * 
	 * ATENÇÃO: Método alterado quando entrou as guias complementares do segundo grau. Em reunião no dia 19/07/2018
	 * com o contador do segundo grau Luiz Claudio, Márcia Perillo, Fernanda, Ana, Leandro Bernardes na sala da Márcia, fui informado que 
	 * as guias complementares do segundo grau não altera os dados do processo.
	 * 
	 * Fluxo:
	 * 1 - Verifica se é guia complementar
	 * 1.1 - Obtem a classe e o valor do processo
	 * 1.2 - Atualiza o processo com a classe e o valor do processo
	 * 1.3 - Gera a movimentação
	 * 
	 * @param String numeroGuiaCompleto
	 * @param FabricaConexao conexao
	 * @throws Exception
	 */
	public void alterarDadosProcessoViaNumeroGuiaComplementar(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao conexao) throws Exception {
		
		//Verifica se é guia complementar
		if(this.isGuiaComplementar(guiaEmissaoDt.getNumeroGuiaCompleto(), conexao) ) {
			
			if( processoNe == null ) {
				processoNe = new ProcessoNe();
			}
			
			String idProcTipo = null;
			String valorProcesso = null;
			
			/*
			 * Comentário repetido acima:
			 * 
			 * ATENÇÃO: Método alterado quando entrou as guias complementares do segundo grau. Em reunião no dia 19/07/2018
			 * com o contador do segundo grau Luiz Claudio, Márcia Perillo, Fernanda, Ana, Leandro Bernardes na sala da Márcia, fui informado que 
 			 * as guias complementares do segundo grau não altera os dados do processo.
			 */
			
			if( guiaEmissaoDt != null &&
				guiaEmissaoDt.getGuiaModeloDt() != null && 
				guiaEmissaoDt.getGuiaModeloDt().getId() != null &&
				guiaEmissaoDt.getValorAcao() != null &&
				guiaEmissaoDt.getId_Processo() != null &&
				guiaEmissaoDt.getGuiaModeloDt().isIdGuiaTipo(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU) ) {
				
				//Obtem a classe/proc_tipo da guia
				idProcTipo = guiaEmissaoDt.getId_ProcessoTipo();
				
				//Obtem valor da acao novo que está na guia
				valorProcesso = Funcoes.FormatarDecimal(guiaEmissaoDt.getValorAcao());
				
				if( guiaEmissaoDt.getId_Processo() != null && ( idProcTipo != null || valorProcesso != null ) ) {
					
					//Consulta processo atual
					ProcessoDt processoDtAntesAtualizar = processoNe.consultarId(guiaEmissaoDt.getId_Processo(), conexao);
					boolean alterouValorCausa = false;
					boolean alterouClasse = false;
					if( processoDtAntesAtualizar != null ) {
						
						//Verifica se alterou classe
						if( processoDtAntesAtualizar.getId_ProcessoTipo() != null && processoDtAntesAtualizar.getId_ProcessoTipo().length() > 0 ) {
							if( !processoDtAntesAtualizar.getId_ProcessoTipo().equals(idProcTipo) ) {
								alterouClasse = true;
							}
						}
						
						//Verifica se alterou valor da causa
						if( processoDtAntesAtualizar.getValor() != null && processoDtAntesAtualizar.getValor().length() > 0 ) {
							if( !processoDtAntesAtualizar.getValor().equals(valorProcesso) ) {
								alterouValorCausa = true;
							}
						}
					}
					
					//Atualiza processo
					ProcessoDt processoDt = new ProcessoDt();
					
					processoDt.setId(guiaEmissaoDt.getId_Processo());
					processoDt.setId_ProcessoTipo(idProcTipo);
					processoDt.setValor(valorProcesso);
					
					boolean processoAlterado = processoNe.alterarProcessoTipoValorCausa(processoDt, conexao);
					if( !processoAlterado ) {
						throw new MensagemException("Erro ao alterar dados do processo via Guia Complementar(número processo:" + processoDt.getProcessoNumeroCompleto()+")");
					}
					
					//Gera movimentação
					
				}
			}
		}
	}
	
	/**
	 * Método para verificar se a guia é complementar e, caso sim, alterar os dados do processo.
	 * 
	 * Comentário repetido abaixo:
	 * 
	 * ATENÇÃO: Método alterado quando entrou as guias complementares do segundo grau. Em reunião no dia 19/07/2018
	 * com o contador do segundo grau Luiz Claudio, Márcia Perillo, Fernanda, Ana, Leandro Bernardes na sala da Márcia, fui informado que 
	 * as guias complementares do segundo grau não altera os dados do processo.
	 * 
	 * @param guiaEmissaoDt
	 * @param conexao
	 * @throws Exception
	 */
	public void alterarDadosProcessoViaGuiaEmissaoDtComplementar(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao conexao) throws Exception {
		//Verifica se é guia complementar
		if( guiaEmissaoDt != null ) {
			
			if( processoNe == null ) {
				processoNe = new ProcessoNe();
			}
			
			String idProcTipo = null;
			String valorProcesso = null;
			
			/*
			 * Comentário repetido acima:
			 * 
			 * ATENÇÃO: Método alterado quando entrou as guias complementares do segundo grau. Em reunião no dia 19/07/2018
			 * com o contador do segundo grau Luiz Claudio, Márcia Perillo, Fernanda, Ana, Leandro Bernardes na sala da Márcia, fui informado que 
 			 * as guias complementares do segundo grau não altera os dados do processo.
			 */
			
			if( guiaEmissaoDt != null &&
				guiaEmissaoDt.getGuiaModeloDt() != null && 
				guiaEmissaoDt.getGuiaModeloDt().getId() != null &&
				guiaEmissaoDt.getValorAcao() != null &&
				guiaEmissaoDt.getId_Processo() != null &&
				guiaEmissaoDt.getGuiaModeloDt().isIdGuiaTipo(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU) ) {
				
				
				//Obtem a classe/proc_tipo da guia
				idProcTipo = guiaEmissaoDt.getId_ProcessoTipo();
				
				//Obtem valor da acao novo que está na guia
				valorProcesso = Funcoes.FormatarDecimal(guiaEmissaoDt.getNovoValorAcaoAtualizado());
				
				if( guiaEmissaoDt.getId_Processo() != null && ( idProcTipo != null || valorProcesso != null ) ) {
					
					//*************************************
					//Consulta processo atual
					ProcessoDt processoDtAntesAtualizar = processoNe.consultarId(guiaEmissaoDt.getId_Processo());
					boolean alterouValorCausa = false;
					boolean alterouClasse = false;
					if( processoDtAntesAtualizar != null ) {
						
						//Verifica se alterou classe
						if( processoDtAntesAtualizar.getId_ProcessoTipo() != null && processoDtAntesAtualizar.getId_ProcessoTipo().length() > 0 ) {
							if( !processoDtAntesAtualizar.getId_ProcessoTipo().equals(idProcTipo) ) {
								alterouClasse = true;
							}
						}
						
						//Verifica se alterou valor da causa
						if( processoDtAntesAtualizar.getValor() != null && processoDtAntesAtualizar.getValor().length() > 0 ) {
							if( !processoDtAntesAtualizar.getValor().equals(valorProcesso) ) {
								alterouValorCausa = true;
							}
						}
					}
					
					//*************************************
					//Atualiza processo
					ProcessoDt processoDt = new ProcessoDt();
					
					processoDt.setId(guiaEmissaoDt.getId_Processo());
					processoDt.setId_ProcessoTipo(idProcTipo);
					processoDt.setValor(valorProcesso);
					
					boolean processoAlterado = processoNe.alterarProcessoTipoValorCausa(processoDt, conexao);
					if( !processoAlterado ) {
						throw new MensagemException("Erro ao alterar dados do processo via Guia Complementar(número processo:" + processoDt.getProcessoNumeroCompleto()+")");
					}
					
					//*************************************
					//Gera movimentação
					
				}
			}
		}
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo SPG do processo.
	 * @param ProcessoDt processo
	 * @param List<GuiaEmissaoDt> listaGuiaEmissaoDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoSPG(ProcessoDt processoDt, List<GuiaEmissaoDt> listaGuiaEmissaoDt) throws Exception {
		return new GuiaSPGNe().consultarGuiaEmissaoSPG(processoDt, listaGuiaEmissaoDt);
	}
	
	/**
	 * Método para consultar Guias Emitidas pelo SSG do processo.
	 * @param ProcessoDt processo
	 * @param List<GuiaEmissaoDt> listaGuiaEmissaoDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaEmissaoSSG(ProcessoDt processoDt, List<GuiaEmissaoDt> listaGuiaEmissaoDt) throws Exception {
		return new GuiaSSGNe().consultarGuiaEmissaoSSG(processoDt, listaGuiaEmissaoDt);
	}
	
	/**
	 * Método para consultar Guia Emitida pelo SPG do processo, juntamento com os itens.
	 * @param ProcessoDt processo
	 * @param String isnGuia
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(ProcessoDt processoDt, String isnGuia) throws Exception {
		return new GuiaSPGNe().consultarGuiaEmissaoSPG(processoDt, isnGuia);
	}
	
	
	
	/**
	 * Método para verificar se precisa cobrar locomoção em dobro ou não conforme o processo tipo
	 * Se retornar true, é pq precisa.
	 * @param String processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isProcessoTipoDobrarLocomocao(String processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch( Funcoes.StringToInt(processoTipoCodigo) ) {
		
			case ProcessoTipoDt.ARRESTO :
			case ProcessoTipoDt.ARRESTO_HIPOTECA_LEGAL :
			case ProcessoTipoDt.BUSCA_APREENSAO :
			case ProcessoTipoDt.BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA :
			case ProcessoTipoDt.BUSCA_APREENSAO_CPC :
			case ProcessoTipoDt.DESPEJO_PEDIDO_LIMINAR :
			case ProcessoTipoDt.EXECUCAO_PROVISORIA :
			case ProcessoTipoDt.EXECUCAO_SENTENCA :
			case ProcessoTipoDt.EXECUCAO_HIPOTECARIA: {
				
				retorno = true;
				
				break;
			}
		}
		
		return retorno;
	}
	
	
	/**
	 * @param String processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isProcessoTipoPortePostagem_e_OrigemEstado(String processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		if( processoTipoCodigo != null && !processoTipoCodigo.isEmpty() ) {
			switch( Funcoes.StringToInt(processoTipoCodigo) ) {
			
				case ProcessoTipoDt.REQUERIMENTO_APREENSAO_VEICULO :
					
				//Linha comentada após problemas relatados nos emails do dia 29/06/2018
				//e ocorrências 2018/8667 e 2018/8873
				//case ProcessoTipoDt.BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA :
				
				case ProcessoTipoDt.CARTA_ORDEM :
				case ProcessoTipoDt.CARTA_ORDEM_CPC :
				case ProcessoTipoDt.CARTA_PRECATORIA_CPC :
				case ProcessoTipoDt.CARTA_PRECATORIA_CPP :
				case ProcessoTipoDt.CARTA_PRECATORIA : {
					
					retorno = true;
					
					break;
				}
			}
		}
		
		return retorno;
	}
	
	
	/**
	 * Método para verificar se o processo tipo exige o oficial companheiro.
	 * 
	 * @param String processoTipoCodigo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isProcessoTipoOficialCompanheiroObrigatorio(String processoTipoCodigo) throws Exception {
		boolean retorno = false;
		
		switch( Funcoes.StringToInt(processoTipoCodigo) ) {
		
			case ProcessoTipoDt.BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA :
			case ProcessoTipoDt.APREENSAO_TITULO :
			case ProcessoTipoDt.BUSCA_APREENSAO :
			case ProcessoTipoDt.BUSCA_APREENSAO_CPC :
			case ProcessoTipoDt.CAUCAO :
			case ProcessoTipoDt.APREENSAO_DEPOSITO_VENDA_COM_RESERVA_DOMINIO :
			case ProcessoTipoDt.REINTEGRACAO_DE_POSSE :
			case ProcessoTipoDt.SEQUESTRO_CPC :
			case ProcessoTipoDt.SEQUESTRO_CPP :
			case ProcessoTipoDt.POSSE_EM_NOME_NASCITURO : {
				
				retorno = true;
				
				break;
			}
		}
		
		return retorno;
	}
	
	
	/**
	 * Método para retornar o nome alterado da custa de acordo com o id da custa.
	 * @param String idCusta
	 * @return String 
	 * @throws Exception
	 */
	public String alterarNomeCustaParaImpressao(String idCusta) throws Exception {
		String retorno = null;
		
		if( idCusta != null && !idCusta.isEmpty() ) {
			switch( Integer.parseInt(idCusta) ) {
				case CustaDt.RECURSO_INOMINADO_APLICA_SE_TABELA_III :
				case CustaDt.RECURSOS : {
					retorno = "RECURSO INOMINADO";
					break;
				}
				case CustaDt.PORTE_REMESSA : {
					retorno = "PORTE REMESSA";
					break;
				}
				case CustaDt.POR_DOCUMENTO_PUBLICADO : {
					retorno = "TAXA SERVICO EDITAL(REG.16.IV)";
					break;
				}
				case CustaDt.TAXAS_DE_SERVICO_POR_DOCUMENTO_PUBLICADO_NO_DIARIO_DE_JUSTICA : {
					retorno = "TAXA SERVICO EDITAL(REG.4.IV)";
					break;
				}
				case CustaDt.TAXA_JUDICIARIA_PROCESSO : {
					retorno = "TAXA JUDICIÁRIA(CTE Artigo 114-B)";
					break;
				}
				case CustaDt.TAXA_JUDICIARIA_SERVICO_CARTA_ARREMATACAO_SITE_TJGO_ITEM_5 : {
					retorno = "TAXA JUDICIÁRIA GRS(Item.05)";
					break;
				}
				case CustaDt.TAXA_JUDICIARIA_SERVICO_CERTIDAO_SITE_TJGO_ITEM_6 : {
					retorno = "TAXA JUDICIÁRIA GRS(Item.06)";
					break;
				}
				case CustaDt.TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES : {
					retorno = "CUSTA GRS(TAXA JUDICIÁRIA GRS Item.06)";
					break;
				}
			}
		}
		
		return retorno;
	}
	
	public List<GuiaEmissaoDt> consultarGuiaEmissaoPorDataEmissao(String dataEmissao) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( dataEmissao != null ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				listaGuiaEmissaoDt = guiaEmissaoPs.consultarGuiaEmissaoPorDataEmissao(dataEmissao);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Consulta guia pendente de pagamento da prefeitura de goiânia pelo id do processo.
	 * @param String idProcesso
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaPrefeituraGoianiaAguardandoPagamento(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());		
		return obPersistencia.consultarGuiaPrefeituraGoianiaAguardandoPagamento(idProcesso);		
	}
	
	/**
	 * Consulta guia paga da prefeitura de goiânia pelo id do processo.
	 * @param String idProcesso
	 * @param FabricaConexao obFabricaConexao
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaPrefeituraGoianiaPaga(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());		
		return obPersistencia.consultarGuiaPrefeituraGoianiaPaga(idProcesso);		
	}
	
	/**
	 * Método para verificar se o Id da Comarca está preenchido.
	 * @param String idComarca
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isIdComarcaPreenchido(String idComarca) throws Exception {
		boolean retorno = true;
		
		if( idComarca == null 
			|| 
			( idComarca != null && idComarca.trim().equals("") )
			||
			( idComarca != null && idComarca.trim().equalsIgnoreCase("null") ) ) {
			
			retorno = false;
			
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se o Id do ProcessoTipo está preenchido.
	 * @param String idProcessoTipo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isIdProcessoTipoPreenchido(String idProcessoTipo) throws Exception {
		boolean retorno = true;
		
		if( idProcessoTipo == null 
			|| 
			( idProcessoTipo != null && idProcessoTipo.trim().equals("") )
			||
			( idProcessoTipo != null && idProcessoTipo.trim().equalsIgnoreCase("null") ) ) {
			
			retorno = false;
			
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se o Id da Area de Distribuição está preenchido.
	 * @param String idAreaDistribuicao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isIdAreaDistribuicaoPreenchido(String idAreaDistribuicao) throws Exception {
		boolean retorno = true;
		
		if( idAreaDistribuicao == null 
			|| 
			( idAreaDistribuicao != null && idAreaDistribuicao.trim().equals("") )
			||
			( idAreaDistribuicao != null && idAreaDistribuicao.trim().equalsIgnoreCase("null") ) ) {
			
			retorno = false;
			
		}
		
		return retorno;
	}
	
	
	public void executeConsultaPagamentosDeGuiasNoSPG() throws Exception
	{
		TenteGravarLogReceberIndicadorSPGGuiaPagas("Início Processamento");
		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		LogNe logNe = new LogNe();
		TJDataHora dataPagamento = obtenhaDataHoraUltimaAtualizarGuiasSPGPagas(logNe);
		dataPagamento.adicioneDia(-30); //Sempre processa os últimos 30 dias...
		
		FabricaConexao obFabricaConexao = null;
		
		try 
		{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			while (dataPagamento.getDataHorayyyyMMdd() < new TJDataHora().getDataHorayyyyMMdd()) 
			{			
				executeConsultaPagamentosDeGuiasNoSPG(dataPagamento, guiaSPGNe, logNe, obFabricaConexao);
				
				LogDt obLogControleDataDt = new LogDt("IntegracaoSPGGuiasPagas", "", UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "");
				obLogControleDataDt.setLogTipoCodigo(String.valueOf(LogTipoDt.IntegracaoSPGGuiasPagas));
				obLogControleDataDt.setData(dataPagamento.getDataFormatadaddMMyyyy());
				logNe.salvar(obLogControleDataDt, obFabricaConexao);
				
				dataPagamento.adicioneDia(1);
			}	
			
		} finally {			
			obFabricaConexao.fecharConexao();			
		}
		
		TenteGravarLogReceberIndicadorSPGGuiaPagas("Fim Processamento");
	}
	
	public void executeConsultaPagamentosDeGuiasNoSPGNoDiaDeHoje() throws Exception
	{
		TenteGravarLogReceberIndicadorSPGGuiaPagasHoje("Início Processamento");
		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		LogNe logNe = new LogNe();
		TJDataHora dataPagamento = new TJDataHora();
		
		FabricaConexao obFabricaConexao = null;
		
		try 
		{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			executeConsultaPagamentosDeGuiasNoSPG(dataPagamento, guiaSPGNe, logNe, obFabricaConexao);
			
		} finally {			
			obFabricaConexao.fecharConexao();			
		}
		
		TenteGravarLogReceberIndicadorSPGGuiaPagasHoje("Fim Processamento");
	}
	
	private void executeConsultaPagamentosDeGuiasNoSPG(TJDataHora dataPagamento, GuiaSPGNe guiaSPGNe, LogNe logNe, FabricaConexao obFabricaConexao) throws Exception
	{
		List<String> listaGuiasPagasSerie50 = guiaSPGNe.consultarGuiasProjudiPagasNoDia(dataPagamento);
		
		if (dataPagamento.getDataHorayyyyMMdd() == new TJDataHora().getDataHorayyyyMMdd())
			TenteGravarLogReceberIndicadorSPGGuiaPagasHoje("Quantidade de Guias: " + listaGuiasPagasSerie50.size() + "; Guias:" + listaGuiasPagasSerie50.toString());
		else 		
			TenteGravarLogReceberIndicadorSPGGuiaPagas("Quantidade de Guias: " + listaGuiasPagasSerie50.size() + "; Guias:" + listaGuiasPagasSerie50.toString());
		
		
		for (String numeroGuiaCompleto : listaGuiasPagasSerie50) {
			GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto, obFabricaConexao);
			
			if (guiaEmissaoDt != null && !guiaEmissaoDt.isGuiaPaga() && !guiaEmissaoDt.isGuiaPagamentoEstornadoBanco()) {
				obFabricaConexao.iniciarTransacao();					
				try
				{
					atualizaGuiaConsolidado(obFabricaConexao, numeroGuiaCompleto, "", dataPagamento);
				
					obFabricaConexao.finalizarTransacao();
				} catch(Exception ex) {
					obFabricaConexao.cancelarTransacao();						
					throw ex;
				}
				
			}		
		}
	}
	
	private void TenteGravarLogReceberIndicadorSPGGuiaPagas(String Mensagem) throws Exception{	
		TenteGravarLog("ReceberIndicadorSPGGuiaPagas", Mensagem);
	}
	
	private void TenteGravarLogReceberIndicadorSPGGuiaPagasHoje(String Mensagem) throws Exception{	
		TenteGravarLog("ReceberIndicadorSPGGuiaPagasHoje", Mensagem);
	}
	
	private void TenteGravarLog(String Tabela, String Mensagem) throws Exception	{	

		LogDt logDt = new LogDt(Tabela, "", UsuarioDt.SistemaProjudi, "Servidor", "", Mensagem, "");
		logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaSPGGuiasPagas));			
		new LogNe().salvar(logDt);

	}
	
	private TJDataHora obtenhaDataHoraUltimaAtualizarGuiasSPGPagas(LogNe logNe) throws Exception{
		
		LogDt obUltimoLogDt = logNe.consultarUltimoLog(String.valueOf(LogTipoDt.IntegracaoSPGGuiasPagas));
		
		TJDataHora dataEmissao = null;
		if (obUltimoLogDt != null) {
			dataEmissao = new TJDataHora(obUltimoLogDt.getData());
			dataEmissao.adicioneDia(1);
		} else {
			// Data Inicial 
			dataEmissao = new TJDataHora("01/01/2015");			
		}
		
		return dataEmissao;
	}
	
	public List<GuiaEmissaoDt> consultarGuiasPrefeituraDeGoiania(TJDataHora dataEmissao, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());		
		return obPersistencia.consultarGuiasPrefeituraDeGoiania(dataEmissao);
	}		
	
	/**
	 * Método para consultar Serventia pelo id.
	 * @param String id_serventia
	 * @return ServentiaDt
	 * @throws Exception
	 */
	public ServentiaDt consultarIdServentia(String id_serventia) throws Exception {
		return new ServentiaNe().consultarId(id_serventia);
	}
	
	
	/**
	 * Método para salvar a guia do SPG (1º grau no PJD).
	 * 
	 * @param String numeroCompletoGuia
	 * @param boolean isGuiaComplementar
	 * @param String idProcessoTipo
	 * @param String idProcesso
	 * @param String idComarca
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param FabricaConexao fabConexao
	 * 
	 * @return GuiaEmissaoDt
	 * 
	 * @throws Exception
	 */
	public GuiaEmissaoDt salvarGuiaSPGNoProjudi(String numeroCompletoGuia, boolean isGuiaComplementar, String idProcessoTipo, String idProcesso, String idComarca, String idUsuarioLog, String ipComputadorLog, FabricaConexao fabConexao) throws Exception {
		guiaItemNe = new GuiaItemNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		bairroNe = new BairroNe();
		ZonaBairroRegiaoNe zonaBairroRegiaoNe = new ZonaBairroRegiaoNe();
		processoTipoNe = new ProcessoTipoNe();
		GuiaModeloNe guiaModeloNe = new GuiaModeloNe();
		serventiaNe = new ServentiaNe();
		FabricaConexao obFabricaConexao = null;
		String isnGuiaSPG;
		
		try {
			if( fabConexao == null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}
			else {
				obFabricaConexao = fabConexao;
			}
			
			GuiaEmissaoDt guiaEmissaoDt = null;
			
			if (isGuiaComplementar)
				guiaEmissaoDt = guiaSPGNe.consultarGuiaEmissaoComplementarSPG(numeroCompletoGuia, idProcesso);
			else
				guiaEmissaoDt = guiaSPGNe.consultarGuiaEmissaoInicialSPGSemValidacao(numeroCompletoGuia);
			
			if (guiaEmissaoDt == null) {
				throw new MensagemException("Guia SPG não localizada com o número " + numeroCompletoGuia + ".");
			}
			
			isnGuiaSPG = guiaEmissaoDt.getId();
			
			ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso, obFabricaConexao);
			
			if (processoDt == null) {
				throw new MensagemException("Processo não localizado com o identificador " + idProcesso + ".");
			}
			
			ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia(), obFabricaConexao);
			
			if (guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().trim().length() > 0) {
    			NaturezaSPGDt naturezaSPGDt = new NaturezaSPGNe().consultarCodigo(guiaEmissaoDt.getNaturezaSPGCodigo());
    			
    			GuiaModeloDt guiaModeloDt = null;
    			
    			if (naturezaSPGDt != null) {
    				guiaEmissaoDt.setId_NaturezaSPG(naturezaSPGDt.getId());
    				guiaEmissaoDt.setNaturezaSPG(naturezaSPGDt.getNaturezaSPG());
    				
    				if (guiaEmissaoDt.isGuiaComplementarSPG()) {
    					guiaModeloDt = guiaModeloNe.consultarGuiaModeloProcessoTipo(obFabricaConexao, GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU, idProcessoTipo);			
    					if (guiaModeloDt == null) {
    						ProcessoTipoDt processoTipoDt = processoTipoNe.consultarId(idProcessoTipo);
    						if (processoTipoDt != null) {
    							throw new MensagemException("Não foi encontrado modelo para a guia complementar de classe " + processoTipoDt.getProcessoTipo() + ".");
    						}
    						else {
    							throw new MensagemException("Não foi localizado a classe com identificador igual a " + idProcessoTipo + ".");
    						}
    					}
    				}
    				else {
    					String idGuiaTipo = null;
    					//Guias Finais
    					if( Funcoes.isNumeroGuiaSerie09(guiaEmissaoDt.getNumeroGuiaCompleto()) && guiaEmissaoDt.getId_GuiaTipo() != null 
    						&& ( guiaEmissaoDt.getId_GuiaTipo().trim().equals("7") || guiaEmissaoDt.getId_GuiaTipo().trim().equals("8") ) ) {
    						
    						if( guiaEmissaoDt.getId_GuiaTipo().trim().equals("7") ) {
    							idGuiaTipo = GuiaTipoDt.ID_FINAL;
    						}
    						else { //guiaEmissaoDt.getId_GuiaTipo().trim().equals("8")
    							idGuiaTipo = GuiaTipoDt.ID_FINAL_ZERO;
    						}
    						
    						guiaModeloDt = guiaModeloNe.consultarGuiaModeloProcessoTipo(obFabricaConexao, idGuiaTipo, processoDt.getId_ProcessoTipo());
    						
    						//consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_FINAL, processoDt.getId_ProcessoTipo())
    					}
    					else {
    						//Guia certidão narrativa
    						if( Funcoes.isNumeroGuiaSerie09(guiaEmissaoDt.getNumeroGuiaCompleto()) && guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().trim().equals("3") 
    							&& guiaEmissaoDt.getTipoGuiaCertidaoSPG() != null && guiaEmissaoDt.getTipoGuiaCertidaoSPG().trim().equals("2") ) {
    							
    							idGuiaTipo = GuiaTipoDt.ID_GUIA_DE_CERTIDAO_NARRATIVA;
    							
    							guiaModeloDt = guiaModeloNe.consultarGuiaModeloProcessoTipo(obFabricaConexao, idGuiaTipo, processoDt.getId_ProcessoTipo());
    							
    						}
	    					//Guias Iniciais
	    					else {
		    					idGuiaTipo = GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU;
		        				
		        				if (serventiaDt != null) {
		        					
		        					guiaEmissaoDt.setId_Serventia(serventiaDt.getId());
		        					
		        					if (ServentiaSubtipoDt.isSegundoGrau(serventiaDt.getServentiaSubtipoCodigo()) || 
		        						ServentiaSubtipoDt.isTurma(serventiaDt.getServentiaSubtipoCodigo())) {
		        						
		        						idGuiaTipo = GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU;
		        					}
		        				}
		        				
		        				guiaModeloDt = guiaModeloNe.consultarGuiaModeloNaturezaSPG(obFabricaConexao, idGuiaTipo, naturezaSPGDt.getId());
	    					}
    					}
    					
    					
        				if (guiaModeloDt == null) {
        					throw new MensagemException("Não foi encontrado modelo para a guia inicial de natureza " + naturezaSPGDt.getNaturezaSPG() + ".");	
        				}
    				}
    				
    				guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
    				guiaEmissaoDt.setId_GuiaModelo(guiaModeloDt.getId());
    			}
    			else {
    				if( guiaEmissaoDt.isGuiaSomenteItensLocomocao() ) {
    					if( idProcessoTipo != null ) {
    						guiaModeloDt = guiaModeloNe.consultarGuiaModeloProcessoTipo(obFabricaConexao, GuiaTipoDt.ID_LOCOMOCAO, idProcessoTipo);
    						if( guiaModeloDt != null ) {
    							guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
    		    				guiaEmissaoDt.setId_GuiaModelo(guiaModeloDt.getId());
    						}
    					}
    				}
    			}
    		}
			
			List<LocomocaoSPGDt> locomocoesNaoUtilizadasSPG = null;
			if (serventiaDt != null) {
				locomocoesNaoUtilizadasSPG = guiaSPGNe.consultarLocomocoesNaoUtilizadas(numeroCompletoGuia, serventiaDt.getComarcaCodigo());
			}
			
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt.setId("");
			guiaEmissaoDt.setId_AreaDistribuicao(processoDt.getId_AreaDistribuicao());
			guiaEmissaoDt.setId_Processo(idProcesso);
			guiaEmissaoDt.setId_Comarca(idComarca);			
			guiaEmissaoDt.setId_Usuario(idUsuarioLog);
			guiaEmissaoDt.setId_UsuarioLog(idUsuarioLog);
			guiaEmissaoDt.setIpComputadorLog(ipComputadorLog);
			if (guiaEmissaoDt.isGuiaComplementarSPG() && idProcessoTipo != null && idProcessoTipo.trim().length() > 0)
				guiaEmissaoDt.setId_ProcessoTipo(idProcessoTipo);
			else guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			
			if (isGuiaComplementar) {
				GuiaEmissaoDt guiaInicialDoProcesso = this.consultarGuiaEmissaoInicial(idProcesso);
				
				if(guiaInicialDoProcesso == null)
					throw new MensagemException("O processo não possui guia inicial vinculada. Antes de vincular uma guia complementar é necessário vincular uma guia inicial.");
				
				guiaEmissaoDt.setId_GuiaEmissaoPrincipal(guiaInicialDoProcesso.getId());	
			}
			
			//Tratar todas as datas yyyy-MM-dd 00:00:00 para dd/MM/yyyy	
			TJDataHora dataHoraAuxiliar = null;	
			
			//Data de emissão...
			dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataEmissao());
			guiaEmissaoDt.setDataEmissao(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			
			//Data de vencimento...
			if (guiaEmissaoDt.getDataVencimento() != null) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataVencimento());	 
			} else if (dataHoraAuxiliar.getMes() == 1) {
				dataHoraAuxiliar.setDia(31);				
			} else {
				dataHoraAuxiliar.adicioneAno(1);
				dataHoraAuxiliar.setMes(1);
				dataHoraAuxiliar.setDia(31);				
			}
			guiaEmissaoDt.setDataVencimento(dataHoraAuxiliar.getDataFormatadaddMMyyyy());

			//Data de recebimento
			if (guiaEmissaoDt.getDataRecebimento() != null && guiaEmissaoDt.getDataRecebimento().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataRecebimento());
				guiaEmissaoDt.setDataRecebimento(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data de cancelamento
			if (guiaEmissaoDt.getDataCancelamento() != null && guiaEmissaoDt.getDataCancelamento().trim().length() > 0) {
				dataHoraAuxiliar.setDataddMMaaaaHHmmss(guiaEmissaoDt.getDataCancelamento());
				guiaEmissaoDt.setDataCancelamento(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data base atualização
			if (guiaEmissaoDt.getDataBaseAtualizacao() != null && guiaEmissaoDt.getDataBaseAtualizacao().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataBaseAtualizacao());
				guiaEmissaoDt.setDataBaseAtualizacao(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data base final atualização
			if (guiaEmissaoDt.getDataBaseFinalAtualizacao() != null && guiaEmissaoDt.getDataBaseFinalAtualizacao().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataBaseFinalAtualizacao());
				guiaEmissaoDt.setDataBaseFinalAtualizacao(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data vencimento DUAM
			if (guiaEmissaoDt.getDataVencimentoDUAM() != null && guiaEmissaoDt.getDataVencimentoDUAM().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataVencimentoDUAM());
				guiaEmissaoDt.setDataVencimentoDUAM(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Valor da ação...
			guiaEmissaoDt.setValorAcao(Funcoes.FormatarDecimal(guiaEmissaoDt.getValorAcao()));
			
			// Valores dos itens e locomoções...
			List<GuiaItemDt> itensAtualizados = new ArrayList<GuiaItemDt>();
			List<LocomocaoDt> locomocoesUtilizadas = new ArrayList<LocomocaoDt>();
			int quantidadeDeCustasDeLocomocao = 0;
			for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
				if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0) {
					//Valor calculado...
					guiaItemDt.setValorCalculado(Funcoes.FormatarDecimal(guiaItemDt.getValorCalculado()));
					//Valor referência...
					guiaItemDt.setValorReferencia(Funcoes.FormatarDecimal(guiaItemDt.getValorReferencia()));
					//Valor calculado original...
					if( guiaItemDt.getValorCalculadoOriginal() != null && guiaItemDt.getValorCalculadoOriginal().length() > 0 ) {
						guiaItemDt.setValorCalculadoOriginal(Funcoes.FormatarDecimal(guiaItemDt.getValorCalculadoOriginal()));
					}
					
					// Vinculando a locomoção, caso seja uma custa de locomoção e ainda não possua locomoção vinculada...
					if (locomocoesNaoUtilizadasSPG != null && guiaItemDt.isLocomocaoRegimento1074() && guiaItemDt.getLocomocaoDt() == null) {
						for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
							if (!locomocaoSPGDt.isJaVerificada()) {								
								obtenhaLocomocaoPDJDaLocomocaoSPG(zonaBairroRegiaoNe, locomocoesUtilizadas, guiaItemDt, locomocaoSPGDt);
								break;
							}
						}
					}
					
					if (guiaItemDt.isLocomocaoRegimento1074() || 
						guiaItemDt.isLocomocaoPenhoraRegimento1082() || 
						guiaItemDt.isLocomocaoAvaliacaoRegimento1084()) {
						quantidadeDeCustasDeLocomocao += 1;
					}
					
					itensAtualizados.add(guiaItemDt);
				}
			}

			//Existem intimações no SPG sem o regimento 1074 - Locomoção, vamos avaliar a custa do regimento 1082 - Penhora...
			if (locomocoesNaoUtilizadasSPG != null && locomocoesNaoUtilizadasSPG.size() > locomocoesUtilizadas.size()) {
				for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
					if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0 && guiaItemDt.isLocomocaoPenhoraRegimento1082()) {
						for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
							if (!locomocaoSPGDt.isJaVerificada()) {
								obtenhaLocomocaoPDJDaLocomocaoSPG(zonaBairroRegiaoNe, locomocoesUtilizadas, guiaItemDt, locomocaoSPGDt);
								itensAtualizados.add(guiaItemDt);								
								break;
							}
						}						
					}
				}	
			}
			
			//Existem intimações no SPG sem o regimento 1074 - Locomoção e 1082 - Penhora, vamos avaliar a custa do regimento 1084 - Avaliação...
			if (locomocoesNaoUtilizadasSPG != null && locomocoesNaoUtilizadasSPG.size() > locomocoesUtilizadas.size()) {
				for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
					if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0 && guiaItemDt.isLocomocaoAvaliacaoRegimento1084()) {
						for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
							if (!locomocaoSPGDt.isJaVerificada()) {
								obtenhaLocomocaoPDJDaLocomocaoSPG(zonaBairroRegiaoNe, locomocoesUtilizadas, guiaItemDt, locomocaoSPGDt);
								itensAtualizados.add(guiaItemDt);
								break;
							}
						}						
					}
				}	
			}
			
			// Locomocoes de conta vinculada...
			for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
				if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0 && guiaItemDt.isLocomocaoContaVinculada1058()) {
					for (LocomocaoDt locomocaoDt : locomocoesUtilizadas) {
						if (locomocaoDt.getGuiaItemContaVinculadaDt() == null && locomocaoDt.getGuiaItemDt() != null) {
							locomocaoDt.setGuiaItemContaVinculadaDt(guiaItemDt);
							locomocaoDt.getGuiaItemDt().setGuiaItemVinculadoDt(guiaItemDt);
						}
					}
				}
			}
			
			// Ajusta segunda locomoção, caso os itens de locomocao sejam superiores às locomoções utilizadas...
			if (quantidadeDeCustasDeLocomocao > locomocoesUtilizadas.size()) {
				for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
					if (guiaItemDt.getCustaDt() != null && 
						guiaItemDt.getCustaDt().getId() != null && 
						guiaItemDt.getCustaDt().getId().trim().length() > 0 && 
						(guiaItemDt.isLocomocaoRegimento1074() || 
						 guiaItemDt.isLocomocaoPenhoraRegimento1082() || 
						 guiaItemDt.isLocomocaoAvaliacaoRegimento1084()) &&
						!itensAtualizados.contains(guiaItemDt)) {
						
						for (LocomocaoDt locomocaoDt : locomocoesUtilizadas) {
							if (locomocaoDt.getGuiaItemSegundoDt() == null) {
								locomocaoDt.setGuiaItemSegundoDt(guiaItemDt);								
								break;
							} else if (locomocaoDt.getGuiaItemTerceiroDt() == null) {
								locomocaoDt.setGuiaItemTerceiroDt(guiaItemDt);
								break;
							}
						}
					}
				}
			}
			
			if( itensAtualizados != null && itensAtualizados.size() > 0 ) {
				guiaEmissaoDt.setListaGuiaItemDt(itensAtualizados);
				
				obPersistencia.inserir(guiaEmissaoDt);
				
				LogDt obLogDt = new LogDt("GuiaEmissaoSPG",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_UsuarioLog(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",guiaEmissaoDt.getPropriedades());
				
				guiaItemNe.salvar(obFabricaConexao, guiaEmissaoDt.getListaGuiaItemDt(), guiaEmissaoDt);
	
				obLog.salvar(obLogDt, obFabricaConexao);
				
				for (LocomocaoDt locomocaoDt : locomocoesUtilizadas) {
					locomocaoDt.getLocomocaoSPGDt().setIdProjudi(locomocaoDt.getId());
					guiaSPGNe.vinculeLocomocaoSPGAoProjudi(isnGuiaSPG, guiaEmissaoDt.isGuiaEmitidaSPGCapital(), locomocaoDt.getLocomocaoSPGDt().getPosicaoVetor(), locomocaoDt.getId());
				}
				
				if (guiaEmissaoDt.isGuiaComplementarSPG()) {
					//Guia Complementar: Processo de alteração dos dados do processo
					this.alterarDadosProcessoViaNumeroGuiaComplementar(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
				}
			}
			
			return guiaEmissaoDt;
		}
		finally {
			if( fabConexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Método para salvar a guia do SSG (2º grau no PJD).
	 * 
	 * @param String numeroCompletoGuia
	 * @param boolean isGuiaComplementar
	 * @param String idProcessoTipo
	 * @param String idProcesso
	 * @param String idComarca
	 * @param String idUsuarioLog
	 * @param String ipComputadorLog
	 * @param FabricaConexao fabConexao
	 * 
	 * @return GuiaEmissaoDt
	 * 
	 * @throws Exception
	 */
	public GuiaEmissaoDt salvarGuiaSSGNoProjudi(String numeroCompletoGuia, boolean isGuiaComplementar, String idProcessoTipo, String idProcesso, String idComarca, String idUsuarioLog, String ipComputadorLog, FabricaConexao fabConexao) throws Exception {
		guiaItemNe = new GuiaItemNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		bairroNe = new BairroNe();
		processoTipoNe = new ProcessoTipoNe();
		ZonaBairroRegiaoNe zonaBairroRegiaoNe = new ZonaBairroRegiaoNe();
		FabricaConexao obFabricaConexao = null;
		String isnGuiaSPG;
		
		try {
			if( fabConexao == null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			}
			else {
				obFabricaConexao = fabConexao;
			}
			
			GuiaEmissaoDt guiaEmissaoDt = null;
			
			guiaEmissaoDt = guiaSSGNe.consultarGuiaEmissaoSSG(numeroCompletoGuia);
			
			if (guiaEmissaoDt == null) {
				throw new MensagemException("Guia SPG não localizada com o número " + numeroCompletoGuia + ".");
			}
			
			isnGuiaSPG = guiaEmissaoDt.getId();
			
			ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso, obFabricaConexao);
			
			if (processoDt == null) {
				throw new MensagemException("Processo não localizado com o identificador " + idProcesso + ".");
			}
			
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia(), obFabricaConexao);
			
			if (guiaEmissaoDt.getNaturezaSPGCodigo() != null && guiaEmissaoDt.getNaturezaSPGCodigo().trim().length() > 0) {
    			NaturezaSPGDt naturezaSPGDt = new NaturezaSPGNe().consultarCodigo(guiaEmissaoDt.getNaturezaSPGCodigo());
    			
    			if (naturezaSPGDt != null) {
    				guiaEmissaoDt.setId_NaturezaSPG(naturezaSPGDt.getId());
    				guiaEmissaoDt.setNaturezaSPG(naturezaSPGDt.getNaturezaSPG());
    			} //***B
			} //***A
    				
    				GuiaModeloDt guiaModeloDt = null;
    				if (guiaEmissaoDt.isGuiaComplementarSSG()) {
    					guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(obFabricaConexao, GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU, idProcessoTipo);			
    					if (guiaModeloDt == null) {
    						ProcessoTipoDt processoTipoDt = processoTipoNe.consultarId(idProcessoTipo);
    						if (processoTipoDt != null) {
    							throw new MensagemException("Não foi encontrado modelo para a guia complementar de classe " + processoTipoDt.getProcessoTipo() + ".");
    						}
    						else {
    							throw new MensagemException("Não foi localizado a classe com identificador igual a " + idProcessoTipo + ".");
    						}
    					}
    				}
    				else {
    					
    					String idGuiaTipo = GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU;
    					
    					if( guiaEmissaoDt.getId_GuiaTipo() != null && !guiaEmissaoDt.getId_GuiaTipo().isEmpty() ) {
    						//Tipo 1 no SSG é inicial
    						if( guiaEmissaoDt.getId_GuiaTipo().equals("1") ) {
    							idGuiaTipo = GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU;
    						}
    						//Tipo 3 no SSG é complementar
    						if( guiaEmissaoDt.getId_GuiaTipo().equals("3") ) {
    							idGuiaTipo = GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU;
    						}
    					}
        				
        				guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(obFabricaConexao, idGuiaTipo, idProcessoTipo);
        				if( guiaModeloDt == null ) {
        					throw new MensagemException("Não foi encontrado modelo para a guia de segundo grau.");
        				}
    				}
    				
    				if( guiaModeloDt != null && guiaModeloDt.getId() != null ) {
    					guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
    					guiaEmissaoDt.setId_GuiaModelo(guiaModeloDt.getId());
    				}
//    			} //***B
//    		} //***A
			
			List<LocomocaoSPGDt> locomocoesNaoUtilizadasSPG = null;
			if (serventiaDt != null) {
				locomocoesNaoUtilizadasSPG = guiaSPGNe.consultarLocomocoesNaoUtilizadas(numeroCompletoGuia, serventiaDt.getComarcaCodigo());
			}
			
			GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			guiaEmissaoDt.setId("");
			guiaEmissaoDt.setId_AreaDistribuicao(processoDt.getId_AreaDistribuicao());
			guiaEmissaoDt.setId_Processo(idProcesso);
			guiaEmissaoDt.setId_Comarca(idComarca);			
			guiaEmissaoDt.setId_Usuario(idUsuarioLog);
			guiaEmissaoDt.setId_UsuarioLog(idUsuarioLog);
			guiaEmissaoDt.setIpComputadorLog(ipComputadorLog);
			if (guiaEmissaoDt.isGuiaComplementarSSG() && idProcessoTipo != null && idProcessoTipo.trim().length() > 0) {
				guiaEmissaoDt.setId_ProcessoTipo(idProcessoTipo);
			}
			else {
				guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			}
			
			if (isGuiaComplementar) {
				GuiaEmissaoDt guiaInicialDoProcesso = this.consultarGuiaEmissaoInicial(idProcesso);
				
				if(guiaInicialDoProcesso == null)
					throw new MensagemException("O processo não possui guia inicial vinculada. Antes de vincular uma guia complementar é necessário vincular uma guia inicial.");
				
				guiaEmissaoDt.setId_GuiaEmissaoPrincipal(guiaInicialDoProcesso.getId());	
			}
			
			//Tratar todas as datas yyyy-MM-dd 00:00:00 para dd/MM/yyyy	
			TJDataHora dataHoraAuxiliar = null;	
			
			//Data de emissão...
			dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataEmissao());
			guiaEmissaoDt.setDataEmissao(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			
			//Data de vencimento...
			if (guiaEmissaoDt.getDataVencimento() != null) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataVencimento());	 
			} else if (dataHoraAuxiliar.getMes() == 1) {
				dataHoraAuxiliar.setDia(31);				
			} else {
				dataHoraAuxiliar.adicioneAno(1);
				dataHoraAuxiliar.setMes(1);
				dataHoraAuxiliar.setDia(31);				
			}
			guiaEmissaoDt.setDataVencimento(dataHoraAuxiliar.getDataFormatadaddMMyyyy());

			//Data de recebimento
			if (guiaEmissaoDt.getDataRecebimento() != null && guiaEmissaoDt.getDataRecebimento().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataRecebimento());
				guiaEmissaoDt.setDataRecebimento(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data de cancelamento
			if (guiaEmissaoDt.getDataCancelamento() != null && guiaEmissaoDt.getDataCancelamento().trim().length() > 0) {
				dataHoraAuxiliar.setDataddMMaaaaHHmmss(guiaEmissaoDt.getDataCancelamento());
				guiaEmissaoDt.setDataCancelamento(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data base atualização
			if (guiaEmissaoDt.getDataBaseAtualizacao() != null && guiaEmissaoDt.getDataBaseAtualizacao().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataBaseAtualizacao());
				guiaEmissaoDt.setDataBaseAtualizacao(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data base final atualização
			if (guiaEmissaoDt.getDataBaseFinalAtualizacao() != null && guiaEmissaoDt.getDataBaseFinalAtualizacao().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataBaseFinalAtualizacao());
				guiaEmissaoDt.setDataBaseFinalAtualizacao(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Data vencimento DUAM
			if (guiaEmissaoDt.getDataVencimentoDUAM() != null && guiaEmissaoDt.getDataVencimentoDUAM().trim().length() > 0) {
				dataHoraAuxiliar = Funcoes.BancoTJDataHora(guiaEmissaoDt.getDataVencimentoDUAM());
				guiaEmissaoDt.setDataVencimentoDUAM(dataHoraAuxiliar.getDataFormatadaddMMyyyy());
			}
			
			//Valor da ação...
			guiaEmissaoDt.setValorAcao(Funcoes.FormatarDecimal(guiaEmissaoDt.getValorAcao()));
			
			// Valores dos itens e locomoções...
			List<GuiaItemDt> itensAtualizados = new ArrayList<GuiaItemDt>();
			List<LocomocaoDt> locomocoesUtilizadas = new ArrayList<LocomocaoDt>();
			int quantidadeDeCustasDeLocomocao = 0;
			for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
				if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0) {
					//Valor calculado...
					guiaItemDt.setValorCalculado(Funcoes.FormatarDecimal(guiaItemDt.getValorCalculado()));
					//Valor referência...
					guiaItemDt.setValorReferencia(Funcoes.FormatarDecimal(guiaItemDt.getValorReferencia()));
					//Valor calculado original...
					if( guiaItemDt.getValorCalculadoOriginal() != null && guiaItemDt.getValorCalculadoOriginal().length() > 0 ) {
						guiaItemDt.setValorCalculadoOriginal(Funcoes.FormatarDecimal(guiaItemDt.getValorCalculadoOriginal()));
					}
					
					// Vinculando a locomoção, caso seja uma custa de locomoção e ainda não possua locomoção vinculada...
					if (locomocoesNaoUtilizadasSPG != null && guiaItemDt.isLocomocaoRegimento1074() && guiaItemDt.getLocomocaoDt() == null) {
						for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
							if (!locomocaoSPGDt.isJaVerificada()) {								
								obtenhaLocomocaoPDJDaLocomocaoSPG(zonaBairroRegiaoNe, locomocoesUtilizadas, guiaItemDt, locomocaoSPGDt);
								break;
							}
						}
					}
					
					if (guiaItemDt.isLocomocaoRegimento1074() || 
						guiaItemDt.isLocomocaoPenhoraRegimento1082() || 
						guiaItemDt.isLocomocaoAvaliacaoRegimento1084()) {
						quantidadeDeCustasDeLocomocao += 1;
					}
					
					itensAtualizados.add(guiaItemDt);
				}
			}

			//Existem intimações no SPG sem o regimento 1074 - Locomoção, vamos avaliar a custa do regimento 1082 - Penhora...
			if (locomocoesNaoUtilizadasSPG != null && locomocoesNaoUtilizadasSPG.size() > locomocoesUtilizadas.size()) {
				for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
					if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0 && guiaItemDt.isLocomocaoPenhoraRegimento1082()) {
						for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
							if (!locomocaoSPGDt.isJaVerificada()) {
								obtenhaLocomocaoPDJDaLocomocaoSPG(zonaBairroRegiaoNe, locomocoesUtilizadas, guiaItemDt, locomocaoSPGDt);
								itensAtualizados.add(guiaItemDt);								
								break;
							}
						}						
					}
				}	
			}
			
			//Existem intimações no SPG sem o regimento 1074 - Locomoção e 1082 - Penhora, vamos avaliar a custa do regimento 1084 - Avaliação...
			if (locomocoesNaoUtilizadasSPG != null && locomocoesNaoUtilizadasSPG.size() > locomocoesUtilizadas.size()) {
				for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
					if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0 && guiaItemDt.isLocomocaoAvaliacaoRegimento1084()) {
						for (LocomocaoSPGDt locomocaoSPGDt : locomocoesNaoUtilizadasSPG) {
							if (!locomocaoSPGDt.isJaVerificada()) {
								obtenhaLocomocaoPDJDaLocomocaoSPG(zonaBairroRegiaoNe, locomocoesUtilizadas, guiaItemDt, locomocaoSPGDt);
								itensAtualizados.add(guiaItemDt);
								break;
							}
						}						
					}
				}	
			}
			
			// Locomocoes de conta vinculada...
			for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
				if (guiaItemDt.getCustaDt() != null && guiaItemDt.getCustaDt().getId() != null && guiaItemDt.getCustaDt().getId().trim().length() > 0 && guiaItemDt.isLocomocaoContaVinculada1058()) {
					for (LocomocaoDt locomocaoDt : locomocoesUtilizadas) {
						if (locomocaoDt.getGuiaItemContaVinculadaDt() == null && locomocaoDt.getGuiaItemDt() != null) {
							locomocaoDt.setGuiaItemContaVinculadaDt(guiaItemDt);
							locomocaoDt.getGuiaItemDt().setGuiaItemVinculadoDt(guiaItemDt);
						}
					}
				}
			}
			
			// Ajusta segunda locomoção, caso os itens de locomocao sejam superiores às locomoções utilizadas...
			if (quantidadeDeCustasDeLocomocao > locomocoesUtilizadas.size()) {
				for(GuiaItemDt guiaItemDt : guiaEmissaoDt.getListaGuiaItemDt()) {
					if (guiaItemDt.getCustaDt() != null && 
						guiaItemDt.getCustaDt().getId() != null && 
						guiaItemDt.getCustaDt().getId().trim().length() > 0 && 
						(guiaItemDt.isLocomocaoRegimento1074() || 
						 guiaItemDt.isLocomocaoPenhoraRegimento1082() || 
						 guiaItemDt.isLocomocaoAvaliacaoRegimento1084()) &&
						!itensAtualizados.contains(guiaItemDt)) {
						
						for (LocomocaoDt locomocaoDt : locomocoesUtilizadas) {
							if (locomocaoDt.getGuiaItemSegundoDt() == null) {
								locomocaoDt.setGuiaItemSegundoDt(guiaItemDt);								
								break;
							} else if (locomocaoDt.getGuiaItemTerceiroDt() == null) {
								locomocaoDt.setGuiaItemTerceiroDt(guiaItemDt);
								break;
							}
						}
					}
				}
			}
			
			if( itensAtualizados != null && itensAtualizados.size() > 0 ) {
				guiaEmissaoDt.setListaGuiaItemDt(itensAtualizados);
				
				obPersistencia.inserir(guiaEmissaoDt);
				
				LogDt obLogDt = new LogDt("GuiaEmissaoSSG",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_UsuarioLog(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",guiaEmissaoDt.getPropriedades());
				
				guiaItemNe.salvar(obFabricaConexao, guiaEmissaoDt.getListaGuiaItemDt(), guiaEmissaoDt);
	
				obLog.salvar(obLogDt, obFabricaConexao);
				
				for (LocomocaoDt locomocaoDt : locomocoesUtilizadas) {
					locomocaoDt.getLocomocaoSPGDt().setIdProjudi(locomocaoDt.getId());
					guiaSPGNe.vinculeLocomocaoSPGAoProjudi(isnGuiaSPG, guiaEmissaoDt.isGuiaEmitidaSPGCapital(), locomocaoDt.getLocomocaoSPGDt().getPosicaoVetor(), locomocaoDt.getId());
				}
				
				if (guiaEmissaoDt.isGuiaComplementarSPG()) {
					//Guia Complementar: Processo de alteração dos dados do processo
					this.alterarDadosProcessoViaNumeroGuiaComplementar(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
				}
			}
			
			return guiaEmissaoDt;
		}
		finally {
			if( fabConexao == null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}

	private void obtenhaLocomocaoPDJDaLocomocaoSPG(ZonaBairroRegiaoNe zonaBairroRegiaoNe, List<LocomocaoDt> locomocoesUtilizadas, GuiaItemDt guiaItemDt, LocomocaoSPGDt locomocaoSPGDt) throws Exception, MensagemException {
		// Consulta os bairros...
		BairroDt bairroDt = new BairroNe().consultarBairroCodigoSPG(locomocaoSPGDt.getCodigoMunicipio(), locomocaoSPGDt.getCodigoBairro());
		
		if (bairroDt == null)
			throw new MensagemException("Bairro não cadastrado no Projudi. Município " + locomocaoSPGDt.getCodigoMunicipio() + " e Bairro " + locomocaoSPGDt.getCodigoBairro() + ". Entre em contato com o suporte.");
		
		ZonaBairroRegiaoDt zonaBairroRegiaoDt = zonaBairroRegiaoNe.consultarIdBairro(bairroDt.getId());
		if (zonaBairroRegiaoDt == null)
			throw new MensagemException("Bairro não zoneado no Projudi. Município " + bairroDt.getCidade() + " e Bairro " + bairroDt.getBairro() + ". Entre em contato com o suporte.");
			
		LocomocaoDt locomocaoDt = obtenhaLocomocao(guiaItemDt, locomocaoSPGDt, bairroDt, zonaBairroRegiaoDt);
		
		// Seta a locomoção como utilizada...
		locomocoesUtilizadas.add(locomocaoDt);
		
	}

	private LocomocaoDt obtenhaLocomocao(GuiaItemDt guiaItemDt, LocomocaoSPGDt locomocaoSPGDt, BairroDt bairroDt, ZonaBairroRegiaoDt zonaBairroRegiaoDt) {
		LocomocaoDt locomocaoDt = new LocomocaoDt();
		
		// Setando flag para identificar que essa locomoção já foi verificada...
		locomocaoSPGDt.setJaVerificada(true);
		
		// Setando o bairro...
		locomocaoDt.setBairroDt(bairroDt);
		
		// Setando a zona...
		ZonaDt zonaDt = new ZonaDt();
		zonaDt.setId(zonaBairroRegiaoDt.getId_Zona());
		zonaDt.setZona(zonaBairroRegiaoDt.getZona());
		locomocaoDt.setZonaDt(zonaDt);
		
		// Setando a região...
		RegiaoDt regiaoDt = new RegiaoDt();
		regiaoDt.setId(zonaBairroRegiaoDt.getId_Regiao());
		regiaoDt.setRegiao(zonaBairroRegiaoDt.getRegiao());
		locomocaoDt.setRegiaoDt(regiaoDt);
		
		// Setando a locomoção do SPG...
		locomocaoDt.setLocomocaoSPGDt(locomocaoSPGDt);		
		
		// Setando o relacionamento bi-direcional...
		guiaItemDt.setLocomocaoDt(locomocaoDt);
		locomocaoDt.setGuiaItemDt(guiaItemDt);
		
		// Setando a finalidade...
		locomocaoDt.setFinalidadeCodigo(40);
		return locomocaoDt;
	}
	
	public void tenteSincronizeBaseProjudiSPG(ProcessoDt processoDt, GuiaEmissaoDt guiaEmissaoDt, UsuarioNe UsuarioSessao) throws Exception {
		try {
			atualizaGuiaSPGProcessoHibrido(processoDt.isProcessoHibrido(), guiaEmissaoDt.getNumeroGuiaCompleto(), processoDt);
			
			sincronizeBaseProjudiSPG(processoDt, guiaEmissaoDt, UsuarioSessao);
		}
		catch (MensagemException ex) { 
			throw ex;
		}
		catch (Exception ex) {}
	}
	
	public void tenteSincronizeBaseProjudiSSG(ProcessoDt processoDt, GuiaEmissaoDt guiaEmissaoDt, UsuarioNe UsuarioSessao) throws Exception {
		try {
			sincronizeBaseProjudiSSG(processoDt, guiaEmissaoDt, UsuarioSessao);
		}
		catch (MensagemException ex) { 
			throw ex;
		}
		catch (Exception ex) {}
	}
	
	/**
	 * Método para atualizar a guia do SPG vinculada ao processo hibrido.
	 * 
	 * @param boolean isProcessoHibrido
	 * @param String numeroGuiaCompleto
	 * @param ProcessoDt processoDt
	 * @throws Exception
	 */
	private void atualizaGuiaSPGProcessoHibrido(boolean isProcessoHibrido, String numeroGuiaCompleto, ProcessoDt processoDt) throws Exception {
		if( isProcessoHibrido &&
			numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() && 
			processoDt != null &&
			processoDt.getId() != null ) {
			
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			
			GuiaEmissaoDt guiaSPG = guiaSPGNe.consultarGuiaEmissaoSPG(numeroGuiaCompleto);
			
			if( guiaSPG != null ) {
				
				//Extrai o numero do processo PJD numero + digito + ano
				String numeroProcesso[] = processoDt.getProcessoNumeroCompleto().split("\\.");
				String numeroProcessoPJD = numeroProcesso[0] + numeroProcesso[1] + numeroProcesso[2];
				
				//Gera o número do processo antigo com base no número do processo do PJD
				String numeroProcessoAntigoSPG = processoDt.getAno().trim();
				if( processoDt.getAno().trim().length() < 4 ) {
					numeroProcessoAntigoSPG = Funcoes.preencheZeros(Funcoes.StringToLong(numeroProcessoAntigoSPG), 4);
				}
				numeroProcessoAntigoSPG += Funcoes.completarZeros(processoDt.getProcessoNumeroSimples().trim(), 7);
				numeroProcessoAntigoSPG += Funcoes.gerarDigitoNumeroProcessoAntigoSPG(numeroProcessoAntigoSPG);
				numeroProcessoAntigoSPG += "0000";
				
				guiaSPGNe.atualizaGuiaVinculadaProcessoHibrido(guiaSPG.getId(), numeroProcessoPJD, numeroProcessoAntigoSPG, guiaSPG.getComarcaCodigo(), numeroGuiaCompleto, UsuarioDt.SistemaProjudi, "127.0.0.1");
			}
			
		}
	}

	private void sincronizeBaseProjudiSPG(ProcessoDt processoDt, GuiaEmissaoDt guiaEmissaoDt, UsuarioNe UsuarioSessao) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {			
			ServentiaDt serventiaDt = null;
			serventiaDt = this.consultarIdServentia(processoDt.getId_Serventia());
			
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			
			// Importar a guia do SPG para o Projudi...
			if (guiaEmissaoDt.isGuiaEmitidaSPG()) {
				if (Funcoes.isNumeroGuiaSerie50Valido(guiaEmissaoDt.getNumeroGuiaCompleto()) && (guiaEmissaoDt.getInfoPatenteSPG().trim().length() > 0 && !guiaEmissaoDt.getInfoPatenteSPG().trim().equals(guiaEmissaoDt.getNumeroGuiaCompleto().trim()))) {
					GuiaEmissaoDt guiaEmissaoProjudi = this.consultarGuiaEmissaoNumeroGuia(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
					
					if (guiaEmissaoProjudi == null) 
						throw new MensagemException("Guia série 50 não existe na base do Processo Judicial. Entre em contato com o suporte!");
					
					if (guiaEmissaoProjudi.getId_Processo() != null && !guiaEmissaoProjudi.getId_Processo().trim().equalsIgnoreCase(processoDt.getId()))
						throw new MensagemException("Guia série 50 existe na base do Processo Judicial, porém está vinculada a outro processo. Entre em contato com o suporte!");
					
					String idComarca = null;					
					if (serventiaDt != null) idComarca = serventiaDt.getId_Comarca();
					
					this.vinculaGuiaProcesso(guiaEmissaoProjudi.getId(), guiaEmissaoProjudi.getNumeroGuiaCompleto(), processoDt.getId(), processoDt.getId_Serventia(), idComarca, UsuarioDt.SistemaProjudi, "127.0.0.1", obFabricaConexao, null);
				} else {
					obFabricaConexao.iniciarTransacao();
					guiaEmissaoDt = importarGuiaSPGParaProjudi(processoDt, serventiaDt, guiaEmissaoDt, obFabricaConexao);
					obFabricaConexao.finalizarTransacao();
					if (guiaEmissaoDt == null) return;	
				}
			}
			
			GuiaEmissaoDt guiaSPG = guiaSPGNe.consultarGuiaEmissaoSPG(guiaEmissaoDt.getNumeroGuiaCompleto());
			
			if (guiaSPG != null) {
				boolean isGuiaPagaProjudi = this.isGuiaPaga(guiaEmissaoDt);	
				TJDataHora dataPagamentoSAJ = guiaSPGNe.consultarDataDePagamentoGuia(guiaEmissaoDt.getNumeroGuiaCompleto()); 
				boolean isGuiaPagaSAJ =  dataPagamentoSAJ != null;
				
				// Atualiza repasse caso esteja incorreto...
				//TODO repasse : Em conversa com o márcio, verifiquei se terei prejuízo neste processo ao retirar o cadastro de repasse para estes casos de sincronizar a guia no SPG
				//A principio não tem prejuízo, então vou comentar este trecho para evitar o problema que ocorreu na ocorrência 2018/15955
				//O problema foi o sequinte: Uma ISN foi encontrada em duas guias no capital e interior. E dentro deste método estava consultando primeiro o repasse pelo ISN. Logo encontrando no capital não consultava no 
				//interior, então a guia que era do interior ficava com o cadastro do repasse errado.
				//atualizaRepasseEInformacaoDePagamento(serventiaDt, guiaSPG, guiaEmissaoDt, isGuiaPagaProjudi, isGuiaPagaSAJ);	
				
				// Conferir se a comarca está preenchida no SPG e atualizeInfoLocalCertidao...			
				if (guiaSPG.getInfoLocalCertidaoSPG() == null || guiaSPG.getInfoLocalCertidaoSPG().trim().length() == 0)
					guiaSPGNe.atualizeInfoLocalCertidao(guiaEmissaoDt);
					
				// Atualizar pagamento da guia no Projudi...
				if (isGuiaPagaSAJ && !isGuiaPagaProjudi) {
					obFabricaConexao.iniciarTransacao();
					atualizaGuiaConsolidado(obFabricaConexao, guiaEmissaoDt.getNumeroGuiaCompleto(), processoDt.getId(), dataPagamentoSAJ);
					obFabricaConexao.finalizarTransacao();
				}
			}
		} catch (MensagemException ex) {
			throw ex;
		} catch (Exception ex) {			
			try 
			{
				obFabricaConexao.cancelarTransacao();
				if( UsuarioSessao != null ) {
					LogDt logDt = new LogDt("GuiaEmissao", "", UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), String.valueOf(LogTipoDt.Erro), "",  obtenhaConteudoLog(ex, UsuarioSessao));
					new LogNe().salvarErro(logDt);
				}
			} catch (Exception e) {}						
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
	}
	
	private void sincronizeBaseProjudiSSG(ProcessoDt processoDt, GuiaEmissaoDt guiaEmissaoDt, UsuarioNe UsuarioSessao) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {			
			ServentiaDt serventiaDt = null;
			serventiaDt = this.consultarIdServentia(processoDt.getId_Serventia());
			
			GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			
			// Importar a guia do SPG para o Projudi...
			if (guiaEmissaoDt.isGuiaEmitidaSSG()) {
				if (Funcoes.isNumeroGuiaSerie50Valido(guiaEmissaoDt.getNumeroGuiaCompleto())) {
					GuiaEmissaoDt guiaEmissaoProjudi = this.consultarGuiaEmissaoNumeroGuia(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
					
					if (guiaEmissaoProjudi == null) 
						throw new MensagemException("Guia série 50 não existe na base do Processo Judicial. Entre em contato com o suporte!");
					
					if (guiaEmissaoProjudi.getId_Processo() != null && !guiaEmissaoProjudi.getId_Processo().trim().equalsIgnoreCase(processoDt.getId()))
						throw new MensagemException("Guia série 50 existe na base do Processo Judicial, porém está vinculada a outro processo. Entre em contato com o suporte!");
					
					String idComarca = null;					
					if (serventiaDt != null) idComarca = serventiaDt.getId_Comarca();
					
					this.vinculaGuiaProcesso(guiaEmissaoProjudi.getId(), guiaEmissaoProjudi.getNumeroGuiaCompleto(), processoDt.getId(), processoDt.getId_Serventia(), idComarca, UsuarioDt.SistemaProjudi, "127.0.0.1", obFabricaConexao, null);
				}
				else {
					obFabricaConexao.iniciarTransacao();
					guiaEmissaoDt = importarGuiaSSGParaProjudi(processoDt, serventiaDt, guiaEmissaoDt, obFabricaConexao);
					obFabricaConexao.finalizarTransacao();
					if (guiaEmissaoDt == null) return;	
				}
			}
			
			GuiaEmissaoDt guiaSSG = guiaSSGNe.consultarGuiaEmissaoSSG(guiaEmissaoDt.getNumeroGuiaCompleto());
			
			if (guiaSSG != null) {
				boolean isGuiaPagaProjudi = this.isGuiaPaga(guiaEmissaoDt);	
				TJDataHora dataPagamentoSAJ = guiaSPGNe.consultarDataDePagamentoGuia(guiaEmissaoDt.getNumeroGuiaCompleto()); 
				boolean isGuiaPagaSAJ =  dataPagamentoSAJ != null;
				
				// Atualiza repasse caso esteja incorreto...
				//TODO repasse : Em conversa com o márcio, verifiquei se terei prejuízo neste processo ao retirar o cadastro de repasse para estes casos de sincronizar a guia no SPG
				//A principio não tem prejuízo, então vou comentar este trecho para evitar o problema que ocorreu na ocorrência 2018/15955
				//O problema foi o sequinte: Uma ISN foi encontrada em duas guias no capital e interior. E dentro deste método estava consultando primeiro o repasse pelo ISN. Logo encontrando no capital não consultava no 
				//interior, então a guia que era do interior ficava com o cadastro do repasse errado.
				//atualizaRepasseEInformacaoDePagamento(serventiaDt, guiaSSG, guiaEmissaoDt, isGuiaPagaProjudi, isGuiaPagaSAJ);	
				
				// Conferir se a comarca está preenchida no SSG e atualizeInfoLocalCertidao...			
				if (guiaSSG.getInfoLocalCertidaoSPG() == null || guiaSSG.getInfoLocalCertidaoSPG().trim().length() == 0) {
					
				}
				
				// Atualizar pagamento da guia no Projudi...
				if (isGuiaPagaSAJ && !isGuiaPagaProjudi) {
					obFabricaConexao.iniciarTransacao();
					atualizaGuiaConsolidado(obFabricaConexao, guiaEmissaoDt.getNumeroGuiaCompleto(), processoDt.getId(), dataPagamentoSAJ);
					obFabricaConexao.finalizarTransacao();
				}
			}
		}
		catch (MensagemException ex) {
			throw ex;
		}
		catch (Exception ex) {			
			try {
				obFabricaConexao.cancelarTransacao();
				LogDt logDt = new LogDt("GuiaEmissao", "", UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), String.valueOf(LogTipoDt.Erro), "",  obtenhaConteudoLog(ex, UsuarioSessao));
				new LogNe().salvarErro(logDt);
			}
			catch (Exception e) {}						
		}
		finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
	}
	
//	//TODO repasse : Comentei a chamada desse método no momento da sincronização da guia entre SPG e PJD.
//	private void atualizaRepasseEInformacaoDePagamento(ServentiaDt serventiaDt, GuiaEmissaoDt guiaSPG, GuiaEmissaoDt guiaEmissaoDt, boolean isGuiaPagaProjudi, boolean isGuiaPagaSAJ) throws Exception {
//		if (serventiaDt.temCodigoExterno()) {
//			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
//			
//			String codigoServentia = Funcoes.completarZeros(serventiaDt.getServentiaCodigoExterno(),3);
//			
//			//TODO repasse Não pode utilizar id(isn) neste caso
////			InfoRepasseSPGDt infoRepasse = guiaSPGNe.consultarInfoRepasseByISNGuia(guiaSPG.getId(), ComarcaDt.GOIANIA);
////			if (infoRepasse == null) infoRepasse = guiaSPGNe.consultarInfoRepasseByISNGuia(guiaSPG.getId(), ComarcaDt.APARECIDA_DE_GOIANIA);
//			InfoRepasseSPGDt infoRepasse = guiaSPGNe.consultarInfoRepasseByISNGuia(guiaSPG.getId(), guiaSPG.getComarcaCodigo());
//			
//			if (isGuiaPagaProjudi || isGuiaPagaSAJ) {			
//				if (infoRepasse == null) {
//					//Gera o info repasse...
//					//TODO repasse o isn está da objeto consultado do guiaSPG mas a comarca está no objeto guiaemissaoDt
//					//guiaSPGNe.inserirGuiaInfoRepasse(guiaSPG.getId(), codigoServentia, new TJDataHora(), guiaEmissaoDt.getComarcaCodigo(), UsuarioDt.SistemaProjudi, "127.0.0.1");
//					guiaSPGNe.inserirGuiaInfoRepasse(guiaSPG.getId(), codigoServentia, new TJDataHora(), guiaSPG.getComarcaCodigo(), guiaSPG.getNumeroGuiaCompleto(), UsuarioDt.SistemaProjudi, "127.0.0.1");
//				} else {
//					if (infoRepasse.getCodgEscrivania() == null || !infoRepasse.getCodgEscrivania().trim().equalsIgnoreCase(codigoServentia.trim())) {
//						// Se o código da serventia estiver diferente, altera o repasse...
//						//guiaSPGNe.atualizaGuiaInfoRepasseSemValidacao(guiaSPG.getId(), codigoServentia, guiaEmissaoDt, UsuarioDt.SistemaProjudi, "127.0.0.1");
//					} else if (infoRepasse.getDataRepasseTJDataHora() == null) {
//						// Se a data do repasse estiver vazia , altera o repasse...										
//						//guiaSPGNe.atualizaGuiaInfoRepasseSemValidacao(guiaSPG.getId(), codigoServentia, guiaEmissaoDt, UsuarioDt.SistemaProjudi, "127.0.0.1");
//					} else if (Funcoes.StringToInt(infoRepasse.getPercRepasse()) <= 0) {
//						// Se o percentual não estiver informado, altera o repasse...	
//						//guiaSPGNe.atualizaGuiaInfoRepasseSemValidacao(guiaSPG.getId(), codigoServentia, guiaEmissaoDt, UsuarioDt.SistemaProjudi, "127.0.0.1");							
//					} 										
//				}						
//			}
//		}
//	}
	
	private GuiaEmissaoDt importarGuiaSPGParaProjudi(ProcessoDt processoDt, ServentiaDt serventiaDt, GuiaEmissaoDt guiaSPG, FabricaConexao obFabricaConexao) throws Exception {
		if (processoDt != null && guiaSPG != null && (guiaSPG.getNumeroProcesso() != null || processoDt != null)) {
			return this.salvarGuiaSPGNoProjudi(guiaSPG.getNumeroGuiaCompleto(), guiaSPG.isGuiaComplementarSPG(), processoDt.getId_ProcessoTipo(), processoDt.getId(), serventiaDt.getId_Comarca(), UsuarioDt.SistemaProjudi, "127.0.0.1", obFabricaConexao);			
		}
		return null;
	}
	
	private GuiaEmissaoDt importarGuiaSSGParaProjudi(ProcessoDt processoDt, ServentiaDt serventiaDt, GuiaEmissaoDt guiaSSG, FabricaConexao obFabricaConexao) throws Exception {
		if (processoDt != null && guiaSSG != null && (guiaSSG.getNumeroProcesso() != null || processoDt != null)) {
			return this.salvarGuiaSSGNoProjudi(guiaSSG.getNumeroGuiaCompleto(), guiaSSG.isGuiaComplementarSSG(), processoDt.getId_ProcessoTipo(), processoDt.getId(), serventiaDt.getId_Comarca(), UsuarioDt.SistemaProjudi, "127.0.0.1", obFabricaConexao);			
		}
		return null;
	}
	
	private String obtenhaConteudoLog(Exception ex, UsuarioNe usuarioNe){
    	String dadosUsuarioLog = "Usuário: "+usuarioNe.getUsuarioDt().getUsuario() +" -- Perfil: "+ usuarioNe.getUsuarioDt().getGrupo();
    	try{
    		return dadosUsuarioLog+Funcoes.obtenhaConteudoPrimeiraExcecao(ex);
    	}catch(Exception e){
    		return ex.getMessage();
    	}    	
    }
	
	public boolean gerarGuiaDescontada(String idGuiaReferencia, String porcentagemDesconto, String idUsuario, String ipComputadorLog) throws Exception {
		
		if( this.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(idGuiaReferencia) == null ) {
			throw new MensagemException(Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA));
		}
		
		if( idGuiaReferencia == null || idGuiaReferencia.isEmpty() ) {
			throw new MensagemException("Guia de Referência não identificada.");
		}
		
		if( porcentagemDesconto == null || porcentagemDesconto.isEmpty() ) {
			throw new MensagemException("Porcentagem de desconto não informado.");
		}
		
		//Consultar a guia referencia
		GuiaEmissaoDt guiaEmissaoReferenciaDt = this.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(idGuiaReferencia);
		if( guiaEmissaoReferenciaDt == null ) {
			throw new MensagemException("Guia não localizada com as condições necessária. (Obs.: A guia deve estar com o status de Aguardando Pagamento e não estar vencida.)");
		}
		
		//Consulta dados do processo
		if( guiaEmissaoReferenciaDt.getId_Processo() != null && !guiaEmissaoReferenciaDt.getId_Processo().isEmpty() ) {
			ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoReferenciaDt.getId_Processo());
			
			ServentiaDt serventiaDt = null;
			ComarcaDt comarcaDt = null;
			if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
				serventiaDt = this.consultarServentiaProcesso(processoDt.getServentiaCodigo());
				if( serventiaDt != null ) {
					comarcaDt = this.consultarComarca(serventiaDt.getId_Comarca());
					processoDt.setComarca(serventiaDt.getComarca());
				}
			}
			
			if( processoDt != null ) {
				guiaEmissaoReferenciaDt.setNumeroProcesso(processoDt.getProcessoNumero());
				guiaEmissaoReferenciaDt.setId_Serventia(processoDt.getId_Serventia());
				guiaEmissaoReferenciaDt.setServentia(processoDt.getServentia());
				guiaEmissaoReferenciaDt.setComarca(processoDt.getComarca());
				guiaEmissaoReferenciaDt.setComarcaCodigo(processoDt.getComarcaCodigo());
			}
		}
		
		//Consulta GuiaModeloDt para obter o flag do grau
		if( guiaEmissaoReferenciaDt.getGuiaModeloDt() != null && guiaEmissaoReferenciaDt.getGuiaModeloDt().getId() != null ) {
			GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarId(guiaEmissaoReferenciaDt.getGuiaModeloDt().getId());
			if( guiaModeloDt != null ) {
				guiaEmissaoReferenciaDt.setGuiaModeloDt(guiaModeloDt);
			}
		}
		
		//Verifica se guia já tem desconto aplicada e se já é guia de referência
//		if( !guiaEmissaoReferenciaDt.isGuiaLivreParaUsoDescontoParcelamento() ) {
//			throw new MensagemException("Guia já utilizada para desconto ou parcelamento.");
//		}
		
		//Consultar itens da guia
		GuiaItemNe guiaItemNe = new GuiaItemNe();
		List<GuiaItemDt> listaGuiaItemDt_GuiaReferencia = guiaItemNe.consultarItensGuia(guiaEmissaoReferenciaDt.getId());
		if( listaGuiaItemDt_GuiaReferencia == null || listaGuiaItemDt_GuiaReferencia.isEmpty() ) {
			throw new MensagemException("Itens da Guia de Referência não encontrados.");
		}
		
		//Desconto
		if( porcentagemDesconto != null || !porcentagemDesconto.isEmpty() ) {
			
			//A - Calcular a nova guia
			//A.1 - Tem item de locomoção? Se sim, retira eles.
			List<GuiaItemDt> listaGuiaItemDt_locomocao = this.extrairItensLocomocao(listaGuiaItemDt_GuiaReferencia);
			List<GuiaItemDt> listaGuiaItemDt_NAO_locomocao = this.extrairItensNaoLocomocao(listaGuiaItemDt_GuiaReferencia);
			
			//A.2 - Gera a guia descontada
			GuiaEmissaoDt guiaEmissaoDescontadaDt = this.getGuiaCalculoNe().descontarGuia(porcentagemDesconto, guiaEmissaoReferenciaDt, listaGuiaItemDt_locomocao, listaGuiaItemDt_NAO_locomocao, idUsuario);
			
			//seta IP
			if( ipComputadorLog != null && guiaEmissaoDescontadaDt != null) {
				guiaEmissaoDescontadaDt.setIpComputadorLog(ipComputadorLog);
			}
			
			//Salvar guia com desconto
			FabricaConexao obFabricaConexao = null;
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				
				obFabricaConexao.iniciarTransacao();
				
				this.salvar(guiaEmissaoDescontadaDt, guiaEmissaoDescontadaDt.getListaGuiaItemDt(), false, idUsuario, obFabricaConexao);
				
				obFabricaConexao.finalizarTransacao();
			}
			catch(Exception e) {
				if( obFabricaConexao != null ) {
					obFabricaConexao.cancelarTransacao();
				}
				throw e;
			}
			finally {
				if( obFabricaConexao != null ) {
					obFabricaConexao.fecharConexao();
				}
			}
			
			return true;
		}
		
		return false;
		
	}
	
	public boolean gerarGuiaParcelada(String idGuiaReferencia, String quantidadeParcelas, String motivoParcelamento, String idUsuario, String ipComputadorLog) throws Exception {
		
		if( this.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(idGuiaReferencia) == null ) {
			throw new MensagemException(Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA));
		}
		
		if( this.isGuiaFinal_FinalZero(idGuiaReferencia) ) {
			throw new MensagemException("Esta guia é uma guia final. De acordo com a resolução 81 de 2017(Proad 201703000029625), esta guia não pode ser mais parcelada.");
		}
		
		if( this.isValorCausaMenorValorMaximoJuizado(idGuiaReferencia) ) {
			throw new MensagemException("Guia não pode ser parcelada. Conforme novo regimento de custas resolução 81 de 2017(Proad 201703000029625), não serão parceladas as despesas concernentes aos processos em que o valor da causa seja inferior ao teto de alçada dos Juizados Especiais Cíveis.");
		}
		
		if( idGuiaReferencia == null || idGuiaReferencia.isEmpty() ) {
			throw new MensagemException("Guia de Referência não identificada.");
		}
		
		//Consultar a guia referencia
		GuiaEmissaoDt guiaEmissaoReferenciaDt = this.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(idGuiaReferencia);
		if( guiaEmissaoReferenciaDt == null ) {
			throw new MensagemException("Guia não localizada com as condições necessária. (Obs.: A guia deve estar com o status de Aguardando Pagamento e não estar vencida.)");
		}
		
		if( quantidadeParcelas != null && !quantidadeParcelas.isEmpty() ) {
			int qtdadeParc = Funcoes.StringToInt(quantidadeParcelas);
//			if( qtdadeParc < QUANTIDADE_MINIMA_PARCELAS || qtdadeParc > GuiaEmissaoNe.getQuantidadeMaximaParcelas(guiaEmissaoReferenciaDt.getId()) ) {
//				throw new MensagemException("Quantidade máxima de parcelas tem que estar entre " + QUANTIDADE_MINIMA_PARCELAS + " ou " + GuiaEmissaoNe.getQuantidadeMaximaParcelas(guiaEmissaoReferenciaDt.getId()) + ".");
//			}
			if( qtdadeParc > 5 && motivoParcelamento != null && motivoParcelamento.length() < 10 ) {
				throw new MensagemException("Por favor, por decisão judicial, preencha a justificativa do parcelamento ser acima de 5 vezes.");
			}
		}
		
		//Consulta dados do processo
		if( guiaEmissaoReferenciaDt.getId_Processo() != null && !guiaEmissaoReferenciaDt.getId_Processo().isEmpty() ) {
			ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoReferenciaDt.getId_Processo());
			
			ServentiaDt serventiaDt = null;
			ComarcaDt comarcaDt = null;
			if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
				serventiaDt = this.consultarServentiaProcesso(processoDt.getServentiaCodigo());
				if( serventiaDt != null ) {
					comarcaDt = this.consultarComarca(serventiaDt.getId_Comarca());
					processoDt.setComarca(serventiaDt.getComarca());
				}
			}
			
			if( processoDt != null ) {
				guiaEmissaoReferenciaDt.setNumeroProcesso(processoDt.getProcessoNumero());
				guiaEmissaoReferenciaDt.setId_Serventia(processoDt.getId_Serventia());
				guiaEmissaoReferenciaDt.setServentia(processoDt.getServentia());
				guiaEmissaoReferenciaDt.setComarca(processoDt.getComarca());
				guiaEmissaoReferenciaDt.setComarcaCodigo(processoDt.getComarcaCodigo());
			}
		}
		
		//Consulta GuiaModeloDt para obter o flag do grau
		if( guiaEmissaoReferenciaDt.getGuiaModeloDt() != null && guiaEmissaoReferenciaDt.getGuiaModeloDt().getId() != null ) {
			GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarId(guiaEmissaoReferenciaDt.getGuiaModeloDt().getId());
			if( guiaModeloDt != null ) {
				guiaEmissaoReferenciaDt.setGuiaModeloDt(guiaModeloDt);
			}
		}
		
		//Verifica se guia já tem desconto aplicada e se já é guia de referência
//		if( !guiaEmissaoReferenciaDt.isGuiaLivreParaUsoDescontoParcelamento() ) {
//			throw new MensagemException("Guia já utilizada para desconto ou parcelamento.");
//		}
		
		//Consultar itens da guia
		GuiaItemNe guiaItemNe = new GuiaItemNe();
		List<GuiaItemDt> listaGuiaItemDt_GuiaReferencia = guiaItemNe.consultarItensGuia(guiaEmissaoReferenciaDt.getId());
		if( listaGuiaItemDt_GuiaReferencia == null || listaGuiaItemDt_GuiaReferencia.isEmpty() ) {
			throw new MensagemException("Itens da Guia de Referência não encontrados.");
		}
		
		//Parcelamento
		if( !quantidadeParcelas.isEmpty() ) {
			
			//A - Calcular as novas guias
			//A.1 - Tem item de locomoção? Se sim, retira eles para a primeira parcela.
			List<GuiaItemDt> listaGuiaItemDt_locomocao = this.extrairItensLocomocao(listaGuiaItemDt_GuiaReferencia);
			List<GuiaItemDt> listaGuiaItemDt_NAO_locomocao = this.extrairItensNaoLocomocao(listaGuiaItemDt_GuiaReferencia);
			
			if( listaGuiaItemDt_locomocao != null && listaGuiaItemDt_NAO_locomocao == null ) {
				throw new MensagemException("Guias que possuem somente itens de locomoções ou despesas postais não podem ser parceladas.");
			}
			
			//A.2 - Gera as parcelas
			List<GuiaEmissaoDt> listaGuiaEmissaoDtParceladas = this.getGuiaCalculoNe().parcelarGuia(quantidadeParcelas, guiaEmissaoReferenciaDt, listaGuiaItemDt_locomocao, listaGuiaItemDt_NAO_locomocao, idUsuario);
			
			//Salvar
			if( listaGuiaEmissaoDtParceladas != null && !listaGuiaEmissaoDtParceladas.isEmpty() ) {
				
				FabricaConexao obFabricaConexao = null;
				
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					
					obFabricaConexao.iniciarTransacao();
					
					//Salva os dados do parcelamento acima de 6 da guia referencia
					new GuiaQuantidadeMaximaParcelasNe().salvar(idGuiaReferencia, quantidadeParcelas, motivoParcelamento, idUsuario, ipComputadorLog, obFabricaConexao);
					
					//Salvar guias do parcelamento
					for( GuiaEmissaoDt guiaEmissaoDtParcela: listaGuiaEmissaoDtParceladas ) {
						//seta IP
						if( ipComputadorLog != null ) {
							guiaEmissaoDtParcela.setIpComputadorLog(ipComputadorLog);
						}
						this.salvar(guiaEmissaoDtParcela, guiaEmissaoDtParcela.getListaGuiaItemDt(), false, idUsuario, obFabricaConexao);
					}
					
					//Ocorrencia 2021/918
					//Atualiza a guia emissao referencia com o status PARCELAMENTO_REALIZADO
					this.atualizarStatusGuiaEmitida(obFabricaConexao, idGuiaReferencia, String.valueOf(GuiaStatusDt.PARCELAMENTO_REALIZADO));
					
					obFabricaConexao.finalizarTransacao();
				}
				catch(Exception e) {
					if( obFabricaConexao != null ) {
						obFabricaConexao.cancelarTransacao();
					}
					throw e;
				}
				finally {
					if( obFabricaConexao != null ) {
						obFabricaConexao.fecharConexao();
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean alterarQuantidadeMaximaParcelas(String idGuiaReferencia, String quantidadeMaximaParcelas, String motivo, String idUsuario, String ipComputadorLog) throws Exception {
		boolean retorno = false;
		
		//Valida parametros
		if( idGuiaReferencia == null || quantidadeMaximaParcelas == null || idUsuario == null || ipComputadorLog == null ) {
			throw new MensagemException("Parâmetro(s) inválido(s). Por favor, refaça a operação.");
		}
		if( idGuiaReferencia.isEmpty() || quantidadeMaximaParcelas.isEmpty() || idUsuario.isEmpty() || ipComputadorLog.isEmpty() ) {
			throw new MensagemException("Parâmetro(s) inválido(s). Por favor, refaça a operação.");
		}
		
		//Consultar a guia referencia
		GuiaEmissaoDt guiaEmissaoReferenciaDt = this.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(idGuiaReferencia);
		if( guiaEmissaoReferenciaDt == null ) {
			throw new MensagemException("Guia não localizada com as condições necessária. (Obs.: A guia deve estar com o status de Aguardando Pagamento e não estar vencida.)");
		}
		
		//Salva a alteração
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			GuiaQuantidadeMaximaParcelasNe guiaQuantidadeMaximaParcelasNe = new GuiaQuantidadeMaximaParcelasNe();
			retorno = guiaQuantidadeMaximaParcelasNe.salvar(idGuiaReferencia, quantidadeMaximaParcelas, motivo, idUsuario, ipComputadorLog, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public boolean atualizarDataVencimentoGuia(String idGuiaReferencia, String idUsuario, String ipComputador) throws Exception {
		
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			obFabricaConexao.iniciarTransacao();
			
			GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
			guiaEmissaoDt.setId(idGuiaReferencia);
			guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia15Dias());
			
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.alterar(guiaEmissaoDt);
			
			LogDt obLogDt = new LogDt("GuiaEmissao", guiaEmissaoDt.getId(), idUsuario, ipComputador, String.valueOf(LogTipoDt.Alterar), "", "[Id_GuiaEmissao:" + guiaEmissaoDt.getId() + ";DataVencimento:" + guiaEmissaoDt.getDataVencimento() +"]");
			obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	public List<GuiaItemDt> extrairItensLocomocao(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt_locomocao = null;
		
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			
			for( GuiaItemDt guiaItemDt: listaGuiaItemDt ) {
				
				switch( Funcoes.StringToInt( guiaItemDt.getCustaDt().getId() ) ) {
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL :
					case CustaDt.LOCOMOCAO_PARA_OFICIAL :
					case CustaDt.LOCOMOCAO_PARA_AVALIACAO :
					case CustaDt.LOCOMOCAO_PARA_PENHORA :
					case CustaDt.CUSTAS_LOCOMOCAO:
					case CustaDt.OFICIAL_JUSTICA:
					case CustaDt.DESPESA_POSTAL:
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL_SEGUNDO_GRAU: {
						
						if( listaGuiaItemDt_locomocao == null ) {
							listaGuiaItemDt_locomocao = new ArrayList<GuiaItemDt>();
						}
						listaGuiaItemDt_locomocao.add(guiaItemDt);
						
						break;
					}
				}
			}
			
		}
		
		return listaGuiaItemDt_locomocao;
	}
	
	public List<GuiaItemDt> extrairItensNaoLocomocao(List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		List<GuiaItemDt> listaGuiaItemDt_NAO_locomocao = null;
		
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
			
			for( GuiaItemDt guiaItemDt: listaGuiaItemDt ) {
				
				switch( Funcoes.StringToInt( guiaItemDt.getCustaDt().getId() ) ) {
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL :
					case CustaDt.LOCOMOCAO_PARA_OFICIAL :
					case CustaDt.LOCOMOCAO_PARA_AVALIACAO :
					case CustaDt.LOCOMOCAO_PARA_PENHORA :
					case CustaDt.CUSTAS_LOCOMOCAO:
					case CustaDt.OFICIAL_JUSTICA:
					case CustaDt.DESPESA_POSTAL:
					case CustaDt.LOCOMOCAO_PARA_TRIBUNAL_SEGUNDO_GRAU: {
						break;
					}
					default: {
						
						if( listaGuiaItemDt_NAO_locomocao == null ) {
							listaGuiaItemDt_NAO_locomocao = new ArrayList<GuiaItemDt>();
						}
						listaGuiaItemDt_NAO_locomocao.add(guiaItemDt);
						
						break;
					}
				}
			}
			
		}
		
		return listaGuiaItemDt_NAO_locomocao;
	}
	
	/**
	 * Método para consultar se a serventia é de segundo grau.
	 * @param String id_serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isServentiaSegundoGrau(String id_serventia) throws Exception {
		return new ServentiaNe().isServentiaSegundoGrau(id_serventia);
	}
	
	
	/**
	 * Método para consultar GuiaModelo pelo idGuiaTipo e idProcessoTipo para o novo regimento de custas.
	 * @param String idGuiaTipo
	 * @param String idProcessoTipo
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaModeloDt consultarGuiaModeloProcessoTipoNovoRegimento(FabricaConexao obFabricaConexao, String idGuiaTipo, String idProcessoTipo) throws Exception {
		return new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(obFabricaConexao, idGuiaTipo, idProcessoTipo);
	}
	
	
	private void retirarItensCalculadosPorRegra(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		
		processoTipoNe = new ProcessoTipoNe();
		
		if( guiaEmissaoDt != null && listaGuiaItemDt != null && !listaGuiaItemDt.isEmpty() ) {
			
			if( guiaEmissaoDt.getGuiaModeloDt() != null && guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo() != null ) {
				
				switch(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()) {
					case GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU :
					case GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU : {
						
						//Ocorrência 2018/1028
						//Se guia inicial
						//e natureza é carta precatória
						//e classe carta precatória ou execução fiscal
						//e noma parte autora contêm "Fazenda Pública"
						//então retira todos os itens e cobra somente os itens de locomoções.
						if( guiaEmissaoDt.getId_NaturezaSPG() != null
							&& !guiaEmissaoDt.getId_NaturezaSPG().isEmpty()
							&& Integer.parseInt(guiaEmissaoDt.getId_NaturezaSPG()) == NaturezaSPGDt.CARTA_PRECATORIA ) {
							
							if( guiaEmissaoDt.getId_ProcessoTipo() != null
								&& !guiaEmissaoDt.getId_ProcessoTipo().isEmpty() ) {
								
								ProcessoTipoDt processoTipoDt = processoTipoNe.consultarId(guiaEmissaoDt.getId_ProcessoTipo());
								
								if( processoTipoDt != null 
									&& 
									(	
											Integer.parseInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.EXECUCAO_FISCAL
											||
											Integer.parseInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA
											||
											Integer.parseInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA_CPC
											||
											Integer.parseInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA_CPP
											||
											Integer.parseInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL
											||
											Integer.parseInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE
									)
								){
									
									if( guiaEmissaoDt.getRequerente() != null
											&&
											(
												Funcoes.retirarAcentos(guiaEmissaoDt.getRequerente()).toUpperCase().contains("FAZENDA")
												&
												Funcoes.retirarAcentos(guiaEmissaoDt.getRequerente()).toUpperCase().contains("PUBLICA")
											)
										) {
										
										
										guiaEmissaoDt.setDadosAdicionaisParaLog("Custas Zerada para ocorrência 2018/1028");
										
										this.getGuiaCalculoNe().zerarItensCalculados(listaGuiaItemDt);
										
									}
								}
								
							}
						}
						
						break;
					}
				}
				
			}
			
		}
	}
	
	/**
	 * Método para retirar itens de acordo com regras estabelecidas pela corregedoria.
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List<GuiaCustaModeloDt> listaGuiaCustaModeloDt
	 * @throws Exception
	 */
	private void retirarItensPorRegra(GuiaEmissaoDt guiaEmissaoDt, List<GuiaCustaModeloDt> listaGuiaCustaModeloDt) throws Exception {
		
		processoTipoNe = new ProcessoTipoNe();
		
		if( listaGuiaCustaModeloDt != null && !listaGuiaCustaModeloDt.isEmpty() ) {
			if( guiaEmissaoDt != null && guiaEmissaoDt.getGuiaModeloDt() != null && guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo() != null ) {
				
				switch(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()) {
				
					case GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU : {
						
						ProcessoTipoDt processoTipoDt = processoTipoNe.consultarId(guiaEmissaoDt.getGuiaModeloDt().getId_ProcessoTipo());
						
						//Se guia classe divorcio consensual, adiciona somente o item 7 e não 5,6 e 8
						if( processoTipoDt != null && processoTipoDt.getProcessoTipoCodigo() != null && Funcoes.StringToInt(processoTipoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.DIVORCIO_CONSENSUAL ) {
							for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
								GuiaCustaModeloDt guiaCustaModeloDt = listaGuiaCustaModeloDt.get(i);
								
								if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO ) {
									listaGuiaCustaModeloDt.remove(i);
									break;
								}
							}
						}
						
						break;
					}
					
					case GuiaTipoDt.ID_FINAL_ZERO : {
						
						//Ocorrência 2017/10919
						//Se guia já tem o item 1041 custa 16(id:28)
						//e
						//Possui o item 1041 custa 17(id:29) com porcentagem de 30% que foi adicionado pelo método "guiaFinalNe.verificarCobranca30PorcentoCustas(guiaEmissaoDt, listaItensGuia)"
						//Deve-se retirar este item de custa 17(id:29) deixando somente o item de custa 16(id:28)
						boolean possuiIdCusta28 = false;
						boolean possuiIdCusta29 = false;
						for(GuiaCustaModeloDt guiaCustaModeloDt: listaGuiaCustaModeloDt) {
							if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_PROCEDIMENTO_ORDINARIO ) {
								possuiIdCusta28 = true;
							}
							if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_ESPECIAL_CONTENCIOSA ) {
								possuiIdCusta29 = true;
							}
						}
						if( possuiIdCusta28 && possuiIdCusta29 ) {
							//listaGuiaCustaModeloDt.removeIf(p -> Funcoes.StringToInt(p.getCustaDt().getId()) == CustaDt.PROCESSOS_ESPECIAL_CONTENCIOSA && p.getCustaDt().getPorcentagem().equals("30.0") );
							for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
								GuiaCustaModeloDt guiaCustaModeloDt = listaGuiaCustaModeloDt.get(i);
								
								if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_ESPECIAL_CONTENCIOSA ) {
									listaGuiaCustaModeloDt.remove(i);
									break;
								}
							}
						}
						
						break;
					}
					
					case GuiaTipoDt.ID_FINAL : {
						
						//Ocorrência 2017/11951
						//Se guia já tem o item 1041 custa 16(id:28)
						//e
						//Possui o item 1041 custa 17(id:29) com porcentagem de 30% que foi adicionado pelo método "guiaFinalNe.verificarCobranca30PorcentoCustas(guiaEmissaoDt, listaItensGuia)"
						//Deve-se retirar este item de custa 16(id:28) deixando somente o item de custa 17(id:29)
						boolean possuiIdCusta28 = false;
						boolean possuiIdCusta29 = false;
						for(GuiaCustaModeloDt guiaCustaModeloDt: listaGuiaCustaModeloDt) {
							if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_PROCEDIMENTO_ORDINARIO ) {
								possuiIdCusta28 = true;
							}
							if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_ESPECIAL_CONTENCIOSA ) {
								possuiIdCusta29 = true;
							}
						}
						if( possuiIdCusta28 && possuiIdCusta29 ) {
							//listaGuiaCustaModeloDt.removeIf(p -> Funcoes.StringToInt(p.getCustaDt().getId()) == CustaDt.PROCESSOS_PROCEDIMENTO_ORDINARIO && p.getCustaDt().getPorcentagem().equals("30.0") );
							for( int i = 0; i < listaGuiaCustaModeloDt.size(); i++ ) {
								GuiaCustaModeloDt guiaCustaModeloDt = listaGuiaCustaModeloDt.get(i);
								
								if( guiaCustaModeloDt != null && guiaCustaModeloDt.getCustaDt() != null && guiaCustaModeloDt.getCustaDt().getId() != null && Funcoes.StringToInt(guiaCustaModeloDt.getCustaDt().getId()) == CustaDt.PROCESSOS_PROCEDIMENTO_ORDINARIO ) {
									listaGuiaCustaModeloDt.remove(i);
									break;
								}
							}
						}
						
						break;
					}
				}
				
			}
		}
		
	}
	
	/**
	 * Consulta guia inicial.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaInicial(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				guiaEmissaoDt = obPersistencia.consultarGuiaInicial(numeroGuiaCompleto);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Consulta guia inicial.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaInicialNaoComplementada(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				guiaEmissaoDt = obPersistencia.consultarGuiaInicialNaoComplementada(numeroGuiaCompleto);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Consulta guia pelo número da guia.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaTodosDados(String numeroGuiaCompleto) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				ProcessoNe processoNe = new ProcessoNe();
				GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
				
				//Consultar a guia
				guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
				
				if( guiaEmissaoDt != null ) {
					
					//Consultar itens
					GuiaItemNe guiaItemNe = new GuiaItemNe();
					guiaEmissaoDt.setListaGuiaItemDt(guiaItemNe.consultarItensGuia(guiaEmissaoDt.getId()));
					
					//Consultar guia no SPG
					GuiaEmissaoDt guiaEmissaoDtSPG = guiaSPGNe.consultarGuiaSPGCapitalInterior(numeroGuiaCompleto);
					
					if( guiaEmissaoDt.getId_Processo() != null && !guiaEmissaoDt.getId_Processo().isEmpty() ) {
						
						//Consultar o processo
						ProcessoDt processoDt = processoNe.consultarId(guiaEmissaoDt.getId_Processo(), obFabricaConexao);
						
						guiaEmissaoDt.setProcessoDt(processoDt);
					}
				}
				
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar o metadado da guia.
	 * @param String numeroGuiaCompleto
	 * @return String
	 * @throws Exception
	 */
	public String consultaMetadadosGuia(String numeroGuiaCompleto) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		String metadados = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());				
				metadados = obPersistencia.consultaMetadadosGuia(numeroGuiaCompleto);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}		
		return metadados;
	}
	
	/**
	 * Método para consultar os metadados de guias registradas do SPG.
	 * @param String numeroGuiaCompleto
	 * @return String
	 * @throws Exception
	 */
	public String consultaMetadadosGuiaSPG(String numeroGuiaCompleto) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		String metadados = null;
		
		try {
			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());				
				metadados = obPersistencia.consultaMetadadosGuia(numeroGuiaCompleto);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}		
		return metadados;
	}
	
	public CertidaoGuiaDt consultarCertidao(String numeroGuia) throws Exception{
		
		CertidaoGuiaDt certidaoGuiaDt = new CertidaoGuiaNe().consultarCertidaoProjudi(numeroGuia);
		
		return certidaoGuiaDt;
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSPG(String numeroCompletoGuia) throws Exception {
		return new GuiaSPGNe().consultarGuiaEmissaoSPG(numeroCompletoGuia);
	}
	
	public GuiaEmissaoDt consultarGuiaEmissaoSSG(String numeroCompletoGuia) throws Exception {
		return new GuiaSSGNe().consultarGuiaEmissaoSSG(numeroCompletoGuia);
	}
	
	/**
	 * Método para consultar lista de info_repasse pelo isn da guia.
	 * @param String isnGuia
	 * @param String comarcaCodigo
	 * @return List<InfoRepasseSPGDt> listaInfoRepasseSPGDt
	 * @throws Exception
	 */
	public List<InfoRepasseSPGDt> consultarListaInfoRepasseByISNGuia(String isnGuia, String comarcaCodigo) throws Exception {
		return new GuiaSPGNe().consultarListaInfoRepasseByISNGuia(isnGuia, comarcaCodigo);
	}
	
	/**
	 * Método para consultar guia no spg pelo número da guia completa. O método irá consultar o número de 
	 * guia tanto no ambiente capital como no interior.
	 * @param String numeroGuiaCompleto
	 * @return GuiaEmissaoDt
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaSPGCapitalInterior(String numeroGuiaCompleto) throws Exception {
		return new GuiaSPGNe().consultarGuiaSPGCapitalInterior(numeroGuiaCompleto);
	}
	
	/**
	 * Método para verificar se a guia é do tipo esperado.
	 * 
	 * @param List<String> listaIdGuiaTipo
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaTipoDoTipoEsperado(List<String> listaIdGuiaTipo, String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		
		if( numeroGuiaCompleto != null 
			&& !numeroGuiaCompleto.isEmpty()
			&& listaIdGuiaTipo != null
			&& !listaIdGuiaTipo.isEmpty() 
			&& this.isNumeroGuiaProjudi(numeroGuiaCompleto) ) {
			
			GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
			if( guiaEmissaoDt != null ) {
				for( String idGuiaTipo: listaIdGuiaTipo ) {
					
					if( idGuiaTipo != null 
						&& guiaEmissaoDt.getGuiaModeloDt() != null 
						&& guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo() != null 
						&& guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(idGuiaTipo) ) {
						
						retorno = true;
						break;
					}
					
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se serventia do processo é diferente da serventia informada na guia.
	 * 
	 * @param ProcessoDt processoDt
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isCodigoServentiaProcessoDiferenteCodigoServentiaGuia(ProcessoDt processoDt, String numeroGuiaCompleto) throws Exception {
		boolean retorno = false;
		
		GuiaEmissaoDt guiaEmissaoDt = null;
		if( numeroGuiaCompleto != null ) {
			guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
			
			if( guiaEmissaoDt == null ) {
				GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
				
				guiaEmissaoDt = guiaSPGNe.consultarGuiaSPGCapitalInterior(numeroGuiaCompleto);
			}
		}
		
		if( processoDt != null 
			&& processoDt.getId() != null
			&& processoDt.getId_Serventia() != null
			&& guiaEmissaoDt != null 
			&& guiaEmissaoDt.getId_Serventia() != null) {
			
			ServentiaNe serventiaNe = new ServentiaNe();
			
			ServentiaDt serventiaDt_Processo = serventiaNe.consultarId(processoDt.getId_Serventia());
			ServentiaDt serventiaDt_Guia = serventiaNe.consultarId(guiaEmissaoDt.getId_Serventia());
			
			if( serventiaDt_Processo != null && serventiaDt_Guia != null ) {
				
				if( serventiaDt_Processo.getServentiaCodigoExterno() != null 
					&& serventiaDt_Guia.getServentiaCodigoExterno() != null 
					&& !serventiaDt_Guia.getServentiaCodigoExterno().isEmpty()
					&& !serventiaDt_Processo.getServentiaCodigoExterno().equals(serventiaDt_Guia.getServentiaCodigoExterno())) {
					
					retorno = true;
				}
			}
			
			if( guiaEmissaoDt.isGuiaEmitidaSPG() ) {
				
				if( serventiaDt_Processo != null
					&& serventiaDt_Processo.getServentiaCodigoExterno() != null
					&& guiaEmissaoDt.getInfoLocalCertidaoSPG() != null ) {
					
					ComarcaNe comarcaNe = new ComarcaNe();
					
					String comarcaCodigoProcesso = serventiaDt_Processo.getComarcaCodigo();
					String comarcaCodigoGuia = comarcaNe.extrairComarcaCodigoInfoLocalCertidaoSPG(guiaEmissaoDt.getInfoLocalCertidaoSPG());
					
					if( comarcaCodigoProcesso != null
						&& comarcaCodigoGuia != null
						&& !comarcaCodigoProcesso.equals(comarcaCodigoGuia) ) {
						
						retorno = true;
					}
				}
				
			}
			
			if( processoDt.getValor() != null 
				&& guiaEmissaoDt.getValorAcao() != null 
				&& !processoDt.getValor().equals(guiaEmissaoDt.getValorAcao()) ) {
				
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	public boolean isValorCausaMenorValorMaximoJuizado(String idGuiaEmissao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idGuiaEmissao != null ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				ProcessoNe processoNe = new ProcessoNe();
				
				GuiaEmissaoDt guiaEmissaoDt = this.consultarId(idGuiaEmissao, obFabricaConexao);
				if( guiaEmissaoDt != null && guiaEmissaoDt.getId_Processo() != null ) {
					ProcessoDt processoDt = processoNe.consultarId(guiaEmissaoDt.getId_Processo(), obFabricaConexao);
					
					if( processoDt != null 
						&& processoDt.getId() != null
						&& !processoDt.getId().isEmpty() 
						&& processoNe.isProcessoJuizadosTurmas(processoDt.getId(), obFabricaConexao) ) {
						
						if( processoDt.getValorCondenacao() != null && !processoDt.getValorCondenacao().isEmpty() ) {
							retorno = new GuiaCalculoNe().isValorCausaMenorValorMaximoJuizado(processoDt.getValorCondenacao());
						}
						else {
							if( processoDt.getValor() != null && !processoDt.getValor().isEmpty() ) {
								retorno = new GuiaCalculoNe().isValorCausaMenorValorMaximoJuizado(processoDt.getValor());
							}
						}
					}
				}
				
				
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar guias do processo que são parcelas e que está com o status de aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaParceladaProcessoAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				listaGuiaEmissaoDt = guiaEmissaoPs.consultarGuiaParceladaProcessoAguardandoPagamento(idProcesso);
				if( listaGuiaEmissaoDt != null && !listaGuiaEmissaoDt.isEmpty() ) {
					for(GuiaEmissaoDt guiaEmissaoDt: listaGuiaEmissaoDt) {
						if( guiaEmissaoDt != null 
								&& guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento() != null 
								&& !guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento().isEmpty() ) {
							
							guiaEmissaoDt.setGuiaEmissaoDtReferencia(this.consultarId(guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento(), obFabricaConexao));
							
						}
					}
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método para consultar guias do processo que são GRS Genérica que está com o status de aguardando pagamento.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 */
	public List<GuiaEmissaoDt> consultarGuiaGRSGenericaProcessoAguardandoPagamento(String idProcesso) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( idProcesso != null && !idProcesso.isEmpty() ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs guiaEmissaoPs = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				listaGuiaEmissaoDt = guiaEmissaoPs.consultarGuiaGRSGenericaProcessoAguardandoPagamento(idProcesso);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public List<GuiaEmissaoDt> consultarGuiasServicosAguardandoPagamentoProcesso(String idProcesso) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiasServicosAguardandoPagamentoProcesso(idProcesso);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public List<GuiaEmissaoDt> consultarGuiasComplementaresAguardandoPagamentoProcesso(String idProcesso) throws Exception {
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			listaGuiaEmissaoDt = obPersistencia.consultarGuiasComplementaresAguardandoPagamentoProcesso(idProcesso);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return listaGuiaEmissaoDt;
	}
	
	public static String getTextoFinalidade(int codigo) {
		if (codigo == LOCOMOCAO) return "Locomoção";
		if (codigo == PENHORA_AVALIACAO_ALIENACAO) return "Penhora, avaliação e alienação";
		if (codigo == CITACAO_PENHORA_AVALIACAO_E_ALIENACAO) return "Citação, penhora, avaliação e alienação";
		if (codigo == CITACAO_PENHORA_E_PRACA_LEILAO) return "Citação, penhora e praça/leilão";
		if (codigo == CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO) return "Citação, penhora, avaliação e praça/leilão";
		if (codigo == LOCOMOCAO_AVALIACAO) return "Locomoção para avaliação";
		if (codigo == LOCOMOCAO_AVALIACAO_PRACA) return "Locomoção para avaliação e Praça";
		return "";
	}
	
	
	/**
	 * Método para consultar as guias inciais aguardando deferimento.
	 * 
	 * @param String idProcesso
	 * @param FabricaConexao fabConexao
	 * @return GuiaEmissaoDt guia inicial do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialAguardandoDeferimento(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		try {
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicialAguardandoDeferimento(idProcesso);
		} finally{
			
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais aguardando deferimento.
	 * 
	 * @param String idProcesso
	 * @return GuiaEmissaoDt guia inicial do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialAguardandoDeferimento(String idProcesso) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicialAguardandoDeferimento(idProcesso);
		
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Método para consultar as guias inciais baixada com isenção.
	 * 
	 * @param String idProcesso
	 * @return GuiaEmissaoDt guia inicial do processo
	 * @throws Exception
	 */
	public GuiaEmissaoDt consultarGuiaEmissaoInicialBaixadaIsencao(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		try {
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicialBaixadaIsencao(idProcesso);
		} finally{
			
		}
		
		return guiaEmissaoDt;
	}
	
	/**
	 * Atualizar o status, Aguardando Deferimento, da guia emitida 
	 * @param String idGuiaEmissao
	 * @param String idGuiaStatus
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void atualizarStatusGuiaInicialAguardandoDeferimento(String idGuiaEmissao, String idGuiaStatus, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		obPersistencia.atualizarStatusGuiaInicialAguardandoDeferimento(idGuiaEmissao, idGuiaStatus);
	}
	
	
	/**
	 * Atualizar o status, Aguardando Deferimento, da guia emitida 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param String idGuiaStatus
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void atualizarStatusGuiaInicialAguardandoDeferimento(GuiaEmissaoDt guiaEmissaoDt, String idGuiaStatus, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs obPersistencia = new  GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		obPersistencia.atualizarStatusGuiaInicialAguardandoDeferimento(guiaEmissaoDt.getId(), idGuiaStatus);
		LogDt obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),
				"[Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_GuiaStatus:" + guiaEmissaoDt.getId_GuiaStatus() +"]", 
				"[Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_GuiaStatus:" + idGuiaStatus +";Id_Usuario:" + guiaEmissaoDt.getId_Usuario()+"]");
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * Consultar boleto.
	 * 
	 * @param idGuiaEmissao
	 * @return
	 * @throws Exception
	 */
	public BoletoDt BuscarBoletoPorId(String idGuiaEmissao) throws Exception {
		BoletoDt boletoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			boletoDt = obPersistencia.buscaBoletoPorId(idGuiaEmissao);
			if (boletoDt != null && boletoDt.getId_Processo() != null && boletoDt.getId_Processo().trim().length() > 0) {
				boletoDt.setNumeroCompletoProcesso(this.consultarNumeroCompletoDoProcesso(boletoDt.getId_Processo(), obFabricaConexao));
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return boletoDt;
	}
	
	public void metodoParaGerarModelosGuiasSemItensEspecificos() throws Exception {
		List<String> listaIdGuiaTipo = new ArrayList<String>();
		listaIdGuiaTipo.add(GuiaTipoDt.ID_LOCOMOCAO);
		listaIdGuiaTipo.add(GuiaTipoDt.ID_SERVICOS);
		listaIdGuiaTipo.add(GuiaTipoDt.ID_POSTAGEM);
		listaIdGuiaTipo.add(GuiaTipoDt.ID_MEDIACAO);
		listaIdGuiaTipo.add(GuiaTipoDt.ID_CONCILIACAO);
		
		for(String idGuiaTipo: listaIdGuiaTipo) {
			List<ProcessoTipoDt> listaProcTipoDt = new ArrayList<ProcessoTipoDt>();
		}
	}
	
	/**
	 * Método para desvincular guia de processo no PJD e SPG.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param String idUsuario
	 * @param String ipComputador
	 * @param String motivoDesvinculacao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean desvincularGuiaProcessoPJD_SPG(String numeroGuiaCompleto, String idUsuario, String ipComputador, String motivoDesvinculacao) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			if( numeroGuiaCompleto != null && numeroGuiaCompleto.length() > 0 ) {

				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				
				GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto, obFabricaConexao);
				
				if( guiaEmissaoDt != null ) {
					
					GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
					
					//consulta guia spg
					GuiaEmissaoDt guiaEmissaoDtSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoCapital(numeroGuiaCompleto);
					if( guiaEmissaoDtSPG != null ) {
						guiaEmissaoDtSPG.setComarcaCodigo(ComarcaDt.GOIANIA);
					}
					
					if( guiaEmissaoDtSPG == null ) {
						guiaEmissaoDtSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoInterior(numeroGuiaCompleto);
						if( guiaEmissaoDtSPG != null ) {
							guiaEmissaoDtSPG.setComarcaCodigo(ComarcaDt.APARECIDA_DE_GOIANIA);
						}
					}
					
					if( guiaEmissaoDtSPG != null && guiaEmissaoDtSPG.getNumeroGuiaCompleto() != null ) {
						List<InfoRepasseSPGDt> listaRepasse = guiaSPGNe.consultarListaInfoRepasseByISNGuia(guiaEmissaoDtSPG.getId(), guiaEmissaoDtSPG.getComarcaCodigo());
						if( listaRepasse != null ) {
							for( InfoRepasseSPGDt repasse: listaRepasse ) {
								if( repasse.getDataRepasse() != null && !repasse.getDataRepasse().isEmpty() ) {
									return false;
								}
							}
						}
					}
					
					//Desvincular no PJD
					this.desvincularGuiaProcesso(obFabricaConexao, guiaEmissaoDt.getId(), idUsuario, ipComputador, motivoDesvinculacao);
					
					//Desvincular no SPG
					guiaSPGNe.limparNumerosProcessosGuiaSPG(guiaEmissaoDt.getNumeroGuiaCompleto(), false, obFabricaConexao, LogTipoDt.Alterar, idUsuario, ipComputador);
					
					retorno = true;
					
				}
				
				obFabricaConexao.finalizarTransacao();
			}
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para desvincular guia de processo no PJD.
	 * @param FabricaConexao obFabricaConexao
	 * @param String idGuiaEmissao
	 * @param String idUsuario
	 * @param String ipComputador
	 * @param String motivoDesvinculacao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean desvincularGuiaProcesso(FabricaConexao obFabricaConexao, String idGuiaEmissao, String idUsuario, String ipComputador, String motivoDesvinculacao) throws Exception {
		boolean retorno = false;
		
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		if( idGuiaEmissao != null && idGuiaEmissao.length() > 0 ) {
			retorno = obPersistencia.desvincularGuiaProcesso(idGuiaEmissao);
			
			if( retorno ) {
				LogDt obLogDt = new LogDt("GuiaEmissao", idGuiaEmissao, idUsuario, ipComputador, String.valueOf(LogTipoDt.Alterar),"", "[Guia desvinculada do processo;Id_GuiaEmissao:"+ idGuiaEmissao +";Motivo:"+motivoDesvinculacao+"]");
				obLog.salvar(obLogDt, obFabricaConexao);
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se o número do processo completo informado é o mesmo que o id informado.
	 * @param String numeroProcessoCompletoInformado
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isNumeroProcessoInformadoIgualProcesso(String numeroProcessoCompletoInformado, String idProcesso) throws Exception {
		boolean retorno = false;
		
		if( numeroProcessoCompletoInformado != null && idProcesso != null ) {
			ProcessoNe processoNe = new ProcessoNe();
			
			ProcessoDt processoDt = processoNe.consultarProcessoNumeroCompleto(numeroProcessoCompletoInformado, null);
			
			if( processoDt != null && processoDt.getId() != null ) {
				if( processoDt.getId().equals(idProcesso) ) {
					retorno = true;
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método administrativo para consultar guia no PJD e cadastrar no SPG.
	 * 
	 * @param String numeroGuiaCompleto
	 * @throws Exception
	 */
	public void consultarGuiaProjudiCadastrarSPG_NAO_UTILIZAR(String numeroGuiaCompleto, String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
				GuiaEmissaoDt guiaEmissaoDt = this.consultarGuiaEmissaoNumeroGuia(numeroGuiaCompleto);
				if( guiaEmissaoDt != null && guiaEmissaoDt.getId() != null ) {
					
					guiaEmissaoDt.getGuiaModeloDt().setFlagGrau(GuiaTipoDt.PRIMEIRO_GRAU.toString());
					//***
					if( idProcesso != null ) {
						guiaEmissaoDt.setId_Processo(idProcesso);
					}
					guiaEmissaoDt.getGuiaModeloDt().setId_GuiaTipo(GuiaTipoDt.PRIMEIRO_GRAU.toString());
					//guiaEmissaoDt.getGuiaModeloDt().setId("20327");
					//***
					
					List<GuiaItemDt> listaGuiaItemDt = new GuiaItemNe().consultarItensGuia(guiaEmissaoDt.getId());
					
					if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
						guiaEmissaoDt.setListaGuiaItemDt(listaGuiaItemDt);
						this.enviaGuiaParaCadastroNoSPGSSG(guiaEmissaoDt, listaGuiaItemDt, obFabricaConexao);
					}
					
				}
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
	}
	
	/**
	 * Mï¿½todo para consultar as outras partes do processo.
	 * @param String id_processo
	 * @return List
	 * @throws Exception
	 */
	public List consultarOutrasPartes(String id_processo) throws Exception {
		List retorno = null;

		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		
		retorno = processoParteNe.consultarOutrasPartes(id_processo);
		
		return retorno;
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
		GuiaEmissaoDt guiaEmissaoDt = null;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			//Fred *************
			guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoInicialAguardandoPagamento(idProcesso,idUsuario);
		} finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return guiaEmissaoDt;
	}
	
	
	/**
	 * Método para Alterar o Status da Guia Inicial de Aguardando Deferimento para Aguardando Pagamento
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param ProcessoDt processoDt
	 * @param  UsuarioDt usuarioDt
	 * @throws Exception
	 */
	public void alterarStatusGuiaAguardandoDeferimento(GuiaEmissaoDt guiaEmissaoDt, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		
			FabricaConexao obFabricaConexao = null;
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				
				LogDt logDt = new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog());
				
				if (Funcoes.StringToInt(guiaEmissaoDt.getId_GuiaStatus()) == GuiaStatusDt.AGUARDANDO_DEFERIMENTO
					||
					Funcoes.StringToInt(guiaEmissaoDt.getId_GuiaStatus()) == GuiaStatusDt.BAIXADA_COM_ASSISTENCIA){
					
					//Atualiza o Status da Guia para Aguardando Pagamento
					this.atualizarStatusGuiaInicialAguardandoDeferimento(guiaEmissaoDt, String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO), obFabricaConexao);
					
					//Altera a Custa do Processo para com Custa
					ProcessoNe processoNe = new ProcessoNe();
					processoNe.alterarCustaTipoProcesso(processoDt.getId_Processo(), processoDt.getId_Custa_Tipo(), String.valueOf(ProcessoDt.COM_CUSTAS), logDt, obFabricaConexao);
					
					//Gera a Pendencia para verificar Guia Pendente
					PendenciaNe pendenciaNe = new PendenciaNe();
					if (!this.consultarGuiaPagaBancos(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao)) {
						pendenciaNe.gerarPendenciaVerificarGuiaPendente(processoDt, UsuarioServentiaDt.SistemaProjudi, processoDt.getId_Serventia(), "", null, logDt, obFabricaConexao);
					}
					
					//Descartar Pendencias do Tipo (Verificar Novo Processo com Pedido de Assistência)
					List pendListVerificar = pendenciaNe.consultarPendenciasProcessoRelacionadasPedidoAssistencia(processoDt.getId(), PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO_PEDIDO_ASSISTENCIA, obFabricaConexao);
					if (pendListVerificar != null && pendListVerificar.size()>0){
						for (Iterator iterator = pendListVerificar.iterator(); iterator.hasNext();) {
							PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
							pendenciaNe.descartarPendencia(pendenciaDt, usuarioDt, obFabricaConexao);							
						}
					}
					
					//Alterar Pendencias do Tipo (Concluso com Pedido de Benefício de Assistência) Para  (Conclusao Generica)
					List pendListConclusao = pendenciaNe.consultarPendenciasProcessoRelacionadasPedidoAssistencia(processoDt.getId(), PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA, obFabricaConexao);
					if (pendListConclusao != null && pendListConclusao.size()>0){
						for (Iterator iterator = pendListConclusao.iterator(); iterator.hasNext();) {
							PendenciaDt pendenciaDt = (PendenciaDt) iterator.next();
							pendenciaNe.alterarTipoConclusaoPedidoAssistenciaParaGenericaOuRelator(pendenciaDt, usuarioDt, obFabricaConexao);							
						}
					}
					
				} else {
					throw new MensagemException("Não foi possível localizar uma Guia Aguardando deferimento ou Baixa com Assistência!");
				}
				
				obFabricaConexao.finalizarTransacao();
			
			} catch (Exception e) {
	            obFabricaConexao.cancelarTransacao();
	            throw e;	
			} finally {
				if( obFabricaConexao != null ) {
					obFabricaConexao.fecharConexao();
				}
			}
	}
	
	/**
	 * Método responsável em verificar se o processo possui alguma guia enviada para o Cadin
	 * @param idProcesso
	 * @return
	 */
	public boolean isProcessoPossuiGuiaEnviadaCadin(String idProcesso) throws Exception {
		return (new ProcessoParteDebitoCadinNe()).isProcessoPossuiGuiaEnviadaCadin(idProcesso);
	}
	
	public void atualizeDadosAnteriores(GuiaEmissaoDt obDados) {
		super.obDados = obDados;
	}
	
	public void salvar(GuiaEmissaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		
		if (dados.getId().length()==0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("GuiaEmissao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("GuiaEmissao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public void adicionarItemCautelarContenciosoParaGuiaInicial(String GRAU_ESCOLHIDO, List listaItensGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		processoTipoNe = new ProcessoTipoNe();
		
		if( guiaEmissaoDt.getProcessoTipoCodigo() != null 
			&& (
					processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
					||
					processoTipoNe.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
					||
					guiaEmissaoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.REQUERIMENTO_APREENSAO_VEICULO))
				)
			) {
			if( GRAU_ESCOLHIDO != null && GRAU_ESCOLHIDO.equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
				listaItensGuia.add( this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS) );
			}
		}
		else {
			if( GRAU_ESCOLHIDO != null && GRAU_ESCOLHIDO.equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
				if( guiaEmissaoDt.getProcessoTipoCodigo() != null && !guiaEmissaoDt.getProcessoTipoCodigo().isEmpty() ) {
					String tipoAtoEscrivaoCivel = this.verificarAtoEscrivaoCivel(guiaEmissaoDt.getProcessoTipoCodigo());
					
					switch(tipoAtoEscrivaoCivel) {
						case GuiaEmissaoNe.PROCESSOS_CAUTELARES : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_CAUTELARES_SERAO_COBRADOS_40_DAS_CUSTAS) );
							break;
						}
						case GuiaEmissaoNe.ATO_ESCRIVAO_CONTENCIOSO_70_PORCENTO : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA) );
							break;
						}
						case GuiaEmissaoNe.DEMAIS_PROCESSOS : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
							break;
						}
						case GuiaEmissaoNe.EXECUCAO_FISCAL : {
							//Ocorrência 2018/13150
							//Não faz nada
							break;
						}
						case GuiaEmissaoNe.CARTA_ORDEM : {
							listaItensGuia.add( this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS) );
							break;
						}
					}
				}
				else {
					listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
				}
			}
		}
		
	}
	
	public void adicionarItemCautelarContenciosoParaGuiaComplementar(String GRAU_ESCOLHIDO, List listaItensGuia, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		
		processoTipoNe = new ProcessoTipoNe();
		
		if( guiaEmissaoDt.getProcessoTipoCodigo() != null 
			&& (
					processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
					||
					processoTipoNe.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
					||
					guiaEmissaoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.REQUERIMENTO_APREENSAO_VEICULO))
				)
			) {
			if( GRAU_ESCOLHIDO != null && GRAU_ESCOLHIDO.equals(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU) ) {
				listaItensGuia.add( this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS) );
			}
		}
		else {
			if( GRAU_ESCOLHIDO != null && GRAU_ESCOLHIDO.equals(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU) ) {
				if( guiaEmissaoDt.getProcessoTipoCodigo() != null && !guiaEmissaoDt.getProcessoTipoCodigo().isEmpty() ) {
					String tipoAtoEscrivaoCivel = this.verificarAtoEscrivaoCivel(guiaEmissaoDt.getProcessoTipoCodigo());
					
					switch(tipoAtoEscrivaoCivel) {
						case GuiaEmissaoNe.PROCESSOS_CAUTELARES : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_CAUTELARES_SERAO_COBRADOS_40_DAS_CUSTAS) );
							break;
						}
						case GuiaEmissaoNe.ATO_ESCRIVAO_CONTENCIOSO_70_PORCENTO : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA) );
							break;
						}
						case GuiaEmissaoNe.DEMAIS_PROCESSOS : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
							break;
						}
						case GuiaEmissaoNe.EXECUCAO_FISCAL : {
							listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA) );
							break;
						}
						case GuiaEmissaoNe.CARTA_ORDEM : {
							listaItensGuia.add( this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS) );
							break;
						}
					}
				}
				else {
					listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
				}
			}
		}
		
	}
	
	/**
	 * ----------------IMPORTANTE-------------------
	 * Método utilizado pelas guias finais e outras.
	 * ---------------------------------------------
	 * Histórico alterações:
	 * Ocorrencia 2020/10728
		Após conversa com o contador Marcelo Tiago ele me explicou que a guia inicial tem que 
		adicionar o item 7, já a guia final e final zero tem que ser o 5(100%)
		Pois o contador pode escolher no menu a opção de 30% somente para complementar 
		o item 7(30%).
	 * 
	 * 
	 * @param List<GuiaCustaModeloDt> listaItensGuia
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param ProcessoDt processoDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public void adicionarItemCautelarContencioso(List<GuiaCustaModeloDt> listaItensGuia, GuiaEmissaoDt guiaEmissaoDt, ProcessoDt processoDt) throws Exception {
		
		//Contato Marcelo informando que o item 1041 estava adicionando em processos criminais.
		if( processoDt != null && processoDt.isCriminal() ) {
			return;
		}
		
		processoTipoNe = new ProcessoTipoNe();
		
		if( guiaEmissaoDt.getProcessoTipoCodigo() != null 
			&& (
					processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
					||
					processoTipoNe.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
					||
					guiaEmissaoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.REQUERIMENTO_APREENSAO_VEICULO))
				)
			) {
			listaItensGuia.add( this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS) );
		}
		else {
			//Verifica se é cautelar, contencioso
			if( guiaEmissaoDt.getProcessoTipoCodigo() != null && !guiaEmissaoDt.getProcessoTipoCodigo().isEmpty() ) {
				String tipoAtoEscrivaoCivel = this.verificarAtoEscrivaoCivel(guiaEmissaoDt.getProcessoTipoCodigo());
				
				switch(tipoAtoEscrivaoCivel) {
					case GuiaEmissaoNe.PROCESSOS_CAUTELARES : {
						listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_CAUTELARES_SERAO_COBRADOS_40_DAS_CUSTAS) );
						break;
					}
					case GuiaEmissaoNe.ATO_ESCRIVAO_CONTENCIOSO_70_PORCENTO : {
						listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA) );
						break;
					}
					case GuiaEmissaoNe.DEMAIS_PROCESSOS : {
						listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
						break;
					}
					case GuiaEmissaoNe.EXECUCAO_FISCAL : {
						
						//Antigo!
						//listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA) );
						
						//Ocorrencia 2020/10728
						//Após conversa com o contador Marcelo Tiago ele me explicou que a guia inicial tem que 
						//adicionar o item 7, já a guia final e final zero tem que ser o 5(100%)
						//Pois o contador pode escolher no menu a opção de 30% somente para complementar 
						//o item 7(30%).
						listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
						
						break;
					}
					case GuiaEmissaoNe.CARTA_ORDEM : {
						listaItensGuia.add( this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS) );
						break;
					}
				}
			}
			else {
				listaItensGuia.add( this.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
			}
		}
		
	}
	
	public String validaPorcentagemEscolhidaRedistribuicao(RedistribuicaoDt redistribuicaoDt) throws Exception {
		String mensagemValidacao = "";
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			if( redistribuicaoDt != null && redistribuicaoDt.getListaProcessos() != null && !redistribuicaoDt.getListaProcessos().isEmpty() ) {
				for (int i = 0; i < redistribuicaoDt.getListaProcessos().size(); i++) {
					ProcessoDt processoDt = (ProcessoDt) redistribuicaoDt.getListaProcessos().get(i);
					
					if( processoDt != null && processoDt.getId() != null ) {
						
						//Extrai a porcentagem escolhida
						String porcentagemProcesso = redistribuicaoDt.getListaIdProcessoPorcentagemRepasse().get(processoDt.getId()).toString();
						
						//Consulta o ultimo repasse
						List listaGuiaEmissaoDt = this.consultarGuiaEmissaoInicial_ComplementarQualquerStatus(obFabricaConexao, processoDt.getId());
						if( listaGuiaEmissaoDt != null && listaGuiaEmissaoDt.size() > 0 ) {
							for( int m = 0; m < listaGuiaEmissaoDt.size(); m++ ) {
								GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiaEmissaoDt.get(m);
								
								GuiaEmissaoDt guiaSPG = null;
								GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
								
								//Consulta no Capital
								guiaSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoCapital(guiaEmissaoDt.getNumeroGuiaCompleto());
								
								//Consulta no Interior
								if( guiaSPG == null ) {
									guiaSPG = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoInterior(guiaEmissaoDt.getNumeroGuiaCompleto());
									if( guiaSPG != null ) {
										guiaSPG.setComarcaCodigo(ComarcaDt.APARECIDA_DE_GOIANIA);
									}
								} else {
									guiaSPG.setComarcaCodigo(ComarcaDt.GOIANIA);
								}
								
								//*******************************
								//Verifica se é menor, se sim, registra mensagem do processo
								//*******************************
								if( guiaSPG != null ) {
									String ultimoRepasseGuiaSPG = guiaSPGNe.consultarUltimaPorcentagemRepassadaInfoRepasse(guiaSPG);
									if( ultimoRepasseGuiaSPG != null && !ultimoRepasseGuiaSPG.isEmpty() ) {
										Long ultimoRepasseGuiaValorNumerico = Funcoes.StringToLong(ultimoRepasseGuiaSPG);
										Long repasseRecebidoParaCadastroValorNumerico = Funcoes.StringToLong(porcentagemProcesso);
										if( ultimoRepasseGuiaValorNumerico < repasseRecebidoParaCadastroValorNumerico ) {
											
											mensagemValidacao += "<br /><br />Para a redistribuição do processo " + processoDt.getProcessoNumeroCompleto() + " foi informado a porcentagem " + porcentagemProcesso + "% mas o último repasse identificado para a guia " + guiaEmissaoDt.getNumeroGuiaCompleto() + " foi de " + ultimoRepasseGuiaSPG + "%.";
											
										}
									}
								}
								//*******************************
							}
						}
					}
				}
			}
			
			if( !mensagemValidacao.isEmpty() ) {
				mensagemValidacao = "Atenção:" + mensagemValidacao;
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return mensagemValidacao;
	}
	
	/**
	 * Ocorrência 2019/6291
	 * Gera a guia inicial para a turma recursal com a classe de mandado de segurança.
	 * 
	 * Ocorrencia 2021/2039
	 Mudei para pesquisar somente "Mandado de Segurança Cível" e criei os modelos para as guias necessárias de primeiro grau. PS.: quando for turma, é gerado os itens pelo método.
	 */
	public List gerarGuiaInicialMandadoSegurancaTurmaRecursal(List listaItensGuiaModelo, GuiaModeloDt guiaModeloDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		if( guiaModeloDt != null && guiaEmissaoDt != null && guiaEmissaoDt.getId_ProcessoTipo() != null ) {
			//adicionar itens
			if( listaItensGuiaModelo == null ) {
				listaItensGuiaModelo = new ArrayList<GuiaCustaModeloDt>();
			}
			
			//Nao consulta no modelo e add os itens de taxa judiciária e mandado de segurança
			listaItensGuiaModelo.add(this.adicionarItem(CustaDt.TAXA_JUDICIARIA_PROCESSO));
			listaItensGuiaModelo.add(this.adicionarItem(CustaDt.MANDADO_DE_SEGURANCA_EM_TURMA_RECURSAL));
			
			//consulta guia modelo
			guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
			
			if( guiaModeloDt != null ) {
				guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
			}
			
			return listaItensGuiaModelo;
		}
		
		return null;
	}
	
	public void vinculeGuia(UsuarioDt usuarioDt, String numeroCompletoGuia, ProcessoDt processoDt, String motivoVinculacao) throws Exception {
		
		if (numeroCompletoGuia == null || numeroCompletoGuia.trim().length() == 0) {
			throw new MensagemException("Informe o número da Guia!");
		}
		
		if (processoDt == null || processoDt.getId() == null || processoDt.getId().trim().length() == 0) {
			throw new MensagemException("Não foi possível obter o número do processo.");
		}
		
		String numeroGuia = numeroCompletoGuia.replaceAll("[/.-]", "").trim();
		
		if( numeroGuia == null ) {
			throw new MensagemException("Erro ao formatar o número da Guia!");
		}
		
		//Verifica o tipo de vinculação:
		int tipoVinculacao = verificaTipoGuiaParaVinculação(numeroGuia);
		
		//Vincula Guia:
		switch(tipoVinculacao) {
			//O tipo 7 não tem problema em ficar junto com o tipo 2 e 6, pois se trata de guia do spg de certidão 
			//narrativa e aqui neste ponto, é somente atualizado o número do processo projudi no SPG.
			//Qualquer coisa depois no futuro, é só separa o case 7 e criar o seu método específico.
			case 2:
			case 6 :
			case 7 : {
				
				vinculeGuiaSerie_02_06(numeroGuia, processoDt, usuarioDt);
				
				break;
			}
			case 3 : {
				
				vinculeGuiaLocomocaoSPG(usuarioDt, numeroGuia, processoDt);
				
				break;
			}
			case 1 :
			default: {
				
				vinculeGuiaInicial(usuarioDt, numeroGuia, processoDt, motivoVinculacao);
				
				break;
			}
		}
			
	}
	
	/**
	 * Método para ser utilizado somente na vinculação.
	 * @param numeroGuia
	 * 
	 * @return
	 * 1 - Inicial
	 * 2 - Série 02
	 * 3 - Locomoção SPG
	 * 6 - Série 06
	 * 7 - Guia certidão narrativa
	 * 
	 * 
	 * @throws Exception
	 */
	private int verificaTipoGuiaParaVinculação(String numeroGuia) throws Exception {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		int tipoVinculacaoDefault = 1;
		
		if( numeroGuia != null && numeroGuia.length() > 3 ) {
			//Série 2?
			if( Funcoes.isNumeroGuiaSerie02(numeroGuia) ) {
				return 2;
			}
			
			//Série 6?
			if( Funcoes.isNumeroGuiaSerie06(numeroGuia) ) {
				return 6;
			}
			
			//Locomoção SPG?
			if( Funcoes.isNumeroGuiaSerie09(numeroGuia) ) {
				GuiaEmissaoDt guiaEmissaoSPGDt = guiaSPGNe.consultarGuiaEmissaoSPG(numeroGuia);
				if( guiaEmissaoSPGDt != null && guiaEmissaoSPGDt.isGuiaSomenteItensLocomocao() ) {
					return 3;
				}
				
				//Desarquivamento tipo_guia = 4
				if( guiaEmissaoSPGDt != null && guiaEmissaoSPGDt.getId_GuiaTipo() != null && guiaEmissaoSPGDt.getId_GuiaTipo().trim().equals("4") ) {
					return 2;
				}
				
				//Certidão Narrativa 
				//tipo_guia = 3 
				//e
				//tipo_certidao = 2
				if( guiaEmissaoSPGDt != null 
					&& guiaEmissaoSPGDt.getId_GuiaTipo() != null 
					&& guiaEmissaoSPGDt.getId_GuiaTipo().trim().equals("3")
					&& guiaEmissaoSPGDt.getTipoGuiaCertidaoSPG() != null 
					&& guiaEmissaoSPGDt.getTipoGuiaCertidaoSPG().trim().equals("2") ) {
					return 7;
				}
			}
			
		}
		
		return tipoVinculacaoDefault;
	}
	
	/**
	 * Método para vincular guia série 02 e 06.
	 * @param String numeroGuia
	 * @param Processo Dt processoDt
	 * @param UsuarioDt usuarioDt
	 * @throws Exception
	 */
	private void vinculeGuiaSerie_02_06(String numeroGuia, ProcessoDt processoDt, UsuarioDt usuarioDt) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			//Vincular guia extra judicial
			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
			
			GuiaEmissaoDt guiaEmissaoSPGDt = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoCapital(numeroGuia);
			if( guiaEmissaoSPGDt != null ) {
				guiaEmissaoSPGDt.setComarcaCodigo(ComarcaDt.GOIANIA);
			}
			
			if( guiaEmissaoSPGDt == null ) {
				guiaEmissaoSPGDt = guiaSPGNe.consultarGuiaSPGByNumeroGuiaCompletoInterior(numeroGuia);
				if( guiaEmissaoSPGDt != null ) {
					guiaEmissaoSPGDt.setComarcaCodigo(ComarcaDt.APARECIDA_DE_GOIANIA);
				}
			}
			
			if( guiaEmissaoSPGDt == null ) {
				throw new MensagemException("Guia "+ numeroGuia +" não identificada no SPG!");
			}
			
			if( guiaEmissaoSPGDt != null ) {
				String numeroProcesso = processoDt.getProcessoNumeroCompleto();
				String nProcesso[] = processoDt.getProcessoNumeroCompleto().replaceAll("-", ".").split("\\.");
				numeroProcesso = nProcesso[0] + nProcesso[1] + nProcesso[2];
				
				LogDt obLogDt = new LogDt("VINCULAR_GUIA_ACESSADO", processoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Informacao), "", "[Funcionalidade de vincular guia acessado para este processo;"+ guiaEmissaoSPGDt.getPropriedades() +"]");
				obLog.salvar(obLogDt, obFabricaConexao);
				
				guiaSPGNe.atualizaGuiaVinculadaProcesso(guiaEmissaoSPGDt.getId(), numeroProcesso, guiaEmissaoSPGDt.getComarcaCodigo(), guiaEmissaoSPGDt.getNumeroGuiaCompleto(), usuarioDt.getId(), usuarioDt.getIpComputadorLog());
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			throw ex;
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para vincular guia de locomoção do SPG ao processo PJD
	 */
	private void vinculeGuiaLocomocaoSPG(UsuarioDt usuarioDt, String numeroCompletoGuia, ProcessoDt processoDt) throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try
		{
			if (numeroCompletoGuia == null || numeroCompletoGuia.trim().length() == 0) {
				throw new MensagemException("Informe o número da Guia Inicial!");
			}
			
			if (processoDt == null || processoDt.getId() == null || processoDt.getId().trim().length() == 0) {
				throw new MensagemException("Não foi possível obter o número do processo, favor consultá-lo novamente!");
			}
			
			if(processoDt.isProcessoArquivado()) {
				throw new MensagemException("Processo arquivado não pode receber esta guia.");
			}
			
			if(processoDt.isProcessoErroMigracao()) {
				throw new MensagemException("Processo com ERRO DE MIGRAÇÃO não pode receber esta guia.");
			}
			
			String numeroGuia = numeroCompletoGuia.replaceAll("[/.-]", "").trim();
			
			VincularGuiaLocomocaoSPGProcesso(usuarioDt, processoDt, obFabricaConexao, numeroGuia);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	private void VincularGuiaLocomocaoSPGProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, FabricaConexao obFabricaConexao, String numeroGuia) throws Exception, MensagemException {
		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		GuiaEmissaoDt guiaEmissaoSPGDt = guiaSPGNe.consultarGuiaEmissaoSPG(numeroGuia);
		
		if (guiaEmissaoSPGDt == null) {
			throw new MensagemException("Guia não encontrada no SPG!");
		}
		
		if( guiaEmissaoSPGDt != null && guiaEmissaoSPGDt.getId() != null ) {
			
			if( guiaEmissaoSPGDt.getId_GuiaStatus() != null && guiaEmissaoSPGDt.getId_GuiaStatus().length() > 0 ) {
				if( Integer.parseInt(guiaEmissaoSPGDt.getId_GuiaStatus()) == GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO ) {
					throw new MensagemException("Guia encontrada no SPG com o status de 'Pedido de Ressarcimento' solicitado!");
				}
			}
			
			
			if( !guiaSPGNe.possuiSomenteItensLocomocao(guiaEmissaoSPGDt.getListaGuiaItemDt()) ) {
				throw new MensagemException("Para utilizar esta funcionalidade, a guia deve possuir somente itens de Locomoções ou Postagem!");				
			}
			
			
			obFabricaConexao.iniciarTransacao();
			try
			{
				if( processoDt.getProcessoNumero() != null ) {
					guiaEmissaoSPGDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				}
				
				new ProcessoNe().vinculaGuiaLocomocaoProcesso(guiaEmissaoSPGDt, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao);
				
				obFabricaConexao.finalizarTransacao();
			}
			catch (Exception ex) {
				obFabricaConexao.cancelarTransacao();
				throw ex;
			}
		}
		
	}
	
	private void vinculeGuiaInicial(UsuarioDt usuarioDt, String numeroCompletoGuiaInicial, ProcessoDt processoDt, String motivoVinculacao) throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			if (numeroCompletoGuiaInicial == null || numeroCompletoGuiaInicial.trim().length() == 0) 
				throw new MensagemException("Informe o número da Guia Inicial!");
			
			if (processoDt == null || processoDt.getId() == null || processoDt.getId().trim().length() == 0) 
				throw new MensagemException("Não foi possível obter o número do processo, por favor consultá-lo novamente!");
			
			if(processoDt.isProcessoArquivado()) {
				throw new MensagemException("Processo arquivado não pode receber esta guia.");
			}
			
			if(processoDt.isProcessoErroMigracao()) {
				throw new MensagemException("Processo com ERRO DE MIGRAÇÃO não pode receber esta guia.");
			}
			
			boolean isGuiaLocomocao = false;
			List<String> listaIdGuiaTipo = new ArrayList<String>();
			listaIdGuiaTipo.add(GuiaTipoDt.ID_LOCOMOCAO);
			listaIdGuiaTipo.add(GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR);
			if( this.isGuiaTipoDoTipoEsperado(listaIdGuiaTipo, numeroCompletoGuiaInicial) ) {
				isGuiaLocomocao = true;
			}
			
			//Ocorrência 2019/12924
			//Processo já tem guia inicial?
			//Alteração 29/01/2020 para permitir vincular guia inicial de 1º grau em processo que já tenha guia inicial de 2º grau, e vice-versa.
			if( !isGuiaLocomocao ) {
				
				if( processoDt.isSegundoGrau() ) {
					listaIdGuiaTipo = new ArrayList<String>();
					listaIdGuiaTipo.add(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
					if( this.possuiGuiaEmitida(processoDt.getId(), GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU) ) {
						if( this.isGuiaTipoDoTipoEsperado(listaIdGuiaTipo, numeroCompletoGuiaInicial) ) {
							throw new MensagemException(Configuracao.getMensagem(Configuracao.MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE));
						}
					}
				}
				else {
					listaIdGuiaTipo = new ArrayList<String>();
					listaIdGuiaTipo.add(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
					if( this.possuiGuiaEmitida(processoDt.getId(), GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
						if( this.isGuiaTipoDoTipoEsperado(listaIdGuiaTipo, numeroCompletoGuiaInicial) ) {
							
							if( !isGuiaMesmoProcessoGuiaPrincipal(numeroCompletoGuiaInicial, processoDt.getId()) ) {
								throw new MensagemException(Configuracao.getMensagem(Configuracao.MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE));
							}
							
						}
					}
				}
				
			}
			
			String numeroGuia = numeroCompletoGuiaInicial.replaceAll("[/.-]", "").trim();
			
			if( Funcoes.isNumeroGuiaProjudiValido(numeroGuia) ) {
				//guia inicial projudi				
				VincularGuiaProjudiProcesso(usuarioDt, processoDt, obFabricaConexao, numeroGuia, motivoVinculacao);
			}
			else {
				GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
				GuiaEmissaoDt guiaSPGDt = guiaSPGNe.consultarGuiaSPGCapitalInterior(numeroGuia);
				
				GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
				GuiaEmissaoDt guiaSSGDt = guiaSSGNe.consultarGuiaEmissaoSSG(numeroGuia);
				
				if (guiaSPGDt == null && guiaSSGDt == null) 
					throw new MensagemException("Guia não encontrada no SPG ou SSG!");
				
				//guia inicial SPG
				boolean guiaVinculadaSPG = VincularGuiaSPGProcesso(usuarioDt, processoDt, obFabricaConexao, numeroGuia, motivoVinculacao);							
				
				//Guia inicial SSG
				if( !guiaVinculadaSPG ) {
					//processo id guia tipo
					VincularGuiaSSGProcesso(usuarioDt, processoDt, obFabricaConexao, numeroGuia, processoDt.getId_ProcessoTipo(), motivoVinculacao);
				}
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}		
	}
	
	private boolean VincularGuiaSPGProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, FabricaConexao obFabricaConexao, String numeroGuia, String motivoVinculacao) throws Exception, MensagemException {
		
		boolean retorno = false;
		
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		GuiaEmissaoDt guiaEmissaoSPGDt = guiaSPGNe.consultarGuiaEmissaoInicialSPG(numeroGuia, processoDt);
		
//		if (guiaEmissaoSPGDt == null) 
//			throw new MensagemException("Guia não encontrada no SPG! Esta guia pode ser uma guia complementar da inicial. Por favor, tente utilizar a opção do menu de <b>Vincular Guia Complementar</b>.");
		
//***********************************************************
//Comentado a pedido da Corregedoria na ocorrência 2016/31159
//		if (Funcoes.StringToInt(guiaEmissaoDt.getId_GuiaStatus()) != GuiaStatusDt.PAGO) 
//			throw new MensagemException("Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + 
//                 "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que impede o vínculo ao processo.");
//***********************************************************
		
		if( guiaEmissaoSPGDt != null && guiaEmissaoSPGDt.getId() != null ) {
			if( guiaEmissaoSPGDt.getId_GuiaStatus() != null && guiaEmissaoSPGDt.getId_GuiaStatus().length() > 0 ) {
				if( Integer.parseInt(guiaEmissaoSPGDt.getId_GuiaStatus()) == GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO ) {
					throw new MensagemException("Guia encontrada no SPG com o status de 'Pedido de Ressarcimento' solicitado!");
				}
			}
			
			obFabricaConexao.iniciarTransacao();
			try {
				if( processoDt.getProcessoNumero() != null ) {
					guiaEmissaoSPGDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				}
				
				new ProcessoNe().vinculaGuiaProcesso(guiaEmissaoSPGDt, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, motivoVinculacao);
				
				retorno = true;
				
				obFabricaConexao.finalizarTransacao();
			}
			catch (Exception ex) {
				obFabricaConexao.cancelarTransacao();
				throw ex;
			}
		}
		
		return retorno;
	}
	
	public boolean VincularGuiaSSGProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, FabricaConexao obFabricaConexao, String numeroGuia, String idProcessoTipoEscolhido, String motivoVinculacao) throws Exception, MensagemException {
		
		boolean retorno = false;
		
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		
		GuiaEmissaoDt guiaEmissaoSSGDt = guiaSSGNe.consultarGuiaEmissaoSSG(numeroGuia);
		
//		if (guiaEmissaoSSGDt == null) 
//			throw new MensagemException("Guia não encontrada no SSG!");
		
		if( guiaEmissaoSSGDt != null && guiaEmissaoSSGDt.getId() != null ) {
			if( idProcessoTipoEscolhido != null && !idProcessoTipoEscolhido.isEmpty() ) {
				guiaEmissaoSSGDt.setId_ProcessoTipo(idProcessoTipoEscolhido);
			}
			
			if( guiaEmissaoSSGDt.getId_GuiaStatus() != null && guiaEmissaoSSGDt.getId_GuiaStatus().length() > 0 ) {
				if( Integer.parseInt(guiaEmissaoSSGDt.getId_GuiaStatus()) == GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO ) {
					throw new MensagemException("Guia encontrada no SSG com o status de 'Pedido de Ressarcimento' solicitado!");
				}
			}
			
			obFabricaConexao.iniciarTransacao();
			try {
				if( processoDt.getProcessoNumero() != null ) {
					guiaEmissaoSSGDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				}
				
				new ProcessoNe().vinculaGuiaSSGProcesso(guiaEmissaoSSGDt, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, motivoVinculacao);
				
				retorno = true;
				
				obFabricaConexao.finalizarTransacao();
			}
			catch (Exception ex) {
				obFabricaConexao.cancelarTransacao();
				throw ex;
			}
		}
		
		return retorno;
	}

	private void VincularGuiaProjudiProcesso(UsuarioDt usuarioDt, ProcessoDt processoDt, FabricaConexao obFabricaConexao, String numeroGuia, String motivoVinculacao) throws Exception, MensagemException {
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		ProcessoNe processoNe = new ProcessoNe();
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		
		GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarNumeroCompleto(numeroGuia, null, null);
		
		if (guiaEmissaoDt == null) 
			throw new MensagemException("Guia não encontrada!");
		
//		if (!guiaEmissaoNe.isGuiaInicial(guiaEmissaoDt.getId(), obFabricaConexao)) {
//			throw new MensagemException("Esta Guia não é uma Guia Inicial! Tipo da guia " + guiaEmissaoDt.getGuiaTipo() + ".");
//		}
		
		if(guiaEmissaoDt != null && guiaEmissaoDt.getId_ProcessoTipo() != null) {
			ProcessoTipoDt classeGuia = processoTipoNe.consultarId(guiaEmissaoDt.getId_ProcessoTipo());
			//Verifica se a classe da guia é carta precatória, se sim, verifica se o processo tb é de carta precatória(qualquer uma das classes)
			if( classeGuia != null && classeGuia.getProcessoTipoCodigo() != null && processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(classeGuia.getProcessoTipoCodigo()))) {
				if( processoDt != null && processoDt.getId_ProcessoTipo() != null && !processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(processoDt.getProcessoTipoCodigo())) ) {
					throw new MensagemException("A Guia possui a classe " + classeGuia.getProcessoTipo() + " e o processo a classe " + processoDt.getProcessoTipo() + ". Não é permitido vincular guia de Carta de Precatória em processos de outras classes.");
				}
			}
		}
		
		if (guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
			ProcessoDt processoVinculadoDt = processoNe.consultarId(guiaEmissaoDt.getId_Processo());
			if (processoVinculadoDt != null) {
				throw new MensagemException("Guia inicial  <b>" + 
			                                 Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + 
			                                 "</b> encontrada, mas já foi utilizada para o cadastro do processo " + 
			                                 processoVinculadoDt.getProcessoNumeroCompleto() + ".");
			}
		}
		
		if (guiaEmissaoNe.verificarGuiaCanceladaIndependentePagamento(numeroGuia)) {
			throw new MensagemException("Guia inicial  <b>" + 
		                                 Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + 
		                                 "</b> está cancelada.");
		}
		
		String numeroProcessoSPGGuiaProjudi = guiaSPGNe.consultarNumeroProcessoSPGGuiaEmissaoInicialProjudi(numeroGuia);
		
		if (numeroProcessoSPGGuiaProjudi != null && numeroProcessoSPGGuiaProjudi.trim().length() > 0) {
			throw new MensagemException("Guia inicial  <b>" + 
		                                 Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + 
		                                 "</b> encontrada, mas já foi utilizada para o cadastro do processo SPG número " + 
		                                 numeroProcessoSPGGuiaProjudi + "." );
		}

//***********************************************************
//Comentado a pedido da Corregedoria na ocorrência 2016/31159		
//		if (!guiaEmissaoNe.isGuiaPaga(numeroGuia)) 
//			throw new MensagemException("Guia inicial  <b>" + Funcoes.FormatarNumeroSerieGuia(numeroGuia) + 
//					                    "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que impede o vínculo ao processo.");
//***********************************************************
		
		GuiaEmissaoDt guiaEmissaoDtPrincipal = null;
		if( guiaEmissaoDt.getId_GuiaEmissaoPrincipal() != null && !guiaEmissaoDt.getId_GuiaEmissaoPrincipal().isEmpty() ) {
			guiaEmissaoDtPrincipal = guiaEmissaoNe.consultarId(guiaEmissaoDt.getId_GuiaEmissaoPrincipal(), obFabricaConexao);
		}
		
		obFabricaConexao.iniciarTransacao();		
		try {
			if( processoDt.getProcessoNumero() != null ) {
				guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				if( guiaEmissaoDtPrincipal != null ) {
					guiaEmissaoDtPrincipal.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				}
			}
			
			processoNe.vinculaGuiaProcesso(guiaEmissaoDt, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, motivoVinculacao);
			
			if( guiaEmissaoDtPrincipal != null && guiaEmissaoDtPrincipal.getId_Processo() == null ) {
				processoNe.vinculaGuiaProcesso(guiaEmissaoDtPrincipal, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, motivoVinculacao);
			}
			
			obFabricaConexao.finalizarTransacao();	
		} catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			throw ex;
		}
	}
	
	public void vincularGuiaComplementar(UsuarioDt usuarioDt, VincularGuiaComplementarProcessoDt vincularGuiaComplementarProcessoDt, ProcessoDt processoDt) throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			if( vincularGuiaComplementarProcessoDt != null && vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto() != null ) {
				GuiaEmissaoDt guiaEmissaoProjudiDt = this.consultarGuiaEmissaoNumeroGuia(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto());
				
				//Guia na base do projudi, série 50 e com id_guia_emis_principal informado?
				if( guiaEmissaoProjudiDt != null && guiaEmissaoProjudiDt.getId_GuiaEmissaoPrincipal() != null && !guiaEmissaoProjudiDt.getId_GuiaEmissaoPrincipal().isEmpty() ) {
					this.VincularGuiaComplementarProjudi(usuarioDt, processoDt, obFabricaConexao, vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto());
				}
				//Caso contrário entra no fluxo normal de guia do SPG
				else {
					GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
					GuiaEmissaoDt guiaSPGDt = guiaSPGNe.consultarGuiaSPGCapitalInterior(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto());
					
					GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
					GuiaEmissaoDt guiaSSGDt = guiaSSGNe.consultarGuiaEmissaoSSG(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto());
					
					if (guiaSPGDt == null && guiaSSGDt == null) 
						throw new MensagemException("Guia não encontrada no SPG ou SSG!");
					
					//vincula SPG
					boolean guiaVinculadaSPG = this.vinculeGuiaComplementarSPGPrimeiroGrau(usuarioDt, vincularGuiaComplementarProcessoDt, processoDt, obFabricaConexao);
					
					//vincula SSG
					if( !guiaVinculadaSPG ) {
						this.VincularGuiaSSGProcesso(usuarioDt, processoDt, obFabricaConexao, vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto(), vincularGuiaComplementarProcessoDt.getId_ProcessoTipo(), "");
					}
				}
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	private void VincularGuiaComplementarProjudi(UsuarioDt usuarioDt, ProcessoDt processoDt, FabricaConexao obFabricaConexao, String numeroGuia) throws Exception, MensagemException {
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		
		GuiaEmissaoDt guiaEmissaoDt = this.consultarNumeroCompleto(numeroGuia, null, null);
		
		if (guiaEmissaoDt == null) 
			throw new MensagemException("Guia não encontrada!");
		
		if(guiaEmissaoDt != null && guiaEmissaoDt.getId_ProcessoTipo() != null) {
			ProcessoTipoDt classeGuia = processoTipoNe.consultarId(guiaEmissaoDt.getId_ProcessoTipo());
			//Verifica se a classe da guia é carta precatória, se sim, verifica se o processo tb é de carta precatória(qualquer uma das classes)
			if( classeGuia != null && classeGuia.getProcessoTipoCodigo() != null && processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(classeGuia.getProcessoTipoCodigo()))) {
				if( processoDt != null && processoDt.getId_ProcessoTipo() != null && !processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(processoDt.getProcessoTipoCodigo())) ) {
					throw new MensagemException("A Guia possui a classe " + classeGuia.getProcessoTipo() + " e o processo a classe " + processoDt.getProcessoTipo() + ". Não é permitido vincular guia de Carta de Precatória em processos de outras classes.");
				}
			}
		}
		
		if (!this.isGuiaComplementar(numeroGuia, obFabricaConexao))  
			throw new MensagemException("Esta Guia não é uma Guia Complementar! Tipo da guia " + guiaEmissaoDt.getGuiaTipo() + ".");		
		
		if (guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().trim().length() > 0) {
			ProcessoDt processoVinculadoDt = new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo());
			if (processoVinculadoDt != null) 
				throw new MensagemException("Guia Complementar  <b>" + 
			                                 Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + 
			                                 "</b> encontrada, mas já foi utilizada para o processo " + 
			                                 processoVinculadoDt.getProcessoNumeroCompleto() + ".");			
		}
		
		if (this.verificarGuiaCanceladaIndependentePagamento(numeroGuia)) 
			throw new MensagemException("Guia Complementar  <b>" + 
		                                 Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + 
		                                 "</b> está cancelada.");
		
		String numeroProcessoSPGGuiaProjudi = guiaSPGNe.consultarNumeroProcessoSPGGuiaEmissaoInicialProjudi(numeroGuia);
		
		if (numeroProcessoSPGGuiaProjudi != null && numeroProcessoSPGGuiaProjudi.trim().length() > 0)
			throw new MensagemException("Guia Complementar  <b>" + 
		                                 Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()) + 
		                                 "</b> encontrada, mas já foi utilizada no processo SPG número " + 
		                                 numeroProcessoSPGGuiaProjudi + "." );

		GuiaEmissaoDt guiaEmissaoDtPrincipal = null;
		if( guiaEmissaoDt.getId_GuiaEmissaoPrincipal() != null && !guiaEmissaoDt.getId_GuiaEmissaoPrincipal().isEmpty() ) {
			guiaEmissaoDtPrincipal = this.consultarId(guiaEmissaoDt.getId_GuiaEmissaoPrincipal(), obFabricaConexao);
		}
		
		obFabricaConexao.iniciarTransacao();
		
		try {
			
			if( processoDt.getProcessoNumero() != null ) {
				guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				if( guiaEmissaoDtPrincipal != null ) {
					guiaEmissaoDtPrincipal.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
				}
			}
			
			new ProcessoNe().vinculaGuiaProcesso(guiaEmissaoDt, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, null);
			
			if( guiaEmissaoDtPrincipal != null && guiaEmissaoDtPrincipal.getId_Processo() == null ) {
				new ProcessoNe().vinculaGuiaProcesso(guiaEmissaoDtPrincipal, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, null);
			}
			
			obFabricaConexao.finalizarTransacao();	
		}
		catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			throw ex;
		}
	}

	private boolean vinculeGuiaComplementarSPGPrimeiroGrau(UsuarioDt usuarioDt, VincularGuiaComplementarProcessoDt vincularGuiaComplementarProcessoDt
			, ProcessoDt processoDt, FabricaConexao obFabricaConexao) throws Exception {
		
		boolean retorno = false;
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
				
		if (vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto() == null || vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto().trim().length() == 0) 
			throw new MensagemException("Informe o número da Guia Complementar!");
		
		if (processoDt == null || processoDt.getId() == null || processoDt.getId().trim().length() == 0) 
			throw new MensagemException("Não foi possível obter o número do processo, favor consultá-lo novamente!");
		
		if(processoDt.isProcessoArquivado()) {
			throw new MensagemException("Processo arquivado não pode receber esta guia.");
		}
		if(processoDt.isErroMigracao()) {
			throw new MensagemException("Processo com ERRO DE MIGRAÇÃO não pode receber esta guia.");
		}
		
		vincularGuiaComplementarProcessoDt.setNumeroGuiaCompleto(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto().replaceAll("[/.-]", "").trim());
		
		if( Funcoes.isNumeroGuiaSerie50Valido(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto()) ) {
			throw new MensagemException("Somente é permitido vincular guias complementares com séries diferentes de 50.");
		}
		
		//guia complementar projudi	só é emitida vinculada ao processo...	
		GuiaEmissaoDt guiaEmissaoProjudiDt = this.consultarGuiaEmissaoNumeroGuia(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto());
		
		if (guiaEmissaoProjudiDt != null) {
			ProcessoDt processoGuiaDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoProjudiDt.getId_Processo());
			if (processoGuiaDt != null) {
				throw new MensagemException("Guia já vinculada ao processo número " + processoGuiaDt.getProcessoNumeroCompleto() + ".");	
			} else {
				throw new MensagemException("Guia já importada para a base do projudi, mas não possui vínculo com processo.");
			}
		}
		
		GuiaEmissaoDt guiaComplementarSPGDt = guiaSPGNe.consultarGuiaEmissaoComplementarSPG(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto(), processoDt.getId());
		
		if (guiaComplementarSPGDt == null) 
//			throw new MensagemException("Guia complementar não encontrada no SPG! Caso seja uma guia que possua somente item de locomoção, por favor, utilize a funcionalidade de Vincular Guia de Locomoção.");
			return false;

			//***********************************************************
			//Comentado a pedido da Corregedoria na ocorrência 2016/31159
			//			if (Funcoes.StringToInt(guiaComplementarSPGDt.getId_GuiaStatus()) != GuiaStatusDt.PAGO) 
			//				throw new MensagemException("Guia complementar  <b>" + Funcoes.FormatarNumeroSerieGuia(vincularGuiaComplementarProcessoDt.getNumeroGuiaCompleto()) + 
			//	                 "</b>  encontrada, mas até o momento <b>não</b> consta o pagamento da mesma no sistema, o que impede o vínculo ao processo.");
			//***********************************************************
		
		if( guiaComplementarSPGDt != null && guiaComplementarSPGDt.getId() != null ) {
			if( guiaComplementarSPGDt.getId_GuiaStatus() != null && guiaComplementarSPGDt.getId_GuiaStatus().length() > 0 ) {
				if( Integer.parseInt(guiaComplementarSPGDt.getId_GuiaStatus()) == GuiaStatusDt.PEDIDO_RESSARCIMENTO_SOLICITADO ) {
					throw new MensagemException("Guia encontrada no SPG com o status de 'Pedido de Ressarcimento' solicitado!</b>.");
				}
			}
		}
		
		GuiaEmissaoDt guiaInicialDoProcesso = this.consultarGuiaEmissaoInicial(processoDt.getId());
		if(guiaInicialDoProcesso == null) {
			guiaInicialDoProcesso = guiaSPGNe.consultarGuiaEmissaoInicialSPGNumeroProcesso(processoDt.getProcessoNumeroCompleto());
			if (guiaInicialDoProcesso == null) {
				throw new MensagemException("O processo não possui guia inicial vinculada. Antes de vincular uma guia complementar é necessário vincular uma guia inicial.");	
			} else {
				obFabricaConexao.iniciarTransacao();		
				try 
				{
					ServentiaDt serventiaDt = new ServentiaNe().consultarId(processoDt.getId_Serventia(), obFabricaConexao);
					new ProcessoNe().vincularGuiaInicialAoProcesso(processoDt.getId(), guiaInicialDoProcesso.getId(), guiaInicialDoProcesso.isGuiaEmitidaSPG(), obFabricaConexao);
					this.salvarGuiaSPGNoProjudi(guiaInicialDoProcesso.getNumeroGuiaCompleto(), false, processoDt.getId_ProcessoTipo(), processoDt.getId(), serventiaDt.getId_Comarca(), UsuarioDt.SistemaProjudi, "127.0.0.1", obFabricaConexao);
											
					obFabricaConexao.finalizarTransacao();	
				} catch (Exception ex) {
					obFabricaConexao.cancelarTransacao();
					throw ex;
				}
				
				guiaInicialDoProcesso = this.consultarGuiaEmissaoInicial(processoDt.getId());
			}
		}
		
		//Só valida se o processo tiver classe do CNJ, ou seja, se estiver consistente, pos do contrário terá que informar uma nova classe... 
		if (processoDt.getId_ProcessoTipo() != null && processoDt.getId_ProcessoTipo().trim().length() > 0)
		{
			//Se a guia inicial possuir natureza, iremos validar se a complementar possui a mesma natureza, caso contrário terá que informar a classe...
			if (guiaInicialDoProcesso.getId_NaturezaSPG() != null && guiaInicialDoProcesso.getId_NaturezaSPG().trim().length() > 0) {
				
				if (vincularGuiaComplementarProcessoDt.getId_ProcessoTipo() != null && vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim().length() > 0) {
					//Informou a nova classe...
					if (guiaComplementarSPGDt.getId_NaturezaSPG() != null && guiaComplementarSPGDt.getId_NaturezaSPG().trim().length() > 0 &&
						guiaInicialDoProcesso.getId_NaturezaSPG().trim().equalsIgnoreCase(guiaComplementarSPGDt.getId_NaturezaSPG()))
							throw new MensagemException("Não houve alteração da natureza vinculada à guia inicial, portanto a classe não poderá ser alterada.");
					
				} else {						
					//Não informou a nova classe...
					if (guiaComplementarSPGDt.getId_NaturezaSPG() != null && guiaComplementarSPGDt.getId_NaturezaSPG().trim().length() > 0 &&
						!guiaInicialDoProcesso.getId_NaturezaSPG().trim().equalsIgnoreCase(guiaComplementarSPGDt.getId_NaturezaSPG()))
							throw new MensagemException("Houve alteração da natureza vinculada à guia inicial, portanto a nova classe deverá ser informada.");
				}	
			} else if (vincularGuiaComplementarProcessoDt.getId_ProcessoTipo() == null || vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim().length() == 0) {
				throw new MensagemException("A nova classe deve ser informada.");
			}
		} else if (vincularGuiaComplementarProcessoDt.getId_ProcessoTipo() == null || vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim().length() == 0) {
			throw new MensagemException("A nova classe deve ser informada.");
		}
		
		if (vincularGuiaComplementarProcessoDt.getId_ProcessoTipo() != null && vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim().length() > 0 &&
			processoDt.getId_ProcessoTipo() != null && processoDt.getId_ProcessoTipo().trim().length() > 0 &&
			processoDt.getId_ProcessoTipo().trim().equalsIgnoreCase(vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim()))
				throw new MensagemException("A nova classe deve ser diferente da classe atual do processo.");
		
		String idProcessoTipo = processoDt.getId_ProcessoTipo();	
		String classe = processoDt.getProcessoTipo();
		if (vincularGuiaComplementarProcessoDt.getId_ProcessoTipo() != null && vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim().length() > 0) {
			idProcessoTipo = vincularGuiaComplementarProcessoDt.getId_ProcessoTipo().trim();
			classe = vincularGuiaComplementarProcessoDt.getProcessoTipo().trim();
		}
		
		//Grau do processo para pegar o tipo da guia
		String tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU; //default
		if( processoDt != null && processoDt.getId_Serventia() != null ) {
			if( this.isProcessoSegundoGrau(processoDt.getId_Serventia()) ) {
				tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU;
			}
		}
		
		GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(obFabricaConexao, tipoGuiaComplementar, idProcessoTipo);			
		if (guiaModeloDt == null) throw new MensagemException("Não foi encontrado modelo para a guia inicial de classe " + classe + ".");
		
		guiaComplementarSPGDt.setId_ProcessoTipo(idProcessoTipo);
		guiaComplementarSPGDt.setProcessoTipo(classe);
		guiaComplementarSPGDt.setGuiaModeloDt(guiaModeloDt);
		guiaComplementarSPGDt.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
		
		obFabricaConexao.iniciarTransacao();
		try {
			new ProcessoNe().vinculaGuiaProcesso(guiaComplementarSPGDt, processoDt.getId(), processoDt.getProcessoNumeroAntigoTemp(), processoDt.getId_Serventia(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), obFabricaConexao, null);
			
			obFabricaConexao.finalizarTransacao();
			
			retorno = true;
			
		}
		catch (Exception ex) {
			obFabricaConexao.cancelarTransacao();
			throw ex;
		}
		
		return retorno;
	}
	
	/**
	 * Método para realizar o reprocessamento dos arquivos de rajada para tentar buscar os registros que 
	 * tiveram o estorno do pagamento realizado no arquivo completo do dia seguinte processado pelo SAJ.
	 * @throws Exception
	 */
	public void reprocessarGuiasCaixa() throws Exception {
		ArquivoBancoNe arquivoBancoNe = new ArquivoBancoNe();
		GuiaSPGNe guiaSGPNe = new GuiaSPGNe();
		
		FabricaConexao obFabricaConexao = null;
		
		try 
		{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			List<ArquivoBancoDt> arquivosLidos = arquivoBancoNe.consulteArquivosLidosParaReprocessamento();
			
			for(ArquivoBancoDt arquivo : arquivosLidos) {
				
				obFabricaConexao.iniciarTransacao();
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				if (arquivo.getConteudo() != null) {
					Reader inputString = new StringReader(arquivo.getConteudo().trim());
					BufferedReader in = new BufferedReader(inputString);
					GerenciaArquivo gerenciaArquivo = new GerenciaArquivoCEF(obFabricaConexao);
					String conteudoLinha = "";
					TJDataHora DataHoraGeracao = null;
					
					List<GuiaEmissaoSajaDocumentoDt> possiveisEstornos = new ArrayList<GuiaEmissaoSajaDocumentoDt>();
					boolean possuiGuiaProcessadaPaga = false;
					
					while (in.ready() ) {
						conteudoLinha = in.readLine();
						
						if (conteudoLinha != null && conteudoLinha.length() > 0) {
							int tipoDeRegistro = Funcoes.StringToInt(conteudoLinha.substring(LeiauteEscritaArquivoCEF.INICIO_TIPO_REGISTRO, LeiauteEscritaArquivoCEF.FIM_TIPO_REGISTRO).trim(), -1);
							
							if (tipoDeRegistro == LeiauteEscritaArquivoCEF.TIPO_REGISTRO_DETALHE) {
								String tipoDeRegistroDetalhe = conteudoLinha.substring(LeiauteEscritaArquivoCEF.INICIO_TIPO_REGISTRO_DETALHE, LeiauteEscritaArquivoCEF.FIM_TIPO_REGISTRO_DETALHE).trim();
								
								if (tipoDeRegistroDetalhe.trim().equalsIgnoreCase("T")) {
									//15.3T | Número Documento (Seu Nº) | Número do Documento de Cobrança | 59 | 69 | X(011) | Seu Número do documento, conforme informado pelo Beneficiário | C011
									String numeroGuia = conteudoLinha.substring(LeiauteLeituraArquivoCEF.INICIO_NUMERO_DOCUMENTO, LeiauteLeituraArquivoCEF.FIM__NUMERO_DOCUMENTO).trim();																		
									GuiaEmissaoDt guiaEmissaoDt = obPersistencia.consultarGuiaEmissaoNumeroGuia(numeroGuia);									
									
									if (guiaEmissaoDt != null && guiaEmissaoDt.isGuiaPaga()) {
										SajaDocumentoDt sajaDocumentoDt = guiaSGPNe.obtenhaSajaDocumento(Funcoes.ObtenhaSomenteNumeroGuiaSemSerie(numeroGuia), Funcoes.ObtenhaSomenteSerieDaGuia(numeroGuia));
										if (sajaDocumentoDt == null) {
											GuiaEmissaoSajaDocumentoDt possivelEstorno = new GuiaEmissaoSajaDocumentoDt();
											possivelEstorno.setGuiaEmissaoDt(guiaEmissaoDt);
											possiveisEstornos.add(possivelEstorno);
										} else if (sajaDocumentoDt.getCodigoDocumento() != null) {
											if (sajaDocumentoDt.getCodigoDocumento().trim().startsWith("19000101")) {
												GuiaEmissaoSajaDocumentoDt possivelEstorno = new GuiaEmissaoSajaDocumentoDt();
												possivelEstorno.setGuiaEmissaoDt(guiaEmissaoDt);
												possivelEstorno.setSajaDocumento(sajaDocumentoDt);									
												possiveisEstornos.add(possivelEstorno);	
											} else {
												possuiGuiaProcessadaPaga = true;
											}
										}
									}									
								}
							}	
						} else {
							break;
						}
					}
					
					in.close();
					
					if (possuiGuiaProcessadaPaga) {						
						for (GuiaEmissaoSajaDocumentoDt possivelEstorno : possiveisEstornos) {
							if (possivelEstorno.getSajaDocumento() != null) {
								guiaSGPNe.excluaSajaDocumento(possivelEstorno.getSajaDocumento().getId());
							}	
							
							obPersistencia.estornarPagamentoPeloBanco(possivelEstorno.getGuiaEmissaoDt().getNumeroGuiaCompleto());
						}						
						arquivoBancoNe.atualizaArquivoReprocessado(arquivo.getId());
					}
				}
				
				obFabricaConexao.finalizarTransacao();
			}
		}
		catch(Exception e) {
			if( obFabricaConexao != null ) {
				obFabricaConexao.cancelarTransacao();
			}
			
			throw e;
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}			
		}	
	}
	
	/**
	 * Método para verificar e alterar status da guia que gerou o parcelamento.
	 * Se a guia referencia estiver com todas as parcelas pagas então muda o seu status.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	private void alterarStatusGuiaParcelada(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexao) throws Exception {
		if( guiaEmissaoDt != null 
			&& guiaEmissaoDt.getId() != null
			&& guiaEmissaoDt.getId_Processo() != null
			&& guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento() != null ) {
			
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			//consulta se a guia de referencia tem parcelas aguardando pagamento
			List<GuiaEmissaoDt> listaParcelasGuia = obPersistencia.consultarParcelasPelaGuiaReferencia(guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento());
			
			if( listaParcelasGuia != null ) {
				boolean parcelamentoPago = true;
				for(GuiaEmissaoDt guiaEmissaoParcelaDt: listaParcelasGuia) {
					if( !this.isGuiaPaga(obFabricaConexao, guiaEmissaoParcelaDt) ) {
						parcelamentoPago = false;
						break;
					}
				}
				
				//Se não tem parcelas em aberto, então altera o status da guia.
				if( parcelamentoPago ) {
					
					GuiaEmissaoDt guiaEmissaoReferenciaDt = this.consultarGuiaEmissao(guiaEmissaoDt.getIdGuiaReferenciaDescontoParcelamento());
					
					if( guiaEmissaoReferenciaDt != null ) {
						//Log
						LogDt logDt = new LogDt("GuiaEmissao", guiaEmissaoReferenciaDt.getId(), UsuarioDt.SistemaProjudi, "127.0.0.1", String.valueOf(LogTipoDt.Alterar), "", "[Id_GuiaEmissao:"+guiaEmissaoReferenciaDt.getId()+";Id_GuiaStatus:"+GuiaStatusDt.PARCELAMENTO_PAGO+"]");
						new LogNe().salvar(logDt, obFabricaConexao);
						
						//atualiza status da guia que gerou o parcelamento
						this.atualizarStatusGuiaEmitida(obFabricaConexao, guiaEmissaoReferenciaDt.getId(), String.valueOf(GuiaStatusDt.PARCELAMENTO_PAGO));
					}
				}
			}
				
		}
	}
	
	/**
	 * Método que consulta a quantidade de boletos emitidos no dia de hoje.
	 * 
	 * @return long
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public long consultarQuantidadeBoletosEmitidosHoje() throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
			return obPersistencia.consultarQuantidadeBoletosEmitidosHoje();		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para fazer tratamento de partes do processo quando este for sigiloso ou segredo de justiça.
	 * A consulta pública limpa todos os dados das partes, e eu preciso do id da parte para guardar na guia para posterior uso.
	 * 
	 * @param ProcessoDt processoDt
	 * @throws Exception
	 */
	public void tratamentoParaProcessosSigilosoSegredoJustica(ProcessoDt processoDt) throws Exception {
		if( processoDt != null && ( processoDt.isSegredoJustica() || processoDt.isSigiloso() ) ) {
			
			ProcessoParteNe processoParteNe = new ProcessoParteNe();
			
			List listaPromoventes = processoParteNe.consultarPromoventesAtivos(processoDt.getId());
			List listaPromovidos = processoParteNe.consultarPromovidosAtivos(processoDt.getId());
			
			processoDt.setDataRecebimento(Funcoes.FormatarData(new TJDataHora().getDate()));
			
			if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
				for(int i = 0; i < listaPromoventes.size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
					
					if( parteDt.getId() != null ) {
						if( parteDt != null ) {
							String id = parteDt.getId();
							String nome = parteDt.getNome();
							parteDt.limpar();
							parteDt.setId(id);
							parteDt.setNome(Funcoes.iniciaisNome(nome));
						}
					}
				}
				
				processoDt.setListaPolosAtivos(listaPromoventes);
			}
			
			if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
				for(int i = 0; i < listaPromovidos.size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					
					if( parteDt.getId() != null ) {
						if( parteDt != null ) {
							String id = parteDt.getId();
							String nome = parteDt.getNome();
							parteDt.limpar();
							parteDt.setId(id);
							parteDt.setNome(Funcoes.iniciaisNome(nome));
						}
					}
				}
				
				processoDt.setListaPolosPassivos(listaPromovidos);
			}
			
			if( processoDt.getValor() == null || ( processoDt.getValor() != null && processoDt.getValor().isEmpty() ) ) {
				ProcessoDt procDt = new ProcessoNe().consultarId(processoDt.getId());
				if( procDt != null ) {
					processoDt.setValor(procDt.getValor());
				}
			}
			
		}
	}
	
	/**
	 * Método para desfazer cancelamento da guia.
	 * 
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean desfazerCancelamentoGuia(GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		boolean retorno = false;
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
			
			if( guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length() > 0 ) {
				
				if( !this.isGuiaCancelada(guiaEmissaoDt.getId(), obFabricaConexao) ) {
					
//					if( guiaEmissaoDt.isGuiaVencida() ) {
//						throw new MensagemException("Guia está vencida, sem autorização para realizar esta ação!");
//					}

					ProcessoDt processoDt = new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo(), obFabricaConexao);
					if( processoDt == null ) {
						throw new MensagemException("Guia sem Processo Vinculado!");
					}
					
					String vetorProcessoNumero[] = processoDt.getProcessoNumeroCompleto().replace("-", ".").split("\\.");
					String numeroProcessoDigitoAno = vetorProcessoNumero[0] + vetorProcessoNumero[1] + vetorProcessoNumero[2];
					
					retorno = obPersistencia.desfazerCancelamentoGuia(guiaEmissaoDt.getId());
					
					if( retorno ) {
						//SPG
						new GuiaSPGNe().desfazerCancelamentoGuiaSPG(guiaEmissaoDt.getNumeroGuiaCompleto(), numeroProcessoDigitoAno, obFabricaConexao, guiaEmissaoDt.getId_Usuario(), guiaEmissaoDt.getIpComputadorLog());
					
						//SSG
						new GuiaSSGNe().desfazerCancelamentoGuiaSSG(guiaEmissaoDt.getNumeroGuiaCompleto(), numeroProcessoDigitoAno, obFabricaConexao, guiaEmissaoDt.getId_Usuario(), guiaEmissaoDt.getIpComputadorLog());
					}
					
					obLogDt = new LogDt("GuiaEmissao",guiaEmissaoDt.getId(), guiaEmissaoDt.getId_Usuario(),guiaEmissaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),"", "[Id_GuiaEmissao:"+ guiaEmissaoDt.getId() +";Id_GuiaStatus:" + GuiaStatusDt.AGUARDANDO_PAGAMENTO +";Id_Usuario:" + guiaEmissaoDt.getId_Usuario()+";Motivo:Desfazendo cancelamento da guia.]");
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para adicionar no request Attribute a quantidade de itens de despesa postal no processo.
	 * 
	 * @param HttpServletRequest request
	 * @param String idProcesso
	 * @throws Exception
	 * @author fasoares
	 */
	public void consultaQuantidadeItemDespesaPostalProcesso(HttpServletRequest request, String idProcesso) throws Exception {
		if( request != null && idProcesso != null ) {
			GuiaItemDisponivelNe guiaItemDisponivelNe = new GuiaItemDisponivelNe();
			
			request.setAttribute("quantidadeItemDepesaPostalNaoPago", guiaItemDisponivelNe.consultaQuantidadeItemDepesaPostalNaoPago(idProcesso));
			request.setAttribute("quantidadeItemDepesaPostalPagoSemVinculoPendencia", guiaItemDisponivelNe.consultaQuantidadeItemDepesaPostalPagoSemVinculoPendencia(idProcesso));
			request.setAttribute("quantidadeItemVinculadoPendencia", guiaItemDisponivelNe.consultaQuantidadeItemVinculadoPendencia(idProcesso));
		}
	}
	
	/**
	 * Método para adicionar no request Attribute a quantidade de ordem de serviço despesa postal aberta e fechado
	 * @param HttpServletRequest request
	 * @param String idProcesso
	 * @throws Exception
	 * @author fasoares
	 */
	public void consultaQuantidadeOrdemServicoDespesaPostalAbertaFechada(HttpServletRequest request, String idProcesso) throws Exception {
		if( request != null && idProcesso != null ) {
			PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
			
			request.setAttribute("quantidadeOSAberta", pendenciaCorreiosNe.consultaQuantidadePendenciaCorreiosOrdemServicoAbertas(idProcesso));
			request.setAttribute("quantidadeOSFechada", pendenciaCorreiosNe.consultaQuantidadePendenciaFinalCorreiosOrdemServicoFechada(idProcesso));
		}
	}
	

	/**
	 * Método para adicionar no request Attribute a quantidade de locomoções disponíveis no processo.
	 * 
	 * @param HttpServletRequest request
	 * @param String idProcesso
	 * @throws Exception
	 * @author fasoares
	 */
	public void consultaQuantidadeLocomocoesDisponivelProcesso(HttpServletRequest request, String idProcesso) throws Exception {
		String guia_status = "";
		if( request != null && idProcesso != null ) {
			LocomocaoNe locomocaoNe = new LocomocaoNe();			
			
			List<LocomocaoDt> listaLocomocaoDt = locomocaoNe.consultaLocomocoesProcessoDisponiveis(idProcesso);
			if( listaLocomocaoDt != null ) {
				request.setAttribute("quantidadeLocomocaoDisponivel", listaLocomocaoDt);
			}			
			
			ImportaDadosSPGNe importaDadosSPGNe = new ImportaDadosSPGNe();
			//ImportaDadosSPGDt importaDadosSPGDt = importaDadosSPGNe.consultaGuiaSaldoProjudi( idProcesso );
			ImportaDadosSPGDt importaDadosSPGDt = importaDadosSPGNe.consultaGuiaSaldoSPG( idProcesso );
			if( importaDadosSPGDt != null ) {
				if( importaDadosSPGDt.getGuiaSaldoValorAtualizado() != 0.0 ) {
					if (importaDadosSPGDt.getGuiaSaldoStatus().equalsIgnoreCase(ImportaDadosSPGDt.GUIA_SALDO_STATUS_NAO_LIBERADA))
					   guia_status = "Bloqueado"; 	
					request.setAttribute("saldoLocomocaoDisponivel", Funcoes.FormatarMoeda(String.valueOf(importaDadosSPGDt.getGuiaSaldoValorAtualizado())) + "       " + guia_status);
				}
			}
		}
	}
	

	/**
	 * Método para gerar guia de locomoção já vinculando ao mandado informado.
	 * 
	 * 
	 * @param FabricaConexao obFabricaConexao
	 * @param String idProcesso
	 * @param String idMandado
	 * @param List<BairroDt> listaBairroDt
	 * 
	 * @return boolean
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean gerarGuiaLocomocaoMandadoJudicial(FabricaConexao obFabricaConexao, String idProcesso, String idMandado, List<BairroDt> listaBairroDt) throws Exception {
		
		boolean retorno = false;
		//**************************************************************************
		//APAGAR 
//		//**************************************************************************	
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			obFabricaConexao.iniciarTransacao();
		//**************************************************************************
		//APAGAR 
		//**************************************************************************
			List listaItensGuia = null;
			GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
			List<GuiaItemDt> listaGuiaItemDt = new ArrayList<GuiaItemDt>();
			
			if( obFabricaConexao == null ) {
				throw new MensagemException("Erro ao identificar transação.(GerarGuiaLocomocaoMandadoJudicial)");
			}
			
			if( idProcesso == null ) {
				throw new MensagemException("Id do Processo não identificado.(GerarGuiaLocomocaoMandadoJudicial)");
			}
			
			if( idMandado == null ) {
				throw new MensagemException("Id do Mandado não identificado.(GerarGuiaLocomocaoMandadoJudicial)");
			}
			
			if( listaBairroDt == null || listaBairroDt.isEmpty() ) {
				throw new MensagemException("Lista de Bairros para a Locomoção está vazia.(GerarGuiaLocomocaoMandadoJudicial)");
			}
			
			ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
			
			if( processoDt != null && processoDt.getId() != null ) {
				
				guiaEmissaoDt.setNovoValorAcaoAtualizado(processoDt.getValor()); //Não atualiza o valor
				
				List<BairroGuiaLocomocaoDt> listaBairroGuiaLocomocaoDt = new ArrayList<BairroGuiaLocomocaoDt>();
				
				for( BairroDt bairroDt: listaBairroDt ) {
					
					BairroGuiaLocomocaoDt bairroGuiaLocomocaoDt = new BairroGuiaLocomocaoDt();
					bairroGuiaLocomocaoDt.setBairroDt(bairroDt);
					bairroGuiaLocomocaoDt.setId_Finalidade(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO));
					bairroGuiaLocomocaoDt.setFinalidade(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO));
					bairroGuiaLocomocaoDt.setIntimacao(false);
					bairroGuiaLocomocaoDt.setPenhora(false);
					bairroGuiaLocomocaoDt.setOficialCompanheiro(false);
					bairroGuiaLocomocaoDt.setQuantidade(1);
					
					this.calcularGuiaLocomocaoNOVO(listaGuiaItemDt, bairroGuiaLocomocaoDt, guiaEmissaoDt, processoDt.isSegundoGrau());
				}
				
				//Consulta modelo da guia
				GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(obFabricaConexao, GuiaTipoDt.ID_LOCOMOCAO, processoDt.getId_ProcessoTipo());
				guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
				//************************************************************
				
				//********* Se os itens foram calculados finaliza e gera a guia ***************
				if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0) {
					guiaEmissaoDt.setListaGuiaItemDt(listaGuiaItemDt);
					guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( this.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
					guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
					guiaEmissaoDt.setValorAcao(processoDt.getValor());
					guiaEmissaoDt.setId_Processo(processoDt.getId());
					guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
					guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
					guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
					guiaEmissaoDt.setId_Usuario(UsuarioDt.SistemaProjudi);
					guiaEmissaoDt.setIpComputadorLog(processoDt.getIpComputadorLog());
					guiaEmissaoDt.setId_GuiaStatus(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO));
					
					//IdMandado informado, então vincula com as locomoções
					for(GuiaItemDt guiaItemDt: listaGuiaItemDt) {
						if( guiaItemDt != null && guiaItemDt.getLocomocaoDt() != null ) {
							MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
							mandadoJudicialDt.setId(idMandado);
							
							guiaItemDt.getLocomocaoDt().setMandadoJudicialDt(mandadoJudicialDt);
						}
					}
					
					//Salvar a Guia
					this.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), false, UsuarioDt.SistemaProjudi, obFabricaConexao);
					
					if( guiaEmissaoDt != null && !guiaEmissaoDt.getId().isEmpty() ) {
						retorno = true;
					}
				}
			}
		//**************************************************************************
		//APAGAR 
		//**************************************************************************
//			obFabricaConexao.finalizarTransacao();
//		}
//		catch(Exception e) {
//			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
//			throw e;
//		}
//		finally {
//			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
//		}
		//**************************************************************************
		//APAGAR 
		//**************************************************************************
		
		return retorno;
	}
	
	/**
	 * Metodo para consultar o número do processo completo pelo id_proc.
	 * 
	 * @param String idProcesso
	 * @return String
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public String consultarNumeroCompletoDoProcesso(String idProcesso) throws Exception {
		String numeroCompletoProcesso = new ProcessoNe().consultarNumeroCompletoDoProcesso(idProcesso);
		if( numeroCompletoProcesso != null ) {
			numeroCompletoProcesso = numeroCompletoProcesso.replaceAll("-", ".");
		}
		return numeroCompletoProcesso;
	}
	
	/**
	 * Metodo para consultar o número do processo completo pelo id_proc.
	 * 
	 * @param String idProcesso
	 * @param FabricaConexao obFabricaConexao
	 * @return String
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public String consultarNumeroCompletoDoProcesso(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		String numeroCompletoProcesso = new ProcessoNe().consultarNumeroCompletoDoProcesso(idProcesso, obFabricaConexao);
		if( numeroCompletoProcesso != null ) {
			numeroCompletoProcesso = numeroCompletoProcesso.replaceAll("-", ".");
		}
		return numeroCompletoProcesso;
	}
	
	/**
	 * Método para verificar se o processo possui guia diferente da série 50.
	 * 
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	private boolean processoPossuiGuiaDiferenteSerie50(String idProcesso) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
			retorno = obPersistencia.processoPossuiGuiaDiferenteSerie50(idProcesso);		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para importar as guias do processo que estão no SPG e SSG.
	 * 
	 * @param ProcessoDt processoDt
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param UsuarioNe usuarioSessao
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public void importarGuiasSPG_SSG_ExecucaoAutomatica() throws Exception {
		PropriedadeNe propriedadeNe = new PropriedadeNe();
		
		PropriedadeDt propFlag = propriedadeNe.consultarCodigo(String.valueOf(ProjudiPropriedades.FLAG_IMPORTACAO_GUIAS_SPG));
		PropriedadeDt propIdProcesso = propriedadeNe.consultarCodigo(String.valueOf(ProjudiPropriedades.ID_PROCESSO_IMPORTACAO_GUIAS_SPG));
		
		if( propFlag.getValor().equals("ATIVO") ) {
			
			Long idProcesso = Funcoes.StringToLong(propIdProcesso.getValor());
			Long idProcessoMaximo = idProcesso + 200;
			
			for(int i = 0; i < idProcessoMaximo; i++) {
				
				ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(String.valueOf(idProcesso));
				
				if(processoDt != null && processoDt.getId() != null && !processoPossuiGuiaDiferenteSerie50(processoDt.getId())) {
					try {
						//Consultar e tentar Importar Guias do SPG
						List<GuiaEmissaoDt> listaGuiaEmissaoSPGDt = new GuiaSPGNe().consultarGuiaEmissaoSPG(processoDt, new ArrayList<GuiaEmissaoDt>());
						for(GuiaEmissaoDt guiaEmissaoDt: listaGuiaEmissaoSPGDt) {
							guiaEmissaoDt = this.consultarGuiaEmissaoSPG(guiaEmissaoDt.getNumeroGuiaCompleto());
							this.tenteSincronizeBaseProjudiSPG(processoDt, guiaEmissaoDt, null);
						}
					}
					catch (Exception ex) {}
					
					try {
						//Consultar e tentar Importar Guias do SSG
						List<GuiaEmissaoDt> listaGuiaEmissaoSSGDt = new GuiaSSGNe().consultarGuiaEmissaoSSG(processoDt, new ArrayList<GuiaEmissaoDt>());
						for(GuiaEmissaoDt guiaEmissaoDt: listaGuiaEmissaoSSGDt) {
							guiaEmissaoDt = this.consultarGuiaEmissaoSSG(guiaEmissaoDt.getNumeroGuiaCompleto());
							this.tenteSincronizeBaseProjudiSSG(processoDt, guiaEmissaoDt, null);
						}
					}
					catch (Exception ex) {}
				}
			}
			
			propIdProcesso.setValor(String.valueOf(idProcessoMaximo));
			propriedadeNe.salvar(propIdProcesso);
		}
	}
	
	/**
	 * Método que verifica se o id pertence a guia que foi parcelada.
	 * 
	 * @param String idGuiaEmissao
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isGuiaFonteParcelamento(String idGuiaEmissao, FabricaConexao obFabricaConexao) throws Exception {
		GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
		return obPersistencia.isGuiaFonteParcelamento(idGuiaEmissao);
	}
	
	/**
	 * Método que verifica se guia é do mesmo processo da guia principal.
	 * 
	 * @param String numeroGuiaCompleto
	 * @param String idProcesso
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isGuiaMesmoProcessoGuiaPrincipal(String numeroGuiaCompleto, String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			if( numeroGuiaCompleto != null && idProcesso != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				return obPersistencia.isGuiaMesmoProcessoGuiaPrincipal(numeroGuiaCompleto, idProcesso);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return false;
	}
	
	/**
	 * Método que verifica se a guia tem oficial vinculado.
	 * Oficial no campo CODIGO_OFICIAL_SPG do SPG (Maneira antiga)
	 * Oficial vinculado no mandado (Central de mandado nova)
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isGuiaOficialVinculadoSPG_Mandando(String idGuiaEmissao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			if( idGuiaEmissao != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());
				
				return obPersistencia.isGuiaOficialVinculadoSPG_Mandando(idGuiaEmissao);
			}
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return false;
	}
	
	/**
	 * ATENçÃO: Método TEMPORARIO
	 * Método para tentar identificar o problema que ocorre relatado na ocorrecia 2020/8294
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List<GuiaItemDt> listaGuiaItemDt
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	private boolean isGuiaGuiaInicialCartaPrecatoriaItemCorreto1041Precatorios(GuiaEmissaoDt guiaEmissaoDt, List<GuiaItemDt> listaGuiaItemDt) throws Exception {
		boolean retorno = true;
		
		if( guiaEmissaoDt != null 
			&& guiaEmissaoDt.getId_ProcessoTipo() != null
			&& guiaEmissaoDt.getId_ProcessoTipo().equals(ProcessoTipoDt.CARTA_PRECATORIA_CPC)
			&& guiaEmissaoDt.getGuiaModeloDt() != null 
			&& guiaEmissaoDt.getGuiaModeloDt().getId() != null 
			&& guiaEmissaoDt.getGuiaModeloDt().getId().equals("20262") ) { //id do modelo com que acontece o problema
			
			if( listaGuiaItemDt != null ) {
				for( GuiaItemDt guiaItemDt: listaGuiaItemDt ) {
					switch(Funcoes.StringToInt(guiaItemDt.getCustaDt().getId())) {
						
						//Se tiver algum desses itens está errado
						case CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO:
						case CustaDt.PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA:
						case CustaDt.PROCESSOS_CAUTELARES_SERAO_COBRADOS_40_DAS_CUSTAS:
						case CustaDt.AUTUACAO_E_OU_PROCESSAMENTO_DE_FEITOS:
							retorno = false;
						break;
					}
				}
			}
			
		}
		
		return retorno;
	}
	
	/**
	 * Valida soma do Rateio das partes, caso tenha sido escolhido
	 * 
	 * @param HttpServletRequest request
	 * @param GuiaEmissaoDt guiaEmissaoDt
	 * @param List listaGuiasRateio
	 * @param List listaTotalGuiaRateio
	 * @param List listaNomeParteGuia
	 * @param List listaNomePartePorcentagemGuia
	 * @param List listaPromoventes
	 * @param List listaPromovidos
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public void validarSomaRateioPartes(HttpServletRequest request, GuiaEmissaoDt guiaEmissaoDt, List listaGuiasRateio, List listaTotalGuiaRateio, List listaNomeParteGuia, List listaNomePartePorcentagemGuia, List listaPromoventes, List listaPromovidos) throws Exception {
		
		listaNomeParteGuia = new ArrayList();
		listaNomePartePorcentagemGuia = new ArrayList();
		
		//Valida soma do Rateio das partes, caso tenha sido escolhido
		BigDecimal total = new BigDecimal(0.0D);
		if( Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaFinalNe.RATEIO_VARIAVEL || Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaFinalNe.RATEIO_50_50 ) {
			
			if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
				for(int i = 0; i < listaPromoventes.size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
					
					//Pega o valor da porcentagem digitada para a parte
					String VARIAVEL_RATEIO_PARTE_VARIAVEL = request.getParameter(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()).toString();
					
					//Soma o valor digitado para validar os 100%
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						BigDecimal variavel = new BigDecimal(VARIAVEL_RATEIO_PARTE_VARIAVEL);
						
						total = total.add(variavel);
					}
					
					//Seta na variavel de sessão este valor
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId(), VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Verifica se emiti a guia para esta parte
					String chekboxEmitirGuiaParte[] = request.getParameterValues(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
					if( chekboxEmitirGuiaParte != null ) {
						//Seta se a parte irá gerar a guia
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId(), chekboxEmitirGuiaParte[0]);
						
						//Para a criação das outras guias
						listaNomeParteGuia.add(parteDt.getNome() + ":" + chekboxEmitirGuiaParte[0]);
						listaNomePartePorcentagemGuia.add(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
				}
			}
			if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
				for(int i = 0; i < listaPromovidos.size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					
					//Pega o valor da porcentagem digitada para a parte
					String VARIAVEL_RATEIO_PARTE_VARIAVEL = request.getParameter(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()).toString();
					
					//Soma o valor digitado para validar os 100%
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						BigDecimal variavel = new BigDecimal(VARIAVEL_RATEIO_PARTE_VARIAVEL);
						
						total = total.add(variavel);
					}
					
					//Seta na variavel de sessão este valor
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId(), VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Verifica se emiti a guia para esta parte
					String chekboxEmitirGuiaParte[] = request.getParameterValues(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
					if( chekboxEmitirGuiaParte != null ) {
						//Seta se a parte irá gerar a guia
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId(), chekboxEmitirGuiaParte[0]);
						
						//Para a criação das outras guias
						listaNomeParteGuia.add(parteDt.getNome() + ":" + chekboxEmitirGuiaParte[0]);
						listaNomePartePorcentagemGuia.add(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
				}
			}
			
			guiaEmissaoDt.setTotalRateio(total.doubleValue());
		}
		
	}


	
	/**
	 * Chamada para o método de consultar mandados.
	 * @param numeroMandado
	 * @param nomeOficial
	 * @param idProcesso
	 * @param posicao
	 * @return String
	 * @throws Exception
	 */
	public String consultarMandadosPagamentoAprovadoJson(String numeroMandado, String nomeOficial, String idProcesso, String posicao) throws Exception {
		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
		return mandadoJudicialNe.consultarMandadosPagamentoAprovadoJson(numeroMandado, nomeOficial, idProcesso, posicao);
	}
	
	/**
	 * Método para consultar o oficial de justiça PRINCIPAL no SPG pelo cpf do oficial vinculado no mandado do projudi.
	 * 
	 * @param boolean oficialPrincipal
	 * @param String idMandado
	 * @return OficialSPGDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public OficialSPGDt consultaOficialJusticaVinculadoMandado(boolean oficialPrincipal, String idMandado) throws Exception {
		OficialSPGDt oficialSPGDt = null;
		
		if(idMandado != null) {
			
			MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			UsuarioNe usuarioNe = new UsuarioNe();
			OficialSPGNe oficialSPGNe = new OficialSPGNe();
			
			MandadoJudicialDt mandadoJudicialDt = mandadoJudicialNe.consultarId(idMandado);
			if( mandadoJudicialDt != null ) {
				UsuarioServentiaDt usuarioServentiaDt_Mandado = null;
				
				if(oficialPrincipal) {
					usuarioServentiaDt_Mandado = mandadoJudicialDt.getUsuarioServentiaDt_1();
				}
				else {
					usuarioServentiaDt_Mandado = mandadoJudicialDt.getUsuarioServentiaDt_2();
				}
				
				if( usuarioServentiaDt_Mandado != null && !usuarioServentiaDt_Mandado.getId().isEmpty() ) {
					UsuarioServentiaDt usuarioServentiaDt = usuarioServentiaNe.consultarId(usuarioServentiaDt_Mandado.getId());
					if( usuarioServentiaDt != null ) {
						UsuarioDt usuarioDt = usuarioNe.consultarId(usuarioServentiaDt.getId_Usuario());
						if( usuarioDt != null && usuarioDt.getCpf() != null ) {
							oficialSPGDt = oficialSPGNe.consultaOficialCpf(usuarioDt.getCpf());
						}
					}
				}
			}
		}
		
		return oficialSPGDt;
	}
	
	
	/**
	 * Método que consulta as guias de locomoções com oficiais de justiça.
	 * 
	 * @param String idProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	private List<GuiaEmissaoDt> consultarGuiaLocomocaoComOficialVinculado(String idProcesso) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		List<GuiaEmissaoDt> listaGuiaEmissaoDt = null;
		
		try {
			if( idProcesso != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
				listaGuiaEmissaoDt = obPersistencia.consultarGuiaLocomocaoComOficialVinculado(idProcesso);
			}
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaEmissaoDt;
	}
	
	/**
	 * Método que consulta as guias de locomoções com oficiais de justiça e retira essas guias encontradas a lista inicial do processo.
	 * Feito este tratamento para apresentar duas tabelas na tela de guias do processo.
	 * 
	 * @param String idProcesso
	 * @param List<GuiaEmissaoDt> listaGuiasProcesso
	 * @return List<GuiaEmissaoDt>
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public List<GuiaEmissaoDt> consularGuiaLocomocaoVinculadaOficialTratamentoListaCompleta(String idProcesso, List<GuiaEmissaoDt> listaGuiasProcesso) throws Exception {
		List<GuiaEmissaoDt> listaGuiasLocomocaoComOficial = null;
		
		if( idProcesso != null ) {
			listaGuiasLocomocaoComOficial = this.consultarGuiaLocomocaoComOficialVinculado(idProcesso);
			
			if( listaGuiasLocomocaoComOficial != null && listaGuiasProcesso != null ) {
				for(GuiaEmissaoDt guiaLocomocao: listaGuiasLocomocaoComOficial) {
					if( guiaLocomocao.getId() != null ) {
						listaGuiasProcesso.removeIf(guia -> guia.getId().equals(guiaLocomocao.getId()));
					}
				}
			}
		}
		
		return listaGuiasLocomocaoComOficial;
	}
	
	/**
	 * Método para verificar se guia tem boleto emitido.
	 * 
	 * @param String idGuiaEmissao
	 * @return boolean
	 * @throws Exception
	 * @author fasoares
	 */
	public boolean isBoletoEmitido(String idGuiaEmissao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		boolean retorno = false;
		
		try {
			if( idGuiaEmissao != null ) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
				GuiaEmissaoPs obPersistencia = new GuiaEmissaoPs(obFabricaConexao.getConexao());			
				retorno = obPersistencia.isBoletoEmitido(idGuiaEmissao);
			}
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
}
