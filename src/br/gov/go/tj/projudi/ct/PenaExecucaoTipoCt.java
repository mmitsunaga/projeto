package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.ne.PenaExecucaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PenaExecucaoTipoCt extends PenaExecucaoTipoCtGen{

    private static final long serialVersionUID = 7555294496444244396L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PenaExecucaoTipoDt PenaExecucaoTipodt;
		PenaExecucaoTipoNe PenaExecucaoTipone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/PenaExecucaoTipo.jsp";
		
		request.setAttribute("tempPrograma","PenaExecucaoTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		PenaExecucaoTipone =(PenaExecucaoTipoNe)request.getSession().getAttribute("PenaExecucaoTipone");
		if (PenaExecucaoTipone == null )  PenaExecucaoTipone = new PenaExecucaoTipoNe();  

		PenaExecucaoTipodt =(PenaExecucaoTipoDt)request.getSession().getAttribute("PenaExecucaoTipodt");
		if (PenaExecucaoTipodt == null )  PenaExecucaoTipodt = new PenaExecucaoTipoDt();  

		PenaExecucaoTipodt.setPenaExecucaoTipo( request.getParameter("PenaExecucaoTipo")); 
		PenaExecucaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PenaExecucaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"PenaExecucaoTipo"};
					String[] lisDescricao = {"PenaExecucaoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_PenaExecucaoTipo");
					request.setAttribute("tempBuscaDescricao", "PenaExecucaoTipo");
					request.setAttribute("tempBuscaPrograma", "PenaExecucaoTipo");
					request.setAttribute("tempRetorno", "PenaExecucaoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = PenaExecucaoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}

		request.getSession().setAttribute("PenaExecucaoTipodt",PenaExecucaoTipodt );
		request.getSession().setAttribute("PenaExecucaoTipone",PenaExecucaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
