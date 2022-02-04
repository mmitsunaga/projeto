package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoRelacionadaDt;
import br.gov.go.tj.projudi.ne.PendenciaTipoRelacionadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PendenciaTipoRelacionadaCt extends PendenciaTipoRelacionadaCtGen{

    private static final long serialVersionUID = 4977398688743256730L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaTipoRelacionadaDt PendenciaTipoRelacionadadt;
		PendenciaTipoRelacionadaNe PendenciaTipoRelacionadane;


		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/PendenciaTipoRelacionada.jsp";

		request.setAttribute("tempPrograma","PendenciaTipoRelacionada");
		request.setAttribute("ListaUlLiPendenciaTipoRelacionada","");

		PendenciaTipoRelacionadane =(PendenciaTipoRelacionadaNe)request.getSession().getAttribute("PendenciaTipoRelacionadane");
		if (PendenciaTipoRelacionadane == null )  PendenciaTipoRelacionadane = new PendenciaTipoRelacionadaNe();  

		PendenciaTipoRelacionadadt =(PendenciaTipoRelacionadaDt)request.getSession().getAttribute("PendenciaTipoRelacionadadt");
		if (PendenciaTipoRelacionadadt == null )  PendenciaTipoRelacionadadt = new PendenciaTipoRelacionadaDt();  

		PendenciaTipoRelacionadadt.setId_PendenciaTipoPrincipal( request.getParameter("Id_PendenciaTipoPrincipal")); 
		PendenciaTipoRelacionadadt.setPendenciaTipoPrincipal( request.getParameter("PendenciaTipoPrincipal")); 
		PendenciaTipoRelacionadadt.setId_PendenciaTipoRelacao( request.getParameter("Id_PendenciaTipoRelacao")); 
		PendenciaTipoRelacionadadt.setPendenciaTipoRelacao( request.getParameter("PendenciaTipoRelacao")); 

		PendenciaTipoRelacionadadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PendenciaTipoRelacionadadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case (PendenciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"PendenciaTipo"};
					String[] lisDescricao = {"PendenciaTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_PendenciaTipoPrincipal");
					request.setAttribute("tempBuscaDescricao", "PendenciaTipoPrincipal");
					request.setAttribute("tempBuscaPrograma", "PendenciaTipo");
					request.setAttribute("tempRetorno", "PendenciaTipoRelacionada");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (PendenciaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = PendenciaTipoRelacionadane.consultarDescricaoPendenciaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("PendenciaTipoRelacionadadt",PendenciaTipoRelacionadadt );
		request.getSession().setAttribute("PendenciaTipoRelacionadane",PendenciaTipoRelacionadane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
