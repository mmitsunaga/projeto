package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para controlar as modificações de dados dos processos, valendo para
 * os os processos cíveis, criminais e de segundo grau. Foi cridado pois o
 * ProcessoCt já estava completo.
 * 
 * @author hmgodinho
 * 
 */
public class Processo2Ct extends ProcessoCtGen {

	private static final long serialVersionUID = -6841031865918102194L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt;
		ProcessoNe processoNe;

		int passoEditar = -1;
		String posicaoLista = "";
		String stAcao = "";

		String novoProcessoStatus = "";

		request.setAttribute("tempBuscaId_Processo", "Id_Processo");
		request.setAttribute("tempBuscaProcessoNumero", "ProcessoNumero");
		request.setAttribute("tempRetorno", "Processo2");

		processoNe = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (processoNe == null)
			processoNe = new ProcessoNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

		processoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		processoDt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		processoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		processoDt.setId_ProcessoFase(request.getParameter("Id_ProcessoFase"));
		processoDt.setProcessoFase(request.getParameter("ProcessoFase"));
		processoDt.setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
		processoDt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		processoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		processoDt.setClassificador(request.getParameter("Classificador"));
		processoDt.setValor(request.getParameter("Valor"));
		processoDt.setId_Assunto(request.getParameter("Id_Assunto"));
		processoDt.setAssunto(request.getParameter("Assunto"));
		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		processoDt.setDataTransitoJulgado(request.getParameter("DataTransitoJulgado"));
		processoDt.setTcoNumero(request.getParameter("TcoNumero"));

		if (request.getParameter("PaginaAnterior") != null)
			Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null)
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		posicaoLista = request.getParameter("posicaoLista");

		stAcao = "/WEB-INF/jsptjgo/Processo.jsp";

		if (Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU)
			request.setAttribute("tempPrograma", "Processo Segundo Grau");
		else
			request.setAttribute("tempPrograma", "Processo Primeiro Grau");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("posicaoLista", posicaoLista);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("Id_AreaDistribuicao", processoDt.getId_AreaDistribuicao());
		request.setAttribute("Id_Serventia", processoDt.getId_Serventia());
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

		// Salvar alterações do status do Processo
		case Configuracao.SalvarResultado:
			novoProcessoStatus = request.getParameter("NovoProcessoStatus").toString();

			// Salva novo status do processo
			processoNe.alterarStatusProcesso(processoDt, Funcoes.StringToInt(novoProcessoStatus));

			// Volta para tela do processo
			request.getSession().removeAttribute("processoDt");
			redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Dados atualizados com sucesso.");
			break;

		//Abre tela de confirmação e prepara para salvar 
		case Configuracao.Salvar:
			novoProcessoStatus = request.getParameter("NovoProcessoStatus").toString();
			if (novoProcessoStatus.equals(String.valueOf(ProcessoStatusDt.ATIVO_PROVISORIAMENTE))) {
				request.setAttribute("NovoProcessoStatus", novoProcessoStatus);
				request.setAttribute("Mensagem", "Clique para confirmar a Ativação Provisória do Processo");
				stAcao = "/WEB-INF/jsptjgo/ProcessoAtivarProvisoriamente.jsp";
			} else if (novoProcessoStatus.equals(String.valueOf(ProcessoStatusDt.ARQUIVADO))) {
				request.setAttribute("NovoProcessoStatus", novoProcessoStatus);
				request.setAttribute("Mensagem", "Clique para confirmar a Desativação do Processo");
				stAcao = "/WEB-INF/jsptjgo/ProcessoDesativar.jsp";
			}
			break;

		// Curinga para Ativar Provisoriamente / Desativar um processo
		case Configuracao.Curinga6:
			novoProcessoStatus = request.getParameter("NovoProcessoStatus").toString();

			if (novoProcessoStatus.equals(String.valueOf(ProcessoStatusDt.ATIVO_PROVISORIAMENTE))) {
				// Para ativar um processo provisoriamente, ele precisa estar
				// arquivado
				if (processoDt.getProcessoStatusCodigo().equals(String.valueOf(ProcessoStatusDt.ARQUIVADO))) {
					request.setAttribute("NovoProcessoStatus", novoProcessoStatus);
					stAcao = "/WEB-INF/jsptjgo/ProcessoAtivarProvisoriamente.jsp";
				} else {
					request.getSession().removeAttribute("processoDt");
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=O processo precisa estar arquivado para ser ativado provisoriamente.");
				}
			} else if (novoProcessoStatus.equals(String.valueOf(ProcessoStatusDt.ARQUIVADO))) {
				// Para ativar um processo provisoriamente, ele precisa estar
				// ativo provisoriamente
				if (processoDt.getProcessoStatusCodigo().equals(String.valueOf(ProcessoStatusDt.ATIVO_PROVISORIAMENTE))) {
					request.setAttribute("NovoProcessoStatus", novoProcessoStatus);
					stAcao = "/WEB-INF/jsptjgo/ProcessoDesativar.jsp";
				} else {
					request.getSession().removeAttribute("processoDt");
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=O processo precisa estar ativado provisoriamente para ser desativado.");
				}
			}
			break;

		}

		request.setAttribute("PassoEditar", passoEditar);
		request.getSession().setAttribute("processoDt", processoDt);
		request.getSession().setAttribute("Processone", processoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
