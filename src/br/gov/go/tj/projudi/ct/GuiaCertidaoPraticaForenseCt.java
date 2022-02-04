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

import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.GuiaCertidaoGeralDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.CustaValorNe;
import br.gov.go.tj.projudi.ne.GuiaCertidaoGeralNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaCertidaoPraticaForenseCt extends Controle {

	private static final long serialVersionUID = -3929235942800870802L;
	
	private static final String PAGINA_GUIA_CERTIDAO_PRATICA_FORENSE = "/WEB-INF/jsptjgo/GuiaCertidaoPraticaForense.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO = "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";

	@Override
	public int Permissao() {
		return GuiaCertidaoGeralDt.CodigoPermissaoPraticaForense;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String stAcao = null;
		int passoEditar = -1;
		stAcao = obtenhaPaginaPrincipal();
		
		List listaCustaDt = null;
		List listaItensGuia = null;
		
		GuiaEmissaoDt guiaEmissaoDt = null;
		GuiaCertidaoGeralDt guiaCertidaoGeralDt = null;
		GuiaCertidaoGeralNe guiaCertidaoGeralNe = null;
		
		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
		if( guiaEmissaoDt == null ) {
			guiaEmissaoDt = new GuiaEmissaoDt();
		}
		guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		guiaCertidaoGeralNe = (GuiaCertidaoGeralNe) request.getSession().getAttribute("guiaCertidaoGeralNe");
		if (guiaCertidaoGeralNe == null){
			guiaCertidaoGeralNe = new GuiaCertidaoGeralNe();
		}
		
		guiaCertidaoGeralDt = (GuiaCertidaoGeralDt) request.getSession().getAttribute("guiaCertidaoGeralDt");
		if (guiaCertidaoGeralDt == null){
			guiaCertidaoGeralDt = new GuiaCertidaoGeralDt();
		}
		
		listaItensGuia = (List) request.getSession().getAttribute("ListaItensGuia");
		if( listaItensGuia == null ) {
			listaItensGuia = new ArrayList();
		}
		
		listaCustaDt = (List) request.getSession().getAttribute("ListaCustaDt");
		if( listaCustaDt == null ) {
			listaCustaDt = new ArrayList();
		}
		
		setDadosGuiaCertidaoPraticaForense(request, guiaCertidaoGeralDt, UsuarioSessao);
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		request.setAttribute("TituloPagina", obtenhaTituloPagina());
		request.setAttribute("tempNomeBusca", obtenhaTituloPagina());
		request.setAttribute("tempPrograma", obtenhaTituloPagina());
		request.setAttribute("tempRetorno", obtenhaServletDeRetornoPesquisa());
		request.setAttribute("tempRetornoPaginaGuia", obtenhaServletPaginaGuia());
		request.setAttribute("PaginaAtual", posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaopaginaatual));
		request.setAttribute("PaginaAnterior", paginaatual);		
		
		if(request.getSession().getAttribute("Cabecalho") == null || request.getSession().getAttribute("Cabecalho").toString().equalsIgnoreCase("false")){			
			request.getSession().setAttribute("Cabecalho", false);
		} else {
			request.getSession().setAttribute("Cabecalho", true);
		}
		
		if ((request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null"))) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		guiaCertidaoGeralDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		guiaCertidaoGeralDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
//**************************************************************************************************************************************************************************************************************************************************		
		case Configuracao.Novo: {
			
			guiaCertidaoGeralDt.limpar();
			
			if( guiaCertidaoGeralNe.isConexaoSPG_OK() ) {
				
				stAcao = obtenhaPaginaPrincipal();
				
				guiaEmissaoDt = new GuiaEmissaoDt();
				request.getSession().removeAttribute("ListaGuiaItemDt");
				request.getSession().removeAttribute("ListaCustaDt");
				request.getSession().removeAttribute("TotalGuia");
				request.getSession().removeAttribute("GuiaEmissaoDt");
				request.getSession().setAttribute("ListaCustaDt", guiaCertidaoGeralNe.consultarItensGuia(null, String.valueOf(obtenhaGuiaTipo()), null));
			}
			else {
				redireciona(response, obtenhaAcaoMensagemErro(Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG)));
				return;
			}			
			break;
		}
//**************************************************************************************************************************************************************************************************************************************************		
		//Apresenta Prévia de Cálculo
		case Configuracao.Curinga6 : {
			switch(passoEditar) {
			
				case Configuracao.Mensagem : { //Apresentar Mensagem
					request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE)); //Atenção!... Não tem break pq precisa mostrar a tela novamente de prévia.
				}
				
				case Configuracao.Curinga8: {
					
					request.getSession().setAttribute("guiaCertidaoGeralDt", guiaCertidaoGeralDt);
					
					guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarIdGuiaModelo(String.valueOf(obtenhaGuiaTipo())));
					
					Map valoresReferenciaCalculo = new HashMap();
					
					valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, "0,0");
					
					String idGuiaTipo = new GuiaTipoNe().consultarIdCodigo(String.valueOf(obtenhaGuiaTipo()));
					
					listaCustaDt = guiaCertidaoGeralNe.consultarItensGuia(null, idGuiaTipo, null);
					listaItensGuia = guiaCertidaoGeralNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
					
					List listaGuiaItemDt = guiaCertidaoGeralNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
					
					guiaEmissaoDt.setId_GuiaTipo(obtenhaGuiaTipo()); //Atualiza o tipo de guia
					
					// Obtendo os número de ID_CUSTA, dos objetos da listaCustaDt. E obtenção dos valores das custas.
					List<String> listaValorCusta = new ArrayList<String>();
					CustaDt custaDt = new CustaDt();
					
					custaDt = (CustaDt) listaCustaDt.get(0); //obtendo a primeira custa (id = 191)
					GuiaItemDt guiaItem1Dt = new CustaValorNe().consultaValorCusta(custaDt.getId());
					guiaCertidaoGeralDt.setCustaCertidao( guiaItem1Dt.getValorReferencia() );
					
					custaDt = (CustaDt) listaCustaDt.get(1); //obtendo a primeira custa (id = 191)
					GuiaItemDt guiaItem2Dt = new CustaValorNe().consultaValorCusta(custaDt.getId());
					guiaCertidaoGeralDt.setCustaTaxaJudiciaria( guiaItem2Dt.getValorReferencia() );
					//FIM (Obtenção dos valores das custas)
					
					if( !guiaCertidaoGeralNe.isGuiaZeradaOuNegativa() ) {
						//Obtem o id_GuiaModelo
						if( listaItensGuia != null && listaItensGuia.size() > 0) {
							GuiaModeloDt guiaModeloDt = ((GuiaCustaModeloDt)listaItensGuia.get(0)).getGuiaModeloDt();
							if( guiaModeloDt != null && guiaEmissaoDt.getGuiaModeloDt() == null )
								guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
						}
						
						//Deve haver no mínimo 1 item de guia
						if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
							
							request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
							request.setAttribute("TotalGuia", guiaCertidaoGeralNe.getGuiaCalculoNe().getTotalGuia() );
							
							request.getSession().setAttribute("ListaGuiaItemDt" 			, listaGuiaItemDt);
							request.getSession().setAttribute("TotalGuia" 					, Funcoes.FormatarDecimal( guiaCertidaoGeralNe.getGuiaCalculoNe().getTotalGuia().toString() ) );
							request.setAttribute("visualizarBotaoImpressaoBotaoPagamento"	, guiaCertidaoGeralNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
							request.setAttribute("visualizarBotaoSalvarGuia" 				, new Boolean(true));
							request.setAttribute("visualizarBotaoVoltar" 					, new Boolean(true));
							request.setAttribute("visualizarLinkProcesso"					, new Boolean(true));
							
							guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
							
							request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
							
							if(this.verificarCamposVaziosDt(guiaCertidaoGeralDt)){
								stAcao = obtenhaPaginaPreviaCalculo();								
							} else {								
								stAcao = obtenhaPaginaPrincipal();
							}
							
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
							stAcao = obtenhaPaginaPrincipal();
						}
					}
					else {
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
						stAcao = obtenhaPaginaPrincipal();
					}
					break;
				}
			}
			break;
		}
