package br.gov.go.tj.projudi.ct;
import java.io.IOException;
import java.text.DecimalFormat;
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
import br.gov.go.tj.projudi.dt.GuiaCertidaoGeralDt;
import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.CustaValorNe;
import br.gov.go.tj.projudi.ne.GuiaCertidaoGeralNe;
import br.gov.go.tj.projudi.ne.GuiaModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class GuiaCertidaoNarrativaCt extends Controle {

	private static final long serialVersionUID = 2446627473259715L;

	private static final String PAGINA_GUIA_CERTIDAO_NARRATIVA = "/WEB-INF/jsptjgo/GuiaCertidaoNarrativa.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO = "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	
	@Override
	public int Permissao() {
		return GuiaCertidaoGeralDt.CodigoPermissaoNarrativa;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String stAcao = null;
		int passoEditar = -1;
		stAcao = obtenhaPaginaPrincipal();
		
		List listaCustaDt = null;
		List listaItensGuia = null;
		
		GuiaEmissaoDt guiaEmissaoDt = null;
		ProcessoDt processoDt = null;		
		GuiaCertidaoGeralNe guiaCertidaoGeralNe = null;
		GuiaCertidaoGeralDt guiaCertidaoGeralDt = null;
		
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
		
		setDadosGuiaCertidaoNarrativa(request, guiaCertidaoGeralDt, UsuarioSessao);
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		request.setAttribute("tempPrograma", obtenhaTituloPagina());
		request.setAttribute("tempRetorno", obtenhaServletDeRetornoPesquisa());
		request.setAttribute("tempRetornoBuscaProcesso", obtenhaServletBuscaProcesso());
		request.setAttribute("PaginaAtual", posicaopaginaatual);
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaopaginaatual));
		request.setAttribute("PaginaAnterior", paginaatual);

		if ((request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null"))) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		guiaCertidaoGeralDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		guiaCertidaoGeralDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		ServentiaDt serventiaDt = null;
		ComarcaDt comarcaDt = null;
		if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
			serventiaDt = guiaCertidaoGeralNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
			
			if( serventiaDt == null ) {
				if( processoDt.getId_Serventia() != null ) {
					serventiaDt = guiaCertidaoGeralNe.consultarIdServentia(processoDt.getId_Serventia());
				}
			}
			
			if( serventiaDt != null ) {
				if( serventiaDt.getId_Comarca() != null ) {
					comarcaDt = guiaCertidaoGeralNe.consultarComarca(serventiaDt.getId_Comarca());
				}
				processoDt.setComarca(serventiaDt.getComarca());
			}
		}
		
		switch(paginaatual) {		
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
				request.getSession().removeAttribute("ListaPartesLitisconsorte");
				
				guiaEmissaoDt.setListaPartesLitisconsorte(guiaCertidaoGeralNe.consultarPartesLitisconsorteAtivoPassivo(processoDt.getId()));
				guiaEmissaoDt.setListaOutrasPartes(guiaCertidaoGeralNe.consultarOutrasPartes(processoDt.getId()));
				
				request.getSession().setAttribute("ListaPartesLitisconsorte", guiaEmissaoDt.getListaPartesLitisconsorte());
				
				// Verifica se já existe guia do mesmo tipo, para emitir uma mensagem avisando.
				if( guiaCertidaoGeralNe.existeGuiaEmitidaMesmoTipo(processoDt.getId(), obtenhaGuiaTipo()) ) {
					request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO));
				}
			}
			else {
				redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_FALHA_CONECTAR_SPG)));
				return;
			}			
			break;
		}
//**************************************************************************************************************************************************************************************************************************************************		
		//Apresenta Prévia de Cálculo
		case Configuracao.Curinga6 : {
			
			String cpf = (String) guiaCertidaoGeralDt.getCpf();
			cpf = cpf.replace("-", "").replace(".", "");
			if(!Funcoes.testaCPFCNPJ(cpf)){
				throw new MensagemException("CPF inválido.");
			} else {
				switch(passoEditar) {
				
					case Configuracao.Mensagem : { //Apresentar Mensagem
						request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE)); //Atenção!... Não tem break pq precisa mostrar a tela novamente de prévia.
					}
					
					case Configuracao.Curinga8: {
						
						//Valida o processo da sua aba
						if( request.getParameter("guiaIdProcesso") != null && !request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {						
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO));
							return;
						}
						
						request.getSession().setAttribute("guiaCertidaoGeralDt", guiaCertidaoGeralDt);
						
						guiaEmissaoDt.setGuiaModeloDt(new GuiaModeloNe().consultarGuiaModeloProcessoTipo(null, obtenhaGuiaTipo(), null));
						
						Map valoresReferenciaCalculo = new HashMap();
						
						valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, processoDt.getValor());
						
						listaCustaDt = guiaCertidaoGeralNe.consultarItensGuia(null, obtenhaGuiaTipo(), null);
						listaItensGuia = guiaCertidaoGeralNe.consultarItensGuiaCustaDt(guiaEmissaoDt, listaCustaDt);
						
						// INSERÇÃO DOS VALORES DE CUSTAS NO OBJETO guiaCertidaoGeralDt E, APÓS, NOS METADADOS
						CustaDt custaDt = new CustaDt();
						GuiaItemDt guiaItem2Dt = new GuiaItemDt();
						Double custaTotal = 0.0;
						DecimalFormat formato = new DecimalFormat("#.##");						
						
						custaDt = (CustaDt) listaCustaDt.get(0);
						if(custaDt != null){
							guiaItem2Dt = new CustaValorNe().consultaValorCusta(custaDt.getId());
							if(guiaItem2Dt != null){
								guiaCertidaoGeralDt.setCustaTaxaJudiciaria(String.format("%.2f", Double.parseDouble(guiaItem2Dt.getValorReferencia())));
								custaTotal += Double.parseDouble(guiaItem2Dt.getValorReferencia());
							}						
						}					
						custaDt = (CustaDt) listaCustaDt.get(1);
						if(custaDt != null){
							guiaItem2Dt = new CustaValorNe().consultaValorCusta(custaDt.getId());						
							if(guiaItem2Dt != null){
								guiaCertidaoGeralDt.setCustaCertidao(String.format("%.2f", Double.parseDouble(guiaItem2Dt.getValorReferencia())));
								custaTotal += Double.parseDouble(guiaItem2Dt.getValorReferencia());
								guiaCertidaoGeralDt.setCustaTotal((String) formato.format(custaTotal));
							}						
						}
						// FIM DA INSERÇÃO DOS VALORES
						
						request.getSession().setAttribute("GuiaCertidaoGeralDt", guiaCertidaoGeralDt);
						
						guiaEmissaoDt.setId_Processo(processoDt.getId_Processo());
						List listaGuiaItemDt = guiaCertidaoGeralNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo, null, null);
						
						guiaEmissaoDt.setId_GuiaTipo(obtenhaGuiaTipo()); //Atualiza o tipo de guia
						
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
									stAcao = PAGINA_GUIA_PREVIA_CALCULO;
									request.setAttribute("MensagemOk", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA));
								} else {								
									stAcao = PAGINA_GUIA_CERTIDAO_NARRATIVA;
								}
								
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
								stAcao = PAGINA_GUIA_CERTIDAO_NARRATIVA;
							}
						}
						else {
							request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_ADVERTENCIA_GUIA_ZERADA));
							stAcao = PAGINA_GUIA_CERTIDAO_NARRATIVA;
						}
						break;
					}
				}
			}	
			break;
		}
