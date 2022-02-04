package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.BairroGuiaLocomocaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaLocomocaoDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaLocomocaoNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class GuiaLocomocaoCt extends Controle {

	private static final long serialVersionUID = -6196706590243912965L;
	
	protected static final String PAGINA_GUIA_LOCOMOCAO 	    = "/WEB-INF/jsptjgo/GuiaLocomocao.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 	= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_LOCALIZAR 			= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	protected static final String ID_SESSAO_GUIA_EMISSAO                        = "GuiaEmissaoDt";
	protected static final String ID_SESSAO_PROCESSO_DT                         = "processoDt";
	protected static final String ID_SESSAO_COMARCA_DT                          = "ComarcaDt_GuiaLocomocaoCt";
	protected static final String ID_SESSAO_LISTA_ITENS_GUIA                    = "ListaItensGuia";
	protected static final String ID_SESSAO_LISTA_GUIA_ITEM_DT                  = "ListaGuiaItemDt";	
	protected static final String ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO       	    = "ListaBairroLocomocao";
	protected static final String ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE         = "ListaBairroLocomocaoQtde";
	protected static final String ID_REQUEST_ID_BAIRRO_LOCOMOCAO       	      	= "tempBuscaId_BairroLocomocao";
	protected static final String ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA       = "ListaLocomocaoNaoUtilizada";	
	protected static final String ID_SESSAO_TOTAL_GUIA                          = "TotalGuia";
	protected static final String ID_SESSAO_ID_MANDADO 							= "tempIdBuscaMandado";
		
	private GuiaLocomocaoNe guiaLocomocaoNe = new GuiaLocomocaoNe();
	
	protected <T> T getValorSessao(Class<T> type, HttpServletRequest request, String chaveSessao) throws Exception {
		Object objetoSessao = request.getSession().getAttribute(chaveSessao);
		if (objetoSessao != null && type.isInstance(objetoSessao)) {
			return type.cast(objetoSessao);
		} else {
			return type.newInstance();
		}
	}
	
	protected <T> List<T> getValorListaSessao(Class<T> type, HttpServletRequest request, String chaveSessao) throws Exception {
		List<T> listaRetorno = new ArrayList<T>();
		
		Object objetoSessao = request.getSession().getAttribute(chaveSessao);
		
		if (objetoSessao != null && objetoSessao instanceof ArrayList) {
			ArrayList<?> listaObj = ArrayList.class.cast(objetoSessao);
			if (listaObj != null) {
				for(Object obj : listaObj) {
					if (type.isInstance(obj)) listaRetorno.add(type.cast(obj));
				}
			}
		}
		
		return listaRetorno;		
	}
	
	@Override
	public int Permissao() {
		return GuiaLocomocaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		executarEspecialista(request, response, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao, paginaatual, posicaopaginaatual);
	}

	protected void executarEspecialista(HttpServletRequest request, HttpServletResponse response, 
			                            String id_Usuario, String ipComputadorLog, String id_UsuarioServentia, UsuarioNe UsuarioSessao,
			                            int paginaatual, String posicaopaginaatual) throws Exception, IOException, ServletException {
		String stAcao 			= null;
		int passoEditar 		= -1;		
				
		stAcao = obtenhaPaginaPrincipal();
		
		GuiaEmissaoDt guiaEmissaoDt = getValorSessao(GuiaEmissaoDt.class, request, ID_SESSAO_GUIA_EMISSAO);
		
		//Requests 
		request.setAttribute("tempPrograma" 			, obtenhaTituloPagina());
		request.setAttribute("tempRetorno" 				, obtenhaServletDeRetornoPesquisa());
		request.setAttribute("tempRetornoBuscaProcesso" , obtenhaServletBuscaProcesso());
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		if( request.getParameter("correioQuantidade") != null && request.getParameter("correioQuantidade").length() > 0 ) {
			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("correioQuantidade"));
		}
		
		if ((request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null"))) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		atualizeParametros(request, guiaEmissaoDt, paginaatual, passoEditar);
				
		//********************************************
		ComarcaDt comarcaDt = null;
		ProcessoDt processoDt = getValorSessao(ProcessoDt.class, request, ID_SESSAO_PROCESSO_DT);
		if (processoDt != null) { 
			request.setAttribute("guiaIdProcesso", processoDt.getId());
			guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
			guiaEmissaoDt.setProcessoTipoCodigo(processoDt.getProcessoTipoCodigo());
			request.getSession().setAttribute("visualizarFieldsetAdicionarLocomocao", guiaLocomocaoNe.podeEmitirLocomocao(processoDt.getId(), processoDt.getProcessoTipoCodigo(), GuiaTipoDt.ID_LOCOMOCAO));
			
			if (processoDt.getServentiaCodigo() != null && (processoDt.getComarca() == null || processoDt.getComarca().trim().length() == 0)) {
				ServentiaDt serventiaDt = guiaLocomocaoNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
				if (serventiaDt != null) {
					processoDt.setComarca(serventiaDt.getComarca());
					comarcaDt = guiaLocomocaoNe.consultarComarca(serventiaDt.getId_Comarca());
					request.getSession().setAttribute(ID_SESSAO_COMARCA_DT, comarcaDt);
				}
			} else {
				comarcaDt = getValorSessao(ComarcaDt.class, request, ID_SESSAO_COMARCA_DT);
			}
		}
		if( comarcaDt != null && comarcaDt.getId() != null ) {
			comarcaDt = guiaLocomocaoNe.consultarComarca(comarcaDt.getId());
		}
		//********************************************
		
		if (guiaEmissaoDt != null) {
			guiaEmissaoDt.setLocomocaoComplementar(exibeLocomocoesNaoUtilizadasParaEmissaoDeComplementar());
		}
		
		switch(paginaatual) {
			
			case Configuracao.Novo: {	
				
//				if( guiaLocomocaoNe.isServentiaSegundoGrau(processoDt.getId_Serventia()) ) {
//					throw new MensagemException("Processo de Segundo Grau, não pode emitir esta guia.");
//				}
				
				if(guiaLocomocaoNe.isConexaoSPG_OK()) {
					guiaEmissaoDt = new GuiaEmissaoDt();
					limpaVariaveisDaSessao(request);
					
					GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_LOCOMOCAO, processoDt.getId_ProcessoTipo());
					if (guiaModeloDt == null) {
						redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_MODELO_GUIA_LOCOMOCAO_NAO_EXISTE)));
						return;
					}	
					//Alteração para salvar locomomocões de processo do segundo Grau no ssg
					if (processoDt.isSegundoGrau()){
						guiaModeloDt.setFlagGrau("2");
					}
					
					guiaEmissaoDt.setProcessoDt(processoDt);
					guiaEmissaoDt.setLocomocaoComplementar(exibeLocomocoesNaoUtilizadasParaEmissaoDeComplementar());
					
					if (guiaEmissaoDt.isLocomocaoComplementar() && request.getParameter("Id_GuiaEmissaoPaga") != null && request.getParameter("Id_GuiaEmissaoPaga").trim().length() > 0) {
						List<LocomocaoDt> listaLocomocaoNaoLococalizadaDt = guiaLocomocaoNe.consultarLocomocaoNaoUtilizada(processoDt.getId(), request.getParameter("Id_GuiaEmissaoPaga"), true);
						request.getSession().setAttribute(ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA, listaLocomocaoNaoLococalizadaDt);
					}
					
					if( guiaEmissaoDt.isLocomocaoComplementar() ) {
						request.setAttribute("MensagemOk", "<b>Informação</b>!<br /><br /><br /> Esta Guia de Locomoção Complementar é utilizada para complementar um valor ou alterar o bairro de uma locomoção emitida.<br /><br /> Caso deseje adicionar mais locomoções, por favor, acessar a Guia de Locomoção. Ela pode ser acessada pelo menu \"Opções Processo\">\"Guias\">\"Guias da Justiça Comum\">\"Guia Locomoção\"");
					}
					atualizeMensagens(request);
				} else {
					redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG)));
					return;
				}				
				break;
			}
			
			//Excluir item de bairro da locomoção
			case Configuracao.Excluir: {				
				removaItemDaLista(request, passoEditar);
				break;
			}
			
			//Apresenta Prévia de Cálculo
			case Configuracao.Curinga6 : {
				if (apresentaPreviaDoCalculo(request, response, passoEditar, processoDt, guiaEmissaoDt)) 
					stAcao = obtenhaAcaoPreviaDeCalculo(request);
				break;
			}
			
