package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt;
import br.gov.go.tj.projudi.ne.UsuarioServentiaAfastamentoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioServentiaAfastamentoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4277610977293264119L;

    public  UsuarioServentiaAfastamentoCtGen() {

	} 
		public int Permissao(){
			return UsuarioServentiaAfastamentoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioServentiaAfastamentoDt UsuarioServentiaAfastamentodt;
		UsuarioServentiaAfastamentoNe UsuarioServentiaAfastamentone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/UsuarioServentiaAfastamento.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","UsuarioServentiaAfastamento");
		request.setAttribute("tempBuscaId_UsuarioServentiaAfastamento","Id_UsuarioServentiaAfastamento");
		request.setAttribute("tempBuscaUsuarioServentiaAfastamento","UsuarioServentiaAfastamento");
		
		request.setAttribute("tempBuscaId_UsuarioServentia","Id_UsuarioServentia");
		request.setAttribute("tempBuscaUsuario","Usuario");
		request.setAttribute("tempBuscaId_Afastamento","Id_Afastamento");
		request.setAttribute("tempBuscaAfastamento","Afastamento");

		request.setAttribute("tempRetorno","UsuarioServentiaAfastamento");



		UsuarioServentiaAfastamentone =(UsuarioServentiaAfastamentoNe)request.getSession().getAttribute("UsuarioServentiaAfastamentone");
		if (UsuarioServentiaAfastamentone == null )  UsuarioServentiaAfastamentone = new UsuarioServentiaAfastamentoNe();  


		UsuarioServentiaAfastamentodt =(UsuarioServentiaAfastamentoDt)request.getSession().getAttribute("UsuarioServentiaAfastamentodt");
		if (UsuarioServentiaAfastamentodt == null )  UsuarioServentiaAfastamentodt = new UsuarioServentiaAfastamentoDt();  

		UsuarioServentiaAfastamentodt.setId_UsuarioServentia( request.getParameter("Id_Usuario")); 
		UsuarioServentiaAfastamentodt.setUsuario( request.getParameter("Usuario")); 
		UsuarioServentiaAfastamentodt.setId_Afastamento( request.getParameter("Id_Afastamento")); 
		UsuarioServentiaAfastamentodt.setAfastamento( request.getParameter("Afastamento")); 
		UsuarioServentiaAfastamentodt.setDataInicio( request.getParameter("DataInicio")); 
		UsuarioServentiaAfastamentodt.setDataFim( request.getParameter("DataFim")); 

		UsuarioServentiaAfastamentodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioServentiaAfastamentodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				UsuarioServentiaAfastamentone.excluir(UsuarioServentiaAfastamentodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/UsuarioAfastamentoLocalizar.jsp";
				tempList =UsuarioServentiaAfastamentone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaUsuarioAfastamento", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", UsuarioServentiaAfastamentone.getQuantidadePaginas());
					UsuarioServentiaAfastamentodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				UsuarioServentiaAfastamentodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=UsuarioServentiaAfastamentone.Verificar(UsuarioServentiaAfastamentodt); 
				if (Mensagem.length()==0){
					UsuarioServentiaAfastamentone.salvar(UsuarioServentiaAfastamentodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =UsuarioServentiaAfastamentone.consultarDescricaoUsuario(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaUsuario", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", UsuarioServentiaAfastamentone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (AfastamentoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					stAcao="/WEB-INF/jsptjgo/AfastamentoLocalizar.jsp";
						tempList =UsuarioServentiaAfastamentone.consultarDescricaoAfastamento(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaAfastamento", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", UsuarioServentiaAfastamentone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_UsuarioServentiaAfastamento");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( UsuarioServentiaAfastamentodt.getId()))){
						UsuarioServentiaAfastamentodt.limpar();
						UsuarioServentiaAfastamentodt = UsuarioServentiaAfastamentone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("UsuarioServentiaAfastamentodt",UsuarioServentiaAfastamentodt );
		request.getSession().setAttribute("UsuarioServentiaAfastamentone",UsuarioServentiaAfastamentone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
