package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MovimentacaoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 296781284311956954L;

    public  MovimentacaoTipoCtGen() {

	} 
		public int Permissao(){
			return MovimentacaoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoTipoDt MovimentacaoTipodt;
		MovimentacaoTipoNe MovimentacaoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MovimentacaoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MovimentacaoTipo");
		request.setAttribute("tempBuscaId_MovimentacaoTipo","Id_MovimentacaoTipo");
		request.setAttribute("tempBuscaMovimentacaoTipo","MovimentacaoTipo");
		

		request.setAttribute("tempRetorno","MovimentacaoTipo");



		MovimentacaoTipone =(MovimentacaoTipoNe)request.getSession().getAttribute("MovimentacaoTipone");
		if (MovimentacaoTipone == null )  MovimentacaoTipone = new MovimentacaoTipoNe();  


		MovimentacaoTipodt =(MovimentacaoTipoDt)request.getSession().getAttribute("MovimentacaoTipodt");
		if (MovimentacaoTipodt == null )  MovimentacaoTipodt = new MovimentacaoTipoDt();  

		MovimentacaoTipodt.setMovimentacaoTipo( request.getParameter("MovimentacaoTipo")); 
		MovimentacaoTipodt.setMovimentacaoTipoCodigo( request.getParameter("MovimentacaoTipoCodigo")); 

		MovimentacaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				MovimentacaoTipone.excluir(MovimentacaoTipodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				MovimentacaoTipodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=MovimentacaoTipone.Verificar(MovimentacaoTipodt); 
				if (Mensagem.length()==0){
					MovimentacaoTipone.salvar(MovimentacaoTipodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_MovimentacaoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MovimentacaoTipodt.getId()))){
						MovimentacaoTipodt.limpar();
						MovimentacaoTipodt = MovimentacaoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MovimentacaoTipodt",MovimentacaoTipodt );
		request.getSession().setAttribute("MovimentacaoTipone",MovimentacaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
