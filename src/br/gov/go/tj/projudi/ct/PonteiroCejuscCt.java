package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;

import br.gov.go.tj.projudi.dt.PonteiroCejuscDt;
import br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaConciliadorCejuscDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.PonteiroCejuscNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioCejuscNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class PonteiroCejuscCt extends PonteiroCejuscCtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5742028562106906042L;

//

	public int Permissao() {
		return PonteiroCejuscDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioNe, int paginaatual, String nomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		List listaPautas = new ArrayList();
		
		AudienciaNe audienciaNe = new AudienciaNe();
		PonteiroCejuscNe ponteiroCejuscNe = new PonteiroCejuscNe();
		UsuarioCejuscNe usuarioCejuscNe = new UsuarioCejuscNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		PonteiroCejuscDt ponteiroCejuscDt = null;
		PonteiroCejuscDt ponteiroCejuscDt2 = null;
		ServentiaCargoDt serventiaCargoDt = null;
		String dataInicial;
		String dataFinal;
		String data = "";
		String respostaJson = "";
		String idUsuCejusc = null;
		String idServentia;
		String idAudienciaTipo;
		String idServentiaCargo;
		String fluxo;
		String idPc;
		String bancaPeriodo = "";
		Date dia = null;
		int diaSemana;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar;
		String[] s;
		RequestDispatcher dis;
		
	    GsonBuilder gb = new GsonBuilder(); 
	    gb.serializeNulls();
		
		String stAcao = "";
		
		fluxo = request.getParameter("fluxo");
		
		request.setAttribute("tempPrograma","Pais");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		String[] lisNomeBusca = {"Cejusc"};
		String[] lisDescricao = {"Data","Banca","Conciliador/Mediador","Status"};
		
		request.setAttribute("lisNomeBusca", lisNomeBusca);
		request.setAttribute("lisDescricao", lisDescricao);
		
		request.setAttribute("tempBuscaId", "Id_PonteiroCejusc");
		request.setAttribute("tempBuscaDescricao", "PonteiroCejusc");
		request.setAttribute("tempBuscaPrograma", "PonteiroCejusc");
		request.setAttribute("tempRetorno", "PonteiroCejusc");
		request.setAttribute("tempDescricaoId", "Id");
		request.setAttribute("tempDescricaoDescricao", "PonteiroCejusc");
		request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
		request.setAttribute("PaginaAtual", (Configuracao.Localizar));
		request.setAttribute("PosicaoPaginaAtual", "0");
		request.setAttribute("QuantidadePaginas", "0");
		
		
		//--------------------------------------------------------------------------------------------
		ponteiroCejuscNe =(PonteiroCejuscNe)request.getSession().getAttribute("PonteiroCejuscne");
		if (ponteiroCejuscNe == null )  ponteiroCejuscNe = new PonteiroCejuscNe();  

		ponteiroCejuscDt =(PonteiroCejuscDt)request.getSession().getAttribute("PonteiroCejuscdt");
		if (ponteiroCejuscDt == null )  ponteiroCejuscDt = new PonteiroCejuscDt();  

		ponteiroCejuscDt.setId_UsuCejusc( request.getParameter("Id_UsuCejusc")); 
		ponteiroCejuscDt.setUsuCejusc( request.getParameter("UsuCejusc")); 
		ponteiroCejuscDt.setId_Serv( request.getParameter("Id_Serv")); 
		ponteiroCejuscDt.setServ( request.getParameter("Serv")); 
		ponteiroCejuscDt.setId_UsuServConfirmou( request.getParameter("Id_UsuServConfirmou")); 
		ponteiroCejuscDt.setUsuServConfirmou( request.getParameter("UsuServConfirmou")); 
		ponteiroCejuscDt.setId_ServCargoBanca( request.getParameter("Id_ServCargoBanca")); 
		ponteiroCejuscDt.setData( request.getParameter("Data")); 
		ponteiroCejuscDt.setId_PonteiroCejuscStatus( request.getParameter("Id_PonteiroCejuscStatus")); 
		ponteiroCejuscDt.setPonteiroCejuscStatus( request.getParameter("PonteiroCejuscStatus")); 

		ponteiroCejuscDt.setId_UsuarioLog(usuarioNe.getId_Usuario());
		ponteiroCejuscDt.setIpComputadorLog(request.getRemoteAddr());
		//--------------------------------------------------------------------------------------------
		
		switch(paginaatual){
			case Configuracao.Localizar:
				break; /* Fim Localizar */
				
			case Configuracao.Salvar:
				if( request.getParameter("Id_PonteiroCejuscStatus") != null && 
					 (Integer.parseInt(request.getParameter("Id_PonteiroCejuscStatus")) == PonteiroCejuscStatuDt.REALIZADO || 
					  Integer.parseInt(request.getParameter("Id_PonteiroCejuscStatus")) == PonteiroCejuscStatuDt.FALTOU )
					) {
					
					ponteiroCejuscDt =(PonteiroCejuscDt)request.getSession().getAttribute("PonteiroCejuscdt");
					if (ponteiroCejuscDt == null )  ponteiroCejuscDt = new PonteiroCejuscDt();  
					
					ponteiroCejuscDt.setId_PonteiroCejuscStatus(request.getParameter("Id_PonteiroCejuscStatus"));
					request.getSession().setAttribute("PonteiroCejuscdt", ponteiroCejuscDt);
				}
				
				request.setAttribute("__Pedido__", usuarioNe.getPedido());
				
				stAcao = "/WEB-INF/jsptjgo/PonteiroCejusc.jsp";
				request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
				request.setAttribute("PaginaAnterior", Configuracao.Salvar);
				
				dis =	request.getRequestDispatcher(stAcao);
				dis.include(request, response);
				break; /* Fim Salvar */
				
			case Configuracao.SalvarResultado:
				String strTmp = request.getParameter("Id_PonteiroCejuscStatus");
				if( strTmp != null && ( Integer.parseInt(strTmp) == PonteiroCejuscStatuDt.REALIZADO || Integer.parseInt(strTmp) == PonteiroCejuscStatuDt.FALTOU )) {
					ponteiroCejuscDt.setId_UsuServCompareceu(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
					String Mensagem=ponteiroCejuscNe.Verificar(ponteiroCejuscDt); 

					if (Mensagem.length()==0){
						ponteiroCejuscNe.salvar(ponteiroCejuscDt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					} else {	
						request.setAttribute("MensagemErro", Mensagem );
					}
					
					stAcao = "/WEB-INF/jsptjgo/PonteiroCejusc.jsp";
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					request.setAttribute("PaginaAnterior", Configuracao.SalvarResultado);
					request.getSession().setAttribute("PonteiroCejuscdt", ponteiroCejuscNe.consultarId(ponteiroCejuscDt.getId()));
					
					dis =	request.getRequestDispatcher(stAcao);
					dis.include(request, response);
				} else {

					request.setAttribute("MensagemErro", "Especifique se o conciliador compareceu ou faltou." );
					request.getSession().setAttribute("PonteiroCejuscdt", ponteiroCejuscDt);
					request.getSession().setAttribute("PonteiroCejuscne", ponteiroCejuscNe);
					
					stAcao = "/WEB-INF/jsptjgo/PonteiroCejuscComparecimento.jsp";
					request.setAttribute("PaginaAtual", (Configuracao.Curinga8));
					request.setAttribute("PaginaAnterior", "0");
					
					dis =	request.getRequestDispatcher(stAcao);
					dis.include(request, response);
				}
				
				break; /* Fim SalvarResultado */
			
			//Sortear conciliador/mediador
			case Configuracao.Curinga7:
				if( fluxo != null && fluxo.equals("-1") ){
					stAcao = "/WEB-INF/jsptjgo/PonteiroCejuscSortear.jsp";
					request.setAttribute("PaginaAtual", (Configuracao.Curinga7));
					request.setAttribute("PaginaAnterior", "0");
					RequestDispatcher requestDispatcher = request.getRequestDispatcher(stAcao);
					requestDispatcher.include(request, response);
				}
				else {
					data = request.getParameter("nomeBusca1");
					idPc = request.getParameter("idPc");
					
					// Testa se o usuário especificou uma data
					if( data != null && !data.equals("") ) {
					
							//Se o fluxo for 2, substituir o responsável pela audiencia
							if( fluxo != null && fluxo.equals("2") && idPc != null && !idPc.equals("") ) {
								
								//Obtendo o dt que está tentando alterar
								ponteiroCejuscDt = ponteiroCejuscNe.consultarId(idPc);
								
								//Obtendo dia da semana
								calendar = Calendar.getInstance();
								calendar.setTime( dateFormat.parse(ponteiroCejuscDt.getData()) );
								diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

								//Obtendo parametros a serem utilizados
								idServentia = ponteiroCejuscDt.getId_Serv();
								idServentiaCargo = ponteiroCejuscDt.getId_ServCargoBanca();
								idAudienciaTipo = ponteiroCejuscDt.getId_AudiTipo();
								
								//Descobringo o período da banca em questão
								serventiaCargoDt = serventiaCargoNe.consultarId(idServentiaCargo);
								
								//Sorteando conciliador/mediador de acordo com o período da banca
								idUsuCejusc = null;
								if(serventiaCargoDt != null) {
									if(serventiaCargoDt.getServentiaCargo().toUpperCase().contains("MATUTINO")) {
										
										idUsuCejusc = ponteiroCejuscNe.distribuirComExcecao(data, idAudienciaTipo, idServentia, diaSemana-1, PonteiroCejuscDt.PERIODO_MATUTINO, ponteiroCejuscDt.getId_UsuCejusc());
										bancaPeriodo = "matutino";
									
									} else if(serventiaCargoDt.getServentiaCargo().toUpperCase().contains("VESPERTINO")) {
									
										idUsuCejusc = ponteiroCejuscNe.distribuirComExcecao(data, idAudienciaTipo, idServentia, diaSemana-1, PonteiroCejuscDt.PERIODO_VESPERTINO, ponteiroCejuscDt.getId_UsuCejusc());
										bancaPeriodo = "vespertino";
									
									}

									if( idUsuCejusc != null ) {
										//Mudando o status para substituido
										ponteiroCejuscDt.setId_PonteiroCejuscStatus( String.valueOf(PonteiroCejuscStatuDt.SUBSTITUIDO) );
										ponteiroCejuscDt.setId_UsuarioLog(usuarioNe.getId_Usuario());
										ponteiroCejuscNe.salvar(ponteiroCejuscDt);
										
										//Atribuindo dados ao novo dt
										ponteiroCejuscDt2 = new PonteiroCejuscDt();
										ponteiroCejuscDt2.setId_UsuCejusc(idUsuCejusc);
										ponteiroCejuscDt2.setId_Serv(idServentia);
										ponteiroCejuscDt2.setId_ServCargoBanca(idServentiaCargo);
										ponteiroCejuscDt2.setData(data);
										ponteiroCejuscDt2.setId_AudiTipo(idAudienciaTipo);
										ponteiroCejuscDt2.setId_UsuarioLog(usuarioCejuscNe.consultarIdUsu(idUsuCejusc));
										ponteiroCejuscDt2.setId_PonteiroCejuscStatus(String.valueOf(PonteiroCejuscStatuDt.AVISADO));
										
										//Salvando o ponteiroCejusc
										ponteiroCejuscNe.salvar(ponteiroCejuscDt2);
										
										ponteiroCejuscNe.enviarEmailInformarSorteado(idUsuCejusc, data, bancaPeriodo, ponteiroCejuscDt2.getId());
										
										respostaJson = "[\"substituicaoFeita\"]";
										
									} else {
										//Não encontrou nenhum conciliador/mediador elegível
										respostaJson = "[\"semConciliador\"]";
									}
								
								
								}
								
							} else {
								// Formatando data
								dia = dateFormat.parse(request.getParameter("nomeBusca1"));
								
								// Obtendo dia da semana
								calendar = Calendar.getInstance();
								calendar.setTime(dia);
								diaSemana = calendar.get(Calendar.DAY_OF_WEEK); 
								
								// Buscando a lista de pautas do dia
								listaPautas = (List) audienciaNe.buscarPauta(dia, usuarioNe.getUsuarioDt().getId_Serventia());
			
								// Testa se tem bancas (cargos) com audiência marcada no dia
								if( listaPautas == null || listaPautas.size() == 0 ) {
									respostaJson = "[\"semPauta\"]";
								} else {
									
									// Percorre a lista de bancas (cargos) e sorteia um conciliador/mediador para cada
									for(int i = 0; i < listaPautas.size(); i++){
										ponteiroCejuscDt = new PonteiroCejuscDt();
										
										//Preenchendo o ponteiroCejusc dt
										s = (String[]) listaPautas.get(i);
										idServentia = s[0];
										idServentiaCargo = s[1];
										idAudienciaTipo = s[3];
										
										if( !ponteiroCejuscNe.isBancaOcupada(idServentia, idServentiaCargo, data) ) {
											
											//Descobringo o período da banca em questão
											serventiaCargoDt = serventiaCargoNe.consultarId(idServentiaCargo);
											
											//Sorteando conciliador/mediador de acordo com o período da banca
											idUsuCejusc = null;
											if(serventiaCargoDt != null) {
												if(serventiaCargoDt.getServentiaCargo().toUpperCase().contains("MATUTINO")) {
													
													idUsuCejusc = ponteiroCejuscNe.distribuir(data, idAudienciaTipo, idServentia, diaSemana-1, PonteiroCejuscDt.PERIODO_MATUTINO);
													bancaPeriodo = "matutino";
												
												} else if(serventiaCargoDt.getServentiaCargo().toUpperCase().contains("VESPERTINO")) {
												
													idUsuCejusc = ponteiroCejuscNe.distribuir(data, idAudienciaTipo, idServentia, diaSemana-1, PonteiroCejuscDt.PERIODO_VESPERTINO);
													bancaPeriodo = "vespertino";
												
												}
												
												if(idUsuCejusc != null && !idUsuCejusc.equals("")) {
													ponteiroCejuscDt.setId_UsuCejusc(idUsuCejusc);
													ponteiroCejuscDt.setId_Serv(idServentia);
													ponteiroCejuscDt.setId_ServCargoBanca(idServentiaCargo);
													ponteiroCejuscDt.setData(data);
													ponteiroCejuscDt.setId_AudiTipo(idAudienciaTipo);
													ponteiroCejuscDt.setId_UsuarioLog(usuarioCejuscNe.consultarIdUsu(idUsuCejusc));
													ponteiroCejuscDt.setId_PonteiroCejuscStatus(String.valueOf(PonteiroCejuscStatuDt.AVISADO));
													
													//Salvando o ponteiroCejusc
													ponteiroCejuscNe.salvar(ponteiroCejuscDt);
													
													
													ponteiroCejuscNe.enviarEmailInformarSorteado(idUsuCejusc, data, bancaPeriodo, ponteiroCejuscDt.getId());
												}
											}
										}
										
									}  
									
									respostaJson = ponteiroCejuscNe.consultaPorDataJSON(data, usuarioNe.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);
								
								}
						}
					} else {
						respostaJson = "[\"semData\"]";
					}
					
					request.setAttribute("PaginaAnterior", "0");
					
					enviarJSON(response, respostaJson);
					
					
				}
				break; /* Fim Curinga7 */
			
			//Confirmar Comparecimento
			case Configuracao.Curinga8:
				
				switch(fluxo) {
					case "2":
						data = request.getParameter("nomeBusca1");
						idPc = request.getParameter("idPc");
						//Listar pautas para o dia
						if( fluxo != null && fluxo.equals("2") ) {
							// Testa se o usuário especificou uma data
							if( data != null && !data.equals("") ) {
								respostaJson = ponteiroCejuscNe.consultaPonteiroCejuscDataStatusJSON(data, usuarioNe.getUsuarioDt().getId_Serventia(), PonteiroCejuscStatuDt.CONFIRMADO, PosicaoPaginaAtual);
							}
							else {
								respostaJson = "[\"semData\"]";
							}
						}
						request.setAttribute("PaginaAnterior", "0");
						enviarJSON(response,respostaJson);
						break;
						
					case "3":
						paginaatual = -1;
						super.executar(request, response, usuarioNe, paginaatual, "ponteiro", PosicaoPaginaAtual);
						break;
						
					default:
						stAcao = "/WEB-INF/jsptjgo/PonteiroCejuscComparecimento.jsp";
						request.setAttribute("PaginaAtual", (Configuracao.Curinga8));
						request.setAttribute("PaginaAnterior", "0");
						RequestDispatcher requestDispatcher = request.getRequestDispatcher(stAcao);
						requestDispatcher.include(request, response);
						break;
				}
				
			break; /* Fim Curinga8 */
				
				
				
				
				
//				if( fluxo != null && fluxo.equals("-1") ){
//					stAcao = "/WEB-INF/jsptjgo/PonteiroCejuscComparecimento.jsp";
//					request.setAttribute("PaginaAtual", (Configuracao.Curinga8));
//					request.setAttribute("PaginaAnterior", "0");
//					RequestDispatcher requestDispatcher = request.getRequestDispatcher(stAcao);
//					requestDispatcher.include(request, response);
//				}
//				else {
//					
//					if( fluxo.equals("2") ) {
//						
//						data = request.getParameter("nomeBusca1");
//						idPc = request.getParameter("idPc");
//						
//						//Listar pautas para o dia
//						if( fluxo != null && fluxo.equals("2") ) {
//							
//							// Testa se o usuário especificou uma data
//							if( data != null && !data.equals("") ) {
//								respostaJson = ponteiroCejuscNe.consultaDataStatusJSON(data, usuarioNe.getUsuarioDt().getId_Serventia(), PonteiroCejuscStatuDt.CONFIRMADO, PosicaoPaginaAtual);
//							}
//							else {
//								respostaJson = "[\"semData\"]";
//							}
//							
//						}
//						
//						request.setAttribute("PaginaAnterior", "0");
//						
//						enviarJSON(response,respostaJson);
//						
//						
//					} else {
//						if( fluxo.equals("3") ) {
//							paginaatual = -1;
//							super.executar(request, response, usuarioNe, paginaatual, "ponteiro", PosicaoPaginaAtual);
//						}
//					}
//				}
//				break; /* Fim Curinga8 */
			
			//Relatório de audiências por conciliador/mediador
			case Configuracao.Curinga9:
				
				RequestDispatcher requestDispatcher;
				String tempFluxo1 = request.getParameter("tempFluxo1");
				
				if(tempFluxo1 != null && !tempFluxo1.equals("")) {
					fluxo = tempFluxo1;
				}
				
				stAcao = "/WEB-INF/jsptjgo/PonteiroCejuscRelatorio.jsp";
				
				if( fluxo != null ) {
					
					
						switch(fluxo) {
						
							case "-1":
								//Carregar página inicial
								lisDescricao = new String[2];
								lisDescricao[0] = "Nome";
								lisDescricao[1] = "Quantidade";
								lisNomeBusca = new String[2];
								lisNomeBusca[0] = "nomeBusca1";
								lisNomeBusca[1] = "nomeBusca2";
								request.setAttribute("lisDescricao", lisDescricao);
								request.setAttribute("lisNomeBusca", lisNomeBusca);
								request.setAttribute("PaginaAtual", (Configuracao.Curinga9));
								request.setAttribute("PaginaAnterior", "0");
								request.setAttribute("stTempRetorno", "PonteiroCejusc");
								request.setAttribute("boMostrarExcluir", false);
								request.setAttribute("tempFluxo1", "2");
								request.setAttribute("PosicaoPaginaAtual", 0);
								requestDispatcher = request.getRequestDispatcher(stAcao);
								requestDispatcher.include(request, response);
								break; /* Fim -1 */
							
							case "2":
								//Fazer consulta e trazer resultado
								dataInicial = request.getParameter("nomeBusca1");
								dataFinal = request.getParameter("nomeBusca2");
								String retorno = ponteiroCejuscNe.consultaRelatorioAudienciaConciliador(dataInicial, dataFinal, "0");
								if (retorno != null && retorno.length() > 0) {
									request.setAttribute("PaginaAnterior", "0");
									request.setAttribute("PosicaoPaginaAtual", PosicaoPaginaAtual);
									
									enviarJSON(response, retorno);
									return;
								} else {
									request.setAttribute("MensagemErro", "Nenhuma agenda disponível.");
									request.setAttribute("PaginaAtual", (Configuracao.Curinga9));
									request.setAttribute("PaginaAnterior", "0");								
								}
								break; /* Fim 2 */
								
							case "3":
								//Gerar e devolver relatório em pdf
								byte[] byTemp = null;
								
								dataInicial = request.getParameter("dataInicialConsulta");
								dataFinal = request.getParameter("dataFinalConsulta");
								byTemp = ponteiroCejuscNe.relAudienciaConciliador(dataInicial, dataFinal, ProjudiPropriedades.getInstance().getCaminhoAplicacao(), ProjudiPropriedades.getInstance().getCaminhoAplicacao());																

								enviarPDF(response, byTemp,"Relatorio");
								
								break; /* Fim 3 */
						
						}
					
				}
				
				break; /* Fim Curinga9 */
				
				
				
			default:
				super.executar(request, response, usuarioNe, paginaatual, "ponteiro", PosicaoPaginaAtual);
				break; /* Fim default */
		}
		
	}
	
}