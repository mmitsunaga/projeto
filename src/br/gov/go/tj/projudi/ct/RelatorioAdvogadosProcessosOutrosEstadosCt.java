package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioAdvogadosProcessosOutrosEstadosDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioAdvogadosProcessosOutrosEstadosCt extends Controle {

	private static final long serialVersionUID = 58752094293520253L;

	public int Permissao() {
		return RelatorioAdvogadosProcessosOutrosEstadosDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioAdvogadosProcessosOutrosEstadosDt relatorioAdvogadosProcessosOutrosEstadosDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioAdvogadosProcessosOutrosEstados");
		request.setAttribute("tempBuscaSistema", "Sistema");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioAdvogadosProcessosOutrosEstadosDt = (RelatorioAdvogadosProcessosOutrosEstadosDt) request.getSession().getAttribute("RelatorioAdvogadosProcessosOutrosEstadosdt");
		if (relatorioAdvogadosProcessosOutrosEstadosDt == null)
			relatorioAdvogadosProcessosOutrosEstadosDt = new RelatorioAdvogadosProcessosOutrosEstadosDt();

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Relatório de Advogados com mais processos que o permitido pela OAB-GO");

		switch (paginaatual) {
		
		case Configuracao.Imprimir:
			
			byTemp = relatorioEstatisticaNe.consultarAdvogadosProcessosOutrosEstados(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome());			

			enviarPDF(response,byTemp,"RelatorioAdvogadosProcessosOutrosEstados");										
			
			return;
			
		default:
			relatorioAdvogadosProcessosOutrosEstadosDt.limparCamposConsulta();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}

		request.getSession().setAttribute("RelatorioAdvogadosProcessosOutrosEstadosdt", relatorioAdvogadosProcessosOutrosEstadosDt);
		request.getSession().setAttribute("RelatorioEstatisticane", relatorioEstatisticaNe);

		stAcao = "WEB-INF/jsptjgo/RelatorioAdvogadosProcessosOutrosEstados.jsp";
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
