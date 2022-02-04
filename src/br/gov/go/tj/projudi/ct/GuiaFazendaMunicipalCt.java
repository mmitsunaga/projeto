package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.BairroLocomocaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaFazendaMunicipalDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaFazendaMunicipalNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaFazendaMunicipalCt extends Controle {

	private static final long serialVersionUID = -2446623657473259715L;
	
	private static final String PAGINA_GUIA_FAZENDA_MUNICIPAL 	= "/WEB-INF/jsptjgo/GuiaFazendaMunicipal.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_LOCALIZAR 				= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	private static final String NOME_CONTROLE_WEB_XML = "GuiaFazendaMunicipal";
	
	private GuiaFazendaMunicipalNe guiaFazendaMunicipalNe = new GuiaFazendaMunicipalNe();

	@Override
	public int Permissao() {
		return GuiaFazendaMunicipalDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 			= null;
		int passoEditar 		= -1;
		List tempList 			= null;		
		String stId 			= "";
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		stAcao = PAGINA_GUIA_FAZENDA_MUNICIPAL;
		
		//********************************************
		// Variáveis utilizadas pela página
		List listaCustaDt 		= null;
		List<BairroLocomocaoDt> listaBairroDt = null;
		List<BairroLocomocaoDt> listaBairroLocomocaoContaVinculada = null;
		List listaQuantidadeBairroDt = null;
		List listaQuantidadeBairroLocomocaoContaVinculada = null;
		List<GuiaCustaModeloDt> listaItensGuia 	= null;
		
		//********************************************
		//Variáveis objetos
		GuiaEmissaoDt guiaEmissaoDt = null;
		ProcessoDt processoDt = null;
		
		//********************************************
		//Variáveis de sessão
		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
		if( guiaEmissaoDt == null ) {
			guiaEmissaoDt = new GuiaEmissaoDt();
		}
		guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if ( processoDt != null ) {
			request.setAttribute("guiaIdProcesso", processoDt.getId());
			guiaEmissaoDt.setId_Processo(processoDt.getId());
			guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
		}
		
		listaItensGuia = (List) request.getSession().getAttribute("ListaItensGuia");
		if( listaItensGuia == null ) {
			listaItensGuia = new ArrayList();
		}
		
		listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
		if( listaCustaDt == null ) {
			listaCustaDt = new ArrayList();
		}
		
		listaBairroDt = (List<BairroLocomocaoDt>) request.getSession().getAttribute("ListaBairroDt");
		if( listaBairroDt == null ) {
			listaBairroDt = new ArrayList<BairroLocomocaoDt>();
			listaBairroLocomocaoContaVinculada = new ArrayList<BairroLocomocaoDt>();
		}
		
		listaBairroLocomocaoContaVinculada = (List<BairroLocomocaoDt>) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
		if( listaBairroLocomocaoContaVinculada == null ) {
			listaBairroLocomocaoContaVinculada = new ArrayList<BairroLocomocaoDt>();
		}
		
		listaQuantidadeBairroDt = (List) request.getSession().getAttribute("ListaQuantidadeBairroDt");
		if( listaQuantidadeBairroDt == null ) {
			listaQuantidadeBairroDt = new ArrayList();
		}
		
		listaQuantidadeBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
		if( listaQuantidadeBairroLocomocaoContaVinculada == null ) {
			listaQuantidadeBairroLocomocaoContaVinculada = new ArrayList();
		}
		
		//********************************************
		//Requests 
		request.setAttribute("tempPrograma" 			, "Guia Fazenda Municipal");
		request.setAttribute("tempRetorno" 				, NOME_CONTROLE_WEB_XML);
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");		
	
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}	
				
		
		if( request.getParameter("numeroDUAM") != null && !(request.getParameter("numeroDUAM").equals("null")) && request.getParameter("numeroDUAM").toString().length() > 0 && !(request.getParameter("numeroDUAM").equals("0")) ) {
			guiaEmissaoDt.setNumeroDUAM(request.getParameter("numeroDUAM").toString());
		}
		if( request.getParameter("dataVencimentoDUAM") != null && !(request.getParameter("dataVencimentoDUAM").equals("null")) && request.getParameter("dataVencimentoDUAM").toString().length() > 0 ) {
			guiaEmissaoDt.setDataVencimentoDUAM(request.getParameter("dataVencimentoDUAM").toString());
		}
		if( request.getParameter("quantidadeParcelasDUAM") != null && !(request.getParameter("quantidadeParcelasDUAM").equals("null")) && request.getParameter("quantidadeParcelasDUAM").toString().length() > 0  && !(request.getParameter("quantidadeParcelasDUAM").equals("0")) ) {
			guiaEmissaoDt.setQuantidadeParcelasDUAM(request.getParameter("quantidadeParcelasDUAM").toString());
		}
		if( request.getParameter("valorImpostoMunicipalDUAM") != null && !(request.getParameter("valorImpostoMunicipalDUAM").equals("null")) && request.getParameter("valorImpostoMunicipalDUAM").toString().length() > 0 ) {
			guiaEmissaoDt.setValorImpostoMunicipalDUAM(request.getParameter("valorImpostoMunicipalDUAM").toString());
		}
		
		if( request.getParameter("novoValorAcao") != null && request.getParameter("novoValorAcao").toString().length() > 0 ) {
			guiaEmissaoDt.setNovoValorAcao(request.getParameter("novoValorAcao").toString());
		}
		else {
			if( guiaEmissaoDt.getNovoValorAcao().length() == 0 ) {
				if( processoDt != null && processoDt.getValor() != null && processoDt.getValor().length() > 0 ) {
					guiaEmissaoDt.setNovoValorAcao(processoDt.getValor());
				}
				else {
					guiaEmissaoDt.setNovoValorAcao("0");
				}
			}
		}
		
		if( request.getParameter("rateioCodigo") != null ) {
			guiaEmissaoDt.setRateioCodigo(request.getParameter("rateioCodigo").toString());
		}
			
		if( request.getParameter("avaliadorQuantidade") != null && request.getParameter("avaliadorQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setAvaliadorQuantidade(request.getParameter("avaliadorQuantidade").toString());
		}
		
		if( request.getParameter("taxaProtocoloQuantidade") != null && request.getParameter("taxaProtocoloQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setTaxaProtocoloQuantidade(request.getParameter("taxaProtocoloQuantidade").toString());
		}
		
		if( request.getParameter("honorariosQuantidade") != null && request.getParameter("honorariosQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setHonorariosQuantidade(request.getParameter("honorariosQuantidade").toString());
		}
		else {
			if( guiaEmissaoDt.getHonorariosQuantidade() != null && guiaEmissaoDt.getHonorariosQuantidade().length() > 0 ){
				//nada
			}
			else {
				guiaEmissaoDt.setHonorariosQuantidade("0");
			}
		}
		
		if( request.getParameter("honorariosValor") != null && request.getParameter("honorariosValor").toString().length() > 0 ) {
			guiaEmissaoDt.setHonorariosValor(request.getParameter("honorariosValor").toString());
		}
		
		if( request.getParameter("parcelasQuantidade") != null && request.getParameter("parcelasQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setParcelasQuantidade(request.getParameter("parcelasQuantidade").toString());
		}
		
		if( request.getParameter("leilaoQuantidade") != null && request.getParameter("leilaoQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setLeilaoQuantidade(request.getParameter("leilaoQuantidade").toString());
		}
		
		if( request.getParameter("partidorQuantidade") != null && request.getParameter("partidorQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setPartidorQuantidade(request.getParameter("partidorQuantidade").toString());
		}
		
		if( request.getParameter("codigoOficialSPGPenhora") != null && request.getParameter("codigoOficialSPGPenhora").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGPenhora(request.getParameter("codigoOficialSPGPenhora").toString());
		}
		
		if( request.getParameter("codigoOficialSPGLocomocao") != null && request.getParameter("codigoOficialSPGLocomocao").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocao(request.getParameter("codigoOficialSPGLocomocao").toString());
		}
		
		if( request.getParameter("codigoOficialSPGLeilao") != null && request.getParameter("codigoOficialSPGLeilao").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLeilao(request.getParameter("codigoOficialSPGLeilao").toString());
		}
		
		if( request.getParameter("codigoOficialSPGLocomocaoContaVinculada") != null && request.getParameter("codigoOficialSPGLocomocaoContaVinculada").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocaoContaVinculada(request.getParameter("codigoOficialSPGLocomocaoContaVinculada").toString());
		}
		
		if( request.getParameter("dividaQuantidade") != null && request.getParameter("dividaQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setDividaAtivaQuantidade(request.getParameter("dividaQuantidade").toString());
		}
		
		if( request.getParameter("distribuidorQuantidade") != null && request.getParameter("distribuidorQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setDistribuidorQuantidade(request.getParameter("distribuidorQuantidade").toString());
		}
		
		if( request.getParameter("escrivaniaQuantidade") != null && request.getParameter("escrivaniaQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setEscrivaniaQuantidade(request.getParameter("escrivaniaQuantidade").toString());
		}
		
		if( request.getParameter("penhoraQuantidade") != null && request.getParameter("penhoraQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setPenhoraQuantidade(request.getParameter("penhoraQuantidade").toString());
		}
		
		if( request.getParameter("contadorQuantidade") != null && request.getParameter("contadorQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setContadorQuantidade(request.getParameter("contadorQuantidade").toString());
		}
		
		if( request.getParameter("custasQuantidade") != null && request.getParameter("custasQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setCustasQuantidade(request.getParameter("custasQuantidade").toString());
		}
		
		if( request.getParameter("retificacaoCustasQuantidade") != null && request.getParameter("retificacaoCustasQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setRetificacaoCustasQuantidade(request.getParameter("retificacaoCustasQuantidade").toString());
		}
		
		if( request.getParameter("retificacaoCalculosQuantidade") != null && request.getParameter("retificacaoCalculosQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setRetificacaoCalculosQuantidade(request.getParameter("retificacaoCalculosQuantidade").toString());
		}
				
		if( request.getParameter("correioQuantidade") != null && request.getParameter("correioQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("correioQuantidade").toString());
		}
		
		if( request.getParameter("rateioQuantidade") != null && request.getParameter("rateioQuantidade").toString().length() > 0 ) {
			guiaEmissaoDt.setRateioQuantidade(request.getParameter("rateioQuantidade").toString());
		}
		
		if( request.getParameter("depositarioPublico") != null && request.getParameter("depositarioPublico").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublico(request.getParameter("depositarioPublico"));
		}
		
		if( request.getParameter("depositarioPublicoDataInicial") != null && request.getParameter("depositarioPublicoDataInicial").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublicoDataInicial(request.getParameter("depositarioPublicoDataInicial"));
		}
		
		if( request.getParameter("depositarioPublicoDataFinal") != null && request.getParameter("depositarioPublicoDataFinal").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublicoDataFinal(request.getParameter("depositarioPublicoDataFinal"));
		}
		
		if( request.getParameter("depositarioPublicoBemImovel") != null && request.getParameter("depositarioPublicoBemImovel").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublicoBemImovel(request.getParameter("depositarioPublicoBemImovel"));
		}
		
		if( request.getParameter("depositarioPublicoBemImovelDataInicial") != null && request.getParameter("depositarioPublicoBemImovelDataInicial").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublicoBemImovelDataInicial(request.getParameter("depositarioPublicoBemImovelDataInicial"));
		}
		
		if( request.getParameter("depositarioPublicoBemImovelDataFinal") != null && request.getParameter("depositarioPublicoBemImovelDataFinal").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublicoBemImovelDataFinal(request.getParameter("depositarioPublicoBemImovelDataFinal"));
		}
		
		if( request.getParameter("pregaoPorteiro") != null && request.getParameter("pregaoPorteiro").toString().length() > 0 ) {
			guiaEmissaoDt.setPregaoPorteiro(request.getParameter("pregaoPorteiro").toString());
		}
		
		if( request.getParameter("afixacaoEdital") != null && request.getParameter("afixacaoEdital").toString().length() > 0 ) {
			guiaEmissaoDt.setAfixacaoEdital(request.getParameter("afixacaoEdital").toString());
		}
		
		if( request.getParameter("quantidadeAcrescimoPessoa") != null && request.getParameter("quantidadeAcrescimoPessoa").toString().length() > 0 ) {
			guiaEmissaoDt.setQuantidadeAcrescimo(request.getParameter("quantidadeAcrescimoPessoa").toString());
		}
		
		if( request.getParameter("documentoDiarioJustica") != null && request.getParameter("documentoDiarioJustica").length() > 0 ) {
			guiaEmissaoDt.setDocumentoDiarioJustica(request.getParameter("documentoDiarioJustica"));
		}
		
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaFazendaMunicipalNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt == null ) {
				serventiaDt = guiaFazendaMunicipalNe.consultarIdServentia(processoDt.getId_Serventia());
			}
			if( serventiaDt != null ) {
				comarcaDt = guiaFazendaMunicipalNe.consultarComarca(serventiaDt.getId_Comarca());
				processoDt.setComarca(serventiaDt.getComarca());
				processoDt.setComarcaCodigo(serventiaDt.getComarcaCodigo());
			}
		}
		
		
		List listaTotalGuiaParcelada = new ArrayList();
		List listaGuiasParceladas = new ArrayList();
		
		switch(paginaatual) {
			case Configuracao.Novo: {
				
				if( guiaFazendaMunicipalNe.isServentiaSegundoGrau(processoDt.getId_Serventia()) ) {
					throw new MensagemException("Processo de Segundo Grau, nã¯ pode emitir esta guia.");
				}
				
				// Validação de Comarca (desativada) + Validação de processo tipo = Execução Fiscal
				//Ocorrencia 2020/10357 - Retirado restrição de acesso para outras comarcas para a nova central única dos contadores.
				if( processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_FISCAL)) &&
					!guiaFazendaMunicipalNe.isProcessoSegundoGrau(serventiaDt.getId())) {
					
					stAcao = PAGINA_GUIA_FAZENDA_MUNICIPAL;
					
					if( guiaFazendaMunicipalNe.isConexaoSPG_OK() ) {
					
						guiaEmissaoDt = new GuiaEmissaoDt();
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						request.getSession().removeAttribute("ListaBairroDt");
						request.getSession().removeAttribute("ServentiaDt");
						request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
						request.getSession().removeAttribute("totalRateio");
						
						//Tratamento para processos sigilosos e segredo de justiça
						guiaFazendaMunicipalNe.tratamentoParaProcessosSigilosoSegredoJustica(processoDt);
						
						List listaPromoventes = processoDt.getListaPolosAtivos();
						List listaPromovidos = processoDt.getListaPolosPassivos();
						if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
							for(int i = 0; i < listaPromoventes.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId());
								}
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
								}
							}
						}
						if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
							for(int i = 0; i < listaPromovidos.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId());
								}
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
								}
							}
						}
						
						
						
						request.getSession().setAttribute("ListaCustaDt", guiaFazendaMunicipalNe.consultarItensGuia(null, GuiaTipoDt.ID_FAZENDA_MUNICIPAL, processoDt.getId_ProcessoTipo()));
						
						List listaOficiaisSPGDt = new OficialSPGNe().consultarOficiaisComarca(processoDt.getComarcaCodigo());
						request.getSession().setAttribute("ListaOficiaisSPGDt", listaOficiaisSPGDt);
						
						//Atualização do valor da causa
						guiaEmissaoDt.setDataBaseAtualizacao(processoDt.getDataRecebimento());
						guiaEmissaoDt.setDataBaseFinalAtualizacao(processoDt.getDataRecebimento());
						guiaEmissaoDt.setNovoValorAcao( processoDt.getValor() );
						guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaFazendaMunicipalNe.atualizarValorCausaUFR(processoDt.getValor(), Funcoes.StringToDate(processoDt.getDataRecebimento())).toString()) );
						
						//Inicia custas com 1, a pedido do contador Marcelo
						guiaEmissaoDt.setCustasQuantidade("1");
						guiaEmissaoDt.setDividaAtivaQuantidade("1");
						guiaEmissaoDt.setDistribuidorQuantidade("1");
						guiaEmissaoDt.setEscrivaniaQuantidade("1");
						//guiaEmissaoDt.setContadorQuantidade("1");
						guiaEmissaoDt.setHonorariosQuantidade("10");
						guiaEmissaoDt.setParcelasQuantidade("1");
						guiaEmissaoDt.setHonorariosValor(guiaEmissaoDt.getNovoValorAcaoAtualizado());
						guiaEmissaoDt.setRateioCodigo(null);
						//guiaEmissaoDt.setDistribuidorQuantidade("1");
						guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
						
						if( comarcaDt.getComarcaCodigo().equals(String.valueOf(ComarcaDt.APARECIDA_DE_GOIANIA)) || comarcaDt.getComarcaCodigo().equals(String.valueOf(ComarcaDt.GOIANIA))) {
							request.getSession().setAttribute("possuiHonorarios", new Boolean(true));
						} else {
							request.getSession().setAttribute("possuiHonorarios", new Boolean(false));
							guiaEmissaoDt.setHonorariosQuantidade("0");
							guiaEmissaoDt.setParcelasQuantidade("0");
						}
						
						if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.CONTADORES_VARA))){
							request.getSession().setAttribute("bloqueiaHonorarios", new Boolean(false));
						} else {
							request.getSession().setAttribute("bloqueiaHonorarios", new Boolean(true));
						}
											
						request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
						
						//Verifica para emitir mensage se já existe guia do mesmo tipo
						if( guiaFazendaMunicipalNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_FAZENDA_MUNICIPAL) ) {
							request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
						}
						
						request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					}
					else {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
						return;
					}
				}
				else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_SEM_PERMISSAO_EMITIR_GUIA_PROCESSO));
					return;
				}
				
				break;
			}
			
			case Configuracao.Atualizar : {
				
				if( request.getParameter("novoValorAcao") != null && request.getParameter("novoValorAcao").toString().length() > 0 ) {
					guiaEmissaoDt.setNovoValorAcao(request.getParameter("novoValorAcao").toString());
				}
				
				guiaEmissaoDt.setDataBaseFinalAtualizacao(request.getParameter("dataBaseFinalAtualizacao").toString());
				guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaFazendaMunicipalNe.atualizarValorCausaUFR(guiaEmissaoDt.getNovoValorAcao(), Funcoes.StringToDate(guiaEmissaoDt.getDataBaseFinalAtualizacao())).toString()) );
				
				break;
			}
			
			//Apresenta Prévia de Cálculo
			case Configuracao.Curinga6 : {
				switch(passoEditar) {
					
					//Apresentar Mensagem
					case Configuracao.Mensagem : {
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE));
						
						//Atenção!
						//Não tem break pq precisa mostrar a tela novamente de prévia.
					}
					
					//Utilizado na Volta da Prévia para a Página da Guia
					case Configuracao.Curinga7 : {
						
						if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_VARIAVEL)) || guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_50_50)) ) {
							request.setAttribute("visualizaDivRateioPartesVariavel", "DivVisivel");
						}
						else {
							List listaPromoventes = processoDt.getListaPolosAtivos();
							List listaPromovidos = processoDt.getListaPolosPassivos();
							
							if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
								for(int i = 0; i < listaPromoventes.size(); i++) {
									ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
									
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
								}
							}
							if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
								for(int i = 0; i < listaPromovidos.size(); i++) {
									ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
									
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
								}
							}
							
							request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
						}
						
						break;
					}
					
					case Configuracao.Curinga8: {
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							return;
						}
						
						//Retira as outras guias do rateio da sessão
						List listaPromoventes = processoDt.getListaPolosAtivos();
						List listaPromovidos = processoDt.getListaPolosPassivos();
						if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
							for(int i = 0; i < listaPromoventes.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
								
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
							}
						}
						if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
							for(int i = 0; i < listaPromovidos.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
								
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
							}
						}
						request.getSession().removeAttribute("ListaGuiasRateio");
						request.getSession().removeAttribute("ListaTotalGuiaRateio");
						request.getSession().removeAttribute("ListaNomeParteGuia");
						request.getSession().removeAttribute("ListaNomePartePorcentagemGuia");
						
							
							//Consulta lista de itens da guia
							//listaItensGuia = guiaFinalNe.consultarItensGuia(guiaEmissaoDt, GuiaTipoDt.FINAL_ZERO, processoDt.getId_ProcessoTipo());
							listaItensGuia = null;
							
							if( listaItensGuia == null ) {
								GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_FAZENDA_MUNICIPAL, processoDt.getId_ProcessoTipo());
								guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
								List listaAux = guiaFazendaMunicipalNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
								if( listaAux != null ) {
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList();
									listaItensGuia.addAll(listaAux);
								}
							}
							
							//Locomoção com Zona-Bairro
							List valoresIdBairroContaVinculada = null;
							List valoresIdBairro = null;
							listaBairroDt = (List<BairroLocomocaoDt>)request.getSession().getAttribute("ListaBairroDt");
							listaQuantidadeBairroDt = new ArrayList();
							if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
								if( valoresIdBairro == null )
									valoresIdBairro = new ArrayList();
								
								for(int i = 0; i < listaBairroDt.size(); i++) {
									BairroLocomocaoDt bairroLocomocaoDt = (BairroLocomocaoDt)listaBairroDt.get(i);
									
									int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocao"+i));
									for(int j = 0; j < quantidadeLocomocao; j++) {
										valoresIdBairro.add(bairroLocomocaoDt.getBairroDt().getId());
									}
									
									listaQuantidadeBairroDt.add(String.valueOf(quantidadeLocomocao));
									request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
									
									//Adicionar item de Locomoção
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
									
									for(int j = 0; j < quantidadeLocomocao; j++) {
										listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItemLocomocaoOficial(bairroLocomocaoDt.getBairroDt(), bairroLocomocaoDt.getOficialSPGDt()) );
									}
								}
								
								if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
									valoresIdBairroContaVinculada = new ArrayList();
									
									for(int i = 0; i < listaBairroDt.size(); i++) {
										for(int k = 0; k < listaBairroLocomocaoContaVinculada.size(); k++) {
											BairroLocomocaoDt bairroLocomocaoDt = (BairroLocomocaoDt)listaBairroDt.get(i);
											BairroLocomocaoDt bairroLocomocaoContaVinculadaDt = (BairroLocomocaoDt)listaBairroLocomocaoContaVinculada.get(k);
											
											if(bairroLocomocaoDt.getBairroDt().getId().equals(bairroLocomocaoContaVinculadaDt.getBairroDt().getId())) {
												int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocao"+i));
												for(int j = 0; j < quantidadeLocomocao; j++) {
													valoresIdBairroContaVinculada.add(bairroLocomocaoDt.getBairroDt().getId());
												}
												listaQuantidadeBairroLocomocaoContaVinculada.add(String.valueOf(quantidadeLocomocao));
												request.getSession().setAttribute("ListaQuantidadeBairroLocomocaoContaVinculada", listaQuantidadeBairroLocomocaoContaVinculada);
												for(int j = 0; j < quantidadeLocomocao; j++) {
													listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItemLocomocaoOficialContaVinculada(bairroLocomocaoDt.getBairroDt(), bairroLocomocaoDt.getOficialSPGDt()) );
												}
											}
										}
									}
								}
							}
							
									
							Map valoresReferenciaCalculo = new HashMap();
							valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcao());
							valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcao());
							
							if( valoresIdBairro != null )
								valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
							if( valoresIdBairroContaVinculada != null )
								valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_CONTA_VINCULADA, valoresIdBairroContaVinculada);
							
							//Taxa Protocolo
							if( guiaEmissaoDt.getTaxaProtocoloQuantidade() != null && guiaEmissaoDt.getTaxaProtocoloQuantidade().length() > 0 && !guiaEmissaoDt.getTaxaProtocoloQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.TAXA_PROTOCOLO) );
							}
							
							//Despesa Postal
							if( guiaEmissaoDt.getCorreioQuantidade() != null && guiaEmissaoDt.getCorreioQuantidade().length() > 0 && !guiaEmissaoDt.getCorreioQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.DESPESA_POSTAL) );
							}
													
							//Divida Ativa Ajuizada
							if( guiaEmissaoDt.getDividaAtivaQuantidade() != null && guiaEmissaoDt.getDividaAtivaQuantidade().length() > 0 && !guiaEmissaoDt.getDividaAtivaQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.DIVIDA_ATIVA_AJUIZADA) );
							}
							
							//Distribuidor
							if( guiaEmissaoDt.getDistribuidorQuantidade() != null && guiaEmissaoDt.getDistribuidorQuantidade().length() > 0 && !guiaEmissaoDt.getDistribuidorQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.ATOS_DE_DISTRIBUICAO_DOS_PROCESSOS_FISICOS_APLICA_SE_10_SOBRE_VALOR_MINIMO_ITEM_5I) );
							}
							
							
							
							//Custa de Escrivania
							if( guiaEmissaoDt.getEscrivaniaQuantidade() != null && guiaEmissaoDt.getEscrivaniaQuantidade().length() > 0 && !guiaEmissaoDt.getEscrivaniaQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO) );
							}
							
							
							//Retificaï¿½ï¿½es de Contas de Custas
							if( guiaEmissaoDt.getRetificacaoCustasQuantidade() != null && guiaEmissaoDt.getRetificacaoCustasQuantidade().length() > 0 && !guiaEmissaoDt.getRetificacaoCustasQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.RETIFICACAO_CONTA_CUSTAS) );
							}
							
							//Retificações de Contas de Calculo
							if( guiaEmissaoDt.getRetificacaoCalculosQuantidade() != null && guiaEmissaoDt.getRetificacaoCalculosQuantidade().length() > 0 && !guiaEmissaoDt.getRetificacaoCalculosQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.RETIFICACAO_CONTA_CALCULO) );
							}
							
							
							//Quantidade de Acrescimo por Pessoa
							if( guiaEmissaoDt.getQuantidadeAcrescimo() != null && guiaEmissaoDt.getQuantidadeAcrescimo().length() > 0 && !guiaEmissaoDt.getQuantidadeAcrescimo().equals("0")) {
								valoresReferenciaCalculo.put(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA, guiaEmissaoDt.getQuantidadeAcrescimo());
							}
							
							
							
							//Desconto 50% determinado pelo juiz
							String checkbox[] = request.getParameterValues("reducao50Porcento");
							if( checkbox != null ) {
								valoresReferenciaCalculo.put(CustaDt.DESCONTO_PEDIDO_JUIZ, checkbox[0]);
							}
							
							//Depositário Público
							if( guiaEmissaoDt.getDepositarioPublico() != null && guiaEmissaoDt.getDepositarioPublico().length() > 0 && !guiaEmissaoDt.getDepositarioPublico().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.DEPOSITO_COMPREENDENDO_OS_REGISTROS_GUARDA_ESCRITURACAO) );
							}
							
							//Afixacao Edital
							if( guiaEmissaoDt.getAfixacaoEdital() != null && guiaEmissaoDt.getAfixacaoEdital().length() > 0 && !guiaEmissaoDt.getAfixacaoEdital().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.AFIXACAO_EDITAL) );
							}
							
							//Pregão Porteiro
							if( guiaEmissaoDt.getPregaoPorteiro() != null && guiaEmissaoDt.getPregaoPorteiro().length() > 0 && !guiaEmissaoDt.getPregaoPorteiro().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.PREGAO_AUDIENCIA) );
							}
							
							
							//Avaliador
							if( guiaEmissaoDt.getAvaliadorQuantidade() != null && guiaEmissaoDt.getAvaliadorQuantidade().length() > 0 && guiaEmissaoDt.getAvaliadorValor().length() > 0 && !guiaEmissaoDt.getAvaliadorQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA) );
							}
							
							
							//Honorários do Procurador
							if( guiaEmissaoDt.getHonorariosQuantidade().length() > 0 && guiaEmissaoDt.getHonorariosValor().length() > 0 && guiaEmissaoDt.getParcelasQuantidade().length() > 0 && !guiaEmissaoDt.getHonorariosQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.HONORARIOS_PROCURADOR) );
							}
							
							//Partidor
							if( guiaEmissaoDt.getPartidorQuantidade().length() > 0 && !guiaEmissaoDt.getPartidorQuantidade().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.CUSTA_PARTIDOR) );
							}
							
							//Por documento publicado diário da justiça
							if( guiaEmissaoDt.getDocumentoDiarioJustica() != null && guiaEmissaoDt.getDocumentoDiarioJustica().length() > 0 && !guiaEmissaoDt.getDocumentoDiarioJustica().equals("0")) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
								
								listaItensGuia.add( guiaFazendaMunicipalNe.adicionarItem(CustaDt.POR_DOCUMENTO_PUBLICADO) );
							}
							
							
							
							
							List listaIdBairroAux = new ArrayList();
							if( valoresIdBairro != null && valoresIdBairro.size() > 0 ) {
								listaIdBairroAux.addAll(valoresIdBairro);
							}
							if( valoresIdBairroContaVinculada != null && valoresIdBairroContaVinculada.size() > 0  ) {
								listaIdBairroAux.addAll(valoresIdBairroContaVinculada);
							}
							boolean bairrosZoneados = guiaFazendaMunicipalNe.isBairroZoneado(listaIdBairroAux);
							
							
							guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
							List listaGuiaItemDt = new ArrayList();
							if( bairrosZoneados ) {
								listaGuiaItemDt = guiaFazendaMunicipalNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
							}
							
							
							if( !bairrosZoneados ) {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
								stAcao = PAGINA_GUIA_FAZENDA_MUNICIPAL;
							}
							else {
								
								if( !guiaFazendaMunicipalNe.isGuiaZeradaOuNegativa() ) {
									//Altera nome do regimento 61:
									for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
										GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
										if( guiaItemDt.getCustaDt().getCodigoRegimento().equals("61") ) {// CustaDt.CUSTA_PENHORA
											guiaItemDt.getCustaDt().setArrecadacaoCusta("AUTOS");
										}
										if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("31") ) {// DUAM
											guiaItemDt.getCustaDt().setArrecadacaoCusta("DUAM NR. " + guiaEmissaoDt.getNumeroDUAM() + " (1/" + guiaEmissaoDt.getQuantidadeParcelasDUAM() + ")");
										}
										if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("26")){// LOCOMOCAO PARA OFICIAL AD HOC
												guiaItemDt.setCodigoOficial(guiaEmissaoDt.getCodigoOficialSPGLocomocao());
												//Gera nome oficial para ser exibido na previa
												OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
												request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
												//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
												guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
												
										}
									}
																
									if(guiaEmissaoDt.getParcelasQuantidade() != null && Funcoes.StringToInt(guiaEmissaoDt.getParcelasQuantidade()) > 1){
										List listaParcelasGuiaItemDt = null;
										
										boolean calcularTodosItens = true;
										for( int i = 1; i <= Funcoes.StringToInt(guiaEmissaoDt.getParcelasQuantidade()); i++ ) {
											listaParcelasGuiaItemDt = guiaFazendaMunicipalNe.calcularParcelasGuia(listaGuiaItemDt, Funcoes.StringToInt(guiaEmissaoDt.getParcelasQuantidade()), calcularTodosItens, i);
											
											if( listaParcelasGuiaItemDt != null ) {
												listaGuiasParceladas.add( listaParcelasGuiaItemDt );
												listaTotalGuiaParcelada.add( guiaFazendaMunicipalNe.calcularTotalGuia(listaParcelasGuiaItemDt) );
											}
											
											if( i == 1 ) {
												calcularTodosItens = false;
											}
										}
										
										request.getSession().setAttribute("ListaGuiasRateio", listaGuiasParceladas);
										request.getSession().setAttribute("ListaTotalGuiaRateio", listaTotalGuiaParcelada);
									}
									
									
									//Obtem o id_GuiaModelo
									if( listaItensGuia != null && listaItensGuia.size() > 0) {
										GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
										if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
											guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
									}
									
									
									//Deve haver no mínimo 1 item de guia
									if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
										
										request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
										request.setAttribute("TotalGuia", guiaFazendaMunicipalNe.getGuiaCalculoNe().getTotalGuia() );
										
										request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
										request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaFazendaMunicipalNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
										request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaFazendaMunicipalNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
										request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
										request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
										request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
										
										ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
										processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
										processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
										processoCadastroDt.setValor(processoDt.getValor());
										processoCadastroDt.setProcessoTipo(processoDt.getProcessoTipo());
										
										guiaEmissaoDt.setValorAcao(processoCadastroDt.getValor());
										guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
										
										request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
										request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
										
										stAcao = PAGINA_GUIA_PREVIA_CALCULO;
										request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA));
										
									}
									else {
										request.setAttribute("MensagemErro", "Nenhum Item de Guia Localizado.<br/>Informe os Oficiais.<br/> A Guia deve Conter 1 ou mais Itens de Custa.");
										stAcao = PAGINA_GUIA_FAZENDA_MUNICIPAL;
									}
								}
								else {
									request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
									stAcao = PAGINA_GUIA_FAZENDA_MUNICIPAL;
								}
								
								break;
							}
						
						break;
					}
				}
				
				break;
			}
			
