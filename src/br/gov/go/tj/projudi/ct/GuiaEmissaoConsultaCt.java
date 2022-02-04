package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoCompletaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaItemDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoGuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteGuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.GuiaCalculoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoConsultaNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.OficialSPGNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GuiaNumero;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaEmissaoConsultaCt extends Controle {

	private static final long serialVersionUID = 7991541641929161456L;
	
	private static final String PAGINA_GUIA_EMISSAO_CONSULTA        = "/WEB-INF/jsptjgo/GuiaEmissaoConsulta.jsp";
	private static final String PAGINA_GUIA_PREVIA_CALCULO 			= "/WEB-INF/jsptjgo/GuiaEmissaoConsultaPreviaCalculo.jsp";
	private static final String PAGINA_GUIAS_PROCESSO 				= "/WEB-INF/jsptjgo/ProcessoGuias.jsp";	

	@Override
	public int Permissao() {
		
		return GuiaEmissaoConsultaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		GuiaEmissaoConsultaDt guiaEmissaoConsultaDt;
		GuiaEmissaoConsultaNe guiaEmissaoConsultaNe;
		String stAcao;
		int passoEditar = -9999;
		
		guiaEmissaoConsultaDt = (GuiaEmissaoConsultaDt) request.getSession().getAttribute("guiaEmissaoConsultaDt");
		if (guiaEmissaoConsultaDt == null) {
			guiaEmissaoConsultaDt = new GuiaEmissaoConsultaDt();
			guiaEmissaoConsultaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			guiaEmissaoConsultaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			guiaEmissaoConsultaDt.setUsuarioDt(UsuarioSessao.getUsuarioDt());
		}
		
		guiaEmissaoConsultaNe = (GuiaEmissaoConsultaNe) request.getSession().getAttribute("guiaEmissaoConsultaNe");
		if (guiaEmissaoConsultaNe == null) guiaEmissaoConsultaNe = new GuiaEmissaoConsultaNe();
		
		request.setAttribute("MensagemErro","");
		
		request.setAttribute("TituloPagina", "Consulta Emissão de Guias");
		request.setAttribute("tempPrograma", "Consulta Emissão de Guias");
		request.setAttribute("tempNomeBusca", "Consulta Emissão de Guias");
		request.setAttribute("tempRetorno", "GuiaEmissaoConsulta");
		request.setAttribute("tempRetornoBuscaProcesso" , "BuscaProcesso");
		request.setAttribute("PaginaAnterior", paginaatual);
		
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		if ((request.getParameter("PassoEditar") != null) && !(request.getParameter("PassoEditar").equals("null"))) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		stAcao = PAGINA_GUIA_EMISSAO_CONSULTA;
		
		switch (paginaatual) {
			case Configuracao.Novo:		
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.setAttribute("tempRetorno", "GuiaEmissaoConsulta");
				stAcao = PAGINA_GUIA_EMISSAO_CONSULTA;
				
				guiaEmissaoConsultaDt = new GuiaEmissaoConsultaDt();
				guiaEmissaoConsultaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				guiaEmissaoConsultaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		
				break;
				
				//Impressão da guia
			case Configuracao.Imprimir : {
				
				GuiaEmissaoCompletaDt guiaEmissaoCompletaDt = (GuiaEmissaoCompletaDt) request.getSession().getAttribute("guiaEmissaoCompletaDt");
				
				if (guiaEmissaoCompletaDt != null && guiaEmissaoConsultaDt != null) {
					//********************************************
					//Pesquisas em Ne auxiliares
					ServentiaDt serventiaDt = null;
					ComarcaDt comarcaDt = null;
					if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt() != null && guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getServentiaCodigo() != null ) {
						serventiaDt = guiaEmissaoConsultaNe.consultarServentiaProcesso(guiaEmissaoConsultaDt);
						if (serventiaDt != null) {
							comarcaDt = guiaEmissaoConsultaNe.consultarComarca(guiaEmissaoConsultaDt, serventiaDt.getId_Comarca());
							if (comarcaDt != null) {
								guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().setId_Comarca(comarcaDt.getId());
								guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().setComarca(comarcaDt.getComarca());
								guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().setComarcaCodigo(comarcaDt.getComarcaCodigo());
							}							
						}
					}					

					//Obtém o próximo número de Guia
					if( guiaEmissaoCompletaDt.getNumeroGuiaCompleto() == null ) {
						guiaEmissaoCompletaDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
						guiaEmissaoCompletaDt.setNumeroGuiaCompleto(guiaEmissaoConsultaNe.getNumeroGuiaCompleto(guiaEmissaoCompletaDt.getNumeroGuia()));
					}
					guiaEmissaoCompletaDt.setListaGuiaItemDt((List) request.getSession().getAttribute("ListaGuiaItemDt") );
					guiaEmissaoCompletaDt.setValorAcao(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getValor());
					guiaEmissaoCompletaDt.setServentia(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getServentia());
					guiaEmissaoCompletaDt.setComarca(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getComarca());
					guiaEmissaoCompletaDt.setComarcaCodigo(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getComarcaCodigo());
					guiaEmissaoCompletaDt.setId_ProcessoTipo(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId_ProcessoTipo());
					guiaEmissaoCompletaDt.setProcessoTipo(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getProcessoTipo());
					
					guiaEmissaoCompletaDt.setListaRequerentes(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaPromoventes());
					guiaEmissaoCompletaDt.setListaRequeridos(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaPromovidos());
					guiaEmissaoCompletaDt.setListaOutrasPartes(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaOutrasPartes());
					//guiaEmissaoCompletaDt.setListaAdvogados(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaAdvogados());
					
					guiaEmissaoCompletaDt.setNumeroProcesso(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getNumeroProcessoCompletoDt().getNumeroCompletoProcesso());
					guiaEmissaoCompletaDt.setId_Usuario(UsuarioSessao.getId_Usuario());
//					
					//Geração da guia PDF
					byte[] byTemp = guiaEmissaoConsultaNe.imprimirGuia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , request.getSession().getAttribute("TotalGuia").toString(), guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getServentia(), guiaEmissaoCompletaDt, guiaEmissaoCompletaDt.getGuiaModeloDt().getId_GuiaTipo(), guiaEmissaoCompletaDt.getGuiaModeloDt().getGuiaTipo());
					
					request.getSession().setAttribute("guiaEmissaoCompletaDt", guiaEmissaoCompletaDt);
					
					//ATENÇÃO: RETIRADO CÓDIGO DE SALVAR.
					
					String nomeGuia = "Guia-numero-" + guiaEmissaoCompletaDt.getNumeroGuiaCompleto() ;										
					
					enviarPDF(response,byTemp,nomeGuia);
					
					return;
				}
				
				break;
			}
				
			case Configuracao.Localizar:
				String numeroProcessoCompleto = request.getParameter("ProcessoNumeroCompleto");
				if (numeroProcessoCompleto == null || numeroProcessoCompleto.trim().length() == 0) {
					super.exibaMensagemInconsistenciaErro(request, "Informe o número do processo.");
				} else {
					guiaEmissaoConsultaDt.getNumeroProcessoCompletoDt().setNumeroCompletoProcessoSemValidacao(numeroProcessoCompleto);
					
					if (!guiaEmissaoConsultaDt.getNumeroProcessoCompletoDt().isValido()) {
						super.exibaMensagemInconsistenciaErro(request, "Número do processo informado é inválido.");	
					} else {
						ProcessoGuiaEmissaoConsultaDt processoGuiaEmissaoConsultaDt = guiaEmissaoConsultaNe.obtenhaProcesso(guiaEmissaoConsultaDt.getNumeroProcessoCompletoDt());
						if (processoGuiaEmissaoConsultaDt == null) {
							super.exibaMensagemInconsistenciaErro(request, "Processo não encontrado.");	
						} else {
							guiaEmissaoConsultaDt.setProcessoGuiaEmissaoConsultaDt(processoGuiaEmissaoConsultaDt);
							
							guiaEmissaoConsultaNe.obtenhaGuiasEmitidas(guiaEmissaoConsultaDt);
							
							if(guiaEmissaoConsultaDt.getListaDeGuiasEmitidas().size() > 0) {
								request.setAttribute("apresentaBotaoCancelar", GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA);								
							}
						}
					}
				}
				
				break;
			case Configuracao.Curinga6:
				switch(passoEditar) {
				
				case Configuracao.Curinga8: {
					
					if( request.getParameter("Id_GuiaEmissao") != null && request.getParameter("hash") != null ) {
						
						String hash = Funcoes.GeraHashMd5(request.getParameter("Id_GuiaEmissao").toString() + GuiaEmissaoNe.NUMERO_SERIE_GUIA);
						String hashParametro = request.getParameter("hash").toString();
					
						if( hash != null && hashParametro != null && hash.equals(hashParametro) ) {
							
							List listaGuiaItemDt = guiaEmissaoConsultaNe.consultarGuiaItens(request.getParameter("Id_GuiaEmissao").toString(), request.getParameter("Id_GuiaTipo"));
							
							//Deve haver no mínimo 1 item de guia
							if( listaGuiaItemDt != null && listaGuiaItemDt.size() > 0 ) {
	
								GuiaEmissaoCompletaDt guiaEmissaoCompletaDt = guiaEmissaoConsultaNe.consultarGuiaEmissaoCompleta(request.getParameter("Id_GuiaEmissao").toString());
								
								GuiaCalculoNe guiaCalculoNe = new GuiaCalculoNe();
								guiaCalculoNe.recalcularTotalGuia(listaGuiaItemDt);
								
								request.setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.setAttribute("TotalGuia", guiaCalculoNe.getTotalGuia() );
								
								request.getSession().setAttribute("ListaGuiaItemDt", listaGuiaItemDt);
								request.getSession().setAttribute("TotalGuia", Funcoes.FormatarDecimal(guiaCalculoNe.getTotalGuia().toString()));
								
								guiaEmissaoConsultaNe.obtenhaPartesProcesso(guiaEmissaoConsultaDt, UsuarioSessao.getNivelAcesso());
								
								//Consulta o usuário
								guiaEmissaoCompletaDt.setUsuario(guiaEmissaoConsultaNe.consultarUsuario(guiaEmissaoCompletaDt.getId_Usuario()) );
								
								//Consultar nome da guia caso seja guia genérica
								if( guiaEmissaoCompletaDt.getId_GuiaTipo() != null && guiaEmissaoCompletaDt.getId_GuiaTipo().length() > 0 ) {
									guiaEmissaoCompletaDt.setGuiaTipo(guiaEmissaoConsultaNe.consultarGuiaTipo( null, guiaEmissaoCompletaDt.getId_GuiaTipo() ) );
								}
								
								guiaEmissaoCompletaDt.setId_Apelante(guiaEmissaoCompletaDt.getId_Apelante());
								guiaEmissaoCompletaDt.setId_Apelado(guiaEmissaoCompletaDt.getId_Apelado());
								
								guiaEmissaoConsultaNe.setNomeApelanteNomeApelado(guiaEmissaoCompletaDt);
								
								if( guiaEmissaoCompletaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_FAZENDA_MUNICIPAL) ) {
									//Altera nome do regimento 61:
									for( int i = 0; i < listaGuiaItemDt.size(); i++ ) {
										GuiaItemDt guiaItemDt = (GuiaItemDt)listaGuiaItemDt.get(i);
										if( guiaItemDt.getCustaDt().getCodigoRegimento().equals("61") ) {// CustaDt.CUSTA_PENHORA
											guiaItemDt.getCustaDt().setArrecadacaoCusta("AUTOS");
										}
										if( guiaItemDt.getCustaDt().getId_ArrecadacaoCusta().equals("31") ) {// DUAM
											guiaItemDt.getCustaDt().setArrecadacaoCusta("DUAM NR. " + guiaEmissaoCompletaDt.getNumeroDUAM() + " (1/" + guiaEmissaoCompletaDt.getQuantidadeParcelasDUAM() + ")");
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
								
								if( guiaEmissaoCompletaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_GUIA_GENERICA) ) {
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
								
								if(guiaEmissaoCompletaDt.getId_ProcessoParteResponsavelGuia() != null && guiaEmissaoCompletaDt.getId_ProcessoParteResponsavelGuia().length() > 0 ) {
									if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaPromoventes() != null ) {
										for(ProcessoParteGuiaEmissaoConsultaDt promovente : guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaPromoventes())
										{
											if( guiaEmissaoCompletaDt.getId_ProcessoParteResponsavelGuia().equals(promovente.getId()) ) {
												guiaEmissaoCompletaDt.setNomeProcessoParteResponsavelGuia(promovente.getNome());
											}
										}
									}
									if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaPromovidos() != null ) {
										for(ProcessoParteGuiaEmissaoConsultaDt promovido : guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getListaPromovidos())
										{
											if( guiaEmissaoCompletaDt.getId_ProcessoParteResponsavelGuia().equals(promovido.getId()) ) {
												guiaEmissaoCompletaDt.setNomeProcessoParteResponsavelGuia(promovido.getNome());
											}
										}
									}
								}								
								
								//Apresentar botão de emissão de guia de locomoção complementar
								String emitirGuiaLocomocaoComplementar = null;
								if( 
									(guiaEmissaoCompletaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_LOCOMOCAO) || guiaEmissaoCompletaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_LOCOMOCAO_COMPLEMENTAR)) && 
									( 
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO)) ||
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_ON_LINE)) || 
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR)) || 
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_INFERIOR)) 
									)
								  ) 
								{
									if (guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt() != null) {
										List<LocomocaoDt> locomocoesNaoUtilizadas = guiaEmissaoConsultaNe.consultarLocomocaoNaoUtilizada(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId() , guiaEmissaoCompletaDt.getId(), true); 
										if (locomocoesNaoUtilizadas.size() > 0) emitirGuiaLocomocaoComplementar = GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_LOC_COMPLEMENTAR;	
									} else {
										emitirGuiaLocomocaoComplementar = GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_LOC_COMPLEMENTAR;
									}									
								}								
								request.setAttribute("emitirGuiaLocomocaoComplementar", emitirGuiaLocomocaoComplementar);
								
								
								//Apresentar botão de emissão de guia de locomoção complementar para a guia inicial
								String emitirGuiaInicialLocomocaoComplementar = null;
								if( 
									guiaEmissaoCompletaDt.getGuiaModeloDt().getId_GuiaTipo().equals(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU) && 
									( 
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO)) ||
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_ON_LINE)) || 
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR)) || 
										guiaEmissaoCompletaDt.getId_GuiaStatus().equals(String.valueOf( GuiaStatusDt.PAGO_COM_VALOR_INFERIOR)) 
									)
								  ) {
									emitirGuiaInicialLocomocaoComplementar = GuiaEmissaoNe.APRESENTAR_LINK_EMITIR_GUIA_INICIAL_LOC_COMPLEMENTAR;
								}
								request.setAttribute("emitirGuiaInicialLocomocaoComplementar", emitirGuiaInicialLocomocaoComplementar);
								
								
								
								request.getSession().setAttribute("guiaEmissaoCompletaDt", guiaEmissaoCompletaDt);
