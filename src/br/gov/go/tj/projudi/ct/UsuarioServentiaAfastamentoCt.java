package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt;
import br.gov.go.tj.projudi.ne.UsuarioServentiaAfastamentoNe;
import br.gov.go.tj.projudi.ne.AfastamentoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class UsuarioServentiaAfastamentoCt extends UsuarioServentiaAfastamentoCtGen {

	private static final long serialVersionUID = 7252467572804599026L;

	String stAcao = "";

	public int Permissao() {
		return UsuarioServentiaAfastamentoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,
			int paginaatual, String tempNomeBusca, String posicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		UsuarioServentiaAfastamentoDt usuarioServentiaAfastamentoDt = new UsuarioServentiaAfastamentoDt();
		UsuarioServentiaAfastamentoNe usuarioServentiaAfastamentoNe = new UsuarioServentiaAfastamentoNe();

		ServentiaDt servDt;
		ServentiaNe servNe = new ServentiaNe();
		servDt = servNe.consultarIdSimples(UsuarioSessao.getId_Serventia());
		String nomeServentia = servDt.getServentia();
		request.setAttribute("nomeServentia", nomeServentia);

		request.setAttribute("tempPrograma", "UsuarioAfastamento");

		if (request.getParameter("Id_Usuario") != null && !request.getParameter("Id_Usuario").equalsIgnoreCase("")) {
			request.getSession().setAttribute("idUsuario", request.getParameter("Id_Usuario"));
			usuarioServentiaAfastamentoDt.setId_Usuario(request.getParameter("Id_Usuario"));
			List listaUsuarioServentiaAfastamento = new ArrayList();
			listaUsuarioServentiaAfastamento = usuarioServentiaAfastamentoNe.consultarAfastamentoPorOficialServentia(
					request.getParameter("Id_Usuario"), UsuarioSessao.getId_Serventia());
			request.setAttribute("listaUsuarioServentiaAfastamento", listaUsuarioServentiaAfastamento);
		} else {
			usuarioServentiaAfastamentoDt.setId_Usuario("");
		}

		if (request.getParameter("Usuario") != null) {
			usuarioServentiaAfastamentoDt.setNomeUsuario(request.getParameter("Usuario"));
		} else
			usuarioServentiaAfastamentoDt.setNomeUsuario("");

		String stNomeBusca1 = "";
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");

		if (request.getSession().getAttribute("stAcaoRetorno") != null
				&& !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
			this.stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {

		case Configuracao.Localizar:
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Usuário" };
				String[] lisDescricao = { "Usuário", "Nome", "Data do Afastamento", "Tipo" };
				stAcao = "/WEB-INF/jsptjgo/UsuarioServentiaAfastamentoLocalizar.jsp";
				request.setAttribute("tempBuscaId", "Id_UsuarioServentiaAfastamento");
				request.setAttribute("tempBuscaDescricao", "UsuarioServentiaAfastamento");
				request.setAttribute("tempBuscaPrograma", "UsuarioServentiaAfastamento");
				request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Imprimir);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				request.getParameter("Passo");
				stTemp = usuarioServentiaAfastamentoNe.consultarDescricaoAfastamentosAbertosJSON(stNomeBusca1,
						posicaoPaginaAtual, UsuarioSessao.getId_Serventia());

				enviarJSON(response, stTemp);

				return;
			}
			break;

		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):

			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Nome" };
				String[] lisDescricao = { "Nome", "Usuário", "Rg", "Cpf" };
				this.stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Usuario");
				request.setAttribute("tempBuscaDescricao", "Usuario");
				request.setAttribute("tempBuscaPrograma", "Usuario");
				request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("tempPrograma", "Afastamento / Retorno de Oficiais de Justiça");
			} else {
				UsuarioNe usuarioNe = new UsuarioNe();
				String stTemp = "";
				stTemp = usuarioNe.consultarOficialJusticaJSON(stNomeBusca1, posicaoPaginaAtual,
						UsuarioSessao.getId_Serventia());
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case (AfastamentoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Afastamento" };
				String[] lisDescricao = { "Afastamento" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Afastamento");
				request.setAttribute("tempBuscaDescricao", "Afastamento");
				request.setAttribute("tempBuscaPrograma", "Afastamento");
				request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual",
						(AfastamentoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = usuarioServentiaAfastamentoNe.consultarDescricaoAfastamentoJSON(stNomeBusca1,
						posicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case Configuracao.Curinga6:

			List listaAfastamento = new ArrayList();
			usuarioServentiaAfastamentoDt = usuarioServentiaAfastamentoNe.consultarAfastamentoAbertoPorOficialServentia(
					request.getParameter("Id_Usuario"), UsuarioSessao.getId_Serventia());
			if (usuarioServentiaAfastamentoDt.getId_Afastamento()
					.equalsIgnoreCase(AfastamentoDt.CODIGO_SUSPENSAO_POR_ATRASO)) {
				request.setAttribute("usuarioServentiaAfastamentoDt", usuarioServentiaAfastamentoDt);
				request.setAttribute("MensagemErro", "Oficial já está suspenso por atraso");
				request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
				request.setAttribute("tempPrograma", "Afastamento /Retorno de Oficiais de Justiça");
				this.stAcao = "WEB-INF/jsptjgo/UsuarioServentiaAfastamento.jsp";
			} else {

				if (usuarioServentiaAfastamentoDt.getAcao().equalsIgnoreCase("Afastar")) {
					AfastamentoNe afastamentoNe = new AfastamentoNe();
					listaAfastamento = afastamentoNe.consultarTodos();
					request.setAttribute("listaAfastamento", listaAfastamento);
				}
				request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
				request.setAttribute("PaginaAtual", Configuracao.Salvar);
				request.setAttribute("idUsuario", request.getParameter("Id_Usuario"));
				request.setAttribute("idUsuarioServentiaAfastamento", usuarioServentiaAfastamentoDt.getId());
				request.setAttribute("idUsuarioServentia", usuarioServentiaAfastamentoDt.getId_UsuarioServentia());
				request.setAttribute("usuarioServentiaAfastamentoDt", usuarioServentiaAfastamentoDt);
				request.setAttribute("tempPrograma", "Afastamento / Retorno de Oficiais de Justiça");
				this.stAcao = "WEB-INF/jsptjgo/EditaUsuarioServentiaAfastamento.jsp";
				request.getSession().setAttribute("stAcaoRetorno", this.stAcao);
			}
			break;

		case Configuracao.Salvar:

			String motivoInicio = request.getParameter("motivoInicio");
			String motivoFim = request.getParameter("motivoFim");

			listaAfastamento = new ArrayList();
			usuarioServentiaAfastamentoDt = usuarioServentiaAfastamentoNe.consultarAfastamentoAbertoPorOficialServentia(
					(String) request.getSession().getAttribute("idUsuario"), UsuarioSessao.getId_Serventia());

			if (usuarioServentiaAfastamentoDt.getAcao().equalsIgnoreCase("Afastar")) {
				AfastamentoNe afastamentoNe = new AfastamentoNe();
				listaAfastamento = afastamentoNe.consultarTodos();
				request.setAttribute("listaAfastamento", listaAfastamento);
			}

			usuarioServentiaAfastamentoDt.setMotivoFim(motivoFim);
			usuarioServentiaAfastamentoDt.setMotivoInicio(motivoInicio);
			request.setAttribute("Mensagem", "Clique para atualizar");
			request.setAttribute("idAfastamento", request.getParameter("idAfastamento"));
			request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
			request.setAttribute("idUsuario", (String) request.getSession().getAttribute("idUsuario"));
			request.setAttribute("idUsuarioServentiaAfastamento", usuarioServentiaAfastamentoDt.getId());
			request.setAttribute("idUsuarioServentia", usuarioServentiaAfastamentoDt.getId_UsuarioServentia());
			request.setAttribute("usuarioServentiaAfastamentoDt", usuarioServentiaAfastamentoDt);
			request.setAttribute("tempPrograma", "Afastamento / Retorno de Oficiais de Justiça");
			this.stAcao = "WEB-INF/jsptjgo/EditaUsuarioServentiaAfastamento.jsp";
			request.getSession().setAttribute("stAcaoRetorno", this.stAcao);
			break;

		case Configuracao.SalvarResultado:

			usuarioServentiaAfastamentoDt = new UsuarioServentiaAfastamentoDt();

			usuarioServentiaAfastamentoDt.setId_Afastamento(request.getParameter("idAfastamento"));
			usuarioServentiaAfastamentoDt
					.setId_UsuarioServentiaAfastamento(request.getParameter("idUsuarioServentiaAfastamento"));
			usuarioServentiaAfastamentoDt.setId_Usuario((String) request.getSession().getAttribute("idUsuario"));
			usuarioServentiaAfastamentoDt.setMotivoInicio(request.getParameter("motivoInicio"));
			usuarioServentiaAfastamentoDt.setMotivoFim(request.getParameter("motivoFim"));

			usuarioServentiaAfastamentoNe.afastamentoRetornoRetornaOficial(usuarioServentiaAfastamentoDt,
					UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getId_Serventia(),
					UsuarioSessao.getIpComputadorLog(), UsuarioSessao.getUsuarioDt().getId());
			request.setAttribute("MensagemOk", "Operação realizada com sucesso!");

			request.setAttribute("usuarioServentiaAfastamentoDt", usuarioServentiaAfastamentoDt);
			request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
			request.setAttribute("tempPrograma", "Afastamento /Retorno de Oficiais de Justiça");
			this.stAcao = "WEB-INF/jsptjgo/UsuarioServentiaAfastamento.jsp";

			break;

		default:
			request.setAttribute("tempRetorno", "UsuarioServentiaAfastamento");
			request.setAttribute("tempPrograma", "Afastamento /Retorno de Oficiais de Justiça");
			if (paginaatual == Configuracao.Imprimir) {
				usuarioServentiaAfastamentoDt = usuarioServentiaAfastamentoNe
						.consultarId(request.getParameter("Id_UsuarioServentiaAfastamento"));
				usuarioServentiaAfastamentoDt.setAcao("");
				this.stAcao = "WEB-INF/jsptjgo/EditaUsuarioServentiaAfastamento.jsp";
			} else
				this.stAcao = "WEB-INF/jsptjgo/UsuarioServentiaAfastamento.jsp";
			request.setAttribute("usuarioServentiaAfastamentoDt", usuarioServentiaAfastamentoDt);
			request.getSession().setAttribute("stAcaoRetorno", this.stAcao);
			break;
		}

		RequestDispatcher dis = request.getRequestDispatcher(this.stAcao);
		dis.include(request, response);
	}
}
