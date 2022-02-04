package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaOabNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioServentiaOabCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1429653990177333580L;

    public  UsuarioServentiaOabCtGen() {

	} 
		public int Permissao(){
			return UsuarioServentiaOabDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioServentiaOabDt UsuarioServentiaOabdt;
		UsuarioServentiaOabNe UsuarioServentiaOabne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","UsuarioServentiaOab");
		request.setAttribute("tempBuscaId_UsuarioServentiaOab","Id_UsuarioServentiaOab");
		request.setAttribute("tempBuscaUsuarioServentiaOab","UsuarioServentiaOab");
		request.setAttribute("tempBuscaId_UsuarioServentia",request.getParameter("tempBuscaId_UsuarioServentia"));
		request.setAttribute("tempBuscaUsuarioServentia",request.getParameter("tempBuscaUsuarioServentia"));



		UsuarioServentiaOabne =(UsuarioServentiaOabNe)request.getSession().getAttribute("UsuarioServentiaOabne");
		if (UsuarioServentiaOabne == null )  UsuarioServentiaOabne = new UsuarioServentiaOabNe();  


		UsuarioServentiaOabdt =(UsuarioServentiaOabDt)request.getSession().getAttribute("UsuarioServentiaOabdt");
		if (UsuarioServentiaOabdt == null )  UsuarioServentiaOabdt = new UsuarioServentiaOabDt();  

		UsuarioServentiaOabdt.setOabNumero( request.getParameter("OabNumero")); 

		UsuarioServentiaOabdt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia")); 

		UsuarioServentiaOabdt.setOabComplemento( request.getParameter("OabComplemento")); 


		UsuarioServentiaOabdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioServentiaOabdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				UsuarioServentiaOabne.excluir(UsuarioServentiaOabdt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				tempList =UsuarioServentiaOabne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaUsuarioServentiaOab", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", UsuarioServentiaOabne.getQuantidadePaginas());
					UsuarioServentiaOabdt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				UsuarioServentiaOabdt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=UsuarioServentiaOabne.Verificar(UsuarioServentiaOabdt); 
				if (Mensagem.length()==0){
					UsuarioServentiaOabne.salvar(UsuarioServentiaOabdt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =UsuarioServentiaOabne.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaUsuarioServentia", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", UsuarioServentiaOabne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_UsuarioServentiaOab");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( UsuarioServentiaOabdt.getId()))){
						UsuarioServentiaOabdt.limpar();
						UsuarioServentiaOabdt = UsuarioServentiaOabne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("UsuarioServentiaOabdt",UsuarioServentiaOabdt );
		request.getSession().setAttribute("UsuarioServentiaOabne",UsuarioServentiaOabne );

		RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/UsuarioServentiaOab.jsp");
		dis.include(request, response);
	}
}
