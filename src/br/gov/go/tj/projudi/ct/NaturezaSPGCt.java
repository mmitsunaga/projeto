package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.NaturezaSPGDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.NaturezaSPGNe;
import br.gov.go.tj.utils.Configuracao;

public class NaturezaSPGCt extends Controle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2004312962157734237L;

	@Override
	public int Permissao() {
		return NaturezaSPGDt.CodigoPermissao;
	}
    
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		NaturezaSPGDt naturezaSPGDt;
		NaturezaSPGNe naturezaSPGNe;
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
			
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		//-fim controle de buscas ajax
		
		
		String stAcao="/WEB-INF/jsptjgo/NaturezaSPG.jsp";
	
		request.setAttribute("tempPrograma","NaturezaSPG");
	
		naturezaSPGNe =(NaturezaSPGNe)request.getSession().getAttribute("NaturezaSPGne");
		if (naturezaSPGNe == null )  naturezaSPGNe = new NaturezaSPGNe();  
	
		naturezaSPGDt =(NaturezaSPGDt)request.getSession().getAttribute("NaturezaSPGdt");
		if (naturezaSPGDt == null )  naturezaSPGDt = new NaturezaSPGDt();  
	
		naturezaSPGDt.setNaturezaSPG( request.getParameter("NaturezaSPG")); 
		naturezaSPGDt.setNaturezaSPGCodigo( request.getParameter("NaturezaSPGCodigo"));
		 
		naturezaSPGDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		naturezaSPGDt.setIpComputadorLog(request.getRemoteAddr());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
				
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
			
		switch (paginaatual) {
			case Configuracao.Novo: 
				naturezaSPGDt.limpar();
				break;
				
			case Configuracao.Editar:
				if (naturezaSPGDt != null && naturezaSPGDt.getId() !=null) {
					naturezaSPGDt = naturezaSPGNe.consultarId(naturezaSPGDt.getId());
				}
				break;
				
			case Configuracao.SalvarResultado: 
				String mensagem = naturezaSPGNe.Verificar(naturezaSPGDt); 
				if (mensagem.length()==0){
					naturezaSPGNe.salvar(naturezaSPGDt); 
					request.setAttribute("MensagemOk", "Dados salvos com sucesso."); 
				} else	{
					request.setAttribute("MensagemErro", mensagem );
				}
			break;
			
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Natureza SPG", "Código SPG"};
					String[] lisDescricao = {"Natureza SPG", "Código SPG"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_NaturezaSPG");
					request.setAttribute("tempBuscaDescricao", "Natureza SPG");
					request.setAttribute("tempBuscaPrograma", "NaturezaSPG");
					request.setAttribute("tempRetorno", "NaturezaSPG");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = naturezaSPGNe.consultarDescricaoNaturezaSPGJSON(stNomeBusca1, stNomeBusca2,PosicaoPaginaAtual);
						
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case Configuracao.ExcluirResultado: //Excluir
				if (naturezaSPGDt != null && naturezaSPGDt.getId() != null && naturezaSPGDt.getId().trim().length() > 0) {
					naturezaSPGNe.excluir(naturezaSPGDt);						
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				} else {
					request.setAttribute("MensagemErro", "Para excluir uma natureza é necessário consultá-la antes" );
				}
			break;
			
			default:
				String stId = request.getParameter("Id_NaturezaSPG");
				if (stId!=null && !stId.equalsIgnoreCase("")) {
					if ( paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase( naturezaSPGDt.getId())) {
						naturezaSPGDt.limpar();
						naturezaSPGDt = naturezaSPGNe.consultarId(stId);
					}
				}
				break;	
		}
	
		request.getSession().setAttribute("NaturezaSPGdt",naturezaSPGDt );
		request.getSession().setAttribute("NaturezaSPGne",naturezaSPGNe );
	
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
