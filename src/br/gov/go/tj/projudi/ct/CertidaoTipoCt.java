package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.projudi.ne.CertidaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CertidaoTipoCt extends CertidaoTipoCtGen{

    private static final long serialVersionUID = -460773899685568628L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertidaoTipoDt CertidaoTipodt;
		CertidaoTipoNe CertidaoTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/CertidaoTipo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma","CertidaoTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		request.setAttribute("descCuringa", "Curinga");

		CertidaoTipone =(CertidaoTipoNe)request.getSession().getAttribute("CertidaoTipone");
		if (CertidaoTipone == null )  CertidaoTipone = new CertidaoTipoNe();  

		CertidaoTipodt =(CertidaoTipoDt)request.getSession().getAttribute("CertidaoTipodt");
		if (CertidaoTipodt == null )  CertidaoTipodt = new CertidaoTipoDt();  

		CertidaoTipodt.setCertidaoTipoCodigo( request.getParameter("CertidaoTipoCodigo")); 
		CertidaoTipodt.setCertidaoTipo( request.getParameter("CertidaoTipo")); 
		CertidaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CertidaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"CertidaoTipo"};
					String[] lisDescricao = {"CertidaoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_CertidaoTipo");
					request.setAttribute("tempBuscaDescricao", "CertidaoTipo");
					request.setAttribute("tempBuscaPrograma", "CertidaoTipo");
					request.setAttribute("tempRetorno", "CertidaoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = CertidaoTipone.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("CertidaoTipodt",CertidaoTipodt );
		request.getSession().setAttribute("CertidaoTipone",CertidaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
