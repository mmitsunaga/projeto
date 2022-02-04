package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GuiaTipoCt extends GuiaTipoCtGen{

    private static final long serialVersionUID = -5634013031797821400L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaTipoDt GuiaTipodt;
		GuiaTipoNe GuiaTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/GuiaTipo.jsp";
				
		request.setAttribute("tempPrograma","GuiaTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		GuiaTipone =(GuiaTipoNe)request.getSession().getAttribute("GuiaTipone");
		if (GuiaTipone == null ) GuiaTipone = new GuiaTipoNe();  

		GuiaTipodt =(GuiaTipoDt)request.getSession().getAttribute("GuiaTipodt");
		if (GuiaTipodt == null ) GuiaTipodt = new GuiaTipoDt();  

		GuiaTipodt.setGuiaTipo( request.getParameter("GuiaTipo")); 
		GuiaTipodt.setGuiaTipoCodigo( request.getParameter("GuiaTipoCodigo")); 
		GuiaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"GuiaTipo"};
					String[] lisDescricao = {"GuiaTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GuiaTipo");
					request.setAttribute("tempBuscaDescricao", "GuiaTipo");
					request.setAttribute("tempBuscaPrograma", "GuiaTipo");
					request.setAttribute("tempRetorno", "GuiaTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = GuiaTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

	}
		request.getSession().setAttribute("GuiaTipodt",GuiaTipodt );
		request.getSession().setAttribute("GuiaTipone",GuiaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
