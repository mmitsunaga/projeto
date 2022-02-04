package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioMaioresPromoventesPromovidosDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioMaioresPromoventesPromovidosCt extends Controle {

	private static final long serialVersionUID = 58752094293520253L;

	public int Permissao() {
		return RelatorioMaioresPromoventesPromovidosDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioMaioresPromoventesPromovidosDt relatorioMaioresPromoventesPromovidosDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioMaioresPromoventesPromovidos");
		request.setAttribute("tempBuscaSistema", "Sistema");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioMaioresPromoventesPromovidosDt = (RelatorioMaioresPromoventesPromovidosDt) request.getSession().getAttribute("RelatorioMaioresPromoventesPromovidosdt");
		if (relatorioMaioresPromoventesPromovidosDt == null)
			relatorioMaioresPromoventesPromovidosDt = new RelatorioMaioresPromoventesPromovidosDt();

		if (request.getParameter("LimiteConsulta") != null) {
			if (request.getParameter("LimiteConsulta").equals("null")) {
				relatorioMaioresPromoventesPromovidosDt.setLimiteConsulta("");
			} else {
				relatorioMaioresPromoventesPromovidosDt.setLimiteConsulta(request.getParameter("LimiteConsulta"));
			}
		}

		if (request.getParameter("TipoParte") != null) {
			if (request.getParameter("TipoParte").equals("null")) {
				relatorioMaioresPromoventesPromovidosDt.setTipoParte("");
			} else {
				relatorioMaioresPromoventesPromovidosDt.setTipoParte(request.getParameter("TipoParte"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Relatório de Maiores Promoventes/Promovidos");

		switch (paginaatual) {
		
		case Configuracao.Imprimir:
			String tipoArquivo = request.getParameter("tipo_Arquivo");
			byTemp = relatorioEstatisticaNe.relMaioresPromoventesPromovidos(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioMaioresPromoventesPromovidosDt.getTipoParte(), relatorioMaioresPromoventesPromovidosDt.getLimiteConsulta(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());

			// Se o parâmertro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um
			// arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
			if (tipoArquivo != null && tipoArquivo.equals("2")) {				
				enviarTXT(response, byTemp,"RelatorioMaioresLitigantes");
			} else {			
				enviarPDF(response, byTemp,"RelatorioMaioresLitigantes");
			}

			return;
			
		
		case Configuracao.Novo:
			relatorioMaioresPromoventesPromovidosDt.limparCamposConsulta();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		
		default:
			break;
		}

		request.getSession().setAttribute("RelatorioMaioresPromoventesPromovidosdt", relatorioMaioresPromoventesPromovidosDt);
		request.getSession().setAttribute("RelatorioEstatisticane", relatorioEstatisticaNe);

		stAcao = "WEB-INF/jsptjgo/RelatorioMaioresPromoventesPromovidos.jsp";
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
