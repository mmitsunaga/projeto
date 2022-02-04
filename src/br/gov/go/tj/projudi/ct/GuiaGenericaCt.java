package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.math.BigDecimal;
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
import br.gov.go.tj.projudi.dt.GuiaGenericaDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ArrecadacaoCustaNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaFinalZeroNe;
import br.gov.go.tj.projudi.ne.GuiaGenericaNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaGenericaCt extends Controle {

	private static final long serialVersionUID = 5825007462655348018L;
	
	private static final String PAGINA_GUIA_GENERICA 		= "/WEB-INF/jsptjgo/GuiaGenerica.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 	= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_LOCALIZAR 			= "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
	
	private GuiaGenericaNe guiaGenericaNe = new GuiaGenericaNe();

	@Override
	public int Permissao() {
		return GuiaGenericaDt.CodigoPermissao;
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
		
		stAcao = PAGINA_GUIA_GENERICA;
		
		//********************************************
		// Vari�veis utilizadas pela p�gina
		List listaCustaDt 		= null;
		List listaBairroDt 		= null;
		List listaBairroLocomocaoContaVinculada = null;
		List listaQuantidadeBairroDt = null;
		List listaQuantidadeBairroLocomocaoContaVinculada = null;
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
		
		listaBairroDt = (List) request.getSession().getAttribute("ListaBairroDt");
		if( listaBairroDt == null ) {
			listaBairroDt = new ArrayList();
		}
		
		listaBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaBairroLocomocaoContaVinculada");
		if( listaBairroLocomocaoContaVinculada == null ) {
			listaBairroLocomocaoContaVinculada = new ArrayList();
		}
		
		listaQuantidadeBairroDt = (List) request.getSession().getAttribute("ListaQuantidadeBairroDt");
		if( listaQuantidadeBairroDt == null ) {
			listaQuantidadeBairroDt = new ArrayList();
		}
		
		listaQuantidadeBairroLocomocaoContaVinculada = (List) request.getSession().getAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
		if( listaQuantidadeBairroLocomocaoContaVinculada == null ) {
			listaQuantidadeBairroLocomocaoContaVinculada = new ArrayList();
		}
		
		//********************************************
		//Requests 
		request.setAttribute("tempPrograma" 			, "Guia de Recolhimento Simplificada");
		request.setAttribute("tempRetorno" 				, "GuiaGenerica");
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, Funcoes.StringToLong(posicaopaginaatual));
		request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
				
				
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
		
		if( request.getParameter("rateioCodigo") != null ) {
			guiaEmissaoDt.setRateioCodigo(request.getParameter("rateioCodigo").toString());
		}
		
//		if( listaBairroDt != null ) {
//			listaQuantidadeBairroDt = new ArrayList();
//			for( int i = 0; i < listaBairroDt.size(); i++ ) {
//				if( request.getParameter("quantidadeLocomocao"+i) != null ) {
//					listaQuantidadeBairroDt.add(request.getParameter("quantidadeLocomocao"+i));
//				}
//			}
//			request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
//		}
		
		
		if( request.getParameter("id_apelante") != null && !(request.getParameter("id_apelante").equals("null")) && request.getParameter("id_apelante").toString().length() > 0 ) {
			guiaEmissaoDt.setId_Apelante(request.getParameter("id_apelante").toString());
		}
		if( request.getParameter("id_apelado") != null && !(request.getParameter("id_apelado").equals("null")) && request.getParameter("id_apelado").toString().length() > 0 ) {
			guiaEmissaoDt.setId_Apelado(request.getParameter("id_apelado").toString());
		}
		if( request.getParameter("codigoOficialSPGLocomocao") != null && !(request.getParameter("codigoOficialSPGLocomocao").equals("null")) && request.getParameter("codigoOficialSPGLocomocao").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocao(request.getParameter("codigoOficialSPGLocomocao").toString());
		}
		if( request.getParameter("codigoOficialSPGLocomocaoContaVinculada") != null && !(request.getParameter("codigoOficialSPGLocomocaoContaVinculada").equals("null")) && request.getParameter("codigoOficialSPGLocomocaoContaVinculada").toString().length() > 0 ) {
			guiaEmissaoDt.setCodigoOficialSPGLocomocaoContaVinculada(request.getParameter("codigoOficialSPGLocomocaoContaVinculada").toString());
		}
		
		//********************************************
		//Pesquisas em Ne auxiliares
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaGenericaNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			if( serventiaDt == null ) {
				serventiaDt = guiaGenericaNe.consultarIdServentia(processoDt.getId_Serventia());
			}
			if( serventiaDt != null ) {
				comarcaDt = guiaGenericaNe.consultarComarca(serventiaDt.getId_Comarca());
				processoDt.setComarca(serventiaDt.getComarca());
				processoDt.setComarcaCodigo(serventiaDt.getComarcaCodigo());
			}
		}
		
		
		
		
		//Valida soma do Rateio das partes, caso tenha sido escolhido
		Double totalRateio = 0.0D;
		List listaGuiasRateio = new ArrayList();
		List listaTotalGuiaRateio = new ArrayList();
		List listaNomeParteGuia = new ArrayList();
		List listaNomePartePorcentagemGuia = new ArrayList();
		if( request.getParameter("rateioCodigo") != null && ( Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaFinalZeroNe.RATEIO_VARIAVEL || Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaFinalZeroNe.RATEIO_50_50 ) ) {
			
			if( processoDt.getListaPolosAtivos() != null && processoDt.getListaPolosAtivos().size() > 0 ) {
				for(int i = 0; i < processoDt.getListaPolosAtivos().size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(i);
					
					//Pega o valor da porcentagem digitada para a parte
					String VARIAVEL_RATEIO_PARTE_VARIAVEL = request.getParameter(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()).toString();
					
					//Soma o valor digitado para validar os 100%
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						totalRateio += Funcoes.StringToDouble(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Seta na variavel de sess�o este valor
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId(), VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Verifica se emiti a guia para esta parte
					String chekboxEmitirGuiaParte[] = request.getParameterValues(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
					if( chekboxEmitirGuiaParte != null ) {
						//Seta se a parte ir� gerar a guia
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId(), chekboxEmitirGuiaParte[0]);
						
						//Para a cria��o das outras guias
						listaNomeParteGuia.add(parteDt.getNome() + ":" + chekboxEmitirGuiaParte[0]);
						listaNomePartePorcentagemGuia.add(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
				}
			}
			if( processoDt.getListaPolosPassivos() != null && processoDt.getListaPolosPassivos().size() > 0 ) {
				for(int i = 0; i < processoDt.getListaPolosPassivos().size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(i);
					
					//Pega o valor da porcentagem digitada para a parte
					String VARIAVEL_RATEIO_PARTE_VARIAVEL = request.getParameter(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()).toString();
					
					//Soma o valor digitado para validar os 100%
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						totalRateio += Funcoes.StringToDouble(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Seta na variavel de sess�o este valor
					if( VARIAVEL_RATEIO_PARTE_VARIAVEL != null ) {
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId(), VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
					
					//Verifica se emiti a guia para esta parte
					String chekboxEmitirGuiaParte[] = request.getParameterValues(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
					if( chekboxEmitirGuiaParte != null ) {
						//Seta se a parte ir� gerar a guia
						request.getSession().setAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId(), chekboxEmitirGuiaParte[0]);
						
						//Para a cria��o das outras guias
						listaNomeParteGuia.add(parteDt.getNome() + ":" + chekboxEmitirGuiaParte[0]);
						listaNomePartePorcentagemGuia.add(VARIAVEL_RATEIO_PARTE_VARIAVEL);
					}
				}
			}
			
			guiaEmissaoDt.setTotalRateio(totalRateio);
		}
		if( guiaEmissaoDt.getRateioCodigo() != null && guiaEmissaoDt.getRateioCodigo().length() > 0 && ( Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalZeroNe.RATEIO_VARIAVEL || Funcoes.StringToInt(guiaEmissaoDt.getRateioCodigo()) == GuiaFinalZeroNe.RATEIO_50_50 ) ) {
			request.setAttribute("visualizaDivRateioPartesVariavel", "DivVisivel");
		}
		if( guiaEmissaoDt.getTotalRateio() == 100.0D ) {
			request.setAttribute("formEdicaoInputSomenteLeituraRateio", "formEdicaoInputSomenteLeitura");
		}
		else {
			request.setAttribute("formEdicaoInputSomenteLeituraRateio", "formEdicaoInputSomenteLeituraVermelho");
		}
		request.setAttribute("totalRateio", totalRateio);
		
		
		
		
		
		switch(paginaatual) {
		
			case Configuracao.Novo: {
				
				
				//*********************************************************
				// Altera磯 soliticitada pela Vanuza ontem para ser igual a 
				// altera磯 da guia de recurso inominado neste mesmo ponto.
				//Abaixo segue o que estᠤocumentado igual na guia de recurso inominado:
				//*********************************************************
				// Conforme ocorrꮣia 2012/84962
				/*
				Foi avisada a fatinha e atrav鳠do email enviado no dia 01/11/12 confirmando esta altera磯.
				Foi adicionado a terceira valida磯 para deixar processo que subiu para a turma com recurso 
				sem guia de recurso inominado. A guia de recurso inominado agora pode ser acessada por um 
				processo que estᠮa turma, DESDE que o recurso tenha como serventia originᲩa alguma serventia 
				relacionada � serventia da contadoria do contador emitente da guia.
				 */
				//*********************************************************
//				if( guiaGenericaNe.consultarAutorizadoEmitirGuiaComarca(UsuarioSessao.getUsuarioDt(), serventiaDt.getId_Comarca()) 
//					|| guiaGenericaNe.consultarAutorizacaoEmitirGuiaRecursoServentiaOrigem(processoDt.getId(), UsuarioSessao.getListaServentiasGruposUsuario(), UsuarioSessao.getUsuarioDt().getId_Serventia()) ) {
					
					stAcao = PAGINA_GUIA_GENERICA;
					
					if( guiaGenericaNe.isConexaoSPG_OK() ) {
						
						guiaEmissaoDt = new GuiaEmissaoDt();
						request.getSession().removeAttribute("ListaGuiaItemDt");
						request.getSession().removeAttribute("ListaCustaDt");
						request.getSession().removeAttribute("TotalGuia");
						request.getSession().removeAttribute("GuiaEmissaoDt");
						request.getSession().removeAttribute("ListaBairroDt");
						request.getSession().removeAttribute("ServentiaDt");
						request.getSession().removeAttribute("ListaBairroLocomocaoContaVinculada");
						request.getSession().removeAttribute("totalRateio");
						request.getSession().removeAttribute("ListaArrecadacaoCustaDt");
						request.getSession().removeAttribute("ListaQuantidadeBairroDt");
						request.getSession().removeAttribute("ListaQuantidadeBairroLocomocaoContaVinculada");
						
						//Tratamento para processos sigilosos e segredo de justi�a
						guiaGenericaNe.tratamentoParaProcessosSigilosoSegredoJustica(processoDt);
						
						List listaPromoventes = processoDt.getListaPolosAtivos();
						List listaPromovidos = processoDt.getListaPolosPassivos();
						if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
							for(int i = 0; i < listaPromoventes.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId());
								}
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
								}
							}
						}
						if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
							for(int i = 0; i < listaPromovidos.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId());
								}
								
								if( request.getSession().getAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId()) != null ) {
									request.getSession().removeAttribute(GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId());
								}
							}
						}
						
						
						
						
						request.getSession().setAttribute("ListaCustaDt", guiaGenericaNe.consultarItensGuia(null, GuiaTipoDt.ID_GUIA_GENERICA, processoDt.getId_ProcessoTipo()));
						
						List listaOficiaisSPGDt = new OficialSPGNe().consultarOficiaisComarca(processoDt.getComarcaCodigo());
						request.getSession().setAttribute("ListaOficiaisSPGDt", listaOficiaisSPGDt);
						
						//Consulta lista de Arrecada��o Custa
						List listaArrecadacaoCustaDt = new ArrecadacaoCustaNe().consultarPorDescricao(null);
						request.getSession().setAttribute("ListaArrecadacaoCustaDt", listaArrecadacaoCustaDt);
						
						//Tipo de cobran�a do ato do escriv�o
						String tipoAtoEscrivaoCivel = guiaGenericaNe.verificarAtoEscrivaoCivel(processoDt.getProcessoTipoCodigo());
						request.getSession().setAttribute("tipoAtoEscrivaoCivel", tipoAtoEscrivaoCivel);
						
						//Atualiza��o do valor da causa
						guiaEmissaoDt.setDataBaseAtualizacao(processoDt.getDataRecebimento());
						guiaEmissaoDt.setDataBaseFinalAtualizacao(processoDt.getDataRecebimento());
						guiaEmissaoDt.setNovoValorAcao( processoDt.getValor() );
						guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaGenericaNe.atualizarValorCausaUFR(processoDt.getValor(), Funcoes.StringToDate(processoDt.getDataRecebimento())).toString()) );
						
						//Inicia custas com 1, a pedido do contador Marcelo
						guiaEmissaoDt.setCustasQuantidade("1");
						guiaEmissaoDt.setRateioCodigo(null);
						
						request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
						
						//Verifica para emitir mensage se j� existe guia do mesmo tipo
						if( guiaGenericaNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), GuiaTipoDt.ID_GUIA_GENERICA) ) {
							request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
						}
						
						request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
					}
					else {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG));
						return;
					}