//			//Encaminha Pagamento On-Line
//			case Configuracao.Curinga7 : {
//				if (encaminhePagamentoOnLine(request, response, passoEditar, processoDt, comarcaDt, id_Usuario)) return;
//				break;
//			}
//			
//			//Retorno do pagamento On-Line e Consulta Prévia do cálculo
//			case Configuracao.Curinga8 : {
//				break;
//			}
			
			//Impressão da guia
			case Configuracao.Imprimir : {
				if (imprimeGuia(request, response, passoEditar, processoDt, comarcaDt, id_Usuario, ipComputadorLog, id_UsuarioServentia)) return;
				break;
			}
			
			//Utilizado para localizar os bairros
			case Configuracao.Localizar : {
				
				//Somente valida se a lupa utilizada é a lupa da lcomoção normal e não a lupa de locomoção para complementar
				if( passoEditar != Configuracao.Curinga7 ) {
					if(UsuarioSessao.isCoodernadorCentralMandado() && validaIsLocomocaoSemOficial(request) ) {
						break;
					}
				}
				
				if (executeConsultaDeLista(request, response, processoDt, passoEditar, posicaopaginaatual, UsuarioSessao.isCoodernadorCentralMandado())) return;
				else stAcao = PAGINA_LOCALIZAR;
				break;
			}
			
			//Consulta de mandado para o caso de guia vinculada à mandado/oficial
			case (MandadoJudicialDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				String stNomeBusca1 = "";
				String stNomeBusca2 = "";
				if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
				if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
				String stTemp = guiaLocomocaoNe.consultarMandadosPagamentoAprovadoJson(stNomeBusca1, stNomeBusca2, processoDt.getId(), posicaopaginaatual);
				enviarJSON(response, stTemp);
				return;
			}
			
			default : {
				//Busca Bairro Locomoção
				validaEAdicionaBairroNaLista(request, ID_REQUEST_ID_BAIRRO_LOCOMOCAO, guiaEmissaoDt);
								
				//Busca Locomoção não Utilizada
				validaEAdicionaLocomocaoNaoUtilizadaNaLista(request);
				
				if( UsuarioSessao.isCoodernadorCentralMandado() ) {
					request.getSession().removeAttribute(ID_SESSAO_ID_MANDADO);
				}
								
				break;
			}				
		}
		
		request.getSession().setAttribute(ID_SESSAO_GUIA_EMISSAO, guiaEmissaoDt);
		
		request.setAttribute("exibeLocomocoesNaoUtilizadas", exibeLocomocoesNaoUtilizadasParaEmissaoDeComplementar());
		request.setAttribute("exibeOficialCompanheiro", new Boolean(true)); //Conforme informado pelo Marcelo da Corregedoria, só é realizado o cálculo em dobro automático na guia inicial.		
			
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected String obtenhaPaginaPrincipal() {
		return PAGINA_GUIA_LOCOMOCAO;
	}
	
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaLocomocao";
	}
	
	protected boolean exibeLocomocoesNaoUtilizadasParaEmissaoDeComplementar() {
		return false;
	}
	
	protected String obtenhaTituloPagina() {
		return "Guia Locomoção";
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
	
	protected void atualizeParametros(HttpServletRequest request, GuiaEmissaoDt guiaEmissaoDt, int paginaatual, int passoEditar) throws Exception {
		if (request.getParameter("finalidade") != null && request.getParameter("finalidade").toString().length() > 0) 
			guiaEmissaoDt.setFinalidade(request.getParameter("finalidade").toString());
		
		if (request.getParameter("quantidadeAcrescimoPessoa") != null && request.getParameter("quantidadeAcrescimoPessoa").toString().length() > 0) 
			guiaEmissaoDt.setQuantidadeAcrescimo(request.getParameter("quantidadeAcrescimoPessoa").toString());
		
		guiaEmissaoDt.setPenhora(request.getParameter("penhora"));		
		guiaEmissaoDt.setIntimacao(request.getParameter("intimacao"));		
		guiaEmissaoDt.setOficialCompanheiro(request.getParameter("oficialCompanheiro"));		
		guiaEmissaoDt.setForaHorarioNormal(request.getParameter("foraHorarioNormal"));
		guiaEmissaoDt.setCitacaoHoraCerta(request.getParameter("citacaoHoraCerta"));
		
		if (paginaatual == Configuracao.Curinga6 && passoEditar == Configuracao.Curinga8) {
			if (request.getParameter("penhora") == null) guiaEmissaoDt.setPenhora(false);
			if (request.getParameter("intimacao") == null) guiaEmissaoDt.setIntimacao(false);
			ajusteQuantidadeDeLocomocao(request);
		}
	}
	
	protected void limpaVariaveisDaSessao(HttpServletRequest request) {		
		request.getSession().removeAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO);
		request.getSession().removeAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE);
		request.getSession().removeAttribute(ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA);
		request.getSession().removeAttribute(ID_SESSAO_LISTA_ITENS_GUIA);
		request.getSession().removeAttribute(ID_SESSAO_LISTA_GUIA_ITEM_DT);		
		request.getSession().removeAttribute(ID_SESSAO_GUIA_EMISSAO);
		request.getSession().removeAttribute(ID_SESSAO_TOTAL_GUIA);
		request.getSession().removeAttribute(ID_SESSAO_ID_MANDADO);
	}
	
	protected void validaEAdicionaBairroNaLista(HttpServletRequest request, String idRequestBairro, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		String stId = request.getParameter(idRequestBairro);
		if( stId != null ) {
			BairroDt bairroDt = guiaLocomocaoNe.consultarBairroId(stId);
			
			if (bairroDt.getZona() == null || bairroDt.getZona().trim().length() == 0) {
				super.exibaMensagemInconsistenciaErro(request, Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
				return;
			} 
			
			List<BairroGuiaLocomocaoDt> listaBairroDt = getValorListaSessao(BairroGuiaLocomocaoDt.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO);
			
			BairroGuiaLocomocaoDt bairroLocomocao = new BairroGuiaLocomocaoDt();
			bairroLocomocao.setBairroDt(bairroDt);
			bairroLocomocao.setQuantidade(1);
			bairroLocomocao.setId_Finalidade(guiaEmissaoDt.getId_Finalidade());
			bairroLocomocao.setFinalidade(guiaEmissaoDt.getFinalidade());
			bairroLocomocao.setOficialCompanheiro(guiaEmissaoDt.isOficialCompanheiro());
			bairroLocomocao.setPenhora(guiaEmissaoDt.isPenhora());
			bairroLocomocao.setIntimacao(guiaEmissaoDt.isIntimacao());
			
			if( request.getSession().getAttribute(ID_SESSAO_ID_MANDADO) != null && !request.getSession().getAttribute(ID_SESSAO_ID_MANDADO).toString().isEmpty() ) {
				String idMandado = request.getSession().getAttribute(ID_SESSAO_ID_MANDADO).toString();
				
				bairroLocomocao.setIdMandadoJudicial(idMandado);
				bairroLocomocao.setOficialSPGDt_Principal(guiaLocomocaoNe.consultaOficialJusticaVinculadoMandado(true, idMandado));
				bairroLocomocao.setOficialSPGDt_Companheiro(guiaLocomocaoNe.consultaOficialJusticaVinculadoMandado(false, idMandado));
			}
			
			listaBairroDt.add(bairroLocomocao);
			
			request.getSession().setAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO, listaBairroDt);
			
			List<Integer> listaBairroDtQuantidade = getValorListaSessao(Integer.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE);			
			listaBairroDtQuantidade.add(1);
			request.getSession().setAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE, listaBairroDtQuantidade);
		}
	}
	
	protected void validaEAdicionaLocomocaoNaoUtilizadaNaLista(HttpServletRequest request) throws Exception {
		String stId = request.getParameter("tempBuscaId_LocomocaoNaoUtilizada");
		if( stId != null ) {			
			LocomocaoDt locomocaoDt = guiaLocomocaoNe.consultarLocomocaoId(stId);
			
			List<LocomocaoDt> listaLocomocaoDt = getValorListaSessao(LocomocaoDt.class, request, ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA);
			
			if (listaLocomocaoDt.contains(locomocaoDt)) {
				super.exibaMensagemInconsistenciaErro(request, "Locomoção já adicionada na lista");
				return;
			}
					
			listaLocomocaoDt.add(locomocaoDt);			
			
			request.getSession().setAttribute(ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA, listaLocomocaoDt);
		}
	}
	
	protected boolean validaIsLocomocaoSemOficial(HttpServletRequest request) throws Exception {
		if( request.getSession().getAttribute(ID_SESSAO_ID_MANDADO) != null && !request.getSession().getAttribute(ID_SESSAO_ID_MANDADO).toString().isEmpty() ) {
			return false;
		}
		
		String idMandado = request.getParameter("idMandado");
		
		if( idMandado == null || (idMandado != null && (idMandado.equals("null") || idMandado.isEmpty())) ) {
			request.setAttribute("MensagemErro", "Por favor, informe antes o Oficial de Justiça que será vinculado nesta Locomoção");
			
			return true;
		}
		
		return false;
	}
	
	protected void removaItemDaLista(HttpServletRequest request, int passoEditar) throws Exception {
		switch(passoEditar) {
		
			//Remover locomoção
			case Configuracao.Curinga6 : {
				removaBairroDaLista(request);						
				break;
			}
			
			//Remover locomoção não utilizada
			case Configuracao.Curinga7 : {
				removaLocomocaoNaoUtilizadaDaLista(request);						
				break;
			}
		}
	}	
	
	private void removaBairroDaLista(HttpServletRequest request) throws Exception {
		int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
		
		if (posicaoListaBairroExcluir != -1) {
			
			List<BairroGuiaLocomocaoDt> listaBairroDt =  getValorListaSessao(BairroGuiaLocomocaoDt.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO);
			
			if( listaBairroDt.size() > 0 ) {
				listaBairroDt.remove(posicaoListaBairroExcluir);
				request.getSession().setAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO, listaBairroDt);
			}
			
			List<Integer> listaBairroDtQuantidade = getValorListaSessao(Integer.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE);
			if (listaBairroDtQuantidade.size() > 0) {
				listaBairroDtQuantidade.remove(posicaoListaBairroExcluir);
				request.getSession().setAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE, listaBairroDtQuantidade);
			}	
		}
	}	
	
	private void removaLocomocaoNaoUtilizadaDaLista(HttpServletRequest request) throws Exception {
		int posicaoListaLocomocaoNaoUtilizadaExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaLocomocaoNaoUtilizadaExcluir"));
		
		if (posicaoListaLocomocaoNaoUtilizadaExcluir != -1) {
			
			List<LocomocaoDt> listaLocomocaoNaoUtilizadaDt =  getValorListaSessao(LocomocaoDt.class, request, ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA);
			
			if( listaLocomocaoNaoUtilizadaDt.size() > 0 ) {
				listaLocomocaoNaoUtilizadaDt.remove(posicaoListaLocomocaoNaoUtilizadaExcluir);
				request.getSession().setAttribute(ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA, listaLocomocaoNaoUtilizadaDt);
			}			
		}
	}
	
	protected boolean executeConsultaDeLista(HttpServletRequest request, HttpServletResponse response, ProcessoDt processoDt, int passoEditar, String posicaopaginaatual, boolean isUsuarioCoordenadorCentralMandado) throws Exception {
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		String idMandadoBusca = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		if(request.getParameter("idMandado") != null) idMandadoBusca = request.getParameter("idMandado");
		
		return executeConsultaDeBairroLocomocaoNaoUtilizada(request, response, processoDt, posicaopaginaatual, passoEditar,  stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, idMandadoBusca, isUsuarioCoordenadorCentralMandado);	
	}
	
	protected boolean executeConsultaDeBairroLocomocaoNaoUtilizada(HttpServletRequest request, HttpServletResponse response, ProcessoDt processoDt, String posicaopaginaatual, int passoEditar, String tempNomeBusca, String tempCidade, String tempUf, String tempZona, String idMandadoBusca, boolean isUsuarioCoordenadorCentralMandado) throws Exception {
		if(request.getSession().getAttribute("PassoEditar") != null && passoEditar == Configuracao.Editar) {
			passoEditar = Funcoes.StringToInt(request.getSession().getAttribute("PassoEditar").toString());
		}
		if (request.getParameter("Passo")==null) {
			prepareConsultaDeBairroLocomocaoNaoUtilizada(request, obtenhaCampoId(request, passoEditar), idMandadoBusca, passoEditar);
			return false;
		} else {			
			if (passoEditar == Configuracao.Curinga7) {
				
				boolean validaOficialVinculadoLocomocao = true;
				if( isUsuarioCoordenadorCentralMandado ) {
					validaOficialVinculadoLocomocao = false;
				}
				
				executeConsultaDeLocomocoesNaoUtilizadas(response, processoDt, tempNomeBusca, tempCidade, tempUf, tempZona, posicaopaginaatual, validaOficialVinculadoLocomocao);
			} else {
				executeConsultaDeBairro(response, tempNomeBusca, tempCidade, tempUf, tempZona, posicaopaginaatual);
			}
			return true;
		}
	}
	
	protected String obtenhaCampoId(HttpServletRequest request, int passoEditar) throws MensagemException {
		String campoId = "";
		
		switch(passoEditar) {
			//Busca de Bairro Locomoção
			case ( Configuracao.Curinga6 ) : {
				campoId = ID_REQUEST_ID_BAIRRO_LOCOMOCAO;
				break;
			}
			
			//Busca de Locomoção não utilizada
			case ( Configuracao.Curinga7 ) : {
				campoId = "tempBuscaId_LocomocaoNaoUtilizada";
				break;
			}
			
			default:
				throw new MensagemException("PassoEditar não esperado para a consulta de bairros.");
		}
		
		return campoId;
	}
	
	protected void prepareConsultaDeBairroLocomocaoNaoUtilizada(HttpServletRequest request, String campoId, String campoIdMandado, int passoEditar) {
		String[] lisNomeBusca = {"Bairro","Cidade","UF"};
		String[] lisDescricao = {"Bairro","Cidade","UF"};
		request.setAttribute("tempBuscaId",campoId);
		request.setAttribute("tempBuscaDescricao","Bairro");
		request.setAttribute("tempBuscaPrograma","Bairro");
		request.setAttribute("tempRetorno",obtenhaServletDeRetornoPesquisa());		
		request.setAttribute("tempDescricaoId","Id");
		request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
		request.setAttribute("PaginaAtual", (Configuracao.Localizar));
		request.setAttribute("PosicaoPaginaAtual", "0");
		request.setAttribute("QuantidadePaginas", "0");
		request.setAttribute("lisNomeBusca", lisNomeBusca);
		request.setAttribute("lisDescricao", lisDescricao);
		request.setAttribute("idMandado", campoIdMandado);
		request.getSession().setAttribute(ID_SESSAO_ID_MANDADO, campoIdMandado);
		request.getSession().setAttribute("PassoEditar", String.valueOf(passoEditar));
	}
	
	protected void executeConsultaDeBairro(HttpServletResponse response, String tempNomeBusca, String tempCidade, String tempUf, String tempZona, String posicaopaginaatual) throws Exception {
		String stTemp="";
		stTemp = guiaLocomocaoNe.consultarDescricaoBairroJSON(tempNomeBusca, tempCidade, tempUf, posicaopaginaatual);
					
		enviarJSON(response, stTemp);
	}
	
	protected void executeConsultaDeLocomocoesNaoUtilizadas(HttpServletResponse response, ProcessoDt processoDt, String tempNomeBusca, String tempCidade, String tempUf, String tempZona, String posicaopaginaatual, boolean validaOficialVinculadoLocomocao) throws Exception {
		String stTemp="";
		stTemp = guiaLocomocaoNe.consultarDescricaoLocomocoesNaoUtilizadasJSON(processoDt.getId(), tempNomeBusca, tempCidade, tempUf, tempZona, posicaopaginaatual, validaOficialVinculadoLocomocao);
					
		enviarJSON(response, stTemp);
	}
	
	protected boolean imprimeGuia(HttpServletRequest request, HttpServletResponse response, 
			                    int passoEditar, ProcessoDt processoDt, 
			                    ComarcaDt comarcaDt, String Id_Usuario,
			                    String ipComputadorLog, String id_UsuarioServentia) throws Exception {
		
		// Valida o processo da sua aba
		if( request.getParameter("guiaIdProcesso") != null && 
			!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
			
			redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO)));
			return false;
		}
		
		GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute(ID_SESSAO_GUIA_EMISSAO);
		if( guiaEmissaoDt == null ) guiaEmissaoDt = new GuiaEmissaoDt();
		
		// Obtém o próximo número de Guia
		if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
			guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
			guiaEmissaoDt.setNumeroGuiaCompleto( guiaLocomocaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
		}
		guiaEmissaoDt.setListaGuiaItemDt(getValorListaSessao(GuiaItemDt.class, request, ID_SESSAO_LISTA_GUIA_ITEM_DT));
		guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
		guiaEmissaoDt.setServentia(processoDt.getServentia());
		guiaEmissaoDt.setComarca(processoDt.getComarca());
		if( comarcaDt != null && comarcaDt.getComarcaCodigo() != null ) {
			guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
		}
		guiaEmissaoDt.setValorAcao(processoDt.getValor());
		guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
		guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
		
		guiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
		guiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
		guiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
		guiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
		
		guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
		guiaEmissaoDt.setId_Usuario(Id_Usuario);
		guiaEmissaoDt.setIpComputadorLog(ipComputadorLog);
				
		if (guiaEmissaoDt.isLocomocaoComplementar()) {
			guiaEmissaoDt.setListaLocomocaoNaoUtilizadaDt(getValorListaSessao(LocomocaoDt.class, request, ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA));
			
			if (guiaLocomocaoNe.getGuiaCalculoNe().getTotalGuia() <= 0) {
				guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.PAGO);
				guiaEmissaoDt.setDataRecebimento(new TJDataHora().getDataFormatadaddMMyyyy());
			}
		}

		guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
		
		//Salvar GuiaEmissao
		guiaLocomocaoNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, id_UsuarioServentia);
		
		request.getSession().setAttribute(ID_SESSAO_GUIA_EMISSAO, guiaEmissaoDt);
		
		switch(passoEditar) {
		
			case Configuracao.Imprimir: {
				//Geração da guia PDF
				byte[] byTemp = guiaLocomocaoNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute(ID_SESSAO_TOTAL_GUIA).toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_LOCOMOCAO, "LOCOMOÇÃO");				
				String nome="GuiaLocomocao_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;				
				enviarPDF(response, byTemp, nome);
				return true;
			}
			
			case Configuracao.Salvar : {
				redireciona(response, obtenhaAcaoMensagemOk(processoDt, "Guia Emitida com Sucesso!"));
				return false;
			}
		}
		
		return false;
	}
	
