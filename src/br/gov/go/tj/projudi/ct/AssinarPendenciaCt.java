package br.gov.go.tj.projudi.ct;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AssinarPendenciaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

/**
 * Servlet para controlar as assinaturas das pré-analises das pendências, após opção de "Guardar para assinar".
 * Suporte para pré-análise múltiplas.
 * 
 * @author mmgomes
 */
public class AssinarPendenciaCt extends Controle {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5343927085229320175L;

	public int Permissao() {
		return AnalisePendenciaDt.CodigoPermissao;
	}

	public AssinarPendenciaCt() {
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AssinarPendenciaDt assinarPendenciaDt;
		MovimentacaoNe Movimentacaone;
		
		int passoEditar = -1;		
		String pendencias[] = null;
		String stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaLocalizar.jsp";

		request.setAttribute("tempPrograma", "Assinar Pendência");
		request.setAttribute("tempRetorno", "AssinarPendencia");
		
		if (request.getParameter("PaginaAnterior") != null) {
		}
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

		assinarPendenciaDt = (AssinarPendenciaDt) request.getSession().getAttribute("AssinarPendenciaDt");
		if (assinarPendenciaDt == null) assinarPendenciaDt = new AssinarPendenciaDt();
		
		assinarPendenciaDt.setTipoPendencia(request.getParameter("tipo"));
		assinarPendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		assinarPendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		assinarPendenciaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		long acaoSalvar = 0; 
		if (request.getParameter("acaoSalvar") != null) acaoSalvar = Funcoes.StringToLong(String.valueOf(request.getParameter("acaoSalvar")));			
		if (acaoSalvar> 0)	assinarPendenciaDt.setAcaoAssinatura((acaoSalvar == Configuracao.Curinga6));

		request.setAttribute("TituloPagina", "Assinar Pendências");
		
		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {		
		
			// Solicitar assinaturas ou confirmação de descarte de assinatura
			case Configuracao.Salvar:
				if (!UsuarioSessao.isAssessorJuizVaraTurma() &&
		    	    !UsuarioSessao.isAssessorAdvogado() &&
		    	    !UsuarioSessao.isAssessorMP() &&
		    	    !UsuarioSessao.isAssessor() &&
		    	    !UsuarioSessao.isAssessorPresidenteSegundoGrau()  &&
		    	    !UsuarioSessao.isAssessorDesembargador() &&
		    	    UsuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao)) {
				
					// Captura as pendências que serão pré-analisadas
					if (request.getParameterValues("pendencias") != null) pendencias = request.getParameterValues("pendencias");
					else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };	

					//Quando trata de analise múltipla
					if (pendencias != null) {
 						assinarPendenciaDt.atualizeListaPendenciasSelecionadas(pendencias);
						request.setAttribute("ListaPreAnalises", assinarPendenciaDt.getListaPendenciaArquivo());
						if (assinarPendenciaDt.isAcaoAssinatura()){
							prepareDadosAssinatura(assinarPendenciaDt, Movimentacaone, request, UsuarioSessao, pendencias);					
							request.setAttribute("Mensagem", "Clique para confirmar a assinatura.");
							stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaConfirmacao.jsp";
						}else{
							request.setAttribute("Mensagem", "Clique para confirmar o descarte do status 'Aguardando assinatura', alterando para o status 'Pré-analisadas'.");
							stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaDescarteConfirmacao.jsp";
						}				
											
					}else{
						request.setAttribute("MensagemErro", "Nenhuma pendência foi selecionada.");					
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;					
					}
					
				} else {					
					if (assinarPendenciaDt.isAcaoAssinatura())
						request.setAttribute("MensagemErro", "Usuário sem permissão para assinar.");
					else 
						request.setAttribute("MensagemErro", "Usuário sem permissão para retornar para pré-análise.");
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}				
				break;

			// Confirmar assinatura ou descarta as pendências de assinatura
			case Configuracao.SalvarResultado:				
				boolean houveAssinatura = false;
				boolean ehParaImprimir = false;
				if (assinarPendenciaDt.isAcaoAssinatura()){
					if (!salvarAnalisePendencia(assinarPendenciaDt, Movimentacaone, request, UsuarioSessao)){						
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;	
					}
					houveAssinatura = true;
					ehParaImprimir = assinarPendenciaDt.isEhParaImprimir();
				}else{
					Movimentacaone.descarteStatusPreAnalisesAguardandoAssinatura(assinarPendenciaDt);
					request.setAttribute("MensagemOk", "Descarte efetuado com sucesso.");
				}
				
