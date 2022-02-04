package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para controlar as an�lises de autos conclusos do tipo Voto / Ementa. 
 * 
 * @author mmgomes
 */
public class AnalisarVotoEmentaCt extends Controle {

    private static final long serialVersionUID = 8652459075296978892L;

    public int Permissao() {
		return PreAnaliseConclusaoDt.CodigoPermissao;
	}

	public AnalisarVotoEmentaCt() {
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AnaliseConclusaoDt analisePendenciaDt;
		MovimentacaoNe Movimentacaone;
    	
		//-Vari�veis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String Mensagem = "";
		int paginaAnterior = 0;
		int passoEditar = -1;
		String pendencias[] = null;
		String stAcao = "/WEB-INF/jsptjgo/AnalisarVotoEmenta.jsp";
		request.setAttribute("tempPrograma", "Voto Ementa");
		request.setAttribute("tempRetorno", "AnalisarVotoEmenta");
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		analisePendenciaDt = (AnaliseConclusaoDt) request.getSession().getAttribute("AnalisePendenciadt");
		if (analisePendenciaDt == null) analisePendenciaDt = new AnaliseConclusaoDt();

		analisePendenciaDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		analisePendenciaDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		analisePendenciaDt.setComplementoMovimentacao(request.getParameter("MovimentacaoComplemento"));
		analisePendenciaDt.setClassificador(request.getParameter("Classificador"));
		analisePendenciaDt.setId_Classificador(request.getParameter("Id_Classificador"));
		analisePendenciaDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		analisePendenciaDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		analisePendenciaDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		analisePendenciaDt.setId_Modelo(request.getParameter("Id_Modelo"));
		analisePendenciaDt.setModelo(request.getParameter("Modelo"));
		analisePendenciaDt.setTextoEditor(request.getParameter("TextoEditor"));
		
		analisePendenciaDt.setId_TipoPendencia(request.getParameter("tipo"));
		
		analisePendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		analisePendenciaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if (request.getParameter("julgadoMerito") == null && request.getParameter("fluxo") != null && request.getParameter("fluxo").toString().equals("1"))
			analisePendenciaDt.setJulgadoMeritoProcessoPrincipal("false");
		else if (request.getParameter("julgadoMerito") != null)
			analisePendenciaDt.setJulgadoMeritoProcessoPrincipal(request.getParameter("julgadoMerito"));

		//if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Analisar M�ltiplos Autos Conclusos");
		//else 
		request.setAttribute("TituloPagina", "Analisar Voto Ementa");

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		configuraParametrosAuxiliares(request, analisePendenciaDt, paginaatual, paginaAnterior, passoEditar, UsuarioSessao, Movimentacaone);

		int fluxo = analisePendenciaDt.getFluxo();
		String tipoConclusao = analisePendenciaDt.getId_PendenciaTipo();
		boolean fluxoPreAnalise = analisePendenciaDt.isPreAnalise();
		
		//--------------------------------------------------------------------------------------------------------------------------------------
		switch (paginaatual) {

			// Inicializa an�lise de autos conclusos
			case Configuracao.Novo:			

				// Captura as pend�ncias que ser�o fechadas
				if (request.getParameter("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				if (pendencias != null && pendencias.length > 0) {
					if (pendencias.length > 1) {//An�lise m�ltipla
						analisePendenciaDt = new AnaliseConclusaoDt();
						//montarTelaAnaliseMultipla(request, analisePendenciaDt, pendencias, null, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoConclusao, UsuarioSessao);
						request.setAttribute("MensagemErro", "A An�lise do Voto / Ementa dever� ser realizada individualmente.");
					} else {
						//Verifica se existe uma pr�-analise para a conclus�o selecionada
						analisePendenciaDt = Movimentacaone.getPreAnaliseConclusao(pendencias[0]);

						if (analisePendenciaDt == null) analisePendenciaDt = new AnaliseConclusaoDt();
						else setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());

						//Montar tela pr�-analise simples
						montarTelaAnaliseSimples(request, analisePendenciaDt, pendencias[0], UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoConclusao, UsuarioSessao);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhum Voto / Ementa foi selecionado.");
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				break;

			case Configuracao.Salvar:
				stAcao = "/WEB-INF/jsptjgo/AnalisarVotoEmentaConfirmacao.jsp";
				analisePendenciaDt.setListaPendenciasGerar(getListaPendencias(request)); // Captura lista de pend�ncias
				analisePendenciaDt.setListaArquivos(getListaArquivos(request)); // Captura lista de arquivos
				analisePendenciaDt.setPasso2("Passo 2 OK");
				analisePendenciaDt.setPasso3("Passo 3");
				break;

			// Salvando An�lise de Conclus�es
			case Configuracao.SalvarResultado:
				Mensagem = Movimentacaone.verificarAnaliseConclusao(analisePendenciaDt, UsuarioSessao);
				if (Mensagem.length() == 0) {
					Mensagem = Movimentacaone.podeAnalisarConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo());
					if (Mensagem.length() == 0) {
						Movimentacaone.salvarAnaliseConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt());

						//Quando trata de despacho m�ltiplo
						if (analisePendenciaDt.isMultipla()) Mensagem = "An�lise M�ltipla registrada com sucesso.";
						else Mensagem = "An�lise efetuada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar());

						request.setAttribute("MensagemOk", Mensagem);
						// Limpa da sess�o os atributos
						analisePendenciaDt.limpar();
						limparListas(request);

						if (analisePendenciaDt.isPreAnalise()) 
							redireciona(response, "PreAnalisarVotoEmenta?PaginaAtual=" + fluxo + "&MensagemOk=" + Mensagem);
						else 
							this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;
					} else request.setAttribute("MensagemErro", Mensagem);
				} else request.setAttribute("MensagemErro", Mensagem);
				break;
			
			//Prepara dados para Descartar An�lise
			case Configuracao.Excluir:
				PendenciaDt pendenciaDt = Movimentacaone.consultarPendenciaId(request.getParameter("Id_Pendencia"));
				analisePendenciaDt.setPendenciaDt(pendenciaDt);
				request.setAttribute("Mensagem", "Deseja descartar Voto Ementa?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPendenciaDescartar.jsp";
				break;

			//Fun��o utilizada para Descartar An�lise
			case Configuracao.ExcluirResultado:
				Movimentacaone.descartarAnalise(analisePendenciaDt.getPendenciaDt(), UsuarioSessao.getUsuarioDt());
				request.setAttribute("MensagemOk", "Voto Ementa descartado com sucesso.");

				// Limpa da sess�o os atributos
				analisePendenciaDt.limpar();

				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

			// Localiza autos conclusos para an�lise
			case Configuracao.Localizar:				
				consultarConclusao(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
				stAcao = "/WEB-INF/jsptjgo/VotoEmentaLocalizar.jsp";
				analisePendenciaDt.setFluxo(paginaatual);
				analisePendenciaDt.limpar();				
				break;
				
			//Consulta de conclus�es finalizadas
			case Configuracao.Curinga7:
				//Se fluxo anterior � esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga7) consultarConclusoesFinalizadas(request, analisePendenciaDt, UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), Movimentacaone);
				else {
					analisePendenciaDt = new AnaliseConclusaoDt();
					analisePendenciaDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/VotosEmentasFinalizadas.jsp";
				break;

			// Consultar Tipos de Movimenta��o
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"Tipo de Movimenta��o"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPemissao = String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_MovimentacaoTipo", "MovimentacaoTipo", "Tipo de Movimenta��o", "AnalisarVotoEmenta", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					
				}else{
					String stTemp = "";
					stTemp = Movimentacaone.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), tempNomeBusca, PosicaoPaginaAtual);
					
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
					request.setAttribute("tempRetorno","AnalisarVotoEmenta");		
					request.setAttribute("tempDescricaoId","Id");
					//Esse valor 2 parao par�metro consultaClassificador � para indicar que est� sendo feita uma an�lise e o
					//usu�rio est� na tela de localizar as an�lises a serem realizadas
					if(request.getParameter("consultaClassificador") != null && request.getParameter("consultaClassificador").equals("2")) {
						request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Localizar));
					} else {
						request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
						if (paginaAnterior == Configuracao.Editar) {
							request.setAttribute("PassoEditar", "1");
						}
						else {
							request.setAttribute("PaginaAtual", analisePendenciaDt.getFluxo());
						}
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
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","AnalisarVotoEmenta");		
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
				if (consultarModelo(request, tempNomeBusca, PosicaoPaginaAtual, Movimentacaone, analisePendenciaDt, paginaatual, UsuarioSessao)) {
					stAcao = "/WEB-INF/jsptjgo/ModeloArquivoTipoLocalizar.jsp";
				}
				break;

			default:
				//Controles da vari�vel passoEditar
				// 0 : Redireciona para passo 1 - Dados An�lise
				// 1 : Redireciona para passo 2 - Pend�ncias a Gerar
				switch (passoEditar) {

					case 0:
						analisePendenciaDt.setPasso1("Passo 1");
						analisePendenciaDt.setPasso2("");
						analisePendenciaDt.setPasso3("");
						break;

					case 1:
						analisePendenciaDt.setPasso1("Passo 1 OK");
						analisePendenciaDt.setPasso2("Passo 2");
						analisePendenciaDt.setPasso3("");
						stAcao = "/WEB-INF/jsptjgo/AnalisarVotoEmentaPendencias.jsp";

						break;

					default:
						//Refere-se a visualiza��o de uma analise j� finalizada
						if (!analisePendenciaDt.isPreAnalise() && fluxo == Configuracao.Curinga7) {
							analisePendenciaDt = montarAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
							analisePendenciaDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarAnaliseVotoEmenta.jsp";
						}
				}
				break;
		}

		//Seta vari�veis auxiliares
		request.setAttribute("Id_ArquivoTipo", analisePendenciaDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", analisePendenciaDt.getArquivoTipo());
		request.setAttribute("nomeArquivo", analisePendenciaDt.getNomeArquivo());
		request.setAttribute("Modelo", analisePendenciaDt.getModelo());
		request.setAttribute("TextoEditor", analisePendenciaDt.getTextoEditor());

		request.getSession().setAttribute("AnalisePendenciadt", analisePendenciaDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);		
		
		//Caso o tipo de Movimenta��o escolhido n�o esteja configurado teremos que adicion�-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analisePendenciaDt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Retorna todos os dados de uma an�lise efetuada a serem exibidos na tela:
	 * dados an�lise, arquivos que foram inseridos pelo juiz, e todo o hist�rico de pr�-analises registradas
	 * @throws Exception 
	 */
	protected AnaliseConclusaoDt montarAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{

		List arquivosResposta = null;
		List historico = null;
		PendenciaDt pendencia = null;

		String id_Pendencia = request.getParameter("Id_Pendencia");

		if (id_Pendencia != null) {
			if (analisePendenciaDt.getPendenciaDt() == null || !id_Pendencia.equalsIgnoreCase(analisePendenciaDt.getPendenciaDt().getId())) {

				pendencia = movimentacaone.consultarPendenciaId(id_Pendencia);

				//Pesquisa as pr�-analises anteriores registradas para a pend�ncia
				historico = movimentacaone.consultarPreAnalisesConclusaoAnteriores(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pend�ncia, que ser�o o arquivo da an�lise e da pr�-analise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaConclusao(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo n�o � assinado, � pr�-analise
						if (pendenciaArquivoDt.getArquivoDt().getUsuarioAssinador().equals("")) {
							//Sendo o arquivo de resposta a �ltima pr�-analise, busca ent�o o respons�vel
							movimentacaone.consultaResponsavelPreAnalise(pendenciaArquivoDt);
							historico.add(0, pendenciaArquivoDt);
						} else pendencia.addListaArquivos(pendenciaArquivoDt);

					}
				}
				analisePendenciaDt.setHistoricoPendencia(historico);
				analisePendenciaDt.setListaPendenciasGerar(movimentacaone.consultarFilhasPendencia(pendencia));
				analisePendenciaDt.setPendenciaDt(pendencia);
			}
		}

		request.setAttribute("linkArquivo", "PendenciaArquivo");
		request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
		request.setAttribute("paginaArquivo", Configuracao.Curinga6);
		return analisePendenciaDt;
	}

	/**
	 * Seta vari�veis necess�rias para redirecionamento para telas corretas
	 * @param analisePendenciaDt
	 * @param fluxo
	 * @param fluxoPreAnalise
	 * @param tipoConclusao
	 */
	protected void setFluxoRedirecionamento(AnaliseConclusaoDt analisePendenciaDt, int fluxo, boolean fluxoPreAnalise, String tipoConclusao) {
		analisePendenciaDt.setFluxo(fluxo);
		analisePendenciaDt.setPreAnalise(fluxoPreAnalise);
		analisePendenciaDt.setId_TipoPendencia(tipoConclusao);
	}

	/**
	 * Zera listas de arquivos e pend�ncias
	 * @param request
	 */
	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pend�ncias
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}
	
	/**
	 * M�todo que ir� setar no request as informa��es da pr�-an�lise j� efetuada por um assistente
	 * para que o juiz possa finalizar
	 * @param request
	 * @param movimentacaodt
	 * @param id_PendenciaFechar 
	 * @throws Exception 
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioDt usuarioDt){

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
	 * Monta tela para an�lise de conclus�o (�nico)
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoConclusao, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pend�ncia de conclus�o
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pend�ncia
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);	
		
		//Verifica se existe peticionamento com data posterior � conclus�o
		processoDt.setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaoNe, pendenciaDt));

		//Seta tipos de pend�ncias que poder�o ser geradas
		analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt));

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoConclusao);
		
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
		
		// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
		analisePendenciaDt.setListaTiposMovimentacaoConfigurado(movimentacaoNe.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(usuarioSessao.getUsuarioDt()));
	}

	/**
	 * Consulta autos conclusos e verifica se usu�rio ver� todas as conclus�es ou somente aquelas que n�o possuem pr�-analise.
	 * Se usu�rio tem permiss�o para analisar, poder� ver todas.
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void consultarConclusao(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao, int passoEditar, int fluxo) throws Exception{
		if (passoEditar == 0) {//Significa que foi acionado Bot�o Limpar
			analisePendenciaDt.setNumeroProcesso("null");
			analisePendenciaDt.setId_Classificador("null");
		}

		//Verifica se usu�rio pode analisar
		request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("podeDescartar", usuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.ExcluirResultado));
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		request.setAttribute("id_Classificador", analisePendenciaDt.getId_Classificador());
		request.setAttribute("classificador", analisePendenciaDt.getClassificador());
		request.setAttribute("isDesembargador", usuarioSessao.isDesembargador());
		if(Funcoes.StringToBoolean(request.getParameter("virtual"))){
			request.setAttribute("virtual", true);
		}

		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)
			request.setAttribute("podeGerarVotoEmenta", "true");
		
		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
			return;
		}
		
		boolean ehIniciada = request.getAttribute("sessaoIniciada") == null ? false : true;

			List tempList = movimentacaone.consultarConclusoesPendentes(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), analisePendenciaDt.getId_PendenciaTipo(), true, false, ehIniciada);
			if (tempList.size() > 0) {
				request.setAttribute("ListaConclusao", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
			} else {
				if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "N�o h� Votos / Ementas Pendentes.");
			}
			
		request.setAttribute("sessaoIniciada", ehIniciada);

	}

	/**
	 * Consulta conclus�es finalizadas
	 * @throws Exception 
	 */
	protected boolean consultarConclusoesFinalizadas(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_ServentiaCargo, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usu�rio digitou "." e d�gito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarConclusoesFinalizadas(id_ServentiaCargo, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal());
			if (tempList.size() > 0) {
				request.setAttribute("ListaConclusoes", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhum Voto / Ementa Localizado.");
		} else request.setAttribute("MensagemErro", "Informe par�metros para consulta: Data Inicial e Data Final ou N�mero do Processo.");
		return boRetorno;
	}

	/**
	 * Recupera lista de arquivos inseridas usando DWR e converte de Map para List
	 */
	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Recupera lista de pend�ncias inseridas usando DWR e converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);

		return lista;
	}

