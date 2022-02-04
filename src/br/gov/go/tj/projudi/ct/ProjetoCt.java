package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.ProjetoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class ProjetoCt extends ProjetoCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = 1642867802587983852L;

//
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String PosicaoPaginaAtual)throws Exception, ServletException, IOException {
    	String stAcao="/WEB-INF/jsptjgo/Projeto.jsp";
    	ProjetoNe Projetone;
    	String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		Projetone =(ProjetoNe)request.getSession().getAttribute("Projetone");
		if (Projetone == null )  Projetone = new ProjetoNe();
		request.setAttribute("tempPrograma","Projeto");
		switch (paginaatual) {
		case Configuracao.Localizar: //localizar
			
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Projeto"};
				String[] lisDescricao = {"Projeto"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Projeto");
				request.setAttribute("tempBuscaDescricao", "Projeto");
				request.setAttribute("tempBuscaPrograma", "Projeto");			
				request.setAttribute("tempRetorno","Projeto");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				
				stTemp = Projetone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
				enviarJSON(response, stTemp);				
				
				return;			
			}
            break;
		   default: {
		 		super.executar(request, response, UsuarioSessao, paginaatual, nomebusca, PosicaoPaginaAtual);
		 		return;
		 	}
		}
		request.getSession().setAttribute("Projetone",Projetone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
			
	}

}
