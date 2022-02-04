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
import br.gov.go.tj.projudi.dt.GuiaRecursoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.GuiaRecursoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaRecursoCt extends Controle {

	private static final long serialVersionUID = -2446623657473259715L;
	
	private static final String PAGINA_GUIA_RECURSO 			= "/WEB-INF/jsptjgo/GuiaRecurso.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	private GuiaRecursoNe guiaRecursoNe = new GuiaRecursoNe();

	@Override
	public int Permissao() {
		return GuiaRecursoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;
		List tempList 		= null;
		String stId 		= "";
		
		stAcao = PAGINA_GUIA_RECURSO;
		
		//********************************************
		// Variáveis utilizadas pela página
		List listaCustaDt 		= null;
		List listaItensGuia 	= null;
		
		//********************************************
		//Variï¿½veis objetos
		GuiaEmissaoDt guiaEmissaoDt = null;
		ProcessoDt processoDt = null;
		
		//********************************************
		//Variï¿½veis de sessï¿½o
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
		request.setAttribute("tempPrograma" 			, "Guia Recurso de Apelação");
		request.setAttribute("tempRetorno" 				, "GuiaRecurso");
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
		
		if( (request.getParameter("porteRemessaValorManual") != null) && !(request.getParameter("porteRemessaValorManual").equals("null")) ) {
			guiaEmissaoDt.setPorteRemessaValorManual(request.getParameter("porteRemessaValorManual").toString());
		}
		
		if( (request.getParameter("custasValorManual") != null) && !(request.getParameter("custasValorManual").equals("null")) ) {
			guiaEmissaoDt.setCustasValorManual(request.getParameter("custasValorManual").toString());
		}
		
		if( (request.getParameter("protocoloIntegrado") != null) && !(request.getParameter("protocoloIntegrado").equals("null")) ) {
			guiaEmissaoDt.setProtocoloIntegrado(request.getParameter("protocoloIntegrado").toString());
		}
		
		if( request.getParameter("id_apelante") != null && !(request.getParameter("id_apelante").equals("null")) && request.getParameter("id_apelante").toString().length() > 0 ) {
			guiaEmissaoDt.setId_Apelante(request.getParameter("id_apelante").toString());
		}
		
		if( request.getParameter("id_apelado") != null && !(request.getParameter("id_apelado").equals("null")) && request.getParameter("id_apelado").toString().length() > 0 ) {
			guiaEmissaoDt.setId_Apelado(request.getParameter("id_apelado").toString());
		}
		
		
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaRecursoNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				if( serventiaDt.getId_Comarca() != null ) {
					comarcaDt = guiaRecursoNe.consultarComarca(serventiaDt.getId_Comarca());
				}
				processoDt.setComarca(serventiaDt.getComarca());
			}
		}
		
		
		boolean isContador = guiaRecursoNe.isEmissorContador(UsuarioSessao.getId_Usuario()); //fazer consulta
		request.setAttribute("isContador", isContador);
		
		
		guiaRecursoNe.consultarNomeProcessoParte(guiaEmissaoDt);
		
		
		
		switch(paginaatual) {
			case Configuracao.Novo: {
				
				//Comentado a pedido do contador Luíz Carlos do segundo grau por telefone. 06/11/2018 às 15hs
//				if( guiaRecursoNe.isServentiaSegundoGrau(processoDt.getId_Serventia()) ) {
//					throw new MensagemException("Processo de Segundo Grau, não pode emitir esta guia.");
//				}
				
				//***********************************************************
				//***********************************************************
				//***********************************************************
				/*
				//ATENÇÃO
				 
				 Regra de negócio:
				 Para emitir esta guia existe 3 validações que deve ser seguidas.
				 1 - O processo não pode estar em juizado, pois estando nessas serventias somente recurso inominado.
				 2 - O processo deve estar no 1º grau.
				 3 - Caso o processo esteja no 2º grau, somente o contador pode emitir.
				 
				 */
				//***********************************************************
				if( serventiaDt != null && guiaRecursoNe.validaAcessoEmissaoGuia(serventiaDt.getId()) ) {
//				if( true ) {
					
					boolean isProcessoSegundoGrau = guiaRecursoNe.isProcessoSegundoGrau(serventiaDt.getId());
					boolean podeEmitir = false;
					
					if( !isProcessoSegundoGrau ) {
						podeEmitir = true;
					}
					else {
						isContador = guiaRecursoNe.isEmissorContador(UsuarioSessao.getId_Usuario()); //fazer consulta
						
						if( isContador ) {
							podeEmitir = true;
						}
						else {
							podeEmitir = false;
						}
					}
					//***********************************************************
					//***********************************************************
					//***********************************************************
					
					if( podeEmitir ) {
					
						if( guiaRecursoNe.isConexaoSPG_OK() ) {
							stAcao = PAGINA_GUIA_RECURSO;
							
							guiaEmissaoDt = new GuiaEmissaoDt();
							request.getSession().removeAttribute("ListaGuiaItemDt");
							request.getSession().removeAttribute("ListaCustaDt");
							request.getSession().removeAttribute("TotalGuia");
							request.getSession().removeAttribute("GuiaEmissaoDt");
														
							request.getSession().setAttribute("ListaCustaDt", guiaRecursoNe.consultarItensGuia(null, GuiaTipoDt.ID_RECURSO, processoDt.getId_ProcessoTipo()));
							
							if( isContador ) {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_CONTADOR_EMISSAO_DE_GUIA));
							}
							
							//Verifica para emitir mensage se jï¿½ existe guia do mesmo tipo
							if( guiaRecursoNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_RECURSO) ) {
								request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
							}
						}
						else {
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
							return;
						}
					}
					else {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Atenção! Sem permissão para emitir guia de <b>RECURSO APELAÇÃO</b> para este processo.");
						return;
					}
				}
				else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Atenção! Sem permissão para emitir guia de <b>RECURSO APELAÇÃO</b> para este processo.");
					return;
				}
				
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
					
					case Configuracao.Curinga8: {
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							return;
						}
						
						if( (guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelado() != null) ||
							(guiaEmissaoDt.getId_Apelante() == null && guiaEmissaoDt.getId_Apelado() != null) ||
							(guiaEmissaoDt.getId_Apelante() != null && guiaEmissaoDt.getId_Apelado() == null)	) {
						
							guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_RECURSO, processoDt.getId_ProcessoTipo()));
							
							if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
								List listaAux = guiaRecursoNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
								if( listaAux != null ) {
									listaItensGuia.addAll(listaAux);
								}
							}
							
							
							
							Map valoresReferenciaCalculo = new HashMap();
							valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				processoDt.getValor());
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, 			guiaEmissaoDt.getNovoValorAcao());
							
							
							
							
							
							//Porte e Remessa
							if( guiaEmissaoDt.getPorteRemessaQuantidade() != null && guiaEmissaoDt.getPorteRemessaQuantidade().length() > 0 && !guiaEmissaoDt.getPorteRemessaQuantidade().equals("0")) {
								listaItensGuia.add( guiaRecursoNe.adicionarItem(CustaDt.TAXAS_DE_SERVICO_PORTE_E_REMESSA_DE_PROCESSOS_FISICOS) );
							}
							
							if( guiaEmissaoDt.getProtocoloIntegrado() != null && Funcoes.StringToInt(guiaEmissaoDt.getProtocoloIntegrado()) > 0 ) {
								listaItensGuia.add( guiaRecursoNe.adicionarItem(CustaDt.PROTOCOLO_INTEGRADO_CONTADOR_SEGUNDO_GRAU) );
							}
							
							if( guiaEmissaoDt.getPorteRemessaValorManual() != null && guiaEmissaoDt.getPorteRemessaValorManual().length() > 0 ) {
								String protocoloIntegradoValorManualCheck[] = request.getParameterValues("porteRemessaValorManualCheck");
								if( request.getParameterValues("porteRemessaValorManualCheck") != null ) {
									if( protocoloIntegradoValorManualCheck[0].equals(String.valueOf(GuiaEmissaoNe.VALOR_MANUAL_SIM)) ) {
										listaItensGuia.add( guiaRecursoNe.adicionarItem(CustaDt.TAXAS_DE_SERVICO_PORTE_E_REMESSA_DE_PROCESSOS_FISICOS) );
										valoresReferenciaCalculo.put(CustaDt.PORTE_REMESSA_MANUAL, guiaEmissaoDt.getPorteRemessaValorManual());
									}
								}
							}
							
							if( guiaEmissaoDt.getCustasValorManual() != null && guiaEmissaoDt.getCustasValorManual().length() > 0 ) {
								String custasValorManualCheck[] = request.getParameterValues("custasValorManualCheck");
								if( request.getParameterValues("custasValorManualCheck") != null ) {
									if( custasValorManualCheck[0].equals(String.valueOf(GuiaEmissaoNe.VALOR_MANUAL_SIM)) ) {
										listaItensGuia.add( guiaRecursoNe.adicionarItem(CustaDt.RECURSOS_CIVEIS_ORIUNDOS_DO_PRIMEIRO_GRAU) );
										//valoresReferenciaCalculo.put(CustaDt.RECURSOS_CIVEIS_ORIUNDOS_DO_PRIMEIRO_GRAU, guiaEmissaoDt.getCustasValorManual());
									}
								}
								else {
									guiaEmissaoDt.setCustasValorManual("0");
									listaItensGuia.add( guiaRecursoNe.adicionarItem(CustaDt.RECURSOS_CIVEIS_ORIUNDOS_DO_PRIMEIRO_GRAU) );
								}
							}
							
							String dobrarValorGuiaCheck[] = request.getParameterValues("dobrarValorGuiaCheck");
							if( request.getParameterValues("dobrarValorGuiaCheck") != null ) {
								if( dobrarValorGuiaCheck[0].equals(String.valueOf(GuiaEmissaoNe.VALOR_MANUAL_SIM)) ) {
									valoresReferenciaCalculo.put(CustaDt.DOBRAR_VALOR_GUIA, CustaDt.DOBRAR_VALOR_GUIA);
								}
							}
							
							
							guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
							List listaGuiaItemDt = guiaRecursoNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
							
							
							if( !guiaRecursoNe.isGuiaZeradaOuNegativa() ) {
								//Obtem o id_GuiaModelo
								if( listaItensGuia != null && listaItensGuia.size() > 0) {
									GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
									if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
										guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
								}
								
								//Deve haver no mï¿½nimo 1 item de guia
								if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
									
									request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
									request.setAttribute("TotalGuia", guiaRecursoNe.getGuiaCalculoNe().getTotalGuia() );
									
									request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
									request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaRecursoNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
									request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaRecursoNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
									request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
									request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
									request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
									
									ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
									processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
									processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
									processoCadastroDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
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
									stAcao = PAGINA_GUIA_RECURSO;
								}
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
								stAcao = PAGINA_GUIA_RECURSO;
							}
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_INFORME_RECORRIDO_RECORRENTE));
							stAcao = PAGINA_GUIA_RECURSO;
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
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaRecursoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaRecursoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				
				//Salvar GuiaEmissï¿½o
				guiaRecursoNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Geraï¿½ï¿½o da guia PDF
						byte[] byTemp = guiaRecursoNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_RECURSO, "RECURSO DE APELAï¿½ï¿½O");
						
						String nome="GuiaRecursoApelacao_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;
						
						enviarPDF(response, byTemp, nome);
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
					CustaDt custaDt = guiaRecursoNe.consultarCustaDtPorId(stId);
					
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