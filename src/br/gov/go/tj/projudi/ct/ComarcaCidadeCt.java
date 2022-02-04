package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ne.ComarcaCidadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ComarcaCidadeCt extends ComarcaCidadeCtGen {

	private static final long serialVersionUID = 1080436885457579963L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ComarcaCidadeDt ComarcaCidadedt;
		ComarcaCidadeNe ComarcaCidadene;
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/ComarcaCidade.jsp";			
		
		request.setAttribute("tempPrograma","ComarcaCidade");
		request.setAttribute("ListaUlLiComarcaCidade","");

		ComarcaCidadene =(ComarcaCidadeNe)request.getSession().getAttribute("ComarcaCidadene");
		if (ComarcaCidadene == null )  ComarcaCidadene = new ComarcaCidadeNe();  

		ComarcaCidadedt =(ComarcaCidadeDt)request.getSession().getAttribute("ComarcaCidadedt");
		if (ComarcaCidadedt == null )  ComarcaCidadedt = new ComarcaCidadeDt();  

		ComarcaCidadedt.setId_Comarca( request.getParameter("Id_Comarca")); 
		ComarcaCidadedt.setComarca( request.getParameter("Comarca")); 
		ComarcaCidadedt.setId_Cidade( request.getParameter("Id_Cidade")); 
		ComarcaCidadedt.setCidade( request.getParameter("Cidade")); 

		ComarcaCidadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ComarcaCidadedt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Comarca");
					request.setAttribute("tempBuscaDescricao", "Comarca");
					request.setAttribute("tempBuscaPrograma", "Comarca");
					request.setAttribute("tempRetorno", "ComarcaCidade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ComarcaCidadene.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ComarcaCidadedt",ComarcaCidadedt );
		request.getSession().setAttribute("ComarcaCidadene",ComarcaCidadene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}
