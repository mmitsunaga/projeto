package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.projudi.ne.EmpresaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EmpresaTipoCt extends EmpresaTipoCtGen{

    private static final long serialVersionUID = -2254146073576759727L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EmpresaTipoDt EmpresaTipodt;
		EmpresaTipoNe EmpresaTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/EmpresaTipo.jsp";

		request.setAttribute("tempPrograma","EmpresaTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		EmpresaTipone =(EmpresaTipoNe)request.getSession().getAttribute("EmpresaTipone");
		if (EmpresaTipone == null )  EmpresaTipone = new EmpresaTipoNe();  

		EmpresaTipodt =(EmpresaTipoDt)request.getSession().getAttribute("EmpresaTipodt");
		if (EmpresaTipodt == null )  EmpresaTipodt = new EmpresaTipoDt();  

		EmpresaTipodt.setEmpresaTipoCodigo( request.getParameter("EmpresaTipoCodigo")); 
		EmpresaTipodt.setEmpresaTipo( request.getParameter("EmpresaTipo")); 
		EmpresaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EmpresaTipodt.setIpComputadorLog(request.getRemoteAddr());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"EmpresaTipo"};
					String[] lisDescricao = {"EmpresaTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EmpresaTipo");
					request.setAttribute("tempBuscaDescricao","EmpresaTipo");
					request.setAttribute("tempBuscaPrograma","EmpresaTipo");			
					request.setAttribute("tempRetorno","EmpresaTipo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = EmpresaTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
				default:
					super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("EmpresaTipodt",EmpresaTipodt );
		request.getSession().setAttribute("EmpresaTipone",EmpresaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
