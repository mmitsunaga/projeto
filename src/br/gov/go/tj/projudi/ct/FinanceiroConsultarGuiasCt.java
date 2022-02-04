package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.FinanceiroConsultarGuiasDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.FinanceiroConsultarGuiasNe;
import br.gov.go.tj.projudi.ne.GuiaCalculoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaItemNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.GuiaSSGNe;
import br.gov.go.tj.projudi.ne.GuiaStatusNe;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import br.gov.go.tj.projudi.ne.LocomocaoNe;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class FinanceiroConsultarGuiasCt extends Controle {

	private static final long serialVersionUID = -1804822061775298448L;

	private static final String PAGINA_FINANCEIRO_CONSULTAR_GUIAS 	= "/WEB-INF/jsptjgo/FinanceiroConsultarGuias.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 			= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	@Override
	public int Permissao() {
		return FinanceiroConsultarGuiasDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		String stAcao 		= PAGINA_FINANCEIRO_CONSULTAR_GUIAS;
		int passoEditar 	= -1;
		List<GuiaEmissaoDt> tempList = null;
		
		int validaPagamento = -1;
		String numeroGuiaCompletoValidarSPG = null;
		String dataAnalise = null;
		
		List listaGuiaTipoDt 		= null;
		List listaGuiaStatusDt 		= null;
		
		//********************************************
		//Variáveis Ne
		GuiaEmissaoNe guiaEmissaoNe 	= new GuiaEmissaoNe();
		GuiaTipoNe guiaTipoNe 			= new GuiaTipoNe();
		GuiaStatusNe guiaStatusNe 		= new GuiaStatusNe();
		FinanceiroConsultarGuiasNe financeiroConsultarGuiasNe = new FinanceiroConsultarGuiasNe();
		
		//********************************************
		//Pesquisas em Ne auxiliares
		listaGuiaTipoDt 	= guiaTipoNe.consultarDescricao();
		listaGuiaStatusDt 	= guiaStatusNe.consultarDescricao();
		
		//********************************************
		//Variáveis de sessão
		FinanceiroConsultarGuiasDt financeiroConsultarGuiasDt = (FinanceiroConsultarGuiasDt) request.getSession().getAttribute("FinanceiroConsultarGuiasDt");
		if( financeiroConsultarGuiasDt == null ) {
			financeiroConsultarGuiasDt = new FinanceiroConsultarGuiasDt();
		}
		
		//********************************************
		//Requests - Attribute
		request.setAttribute("tempPrograma" 					, "Financeiro - Consultar Guias");
		request.setAttribute("tempRetorno" 						, "obtenhaUrlJSON()");
		request.setAttribute("PaginaAtual" 						, PosicaoPaginaAtual);
		request.setAttribute("PaginaEditar" 					, passoEditar);
		request.setAttribute("PosicaoPaginaAtual" 				, Funcoes.StringToLong(PosicaoPaginaAtual));
		
		request.setAttribute("listaGuiaTipoDt" 					, listaGuiaTipoDt);
		request.setAttribute("listaGuiaStatusDt" 				, listaGuiaStatusDt);
		request.setAttribute("emitirGuiaLocomocaoComplementar"	, null);
		
		//********************************************
		//Requests - Parameter
		String numeroGuiaInformado = "";
		if( request.getParameter("numeroGuiaCompleto") != null ) {
			numeroGuiaInformado = request.getParameter("numeroGuiaCompleto").trim();
			
			numeroGuiaInformado = numeroGuiaInformado.trim().replaceAll("/", "").replaceAll("-", "").replaceAll(" ", "");
		}
		financeiroConsultarGuiasDt.setNumeroGuiaCompleto( numeroGuiaInformado );
		financeiroConsultarGuiasDt.setNumeroProcesso( request.getParameter("numeroProcesso") );
		financeiroConsultarGuiasDt.setId_GuiaTipo( request.getParameter("Id_GuiaTipo") );
		financeiroConsultarGuiasDt.setId_GuiaStatus( request.getParameter("Id_GuiaStatus") );
		financeiroConsultarGuiasDt.setDataInicioEmissao( request.getParameter("dataInicioEmissao") );
		financeiroConsultarGuiasDt.setDataFimEmissao( request.getParameter("dataFimEmissao") );
		financeiroConsultarGuiasDt.setDataInicioRecebimento( request.getParameter("dataInicioRecebimento") );
		financeiroConsultarGuiasDt.setDataFimRecebimento( request.getParameter("dataFimRecebimento") );
		financeiroConsultarGuiasDt.setDataInicioCancelamento( request.getParameter("dataInicioCancelamento") );
		financeiroConsultarGuiasDt.setDataFimCancelamento( request.getParameter("dataFimCancelamento") );
		financeiroConsultarGuiasDt.setDataInicioCertidao( request.getParameter("dataInicioCertidao") );
		financeiroConsultarGuiasDt.setDataFimCertidao( request.getParameter("dataFimCertidao") );
		financeiroConsultarGuiasDt.setOrdenacao( request.getParameter("ordenacao") );
		
		if( request.getParameter("PaginaEditar") != null && request.getParameter("PaginaEditar").toString().length() > 0 ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PaginaEditar"));
		}
		if( request.getParameter("validaPagamento") != null && request.getParameter("validaPagamento").length() > 0 ) {
			validaPagamento = Integer.parseInt(request.getParameter("validaPagamento"));
		}
		if( request.getParameter("dataAnalise") != null && request.getParameter("dataAnalise").length() > 0 ) {
			dataAnalise = request.getParameter("dataAnalise");
		}
		if( request.getParameter("numeroGuiaCompletoValidarSPG") != null && request.getParameter("numeroGuiaCompletoValidarSPG").length() > 0 ) {
			numeroGuiaCompletoValidarSPG = request.getParameter("numeroGuiaCompletoValidarSPG").toString();
		}
		
		switch(paginaatual) {
			case Configuracao.Novo : {
				
				financeiroConsultarGuiasDt = new FinanceiroConsultarGuiasDt();
				request.getSession().removeAttribute("GuiaEmissaoDt");
				request.getSession().removeAttribute("FinanceiroConsultarGuiasCt_ListaGuiaEmissaoDt");
				request.getSession().removeAttribute("FinanceiroConsultarGuiasCt_ValorTotalGuiasConsultadas");
				
				request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
				request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
				
				break;
			}
			
			case Configuracao.Curinga6 : {
				
				switch(validaPagamento) {
					case Configuracao.Curinga6 : {
						
						if( numeroGuiaCompletoValidarSPG != null ) {
							try {
								String dataPagamento = financeiroConsultarGuiasNe.validaGuiaPagaSPG(numeroGuiaCompletoValidarSPG, dataAnalise);
								if( dataPagamento != null && dataPagamento.length() > 0 ) {
									//request.setAttribute("MensagemOk", "A guia " + Funcoes.FormatarNumeroSerieGuia(numeroGuiaCompletoValidarSPG) + " foi atualizada com o pagamento do dia " + dataPagamento);
									redireciona(response, "FinanceiroConsultarGuias?PaginaAtual="+Configuracao.Novo+"&MensagemOk=A guia " + Funcoes.FormatarNumeroSerieGuia(numeroGuiaCompletoValidarSPG) + " foi atualizada com o pagamento do dia " + dataPagamento);
									return;
								}
								else {
									//request.setAttribute("MensagemOk", "Ainda não foi identificado no SPG pagamento para a guia " + Funcoes.FormatarNumeroSerieGuia(numeroGuiaCompletoValidarSPG));
									redireciona(response, "FinanceiroConsultarGuias?PaginaAtual="+Configuracao.Novo+"&MensagemOk=Ainda não foi identificado no SPG pagamento para a guia " + Funcoes.FormatarNumeroSerieGuia(numeroGuiaCompletoValidarSPG));
									return;
								}
							}
							catch(Exception e) {
								redireciona(response, "FinanceiroConsultarGuias?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+e);
								return;
							}
						}
						
						break;
					}
				}
				
				break;
			}
			
			case Configuracao.Localizar : {
				
				switch(passoEditar) {
					case -1 : {
						if (MostraDetalhesDaGuia(request)) {
							stAcao = PAGINA_GUIA_PREVIA_CALCULO;
							request.setAttribute("tempRetorno", "FinanceiroConsultarGuias");
						}
						break;
					}
					
					case -2 : {
						String conteutoJson = guiaEmissaoNe.consultarGuiaEmissaoJSON(financeiroConsultarGuiasDt, PosicaoPaginaAtual);						
						enviarJSON(response, conteutoJson);																								
						return;
					}
					
					case -3 : {
						
						if( financeiroConsultarGuiasDt.getNumeroGuiaCompleto().length() > 0 		||
							financeiroConsultarGuiasDt.getNumeroProcesso().length() > 0 			||
							financeiroConsultarGuiasDt.getId_GuiaTipo().length() > 0 				||
							financeiroConsultarGuiasDt.getId_GuiaStatus().length() > 0 				||
							(
								financeiroConsultarGuiasDt.getDataInicioEmissao().length() > 0 		&& 
								financeiroConsultarGuiasDt.getDataFimEmissao().length() > 0 
							) 																		||
							(
								financeiroConsultarGuiasDt.getDataInicioRecebimento().length() > 0 	&&
								financeiroConsultarGuiasDt.getDataFimRecebimento().length() > 0 
							)																		||
							(
								financeiroConsultarGuiasDt.getDataInicioCancelamento().length() > 0 &&
								financeiroConsultarGuiasDt.getDataFimCancelamento().length() > 0 
							)
																									||
							(
								financeiroConsultarGuiasDt.getDataInicioCertidao().length() > 0 &&
								financeiroConsultarGuiasDt.getDataFimCertidao().length() > 0 
							)
						   ) 
						{
							tempList = guiaEmissaoNe.consultarGuiaEmissao(financeiroConsultarGuiasDt);
							
							if( tempList != null ) {
								
								GuiaItemNe guiaItemNe = new GuiaItemNe();
								
								String valorTotalGuiasConsultadas = guiaItemNe.consultarTotalGuias(tempList);
								
								request.getSession().removeAttribute("GuiaEmissaoDt");
								
								request.getSession().setAttribute("FinanceiroConsultarGuiasCt_ListaGuiaEmissaoDt", tempList);
								request.getSession().setAttribute("FinanceiroConsultarGuiasCt_ValorTotalGuiasConsultadas", valorTotalGuiasConsultadas);
								
							} else {
								request.setAttribute("MensagemOk", "Nenhuma Guia Emitida pelo Projudi Localizada.");
							}
						 } else {
							request.setAttribute("MensagemErro", "Por favor, informe no mínimo 1 parâmetro no filtro.");
						 }					
						
						
						break;
					}
				}
				
				break;
			}
			
		}
		
		request.getSession().setAttribute("FinanceiroConsultarGuiasDt", financeiroConsultarGuiasDt);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	public boolean MostraDetalhesDaGuia(HttpServletRequest request) throws Exception
	{
		GuiaEmissaoNe GuiaEmissaone = new GuiaEmissaoNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		CertidaoNe certidaoNe = new CertidaoNe();
		LogNe logNe = new LogNe();
		
		//GuiaEmissaoDt guiaEmissaoDt = GuiaEmissaone.consultarGuiaEmissao(request.getParameter("tempBuscaId").toString());
		GuiaEmissaoDt guiaEmissaoDt = GuiaEmissaone.consultarGuiaEmissaoNumeroGuia(request.getParameter("tempBuscaId").toString());
				
		if (guiaEmissaoDt == null || guiaEmissaoDt.getGuiaModeloDt() == null) {
			request.setAttribute("MensagemErro", "Guia não localizada com o id " + request.getParameter("tempBuscaId") + ". Consulte novamente por favor.");
			return false;
		}
		
		List<LogDt> listaLogDtHistoricoLogsGuia = null;
		GuiaEmissaoDt guiaSPG = guiaSPGNe.consultarGuiaEmissaoSPG(guiaEmissaoDt.getNumeroGuiaCompleto());
		if( guiaSPG != null ) {
			if( guiaEmissaoDt.getId() != null && guiaSPG.getId() != null ) {
				listaLogDtHistoricoLogsGuia = logNe.consultarLogsGuia(guiaEmissaoDt.getId(), guiaSPG.getId(), guiaEmissaoDt.getNumeroGuiaCompleto());
			}
		}
		else {
			GuiaEmissaoDt guiaSSG = guiaSSGNe.consultarGuiaEmissaoSSG(guiaEmissaoDt.getNumeroGuiaCompleto());
			if( guiaSSG != null ) {
				if( guiaEmissaoDt.getId() != null && guiaSSG.getId() != null ) {
					listaLogDtHistoricoLogsGuia = logNe.consultarLogsGuia(guiaEmissaoDt.getId(), guiaSSG.getId(), guiaEmissaoDt.getNumeroGuiaCompleto());
				}
			}
		}
		if( listaLogDtHistoricoLogsGuia != null ) {
			request.setAttribute("listaLogDtHistoricoLogsGuia", listaLogDtHistoricoLogsGuia);
		}
		
		List listaGuiaItemDt = GuiaEmissaone.consultarGuiaItens(guiaEmissaoDt.getId(), guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo());
		
		//Deve haver no mínimo 1 item de guia
		if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {		
			
			GuiaCalculoNe guiaCalculoNe = new GuiaCalculoNe();
			guiaCalculoNe.recalcularTotalGuia(listaGuiaItemDt);
			
			request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
			request.setAttribute("TotalGuia", guiaCalculoNe.getTotalGuia() );
			
			request.getSession().setAttribute("ListaGuiaItemDt" 	, listaGuiaItemDt);
			request.getSession().setAttribute("TotalGuia" 			, Funcoes.FormatarDecimal( guiaCalculoNe.getTotalGuia().toString() ) );

			ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
			ProcessoDt processoDt = null;
			
			if( guiaEmissaoDt.getId_Processo() != null && guiaEmissaoDt.getId_Processo().length() > 0 ) {
				processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo());
				
				processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
				processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
				processoCadastroDt.setValor(processoDt.getValor());
				processoCadastroDt.setProcessoTipo(processoDt.getProcessoTipo());
				
				guiaEmissaoDt.setListaRequerentes(processoCadastroDt.getListaPolosAtivos());
				guiaEmissaoDt.setListaRequeridos(processoCadastroDt.getListaPolosPassivos());
				guiaEmissaoDt.setListaPartesLitisconsorte(GuiaEmissaone.consultarPartesLitisconsorteAtivoPassivo(processoDt.getId()));
				guiaEmissaoDt.setListaOutrasPartes(GuiaEmissaone.consultarOutrasPartes(processoDt.getId()));
			}
			
			//Consulta o usuário
			guiaEmissaoDt.setUsuario( GuiaEmissaone.consultarUsuario(guiaEmissaoDt.getId_Usuario()) );
			
			//Consultar nome da guia caso seja guia genï¿½rica
			if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
				guiaEmissaoDt.setGuiaTipo( GuiaEmissaone.consultarGuiaTipo(null, guiaEmissaoDt.getId_GuiaTipo() ) );
			}
			
			guiaEmissaoDt.setId_Apelante(guiaEmissaoDt.getId_Apelante());
			guiaEmissaoDt.setId_Apelado(guiaEmissaoDt.getId_Apelado());
			
			GuiaEmissaone.setNomeApelanteNomeApelado(guiaEmissaoDt);
			
			//Se tiver id guia referencia consulta
			GuiaEmissaone.consultarGuiaReferencia(guiaEmissaoDt);
			
			//Se tiver id guia principal
			GuiaEmissaone.consultarGuiaPrincipal(guiaEmissaoDt);
			
			if( guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_FAZENDA_MUNICIPAL) ) {
				//Altera nome do regimento 61:
				for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
					GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
					if( guiaItemDt.getCustaDt().getCodigoRegimento().equals("61") ) {// CustaDt.CUSTA_PENHORA
						guiaItemDt.getCustaDt().setArrecadacaoCusta("AUTOS");
					}
					if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("31") ) {// DUAM
						guiaItemDt.getCustaDt().setArrecadacaoCusta("DUAM NR. " + guiaEmissaoDt.getNumeroDUAM() + " (1/" + guiaEmissaoDt.getQuantidadeParcelasDUAM() + ")");
					}
					if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("24") && guiaItemDt.getParcelas().length() > 0 ) {// HONORARIOS DO PROCURADOR
						guiaItemDt.getCustaDt().setArrecadacaoCusta("HON. PROCURADORES MUNICIPAIS (" + guiaItemDt.getParcelaCorrente() + "/" + guiaItemDt.getParcelas() + ")");
					}
					if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("26") && guiaItemDt.getCodigoOficial() != null && guiaItemDt.getCodigoOficial().length() > 0){// LOCOMOCAO PARA OFICIAL AD HOC
						//Gera nome oficial para ser exibido na previa
						OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
						request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
						//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
						guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
					}
				}
			}
			
			if( guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_GUIA_GENERICA) ) {
				//Altera nome do regimento 61:
				for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
					GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
					if( guiaItemDt.getCustaDt().getId().equals("82") && guiaItemDt.getCodigoOficial() != null && guiaItemDt.getCodigoOficial().length() > 0){// LOCOMOCAO PARA OFICIAL AD HOC
						//Gera nome oficial para ser exibido na previa
						OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
						request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
						//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
						guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
					}
					if( guiaItemDt.getCustaDt().getId().equals("81") && guiaItemDt.getCodigoOficial() != null && guiaItemDt.getCodigoOficial().length() > 0){// LOCOMOCAO PARA OFICIAL AD HOC
						//Gera nome oficial para ser exibido na previa
						OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
						request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
						//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
						guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
					}
				}
			}
			
			if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
				if( processoCadastroDt.getListaPolosAtivos() != null ) {
					for( int i = 0; i < processoCadastroDt.getListaPolosAtivos().size(); i++ ) {
						ProcessoParteDt parteDt = (ProcessoParteDt)processoCadastroDt.getListaPolosAtivos().get(i);
						
						if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
							guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
						}
					}
				}
				if( processoCadastroDt.getListaPolosPassivos() != null ) {
					for( int i = 0; i < processoCadastroDt.getListaPolosPassivos().size(); i++ ) {
						ProcessoParteDt parteDt = (ProcessoParteDt)processoCadastroDt.getListaPolosPassivos().get(i);
						
						if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
							guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
						}
					}
				}
			}
			
			//guia utilizada certidao projudi?
			if( certidaoNe.isGuiaJaUtilizada(guiaEmissaoDt.getNumeroGuiaCompleto()) ) {
				request.setAttribute("visualizarGuiaUtilizadaCertidaoProjudi" , new Boolean(true));
				CertidaoValidacaoDt certidaoValidacaoDt = certidaoNe.consultarNumeroGuia(guiaEmissaoDt.getNumeroGuiaCompleto());
				if( certidaoValidacaoDt != null ) {
					request.setAttribute("visualizarDataEmissaoCertidao", Funcoes.FormatarData(certidaoValidacaoDt.getDataEmissao()));
				}
			}
			
			//guia tem boleto emitido?
			if( GuiaEmissaone.isBoletoEmitido(guiaEmissaoDt.getId()) ) {
				request.setAttribute("visualizarBoletoEmitido" , new Boolean(true));
			}
			
			request.removeAttribute("emitirGuiaLocomocaoComplementar");
			request.removeAttribute("emitirGuiaInicialLocomocaoComplementar");
			request.setAttribute("ListLocomocaoMandado", new LocomocaoNe().consultarLocomocaoMandadoSPG(guiaEmissaoDt.getNumeroGuiaCompleto()));
			request.setAttribute("visualizarBotaoSalvarGuia" , new Boolean(false));
			request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", new Boolean(false));
			request.setAttribute("visualizarLinkProcesso" , new Boolean(false));
			request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", new Boolean(false));						
			request.setAttribute("visualizarBotaoVoltar" , new Boolean(true));
			request.setAttribute("comandoOnClickBotaoVoltar" , "AlterarValue('PaginaAtual','" + Configuracao.Novo + "');");
			if ( guiaEmissaoDt.getId_GuiaStatus() != null && 
				(guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.AGUARDANDO_PAGAMENTO)) ||
				 guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.ESTORNO_BANCARIO)))) {
				request.setAttribute("visualizarBotaoValidarPagamentoGuia" , new Boolean(true));
			}
			
			request.setAttribute("visualizarDadosProcesso" , new Boolean(true));
			
			request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
			request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
			request.getSession().setAttribute("processoDt", processoDt);
			
			return true;
		}
		else {
			request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
			return false;
		}
	}
}
