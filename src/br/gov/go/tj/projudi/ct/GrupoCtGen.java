package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.GrupoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 4926929614090910160L;

    public  GrupoCtGen() {

	} 
		public int Permissao(){
			return GrupoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoDt Grupodt;
		GrupoNe Grupone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Grupo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Grupo");




		Grupone =(GrupoNe)request.getSession().getAttribute("Grupone");
		if (Grupone == null )  Grupone = new GrupoNe();  


		Grupodt =(GrupoDt)request.getSession().getAttribute("Grupodt");
		if (Grupodt == null )  Grupodt = new GrupoDt();  

		Grupodt.setGrupo( request.getParameter("Grupo")); 
		Grupodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		Grupodt.setId_ServentiaTipo( request.getParameter("Id_ServentiaTipo")); 
		Grupodt.setServentiaTipo( request.getParameter("ServentiaTipo")); 
		Grupodt.setId_GrupoTipo( request.getParameter("Id_GrupoTipo")); 
		Grupodt.setGrupoTipo( request.getParameter("GrupoTipo")); 
		Grupodt.setServentiaTipoCodigo( request.getParameter("ServentiaTipoCodigo")); 
		Grupodt.setGrupoTipoCodigo( request.getParameter("GrupoTipoCodigo")); 

		Grupodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Grupodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Grupone.excluir(Grupodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/GrupoLocalizar.jsp";
				request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
				request.setAttribute("tempBuscaGrupo","Grupo");
				request.setAttribute("tempRetorno","Grupo");
				tempList =Grupone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGrupo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Grupone.getQuantidadePaginas());
					Grupodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Grupodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Grupone.Verificar(Grupodt); 
					if (Mensagem.length()==0){
						Grupone.salvar(Grupodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Grupo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Grupodt.getId()))){
						Grupodt.limpar();
						Grupodt = Grupone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Grupodt",Grupodt );
		request.getSession().setAttribute("Grupone",Grupone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
