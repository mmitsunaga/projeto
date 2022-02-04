package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AssinarConclusaoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet para controlar as assinaturas das pré-analises dos autos conclusos, após opção de "Guardar para assinar".
 * Suporte para pré-análise múltiplas.
 * 
 * @author mmgomes
 */
public class AssinarConclusaoCt extends Controle {
	
	private static final long serialVersionUID = -5433054083953184802L;
	
	private static final String CHAVE_SESSAO_CONCLUSOES_ASSINATURA = "CHAVE_SESSAO_CONCLUSOES_ASSINATURA";

	public int Permissao() {
		return AnaliseConclusaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AssinarConclusaoDt assinarConclusaoDt;
		MovimentacaoNe Movimentacaone;
		
		int paginaAnterior = 0;
		int passoEditar = -1;		
		String pendencias[] = null;
		String stAcao = "/WEB-INF/jsptjgo/AssinarConclusaoLocalizar.jsp";
		
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "Autos Conclusos");
		request.setAttribute("tempRetorno", "AssinarConclusao");
		
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
			
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");

		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		request.setAttribute("PassoEditar", passoEditar);

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		assinarConclusaoDt = (AssinarConclusaoDt) request.getSession().getAttribute("AssinarPendenciaDt");
		if (assinarConclusaoDt == null) assinarConclusaoDt = new AssinarConclusaoDt();
		
		assinarConclusaoDt.setClassificador(request.getParameter("Classificador"));
		assinarConclusaoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		assinarConclusaoDt.setTipoPendencia(request.getParameter("tipo"));
		assinarConclusaoDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		assinarConclusaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		assinarConclusaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		//quando a ação salvar for Curinga7 indica que será retirado da lista dos guardados para assinar 
		long acaoSalvar = 0; 
		if (request.getParameter("acaoSalvar") != null){
			acaoSalvar = Funcoes.StringToLong(String.valueOf(request.getParameter("acaoSalvar")));			
		}
		if (acaoSalvar> 0){
			assinarConclusaoDt.setAcaoAssinatura((acaoSalvar == Configuracao.Curinga6));
		}

