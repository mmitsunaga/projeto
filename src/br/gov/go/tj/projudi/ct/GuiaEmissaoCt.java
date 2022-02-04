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
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GuiaCertidaoGeralDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.InfoRepasseSPGDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.GuiaCalculoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.LocomocaoNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class GuiaEmissaoCt extends GuiaEmissaoCtGen{

	private static final String PAGINA_GUIA_PREVIA_CALCULO 			= "/WEB-INF/jsptjgo/GuiaPreviaCalculo.jsp";
	private static final String PAGINA_GUIAS_PROCESSO 				= "/WEB-INF/jsptjgo/ProcessoGuias.jsp";
	private static final String PAGINA_GUIAS_USUARIO 				= "/WEB-INF/jsptjgo/UsuarioGuias.jsp";
	private static final String PAGINA_CANCELAR_GUIA 				= "/WEB-INF/jsptjgo/GuiaCancelar.jsp";	
	private static final String PAGINA_GUIA_EMISSAO 				= "/WEB-INF/jsptjgo/GuiaEmissao.jsp";
	private static final String PAGINA_SERVENTIA_LOCALIZAR 			= "/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
	private static final String PAGINA_PROCESSO_LOCALIZAR 			= "/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
	private static final String PAGINA_DESCONTO_PARCELAMENTO 		= "/WEB-INF/jsptjgo/GuiaDescontoParcelamento.jsp";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2261573645566923014L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaEmissaoDt GuiaEmissaodt;
		GuiaEmissaoNe guiaEmissaoNe;
		ProcessoDt processoDt = null;


		List tempList=null; 
		String Mensagem="";
		String stId="";
		int passoEditar = -9999;

		String stAcao = PAGINA_GUIA_EMISSAO;

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descriï¿½ï¿½o das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variï¿½vel para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GuiaEmissao");

		GuiaCertidaoGeralDt guiaCertidaoGeralDt = null;
		
		guiaEmissaoNe = new GuiaEmissaoNe();  

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
							
		GuiaEmissaodt =(GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaodt");
		if (GuiaEmissaodt == null )  GuiaEmissaodt = new GuiaEmissaoDt();
		
		GuiaEmissaodt.setGuiaEmissao( 			request.getParameter("GuiaEmissao"));
		GuiaEmissaodt.setId_GuiaModelo( 		request.getParameter("Id_GuiaModelo"));
		GuiaEmissaodt.setGuiaModelo( 			request.getParameter("GuiaModelo"));
		GuiaEmissaodt.setId_Processo( 			request.getParameter("Id_Processo"));
		GuiaEmissaodt.setId_Serventia( 			request.getParameter("Id_Serventia"));
		GuiaEmissaodt.setServentia( 			request.getParameter("Serventia"));
		GuiaEmissaodt.setId_ProcessoTipo( 		request.getParameter("Id_ProcessoTipo"));
		GuiaEmissaodt.setProcessoTipo( 			request.getParameter("ProcessoTipo"));

		GuiaEmissaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaEmissaodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//Página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		if ((request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null"))) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		switch (paginaatual) {
		
			case Configuracao.LocalizarDWR:{
				switch(passoEditar) {	
					case Configuracao.Salvar : {
						//Ocorrencia 2020/11319 - Retirado validação processos de outras serventias
						//Esta validação já deveria ter sido removida quando entrou a contadoria única.
//						if (UsuarioSessao.getUsuarioDt().getId_Serventia().equalsIgnoreCase(processoDt.getId_Serventia())){
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
							request.setAttribute("GuiaAlterarStatus", request.getParameter("GuiaAlterarStatus"));
							request.setAttribute("PassoEditar", Configuracao.LocalizarDWR);
							request.setAttribute("PaginaAnterior", Configuracao.Salvar);
							stAcao = PAGINA_GUIAS_PROCESSO;
							super.exibaMensagemConfirmacao(request, "Ao confirmar a alteração da guia para Aguardando Pagamento "
									+ "as pendências relacionada ao Pedido de Assistência: (Verificar Novo Processo com Pedido de Assistência) será descartada e a (Concluso com Pedido de Benefício de Assistência) terá o seu tipo Alterado, se as mesmas existirem.");
							AtualizaListaDeGuiasDoProcesso(request, paginaatual, processoDt, guiaEmissaoNe);
//						} else {
//							throw new MensagemException("Não é possível realizar operação em processos de outras serventias!");
//						}
						break;
					}
				
				}
			break;
			}
		
			case Configuracao.Excluir: { //Excluir
				if (passoEditar == Configuracao.Curinga9) {
					stAcao = PAGINA_GUIAS_PROCESSO;
					super.exibaMensagemConfirmacao(request, "Clique para confirmar o cancelamento da guia selecionada");
					AtualizaListaDeGuiasDoProcesso(request, paginaatual, processoDt, guiaEmissaoNe);
					request.setAttribute("PassoEditar", passoEditar);
					request.setAttribute("GuiaCancelar", request.getParameter("GuiaCancelar"));
					if( request.getParameter("GuiaCancelar") != null ) {
						if( guiaEmissaoNe.isBoletoEmitido(request.getParameter("GuiaCancelar").toString().trim()) ) {
							request.setAttribute("MensagemErro", "Esta guia já possui boleto emitido! Caso deseje mesmo cancelar esta guia, por favor, continue com o cancelamento." );
						}
					}
				}
				else {
					if (passoEditar == Configuracao.Curinga8) {
						stAcao = PAGINA_GUIAS_PROCESSO;
						super.exibaMensagemConfirmacao(request, "Clique para confirmar e desfazer o cancelamento da guia selecionada");
						AtualizaListaDeGuiasDoProcesso(request, paginaatual, processoDt, guiaEmissaoNe);
						request.setAttribute("PassoEditar", passoEditar);
						request.setAttribute("DesfazerGuiaCancelar", request.getParameter("DesfazerGuiaCancelar"));
					}
				}
				break;
			}
			
			case Configuracao.ExcluirResultado: {//Excluir
				if (passoEditar == Configuracao.Curinga9) {
					stAcao = PAGINA_GUIAS_PROCESSO;
					cancelarGuiaEmitida(request, UsuarioSessao, paginaatual, guiaEmissaoNe, processoDt);					
				}
				else {
					if (passoEditar == Configuracao.Curinga8) {
						stAcao = PAGINA_GUIAS_PROCESSO;
						desfazerCancelamentoGuia(request, UsuarioSessao, paginaatual, guiaEmissaoNe, processoDt);					
					}
				}
				break;
			}

			//Impressão da guia
			case Configuracao.Imprimir : {
				
				//********************************************
				//Pesquisas em Ne auxiliares
				ServentiaDt serventiaDt = null;
				ComarcaDt comarcaDt = null;
				if( processoDt != null && processoDt.getServentiaCodigo() != null ) {
					serventiaDt = guiaEmissaoNe.consultarServentiaProcesso(processoDt.getServentiaCodigo());
					if( serventiaDt != null ) {
						comarcaDt = guiaEmissaoNe.consultarComarca(serventiaDt.getId_Comarca());
						if( serventiaDt != null ) {
							processoDt.setComarca(serventiaDt.getComarca());
						}
					}
				}
				
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
				if( guiaEmissaoDt == null )
					guiaEmissaoDt = new GuiaEmissaoDt();
				
				//Obtém o próximo número de Guia
				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
					guiaEmissaoDt.setNumeroGuiaCompleto( guiaEmissaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
				}
				guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
				
				if( guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU)
					||
					guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU) ) {
					
					guiaEmissaoDt.setApelante(guiaEmissaoDt.getRequerente());
					guiaEmissaoDt.setApelado(guiaEmissaoDt.getRequerido());
					
					if( comarcaDt == null && guiaEmissaoDt.getId_Comarca() != null ) {
						comarcaDt = guiaEmissaoNe.consultarComarca(guiaEmissaoDt.getId_Comarca());
						guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					}
					
					if( processoDt != null && processoDt.getProcessoNumero() != null ) {
						guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
					}
					guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				}
				else {
					guiaEmissaoDt.setValorAcao(processoDt.getValor());
					guiaEmissaoDt.setServentia(processoDt.getServentia());
					guiaEmissaoDt.setComarca(processoDt.getComarca());
					if( comarcaDt != null && comarcaDt.getComarcaCodigo() != null ) {
						guiaEmissaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					}
					guiaEmissaoDt.setId_ProcessoTipo(processoDt.getId_ProcessoTipo());
					guiaEmissaoDt.setProcessoTipo(processoDt.getProcessoTipo());
					
					guiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
					guiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
					guiaEmissaoDt.setListaOutrasPartes(processoDt.getListaOutrasPartes());
					guiaEmissaoDt.setListaAdvogados(processoDt.getListaAdvogados());
					
					guiaEmissaoDt.setNumeroProcesso(processoDt.getProcessoNumero());
					guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				}
				
				//para guia inicial
				if( processoDt == null ) {
					processoDt = new ProcessoDt();
					
					if( guiaEmissaoDt != null && guiaEmissaoDt.getId_AreaDistribuicao() != null ) {
						AreaDistribuicaoDt areaDistribuicaoDt = new AreaDistribuicaoNe().consultarId(guiaEmissaoDt.getId_AreaDistribuicao());
						processoDt.setServentia(areaDistribuicaoDt.getAreaDistribuicao());
					}
				}
				
				//Geração da guia PDF
				byte[] byTemp = guiaEmissaoNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), processoDt.getServentia(), guiaEmissaoDt, guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo(), guiaEmissaoDt.getGuiaModeloDt().getGuiaTipo());
				
				request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				
				//ATENÇÃO: RETIRADO Cï¿½DIGO DE SALVAR.
				
				String nome="Guia-numero-" + guiaEmissaoDt.getNumeroGuiaCompleto();
				
				enviarPDF(response, byTemp, nome);
				return;
			}

			case Configuracao.Localizar: {//localizar
				
				switch(passoEditar) {
					
					//Case para a consulta do advogado. Aqui irï¿½ consultar as guias iniciais emitidas por ele.
					case Configuracao.Curinga6 : {
						stAcao = PAGINA_GUIAS_USUARIO;
						
						//retira o objeto processo da sessão para evitar problemas de inconsistência dos processos e suas guias
						request.getSession().removeAttribute("processoDt");
						
						request.setAttribute("tempPrograma" 			, "GuiaEmissao");
						request.setAttribute("tempRetorno" 				, "GuiaEmissao");
						request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
						request.setAttribute("PaginaAtual" 				, paginaatual);
						request.setAttribute("PosicaoPaginaAtual" 		, paginaatual);
						
						List listaGuiaTipoDt = new ArrayList();
						listaGuiaTipoDt.add(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
						listaGuiaTipoDt.add(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU);
						
						tempList = guiaEmissaoNe.consultarGuiasIdUsuario(UsuarioSessao.getId_Usuario(), listaGuiaTipoDt );
						
						if( tempList != null ) {
							request.setAttribute("ListaGuiaEmissao", tempList);
						}
						else {
							request.setAttribute("MensagemErro", "Nenhuma Guia Emitida pelo Projudi Localizada.");
						}
						break;
					}
				
					default : {
//						stAcao = PAGINA_GUIA_EMISSAO_LOCALIZAR;
//						request.setAttribute("tempBuscaId_GuiaEmissao","Id_GuiaEmissao");
//						request.setAttribute("tempBuscaGuiaEmissao","GuiaEmissao");
//						request.setAttribute("tempRetorno","GuiaEmissao");
//						tempList =GuiaEmissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
//						if (tempList.size()>0) {
//							request.setAttribute("ListaGuiaEmissao", tempList);
//							request.setAttribute("PaginaAtual", Configuracao.Localizar);
//							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//							request.setAttribute("QuantidadePaginas", GuiaEmissaone.getQuantidadePaginas());
//							GuiaEmissaodt.limpar();
//						}
//						else {
//							request.setAttribute("MensagemErro", "Dados Não Localizados");
//						}
						break;
					}
				}
				
				break;
			}
			
			
			case Configuracao.Novo: {
				
				String legendFieldset = "";
				
				String tipoGuiaReferencia = null;
				if( request.getParameter("TipoGuiaReferenciaDescontoParcelamento") != null ) {
					tipoGuiaReferencia = request.getParameter("TipoGuiaReferenciaDescontoParcelamento").toString();
				}
				request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
				
				switch(passoEditar) {
				
					//Desconto
					case Configuracao.Curinga6 : {
						
						if( guiaEmissaoNe.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(request.getParameter("Id_GuiaEmissaoReferencia").toString()) == null ) {
							throw new MensagemException(Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA));
						}
						
						if( tipoGuiaReferencia != null ) {
							
							if( tipoGuiaReferencia != null && tipoGuiaReferencia.equals(GuiaEmissaoDt.TIPO_GUIA_COM_DESCONTO) ) {
								
								request.setAttribute("mostrarDescontoGuia", new Boolean(true));
								request.setAttribute("mostrarParcelamentoGuia", new Boolean(false));
								request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(false));
								request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(false));
								
								request.setAttribute("porcentagemDescontoSelecionado", "");
								request.setAttribute("quantidadeParcelasSelecionado", "");
								legendFieldset = "Gerar Guia com Desconto";
								
							}
						}
						
						break;
					}
					
					//Parcelamento
					case Configuracao.Curinga7 : {
						
						if( guiaEmissaoNe.consultarGuiaEmissaoAguardandoPagamentoNaoVencida(request.getParameter("Id_GuiaEmissaoReferencia").toString()) == null ) {
							throw new MensagemException(Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA));
						}
						
						if( guiaEmissaoNe.isGuiaFinal_FinalZero(request.getParameter("Id_GuiaEmissaoReferencia").toString()) ) {
							throw new MensagemException("Guia é do tipo Final. De acordo com resolução número 81 de 22 de novembro de 2017, é vedado o parcelamento das custas finais.");
						}
						
						if( guiaEmissaoNe.isValorCausaMenorValorMaximoJuizado(request.getParameter("Id_GuiaEmissaoReferencia").toString()) ) {
							throw new MensagemException("Guia possui o valor da causa menor que o valor máximo permitido no juizado especial.");
						}
						
						if( tipoGuiaReferencia != null ) {
							
							if( tipoGuiaReferencia != null && tipoGuiaReferencia.equals(GuiaEmissaoDt.TIPO_GUIA_PARCELADA) ) {
								
								request.setAttribute("mostrarDescontoGuia", new Boolean(false));
								request.setAttribute("mostrarParcelamentoGuia", new Boolean(true));
								request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(false));
								request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(false));
								
								request.setAttribute("porcentagemDescontoSelecionado", "");
								request.setAttribute("quantidadeParcelasSelecionado", "");
								request.setAttribute("motivoParcelamentoSelecionado", "");
								request.setAttribute("mostrarDivMotivoParcelamento", new Boolean(false));
								request.getSession().removeAttribute("motivoParcelamentoSelecionado"+request.getParameter("Id_GuiaEmissaoReferencia").toString());
								legendFieldset = "Gerar Parcelamento de Guia";
								
							}
						}
						
						break;
					}
					
					//Adicionar 15 dias no vencimento apartir de hoje
					case Configuracao.Curinga8 : {
						
						request.setAttribute("mostrarDescontoGuia", new Boolean(false));
						request.setAttribute("mostrarParcelamentoGuia", new Boolean(false));
						request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(true));
						request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(false));
						
						request.setAttribute("reemitirGuiaSelecionado", new Boolean(true));
						
						legendFieldset = "Reemtitir esta Guia";
						
						break;
					}
					
					//Aumentar quantidade Máxima de Parcelamento de guia
					case Configuracao.Curinga9 : {
						
						request.setAttribute("mostrarDescontoGuia", new Boolean(false));
						request.setAttribute("mostrarParcelamentoGuia", new Boolean(false));
						request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(false));
						request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(true));
						
						legendFieldset = "Alterar Quantidade Máxima do Parcelamento";
						
						break;
					}
					
					//Primeira requisição para emitir a guia Parcelada ou Descontada
					case Configuracao.Salvar : {
						
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
						request.setAttribute("PassoEditar", Configuracao.SalvarResultado);
						
						String reemitirGuiaSelecionado 		= request.getParameter("reemitirGuiaSelecionado");
						String porcentagemDesconto 			= request.getParameter("porcentagemDesconto");
						String quantidadeParcelas 			= request.getParameter("quantidadeParcelas");
						String motivoParcelamento 			= request.getParameter("motivoParcelamento");
						String quantidadeMaximaParcelas 	= request.getParameter("quantidadeMaximaParcelas");
						legendFieldset 						= request.getParameter("legendFieldset");
						
						String mensagem = "";
						
						//Alterar Quantidade Máxima de Parcelamento
						if( quantidadeMaximaParcelas != null && !quantidadeMaximaParcelas.isEmpty() ) {
							mensagem = "Clique para confirmar a alteração da quantidade máxima de parcelamento da guia.";
							
							request.setAttribute("mostrarDescontoGuia", new Boolean(false));
							request.setAttribute("mostrarParcelamentoGuia", new Boolean(false));
							request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(false));
							request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(true));
							
							request.setAttribute("quantidadeMaximaParcelasSelecionado", quantidadeMaximaParcelas);
						}
						else {
							//Desconto
							if( porcentagemDesconto != null && !porcentagemDesconto.isEmpty() ) {
								mensagem = "Clique para confirmar o desconto desta guia.";
								
								request.setAttribute("mostrarDescontoGuia", new Boolean(true));
								request.setAttribute("mostrarParcelamentoGuia", new Boolean(false));
								request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(false));
								request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(false));
								
								request.setAttribute("porcentagemDescontoSelecionado", porcentagemDesconto);
							}
							else {
								//Parcelamento
								if( quantidadeParcelas != null && !quantidadeParcelas.isEmpty() ) {
									mensagem = "Clique para confirmar o parcelamento da guia.";
									
									request.setAttribute("mostrarDescontoGuia", new Boolean(false));
									request.setAttribute("mostrarParcelamentoGuia", new Boolean(true));
									request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(false));
									request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(false));
									
									request.setAttribute("quantidadeParcelasSelecionado", quantidadeParcelas);
									request.setAttribute("motivoParcelamentoSelecionado", motivoParcelamento);
									request.getSession().setAttribute("motivoParcelamentoSelecionado"+request.getParameter("Id_GuiaEmissaoReferencia").toString(), motivoParcelamento);
									if( Funcoes.StringToInt(quantidadeParcelas) > GuiaEmissaoNe.QUANTIDADE_MAXIMA_PARCELAS ) {
										request.setAttribute("mostrarDivMotivoParcelamento", new Boolean(true));
									}
									else {
										request.setAttribute("mostrarDivMotivoParcelamento", new Boolean(false));
									}
								}
								else {
									//Reemissão adicionando 15 dias a partir de hoje
									if( reemitirGuiaSelecionado != null && !reemitirGuiaSelecionado.isEmpty() ) {
										mensagem = "Clique para confirmar a reemissão da guia.";
										
										request.setAttribute("mostrarDescontoGuia", new Boolean(false));
										request.setAttribute("mostrarParcelamentoGuia", new Boolean(false));
										request.setAttribute("mostrarAreaReemissaoGuia", new Boolean(true));
										request.setAttribute("mostrarPermissaoParcelamentoGuia", new Boolean(false));
										
										request.setAttribute("reemitirGuiaSelecionado", new Boolean(true));
									}
									else {
										redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Por favor, informe o parâmetro solicitado para realizar a operação.");
										return;
									}
								}
							}
						}
						
						super.exibaMensagemConfirmacao(request, mensagem);
						request.setAttribute("PaginaAnterior", Configuracao.Salvar);
						
						break;
					}
					