//				}
//				else {
//					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro="+Configuracao.getMensagem(Configuracao.MENSAGEM_SEM_PERMISSAO_EMITIR_GUIA_PROCESSO));
//				}
				
				break;
			}
			
			case Configuracao.Atualizar : {
				
				if( request.getParameter("novoValorAcao") != null && request.getParameter("novoValorAcao").toString().length() > 0 ) {
					guiaEmissaoDt.setNovoValorAcao(request.getParameter("novoValorAcao").toString());
				}
				
				guiaEmissaoDt.setDataBaseFinalAtualizacao(request.getParameter("dataBaseFinalAtualizacao").toString());
				guiaEmissaoDt.setNovoValorAcaoAtualizado( Funcoes.FormatarDecimal(guiaGenericaNe.atualizarValorCausaUFR(guiaEmissaoDt.getNovoValorAcao(), Funcoes.StringToDate(guiaEmissaoDt.getDataBaseFinalAtualizacao())).toString()) );
				
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
					
					//Utilizado na Volta da Pr�via para a P�gina da Guia
					case Configuracao.Curinga7 : {
						
						if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_VARIAVEL)) || guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_50_50)) ) {
							request.setAttribute("visualizaDivRateioPartesVariavel", "DivVisivel");
						}
						else {
							List listaPromoventes = processoDt.getListaPolosAtivos();
							List listaPromovidos = processoDt.getListaPolosPassivos();
							
							if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
								for(int i = 0; i < listaPromoventes.size(); i++) {
									ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
									
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
								}
							}
							if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
								for(int i = 0; i < listaPromovidos.size(); i++) {
									ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
									
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
									request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
								}
							}
							
							request.setAttribute("visualizaDivRateioPartesVariavel", "DivInvisivel");
						}
						
						break;
					}
					
					case Configuracao.Curinga8: {
						
						Map valoresReferenciaCalculo = new HashMap();
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && 
							!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
							
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							return;
						}
						
						//Retira as outras guias do rateio da sess�o
						List listaPromoventes = processoDt.getListaPolosAtivos();
						List listaPromovidos = processoDt.getListaPolosPassivos();
						if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
							for(int i = 0; i < listaPromoventes.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
								
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
							}
						}
						if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
							for(int i = 0; i < listaPromovidos.size(); i++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
								
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
								request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
							}
						}
						request.getSession().removeAttribute("ListaGuiasRateio");
						request.getSession().removeAttribute("ListaTotalGuiaRateio");
						request.getSession().removeAttribute("ListaNomeParteGuia");
						request.getSession().removeAttribute("ListaNomePartePorcentagemGuia");
						
						guiaGenericaNe.validarSomaRateioPartes(request, guiaEmissaoDt, listaGuiasRateio, listaTotalGuiaRateio, listaNomeParteGuia, listaNomePartePorcentagemGuia, listaPromoventes, listaPromovidos);
						
						
						
						//Escolha do Guia Tipo da guia gen�rica
						if( request.getParameter("idTipoGuia") != null && request.getParameter("idTipoGuia").toString().length() > 0 ) {
							guiaEmissaoDt.setId_GuiaTipo( request.getParameter("idTipoGuia").toString() );
							guiaEmissaoDt.setGuiaTipo( guiaGenericaNe.consultarGuiaTipo( null, guiaEmissaoDt.getId_GuiaTipo() ) );
						}
						
