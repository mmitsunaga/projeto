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
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaServicosDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.GuiaServicosNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaServicosCt extends Controle {

	private static final long serialVersionUID = 2446627473259715L;
	
	private static final String PAGINA_GUIA_SERVICOS 			= "/WEB-INF/jsptjgo/GuiaServicos.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	private GuiaServicosNe guiaServicosNe = new GuiaServicosNe();
	
	@Override
	public int Permissao() {
		return GuiaServicosDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;		
		
		stAcao = obtenhaPaginaPrincipal();
		boolean isProcessoSegundoGrau = false;
		
		//********************************************
		// Vari�veis utilizadas pela p�gina
		List listaCustaDt 		= null;
		List listaItensGuia 	= null;
		
		//********************************************
		//Vari�veis objetos
		GuiaEmissaoDt guiaEmissaoDt = null;
		ProcessoDt processoDt = null;
		
		//********************************************
		//Vari�veis de sess�o
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
				isProcessoSegundoGrau = guiaServicosNe.isProcessoSegundoGrau(processoDt.getId_Serventia());
				request.setAttribute("ProcessoSegundoGrau", isProcessoSegundoGrau);
			}
		}
		else {
			throw new MensagemException("Processo n�o identificado. Por favor, acesse o processo novamente.");
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
		
		if( isProcessoSegundoGrau ) {
			this.extrairParametrosRequestTaxaServicoItemCusta4(guiaEmissaoDt, request);
		}
		if( !isProcessoSegundoGrau ) {
			this.extrairParametrosRequestTaxaServicoItemCusta16(guiaEmissaoDt, request);
		}
		
		if( request.getParameter("taxaJudiciariaServicoCertidao") != null && request.getParameter("taxaJudiciariaServicoCertidao").length() > 0 ) {
			guiaEmissaoDt.setTaxaJudiciariaServicoCertidao(request.getParameter("taxaJudiciariaServicoCertidao"));
		}
		
		
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaServicosNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				if( serventiaDt.getId_Comarca() != null ) {
					comarcaDt = guiaServicosNe.consultarComarca(serventiaDt.getId_Comarca());
				}
				processoDt.setComarca(serventiaDt.getComarca());
			}
		}
		
		
		switch(paginaatual) {
			case Configuracao.Novo: {
				
				if( guiaServicosNe.isConexaoSPG_OK() ) {
					stAcao = obtenhaPaginaPrincipal();
					
					guiaEmissaoDt = new GuiaEmissaoDt();
					request.getSession().removeAttribute("ListaGuiaItemDt");
					request.getSession().removeAttribute("ListaCustaDt");
					request.getSession().removeAttribute("TotalGuia");
					request.getSession().removeAttribute("GuiaEmissaoDt");
					request.getSession().removeAttribute("ListaPartesLitisconsorte");
					
					if( processoDt != null && processoDt.getId() != null ) {
						guiaEmissaoDt.setListaPartesLitisconsorte(guiaServicosNe.consultarPartesLitisconsorteAtivoPassivo(processoDt.getId()));
						guiaEmissaoDt.setListaOutrasPartes(guiaServicosNe.consultarOutrasPartes(processoDt.getId()));
						
						request.getSession().setAttribute("ListaPartesLitisconsorte", guiaEmissaoDt.getListaPartesLitisconsorte());
					}
					
					request.getSession().setAttribute("ListaCustaDt", guiaServicosNe.consultarItensGuia(null, GuiaTipoDt.ID_SERVICOS, processoDt.getId_ProcessoTipo()));
					
					//Verifica para emitir mensage se j� existe guia do mesmo tipo
					if( guiaServicosNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_SERVICOS) ) {
						request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
					}
					
					//Ocorrencia 2020/5668
					//� advogado? Ent�o n�o mostra campo na tela do item 16.X (false)
					//N�o � advogado? Sim, mostra. (true)
					request.setAttribute("MostrarCampoItem16X", new Boolean(true));
					if( guiaServicosNe.isEmissorAdvogado(UsuarioSessao.getId_Usuario(), UsuarioSessao.getId_Serventia()) ) {
						request.setAttribute("MostrarCampoItem16X", new Boolean(false));
					}
				}
				else {
					redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG)));
					return;
				}
				
				break;
			}
			
			//Apresenta Pr�via de C�lculo
			case Configuracao.Curinga6 : {
				switch(passoEditar) {
					
					//Apresentar Mensagem
					case Configuracao.Mensagem : {
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE));
						
						//Aten��o!
						//N�o tem break pq precisa mostrar a tela novamente de pr�via.
					}
					
					case Configuracao.Curinga8: {
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO)));
							return;
						}
						
						guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_SERVICOS, processoDt.getId_ProcessoTipo()));
						
						
						
						Map valoresReferenciaCalculo = new HashMap();
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				processoDt.getValor());
						valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, 			guiaEmissaoDt.getNovoValorAcao());
						
						
