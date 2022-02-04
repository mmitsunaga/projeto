package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesSegundoGrauDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioConclusoesSegundoGrauCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioConclusoesSegundoGrauDt.CodigoPermissaoConclusoesSegundoGrau;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioConclusoesSegundoGrauDt relatorioConclusoesSegundoGrauDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioConclusoesSegundoGrauArea");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório de Conclusões/Sessões do 2º Grau");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("relatorioEstatisticaNe");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioConclusoesSegundoGrauDt = (RelatorioConclusoesSegundoGrauDt) request.getSession().getAttribute("relatorioConclusoesSegundoGrauDt");
		if (relatorioConclusoesSegundoGrauDt == null)
			relatorioConclusoesSegundoGrauDt = new RelatorioConclusoesSegundoGrauDt();

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			relatorioConclusoesSegundoGrauDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			relatorioConclusoesSegundoGrauDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			relatorioConclusoesSegundoGrauDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			relatorioConclusoesSegundoGrauDt.setAnoFinal(request.getParameter("AnoFinal"));
		}

		if (request.getParameter("IdServentia") != null) {
			if (request.getParameter("IdServentia").equals("null")) {
				relatorioConclusoesSegundoGrauDt.setIdServentia("");
			} else {
				relatorioConclusoesSegundoGrauDt.setIdServentia(request.getParameter("IdServentia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioConclusoesSegundoGrauDt.setServentia("");
			} else {
				relatorioConclusoesSegundoGrauDt.setServentia(request.getParameter("Serventia"));
			}
		}
		if (request.getParameter("TipoRelatorio") != null) {
			if (request.getParameter("TipoRelatorio").equals("null")) {
				relatorioConclusoesSegundoGrauDt.setTipoRelatorio("");
			} else {
				relatorioConclusoesSegundoGrauDt.setTipoRelatorio(request.getParameter("TipoRelatorio"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:

			mensagemRetorno = "";
			
			Date dataInicial = Funcoes.getPrimeiroDiaMes(Funcoes.StringToInt(relatorioConclusoesSegundoGrauDt.getMesInicial()), Funcoes.StringToInt(relatorioConclusoesSegundoGrauDt.getAnoInicial()));
			
			Date dataFinal = Funcoes.getUltimoDiaHoraMinutoSegundoMes(relatorioConclusoesSegundoGrauDt.getMesFinal(), relatorioConclusoesSegundoGrauDt.getAnoFinal());
			
			long[] diferencaDias = Funcoes.diferencaDatas(dataFinal, dataInicial);
			if (diferencaDias[0] < 0) {
				mensagemRetorno = "Mês/Ano Inicial não pode ser maior que Mês/Ano Final. ";
			} else if (diferencaDias[0] > 91) {
				mensagemRetorno = "Período entre Mês/Ano Inicial e Mês/Ano Final não pode superar 03(três) meses. ";
			}
			
			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.imprimirRelatorioConclusoesSegundoGrau(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioConclusoesSegundoGrauDt.getAnoInicial(), relatorioConclusoesSegundoGrauDt.getMesInicial(), relatorioConclusoesSegundoGrauDt.getAnoFinal(), relatorioConclusoesSegundoGrauDt.getMesFinal(), dataInicial, dataFinal, relatorioConclusoesSegundoGrauDt.getIdServentia(), relatorioConclusoesSegundoGrauDt.getServentia(), relatorioConclusoesSegundoGrauDt.getTipoRelatorio(), UsuarioSessao.getUsuarioDt().getNome());

				String nome="RelatorioConclusoesSegundoGrau";				
				
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
			relatorioConclusoesSegundoGrauDt.limpar();

			if (relatorioConclusoesSegundoGrauDt.getMesInicial().equals("") && relatorioConclusoesSegundoGrauDt.getAnoInicial().equals("") && relatorioConclusoesSegundoGrauDt.getMesFinal().equals("") && relatorioConclusoesSegundoGrauDt.getAnoFinal().equals("")) {
				relatorioConclusoesSegundoGrauDt = this.atribuirDataAtual(relatorioConclusoesSegundoGrauDt);
			}

			relatorioConclusoesSegundoGrauDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
			relatorioConclusoesSegundoGrauDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());

			stAcao = "WEB-INF/jsptjgo/RelatorioConclusoesSegundoGrau.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}
		
		request.getSession().setAttribute("relatorioConclusoesSegundoGrauDt", relatorioConclusoesSegundoGrauDt);
		request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioConclusoesSegundoGrauDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioConclusoesSegundoGrauDt atribuirDataAtual(RelatorioConclusoesSegundoGrauDt relatorioConclusoesSegundoGrauDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioConclusoesSegundoGrauDt.setMesInicial(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioConclusoesSegundoGrauDt.setAnoInicial(String.valueOf(dataAtual.get(Calendar.YEAR)));
		relatorioConclusoesSegundoGrauDt.setMesFinal(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioConclusoesSegundoGrauDt.setAnoFinal(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioConclusoesSegundoGrauDt;
	}
}
