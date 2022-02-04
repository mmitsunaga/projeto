package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaGrupoNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class UsuarioServentiaGrupoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8259486109925109726L;

    public  UsuarioServentiaGrupoCtGen() {

	} 
		public int Permissao(){
			return UsuarioServentiaGrupoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioServentiaGrupoDt UsuarioServentiaGrupodt;
		UsuarioServentiaGrupoNe UsuarioServentiaGrupone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/UsuarioServentiaGrupo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","UsuarioServentiaGrupo");




		UsuarioServentiaGrupone =(UsuarioServentiaGrupoNe)request.getSession().getAttribute("UsuarioServentiaGrupone");
		if (UsuarioServentiaGrupone == null )  UsuarioServentiaGrupone = new UsuarioServentiaGrupoNe();  


		UsuarioServentiaGrupodt =(UsuarioServentiaGrupoDt)request.getSession().getAttribute("UsuarioServentiaGrupodt");
		if (UsuarioServentiaGrupodt == null )  UsuarioServentiaGrupodt = new UsuarioServentiaGrupoDt();  

		UsuarioServentiaGrupodt.setUsuarioServentiaGrupo( request.getParameter("UsuarioServentiaGrupo")); 
		UsuarioServentiaGrupodt.setId_UsuarioServentia( request.getParameter("Id_UsuarioServentia")); 
		UsuarioServentiaGrupodt.setUsuarioServentia( request.getParameter("UsuarioServentia")); 
		UsuarioServentiaGrupodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		UsuarioServentiaGrupodt.setGrupo( request.getParameter("Grupo")); 
		if (request.getParameter("Ativo") != null)
			UsuarioServentiaGrupodt.setAtivo( request.getParameter("Ativo")); 
		else UsuarioServentiaGrupodt.setAtivo("false");
		UsuarioServentiaGrupodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		UsuarioServentiaGrupodt.setNome( request.getParameter("Nome")); 
		UsuarioServentiaGrupodt.setId_Serventia( request.getParameter("Id_Serventia")); 
		UsuarioServentiaGrupodt.setServentia( request.getParameter("Serventia")); 

		UsuarioServentiaGrupodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioServentiaGrupodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					UsuarioServentiaGrupone.excluir(UsuarioServentiaGrupodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				UsuarioServentiaGrupodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=UsuarioServentiaGrupone.Verificar(UsuarioServentiaGrupodt); 
					if (Mensagem.length()==0){
						UsuarioServentiaGrupone.salvar(UsuarioServentiaGrupodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_UsuarioServentia","Id_UsuarioServentia");
					request.setAttribute("tempBuscaUsuarioServentia","UsuarioServentia");
					request.setAttribute("tempRetorno","UsuarioServentiaGrupo");
					stAcao="/WEB-INF/jsptjgo/UsuarioServentiaLocalizar.jsp";
					tempList =UsuarioServentiaGrupone.consultarDescricaoUsuarioServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaUsuarioServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", UsuarioServentiaGrupone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
					request.setAttribute("tempBuscaGrupo","Grupo");
					request.setAttribute("tempRetorno","UsuarioServentiaGrupo");
					stAcao="/WEB-INF/jsptjgo/GrupoLocalizar.jsp";
					tempList =UsuarioServentiaGrupone.consultarDescricaoGrupo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaGrupo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", UsuarioServentiaGrupone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Serventia","Id_Serventia");
					request.setAttribute("tempBuscaServentia","Serventia");
					request.setAttribute("tempRetorno","UsuarioServentiaGrupo");
					stAcao="/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
					tempList =UsuarioServentiaGrupone.consultarDescricaoServentia(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaServentia", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", UsuarioServentiaGrupone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_UsuarioServentiaGrupo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( UsuarioServentiaGrupodt.getId()))){
						UsuarioServentiaGrupodt.limpar();
						UsuarioServentiaGrupodt = UsuarioServentiaGrupone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("UsuarioServentiaGrupodt",UsuarioServentiaGrupodt );
		request.getSession().setAttribute("UsuarioServentiaGrupone",UsuarioServentiaGrupone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
