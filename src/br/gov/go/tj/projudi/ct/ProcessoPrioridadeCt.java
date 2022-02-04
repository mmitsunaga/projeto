package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.ne.ProcessoPrioridadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoPrioridadeCt extends ProcessoPrioridadeCtGen {

	private static final long serialVersionUID = 7479629030599827121L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoPrioridadeDt ProcessoPrioridadedt;
		ProcessoPrioridadeNe ProcessoPrioridadene;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/ProcessoPrioridade.jsp";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		ProcessoPrioridadene = (ProcessoPrioridadeNe) request.getSession().getAttribute("ProcessoPrioridadene");
		if (ProcessoPrioridadene == null) ProcessoPrioridadene = new ProcessoPrioridadeNe();

		ProcessoPrioridadedt = (ProcessoPrioridadeDt) request.getSession().getAttribute("ProcessoPrioridadedt");
		if (ProcessoPrioridadedt == null) ProcessoPrioridadedt = new ProcessoPrioridadeDt();

		ProcessoPrioridadedt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		ProcessoPrioridadedt.setProcessoPrioridadeCodigo(request.getParameter("ProcessoPrioridadeCodigo"));
		ProcessoPrioridadedt.setProcessoPrioridadeOrdem(request.getParameter("ProcessoPrioridadeOrdem"));
		ProcessoPrioridadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoPrioridadedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "ProcessoPrioridade");
		request.setAttribute("tempBuscaId_ProcessoPrioridade", "Id_ProcessoPrioridade");
		request.setAttribute("tempBuscaProcessoPrioridade", "ProcessoPrioridade");
		request.setAttribute("tempRetorno", "ProcessoPrioridade");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoPrioridade"};
					String[] lisDescricao = {"ProcessoPrioridade", "Código", "Ordem"};;
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaDescricao", "ProcessoPrioridade");
					request.setAttribute("tempBuscaPrograma", "ProcessoPrioridade");
					request.setAttribute("tempRetorno", "ProcessoPrioridade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ProcessoPrioridadene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ProcessoPrioridadedt", ProcessoPrioridadedt);
		request.getSession().setAttribute("ProcessoPrioridadene", ProcessoPrioridadene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
