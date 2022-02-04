package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoPermissaoDt;
import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.ne.GrupoPermissaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoPermissaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7580506104503536584L;

    public  GrupoPermissaoCtGen() {

	} 
		public int Permissao(){
			return GrupoPermissaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoPermissaoDt GrupoPermissaodt;
		GrupoPermissaoNe GrupoPermissaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GrupoPermissao");
		request.setAttribute("tempBuscaId_GrupoPermissao","Id_GrupoPermissao");
		request.setAttribute("tempBuscaGrupoPermissao","GrupoPermissao");
		request.setAttribute("tempBuscaId_Grupo",request.getParameter("tempBuscaId_Grupo"));
		request.setAttribute("tempBuscaGrupo",request.getParameter("tempBuscaGrupo"));
		request.setAttribute("tempBuscaId_Permissao",request.getParameter("tempBuscaId_Permissao"));
		request.setAttribute("tempBuscaPermissao",request.getParameter("tempBuscaPermissao"));



		GrupoPermissaone =(GrupoPermissaoNe)request.getSession().getAttribute("GrupoPermissaone");
		if (GrupoPermissaone == null )  GrupoPermissaone = new GrupoPermissaoNe();  


		GrupoPermissaodt =(GrupoPermissaoDt)request.getSession().getAttribute("GrupoPermissaodt");
		if (GrupoPermissaodt == null )  GrupoPermissaodt = new GrupoPermissaoDt();  

		GrupoPermissaodt.setId_Grupo( request.getParameter("Id_Grupo")); 

		GrupoPermissaodt.setGrupo( request.getParameter("Grupo")); 

		GrupoPermissaodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 

		GrupoPermissaodt.setId_Permissao( request.getParameter("Id_Permissao")); 

		GrupoPermissaodt.setPermissao( request.getParameter("Permissao")); 

		GrupoPermissaodt.setPermissaoCodigo( request.getParameter("PermissaoCodigo")); 


		GrupoPermissaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoPermissaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				GrupoPermissaone.excluir(GrupoPermissaodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				tempList =GrupoPermissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGrupoPermissao", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GrupoPermissaone.getQuantidadePaginas());
					GrupoPermissaodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.LocalizarDWR: 
				break;
			case Configuracao.Novo: 
				GrupoPermissaodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=GrupoPermissaone.Verificar(GrupoPermissaodt); 
				if (Mensagem.length()==0){
					GrupoPermissaone.salvar(GrupoPermissaodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =GrupoPermissaone.consultarDescricaoGrupo(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaGrupo", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", GrupoPermissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (PermissaoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =GrupoPermissaone.consultarDescricaoPermissao(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaPermissao", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", GrupoPermissaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_GrupoPermissao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GrupoPermissaodt.getId()))){
						GrupoPermissaodt.limpar();
						GrupoPermissaodt = GrupoPermissaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("GrupoPermissaodt",GrupoPermissaodt );
		request.getSession().setAttribute("GrupoPermissaone",GrupoPermissaone );

		RequestDispatcher dis =	request.getRequestDispatcher("/WEB-INF/jsptjgo/GrupoPermissao.jsp");
		dis.include(request, response);
	}
}
