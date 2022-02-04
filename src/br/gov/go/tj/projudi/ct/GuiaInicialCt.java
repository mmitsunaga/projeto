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
import br.gov.go.tj.projudi.dt.BairroGuiaLocomocaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaInicialDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
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

public class GuiaInicialCt extends Controle {

	private static final long serialVersionUID = 8606566649924007255L;
	
	protected static final String PAGINA_GUIA_INICIAL 			= "/WEB-INF/jsptjgo/GuiaInicial.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 	= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_PADRAO_LOCALIZAR 		= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	protected static final String NOME_CONTROLE_WEB_XML = "GuiaInicial1Grau";
	
	private GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
	private GuiaLocomocaoNe guiaLocomocaoNe = new GuiaLocomocaoNe();
	private ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
	
	@Override
	public int Permissao() {
		return GuiaInicialDt.CodigoPermissao;
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

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		stAcao = PAGINA_GUIA_INICIAL;
		
		//********************************************
		// Variáveis utilizadas pela página
		List listaCustaDt 		= null;
		List listaBairroDt 		= null;
		List listaQuantidadeBairroDt = null;
		List listaItensGuia 	= null;
		
		//********************************************
		//Variáveis objetos
		GuiaEmissaoDt guiaEmissaoDt = null;
		
		//********************************************
		//Variáveis de sessão
		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDtInicial");
		if( guiaEmissaoDt == null ) {
			guiaEmissaoDt = new GuiaEmissaoDt();
		}
		guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
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
			if( guiaEmissaoDt.getCodigoGrau() != null && guiaEmissaoDt.getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
				GRAU_ESCOLHIDO = GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU;
			}
			else {
				if( guiaEmissaoDt.getCodigoGrau() != null && guiaEmissaoDt.getCodigoGrau().equals("3") ) {
					GRAU_ESCOLHIDO = "3";
				}
				else {
					GRAU_ESCOLHIDO = GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU;
				}
			}
			guiaEmissaoDt.setCodigoGrau(String.valueOf(GRAU_ESCOLHIDO));
			request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
		}
		else {
			if( guiaEmissaoDt.getCodigoGrau() != null && !guiaEmissaoDt.getCodigoGrau().isEmpty() ) {
				GRAU_ESCOLHIDO = guiaEmissaoDt.getCodigoGrau();
			}
		}
		
		if( request.getParameter("area") != null && request.getParameter("area").length() > 0 ) {
			guiaEmissaoDt.setCodigoArea(request.getParameter("area"));
		}
		
		if( request.getParameter("bensPartilhar") != null && !request.getParameter("bensPartilhar").equals("null") && request.getParameter("bensPartilhar").length() > 0 ) {
			guiaEmissaoDt.setBensPartilhar(request.getParameter("bensPartilhar"));
		}
		
		if (request.getParameter("finalidade") != null && request.getParameter("finalidade").toString().length() > 0) {
			guiaEmissaoDt.setFinalidade(request.getParameter("finalidade").toString());
		}
		
		if (request.getParameter("penhora") != null && request.getParameter("penhora").toString().length() > 0) {
			guiaEmissaoDt.setPenhora(request.getParameter("penhora").toString());
		}
		
		if (request.getParameter("intimacao") != null && request.getParameter("intimacao").toString().length() > 0) {
			guiaEmissaoDt.setIntimacao(request.getParameter("intimacao").toString());
		}
		
		if (request.getParameter("oficialCompanheiro") != null && request.getParameter("oficialCompanheiro").toString().length() > 0) {
			guiaEmissaoDt.setOficialCompanheiro(request.getParameter("oficialCompanheiro"));
		}
		
		if( request.getParameter("correioQuantidade") != null && request.getParameter("correioQuantidade").length() > 0 ) {
			guiaEmissaoDt.setCorreioQuantidade(request.getParameter("correioQuantidade"));
			//Se for segundo grau
			guiaEmissaoDt.setCorreioQuantidadeReg4VI(request.getParameter("correioQuantidade"));
		}
		
		request.setAttribute("exibeOficialCompanheiro", new Boolean(true)); //Conforme informado pelo Marcelo da Corregedoria, só é realizado o cálculo em dobro automático na guia inicial.
		