	/**
	 * Consulta de modelos Se n�o encontrar nenhum modelo retorna false
	 * @throws Exception 
	 */
	protected boolean consultarModelo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, MovimentacaoNe movimentacaone, AnaliseConclusaoDt analisePendenciaDt, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
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
	 * Seta parametros necessarios
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void configuraParametrosAuxiliares(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, int paginaAtual, int paginaAnterior, int passoEditar, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{

		//Captura par�metros consulta		
		if (request.getParameter("numeroProcesso") != null) analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("classificador") != null) analisePendenciaDt.setClassificador(request.getParameter("classificador"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));
		if (request.getParameter("preAnalise") != null && request.getParameter("preAnalise").equals("true")) analisePendenciaDt.setPreAnalise(true);
		if (request.getParameter("preAnaliseConclusao") != null && request.getParameter("preAnaliseConclusao").equals("true")) analisePendenciaDt.setPreAnalise(true);
		// Quando modelo foi selecionado monta conte�do para aparecer no editor e j� carrego o tipo do arquivo
		if (!analisePendenciaDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(), analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
			analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
			analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
		}
		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		request.setAttribute("PassoEditar", passoEditar);

		//Tratamento mensagem de Confirma��o de An�lise partindo de uma pr�-analise simples
		//Essa mensagem pode vir redirecionada da servlet AnalisarVotoEmentaCt e vir� como parameter
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		
		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

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
