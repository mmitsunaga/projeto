package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PrazoSuspensoTipoDt;
import br.gov.go.tj.projudi.ne.PrazoSuspensoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PrazoSuspensoTipoCt extends PrazoSuspensoTipoCtGen {

	private static final long serialVersionUID = -3479004667338377872L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PrazoSuspensoTipoDt PrazoSuspensoTipodt;
		PrazoSuspensoTipoNe PrazoSuspensoTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao = "/WEB-INF/jsptjgo/PrazoSuspensoTipo.jsp";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma", "PrazoSuspensoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		PrazoSuspensoTipone = (PrazoSuspensoTipoNe) request.getSession().getAttribute("PrazoSuspensoTipone");
		if (PrazoSuspensoTipone == null) PrazoSuspensoTipone = new PrazoSuspensoTipoNe();

		PrazoSuspensoTipodt = (PrazoSuspensoTipoDt) request.getSession().getAttribute("PrazoSuspensoTipodt");
		if (PrazoSuspensoTipodt == null) PrazoSuspensoTipodt = new PrazoSuspensoTipoDt();

		PrazoSuspensoTipodt.setPrazoSuspensoTipo(request.getParameter("PrazoSuspensoTipo"));
		PrazoSuspensoTipodt.setPrazoSuspensoTipoCodigo(request.getParameter("PrazoSuspensoTipoCodigo"));
		PrazoSuspensoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PrazoSuspensoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"PrazoSuspensoTipo"};
					String[] lisDescricao = {"PrazoSuspensoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_PrazoSuspensoTipo");
					request.setAttribute("tempBuscaDescricao", "PrazoSuspensoTipo");
					request.setAttribute("tempBuscaPrograma", "PrazoSuspensoTipo");
					request.setAttribute("tempRetorno", "PrazoSuspensoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = PrazoSuspensoTipone.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}
		
		request.getSession().setAttribute("PrazoSuspensoTipodt",PrazoSuspensoTipodt );
		request.getSession().setAttribute("PrazoSuspensoTipone",PrazoSuspensoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
