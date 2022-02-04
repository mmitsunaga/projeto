package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaFinalZeroDt;
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
import br.gov.go.tj.projudi.ne.GuiaCalculoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaFinalZeroNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class GuiaFinalZeroCt extends Controle {

	private static final long serialVersionUID = 5825007462655348018L;
	
	private static final String PAGINA_GUIA_FINAL_ZERO 		= "/WEB-INF/jsptjgo/GuiaFinalZero.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 	= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_LOCALIZAR 			= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	private static final String NOME_CONTROLE_WEB_XML = "GuiaFinalZero";
	
	private GuiaFinalZeroNe guiaFinalZeroNe = new GuiaFinalZeroNe();

	@Override
	public int Permissao() {
		return GuiaFinalZeroDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;
		List tempList 		= null;		
		String stId 		= "";
		
		boolean isProcessoSegundoGrau = false;
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		//-fim controle de buscas ajax
		
		stAcao = PAGINA_GUIA_FINAL_ZERO;
		
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
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if ( processoDt != null ) {
			request.setAttribute("guiaIdProcesso", processoDt.getId());
			guiaEmissaoDt.setId_Processo(processoDt.getId());
			guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
			
			if( processoDt.getId_Serventia() != null ) {
				isProcessoSegundoGrau = guiaFinalZeroNe.isProcessoSegundoGrau(processoDt.getId_Serventia());
				request.setAttribute("ProcessoSegundoGrau", isProcessoSegundoGrau);
			}
			
			request.setAttribute("permiteAdicionarEscrivaoCrime", processoDt.isCriminal());
		}
		
		listaItensGuia = (List<GuiaCustaModeloDt>) request.getSession().getAttribute("ListaItensGuia");
		if( listaItensGuia == null ) {
			listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
		}
		
		listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
		if( listaCustaDt == null ) {
			listaCustaDt = new ArrayList();
		}
		
		listaBairroDt = (List<BairroLocomocaoDt>) request.getSession().getAttribute("ListaBairroDt");
		if( listaBairroDt == null ) {
			listaBairroDt = new ArrayList<BairroLocomocaoDt>();
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
		request.setAttribute("tempPrograma" 			, "Final Zero");
		request.setAttribute("tempRetorno" 				, NOME_CONTROLE_WEB_XML);
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
				
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		if( request.getParameter("numeroDUAM") != null && !(request.getParameter("numeroDUAM").equals("null")) && request.getParameter("numeroDUAM").length() > 0 ) {
			guiaEmissaoDt.setNumeroDUAM(request.getParameter("numeroDUAM"));
		}
		if( request.getParameter("dataVencimentoDUAM") != null && !(request.getParameter("dataVencimentoDUAM").equals("null")) && request.getParameter("dataVencimentoDUAM").length() > 0 ) {
			guiaEmissaoDt.setDataVencimentoDUAM(request.getParameter("dataVencimentoDUAM"));
		}
		if( request.getParameter("quantidadeParcelasDUAM") != null && !(request.getParameter("quantidadeParcelasDUAM").equals("null")) && request.getParameter("quantidadeParcelasDUAM").length() > 0 ) {
			guiaEmissaoDt.setQuantidadeParcelasDUAM(request.getParameter("quantidadeParcelasDUAM"));
		}
		if( request.getParameter("valorImpostoMunicipalDUAM") != null && !(request.getParameter("valorImpostoMunicipalDUAM").equals("null")) && request.getParameter("valorImpostoMunicipalDUAM").length() > 0 ) {
			guiaEmissaoDt.setValorImpostoMunicipalDUAM(request.getParameter("valorImpostoMunicipalDUAM"));
		}
		
		if( request.getParameter("novoValorAcao") != null && request.getParameter("novoValorAcao").length() > 0 ) {
			guiaEmissaoDt.setNovoValorAcao(request.getParameter("novoValorAcao"));
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
			guiaEmissaoDt.setRateioCodigo(request.getParameter("rateioCodigo"));
		}
		
		if( request.getParameter("descontoTaxaJudiciaria") != null && !(request.getParameter("descontoTaxaJudiciaria").equals("null")) && request.getParameter("descontoTaxaJudiciaria").length() > 0 ) {
			guiaEmissaoDt.setDescontoTaxaJudiciaria(request.getParameter("descontoTaxaJudiciaria"));
		}
		
		//******************************************
		//ATENÇÃO: Adicionado este if para a ocorrência 2013/52746 e 
		// reunião do dia 13/06/2013 pela tarde onde juntamente com o Leonardo da corregedoria
		// foi definido a retirada deste item para as guias de execução fiscal. 
		if( processoDt.getProcessoTipoCodigo() != null && processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_FISCAL)) ) {
			guiaEmissaoDt.setDescontoTaxaJudiciaria(String.valueOf(GuiaCalculoNe.DESCONTO_TAXA_JUDICIARIA_100));
		}
		
		if( request.getParameter("atoEscrivao") != null && !(request.getParameter("atoEscrivao").equals("null")) && request.getParameter("atoEscrivao").length() > 0 ) {
			guiaEmissaoDt.setAtoEscrivao(request.getParameter("atoEscrivao"));
		}
		
		if( request.getParameter("atoEscrivaoCivel") != null && !(request.getParameter("atoEscrivaoCivel").equals("null")) && request.getParameter("atoEscrivaoCivel").length() > 0 ) {
			guiaEmissaoDt.setAtoEscrivaoCivel(request.getParameter("atoEscrivaoCivel"));
		}
		
		if( request.getParameter("porcentagemEscrivaesCivel") != null && !(request.getParameter("porcentagemEscrivaesCivel").equals("null")) && request.getParameter("porcentagemEscrivaesCivel").length() > 0 ) {
			guiaEmissaoDt.setPorcentagemEscrivaesCivel(request.getParameter("porcentagemEscrivaesCivel"));
		}
		
		if( request.getParameter("avaliadorQuantidade") != null && request.getParameter("avaliadorQuantidade").length() > 0 ) {
			guiaEmissaoDt.setAvaliadorQuantidade(request.getParameter("avaliadorQuantidade"));
		}
		
		if( request.getParameter("taxaProtocoloQuantidade") != null && request.getParameter("taxaProtocoloQuantidade").length() > 0 ) {
			guiaEmissaoDt.setTaxaProtocoloQuantidade(request.getParameter("taxaProtocoloQuantidade"));
		}
		
		if( request.getParameter("taxaServicoDividaAtivaQuantidade") != null && request.getParameter("taxaServicoDividaAtivaQuantidade").length() > 0 ) {
			guiaEmissaoDt.setTaxaServicoDividaAtivaQuantidade(request.getParameter("taxaServicoDividaAtivaQuantidade"));
		}
		
		if( request.getParameter("distribuidorQuantidade") != null && request.getParameter("distribuidorQuantidade").length() > 0 ) {
			guiaEmissaoDt.setDistribuidorQuantidade(request.getParameter("distribuidorQuantidade"));
		}
		
		if( request.getParameter("leilaoQuantidade") != null && request.getParameter("leilaoQuantidade").length() > 0 ) {
			guiaEmissaoDt.setLeilaoQuantidade(request.getParameter("leilaoQuantidade"));
		}
		
		if( request.getParameter("partidorQuantidade") != null && request.getParameter("partidorQuantidade").length() > 0 ) {
			guiaEmissaoDt.setPartidorQuantidade(request.getParameter("partidorQuantidade"));
		}
		
		if( request.getParameter("codigoOficialSPGPenhora") != null && request.getParameter("codigoOficialSPGPenhora").length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGPenhora(request.getParameter("codigoOficialSPGPenhora"));
		}
		
		if( request.getParameter("codigoOficialSPGLocomocao") != null && request.getParameter("codigoOficialSPGLocomocao").length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocao(request.getParameter("codigoOficialSPGLocomocao"));
		}
		
		if( request.getParameter("codigoOficialSPGLeilao") != null && request.getParameter("codigoOficialSPGLeilao").length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLeilao(request.getParameter("codigoOficialSPGLeilao"));
		}
		
		if( request.getParameter("codigoOficialSPGLocomocaoContaVinculada") != null && request.getParameter("codigoOficialSPGLocomocaoContaVinculada").length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocaoContaVinculada(request.getParameter("codigoOficialSPGLocomocaoContaVinculada"));
		}
		
		if( request.getParameter("atosEscrivaesCivel") != null && request.getParameter("atosEscrivaesCivel").length() > 0 ) {
			guiaEmissaoDt.setAtosEscrivaesCivel(request.getParameter("atosEscrivaesCivel"));
		}
		
		if( request.getParameter("contadorQuantidade") != null && request.getParameter("contadorQuantidade").length() > 0 ) {
			guiaEmissaoDt.setContadorQuantidade(request.getParameter("contadorQuantidade"));
		}
		
		if( request.getParameter("contadorQuantidadeAcrescimo") != null && request.getParameter("contadorQuantidadeAcrescimo").length() > 0 ) {
			guiaEmissaoDt.setContadorQuantidadeAcrescimo(request.getParameter("contadorQuantidadeAcrescimo"));
		}
		
//		if( request.getParameter("correioQuantidade") != null && request.getParameter("correioQuantidade").length() > 0 ) {
//			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("correioQuantidade"));
//		}
		
		if( request.getParameter("rateioQuantidade") != null && request.getParameter("rateioQuantidade").length() > 0 ) {
			guiaEmissaoDt.setRateioQuantidade(request.getParameter("rateioQuantidade"));
		}
		
		if( request.getParameter("depositarioPublico") != null && request.getParameter("depositarioPublico").length() > 0 ) {
			guiaEmissaoDt.setDepositarioPublico(request.getParameter("depositarioPublico"));
		}
		
//		if( request.getParameter("documentoDiarioJustica") != null && request.getParameter("documentoDiarioJustica").length() > 0 ) {
//			guiaEmissaoDt.setDocumentoDiarioJustica(request.getParameter("documentoDiarioJustica"));
//		}
		
		if( request.getParameter("quantidadeAcrescimoPessoa") != null && request.getParameter("quantidadeAcrescimoPessoa").length() > 0 ) {
			guiaEmissaoDt.setQuantidadeAcrescimo(request.getParameter("quantidadeAcrescimoPessoa"));
		}
		
		if( isProcessoSegundoGrau ) {
			GuiaServicosCt.extrairParametrosRequestTaxaServicoItemCusta4(guiaEmissaoDt, request);
		}
		if( !isProcessoSegundoGrau ) {
			GuiaServicosCt.extrairParametrosRequestTaxaServicoItemCusta16(guiaEmissaoDt, request);
		}
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaFinalZeroNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt == null ) {
				serventiaDt = guiaFinalZeroNe.consultarIdServentia(processoDt.getId_Serventia());
			}
			if( serventiaDt != null ) {
				comarcaDt = guiaFinalZeroNe.consultarComarca(serventiaDt.getId_Comarca());
				processoDt.setComarca(serventiaDt.getComarca());
				processoDt.setComarcaCodigo(serventiaDt.getComarcaCodigo());
			}
		}
		
		//Valida soma do Rateio das partes, caso tenha sido escolhido
		Double totalRateio = 0.0D;
		List listaGuiasRateio = new ArrayList();
		List listaTotalGuiaRateio = new ArrayList();
		List listaNomeParteGuia = new ArrayList();
		List listaNomePartePorcentagemGuia = new ArrayList();
		if( request.getParameter("rateioCodigo") != null && ( Funcoes.StringToInt(request.getParameter("rateioCodigo")) == GuiaFinalZeroNe.RATEIO_VARIAVEL || Funcoes.StringToInt(request.getParameter("rateioCodigo")) == GuiaFinalZeroNe.RATEIO_50_50 ) ) {
			
			if( processoDt.getListaPolosAtivos() != null && processoDt.getListaPolosAtivos().size() > 0 ) {
				for(int i = 0; i < processoDt.getListaPolosAtivos().size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(i);
					
					//Pega o valor da porcentagem digitada para a parte
					String VARIAVEL_RATEIO_PARTE_VARIAVEL = request.getParameter(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId());
					
					//Soma o valor digitado para validar os 100%
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						totalRateio += Funcoes.StringToDouble(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Seta na variavel de sessï¿½o este valor
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId(), VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Verifica se emiti a guia para esta parte
					String chekboxEmitirGuiaParte[] = request.getParameterValues(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
					if( chekboxEmitirGuiaParte != null ) {
						//Seta se a parte irï¿½ gerar a guia
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId(), chekboxEmitirGuiaParte[0]);
						
						//Para a criaï¿½ï¿½o das outras guias
						listaNomeParteGuia.add(parteDt.getNome() + ":" + chekboxEmitirGuiaParte[0]);
						listaNomePartePorcentagemGuia.add(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
				}
			}
			if( processoDt.getListaPolosPassivos() != null && processoDt.getListaPolosPassivos().size() > 0 ) {
				for(int i = 0; i < processoDt.getListaPolosPassivos().size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(i);
					
					//Pega o valor da porcentagem digitada para a parte
					String VARIAVEL_RATEIO_PARTE_VARIAVEL = request.getParameter(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId());
					
					//Soma o valor digitado para validar os 100%
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						totalRateio += Funcoes.StringToDouble(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Seta na variavel de sessï¿½o este valor
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId(), VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Verifica se emiti a guia para esta parte
					String chekboxEmitirGuiaParte[] = request.getParameterValues(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
					if( chekboxEmitirGuiaParte != null ) {
						//Seta se a parte irï¿½ gerar a guia
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId(), chekboxEmitirGuiaParte[0]);
						
						//Para a criaï¿½ï¿½o das outras guias
						listaNomeParteGuia.add(parteDt.getNome() + ":" + chekboxEmitirGuiaParte[0]);
						listaNomePartePorcentagemGuia.add(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
				}
			}
			
			guiaEmissaoDt.setTotalRateio(totalRateio);
		}
		if( guiaEmissaoDt.getRateioCodigo() != null && guiaEmissaoDt.getRateioCodigo().length() > 0 && ( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalZeroNe.RATEIO_VARIAVEL || Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalZeroNe.RATEIO_50_50 ) ) {
			request.setAttribute("visualizaDivRateioPartesVariavel", "DivVisivel");
		}
		if( guiaEmissaoDt.getTotalRateio() == 100.0D ) {
			request.setAttribute("formEdicaoInputSomenteLeituraRateio", "formEdicaoInputSomenteLeitura");
		}
		else {
			request.setAttribute("formEdicaoInputSomenteLeituraRateio", "formEdicaoInputSomenteLeituraVermelho");
		}
		request.setAttribute("totalRateio", totalRateio);
		
		switch(paginaatual) {
		
			case Configuracao.Novo: {
				
				if( guiaFinalZeroNe.isServentiaSegundoGrau(processoDt.getId_Serventia()) ) {
					throw new MensagemException("Processo de Segundo Grau, não pode emitir esta guia.");
				}
				
				//Ocorrencia 2020/10357 - Retirado restrição de acesso para outras comarcas para a nova central única dos contadores.
				if( !guiaFinalZeroNe.isProcessoSegundoGrau(serventiaDt.getId()) ) {
					
					stAcao = PAGINA_GUIA_FINAL_ZERO;
					
					boolean conectouSPG = true;
					try{
						List listaOficiaisSPGDt = new OficialSPGNe().consultarOficiaisComarca(comarcaDt.getComarcaCodigo());
						
						OficialSPGDt oficialTJGOSPGDt = new OficialSPGDt();						
						oficialTJGOSPGDt.setNomeOficial(OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA);
						oficialTJGOSPGDt.setCodigoOficial(OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA);
						oficialTJGOSPGDt.setAtivo("1");
						oficialTJGOSPGDt.setCodigoComarca(ComarcaDt.GOIANIA);
						
						listaOficiaisSPGDt.add(0, oficialTJGOSPGDt);
						
						request.getSession().setAttribute("ListaOficiaisSPGDt", listaOficiaisSPGDt);
					}
					catch(Exception e) {
						conectouSPG = false;
					}
					
					if( conectouSPG ) {
					
						guiaEmissaoDt = new GuiaEmissaoDt();
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						request.getSession().removeAttribute("ListaBairroDt");
						request.getSession().removeAttribute("ServentiaDt");
						request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
						request.getSession().removeAttribute("totalRateio");
						request.getSession().removeAttribute("ListaQuantidadeBairroDt");
						request.getSession().removeAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
						
						//Tratamento para processos sigilosos e segredo de justiça
						guiaFinalZeroNe.tratamentoParaProcessosSigilosoSegredoJustica(processoDt);
						
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
						
						request.getSession().setAttribute("ListaCustaDt", guiaFinalZeroNe.consultarItensGuia(null, GuiaTipoDt.ID_FINAL_ZERO, processoDt.getId_ProcessoTipo()));
						
						//Atualização do valor da causa
						TJDataHora database = new TJDataHora();
						database.setDataddMMaaaaHHmmss(processoDt.getDataRecebimento());
						
						guiaEmissaoDt.setDataBaseAtualizacao(database.getDataFormatadaddMMyyyy());						
						guiaEmissaoDt.setDataBaseFinalAtualizacao(Funcoes.dateToStringSoData(new Date()));
						guiaEmissaoDt.setNovoValorAcao( processoDt.getValor() );
						guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaFinalZeroNe.atualizarValorCausaUFR(processoDt.getValor(), Funcoes.StringToDate(processoDt.getDataRecebimento())).toString()) );
						
						//Inicia custas com 1, a pedido do contador Marcelo
						guiaEmissaoDt.setCustasQuantidade("1");
						guiaEmissaoDt.setDistribuidorQuantidade("1");
						guiaEmissaoDt.setContadorQuantidade("1");
						guiaEmissaoDt.setLeilaoQuantidade("1");
						guiaEmissaoDt.setRateioCodigo(null);
						guiaEmissaoDt.setAtosEscrivaesCivel("1");
//						guiaEmissaoDt.setAtoEscrivaoCivel("100");
						guiaEmissaoDt.setPorcentagemEscrivaesCivel("100");
						if( processoDt != null && processoDt.isCriminal() ) {
							guiaEmissaoDt.setAtoEscrivao("100");
						}
						else {
							guiaEmissaoDt.setAtoEscrivao("0");
						}
						
						if( guiaFinalZeroNe.isProcessoTipoExecucaoFiscal(processoDt.getProcessoTipoCodigo()) ) {
							guiaEmissaoDt.setTaxaServicoDividaAtivaQuantidade(GuiaEmissaoDt.VALOR_SIM);
						}
						else {
							guiaEmissaoDt.setTaxaServicoDividaAtivaQuantidade(null);
						}
						
						request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
						
						//Verifica para emitir mensage se jï¿½ existe guia do mesmo tipo
						if( guiaFinalZeroNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_FINAL_ZERO) ) {
							
							String mensagemErro = "";
							if( request.getParameter("MensagemErro") != null && request.getParameter("MensagemErro").length() > 0 ) {
								mensagemErro = "<br /><br /><b>Atenção:</b><br />" + request.getParameter("MensagemErro");
							}
							
							request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO) + mensagemErro);
						}
						
						request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
						
						//Ocorrencia 2020/5668
						//É advogado? Então não mostra campo na tela do item 16.X (false)
						//Não é advogado? Sim, mostra. (true)
						request.setAttribute("MostrarCampoItem16X", new Boolean(true));
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
				guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaFinalZeroNe.atualizarValorCausaUFR(guiaEmissaoDt.getNovoValorAcao(), Funcoes.StringToDate(guiaEmissaoDt.getDataBaseFinalAtualizacao())).toString()) );
				
				break;
			}
			
			//Apresenta Prï¿½via de Cï¿½lculo
			case Configuracao.Curinga6 : {
				switch(passoEditar) {
					
					//Apresentar Mensagem
					case Configuracao.Mensagem : {
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE));
						
						//Atenï¿½ï¿½o!
						//Nï¿½o tem break pq precisa mostrar a tela novamente de prï¿½via.
					}
					
					//Utilizado na Volta da Prï¿½via para a Pï¿½gina da Guia
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
						request.getSession().removeAttribute("ListaGuiasRateio");
						request.getSession().removeAttribute("ListaTotalGuiaRateio");
						request.getSession().removeAttribute("ListaNomeParteGuia");
						request.getSession().removeAttribute("ListaNomePartePorcentagemGuia");
						
						guiaFinalZeroNe.validarSomaRateioPartes(request, guiaEmissaoDt, listaGuiasRateio, listaTotalGuiaRateio, listaNomeParteGuia, listaNomePartePorcentagemGuia, listaPromoventes, listaPromovidos);
						
						
						//Consulta lista de itens da guia
						//listaItensGuia = guiaFinalNe.consultarItensGuia(guiaEmissaoDt, GuiaTipoDt.FINAL_ZERO, processoDt.getId_ProcessoTipo());
						listaItensGuia = null;
						
						if( listaItensGuia == null ) {
							GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_FINAL_ZERO, processoDt.getId_ProcessoTipo());
							guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
						}
						
						if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
							List listaAux = guiaFinalZeroNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
							if( listaAux != null ) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
								listaItensGuia.addAll(listaAux);
							}
						}
						
						//Locomoï¿½ï¿½o com Zona-Bairro
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
								
								//Adicionar item de Locomoï¿½ï¿½o
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									listaItensGuia.add( guiaFinalZeroNe.adicionarItemLocomocaoOficial(bairroLocomocaoDt.getBairroDt(), bairroLocomocaoDt.getOficialSPGDt()) );
								}
							}
						}
						
						//Locomoï¿½ï¿½o com Conta Vinculada
						List valoresIdBairroContaVinculada = null;
						listaBairroLocomocaoContaVinculada = (List<BairroLocomocaoDt>)request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
						listaQuantidadeBairroLocomocaoContaVinculada = new ArrayList();
						if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
							if( valoresIdBairroContaVinculada == null )
								valoresIdBairroContaVinculada = new ArrayList();
							
							for(int i = 0; i < listaBairroLocomocaoContaVinculada.size(); i++) {
								BairroLocomocaoDt bairroLocomocaoDt = (BairroLocomocaoDt)listaBairroLocomocaoContaVinculada.get(i);
								
								int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocaoContaVinculada"+i));
								for(int j = 0; j < quantidadeLocomocao; j++) {
									valoresIdBairroContaVinculada.add(bairroLocomocaoDt.getBairroDt().getId());
								}
								
								listaQuantidadeBairroLocomocaoContaVinculada.add(String.valueOf(quantidadeLocomocao));
								request.getSession().setAttribute("ListaQuantidadeBairroLocomocaoContaVinculada", listaQuantidadeBairroLocomocaoContaVinculada);
								
								//Adicionar item de Locomoï¿½ï¿½o
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									listaItensGuia.add( guiaFinalZeroNe.adicionarItemLocomocaoOficialContaVinculada(bairroLocomocaoDt.getBairroDt(), bairroLocomocaoDt.getOficialSPGDt()) );
								}
							}
						}
						
						
						Map valoresReferenciaCalculo = new HashMap();
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcao());
						valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcao());
						if( guiaEmissaoDt.getNovoValorAcaoAtualizado().toString().length() == 0 ) {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0.0");
						}
						else {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, guiaEmissaoDt.getNovoValorAcaoAtualizado());
						}
						if( valoresIdBairro != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
						if( valoresIdBairroContaVinculada != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_CONTA_VINCULADA, valoresIdBairroContaVinculada);
						
						
						//Itens taxax de serviço regimento 4 e 16
						if( listaItensGuia == null )
							listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
						guiaFinalZeroNe.adicionarItemCalculo(guiaEmissaoDt, listaItensGuia);
						
						//Contador
						if( guiaEmissaoDt.getContadorQuantidade() != null && guiaEmissaoDt.getContadorQuantidade().length() > 0 && !guiaEmissaoDt.getContadorQuantidade().equals("0") ) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.CONTADOR) );
						}
						
						//Contador Acrescimo
						if( guiaEmissaoDt.getContadorQuantidadeAcrescimo() != null && guiaEmissaoDt.getContadorQuantidadeAcrescimo().length() > 0 && !guiaEmissaoDt.getContadorQuantidadeAcrescimo().equals("0") ) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES_10_MINIMO_ITEM_5) );
						}
						
						//Atos escrivães do civel
						if( guiaEmissaoDt.getAtosEscrivaesCivel() != null && guiaEmissaoDt.getAtosEscrivaesCivel().length() > 0 && !guiaEmissaoDt.getAtosEscrivaesCivel().equals("0") ) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							guiaFinalZeroNe.adicionarItemCautelarContencioso(listaItensGuia, guiaEmissaoDt, processoDt);
							
