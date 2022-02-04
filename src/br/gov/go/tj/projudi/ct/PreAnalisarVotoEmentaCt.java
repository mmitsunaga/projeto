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
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoPendenciaNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para contrar as pr�-analises dos autos conclusos.
 * Suporte para pr�-an�lise m�ltiplas.
 * 
 * @author mmgomes
 */
public class PreAnalisarVotoEmentaCt extends Controle {

    private static final long serialVersionUID = 6588241972732213078L;

    public int Permissao() {
		return PreAnaliseConclusaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AnaliseConclusaoDt analiseConclusaoDt;
		MovimentacaoNe Movimentacaone;

		String Mensagem = "";		
		int paginaAnterior = 0;
		int tempFluxo1 = -1;
		String pendencias[] = null;	
		
		String stNomeBusca1 = "";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao = "/WEB-INF/jsptjgo/PreAnalisarVotoEmenta.jsp";

		request.setAttribute("tempPrograma", "Votos / Ementas");
		request.setAttribute("tempRetorno", "PreAnalisarVotoEmenta");
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		analiseConclusaoDt = (AnaliseConclusaoDt) request.getSession().getAttribute("AnalisePendenciadt");
		if (analiseConclusaoDt == null) analiseConclusaoDt = new AnaliseConclusaoDt();

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
		analiseConclusaoDt.setId_ArquivoTipoEmenta(request.getParameter("Id_ArquivoTipoEmenta"));
		analiseConclusaoDt.setArquivoTipoEmenta(request.getParameter("ArquivoTipoEmenta"));
		analiseConclusaoDt.setNomeArquivoEmenta(request.getParameter("nomeArquivoEmenta"));
		analiseConclusaoDt.setId_ModeloEmenta(request.getParameter("Id_ModeloEmenta"));
		analiseConclusaoDt.setModeloEmenta(request.getParameter("ModeloEmenta"));
		analiseConclusaoDt.setTextoEditorEmenta(request.getParameter("TextoEditorEmenta"));
		analiseConclusaoDt.setId_TipoPendencia(request.getParameter("tipo"));
		analiseConclusaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		analiseConclusaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if (request.getParameter("julgadoMerito") == null && request.getParameter("fluxo") != null && request.getParameter("fluxo").toString().equals("1"))
			analiseConclusaoDt.setJulgadoMeritoProcessoPrincipal("false");
		else if (request.getParameter("julgadoMerito") != null)
			analiseConclusaoDt.setJulgadoMeritoProcessoPrincipal(request.getParameter("julgadoMerito"));

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null")) tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo1"));

		setParametrosAuxiliares(request, analiseConclusaoDt, paginaAnterior, paginaatual, tempFluxo1, Movimentacaone, UsuarioSessao);
		
		request.setAttribute("TituloPagina", "Pr�-Analisar Votos / Ementas");

		int fluxo = analiseConclusaoDt.getFluxo();
		String tipoConclusao = analiseConclusaoDt.getId_PendenciaTipo();

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
						analiseConclusaoDt = new AnaliseConclusaoDt();						
						request.setAttribute("MensagemErro", "A Pr�An�lise do Voto / Ementa dever� ser realizada individualmente.");

					} else {
						
						//Verifica se existe uma pr�-analise para a conclus�o selecionada
						analiseConclusaoDt = Movimentacaone.getPreAnaliseConclusao(pendencias[0]);	
						
						if (analiseConclusaoDt == null)  analiseConclusaoDt = new AnaliseConclusaoDt();
							
						// Consulta o Tipo de Arquivo voto
						ArquivoTipoDt arquivoTipoDtVotoSessao = Movimentacaone.consultarArquivoTipoVotoSessao();
						if (arquivoTipoDtVotoSessao != null){								
							analiseConclusaoDt.setId_ArquivoTipo(arquivoTipoDtVotoSessao.getId());																					
							analiseConclusaoDt.setArquivoTipo(arquivoTipoDtVotoSessao.getArquivoTipo());
							
							analiseConclusaoDt.setArquivoTipoSomenteLeitura(true);											
						}
						
						// Seta o nome do arquivo de voto
						analiseConclusaoDt.setNomeArquivo("Relatorio_Voto_Acordao.html");
						
						// Consulta do Tipo de Arquivo Ementa
						ArquivoTipoDt arquivoTipoDtEmentaSessao = Movimentacaone.consultarArquivoTipoEmentaSessao();
						if (arquivoTipoDtEmentaSessao != null){								
							analiseConclusaoDt.setId_ArquivoTipoEmenta(arquivoTipoDtEmentaSessao.getId());																					
							analiseConclusaoDt.setArquivoTipoEmenta(arquivoTipoDtEmentaSessao.getArquivoTipo());
							
							analiseConclusaoDt.setArquivoTipoEmentaSomenteLeitura(true);											
						}
						
						// Seta o nome do arquivo de ementa
						analiseConclusaoDt.setNomeArquivoEmenta(AudienciaNe.nomeArquivoEmenta);	
						
						montarTelaPreAnaliseSimples(request, analiseConclusaoDt, pendencias[0], fluxo, tipoConclusao, Movimentacaone, UsuarioSessao);
						
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhum Voto / Ementa foi selecionado.");
					setFluxoRedirecionamento(analiseConclusaoDt, fluxo, tipoConclusao);
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
				analiseConclusaoDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				break;

			case Configuracao.Salvar:
				// Captura lista de pend�ncias
				analiseConclusaoDt.setListaPendenciasGerar(getListaPendencias(request));
				analiseConclusaoDt.setPasso2("Passo 2 OK");
				analiseConclusaoDt.setPasso3("Passo 3");
				
				stAcao = "/WEB-INF/jsptjgo/PreAnalisarVotoEmentaConfirmacao.jsp";
				break;

			case Configuracao.SalvarResultado:
				Mensagem = Movimentacaone.verificarPreAnaliseVotoEmenta(analiseConclusaoDt);
				if (Mensagem.length() == 0) {
					analiseConclusaoDt.setPendenteAssinatura((tempFluxo1 == 2));
					
					Movimentacaone.salvarPreAnaliseVotoEmenta(analiseConclusaoDt, UsuarioSessao.getUsuarioDt());

					request.setAttribute("MensagemOk", "Pr�-An�lise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analiseConclusaoDt.getNumeroPrimeiroProcessoListaFechar()));
					
					String voto = request.getParameter("pendencia");
					if (voto == null || voto.length() == 0){
						voto = analiseConclusaoDt.getId_PendenciaVotoGerada();
					}
					
					String ementa = request.getParameter("ementa");
					//a ementa pode ser obtida da tela, se a pend�ncia j� estiver pre-analisada ou  do objeto se a pend�ncia estiver sendo pre-analisada
					if (ementa == null || ementa.length() == 0){
						ementa = analiseConclusaoDt.getId_PendenciaEmentaGerada();
					}
					String novoServentiaCargo = analiseConclusaoDt.getId_ServentiaCargoVotoEmentaGerada();
					
					// Limpa da sess�o os atributos
					analiseConclusaoDt.limpar();

					// Limpa lista DWR e zera contador Pend�ncias
					request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
					request.getSession().removeAttribute("ListaPendencias");
					
					if(Funcoes.StringToBoolean(request.getParameter("SalvarRedistribuir")) ) {
						redireciona(response, "PendenciaResponsavel?PaginaAtual="+Configuracao.Novo+"&pendencia="+voto+"&CodigoPendencia="+request.getParameter("CodigoPendencia")+"&ementa="+ementa+"&novoServentiaCargo="+novoServentiaCargo+"&MensagemOk=Pr�-An�lise registrada com sucesso.");
					
					} else {
						this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;
					}

				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AUTOS CONCLUSOS PARA PR�-AN�LISE
			// Assistente ver� os autos conclusos para o ServentiaCargo que seu usu�rio chefe ocupa e que ainda n�o tenham uma pr�-analise registrada
			case Configuracao.Localizar:
				consultarConclusao(request, analiseConclusaoDt, Movimentacaone, tempFluxo1, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/VotoEmentaLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			// FUN��O UTILIZADA PARA LOCALIZAR AS PR�-ANALISES SIMPLES EFETUADAS PELO USU�RIO
			// Pr�-analises n�o assinados s�o exibidas e podem ser alteradas ou descartadas
			case Configuracao.Curinga6:
				consultarPreAnalisesSimples(request, analiseConclusaoDt, analiseConclusaoDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseVotoEmentaLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;
				
			case Configuracao.Curinga7:
				consultarPreAnalisesSimples(request, analiseConclusaoDt, analiseConclusaoDt.getId_PendenciaTipo(), UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/PreAnaliseVotoEmentaLocalizar.jsp";
				analiseConclusaoDt.limpar();
				analiseConclusaoDt.setFluxo(paginaatual);
				break;

			//Prepara dados para exclus�o
			case Configuracao.Excluir:
				analiseConclusaoDt = montarPreAnaliseExclusao(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
				analiseConclusaoDt.setFluxo(fluxo);
				analiseConclusaoDt.setId_TipoPendencia(tipoConclusao);							
				request.setAttribute("Mensagem", "Deseja descartar a pr�-an�lise?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseVotoEmenta.jsp";
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

				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;
				
			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador","Prioridade","Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Classificador");
					request.setAttribute("tempBuscaDescricao","Classificador");
					request.setAttribute("tempBuscaPrograma","Classificador");			
					request.setAttribute("tempRetorno","PreAnalisarVotoEmenta");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if (paginaAnterior == Configuracao.Editar) request.setAttribute("tempFluxo1", "1");
					else request.setAttribute("PaginaAtual", analiseConclusaoDt.getFluxo());
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
					boolean EhEmenta = (request.getParameter("ConsultaEmenta") != null && String.valueOf(request.getParameter("ConsultaEmenta")).trim().equalsIgnoreCase("S"));
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo" + (EhEmenta ? "Ementa" : ""));
					request.setAttribute("tempBuscaDescricao", "Modelo" + (EhEmenta ? "Ementa" : ""));
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "PreAnalisarVotoEmenta");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if(EhEmenta) request.getSession().setAttribute("EhEmenta", "S");
				}else{
					String stTemp = "";
					stTemp = Movimentacaone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), analiseConclusaoDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			default:
				//Controles da vari�vel tempFluxo1
				// 0 : Redireciona para passo 1 - Dados Pr�-An�lise
				// 1 : Redireciona para passo 2 - Pend�ncias a Gerar
				switch (tempFluxo1) {

					case 0:
						analiseConclusaoDt.setPasso1("Passo 1");
						analiseConclusaoDt.setPasso2("");
						analiseConclusaoDt.setPasso3("");
						break;

					case 1:
						analiseConclusaoDt.setPasso1("Passo 1 OK");
						analiseConclusaoDt.setPasso2("Passo 2");
						analiseConclusaoDt.setPasso3("");
						//Seta os tipos de pend�ncia que poder�o ser gerados
						analiseConclusaoDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
						stAcao = "/WEB-INF/jsptjgo/AnalisarVotoEmentaPendencias.jsp";
						break;

					default:
						//Refere-se a visualiza��o de uma pr�-analise j� finalizada
						if (fluxo == Configuracao.Curinga9) {
							analiseConclusaoDt = montarHistoricoPreAnalise(request, analiseConclusaoDt, UsuarioSessao, Movimentacaone);
							analiseConclusaoDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarPreAnaliseVotoEmentaFinalizada.jsp";
						}
				}
				break;
		}

		request.setAttribute("TextoEditor", analiseConclusaoDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", analiseConclusaoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", analiseConclusaoDt.getArquivoTipo());
		request.setAttribute("Modelo", analiseConclusaoDt.getModelo());
		
		request.setAttribute("TextoEditorEmenta", analiseConclusaoDt.getTextoEditorEmenta());
		request.setAttribute("Id_ArquivoTipoEmenta", analiseConclusaoDt.getId_ArquivoTipoEmenta());
		request.setAttribute("ArquivoTipoEmenta", analiseConclusaoDt.getArquivoTipoEmenta());
		request.setAttribute("ModeloEmenta", analiseConclusaoDt.getModeloEmenta());

		request.getSession().setAttribute("AnalisePendenciadt", analiseConclusaoDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);		
		
		//Caso o tipo de Movimenta��o escolhido n�o esteja configurado teremos que adicion�-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analiseConclusaoDt);

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
		if ((!analisePendenciaDt.getId_Modelo().equals("") || !analisePendenciaDt.getId_ModeloEmenta().equals("")) && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {			
			boolean EhEmenta = (request.getSession().getAttribute("EhEmenta") != null && String.valueOf(request.getSession().getAttribute("EhEmenta")).trim().equalsIgnoreCase("S"));
			
			ModeloDt modeloDt = null;			
			if (EhEmenta){
				modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_ModeloEmenta(), analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
				analisePendenciaDt.setId_ArquivoTipoEmenta(modeloDt.getId_ArquivoTipo());
				analisePendenciaDt.setArquivoTipoEmenta(modeloDt.getArquivoTipo());
				analisePendenciaDt.setTextoEditorEmenta(modeloDt.getTexto());
			} else {
				modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(), analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
				analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
				analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
				analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
			}			
			request.getSession().removeAttribute("EhEmenta"); 
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
	protected AnaliseConclusaoDt montarPreAnaliseExclusao(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{
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
			//Recupera dados da pr�-analise
			analisePendenciaDt = movimentacaoNe.getPreAnaliseConclusao(pendenciaArquivoDt);
			analisePendenciaDt.setPendenciaDt(pendencia);

			//Se h� uma pend�ncia espec�fica, trata-se de visualiza��o da pr�-analise
			analisePendenciaDt.addPendenciasFechar(pendencia);
			
			//Resgata processo ligado a pend�ncia
			ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendencia.getId_Processo());
			pendencia.setProcessoDt(processoDt);
			analisePendenciaDt.setProcessoDt(processoDt);
						
			setarPreAnalise(request, analisePendenciaDt, movimentacaoNe, usuarioSessao, id_Pendencia);
			
			if (analisePendenciaDt.getArquivoPreAnaliseEmenta() != null && analisePendenciaDt.getArquivoPreAnaliseEmenta().getId_Pendencia() != null && analisePendenciaDt.getArquivoPreAnaliseEmenta().getId_Pendencia().trim().length() > 0) {
				PendenciaDt pendenciaDtEmenta = movimentacaoNe.consultarPendenciaId(analisePendenciaDt.getArquivoPreAnaliseEmenta().getId_Pendencia());
				if (pendenciaDtEmenta != null) analisePendenciaDt.addPendenciasFechar(pendenciaDtEmenta);							
			}
		}
		return analisePendenciaDt;
	}
	
	protected void montarTelaPreAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, int fluxo, String tipoConclusao, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception{
		AudienciaProcessoDt audienciaProcesso = (new AudienciaProcessoNe()).buscarAudienciaProcessoPendentePorPendenciaOuProcesso(id_Pendencia, usuarioSessao); // jvosantos - 13/09/2019 14:45 - Corre��o busca de audiencia por pend�ncia e depois por processo
		montarTelaPreAnaliseSimples(request, analisePendenciaDt, id_Pendencia, audienciaProcesso, fluxo, tipoConclusao, movimentacaone, usuarioSessao);
	}
	/**
	 * Monta a tela de pr�-an�lise simples
	 * @throws Exception 
	 */
	protected void montarTelaPreAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, AudienciaProcessoDt audienciaProcesso, int fluxo, String tipoConclusao, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pend�ncia de conclus�o
		PendenciaDt pendenciaDt = movimentacaone.consultarPendenciaId(id_Pendencia);
		pendenciaDt.setHash(usuarioSessao.getCodigoHash(pendenciaDt.getId()));
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pend�ncia
		ProcessoDt processoDt = movimentacaone.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);
		analisePendenciaDt.setProcessoDt(processoDt);
		
		//Verifica se existe peticionamento com data posterior � conclus�o
		processoDt.setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaone, pendenciaDt));
		
		if (audienciaProcesso != null) {
			if (audienciaProcesso.isSessaoVirtual()) {
				//lrcampos 31/01/2020 10:02 - Seta classe do Processo.
				analisePendenciaDt.setProcessoTipoSessao(new ProcessoParteNe().consultaClasseProcessoIdAudiProc(audienciaProcesso.getId()));	
			} else {
				analisePendenciaDt.setId_ProcessoTipoSessao(audienciaProcesso.getId_ProcessoTipo());
				analisePendenciaDt.setProcessoTipoSessao(audienciaProcesso.getProcessoTipo());
			}
		}
		
		setarPreAnalise(request, analisePendenciaDt, movimentacaone, usuarioSessao, id_Pendencia);

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
	protected AnaliseConclusaoDt montarHistoricoPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{

		PendenciaDt pendencia = null;
		List listaFilhasConclusao = null;
		List arquivosResposta = null;
		PendenciaArquivoDt arquivoPreAnalise = null;
		String id_Pendencia = request.getParameter("Id_Pendencia");

		if (id_Pendencia != null) {
			if (analisePendenciaDt.getPendenciaDt() == null || !id_Pendencia.equalsIgnoreCase(analisePendenciaDt.getPendenciaDt().getId())) {

				pendencia = movimentacaone.consultarPendenciaId(id_Pendencia);

				//Consulta as pend�ncias filhas da pr�-analise selecionada do tipo Conclus�o
				listaFilhasConclusao = movimentacaone.consultarPendenciasFilhas(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pend�ncia, que ser�o o arquivo da pr�-analise e an�lise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaConclusao(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo n�o � assinado, � pr�-analise
						if (pendenciaArquivoDt.getArquivoDt().getUsuarioAssinador().equals("")) {
							//Sendo o arquivo de resposta a �ltima pr�-analise, busca ent�o o respons�vel
							movimentacaone.consultaResponsavelPreAnalise(pendenciaArquivoDt);
							arquivoPreAnalise = pendenciaArquivoDt;
						} else {
							pendencia.addListaArquivos(pendenciaArquivoDt);
							listaFilhasConclusao.add(pendencia);
						}
					}
				}

				if (arquivoPreAnalise != null) {
					//Recupera dados da pr�-analise que foi selecionada
					analisePendenciaDt = movimentacaone.getPreAnaliseConclusao(arquivoPreAnalise);
				}
				analisePendenciaDt.setHistoricoPendencia(listaFilhasConclusao);
				analisePendenciaDt.setPendenciaDt(pendencia);
			}
		}

		setarPreAnalise(request, analisePendenciaDt, movimentacaone, usuarioSessao, id_Pendencia);
		request.setAttribute("linkArquivo", "PendenciaArquivo");
		request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
		request.setAttribute("paginaArquivo", Configuracao.Curinga6);
		return analisePendenciaDt;
	}

	/**
	 * M�todo que ir� setar no request as informa��es da pr�-an�lise j� efetuada por um assistente
	 * para que o juiz possa finalizar
	 * @throws Exception 
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe Movimentacaone, UsuarioNe usuarioSessao, String id_Pendencia) throws Exception{
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
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
			
			if (analisePendenciaDt.getProcessoDt() != null) {
				String id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargo();
				if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) id_ServentiaCargo = usuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
				
				if (analisePendenciaDt != null && 
				    analisePendenciaDt.isVirtual() &&
					analisePendenciaDt.getPendenciaDt() != null &&
					analisePendenciaDt.getPendenciaDt().getId() != null && 
					analisePendenciaDt.getPendenciaDt().getId().trim().length() > 0) {
					id_Pendencia = analisePendenciaDt.getPendenciaDt().getId();
				}
				
				if (analisePendenciaDt.isVirtual()) {
					analisePendenciaDt.setArquivoPreAnaliseEmenta(Movimentacaone.consultarEmentaDesembargadorPorId(id_ServentiaCargo,  audienciaProcessoPendenciaNe.consultarPorIdPend(id_Pendencia)));			
				} else {
					analisePendenciaDt.setArquivoPreAnaliseEmenta(Movimentacaone.consultarEmentaDesembargador(id_ServentiaCargo, analisePendenciaDt.getProcessoDt().getId(), analisePendenciaDt.getId_ProcessoTipoSessao()));			
				}
				
				if (analisePendenciaDt.getArquivoPreAnaliseEmenta() != null) {
					PendenciaArquivoDt preAnaliseEmenta = analisePendenciaDt.getArquivoPreAnaliseEmenta();
					analisePendenciaDt.setTextoEditorEmenta(preAnaliseEmenta.getArquivoDt().getArquivo());
					analisePendenciaDt.setId_ArquivoTipoEmenta(preAnaliseEmenta.getArquivoDt().getId_ArquivoTipo());
					analisePendenciaDt.setArquivoTipoEmenta(preAnaliseEmenta.getArquivoDt().getArquivoTipo());
					analisePendenciaDt.setNomeArquivoEmenta(preAnaliseEmenta.getArquivoDt().getNomeArquivo());			
					
				}
			}			
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
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
			request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
		else
			request.setAttribute("podeAnalisar", "false");
		
		request.setAttribute("podeDescartar", usuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.ExcluirResultado));
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		request.setAttribute("id_Classificador", analisePendenciaDt.getId_Classificador());
		request.setAttribute("classificador", analisePendenciaDt.getClassificador());

		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
			return;
		}
		
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
			request.setAttribute("podeGerarVotoEmenta", "true");

		List tempList = movimentacaone.consultarConclusoesPendentes(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), analisePendenciaDt.getId_PendenciaTipo(), true, false, false);
		if (tempList.size() > 0) {
			request.setAttribute("ListaConclusao", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "N�o h� Votos / Ementas Pendentes.");
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
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
			request.setAttribute("podeGerarVotoEmenta", true);
			request.setAttribute("isDesembargador", true);
		}
		
		
		
		boolean ehIniciada = request.getParameter("sessaoIniciada") == null ? false : true;
		List tempList = null;
		if(ehIniciada) {
			//lrcampos 19/03/2020 15:20 - Incluindo filtro de serventia para sess�es virtuais iniciadas	
			String idServentiaFiltro = request.getParameter("serventia");
			tempList = movimentacaone.consultarPreAnalisesConclusaoSimplesVirtual(usuarioSessao, idServentiaFiltro, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), tipoConclusao, true, false, ehIniciada);		
		}
		else {
			tempList = movimentacaone.consultarPreAnalisesConclusaoSimples(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), tipoConclusao, true, false);
		}
		
		if (tempList.size() > 0) {
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
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					|| usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
				request.setAttribute("podeGerarVotoEmenta", "true");
			
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
			boRetorno = true;
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Nenhuma Pr�-An�lise Simples foi localizada.");
		}
		
		request.setAttribute("sessaoIniciada", ehIniciada);
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

	protected boolean existePeticionamentoPendente(UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe, PendenciaDt pendenciaDt) throws Exception{
		if (pendenciaDt == null) return false;
		if (pendenciaDt.getProcessoDt() == null) return false;
		return movimentacaoNe.existePeticionamentoPendente(usuarioSessao, pendenciaDt.getProcessoDt().getProcessoNumero(), pendenciaDt.getDataInicio(), pendenciaDt.getProcessoDt().getId_Serventia());
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

}
