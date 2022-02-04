package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ne.ServentiaCargoEscalaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCargoEscalaCt extends ServentiaCargoEscalaCtGen{

    private static final long serialVersionUID = 7837319197802016943L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoEscalaDt serventiaCargoEscalaDt;
		ServentiaCargoEscalaNe serventiaCargoEscalaNe;
		
		String stId = "";
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		String stAcao="/WEB-INF/jsptjgo/ServentiaCargoEscala.jsp";
		String Mensagem="";		
		
		serventiaCargoEscalaNe =(ServentiaCargoEscalaNe)request.getSession().getAttribute("ServentiaCargoEscalaNe");
		if (serventiaCargoEscalaNe == null )  serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();  

		serventiaCargoEscalaDt =(ServentiaCargoEscalaDt)request.getSession().getAttribute("ServentiaCargoEscalaDt");
		if (serventiaCargoEscalaDt == null )  serventiaCargoEscalaDt = new ServentiaCargoEscalaDt();
		
		if(request.getParameter("Id_Serventia") != null) {
			request.setAttribute("Id_Serventia", request.getParameter("Id_Serventia"));
			request.setAttribute("Serventia", request.getParameter("Serventia"));
		} else {
			request.setAttribute("Id_Serventia","");
			request.setAttribute("Serventia","");
		}
		
		request.setAttribute("tempBuscaId_ServentiaCargoEscala","tempBuscaId_ServentiaCargoEscala");
		request.setAttribute("tempBuscaServentiaCargoEscala","tempBuscaServentiaCargoEscala");
		request.setAttribute("tempBuscaId_ServentiaCargo","Id_ServentiaCargo");
		request.setAttribute("tempBuscaServentiaCargo","ServentiaCargo");
		request.setAttribute("tempBuscaId_Escala","tempBuscaId_Escala");
		request.setAttribute("tempBuscaEscala","tempBuscaEscala");
		request.setAttribute("tempBuscaServentiaCargoEscalaStatus","tempBuscaServentiaCargoEscalaStatus");
		request.setAttribute("tempBuscaId_ServentiaCargoEscalaStatus","tempBuscaId_ServentiaCargoEscalaStatus");
		request.setAttribute("tempPrograma","ServentiaCargoEscala");
		request.setAttribute("tempRetorno","ServentiaCargoEscala");

		//ServentiaCargoEscala
		serventiaCargoEscalaDt.setServentiaCargoEscala(request.getParameter("ServentiaCargoEscala")); 
		serventiaCargoEscalaDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo")); 
		serventiaCargoEscalaDt.setServentiaCargo(request.getParameter("ServentiaCargo")); 
		serventiaCargoEscalaDt.setId_Escala(request.getParameter("Id_Escala")); 
		serventiaCargoEscalaDt.setEscala(request.getParameter("Escala"));
		
		//ServentiaCargoEscalaStatus
		if( serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt() == null )
			serventiaCargoEscalaDt.setServentiaCargoEscalaStatusDt( new ServentiaCargoEscalaStatusDt() );
		if( serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getId() != null && request.getParameter("Id_ServentiaCargoEscalaStatus") != null && !request.getParameter("Id_ServentiaCargoEscalaStatus").equals(serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getId()) )
			serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().setId(	request.getParameter("Id_ServentiaCargoEscalaStatus"));
		if( serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus() != null && request.getParameter("ServentiaCargoEscalaStatus") != null && !request.getParameter("ServentiaCargoEscalaStatus").equals(serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus()) )
			serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().setServentiaCargoEscalaStatus( request.getParameter("ServentiaCargoEscalaStatus"));
		else if( serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getServentiaCargoEscalaStatus() == null)
			serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().setServentiaCargoEscalaStatus("");
		if( serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getDataStatus() != null && request.getParameter("DataStatus") != null && !serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().getDataStatus().equals(request.getParameter("DataStatus")) )
			serventiaCargoEscalaDt.getServentiaCargoEscalaStatusDt().setDataStatus( request.getParameter("DataStatus") );
		
		serventiaCargoEscalaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		serventiaCargoEscalaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual) {
			case Configuracao.Imprimir: {
				break;
			}
			
			case Configuracao.Localizar: {//localizar
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Cargo"};
					String[] lisDescricao = {"Cargo","Nome", "Escala","Status"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargoEscala");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargoEscala");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargoEscala");
					request.setAttribute("tempRetorno", "ServentiaCargoEscala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = serventiaCargoEscalaNe.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
					response.setContentType("text/x-json");
					try{
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			case Configuracao.Novo: {
				serventiaCargoEscalaDt.setServentiaCargoEscala(""); 
				serventiaCargoEscalaDt.setId_ServentiaCargo(""); 
				serventiaCargoEscalaDt.setServentiaCargo(""); 
				serventiaCargoEscalaDt.setId_Escala(""); 
				serventiaCargoEscalaDt.setEscala("");
				serventiaCargoEscalaDt.setDataVinculacao("");
				serventiaCargoEscalaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				serventiaCargoEscalaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				ServentiaCargoEscalaStatusDt usuServEscStatusDt = new ServentiaCargoEscalaStatusDt();
				usuServEscStatusDt.setId("");
				usuServEscStatusDt.setServentiaCargoEscalaStatus("");
				usuServEscStatusDt.setDataStatus("");
				
				serventiaCargoEscalaDt.setServentiaCargoEscalaStatusDt(usuServEscStatusDt);
				
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			}
			case Configuracao.Salvar: {
				Mensagem = serventiaCargoEscalaNe.Verificar(serventiaCargoEscalaDt);
				
				request.setAttribute("MensagemErro", Mensagem);
				
				break;
			}
			
			case Configuracao.SalvarResultado: {
				Mensagem = serventiaCargoEscalaNe.Verificar(serventiaCargoEscalaDt);
				
				if( Mensagem.equals("") ) {
					
					serventiaCargoEscalaNe.salvar(serventiaCargoEscalaDt);
					
					serventiaCargoEscalaDt = serventiaCargoEscalaNe.consultarId(serventiaCargoEscalaDt.getId());
					
					/** *****************************************************
					 * IMPORTANTE:
					 * Salva Historico Status ********* */
					serventiaCargoEscalaNe.inserirServentiaCargoEscalaHistorico(serventiaCargoEscalaDt);
					/** ******************** */
					
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso!");
				}
				else 
					request.setAttribute("MensagemErro", Mensagem);
				
				break;
			}
			
			
			case Configuracao.Curinga6: {//Localizar Oficial Ad
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Cargo"};
					String[] lisDescricao = {"Cargo","Nome", "Escala","Status"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargoEscala");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargoEscala");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargoEscala");
					request.setAttribute("tempRetorno", "ServentiaCargoEscala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = serventiaCargoEscalaNe.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
					response.setContentType("text/x-json");
					try{
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			
			
			case (ServentiaCargoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome"};
					String[] lisDescricao = {"Cargo","Nome"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "ServentiaCargoEscala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					try {
					stTemp = serventiaCargoEscalaNe.consultarCargosServentiaJSON(UsuarioSessao.getUsuarioDt().getId_Serventia(), stNomeBusca1, PosicaoPaginaAtual);
					response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e) { response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			case (EscalaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				if (request.getParameter("Passo") == null) {
					String[] lisDescricao = {"Escala"};
					String[] lisNomeBusca = {"Escala"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Escala");
					request.setAttribute("tempBuscaDescricao", "Escala");
					request.setAttribute("tempBuscaPrograma", "Escala");
					request.setAttribute("tempRetorno", "ServentiaCargoEscala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EscalaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					try {
					stTemp = serventiaCargoEscalaNe.consultarEscalaJSON(stNomeBusca1, UsuarioSessao.getId_Serventia(), PosicaoPaginaAtual);
					response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			case (ServentiaCargoEscalaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar) : {
				if (request.getParameter("Passo") == null) {
					String[] lisDescricao = {"Status"};
					String[] lisNomeBusca = {"Status"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargoEscalaStatus");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargoEscalaStatus");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargoEscalaStatus");
					request.setAttribute("tempRetorno", "ServentiaCargoEscala");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoEscalaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					try{
					stTemp = serventiaCargoEscalaNe.consultarServentiaCargoEscalaStatusJSON(stNomeBusca1,PosicaoPaginaAtual);
					response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
					return;
				}
				break;
			}
			
			case Configuracao.ExcluirResultado : {
				if( serventiaCargoEscalaDt.getId().equals("") )
					request.setAttribute("MensagemErro", "Selecione um item para ser excluído!");
				else {
					serventiaCargoEscalaNe.excluir(serventiaCargoEscalaDt);
					request.setAttribute("MensagemOk", "Dados excluídos com sucesso!");
				}
				
				break;
			}
			
			default: {
				stId = request.getParameter("Id_ServentiaCargoEscala");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase(serventiaCargoEscalaDt.getId()))){
						serventiaCargoEscalaDt.limpar();
						serventiaCargoEscalaDt = serventiaCargoEscalaNe.consultarId(stId);
					}
				break;
			}
		}

		request.getSession().setAttribute("ServentiaCargoEscalaDt",serventiaCargoEscalaDt );
		request.getSession().setAttribute("ServentiaCargoEscalaNe",serventiaCargoEscalaNe );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}