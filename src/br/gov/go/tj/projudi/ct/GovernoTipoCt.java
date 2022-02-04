package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.ne.GovernoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GovernoTipoCt extends GovernoTipoCtGen{

    private static final long serialVersionUID = -6603617839016236427L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GovernoTipoDt GovernoTipodt;
		GovernoTipoNe GovernoTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/GovernoTipo.jsp";
		
		request.setAttribute("tempPrograma","GovernoTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);		
		
		GovernoTipone =(GovernoTipoNe)request.getSession().getAttribute("GovernoTipone");
		if (GovernoTipone == null )  GovernoTipone = new GovernoTipoNe();  

		GovernoTipodt =(GovernoTipoDt)request.getSession().getAttribute("GovernoTipodt");
		if (GovernoTipodt == null )  GovernoTipodt = new GovernoTipoDt();  

		GovernoTipodt.setGovernoTipoCodigo( request.getParameter("GovernoTipoCodigo")); 
		GovernoTipodt.setGovernoTipo( request.getParameter("GovernoTipo")); 
		GovernoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GovernoTipodt.setIpComputadorLog(request.getRemoteAddr());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"GovernoTipo"};
					String[] lisDescricao = {"GovernoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GovernoTipo");
					request.setAttribute("tempBuscaDescricao", "GovernoTipo");
					request.setAttribute("tempBuscaPrograma", "GovernoTipo");
					request.setAttribute("tempRetorno", "GovernoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = GovernoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
    					
    				
    				return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}

		request.getSession().setAttribute("GovernoTipodt",GovernoTipodt );
		request.getSession().setAttribute("GovernoTipone",GovernoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
