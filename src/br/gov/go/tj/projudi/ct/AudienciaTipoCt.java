package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class AudienciaTipoCt extends AudienciaTipoCtGen {

	private static final long serialVersionUID = -2189781351818210059L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaTipoDt AudienciaTipodt;
		AudienciaTipoNe AudienciaTipone;
		//List lisNomeBusca = new ArrayList();
		//List lisDescricao = new ArrayList();
		String stNomeBusca = "";

		String stAcao = "/WEB-INF/jsptjgo/AudienciaTipo.jsp";

		request.setAttribute("tempRetorno", "AudienciaTipo");
		request.setAttribute("tempPrograma", "AudienciaTipo");
		request.setAttribute("tempBuscaId_AudienciaTipo", "Id_AudienciaTipo");
		request.setAttribute("tempBuscaAudienciaTipo", "AudienciaTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca = request.getParameter("nomeBusca1");

		AudienciaTipone = (AudienciaTipoNe) request.getSession().getAttribute("AudienciaTipone");
		if (AudienciaTipone == null) AudienciaTipone = new AudienciaTipoNe();

		AudienciaTipodt = (AudienciaTipoDt) request.getSession().getAttribute("AudienciaTipodt");
		if (AudienciaTipodt == null) AudienciaTipodt = new AudienciaTipoDt();

		AudienciaTipodt.setAudienciaTipo(request.getParameter("AudienciaTipo"));
		AudienciaTipodt.setAudienciaTipoCodigo(request.getParameter("AudienciaTipoCodigo"));
		AudienciaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		AudienciaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"AudienciaTipo"};
					String[] lisDescricao = {"AudienciaTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_AudienciaTipo");
					request.setAttribute("tempBuscaDescricao", "AudienciaTipo");
					request.setAttribute("tempBuscaPrograma", "AudienciaTipo");
					request.setAttribute("tempRetorno", "AudienciaTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "AudienciaTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = AudienciaTipone.consultarDescricaoJSON(stNomeBusca, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				default: {
					super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
		}

		request.getSession().setAttribute("AudienciaTipodt", AudienciaTipodt);
		request.getSession().setAttribute("AudienciaTipone", AudienciaTipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