				if (request.getParameter("MensagemOk")!= null)
					request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
				
				consultarPreAnalisadasPendentesAssinatura(request, assinarPendenciaDt, UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaLocalizar.jsp";
				
				if (houveAssinatura && ehParaImprimir)
				{
					request.setAttribute("PaginaAtual", Configuracao.Imprimir);
					request.setAttribute("OperacaoExpedirImprimir", "ExpedirImprimir");
				} else {
					limparListas(request);
				}
				
				break;
				
			case Configuracao.Curinga6:{
				if (!UsuarioSessao.isAssessorJuizVaraTurma() &&
			    	    !UsuarioSessao.isAssessorAdvogado() &&
			    	    !UsuarioSessao.isAssessorMP() &&
			    	    !UsuarioSessao.isAssessor() &&
			    	    !UsuarioSessao.isAssessorPresidenteSegundoGrau()  &&
			    	    !UsuarioSessao.isAssessorDesembargador() &&
			    	    UsuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao)) {
					// id da pendência e a senha do certificado
					String senhaCertificado = (String) request.getParameter("senha");
					String id_Pend = (String) request.getParameter("id_pendencia");
					boolean salvarSenha =  Boolean.parseBoolean(request.getParameter("salvarSenha"));
					
					AnalisePendenciaDt analisePendenciaDt = new AnalisePendenciaDt();
					analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(id_Pend);		
					
					if (analisePendenciaDt != null){
						setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());				
						prepareDadosAssinaturaNovo(assinarPendenciaDt, Movimentacaone, request, UsuarioSessao, analisePendenciaDt);
					}else{
						throw new MensagemException( "Pendênica selecionada não foi encontrada.");				
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
					
					this.salvarAnalisePendenciaAssincrona(analisePendenciaDt, Movimentacaone, request, response, UsuarioSessao,  assinarPendenciaDt.getQuantidadeConclucoesPendentes());
					
					List<AnalisePendenciaDt> pendenciasAssinadasMagistrados = null;
					if (request.getSession().getAttribute("pendenciasAssinadasMagistrados") != null){
						pendenciasAssinadasMagistrados = (List<AnalisePendenciaDt>) request.getSession().getAttribute("pendenciasAssinadasMagistrados");
					} else {
						pendenciasAssinadasMagistrados = new ArrayList<AnalisePendenciaDt>();
					}
					pendenciasAssinadasMagistrados.add(analisePendenciaDt);
					request.getSession().setAttribute("pendenciasAssinadasMagistrados", pendenciasAssinadasMagistrados);
					
				} else {					
					if (assinarPendenciaDt.isAcaoAssinatura()){
						throw new MensagemException( "Usuário sem permissão para assinar.");	
					} else {
						throw new MensagemException( "Usuário sem permissão para retornar para pré-análise.");
					}
				}			
				return;					
			}
			case Configuracao.Curinga7:{
				consultarPreAnalisadasPendentesAssinatura(request, assinarPendenciaDt, UsuarioSessao, Movimentacaone);
				stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaLocalizar.jsp";
				request.setAttribute("PaginaAtual", Configuracao.Imprimir);
				request.setAttribute("OperacaoExpedirImprimir", "ExpedirImprimir");
				break;
			}
			case Configuracao.Curinga8:{
				
				break;
			}
			
			// Consultar as pendências pendentes de assinatura
			case Configuracao.Localizar:
				consultarPreAnalisadasPendentesAssinatura(request, assinarPendenciaDt, UsuarioSessao, Movimentacaone);				
				stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaLocalizar.jsp";
				break;
				
			case Configuracao.Imprimir:
				request.setAttribute("OperacaoExpedirImprimir", "");

				// Verifica se possui arquivos na sessao
				List<AnalisePendenciaDt> pendenciasAssinadasMagistrados = ((List<AnalisePendenciaDt>) request.getSession().getAttribute("pendenciasAssinadasMagistrados"));

