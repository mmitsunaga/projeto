package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioRecursoRepetitivoDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioRecursoRepetitivoCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioRecursoRepetitivoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,
			int paginaatual, String tempNomeBusca, String posicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		RelatorioRecursoRepetitivoDt relatorioRecursoRepetitivoDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempBuscaSistema", "Sistema");

		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");
		if (request.getParameter("nomeBusca2") != null)
			stNomeBusca2 = request.getParameter("nomeBusca2");
		if (request.getParameter("nomeBusca3") != null)
			stNomeBusca3 = request.getParameter("nomeBusca3");
		if (request.getParameter("nomeBusca4") != null)
			stNomeBusca4 = request.getParameter("nomeBusca4");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioRecursoRepetitivoDt = (RelatorioRecursoRepetitivoDt) request.getSession()
				.getAttribute("RelatorioRecursoRepetitivoDt");
		if (relatorioRecursoRepetitivoDt == null)
			relatorioRecursoRepetitivoDt = new RelatorioRecursoRepetitivoDt();

		if (request.getParameter("DataInicial") != null && !request.getParameter("DataInicial").equals("")) {
			relatorioRecursoRepetitivoDt.setDataInicial(request.getParameter("DataInicial"));
		}
		if (request.getParameter("DataFinal") != null && !request.getParameter("DataFinal").equals("")) {
			relatorioRecursoRepetitivoDt.setDataFinal(request.getParameter("DataFinal"));
		}

		if (request.getParameter("Tema") != null) {
			if (request.getParameter("Tema").equals("null")) {
				relatorioRecursoRepetitivoDt.setTema("");
			} else {
				relatorioRecursoRepetitivoDt.setTema(request.getParameter("Tema"));
			}
		}

		if (request.getParameter("Id_Tema") != null) {
			if (request.getParameter("Id_Tema").equals("null")) {
				relatorioRecursoRepetitivoDt.setIdTema("");
			} else {
				relatorioRecursoRepetitivoDt.setIdTema(request.getParameter("Id_Tema"));
			}
		}

		if (request.getParameter("TipoRelatorio") != null) {
			if (request.getParameter("TipoRelatorio").equals("null")) {
				relatorioRecursoRepetitivoDt.setTipoRelatorio("");
			} else {
				relatorioRecursoRepetitivoDt.setTipoRelatorio(request.getParameter("TipoRelatorio"));
			}
		}

		if (request.getParameter("OpcaoRelatorio") != null) {
			if (request.getParameter("OpcaoRelatorio").equals("null")) {
				relatorioRecursoRepetitivoDt.setOpcaoRelatorio("");
			} else {
				relatorioRecursoRepetitivoDt.setOpcaoRelatorio(request.getParameter("OpcaoRelatorio"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {

		case Configuracao.Imprimir:

			mensagemRetorno = "";			

			if (relatorioRecursoRepetitivoDt.getDataInicial() == null
					|| relatorioRecursoRepetitivoDt.getDataInicial().equals("null")
					|| relatorioRecursoRepetitivoDt.getDataInicial().equals("")) {
				mensagemRetorno = "O campo Data Inicial é obrigatório. \n";
			}
			if (relatorioRecursoRepetitivoDt.getDataFinal() == null
					|| relatorioRecursoRepetitivoDt.getDataFinal().equals("null")
					|| relatorioRecursoRepetitivoDt.getDataFinal().equals("")) {
				mensagemRetorno += "O campo Data Final é obrigatório. \n";
			}

			if (mensagemRetorno.equals("")) {
				boolean relatorioSumario = true;
				boolean relatorioOpcao = true;
				// Se o parâmetro tipoRelatorio for setado e igual a 1 significa que o relatório
				// é analítico, se for 2 é sumário
				if (relatorioRecursoRepetitivoDt.getTipoRelatorio() != null
						&& relatorioRecursoRepetitivoDt.getTipoRelatorio().equals("1")) {
					relatorioSumario = false;
				}
				// Se o parâmetro opcaoRelatorio for setado e igual a 1 significa que o
				// relatório é por tema, se for 2 é por camara
				if (relatorioRecursoRepetitivoDt.getOpcaoRelatorio() != null
						&& relatorioRecursoRepetitivoDt.getOpcaoRelatorio().equals("1")) {
					relatorioOpcao = false;
				}
				if (relatorioOpcao) {
					byTemp = relatorioEstatisticaNe.relAnaliticoRecursoRepetitivoPorServentia(
							ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
							relatorioRecursoRepetitivoDt.getDataInicial(), relatorioRecursoRepetitivoDt.getDataFinal(),
							relatorioRecursoRepetitivoDt.getIdTema(), relatorioRecursoRepetitivoDt.getTema(),
							UsuarioSessao.getUsuarioDt().getNome());
				} else {
					if (relatorioSumario) {
						byTemp = relatorioEstatisticaNe.relSumarioRecursoRepetitivo(
								ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
								relatorioRecursoRepetitivoDt.getDataInicial(),
								relatorioRecursoRepetitivoDt.getDataFinal(), relatorioRecursoRepetitivoDt.getIdTema(),
								relatorioRecursoRepetitivoDt.getTema(), UsuarioSessao.getUsuarioDt().getNome());
					} else {
						byTemp = relatorioEstatisticaNe.relAnaliticoRecursoRepetitivo(
								ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
								relatorioRecursoRepetitivoDt.getDataInicial(),
								relatorioRecursoRepetitivoDt.getDataFinal(), relatorioRecursoRepetitivoDt.getIdTema(),
								relatorioRecursoRepetitivoDt.getTema(), UsuarioSessao.getUsuarioDt().getNome());
					}
				}
				// se byTemp for null deve retornar msg de erro de relatório vazio para o
				// usuário
				if (byTemp != null) {
					String nome = "";
					if (relatorioOpcao) { // mechi
						nome = "RelatorioAnaliticoRecursoRepetitivoPorServentia";
					} else {
						if (relatorioSumario) {
							nome = "RelatorioSumarioRecursoRepetitivo";
						} else {
							nome = "RelatorioAnaliticoRecursoRepetitivo";
						}
					}

					enviarPDF(response, byTemp, nome);
				} else {
					if (request.getSession().getAttribute("stAcaoRetorno") != null
							&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
						stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
					}
					request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					break;
				}

				return;

			} else {
				if (request.getSession().getAttribute("stAcaoRetorno") != null
						&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;

		case Configuracao.Novo:
			relatorioRecursoRepetitivoDt.limparCamposConsulta();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (request.getSession().getAttribute("stAcaoRetorno") != null
					&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;

		case (TemaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Tema", "Origem", "Situação", "Código" };
				String[] lisDescricao = { "Tema", "Origem", "Situação" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Tema");
				request.setAttribute("tempBuscaDescricao", "Tema");
				request.setAttribute("tempBuscaPrograma", "Tema");
				request.setAttribute("tempRetorno", "RelatorioRecursoRepetitivo");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						(TemaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarDescricaoTemaJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3,
						stNomeBusca4, posicaoPaginaAtual);

				enviarJSON(response, stTemp);

				return;
			}
			break;

		case Configuracao.Curinga9: 
			
			request.setAttribute("tempRetorno", "RelatorioEstatisticoNugep"); 
			
			request.setAttribute("tempPrograma", "Movimentação de Recursos");
			
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			
			if (request.getSession().getAttribute("stAcaoRetorno") != null
					&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			stAcao = "WEB-INF/jsptjgo/RelatorioEstatisticoNugep.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			break;

		case Configuracao.Curinga8:
			
			mensagemRetorno = "";

			if (relatorioRecursoRepetitivoDt.getDataInicial() == null
					|| relatorioRecursoRepetitivoDt.getDataInicial().equals("null")
					|| relatorioRecursoRepetitivoDt.getDataInicial().equals("")) {
				mensagemRetorno = "O campo Data Inicial é obrigatório. \n";
			}
			if (relatorioRecursoRepetitivoDt.getDataFinal() == null
					|| relatorioRecursoRepetitivoDt.getDataFinal().equals("null")
					|| relatorioRecursoRepetitivoDt.getDataFinal().equals("")) {
				mensagemRetorno += "O campo Data Final é obrigatório. \n";
			}

			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.relatorioEstatisticoNugep(
						ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
						relatorioRecursoRepetitivoDt.getDataInicial(), relatorioRecursoRepetitivoDt.getDataFinal(),
						UsuarioSessao.getUsuarioDt().getNome());
				// se byTemp for null deve retornar msg de erro de relatório vazio para o
				// usuário
				if (byTemp != null) {
					String nome = "RelatorioEstatisticoNugep";
					enviarPDF(response, byTemp, nome);
				} else {
					if (request.getSession().getAttribute("stAcaoRetorno") != null
							&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
						stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
					}
					request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					break;
				}
				return;

			} else {
				if (request.getSession().getAttribute("stAcaoRetorno") != null
						&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;

		default:
			
			request.setAttribute("tempRetorno", "RelatorioRecursoRepetitivo");
			
			request.setAttribute("tempPrograma", "Relatório de Processos Sobrestados");
			
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			if (request.getSession().getAttribute("stAcaoRetorno") != null
					&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioRecursoRepetitivo.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			break;
		}

		request.getSession().setAttribute("RelatorioRecursoRepetitivodt", relatorioRecursoRepetitivoDt);
		request.getSession().setAttribute("RelatorioEstatisticane", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
