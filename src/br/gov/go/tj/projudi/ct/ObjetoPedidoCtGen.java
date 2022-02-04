package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.projudi.ne.ObjetoPedidoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ObjetoPedidoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 8637688812585630705L;

    public  ObjetoPedidoCtGen() {

	} 
		public int Permissao(){
			return ObjetoPedidoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ObjetoPedidoDt ObjetoPedidodt;
		ObjetoPedidoNe ObjetoPedidone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ObjetoPedido.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ObjetoPedido");
		request.setAttribute("tempBuscaId_ObjetoPedido","Id_ObjetoPedido");
		request.setAttribute("tempBuscaObjetoPedido","ObjetoPedido");
		

		request.setAttribute("tempRetorno","ObjetoPedido");



		ObjetoPedidone =(ObjetoPedidoNe)request.getSession().getAttribute("ObjetoPedidone");
		if (ObjetoPedidone == null )  ObjetoPedidone = new ObjetoPedidoNe();  


		ObjetoPedidodt =(ObjetoPedidoDt)request.getSession().getAttribute("ObjetoPedidodt");
		if (ObjetoPedidodt == null )  ObjetoPedidodt = new ObjetoPedidoDt();  

		ObjetoPedidodt.setObjetoPedido( request.getParameter("ObjetoPedido")); 
		ObjetoPedidodt.setObjetoPedidoCodigo( request.getParameter("ObjetoPedidoCodigo")); 

		ObjetoPedidodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ObjetoPedidodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ObjetoPedidone.excluir(ObjetoPedidodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ObjetoPedidoLocalizar.jsp";
				tempList =ObjetoPedidone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaObjetoPedido", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ObjetoPedidone.getQuantidadePaginas());
					ObjetoPedidodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
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
//--------------------------------------------------------------------------------//
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
