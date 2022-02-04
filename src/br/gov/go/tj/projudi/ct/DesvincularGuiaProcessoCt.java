package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.DesvincularGuiaProcessoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.InfoRepasseSPGDt;
import br.gov.go.tj.projudi.dt.LocomocaoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.LocomocaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class DesvincularGuiaProcessoCt extends Controle {

	private static final long serialVersionUID = 100000031561215L;

	private static final String PAGINA_DESVINCULAR_GUIA_PROCESSO 			= "/WEB-INF/jsptjgo/DesvincularGuiaProcesso.jsp";
	private static final String PAGINA_DESVINCULAR_GUIA_PROCESSO_CONSULTAR 	= "/WEB-INF/jsptjgo/DesvincularGuiaProcessoConsultar.jsp";
	
	private static final String NOME_CONTROLE_WEB_XML = "DesvincularGuiaProcesso";
	
	private GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();

	@Override
	public int Permissao() {
		return DesvincularGuiaProcessoDt.CodigoPermissao;
	}
	
	protected String obtenhaNomeControleWebXml() {
		return NOME_CONTROLE_WEB_XML;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
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
		
		stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO;
		
		//********************************************
		// Variáveis utilizadas pela página
		List listaCustaDt 		= null;
		List listaBairroDt 		= null;
		List listaQuantidadeBairroDt = null;
		List listaItensGuia 	= null;
		
		//********************************************
		//Variáveis objetos
//		GuiaEmissaoDt guiaEmissaoDt_A = null;
		
		//********************************************
		//Variáveis de sessão
//		guiaEmissaoDt_A = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt_A");
//		if( guiaEmissaoDt_A == null ) {
//			guiaEmissaoDt_A = new GuiaEmissaoDt();
//		}
		
		//********************************************
		//Requests 
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("tempPrograma" 			, obtenhaTituloPagina());
		request.setAttribute("tempRetorno" 				, obtenhaNomeControleWebXml());
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
				
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
//		if( request.getParameter("numeroGuia") != null && request.getParameter("numeroGuia").length() > 0 ) {
//			guiaEmissaoDt_A.setNumeroGuiaCompleto(request.getParameter("numeroGuia"));
//		}
		
		switch(paginaatual) {
		
			//Acessar página inicial
			case Configuracao.Novo: {
				
				limparTela(request);
				
				stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO_CONSULTAR;
				
				request.setAttribute("numeroGuiaDesvincular", "");
				request.setAttribute("numeroGuiaVincular", "");
				request.setAttribute("numeroProcessoVincular", "");
				request.setAttribute("numeroGuiaVincularConfirmar", "");
				request.setAttribute("numeroProcessoVincularConfirmar", "");
				request.setAttribute("__Pedido__", "");
				request.setAttribute("numeroGuia", "");
				request.setAttribute("motivo", "");
				request.setAttribute("motivoVincular", "");
				request.setAttribute("numeroGuiaAlterarStatus", "");
				request.setAttribute("novoStatusGuia", "");
				request.setAttribute("motivoAlteracao", "");
				request.setAttribute("motivoAlteracaoComarca", "");
				request.setAttribute("Id_AreaDistribuicao", "");
				request.setAttribute("Id_Comarca", "");
				request.setAttribute("numeroGuiaAlterarComarca", "");
				
				if( !guiaEmissaoNe.isConexaoSPG_OK() ) {
					redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Cancelar+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
					return;
				}
				
				break;
			}
			
			case Configuracao.Localizar : {
				
				//Default
				stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO_CONSULTAR;
				
				if( request.getParameter("numeroGuia") == null || request.getParameter("numeroGuia").isEmpty() ) {
					throw new MensagemException("Por favor, informe o número de Guia Completo!");
				}
				if( request.getParameter("motivo") == null || request.getParameter("motivo").length() == 0 ) {
					throw new MensagemException("Por favor, informe a justificativa ou motivo da desvinculação desta guia!");
				}
				
				String numeroGuiaInformado_A = request.getParameter("numeroGuia").trim().replace("/", "").replace("-", "").replace(" ", "");
				
//				guiaEmissaoDt_A.setNumeroGuiaCompleto(numeroGuiaInformado_A);
				
				GuiaEmissaoDt guiaEmissaoDtConsulta_A = null;
				GuiaEmissaoDt guiaEmissaoDtConsulta_A_SPG = null;
				List<InfoRepasseSPGDt> listaInfoRepasseSPGDt = null;
				List<LocomocaoSPGDt> listaLocomocaoMandadoSPGDt = null;
				List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
				
				if( numeroGuiaInformado_A != null && numeroGuiaInformado_A.length() > 0 ) {
					guiaEmissaoDtConsulta_A = guiaEmissaoNe.consultarGuiaTodosDados(numeroGuiaInformado_A);
					request.setAttribute("GuiaEmissaoDt_A", guiaEmissaoDtConsulta_A);
					request.setAttribute("numeroGuiaDesvincular", request.getParameter("numeroGuia"));
					request.setAttribute("motivo", request.getParameter("motivo"));
					
					guiaEmissaoDtConsulta_A_SPG = guiaEmissaoNe.consultarGuiaEmissaoSPG(numeroGuiaInformado_A);
					request.setAttribute("guiaEmissaoDtConsulta_A_SPG", guiaEmissaoDtConsulta_A_SPG);
					
					if( guiaEmissaoDtConsulta_A_SPG != null ) {
						listaInfoRepasseSPGDt = guiaEmissaoNe.consultarListaInfoRepasseByISNGuia(guiaEmissaoDtConsulta_A_SPG.getId(), guiaEmissaoDtConsulta_A_SPG.getComarcaCodigo());
						request.setAttribute("listaInfoRepasseSPGDt", listaInfoRepasseSPGDt);
						
						listaLocomocaoMandadoSPGDt = new LocomocaoNe().consultarLocomocaoMandadoSPG(numeroGuiaInformado_A);
						request.setAttribute("listaLocomocaoMandadoSPGDt", listaLocomocaoMandadoSPGDt);
						
						listaLocomocaoSPGDt = new LocomocaoNe().consultarListaLocomocaoSPG(numeroGuiaInformado_A);
						request.setAttribute("listaLocomocaoSPGDt", listaLocomocaoSPGDt);
					}
				}
				
				if( guiaEmissaoDtConsulta_A != null ) {
					stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO;
				}
				else {
					stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO_CONSULTAR;
					request.setAttribute("MensagemErro", "Guia(s) não encontrada(s). <br />Verifique o(s) número(s) e tente novamente.");
				}
				
				break;
			}
			
			case Configuracao.Salvar : {
				
				request.setAttribute("PosicaoPaginaAtual", paginaatual);
				request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
				
				request.setAttribute("tempPrograma", NOME_CONTROLE_WEB_XML);
				request.setAttribute("tempRetorno", NOME_CONTROLE_WEB_XML);
				
				switch( passoEditar ) {
					case Configuracao.Curinga6 : {
						
						if( request.getParameter("numeroGuiaDesvincular") == null || request.getParameter("numeroGuiaDesvincular").length() == 0 ) {
							throw new MensagemException("Por favor, informe o número de Guia Completo que Desejar Desvincular do Processo!");
						}
						if( request.getParameter("motivo") == null || request.getParameter("motivo").length() == 0 ) {
							throw new MensagemException("Por favor, informe a justificativa ou motivo da desvinculação desta guia!");
						}
						
						super.exibaMensagemConfirmacao(request, "Clique para confirmar a desvinculação da guia <b>" + Funcoes.FormatarNumeroSerieGuia(request.getParameter("numeroGuiaDesvincular").trim().replace("/", "").replace("-", "").replace(" ", "")) + "</b> nos processos do PJD e SPG.");
						
						request.setAttribute("numeroGuiaDesvincular", request.getParameter("numeroGuiaDesvincular"));
						request.setAttribute("motivo", request.getParameter("motivo"));
						request.setAttribute("PassoEditar", Configuracao.Curinga6);
						
						break;
					}
					
					case Configuracao.Curinga7 : {
						
						stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO_CONSULTAR;
						if( request.getParameter("numeroGuiaVincular") == null || request.getParameter("numeroGuiaVincular").length() == 0 ) {
							throw new MensagemException("Por favor, informe o número da Guia Completo que Desejar Vincular ao Processo!");
						}
						if( request.getParameter("numeroProcessoVincular") == null || request.getParameter("numeroProcessoVincular").length() == 0 ) {
							throw new MensagemException("Por favor, informe o número do Processo Desejado para Vincular a Guia!");
						}
						if( request.getParameter("motivoVincular") == null || request.getParameter("motivoVincular").length() == 0 ) {
							throw new MensagemException("Por favor, informe a justificativa ou motivo da vinculação desta guia!");
						}
						
						super.exibaMensagemConfirmacao(request, "Clique para confirmar a vinculação da guia <b>" + Funcoes.FormatarNumeroSerieGuia(request.getParameter("numeroGuiaVincular").trim().replace("/", "").replace("-", "").replace(" ", "")) + "</b> ao processo <b>" + request.getParameter("numeroProcessoVincular") + "</b>");
						
						request.setAttribute("numeroGuiaVincularConfirmar", request.getParameter("numeroGuiaVincular"));
						request.setAttribute("numeroProcessoVincularConfirmar", request.getParameter("numeroProcessoVincular"));
						request.setAttribute("motivoVincular", request.getParameter("motivoVincular"));
						request.setAttribute("PassoEditar", Configuracao.Curinga7);
						
						break;
					}
					
					case Configuracao.Curinga8 : {
						
						if( request.getParameter("numeroGuiaAlterarStatus") == null || request.getParameter("numeroGuiaAlterarStatus").length() == 0 ) {
							throw new MensagemException("Por favor, informe o número da Guia Completo que Desejar Alterar o Status!");
						}
						if( request.getParameter("novoStatusGuia") == null || request.getParameter("novoStatusGuia").length() == 0 ) {
							throw new MensagemException("Por favor, escolha o novo status para a Guia!");
						}
						if( request.getParameter("motivoAlteracao") == null || request.getParameter("motivoAlteracao").length() == 0 ) {
							throw new MensagemException("Por favor, informe a justificativa ou motivo da alteração do status da guia!");
						}
						
//						String numeroGuiaInformado = request.getParameter("numeroGuiaAlterarStatus").trim().replace("/", "").replace("-", "").replace(" ", "");
//						request.setAttribute("GuiaEmissaoDt_A", guiaEmissaoNe.consultarGuiaTodosDados(numeroGuiaInformado));
						
						super.exibaMensagemConfirmacao(request, "Clique para confirmar a alteração do Status da guia <b>" + Funcoes.FormatarNumeroSerieGuia(request.getParameter("numeroGuiaAlterarStatus").trim().replace("/", "").replace("-", "").replace(" ", "")) + "</b>.");
						
						request.setAttribute("numeroGuiaAlterarStatus", request.getParameter("numeroGuiaAlterarStatus"));
						request.setAttribute("motivoAlteracao", request.getParameter("motivoAlteracao"));
						request.setAttribute("novoStatusGuia", request.getParameter("novoStatusGuia"));
						request.setAttribute("PassoEditar", Configuracao.Curinga8);
						
						break;
					}
					
					case Configuracao.Curinga9 : {
						
						if( request.getParameter("numeroGuiaAlterarComarca") == null || request.getParameter("numeroGuiaAlterarComarca").length() == 0 ) {
							throw new MensagemException("Por favor, informe o número da Guia para Alterar a Comarca ou Área de Distribuição!");
						}
						if( request.getParameter("Id_AreaDistribuicao") == null || request.getParameter("Id_AreaDistribuicao").length() == 0 ) {
							throw new MensagemException("Por favor, informe a Área de Distribuição!");
						}
						if( request.getParameter("Id_Comarca") == null || request.getParameter("Id_Comarca").length() == 0 ) {
							throw new MensagemException("Por favor, a Comarca!");
						}
						if( request.getParameter("motivoAlteracaoComarca") == null || request.getParameter("motivoAlteracaoComarca").length() == 0 ) {
							throw new MensagemException("Por favor, informe a justificativa ou motivo da alteração da comarca/área de distribuição da guia!");
						}
						
						super.exibaMensagemConfirmacao(request, "Clique para confirmar a alteração da Comarca / Área de Distribuição da guia <b>" + Funcoes.FormatarNumeroSerieGuia(request.getParameter("numeroGuiaAlterarStatus").trim().replace("/", "").replace("-", "").replace(" ", "")) + "</b>.");
						
						request.setAttribute("numeroGuiaAlterarComarca", request.getParameter("numeroGuiaAlterarComarca"));
						request.setAttribute("Id_AreaDistribuicao", request.getParameter("Id_AreaDistribuicao"));
						request.setAttribute("Id_Comarca", request.getParameter("Id_Comarca"));
						request.setAttribute("motivoAlteracaoComarca", request.getParameter("motivoAlteracaoComarca"));
						request.setAttribute("PassoEditar", Configuracao.Curinga9);
						
						break;
					}
				}
				
				break;
			}
			
			case Configuracao.SalvarResultado : {
				
				stAcao = PAGINA_DESVINCULAR_GUIA_PROCESSO_CONSULTAR;
				
				switch( passoEditar ) {
					case Configuracao.Curinga6 : {
						
						if( request.getParameter("numeroGuiaDesvincular") != null && request.getParameter("numeroGuiaDesvincular").length() > 0 ) {
							boolean guiaDesvinculada = guiaEmissaoNe.desvincularGuiaProcessoPJD_SPG(request.getParameter("numeroGuiaDesvincular").trim().replace("/", "").replace("-", "").replace(" ", ""), UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), request.getParameter("motivo"));
							
							if( guiaDesvinculada ) {
								request.setAttribute("MensagemOk","Guia " + Funcoes.FormatarNumeroSerieGuia(request.getParameter("numeroGuiaDesvincular").trim().replace("/", "").replace("-", "").replace(" ", "")) + " Desvinculada com Sucesso!");
							}
							else {
								request.setAttribute("MensagemErro","A Guia não foi desvinculada! Pode ser que tenha repasse cadastrado. Por favor, entrar em contato com a equipe do SPG para verificar se pode desvincular.");
							}
						}
						else {
							throw new MensagemException("Erro ao tentar desvincular guia de processo. Id da guia não identificado.");
						}
						
						break;
					}
					
					case Configuracao.Curinga7 : {
						
						if( request.getParameter("numeroGuiaVincularConfirmar") != null 
							&& request.getParameter("numeroGuiaVincularConfirmar").length() > 0 
							&& request.getParameter("numeroProcessoVincularConfirmar") != null 
							&& request.getParameter("numeroProcessoVincularConfirmar").length() > 0
							&& request.getParameter("motivoVincularConfirmar") != null 
							&& request.getParameter("motivoVincularConfirmar").length() > 0) {
							
							ProcessoDt processoDt = new ProcessoNe().consultarProcessoNumeroCompletoDigitoAno(request.getParameter("numeroProcessoVincularConfirmar"));
							
							if( processoDt == null ) {
								throw new MensagemException("Processo " + request.getParameter("numeroProcessoVincularConfirmar") + " não foi encontrado. Por favor, confirme se o número está correto.");
							}
							
							guiaEmissaoNe.vinculeGuia(UsuarioSessao.getUsuarioDt(), request.getParameter("numeroGuiaVincularConfirmar"), processoDt, request.getParameter("motivoVincularConfirmar"));
							
							GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarNumeroCompleto(request.getParameter("numeroGuiaVincularConfirmar"), null, null);
							
							if( guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && processoDt.getId() != null && guiaEmissaoDt.getId_Processo().equals(processoDt.getId()) ) {
								request.setAttribute("MensagemOk","Guia " + Funcoes.FormatarNumeroSerieGuia(request.getParameter("numeroGuiaVincularConfirmar").trim().replace("/", "").replace("-", "").replace(" ", "")) + " Vinculada com Sucesso ao Processo "+ request.getParameter("numeroProcessoVincularConfirmar") +" !");
							}
							else {
								request.setAttribute("MensagemErro","A Guia não foi vinculada!");
							}
						}
						else {
							throw new MensagemException("Erro ao tentar vincular guia processo.(Admin.)");
						}
						
						break;
					}
					
					case Configuracao.Curinga8 : {
						
						if( request.getParameter("numeroGuiaAlterarStatus") != null && request.getParameter("numeroGuiaAlterarStatus").length() > 0 ) {
							boolean guiaAlterada = guiaEmissaoNe.atualizarStatusGuia(request.getParameter("numeroGuiaAlterarStatus").trim().replace("/", "").replace("-", "").replace(" ", ""), UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), request.getParameter("motivoAlteracao"), request.getParameter("novoStatusGuia"));
							
							if( guiaAlterada ) {
								request.setAttribute("MensagemOk","Status da Guia alterado!");
							}
							else {
								request.setAttribute("MensagemErro","Status da Guia NÃO alterado!");
							}
						}
						else {
							request.setAttribute("MensagemErro","Status da Guia NÃO alterado!");
						}
						
						break;
					}
					
					case Configuracao.Curinga9 : {
						
						if( request.getParameter("numeroGuiaAlterarComarca") != null && request.getParameter("numeroGuiaAlterarComarca").length() > 0 ) {
							boolean guiaAlterada = guiaEmissaoNe.atualizarComarcaAreaDistribuicao(request.getParameter("numeroGuiaAlterarComarca").trim().replace("/", "").replace("-", "").replace(" ", ""), UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), request.getParameter("Id_AreaDistribuicao"), request.getParameter("Id_Comarca"), request.getParameter("motivoAlteracaoComarca"));
							
							if( guiaAlterada ) {
								request.setAttribute("MensagemOk","Comarca / Área de Distribuição da Guia alterado!");
							}
							else {
								request.setAttribute("MensagemErro","Comarca / Área de Distribuição da Guia NÃO alterado!");
							}
						}
						else {
							request.setAttribute("MensagemErro","Comarca / Área de Distribuição da Guia NÃO alterado!");
						}
						
						break;
					}
				}
				
				break;
			}
			
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private void limparTela(HttpServletRequest request) {
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
	}

	protected String obtenhaTituloPagina() {
		return "Desvincular Guia de Processo";
	}
	
}
