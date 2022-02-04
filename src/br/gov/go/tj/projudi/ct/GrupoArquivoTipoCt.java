package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.GrupoArquivoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GrupoArquivoTipoCt extends GrupoArquivoTipoCtGen{

    private static final long serialVersionUID = 4890604387786879782L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoArquivoTipoDt GrupoArquivoTipodt;
		GrupoArquivoTipoNe GrupoArquivoTipone;
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/GrupoArquivoTipo.jsp";

		request.setAttribute("tempPrograma","Grupo Arquivo Tipo");
		request.setAttribute("tempBuscaId_GrupoArquivoTipo","Id_GrupoArquivoTipo");
		request.setAttribute("tempBuscaGrupoArquivoTipo","GrupoArquivoTipo");
		request.setAttribute("ListaUlLiGrupoArquivoTipo","");
		request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
		request.setAttribute("tempBuscaGrupo","Grupo");
		request.setAttribute("tempBuscaId_ArquivoTipo","Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo","ArquivoTipo");

		request.setAttribute("tempRetorno","GrupoArquivoTipo");
		
		GrupoArquivoTipone =(GrupoArquivoTipoNe)request.getSession().getAttribute("GrupoArquivoTipone");
		if (GrupoArquivoTipone == null )  GrupoArquivoTipone = new GrupoArquivoTipoNe();  

		GrupoArquivoTipodt =(GrupoArquivoTipoDt)request.getSession().getAttribute("GrupoArquivoTipodt");
		if (GrupoArquivoTipodt == null )  GrupoArquivoTipodt = new GrupoArquivoTipoDt();  

		GrupoArquivoTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		GrupoArquivoTipodt.setGrupo( request.getParameter("Grupo")); 
		GrupoArquivoTipodt.setId_ArquivoTipo( request.getParameter("Id_ArquivoTipo")); 
		GrupoArquivoTipodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 
		GrupoArquivoTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		GrupoArquivoTipodt.setArquivoTipoCodigo( request.getParameter("ArquivoTipoCodigo")); 

		GrupoArquivoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoArquivoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo", "Código", "Tipo de Serventia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "GrupoArquivoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = GrupoArquivoTipone.consultarDescricaoGrupoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("GrupoArquivoTipodt",GrupoArquivoTipodt );
		request.getSession().setAttribute("GrupoArquivoTipone",GrupoArquivoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
    
}
