package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.ne.GrupoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GrupoTipoCt extends GrupoTipoCtGen {

	private static final long serialVersionUID = -5627471571515312217L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoTipoDt GrupoTipodt;
		GrupoTipoNe GrupoTipone;
		//List lisNomeBusca = new ArrayList();
		//List lisDescricao = new ArrayList();
		String stNomeBusca1 = "";

		String stAcao = "/WEB-INF/jsptjgo/GrupoTipo.jsp";

		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma", "GrupoTipo");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		GrupoTipone = (GrupoTipoNe) request.getSession().getAttribute("GrupoTipone");
		if (GrupoTipone == null) GrupoTipone = new GrupoTipoNe();

		GrupoTipodt = (GrupoTipoDt) request.getSession().getAttribute("GrupoTipodt");
		if (GrupoTipodt == null) GrupoTipodt = new GrupoTipoDt();

		GrupoTipodt.setGrupoTipo(request.getParameter("GrupoTipo"));
		GrupoTipodt.setGrupoTipoCodigo(request.getParameter("GrupoTipoCodigo"));
		GrupoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoTipodt.setIpComputadorLog(request.getRemoteAddr());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"GrupoTipo"};
					String[] lisDescricao = {"GrupoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GrupoTipo");
					request.setAttribute("tempBuscaDescricao", "GrupoTipo");
					request.setAttribute("tempBuscaPrograma", "GrupoTipo");
					request.setAttribute("tempRetorno", "GrupoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = GrupoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}

		request.getSession().setAttribute("GrupoTipodt", GrupoTipodt);
		request.getSession().setAttribute("GrupoTipone", GrupoTipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