//**************************************************************************************************************************************************************************************************************************************************		
		//Impressão da guia
		case Configuracao.Imprimir : {
			
			guiaCertidaoGeralDt = (GuiaCertidaoGeralDt)request.getSession().getAttribute("guiaCertidaoGeralDt");
			
			guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
			if( guiaEmissaoDt == null )
				guiaEmissaoDt = new GuiaEmissaoDt();
			
			//Obtêm o próximo número de Guia
			if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
				guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
				guiaEmissaoDt.setNumeroGuiaCompleto( guiaCertidaoGeralNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
			}
			guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
			UsuarioDt usuarioDt = UsuarioSessao.getUsuarioDt();
			
			//guiaEmissaoDt.setId_Serventia(usuarioDt.getId_Serventia());
			//guiaEmissaoDt.setServentia(usuarioDt.getServentia());
			guiaEmissaoDt.setId_Serventia(guiaCertidaoGeralDt.getId_Serventia());
			guiaEmissaoDt.setServentia(guiaCertidaoGeralDt.getServentia());
			//guiaEmissaoDt.setComarcaCodigo(usuarioDt.getComarcaCodigo());
			guiaEmissaoDt.setComarcaCodigo(guiaCertidaoGeralDt.getId_Comarca()); // Optei por usar o id comarca em vez do código, aqui. Para não haver inconsistências
			
			// Trecho alterado para mostrar comarca e o requerente no pdf da guia (GuiaEmissaoNe/imprimirGuia)
			guiaEmissaoDt.setComarca(guiaCertidaoGeralDt.getComarca());			
			if( guiaCertidaoGeralDt.getNome() != null && guiaCertidaoGeralDt.getNome().length() > 0){
				guiaEmissaoDt.setRequerente(guiaCertidaoGeralDt.getNome());
			}
			
			guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
			guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, String.valueOf(GuiaTipoDt.ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE), null));
			guiaEmissaoDt.setGuiaTipo(GuiaTipoDt.ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE);
			
			// Preenchendo os dados do advogado para leitura pelo SPG
			guiaEmissaoDt.setCodigoOabSPG(guiaCertidaoGeralDt.getOab() + guiaCertidaoGeralDt.getOabComplemento() + guiaCertidaoGeralDt.getOabUf());
			guiaEmissaoDt.setDataIniCertidaoSPG(guiaCertidaoGeralDt.getDataTimeInicial());
			guiaEmissaoDt.setDataFimCertidaoSPG(guiaCertidaoGeralDt.getDataTimeFinal());
			
			CertidaoGuiaDt certidaoGuiaDt = new CertidaoGuiaDt();
			
			String tipoCertPraticaForense = "";
			
			//Salvar GuiaEmissao
			if(certidaoGuiaDt.getTipo().equalsIgnoreCase("Quantitativa")){
				tipoCertPraticaForense = Integer.toString(ModeloDt.PRATICA_FORENSE_QUANTITATIVA_MODELO_CODIGO);
			} else {
				tipoCertPraticaForense = Integer.toString(ModeloDt.PRATICA_FORENSE_DESCRITIVA_MODELO_CODIGO);
			}
			
			guiaCertidaoGeralNe.salvar(guiaEmissaoDt, guiaCertidaoGeralDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), tipoCertPraticaForense);
			
			request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
			
			switch(passoEditar) {			
				case Configuracao.Imprimir: { //Geração da guia PDF (Opção Imprimir Boleto)
					
					byte[] byTemp = guiaCertidaoGeralNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), guiaCertidaoGeralDt.getServentia(), guiaEmissaoDt, String.valueOf(obtenhaGuiaTipo()), "CERTIDÃO DE PRÁTICA FORENSE");
					String nome="GuiaCertidaoPraticaForense";
					enviarPDF(response, byTemp, nome);
					return;
				}
				
				case Configuracao.Salvar : { // Emissão da guia (Opção Emitir Guia)
					
					redireciona(response, "Usuario?PaginaAtual=" + Configuracao.Cancelar + "&MensagemOk=Guia Emitida com Sucesso!\nNúmero da Guia: " + guiaEmissaoDt.getNumeroGuiaCompleto());
					return;
				}
			}
			break;
		}