//					//Salvar o resultado
//					case Configuracao.SalvarResultado : {
//						
//						String porcentagemDesconto 	= request.getParameter("porcentagemDesconto");
//						String quantidadeParcelas 	= request.getParameter("quantidadeParcelas");
//						String idGuiaEmissao 		= request.getParameter("Id_GuiaEmissaoReferencia");
//						
//						String msg = "";
//						
//						if( idGuiaEmissao == null || (idGuiaEmissao != null && idGuiaEmissao.isEmpty()) ) {
//							throw new MensagemException("Guia de Referência("+idGuiaEmissao+") não encontrado.");
//						}
//						
//						//Desconto
//						if( porcentagemDesconto != null && !porcentagemDesconto.isEmpty() ) {
//							if( GuiaEmissaone.gerarGuiaDescontada(idGuiaEmissao, porcentagemDesconto, UsuarioSessao.getId_Usuario()) ) {
//								msg = "MensagemOk=Guia com Desconto Emitida com Sucesso!";
//							}
//							else {
//								msg = "MensagemErro=Erro ao Emitir Guia com Desconto!";
//							}
//						}
//						else {
//							//Parcelamento
//							if( quantidadeParcelas != null && !quantidadeParcelas.isEmpty() ) {
//								if( GuiaEmissaone.gerarGuiaParcelada(idGuiaEmissao, quantidadeParcelas, UsuarioSessao.getId_Usuario()) ) {
//									msg = "MensagemOk=Guia Parcelada com Sucesso!";
//								}
//								else {
//									msg = "MensagemErro=Erro ao Parcelar a Guia.";
//								}
//							}
//						}
//						
//						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&"+msg);
//						
//						break;
//					}
					
				}
				
				GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissao(request.getParameter("Id_GuiaEmissaoReferencia").toString());
				
				request.setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
				request.setAttribute("tempRetorno","GuiaEmissao");
				request.setAttribute("legendFieldset", legendFieldset);
				
				stAcao = PAGINA_DESCONTO_PARCELAMENTO;
				
				break;
			}
			
			case Configuracao.SalvarResultado: {
				
				switch(passoEditar) {
				
					case Configuracao.LocalizarDWR:{
						
						if( request.getParameter("GuiaAlterarStatus") != null ) {
							String idGuiaEmissao = request.getParameter("GuiaAlterarStatus").toString().trim();
							GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarId(idGuiaEmissao);
							
							if (guiaEmissaoDt != null) {
								guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
								guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
								
								guiaEmissaoNe.alterarStatusGuiaAguardandoDeferimento(guiaEmissaoDt, processoDt, UsuarioSessao.getUsuarioDt());
								redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&"+"MensagemOk=Situação da Guia Alterada para AGUARDANDO PAGAMENTO com Sucesso!");
							} else {
								throw new MensagemException("Não foi possível localizar uma Guia Aguardando deferimento!");
							}
							
						} else {
							throw new MensagemException("Não foi possível localizar uma Guia Aguardando deferimento!");
						}
						
						break;
					}
					
					case Configuracao.SalvarResultado : {
						
						String reemitirGuiaSelecionado = request.getParameter("reemitirGuiaSelecionado");
						String porcentagemDesconto 	= request.getParameter("porcentagemDescontoSelecionado");
						String quantidadeParcelas 	= request.getParameter("quantidadeParcelasSelecionado");
						String motivoParcelamento 			= (String) request.getSession().getAttribute("motivoParcelamentoSelecionado"+request.getParameter("Id_GuiaEmissaoReferencia").toString());
						String quantidadeMaximaParcelas 	= request.getParameter("quantidadeMaximaParcelasSelecionado");
						String idGuiaEmissao 		= request.getParameter("Id_GuiaEmissaoReferencia");
						
						String msg = "";
						
						if( idGuiaEmissao == null || (idGuiaEmissao != null && idGuiaEmissao.isEmpty()) ) {
							throw new MensagemException("Guia de Referência("+idGuiaEmissao+") não encontrado.");
						}
						
						//Alterar a quantidade de parcelas
						if( quantidadeMaximaParcelas != null && !quantidadeMaximaParcelas.isEmpty() && !quantidadeMaximaParcelas.equalsIgnoreCase("null") ) {
							if( guiaEmissaoNe.alterarQuantidadeMaximaParcelas(idGuiaEmissao, quantidadeMaximaParcelas, motivoParcelamento, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog()) ) {
								msg = "MensagemOk=Alterado a Quantidade Máxima de Parcelas para a Guia!";
							}
							else {
								msg = "MensagemErro=Erro ao Alterar a Quantidade Máxima de Parcelas.";
							}
						}
						else {
							//Desconto
							if( porcentagemDesconto != null && !porcentagemDesconto.isEmpty() && !porcentagemDesconto.equalsIgnoreCase("null") ) {
								if( guiaEmissaoNe.gerarGuiaDescontada(idGuiaEmissao, porcentagemDesconto, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog()) ) {
									msg = "MensagemOk=Guia com Desconto Emitida com Sucesso!";
								}
								else {
									msg = "MensagemErro=Erro ao Emitir Guia com Desconto!";
								}
							}
							else {
								//Parcelamento
								if( quantidadeParcelas != null && !quantidadeParcelas.isEmpty() && !quantidadeParcelas.equalsIgnoreCase("null") ) {
									if( guiaEmissaoNe.gerarGuiaParcelada(idGuiaEmissao, quantidadeParcelas, motivoParcelamento, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog()) ) {
										msg = "MensagemOk=Guia Parcelada com Sucesso!";
									}
									else {
										msg = "MensagemErro=Erro ao Parcelar a Guia.";
									}
								}
								else {
									//Reemissão adicionando 15 dias a partir de hoje
									if( reemitirGuiaSelecionado != null && !reemitirGuiaSelecionado.isEmpty() && !reemitirGuiaSelecionado.equalsIgnoreCase("null") ) {
										if( guiaEmissaoNe.atualizarDataVencimentoGuia(idGuiaEmissao, UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()) ) {
											msg = "MensagemOk=Guia Alterada com Nova Data de Vencimento!";
										}
										else {
											msg = "MensagemErro=Erro ao Reemitir a Guia.";
										}
									}
								}
							}
						}
						
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&"+msg);
						
						break;
					}
					
					default : {
						Mensagem=guiaEmissaoNe.Verificar(GuiaEmissaodt);
						if (Mensagem.length()==0) {
							guiaEmissaoNe.salvar(GuiaEmissaodt);
							request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
						}
						else {
							request.setAttribute("MensagemErro", Mensagem );
						}
					}
					
				}
				
				break;
			}
			
			//cancelar guia emitida caso não esteja paga
			case Configuracao.Curinga9 : {
				
				stAcao = PAGINA_GUIAS_PROCESSO;
				cancelarGuiaEmitida(request, UsuarioSessao, paginaatual, guiaEmissaoNe, processoDt);;
				
				break;
			}
			
			//Consultar Lista de Guias do Processo para o "Menu Especial"/"Opções Processo"
			case Configuracao.Curinga6 : {
				
				request.setAttribute("tempPrograma" 			, "GuiaEmissao");
				request.setAttribute("tempRetorno" 				, "GuiaEmissao");
				request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
				request.setAttribute("PaginaAtual" 				, paginaatual);
				request.setAttribute("PosicaoPaginaAtual" 		, paginaatual);
				
				switch(passoEditar) {
				
					case Configuracao.Curinga8: {
						
						if((request.getParameter("Id_GuiaEmissao") != null || 
							(request.getParameter("NumeroGuia") != null && 
							 request.getParameter("EhGuiaSPG_SSG") != null && 
							 request.getParameter("EhGuiaSPG_SSG").toString().toUpperCase().equals("S"))) 
							&& request.getParameter("hash") != null ) {
							
							String hash = null;
							if (request.getParameter("Id_GuiaEmissao") != null) {
								hash = Funcoes.GeraHashMd5( request.getParameter("Id_GuiaEmissao").toString() + GuiaEmissaoNe.NUMERO_SERIE_GUIA );
							} else {
								hash = Funcoes.GeraHashMd5( request.getParameter("NumeroGuia").toString() + GuiaEmissaoNe.NUMERO_SERIE_GUIA );
							}
							
							String hashParametro = request.getParameter("hash").toString();
						
							if( hash != null && hashParametro != null && hash.equals(hashParametro) ) {
								
								List listaGuiaItemDt = null;
								
								if (request.getParameter("EhGuiaSPG_SSG") != null && request.getParameter("EhGuiaSPG_SSG").toString().toUpperCase().equals("S")) {
									
									GuiaEmissaoDt guiaEmissaoDt = null;
									
									//Guia SPG
									if (request.getParameter("NumeroGuia") != null && request.getParameter("NumeroGuia").trim().length() > 0) {
										guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoSPG(request.getParameter("NumeroGuia").toString());
									}
									else {
										guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoSPG(processoDt, request.getParameter("Id_GuiaEmissao").toString());	
									}
									//Guia SSG
									if( guiaEmissaoDt == null ) {
										guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoSSG(request.getParameter("NumeroGuia").toString());
									}
									
									listaGuiaItemDt = guiaEmissaoDt.getListaGuiaItemDt();
									
									GuiaCalculoNe guiaCalculoNe = new GuiaCalculoNe();
									guiaCalculoNe.recalcularTotalGuia(listaGuiaItemDt);
									
									request.setAttribute("ListLocomocaoMandado", new LocomocaoNe().consultarLocomocaoMandadoSPG(guiaEmissaoDt.getNumeroGuiaCompleto()));
									request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
									request.setAttribute("TotalGuia", guiaCalculoNe.getTotalGuia() );
									
									request.getSession().setAttribute("ListaGuiaItemDt" 	, listaGuiaItemDt);
									request.getSession().setAttribute("TotalGuia" 			, Funcoes.FormatarDecimal( guiaCalculoNe.getTotalGuia().toString() ) );
									
									//Este trecho do if ï¿½ utilizado pelo FinanceiroConsultarGuias 
									if( guiaEmissaoDt.getId_Processo() != null ) {
										processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo());
										
										guiaEmissaoDt.setId_Processo(processoDt.getId());
										guiaEmissaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
										guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
										guiaEmissaoDt.setIpComputadorLog(request.getRemoteAddr());
									}
									if( guiaEmissaoDt != null && guiaEmissaoDt.getId_Processo() == null && processoDt == null ) {
										processoDt = null;
									}
									
									if (processoDt == null && guiaEmissaoDt.getNumeroProcesso() != null && guiaEmissaoDt.getNumeroProcesso().trim().length() > 0) {
										processoDt = new ProcessoNe().consultarProcessoNumeroCompletoDigitoAno(tentarObterNumeroProcessoProjudi(guiaEmissaoDt.getNumeroProcesso()));
										guiaEmissaoDt.setId_Processo(processoDt.getId());
										guiaEmissaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
										guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
										guiaEmissaoDt.setIpComputadorLog(request.getRemoteAddr());
									}
									
									request.removeAttribute("emitirGuiaInicialLocomocaoComplementar");
																		
									ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
									
									if( processoDt != null && processoDt.getId() != null ) {
										guiaEmissaoDt.setListaPartesLitisconsorte(guiaEmissaoNe.consultarPartesLitisconsorteAtivoPassivo(processoDt.getId()));
										guiaEmissaoDt.setListaOutrasPartes(guiaEmissaoNe.consultarOutrasPartes(processoDt.getId()));
									}
									
									request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
									request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
									if (processoDt != null) request.getSession().setAttribute("processoDt", processoDt);
									request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", new Boolean(false));
									request.setAttribute("visualizarBotaoSalvarGuia" , new Boolean(false));
									if( request.getParameter("visualizarBotaoVoltar") != null && request.getParameter("visualizarBotaoVoltar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_VOLTAR) ) {
										request.setAttribute("visualizarBotaoVoltar" , new Boolean(false));
									}
									else {
										request.setAttribute("visualizarBotaoVoltar" , new Boolean(true));
									}
									if( request.getParameter("visualizarBotaoImpressaoBotaoPagamento") != null && request.getParameter("visualizarBotaoImpressaoBotaoPagamento").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_IMPRIMIR) ) {
										request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", new Boolean(false));
									}
									request.setAttribute("visualizarLinkProcesso" , new Boolean(true));
									
									if (processoDt != null) {
										guiaEmissaoNe.tenteSincronizeBaseProjudiSPG(processoDt, guiaEmissaoDt, UsuarioSessao);
										
										guiaEmissaoNe.tenteSincronizeBaseProjudiSSG(processoDt, guiaEmissaoDt, UsuarioSessao);
									}
									
									stAcao = PAGINA_GUIA_PREVIA_CALCULO;
								} else {
									
									listaGuiaItemDt = guiaEmissaoNe.consultarGuiaItens(request.getParameter("Id_GuiaEmissao").toString(), request.getParameter("Id_GuiaTipo"));
									
									//Deve haver no mínimo 1 item de guia
									if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
										
			//							//Consulta se tem guia complementar
			//							GuiaEmissaoDt guiaEmissaoDt_Complementar = GuiaEmissaone.consultarGuiaComplementarGuiaLocomocao(request.getParameter("Id_GuiaEmissao").toString());
			//							//Apresentar link guia complementar
			//							if( guiaEmissaoDt_Complementar != null && 
			//								guiaEmissaoDt_Complementar.getId_GuiaEmissaoPrincipal() != null && 
			//								guiaEmissaoDt_Complementar.getId_GuiaEmissaoPrincipal().length() > 0 ) {
			//								request.setAttribute("apresentarLinkGuiaComplementar", guiaEmissaoDt_Complementar.getId_GuiaEmissaoPrincipal());
			//							}
										
										GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissao(request.getParameter("Id_GuiaEmissao").toString());
										
										//Se tiver id guia referencia consulta
										guiaEmissaoNe.consultarGuiaReferencia(guiaEmissaoDt);
										
										//Se tiver id guia principal
										guiaEmissaoNe.consultarGuiaPrincipal(guiaEmissaoDt);
										
										GuiaCalculoNe guiaCalculoNe = new GuiaCalculoNe();
										guiaCalculoNe.recalcularTotalGuia(listaGuiaItemDt);
										
										request.setAttribute("ListLocomocaoMandado", new LocomocaoNe().consultarLocomocaoMandadoSPG(guiaEmissaoDt.getNumeroGuiaCompleto()));
										request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
										request.setAttribute("TotalGuia", guiaCalculoNe.getTotalGuia() );
										
										request.getSession().setAttribute("ListaGuiaItemDt" 	, listaGuiaItemDt);
										request.getSession().setAttribute("TotalGuia" 			, Funcoes.FormatarDecimal( guiaCalculoNe.getTotalGuia().toString() ) );
										
										//Este trecho do if ï¿½ utilizado pelo FinanceiroConsultarGuias 
										if( processoDt == null && guiaEmissaoDt.getId_Processo() != null ) {
											processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo());
										}
										if( guiaEmissaoDt.getId_Processo() != null ) {
											processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo());
										}
										if( guiaEmissaoDt != null && guiaEmissaoDt.getId_Processo() == null ) {
											processoDt = null;
										}
										
										ProcessoCadastroDt processoCadastroDt = new ProcessoCadastroDt();
										
										if( processoDt != null && processoDt.getListaPolosAtivos() != null && processoDt.getListaPolosAtivos().size() == 0 && processoDt.getListaPolosPassivos() != null && processoDt.getListaPolosPassivos().size() == 0 ) {
											
											processoDt = new ProcessoNe().consultarDadosProcesso(GuiaEmissaodt.getId_Processo(), UsuarioSessao.getUsuarioDt(), false, false , UsuarioSessao.getNivelAcesso() );
											
											processoCadastroDt.setListaPolosAtivos(processoDt.getListaPolosAtivos());
											processoCadastroDt.setListaPolosPassivos(processoDt.getListaPolosPassivos());
											processoCadastroDt.setValor(processoDt.getValor());
											processoCadastroDt.setProcessoTipo(processoDt.getProcessoTipo());
										}
										
										//Consulta o usuário
										guiaEmissaoDt.setUsuario( guiaEmissaoNe.consultarUsuario(guiaEmissaoDt.getId_Usuario()) );
										
										//Consultar nome da guia caso seja guia genï¿½rica
										if( guiaEmissaoDt.getId_GuiaTipo() != null && guiaEmissaoDt.getId_GuiaTipo().length() > 0 ) {
											guiaEmissaoDt.setGuiaTipo( guiaEmissaoNe.consultarGuiaTipo( null, guiaEmissaoDt.getId_GuiaTipo() ) );
										}
										
										if( processoCadastroDt.getListaPolosAtivos() != null && processoCadastroDt.getListaPolosAtivos().size() > 0 
												&&
											processoCadastroDt.getListaPolosPassivos() != null && processoCadastroDt.getListaPolosPassivos().size() > 0 ) {
											
											guiaEmissaoDt.setListaRequerentes(processoCadastroDt.getListaPolosAtivos());
											guiaEmissaoDt.setListaRequeridos(processoCadastroDt.getListaPolosPassivos());
										}
										else {
											if( processoDt != null 
													&&
												processoDt.getListaPolosAtivos() != null && processoDt.getListaPolosAtivos().size() > 0 
													&&
												processoDt.getListaPolosPassivos() != null && processoDt.getListaPolosPassivos().size() > 0 ) {
												
												guiaEmissaoDt.setListaRequerentes(processoDt.getListaPolosAtivos());
												guiaEmissaoDt.setListaRequeridos(processoDt.getListaPolosPassivos());
											}
										}
										if( processoDt != null && processoDt.getId() != null ) {
											guiaEmissaoDt.setListaPartesLitisconsorte(guiaEmissaoNe.consultarPartesLitisconsorteAtivoPassivo(processoDt.getId()));
											guiaEmissaoDt.setListaOutrasPartes(guiaEmissaoNe.consultarOutrasPartes(processoDt.getId()));
										}
										
										guiaEmissaoDt.setId_Apelante(guiaEmissaoDt.getId_Apelante());
										guiaEmissaoDt.setId_Apelado(guiaEmissaoDt.getId_Apelado());
										
										guiaEmissaoNe.setNomeApelanteNomeApelado(guiaEmissaoDt);
										
										if( guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_FAZENDA_MUNICIPAL) ) {
											//Altera nome do regimento 61:
											for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
												GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
												if( guiaItemDt.getCustaDt().getCodigoRegimento().equals("61") ) {// CustaDt.CUSTA_PENHORA
													guiaItemDt.getCustaDt().setArrecadacaoCusta("AUTOS");
												}
												if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("31") ) {// DUAM
													guiaItemDt.getCustaDt().setArrecadacaoCusta("DUAM NR. " + guiaEmissaoDt.getNumeroDUAM() + " (1/" + guiaEmissaoDt.getQuantidadeParcelasDUAM() + ")");
												}
												if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("24") && guiaItemDt.getParcelas().length() > 0 ) {// HONORARIOS DO PROCURADOR
													guiaItemDt.getCustaDt().setArrecadacaoCusta("HON. PROCURADORES MUNICIPAIS (" + guiaItemDt.getParcelaCorrente() + "/" + guiaItemDt.getParcelas() + ")");
												}
												if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("26") && guiaItemDt.getCodigoOficial() != null && guiaItemDt.getCodigoOficial().length() > 0){// LOCOMOCAO PARA OFICIAL AD HOC
													//Gera nome oficial para ser exibido na previa
													OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
													request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
													//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
													guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
												}
											}
										}
										
										if( guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_GUIA_GENERICA) ) {
											//Altera nome do regimento 61:
											for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
												GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
												if( guiaItemDt.getCustaDt().getId().equals("82") && guiaItemDt.getCodigoOficial() != null && guiaItemDt.getCodigoOficial().length() > 0){// LOCOMOCAO PARA OFICIAL AD HOC
													//Gera nome oficial para ser exibido na previa
													OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
													request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
													//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
													guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
												}
												if( guiaItemDt.getCustaDt().getId().equals("81") && guiaItemDt.getCodigoOficial() != null && guiaItemDt.getCodigoOficial().length() > 0){// LOCOMOCAO PARA OFICIAL AD HOC
													//Gera nome oficial para ser exibido na previa
													OficialSPGDt oficialSPGAdHoc = new OficialSPGNe().consultaOficial(guiaItemDt.getCodigoOficial());
													request.setAttribute("oficialAdHoc", oficialSPGAdHoc);
													//Muda nome da custa para nome do oficial - conforme BO 2012/34587 - ocomon
													guiaItemDt.getCustaDt().setArrecadacaoCusta(oficialSPGAdHoc.getCodigoOficial() + " - " + oficialSPGAdHoc.getNomeOficial());
												}
											}
										}
										
										if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
											if( processoCadastroDt.getListaPolosAtivos() != null ) {
												for( int i = 0; i < processoCadastroDt.getListaPolosAtivos().size(); i++ ) {
													ProcessoParteDt parteDt = (ProcessoParteDt)processoCadastroDt.getListaPolosAtivos().get(i);
													
													if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
														guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
													}
												}
											}
											if( processoCadastroDt.getListaPolosPassivos() != null ) {
												for( int i = 0; i < processoCadastroDt.getListaPolosPassivos().size(); i++ ) {
													ProcessoParteDt parteDt = (ProcessoParteDt)processoCadastroDt.getListaPolosPassivos().get(i);
													
													if( guiaEmissaoDt.getId_ProcessoParteResponsavelGuia().equals(parteDt.getId()) ) {
														guiaEmissaoDt.setNomeProcessoParteResponsavelGuia(parteDt.getNome());
													}
												}
											}
										}									
										
										//Apresentar botï¿½o de emissï¿½o de guia de locomoï¿½ï¿½o complementar
										String emitirGuiaLocomocaoComplementar = null;
										if( 
											(guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_LOCOMOCAO) || guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR)) && 
											( 
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO)) ||
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_ON_LINE)) || 
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR)) || 
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_INFERIOR)) 
											)
										  ) 
										{
											List<LocomocaoDt> locomocoesNaoUtilizadas = guiaEmissaoNe.consultarLocomocaoNaoUtilizada(processoDt.getId(), guiaEmissaoDt.getId(), true);
											if (locomocoesNaoUtilizadas.size() > 0) emitirGuiaLocomocaoComplementar = GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_LOC_COMPLEMENTAR;
										} 
										request.setAttribute("emitirGuiaLocomocaoComplementar", emitirGuiaLocomocaoComplementar);
										
										if (guiaEmissaoDt != null && guiaEmissaoDt.isLocomocaoComplementar()) {
											List<LocomocaoDt> listaLocomocaoUtilizadasDt = guiaEmissaoNe.consultarLocomocaoUtilizadaGuiaComplementar(processoDt.getId(), guiaEmissaoDt.getId());
											request.getSession().setAttribute("ListaLocomocaoNaoUtilizada", listaLocomocaoUtilizadasDt);
											request.setAttribute("exibeLocomocoesNaoUtilizadas", true);
										}
										
										//Apresentar botï¿½o de emissï¿½o de guia de locomoï¿½ï¿½o complementar para a guia inicial
										String emitirGuiaInicialLocomocaoComplementar = null;
										if( 
											guiaEmissaoDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) && 
											( 
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO)) ||
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_ON_LINE)) || 
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR)) || 
												guiaEmissaoDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_INFERIOR)) 
											)
										  ) {
											emitirGuiaInicialLocomocaoComplementar = GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_INICIAL_LOC_COMPLEMENTAR;
										}
										request.setAttribute("emitirGuiaInicialLocomocaoComplementar", emitirGuiaInicialLocomocaoComplementar);
										
										request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
										request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
										if (processoDt != null) request.getSession().setAttribute("processoDt", processoDt);
										request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", guiaEmissaoNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoDt));
										request.setAttribute("visualizarBotaoSalvarGuia" , new Boolean(false));
										if( request.getParameter("visualizarBotaoVoltar") != null && request.getParameter("visualizarBotaoVoltar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_VOLTAR) ) {
											request.setAttribute("visualizarBotaoVoltar" , new Boolean(false));
										}
										else {
											request.setAttribute("visualizarBotaoVoltar" , new Boolean(true));
										}
										if( request.getParameter("visualizarBotaoImpressaoBotaoPagamento") != null && request.getParameter("visualizarBotaoImpressaoBotaoPagamento").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_IMPRIMIR) ) {
											request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", new Boolean(false));
										}
										request.setAttribute("visualizarLinkProcesso" , new Boolean(true));
										
										guiaEmissaoNe.tenteSincronizeBaseProjudiSPG(processoDt, guiaEmissaoDt, UsuarioSessao);
										
										stAcao = PAGINA_GUIA_PREVIA_CALCULO;
										
										// Atualiza o objeto guiaCertidaoGeralDt, para poder apresentar as informações da guia na prévia de cálculo.
										if((guiaEmissaoDt.getNumeroGuiaCompleto() != null) && (!guiaEmissaoDt.getNumeroGuiaCompleto().equalsIgnoreCase(""))){
											
											CertidaoGuiaDt certidaoGuiaDt = new GuiaEmissaoNe().consultarCertidao(guiaEmissaoDt.getNumeroGuiaCompleto());
											
											if(certidaoGuiaDt != null){
												
												if( guiaCertidaoGeralDt == null ) {
													guiaCertidaoGeralDt = new GuiaCertidaoGeralDt();
												}
												
												guiaCertidaoGeralDt.setNome(certidaoGuiaDt.getNome());
												guiaCertidaoGeralDt.setCpf(certidaoGuiaDt.getCpf());
												guiaCertidaoGeralDt.setTipoPessoa(certidaoGuiaDt.getTipoPessoa());
												guiaCertidaoGeralDt.setId_Naturalidade(certidaoGuiaDt.getId_Naturalidade());
												guiaCertidaoGeralDt.setNaturalidade(certidaoGuiaDt.getNaturalidade());
												guiaCertidaoGeralDt.setSexo(certidaoGuiaDt.getSexo());
												guiaCertidaoGeralDt.setDataNascimento(certidaoGuiaDt.getDataNascimento());
												guiaCertidaoGeralDt.setRg(certidaoGuiaDt.getRg());
												guiaCertidaoGeralDt.setId_RgOrgaoExpedidor(certidaoGuiaDt.getId_RgOrgaoExpedidor());
												guiaCertidaoGeralDt.setRgOrgaoExpedidor(certidaoGuiaDt.getRgOrgaoExpedidor());
												guiaCertidaoGeralDt.setRgOrgaoExpedidorSigla(certidaoGuiaDt.getRgOrgaoExpedidorSigla());
												guiaCertidaoGeralDt.setRgDataExpedicao(certidaoGuiaDt.getRgDataExpedicao());
												guiaCertidaoGeralDt.setId_EstadoCivil(certidaoGuiaDt.getId_EstadoCivil());
												guiaCertidaoGeralDt.setEstadoCivil(certidaoGuiaDt.getEstadoCivil());
												guiaCertidaoGeralDt.setId_Profissao(certidaoGuiaDt.getId_Profissao());
												guiaCertidaoGeralDt.setProfissao(certidaoGuiaDt.getProfissao());
												guiaCertidaoGeralDt.setDomicilio(certidaoGuiaDt.getDomicilio());
												
											}
										}
									}
									else {
										request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
										stAcao = PAGINA_GUIAS_PROCESSO;
									}	
								}
							}
						}
						break;
					}
					
					default : {
						stAcao = PAGINA_GUIAS_PROCESSO;
						
						if( processoDt != null ) {
							tempList = guiaEmissaoNe.consultarGuiaEmissao(null, processoDt.getId(), guiaEmissaoNe.consultarListaId_GuiaTipo(null));
							
							if (tempList == null) {
								tempList = new ArrayList();
							}
							
							//Consultar guias SPG
							List tempListSPG = guiaEmissaoNe.consultarGuiaEmissaoSPG(processoDt, tempList);
							if ( tempListSPG != null && tempListSPG.size() > 0 ) {
								tempList.addAll(tempListSPG);
							}
							
							//Consultar guias SSG
							List tempListSSG = guiaEmissaoNe.consultarGuiaEmissaoSSG(processoDt, tempList);
							if( tempListSSG != null && tempListSSG.size() > 0 ) {
								tempList.addAll(tempListSSG);
							}
							
							if (tempList != null) {
								//Consulta repasse da guia no SPG
								Map<GuiaEmissaoDt, List<InfoRepasseSPGDt>> listaGuiaRepasses = new HashMap<GuiaEmissaoDt, List<InfoRepasseSPGDt>>();
								guiaEmissaoNe.consultarPercentualRepasseCadaGuia(tempList, listaGuiaRepasses);
							}
						}
						
						if( tempList != null && tempList.size() > 0) {
							
							//Segurança: Qual usuário da Central de mandado?
							if( !UsuarioSessao.isAdvogado() ) {
								request.setAttribute("ListaGuiaEmissaoLocomocaoVinculadaOficial", guiaEmissaoNe.consularGuiaLocomocaoVinculadaOficialTratamentoListaCompleta(processoDt.getId(), tempList));
							}
							request.setAttribute("ListaGuiaEmissao", tempList);
							
							if( !UsuarioSessao.isAdvogado() ) {
								request.setAttribute("apresentaBotaoCancelar", GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA);
							}
							else {
								request.removeAttribute("apresentaBotaoCancelar");
							}
							
							//Verifica se mostra o percentual de repasse para contador e analista
							if( UsuarioSessao.getUsuarioDt().isContador() || UsuarioSessao.getUsuarioDt().isAnalistaJudiciario() ) {
								request.setAttribute("apresentarPercentualRepasse", new Boolean(true));
							}
							else {
								request.setAttribute("apresentarPercentualRepasse", new Boolean(false));
							}
							
							//Adiciona nos Atributos do request quantidades de itens despesa postal
							guiaEmissaoNe.consultaQuantidadeItemDespesaPostalProcesso(request, processoDt.getId());
							
							//Adiciona nos Atributos do request quantidades de itens despesa postal de ORDEM de SERVICO aberta e fechada
							guiaEmissaoNe.consultaQuantidadeOrdemServicoDespesaPostalAbertaFechada(request, processoDt.getId());
							
							
							//Adiciona nos Atributos do request quantidades de itens locomocao disponivel
							guiaEmissaoNe.consultaQuantidadeLocomocoesDisponivelProcesso(request, processoDt.getId());
						}
						else {
							if (processoDt != null && (processoDt.isProcessoImportadoSPG()  || processoDt.isGuiaInicialSPG()))
								request.setAttribute("MensagemErro", "Nenhuma Guia Emitida Localizada para este Processo.");
						}
						
						break;
					}
				}
				
				break;
			}
			
