package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.ne.ForumNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ForumCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7744814977907872768L;

    public  ForumCtGen() {

	} 
		public int Permissao(){
			return ForumDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ForumDt Forumdt;
		ForumNe Forumne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Forum.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Forum");




		Forumne =(ForumNe)request.getSession().getAttribute("Forumne");
		if (Forumne == null )  Forumne = new ForumNe();  


		Forumdt =(ForumDt)request.getSession().getAttribute("Forumdt");
		if (Forumdt == null )  Forumdt = new ForumDt();  

		Forumdt.setForum( request.getParameter("Forum")); 
		Forumdt.setForumCodigo( request.getParameter("ForumCodigo")); 
		Forumdt.setId_Comarca( request.getParameter("Id_Comarca")); 
		Forumdt.setComarca( request.getParameter("Comarca")); 
		Forumdt.setId_Endereco( request.getParameter("Id_Endereco")); 
//		Forumdt.setEndereco( request.getParameter("Endereco")); 
		Forumdt.setComarcaCodigo( request.getParameter("ComarcaCodigo")); 

		Forumdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Forumdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Forumne.excluir(Forumdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ForumLocalizar.jsp";
				request.setAttribute("tempBuscaId_Forum","Id_Forum");
				request.setAttribute("tempBuscaForum","Forum");
				request.setAttribute("tempRetorno","Forum");
				tempList =Forumne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaForum", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Forumne.getQuantidadePaginas());
					Forumdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Forumdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Forumne.Verificar(Forumdt); 
					if (Mensagem.length()==0){
						Forumne.salvar(Forumdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Comarca","Id_Comarca");
					request.setAttribute("tempBuscaComarca","Comarca");
					request.setAttribute("tempRetorno","Forum");
					stAcao="/WEB-INF/jsptjgo/ComarcaLocalizar.jsp";
					tempList =Forumne.consultarDescricaoComarca(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaComarca", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Forumne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EnderecoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Endereco","Id_Endereco");
					request.setAttribute("tempBuscaEndereco","Endereco");
					request.setAttribute("tempRetorno","Forum");
					stAcao="/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
					tempList =Forumne.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEndereco", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Forumne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Forum");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Forumdt.getId()))){
						Forumdt.limpar();
						Forumdt = Forumne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Forumdt",Forumdt );
		request.getSession().setAttribute("Forumne",Forumne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
