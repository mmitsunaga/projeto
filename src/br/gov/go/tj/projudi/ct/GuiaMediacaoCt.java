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

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaMediacaoDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaMediacaoNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaMediacaoCt extends Controle {

	private static final long serialVersionUID = 5698705201694757774L;
	
	private static final String PAGINA_GUIA_MEDIACAO 			= "/WEB-INF/jsptjgo/GuiaMediacao.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	private GuiaMediacaoNe guiaMediacaoNe = new GuiaMediacaoNe();

	@Override
	public int Permissao() {
		return GuiaMediacaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;		
		
		stAcao = obtenhaPaginaPrincipal();
		
		//********************************************
		// Variáveis utilizadas pela página
		List listaCustaDt 		= null;
		List listaItensGuia 	= null;
		
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
		}
		
		listaItensGuia = (List) request.getSession().getAttribute("ListaItensGuia");
		if( listaItensGuia == null ) {
			listaItensGuia = new ArrayList();
		}
		
		listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
		if( listaCustaDt == null ) {
			listaCustaDt = new ArrayList();
		}
		
		//********************************************
		//Requests 
		request.setAttribute("tempPrograma" 			, obtenhaTituloPagina());
		request.setAttribute("tempRetorno" 				, obtenhaServletDeRetornoPesquisa());
		request.setAttribute("tempRetornoBuscaProcesso" , obtenhaServletBuscaProcesso());
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		if( request.getParameter("valorMediacao") != null && request.getParameter("valorMediacao").length() > 0 ) {
			guiaEmissaoDt.setValorMediacao(request.getParameter("valorMediacao"));
		}
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaMediacaoNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				comarcaDt = guiaMediacaoNe.consultarComarca(serventiaDt.getId_Comarca());
				processoDt.setComarca(serventiaDt.getComarca());
			}
		}
		
		
		switch(paginaatual) {
			case Configuracao.Novo: {
				
				if( guiaMediacaoNe.isConexaoSPG_OK() ) {
					stAcao = obtenhaPaginaPrincipal();
					
					guiaEmissaoDt = new GuiaEmissaoDt();
					request.getSession().removeAttribute("ListaGuiaItemDt");
					request.getSession().removeAttribute("ListaCustaDt");
					request.getSession().removeAttribute("TotalGuia");
					request.getSession().removeAttribute("GuiaEmissaoDt");
					request.getSession().removeAttribute("ListaPartesLitisconsorte");
					
					guiaEmissaoDt.setListaPartesLitisconsorte(guiaMediacaoNe.consultarPartesLitisconsorteAtivoPassivo(processoDt.getId()));
					guiaEmissaoDt.setListaOutrasPartes(guiaMediacaoNe.consultarOutrasPartes(processoDt.getId()));
					
					request.getSession().setAttribute("ListaPartesLitisconsorte", guiaEmissaoDt.getListaPartesLitisconsorte());
					request.getSession().setAttribute("ListaCustaDt", guiaMediacaoNe.consultarItensGuia(null, GuiaTipoDt.ID_MEDIACAO, processoDt.getId_ProcessoTipo()));
					
					//Verifica para emitir mensage se jï¿½ existe guia do mesmo tipo
					if( guiaMediacaoNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_MEDIACAO) ) {
						request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
					}
				}
				else {
					redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG)));
					return;
				}
				
				break;
			}
			
			//Apresenta Prévia de Cálculo
			case Configuracao.Curinga6 : {
				switch(passoEditar) {
					
					//Apresentar Mensagem
					case Configuracao.Mensagem : {
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE));
						
						//Atenção!
						//Não tem break pq precisa mostrar a tela novamente de prï¿½via.
					}
					
					case Configuracao.Curinga8: {
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO)));
							return;
						}
						
						guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_MEDIACAO, processoDt.getId_ProcessoTipo()));
						
						
						
						Map valoresReferenciaCalculo = new HashMap();
//						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				processoDt.getValor());
//						valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, 			guiaEmissaoDt.getNovoValorAcao());
						
						//Mediadores
						if( guiaEmissaoDt.getValorMediacao() != null && guiaEmissaoDt.getValorMediacao().length() > 0 && !guiaEmissaoDt.getValorMediacao().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaMediacaoNe.adicionarItem(CustaDt.MEDIACAO) );
							valoresReferenciaCalculo.put(CustaDt.ITEM_MEDIACAO, guiaEmissaoDt.getValorMediacao());
							
						}
						 
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = guiaMediacaoNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						
						
						if( !guiaMediacaoNe.isGuiaZeradaOuNegativa() ) {
							//Obtem o id_GuiaModelo
							if( listaItensGuia != null && listaItensGuia.size() > 0) {
								GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
								if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							//Deve haver no mínimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
								
								request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.setAttribute("TotalGuia", guiaMediacaoNe.getGuiaCalculoNe().getTotalGuia() );
								
								request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
								request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaMediacaoNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
								request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaMediacaoNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
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
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
								stAcao = obtenhaPaginaPrincipal();
							}
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
							stAcao = obtenhaPaginaPrincipal();
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
//				//Obtêm o próximo número de Guia
//				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaMediacaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
//						redireciona(response, "PagamentoOnLine?PaginaAtual=" + Configuracao.Curinga6 + "&PassoEditar=" + Configuracao.Curinga6 + "&tempRetornoBuscaProcesso=" + obtenhaServletBuscaProcesso() + "&sv=" + PagamentoOnLineNe.PERMITE_SALVAR_SIM);
//						return;
//					}
//					
//					//Banco Caixa Econômica
//					case Configuracao.Curinga7 : {
//						return;
//					}
//					
//					//Banco Itaú
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
				
					//Remover Locomoção
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
				}
				
				break;
			}
			
			//Impressão da guia
			case Configuracao.Imprimir : {
				
				//Valida o processo da sua aba
				if( request.getParameter("guiaIdProcesso") != null && 
					!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
					
					redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO)));
					return;
				}
				
				guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
				if( guiaEmissaoDt == null )
					guiaEmissaoDt = new GuiaEmissaoDt();
				
				//Obtêm o próximo número de Guia
				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaMediacaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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

				guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
				
				//Salvar GuiaEmissao
				guiaMediacaoNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Geração da guia PDF
						byte[] byTemp = guiaMediacaoNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_MEDIACAO, "MEDIAÇÃO");
						
						String nome="GuiaMediacao_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;
						
						enviarPDF(response, byTemp, nome);
						return;
					}
					
					case Configuracao.Salvar : {
						redireciona(response, obtenhaAcaoMensagemOk(processoDt, "Guia Emitida com Sucesso!"));
						return;
					}
				}
				break;
			}
			
			default : {
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected String obtenhaPaginaPrincipal() {
		return PAGINA_GUIA_MEDIACAO;
	}
	
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaMediacao";
	}
	
	protected String obtenhaTituloPagina() {
		return "Guia de Mediação";
	}
	
	protected String obtenhaServletBuscaProcesso() {
		return "BuscaProcesso";
	}

	protected String obtenhaAcaoMensagemOk(ProcessoDt processoDt, String mensagem) {
		return obtenhaServletBuscaProcesso() + "?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk="+mensagem;
	}
	
	protected String obtenhaAcaoMensagemErro(ProcessoDt processoDt, String mensagem) {
		return obtenhaServletBuscaProcesso() + "?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+mensagem;
	}
}
