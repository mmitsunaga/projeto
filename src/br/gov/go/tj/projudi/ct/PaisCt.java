package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.projudi.ne.PaisNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PaisCt extends PaisCtGen{

    private static final long serialVersionUID = 603803117847470756L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PaisDt Paisdt;
		PaisNe Paisne;
		
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao="/WEB-INF/jsptjgo/Pais.jsp";

		Paisne =(PaisNe)request.getSession().getAttribute("Paisne");
		if (Paisne == null )  Paisne = new PaisNe();  

		Paisdt =(PaisDt)request.getSession().getAttribute("Paisdt");
		if (Paisdt == null )  Paisdt = new PaisDt();  

		Paisdt.setPais( request.getParameter("Pais")); 
		Paisdt.setPaisCodigo( request.getParameter("PaisCodigo")); 
		Paisdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Paisdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma","Pais");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Pais"};
					String[] lisDescricao = {"Pais"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Pais");
					request.setAttribute("tempBuscaDescricao", "Pais");
					request.setAttribute("tempBuscaPrograma", "Pais");
					request.setAttribute("tempRetorno", "Pais");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Pais");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Paisne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("Paisdt",Paisdt );
		request.getSession().setAttribute("Paisne",Paisne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
