package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.ne.RegimeExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

//---------------------------------------------------------
public class RegimeExecucaoCt extends RegimeExecucaoCtGen{

    private static final long serialVersionUID = -5364049899248905868L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RegimeExecucaoDt RegimeExecucaodt;
		RegimeExecucaoNe RegimeExecucaone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/RegimeExecucao.jsp";
		request.setAttribute("tempPrograma","RegimeExecucao");

		RegimeExecucaone =(RegimeExecucaoNe)request.getSession().getAttribute("RegimeExecucaone");
		if (RegimeExecucaone == null )  RegimeExecucaone = new RegimeExecucaoNe();  

		RegimeExecucaodt =(RegimeExecucaoDt)request.getSession().getAttribute("RegimeExecucaodt");
		if (RegimeExecucaodt == null )  RegimeExecucaodt = new RegimeExecucaoDt();  

		RegimeExecucaodt.setRegimeExecucao( request.getParameter("RegimeExecucao")); 
		RegimeExecucaodt.setId_PenaExecucaoTipo( request.getParameter("Id_PenaExecucaoTipo")); 
		RegimeExecucaodt.setPenaExecucaoTipo( request.getParameter("PenaExecucaoTipo"));
		RegimeExecucaodt.setProximoRegimeExecucao( request.getParameter("ProximoRegimeExecucao")); 
		RegimeExecucaodt.setId_ProximoRegimeExecucao( request.getParameter("Id_ProximoRegimeExecucao"));

		RegimeExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RegimeExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
		case Configuracao.Localizar: //localizar
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Regime"};
				String[] lisDescricao = {"Regime","Tipo de Pena"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_RegimeExecucao");
				request.setAttribute("tempBuscaDescricao","RegimeExecucao");
				request.setAttribute("tempBuscaPrograma","RegimeExecucao");
				request.setAttribute("tempRetorno","RegimeExecucao");
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = RegimeExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
			
			case (PenaExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
			if (request.getParameter("Passo")==null){
				//lisNomeBusca = new ArrayList();
				//lisDescricao = new ArrayList();
				String[] lisNomeBusca = {"PenaExecucaoTipo"};
				String[] lisDescricao = {"PenaExecucaoTipo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_PenaExecucaoTipo");
				request.setAttribute("tempBuscaDescricao", "PenaExecucaoTipo");
				request.setAttribute("tempBuscaPrograma", "PenaExecucaoTipo");
				request.setAttribute("tempRetorno", "RegimeExecucao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
				request.setAttribute("PaginaAtual", PenaExecucaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = RegimeExecucaone.consultarDescricaoPenaExecucaoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
			
			case (RegimeExecucaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"RegimeExecucao"};
					String[] lisDescricao = {"RegimeExecucao"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProximoRegimeExecucao");
					request.setAttribute("tempBuscaDescricao", "ProximoRegimeExecucao");
					request.setAttribute("tempBuscaPrograma", "ProximoRegimeExecucao");
					request.setAttribute("tempRetorno", "RegimeExecucao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", RegimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = RegimeExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
				
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("RegimeExecucaodt",RegimeExecucaodt );
		request.getSession().setAttribute("RegimeExecucaone",RegimeExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}


}