//							valoresReferenciaCalculo.put(CustaDt.PORCENTAGEM_ESCRIVAO_CIVEL, guiaEmissaoDt.getAtoEscrivaoCivel().toString());
						}
						
						//Ocorrência 2019/12325
						//Porcentagem escrivão do cível
						if( guiaEmissaoDt.getPorcentagemEscrivaesCivel() != null && !guiaEmissaoDt.getPorcentagemEscrivaesCivel().isEmpty() ) {
							
							valoresReferenciaCalculo.put(CustaDt.PORCENTAGEM_ESCRIVAO_CIVEL, guiaEmissaoDt.getPorcentagemEscrivaesCivel());
						}
						
						//Custas Contador
						if( guiaEmissaoDt.getCustasQuantidade() != null && guiaEmissaoDt.getCustasQuantidade().length() > 0 && !guiaEmissaoDt.getCustasQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.CUSTAS) );
						}
						
						//Atos dos Escrivães
						if( guiaEmissaoDt.getAtoEscrivao() != null && guiaEmissaoDt.getAtoEscrivao().toString().length() > 0 && !guiaEmissaoDt.getAtoEscrivao().equals("0") ) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.AUTUACAO_E_OU_PROCESSAMENTO_DE_FEITOS) );
							
							valoresReferenciaCalculo.put(CustaDt.PROCESSO_PROCEDIMENTO_ORDINARIO, guiaEmissaoDt.getAtoEscrivao().toString());
						}
						
						//Taxa Protocolo
						if( guiaEmissaoDt.getTaxaProtocoloQuantidade() != null && guiaEmissaoDt.getTaxaProtocoloQuantidade().length() > 0 && !guiaEmissaoDt.getTaxaProtocoloQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.TAXA_PROTOCOLO) );
						}
						
						//Taxa de Serviço Dívida Ativa: Para quando for execução fiscal não cobra taxa de protocolo e sim taxa de serviço
						if( guiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade() != null && guiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade().length() > 0 && !guiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.TAXA_SERVICO_DIVIDA_ATIVA) );
						}
						
						//Distribuidor
						if( guiaEmissaoDt.getDistribuidorQuantidade() != null && guiaEmissaoDt.getDistribuidorQuantidade().length() > 0 && !guiaEmissaoDt.getDistribuidorQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.ATOS_DE_DISTRIBUICAO_DOS_PROCESSOS_FISICOS_APLICA_SE_10_SOBRE_VALOR_MINIMO_ITEM_5I) );
						}
						
						//Atualizaï¿½ï¿½o valor nominal
						if( guiaEmissaoDt.getAtualizacaoValorNominalQuantidade() != null && guiaEmissaoDt.getAtualizacaoValorNominalQuantidade().length() > 0 && !guiaEmissaoDt.getAtualizacaoValorNominalQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.ATUALIZACAO_MOEDA_NACIONAL) );
						}
						
						//Conversao moeda nacional
						if( guiaEmissaoDt.getTransformacaoMoedaQuantidade() != null && guiaEmissaoDt.getTransformacaoMoedaQuantidade().length() > 0 && !guiaEmissaoDt.getTransformacaoMoedaQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.CONVERSAO_MOEDA_NACIOMAL) );
						}
						