//**************************************************************************************************************************************************************************************************************************************************		
		//Impressão da guia
		case Configuracao.Imprimir : {
			
			//Valida o processo da sua aba
			if( request.getParameter("guiaIdProcesso") != null && 
				!request.getParameter("guiaIdProcesso").toString().equals(processoDt.getId()) ) {
				
				redireciona(response, obtenhaAcaoMensagemErro(processoDt, Configuracao.getMensagem(Configuracao.MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO)));
				return;
			}
			
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
			
			//Salvar GuiaEmissao
			guiaCertidaoGeralNe.salvar(guiaEmissaoDt, guiaCertidaoGeralDt, guiaEmissaoDt.getListaGuiaItemDt(), true, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), Integer.toString(ModeloDt.NARRATIVA_MODELO_CODIGO));
			
			request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
			
			switch(passoEditar) {			
				case Configuracao.Imprimir: { //Geração da guia PDF					
					byte[] byTemp = guiaCertidaoGeralNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, obtenhaGuiaTipo(), "CERTIDÃO NARRATIVA");
					String nome="GuiaCertidaoNarrativa_Processo_"+ guiaEmissaoDt.getNumeroProcesso();
					enviarPDF(response, byTemp, nome);
					return;
				}
				
				case Configuracao.Salvar : {					
					redireciona(response, obtenhaAcaoMensagemOk(processoDt, "Guia Emitida com Sucesso!\nNúmero da Guia: " + guiaEmissaoDt.getNumeroGuiaCompleto()));					
					return;
				}
			}
			break;
		}
		default: {
			break;
		}
		}
		request.getSession().setAttribute("guiaCertidaoGeralNe", guiaCertidaoGeralNe);
		request.getSession().setAttribute("guiaCertidaoGeralDt", guiaCertidaoGeralDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
//**************************************************************************************************************************************************************************************************************************************************
	protected boolean verificarCamposVaziosDt(GuiaCertidaoGeralDt guiaCertidaoGeralDt) throws Exception {
		
		if(guiaCertidaoGeralDt.getNome().isEmpty()) return false;
		if(guiaCertidaoGeralDt.getCpf().isEmpty()) return false;

		return true;
	}
//**************************************************************************************************************************************************************************************************************************************************	
	protected void setDadosGuiaCertidaoNarrativa(HttpServletRequest request, GuiaCertidaoGeralDt guiaCertidaoGeralDt, UsuarioNe usuarioSessao) {
		
		guiaCertidaoGeralDt.setNome(request.getParameter("Nome"));
		
		if(request.getParameter("Cpf") != null){
			String cpf = (String) request.getParameter("Cpf");
			cpf = cpf.replace("-", "").replace(".", "");
			guiaCertidaoGeralDt.setCpf(cpf);
		}		
	}
//**************************************************************************************************************************************************************************************************************************************************
	protected String obtenhaPaginaPrincipal() {
		return PAGINA_GUIA_CERTIDAO_NARRATIVA;
	}

	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaCertidaoNarrativa";
	}

	protected String obtenhaTituloPagina() {
		return "Guia de Certidão Narrativa";
	}

	protected String obtenhaServletBuscaProcesso() {
		return "BuscaProcesso";
	}

	protected String obtenhaPaginaPreviaCalculo() {
		return PAGINA_GUIA_PREVIA_CALCULO;
	}
	
	protected String obtenhaGuiaTipo(){
		return GuiaTipoDt.ID_GUIA_DE_CERTIDAO_NARRATIVA;
	}
	
	protected String obtenhaAcaoMensagemOk(ProcessoDt processoDt, String mensagem) {
		return obtenhaServletBuscaProcesso() + "?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=" + mensagem;
	}

	protected String obtenhaAcaoMensagemErro(ProcessoDt processoDt, String mensagem) {
		return obtenhaServletBuscaProcesso() + "?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=" + mensagem;
	}
}
