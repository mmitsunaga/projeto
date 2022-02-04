package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaDRSDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.AudienciaDRSNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * Servlet respons�vel em controlar o vinculo de audi�ncias do DRS de outros processos no processo atual 
 * @author mmgomes
 */
public class AudienciaDRSCt extends Controle {

	private static final long serialVersionUID = -1202448373828988587L;

	public int Permissao() {
		return AudienciaDRSDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		ProcessoDt processoDt;
		AudienciaDRSNe audienciaDRSNe;
		AudienciaDRSDt audienciaDRSDt;
		String mensagem = "";

		String stAcao = "/WEB-INF/jsptjgo/AudienciaDRS.jsp";

		request.setAttribute("tempRetorno", "AudienciaDRS");
		request.setAttribute("tempPrograma", "Processo");

		audienciaDRSNe = (AudienciaDRSNe) request.getSession().getAttribute("audienciaDRSne");
		if (audienciaDRSNe == null) audienciaDRSNe = new AudienciaDRSNe();
		
		audienciaDRSDt = (AudienciaDRSDt) request.getSession().getAttribute("audienciaDRSDt");
		if (audienciaDRSDt == null) audienciaDRSDt = new AudienciaDRSDt();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		//se n�o houver um processo na sess�o ocorreu algum problema assim retorno para pagina inicial
		if (processoDt==null){
			redireciona(response, "Usuario?PaginaAtual=-10");
			return;			
		}

		audienciaDRSDt.setProcessoNumero(request.getParameter("ProcessoNumeroAudiencia"));
		audienciaDRSDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
		audienciaDRSDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		//audienciaDRSDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		//UsuarioSessao.getUsuarioDt().setId_UsuarioServentia(UsuarioDt.SistemaProjudi);
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		
			case Configuracao.Novo:				
				audienciaDRSDt = new AudienciaDRSDt();				
				break;

			case Configuracao.Salvar:
				if (request.getParameter("DataAudiencia") == null || request.getParameter("DataAudiencia").trim().length() == 0) {
					mensagem += "Data da audi�ncia deve ser informada.";
				} else if (!Funcoes.validaData(request.getParameter("DataAudiencia"))) {
					mensagem += "Data da audi�ncia inv�lida.";
				}
				
				if (request.getParameter("HoraAudiencia") == null || request.getParameter("HoraAudiencia").trim().length() == 0) {
					mensagem += "Hora da audi�ncia deve ser informada.";
				} else if (!Funcoes.validaHora(request.getParameter("HoraAudiencia"))) {
					mensagem += "Hora da audi�ncia inv�lida.";
				}
				
				if (mensagem.length() == 0) {
					TJDataHora dataHoraInformada = new TJDataHora(request.getParameter("DataAudiencia"), request.getParameter("HoraAudiencia") + ":00");
					audienciaDRSDt.setDataHoraDaAudiencia(dataHoraInformada);
					super.exibaMensagemConfirmacao(request, "Confirma a vincula��o da audi�ncia do DRS informada");
					request.setAttribute("ocultarSalvar", "true");	
				} else {
					super.exibaMensagemInconsistenciaErro(request, mensagem);
				}			
					
				break;

			case Configuracao.SalvarResultado:				
				mensagem = audienciaDRSNe.Verificar(audienciaDRSDt, processoDt);
				if (mensagem.length() == 0) {	
					audienciaDRSNe.salvar(audienciaDRSDt, processoDt, UsuarioSessao.getUsuarioDt());
					audienciaDRSDt.limpar();
					audienciaDRSDt.LimparDataHora();
					super.exibaMensagemSucesso(request, "Vincula��o realizada com sucesso.");
				} else {
					super.exibaMensagemInconsistenciaErro(request, mensagem);
				}				
				break;
		}

		request.getSession().setAttribute("audienciaDRSDt", audienciaDRSDt);
		request.getSession().setAttribute("audienciaDRSne", audienciaDRSNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}


}