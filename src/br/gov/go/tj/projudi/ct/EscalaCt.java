package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EscalaTipoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ne.EscalaNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoEscalaNe;
import br.gov.go.tj.projudi.ne.UsuarioAfastamentoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EscalaCt extends EscalaCtGen{

    private static final long serialVersionUID = -7982295374751754991L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
    	
    	EscalaDt escalaDt = new EscalaDt();
		EscalaNe escalaNe = new EscalaNe();
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String mensagem = "";		
		String stId = null;
				
		String stAcao = "/WEB-INF/jsptjgo/Escala.jsp";				
		
		escalaDt =(EscalaDt)request.getSession().getAttribute("escalaDt");
		if (escalaDt == null )  escalaDt = new EscalaDt();
		
		escalaDt.setId_EscalaTipo(request.getParameter("Id_EscalaTipo"));
		escalaDt.setEscalaTipo(request.getParameter("EscalaTipo"));
		escalaDt.setId_MandadoTipo(request.getParameter("Id_MandadoTipo"));
		escalaDt.setMandadoTipo(request.getParameter("MandadoTipo"));
		escalaDt.setId_Regiao(request.getParameter("Id_Regiao"));
		escalaDt.setRegiao(request.getParameter("Regiao"));
		escalaDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
		escalaDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
		if(request.getParameter("Id_Escala") != null && request.getParameter("Id_Escala").length() > 0 )
			escalaDt.setId(request.getParameter("Id_Escala"));
		if(request.getParameter("Escala") != null && request.getParameter("Escala").length() > 0 )
			escalaDt.setEscala(request.getParameter("Escala"));
		if(request.getParameter("QuantidadeMandado") != null)  
			escalaDt.setQuantidadeMandado(request.getParameter("QuantidadeMandado"));
		if(request.getParameter("escalaAtiva") != null && request.getParameter("escalaAtiva").length() > 0 )
			escalaDt.setAtivo(request.getParameter("escalaAtiva"));
		if(request.getParameter("Id_ServentiaCargoEscala") != null) {
			request.getSession().setAttribute("Id_ServentiaCargoEscala", request.getParameter("Id_ServentiaCargoEscala"));
		}
		if(request.getParameter("NomeUsuTemp") != null) {
			request.getSession().setAttribute("NomeUsuTemp", request.getParameter("NomeUsuTemp"));
		}
		if(request.getParameter("isEscalaEspecial") != null) {
			switch( request.getParameter("isEscalaEspecial") ){
				case "1":
					escalaDt.defineTipoEspecialPlantao();
					break;
				case "2":
					escalaDt.defineTipoEspecialAdHoc();
					break;
				default:
					escalaDt.defineTipoEspecialNormal();
					break;
			}
		}
		
		escalaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		escalaDt.setIpComputadorLog(request.getRemoteAddr());
		
		if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").length() > 0 )
			request.setAttribute("Fluxo",request.getParameter("Fluxo"));
		request.setAttribute("Id_ServentiaCargoEscala", request.getParameter("Id_ServentiaCargoEscala"));
		request.setAttribute("tempPrograma","Escala");
		request.setAttribute("tempRetorno","Escala");
		request.setAttribute("tempTituloFormulario","Escala");
		request.setAttribute("tempBuscaMandadoJudicial","Escala");
		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch(paginaatual) {

		case Configuracao.SalvarResultado: {
			mensagem = escalaNe.Verificar(escalaDt);
			if (mensagem.length() == 0) {
				mensagem = escalaNe.salvarEscala(escalaDt);
				if (mensagem.equalsIgnoreCase("")) {
					escalaDt.setListaServentiaCargo(escalaNe.consultarServentiaCargoEscalaPorEscala(escalaDt.getId()));
					request.setAttribute("MensagemOk", "Operação realizada com sucesso.");
				} else {
					request.setAttribute("MensagemErro", mensagem);
				}
			} else
				request.setAttribute("MensagemErro", mensagem);
			break;
		}
			
			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para salvar a Escala.");
				break;
			
			case Configuracao.Localizar : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Escala"};
					String[] lisDescricao = {"Escala"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Escala");
					request.setAttribute("tempBuscaDescricao","Escala");
					request.setAttribute("tempBuscaPrograma","Escala.");			
					request.setAttribute("tempRetorno","Escala");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					try{
					stTemp = escalaNe.consultarDescricaoJSON(stNomeBusca1, UsuarioSessao.getId_Serventia(), PosicaoPaginaAtual);
					response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
				break;
			}
				
			case Configuracao.Novo : {
				//HELLENO -- retirar
				//UsuarioAfastamentoNe  usuAfastNe = new UsuarioAfastamentoNe();
				//usuAfastNe.afastarOficiaisAtrasados();
				//--------------
				request.getSession().removeAttribute("escalaDt");
				escalaDt.limpar();
				request.getSession().setAttribute("serventiaCargoEscalaStatusDt", new ServentiaCargoEscalaStatusDt());
				break;
			}
			
			case Configuracao.ExcluirResultado : {
				if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").length() > 0) {
					if(request.getParameter("Fluxo").equalsIgnoreCase("1")) {
						if(escalaDt.getListaServentiaCargo().size() <= 0) {
							LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), request.getRemoteAddr());
							if(escalaDt.getId() != null && escalaDt.getId().length() > 0) {
								escalaNe.excluirEscala(escalaDt, logDt);
								request.getSession().removeAttribute("escalaDt");
								escalaDt.limpar();
								request.setAttribute("Fluxo", "");
								request.setAttribute("MensagemOk", "Escala excluída com sucesso.");
							} else {
								request.setAttribute("MensagemErro", "Nenhuma escala selecionada para exclusão.");
							} 
						}else {
							request.setAttribute("MensagemErro", "Não é possível excluir escala que contém oficiais");
						}
					} else if(request.getParameter("Fluxo").equalsIgnoreCase("2")) {
						if(request.getParameter("Id_ServentiaCargoEscala") != null && request.getParameter("Id_ServentiaCargoEscala").length() > 0) {
							ServentiaCargoEscalaDt serventiaCargoEscalaDt = new ServentiaCargoEscalaDt();
							serventiaCargoEscalaDt.setId(request.getParameter("Id_ServentiaCargoEscala"));
							serventiaCargoEscalaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							serventiaCargoEscalaDt.setIpComputadorLog(request.getRemoteAddr());
							escalaNe.excluirServentiaCargoEscala(serventiaCargoEscalaDt);
							request.setAttribute("Fluxo", "");
							request.setAttribute("Id_ServentiaCargoEscala", "");
						} else {
							request.setAttribute("MensagemErro", "Nenhum oficial selecionado para exclusão.");
						}
					} else {
						request.setAttribute("MensagemErro", "Nenhuma escala selecionada para exclusão.");
					}
					escalaDt.setListaServentiaCargo(escalaNe.consultarServentiaCargoEscalaPorEscala(escalaDt.getId()));
				}
				break;
			}
			
			case EscalaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"EscalaTipo"};
					String[] lisDescricao = {"EscalaTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EscalaTipo");
					request.setAttribute("tempBuscaDescricao","EscalaTipo");
					request.setAttribute("tempBuscaPrograma","EscalaTipo");			
					request.setAttribute("tempRetorno","Escala");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EscalaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					String stTemp="";
					try{
						stTemp = escalaNe.consultarDescricaoEscalaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
			}
			
			case MandadoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MandadoTipo"};
					String[] lisDescricao = {"MandadoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MandadoTipo");
					request.setAttribute("tempBuscaDescricao","MandadoTipo");
					request.setAttribute("tempBuscaPrograma","MandadoTipo");			
					request.setAttribute("tempRetorno","Escala");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (MandadoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","Escala");	
					try{
						stTemp = escalaNe.consultarDescricaoMandadoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
				break;
			}
			
			case RegiaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Regiao"};
					String[] lisDescricao = {"Regiao", "Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Regiao");
					request.setAttribute("tempBuscaDescricao","Regiao");
					request.setAttribute("tempBuscaPrograma","Regiao");			
					request.setAttribute("tempRetorno","Escala");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (RegiaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = escalaNe.consultarDescricaoPorComarcaJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt().getId_Comarca(), PosicaoPaginaAtual);
					response.setContentType("text/x-json");
					try{
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;								
				}
				break;
			}
			
			//Funcionalidade para adicionar novos usuários serventia escala 
			case ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: {
				if(escalaDt.getId() == null || escalaDt.getId().equalsIgnoreCase("")){
					request.setAttribute("MensagemErro", "Antes de buscar um Usuário é preciso criar uma Escala.");
					break;
				}
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome"};
					String[] lisDescricao = {"Nome","Cargo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "Escala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = escalaNe.consultarCargosServentiaJSON(escalaDt.getId_Serventia(), stNomeBusca1, PosicaoPaginaAtual);
					response.setContentType("text/x-json");
					try{
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			case ServentiaCargoEscalaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: {
				if (request.getParameter("Passo") == null) {
					String[] lisDescricao = {"Status"};
					String[] lisNomeBusca = {"Status"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Status");
					request.setAttribute("tempBuscaDescricao", "Status");
					request.setAttribute("tempBuscaPrograma", "Status");
					request.setAttribute("tempRetorno", "Escala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoEscalaStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
				} else {
					String stTemp = "";
					stTemp = escalaNe.consultarDescricaoServentiaCargoEscalaStatusJSON(stNomeBusca1,PosicaoPaginaAtual);
					response.setContentType("text/x-json");
					try {
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			//Adicionando um novo usuário na lista de oficiais da escala ou Alteração do status de um usuário já cadastrado na escala
			case Configuracao.Curinga6: {
				ServentiaCargoEscalaDt serventiaCargoEscalaDt = new ServentiaCargoEscalaDt();
				ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();
				String idServentiaCargoEscala = "";
				
				
				if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equals("2")){
					//INATIVA OFICIAL NA ESCALA
					
					if(request.getParameter("Id_ServentiaCargoEscala") != null && !request.getParameter("Id_ServentiaCargoEscala").isEmpty() && !request.getParameter("Id_ServentiaCargoEscala").equals("null")) {
						idServentiaCargoEscala = request.getParameter("Id_ServentiaCargoEscala");
					}
					escalaNe.desativaServentiaCargoEscala(idServentiaCargoEscala);
					request.setAttribute("MensagemOk", "Oficial removido da escala.");
					
				} 
				else if(request.getParameter("Fluxo") != null && request.getParameter("Fluxo").equals("3")){
					//ATIVA OFICIAL NA ESCALA
					
					if(request.getParameter("Id_ServentiaCargo") != null && !request.getParameter("Id_ServentiaCargo").isEmpty() && !request.getParameter("Id_ServentiaCargo").equals("null")) {
						idServentiaCargoEscala = serventiaCargoEscalaNe.consultarUsuariosServentiaEscala(request.getParameter("Id_ServentiaCargo"),  request.getParameter("Id_Escala"));
						if(idServentiaCargoEscala != null && !idServentiaCargoEscala.isEmpty()) {
							//CASO JÁ EXISTA NA ESCALA APENAS REATIVA
							escalaNe.ativaServentiaCargoEscala(idServentiaCargoEscala);
							request.setAttribute("MensagemOk", "Oficial reativado na escala.");
						} 
						else {
							//CASO NÃO EXISTA NA ESCALA ADICIONA
							idServentiaCargoEscala = escalaNe.salvarServentiaCargoEscala("",request.getParameter("Id_ServentiaCargo"), request.getParameter("Id_Status"), escalaDt.getId(), UsuarioSessao);
							request.removeAttribute("NovoUsuario");
							request.setAttribute("MensagemOk", "Oficial inserido na escala.");
						}
					}
					
				}

				serventiaCargoEscalaDt = escalaNe.consultarServentiaCargoEscala(idServentiaCargoEscala);

				// salva histórico
				escalaNe.inserirServentiaCargoEscalaHistorico(serventiaCargoEscalaDt);
				
				//Limpando os campos de alteração do status e atualizando a lista de usuários apresentada na tela
				request.setAttribute("Status", "");
				request.setAttribute("Id_Status", "");
				request.setAttribute("NomeUsuTemp", "");
				request.setAttribute("Id_ServentiaCargo", "");
				request.getSession().setAttribute("Id_ServentiaCargoEscala", "");
				escalaDt.setListaServentiaCargo(escalaNe.consultarServentiaCargoEscalaAtivoSuspensoPorEscala(escalaDt.getId()));
				request.getSession().setAttribute("serventiaCargoEscalaStatusDt", new ServentiaCargoEscalaStatusDt());
				break;
			}
			
			default : {
				
				stId = request.getParameter("Id_Escala");
				if( stId != null && stId.length() > 0 && !stId.equalsIgnoreCase("null")) {
					escalaDt = escalaNe.consultarId(stId);
					if (escalaDt.isPlantao()) {
						escalaDt.setEscalaTipo("");
						escalaDt.setMandadoTipo("");
						escalaDt.setRegiao("");					
					}
					escalaDt.setListaServentiaCargo(escalaNe.consultarServentiaCargoEscalaAtivoSuspensoPorEscala(escalaDt.getId()));
					request.getSession().setAttribute("serventiaCargoEscalaStatusDt", new ServentiaCargoEscalaStatusDt());
				}
				
				//Adicionando novo usuário na lista de usuários da escala
				stId = request.getParameter("Id_ServentiaCargo");
				if( stId != null && stId.length() > 0 && !stId.equalsIgnoreCase("null")) {
					request.setAttribute("NomeUsuTemp", request.getParameter("ServentiaCargo"));
					request.setAttribute("Id_ServentiaCargo", request.getParameter("Id_ServentiaCargo"));
					request.setAttribute("Id_Status", escalaNe.consultarIdStatusPorCodigo(String.valueOf(ServentiaCargoEscalaStatusDt.ATIVO)));
					request.setAttribute("Status", "Ativo");
					request.setAttribute("NovoUsuario", "1");
					stId = null;
				}
				
				//Consultando Status do Usuário da Escala
				stId = request.getParameter("Id_Status");
				if( stId != null && stId.length() > 0 && !stId.equalsIgnoreCase("null")) {
					ServentiaCargoEscalaStatusDt usu = new ServentiaCargoEscalaStatusDt();
					usu.setId(request.getParameter("Id_Status"));
					usu.setServentiaCargoEscalaStatus(request.getParameter("Status"));
					request.getSession().setAttribute("serventiaCargoEscalaStatusDt", usu);
					request.setAttribute("NomeUsuTemp", request.getSession().getAttribute("NomeUsuTemp"));
					request.setAttribute("Id_ServentiaCargoEscala", request.getSession().getAttribute("Id_ServentiaCargoEscala"));
					request.setAttribute("NovoUsuario", "1");
					
					request.setAttribute("MensagemOk", "É preciso salvar o novo status para alterar a situação do usuário.");
					stId = null;
				}
				
				
				
				
				
//				//Busca Área
//				stId = request.getParameter("Id_EscalaTipo");
//				if( stId != null ) {
//					escalaDt.setId_EscalaTipo(stId);
//					escalaDt.setEscalaTipo(request.getParameter("EscalaTipo"));
//					request.setAttribute("idEscalaTipo", escalaDt.getId_EscalaTipo());
//					request.setAttribute("EscalaTipo", escalaDt.getEscalaTipo());
//					stId = null;
//				}
//				
//				//Busca Tipo Mandado
//				stId = request.getParameter("Id_MandadoTipo");
//				if( stId != null ) {
//					escalaDt.setId_MandadoTipo(stId);
//					escalaDt.setMandadoTipo(request.getParameter("MandadoTipo"));
//					request.setAttribute("idMandadoTipo", escalaDt.getId_MandadoTipo());
//					request.setAttribute("MandadoTipo",escalaDt.getMandadoTipo());
//					stId = null;
//				}
//				
//				//Busca Serventia
//				stId = request.getParameter("Id_Serventia");
//				if( stId != null ) {
//					escalaDt.setId_Serventia(stId);
//					escalaDt.setServentia(request.getParameter("Serventia"));
//					request.setAttribute("idServentia", escalaDt.getId_Serventia());
//					request.setAttribute("Serventia",escalaDt.getServentia());
//					//sempre que consultar a serventia, atualizar a comarca da mesma
//					escalaDt.setComarca(escalaNe.consultarNomeComarca(escalaDt.getId_Serventia()));
//					request.setAttribute("Comarca", escalaDt.getComarca());
//					stId = null;
//				}
//
//				//Busca Região
//				stId = request.getParameter("Id_Regiao");
//				if( stId != null ) {
//					escalaDt.setId_Regiao(stId);
//					escalaDt.setRegiao(request.getParameter("Regiao"));
//					request.setAttribute("idRegiao", escalaDt.getId_Regiao());
//					request.setAttribute("Regiao",escalaDt.getRegiao());
//					stId = null;
//				}
//				
//				//Consultado Escala
//				stId = request.getParameter("Id_Escala");
//				if( stId != null ) {
//					escalaDt = escalaNe.consultarId(stId);
//					request.setAttribute("idEscalaTipo", escalaDt.getId_EscalaTipo());
//					request.setAttribute("EscalaTipo", escalaDt.getEscalaTipo());
//					request.setAttribute("idMandadoTipo", escalaDt.getId_MandadoTipo());
//					request.setAttribute("MandadoTipo",escalaDt.getMandadoTipo());
//					request.setAttribute("idZona", escalaDt.getId_Zona());
//					request.setAttribute("Zona",escalaDt.getZona());
//					request.setAttribute("idRegiao", escalaDt.getId_Regiao());
//					request.setAttribute("Regiao",escalaDt.getRegiao());
//					request.setAttribute("idBairro", escalaDt.getId_Bairro());
//					request.setAttribute("Bairro",escalaDt.getBairro());
//					request.setAttribute("Escala",escalaDt.getEscala());
//					request.setAttribute("QuantidadeMandado",escalaDt.getQuantidadeMandado());
//					request.setAttribute("escalaAtiva",escalaDt.getAtivo());
//					escalaDt.setListaServentiaCargo(escalaNe.consultarUsuarioServentiaEscalaPorEscala(escalaDt.getId()));
//				}
//				
//				//Adicionando novo usuário na lista de usuários da escala
//				stId = request.getParameter("Id_UsuarioServentia");
//				if( stId != null ) {
//					request.setAttribute("nomeUsuTemp", request.getParameter("UsuarioServentia"));
//					request.setAttribute("idUsuarioServentia", request.getParameter("Id_UsuarioServentia"));
//					request.setAttribute("idStatus", escalaNe.consultarIdStatusPorCodigo(String.valueOf(ServentiaCargoEscalaStatusDt.ATIVO)));
//					request.setAttribute("status", "Ativo");
//					request.setAttribute("novoUsuario", "1");
//					stId = null;
//				}
//				
//				//Consultando Status do Usuário da Escala
//				stId = request.getParameter("Id_UsuServEscStatus");
//				if( stId != null ) {
//					ServentiaCargoEscalaStatusDt usu = new ServentiaCargoEscalaStatusDt();
//					usu.setId(request.getParameter("Id_UsuServEscStatus"));
//					usu.setServentiaCargoEscalaStatus(request.getParameter("UsuEscStatus"));
//					request.getSession().setAttribute("usuarioServentiaEscalaStatusDt", usu);
//					request.setAttribute("nomeUsuTemp", request.getSession().getAttribute("nomeUsuTemp"));
//					request.setAttribute("idUsuarioServentiaEscala", request.getSession().getAttribute("idUsuarioServentiaEscala"));
//					
//					request.setAttribute("MensagemOk", "É preciso salvar o novo status para alterar a sitção do usuário.");
//					stId = null;
//				
//				
				
				
				
				
				break;
			}
		}
		request.getSession().setAttribute("escalaDt", escalaDt);
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
    }
}