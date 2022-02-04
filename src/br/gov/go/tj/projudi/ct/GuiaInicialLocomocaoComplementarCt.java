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
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaLocomocaoDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaLocomocaoComplementarNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaInicialLocomocaoComplementarCt extends Controle {

	private static final long serialVersionUID = -6196706590243912965L;
	
	private static final String PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR 	= "/WEB-INF/jsptjgo/GuiaIntermediariaComplementar.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 				= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	private GuiaLocomocaoComplementarNe guiaLocomocaoComplementarNe = new GuiaLocomocaoComplementarNe();
	
	@Override
	public int Permissao() {
		return GuiaLocomocaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 			= null;
		int passoEditar 		= -1;
		List tempList 			= null;
		String stId 			= "";
		
		stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
		
		List listaItensGuia 	= null;
		List listaBairroIntimacaoDt 		= null;
		List listaBairroDt 		= null;
		List listaBairroLocomocaoContaVinculada = null;
		List listaBairroLocomocaoPenhora = null;
		List listaBairroLocomocaoAvaliacao = null;
		
		//********************************************
		//Variï¿½veis objetos
		GuiaEmissaoDt guiaEmissaoDt = null;
		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
		if( guiaEmissaoDt == null ) {
			guiaEmissaoDt = new GuiaEmissaoDt();
		}
		guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		ProcessoDt processoDt = null;
		
		//********************************************
		//Requests 
		if( request.getParameter("idGuiaTipo") != null && !request.getParameter("idGuiaTipo").toString().equals("null") )
			request.getParameter("idGuiaTipo");
		request.setAttribute("tempPrograma" 			, "Guia Inicial Locomoção Complementar");
		request.setAttribute("tempRetorno" 				, "GuiaInicialLocomocaoComplementar");
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		if ((request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null"))) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		if( request.getParameter("finalidade") != null && request.getParameter("finalidade").toString().length() > 0 ) {
			guiaEmissaoDt.setFinalidade(request.getParameter("finalidade").toString());
		}
		String citacaoHoraCerta = null;
		if( request.getParameter("citacaoHoraCerta") != null ) {
			citacaoHoraCerta = request.getParameter("citacaoHoraCerta").toString();
		}
		if( request.getParameter("oficialCompanheiro") != null ) {
			request.getParameter("oficialCompanheiro").toString();
		}
		String foraHorarioNormal = null;
		if( request.getParameter("foraHorarioNormal") != null && request.getParameter("foraHorarioNormal").toString().length() > 0 ) {
			foraHorarioNormal = request.getParameter("foraHorarioNormal").toString();
		}
		
		if( request.getParameter("quantidadeAcrescimoPessoa") != null && request.getParameter("quantidadeAcrescimoPessoa").toString().length() > 0 ) {
			guiaEmissaoDt.setQuantidadeAcrescimo(request.getParameter("quantidadeAcrescimoPessoa").toString());
		}
		
		
		//********************************************
		//Variï¿½veis de sessï¿½o
		listaItensGuia = (List) request.getSession().getAttribute("ListaItensGuia");
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if ( processoDt != null ) {
			request.setAttribute("guiaIdProcesso", processoDt.getId());
			guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
		}
		
		listaBairroIntimacaoDt = (List) request.getSession().getAttribute("ListaBairroLocomocaoIntimacao");
		if( listaBairroIntimacaoDt == null ) {
			listaBairroIntimacaoDt = new ArrayList();
		}
		
		listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
		if( listaBairroDt == null ) {
			listaBairroDt = new ArrayList();
		}
		
		listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
		if( listaBairroLocomocaoContaVinculada == null ) {
			listaBairroLocomocaoContaVinculada = new ArrayList();
		}
		
		listaBairroLocomocaoPenhora = (List) request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
		if( listaBairroLocomocaoPenhora == null ) {
			listaBairroLocomocaoPenhora = new ArrayList();
		}
		
		listaBairroLocomocaoAvaliacao = (List) request.getSession().getAttribute("ListaBairroLocomocaoAvaliacao");
		if( listaBairroLocomocaoAvaliacao == null ) {
			listaBairroLocomocaoAvaliacao = new ArrayList();
		}
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaLocomocaoComplementarNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				comarcaDt = guiaLocomocaoComplementarNe.consultarComarca(serventiaDt.getId_Comarca());
				processoDt.setComarca(serventiaDt.getComarca());
			}
		}
		
		
		switch(paginaatual) {
			
			case Configuracao.Novo: {
				
				//Ocorrencia 2020/10357 - Retirado restrição de acesso para outras comarcas para a nova central única dos contadores.
					
				stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
					
				guiaEmissaoDt = new GuiaEmissaoDt();
				request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
				request.getSession().removeAttribute("ListaBairroDt");
				request.getSession().removeAttribute("ListaBairroLocomocaoIntimacao");
				request.getSession().removeAttribute("ListaBairroLocomocaoPenhora");
				request.getSession().removeAttribute("ListaBairroLocomocaoAvaliacao");
				request.getSession().removeAttribute("ListaRecalculoBairroDt");
				request.getSession().removeAttribute("ListaRecalculoBairroLocomocaoContaVinculada");
				request.getSession().removeAttribute("ListaRecalculoBairroLocomocaoAvaliacao");
				request.getSession().removeAttribute("ListaRecalculoBairroLocomocaoPenhora");
				request.getSession().removeAttribute("ListaItensGuia");
				request.getSession().removeAttribute("ListaGuiaItemDt");
				request.getSession().removeAttribute("GuiaEmissaoDt");
				request.getSession().removeAttribute("TotalGuia");
				request.getSession().removeAttribute("serventiaDt");
				
				if( request.getParameter("Id_GuiaEmissaoPaga") != null && request.getParameter("Id_GuiaEmissaoPaga").toString().length() > 0 ) {
					request.getSession().setAttribute("Id_GuiaEmissaoPaga", request.getParameter("Id_GuiaEmissaoPaga").toString());
					guiaEmissaoDt.setId_GuiaEmissaoPrincipal(request.getParameter("Id_GuiaEmissaoPaga").toString());
				}
				
				listaBairroIntimacaoDt = null;
				listaBairroDt = null;
				listaBairroLocomocaoAvaliacao = null;
				listaBairroLocomocaoPenhora = null;
				listaBairroLocomocaoContaVinculada = null;
				
				//Consulta itens de guia da guia paga
				List listaGuiaItemDt_GuiaLocomocaoPaga = guiaLocomocaoComplementarNe.consultarGuiaItens(request.getSession().getAttribute("Id_GuiaEmissaoPaga").toString(), null);
				
				if( listaGuiaItemDt_GuiaLocomocaoPaga != null && listaGuiaItemDt_GuiaLocomocaoPaga.size() > 0 ) {
					
					//Adicionar lista de GuiaItemDt da guia paga na sessï¿½o
					request.getSession().setAttribute("ListaGuiaItemDt_GuiaLocomocaoPaga", listaGuiaItemDt_GuiaLocomocaoPaga);
					
					//Extrai os bairros das locomoï¿½ï¿½es caso tenha algum item de locomoï¿½ï¿½o
//						listaBairroDt 						= guiaIntermediariaComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_OFICIAL);
//						listaBairroLocomocaoContaVinculada 	= guiaIntermediariaComplementarNe.extrairListaBairroDtContaVinculada(listaGuiaItemDt_GuiaLocomocaoPaga);
//						listaBairroLocomocaoAvaliacao 		= guiaIntermediariaComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_AVALIACAO);
//						listaBairroLocomocaoPenhora 		= guiaIntermediariaComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_PENHORA);
					
					//Listas com os itens atuais e receberï¿½ os novos
					request.getSession().setAttribute("ListaBairroDt"						, guiaLocomocaoComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_OFICIAL));
					request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada"	, guiaLocomocaoComplementarNe.extrairListaBairroDtContaVinculada(listaGuiaItemDt_GuiaLocomocaoPaga));
					request.getSession().setAttribute("ListaBairroLocomocaoAvaliacao"		, guiaLocomocaoComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_AVALIACAO));
					request.getSession().setAttribute("ListaBairroLocomocaoPenhora"			, guiaLocomocaoComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_PENHORA));
					
					//Listas com os itens Pagos
					request.getSession().setAttribute("ListaRecalculoBairroDt"							, guiaLocomocaoComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_OFICIAL));
					request.getSession().setAttribute("ListaRecalculoBairroLocomocaoContaVinculada"		, guiaLocomocaoComplementarNe.extrairListaBairroDtContaVinculada(listaGuiaItemDt_GuiaLocomocaoPaga));
					request.getSession().setAttribute("ListaRecalculoBairroLocomocaoAvaliacao"			, guiaLocomocaoComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_AVALIACAO));
					request.getSession().setAttribute("ListaRecalculoBairroLocomocaoPenhora"			, guiaLocomocaoComplementarNe.extrairListaBairroDt(listaGuiaItemDt_GuiaLocomocaoPaga, CustaDt.LOCOMOCAO_PARA_PENHORA));
				}
				else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Atenção! Esta guia não possui locomoção.");
					return;
				}
				
				break;
			}
			
			//Excluir item de bairro da locomoï¿½ï¿½o
			case Configuracao.Excluir: {
				
				switch(passoEditar) {
					
					//Remover Locomoï¿½ï¿½o
					case Configuracao.Curinga6 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							
							listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
							listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							
							if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
//								listaBairroDt.remove(posicaoListaBairroExcluir);
								listaBairroDt.set(posicaoListaBairroExcluir, null);
								request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
								
//								List listaRecalculoBairroDt = (List) request.getSession().getAttribute("ListaRecalculoBairroDt");
//								if( listaRecalculoBairroDt != null && listaRecalculoBairroDt.size() > 0 ) {
//									listaRecalculoBairroDt.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroDt", listaRecalculoBairroDt);
//								}
							}
							if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
								listaBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
								
//								List listaRecalculoBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoContaVinculada");
//								if( listaRecalculoBairroLocomocaoContaVinculada != null && listaRecalculoBairroLocomocaoContaVinculada.size() > 0 ) {
//									listaRecalculoBairroLocomocaoContaVinculada.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroLocomocaoContaVinculada", listaRecalculoBairroLocomocaoContaVinculada);
//								}
							}
						}
						break;
					}
					
					//Remover Locomoï¿½ï¿½o Penhora
					case Configuracao.Curinga7 : {
						
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							
							listaBairroLocomocaoPenhora = (List) request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
							listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							
							if( listaBairroLocomocaoPenhora != null && listaBairroLocomocaoPenhora.size() > 0 ) {
								listaBairroLocomocaoPenhora.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroLocomocaoPenhora", listaBairroLocomocaoPenhora);
								
//								List listaRecalculoBairroLocomocaoPenhora = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoPenhora");
//								if( listaRecalculoBairroLocomocaoPenhora != null && listaRecalculoBairroLocomocaoPenhora.size() > 0 ) {
//									listaRecalculoBairroLocomocaoPenhora.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroLocomocaoPenhora", listaRecalculoBairroLocomocaoPenhora);
//								}
							}
							if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
								listaBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
								
//								List listaRecalculoBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoContaVinculada");
//								if( listaRecalculoBairroLocomocaoContaVinculada != null && listaRecalculoBairroLocomocaoContaVinculada.size() > 0 ) {
//									listaRecalculoBairroLocomocaoContaVinculada.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroLocomocaoContaVinculada", listaRecalculoBairroLocomocaoContaVinculada);
//								}
							}
						}
						break;
					}
					
					//Remover Locomoï¿½ï¿½o Avaliaï¿½ï¿½o
					case Configuracao.Curinga8 : {
						
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							
							listaBairroLocomocaoAvaliacao = (List) request.getSession().getAttribute("ListaBairroLocomocaoAvaliacao");
							listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							
							if( listaBairroLocomocaoAvaliacao != null &&  listaBairroLocomocaoAvaliacao.size() > 0 ) {
								listaBairroLocomocaoAvaliacao.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroLocomocaoAvaliacao", listaBairroLocomocaoAvaliacao);
								
//								List listaRecalculoBairroLocomocaoAvaliacao = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoAvaliacao");
//								if( listaRecalculoBairroLocomocaoAvaliacao != null && listaRecalculoBairroLocomocaoAvaliacao.size() > 0 ) {
//									listaRecalculoBairroLocomocaoAvaliacao.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroLocomocaoAvaliacao", listaRecalculoBairroLocomocaoAvaliacao);
//								}
							}
							if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
								listaBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
								
//								List listaRecalculoBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoContaVinculada");
//								if( listaRecalculoBairroLocomocaoContaVinculada != null && listaRecalculoBairroLocomocaoContaVinculada.size() > 0 ) {
//									listaRecalculoBairroLocomocaoContaVinculada.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroLocomocaoContaVinculada", listaRecalculoBairroLocomocaoContaVinculada);
//								}
							}
						}
						break;
					}
					
					//Remover Locomoï¿½ï¿½o
					case Configuracao.Curinga9 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							
							listaBairroIntimacaoDt = (List) request.getSession().getAttribute("ListaBairroLocomocaoIntimacao");
							listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							
							if( listaBairroIntimacaoDt != null && listaBairroIntimacaoDt.size() > 0 ) {
								listaBairroIntimacaoDt.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroIntimacaoDt", listaBairroIntimacaoDt);
							}
							if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
								listaBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
								