		switch(paginaatual) {
		
			case Configuracao.Novo: {
				
				stAcao = PAGINA_GUIA_INICIAL;
				
				if( guiaEmissaoNe.isConexaoSPG_OK() ) {
					
//					//****
//					//****
//					guiaEmissaoNe.consultarGuiaProjudiCadastrarSPG_NAO_UTILIZAR("1905138750", null);
//					guiaEmissaoNe.consultarGuiaProjudiCadastrarSPG_NAO_UTILIZAR("1916357650", null);
//					//****
//					//****
//					List<BairroDt> lista = new ArrayList<BairroDt>();
//					BairroDt b1 = new BairroDt();
//					b1.setId("6");
//					BairroDt b2 = new BairroDt();
//					b2.setId("6");
//					lista.add(b1);
//					lista.add(b1);

//					//5004398.78.2020.8.09.0000
//					String idProcesso = "366448";
//					String idMandado = "99999";

//					String idProcesso = "352982";
//					String idMandado = "26";
					
//					guiaEmissaoNe.gerarGuiaLocomocaoMandadoJudicial(null, idProcesso, idMandado, lista);
					
					limparGuia(guiaEmissaoDt, request);
					
					if( request.getParameter("MensagemOk") != null ) {
						request.setAttribute("MensagemOk",request.getParameter("MensagemOk"));
					}
					if( request.getParameter("MensagemErro") != null ) {
						request.setAttribute("MensagemErro",request.getParameter("MensagemErro"));
					}
						
				}
				else {
					redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
					return;
				}
				
				break;
			}
			
			//Valida processo principal ou vinculado
			case Configuracao.Curinga7: {
				
				if( guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
					if( !guiaEmissaoNe.isProcessoNumeroCompletoExistente(guiaEmissaoDt.getNumeroProcessoDependente()) ) {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE));
						return;
					}
					
					//Se o processo principal informado não estiver no mesmo grau da guia inicial informado, o sistem não pode ocultar os campos pois filtraria as classes com o subtipo errado 
					ProcessoDt processoViculadoDt = guiaEmissaoNe.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente());
					if( processoViculadoDt != null && processoViculadoDt.getId_Serventia() != null ) {
						ServentiaDt serventiaProcessoPrincipal = guiaEmissaoNe.consultarIdServentia(processoViculadoDt.getId_Serventia());
						if (guiaEmissaoDt.getCodigoGrau() == GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU && ServentiaSubtipoDt.isSegundoGrau(serventiaProcessoPrincipal.getServentiaSubtipoCodigo()) 
								|| (guiaEmissaoDt.getCodigoGrau() == GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU && !ServentiaSubtipoDt.isSegundoGrau(serventiaProcessoPrincipal.getServentiaSubtipoCodigo()))
								){
							//Se encontrou o processo então esconde div de escolha da comarca e área distribuição
							guiaEmissaoDt.setId_Comarca(null);
							guiaEmissaoDt.setId_AreaDistribuicao(null);
							guiaEmissaoDt.setId_ProcessoTipo(null);
							
							guiaEmissaoDt.setComarca("");
							guiaEmissaoDt.setAreaDistribuicao("");
							guiaEmissaoDt.setProcessoTipo("");
							guiaEmissaoDt.setGrauGuiaInicialMesmoGrauProcessoDependente(true);
						} else{
							guiaEmissaoDt.setGrauGuiaInicialMesmoGrauProcessoDependente(false);
						}
					}
					else {
						redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE));
						return;
					}
					
				} else{
					guiaEmissaoDt.setGrauGuiaInicialMesmoGrauProcessoDependente(false);
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
						
						//Se o processo dependente não está preenchido, então
						//Valida se preencheu a comarca, area de distribuicao e classe
						//if( guiaEmissaoDt.getNumeroProcessoDependente() == null || guiaEmissaoDt.getNumeroProcessoDependente().isEmpty() ) {
						if (!guiaEmissaoDt.isGrauGuiaInicialMesmoGrauProcessoDependente()) {
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
						
						//Valor da causa negativo?
						if( guiaEmissaoDt.isValorAcaoNegativo() ) {
							throw new MensagemException("Valor da Causa não pode ser Negativo.");
						}
						
						//Ocorrência 2019/1008
						//Suporte solicita que caso seja informada a classe de Carta Precatória, não deixar informar número do processo principal/dependente
						if( 
							processoTipoNe.isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
							&&
							guiaEmissaoDt.getNumeroProcessoDependente() != null 
							&& !guiaEmissaoDt.getNumeroProcessoDependente().isEmpty()
						  ) {
							throw new MensagemException("Ao informar a classe de Carta Precatória, não é permitido informar o número do processo principal/dependente.");
						}
						
						//Se o processo dependente está preenchido
						//Valida se a classe está presente
						if( guiaEmissaoDt.getNumeroProcessoDependente() != null && !guiaEmissaoDt.getNumeroProcessoDependente().isEmpty() ) {
							if( guiaEmissaoDt.getId_ProcessoTipo() == null || guiaEmissaoDt.getId_ProcessoTipo().isEmpty() ) {
								throw new MensagemException("Por favor, informe a Classe.");
							}
						}
						
						//Validação da modelo da guia
						if( !GRAU_ESCOLHIDO.equals("3") && guiaEmissaoNe.consultarGuiaModeloProcessoTipoNovoRegimento(null, GRAU_ESCOLHIDO, guiaEmissaoDt.getId_ProcessoTipo()) == null ) {
							throw new MensagemException("Modelo da guia não encontrado. Pode ser que esta classe não tenha cálculo homologado(Guia Inicial).");
						}
						
						//Valida se a Classe é "Carta de Ordem" ou "Carta Precatória" para verificar se é o estado 
						// para a cobrança de taxa judiciária
						if( guiaEmissaoDt.getProcessoTipoCodigo() != null &&
							guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && 
							guiaEmissaoNe.isProcessoTipoPortePostagem_e_OrigemEstado(guiaEmissaoDt.getProcessoTipoCodigo()) &&
							(guiaEmissaoDt.getOrigemEstado() == null || guiaEmissaoDt.getOrigemEstado().isEmpty() )) {
							
							redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro=Por favor. De acordo com a Classe escolhida, informe se o Estado de Goiás é origem ou não para a cobrança da Taxa Judiciária.");
							return;
						}
						
						//Validação do processo vinculado caso seja informado
						boolean validaComarcaAreaDistribuicao = true;
						if( guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
							if( guiaEmissaoNe.isProcessoNumeroCompletoExistente(guiaEmissaoDt.getNumeroProcessoDependente())) {
								if (guiaEmissaoDt.isGrauGuiaInicialMesmoGrauProcessoDependente()){
									validaComarcaAreaDistribuicao = false;
								}
							}
							else {
								redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE));
								return;
							}
						}
						
						if( validaComarcaAreaDistribuicao ) {
							//Verificar se comarca, area de distribuição e classe está preenchido
							if( guiaEmissaoDt.getId_Comarca() == null || guiaEmissaoDt.getId_AreaDistribuicao() == null ) {
								redireciona(response, obtenhaNomeControleWebXml()+"?PaginaAtual=-666&MensagemErro=Por favor. Escolha Comarca, Área de Distribuição e a Classe CNJ.");
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
						
						//Ocorrência 2019/6291
						//Consulta lista de itens da guia e modelo
						//Ocorrencia 2021/2039
						//Mudei para pesquisar somente "Mandado de Segurança Cível" e criei os modelos para as guias necessárias de primeiro grau. PS.: quando for turma, é gerado os itens pelo método.
						if( guiaEmissaoDt.getCodigoGrau() != null && guiaEmissaoDt.getCodigoGrau().equals("3") ) {
							listaItensGuia = guiaEmissaoNe.gerarGuiaInicialMandadoSegurancaTurmaRecursal(listaItensGuia, new GuiaModeloDt(), guiaEmissaoDt);
						}
						else {
							listaItensGuia = guiaEmissaoNe.consultarItensGuiaNovoRegimento(null, GRAU_ESCOLHIDO, guiaEmissaoDt.getId_ProcessoTipo());
							
							GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipoNovoRegimento(null, GRAU_ESCOLHIDO, guiaEmissaoDt.getId_ProcessoTipo());
							guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
						}
						
						//Locomoção com Zona-Bairro
						List valoresIdBairro = null;
						List valoresIdBairroContaVinculada = null;
						listaBairroDt = (List)request.getSession().getAttribute("ListaBairroDt");//aqui leandro
						listaQuantidadeBairroDt = new ArrayList();
						if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
							if( valoresIdBairro == null )
								valoresIdBairro = new ArrayList();
							if( valoresIdBairroContaVinculada == null )
								valoresIdBairroContaVinculada = new ArrayList();
							
							for(int i = 0; i < listaBairroDt.size(); i++) {
								BairroGuiaLocomocaoDt bairroDt = (BairroGuiaLocomocaoDt)listaBairroDt.get(i);
								
								
								int quantidadeLocomocao = 0;
								if( request.getSession().getAttribute("guiaInicialPublica") != null && Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()) > 0 ) {
									if( listaBairroDt.size() == Funcoes.StringToInt(request.getSession().getAttribute("guiaInicialPublica").toString()) ) {
										
										if( request.getSession().getAttribute("guiaInicialPublicaQuantidadeLocomocao"+i) != null ) {
											quantidadeLocomocao = Funcoes.StringToInt(request.getSession().getAttribute("guiaInicialPublicaQuantidadeLocomocao"+i).toString());
										}
										
									}
								}
								else {
									quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocao"+i));//aqui leandro
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
							}
						}
						
						
						
						Map valoresReferenciaCalculo = new HashMap();
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcao());
						valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcao());
						if( guiaEmissaoDt.getNovoValorAcaoAtualizado().toString().length() == 0 ) {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
						}
						else {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, guiaEmissaoDt.getNovoValorAcaoAtualizado());
						}
						if( valoresIdBairro != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
						valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;0");
						if( guiaEmissaoDt.getProcessoTipoCodigo() != null && guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && guiaEmissaoNe.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
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
							 Integer.parseInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_CIVEL)
						   ) {
							
							valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + guiaEmissaoDt.getProcessoTipoCodigo());
						}
						
						
