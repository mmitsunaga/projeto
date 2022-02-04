package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.AudienciaPublicadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaPublicadaCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5502736676997859887L;
	
	public static final int CodigoPermissao = 915;

	public int Permissao() {
		return AudienciaPublicadaCt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt = null;
		
		String stAcao;
		
		request.setAttribute("tempPrograma", "AudienciaPublicada");
		request.setAttribute("tempRetorno", "AudienciaPublicada");
		request.setAttribute("TituloPagina", "Audiência Publicada");
		stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendencias.jsp";

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");			
		
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
		
		if (request.getParameter("PaginaAnterior") != null)
			Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		request.setAttribute("PaginaAnterior", paginaatual);
		
		switch (paginaatual) {				
			// Sincronizar audiências publicadas
			case Configuracao.Curinga6:				
				if (processoDt != null && processoDt.getId() != null &&  !processoDt.getId().equals("")){					
					AudienciaPublicadaNe audienciaPublicadaNe = new AudienciaPublicadaNe();
					audienciaPublicadaNe.sincronizeAudiencias(processoDt, UsuarioSessao.getUsuarioDt());
					
					String mensagem = "Audiências sincronizadas com sucesso!";
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
					return; 
				}
				break;
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