//								List listaRecalculoBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoContaVinculada");
//								if( listaRecalculoBairroLocomocaoContaVinculada != null && listaRecalculoBairroLocomocaoContaVinculada.size() > 0 ) {
//									listaRecalculoBairroLocomocaoContaVinculada.set(posicaoListaBairroExcluir, null);
//									request.getSession().setAttribute("ListaRecalculoBairroLocomocaoContaVinculada", listaRecalculoBairroLocomocaoContaVinculada);
//								}
							}
						}
						break;
					}
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
						//Nï¿½o tem BREAK pq precisa mostrar a tela novamente de prï¿½via
					}
					
					case Configuracao.Curinga8: {
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							break;
						}
						
						//if(  ) {
						
						
							//Consulta lista de itens da guia
							listaItensGuia = guiaLocomocaoComplementarNe.consultarItensGuia(null, guiaEmissaoDt, GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR, processoDt.getId_ProcessoTipo());
							if( listaItensGuia == null ) {
								GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR, processoDt.getId_ProcessoTipo());
								guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							
							
							
							//Locomoï¿½ï¿½o
							List valoresIdBairro = null;
							listaBairroDt = (List)request.getSession().getAttribute("ListaBairroDt");
							if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
								if( valoresIdBairro == null )
									valoresIdBairro = new ArrayList();
								
								for(int i = 0; i < listaBairroDt.size(); i++) {
									BairroDt bairroDt = (BairroDt)listaBairroDt.get(i);
									valoresIdBairro.add(bairroDt.getId());
									
									//Adicionar item de Locomoï¿½ï¿½o
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList();
									listaItensGuia.add( guiaLocomocaoComplementarNe.adicionarItemLocomocao(bairroDt) );
									
									/**
									 * Retirado este cï¿½digo, pois ele estï¿½ duplicando,novamente, sem a necessidade.
									 * 
									 * 
									if( guiaIntermediariaComplementarNe.existeLocomocaoDobro(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
										valoresIdBairro.add(bairroDt.getId());
										listaItensGuia.add( guiaIntermediariaComplementarNe.adicionarItemLocomocaoOficial() );
									}
									*/
								}
							}
							
							
							//Locomoï¿½ï¿½o com Conta Vinculada
							List valoresIdBairroContaVinculada = null;
							listaBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
								if( valoresIdBairroContaVinculada == null )
									valoresIdBairroContaVinculada = new ArrayList();
								
								for(int i = 0; i < listaBairroLocomocaoContaVinculada.size(); i++) {
									BairroDt bairroDt = (BairroDt)listaBairroLocomocaoContaVinculada.get(i);
									valoresIdBairroContaVinculada.add(bairroDt.getId());
									
									//Adicionar item de Locomoï¿½ï¿½o
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList();
									listaItensGuia.add( guiaLocomocaoComplementarNe.adicionarItemLocomocaoOficialContaVinculada(bairroDt) );
									
									/**
									 * Retirado este cï¿½digo, pois ele estï¿½ duplicando,novamente, sem a necessidade.
									 * 
									 * 
									if( guiaIntermediariaComplementarNe.existeLocomocaoDobro(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
										valoresIdBairroContaVinculada.add(bairroDt.getId());
										listaItensGuia.add( guiaIntermediariaComplementarNe.adicionarItemLocomocaoOficialContaVinculada() );
									}
									*/
								}
							}
							
							
							//Locomoï¿½ï¿½o Penhora
							List valoresIdBairroPenhora = null;
							listaBairroLocomocaoPenhora = (List)request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
							if( listaBairroLocomocaoPenhora != null && listaBairroLocomocaoPenhora.size() > 0 ) {
								if( valoresIdBairroPenhora == null )
									valoresIdBairroPenhora = new ArrayList();
								
								for(int i = 0; i < listaBairroLocomocaoPenhora.size(); i++) {
									BairroDt bairroDt = (BairroDt)listaBairroLocomocaoPenhora.get(i);
									valoresIdBairroPenhora.add(bairroDt.getId());
									
									//Adicionar item de Locomoï¿½ï¿½o
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList();
									listaItensGuia.add( guiaLocomocaoComplementarNe.adicionarItemLocomocaoPenhora(bairroDt) );
									
									/**
									 * Retirado este cï¿½digo, pois ele estï¿½ duplicando,novamente, sem a necessidade.
									 * 
									 * 
									if( guiaIntermediariaComplementarNe.existeLocomocaoDobro(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
										valoresIdBairroPenhora.add(bairroDt.getId());
										listaItensGuia.add( guiaIntermediariaComplementarNe.adicionarItemLocomocaoPenhora() );
									}
									*/
								}
							}
							
							
							//Locomoï¿½ï¿½o Avaliaï¿½ï¿½o
							List valoresIdBairroAvaliacao = null;
							listaBairroLocomocaoAvaliacao = (List)request.getSession().getAttribute("ListaBairroLocomocaoAvaliacao");
							if( listaBairroLocomocaoAvaliacao != null && listaBairroLocomocaoAvaliacao.size() > 0 ) {
								if( valoresIdBairroAvaliacao == null )
									valoresIdBairroAvaliacao = new ArrayList();
								
								for(int i = 0; i < listaBairroLocomocaoAvaliacao.size(); i++) {
									BairroDt bairroDt = (BairroDt)listaBairroLocomocaoAvaliacao.get(i);
									valoresIdBairroAvaliacao.add(bairroDt.getId());
									
									//Adicionar item de Locomoï¿½ï¿½o
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList();
									listaItensGuia.add( guiaLocomocaoComplementarNe.adicionarItemLocomocaoAvaliacao(bairroDt) );
									/**
									 * Retirado este cï¿½digo, pois ele estï¿½ duplicando,novamente, sem a necessidade.
									 * 
									 *
									if( guiaIntermediariaComplementarNe.existeLocomocaoDobro(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
										valoresIdBairroAvaliacao.add(bairroDt.getId());
										listaItensGuia.add( guiaIntermediariaComplementarNe.adicionarItemLocomocaoAvaliacao() );
									}
									*/
								}
							}
							
							
							
							
							//Locomoï¿½ï¿½o Intimaï¿½ï¿½o
							List valoresIdBairroIntimacao = null;
							listaBairroIntimacaoDt = (List)request.getSession().getAttribute("ListaBairroLocomocaoIntimacao");
							if( listaBairroIntimacaoDt != null && listaBairroIntimacaoDt.size() > 0 ) {
								if( valoresIdBairroIntimacao == null )
									valoresIdBairroIntimacao = new ArrayList();
								
								for(int i = 0; i < listaBairroIntimacaoDt.size(); i++) {
									BairroDt bairroDt = (BairroDt)listaBairroIntimacaoDt.get(i);
									valoresIdBairroIntimacao.add(bairroDt.getId());
									
									//Adicionar item de Locomoï¿½ï¿½o
									if( listaItensGuia == null )
										listaItensGuia = new ArrayList();
									listaItensGuia.add( guiaLocomocaoComplementarNe.adicionarItemLocomocao(bairroDt) );
									
									/**
									 * Retirado este cï¿½digo, pois ele estï¿½ duplicando,novamente, sem a necessidade.
									 * 
									 * 
									if( guiaIntermediariaComplementarNe.existeLocomocaoDobro(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
										valoresIdBairroIntimacao.add(bairroDt.getId());
										listaItensGuia.add( guiaIntermediariaComplementarNe.adicionarItemLocomocaoOficial() );
									}
									else {
										guiaIntermediariaComplementarNe.dobrarIntimacoes(valoresIdBairroIntimacao, valoresIdBairroContaVinculada, listaItensGuia, temOficialCompanheiro);
										guiaIntermediariaComplementarNe.dobrarBairrosIntimacoes(valoresIdBairroIntimacao, valoresIdBairroContaVinculada, temOficialCompanheiro);
									}
									*/
								}
								
								if( valoresIdBairro == null ) {
									valoresIdBairro = new ArrayList();
								}
								if( valoresIdBairroIntimacao.size() > 0 ) {
									valoresIdBairro.addAll(valoresIdBairroIntimacao);
								}
							}
							
							
							
							//Fora horï¿½rio normal
							guiaLocomocaoComplementarNe.dobrarForaHorarioNormal(listaItensGuia, foraHorarioNormal);
							if( GuiaEmissaoDt.VALOR_SIM.equals(foraHorarioNormal) ) {
								valoresIdBairro = valoresIdBairroIntimacao;
							}
							
							
							Map valoresReferenciaCalculo = new HashMap();
							valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, processoDt.getValor());
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, processoDt.getValor());
							if( valoresIdBairro != null )
								valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
							if( valoresIdBairroContaVinculada != null )
								valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_CONTA_VINCULADA, valoresIdBairroContaVinculada);
							if( valoresIdBairroPenhora != null )
								valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_PENHORA, valoresIdBairroPenhora);
							if( valoresIdBairroAvaliacao != null )
								valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_AVALIACAO, valoresIdBairroAvaliacao);
							
							
							//Quantidade de Acrescimo por Pessoa
							if( guiaEmissaoDt.getQuantidadeAcrescimo() != null && guiaEmissaoDt.getQuantidadeAcrescimo().length() > 0 && !guiaEmissaoDt.getQuantidadeAcrescimo().equals("0")) {
								valoresReferenciaCalculo.put(CustaDt.QUANTIDADE_ACRESCIMO_PESSOA, guiaEmissaoDt.getQuantidadeAcrescimo());
							}
							
							
							//Obtem o id_GuiaModelo
							if( listaItensGuia != null && listaItensGuia.size() > 0) {
								GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
								if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							
							//Validaï¿½ï¿½o das locomoï¿½ï¿½es
							boolean locomocoesPreenchidas = false;
							String mensagem = null;
							if( guiaEmissaoDt.getFinalidade() != null && guiaEmissaoDt.getFinalidade().length() > 0 ) {
								
								switch( Funcoes.StringToInt(guiaEmissaoDt.getFinalidade()) ) {
								
									case GuiaEmissaoNe.LOCOMOCAO : {
										locomocoesPreenchidas = true;
										
										break;
									}
								
									case GuiaEmissaoNe.PENHORA_AVALIACAO_ALIENACAO : {
										
										if( listaBairroLocomocaoPenhora == null || listaBairroLocomocaoPenhora.size() == 0 ) {
											mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Penhora";
										}
										else {
											if( listaBairroLocomocaoAvaliacao == null || listaBairroLocomocaoAvaliacao.size() == 0 ) {
												mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Avaliaï¿½ï¿½o";
											}
											else {
												locomocoesPreenchidas = true;
											}
										}
										
										break;
									}
									
									case GuiaEmissaoNe.CITACAO_PENHORA_AVALIACAO_E_ALIENACAO : {
										
										if( listaBairroDt == null || listaBairroDt.size() == 0 ) {
											mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Citaï¿½ï¿½o";
										}
										else {
											if( listaBairroLocomocaoAvaliacao == null || listaBairroLocomocaoAvaliacao.size() == 0 ) {
												mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Avaliaï¿½ï¿½o";
											}
											else {
												if( listaBairroLocomocaoPenhora == null || listaBairroLocomocaoPenhora.size() == 0 ) {
													mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Penhora";
												}
												else {
													locomocoesPreenchidas = true;
												}
											}
										}
										
										break;
									}
									
									case GuiaEmissaoNe.CITACAO_PENHORA_E_PRACA_LEILAO : {
										
										if( listaBairroDt == null || listaBairroDt.size() == 0 ) {
											mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Citaï¿½ï¿½o";
										}
										else {
											if( listaBairroLocomocaoPenhora == null || listaBairroLocomocaoPenhora.size() == 0 ) {
												mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Penhora";
											}
											else {
												locomocoesPreenchidas = true;
											}
										}
										
										break;
									}
									
									case GuiaEmissaoNe.CITACAO_PENHORA_AVALIACAO_PRACA_LEILAO : {
										
										if( listaBairroDt == null || listaBairroDt.size() == 0 ) {
											mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Citaï¿½ï¿½o";
										}
										else {
											if( listaBairroLocomocaoPenhora == null || listaBairroLocomocaoPenhora.size() == 0 ) {
												mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Penhora";
											}
											else {
												if( listaBairroLocomocaoAvaliacao == null || listaBairroLocomocaoAvaliacao.size() == 0 ) {
													mensagem = "Informe o(s) bairro(s) para a locomoï¿½ï¿½o de Avaliaï¿½ï¿½o";
												}
												else {
													locomocoesPreenchidas = true;
												}
											}
										}
										
										break;
									}
								}
							}
							else {
								mensagem = "Informe a finalidade.";
							}
							
							
							if( guiaEmissaoDt.getFinalidade() != null && guiaEmissaoDt.getFinalidade().length() > 0 ) {
								if( locomocoesPreenchidas ) {
									
									List listaIdBairroAux = new ArrayList();
									if( valoresIdBairro != null && valoresIdBairro.size() > 0 ) {
										listaIdBairroAux.addAll(valoresIdBairro);
									}
									if( valoresIdBairroContaVinculada != null && valoresIdBairroContaVinculada.size() > 0  ) {
										listaIdBairroAux.addAll(valoresIdBairroContaVinculada);
									}
									if( valoresIdBairroAvaliacao != null && valoresIdBairroAvaliacao.size() > 0  ) {
										listaIdBairroAux.addAll(valoresIdBairroAvaliacao);
									}
									if( valoresIdBairroIntimacao != null && valoresIdBairroIntimacao.size() > 0  ) {
										listaIdBairroAux.addAll(valoresIdBairroIntimacao);
									}
									if( valoresIdBairroPenhora != null && valoresIdBairroPenhora.size() > 0  ) {
										listaIdBairroAux.addAll(valoresIdBairroPenhora);
									}
									boolean bairrosZoneados = guiaLocomocaoComplementarNe.isBairroZoneado(listaIdBairroAux);
									
									
									guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
									List listaGuiaItemDt = new ArrayList();
									if( bairrosZoneados ) {
										listaGuiaItemDt = guiaLocomocaoComplementarNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, citacaoHoraCerta);
										
										
										List listaGuiaItemDt_GuiaLocomocaoPaga = (List) request.getSession().getAttribute("ListaGuiaItemDt_GuiaLocomocaoPaga");
										
										if( listaGuiaItemDt != null && listaGuiaItemDt_GuiaLocomocaoPaga != null ) {
											
											listaGuiaItemDt_GuiaLocomocaoPaga = guiaLocomocaoComplementarNe.retirarItensNaoLocomocao(listaGuiaItemDt_GuiaLocomocaoPaga);
											
											guiaLocomocaoComplementarNe.calcularItensGuiaInicialLocomocaoComplementar(listaGuiaItemDt, listaGuiaItemDt_GuiaLocomocaoPaga);
											guiaLocomocaoComplementarNe.retirarItensValorCalculadoZerado(listaGuiaItemDt);
										}
										
//										//Consulta itens de guia da guia paga
//										List listaGuiaItemDt_GuiaLocomocaoPaga = guiaIntermediariaComplementarNe.consultarGuiaItens(request.getSession().getAttribute("Id_GuiaEmissaoPaga").toString(), null);
//										
//										if( listaGuiaItemDt_GuiaLocomocaoPaga != null && listaGuiaItemDt_GuiaLocomocaoPaga.size() > 0 ) {
//											guiaIntermediariaComplementarNe.calcularItensGuiaLocomocaoComplementar(listaGuiaItemDt, listaGuiaItemDt_GuiaLocomocaoPaga);
//											
//											//guiaIntermediariaComplementarNe.recalcularTotalGuia(listaGuiaItemDt);
//										}
									}
									
									
									if( !bairrosZoneados ) {
										request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
										stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
									}
									else {
										//Deve haver no mï¿½nimo 1 item de guia
										if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
											
											request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
											request.setAttribute("TotalGuia", guiaLocomocaoComplementarNe.getGuiaCalculoNe().getTotalGuia() );
											
											request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
											request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaLocomocaoComplementarNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
											request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaLocomocaoComplementarNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
											request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
											request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
											request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
											
											ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
											processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
											processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
											processoCadastroDt.setValor(processoDt.getValor());
											processoCadastroDt.setProcessoTipo(processoDt.getProcessoTipo());
											
											guiaEmissaoDt.setValorAcao(processoCadastroDt.getValor());
											if( guiaEmissaoDt.getDataVencimento() == null || (guiaEmissaoDt.getDataVencimento() != null && guiaEmissaoDt.getDataVencimento().length() == 0) )
												guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
											
											request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
											request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
											
											stAcao = PAGINA_GUIA_PREVIA_CALCULO;
											request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA));
											
										}
										else {
											//request.setAttribute("Mensagem", "Atenï¿½ï¿½o! Nenhum Item de Guia Localizado. A Guia deve Conter 1 ou mais Itens de Custa.");
											//stAcao = PAGINA_ERRO;
											
											request.setAttribute("MensagemErro", "Nenhum Item de Guia Localizado.<br/> Informe o oficial de justiï¿½a.<br/> A Guia deve Conter 1 ou mais Itens de Custa.");
											stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
										}
									}
								}
								else {
									request.setAttribute("MensagemErro", mensagem);
									stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
								}
							}
							else {
								request.setAttribute("MensagemErro", mensagem);
								stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
							}
						//}
						
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
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaLocomocaoComplementarNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
//					//Banco Itaï¿½
//					case Configuracao.Curinga7 : {
//						return;
//					}
//					
//					//Banco Caixa Econï¿½mica
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
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaLocomocaoComplementarNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				guiaLocomocaoComplementarNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Geraï¿½ï¿½o da guia PDF
						byte[] byTemp = guiaLocomocaoComplementarNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR, "INTERMEDIï¿½RIA/LOCOMOï¿½ï¿½O COMPLEMENTAR");						
						String nome="GuiaIntermediariaLocomocaoComplementar_Processo_"+ guiaEmissaoDt.getNumeroProcesso();						
						enviarPDF(response, byTemp, nome);
						return;
					}
					
					case Configuracao.Salvar : {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=Guia Emitida com Sucesso!");
						return;
					}
				}
				
				//Retira as listas da sessï¿½o
				request.getSession().removeAttribute("ListaGuiaItemDt_GuiaLocomocaoPaga");
				request.getSession().removeAttribute("ListaBairroDt");
				request.getSession().removeAttribute("ListaBairroLocomocaoIntimacao");
				request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
				request.getSession().removeAttribute("ListaBairroLocomocaoAvaliacao");
				request.getSession().removeAttribute("ListaBairroLocomocaoPenhora");
				request.getSession().removeAttribute("ListaRecalculoBairroDt");
				request.getSession().removeAttribute("ListaRecalculoBairroLocomocaoContaVinculada");
				request.getSession().removeAttribute("ListaRecalculoBairroLocomocaoAvaliacao");
				request.getSession().removeAttribute("ListaRecalculoBairroLocomocaoPenhora");
				
				break;
			}
			
			//Utilizado para localizar os bairros
			case Configuracao.Localizar : {
				
				switch(passoEditar) {
					//Busca de Bairro
					case ( Configuracao.Curinga6 ) : {
						
						List listaRecalculoBairroDt = (List) request.getSession().getAttribute("ListaRecalculoBairroDt");
						if( listaRecalculoBairroDt == null ) {
							stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_SEM_PERMISSAO_ADD_LOCOMOCAO_COMPLEMENTAR));
						}
						else {
//							stAcao = PAGINA_BAIRRO_LOCALIZAR;
							
							tempList = guiaLocomocaoComplementarNe.consultarDescricaoBairro(nomebusca, posicaopaginaatual);
							
							request.setAttribute("ListaBairro"			, tempList);
							request.setAttribute("tempBuscaId_Bairro"	,"tempBuscaId_Bairro");
							request.setAttribute("tempBuscaBairro"		,"tempBuscaBairro");
							request.setAttribute("QuantidadePaginas"	, guiaLocomocaoComplementarNe.getQuantidadePaginasBairro());
						}
						
						break;
					}
					
					//Busca de Bairro Locomoï¿½ï¿½o Penhora
					case ( Configuracao.Curinga7 ) : {
						
						List listaRecalculoBairroLocomocaoPenhora = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoPenhora");
						if( listaRecalculoBairroLocomocaoPenhora == null ) {
							stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_SEM_PERMISSAO_ADD_LOCOMOCAO_COMPLEMENTAR));
						}
						else {
//							stAcao = PAGINA_BAIRRO_LOCALIZAR;
							
							tempList = guiaLocomocaoComplementarNe.consultarDescricaoBairro(nomebusca, posicaopaginaatual);
							
							request.setAttribute("ListaBairro"			, tempList);
							request.setAttribute("tempBuscaId_Bairro"	,"tempBuscaId_BairroLocomocaoPenhora");
							request.setAttribute("tempBuscaBairro"		,"tempBuscaBairro");
							request.setAttribute("QuantidadePaginas"	, guiaLocomocaoComplementarNe.getQuantidadePaginasBairro());
						}
						
						break;
					}
					
					//Busca de Bairro Locomoï¿½ï¿½o Avaliaï¿½ï¿½o
					case ( Configuracao.Curinga8 ) : {
						
						List listaRecalculoBairroLocomocaoAvaliacao = (List) request.getSession().getAttribute("ListaRecalculoBairroLocomocaoAvaliacao");
						if( listaRecalculoBairroLocomocaoAvaliacao == null ) {
							stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_SEM_PERMISSAO_ADD_LOCOMOCAO_COMPLEMENTAR));
						}
						else {
//							stAcao = PAGINA_BAIRRO_LOCALIZAR;
							
							tempList = guiaLocomocaoComplementarNe.consultarDescricaoBairro(nomebusca, posicaopaginaatual);
							
							request.setAttribute("ListaBairro"			, tempList);
							request.setAttribute("tempBuscaId_Bairro"	,"tempBuscaId_BairroLocomocaoAvaliacao");
							request.setAttribute("tempBuscaBairro"		,"tempBuscaBairro");
							request.setAttribute("QuantidadePaginas"	, guiaLocomocaoComplementarNe.getQuantidadePaginasBairro());
						}
						
						break;
					}
					
					//Busca de Bairro
					case ( Configuracao.Curinga9 ) : {
						
						stAcao = PAGINA_GUIA_INTERMEDIARIA_COMPLEMENTAR;
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_SEM_PERMISSAO_ADD_LOCOMOCAO_COMPLEMENTAR));
						
