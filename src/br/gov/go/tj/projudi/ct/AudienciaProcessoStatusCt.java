package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaProcessoStatusCt extends AudienciaProcessoStatusCtGen {

	/**
     * 
     */
	private static final long serialVersionUID = 2533201080410193385L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		AudienciaProcessoStatusDt AudienciaProcessoStatusdt;
		AudienciaProcessoStatusNe AudienciaProcessoStatusne;


		List tempList=null; 
		String Mensagem="";
		String stId="";
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao="/WEB-INF/jsptjgo/AudienciaProcessoStatus.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","AudienciaProcessoStatus");




		AudienciaProcessoStatusne =(AudienciaProcessoStatusNe)request.getSession().getAttribute("AudienciaProcessoStatusne");
		if (AudienciaProcessoStatusne == null )  AudienciaProcessoStatusne = new AudienciaProcessoStatusNe();  


		AudienciaProcessoStatusdt =(AudienciaProcessoStatusDt)request.getSession().getAttribute("AudienciaProcessoStatusdt");
		if (AudienciaProcessoStatusdt == null )  AudienciaProcessoStatusdt = new AudienciaProcessoStatusDt();  

		AudienciaProcessoStatusdt.setAudienciaProcessoStatus( request.getParameter("AudienciaProcessoStatus")); 
		AudienciaProcessoStatusdt.setAudienciaProcessoStatusCodigo( request.getParameter("AudienciaProcessoStatusCodigo")); 
		AudienciaProcessoStatusdt.setId_ServentiaTipo( request.getParameter("Id_ServentiaTipo")); 
		AudienciaProcessoStatusdt.setServentiaTipo( request.getParameter("ServentiaTipo")); 

		AudienciaProcessoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		AudienciaProcessoStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
		case Configuracao.Localizar: //localizar
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Status"};
				String[] lisDescricao = {"AudienciaProcessoStatus"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_AudienciaProcessoStatus");
				request.setAttribute("tempBuscaDescricao", "AudienciaProcessoStatus");
				request.setAttribute("tempBuscaPrograma", "AudienciaProcessoStatus");
				request.setAttribute("tempRetorno", "AudienciaProcessoStatus");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = AudienciaProcessoStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		
				case (ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						//lisNomeBusca = new ArrayList();
						//lisDescricao = new ArrayList();
						String[] lisNomeBusca = {"ServentiaTipo"};
						String[] lisDescricao = {"Tipo de Serventia"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						
						String permissao = String.valueOf(ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
						
						atribuirJSON(request, "Id_ServentiaTipo", "ServentiaTipo", "ServentiaTipo","AudienciaProcessoStatus", Configuracao.Editar , permissao, lisNomeBusca, lisDescricao);
						
					} else {
						String stTemp="";
						stTemp = AudienciaProcessoStatusne.consultarDescricaoServentiaTipoJSON(tempNomeBusca, PosicaoPaginaAtual);
							
						
							enviarJSON(response, stTemp);
							
						
						return;								
					}
					break;
					
					
//--------------------------------------------------------------------------------//
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("AudienciaProcessoStatusdt",AudienciaProcessoStatusdt );
		request.getSession().setAttribute("AudienciaProcessoStatusne",AudienciaProcessoStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