		request.setAttribute("TituloPagina", "Assinar Autos Conclusos");
		
		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {		
		
			// Solicitar assinaturas ou confirmação de descarte de assinatura
			case Configuracao.Salvar:
				
				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE &&
					UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO &&
			    	UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA &&
			    	UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR &&
			    	UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao)) {
					
					// Captura as pendências que serão pré-analisadas
					if (request.getParameterValues("pendencias") != null){
						pendencias = request.getParameterValues("pendencias");
					}	else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")){
						pendencias = new String[] {request.getParameter("Id_Pendencia") };	
					}

					//Quando trata de despacho múltiplo
					if (pendencias != null) {
						assinarConclusaoDt.atualizeListaPendenciasSelecionadas(pendencias);
						request.setAttribute("ListaPreAnalises", assinarConclusaoDt.getListaPendenciaArquivo());
						if (assinarConclusaoDt.isAcaoAssinatura()){
							prepareDadosAssinatura(assinarConclusaoDt, Movimentacaone, request, UsuarioSessao, pendencias);					
							request.setAttribute("Mensagem", "Clique para confirmar a assinatura.");
							stAcao = "/WEB-INF/jsptjgo/AssinarConclusaoConfirmacao.jsp";
						}	
											
					}else{
						request.setAttribute("MensagemErro", "Nenhuma Conclusão foi selecionada.");					
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;					
					}
				} else {
					if (assinarConclusaoDt.isAcaoAssinatura())
						request.setAttribute("MensagemErro", "Usuário sem permissão para assinar.");
					else 
						request.setAttribute("MensagemErro", "Usuário sem permissão para retornar para pré-análise.");
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
					return;	
				}				

				break;

			// Confirmar assinatura ou descarta as pendências de assinatura
			case Configuracao.SalvarResultado:// vai chegar
				if (assinarConclusaoDt.isAcaoAssinatura()){
					if (!salvarAnaliseConclusao(assinarConclusaoDt, Movimentacaone, request, UsuarioSessao)){						
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;	
					}
				}else{
					// Captura as pendências que sairam do aguardando assinatura
					if (request.getParameterValues("pendencias") != null){
						pendencias = request.getParameterValues("pendencias");
					}	else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")){
						pendencias = new String[] {request.getParameter("Id_Pendencia") };	
					}
					if (pendencias != null) {
						assinarConclusaoDt.atualizeListaPendenciasSelecionadas(pendencias);
						Movimentacaone.descarteStatusPreAnalisesConclusaoAguardandoAssinatura(assinarConclusaoDt);
						request.setAttribute("MensagemOk", "Conclusão(ões) retirada(s) do aguardando assinatura com sucesso.");		
						//como foi poder retirar as conclusão do guardado para assinar direto, vou colocar a variavel __pedido__ na sessao
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido()); 
					}else{
						request.setAttribute("MensagemErro", "Nenhuma Conclusão foi selecionada.");					
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;					
					}
				
				}				
				consultarPreAnalisadasPendentesAssinatura(request, assinarConclusaoDt, UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/AssinarConclusaoLocalizar.jsp";
				break;

			// FUNÇÃO UTILIZADA PARA LOCALIZAR AUTOS CONCLUSOS PARA PRÉ-ANÁLISE
			// Assistente verá os autos conclusos para o ServentiaCargo que seu usuário chefe ocupa e que ainda não tenham uma pré-analise registrada
			case Configuracao.Localizar:
				//como foi poder retirar as conclusão do guardado para assinar direto, vou colocar a variavel __pedido__ na sessao
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido()); 	
				consultarPreAnalisadasPendentesAssinatura(request, assinarConclusaoDt, UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/AssinarConclusaoLocalizar.jsp";
				break;			

			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stTempRetorno = "AssinarConclusao?PaginaAtual=" + Configuracao.Localizar;
					if (paginaAnterior == Configuracao.Editar){
						stTempRetorno= "AssinarConclusao?PassoEditar=1";
					}	 
					
					String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", stTempRetorno, Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					
					break;
				} else{
					String stTemp="";
					stTemp = Movimentacaone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}	
			
			case Configuracao.Curinga6:{
				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE && UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO &&
				    	UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA && UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR &&
				    	UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao)) {
					
					// id da pendência e a senha do certificado
					String senhaCertificado = (String) request.getParameter("senha");
					String id_Pend = (String) request.getParameter("id_pendencia");
					boolean salvarSenha =  Boolean.parseBoolean(request.getParameter("salvarSenha"));
					
					AnaliseConclusaoDt analisePendenciaDt = new AnaliseConclusaoDt();
					analisePendenciaDt = Movimentacaone.getPreAnaliseConclusao(id_Pend);			
					
					if (analisePendenciaDt != null){
						setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());				
						preparaAnaliseSimplesNova(request, analisePendenciaDt, id_Pend, UsuarioSessao.getUsuarioDt(), Movimentacaone, UsuarioSessao);				
					} else{
						throw new MensagemException( "Conclusão selecionada não foi encontrada.");				
					}
					
					//limpa campos arquvivo DT
					analisePendenciaDt.getArquivoPreAnalise().getArquivoDt().setId("");
					analisePendenciaDt.getArquivoPreAnalise().getArquivoDt().setDataInsercao("");
					analisePendenciaDt.getArquivoPreAnalise().getArquivoDt().setContentType("text/html");
					
					//verifico se o certificado ja está carregado
					if(!UsuarioSessao.isCertificadoCarregado()){
						//se não carrego o certificado
						UsuarioSessao.carregarCertificado();
					} 
					
					//nesse ponto o certificado já foi carregado pefeitamente, ou uma exception foi lançada
					if (senhaCertificado != null &&  !senhaCertificado.isEmpty()) {
						UsuarioSessao.setSenhaCertificado(senhaCertificado);
					}
					//assino o arquivo e incluo o assinante e preparo o .p7s
					UsuarioSessao.assinarByte(analisePendenciaDt.getArquivoPreAnalise().getArquivoDt());
					//se não é para salvar o cetificado eu limpo
					if (!salvarSenha) {
						UsuarioSessao.setSenhaCertificado("");
					}
					
					ArrayList<ArquivoDt> listaArquivos = new ArrayList<ArquivoDt>();
					listaArquivos.add(analisePendenciaDt.getArquivoPreAnalise().getArquivoDt());
					analisePendenciaDt.setListaArquivos(listaArquivos);
					
					salvarPrimeiraAnaliseConclusaoListaAssincrona(analisePendenciaDt, Movimentacaone, request, UsuarioSessao, response);
					
				} else {
					if (assinarConclusaoDt.isAcaoAssinatura()){
						throw new MensagemException( "Usuário sem permissão para assinar.");	
					} else {
						throw new MensagemException( "Usuário sem permissão para retornar para pré-análise.");
					}
				}		
				return;				
			}
			
			case Configuracao.Curinga7:{
				//Volta para a página inicial						
				redireciona(response, "Usuario?PaginaAtual=" + Configuracao.Cancelar);// + "&MensagemOk=" + "Processo de Assinatura Finalizado.");
				return;
			}
		}

		request.getSession().setAttribute("AssinarPendenciaDt", assinarConclusaoDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		request.setAttribute("numeroProcesso", assinarConclusaoDt.getNumeroProcesso());
		request.setAttribute("id_Classificador", assinarConclusaoDt.getId_Classificador());
		request.setAttribute("classificador", assinarConclusaoDt.getClassificador());
		
		if (!(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE &&
			  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO &&
		      UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA &&
		      UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR &&
		      UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao))) {
			stAcao = "/WEB-INF/jsptjgo/AssinarConclusaoLocalizarAssessor.jsp";
		}
		
		request.getSession().setAttribute("__Pedido__", UsuarioSessao.getPedido());
		request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	
	/**
	 * Realiza chamada ao método para consultar as pré-analises simples de um usuário pendentes de assinatura
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisadasPendentesAssinatura(HttpServletRequest request, AssinarConclusaoDt assinarConclusaoDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		//Verifica se usuário digitou "." e dígito verificador
		if (assinarConclusaoDt.getNumeroProcesso().length() > 0 && assinarConclusaoDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return boRetorno;
		}

		request.setAttribute("id_Classificador", assinarConclusaoDt.getId_Classificador());
		request.setAttribute("classificador", assinarConclusaoDt.getClassificador());
		request.setAttribute("numeroProcesso", assinarConclusaoDt.getNumeroProcesso());
		
		assinarConclusaoDt.setListaPendenciaArquivo(movimentacaone.consultarPreAnalisesConclusaoSimplesPendentesAssinatura(usuarioSessao.getUsuarioDt(), assinarConclusaoDt.getNumeroProcesso(), assinarConclusaoDt.getId_Classificador(), assinarConclusaoDt.getTipoPendencia()));
		if (assinarConclusaoDt.getListaPendenciaArquivo().size() > 0) {
			
			//Atualiza o rash para consulta
			for (Object pendenciaArquivoObj : assinarConclusaoDt.getListaPendenciaArquivo()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)pendenciaArquivoObj;
				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
			
			request.setAttribute("ListaPreAnalises", assinarConclusaoDt.getListaPendenciaArquivo());
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao));
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
			boRetorno = true;
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemOk", "Todas as pré-análises foram assinadas, e não existem novas pré-análises aguardando assinatura.");
		}
		
		assinarConclusaoDt.limparProcesso();

		return boRetorno;
	}
	
	
	/**
	 * Prepara estrutura com os nomes dos arquivos e o conteúdo dos mesmos para serem assinados pelo applet, 
	 * e prepara também uma estruta map no Dt para que seja possível vincular os processos assinados com as preanálises.
	 * 
	 * Exemplo:
	 * Processo: 5000.14 / Nome do arquivo: textoonline.html / Conteúdo: Teste análise 
	 * Processo: 5002.29 / Nome do arquivo: textoonline.html / Conteúdo: Teste análise arquivo 2
	 *           
	 * nomeArquivos = 5000.14 : textoonline.html__@---5002.29 : textoonline.html
	 * conteudoArquivos = Teste análise__@---Teste análise arquivo 2  
	 * 
	 * @param assinarConclusaoDt
	 * @param Movimentacaone
	 * @throws Exception 
	 */
	protected void prepareDadosAssinatura(AssinarConclusaoDt assinarConclusaoDt, MovimentacaoNe Movimentacaone, HttpServletRequest request, UsuarioNe UsuarioSessao, String[] pendencias) throws Exception{
		
		String id_Pendencia;
		AnaliseConclusaoDt analisePendenciaDt;
		int fluxo = 2;
		List listaAnalisePendencia = new ArrayList();
		
		for (int i = 0; i < pendencias.length; i++) {			
			id_Pendencia = (String) pendencias[i];
			analisePendenciaDt = null;			
			analisePendenciaDt = Movimentacaone.getPreAnaliseConclusao(id_Pendencia);			
			if (analisePendenciaDt != null){
				setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());				
				preparaAnaliseSimples(request, analisePendenciaDt, id_Pendencia, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, analisePendenciaDt.isPreAnalise(), assinarConclusaoDt.getTipoPendencia(), UsuarioSessao);				
				listaAnalisePendencia.add(analisePendenciaDt);
			}		
		}
		
		assinarConclusaoDt.prepareDadosAssinatura(listaAnalisePendencia);
		//mfssilva 03/12/2019 - Correção para funcionar com o assinador novo
		request.setAttribute("conteudoArquivos", assinarConclusaoDt.getConteudoArquivos());
		
		// Limpa lista DWR
		limparListas(request);		
		
	}
	
	/**
	 * Método que irá setar no request as informações da pré-análise já efetuada por um assistente
	 * para que o juiz possa finalizar
	 * @param request
	 * @param analisePendenciaDt
	 * @param movimentacaoNe
	 * @param usuarioDt
	 * @throws Exception
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioDt usuarioDt){

		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			PendenciaArquivoDt preAnalise = analisePendenciaDt.getArquivoPreAnalise();
			analisePendenciaDt.setTextoEditor(preAnalise.getArquivoDt().getArquivo());
			analisePendenciaDt.setId_ArquivoTipo(preAnalise.getArquivoDt().getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(preAnalise.getArquivoDt().getArquivoTipo());
			analisePendenciaDt.setNomeArquivo(preAnalise.getArquivoDt().getNomeArquivo());
			analisePendenciaDt.setDataPreAnalise(preAnalise.getArquivoDt().getDataInsercao());

			//Se pré-análise foi feita por assistente
			if (preAnalise.getAssistenteResponsavel() != null && !preAnalise.getAssistenteResponsavel().equals("")) {
				analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getAssistenteResponsavel());
			} 	else{
				analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getJuizResponsavel());
			}
		}
	}
	
	/**
	 * Prepara Análise Simples
	 * 
	 * @param request
	 * @param analisePendenciaDt
	 * @param id_Pendencia
	 * @param usuarioDt
	 * @param movimentacaoNe
	 * @param fluxo
	 * @param fluxoPreAnalise
	 * @param tipoConclusao
	 * @param usuarioSessao
	 * @throws Exception
	 */
	protected void preparaAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoConclusao, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pendência de conclusão
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);	
		
		//Seta tipos de pendências que poderão ser geradas
		analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt));

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoConclusao);
	}
	
	
	/**
	 * Prepara Análise Simples
	 * 
	 * @param request
	 * @param analisePendenciaDt
	 * @param id_Pendencia
	 * @param usuarioDt
	 * @param movimentacaoNe
	 * @param fluxo
	 * @param fluxoPreAnalise
	 * @param tipoConclusao
	 * @param usuarioSessao
	 * @throws Exception
	 */
	protected void preparaAnaliseSimplesNova(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pendência de conclusão
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);	
		
		//Seta tipos de pendências que poderão ser geradas
		analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt));
	}
	
	/**
	 * Armazena fluxo padrão.
	 * 
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
	 * Vincula os arquivos assinados da sessão (inseridas no ct AssinarConclusaoCtDwr) na preanalise e salva a análise conclusão.
	 * 
	 * @param assinarConclusaoDt
	 * @param Movimentacaone
	 * @param request
	 * @param UsuarioSessao
	 * @return
	 * @throws Exception
	 */
	protected boolean salvarAnaliseConclusao(AssinarConclusaoDt assinarConclusaoDt, MovimentacaoNe Movimentacaone, HttpServletRequest request, UsuarioNe UsuarioSessao) throws Exception{
		List listaAnalisePendencia = null;
		String Mensagem = "";
		String MensagemRetorno = "";
		
		//mfssilva 03/12/2019 - Correção para funcionar com o assinador novo		
		listaAnalisePendencia = assinarConclusaoDt.getListaAnalisePendencia();
		for(int i = 0 ; i< listaAnalisePendencia.size();i++) {
			AnaliseConclusaoDt analisePendenciaDt = (AnaliseConclusaoDt)listaAnalisePendencia.get(i);
	
			Mensagem = Movimentacaone.verificarAnaliseConclusao(analisePendenciaDt, UsuarioSessao);
			if (Mensagem.length() == 0) {
				Mensagem = Movimentacaone.podeAnalisarConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo());
				if (Mensagem.length() == 0) {
					analisePendenciaDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
					analisePendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					Movimentacaone.salvarAnaliseConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt());												
				} 
			}			
			if (Mensagem.trim().length() > 0){
				if (MensagemRetorno.trim().length() == 0) MensagemRetorno = "Não foi possível assinar a análise dos seguintes processos:";				
				MensagemRetorno += "\nProcesso: " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()) + " - Motivo: " + Mensagem;
			}
			analisePendenciaDt.limpar();
			Mensagem = "";
			
			//Thread.sleep(5000);	
		}
		
		limparListas(request);
		
		if (MensagemRetorno.trim().length() == 0) request.setAttribute("MensagemOk", "Assinatura efetuada com sucesso.");		
		else request.setAttribute("MensagemErro", MensagemRetorno);
		
		return true;
	}
	
	/**
	 * Vincula os arquivos assinados da sessão (inseridas no ct AssinarConclusaoCtDwr) na preanalise e prepara a análise conclusão.
	 * 
	 * @param assinarConclusaoDt
	 * @param request
	 * @throws Exception
	 */
	protected void prepararAnaliseConclusaoAssincrona(AssinarConclusaoDt assinarConclusaoDt, HttpServletRequest request, HttpServletResponse response) throws Exception{
		List listaAnalisePendencia = null;
		String mensagem = "";
		int quantidade = 0;
		String sucesso = "true";
		
		Map MapArquivosAssinados = (Map) request.getSession().getAttribute("MapArquivosAssinados");
		
		if (MapArquivosAssinados == null || MapArquivosAssinados.isEmpty()){
			mensagem = "É necessário assinar o(s) arquivo(s).";
			sucesso = "false";
		} else {
			assinarConclusaoDt.atualizeArquivosAssinados(MapArquivosAssinados);
			
			listaAnalisePendencia = assinarConclusaoDt.getListaAnalisePendencia();
			
			request.getSession().setAttribute(CHAVE_SESSAO_CONCLUSOES_ASSINATURA, listaAnalisePendencia);
			
			quantidade = listaAnalisePendencia.size();
			
			limparListas(request);
			
			//Thread.sleep(5000);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mensagem", mensagem);
		jsonObject.put("sucesso", sucesso);
		jsonObject.put("quantidade", quantidade);
		montaRetornoJSON(response, jsonObject.toString()); 
	}
	
	/**
	 * salva a primeira análise conclusão da lista.
	 * 
	 * @param Movimentacaone
	 * @param request
	 * @param UsuarioSessao
	 * @return
	 * @throws Exception
	 */
	protected void salvarPrimeiraAnaliseConclusaoListaAssincrona(AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe Movimentacaone, HttpServletRequest request, UsuarioNe UsuarioSessao, HttpServletResponse response) throws Exception{
		String mensagem = Movimentacaone.podeAnalisarConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo());
		if (mensagem.length() == 0) {
			analisePendenciaDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
			analisePendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			Movimentacaone.salvarAnaliseConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt());												
		} else {
			mensagem = "Processo: " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()) + " - Motivo: " + mensagem;
			throw new MensagemException(mensagem);
			
		}
	}
	
	/**
	 * Zera listas de arquivos da sessão
	 * @param request
	 */
	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("MapArquivosAssinados");
		request.getSession().removeAttribute("Id_MapArquivosAssinadosDwr");		
	}

}
