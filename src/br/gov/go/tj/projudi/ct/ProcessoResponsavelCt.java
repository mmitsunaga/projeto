package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Responsáveis por Processos.
 * 
 * @author msapaula
 * 27/08/2009 09:42:36
 */
public class ProcessoResponsavelCt extends ProcessoResponsavelCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4328653535646972361L;
	
	private static final int PASSO_CONSULTA_RESPONSAVEL_REVISOR = 1;
	private static final int PASSO_CONSULTA_RESPONSAVEL_VOGAL = 2;
	
	private static final String VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL = "Id_ServentiaCargo";
	private static final String VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_REVISOR = "Id_ServentiaCargoRevisor";
	private static final String VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_VOGAL = "Id_ServentiaCargoVogal";
	
	private static final String VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL = "ServentiaCargo";
	private static final String VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL_REVISOR = "ServentiaCargoRevisor";
	private static final String VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL_VOGAL = "ServentiaCargoVogal";

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		ProcessoResponsavelDt ProcessoResponsaveldt;
		ProcessoResponsavelDt ProcessoResponsaveldtRevisor;
		ProcessoResponsavelDt ProcessoResponsaveldtVogal;
		ProcessoResponsavelNe ProcessoResponsavelne;
		MovimentacaoProcessoDt movimentacaoProcessoDt;

		ProcessoDt processoDt = null;
		List tempList = null;
		String Mensagem = "";
		String processos[] = null;
		String stId = "";
		String posicaoLista = "";
		String Id_Serventia = "";
		String Serventia = "";
		int PassoEditar = 0;
		boolean ehCamaraSegundoGrau;
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavel.jsp";
		request.setAttribute("tempRetorno", "ProcessoResponsavel");
		request.setAttribute("tempPrograma", "ProcessoResponsavel");
		
		// Obtem o Id e a descrição da Serventia selecionada
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").trim().equalsIgnoreCase("")) Id_Serventia = request.getParameter("Id_Serventia");
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").trim().equalsIgnoreCase("")) Serventia = request.getParameter("Serventia");
		if (Id_Serventia == null || Id_Serventia.trim().equalsIgnoreCase("") || Serventia == null || Serventia.trim().equalsIgnoreCase("")){
			Id_Serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
			Serventia = UsuarioSessao.getUsuarioDt().getServentia();
		}
		
		movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (movimentacaoProcessoDt == null) movimentacaoProcessoDt = new MovimentacaoProcessoDt();
		
		ProcessoResponsavelne = (ProcessoResponsavelNe) request.getSession().getAttribute("ProcessoResponsavelne");
		if (ProcessoResponsavelne == null) ProcessoResponsavelne = new ProcessoResponsavelNe();

		ProcessoResponsaveldt = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldt");
		if (ProcessoResponsaveldt == null) ProcessoResponsaveldt = new ProcessoResponsavelDt();
		
		ProcessoResponsaveldtRevisor = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldtRevisor");
		if (ProcessoResponsaveldtRevisor == null) ProcessoResponsaveldtRevisor = new ProcessoResponsavelDt();
		
		ProcessoResponsaveldtVogal = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldtVogal");
		if (ProcessoResponsaveldtVogal == null) ProcessoResponsaveldtVogal = new ProcessoResponsavelDt();
		
		if(request.getParameter(VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_REVISOR) != null){
			ProcessoResponsaveldtRevisor.setId_ServentiaCargo(request.getParameter(VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_REVISOR));
			ProcessoResponsaveldtRevisor.setServentiaCargo(request.getParameter(VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL_REVISOR));
			ProcessoResponsaveldtRevisor.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
			if (ProcessoResponsaveldtRevisor.getId_ServentiaCargo() != null && ProcessoResponsaveldtRevisor.getId_ServentiaCargo().trim().length() > 0) {
				ServentiaCargoDt serventiaCargoDt = ProcessoResponsavelne.consultarServentiaCargo(ProcessoResponsaveldtRevisor.getId_ServentiaCargo());
				if(serventiaCargoDt != null) {
					ProcessoResponsaveldtRevisor.setServentiaCargo(serventiaCargoDt.getServentiaCargo() + " - " + serventiaCargoDt.getNomeUsuario());
				}	
			}			
		}
		
		if(request.getParameter(VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_VOGAL) != null){
			ProcessoResponsaveldtVogal.setId_ServentiaCargo(request.getParameter(VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_VOGAL));
			ProcessoResponsaveldtVogal.setServentiaCargo(request.getParameter(VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL_VOGAL));
			ProcessoResponsaveldtVogal.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
			if (ProcessoResponsaveldtVogal.getId_ServentiaCargo() != null && ProcessoResponsaveldtVogal.getId_ServentiaCargo().trim().length() > 0) {
				ServentiaCargoDt serventiaCargoDt = ProcessoResponsavelne.consultarServentiaCargo(ProcessoResponsaveldtVogal.getId_ServentiaCargo());
				if(serventiaCargoDt != null) {
					ProcessoResponsaveldtVogal.setServentiaCargo(serventiaCargoDt.getServentiaCargo() + " - " + serventiaCargoDt.getNomeUsuario());
				}	
			}			
		}
		
		if(request.getParameter(VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL) != null){
			ProcessoResponsaveldt.setId_ServentiaCargo(request.getParameter(VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL));
			ProcessoResponsaveldt.setServentiaCargo(request.getParameter(VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL));
			ProcessoResponsaveldt.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
			if (ProcessoResponsaveldt.getId_ServentiaCargo() != null && ProcessoResponsaveldt.getId_ServentiaCargo().trim().length() > 0) {
				ServentiaCargoDt serventiaCargoDt = ProcessoResponsavelne.consultarServentiaCargo(ProcessoResponsaveldt.getId_ServentiaCargo());
				if(serventiaCargoDt != null) {
					ProcessoResponsaveldt.setServentiaCargo(serventiaCargoDt.getServentiaCargo() + " - " + serventiaCargoDt.getNomeUsuario());
				}	
			}			
		}
		
		ProcessoResponsaveldt.setId_Processo(request.getParameter("Id_Processo"));
		ProcessoResponsaveldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoResponsaveldt.setId_Grupo(request.getParameter("Id_Grupo"));
		ProcessoResponsaveldt.setGrupoCodigo(request.getParameter("GrupoCodigo"));
		
		ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());	

		posicaoLista = request.getParameter("posicaoLista");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		if (request.getParameter("PassoEditar") != null) PassoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		
		ehCamaraSegundoGrau = UsuarioSessao.getUsuarioDt().isSegundoGrau();	

		switch (paginaatual) {

			//Inicializa Troca de Responsável
			case Configuracao.Novo:
				ProcessoResponsaveldt = new ProcessoResponsavelDt();
				ProcessoResponsaveldtRevisor = new ProcessoResponsavelDt();
				ProcessoResponsaveldtVogal = new ProcessoResponsavelDt();

				// Captura os processos no caso de Troca de Responsável em Lote
				if (request.getParameter("processos") != null) {
					processos = request.getParameterValues("processos");
				
				} else if (movimentacaoProcessoDt.getListaProcessos() != null){//troca de responsavel em lote vem redirecionado do movimentacaoCt
					processos = new String [movimentacaoProcessoDt.getListaProcessos().size()];
					int aux = 0;
					for (Iterator iterator = movimentacaoProcessoDt.getListaProcessos().iterator(); iterator.hasNext();) {
						ProcessoDt tempProcessoDt = (ProcessoDt) iterator.next();
						processos[aux] = tempProcessoDt.getId();
						aux += 1;
					}
					
					
				} else {
					//Recupera o processo da sessão e adiciona ao vetor
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					//Adiciona processo único ao vetor para montar tela corretamente
					if (processoDt != null && processoDt.getId().length() > 0) processos = new String[] {processoDt.getId() };
				}
				
				boolean atualizaTipoServentia = true; // Utilizada para obter o tipo da serventia do primeiro processo da lista (lote) ou o único processo selecionado
				if (processos != null && processos.length > 0) {
					//Consulta dados básicos de cada processo e adiciona à lista
					for (int i = 0; i < processos.length; i++) {
						ProcessoDt obj = ProcessoResponsavelne.consultarProcessoId(processos[i]);
						//Verifica se usuário pode trocar o responsável pelo processo
						Mensagem = ProcessoResponsavelne.podeTrocarResponsavel(obj, UsuarioSessao.getUsuarioDt(), ProcessoResponsavelne);
						if (Mensagem.length() == 0) {
							obj.setServentiaCargoResponsavelDt(ProcessoResponsavelne.consultarResponsavelProcesso(obj.getId(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
							if(ehCamaraSegundoGrau){
								obj.setServentiaCargoRevisorResponsavelDt(ProcessoResponsavelne.consultarRevisorResponsavelProcessoSegundoGrau(obj.getId()));
								obj.setServentiaCargoVogalResponsavelDt(ProcessoResponsavelne.consultarVogalResponsavelProcessoSegundoGrau(obj.getId()));															
							}	
							ProcessoResponsaveldt.addListaProcessos(obj);
							
							// Obtem a ServentiaTipoCodigo da serventia onde o processo está, será utilizado para obter somente as promotorias de mesmo nível do processo, sendo de primeiro grau ou de segundo grau 
							if (atualizaTipoServentia){
								ServentiaDt serventiaProcesso = ProcessoResponsavelne.consultarServentiaIdSimples(obj.getId_Serventia());
								if (serventiaProcesso != null) ProcessoResponsaveldt.setServentiaTipoCodigoProcesso(serventiaProcesso.getServentiaTipoCodigo());
								atualizaTipoServentia = false; // Para garantir que só irá obter de um único processo
							}
						} else {
							if (processoDt != null) redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
							else redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=" + Mensagem);
							return;
						}
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;

			case Configuracao.Salvar:
				//Se o atributo novoRelatorDesabilitado estiver na sessão, então o usuário está incluindo um relator desabilitado ao processo.
				if(request.getSession().getAttribute("novoRelatorDesabilitado") == null) {
					request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável pelo Processo");
				} else {
					//Se os dois campos abaixo estiverem preenchidos, então é desabilitação de usuário. Senão, é inclusão de novo relator desabilitado.
					if(request.getParameter("idProcessoResponsavel") != null && !request.getParameter("idProcessoResponsavel").equalsIgnoreCase("null") &&  
							request.getParameter("habilitrDesabilitar") != null && !request.getParameter("habilitrDesabilitar").equalsIgnoreCase("null")) {
						request.setAttribute("Mensagem", "Clique para Confirmar a Habilitar/Desabilitar o Responsável pelo Processo");
						request.setAttribute("idProcessoResponsavel", request.getParameter("idProcessoResponsavel"));
						request.setAttribute("habilitrDesabilitar", request.getParameter("habilitrDesabilitar"));
					} else {
						request.setAttribute("Mensagem", "Clique para incluir o Relator desabilitado ao Processo");					
					}
					stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavelDesabilitado.jsp";
				}
				break;

			case Configuracao.SalvarResultado:
				//Se o atributo novoRelatorDesabilitado estiver na sessão, então o usuário está incluindo um relator desabilitado ao processo.
				if(request.getSession().getAttribute("novoRelatorDesabilitado") == null) {
					if (ehCamaraSegundoGrau) 
						Mensagem = ProcessoResponsavelne.verificarTrocaDesembargadoresResponsaveis(ProcessoResponsaveldt, ProcessoResponsaveldtRevisor, ProcessoResponsaveldtVogal);
					else 
						Mensagem = ProcessoResponsavelne.verificarTrocaResponsavel(ProcessoResponsaveldt, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), false);
					
					if (Mensagem.length() == 0) {
						if (ehCamaraSegundoGrau) 
							ProcessoResponsavelne.salvarTrocaDesembargadoresResponsaveis(ProcessoResponsaveldt.getId_ServentiaCargo(), ProcessoResponsaveldtRevisor.getId_ServentiaCargo(), ProcessoResponsaveldtVogal.getId_ServentiaCargo() , ProcessoResponsaveldt.getListaProcessos(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()), UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt);
						else 
							ProcessoResponsavelne.salvarTrocaResponsavel(ProcessoResponsaveldt.getId_ServentiaCargo(), ProcessoResponsaveldt.getListaProcessos(), ProcessoResponsaveldt.getCargoTipoCodigo(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()), UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt);				
						
						request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");
						
						List listaProcessos = ProcessoResponsaveldt.getListaProcessos();
						for (int i = 0; i < listaProcessos.size(); i++) {
							ProcessoDt objTemp = (ProcessoDt) listaProcessos.get(i);
							objTemp.setServentiaCargoResponsavelDt(ProcessoResponsavelne.consultarResponsavelProcesso(objTemp.getId(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
							if(ehCamaraSegundoGrau){
								objTemp.setServentiaCargoRevisorResponsavelDt(ProcessoResponsavelne.consultarRevisorResponsavelProcessoSegundoGrau(objTemp.getId()));
								objTemp.setServentiaCargoVogalResponsavelDt(ProcessoResponsavelne.consultarVogalResponsavelProcessoSegundoGrau(objTemp.getId()));	
							}						
						}
						ProcessoResponsaveldt.setId_ServentiaCargo("null");
						ProcessoResponsaveldtRevisor.limpar();
						ProcessoResponsaveldtVogal.limpar();
						//Dados movimentacao
						movimentacaoProcessoDt.limpar();
						limparListas(request);
						request.getSession().removeAttribute("Movimentacaodt");
					} else 
						request.setAttribute("MensagemErro", Mensagem);
				} else {
					String mensagemRetorno = "";
					//Se os dois campos abaixo estiverem preenchidos, então é desabilitação de usuário. Senão, é inclusão de novo relator desabilitado.
					if(request.getParameter("idProcessoResponsavel") != null && !request.getParameter("idProcessoResponsavel").equalsIgnoreCase("null") &&  
							request.getParameter("habilitrDesabilitar") != null && !request.getParameter("habilitrDesabilitar").equalsIgnoreCase("null")) {
						ProcessoResponsaveldt.setId(request.getParameter("idProcessoResponsavel"));
						ProcessoResponsaveldt.setCodigoTemp(request.getParameter("habilitrDesabilitar"));
						ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
						
						ProcessoResponsavelne.habilitarDesabilitarResponsavelProcesso(ProcessoResponsaveldt);
						mensagemRetorno = "Status de responsável alterado com sucesso.";
						
					} else {
						//o usuário a ser inserido é um relator desabilitado para o processo.
						ProcessoResponsaveldt.setCargoTipoCodigo(String.valueOf(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU));
						ProcessoResponsaveldt.setRedator(true);
						//se houver erro na validação, dispara uma exception desde o Ne.
						ProcessoResponsavelne.verificarInclusaoRelatorDesabilitado(ProcessoResponsaveldt);
						//se passar pela validação pode salvar o registro
						ProcessoResponsavelne.salvar(ProcessoResponsaveldt);
						ProcessoResponsavelne.inativarResponsavelProcesso(ProcessoResponsaveldt);
						
						//limpando dados e sessão
						request.getSession().removeAttribute("novoRelatorDesabilitado");
						mensagemRetorno = "Relator desabilitado incluído com sucesso.";
					}
					String idProcesso = ProcessoResponsaveldt.getId_Processo();
					ProcessoResponsaveldt.limpar();
					// Volta para tela de capa do processo com mensagem de confirmação
					redireciona(response, "BuscaProcesso?Id_Processo=" + idProcesso + "&MensagemOk="+mensagemRetorno);
				}
				break;

			// Remover um processo selecionado
			case Configuracao.LocalizarAutoPai:
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					ProcessoResponsaveldt.getListaProcessos().remove(posicao);
					movimentacaoProcessoDt.getListaProcessos().remove(posicao);
				}
				break;

			case Configuracao.Localizar: //localizar
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavelLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoResponsavel", "Id_ProcessoResponsavel");
				request.setAttribute("tempBuscaProcessoResponsavel", "ProcessoResponsavel");
				tempList = ProcessoResponsavelne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size() > 0) {
					request.setAttribute("ListaProcessoResponsavel", tempList);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoResponsavelne.getQuantidadePaginas());
					ProcessoResponsaveldt.limpar();
					ProcessoResponsaveldtRevisor.limpar();
					ProcessoResponsaveldtVogal.limpar();
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
				}
				break;
				
			case Configuracao.Curinga6:
				//Se acessar esse Curinga, então o usuário está incluindo um relator desabilitado ao processo.
				if(request.getParameter("PassoAtual") != null && request.getParameter("PassoAtual").equals("1")) {
					stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavelDesabilitado.jsp";
					request.getSession().setAttribute("novoRelatorDesabilitado", true);
					ProcessoResponsaveldt.setProcessoNumero(new ProcessoNe().consultarNumeroCompletoDoProcesso(ProcessoResponsaveldt.getId_Processo()));
					request.getSession().setAttribute("listaResponsaveisProcesso", ProcessoResponsavelne.consultarTodosResponsaveisProcesso(ProcessoResponsaveldt.getId_Processo()));
				}
				if(request.getSession().getAttribute("novoRelatorDesabilitado") != null) {
					stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavelDesabilitado.jsp";
				}
				
				break;

			//Consulta possíveis cargos que podem ser novos responsáveis por um processo
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):							
				String ValorCampo_id_ServentiaCargo = VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL;
				String ValorCampoServentiaCargo = VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL; 
				
				if (PassoEditar == PASSO_CONSULTA_RESPONSAVEL_REVISOR){
					ValorCampo_id_ServentiaCargo = VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_REVISOR;
					ValorCampoServentiaCargo = VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL_REVISOR;
				}else if(PassoEditar == PASSO_CONSULTA_RESPONSAVEL_VOGAL){
					ValorCampo_id_ServentiaCargo = VALOR_ATRITUBO_ID_CONSULTA_RESPONSAVEL_VOGAL;
					ValorCampoServentiaCargo = VALOR_ATRITUBO_NOME_CONSULTA_RESPONSAVEL_VOGAL;
				}

				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo [Serventia]", "Usuário", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", ValorCampo_id_ServentiaCargo);
					request.setAttribute("tempBuscaDescricao", ValorCampoServentiaCargo);
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "ProcessoResponsavel");
					//Atributo novoRelatorDesabilitado será usado para o controle das transações no ct
					if(request.getSession().getAttribute("novoRelatorDesabilitado") == null) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					} else {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					}
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
					request.setAttribute("tempFluxo1", "1");
				}else{
					String stTemp = "";
					//Se o atributo novoRelatorDesabilitado estiver na sessão, então o usuário está incluindo um relator desabilitado ao processo.
					if(request.getSession().getAttribute("novoRelatorDesabilitado") == null) {
						stTemp = ProcessoResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, Id_Serventia, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());						
					} else {
						stTemp = ProcessoResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, "", UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
					}
					enviarJSON(response, stTemp);
					return;
				}
				break;
			
			// Consultar as serventias que utilizam o mesmo tipo da Serventia logada 
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");	
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
				
					String serventiaSubTipo = obtenhaServentiaSubTipo(ProcessoResponsaveldt, UsuarioSessao.getUsuarioDt());
					stTemp = ProcessoResponsavelne.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), serventiaSubTipo);
					
					enviarJSON(response, stTemp);
					
					request.setAttribute("serventiaTipoCodigo", UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo());
					request.setAttribute("serventiaSubTipoCodigo", serventiaSubTipo);
					
					// Limpa as informações do Cargo anteriormente escolhidos
					ProcessoResponsaveldt.setId_ServentiaCargo("null");
					ProcessoResponsaveldt.setServentiaCargo("");
				
					return;			
				}
				break;

			default:
				stId = request.getParameter("Id_ProcessoResponsavel");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoResponsaveldt.getId())) {
					ProcessoResponsaveldt.limpar();
					ProcessoResponsaveldt = ProcessoResponsavelne.consultarId(stId);
				}
				break;
		}

		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);
		
		request.getSession().setAttribute("ProcessoResponsaveldt", ProcessoResponsaveldt);
		request.getSession().setAttribute("ProcessoResponsavelne", ProcessoResponsavelne);
		request.getSession().setAttribute("ProcessoResponsaveldtRevisor", ProcessoResponsaveldtRevisor);
		request.getSession().setAttribute("ProcessoResponsaveldtVogal", ProcessoResponsaveldtVogal);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Obtem o SubTipo da Serventia para ser utilizado na pesquisa de serventias
	 * @param processoResponsaveldt
	 * @param usuarioDt
	 * @return
	 */
	protected String obtenhaServentiaSubTipo(ProcessoResponsavelDt processoResponsaveldt, UsuarioDt usuarioDt){
		
		if (processoResponsaveldt == null) return "";
		
		String serventiaTipo_Processo = processoResponsaveldt.getServentiaTipoCodigoProcesso();
		
		if (serventiaTipo_Processo == null) return "";
		
		if (usuarioDt.getServentiaTipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.PROMOTORIA))){
			if (serventiaTipo_Processo.trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.VARA))) return String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU);
		
			if (serventiaTipo_Processo.trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU))) return String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU);
		}
		
		return "";
	}
	
	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}
	
}
