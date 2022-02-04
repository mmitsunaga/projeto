package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.dt.relatorios.SaidaTemporariaDt;
import br.gov.go.tj.projudi.ne.ProcessoEventoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;


/**
 * Servlet que controla os eventos do Processo de Execução Penal. Os eventos podem ser visualizados por qualquer usuário, porém
 * a edição e exclusão podem ser feitas somente pelo cartório a qualquer momento no processo. 
 * Utilizada quand   o desejam alterar dados dos eventos, na impressão de relatórios do processo de execução penal.
 * 
 * @author wcsilva
 */
public class ProcessoEventoExecucaoCt extends ProcessoEventoExecucaoCtGen{

    private static final long serialVersionUID = 7554535923090150054L;
    
		
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		ProcessoEventoExecucaoNe processoEventoExecucaoNe = null;
		ProcessoEventoExecucaoDt processoEventoExecucaoDt;
		ProcessoDt processoDt = null;
		CalculoLiquidacaoDt calculoLiquidacaoDt = null;
		SaidaTemporariaDt saidaTemporariaBean = null; //variável utilizada na visualização das saídas temporárias
		List listaEventoSaidaTemporaria = null; //variável utilizada na consulta das saídas temporárias, contém apenas os eventos de saída temporária

 		List listaEventos = null; //lista dos demais eventos ou eventos da movimentação
		List listaHistoricoPsc = null; //lista dos eventos de histórico de cumprimento da prestação de serviço à comunidade
		List listaHistoricoPec = null; //lista dos eventos de histórico de cumprimento da prestação pecuniária
		List listaHistoricoLfs = null; //lista dos eventos de histórico de cumprimento da limitação de fim de semana
		List listaHistoricoItd = null; //lista dos eventos de histórico de cumprimento da interdição temporária de direitos
		List listaHistoricoPcb = null; //lista dos eventos de histórico de cumprimento do pagamento de cestas básicas
		List listaEventosDataBase = null;
		List listaParametroComutacao = null;
		List listaTipoCalculo = null; //utilizada para armazenar a lista de cálculo na impressão do relatório.
		List listaCondenacaoExtinta = new ArrayList();
		//refere-se ao id do processo pai (para o caso de processo apenso)
		String id_Processo = null; //utilizado para verificar se a lista de eventos que está na sessão refere-se ao processo que está na sessão.
		String id_Movimentacao = null; //utilizado para verificar se a lista de eventos que está na sessão refere-se a movimentação que está na sessão.
		List tempList = null;
		byte[] byTemp = null;
				
		int paginaAnterior = 0;
		int passoEditar = -1;
		ProcessoExecucaoDt processoExecucaoDt = null;
		SituacaoAtualExecucaoDt situacaoAtualExecucaoDt = null;
		
		String Mensagem = "";
		String MensagemErro = "Os dados não foram encontrados";
		String posicaoLista = "";
		String stAcao = "";
		
		//consulta JSON
		//List lisNomeBusca = new ArrayList();
		//List lisDescricao = new ArrayList();
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3"); 
		
		request.setAttribute("tempPrograma", "ProcessoEventoExecucao");
		request.setAttribute("tempRetorno", "ProcessoEventoExecucao");
		request.setAttribute("TituloPagina", "Eventos da Execução Penal");
		request.setAttribute("tempRetornoProcesso", "BuscaProcesso");

		if (processoEventoExecucaoNe == null) processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
		
		if (request.getSession().getAttribute("processoDt") != null)
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

		if (request.getSession().getAttribute("id_Processo") != null) 
			id_Processo = request.getSession().getAttribute("id_Processo").toString();
			
		calculoLiquidacaoDt = (CalculoLiquidacaoDt) request.getSession().getAttribute("CalculoLiquidacaodt");
		if (calculoLiquidacaoDt == null) calculoLiquidacaoDt = new CalculoLiquidacaoDt();
		listaTipoCalculo = calculoLiquidacaoDt.getListaTipoCalculo();

		if (request.getSession().getAttribute("listaEventosDataBase") != null)
			listaEventosDataBase = (List) request.getSession().getAttribute("listaEventosDataBase");

		if (request.getSession().getAttribute("listaParametroComutacao") != null)
			listaParametroComutacao = (List) request.getSession().getAttribute("listaParametroComutacao");
		
		if (request.getSession().getAttribute("listaCondenacaoExtinta") != null)
			listaCondenacaoExtinta = (List) request.getSession().getAttribute("listaCondenacaoExtinta");

		if (request.getSession().getAttribute("SituacaoDt") != null)
			situacaoAtualExecucaoDt = (SituacaoAtualExecucaoDt) request.getSession().getAttribute("SituacaoDt");
		
		processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) request.getSession().getAttribute("ProcessoEventoExecucaodt");
		
		if (processoEventoExecucaoDt == null){
			situacaoAtualExecucaoDt = null;
			situacaoAtualExecucaoDt = new SituacaoAtualExecucaoDt();
			processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
			//captura o id do processo pai
			if (processoDt!=null && processoDt.isProcessoDependente()) {
				id_Processo = processoDt.getId_ProcessoPrincipal();
			}
			else if (processoDt!=null){
				id_Processo = processoDt.getId_Processo();
			}
		} else{
			if (id_Processo != processoDt.getId_Processo() && id_Processo != processoDt.getId_ProcessoPrincipal()){
				situacaoAtualExecucaoDt = null;
				situacaoAtualExecucaoDt = new SituacaoAtualExecucaoDt();
				
				//captura o id do processo pai
				if (processoDt.getId_ProcessoPrincipal().length() > 0) id_Processo = processoDt.getId_ProcessoPrincipal(); 
				else id_Processo = processoDt.getId_Processo();
			}
		}
		
		listaEventos = (List) request.getSession().getAttribute("listaEventos");
		listaHistoricoPsc = (List) request.getSession().getAttribute("listaHistoricoPsc");
		listaHistoricoPec = (List) request.getSession().getAttribute("listaHistoricoPec");
		listaHistoricoLfs = (List) request.getSession().getAttribute("listaHistoricoLfs");
		listaHistoricoItd = (List) request.getSession().getAttribute("listaHistoricoItd");
		listaHistoricoPcb = (List) request.getSession().getAttribute("listaHistoricoPcb");
		
		//recupera os valores do request
		processoEventoExecucaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoEventoExecucaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		//utilizado para manter os dados originais do evento a ser alterado
		ProcessoEventoExecucaoDt processoEventoExecucaoDtAnterior = new ProcessoEventoExecucaoDt();
		processoEventoExecucaoDtAnterior = processoEventoExecucaoNe.copiar(processoEventoExecucaoDt);
		
		// variáveis utilizadas na edição dos eventos e inclusão de novas condenações
		setDadosEdicaoEvento(processoEventoExecucaoNe, processoEventoExecucaoDt, request, UsuarioSessao, id_Processo);
		id_Movimentacao = processoEventoExecucaoDt.getId_Movimentacao();
				
		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		posicaoLista = request.getParameter("posicaoLista");
		//mesmo passo editar - para controlar retorno das consultas JSON
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null") && request.getParameter("tempFluxo1").length() > 0){
			passoEditar = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		}
		
		if (request.getSession().getAttribute("ProcessoExecucaodt_PE") != null){
			processoExecucaoDt = (ProcessoExecucaoDt) request.getSession().getAttribute("ProcessoExecucaodt_PE");
			setDadosProcessoExecucao(request, UsuarioSessao, processoExecucaoDt, paginaatual);
		}
		
		// set parâmetros auxiliares
		request.setAttribute("PaginaAnterior", paginaAnterior);
		request.setAttribute("permissaoEditarEvento", "true");
		if (request.getSession().getAttribute("displayNovaCondenacao") == null) request.getSession().setAttribute("displayNovaCondenacao", "none");
		
        //controla a origem da lista de eventos: eventos da movimentação ou todos os eventos do processo
        String passoEditarListaEvento = "";
        if ((request.getSession().getAttribute("PassoEditarListaEvento") != null) && (!request.getSession().getAttribute("PassoEditarListaEvento").equals(""))) {
        	passoEditarListaEvento = (String) request.getSession().getAttribute("PassoEditarListaEvento");
        }
        
        if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
        
        setDadosSituacaoAtualExecucao(request, UsuarioSessao, situacaoAtualExecucaoDt, id_Processo);
        
		switch (paginaatual) {
			case Configuracao.Novo:
				Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else{
					//verifica o redirecionamento da página para listar os eventos: parâmetro inserido no request pelo ProcessoExecucaoCt
					if (request.getParameter("RedirecionarListaEvento") != null){
						if (Funcoes.StringToBoolean(request.getParameter("RedirecionarListaEvento"))){
							if (passoEditarListaEvento.equals("1")){
								stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
								passoEditar = 1;
							}
							else if (passoEditarListaEvento.equals("2")){
								stAcao = "/WEB-INF/jsptjgo/ListaProcessoEventoExecucao.jsp";
								passoEditar = -1;
							}
						}
					}
					setListaRegimeRequest(processoEventoExecucaoNe, request);
					
					switch(passoEditar){
					case 1://quando o usuário acessar lista de eventos pelo Menu: Manter eventos de execucao
						calculoLiquidacaoDt.setQtdeTempoHorasEstudo("12"); //coloco o tempo padrão no acesso inicial
						//lista de eventos de acordo com o processo
						HashMap map = processoEventoExecucaoNe.montarListaEventosCompleta(id_Processo, UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
						listaEventos =  (List)map.get("listaEventos");
						listaHistoricoPsc = (List)map.get("listaHistoricoPsc");
						listaHistoricoPec = (List)map.get("listaHistoricoPec");
						listaHistoricoLfs = (List)map.get("listaHistoricoLfs");
						listaHistoricoItd = (List)map.get("listaHistoricoItd");
						listaHistoricoPcb = (List)map.get("listaHistoricoPcb");
						
						stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
						
						//informações do último cálculo
						passoEditarListaEvento = "1"; //origem da lista de evento: manter eventos de execução
						DataProvavelDt ultimoCalculo = new DataProvavelDt();
						ultimoCalculo = processoEventoExecucaoNe.consultarCalculoLiquidacao(id_Processo);
						request.getSession().setAttribute("ultimoCalculo", ultimoCalculo);
						if (ultimoCalculo.getRelatorioByte() != null){
							request.setAttribute("visualizaUltimoCalculo", "true");
							calculoLiquidacaoDt.setRelatorioByte(ultimoCalculo.getRelatorioByte());
						}
						request.getSession().removeAttribute("ProcessoExecucaodt_PE");
						
						//informação da situação atual de cumprimento da pena
						situacaoAtualExecucaoDt = processoEventoExecucaoNe.consultarSituacaoAtualExecucao(id_Processo, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						Mensagem = this.verificarAtualizacaoSituacaoAtual(situacaoAtualExecucaoDt.getDataAlteracao());
						if (Mensagem.length() > 0){
							request.setAttribute("MensagemErro", Mensagem);
						}
						break;
					default: //quando o usuário acessar lista de eventos pela movimentação
						//lista de eventos de acordo com o processo e a movimentação
						listaEventos = montarListaEventosMovimentacao(processoEventoExecucaoNe, id_Processo, id_Movimentacao, UsuarioSessao.getUsuarioDt());
						stAcao = "/WEB-INF/jsptjgo/ListaProcessoEventoExecucao.jsp";
						passoEditarListaEvento = "2"; //origem da lista de evento: movimentação
						break;
					}
				}	
				break;
				
			// lista os eventos da movimentação do processo
			case Configuracao.Localizar:
				Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0 && passoEditar != 5){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else{
					setListaRegimeRequest(processoEventoExecucaoNe, request);
					request.setAttribute("TituloPagina", "Eventos da Movimentação do Processo");
					passoEditarListaEvento = "2"; //origem eventos da movimentação
					listaEventos = montarListaEventosMovimentacao(processoEventoExecucaoNe, id_Processo, id_Movimentacao, UsuarioSessao.getUsuarioDt());
					if (listaEventos.size() > 0) {
						request.setAttribute("QuantidadePaginas", processoEventoExecucaoNe.getQuantidadePaginas());
						request.setAttribute("tempBuscaId_EventoExecucao", "Id_EventoExecucao");
						request.setAttribute("tempBuscaEventoExecucao", "EventoExecucao");
						request.setAttribute("tempRetorno", "ProcessoEventoExecucao");
					} else {
						if (request.getParameter("MovimentacaoDataRealizacaoTipo")!=null){
							ProcessoEventoExecucaoDt processoEventoExecucaoDtTemp = new ProcessoEventoExecucaoDt();
							processoEventoExecucaoDtTemp.setId_Movimentacao(request.getParameter("Id_Movimentacao"));
							processoEventoExecucaoDtTemp.setMovimentacaoDataRealizacaoTipo(request.getParameter("MovimentacaoDataRealizacaoTipo"));
							listaEventos.add(processoEventoExecucaoDtTemp);
						}
						request.setAttribute("MensagemErro", "Não existe(m) Evento(s) cadastrado(s) para esta movimentação.");
					}
					request.setAttribute("PaginaAnterior", paginaatual);
					request.getSession().removeAttribute("PaginaPai");
					stAcao = "/WEB-INF/jsptjgo/ListaProcessoEventoExecucao.jsp";
				}
				break;

				  //inclui e exclui condenação da lista de condenações
	        case Configuracao.LocalizarAutoPai:
	            if (posicaoLista.length() == 0) {//adicionar condenação
	                Mensagem = processoEventoExecucaoNe.adicionarCondenacaoProcesso(request, UsuarioSessao.getUsuarioDt(), Configuracao.Curinga6, processoExecucaoDt);
	                if (Mensagem.length() > 0)
	                    request.setAttribute("MensagemErro", Mensagem);
	            } else {//excluir condenação
	            	Mensagem = processoEventoExecucaoNe.excluirCondenacao(posicaoLista, processoExecucaoDt);
	            	if (Mensagem.length() > 0)
	                    request.setAttribute("MensagemErro", Mensagem);
	            }
	            
	            stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
	            break;
	            
			case Configuracao.Salvar:
				Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0 && passoEditar != 5){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else{
					//altera os dados do evento
					processoEventoExecucaoDt.setId_UsuarioServentia(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					MensagemErro = setDadosEventoComutacao_RevogacaoLC(request, processoEventoExecucaoDt);
					if (MensagemErro.length() == 0) MensagemErro = processoEventoExecucaoNe.VerificarCadastroEventoExecucao(processoEventoExecucaoDt, processoExecucaoDt, id_Processo, Funcoes.StringToInt(calculoLiquidacaoDt.getQtdeTempoHorasEstudo()), processoEventoExecucaoDt.getDataDecretoComutacao());
					if (MensagemErro.length() > 0){
						stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
						request.setAttribute("listaParametroComutacao", listaParametroComutacao);
						request.setAttribute("MensagemErro", MensagemErro);
					} else {
						//salvar os dados
						processoEventoExecucaoDt.setIdProcessoExecucaoPenal(id_Processo);
						processoEventoExecucaoNe.alterar(processoEventoExecucaoDt, processoExecucaoDt);
						
						if (Funcoes.StringToBoolean(processoEventoExecucaoNe.isEventoManterAcaoPenal(processoEventoExecucaoDt.getEventoExecucaoDt().getId()))){
							processoEventoExecucaoNe.salvarProcessoExecucao(processoExecucaoDt, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
							processoExecucaoDt.limpar();
						}
						processoEventoExecucaoDt.limpar();
						processoExecucaoDt.limpar();
						listaEventosDataBase = null;
						listaEventos = null;
						request.setAttribute("MensagemOk", "O Evento foi cadastrado com sucesso!");
						if (passoEditarListaEvento.equals("0")){
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Evento incluído com sucesso!");
							return;
						}
						else if (passoEditarListaEvento.equals("2")){
							//lista de eventos de acordo com o processo e a movimentação
							listaEventos = montarListaEventosMovimentacao(processoEventoExecucaoNe, id_Processo, id_Movimentacao, UsuarioSessao.getUsuarioDt());
							stAcao = "/WEB-INF/jsptjgo/ListaProcessoEventoExecucao.jsp";
						}
						else if(passoEditarListaEvento.equals("3")){
							//lista os eventos de saída temporária
							redireciona(response, "ProcessoEventoExecucao?PaginaAtual="+ Configuracao.Curinga7 +"&PassoEditar=1");
							return;
						}
						else {
							//lista de eventos de acordo com o processo
							HashMap map = processoEventoExecucaoNe.montarListaEventosCompleta(id_Processo, UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
							listaEventos =  (List)map.get("listaEventos");
							listaHistoricoPsc = (List)map.get("listaHistoricoPsc");
							listaHistoricoPec = (List)map.get("listaHistoricoPec");
							listaHistoricoLfs = (List)map.get("listaHistoricoLfs");
							listaHistoricoItd = (List)map.get("listaHistoricoItd");
							listaHistoricoPcb = (List)map.get("listaHistoricoPcb");
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							setListaRegimeRequest(processoEventoExecucaoNe, request);
						}
					}
				}
				break;
				
			//Salva o calculo e Gera o relatório dos eventos da execução penal e cálculo de liquidação de penas (se existir)
			case Configuracao.Imprimir:
				Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0 && passoEditar != 5 && passoEditar != 2){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else{
					boolean calculoNaoOficial = true;
//					boolean isPenaRestritiva = processoEventoExecucaoNe.isProcessoPenaRestritivaDireito(listaEventos);
					String tituloRelatorio = "Relatório de Liquidação de Penas";
					switch(passoEditar){
						case 1://quando o usuário confirmar o cálculo de liquidação: salvar calculo
							calculoNaoOficial = false;
							request.setAttribute("TituloPagina", "Cálculo de Liquidação de Penas");
							break;
							
						case 3: //relatório de cadastramento
							Mensagem = processoEventoExecucaoNe.verificarCalculo(listaEventos, calculoLiquidacaoDt);
							if (Mensagem.length() > 0){
								redireciona(response, "ProcessoEventoExecucao?PaginaAtual=" + Configuracao.Editar + "&PassoEditar=-1&MensagemErro="+Mensagem);
								return;
							}else {
								calculoNaoOficial = false;
								tituloRelatorio = "Relatório de Cadastramento";
							}
							break;
					}
					if (!calculoNaoOficial){
						Mensagem = this.verificarAtualizacaoSituacaoAtual(situacaoAtualExecucaoDt.getDataAlteracao());
						if (Mensagem.length() > 0){
//							calcularLiquidacaoDePena(request, processoEventoExecucaoNe, listaEventos, listaHistoricoPsc, listaHistoricoLfs, listaHistoricoPec, listaHistoricoItd, listaHistoricoPcb, calculoLiquidacaoDt, processoDt.getPrimeiroProvovidoDataNascimento(), id_Processo, UsuarioSessao.getUsuarioDt(), listaCondenacaoExtinta);
//							request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Curinga6));
							request.setAttribute("MensagemErro", Mensagem);
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							break;
						}
					}
					//imprimir calculo
					String informacoes = "";
					if (request.getParameter("InformacaoAdicional") != null) informacoes = (String) request.getParameter("InformacaoAdicional");
					calculoLiquidacaoDt.setListaTipoCalculo(listaTipoCalculo);
					calculoLiquidacaoDt.setFormaCumprimento(situacaoAtualExecucaoDt.getFormaCumprimento());
					
					String[] nomes = UsuarioSessao.getUsuarioDt().getNome().split(" ");
					String iniciaisNome = "";
					for (int i=0; i<nomes.length; i++){
						if (nomes[i].length() >=2 ){
							iniciaisNome += nomes[i].substring(0,1);
						}
					}
					byTemp = processoEventoExecucaoNe.relCalculoLiquidacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,
							listaEventos, listaHistoricoPsc, listaHistoricoLfs, listaHistoricoPec, listaHistoricoItd, listaHistoricoPcb, calculoLiquidacaoDt, processoDt, informacoes, calculoNaoOficial, listaCondenacaoExtinta, tituloRelatorio,
							iniciaisNome);
					
					if (passoEditar == 1){
						//gravar as datas do calculo
						DataProvavelDt dataProvavelDt = new DataProvavelDt();
						dataProvavelDt.setIdCalculo(calculoLiquidacaoDt.getId());
						dataProvavelDt.setIdProcesso(id_Processo);
						dataProvavelDt.setTemProcessoApenso(processoDt.temApensos());
						dataProvavelDt.setIdProcessoPrincipal(processoDt.getId_ProcessoPrincipal());
						dataProvavelDt.setId_UsuarioLog(UsuarioSessao.getUsuarioDt().getId());
						dataProvavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						dataProvavelDt.setInformacaoSentenciado(request.getParameter("InformacaoAdicional"));
						dataProvavelDt.setIdUsuarioServentia(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
						dataProvavelDt.setRelatorioByte(byTemp);
						
						processoEventoExecucaoNe.salvarCalculoLiquidacao(dataProvavelDt, calculoLiquidacaoDt);
					}
					
				    
				    String nome = "Relatorio_" + processoDt.getPrimeiroPoloPassivoNome().replaceAll(" ", "") ;					
					enviarPDF(response,byTemp,nome);											
					
				    byTemp = null;
				}
				return;
				
			//trata o cálculo de liquidação de penas.
			case Configuracao.Curinga6:
				if (passoEditar == 2){
					stAcao = "/WEB-INF/jsptjgo/CalculoLiquidacaoInformacaoCalculos.jsp";
					break;
				} else if (passoEditar == 3){
					stAcao = "/WEB-INF/jsptjgo/CalculoLiquidacaoInformacaoOpcoesCalculo.jsp";
					break;
				} else if (passoEditar == 4){
					stAcao = "/WEB-INF/jsptjgo/CalculoLiquidacaoInformacaoDataBase.jsp";
					break;
				} else if (passoEditar == 5){
					stAcao = "/WEB-INF/jsptjgo/CalculoLiquidacaoInformacaoPRLC.jsp";
					break;
				}
				setListaRegimeRequest(processoEventoExecucaoNe, request);
				//informação da situação atual de cumprimento da pena
				situacaoAtualExecucaoDt = processoEventoExecucaoNe.consultarSituacaoAtualExecucao(id_Processo, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else{
					//adicionado pois qdo acesso o calculo liquidação direto pelo menu, a listaEventos vem vazia
					HashMap map = processoEventoExecucaoNe.montarListaEventosCompleta(id_Processo, UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
					listaEventos =  (List)map.get("listaEventos");
					listaHistoricoPsc = (List)map.get("listaHistoricoPsc");
					listaHistoricoPec = (List)map.get("listaHistoricoPec");
					listaHistoricoLfs = (List)map.get("listaHistoricoLfs");
					listaHistoricoItd = (List)map.get("listaHistoricoItd");
					listaHistoricoPcb = (List)map.get("listaHistoricoPcb");
					
					// verifica se o processo está com status "extinto"
					if (processoEventoExecucaoNe.getIdStatus(listaEventos).equals(String.valueOf(EventoExecucaoStatusDt.EXTINTO))){
						Mensagem = "Não é possível efetuar cálculo de Liquidação de Penas! (Motivo: Pena extinta)";
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						return;
						
					} else {
						Mensagem = processoEventoExecucaoNe.verificarTodosVinculoEventoTJ(listaEventos, id_Processo); 
						if (Mensagem.length() > 0){
							request.setAttribute("MensagemErro", Mensagem);
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
						} else {
							if (listaParametroComutacao == null) listaParametroComutacao = processoEventoExecucaoNe.listarParametroComutacaoExecucao();
							request.setAttribute("listaParametroComutacao", listaParametroComutacao);
							listaEventosDataBase = processoEventoExecucaoNe.getListaEventosDataBase_Da_ListaEventos(listaEventos);
						
							// variável que controla o tipo de cálculo de liquidação de pena
							if (calculoLiquidacaoDt != null){
								calculoLiquidacaoDt.limpar();
								calculoLiquidacaoDt.setListaTipoCalculo(null);
							}
							//limpar dados do calculo de liquidação
							calculoLiquidacaoDt.limparDadosEspecificos(false, true, true, true, true);
							stAcao = "/WEB-INF/jsptjgo/CalculoLiquidacao.jsp";
						}
					}
				}
				break;

			//trata as consultas do processo de execução penal
				//passoEditar 1: saídas temporárias - acesso inicial
				//passoEditar 2: lista os eventos da execução penal (sem edição)
				//passoEditar 3: relatório de saída temporária
				//passoEditar 4: exclui o evento de saída temporária
				//passoEditar 5: Carrega o evento selecionado para edição
				//passoEditar 6: Imprime relatório de saída temporária
			case Configuracao.Curinga7:
					HashMap map = processoEventoExecucaoNe.montarListaEventosCompleta(id_Processo, UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
					listaEventos =  (List)map.get("listaEventos");
					listaHistoricoPsc = (List)map.get("listaHistoricoPsc");
					listaHistoricoPec = (List)map.get("listaHistoricoPec");
					listaHistoricoLfs = (List)map.get("listaHistoricoLfs");
					listaHistoricoItd = (List)map.get("listaHistoricoItd");
					listaHistoricoPcb = (List)map.get("listaHistoricoPcb");
					
					switch(passoEditar){
						case 1: //acesso inicial da tela de saída temporária
							Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
							if (Mensagem.length() > 0){
								request.setAttribute("permissaoEditarEvento", "false");
							} else {
								request.setAttribute("permissaoEditarEvento", "true");
							}
							stAcao = "/WEB-INF/jsptjgo/SaidaTemporaria.jsp";
							passoEditarListaEvento = "3"; //edição do evento de saída temporária

							saidaTemporariaBean = setDadosSaidaTemporariaBean(request, saidaTemporariaBean, processoDt.getId(), processoDt.getProcessoNumeroCompleto());
							listaEventoSaidaTemporaria = (List)map.get("listaEventosSaidaTemporaria");
							
							request.getSession().setAttribute("listaEventoSaidaTemporaria", listaEventoSaidaTemporaria);
							break;
							
						case 2: //lista os eventos da execução penal
							request.setAttribute("permissaoEditarEvento", "false");
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							break;
							
						case 3: //relatório de saída temporária
							Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
							stAcao = "/WEB-INF/jsptjgo/SaidaTemporaria.jsp";
							if (Mensagem.length() > 0){
								request.setAttribute("MensagemErro", Mensagem);
								request.setAttribute("permissaoEditarEvento", "false");
							} else {
								saidaTemporariaBean = setDadosSaidaTemporariaBean(request, saidaTemporariaBean, processoDt.getId(), processoDt.getProcessoNumeroCompleto());
								if (listaEventos!=null && listaEventos.size()>0){
									Mensagem = processoEventoExecucaoNe.VerificarSaidaTemporaria((ProcessoEventoExecucaoDt) listaEventos.get(0), saidaTemporariaBean.getAno());
									if (Mensagem.length()==0){
										listaEventoSaidaTemporaria = (List) request.getSession().getAttribute("listaEventoSaidaTemporaria");
										saidaTemporariaBean = processoEventoExecucaoNe.montarBeanSaidaTemporaria(processoDt, listaEventos, listaEventoSaidaTemporaria, calculoLiquidacaoDt,	saidaTemporariaBean.getAno());
										request.setAttribute("SaidaTemporariaBean", saidaTemporariaBean);
									} else {
										saidaTemporariaBean.setIdProcesso(processoDt.getId());
										saidaTemporariaBean.setNumeroProcesso(processoDt.getProcessoNumeroCompleto());
										saidaTemporariaBean.setAno("");
										request.setAttribute("SaidaTemporariaBean", saidaTemporariaBean);
										request.setAttribute("MensagemErro", Mensagem);
									}
								}
							}
							break;
							
						case 4: //exclui o evento de saída temporária
							Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
							stAcao = "/WEB-INF/jsptjgo/SaidaTemporaria.jsp";
							if (Mensagem.length() > 0){
								request.setAttribute("MensagemErro", Mensagem);
								request.setAttribute("permissaoEditarEvento", "false");
							} else {
								listaEventoSaidaTemporaria = (List) request.getSession().getAttribute("listaEventoSaidaTemporaria");
								saidaTemporariaBean = setDadosSaidaTemporariaBean(request, saidaTemporariaBean, processoDt.getId(), processoDt.getProcessoNumeroCompleto());
								processoEventoExecucaoDt = null;
								processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventoSaidaTemporaria.get(Funcoes.StringToInt(posicaoLista));
								processoEventoExecucaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
								processoEventoExecucaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
								processoEventoExecucaoNe.excluirCompleto(processoEventoExecucaoDt);
								listaEventoSaidaTemporaria.remove(Funcoes.StringToInt(posicaoLista));
								request.getSession().setAttribute("listaEventoSaidaTemporaria", listaEventoSaidaTemporaria);
								request.setAttribute("permissaoEditarEvento", "true");
							}
							break;
							
						case 5: //Carrega o evento selecionado para edição
							Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
							if (Mensagem.length() > 0){
								request.setAttribute("MensagemErro", Mensagem);
								stAcao = "/WEB-INF/jsptjgo/SaidaTemporaria.jsp";
								request.setAttribute("permissaoEditarEvento", "false");
							} else {
								listaEventoSaidaTemporaria = (List) request.getSession().getAttribute("listaEventoSaidaTemporaria");
								processoEventoExecucaoDt = null;
								processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventoSaidaTemporaria.get(Funcoes.StringToInt(posicaoLista));
								stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
							}
							break;
							
						case 6: //Imprime relatório de saída temporária
							saidaTemporariaBean = setDadosSaidaTemporariaBean(request, saidaTemporariaBean, processoDt.getId(), processoDt.getProcessoNumeroCompleto());
							listaEventoSaidaTemporaria = (List) request.getSession().getAttribute("listaEventoSaidaTemporaria");
							saidaTemporariaBean.setAno(request.getParameter("Ano"));
							saidaTemporariaBean = processoEventoExecucaoNe.montarBeanSaidaTemporaria(processoDt, listaEventos, listaEventoSaidaTemporaria, calculoLiquidacaoDt,	saidaTemporariaBean.getAno());
							request.setAttribute("SaidaTemporariaBean", saidaTemporariaBean);
							
							byTemp = processoEventoExecucaoNe.relSaidaTemporaria(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , saidaTemporariaBean, UsuarioSessao.getUsuarioDt());
													    
						    enviarPDF(response, byTemp, "RelatorioDeSaidaTemporaria");															
							
						    byTemp = null;
						    return;
					}
				break;
				
			//exclui o evento da lista de eventos
			// foi utilizado o curinga8, pois não é necessário fazer o tratamento se o pedido foi enviado mais de uma vez, e não são todos os usuários que têm permissão para esta exclusão
			case Configuracao.Curinga8:
				boolean excluiu = false;
				
				if (posicaoLista.equals("-1")){//exclusão de vários eventos
					String[] idExcluir = request.getParameterValues("chkExcluir[]");
					if (idExcluir != null){
						List listaId = new ArrayList();
						for (int i=0; i<idExcluir.length; i++){
							listaId.add(idExcluir[i]);
						}	

						List listaEventoExcluir = new ArrayList();
						for (String id : (List<String>)listaId) {
							for (ProcessoEventoExecucaoDt evento : (List<ProcessoEventoExecucaoDt>)listaEventos) {
								if (evento.getId().equals(id)){
									listaEventoExcluir.add(evento);
								}
							}
						}

						processoEventoExecucaoNe.excluirCompleto(listaEventoExcluir);
						excluiu = true;
					} else {
						MensagemErro = "Selecione os eventos para exclusão.";
						excluiu = false;
					}
				} else {
					//verifica em qual lista houve a exclusão do evento
					processoEventoExecucaoDt = null;
					if (request.getParameter("listaEvento") != null && request.getParameter("listaEvento").length() > 0){
						if (request.getParameter("listaEvento").equals("psc"))
							processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoPsc.get(Funcoes.StringToInt(posicaoLista));
						else if (request.getParameter("listaEvento").equals("pec"))
							processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoPec.get(Funcoes.StringToInt(posicaoLista));
						else if (request.getParameter("listaEvento").equals("lfs"))
							processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoLfs.get(Funcoes.StringToInt(posicaoLista));
						else if (request.getParameter("listaEvento").equals("itd"))
							processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoItd.get(Funcoes.StringToInt(posicaoLista));
						else if (request.getParameter("listaEvento").equals("pcb"))
							processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoPcb.get(Funcoes.StringToInt(posicaoLista));
					} else processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventos.get(Funcoes.StringToInt(posicaoLista));
					
					MensagemErro = processoEventoExecucaoNe.VerificarExclusao(processoEventoExecucaoDt, listaEventos);
					
					if (Mensagem.length() > 0) {
						excluiu = false;
					} else {
						processoEventoExecucaoNe.excluirCompleto(processoEventoExecucaoDt);
						excluiu = true;
					}
				}
				
				if (excluiu){
					request.setAttribute("MensagemOk", "Exclusão feita com sucesso!");
					if (passoEditarListaEvento.equals("2")){
						//lista de eventos de acordo com o processo e a movimentação
						listaEventos = processoEventoExecucaoNe.listarEventos(id_Processo, id_Movimentacao);
						if ((listaEventos.size() == 0) && (request.getParameter("MovimentacaoDataRealizacaoTipo")!=null)){
							ProcessoEventoExecucaoDt processoEventoExecucaoDtTemp = new ProcessoEventoExecucaoDt();
							processoEventoExecucaoDtTemp.setId_Movimentacao(request.getParameter("Id_Movimentacao"));
							processoEventoExecucaoDtTemp.setMovimentacaoDataRealizacaoTipo(request.getParameter("MovimentacaoDataRealizacaoTipo"));
							listaEventos.add(processoEventoExecucaoDtTemp);
							request.setAttribute("MensagemErro", "Não existe(m) Evento(s) cadastrado(s) para esta movimentação.");
						}
						processoEventoExecucaoNe.montarListaEventos(listaEventos, UsuarioSessao.getUsuarioDt(), null, id_Processo, Funcoes.StringToInt(calculoLiquidacaoDt.getQtdeTempoHorasEstudo()));
						
					} else if (passoEditarListaEvento.equals("1")){
						//lista de eventos de acordo com o processo
						HashMap map1 = processoEventoExecucaoNe.montarListaEventosCompleta(id_Processo, UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
						listaEventos =  (List)map1.get("listaEventos");
						listaHistoricoPsc = (List)map1.get("listaHistoricoPsc");
						listaHistoricoPec = (List)map1.get("listaHistoricoPec");
						listaHistoricoLfs = (List)map1.get("listaHistoricoLfs");
						listaHistoricoItd = (List)map1.get("listaHistoricoItd");
						listaHistoricoPcb = (List)map1.get("listaHistoricoPcb");
					}
				} else {
					request.setAttribute("MensagemErro", MensagemErro);
				}
				
				if (passoEditarListaEvento.equals("2")){
					stAcao = "/WEB-INF/jsptjgo/ListaProcessoEventoExecucao.jsp";
				}
				else if (passoEditarListaEvento.equals("1")){
					stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
				}
				
				break;
				
			//permissão utilizada no cálculo de liquidação de pena. Menu Opções -> Calcular Liquidacao
//			case Configuracao.Curinga9:
//				break;
				
			// Consulta os crimes
	        case (CrimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
	        	if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Crime","Lei","Artigo"};
					String[] lisDescricao = {"Crime"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					if (posicaoLista.length() == 0) {
						request.setAttribute("tempBuscaId","Id_CrimeExecucao");
						request.setAttribute("tempBuscaDescricao","CrimeExecucao");
	                    request.getSession().setAttribute("displayNovaCondenacao", "block");
	                } else {
	                    request.setAttribute("tempBuscaId", "Id_CrimeExecucao_" + posicaoLista);
	                    request.setAttribute("tempBuscaDescricao", "CrimeExecucao_" + posicaoLista);
	                }
					 
					request.setAttribute("tempBuscaPrograma","CrimeExecucao");
					request.setAttribute("tempRetorno","ProcessoEventoExecucao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (CrimeExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("tempFluxo1", "1"); //mesmo passo editar
				} else {
					String stTemp = "";
					stTemp = processoEventoExecucaoNe.consultarDescricaoCrimeExecucaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
	            break;
	            
			//Consulta os Eventos disponíveis 
			case (EventoExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				Mensagem = processoEventoExecucaoNe.podeAlterarEvento(processoEventoExecucaoDt);
			if (Mensagem.length() == 0){
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Digite o Evento"};
					String[] lisDescricao = {"Descrição do Evento","Observação do Evento","Tipo do Evento"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EventoExecucao");
					request.setAttribute("tempBuscaDescricao","EventoExecucao");
					request.setAttribute("tempBuscaPrograma","Consulta de Evento");			
					request.setAttribute("tempRetorno","ProcessoEventoExecucao?PassoEditar=2");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EventoExecucaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";

					stTemp = processoEventoExecucaoNe.consultarEventoExecucaoJSON(stNomeBusca1, processoEventoExecucaoDt.getEventoExecucaoDt().getId(), PosicaoPaginaAtual);						

						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
		} else {
			request.setAttribute("MensagemErro", Mensagem); 
			stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
		}
				break;
				
			//Consulta as Movimentações disponíveis
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"Tipo de Movimentação"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPemissao = String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_MovimentacaoTipo", "MovimentacaoTipo", "Tipo de Movimentação", "AnalisarVotoEmenta", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					
				}else{
					String stTemp = "";
					stTemp = processoEventoExecucaoNe.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), tempNomeBusca, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
										
					return;
				}
				break;
								
			// Consulta de Cidades - cidadeOrigem do processo
	        case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
	        	if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					request.setAttribute("tempBuscaId", "Id_CidadeOrigem");
					request.setAttribute("tempBuscaDescricao", "CidadeOrigem");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ProcessoEventoExecucao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("tempFluxo1", "1"); //mesmo passo editar
				}else{
					String stTemp = "";
					stTemp = processoEventoExecucaoNe.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
	            break;
	            
			default:
				Mensagem = processoEventoExecucaoNe.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
				if (Mensagem.length() > 0 && passoEditar != 5){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
					return;
				} else{
					// Controles da variável passoEditar
					// passo 0: Carrega o evento selecionado para edição
					// passo 1: redireciona para a jsp de edição do EventoExecução
					// passo 2: direciona para a jsp de edição do EventoExecução
					// passo 3: direciona para a tela de inclusão de evento, referente à pagina: ListaProcessoEventoExecucao.jsp
					// passo 4: cálculo de liquidação de pena
					// passo 5: visualiza os detalhes da(s) comutação(ões)
					// passo 6: visualiza último atestado de pena a cumprir
					// passo 7: salva data de homologação do cálculo
					// passo 8: consulta dados da ação penal, na lista de eventos
					// passo 18: exclui/inclui modalidade na situação atual do processo --mesmo do ProcessoExecucaoCt
					// passo 19: exclui/inclui tipo de pena na situação atual do processo --mesmo do ProcessoExecucaoCt
					// passo 20: salva situação atual do processo --mesmo do ProcessoExecucaoCt
					// passo 21: inclui/exclui modalidade. --mesmo do ProcessoExecucaoCt
					switch (passoEditar) {
						case 0: //Carrega o evento selecionado para edição (inclusão ou alteração)
							processoEventoExecucaoDt = null;
							//se for inclusão do evento não apresenta evento cadastrado
							if (posicaoLista!=null){
								//verifica em qual lista houve a seleção do evento
								if (request.getParameter("listaEvento") != null && request.getParameter("listaEvento").length() > 0){
									if (request.getParameter("listaEvento").equals("psc"))
										processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoPsc.get(Funcoes.StringToInt(posicaoLista));
									else if (request.getParameter("listaEvento").equals("pec"))
										processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoPec.get(Funcoes.StringToInt(posicaoLista));
									else if (request.getParameter("listaEvento").equals("lfs"))
										processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoLfs.get(Funcoes.StringToInt(posicaoLista));
									else if (request.getParameter("listaEvento").equals("itd"))
										processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoItd.get(Funcoes.StringToInt(posicaoLista));
									else if (request.getParameter("listaEvento").equals("pcb"))
										processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaHistoricoPcb.get(Funcoes.StringToInt(posicaoLista));
								} else processoEventoExecucaoDt = (ProcessoEventoExecucaoDt) listaEventos.get(Funcoes.StringToInt(posicaoLista));
								
								if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
										|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))){
									request.setAttribute("listaPrisao", getListaPrisaoLC(processoEventoExecucaoNe, id_Processo));
									
									//verifica se o evento com id do LC está na lista de eventos ou pertence ao processo
									String dataInicioLivramentoCondicional = processoEventoExecucaoNe.getDataInicioLivramentoCondicional(processoEventoExecucaoDt.getId_LivramentoCondicional(), listaEventos, id_Processo, processoEventoExecucaoDt.getDataInicio());
									
									if (dataInicioLivramentoCondicional.length() == 0){
										ProcessoEventoExecucaoDt lc = processoEventoExecucaoNe.getLivramentoCondicional(id_Processo, processoEventoExecucaoDt.getDataInicio());
										dataInicioLivramentoCondicional = lc.getDataInicio();
										processoEventoExecucaoDt.setId_LivramentoCondicional(lc.getId());
									}
									processoEventoExecucaoDt.setDataInicioLivramentoCondicional(dataInicioLivramentoCondicional);
								}
								
								personalizaEvento(processoEventoExecucaoDt, request, processoEventoExecucaoNe);
								
								if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){
									if (listaParametroComutacao == null) listaParametroComutacao = processoEventoExecucaoNe.listarParametroComutacaoExecucao();
									if (listaParametroComutacao != null && listaParametroComutacao.size() > 0){
										request.setAttribute("listaParametroComutacao", listaParametroComutacao);
									}
								}

								//para o evento de conversão em prd e respectivas modalidades
								if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO)) || 
									processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.ALTERACAO_MODALIDADE))
										|| (processoEventoExecucaoNe.isModalidade(processoEventoExecucaoDt.getId_EventoExecucao()) 
												&& processoEventoExecucaoDt.getIdEventoPai().length() > 0)){
									if (processoExecucaoDt == null)
										processoExecucaoDt = new ProcessoExecucaoDt();
								
									if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO)) || 
										processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.ALTERACAO_MODALIDADE))) {
										processoExecucaoDt.setListaModalidade(processoEventoExecucaoNe.listarModalidadesDoEvento(processoEventoExecucaoDt.getId()));
										
									} else {
										processoExecucaoDt.setListaModalidade(processoEventoExecucaoNe.listarModalidadesDoEvento(processoEventoExecucaoDt.getIdEventoPai()));
										for (ProcessoEventoExecucaoDt pee : (List<ProcessoEventoExecucaoDt>) listaEventos) {
											if (pee.getId().equals(processoEventoExecucaoDt.getIdEventoPai())){
												processoEventoExecucaoDt = pee;
											}
										}	
									}
									request.getSession().setAttribute("ProcessoExecucaodt_PE", processoExecucaoDt);
					                request.getSession().setAttribute("ProcessoExecucao", processoExecucaoDt);
					                
								}
								
								processoEventoExecucaoDt.setId_UsuarioServentia(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
								
								if (processoEventoExecucaoNe.isVinculoEvento_TJ(processoEventoExecucaoDt.getId_EventoExecucao())){
									processoEventoExecucaoDt.setListaTJ(processoEventoExecucaoNe.listarTransitoComTotalCondenacao_HashMap(id_Processo, processoEventoExecucaoDt.getId(), processoEventoExecucaoDt.getId_EventoExecucao()));
								}
								
								//carrega os dados da ação penal na tela de edição do evento
								if (processoEventoExecucaoDt.isManterAcaoPenal() && processoEventoExecucaoDt.getIdEventoPai().length() == 0){
									processoExecucaoDt = null;
					                if (processoEventoExecucaoDt.getId_ProcessoExecucao().length() == 0){
					                	request.setAttribute("MensagemErro", "Ação penal não identificada!");
					                	break;
					                }
					                processoExecucaoDt = processoEventoExecucaoNe.consultarAcaoPenalComCondenacao(processoEventoExecucaoDt.getId_ProcessoExecucao());
					                if (processoExecucaoDt != null) {
					                	processoExecucaoDt.setProcessoDt(processoDt);
					                } else {
					                	request.setAttribute("MensagemErro", "Ação penal não identificada!");
					                	break;
					                }
					                //verifica se a pena é medida de segurança para controle da jsp
					                processoExecucaoDt.validarMedidaSeguranca();
					                
					                request.getSession().setAttribute("ProcessoExecucaodt_PE", processoExecucaoDt);
					                request.getSession().setAttribute("ProcessoExecucao", processoExecucaoDt);
								}
								
								if (paginaAnterior == Configuracao.Localizar)
									request.getSession().setAttribute("PaginaPai", "ProcessoEventoExecucao?PaginaAnterior=2");
								else request.getSession().setAttribute("PaginaPai", "ProcessoEventoExecucao");
							}
							stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
							break;
	
						case 1: //Redireciona para a tela de edição do Evento 
							stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
							if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
									|| processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))){
								request.setAttribute("listaPrisao", getListaPrisaoLC(processoEventoExecucaoNe, id_Processo));
							}
							break;
						
						case 2: //Direciona para a tela de edição do Evento Execucao (quando clica na lupa e escolhe o evento)
							if (processoEventoExecucaoNe.isVinculoEvento_TJ(processoEventoExecucaoDt.getId_EventoExecucao())){
								processoEventoExecucaoDt.setListaTJ(processoEventoExecucaoNe.listarTransitoComTotalCondenacao_HashMap(id_Processo, processoEventoExecucaoDt.getId(), processoEventoExecucaoDt.getId_EventoExecucao()));
							
								if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){
									if (listaParametroComutacao == null) listaParametroComutacao = processoEventoExecucaoNe.listarParametroComutacaoExecucao();
									if (listaParametroComutacao != null && listaParametroComutacao.size() > 0){
										request.setAttribute("listaParametroComutacao", listaParametroComutacao);
									}
								}
							}
							personalizaEvento(processoEventoExecucaoDt, request, processoEventoExecucaoNe);
							if (!processoEventoExecucaoDt.getId().equals("")){//ALTERAÇÃO DO EVENTO
								//verifica se o evento a ser alterado não é TJ ou GRP (dados do objeto)
								if (!processoEventoExecucaoDtAnterior.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))
										&& !processoEventoExecucaoDtAnterior.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))){
									
									//verifica se o evento selecionado é TJ ou GRP (dados do request)
									if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO))
											|| processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))){
										request.setAttribute("MensagemErro", "Este evento não pode ser alterado para Trânsito em Julgado ou Guia de Recolhimento Provisória");
										processoEventoExecucaoDt = processoEventoExecucaoNe.copiar(processoEventoExecucaoDtAnterior);
										
									//verifica se o evento selecionado é de pena restritiva de direito: a inclusão dos eventos informando as penas restritivas de direito deve ser feita pela ação penal
									} else if (processoEventoExecucaoNe.isEventoPenaRestritivaDireito(processoEventoExecucaoDt.getId_EventoExecucao())){
										request.setAttribute("MensagemErro", "Ação não permitida. (Motivo: Este evento deve ser incluído a partir da Ação Penal)");
										processoEventoExecucaoDt = processoEventoExecucaoNe.copiar(processoEventoExecucaoDtAnterior);
											
									} else processoEventoExecucaoDt.setEventoExecucaoDt(processoEventoExecucaoNe.consultarEventoExecucao(processoEventoExecucaoDt.getId_EventoExecucao()));
									
								} else processoEventoExecucaoDt.setEventoExecucaoDt(processoEventoExecucaoNe.consultarEventoExecucao(processoEventoExecucaoDt.getId_EventoExecucao()));	
							}
							else {//INCLUSÃO DO EVENTO
								//a inclusão dos eventos informando as penas restritivas de direito deve ser feita pela ação penal
								if (processoEventoExecucaoNe.isEventoPenaRestritivaDireito(processoEventoExecucaoDt.getId_EventoExecucao())){
									request.setAttribute("MensagemErro", "Verifique! Não é permitido o cadastro do evento. (Motivo: Este evento deve ser incluído a partir da Ação Penal)");
									processoEventoExecucaoDt.setId_EventoExecucao("");
									processoEventoExecucaoDt.setEventoExecucao("");
									processoEventoExecucaoDt.getEventoExecucaoDt().setId("");
									processoEventoExecucaoDt.getEventoExecucaoDt().setEventoExecucao("");
								}
								else {
									//validações quando for inclusão de livramento e revogação do livramento
									if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL)) 
											|| processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
											|| processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))){
										List listaTipoEvento = new ArrayList(3);
										listaTipoEvento.add(0,String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL));
										listaTipoEvento.add(1,String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL));
										listaTipoEvento.add(2,String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO));
										List ultimoEvento = processoEventoExecucaoNe.VerificarListaLCeRLC(id_Processo, listaTipoEvento);
										String tipoEvento = "";
										String idEvento = "";
										String dataInicioEvento = "";
										if (ultimoEvento!=null && ultimoEvento.size()>0){
											tipoEvento = (String) ultimoEvento.get(0);
											idEvento = (String) ultimoEvento.get(1);
											dataInicioEvento = (String) ultimoEvento.get(2);
										}
										if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))){
											//se for inclusão de CONCESSAO DE LIVRAMENTO CONDICIONAL
											//não pode existir um livramento anterior
											if (tipoEvento.equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))){
												request.setAttribute("MensagemErro", "Verifique! Não é permitido o cadastro do evento, pois existe uma Concessão de Livramento Condicional sem uma Revogação/Suspensão referente.");
												processoEventoExecucaoDt.getEventoExecucaoDt().setId("");
												processoEventoExecucaoDt.getEventoExecucaoDt().setEventoExecucao("");
											}
										} else if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
												|| processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO))){
											//se for inclusão de REVOGACAO DE LIVRAMENTO CONDICIONAL ou SUSPENSÃO DO LIVRAMENTO CONDICIONAL
											//não pode existir uma revogação ou suspensão anterior e não ter um livramento
											if ((ultimoEvento.size()==0) || (tipoEvento.equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))) || (tipoEvento.equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO)))){
												request.setAttribute("MensagemErro", "Verifique! Não é permitido o cadastro do evento, pois para uma Revogação ou Suspensão é preciso ter uma Concessão de Livramento Condicional cadastrada primeiramente.");
												processoEventoExecucaoDt.getEventoExecucaoDt().setId("");
												processoEventoExecucaoDt.getEventoExecucaoDt().setEventoExecucao("");
											} else{
												processoEventoExecucaoDt.setId_LivramentoCondicional(idEvento);
												processoEventoExecucaoDt.setDataInicioLivramentoCondicional(dataInicioEvento);
												request.setAttribute("listaPrisao", getListaPrisaoLC(processoEventoExecucaoNe, id_Processo));
											}
										}
									}
								}
							}
							stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
							break;
						
						case 3: //Direciona para a tela de inclusão de evento: referente à pagina ListaProcessoEventoExecucao.jsp e menu: Incluir Evento de Execução (incluir evento - processo físico) 
							if (processoDt.getId_ProcessoPrincipal().length() > 0){
								request.setAttribute("MensagemErro", "Não é possíve incluir evento neste processo (Processo apenso).");
								stAcao = "/WEB-INF/jsptjgo/ListaProcessoEventoExecucao.jsp";
							} else {
								setListaRegimeRequest(processoEventoExecucaoNe, request);
								//menu "incluir evento"
								if (request.getParameter("IncluirEvento") != null && request.getParameter("IncluirEvento").equalsIgnoreCase("true")){
									if (request.getParameter("telaOrigem").equals("Processo"))
										passoEditarListaEvento = "0"; //origem: menu "opções processo"
									else passoEditarListaEvento = "1"; //origem: menu "opções" da lista de eventos
									processoEventoExecucaoDt.limpar();
									MovimentacaoDt movi = processoDt.getPrimeiraMovimentacaoLista();
									if (movi!=null){
										processoEventoExecucaoDt.setId_Movimentacao(movi.getId());
										processoEventoExecucaoDt.setMovimentacaoTipo(movi.getMovimentacaoTipo());
										processoEventoExecucaoDt.setMovimentacaoDataRealizacaoTipo(movi.getDataRealizacao() + " - " + processoEventoExecucaoDt.getMovimentacaoTipo());
									}
								//inclusão do evento pela movimentação
								} else if (processoEventoExecucaoDt.getId_Movimentacao().length() > 0){
										String movimentacao = processoEventoExecucaoDt.getMovimentacaoDataRealizacaoTipo();
										String idMovimentacao = processoEventoExecucaoDt.getId_Movimentacao();
										processoEventoExecucaoDt.limpar();
										processoEventoExecucaoDt.setMovimentacaoDataRealizacaoTipo(movimentacao);
										processoEventoExecucaoDt.setId_Movimentacao(idMovimentacao);
								}
								stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
							}
							break;
						
						case 4: //calcula de liquidação de penas
							stAcao = "/WEB-INF/jsptjgo/CalculoLiquidacao.jsp";
							request.setAttribute("TituloPagina", "Cálculo de Liquidação de Penas");
							// variável que controla o tipo de cálculo de liquidação de pena
							if (calculoLiquidacaoDt != null)
								setDadosCalculoLiquidacao(request, calculoLiquidacaoDt);
							if (calculoLiquidacaoDt.getListaTipoCalculo() != null){
								Mensagem = processoEventoExecucaoNe.verificarCalculo(listaEventos, calculoLiquidacaoDt);
								if (Mensagem.length() > 0) request.setAttribute("MensagemErro", Mensagem);
								else {
									//consulta se já existe calculo cadastrado para o processo de execucao
									if (id_Processo.length()>0){
										calculoLiquidacaoDt.setIdProcesso(id_Processo);
										calculoLiquidacaoDt = processoEventoExecucaoNe.consultarCalculoLiquidacao(calculoLiquidacaoDt);
									}
									//mantém os dados do último cálculo
									String informacaoAdicional = calculoLiquidacaoDt.getInformacaoAdicional();
									String idCalculoLiquidacao = calculoLiquidacaoDt.getId();
									
									calcularLiquidacaoDePena(request, processoEventoExecucaoNe, listaEventos, listaHistoricoPsc, listaHistoricoLfs, listaHistoricoPec, listaHistoricoItd, listaHistoricoPcb, calculoLiquidacaoDt, processoDt.getPrimeiroPoloPassivoDataNascimento(), id_Processo, UsuarioSessao.getUsuarioDt(), listaCondenacaoExtinta);
									stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
									
									//mantém os dados do último cálculo
									calculoLiquidacaoDt.setInformacaoAdicional(informacaoAdicional);
									calculoLiquidacaoDt.setId(idCalculoLiquidacao);									
								}
							} else request.setAttribute("MensagemErro", "Selecione a opção de Cálculo de Liquidação de Penas!");
							break;
							
						// visualiza os detalhes da(s) comutação(ões)
						case 5:
							List lista = processoEventoExecucaoNe.calcularTempoComutacao(id_Processo, listaEventos, null);
							stAcao = "/WEB-INF/jsptjgo/Comutacao.jsp";
							request.setAttribute("listaTJComutacao", lista);	
							break;
						
						//visualiza último atestado de pena a cumprir
						case 6:
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							DataProvavelDt calculo = (DataProvavelDt)request.getSession().getAttribute("ultimoCalculo");
							
							//visualização do atestado pelo ícone na capa do processo
							if (calculo == null || calculo.getRelatorioByte() == null) {
								calculo = new DataProvavelDt();
								calculo = processoEventoExecucaoNe.consultarCalculoLiquidacao(processoDt.getId_Processo());
								request.getSession().setAttribute("ultimoCalculo", calculo);
								stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
							}
							
							if (calculo.getRelatorioByte() != null) {
								byTemp = calculo.getRelatorioByte();
																								
								enviarPDF(response,byTemp,"Relatorio");
								
								return;	
							} else{
								request.setAttribute("MensagemErro", "Atestado de Pena a cumprir não localizado");
							}
							break;
							
						// salva data de homologação do cálculo
						case 7:
							DataProvavelDt dataProvavelDt = (DataProvavelDt)request.getSession().getAttribute("ultimoCalculo");
							dataProvavelDt.setDataHomologacao(request.getParameter("dataHomologacao"));
							dataProvavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							dataProvavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
							processoEventoExecucaoNe.salvarDataHomologacaoCalculoLiquidacao(dataProvavelDt, true);
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							break;
							
						//consulta dados da ação penal, na lista de eventos --mesmo passo editar do BuscaProcessoPublica, BuscaProcesso, BuscaProcessoUsuarioExterno
						case 8: 
							String stTemp = "";
							request.setAttribute("tempRetorno", "ProcessoEventoExecucao");
							
							stTemp = processoEventoExecucaoNe.consultarIdJSON(request.getParameter("idProcessoExecucao"));
															
							enviarJSON(response, stTemp);
															
							return;
						
						//exclui/inclui modalidades na situação atual do processo
						case 18: 
							if (posicaoLista != null && !posicaoLista.equalsIgnoreCase("-1") && posicaoLista.length() > 0){//exclui modalidade
								SituacaoAtualModalidadeDt modalidade = (SituacaoAtualModalidadeDt) situacaoAtualExecucaoDt.getListaSituacaoAtualModalidadeDt().get(Funcoes.StringToInt(posicaoLista));
								if (modalidade.getId().length() > 0){
									modalidade.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		                			modalidade.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		                			processoEventoExecucaoNe.excluirModalidadeSituacaoAtual(modalidade, UsuarioSessao.getUsuarioDt());
		                		}
								situacaoAtualExecucaoDt.getListaSituacaoAtualModalidadeDt().remove(Funcoes.StringToInt(posicaoLista));
			                } else {//inclui modalidade
			                	processoEventoExecucaoNe.adicionarModalidadeSituacaoAtual(situacaoAtualExecucaoDt);
			                }
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
			                break;
				                
			            //exclui/inclui tipo de pena na situação atual do processo
						case 19: 
							if (posicaoLista != null && !posicaoLista.equalsIgnoreCase("-1") && posicaoLista.length() > 0){//exclui tipo de pena
								SituacaoAtualTipoPenaDt tipoPena = (SituacaoAtualTipoPenaDt) situacaoAtualExecucaoDt.getListaSituacaoAtualTipoPenaDt().get(Funcoes.StringToInt(posicaoLista));
								if (tipoPena.getId().length() > 0){
									tipoPena.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
									tipoPena.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		                			processoEventoExecucaoNe.excluirTipoPenaSituacaoAtual(tipoPena, UsuarioSessao.getUsuarioDt());
		                		}
								situacaoAtualExecucaoDt.getListaSituacaoAtualTipoPenaDt().remove(Funcoes.StringToInt(posicaoLista));
			                } else {//inclui tipo de pena
			                	processoEventoExecucaoNe.adicionarTipoPenaSituacaoAtual(situacaoAtualExecucaoDt);
			                }
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
			                break;
			                
			            //salva situação atual do processo
						case 20:
							Mensagem = processoEventoExecucaoNe.verificarSituacaoAtualExecucao(situacaoAtualExecucaoDt);
							if (Mensagem.length() == 0){
								processoEventoExecucaoNe.salvarSituacaoAtualExecucao(situacaoAtualExecucaoDt);
								request.setAttribute("MensagemOk", "Dados salvo com sucesso");
							} else {
								request.setAttribute("MensagemErro", Mensagem);
							}
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							break;
							
				        //exclui/inclui modalidades no processo --mesmo do ProcessoExecucaoCt
						case 21: 
							if (posicaoLista != null && posicaoLista.length() > 0){//exclui modalidade
		                		ProcessoEventoExecucaoDt modalidade = (ProcessoEventoExecucaoDt) processoExecucaoDt.getListaModalidade().get(Funcoes.StringToInt(posicaoLista));
		                		if (modalidade.getId().length() > 0){
		                			modalidade.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		                			modalidade.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		                			processoEventoExecucaoNe.excluirCompleto(modalidade);
		                		}
			                	processoExecucaoDt.getListaModalidade().remove(Funcoes.StringToInt(posicaoLista));
			                } else {//inclui modalidade
			                	Mensagem = processoEventoExecucaoNe.adicionarModalidadeProcessoExecucao(processoExecucaoDt);
			         	        if (Mensagem.length() > 0) request.setAttribute("MensagemErro", Mensagem);
			                }
				            stAcao = "/WEB-INF/jsptjgo/ProcessoEventoExecucaoEditar.jsp";
			                break;
				                
						//direciona para a tela de visualização dos eventos
						default:
							stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
							break;
					}
				}
			break;
		}
		
		if (processoEventoExecucaoDt != null && !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals("")){
			request.setAttribute("InformarQuantidade", processoEventoExecucaoNe.isInformarQuantidade(processoEventoExecucaoDt.getEventoExecucaoDt().getId()));
		}
		if (processoEventoExecucaoDt != null && !processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals("")){
			request.setAttribute("InformarObs", processoEventoExecucaoNe.isInformarObs(processoEventoExecucaoDt.getEventoExecucaoDt().getId()));
		}
		if (processoEventoExecucaoDt != null && processoEventoExecucaoDt.getObservacaoAux().length() > 0 
				|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA))
				|| processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){
			if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA)))
				request.setAttribute("ObservacaoAuxiliar", "Data do TJ");
			else request.setAttribute("ObservacaoAuxiliar", "Observação Complementar");
		}

		// Prepara menu especial de Liquidação
		String Menu = UsuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesLiquidacaoCodigo);
		request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesLiquidacaoCodigo, Menu);
		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("PassoEditar", passoEditar);
		
		//utilizado nas jsps para controlar a interface de cálculo e evento para os tipos de pena: PPL e PRD
		request.setAttribute("isPenaRestritivaDireito", processoEventoExecucaoNe.isProcessoPenaRestritivaDireito(listaEventos));
		
		request.getSession().setAttribute("PassoEditarListaEvento", passoEditarListaEvento);
		request.getSession().setAttribute("ProcessoEventoExecucaodt", processoEventoExecucaoDt);
		request.getSession().setAttribute("SituacaoDt", situacaoAtualExecucaoDt);
		request.getSession().setAttribute("ProcessoEventoExecucaone", processoEventoExecucaoNe);
		request.getSession().setAttribute("listaEventos", listaEventos); //utilizado na jsp
		request.getSession().setAttribute("listaHistoricoPsc", listaHistoricoPsc); //utilizado na jsp
		request.getSession().setAttribute("listaHistoricoPec", listaHistoricoPec); //utilizado na jsp
		request.getSession().setAttribute("listaHistoricoLfs", listaHistoricoLfs); //utilizado na jsp
		request.getSession().setAttribute("listaHistoricoItd", listaHistoricoItd); //utilizado na jsp
		request.getSession().setAttribute("listaHistoricoPcb", listaHistoricoPcb); //utilizado na jsp
		request.getSession().setAttribute("listaEventosDataBase", listaEventosDataBase); //utilizado na jsp
		request.getSession().setAttribute("listaParametroComutacao", listaParametroComutacao); //utilizado na jsp
		request.getSession().setAttribute("listaCondenacaoExtinta", listaCondenacaoExtinta); //utilizado na jsp
		request.getSession().setAttribute("CalculoLiquidacaodt", calculoLiquidacaoDt);
		request.getSession().setAttribute("id_Processo", id_Processo);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected void setDadosEdicaoEvento(ProcessoEventoExecucaoNe processoEventoExecucaoNe, ProcessoEventoExecucaoDt processoEventoExecucaoDt, HttpServletRequest request, UsuarioNe UsuarioSessao, String idProcesso) throws Exception{
		// variáveis utilizadas na edição dos eventos
		processoEventoExecucaoDt.setId_Movimentacao(request.getParameter("Id_Movimentacao"));
		processoEventoExecucaoDt.setMovimentacaoDataRealizacaoTipo(request.getParameter("MovimentacaoDataRealizacaoTipo"));
		processoEventoExecucaoDt.setId_EventoExecucao(request.getParameter("Id_EventoExecucao"));
		processoEventoExecucaoDt.setEventoExecucao(request.getParameter("EventoExecucao"));
		processoEventoExecucaoDt.setDataInicio(request.getParameter("DataInicio"));
		processoEventoExecucaoDt.setConsiderarTempoLivramentoCondicional(request.getParameter("radioConsiderarLivramento"));
		processoEventoExecucaoDt.setId_LivramentoCondicional(request.getParameter("Id_LivramentoCondicional"));
		processoEventoExecucaoDt.setDataPrisaoRevogacaoLC(request.getParameter("radioPrisao"));
		processoEventoExecucaoDt.setDataDecretoComutacao(request.getParameter("DataDecretoSelecionado"));
		if (request.getParameter("Observacao") != null) processoEventoExecucaoDt.setObservacao(request.getParameter("Observacao"));
		if (request.getParameter("Quantidade") != null) processoEventoExecucaoDt.setQuantidade(request.getParameter("Quantidade").trim());
		if ((request.getParameter("Id_EventoExecucao")!=null) && (!request.getParameter("Id_EventoExecucao").equals(""))) {
			processoEventoExecucaoDt.setEventoExecucaoDt(processoEventoExecucaoNe.consultarEventoExecucao(request.getParameter("Id_EventoExecucao")));
		}
		if (request.getParameter("ObservacaoAux") != null) processoEventoExecucaoDt.setObservacaoAux(request.getParameter("ObservacaoAux"));
		
		processoEventoExecucaoDt.getEventoExecucaoDt().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoEventoExecucaoDt.getEventoExecucaoDt().setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		processoEventoExecucaoDt.getEventoRegimeDt().setId_RegimeExecucao(request.getParameter("Id_RegimeExecucao"));
		processoEventoExecucaoDt.getEventoRegimeDt().setRegimeExecucao(request.getParameter("RegimeExecucao"));
		processoEventoExecucaoDt.getEventoRegimeDt().setId_ProcessoEventoExecucao(request.getParameter("Id_ProcessoEventoExecucao"));
		processoEventoExecucaoDt.getEventoRegimeDt().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoEventoExecucaoDt.getEventoRegimeDt().setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
	
		processoEventoExecucaoDt.getEventoLocalDt().setId_LocalCumprimentoPena(request.getParameter("Id_LocalCumprimentoPena"));
		processoEventoExecucaoDt.getEventoLocalDt().setLocalCumprimentoPena(request.getParameter("LocalCumprimentoPena"));
		processoEventoExecucaoDt.getEventoLocalDt().setId_ProcessoEventoExecucao(request.getParameter("Id_ProcessoEventoExecucao"));
		processoEventoExecucaoDt.getEventoLocalDt().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoEventoExecucaoDt.getEventoLocalDt().setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
	}
	
	
	protected void setDadosCalculoLiquidacao(HttpServletRequest request, CalculoLiquidacaoDt calculoLiquidacaoDt){
		calculoLiquidacaoDt.limpar();
		
		if (request.getParameterValues("chkTipoCalculo[]") != null){
			//pega as opções de cálculo do request e adiciona na lista
			String[] valoresTipoCalculo = request.getParameterValues("chkTipoCalculo[]");
			List listaTipoCalculo = new ArrayList();
			
			for (int i=0; i<valoresTipoCalculo.length; i++){
				listaTipoCalculo.add(valoresTipoCalculo[i]);
				
				//instancia o objeto se for null (está no método)
				if (valoresTipoCalculo[i].equalsIgnoreCase("PROGRESSAO")){
					calculoLiquidacaoDt.newCalculoProgressaoLivramentoDt();
				}
				else if (valoresTipoCalculo[i].equalsIgnoreCase("COMUTACAO") || valoresTipoCalculo[i].equalsIgnoreCase("COMUTACAO_UNIFICADA")){
						calculoLiquidacaoDt.newCalculoComutacaoDt();
				}
				else if (valoresTipoCalculo[i].equalsIgnoreCase("INDULTO")){
						calculoLiquidacaoDt.newCalculoIndultoDt();
				}
				else if (valoresTipoCalculo[i].equalsIgnoreCase("PENARESTRITIVA")){
						calculoLiquidacaoDt.newCalculoPenaRestritivaDt();
				}
			}
			calculoLiquidacaoDt.setListaTipoCalculo(listaTipoCalculo);
			
			if (calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt() != null){
				//pega a data base selecionada
				if (request.getParameter("radioDataBaseProgressao") != null){
					calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataBaseProgressao(request.getParameter("radioDataBaseProgressao"));
				} else if (request.getParameter("dataBasePR") != null){
					calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataBaseProgressao(request.getParameter("dataBasePR"));
				}
				if (request.getParameter("radioDataBaseLC") != null){
					calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setDataBaseLivramento(request.getParameter("radioDataBaseLC"));
				}
				//pega a opção de consideração da Reincidência Específica para o cálculo do Livramento Condicional
				if (request.getParameterValues("chkReincidenteEspecifico") != null){
					String reincidente = request.getParameter("chkReincidenteEspecifico");
					if (reincidente.equalsIgnoreCase("RE"))
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setReincidenteEspecificoLC("true");
				}
				//pega a opção de consideração da Reincidência para os crimes hediondos no cálculo da progressão de regime
				if (request.getParameterValues("chkReincidenteHediondoPR") != null){
					String reincidente = request.getParameter("chkReincidenteHediondoPR");
					if (reincidente.equalsIgnoreCase("RE_PR"))
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setReincidenteHediondoPR("true");
				}
				//pega a opção de calcular somente progressão de regime ou livramento condicional
				if (request.getParameterValues("radioSomente") != null){//valores: PR, LC, TODOS
					calculoLiquidacaoDt.setCalcularSomente(request.getParameter("radioSomente").toString());
				}
				//pega a opção de realizar o cálculo do LC, ainda que o sentenciado já esteja em Livramento Condicional
				if (request.getParameterValues("chkCalculoLivramento") != null){
					String opc = request.getParameter("chkCalculoLivramento");
					if (opc.equalsIgnoreCase("CALC_LC")){
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setForcarCalculoLC("true");
						calculoLiquidacaoDt.getCalculoProgressaoLivramentoDt().setNovoRegimeProgressao(request.getParameter("DescricaoRegime"));
					}
				}
			}
			
			//pega as opções de fração para cálculo da comutacao
			if (request.getParameter("radioDecreto") != null && calculoLiquidacaoDt.getCalculoComutacaoDt() != null){
				calculoLiquidacaoDt.getCalculoComutacaoDt().setIdParametroComutacao(request.getParameter("radioDecreto"));
			}
			
			//pega as opções de fração para cálculo do indulto e adiciona na lista
//			if (request.getParameter("radioFracaoIndulto") != null && calculoLiquidacaoDt.getCalculoIndultoDt() != null){
//				calculoLiquidacaoDt.getCalculoIndultoDt().setFracaoIndulto(request.getParameter("radioFracaoIndulto"));
//			}
			if (request.getParameter("radioFracaoIndultoComum") != null && calculoLiquidacaoDt.getCalculoIndultoDt() != null){
				calculoLiquidacaoDt.getCalculoIndultoDt().setFracaoIndultoComum(request.getParameter("radioFracaoIndultoComum"));
			}
			if (request.getParameter("radioFracaoIndultoHediondo") != null && calculoLiquidacaoDt.getCalculoIndultoDt() != null){
				calculoLiquidacaoDt.getCalculoIndultoDt().setFracaoIndultoHediondo(request.getParameter("radioFracaoIndultoHediondo"));
			}
			
			//pega a opção de como considerar o tempo de remição no cálculo
			//tipo 1: deduzir o tempo de Remição após a data base, direto no Requisito Temporal
			//tipo 2: considerar todo o tempo de Remição (antes e depois da data base) como Tempo Cumprido
			if (request.getParameter("radioRemicao") != null){
				calculoLiquidacaoDt.setTipoRemicao(request.getParameter("radioRemicao"));
			}
			if (request.getParameter("radioTerminoPena") != null){
				calculoLiquidacaoDt.setVisualizaPenaUnificada(request.getParameter("radioTerminoPena"));
			}
			if (request.getParameter("radioRestantePena") != null){
				calculoLiquidacaoDt.setVisualizaRestantePenaUltimoEvento(request.getParameter("radioRestantePena"));
			}
			
			//verifica a opção de qtde de Horas de Estudo a serem utilizadas no cálculo de remição (12 ou 18)
			if (request.getParameter("radioHorasEstudo") != null){
				calculoLiquidacaoDt.setQtdeTempoHorasEstudo(request.getParameter("radioHorasEstudo"));
			}
			
			//verifica se iniciou o cumprimento da pena para o cálculo da prescrição executória individual
			if (request.getParameterValues("chkIniciou") != null){
				if (request.getParameter("chkIniciou").toString().equalsIgnoreCase("N"))
					calculoLiquidacaoDt.setIniciouCumprimentoPena(false);
				else calculoLiquidacaoDt.setIniciouCumprimentoPena(true);
			}
			
			//verifica se iniciou o cumprimento da pena para o cálculo da prescrição executória individual
			if (request.getParameterValues("chkDetracao") != null){
				if (request.getParameter("chkDetracao").toString().equalsIgnoreCase("N"))
					calculoLiquidacaoDt.setConsiderarDetracao(false);
				else calculoLiquidacaoDt.setConsiderarDetracao(true);
			}
			
			//verifica se é para considerar o tempo do crime hediondo (conforme decreto) no saldo devedor
			if (request.getParameterValues("chkSaldoDevedorHedindoComutacao") != null){
				if (request.getParameter("chkSaldoDevedorHedindoComutacao").toString().equalsIgnoreCase("S"))
					calculoLiquidacaoDt.setSaldoDevedorCrimeHediondo(true);
				else calculoLiquidacaoDt.setIniciouCumprimentoPena(false);
			}
			
			if (request.getParameterValues("chkMulherComMenoresComutacao") != null){
				if (request.getParameter("chkMulherComMenoresComutacao").toString().equalsIgnoreCase("S"))
					calculoLiquidacaoDt.setMulherComMenores(true);
				else calculoLiquidacaoDt.setMulherComMenores(false);
			}
		} 
	}
	
	protected String setDadosEventoComutacao_RevogacaoLC(HttpServletRequest request, ProcessoEventoExecucaoDt eventoDt){
		String mensagem = "";
		//int tempoCondenacao = 0;
		
		if (!eventoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))
				&& !eventoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
				&& !eventoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO)))
			return "";
		
		if (mensagem.length() == 0 ) {
			String fracao = "";
			if (eventoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.COMUTACAO_PENA))){
				//recupera do request a fração selecionada
				if (request.getParameter("radioFracaoComutacao") != null)
					fracao = request.getParameter("radioFracaoComutacao").toString();
				else mensagem += "Selecione a fração da " + eventoDt.getEventoExecucao() + ". \n";	
			}
			
			//recupera os TJ selecionados e seta os TJ referentes na lista de TJ do objeto.
			if (!((eventoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
					|| eventoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO)))
					&& request.getParameter("radioConsiderarLivramento") != null
					&& request.getParameter("radioConsiderarLivramento").toString().equalsIgnoreCase("1")
					)){
				if (request.getParameterValues("chkTJ[]") == null) 
					mensagem += "Selecione os TJ para aplicar a " + eventoDt.getEventoExecucao() + ".";
				else {
					//chaves do hashMap: Id_ProcessoExecucao, DataTransitoJulgado, DataInicioCumprimentoPena, TempoNaoExtintoDias, TempoExtintoDias, Checked, Fracao,
					// Id_TransitoJulgadoEvento, TempoTotalDias, TempoTotalAnos, TempoExtintoAnos, TempoNaoExtintoAnos
					String[] listaTJ = request.getParameterValues("chkTJ[]");
					int w = 0;
					
					for (int i=0; i<eventoDt.getListaTJ().size(); i++){
						if (w < listaTJ.length){
							//verifica se a posição do TJ do objeto é a mesma posição contida na lista de TJ do request (TJ selecionados)
							if (i == Funcoes.StringToInt(listaTJ[w])){
								((HashMap)eventoDt.getListaTJ().get(i)).put("Checked", "1");
								((HashMap)eventoDt.getListaTJ().get(i)).put("Fracao", fracao);
								w++;
							} else {
								((HashMap)eventoDt.getListaTJ().get(i)).put("Checked", "0");
							}
						} else {
							((HashMap)eventoDt.getListaTJ().get(i)).put("Checked", "0");
						}
					}
				}
			}
		}
		
