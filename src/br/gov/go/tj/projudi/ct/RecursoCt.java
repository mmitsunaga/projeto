package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoAssuntoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.RecursoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla a modificação de dados e autuação de recursos.
 * 
 * @author msapaula
 *
 */
public class RecursoCt extends RecursoCtGen {

	private static final long serialVersionUID = 4049311005847580547L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RecursoDt Recursodt;
		RecursoNe Recursone;

		List tempList = null;
		String Mensagem = "";
		String stId = "";
		int passoEditar = -1;
		int paginaAnterior = 0;		
		String posicaoLista = "";
		String stAcao = "";
		String stSemParte = "checked=checked";
		
		request.setAttribute("tempPrograma", "Recurso");	
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));		

		Recursone = (RecursoNe) request.getSession().getAttribute("Recursone");
		if (Recursone == null) Recursone = new RecursoNe();

		Recursodt = (RecursoDt) request.getSession().getAttribute("Recursodt");
		if (Recursodt == null) Recursodt = new RecursoDt();

		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt != null && processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
			Recursodt = processoDt.getRecursoDt();
			Recursodt.setProcessoDt(processoDt);
		}

		if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) {
			stAcao = "/WEB-INF/jsptjgo/RecursoAutuar.jsp";
		} else stAcao = "/WEB-INF/jsptjgo/Recurso.jsp";	

		Recursodt.setId_Processo(request.getParameter("Id_Processo"));
		Recursodt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		Recursodt.setId_ServentiaOrigem(request.getParameter("Id_ServentiaOrigem"));
		Recursodt.setServentiaOrigem(request.getParameter("ServentiaOrigem"));
		Recursodt.setId_ServentiaRecurso(request.getParameter("Id_ServentiaRecurso"));
		Recursodt.setServentiaRecurso(request.getParameter("ServentiaRecurso"));
		Recursodt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		Recursodt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		Recursodt.setDataEnvio(request.getParameter("DataEnvio"));
		Recursodt.setDataRecebimento(request.getParameter("DataRecebimento"));
		Recursodt.setDataRetorno(request.getParameter("DataRetorno"));
		Recursodt.setId_Assunto(request.getParameter("Id_Assunto"));
		Recursodt.setAssunto(request.getParameter("Assunto"));		
		if (request.getParameter("Id_Assunto") != null && request.getParameter("Assunto") != null) {
			if (Recursodt.getSemParte() != null && Recursodt.getSemParte().trim().equalsIgnoreCase("0")) stSemParte="";
		} else if (request.getParameter("semParte")== null || !request.getParameter("semParte").equalsIgnoreCase("1")){
			stSemParte="";	
			Recursodt.setSemParte("0");
		} else{
			Recursodt.setSemParte("1");			
		}
		
		if (Recursodt.getProcessoDt() != null) {
			Recursodt.getProcessoDt().setId_ProcessoFase(request.getParameter("Id_ProcessoFase"));
			Recursodt.getProcessoDt().setProcessoFase(request.getParameter("ProcessoFase"));
			Recursodt.getProcessoDt().setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
			Recursodt.getProcessoDt().setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
			Recursodt.getProcessoDt().setId_Classificador(request.getParameter("Id_Classificador"));
			Recursodt.getProcessoDt().setClassificador(request.getParameter("Classificador"));
			Recursodt.getProcessoDt().setValor(request.getParameter("Valor"));
		}
		
		Recursodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Recursodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		
		
		posicaoLista = request.getParameter("posicaoLista");

		List listaAssuntosEditada = (List) request.getSession().getAttribute("ListaAssuntosRecurso");
		if (listaAssuntosEditada == null) listaAssuntosEditada = new ArrayList();

		List listaRecorrentesEditada = getPartesRecorrentes(request, Recursodt, paginaatual);
		List listaRecorridosEditada = getPartesRecorridas(request, Recursodt, paginaatual);
		this.atualizaAssuntosRecurso(request, Recursodt, paginaAnterior, listaAssuntosEditada);

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa dados
			case Configuracao.Novo:
				stId = request.getParameter("Id_Recurso");
				//Se foi passado Id_Recurso refere-se a alteração de dados
				if (stId != null && !stId.equalsIgnoreCase("")) {
					//Recupera dados de recurso que está no objeto Processo da sessão
					Recursodt = processoDt.getRecursoDt();
					//Joga listas atuais na sessão
					listaAssuntosEditada = new ArrayList();
					listaAssuntosEditada.addAll(Recursodt.getListaAssuntos());

				} else if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) {
					//Refere-se ao início de autuação
					Recursodt.setListaRecorrentes(null);
					Recursodt.setListaRecorridos(null);
					Recursodt.setListaAssuntos(null);
					listaAssuntosEditada = null;
					listaRecorrentesEditada = null;
					listaRecorridosEditada = null;
					Recursodt.limpar();
				}
				break;

			case Configuracao.ExcluirResultado: //Excluir
				Recursone.excluir(Recursodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			//Localiza recursos para autuar
			case Configuracao.Localizar:
				Recursodt.limpar();
				
	        	if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Número do Processo:"};
					String[] lisDescricao = {"Id Processo", "Número Processo", "Data Envio", "Serventia Origem"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Recurso");
					request.setAttribute("tempBuscaDescricao", "Recurso");
					String[] camposHidden = {"ProcessoNumero"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaPrograma","Recurso");			
					request.setAttribute("tempRetorno","Recurso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					
						
					if(Recursodt != null && Recursodt.getProcessoNumero() != null && Recursodt.getProcessoNumero().length()>0)
						stTemp = Recursone.consultarRecursosAutuarJSON(UsuarioSessao.getUsuarioDt().getId_Serventia(), Recursodt.getProcessoNumero(), PosicaoPaginaAtual);
					else
						stTemp = Recursone.consultarRecursosAutuarJSON(UsuarioSessao.getUsuarioDt().getId_Serventia(), stNomeBusca1, PosicaoPaginaAtual);
					
					
					enviarJSON(response, stTemp);
						
					
					return;								
				}    
				break;

			case Configuracao.Salvar:
				//Se não tem data de recebimento refere-se a autuação
				if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) {
					request.setAttribute("MensagemOk", "Clique para confirmar a Autuação do Recurso");
				}
				
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				
				if (request.getParameter("SegredoJustica") == null) 
					Recursodt.getProcessoDt().setSegredoJustica("false");
				else 
					Recursodt.getProcessoDt().setSegredoJustica(request.getParameter("SegredoJustica"));
				
				if (request.getParameter("Penhora") == null) 
					Recursodt.getProcessoDt().setPenhora("false");
				else 
					Recursodt.getProcessoDt().setPenhora(request.getParameter("Penhora"));
				
				break;

			case Configuracao.SalvarResultado:
				List listaAssuntosAnterior = Recursodt.getListaAssuntos();
				Recursodt.setListaAssuntos(listaAssuntosEditada);

				//Se não tem data de recebimento refere-se a autuação
				Mensagem = Recursone.Verificar(Recursodt);
				if (Mensagem.length() == 0) {
					if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) {
						//Recursodt.setListaAssuntos(listaAssuntosEditada);
						Recursone.salvarAutuacaoRecurso(Recursodt, UsuarioSessao.getUsuarioDt());
						request.getSession().removeAttribute("processoDt");
						limparListas(request);
						redireciona(response, "BuscaProcesso?Id_Processo=" + Recursodt.getProcessoDt().getId() + "&MensagemOk=Recurso Autuado com sucesso.");
						return;
					} else {
						Recursone.alterarDadosRecurso(Recursodt, listaAssuntosAnterior, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
						limparListas(request);
						redireciona(response, "BuscaProcesso?Id_Processo=" + Recursodt.getProcessoDt().getId() + "&MensagemOk=Dados atualizados com sucesso.");
						return;
					}
				} else {
					request.setAttribute("MensagemErro", Mensagem);
					if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) stAcao = "/WEB-INF/jsptjgo/RecursoAutuar.jsp";
					else stAcao = "/WEB-INF/jsptjgo/Recurso.jsp";
					Recursodt.setListaAssuntos(listaAssuntosAnterior);
				}

				request.getSession().removeAttribute("ListaAssuntosRecurso");
				request.getSession().removeAttribute("ListaRecorrentes");
				request.getSession().removeAttribute("ListaRecorridos");
				break;

			//Inicia autuação de recurso
			case Configuracao.Curinga6:
				stId = request.getParameter("Id_Recurso");
				Mensagem = request.getParameter("MensagemOk");
				request.setAttribute("MensagemOk",Mensagem);
				
				stAcao = "/WEB-INF/jsptjgo/RecursoAutuar.jsp";
				Recursodt.setListaRecorrentes(null);
				Recursodt.setListaRecorridos(null);
				Recursodt.setListaAssuntos(null);
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Recursodt.getId())) {
					Recursodt.limpar();
					Recursodt = Recursone.consultarId(stId);
					Recursodt.setProcessoDt(Recursone.consultarProcessoIdCompleto(Recursodt.getId_Processo()));
					getAssuntosProcessoOriginario(Recursodt, listaAssuntosEditada, Recursone);
				}
				break;
			case Configuracao.Curinga7:
				switch (passoEditar) {
					//Redireciona para tela de confirmação de conversao de recurso em processo
					case 1:
	 					stAcao = "/WEB-INF/jsptjgo/RecursoConfirmarConversao.jsp";
																
						if (!UsuarioSessao.verificarPedido(request.getParameter("__Pedido__"))){	                
		                    throw new ServletException("<{Pedido enviado mais de um vez.}> Local Exception: " + this.getClass().getName() + ".doPost()");
		                }
						
						Recursone.ConverterRecursoProcesso(Recursodt, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), Recursodt.getId_ServentiaRecurso() , UsuarioSessao.getUsuarioDt().getIpComputadorLog(), UsuarioSessao.getId_Usuario()  );
						request.setAttribute("MensagemOk", "Recurso Convertido em Processo");
						
						redireciona(response, "BuscaProcesso?Id_Processo=" + Recursodt.getId_Processo() + "&PassoBusca=2");
						
						break;
			
	
					default:
						
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido());					
						stAcao = "/WEB-INF/jsptjgo/RecursoConfirmarConversao.jsp";										
						
						stId = request.getParameter("Id_Recurso");
						
				}
				
				break;
				// Consultar tipos de processo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe", "Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPermissao = String.valueOf((ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "Classe", "Recurso", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
				} else {
					String stTemp = "";
					
					stTemp = Recursone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt().getId_Serventia() ,PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}			
			break;
			
			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Assunto"};
					String[] lisDescricao = {"Assunto","Pai","Disp. Legal"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Assunto");
					request.setAttribute("tempBuscaDescricao","Assunto");
					request.setAttribute("tempBuscaPrograma","Assunto");			
					request.setAttribute("tempRetorno","Recurso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = -1;
					break;
				} else {
					String stTemp = "";
					
					if (Recursodt.getProcessoDt() != null && Recursodt.getProcessoDt().getId_Serventia().length() > 0) 						
						stTemp = Recursone.consultarAssuntosServentiaJSON(stNomeBusca1, Recursodt.getProcessoDt().getId_Serventia(), PosicaoPaginaAtual);
					else {
						request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
					}
							
					
					enviarJSON(response, stTemp);
						
					
					return;								
				}
				
			// Consultar fases de processo
			case (ProcessoFaseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				//Quando o processo estiver na fase Recurso não deve permitir que o usuário altere
				if (Recursodt.getProcessoFaseCodigoToInt() == ProcessoFaseDt.RECURSO) {
					request.setAttribute("MensagemErro", "Fase Processual não pode ser modificada.");
				} else if (request.getParameter("Passo")==null) {
					String[] lisNomeBusca = {"ProcessoFase"};
					String[] lisDescricao = {"ProcessoFase"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoFase");
					request.setAttribute("tempBuscaDescricao","ProcessoFase");
					request.setAttribute("tempBuscaPrograma","ProcessoFase");			
					request.setAttribute("tempRetorno","Recurso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoFaseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp="";
					stTemp = Recursone.consultarDescricaoProcessoFaseJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			// Consultar prioridades de processo
			case (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoPrioridade"};
					String[] lisDescricao = {"ProcessoPrioridade", "Codigo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaDescricao","ProcessoPrioridade");
					request.setAttribute("tempBuscaPrograma","ProcessoPrioridade");			
					request.setAttribute("tempRetorno","Recurso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Recursone.consultarDescricaoProcessoPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
				
			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";						
					
					String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "Recurso", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					
					break;
				} else{
					String stTemp="";
					stTemp = Recursone.consultarDescricaoClassificadorJSON(tempNomeBusca, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}				

			default:

				switch (passoEditar) {
					//Redireciona para tela de Autuação de recurso
					case 1:
						stAcao = "/WEB-INF/jsptjgo/RecursoAutuar.jsp";
						break;

					//Remover assuntos do recurso
					case 2:
						removerAssuntoProcesso(listaAssuntosEditada, posicaoLista);
						if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) {
							Recursodt.setListaAssuntos(listaAssuntosEditada);
						}
						break;

					default:

						//Se chegou aqui a lista de recorrentes e recorridos é capturada da sessão
						listaRecorrentesEditada = (List) request.getSession().getAttribute("ListaRecorrentes");
						listaRecorridosEditada = (List) request.getSession().getAttribute("ListaRecorridos");
						stId = request.getParameter("Id_Recurso");

						if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Recursodt.getId())) {
							Recursodt.limpar();
							Recursodt = Recursone.consultarId(stId);
						}
				}
				break;
		}
			
		request.setAttribute("semParte", stSemParte);
		request.setAttribute("PassoEditar", passoEditar);		
		request.getSession().setAttribute("ListaAssuntosRecurso", listaAssuntosEditada);
		request.getSession().setAttribute("ListaRecorrentes", listaRecorrentesEditada);
		request.getSession().setAttribute("ListaRecorridos", listaRecorridosEditada);
		request.getSession().setAttribute("Recursodt", Recursodt);
		request.getSession().setAttribute("Recursone", Recursone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	public void atualizaAssuntosRecurso(HttpServletRequest request, RecursoDt Recursodt, int paginaAnterior, List listaAssuntosEditada) {
		//Quando um assunto é selecionado já insere na lista de assuntos
		if (!Recursodt.getId_Assunto().equals("") && paginaAnterior == (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			this.adicionarAssuntoProcesso(Recursodt, listaAssuntosEditada, request);
			if (Recursodt.getDataRecebimento() == null || Recursodt.getDataRecebimento().equals("")) {
				//stAcao = "/WEB-INF/jsptjgo/RecursoAutuar.jsp";
				Recursodt.setListaAssuntos(listaAssuntosEditada);
			}// else stAcao = "/WEB-INF/jsptjgo/Recurso.jsp";
		}
	}

	/**
	 * Após autuação ou alteração limpa listas da sessão
	 * @param request
	 */
	protected void limparListas(HttpServletRequest request) {
		request.getSession().removeAttribute("ListaAssuntosRecurso");
		request.getSession().removeAttribute("ListaRecorrentes");
		request.getSession().removeAttribute("ListaRecorridos");
	}

	/**
	 * Captura os assuntos do processo e adiciona ao recurso
	 * @throws Exception 
	 */
	protected void getAssuntosProcessoOriginario(RecursoDt recursodt, List listaAssuntosEditada, RecursoNe recursoNe) throws Exception {
		RecursoAssuntoDt recursoAssuntoDt = null;

		//Consulta assuntos do processo de 1º grau
		if (recursodt.getProcessoDt().getListaAssuntos() == null) {
			recursodt.getProcessoDt().setListaAssuntos(recursoNe.getAssuntosProcesso(recursodt.getProcessoDt().getId()));
		}
		List assuntosProcesso = recursodt.getProcessoDt().getListaAssuntos();

		for (int i = 0; i < assuntosProcesso.size(); i++) {
			ProcessoAssuntoDt processoAssuntoDt = (ProcessoAssuntoDt) assuntosProcesso.get(i);
			recursoAssuntoDt = new RecursoAssuntoDt();
			recursoAssuntoDt.setId_Recurso(recursodt.getId());
			recursoAssuntoDt.setId_Assunto(processoAssuntoDt.getId_Assunto());
			recursoAssuntoDt.setAssunto(processoAssuntoDt.getAssunto());
			recursodt.addListaAssuntos(recursoAssuntoDt);
			listaAssuntosEditada.add(recursoAssuntoDt);
		}
	}

	/**
	 * Método responsável em adicionar assuntos a um processo
	 */
	protected void adicionarAssuntoProcesso(RecursoDt recursoDt, List listaAssuntosEditada, HttpServletRequest request) {
		if (recursoDt.getId_Assunto().length() > 0) {
			RecursoAssuntoDt recursoAssuntoDt = new RecursoAssuntoDt();
			recursoAssuntoDt.setId_Assunto(recursoDt.getId_Assunto());
			recursoAssuntoDt.setAssunto(recursoDt.getAssunto());
			listaAssuntosEditada.add(recursoAssuntoDt);

			recursoDt.setId_Assunto("null");
			recursoDt.setAssunto("null");
		} else request.setAttribute("MensagemErro", "Selecione um Assunto para ser adicionado.");
	}

	/**
	 * Método responsável em remover um assunto de um processo
	 */
	protected void removerAssuntoProcesso(List listaAssuntosEditada, String posicaoLista) {
		if (posicaoLista != null && posicaoLista.length() > 0) {
			listaAssuntosEditada.remove(Funcoes.StringToInt(posicaoLista));
		}
	}

	
	/** 
	 * Consulta de fases do processo 
	 * @throws Exception 
	 */
	private boolean consultarProcessoFase(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, int paginaatual, RecursoNe neRecurso) throws Exception{
		boolean boRetorno = false;
		//Quando o processo estiver na fase Recurso não deve permitir que o usuário altere
		List tempList = neRecurso.consultarDescricaoProcessoFase(tempNomeBusca, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaProcessoFase", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", neRecurso.getQuantidadePaginas());
			request.setAttribute("tempRetorno", "Recurso");
			request.setAttribute("tempBuscaId_ProcessoFase", "Id_ProcessoFase");
			request.setAttribute("tempBuscaProcessoFase", "ProcessoFase");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhuma fase processual foi localizada.");
		
		return boRetorno;
	}
	
	/** 
	 * Consulta de prioridades possíveis do processo 
	 * @throws Exception 
	 */
	protected boolean consultarProcessoPrioridade(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, int paginaatual, RecursoNe neRecurso) throws Exception {
		boolean boRetorno = false;
		List tempList = neRecurso.consultarDescricaoProcessoPrioridade(tempNomeBusca, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaProcessoPrioridade", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", neRecurso.getQuantidadePaginas());
			request.setAttribute("tempRetorno", "Recurso");
			request.setAttribute("tempBuscaId_ProcessoPrioridade", "Id_ProcessoPrioridade");
			request.setAttribute("tempBuscaProcessoPrioridade", "ProcessoPrioridade");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Prioridade foi localizado.");
		return boRetorno;
	}
	
	/**
	 * Captura as partes recorrentes selecionadas pelo usuário
	 * @throws JSONException // necessário para @Override no PJD - jvosantos
	 */
	protected List getPartesRecorrentes(HttpServletRequest request, RecursoDt recursoDt, int paginaatual) throws JSONException {
		List liTemp = null;
		String[] recorrentes = request.getParameterValues("Recorrente");
		if (recorrentes != null) {
			liTemp = getPartesSelecionadas(recorrentes, recursoDt.getProcessoDt());			
			if ((recursoDt.getDataRecebimento() == null || recursoDt.getDataRecebimento().equals("")) && (recursoDt.getSemParte() == null || recursoDt.getSemParte().trim().equalsIgnoreCase("0"))) recursoDt.setListaRecorrentes(liTemp);
			else recursoDt.setListaRecorrentes(null);
		} else {
			liTemp = new ArrayList();
			if (paginaatual == Configuracao.Salvar || paginaatual == Configuracao.SalvarResultado) recursoDt.setListaRecorrentes(null);
		}
		return liTemp;

	}

	/**
	 * Captura as partes recorridas selecionadas pelo usuário
	 * @throws JSONException // necessário para @Override no PJD - jvosantos
	 */
	protected List getPartesRecorridas(HttpServletRequest request, RecursoDt recursoDt, int paginaatual) throws JSONException {
		List liTemp = null;
		String[] recorridos = request.getParameterValues("Recorrido");
		if (recorridos != null) {
			liTemp = getPartesSelecionadas(recorridos, recursoDt.getProcessoDt());
			if ((recursoDt.getDataRecebimento() == null || recursoDt.getDataRecebimento().equals("")) && (recursoDt.getSemParte() == null || recursoDt.getSemParte().trim().equalsIgnoreCase("0"))) recursoDt.setListaRecorridos(liTemp);
			else recursoDt.setListaRecorridos(null);
		} else {
			liTemp = new ArrayList();
			if (paginaatual == Configuracao.Salvar || paginaatual == Configuracao.SalvarResultado) recursoDt.setListaRecorridos(null);
		}
		return liTemp;
	}

	/**
	 * Captura partes selecionadas
	 * @param partes
	 * @param processoDt
	 */
	protected List getPartesSelecionadas(String[] partes, ProcessoDt processoDt) {
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
					recursoParteDt.setNome(parteDt.getNome());
					partesSelecionadas.add(recursoParteDt);
					break;
				}
			}
		}
		return partesSelecionadas;
	}
}
