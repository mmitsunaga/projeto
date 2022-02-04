package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoArquivamentoTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoArquivamentoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoArquivamentoTipoCt extends ProcessoArquivamentoTipoCtGen {

	private static final long serialVersionUID = 5331773103594691824L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoArquivamentoTipoDt ProcessoArquivamentoTipodt;
		ProcessoArquivamentoTipoNe ProcessoArquivamentoTipone;

		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoArquivamentoTipo.jsp";

		request.setAttribute("tempPrograma", "ProcessoArquivamentoTipo");

		ProcessoArquivamentoTipone = (ProcessoArquivamentoTipoNe) request.getSession().getAttribute("ProcessoArquivamentoTipone");
		if (ProcessoArquivamentoTipone == null)
			ProcessoArquivamentoTipone = new ProcessoArquivamentoTipoNe();

		ProcessoArquivamentoTipodt = (ProcessoArquivamentoTipoDt) request.getSession().getAttribute("ProcessoArquivamentoTipodt");
		if (ProcessoArquivamentoTipodt == null)
			ProcessoArquivamentoTipodt = new ProcessoArquivamentoTipoDt();

		ProcessoArquivamentoTipodt.setProcessoArquivamentoTipo(request.getParameter("ProcessoArquivamentoTipo"));

		ProcessoArquivamentoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoArquivamentoTipodt.setIpComputadorLog(request.getRemoteAddr());

		if (request.getParameter("consultarTodos") != null && request.getParameter("consultarTodos").equals("1")) {
			request.getSession().setAttribute("consultarTodos", Boolean.TRUE);
		}

		String stFluxo = request.getParameter("tempFluxo1");

		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		case Configuracao.ExcluirResultado: // Excluir
			ProcessoArquivamentoTipone.excluir(ProcessoArquivamentoTipodt);
			request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
			break;

		case Configuracao.Imprimir:
			break;

		case Configuracao.Localizar: // localizar
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "ProcessoArquivamentoTipo" };
				String[] lisDescricao = { "ProcessoArquivamentoTipo" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_PrococessoArquivamentoTipo");
				request.setAttribute("tempBuscaDescricao", "ProcessoArquivamentoTipo");
				request.setAttribute("tempBuscaPrograma", "ProcessoArquivamentoTipo");
				request.setAttribute("tempRetorno", "ProcessoArquivamentoTipo");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				boolean boTodos = false;
				if(request.getSession().getAttribute("consultarTodos") != null) {
					boTodos = Funcoes.StringToBoolean(request.getSession().getAttribute("consultarTodos").toString());
				}

				String stTemp = ProcessoArquivamentoTipone.consultarDescricaoJSON(stNomeBusca1, boTodos, PosicaoPaginaAtual);
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch (Exception e) {
				}
				return;
			}
			break;
		case Configuracao.Novo:
			ProcessoArquivamentoTipodt.limpar();
			break;
		case Configuracao.SalvarResultado:
			Mensagem = ProcessoArquivamentoTipone.Verificar(ProcessoArquivamentoTipodt);
			if (Mensagem.length() == 0) {
				if (stFluxo.equals(ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_BLOQUEIO)) {
					ProcessoArquivamentoTipodt.setCodigoTemp(String.valueOf(ProcessoArquivamentoTipoDt.INATIVO));
				} else if (stFluxo.equals(ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_DESBLOQUEIO)) {
					ProcessoArquivamentoTipodt.setCodigoTemp(String.valueOf(ProcessoArquivamentoTipoDt.ATIVO));
				}
				ProcessoArquivamentoTipone.salvar(ProcessoArquivamentoTipodt);
				ProcessoArquivamentoTipodt = ProcessoArquivamentoTipone.consultarId(ProcessoArquivamentoTipodt.getId());
				request.getSession().removeAttribute("consultarTodos");
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
			} else
				request.setAttribute("MensagemErro", Mensagem);
			break;
		case Configuracao.Salvar:
			if (stFluxo.equals(ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_BLOQUEIO)) {
				request.setAttribute("tempFluxo1", ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_BLOQUEIO);
				request.setAttribute("Mensagem", "Clique para Bloquear o Tipo de Arquivamento: " + ProcessoArquivamentoTipodt.getProcessoArquivamentoTipo());
			} else if (stFluxo.equals(ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_DESBLOQUEIO)) {
				request.setAttribute("tempFluxo1", ProcessoArquivamentoTipoDt.FLUXO_PROCESSO_ARQUIVAMENTO_TIPO_DESBLOQUEIO);
				request.setAttribute("Mensagem", "Clique para Desbloquear o Tipo de Arquivamento: " + ProcessoArquivamentoTipodt.getProcessoArquivamentoTipo());
			} else {
				request.setAttribute("Mensagem", "Clique para Salvar os dados.");
			}
			break;

		default:
			stId = request.getParameter("Id_PrococessoArquivamentoTipo");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoArquivamentoTipodt.getId())) {
					ProcessoArquivamentoTipodt.limpar();
					ProcessoArquivamentoTipodt = ProcessoArquivamentoTipone.consultarId(stId);
				}
			break;
		}

		request.getSession().setAttribute("ProcessoArquivamentoTipodt", ProcessoArquivamentoTipodt);
		request.getSession().setAttribute("ProcessoArquivamentoTipone", ProcessoArquivamentoTipone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
