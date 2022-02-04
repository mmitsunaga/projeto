package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.GrupoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GrupoCt extends GrupoCtGen{

    private static final long serialVersionUID = 7397120651745929780L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoDt Grupodt;
		GrupoNe Grupone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/Grupo.jsp";

		Grupone =(GrupoNe)request.getSession().getAttribute("Grupone");
		if (Grupone == null )  Grupone = new GrupoNe();  

		Grupodt =(GrupoDt)request.getSession().getAttribute("Grupodt");
		if (Grupodt == null )  Grupodt = new GrupoDt();  

		Grupodt.setGrupo( request.getParameter("Grupo")); 
		Grupodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		Grupodt.setId_ServentiaTipo( request.getParameter("Id_ServentiaTipo")); 
		Grupodt.setServentiaTipo( request.getParameter("ServentiaTipo")); 
		Grupodt.setId_GrupoTipo( request.getParameter("Id_GrupoTipo")); 
		Grupodt.setGrupoTipo( request.getParameter("GrupoTipo")); 
		Grupodt.setServentiaTipoCodigo( request.getParameter("ServentiaTipoCodigo")); 
		Grupodt.setGrupoTipoCodigo( request.getParameter("GrupoTipoCodigo")); 
		Grupodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Grupodt.setIpComputadorLog(request.getRemoteAddr());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma","Grupo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "Grupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "Grupo");
					stTemp = Grupone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case (ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ServentiaTipo"};
					String[] lisDescricao = {"ServentiaTipo","ServentiaTipoCodigo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaTipo");
					request.setAttribute("tempBuscaDescricao", "ServentiaTipo");
					request.setAttribute("tempBuscaPrograma", "ServentiaTipo");
					request.setAttribute("tempRetorno", "Grupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Grupone.consultarDescricaoServentiaTipoJSON(stNomeBusca1);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			case (GrupoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"GrupoTipo"};
					String[] lisDescricao = {"GrupoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GrupoTipo");
					request.setAttribute("tempBuscaDescricao", "GrupoTipo");
					request.setAttribute("tempBuscaPrograma", "GrupoTipo");
					request.setAttribute("tempRetorno", "Grupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (GrupoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Grupone.consultarDescricaoGrupoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
		default:
			super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		}

		request.getSession().setAttribute("Grupodt",Grupodt );
		request.getSession().setAttribute("Grupone",Grupone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
