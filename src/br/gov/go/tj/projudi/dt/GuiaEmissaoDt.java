package br.gov.go.tj.projudi.dt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.json.JSONException;

import br.gov.go.tj.projudi.ne.GuiaCalculoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaLocomocaoNe;
import br.gov.go.tj.projudi.ne.GuiaRecursoInominadoNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

public class GuiaEmissaoDt extends GuiaEmissaoDtGen {

	private static final long serialVersionUID = 8787396926242316681L;
	
	public static final int CodigoPermissao = 646;
	
	public static final Integer REQUERENTE 	= 1;
	public static final Integer REQUERIDO 	= 0;
	
	public static final String SIM = "S";
	public static final String NAO = "N";
	
	public static final String VALOR_SIM = "1";
	public static final String VALOR_NAO = "0";
	
	public static final String TIPO_GUIA_PARCELADA 		= "1";
	public static final String TIPO_GUIA_COM_DESCONTO 	= "2";
	
	private GuiaModeloDt guiaModeloDt;
	private GuiaFinalidadeModeloDt guiaFinalidadeModeloDt;
	private ProcessoDt processoDt;
	private String Id_GuiaFinalidadeModelo;
	private String Id_Comarca;
	private String Id_AreaQueixaCrime;
	private String Id_AreaDistribuicao;
	private String Id_GuiaStatus;
	private String Id_GuiaEmissaoPrincipal;
	private String Id_ProcessoPrioridade;
	private String Id_Processo;
	private String GuiaStatus;
	private String Comarca;
	private String ComarcaCodigo;
	private String ValorAcao;
	private String NovoValorAcao;
	private String NovoValorAcaoAtualizado;
	private String ValorCondenacao;
	private String NovoValorCondenacao;
	private String valorCausaCoringa;
	private String Requerente;
	private String Requerido;
	private String numeroProcesso;
	private String numeroGuia;
	private String numeroGuiaSerie;
	private String numeroGuiaCompleto;
	private String numeroGuiaPrincipalCompleto;
	private String numeroProcessoDependente;
	private String AreaQueixaCrime;
	private String ProcessoTipoCodigo;
	private String DataVencimento;
	private String DataRecebimento;
	private String DataEmissao;
	private String DataCancelamento;
	private String DataEmissaoBoleto;
	private String DataBaseAtualizacao;
	private String DataBaseFinalAtualizacao;
	private String Id_Usuario;
	private String Usuario;
	private String NumeroDUAM;
	private String QuantidadeParcelasDUAM;
	private String DataVencimentoDUAM;
	private String ValorImpostoMunicipalDUAM;
	private String QuantidadeAcrescimo;
	private String HonorariosQuantidade;
	private String AvaliadorQuantidade;
	private String AvaliadorValor;
	private String HonorariosValor;
	private String AtosEscrivaesCivel;
	private String PorcentagemEscrivaesCivel;
	private String ParcelasQuantidade;
	private String PenhoraQuantidade;
	private String PenhoraValor;
	private String PenhoraComarca;
	private String IntimacaoLocomocao;
	private String PartidorQuantidade;
	private String PartidorValor;
	private String LeilaoQuantidade;
	private String LeilaoValor;
	private String PorteRemessaQuantidade;
	private String PorteRemessaValorManual;
	private String TaxaJudiciariaServicoCartaArrematacao;
	private String TaxaJudiciariaServicoCertidao;
	private String RemessaStjQuantidade;
	private String DescontoTaxaJudiciaria;
	private String CodigoOficialSPGAvaliador;
	private String CodigoOficialSPGPenhora;
	private String CodigoOficialSPGPartidor;
	private String CodigoOficialSPGLocomocao;
	private String CodigoOficialSPGLocomocaoContaVinculada;
	private String CodigoOficialSPGLeilao;
	private String DividaAtivaQuantidade;
	private String EscrivaniaQuantidade;
	private String ContadorQuantidade;
	private String ContadorQuantidadeAcrescimo;
	private String CustasQuantidade;
	private String CustasValorManual;
	private String TaxaProtocoloQuantidade;
	private String TaxaServicoDividaAtivaQuantidade;
	private String RetificacaoCustasQuantidade;
	private String RetificacaoCalculosQuantidade;
	private String AtualizacaoValorNominalQuantidade;
	private String TransformacaoMoedaQuantidade;
	private String CorreioQuantidade;
	private String RateioQuantidade;
	private String FormalPartilhaCartaQuantidade;
	private String FormalPartilhaCartaQuantidade16IX;
	private String DepositarioPublico;
	private String DepositarioPublicoDataInicial;
	private String DepositarioPublicoDataFinal;
	private String DepositarioPublicoBemImovel;
	private String DepositarioPublicoBemImovelDataInicial;
	private String DepositarioPublicoBemImovelDataFinal;
	private String PregaoPorteiro;
	private String AfixacaoEdital;
	private String Apelante; //ou Recorrente
	private String Apelado; //ou Recorrido
	private String Id_Apelante;
	private String Id_Apelado;
	private String Id_Finalidade;
	private String Finalidade = "";
	private String RateioCodigo = "";
	private String ProcessoParteTipoCodigo;
	private String AtoEscrivao;
	private String AtoEscrivaoCivel;
	private String DistribuidorQuantidade;
	private String Id_ProcessoParteResponsavelGuia;
	private String NomeProcessoParteResponsavelGuia;
	private String AreaDistribuicao;
	private Double totalRateio = 0.0D;
	private String codigoGrau;
	private String codigoArea;
	private String NumeroImpetrantes;
	private String NaturezaSPGCodigo;
	private String NaturezaSPG;
	private String Id_NaturezaSPG;
	private String origemEstado;
	private String protocoloIntegrado;
	private String infoLocalCertidaoSPG;
	private String valorMediacao;
	private String valorConciliacao;
	private String documentoPublicadoQuantidade;
	private String documentoDiarioJustica;
	private String certidaoAcordao;
	private String desarquivamento;
	private String desarquivamento16II;
	private String restauracao;
	private String restauracaoAtos;
	private String correioQuantidadeReg4VI;
	private String emissaoDocumentoQuantidade;
	private String emissaoDocumentoQuantidade16VII;
	private String atosConstricao;
	private String atosConstricao16VIII;
	private String certidaoDecisao;
	private String PorteRemessaQuantidade16V;
	private String cumprimentoPrecatoria;
	
	//************
	//Dados parcelamento e/ou desconto (autor Fred)
	private String idGuiaReferenciaDescontoParcelamento;
	private String tipoGuiaReferenciaDescontoParcelamento;
	private String quantidadeParcelas;
	private String parcelaAtual;
	private String porcentagemDesconto;
	
	private String numeroGuiaReferenciaDescontoParcelamento;
	private GuiaEmissaoDt guiaEmissaoDtReferencia;
	private GuiaEmissaoDt guiaEmissaoDtPrincipal;
	//************
	
	private List<GuiaItemDt> listaGuiaItemDt;
	private List listaRequerentes;
	private List listaRequeridos;
	private List listaPartesLitisconsorte;
	private List listaOutrasPartes;
	private List listaAdvogados;
	
	private List listaAssuntos;
	
	//********************************
	//Variáveis utilizada para auxiliar na lista de itens que entra no cálculo - Início
	private String bensPartilhar;
	private String qtdeImpetrantes;
	private String natureza;
	private String codigoNatureza;
	private String citacaoUrbana;
	private String liquidacaoArtigoArbitramento;
	private String regimento33ExecucaoRecaiBens; //Execução recai sobre bens que devam ser penhorados, avaliados e alienados
	private String bensPenhoradosAvaliadosAlienados;
	private String tipoFalencia;
	private String InventarioArrolamentoSobrepartilha;
	
	private boolean penhora;
	private boolean intimacao;
	private boolean oficialCompanheiro;
	private boolean foraHorarioNormal;
	private boolean citacaoHoraCerta;
	private boolean locomocaoComplementar;
	private List<LocomocaoDt> listaLocomocaoNaoUtilizadaDt;
	//Variáveis utilizada para auxiliar na lista de itens que entra no cálculo - Fim
	//********************************
	
	private String ValorRecebimento;
	private String DataMovimento;
	private String DataRepasse;
	private String percRepasse;
	
	private boolean isGuiaEnviadaCadin;
	private boolean isProcessoPossuiGuiaEnviadaCadin;
	
	private boolean guiaEmitidaSPG;
	private boolean guiaEmitidaSSG;
	private String NumeroProcessoSPG;
	private String SituacaoPagamentoSPG;
	private boolean guiaEmitidaSPGCapital;
	private String dataApresentacaoSPG;
	private String tipoPessoaSPG;
	private String infoPatenteSPG;
	private String tipoGuiaCertidaoSPG;
	
	private String codigoOabSPG;
	private String dataIniCertidaoSPG;
	private String dataFimCertidaoSPG;
	
	//Variáveis utilizadas na obtenção de dados do SPG para utilização na Certidão Narrativa
	private String nomePrimeiroAutor;
	private String numrCpfCgc;
	private String tipoGuia;
	private String comarcaCodigo;
	private String serventiaCodigo;
	private String processoTipoCodNatureza;
	private String custaCertidao;
	private String custaTaxaJudiciaria;
	private String custaTotal;
	private String modoEmissaoCertidaoSPG;
	private String infoAreaSPG;
			
	// Variável utilizada para receber os metadados das guias de certidões
	private String metadadosGuia;
	
	//Variável criada para caso queira adicionar alguma informação na guia durante sua emissão que seja trivial
	//Caso esta variável esteja preenchida ela irá aparecer no método getPropriedades() para o log.
	private String dadosAdicionaisParaLog = "";
	
	private String numeroCompletoProcesso;
	
	private boolean grauGuiaInicialMesmoGrauProcessoDependente=false;

