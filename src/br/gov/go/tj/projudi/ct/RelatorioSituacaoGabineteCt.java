package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSituacaoGabineteDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioSituacaoGabineteCt extends Controle {

	private static final long serialVersionUID = -5519944631247843291L;

	public int Permissao() {
		return RelatorioSituacaoGabineteDt.CodigoPermissaoSituacaoGabinete;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioSituacaoGabinete");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório da Situação do Gabinete");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("relatorioEstatisticaNe");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioSituacaoGabineteDt = (RelatorioSituacaoGabineteDt) request.getSession().getAttribute("relatorioSituacaoGabineteDt");
		if (relatorioSituacaoGabineteDt == null)
			relatorioSituacaoGabineteDt = new RelatorioSituacaoGabineteDt();

		if (request.getParameter("MesInicial") != null && !request.getParameter("MesInicial").equals("")) {
			relatorioSituacaoGabineteDt.setMesInicial(request.getParameter("MesInicial"));
		}
		if (request.getParameter("AnoInicial") != null && !request.getParameter("AnoInicial").equals("")) {
			relatorioSituacaoGabineteDt.setAnoInicial(request.getParameter("AnoInicial"));
		}
		if (request.getParameter("MesFinal") != null && !request.getParameter("MesFinal").equals("")) {
			relatorioSituacaoGabineteDt.setMesFinal(request.getParameter("MesFinal"));
		}
		if (request.getParameter("AnoFinal") != null && !request.getParameter("AnoFinal").equals("")) {
			relatorioSituacaoGabineteDt.setAnoFinal(request.getParameter("AnoFinal"));
		}

		if (request.getParameter("IdServentia") != null) {
			if (request.getParameter("IdServentia").equals("null")) {
				relatorioSituacaoGabineteDt.setIdServentia("");
			} else {
				relatorioSituacaoGabineteDt.setIdServentia(request.getParameter("IdServentia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioSituacaoGabineteDt.setServentia("");
			} else {
				relatorioSituacaoGabineteDt.setServentia(request.getParameter("Serventia"));
			}
		}
//		if (request.getParameter("TipoRelatorio") != null) {
//			if (request.getParameter("TipoRelatorio").equals("null")) {
//				relatorioSituacaoGabineteDt.setTipoRelatorio("");
//			} else {
//				relatorioSituacaoGabineteDt.setTipoRelatorio(request.getParameter("TipoRelatorio"));
//			}
//		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Localizar:
			mensagemRetorno = "";
			
			Calendar dataInicialTemp = Calendar.getInstance();
			dataInicialTemp.set(Calendar.DAY_OF_MONTH, 1);
			dataInicialTemp.set(Calendar.MONTH, Funcoes.StringToInt(relatorioSituacaoGabineteDt.getMesInicial())-1);
			dataInicialTemp.set(Calendar.YEAR, Funcoes.StringToInt(relatorioSituacaoGabineteDt.getAnoInicial()));
			
			Date dataInicial = dataInicialTemp.getTime();
			Date dataFinal = Funcoes.getUltimoDiaMes(relatorioSituacaoGabineteDt.getMesFinal(), relatorioSituacaoGabineteDt.getAnoFinal());
			
			long[] diferencaDias = Funcoes.diferencaDatas(dataFinal, dataInicial);
			if (diferencaDias[0] < 0) {
				mensagemRetorno = "Mês/Ano Inicial não pode ser maior que Mês/Ano Final. ";
			} else if (diferencaDias[0] > 91) {
				mensagemRetorno = "Período entre Mês/Ano Inicial e Mês/Ano Final não pode superar 03(três) meses. ";
			}
			
			if (mensagemRetorno.equals("")) {
				
				//variável será inicializada com 0, pois será usado para quando a consulta estiver sendo feita pelo botão "Buscar" da tela.
				int tipoConsulta = 0;
				if(request.getParameter("TipoConsulta") != null && !request.getParameter("TipoConsulta").equals("")) {
					tipoConsulta = Funcoes.StringToInt(request.getParameter("TipoConsulta").toString());
				}
				
				
				switch (tipoConsulta) {
				case 0: //consulta inicial
					
					//garantindo que a lista de Movimentação Tipo Classe será sempre limpa
					relatorioSituacaoGabineteDt.setListaMovimentacaoTipoClasse(new ArrayList());
					
					relatorioSituacaoGabineteDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					relatorioSituacaoGabineteDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());	
					
					relatorioSituacaoGabineteDt.setIdUsuarioServentiaResponsavel(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia()); //22885
					relatorioSituacaoGabineteDt.setIdServentiaCargoResponsavel(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo()); //12155
					//Se o usuário for o desembargador do gabinete, o serventia cargo dele será usado para as consultas futuras
					if(UsuarioSessao.getUsuarioDt().getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.DESEMBARGADOR))) {
						relatorioSituacaoGabineteDt.setNomeResponsavel("Desembargador: " + UsuarioSessao.getUsuarioDt().getNome());
					} else if (UsuarioSessao.getUsuarioDt().getCargoTipoCodigo().equals(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE))){
						//Se o usuário for assistente do desembargador, vai usar o serventia cargo do usuário chefe dele
//						relatorioSituacaoGabineteDt.setIdServentiaCargoResponsavel(UsuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
//						relatorioSituacaoGabineteDt.setIdUsuarioServentiaResponsavel(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
						relatorioSituacaoGabineteDt.setNomeResponsavel("Assistente: " + UsuarioSessao.getUsuarioDt().getNome());
						
					}
					
					relatorioSituacaoGabineteDt = relatorioEstatisticaNe.imprimirRelatorioSituacaoGabinete(relatorioSituacaoGabineteDt);
					stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabinete.jsp";
					break;
				
				case 1: //Consulta de processos distribuídos
					relatorioSituacaoGabineteDt = relatorioEstatisticaNe.consultarProcessosDistribuidosGabinete(relatorioSituacaoGabineteDt);
					request.setAttribute("TipoConsulta", tipoConsulta);
					stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabineteListagem.jsp";
					break;
					
				case 2: //Consulta de Votos Proferidos
					break;
				
				case 3: //Consulta de Processos Conclusos Pendentes 
					relatorioSituacaoGabineteDt = relatorioEstatisticaNe.consultarProcessosConclusosPendentesGabinete(relatorioSituacaoGabineteDt);
					request.setAttribute("TipoConsulta", tipoConsulta);
					stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabineteListagem.jsp";					
					break;
					
				case 4: //Consulta de Processos Conclusos Finalizados 
					relatorioSituacaoGabineteDt = relatorioEstatisticaNe.consultarProcessosConclusosFinalizadosGabinete(relatorioSituacaoGabineteDt);
					request.setAttribute("TipoConsulta", tipoConsulta);
					stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabineteListagem.jsp";					
					break;
				
				case 5: //Consulta de Processos para Revisão
					relatorioSituacaoGabineteDt = relatorioEstatisticaNe.consultarProcessosRevisaoGabinete(relatorioSituacaoGabineteDt);
					request.setAttribute("TipoConsulta", tipoConsulta);
					stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabineteListagem.jsp";					
					break;
					
				case 999: //Consulta dos itens Movimentação Tipo Classe
					if(request.getParameter("IdMovimentacaoTipoClasse") != null && !request.getParameter("IdMovimentacaoTipoClasse").equals("")) {
						relatorioSituacaoGabineteDt.setIdMovimentacaoTipoClasse(request.getParameter("IdMovimentacaoTipoClasse").toString());
					}
					if(request.getParameter("MovimentacaoTipoClasse") != null && !request.getParameter("MovimentacaoTipoClasse").equals("")) {
						relatorioSituacaoGabineteDt.setMovimentacaoTipoClasse(request.getParameter("MovimentacaoTipoClasse").toString());
					}
					relatorioSituacaoGabineteDt = relatorioEstatisticaNe.consultarEstatisticaGenericaGabinete(relatorioSituacaoGabineteDt);
					request.setAttribute("TipoConsulta", tipoConsulta);
					stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabineteListagem.jsp";
					break;
				}
				
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.getSession().setAttribute("stAcaoRetorno", stAcao);

			} else {
				if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			
			break;
		
			
		case Configuracao.Novo:
			relatorioSituacaoGabineteDt.limpar();
			if (relatorioSituacaoGabineteDt.getMesInicial().equals("") && relatorioSituacaoGabineteDt.getAnoInicial().equals("") && relatorioSituacaoGabineteDt.getMesFinal().equals("") && relatorioSituacaoGabineteDt.getAnoFinal().equals("")) {
				relatorioSituacaoGabineteDt = this.atribuirDataAtual(relatorioSituacaoGabineteDt);
			}
			
			stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabinete.jsp";
			break;
		case Configuracao.Curinga6:
	        redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
	        break;		
		default:
			
			relatorioSituacaoGabineteDt.limpar();

			if (relatorioSituacaoGabineteDt.getMesInicial().equals("") && relatorioSituacaoGabineteDt.getAnoInicial().equals("") && relatorioSituacaoGabineteDt.getMesFinal().equals("") && relatorioSituacaoGabineteDt.getAnoFinal().equals("")) {
				relatorioSituacaoGabineteDt = this.atribuirDataAtual(relatorioSituacaoGabineteDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioSituacaoGabinete.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}
		
		
		request.getSession().setAttribute("relatorioSituacaoGabineteDt", relatorioSituacaoGabineteDt);
		request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * 
	 * @param relatorioSituacaoGabineteDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioSituacaoGabineteDt atribuirDataAtual(RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioSituacaoGabineteDt.setMesInicial(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioSituacaoGabineteDt.setAnoInicial(String.valueOf(dataAtual.get(Calendar.YEAR)));
		relatorioSituacaoGabineteDt.setMesFinal(String.valueOf(dataAtual.get(Calendar.MONTH) + 1));
		relatorioSituacaoGabineteDt.setAnoFinal(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioSituacaoGabineteDt;
	}
}
