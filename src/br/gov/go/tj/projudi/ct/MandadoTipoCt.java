package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.ne.MandadoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MandadoTipoCt extends MandadoTipoCtGen{

    private static final long serialVersionUID = 4131099709751484842L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MandadoTipoDt MandadoTipodt;
		MandadoTipoNe MandadoTipone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//-fim controle de buscas ajax
		
		String stAcao="/WEB-INF/jsptjgo/MandadoTipo.jsp";
		
		request.setAttribute("tempPrograma","MandadoTipo");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		MandadoTipone =(MandadoTipoNe)request.getSession().getAttribute("MandadoTipone");
		if (MandadoTipone == null ) MandadoTipone = new MandadoTipoNe();  

		MandadoTipodt =(MandadoTipoDt)request.getSession().getAttribute("MandadoTipodt");
		if (MandadoTipodt == null ) MandadoTipodt = new MandadoTipoDt();  

		MandadoTipodt.setMandadoTipo( request.getParameter("MandadoTipo")); 
		MandadoTipodt.setMandadoTipoCodigo( request.getParameter("MandadoTipoCodigo")); 
		MandadoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MandadoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MandadoTipo"};
					String[] lisDescricao = {"MandadoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_MandadoTipo");
					request.setAttribute("tempBuscaDescricao", "MandadoTipo");
					request.setAttribute("tempBuscaPrograma", "MandadoTipo");
					request.setAttribute("tempRetorno", "MandadoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = MandadoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

	}

		request.getSession().setAttribute("MandadoTipodt",MandadoTipodt );
		request.getSession().setAttribute("MandadoTipone",MandadoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