	/**
	 * Método para copiar os dados.
	 * @param GuiaEmissaoDt objeto
	 */
	public void copiar(GuiaEmissaoDt objeto) {
		super.copiar(objeto);
		if( objeto != null ) {
			this.setId_GuiaModelo(objeto.getId_GuiaModelo());
			this.setGuiaModelo(objeto.getGuiaModelo());
			Id_Processo = objeto.getId_Processo();
			Id_GuiaFinalidadeModelo = objeto.getId_GuiaFinalidadeModelo();
			this.setNumeroProcessoDependente(objeto.getNumeroProcessoDependente());
			this.setId_ProcessoTipo(objeto.getId_ProcessoTipo());
			this.setProcessoTipo(objeto.getProcessoTipo());
			Id_GuiaEmissaoPrincipal = objeto.getId_GuiaEmissaoPrincipal();
			this.setId_Serventia(objeto.getId_Serventia());
			this.setServentia(objeto.getServentia());
			Id_Comarca = objeto.getId_Comarca();
			Comarca = objeto.getComarca();
			this.setId_GuiaTipo(objeto.getId_GuiaTipo());
			this.setGuiaTipo(objeto.getGuiaTipo());
			Id_GuiaStatus = objeto.getId_GuiaStatus();
			GuiaStatus = objeto.getGuiaStatus();
			Id_AreaDistribuicao = objeto.getId_AreaDistribuicao();
			Id_ProcessoPrioridade = objeto.getId_ProcessoPrioridade();
			Id_Usuario = objeto.getId_Usuario();
			DataEmissao = objeto.getDataEmissao();
			DataRecebimento = objeto.getDataRecebimento();
			DataVencimento = objeto.getDataVencimento();
			Requerente = objeto.getRequerente();
			Requerido = objeto.getRequerido();
			ValorAcao = objeto.getValorAcao();
			NumeroDUAM = objeto.getNumeroDUAM();
			QuantidadeParcelasDUAM = objeto.getQuantidadeParcelasDUAM();
			DataVencimentoDUAM = objeto.getDataVencimentoDUAM();
			ValorImpostoMunicipalDUAM = objeto.getValorImpostoMunicipalDUAM();
			guiaModeloDt = objeto.getGuiaModeloDt();
			guiaFinalidadeModeloDt = objeto.getGuiaFinalidadeModeloDt();
			Id_Comarca = objeto.getId_Comarca();
			Id_AreaQueixaCrime = objeto.getId_AreaQueixaCrime();
			Id_AreaDistribuicao = objeto.getId_AreaDistribuicao();
			Id_GuiaStatus = objeto.getId_GuiaStatus();
			Id_GuiaEmissaoPrincipal = objeto.getId_GuiaEmissaoPrincipal();
			Id_ProcessoPrioridade = objeto.getId_ProcessoPrioridade();
			Id_Processo = objeto.getId_Processo();
			GuiaStatus = objeto.getGuiaStatus();
			ComarcaCodigo = objeto.getComarcaCodigo();
			ValorAcao = objeto.getValorAcao();
			NovoValorAcao = objeto.getNovoValorAcao();
			NovoValorAcaoAtualizado = objeto.getNovoValorAcaoAtualizado();
			Requerente = objeto.getRequerente();
			Requerido = objeto.getRequerido();
			numeroProcesso = objeto.getNumeroProcesso();
			numeroGuia = objeto.getNumeroGuia();
			numeroGuiaSerie = objeto.getNumeroGuiaSerie();
			numeroGuiaCompleto = objeto.getNumeroGuiaCompleto();
			numeroProcessoDependente = objeto.getNumeroProcessoDependente();
			AreaQueixaCrime = objeto.getAreaQueixaCrime();
			ProcessoTipoCodigo = objeto.getProcessoTipoCodigo();
			DataVencimento = objeto.getDataVencimento();
			DataRecebimento = objeto.getDataRecebimento();
			DataEmissao = objeto.getDataEmissao();
			DataCancelamento = objeto.getDataCancelamento();
			DataEmissaoBoleto = objeto.getDataEmissaoBoleto();
			DataBaseAtualizacao = objeto.getDataBaseAtualizacao();
			DataBaseFinalAtualizacao = objeto.getDataBaseFinalAtualizacao();
			Id_Usuario = objeto.getId_Usuario();
			NumeroDUAM = objeto.getNumeroDUAM();
			QuantidadeParcelasDUAM = objeto.getQuantidadeParcelasDUAM();
			DataVencimentoDUAM = objeto.getDataVencimentoDUAM();
			ValorImpostoMunicipalDUAM = objeto.getValorImpostoMunicipalDUAM();
			QuantidadeAcrescimo = objeto.getQuantidadeAcrescimo();
			AvaliadorQuantidade = objeto.getAvaliadorQuantidade();
			AvaliadorValor = objeto.getAvaliadorValor();
			HonorariosQuantidade = objeto.getHonorariosQuantidade();
			HonorariosValor = objeto.getHonorariosValor();
			ParcelasQuantidade = objeto.getParcelasQuantidade();
			PenhoraQuantidade = objeto.getPenhoraQuantidade();
			PenhoraValor = objeto.getPenhoraValor();
			PartidorQuantidade = objeto.getPartidorQuantidade();
			PartidorValor = objeto.getPartidorValor();
			LeilaoQuantidade = objeto.getLeilaoQuantidade();
			LeilaoValor = objeto.getLeilaoValor();
			PorteRemessaQuantidade = objeto.getPorteRemessaQuantidade();
			PorteRemessaValorManual = objeto.getPorteRemessaValorManual();
			PorteRemessaQuantidade16V = objeto.getPorteRemessaQuantidade16V();
			RemessaStjQuantidade = objeto.getRemessaStjQuantidade();
			TaxaJudiciariaServicoCartaArrematacao = objeto.getTaxaJudiciariaServicoCartaArrematacao();
			TaxaJudiciariaServicoCertidao = objeto.getTaxaJudiciariaServicoCertidao();
			DescontoTaxaJudiciaria = objeto.getDescontoTaxaJudiciaria();
			CodigoOficialSPGAvaliador = objeto.getCodigoOficialSPGAvaliador();
			CodigoOficialSPGPenhora = objeto.getCodigoOficialSPGPenhora();
			CodigoOficialSPGPartidor = objeto.getCodigoOficialSPGPartidor();
			CodigoOficialSPGLocomocao = objeto.getCodigoOficialSPGLocomocao();
			CodigoOficialSPGLocomocaoContaVinculada = objeto.getCodigoOficialSPGLocomocaoContaVinculada();
			CodigoOficialSPGLeilao = objeto.getCodigoOficialSPGLeilao();
			DividaAtivaQuantidade = objeto.getDividaAtivaQuantidade();
			EscrivaniaQuantidade = objeto.getEscrivaniaQuantidade();
			ContadorQuantidade = objeto.getContadorQuantidade();
			AtosEscrivaesCivel = objeto.getAtosEscrivaesCivel();
			PorcentagemEscrivaesCivel = objeto.getPorcentagemEscrivaesCivel();
			ContadorQuantidadeAcrescimo = objeto.getContadorQuantidadeAcrescimo();
			CustasQuantidade = objeto.getCustasQuantidade();
			CustasValorManual = objeto.getCustasValorManual();
			TaxaProtocoloQuantidade = objeto.getTaxaProtocoloQuantidade();
			TaxaServicoDividaAtivaQuantidade = objeto.getTaxaServicoDividaAtivaQuantidade();
			RetificacaoCustasQuantidade = objeto.getRetificacaoCustasQuantidade();
			RetificacaoCalculosQuantidade = objeto.getRetificacaoCalculosQuantidade();
			AtualizacaoValorNominalQuantidade = objeto.getAtualizacaoValorNominalQuantidade();
			TransformacaoMoedaQuantidade = objeto.getTransformacaoMoedaQuantidade();
			CorreioQuantidade = objeto.getCorreioQuantidade();
			RateioQuantidade = objeto.getRateioQuantidade();
			FormalPartilhaCartaQuantidade = objeto.getFormalPartilhaCartaQuantidade();
			DepositarioPublico = objeto.getDepositarioPublico();
			PregaoPorteiro = objeto.getPregaoPorteiro();
			AfixacaoEdital = objeto.getAfixacaoEdital();
			Apelante = objeto.getApelante();
			Apelado = objeto.getApelado();
			Id_Apelante = objeto.getId_Apelante();
			Id_Apelado = objeto.getId_Apelado();
			Id_Finalidade = objeto.getId_Finalidade();
			Finalidade = objeto.getFinalidade();
			RateioCodigo = objeto.getRateioCodigo();
			ProcessoParteTipoCodigo = objeto.getProcessoParteTipoCodigo();
			AtoEscrivao = objeto.getAtoEscrivao();
			AtoEscrivaoCivel = objeto.getAtoEscrivaoCivel();
			DistribuidorQuantidade = objeto.getDistribuidorQuantidade();
			Id_ProcessoParteResponsavelGuia = objeto.getId_ProcessoParteResponsavelGuia();
			listaGuiaItemDt = objeto.getListaGuiaItemDt();
			listaRequerentes = objeto.getListaRequerentes();
			listaRequeridos = objeto.getListaRequeridos();
			listaOutrasPartes = objeto.getListaOutrasPartes();
			listaAdvogados = objeto.getListaAdvogados();
			listaAssuntos = objeto.getListaAssuntos();
			bensPartilhar = objeto.getBensPartilhar();
			citacaoUrbana = objeto.getCitacaoUrbana();
			qtdeImpetrantes = objeto.getQtdeImpetrantes();
			natureza = objeto.getNatureza();
			codigoNatureza = objeto.getCodigoNatureza();
			liquidacaoArtigoArbitramento = objeto.getLiquidacaoArtigoArbitramento();
			regimento33ExecucaoRecaiBens = objeto.getRegimento33ExecucaoRecaiBens();
			bensPenhoradosAvaliadosAlienados = objeto.getBensPenhoradosAvaliadosAlienados();
			tipoFalencia = objeto.getTipoFalencia();
			InventarioArrolamentoSobrepartilha = objeto.getInventarioArrolamentoSobrepartilha();
			this.setProcessoDt(objeto.getProcessoDt());
			this.ValorRecebimento = objeto.getValorRecebimento();
			this.DataMovimento = objeto.getDataMovimento();
			this.DataRecebimento = objeto.getDataRecebimento();
			this.penhora = objeto.isPenhora();
			this.intimacao = objeto.isIntimacao();
			this.oficialCompanheiro = objeto.isOficialCompanheiro();
			this.foraHorarioNormal = objeto.isForaHorarioNormal();
			this.citacaoHoraCerta = objeto.isCitacaoHoraCerta();
			this.locomocaoComplementar = objeto.isLocomocaoComplementar();	
			this.listaLocomocaoNaoUtilizadaDt = objeto.getListaLocomocaoNaoUtilizadaDt();
			this.setGuiaEmitidaSPG(objeto.isGuiaEmitidaSPG());
			this.guiaEmitidaSPG = objeto.isGuiaEmitidaSPG();
			NumeroProcessoSPG = objeto.getNumeroProcessoSPG();
			this.setSituacaoPagamentoSPG(objeto.getSituacaoPagamentoSPG());
			this.setGuiaEmitidaSPGCapital(objeto.isGuiaEmitidaSPGCapital());
			valorMediacao = objeto.getValorMediacao();
			valorConciliacao = objeto.getValorConciliacao();
			dataApresentacaoSPG = objeto.getDataApresentacaoSPG();
			this.codigoOabSPG = objeto.getCodigoOabSPG();
			this.dataIniCertidaoSPG = objeto.getDataIniCertidaoSPG();
			this.dataFimCertidaoSPG = objeto.getDataFimCertidaoSPG();
			this.tipoPessoaSPG = objeto.getTipoPessoaSPG();
			this.infoPatenteSPG = objeto.getInfoPatenteSPG();
			this.tipoGuiaCertidaoSPG = objeto.getTipoGuiaCertidaoSPG();
			this.dadosAdicionaisParaLog = objeto.getDadosAdicionaisParaLog();
			this.setGuiaEmitidaSSG(objeto.isGuiaEmitidaSSG());
			this.certidaoAcordao = objeto.getCertidaoAcordao();
			this.desarquivamento = objeto.getDesarquivamento();
			this.desarquivamento16II = objeto.getDesarquivamento16II();
			this.restauracao = objeto.getRestauracao();
			this.restauracaoAtos = objeto.getRestauracaoAtos();
			this.correioQuantidadeReg4VI = objeto.getCorreioQuantidadeReg4VI();
			this.emissaoDocumentoQuantidade = objeto.getEmissaoDocumentoQuantidade();
			this.atosConstricao = objeto.getAtosConstricao();
			this.certidaoDecisao = objeto.getCertidaoDecisao();
			this.documentoDiarioJustica = objeto.getDocumentoDiarioJustica();
			this.emissaoDocumentoQuantidade16VII = objeto.getEmissaoDocumentoQuantidade16VII();
			this.atosConstricao16VIII = objeto.getAtosConstricao16VIII();
			this.FormalPartilhaCartaQuantidade16IX = objeto.getFormalPartilhaCartaQuantidade16IX();
			this.cumprimentoPrecatoria = objeto.getCumprimentoPrecatoria();
			this.numeroCompletoProcesso = objeto.getNumeroCompletoProcesso();
			this.infoLocalCertidaoSPG = objeto.getInfoLocalCertidaoSPG();
			this.setGrauGuiaInicialMesmoGrauProcessoDependente(objeto.isGrauGuiaInicialMesmoGrauProcessoDependente());
			this.isGuiaEnviadaCadin = objeto.isGuiaEnviadaCadin();
			this.isProcessoPossuiGuiaEnviadaCadin = objeto.isProcessoPossuiGuiaEnviadaCadin();
			this.tipoGuiaReferenciaDescontoParcelamento = objeto.getTipoGuiaReferenciaDescontoParcelamento();
			
			nomePrimeiroAutor = objeto.getNomePrimeiroAutor();
			numrCpfCgc = objeto.getNumrCpfCgc();
			tipoGuia = objeto.getTipoGuia();
			comarcaCodigo = objeto.getComarcaCodigo();
			serventiaCodigo = objeto.getServentiaCodigo();
			processoTipoCodNatureza = objeto.getProcessoTipoCodNatureza();
			custaCertidao = objeto.getCustaCertidao();
			custaTaxaJudiciaria = objeto.getCustaTaxaJudiciaria();
			custaTotal = objeto.getCustaTotal();
			modoEmissaoCertidaoSPG = objeto.getModoEmissaoCertidaoSPG();
			infoAreaSPG = objeto.getInfoAreaSPG();
		}
	}
	
	public String getDadosAdicionaisParaLog() {
		return dadosAdicionaisParaLog;
	}

	public void setDadosAdicionaisParaLog(String dadosAdicionaisParaLog) {
		this.dadosAdicionaisParaLog += dadosAdicionaisParaLog + "; ";
	}

	public String getValorMediacao() {
		if( valorMediacao == null )
			valorMediacao = "";
		return valorMediacao;
	}


	public void setValorMediacao(String valorMediacao) {
		this.valorMediacao = valorMediacao;
	}


	public String getValorConciliacao() {
		if( valorConciliacao == null )
			valorConciliacao = "";
		return valorConciliacao;
	}


	public void setValorConciliacao(String valorConciliacao) {
		this.valorConciliacao = valorConciliacao;
	}
	
	public void setId_Processo(String Id_Processo) {
		this.Id_Processo = Id_Processo;
	}
	
	public String getId_Processo() {
		return this.Id_Processo;
	}
	
	public void setId_GuiaFinalidadeModelo(String Id_GuiaFinalidadeModelo) {
		this.Id_GuiaFinalidadeModelo = Id_GuiaFinalidadeModelo;
	}
	
	public String getId_GuiaFinalidadeModelo() {
		return this.Id_GuiaFinalidadeModelo;
	}
	
	
	/**
	 * Método para receber Propriedades
	 */
	public String getPropriedades() {
		String id_guiamodelo = new String("null");
		String guiamodelo = new String("null");
		String id_guiafinalidademodelo = new String("null");

		if (guiaModeloDt != null){
			id_guiamodelo = guiaModeloDt.getId();
			guiamodelo = guiaModeloDt.getGuiaModelo();
		}
		if (guiaFinalidadeModeloDt != null){
			id_guiafinalidademodelo = guiaFinalidadeModeloDt.getId();
		}
		
		String retorno = "[Id_GuiaEmissao:" + this.getId() + ";GuiaEmissao:" + this.getGuiaEmissao() + ";Id_GuiaModelo:" + id_guiamodelo + ";GuiaModelo:" + guiamodelo + ";Id_GuiaFinalidadeModelo:" + id_guiafinalidademodelo + ";Id_Processo:" + this.getId_Processo() + ";Id_Serventia:" + this.getId_Serventia() + ";Serventia:" + this.getServentia() + ";Id_ProcessoTipo:" + this.getId_ProcessoTipo() + ";ProcessoTipo:" + this.getProcessoTipo() + ";Id_GuiaEmissaoPrincipal:" + Id_GuiaEmissaoPrincipal + ";Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";DataRecebimento:" + DataRecebimento + ";DataVencimento:" + DataVencimento + ";DataEmissao:" + DataEmissao + ";Requerente:" + Requerente + ";Requerido:" + Requerido + ";ValorAcao:" + ValorAcao + ";numeroGuiaCompleto:" + numeroGuiaCompleto + ";numeroGuiaPrincipalCompleto:" + numeroGuiaPrincipalCompleto + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_GuiaStatus:" + Id_GuiaStatus + ";GuiaStatus:" + GuiaStatus + ";Id_Usuario:" + Id_Usuario + ";numeroProcessoDependente:" + numeroProcessoDependente + ";Id_AreaDistribuicao:" + Id_AreaDistribuicao + ";Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";Id_AreaQueixaCrime:" + Id_AreaQueixaCrime + ";NumeroProcessoSPG:" + NumeroProcessoSPG + ";SituacaoPagamentoSPG:" + SituacaoPagamentoSPG + ";AreaQueixaCrime:" + AreaQueixaCrime + ";idGuiaReferenciaDescontoParcelamento:"+idGuiaReferenciaDescontoParcelamento+";tipoGuiaReferenciaDescontoParcelamento:"+tipoGuiaReferenciaDescontoParcelamento+";quantidadeParcelas:"+quantidadeParcelas+";parcelaAtual:"+parcelaAtual+";porcentagemDesconto:"+porcentagemDesconto+ ";ValorMediacao:" + valorMediacao +";ValorConciliacao:" + valorConciliacao + ";DocumentoPublicadoQuantidade:" + documentoPublicadoQuantidade + ";OrigemEstado:"+ this.getOrigemEstado() +"]";
		
		if( this.dadosAdicionaisParaLog != null && !this.dadosAdicionaisParaLog.isEmpty() ) {
			retorno = retorno.replace("[", "[DadosAdicionaisParaLog:" + this.dadosAdicionaisParaLog + ";");
		}
		