//**************************************************************************************************************************************************************************************************************************************************
			case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"EstadoCivil"};
					String[] lisDescricao = {"EstadoCivil"};
					String[] camposHidden = {"EstadoCivil"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstadoCivil");
					request.setAttribute("tempBuscaDescricao", "EstadoCivil");
					request.setAttribute("tempBuscaPrograma", "EstadoCivil");
					request.setAttribute("tempRetorno", "GuiaCertidaoPraticaForense");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					try{
						stTemp = guiaCertidaoGeralNe.consultarDescricaoEstadoCivilJSON(stNomeBusca1, posicaopaginaatual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
//**************************************************************************************************************************************************************************************************************************************************	
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "GuiaCertidaoPraticaForense");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					try{
						stTemp = guiaCertidaoGeralNe.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, posicaopaginaatual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
//**************************************************************************************************************************************************************************************************************************************************
			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): // Localizar Orgao expedidor
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Sigla","Órgão Expedidor"};
					String[] lisDescricao = {"Sigla","RgOrgaoExpedidor","Estado"};
					String[] camposHidden = {"Sigla","RgOrgaoExpedidor","Estado"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "GuiaCertidaoPraticaForense");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					try{
						stTemp = guiaCertidaoGeralNe.consultarDescricaoRgOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2, posicaopaginaatual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;				
//**************************************************************************************************************************************************************************************************************************************************
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					String[] camposHidden = {"Comarca"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Comarca");
					request.setAttribute("tempBuscaDescricao", "Comarca");
					request.setAttribute("tempBuscaPrograma", "Comarca");
					request.setAttribute("tempRetorno", "GuiaCertidaoPraticaForense");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					try{
						stTemp = guiaCertidaoGeralNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaopaginaatual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
//**************************************************************************************************************************************************************************************************************************************************
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				String idComarca = "";
				if (guiaCertidaoGeralDt != null && 
				    guiaCertidaoGeralDt.getId_Comarca() != null && 
				    guiaCertidaoGeralDt.getId_Comarca().trim().length() > 0) {
					
					idComarca = guiaCertidaoGeralDt.getId_Comarca().trim();
					
					if (request.getParameter("Passo") == null) {
						String[] lisNomeBusca = {"Serventia"};
						String[] lisDescricao = {"Serventia","Estado"};
						String[] camposHidden = {"Serventia","Estado"};
						request.setAttribute("camposHidden",camposHidden);
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_Serventia");
						request.setAttribute("tempBuscaDescricao", "Serventia");
						request.setAttribute("tempBuscaPrograma", "Serventia");
						request.setAttribute("tempRetorno", "GuiaCertidaoPraticaForense");
						request.setAttribute("tempDescricaoId", "Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp = "";
						try{
							stTemp = guiaCertidaoGeralNe.consultarServentiasDistribuidorAtivoJSON(stNomeBusca1, posicaopaginaatual, idComarca);
							response.setContentType("text/x-json");
							response.getOutputStream().write(stTemp.getBytes());
							response.flushBuffer();
						} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
						return;
					}
					
				} else {					
					super.exibaMensagemInconsistenciaErro(request, "É necessário selecionar a comarca primeiro.");					
				}
				break;				
//**************************************************************************************************************************************************************************************************************************************************
			default:
				break;
		}
		request.getSession().setAttribute("guiaCertidaoGeralDt", guiaCertidaoGeralDt);
		request.getSession().setAttribute("guiaCertidaoGeralNe", guiaCertidaoGeralNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
//**************************************************************************************************************************************************************************************************************************************************
	protected boolean verificarCamposVaziosDt(GuiaCertidaoGeralDt guiaCertidaoGeralDt) throws Exception {
		
		if(guiaCertidaoGeralDt.getMesInicial().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getAnoInicial().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getMesFinal().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getAnoFinal().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getOabNumero().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getOabComplemento().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getOabUf().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getId_Comarca().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getComarca().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getId_Serventia().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getServentia().isEmpty()) return false;
		return true;
	}
//**************************************************************************************************************************************************************************************************************************************************	
	protected void setDadosGuiaCertidaoPraticaForense(HttpServletRequest request, GuiaCertidaoGeralDt guiaCertidaoGeralDt, UsuarioNe usuarioSessao) {
					
		if (request.getParameter("Tipo") != null && !request.getParameter("Tipo").equals("")) {
			guiaCertidaoGeralDt.setTipo(request.getParameter("Tipo"));
		}
		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			guiaCertidaoGeralDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			guiaCertidaoGeralDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			guiaCertidaoGeralDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			guiaCertidaoGeralDt.setAnoFinal(request.getParameter("AnoFinal"));
		}
		if (request.getParameter("Nome") != null && !request.getParameter("Nome").isEmpty()) {
			guiaCertidaoGeralDt.setNome(request.getParameter("Nome"));
		}
		String sexo = request.getParameter("Sexo");
		if (sexo != null && !sexo.isEmpty()) {
			guiaCertidaoGeralDt.setSexo(sexo);
		}		
		guiaCertidaoGeralDt.setId_EstadoCivil(request.getParameter("Id_EstadoCivil"));
		guiaCertidaoGeralDt.setEstadoCivil(request.getParameter("EstadoCivil"));			
		guiaCertidaoGeralDt.setId_Naturalidade(request.getParameter("Id_Cidade"));
		guiaCertidaoGeralDt.setNaturalidade(request.getParameter("Cidade"));
		
		String rg = request.getParameter("Rg");
		if (rg != null && !rg.isEmpty()) {
			guiaCertidaoGeralDt.setRg(rg);
		}
		String id_RgOrgaoExpedidor = request.getParameter("Id_RgOrgaoExpedidor");
		if (id_RgOrgaoExpedidor != null && !id_RgOrgaoExpedidor.isEmpty()) {
			guiaCertidaoGeralDt.setId_RgOrgaoExpedidor(id_RgOrgaoExpedidor);
		}		
		String rgOrgaoExpedidor = request.getParameter("RgOrgaoExpedidor");
		if (rgOrgaoExpedidor != null && !rgOrgaoExpedidor.isEmpty()) {
			guiaCertidaoGeralDt.setRgOrgaoExpedidor(rgOrgaoExpedidor);
		}		
		String rgOrgaoExpedidorSigla = request.getParameter("RgOrgaoExpedidorSigla");
		if (rgOrgaoExpedidorSigla != null && !rgOrgaoExpedidorSigla.isEmpty()) {
			guiaCertidaoGeralDt.setRgOrgaoExpedidorSigla(rgOrgaoExpedidorSigla);
		}		
		String cpf = request.getParameter("Cpf");
		if (cpf != null && !cpf.isEmpty()) {
			guiaCertidaoGeralDt.setCpf(cpf);
		}			
		if (request.getParameter("OabNumero") != null && !request.getParameter("OabNumero").equals("")) {
			guiaCertidaoGeralDt.setOab(request.getParameter("OabNumero"));
		}			
		if (request.getParameter("OabComplemento") != null && !request.getParameter("OabComplemento").equals("")) {
			guiaCertidaoGeralDt.setOabComplemento(request.getParameter("OabComplemento"));
		}
		if (request.getParameter("OabUf") != null && !request.getParameter("OabUf").equals("")) {
			String[] aux = request.getParameter("OabUf").split("-");
			guiaCertidaoGeralDt.setOabUfCodigo(aux[0]);
			guiaCertidaoGeralDt.setOabUf(aux[1]);
		}
		String id_Comarca = request.getParameter("Id_Comarca");
		if (id_Comarca != null && !id_Comarca.isEmpty()) {
			guiaCertidaoGeralDt.setId_Comarca(id_Comarca);
		}
		String comarca = request.getParameter("Comarca");
		if (comarca != null && !comarca.isEmpty()) {
			guiaCertidaoGeralDt.setComarca(comarca);
		}
		String id_Serventia = request.getParameter("Id_Serventia");
		if (id_Serventia != null && !id_Serventia.isEmpty()) {
			guiaCertidaoGeralDt.setId_Serventia(id_Serventia);
		}
		String serventia = request.getParameter("Serventia");
		if (serventia != null && !serventia.isEmpty()) {
			guiaCertidaoGeralDt.setServentia(serventia);
		}
	}
//**************************************************************************************************************************************************************************************************************************************************	
	protected String obtenhaPaginaPrincipal() {
		return PAGINA_GUIA_CERTIDAO_PRATICA_FORENSE;
	}
	
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaCertidaoPraticaForense";
	}
	
	protected String obtenhaTituloPagina() {
		return "Guia de Certidão de Prática Forense";
	}
	
	protected String obtenhaPaginaPreviaCalculo() {
		return PAGINA_GUIA_PREVIA_CALCULO;
	}
	
	protected String obtenhaServletPaginaGuia() {
		return "GuiaCertidaoPraticaForense?PaginaAtual=" + Configuracao.Novo;
	}
	
	protected String obtenhaGuiaTipo(){
		return GuiaTipoDt.ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE;
	}
	
	protected String obtenhaServletProximaPagina() {
		return "GuiaCertidaoPraticaForense?PaginaAtual=" + Configuracao.Novo;
	}
	
	protected String obtenhaAcaoMensagemOk(String mensagem) {
		return obtenhaServletProximaPagina() + "&MensagemOk=" + mensagem;
	}
	
	protected String obtenhaAcaoMensagemErro(String mensagem) {
		return obtenhaServletProximaPagina() + "&MensagemErro=" + mensagem;
	}
}
