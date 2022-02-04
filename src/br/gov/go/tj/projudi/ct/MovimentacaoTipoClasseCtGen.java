package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoClasseNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MovimentacaoTipoClasseCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2206023664701759749L;

	public  MovimentacaoTipoClasseCtGen() {

	} 
		public int Permissao(){
			return MovimentacaoTipoClasseDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoTipoClasseDt MovimentacaoTipoClassedt;
		MovimentacaoTipoClasseNe MovimentacaoTipoClassene;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/MovimentacaoTipoClasse.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MovimentacaoTipoClasse");




		MovimentacaoTipoClassene =(MovimentacaoTipoClasseNe)request.getSession().getAttribute("MovimentacaoTipoClassene");
		if (MovimentacaoTipoClassene == null )  MovimentacaoTipoClassene = new MovimentacaoTipoClasseNe();  


		MovimentacaoTipoClassedt =(MovimentacaoTipoClasseDt)request.getSession().getAttribute("MovimentacaoTipoClassedt");
		if (MovimentacaoTipoClassedt == null )  MovimentacaoTipoClassedt = new MovimentacaoTipoClasseDt();  

		MovimentacaoTipoClassedt.setMovimentacaoTipoClasse( request.getParameter("MovimentacaoTipoClasse")); 
		MovimentacaoTipoClassedt.setMovimentacaoTipoClasseCodigo( request.getParameter("MovimentacaoTipoClasseCodigo")); 

		MovimentacaoTipoClassedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoTipoClassedt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					MovimentacaoTipoClassene.excluir(MovimentacaoTipoClassedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/MovimentacaoTipoClasseLocalizar.jsp";
				request.setAttribute("tempBuscaId_MovimentacaoTipoClasse","Id_MovimentacaoTipoClasse");
				request.setAttribute("tempBuscaMovimentacaoTipoClasse","MovimentacaoTipoClasse");
				request.setAttribute("tempRetorno","MovimentacaoTipoClasse");
				tempList =MovimentacaoTipoClassene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaMovimentacaoTipoClasse", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", MovimentacaoTipoClassene.getQuantidadePaginas());
					MovimentacaoTipoClassedt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				MovimentacaoTipoClassedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=MovimentacaoTipoClassene.Verificar(MovimentacaoTipoClassedt); 
					if (Mensagem.length()==0){
						MovimentacaoTipoClassene.salvar(MovimentacaoTipoClassedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_MovimentacaoTipoClasse");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( MovimentacaoTipoClassedt.getId()))){
						MovimentacaoTipoClassedt.limpar();
						MovimentacaoTipoClassedt = MovimentacaoTipoClassene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("MovimentacaoTipoClassedt",MovimentacaoTipoClassedt );
		request.getSession().setAttribute("MovimentacaoTipoClassene",MovimentacaoTipoClassene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