//-------------------------------------------------------------
//**Retirado estas linhas, pois o cálculo abaixo é um cálculo automático do tempo a ser comutado, e conforme Ata de Reunião nº 22, a quantidade de dias a comutar
//** deve ser informada pelo usuário, já que pode variar a forma de calcular o tempo a ser comutado (a Quantidade é colocada no objeto no método .setDadosEdicaoEvento()
//-------------------------------------------------------------		
		//calcula o tempo em dias desta comutação 
//    	int quantidadeDias = 0;
//    	for (int i=0; i<eventoDt.getListaTJ().size(); i++){
//    		HashMap mapTJ = (HashMap)eventoDt.getListaTJ().get(i);
//    		if(mapTJ.get("Checked").equals("1")){
//    			quantidadeDias += Funcoes.StringToInt(mapTJ.get("TempoNaoExtintoDias").toString()) * Funcoes.StringToInt(mapTJ.get("Fracao").toString().substring(0, 1)) / Funcoes.StringToInt(mapTJ.get("Fracao").toString().substring(2));  
//    		}
//    	}
//    	eventoDt.setQuantidade(String.valueOf(quantidadeDias));
//-------------------------------------------------------------
		return mensagem;
	}
	
	
	/**
	 * Monta a lista de eventos com os dados dos eventos, dados das condenações e dados do ProcessoExecução
	 * @param lista, lista dos eventos (ProcessoEventoExecução)
	 * @param id_processo, id do processo 
	 * @author wcsilva
	 */
	protected List montarListaEventosMovimentacao(ProcessoEventoExecucaoNe processoEventoExecucaoNe, String id_processo, String id_Movimentacao, UsuarioDt usuarioDt) throws Exception{
	
		List lista = processoEventoExecucaoNe.listarEventos(id_processo, id_Movimentacao);
		processoEventoExecucaoNe.montarListaEventos(lista, usuarioDt, null, id_processo, 12);
		
		return lista;
	}
	
	protected void calcularLiquidacaoDePena(HttpServletRequest request, ProcessoEventoExecucaoNe processoEventoExecucaoNe, List listaEventos, List listaEventosPSC, List listaEventosLFS, List listaEventosPEC, List listaEventosITD, List listaEventosPCB,
			CalculoLiquidacaoDt calculoLiquidacaoDt, String dataNascimento, String id_Processo, UsuarioDt usuarioDt,List listaCondenacaoExtinta) throws Exception{

		//monta novamente a lista de eventos para reconfigurar o tempo de horas de estudo (p/ remição) escolhido pelo usuário no momento do cálculo 
		processoEventoExecucaoNe.montarListaEventos(listaEventos, usuarioDt, listaCondenacaoExtinta, id_Processo, Funcoes.StringToInt(calculoLiquidacaoDt.getQtdeTempoHorasEstudo()));
		
		HashMap mapMsg = processoEventoExecucaoNe.calcularLiquidacaoPena(listaEventos, listaEventosPSC, listaEventosLFS, listaEventosPEC, listaEventosITD, listaEventosPCB, calculoLiquidacaoDt, dataNascimento, id_Processo);
		if (mapMsg.get("msgGeral") != null && mapMsg.get("msgGeral").toString().length() > 0) 
			request.setAttribute("MensagemErro", mapMsg.get("msgGeral").toString());
		if (mapMsg.get("msgPenaRestritiva") != null && mapMsg.get("msgPenaRestritiva").toString().length() > 0) 
			request.setAttribute("MensagemErro", mapMsg.get("msgPenaRestritiva").toString());
		request.setAttribute("mapMensagem", mapMsg);
		
		//utilizado na jsp calculoLiquidacaoResultado
		if (calculoLiquidacaoDt.getListaTipoCalculo() != null){
			for (int i=0; i<calculoLiquidacaoDt.getListaTipoCalculo().size();i++){
				String tipoCalculo = (String)calculoLiquidacaoDt.getListaTipoCalculo().get(i);
				request.setAttribute(tipoCalculo, tipoCalculo);
			}
		}
	}
	
	protected SaidaTemporariaDt setDadosSaidaTemporariaBean(HttpServletRequest request, SaidaTemporariaDt saidaTemporariaBean, String idProcesso, String numeroProcesso){
		if (request.getAttribute("SaidaTemporariaBean") != null) 
			saidaTemporariaBean = (SaidaTemporariaDt) request.getAttribute("SaidaTemporariaBean");
		if (saidaTemporariaBean == null){
			saidaTemporariaBean = new SaidaTemporariaDt();
			saidaTemporariaBean.setAno(request.getParameter("AnoSaidaTemporaria"));
			saidaTemporariaBean.setIdProcesso(idProcesso);
			saidaTemporariaBean.setNumeroProcesso(numeroProcesso);
			request.setAttribute("SaidaTemporariaBean", saidaTemporariaBean);
		}
		return saidaTemporariaBean;
	}
	
	protected void setDadosProcessoExecucao(HttpServletRequest request, UsuarioNe usuarioSessao, ProcessoExecucaoDt processoExecucaoDt, int paginaAtual) throws Exception{
    	processoExecucaoDt.setId_PenaExecucaoTipo(request.getParameter("Id_PenaExecucaoTipo"));
    	processoExecucaoDt.setPenaExecucaoTipo(request.getParameter("PenaExecucaoTipo"));
    	if (processoExecucaoDt.getId_PenaExecucaoTipo().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA)))
    		processoExecucaoDt.setId_RegimeExecucao(request.getParameter("Id_RegimeExecucao_MS"));
    	else processoExecucaoDt.setId_RegimeExecucao(request.getParameter("Id_RegimeExecucao_PPL"));
    	processoExecucaoDt.setRegimeExecucao(request.getParameter("RegimeExecucao"));
        processoExecucaoDt.setId_Modalidade(request.getParameter("Id_Modalidade"));
        processoExecucaoDt.setModalidade(request.getParameter("Modalidade"));
        processoExecucaoDt.setId_LocalCumprimentoPena(request.getParameter("Id_LocalCumprimentoPena"));
        processoExecucaoDt.setLocalCumprimentoPena(request.getParameter("LocalCumprimentoPena"));
        processoExecucaoDt.setDataAcordao(request.getParameter("DataAcordao"));
        processoExecucaoDt.setDataDistribuicao(request.getParameter("DataDistribuicao"));
        processoExecucaoDt.setDataPronuncia(request.getParameter("DataPronuncia"));
        processoExecucaoDt.setDataDenuncia(request.getParameter("DataDenuncia"));
        processoExecucaoDt.setDataSentenca(request.getParameter("DataSentenca"));
        processoExecucaoDt.setDataAdmonitoria(request.getParameter("DataAdmonitoria"));
        processoExecucaoDt.setDataTransitoJulgado(request.getParameter("DataTransito"));
        processoExecucaoDt.setDataTransitoJulgadoMP(request.getParameter("DataTJ_MP"));
        processoExecucaoDt.setDataInicioCumprimentoPena(request.getParameter("DataInicioCumprimentoPena"));
        processoExecucaoDt.setNumeroAcaoPenal(request.getParameter("NumeroAcaoPenal"));
        processoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
        processoExecucaoDt.setDataInicioSursis(request.getParameter("DataInicioSursis"));
        processoExecucaoDt.setTempoSursisAno(request.getParameter("SursisAno"));
        processoExecucaoDt.setTempoSursisMes(request.getParameter("SursisMes"));
        processoExecucaoDt.setTempoSursisDia(request.getParameter("SursisDia"));
        //fiz isso para controle de tela (para não ficar no tempo do sursis: 00-00-00)
        String tempoTotalSursisDias = Funcoes.converterParaDias(processoExecucaoDt.getTempoSursisAno(), processoExecucaoDt.getTempoSursisMes(), processoExecucaoDt.getTempoSursisDia());
        if (Funcoes.StringToInt(tempoTotalSursisDias) > 0)
        	processoExecucaoDt.setTempoTotalSursisDias(tempoTotalSursisDias);
        else processoExecucaoDt.setTempoTotalSursisDias("");
        
        // Parâmetros para auxiliar no cadastro das condenações
        processoExecucaoDt.setId_CrimeExecucao(request.getParameter("Id_CrimeExecucao"));
        processoExecucaoDt.setCrimeExecucao(request.getParameter("CrimeExecucao"));
        processoExecucaoDt.setDataFato(request.getParameter("DataFato"));
        processoExecucaoDt.setIdCondenacaoExecucaoSituacao(request.getParameter("Id_CondenacaoExecucaoSituacao"));
        processoExecucaoDt.setTempoCondenacaoAno(request.getParameter("Ano"));
        processoExecucaoDt.setTempoCondenacaoMes(request.getParameter("Mes"));
        processoExecucaoDt.setTempoCondenacaoDia(request.getParameter("Dia"));
        processoExecucaoDt.setTempoPenaEmDias(Funcoes.converterParaDias(processoExecucaoDt.getTempoCondenacaoAno(), processoExecucaoDt.getTempoCondenacaoMes(), processoExecucaoDt.getTempoCondenacaoDia()));
        processoExecucaoDt.setObservacaoCondenacao(request.getParameter("ObservacaoCondenacao"));
        
        if (request.getParameter("radioReincidente") != null){
			if (request.getParameter("radioReincidente").equalsIgnoreCase("t"))
				processoExecucaoDt.setReincidente("true");
			else if (request.getParameter("radioReincidente").equalsIgnoreCase("f"))
				processoExecucaoDt.setReincidente("false");
			else processoExecucaoDt.setReincidente("");
		}
        
        // Parâmetros para auxiliar na alteração das condenações existentes
        if (processoExecucaoDt.getListaCondenacoes() != null) {
            for (int i=0; i<processoExecucaoDt.getListaCondenacoes().size(); i++) {
                CondenacaoExecucaoDt condenacaoExecucaoDt = (CondenacaoExecucaoDt) processoExecucaoDt.getListaCondenacoes().get(i);
                condenacaoExecucaoDt.setId_CrimeExecucao(request.getParameter("Id_CrimeExecucao_" + i));
                condenacaoExecucaoDt.setCrimeExecucao(request.getParameter("CrimeExecucao_" + i));
                condenacaoExecucaoDt.setId_CondenacaoExecucaoSituacao(request.getParameter("Id_CondenacaoExecucaoSituacao_" + i));
                condenacaoExecucaoDt.setTempoCumpridoExtintoDias(request.getParameter("TempoCumpridoExtintoDias_" + i));
                condenacaoExecucaoDt.setDataFato(request.getParameter("DataFato_" + i));
                condenacaoExecucaoDt.setReincidente(request.getParameter("radioReincidente_"+i));
                condenacaoExecucaoDt.setQtdeAno(request.getParameter("Ano_" + i));
                condenacaoExecucaoDt.setQtdeMes(request.getParameter("Mes_" + i));
                condenacaoExecucaoDt.setQtdeDias(request.getParameter("Dia_" + i));
                condenacaoExecucaoDt.setTempoPenaEmDias(Funcoes.converterParaDias(condenacaoExecucaoDt.getQtdeAno(), condenacaoExecucaoDt.getQtdeMes(), condenacaoExecucaoDt.getQtdeDias()));
                condenacaoExecucaoDt.setTempoPena(condenacaoExecucaoDt.getTempoPenaEmDias());
                condenacaoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
                condenacaoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
                condenacaoExecucaoDt.setObservacao(request.getParameter("ObservacaoCondenacao_"+i));
                
                processoExecucaoDt.getListaCondenacoes().set(i, condenacaoExecucaoDt);
            }
        }

        // Parâmetros para auxiliar na alteração das modalidades existentes
        if (processoExecucaoDt.getListaModalidade() != null) {
            for (int i=0; i<processoExecucaoDt.getListaModalidade().size(); i++) {
                ProcessoEventoExecucaoDt modalidadeDt = (ProcessoEventoExecucaoDt) processoExecucaoDt.getListaModalidade().get(i);
//                if (modalidadeDt.getEventoRegimeDt().getId_RegimeExecucao().equals("11")){
                	modalidadeDt.setQuantidade(request.getParameter("QuantidadeModalidade_" + i));
//                } else {
//                	modalidadeDt.setTempoAno(request.getParameter("ModAno_" + i));
//                    modalidadeDt.setTempoMes(request.getParameter("ModMes_" + i));
//                    modalidadeDt.setTempoDia(request.getParameter("ModDia_" + i));	
//                	modalidadeDt.setQuantidade(Funcoes.converterParaDias(modalidadeDt.getTempoAno(), modalidadeDt.getTempoMes(), modalidadeDt.getTempoDia()));                	
//                }
                modalidadeDt.setObservacao(request.getParameter("ObsModalidade_" + i));
                modalidadeDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
                modalidadeDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
          
                processoExecucaoDt.getListaModalidade().set(i, modalidadeDt);
            }
        }
        
        // dados da origem do processo
        processoExecucaoDt.setVaraOrigem(request.getParameter("VaraOrigem"));
        processoExecucaoDt.setId_CidadeOrigem(request.getParameter("Id_CidadeOrigem"));
        processoExecucaoDt.setCidadeOrigem(request.getParameter("CidadeOrigem"));
        processoExecucaoDt.setEstadoOrigem(request.getParameter("EstadoOrigem"));
        processoExecucaoDt.setUfOrigem(request.getParameter("EstadoOrigem"));
    }
	 
	protected void setDadosSituacaoAtualExecucao(HttpServletRequest request, UsuarioNe usuarioSessao, SituacaoAtualExecucaoDt situacaoAtualExecucaoDt, String idProcesso) throws Exception{
		situacaoAtualExecucaoDt.setIdEventoExecucaoStatus(request.getParameter("SA_Id_EventoExecucaoStatus"));
		situacaoAtualExecucaoDt.setEventoExecucaoStatus(request.getParameter("SA_EventoExecucaoStatus"));
		situacaoAtualExecucaoDt.setIdLocalCumprimentoPena(request.getParameter("SA_Id_LocalCumprimentoPena"));
    	situacaoAtualExecucaoDt.setLocalCumprimentoPena(request.getParameter("SA_LocalCumprimentoPena"));
    	situacaoAtualExecucaoDt.setIdRegime(request.getParameter("SA_Id_RegimeExecucao"));
    	situacaoAtualExecucaoDt.setRegime(request.getParameter("SA_RegimeExecucao"));
    	situacaoAtualExecucaoDt.setIdFormaCumprimento(request.getParameter("SA_Id_FormaCumprimento"));
    	situacaoAtualExecucaoDt.setFormaCumprimento(request.getParameter("SA_FormaCumprimento"));
		situacaoAtualExecucaoDt.setIdPenaExecucaoTipo(request.getParameter("SA_Id_PenaExecucaoTipo"));
		situacaoAtualExecucaoDt.setPenaExecucaoTipo(request.getParameter("SA_PenaExecucaoTipo"));
    	situacaoAtualExecucaoDt.setIdModalidade(request.getParameter("SA_Id_Modalidade"));
    	situacaoAtualExecucaoDt.setModalidade(request.getParameter("SA_Modalidade"));
    	situacaoAtualExecucaoDt.setIdProcesso(idProcesso);
    	situacaoAtualExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
    	situacaoAtualExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
		situacaoAtualExecucaoDt.setDataAlteracao(request.getParameter("SA_DataAlteracao"));
		situacaoAtualExecucaoDt.setDataFuga(request.getParameter("SA_DataFuga"));
		if (!situacaoAtualExecucaoDt.getIdEventoExecucaoStatus().equalsIgnoreCase(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))){
			situacaoAtualExecucaoDt.setDataFuga("");	
		}
	}
	        
	
	protected List getListaPrisaoLC(ProcessoEventoExecucaoNe peeNe, String idProcesso) throws Exception{
		List listaPrisao = new ArrayList();
		List listaEventos = peeNe.listarEventos(idProcesso, "");
		for (int i=0; i<listaEventos.size(); i++) {
			if (((ProcessoEventoExecucaoDt)listaEventos.get(i)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))){
				for (int j=i; j<listaEventos.size(); j++){
					if (((ProcessoEventoExecucaoDt)listaEventos.get(j)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO))
							||((ProcessoEventoExecucaoDt)listaEventos.get(j)).getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO_FLAGRANTE))){
						listaPrisao.add(listaEventos.get(j));
					}
				} 
				break;
			}
		}
		return listaPrisao;
	}

	protected void setListaRegimeRequest(ProcessoEventoExecucaoNe negocio, HttpServletRequest request) throws Exception{
//		 List listaRegimePPL = null;
//	    	List listaRegimeMS = null;
//	    	if (request.getSession().getAttribute("ListaRegime_PPL") == null){
//	    		listaRegimePPL = negocio.consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE));
//	    		request.getSession().setAttribute("ListaRegime_PPL", listaRegimePPL);
//	    	} else listaRegimePPL = (List)request.getSession().getAttribute("ListaRegime_PPL");
//	    	
//	    	if (request.getSession().getAttribute("ListaRegime_MS") == null){
//	    		listaRegimeMS = negocio.consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA));
//	    		request.getSession().setAttribute("ListaRegime_MS", listaRegimeMS);	
//	    	} else listaRegimeMS = (List)request.getSession().getAttribute("ListaRegime_MS");
//	        
//	    	if (request.getSession().getAttribute("SA_ListaRegime_PPL") == null){
//	    		List SA_listaRegimePPL = new ArrayList();
//	            SA_listaRegimePPL.addAll(listaRegimePPL);
//	            SA_listaRegimePPL.addAll(listaRegimeMS);
//	            request.getSession().setAttribute("SA_ListaRegime_PPL", SA_listaRegimePPL);	
//	    	}
//	        
//	    	if (request.getSession().getAttribute("ListaPenaExecucaoTipo") == null) request.getSession().setAttribute("ListaPenaExecucaoTipo", negocio.consultarIdsPenaExecucaoTipo(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE + "," + PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA));
//	    	if (request.getSession().getAttribute("SA_ListaPenaExecucaoTipo") == null) request.getSession().setAttribute("SA_ListaPenaExecucaoTipo", negocio.consultarIdsPenaExecucaoTipo(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE + "," + PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA + "," + PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO));
//	    	if (request.getSession().getAttribute("ListaModalidade") == null) request.getSession().setAttribute("ListaModalidade", negocio.consultarDescricaoRegimeExecucao(String.valueOf(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO)));
//	        if (request.getSession().getAttribute("ListaLocal") == null) request.getSession().setAttribute("ListaLocal", negocio.consultarDescricaoLocalCumprimentoPena());
//	        if (request.getSession().getAttribute("ListaFormaCumprimento") == null) request.getSession().setAttribute("ListaFormaCumprimento", negocio.consultarDescricaoFormaCumprimentoExecucao());
//	        if (request.getSession().getAttribute("ListaStatus") == null) request.getSession().setAttribute("ListaStatus", negocio.consultarDescricaoEventoExecucaoStatus(true));
		 negocio.setListaRegimeRequest(request);
    }
	 
    
	protected void personalizaEvento(ProcessoEventoExecucaoDt processoEventoExecucaoDt, HttpServletRequest request, ProcessoEventoExecucaoNe processoEventoExecucaoNe) throws Exception{
    	if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_MEDIDA_SEGURANCA))){
    		if (request.getSession().getAttribute("ListaRegime_MS") == null)
    			setListaRegimeRequest(processoEventoExecucaoNe, request);
			request.getSession().setAttribute("ListaRegime_PPL", request.getSession().getAttribute("ListaRegime_MS"));
			request.getSession().removeAttribute("ListaRegime_MS");
		} else {
			setListaRegimeRequest(processoEventoExecucaoNe, request);
		}
    }
    
	protected String verificarAtualizacaoSituacaoAtual(String dataAlteracao) throws Exception{
    	if (!dataAlteracao.equalsIgnoreCase(Funcoes.dateToStringSoData(new Date()))){
			return "Atualize os dados da Situação atual de cumprimento da pena!";
		}
    	return "";
    }
    
}


