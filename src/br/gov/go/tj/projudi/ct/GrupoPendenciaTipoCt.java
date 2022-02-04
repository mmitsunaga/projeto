package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoPendenciaTipoDt;
import br.gov.go.tj.projudi.ne.GrupoPendenciaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GrupoPendenciaTipoCt extends GrupoPendenciaTipoCtGen{

    private static final long serialVersionUID = -5818131409968237217L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoPendenciaTipoDt GrupoPendenciaTipodt;
		GrupoPendenciaTipoNe GrupoPendenciaTipone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/GrupoPendenciaTipo.jsp";

		request.setAttribute("tempPrograma","GrupoPendenciaTipo");
		request.setAttribute("tempBuscaId_GrupoPendenciaTipo","Id_GrupoPendenciaTipo");
		request.setAttribute("tempBuscaGrupoPendenciaTipo","GrupoPendenciaTipo");
		request.setAttribute("ListaUlLiGrupoPendenciaTipo","");
		request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
		request.setAttribute("tempBuscaGrupo","Grupo");
		request.setAttribute("tempBuscaId_PendenciaTipo","Id_PendenciaTipo");
		request.setAttribute("tempBuscaPendenciaTipo","PendenciaTipo");

		request.setAttribute("tempRetorno","GrupoPendenciaTipo");

		GrupoPendenciaTipone =(GrupoPendenciaTipoNe)request.getSession().getAttribute("GrupoPendenciaTipone");
		if (GrupoPendenciaTipone == null )  GrupoPendenciaTipone = new GrupoPendenciaTipoNe();  

		GrupoPendenciaTipodt =(GrupoPendenciaTipoDt)request.getSession().getAttribute("GrupoPendenciaTipodt");
		if (GrupoPendenciaTipodt == null )  GrupoPendenciaTipodt = new GrupoPendenciaTipoDt();  

		GrupoPendenciaTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		GrupoPendenciaTipodt.setGrupo( request.getParameter("Grupo")); 
		GrupoPendenciaTipodt.setId_PendenciaTipo( request.getParameter("Id_PendenciaTipo")); 
		GrupoPendenciaTipodt.setPendenciaTipo( request.getParameter("PendenciaTipo")); 
		GrupoPendenciaTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		GrupoPendenciaTipodt.setPendenciaTipoCodigo( request.getParameter("PendenciaTipoCodigo")); 

		GrupoPendenciaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoPendenciaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual) {
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "GrupoPendenciaTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					String stTemp = "";
					stTemp = GrupoPendenciaTipone.consultarDescricaoGrupoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("GrupoPendenciaTipodt",GrupoPendenciaTipodt );
		request.getSession().setAttribute("GrupoPendenciaTipone",GrupoPendenciaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
