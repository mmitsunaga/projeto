package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoEscalaStatusNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaCargoEscalaStatusCt extends ServentiaCargoEscalaStatusCtGen{

    private static final long serialVersionUID = -4791452844485276000L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoEscalaStatusDt ServentiaCargoEscalaStatusdt;
		ServentiaCargoEscalaStatusNe ServentiaCargoEscalaStatusne;

		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao="/WEB-INF/jsptjgo/ServentiaCargoEscalaStatus.jsp";

		request.setAttribute("tempPrograma","ServentiaCargoEscalaStatus");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		ServentiaCargoEscalaStatusne =(ServentiaCargoEscalaStatusNe)request.getSession().getAttribute("ServentiaCargoEscalaStatusne");
		if (ServentiaCargoEscalaStatusne == null )  ServentiaCargoEscalaStatusne = new ServentiaCargoEscalaStatusNe();  

		ServentiaCargoEscalaStatusdt =(ServentiaCargoEscalaStatusDt)request.getSession().getAttribute("ServentiaCargoEscalaStatusdt");
		if (ServentiaCargoEscalaStatusdt == null )  ServentiaCargoEscalaStatusdt = new ServentiaCargoEscalaStatusDt();  

		if (request.getParameter("Ativo") != null) ServentiaCargoEscalaStatusdt.setAtivo( request.getParameter("Ativo")); 
		else ServentiaCargoEscalaStatusdt.setAtivo("false");
		
		ServentiaCargoEscalaStatusdt.setServentiaCargoEscalaStatus( request.getParameter("ServentiaCargoEscalaStatus")); 
		ServentiaCargoEscalaStatusdt.setServentiaCargoEscalaStatusCodigo( request.getParameter("ServentiaCargoEscalaStatusCodigo")); 
		ServentiaCargoEscalaStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaCargoEscalaStatusdt.setIpComputadorLog(request.getRemoteAddr());

		switch (paginaatual) {
		case Configuracao.Localizar:
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Status"};
				String[] lisDescricao = {"Status"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ServentiaCargoEscalaStatus");
				request.setAttribute("tempBuscaDescricao", "ServentiaCargoEscalaStatus");
				request.setAttribute("tempBuscaPrograma", "ServentiaCargoEscalaStatus");
				request.setAttribute("tempRetorno", "ServentiaCargoEscalaStatus");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = ServentiaCargoEscalaStatusne.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
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

		request.getSession().setAttribute("ServentiaCargoEscalaStatusdt",ServentiaCargoEscalaStatusdt );
		request.getSession().setAttribute("ServentiaCargoEscalaStatusne",ServentiaCargoEscalaStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
