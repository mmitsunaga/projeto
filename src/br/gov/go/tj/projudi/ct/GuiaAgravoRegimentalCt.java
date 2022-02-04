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
import br.gov.go.tj.projudi.dt.GuiaAgravoRegimentalDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaAgravoRegimentalNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaAgravoRegimentalCt extends Controle {

	private static final long serialVersionUID = -2446623657473259715L;
	
	private static final String PAGINA_GUIA_AGRAVO_REGIMENTAL 	= "/WEB-INF/jsptjgo/GuiaAgravoRegimental.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	private GuiaAgravoRegimentalNe guiaAgravoRegimentalNe = new GuiaAgravoRegimentalNe();

	@Override
	public int Permissao() {
		return GuiaAgravoRegimentalDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;
		String stId 		= "";
		
		stAcao = PAGINA_GUIA_AGRAVO_REGIMENTAL;
		
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
		request.setAttribute("tempPrograma" 			, "Guia de Agravo Regimental");
		request.setAttribute("tempRetorno" 				, "GuiaAgravoRegimental");
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
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
		
		if( (request.getParameter("porteRemessaQuantidade") != null) && !(request.getParameter("porteRemessaQuantidade").equals("null")) ) {
			guiaEmissaoDt.setPorteRemessaQuantidade(request.getParameter("porteRemessaQuantidade").toString());
		}
		
		
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaAgravoRegimentalNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				if( serventiaDt.getId_Comarca() != null ) {
					comarcaDt = guiaAgravoRegimentalNe.consultarComarca(serventiaDt.getId_Comarca());
				}
				processoDt.setComarca(serventiaDt.getComarca());
			}
		}
		
		
		guiaAgravoRegimentalNe.consultarNomeProcessoParte(guiaEmissaoDt);
		
		
		switch(paginaatual) {
			case Configuracao.Novo: {
				
				if( guiaAgravoRegimentalNe.validaAcessoEmissaoGuiaAgravoRegimental(serventiaDt.getId()) ) {
					stAcao = PAGINA_GUIA_AGRAVO_REGIMENTAL;
					
					if( guiaAgravoRegimentalNe.isConexaoSPG_OK() ) {
					
						guiaEmissaoDt = new GuiaEmissaoDt();
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						
						request.getSession().setAttribute("ListaCustaDt", guiaAgravoRegimentalNe.consultarItensGuia(null, GuiaTipoDt.ID_AGRAVO_REGIMENTAL, processoDt.getId_ProcessoTipo()));
						
						//Verifica para emitir mensage se j� existe guia do mesmo tipo
						if( guiaAgravoRegimentalNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_AGRAVO_REGIMENTAL) ) {
							request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
						}
					}
					else {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
						return;
					}
				}
				else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Aten��o! Sem permiss�o para emitir guia de <b>AGRAVO REGIMENTAL</b> para este processo e esta serventia.");
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
							
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							return;
						}
						
						
						guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_AGRAVO_REGIMENTAL, processoDt.getId_ProcessoTipo()));
						
						if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
							List listaAux = guiaAgravoRegimentalNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
							if( listaAux != null ) {
								listaItensGuia.addAll(listaAux);
							}
						}
						
						
						
						Map valoresReferenciaCalculo = new HashMap();
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				processoDt.getValor());
						valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, 			guiaEmissaoDt.getNovoValorAcao());
						
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = guiaAgravoRegimentalNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						
						
						
						if( !guiaAgravoRegimentalNe.isGuiaZeradaOuNegativa() ) {
							//Obtem o id_GuiaModelo
							if( listaItensGuia != null && listaItensGuia.size() > 0) {
								GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
								if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							//Deve haver no m�nimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
								
								request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.setAttribute("TotalGuia", guiaAgravoRegimentalNe.getGuiaCalculoNe().getTotalGuia() );
								
								request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
								request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaAgravoRegimentalNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
								request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaAgravoRegimentalNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
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
								stAcao = PAGINA_GUIA_AGRAVO_REGIMENTAL;
							}
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
							stAcao = PAGINA_GUIA_AGRAVO_REGIMENTAL;
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
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaAgravoRegimentalNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
					
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
					return;
				}
				
				guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
				if( guiaEmissaoDt == null )
					guiaEmissaoDt = new GuiaEmissaoDt();
				
				//Obt�m o pr�ximo n�mero de Guia
				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaAgravoRegimentalNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				
				//Salvar GuiaEmiss�o
				guiaAgravoRegimentalNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Gera��o da guia PDF
						byte[] byTemp = guiaAgravoRegimentalNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_AGRAVO_REGIMENTAL, "AGRAVO REGIMENTAL");						
						String nome="GuiaRecursoApelacao_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;												
						enviarPDF(response, byTemp,nome);						
						return;
					}
					
					case Configuracao.Salvar : {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=Guia Emitida com Sucesso!");
						return;
					}
				}
				break;
			}
			
			default : {
				//Busca Custa
				stId = request.getParameter("tempBuscaId_Custa");
				if( stId != null ) {
					CustaDt custaDt = guiaAgravoRegimentalNe.consultarCustaDtPorId(stId);
					
					if( listaCustaDt != null )
						listaCustaDt.add(custaDt);
					
					request.getSession().setAttribute("ListaCustaDt", listaCustaDt);
					
					stId = null;
				}
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}