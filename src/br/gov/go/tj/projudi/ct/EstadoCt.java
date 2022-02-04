package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.projudi.ne.EstadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EstadoCt extends EstadoCtGen{

    private static final long serialVersionUID = -1565231053247692868L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

	EstadoDt Estadodt;
	EstadoNe Estadone;

	String stNomeBusca1 = "";

	if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

	String stAcao="/WEB-INF/jsptjgo/Estado.jsp";

	request.setAttribute("tempPrograma","Estado");				

	Estadone =(EstadoNe)request.getSession().getAttribute("Estadone");
	if (Estadone == null )  Estadone = new EstadoNe();  

	Estadodt =(EstadoDt)request.getSession().getAttribute("Estadodt");
	if (Estadodt == null )  Estadodt = new EstadoDt();  

	Estadodt.setEstado( request.getParameter("Estado")); 
	Estadodt.setEstadoCodigo( request.getParameter("EstadoCodigo")); 
	Estadodt.setId_Pais( request.getParameter("Id_Pais")); 
	Estadodt.setPais( request.getParameter("Pais")); 
	Estadodt.setUf( request.getParameter("Uf")); 
	Estadodt.setPaisCodigo( request.getParameter("PaisCodigo")); 
	Estadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
	Estadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
	request.setAttribute("PaginaAnterior",paginaatual);
	request.setAttribute("MensagemOk", "");
	request.setAttribute("MensagemErro", "");
	//é a página padrão
	request.setAttribute("PaginaAtual",Configuracao.Editar);

	switch (paginaatual) {
		case Configuracao.Localizar:
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Estado"};
				String[] lisDescricao = {"UF","Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Estado");
				request.setAttribute("tempBuscaDescricao", "Estado");
				request.setAttribute("tempBuscaPrograma", "Estado");
				request.setAttribute("tempRetorno", "Estado");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Estadone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		case (PaisDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Pais"};
				String[] lisDescricao = {"Pais"};				
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Pais");
				request.setAttribute("tempBuscaDescricao", "Pais");
				request.setAttribute("tempBuscaPrograma", "Pais");
				request.setAttribute("tempRetorno", "Estado");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",  String.valueOf(PaisDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);							
			}else{
				String stTemp = "";
				stTemp = Estadone.consultarDescricaoPaisJSON(stNomeBusca1, PosicaoPaginaAtual);
					
				
					enviarJSON(response, stTemp);
					
				
				return;
				
			}
			break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
			}
	}

	request.getSession().setAttribute("Estadodt",Estadodt );
	request.getSession().setAttribute("Estadone",Estadone );

	RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
	dis.include(request, response);
}


}
