package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.projudi.ne.EventoExecucaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EventoExecucaoTipoCt extends EventoExecucaoTipoCtGen {

	private static final long serialVersionUID = 871244743047736043L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EventoExecucaoTipoDt EventoExecucaoTipodt;
		EventoExecucaoTipoNe EventoExecucaoTipone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao = "/WEB-INF/jsptjgo/EventoExecucaoTipo.jsp";

		request.setAttribute("tempPrograma", "EventoExecucaoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);		
		
		EventoExecucaoTipone = (EventoExecucaoTipoNe) request.getSession().getAttribute("EventoExecucaoTipone");
		if (EventoExecucaoTipone == null) EventoExecucaoTipone = new EventoExecucaoTipoNe();

		EventoExecucaoTipodt = (EventoExecucaoTipoDt) request.getSession().getAttribute("EventoExecucaoTipodt");
		if (EventoExecucaoTipodt == null) EventoExecucaoTipodt = new EventoExecucaoTipoDt();

		EventoExecucaoTipodt.setEventoExecucaoTipo(request.getParameter("EventoExecucaoTipo"));
		EventoExecucaoTipodt.setEventoExecucaoTipoCodigo(request.getParameter("EventoExecucaoTipoCodigo"));
		EventoExecucaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EventoExecucaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"EventoExecucaoTipo"};
					String[] lisDescricao = {"EventoExecucaoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EventoExecucaoTipo");
					request.setAttribute("tempBuscaDescricao", "EventoExecucaoTipo");
					request.setAttribute("tempBuscaPrograma", "EventoExecucaoTipo");
					request.setAttribute("tempRetorno", "EventoExecucaoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = EventoExecucaoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
    					
    				
    				return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("EventoExecucaoTipodt", EventoExecucaoTipodt);
		request.getSession().setAttribute("EventoExecucaoTipone", EventoExecucaoTipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
