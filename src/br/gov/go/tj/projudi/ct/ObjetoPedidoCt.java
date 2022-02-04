package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.projudi.ne.ObjetoPedidoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ObjetoPedidoCt extends ObjetoPedidoCtGen {

	private static final long serialVersionUID = -8133536144972878553L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ObjetoPedidoDt ObjetoPedidodt;
		ObjetoPedidoNe ObjetoPedidone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
				
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ObjetoPedido.jsp";
		request.setAttribute("tempPrograma","ObjetoPedido");
		request.setAttribute("tempBuscaId_ObjetoPedido","Id_ObjetoPedido");
		request.setAttribute("tempBuscaObjetoPedido","ObjetoPedido");
		request.setAttribute("tempRetorno","ObjetoPedido");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
				
		ObjetoPedidone =(ObjetoPedidoNe)request.getSession().getAttribute("ObjetoPedidone");
		if (ObjetoPedidone == null )  ObjetoPedidone = new ObjetoPedidoNe();  
		ObjetoPedidodt =(ObjetoPedidoDt)request.getSession().getAttribute("ObjetoPedidodt");
		if (ObjetoPedidodt == null )  ObjetoPedidodt = new ObjetoPedidoDt();  

		ObjetoPedidodt.setObjetoPedido( request.getParameter("ObjetoPedido")); 
		ObjetoPedidodt.setObjetoPedidoCodigo( request.getParameter("ObjetoPedidoCodigo")); 
		ObjetoPedidodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ObjetoPedidodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ObjetoPedidone.excluir(ObjetoPedidodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ObjetoPedido"};
					String[] lisDescricao = {"ObjetoPedido"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ObjetoPedido");
					request.setAttribute("tempBuscaDescricao","ObjetoPedido");
					request.setAttribute("tempBuscaPrograma","ObjetoPedido");			
					request.setAttribute("tempRetorno","ObjetoPedido");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ObjetoPedidone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			case Configuracao.Novo: 
				ObjetoPedidodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ObjetoPedidone.Verificar(ObjetoPedidodt); 
				if (Mensagem.length()==0){
					ObjetoPedidone.salvar(ObjetoPedidodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ObjetoPedido");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ObjetoPedidodt.getId()))){
						ObjetoPedidodt.limpar();
						ObjetoPedidodt = ObjetoPedidone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ObjetoPedidodt",ObjetoPedidodt );
		request.getSession().setAttribute("ObjetoPedidone",ObjetoPedidone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
