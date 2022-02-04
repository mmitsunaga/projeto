package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt;
import br.gov.go.tj.projudi.ne.ParametroCrimeExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

//---------------------------------------------------------
public class ParametroCrimeExecucaoCt extends ParametroCrimeExecucaoCtGen{

    private static final long serialVersionUID = -2773487536015572752L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
	    ParametroCrimeExecucaoDt ParametroCrimeExecucaodt;
		ParametroCrimeExecucaoNe ParametroCrimeExecucaone;
	
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
		
		String stAcao="/WEB-INF/jsptjgo/ParametroCrimeExecucao.jsp";
		request.setAttribute("tempPrograma","Parâmetro dos Crimes de Execução Penal");
		request.setAttribute("descCuringa", "");
	
		ParametroCrimeExecucaone =(ParametroCrimeExecucaoNe)request.getSession().getAttribute("ParametroCrimeExecucaone");
		if (ParametroCrimeExecucaone == null )  ParametroCrimeExecucaone = new ParametroCrimeExecucaoNe();  
	
		ParametroCrimeExecucaodt =(ParametroCrimeExecucaoDt)request.getSession().getAttribute("ParametroCrimeExecucaodt");
		if (ParametroCrimeExecucaodt == null )  ParametroCrimeExecucaodt = new ParametroCrimeExecucaoDt();  
	
		ParametroCrimeExecucaodt.setData( request.getParameter("Data")); 
		ParametroCrimeExecucaodt.setId_CrimeExecucao( request.getParameter("Id_CrimeExecucao")); 
		ParametroCrimeExecucaodt.setCrimeExecucao( request.getParameter("CrimeExecucao")); 
		ParametroCrimeExecucaodt.setArtigo( request.getParameter("Artigo")); 
		ParametroCrimeExecucaodt.setParagrafo( request.getParameter("Paragrafo")); 
		ParametroCrimeExecucaodt.setLei( request.getParameter("Lei")); 
		ParametroCrimeExecucaodt.setInciso( request.getParameter("Inciso"));
		if (request.getParameterValues("chkParametro") != null){
			ParametroCrimeExecucaodt.setHediondoProgressao("false");
			ParametroCrimeExecucaodt.setHediondoLivramCond("false");
			ParametroCrimeExecucaodt.setEquiparaHediondoLivramCond("false");
			
			String[] chkParametros = request.getParameterValues("chkParametro");
			for (int i=0; i<chkParametros.length; i++){
				if (chkParametros[i].equals("HediondoProgressao")){
					ParametroCrimeExecucaodt.setHediondoProgressao("true");
				} else if (chkParametros[i].equals("HediondoLivramCond")){
					ParametroCrimeExecucaodt.setHediondoLivramCond("true");
				} else if (chkParametros[i].equals("EquiparaHediondoLivramCond")){
					ParametroCrimeExecucaodt.setEquiparaHediondoLivramCond("true");
				}
			}
		}
	
		
		switch (paginaatual) {
			case Configuracao.Localizar: 
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Crime","Lei","Artigo"};
					String[] lisDescricao = {"Crime","Data","Parâmetro"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ParametroCrimeExecucao");
					request.setAttribute("tempBuscaDescricao","Parêmetro dos Crimes");
					request.setAttribute("tempBuscaPrograma","ParametroCrimeExecucao");
					request.setAttribute("tempRetorno","ParametroCrimeExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ParametroCrimeExecucaone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);					
					enviarJSON(response, stTemp);
											
					return;
				}
				break;
				
			case (CrimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Crime","Lei","Artigo"};
					String[] lisDescricao = {"Crime"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CrimeExecucao");
					request.setAttribute("tempBuscaDescricao","CrimeExecucao");
					request.setAttribute("tempBuscaPrograma","CrimeExecucao");
					request.setAttribute("tempRetorno","ParametroCrimeExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", CrimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ParametroCrimeExecucaone.consultarDescricaoCrimeExecucaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);				
					enviarJSON(response, stTemp);											
					
					return;
				}
				break;
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}
	
		request.getSession().setAttribute("ParametroCrimeExecucaodt",ParametroCrimeExecucaodt );
		request.getSession().setAttribute("ParametroCrimeExecucaone",ParametroCrimeExecucaone );
	
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