		return retorno;
	}
	
	/**
	 * Método que retorna somente o primeiro Requerente da lista do json.
	 * @return String nome primeiro requerente
	 * @throws JSONException
	 */
	public String getNomePrimeiroRequerente() throws JSONException {
		String retorno = null;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			ProcessoParteDt processoParteDt = (ProcessoParteDt) listaRequerentes.get(0);
			retorno = processoParteDt.getNome();
		}
		else {
			/*
			JSONArray jsonArray = new JSONArray(this.getRequerente());
			if( jsonArray.length() > 0 ) {
				for (int i = 0; i < 1; i++) {
				    JSONObject jsonObject = jsonArray.getJSONObject(i);
				    
				    retorno = jsonObject.getString("nome");
				}
			}
			*/
			return "";
		}
		
		return retorno;
	}
	
	/**
	 * Método que retorna somente o primeiro Requerido da lista do json.
	 * @return String nome primeiro requerente
	 * @throws JSONException
	 */
	public String getNomePrimeiroRequerido() throws JSONException {
		String retorno = null;
		
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			ProcessoParteDt processoParteDt = (ProcessoParteDt) listaRequeridos.get(0);
			retorno = processoParteDt.getNome();
		}
		else {
//			JSONArray jsonArray = new JSONArray(this.getRequerido());
//			if( jsonArray.length() > 0 ) {
//				for (int i = 0; i < 1; i++) {
//				    JSONObject jsonObject = jsonArray.getJSONObject(i);
//				    
//				    retorno = jsonObject.getString("nome");
//				}
//			}
			return "";
		}
		
		return retorno;
	}
	
	/**
	 * Método para obter o nome da parte pelo id do processoParte.
	 * 
	 * @param String idProcessoParte
	 * @return String
	 */
	public String getNomeParte(String idProcessoParte) {
		String retorno = null;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for (int i = 0; i < listaRequerentes.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaRequerentes.get(i);
			    
				if( idProcessoParte.equals(processoParteDt.getId()) ) {
					retorno = processoParteDt.getNome();
					break;
				}
			}
		}
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for (int i = 0; i < listaRequeridos.size(); i++) {
				ProcessoParteDt processoParteDt = (ProcessoParteDt) listaRequeridos.get(i);
			    
				if( idProcessoParte.equals(processoParteDt.getId()) ) {
					retorno = processoParteDt.getNome();
					break;
				}
			}
		}
		
		return retorno;
	}
	
	public String getTipoFalencia() {
		return tipoFalencia;
	}

	public void setTipoFalencia(String tipoFalencia) {
		this.tipoFalencia = tipoFalencia;
	}
	
	public String getInventarioArrolamentoSobrepartilha() {
		return InventarioArrolamentoSobrepartilha;
	}

	public void setInventarioArrolamentoSobrepartilha(String inventarioArrolamentoSobrepartilha) {
		InventarioArrolamentoSobrepartilha = inventarioArrolamentoSobrepartilha;
	}

	public String getBensPenhoradosAvaliadosAlienados() {
		return bensPenhoradosAvaliadosAlienados;
	}

	public void setBensPenhoradosAvaliadosAlienados(String bensPenhoradosAvaliadosAlienados) {
		this.bensPenhoradosAvaliadosAlienados = bensPenhoradosAvaliadosAlienados;
	}

	public String getRegimento33ExecucaoRecaiBens() {
		return regimento33ExecucaoRecaiBens;
	}
	
	public void setRegimento33ExecucaoRecaiBens(String regimento33ExecucaoRecaiBens) {
		this.regimento33ExecucaoRecaiBens = regimento33ExecucaoRecaiBens;
	}
	
	public String getLiquidacaoArtigoArbitramento() {
		return liquidacaoArtigoArbitramento;
	}
	
	public void setLiquidacaoArtigoArbitramento(String liquidacaoArtigoArbitramento) {
		this.liquidacaoArtigoArbitramento = liquidacaoArtigoArbitramento;
	}
	
	public String getNatureza() {
		return natureza;
	}
	
	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}
	
	public String getCodigoNatureza() {
		return codigoNatureza;
	}
	
	public void setCodigoNatureza(String codigoNatureza) {
		this.codigoNatureza = codigoNatureza;
	}
	
	public String getBensPartilhar() {
		return this.bensPartilhar;
	}
	
	public void setBensPartilhar(String bensPartilhar) {
		this.bensPartilhar = bensPartilhar;
	}
	
	public String getCitacaoUrbana() {
		if( citacaoUrbana == null )
			citacaoUrbana = "0";
		return this.citacaoUrbana;
	}
	
	public void setCitacaoUrbana(String citacaoUrbana) {
		this.citacaoUrbana = citacaoUrbana;
	}
	
	public String getQtdeImpetrantes() {
		return this.qtdeImpetrantes;
	}
	
	public void setQtdeImpetrantes(String qtdeImpetrantes) {
		this.qtdeImpetrantes = qtdeImpetrantes;
	}
	
	
	public String getProcessoTipoCodigo() {
		return ProcessoTipoCodigo;
	}

	public void setProcessoTipoCodigo(String processoTipoCodigo) {
		ProcessoTipoCodigo = processoTipoCodigo;
	}
	
	public String getDataVencimento() {
		if (DataVencimento == null || DataVencimento.trim().length() == 0) {
			try {
				if( getDataEmissao() != null ) {
					TJDataHora emissaoBoleto = new TJDataHora(Funcoes.Stringyyyy_MM_ddToDateTime(getDataEmissao()));
					if (emissaoBoleto.getMes() != 1) {
						emissaoBoleto.setAno(emissaoBoleto.getAno() + 1);
					}
					emissaoBoleto.setMes(1);
					emissaoBoleto.setDia(31);	
					DataVencimento = emissaoBoleto.getDataHoraFormatadaaaaa_MM_ddHHmmss();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return DataVencimento;
	}
	
	public void setDataVencimento(String dataVencimento) {
		this.DataVencimento = dataVencimento;
	}
	
	public String getDataRecebimento() {
		return DataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		DataRecebimento = dataRecebimento;
	}

	public String getDataEmissao() {
		return DataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		DataEmissao = dataEmissao;
	}

	public String getDataCancelamento() {
		return DataCancelamento;
	}

	public void setDataCancelamento(String dataCancelamento) {
		DataCancelamento = dataCancelamento;
	}
	
	public String getDataEmissaoBoleto() {
		return DataEmissaoBoleto;
	}

	public void setDataEmissaoBoleto(String dataEmissaoBoleto) {
		DataEmissaoBoleto = dataEmissaoBoleto;
	}

	public String getDataBaseAtualizacao() {
		if( DataBaseAtualizacao == null ) {
			DataBaseAtualizacao = "";
		}
		return DataBaseAtualizacao;
	}

	public void setDataBaseAtualizacao(String dataBaseAtualizacao) {
		DataBaseAtualizacao = dataBaseAtualizacao;
	}
	
	public String getDataBaseFinalAtualizacao() {
		if( DataBaseFinalAtualizacao == null ) {
			DataBaseFinalAtualizacao = "";
		}
		return DataBaseFinalAtualizacao;
	}

	public void setDataBaseFinalAtualizacao(String dataBaseFinalAtualizacao) {
		DataBaseFinalAtualizacao = dataBaseFinalAtualizacao;
	}

	public String getId_Usuario() {
		return Id_Usuario;
	}
	
	public void setId_Usuario(String Id_Usuario) {
		this.Id_Usuario = Id_Usuario;
	}
	
	public String getUsuario() {
		return Usuario;
	}
	
	public void setUsuario(String Usuario) {
		this.Usuario = Usuario;
	}

	public String getNumeroDUAM() {
		if( NumeroDUAM == null )
			NumeroDUAM = "";
		return NumeroDUAM;
	}

	public void setNumeroDUAM(String numeroDUAM) {
		NumeroDUAM = numeroDUAM;
	}

	public String getQuantidadeParcelasDUAM() {
		if( QuantidadeParcelasDUAM == null )
			QuantidadeParcelasDUAM = "";
		return QuantidadeParcelasDUAM;
	}

	public void setQuantidadeParcelasDUAM(String quantidadeParcelasDUAM) {
		QuantidadeParcelasDUAM = quantidadeParcelasDUAM;
	}

	public String getDataVencimentoDUAM() {
		if( DataVencimentoDUAM == null )
			DataVencimentoDUAM = "";
		return DataVencimentoDUAM;
	}

	public void setDataVencimentoDUAM(String dataVencimentoDUAM) {
		DataVencimentoDUAM = dataVencimentoDUAM;
	}

	public String getValorImpostoMunicipalDUAM() {
		if( ValorImpostoMunicipalDUAM == null )
			ValorImpostoMunicipalDUAM = "";
		return ValorImpostoMunicipalDUAM;
	}

	public void setValorImpostoMunicipalDUAM(String valorImpostoMunicipalDUAM) {
		ValorImpostoMunicipalDUAM = valorImpostoMunicipalDUAM;
	}
	
	public String getQuantidadeAcrescimo() {
		if( QuantidadeAcrescimo == null )
			QuantidadeAcrescimo = "0";
		return QuantidadeAcrescimo;
	}

	public void setQuantidadeAcrescimo(String QuantidadeAcrescimo) {
		this.QuantidadeAcrescimo = QuantidadeAcrescimo;
	}

	public String getAvaliadorQuantidade() {
		if( AvaliadorQuantidade == null )
			AvaliadorQuantidade = "0";
		return AvaliadorQuantidade;
	}

	public void setAvaliadorQuantidade(String AvaliadorQuantidade) {
		this.AvaliadorQuantidade = AvaliadorQuantidade;
	}
	
	public String getAvaliadorValor() {
		if( AvaliadorValor == null )
			AvaliadorValor = "0";
		return AvaliadorValor;
	}

	public void setAvaliadorValor(String AvaliadorValor) {
		this.AvaliadorValor = AvaliadorValor;
	}
	
	public String getHonorariosQuantidade() {
		if( HonorariosQuantidade == null )
			HonorariosQuantidade = "0";
		return HonorariosQuantidade;
	}

	public void setHonorariosQuantidade(String HonorariosQuantidade) {
		this.HonorariosQuantidade = HonorariosQuantidade;
	}
	
	public String getHonorariosValor() {
		if( HonorariosValor == null )
			HonorariosValor = "0";
		return HonorariosValor;
	}

	public void setHonorariosValor(String HonorariosValor) {
		this.HonorariosValor = HonorariosValor;
	}
	
	public String getParcelasQuantidade() {
		if( ParcelasQuantidade == null )
			ParcelasQuantidade = "0";
		return ParcelasQuantidade;
	}

	public void setParcelasQuantidade(String ParcelasQuantidade) {
		this.ParcelasQuantidade = ParcelasQuantidade;
	}
	
	public String getPenhoraQuantidade() {
		if( PenhoraQuantidade == null )
			PenhoraQuantidade = "0";
		return PenhoraQuantidade;
	}

	public void setPenhoraQuantidade(String PenhoraQuantidade) {
		this.PenhoraQuantidade = PenhoraQuantidade;
	}
	
	public String getPenhoraValor() {
		if( PenhoraValor == null )
			PenhoraValor = "";
		return PenhoraValor;
	}

	public void setPenhoraValor(String PenhoraValor) {
		this.PenhoraValor = PenhoraValor;
	}
	
	public String getPenhoraComarca() {
		if( PenhoraComarca == null )
			PenhoraComarca = GuiaEmissaoDt.VALOR_NAO;
		return PenhoraComarca;
	}

	public void setPenhoraComarca(String penhoraComarca) {
		PenhoraComarca = penhoraComarca;
	}

	public String getIntimacaoLocomocao() {
		if( IntimacaoLocomocao == null )
			IntimacaoLocomocao = GuiaEmissaoDt.VALOR_NAO;
		return IntimacaoLocomocao;
	}

	public void setIntimacaoLocomocao(String intimacaoLocomocao) {
		IntimacaoLocomocao = intimacaoLocomocao;
	}

	public String getPartidorQuantidade() {
		if( PartidorQuantidade == null )
			PartidorQuantidade = "0";
		return PartidorQuantidade;
	}

	public void setPartidorQuantidade(String PartidorQuantidade) {
		this.PartidorQuantidade = PartidorQuantidade;
	}
	
	public String getPartidorValor() {
		if( PartidorValor == null )
			PartidorValor = "";
		return PartidorValor;
	}
	
	public String getLeilaoQuantidade() {
		if( LeilaoQuantidade == null )
			LeilaoQuantidade = "0";
		return LeilaoQuantidade;
	}

	public void setLeilaoQuantidade(String LeilaoQuantidade) {
		this.LeilaoQuantidade = LeilaoQuantidade;
	}

	public void setPartidorValor(String PartidorValor) {
		this.PartidorValor = PartidorValor;
	}
	
	public String getLeilaoValor() {
		if( LeilaoValor == null )
			LeilaoValor = "0";
		return LeilaoValor;
	}

	public void setLeilaoValor(String LeilaoValor) {
		this.LeilaoValor = LeilaoValor;
	}
	
	public String getPorteRemessaQuantidade() {
		if( PorteRemessaQuantidade == null )
			PorteRemessaQuantidade = "0";
		return PorteRemessaQuantidade;
	}

	public void setPorteRemessaQuantidade(String porteRemessaQuantidade) {
		PorteRemessaQuantidade = porteRemessaQuantidade;
	}
	
	public String getPorteRemessaValorManual() {
		if( PorteRemessaValorManual == null )
			PorteRemessaValorManual = "0";
		return PorteRemessaValorManual;
	}

	public void setPorteRemessaValorManual(String porteRemessaValorManual) {
		PorteRemessaValorManual = porteRemessaValorManual;
	}

	public String getRemessaStjQuantidade() {
		if( RemessaStjQuantidade == null )
			RemessaStjQuantidade = "0";
		return RemessaStjQuantidade;
	}

	public void setRemessaStjQuantidade(String remessaStjQuantidade) {
		RemessaStjQuantidade = remessaStjQuantidade;
	}

	public String getDescontoTaxaJudiciaria() {
		if( DescontoTaxaJudiciaria == null )
			DescontoTaxaJudiciaria = "";
		return DescontoTaxaJudiciaria;
	}
	
	public void setDescontoTaxaJudiciaria(String DescontoTaxaJudiciaria) {
		this.DescontoTaxaJudiciaria = DescontoTaxaJudiciaria;
	}

	public String getCodigoOficialSPGAvaliador() {
		if( CodigoOficialSPGAvaliador == null )
			CodigoOficialSPGAvaliador = "";
		return CodigoOficialSPGAvaliador;
	}

	public void setCodigoOficialSPGAvaliador(String CodigoOficialSPGAvaliador) {
		this.CodigoOficialSPGAvaliador = CodigoOficialSPGAvaliador;
	}

	public String getCodigoOficialSPGPenhora() {
		if( CodigoOficialSPGPenhora == null )
			CodigoOficialSPGPenhora = "";
		return CodigoOficialSPGPenhora;
	}

	public void setCodigoOficialSPGPenhora(String CodigoOficialSPGPenhora) {
		this.CodigoOficialSPGPenhora = CodigoOficialSPGPenhora;
	}
	
	public String getCodigoOficialSPGPartidor() {
		if( CodigoOficialSPGPartidor == null )
			CodigoOficialSPGPartidor = "";
		return CodigoOficialSPGPartidor;
	}

	public void setCodigoOficialSPGPartidor(String CodigoOficialSPGPartidor) {
		this.CodigoOficialSPGPartidor = CodigoOficialSPGPartidor;
	}

	public String getCodigoOficialSPGLocomocao() {
		if( CodigoOficialSPGLocomocao == null )
			CodigoOficialSPGLocomocao = "";
		return CodigoOficialSPGLocomocao;
	}

	public void setCodigoOficialSPGLocomocao(String CodigoOficialSPGLocomocao) {
		this.CodigoOficialSPGLocomocao = CodigoOficialSPGLocomocao;
	}
	
	public String getCodigoOficialSPGLocomocaoContaVinculada() {
		if( CodigoOficialSPGLocomocaoContaVinculada == null )
			CodigoOficialSPGLocomocaoContaVinculada = "";
		return CodigoOficialSPGLocomocaoContaVinculada;
	}

	public void setCodigoOficialSPGLocomocaoContaVinculada(String CodigoOficialSPGLocomocaoContaVinculada) {
		this.CodigoOficialSPGLocomocaoContaVinculada = CodigoOficialSPGLocomocaoContaVinculada;
	}
	
	public String getCodigoOficialSPGLeilao() {
		if( CodigoOficialSPGLeilao == null )
			CodigoOficialSPGLeilao = "";
		return CodigoOficialSPGLeilao;
	}

	public void setCodigoOficialSPGLeilao(String CodigoOficialSPGLeilao) {
		this.CodigoOficialSPGLeilao = CodigoOficialSPGLeilao;
	}
	
	public String getDividaAtivaQuantidade() {
		if( DividaAtivaQuantidade == null )
			DividaAtivaQuantidade = "1";
		return DividaAtivaQuantidade;
	}

	public void setDividaAtivaQuantidade(String dividaAtivaQuantidade) {
		this.DividaAtivaQuantidade = dividaAtivaQuantidade;
	}
	
	public String getEscrivaniaQuantidade() {
		if( EscrivaniaQuantidade == null )
			EscrivaniaQuantidade = "0";
		return EscrivaniaQuantidade;
	}

	public void setEscrivaniaQuantidade(String escrivaniaQuantidade) {
		this.EscrivaniaQuantidade = escrivaniaQuantidade;
	}
	
	public String getContadorQuantidade() {
		if( ContadorQuantidade == null )
			ContadorQuantidade = "0";
		return ContadorQuantidade;
	}

	public void setContadorQuantidade(String ContadorQuantidade) {
		this.ContadorQuantidade = ContadorQuantidade;
	}
	
	public String getAtosEscrivaesCivel() {
		if( AtosEscrivaesCivel == null )
			AtosEscrivaesCivel = "0";
		return AtosEscrivaesCivel;
	}

	public void setAtosEscrivaesCivel(String AtosEscrivaesCivel) {
		this.AtosEscrivaesCivel = AtosEscrivaesCivel;
	}
	
	public String getPorcentagemEscrivaesCivel() {
		if( PorcentagemEscrivaesCivel == null )
			PorcentagemEscrivaesCivel = "0";
		return PorcentagemEscrivaesCivel;
	}

	public void setPorcentagemEscrivaesCivel(String PorcentagemEscrivaesCivel) {
		this.PorcentagemEscrivaesCivel = PorcentagemEscrivaesCivel;
	}
	
	public String getContadorQuantidadeAcrescimo() {
		if( ContadorQuantidadeAcrescimo == null )
			ContadorQuantidadeAcrescimo = "0";
		return ContadorQuantidadeAcrescimo;
	}

	public void setContadorQuantidadeAcrescimo(String ContadorQuantidadeAcrescimo) {
		this.ContadorQuantidadeAcrescimo = ContadorQuantidadeAcrescimo;
	}
	
	public String getCustasQuantidade() {
		if( CustasQuantidade == null )
			CustasQuantidade = "0";
		return CustasQuantidade;
	}

	public void setCustasQuantidade(String CustasQuantidade) {
		this.CustasQuantidade = CustasQuantidade;
	}
	
	public String getCustasValorManual() {
		if( CustasValorManual == null )
			CustasValorManual = "0";
		return CustasValorManual;
	}

	public void setCustasValorManual(String CustasValorManual) {
		this.CustasValorManual = CustasValorManual;
	}
	
	
	public String getTaxaProtocoloQuantidade() {
		if( TaxaProtocoloQuantidade == null )
			TaxaProtocoloQuantidade = "0";
		return TaxaProtocoloQuantidade;
	}

	public void setTaxaProtocoloQuantidade(String TaxaProtocoloQuantidade) {
		this.TaxaProtocoloQuantidade = TaxaProtocoloQuantidade;
	}
	
	public String getTaxaServicoDividaAtivaQuantidade() {
		return TaxaServicoDividaAtivaQuantidade;
	}

	public void setTaxaServicoDividaAtivaQuantidade(String TaxaServicoDividaAtivaQuantidade) {
		this.TaxaServicoDividaAtivaQuantidade = TaxaServicoDividaAtivaQuantidade;
	}

	public String getRetificacaoCustasQuantidade() {
		if( RetificacaoCustasQuantidade == null )
			RetificacaoCustasQuantidade = "0";
		return RetificacaoCustasQuantidade;
	}

	public void setRetificacaoCustasQuantidade(String RetificacaoCustasQuantidade) {
		this.RetificacaoCustasQuantidade = RetificacaoCustasQuantidade;
	}
	
	public String getRetificacaoCalculosQuantidade() {
		if( RetificacaoCalculosQuantidade == null )
			RetificacaoCalculosQuantidade = "0";
		return RetificacaoCalculosQuantidade;
	}

	public void setRetificacaoCalculosQuantidade(String RetificacaoCalculosQuantidade) {
		this.RetificacaoCalculosQuantidade = RetificacaoCalculosQuantidade;
	}
	
	public String getAtualizacaoValorNominalQuantidade() {
		if( AtualizacaoValorNominalQuantidade == null )
			AtualizacaoValorNominalQuantidade = "0";
		return AtualizacaoValorNominalQuantidade;
	}

	public void setAtualizacaoValorNominalQuantidade(String AtualizacaoValorNominalQuantidade) {
		this.AtualizacaoValorNominalQuantidade = AtualizacaoValorNominalQuantidade;
	}
	
	public String getTransformacaoMoedaQuantidade() {
		if( TransformacaoMoedaQuantidade == null )
			TransformacaoMoedaQuantidade = "0";
		return TransformacaoMoedaQuantidade;
	}

	public void setTransformacaoMoedaQuantidade(String TransformacaoMoedaQuantidade) {
		this.TransformacaoMoedaQuantidade = TransformacaoMoedaQuantidade;
	}
	
	public String getCorreioQuantidade() {
		if( CorreioQuantidade == null )
			CorreioQuantidade = "0";
		return CorreioQuantidade;
	}

	public void setCorreioQuantidade(String CorreioQuantidade) {
		this.CorreioQuantidade = CorreioQuantidade;
	}
	
	public String getRateioQuantidade() {
		if( RateioQuantidade == null )
			RateioQuantidade = "0";
		return RateioQuantidade;
	}

	public void setRateioQuantidade(String RateioQuantidade) {
		this.RateioQuantidade = RateioQuantidade;
	}
	
	public String getFormalPartilhaCartaQuantidade() {
		if( FormalPartilhaCartaQuantidade == null || FormalPartilhaCartaQuantidade.equals("null") ) {
			FormalPartilhaCartaQuantidade = "0";
		}
		return FormalPartilhaCartaQuantidade;
	}

	public void setFormalPartilhaCartaQuantidade(String formalPartilhaCartaQuantidade) {
		FormalPartilhaCartaQuantidade = formalPartilhaCartaQuantidade;
	}

	public String getDepositarioPublico() {
		if( DepositarioPublico == null )
			DepositarioPublico = "0";
		return DepositarioPublico;
	}

	public void setDepositarioPublico(String DepositarioPublico) {
		this.DepositarioPublico = DepositarioPublico;
	}
	
	public String getDepositarioPublicoBemImovel() {
		if( DepositarioPublicoBemImovel == null )
			DepositarioPublicoBemImovel = "";
		return DepositarioPublicoBemImovel;
	}

	public void setDepositarioPublicoBemImovel(String depositarioPublicoBemImovel) {
		DepositarioPublicoBemImovel = depositarioPublicoBemImovel;
	}

	public String getDepositarioPublicoDataInicial() {
		if( DepositarioPublicoDataInicial == null )
			DepositarioPublicoDataInicial = "";
		return DepositarioPublicoDataInicial;
	}

	public void setDepositarioPublicoDataInicial(String depositarioPublicoDataInicial) {
		DepositarioPublicoDataInicial = depositarioPublicoDataInicial;
	}

	public String getDepositarioPublicoDataFinal() {
		if( DepositarioPublicoDataFinal == null )
			DepositarioPublicoDataFinal = "";
		return DepositarioPublicoDataFinal;
	}

	public void setDepositarioPublicoDataFinal(String depositarioPublicoDataFinal) {
		DepositarioPublicoDataFinal = depositarioPublicoDataFinal;
	}

	public String getDepositarioPublicoBemImovelDataInicial() {
		if( DepositarioPublicoBemImovelDataInicial == null )
			DepositarioPublicoBemImovelDataInicial = "";
		return DepositarioPublicoBemImovelDataInicial;
	}

	public void setDepositarioPublicoBemImovelDataInicial(String depositarioPublicoBemImovelDataInicial) {
		DepositarioPublicoBemImovelDataInicial = depositarioPublicoBemImovelDataInicial;
	}

	public String getDepositarioPublicoBemImovelDataFinal() {
		if( DepositarioPublicoBemImovelDataFinal == null )
			DepositarioPublicoBemImovelDataFinal = "";
		return DepositarioPublicoBemImovelDataFinal;
	}

	public void setDepositarioPublicoBemImovelDataFinal(String depositarioPublicoBemImovelDataFinal) {
		DepositarioPublicoBemImovelDataFinal = depositarioPublicoBemImovelDataFinal;
	}
	
	public String getPregaoPorteiro() {
		if( PregaoPorteiro == null )
			PregaoPorteiro = "0";
		return PregaoPorteiro;
	}

	public void setPregaoPorteiro(String PregaoPorteiro) {
		this.PregaoPorteiro = PregaoPorteiro;
	}
	
	public String getAfixacaoEdital() {
		if( AfixacaoEdital == null )
			AfixacaoEdital = "0";
		return AfixacaoEdital;
	}

	public void setAfixacaoEdital(String AfixacaoEdital) {
		this.AfixacaoEdital = AfixacaoEdital;
	}
	
	public String getApelante() {
		return Apelante;
	}

	public void setApelante(String apelante) {
		Apelante = apelante;
	}

	public String getApelado() {
		return Apelado;
	}

	public void setApelado(String apelado) {
		Apelado = apelado;
	}
	
	public String getId_Apelante() {
		return Id_Apelante;
	}

	public void setId_Apelante(String Id_apelante) {
		Id_Apelante = Id_apelante;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for( int i = 0; i < listaRequerentes.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaRequerentes.get(i);
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for( int i = 0; i < listaRequeridos.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaRequeridos.get(i);
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		if( listaPartesLitisconsorte != null && listaPartesLitisconsorte.size() > 0 ) {
			for( int i = 0; i < listaPartesLitisconsorte.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartesLitisconsorte.get(i);
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		
		if( listaOutrasPartes != null && listaOutrasPartes.size() > 0 ) {
			for( int i = 0; i < listaOutrasPartes.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaOutrasPartes.get(i);
				if( parteDt.getId().equals(Id_apelante) ) {
					this.setApelante(parteDt.getNome());
					break;
				}
			}
		}
		
	}
	
	public String getId_Apelado() {
		return Id_Apelado;
	}

	public void setId_Apelado(String Id_apelado) {
		Id_Apelado = Id_apelado;
		
		if( listaRequerentes != null && listaRequerentes.size() > 0 ) {
			for( int i = 0; i < listaRequerentes.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaRequerentes.get(i);
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}
		if( listaRequeridos != null && listaRequeridos.size() > 0 ) {
			for( int i = 0; i < listaRequeridos.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaRequeridos.get(i);
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}
		if( listaPartesLitisconsorte != null && listaPartesLitisconsorte.size() > 0 ) {
			for( int i = 0; i < listaPartesLitisconsorte.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartesLitisconsorte.get(i);
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}			
		if( listaOutrasPartes != null && listaOutrasPartes.size() > 0 ) {
			for( int i = 0; i < listaOutrasPartes.size(); i++ ) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaOutrasPartes.get(i);
				if( parteDt.getId().equals(Id_apelado) ) {
					this.setApelado(parteDt.getNome());
					break;
				}
			}
		}	
	}
	
	public String getId_Finalidade() {		
		if( Id_Finalidade == null )
			Id_Finalidade = "";
		return Id_Finalidade;
	}

	public void setId_Finalidade(String id_finalidade) {
		Id_Finalidade = id_finalidade;
	}

	public String getFinalidade() {
		return Finalidade;
	}

	public void setFinalidade(String finalidade) {
		Finalidade = finalidade;
	}
	
	public String getRateioCodigo() {
		if( RateioCodigo == null ) {
			RateioCodigo = "";
		}
		return RateioCodigo;
	}

	public void setRateioCodigo(String rateioCodigo) {
		RateioCodigo = rateioCodigo;
	}

	public String getProcessoParteTipoCodigo() {
		return ProcessoParteTipoCodigo;
	}

	public void setProcessoParteTipoCodigo(String processoParteTipoCodigo) {
		ProcessoParteTipoCodigo = processoParteTipoCodigo;
	}

	public String getAreaQueixaCrime() {
		return AreaQueixaCrime;
	}

	public String getAtoEscrivao() {
		if( AtoEscrivao == null ) {
			AtoEscrivao = "0";
		}
		return AtoEscrivao;
	}

	public void setAtoEscrivao(String atoEscrivao) {
		AtoEscrivao = atoEscrivao;
	}
	
	public String getAtoEscrivaoCivel() {
		if( AtoEscrivaoCivel == null ) {
			AtoEscrivaoCivel = "0";
		}
		return AtoEscrivaoCivel;
	}

	public void setAtoEscrivaoCivel(String atoEscrivaoCivel) {
		AtoEscrivaoCivel = atoEscrivaoCivel;
	}

	public String getDistribuidorQuantidade() {
		if( DistribuidorQuantidade == null ) {
			DistribuidorQuantidade = "0";
		}
		return DistribuidorQuantidade;
	}

	public void setDistribuidorQuantidade(String distribuidorQuantidade) {
		DistribuidorQuantidade = distribuidorQuantidade;
	}

	public String getId_ProcessoParteResponsavelGuia() {
		return Id_ProcessoParteResponsavelGuia;
	}

	public void setId_ProcessoParteResponsavelGuia(String id_ProcessoParteResponsavelGuia) {
		Id_ProcessoParteResponsavelGuia = id_ProcessoParteResponsavelGuia;
	}

	public String getAreaDistribuicao() {
		return AreaDistribuicao;
	}

	public void setAreaDistribuicao(String areaDistribuicao) {
		AreaDistribuicao = areaDistribuicao;
	}

	public String getNomeProcessoParteResponsavelGuia() {
		return NomeProcessoParteResponsavelGuia;
	}

	public void setNomeProcessoParteResponsavelGuia(String nomeProcessoParteResponsavelGuia) {
		NomeProcessoParteResponsavelGuia = nomeProcessoParteResponsavelGuia;
	}

	public Double getTotalRateio() {
		return totalRateio;
	}

	public void setTotalRateio(Double totalRateio) {
		
		BigDecimal totRateio = new BigDecimal(totalRateio);
		totRateio = totRateio.setScale(2, BigDecimal.ROUND_DOWN);
		totalRateio = totRateio.doubleValue();
		
		this.totalRateio = totalRateio;
	}

	public String getCodigoGrau() {
		return codigoGrau;
	}

	public void setCodigoGrau(String codigoGrau) {
		this.codigoGrau = codigoGrau;
	}

	public String getCodigoArea() {
		return codigoArea;
	}

	public void setCodigoArea(String codigoArea) {
		this.codigoArea = codigoArea;
	}

	public void setAreaQueixaCrime(String areaQueixaCrime) {
		AreaQueixaCrime = areaQueixaCrime;
	}

	public String getId_AreaQueixaCrime() {
		if( Id_AreaQueixaCrime == null )
			return "";
		return Id_AreaQueixaCrime;
	}

	public void setId_AreaQueixaCrime(String id_AreaQueixaCrime) {
		Id_AreaQueixaCrime = id_AreaQueixaCrime;
	}
	
	public String getId_AreaDistribuicao() {
		return Id_AreaDistribuicao;
	}

	public void setId_AreaDistribuicao(String id_AreaDistribuicao) {
		Id_AreaDistribuicao = id_AreaDistribuicao;
	}

	public String getId_GuiaStatus() {
		return Id_GuiaStatus;
	}

	public void setId_GuiaStatus(int id_GuiaStatus) {
		Id_GuiaStatus = String.valueOf(id_GuiaStatus);
	}
	
	public void setId_GuiaStatus(String id_GuiaStatus) {
		Id_GuiaStatus = id_GuiaStatus;
	}

	public String getId_GuiaEmissaoPrincipal() {
		return Id_GuiaEmissaoPrincipal;
	}

	public void setId_GuiaEmissaoPrincipal(String id_GuiaEmissaoPrincipal) {
		Id_GuiaEmissaoPrincipal = id_GuiaEmissaoPrincipal;
	}
	

	public String getId_ProcessoPrioridade() {
		return Id_ProcessoPrioridade;
	}

	public void setId_ProcessoPrioridade(String id_ProcessoPrioridade) {
		Id_ProcessoPrioridade = id_ProcessoPrioridade;
	}

	public String getGuiaStatus() {
		return GuiaStatus;
	}

	public void setGuiaStatus(String guiaStatus) {
		GuiaStatus = guiaStatus;
	}

	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		this.numeroGuia = numeroGuia;
	}
	
	public String getNumeroGuiaSerie() {
		return numeroGuiaSerie;
	}
	
	public void setNumeroGuiaSerie(String numeroGuiaSerie) {
		this.numeroGuiaSerie = numeroGuiaSerie;
	}
	
	public String getNumeroGuiaCompleto() {
		return numeroGuiaCompleto;
	}

	public void setNumeroGuiaCompleto(String numeroGuiaCompleto) {
		this.numeroGuiaCompleto = numeroGuiaCompleto;
	}
	
	public String getNumeroGuiaPrincipalCompleto() {
		return numeroGuiaPrincipalCompleto;
	}

	public void setNumeroGuiaPrincipalCompleto(String numeroGuiaPrincipalCompleto) {
		this.numeroGuiaPrincipalCompleto = numeroGuiaPrincipalCompleto;
	}

	public String getNumeroProcessoDependente() {
		return this.numeroProcessoDependente;
	}
	
	public void setNumeroProcessoDependente(String numeroProcessoDependente) {
		this.numeroProcessoDependente = numeroProcessoDependente;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getRequerente() {
		return Requerente;
	}

	public void setRequerente(String requerente) {
		Requerente = requerente;
	}

	public String getRequerido() {
		return Requerido;
	}

	public void setRequerido(String requerido) {
		Requerido = requerido;
	}

	public List getListaAssuntos() {
		return listaAssuntos;
	}

	public void setListaAssuntos(List listaAssuntos) {
		this.listaAssuntos = listaAssuntos;
	}

	public GuiaModeloDt getGuiaModeloDt() {
		return guiaModeloDt;
	}

	public void setGuiaModeloDt(GuiaModeloDt guiaModeloDt) {
		if( this.getId_GuiaTipo() != null 
			&& this.getId_GuiaTipo().length() == 0
			&& guiaModeloDt != null 
			&& guiaModeloDt.getId_GuiaTipo() != null 
			&& !guiaModeloDt.getId_GuiaTipo().isEmpty() 
			&& !guiaModeloDt.getId_GuiaTipo().equals("null")) {
			
			this.setId_GuiaTipo(guiaModeloDt.getId_GuiaTipo());
		}
		this.guiaModeloDt = guiaModeloDt;
	}
	
	public GuiaFinalidadeModeloDt getGuiaFinalidadeModeloDt() {
		return guiaFinalidadeModeloDt;
	}

	public void setGuiaFinalidadeModeloDt(GuiaFinalidadeModeloDt guiaFinalidadeModeloDt) {
		this.guiaFinalidadeModeloDt = guiaFinalidadeModeloDt;
	}

	public String getId_Comarca() {
		return Id_Comarca;
	}

	public void setId_Comarca(String id_Comarca) {
		Id_Comarca = id_Comarca;
	}

	public String getComarca() {
		if( Comarca == null )
			return "";
		return Comarca;
	}

	public void setComarca(String comarca) {
		Comarca = comarca;
	}
	
	public String getComarcaCodigo() {
		return ComarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		ComarcaCodigo = comarcaCodigo;
	}
	
	public String getValorAcao() {
		return ValorAcao;
	}

	public void setValorAcao(String valorAcao) {
		ValorAcao = valorAcao;
	}

	public String getNovoValorAcao() {
		if( NovoValorAcao == null )
			NovoValorAcao = "";
		return NovoValorAcao;
	}

	public void setNovoValorAcao(String novoValorAcao) {
		NovoValorAcao = novoValorAcao;
	}
	
	public String getNovoValorAcaoAtualizado() {
		if( NovoValorAcaoAtualizado == null )
			NovoValorAcaoAtualizado = "";
		return NovoValorAcaoAtualizado;
	}

	public void setNovoValorAcaoAtualizado(String novoValorAcaoAtualizado) {
		NovoValorAcaoAtualizado = novoValorAcaoAtualizado;
	}

	public String getValorCondenacao() {
		return ValorCondenacao;
	}

	public void setValorCondenacao(String valorCondenacao) {
		ValorCondenacao = valorCondenacao;
	}

	public String getNovoValorCondenacao() {
		if( NovoValorCondenacao == null ) {
			NovoValorCondenacao = "";
		}
		return NovoValorCondenacao;
	}

	public void setNovoValorCondenacao(String novoValorCondenacao) {
		NovoValorCondenacao = novoValorCondenacao;
	}
	
	public String getValorCausaCoringa() {
		return valorCausaCoringa;
	}

	public void setValorCausaCoringa(String valorCausaCoringa) {
		this.valorCausaCoringa = valorCausaCoringa;
	}

	public List<GuiaItemDt> getListaGuiaItemDt() {
		return listaGuiaItemDt;
	}

	public void setListaGuiaItemDt(List<GuiaItemDt> listaGuiaItemDt) {
		this.listaGuiaItemDt = listaGuiaItemDt;
	}

	public List getListaRequerentes() {
		return listaRequerentes;
	}

	public void setListaRequerentes(List listaRequerentes) {
		this.listaRequerentes = listaRequerentes;
	}

	public List getListaRequeridos() {
		return listaRequeridos;
	}

	public void setListaRequeridos(List listaRequeridos) {
		this.listaRequeridos = listaRequeridos;
	}

	public List getListaPartesLitisconsorte() {
		return listaPartesLitisconsorte;
	}

	public void setListaPartesLitisconsorte(List listaPartesLitisconsorte) {
		this.listaPartesLitisconsorte = listaPartesLitisconsorte;
	}

	public List getListaOutrasPartes() {
		return listaOutrasPartes;
	}

	public void setListaOutrasPartes(List listaOutrasPartes) {
		this.listaOutrasPartes = listaOutrasPartes;
	}

	public List getListaAdvogados() {
		return listaAdvogados;
	}

	public void setListaAdvogados(List listaAdvogados) {
		this.listaAdvogados = listaAdvogados;
	}
	
	public String getValorTotalGuia() throws Exception {
		return new DecimalFormat("#0.00").format(getValorTotalGuiaDouble()).replace(",", ".");
	}
	
	public Double getValorTotalGuiaDouble() throws Exception {
		Double valorTotal = 0d;
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		
		if (this.listaGuiaItemDt == null || this.listaGuiaItemDt.size() == 0) {
			int id_guia_tipo = Funcoes.StringToInt(this.getId_GuiaTipo());
			if (id_guia_tipo == 0 && this.getGuiaModeloDt() != null && this.getGuiaModeloDt().getId_GuiaTipo() != null){
				id_guia_tipo = Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo());
			}			
			this.listaGuiaItemDt = guiaEmissaoNe.consultarGuiaItens(this.getId(), String.valueOf(id_guia_tipo));
		}
			
		
		valorTotal = guiaEmissaoNe.calcularTotalGuia(this.listaGuiaItemDt);	
		
		return valorTotal;		
	}
	
	public Double getValorTotalGuiaDouble(FabricaConexao obFabricaConexao) throws Exception {
		Double valorTotal = 0d;
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		
		if (this.listaGuiaItemDt == null || this.listaGuiaItemDt.size() == 0) {
			int id_guia_tipo = Funcoes.StringToInt(this.getId_GuiaTipo());
			if (id_guia_tipo == 0 && this.getGuiaModeloDt() != null && this.getGuiaModeloDt().getId_GuiaTipo() != null){
				id_guia_tipo = Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo());
			}			
			this.listaGuiaItemDt = guiaEmissaoNe.consultarGuiaItens(this.getId(), String.valueOf(id_guia_tipo), obFabricaConexao);
		}
			
		
		valorTotal = guiaEmissaoNe.calcularTotalGuia(this.listaGuiaItemDt);	
		
		return valorTotal;		
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public String getValorRecebimento() {
		return ValorRecebimento;
	}

	public void setValorRecebimento(String valorRecebimento) {
		ValorRecebimento = valorRecebimento;
	}

	public String getDataMovimento() {
		return DataMovimento;
	}

	public void setDataMovimento(String dataMovimento) {
		DataMovimento = dataMovimento;
	}

	public String getDataRepasse() {
		return DataRepasse;
	}

	public void setDataRepasse(String dataRepasse) {
		DataRepasse = dataRepasse;
	}

	public String getPercRepasse() {
		return percRepasse;
	}

	public void setPercRepasse(String percRepasse) {
		this.percRepasse = percRepasse;
	}
	
	public boolean isGuiaEnviadaCadin() {
		return isGuiaEnviadaCadin;
	}

	public void setGuiaEnviadaCadin(boolean isGuiaEnviadaCadin) {
		this.isGuiaEnviadaCadin = isGuiaEnviadaCadin;
	}
	
	public boolean isProcessoPossuiGuiaEnviadaCadin() {
		return isProcessoPossuiGuiaEnviadaCadin;
	}

	public void setProcessoPossuiGuiaEnviadaCadin(boolean isProcessoPossuiGuiaEnviadaCadin) {
		this.isProcessoPossuiGuiaEnviadaCadin = isProcessoPossuiGuiaEnviadaCadin;
	}

	public String getNumeroImpetrantes() {
		return NumeroImpetrantes;
	}

	public void setNumeroImpetrantes(String numeroImpetrantes) {
		NumeroImpetrantes = numeroImpetrantes;
	}

	public String getNaturezaSPGCodigo() {
		return NaturezaSPGCodigo;
	}

	public void setNaturezaSPGCodigo(String naturezaSPGCodigo) {
		NaturezaSPGCodigo = naturezaSPGCodigo;
	}

	public String getNaturezaSPG() {
		return NaturezaSPG;
	}

	public void setNaturezaSPG(String naturezaSPG) {
		NaturezaSPG = naturezaSPG;
	}

	public String getId_NaturezaSPG() {
		return Id_NaturezaSPG;
	}

	public void setId_NaturezaSPG(String id_NaturezaSPG) {
		Id_NaturezaSPG = id_NaturezaSPG;
	}

	public String getOrigemEstado() {
		if( origemEstado == null )
			origemEstado = "";
		if( origemEstado != null && origemEstado.equals("null") )
			origemEstado = "";
		
		return origemEstado;
	}

	public void setOrigemEstado(String origemEstado) {
		this.origemEstado = origemEstado;
	}
	
	public String getProtocoloIntegrado() {
		if( protocoloIntegrado == null )
			protocoloIntegrado = "0";
		return protocoloIntegrado;
	}

	public void setProtocoloIntegrado(String protocoloIntegrado) {
		this.protocoloIntegrado = protocoloIntegrado;
	}

	public String getInfoLocalCertidaoSPG() {
		return infoLocalCertidaoSPG;
	}

	public void setInfoLocalCertidaoSPG(String infoLocalCertidaoSPG) {
		this.infoLocalCertidaoSPG = infoLocalCertidaoSPG;
	}
	
	public String getIdGuiaReferenciaDescontoParcelamento() {
		return idGuiaReferenciaDescontoParcelamento;
	}

	public void setIdGuiaReferenciaDescontoParcelamento(String idGuiaReferenciaDescontoParcelamento) {
		this.idGuiaReferenciaDescontoParcelamento = idGuiaReferenciaDescontoParcelamento;
	}

	public String getTipoGuiaReferenciaDescontoParcelamento() {
		return tipoGuiaReferenciaDescontoParcelamento;
	}

	public void setTipoGuiaReferenciaDescontoParcelamento(String tipoGuiaReferenciaDescontoParcelamento) {
		this.tipoGuiaReferenciaDescontoParcelamento = tipoGuiaReferenciaDescontoParcelamento;
	}

	public String getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(String quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public String getParcelaAtual() {
		return parcelaAtual;
	}

	public void setParcelaAtual(String parcelaAtual) {
		this.parcelaAtual = parcelaAtual;
	}

	public String getPorcentagemDesconto() {
		return porcentagemDesconto;
	}

	public void setPorcentagemDesconto(String porcentagemDesconto) {
		this.porcentagemDesconto = porcentagemDesconto;
	}

	public String getNumeroGuiaReferenciaDescontoParcelamento() {
		return numeroGuiaReferenciaDescontoParcelamento;
	}

	public void setNumeroGuiaReferenciaDescontoParcelamento(String numeroGuiaReferenciaDescontoParcelamento) {
		this.numeroGuiaReferenciaDescontoParcelamento = numeroGuiaReferenciaDescontoParcelamento;
	}

	public GuiaEmissaoDt getGuiaEmissaoDtReferencia() {
		return guiaEmissaoDtReferencia;
	}

	public void setGuiaEmissaoDtReferencia(GuiaEmissaoDt guiaEmissaoDtReferencia) {
		this.guiaEmissaoDtReferencia = guiaEmissaoDtReferencia;
	}

	public GuiaEmissaoDt getGuiaEmissaoDtPrincipal() {
		return guiaEmissaoDtPrincipal;
	}

	public void setGuiaEmissaoDtPrincipal(GuiaEmissaoDt guiaEmissaoDtPrincipal) {
		this.guiaEmissaoDtPrincipal = guiaEmissaoDtPrincipal;
	}

	public String getNumeroProcessoSPG()  {return NumeroProcessoSPG;}
	
	public void setNumeroProcessoSPG(String valor ) {if (valor!=null) NumeroProcessoSPG = valor;}
	
    public String getSituacaoPagamentoSPG()  {return SituacaoPagamentoSPG;}
	
	public void setSituacaoPagamentoSPG(String valor ) {if (valor!=null) SituacaoPagamentoSPG = valor;}

	public boolean possuiOficialAvaliadorPenhoraPartidorOuLeilaoInformado() {
		if(this.getCodigoOficialSPGAvaliador() != null && this.getCodigoOficialSPGAvaliador().trim().length() > 0 && 
		   Funcoes.StringToDouble(this.getAvaliadorValor()) > 0 && Funcoes.StringToInt(this.getAvaliadorQuantidade()) > 0) return true;
		
		if(this.getCodigoOficialSPGPenhora() != null && this.getCodigoOficialSPGPenhora().trim().length() > 0 && 
		   Funcoes.StringToDouble(this.getPenhoraValor()) > 0 && Funcoes.StringToInt(this.getPenhoraQuantidade()) > 0) return true;
		
		if(this.getCodigoOficialSPGPartidor() != null && this.getCodigoOficialSPGPartidor().trim().length() > 0 && 
		   Funcoes.StringToDouble(this.getPartidorValor()) > 0 && Funcoes.StringToInt(this.getPartidorQuantidade()) > 0) return true;
		
		if(this.getCodigoOficialSPGLeilao() != null && this.getCodigoOficialSPGLeilao().trim().length() > 0 && 
		   Funcoes.StringToDouble(this.getLeilaoValor()) > 0 && Funcoes.StringToInt(this.getLeilaoQuantidade()) > 0) return true;	
				
		return false;
	}
	
	public boolean possuiOficialLocomocaoInformado() {
		if(this.getCodigoOficialSPGLocomocao() != null && this.getCodigoOficialSPGLocomocao().trim().length() > 0) return true;
		
		return false;
	}
	
	public boolean isPenhora() {
		return penhora;
	}
	
	public void setPenhora(boolean penhora) {
		this.penhora = penhora;
	}
	
	public void setPenhora(String penhora) {
		if (penhora != null && !penhora.trim().equalsIgnoreCase("null") && penhora.trim().length() > 0)
			this.penhora = penhora.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}

	public boolean isIntimacao() {
		return intimacao;
	}

	public void setIntimacao(boolean intimacao) {
		this.intimacao = intimacao;
	}
	
	public void setIntimacao(String intimacao) {
		if (intimacao != null && !intimacao.trim().equalsIgnoreCase("null") && intimacao.trim().length() > 0)
			this.intimacao = intimacao.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}
	
	public boolean isLocomocaoParaAvaliacao() {
		return Funcoes.StringToInt(this.getFinalidade()) ==	GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO || Funcoes.StringToInt(this.getFinalidade()) ==	GuiaLocomocaoNe.LOCOMOCAO_AVALIACAO_PRACA;
	}
	
	public Map<Integer, Object> getValoresReferenciaCalculoLocomocao() {
		Map<Integer, Object> valoresReferenciaCalculo = new HashMap<Integer, Object>();
		
		if (this.processoDt != null) {
			valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, this.processoDt.getValor());
			valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, this.processoDt.getValor());	
			
			//se processo criminal mas Não é classe REPRESENTACAO CRIMINAL cobra locomoção civel
			if (this.processoDt.getProcessoTipoCodigo() != null && Funcoes.StringToInt(this.processoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.REPRESENTACAO_CRIMINAL_CPP ) {
				valoresReferenciaCalculo.put(CustaDt.IS_LOCOMOCAO_CIVEL, "NAO");
			} else {
				valoresReferenciaCalculo.put(CustaDt.IS_LOCOMOCAO_CIVEL, "SIM");
			}
		}		
		
		//Quantidade de Acrescimo por Pessoa
		if (this.getQuantidadeAcrescimo() != null && this.getQuantidadeAcrescimo().length() > 0 && !this.getQuantidadeAcrescimo().equals("0")) 
			valoresReferenciaCalculo.put(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA, this.getQuantidadeAcrescimo());
		
		return valoresReferenciaCalculo;
	}
	
	public int getValorLocomocaoCodigo() throws Exception {
		int valorLocomocao = 0; //valor default
		
		ProcessoDt processoDt = this.getProcessoDt();
		
		if( processoDt == null && this.getId_Processo() != null && this.getId_Processo().length() > 0 ) {
			processoDt = new ProcessoNe().consultarProcessoSimplificado(this.getId_Processo());
		}
		
		if (processoDt != null) {
			if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.VARA ) {
				
				if( processoDt.isCivel() ) {
					valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CIVEL;									
				}
				else {
					if( processoDt.isCriminal() ) {
						valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CRIMINAL;
					}
				}
				
			} else if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU ) {
					valorLocomocao = GuiaCalculoNe.LOCOMOCAO_SEGUNDO_GRAU;				
			}
		}		
		
		Map<Integer, Object> valoresReferenciaCalculo = this.getValoresReferenciaCalculoLocomocao();
		
		//Na guia de recurso inominado é cobrado os valores de locomoção como sendo de criminal.
		if( valoresReferenciaCalculo.get(GuiaRecursoInominadoNe.VARIAVEL_GUIA_RECURSO_INOMINADO) != null && valoresReferenciaCalculo.get(GuiaRecursoInominadoNe.VARIAVEL_GUIA_RECURSO_INOMINADO).toString().equals(GuiaRecursoInominadoNe.VALOR_GUIA_RECURSO_INOMINADO) ) {
			valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CRIMINAL;
		}
		
		//PONTO (A)
		//Guia recurso inominado queixa-crime ï¿½ sï¿½ oficial
		if( valoresReferenciaCalculo.get(CustaDt.GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE) != null && valoresReferenciaCalculo.get(CustaDt.GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE).toString().equals("SIM") ) {
			valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CIVEL;
		}
		
		//PONTO (B)
		//Guia Execução Queixa-Crime
		if( valoresReferenciaCalculo.get(CustaDt.GUIA_EXECUCAO_QUEIXA_CRIMINE) != null && valoresReferenciaCalculo.get(CustaDt.GUIA_EXECUCAO_QUEIXA_CRIMINE).toString().equals("SIM") ) {
			valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CIVEL;
		}
		
		//*****************************************************
		//*****************************************************
		//Alteração para a ocorrência 2013/76036 e redmine #1759
		//Este trecho reforça para o PONTO (A) e (B) acima.
		//*****************************************************
		if (this.getGuiaModeloDt() != null && this.getGuiaModeloDt().getId_GuiaTipo() != null) {
			GuiaModeloDt guiaEmissaoDt = this.getGuiaModeloDt();
			if( guiaEmissaoDt.isIdGuiaTipo( GuiaTipoDt.ID_RECURSO_INOMINADO_QUEIXA_CRIME)		||			 guiaEmissaoDt.isIdGuiaTipo(  GuiaTipoDt.ID_FINAL_EXECUCAO_QUEIXA_CRIME )) {				
				valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CIVEL;
			}
			
			if( guiaEmissaoDt.isIdGuiaTipo(GuiaTipoDt.ID_LOCOMOCAO )			||			guiaEmissaoDt.isIdGuiaTipo( GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR )) {
				
				if( valoresReferenciaCalculo.get(CustaDt.IS_LOCOMOCAO_CIVEL) != null && valoresReferenciaCalculo.get(CustaDt.IS_LOCOMOCAO_CIVEL).equals("SIM") ) {
					valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CIVEL;
					if( Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU ) {
						valorLocomocao = GuiaCalculoNe.LOCOMOCAO_SEGUNDO_GRAU;
					}
				}
				else {
					valorLocomocao = GuiaCalculoNe.LOCOMOCAO_CRIMINAL;
				}
			}
		}						
		//*****************************************************
		//*****************************************************
		
		return valorLocomocao;
	}
	
	public boolean processoPermiteLocomocaoContaVinculada() {
		if (this.processoDt != null && this.processoDt.getProcessoTipoCodigo() != null && !this.processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_FISCAL))) {
			return true;
		}		
		return false;
	}

	public boolean isOficialCompanheiro() {
		return oficialCompanheiro;
	}

	public void setOficialCompanheiro(boolean oficialCompanheiro) {
		this.oficialCompanheiro = oficialCompanheiro;
	}
	
	public void setOficialCompanheiro(String oficialCompanheiro) {
		if (oficialCompanheiro != null && !oficialCompanheiro.trim().equalsIgnoreCase("null") && oficialCompanheiro.trim().length() > 0)
			this.oficialCompanheiro = oficialCompanheiro.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}

	public boolean isForaHorarioNormal() {
		return foraHorarioNormal;
	}

	public void setForaHorarioNormal(boolean foraHorarioNormal) {
		this.foraHorarioNormal = foraHorarioNormal;
	}
	
	public void setForaHorarioNormal(String foraHorarioNormal) {
		if (foraHorarioNormal != null && !foraHorarioNormal.trim().equalsIgnoreCase("null") && foraHorarioNormal.trim().length() > 0)
			this.foraHorarioNormal = foraHorarioNormal.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}

	public boolean isCitacaoHoraCerta() {
		return citacaoHoraCerta;
	}

	public void setCitacaoHoraCerta(boolean citacaoHoraCerta) {
		this.citacaoHoraCerta = citacaoHoraCerta;
	}
	
	public void setCitacaoHoraCerta(String citacaoHoraCerta) {
		if (citacaoHoraCerta != null && !citacaoHoraCerta.trim().equalsIgnoreCase("null") && citacaoHoraCerta.trim().length() > 0)
			this.citacaoHoraCerta = citacaoHoraCerta.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}

	public boolean isLocomocaoComplementar() {
		return locomocaoComplementar;
	}

	public void setLocomocaoComplementar(boolean locomocaoComplementar) {
		this.locomocaoComplementar = locomocaoComplementar;
	}
	
	public boolean isGuiaLocomocao() {
		return ( this.getGuiaModeloDt() != null &&
				 ( 	this.getGuiaModeloDt().isIdGuiaTipo( GuiaTipoDt.ID_LOCOMOCAO ) ||
						 this.getGuiaModeloDt().isIdGuiaTipo( GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR ) ));
	}
	
	public boolean isGuiaSomenteItensLocomocao() {
		return new GuiaSPGNe().possuiSomenteItensLocomocao(this.getListaGuiaItemDt());
	}
	
	public boolean possuiLocomocao() {
		if (this.isGuiaLocomocao()) return true;
		
		if (getListaGuiaItemDt() == null) return false;
		
		for(GuiaItemDt guiaItemTemp : getListaGuiaItemDt()) {
			if (guiaItemTemp.getLocomocaoDt() != null) return true;
		}
		
		return false;
	}
	
	public boolean isProcessoExecucaoFiscal() {
		return (this.processoDt != null && 
				this.processoDt.getProcessoTipoCodigo() != null && 
				this.processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_FISCAL)));
	}
	
	public List<GuiaItemDt> getListaGuiaItemDtAgrupadosImpressao(List<GuiaItemDt> listaGuiaItemDt) {
		if (!this.isGuiaLocomocao()) return listaGuiaItemDt;
		
		SortedMap<String, GuiaItemDt> dicGuiaItem = new TreeMap<String, GuiaItemDt>();
		
		for(GuiaItemDt guiaItemDt : listaGuiaItemDt) {
			String chaveTemp = guiaItemDt.getCustaDt().getId() + "|" + guiaItemDt.getValorReferencia();
			if (guiaItemDt.getLocomocaoDt() != null && guiaItemDt.getLocomocaoDt().getBairroDt() != null) {
				chaveTemp += "|" + guiaItemDt.getLocomocaoDt().getBairroDt().getId();
			}
			
			//Verifica se já existe a data da interrupção no dicionário...
			GuiaItemDt guiaItemAgrupadoDt = dicGuiaItem.get(chaveTemp);	
			if (guiaItemAgrupadoDt == null){				
				dicGuiaItem.put(chaveTemp, guiaItemDt.clonar());
			} else {
				Double quantidadeAtualizada = Funcoes.StringToDouble(guiaItemAgrupadoDt.getQuantidade()) + Funcoes.StringToDouble(guiaItemDt.getQuantidade());
				guiaItemAgrupadoDt.setQuantidade(Funcoes.retirarCasasDecimais(quantidadeAtualizada).toString());
				
				if (Funcoes.StringToDouble(guiaItemAgrupadoDt.getValorCalculadoOriginal()) > 0 || Funcoes.StringToDouble(guiaItemDt.getValorCalculadoOriginal()) > 0) {
					Double valorCalculadoOriginalAtualizado = Funcoes.StringToDouble(guiaItemAgrupadoDt.getValorCalculadoOriginal()) + Funcoes.StringToDouble(guiaItemDt.getValorCalculadoOriginal());
					Double valorCalculadoAtualizado = Funcoes.StringToDouble(guiaItemAgrupadoDt.getValorCalculado()) + Funcoes.StringToDouble(guiaItemDt.getValorCalculado());
					
					guiaItemAgrupadoDt.setValorCalculadoOriginal(Funcoes.retirarCasasDecimais(valorCalculadoOriginalAtualizado).toString());					
					guiaItemAgrupadoDt.setValorCalculado(Funcoes.retirarCasasDecimais(valorCalculadoAtualizado).toString());
				} else {
					guiaItemAgrupadoDt.recalculeValor();	
				}
			}
		}		
		
		List<GuiaItemDt> listaGuiaItemAgrupadoDt = new ArrayList<GuiaItemDt>(dicGuiaItem.values());		
		
		this.ordeneListaGuiaItemDt(listaGuiaItemAgrupadoDt);
		
		return listaGuiaItemAgrupadoDt;
	}
	
	public void ordeneListaGuiaItemDt(List<GuiaItemDt> listaGuiaItemDt) {
		//Sorting
		Collections.sort(listaGuiaItemDt, new Comparator<GuiaItemDt>() {
	        @Override
	        public int compare(GuiaItemDt guiaItem1, GuiaItemDt guiaItem2)
	        {
	        	String custa1 = "";
	        	String custa2 = "";
	        	
	        	if (guiaItem1.getCusta() != null && guiaItem1.getCusta().trim().length() > 0) custa1 = guiaItem1.getCusta().trim();
	        	
	        	if (guiaItem2.getCusta() != null && guiaItem2.getCusta().trim().length() > 0) custa2 = guiaItem2.getCusta().trim();
	        	
	        	if (guiaItem1.getCustaDt() != null) {
	        		if (guiaItem1.getCustaDt().getCusta() != null && guiaItem1.getCustaDt().getCusta().trim().length() > 0) custa1 = guiaItem1.getCustaDt().getCusta().trim();	        			        			   
	        	}
	        	
	        	if (guiaItem2.getCustaDt() != null) {
	        		if (guiaItem2.getCustaDt().getCusta() != null && guiaItem2.getCustaDt().getCusta().trim().length() > 0) custa2 = guiaItem2.getCustaDt().getCusta().trim();	        		
	        	}
	        	
	            return custa1.compareTo(custa2);	            
	        }
	    });
	}
	
	public void ordeneListaGuiaItemDtCodigoArrecadacao(List<GuiaItemDt> listaGuiaItemDt) {
		//Sorting
		Collections.sort(listaGuiaItemDt, new Comparator<GuiaItemDt>() {
	        @Override
	        public int compare(GuiaItemDt guiaItem1, GuiaItemDt guiaItem2)
	        {
	        	String custa1 = "";
	        	String custa2 = "";
	        	
	        	if (guiaItem1.getCustaDt() != null) {
	        		if (guiaItem1.getCustaDt().getCodigoArrecadacao() != null && guiaItem1.getCustaDt().getCodigoArrecadacao().trim().length() > 0) custa1 = guiaItem1.getCustaDt().getCodigoArrecadacao().trim();	        			   
	        	}
	        	
	        	if (guiaItem2.getCustaDt() != null) {
	        		if (guiaItem2.getCustaDt().getCodigoArrecadacao() != null && guiaItem2.getCustaDt().getCodigoArrecadacao().trim().length() > 0) custa2 = guiaItem2.getCustaDt().getCodigoArrecadacao().trim();
	        	}
	        	
	            return custa1.compareTo(custa2);	            
	        }
	    });
	}	

	public List<LocomocaoDt> getListaLocomocaoNaoUtilizadaDt() {
		return listaLocomocaoNaoUtilizadaDt;
	}

	public void setListaLocomocaoNaoUtilizadaDt(List<LocomocaoDt> listaLocomocaoNaoUtilizadaDt) {
		this.listaLocomocaoNaoUtilizadaDt = listaLocomocaoNaoUtilizadaDt;
	}

	public boolean isFinalidadeCitacaoPenhoraPracaLeilao() {
		return this.getFinalidade() != null && this.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_E_PRACA_LEILAO));
	}
	
	public boolean isFinalidadeCitacaoPenhoraAvaliacaoPracaLeilao() {
		return this.getFinalidade() != null && this.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO));
	}
	
	public boolean isFinalidadeCitacaoPenhoraAvaliacaoAlienacao() {
		return this.getFinalidade() != null && this.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO));
	}
	
	public boolean isFinalidadeLocomocao() {
		return this.getFinalidade() != null && this.getFinalidade().equals(String.valueOf(GuiaLocomocaoNe.LOCOMOCAO));
	}

	public boolean isGuiaEmitidaSPG() {
		return guiaEmitidaSPG;
	}

	public void setGuiaEmitidaSPG(boolean guiaEmitidaSPG) {
		this.guiaEmitidaSPG = guiaEmitidaSPG;
	}
	
	public boolean isGuiaComplementarSPG() {
		return this.isGuiaEmitidaSPG() && (this.getId_GuiaEmissaoPrincipal() != null && this.getId_GuiaEmissaoPrincipal().trim().length() > 0);
	}
	
	public boolean isGuiaComplementarSSG() {
		return this.isGuiaEmitidaSSG() && (this.getId_GuiaEmissaoPrincipal() != null && this.getId_GuiaEmissaoPrincipal().trim().length() > 0);
	}
	
	public boolean isGuiaEmitidaSSG() {
		return guiaEmitidaSSG;
	}

	public void setGuiaEmitidaSSG(boolean guiaEmitidaSSG) {
		this.guiaEmitidaSSG = guiaEmitidaSSG;
	}
	
	public void atualizeNumeroESeriePeloNumeroCompleto() {
		if( this.numeroGuiaCompleto != null) {
			if (this.numeroGuiaCompleto.length() > 2) {
				this.numeroGuia = numeroGuiaCompleto.substring(0, numeroGuiaCompleto.length() - 2);
				this.numeroGuiaSerie = numeroGuiaCompleto.substring(numeroGuiaCompleto.length() - 2, numeroGuiaCompleto.length());	
			} else {
				// Inconsistência na base do SPG...
				this.numeroGuia = this.numeroGuiaCompleto;
				this.numeroGuiaSerie = "00";
			}
		} 
	}
	
	/**
	 * Método para verificar se a guia está paga pelo id da guia status
	 * @return boolean
	 */
	public boolean isGuiaPaga() {
		boolean retorno = false;
		
		if( this.getId_GuiaStatus() != null && this.getId_GuiaStatus().length() > 0 ) {
			
			if( Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.PAGO || 
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.PAGO_COM_VALOR_INFERIOR ||	
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR ||
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.PAGO_APOS_VENCIMENTO ||
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.PAGA_CANCELADA ||
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.CANCELADA_PAGA ||
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.GUIA_COMPLEMENTAR_GERADA_PAGA ||
				Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.PARCELAMENTO_PAGO ) {
				
				retorno = true;
				
			}
			
		}
	
		return retorno;
	}
	
	public boolean isGuiaAguardandoPagamento() {
		boolean retorno = false;
		
		if( this.getId_GuiaStatus() != null && this.getId_GuiaStatus().length() > 0 ) {			
			if(Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.AGUARDANDO_PAGAMENTO ||
			   Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.ESTORNO_BANCARIO) {				
				retorno = true;				
			}			
		}
	
		return retorno;
	}
	
	public boolean isGuiaCancelada() {
		boolean retorno = false;
		
		if( this.getId_GuiaStatus() != null && this.getId_GuiaStatus().length() > 0 ) {			
			if( Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.CANCELADA ) {
				retorno = true;
			}
		}
	
		return retorno;
	}
	
	public boolean isGuiaFinal() {
		boolean retorno = false;
		
		int id_guia_tipo = Funcoes.StringToInt(this.getId_GuiaTipo());
		if (id_guia_tipo == 0 && this.getGuiaModeloDt() != null && this.getGuiaModeloDt().getId_GuiaTipo() != null){
			id_guia_tipo = Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo());
		}
		
		if( id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_FINAL) || 
			id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_FINAL_EXECUCAO_QUEIXA_CRIME) ||
			id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_FINAL_EXECUCAO_SENTENCA) ||
			id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_FINAL_ZERO) ||
		    id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_FAZENDA_MUNICIPAL) ||
		    id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA) ||
		    id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA)) {			
			retorno = true;				
		}
		
		return retorno;
	}
	
	public boolean isGuiaGRSGenerica() {
		boolean retorno = false;
		
		int id_guia_tipo = Funcoes.StringToInt(this.getId_GuiaTipo());
		if (id_guia_tipo == 0 && this.getGuiaModeloDt() != null && this.getGuiaModeloDt().getId_GuiaTipo() != null){
			id_guia_tipo = Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo());
		}
		
		if( id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_GUIA_GENERICA) ) {			
			retorno = true;				
		}
		
		return retorno;
	}
	
	public boolean isGuiaCertidaoPraticaForense() {
		boolean retorno = false;
		
		int id_guia_tipo = Funcoes.StringToInt(this.getId_GuiaTipo());
		if (id_guia_tipo == 0 && this.getGuiaModeloDt() != null && this.getGuiaModeloDt().getId_GuiaTipo() != null){
			id_guia_tipo = Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo());
		}
		
		if( id_guia_tipo == Funcoes.StringToInt(GuiaTipoDt.ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE)) {			
			retorno = true;				
		}
		
		return retorno;
	}
	
	public boolean isGuiaFinalSPG() {
		boolean retorno = false;
		
		if( this.getId_GuiaTipo() != null && this.getId_GuiaTipo().length() > 0 ) {			
			if( Funcoes.StringToInt(this.getId_GuiaTipo()) == 5 || //SPG - Final Fazenda Pública 
				Funcoes.StringToInt(this.getId_GuiaTipo()) == 7 || //SPG - Final  
				Funcoes.StringToInt(this.getId_GuiaTipo()) == 8) { //SPG - Final Zero
				
				retorno = true;				
			}			
		}
	
		return retorno;
	}
	
	public boolean isGuiaParcelada() {
		if( this.getTipoGuiaReferenciaDescontoParcelamento() != null && this.getTipoGuiaReferenciaDescontoParcelamento().equals(GuiaEmissaoDt.TIPO_GUIA_PARCELADA) ) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Método para verificar se esta guia está livre para fazer o parcelamento ou desconto.
	 * 
	 * @return true, se pode utilizar
	 */
	public boolean isGuiaLivreParaUsoDescontoParcelamento() {
		return true;
	}
	
	/**
	 * Método para verificar se o número do processo dependente está preenchido.
	 * 
	 * @return boolean
	 */
	public boolean isNumeroProcessoDependentePreenchido() {
		
		if( this.numeroProcessoDependente != null && this.numeroProcessoDependente.trim().length() > 5 ) {
			return true;
		}
		
		return false;
	}
	
	public boolean isGuiaEmitidaSPGCapital() {
		return guiaEmitidaSPGCapital;
	}

	public void setGuiaEmitidaSPGCapital(boolean guiaEmitidaSPGCapital) {
		this.guiaEmitidaSPGCapital = guiaEmitidaSPGCapital;
	}

	public String getDataApresentacaoSPG() {
		return dataApresentacaoSPG;
	}

	public void setDataApresentacaoSPG(String dataApresentacaoSPG) {
		if (dataApresentacaoSPG != null)
			this.dataApresentacaoSPG = dataApresentacaoSPG;
	}
	
	public String getCodigoOabSPG() {
		return codigoOabSPG;
	}

	public void setCodigoOabSPG(String codigoOabSPG) {
		if (codigoOabSPG != null)
			this.codigoOabSPG = codigoOabSPG;
	}

	public String getDataIniCertidaoSPG() {
		return dataIniCertidaoSPG;
	}

	public void setDataIniCertidaoSPG(String dataIniCertidaoSPG) {
		if (dataIniCertidaoSPG != null)
			this.dataIniCertidaoSPG = dataIniCertidaoSPG;
	}

	public String getDataFimCertidaoSPG() {
		return dataFimCertidaoSPG;
	}

	public void setDataFimCertidaoSPG(String dataFimCertidaoSPG) {
		if (dataFimCertidaoSPG != null)
			this.dataFimCertidaoSPG = dataFimCertidaoSPG;
	}

	public String getTipoPessoaSPG() {
		return tipoPessoaSPG;
	}

	public void setTipoPessoaSPG(String tipoPessoaSPG) {
		if (tipoPessoaSPG != null)
			this.tipoPessoaSPG = tipoPessoaSPG;
	}
	
	public String getInfoPatenteSPG() {
		if( infoPatenteSPG == null ) {
			infoPatenteSPG = "";
		}
		return infoPatenteSPG;
	}

	public void setInfoPatenteSPG(String infoPatenteSPG) {
		this.infoPatenteSPG = infoPatenteSPG;
	}
	
	public String getTipoGuiaCertidaoSPG() {
		if( tipoGuiaCertidaoSPG == null ) {
			tipoGuiaCertidaoSPG = "";
		}
		return tipoGuiaCertidaoSPG;
	}

	public void setTipoGuiaCertidaoSPG(String tipoGuiaCertidaoSPG) {
		this.tipoGuiaCertidaoSPG = tipoGuiaCertidaoSPG;
	}

	public boolean isPessoalFisicaSPG() {		
		if (tipoPessoaSPG != null) return tipoPessoaSPG.equalsIgnoreCase("F");
		return false;
	}
	
	public boolean isGuiaCertidaoSPG() {
		return ( this.getGuiaModeloDt() != null && Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo()) == 3);
	}
	
	public boolean isGuiaTipoDevolucaoSPG() {
		return ( this.getId_GuiaTipo().equals("5") 
				|| 
				( this.getGuiaModeloDt() != null && Funcoes.StringToInt(this.getGuiaModeloDt().getId_GuiaTipo()) == 5)
				);
	}
	
	/**
	 * Método para validar se a guia GRS do SPG é de precatórios municipal, estadual ou federal.
	 * Valida se é série 02 e tipo SPG 9.
	 * 
	 * @return boolean
	 * @author fasoares
	 */
	public boolean isGuiaGRSPrecatorioSPG() {
		if( this.isSerie02() ) {
			if( this.getId_GuiaTipo() != null && this.getId_GuiaTipo().equals("9") ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isGuiaInicial1Grau(){
		if (getCodigoGrau()!=null && getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU))
			return true;
		return false;
	}
	
	public boolean isGuiaInicial2Grau(){
		if (getCodigoGrau()!=null && getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU))
			return true;
		return false;
	}
	
	//Ocorrência 2019/6291
	//ATENÇÃO: Somente utilizar na guia inicial
	//NÃO UTILIZAR EM OUTRA GUIA
	//Ocorrencia 2021/2039
	//Mudei para pesquisar somente "Mandado de Segurança Cível" e criei os modelos para as guias necessárias de primeiro grau. PS.: quando for turma, é gerado os itens pelo método.
	public boolean isGuiaInicialTurmaRecursal() {
		if( getCodigoGrau()!=null && getCodigoGrau().equals(GuiaTipoDt.TURMA_RECURSAL) )
			return true;
		return false;
	}
	
	public boolean isAreaCivil(){
		if (getCodigoArea()!=null && getCodigoArea().equals(AreaDt.CIVEL))
			return true;
		return false;
	}

	public boolean isAreaCriminal(){
		if (getCodigoArea()!=null && getCodigoArea().equals(AreaDt.CRIMINAL))
			return true;
		return false;
	}
	
	public boolean isIdGuiaTipo(String guiaTipo) {
		if (getGuiaModeloDt()!=null && getGuiaModeloDt().equals(guiaTipo))
			return true;
		return false;
	}

	public String getMetadadosGuia() {
		return metadadosGuia;
	}

	public void setMetadadosGuia(String metadadosGuia) {
		if(metadadosGuia != null){
			this.metadadosGuia = metadadosGuia;
		}
	}
	
	public boolean isGuiaVencida() throws Exception {
		TJDataHora dataAtual = new TJDataHora();
		dataAtual.atualizeUltimaHoraDia();
		TJDataHora vencimentoBoleto = new TJDataHora(Funcoes.Stringyyyy_MM_ddToDateTime(getDataVencimento()));
		vencimentoBoleto.atualizeUltimaHoraDia();
		return dataAtual.ehApos(vencimentoBoleto);
	}
	
	public boolean podeGerarBoleto() throws Exception {
		if (isGuiaPaga()) return false;
		if (!isGuiaVencida()) return true;
		return isSerie10(); //Somente série 10 pode gerar o boleto depois de vencido
	}
	
	public boolean isSerie10() {
		if (numeroGuiaSerie == null) return false;
		return numeroGuiaSerie.trim().equals("10");
	}
	
	public boolean isSerie50() {
		if (numeroGuiaSerie == null) return false;
		return numeroGuiaSerie.trim().equals("50");
	}
	
	public boolean isSerie02() {
		if (numeroGuiaSerie == null) return false;
		return Funcoes.StringToInt(numeroGuiaSerie) == 2;
	}
	
	public boolean isSerie06() {
		if (numeroGuiaSerie == null) return false;
		return Funcoes.StringToInt(numeroGuiaSerie) == 6;
	}
	
	public boolean isSerie09() {
		if (numeroGuiaSerie == null) return false;
		return Funcoes.StringToInt(numeroGuiaSerie) == 9;
	}
	
	public String getCertidaoAcordao() {
		if( certidaoAcordao == null ) {
			certidaoAcordao = "0";
		}
		return certidaoAcordao;
	}

	public void setCertidaoAcordao(String certidaoAcordao) {
		this.certidaoAcordao = certidaoAcordao;
	}

	public String getDesarquivamento() {
		if( desarquivamento == null ) {
			desarquivamento = "0";
		}
		return desarquivamento;
	}

	public void setDesarquivamento(String desarquivamento) {
		this.desarquivamento = desarquivamento;
	}
	
	public String getDesarquivamento16II() {
		if( desarquivamento16II == null ) {
			desarquivamento16II = "0";
		}
		return desarquivamento16II;
	}

	public void setDesarquivamento16II(String desarquivamento16II) {
		this.desarquivamento16II = desarquivamento16II;
	}
	
	public String getRestauracao() {
		if( restauracao == null ) {
			restauracao = "0";
		}
		return restauracao;
	}

	public void setRestauracaoAtos(String restauracaoAtos) {
		this.restauracaoAtos = restauracaoAtos;
	}
	
	public String getRestauracaoAtos() {
		if( restauracaoAtos == null ) {
			restauracaoAtos = "0";
		}
		return restauracaoAtos;
	}

	public void setRestauracao(String restauracao) {
		this.restauracao = restauracao;
	}

	public String getCorreioQuantidadeReg4VI() {
		if( correioQuantidadeReg4VI == null ) {
			correioQuantidadeReg4VI = "0";
		}
		return correioQuantidadeReg4VI;
	}

	public void setCorreioQuantidadeReg4VI(String correioQuantidadeReg4VI) {
		this.correioQuantidadeReg4VI = correioQuantidadeReg4VI;
	}

	public String getEmissaoDocumentoQuantidade() {
		if( emissaoDocumentoQuantidade == null ) {
			emissaoDocumentoQuantidade = "0";
		}
		return emissaoDocumentoQuantidade;
	}

	public void setEmissaoDocumentoQuantidade(String emissaoDocumentoQuantidade) {
		this.emissaoDocumentoQuantidade = emissaoDocumentoQuantidade;
	}
	
	public String getEmissaoDocumentoQuantidade16VII() {
		if( emissaoDocumentoQuantidade16VII == null ) {
			emissaoDocumentoQuantidade16VII = "0";
		}
		return emissaoDocumentoQuantidade16VII;
	}

	public void setEmissaoDocumentoQuantidade16VII(String emissaoDocumentoQuantidade16VII) {
		this.emissaoDocumentoQuantidade16VII = emissaoDocumentoQuantidade16VII;
	}

	public String getAtosConstricao() {
		if( atosConstricao == null ) {
			atosConstricao = "0";
		}
		return atosConstricao;
	}

	public void setAtosConstricao(String atosConstricao) {
		this.atosConstricao = atosConstricao;
	}
	
	public String getAtosConstricao16VIII() {
		if( atosConstricao16VIII == null ) {
			atosConstricao16VIII = "0";
		}
		return atosConstricao16VIII;
	}

	public void setAtosConstricao16VIII(String atosConstricao16VIII) {
		this.atosConstricao16VIII = atosConstricao16VIII;
	}

	public String getCertidaoDecisao() {
		if( certidaoDecisao == null ) {
			certidaoDecisao = "0";
		}
		return certidaoDecisao;
	}

	public void setCertidaoDecisao(String certidaoDecisao) {
		this.certidaoDecisao = certidaoDecisao;
	}

	public String getDocumentoPublicadoQuantidade() {
		if( documentoPublicadoQuantidade == null )
			documentoPublicadoQuantidade = "0";
		return documentoPublicadoQuantidade;
	}

	public void setDocumentoPublicadoQuantidade(String documentoPublicadoQuantidade) {
		this.documentoPublicadoQuantidade = documentoPublicadoQuantidade;
	}
	
	public String getDocumentoDiarioJustica() {
		if( documentoDiarioJustica == null ) {
			documentoDiarioJustica = "0";
		}
		return documentoDiarioJustica;
	}

	public void setDocumentoDiarioJustica(String documentoDiarioJustica) {
		this.documentoDiarioJustica = documentoDiarioJustica;
	}
	
	public String getPorteRemessaQuantidade16V() {
		if( PorteRemessaQuantidade16V == null ) {
			PorteRemessaQuantidade16V = "0";
		}
		return PorteRemessaQuantidade16V;
	}

	public void setPorteRemessaQuantidade16V(String porteRemessaQuantidade16V) {
		PorteRemessaQuantidade16V = porteRemessaQuantidade16V;
	}
	
	public String getTaxaJudiciariaServicoCartaArrematacao() {
		if( TaxaJudiciariaServicoCartaArrematacao == null ) {
			TaxaJudiciariaServicoCartaArrematacao = "0";
		}
		return TaxaJudiciariaServicoCartaArrematacao;
	}

	public void setTaxaJudiciariaServicoCartaArrematacao(String taxaJudiciariaServicoCartaArrematacao) {
		TaxaJudiciariaServicoCartaArrematacao = taxaJudiciariaServicoCartaArrematacao;
	}
	
	public String getTaxaJudiciariaServicoCertidao() {
		if( TaxaJudiciariaServicoCertidao == null ) {
			TaxaJudiciariaServicoCertidao = "0";
		}
		return TaxaJudiciariaServicoCertidao;
	}

	public void setTaxaJudiciariaServicoCertidao(String taxaJudiciariaServicoCertidao) {
		TaxaJudiciariaServicoCertidao = taxaJudiciariaServicoCertidao;
	}
	
	public String getFormalPartilhaCartaQuantidade16IX() {
		if( FormalPartilhaCartaQuantidade16IX == null ) {
			FormalPartilhaCartaQuantidade16IX = "0";
		}
		return FormalPartilhaCartaQuantidade16IX;
	}

	public void setFormalPartilhaCartaQuantidade16IX(String formalPartilhaCartaQuantidade16IX) {
		FormalPartilhaCartaQuantidade16IX = formalPartilhaCartaQuantidade16IX;
	}

	public String getCumprimentoPrecatoria() {
		if( cumprimentoPrecatoria == null ) {
			cumprimentoPrecatoria = "0";
		}
		return cumprimentoPrecatoria;
	}

	public void setCumprimentoPrecatoria(String cumprimentoPrecatoria) {
		this.cumprimentoPrecatoria = cumprimentoPrecatoria;
	}

	public String getNumeroCompletoProcesso() {
		return numeroCompletoProcesso;
	}

	public void setNumeroCompletoProcesso(String numeroCompletoProcesso) {
		if (numeroCompletoProcesso != null) this.numeroCompletoProcesso = numeroCompletoProcesso;
	}

	public boolean isGrauGuiaInicialMesmoGrauProcessoDependente() {
		return grauGuiaInicialMesmoGrauProcessoDependente;
	}

	public void setGrauGuiaInicialMesmoGrauProcessoDependente(
			boolean grauGuiaInicialMesmoGrauProcessoDependente) {
		this.grauGuiaInicialMesmoGrauProcessoDependente = grauGuiaInicialMesmoGrauProcessoDependente;
	}
	
	public boolean isGuiaBaixadaComAssistencia() {
		
		if( this.getId_GuiaStatus() != null && this.getId_GuiaStatus().length() > 0 ) {			
			if( Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.BAIXADA_COM_ASSISTENCIA ) {				
				return true;				
			}			
		}
		
		return false;
	}
	
	public boolean isGuiaAguardandoDeferimento() {
		boolean retorno = false;
		
		if( this.getId_GuiaStatus() != null && this.getId_GuiaStatus().length() > 0 ) {			
			if( Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.AGUARDANDO_DEFERIMENTO) {				
				retorno = true;				
			}			
		}
	
		return retorno;
	}
	
	public boolean isGuiaPagamentoEstornadoBanco() {
		boolean retorno = false;
		
		if( this.getId_GuiaStatus() != null && this.getId_GuiaStatus().length() > 0 ) {			
			if( Funcoes.StringToInt(this.getId_GuiaStatus()) == GuiaStatusDt.ESTORNO_BANCARIO) {				
				retorno = true;				
			}			
		}
	
		return retorno;
	}
	
	public boolean isGuiaBloqueadaParaEmissaoDeGuiaEBoleto() {
		if (this.isGuiaEmitidaSPG() || this.isGuiaEmitidaSPGCapital() || this.isGuiaEmitidaSSG()) {
			if (this.isGuiaComplementarSPG() || this.isGuiaComplementarSSG() || this.isGuiaFinalSPG()) return true;			
		}
		
		if (this.isGrauGuiaInicialMesmoGrauProcessoDependente() || this.isGuiaFinal() || this.isGuiaInicial1Grau() || this.isGuiaInicial2Grau())
			return true;
		
		return false;
	}
	
	public boolean isOrigemEstado() {
		if( this.getOrigemEstado() != null && this.getOrigemEstado().equals(GuiaEmissaoDt.VALOR_SIM) ) {
			return true;
		}
		return false;
	}
	
	public boolean isProtocoloIntegrado() {
		if( this.getProtocoloIntegrado() != null && this.getProtocoloIntegrado().equals(GuiaEmissaoDt.VALOR_SIM) ) {
			return true;
		}
		return false;
	}	
	
	public String getNomePrimeiroAutor() {		
		return nomePrimeiroAutor;
	}
	
	public void setNomePrimeiroAutor(String nomePrimeiroAutor) {
		this.nomePrimeiroAutor = nomePrimeiroAutor;
	}

	public String getNumrCpfCgc() {
		return numrCpfCgc;
	}
	
	public void setNumrCpfCgc(String numrCpfCgc) {
		this.numrCpfCgc = numrCpfCgc;
	}
	
	public String getTipoGuia() {
		return tipoGuia;
	}
	
	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}
	
	public String getServentiaCodigo() {
		return serventiaCodigo;
	}
	
	public void setServentiaCodigo(String serventiaCodigo) {
		this.serventiaCodigo = serventiaCodigo;
	}
	
	public String getProcessoTipoCodNatureza() {
		return processoTipoCodNatureza;
	}
	
	public void setProcessoTipoCodNatureza(String processoTipoCodNatureza) {
		this.processoTipoCodNatureza = processoTipoCodNatureza;
	}
	
	public String getCustaCertidao() {
		return custaCertidao;
	}
	
	public void setCustaCertidao(String custaCertidao) {
		this.custaCertidao = custaCertidao;
	}
	
	public String getCustaTaxaJudiciaria() {
		return custaTaxaJudiciaria;
	}
	
	public void setCustaTaxaJudiciaria(String custaTaxaJudiciaria) {
		this.custaTaxaJudiciaria = custaTaxaJudiciaria;
	}
	
	public String getCustaTotal() {
		return custaTotal;
	}
	
	public void setCustaTotal(String custaTotal) {
		this.custaTotal = custaTotal;
	}
	
	public String getModoEmissaoCertidaoSPG() {
		return modoEmissaoCertidaoSPG;
	}
	
	public void setModoEmissaoCertidaoSPG(String modoEmissaoCertidaoSPG) {
		this.modoEmissaoCertidaoSPG = modoEmissaoCertidaoSPG;
	}
	
	public boolean isValorAcaoNegativo() {
		if( ( this.getValorAcao() != null && Funcoes.StringToDouble(this.getValorAcao()) < 0D )
			||
			( this.getNovoValorAcaoAtualizado() != null && Funcoes.StringToDouble(this.getNovoValorAcaoAtualizado()) < 0D )
			||
			( this.getNovoValorAcao() != null && Funcoes.StringToDouble(this.getNovoValorAcao()) < 0D )
			)
		{
			return true;
		}
		
		return false;
	}

	public String getInfoAreaSPG() {
		return infoAreaSPG;
	}

	public void setInfoAreaSPG(String infoAreaSPG) {
		this.infoAreaSPG = infoAreaSPG;
	}
}