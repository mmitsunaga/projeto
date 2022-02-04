package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.TemaOrigemDt;
import br.gov.go.tj.projudi.ne.TemaOrigemNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class TemaOrigemCt extends TemaOrigemCtGen{

	private static final long serialVersionUID = -5674163095141714918L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TemaOrigemDt TemaOrigemdt;
		TemaOrigemNe TemaOrigemne;
		String stNomeBusca1="";
		String Mensagem="";
		String stId="";
		String stAcao="/WEB-INF/jsptjgo/TemaOrigem.jsp";

		request.setAttribute("tempPrograma","TemaOrigem");

		TemaOrigemne =(TemaOrigemNe)request.getSession().getAttribute("TemaOrigemne");
		if (TemaOrigemne == null )  TemaOrigemne = new TemaOrigemNe();  

		TemaOrigemdt =(TemaOrigemDt)request.getSession().getAttribute("TemaOrigemdt");
		if (TemaOrigemdt == null )  TemaOrigemdt = new TemaOrigemDt();  

		TemaOrigemdt.setTemaOrigemCodigo( request.getParameter("TemaOrigemCodigo")); 
		TemaOrigemdt.setTemaOrigem( request.getParameter("TemaOrigem")); 
		TemaOrigemdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		TemaOrigemdt.setIpComputadorLog(request.getRemoteAddr());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tema Origem"};
					String[] lisDescricao = {"Tema Origem"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_TemaOrigem");
					request.setAttribute("tempBuscaDescricao","TemaOrigem");
					request.setAttribute("tempBuscaPrograma","TemaOrigem");
					request.setAttribute("tempRetorno","TemaOrigem");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = TemaOrigemne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);					
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("TemaOrigemdt",TemaOrigemdt );
		request.getSession().setAttribute("TemaOrigemne",TemaOrigemne );
		request.getSession().getAttribute("MensagemErro");
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