//						//Despesa Postal
//						if( guiaEmissaoDt.getCorreioQuantidade() != null && guiaEmissaoDt.getCorreioQuantidade().length() > 0 && !guiaEmissaoDt.getCorreioQuantidade().equals("0")) {
//							if( listaItensGuia == null )
//								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
//							
//							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.DESPESA_POSTAL) );
//						}
						

						//Retificaï¿½ï¿½es de Contas de Custas
						if( guiaEmissaoDt.getRetificacaoCustasQuantidade() != null && guiaEmissaoDt.getRetificacaoCustasQuantidade().length() > 0 && !guiaEmissaoDt.getRetificacaoCustasQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.RETIFICACAO_CONTA_CUSTAS) );
						}
						
						
						//Taxa de Serviço
						if( guiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade() != null && guiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade().length() > 0 && !guiaEmissaoDt.getTaxaServicoDividaAtivaQuantidade().equals(GuiaEmissaoDt.VALOR_NAO)) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.TAXA_SERVICO_DIVIDA_ATIVA) );
						}
						
						
						//Quantidade de Acrescimo por Pessoa
						if( guiaEmissaoDt.getQuantidadeAcrescimo() != null && guiaEmissaoDt.getQuantidadeAcrescimo().length() > 0 && !guiaEmissaoDt.getQuantidadeAcrescimo().equals("0")) {
							valoresReferenciaCalculo.put(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA, guiaEmissaoDt.getQuantidadeAcrescimo());
						}
						
						
						//Desconto Taxa Judiciaria
						if( guiaEmissaoDt.getDescontoTaxaJudiciaria() != null && guiaEmissaoDt.getDescontoTaxaJudiciaria().length() > 0 ) {
							valoresReferenciaCalculo.put(CustaDt.DESCONTO_TAXA_JUDICIARIA, guiaEmissaoDt.getDescontoTaxaJudiciaria());
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
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.DEPOSITO_COMPREENDENDO_OS_REGISTROS_GUARDA_ESCRITURACAO) );
						}
						
						//Avaliador
						if( guiaEmissaoDt.getAvaliadorQuantidade() != null && guiaEmissaoDt.getAvaliadorQuantidade().length() > 0 && guiaEmissaoDt.getAvaliadorValor().length() > 0 && !guiaEmissaoDt.getAvaliadorQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA) );
						}
						
						//Leilão ou Pregão
						if( guiaEmissaoDt.getLeilaoQuantidade().length() > 0 && guiaEmissaoDt.getLeilaoValor().length() > 0 && !guiaEmissaoDt.getLeilaoQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.PREGAO_PRACAO_LEILAO) );
						}
						
						//Partidor
						if( guiaEmissaoDt.getPartidorQuantidade().length() > 0 && !guiaEmissaoDt.getPartidorQuantidade().equals("0") ) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.CUSTA_PARTIDOR) );
						}
						
