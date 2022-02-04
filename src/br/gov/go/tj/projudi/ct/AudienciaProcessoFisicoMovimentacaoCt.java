package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaFisicoMovimentacaoDt;
import br.gov.go.tj.projudi.ne.AudienciaFisicoNe;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaProcessoFisicoMovimentacaoCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6144704803084334252L;
	
	public int Permissao() {
		return AudienciaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		AudienciaFisicoMovimentacaoDt audienciaMovimentacaoDt;
		AudienciaFisicoNe audienciaNe;

		String Mensagem = "";
		String stId = "";
		int paginaAnterior = 0;
		String stAcao = "/WEB-INF/jsptjgo/AudienciaProcessoFisicoMovimentacao.jsp";
		
		request.setAttribute("tempPrograma", "Movimentação de Audiência");
		request.setAttribute("tempRetorno", "AudienciaProcessoFisicoMovimentacao");
		
		audienciaNe = (AudienciaFisicoNe) request.getSession().getAttribute("AudienciaFisicone");
		if (audienciaNe == null) audienciaNe = new AudienciaFisicoNe();

		audienciaMovimentacaoDt = (AudienciaFisicoMovimentacaoDt) request.getSession().getAttribute("AudienciaFisicoMovimentacaoDt");
		if (audienciaMovimentacaoDt == null) audienciaMovimentacaoDt = new AudienciaFisicoMovimentacaoDt();

		// Variáveis auxiliares
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));	
		
		audienciaMovimentacaoDt.setAudienciaStatusCodigo(request.getParameter("AudienciaStatusCodigo"));
		audienciaMovimentacaoDt.setAudienciaStatus(request.getParameter("AudienciaStatus"));
		audienciaMovimentacaoDt.getAudienciaProcessoFisicoDt().setAcordo(request.getParameter("Acordo"));
		audienciaMovimentacaoDt.getAudienciaProcessoFisicoDt().setValorAcordo(request.getParameter("ValorAcordo"));
		audienciaMovimentacaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		audienciaMovimentacaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		
		
		setParametrosAuxiliares(audienciaMovimentacaoDt, paginaAnterior, paginaatual, request, audienciaNe, UsuarioSessao);

		// -----------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			// Inicializa movimentação
			case Configuracao.Novo:
				audienciaMovimentacaoDt = new AudienciaFisicoMovimentacaoDt();
				audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao(request.getParameter("TipoAudienciaProcessoMovimentacao"));	
				//Captura o menu acionado para permitir retorno para consulta correta
				audienciaMovimentacaoDt.setfluxo(request.getParameter("fluxo"));

				stId = request.getParameter("Id_AudienciaProcesso");
				if (stId != null && !stId.equals("")) {
					AudienciaDt audienciaDt = audienciaNe.consultarAudienciaProcessoCompleta(stId);
					
					// Seta a audiência
					audienciaMovimentacaoDt.setAudienciaDt(audienciaDt);
					
					// Seta status de audiência possíveis
					audienciaMovimentacaoDt.setListaAudienciaProcessoStatus(audienciaNe.consultarStatusAudiencia(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));										
				} 
				break;			

			// Salva o status da audiência
			case Configuracao.SalvarResultado:
				Mensagem = audienciaNe.verificarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() == 0) {
					audienciaNe.salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());					
					redireciona(response, "Usuario?PaginaAtual=" + Configuracao.Cancelar + "&MensagemOk=Movimentação de Audiência efetuada com sucesso.");
					audienciaMovimentacaoDt.limpar();
					return;						 
				} 
				
				if (Mensagem.length() > 0) request.setAttribute("MensagemErro", Mensagem);
				
				break;
		}	
		
		request.setAttribute("TituloPagina", "Concluir Audiência");				
		request.getSession().setAttribute("AudienciaFisicoMovimentacaoDt", audienciaMovimentacaoDt);
		request.getSession().setAttribute("AudienciaFisicone", audienciaNe);		

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Tratamentos necessários ao realizar uma movimentação
	 * @throws Exception 
	 */
	private void setParametrosAuxiliares(AudienciaMovimentacaoDt audienciaMovimentacaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, AudienciaNe audienciaNe, UsuarioNe usuarioNe) throws Exception{
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getTextoEditor());
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");		
	}	
}
