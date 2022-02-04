package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaSubtipoCt extends ServentiaSubtipoCtGen {

	private static final long serialVersionUID = -8299624414116263538L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaSubtipoDt ServentiaSubtipodt;
		ServentiaSubtipoNe ServentiaSubtipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao = "/WEB-INF/jsptjgo/ServentiaSubtipo.jsp";				

		request.setAttribute("tempPrograma", "ServentiaSubtipo");
		request.setAttribute("tempBuscaId_ServentiaSubtipo", "Id_ServentiaSubtipo");
		request.setAttribute("tempBuscaServentiaSubtipo", "ServentiaSubtipo");
		request.setAttribute("tempRetorno", "ServentiaSubtipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("descCuringa", "Curinga");

		ServentiaSubtipone = (ServentiaSubtipoNe) request.getSession().getAttribute("ServentiaSubtipone");
		if (ServentiaSubtipone == null)	ServentiaSubtipone = new ServentiaSubtipoNe();

		ServentiaSubtipodt = (ServentiaSubtipoDt) request.getSession().getAttribute("ServentiaSubtipodt");
		if (ServentiaSubtipodt == null)	ServentiaSubtipodt = new ServentiaSubtipoDt();

		ServentiaSubtipodt.setServentiaSubtipo(request.getParameter("ServentiaSubtipo"));
		ServentiaSubtipodt.setServentiaSubtipoCodigo(request.getParameter("ServentiaSubtipoCodigo"));
		ServentiaSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaSubtipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ServentiaSubtipo"};
					String[] lisDescricao = {"ServentiaSubtipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaSubtipo");
					request.setAttribute("tempBuscaDescricao", "ServentiaSubtipo");
					request.setAttribute("tempBuscaPrograma", "ServentiaSubtipo");
					request.setAttribute("tempRetorno", "ServentiaSubtipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaSubtipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ServentiaSubtipodt", ServentiaSubtipodt);
		request.getSession().setAttribute("ServentiaSubtipone", ServentiaSubtipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
