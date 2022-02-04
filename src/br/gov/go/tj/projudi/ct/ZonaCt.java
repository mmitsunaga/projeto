package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.RelatorioZonaHistoricoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ZonaNe;
import br.gov.go.tj.utils.Configuracao;

public class ZonaCt extends ZonaCtGen{

    private static final long serialVersionUID = -1229684991264552804L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ZonaDt zonaDt;
		ZonaNe zonaNe;
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
	
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		//-fim controle de buscas ajax
		
		
		String stAcao="/WEB-INF/jsptjgo/Zona.jsp";
	
		request.setAttribute("tempPrograma","Zona");
	
		zonaNe =(ZonaNe)request.getSession().getAttribute("Zonane");
		if (zonaNe == null )  zonaNe = new ZonaNe();  
	
		zonaDt =(ZonaDt)request.getSession().getAttribute("Zonadt");
		if (zonaDt == null )  zonaDt = new ZonaDt();  
	
		zonaDt.setZona( request.getParameter("Zona")); 
		zonaDt.setZonaCodigo( request.getParameter("ZonaCodigo"));
		zonaDt.setId_Comarca( request.getParameter("Id_Comarca")); 
		zonaDt.setComarca( request.getParameter("Comarca"));
		zonaDt.setValorCivel(request.getParameter("ValorCivel")); 
		zonaDt.setValorCriminal(request.getParameter("ValorCriminal"));
		zonaDt.setValorContaVinculada(request.getParameter("ValorContaVinculada"));
		zonaDt.setValorSegundoGrau(request.getParameter("ValorSegundoGrau"));
		zonaDt.setValorSegundoGrauContadoria(request.getParameter("ValorSegundoGrauContadoria"));
		 
		zonaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		zonaDt.setIpComputadorLog(request.getRemoteAddr());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		request.getSession().removeAttribute("RelatorioZonaHistoricodt");
	
		switch (paginaatual) {
			case Configuracao.Novo: 
				zonaDt.limpar();
				break;
				
			case Configuracao.SalvarResultado: 
				String mensagem = zonaNe.Verificar(zonaDt); 
				if (mensagem.length()==0){
					zonaDt.setId_UsuarioServentia(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					zonaNe.salvar(zonaDt); 
					request.setAttribute("MensagemOk", "Dados salvos com sucesso."); 
				} else	{
					request.setAttribute("MensagemErro", mensagem );
				}
			break;
			
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Zona","Comarca"};
					String[] lisDescricao = {"Zona","Comarca"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Zona");
					request.setAttribute("tempBuscaDescricao", "Zona");
					request.setAttribute("tempBuscaPrograma", "Zona");
					request.setAttribute("tempRetorno", "Zona");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = zonaNe.consultarDescricaoJSON(stNomeBusca1,stNomeBusca2,PosicaoPaginaAtual);											
					enviarJSON(response, stTemp);										
					return;
				}
				break;
				
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","Zona");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = zonaNe.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);											
					enviarJSON(response, stTemp);					
					
					return;								
				}
				break;
				
			case Configuracao.ExcluirResultado: //Excluir
				if (zonaDt != null && zonaDt.getId() != null && zonaDt.getId().trim().length() > 0) {
					zonaNe.excluir(zonaDt);						
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				} else {
					request.setAttribute("MensagemErro", "Para excluir uma zona é necessário consultá-la antes" );
				}
			break;
			
			case Configuracao.Curinga6: //Visualizar Histórico
				if (zonaDt != null && zonaDt.getId() != null && zonaDt.getId().trim().length() > 0) {
					RelatorioZonaHistoricoDt relatorio = zonaNe.consultarHistoricos(zonaDt.getId());
					request.getSession().setAttribute("RelatorioZonaHistoricodt", relatorio );
				} else {
					request.setAttribute("MensagemErro", "Para listar o histórico de alteração de uma zona é necessário consultá-la antes" );
				}								
			break;
				
			default:
				String stId = request.getParameter("Id_Zona");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( zonaDt.getId()))){
						zonaDt.limpar();
						zonaDt = zonaNe.consultarId(stId);
					}
				break;		
	
		}
	
		request.getSession().setAttribute("Zonadt",zonaDt );
		request.getSession().setAttribute("Zonane",zonaNe );
	
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