				if (pendenciasAssinadasMagistrados == null || pendenciasAssinadasMagistrados.size() == 0) {
					limparLista(assinarPendenciaDt, request);
					return;
				}
				
				// Emissão dos arquivos em PDF
				byte[] arquivoPDFCompleto = null;
				boolean isPrimeiroArquivo = true;
				int contArquivo = 1;
				
				for (Iterator<AnalisePendenciaDt> iterator = pendenciasAssinadasMagistrados.iterator(); iterator.hasNext();) {
					AnalisePendenciaDt AnalisePendenciaDt = (AnalisePendenciaDt) iterator.next();
				    for (Iterator<ArquivoDt> iterator2 = AnalisePendenciaDt.getListaArquivos().iterator(); iterator2.hasNext();) {
				    	ArquivoDt arquivoDt = (ArquivoDt) iterator2.next();
						CMSSignedData dados = new CMSSignedData(arquivoDt.conteudoBytes());
						CMSProcessable conteudo = dados.getSignedContent();
						ByteArrayOutputStream arquivoSemAssinatura = new ByteArrayOutputStream();
						conteudo.write(arquivoSemAssinatura);
						
						byte arquivoByteArrayPDF[] = null;
						if (arquivoDt.isArquivoHtml()) {
							arquivoByteArrayPDF = ConverterHtmlPdf.converteHtmlPDF(arquivoSemAssinatura.toByteArray(), false);
						} else if (arquivoDt.isArquivoPDF()) {
							arquivoByteArrayPDF = arquivoSemAssinatura.toByteArray();
						}
						
						if (arquivoByteArrayPDF != null) {
							if (isPrimeiroArquivo) {
								arquivoPDFCompleto = GerarCabecalhoProcessoPDF.geraCabecalhoArquivoPDF(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , arquivoDt, UsuarioSessao.getUsuarioDt().getNome(), AnalisePendenciaDt.getNumeroPrimeiroProcessoListaFechar(), arquivoByteArrayPDF, contArquivo);
							} else{
								arquivoByteArrayPDF = GerarCabecalhoProcessoPDF.geraCabecalhoArquivoPDF(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , arquivoDt, UsuarioSessao.getUsuarioDt().getNome(), AnalisePendenciaDt.getNumeroPrimeiroProcessoListaFechar(), arquivoByteArrayPDF, contArquivo);
								arquivoPDFCompleto = ConcatenatePDF.concatenaPdf(arquivoPDFCompleto, arquivoByteArrayPDF);
							}
							isPrimeiroArquivo = false;
							contArquivo += 1;
						}					
					}
				}

				if (arquivoPDFCompleto.length > 0) {
					enviarPDF(response, arquivoPDFCompleto, "ArquivoPDF_Pendencia_" );
					// Limpa as listas de arquivos
					limparLista(assinarPendenciaDt, request);
					return;
				}
				
