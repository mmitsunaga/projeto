package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.ne.RgOrgaoExpedidorNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class RgOrgaoExpedidorCt extends RgOrgaoExpedidorCtGen {

	private static final long serialVersionUID = -6481331587017253249L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RgOrgaoExpedidorDt RgOrgaoExpedidordt;
		RgOrgaoExpedidorNe RgOrgaoExpedidorne;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
				
		String stAcao = "/WEB-INF/jsptjgo/RgOrgaoExpedidor.jsp";
		
		request.setAttribute("tempPrograma", "RgOrgaoExpedidor");
		request.setAttribute("tempBuscaId_RgOrgaoExpedidor", "Id_RgOrgaoExpedidor");
		request.setAttribute("tempBuscaRgOrgaoExpedidor", "RgOrgaoExpedidor");
		request.setAttribute("tempBuscaId_Estado", "Id_Estado");
		request.setAttribute("tempBuscaEstado", "Estado");
		request.setAttribute("tempRetorno", "RgOrgaoExpedidor");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		RgOrgaoExpedidorne = (RgOrgaoExpedidorNe) request.getSession().getAttribute("RgOrgaoExpedidorne");
		if (RgOrgaoExpedidorne == null)	RgOrgaoExpedidorne = new RgOrgaoExpedidorNe();

		RgOrgaoExpedidordt = (RgOrgaoExpedidorDt) request.getSession().getAttribute("RgOrgaoExpedidordt");
		if (RgOrgaoExpedidordt == null) RgOrgaoExpedidordt = new RgOrgaoExpedidorDt();

		RgOrgaoExpedidordt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
		RgOrgaoExpedidordt.setSigla(request.getParameter("Sigla"));
		RgOrgaoExpedidordt.setId_Estado(request.getParameter("Id_Estado"));
		RgOrgaoExpedidordt.setEstado(request.getParameter("Estado"));
		RgOrgaoExpedidordt.setEstadoCodigo(request.getParameter("EstadoCodigo"));
		RgOrgaoExpedidordt.setUf(request.getParameter("Uf"));
		RgOrgaoExpedidordt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RgOrgaoExpedidordt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Sigla","Nome"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao","RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma","RgOrgaoExpedidor");			
					request.setAttribute("tempRetorno","RgOrgaoExpedidor");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = RgOrgaoExpedidorne.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Estado"};
					String[] lisDescricao = {"Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Estado");
					request.setAttribute("tempBuscaDescricao","Estado");
					request.setAttribute("tempBuscaPrograma","Estado");			
					request.setAttribute("tempRetorno","RgOrgaoExpedidor");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = RgOrgaoExpedidorne.consultarDescricaoEstadoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("RgOrgaoExpedidordt", RgOrgaoExpedidordt);
		request.getSession().setAttribute("RgOrgaoExpedidorne", RgOrgaoExpedidorne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
