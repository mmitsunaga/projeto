package br.gov.go.tj.projudi.ct;

 
 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoMovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoMovimentacaoTipoClasseNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class MovimentacaoTipoMovimentacaoTipoClasseCtGen extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5947534975613100280L;

	public  MovimentacaoTipoMovimentacaoTipoClasseCtGen() {

	} 
		public int Permissao(){
			return MovimentacaoTipoMovimentacaoTipoClasseDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoTipoMovimentacaoTipoClasseDt MovimentacaoTipoMovimentacaoTipoClassedt;
		MovimentacaoTipoMovimentacaoTipoClasseNe MovimentacaoTipoMovimentacaoTipoClassene;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/MovimentacaoTipoMovimentacaoTipoClasse.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","MovimentacaoTipoMovimentacaoTipoClasse");
		request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse","");




		MovimentacaoTipoMovimentacaoTipoClassene =(MovimentacaoTipoMovimentacaoTipoClasseNe)request.getSession().getAttribute("MovimentacaoTipoMovimentacaoTipoClassene");
		if (MovimentacaoTipoMovimentacaoTipoClassene == null )  MovimentacaoTipoMovimentacaoTipoClassene = new MovimentacaoTipoMovimentacaoTipoClasseNe();  


		MovimentacaoTipoMovimentacaoTipoClassedt =(MovimentacaoTipoMovimentacaoTipoClasseDt)request.getSession().getAttribute("MovimentacaoTipoMovimentacaoTipoClassedt");
		if (MovimentacaoTipoMovimentacaoTipoClassedt == null )  MovimentacaoTipoMovimentacaoTipoClassedt = new MovimentacaoTipoMovimentacaoTipoClasseDt();  

		MovimentacaoTipoMovimentacaoTipoClassedt.setMovimentacaoTipoMovimentacaoTipoClasse( request.getParameter("MovimentacaoTipoMovimentacaoTipoClasse")); 
		MovimentacaoTipoMovimentacaoTipoClassedt.setId_MovimentacaoTipo( request.getParameter("Id_MovimentacaoTipo")); 
		MovimentacaoTipoMovimentacaoTipoClassedt.setMovimentacaoTipo( request.getParameter("MovimentacaoTipo")); 
		MovimentacaoTipoMovimentacaoTipoClassedt.setId_MovimentacaoTipoClasse( request.getParameter("Id_MovimentacaoTipoClasse")); 
		MovimentacaoTipoMovimentacaoTipoClassedt.setMoviTipoClasse( request.getParameter("MoviTipoClasse")); 

		MovimentacaoTipoMovimentacaoTipoClassedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoTipoMovimentacaoTipoClassedt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,MovimentacaoTipoMovimentacaoTipoClassedt.getId_MovimentacaoTipoClasse() ,MovimentacaoTipoMovimentacaoTipoClassene, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				MovimentacaoTipoMovimentacaoTipoClassedt.limpar();
				request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=MovimentacaoTipoMovimentacaoTipoClassene.Verificar(MovimentacaoTipoMovimentacaoTipoClassedt); 
				if (Mensagem.length()==0){
					MovimentacaoTipoMovimentacaoTipoClassene.salvarMultiplo(MovimentacaoTipoMovimentacaoTipoClassedt, idsDados); 
					localizar(request,MovimentacaoTipoMovimentacaoTipoClassedt.getId_MovimentacaoTipoClasse() ,MovimentacaoTipoMovimentacaoTipoClassene, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (MovimentacaoTipoClasseDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				request.setAttribute("tempBuscaID_MOVI_TIPO_CLASSE","ID_MOVI_TIPO_CLASSE");
				request.setAttribute("tempBuscaMOVI_TIPO_CLASSE","MOVI_TIPO_CLASSE");
				request.setAttribute("tempRetorno","MovimentacaoTipoMovimentacaoTipoClasse");
				stAcao="/WEB-INF/jsptjgo/MovimentacaoTipoClasseLocalizar.jsp";
				tempList =MovimentacaoTipoMovimentacaoTipoClassene.consultarDescricaoMovimentacaoTipoClasse(tempNomeBusca, PosicaoPaginaAtual);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", MovimentacaoTipoMovimentacaoTipoClassene.getQuantidadePaginas());
				if (tempList.size()>0){
					request.setAttribute("ListaMovimentacaoTipoClasse", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("tempRetorno","MovimentacaoTipoMovimentacaoTipoClasse?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("MovimentacaoTipoMovimentacaoTipoClassedt",MovimentacaoTipoMovimentacaoTipoClassedt );
		request.getSession().setAttribute("MovimentacaoTipoMovimentacaoTipoClassene",MovimentacaoTipoMovimentacaoTipoClassene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, MovimentacaoTipoMovimentacaoTipoClasseNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarMovimentacaoTipoMovimentacaoTipoClasseUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
