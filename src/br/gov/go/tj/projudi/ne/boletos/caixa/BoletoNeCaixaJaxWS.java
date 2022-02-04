package br.gov.go.tj.projudi.ne.boletos.caixa;

import java.math.BigDecimal;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;
import br.gov.caixa.jaxws.sibar.CONTROLENEGOCIALTYPE;
import br.gov.caixa.jaxws.sibar.HEADERBARRAMENTOTYPE;
import br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.ConsultaBoletoEntradaType;
import br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.ConsultaCobrancaBancariaBoleto;
import br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.ConsultaCobrancaBancariaBoleto_Service;
import br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.DADOSENTRADATYPE;
import br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.SERVICOENTRADANEGOCIALTYPE;
import br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.SERVICOSAIDANEGOCIALTYPE;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.BaixaBoletoEntradaType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.DadosEntradaType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.EnderecoType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.FichaCompensacaoType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.IncluiBoletoEntradaType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.IncluiBoletoSaidaType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.JurosMoraType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.ManutencaoCobrancaBancaria;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.ManutencaoCobrancaBancaria_Service;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.MensagensFichaCompensacaoType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.MensagensReciboPagadorType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.PagadorType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.PagamentoType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.PosVencimentoType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.ReciboPagadorType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.ServicoEntradaNegocialType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.ServicoSaidaNegocialType;
import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.TituloEntradaType;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ne.boletos.BoletoNe;
import br.gov.go.tj.projudi.ne.boletos.PagadorBoleto;
import br.gov.go.tj.projudi.ne.boletos.SituacaoBoleto;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class BoletoNeCaixaJaxWS extends BoletoNe {
	
	private final Logger LOGGER = Logger.getLogger(BoletoNeCaixaJaxWS.class);
	
	private final String USUARIO_SERVICO;
	private final int CODIGO_BENEFICIARIO;
	private final ConsultaCobrancaBancariaBoleto webserviceConsulta;
	private final ManutencaoCobrancaBancaria webServiceEscrita;
	
	public BoletoNeCaixaJaxWS() throws Exception {
		configuraAceitacaoCertificadoCaixa();
		
		ProjudiPropriedades prop = ProjudiPropriedades.getInstance();
		USUARIO_SERVICO = prop.getUsuarioCaixa();
		CODIGO_BENEFICIARIO = Integer.parseInt(prop.getBeneficiarioCaixa());
		
		URL url = new URL(prop.getUrlCaixa() + "ConsultaCobrancaBancaria/Boleto?wsdl");
		QName qName = new QName("http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto", "Consulta_Cobranca_Bancaria_Boleto");
		ConsultaCobrancaBancariaBoleto_Service localizadorConsulta = new ConsultaCobrancaBancariaBoleto_Service(url, qName);
		webserviceConsulta = localizadorConsulta.getConsultaCobrancaBancariaBoletoSOAP();
		
		url = new URL(prop.getUrlCaixa() + "ManutencaoCobrancaBancaria/Boleto/Externo?wsdl");
		qName = new QName("http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo", "manutencao_cobranca_bancaria");
		ManutencaoCobrancaBancaria_Service localizadorEscrita = new ManutencaoCobrancaBancaria_Service(url, qName);
		webServiceEscrita = localizadorEscrita.getManutencaoCobrancaBancariaSOAP();
		
		BindingProvider bindProv = (BindingProvider) webserviceConsulta;
		List<Handler> handlers = bindProv.getBinding().getHandlerChain();
		handlers.add(new ImprimeEnvelopesSoapEmDebug());
		bindProv.getBinding().setHandlerChain(handlers);
		
		bindProv = (BindingProvider) webServiceEscrita;
		handlers = bindProv.getBinding().getHandlerChain();
		handlers.add(new ImprimeEnvelopesSoapEmDebug());
		bindProv.getBinding().setHandlerChain(handlers);
	}
	
	private void configuraAceitacaoCertificadoCaixa() throws Exception {
		SSLContext contexto = SSLContext.getInstance("TLS");
		X509TrustManager[] trustManagers = new X509TrustManager[] { new CaixaTrustManager() };
		contexto.init(null, trustManagers, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(contexto.getSocketFactory());
	}
	
	private static final DateTimeFormatter FORMATTER_DATA_HORA_HEADER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private static final DateFormat FORMATTER_DATA_HASH_AUTENTICACAO = new SimpleDateFormat("ddMMyyyy");
	private static final String CNPJ_TJGO = "02292266000180";
	
	private HEADERBARRAMENTOTYPE constroiHeader(OperacaoSibar operacao, BoletoDt boleto) throws Exception {
		HEADERBARRAMENTOTYPE header = new HEADERBARRAMENTOTYPE();
		header.setUSUARIOSERVICO(USUARIO_SERVICO);
		header.setOPERACAO(operacao.toString());
		header.setSISTEMAORIGEM("SIGCB");
		header.setDATAHORA(FORMATTER_DATA_HORA_HEADER.format(LocalDateTime.now()));
		header.setVERSAO(operacao.getVersao());
		
		String autenticacao = Funcoes.completarZeros(Integer.toString(CODIGO_BENEFICIARIO), 7);
		autenticacao += Funcoes.completarZeros(boleto.getNossoNumero(), 17);
		switch (operacao) {
			case CONSULTA_BOLETO:
			case BAIXA_BOLETO:
				autenticacao += Funcoes.completarZeros("0", 8);
				autenticacao += Funcoes.completarZeros("0", 15);
				break;
			default:
				autenticacao += FORMATTER_DATA_HASH_AUTENTICACAO.format(Funcoes.StringToDate(boleto.getDataVencimentoBoleto()));
				autenticacao += Funcoes.completarZeros(Funcoes.obtenhaSomenteNumeros(boleto.getValorBoleto()), 15);
				break;
		}
		autenticacao += Funcoes.completarZeros(CNPJ_TJGO, 14);
		
		LOGGER.debug("\n\tHASH AUTENTICACAO = " + autenticacao);
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(autenticacao.getBytes("ISO8859-1"));
		header.setAUTENTICACAO(new BASE64Encoder().encode(hash));
		return header;
	}
	
	@Override
	public SituacaoBoleto consultaSituacao(BoletoDt boleto) throws Exception {
		SERVICOENTRADANEGOCIALTYPE requisicao = new SERVICOENTRADANEGOCIALTYPE();
		requisicao.setHEADER(constroiHeader(OperacaoSibar.CONSULTA_BOLETO, boleto));
		
		DADOSENTRADATYPE dados = new DADOSENTRADATYPE();
		ConsultaBoletoEntradaType operacao = new ConsultaBoletoEntradaType();
		operacao.setCODIGOBENEFICIARIO(CODIGO_BENEFICIARIO);
		operacao.setNOSSONUMERO(Long.parseLong(boleto.getNossoNumero()));
		dados.setCONSULTABOLETO(operacao);
		requisicao.setDADOS(dados);
		
		SERVICOSAIDANEGOCIALTYPE resposta = webserviceConsulta.consultaBOLETO(requisicao);
		RetornoSibar retornoSibar = RetornoSibar.comCodigo(resposta.getCODRETORNO());
		switch (retornoSibar) {
			case OK:
				CONTROLENEGOCIALTYPE controle = resposta.getDADOS().getCONTROLENEGOCIAL().get(0);
				RetornoSigcb retornoSigcb = RetornoSigcb.comCodigo(controle.getCODRETORNO());
				switch (retornoSigcb) {
					case EFETUADA:
						return SituacaoBoleto.comTexto(controle.getMENSAGENS().getRETORNO());
					case CODIGO_DESCONHECIDO:
						throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSibar.getTexto() + "(" + controle.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
					case NAO_EFETUADA:
					case ERRO_INTERNO:
					default:
						throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSigcb.getTexto() + ": " + controle.getMENSAGENS().getRETORNO());
				}
			case CODIGO_DESCONHECIDO:
				throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + "(" + resposta.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
			case INVALIDO:
			case ERRO:
			default:
				throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + ": " + resposta.getMSGRETORNO());
		}
	}
	
	private static final short TIPO_ESPECIE_OUTROS = 99;
	private static final String FLAG_ACEITE_NAO = "N";
	private static final String TIPO_JURO_ISENTO = "ISENTO";
	private static final String ACAO_APOS_VENCIMENTO_DEVOLVER = "DEVOLVER";
	private static final short CODIGO_MOEDA_REAL = 9;
	
	@Override
	public void trasmiteBoleto(BoletoDt boleto) throws Exception {
		ServicoEntradaNegocialType requisicao = new ServicoEntradaNegocialType();
		
		requisicao.setHEADER(constroiHeader(OperacaoSibar.INCLUI_BOLETO, boleto));
		
		DadosEntradaType dadosRequisicao = new DadosEntradaType();
		TituloEntradaType titulo = new TituloEntradaType();
		titulo.setNOSSONUMERO(Long.parseLong(boleto.getNossoNumero()));
		titulo.setNUMERODOCUMENTO(boleto.getNumeroDocumento());
		titulo.setDATAVENCIMENTO(getDate(Funcoes.StringToDate(boleto.getDataVencimentoBoleto())));
		titulo.setVALOR(new BigDecimal(boleto.getValorBoleto()));
		titulo.setTIPOESPECIE(TIPO_ESPECIE_OUTROS);
		titulo.setFLAGACEITE(FLAG_ACEITE_NAO);
		titulo.setDATAEMISSAO(getDate(Funcoes.StringToDate(boleto.getDataEmissaoBoleto())));
		JurosMoraType juros = new JurosMoraType();
		juros.setTIPO(TIPO_JURO_ISENTO);
		juros.setVALOR(BigDecimal.ZERO);
		titulo.setJUROSMORA(juros);
		PosVencimentoType posVencimento = new PosVencimentoType();
		posVencimento.setACAO(ACAO_APOS_VENCIMENTO_DEVOLVER);
//		posVencimento.setNUMERODIAS((short) 1);
		if (boleto.isSerie10())
	        posVencimento.setNUMERODIAS((short) 720);
		else
	        posVencimento.setNUMERODIAS((short) 1);
		titulo.setPOSVENCIMENTO(posVencimento);
		titulo.setCODIGOMOEDA(CODIGO_MOEDA_REAL);
		PagadorType pagador = new PagadorType();
		PagadorBoleto pagadorBoleto = boleto.getPagador();
		if (pagadorBoleto.getCpf() != null && !pagadorBoleto.getCpf().isEmpty()) {
			pagador.setCPF(Long.parseLong(pagadorBoleto.getCpf()));
			pagador.setNOME(pagadorBoleto.getNome().trim().toUpperCase());
		}
		if (pagadorBoleto.getCnpj() != null && !pagadorBoleto.getCnpj().isEmpty()) {
			pagador.setCNPJ(Long.parseLong(pagadorBoleto.getCnpj()));
			pagador.setRAZAOSOCIAL(pagadorBoleto.getRazaoSocial().trim().toUpperCase());
		}
		EnderecoType endereco = new EnderecoType();
		endereco.setLOGRADOURO(pagadorBoleto.getLogradouro());
		endereco.setBAIRRO(pagadorBoleto.getBairro());
		endereco.setCIDADE(pagadorBoleto.getCidade());
		endereco.setUF(pagadorBoleto.getUf());
		if (pagadorBoleto.getCep() != null && !pagadorBoleto.getCep().isEmpty())
			endereco.setCEP(Integer.parseInt(pagadorBoleto.getCep()));
		pagador.setENDERECO(endereco);
		titulo.setPAGADOR(pagador);
		FichaCompensacaoType ficha = new FichaCompensacaoType();
		
		MensagensFichaCompensacaoType mensagens = new MensagensFichaCompensacaoType();
		mensagens.getMENSAGEM().add("NÃO RECEBER EM CHEQUE");
		ficha.setMENSAGENS(mensagens);
		titulo.setFICHACOMPENSACAO(ficha);
		ReciboPagadorType recibo = new ReciboPagadorType();
		MensagensReciboPagadorType mensagens2 = new MensagensReciboPagadorType();
		
		if (!boleto.isObservacao1Informada()) {	
			boleto.setObservacao1("Consulte os ítens da cobrança em");
		}
		if (!boleto.isObservacao2Informada()) {
			boleto.setObservacao2("https://projudi.tjgo.jus.br/GerarBoleto");
		}
		if (!boleto.isObservacao3Informada()) {			
			String parcela = "";		
			if (Funcoes.StringToInt(boleto.getParcelaAtual()) > 1)
			{
				parcela += " (";
				parcela += Funcoes.StringToInt(boleto.getParcelaAtual());
				parcela += "/";
				parcela += Funcoes.StringToInt(boleto.getQuantidadeParcelas());
				parcela += ")";
			}
			boleto.setObservacao3("e informe a guia" + parcela + " N. " + Funcoes.FormatarNumeroSerieGuia(boleto.getNumeroGuiaCompleto()));
		}
		
		if (!boleto.isObservacao4Informada()) {			
			if (boleto.getNumeroCompletoProcesso() != null && boleto.getNumeroCompletoProcesso().trim().length() > 0) {
				boleto.setObservacao4("Processo N. " + boleto.getNumeroCompletoProcesso());				
			} else if (boleto.isSerie10()) {
				boleto.setObservacao4("Vencimento Original - " + Funcoes.dateToStringSoData(Funcoes.Stringyyyy_MM_ddToDateTime(boleto.getDataVencimento())));				
			} else {
				boleto.setObservacao4("Sem vinculo com Processo");				
			}			
		} 
		//mensagens2.getMENSAGEM().add("PAGAMENTO AO FUNDESP CNPJ 02050330000117");
		//mensagens2.getMENSAGEM().add("Referente à 15. parcela (dezembro 2018)");
		//mensagens2.getMENSAGEM().add("Licitação 022/2017, Lote I");		
		mensagens2.getMENSAGEM().add(Funcoes.obtenhaPrimeirosCarctereString(Funcoes.retirarAcentos(boleto.getObservacao1()), 40));
		mensagens2.getMENSAGEM().add(Funcoes.obtenhaPrimeirosCarctereString(Funcoes.retirarAcentos(boleto.getObservacao2()), 40));
		mensagens2.getMENSAGEM().add(Funcoes.obtenhaPrimeirosCarctereString(Funcoes.retirarAcentos(boleto.getObservacao3()), 40));
		mensagens2.getMENSAGEM().add(Funcoes.obtenhaPrimeirosCarctereString(Funcoes.retirarAcentos(boleto.getObservacao4()), 40));
		
		recibo.setMENSAGENS(mensagens2);
		titulo.setRECIBOPAGADOR(recibo);
		
		PagamentoType pagamento = new PagamentoType();
		pagamento.setQUANTIDADEPERMITIDA((short) 1);
		pagamento.setTIPO("NAO_ACEITA_VALOR_DIVERGENTE");
		pagamento.setVALORMINIMO(BigDecimal.ZERO);
		pagamento.setVALORMAXIMO(BigDecimal.ZERO);
		titulo.setPAGAMENTO(pagamento);
		
		IncluiBoletoEntradaType operacao = new IncluiBoletoEntradaType();
		operacao.setCODIGOBENEFICIARIO(CODIGO_BENEFICIARIO);
		operacao.setTITULO(titulo);
		dadosRequisicao.setINCLUIBOLETO(operacao);
		
		requisicao.setDADOS(dadosRequisicao);
		ServicoSaidaNegocialType resposta = webServiceEscrita.incluiBOLETO(requisicao);
		RetornoSibar retornoSibar = RetornoSibar.comCodigo(resposta.getCODRETORNO());
		switch (retornoSibar) {
			case OK:
				CONTROLENEGOCIALTYPE controle = resposta.getDADOS().getCONTROLENEGOCIAL().get(0);
				RetornoSigcb retornoSigcb = RetornoSigcb.comCodigo(controle.getCODRETORNO());
				switch (retornoSigcb) {
					case EFETUADA:
						IncluiBoletoSaidaType boletoIncluido = resposta.getDADOS().getINCLUIBOLETO();
						boleto.setCodigoDeBarras(boletoIncluido.getCODIGOBARRAS());
						boleto.setLinhaDigitavel(boletoIncluido.getLINHADIGITAVEL());
						boleto.setUrlPdf(boletoIncluido.getURL());
						if (controle.getMENSAGENS().getRETORNO().equalsIgnoreCase("(0) OPERACAO EFETUADA - SITUACAO DO TITULO = BAIXADO POR DEVOLUCAO")) {
							reemiteBoleto(boleto);														
						}						
						return;
					case CODIGO_DESCONHECIDO:
						throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSibar.getTexto() + "(" + controle.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
					case NAO_EFETUADA:
						if (controle.getMENSAGENS().getRETORNO().equalsIgnoreCase("(38) NOSSO NUMERO JA CADASTRADO PARA O BENEFICIARIO")) {
							atualizaInformacoesBoletoTransmitido(boleto);
							return;
						} 
					case ERRO_INTERNO:
					default:
						throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSigcb.getTexto() + ": " + controle.getMENSAGENS().getRETORNO());
				}
			case CODIGO_DESCONHECIDO:
				throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + "(" + resposta.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
			case INVALIDO:
			case ERRO:
			default:
				throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + ": " + resposta.getMSGRETORNO());
		}
	}
	
	@Override
	public void transmiteCancelamento(BoletoDt boleto) throws Exception {
		ServicoEntradaNegocialType requisicao = new ServicoEntradaNegocialType();
		
		requisicao.setHEADER(constroiHeader(OperacaoSibar.BAIXA_BOLETO, boleto));
		
		DadosEntradaType dadosRequisicao = new DadosEntradaType();
		BaixaBoletoEntradaType operacao = new BaixaBoletoEntradaType();
		operacao.setCODIGOBENEFICIARIO(CODIGO_BENEFICIARIO);
		operacao.setNOSSONUMERO(Long.parseLong(boleto.getNossoNumero()));
		dadosRequisicao.setBAIXABOLETO(operacao);
		
		requisicao.setDADOS(dadosRequisicao);
		
		ServicoSaidaNegocialType resposta = webServiceEscrita.baixaBOLETO(requisicao);
		RetornoSibar retornoSibar = RetornoSibar.comCodigo(resposta.getCODRETORNO());
		switch (retornoSibar) {
			case OK:
				CONTROLENEGOCIALTYPE controle = resposta.getDADOS().getCONTROLENEGOCIAL().get(0);
				RetornoSigcb retornoSigcb = RetornoSigcb.comCodigo(controle.getCODRETORNO());
				switch (retornoSigcb) {
					case EFETUADA:
						return;
					case CODIGO_DESCONHECIDO:
						throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSibar.getTexto() + "(" + controle.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
					case NAO_EFETUADA:
					case ERRO_INTERNO:
					default:
						throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSigcb.getTexto() + ": " + controle.getMENSAGENS().getRETORNO());
				}
			case CODIGO_DESCONHECIDO:
				throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + "(" + resposta.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
			case INVALIDO:
			case ERRO:
			default:
				throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + ": " + resposta.getMSGRETORNO());
		}
	}
	
	@Override
	public void transmiteAlteracao(BoletoDt boleto) throws Exception {
		throw new MensagemException("FUNCIONALIDADE NÃO DISPONÍVEL");
	}
	
	private static final XMLGregorianCalendar getDate(Date date) {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
		} catch (DatatypeConfigurationException e) {
			return null;
		}
	}
	
	private static final Date getDate(XMLGregorianCalendar date) {
		TJDataHora data = new TJDataHora();
		if (date != null) {
			data.setData(date.getYear(), date.getMonth(), date.getDay());	
		}
		return data.getDate();
	}
	
	public byte[] geraPDF(BoletoDt boleto) throws Exception {
//		URL url = new URL(boleto.getUrlPdf());
//		byte[] array = null; //request.getEncoded();
//		HttpURLConnection con = (HttpURLConnection) url.openConnection();
//		con.setRequestProperty("Content-Type", "application/ocsp-request");
//		con.setRequestProperty("Accept", "application/ocsp-response");
//		con.setDoOutput(true);
//		OutputStream out = con.getOutputStream();
//		DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
//		dataOut.write(array);
//		dataOut.flush();
//		dataOut.close();
//		if (con.getResponseCode() != HttpStatus.SC_OK) {
//			throw new MensagemException("ERRO HTTP " + con.getResponseCode() + " AO CONSULTAR BOLETO NA CAIXA em " + url);
//		}
//		return array;
		
		return null;
	}

	@Override
	public String consultaUrlPdf(BoletoDt boleto) throws Exception {
		SERVICOENTRADANEGOCIALTYPE requisicao = new SERVICOENTRADANEGOCIALTYPE();
		requisicao.setHEADER(constroiHeader(OperacaoSibar.CONSULTA_BOLETO, boleto));
		
		DADOSENTRADATYPE dados = new DADOSENTRADATYPE();
		ConsultaBoletoEntradaType operacao = new ConsultaBoletoEntradaType();
		operacao.setCODIGOBENEFICIARIO(CODIGO_BENEFICIARIO);
		operacao.setNOSSONUMERO(Long.parseLong(boleto.getNossoNumero()));
		dados.setCONSULTABOLETO(operacao);
		requisicao.setDADOS(dados);
		
		SERVICOSAIDANEGOCIALTYPE resposta = webserviceConsulta.consultaBOLETO(requisicao);		
		RetornoSibar retornoSibar = RetornoSibar.comCodigo(resposta.getCODRETORNO());
		switch (retornoSibar) {
		case OK:
			CONTROLENEGOCIALTYPE controle = resposta.getDADOS().getCONTROLENEGOCIAL().get(0);
			RetornoSigcb retornoSigcb = RetornoSigcb.comCodigo(controle.getCODRETORNO());
			switch (retornoSigcb) {
				case EFETUADA:
					if (resposta.getDADOS().getCONSULTABOLETO() != null &&
					    resposta.getDADOS().getCONSULTABOLETO().getTITULO() != null &&
					    resposta.getDADOS().getCONSULTABOLETO().getTITULO().getURL() != null &&
					    resposta.getDADOS().getCONSULTABOLETO().getTITULO().getURL().trim().length() > 0) {
						return resposta.getDADOS().getCONSULTABOLETO().getTITULO().getURL();
					}
					if (resposta.getDADOS().getCONTROLENEGOCIAL() != null &&
						resposta.getDADOS().getCONTROLENEGOCIAL().size() > 0 &&
						resposta.getDADOS().getCONTROLENEGOCIAL().get(0) != null &&
						resposta.getDADOS().getCONTROLENEGOCIAL().get(0).getMENSAGENS() != null &&
						resposta.getDADOS().getCONTROLENEGOCIAL().get(0).getMENSAGENS().getRETORNO() != null &&
						resposta.getDADOS().getCONTROLENEGOCIAL().get(0).getMENSAGENS().getRETORNO().trim().length() > 0) {
						throw new MensagemException(resposta.getDADOS().getCONTROLENEGOCIAL().get(0).getMENSAGENS().getRETORNO().trim());
					}
					return "";
				case CODIGO_DESCONHECIDO:
					throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSibar.getTexto() + "(" + controle.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
				case NAO_EFETUADA:
				case ERRO_INTERNO:
				default:
					throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSigcb.getTexto() + ": " + controle.getMENSAGENS().getRETORNO());
			}
		case CODIGO_DESCONHECIDO:
			throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + "(" + resposta.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
		case INVALIDO:
		case ERRO:
		default:
			throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + ": " + resposta.getMSGRETORNO());
		}
	}
	
	@Override
	public void atualizaInformacoesBoletoTransmitido(BoletoDt boleto) throws Exception {
		SERVICOENTRADANEGOCIALTYPE requisicao = new SERVICOENTRADANEGOCIALTYPE();
		requisicao.setHEADER(constroiHeader(OperacaoSibar.CONSULTA_BOLETO, boleto));
		
		DADOSENTRADATYPE dados = new DADOSENTRADATYPE();
		ConsultaBoletoEntradaType operacao = new ConsultaBoletoEntradaType();
		operacao.setCODIGOBENEFICIARIO(CODIGO_BENEFICIARIO);
		operacao.setNOSSONUMERO(Long.parseLong(boleto.getNossoNumero()));
		dados.setCONSULTABOLETO(operacao);
		requisicao.setDADOS(dados);
		
		SERVICOSAIDANEGOCIALTYPE resposta = webserviceConsulta.consultaBOLETO(requisicao);		
		RetornoSibar retornoSibar = RetornoSibar.comCodigo(resposta.getCODRETORNO());
		switch (retornoSibar) {
		case OK:
			CONTROLENEGOCIALTYPE controle = resposta.getDADOS().getCONTROLENEGOCIAL().get(0);
			RetornoSigcb retornoSigcb = RetornoSigcb.comCodigo(controle.getCODRETORNO());
			switch (retornoSigcb) {
				case EFETUADA:
					if (resposta.getDADOS().getCONSULTABOLETO() != null) {
						if (resposta.getDADOS().getCONSULTABOLETO().getTITULO() != null) {
							boleto.setCodigoDeBarras(resposta.getDADOS().getCONSULTABOLETO().getTITULO().getCODIGOBARRAS());
							boleto.setLinhaDigitavel(resposta.getDADOS().getCONSULTABOLETO().getTITULO().getLINHADIGITAVEL());
							boleto.setUrlPdf(resposta.getDADOS().getCONSULTABOLETO().getTITULO().getURL());
							boleto.setDataVencimentoBoleto(Funcoes.dateToStringSoData(getDate(resposta.getDADOS().getCONSULTABOLETO().getTITULO().getDATAVENCIMENTO())));
							//boleto.setSituacaoBoleto(resposta.getDADOS().getCONSULTABOLETO().getTITULO().getsi);
							if (controle.getMENSAGENS().getRETORNO().equalsIgnoreCase("(0) OPERACAO EFETUADA - SITUACAO DO TITULO = BAIXADO POR DEVOLUCAO")) {
								boleto.setSituacaoBoleto(SituacaoBoleto.BAIXA_POR_DEVOLUCAO);
								reemiteBoleto(boleto);														
							}							
							return;
						}	
					}
					break;
				case CODIGO_DESCONHECIDO:
					throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSibar.getTexto() + "(" + controle.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
				case NAO_EFETUADA:
				case ERRO_INTERNO:
				default:
					throw new MensagemException("SERVIÇO CAIXA (SIGCB) - " + retornoSigcb.getTexto() + ": " + controle.getMENSAGENS().getRETORNO());
			}
		case CODIGO_DESCONHECIDO:
			throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + "(" + resposta.getCODRETORNO() + "): " + resposta.getMSGRETORNO());
		case INVALIDO:
		case ERRO:
		default:
			throw new MensagemException("SERVIÇO CAIXA (SIBAR) - " + retornoSibar.getTexto() + ": " + resposta.getMSGRETORNO());
		}
	}
}
