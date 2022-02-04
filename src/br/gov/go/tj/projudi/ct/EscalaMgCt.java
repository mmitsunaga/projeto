package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.projudi.dt.EscalaMgDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.EscalaMgNe;
import br.gov.go.tj.projudi.ne.EscalaTipoMgNe;

public class EscalaMgCt extends Controle {

	private static final long serialVersionUID = -8759887635295944141L;

	public int Permissao() {
		return EscalaMgDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao,
			int paginaatual, String tempNomeBusca, String posicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		List listaEscalaTipoMg = new ArrayList();
		List listaEscalaMg = new ArrayList();

		EscalaMgNe escalaMgNe = new EscalaMgNe();
		EscalaMgDt escalaMgDt = new EscalaMgDt();

		UsuarioNe usuarioNe = new UsuarioNe();

		String idUsuario = (request.getParameter("Id_Usuario"));
		if (ValidacaoUtil.isVazio(idUsuario))
			escalaMgDt.setIdUsuario("");
		else
			escalaMgDt.setIdUsuario(idUsuario);

		String usuario = (request.getParameter("Usuario"));
		if (ValidacaoUtil.isVazio(usuario))
			escalaMgDt.setUsuario("");
		else
			escalaMgDt.setUsuario(usuario);

		EscalaTipoMgNe escalaTipoMgNe = new EscalaTipoMgNe();
		listaEscalaTipoMg = escalaTipoMgNe.consultaEscalaTipoMg();
		request.setAttribute("listaEscalaTipoMg", listaEscalaTipoMg);

		String idEscalaTipoMg = request.getParameter("idEscalaTipoMg");
		String escalaTipoMg = request.getParameter("escalaTipoMg");
		String idEscalaMg = request.getParameter("idEscalaMg");
		String fluxo = request.getParameter("Fluxo");

		String stNomeBusca1 = "";
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "";

		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("Mensagem", "");
		request.setAttribute("MensagemOk", "");

		switch (paginaatual) {

		case (UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "Nome" };
				String[] lisDescricao = { "Nome", "Usuário", "Rg", "Cpf" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Usuario");
				request.setAttribute("tempBuscaDescricao", "Usuario");
				request.setAttribute("tempBuscaPrograma", "Usuario");
				request.setAttribute("tempRetorno", "EscalaMg");
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
				stTemp = usuarioNe.consultarOficialJusticaJSON(stNomeBusca1, posicaoPaginaAtual,
						usuarioSessao.getId_Serventia());
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case Configuracao.Salvar:

			if (!ValidacaoUtil.isVazio(fluxo) && (request.getParameter("Fluxo").equalsIgnoreCase("cadastraEscala"))) {

				stAcao = "/WEB-INF/jsptjgo/EscalaMg.jsp";

				if (ValidacaoUtil.isVazio(idUsuario)) {
					request.setAttribute("MensagemErro", "O campo oficial e obrigatório.");
					break;
				}
				if (ValidacaoUtil.isVazio(idEscalaTipoMg)) {
					request.setAttribute("MensagemErro", "O campo escala e obrigatório.");
					break;
				}

				EscalaMgDt escalaDt = escalaMgNe.consultaPorIdUsuarioAtivo(idUsuario);
				if (escalaDt != null) {
					if (escalaDt.getDataFim().equalsIgnoreCase("")) {
						request.setAttribute("MensagemErro",
								"Oficial já está cadastrado na escala " + escalaDt.getEscalaTipoMg() + ".");
						break;
					}
				}
				request.setAttribute("idEscalaMg", "idEscalaMg");
			} else {

				if (ValidacaoUtil.isVazio(idEscalaMg)) {
					stAcao = "/WEB-INF/jsptjgo/EscalaMg.jsp";
					request.setAttribute("MensagemErro", "Oficial não selecionado.");
					break;
				}
				escalaMgDt = escalaMgNe.consultaPorIdEscala(idEscalaMg);
				request.setAttribute("escalaMgDt", escalaMgDt);
				request.setAttribute("idEscalaMg", idEscalaMg);
				request.setAttribute("tempRetorno", "EscalaMg");
				stAcao = "/WEB-INF/jsptjgo/RetiraOficialEscalaMg.jsp";
			}
			request.setAttribute("listaEscalaTipoMg", listaEscalaTipoMg);
			request.setAttribute("escalaTipoMg", escalaTipoMg);
			request.setAttribute("Mensagem", "Clique para salvar.");
			request.setAttribute("idEscalaTipoMg", idEscalaTipoMg);
			request.setAttribute("tempRetorno", "EscalaMg");
			break;

		case Configuracao.SalvarResultado:

			if (!ValidacaoUtil.isVazio(idEscalaMg)) {
				escalaMgNe.AlteraEscalaMg(idEscalaMg, usuarioSessao, request.getRemoteAddr());
				escalaMgDt.limpar();
				request.setAttribute("MensagemOk", "Oficial retirado da escala.");
			} else {
				escalaMgDt = new EscalaMgDt();
				escalaMgDt.setIdUsuario(idUsuario);
				escalaMgDt.setIdEscalaTipoMg(idEscalaTipoMg);
				escalaMgDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
				escalaMgDt.setIpComputadorLog(request.getRemoteAddr());
				escalaMgNe.CadastraEscalaMg(escalaMgDt);
				escalaMgDt.limpar();
				request.setAttribute("MensagemOk", "Oficial incluido na escala.");
			}
			request.setAttribute("tempRetorno", "EscalaMg");
			stAcao = "/WEB-INF/jsptjgo/EscalaMg.jsp";
			break;

		case Configuracao.Curinga6:

			listaEscalaMg = escalaMgNe.ListaTodosEscala(usuarioSessao.getId_Serventia());
			if (listaEscalaMg.isEmpty()) {
				request.setAttribute("MensagemErro", "A consulta não retornou dados.");
				stAcao = "/WEB-INF/jsptjgo/EscalaMg.jsp";
				break;
			}
			request.setAttribute("listaEscalaMg", listaEscalaMg);
			request.setAttribute("tempRetorno", "EscalaMg");

			stAcao = "/WEB-INF/jsptjgo/ListaEscalaMg.jsp";
			break;

		case Configuracao.Curinga7:

			escalaMgDt = escalaMgNe.consultaPorIdEscala(idEscalaMg);
			request.setAttribute("escalaMgDt", escalaMgDt);
			request.setAttribute("tempRetorno", "EscalaMg");
			request.setAttribute("idEscalaMg", idEscalaMg);
			stAcao = "/WEB-INF/jsptjgo/RetiraOficialEscalaMg.jsp";
			break;

		default:
			request.setAttribute("tempRetorno", "EscalaMg");
			stAcao = "WEB-INF/jsptjgo/EscalaMg.jsp";
			break;
		}
		request.setAttribute("tempPrograma", "Cadastro Escala Mandado Gratuito");
		request.setAttribute("escalaMgDt", escalaMgDt);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
