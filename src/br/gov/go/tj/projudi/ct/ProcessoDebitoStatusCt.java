package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.ne.ProcessoDebitoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoDebitoStatusCt extends ProcessoDebitoStatusCtGen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2453489648484397292L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDebitoStatusDt ProcessoDebitoStatusdt;
		ProcessoDebitoStatusNe ProcessoDebitoStatusne;
		String stNomeBusca1 = "";
		
		String stAcao = "/WEB-INF/jsptjgo/ProcessoDebitoStatus.jsp";

		ProcessoDebitoStatusne = (ProcessoDebitoStatusNe) request.getSession().getAttribute("ProcessoDebitoStatusne");
		if (ProcessoDebitoStatusne == null) ProcessoDebitoStatusne = new ProcessoDebitoStatusNe();

		ProcessoDebitoStatusdt = (ProcessoDebitoStatusDt) request.getSession().getAttribute("ProcessoDebitoStatusdt");
		if (ProcessoDebitoStatusdt == null) ProcessoDebitoStatusdt = new ProcessoDebitoStatusDt();

		ProcessoDebitoStatusdt.setProcessoDebitoStatus(request.getParameter("ProcessoDebitoStatus"));

		ProcessoDebitoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoDebitoStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "ProcessoDebito");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Status do Débito do Processo"};
					String[] lisDescricao = {"Status do Débito do Processo"};;
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoDebitoStatus");
					request.setAttribute("tempBuscaDescricao", "Status do Débito do Processo");
					request.setAttribute("tempBuscaPrograma", "Status do Débito do Processo");
					request.setAttribute("tempRetorno", "ProcessoDebitoStatus");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ProcessoDebitoStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);										
					
					return;
				}
				break;
				
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ProcessoDebitoStatusdt", ProcessoDebitoStatusdt);
		request.getSession().setAttribute("ProcessoDebitoStatusne", ProcessoDebitoStatusne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
