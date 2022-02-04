package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import br.gov.go.tj.projudi.dt.GuiaCartaPrecatoriaDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.AssuntoNe;
import br.gov.go.tj.projudi.ne.FinalidadeNe;
import br.gov.go.tj.projudi.ne.GuiaCartaPrecatoriaNe;
import br.gov.go.tj.projudi.ne.GuiaFinalNe;
import br.gov.go.tj.projudi.ne.GuiaFinalidadeModeloNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaCartaPrecatoriaCt extends Controle {

	private static final long serialVersionUID = 5522659051373116998L;
	
	private static final String PAGINA_GUIA_CARTA_PRECATORIA	= "/WEB-INF/jsptjgo/GuiaCartaPrecatoria.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 		= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_LOCALIZAR 				= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	private GuiaCartaPrecatoriaNe guiaCartaPrecatoriaNe = new GuiaCartaPrecatoriaNe();

	@Override
	public int Permissao() {
		return GuiaCartaPrecatoriaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		String stAcao 		= null;
		int passoEditar 	= -1;
		List tempList 		= null;		
		String stId 		= "";

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		stAcao = PAGINA_GUIA_CARTA_PRECATORIA;
		
		//********************************************
		// Variï¿½veis utilizadas pela pï¿½gina
		List listaCustaDt 		= null;
		List listaBairroDt 		= null;
		List listaBairroLocomocaoPenhora = null;
		List listaBairroLocomocaoContaVinculada = null;
		List listaQuantidadeBairroLocomocaoContaVinculada = null;
		List listaQuantidadeLocomocaoPenhora = null;
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
		
		//Verifica se tem finalidade para setar a ListaCustaDt
		if (guiaEmissaoDt.getId_Finalidade() != null && guiaEmissaoDt.getId_Finalidade().length() > 0){
			GuiaFinalidadeModeloDt guiaFinalidadeTeste = new GuiaFinalidadeModeloNe().consultarGuiaFinalidadeModelo(GuiaTipoDt.ID_CARTA_PRECATORIA, guiaEmissaoDt.getId_Finalidade());
			if(guiaFinalidadeTeste == null){
				request.getSession().setAttribute("ListaCustaDt", guiaCartaPrecatoriaNe.consultarItensGuia(null, GuiaTipoDt.ID_CARTA_PRECATORIA, processoDt.getId_ProcessoTipo()));
			} else {
				request.getSession().setAttribute("ListaCustaDt", guiaCartaPrecatoriaNe.consultarItensGuiaFinalidade(GuiaTipoDt.ID_CARTA_PRECATORIA, guiaEmissaoDt.getId_Finalidade()));
			}
		}
		
		listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
		if( listaCustaDt == null ) {
			listaCustaDt = new ArrayList();
		}
		
		listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
		if( listaBairroDt == null ) {
			listaBairroDt = new ArrayList();
		}
		
		listaBairroLocomocaoPenhora = (List) request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
		if( listaBairroLocomocaoPenhora == null ) {
			listaBairroLocomocaoPenhora = new ArrayList();
		}
		
		listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
		if( listaBairroLocomocaoContaVinculada == null ) {
			listaBairroLocomocaoContaVinculada = new ArrayList();
		}
		
		listaQuantidadeLocomocaoPenhora = (List) request.getSession().getAttribute("ListaQuantidadeLocomocaoPenhora");
		if( listaQuantidadeLocomocaoPenhora == null ) {
			listaQuantidadeLocomocaoPenhora = new ArrayList();
		}
		
		listaQuantidadeBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
		if( listaQuantidadeBairroLocomocaoContaVinculada == null ) {
			listaQuantidadeBairroLocomocaoContaVinculada = new ArrayList();
		}
		
		//********************************************
		//Requests 
		request.setAttribute("tempPrograma" 			, "Guia Carta Precatï¿½ria");
		request.setAttribute("tempRetorno" 				, "GuiaCartaPrecatoria");
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		
		if(request.getParameter("nomeBusca1") != null) 
			stNomeBusca1 = request.getParameter("nomeBusca1");
		
		if(request.getParameter("nomeBusca2") != null) 
			stNomeBusca2 = request.getParameter("nomeBusca2");
		
		if(request.getParameter("nomeBusca3") != null) 
			stNomeBusca3 = request.getParameter("nomeBusca3");
		
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
		
		guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.ID_CARTA_PRECATORIA);
		
		if (request.getParameter("codigoFinalidade") != null && request.getParameter("codigoFinalidade").toString().length() > 0 ) {
			guiaEmissaoDt.setId_Finalidade(request.getParameter("codigoFinalidade").toString());
		}
		
		if( request.getParameter("codigoOficialSPGPenhora") != null && request.getParameter("codigoOficialSPGPenhora").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGPenhora(request.getParameter("codigoOficialSPGPenhora").toString());
		}
		
		if( request.getParameter("codigoOficialSPGLocomocao") != null && request.getParameter("codigoOficialSPGLocomocao").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocao(request.getParameter("codigoOficialSPGLocomocao").toString());
		}
		
		if( request.getParameter("codigoOficialSPGLeilao") != null && request.getParameter("codigoOficialSPGLeilao").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLeilao(request.getParameter("codigoOficialSPGLeilao").toString());
		}
		
		if( request.getParameter("codigoOficialSPGLocomocaoContaVinculada") != null && request.getParameter("codigoOficialSPGLocomocaoContaVinculada").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocaoContaVinculada(request.getParameter("codigoOficialSPGLocomocaoContaVinculada").toString());
		}
		
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaCartaPrecatoriaNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt != null ) {
				if( serventiaDt.getId_Comarca() != null ) {
					comarcaDt = guiaCartaPrecatoriaNe.consultarComarca(serventiaDt.getId_Comarca());
				}
				processoDt.setComarca(serventiaDt.getComarca());
				processoDt.setComarcaCodigo(serventiaDt.getComarcaCodigo());
			}
		}
		
		switch(paginaatual) {
		
			case Configuracao.Novo: {
				
				if( guiaCartaPrecatoriaNe.validaAcessoEmissaoGuiaCartaPrecatoria(processoDt.getProcessoTipoCodigo()) && 
					!guiaCartaPrecatoriaNe.isProcessoSegundoGrau(serventiaDt.getId()) ) {
					
					stAcao = PAGINA_GUIA_CARTA_PRECATORIA;
					
					guiaEmissaoDt = new GuiaEmissaoDt();
					request.getSession().removeAttribute("ListaGuiaItemDt");
					request.getSession().removeAttribute("ListaCustaDt");
					request.getSession().removeAttribute("TotalGuia");
					request.getSession().removeAttribute("GuiaEmissaoDt");
					request.getSession().removeAttribute("ListaQuantidadeLocomocaoPenhora");
					request.getSession().removeAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
					
					Integer idFinalidade = 0;
					//TODO: Colocar essa logica no Ne.
					if (processoDt.getListaAssuntos() != null && !processoDt.getListaAssuntos().isEmpty()){
						AssuntoNe assuntoNe = new AssuntoNe();
						ProcessoAssuntoDt processoAssuntoDt =  processoDt.getPrimeiroAssuntoLista();
						if (processoAssuntoDt!=null && processoAssuntoDt.isProcessoAssunto()){
							idFinalidade = Funcoes.StringToInt(assuntoNe.consultarId(processoAssuntoDt.getId_Assunto()).getAssuntoCodigo()) - GuiaCartaPrecatoriaDt.FAIXA_CODIGO_FINALIDADE;
						}
					}
				
					
					
					//******************************************************************
					//******************************************************************
					//******************************************************************
					// CONTINUA?
					//******************************************************************
					//******************************************************************
					
					stAcao = PAGINA_GUIA_CARTA_PRECATORIA;
					
					if( guiaCartaPrecatoriaNe.isConexaoSPG_OK() ) {
						
						guiaEmissaoDt = new GuiaEmissaoDt();
						if (idFinalidade >= GuiaCartaPrecatoriaDt.FINALIDADE_MINIMA && idFinalidade <= GuiaCartaPrecatoriaDt.FINALIDADE_MAXIMA){
							guiaEmissaoDt.setId_Finalidade(String.valueOf(idFinalidade));
						}
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						request.getSession().removeAttribute("ListaBairroDt");
						request.getSession().removeAttribute("ServentiaDt");
						request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
						request.getSession().removeAttribute("ListaBairroLocomocaoPenhora");
						
						request.getSession().setAttribute("ListaCustaDt", guiaCartaPrecatoriaNe.consultarItensGuia(null, GuiaTipoDt.ID_CARTA_PRECATORIA, processoDt.getId_ProcessoTipo()));
						
						List listaOficiaisSPGDt = new OficialSPGNe().consultarOficiaisComarca(processoDt.getComarcaCodigo());
						request.getSession().setAttribute("ListaOficiaisSPGDt", listaOficiaisSPGDt);
						
						//Traz a lista de finalidades
						List listaFinalidadesDt = new FinalidadeNe().consultarFinalidades();
						request.getSession().setAttribute("ListaFinalidadesDt", listaFinalidadesDt);
						
						//Tipo de cobranï¿½a do ato do escrivï¿½o
						String tipoAtoEscrivaoCivel = guiaCartaPrecatoriaNe.verificarAtoEscrivaoCivel(processoDt.getProcessoTipoCodigo());
						request.getSession().setAttribute("tipoAtoEscrivaoCivel", tipoAtoEscrivaoCivel);
						
						//Atualizaï¿½ï¿½o do valor da causa
						guiaEmissaoDt.setDataBaseAtualizacao(processoDt.getDataRecebimento());
						guiaEmissaoDt.setDataBaseFinalAtualizacao(Funcoes.dateToStringSoData(new Date()));
						guiaEmissaoDt.setNovoValorAcao( processoDt.getValor() );
						guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaCartaPrecatoriaNe.atualizarValorCausaUFR(processoDt.getValor(), Funcoes.StringToDate(processoDt.getDataRecebimento())).toString()) );
						
						guiaEmissaoDt.setDistribuidorQuantidade("1");
						guiaEmissaoDt.setContadorQuantidade("1");
						
						//Verifica para emitir mensage se jï¿½ existe guia do mesmo tipo
						if( guiaCartaPrecatoriaNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_CARTA_PRECATORIA) ) {
							request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
						}
						
						request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					}
					else {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
						return;
					}
					//******************************************************************
					//******************************************************************
					//******************************************************************
					//******************************************************************
					//******************************************************************
					
				}
				else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Atenção! Sem permissão para emitir guia de <b>CARTA PRECATÓRIA</b> para este processo e esta serventia.");
					return;
				}
				
				break;
			}
			
			case Configuracao.Atualizar : {
				
				if( request.getParameter("novoValorAcao") != null && request.getParameter("novoValorAcao").toString().length() > 0 ) {
					guiaEmissaoDt.setNovoValorAcao(request.getParameter("novoValorAcao").toString());
				}
				
				guiaEmissaoDt.setDataBaseFinalAtualizacao(request.getParameter("dataBaseFinalAtualizacao").toString());
				guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaCartaPrecatoriaNe.atualizarValorCausaUFR(guiaEmissaoDt.getNovoValorAcao(), Funcoes.StringToDate(guiaEmissaoDt.getDataBaseFinalAtualizacao())).toString()) );
				
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
						
						//Consulta lista de itens da guia
						//listaItensGuia = guiaFinalNe.consultarItensGuia(guiaEmissaoDt, GuiaTipoDt.CARTA_PRECATORIA, processoDt.getId_ProcessoTipo());
						listaItensGuia = null;
						
						if( listaItensGuia == null ) {
							if (guiaEmissaoDt.getId_Finalidade() != null && guiaEmissaoDt.getId_Finalidade().length() > 0){
								GuiaFinalidadeModeloDt guiaFinalidadeModeloDt = new GuiaFinalidadeModeloNe().consultarGuiaFinalidadeModelo(GuiaTipoDt.ID_CARTA_PRECATORIA, guiaEmissaoDt.getId_Finalidade());
								if(guiaFinalidadeModeloDt != null) {
									guiaEmissaoDt.setGuiaFinalidadeModeloDt(guiaFinalidadeModeloDt);
								} else {
									GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_CARTA_PRECATORIA, processoDt.getId_ProcessoTipo());
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
								}
							}
						}
						
						if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
							List listaAux = guiaCartaPrecatoriaNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
							if( listaAux != null ) {
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								listaItensGuia.addAll(listaAux);
							}
						}
						
						//ValoresIdBairro das Locomoï¿½ï¿½es
						List valoresIdBairro = null;
						List valoresIdBairroContaVinculada = null;
						List valoresIdBairroPenhora = null;
						
						//Locomoï¿½ï¿½o com Zona-Bairro
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
								
								if (i == 0 && guiaEmissaoDt.getGuiaFinalidadeModeloDt() != null){
									// Acrescimo Locomoï¿½ï¿½o
									if (Funcoes.StringToInt(guiaEmissaoDt.getGuiaFinalidadeModeloDt().getAcrescimoLocomocao()) > 0){
										listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoOficial(bairroDt) );
										
										if( valoresIdBairroContaVinculada == null )
											valoresIdBairroContaVinculada = new ArrayList();
										
										valoresIdBairroContaVinculada.add(bairroDt.getId());
										listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoOficialContaVinculada(bairroDt) );
									}
									// Acrescimo Penhora
									if (Funcoes.StringToInt(guiaEmissaoDt.getGuiaFinalidadeModeloDt().getPenhoraLocomocao()) > 0){
										if( valoresIdBairroPenhora == null )
											valoresIdBairroPenhora = new ArrayList();
										
										if( valoresIdBairroContaVinculada == null )
											valoresIdBairroContaVinculada = new ArrayList();
										
										valoresIdBairroPenhora.add(bairroDt.getId());
										valoresIdBairroContaVinculada.add(bairroDt.getId());
										
										listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoPenhora(bairroDt) );
										listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoOficialContaVinculada(bairroDt) );
									}
								}
								listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoOficial(bairroDt) );
							}
						}
						
						//Locomoï¿½ï¿½o com Conta Vinculada
						listaBairroLocomocaoContaVinculada = (List)request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
						listaQuantidadeBairroLocomocaoContaVinculada = new ArrayList();
						if( listaBairroLocomocaoContaVinculada != null && listaBairroLocomocaoContaVinculada.size() > 0 ) {
							if( valoresIdBairroContaVinculada == null )
								valoresIdBairroContaVinculada = new ArrayList();
							
							for(int i = 0; i < listaBairroLocomocaoContaVinculada.size(); i++) {
								BairroDt bairroDt = (BairroDt)listaBairroLocomocaoContaVinculada.get(i);
								
								int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocaoContaVinculada"+i));
								for(int j = 0; j < quantidadeLocomocao; j++) {
									valoresIdBairroContaVinculada.add(bairroDt.getId());
								}
								
								listaQuantidadeBairroLocomocaoContaVinculada.add(String.valueOf(quantidadeLocomocao));
								request.getSession().setAttribute("ListaQuantidadeBairroLocomocaoContaVinculada", listaQuantidadeBairroLocomocaoContaVinculada);
								
								//Adicionar item de Locomoï¿½ï¿½o
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoOficialContaVinculada(bairroDt) );
								}
							}
						}
						
						//Locomoï¿½ï¿½o Penhora
						listaBairroLocomocaoPenhora = (List)request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
						listaQuantidadeLocomocaoPenhora = new ArrayList();
						if( listaBairroLocomocaoPenhora != null && listaBairroLocomocaoPenhora.size() > 0 ) {
							if( valoresIdBairroPenhora == null )
								valoresIdBairroPenhora = new ArrayList();
							
							for(int i = 0; i < listaBairroLocomocaoPenhora.size(); i++) {
								BairroDt bairroDt = (BairroDt)listaBairroLocomocaoPenhora.get(i);
								
								int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocaoPenhora"+i));
								for(int j = 0; j < quantidadeLocomocao; j++) {
									valoresIdBairroPenhora.add(bairroDt.getId());
								}
								
								listaQuantidadeLocomocaoPenhora.add(String.valueOf(quantidadeLocomocao));
								request.getSession().setAttribute("ListaQuantidadeLocomocaoPenhora", listaQuantidadeLocomocaoPenhora);
								
								//Adicionar item de Locomoï¿½ï¿½o
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									listaItensGuia.add( guiaCartaPrecatoriaNe.adicionarItemLocomocaoPenhora(bairroDt) );
								}
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
						if( valoresIdBairroContaVinculada != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_CONTA_VINCULADA, valoresIdBairroContaVinculada);
						if( valoresIdBairroPenhora != null)
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_PENHORA, valoresIdBairroPenhora);
						
						
						
						List listaIdBairroAux = new ArrayList();
						if( valoresIdBairro != null && valoresIdBairro.size() > 0 ) {
							listaIdBairroAux.addAll(valoresIdBairro);
						}
						if( valoresIdBairroContaVinculada != null && valoresIdBairroContaVinculada.size() > 0  ) {
							listaIdBairroAux.addAll(valoresIdBairroContaVinculada);
						}
						if( valoresIdBairroPenhora != null && valoresIdBairroPenhora.size() > 0  ) {
							listaIdBairroAux.addAll(valoresIdBairroPenhora);
						}
						boolean bairrosZoneados = guiaCartaPrecatoriaNe.isBairroZoneado(listaIdBairroAux);
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = new ArrayList();
						if( bairrosZoneados ) {
							listaGuiaItemDt = guiaCartaPrecatoriaNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						}
						
						
						if( !guiaCartaPrecatoriaNe.isGuiaZeradaOuNegativa() ) {
							
							//Obtem o id_GuiaModelo
							if( listaItensGuia != null && listaItensGuia.size() > 0) {
								GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
								if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
									guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
							}
							
							
							if( !bairrosZoneados ) {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
								stAcao = PAGINA_GUIA_CARTA_PRECATORIA;
							}
							else {
								//Deve haver no mï¿½nimo 1 item de guia
								if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
									
									request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
									request.setAttribute("TotalGuia", guiaCartaPrecatoriaNe.getGuiaCalculoNe().getTotalGuia() );
									
									request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
									request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaCartaPrecatoriaNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
									request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaCartaPrecatoriaNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
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
									stAcao = PAGINA_GUIA_CARTA_PRECATORIA;
								}
							}
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
							stAcao = PAGINA_GUIA_CARTA_PRECATORIA;
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
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaCartaPrecatoriaNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
					
					//Remover Item de Bairro
					case Configuracao.Curinga7 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
							//listaQuantidadeBairroDt = (List) request.getSession().getAttribute("ListaQuantidadeBairroDt");
							if( listaBairroDt.size() > 0 ) {
								listaBairroDt.remove(posicaoListaBairroExcluir);
								//listaQuantidadeBairroDt.remove(posicaoListaBairroExcluir);
								
								request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
								//request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
							}
						}
						
						break;
					}
					
					//Remover Item de Bairro Conta Vinculada
					case Configuracao.Curinga8 : {
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroContaVinculadaExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
							listaQuantidadeBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
							if( listaBairroLocomocaoContaVinculada.size() > 0 ) {
								listaBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								listaQuantidadeBairroLocomocaoContaVinculada.remove(posicaoListaBairroExcluir);
								
								request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
								request.getSession().setAttribute("ListaQuantidadeBairroLocomocaoContaVinculada", listaQuantidadeBairroLocomocaoContaVinculada);
							}
						}
						
						break;
					}
					
					//Remover Locomoï¿½ï¿½o Penhora
					case Configuracao.Curinga9 : {
						
						int posicaoListaBairroExcluir = Funcoes.StringToInt(request.getParameter("posicaoListaBairroExcluir"));
						if( posicaoListaBairroExcluir != -1 ) {
							
							listaBairroLocomocaoPenhora = (List) request.getSession().getAttribute("ListaBairroLocomocaoPenhora");
							listaQuantidadeLocomocaoPenhora = (List) request.getSession().getAttribute("ListaQuantidadeLocomocaoPenhora");
							
							if( listaBairroLocomocaoPenhora.size() > 0 ) {
								listaBairroLocomocaoPenhora.remove(posicaoListaBairroExcluir);
								listaQuantidadeLocomocaoPenhora.remove(posicaoListaBairroExcluir);
								
								request.getSession().setAttribute("ListaBairroLocomocaoPenhora", listaBairroLocomocaoPenhora);
								request.getSession().setAttribute("ListaQuantidadeLocomocaoPenhora", listaQuantidadeLocomocaoPenhora);
							}
						}
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
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaCartaPrecatoriaNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				
				//ProcessoParteTipoCodigo
				if( guiaEmissaoDt.getProcessoParteTipoCodigo() == null ) {
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalNe.RATEIO_100_REQUERENTE)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					}
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaFinalNe.RATEIO_100_REQUERIDO)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
					}
				}
				
				//Salvar GuiaEmissï¿½o
				guiaCartaPrecatoriaNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						//Geraï¿½ï¿½o da guia PDF
						byte[] byTemp = guiaCartaPrecatoriaNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_CARTA_PRECATORIA, "CARTA PRECATï¿½RIA");
						
						String nome="GuiaCartaPrecatoria_Processo_"+ guiaEmissaoDt.getNumeroProcesso();

						enviarPDF(response, byTemp, nome);
						
						return;
					}
					
					case Configuracao.Salvar : {
						
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						request.getSession().removeAttribute("ListaBairroDt");
						request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
						
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=Guia Emitida com Sucesso!");
						return;
					}
				}
				break;
			}
			
			//Busca de Bairro
			case ( BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar ) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_Bairro");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno","GuiaCartaPrecatoria");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					String stTemp="";
					stTemp = guiaCartaPrecatoriaNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			}
			
			//Busca de Bairro Locomoï¿½ï¿½o Penhora
			case (Configuracao.Curinga9) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_BairroLocomocaoPenhora");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno","GuiaCartaPrecatoria");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Curinga9));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaCartaPrecatoriaNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			//Busca de Bairro Locomoï¿½ï¿½o Conta Vinculada
			case ( BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1 ) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_BairroLocomocaoContaVinculada");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno","GuiaCartaPrecatoria");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1 ));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaCartaPrecatoriaNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			default : {
				//Busca Custa
				stId = request.getParameter("tempBuscaId_Custa");
				if( stId != null ) {
					CustaDt custaDt = guiaCartaPrecatoriaNe.consultarCustaDtPorId(stId);
					
					if( listaCustaDt != null )
						listaCustaDt.add(custaDt);
					
					request.getSession().setAttribute("ListaCustaDt", listaCustaDt);
					
					stId = null;
				}
				
				//Busca Bairro
				stId = request.getParameter("tempBuscaId_Bairro");
				if( stId != null ) {
					BairroDt bairroDt = guiaCartaPrecatoriaNe.consultarBairroId(stId);
					
					if( listaBairroDt != null )
						listaBairroDt.add(bairroDt);
					
					request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
					
					stId = null;
				}
				
				//Busca Bairro Locomoï¿½ï¿½o Penhora
				stId = request.getParameter("tempBuscaId_BairroLocomocaoPenhora");
				if( stId != null ) {
					BairroDt bairroDt = guiaCartaPrecatoriaNe.consultarBairroId(stId);
					
					if( listaBairroLocomocaoPenhora != null )
						listaBairroLocomocaoPenhora.add(bairroDt);
					
					if( listaQuantidadeLocomocaoPenhora != null )
						listaQuantidadeLocomocaoPenhora.add("1");
					
					request.getSession().setAttribute("ListaBairroLocomocaoPenhora", listaBairroLocomocaoPenhora);
					request.getSession().setAttribute("ListaQuantidadeLocomocaoPenhora", listaQuantidadeLocomocaoPenhora);
					
					stId = null;
				}
				
				//Busca Bairro Locomoï¿½ï¿½o Conta Vinculada
				stId = request.getParameter("tempBuscaId_BairroLocomocaoContaVinculada");
				if( stId != null ) {
					BairroDt bairroDt = guiaCartaPrecatoriaNe.consultarBairroId(stId);
					
					if( listaBairroLocomocaoContaVinculada != null )
						listaBairroLocomocaoContaVinculada.add(bairroDt);
					
					if( listaQuantidadeBairroLocomocaoContaVinculada != null )
						listaQuantidadeBairroLocomocaoContaVinculada.add("1");
					
					request.getSession().setAttribute("ListaBairroLocomocaoContaVinculada", listaBairroLocomocaoContaVinculada);
					request.getSession().setAttribute("ListaQuantidadeBairroLocomocaoContaVinculada", listaQuantidadeBairroLocomocaoContaVinculada);
					
					stId = null;
				}
				
				break;
			}
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