//			//Encaminha Pagamento On-Line
//			case Configuracao.Curinga7 : {
//				
//				guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
//				if( guiaEmissaoDt == null )
//					guiaEmissaoDt = new GuiaEmissaoDt();
//				
//				//Obtï¿½m o prï¿½ximo nï¿½mero de Guia
//				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaFazendaMunicipalNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
//				}
//				guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
//				guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
//				guiaEmissaoDt.setServentia(processoDt.getServentia());
//				guiaEmissaoDt.setComarca(processoDt.getComarca());
//				guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
//				guiaEmissaoDt.setValorAcao(processoDt.getValor());
//				guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
//				guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
//				
//				guiaEmissaoDt.setListaRequerentes(processoDt.getListaPromoventes());
//				guiaEmissaoDt.setListaRequeridos(processoDt.getListaPromovidos());
//				guiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
//				guiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
//				
//				guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
//				guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
//				
//				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
//				
//				switch(passoEditar) {
//					
//					//Banco do Brasil
//					case Configuracao.Curinga6 : {
//						redireciona(response, "PagamentoOnLine?PaginaAtual=" + Configuracao.Curinga6 + "&PassoEditar=" + Configuracao.Curinga6 + "&tempRetornoBuscaProcesso=BuscaProcesso&sv=" + PagamentoOnLineNe.PERMITE_SALVAR_SIM);
//						return;
//					}
//					
//					//Banco Caixa Econï¿½mica
//					case Configuracao.Curinga7 : {
//						return;
//					}
//					
//					//Banco Itaï¿½
//					case Configuracao.Curinga8 : {
//						return;
//					}
//				}
//				
//				break;
//			}
//			
//			//Retorno do pagamento On-Line
//			case Configuracao.Curinga8 : {
//				break;
//			}
			
			//Remover Item
			case Configuracao.Excluir : {
				
				switch(passoEditar) {
				
					//Remover Locomoï¿½ï¿½o
					case Configuracao.Curinga6 : {
						int posicaoListaCustaExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaCustaExcluir"));
						listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
						
						if( posicaoListaCustaExcluir == -99999) {
							listaCustaDt.clear();
						}
						else {
							if( posicaoListaCustaExcluir != -1 ) {
								if( listaCustaDt.size() > 0 ) {
									listaCustaDt.remove(posicaoListaCustaExcluir);
								}
							}
						}
						
						request.getSession().setAttribute("ListaCustaDt", listaCustaDt);
						
						break;
					}
					
					//Remover Item de Bairro
					case Configuracao.Curinga7 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							listaBairroDt = (List<BairroLocomocaoDt>) request.getSession().getAttribute("ListaBairroDt");
							listaQuantidadeBairroDt = (List) request.getSession().getAttribute("ListaQuantidadeBairroDt");
							if( listaBairroDt.size() > 0 ) {
								listaBairroDt.remove(posicaoListaBairroExcluir);
								listaQuantidadeBairroDt.remove(posicaoListaBairroExcluir);
								
								request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
								request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
							}
						}
						
						break;
					}
					
					//Remover Item de Bairro Conta Vinculada
					case Configuracao.Curinga8 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroContaVinculadaExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							listaBairroLocomocaoContaVinculada = (List<BairroLocomocaoDt>) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							listaQuantidadeBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
							if( listaBairroLocomocaoContaVinculada.size() > 0 ) {
								listaBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								listaQuantidadeBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								
								request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
								request.getSession().setAttribute("ListaQuantidadeBairroLocomocaoContaVinculada", listaQuantidadeBairroLocomocaoContaVinculada);
							}
						}
						
						break;
					}
				}
				
				break;
			}
			
			//Impressï¿½o da guia
			case Configuracao.Imprimir : {
				
				//Valida o processo da sua aba
				if( request.getParameter("guiaIdProcesso") != null && 
					!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
					
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
					return;
				}
				
				guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
				if( guiaEmissaoDt == null )
					guiaEmissaoDt = new GuiaEmissaoDt();
				
				//Obtï¿½m o prï¿½ximo nï¿½mero de Guia
				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaFazendaMunicipalNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
				}
				guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
				guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
				guiaEmissaoDt.setServentia(processoDt.getServentia());
				guiaEmissaoDt.setComarca(processoDt.getComarca());
				guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
				guiaEmissaoDt.setValorAcao(processoDt.getValor());
				guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
				guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
				
				guiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
				guiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
				guiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
				guiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
				
				guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
				guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				
				//Quando tem Rateio
				if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFazendaMunicipalNe.RATEIO_50_50)) ) {
					
		            GuiaEmissaoDt guiaEmissaoDt2 = (GuiaEmissaoDt) Funcoes.serializeObjeto(guiaEmissaoDt);
		            
					//GuiaEmissaoDt guiaEmissaoDt2 = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
					if( guiaEmissaoDt2 == null )
						guiaEmissaoDt2 = new GuiaEmissaoDt();
					
					//Obtï¿½m o prï¿½ximo nï¿½mero de Guia
					guiaEmissaoDt2.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt2.setNumeroGuiaCompleto( guiaFazendaMunicipalNe.getNumeroGuiaCompleto(guiaEmissaoDt2.getNumeroGuia()) );
					
					guiaFazendaMunicipalNe.calcularRateio50Porcento(guiaEmissaoDt.getListaGuiaItemDt());
					guiaFazendaMunicipalNe.calcularRateio50Porcento(guiaEmissaoDt2.getListaGuiaItemDt());
					
					guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					guiaEmissaoDt2.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));

					guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
					
					guiaFazendaMunicipalNe.salvar(guiaEmissaoDt2, guiaEmissaoDt2.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				
				//ProcessoParteTipoCodigo
				if( guiaEmissaoDt.getProcessoParteTipoCodigo() == null ) {
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFazendaMunicipalNe.RATEIO_100_REQUERENTE)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					}
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFazendaMunicipalNe.RATEIO_100_REQUERIDO)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
					}
				}
				
				//Salvar GuiaEmissï¿½o
				List listaAuxGuiaEmissaoDtRateioImpressao = null;
				if( request.getSession().getAttribute("ListaGuiasRateio") == null && 
					request.getSession().getAttribute("ListaTotalGuiaRateio") == null ) {
					
					guiaFazendaMunicipalNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				else {
					//Salvar as guias que teve rateio
					listaGuiasParceladas = (List) request.getSession().getAttribute("ListaGuiasRateio");
					//listaNomeParteGuia = (List) request.getSession().getAttribute("ListaNomeParteGuia");
					
					listaAuxGuiaEmissaoDtRateioImpressao = new ArrayList();
					
					if( listaGuiasParceladas != null && listaGuiasParceladas.size() > 0 ) {
						for( int i = 0; i < listaGuiasParceladas.size(); i++ ) {
							GuiaEmissaoDt auxGuiaEmissaoDt = new GuiaEmissaoDt();
							
							auxGuiaEmissaoDt.copiar(guiaEmissaoDt);
							
							//auxGuiaEmissaoDt.setId_ProcessoParteResponsavelGuia(listaNomeParteGuia.get(i).toString().split(":")[1]);
							
							auxGuiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
							auxGuiaEmissaoDt.setNumeroGuiaCompleto( guiaFazendaMunicipalNe.getNumeroGuiaCompleto(auxGuiaEmissaoDt.getNumeroGuia()) );
							auxGuiaEmissaoDt.setListaGuiaItemDt( (List) listaGuiasParceladas.get(i) );
							auxGuiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
							auxGuiaEmissaoDt.setServentia(processoDt.getServentia());
							auxGuiaEmissaoDt.setComarca(processoDt.getComarca());
							auxGuiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
							auxGuiaEmissaoDt.setValorAcao(processoDt.getValor());
							auxGuiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
							auxGuiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
							auxGuiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
							auxGuiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
							auxGuiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
							auxGuiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
							auxGuiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
							auxGuiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
							
							guiaFazendaMunicipalNe.salvar(auxGuiaEmissaoDt, (List) listaGuiasParceladas.get(i), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
							
							listaAuxGuiaEmissaoDtRateioImpressao.add(auxGuiaEmissaoDt);
						}
					}
				}
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Geraï¿½ï¿½o da guia PDF
						byte[] byTemp = guiaFazendaMunicipalNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_FAZENDA_MUNICIPAL, "FAZENDA MUNICIPAL");
						
						String nome="GuiaFazendaMunicipal_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;
						
						enviarPDF(response, byTemp, nome);
						return;
					}
					
					case Configuracao.Salvar : {
						
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						request.getSession().removeAttribute("ListaBairroDt");
						request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
						
						request.getSession().removeAttribute("ListaGuiasRateio");
						request.getSession().removeAttribute("ListaTotalGuiaRateio");
						request.getSession().removeAttribute("ListaNomeParteGuia");
						request.getSession().removeAttribute("ListaNomePartePorcentagemGuia");
						
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=Guia Emitida com Sucesso!");
						return;
					}
				}
				
				//Retira as outras guias do rateio da sessï¿½o
				List listaPromoventes = processoDt.getListaPolosAtivos();
				List listaPromovidos = processoDt.getListaPolosPassivos();
				if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
					for(int i = 0; i < listaPromoventes.size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
						
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
					}
				}
				if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
					for(int i = 0; i < listaPromovidos.size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
						
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
					}
				}
				request.getSession().removeAttribute("totalRateio");
				
				break;
			}
			
			//Busca de Bairro
			case ( BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar ) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_Bairro");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno",NOME_CONTROLE_WEB_XML);		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaFazendaMunicipalNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			//Busca de Bairro Locomoção Conta Vinculada
			case ( BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1 ) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_BairroLocomocaoContaVinculada");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno",NOME_CONTROLE_WEB_XML);		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaFazendaMunicipalNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			default : {
				//Busca Custa
				stId = request.getParameter("tempBuscaId_Custa");
				if( stId != null ) {
					CustaDt custaDt = guiaFazendaMunicipalNe.consultarCustaDtPorId(stId);
					
					if( listaCustaDt != null )
						listaCustaDt.add(custaDt);
					
					request.getSession().setAttribute("ListaCustaDt", listaCustaDt);
					
					stId = null;
				}
				
				//Busca Bairro
				AdicioneLocomocao(request, "tempBuscaId_Bairro", guiaEmissaoDt.getCodigoOficialSPGLocomocao(), listaBairroDt, listaQuantidadeBairroDt, "ListaBairroDt", "ListaQuantidadeBairroDt");
				
				//Busca Bairro Locomoç¡¯ Conta Vinculada
				AdicioneLocomocao(request, "tempBuscaId_Bairro", guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada(), listaBairroLocomocaoContaVinculada, listaQuantidadeBairroLocomocaoContaVinculada, "ListaBairroLocomocaoContaVinculada", "ListaQuantidadeBairroLocomocaoContaVinculada");
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private void AdicioneLocomocao(HttpServletRequest request, String stIdRequestBairro, String codigoOficialSPG, List<BairroLocomocaoDt> listaBairroDt, List listaQuantidadeBairroDt, String stIdSessionListaBairro, String stIdSessionListaQtdeBairro) throws Exception {
		String stId = request.getParameter(stIdRequestBairro);
		if( stId != null ) {
				OficialSPGDt OficialSPG = null;					
				if (codigoOficialSPG != null && codigoOficialSPG.trim().length() > 0) {
					List listaOficiaisSPGDt = (List)request.getSession().getAttribute("ListaOficiaisSPGDt");
					for (Object oficialSPGObj : listaOficiaisSPGDt) {
						OficialSPGDt OficialSPGTemp = (OficialSPGDt) oficialSPGObj;
						if (OficialSPGTemp.getCodigoOficial().trim().equalsIgnoreCase(codigoOficialSPG.trim())) {
							OficialSPG = OficialSPGTemp;
							break;
						}								
					}
			}
			
			if (OficialSPG == null && (!codigoOficialSPG.equals("999999") && !codigoOficialSPG.equals(""))) {
				super.exibaMensagemInconsistenciaErro(request, "Favor selecionar o oficial de justiç¡ antes de consultar o bairro");
			} else {
				BairroDt bairroDt = guiaFazendaMunicipalNe.consultarBairroId(stId);
			
				if (bairroDt != null) {
					if (bairroDt.getZona() == null || bairroDt.getZona().trim().length() == 0) {
						super.exibaMensagemInconsistenciaErro(request, Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
					} else {
						if( listaBairroDt != null) {
							BairroLocomocaoDt bairroLocomocao = new BairroLocomocaoDt();
							bairroLocomocao.setBairroDt(bairroDt);
							bairroLocomocao.setOficialSPGDt(OficialSPG);
						
							listaBairroDt.add(bairroLocomocao);
						}	
				
						if( listaQuantidadeBairroDt != null )
							listaQuantidadeBairroDt.add("1");
				
						request.getSession().setAttribute(stIdSessionListaBairro, listaBairroDt);
						request.getSession().setAttribute(stIdSessionListaQtdeBairro, listaQuantidadeBairroDt);
					}
				}
			}	
		}		
	}
}
