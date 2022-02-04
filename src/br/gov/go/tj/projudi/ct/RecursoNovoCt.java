package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoNovoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.ne.RecursoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla a inclusão de novos recursos.
 *  
 * @author mmgomes
 *
 */
public class RecursoNovoCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = 2378840777156802255L;

    public int Permissao() {
		return RecursoParteDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RecursoDt Recursodt;
		RecursoNovoDt RecursoNovodt;
		RecursoParteNe RecursoPartene;

		String stAcao = "/WEB-INF/jsptjgo/RecursoNovo.jsp";
		request.setAttribute("tempPrograma", "RecursoNovo");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		RecursoPartene = (RecursoParteNe) request.getSession().getAttribute("RecursoPartene");
		if (RecursoPartene == null) RecursoPartene = new RecursoParteNe();

		Recursodt = (RecursoDt) request.getSession().getAttribute("Recursodt");
		if (Recursodt == null) Recursodt = new RecursoDt();
		
		RecursoNovodt = (RecursoNovoDt) request.getSession().getAttribute("RecursoNovodt");
		if (RecursoNovodt == null) RecursoNovodt = new RecursoNovoDt();

		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt != null && processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
			Recursodt = processoDt.getRecursoDt();
		}

		Recursodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Recursodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		RecursoNovodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RecursoNovodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		//Captura a classe selecionada
		RecursoNovodt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		RecursoNovodt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		RecursoNovodt.setProcessoTipoCodigo(request.getParameter("ProcessoTipoCodigo"));
		
		//Captura partes selecionadas
		if (request.getParameter("PaginaAnterior") != null && Funcoes.StringToInt(request.getParameter("PaginaAnterior")) != (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			Recursodt.setListaRecorrentes(getPartesRecorrentes(request, Recursodt, processoDt));
			Recursodt.setListaRecorridos(getPartesRecorridas(request, Recursodt, processoDt));
		}
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa dados
			case Configuracao.Novo:
				
				//Se foi passado Id_Recurso refere-se a alteração de dados
				if (Recursodt != null && Recursodt.getId().length() > 0) {
					//Recupera dados de recurso que está no objeto Processo da sessão
					Recursodt = processoDt.getRecursoDt();
					Recursodt.getListaRecorrentes().clear();
					Recursodt.getListaRecorridos().clear();					
					RecursoNovodt.Limpar();
				} else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=Opção Disponível somente para Recurso.");
					return;
				}
				break;

			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para confirmar a inclusão do novo recurso");
				break;

			case Configuracao.SalvarResultado:
				String mensagem = RecursoPartene.verificarAdicionarRecursoSegundoGrau(Recursodt, RecursoNovodt.getId_ProcessoTipo());
				if (mensagem.trim().length() == 0) {
					RecursoPartene.adicionarRecursoSegundoGrau(Recursodt, RecursoNovodt, UsuarioSessao.getUsuarioDt());
					limparListas(request);
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Recurso incluído com sucesso.");
					return;
				} else {
					request.setAttribute("MensagemErro", mensagem);	
				}
				break;
				
				// Consultar tipos de processo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe", "Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPermissao = String.valueOf((ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "Classe", "RecursoNovo", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
				} else {
					String stTemp = "";
					stTemp = RecursoPartene.consultarDescricaoProcessoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);
					return;
				}			
			break;

		}

		request.setAttribute("ListaRecorrentes", Recursodt.getListaRecorrentes());
		request.setAttribute("ListaRecorridos", Recursodt.getListaRecorridos());
		request.getSession().setAttribute("Recursodt", Recursodt);
		request.getSession().setAttribute("RecursoNovodt", RecursoNovodt);
		request.getSession().setAttribute("RecursoPartene", RecursoPartene);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Após ação limpa listas da sessão
	 * @param request
	 */
	protected void limparListas(HttpServletRequest request) {
		request.getSession().removeAttribute("ListaRecorrentes");
		request.getSession().removeAttribute("ListaRecorridos");
	}

	/**
	 * Captura as partes recorrentes selecionadas pelo usuário
	 */
	protected List getPartesRecorrentes(HttpServletRequest request, RecursoDt recursoDt, ProcessoDt processoDt) {
		List liTemp = null;
		String[] recorrentes = request.getParameterValues("Recorrente");
		if (recorrentes != null) {
			liTemp = montaListaPartes(recorrentes, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, processoDt);
			if (recursoDt.getDataRecebimento() == null || recursoDt.getDataRecebimento().equals("")) recursoDt.setListaRecorrentes(liTemp);
		} else liTemp = new ArrayList();
		return liTemp;

	}

	/**
	 * Captura as partes recorridas selecionadas pelo usuário
	 */
	protected List getPartesRecorridas(HttpServletRequest request, RecursoDt recursoDt, ProcessoDt processoDt) {
		List liTemp = null;
		String[] recorridos = request.getParameterValues("Recorrido");
		if (recorridos != null) {
			liTemp = montaListaPartes(recorridos, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, processoDt);
			if (recursoDt.getDataRecebimento() == null || recursoDt.getDataRecebimento().equals("")) recursoDt.setListaRecorridos(liTemp);
		} else liTemp = new ArrayList();
		return liTemp;
	}

	/**
	 * Captura partes selecionadas
	 * @param partes
	 * @param processoDt
	 */
	protected List montaListaPartes(String[] partes, int processoParteTipo, ProcessoDt processoDt) {
		List partesSelecionadas = new ArrayList();
		//Para cada parte selecionada pelo usuário, busca os dados dessa no processo de 1º grau
		for (int i = 0; i < partes.length; i++) {
			List lista = processoDt.getPartesProcesso();
			//Para cada parte do processo selecionada, gera um objeto RecursoParte
			for (int j = 0; j < lista.size(); j++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) lista.get(j);
				if (parteDt.getId_ProcessoParte().equals(partes[i])) {
					RecursoParteDt recursoParteDt = new RecursoParteDt();
					recursoParteDt.setId_ProcessoParte(parteDt.getId());
					recursoParteDt.setProcessoParteTipoCodigo(String.valueOf(processoParteTipo));
					partesSelecionadas.add(recursoParteDt);
					break;
				}
			}
		}
		return partesSelecionadas;
	}

}
