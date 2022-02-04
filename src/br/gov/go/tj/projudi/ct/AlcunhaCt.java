package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AlcunhaDt;
import br.gov.go.tj.projudi.ne.AlcunhaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class AlcunhaCt extends AlcunhaCtGen {

	private static final long serialVersionUID = 3996431314125102947L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AlcunhaDt Alcunhadt;
		AlcunhaNe Alcunhane;
		String stNomeBusca1 = "";

		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/Alcunha.jsp";

		request.setAttribute("tempPrograma", "Alcunha");

		Alcunhane = (AlcunhaNe) request.getSession().getAttribute("Alcunhane");
		if (Alcunhane == null) Alcunhane = new AlcunhaNe();

		Alcunhadt = (AlcunhaDt) request.getSession().getAttribute("Alcunhadt");
		if (Alcunhadt == null) Alcunhadt = new AlcunhaDt();

		Alcunhadt.setAlcunha(request.getParameter("Alcunha"));

		Alcunhadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Alcunhadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Alcunha"};
					String[] lisDescricao = {"Alcunha"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Alcunha");
					request.setAttribute("tempBuscaDescricao", "Alcunha");
					request.setAttribute("tempBuscaPrograma", "Alcunha");
					request.setAttribute("tempRetorno", "Alcunha");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = Alcunhane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
																
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("Alcunhadt", Alcunhadt);
		request.getSession().setAttribute("Alcunhane", Alcunhane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
