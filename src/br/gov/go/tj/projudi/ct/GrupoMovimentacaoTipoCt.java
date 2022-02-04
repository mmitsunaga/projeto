package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.GrupoMovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GrupoMovimentacaoTipoCt extends GrupoMovimentacaoTipoCtGen{

    private static final long serialVersionUID = 3090217764211063526L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GrupoMovimentacaoTipoDt GrupoMovimentacaoTipodt;
		GrupoMovimentacaoTipoNe GrupoMovimentacaoTipone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/GrupoMovimentacaoTipo.jsp";

		request.setAttribute("tempPrograma","Grupo Movimentação Tipo");
		request.setAttribute("tempBuscaId_GrupoMovimentacaoTipo","Id_GrupoMovimentacaoTipo");
		request.setAttribute("tempBuscaGrupoMovimentacaoTipo","GrupoMovimentacaoTipo");
		request.setAttribute("ListaUlLiGrupoMovimentacaoTipo","");
		request.setAttribute("tempBuscaId_Grupo","Id_Grupo");
		request.setAttribute("tempBuscaGrupo","Grupo");
		request.setAttribute("tempBuscaId_MovimentacaoTipo","Id_MovimentacaoTipo");
		request.setAttribute("tempBuscaMovimentacaoTipo","MovimentacaoTipo");

		request.setAttribute("tempRetorno","GrupoMovimentacaoTipo");

		GrupoMovimentacaoTipone =(GrupoMovimentacaoTipoNe)request.getSession().getAttribute("GrupoMovimentacaoTipone");
		if (GrupoMovimentacaoTipone == null )  GrupoMovimentacaoTipone = new GrupoMovimentacaoTipoNe();  

		GrupoMovimentacaoTipodt =(GrupoMovimentacaoTipoDt)request.getSession().getAttribute("GrupoMovimentacaoTipodt");
		if (GrupoMovimentacaoTipodt == null )  GrupoMovimentacaoTipodt = new GrupoMovimentacaoTipoDt();  

		GrupoMovimentacaoTipodt.setId_Grupo( request.getParameter("Id_Grupo")); 
		GrupoMovimentacaoTipodt.setGrupo( request.getParameter("Grupo")); 
		GrupoMovimentacaoTipodt.setId_MovimentacaoTipo( request.getParameter("Id_MovimentacaoTipo")); 
		GrupoMovimentacaoTipodt.setMovimentacaoTipo( request.getParameter("MovimentacaoTipo")); 
		GrupoMovimentacaoTipodt.setGrupoCodigo( request.getParameter("GrupoCodigo")); 
		GrupoMovimentacaoTipodt.setMovimentacaoTipoCodigo( request.getParameter("MovimentacaoTipoCodigo")); 

		GrupoMovimentacaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GrupoMovimentacaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		switch (paginaatual) {
			case (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Grupo"};
					String[] lisDescricao = {"Grupo", "Código", "Tipo de Serventia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Grupo");
					request.setAttribute("tempBuscaDescricao", "Grupo");
					request.setAttribute("tempBuscaPrograma", "Grupo");
					request.setAttribute("tempRetorno", "GrupoMovimentacaoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (GrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = GrupoMovimentacaoTipone.consultarDescricaoGrupoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("GrupoMovimentacaoTipodt",GrupoMovimentacaoTipodt );
		request.getSession().setAttribute("GrupoMovimentacaoTipone",GrupoMovimentacaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
