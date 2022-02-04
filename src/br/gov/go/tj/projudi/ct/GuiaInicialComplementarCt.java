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

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaInicialComplementarDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.GuiaInicialComplementarNe;
import br.gov.go.tj.projudi.ne.GuiaItemNe;
import br.gov.go.tj.projudi.ne.GuiaLocomocaoNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaInicialComplementarCt extends Controle {
	
	private static final long serialVersionUID = 8549400000031561215L;

	private static final String PAGINA_GUIA_INICIAL_COMPLEMENTAR 	= "/WEB-INF/jsptjgo/GuiaInicialComplementar.jsp";
	private static final String PAGINA_CONSULTAR_GUIA 			 	= "/WEB-INF/jsptjgo/GuiaInicialComplementarConsultar.jsp";
	protected static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_PADRAO_LOCALIZAR 			= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	private static final String NOME_CONTROLE_WEB_XML = "GuiaInicialComplementar";
	
	private GuiaInicialComplementarNe guiaInicialComplementarNe = new GuiaInicialComplementarNe();
	private GuiaLocomocaoNe guiaLocomocaoNe = new GuiaLocomocaoNe();

	@Override
	public int Permissao() {
		return GuiaInicialComplementarDt.CodigoPermissao;
	}
	
	protected String obtenhaNomeControleWebXml() {
		return NOME_CONTROLE_WEB_XML;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String GRAU_ESCOLHIDO = GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU; //valor default
		
		String stAcao 		= null;
		int passoEditar 	= -1;
		String stId 		= "";
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		
		stAcao = PAGINA_GUIA_INICIAL_COMPLEMENTAR;
		
		//********************************************
		// Variáveis utilizadas pela página
		List listaCustaDt 		= null;
		List listaBairroDt 		= null;
		List listaQuantidadeBairroDt = null;
		List listaItensGuia 	= null;
		
		//********************************************
		//Variáveis objetos
		GuiaEmissaoDt guiaEmissaoDt = null;
		GuiaEmissaoDt guiaEmissaoDtBase = null;
		
		//********************************************
		//Variáveis de sessão
		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
		if( guiaEmissaoDt == null ) {
			guiaEmissaoDt = new GuiaEmissaoDt();
		}
		guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		guiaEmissaoDtBase = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDtBase");
		if( guiaEmissaoDtBase == null ) {
			guiaEmissaoDtBase = new GuiaEmissaoDt();
		}
		
		listaItensGuia = (List) request.getSession().getAttribute("ListaItensGuia");
		if( listaItensGuia == null ) {
			listaItensGuia = new ArrayList();
		}
		
		listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
		if( listaCustaDt == null ) {
			listaCustaDt = new ArrayList();
		}
		
		listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
		if( listaBairroDt == null ) {
			listaBairroDt = new ArrayList();
		}
		
		listaQuantidadeBairroDt = (List) request.getSession().getAttribute("ListaQuantidadeBairroDt");
		if( listaQuantidadeBairroDt == null ) {
			listaQuantidadeBairroDt = new ArrayList();
		}
		
		//********************************************
		//Requests 
		request.setAttribute("tempPrograma" 			, obtenhaTituloPagina());
		request.setAttribute("tempRetorno" 				, obtenhaNomeControleWebXml());
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
				
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		if( request.getParameter("numeroGuiaInicialPaga") != null && request.getParameter("numeroGuiaInicialPaga").length() > 0 ) {
			guiaEmissaoDtBase.setNumeroGuiaCompleto(request.getParameter("numeroGuiaInicialPaga"));
		}
		
		if( request.getParameter("novoValorAcao") != null && request.getParameter("novoValorAcao").length() > 0 ) {
			guiaEmissaoDt.setNovoValorAcao(request.getParameter("novoValorAcao"));
			guiaEmissaoDt.setNovoValorAcaoAtualizado(request.getParameter("novoValorAcao"));
			guiaEmissaoDt.setValorAcao(request.getParameter("novoValorAcao"));
		}
		
		if( request.getParameter("origemEstado") != null && request.getParameter("origemEstado").length() > 0 ) {
			guiaEmissaoDt.setOrigemEstado(request.getParameter("origemEstado"));
		}
		
		if( request.getParameter("porteRemessaQuantidade") != null && request.getParameter("porteRemessaQuantidade").length() > 0 && !request.getParameter("porteRemessaQuantidade").equals("null") ) {
			guiaEmissaoDt.setPorteRemessaQuantidade(request.getParameter("porteRemessaQuantidade").toString());
			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("porteRemessaQuantidade").toString());
		}
		
		if( request.getParameter("protocoloIntegrado") != null && request.getParameter("protocoloIntegrado").length() > 0 ) {
			guiaEmissaoDt.setProtocoloIntegrado(request.getParameter("protocoloIntegrado"));
		}
		
		if( request.getParameter("requerente") != null && request.getParameter("requerente").length() > 0 ) {
			guiaEmissaoDt.setRequerente(request.getParameter("requerente"));
		}
		
		if( request.getParameter("requerido") != null && request.getParameter("requerido").length() > 0 ) {
			guiaEmissaoDt.setRequerido(request.getParameter("requerido"));
		}
		
		if( request.getParameter("numeroProcessoDependente") != null && request.getParameter("numeroProcessoDependente").length() > 0 ) {
			guiaEmissaoDt.setNumeroProcessoDependente(request.getParameter("numeroProcessoDependente"));
		}
		
		if( request.getParameter("numeroImpetrantes") != null && request.getParameter("numeroImpetrantes").length() > 0 ) {
			guiaEmissaoDt.setNumeroImpetrantes(request.getParameter("numeroImpetrantes"));
		}
		
		if( request.getParameter("grau") != null && request.getParameter("grau").length() > 0 ) {
			guiaEmissaoDt.setCodigoGrau(request.getParameter("grau"));
			if( guiaEmissaoDt.isGuiaInicial1Grau() ) {
				GRAU_ESCOLHIDO = GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU;
			}
			else {
				GRAU_ESCOLHIDO = GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU;
			}
			guiaEmissaoDt.setCodigoGrau(String.valueOf(GRAU_ESCOLHIDO));
		}
		else {
			GRAU_ESCOLHIDO = GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU;
			guiaEmissaoDt.setCodigoGrau(String.valueOf(GRAU_ESCOLHIDO));
		}
		request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
		
		if( request.getParameter("area") != null && request.getParameter("area").length() > 0 ) {
			guiaEmissaoDt.setCodigoArea(request.getParameter("area"));
		}
		
		if( request.getParameter("bensPartilhar") != null && !request.getParameter("bensPartilhar").equals("null") && request.getParameter("bensPartilhar").length() > 0 ) {
			guiaEmissaoDt.setBensPartilhar(request.getParameter("bensPartilhar"));
		}
		
		if( request.getParameter("correioQuantidade") != null && request.getParameter("correioQuantidade").length() > 0 ) {
			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("correioQuantidade"));
		}
		
		request.setAttribute("exibeOficialCompanheiro", new Boolean(true)); //Conforme informado pelo Marcelo da Corregedoria, só é realizado o cálculo em dobro automático na guia inicial.
		
		switch(paginaatual) {
		
			case Configuracao.Novo: {
				
				stAcao = PAGINA_CONSULTAR_GUIA;
				
				if( guiaInicialComplementarNe.isConexaoSPG_OK() ) {
					
					limparGuia(guiaEmissaoDt, request);
					
					guiaEmissaoDt = new GuiaEmissaoDt();
					guiaEmissaoDtBase = new GuiaEmissaoDt();
					
					guiaEmissaoDtBase.setNumeroGuiaCompleto("");
					
					request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					request.getSession().setAttribute("GuiaEmissaoDtBase", guiaEmissaoDtBase);
					
					if( request.getParameter("MensagemOk") != null ) {
						request.setAttribute("MensagemOk",request.getParameter("MensagemOk"));
					}
					if( request.getParameter("MensagemErro") != null ) {
						request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
					}
					
					guiaEmissaoDt.setAtosEscrivaesCivel("1");
					
				}
				else {
					redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Cancelar+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
					return;
				}
				
				break;
			}
			
			case Configuracao.Cancelar : {
				stAcao = PAGINA_CONSULTAR_GUIA;
				limparGuia(guiaEmissaoDt, request);
				
				guiaEmissaoDt = new GuiaEmissaoDt();
				guiaEmissaoDtBase = new GuiaEmissaoDt();
				
				guiaEmissaoDtBase.setNumeroGuiaCompleto("");
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				request.getSession().setAttribute("GuiaEmissaoDtBase", guiaEmissaoDtBase);
				
				if( request.getParameter("MensagemOk") != null ) {
					request.setAttribute("MensagemOk",request.getParameter("MensagemOk"));
				}
				if( request.getParameter("MensagemErro") != null ) {
					request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
				}
				
				break;
			}
			
			case Configuracao.Localizar : {
				
				stAcao = PAGINA_GUIA_INICIAL_COMPLEMENTAR;
				
				if( guiaEmissaoDtBase.getNumeroGuiaCompleto() == null || guiaEmissaoDtBase.getNumeroGuiaCompleto().isEmpty() ) {
					throw new MensagemException("Por favor, informe o número da Guia Completo!");
				}
				
				String numeroGuiaInformado = guiaEmissaoDtBase.getNumeroGuiaCompleto().trim();
				
				numeroGuiaInformado = numeroGuiaInformado.trim().replace("/", "").replace("-", "").replace(" ", "");
				
				GuiaEmissaoDt guiaEmissaoInicialDtBase = guiaInicialComplementarNe.consultarGuiaInicial(numeroGuiaInformado);
				
				if( guiaEmissaoInicialDtBase != null ) {
					
					if( guiaEmissaoInicialDtBase.isGuiaCancelada() ) {
						throw new MensagemException("Ação não permitida. Guia está cancelada. Por favor, informar outra guia.");
					}
					
					if( guiaInicialComplementarNe.isGuiaPaga(guiaEmissaoInicialDtBase.getNumeroGuiaCompleto()) ) {
						
						//Optei por fazer separado este passo:
						//Consulta novamente para validar se é uma guia que não foi complementada já.
						guiaEmissaoInicialDtBase = guiaInicialComplementarNe.consultarGuiaInicialNaoComplementada(numeroGuiaInformado);
						
						if( guiaEmissaoInicialDtBase == null || (guiaEmissaoInicialDtBase.getId_GuiaEmissaoPrincipal() != null && !guiaEmissaoInicialDtBase.getId_GuiaEmissaoPrincipal().isEmpty()) ) {
							throw new MensagemException("Esta guia "+ Funcoes.FormatarNumeroSerieGuia(numeroGuiaInformado) +" já foi complementada por outra Guia. Ela não pode ser complementada novamente.");
						}
						
						//Consulta os itens da guia
						guiaEmissaoInicialDtBase.setListaGuiaItemDt(new GuiaItemNe().consultarItensGuia(guiaEmissaoInicialDtBase.getId()));
						
						if( guiaEmissaoInicialDtBase.getId_Processo() != null && !guiaEmissaoInicialDtBase.getId_Processo().isEmpty() ) {
							guiaEmissaoDt.setId_Processo(			guiaEmissaoInicialDtBase.getId_Processo());
							
							if( guiaEmissaoInicialDtBase.getId_Serventia() != null && !guiaEmissaoInicialDtBase.getId_Serventia().isEmpty() ) {
								guiaEmissaoDt.setId_Serventia(			guiaEmissaoInicialDtBase.getId_Serventia());
							}
						}
						
						guiaEmissaoDt.setId_ProcessoTipo(			guiaEmissaoInicialDtBase.getId_ProcessoTipo());
						guiaEmissaoDt.setProcessoTipo(				guiaEmissaoInicialDtBase.getProcessoTipo());
						guiaEmissaoDt.setNovoValorAcaoAtualizado(	guiaEmissaoInicialDtBase.getNovoValorAcaoAtualizado());
						guiaEmissaoDt.setNovoValorAcao(				guiaEmissaoInicialDtBase.getNovoValorAcaoAtualizado());
						
						guiaEmissaoDt.setNumeroProcessoDependente(guiaEmissaoInicialDtBase.getNumeroProcessoDependente());
						guiaEmissaoDt.setId_Comarca(guiaEmissaoInicialDtBase.getId_Comarca());
						if( guiaEmissaoInicialDtBase.getId_Comarca() != null ) {
							guiaEmissaoDt.setComarca(new ComarcaNe().consultarId(guiaEmissaoInicialDtBase.getId_Comarca()).getComarca());
						}
						guiaEmissaoDt.setId_AreaDistribuicao(guiaEmissaoInicialDtBase.getId_AreaDistribuicao());
						if( guiaEmissaoInicialDtBase.getId_AreaDistribuicao() != null ) {
							guiaEmissaoDt.setAreaDistribuicao(new AreaDistribuicaoNe().consultarId(guiaEmissaoInicialDtBase.getId_AreaDistribuicao()).getAreaDistribuicao());
						}
						
						guiaEmissaoDt.setCodigoArea(String.valueOf(AreaDt.CIVEL));
						guiaEmissaoDt.setBensPartilhar(new String(GuiaEmissaoDt.VALOR_NAO));
						guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
						guiaEmissaoDt.setDistribuidorQuantidade("1");
						guiaEmissaoDt.setContadorQuantidade("1");
						guiaEmissaoDt.setCustasQuantidade("1");
						guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
						guiaEmissaoDt.setEscrivaniaQuantidade("1");
						guiaEmissaoDt.setNumeroImpetrantes("0");
						guiaEmissaoDt.setPenhoraQuantidade("1");
						guiaEmissaoDt.setOrigemEstado("");
						guiaEmissaoDt.setProtocoloIntegrado("");
						
						guiaEmissaoDt.setRequerente(guiaEmissaoInicialDtBase.getRequerente());
						guiaEmissaoDt.setRequerido(guiaEmissaoInicialDtBase.getRequerido());
						
						//seta qual é a guia principal desta que está sendo gerada.
						guiaEmissaoDt.setId_GuiaEmissaoPrincipal(guiaEmissaoInicialDtBase.getId());
						
						if( guiaEmissaoInicialDtBase.getNumeroGuiaCompleto() != null && !guiaEmissaoInicialDtBase.getNumeroGuiaCompleto().isEmpty() ) {
							guiaEmissaoDt.setNumeroGuiaPrincipalCompleto(guiaEmissaoInicialDtBase.getNumeroGuiaCompleto());
						}
						
						ProcessoTipoDt processoTipoDt = guiaInicialComplementarNe.consultarProcessoTipo(guiaEmissaoDt.getId_ProcessoTipo());
						
						guiaEmissaoDt.setProcessoTipoCodigo(processoTipoDt.getProcessoTipoCodigo());
						
						request.getSession().setAttribute("GuiaEmissaoDtBase", guiaEmissaoInicialDtBase);
						request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					}
					else {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo+"&MensagemErro=Guia Inicial "+ Funcoes.FormatarNumeroSerieGuia(guiaEmissaoInicialDtBase.getNumeroGuiaCompleto()) +" encontrada, porém esta não pode ser complementada porque não se encontra <b>paga</b>!");
						return;
					}
				}
				else {
					stAcao = PAGINA_CONSULTAR_GUIA;
					request.setAttribute("MensagemErro", "Guia Inicial "+ Funcoes.FormatarNumeroSerieGuia(numeroGuiaInformado) +" não encontrada. <br />Talvez esta guia não seja do tipo inicial. <br />Verifique o número e tente novamente.");
				}
				
				break;
			}
			
			//Valida processo principal ou vinculado
			case Configuracao.Curinga7: {
				
				if( guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
					if( !guiaInicialComplementarNe.isProcessoNumeroCompletoExistente(guiaEmissaoDt.getNumeroProcessoDependente()) ) {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE));
						return;
					}
					
					//Se encontrou o processo então esconde div de escolha da comarca e área distribuição
					guiaEmissaoDt.setId_Comarca(null);
					guiaEmissaoDt.setId_AreaDistribuicao(null);
					guiaEmissaoDt.setId_ProcessoTipo(null);
					
					guiaEmissaoDt.setComarca("");
					guiaEmissaoDt.setAreaDistribuicao("");
					guiaEmissaoDt.setProcessoTipo("");
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
						//Não tem break pq precisa mostrar a tela novamente de prévia.
					}
					
					case Configuracao.Curinga8: {
						
						request.getSession().removeAttribute("ListaGuiaItemDt");
						
						//Validação da modelo da guia
						if( guiaInicialComplementarNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, String.valueOf(GRAU_ESCOLHIDO), guiaEmissaoDt.getId_ProcessoTipo()) == null ) {
							throw new MensagemException("Modelo/Template da guia não encontrado. Pode ser que esta classe não tenha cálculo homologado(Guia Inicial Complementar).");
						}
						
						//Se o processo dependente não está preenchido, então
						//Valida se preencheu a comarca, area de distribuicao e classe
						if( guiaEmissaoDt.getNumeroProcessoDependente() == null || guiaEmissaoDt.getNumeroProcessoDependente().isEmpty() ) {
							if( guiaEmissaoDt.getId_Comarca() == null || guiaEmissaoDt.getId_Comarca().isEmpty() ) {
								throw new MensagemException("Por favor, informe a Comarca.");
							}
							
							if( guiaEmissaoDt.getId_AreaDistribuicao() == null || guiaEmissaoDt.getId_AreaDistribuicao().isEmpty() ) {
								throw new MensagemException("Por favor, informe a Área de Distribuição.");
							}
							
							if( guiaEmissaoDt.getId_ProcessoTipo() == null || guiaEmissaoDt.getId_ProcessoTipo().isEmpty() ) {
								throw new MensagemException("Por favor, informe a Classe.");
							}
						}
						
						//Se o processo dependente está preenchido
						//Valida se a classe está presente
						if( guiaEmissaoDt.getNumeroProcessoDependente() != null && !guiaEmissaoDt.getNumeroProcessoDependente().isEmpty() ) {
							if( guiaEmissaoDt.getId_ProcessoTipo() == null || guiaEmissaoDt.getId_ProcessoTipo().isEmpty() ) {
								throw new MensagemException("Por favor, informe a Classe.");
							}
						}
						
						//Valida se o processo tipo é "Carta de Ordem" ou "Carta Precatória" para verificar se é o estado 
						// para a cobrança de taxa judiciária
