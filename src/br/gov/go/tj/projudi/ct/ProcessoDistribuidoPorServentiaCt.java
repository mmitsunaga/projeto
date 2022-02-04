package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoDistribuidoPorServentiaDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaGrupoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoDistribuidoPorServentiaCt extends Controle {

	private static final long serialVersionUID = 3730482006633785400L;

	public int Permissao() {
		return ProcessoDistribuidoPorServentiaDt.CodigoPermissaoDistribuicao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,
			int paginaatual, String tempNomeBusca, String posicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		ProcessoDistribuidoPorServentiaDt ProcessoDistribuidoPorServentiaDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe = new RelatorioEstatisticaNe();
		UsuarioServentiaGrupoDt UsuarioPorServentiaGrupodt = new UsuarioServentiaGrupoDt();
		UsuarioServentiaGrupoNe UsuarioServentiaGrupone = new UsuarioServentiaGrupoNe();

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");
		if (request.getParameter("nomeBusca2") != null)
			stNomeBusca2 = request.getParameter("nomeBusca2");

		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempPrograma", "Processo Distribuido Por Serventia");
		request.setAttribute("tempRetorno", "ProcessoDistribuidoPorServentia");
		request.setAttribute("tempBuscaSistema", "Sistema");

		ProcessoDistribuidoPorServentiaDt = (ProcessoDistribuidoPorServentiaDt) request.getSession()
				.getAttribute("Relatorio1");
		if (ProcessoDistribuidoPorServentiaDt == null)
			ProcessoDistribuidoPorServentiaDt = new ProcessoDistribuidoPorServentiaDt();

		if (request.getParameter("Id_AreaDistribuicao") != null) {
			if (request.getParameter("Id_AreaDistribuicao").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setIdAreaDistribuicao("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			}
		}
		if (request.getParameter("AreaDistribuicao") != null) {
			if (request.getParameter("AreaDistribuicao").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setAreaDistribuicao("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
			}
		}
		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setIdServentia("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setIdServentia(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setServentia("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setServentia(request.getParameter("Serventia"));
			}
		}

		if (request.getParameter("Id_Usuario") != null) {
			if (request.getParameter("Id_Usuario").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setIdUsuario("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setIdUsuario(request.getParameter("Id_Usuario"));
			}
		}

		if (request.getParameter("Usuario") != null) {
			if (request.getParameter("Usuario").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setUsuario("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setUsuario(request.getParameter("Usuario"));
			}
		}

		if (request.getParameter("DataInicial") != null) {
			if (request.getParameter("DataInicial").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setDataInicial("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setDataInicial(request.getParameter("DataInicial"));
			}
		}
		if (request.getParameter("DataFinal") != null) {
			if (request.getParameter("DataFinal").equals("null")) {
				ProcessoDistribuidoPorServentiaDt.setDataFinal("");
			} else {
				ProcessoDistribuidoPorServentiaDt.setDataFinal(request.getParameter("DataFinal"));
			}
		}
		ProcessoDistribuidoPorServentiaDt.setTipoRelatorio(request.getParameter("TipoRelatorio"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		if (UsuarioSessao.getGrupoCodigo().equalsIgnoreCase(Integer.toString(GrupoDt.ESTATISTICA))) {
			request.setAttribute("imaLocalizarServentia", "");
		} else {
			if (UsuarioSessao.getGrupoCodigo()
					.equalsIgnoreCase(Integer.toString(GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU))) {
				if (UsuarioSessao.getId_Serventia()
						.equalsIgnoreCase(Integer.toString(ServentiaDt.DIVISAO_DISTRIBUICAO_TRIBUNAL_ID))) {
					request.setAttribute("imaLocalizarServentia", "");
				} else {
					request.setAttribute("imaLocalizarServentia", "disabled");
				}
			} else {
				request.setAttribute("imaLocalizarServentia", "disabled");
			}
		}

		switch (paginaatual) {

		case Configuracao.Imprimir:

			mensagemRetorno = "";

			if (ProcessoDistribuidoPorServentiaDt.getIdAreaDistribuicao() == null
					|| ProcessoDistribuidoPorServentiaDt.getIdAreaDistribuicao().equals("null")
					|| ProcessoDistribuidoPorServentiaDt.getIdAreaDistribuicao().equals("")) {
				mensagemRetorno = "O campo área de distribuição é obrigatório. \n";
			}

			if (ProcessoDistribuidoPorServentiaDt.getDataInicial() == null
					|| ProcessoDistribuidoPorServentiaDt.getDataInicial().equals("null")
					|| ProcessoDistribuidoPorServentiaDt.getDataInicial().equals("")) {
				mensagemRetorno += "O campo data inicial é obrigatório. \n";
			}
			if (ProcessoDistribuidoPorServentiaDt.getDataFinal() == null
					|| ProcessoDistribuidoPorServentiaDt.getDataFinal().equals("null")
					|| ProcessoDistribuidoPorServentiaDt.getDataFinal().equals("")) {
				mensagemRetorno += "O campo data final é obrigatório. \n";
			}
			if (ProcessoDistribuidoPorServentiaDt.getTipoRelatorio() == null
					|| ProcessoDistribuidoPorServentiaDt.getTipoRelatorio().equals("null")
					|| ProcessoDistribuidoPorServentiaDt.getTipoRelatorio().equals("")) {
				mensagemRetorno += "O campo tipo de relatório é obrigatório. \n";
			}

			if (mensagemRetorno.equals("")) {

				if (ProcessoDistribuidoPorServentiaDt.getTipoRelatorio().equals("1")) {
					byTemp = relatorioEstatisticaNe.processoDistribuidoPorServentiaSintetico(
							ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
							ProcessoDistribuidoPorServentiaDt.getIdAreaDistribuicao(),
							ProcessoDistribuidoPorServentiaDt.getAreaDistribuicao(),
							ProcessoDistribuidoPorServentiaDt.getIdServentia(),
							ProcessoDistribuidoPorServentiaDt.getServentia(),
							ProcessoDistribuidoPorServentiaDt.getIdUsuario(),
							ProcessoDistribuidoPorServentiaDt.getUsuario(),
							ProcessoDistribuidoPorServentiaDt.getDataInicial(),
							ProcessoDistribuidoPorServentiaDt.getDataFinal(), UsuarioSessao.getUsuarioDt().getNome(),
							UsuarioSessao.getId_Usuario(), UsuarioSessao.getId_Serventia(),
							UsuarioSessao.getGrupoCodigo());
				} else {
					if (ProcessoDistribuidoPorServentiaDt.getTipoRelatorio().equals("2")) {
						byTemp = relatorioEstatisticaNe.processoDistribuidoPorServentiaAnalitico(
								ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
								ProcessoDistribuidoPorServentiaDt.getIdAreaDistribuicao(),
								ProcessoDistribuidoPorServentiaDt.getAreaDistribuicao(),
								ProcessoDistribuidoPorServentiaDt.getIdServentia(),
								ProcessoDistribuidoPorServentiaDt.getServentia(),
								ProcessoDistribuidoPorServentiaDt.getIdUsuario(),
								ProcessoDistribuidoPorServentiaDt.getUsuario(),
								ProcessoDistribuidoPorServentiaDt.getDataInicial(),
								ProcessoDistribuidoPorServentiaDt.getDataFinal(),
								UsuarioSessao.getUsuarioDt().getNome(), UsuarioSessao.getId_Usuario(),
								UsuarioSessao.getId_Serventia(), UsuarioSessao.getGrupoCodigo());
					}
				}

				if (byTemp != null) {

					if (ProcessoDistribuidoPorServentiaDt.getTipoRelatorio().equals("1")) {
						enviarPDF(response, byTemp, "sinteticoDistribuidoPorServentia");
					} else {
						enviarPDF(response, byTemp, "analiticoDistribuidoPorServentia");
					}
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
			ProcessoDistribuidoPorServentiaDt.limparCamposConsulta();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (request.getSession().getAttribute("stAcaoRetorno") != null
					&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;

		case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "AreaDistribuicao" };
				String[] lisDescricao = { "Área de Distribuição" };

				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
				request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
				request.setAttribute("tempBuscaPrograma", "AreaDistribuicao");
				request.setAttribute("tempRetorno", "ProcessoDistribuidoPorServentia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";

				if (UsuarioSessao.podeConsultarOutrasAreasDistribuicao()) {
					stTemp = relatorioEstatisticaNe.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1,
							posicaoPaginaAtual);
				} else {
					stTemp = relatorioEstatisticaNe.consultarDescricaoAreaDistribuicaoServentiaJSON(stNomeBusca1,
							UsuarioSessao.getUsuarioDt().getId_Serventia(), posicaoPaginaAtual);
				}
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Serventia" };
				String[] lisDescricao = { "Serventia", "Estado" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "ProcessoDistribuidoPorServentia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String
						.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaoPaginaAtual);

				enviarJSON(response, stTemp);

				return;
			}
			break;

		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Usuario" };
				String[] lisDescricao = { "Usuário" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Usuario");
				request.setAttribute("tempBuscaDescricao", "Usuario");
				request.setAttribute("tempBuscaPrograma", "Usuario");
				request.setAttribute("tempRetorno", "ProcessoDistribuidoPorServentia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = relatorioEstatisticaNe.consultarDescricaoServidorJudiciarioJSON(stNomeBusca1, "",
						posicaoPaginaAtual);

				enviarJSON(response, stTemp);

				return;
			}
			break;

		default:
			stAcao = "WEB-INF/jsptjgo/ProcessoDistribuidoPorServentia.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.setAttribute("PaginaAtual", Configuracao.Editar);			
			break;
		}
		request.getSession().setAttribute("Relatorio1", ProcessoDistribuidoPorServentiaDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
