package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.ne.CrimeExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CrimeExecucaoCt extends CrimeExecucaoCtGen{

    private static final long serialVersionUID = 8432859805223963979L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CrimeExecucaoDt CrimeExecucaodt;
		CrimeExecucaoNe CrimeExecucaone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		//-fim controle de buscas ajax
		
		
		String stAcao="/WEB-INF/jsptjgo/CrimeExecucao.jsp";
		request.setAttribute("tempPrograma","CrimeExecucao");

		CrimeExecucaone =(CrimeExecucaoNe)request.getSession().getAttribute("CrimeExecucaone");
		if (CrimeExecucaone == null )  CrimeExecucaone = new CrimeExecucaoNe();  

		CrimeExecucaodt =(CrimeExecucaoDt)request.getSession().getAttribute("CrimeExecucaodt");
		if (CrimeExecucaodt == null )  CrimeExecucaodt = new CrimeExecucaoDt();  

		CrimeExecucaodt.setCrimeExecucaoCodigo( request.getParameter("CrimeExecucaoCodigo")); 
		CrimeExecucaodt.setCrimeExecucao( request.getParameter("CrimeExecucao")); 
		CrimeExecucaodt.setLei( request.getParameter("Lei")); 
		CrimeExecucaodt.setArtigo( request.getParameter("Artigo")); 
		CrimeExecucaodt.setParagrafo( request.getParameter("Paragrafo")); 
		CrimeExecucaodt.setInciso( request.getParameter("Inciso")); 

		CrimeExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CrimeExecucaodt.setIpComputadorLog(request.getRemoteAddr());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1"); //crime
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2"); //lei
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3"); //artigo
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Crime","Lei","Artigo"};
					String[] lisDescricao = {"Crime"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CrimeExecucao");
					request.setAttribute("tempBuscaDescricao","CrimeExecucao");
					request.setAttribute("tempBuscaPrograma","CrimeExecucao");
					request.setAttribute("tempRetorno","CrimeExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = CrimeExecucaone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}
		
		request.getSession().setAttribute("CrimeExecucaodt",CrimeExecucaodt );
		request.getSession().setAttribute("CrimeExecucaone",CrimeExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