//						stAcao = PAGINA_BAIRRO_LOCALIZAR;
//						
//						tempList = guiaIntermediariaComplementarNe.consultarDescricaoBairro(nomebusca, posicaopaginaatual);
//						
//						request.setAttribute("ListaBairro"			, tempList);
//						request.setAttribute("tempBuscaId_Bairro"	,"tempBuscaId_BairroIntimacao");
//						request.setAttribute("tempBuscaBairro"		,"tempBuscaBairro");
//						request.setAttribute("QuantidadePaginas"	, guiaIntermediariaComplementarNe.getQuantidadePaginasBairro());
						
						break;
					}
				}
				
				break;
			}
			
			default : {
				//Busca Bairro
				stId = request.getParameter("tempBuscaId_Bairro");
				if( stId != null ) {
					BairroDt bairroDt = new BairroDt();
					
					bairroDt.setId(stId);
					bairroDt.setBairro(request.getParameter("tempBuscaBairro"));
					bairroDt.setCidade(request.getParameter("BairroCidade"));
					bairroDt.setUf(request.getParameter("BairroUf"));
					
//					if( listaBairroDt != null )
//						listaBairroDt.add(bairroDt);
//					
					//************
					if( listaBairroDt != null ) {
						for( int i = 0; i < listaBairroDt.size(); i++ ) {
							if( listaBairroDt.get(i) == null ) {
								listaBairroDt.set(i, bairroDt);
								break;
							}
						}
					}
					//************
					
					request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
					
					if( listaBairroLocomocaoContaVinculada != null )
						listaBairroLocomocaoContaVinculada.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
					
					
					stId = null;
				}
				
				//Busca Bairro Intimaï¿½ï¿½o
				stId = request.getParameter("tempBuscaId_BairroIntimacao");
				if( stId != null ) {
					BairroDt bairroDt = new BairroDt();
					
					bairroDt.setId(stId);
					bairroDt.setBairro(request.getParameter("tempBuscaBairro"));
					bairroDt.setCidade(request.getParameter("BairroCidade"));
					bairroDt.setUf(request.getParameter("BairroUf"));
					
					if( listaBairroIntimacaoDt != null )
						listaBairroIntimacaoDt.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoIntimacao", listaBairroIntimacaoDt);
					
					if( listaBairroLocomocaoContaVinculada != null )
						listaBairroLocomocaoContaVinculada.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
					
					
					stId = null;
				}
				
				//Busca Penhora
				stId = request.getParameter("tempBuscaId_BairroLocomocaoPenhora");
				if( stId != null ) {
					BairroDt bairroDt = new BairroDt();
					
					bairroDt.setId(stId);
					bairroDt.setBairro(request.getParameter("tempBuscaBairro"));
					bairroDt.setCidade(request.getParameter("BairroCidade"));
					bairroDt.setUf(request.getParameter("BairroUf"));
					
					if( listaBairroLocomocaoPenhora != null )
						listaBairroLocomocaoPenhora.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoPenhora", listaBairroLocomocaoPenhora);
					
					if( listaBairroLocomocaoContaVinculada != null )
						listaBairroLocomocaoContaVinculada.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
					
					
					stId = null;
				}
				
				//Busca Avaliaï¿½ï¿½o
				stId = request.getParameter("tempBuscaId_BairroLocomocaoAvaliacao");
				if( stId != null ) {
					BairroDt bairroDt = new BairroDt();
					
					bairroDt.setId(stId);
					bairroDt.setBairro(request.getParameter("tempBuscaBairro"));
					bairroDt.setCidade(request.getParameter("BairroCidade"));
					bairroDt.setUf(request.getParameter("BairroUf"));
					
					if( listaBairroLocomocaoAvaliacao != null )
						listaBairroLocomocaoAvaliacao.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoAvaliacao", listaBairroLocomocaoAvaliacao);
					
					if( listaBairroLocomocaoContaVinculada != null )
						listaBairroLocomocaoContaVinculada.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
					
					
					stId = null;
				}
				
				break;
			}
				
		}
		
		request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}
