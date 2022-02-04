package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.ne.AreaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class AreaCt extends AreaCtGen{

    private static final long serialVersionUID = 5769272199993553069L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//-fim controle de buscas ajax
		
		AreaDt areaDt;
		AreaNe areaNe;

		String stAcao="/WEB-INF/jsptjgo/Area.jsp";

		request.setAttribute("tempPrograma","Area");
		areaNe =(AreaNe)request.getSession().getAttribute("Areane");
		if (areaNe == null )  areaNe = new AreaNe();  

		areaDt =(AreaDt)request.getSession().getAttribute("Areadt");
		if (areaDt == null )  areaDt = new AreaDt();  

		areaDt.setArea( request.getParameter("Area")); 
		areaDt.setAreaCodigo( request.getParameter("AreaCodigo")); 

		areaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		areaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Area"};
					String[] lisDescricao = {"Area"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Area");
					request.setAttribute("tempBuscaDescricao","Area");
					request.setAttribute("tempBuscaPrograma","Area");			
					request.setAttribute("tempRetorno","Area");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = areaNe.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);					
					
					return;								
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("Areadt",areaDt );
		request.getSession().setAttribute("Areane",areaNe );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
