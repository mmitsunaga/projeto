package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosServentiaDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioSumarioProcessosServentiaCt extends Controle {

	private static final long serialVersionUID = -438380512473505014L;

	public int Permissao() {
		return RelatorioSumarioProcessosServentiaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioSumarioProcessosServentiaDt relatorioSumarioProcessosServentiaDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioSumarioProcessosServentia");
		request.setAttribute("tempBuscaSistema", "Sistema");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioSumarioProcessosServentiaDt = (RelatorioSumarioProcessosServentiaDt) request.getSession().getAttribute("RelatorioSumarioProcessosServentiadt");
		if (relatorioSumarioProcessosServentiaDt == null)
			relatorioSumarioProcessosServentiaDt = new RelatorioSumarioProcessosServentiaDt();

		if (request.getParameter("LimiteConsulta") != null) {
			if (request.getParameter("LimiteConsulta").equals("null")) {
				relatorioSumarioProcessosServentiaDt.setLimiteConsulta("");
			} else {
				relatorioSumarioProcessosServentiaDt.setLimiteConsulta(request.getParameter("LimiteConsulta"));
			}
		}

		if (request.getParameter("TipoProcesso") != null) {
			if (request.getParameter("TipoProcesso").equals("null")) {
				relatorioSumarioProcessosServentiaDt.setTipoProcesso("");
			} else {
				relatorioSumarioProcessosServentiaDt.setTipoProcesso(request.getParameter("TipoProcesso"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Relatório de Processos por Serventia");

		switch (paginaatual) {

		case Configuracao.Imprimir:
			byTemp = relatorioEstatisticaNe.relSumarioMaisProcessosServentia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioSumarioProcessosServentiaDt.getTipoProcesso(), relatorioSumarioProcessosServentiaDt.getLimiteConsulta(), UsuarioSessao.getUsuarioDt().getNome());						
			enviarPDF(response, byTemp,"Relatorio");				
			return;

		case Configuracao.Novo:
			relatorioSumarioProcessosServentiaDt.limparCamposConsulta();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;

		default:
			break;
		}

		request.getSession().setAttribute("RelatorioSumarioProcessosServentiadt", relatorioSumarioProcessosServentiaDt);
		request.getSession().setAttribute("RelatorioEstatisticane", relatorioEstatisticaNe);

		stAcao = "WEB-INF/jsptjgo/RelatorioSumarioProcessosServentia.jsp";
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
