package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosComarcaDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioSumarioProcessosComarcaCt extends Controle {

	private static final long serialVersionUID = 5684858204449066163L;

	public int Permissao() {
		return RelatorioSumarioProcessosComarcaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioSumarioProcessosComarcaDt relatorioSumarioProcessosComarcaDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioSumarioProcessosComarca");
		request.setAttribute("tempBuscaSistema", "Sistema");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioSumarioProcessosComarcaDt = (RelatorioSumarioProcessosComarcaDt) request.getSession().getAttribute("RelatorioSumarioProcessosComarcadt");
		if (relatorioSumarioProcessosComarcaDt == null)
			relatorioSumarioProcessosComarcaDt = new RelatorioSumarioProcessosComarcaDt();

		if (request.getParameter("LimiteConsulta") != null) {
			if (request.getParameter("LimiteConsulta").equals("null")) {
				relatorioSumarioProcessosComarcaDt.setLimiteConsulta("");
			} else {
				relatorioSumarioProcessosComarcaDt.setLimiteConsulta(request.getParameter("LimiteConsulta"));
			}
		}

		if (request.getParameter("TipoProcesso") != null) {
			if (request.getParameter("TipoProcesso").equals("null")) {
				relatorioSumarioProcessosComarcaDt.setTipoProcesso("");
			} else {
				relatorioSumarioProcessosComarcaDt.setTipoProcesso(request.getParameter("TipoProcesso"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Relatório de Processos por Comarca");

		switch (paginaatual) {

		case Configuracao.Imprimir:
			byTemp = relatorioEstatisticaNe.relSumarioMaisProcessosComarca(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioSumarioProcessosComarcaDt.getTipoProcesso(), relatorioSumarioProcessosComarcaDt.getLimiteConsulta(), UsuarioSessao.getUsuarioDt().getNome());			
			
			enviarPDF(response, byTemp,"Relatorio");
			
			return;

		case Configuracao.Novo:
			relatorioSumarioProcessosComarcaDt.limparCamposConsulta();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;

		default:
			break;
		}

		request.getSession().setAttribute("RelatorioSumarioProcessosComarcadt", relatorioSumarioProcessosComarcaDt);
		request.getSession().setAttribute("RelatorioEstatisticane", relatorioEstatisticaNe);

		stAcao = "WEB-INF/jsptjgo/RelatorioSumarioProcessosComarca.jsp";
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
