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
import br.gov.go.tj.projudi.dt.GuiaComplementarDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaComplementarNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaItemNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaComplementarCt extends Controle {

	private static final long serialVersionUID = -2446623657473259715L;
	
	private static final String PAGINA_GUIA_COMPLEMENTAR 		= "/WEB-INF/jsptjgo/GuiaComplementar.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_PADRAO_LOCALIZAR 		= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	private static final String NOME_CONTROLE_WEB_XML 			= "GuiaComplementar";
	
	private GuiaComplementarNe guiaComplementarNe = new GuiaComplementarNe();
	
	public int Permissao() {
		return GuiaComplementarDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;
		String stId 		= "";
		
		String nomeBusca1 = "";
		String nomeBusca2 = "";
		String PosicaoPaginaAtual = "0";
		boolean isProcessoSegundoGrau = false; //Default primeiro grau
		String tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU; //Default complementar de primeiro grau
		String GRAU_ESCOLHIDO = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU;
		
		stAcao = PAGINA_GUIA_COMPLEMENTAR;
		
		//********************************************
		// Variï¿½veis utilizadas pela pï¿½gina
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
		request.setAttribute("tempPrograma" 			, "Guia Complementar");
		request.setAttribute("tempRetorno" 				, NOME_CONTROLE_WEB_XML);
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		if (request.getParameter("nomeBusca1") != null) {
			nomeBusca1 = request.getParameter("nomeBusca1");
		}
		if (request.getParameter("nomeBusca2") != null) {
			nomeBusca2 = request.getParameter("nomeBusca2");
		}
		if (request.getParameter("PosicaoPaginaAtual") != null)	{
			PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
		}
		if (request.getParameter("posicaoLista") != null) {
			request.getParameter("posicaoLista");
		}
		if( request.getParameter("novoValorAcaoAtualizado") != null ) {
			guiaEmissaoDt.setNovoValorAcaoAtualizado( request.getParameter("novoValorAcaoAtualizado").toString() );
		}
		if( request.getParameter("novoValorCondenacao") != null ) {
			guiaEmissaoDt.setNovoValorCondenacao( request.getParameter("novoValorCondenacao").toString() );
		}
		if( request.getParameter("Id_ProcessoTipo") != null ) {
			guiaEmissaoDt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo").toString() );
		}
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaComplementarNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				comarcaDt = guiaComplementarNe.consultarComarca(serventiaDt.getId_Comarca());
				processoDt.setComarca(serventiaDt.getComarca());
				
				//Verifica se processo estï¿½ no segundo grau
				isProcessoSegundoGrau = guiaComplementarNe.isProcessoSegundoGrau(serventiaDt.getId());
				if( isProcessoSegundoGrau ) {
					tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU;
					GRAU_ESCOLHIDO = GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU;
				}
				else {
					tipoGuiaComplementar = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU;
					GRAU_ESCOLHIDO = GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU;
				}
			}
		}
		
		guiaComplementarNe.consultarNomeProcessoParte(guiaEmissaoDt);
		
		
		switch(paginaatual) {
			case Configuracao.Novo: {
				
				stAcao = PAGINA_GUIA_COMPLEMENTAR;
				
				if( processoDt == null ) {
					throw new MensagemException("Erro ao identificar os dados do processo. Por favor, acessar a guia novamente.");
				}
				
				//Ocorrência 2019/12924
				//Processo possui guia inicial paga?
				//Ocorrência 2020/1214
				//Foi alterado após conversa com o Marcelo Tiago da Financeira. Agora será permitido emitir guia complementar 
				//quando o processo tiver uma guia inicial ou complementar, independente de do status.
				if( processoDt.getId() != null && guiaComplementarNe.consultarGuiaEmissaoInicial_ComplementarQualquerStatus(processoDt.getId()) == null ) {
					throw new MensagemException("Não é permitido emitir Guia Complementar caso este processo não possua uma Guia Inicial vinculada.");
				}
				
				if( guiaComplementarNe.isConexaoSPG_OK() ) {
				
					guiaEmissaoDt = new GuiaEmissaoDt();
					request.getSession().removeAttribute("ListaGuiaItemDt");
					request.getSession().removeAttribute("ListaCustaDt");
					request.getSession().removeAttribute("TotalGuia");
					request.getSession().removeAttribute("GuiaEmissaoDt");
					request.getSession().removeAttribute("ListaGuiasIniciaisComplementaresPagas");
					
					guiaEmissaoDt.setNovoValorAcaoAtualizado(processoDt.getValor());
					guiaEmissaoDt.setNovoValorCondenacao(processoDt.getValorCondenacao());
					guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
					guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
					guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
					
					guiaEmissaoDt.setAtosEscrivaesCivel("1");
					
					request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				}
				else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
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
						
						guiaEmissaoDt.setListaGuiaItemDt(null);
						
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
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							return;
						}
						
						
						//Consulta modelo
						if( isProcessoSegundoGrau ) {
							guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, guiaEmissaoDt.getId_ProcessoTipo()));
						}
						else {
							guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo()));
						}
						if( guiaEmissaoDt.getGuiaModeloDt() == null ) {
							throw new MensagemException("Modelo/Template da guia não encontrado. Pode ser que esta classe não tenha cálculo homologado(Guia Inicial).");
						}
						
						guiaEmissaoDt.getGuiaModeloDt().setNomeModeloAlternativo("GUIA COMPLEMENTAR");
						
						//Consulta itens
						if( isProcessoSegundoGrau ) {
							listaItensGuia = guiaComplementarNe.consultarItensGuiaNovoRegimento(null, GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
						}
						else {
							listaItensGuia = guiaComplementarNe.consultarItensGuiaNovoRegimento(null, GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
						}
						
						if( listaItensGuia == null ) {
							listaItensGuia = new ArrayList();
						}
						
						
						
						Map valoresReferenciaCalculo = new HashMap();
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcaoAtualizado());
						valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcaoAtualizado());
						if( guiaEmissaoDt.getNovoValorAcaoAtualizado().toString().length() == 0 ) {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
						}
						else {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, guiaEmissaoDt.getNovoValorAcaoAtualizado());
						}
						if( guiaComplementarNe.isProcessoTipoMandado(Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
							valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
							if( guiaEmissaoDt.getNumeroImpetrantes() != null && guiaEmissaoDt.getNumeroImpetrantes().length() > 0 ) {
								valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
							}
							if( guiaEmissaoDt.getProcessoTipoCodigo() != null && Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO ) {
								valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO);
							}
						}
						
						
						if( guiaEmissaoDt.getPorteRemessaQuantidade() != null && Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade()) > 0 ) {
							String protocoloIntegrado[] = request.getParameterValues("protocoloIntegrado");
							if( request.getParameterValues("protocoloIntegrado") != null ) {
								if( protocoloIntegrado[0].equals(String.valueOf(GuiaEmissaoNe.PROTOCOLO_INTEGRADO_SIM)) ) {
									if( listaItensGuia == null ) {
										listaItensGuia = new ArrayList();
									}
									listaItensGuia.add( guiaComplementarNe.adicionarItem(CustaDt.PORTE_REMESSA) );
								}
							}
						}
						
						
						guiaComplementarNe.adicionarItemCautelarContenciosoParaGuiaComplementar(GRAU_ESCOLHIDO, listaItensGuia, guiaEmissaoDt);
						
						
						List<String> listaIdGuiaTipoConsultar = new ArrayList<String>();
						
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_COMPLEMENTAR_SEGUNDO_GRAU);
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
						listaIdGuiaTipoConsultar.add(GuiaTipoDt.ID_COMPLEMENTAR_PRIMEIRO_GRAU);
						
						
						//Tem guia inicial ou complementar inicial?
						List<GuiaEmissaoDt> listaGuiaEmissaoDtPagas = guiaComplementarNe.consultarGuiaEmissaoPagaParaGuiaComplementar(null, processoDt.getId(), listaIdGuiaTipoConsultar);
						
						if( listaGuiaEmissaoDtPagas != null ) {
							GuiaItemNe guiaItemNe = new GuiaItemNe();
							
							for(int i = 0; i < listaGuiaEmissaoDtPagas.size(); i++) {
								GuiaEmissaoDt guiaEmissaoDtAux = (GuiaEmissaoDt) listaGuiaEmissaoDtPagas.get(i);
								
								List listaAux = guiaItemNe.consultaItensGuiaGuiaComplementar(null, guiaEmissaoDtAux);
								if( guiaEmissaoDt.getListaGuiaItemDt() != null ) {
									if( listaAux != null ) {
										guiaEmissaoDt.getListaGuiaItemDt().addAll( listaAux );
									}
								}
								else {
									if( listaAux != null ) {
										guiaEmissaoDt.setListaGuiaItemDt( listaAux );
									}
								}
							}
						}
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = guiaComplementarNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null, null);
						
						
						List listaGuiaItemDtLocomocao = new ArrayList();
						
						//Calcular a guia Complementar
						guiaComplementarNe.recalcularGuiaComplementar(guiaEmissaoDt, listaGuiaItemDt, listaGuiaItemDtLocomocao);
						guiaComplementarNe.recalcularTotalGuia(listaGuiaItemDt);
						
						
						//Consulta guias iniciais e/ou complementares pagas
						if( guiaEmissaoDt.getId_Processo() != null ) {
							
//							List<GuiaEmissaoDt> listaGuiasIniciaisComplementaresPagas = guiaComplementarNe.consultarGuiaEmissaoInicial_Complementar(guiaEmissaoDt.getId_Processo());
//							List<GuiaEmissaoDt> listaGuiasIniciaisComplementaresPagas = listaGuiaEmissaoDtPagas;
							
							if( listaGuiaEmissaoDtPagas != null && !listaGuiaEmissaoDtPagas.isEmpty() ) {
								List<GuiaEmissaoDt> listaTemp = new ArrayList<GuiaEmissaoDt>();
								for(GuiaEmissaoDt guiaEmissaoTemp: listaGuiaEmissaoDtPagas) {
									
									guiaEmissaoTemp = guiaComplementarNe.consultarGuiaEmissao(guiaEmissaoTemp.getId());
									guiaEmissaoTemp.setListaGuiaItemDt(new GuiaItemNe().consultarItensGuia(guiaEmissaoTemp.getId()));
									
									listaTemp.add(guiaEmissaoTemp);
								}
								
								request.setAttribute("ListaGuiasIniciaisComplementaresPagas", listaTemp);
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
							
							request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
							request.setAttribute("TotalGuia", guiaComplementarNe.getGuiaCalculoNe().getTotalGuia() );
							
							request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
							request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaComplementarNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
							request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaComplementarNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
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
							stAcao = PAGINA_GUIA_COMPLEMENTAR;
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
//				//Obtem o prï¿½ximo nï¿½mero de Guia
//				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaComplementarNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
//				}
//				guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
//				guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
//				guiaEmissaoDt.setServentia(processoDt.getServentia());
//				guiaEmissaoDt.setComarca(processoDt.getComarca());
//				guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
//				guiaEmissaoDt.setValorAcao(processoDt.getValor());
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
				
				//Obtem o prï¿½ximo nï¿½mero de Guia
				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaComplementarNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
				}
				if( guiaEmissaoDt.getId_ProcessoTipo() != null ) {
					GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, tipoGuiaComplementar, guiaEmissaoDt.getId_ProcessoTipo());
					guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
				}
				
				guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
				guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
				guiaEmissaoDt.setServentia(processoDt.getServentia());
				guiaEmissaoDt.setComarca(processoDt.getComarca());
				guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
				guiaEmissaoDt.setValorAcao(processoDt.getValor());
				guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
				
				guiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
				guiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
				guiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
				guiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
				
				guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
				guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				
				if( guiaEmissaoDt.getListaGuiaItemDt() != null && guiaEmissaoDt.getListaGuiaItemDt().size() > 0 ) {
					//Salvar GuiaEmissï¿½o
					
					
					guiaComplementarNe.salvarGuiaComplementar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt());
				}
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Geraï¿½ï¿½o da guia PDF
						byte[] byTemp = guiaComplementarNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, tipoGuiaComplementar, "COMPLEMENTAR");
						
						String nome ="GuiaComplementar_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;
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
			
			//Busca Processo Tipo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				
				if (request.getParameter("Passo")==null) {
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					String[] lisNomeBusca = {"ProcessoTipo"};
					String[] lisDescricao = {"ProcessoTipo"};
					request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoTipo");
					request.setAttribute("tempBuscaPrograma", "ProcessoTipo");
					request.setAttribute("tempRetorno", NOME_CONTROLE_WEB_XML);
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("tempFluxo1", "LAS");
					request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}
				else {
					String stTemp = "";
					
					if( guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
						ProcessoDt processoViculadoDt = guiaComplementarNe.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente());
						if (processoViculadoDt != null && processoViculadoDt.getId_Serventia() != null) {
							//1 grau
							if( guiaEmissaoDt.getCodigoGrau() != null && guiaEmissaoDt.getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {									
									stTemp = new ProcessoNe().consultarProcessoTipoServentiaJSON(nomeBusca1, processoViculadoDt.getId_Serventia() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
							}
							else {
								//2 grau									
								stTemp = new ProcessoNe().consultarDescricaoProcessoTipoJSON(nomeBusca1, processoViculadoDt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
							}
						}
						else {
							request.setAttribute("MensagemErro","Processo nï¿½o encontrado. Por favor, verifique o nï¿½mero do processo informado.");
						}
					}
					else {
						
						stTemp = new ProcessoNe().consultarProcessoTipoServentiaJSON(nomeBusca1, processoDt.getId_Serventia() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
					}
				
					
					enviarJSON(response, stTemp);
						
					
					return;
					
				}
				break;
				
			}
			
			default : {
				//Busca Processo Tipo
				stId = request.getParameter("Id_ProcessoTipo");
				if( stId != null ) {
					ProcessoTipoDt processoTipoDt = guiaComplementarNe.consultarProcessoTipo(stId);
					
					if( guiaEmissaoDt != null && processoTipoDt != null ) {
						
						guiaEmissaoDt.setId_ProcessoTipo(processoTipoDt.getId());
						guiaEmissaoDt.setProcessoTipo(processoTipoDt.getProcessoTipo());
						guiaEmissaoDt.setProcessoTipoCodigo(processoTipoDt.getProcessoTipoCodigo());
						
						request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					}
					
					stId = null;
				}
				
				break;
			}
			
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}