//			//Encaminha Pagamento On-Line
//			case Configuracao.Curinga7 : {
//				
//				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt)request.getSession().getAttribute("GuiaEmissaoDt");
//				if( guiaEmissaoDt == null )
//					guiaEmissaoDt = new GuiaEmissaoDt();
//				
//				//Obtï¿½m o prï¿½ximo nï¿½mero de Guia
//				if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//					guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//					guiaEmissaoDt.setNumeroGuiaCompleto( guiaEmissaoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
//				}
//				guiaEmissaoDt.setListaGuiaItemDt( (List) request.getSession().getAttribute("ListaGuiaItemDt") );
//				guiaEmissaoDt.setId_Serventia(processoDt.getId_Serventia());
//				guiaEmissaoDt.setComarca(processoDt.getComarca());
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
//						redireciona(response, "PagamentoOnLine?PaginaAtual=" + Configuracao.Curinga6 + "&PassoEditar=" + Configuracao.Curinga6 + "&tempRetornoBuscaProcesso=BuscaProcesso&sv=" + PagamentoOnLineNe.PERMITE_SALVAR_NAO);
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
			
			//Cancelar guia
			case Configuracao.Curinga8 : {
				
				switch(passoEditar) {
					
					//Abrir formulï¿½rio e pï¿½gina
					case Configuracao.Novo : {
						stAcao = PAGINA_CANCELAR_GUIA;
						break;
					}
					
					//Aï¿½ï¿½o de cancelar: Aqui sendo utilizado literalmente para cancelar a guia
					case Configuracao.Cancelar : {
						break;
					}
					
				}
				
				break;
			}
			
			case (ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				request.setAttribute("tempBuscaId_Processo","Id_Processo");
				request.setAttribute("tempBuscaProcesso","Processo");
				request.setAttribute("tempRetorno","GuiaEmissao");
				stAcao = PAGINA_PROCESSO_LOCALIZAR;
				tempList =guiaEmissaoNe.consultarDescricaoProcesso(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0) {
					request.setAttribute("ListaProcesso", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", guiaEmissaoNe.getQuantidadePaginas());
				}
				else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			}
			
			case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
				request.setAttribute("tempBuscaServentia","Serventia");
				request.setAttribute("tempRetorno","GuiaEmissao");
				stAcao = PAGINA_SERVENTIA_LOCALIZAR;
				tempList =guiaEmissaoNe.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0) {
					request.setAttribute("ListaServentia", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", guiaEmissaoNe.getQuantidadePaginas());
				}
				else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			}
			
			case (ProcessoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				
				
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ProcessoTipo"};
					String[] lisDescricao = {"Tipo de Processo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissao = String.valueOf((ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "ProcessoTipo", "GuiaEmissao", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
										
				} else {
					String stTemp = "";
					stTemp = guiaEmissaoNe.consultarDescricaoProcessoTipoJSON(tempNomeBusca,PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;	
				
			}
			
			default: {
				stId = request.getParameter("Id_GuiaEmissao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GuiaEmissaodt.getId()))){
						GuiaEmissaodt.limpar();
						GuiaEmissaodt = guiaEmissaoNe.consultarId(stId);
					}
				break;
			}
		}

		request.getSession().setAttribute("GuiaEmissaodt",GuiaEmissaodt );
		request.getSession().setAttribute("GuiaEmissaone",guiaEmissaoNe );
		request.getSession().setAttribute("guiaCertidaoGeralDt", guiaCertidaoGeralDt);
		
		if ((request.getParameter("comandoOnClickBotaoVoltar") != null) && !(request.getParameter("comandoOnClickBotaoVoltar").equals("null")) && 
			(request.getParameter("tempRetorno") != null) && !(request.getParameter("tempRetorno").equals("null"))) { 
			request.setAttribute("comandoOnClickBotaoVoltar", request.getParameter("comandoOnClickBotaoVoltar"));
			request.setAttribute("tempRetorno", request.getParameter("tempRetorno"));
		}		

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected void cancelarGuiaEmitida(HttpServletRequest request, UsuarioNe UsuarioSessao, int paginaatual, GuiaEmissaoNe GuiaEmissaone, ProcessoDt processoDt) throws Exception {
		if( request.getParameter("GuiaCancelar") != null ) {
			String idGuiaEmissao = request.getParameter("GuiaCancelar").toString().trim();
			boolean cancelada = false;
			
			GuiaEmissaoDt guiaEmissaoDt = GuiaEmissaone.consultarId(idGuiaEmissao);
			
			if (guiaEmissaoDt != null) {
				guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				cancelada = GuiaEmissaone.cancelarGuiaEmitida(guiaEmissaoDt);	
			}			
			
			if( cancelada ) {
				request.setAttribute("MensagemOk","Guia Cancelada com Sucesso!");
			} else {
				request.setAttribute("MensagemErro", "Guia não Cancelada!" );
			}
		}
		
		AtualizaListaDeGuiasDoProcesso(request, paginaatual, processoDt, GuiaEmissaone);
	}
	
	protected void desfazerCancelamentoGuia(HttpServletRequest request, UsuarioNe UsuarioSessao, int paginaatual, GuiaEmissaoNe GuiaEmissaone, ProcessoDt processoDt) throws Exception {
		if( request.getParameter("DesfazerGuiaCancelar") != null ) {
			String idGuiaEmissao = request.getParameter("DesfazerGuiaCancelar").toString().trim();
			boolean cancelamentoDesfeito = false;
			
			GuiaEmissaoDt guiaEmissaoDt = GuiaEmissaone.consultarId(idGuiaEmissao);
			
			if (guiaEmissaoDt != null) {
				guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				guiaEmissaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				cancelamentoDesfeito = GuiaEmissaone.desfazerCancelamentoGuia(guiaEmissaoDt);	
			}			
			
			if( cancelamentoDesfeito ) {
				request.setAttribute("MensagemOk","Cancelamento da Guia Desfeito com Sucesso!");
			} else {
				request.setAttribute("MensagemErro", "Guia não foi Alterada!" );
			}
		}
		
		AtualizaListaDeGuiasDoProcesso(request, paginaatual, processoDt, GuiaEmissaone);
	}
	
	protected void AtualizaListaDeGuiasDoProcesso(HttpServletRequest request, int paginaatual, ProcessoDt processoDt, GuiaEmissaoNe GuiaEmissaone) throws Exception {
		request.setAttribute("tempPrograma" 			, "GuiaEmissao");
		request.setAttribute("tempRetorno" 				, "GuiaEmissao");
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAtual" 				, paginaatual);
		request.setAttribute("PosicaoPaginaAtual" 		, paginaatual);
		
		List tempList = GuiaEmissaone.consultarGuiaEmissao(null, processoDt.getId(), GuiaEmissaone.consultarListaId_GuiaTipo(null));
		
		if( tempList != null ) {
			request.setAttribute("ListaGuiaEmissao", tempList);
			request.setAttribute("apresentaBotaoCancelar", GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA);					}
		else {
			request.setAttribute("MensagemErro", "Nenhuma Guia Emitida pelo Projudi Localizada para este Processo.");
		}
	}
	
	protected static String tentarObterNumeroProcessoProjudi(String numeroDoProcessoProjudi){
		try	{
			String anoProcesso = numeroDoProcessoProjudi.trim().substring(numeroDoProcessoProjudi.trim().length() - 4, numeroDoProcessoProjudi.trim().length());
			String digitoProcesso = numeroDoProcessoProjudi.trim().substring(numeroDoProcessoProjudi.trim().length() - 6, numeroDoProcessoProjudi.trim().length() - 4);
			String numeroProcesso = numeroDoProcessoProjudi.trim().substring(0, numeroDoProcessoProjudi.trim().length() - 6);		
			
			return numeroProcesso + "." + digitoProcesso + "." + anoProcesso;
		
		} catch (Exception ex) {
			return "";
		}
	}
}
