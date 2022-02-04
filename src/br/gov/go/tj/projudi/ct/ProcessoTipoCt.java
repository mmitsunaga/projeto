package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

public class ProcessoTipoCt extends ProcessoTipoCtGen {

	private static final long serialVersionUID = 1510044808345067447L;

	public int Permissao() {
		return ProcessoTipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTipoDt processoTipoDt;
		ProcessoTipoNe processoTipoNe;
		String stNomeBusca1 = "";

		List tempList = null;
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoTipo.jsp";

		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "ProcessoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		processoTipoNe = (ProcessoTipoNe) request.getSession().getAttribute("ProcessoTipone");
		if (processoTipoNe == null)
			processoTipoNe = new ProcessoTipoNe();

		processoTipoDt = (ProcessoTipoDt) request.getSession().getAttribute("ProcessoTipodt");
		if (processoTipoDt == null)
			processoTipoDt = new ProcessoTipoDt();

		processoTipoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		processoTipoDt.setProcessoTipoCodigo(request.getParameter("ProcessoTipoCodigo"));
		processoTipoDt.setOrdem2Grau(request.getParameter("Ordem2Grau"));

		processoTipoDt.setCnjCodigo(request.getParameter("CnjCodigo"));

		if (request.getParameter("Publico") != null)
			processoTipoDt.setPublico(request.getParameter("Publico"));
		else
			processoTipoDt.setPublico("false");
		processoTipoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoTipoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		ProcessoNe Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null)
			Processone = new ProcessoNe();

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				processoTipoNe.excluir(processoTipoDt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir:
				Integer tipoArquivo = new Integer(request.getParameter("tipoArquivo"));
				byte[] byTemp = processoTipoNe.relProcessoTipo(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), processoTipoDt.getProcessoTipo(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
				// Se o parâmertro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
				if (tipoArquivo != null && tipoArquivo.equals(2)) {
					enviarTXT(response, byTemp, "Relatorio");
				} else {
					enviarPDF(response, byTemp, "Relatorio");
				}

				return;
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = { "ProcessoTipo" };
					String[] lisDescricao = { "ProcessoTipo","ProcessoTipoCodigo","Público" };
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoTipo");
					request.setAttribute("tempBuscaPrograma", "ProcessoTipo");
					request.setAttribute("tempRetorno", "ProcessoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp;
					
					String Id_AreaDistribuicao = request.getParameter("filtroTabela");
					if (ValidacaoUtil.isNaoVazio(Id_AreaDistribuicao)) {
						stTemp = Processone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, Id_AreaDistribuicao, UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
					} else {
						stTemp = processoTipoNe.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					}
					
					enviarJSON(response, stTemp);
					
					return;
				}
				break;
			case Configuracao.Novo:
				processoTipoDt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = processoTipoNe.Verificar(processoTipoDt);
				if (Mensagem.length() == 0) {
					processoTipoNe.salvar(processoTipoDt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			case Configuracao.Curinga6: // Curinga de acesso à tela de listagem de ProcessoTipo

				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.setAttribute("tempPrograma", "Listagem de Tipos de Processo");
				stAcao = "WEB-INF/jsptjgo/ListagemProcessoTipo.jsp";
				request.getSession().setAttribute("stAcaoRetorno", stAcao);
				processoTipoDt.limpar();
				break;
			case Configuracao.Curinga7: // Curinga destinado à listagem de ProcessoTipodt
				if (request.getParameter("ProcessoTipo") != null) {
					if (request.getParameter("ProcessoTipo").equals("null")) {
						processoTipoDt.setProcessoTipo("");
					} else {
						processoTipoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
					}
				}
				stAcao = "/WEB-INF/jsptjgo/ListagemProcessoTipo.jsp";
				tempList = processoTipoNe.consultarDescricao(processoTipoDt.getProcessoTipo(), PosicaoPaginaAtual);
				if (tempList.size() > 0) {
					request.setAttribute("listaProcessoTipo", tempList);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", processoTipoNe.getQuantidadePaginas());
					request.setAttribute("PaginaAtual", Configuracao.Curinga7);
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
				}
				request.setAttribute("tempPrograma", "Listagem de Tipos de Processo");
				break;
			default: {
				stId = request.getParameter("Id_ProcessoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(processoTipoDt.getId())) {
						processoTipoDt.limpar();
						processoTipoDt = processoTipoNe.consultarId(stId);
					}
				break;
			}
		}

		request.getSession().setAttribute("ProcessoTipodt", processoTipoDt);
		request.getSession().setAttribute("ProcessoTipone", processoTipoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}