//						//Por documento publicado diário da justiça
//						if( guiaEmissaoDt.getDocumentoDiarioJustica() != null && guiaEmissaoDt.getDocumentoDiarioJustica().length() > 0 && !guiaEmissaoDt.getDocumentoDiarioJustica().equals("0")) {
//							if( listaItensGuia == null )
//								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
//							
//							listaItensGuia.add( guiaFinalZeroNe.adicionarItem(CustaDt.POR_DOCUMENTO_PUBLICADO) );
//						}
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = new ArrayList();
						
						listaGuiaItemDt = guiaFinalZeroNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						
						if( !guiaFinalZeroNe.isGuiaZeradaOuNegativa() ) {
							//Altera o nome do item de cï¿½digo de regimento 61
							for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
								GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
								if( guiaItemDt.getCustaDt().getCodigoRegimento().equals("61") ) { // CustaDt.CUSTA_PENHORA
									guiaItemDt.getCustaDt().setArrecadacaoCusta("AUTOS");
									break;
								}
							}
							
							//Outras Guias do Rateio
							if( listaGuiaItemDt != null &&
								listaGuiaItemDt.size() > 0 &&
								
								listaNomeParteGuia != null && 
								listaNomeParteGuia.size() > 0 ) {
								
								for( int i = 0; i < listaNomeParteGuia.size(); i++ ) {
									List listaRateioGuiaItemDt = guiaFinalZeroNe.calcularItensGuiaRateio(guiaEmissaoDt, listaGuiaItemDt, Funcoes.StringToDouble(listaNomePartePorcentagemGuia.get(i).toString()));
									
									listaGuiasRateio.add( listaRateioGuiaItemDt );
								}
								
								if( listaGuiasRateio.size() > 0 ) {
									for( int m = 0; m < listaGuiasRateio.size(); m++ ) {
										listaTotalGuiaRateio.add(guiaFinalZeroNe.calcularTotalGuia((List)listaGuiasRateio.get(m)));
									}
									request.getSession().setAttribute("ListaGuiasRateio", listaGuiasRateio);
									request.getSession().setAttribute("ListaTotalGuiaRateio", listaTotalGuiaRateio);
									request.getSession().setAttribute("ListaNomeParteGuia", listaNomeParteGuia);
									request.getSession().setAttribute("ListaNomePartePorcentagemGuia", listaNomePartePorcentagemGuia);
								}
								else {
									request.getSession().removeAttribute("ListaGuiasRateio");
								}
							}
							
							
							//Obtem o id_GuiaModelo
							if( listaItensGuia != null && listaItensGuia.size() > 0) {
								GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
								if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							
							//Deve haver no mï¿½nimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
								
								boolean apresentaPrevia = true;
								if( Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaFinalZeroNe.RATEIO_VARIAVEL || Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaFinalZeroNe.RATEIO_50_50 ) {
//									if( totalRateio != 100.0D ) {
									if( new BigDecimal(guiaEmissaoDt.getTotalRateio()).compareTo(new BigDecimal(100.D)) != 0 ) {
										request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_TOTAL_RATEIO_ERRADO));
										stAcao = PAGINA_GUIA_FINAL_ZERO;
										apresentaPrevia = false;
									}
								}
								if( apresentaPrevia ) {
								
									request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
									request.setAttribute("TotalGuia", guiaFinalZeroNe.getGuiaCalculoNe().getTotalGuia() );
									
									request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
									request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaFinalZeroNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
									request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaFinalZeroNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
									request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
									request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
									request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
									
									ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
									processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
									processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
									processoCadastroDt.setValor(processoDt.getValor());
									processoCadastroDt.setProcessoTipo(processoDt.getProcessoTipo());
									
									guiaEmissaoDt.setValorAcao(processoCadastroDt.getValor());
									/*
									 * Alteração da data de vencimento realizada de acordo com pedido da financeira na 
									 * ocorrência 2017/10105.
									 */
									guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia30Dias());
									
									request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
									request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
									
									stAcao = PAGINA_GUIA_PREVIA_CALCULO;
									request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA));
									
								}
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
								stAcao = PAGINA_GUIA_FINAL_ZERO;
							}
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
							stAcao = PAGINA_GUIA_FINAL_ZERO;
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
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaFinalZeroNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaFinalZeroNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				
				