//						//Despesa Postal
						if( guiaEmissaoDt.getCorreioQuantidade() != null && guiaEmissaoDt.getCorreioQuantidade().length() > 0 && !guiaEmissaoDt.getCorreioQuantidade().equals("0")) {
							if( listaItensGuia == null )
								listaItensGuia = new ArrayList<GuiaCustaModeloDt>();
							
							if( GRAU_ESCOLHIDO != null && GRAU_ESCOLHIDO.equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
								listaItensGuia.add( guiaEmissaoNe.adicionarItem(CustaDt.DESPESA_POSTAL) );
							}
							else {
								listaItensGuia.add( guiaEmissaoNe.adicionarItem(CustaDt.TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM) );
							}
						}
						
						//Verifica se é cautelar, contencioso
						guiaEmissaoNe.adicionarItemCautelarContenciosoParaGuiaInicial(GRAU_ESCOLHIDO, listaItensGuia, guiaEmissaoDt);
						
						//Inclusão da Taxa Judiciária
						//Estado é origem?
						if( guiaEmissaoDt.getProcessoTipoCodigo() != null &&
							guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && 
							guiaEmissaoNe.isProcessoTipoPortePostagem_e_OrigemEstado(guiaEmissaoDt.getProcessoTipoCodigo()) &&
							guiaEmissaoDt.getOrigemEstado() != null && 
							guiaEmissaoDt.getOrigemEstado().equals(GuiaEmissaoDt.VALOR_NAO) ) {
							
							listaItensGuia.add( guiaEmissaoNe.adicionarItem(CustaDt.TAXA_JUDICIARIA_PROCESSO) );
							
							//Pedido do Marcelo da Corregedoria: Quando é natureza "carta precatoria" e a origem não é o estado, então cobra o valor mínimo
							//if( guiaEmissaoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA)) ) {
							//Ocorrência 2019/7631 - Foi adicionado no if a verificação se o processo é carta de ordem cpc para colocar o valor mínimo da tx judiciária.
							if( guiaEmissaoDt.getProcessoTipoCodigo() != null && 
								(
									new ProcessoTipoNe().isProcessoTipoCartaPrecatoria(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()))
									||
									guiaEmissaoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_ORDEM_CPC))
								)
							) {
								valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
							}
						}
						
						//Porte Remessa
						if( guiaEmissaoDt.getProtocoloIntegrado() != null && guiaEmissaoDt.getPorteRemessaQuantidade() != null && Funcoes.StringToInt(guiaEmissaoDt.getPorteRemessaQuantidade()) > 0 ) {
							String protocoloIntegrado[] = request.getParameterValues("protocoloIntegrado");
							if( protocoloIntegrado != null && guiaEmissaoDt.getProtocoloIntegrado().equals(GuiaEmissaoDt.VALOR_SIM) ) {
								listaItensGuia.add( guiaEmissaoNe.adicionarItem(CustaDt.PORTE_REMESSA) );
							}
						}
						
						List listaIdBairroAux = new ArrayList();
						if( valoresIdBairro != null && valoresIdBairro.size() > 0 ) {
							listaIdBairroAux.addAll(valoresIdBairro);
						}
						boolean bairrosZoneados = guiaEmissaoNe.isBairroZoneado(listaIdBairroAux);
						
						
						//Verifica se guia inicial é de locomoção em dobro
						if( guiaEmissaoNe.isProcessoTipoDobrarLocomocao(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
							guiaEmissaoDt.setOficialCompanheiro(GuiaEmissaoDt.VALOR_SIM);
						}
						
						//Verifica se guia inicial é de oficial companheiro obrigatório
						if( guiaEmissaoNe.isProcessoTipoOficialCompanheiroObrigatorio(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
							guiaEmissaoDt.setOficialCompanheiro(GuiaEmissaoDt.VALOR_SIM);
						}
						
						List listaGuiaItemDt = new ArrayList();
						if( bairrosZoneados ) {
							listaGuiaItemDt = guiaEmissaoNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						}
						
						
						List<GuiaItemDt> listaGuiaItemLocomocaoDt = new ArrayList<GuiaItemDt>();
						List<Integer> listaQuantidadeBairroDt_INTEGER = new ArrayList<Integer>();
						for( int k = 0; k < listaQuantidadeBairroDt.size(); k++ ) {
							listaQuantidadeBairroDt_INTEGER.add(new Integer(listaQuantidadeBairroDt.get(k).toString()));
						}
						preparaItensDeLocomocao(listaBairroDt, listaQuantidadeBairroDt_INTEGER, listaGuiaItemLocomocaoDt, guiaEmissaoDt, !GRAU_ESCOLHIDO.equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU)); //aqui leandro
						if (listaGuiaItemLocomocaoDt.size() > 0) {
							
							//Comentado depois de reunião com Marcelo contador e Dadiany.
//							//**************
//							//lógica adicionado depois do email dia 07/06/2016 do marcelo com a seguinte informação:
//							//As naturezas abaixo relacionadas, quando utilizar locomoção de oficial de justiça, o sistema deve fazer a cobrança automática do item de receita nº (1066), Reg. de Custas nº 61 I a XI de acordo com o valor causa.
//							if( guiaEmissaoDt.getProcessoTipoCodigo() != null && 
//								guiaEmissaoNe.isProcessoTipoAdicionarRegimento61(guiaEmissaoDt.getProcessoTipoCodigo()) ) {
//								
//								for( GuiaItemDt guiaItemDt: listaGuiaItemLocomocaoDt ) {
//									if( guiaEmissaoNe.isItemLocomocaoTribunal(guiaItemDt.getCustaDt().getId()) ) {
//										//Adiciona o item
//										listaItensGuia.add( guiaEmissaoNe.adicionarItem(CustaDt.CUSTA_PENHORA) );
//										
//										//recalcula
//										listaGuiaItemDt = guiaEmissaoNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
//										
//										//break pois é necessário adicionar somente 1 vez
//										break;
//									}
//								}
//							}
//							//**************
							
							listaGuiaItemDt.addAll(listaGuiaItemLocomocaoDt);
							
							guiaLocomocaoNe.recalcularTotalGuia(listaGuiaItemDt);
						}
						
						
						//Trecho comentado através da ocorrência 2016/27831:
						//Verifica se tem item de custa regimento 61, caso sim, exige que tenha locomoção informada.
//						if( guiaEmissaoNe.isRegimento61Presente(listaGuiaItemDt) && listaGuiaItemLocomocaoDt.isEmpty() ) {
//							request.setAttribute("MensagemErro", "Foi identificado o item de custa Regimento 61. <br />Deve ser adicionado ao menos 1 locomoção.");
//							stAcao = PAGINA_GUIA_INICIAL;
//							
//							break;
//						}
						
						
						guiaEmissaoNe.getGuiaCalculoNe().setTotalGuia(guiaEmissaoNe.getGuiaCalculoNe().calcularTotalGuia(listaGuiaItemDt));
						
						
						if( !bairrosZoneados ) {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
							stAcao = PAGINA_GUIA_INICIAL;
						}
						else {
							//Deve haver no mínimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
								
								request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.setAttribute("TotalGuia", guiaEmissaoNe.getGuiaCalculoNe().getTotalGuia() );
								
								request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
								request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaEmissaoNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
								request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaEmissaoNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
								request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
								request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
								request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
								
								guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
								
								request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
								
								stAcao = obtenhaAcaoPreviaDeCalculo(request);
								
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
								stAcao = PAGINA_GUIA_INICIAL;
							}
						}
						
						break;
					}
					
					case Configuracao.Excluir : {
						//Limpar dados da sessão
						if( guiaEmissaoDt != null ) {
							guiaEmissaoDt.setId_AreaDistribuicao(null);
							guiaEmissaoDt.setId_ProcessoTipo(null);
							
							guiaEmissaoDt.setAreaDistribuicao("");
							guiaEmissaoDt.setProcessoTipo("");
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
							if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
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
				
				guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDtInicial");
				if( guiaEmissaoDt == null ) {
					guiaEmissaoDt = new GuiaEmissaoDt();
				}
				
				//Analisando os erros de log vi um erro que partiu da guia inicial. Porém a nesse controle não deveria ter data de recebimento preenchido.
				if( guiaEmissaoDt != null && guiaEmissaoDt.getDataRecebimento() != null ) {
					throw new MensagemException("Guia gerada com dados inconsistentes. Por favor, recomeçar o preenchimento.");
				}
				
				//Validações para evitar possíveis problemas de guia inicial sem estes dados preenchidos
				//Se número do processo dependente está preenchido, informa somente a classe(proc_tipo)
				String msgValidacaoGuiaInicial = "";
				if( !guiaEmissaoDt.isGrauGuiaInicialMesmoGrauProcessoDependente() ) {
					if( !guiaEmissaoNe.isIdComarcaPreenchido(guiaEmissaoDt.getId_Comarca()) ) {
						msgValidacaoGuiaInicial += "<br />Comarca não identificada nesta guia, verifique se escolheu a Comarca.";
					}
					if( !guiaEmissaoNe.isIdAreaDistribuicaoPreenchido(guiaEmissaoDt.getId_AreaDistribuicao()) ) {
						msgValidacaoGuiaInicial += "<br />Área de Distribuição não identificada nesta guia, verifique se escolheu a Área de Distribuição.";
					}
					if( !guiaEmissaoNe.isIdProcessoTipoPreenchido(guiaEmissaoDt.getId_ProcessoTipo()) ) {
						msgValidacaoGuiaInicial += "<br />Classe não identificada nesta guia, verifique se escolheu a Classe Processual.";
					}
				}
				else {
					if( !guiaEmissaoNe.isIdProcessoTipoPreenchido(guiaEmissaoDt.getId_ProcessoTipo()) ) {
						msgValidacaoGuiaInicial += "<br />Classe não identificada nesta guia, verifique se escolheu a Classe Processual.";
					}
					
					ProcessoDt processoDependente = guiaEmissaoNe.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente());
					if( processoDependente != null) {
						ServentiaDt serventiaProcessoDependente = guiaEmissaoNe.consultarIdServentia(processoDependente.getId_Serventia());
						if (serventiaProcessoDependente != null) {
							if (serventiaProcessoDependente.getId_Comarca() != null && serventiaProcessoDependente.getId_Comarca().trim().length() > 0) {
								guiaEmissaoDt.setId_Comarca(serventiaProcessoDependente.getId_Comarca());
								guiaEmissaoDt.setComarcaCodigo(serventiaProcessoDependente.getComarcaCodigo());
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
				
				if( msgValidacaoGuiaInicial.length() > 0 ) {
					request.setAttribute("MensagemErro", msgValidacaoGuiaInicial);
					stAcao = PAGINA_GUIA_INICIAL;
				}
				else {
					//Obtem o proximo numero de Guia
					if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
						guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
						guiaEmissaoDt.setNumeroGuiaCompleto( guiaEmissaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
					}
					guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
					
					guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
					
					//Ocorrência 2018/1028
					//fred
					if( guiaEmissaoNe.isGuiaZeradaOuNegativa(guiaEmissaoDt.getListaGuiaItemDt()) ) {
						switch(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo()) {
							case GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU :
							case GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU : {
								guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.PAGO);
								break;
							}
						}
					}
					
					guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
					
					//Tratamento para evitar o erro que de tempos em tempos aparece ao emitir guia pública que já sai vinculada a processo. Possivelmente problema de sessão.
					guiaEmissaoDt.setId_Processo(null);
					guiaEmissaoDt.setProcessoDt(null);
					guiaEmissaoDt.setId_Serventia(null);
					guiaEmissaoDt.setServentia(null);
					
					//Salvar GuiaEmissão
					guiaEmissaoNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					
					request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
					
					switch(passoEditar) {
					
						case Configuracao.Imprimir: {
							
							guiaEmissaoDt.setApelante(guiaEmissaoDt.getRequerente());
							guiaEmissaoDt.setApelado(guiaEmissaoDt.getRequerido());
							guiaEmissaoDt.setValorAcao(guiaEmissaoDt.getNovoValorAcao());
							
							String nomeAdicionalGuia = "";
							if( guiaEmissaoDt.isGuiaInicialTurmaRecursal() ) {
								nomeAdicionalGuia = " (Mand.Seg. - Turma Recursal)";
							}
							
							//Geração da guia PDF
							byte[] byTemp = guiaEmissaoNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), guiaEmissaoDt.getAreaDistribuicao(), guiaEmissaoDt, guiaEmissaoDt.getCodigoGrau(), "INICIAL" + nomeAdicionalGuia);
							
							String nome="GuiaInicial_Numero_"+ guiaEmissaoDt.getNumeroGuiaCompleto();							
							enviarPDF(response, byTemp, nome);
							request.getSession().setAttribute("GuiaEmissaoDtInicial", limparGuia(guiaEmissaoDt, request));
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
							
							break;
						}
					}
				}
				
				break;
			}
			
			//Busca de Bairro
			case BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: {
				if (request.getParameter("Passo") == null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_Bairro");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno",obtenhaNomeControleWebXml());		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaEmissaoNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			//Busca comarca
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo") == null) {
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
				if (request.getParameter("Passo") == null) {
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
					ProcessoNe processoNe = new ProcessoNe();
					guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDtInicial");
					
					if( guiaEmissaoDt.getCodigoArea() != null && guiaEmissaoDt.getCodigoArea().equals(new String(String.valueOf(AreaDt.CIVEL))) ) {
						if( guiaEmissaoDt.isGuiaInicial1Grau() ) {
							stTemp = areaDistribuicaoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(), null, posicaopaginaatual);
						}
						else {
							boolean turmaRecursal = false;
							if( guiaEmissaoDt.getCodigoGrau() != null && guiaEmissaoDt.getCodigoGrau().equals("3") ) {
								turmaRecursal = true;
							}
							stTemp = processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(),  posicaopaginaatual, turmaRecursal);
						}
					}
					else {
						if( guiaEmissaoDt.isGuiaInicial1Grau() ) {
							stTemp = areaDistribuicaoNe.consultarAreasDistribuicaoCriminalJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(), posicaopaginaatual);
						}
						else {
							stTemp = processoNe.consultarAreasDistribuicaoSegundoGrauCriminalJSON(stNomeBusca1, guiaEmissaoDt.getId_Comarca(), posicaopaginaatual, false);
						}
					}
					
					enviarJSON(response, stTemp);
										
					return;
				}
				break;
			}
			
			// Consulta de Processo Tipo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				
				if( !guiaEmissaoDt.isGrauGuiaInicialMesmoGrauProcessoDependente() ) {
					if( guiaEmissaoDt.getId_AreaDistribuicao() == null || guiaEmissaoDt.getId_AreaDistribuicao().isEmpty() ) {
						throw new MensagemException("Por favor, informe a Área de Distribuição.");
					}
				}
				
				if (request.getParameter("Passo") == null) {
					stAcao = PAGINA_PADRAO_LOCALIZAR;
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe","Código","CNJ"};
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
					
					ProcessoNe processoNe = new ProcessoNe();
					
					if( guiaEmissaoDt.isGrauGuiaInicialMesmoGrauProcessoDependente() ) {
						ProcessoDt processoViculadoDt = guiaEmissaoNe.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente());
						if (processoViculadoDt != null && processoViculadoDt.getId_Serventia() != null) {
							//1 grau
							if( guiaEmissaoDt.getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) ) {
								
									stTemp = processoNe.consultarProcessoTipoServentiaJSON(stNomeBusca1, processoViculadoDt.getId_Serventia() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
							}
							else {
								//2 grau								
								stTemp = processoNe.consultarDescricaoProcessoTipoJSON(stNomeBusca1, processoViculadoDt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
							}
						}
						else {
							request.setAttribute("MensagemErro","Processo não encontrado. Por favor, verifique o número do processo informado.");
						}
					} else {
						if( guiaEmissaoDt.isGuiaInicialTurmaRecursal() ) {
							//Ocorrência 2019/6291
							//Fixar para emitir esta guia somente para a classe "Mandado de Segurança (CF; Lei 12016/2009)" de acordo com o assentamento do dia 25/06/2019 e email do dia 13/06/2019.
							//Ocorrencia 2021/2039
							//Mudei para pesquisar somente "Mandado de Segurança Cível" e criei os modelos para as guias necessárias de primeiro grau. PS.: quando for turma, é gerado os itens pelo método.
							stTemp = processoNe.consultarDescricaoProcessoTipoJSON("Mandado de Segurança Cível", guiaEmissaoDt.getId_AreaDistribuicao() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
						}
						else {
							stTemp = processoNe.consultarDescricaoProcessoTipoJSON(stNomeBusca1, guiaEmissaoDt.getId_AreaDistribuicao() , UsuarioSessao.getUsuarioDt(), posicaopaginaatual);
						}
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
				
				//Busca Bairro
				stId = request.getParameter("tempBuscaId_Bairro");
				if( stId != null ) {
					BairroDt bairroDt = guiaEmissaoNe.consultarBairroId(stId);
					
					BairroGuiaLocomocaoDt bairroLocomocao = new BairroGuiaLocomocaoDt();
					bairroLocomocao.setBairroDt(bairroDt);
					bairroLocomocao.setQuantidade(1);
					bairroLocomocao.setId_Finalidade(guiaEmissaoDt.getId_Finalidade());
					bairroLocomocao.setFinalidade(guiaEmissaoDt.getFinalidade());
					bairroLocomocao.setOficialCompanheiro(guiaEmissaoDt.isOficialCompanheiro());
					bairroLocomocao.setPenhora(guiaEmissaoDt.isPenhora());
					bairroLocomocao.setIntimacao(guiaEmissaoDt.isIntimacao());
					
					if( listaBairroDt != null )
						listaBairroDt.add(bairroLocomocao);
					
					if( listaQuantidadeBairroDt != null )
						listaQuantidadeBairroDt.add("1");
					
					request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
					request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
					
					stId = null;
				}
				
				//Busca Comarca
				stId = request.getParameter("Id_Comarca");
				if( stId != null ) {
					ComarcaDt comarcaDt = guiaEmissaoNe.consultarComarca(stId);
					
					guiaEmissaoDt.setId_Comarca(comarcaDt.getId());
					guiaEmissaoDt.setComarca(comarcaDt.getComarca());
					guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					
					request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
					
					stId = null;
				}
				
				//Busca area de distribuição
				stId = request.getParameter("Id_AreaDistribuicao");
				if( stId != null ) {
					AreaDistribuicaoDt areaDistribuicaoDt = guiaEmissaoNe.consultarAreaDistribuicao(stId);
					
					guiaEmissaoDt.setId_AreaDistribuicao(areaDistribuicaoDt.getId());
					guiaEmissaoDt.setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());
					
					request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
					
					stId = null;
				}
				
				//Busca processo tipo / classe
				stId = request.getParameter("Id_ProcessoTipo");
				if( stId != null ) {
					ProcessoTipoDt processoTipoDt = guiaEmissaoNe.consultarProcessoTipo(stId);
					
					guiaEmissaoDt.setId_ProcessoTipo(processoTipoDt.getId());
					guiaEmissaoDt.setProcessoTipo(processoTipoDt.getProcessoTipo());
					guiaEmissaoDt.setProcessoTipoCodigo(processoTipoDt.getProcessoTipoCodigo());
					
					request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
					
					stId = null;
				}
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected GuiaEmissaoDt limparGuia(GuiaEmissaoDt guiaEmissaoDt, HttpServletRequest request) {
		List listaBairroDt;
		List listaQuantidadeBairroDt;
		
		guiaEmissaoDt.setId(null);
		guiaEmissaoDt.setNumeroGuiaCompleto(null);
		
		guiaEmissaoDt.setRequerente("");
		guiaEmissaoDt.setRequerido("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setAreaDistribuicao("");
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNumeroProcessoDependente("");
		guiaEmissaoDt.setCodigoArea(AreaDt.CIVEL);
		guiaEmissaoDt.setBensPartilhar(GuiaEmissaoDt.VALOR_NAO);
		
		if( guiaEmissaoDt.getCodigoGrau() == null || guiaEmissaoDt.getCodigoGrau().isEmpty() ) {
			guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
		}
		if( guiaEmissaoDt.getCodigoGrau().equals("3") || guiaEmissaoDt.getCodigoGrau().equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU) ) {
			guiaEmissaoDt.setId_Comarca(ComarcaDt.ID_GOIANIA);
			guiaEmissaoDt.setComarca("GOIÂNIA");
			guiaEmissaoDt.setComarcaCodigo(ComarcaDt.GOIANIA);
		}
		else {
			guiaEmissaoDt.setId_Comarca("");
			guiaEmissaoDt.setComarca("");
			guiaEmissaoDt.setComarcaCodigo("");
		}
		
		guiaEmissaoDt.setDistribuidorQuantidade("1");
		guiaEmissaoDt.setContadorQuantidade("1");
		guiaEmissaoDt.setCustasQuantidade("1");
		guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
		guiaEmissaoDt.setEscrivaniaQuantidade("1");
		guiaEmissaoDt.setAtosEscrivaesCivel("1");
		guiaEmissaoDt.setNovoValorAcao("0,00");
		guiaEmissaoDt.setValorAcao("0,00");
		guiaEmissaoDt.setNumeroImpetrantes("0");
		guiaEmissaoDt.setPenhoraQuantidade("1");
		guiaEmissaoDt.setOrigemEstado("");
		guiaEmissaoDt.setProtocoloIntegrado("");
		
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPGCodigo("");
		
		guiaEmissaoDt.setId_AreaDistribuicao("");
		guiaEmissaoDt.setAreaDistribuicao("");
		
		guiaEmissaoDt.setProcessoDt(null);
		
		guiaEmissaoDt.setId_Processo(null);
		guiaEmissaoDt.setId_ProcessoTipo("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setProcessoTipoCodigo("");
		
		guiaEmissaoDt.setId_Serventia(null);
		guiaEmissaoDt.setServentia(null);
		
		guiaEmissaoDt.setRequerente("");
		guiaEmissaoDt.setRequerido("");
		
		listaBairroDt = new ArrayList();
		listaQuantidadeBairroDt = new ArrayList();
		
		request.getSession().removeAttribute("ListaGuiaItemDt");
		request.getSession().removeAttribute("ListaCustaDt");
		request.getSession().removeAttribute("TotalGuia");
		request.getSession().removeAttribute("GuiaEmissaoDtInicial");
		request.getSession().removeAttribute("ListaBairroDt");
		request.getSession().removeAttribute("ListaQuantidadeBairroDt");
		if( request.getSession().getAttribute("guiaInicialPublica") != null && Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()) > 0 ) {
			for( int i = 0; i < Integer.parseInt(request.getSession().getAttribute("guiaInicialPublica").toString()); i++ ) {
				request.getSession().removeAttribute("guiaInicialPublicaQuantidadeLocomocao"+i);
			}
		}
		request.getSession().removeAttribute("guiaInicialPublica");
		
		request.getSession().removeAttribute("processoDt");
		request.getSession().removeAttribute("ProcessoCadastroDt");

		request.getSession().setAttribute("GuiaEmissaoDtInicial", guiaEmissaoDt);
		
		return guiaEmissaoDt;
	}

	protected void preparaItensDeLocomocao(List<BairroGuiaLocomocaoDt> listaBairroIntimacaoDt, List<Integer> listaBairroIntimacaoDtQuantidade, List<GuiaItemDt> listaGuiaItemLocomocaoDt, GuiaEmissaoDt guiaEmissaoDt, boolean contaVinculadaSegundoGrau) throws Exception {
		
		if( listaBairroIntimacaoDt != null && listaBairroIntimacaoDt.size() > 0 ) {
			for(int i = 0; i < listaBairroIntimacaoDt.size(); i++) {
				BairroGuiaLocomocaoDt bairroLocomocao = listaBairroIntimacaoDt.get(i);	
				int quantidade = 1;				
				if (listaBairroIntimacaoDtQuantidade != null && listaBairroIntimacaoDt.size() == listaBairroIntimacaoDt.size() && listaBairroIntimacaoDtQuantidade.get(i).intValue() > 1) { 
					quantidade = listaBairroIntimacaoDtQuantidade.get(i);
				}
				for(int j = 0; j < quantidade; j++) {
					guiaLocomocaoNe.calcularGuiaLocomocaoNOVO(listaGuiaItemLocomocaoDt, bairroLocomocao, guiaEmissaoDt, contaVinculadaSegundoGrau);	
				}				
			}										
		}		
	}
	
	protected String obtenhaAcaoPreviaDeCalculo(HttpServletRequest request) {
		// Parâmetro request utilizado na subclasse GuiaInicialPublicaCt.java
		return PAGINA_GUIA_PREVIA_CALCULO;
	}
	
	protected String obtenhaTituloPagina() {
		//return "Guia Inicial - 1º Grau";
		return "Guia Inicial";
	}
}
