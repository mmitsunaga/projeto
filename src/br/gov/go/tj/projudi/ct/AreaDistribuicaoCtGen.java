package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AreaDistribuicaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 1067703396643389943L;

    public  AreaDistribuicaoCtGen() {

	} 
		public int Permissao(){
			return AreaDistribuicaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AreaDistribuicaoDt AreaDistribuicaodt;
		AreaDistribuicaoNe AreaDistribuicaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/AreaDistribuicao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","AreaDistribuicao");

		AreaDistribuicaone =(AreaDistribuicaoNe)request.getSession().getAttribute("AreaDistribuicaone");
		if (AreaDistribuicaone == null )  AreaDistribuicaone = new AreaDistribuicaoNe();  

		AreaDistribuicaodt =(AreaDistribuicaoDt)request.getSession().getAttribute("AreaDistribuicaodt");
		if (AreaDistribuicaodt == null )  AreaDistribuicaodt = new AreaDistribuicaoDt();  

		AreaDistribuicaodt.setAreaDistribuicao( request.getParameter("AreaDistribuicao")); 
		AreaDistribuicaodt.setAreaDistribuicaoCodigo( request.getParameter("AreaDistribuicaoCodigo")); 
		AreaDistribuicaodt.setId_Forum( request.getParameter("Id_Forum")); 
		AreaDistribuicaodt.setForum( request.getParameter("Forum")); 
		AreaDistribuicaodt.setId_ServentiaSubtipo( request.getParameter("Id_ServentiaSubtipo")); 
		AreaDistribuicaodt.setServentiaSubtipo( request.getParameter("ServentiaSubtipo")); 
		AreaDistribuicaodt.setId_AreaDistribuicaoRelacionada( request.getParameter("Id_AreaDistribuicaoRelacionada")); 
		AreaDistribuicaodt.setAreaDistribuicaoRelacionada( request.getParameter("AreaDistribuicaoRelacionada")); 
		AreaDistribuicaodt.setComarcaCodigo( request.getParameter("ComarcaCodigo")); 
		AreaDistribuicaodt.setServentiaSubtipoCodigo( request.getParameter("ServentiaSubtipoCodigo")); 
		AreaDistribuicaodt.setId_Comarca( request.getParameter("Id_Comarca")); 
		AreaDistribuicaodt.setComarca( request.getParameter("Comarca")); 
		AreaDistribuicaodt.setForumCodigo( request.getParameter("ForumCodigo")); 

		AreaDistribuicaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		AreaDistribuicaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					AreaDistribuicaone.excluir(AreaDistribuicaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AreaDistribuicaoLocalizar.jsp";
				request.setAttribute("tempBuscaId_AreaDistribuicao","Id_AreaDistribuicao");
				request.setAttribute("tempBuscaAreaDistribuicao","AreaDistribuicao");
				request.setAttribute("tempRetorno","AreaDistribuicao");
				tempList =AreaDistribuicaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAreaDistribuicao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", AreaDistribuicaone.getQuantidadePaginas());
					AreaDistribuicaodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				AreaDistribuicaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=AreaDistribuicaone.Verificar(AreaDistribuicaodt); 
					if (Mensagem.length()==0){
						AreaDistribuicaone.salvar(AreaDistribuicaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;

				case (ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ServentiaSubtipo","Id_ServentiaSubtipo");
					request.setAttribute("tempBuscaServentiaSubtipo","ServentiaSubtipo");
					request.setAttribute("tempRetorno","AreaDistribuicao");
					stAcao="/WEB-INF/jsptjgo/ServentiaSubtipoLocalizar.jsp";
					tempList =AreaDistribuicaone.consultarDescricaoServentiaSubtipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentiaSubtipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AreaDistribuicaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AreaDistribuicaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_AreaDistribuicaoRelacionada","Id_AreaDistribuicaoRelacionada");
					request.setAttribute("tempBuscaAreaDistribuicaoRelacionada","AreaDistribuicaoRelacionada");
					request.setAttribute("tempRetorno","AreaDistribuicao");
					stAcao="/WEB-INF/jsptjgo/AreaDistribuicaoRelacionadaLocalizar.jsp";
					tempList =AreaDistribuicaone.consultarDescricaoAreaDistribuicaoRelacionada(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaAreaDistribuicaoRelacionada", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AreaDistribuicaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ComarcaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Comarca","Id_Comarca");
					request.setAttribute("tempBuscaComarca","Comarca");
					request.setAttribute("tempRetorno","AreaDistribuicao");
					stAcao="/WEB-INF/jsptjgo/ComarcaLocalizar.jsp";
					tempList =AreaDistribuicaone.consultarDescricaoComarca(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaComarca", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", AreaDistribuicaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_AreaDistribuicao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( AreaDistribuicaodt.getId()))){
						AreaDistribuicaodt.limpar();
						AreaDistribuicaodt = AreaDistribuicaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("AreaDistribuicaodt",AreaDistribuicaodt );
		request.getSession().setAttribute("AreaDistribuicaone",AreaDistribuicaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
