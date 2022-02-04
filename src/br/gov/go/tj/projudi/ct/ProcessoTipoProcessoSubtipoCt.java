package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoProcessoSubtipoDt;
import br.gov.go.tj.projudi.ne.ProcessoTipoProcessoSubtipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoTipoProcessoSubtipoCt extends ProcessoTipoProcessoSubtipoCtGen{

    private static final long serialVersionUID = -6828678621598673038L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTipoProcessoSubtipoDt ProcessoTipoProcessoSubtipodt;
		ProcessoTipoProcessoSubtipoNe ProcessoTipoProcessoSubtipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/ProcessoTipoProcessoSubtipo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma","Processo Tipo Processo Subtipo");
		request.setAttribute("ListaUlLiProcessoTipoProcessoSubtipo","");

		ProcessoTipoProcessoSubtipone =(ProcessoTipoProcessoSubtipoNe)request.getSession().getAttribute("ProcessoTipoProcessoSubtipone");
		if (ProcessoTipoProcessoSubtipone == null )  ProcessoTipoProcessoSubtipone = new ProcessoTipoProcessoSubtipoNe();  

		ProcessoTipoProcessoSubtipodt =(ProcessoTipoProcessoSubtipoDt)request.getSession().getAttribute("ProcessoTipoProcessoSubtipodt");
		if (ProcessoTipoProcessoSubtipodt == null )  ProcessoTipoProcessoSubtipodt = new ProcessoTipoProcessoSubtipoDt();  

		ProcessoTipoProcessoSubtipodt.setId_ProcessoSubtipo( request.getParameter("Id_ProcessoSubtipo")); 
		ProcessoTipoProcessoSubtipodt.setProcessoSubtipo( request.getParameter("ProcessoSubtipo")); 
		ProcessoTipoProcessoSubtipodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		ProcessoTipoProcessoSubtipodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 

		ProcessoTipoProcessoSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoTipoProcessoSubtipodt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case (ProcessoSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Processo Subtipo"};
					String[] lisDescricao = {"Processo Subtipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoSubtipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoSubtipo");
					request.setAttribute("tempBuscaPrograma", "Processo Subtipo");
					request.setAttribute("tempRetorno", "ProcessoTipoProcessoSubtipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ProcessoSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					String stTemp = "";
					stTemp = ProcessoTipoProcessoSubtipone.consultarDescricaoProcessoSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ProcessoTipoProcessoSubtipodt",ProcessoTipoProcessoSubtipodt );
		request.getSession().setAttribute("ProcessoTipoProcessoSubtipone",ProcessoTipoProcessoSubtipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
