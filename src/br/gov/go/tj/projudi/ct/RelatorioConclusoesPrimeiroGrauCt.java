package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesPrimeiroGrauDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioConclusoesPrimeiroGrauCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioConclusoesPrimeiroGrauDt.CodigoPermissaoConclusoesPrimeiroGrau;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioConclusoesPrimeiroGrauDt relatorioConclusoesPrimeiroGrauDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioConclusoesPrimeiroGrauArea");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório de Conclusões do 1º Grau");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("relatorioEstatisticaNe");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioConclusoesPrimeiroGrauDt = (RelatorioConclusoesPrimeiroGrauDt) request.getSession().getAttribute("relatorioConclusoesPrimeiroGrauDt");
		if (relatorioConclusoesPrimeiroGrauDt == null)
			relatorioConclusoesPrimeiroGrauDt = new RelatorioConclusoesPrimeiroGrauDt();

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			relatorioConclusoesPrimeiroGrauDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			relatorioConclusoesPrimeiroGrauDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			relatorioConclusoesPrimeiroGrauDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			relatorioConclusoesPrimeiroGrauDt.setAnoFinal(request.getParameter("AnoFinal"));
		}

		if (request.getParameter("IdServentia") != null) {
			if (request.getParameter("IdServentia").equals("null")) {
				relatorioConclusoesPrimeiroGrauDt.setIdServentia("");
			} else {
				relatorioConclusoesPrimeiroGrauDt.setIdServentia(request.getParameter("IdServentia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioConclusoesPrimeiroGrauDt.setServentia("");
			} else {
				relatorioConclusoesPrimeiroGrauDt.setServentia(request.getParameter("Serventia"));
			}
		}
		if (request.getParameter("TipoRelatorio") != null) {
			if (request.getParameter("TipoRelatorio").equals("null")) {
				relatorioConclusoesPrimeiroGrauDt.setTipoRelatorio("");
			} else {
				relatorioConclusoesPrimeiroGrauDt.setTipoRelatorio(request.getParameter("TipoRelatorio"));
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
			dataInicialTemp.set(Calendar.MONTH, Funcoes.StringToInt(relatorioConclusoesPrimeiroGrauDt.getMesInicial())-1);
			dataInicialTemp.set(Calendar.YEAR, Funcoes.StringToInt(relatorioConclusoesPrimeiroGrauDt.getAnoInicial()));
			
			Date dataInicial = dataInicialTemp.getTime();
			Date dataFinal = Funcoes.getUltimoDiaMes(relatorioConclusoesPrimeiroGrauDt.getMesFinal(), relatorioConclusoesPrimeiroGrauDt.getAnoFinal());
			
			long[] diferencaDias = Funcoes.diferencaDatas(dataFinal, dataInicial);
			if (diferencaDias[0] < 0) {
				mensagemRetorno = "Mês/Ano Inicial não pode ser maior que Mês/Ano Final. ";
			} else if (diferencaDias[0] > 91) {
				mensagemRetorno = "Período entre Mês/Ano Inicial e Mês/Ano Final não pode superar 03(três) meses. ";
			}
			
			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.imprimirRelatorioConclusoesPrimeiroGrau(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioConclusoesPrimeiroGrauDt.getAnoInicial(), relatorioConclusoesPrimeiroGrauDt.getMesInicial(), relatorioConclusoesPrimeiroGrauDt.getAnoFinal(), relatorioConclusoesPrimeiroGrauDt.getMesFinal(), dataInicial, dataFinal, relatorioConclusoesPrimeiroGrauDt.getIdServentia(), relatorioConclusoesPrimeiroGrauDt.getServentia(), relatorioConclusoesPrimeiroGrauDt.getTipoRelatorio(), UsuarioSessao.getUsuarioDt().getNome());

				String nome="RelatorioConclusoesPrimeiroGrau";								
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
			relatorioConclusoesPrimeiroGrauDt.limpar();

			if (relatorioConclusoesPrimeiroGrauDt.getMesInicial().equals("") && relatorioConclusoesPrimeiroGrauDt.getAnoInicial().equals("") && relatorioConclusoesPrimeiroGrauDt.getMesFinal().equals("") && relatorioConclusoesPrimeiroGrauDt.getAnoFinal().equals("")) {
				relatorioConclusoesPrimeiroGrauDt = this.atribuirDataAtual(relatorioConclusoesPrimeiroGrauDt);
			}

			relatorioConclusoesPrimeiroGrauDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
			relatorioConclusoesPrimeiroGrauDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());

			stAcao = "WEB-INF/jsptjgo/RelatorioConclusoesPrimeiroGrau.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}
		
		request.getSession().setAttribute("relatorioConclusoesPrimeiroGrauDt", relatorioConclusoesPrimeiroGrauDt);
		request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioConclusoesPrimeiroGrauDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	private RelatorioConclusoesPrimeiroGrauDt atribuirDataAtual(RelatorioConclusoesPrimeiroGrauDt relatorioConclusoesPrimeiroGrauDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioConclusoesPrimeiroGrauDt.setMesInicial(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioConclusoesPrimeiroGrauDt.setAnoInicial(String.valueOf(dataAtual.get(Calendar.YEAR)));
		relatorioConclusoesPrimeiroGrauDt.setMesFinal(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioConclusoesPrimeiroGrauDt.setAnoFinal(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioConclusoesPrimeiroGrauDt;
	}
}
