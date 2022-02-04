package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.ne.ProcessoDebitoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoDebitoCt extends ProcessoDebitoCtGen {

	private static final long serialVersionUID = -6476077070494616243L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDebitoDt ProcessoDebitodt;
		ProcessoDebitoNe ProcessoDebitone;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoDebito.jsp";

		ProcessoDebitone = (ProcessoDebitoNe) request.getSession().getAttribute("ProcessoDebitone");
		if (ProcessoDebitone == null) ProcessoDebitone = new ProcessoDebitoNe();

		ProcessoDebitodt = (ProcessoDebitoDt) request.getSession().getAttribute("ProcessoDebitodt");
		if (ProcessoDebitodt == null) ProcessoDebitodt = new ProcessoDebitoDt();

		ProcessoDebitodt.setProcessoDebito(request.getParameter("ProcessoDebito"));

		ProcessoDebitodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoDebitodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "ProcessoDebito");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoDebito"};
					String[] lisDescricao = {"ProcessoDebito"};;
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoDebito");
					request.setAttribute("tempBuscaDescricao", "ProcessoDebito");
					request.setAttribute("tempBuscaPrograma", "ProcessoDebito");
					request.setAttribute("tempRetorno", "ProcessoDebito");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ProcessoDebitone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);										
					
					return;
				}
				break;
				
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ProcessoDebitodt", ProcessoDebitodt);
		request.getSession().setAttribute("ProcessoDebitone", ProcessoDebitone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
