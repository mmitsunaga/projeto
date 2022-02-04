package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.ne.ProcessoSubtipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoSubtipoCt extends ProcessoSubtipoCtGen{

    private static final long serialVersionUID = 4377038591876218115L;
    
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoSubtipoDt ProcessoSubtipodt;
		ProcessoSubtipoNe ProcessoSubtipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/ProcessoSubtipo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma","ProcessoSubtipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		ProcessoSubtipone =(ProcessoSubtipoNe)request.getSession().getAttribute("ProcessoSubtipone");
		if (ProcessoSubtipone == null )  ProcessoSubtipone = new ProcessoSubtipoNe();  

		ProcessoSubtipodt =(ProcessoSubtipoDt)request.getSession().getAttribute("ProcessoSubtipodt");
		if (ProcessoSubtipodt == null )  ProcessoSubtipodt = new ProcessoSubtipoDt();  

		ProcessoSubtipodt.setProcessoSubtipo( request.getParameter("ProcessoSubtipo")); 
		ProcessoSubtipodt.setProcessoSubtipoCodigo( request.getParameter("ProcessoSubtipoCodigo")); 
		ProcessoSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoSubtipodt.setIpComputadorLog(request.getRemoteAddr());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ProcessoSubtipo"};
					String[] lisDescricao = {"ProcessoSubtipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoSubtipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoSubtipo");
					request.setAttribute("tempBuscaPrograma", "ProcessoSubtipo");
					request.setAttribute("tempRetorno", "ProcessoSubtipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ProcessoSubtipone.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}

		request.getSession().setAttribute("ProcessoSubtipodt",ProcessoSubtipodt );
		request.getSession().setAttribute("ProcessoSubtipone",ProcessoSubtipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