//						//Reiniciando valor quantidade taxa judici�ria de servi�o site tjgo item 6 
						guiaEmissaoDt.setTaxaJudiciariaServicoCertidao("0");
						
						//Itens taxax de servi�o regimento 4 e 16
						if( listaItensGuia == null )
							listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
						guiaServicosNe.adicionarItemCalculo(guiaEmissaoDt, listaItensGuia);
						
						//Ocorr�ncia 2018/15691
						//Adicionado cobran�a do item 6 da taxa judici�ria do site do TJGO (http://www.tjgo.jus.br/index.php/tribunal/tribunal-servicos/tribunal-servicos-taxasindicadores/tribunal-servicos-taxasindicadores-taxajudiciaria)
						if( request.getParameter("taxaJudiciariaServicoCertidao") != null && request.getParameter("taxaJudiciariaServicoCertidao").length() > 0 && !request.getParameter("taxaJudiciariaServicoCertidao").equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							guiaEmissaoDt.setTaxaJudiciariaServicoCertidao(String.valueOf(Funcoes.StringToInt(guiaEmissaoDt.getTaxaJudiciariaServicoCertidao()) + Funcoes.StringToInt(request.getParameter("taxaJudiciariaServicoCertidao").toString())));
							
							listaItensGuia.add( guiaServicosNe.adicionarItem(CustaDt.TAXA_JUDICIARIA_SERVICO_CERTIDAO_SITE_TJGO_ITEM_6) );
						}
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = guiaServicosNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						
						
						if( !guiaServicosNe.isGuiaZeradaOuNegativa() ) {
							//Obtem o id_GuiaModelo
							if( listaItensGuia != null && listaItensGuia.size() > 0) {
								GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
								if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							//Deve haver no m�nimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
								
								request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.setAttribute("TotalGuia", guiaServicosNe.getGuiaCalculoNe().getTotalGuia() );
								
								request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
								request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaServicosNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
								request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaServicosNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
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
//				//Obt�m o pr�ximo n�mero de Guia
//				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaServicosNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
//					//Banco Caixa Econ�mica
//					case Configuracao.Curinga7 : {
//						return;
//					}
//					
//					//Banco Ita�
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
				
					//Remover Locomo��o
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
			
			//Impress�o da guia
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
				
				//Obt�m o pr�ximo n�mero de Guia
				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaServicosNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				guiaServicosNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Gera��o da guia PDF
						byte[] byTemp = guiaServicosNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_SERVICOS, "SERVICOS");
						
						String nome="GuiaServicos_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;
						
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
		return PAGINA_GUIA_SERVICOS;
	}
	
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaServicos";
	}
	
	protected String obtenhaTituloPagina() {
		return "Guia de Servi�os";
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
	
protected static void extrairParametrosRequestTaxaServicoItemCusta4(GuiaEmissaoDt guiaEmissaoDt, HttpServletRequest request) throws Exception {
		
		if( request.getParameter("certidaoAcordao") != null && request.getParameter("certidaoAcordao").length() > 0 ) {
			guiaEmissaoDt.setCertidaoAcordao(request.getParameter("certidaoAcordao"));
		}
		
		if( request.getParameter("desarquivamento") != null && request.getParameter("desarquivamento").length() > 0 ) {
			guiaEmissaoDt.setDesarquivamento(request.getParameter("desarquivamento"));
		}
		
		if( request.getParameter("restauracao") != null && request.getParameter("restauracao").length() > 0 ) {
			guiaEmissaoDt.setRestauracao(request.getParameter("restauracao"));
		}
		
		if( request.getParameter("documentoPublicadoQuantidade") != null && request.getParameter("documentoPublicadoQuantidade").length() > 0 ) {
			guiaEmissaoDt.setDocumentoPublicadoQuantidade(request.getParameter("documentoPublicadoQuantidade"));
		}
		
		if( request.getParameter("PorteRemessaQuantidade") != null && request.getParameter("PorteRemessaQuantidade").length() > 0 ) {
			guiaEmissaoDt.setPorteRemessaQuantidade(request.getParameter("PorteRemessaQuantidade"));
		}
		
		if( request.getParameter("correioQuantidadeReg4VI") != null && request.getParameter("correioQuantidadeReg4VI").length() > 0 ) {
			guiaEmissaoDt.setCorreioQuantidadeReg4VI(request.getParameter("correioQuantidadeReg4VI"));
		}
		
		if( request.getParameter("emissaoDocumentoQuantidade") != null && request.getParameter("emissaoDocumentoQuantidade").length() > 0 ) {
			guiaEmissaoDt.setEmissaoDocumentoQuantidade(request.getParameter("emissaoDocumentoQuantidade"));
		}
		
		if( request.getParameter("atosConstricao") != null && request.getParameter("atosConstricao").length() > 0 ) {
			guiaEmissaoDt.setAtosConstricao(request.getParameter("atosConstricao"));
		}
	}
	
	protected static void extrairParametrosRequestTaxaServicoItemCusta16(GuiaEmissaoDt guiaEmissaoDt, HttpServletRequest request) throws Exception {
		if( request.getParameter("certidaoDecisao") != null && request.getParameter("certidaoDecisao").length() > 0 ) {
			guiaEmissaoDt.setCertidaoDecisao(request.getParameter("certidaoDecisao"));
		}
		
		if( request.getParameter("correioQuantidade") != null && request.getParameter("correioQuantidade").length() > 0 ) {
			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("correioQuantidade"));
		}
		
		if( request.getParameter("desarquivamento16II") != null && request.getParameter("desarquivamento16II").length() > 0 ) {
			guiaEmissaoDt.setDesarquivamento16II(request.getParameter("desarquivamento16II"));
		}
		
		if( request.getParameter("restauracaoAtos") != null && request.getParameter("restauracaoAtos").length() > 0 ) {
			guiaEmissaoDt.setRestauracaoAtos(request.getParameter("restauracaoAtos"));
		}
		
		if( request.getParameter("documentoDiarioJustica") != null && request.getParameter("documentoDiarioJustica").length() > 0 ) {
			guiaEmissaoDt.setDocumentoDiarioJustica(request.getParameter("documentoDiarioJustica"));
		}
		
		if( request.getParameter("PorteRemessaQuantidade16V") != null && request.getParameter("PorteRemessaQuantidade16V").length() > 0 ) {
			guiaEmissaoDt.setPorteRemessaQuantidade16V(request.getParameter("PorteRemessaQuantidade16V"));
		}
		
		if( request.getParameter("emissaoDocumentoQuantidade16VII") != null && request.getParameter("emissaoDocumentoQuantidade16VII").length() > 0 ) {
			guiaEmissaoDt.setEmissaoDocumentoQuantidade16VII(request.getParameter("emissaoDocumentoQuantidade16VII"));
		}
		
		if( request.getParameter("atosConstricao16VIII") != null && request.getParameter("atosConstricao16VIII").length() > 0 ) {
			guiaEmissaoDt.setAtosConstricao16VIII(request.getParameter("atosConstricao16VIII"));
		}
		
		if( request.getParameter("FormalPartilhaCartaQuantidade16IX") != null && request.getParameter("FormalPartilhaCartaQuantidade16IX").length() > 0 ) {
			guiaEmissaoDt.setFormalPartilhaCartaQuantidade16IX(request.getParameter("FormalPartilhaCartaQuantidade16IX"));
		}
		
		if( request.getParameter("cumprimentoPrecatoria") != null && request.getParameter("cumprimentoPrecatoria").length() > 0 ) {
			guiaEmissaoDt.setCumprimentoPrecatoria(request.getParameter("cumprimentoPrecatoria"));
		}
	}
}
