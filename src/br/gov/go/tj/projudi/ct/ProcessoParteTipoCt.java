package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoParteTipoCt extends ProcessoParteTipoCtGen {

	private static final long serialVersionUID = 5791871847904035197L;
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteTipoDt ProcessoParteTipodt;
		ProcessoParteTipoNe ProcessoParteTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/ProcessoParteTipo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma","ProcessoParteTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		ProcessoParteTipone =(ProcessoParteTipoNe)request.getSession().getAttribute("ProcessoParteTipone");
		if (ProcessoParteTipone == null )  ProcessoParteTipone = new ProcessoParteTipoNe();  

		ProcessoParteTipodt =(ProcessoParteTipoDt)request.getSession().getAttribute("ProcessoParteTipodt");
		if (ProcessoParteTipodt == null )  ProcessoParteTipodt = new ProcessoParteTipoDt();  

		ProcessoParteTipodt.setProcessoParteTipo( request.getParameter("ProcessoParteTipo")); 
		ProcessoParteTipodt.setProcessoParteTipoCodigo( request.getParameter("ProcessoParteTipoCodigo")); 
		ProcessoParteTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ProcessoParteTipo"};
					String[] lisDescricao = {"ProcessoParteTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoParteTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoParteTipo");
					request.setAttribute("tempBuscaPrograma", "ProcessoParteTipo");
					request.setAttribute("tempRetorno", "ProcessoParteTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ProcessoParteTipone.consultarDescricaoJSON(stNomeBusca1,PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}
		
		request.getSession().setAttribute("ProcessoParteTipodt",ProcessoParteTipodt );
		request.getSession().setAttribute("ProcessoParteTipone",ProcessoParteTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
