package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.ne.BancoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class BancoCt extends BancoCtGen{

    private static final long serialVersionUID = -6314010980291846082L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		BancoDt Bancodt;
		BancoNe Bancone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/Banco.jsp";

		request.setAttribute("tempPrograma","Banco");
		request.setAttribute("tempBuscaId_Banco","Id_Banco");
		request.setAttribute("tempBuscaBanco","Banco");

		request.setAttribute("tempRetorno","Banco");

		Bancone =(BancoNe)request.getSession().getAttribute("Bancone");
		if (Bancone == null )  Bancone = new BancoNe();  

		Bancodt =(BancoDt)request.getSession().getAttribute("Bancodt");
		if (Bancodt == null )  Bancodt = new BancoDt();  

		Bancodt.setBancoCodigo( request.getParameter("BancoCodigo")); 
		Bancodt.setBanco( request.getParameter("Banco")); 

		Bancodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Bancodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Banco"};
					String[] lisDescricao = {"Codigo", "Banco"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Banco");
					request.setAttribute("tempBuscaDescricao", "Banco");
					request.setAttribute("tempBuscaPrograma", "Banco");
					request.setAttribute("tempRetorno", "Banco");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Bancone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		} 

		request.getSession().setAttribute("Bancodt",Bancodt );
		request.getSession().setAttribute("Bancone",Bancone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
