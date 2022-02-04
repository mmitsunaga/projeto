package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ServentiaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaTipoCt extends ServentiaTipoCtGen{

    private static final long serialVersionUID = 2658751276056130157L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaTipoDt ServentiaTipodt;
		ServentiaTipoNe ServentiaTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/ServentiaTipo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma","ServentiaTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		request.setAttribute("descCuringa", "Curinga");

		ServentiaTipone =(ServentiaTipoNe)request.getSession().getAttribute("ServentiaTipone");
		if (ServentiaTipone == null )  ServentiaTipone = new ServentiaTipoNe();  

		ServentiaTipodt =(ServentiaTipoDt)request.getSession().getAttribute("ServentiaTipodt");
		if (ServentiaTipodt == null )  ServentiaTipodt = new ServentiaTipoDt();  

		ServentiaTipodt.setServentiaTipo( request.getParameter("ServentiaTipo")); 
		ServentiaTipodt.setServentiaTipoCodigo( request.getParameter("ServentiaTipoCodigo")); 
		if (request.getParameter("Externa") != null) ServentiaTipodt.setExterna( request.getParameter("Externa")); 
		else ServentiaTipodt.setExterna("false");
		ServentiaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ServentiaTipo"};
					String[] lisDescricao = {"ServentiaTipo","Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaTipo");
					request.setAttribute("tempBuscaDescricao", "ServentiaTipo");
					request.setAttribute("tempBuscaPrograma", "ServentiaTipo");
					request.setAttribute("tempRetorno", "ServentiaTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					request.setAttribute("tempRetorno", "ServentiaTipo");
					stTemp = ServentiaTipone.consultarDescricaoJSON(stNomeBusca1);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}

		request.getSession().setAttribute("ServentiaTipodt",ServentiaTipodt );
		request.getSession().setAttribute("ServentiaTipone",ServentiaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