//	protected boolean encaminhePagamentoOnLine(HttpServletRequest request, HttpServletResponse response, 
//			                                 int passoEditar, ProcessoDt processoDt,
//			                                 ComarcaDt comarcaDt, String id_Usuario) throws Exception {
//		GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute(ID_SESSAO_GUIA_EMISSAO);
//		if( guiaEmissaoDt == null ) guiaEmissaoDt = new GuiaEmissaoDt();
//		
//		// Obtém o próximo número de Guia
//		if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//			guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//			guiaEmissaoDt.setNumeroGuiaCompleto( guiaLocomocaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
//		}
//		guiaEmissaoDt.setListaGuiaItemDt(getValorListaSessao(GuiaItemDt.class, request, ID_SESSAO_LISTA_GUIA_ITEM_DT));
//		guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
//		guiaEmissaoDt.setServentia(processoDt.getServentia());
//		guiaEmissaoDt.setComarca(processoDt.getComarca());
//		guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
//		guiaEmissaoDt.setValorAcao(processoDt.getValor());
//		guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
//		guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
//		
//		guiaEmissaoDt.setListaRequerentes(processoDt.getListaPromoventes());
//		guiaEmissaoDt.setListaRequeridos(processoDt.getListaPromovidos());
//		guiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
//		guiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
//		
//		guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
//		guiaEmissaoDt.setId_Usuario(id_Usuario);
//		
//		request.getSession().setAttribute(ID_SESSAO_GUIA_EMISSAO, guiaEmissaoDt);
//		
//		switch(passoEditar) {
//			
//			// Banco do Brasil
//			case Configuracao.Curinga6 : {
//				redireciona(response, "PagamentoOnLine?PaginaAtual=" + Configuracao.Curinga6 + "&PassoEditar=" + Configuracao.Curinga6 + "&tempRetornoBuscaProcesso=" + obtenhaServletBuscaProcesso() + "&sv=" + PagamentoOnLineNe.PERMITE_SALVAR_SIM);
//				return true;
//			}
//			
//			// Banco Itaú
//			case Configuracao.Curinga7 : {
//				return true;
//			}
//			
//			// Banco Caixa Econômica
//			case Configuracao.Curinga8 : {
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
	protected boolean apresentaPreviaDoCalculo(HttpServletRequest request, HttpServletResponse response, int passoEditar, ProcessoDt processoDt, GuiaEmissaoDt guiaEmissaoDt) throws Exception {
		List<GuiaCustaModeloDt> listaItensGuia = getValorListaSessao(GuiaCustaModeloDt.class, request, ID_SESSAO_LISTA_ITENS_GUIA);
		List<BairroGuiaLocomocaoDt> listaBairroLocomocaoDt = getValorListaSessao(BairroGuiaLocomocaoDt.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO);
		List<LocomocaoDt> listaLocomocaoNaoUtilizadaDt = getValorListaSessao(LocomocaoDt.class, request, ID_SESSAO_LISTA_LOCOMOCAO_NAO_UTILIZADA);
		List<Integer> listaBairroIntimacaoDtQuantidade = getValorListaSessao(Integer.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE);
		
		if (guiaEmissaoDt.isLocomocaoComplementar() && (listaLocomocaoNaoUtilizadaDt == null || listaLocomocaoNaoUtilizadaDt.size() == 0)) {
			super.exibaMensagemInconsistenciaErro(request, Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_LOCOMOCAO_COMPLEMENTAR_SEM_ITENS));			
		} else if (listaBairroLocomocaoDt == null || listaBairroLocomocaoDt.size() == 0) {
			super.exibaMensagemInconsistenciaErro(request, Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_LOCOMOCAO_SEM_BAIRROS));			
		} else {
			switch(passoEditar) {		
			//Apresentar Mensagem
			case Configuracao.Mensagem : {
				super.exibaMensagemInconsistenciaErro(request, Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE));
				//Atenção!
				//Não tem BREAK pq precisa mostrar a tela novamente de Prévia
			}
			
			case Configuracao.Curinga8: {		
				//Valida o processo da sua aba
				if( request.getParameter("guiaIdProcesso") != null && 
					!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
					
					redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO)));
					return false;
				}
				
				//Consulta lista de itens da guia
				listaItensGuia = guiaLocomocaoNe.consultarItensGuia(null, guiaEmissaoDt, GuiaTipoDt.ID_LOCOMOCAO, processoDt.getId_ProcessoTipo());
				if( listaItensGuia == null ) {
					GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_LOCOMOCAO, processoDt.getId_ProcessoTipo());
					guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
					//Alteração para salvar locomomocões de processo do segundo Grau no ssg
					if (processoDt.isSegundoGrau()){
						guiaModeloDt.setFlagGrau("2");
					}
				}						
				if( listaItensGuia == null ) listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
				
				//Despesa Postal
				if( guiaEmissaoDt.getCorreioQuantidade() != null && guiaEmissaoDt.getCorreioQuantidade().length() > 0 && !guiaEmissaoDt.getCorreioQuantidade().equals("0")) {
					listaItensGuia.add( guiaLocomocaoNe.adicionarItem(CustaDt.DESPESA_POSTAL) );
				}
				
				//Locomoção Intimação / Locomoção com Conta Vinculada
				List<GuiaItemDt> listaGuiaItemLocomocaoDt = new ArrayList<GuiaItemDt>();
				preparaItensDeLocomocao(processoDt, listaBairroLocomocaoDt, listaBairroIntimacaoDtQuantidade, listaGuiaItemLocomocaoDt, guiaEmissaoDt, new ServentiaNe().isServentiaSegundoGrau(processoDt.getId_Serventia()));
				
				//Obtem o id_GuiaModelo
				if (listaItensGuia != null && listaItensGuia.size() > 0) {
					GuiaModeloDt guiaModeloDt = listaItensGuia.get(0).getGuiaModeloDt();
					if (guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null)
						guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
				}
				
				//Validações das locomoções
				String mensagem = guiaLocomocaoNe.valideLocomocaoFinalidadeEModelo(guiaEmissaoDt);
				
				if (mensagem.trim().length() > 0) {
					super.exibaMensagemInconsistenciaErro(request, mensagem);
				} else {							
					guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
					
					List<GuiaItemDt> listaGuiaItemDt = guiaLocomocaoNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, guiaEmissaoDt.getValoresReferenciaCalculoLocomocao(), null, (guiaEmissaoDt.isCitacaoHoraCerta()?GuiaEmissaoDt.VALOR_SIM:GuiaEmissaoDt.VALOR_NAO));
					if (listaGuiaItemLocomocaoDt.size() > 0) listaGuiaItemDt.addAll(listaGuiaItemLocomocaoDt);
					
					guiaLocomocaoNe.recalcularTotalGuia(listaGuiaItemDt);
					
					mensagem = guiaLocomocaoNe.valideCalculoItensGuia(listaGuiaItemDt);
					
					if (mensagem.trim().length() == 0 && guiaEmissaoDt.isLocomocaoComplementar()) {
						guiaEmissaoDt.ordeneListaGuiaItemDtCodigoArrecadacao(listaGuiaItemDt);
						mensagem = guiaLocomocaoNe.recalcularItensGuiaLocomocaoComplementar(listaGuiaItemDt, listaLocomocaoNaoUtilizadaDt);						
					}
					
					mensagem = guiaLocomocaoNe.validarLocomocoesIguaisValorZerado(listaGuiaItemDt, listaBairroLocomocaoDt);
					
					guiaEmissaoDt.ordeneListaGuiaItemDt(listaGuiaItemDt);
					
					if(mensagem.trim().length() > 0) {
						super.exibaMensagemInconsistenciaErro(request, mensagem);
					} else {
						
						request.setAttribute(ID_SESSAO_LISTA_GUIA_ITEM_DT, listaGuiaItemDt);
						request.setAttribute(ID_SESSAO_TOTAL_GUIA, guiaLocomocaoNe.getGuiaCalculoNe().getTotalGuia() );
						
						request.getSession().setAttribute(ID_SESSAO_LISTA_GUIA_ITEM_DT 	, listaGuiaItemDt);
						request.getSession().setAttribute(ID_SESSAO_TOTAL_GUIA 			, Funcoes.FormatarDecimal( guiaLocomocaoNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
						request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaLocomocaoNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
						request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
						request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
						
						ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
						processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
						processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
						processoCadastroDt.setValor(processoDt.getValor());
						processoCadastroDt.setProcessoTipo(processoDt.getProcessoTipo());
						
						guiaEmissaoDt.setValorAcao(processoCadastroDt.getValor());
						if (guiaEmissaoDt.getDataVencimento() == null || (guiaEmissaoDt.getDataVencimento() != null && guiaEmissaoDt.getDataVencimento().length() == 0))
							guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
						
						request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
						request.getSession().setAttribute(ID_SESSAO_GUIA_EMISSAO, guiaEmissaoDt);
						
						
						return true;
					}
				}
				
				break;
			}
		}
		}		
		
		return false;
	}
	
	private void ajusteQuantidadeDeLocomocao(HttpServletRequest request) throws Exception {
		List<Integer> listaBairroDtQuantidade = new ArrayList<Integer>();
		List<BairroGuiaLocomocaoDt> listaBairroLocomocaoDt = getValorListaSessao(BairroGuiaLocomocaoDt.class, request, ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO);
		
		if (listaBairroLocomocaoDt != null && listaBairroLocomocaoDt.size() > 0) {
			for(int i = 0; i < listaBairroLocomocaoDt.size(); i++) {
				int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocao"+i), 1);				
				listaBairroDtQuantidade.add(quantidadeLocomocao);				
			}
		}
		
		request.getSession().setAttribute(ID_SESSAO_LISTA_BAIRRO_LOCOMOCAO_QTDE, listaBairroDtQuantidade);
	}
	
	private void preparaItensDeLocomocao(ProcessoDt processoDt, List<BairroGuiaLocomocaoDt> listaBairroIntimacaoDt, List<Integer> listaBairroIntimacaoDtQuantidade, List<GuiaItemDt> listaGuiaItemLocomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		if( listaBairroIntimacaoDt != null && listaBairroIntimacaoDt.size() > 0 ) {
			for(int i = 0; i < listaBairroIntimacaoDt.size(); i++) {
				BairroGuiaLocomocaoDt bairroLocomocao = listaBairroIntimacaoDt.get(i);	
				int quantidade = 1;				
				if (listaBairroIntimacaoDtQuantidade != null && listaBairroIntimacaoDt.size() == listaBairroIntimacaoDt.size() && listaBairroIntimacaoDtQuantidade.get(i) > 1) {
					quantidade = listaBairroIntimacaoDtQuantidade.get(i);
				}
				for(int j = 0; j < quantidade; j++) {
					guiaLocomocaoNe.calcularGuiaLocomocaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);
				}				
			}										
		}		
	}
	
	protected void atualizeMensagens(HttpServletRequest request) {
		if (request.getParameter("MensagemErro") != null && 
		    !request.getParameter("MensagemErro").toString().equalsIgnoreCase("null") && 
		    request.getParameter("MensagemErro").toString().trim().length() > 0) {
			request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		}

		if (request.getParameter("MensagemOk") != null && 
		    !request.getParameter("MensagemOk").toString().equalsIgnoreCase("null") && 
		    request.getParameter("MensagemOk").toString().trim().length() > 0) {
			request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		}	
	}
	
	protected String obtenhaAcaoPreviaDeCalculo(HttpServletRequest request) {
		// Parâmetro request utilizado na subclasse GuiaInicialPublicaCt.java
		return PAGINA_GUIA_PREVIA_CALCULO;
	}
}
