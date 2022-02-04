package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.ne.ArquivoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ArquivoTipoCt extends ArquivoTipoCtGen {

	private static final long serialVersionUID = 7992781024932107339L;
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ArquivoTipoDt ArquivoTipodt;
		ArquivoTipoNe ArquivoTipone;
		String stAcao = "/WEB-INF/jsptjgo/ArquivoTipo.jsp";
		String stNomeBusca1 = "";
		String Mensagem="";
		
		request.setAttribute("tempRetorno", "ArquivoTipo");
		request.setAttribute("tempPrograma", "ArquivoTipo");
		request.setAttribute("tempBuscaId_ArquivoTipo", "Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo", "ArquivoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		ArquivoTipone = (ArquivoTipoNe) request.getSession().getAttribute("ArquivoTipone");
		if (ArquivoTipone == null) ArquivoTipone = new ArquivoTipoNe();
		ArquivoTipodt = (ArquivoTipoDt) request.getSession().getAttribute("ArquivoTipodt");
		if (ArquivoTipodt == null) ArquivoTipodt = new ArquivoTipoDt();
		
		ArquivoTipodt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		ArquivoTipodt.setArquivoTipoCodigo(request.getParameter("ArquivoTipoCodigo"));
		ArquivoTipodt.setCodigoTemp(request.getParameter("CodigoTemp"));
		ArquivoTipodt.setPublico(request.getParameter("Publico") != null ? "true" : "false");		
		ArquivoTipodt.setDje(request.getParameter("Dje") != null ? "true" : "false");				
		ArquivoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ArquivoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
		
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao", "ArquivoTipo");
					request.setAttribute("tempBuscaPrograma", "ArquivoTipo");
					request.setAttribute("tempRetorno", "ArquivoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "ArquivoTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ArquivoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case Configuracao.Salvar:
				Mensagem=ArquivoTipone.Verificar(ArquivoTipodt); 
				if (Mensagem.length()!=0){
					request.setAttribute("MensagemErro", Mensagem );
				}
			    break;

			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		request.getSession().setAttribute("ArquivoTipodt", ArquivoTipodt);
		request.getSession().setAttribute("ArquivoTipone", ArquivoTipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
