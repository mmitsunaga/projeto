package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ConflitoDeAbasException;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet para contrar as pré-analises dos autos conclusos.
 * Suporte para pré-análise múltiplas.
 * 
 * @author msapaula
 */
public class PreAnalisarConclusaoCt extends Controle {

    private static final long serialVersionUID = 6588241972732213078L;

    public int Permissao() {
		return PreAnaliseConclusaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AnaliseConclusaoDt analiseConclusaoDt;
		ProcessoEncaminhamentoDt processoEncaminhamentoDt;
		MovimentacaoNe Movimentacaone;

		String Mensagem = "";
		int paginaAnterior = 0;
		int tempFluxo1 = -1;
		int fluxoEditar = -1;
		String pendencias[] = null;
		String id_Arquivo = null;
		
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao = "/WEB-INF/jsptjgo/PreAnalisarConclusao.jsp";

		request.setAttribute("tempPrograma", "Autos Conclusos");
		request.setAttribute("tempRetorno", "PreAnalisarConclusao");

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		if(request.getSession().getAttribute("AnalisePendenciadt") instanceof AnaliseConclusaoDt){
			analiseConclusaoDt = (AnaliseConclusaoDt) request.getSession().getAttribute("AnalisePendenciadt");
		}else{
			analiseConclusaoDt = new AnaliseConclusaoDt();			
		}
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		analiseConclusaoDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		analiseConclusaoDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		analiseConclusaoDt.setComplementoMovimentacao(request.getParameter("MovimentacaoComplemento"));
		analiseConclusaoDt.setClassificador(request.getParameter("Classificador"));
		analiseConclusaoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		analiseConclusaoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		analiseConclusaoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		analiseConclusaoDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		analiseConclusaoDt.setId_Modelo(request.getParameter("Id_Modelo"));
		analiseConclusaoDt.setModelo(request.getParameter("Modelo"));
		analiseConclusaoDt.setTextoEditor(request.getParameter("TextoEditor"));
		
		//----------------------------------------------------------------------------------------------------------------------
		processoEncaminhamentoDt = (ProcessoEncaminhamentoDt) request.getSession().getAttribute("processoEncaminhamentoDt");
		if (processoEncaminhamentoDt == null) processoEncaminhamentoDt = new ProcessoEncaminhamentoDt();
		
		
		if(request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals(""))  
			processoEncaminhamentoDt.setIdServentia(request.getParameter("Id_Serventia"));
		
		if(request.getParameter("Serventia") != null && !request.getParameter("Serventia").equals("")) 
			processoEncaminhamentoDt.setServentia(request.getParameter("Serventia"));
		
		if(request.getParameter("Id_AreaDistribuicao") != null && !request.getParameter("Id_AreaDistribuicao").equals("")){ 
			processoEncaminhamentoDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			processoEncaminhamentoDt.setIdServentia("");
			processoEncaminhamentoDt.setServentia("");
		}
		
		if(request.getParameter("AreaDistribuicao") != null && !request.getParameter("AreaDistribuicao").equals("")) 
			processoEncaminhamentoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		//------------------------------------------------------------------------------------------------------------------------
		
		analiseConclusaoDt.setId_TipoPendencia(request.getParameter("tipo"));
		
		if (request.getParameter("unidadeTrabalho") != null && request.getParameter("unidadeTrabalho").equals("todas")) analiseConclusaoDt.setUnidadeTrabalho("null");
		else analiseConclusaoDt.setUnidadeTrabalho(request.getParameter("unidadeTrabalho"));
		
		if (request.getParameter("julgadoMerito") == null && request.getParameter("fluxo") != null && request.getParameter("fluxo").toString().equals("1"))
			analiseConclusaoDt.setJulgadoMeritoProcessoPrincipal("false");
		else if (request.getParameter("julgadoMerito") != null)
			analiseConclusaoDt.setJulgadoMeritoProcessoPrincipal(request.getParameter("julgadoMerito"));
		
//		if (request.getParameter("exibePendenciaAssinatura") != null && !request.getParameter("exibePendenciaAssinatura").toString().equals("")) {
//			request.getSession().setAttribute("exibePendenciaAssinatura", request.getParameter("exibePendenciaAssinatura"));
//		} else if (request.getSession().getAttribute("exibePendenciaAssinatura") != null && !request.getSession().getAttribute("exibePendenciaAssinatura").toString().equals("")) {
//			//request.setAttribute("exibePendenciaAssinatura", request.getSession().getAttribute("exibePendenciaAssinatura"));
//		}
		
		analiseConclusaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		analiseConclusaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").toString().equalsIgnoreCase("null")) 
			tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo1"));		
		if (request.getParameter("tempFluxo2") != null && !request.getParameter("tempFluxo2").toString().equalsIgnoreCase("null")) 
			tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo2"));
		if (request.getParameter("FluxoEditar") != null) fluxoEditar = Funcoes.StringToInt(request.getParameter("FluxoEditar"));
			
		//Setando váriavel para verifcar se o pedido de assistência foi deferido ou não
		if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("1")) {
			analiseConclusaoDt.setPedidoAssistencia("1");
		} else if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("0")) {
			analiseConclusaoDt.setPedidoAssistencia("0");
		} else if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("2")) {
			analiseConclusaoDt.setPedidoAssistencia("2");
		} 

		setParametrosAuxiliares(request, analiseConclusaoDt, paginaAnterior, paginaatual, tempFluxo1, Movimentacaone, UsuarioSessao);

		if (analiseConclusaoDt.getListaPendenciasFechar() != null && analiseConclusaoDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Pré-Analisar Múltiplos Autos Conclusos");
		else request.setAttribute("TituloPagina", "Pré-Analisar Autos Conclusos");

		int fluxo = analiseConclusaoDt.getFluxo();
		String tipoConclusao = analiseConclusaoDt.getId_PendenciaTipo();
		
		if (UsuarioSessao.getUsuarioDt().isGrupoTipoCodigoDeAutoridade()){
			request.setAttribute("SegundoGrau", "true");
		}
		
		//se esta chegando o __pedido__ indica que esta no meio de uma alteração
		//caso contrario uma nova alteração será iniciada
		if (request.getParameter("__Pedido__")!= null && !request.getParameter("__Pedido__").equals("")){
			request.setAttribute("__Pedido__", request.getParameter("__Pedido__"));	
		}else{
			 //sempre que acessar pela primeira vez, ja vou criar o __pedido__
			//pois essa tela ja é de confirmação de dados
			request.setAttribute("__Pedido__", UsuarioSessao.getPedido());			
		}
		
//		//Setando o pedido na sessão para garantir o funcionamento dos botões Salvar e Guardar para Assinar
//		if(request.getSession().getAttribute("__Pedido__") != null && !request.getSession().getAttribute("__Pedido__").equals("")){
//			request.getSession().removeAttribute("__Pedido__");
//			request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
//		}
		
		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			case Configuracao.Novo:
				// Captura as pendências que serão pré-analisadas
				if (request.getParameterValues("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				// Limpa lista DWR e zera contador Pendências
				request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
				request.getSession().removeAttribute("ListaPendencias");

				//Quando trata de despacho múltiplo
				if (pendencias != null) {
					if (pendencias.length > 1) {
						
						if(UsuarioSessao.isGabinetePresidenciaTjgo() || UsuarioSessao.isGabineteVicePresidenciaTjgo()) {
							throw new MensagemException("Acesso bloqueado. Não é possível realizar a tarefa por enquanto, pois a pré-análise múltipla está sendo desenvolvida para as serventias UPJ, Gabinete da Vice-Presidência e Gabinete da Presidência.");
						}
						
						analiseConclusaoDt = new AnaliseConclusaoDt();
						montarTelaPreAnaliseMultipla(request, analiseConclusaoDt, pendencias, fluxo, tipoConclusao, Movimentacaone, UsuarioSessao);

					} else {
						//Verifica se existe uma pré-analise para a conclusão selecionada
						analiseConclusaoDt = Movimentacaone.getPreAnaliseConclusao(pendencias[0]);

						if (analiseConclusaoDt == null) analiseConclusaoDt = new AnaliseConclusaoDt();
						else setarPreAnalise(request, analiseConclusaoDt);

						montarTelaPreAnaliseSimples(request, analiseConclusaoDt, pendencias[0], fluxo, tipoConclusao, Movimentacaone, UsuarioSessao);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Conclusão foi selecionada.");
					setFluxoRedirecionamento(analiseConclusaoDt, fluxo, tipoConclusao);
					
					if (fluxo == 0) throw new ConflitoDeAbasException();
					
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de pendência configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				analiseConclusaoDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
				// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				analiseConclusaoDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				
				if (request.getParameter("Visualizar") != null && request.getParameter("Visualizar").equalsIgnoreCase("true")) {
					stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseConclusaoAcao.jsp";
					verificarPermissaoConclusao(request, analiseConclusaoDt, Movimentacaone, UsuarioSessao);
				}				
				
				break;
				
			case Configuracao.Salvar:
				if (fluxoEditar == 2) {
					analiseConclusaoDt = montarPreAnalise(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
					analiseConclusaoDt.setFluxo(fluxo);
					analiseConclusaoDt.setId_TipoPendencia(tipoConclusao);
					
					if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) {
						request.setAttribute("MensagemErro", "Impossível realizar esta ação. Guardar para assinar é somente para pré-análises simples.");						
						this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;
					} else {
						request.setAttribute("tempFluxo1", fluxoEditar);
						request.setAttribute("Mensagem", "Deseja guardar para assinar a pré-análise?");
						stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseConclusao.jsp";
					}				
				} else {
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				break;

			case Configuracao.SalvarResultado:
				analiseConclusaoDt.setListaPendenciasGerar(getListaPendencias(request));
				Mensagem = Movimentacaone.verificarPreAnaliseConclusao(analiseConclusaoDt, request.getParameter("IdProcessoValidacaoAba"), UsuarioSessao);
				if (Mensagem.length() == 0) {
					analiseConclusaoDt.setPendenteAssinatura((tempFluxo1 == 2));
					//Alteração para permitir pre-analisar e distribuir uma pre-analise multipla *************************************
					String idArquivoPreAnalise = Movimentacaone.salvarPreAnaliseConclusao(analiseConclusaoDt, UsuarioSessao.getUsuarioDt());
					boolean preAnaliseMultipla = analiseConclusaoDt.isMultipla();
					//*****************************************************************************************************************
					
					//Quando trata de despacho múltiplo
					if (analiseConclusaoDt.isMultipla()){
						request.setAttribute("MensagemOk", "Pré-Análise Múltipla registrada com sucesso.");
					
					}else{
						request.setAttribute("MensagemOk", "Pré-Análise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));
						if (tempFluxo1 == 2){
							request.setAttribute("MensagemOk", "Pré-Análise guardada para assinar com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));	
						}
						
					}
					
					
					// Limpa da sessão os atributos
					analiseConclusaoDt.limpar();
					//request.getSession().removeAttribute("__Pedido__");
					request.getSession().removeAttribute("exibePendenciaAssinatura");

					// Limpa lista DWR e zera contador Pendências
					request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
					request.getSession().removeAttribute("ListaPendencias");
					
					if(Funcoes.StringToBoolean(request.getParameter("SalvarRedistribuir")) && preAnaliseMultipla ) {
						redireciona(response, "DistribuicaoPendencia?PaginaAtual="+Configuracao.Novo+"&Id_Arquivo="+idArquivoPreAnalise+ "&MensagemOk=Pré-Análise Múltipla registrada com sucesso.");						
					} else if(Funcoes.StringToBoolean(request.getParameter("SalvarRedistribuir")) ) {
						redireciona(response, "PendenciaResponsavel?PaginaAtual="+Configuracao.Novo+"&pendencia="+request.getParameter("pendencia")+"&CodigoPendencia="+request.getParameter("CodigoPendencia")+ "&MensagemOk=Pré-Análise registrada com sucesso.");
						//redireciona(response, "PendenciaResponsavel");
					
					}else {
						this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					}
					
					return;

				} else {
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
					request.setAttribute("MensagemErro", Mensagem);
				}
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AUTOS CONCLUSOS PARA PRÉ-ANÁLISE
			// Assistente verá os autos conclusos para o ServentiaCargo que seu usuário chefe ocupa e que ainda não tenham uma pré-analise registrada
			case Configuracao.Localizar:
				consultarConclusao(request, analiseConclusaoDt, Movimentacaone, tempFluxo1, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/ConclusaoLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AS PRÉ-ANALISES SIMPLES EFETUADAS PELO USUÁRIO
			// Pré-analises não assinados são exibidas e podem ser alteradas ou descartadas
			case Configuracao.Curinga6:
				consultarPreAnalisesSimples(request, analiseConclusaoDt, analiseConclusaoDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AS PRÉ-ANÁLISES MULTIPLAS EFETUADAS PELO USUÁRIO
			// Pré-análises não assinadas são exibidas. Assistente vê apenas as suas e juiz vê as feitas pelo ServentiaCargo correspondente
			case Configuracao.Curinga7:
				consultarPreAnalisesConclusoesMultiplas(request, analiseConclusaoDt, Movimentacaone, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseMultiplaLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// ALTERAÇÃO DE PRÉ-ANÁLISE MÚLITPLA
			case Configuracao.Curinga8:

				//Se um arquivo de pré-análise foi passado, verificar quais pendências ele está vinculado
				id_Arquivo = request.getParameter("Id_Arquivo");

				if (id_Arquivo != null) {
					//Busca pendências vinculadas ao arquivo
					List pendenciasFechar = Movimentacaone.consultarPendencias(id_Arquivo);
					if (pendenciasFechar!=null && pendenciasFechar.size()>0){
						//Busca arquivo de pré-análise para primeira pendência vinculada
						PendenciaDt primeiraPendencia = (PendenciaDt) pendenciasFechar.get(0);
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnaliseConclusao(primeiraPendencia.getId());
	
						//Se encontrou uma pré-analise para a pendência, compara se é o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pré-analise
							analiseConclusaoDt = Movimentacaone.getPreAnaliseConclusao(pendenciaArquivoDt);
							//Monta tela para análise das pré-analises multiplas
							montarTelaPreAnaliseMultipla(request, analiseConclusaoDt, pendenciasFechar, fluxo, tipoConclusao, UsuarioSessao.getUsuarioDt(), Movimentacaone);
							
							if (primeiraPendencia != null && primeiraPendencia.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
								analiseConclusaoDt.setVisualizarPedidoAssistencia(true);
							}
							
						}
					}
				}
				// Seta os tipos de pendência configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				analiseConclusaoDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
				// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				analiseConclusaoDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				//Setando o pedido na sessão para garantir o funcionamento dos botões Salvar e Guardar para Assinar
				//request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AS PRÉ-ANÁLISES FEITAS PELO USUÁRIO E QUE JÁ FORAM ASSINADAS PELO JUIZ
			case Configuracao.Curinga9:
				//Se fluxo anterior é esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga9){
					consultarPreAnalisesFinalizadas(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
				}else {
					analiseConclusaoDt = new AnaliseConclusaoDt();
					analiseConclusaoDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseAssinadaLocalizar.jsp";

				break;

			//Prepara dados para exclusão
			case Configuracao.Excluir:
				analiseConclusaoDt = montarPreAnalise(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
				analiseConclusaoDt.setFluxo(fluxo);
				analiseConclusaoDt.setId_TipoPendencia(tipoConclusao);
				if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) request.setAttribute("Mensagem", "Deseja retirar o processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getPendenciaDt().getProcessoNumero()) + " da pré-análise múltipla selecionada?");
				else request.setAttribute("Mensagem", "Deseja descartar a pré-análise?");
				
				//Setando o pedido na sessão para garantir o funcionamento dos botões Salvar e Guardar para Assinar
//				request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
				
				stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseConclusao.jsp";
				break;

			//FUNÇÃO UTILIZADA PARA DESCARTAR PRÉ-ANÁLISE
			case Configuracao.ExcluirResultado:
				LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());

				Movimentacaone.descartarPreAnalise(analiseConclusaoDt.getListaPendenciasFechar(), UsuarioSessao.getUsuarioDt(), logDt);
				request.setAttribute("MensagemOk", "Pré-Análise descartada com sucesso.");

				// Limpa da sessão os atributos
				analiseConclusaoDt.limpar();
				// Limpa lista DWR e zera contador Pendências
				request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
				request.getSession().removeAttribute("ListaPendencias");

				if (fluxo == 0) throw new ConflitoDeAbasException();
				
				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

			// Consultar Tipos de Movimentação
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"MovimentacaoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao", "MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma", "MovimentacaoTipo");
					request.setAttribute("tempRetorno", "PreAnalisarConclusao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Movimentacaone.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador","Prioridade","Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Classificador");
					request.setAttribute("tempBuscaDescricao","Classificador");
					request.setAttribute("tempBuscaPrograma","Classificador");			
					request.setAttribute("tempRetorno","PreAnalisarConclusao");		
					request.setAttribute("tempDescricaoId","Id");
					//Esse valor 1 parao parâmetro consultaClassificador é para indicar que está sendo feita uma pré-análise e o
					//usuário está na tela de localizar as pré-análises a serem realizadas
					if(request.getParameter("consultaClassificador") != null) {
						if(request.getParameter("consultaClassificador").equals("1")) {
							request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Curinga6));
						} else {
							request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Localizar));
						}
					} else {
						request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					}
					request.setAttribute("PaginaAtual", (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);				
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","PreAnalisarConclusao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			// Consultar Modelos do Usuário
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "PreAnalisarConclusao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Modelo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				}else{
					String stTemp = "";
					stTemp = Movimentacaone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), analiseConclusaoDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}

			
			//Consultar Áreas de Distribuição
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					//quando for necessário retornar outros valos além do id, coloque outras colunas de descrição
					// na localizar.jsp as descrições geram novos input hidem para retornar ao ct
					// na funcoes.js as descricoes serão usadas para gerar os AlterarValue para retornar para o ct
					//String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
					//request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
					request.setAttribute("tempRetorno","PreAnalisarConclusao?PassoEditar=1");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
//					idAreaDistribuicao = null;
//					areaDistribuicao = "";
				} else {
					String stTemp="";
					stTemp = new ProcessoNe().consultarDescricaoAreasDistribuicaoAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);					
					
					return;								
				}
			break;

			//Consultar Serventias
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Serventia");			
					request.setAttribute("tempRetorno","PreAnalisarConclusao?PassoEditar=1");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
//					idServentia = null;
//					serventia = "";
				} else {
					String stTemp="";
					stTemp = new ProcessoNe().consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, processoEncaminhamentoDt.getIdAreaDistribuicao(), PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
			break;

			default:
				//Controles da variável tempFluxo1
				// 0 : Redireciona para passo 1 - Dados Pré-Análise
				// 3 : Salva parcial o Texto Digitado 
				switch (tempFluxo1) {

					case 0:
						analiseConclusaoDt.setPasso1("Passo 1");
						analiseConclusaoDt.setPasso2("");
						analiseConclusaoDt.setPasso3("");
						break;

					case 3:
						request.setAttribute("tempFluxo1", -1);
						Mensagem = Movimentacaone.verificarPreAnaliseConclusaoSalvarTextoParcial(analiseConclusaoDt);
						if (Mensagem.length() == 0) {
							analiseConclusaoDt.setPendenteAssinatura(false);
							PendenciaDt pendenciaSalvaDt = Movimentacaone.salvarTextoParcialPreAnaliseConclusao(analiseConclusaoDt, UsuarioSessao.getUsuarioDt());
							
							if (pendenciaSalvaDt == null) {
								//Quando trata de despacho múltiplo
								if (analiseConclusaoDt.isMultipla()){
									request.setAttribute("MensagemOk", "Pré-Análise Múltipla registrada com sucesso.");
								}else{
									request.setAttribute("MensagemOk", "Pré-Análise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));
									if (tempFluxo1 == 2){
										request.setAttribute("MensagemOk", "Pré-Análise guardada para assinar com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));	
									}
								}
							}
						
							// Limpa da sessão os atributos
							analiseConclusaoDt.limpar();
							request.getSession().removeAttribute("exibePendenciaAssinatura");

							// Limpa lista DWR e zera contador Pendências
							request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
							request.getSession().removeAttribute("ListaPendencias");

							if (fluxo == 0) throw new ConflitoDeAbasException();	
							
							if (pendenciaSalvaDt != null) {
								redireciona(response, "PreAnalisarConclusao?Id_Pendencia=" + pendenciaSalvaDt.getId() + "&PaginaAtual=" + Configuracao.Novo + "&preAnaliseConclusao='false'&FluxoEditar=''&MensagemOk=Texto Parcial da Pré-Análise salvo com sucesso.");	
							} else {
								this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
							}
														
							return;

						} else request.setAttribute("MensagemErro", Mensagem);						
						break;

					default:
						//Refere-se a visualização de uma pré-analise já finalizada
						if (fluxo == Configuracao.Curinga9) {
							analiseConclusaoDt = montarHistoricoPreAnaliseFinalizada(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
							analiseConclusaoDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseFinalizada.jsp";
						}
				}
				break;
		}
		
		request.setAttribute("TextoEditor", analiseConclusaoDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", analiseConclusaoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", analiseConclusaoDt.getArquivoTipo());
		request.setAttribute("Modelo", analiseConclusaoDt.getModelo());

		request.getSession().setAttribute("AnalisePendenciadt", analiseConclusaoDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		//Caso o tipo de Movimentação escolhido não esteja configurado teremos que adicioná-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analiseConclusaoDt);

		//Libera o botão Guardar para Assinar segundo o perfil do usuário
		//request.setAttribute("exibePendenciaAssinatura",  UsuarioSessao.isPodeExibirPendenciaAssinatura(analiseConclusaoDt.isMultipla(), Funcoes.StringToInt(analiseConclusaoDt.getTipoPendenciaCodigo())));
		
//		request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
//		request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Seta parâmetros auxiliares para a pré-analise de conclusões
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, int paginaAnterior, int paginaAtual, int tempFluxo1, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));

		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!analisePendenciaDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(),  analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
			analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
			analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("tempFluxo1", tempFluxo1);
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());

		//Tratamento mensagem de Confirmação de Análise partindo de uma pré-analise simples
		//Essa mensagem pode vir redirecionada da servlet AnalisarConclusaoCt e virá como parameter
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");

		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
	}

	/**
	 * Prepara dados para visualizar ou descartar uma pré-analise
	 * @throws Exception 
	 */
	protected AnaliseConclusaoDt montarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{
		String id_Pendencia = request.getParameter("Id_Pendencia");
		String id_Arquivo = request.getParameter("Id_Arquivo");
		PendenciaDt pendencia = null;
		List pendenciasFechar = null;

		//Quando é pré-analise multilpa, busca pendências vinculadas ao arquivo
		if (id_Arquivo != null) {
			pendenciasFechar = movimentacaoNe.consultarPendencias(id_Arquivo);
			if (pendenciasFechar!=null && pendenciasFechar.size()>0)
				id_Pendencia = ((PendenciaDt) pendenciasFechar.get(0)).getId();

		} else if (id_Pendencia != null) {
			pendencia = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		}

		//Busca arquivo de pré-análise
		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
		pendenciaArquivoDt = movimentacaoNe.getArquivoPreAnaliseConclusao(id_Pendencia);

		//Se encontrou uma pré-analise para a pendência
		if (pendenciaArquivoDt != null) {
			String id_Classificador = analisePendenciaDt.getId_Classificador();
			String classificador = analisePendenciaDt.getClassificador();
			
			//Recupera dados da pré-analise
			analisePendenciaDt = movimentacaoNe.getPreAnaliseConclusao(pendenciaArquivoDt);
			analisePendenciaDt.setPendenciaDt(pendencia);
			
			analisePendenciaDt.setId_Classificador(id_Classificador);
			analisePendenciaDt.setClassificador(classificador);

			//Se há uma pendência específica, trata-se de visualização da pré-analise
			if (analisePendenciaDt.getPendenciaDt() != null) analisePendenciaDt.addPendenciasFechar(pendencia);
			else {
				//Trata de pré-análise multipla
				analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar);
				analisePendenciaDt.setMultipla(true);
			}
			setarPreAnalise(request, analisePendenciaDt);
		}
		return analisePendenciaDt;
	}

	/**
	 * Monta a tela de pré-análise simples
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, int fluxo, String tipoConclusao, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pendência de conclusão
		PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);
		analisePendenciaDt.setId_Classificador(pendenciaDt.getId_Classificador());
		analisePendenciaDt.setClassificador(pendenciaDt.getClassificador());
		pendenciaDt.setHash(usuarioSessao.getCodigoHash(id_Pendencia));
		
		if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
			analisePendenciaDt.setVisualizarPedidoAssistencia(true);
		}

		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaone.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);
		//Verifica se existe peticionamento com data posterior à conclusão
		processoDt.setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaone, pendenciaDt));
		//Verifica se existe Guias Pendentes para o processo
		pendenciaDt.getProcessoDt().setExisteGuiasPendentes(existeGuiasPendentes(movimentacaone, pendenciaDt));

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoConclusao);
		//na verdade tem que setar o id...
		analisePendenciaDt.setId_TipoPendencia(pendenciaDt.getId_PendenciaTipo());
		
		verificarPermissaoConclusao(request, analisePendenciaDt, movimentacaone, usuarioSessao);
	}

	/**
	 * Monta a tela inserção de pré-análise múltipla.
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String[] pendencias, int fluxo, String tipoConclusao, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setMultipla(true);
		
		//Pesquisa pendências
		for (int i = 0; i < pendencias.length; i++) {
			String id_Pendencia = (String) pendencias[i];
			PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
			
			if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
				analisePendenciaDt.setVisualizarPedidoAssistencia(true);
			}
			
			//Resgata dados básicos de cada processo
			pendenciaDt.setProcessoDt(movimentacaone.consultarProcessoId(pendenciaDt.getId_Processo()));
			//Verifica se existe peticionamento com data posterior à conclusão
			pendenciaDt.getProcessoDt().setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaone, pendenciaDt));
			//Verifica se existe Guias Pendentes para o processo
			pendenciaDt.getProcessoDt().setExisteGuiasPendentes(existeGuiasPendentes(movimentacaone, pendenciaDt));
			analisePendenciaDt.addPendenciasFechar(pendenciaDt);			
		}

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoConclusao);
	}

	/**
	 * Método que faz tratamentos necessários para setar o fluxo de redirecionamento correto
	 * levando em consideração a servlet para qual retornar e se o filtro de tipo de conclusão está preenchido
	 * @param analisePendenciaDt
	 * @param fluxo
	 * @param tipoConclusao
	 */
	protected void setFluxoRedirecionamento(AnaliseConclusaoDt analisePendenciaDt, int fluxo, String tipoConclusao) {
		if (fluxo != Configuracao.Curinga6 && fluxo != Configuracao.Curinga7 && fluxo != Configuracao.Curinga9) analisePendenciaDt.setFluxo(Configuracao.Localizar);
		else analisePendenciaDt.setFluxo(fluxo);
		analisePendenciaDt.setId_TipoPendencia(tipoConclusao);
	}

	/**
	 * Retorna todos os dados de uma pré-analise finalizada, e monta todo o histórico de correções existentes.
	 * @throws Exception 
	 */
	protected AnaliseConclusaoDt montarHistoricoPreAnaliseFinalizada(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{

		PendenciaDt pendencia = null;
		List listaFilhasConclusao = null;
		List arquivosResposta = null;
		PendenciaArquivoDt arquivoPreAnalise = null;
		String id_Pendencia = request.getParameter("Id_Pendencia");

		if (id_Pendencia != null) {
			if (analisePendenciaDt.getPendenciaDt() == null || !id_Pendencia.equalsIgnoreCase(analisePendenciaDt.getPendenciaDt().getId())) {

				pendencia = movimentacaone.consultarFinalizadaId(id_Pendencia);

				//Consulta as pendências filhas da pré-analise selecionada do tipo Conclusão
				listaFilhasConclusao = movimentacaone.consultarPendenciasFinalizadasFilhas(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pendência, que serão o arquivo da pré-analise e análise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaConclusaoFinalizada(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo não é assinado, é pré-analise
						if (pendenciaArquivoDt.getArquivoDt().getUsuarioAssinador().equals("")) {
							//Sendo o arquivo de resposta a última pré-analise, busca então o responsável
							pendenciaArquivoDt.setResponsavelPreAnalise( movimentacaone.consultaResponsavelPreAnaliseFinalizada(pendenciaArquivoDt.getId()));
							arquivoPreAnalise = pendenciaArquivoDt;
						} else {
							pendencia.addListaArquivos(pendenciaArquivoDt);
							listaFilhasConclusao.add(pendencia);
						}
					}
				}

				if (arquivoPreAnalise != null) {
					//Recupera dados da pré-analise que foi selecionada
					analisePendenciaDt = movimentacaone.getPreAnaliseConclusaoFinalizada(arquivoPreAnalise);
				}
				analisePendenciaDt.setHistoricoPendencia(listaFilhasConclusao);
				analisePendenciaDt.setPendenciaDt(pendencia);
			}
		}

		setarPreAnalise(request, analisePendenciaDt);
		request.setAttribute("linkArquivo", "PendenciaArquivo");
		request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
		request.setAttribute("paginaArquivo", Configuracao.Curinga6);
		return analisePendenciaDt;
	}

	/**
	 * Monta tela para alteração de pré-análises múltiplas
	 * @param pendenciasFechar: Esse método já recebe uma lista de objetos das pendências a serem fechadas.
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, List pendenciasFechar, int fluxo, String tipoConclusao, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe){

		analisePendenciaDt.setMultipla(true);

		//Adiciona pendências a serem fechadas
		analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar);

		//Seta os tipos de pendência que poderão ser gerados
		//analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt.getGrupoCodigo()));

		request.setAttribute("TituloPagina", "Pré-Analisar Múltiplos Autos Conclusos");

		setarPreAnalise(request, analisePendenciaDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoConclusao);
	}

	/**
	 * Método que irá setar no request as informações da pré-análise já efetuada por um assistente
	 * para que o juiz possa finalizar
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt){
		//Recupera lista das pendências definida pelo assistente. É necessário conversão pois Lista DWR espera um Set
		if (analisePendenciaDt.getListaPendenciasGerar() != null) {
			request.getSession().setAttribute("ListaPendencias", Funcoes.converterListParaSet(analisePendenciaDt.getListaPendenciasGerar()));
			request.getSession().setAttribute("Id_ListaDadosMovimentacao", analisePendenciaDt.getListaPendenciasGerar().size());
		}

		//Seta dados da pré-analise em AnaliseConclusaoDt para mostrar na tela
		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			PendenciaArquivoDt preAnalise = analisePendenciaDt.getArquivoPreAnalise();
			analisePendenciaDt.setTextoEditor(preAnalise.getArquivoDt().getArquivo());
			analisePendenciaDt.setId_ArquivoTipo(preAnalise.getArquivoDt().getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(preAnalise.getArquivoDt().getArquivoTipo());
			analisePendenciaDt.setNomeArquivo(preAnalise.getArquivoDt().getNomeArquivo());
			analisePendenciaDt.setDataPreAnalise(preAnalise.getArquivoDt().getDataInsercao());
			
			//Se pré-análise foi feita por assistente
			if (preAnalise.getAssistenteResponsavel() != null && !preAnalise.getAssistenteResponsavel().equals("")) analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getAssistenteResponsavel());
			else analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getJuizResponsavel());
		}
	}

	/**
	 * Consulta de autos conclusos pendentes
	 * Se é um juiz retorna as conclusões para seu ServentiaCargo, se é assistente retorna
	 * as conclusões do ServentiaCargo de seu usuário chefe
	 * @throws Exception 
	 */
	protected void consultarConclusao(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaone, int tempFluxo1, UsuarioNe usuarioSessao) throws Exception{
		if (tempFluxo1 == 0) {//Significa que foi acionado Botão Limpar
			analisePendenciaDt.setNumeroProcesso("null");
			analisePendenciaDt.setId_Classificador("null");
		}

		//Verifica se usuário pode analisar e pré-analisar
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		request.setAttribute("id_Classificador", analisePendenciaDt.getId_Classificador());
		request.setAttribute("classificador", analisePendenciaDt.getClassificador());

		//Verifica se usuário digitou "." e dígito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return;
		}
		
		verificarPermissaoConclusao(request, analisePendenciaDt, movimentacaone, usuarioSessao);
		
		if ( (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU && !(Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO || Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO))
				|| (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE || usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO) && !(Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO) 
				||	Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
			request.setAttribute("podeGerarVotoEmenta", "true");

		boolean EhVoto = false;
		boolean EhVotoVencido = false;
		if (analisePendenciaDt.getId_PendenciaTipo() != null && analisePendenciaDt.getId_PendenciaTipo().length() > 0){
			PendenciaTipoDt pendenciaTipoDt = movimentacaone.consultarPendenciaTipo(analisePendenciaDt.getId_PendenciaTipo());	
			if(pendenciaTipoDt != null && pendenciaTipoDt.getPendenciaTipoCodigo() != null && pendenciaTipoDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))){
				EhVoto = true;
				EhVotoVencido = true;
			}
		}
		
		List tempList = null;
		if ((usuarioSessao.isAssistenteGabinete() || usuarioSessao.isAssistenteGabineteComFluxo()) 
				&& (usuarioSessao.isGabinetePresidenciaTjgo() || usuarioSessao.isGabineteVicePresidenciaTjgo() ||usuarioSessao.isGabineteUpj()) ){
			tempList = movimentacaone.consultarConclusoesPendentesAssistenteGabinete(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), analisePendenciaDt.getId_PendenciaTipo(), analisePendenciaDt.getUnidadeTrabalho(),EhVoto, EhVotoVencido);
		} else
			tempList = movimentacaone.consultarConclusoesPendentes(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), analisePendenciaDt.getId_PendenciaTipo(), EhVoto, EhVotoVencido);
		 
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaConclusao", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Não há Conclusões Pendentes.");
		}

	}

	protected void verificarPermissaoConclusao(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception {
		
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE && usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA && usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.JUIZ_LEIGO) {
			
				request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
				
		} else{
				request.setAttribute("podeAnalisar", "false");
		}
			
		if ((usuarioSessao.isAssistenteGabinete() || usuarioSessao.isAssistenteGabineteComFluxo()) 
				&& (usuarioSessao.isGabinetePresidenciaTjgo() || usuarioSessao.isGabineteVicePresidenciaTjgo()) ) {
				request.setAttribute("podeAnalisarMultipla", "false");
				
		} else {
			request.setAttribute("podeAnalisarMultipla", "true");
		}
		
		//verifica se o usuário pode trocar o responsável de pendência
		if (analisePendenciaDt.getId_PendenciaTipo() != null && analisePendenciaDt.getId_PendenciaTipo().length()>0){
			PendenciaTipoDt pendenciaTipoDt = movimentacaone.consultarPendenciaTipo(analisePendenciaDt.getId_PendenciaTipo());			
		}
		
		if (usuarioSessao.isGabinetePresidenciaTjgo() || usuarioSessao.isGabineteVicePresidenciaTjgo() || usuarioSessao.isGabineteUpj()){
			request.setAttribute("podeDescartarPreAnalise", "false");
		}
	}

	/**
	 * Realiza chamada ao método para consultar as pré-analises simples de um usuário
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String tipoConclusao, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		//Verifica se usuário digitou "." e dígito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return boRetorno;
		}

		request.setAttribute("id_Classificador", analisePendenciaDt.getId_Classificador());
		request.setAttribute("classificador", analisePendenciaDt.getClassificador());
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		
		verificarPermissaoConclusao(request, analisePendenciaDt, movimentacaone, usuarioSessao);
		
		boolean EhVoto = false;
		boolean EhVotoVencido = false;
		if (analisePendenciaDt.getId_PendenciaTipo() != null && analisePendenciaDt.getId_PendenciaTipo().length() > 0){
			PendenciaTipoDt pendenciaTipoDt = movimentacaone.consultarPendenciaTipo(analisePendenciaDt.getId_PendenciaTipo());	
			if(pendenciaTipoDt != null && pendenciaTipoDt.getPendenciaTipoCodigo() != null && pendenciaTipoDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))){
				EhVoto = true;
				EhVotoVencido = true;
			}
		}
		
		if ( (usuarioSessao.isGabinetePresidenciaTjgo() || usuarioSessao.isGabineteVicePresidenciaTjgo() || usuarioSessao.isGabineteUpj()) 
				&& (usuarioSessao.isAssistenteGabinete() || usuarioSessao.isAssistenteGabineteComFluxo()) ){
			request.setAttribute("podeDescartarPreAnalise", "false");
		}
		
		List tempList = null;
		
		if ((usuarioSessao.isGabinetePresidenciaTjgo() || usuarioSessao.isGabineteVicePresidenciaTjgo() || usuarioSessao.isGabineteUpj()) && (usuarioSessao.isAssistenteGabinete() || usuarioSessao.isAssistenteGabineteComFluxo()) ){
			tempList = movimentacaone.consultarPreAnalisesConclusaoSimplesAssistenteGabinete(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), analisePendenciaDt.getId_PendenciaTipo(), analisePendenciaDt.getUnidadeTrabalho(),EhVoto, EhVotoVencido);
		} else
			tempList = movimentacaone.consultarPreAnalisesConclusaoSimples(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), tipoConclusao, EhVoto, EhVotoVencido);
		
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaPreAnalises", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			
			if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
			else
				request.setAttribute("podeAnalisar", "false");
			
			if ((usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
					&& !(Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO ||
							Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO))
					|| ((usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE || usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO)
							&& !(Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO ||
									Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO))
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeGerarVotoEmenta", "true");
			
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
			
			//Atualiza o rash para consulta
			for (Object pendenciaArquivoObj : tempList) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)pendenciaArquivoObj;
				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
			
			boRetorno = true;
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pré-Análise Simples foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Consulta as pré-analises multiplas efetuadas por um usuario ou serventia cargo que ainda não foram finalizadas
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesConclusoesMultiplas(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;

		List tempList = movimentacaoNe.consultarPreAnalisesConclusoesMultiplas(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso());
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaPreAnalises", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
			else
				request.setAttribute("podeAnalisar", "false");
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
			boRetorno = true;
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pré-Análise Múltipla foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Captura lista de pendências a serem geradas, e converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);
		return lista;
	}


	/**
	 * Realiza chamada ao método para consultar as pré-analises que já foram finalizadas (assinadas)
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesFinalizadas(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usuário digitou "." e dígito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarPreAnalisesConclusaoFinalizadas(analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal(), usuarioSessao.getUsuarioDt());
			if (tempList.size() > 0) {
				request.setAttribute("ListaPreAnalises", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Pré-Análise Localizada.");

		} else request.setAttribute("MensagemErro", "Informe parâmetros para consulta: Data Inicial e Data Final ou Número do Processo.");
		return boRetorno;
	}
	
	protected boolean existePeticionamentoPendente(UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe, PendenciaDt pendenciaDt) throws Exception{
		if (pendenciaDt == null) return false;
		if (pendenciaDt.getProcessoDt() == null) return false;
		return movimentacaoNe.existePeticionamentoPendente(usuarioSessao, pendenciaDt.getProcessoDt().getProcessoNumero(), pendenciaDt.getDataInicio(), pendenciaDt.getProcessoDt().getId_Serventia());
	}
	
	private boolean existeGuiasPendentes(MovimentacaoNe movimentacaoNe, PendenciaDt pendenciaDt) throws Exception{
		if (pendenciaDt == null) return false;
		if (pendenciaDt.getProcessoDt() == null) return false;
		return movimentacaoNe.verificarGuiasPendentesProcesso(pendenciaDt.getProcessoDt().getId());
	}
	

	/**
	 * Verifica se o tipo de movimentação selecionado é um tipo de movimentação já configurado para o usuário logado
	 * @param analisePendenciaDt
	 */
	protected void verifiqueMovimentacaoTipo(AnaliseConclusaoDt analisePendenciaDt) {
		if ((analisePendenciaDt.getId_MovimentacaoTipo() == null) || (analisePendenciaDt.getId_MovimentacaoTipo().trim().equalsIgnoreCase("")) 
		    || (analisePendenciaDt.getMovimentacaoTipo() == null) || (analisePendenciaDt.getMovimentacaoTipo().trim().equalsIgnoreCase(""))) return;
		boolean encontrouNaLista = false;	
		List listaMovimentacaoTipo = analisePendenciaDt.getListaTiposMovimentacaoConfigurado();
		for (int i=0;i<listaMovimentacaoTipo.size();i++){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
			if (analisePendenciaDt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo())) encontrouNaLista = true;
		}
		if (!encontrouNaLista){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = new UsuarioMovimentacaoTipoDt();
			usuarioMovimentacaoTipoDt.setId_MovimentacaoTipo(analisePendenciaDt.getId_MovimentacaoTipo());
			usuarioMovimentacaoTipoDt.setMovimentacaoTipo(analisePendenciaDt.getMovimentacaoTipo());
			analisePendenciaDt.addListaTiposMovimentacaoConfigurado(usuarioMovimentacaoTipoDt);
		}
	}
	
	protected boolean verificaPodeTrocarResponsavel(PendenciaTipoDt pendenciaTipoDt, UsuarioDt usuarioDt){
		boolean retorno = false;
		
		if (pendenciaTipoDt.getPendenciaTipoCodigo() != null && pendenciaTipoDt.getPendenciaTipoCodigo().length() > 0 
				&& (Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO
					|| Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO
					|| Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA)
				&& (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)){
			retorno = true;
		} else if (Funcoes.StringToInt(usuarioDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ && 
					(Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU)
				){
			retorno = true;
		}
		
		return retorno;
		
	}

}
