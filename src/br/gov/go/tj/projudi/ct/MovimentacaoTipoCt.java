package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MovimentacaoTipoCt extends MovimentacaoTipoCtGen {

    private static final long serialVersionUID = -8315546126649445612L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoTipoDt MovimentacaoTipodt;
		MovimentacaoTipoNe MovimentacaoTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/MovimentacaoTipo.jsp";		
		
		request.setAttribute("tempRetorno","MovimentacaoTipo");
		request.setAttribute("tempPrograma", "Tipo de Movimentação");
		request.setAttribute("tempBuscaId_MovimentacaoTipo", "Id_MovimentacaoTipo");
		request.setAttribute("tempBuscaMovimentacaoTipo", "MovimentacaoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		MovimentacaoTipone = (MovimentacaoTipoNe) request.getSession().getAttribute("MovimentacaoTipone");
		if (MovimentacaoTipone == null) MovimentacaoTipone = new MovimentacaoTipoNe();

		MovimentacaoTipodt = (MovimentacaoTipoDt) request.getSession().getAttribute("MovimentacaoTipodt");
		if (MovimentacaoTipodt == null) MovimentacaoTipodt = new MovimentacaoTipoDt();
		
		MovimentacaoTipodt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		MovimentacaoTipodt.setId_CNJ(request.getParameter("Id_CNJ"));
		MovimentacaoTipodt.setCodigoTemp(request.getParameter("CodigoTemp"));
		MovimentacaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Tipo de Movimentação"};
					String[] lisDescricao = {"Tipo de Movimentação"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao", "MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma", "Tipo de Movimentação");
					request.setAttribute("tempRetorno", "MovimentacaoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = MovimentacaoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

	}

		request.getSession().setAttribute("MovimentacaoTipodt", MovimentacaoTipodt);
		request.getSession().setAttribute("MovimentacaoTipone", MovimentacaoTipone);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
