package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.GuiaNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet responsável em controlar o Apensamento/Desapensamento de Processos
 * @author msapaula
 */
public class RecebimentoGuiaCt extends Controle {

	private static final long serialVersionUID = -1202448373828988587L;

	public RecebimentoGuiaCt() {
	}

	public int Permissao() {
		return 256;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		String stMensagem = "";
		String stAcao = "/WEB-INF/jsptjgo/RecebimentoGuia.jsp";
		String numeroProcesso = "";
		String numeroGuia = "";

		request.setAttribute("tempRetorno", "RecebimentoGuia");
		request.setAttribute("tempPrograma", "Recebimento de Guia");

		if (request.getParameter("ProcessoNumero") != null)
			numeroProcesso = request.getParameter("ProcessoNumero");
		if (request.getParameter("NumeroGuia") != null)
			numeroGuia = request.getParameter("NumeroGuia");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

		case Configuracao.Salvar:
			request.setAttribute("Mensagem", "Clique para confirmar o Recebimento da Guia");
			break;

		case Configuracao.SalvarResultado:
			if (numeroProcesso == null || numeroProcesso.length() == 0) {
				stMensagem = "Informe o Número do Processo. \n";
			}
			if (!Funcoes.validaProcessoNumero(numeroProcesso)) {
				stMensagem = "Número do Processo Completo inválido. \n";
			}
			if (numeroGuia == null || numeroGuia.length() == 0) {
				stMensagem += "Informe o Número da Guia. ";
			}
			if (stMensagem.length() == 0) {
				String matricula = UsuarioSessao.getUsuarioDt().getMatriculaTjGo();
				String serventiaCodigo = UsuarioSessao.getUsuarioDt().getServentiaCodigo();
				String serventiaCodigoExterno = new ServentiaNe().consultarCodigoExterno(serventiaCodigo);

				if (serventiaCodigoExterno == null || serventiaCodigoExterno.trim().equalsIgnoreCase("")) {
					request.setAttribute("MensagemErro", "Não foi possível resgatar código da Serventia. Contate o suporte e solicite que seja verificado o cadastro da Serventia.");
				}

				GuiaNe guiaNe = new GuiaNe();
				stMensagem = guiaNe.informarRecebimentoGuiaSPG(numeroProcesso, numeroGuia, matricula, serventiaCodigoExterno);
				String[] mensagem = stMensagem.split(";");
				if (mensagem[0].contains("SUCESSO")) {
					request.setAttribute("MensagemOk", "GUIA CADASTRADA COM SUCESSO.");
				} else if (mensagem[0].contains("ERRO")) {
					request.setAttribute("MensagemErro", "GUIA NÃO CADASTRADA. \n" + mensagem[1]);
				} else {
					request.setAttribute("MensagemErro", "Um erro inesperado impediu o recolhimento da guia. Favor entrar em contato com o suporte. \n" + mensagem[0] + mensagem[1]);
				}
			} else {
				request.setAttribute("MensagemErro", stMensagem);
			}
			break;
		}
		request.setAttribute("ProcessoNumero", numeroProcesso);
		request.setAttribute("NumeroGuia", numeroGuia);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