//				//Quando tem Rateio
//				if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_50_50)) ) {
//					
//		            GuiaEmissaoDt guiaEmissaoDt2 = (GuiaEmissaoDt) Funcoes.serializeObjeto(guiaEmissaoDt);
//		            
//					//GuiaEmissaoDt guiaEmissaoDt2 = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
//					if( guiaEmissaoDt2 == null )
//						guiaEmissaoDt2 = new GuiaEmissaoDt();
//					
//					//Obtï¿½m o prï¿½ximo nï¿½mero de Guia
//					guiaEmissaoDt2.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//					guiaEmissaoDt2.setNumeroGuiaCompleto( guiaFinalZeroNe.getNumeroGuiaCompleto(guiaEmissaoDt2.getNumeroGuia()) );
//					
//					guiaFinalZeroNe.calcularRateio50Porcento(guiaEmissaoDt.getListaGuiaItemDt());
//					guiaFinalZeroNe.calcularRateio50Porcento(guiaEmissaoDt2.getListaGuiaItemDt());
//					
//					guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
//					guiaEmissaoDt2.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
//
//					guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
//					
//					guiaFinalZeroNe.salvar(guiaEmissaoDt2, guiaEmissaoDt2.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
//				}
				
				//ProcessoParteTipoCodigo
				if( guiaEmissaoDt.getProcessoParteTipoCodigo() == null ) {
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_100_REQUERENTE)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					}
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalZeroNe.RATEIO_100_REQUERIDO)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
					}
				}
				
				//Salvar GuiaEmissï¿½o
				List listaAuxGuiaEmissaoDtRateioImpressao = null;
				if( request.getSession().getAttribute("ListaGuiasRateio") == null && 
					request.getSession().getAttribute("ListaTotalGuiaRateio") == null ) {

					guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
					
					guiaFinalZeroNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				else {
					//Salvar as guias que teve rateio
					listaGuiasRateio = (List) request.getSession().getAttribute("ListaGuiasRateio");
					listaNomeParteGuia = (List) request.getSession().getAttribute("ListaNomeParteGuia");
					listaNomePartePorcentagemGuia = (List)request.getSession().getAttribute("ListaNomePartePorcentagemGuia");
					
					listaAuxGuiaEmissaoDtRateioImpressao = new ArrayList();
					
					if( listaGuiasRateio != null && listaGuiasRateio.size() > 0 ) {
						for( int i = 0; i < listaGuiasRateio.size(); i++ ) {
							GuiaEmissaoDt auxGuiaEmissaoDt = new GuiaEmissaoDt();
							
							auxGuiaEmissaoDt.copiar(guiaEmissaoDt);
							
							auxGuiaEmissaoDt.setId_ProcessoParteResponsavelGuia(listaNomeParteGuia.get(i).toString().split(":")[1]);
							
							if( processoDt.getListaPolosAtivos() != null ) {
								for(int contadorPromoventes = 0; contadorPromoventes < processoDt.getListaPolosAtivos().size(); contadorPromoventes++) {
									ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(contadorPromoventes);
									if( parteDt.getId().equals(auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia()) ) {
										
										auxGuiaEmissaoDt.setRequerente(guiaEmissaoDt.getNomeParte( auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) + " (" + listaNomePartePorcentagemGuia.get(i) +"%)");
										auxGuiaEmissaoDt.setRequerido(auxGuiaEmissaoDt.getNomePrimeiroRequerido());
										
										break;
									}
								}
							}
							if( processoDt.getListaPolosPassivos() != null ) {
								for(int contadorPromovidos = 0; contadorPromovidos < processoDt.getListaPolosPassivos().size(); contadorPromovidos++) {
									ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(contadorPromovidos);
									if( parteDt.getId().equals(auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia()) ) {
										
										auxGuiaEmissaoDt.setRequerente(auxGuiaEmissaoDt.getNomePrimeiroRequerente());
										auxGuiaEmissaoDt.setRequerido(guiaEmissaoDt.getNomeParte( auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) + " (" + listaNomePartePorcentagemGuia.get(i) +"%)");
										
										break;
									}
								}
							}
							
							auxGuiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
							auxGuiaEmissaoDt.setNumeroGuiaCompleto( guiaFinalZeroNe.getNumeroGuiaCompleto(auxGuiaEmissaoDt.getNumeroGuia()) );
							auxGuiaEmissaoDt.setListaGuiaItemDt( (List) listaGuiasRateio.get(i) );
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
							
							guiaFinalZeroNe.salvar(auxGuiaEmissaoDt, (List) listaGuiasRateio.get(i), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
							
							listaAuxGuiaEmissaoDtRateioImpressao.add(auxGuiaEmissaoDt);
						}
					}
				}
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						
						if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
							for( int i = 0; i < processoDt.getListaPolosAtivos().size(); i++ ) {
								ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(i);
								
								if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
									guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
								}
							}
							for( int i = 0; i < processoDt.getListaPolosPassivos().size(); i++ ) {
								ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(i);
								
								if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
									guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
								}
							}
						}
						
						//Geraï¿½ï¿½o da guia PDF
						byte[] byTemp = guiaFinalZeroNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_FINAL_ZERO, "FINAL ZERO");
						
						String nome="GuiaFinalZero_Processo_"+ guiaEmissaoDt.getNumeroProcesso();
						
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
					stTemp = guiaFinalZeroNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			//Busca de Bairro Locomoï¿½ï¿½o Conta Vinculada
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
					stTemp = guiaFinalZeroNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			default : {
				//Busca Custa
				stId = request.getParameter("tempBuscaId_Custa");
				if( stId != null ) {
					CustaDt custaDt = guiaFinalZeroNe.consultarCustaDtPorId(stId);
					
					if( listaCustaDt != null )
						listaCustaDt.add(custaDt);
					
					request.getSession().setAttribute("ListaCustaDt", listaCustaDt);
					
					stId = null;
				}
				
				//Busca Bairro
				AdicioneLocomocao(request, "tempBuscaId_Bairro", guiaEmissaoDt.getCodigoOficialSPGLocomocao(), listaBairroDt, listaQuantidadeBairroDt, "ListaBairroDt", "ListaQuantidadeBairroDt");
				
				//Busca Bairro Locomoçao Conta Vinculada
				AdicioneLocomocao(request, "tempBuscaId_BairroLocomocaoContaVinculada", guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada(), listaBairroLocomocaoContaVinculada, listaQuantidadeBairroLocomocaoContaVinculada, "ListaBairroLocomocaoContaVinculada", "ListaQuantidadeBairroLocomocaoContaVinculada");
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected void AdicioneLocomocao(HttpServletRequest request, String stIdRequestBairro, 
            					   String codigoOficialSPG, List<BairroLocomocaoDt> listaBairroDt, 
                                   List listaQuantidadeBairroDt, String stIdSessionListaBairro, String stIdSessionListaQtdeBairro) throws Exception {
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
			
			if (OficialSPG == null) {
				super.exibaMensagemInconsistenciaErro(request, "Favor selecionar o oficial de justiça antes de consultar o bairro");
			} else {
				BairroDt bairroDt = guiaFinalZeroNe.consultarBairroId(stId);
			
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
	
	/**
	 * Mï¿½todo para verificar se guia deve adicionar DUAM ou nï¿½o.
	 * @return
	 */
	public boolean isDUAM() {
		boolean retorno = false;
		
		
		
		return retorno;
	}
}
