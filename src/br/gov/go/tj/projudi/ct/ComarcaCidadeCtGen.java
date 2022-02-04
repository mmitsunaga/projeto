package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ne.ComarcaCidadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ComarcaCidadeCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 8623206414063798614L;

    public  ComarcaCidadeCtGen() {

	} 
		public int Permissao(){
			return ComarcaCidadeDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ComarcaCidadeDt ComarcaCidadedt;
		ComarcaCidadeNe ComarcaCidadene;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ComarcaCidade.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ComarcaCidade");
		request.setAttribute("ListaUlLiComarcaCidade","");




		ComarcaCidadene =(ComarcaCidadeNe)request.getSession().getAttribute("ComarcaCidadene");
		if (ComarcaCidadene == null )  ComarcaCidadene = new ComarcaCidadeNe();  


		ComarcaCidadedt =(ComarcaCidadeDt)request.getSession().getAttribute("ComarcaCidadedt");
		if (ComarcaCidadedt == null )  ComarcaCidadedt = new ComarcaCidadeDt();  

		ComarcaCidadedt.setId_Comarca( request.getParameter("Id_Comarca")); 
		ComarcaCidadedt.setComarca( request.getParameter("Comarca")); 
		ComarcaCidadedt.setId_Cidade( request.getParameter("Id_Cidade")); 
		ComarcaCidadedt.setCidade( request.getParameter("Cidade")); 

		ComarcaCidadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ComarcaCidadedt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,ComarcaCidadedt.getId_Comarca() ,ComarcaCidadene, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				ComarcaCidadedt.limpar();
				request.setAttribute("ListaUlLiComarcaCidade", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=ComarcaCidadene.Verificar(ComarcaCidadedt); 
				if (Mensagem.length()==0){
					ComarcaCidadene.salvarMultiplo(ComarcaCidadedt, idsDados); 
					localizar(request,ComarcaCidadedt.getId_Comarca() ,ComarcaCidadene, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				request.setAttribute("tempBuscaId_Comarca","Id_Comarca");
				request.setAttribute("tempBuscaComarca","Comarca");
				request.setAttribute("tempRetorno","ComarcaCidade");
				stAcao="/WEB-INF/jsptjgo/ComarcaLocalizar.jsp";
				tempList =ComarcaCidadene.consultarDescricaoComarca(tempNomeBusca, PosicaoPaginaAtual);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", ComarcaCidadene.getQuantidadePaginas());
				if (tempList.size()>0){
					request.setAttribute("ListaComarca", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("tempRetorno","ComarcaCidade?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("ComarcaCidadedt",ComarcaCidadedt );
		request.getSession().setAttribute("ComarcaCidadene",ComarcaCidadene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, ComarcaCidadeNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarCidadeComarcaUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiComarcaCidade", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
