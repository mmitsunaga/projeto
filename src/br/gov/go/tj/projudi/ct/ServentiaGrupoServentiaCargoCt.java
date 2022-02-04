package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoServentiaCargoDt;
import br.gov.go.tj.projudi.ne.ServentiaGrupoServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaGrupoServentiaCargoCt extends ServentiaGrupoServentiaCargoCtGen{

	private static final long serialVersionUID = 1430390277343010258L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaGrupoServentiaCargoDt ServentiaGrupoServentiaCargodt;
		ServentiaGrupoServentiaCargoNe ServentiaGrupoServentiaCargone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null){ 
			stNomeBusca1 = request.getParameter("nomeBusca1");
		}
		
		if(request.getParameter("nomeBusca2") != null) {
			stNomeBusca2 = request.getParameter("nomeBusca2");
		}
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/ServentiaGrupoServentiaCargo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		
		request.setAttribute("tempPrograma","ServentiaGrupoServentiaCargo");
		request.setAttribute("ListaUlLiServentiaGrupoServentiaCargo","");
		request.setAttribute("tempBuscaID_SERV_GRUPO","ID_SERV_GRUPO");
		request.setAttribute("tempBuscaSERV_GRUPO","SERV_GRUPO");
		request.setAttribute("tempRetorno","ServentiaGrupoServentiaCargo");

		ServentiaGrupoServentiaCargone =(ServentiaGrupoServentiaCargoNe)request.getSession().getAttribute("ServentiaGrupoServentiaCargone");
		if (ServentiaGrupoServentiaCargone == null )  ServentiaGrupoServentiaCargone = new ServentiaGrupoServentiaCargoNe();  

		ServentiaGrupoServentiaCargodt =(ServentiaGrupoServentiaCargoDt)request.getSession().getAttribute("ServentiaGrupoServentiaCargodt");
		if (ServentiaGrupoServentiaCargodt == null )  ServentiaGrupoServentiaCargodt = new ServentiaGrupoServentiaCargoDt();  

		ServentiaGrupoServentiaCargodt.setServentiaCargoServentiaGrupo( request.getParameter("ServentiaCargoServentiaGrupo")); 
		ServentiaGrupoServentiaCargodt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		ServentiaGrupoServentiaCargodt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		ServentiaGrupoServentiaCargodt.setId_ServentiaGrupo( request.getParameter("Id_ServentiaGrupo")); 
		ServentiaGrupoServentiaCargodt.setServentiaGrupo( request.getParameter("ServentiaGrupo")); 

		ServentiaGrupoServentiaCargodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaGrupoServentiaCargodt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case (ServentiaGrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				ServentiaGrupoServentiaCargodt = new ServentiaGrupoServentiaCargoDt(); 
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Unidade de Trabalho", "Serventia"};
					String[] lisDescricao = {"Função (Unidade de Trabalho)", "Serventia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaGrupo");
					request.setAttribute("tempBuscaDescricao","ServentiaGrupo");
					request.setAttribute("tempBuscaPrograma","Função (Unidade de Trabalho)");
					request.setAttribute("tempRetorno","ServentiaGrupoServentiaCargo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ServentiaGrupoDt.CodigoPermissao   * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else {
					String stTemp = "";
					stTemp = ServentiaGrupoServentiaCargone.consultarDescricaoServentiaGrupoDoisParametrosBuscaJSON(stNomeBusca1, stNomeBusca2, UsuarioSessao.getGrupoCodigoToInt(), PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("ServentiaGrupoServentiaCargodt",ServentiaGrupoServentiaCargodt );
		request.getSession().setAttribute("ServentiaGrupoServentiaCargone",ServentiaGrupoServentiaCargone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
