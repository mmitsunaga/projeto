package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoClasseNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MovimentacaoTipoClasseCt extends MovimentacaoTipoClasseCtGen{

	private static final long serialVersionUID = -6002518957794933581L;

	public int Permissao() {
		return MovimentacaoTipoClasseDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoTipoClasseDt MovimentacaoTipoClassedt;
		MovimentacaoTipoClasseNe MovimentacaoTipoClassene;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/MovimentacaoTipoClasse.jsp";
				
		request.setAttribute("tempPrograma","MovimentacaoTipoClasse");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		MovimentacaoTipoClassene =(MovimentacaoTipoClasseNe)request.getSession().getAttribute("MovimentacaoTipoClassene");
		if (MovimentacaoTipoClassene == null )  MovimentacaoTipoClassene = new MovimentacaoTipoClasseNe();  

		MovimentacaoTipoClassedt =(MovimentacaoTipoClasseDt)request.getSession().getAttribute("MovimentacaoTipoClassedt");
		if (MovimentacaoTipoClassedt == null )  MovimentacaoTipoClassedt = new MovimentacaoTipoClasseDt();  

		MovimentacaoTipoClassedt.setMovimentacaoTipoClasse( request.getParameter("MovimentacaoTipoClasse")); 
		MovimentacaoTipoClassedt.setMovimentacaoTipoClasseCodigo( request.getParameter("MovimentacaoTipoClasseCodigo")); 
		MovimentacaoTipoClassedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoTipoClassedt.setIpComputadorLog(request.getRemoteAddr());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"MovimentacaoTipoClasse"};
					String[] lisDescricao = {"MovimentacaoTipoClasse"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_MovimentacaoTipoClasse");
					request.setAttribute("tempBuscaDescricao", "MovimentacaoTipoClasse");
					request.setAttribute("tempBuscaPrograma", "MovimentacaoTipoClasse");
					request.setAttribute("tempRetorno", "MovimentacaoTipoClasse");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = MovimentacaoTipoClassene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("MovimentacaoTipoClassedt",MovimentacaoTipoClassedt );
		request.getSession().setAttribute("MovimentacaoTipoClassene",MovimentacaoTipoClassene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}

