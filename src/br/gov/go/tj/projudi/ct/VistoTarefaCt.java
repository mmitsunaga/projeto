package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import br.gov.go.tj.projudi.ne.TarefaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

@SuppressWarnings("unchecked")
public class VistoTarefaCt extends Controle {

	private static final long serialVersionUID = -4581837040631380682L;

	@Override
	public int Permissao() {
		return TarefaDt.CodigoPermissaoVistarTarefa;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TarefaDt Tarefadt;
		TarefaNe Tarefane;		
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		
		String Mensagem = "";
		String stAcao = "";

		request.setAttribute("tempPrograma", "VistoTarefa");		

		request.setAttribute("tempBuscaId_Tarefa", "Id_Tarefa");
		request.setAttribute("tempBuscaTarefa", "Tarefa");
		request.setAttribute("tempRetorno", "VistoTarefa");

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
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		// é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {			
			case Configuracao.Salvar: // Salvar o visto da tarefa
				stAcao = "/WEB-INF/jsptjgo/VistoTarefa.jsp";
				break;
	
			case Configuracao.Novo: // Limpar tela:
				Tarefadt.limpar();
				// os dois atributos abaixo foram setados para garantir a paginação.
				request.setAttribute("PosicaoPaginaAtual", new Long(0));
				request.setAttribute("QuantidadePaginas", new Long(0));
				
				if (processaPesquisa(request, response, Tarefane, stNomeBusca1, stNomeBusca2, UsuarioSessao, PosicaoPaginaAtual))
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				else
					return;
				break;
			case Configuracao.Editar:
				stAcao = "/WEB-INF/jsptjgo/VistoTarefa.jsp";
				Tarefadt = Tarefane.consultarId(Tarefadt.getId());
				break;
			case Configuracao.SalvarResultado: // Salvar resultado do visto da tarefa:
				Mensagem = Tarefane.Verificar(Tarefadt);
				if (Mensagem.length() == 0) {
					Tarefane.alterarStatusTarefa(Tarefadt, TarefaStatusDt.FINALIZADAS.toString());
					request.setAttribute("MensagemOk", "Tarefa finalizada com sucesso.");
					stAcao = "/WEB-INF/jsptjgo/VistoTarefa.jsp";
					Tarefadt.limpar();
				} else {
					request.setAttribute("MensagemErro", Mensagem);
					stAcao = "/WEB-INF/jsptjgo/VistoTarefa.jsp";
				}
				break;		
			case Configuracao.Localizar:
				if (processaPesquisa(request, response, Tarefane, stNomeBusca1, stNomeBusca2, UsuarioSessao, PosicaoPaginaAtual))
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				else
					return;
				break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
		}

		request.setAttribute("idUsuarioCriador", UsuarioSessao.getUsuarioDt().getId());
		request.getSession().setAttribute("Tarefadt", Tarefadt);
		request.getSession().setAttribute("Tarefane", Tarefane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private boolean processaPesquisa(HttpServletRequest request, HttpServletResponse response, TarefaNe Tarefane, String stNomeBusca1, String stNomeBusca2, UsuarioNe UsuarioSessao, String PosicaoPaginaAtual) throws Exception
	{
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		
		if (request.getParameter("Passo") == null) {
			//lisNomeBusca = new ArrayList();
			//lisDescricao = new ArrayList();
			
			String[] lisNomeBusca = {"Projeto","Tarefa"};			
			String[] lisDescricao = {"Tarefa","Situação","Prioridade","Projeto"};		
			
			request.setAttribute("tempBuscaId", "Id_Tarefa");
			request.setAttribute("tempBuscaDescricao", "Tarefa");
			request.setAttribute("tempBuscaPrograma", "Tarefa");
			request.setAttribute("tempRetorno", "VistoTarefa");
			request.setAttribute("tempDescricaoId", "Id");
			request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
			request.setAttribute("PaginaAtual", (Configuracao.Localizar));
			request.setAttribute("PosicaoPaginaAtual", "0");
			request.setAttribute("QuantidadePaginas", "0");
			request.setAttribute("lisNomeBusca", lisNomeBusca);
			request.setAttribute("lisDescricao", lisDescricao);
			
			return true;
		} else {
			String stTemp = "";
			request.setAttribute("tempRetorno", "VistoTarefa");

			stTemp = Tarefane.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, UsuarioSessao.getUsuarioDt().getId(), PosicaoPaginaAtual);
							
			enviarJSON(response, stTemp);

			return false;
		}
	}
}