//						//Obtem nova Data base para Atualiza��o do valor da causa
//						if( request.getParameter("dataAtualizacao") != null && request.getParameter("dataAtualizacao").toString().length() > 0 ) {
//							guiaEmissaoDt.setDataAtualizacao(request.getParameter("dataAtualizacao").toString());
//							guiaEmissaoDt.setNovoValorAcao( Funcoes.FormatarDecimal(guiaFinalZeroNe.atualizarValorCausaUFR(guiaEmissaoDt.getNovoValorAcao(), Funcoes.StringToDate(guiaEmissaoDt.getDataAtualizacao())).toString()) );
//						}
						
						//Consulta lista de itens da guia
						//listaItensGuia = guiaFinalNe.consultarItensGuia(guiaEmissaoDt, GuiaTipoDt.GUIA_GENERICA, processoDt.getId_ProcessoTipo());
						listaItensGuia = null;
						
						if( listaItensGuia == null ) {
							GuiaModeloDt guiaModeloDt = new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.ID_GUIA_GENERICA, processoDt.getId_ProcessoTipo());
							guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
						}
						
						List listaVariavelIdArrecadacaoCusta = new ArrayList();
						List listaVariavelQuantidadeArrecadacaoCusta = new ArrayList();
						List listaVariavelValorArrecadacaoCusta = new ArrayList();
						for( int i = 0; i < GuiaGenericaNe.QUANTIDADE_ITENS_CUSTA_GENERICO; i++ ) {
							String variavelIdArrecadacaoCusta 			= "idArrecadacaoCusta_" + i;
							String variavelQuantidadeArrecadacaoCusta 	= "quantidadeArrecadacaoCusta_" + i;
							String variavelValorArrecadacaoCusta 		= "valorArrecadacaoCusta_" + i;
							
							if( request.getParameter(variavelIdArrecadacaoCusta) != null 			&& request.getParameter(variavelIdArrecadacaoCusta).toString().length() > 0 && 
								request.getParameter(variavelQuantidadeArrecadacaoCusta) != null 	&& request.getParameter(variavelQuantidadeArrecadacaoCusta).toString().length() > 0 && 
								request.getParameter(variavelValorArrecadacaoCusta) != null 		&& request.getParameter(variavelValorArrecadacaoCusta).toString().length() > 0 ) {
								
								listaVariavelIdArrecadacaoCusta.add(request.getParameter(variavelIdArrecadacaoCusta).toString());
								listaVariavelQuantidadeArrecadacaoCusta.add(request.getParameter(variavelQuantidadeArrecadacaoCusta).toString());
								listaVariavelValorArrecadacaoCusta.add(request.getParameter(variavelValorArrecadacaoCusta).toString());
								
								
								CustaDt custaDt = new CustaDt();
								custaDt = guiaGenericaNe.consultarCustaDtPorId(String.valueOf(CustaDt.CUSTA_GENERICA));
								
								GuiaCustaModeloDt guiaCustaModeloDt = new GuiaCustaModeloDt();
								
								guiaCustaModeloDt.setCustaDt(custaDt);
								
								if( listaItensGuia == null) {
									listaItensGuia = new ArrayList();
								}
								
								listaItensGuia.add(guiaCustaModeloDt);
							}
						}
						if( listaItensGuia != null && listaItensGuia.size() > 0 ) {
//							List listaAux = guiaGenericaNe.consultarItensGuiaCustaDt(listaCustaDt);
//							if( listaAux != null ) {
//								if( listaItensGuia == null )
//									listaItensGuia = new ArrayList();
//								listaItensGuia.addAll(listaAux);
//							}
							
							valoresReferenciaCalculo.put("ListaVariavelIdArrecadacaoCusta", listaVariavelIdArrecadacaoCusta);
							valoresReferenciaCalculo.put("ListaVariavelQuantidadeArrecadacaoCusta", listaVariavelQuantidadeArrecadacaoCusta);
							valoresReferenciaCalculo.put("ListaVariavelValorArrecadacaoCusta", listaVariavelValorArrecadacaoCusta);
						}
						
						
						//Locomo��o com Zona-Bairro
						List valoresIdBairro = null;
						listaBairroDt = (List)request.getSession().getAttribute("ListaBairroDt");
						listaQuantidadeBairroDt = new ArrayList();
						if( listaBairroDt != null && listaBairroDt.size() > 0 ) {
							if( valoresIdBairro == null )
								valoresIdBairro = new ArrayList();
							
							for(int i = 0; i < listaBairroDt.size(); i++) {
								BairroDt bairroDt = (BairroDt)listaBairroDt.get(i);
								
								int quantidadeLocomocao = Funcoes.StringToInt(request.getParameter("quantidadeLocomocao"+i));
								for(int j = 0; j < quantidadeLocomocao; j++) {
									valoresIdBairro.add(bairroDt.getId());
								}
								
								listaQuantidadeBairroDt.add(String.valueOf(quantidadeLocomocao));
								request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
								
								//Adicionar item de Locomo��o
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								OficialSPGDt oficialSPGAdHoc = null;
								if( guiaEmissaoDt.getCodigoOficialSPGLocomocao() != null && guiaEmissaoDt.getCodigoOficialSPGLocomocao().length() > 0 ) {
									oficialSPGAdHoc = new OficialSPGNe().consultaOficial( guiaEmissaoDt.getCodigoOficialSPGLocomocao() );
								}
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									GuiaCustaModeloDt guiaCustaModeloDt = guiaGenericaNe.adicionarItemLocomocaoOficial(bairroDt);
									if( oficialSPGAdHoc != null ) {
										guiaCustaModeloDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
									}
									listaItensGuia.add( guiaCustaModeloDt );
								}
							}
						}
						
						//Locomo��o com Conta Vinculada
						List valoresIdBairroContaVinculada = null;
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
								
								//Adicionar item de Locomo��o
								if( listaItensGuia == null )
									listaItensGuia = new ArrayList();
								
								OficialSPGDt oficialSPGAdHoc = null;
								if( guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada() != null && guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada().length() > 0 ) {
									oficialSPGAdHoc = new OficialSPGNe().consultaOficial( guiaEmissaoDt.getCodigoOficialSPGLocomocaoContaVinculada() );
								}
								
								for(int j = 0; j < quantidadeLocomocao; j++) {
									GuiaCustaModeloDt guiaCustaModeloDt = guiaGenericaNe.adicionarItemLocomocaoOficialContaVinculada(bairroDt);
									if( oficialSPGAdHoc != null ) {
										guiaCustaModeloDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
									}
									listaItensGuia.add( guiaCustaModeloDt );
								}
							}
						}
						
						
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcao());
						valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcao());
						if( guiaEmissaoDt.getNovoValorAcaoAtualizado().toString().length() == 0 ) {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0.0");
						}
						else {
							valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, guiaEmissaoDt.getNovoValorAcaoAtualizado());
						}
						if( valoresIdBairro != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO, valoresIdBairro);
						if( valoresIdBairroContaVinculada != null )
							valoresReferenciaCalculo.put(CustaDt.LOCOMOCAO_CONTA_VINCULADA, valoresIdBairroContaVinculada);
						
						

						
						
						List listaIdBairroAux = new ArrayList();
						if( valoresIdBairro != null && valoresIdBairro.size() > 0 ) {
							listaIdBairroAux.addAll(valoresIdBairro);
						}
						if( valoresIdBairroContaVinculada != null && valoresIdBairroContaVinculada.size() > 0  ) {
							listaIdBairroAux.addAll(valoresIdBairroContaVinculada);
						}
						boolean bairrosZoneados = guiaGenericaNe.isBairroZoneado(listaIdBairroAux);
						
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = new ArrayList();
						if( bairrosZoneados ) {
							listaGuiaItemDt = guiaGenericaNe.calcularItensGuiaGenerica(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null, null);
						}
						
						if( !bairrosZoneados ) {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BAIRRO_NAO_ZONEADO));
							stAcao = PAGINA_GUIA_GENERICA;
						}
						else {
							if( !guiaGenericaNe.isGuiaZeradaOuNegativa() ) {
								//Outras Guias do Rateio
								if( listaGuiaItemDt != null &&
									listaGuiaItemDt.size() > 0 &&
									
									listaNomeParteGuia != null && 
									listaNomeParteGuia.size() > 0 ) {
									
									for( int i = 0; i < listaNomeParteGuia.size(); i++ ) {
										List listaRateioGuiaItemDt = guiaGenericaNe.calcularItensGuiaRateio(guiaEmissaoDt, listaGuiaItemDt, Funcoes.StringToDouble(listaNomePartePorcentagemGuia.get(i).toString()));
										
										listaGuiasRateio.add( listaRateioGuiaItemDt );
									}
									
									if( listaGuiasRateio.size() > 0 ) {
										for( int m = 0; m < listaGuiasRateio.size(); m++ ) {
											listaTotalGuiaRateio.add(guiaGenericaNe.calcularTotalGuia((List)listaGuiasRateio.get(m)));
										}
										request.getSession().setAttribute("ListaGuiasRateio", listaGuiasRateio);
										request.getSession().setAttribute("ListaTotalGuiaRateio", listaTotalGuiaRateio);
										request.getSession().setAttribute("ListaNomeParteGuia", listaNomeParteGuia);
										request.getSession().setAttribute("ListaNomePartePorcentagemGuia", listaNomePartePorcentagemGuia);
									}
									else {
										request.getSession().removeAttribute("ListaGuiasRateio");
									}
								}
								
								
								//Obtem o id_GuiaModelo
								if( listaItensGuia != null && listaItensGuia.size() > 0) {
									GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
									if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
										guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
								}
								
								
								//Deve haver no m�nimo 1 item de guia
								if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
									
									boolean apresentaPrevia = true;
									if( Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaEmissaoNe.RATEIO_VARIAVEL || Funcoes.StringToInt(request.getParameter("rateioCodigo").toString()) == GuiaEmissaoNe.RATEIO_50_50 ) {
//										if( totalRateio != 100.0D ) {
										if( new BigDecimal(guiaEmissaoDt.getTotalRateio()).compareTo(new BigDecimal(100.D)) != 0 ) {
											request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_TOTAL_RATEIO_ERRADO));
											stAcao = PAGINA_GUIA_GENERICA;
											apresentaPrevia = false;
										}
									}
									if( apresentaPrevia ) {
									
										request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
										request.setAttribute("TotalGuia", guiaGenericaNe.getGuiaCalculoNe().getTotalGuia() );
										
										request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
										request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaGenericaNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
										request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaGenericaNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
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
								}
								else {
									request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
									stAcao = PAGINA_GUIA_GENERICA;
								}
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
								stAcao = PAGINA_GUIA_GENERICA;
							}
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
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaGenericaNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaGenericaNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
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
				
				//Quando tem Rateio
				if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_50_50)) ) {
					
		            GuiaEmissaoDt guiaEmissaoDt2 = (GuiaEmissaoDt) Funcoes.serializeObjeto(guiaEmissaoDt);
		            
					//GuiaEmissaoDt guiaEmissaoDt2 = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
					if( guiaEmissaoDt2 == null )
						guiaEmissaoDt2 = new GuiaEmissaoDt();
					
					//Obt�m o pr�ximo n�mero de Guia
					guiaEmissaoDt2.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt2.setNumeroGuiaCompleto( guiaGenericaNe.getNumeroGuiaCompleto(guiaEmissaoDt2.getNumeroGuia()) );
					
					guiaGenericaNe.calcularRateio50Porcento(guiaEmissaoDt.getListaGuiaItemDt());
					guiaGenericaNe.calcularRateio50Porcento(guiaEmissaoDt2.getListaGuiaItemDt());
					
					guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					guiaEmissaoDt2.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));

					guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
					
					guiaGenericaNe.salvar(guiaEmissaoDt2, guiaEmissaoDt2.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				
				//ProcessoParteTipoCodigo
				if( guiaEmissaoDt.getProcessoParteTipoCodigo() == null ) {
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_100_REQUERENTE)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					}
					if( guiaEmissaoDt.getRateioCodigo().equals(String.valueOf(GuiaEmissaoNe.RATEIO_100_REQUERIDO)) ) {
						guiaEmissaoDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
					}
				}
				
				//Salvar GuiaEmiss�o
				List listaAuxGuiaEmissaoDtRateioImpressao = null;
				if( request.getSession().getAttribute("ListaGuiasRateio") == null && 
					request.getSession().getAttribute("ListaTotalGuiaRateio") == null ) {

					guiaEmissaoDt.setDadosAdicionaisParaLog(this.getClass().toString());
					
					guiaGenericaNe.salvar(guiaEmissaoDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				}
				else {
					//Salvar as guias que teve rateio
					listaGuiasRateio = (List) request.getSession().getAttribute("ListaGuiasRateio");
					listaNomeParteGuia = (List) request.getSession().getAttribute("ListaNomeParteGuia");
					listaNomePartePorcentagemGuia = (List)request.getSession().getAttribute("ListaNomePartePorcentagemGuia");
					
					listaAuxGuiaEmissaoDtRateioImpressao = new ArrayList();
					
					if( listaGuiasRateio != null && listaGuiasRateio.size() > 0 ) {
						for( int i = 0; i < listaGuiasRateio.size(); i++ ) {
							GuiaEmissaoDt auxGuiaEmissaoDt = new GuiaEmissaoDt();
							
							auxGuiaEmissaoDt.copiar(guiaEmissaoDt);
							
							auxGuiaEmissaoDt.setId_ProcessoParteResponsavelGuia(listaNomeParteGuia.get(i).toString().split(":")[1]);
							
							for(int contadorPromoventes = 0; contadorPromoventes < processoDt.getListaPolosAtivos().size(); contadorPromoventes++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(contadorPromoventes);
								if( parteDt.getId().equals(auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia()) ) {
									
									auxGuiaEmissaoDt.setRequerente(guiaEmissaoDt.getNomeParte( auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) + " (" + listaNomePartePorcentagemGuia.get(i) +"%)");
									auxGuiaEmissaoDt.setRequerido(auxGuiaEmissaoDt.getNomePrimeiroRequerido());
									
									break;
								}
							}
							for(int contadorPromovidos = 0; contadorPromovidos < processoDt.getListaPolosPassivos().size(); contadorPromovidos++) {
								ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(contadorPromovidos);
								if( parteDt.getId().equals(auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia()) ) {
									
									auxGuiaEmissaoDt.setRequerente(auxGuiaEmissaoDt.getNomePrimeiroRequerente());
									auxGuiaEmissaoDt.setRequerido(guiaEmissaoDt.getNomeParte( auxGuiaEmissaoDt.getId_ProcessoParteResponsavelGuia() ) + " (" + listaNomePartePorcentagemGuia.get(i) +"%)");
									
									break;
								}
							}
							
							auxGuiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
							auxGuiaEmissaoDt.setNumeroGuiaCompleto( guiaGenericaNe.getNumeroGuiaCompleto(auxGuiaEmissaoDt.getNumeroGuia()) );
							auxGuiaEmissaoDt.setListaGuiaItemDt( (List) listaGuiasRateio.get(i) );
							auxGuiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
							auxGuiaEmissaoDt.setServentia(processoDt.getServentia());
							auxGuiaEmissaoDt.setComarca(processoDt.getComarca());
							auxGuiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
							auxGuiaEmissaoDt.setValorAcao(processoDt.getValor());
							auxGuiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
							auxGuiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
							auxGuiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
							auxGuiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
							auxGuiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
							auxGuiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
							auxGuiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
							auxGuiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
							
							guiaGenericaNe.salvar(auxGuiaEmissaoDt, (List) listaGuiasRateio.get(i), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
							
							listaAuxGuiaEmissaoDtRateioImpressao.add(auxGuiaEmissaoDt);
						}
					}
				}
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				switch(passoEditar) {
				
					case Configuracao.Imprimir: {
						
						if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
							for( int i = 0; i < processoDt.getListaPolosAtivos().size(); i++ ) {
								ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosAtivos().get(i);
								
								if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
									guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
								}
							}
							for( int i = 0; i < processoDt.getListaPolosPassivos().size(); i++ ) {
								ProcessoParteDt parteDt = (ProcessoParteDt)processoDt.getListaPolosPassivos().get(i);
								
								if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
									guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
								}
							}
						}
						
						//Gera��o da guia PDF
						byte[] byTemp = guiaGenericaNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, GuiaTipoDt.ID_GUIA_GENERICA, "GUIA DE RECOLHIMENTO SIMPLIFICADA");
						
						String nome="GuiaGenerica_Processo_"+ guiaEmissaoDt.getNumeroProcesso() ;
						
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
						request.getSession().removeAttribute("ListaArrecadacaoCustaDt");
						
						request.getSession().removeAttribute("ListaGuiasRateio");
						request.getSession().removeAttribute("ListaTotalGuiaRateio");
						request.getSession().removeAttribute("ListaNomeParteGuia");
						request.getSession().removeAttribute("ListaNomePartePorcentagemGuia");
						
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=Guia Emitida com Sucesso!");
						return;
					}
				}
				
				//Retira as outras guias do rateio da sess�o
				List listaPromoventes = processoDt.getListaPolosAtivos();
				List listaPromovidos = processoDt.getListaPolosPassivos();
				if( listaPromoventes != null && listaPromoventes.size() > 0 ) {
					for(int i = 0; i < listaPromoventes.size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
						
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
					}
				}
				if( listaPromovidos != null && listaPromovidos.size() > 0 ) {
					for(int i = 0; i < listaPromovidos.size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
						
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_RATEIO_PARTE_VARIAVEL + parteDt.getId() );
						request.getSession().removeAttribute( GuiaEmissaoNe.VARIAVEL_EMITIR_GUIA_PARTE + parteDt.getId() );
					}
				}
				request.getSession().removeAttribute("totalRateio");
				
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
					request.setAttribute("tempRetorno","GuiaGenerica");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaGenericaNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			//Busca de Bairro Locomo��o Conta Vinculada
			case ( BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1 ) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					stAcao = PAGINA_LOCALIZAR;
					request.setAttribute("tempBuscaId","tempBuscaId_BairroLocomocaoContaVinculada");
					request.setAttribute("tempBuscaDescricao","Bairro");
					request.setAttribute("tempBuscaPrograma","Bairro");		
					request.setAttribute("tempRetorno","GuiaGenerica");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar + 1));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = guiaGenericaNe.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, posicaopaginaatual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			}
			
			default : {
				//Busca Custa
				stId = request.getParameter("tempBuscaId_Custa");
				if( stId != null ) {
					CustaDt custaDt = guiaGenericaNe.consultarCustaDtPorId(stId);
					
					boolean adicionar = true;
					
					if( listaCustaDt != null && listaCustaDt.size() > 0 ) {
						for( int i = 0; i < listaCustaDt.size(); i++ ) {
							CustaDt custaDtAux = (CustaDt) listaCustaDt.get(i);
							if( custaDt.getId().equals(custaDtAux.getId()) ) {
								adicionar = false;
								break;
							}
						}
					}
					
					if( adicionar ) {
						if( listaCustaDt != null )
							listaCustaDt.add(custaDt);
					}
					else {
						request.setAttribute("MensagemErro", "Este item de custa j� foi adicionado na lista.");
					}
					
					request.getSession().setAttribute("ListaCustaDt", listaCustaDt);
					
					stId = null;
				}
				
				//Busca Bairro
				stId = request.getParameter("tempBuscaId_Bairro");
				if( stId != null ) {
					BairroDt bairroDt = guiaGenericaNe.consultarBairroId(stId);
					
					if( listaBairroDt != null )
						listaBairroDt.add(bairroDt);
					
					if( listaQuantidadeBairroDt != null )
						listaQuantidadeBairroDt.add("1");
					
					request.getSession().setAttribute("ListaBairroDt", listaBairroDt);
					request.getSession().setAttribute("ListaQuantidadeBairroDt", listaQuantidadeBairroDt);
					
					stId = null;
				}
				
				//Busca Bairro Locomo��o Conta Vinculada
				stId = request.getParameter("tempBuscaId_BairroLocomocaoContaVinculada");
				if( stId != null ) {
					BairroDt bairroDt = guiaGenericaNe.consultarBairroId(stId);
					
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
