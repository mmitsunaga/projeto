package br.gov.go.tj.projudi.ct;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.AreaDt; 
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.MandadoJudicialNe;
import br.gov.go.tj.projudi.ne.MandadoJudicialStatusNe;
import br.gov.go.tj.projudi.ne.MandadoTipoRedistribuicaoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class MandadoJudicialCt extends MandadoJudicialCtGen {

	private static final long serialVersionUID = -4074111750305923853L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoJudicialDt MandadoJudicialdt;
		MandadoJudicialNe MandadoJudicialne;
		List tempList = null;
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String novaDataLimite = request.getParameter("novaDataLimite");
		novaDataLimite = novaDataLimite != null ? novaDataLimite : "";
		request.setAttribute("novaDataLimite", novaDataLimite);
		
		String stAcao = "";
		if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equals("salvarAlterarDataLimite")) {
			stAcao = "/WEB-INF/jsptjgo/AlterarDataMandadoJudicial.jsp";
		}
		else {
			stAcao = "/WEB-INF/jsptjgo/MandadoJudicial.jsp";
		}

		request.setAttribute("tempPrograma", "MandadoJudicial");
		
		MandadoJudicialne = (MandadoJudicialNe) request.getSession().getAttribute("MandadoJudicialne");
		if (MandadoJudicialne == null) MandadoJudicialne = new MandadoJudicialNe();

		MandadoJudicialdt = (MandadoJudicialDt) request.getSession().getAttribute("MandadoJudicialdt");
		if (MandadoJudicialdt == null) MandadoJudicialdt = new MandadoJudicialDt();

		if(request.getParameter("redistribuirPlantao") != null && request.getParameter("redistribuirPlantao").length() > 0) {
			request.setAttribute("redistribuirPlantao", request.getParameter("redistribuirPlantao"));
		}
			
		
		MandadoJudicialdt.setId_MandadoTipo(request.getParameter("Id_MandadoTipo"));
		MandadoJudicialdt.setMandadoTipo(request.getParameter("MandadoTipo"));
		MandadoJudicialdt.setId_MandadoJudicialStatus(request.getParameter("Id_MandadoJudicialStatus"));
		MandadoJudicialdt.setMandadoJudicialStatus(request.getParameter("MandadoJudicialStatus"));
		MandadoJudicialdt.setId_Area(request.getParameter("Id_Area"));
		MandadoJudicialdt.setArea(request.getParameter("Area"));
		MandadoJudicialdt.setId_Zona(request.getParameter("Id_Zona"));
		MandadoJudicialdt.setZona(request.getParameter("Zona"));
		MandadoJudicialdt.setId_Regiao(request.getParameter("Id_Regiao"));
		MandadoJudicialdt.setRegiao(request.getParameter("Regiao"));
		MandadoJudicialdt.setId_Bairro(request.getParameter("Id_Bairro"));
		MandadoJudicialdt.setBairro(request.getParameter("Bairro"));
		MandadoJudicialdt.setId_ProcessoParte(request.getParameter("Id_ProcessoParte"));
		MandadoJudicialdt.setProcessoParte(request.getParameter("NomeParte"));
		MandadoJudicialdt.setId_EnderecoParte(request.getParameter("Id_EnderecoParte"));
		MandadoJudicialdt.setEnderecoParte(request.getParameter("EnderecoParte"));
		MandadoJudicialdt.setId_Pendencia(request.getParameter("Id_Pendencia"));
		MandadoJudicialdt.setPendencia(request.getParameter("Pendencia"));
		MandadoJudicialdt.setId_Escala(request.getParameter("Id_Escala"));
		MandadoJudicialdt.setId_UsuarioServentia_1(request.getParameter("Id_UsuarioServentia"));
		MandadoJudicialdt.setId_UsuarioServentia_2(request.getParameter("Id_UsuarioServentia_2"));
		
		
		
		
		if(MandadoJudicialdt.getUsuarioServentiaDt_1() != null){
			MandadoJudicialdt.getUsuarioServentiaDt_1().setId(request.getParameter("Id_UsuarioServentia"));
			MandadoJudicialdt.getUsuarioServentiaDt_1().setNome(request.getParameter("UsuarioServentia"));
		}
		
		if(MandadoJudicialdt.getUsuarioServentiaDt_2() != null) {
			MandadoJudicialdt.getUsuarioServentiaDt_2().setId(request.getParameter("Id_UsuarioServentia_2"));
			MandadoJudicialdt.getUsuarioServentiaDt_2().setNome(request.getParameter("UsuarioServentia_2"));
		}
		
		MandadoJudicialdt.setEscala(request.getParameter("Escala"));
		MandadoJudicialdt.setValor(request.getParameter("Valor"));
		MandadoJudicialdt.setAssistencia(request.getParameter("Assistencia"));
		MandadoJudicialdt.setLocomocoesFrutiferas(request.getParameter("LocomocoesFrutiferas"));
		MandadoJudicialdt.setLocomocoesInfrutiferas(request.getParameter("LocomocoesInfrutiferas"));
		if (request.getParameter("LocomocaoHoraMarcada") != null)
			MandadoJudicialdt.setLocomocaoHoraMarcada(request.getParameter("LocomocaoHoraMarcada"));
		else
			MandadoJudicialdt.setLocomocaoHoraMarcada("false");
		MandadoJudicialdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoJudicialdt.setIpComputadorLog(request.getRemoteAddr());		

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				switch( Funcoes.StringToInt(UsuarioSessao.getGrupoCodigo()) ) {
					
					case GrupoDt.OFICIAL_JUSTICA:
						String fluxo = (String)request.getParameter("fluxo");
						if(fluxo == null) {
							request.setAttribute("serventia", UsuarioSessao.getServentia());
							
							List<EscalaDt> listaEscala = MandadoJudicialne.consultarServentiaCargoEscalaPorServentiaCargo(UsuarioSessao.getId_ServentiaCargo());
							request.setAttribute("listaEscala", listaEscala);
							
							stAcao = "/WEB-INF/jsptjgo/MeusMandados.jsp";
						}
						else if(fluxo != null && fluxo.equals("1")){
							
							if (request.getParameter("Passo")==null){
									String[] lisNomeBusca = {"dataInicial","dataFinal"};
									String[] lisDescricao = {"mandTipo", "area", "zona", "regiao", "dataDist", "dataLimite", "situacao"};
									stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
									request.setAttribute("tempBuscaId", "Id_MandadoJudicial");
									request.setAttribute("tempBuscaDescricao", "MandadoJudicial");
									request.setAttribute("tempBuscaPrograma", "MandadoJudicial");
									request.setAttribute("tempRetorno", "MandadoJudicial?fluxo=2");
									request.setAttribute("tempDescricaoId", "Id");
									request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
									request.setAttribute("PaginaAtual", (Configuracao.Localizar));
									request.setAttribute("PosicaoPaginaAtual", "0");
									request.setAttribute("QuantidadePaginas", "0");
									request.setAttribute("lisNomeBusca", lisNomeBusca);
									request.setAttribute("lisDescricao", lisDescricao);
									MandadoJudicialdt.limpar();
							} else {
									String stTemp = "";
									String dataInicial = (String) request.getParameter("nomeBusca1");
									String dataFinal = (String) request.getParameter("nomeBusca2");
									
									//Se não for informadas data inicial ou data final utilizo o primeiro e último dia do mês.
									if(dataInicial == null || dataInicial.isEmpty()) {
										dataInicial = Funcoes.dateToStringSoData(Funcoes.getPrimeiroDiaMes());
									}
									if(dataFinal == null || dataFinal.isEmpty()) {
										dataFinal = Funcoes.dateToStringSoData(Funcoes.getUltimoDiaMes());
									}
									
									//Se a data inicial for maior do que a data final ou a diferença for maior do que 30 dias não faz a consulta.
									if(Funcoes.validaDataInicialMenorDataFinal(dataInicial, dataFinal) && Funcoes.diferencaDatas(dataFinal+" 00:00:00", dataInicial+" 00:00:00")[0] < 30){
										stTemp = MandadoJudicialne.consultarMeusMandadosOficialJSON(UsuarioSessao.getId_UsuarioServentia(), dataInicial, dataFinal, PosicaoPaginaAtual);
										enviarJSON(response, stTemp);
									}
									else {
										response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"A data final deve ser maior do que a data inicial e a diferença não deve ser maior do que 30 dias.");
									}
									return;					
							}
						}
						else if(fluxo != null && fluxo.equals("2")) {
							//Aba 2
							String stTemp = MandadoJudicialne.consultarSituacaoDistribuicaoCentral(UsuarioSessao.getId_Serventia(), (String) request.getParameter("nomeBusca3"), "0");
							enviarJSON(response, stTemp);
							return;
						}
						break;
				
					default:
						if (request.getParameter("Passo")==null){
							String[] lisNomeBusca = {"Area"};
							String[] lisDescricao = {"Area"};
							stAcao="/WEB-INF/jsptjgo/Padroes/MandadoLocalizar.jsp";
							request.setAttribute("tempBuscaId","Id_Area");
							request.setAttribute("tempBuscaDescricao","MandadoJudicial");
							request.setAttribute("tempBuscaPrograma","Area");			
							request.setAttribute("tempRetorno","MandadoJudicial");		
							request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
							request.setAttribute("PaginaAtual", (AreaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
							request.setAttribute("PosicaoPaginaAtual", "0");
							request.setAttribute("QuantidadePaginas", "0");
							request.setAttribute("lisNomeBusca", lisNomeBusca);
							request.setAttribute("lisDescricao", lisDescricao);
						} else {
							String stTemp="";
							stTemp = MandadoJudicialne.consultarDescricaoAreaJSON(stNomeBusca1, PosicaoPaginaAtual);
							response.setContentType("text/x-json");
							try{
								response.getOutputStream().write(stTemp.getBytes());
								response.flushBuffer();
							} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
							return;								
						}
						break;				
				}

			break;
			
			case Configuracao.Salvar: 
				
				
				if (request.getParameter("Fluxo") != null && (request.getParameter("Fluxo").equalsIgnoreCase("salvarAnalisePagamentoGratuito"))) {
					//Fluxo para tela de aprovação de mandados gratuitos
					request.setAttribute("Mensagem", "Clique para atualizar a ordem de pagamento.");
			        request.setAttribute("botaoAutorizar", "desativar");
	  	            request.setAttribute("botaoNegar", "desativar");
					request.setAttribute("Fluxo", request.getParameter("Fluxo"));
				   	request.setAttribute("htmlCertidao", "");
				   	
				   	MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
					MandadoJudicialDt mandadoJudicialDt; 
				    MandadoJudicialStatusNe mandadoJudicialStatusNe = new MandadoJudicialStatusNe();
				    List listaStatusMandado = new ArrayList();
					    
					mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
					request.setAttribute("idMandJud", mandadoJudicialDt.getId());
				    request.setAttribute("idMandJudPagamentoStatus",  MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO);
				    mandadoJudicialDt.setId_MandadoJudicialStatus(request.getParameter("idMandJudStatus"));
				    mandadoJudicialDt.setResolutivo(request.getParameter("mandJudResolutivo"));	
					listaStatusMandado = mandadoJudicialStatusNe.consultarListaStatus();
		            request.setAttribute("listaStatusMandado", listaStatusMandado);	    
				    request.setAttribute("mandadoJudicial", mandadoJudicialDt);
				   	request.setAttribute("tempRetorno", "MandadoJudicial");					   	
				   	request.setAttribute("htmlCertidao" , null);					   	
					stAcao = "/WEB-INF/jsptjgo/EditaOrdemPagamentoGratuito.jsp";						
			
				
				
				}  
				else if (request.getParameter("Fluxo") != null && (request.getParameter("Fluxo").equalsIgnoreCase("alteraStatusOrdemPg"))) {
					request.setAttribute("Mensagem", "Clique para atualizar a ordem de pagamento.");
			        request.setAttribute("botaoAutorizar", "desativar");
	  	            request.setAttribute("botaoNegar", "desativar");
					request.setAttribute("Fluxo", request.getParameter("Fluxo"));
				   	request.setAttribute("htmlCertidao", "");
				   	
				   	//HELLENO
				   	request.setAttribute("quantidadeLocomocao", request.getParameter("quantidadeLocomocao"));
				   	
					
				   	MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
					MandadoJudicialDt mandadoJudicialDt; 
				    MandadoJudicialStatusNe mandadoJudicialStatusNe = new MandadoJudicialStatusNe();
					MandadoJudicialStatusDt mandadoJudicialStatusDt = new MandadoJudicialStatusDt();
				    List listaStatusMandado = new ArrayList();
					    
					mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
					if(request.getParameter("quantidadeLocomocao") != null && !request.getParameter("quantidadeLocomocao").equals(request.getParameter("qtdOrigLoc"))){
				   	   mandadoJudicialDt.setValorLocomocao("");
				   	}
					request.setAttribute("idMandJud", mandadoJudicialDt.getId());
					if (request.getParameter("idMandJudPagamentoStatus").equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO)) {					
				       request.setAttribute("idMandJudPagamentoStatus",  MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO);
					} else { 											
					   request.setAttribute("idMandJudPagamentoStatus",  MandadoJudicialDt.ID_PAGAMENTO_NEGADO); 						
					}	
				    mandadoJudicialDt.setId_MandadoJudicialStatus(request.getParameter("idMandJudStatus"));	
					listaStatusMandado = mandadoJudicialStatusNe.consultarListaStatus();
		            request.setAttribute("listaStatusMandado", listaStatusMandado);	    
				    request.setAttribute("mandadoJudicial", mandadoJudicialDt);
				   	request.setAttribute("tempRetorno", "MandadoJudicial");					   	
				   	request.setAttribute("htmlCertidao" , null);					   	
					stAcao = "/WEB-INF/jsptjgo/EditaOrdemPagamento.jsp";						
			
				} else {                
				
					request.setAttribute("Mensagem", "Clique para salvar");
			        request.setAttribute("Fluxo", request.getParameter("Fluxo"));			        
				}
			break;
				
			case Configuracao.SalvarResultado: 
         
				if (request.getParameter("Fluxo") != null && (request.getParameter("Fluxo").equalsIgnoreCase("salvarAnalisePagamentoGratuito"))) {
					//Fluxo para aprovação de mandado gratuito
					MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe(); 

					if (Funcoes.StringToInt(request.getParameter("mandJudResolutivo")) != MandadoJudicialDt.SIM_RESOLUTIVO
							&& Funcoes.StringToInt(request.getParameter("mandJudResolutivo")) != MandadoJudicialDt.NAO_RESOLUTIVO) {							
						request.setAttribute("MensagemOk", "É obrigatório especificar se o mandado foi resolutivo ou não.");		 
					}
					else {					 
					  MandadoJudicialdt.setId(request.getParameter("idMandJud"));
				      MandadoJudicialdt.setIdMandJudPagamentoStatus(MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO);				    
				      //MandadoJudicialdt.setMandJudPagamentoStatus(MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO);
                      MandadoJudicialdt.setNomeUsuarioServentia_1(request.getParameter("nomeUsuarioServentia_1"));                    
                      MandadoJudicialdt.setIdUsuPagamentoStatus(UsuarioSessao.getId_Usuario());
                      MandadoJudicialdt.setNomeUsuPagamentoStatus(request.getParameter("nomeUsuPagamentoStatus"));
                      MandadoJudicialdt.setDataPagamentoStatus(request.getParameter("dataPagamentoStatus"));
                      MandadoJudicialdt.setId_MandadoJudicialStatus(request.getParameter("idMandJudStatus"));
                      MandadoJudicialdt.setResolutivo(request.getParameter("mandJudResolutivo"));
					  mandadoJudicialNe.alteraPagamentoStatusGratuito(MandadoJudicialdt);	
					  request.setAttribute("MensagemOk", "Análise de Adicional de Mandado Gratuito Registrada.");
					}
					ServentiaNe servNe = new ServentiaNe();
				    ServentiaDt  servDt = servNe.consultarIdSimples(UsuarioSessao.getId_Serventia());
				    String nomeServentia = servDt.getServentia();		  
				    request.setAttribute("nomeServentia", nomeServentia);
				    request.setAttribute("dataReferencia", Funcoes.FormatarData(new Date()));	
					request.setAttribute("mandadoJudicial", null);
			       	request.setAttribute("tempRetorno", "MandadoJudicial");
			    	stAcao =  "/WEB-INF/jsptjgo/AutorizaOrdemPagamentoGratuito.jsp";	
			    	
			        List listaStatusPagamento = new ArrayList();	
					listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatus();	
					request.setAttribute("listaStatusPagamento", listaStatusPagamento);		
					
					
					
					
					
					
					
					
					
					
				}
				else if (request.getParameter("Fluxo") != null && (request.getParameter("Fluxo").equalsIgnoreCase("alteraStatusOrdemPg"))) { 
					
					MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe(); 

					if (request.getParameter("idMandJudStatus").equalsIgnoreCase(Integer.toString(MandadoJudicialStatusDt.ID_FRUSTRADO))
							&& !request.getParameter("idMandJudPagamentoStatus").equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_NEGADO))							
						request.setAttribute("MensagemOk", "Para status de mandado frustado e preciso que o pagamento seja negado.");		 
					else {					 
					  MandadoJudicialdt.setId(request.getParameter("idMandJud"));
				      MandadoJudicialdt.setIdMandJudPagamentoStatus(request.getParameter("idMandJudPagamentoStatus"));				    
				      MandadoJudicialdt.setMandJudPagamentoStatus(request.getParameter("mandJudPagamentoStatus"));
                      MandadoJudicialdt.setNomeUsuarioServentia_1(request.getParameter("nomeUsuarioServentia_1"));                    
                      if (MandadoJudicialdt.getIdMandJudPagamentoStatus().equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_NEGADO)) 
                         MandadoJudicialdt.setQuantidadeLocomocao(request.getParameter("0"));
                      else MandadoJudicialdt.setQuantidadeLocomocao(request.getParameter("quantidadeLocomocao"));
                      MandadoJudicialdt.setValorLocomocao(request.getParameter("valorLocomocao"));
                      MandadoJudicialdt.setIdUsuPagamentoStatus(UsuarioSessao.getId_Usuario());
                      MandadoJudicialdt.setNomeUsuPagamentoStatus(request.getParameter("nomeUsuPagamentoStatus"));
                      MandadoJudicialdt.setDataPagamentoStatus(request.getParameter("dataPagamentoStatus"));
                      MandadoJudicialdt.setId_MandadoJudicialStatus(request.getParameter("idMandJudStatus"));	
					  mandadoJudicialNe.alteraPagamentoStatus(MandadoJudicialdt);	
					  request.setAttribute("MensagemOk", "Ordem de Pagamento Atualizada.");
					}
					ServentiaNe servNe = new ServentiaNe();
				    ServentiaDt  servDt = servNe.consultarIdSimples(UsuarioSessao.getId_Serventia());
				    String nomeServentia = servDt.getServentia();		  
				    request.setAttribute("nomeServentia", nomeServentia);
				    request.setAttribute("dataReferencia", Funcoes.FormatarData(new Date()));	
					request.setAttribute("mandadoJudicial", null);
			       	request.setAttribute("tempRetorno", "MandadoJudicial");
			    	stAcao =  "/WEB-INF/jsptjgo/AutorizaOrdemPagamento.jsp";	
			    	
			        List listaStatusPagamento = new ArrayList();	
					listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatus();	
					request.setAttribute("listaStatusPagamento", listaStatusPagamento);		
									
				} else {
				 
				
				if(request.getParameter("Fluxo") != null && !request.getParameter("Fluxo").equalsIgnoreCase("") ) {
					
					if(request.getParameter("Fluxo").equalsIgnoreCase("salvarOficial")) {
						MandadoJudicialne.salvar(MandadoJudicialdt);
						request.setAttribute("MensagemOk", "Dados salvos com sucesso.");
					}
					else if(request.getParameter("Fluxo").equalsIgnoreCase("salvarAlterarDataLimite")) {
						
						// SALVA A NOVA DATA LIMITE
						if(novaDataLimite != null && !novaDataLimite.isEmpty()) {
							MandadoJudicialdt.setDataLimite(novaDataLimite);
							MandadoJudicialne.alterarDataLimite(MandadoJudicialdt);
							request.setAttribute("Fluxo", "inicioAlterarData");
							request.setAttribute("novaDataLimite", "");
							MandadoJudicialdt.limpar();
							
							stAcao = "/WEB-INF/jsptjgo/AlterarDataMandadoJudicial.jsp";
							request.setAttribute("MensagemOk", "Data alterada com sucesso.");
						} 
						else {
							request.setAttribute("Fluxo", "resultadoConsultaAlterarData");
							request.setAttribute("MensagemErro", "Nova data inválida.");
						}
					}
				}			
			
				}
				break;
				
			case Configuracao.Novo:
		 
			  MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();				       
			  if (request.getParameter("id") != null && !request.getParameter("id").equalsIgnoreCase("")) {   		   
				  MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
				  mandadoJudicialDt = mandadoJudicialNe.consultaMandado(request.getParameter("id"), 
				             UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getGrupoCodigo(), UsuarioSessao.getId_Serventia()); 
				  if (mandadoJudicialDt.getProcNumero().equalsIgnoreCase("")) {
				      request.setAttribute("MensagemErro", "Mandado não localizado para este perfil"); 
				  } else { String[] htmlCertidao = mandadoJudicialNe.retornarConteudoArquivoMandado(mandadoJudicialDt.getId_Pendencia(), UsuarioSessao);
	                       request.setAttribute("htmlCertidao", htmlCertidao);
		            }  				  
			  } else request.setAttribute("MensagemOk", "Informe o número do mandado");  
			  
			  request.setAttribute("mandadoJudicial", mandadoJudicialDt); 
			  request.setAttribute("tempRetorno", "MandadoJudicial");
			  stAcao = "/WEB-INF/jsptjgo/ConsultaMandado.jsp";	 		    
				
			break;
			
			case (MandadoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MandadoTipo"};
					String[] lisDescricao = {"Tipo de Mandado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";	
					request.setAttribute("tempBuscaId","Id_MandadoTipo");
					request.setAttribute("tempBuscaDescricao","MandadoTipo");
					request.setAttribute("tempBuscaPrograma","MandadoTipo");			
					request.setAttribute("tempRetorno","MandadoJudicial");	
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (MandadoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = MandadoJudicialne.consultarDescricaoMandadoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					response.setContentType("text/x-json");
					try{
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
			break;
			
			case (AreaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Area"};
					String[] lisDescricao = {"Area"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Area");
					request.setAttribute("tempBuscaDescricao","Area");
					request.setAttribute("tempBuscaPrograma","Area");			
					request.setAttribute("tempRetorno","MandadoJudicial");		
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					try{
						stTemp = MandadoJudicialne.consultarDescricaoAreaJSON(stNomeBusca1, PosicaoPaginaAtual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
			break;
			
			// Redistribuir Mandado  /  Autoriza Ordem Pagamento
			
			case Configuracao.Curinga6:
				//Autorização de pagamento
			   
				if(isFluxoAutorizacaoPagamentoCustas(request.getParameter("Fluxo"))){
			       ServentiaDt servDt;
			       ServentiaNe servNe = new ServentiaNe();
			       
			       servDt = servNe.consultarIdSimples(UsuarioSessao.getId_Serventia());
			       String nomeServentia = servDt.getServentia();		  
			       request.setAttribute("nomeServentia", nomeServentia);	
			       request.setAttribute("dataReferencia", Funcoes.FormatarData(new Date()));	
				   stAcao = autorizaPagamentoCustas(request, response, UsuarioSessao, request.getParameter("Fluxo") );
				} 
				
				else if(isFluxoAutorizacaoPagamentoGratuito(request.getParameter("Fluxo"))) {
				   ServentiaDt servDt;
			       ServentiaNe servNe = new ServentiaNe();
			       
			       servDt = servNe.consultarIdSimples(UsuarioSessao.getId_Serventia());
			       String nomeServentia = servDt.getServentia();		  
			       request.setAttribute("nomeServentia", nomeServentia);	
			       request.setAttribute("dataReferencia", Funcoes.FormatarData(new Date()));	
				   stAcao = autorizaPagamentoGratuito(request, response, UsuarioSessao, request.getParameter("Fluxo") );
				}
				
				else {				
					String idMandTipoRedist = "";
					if (request.getParameter("idMandTipoRedist") != null && !request.getParameter("idMandTipoRedist").equalsIgnoreCase("null")) {
						idMandTipoRedist = request.getParameter("idMandTipoRedist");
						request.getSession().setAttribute("idMandTipoRedist", idMandTipoRedist);						
					} else {
						request.getSession().setAttribute("idMandTipoRedist",  "");
					}
				
				//Redistribuição de mandado		 
					
					
					if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equalsIgnoreCase("consultar")) {
					   tempList = MandadoJudicialne.consultarMandadosAbertos(request.getParameter("NumeroMandado"), request.getParameter("Id_UsuarioServentia_2"), UsuarioSessao.getUsuarioDt().getId_Serventia());
				    } else if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equalsIgnoreCase("redistribuir")) {
	 					     if (idMandTipoRedist.equalsIgnoreCase("")) {
						         request.setAttribute("MensagemErro", "Informe o motivo da redistribuição");
					       } else {
					              String[] mandados = null;
					              
					              String numero = request.getParameter("NumeroMandado");
			   		              if (request.getParameter("mandados") != null) {
						             mandados = request.getParameterValues("mandados");
						             if( request.getParameter("redistribuirPlantao") != null && request.getParameter("redistribuirPlantao").equals("sim") ){
							             MandadoJudicialne.redistribuirPlantao(mandados, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog(), null, idMandTipoRedist);
						             } else {
							                MandadoJudicialne.redistribuir(mandados, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog(), null, idMandTipoRedist);
						               }
						             request.getSession().setAttribute("idMandTipoRedist", "");
						             request.setAttribute("MensagemOk", "Mandados redistribuídos com sucesso");
					              } else {						             
						               request.setAttribute("MensagemErro", "Selecione ao menos um mandado");
					                }
					           }            
				      }
				MandadoTipoRedistribuicaoNe objNe = new MandadoTipoRedistribuicaoNe();	
				List listaTipo = objNe.consultaMandadoTipoRedistribuicao();	
				
				request.setAttribute("listaTipo", listaTipo);					
				request.setAttribute("idMandTipoRedist", request.getSession().getAttribute("idMandTipoRedist"));
				request.setAttribute("NumeroMandado", request.getParameter("NumeroMandado"));
				request.setAttribute("ListaMandados", tempList);
				stAcao = "/WEB-INF/jsptjgo/RedistribuirMandadoJudicial.jsp";					
		    }
			break;
			
			// Inserir Oficial Companheiro
			case Configuracao.Curinga7:
			    PendenciaDt pendenciaDt = new PendenciaDt();
				
				if(request.getParameter("NumeroMandado") != null && !request.getParameter("NumeroMandado").equalsIgnoreCase("")) {
					MandadoJudicialdt = MandadoJudicialne.consultarIdFinalizado(request.getParameter("NumeroMandado"));
					if(MandadoJudicialdt != null) {
					   pendenciaDt = MandadoJudicialne.consultarPendenciaFinalizadaId(MandadoJudicialdt.getId_Pendencia(), UsuarioSessao);
				  	   request.getSession().setAttribute("Pendenciadt", pendenciaDt);
					   
					} else {
					    request.setAttribute("MensagemErro", "Mandado não encontrado");
					}
				}
							
			break;
			
			// Devolver Mandado
			case Configuracao.Curinga8:
				MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
				List listaMandadoJudicial = null;
				
				String idMandTipoRedist = "";
				if (request.getParameter("idMandTipoRedist") != null && !request.getParameter("idMandTipoRedist").equalsIgnoreCase("null")) {
					idMandTipoRedist = request.getParameter("idMandTipoRedist");
					request.getSession().setAttribute("idMandTipoRedist", idMandTipoRedist);						
				} else {
					request.getSession().setAttribute("idMandTipoRedist", "");
				}			
				
				if(request.getParameter("fluxo") == null) {
					// pegar parametros de consulta
					
					// consultar lista de mandados do oficial
					listaMandadoJudicial = mandadoJudicialNe.consultarMandadosAbertos(null, UsuarioSessao.getId_UsuarioServentia(), UsuarioSessao.getId_Serventia());
					request.setAttribute("listaMandados", listaMandadoJudicial);
					
					// adicionar mandados em uma variável para a jsp usar
				} 
				else if(request.getParameter("fluxo").equalsIgnoreCase("devolver")){
					// Pegar a lista de mandados
					String mandados[] = request.getParameterValues("mandadoSelecionado[]");
					
					 if (idMandTipoRedist.equalsIgnoreCase("")) {
						 throw new MensagemException("Erro. Informe o motivo da devolução.");	
					 }		 
					
					// Verificar se todos os mandados são do oficial
					if( !mandadoJudicialNe.testarPropriedadeMandado(mandados, UsuarioSessao.getId_UsuarioServentia()) ) {
						throw new MensagemException("Erro. Não possui permissão em um dos mandados especificados.");
					}
					
					// Devolver mandados da lista
					mandadoJudicialNe.redistribuir(mandados, UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog(), "Devolvido pelo oficial", idMandTipoRedist);
					
					
					// Adicionar o resultado em uma variável para a jsp usar
					request.setAttribute("MensagemOk", "Mandados devolvidos e redistribuídos automaticamente");
				}
				
				MandadoTipoRedistribuicaoNe objNe = new MandadoTipoRedistribuicaoNe();	
				List listaTipo = objNe.consultaMandadoTipoRedistribuicao();	
				
				
				request.setAttribute("listaTipo", listaTipo);	
				request.setAttribute("idMandTipoRedist", request.getSession().getAttribute("idMandTipoRedist"));				
				request.setAttribute("PaginaAtual", Configuracao.Curinga8);
				stAcao = "/WEB-INF/jsptjgo/DevolverMandadoJudicial.jsp";
			break;
			
			// Alterar data limite do mandado
			case Configuracao.Curinga9:
				request.setAttribute("Fluxo", "inicioAlterarData");
				MandadoJudicialdt.limpar();
				if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equalsIgnoreCase("consultar")) {
					MandadoJudicialdt = MandadoJudicialne.consultarId(request.getParameter("NumeroMandado"));
					if(MandadoJudicialdt == null) {
						request.setAttribute("MensagemErro", "Mandado não encontrado.");
					} else {
						request.setAttribute("Fluxo", "resultadoConsultaAlterarData");
					}
				} 
				request.setAttribute("NumeroMandado", request.getParameter("NumeroMandado"));
				request.setAttribute("ListaMandados", tempList);
				stAcao = "/WEB-INF/jsptjgo/AlterarDataMandadoJudicial.jsp";
				break;
			
			case UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome"};
					String[] lisDescricao = {"Nome", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_UsuarioServentia_2");
					request.setAttribute("tempBuscaDescricao","UsuarioServentia_2");
					request.setAttribute("tempBuscaPrograma","UsuarioServentia");			
					request.setAttribute("tempRetorno","MandadoJudicial");		
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equalsIgnoreCase("redistribuir")) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					} else {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga7);						
					}
				} else {
					String stTemp="";
					stTemp = MandadoJudicialne.consultarOficialJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					response.setContentType("text/x-json");
					try{
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
			break;
			
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}
		request.getSession().setAttribute("MandadoJudicialdt", MandadoJudicialdt);
		request.getSession().setAttribute("MandadoJudicialne", MandadoJudicialne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
    public String autorizaPagamentoCustas(HttpServletRequest request, HttpServletResponse response,  UsuarioNe usuarioSessao, String fluxo) throws Exception {
		
		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
	    MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
	    ProcessoDt processoDt = null;
	    MandadoJudicialStatusNe mandadoJudicialStatusNe = new MandadoJudicialStatusNe();
	    MandadoJudicialStatusDt mandadoJudicialStatusDt = new MandadoJudicialStatusDt();
	    
	    List listaStatusMandado = new ArrayList();
	    List listaStatusPagamento = new ArrayList();
		List <MandadoJudicialDt> listaMandado = new ArrayList();
    
		switch(fluxo)  {
		
		case "verCertidao":
			mandadoJudicialNe.baixaArquivoMandado(request.getParameter("a"), usuarioSessao, response);
		
		case "autorizaOrdemPagamento" :			
		  listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatus();	
		  request.setAttribute("listaStatusPagamento", listaStatusPagamento);			
		  request.setAttribute("tempRetorno", "MandadoJudicial");
		  return "/WEB-INF/jsptjgo/AutorizaOrdemPagamento.jsp";
	      
		case "buscaOrdemPg" :
			if (request.getParameter("dataReferencia") != null && !request.getParameter("dataReferencia").equals("")) {
				request.setAttribute("dataReferencia", request.getParameter("dataReferencia"));					
				int dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("dataReferencia"),
						Funcoes.FormatarData(new Date()));
				if (dias < 0)
					request.setAttribute("MensagemErro", "Data informada maior que a data atual.");		 
		    	else {
				   listaMandado = mandadoJudicialNe
					   	.consultaMandadosPorDataRetornoCustas(usuarioSessao.getId_Serventia(), request.getParameter("dataReferencia"), request.getParameter("idMandJudPagamentoStatus"));
				   if (listaMandado.size() > 0) 
					   request.setAttribute("listaMandado", listaMandado);				
				   else request.setAttribute("MensagemErro", "Nenhum Mandado com Data e Status Informado.");				          
				   }
			} else {
				request.setAttribute("MensagemErro", "O campo data de referência e obrigatório");
			}
			listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatus();
							
		    request.setAttribute("listaStatusPagamento", listaStatusPagamento);

		    request.setAttribute("idMandJudPagamentoStatus", request.getParameter("idMandJudPagamentoStatus"));
			request.setAttribute("tempRetorno", "MandadoJudicial");			
		    return "/WEB-INF/jsptjgo/AutorizaOrdemPagamento.jsp";		
			
		case "editaOrdemPg" :			 
			request.setAttribute("tempRetorno", "MandadoJudicial");	
			if (request.getParameter("idMandJud") != null && !request.getParameter("idMandJud").equalsIgnoreCase("")) {
				mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
				
				 //ATENÇÃO: Este trecho é necessário por causa da forma como o GuiaLocomocaoCt funciona. Na tela de aprovação de pagamento
			       // estamos criando um link para a tela de emissão de guia, para suprir casos de processos sigilosos em que o coordenador
			       // da central não tem acesso às opções do processo, e a forma como o GuiaLocomocaoCt identifica o processo a ser usado
			       // é através de uma variável de seção. Portanto, ao abrir a tela de aprovação de pagamento de um mandado, precisamos
			       // colocar seu respectivo processo na seção para que o link funcione.
			       processoDt = mandadoJudicialNe.retornarProcessoDtPorIdPend(mandadoJudicialDt.getId_Pendencia());
			       request.getSession().removeAttribute("processoDt");
			       request.getSession().setAttribute("processoDt", processoDt);
			       request.setAttribute("serverName", request.getServerName());
			       //---------------------
				
			    if (mandadoJudicialDt.getQuantidadeLocomocao().equalsIgnoreCase("") &&
			    		!mandadoJudicialDt.getIdMandJudPagamentoStatus().equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_NEGADO)) {			    	 
			    	listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatus();	
				    request.setAttribute("listaStatusPagamento", listaStatusPagamento);
				    request.setAttribute("idMandJudPagamentoStatus", request.getParameter("idMandJudPagamentoStatus"));
					request.setAttribute("tempRetorno", "MandadoJudicial");	
					request.setAttribute("MensagemErro", "Mandado sem Locomoção.");
				    return "/WEB-INF/jsptjgo/AutorizaOrdemPagamento.jsp";					
			    }			    
			    request.setAttribute("quantidadeLocomocao", mandadoJudicialDt.getQuantidadeLocomocao());
	            request.setAttribute("mandadoJudicial", mandadoJudicialDt);            
               
	            listaStatusMandado = mandadoJudicialStatusNe.consultarListaStatus();
                request.setAttribute("listaStatusMandado", listaStatusMandado);	            
	            
                String[] htmlCertidao = mandadoJudicialNe.retornarConteudoArquivoMandado(mandadoJudicialDt.getId_Pendencia(), usuarioSessao);
	            request.setAttribute("htmlCertidao", htmlCertidao);
	           	
		    	switch (mandadoJudicialDt.getIdMandJudPagamentoStatus()) {
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_PENDENTE):
		    		 request.setAttribute("botaoAutorizar", "ativar");
		    	     request.setAttribute("botaoNegar", "ativar");
		    	     break;
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO):
		    		 request.setAttribute("botaoAutorizar", "desativar");
	    	         request.setAttribute("botaoNegar", "desativar");
	    	          break;
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_ENVIADO):
		    		 request.setAttribute("botaoAutorizar", "desativar");
	                 request.setAttribute("botaoNegar", "desativar");
	                 break;
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_NEGADO):
		     	     request.setAttribute("botaoAutorizar", "desativar");
  	                 request.setAttribute("botaoNegar", "desativar");
  	     			  break;	
			   }
		      	    	
 	       } else {
 	    	  request.setAttribute("MensagemErro", "Id mandado não encontrado.");
			  request.setAttribute("tempRetorno", "MandadoJudicial");
 	       }
		   return "/WEB-INF/jsptjgo/EditaOrdemPagamento.jsp";	  
		
		case "guiaLocomocao" :
			//ATENÇÃO: Este trecho é necessário por causa da forma como o GuiaLocomocaoCt funciona. Na tela de aprovação de pagamento
		    // estamos criando um link para a tela de emissão de guia, para suprir casos de processos sigilosos em que o coordenador
	        // da central não tem acesso às opções do processo, e a forma como o GuiaLocomocaoCt identifica o processo a ser usado
	        // é através de uma variável de seção. Portanto, ao abrir a tela de aprovação de pagamento de um mandado, precisamos
	        // colocar seu respectivo processo na seção para que o link funcione.
			mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
			if(mandadoJudicialDt != null) {
		        processoDt = mandadoJudicialNe.retornarProcessoDtPorIdPend(mandadoJudicialDt.getId_Pendencia());
		        request.getSession().removeAttribute("processoDt");
		        request.getSession().setAttribute("processoDt", processoDt);
		        request.setAttribute("serverName", request.getServerName());
				response.sendRedirect("GuiaLocomocao?PaginaAtual=4");
			}
			return "/WEB-INF/jsptjgo/EditaOrdemPagamento.jsp";
			
		case "guiaLocomocaoComplementar" :
			//ATENÇÃO: Este trecho é necessário por causa da forma como o GuiaLocomocaoCt funciona. Na tela de aprovação de pagamento
		    // estamos criando um link para a tela de emissão de guia, para suprir casos de processos sigilosos em que o coordenador
	        // da central não tem acesso às opções do processo, e a forma como o GuiaLocomocaoCt identifica o processo a ser usado
	        // é através de uma variável de seção. Portanto, ao abrir a tela de aprovação de pagamento de um mandado, precisamos
	        // colocar seu respectivo processo na seção para que o link funcione.
			mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
			if(mandadoJudicialDt != null) {
		        processoDt = mandadoJudicialNe.retornarProcessoDtPorIdPend(mandadoJudicialDt.getId_Pendencia());
		        request.getSession().removeAttribute("processoDt");
		        request.getSession().setAttribute("processoDt", processoDt);
		        request.setAttribute("serverName", request.getServerName());
				response.sendRedirect("GuiaLocomocaoComplementar?PaginaAtual=4");
			}
			return "/WEB-INF/jsptjgo/EditaOrdemPagamento.jsp";
			
		default :
		  request.setAttribute("MensagemErro", "Erro no Fluxo de Autorização");
		  request.setAttribute("tempRetorno", "MandadoJudicial");
		  return "/WEB-INF/jsptjgo/AutorizaOrdemPagamento.jsp";	
		}		 
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
public String autorizaPagamentoGratuito(HttpServletRequest request, HttpServletResponse response,  UsuarioNe usuarioSessao, String fluxo) throws Exception {
		
		MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
	    MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
	    ProcessoDt processoDt = null;
	    MandadoJudicialStatusNe mandadoJudicialStatusNe = new MandadoJudicialStatusNe();
	    MandadoJudicialStatusDt mandadoJudicialStatusDt = new MandadoJudicialStatusDt();
	    
	    List listaStatusMandado = new ArrayList();
	    List listaStatusPagamento = new ArrayList();
		List <MandadoJudicialDt> listaMandado = new ArrayList();
    
		switch(fluxo)  {
		
		case "verCertidao":
			mandadoJudicialNe.baixaArquivoMandado(request.getParameter("a"), usuarioSessao, response);
		
		case "autorizaOrdemPagamentoGratuito" :			
		  listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatusTelaAnaliseGratuitos();	
		  request.setAttribute("listaStatusPagamento", listaStatusPagamento);			
		  request.setAttribute("tempRetorno", "MandadoJudicial");
		  return "/WEB-INF/jsptjgo/AutorizaOrdemPagamentoGratuito.jsp";
	      
		case "buscaOrdemPgGratuito" :
			if (request.getParameter("dataReferencia") != null && !request.getParameter("dataReferencia").equals("")) {
				request.setAttribute("dataReferencia", request.getParameter("dataReferencia"));					
				int dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("dataReferencia"),
						Funcoes.FormatarData(new Date()));
				if (dias < 0)
					request.setAttribute("MensagemErro", "Data informada maior que a data atual.");		 
		    	else {
		    		
		    		if(MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO.equals(request.getParameter("idMandJudPagamentoStatus"))){
		    			listaMandado = mandadoJudicialNe.consultaMandadosGratuitosAdicionalAnalisado(usuarioSessao.getId_Serventia(), request.getParameter("dataReferencia"));
		    		}
		    		else {
		    			listaMandado = mandadoJudicialNe
		    					.consultaMandadosPorDataRetornoGratuito(usuarioSessao.getId_Serventia(), request.getParameter("dataReferencia"), request.getParameter("idMandJudPagamentoStatus"));
		    		}
				   if (listaMandado.size() > 0) 
					   request.setAttribute("listaMandado", listaMandado);				
				   else request.setAttribute("MensagemErro", "Nenhum Mandado com Data e Status Informado.");				          
				   }
			} else {
				request.setAttribute("MensagemErro", "O campo data de referência e obrigatório");
			}
			listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatusTelaAnaliseGratuitos();
							
		    request.setAttribute("listaStatusPagamento", listaStatusPagamento);

		    request.setAttribute("idMandJudPagamentoStatus", request.getParameter("idMandJudPagamentoStatus"));
			request.setAttribute("tempRetorno", "MandadoJudicial");			
		    return "/WEB-INF/jsptjgo/AutorizaOrdemPagamentoGratuito.jsp";		
			
		case "editaOrdemPgGratuito" :			 
			request.setAttribute("tempRetorno", "MandadoJudicial");	
			if (request.getParameter("idMandJud") != null && !request.getParameter("idMandJud").equalsIgnoreCase("")) {
				mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
				
				 //ATENÇÃO: Este trecho é necessário por causa da forma como o GuiaLocomocaoCt funciona. Na tela de aprovação de pagamento
			       // estamos criando um link para a tela de emissão de guia, para suprir casos de processos sigilosos em que o coordenador
			       // da central não tem acesso às opções do processo, e a forma como o GuiaLocomocaoCt identifica o processo a ser usado
			       // é através de uma variável de seção. Portanto, ao abrir a tela de aprovação de pagamento de um mandado, precisamos
			       // colocar seu respectivo processo na seção para que o link funcione.
			       processoDt = mandadoJudicialNe.retornarProcessoDtPorIdPend(mandadoJudicialDt.getId_Pendencia());
			       request.getSession().removeAttribute("processoDt");
			       request.getSession().setAttribute("processoDt", processoDt);
			       request.setAttribute("serverName", request.getServerName());
			       //---------------------
				
			    if (mandadoJudicialDt.getQuantidadeLocomocao().equalsIgnoreCase("") &&
			    		!mandadoJudicialDt.getIdMandJudPagamentoStatus().equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_NEGADO)) {			    	 
			    	listaStatusPagamento =  mandadoJudicialNe.consultaMandJudPagamentoStatus();	
				    request.setAttribute("listaStatusPagamento", listaStatusPagamento);
				    request.setAttribute("idMandJudPagamentoStatus", request.getParameter("idMandJudPagamentoStatus"));
					request.setAttribute("tempRetorno", "MandadoJudicial");	
					request.setAttribute("MensagemErro", "Mandado sem Locomoção.");
				    return "/WEB-INF/jsptjgo/AutorizaOrdemPagamentoGratuito.jsp";					
			    }			    
//			    request.setAttribute("quantidadeLocomocao", mandadoJudicialDt.getQuantidadeLocomocao());
	            request.setAttribute("mandadoJudicial", mandadoJudicialDt);            
               
	            listaStatusMandado = mandadoJudicialStatusNe.consultarListaStatus();
                request.setAttribute("listaStatusMandado", listaStatusMandado);	            
	            
                String[] htmlCertidao = mandadoJudicialNe.retornarConteudoArquivoMandado(mandadoJudicialDt.getId_Pendencia(), usuarioSessao);
	            request.setAttribute("htmlCertidao", htmlCertidao);
	           	
		    	switch (mandadoJudicialDt.getIdMandJudPagamentoStatus()) {
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_PENDENTE):
		    		 request.setAttribute("botaoAutorizar", "ativar");
		    	     request.setAttribute("botaoNegar", "ativar");
		    	     break;
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO):
		    		 request.setAttribute("botaoAutorizar", "desativar");
	    	         request.setAttribute("botaoNegar", "desativar");
	    	          break;
		    	  case (MandadoJudicialDt.ID_PAGAMENTO_ENVIADO):
		    		 request.setAttribute("botaoAutorizar", "desativar");
	                 request.setAttribute("botaoNegar", "desativar");
	                 break;
		    	  case (MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO):
		     	     request.setAttribute("botaoAutorizar", "desativar");
  	                 request.setAttribute("botaoNegar", "desativar");
  	     			  break;	
			   }
		      	    	
 	       } else {
 	    	  request.setAttribute("MensagemErro", "Id mandado não encontrado.");
			  request.setAttribute("tempRetorno", "MandadoJudicial");
 	       }
		   return "/WEB-INF/jsptjgo/EditaOrdemPagamentoGratuito.jsp";	  
		
		case "guiaLocomocao" :
			//ATENÇÃO: Este trecho é necessário por causa da forma como o GuiaLocomocaoCt funciona. Na tela de aprovação de pagamento
		    // estamos criando um link para a tela de emissão de guia, para suprir casos de processos sigilosos em que o coordenador
	        // da central não tem acesso às opções do processo, e a forma como o GuiaLocomocaoCt identifica o processo a ser usado
	        // é através de uma variável de seção. Portanto, ao abrir a tela de aprovação de pagamento de um mandado, precisamos
	        // colocar seu respectivo processo na seção para que o link funcione.
			mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
			if(mandadoJudicialDt != null) {
		        processoDt = mandadoJudicialNe.retornarProcessoDtPorIdPend(mandadoJudicialDt.getId_Pendencia());
		        request.getSession().removeAttribute("processoDt");
		        request.getSession().setAttribute("processoDt", processoDt);
		        request.setAttribute("serverName", request.getServerName());
				response.sendRedirect("GuiaLocomocao?PaginaAtual=4");
			}
			return "/WEB-INF/jsptjgo/EditaOrdemPagamentoGratuito.jsp";
			
		case "guiaLocomocaoComplementar" :
			//ATENÇÃO: Este trecho é necessário por causa da forma como o GuiaLocomocaoCt funciona. Na tela de aprovação de pagamento
		    // estamos criando um link para a tela de emissão de guia, para suprir casos de processos sigilosos em que o coordenador
	        // da central não tem acesso às opções do processo, e a forma como o GuiaLocomocaoCt identifica o processo a ser usado
	        // é através de uma variável de seção. Portanto, ao abrir a tela de aprovação de pagamento de um mandado, precisamos
	        // colocar seu respectivo processo na seção para que o link funcione.
			mandadoJudicialDt = mandadoJudicialNe.consultarId(Integer.parseInt(request.getParameter("idMandJud")));
			if(mandadoJudicialDt != null) {
		        processoDt = mandadoJudicialNe.retornarProcessoDtPorIdPend(mandadoJudicialDt.getId_Pendencia());
		        request.getSession().removeAttribute("processoDt");
		        request.getSession().setAttribute("processoDt", processoDt);
		        request.setAttribute("serverName", request.getServerName());
				response.sendRedirect("GuiaLocomocaoComplementar?PaginaAtual=4");
			}
			return "/WEB-INF/jsptjgo/EditaOrdemPagamentoGratuito.jsp";
			
		default :
		  request.setAttribute("MensagemErro", "Erro no Fluxo de Autorização");
		  request.setAttribute("tempRetorno", "MandadoJudicial");
		  return "/WEB-INF/jsptjgo/AutorizaOrdemPagamentoGratuito.jsp";	
		}		 
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public boolean isFluxoAutorizacaoPagamentoCustas(String fluxo) throws Exception {
    	
    	 if (fluxo != null && (fluxo.equalsIgnoreCase("autorizaOrdemPagamento") ||
    			 fluxo.equalsIgnoreCase("buscaOrdemPg") ||
    			 fluxo.equalsIgnoreCase("editaOrdemPg") || 
    			 fluxo.equalsIgnoreCase("guiaLocomocao") ||
    			 fluxo.equalsIgnoreCase("guiaLocomocaoComplementar") ||
    			 fluxo.equalsIgnoreCase("verCertidao"))) {
    		 return true;
    	 }
    	
    	return false;
    }
    
    public boolean isFluxoAutorizacaoPagamentoGratuito(String fluxo) throws Exception {
    	
   	 if (fluxo != null && (fluxo.equalsIgnoreCase("autorizaOrdemPagamentoGratuito") ||
   			 fluxo.equalsIgnoreCase("buscaOrdemPgGratuito") ||
   			 fluxo.equalsIgnoreCase("editaOrdemPgGratuito") || 
   			 fluxo.equalsIgnoreCase("guiaLocomocaoGratuito") ||
   			 fluxo.equalsIgnoreCase("guiaLocomocaoComplementarGratuito") ||
   			 fluxo.equalsIgnoreCase("verCertidaoGratuito"))) {
   		 return true;
   	 }
   	
   	return false;
   }
}
 
