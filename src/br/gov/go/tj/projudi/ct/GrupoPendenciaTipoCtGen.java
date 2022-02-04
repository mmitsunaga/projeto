package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoPendenciaTipoDt;
import br.gov.go.tj.projudi.ne.GrupoPendenciaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GrupoPendenciaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 5121606879281465372L;

    public  GrupoPendenciaTipoCtGen() {

	} 
		public int Permissao(){
			return GrupoPendenciaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoPendenciaTipoDt GrupoPendenciaTipodt;
		GrupoPendenciaTipoNe GrupoPendenciaTipone;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/GrupoPendenciaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GrupoPendenciaTipo");
		request.setAttribute("tempBuscaId_GrupoPendenciaTipo","Id_GrupoPendenciaTipo");
		request.setAttribute("tempBuscaGrupoPendenciaTipo","GrupoPendenciaTipo");
		request.setAttribute("ListaUlLiGrupoPendenciaTipo","");
		request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
		request.setAttribute("tempBuscaGrupo","Grupo");
		request.setAttribute("tempBuscaId_PendenciaTipo","Id_PendenciaTipo");
		request.setAttribute("tempBuscaPendenciaTipo","PendenciaTipo");

		request.setAttribute("tempRetorno","GrupoPendenciaTipo");



		GrupoPendenciaTipone =(GrupoPendenciaTipoNe)request.getSession().getAttribute("GrupoPendenciaTipone");
		if (GrupoPendenciaTipone == null )  GrupoPendenciaTipone = new GrupoPendenciaTipoNe();  


		GrupoPendenciaTipodt =(GrupoPendenciaTipoDt)request.getSession().getAttribute("GrupoPendenciaTipodt");
		if (GrupoPendenciaTipodt == null )  GrupoPendenciaTipodt = new GrupoPendenciaTipoDt();  

		GrupoPendenciaTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		GrupoPendenciaTipodt.setGrupo( request.getParameter("Grupo")); 
		GrupoPendenciaTipodt.setId_PendenciaTipo( request.getParameter("Id_PendenciaTipo")); 
		GrupoPendenciaTipodt.setPendenciaTipo( request.getParameter("PendenciaTipo")); 
		GrupoPendenciaTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		GrupoPendenciaTipodt.setPendenciaTipoCodigo( request.getParameter("PendenciaTipoCodigo")); 

		GrupoPendenciaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoPendenciaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				localizar(request,GrupoPendenciaTipodt.getId_Grupo() ,GrupoPendenciaTipone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				GrupoPendenciaTipodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				request.setAttribute("ListaUlLiGrupoPendenciaTipo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=GrupoPendenciaTipone.Verificar(GrupoPendenciaTipodt); 
				if (Mensagem.length()==0){
					GrupoPendenciaTipone.salvarMultiplo(GrupoPendenciaTipodt, idsDados); 
					localizar(request,GrupoPendenciaTipodt.getId_Grupo() ,GrupoPendenciaTipone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				stAcao="/WEB-INF/jsptjgo/GrupoLocalizar.jsp";
				tempList =GrupoPendenciaTipone.consultarDescricaoGrupo(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGrupo", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GrupoPendenciaTipone.getQuantidadePaginas());
					request.setAttribute("tempRetorno","GrupoPendenciaTipo?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
		}

		request.getSession().setAttribute("GrupoPendenciaTipodt",GrupoPendenciaTipodt );
		request.getSession().setAttribute("GrupoPendenciaTipone",GrupoPendenciaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, GrupoPendenciaTipoNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarPendenciaTipoGrupoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiGrupoPendenciaTipo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
                    request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
