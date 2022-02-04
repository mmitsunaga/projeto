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
 * Servlet para contrar as pr�-analises dos autos conclusos.
 * Suporte para pr�-an�lise m�ltiplas.
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
			
		//Setando v�riavel para verifcar se o pedido de assist�ncia foi deferido ou n�o
		if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("1")) {
			analiseConclusaoDt.setPedidoAssistencia("1");
		} else if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("0")) {
			analiseConclusaoDt.setPedidoAssistencia("0");
		} else if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("2")) {
			analiseConclusaoDt.setPedidoAssistencia("2");
		} 

		setParametrosAuxiliares(request, analiseConclusaoDt, paginaAnterior, paginaatual, tempFluxo1, Movimentacaone, UsuarioSessao);

		if (analiseConclusaoDt.getListaPendenciasFechar() != null && analiseConclusaoDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Pr�-Analisar M�ltiplos Autos Conclusos");
		else request.setAttribute("TituloPagina", "Pr�-Analisar Autos Conclusos");

		int fluxo = analiseConclusaoDt.getFluxo();
		String tipoConclusao = analiseConclusaoDt.getId_PendenciaTipo();
		
		if (UsuarioSessao.getUsuarioDt().isGrupoTipoCodigoDeAutoridade()){
			request.setAttribute("SegundoGrau", "true");
		}
		
		//se esta chegando o __pedido__ indica que esta no meio de uma altera��o
		//caso contrario uma nova altera��o ser� iniciada
		if (request.getParameter("__Pedido__")!= null && !request.getParameter("__Pedido__").equals("")){
			request.setAttribute("__Pedido__", request.getParameter("__Pedido__"));	
		}else{
			 //sempre que acessar pela primeira vez, ja vou criar o __pedido__
			//pois essa tela ja � de confirma��o de dados
			request.setAttribute("__Pedido__", UsuarioSessao.getPedido());			
		}
		
//		//Setando o pedido na sess�o para garantir o funcionamento dos bot�es Salvar e Guardar para Assinar
//		if(request.getSession().getAttribute("__Pedido__") != null && !request.getSession().getAttribute("__Pedido__").equals("")){
//			request.getSession().removeAttribute("__Pedido__");
//			request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
//		}
		
		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			case Configuracao.Novo:
				// Captura as pend�ncias que ser�o pr�-analisadas
				if (request.getParameterValues("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				// Limpa lista DWR e zera contador Pend�ncias
				request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
				request.getSession().removeAttribute("ListaPendencias");

				//Quando trata de despacho m�ltiplo
				if (pendencias != null) {
					if (pendencias.length > 1) {
						
						if(UsuarioSessao.isGabinetePresidenciaTjgo() || UsuarioSessao.isGabineteVicePresidenciaTjgo()) {
							throw new MensagemException("Acesso bloqueado. N�o � poss�vel realizar a tarefa por enquanto, pois a pr�-an�lise m�ltipla est� sendo desenvolvida para as serventias UPJ, Gabinete da Vice-Presid�ncia e Gabinete da Presid�ncia.");
						}
						
						analiseConclusaoDt = new AnaliseConclusaoDt();
						montarTelaPreAnaliseMultipla(request, analiseConclusaoDt, pendencias, fluxo, tipoConclusao, Movimentacaone, UsuarioSessao);

					} else {
						//Verifica se existe uma pr�-analise para a conclus�o selecionada
						analiseConclusaoDt = Movimentacaone.getPreAnaliseConclusao(pendencias[0]);

						if (analiseConclusaoDt == null) analiseConclusaoDt = new AnaliseConclusaoDt();
						else setarPreAnalise(request, analiseConclusaoDt);

						montarTelaPreAnaliseSimples(request, analiseConclusaoDt, pendencias[0], fluxo, tipoConclusao, Movimentacaone, UsuarioSessao);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Conclus�o foi selecionada.");
					setFluxoRedirecionamento(analiseConclusaoDt, fluxo, tipoConclusao);
					
					if (fluxo == 0) throw new ConflitoDeAbasException();
					
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de pend�ncia configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
				analiseConclusaoDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
				// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
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
						request.setAttribute("MensagemErro", "Imposs�vel realizar esta a��o. Guardar para assinar � somente para pr�-an�lises simples.");						
						this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;
					} else {
						request.setAttribute("tempFluxo1", fluxoEditar);
						request.setAttribute("Mensagem", "Deseja guardar para assinar a pr�-an�lise?");
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
					//Altera��o para permitir pre-analisar e distribuir uma pre-analise multipla *************************************
					String idArquivoPreAnalise = Movimentacaone.salvarPreAnaliseConclusao(analiseConclusaoDt, UsuarioSessao.getUsuarioDt());
					boolean preAnaliseMultipla = analiseConclusaoDt.isMultipla();
					//*****************************************************************************************************************
					
					//Quando trata de despacho m�ltiplo
					if (analiseConclusaoDt.isMultipla()){
						request.setAttribute("MensagemOk", "Pr�-An�lise M�ltipla registrada com sucesso.");
					
					}else{
						request.setAttribute("MensagemOk", "Pr�-An�lise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));
						if (tempFluxo1 == 2){
							request.setAttribute("MensagemOk", "Pr�-An�lise guardada para assinar com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));	
						}
						
					}
					
					
					// Limpa da sess�o os atributos
					analiseConclusaoDt.limpar();
					//request.getSession().removeAttribute("__Pedido__");
					request.getSession().removeAttribute("exibePendenciaAssinatura");

					// Limpa lista DWR e zera contador Pend�ncias
					request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
					request.getSession().removeAttribute("ListaPendencias");
					
					if(Funcoes.StringToBoolean(request.getParameter("SalvarRedistribuir")) && preAnaliseMultipla ) {
						redireciona(response, "DistribuicaoPendencia?PaginaAtual="+Configuracao.Novo+"&Id_Arquivo="+idArquivoPreAnalise+ "&MensagemOk=Pr�-An�lise M�ltipla registrada com sucesso.");						
					} else if(Funcoes.StringToBoolean(request.getParameter("SalvarRedistribuir")) ) {
						redireciona(response, "PendenciaResponsavel?PaginaAtual="+Configuracao.Novo+"&pendencia="+request.getParameter("pendencia")+"&CodigoPendencia="+request.getParameter("CodigoPendencia")+ "&MensagemOk=Pr�-An�lise registrada com sucesso.");
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

			// FUN��O UTILIZADA PARA LOCALIZAR AUTOS CONCLUSOS PARA PR�-AN�LISE
			// Assistente ver� os autos conclusos para o ServentiaCargo que seu usu�rio chefe ocupa e que ainda n�o tenham uma pr�-analise registrada
			case Configuracao.Localizar:
				consultarConclusao(request, analiseConclusaoDt, Movimentacaone, tempFluxo1, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/ConclusaoLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-ANALISES SIMPLES EFETUADAS PELO USU�RIO
			// Pr�-analises n�o assinados s�o exibidas e podem ser alteradas ou descartadas
			case Configuracao.Curinga6:
				consultarPreAnalisesSimples(request, analiseConclusaoDt, analiseConclusaoDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-AN�LISES MULTIPLAS EFETUADAS PELO USU�RIO
			// Pr�-an�lises n�o assinadas s�o exibidas. Assistente v� apenas as suas e juiz v� as feitas pelo ServentiaCargo correspondente
			case Configuracao.Curinga7:
				consultarPreAnalisesConclusoesMultiplas(request, analiseConclusaoDt, Movimentacaone, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseMultiplaLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// ALTERA��O DE PR�-AN�LISE M�LITPLA
			case Configuracao.Curinga8:

				//Se um arquivo de pr�-an�lise foi passado, verificar quais pend�ncias ele est� vinculado
				id_Arquivo = request.getParameter("Id_Arquivo");

				if (id_Arquivo != null) {
					//Busca pend�ncias vinculadas ao arquivo
					List pendenciasFechar = Movimentacaone.consultarPendencias(id_Arquivo);
					if (pendenciasFechar!=null && pendenciasFechar.size()>0){
						//Busca arquivo de pr�-an�lise para primeira pend�ncia vinculada
						PendenciaDt primeiraPendencia = (PendenciaDt) pendenciasFechar.get(0);
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnaliseConclusao(primeiraPendencia.getId());
	
						//Se encontrou uma pr�-analise para a pend�ncia, compara se � o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pr�-analise
							analiseConclusaoDt = Movimentacaone.getPreAnaliseConclusao(pendenciaArquivoDt);
							//Monta tela para an�lise das pr�-analises multiplas
							montarTelaPreAnaliseMultipla(request, analiseConclusaoDt, pendenciasFechar, fluxo, tipoConclusao, UsuarioSessao.getUsuarioDt(), Movimentacaone);
							
							if (primeiraPendencia != null && primeiraPendencia.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
								analiseConclusaoDt.setVisualizarPedidoAssistencia(true);
							}
							
						}
					}
				}
				// Seta os tipos de pend�ncia configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
				analiseConclusaoDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
				// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
				analiseConclusaoDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				//Setando o pedido na sess�o para garantir o funcionamento dos bot�es Salvar e Guardar para Assinar
				//request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-AN�LISES FEITAS PELO USU�RIO E QUE J� FORAM ASSINADAS PELO JUIZ
			case Configuracao.Curinga9:
				//Se fluxo anterior � esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga9){
					consultarPreAnalisesFinalizadas(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
				}else {
					analiseConclusaoDt = new AnaliseConclusaoDt();
					analiseConclusaoDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseAssinadaLocalizar.jsp";

				break;

			//Prepara dados para exclus�o
			case Configuracao.Excluir:
				analiseConclusaoDt = montarPreAnalise(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
				analiseConclusaoDt.setFluxo(fluxo);
				analiseConclusaoDt.setId_TipoPendencia(tipoConclusao);
				if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) request.setAttribute("Mensagem", "Deseja retirar o processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getPendenciaDt().getProcessoNumero()) + " da pr�-an�lise m�ltipla selecionada?");
				else request.setAttribute("Mensagem", "Deseja descartar a pr�-an�lise?");
				
				//Setando o pedido na sess�o para garantir o funcionamento dos bot�es Salvar e Guardar para Assinar
//				request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
				
				stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseConclusao.jsp";
				break;

			//FUN��O UTILIZADA PARA DESCARTAR PR�-AN�LISE
			case Configuracao.ExcluirResultado:
				LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());

				Movimentacaone.descartarPreAnalise(analiseConclusaoDt.getListaPendenciasFechar(), UsuarioSessao.getUsuarioDt(), logDt);
				request.setAttribute("MensagemOk", "Pr�-An�lise descartada com sucesso.");

				// Limpa da sess�o os atributos
				analiseConclusaoDt.limpar();
				// Limpa lista DWR e zera contador Pend�ncias
				request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
				request.getSession().removeAttribute("ListaPendencias");

				if (fluxo == 0) throw new ConflitoDeAbasException();
				
				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

			// Consultar Tipos de Movimenta��o
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
					//Esse valor 1 parao par�metro consultaClassificador � para indicar que est� sendo feita uma pr�-an�lise e o
					//usu�rio est� na tela de localizar as pr�-an�lises a serem realizadas
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

			// Consultar Modelos do Usu�rio
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

			
			//Consultar �reas de Distribui��o
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					//quando for necess�rio retornar outros valos al�m do id, coloque outras colunas de descri��o
					// na localizar.jsp as descri��es geram novos input hidem para retornar ao ct
					// na funcoes.js as descricoes ser�o usadas para gerar os AlterarValue para retornar para o ct
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
				//Controles da vari�vel tempFluxo1
				// 0 : Redireciona para passo 1 - Dados Pr�-An�lise
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
								//Quando trata de despacho m�ltiplo
								if (analiseConclusaoDt.isMultipla()){
									request.setAttribute("MensagemOk", "Pr�-An�lise M�ltipla registrada com sucesso.");
								}else{
									request.setAttribute("MensagemOk", "Pr�-An�lise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));
									if (tempFluxo1 == 2){
										request.setAttribute("MensagemOk", "Pr�-An�lise guardada para assinar com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));	
									}
								}
							}
						
							// Limpa da sess�o os atributos
							analiseConclusaoDt.limpar();
							request.getSession().removeAttribute("exibePendenciaAssinatura");

							// Limpa lista DWR e zera contador Pend�ncias
							request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
							request.getSession().removeAttribute("ListaPendencias");

							if (fluxo == 0) throw new ConflitoDeAbasException();	
							
							if (pendenciaSalvaDt != null) {
								redireciona(response, "PreAnalisarConclusao?Id_Pendencia=" + pendenciaSalvaDt.getId() + "&PaginaAtual=" + Configuracao.Novo + "&preAnaliseConclusao='false'&FluxoEditar=''&MensagemOk=Texto Parcial da Pr�-An�lise salvo com sucesso.");	
							} else {
								this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
							}
														
							return;

						} else request.setAttribute("MensagemErro", Mensagem);						
						break;

					default:
						//Refere-se a visualiza��o de uma pr�-analise j� finalizada
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
		
		//Caso o tipo de Movimenta��o escolhido n�o esteja configurado teremos que adicion�-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analiseConclusaoDt);

		//Libera o bot�o Guardar para Assinar segundo o perfil do usu�rio
		//request.setAttribute("exibePendenciaAssinatura",  UsuarioSessao.isPodeExibirPendenciaAssinatura(analiseConclusaoDt.isMultipla(), Funcoes.StringToInt(analiseConclusaoDt.getTipoPendenciaCodigo())));
		
//		request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
//		request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Seta par�metros auxiliares para a pr�-analise de conclus�es
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, int paginaAnterior, int paginaAtual, int tempFluxo1, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));

		// Quando modelo foi selecionado monta conte�do para aparecer no editor e j� carrego o tipo do arquivo
		if (!analisePendenciaDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(),  analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
			analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
			analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("tempFluxo1", tempFluxo1);
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());

		//Tratamento mensagem de Confirma��o de An�lise partindo de uma pr�-analise simples
		//Essa mensagem pode vir redirecionada da servlet AnalisarConclusaoCt e vir� como parameter
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");

		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
	}

	/**
	 * Prepara dados para visualizar ou descartar uma pr�-analise
	 * @throws Exception 
	 */
	protected AnaliseConclusaoDt montarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{
		String id_Pendencia = request.getParameter("Id_Pendencia");
		String id_Arquivo = request.getParameter("Id_Arquivo");
		PendenciaDt pendencia = null;
		List pendenciasFechar = null;

		//Quando � pr�-analise multilpa, busca pend�ncias vinculadas ao arquivo
		if (id_Arquivo != null) {
			pendenciasFechar = movimentacaoNe.consultarPendencias(id_Arquivo);
			if (pendenciasFechar!=null && pendenciasFechar.size()>0)
				id_Pendencia = ((PendenciaDt) pendenciasFechar.get(0)).getId();

		} else if (id_Pendencia != null) {
			pendencia = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		}

		//Busca arquivo de pr�-an�lise
		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
		pendenciaArquivoDt = movimentacaoNe.getArquivoPreAnaliseConclusao(id_Pendencia);

		//Se encontrou uma pr�-analise para a pend�ncia
		if (pendenciaArquivoDt != null) {
			String id_Classificador = analisePendenciaDt.getId_Classificador();
			String classificador = analisePendenciaDt.getClassificador();
			
			//Recupera dados da pr�-analise
			analisePendenciaDt = movimentacaoNe.getPreAnaliseConclusao(pendenciaArquivoDt);
			analisePendenciaDt.setPendenciaDt(pendencia);
			
			analisePendenciaDt.setId_Classificador(id_Classificador);
			analisePendenciaDt.setClassificador(classificador);

			//Se h� uma pend�ncia espec�fica, trata-se de visualiza��o da pr�-analise
			if (analisePendenciaDt.getPendenciaDt() != null) analisePendenciaDt.addPendenciasFechar(pendencia);
			else {
				//Trata de pr�-an�lise multipla
				analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar);
				analisePendenciaDt.setMultipla(true);
			}
			setarPreAnalise(request, analisePendenciaDt);
		}
		return analisePendenciaDt;
	}

	/**
	 * Monta a tela de pr�-an�lise simples
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, int fluxo, String tipoConclusao, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pend�ncia de conclus�o
		PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);
		analisePendenciaDt.setId_Classificador(pendenciaDt.getId_Classificador());
		analisePendenciaDt.setClassificador(pendenciaDt.getClassificador());
		pendenciaDt.setHash(usuarioSessao.getCodigoHash(id_Pendencia));
		
		if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
			analisePendenciaDt.setVisualizarPedidoAssistencia(true);
		}

		//Resgata processo ligado a pend�ncia
		ProcessoDt processoDt = movimentacaone.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);
		//Verifica se existe peticionamento com data posterior � conclus�o
		processoDt.setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaone, pendenciaDt));
		//Verifica se existe Guias Pendentes para o processo
		pendenciaDt.getProcessoDt().setExisteGuiasPendentes(existeGuiasPendentes(movimentacaone, pendenciaDt));

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoConclusao);
		//na verdade tem que setar o id...
		analisePendenciaDt.setId_TipoPendencia(pendenciaDt.getId_PendenciaTipo());
		
		verificarPermissaoConclusao(request, analisePendenciaDt, movimentacaone, usuarioSessao);
	}

	/**
	 * Monta a tela inser��o de pr�-an�lise m�ltipla.
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String[] pendencias, int fluxo, String tipoConclusao, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setMultipla(true);
		
		//Pesquisa pend�ncias
		for (int i = 0; i < pendencias.length; i++) {
			String id_Pendencia = (String) pendencias[i];
			PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
			
			if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
				analisePendenciaDt.setVisualizarPedidoAssistencia(true);
			}
			
			//Resgata dados b�sicos de cada processo
			pendenciaDt.setProcessoDt(movimentacaone.consultarProcessoId(pendenciaDt.getId_Processo()));
			//Verifica se existe peticionamento com data posterior � conclus�o
			pendenciaDt.getProcessoDt().setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaone, pendenciaDt));
			//Verifica se existe Guias Pendentes para o processo
			pendenciaDt.getProcessoDt().setExisteGuiasPendentes(existeGuiasPendentes(movimentacaone, pendenciaDt));
			analisePendenciaDt.addPendenciasFechar(pendenciaDt);			
		}

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoConclusao);
	}

	/**
	 * M�todo que faz tratamentos necess�rios para setar o fluxo de redirecionamento correto
	 * levando em considera��o a servlet para qual retornar e se o filtro de tipo de conclus�o est� preenchido
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
	 * Retorna todos os dados de uma pr�-analise finalizada, e monta todo o hist�rico de corre��es existentes.
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

				//Consulta as pend�ncias filhas da pr�-analise selecionada do tipo Conclus�o
				listaFilhasConclusao = movimentacaone.consultarPendenciasFinalizadasFilhas(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pend�ncia, que ser�o o arquivo da pr�-analise e an�lise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaConclusaoFinalizada(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo n�o � assinado, � pr�-analise
						if (pendenciaArquivoDt.getArquivoDt().getUsuarioAssinador().equals("")) {
							//Sendo o arquivo de resposta a �ltima pr�-analise, busca ent�o o respons�vel
							pendenciaArquivoDt.setResponsavelPreAnalise( movimentacaone.consultaResponsavelPreAnaliseFinalizada(pendenciaArquivoDt.getId()));
							arquivoPreAnalise = pendenciaArquivoDt;
						} else {
							pendencia.addListaArquivos(pendenciaArquivoDt);
							listaFilhasConclusao.add(pendencia);
						}
					}
				}

				if (arquivoPreAnalise != null) {
					//Recupera dados da pr�-analise que foi selecionada
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
	 * Monta tela para altera��o de pr�-an�lises m�ltiplas
	 * @param pendenciasFechar: Esse m�todo j� recebe uma lista de objetos das pend�ncias a serem fechadas.
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, List pendenciasFechar, int fluxo, String tipoConclusao, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe){

		analisePendenciaDt.setMultipla(true);

		//Adiciona pend�ncias a serem fechadas
		analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar);

		//Seta os tipos de pend�ncia que poder�o ser gerados
		//analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt.getGrupoCodigo()));

		request.setAttribute("TituloPagina", "Pr�-Analisar M�ltiplos Autos Conclusos");

		setarPreAnalise(request, analisePendenciaDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoConclusao);
	}

	/**
	 * M�todo que ir� setar no request as informa��es da pr�-an�lise j� efetuada por um assistente
	 * para que o juiz possa finalizar
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt){
		//Recupera lista das pend�ncias definida pelo assistente. � necess�rio convers�o pois Lista DWR espera um Set
		if (analisePendenciaDt.getListaPendenciasGerar() != null) {
			request.getSession().setAttribute("ListaPendencias", Funcoes.converterListParaSet(analisePendenciaDt.getListaPendenciasGerar()));
			request.getSession().setAttribute("Id_ListaDadosMovimentacao", analisePendenciaDt.getListaPendenciasGerar().size());
		}

		//Seta dados da pr�-analise em AnaliseConclusaoDt para mostrar na tela
		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			PendenciaArquivoDt preAnalise = analisePendenciaDt.getArquivoPreAnalise();
			analisePendenciaDt.setTextoEditor(preAnalise.getArquivoDt().getArquivo());
			analisePendenciaDt.setId_ArquivoTipo(preAnalise.getArquivoDt().getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(preAnalise.getArquivoDt().getArquivoTipo());
			analisePendenciaDt.setNomeArquivo(preAnalise.getArquivoDt().getNomeArquivo());
			analisePendenciaDt.setDataPreAnalise(preAnalise.getArquivoDt().getDataInsercao());
			
			//Se pr�-an�lise foi feita por assistente
			if (preAnalise.getAssistenteResponsavel() != null && !preAnalise.getAssistenteResponsavel().equals("")) analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getAssistenteResponsavel());
			else analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getJuizResponsavel());
		}
	}

	/**
	 * Consulta de autos conclusos pendentes
	 * Se � um juiz retorna as conclus�es para seu ServentiaCargo, se � assistente retorna
	 * as conclus�es do ServentiaCargo de seu usu�rio chefe
	 * @throws Exception 
	 */
	protected void consultarConclusao(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaone, int tempFluxo1, UsuarioNe usuarioSessao) throws Exception{
		if (tempFluxo1 == 0) {//Significa que foi acionado Bot�o Limpar
			analisePendenciaDt.setNumeroProcesso("null");
			analisePendenciaDt.setId_Classificador("null");
		}

		//Verifica se usu�rio pode analisar e pr�-analisar
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		request.setAttribute("id_Classificador", analisePendenciaDt.getId_Classificador());
		request.setAttribute("classificador", analisePendenciaDt.getClassificador());

		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
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
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "N�o h� Conclus�es Pendentes.");
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
		
		//verifica se o usu�rio pode trocar o respons�vel de pend�ncia
		if (analisePendenciaDt.getId_PendenciaTipo() != null && analisePendenciaDt.getId_PendenciaTipo().length()>0){
			PendenciaTipoDt pendenciaTipoDt = movimentacaone.consultarPendenciaTipo(analisePendenciaDt.getId_PendenciaTipo());			
		}
		
		if (usuarioSessao.isGabinetePresidenciaTjgo() || usuarioSessao.isGabineteVicePresidenciaTjgo() || usuarioSessao.isGabineteUpj()){
			request.setAttribute("podeDescartarPreAnalise", "false");
		}
	}

	/**
	 * Realiza chamada ao m�todo para consultar as pr�-analises simples de um usu�rio
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String tipoConclusao, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
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
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise Simples foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Consulta as pr�-analises multiplas efetuadas por um usuario ou serventia cargo que ainda n�o foram finalizadas
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
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise M�ltipla foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Captura lista de pend�ncias a serem geradas, e converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);
		return lista;
	}


	/**
	 * Realiza chamada ao m�todo para consultar as pr�-analises que j� foram finalizadas (assinadas)
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesFinalizadas(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usu�rio digitou "." e d�gito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarPreAnalisesConclusaoFinalizadas(analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal(), usuarioSessao.getUsuarioDt());
			if (tempList.size() > 0) {
				request.setAttribute("ListaPreAnalises", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise Localizada.");

		} else request.setAttribute("MensagemErro", "Informe par�metros para consulta: Data Inicial e Data Final ou N�mero do Processo.");
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
	 * Verifica se o tipo de movimenta��o selecionado � um tipo de movimenta��o j� configurado para o usu�rio logado
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