//						if( guiaEmissaoDt.getProcessoTipoCodigo() != null &&
//							guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && 
//							guiaInicialComplementarNe.isProcessoTipoPortePostagem_e_OrigemEstado(guiaEmissaoDt.getProcessoTipoCodigo()) &&
//							(guiaEmissaoDt.getOrigemEstado() == null || guiaEmissaoDt.getOrigemEstado().isEmpty() )) {
//							
//							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro=Por favor. De acordo com a Classe escolhida, informe se o Estado de Goiás é origem ou não para a cobrança da Taxa Judiciária.");
//							break;
//						}
						
						//Validação do processo vinculado caso seja informado
						boolean validaComarcaAreaDistribuicao = true;
						if( guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
							if( guiaInicialComplementarNe.isProcessoNumeroCompletoExistente(guiaEmissaoDt.getNumeroProcessoDependente()) ) {
								validaComarcaAreaDistribuicao = false;
							}
							else {
								redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE));
								return;
							}
						}
						
						if( validaComarcaAreaDistribuicao ) {
							//Verificar se comarca, area de distribuição e classe está preenchido
							if( guiaEmissaoDt.getId_Comarca() == null || guiaEmissaoDt.getId_AreaDistribuicao() == null ) {
								redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro=Por favor. Escolha Comarca, Área de Distribuição e Classe.");
								return;
							}
						}
						
						if( guiaEmissaoDt.getRequerente() == null || (guiaEmissaoDt.getRequerente() != null && guiaEmissaoDt.getRequerente().trim().length() == 0) ) {
							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro=Por favor, informe o nome da parte autora.");
							return;
						}
						
						if( guiaEmissaoDt.getId_ProcessoTipo() == null ) {
							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro=Por favor, escolha a Classe CNJ.");
							return;
						}
						
						//Consulta lista de itens da guia
						listaItensGuia = guiaInicialComplementarNe.consultarItensGuiaNovoRegimento(null, String.valueOf(GRAU_ESCOLHIDO), guiaEmissaoDt.getId_ProcessoTipo());
						
						GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, String.valueOf(GRAU_ESCOLHIDO), guiaEmissaoDt.getId_ProcessoTipo());
						guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
						
						//Locomoção com Zona-Bairro
						List valoresIdBairro = null;
						List valoresIdBairroContaVinculada = null;
						listaBairroDt = (List)request.getSession().getAttribute("ListaBairroDt");
						listaQuantidadeBairroDt = new ArrayList();
						if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
							if( valoresIdBairro == null )
								valoresIdBairro = new ArrayList();
							if( valoresIdBairroContaVinculada == null )
								valoresIdBairroContaVinculada = new ArrayList();
							
							for(int i = 0; i < listaBairroDt.size(); i++) {
								BairroDt bairroDt = (BairroDt)listaBairroDt.get(i);
								
								
								int quantidadeLocomocao = 0;
								if( request.getSession().getAttribute("guiaInicialPublica") != null && Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()) > 0 ) {
									if( listaBairroDt.size() == Funcoes.StringToInt(request.getSession().getAttribute("guiaInicialPublica").toString()) ) {
										
										if( request.getSession().getAttribute("guiaInicialPublicaQuantidadeLocomocao"+i) != null ) {
											quantidadeLocomocao = Funcoes.StringToInt(request.getSession().getAttribute("guiaInicialPublicaQuantidadeLocomocao"+i).toString());
										}
										
									}
								}
								else {
									quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocao"+i));
								}
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									valoresIdBairro.add(bairroDt.getId());
									valoresIdBairroContaVinculada.add(bairroDt.getId());
								}
								
								listaQuantidadeBairroDt.add(String.valueOf(quantidadeLocomocao));
								request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
								
								//Adicionar item de Locomoção
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
//TODO ATENÇÃO: Comentei estas linhas depois de alterar e colocar a div na tela de finalidade.
//Em conversa com o Marcio peguei um trecho de código abaixo do ct de locomocaoCt do Marcio.
//Agora deixei este trecho de locomocao zona-bairro para preencher a lista de id dos bairros e fazer a verificação 
//mais a baixo para ver se o bairro está zoneado.
//TODO Fred									listaItensGuia.add( guiaInicialComplementarNe.adicionarItemLocomocao(bairroDt) );
//TODO Fred									listaItensGuia.add( guiaInicialComplementarNe.adicionarItemLocomocaoOficialContaVinculada(bairroDt) );
								}
							}
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
						if( valoresIdBairro != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
						if( guiaEmissaoDt.getProcessoTipoCodigo() != null && guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && guiaInicialComplementarNe.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
							valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
							if( guiaEmissaoDt.getNumeroImpetrantes() != null && guiaEmissaoDt.getNumeroImpetrantes().length() > 0 ) {
								valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
							}
							if( guiaEmissaoDt.getProcessoTipoCodigo() != null && Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO ) {
								valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO);
							}
						}
						if( guiaEmissaoDt.getProcessoTipoCodigo() != null && 
								guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && 
								(Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO || 
								 Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_8069 || 
								 Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_1531 || 
								 Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_CIVEL)) {
								
							valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + guiaEmissaoDt.getProcessoTipoCodigo());
						}
						
						//Verifica se é cautelar, contencioso
						guiaInicialComplementarNe.adicionarItemCautelarContencioso(listaItensGuia, guiaEmissaoDt, null);
						
						//Despesa Postal
						if( guiaEmissaoDt.getCorreioQuantidade() != null && guiaEmissaoDt.getCorreioQuantidade().length() > 0 && !guiaEmissaoDt.getCorreioQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							listaItensGuia.add( guiaInicialComplementarNe.adicionarItem(CustaDt.DESPESA_POSTAL) );
						}
						
						//Inclusão da Taxa Judiciária
						//Estado é origem?
						if( guiaEmissaoDt.getProcessoTipoCodigo() != null &&
							guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && 
							guiaInicialComplementarNe.isProcessoTipoPortePostagem_e_OrigemEstado(guiaEmissaoDt.getProcessoTipoCodigo()) &&
							guiaEmissaoDt.getOrigemEstado() != null && 
							guiaEmissaoDt.getOrigemEstado().equals(GuiaEmissaoDt.VALOR_NAO) ) {
							
							listaItensGuia.add( guiaInicialComplementarNe.adicionarItem(CustaDt.TAXA_JUDICIARIA_PROCESSO) );
							
							//Pedido do Marcelo da Corregedoria: Quando é natureza "carta precatoria" e a origem não é o estado, então cobra o valor mínimo
							if( new ProcessoTipoNe().isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
								valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
							}
						}
						
						//Porte Remessa
						if( guiaEmissaoDt.getProtocoloIntegrado() != null && guiaEmissaoDt.getPorteRemessaQuantidade() != null && Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade()) > 0 ) {
							String protocoloIntegrado[] = request.getParameterValues("protocoloIntegrado");
							if( protocoloIntegrado != null && guiaEmissaoDt.getProtocoloIntegrado().equals(GuiaEmissaoDt.VALOR_SIM) ) {
								listaItensGuia.add( guiaInicialComplementarNe.adicionarItem(CustaDt.PORTE_REMESSA) );
							}
						}
						
						List listaIdBairroAux = new ArrayList();
						if( valoresIdBairro != null && valoresIdBairro.size() > 0 ) {
							listaIdBairroAux.addAll(valoresIdBairro);
						}
						boolean bairrosZoneados = guiaInicialComplementarNe.isBairroZoneado(listaIdBairroAux);
						
						
						//Verifica se guia inicial é de locomoção em dobro
						if( guiaInicialComplementarNe.isProcessoTipoDobrarLocomocao(guiaEmissaoDt.getProcessoTipoCodigo())) {
							guiaEmissaoDt.setOficialCompanheiro(GuiaEmissaoDt.VALOR_SIM);
						}
						
						//Verifica se guia inicial é de oficial companheiro obrigatório
						if( guiaInicialComplementarNe.isProcessoTipoOficialCompanheiroObrigatorio(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
							guiaEmissaoDt.setOficialCompanheiro(GuiaEmissaoDt.VALOR_SIM);
						}
						
						List listaGuiaItemDt = new ArrayList();
						if( bairrosZoneados ) {
							listaGuiaItemDt = guiaInicialComplementarNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						}
						
						
						
						List<GuiaItemDt> listaGuiaItemLocomocaoDt = new ArrayList<GuiaItemDt>();
						List<Integer> listaQuantidadeBairroDt_INTEGER = new ArrayList<Integer>();
						
						//
						guiaLocomocaoNe.recalcularGuiaComplementar(guiaEmissaoDtBase, listaGuiaItemDt, listaGuiaItemLocomocaoDt);
						guiaLocomocaoNe.recalcularTotalGuia(listaGuiaItemDt);
						
						
						guiaInicialComplementarNe.getGuiaCalculoNe().setTotalGuia(guiaInicialComplementarNe.getGuiaCalculoNe().calcularTotalGuia(listaGuiaItemDt));
						
						
						if( !bairrosZoneados ) {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
							stAcao = PAGINA_GUIA_INICIAL_COMPLEMENTAR;
						}
						else {
							//Deve haver no mínimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
								
								request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.setAttribute("TotalGuia", guiaInicialComplementarNe.getGuiaCalculoNe().getTotalGuia() );
								
								request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
								request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaInicialComplementarNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
								request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaInicialComplementarNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
								request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
								request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
								request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
								
								guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
								
								request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
								
								stAcao = obtenhaAcaoPreviaDeCalculo(request);
								
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
								stAcao = PAGINA_GUIA_INICIAL_COMPLEMENTAR;
							}
						}
						
						break;
					}
					
					case Configuracao.Curinga7: {
						
						if( guiaEmissaoDtBase != null && guiaEmissaoDtBase.getNumeroGuiaCompleto() != null && !guiaEmissaoDtBase.getNumeroGuiaCompleto().isEmpty() ) {
							stAcao = PAGINA_GUIA_INICIAL_COMPLEMENTAR;
						}
						else {
							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo);
							return;
						}
						
						break;
					}
				}
				
				break;
			}
			
			//Remover Item
			case Configuracao.Excluir : {
				
				switch(passoEditar) {
				
					//Remover Item de Bairro
					case Configuracao.Curinga7 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
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
					
				}
				
				break;
			}
			
			//Impressão da guia
			case Configuracao.Imprimir : {
				
				guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
				if( guiaEmissaoDt == null ) {
					guiaEmissaoDt = new GuiaEmissaoDt();
				}
				
				//Validações para evitar possíveis problemas de guia inicial sem estes dados preenchidos
				//Se número do processo dependente está preenchido, informa somente a classe(proc_tipo)
				String msgValidacaoGuiaInicial = "";
				
				if (guiaEmissaoDt.getId_Processo() != null && guiaInicialComplementarNe.isProcessoPossuiGuiaEnviadaCadin(guiaEmissaoDt.getId_Processo())) {
					msgValidacaoGuiaInicial += "<br />Impossível gerar essa guia, pois esse processo já possui débito enviado ao <b>CADIN/SEFAZ</b>! <br /> Para mais informações: <b>(62) 3213-1581</b> TELEJUDICIÁRIO!";
				} else {
					if( !guiaEmissaoDt.isNumeroProcessoDependentePreenchido() ) {
						if( !guiaInicialComplementarNe.isIdComarcaPreenchido(guiaEmissaoDt.getId_Comarca()) ) {
							msgValidacaoGuiaInicial += "<br />Comarca não identificada nesta guia, verifique se escolheu a Comarca.";
						}
						if( !guiaInicialComplementarNe.isIdAreaDistribuicaoPreenchido(guiaEmissaoDt.getId_AreaDistribuicao()) ) {
							msgValidacaoGuiaInicial += "<br />Área de Distribuição não identificada nesta guia, verifique se escolheu a Área de Distribuição.";
						}
						if( !guiaInicialComplementarNe.isIdProcessoTipoPreenchido(guiaEmissaoDt.getId_ProcessoTipo()) ) {
							msgValidacaoGuiaInicial += "<br />Classe não identificada nesta guia, verifique se escolheu a Classe Processual.";
						}
					}
					else {
						if( !guiaInicialComplementarNe.isIdProcessoTipoPreenchido(guiaEmissaoDt.getId_ProcessoTipo()) ) {
							msgValidacaoGuiaInicial += "<br />Classe não identificada nesta guia, verifique se escolheu a Classe Processual.";
						}
						
						ProcessoDt processoDependente = guiaInicialComplementarNe.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente());
						if( processoDependente != null) {
							ServentiaDt serventiaProcessoDependente = guiaInicialComplementarNe.consultarIdServentia(processoDependente.getId_Serventia());
							if (serventiaProcessoDependente != null) {
								if (serventiaProcessoDependente.getId_Comarca() != null && serventiaProcessoDependente.getId_Comarca().trim().length() > 0) {
									guiaEmissaoDt.setId_Comarca(serventiaProcessoDependente.getId_Comarca());
								} else {
									msgValidacaoGuiaInicial += "<br />Processo dependente não possui comarca vinculada à serventia " + serventiaProcessoDependente.getServentiaCodigo() + "-" + serventiaProcessoDependente.getServentia() + ".";		
								}
							}						
							if (processoDependente.getId_AreaDistribuicao() != null && processoDependente.getId_AreaDistribuicao().trim().length() > 0) {
								guiaEmissaoDt.setId_AreaDistribuicao(processoDependente.getId_AreaDistribuicao());
							} else {
								msgValidacaoGuiaInicial += "<br />Processo dependente não possui area de distribuição.";		
							}
						} else {
							msgValidacaoGuiaInicial += "<br />Processo dependente não cadastrado.";
						}
					}
				}								
				
				if( msgValidacaoGuiaInicial.length() > 0 ) {
					request.setAttribute("MensagemErro", msgValidacaoGuiaInicial);
					stAcao = PAGINA_GUIA_INICIAL_COMPLEMENTAR;
				}
				else {
					//Obtem o proximo numero de Guia
					if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
						guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
						guiaEmissaoDt.setNumeroGuiaCompleto( guiaInicialComplementarNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
					}
					guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
					
					guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
					
					//Salvar GuiaEmissão
					guiaInicialComplementarNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					
					request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					
					switch(passoEditar) {
					
						case Configuracao.Imprimir: {
							
							guiaEmissaoDt.setApelante(guiaEmissaoDt.getRequerente());
							guiaEmissaoDt.setApelado(guiaEmissaoDt.getRequerido());
							guiaEmissaoDt.setValorAcao(guiaEmissaoDt.getNovoValorAcao());
							
							//Geração da guia PDF
							byte[] byTemp = guiaInicialComplementarNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), guiaEmissaoDt.getAreaDistribuicao(), guiaEmissaoDt, guiaEmissaoDt.getCodigoGrau(), "COMPLEMENTAR");
							
							String nome="GuiaInicial_Numero_"+ guiaEmissaoDt.getNumeroGuiaCompleto() ;							
							enviarPDF(response, byTemp, nome);
							request.getSession().setAttribute("GuiaEmissaoDt", limparGuia(guiaEmissaoDt, request));
							return;
						}
						
						case Configuracao.Salvar : {
							
							request.getSession().removeAttribute("ListaGuiaItemDt");
							request.getSession().removeAttribute("ListaCustaDt");
							request.getSession().removeAttribute("TotalGuia");
							request.getSession().removeAttribute("ListaBairroDt");
							request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
							
							request.getSession().removeAttribute("ListaGuiasRateio");
							request.getSession().removeAttribute("ListaTotalGuiaRateio");
							request.getSession().removeAttribute("ListaNomeParteGuia");
							request.getSession().removeAttribute("ListaNomePartePorcentagemGuia");
							
							request.setAttribute("MensagemOk","Guia Emitida com Sucesso!");
							
							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=" + Configuracao.Novo + "&MensagemOk=Guia Emitida com Sucesso! Número: " + Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()));
							return;
						}
					}
				}
				
				break;
			}			

			//Busca comarca
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null) {
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					request.setAttribute("tempBuscaId", "Id_Comarca");
					request.setAttribute("tempBuscaDescricao", "Comarca");
					request.setAttribute("tempBuscaPrograma", "Comarca");
					request.setAttribute("tempRetorno", obtenhaNomeControleWebXml());
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}
				else{
					String stTemp = "";
					stTemp = new ServentiaNe().consultarDescricaoComarcaJSON(stNomeBusca1, posicaopaginaatual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}
			
			// Consulta de Áreas de Distribuição
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null) {
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					String[] lisNomeBusca = {"Área de Distribuição"};
					String[] lisDescricao = {"Área de Distribuição"};
					request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma", "Área de Distribuição");
					request.setAttribute("tempRetorno", obtenhaNomeControleWebXml());
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("tempFluxo1", "LAS");
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}
				else {
					String stTemp = "";
					AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
					guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
					
					if( guiaEmissaoDt.getCodigoArea().equals(new String(String.valueOf(AreaDt.CIVEL))) ) {
						if( guiaEmissaoDt.isGuiaInicial1Grau() ) {							
							stTemp = areaDistribuicaoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(), null, posicaopaginaatual);
						}else {							
							stTemp = areaDistribuicaoNe.consultarAreasDistribuicaoSegundoGrauJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(), guiaEmissaoDt.getCodigoArea(), posicaopaginaatual);
						}
					}else {						
						stTemp = areaDistribuicaoNe.consultarAreasDistribuicaoCriminalJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(), posicaopaginaatual);
					}
					
					enviarJSON(response, stTemp);
											
					return;
				}
				break;
			}
			
			// Consulta de Processo Tipo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				
				if (request.getParameter("Passo")==null) {
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe"};
					request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoTipo");
					request.setAttribute("tempBuscaPrograma", "Classe");
					request.setAttribute("tempRetorno", obtenhaNomeControleWebXml());
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
						ProcessoDt processoViculadoDt = guiaInicialComplementarNe.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente());
						if (processoViculadoDt != null && processoViculadoDt.getId_Serventia() != null) {
							//1 grau
							if( guiaEmissaoDt.getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
								
									stTemp = new ProcessoNe().consultarProcessoTipoServentiaJSON(stNomeBusca1, processoViculadoDt.getId_Serventia() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
							}
							else {
								//2 grau								
								stTemp = new ProcessoNe().consultarDescricaoProcessoTipoJSON(stNomeBusca1, processoViculadoDt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
							}
						}
						else {
							request.setAttribute("MensagemErro","Processo não encontrado. Por favor, verifique o número do processo informado.");
						}
					}else {						
						stTemp = new ProcessoNe().consultarDescricaoProcessoTipoJSON(stNomeBusca1, guiaEmissaoDt.getId_AreaDistribuicao() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
					}				
					
					enviarJSON(response, stTemp);
						
					
					return;
					
				}
				break;
			}
			
			default : {
				
				if( request.getParameter("MensagemErro") != null ) {
					request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
				}
				
				//Busca Comarca
				stId = request.getParameter("Id_Comarca");
				if( stId != null ) {
					ComarcaDt comarcaDt = guiaInicialComplementarNe.consultarComarca(stId);
					
					guiaEmissaoDt.setId_Comarca(comarcaDt.getId());
					guiaEmissaoDt.setComarca(comarcaDt.getComarca());
					guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					
					guiaEmissaoDt.setId_AreaDistribuicao("");
					guiaEmissaoDt.setAreaDistribuicao("");
					
					request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					
					stId = null;
				}
				
				//Busca area de distribuição
				stId = request.getParameter("Id_AreaDistribuicao");
				if( stId != null ) {
					AreaDistribuicaoDt areaDistribuicaoDt = guiaInicialComplementarNe.consultarAreaDistribuicao(stId);
					
					guiaEmissaoDt.setId_AreaDistribuicao(areaDistribuicaoDt.getId());
					guiaEmissaoDt.setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());
					
					request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					
					stId = null;
				}
				
				//Busca processo tipo / classe
				stId = request.getParameter("Id_ProcessoTipo");
				if( stId != null ) {
					ProcessoTipoDt processoTipoDt = guiaInicialComplementarNe.consultarProcessoTipo(stId);
					
					guiaEmissaoDt.setId_ProcessoTipo(processoTipoDt.getId());
					guiaEmissaoDt.setProcessoTipo(processoTipoDt.getProcessoTipo());
					guiaEmissaoDt.setProcessoTipoCodigo(processoTipoDt.getProcessoTipoCodigo());
					
					request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					
					stId = null;
				}
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private GuiaEmissaoDt limparGuia(GuiaEmissaoDt guiaEmissaoDt, HttpServletRequest request) {
		List listaBairroDt;
		List listaQuantidadeBairroDt;
		guiaEmissaoDt = new GuiaEmissaoDt();
		
		guiaEmissaoDt.setRequerente("");
		guiaEmissaoDt.setRequerido("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setAreaDistribuicao("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNumeroProcessoDependente("");
		guiaEmissaoDt.setCodigoArea(String.valueOf(AreaDt.CIVEL));
		guiaEmissaoDt.setBensPartilhar(new String(GuiaEmissaoDt.VALOR_NAO));
		guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
		guiaEmissaoDt.setDistribuidorQuantidade("1");
		guiaEmissaoDt.setContadorQuantidade("1");
		guiaEmissaoDt.setCustasQuantidade("1");
		guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
		guiaEmissaoDt.setEscrivaniaQuantidade("1");
		guiaEmissaoDt.setNovoValorAcao("0,00");
		guiaEmissaoDt.setValorAcao("0,00");
		guiaEmissaoDt.setNumeroImpetrantes("0");
		guiaEmissaoDt.setPenhoraQuantidade("1");
		guiaEmissaoDt.setOrigemEstado("");
		guiaEmissaoDt.setProtocoloIntegrado("");
		
		guiaEmissaoDt.setId_Comarca("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setComarcaCodigo("");
		
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPGCodigo("");
		
		guiaEmissaoDt.setId_AreaDistribuicao("");
		guiaEmissaoDt.setAreaDistribuicao("");
		
		guiaEmissaoDt.setId_ProcessoTipo("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setProcessoTipoCodigo("");
		
		listaBairroDt = new ArrayList();
		listaQuantidadeBairroDt = new ArrayList();
		
		request.getSession().removeAttribute("ListaGuiaItemDt");
		request.getSession().removeAttribute("ListaCustaDt");
		request.getSession().removeAttribute("TotalGuia");
		request.getSession().removeAttribute("GuiaEmissaoDt");
		request.getSession().removeAttribute("GuiaEmissaoDtBase");
		request.getSession().removeAttribute("ListaBairroDt");
		request.getSession().removeAttribute("ListaQuantidadeBairroDt");
		if( request.getSession().getAttribute("guiaInicialPublica") != null && Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()) > 0 ) {
			for( int i = 0; i < Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()); i++ ) {
				request.getSession().removeAttribute("guiaInicialPublicaQuantidadeLocomocao"+i);
			}
		}
		request.getSession().removeAttribute("guiaInicialPublica");

		request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
		
		return guiaEmissaoDt;
	}

	protected String obtenhaAcaoPreviaDeCalculo(HttpServletRequest request) {
		// Parâmetro request utilizado na subclasse GuiaInicialPublicaCt.java
		return PAGINA_GUIA_PREVIA_CALCULO;
	}
	
	protected String obtenhaTituloPagina() {
		return "Guia Complementar da Inicial";
	}
	
}