//								request.getSession().setAttribute("ProcessoCadastroDt", processoCadastroDt);
//								request.getSession().setAttribute("processoDt", processoDt);
								request.setAttribute("visualizarBotaoImpressaoBotaoPagamento", guiaEmissaoConsultaNe.visualizarBotaoImpressaoBotaoPagamento(guiaEmissaoCompletaDt));
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
								
								stAcao = PAGINA_GUIA_PREVIA_CALCULO;
							}
							else {
								request.setAttribute("MensagemErro", Configuracao.getMensagem(Configuracao.MENSAGEM_GUIA_SEM_ITEM));
								stAcao = PAGINA_GUIAS_PROCESSO;
							}
							
						}
					}
					
					break;
				}
				
				//cancelar guia emitida caso nao esteja paga
				case Configuracao.Curinga9 : {
					
					if(request.getParameter("GuiaCancelar") != null ) {
						String idGuiaEmissao = request.getParameter("GuiaCancelar").toString().trim();
						
						GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
						guiaEmissaoDt.setId(idGuiaEmissao);
						guiaEmissaoDt.setId_Usuario(UsuarioSessao.getId_Usuario());
						
						boolean cancelada = guiaEmissaoConsultaNe.cancelarGuiaEmitida(guiaEmissaoDt);
						
						guiaEmissaoConsultaNe.obtenhaGuiasEmitidas(guiaEmissaoConsultaDt);
						
						if(guiaEmissaoConsultaDt.getListaDeGuiasEmitidas().size() > 0) {
							request.setAttribute("apresentaBotaoCancelar", GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA);								
						} else {
							request.setAttribute("MensagemErro", "Nenhuma Guia Emitida  Localizada para este Processo.");
						}
						
						if( cancelada ) {
							request.setAttribute("MensagemOk","Guia Cancelada com Sucesso!");
						}
						else {
							request.setAttribute("MensagemErro", "Guia não Cancelada!" );
						}
					}
					
					break;
				}
				
			}
			break;
		}
		
		if(guiaEmissaoConsultaDt != null && guiaEmissaoConsultaDt.getListaDeGuiasEmitidas() != null && guiaEmissaoConsultaDt.getListaDeGuiasEmitidas().size() > 0) {
			request.setAttribute("apresentaBotaoCancelar", GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA);								
		}
		
		request.getSession().setAttribute("guiaEmissaoConsultaDt", guiaEmissaoConsultaDt);
		request.getSession().setAttribute("guiaEmissaoConsultaNe", guiaEmissaoConsultaNe);		
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

}
