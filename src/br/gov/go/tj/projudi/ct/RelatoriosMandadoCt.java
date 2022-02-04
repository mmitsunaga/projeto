package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ImportaDadosSPGDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.projudi.ne.ImportaDadosSPGNe;
import br.gov.go.tj.projudi.ne.RelatoriosMandadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

public class RelatoriosMandadoCt extends Controle {

	private static final long serialVersionUID = -8759887635295944141L;

	String stAcao = "";

	public int Permissao() {
		return RelatoriosMandadoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,
			int paginaatual, String tempNomeBusca, String posicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		RelatoriosMandadoDt relatoriosMandadoDt = (RelatoriosMandadoDt) request.getSession()
				.getAttribute("relatorioMandadoDt");

		if (relatoriosMandadoDt == null)
			relatoriosMandadoDt = new RelatoriosMandadoDt();

		if (request.getParameter("Fluxo") != null)
			request.getSession().setAttribute("Fluxo", request.getParameter("Fluxo"));

		String grupoNome = "";
		request.setAttribute("grupoNome", "");
		if (UsuarioSessao.getGrupoCodigo().equalsIgnoreCase(Integer.toString(GrupoDt.OFICIAL_JUSTICA))) {
			request.setAttribute("grupoNome", "oficialJustica");
			grupoNome = "oficialJustica";
		}
		if (UsuarioSessao.getGrupoCodigo().equalsIgnoreCase(Integer.toString(GrupoDt.ANALISTA_FINANCEIRO))) {
			request.setAttribute("grupoNome", "analistaFinanceiro");
			grupoNome = "analistaFinanceiro";
		}
		if (UsuarioSessao.getGrupoCodigo().equalsIgnoreCase(Integer.toString(GrupoDt.COORDENADOR_CENTRAL_MANDADO))) {
			request.setAttribute("grupoNome", "coordenadorCentral");
			grupoNome = "coordenadorCentral";
		}

		String idUsuarioTela = (request.getParameter("Id_Usuario"));
		if (idUsuarioTela != null)
			if (idUsuarioTela.equalsIgnoreCase("null")) {
				relatoriosMandadoDt.setIdUsuario(0);
				idUsuarioTela = "0";
			} else
				relatoriosMandadoDt.setIdUsuario(Integer.parseInt(idUsuarioTela));
		else
			relatoriosMandadoDt.setIdUsuario(0);

		String usuario = (request.getParameter("Usuario"));
		if (ValidacaoUtil.isVazio(usuario))
			relatoriosMandadoDt.setNomeUsuario("");
		else
			relatoriosMandadoDt.setNomeUsuario(usuario);

		String stNomeBusca1 = "";
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");

		if (request.getSession().getAttribute("stAcaoRetorno") != null
				&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
			this.stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
		}

		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		RelatoriosMandadoNe relatoriosMandadoNe = new RelatoriosMandadoNe();

		byte[] byTemp = null;

		String mensagemRetorno = "";

		int tipoOpcao = 0;

		String assistencia = "";

		String tipoRelatorio = "";

		switch (paginaatual) {

		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):

			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Nome" };
				String[] lisDescricao = { "Nome", "Usuário", "Rg", "Cpf" };
				this.stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Usuario");
				request.setAttribute("tempBuscaDescricao", "Usuario");
				request.setAttribute("tempBuscaPrograma", "Usuario");
				request.setAttribute("tempRetorno", "RelatoriosMandado");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("tempPrograma", "Lista Mandados Distribuídos");
			} else {
				UsuarioNe usuarioNe = new UsuarioNe();
				String stTemp = "";
				stTemp = usuarioNe.consultarOficialJusticaJSON(stNomeBusca1, posicaoPaginaAtual,
						UsuarioSessao.getId_Serventia());
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case Configuracao.Imprimir: // relatorios financeiros

			tipoOpcao = Integer.parseInt(request.getParameter("tipoOpcao"));

			if (tipoOpcao == 1)
				RelatorioFinanceiroMandadoGratuito(request, response, UsuarioSessao, relatoriosMandadoDt, grupoNome);
			else if (tipoOpcao == 2)
				RelatorioFinanceiroMandadoComCustas(request, response, UsuarioSessao, relatoriosMandadoDt, grupoNome);
			else if (tipoOpcao == 3)
				RelatorioFinanceiroMandadoADHoc(request, response, UsuarioSessao, relatoriosMandadoDt);
			else if (tipoOpcao == 4)
				RelatorioFinanceiroGuiaVinculada(request, response, UsuarioSessao, relatoriosMandadoDt);

			break;

		case Configuracao.Curinga6: // lista Ordem Pagamento autorizada.

			mensagemRetorno = "";
			int tipoArquivo = 0;
			int dias = 0;

			if (request.getParameter("DataInicial") == null || request.getParameter("DataInicial").equals("null")
					|| request.getParameter("DataInicial").equals("")) {
				mensagemRetorno += "O campo data inicial é obrigatório. \n";
			} else
				relatoriosMandadoDt.setDataInicial(request.getParameter("DataInicial"));

			if (request.getParameter("DataFinal") == null || request.getParameter("DataFinal").equals("null")
					|| request.getParameter("DataFinal").equals("")) {
				mensagemRetorno += "O campo data final é obrigatório. \n";
			} else
				relatoriosMandadoDt.setDataFinal(request.getParameter("DataFinal"));

			if (mensagemRetorno.equals("")) {

				dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataInicial"),
						request.getParameter("DataFinal"));
				if (dias < 0 || dias > 60)
					mensagemRetorno += "Data final menor que a data inicial ou com uma diferenca maior que 60 dias. \n";
				else {
					dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataFinal"),
							Funcoes.FormatarData(new Date()));
					if (dias < 0)
						mensagemRetorno += "Data final maior que a data do dia. \n";
				}
			}

			if (request.getParameter("tipoArquivo") != null) {
				tipoArquivo = Integer.parseInt(request.getParameter("tipoArquivo"));
			} else
				tipoArquivo = 1;

			if (mensagemRetorno.equals("")) {

				//
				// atualizar locomocoes spg. usar enquanto nao implanta saj digital
				//
				if (tipoArquivo == 2) { // enviar dados diretoria financeira. Quando implantado o saj, o texto devera
										// ser
										// gerado pelo saj acho.

					ImportaDadosSPGNe objNe = new ImportaDadosSPGNe();
					mensagemRetorno = objNe.atualizaLomocaoSpg(relatoriosMandadoDt.getDataInicial(),
							relatoriosMandadoDt.getDataFinal(), UsuarioSessao.getId_Serventia(),
							UsuarioSessao.getUsuarioDt().getId());
					request.setAttribute("MensagemOk", mensagemRetorno);
					request.setAttribute("tempRetorno", "RelatoriosMandado");
					request.setAttribute("tempPrograma", "Lista Ordem de Pagamento Autorizada");
					this.stAcao = "WEB-INF/jsptjgo/ListaOrdemPagamentoAutorizada.jsp";
				} else {
					byTemp = relatoriosMandadoNe.listaOrdemPagamentoAutorizada(
							ProjudiPropriedades.getInstance().getCaminhoAplicacao(),
							relatoriosMandadoDt.getDataInicial(), relatoriosMandadoDt.getDataFinal(),
							UsuarioSessao.getUsuarioDt().getNome(), UsuarioSessao.getUsuarioDt().getId(),
							UsuarioSessao.getGrupoCodigo(), UsuarioSessao.getId_Serventia());
					if (byTemp != null) {
						enviarPDF(response, byTemp, "listaOrdemPagamentoAutorizada");
					} else {
						request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
					}
				}
			} else {
				request.setAttribute("MensagemErro", mensagemRetorno);
			}
			request.setAttribute("tempRetorno", "RelatoriosMandado");
			request.setAttribute("tempPrograma", "Lista Ordem de pagamento Autorizada");
			this.stAcao = "WEB-INF/jsptjgo/ListaOrdemPagamentoAutorizada.jsp";
			break;

		case Configuracao.Curinga7: // lista mandados redistribuidos

			mensagemRetorno = "";

			if (request.getParameter("DataInicial") == null || request.getParameter("DataInicial").equals("null")
					|| request.getParameter("DataInicial").equals("")) {
				mensagemRetorno += "O campo data inicial é obrigatório. \n";
			} else
				relatoriosMandadoDt.setDataInicial(request.getParameter("DataInicial"));

			if (request.getParameter("DataFinal") == null || request.getParameter("DataFinal").equals("null")
					|| request.getParameter("DataFinal").equals("")) {
				mensagemRetorno += "O campo data final é obrigatório. \n";
			} else
				relatoriosMandadoDt.setDataFinal(request.getParameter("DataFinal"));

			if (mensagemRetorno.equals("")) {

				dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataInicial"),
						request.getParameter("DataFinal"));
				if (dias < 0 || dias > 60)
					mensagemRetorno += "Data final menor que a data inicial ou com uma diferenca maior que 60 dias. \n";
				else {
					dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataFinal"),
							Funcoes.FormatarData(new Date()));
					if (dias < 0)
						mensagemRetorno += "Data final maior que a data do dia. \n";
				}
			}

			if (mensagemRetorno.equals("")) {

				String serv = UsuarioSessao.getId_UsuarioServentia();

				byTemp = relatoriosMandadoNe.relatorioMandadosRedistribuidos(
						ProjudiPropriedades.getInstance().getCaminhoAplicacao(), request.getParameter("DataInicial"),
						request.getParameter("DataFinal"), UsuarioSessao.getUsuarioDt().getNome(),
						UsuarioSessao.getUsuarioDt().getId(), UsuarioSessao.getGrupoCodigo(),
						UsuarioSessao.getId_Serventia(), idUsuarioTela);

				if (byTemp != null)
					enviarPDF(response, byTemp, "listaMandadosRedistribuidos");
				else {
					request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
				}
			} else
				request.setAttribute("MensagemErro", mensagemRetorno);

			request.setAttribute("tempRetorno", "RelatoriosMandado");
			request.setAttribute("tempPrograma", "Lista Mandados Redistribuídos");
			this.stAcao = "WEB-INF/jsptjgo/ListaMandadosRedistribuidos.jsp";
			break;

		case Configuracao.Curinga8: // lista mandados distribuidos

			mensagemRetorno = "";

			if (request.getParameter("DataInicial") == null || request.getParameter("DataInicial").equals("null")
					|| request.getParameter("DataInicial").equals("")) {
				mensagemRetorno += "O campo data inicial é obrigatório. \n";
			} else
				relatoriosMandadoDt.setDataInicial(request.getParameter("DataInicial"));

			if (request.getParameter("DataFinal") == null || request.getParameter("DataFinal").equals("null")
					|| request.getParameter("DataFinal").equals("")) {
				mensagemRetorno += "O campo data final é obrigatório. \n";
			} else
				relatoriosMandadoDt.setDataFinal(request.getParameter("DataFinal"));

			if (mensagemRetorno.equals("")) {

				dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataInicial"),
						request.getParameter("DataFinal"));
				if (dias < 0 || dias > 60)
					mensagemRetorno += "Data final menor que a data inicial ou com uma diferenca maior que 60 dias. \n";
				else {
					dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataFinal"),
							Funcoes.FormatarData(new Date()));
					if (dias < 0)
						mensagemRetorno += "Data final maior que a data do dia. \n";
				}
			}

			if (request.getParameter("assistencia") != null) {
				assistencia = request.getParameter("assistencia");
				if (!assistencia.equalsIgnoreCase("sim") && !assistencia.equalsIgnoreCase("nao"))
					mensagemRetorno += "O campo assistência não foi informado corretamente. \n";
			}

			if (request.getParameter("tipoRelatorio") != null) {
				tipoRelatorio = request.getParameter("tipoRelatorio");
				if (!tipoRelatorio.equalsIgnoreCase("analitico") && !tipoRelatorio.equalsIgnoreCase("sintetico"))
					mensagemRetorno += "O campo tipo relatório não foi informado corretamente. \n";
			}

			if (mensagemRetorno.equals("")) {

				String serv = UsuarioSessao.getId_UsuarioServentia();

				byTemp = relatoriosMandadoNe.listaMandadosDistribuidos(assistencia,
						ProjudiPropriedades.getInstance().getCaminhoAplicacao(), request.getParameter("DataInicial"),
						request.getParameter("DataFinal"), UsuarioSessao.getUsuarioDt().getNome(),
						UsuarioSessao.getUsuarioDt().getId(), UsuarioSessao.getGrupoCodigo(),
						UsuarioSessao.getId_Serventia(), idUsuarioTela, tipoRelatorio);

				if (byTemp != null) {
					if (tipoRelatorio.equalsIgnoreCase("analitico"))
						enviarPDF(response, byTemp, "listaMandadosDistribuidosAnalitico");
					else
						enviarPDF(response, byTemp, "listaMandadosDistribuidosSintetico");
				} else {
					request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
				}
			} else {
				request.setAttribute("MensagemErro", mensagemRetorno);
			}

			request.setAttribute("tempRetorno", "RelatoriosMandado");
			request.setAttribute("tempPrograma", "Lista Mandados Distribuídos");
			this.stAcao = "WEB-INF/jsptjgo/ListaMandadosDistribuidos.jsp";
			break;

		case Configuracao.Curinga9: // lista mandados com o oficial

			mensagemRetorno = "";

			tipoOpcao = Integer.parseInt(request.getParameter("tipoOpcao"));

			if (tipoOpcao == 1 || tipoOpcao == 4 || tipoOpcao == 5) {
				if (request.getParameter("DataInicial") == null || request.getParameter("DataInicial").equals("null")
						|| request.getParameter("DataInicial").equals("")) {
					mensagemRetorno += "O campo data inicial é obrigatório. \n";
				} else
					relatoriosMandadoDt.setDataInicial(request.getParameter("DataInicial"));

				if (request.getParameter("DataFinal") == null || request.getParameter("DataFinal").equals("null")
						|| request.getParameter("DataFinal").equals("")) {
					mensagemRetorno += "O campo data final é obrigatório. \n";
				} else
					relatoriosMandadoDt.setDataFinal(request.getParameter("DataFinal"));

				if (mensagemRetorno.equals("")) {

					dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataInicial"),
							request.getParameter("DataFinal"));
					if (dias < 0 || dias > 60)
						mensagemRetorno += "Data final menor que a data inicial ou com uma diferenca maior que 60 dias. \n";
					else {
						dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataFinal"),
								Funcoes.FormatarData(new Date()));
						if (dias < 0)
							mensagemRetorno += "Data final maior que a data do dia. \n";
					}
				}

			}

			if (mensagemRetorno.equals("")) {

				String serv = UsuarioSessao.getId_UsuarioServentia();

				byTemp = relatoriosMandadoNe.listaMandadosPorOficial(
						ProjudiPropriedades.getInstance().getCaminhoAplicacao(), request.getParameter("DataInicial"),
						request.getParameter("DataFinal"), UsuarioSessao.getUsuarioDt().getNome(),
						UsuarioSessao.getUsuarioDt().getId(), UsuarioSessao.getGrupoCodigo(),
						UsuarioSessao.getId_Serventia(), idUsuarioTela, tipoOpcao);
				if (byTemp != null) {
					enviarPDF(response, byTemp, "listaMandadosPorOficial");
				} else {
					request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
				}
			} else {
				request.setAttribute("MensagemErro", mensagemRetorno);
			}

			request.setAttribute("tempRetorno", "RelatoriosMandado");
			request.setAttribute("tempPrograma", "Lista Mandados Por Oficial");
			this.stAcao = "WEB-INF/jsptjgo/ListaMandadosPorOficial.jsp";
			break;

		default:

			String fluxo = (String) request.getSession().getAttribute("Fluxo");

			if (fluxo.equalsIgnoreCase("RelatorioFinanceiro")) {
				request.setAttribute("tempRetorno", "RelatoriosMandado");
				request.setAttribute("tempPrograma", "Relatório Financeiro de Mandados");
				this.stAcao = "WEB-INF/jsptjgo/RelatorioFinanceiroMandado.jsp";
			} else {
				if (fluxo.equalsIgnoreCase("ListaOrdemPagamento")) {
					request.setAttribute("tempPrograma", "Lista Ordem de Pagamento Autorizada");
					request.setAttribute("tempRetorno", "RelatoriosMandado");
					this.stAcao = "WEB-INF/jsptjgo/ListaOrdemPagamentoAutorizada.jsp";
				} else {
					if (fluxo.equalsIgnoreCase("ListaMandadosPorOficial")) {
						request.setAttribute("tempPrograma", "Lista Mandados por Oficial");
						request.setAttribute("tempRetorno", "RelatoriosMandado");
						this.stAcao = "WEB-INF/jsptjgo/ListaMandadosPorOficial.jsp";
					} else {
						if (fluxo.equalsIgnoreCase("ListaMandadosDistribuidos")) {
							request.setAttribute("tempPrograma", "Lista Mandados Distribuídos");
							request.setAttribute("tempRetorno", "RelatoriosMandado");
							this.stAcao = "WEB-INF/jsptjgo/ListaMandadosDistribuidos.jsp";
						} else {
							if (fluxo.equalsIgnoreCase("ListaMandadosRedistribuidos")) {
								request.setAttribute("tempPrograma", "Lista Mandados Redistribuídos");
								request.setAttribute("tempRetorno", "RelatoriosMandado");
								this.stAcao = "WEB-INF/jsptjgo/ListaMandadosRedistribuidos.jsp";
							}
						}
					}
				}
				request.getSession().setAttribute("stAcaoRetorno", this.stAcao);
				break;
			}
		}
		request.getSession().setAttribute("relatorioMandado", relatoriosMandadoDt);

		RequestDispatcher dis = request.getRequestDispatcher(this.stAcao);
		dis.include(request, response);
	}

	public void RelatorioFinanceiroMandadoGratuito(HttpServletRequest request, HttpServletResponse response,
			UsuarioNe UsuarioSessao, RelatoriosMandadoDt relatoriosMandadoDt, String grupoNome) throws Exception {

		String mensagemRetorno = "";
		int tipoArquivo = 0;
		byte[] byTemp;
		RelatoriosMandadoNe relatoriosMandadoNe = new RelatoriosMandadoNe();

		if (request.getParameter("DataInicial") != null && !request.getParameter("DataInicial").equals("")) {
			relatoriosMandadoDt.setDataReferencia(request.getParameter("DataInicial"));
		} else
			mensagemRetorno += "O campo data Inicial e obrigatório. \n";

		if (request.getParameter("tipoArquivo") != null) {
			tipoArquivo = Integer.parseInt(request.getParameter("tipoArquivo"));
		} else
			tipoArquivo = 1;

		if (mensagemRetorno.equals("")) {
		 ///// tirar
			byTemp = relatoriosMandadoNe.relatorioFinanceiroMandadoGratuito(tipoArquivo, 
			// fim tirar
			//byTemp = relatoriosMandadoNe.relatorioMensalMandadoGratuito(tipoArquivo,
			      	 ProjudiPropriedades.getInstance().getCaminhoAplicacao(), relatoriosMandadoDt.getDataReferencia(),
					 UsuarioSessao.getUsuarioDt().getNome(), UsuarioSessao.getUsuarioDt().getId(),
					 UsuarioSessao.getGrupoCodigo(), UsuarioSessao.getId_Serventia());
					
			if (byTemp != null) {
				if (tipoArquivo == 1)
					enviarPDF(response, byTemp, "relatorioFinanceiroMandadoGratuito");
				else {
					String ano = relatoriosMandadoDt.getDataReferencia().substring(6);
					String mes = relatoriosMandadoDt.getDataReferencia().substring(3, 5);
					String nomeTexto = "mandadosGratuitos"
							+ (Funcoes.identificarNomeMes(Integer.parseInt(mes)) + "_" + ano + "_");
					enviarTXT(response, byTemp, nomeTexto);
				}
			} else {
				request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
			}
		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
		}
		request.setAttribute("tempRetorno", "RelatoriosMandado");
		request.setAttribute("tempPrograma", "Relatório Financeiro de Mandados");
		this.stAcao = "WEB-INF/jsptjgo/RelatorioFinanceiroMandado.jsp";
	}

	public void RelatorioFinanceiroMandadoComCustas(HttpServletRequest request, HttpServletResponse response,
			UsuarioNe UsuarioSessao, RelatoriosMandadoDt relatoriosMandadoDt, String grupoNome) throws Exception {
		String mensagemRetorno = "";
		int tipoArquivo = 0;
		byte[] byTemp;
		int dias = 0;

		RelatoriosMandadoNe relatoriosMandadoNe = new RelatoriosMandadoNe();

		if (request.getParameter("DataInicial") == null || request.getParameter("DataInicial").equals("null")
				|| request.getParameter("DataInicial").equals("")) {
			mensagemRetorno += "O campo data inicial é obrigatório. \n";
		} else
			relatoriosMandadoDt.setDataInicial(request.getParameter("DataInicial"));

		if (request.getParameter("DataFinal") == null || request.getParameter("DataFinal").equals("null")
				|| request.getParameter("DataFinal").equals("")) {
			mensagemRetorno += "O campo data final é obrigatório. \n";
		} else
			relatoriosMandadoDt.setDataFinal(request.getParameter("DataFinal"));

		if (mensagemRetorno.equals("")) {

			dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataInicial"),
					request.getParameter("DataFinal"));
			if (dias < 0 || dias > 60)
				mensagemRetorno += "Data final menor que a data inicial ou com uma diferenca maior que 60 dias. \n";
			else {
				dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataFinal"),
						Funcoes.FormatarData(new Date()));
				if (dias < 0)
					mensagemRetorno += "Data final maior que a data do dia. \n";
			}
		}

		if (request.getParameter("tipoArquivo") != null) {
			tipoArquivo = Integer.parseInt(request.getParameter("tipoArquivo"));
			if (tipoArquivo == 2)
				if (grupoNome.equalsIgnoreCase("analistaFinanceiro"))
					mensagemRetorno += "Tipo de arquivo inválido para mandados com custas. \n";
		} else
			tipoArquivo = 1;

		if (mensagemRetorno.equals("")) {

			byTemp = relatoriosMandadoNe.RelatorioFinanceiroMandadoComCustas(tipoArquivo,
					ProjudiPropriedades.getInstance().getCaminhoAplicacao(), relatoriosMandadoDt.getDataInicial(),
					relatoriosMandadoDt.getDataFinal(), UsuarioSessao.getUsuarioDt().getNome(),
					UsuarioSessao.getUsuarioDt().getId(), UsuarioSessao.getGrupoCodigo(),
					UsuarioSessao.getId_Serventia());

			if (byTemp != null) {
				if (tipoArquivo == 1)
					enviarPDF(response, byTemp, "relatorioFinanceiroMandadoComCustas");
				else {
					String ano = relatoriosMandadoDt.getDataReferencia().substring(6);
					String mes = relatoriosMandadoDt.getDataReferencia().substring(3, 5);
					String nomeTexto = "mandadosComCustas"
							+ (Funcoes.identificarNomeMes(Integer.parseInt(mes)) + "_" + ano + "_");
					enviarTXT(response, byTemp, nomeTexto);
				}
			} else {
				request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
			}
		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
		}
		request.setAttribute("tempRetorno", "RelatoriosMandado");
		request.setAttribute("tempPrograma", "Relatório Financeiro de  Mandados");
		this.stAcao = "WEB-INF/jsptjgo/RelatorioFinanceiroMandado.jsp";
	}

	public void RelatorioFinanceiroMandadoADHoc(HttpServletRequest request, HttpServletResponse response,
			UsuarioNe UsuarioSessao, RelatoriosMandadoDt relatoriosMandadoDt) throws Exception {

		String mensagemRetorno = "";
		byte[] byTemp;
		int tipoArquivo = 0;

		RelatoriosMandadoNe relatoriosMandadoNe = new RelatoriosMandadoNe();

		if (request.getParameter("DataInicial") != null && !request.getParameter("DataInicial").equals("")) {
			relatoriosMandadoDt.setDataReferencia(request.getParameter("DataInicial"));
		} else
			mensagemRetorno += "O campo data Inicial e obrigatório. \n";

		if (request.getParameter("tipoArquivo") != null) {
			tipoArquivo = Integer.parseInt(request.getParameter("tipoArquivo"));
			if (tipoArquivo == 2)
				mensagemRetorno += "Tipo de arquivo inválido para mandados AD HOC. \n";
		} else
			tipoArquivo = 1;

		if (mensagemRetorno.equals("")) {

			byTemp = relatoriosMandadoNe.RelatorioFinanceiroMandadoAdHoc(relatoriosMandadoDt.getDataReferencia(),
					ProjudiPropriedades.getInstance().getCaminhoAplicacao(), UsuarioSessao.getUsuarioDt().getNome(),
					UsuarioSessao.getUsuarioDt().getId(), UsuarioSessao.getGrupoCodigo(),
					UsuarioSessao.getId_Serventia());

			if (byTemp != null)
				enviarPDF(response, byTemp, "relatorioFinanceiroMandadoAdHoc");
			else {
				request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
			}
		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
		}
		request.setAttribute("tempRetorno", "RelatoriosMandado");
		request.setAttribute("tempPrograma", "Relatório Financeiro de  Mandados");
		this.stAcao = "WEB-INF/jsptjgo/RelatorioFinanceiroMandado.jsp";
	}

	public void RelatorioFinanceiroGuiaVinculada(HttpServletRequest request, HttpServletResponse response,
			UsuarioNe UsuarioSessao, RelatoriosMandadoDt relatoriosMandadoDt) throws Exception {

		String mensagemRetorno = "";
		int tipoArquivo = 0;
		byte[] byTemp;
		int dias = 0;

		RelatoriosMandadoNe relatoriosMandadoNe = new RelatoriosMandadoNe();

		if (request.getParameter("DataInicial") == null || request.getParameter("DataInicial").equals("null")
				|| request.getParameter("DataInicial").equals("")) {
			mensagemRetorno += "O campo data inicial é obrigatório. \n";
		} else
			relatoriosMandadoDt.setDataInicial(request.getParameter("DataInicial"));

		if (request.getParameter("DataFinal") == null || request.getParameter("DataFinal").equals("null")
				|| request.getParameter("DataFinal").equals("")) {
			mensagemRetorno += "O campo data final é obrigatório. \n";
		} else
			relatoriosMandadoDt.setDataFinal(request.getParameter("DataFinal"));

		if (mensagemRetorno.equals("")) {

			dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataInicial"),
					request.getParameter("DataFinal"));
			if (dias < 0 || dias > 60)
				mensagemRetorno += "Data final menor que a data inicial ou com uma diferenca maior que 60 dias. \n";
			else {
				dias = Funcoes.calculaDiferencaEntreDatas(request.getParameter("DataFinal"),
						Funcoes.FormatarData(new Date()));
				if (dias < 0)
					mensagemRetorno += "Data final maior que a data do dia. \n";
			}
		}

		if (request.getParameter("tipoArquivo") != null) {
			tipoArquivo = Integer.parseInt(request.getParameter("tipoArquivo"));
			if (tipoArquivo == 2)
				mensagemRetorno += "Tipo de arquivo inválido para guias vinculadas. \n";
		} else
			tipoArquivo = 1;

		if (mensagemRetorno.equals("")) {

			byTemp = relatoriosMandadoNe.RelatorioFinanceiroGuiaVinculada(tipoArquivo,
					relatoriosMandadoDt.getDataInicial(), relatoriosMandadoDt.getDataFinal(),
					ProjudiPropriedades.getInstance().getCaminhoAplicacao(), UsuarioSessao.getUsuarioDt().getNome(),
					UsuarioSessao.getUsuarioDt().getId(), UsuarioSessao.getGrupoCodigo(),
					UsuarioSessao.getId_Serventia());

			if (byTemp != null) {

				if (tipoArquivo == 1)
					enviarPDF(response, byTemp, "relatorioFinanceiroGuiaVinculada");
				else {
					String ano = relatoriosMandadoDt.getDataReferencia().substring(6);
					String mes = relatoriosMandadoDt.getDataReferencia().substring(3, 5);
					String nomeTexto = "guiaVinculada"
							+ (Funcoes.identificarNomeMes(Integer.parseInt(mes)) + "_" + ano + "_");
					enviarTXT(response, byTemp, nomeTexto);
				}
			} else {
				request.setAttribute("MensagemErro", "Não existe resultado para as informações solicitadas.");
			}

		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
		}
		request.setAttribute("tempRetorno", "RelatoriosMandado");
		request.setAttribute("tempPrograma", "Relatório Financeiro de  Mandados");
		this.stAcao = "WEB-INF/jsptjgo/RelatorioFinanceiroMandado.jsp";
	}
}
