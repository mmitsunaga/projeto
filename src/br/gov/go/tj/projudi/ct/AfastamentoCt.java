package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.ne.AfastamentoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class AfastamentoCt extends AfastamentoCtGen {

	private static final long serialVersionUID = -6049010905715853334L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AfastamentoDt Afastamentodt;
		AfastamentoNe Afastamentone;
		String stNomeBusca1 = "";

		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/Afastamento.jsp";
		request.setAttribute("tempPrograma", "Afastamento");
		request.setAttribute("tempBuscaId_Afastamento", "Id_Afastamento");
		request.setAttribute("tempBuscaAfastamento", "Afastamento");

		request.setAttribute("tempRetorno", "Afastamento");

		Afastamentone = (AfastamentoNe) request.getSession().getAttribute("Afastamentone");
		if (Afastamentone == null) Afastamentone = new AfastamentoNe();

		Afastamentodt = (AfastamentoDt) request.getSession().getAttribute("Afastamentodt");
		if (Afastamentodt == null) Afastamentodt = new AfastamentoDt();

		Afastamentodt.setAfastamento(request.getParameter("Afastamento"));

		Afastamentodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Afastamentodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Afastamento" };
					String[] lisDescricao = {"Afastamento" };
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Afastamento");
					request.setAttribute("tempBuscaDescricao", "Afastamento");
					request.setAttribute("tempBuscaPrograma", "Afastamento");
					request.setAttribute("tempRetorno", "Afastamento");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = Afastamentone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					response.setContentType("text/x-json");
						enviarJSON(response, stTemp);
						response.flushBuffer();
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("Afastamentodt", Afastamentodt);
		request.getSession().setAttribute("Afastamentone", Afastamentone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
