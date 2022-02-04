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
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.ne.RecursoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla a alteração de partes de recursos inominados.
 *  
 * @author msapaula
 *
 */
public class RecursoParteCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = 2378840777156802255L;

    public int Permissao() {
		return RecursoParteDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RecursoDt Recursodt;
		RecursoParteNe RecursoPartene;

		String stAcao = "/WEB-INF/jsptjgo/RecursoParte.jsp";
		request.setAttribute("tempPrograma", "Recurso");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		RecursoPartene = (RecursoParteNe) request.getSession().getAttribute("RecursoPartene");
		if (RecursoPartene == null) RecursoPartene = new RecursoParteNe();

		Recursodt = (RecursoDt) request.getSession().getAttribute("Recursodt");
		if (Recursodt == null) Recursodt = new RecursoDt();

		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt != null && processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
			Recursodt = processoDt.getRecursoDt();
		}
		
		Recursodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Recursodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		//Captura a classe selecionada
		Recursodt.setId_ProcessoTipoRecursoParteAtual(request.getParameter("Id_ProcessoTipo"));
		Recursodt.setProcessoTipoRecursoParteAtual(request.getParameter("ProcessoTipo"));
				
		if (Recursodt.getId_ProcessoTipoRecursoParteAtual() == null || Recursodt.getId_ProcessoTipoRecursoParteAtual().trim().length() == 0) {
			// Não passou um id de recurso adicional iremos obter o Recurso principal
			Recursodt.setId_ProcessoTipoRecursoParteAtual(Recursodt.getId_ProcessoTipo());
			Recursodt.setProcessoTipoRecursoParteAtual(Recursodt.getProcessoTipo());		
		}
		
		//Captura partes selecionadas
		List listaRecorrentesEditada = getPartesRecorrentes(request, Recursodt, processoDt);
		List listaRecorridosEditada = getPartesRecorridas(request, Recursodt, processoDt);

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
					//Joga listas atuais na sessão
					listaRecorrentesEditada = new ArrayList();
					if(Recursodt.getListaRecorrentes() != null){
						listaRecorrentesEditada.addAll(Recursodt.getListaRecorrentesAtivos(Recursodt.getId_ProcessoTipoRecursoParteAtual()));
					}
					listaRecorridosEditada = new ArrayList();
					if(Recursodt.getListaRecorridos() != null){
						listaRecorridosEditada.addAll(Recursodt.getListaRecorridosAtivos(Recursodt.getId_ProcessoTipoRecursoParteAtual()));
					}
				} else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=Opção Disponível somente para Recurso.");
					return;
				}
				break;

			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para confirmar a alteração da(s) parte(s)");
				break;

			case Configuracao.SalvarResultado:
				List listaRecorrentesAnterior = Recursodt.getListaRecorrentesAtivos(Recursodt.getId_ProcessoTipoRecursoParteAtual());
				List listaRecorridosAnterior = Recursodt.getListaRecorridosAtivos(Recursodt.getId_ProcessoTipoRecursoParteAtual());
				
				if (listaRecorrentesEditada.size() > 0 && listaRecorridosEditada.size() > 0) {
					Recursodt.setListaRecorrentes(listaRecorrentesEditada);
					Recursodt.setListaRecorridos(listaRecorridosEditada);

					RecursoPartene.alterarPartesRecurso(Recursodt, listaRecorrentesAnterior, listaRecorridosAnterior, Recursodt.getId_ProcessoTipoRecursoParteAtual());
					limparListas(request);
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Parte(s) alterada(s) com sucesso.");
					return;

				} else {
					request.setAttribute("MensagemErro", "Selecione as partes do recurso");
					Recursodt.setListaRecorrentes(listaRecorrentesAnterior);
					Recursodt.setListaRecorridos(listaRecorridosAnterior);
				}

				request.getSession().removeAttribute("ListaRecorrentes");
				request.getSession().removeAttribute("ListaRecorridos");
				break;
				
				// Consultar tipos de processo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe", "Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPermissao = String.valueOf((ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "Classe", "RecursoParte", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
				} else {
					String stTemp = "";
					
					stTemp = RecursoPartene.consultarProcessoTipoServentiaRecursoJSON(stNomeBusca1, Recursodt.getId(), false, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}			
			break;

		}
		
		// Atualiza as partes selecionadas
		if (request.getParameter("PaginaAnterior") != null && 
			Funcoes.StringToInt(request.getParameter("PaginaAnterior")) == (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar) &&
			Recursodt != null && Recursodt.getId().length() > 0) {
			//Joga listas atuais na sessão
			listaRecorrentesEditada = new ArrayList();
			if(Recursodt.getListaRecorrentes() != null){
				listaRecorrentesEditada.addAll(Recursodt.getListaRecorrentesAtivos(Recursodt.getId_ProcessoTipoRecursoParteAtual()));
			}
			listaRecorridosEditada = new ArrayList();
			if(Recursodt.getListaRecorridos() != null){
				listaRecorridosEditada.addAll(Recursodt.getListaRecorridosAtivos(Recursodt.getId_ProcessoTipoRecursoParteAtual()));
			}
		}

		request.getSession().setAttribute("ListaRecorrentes", listaRecorrentesEditada);
		request.getSession().setAttribute("ListaRecorridos", listaRecorridosEditada);
		request.getSession().setAttribute("Recursodt", Recursodt);
		request.getSession().setAttribute("RecursoPartene", RecursoPartene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Após autuação ou alteração limpa listas da sessão
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
