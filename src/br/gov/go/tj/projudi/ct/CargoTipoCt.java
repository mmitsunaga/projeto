package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.CargoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CargoTipoCt extends CargoTipoCtGen {

	private static final long serialVersionUID = 1027923736528815492L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CargoTipoDt CargoTipodt;
		CargoTipoNe CargoTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao = "/WEB-INF/jsptjgo/CargoTipo.jsp";
		
		request.setAttribute("descCuringa", "");
		request.setAttribute("tempPrograma", "CargoTipo");
		request.setAttribute("tempBuscaId_CargoTipo", "Id_CargoTipo");
		request.setAttribute("tempBuscaCargoTipo", "CargoTipo");
		request.setAttribute("tempBuscaId_Grupo", "Id_Grupo");
		request.setAttribute("tempBuscaGrupo", "Grupo");
		request.setAttribute("tempRetorno", "CargoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		CargoTipone = (CargoTipoNe) request.getSession().getAttribute("CargoTipone");
		if (CargoTipone == null) CargoTipone = new CargoTipoNe();

		CargoTipodt = (CargoTipoDt) request.getSession().getAttribute("CargoTipodt");
		if (CargoTipodt == null) CargoTipodt = new CargoTipoDt();

		CargoTipodt.setCargoTipo(request.getParameter("CargoTipo"));
		CargoTipodt.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
		CargoTipodt.setId_Grupo(request.getParameter("Id_Grupo"));
		CargoTipodt.setGrupo(request.getParameter("Grupo"));
		CargoTipodt.setGrupoCodigo(request.getParameter("GrupoCodigo"));
		CargoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CargoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"CargoTipo"};
					String[] lisDescricao = {"CargoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_CargoTipo");
					request.setAttribute("tempBuscaDescricao", "CargoTipo");
					request.setAttribute("tempBuscaPrograma", "CargoTipo");
					request.setAttribute("tempRetorno", "CargoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "CargoTipo");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					CargoTipodt.limpar();
				} else {
					String stTemp = "";
					stTemp = CargoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "CargoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Grupo");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual",  String.valueOf(GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
					stTemp = CargoTipone.consultarDescricaoGrupoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				default:
					super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
					return;
		}

		request.getSession().setAttribute("CargoTipone", CargoTipone);
		request.getSession().setAttribute("CargoTipodt", CargoTipodt);
	
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
