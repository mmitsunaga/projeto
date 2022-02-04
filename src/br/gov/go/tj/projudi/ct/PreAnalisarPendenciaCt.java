package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PreAnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para contrar as pré-analises de outras pendências que serão tratadas como conclusões (Pedido de Vista, Relatório).
 * Suporte para pré-análise múltiplas.
 * 
 * @author msapaula
 */
public class PreAnalisarPendenciaCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6588241972732213078L;

	public int Permissao() {
		return PreAnalisePendenciaDt.CodigoPermissao;
	}

	public PreAnalisarPendenciaCt() {
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AnalisePendenciaDt analisePendenciaDt;
		MovimentacaoNe Movimentacaone;

		String Mensagem = "";
		int paginaAnterior = 0;
		int passoEditar = -1;	
		int fluxoEditar = -1;
		String pendencias[] = null;
		String id_Arquivo = null;
		String stAcao = "/WEB-INF/jsptjgo/PreAnalisarPendencia.jsp";

		request.setAttribute("tempPrograma", "Pré-Analisar Pendência");
		request.setAttribute("tempRetorno", "PreAnalisarPendencia");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		analisePendenciaDt = (AnalisePendenciaDt) request.getSession().getAttribute("AnalisePendenciadt");
		if (analisePendenciaDt == null) analisePendenciaDt = new AnalisePendenciaDt();

		analisePendenciaDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		analisePendenciaDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		analisePendenciaDt.setComplementoMovimentacao(request.getParameter("MovimentacaoComplemento"));
		analisePendenciaDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		analisePendenciaDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		analisePendenciaDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		analisePendenciaDt.setId_Modelo(request.getParameter("Id_Modelo"));
		
		analisePendenciaDt.setModelo(request.getParameter("Modelo"));
		analisePendenciaDt.setTextoEditor(request.getParameter("TextoEditor"));
		analisePendenciaDt.setId_TipoPendencia(request.getParameter("tipo"));
		analisePendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		analisePendenciaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("FluxoEditar") != null) fluxoEditar = Funcoes.StringToInt(request.getParameter("FluxoEditar"));

		setParametrosAuxiliares(request, analisePendenciaDt, paginaAnterior, paginaatual, passoEditar, Movimentacaone, UsuarioSessao);

		if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Pré-Analisar Múltiplass Pendências");
		else request.setAttribute("TituloPagina", "Pré-Analisar Pendência");

		int fluxo = analisePendenciaDt.getFluxo();
		String tipoPendencia = analisePendenciaDt.getId_PendenciaTipo();

		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			case Configuracao.Novo:
				// Captura as pendências que serão pré-analisadas
				if (request.getParameterValues("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				//Quando trata de análise múltipla
				if (pendencias != null) {
					if (pendencias.length > 1) {
						analisePendenciaDt = new AnalisePendenciaDt();
						montarTelaPreAnaliseMultipla(request, analisePendenciaDt, pendencias, fluxo, tipoPendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone);

					} else {
						//Verifica se existe uma pré-analise para a pendência selecionada
						analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(pendencias[0]);

						if (analisePendenciaDt == null) analisePendenciaDt = new AnalisePendenciaDt();
						else setarPreAnalise(request, analisePendenciaDt);

						montarTelaPreAnaliseSimples(request, analisePendenciaDt, pendencias[0], fluxo, tipoPendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Pendência foi selecionada.");
					setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				analisePendenciaDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				break;

			case Configuracao.Salvar:
				if (fluxoEditar == 2) {
					analisePendenciaDt = montarPreAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
					analisePendenciaDt.setFluxo(fluxo);
					analisePendenciaDt.setId_TipoPendencia(tipoPendencia);
					
					if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) {
						request.setAttribute("MensagemErro", "Impossível realizar esta ação. Guardar para assinar é somente para pré-análises simples.");
						
						consultarPreAnalisesSimples(request, analisePendenciaDt, analisePendenciaDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
						stAcao = "/WEB-INF/jsptjgo/PreAnalisePendenciaLocalizar.jsp";
						analisePendenciaDt.limpar();
						analisePendenciaDt.setFluxo(Configuracao.Curinga6);
						if (UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getId_PendenciaTipo()))) {							
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
							//request.setAttribute("exibePendenciaAssinatura", true);
						}
					} else {
						request.setAttribute("Mensagem", "Deseja guardar para assinar a pré-análise?");
						request.setAttribute("PassoEditar", fluxoEditar);
						stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnalisePendencia.jsp";
					}					
				} else {
					analisePendenciaDt.setPasso1("Passo 1 OK");
					analisePendenciaDt.setPasso2("Passo 2");			
										
					//request.setAttribute("exibePendenciaAssinatura", UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getTipoPendenciaCodigo())));
										
					
					stAcao = "/WEB-INF/jsptjgo/PreAnalisarPendenciaConfirmacao.jsp";	
				}
				break;

			case Configuracao.SalvarResultado:
				Mensagem = Movimentacaone.verificarPreAnalisePendencia(analisePendenciaDt, request.getParameter("IdProcessoValidacaoAba"));
				if (Mensagem.length() == 0) {
					analisePendenciaDt.setPendenteAssinatura((passoEditar == 2));
					Movimentacaone.salvarPreAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());

					//Quando trata de análise múltipla
					if (analisePendenciaDt.isMultipla()) request.setAttribute("MensagemOk", "Pré-Análise Múltipla registrada com sucesso.");
					else request.setAttribute("MensagemOk", "Pré-Análise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()));

					// Limpa da sessão os atributos
					analisePendenciaDt.limpar();

					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;

				} else request.setAttribute("MensagemErro", Mensagem);
				
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR PENDÊNCIAS NÃO ANALISADAS
			case Configuracao.Localizar:
				consultarPendenciasNaoAnalisadas(request, analisePendenciaDt, Movimentacaone, passoEditar, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/PendenciasNaoAnalisadasLocalizar.jsp";
				analisePendenciaDt.limpar();
				analisePendenciaDt.setFluxo(paginaatual);
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AS PRÉ-ANALISES SIMPLES EFETUADAS PELO USUÁRIO
			// Pré-analises não assinados são exibidas e podem ser alteradas ou descartadas
			case Configuracao.Curinga6:
				if (request.getParameter("MensagemOk")!= null)
					request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
				consultarPreAnalisesSimples(request, analisePendenciaDt, analisePendenciaDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/PreAnalisePendenciaLocalizar.jsp";
				analisePendenciaDt.limpar();
				analisePendenciaDt.setFluxo(paginaatual);
				if (UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getId_PendenciaTipo()))){			
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
					//request.setAttribute("exibePendenciaAssinatura", true);
				}				
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AS PRÉ-ANÁLISES MULTIPLAS EFETUADAS PELO USUÁRIO
			case Configuracao.Curinga7:
				consultarPreAnalisesMultiplas(request, analisePendenciaDt, Movimentacaone, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseMultiplaPendenciaLocalizar.jsp";
				analisePendenciaDt.limpar();
				analisePendenciaDt.setFluxo(paginaatual);
				break;

			// ALTERAÇÃO DE PRÉ-ANÁLISE MÚLITPLA
			case Configuracao.Curinga8:

				//Se um arquivo de pré-análise foi passado, verificar quais pendências ele está vinculado
				id_Arquivo = request.getParameter("Id_Arquivo");

				if (id_Arquivo != null) {
					//Busca pendências vinculadas ao arquivo
					List pendenciasFechar = Movimentacaone.consultarPendencias(id_Arquivo);
					
					if(pendenciasFechar!=null && pendenciasFechar.size()>0){

						//Busca arquivo de pré-análise para primeira pendência vinculada
						PendenciaDt primeiraPendencia = (PendenciaDt) pendenciasFechar.get(0);
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnalisePendencia(primeiraPendencia.getId());
	
						//Se encontrou uma pré-analise para a pendência, compara se é o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pré-analise
							analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(pendenciaArquivoDt);
							//Monta tela para análise das pré-analises multiplas
							montarTelaPreAnaliseMultipla(request, analisePendenciaDt, pendenciasFechar, fluxo, tipoPendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone);
						}
					}
				}
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AS PRÉ-ANÁLISES FEITAS PELO USUÁRIO E QUE JÁ FORAM ASSINADAS PELO JUIZ
			case Configuracao.Curinga9:
				//Se fluxo anterior é esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga9) consultarPreAnalisesFinalizadas(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
				else {
					analisePendenciaDt = new AnalisePendenciaDt();
					analisePendenciaDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/PreAnalisePendenciaAssinadaLocalizar.jsp";

				break;

			//Prepara dados para Descartar Pré-Análise
			case Configuracao.Excluir:
				analisePendenciaDt = montarPreAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
				analisePendenciaDt.setFluxo(fluxo);
				analisePendenciaDt.setId_TipoPendencia(tipoPendencia);
				if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) request.setAttribute("Mensagem", "Deseja retirar o processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getPendenciaDt().getProcessoNumero()) + " da pré-análise múltipla selecionada?");
				else request.setAttribute("Mensagem", "Deseja descartar a pré-análise?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnalisePendencia.jsp";
				break;

			//FUNÇÃO UTILIZADA PARA DESCARTAR PRÉ-ANÁLISE
			case Configuracao.ExcluirResultado:
				LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());

				Movimentacaone.descartarPreAnalise(analisePendenciaDt.getListaPendenciasFechar(), UsuarioSessao.getUsuarioDt(), logDt);
				request.setAttribute("MensagemOk", "Pré-Análise descartada com sucesso.");

				// Limpa da sessão os atributos
				analisePendenciaDt.limpar();

				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

				// Consultar Tipos de Movimentação
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"MovimentacaoTipo"};
				String[] lisDescricao = {"MovimentacaoTipo"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_MovimentacaoTipo");
				request.setAttribute("tempBuscaDescricao","MovimentacaoTipo");
				request.setAttribute("tempBuscaPrograma","MovimentacaoTipo");			
				request.setAttribute("tempRetorno","PreAnalisarPendencia");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PaginaAnterior", String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = Movimentacaone.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
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
					request.setAttribute("tempRetorno","PreAnalisarPendencia");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
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
				request.setAttribute("tempRetorno", "PreAnalisarPendencia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempDescricaoDescricao", "Modelo");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
				stTemp = Movimentacaone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), analisePendenciaDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
					
				
				return;
			}
				break;

			default:
				//Controles da variável passoEditar
				// 0 : Redireciona para passo 1 - Dados Pré-Análise
				switch (passoEditar) {

					case 0:
						analisePendenciaDt.setPasso1("Passo 1");
						analisePendenciaDt.setPasso2("");
						break;

					default:
						//Refere-se a visualização de uma pré-analise já finalizada
						if (fluxo == Configuracao.Curinga9) {
							analisePendenciaDt = montarHistoricoPreAnaliseFinalizada(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
							analisePendenciaDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnalisePendenciaFinalizada.jsp";
						}
				}
				break;
		}

		request.setAttribute("GrupoTipoUsuarioLogado", UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt());
		request.setAttribute("TextoEditor", analisePendenciaDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", analisePendenciaDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", analisePendenciaDt.getArquivoTipo());
		request.setAttribute("Modelo", analisePendenciaDt.getModelo());

		request.getSession().setAttribute("AnalisePendenciadt", analisePendenciaDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		//Caso o tipo de Movimentação escolhido não esteja configurado teremos que adicioná-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analisePendenciaDt);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Seta parâmetros auxiliares para a pré-analise de pendências
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, int paginaAnterior, int paginaAtual, int passoEditar, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));

		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!analisePendenciaDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(), analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
			analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
			analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());

		//Tratamento mensagem de Confirmação de Análise partindo de uma pré-analise simples
		//Essa mensagem pode vir redirecionada da servlet AnalisarPendenciaCt e virá como parameter
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
	protected AnalisePendenciaDt montarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{
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
		pendenciaArquivoDt = movimentacaoNe.getArquivoPreAnalisePendencia(id_Pendencia);

		//Se encontrou uma pré-analise para a pendência
		if (pendenciaArquivoDt != null) {
			//Recupera dados da pré-analise
			analisePendenciaDt = movimentacaoNe.getPreAnalisePendencia(pendenciaArquivoDt);
			analisePendenciaDt.setPendenciaDt(pendencia);

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
	protected void montarTelaPreAnaliseSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String id_Pendencia, int fluxo, String tipoPendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaone) throws Exception{
		//Pesquisa dados da pendência
		PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaone.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
	}

	/**
	 * Monta a tela inserção de pré-análise múltipla.
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String[] pendencias, int fluxo, String tipoPendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaone) throws Exception{

		analisePendenciaDt.setMultipla(true);

		//Pesquisa pendências
		for (int i = 0; i < pendencias.length; i++) {
			String id_Pendencia = (String) pendencias[i];
			PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
			//Resgata dados básicos de cada processo
			pendenciaDt.setProcessoDt(movimentacaone.consultarProcessoId(pendenciaDt.getId_Processo()));
			analisePendenciaDt.addPendenciasFechar(pendenciaDt);
		}

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
	}

	/**
	 * Método que faz tratamentos necessários para setar o fluxo de redirecionamento correto
	 * levando em consideração a servlet para qual retornar e se o filtro de tipo de pendência está preenchido
	 * @param analisePendenciaDt
	 * @param fluxo
	 * @param tipoPendencia
	 */
	protected void setFluxoRedirecionamento(AnalisePendenciaDt analisePendenciaDt, int fluxo, String tipoPendencia) {
		if (fluxo != Configuracao.Curinga6 && fluxo != Configuracao.Curinga7 && fluxo != Configuracao.Curinga9) analisePendenciaDt.setFluxo(Configuracao.Localizar);
		else analisePendenciaDt.setFluxo(fluxo);
		analisePendenciaDt.setId_TipoPendencia(tipoPendencia);
	}

	/**
	 * Retorna todos os dados de uma pré-analise finalizada, e monta todo o histórico de correções existentes.
	 * @throws Exception 
	 */
	protected AnalisePendenciaDt montarHistoricoPreAnaliseFinalizada(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{

		PendenciaDt pendencia = null;
		List listaFilhas = null;
		List arquivosResposta = null;
		PendenciaArquivoDt arquivoPreAnalise = null;
		String id_Pendencia = request.getParameter("Id_Pendencia");

		if (id_Pendencia != null) {
			if (analisePendenciaDt.getPendenciaDt() == null || !id_Pendencia.equalsIgnoreCase(analisePendenciaDt.getPendenciaDt().getId())) {

				pendencia = movimentacaone.consultarFinalizadaId(id_Pendencia);

				//Consulta as pendências filhas da pré-analise selecionada do tipo Pendência
				listaFilhas = movimentacaone.consultarPendenciasFinalizadasFilhas(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pendência, que serão o arquivo da pré-analise e análise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaPendenciaFinalizada(pendencia.getId(), usuarioSessao);
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
							listaFilhas.add(pendencia);
						}
					}
				}

				if (arquivoPreAnalise != null) {
					//Recupera dados da pré-analise que foi selecionada
					analisePendenciaDt = movimentacaone.getPreAnalisePendencia(arquivoPreAnalise);
				}
				analisePendenciaDt.setHistoricoPendencia(listaFilhas);
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
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, List pendenciasFechar, int fluxo, String tipoPendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe){

		analisePendenciaDt.setMultipla(true);

		//Adiciona pendências a serem fechadas
		analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar);

		request.setAttribute("TituloPagina", "Pré-Analisar Múltiplas Pendências");

		setarPreAnalise(request, analisePendenciaDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
	}

	/**
	 * Método que irá setar no request as informações da pré-análise já efetuada por um assistente
	 * para que o juiz possa finalizar
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt){

		//Seta dados da pré-analise em AnalisePendenciaDt para mostrar na tela
		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			PendenciaArquivoDt preAnalise = analisePendenciaDt.getArquivoPreAnalise();
			analisePendenciaDt.setTextoEditor(preAnalise.getArquivoDt().getArquivo());
			analisePendenciaDt.setId_ArquivoTipo(preAnalise.getArquivoDt().getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(preAnalise.getArquivoDt().getArquivoTipo());
			analisePendenciaDt.setNomeArquivo(preAnalise.getArquivoDt().getNomeArquivo());
			analisePendenciaDt.setDataPreAnalise(preAnalise.getArquivoDt().getDataInsercao());

		}
	}

	/**
	 * Consulta de pendência não analisadas
	 * @throws Exception 
	 */
	protected void consultarPendenciasNaoAnalisadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaone, int passoEditar, UsuarioNe usuarioSessao) throws Exception{
		if (passoEditar == 0) {//Significa que foi acionado Botão Limpar
			analisePendenciaDt.setNumeroProcesso("null");
		}

		//Verifica se usuário pode analisar e pré-analisar
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
			request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
		else
			request.setAttribute("podeAnalisar", "false");
		
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnalisePendenciaDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());

		
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
			request.setAttribute("podeGerarVotoEmenta", "true");
		
		//Verifica se usuário digitou "." e dígito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return;
		}

		List tempList = movimentacaone.consultarPendenciasNaoAnalisadas(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_PendenciaTipo());
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaPendencias", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Não há Pendências em Aberto.");
		}

	}

	/**
	 * Realiza chamada ao método para consultar as pré-analises simples de um usuário
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String tipoPendencia, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		//Verifica se usuário digitou "." e dígito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return boRetorno;
		}

		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());

		List tempList = movimentacaone.consultarPreAnalisesSimples(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso(), tipoPendencia);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaPreAnalises", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
			else
				request.setAttribute("podeAnalisar", "false");
			
			if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeGerarVotoEmenta", "true");
			
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnalisePendenciaDt.CodigoPermissao));
			boRetorno = true;
			
			//Atualiza o rash para consulta
			for (Object pendenciaArquivoObj : tempList) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)pendenciaArquivoObj;
				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pré-Análise Simples foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Consulta as pré-analises multiplas efetuadas por um usuario ou serventia cargo que ainda não foram finalizadas
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesMultiplas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;

		List tempList = movimentacaoNe.consultarPreAnalisesMultiplas(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso());
		if (tempList!= null && tempList.size() > 0) {
			request.setAttribute("ListaPreAnalises", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
					&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
			else
				request.setAttribute("podeAnalisar", "false");
			
			if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeGerarVotoEmenta", "true");
			
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnalisePendenciaDt.CodigoPermissao));
			boRetorno = true;
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pré-Análise Múltipla foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Consulta tipos de movimentação filtrando pelo grupo do usuário logado
	 * @throws Exception 
	 */
	protected boolean consultarMovimentacaoTipo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, MovimentacaoNe movimentacaone, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		List tempList = null;

		tempList = movimentacaone.consultarGrupoMovimentacaoTipo(usuarioSessao.getUsuarioDt(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaMovimentacaoTipo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", movimentacaone.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_MovimentacaoTipo", "Id_MovimentacaoTipo");
			request.setAttribute("tempBuscaMovimentacaoTipo", "MovimentacaoTipo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Movimentação localizado.");

		return boRetorno;
	}

	/**
	 * Consulta de modelos
	 * @throws Exception 
	 */
	protected boolean consultarModelo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, MovimentacaoNe movimentacaone, AnalisePendenciaDt analisePendenciaDt, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		List tempList = movimentacaone.consultarModelo(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getId_ArquivoTipo(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaModelo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", movimentacaone.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
			request.setAttribute("tempBuscaModelo", "Modelo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Modelo Localizado.");

		return boRetorno;
	}

	/**
	 * Consulta tipos de arquivos vinculados ao grupo do usuário logado
	 * @throws Exception 
	 */
	protected boolean consultarArquivoTipo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, int paginaatual, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		List tempList = movimentacaone.consultarGrupoArquivoTipo(usuarioSessao.getUsuarioDt(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaArquivoTipo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", movimentacaone.getQuantidadePaginas());
			request.setAttribute("tempBuscaArquivoTipo", "ArquivoTipo");
			request.setAttribute("tempBuscaId_ArquivoTipo", "Id_ArquivoTipo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Arquivo localizado.");

		return boRetorno;
	}

	/**
	 * Realiza chamada ao método para consultar as pré-analises que já foram finalizadas (assinadas)
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesFinalizadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usuário digitou "." e dígito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarPreAnalisesFinalizadas(analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal(), usuarioSessao.getUsuarioDt());
			if (tempList.size() > 0) {
				request.setAttribute("ListaPreAnalises", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Pré-Análise Localizada.");

		} else request.setAttribute("MensagemErro", "Informe parâmetros para consulta: Data Inicial e Data Final ou Número do Processo.");
		return boRetorno;
	}
		

	/**
	 * Verifica se o tipo de movimentação selecionado é um tipo de movimentação já configurado para o usuário logado
	 * @param analisePendenciaDt
	 */
	protected void verifiqueMovimentacaoTipo(AnalisePendenciaDt analisePendenciaDt) {
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
	
}
