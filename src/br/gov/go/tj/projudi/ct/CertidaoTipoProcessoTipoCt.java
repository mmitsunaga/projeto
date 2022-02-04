package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.projudi.dt.CertidaoTipoProcessoTipoDt;
import br.gov.go.tj.projudi.ne.CertidaoTipoProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class CertidaoTipoProcessoTipoCt extends CertidaoTipoProcessoTipoCtGen {

	private static final long serialVersionUID = 3803672832297348374L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CertidaoTipoProcessoTipoDt CertidaoTipoProcessoTipodt;
		CertidaoTipoProcessoTipoNe CertidaoTipoProcessoTipone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao = "/WEB-INF/jsptjgo/CertidaoTipoProcessoTipo.jsp";

		request.setAttribute("tempPrograma", "CertidaoTipoProcessoTipo");
		request.setAttribute("ListaUlLiCertidaoTipoProcessoTipo", "");

		CertidaoTipoProcessoTipone = (CertidaoTipoProcessoTipoNe) request.getSession().getAttribute("CertidaoTipoProcessoTipone");
		if (CertidaoTipoProcessoTipone == null)	CertidaoTipoProcessoTipone = new CertidaoTipoProcessoTipoNe();

		CertidaoTipoProcessoTipodt = (CertidaoTipoProcessoTipoDt) request.getSession().getAttribute("CertidaoTipoProcessoTipodt");
		if (CertidaoTipoProcessoTipodt == null)	CertidaoTipoProcessoTipodt = new CertidaoTipoProcessoTipoDt();

		CertidaoTipoProcessoTipodt.setId_CertidaoTipo(request.getParameter("Id_CertidaoTipo"));
		CertidaoTipoProcessoTipodt.setCertidaoTipo(request.getParameter("CertidaoTipo"));
		CertidaoTipoProcessoTipodt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		CertidaoTipoProcessoTipodt.setProcessoTipo(request.getParameter("ProcessoTipo"));

		CertidaoTipoProcessoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CertidaoTipoProcessoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case (CertidaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"CertidaoTipo"};
					String[] lisDescricao = {"CertidaoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_CertidaoTipo");
					request.setAttribute("tempBuscaDescricao", "CertidaoTipo");
					request.setAttribute("tempBuscaPrograma", "CertidaoTipo");
					request.setAttribute("tempRetorno", "CertidaoTipoProcessoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (CertidaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = CertidaoTipoProcessoTipone.consultarDescricaoCertidaoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("CertidaoTipoProcessoTipodt", CertidaoTipoProcessoTipodt);
		request.getSession().setAttribute("CertidaoTipoProcessoTipone", CertidaoTipoProcessoTipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
