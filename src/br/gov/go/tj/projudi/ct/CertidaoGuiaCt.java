package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.CertidaoGuiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoGuiaCt extends Controle {

	private static final String CERTIDAO_GUIA = "CertidaoGuia";
	private static final String GERAR_CERTIDAO = "Gerar Certidão";
	private static final String CERTIDAO_DE_PRATICA_FORENSE = "Certidão de Prática Forense";
	private static final String CERTIDAO_NARRATIVA = "Certidão Narrativa";
	private static final String CERTIDAO_GUIA_NE = "certidaoGuiaNe";
	private static final String NUMERO_GUIA = "numeroGuia";
	private static final String CERTIDAO_GUIA_DT = "certidaoGuiaDt";

	private static final long serialVersionUID = 5850769893684390343L;
	
	public int Permissao() {
		return CertidaoGuiaDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", CERTIDAO_GUIA);
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("TituloPagina", GERAR_CERTIDAO);
		request.setAttribute("tempPrograma", GERAR_CERTIDAO);
		request.setAttribute("tempNomeBusca", GERAR_CERTIDAO);
		request.setAttribute("numeroGuiaOk", "");
		
		String stAcao = "/WEB-INF/jsptjgo/GerarCertidao.jsp";
		String stTemp = "";
		String stNomeBusca1 = "";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		CertidaoGuiaNe certidaoGuiaNe = getCertidaoNe(request);	
		CertidaoGuiaDt certidaoGuiaDt = getCertidaoAtualizada(certidaoGuiaNe, request, usuarioSessao, paginaatual);
				
		certidaoGuiaDt.setId_UsuarioEscrivaoResponsavel(request.getParameter("Id_UsuarioServentia")); 
		certidaoGuiaDt.setNomeUsuarioEscrivaoResponsavel(request.getParameter("UsuarioServentia")); 
		certidaoGuiaDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		certidaoGuiaDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
			// Localizar Guia
			case Configuracao.Localizar:				
				if(certidaoGuiaDt == null || certidaoGuiaDt.getNumeroGuia().isEmpty()) {
					request.setAttribute("numeroGuiaOk", "inexistente");
				} else {						
					if(request.getSession().getAttribute("TipoConsulta") != null){
						if(request.getSession().getAttribute("TipoConsulta").toString().equalsIgnoreCase("Publica")) {
							ProcessoDt processoDt = certidaoGuiaNe.consultarProcessoId(certidaoGuiaDt.getIdProcesso());
							if(processoDt.isSigiloso() || processoDt.isSegredoJustica()){
								throw new MensagemException("Processo sigiloso ou em segredo de justiça. Favor procurar a unidade judiciária.");
							}
						}
					}						
					stAcao = obtenhaAcaoCertidaoGuia(request, certidaoGuiaDt);
					request.setAttribute("numeroGuiaOk", "ok");
					
					if(!certidaoGuiaDt.isGuiaPaga()) { // Verifica se a guia está como NÃO PAGA.
						request.setAttribute("numeroGuiaOk", "nao_paga");
					} else if(certidaoGuiaDt.isGuiaJaUtilizada()) {
						request.setAttribute("numeroGuiaOk", "ja_utilizada");
						gerarCertidaoGuiaJaUtilizada(request, response, usuarioSessao, certidaoGuiaDt, certidaoGuiaNe);
						return;
					} else {
						certidaoGuiaDt.setMovimentacoesProcesso(certidaoGuiaNe.consultarStringMovimentacoesProcesso(certidaoGuiaDt.getIdProcesso()) );
					}											
				}
				break;
				
			// Gerar Pré-Visualização da Certidão
			case Configuracao.Curinga6:
				if (GuiaTipoDt.isGuiaCertidaoNarrativa(certidaoGuiaDt)) {
					gerarCertidaoNarrativa(request, usuarioSessao, certidaoGuiaDt, certidaoGuiaNe);					
				} else if (GuiaTipoDt.isGuiaCertidaoPraticaForense(certidaoGuiaDt)) {
					gerarCertidaoPraticaForense(usuarioSessao, certidaoGuiaDt, certidaoGuiaNe);
				} else {
					// 35 - Certidão Interdição
					// 36 - Certidão Licitação
					// 37 - Certidão Autoria
					lancaMensagemExceptionTipoDeGuiaNaoImplementado();
				}
				break;
			
			// Salvar / Gerar PDF (Utilizado para salvar e imprimir a certidão)
			case Configuracao.Salvar:					
				if(gerarNovaCertidao(request, response, usuarioSessao, certidaoGuiaDt, certidaoGuiaNe)) {
					return;
				}
				break;
				
			// Consultar usuários responsáveis
			case (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = { "Nome" };
					String[] lisDescricao = { "Usuário", "Serventia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_UsuarioServentia");
					request.setAttribute("tempBuscaDescricao", "UsuarioServentia");
					request.setAttribute("tempBuscaPrograma", "Busca de Escrivão(ã) Responsável ");
					request.setAttribute("tempRetorno", "CertidaoGuia");
					request.setAttribute("tempPaginaAtualJSON", "-1");
					request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					stTemp = "";
					stTemp = certidaoGuiaNe.consultarUsuariosServentiaJSON(stNomeBusca1, posicaopaginaatual, usuarioSessao.getUsuarioDt().getId_Serventia());
					enviarJSON(response, stTemp);
					return;
				}
			break;
		}
		
		request.getSession().setAttribute(CERTIDAO_GUIA_DT, certidaoGuiaDt);
		
		stAcao = obtenhaAcaoCertidaoGuia(request, paginaatual, stAcao, certidaoGuiaDt);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected String obtenhaAcaoCertidaoGuia(HttpServletRequest request, int paginaatual, String stAcao, CertidaoGuiaDt certidaoGuiaDt) throws MensagemException {
		if (paginaatual != (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			if((certidaoGuiaDt.getId_GuiaTipo() != null && 
			    !certidaoGuiaDt.getId_GuiaTipo().isEmpty()) ||
				(certidaoGuiaDt.getGuiaEmissaoDt() != null && 
				certidaoGuiaDt.getGuiaEmissaoDt().isGuiaEmitidaSPG())){
					stAcao = obtenhaAcaoCertidaoGuia(request, certidaoGuiaDt);
			}
		} 
		return stAcao;
	}
	
	protected CertidaoGuiaNe getCertidaoNe(HttpServletRequest request) {
		CertidaoGuiaNe certidaoGuiaNe = (CertidaoGuiaNe) request.getSession().getAttribute(CERTIDAO_GUIA_NE);
		if (certidaoGuiaNe == null){
			certidaoGuiaNe = new CertidaoGuiaNe();
			request.getSession().setAttribute(CERTIDAO_GUIA_NE, certidaoGuiaNe);			
		}
		return certidaoGuiaNe;
	}
	
	protected CertidaoGuiaDt getCertidaoAtualizada(CertidaoGuiaNe certidaoGuiaNe, HttpServletRequest request,  UsuarioNe usuarioSessao, int paginaatual) throws Exception {
		if (paginaatual == Configuracao.Novo) {
			request.getSession().removeAttribute(CERTIDAO_GUIA_DT);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			CertidaoGuiaDt certidaoGuiaDt = new CertidaoGuiaDt();
			certidaoGuiaDt.setId_UsuarioEscrivaoResponsavel(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
			certidaoGuiaDt.setNomeUsuarioEscrivaoResponsavel(usuarioSessao.getUsuarioDt().getNome());
			return certidaoGuiaDt;
		}
		
		String numeroGuia = "";
		if(request.getParameter(NUMERO_GUIA) != null){
			numeroGuia = request.getParameter(NUMERO_GUIA);
		}
		
		CertidaoGuiaDt certidaoGuiaDt = (CertidaoGuiaDt) request.getSession().getAttribute(CERTIDAO_GUIA_DT);
		
		if(numeroGuia != null && !numeroGuia.isEmpty() &&
			(certidaoGuiaDt == null || 
			  !Funcoes.obtenhaSomenteNumeros(numeroGuia).equalsIgnoreCase(Funcoes.obtenhaSomenteNumeros(certidaoGuiaDt.getNumeroGuia())))) {
			
			certidaoGuiaDt = certidaoGuiaNe.consultarCertidaoProjudi(Funcoes.obtenhaSomenteNumeros(numeroGuia));
			
			ServentiaDt serventiaDt = certidaoGuiaNe.consultarServentiaId(usuarioSessao.getId_Serventia());
			if (serventiaDt == null) throw new MensagemException(String.format("Serventia logada não encontrada com o id %s.", usuarioSessao.getId_Serventia()));
			
			if(certidaoGuiaDt == null || certidaoGuiaDt.getNumeroGuia().isEmpty()) {
				certidaoGuiaDt = certidaoGuiaNe.consultarCertidaoSPG(Funcoes.obtenhaSomenteNumeros(numeroGuia), serventiaDt);
			} else if (certidaoGuiaDt.getGuiaEmissaoDt().isSerie50() && GuiaTipoDt.isGuiaCertidaoPraticaForense(certidaoGuiaDt)) {
				//Atualizar informações do SPG
				certidaoGuiaNe.preenchaDadosCertidaoPraticaForenseSPG(certidaoGuiaDt, serventiaDt);
			}
			
			if (certidaoGuiaDt != null) {
				certidaoGuiaDt.setId_UsuarioEscrivaoResponsavel(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				certidaoGuiaDt.setNomeUsuarioEscrivaoResponsavel(usuarioSessao.getUsuarioDt().getNome());
			}
		}
		
		if (certidaoGuiaDt == null) {
			certidaoGuiaDt = new CertidaoGuiaDt();
			certidaoGuiaDt.setId_UsuarioEscrivaoResponsavel(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
			certidaoGuiaDt.setNomeUsuarioEscrivaoResponsavel(usuarioSessao.getUsuarioDt().getNome());
		}
		
		return certidaoGuiaDt;
	}

	protected void gerarCertidaoPraticaForense(UsuarioNe usuarioSessao, CertidaoGuiaDt certidaoGuiaDt, CertidaoGuiaNe certidaoGuiaNe) throws Exception, MensagemException {
		boolean quantitativa = certidaoGuiaDt.getTipo().equalsIgnoreCase("Quantitativa");
		
		EstadoDt estadoDt = certidaoGuiaNe.buscaEstado(certidaoGuiaDt.getOabUf());
		if (estadoDt == null) throw new MensagemException(String.format("Estado não encontrado com a UF %s.", certidaoGuiaDt.getOabUf()));
								
		ServentiaDt serventiaDt = certidaoGuiaNe.consultarServentiaId(usuarioSessao.getId_Serventia());
		if (serventiaDt == null) throw new MensagemException(String.format("Serventia logada não encontrada com o id %s.", usuarioSessao.getId_Serventia()));
		
		if (quantitativa) {
			int quantidadeProjudi = certidaoGuiaNe.consultarQuantidadeProcessosProjudiPraticaForenseAdvogado(certidaoGuiaDt.getOabNumero(), certidaoGuiaDt.getOabComplemento(), estadoDt.getId(), certidaoGuiaDt.getDataTimeInicial(), certidaoGuiaDt.getDataTimeFinal(), certidaoGuiaDt.getId_Comarca(), serventiaDt.getId_Area());
			int quantidadeSPG = certidaoGuiaNe.consultarQuantidadeProcessosSPGPraticaForenseAdvogado(certidaoGuiaDt);
			
			certidaoGuiaDt.setQuantidade(quantidadeProjudi + quantidadeSPG);
			
			if (certidaoGuiaDt.getQuantidade() > 0) {
				certidaoGuiaDt.setTexto(certidaoGuiaNe.montaConteudoPorModeloPF(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), Integer.toString(ModeloDt.PRATICA_FORENSE_QUANTITATIVA_MODELO_CODIGO), serventiaDt));
			} else {
				certidaoGuiaDt.setTexto(certidaoGuiaNe.montaConteudoPorModeloPF(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), Integer.toString(ModeloDt.PRATICA_FORENSE_NEGATIVA_MODELO_CODIGO), serventiaDt));
			}			
		} else {
			List<ProcessoCertidaoPraticaForenseDt> listaProcessos = certidaoGuiaNe.consultarProcessosProjudiPraticaForenseAdvogado(certidaoGuiaDt.getOabNumero(), certidaoGuiaDt.getOabComplemento(), estadoDt.getId(), certidaoGuiaDt.getDataTimeInicial(), certidaoGuiaDt.getDataTimeFinal(), certidaoGuiaDt.getId_Comarca(), serventiaDt.getId_Area());
			List<ProcessoCertidaoPraticaForenseDt> listaProcessosSPG = certidaoGuiaNe.consultarProcessosSPGPraticaForenseAdvogado(certidaoGuiaDt);
			
			if (listaProcessosSPG != null && listaProcessosSPG.size() > 0) {
				listaProcessos.addAll(listaProcessosSPG);
			}							
			certidaoGuiaDt.setListaProcesso(listaProcessos);
			
			if (certidaoGuiaDt.getListaProcesso().size() > 0) {
				certidaoGuiaDt.setTexto(certidaoGuiaNe.montaConteudoPorModeloPF(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), Integer.toString(ModeloDt.PRATICA_FORENSE_DESCRITIVA_MODELO_CODIGO), serventiaDt));		
			} else {
				certidaoGuiaDt.setTexto(certidaoGuiaNe.montaConteudoPorModeloPF(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), Integer.toString(ModeloDt.PRATICA_FORENSE_NEGATIVA_MODELO_CODIGO), serventiaDt));
			}					
		}
	}

	protected void gerarCertidaoNarrativa(HttpServletRequest request, UsuarioNe usuarioSessao,
			                            CertidaoGuiaDt certidaoGuiaDt, CertidaoGuiaNe certidaoGuiaNe) throws MensagemException, Exception {		
		String cpf = Funcoes.obtenhaSomenteNumeros(certidaoGuiaDt.getCpf());
		if(!Funcoes.testaCPFCNPJ(cpf)){
			throw new MensagemException("CPF inválido.");
		} else {
			if(request.getParameter("textoCertidao") != null){
				certidaoGuiaDt.setTextoCertidao(request.getParameter("textoCertidao")); // Campo texto usado na certidão narrativa.
			}
			
			certidaoGuiaDt.setMovimentacoesProcesso(certidaoGuiaNe.consultarStringMovimentacoesProcesso(certidaoGuiaDt.getIdProcesso()) ); // INSERÇÃO, NA CERTIDÃO, DAS MOVIMENTAÇÕES DO PROCESSO
			
			if(request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").toString().equalsIgnoreCase("Publica")){
				certidaoGuiaDt.setTexto(certidaoGuiaNe.montaConteudoPorModelo(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), ModeloDt.NARRATIVA_PUBLICA_MODELO_CODIGO));
			} else {
				certidaoGuiaDt.setTexto(certidaoGuiaNe.montaConteudoPorModelo(certidaoGuiaDt, usuarioSessao.getUsuarioDt(), ModeloDt.NARRATIVA_MODELO_CODIGO));								
			}
		}
	}
	
	protected void gerarCertidaoGuiaJaUtilizada(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, 
			                                  CertidaoGuiaDt certidaoGuiaDt, CertidaoGuiaNe certidaoGuiaNe) throws Exception {
		String numeroGuia = Funcoes.obtenhaSomenteNumeros(request.getParameter(NUMERO_GUIA));
		byte[] byTemp = null;
		Signer.acceptSSL();					
		CertidaoValidacaoDt certidao = certidaoGuiaNe.consultarCertidaoValidacaoNumeroGuia(numeroGuia);
		byTemp = certidaoGuiaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , certidao);
		enviarPDFDownload(response, byTemp, getNomeArquivo(certidaoGuiaDt));
		byTemp = null;
	}

	protected boolean gerarNovaCertidao(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, 
			                          CertidaoGuiaDt certidaoGuiaDt, CertidaoGuiaNe certidaoGuiaNe) throws Exception {
		if (certidaoGuiaDt == null || certidaoGuiaDt.getTexto() == null || certidaoGuiaDt.getTexto().equalsIgnoreCase("") || certidaoGuiaDt.getTexto().equals("")){
			request.setAttribute("MensagemErro", "Informe o número da guia!\nE busque os processos!");
			return false;
		}
		else {
			byte[] byTemp = null;
			Signer.acceptSSL();
			byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoGuiaDt.getTexto().getBytes(), true);
			TJDataHora tjdathora = new TJDataHora();
			tjdathora.adicioneDia(30); // Data de validade = 30 dias.
			CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(byTemp, tjdathora.getDataFormatadaddMMyyyyHHmmss(), new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
			
			// Preechimento dos dados da certidão
			cdt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			cdt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
			cdt.setNumeroGuia(certidaoGuiaDt.getNumeroGuia());
			cdt.setId_Modelo(certidaoGuiaDt.getId_Modelo());
			cdt.setId_Comarca(certidaoGuiaDt.getId_Comarca());
			
			// Persistir certidão
			certidaoGuiaNe.salvarCertidaoValidacao(cdt);			
			byTemp = certidaoGuiaNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , cdt);							
			enviarPDFDownload(response, byTemp, getNomeArquivo(certidaoGuiaDt));			
			byTemp = null;
			return true;
		}
	}
	
	protected String getNomeArquivo(CertidaoGuiaDt certidaoGuiaDt) {
		if (GuiaTipoDt.isGuiaCertidaoNarrativa(certidaoGuiaDt)) {
			return "CertidaoNarrativa_";					
		} else if (GuiaTipoDt.isGuiaCertidaoPraticaForense(certidaoGuiaDt)) {
			return "CertidaoPraticaForense_";
		} else {
			return "Certidao_";
		}
	}
	
	protected String obtenhaAcaoCertidaoGuia(HttpServletRequest request, CertidaoGuiaDt certidaoGuiaDt) throws MensagemException{
		if (GuiaTipoDt.isGuiaCertidaoNarrativa(certidaoGuiaDt)) {
			request.setAttribute("TituloPagina", CERTIDAO_NARRATIVA);
			request.setAttribute("tempPrograma", CERTIDAO_NARRATIVA);
			request.setAttribute("tempNomeBusca", CERTIDAO_NARRATIVA);
			return "/WEB-INF/jsptjgo/CertidaoNarrativa.jsp";					
		} else if (GuiaTipoDt.isGuiaCertidaoPraticaForense(certidaoGuiaDt)) {
			request.setAttribute("TituloPagina", CERTIDAO_DE_PRATICA_FORENSE);
			request.setAttribute("tempPrograma", CERTIDAO_DE_PRATICA_FORENSE);
			request.setAttribute("tempNomeBusca", CERTIDAO_DE_PRATICA_FORENSE);
			return "/WEB-INF/jsptjgo/CertidaoPraticaForense.jsp";
		} else {
			// 35 - Certidão Interdição -- "/WEB-INF/jsptjgo/CertidaoInterdicao.jsp";
			// 36 - Certidão Licitação -- "/WEB-INF/jsptjgo/CertidaoLicitacao.jsp";
			// 37 - Certidão Autoria -- "/WEB-INF/jsptjgo/CertidaoAutoria.jsp"
			lancaMensagemExceptionTipoDeGuiaNaoImplementado();
			return "";
		}		
	}

	protected void lancaMensagemExceptionTipoDeGuiaNaoImplementado() throws MensagemException {
		lancaMensagemException(String.format("Tipo de guia não preparado para emitir certidão por esta funcionalidade!"));
	} 
	
	protected void lancaMensagemException(String mensagem) throws MensagemException {
		throw new MensagemException(mensagem);
	}
}
