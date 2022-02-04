package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaInicialComplementarDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.GerarBoletoNe;
import br.gov.go.tj.projudi.ne.GuiaItemNe;
import br.gov.go.tj.projudi.ne.LocomocaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.boletos.BoletoDt;
import br.gov.go.tj.projudi.ne.boletos.PagadorBoleto;
import br.gov.go.tj.projudi.ne.boletos.SituacaoBoleto;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GerarBoletoCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8958564594467473800L;
	
	private static final String PAGINA_GUIA_DADOS_PAGADOR 	        = "/WEB-INF/jsptjgo/GerarBoleto.jsp";
	private static final String PAGINA_CONSULTAR_GUIA 			 	= "/WEB-INF/jsptjgo/GerarBoletoConsultar.jsp";
	private static final String PAGINA_BOLETO_PDF	 			 	= "/WEB-INF/jsptjgo/GerarBoletoPdf.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		    = "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	private static final String NOME_CONTROLE_WEB_XML = "GerarBoleto";
	
	private static final int CONSULTA_GERAR_BOLETO = 1;
	private static final int CONSULTA_ITENS_GUIA = 2;
	
	private GerarBoletoNe gerarBoletoNe = new GerarBoletoNe();
	
	@Override
	public int Permissao() {
		return GuiaInicialComplementarDt.CodigoPermissao;
	}
	
	@Override
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
	protected String obtenhaNomeControleWebXml() {
		return NOME_CONTROLE_WEB_XML;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		configure(request, response);
		String stAcao 		= null;
		int passoEditar 	= -1;
		int passoConsultar  = -1;
						
		stAcao = PAGINA_GUIA_DADOS_PAGADOR;
		
		//********************************************
		//Variáveis objetos
		BoletoDt guiaEmissaoBoletoDt = null;
		GuiaEmissaoDt guiaEmissaoDtBase = null;
		
		//********************************************
		//Variáveis de sessão
		guiaEmissaoBoletoDt = (BoletoDt) request.getSession().getAttribute("GuiaEmissaoBoletoDt");
		if( guiaEmissaoBoletoDt == null ) {
			guiaEmissaoBoletoDt = new BoletoDt();
		}
		
		guiaEmissaoDtBase = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDtBase");
		if( guiaEmissaoDtBase == null ) {
			guiaEmissaoDtBase = new GuiaEmissaoDt();
		}	
		
		if( (request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null")) ) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		if( (request.getParameter("PassoConsultar") != null) && !(request.getParameter("PassoConsultar").equals("null")) ) {
			passoConsultar = Funcoes.StringToInt(request.getParameter("PassoConsultar"));
		}
		
		if( request.getParameter("numeroGuiaConsulta") != null && request.getParameter("numeroGuiaConsulta").length() > 0 ) {
			guiaEmissaoDtBase.setNumeroGuiaCompleto(request.getParameter("numeroGuiaConsulta"));
		}
		
		if (super.getAtributeParameter(request, "tipoPessoa") != null && (super.getAtributeParameter(request, "tipoPessoa").equalsIgnoreCase(PagadorBoleto.TIPO_PESSOA_FISICA) || super.getAtributeParameter(request, "tipoPessoa").equalsIgnoreCase(PagadorBoleto.TIPO_PESSOA_JURIDICA))){		
			guiaEmissaoBoletoDt.getPagador().setTipoPessoa(super.getAtributeParameter(request, "tipoPessoa"));			
		}
		
		if (super.getAtributeParameter(request, "Nome") != null && !super.getAtributeParameter(request, "Nome").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.getPagador().setNome(super.getAtributeParameter(request, "Nome"));			
		}
		
		if (super.getAtributeParameter(request, "Cpf") != null && !super.getAtributeParameter(request, "Cpf").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.getPagador().setCpf(super.getAtributeParameter(request, "Cpf"));			
		}
		
		if (super.getAtributeParameter(request, "RazaoSocial") != null && !super.getAtributeParameter(request, "RazaoSocial").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.getPagador().setRazaoSocial(super.getAtributeParameter(request, "RazaoSocial"));			
		}
		
		if (super.getAtributeParameter(request, "Cnpj") != null && !super.getAtributeParameter(request, "Cnpj").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.getPagador().setCnpj(super.getAtributeParameter(request, "Cnpj"));			
		}
		
		if (super.getAtributeParameter(request, "observacao1") != null && !super.getAtributeParameter(request, "observacao1").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.setObservacao1(super.getAtributeParameter(request, "observacao1"));			
		}
		
		if (super.getAtributeParameter(request, "observacao2") != null && !super.getAtributeParameter(request, "observacao2").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.setObservacao2(super.getAtributeParameter(request, "observacao2"));			
		}
		
		if (super.getAtributeParameter(request, "observacao3") != null && !super.getAtributeParameter(request, "observacao3").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.setObservacao3(super.getAtributeParameter(request, "observacao3"));			
		}
		
		if (super.getAtributeParameter(request, "observacao4") != null && !super.getAtributeParameter(request, "observacao4").equalsIgnoreCase("null")){		
			guiaEmissaoBoletoDt.setObservacao4(super.getAtributeParameter(request, "observacao4"));			
		}
		
		request.setAttribute("exibeOficialCompanheiro", new Boolean(true)); //Conforme informado pelo Marcelo da Corregedoria, só é realizado o cálculo em dobro automático na guia inicial.
		request.setAttribute("PassoConsultar", passoConsultar);
		
		//********************************************
		//Requests 
		request.setAttribute("tempPrograma" 			, obtenhaTituloPagina(passoConsultar));
		request.setAttribute("tempRetorno" 				, obtenhaNomeControleWebXml());
		request.setAttribute("PaginaAtual" 				, paginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		
		switch(paginaatual) {
		
			case Configuracao.Novo: {				
				stAcao = executeAcaoInicial(request, response);				
				break;
			}
			
			case Configuracao.Cancelar : {
				stAcao = PAGINA_CONSULTAR_GUIA;
				gerarBoletoNe.limparGuiaSessao(request);
				
				guiaEmissaoBoletoDt = new BoletoDt();
				guiaEmissaoDtBase = new GuiaEmissaoDt();
				
				guiaEmissaoDtBase.setNumeroGuiaCompleto("");
				
				request.getSession().setAttribute("GuiaEmissaoBoletoDt", guiaEmissaoBoletoDt);
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
				if(guiaEmissaoDtBase.getNumeroGuiaCompleto() == null || guiaEmissaoDtBase.getNumeroGuiaCompleto().isEmpty() ) {
					throw new MensagemException("Por favor, informe o número da Guia Completo!");
				}
				
				String numeroGuiaInformado = guiaEmissaoDtBase.getNumeroGuiaCompleto().trim();
				
				numeroGuiaInformado = numeroGuiaInformado.trim().replace("/", "").replace("-", "").replace(" ", "");
				
				GuiaEmissaoDt guiaEmissaoDtBaseConversao = gerarBoletoNe.consultarGuiaEmissaoParaConversao(numeroGuiaInformado);
				
				if( guiaEmissaoDtBaseConversao != null ) {
					/*
					 * Ocorrencia 2021/2402: Retirado restrição de emissão de boleto para guias enviadas para o CADIN
					 * 
					if (guiaEmissaoDtBaseConversao.isGuiaEnviadaCadin()) {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Cancelar+"&MensagemErro=Guia "+ Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtBaseConversao.getNumeroGuiaCompleto()) +" encontrada, porém esta não pode ser utilizada para geração de boleto, pois o débito já foi enviado ao <b>CADIN/SEFAZ</b>! <br /> Para mais informações: <b>(62) 3213-1581</b> TELEJUDICIÁRIO!");
					} else
					*/
					
						/*
						
						Ocorrencia 2020/5832 - Retirando a validação a pedido do Marcelo Tiago e Gustavo(trello):
						O sistema estava emitindo uma msg informando que a guia não podia emitir boleto pq o processo dela já tinha outra guia cadastrada no CADIN.
						Existe duas validações: Se a guia atual está no CADIN e esta outra validacao se o processo possui alguma guia no CADIN.
						Será retirado esta segunda validação abaixo.
						
						if (guiaEmissaoDtBaseConversao.isProcessoPossuiGuiaEnviadaCadin() && guiaEmissaoDtBaseConversao.isGuiaBloqueadaParaEmissaoDeGuiaEBoleto()) {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Cancelar+"&MensagemErro=Guia "+ Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtBaseConversao.getNumeroGuiaCompleto()) +" encontrada, porém esta não pode ser utilizada para geração de boleto para esse tipo de guia, pois esse processo já possui débito enviado ao <b>CADIN/SEFAZ</b>! <br /> Para mais informações: <b>(62) 3213-1581</b> TELEJUDICIÁRIO!");
					} else*/ if(gerarBoletoNe.isGuiaStatusAguardandoPagamento(guiaEmissaoDtBaseConversao) || passoConsultar == CONSULTA_ITENS_GUIA) {
						if(guiaEmissaoDtBaseConversao.podeGerarBoleto()) {
							guiaEmissaoBoletoDt = gerarBoletoNe.consultarBoleto(guiaEmissaoDtBaseConversao);	
						} else {
							guiaEmissaoBoletoDt.copiar(guiaEmissaoDtBaseConversao);
						}	
						
						if (super.getAtributeParameter(request, "observacao1") != null && !super.getAtributeParameter(request, "observacao1").equalsIgnoreCase("null")){		
							guiaEmissaoBoletoDt.setObservacao1(super.getAtributeParameter(request, "observacao1"));			
						} 
						
						if (super.getAtributeParameter(request, "observacao2") != null && !super.getAtributeParameter(request, "observacao2").equalsIgnoreCase("null")){		
							guiaEmissaoBoletoDt.setObservacao2(super.getAtributeParameter(request, "observacao2"));			
						}
						
						if (super.getAtributeParameter(request, "observacao3") != null && !super.getAtributeParameter(request, "observacao3").equalsIgnoreCase("null")){		
							guiaEmissaoBoletoDt.setObservacao3(super.getAtributeParameter(request, "observacao3"));			
						}
						
						if (super.getAtributeParameter(request, "observacao4") != null && !super.getAtributeParameter(request, "observacao4").equalsIgnoreCase("null")){		
							guiaEmissaoBoletoDt.setObservacao4(super.getAtributeParameter(request, "observacao4"));			
						}
						
						ComarcaNe comarcaNe = new ComarcaNe();
						
						if (!guiaEmissaoBoletoDt.isGuiaEmitidaSPG() && !guiaEmissaoBoletoDt.isGuiaEmitidaSSG()) {
							//Consulta os itens da guia
							guiaEmissaoBoletoDt.setListaGuiaItemDt(new GuiaItemNe().consultarItensGuia(guiaEmissaoBoletoDt.getId()));							
						}
						if ((guiaEmissaoBoletoDt.getId_Comarca() == null || guiaEmissaoBoletoDt.getId_Comarca().trim().length() == 0)) {
							String comarcaCodigo = ComarcaDt.GOIANIA;
							if (guiaEmissaoBoletoDt.isGuiaCertidaoSPG()) {
								if (guiaEmissaoBoletoDt.getInfoLocalCertidaoSPG() != null && guiaEmissaoBoletoDt.getInfoLocalCertidaoSPG().trim().length() > 3) {							
									comarcaCodigo = guiaEmissaoBoletoDt.getInfoLocalCertidaoSPG().trim().substring(0, 3);
								} else if (guiaEmissaoBoletoDt.getComarcaCodigo() != null && guiaEmissaoBoletoDt.getComarcaCodigo().trim().length() > 0) {
									comarcaCodigo = guiaEmissaoBoletoDt.getComarcaCodigo();
								}
							} else if (guiaEmissaoBoletoDt.isGuiaEmitidaSPG() && guiaEmissaoBoletoDt.getComarcaCodigo() != null && guiaEmissaoBoletoDt.getComarcaCodigo().trim().length() > 0) {
								comarcaCodigo = guiaEmissaoBoletoDt.getComarcaCodigo();
							}
							ComarcaDt comarcaDt = comarcaNe.consultarComarcaCodigo(comarcaCodigo);
							if (comarcaDt == null && guiaEmissaoBoletoDt.getComarcaCodigo() != null && guiaEmissaoBoletoDt.getComarcaCodigo().trim().length() > 0) {
								comarcaDt = comarcaNe.consultarComarcaCodigo(guiaEmissaoBoletoDt.getComarcaCodigo());
							} 
							if (comarcaDt == null) {
								comarcaDt = comarcaNe.consultarComarcaCodigo(ComarcaDt.GOIANIA);
							}							
							if (comarcaDt != null) {
								guiaEmissaoBoletoDt.setId_Comarca(comarcaDt.getId());
								guiaEmissaoBoletoDt.setComarca(comarcaDt.getComarca());
								guiaEmissaoBoletoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
							}
						}
						if(guiaEmissaoBoletoDt.getId_Comarca() != null && guiaEmissaoBoletoDt.getId_Comarca().trim().length() > 0) {
							ComarcaDt comarcaDt = new ComarcaNe().consultarId(guiaEmissaoBoletoDt.getId_Comarca());
							if (comarcaDt != null) {
								guiaEmissaoBoletoDt.setComarca(comarcaDt.getComarca());	
							}							
						}
						
						if(guiaEmissaoBoletoDt.getId_AreaDistribuicao() != null && guiaEmissaoBoletoDt.getId_AreaDistribuicao().trim().length() > 0) {
							AreaDistribuicaoDt areaDistribuicaoDt = new AreaDistribuicaoNe().consultarId(guiaEmissaoBoletoDt.getId_AreaDistribuicao());
							if (areaDistribuicaoDt != null) {
								guiaEmissaoBoletoDt.setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());	
							}							
						}
						
						if (guiaEmissaoBoletoDt.getId_ProcessoTipo() != null && guiaEmissaoBoletoDt.getId_ProcessoTipo().trim().length() > 0) {
							ProcessoTipoDt processoTipoDt = gerarBoletoNe.consultarProcessoTipo(guiaEmissaoBoletoDt.getId_ProcessoTipo());
							if (processoTipoDt != null) {
								guiaEmissaoBoletoDt.setProcessoTipoCodigo(processoTipoDt.getProcessoTipoCodigo());	
							}								
						}
						
						if (guiaEmissaoBoletoDt.getId_Processo() != null && guiaEmissaoBoletoDt.getId_Processo().trim().length() > 0) {
							ProcessoDt processoDt = gerarBoletoNe.consultarProcesso(guiaEmissaoBoletoDt.getId_Processo());
							if (processoDt != null) {
								guiaEmissaoBoletoDt.setProcessoDt(processoDt);	
							}								
						}
						
						//*********** Alteração para o concurso de Juiz
						//Leandro Prezotto precisa que o link de acesso ao serviço do boleto, possa adicionar no link o nome e cpf do candidato.
						//Fiz esta interface para somente o caso dessas variáveis estarem preenchidas.
						if( request.getParameter("NomeCandidato") != null && !request.getParameter("NomeCandidato").equalsIgnoreCase("null") ) {
							if( guiaEmissaoBoletoDt != null && guiaEmissaoBoletoDt.getPagador() != null && (guiaEmissaoBoletoDt.getPagador().getNome() == null || guiaEmissaoBoletoDt.getPagador().getNome().isEmpty() ) ) {
								guiaEmissaoBoletoDt.getPagador().setNome(request.getParameter("NomeCandidato").trim());
							}
						}
						if( request.getParameter("CpfCandidato") != null && !request.getParameter("CpfCandidato").equalsIgnoreCase("null") ) {
							if( guiaEmissaoBoletoDt != null && guiaEmissaoBoletoDt.getPagador() != null && (guiaEmissaoBoletoDt.getPagador().getCpf() == null || guiaEmissaoBoletoDt.getPagador().getCpf().isEmpty() ) ) {
								guiaEmissaoBoletoDt.getPagador().setCpf(request.getParameter("CpfCandidato").trim());
							}
						}
						//***********
						
						request.getSession().setAttribute("GuiaEmissaoDtBase", guiaEmissaoDtBaseConversao);
						request.getSession().setAttribute("GuiaEmissaoBoletoDt", guiaEmissaoBoletoDt);
						
						if (passoConsultar == CONSULTA_ITENS_GUIA) {					
							stAcao = gereBoleto(request, response, passoConsultar, guiaEmissaoBoletoDt);
						} else {
							stAcao = PAGINA_GUIA_DADOS_PAGADOR;
						}	
					} else {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Cancelar+"&MensagemErro=Guia "+ Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDtBaseConversao.getNumeroGuiaCompleto()) +" encontrada, porém esta não pode ser utilizada para geração de boleto por não se encontrar <b>em aberto</b>! Situação atual: <b>" + guiaEmissaoDtBaseConversao.getGuiaStatus() +"</b>!");
					}
				}
				else {
					stAcao = PAGINA_CONSULTAR_GUIA;
					request.setAttribute("MensagemErro", "Guia "+ Funcoes.FormatarNumeroSerieGuia(numeroGuiaInformado) +" não encontrada. <br />Verifique o número e tente novamente.");
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
						
						String mensagem = gerarBoletoNe.verificarDadosPagador(guiaEmissaoBoletoDt);
						
						if(mensagem.length() > 0) {
							request.setAttribute("MensagemErro", mensagem);
							stAcao = PAGINA_GUIA_DADOS_PAGADOR;
						} else {
							//Captcha
							stAcao = gereBoleto(request, response, passoConsultar, guiaEmissaoBoletoDt);
						}
						
						break;
					}					
					
					case Configuracao.Curinga7: {
						
						if( guiaEmissaoDtBase != null && guiaEmissaoDtBase.getNumeroGuiaCompleto() != null && !guiaEmissaoDtBase.getNumeroGuiaCompleto().isEmpty() ) {
							stAcao = PAGINA_GUIA_DADOS_PAGADOR;
						}
						else {
							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo);
						}
						
						break;
					}
				}
				
				break;
			}
			
			//Impressão da guia
			case Configuracao.Imprimir : {
				
				guiaEmissaoBoletoDt = (BoletoDt)request.getSession().getAttribute("GuiaEmissaoBoletoDt");
				if( guiaEmissaoBoletoDt == null ) {
					request.setAttribute("MensagemErro", "Guia não encontrada, favor consultar novamente!");
					stAcao = PAGINA_CONSULTAR_GUIA;
				} else {				
					if (SituacaoBoleto.NAO_REGISTRADO.equals(guiaEmissaoBoletoDt.getSituacaoBoleto()))
					{
						guiaEmissaoBoletoDt	= gerarBoletoNe.emitirBoleto(guiaEmissaoBoletoDt, UsuarioSessao);
					} 
					else if (!guiaEmissaoBoletoDt.isSerie10() && (guiaEmissaoBoletoDt.isVencido() || 
							 guiaEmissaoBoletoDt.isMudouPagador() ||
							 guiaEmissaoBoletoDt.isValorGuiaDiferenteValorBoleto() ||
							 guiaEmissaoBoletoDt.isObservacaoFoiAterada()) ) 
					{								
						gerarBoletoNe.reemitirBoleto(guiaEmissaoBoletoDt, UsuarioSessao);					
					}
					
					if (guiaEmissaoBoletoDt.getUrlPdf() == null || guiaEmissaoBoletoDt.getUrlPdf().trim().length() == 0) {
						guiaEmissaoBoletoDt.setUrlPdf(gerarBoletoNe.obtenhaUrlPdf(guiaEmissaoBoletoDt));
					}
					request.getSession().setAttribute("GuiaEmissaoBoletoDt", guiaEmissaoBoletoDt);					
					stAcao = PAGINA_BOLETO_PDF;					
				}
				break;
			}			

			default : {
				
				if( request.getParameter("MensagemErro") != null ) {
					request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
				}
				
				if (passoEditar == Configuracao.Curinga9) {
					stAcao = gereBoleto(request, response, passoConsultar, guiaEmissaoBoletoDt);
				} else {
					stAcao = PAGINA_CONSULTAR_GUIA;
					gerarBoletoNe.limparGuiaSessao(request);
					
					guiaEmissaoBoletoDt = new BoletoDt();
					guiaEmissaoDtBase = new GuiaEmissaoDt();
					
					guiaEmissaoDtBase.setNumeroGuiaCompleto("");
					
					request.getSession().setAttribute("GuiaEmissaoBoletoDt", guiaEmissaoBoletoDt);
					request.getSession().setAttribute("GuiaEmissaoDtBase", guiaEmissaoDtBase);
					
					if( request.getParameter("MensagemOk") != null ) {
						request.setAttribute("MensagemOk",request.getParameter("MensagemOk"));
					}
					if( request.getParameter("MensagemErro") != null ) {
						request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
					}
				}
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private String gereBoleto(HttpServletRequest request, HttpServletResponse response, int passoConsultar, BoletoDt guiaEmissaoBoletoDt) throws Exception, IOException {
		String stAcao;
		if (super.checkRecaptcha(request)) {
			if (guiaEmissaoBoletoDt.getListaGuiaItemDt() == null ||
				guiaEmissaoBoletoDt.getListaGuiaItemDt().size() == 0) {
				guiaEmissaoBoletoDt.setListaGuiaItemDt(gerarBoletoNe.consultarGuiaItens(guiaEmissaoBoletoDt.getId(), guiaEmissaoBoletoDt.getId_GuiaTipo()));				
			}
				
			List listaGuiaItemDt = guiaEmissaoBoletoDt.getListaGuiaItemDt();
			
			//Deve haver no mínimo 1 item de guia
			if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
				
				gerarBoletoNe.getGuiaCalculoNe().recalcularTotalGuia(listaGuiaItemDt);	
				
				request.setAttribute("ListLocomocaoMandado", new LocomocaoNe().consultarLocomocaoMandadoSPG(guiaEmissaoBoletoDt.getNumeroGuiaCompleto()));
				request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
				request.setAttribute("TotalGuia", gerarBoletoNe.getGuiaCalculoNe().getTotalGuia() );
				
				request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
				request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( gerarBoletoNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
				if (passoConsultar == CONSULTA_ITENS_GUIA) {
					request.setAttribute("visualizarBotaoImpressaoBotaoPagamento" , new Boolean(false));
					request.setAttribute("comandoOnClickBotaoVoltar" , "AlterarValue('PaginaAtual','" + Configuracao.Novo + "');");
				} else {
					request.setAttribute("visualizarBotaoImpressaoBotaoPagamento" , gerarBoletoNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoBoletoDt));	
				}							
				request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
				request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
				request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoBoletoDt);
				stAcao = PAGINA_GUIA_PREVIA_CALCULO;								
			}
			else {
				request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
				stAcao = PAGINA_GUIA_DADOS_PAGADOR;
			}
		} else {
			//Captcha
			stAcao = executeAcaoInicial(request, response);
		}
		return stAcao;
	}

	private String executeAcaoInicial(HttpServletRequest request, HttpServletResponse response)
			throws Exception, IOException {
		String stAcao;
		BoletoDt guiaEmissaoBoletoDt;
		GuiaEmissaoDt guiaEmissaoDtBase;
		stAcao = PAGINA_CONSULTAR_GUIA;
		
		if( gerarBoletoNe.isConexaoSPG_OK() ) {
			
			gerarBoletoNe.limparGuiaSessao(request);
			
			guiaEmissaoBoletoDt = new BoletoDt();
			guiaEmissaoDtBase = new GuiaEmissaoDt();
			
			guiaEmissaoDtBase.setNumeroGuiaCompleto("");
			
			request.getSession().setAttribute("GuiaEmissaoBoletoDt", guiaEmissaoBoletoDt);
			request.getSession().setAttribute("GuiaEmissaoDtBase", guiaEmissaoDtBase);
			
			//Removendo processoDt da sessão para evitar de mostrar dados de processos de outras abas ou que o adv estava trabalhando.
			request.getSession().removeAttribute("processoDt");
			request.getSession().setAttribute("processoDt", new ProcessoDt());
			
			request.getSession().removeAttribute("ProcessoCadastroDt");
			request.getSession().setAttribute("ProcessoCadastroDt", new ProcessoCadastroDt());
			
			if( request.getParameter("MensagemOk") != null ) {
				request.setAttribute("MensagemOk",request.getParameter("MensagemOk"));
			}
			if( request.getParameter("MensagemErro") != null ) {
				request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
			}
			
		}
		else {
			redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Cancelar+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
		}
		return stAcao;
	}
	
	protected void configure(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("TipoConsulta", "Publica");	
	}
	
	protected String obtenhaTituloPagina(int passoConsultar) {
		if (passoConsultar == CONSULTA_ITENS_GUIA) {
			return "Consultar Itens da Guia";	
		} else {
			return "Geração de Boleto";	
		}		
	}
}
