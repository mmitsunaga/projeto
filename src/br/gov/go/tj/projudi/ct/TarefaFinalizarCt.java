package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProjetoDt;
import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.ne.TarefaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

@SuppressWarnings("unchecked")
public class TarefaFinalizarCt extends Controle {

	private static final long serialVersionUID = -4581837040631380682L;

	@Override
	public int Permissao() {
		return TarefaDt.CodigoPermissaoTarefaFinalizar;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TarefaDt Tarefadt;
		TarefaNe Tarefane;

		List tempList = null;
		String stAcao = "";

		request.setAttribute("tempPrograma", "TarefaFinalizar");

		stAcao = "/WEB-INF/jsptjgo/TarefaFinalizarLocalizar.jsp";

		request.setAttribute("tempBuscaId_Tarefa", "Id_Tarefa");
		request.setAttribute("tempBuscaTarefa", "Tarefa");
		request.setAttribute("tempRetorno", "TarefaFinalizar");

		Tarefane = (TarefaNe) request.getSession().getAttribute("Tarefane");
		if (Tarefane == null)
			Tarefane = new TarefaNe();

		Tarefadt = (TarefaDt) request.getSession().getAttribute("Tarefadt");
		if (Tarefadt == null)
			Tarefadt = new TarefaDt();

		Tarefadt.setId(request.getParameter("Id_Tarefa"));
		Tarefadt.setTarefa(request.getParameter("Tarefa"));
		Tarefadt.setDescricao(request.getParameter("Descricao"));
		Tarefadt.setResposta(request.getParameter("Resposta"));
		Tarefadt.setDataInicio(request.getParameter("DataInicio"));
		Tarefadt.setPrevisao(request.getParameter("Previsao"));
		Tarefadt.setDataFim(request.getParameter("DataFim"));

		// ---- auto-relacionamento com tarefa-pai
		Tarefadt.setId_TarefaPai(request.getParameter("Id_TarefaPai"));
		Tarefadt.setTarefaPai(request.getParameter("TarefaPai"));

		Tarefadt.setPontosApf(request.getParameter("PontosApf"));
		Tarefadt.setPontosApg(request.getParameter("PontosApg"));
		Tarefadt.setId_TarefaPrioridade(request.getParameter("Id_TarefaPrioridade"));
		Tarefadt.setTarefaPrioridade(request.getParameter("TarefaPrioridade"));

		Tarefadt.setId_TarefaStatus(request.getParameter("Id_TarefaStatus"));
		Tarefadt.setTarefaStatus(request.getParameter("TarefaStatus"));

		Tarefadt.setId_TarefaTipo(request.getParameter("Id_TarefaTipo"));
		Tarefadt.setTarefaTipo(request.getParameter("TarefaTipo"));
		Tarefadt.setId_Projeto(request.getParameter("Id_Projeto"));
		Tarefadt.setProjeto(request.getParameter("Projeto"));
		Tarefadt.setId_ProjetoParticipanteResponsavel(request.getParameter("Id_ProjetoParticipante"));
		Tarefadt.setProjetoParticipanteResponsavel(request.getParameter("ProjetoParticipante"));
		Tarefadt.setId_UsuarioCriador(request.getParameter("Id_UsuarioCriador"));
		Tarefadt.setUsuarioCriador(request.getParameter("UsuarioCriador"));
		Tarefadt.setId_UsuarioFinalizador(request.getParameter("Id_UsuarioFinalizador"));
		Tarefadt.setUsuarioFinalizador(request.getParameter("UsuarioFinalizador"));

		Tarefadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Tarefadt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
    	String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		switch (paginaatual) {
		case Configuracao.Curinga6: // Abrir tela para visualizar os detalhes de
									// uma tarefa
			stAcao = "/WEB-INF/jsptjgo/TarefaFinalizar.jsp";
			Tarefadt = Tarefane.consultarId(Tarefadt.getId());
			break;
		case Configuracao.Novo: // Limpar tela:
			Tarefadt.limpar();
			// os dois atributos abaixo foram setados para garantir a paginação.
			request.setAttribute("PosicaoPaginaAtual", new Long(0));
			request.setAttribute("QuantidadePaginas", new Long(0));
			break;
		case Configuracao.Editar:
			request.setAttribute("idProjeto", Tarefadt.getId_Projeto());

			// limpa os parametros do filtro na primeira requisição
			request.setAttribute("TarefaStatus", "");
			request.setAttribute("TarefaPrioridade", "");
			request.setAttribute("Projeto", "");

			tempList = Tarefane.pesquisarTarefaNaoFinalizada(tempNomeBusca, Tarefadt.getId_Projeto(), PosicaoPaginaAtual);
			if (tempList.size() > 0) {
				request.setAttribute("ListaTarefa", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", Tarefane.getQuantidadePaginas());
			} else {
				request.setAttribute("MensagemErro", "Dados Não Localizados");
			}
			break;
		case (ProjetoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Projeto"};
				String[] lisDescricao = {"Projeto"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Projeto");
				request.setAttribute("tempBuscaDescricao", "Projeto");
				request.setAttribute("tempBuscaPrograma", "Projeto");	
				request.setAttribute("tempRetorno","TarefaFinalizar");
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
				request.setAttribute("PaginaAtual", ProjetoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";			
				stTemp = Tarefane.consultarDescricaoProjetoJSON(stNomeBusca1, PosicaoPaginaAtual);			
				enviarJSON(response, stTemp);								
				return;			
			}
			break;
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}

		request.getSession().setAttribute("Tarefadt", Tarefadt);
		request.getSession().setAttribute("Tarefane", Tarefane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}