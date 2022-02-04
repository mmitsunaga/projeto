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
 * Servlet para contrar as pr�-analises de outras pend�ncias que ser�o tratadas como conclus�es (Pedido de Vista, Relat�rio).
 * Suporte para pr�-an�lise m�ltiplas.
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

		request.setAttribute("tempPrograma", "Pr�-Analisar Pend�ncia");
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

		if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Pr�-Analisar M�ltiplass Pend�ncias");
		else request.setAttribute("TituloPagina", "Pr�-Analisar Pend�ncia");

		int fluxo = analisePendenciaDt.getFluxo();
		String tipoPendencia = analisePendenciaDt.getId_PendenciaTipo();

		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			case Configuracao.Novo:
				// Captura as pend�ncias que ser�o pr�-analisadas
				if (request.getParameterValues("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				//Quando trata de an�lise m�ltipla
				if (pendencias != null) {
					if (pendencias.length > 1) {
						analisePendenciaDt = new AnalisePendenciaDt();
						montarTelaPreAnaliseMultipla(request, analisePendenciaDt, pendencias, fluxo, tipoPendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone);

					} else {
						//Verifica se existe uma pr�-analise para a pend�ncia selecionada
						analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(pendencias[0]);

						if (analisePendenciaDt == null) analisePendenciaDt = new AnalisePendenciaDt();
						else setarPreAnalise(request, analisePendenciaDt);

						montarTelaPreAnaliseSimples(request, analisePendenciaDt, pendencias[0], fluxo, tipoPendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Pend�ncia foi selecionada.");
					setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
				analisePendenciaDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				break;

			case Configuracao.Salvar:
				if (fluxoEditar == 2) {
					analisePendenciaDt = montarPreAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
					analisePendenciaDt.setFluxo(fluxo);
					analisePendenciaDt.setId_TipoPendencia(tipoPendencia);
					
					if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) {
						request.setAttribute("MensagemErro", "Imposs�vel realizar esta a��o. Guardar para assinar � somente para pr�-an�lises simples.");
						
						consultarPreAnalisesSimples(request, analisePendenciaDt, analisePendenciaDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
						stAcao = "/WEB-INF/jsptjgo/PreAnalisePendenciaLocalizar.jsp";
						analisePendenciaDt.limpar();
						analisePendenciaDt.setFluxo(Configuracao.Curinga6);
						if (UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getId_PendenciaTipo()))) {							
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
							//request.setAttribute("exibePendenciaAssinatura", true);
						}
					} else {
						request.setAttribute("Mensagem", "Deseja guardar para assinar a pr�-an�lise?");
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

					//Quando trata de an�lise m�ltipla
					if (analisePendenciaDt.isMultipla()) request.setAttribute("MensagemOk", "Pr�-An�lise M�ltipla registrada com sucesso.");
					else request.setAttribute("MensagemOk", "Pr�-An�lise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()));

					// Limpa da sess�o os atributos
					analisePendenciaDt.limpar();

					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;

				} else request.setAttribute("MensagemErro", Mensagem);
				
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR PEND�NCIAS N�O ANALISADAS
			case Configuracao.Localizar:
				consultarPendenciasNaoAnalisadas(request, analisePendenciaDt, Movimentacaone, passoEditar, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/PendenciasNaoAnalisadasLocalizar.jsp";
				analisePendenciaDt.limpar();
				analisePendenciaDt.setFluxo(paginaatual);
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-ANALISES SIMPLES EFETUADAS PELO USU�RIO
			// Pr�-analises n�o assinados s�o exibidas e podem ser alteradas ou descartadas
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

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-AN�LISES MULTIPLAS EFETUADAS PELO USU�RIO
			case Configuracao.Curinga7:
				consultarPreAnalisesMultiplas(request, analisePendenciaDt, Movimentacaone, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseMultiplaPendenciaLocalizar.jsp";
				analisePendenciaDt.limpar();
				analisePendenciaDt.setFluxo(paginaatual);
				break;

			// ALTERA��O DE PR�-AN�LISE M�LITPLA
			case Configuracao.Curinga8:

				//Se um arquivo de pr�-an�lise foi passado, verificar quais pend�ncias ele est� vinculado
				id_Arquivo = request.getParameter("Id_Arquivo");

				if (id_Arquivo != null) {
					//Busca pend�ncias vinculadas ao arquivo
					List pendenciasFechar = Movimentacaone.consultarPendencias(id_Arquivo);
					
					if(pendenciasFechar!=null && pendenciasFechar.size()>0){

						//Busca arquivo de pr�-an�lise para primeira pend�ncia vinculada
						PendenciaDt primeiraPendencia = (PendenciaDt) pendenciasFechar.get(0);
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnalisePendencia(primeiraPendencia.getId());
	
						//Se encontrou uma pr�-analise para a pend�ncia, compara se � o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pr�-analise
							analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(pendenciaArquivoDt);
							//Monta tela para an�lise das pr�-analises multiplas
							montarTelaPreAnaliseMultipla(request, analisePendenciaDt, pendenciasFechar, fluxo, tipoPendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone);
						}
					}
				}
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-AN�LISES FEITAS PELO USU�RIO E QUE J� FORAM ASSINADAS PELO JUIZ
			case Configuracao.Curinga9:
				//Se fluxo anterior � esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga9) consultarPreAnalisesFinalizadas(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
				else {
					analisePendenciaDt = new AnalisePendenciaDt();
					analisePendenciaDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/PreAnalisePendenciaAssinadaLocalizar.jsp";

				break;

			//Prepara dados para Descartar Pr�-An�lise
			case Configuracao.Excluir:
				analisePendenciaDt = montarPreAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
				analisePendenciaDt.setFluxo(fluxo);
				analisePendenciaDt.setId_TipoPendencia(tipoPendencia);
				if (request.getParameter("multiplo") != null && request.getParameter("multiplo").equals("true")) request.setAttribute("Mensagem", "Deseja retirar o processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getPendenciaDt().getProcessoNumero()) + " da pr�-an�lise m�ltipla selecionada?");
				else request.setAttribute("Mensagem", "Deseja descartar a pr�-an�lise?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnalisePendencia.jsp";
				break;

			//FUN��O UTILIZADA PARA DESCARTAR PR�-AN�LISE
			case Configuracao.ExcluirResultado:
				LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());

				Movimentacaone.descartarPreAnalise(analisePendenciaDt.getListaPendenciasFechar(), UsuarioSessao.getUsuarioDt(), logDt);
				request.setAttribute("MensagemOk", "Pr�-An�lise descartada com sucesso.");

				// Limpa da sess�o os atributos
				analisePendenciaDt.limpar();

				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

				// Consultar Tipos de Movimenta��o
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

			// Consultar Modelos do Usu�rio
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
				//Controles da vari�vel passoEditar
				// 0 : Redireciona para passo 1 - Dados Pr�-An�lise
				switch (passoEditar) {

					case 0:
						analisePendenciaDt.setPasso1("Passo 1");
						analisePendenciaDt.setPasso2("");
						break;

					default:
						//Refere-se a visualiza��o de uma pr�-analise j� finalizada
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
		
		//Caso o tipo de Movimenta��o escolhido n�o esteja configurado teremos que adicion�-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analisePendenciaDt);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Seta par�metros auxiliares para a pr�-analise de pend�ncias
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, int paginaAnterior, int paginaAtual, int passoEditar, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));

		// Quando modelo foi selecionado monta conte�do para aparecer no editor e j� carrego o tipo do arquivo
		if (!analisePendenciaDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(), analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
			analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
			analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());

		//Tratamento mensagem de Confirma��o de An�lise partindo de uma pr�-analise simples
		//Essa mensagem pode vir redirecionada da servlet AnalisarPendenciaCt e vir� como parameter
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
	protected AnalisePendenciaDt montarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{
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
		pendenciaArquivoDt = movimentacaoNe.getArquivoPreAnalisePendencia(id_Pendencia);

		//Se encontrou uma pr�-analise para a pend�ncia
		if (pendenciaArquivoDt != null) {
			//Recupera dados da pr�-analise
			analisePendenciaDt = movimentacaoNe.getPreAnalisePendencia(pendenciaArquivoDt);
			analisePendenciaDt.setPendenciaDt(pendencia);

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
	protected void montarTelaPreAnaliseSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String id_Pendencia, int fluxo, String tipoPendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaone) throws Exception{
		//Pesquisa dados da pend�ncia
		PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pend�ncia
		ProcessoDt processoDt = movimentacaone.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
	}

	/**
	 * Monta a tela inser��o de pr�-an�lise m�ltipla.
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String[] pendencias, int fluxo, String tipoPendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaone) throws Exception{

		analisePendenciaDt.setMultipla(true);

		//Pesquisa pend�ncias
		for (int i = 0; i < pendencias.length; i++) {
			String id_Pendencia = (String) pendencias[i];
			PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
			//Resgata dados b�sicos de cada processo
			pendenciaDt.setProcessoDt(movimentacaone.consultarProcessoId(pendenciaDt.getId_Processo()));
			analisePendenciaDt.addPendenciasFechar(pendenciaDt);
		}

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
	}

	/**
	 * M�todo que faz tratamentos necess�rios para setar o fluxo de redirecionamento correto
	 * levando em considera��o a servlet para qual retornar e se o filtro de tipo de pend�ncia est� preenchido
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
	 * Retorna todos os dados de uma pr�-analise finalizada, e monta todo o hist�rico de corre��es existentes.
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

				//Consulta as pend�ncias filhas da pr�-analise selecionada do tipo Pend�ncia
				listaFilhas = movimentacaone.consultarPendenciasFinalizadasFilhas(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pend�ncia, que ser�o o arquivo da pr�-analise e an�lise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaPendenciaFinalizada(pendencia.getId(), usuarioSessao);
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
							listaFilhas.add(pendencia);
						}
					}
				}

				if (arquivoPreAnalise != null) {
					//Recupera dados da pr�-analise que foi selecionada
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
	 * Monta tela para altera��o de pr�-an�lises m�ltiplas
	 * @param pendenciasFechar: Esse m�todo j� recebe uma lista de objetos das pend�ncias a serem fechadas.
	 */
	protected void montarTelaPreAnaliseMultipla(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, List pendenciasFechar, int fluxo, String tipoPendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe){

		analisePendenciaDt.setMultipla(true);

		//Adiciona pend�ncias a serem fechadas
		analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar);

		request.setAttribute("TituloPagina", "Pr�-Analisar M�ltiplas Pend�ncias");

		setarPreAnalise(request, analisePendenciaDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, tipoPendencia);
	}

	/**
	 * M�todo que ir� setar no request as informa��es da pr�-an�lise j� efetuada por um assistente
	 * para que o juiz possa finalizar
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt){

		//Seta dados da pr�-analise em AnalisePendenciaDt para mostrar na tela
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
	 * Consulta de pend�ncia n�o analisadas
	 * @throws Exception 
	 */
	protected void consultarPendenciasNaoAnalisadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaone, int passoEditar, UsuarioNe usuarioSessao) throws Exception{
		if (passoEditar == 0) {//Significa que foi acionado Bot�o Limpar
			analisePendenciaDt.setNumeroProcesso("null");
		}

		//Verifica se usu�rio pode analisar e pr�-analisar
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
		
		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
			return;
		}

		List tempList = movimentacaone.consultarPendenciasNaoAnalisadas(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_PendenciaTipo());
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaPendencias", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "N�o h� Pend�ncias em Aberto.");
		}

	}

	/**
	 * Realiza chamada ao m�todo para consultar as pr�-analises simples de um usu�rio
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String tipoPendencia, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
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
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise Simples foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Consulta as pr�-analises multiplas efetuadas por um usuario ou serventia cargo que ainda n�o foram finalizadas
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
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise M�ltipla foi localizada.");
		}

		return boRetorno;
	}

	/**
	 * Consulta tipos de movimenta��o filtrando pelo grupo do usu�rio logado
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
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Movimenta��o localizado.");

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
	 * Consulta tipos de arquivos vinculados ao grupo do usu�rio logado
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
	 * Realiza chamada ao m�todo para consultar as pr�-analises que j� foram finalizadas (assinadas)
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisesFinalizadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usu�rio digitou "." e d�gito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarPreAnalisesFinalizadas(analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal(), usuarioSessao.getUsuarioDt());
			if (tempList.size() > 0) {
				request.setAttribute("ListaPreAnalises", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise Localizada.");

		} else request.setAttribute("MensagemErro", "Informe par�metros para consulta: Data Inicial e Data Final ou N�mero do Processo.");
		return boRetorno;
	}
		

	/**
	 * Verifica se o tipo de movimenta��o selecionado � um tipo de movimenta��o j� configurado para o usu�rio logado
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