				limparLista(assinarPendenciaDt, request);
				break;
		}

		request.getSession().setAttribute("AssinarPendenciaDt", assinarPendenciaDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);	
		
		if (!(!UsuarioSessao.isAssessorJuizVaraTurma() &&
	    	  !UsuarioSessao.isAssessorAdvogado() &&
	    	  !UsuarioSessao.isAssessorMP() &&
	    	  !UsuarioSessao.isAssessor() &&
	    	  !UsuarioSessao.isAssessorPresidenteSegundoGrau() &&
	    	  !UsuarioSessao.isAssessorDesembargador() &&
	    	  UsuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao))) {
			stAcao = "/WEB-INF/jsptjgo/AssinarPendenciaLocalizarAssessor.jsp";
		}
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	
	/**
	 * Realiza chamada ao método para consultar as pré-analises simples de um usuário pendentes de assinatura
	 * @throws Exception 
	 */
	protected boolean consultarPreAnalisadasPendentesAssinatura(HttpServletRequest request, AssinarPendenciaDt assinarPendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		
		String numeroProcesso = assinarPendenciaDt.getNumeroProcesso();
		assinarPendenciaDt.limparProcesso();
		request.setAttribute("numeroProcesso", numeroProcesso);
		
		//Verifica se usuário digitou "." e dígito verificador
		if (numeroProcesso.length() > 0 && numeroProcesso.indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto.");
			return boRetorno;
		}	

		assinarPendenciaDt.setListaPendenciaArquivo(movimentacaone.consultarPreAnalisesSimplesPendentesAssinatura(usuarioSessao.getUsuarioDt(), numeroProcesso, assinarPendenciaDt.getTipoPendencia()));
		if (assinarPendenciaDt.getListaPendenciaArquivo().size() > 0) {
			
			//Atualiza o rash para consulta
			for (Object pendenciaArquivoObj : assinarPendenciaDt.getListaPendenciaArquivo()) {
				PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt)pendenciaArquivoObj;
				pendenciaArquivoDt.setHash(usuarioSessao.getCodigoHash(pendenciaArquivoDt.getId()));
			}
			
			request.setAttribute("ListaPreAnalises", assinarPendenciaDt.getListaPendenciaArquivo());
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
			request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
			boRetorno = true;
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemOk", "Todas as pré-análises foram assinadas, e não existem novas pré-análises aguardando assinatura.");
		}

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
	 * @param AssinarPendenciaDt
	 * @param Movimentacaone
	 * @throws Exception 
	 */
	protected void prepareDadosAssinatura(AssinarPendenciaDt assinarPendenciaDt, MovimentacaoNe movimentacaone, HttpServletRequest request, UsuarioNe UsuarioSessao, String[] pendencias) throws Exception{		

		int fluxo = 6;
		boolean fluxoPreAnalise = true;
		List listaAnalisePendencia = new ArrayList();
		
		for (int i = 0; i < pendencias.length; i++) {			
			String id_Pendencia = (String) pendencias[i];		
			//Análise Simples: verifica se existe uma pré-analise para a pendência selecionada
			PendenciaArquivoDt arquivoPreAnalise = movimentacaone.getArquivoPreAnalisePendencia(id_Pendencia);			
			if (arquivoPreAnalise != null) {
				AnalisePendenciaDt analisePendenciaDt = movimentacaone.getPreAnalisePendencia(arquivoPreAnalise);
				if (analisePendenciaDt != null){
					setarPreAnalise(request, analisePendenciaDt, movimentacaone, UsuarioSessao.getUsuarioDt());
					preparaAnaliseSimples(request, analisePendenciaDt, id_Pendencia, UsuarioSessao.getUsuarioDt(), movimentacaone, fluxo, fluxoPreAnalise, assinarPendenciaDt.getTipoPendencia());				
					listaAnalisePendencia.add(analisePendenciaDt);
				}
			}			
		}
		
		assinarPendenciaDt.prepareDadosAssinatura(listaAnalisePendencia);
		//request.setAttribute("nomeArquivos", assinarPendenciaDt.getNomeArquivos());
		request.setAttribute("conteudoArquivos", assinarPendenciaDt.getConteudoArquivos());
		
	}
	
	protected void prepareDadosAssinaturaNovo(AssinarPendenciaDt assinarPendenciaDt, MovimentacaoNe movimentacaone, HttpServletRequest request, UsuarioNe UsuarioSessao, AnalisePendenciaDt analisePendenciaDt) throws Exception{		
		int fluxo = 6;
		boolean fluxoPreAnalise = true;
		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			setarPreAnalise(request, analisePendenciaDt, movimentacaone, UsuarioSessao.getUsuarioDt());
			preparaAnaliseSimples(request, analisePendenciaDt, analisePendenciaDt.getId_Pendencia(), UsuarioSessao.getUsuarioDt(), movimentacaone, fluxo, fluxoPreAnalise, assinarPendenciaDt.getTipoPendencia());				
		}			
	}
	
	/**
	 * Método que irá setar na analise na pendencia
	 * @param request
	 * @param analisePendenciaDt
	 * @param movimentacaoNe
	 * @param usuarioDt 
	 * @throws Exception 
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioDt usuarioDt){

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
	 * Prepara Análise Simples
	 * 
	 * @param request
	 * @param analisePendenciaDt
	 * @param id_Pendencia
	 * @param usuarioDt
	 * @param movimentacaoNe
	 * @param fluxo
	 * @param fluxoPreAnalise	 
	 * @param usuarioSessao
	 * @throws Exception
	 */
	protected void preparaAnaliseSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) throws Exception{		
		//Pesquisa dados da pendência
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoPendencia);
		
	}
	
	/**
	 * Armazena fluxo padrão.
	 * 
	 * @param analisePendenciaDt
	 * @param fluxo
	 * @param fluxoPreAnalise
	 * @param tipoPendencia
	 */
	protected void setFluxoRedirecionamento(AnalisePendenciaDt analisePendenciaDt, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) {
		analisePendenciaDt.setFluxo(fluxo);
		analisePendenciaDt.setPreAnalise(fluxoPreAnalise);
		analisePendenciaDt.setId_TipoPendencia(tipoPendencia);		
	}
	
	protected boolean salvarAnalisePendencia(AssinarPendenciaDt AssinarPendenciaDt, MovimentacaoNe Movimentacaone, HttpServletRequest request, UsuarioNe UsuarioSessao) throws Exception{
		List listaAnalisePendencia = null;
		String Mensagem = "";
		String MensagemRetorno = "";
		AssinarPendenciaDt.setEhParaImprimir(false);
		String pendenciaTipoCodigo = "";
		
		//mfssilva 03/12/2019 - Correção para funcionar com o assinador novo
		
		if (AssinarPendenciaDt.getTipoPendencia() == null || AssinarPendenciaDt.getTipoPendencia().equals("")){
			
			List pendenciasArquivos = AssinarPendenciaDt.getListaPendenciaArquivo();
			
			for(int i = 0 ; i< pendenciasArquivos.size();i++) {
				PendenciaArquivoDt pendenciaArquivo =  (PendenciaArquivoDt)pendenciasArquivos.get(i);
				pendenciaTipoCodigo = pendenciaArquivo.getPendenciaDt().getId_PendenciaTipo();
				Mensagem = encaminharSalvarAnalisePendencia(pendenciaTipoCodigo, AssinarPendenciaDt, Movimentacaone, UsuarioSessao, i);
					
				if (Mensagem.trim().length() > 0){
					if (MensagemRetorno.trim().length() == 0) MensagemRetorno = "Não foi possível assinar a análise dos seguintes processos:";				
					MensagemRetorno += "\nProcesso: " + Funcoes.formataNumeroProcesso( ((AnalisePendenciaDt)AssinarPendenciaDt.getListaAnalisePendencia().get(i)).getNumeroPrimeiroProcessoListaFechar()) + " - Motivo: " + Mensagem;
				}	
				
				Mensagem = "";
				
			}
			
			
		}else{
			listaAnalisePendencia = AssinarPendenciaDt.getListaAnalisePendencia();
			for(int i = 0 ; i< listaAnalisePendencia.size();i++) {
				AnalisePendenciaDt analisePendenciaDt = (AnalisePendenciaDt)listaAnalisePendencia.get(i);
				PendenciaTipoDt pendenciaTipoDt = (PendenciaTipoDt) Movimentacaone.consultarPendenciaTipo(analisePendenciaDt.getId_PendenciaTipo());
				pendenciaTipoCodigo = pendenciaTipoDt.getPendenciaTipoCodigo();
				
				Mensagem = encaminharSalvarAnalisePendencia(pendenciaTipoCodigo, AssinarPendenciaDt, Movimentacaone,UsuarioSessao, i);
				
							
				if (Mensagem.trim().length() > 0){
					if (MensagemRetorno.trim().length() == 0) MensagemRetorno = "Não foi possível assinar a análise dos seguintes processos:";				
					MensagemRetorno += "\nProcesso: " + Funcoes.formataNumeroProcesso( analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()) + " - Motivo: " + Mensagem;
				}
				analisePendenciaDt.limpar();
				Mensagem = "";
			}
						
		}
		
		
		if (MensagemRetorno.trim().length() == 0) request.setAttribute("MensagemOk", "Assinatura efetuada com sucesso.");		
		else request.setAttribute("MensagemErro", MensagemRetorno);
		
		return true;
	}
	
	
	protected String encaminharSalvarAnalisePendencia(String pendenciaTipoCodigo, AssinarPendenciaDt AssinarPendenciaDt, MovimentacaoNe Movimentacaone, UsuarioNe UsuarioSessao, int index ) throws Exception{
		
		String Mensagem = "";
		AnalisePendenciaDt analisePendenciaDt = (AnalisePendenciaDt)AssinarPendenciaDt.getListaAnalisePendencia().get(index);
				
		if (pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PEDIDO_VISTA)) || 
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.RELATORIO)) ||
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PRE_ANALISE_PRECATORIA)) ||
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PEDIDO_MANIFESTACAO)) ||
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.REVISAO)))
		{
			
			Mensagem = Movimentacaone.verificarAnalisePendencia(analisePendenciaDt);
			if (Mensagem.length() == 0) {
				Movimentacaone.salvarAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());			 
			}
		}
		else if (pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CARTA_PRECATORIA)))
		{
			Movimentacaone.distribuirCartaPrecatoria(analisePendenciaDt, UsuarioSessao.getUsuarioDt());
			AssinarPendenciaDt.setEhParaImprimir(ehParaImprimir(UsuarioSessao));
		}
		else
		{
			Movimentacaone.responderPendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());
			AssinarPendenciaDt.setEhParaImprimir(ehParaImprimir(UsuarioSessao));
		}
		
		return Mensagem;
		
	}
	
	protected boolean ehParaImprimir(UsuarioNe usuarioNe)
	{
		return (usuarioNe.getUsuarioDt().getGrupoCodigo() != null && usuarioNe.getUsuarioDt().isGrupoCodigoDeAutoridade());
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
	
	/**
	 * Zera listas de pendências da sessão
	 * @param request
	 */
	protected void limparLista(AssinarPendenciaDt assinarPendenciaDt, HttpServletRequest request) {
		assinarPendenciaDt.limpar();
		request.getSession().removeAttribute("pendenciasAssinadasMagistrados");
	}
	
	/**
	 * salva a primeira análise pendência da lista.
	 * 
	 * @param AssinarPendenciaDt
	 * @param Movimentacaone
	 * @param request
	 * @param UsuarioSessao
	 * @return
	 * @throws Exception
	 */
	protected void salvarAnalisePendenciaAssincrona(AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe Movimentacaone, HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int qtdPendencias) throws Exception{
		String mensagem = "";
		String MensagemRetorno = "";
		String pendenciaTipoCodigo = "";
		
		//Sempre haverá um item na lista ListaPendenciasFechar, de onde será extraído o tipo da pendência.
		pendenciaTipoCodigo = ((PendenciaDt)analisePendenciaDt.getListaPendenciasFechar().get(0)).getPendenciaTipoCodigo();
		
		mensagem = salvarAnalisePendencia(analisePendenciaDt, pendenciaTipoCodigo, Movimentacaone,UsuarioSessao);
		
		if (mensagem.length() > 0) {
			MensagemRetorno = "Não foi possível assinar a análise dos seguintes processos: \nProcesso: " + Funcoes.formataNumeroProcesso( analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()) + " - Motivo: " + mensagem;
			throw new MensagemException(MensagemRetorno);
		}
	}
	
	protected String salvarAnalisePendencia(AnalisePendenciaDt analisePendenciaDt, String pendenciaTipoCodigo, MovimentacaoNe Movimentacaone, UsuarioNe UsuarioSessao) throws Exception{
		
		String Mensagem = "";
				
		if (pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PEDIDO_VISTA)) || 
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.RELATORIO)) ||
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PRE_ANALISE_PRECATORIA)) ||
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PEDIDO_MANIFESTACAO)) ||
				pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.REVISAO)))	{
			
			Mensagem = Movimentacaone.verificarAnalisePendencia(analisePendenciaDt);
			if (Mensagem.length() == 0) {
				Movimentacaone.salvarAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());			 
			}
		
		} else if (pendenciaTipoCodigo.equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CARTA_PRECATORIA)))		{
			Movimentacaone.distribuirCartaPrecatoria(analisePendenciaDt, UsuarioSessao.getUsuarioDt());
		
		} else{
			Movimentacaone.responderPendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());
		}
		
		return Mensagem;
		
	}

}
