package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioDesembargadoresDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioDesembargadoresCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioDesembargadoresDt.CodigoPermissaoDesembargadores;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioDesembargadoresDt relatorioDesembargadoresDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioDesembargadores");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório de Desembargadores");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("relatorioEstatisticaNe");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioDesembargadoresDt = (RelatorioDesembargadoresDt) request.getSession().getAttribute("relatorioDesembargadoresDt");
		if (relatorioDesembargadoresDt == null)
			relatorioDesembargadoresDt = new RelatorioDesembargadoresDt();

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			relatorioDesembargadoresDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			relatorioDesembargadoresDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			relatorioDesembargadoresDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			relatorioDesembargadoresDt.setAnoFinal(request.getParameter("AnoFinal"));
		}

		if (request.getParameter("IdServentia") != null) {
			if (request.getParameter("IdServentia").equals("null")) {
				relatorioDesembargadoresDt.setIdServentia("");
			} else {
				relatorioDesembargadoresDt.setIdServentia(request.getParameter("IdServentia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioDesembargadoresDt.setServentia("");
			} else {
				relatorioDesembargadoresDt.setServentia(request.getParameter("Serventia"));
			}
		}
		if (request.getParameter("TipoRelatorio") != null) {
			if (request.getParameter("TipoRelatorio").equals("null")) {
				relatorioDesembargadoresDt.setTipoRelatorio("");
			} else {
				relatorioDesembargadoresDt.setTipoRelatorio(request.getParameter("TipoRelatorio"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:

			mensagemRetorno = "";
			
			Calendar dataInicialTemp = Calendar.getInstance();
			dataInicialTemp.set(Calendar.DAY_OF_MONTH, 1);
			dataInicialTemp.set(Calendar.MONTH, Funcoes.StringToInt(relatorioDesembargadoresDt.getMesInicial())-1);
			dataInicialTemp.set(Calendar.YEAR, Funcoes.StringToInt(relatorioDesembargadoresDt.getAnoInicial()));
			
			Date dataInicial = dataInicialTemp.getTime();
			Date dataFinal = Funcoes.getUltimoDiaMes(relatorioDesembargadoresDt.getMesFinal(), relatorioDesembargadoresDt.getAnoFinal());
			
			long[] diferencaDias = Funcoes.diferencaDatas(dataFinal, dataInicial);
			if (diferencaDias[0] < 0) {
				mensagemRetorno = "Mês/Ano Inicial não pode ser maior que Mês/Ano Final. ";
			} else if (diferencaDias[0] > 91) {
				mensagemRetorno = "Período entre Mês/Ano Inicial e Mês/Ano Final não pode superar 03(três) meses. ";
			}
			
			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.imprimirRelatorioConclusoesSegundoGrau(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioDesembargadoresDt.getAnoInicial(), relatorioDesembargadoresDt.getMesInicial(), relatorioDesembargadoresDt.getAnoFinal(), relatorioDesembargadoresDt.getMesFinal(), dataInicial, dataFinal, relatorioDesembargadoresDt.getIdServentia(), relatorioDesembargadoresDt.getServentia(), relatorioDesembargadoresDt.getTipoRelatorio(), UsuarioSessao.getUsuarioDt().getNome());

				String nome="RelatorioDesembargadores";				
				
				enviarPDF(response, byTemp, nome);
				return;

			} else {
				if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioDesembargadoresDt.limpar();

			if (relatorioDesembargadoresDt.getMesInicial().equals("") && relatorioDesembargadoresDt.getAnoInicial().equals("") && relatorioDesembargadoresDt.getMesFinal().equals("") && relatorioDesembargadoresDt.getAnoFinal().equals("")) {
				relatorioDesembargadoresDt = this.atribuirDataAtual(relatorioDesembargadoresDt);
			}

			relatorioDesembargadoresDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
			relatorioDesembargadoresDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());

			stAcao = "WEB-INF/jsptjgo/RelatorioDesembargadores.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}
		
		request.getSession().setAttribute("relatorioDesembargadoresDt", relatorioDesembargadoresDt);
		request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioDesembargadoresDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioDesembargadoresDt atribuirDataAtual(RelatorioDesembargadoresDt relatorioDesembargadoresDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioDesembargadoresDt.setMesInicial(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioDesembargadoresDt.setAnoInicial(String.valueOf(dataAtual.get(Calendar.YEAR)));
		relatorioDesembargadoresDt.setMesFinal(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioDesembargadoresDt.setAnoFinal(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioDesembargadoresDt;
	}
}
