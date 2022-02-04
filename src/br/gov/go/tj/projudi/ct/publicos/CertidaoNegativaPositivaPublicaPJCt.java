package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CertidaoDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoNegativaPositivaPublicaPJCt extends Controle {
	
	private static final long serialVersionUID = -3767491766355531344L;
		
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
				
		String PosicaoPaginaAtual = "";
		String posicaoPagina= "";
		int passoBusca = 0;		
		CertidaoNe certidaoNe;		
		CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt;		
		ModeloDt modeloDt;
		String stNomeBusca1 = "";
		
		//Configuracoes gerais
		
		if(super.getAtributeParameter(request, "nomeBusca1") != null) stNomeBusca1 = super.getAtributeParameter(request, "nomeBusca1");
		
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
        
		String stAcao = "/WEB-INF/jsptjgo/CertidaoNegativaPositivaPublicaPJ.jsp";
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("CertidaoNe");
		if (certidaoNe == null) certidaoNe = new CertidaoNe();	
		
		certidaoNegativaPositivaPublicaDt = (CertidaoNegativaPositivaPublicaDt) request.getSession().getAttribute("certidaoNegativaPositivaPublicaDt");
		if (certidaoNegativaPositivaPublicaDt == null) certidaoNegativaPositivaPublicaDt = new CertidaoNegativaPositivaPublicaDt();
		
		if (request.getParameter("PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0";
		else PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
		
		// Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		
		request.setAttribute("tempPrograma", "CertidaoNegativaPositivaPublicaPJ");		
		request.setAttribute("tempRetorno", "CertidaoNegativaPositivaPublicaPJ");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");		
		
		if(request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		if(request.getParameter("nomeBusca1") != null)
			request.getParameter("nomeBusca1");
		
		certidaoNegativaPositivaPublicaDt.setNome(request.getParameter("RazaoSocial"));
		certidaoNegativaPositivaPublicaDt.setCpfCnpj(request.getParameter("Cnpj"));
		certidaoNegativaPositivaPublicaDt.setEhPessoaJuridica(true);
		if (request.getParameter("TipoArea") != null &&			
		   (request.getParameter("TipoArea").equalsIgnoreCase("") || request.getParameter("TipoArea").equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)) || request.getParameter("TipoArea").equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))))		
				certidaoNegativaPositivaPublicaDt.setAreaCodigo(request.getParameter("TipoArea"));
		certidaoNegativaPositivaPublicaDt.setNumeroGuiaCertidao(request.getParameter("NumeroDoRequerimento"));
		if (super.getAtributeParameter(request, "Territorio") != null && (super.getAtributeParameter(request, "Territorio").equalsIgnoreCase("") || super.getAtributeParameter(request, "Territorio").equalsIgnoreCase("E") || super.getAtributeParameter(request, "Territorio").equalsIgnoreCase("C")))		
			certidaoNegativaPositivaPublicaDt.setTerritorio(super.getAtributeParameter(request, "Territorio"));
		
		switch (paginaatual) {	
			case 1:
				executeAcaoNovo(request, certidaoNegativaPositivaPublicaDt);				
				break;
			case 3: 
				String mensagem = certidaoNe.VerificarCertidaoNegativaPositivaPublicaPJ(certidaoNegativaPositivaPublicaDt, true); 
				if (mensagem != null && mensagem.trim().length() > 0) {
					request.setAttribute("MensagemErro",  mensagem);
				} else if (emitirCertidaoNegativaPositiva(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)) {
					return;								
				}
				break;
				
				//Busca comarca
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (super.getAtributeParameter(request, "Passo")==null) {
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					request.setAttribute("tempBuscaId", "Id_Comarca");
					request.setAttribute("tempBuscaDescricao", "Comarca");
					request.setAttribute("tempBuscaPrograma", "Comarca");
					request.setAttribute("tempRetorno", "CertidaoNegativaPositivaPublicaPJ");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}
				else{
					String stTemp = "";
					stTemp = new ServentiaNe().consultarDescricaoComarcaJSON(stNomeBusca1, posicaopaginaatual);
					enviarJSON(response, stTemp);					
					return;
				}
				break;
			}
			default:
				if (passoBusca == 3)
				{
					if (emitirCertidaoNegativaPositiva(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)) {
						return;
					}
				} else {
					//Busca Comarca
					String stId = super.getAtributeParameter(request, "Id_Comarca");
					if( stId != null ) {
						ComarcaDt comarcaDt = certidaoNe.consultarIdComarca(stId);
						
						certidaoNegativaPositivaPublicaDt.setId_Comarca(comarcaDt.getId());
						certidaoNegativaPositivaPublicaDt.setComarca(comarcaDt.getComarca());
						certidaoNegativaPositivaPublicaDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					}
				}
				break;
				
		}
		
		request.setAttribute("PosicaoPagina", Funcoes.StringToInt(posicaoPagina+1));		
		request.getSession().setAttribute("CertidaoNe", certidaoNe);	
		request.getSession().setAttribute("certidaoNegativaPositivaPublicaDt", certidaoNegativaPositivaPublicaDt);
		request.setAttribute("PaginaAtual", paginaatual);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);		
	}

	private boolean emitirCertidaoNegativaPositiva(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, CertidaoNe certidaoNe, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws MensagemException, Exception, IOException {
		if(super.checkRecaptcha(request)){					
			if (certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)) && !CertidaoDt.isGratuitaEmissaoPrimeiroGrau) {
				if (emitirCertidaoCivel(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)) return true;
			} else if (certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL)) || CertidaoDt.isGratuitaEmissaoPrimeiroGrau) {
				if (emitirCertidaoCriminalECivelGratuita(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)) return true;
			} else {
				super.redireciona(response, "LogOn?PaginaAtual=-200");
				return true;
			}
		}else {					
			executeAcaoNovo(request, certidaoNegativaPositivaPublicaDt);						
		}
		return false;
	}

	private void executeAcaoNovo(HttpServletRequest request, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		certidaoNegativaPositivaPublicaDt.limpar();	
		certidaoNegativaPositivaPublicaDt.setTerritorio("");
		request.getSession().setAttribute("TipoConsulta", "Publica");
		request.getSession().setAttribute("CodigoCaptcha", "1");
	}

	private boolean emitirCertidaoCriminalECivelGratuita(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, CertidaoNe certidaoNe, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws MensagemException, Exception {
		ModeloDt modeloDt;
		
		List<String> listaProcessos = certidaoNe.consultarProcessosPrimeiroGrauCertidaoNP(certidaoNegativaPositivaPublicaDt);
		certidaoNegativaPositivaPublicaDt.setListaNomesComarcasComProcessos(listaProcessos);	
							
		
		if (certidaoNegativaPositivaPublicaDt.getListaNomesComarcasComProcessos().isEmpty())	{			
			List<String> listaNomesComarcasComProcessoSPGPublica = certidaoNe.getListaNomesComarcasComProcessoSPGPublica(certidaoNegativaPositivaPublicaDt);
			certidaoNegativaPositivaPublicaDt.addListaNomesComarcasComProcessos(listaNomesComarcasComProcessoSPGPublica);				
		}
		
		if (certidaoNegativaPositivaPublicaDt.getListaNomesComarcasComProcessos().isEmpty())
		{
			if (certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))){
				modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaPublicaDt.NEGATIVA_CRIMINAL_JURIDICA_PUBLICA_MODELO_CODIGO);	
			}else if (CertidaoDt.isGratuitaEmissaoPrimeiroGrau){ 
				modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaPublicaDt.NEGATIVA_CIVEL_JURIDICA_PUBLICA_MODELO_CODIGO);
			}else{ 
				throw new MensagemException("Para emissão pela internet sem guia só é possível emitir certidão negativa criminal");
			}
									
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNe.montaModelo(certidaoNegativaPositivaPublicaDt, modeloDt, UsuarioSessao));
			
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNegativaPositivaPublicaDt.getTexto().replace(CertidaoNegativaPositivaPublicaDt.TAG_NOME_COMARCA_TITULO, obtenhaNomeDaComarcaTitulo(certidaoNegativaPositivaPublicaDt)));
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNegativaPositivaPublicaDt.getTexto().replace(CertidaoNegativaPositivaPublicaDt.TAG_NOME_COMARCA_TEXTO, obtenhaNomeDaComarcaTexto(certidaoNegativaPositivaPublicaDt)));

										
			TJDataHora tjdathoraVenciamento = new TJDataHora();							
			tjdathoraVenciamento.adicioneDia(30);
			// Gerar o id da certidão...
			CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(new String().getBytes(), tjdathoraVenciamento.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
			cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			cdt.setCodigoTemp(CertidaoValidacaoDt.CERTIDAO_PUBLICA_NADA_CONSTA);
			certidaoNe.salvar(cdt);
			
			// Preenchendo o código da certidão...
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNegativaPositivaPublicaDt.getTexto().replace(CertidaoNegativaPositivaPublicaDt.TAG_NUMERO_GUIA_VALIDACAO, Cifrar.codificarId_certidao(cdt.getId())));
									
			byte[] byTemp = null;
			Signer.acceptSSL();
			byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoNegativaPositivaPublicaDt.getTexto().getBytes(), true);
			cdt.setCertidao(byTemp);
			certidaoNe.salvar(cdt);
			
			byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), cdt);
			
			enviarPDF(response, byTemp, "Certidao");
			
			byTemp = null;
		    return true;
		} else {
			super.exibaMensagemInconsistenciaErro(request, "<b>Atenção:</b> Essa certidão não pode ser emitida de forma automática.\nIsto ocorre porque pode haver algum processo vinculado ao requerente\n ou outro caso que exija análise para emissão, portanto\n dirija-se ao Cartório Distribuidor do Fórum local.");
			return false;
		}
	}
	
	private String obtenhaNomeDaComarcaTitulo(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		if (certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E") || certidaoNegativaPositivaPublicaDt.getComarca().trim().length() == 0)
			return "TODAS AS COMARCAS";
		return "COMARCA DE " + certidaoNegativaPositivaPublicaDt.getComarca().toUpperCase();
	}
	
	private String obtenhaNomeDaComarcaTexto(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		if (certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E") || certidaoNegativaPositivaPublicaDt.getComarca().trim().length() == 0)
			return "";
		return ", na COMARCA DE " + certidaoNegativaPositivaPublicaDt.getComarca().toUpperCase() + ", ";
	}
	
	private boolean emitirCertidaoCivel(HttpServletRequest request, HttpServletResponse response,
                                        UsuarioNe UsuarioSessao, CertidaoNe certidaoNe,
                                        CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws MensagemException, Exception {
		
		// Se já foi emitida certidão com a guia informada, devolvemos a certidão já emitida...
		if (certidaoNegativaPositivaPublicaDt.getCertidaoEmitida() != null) {
			String nome="'Certidao" + request.getSession().getAttribute("codigo");	    	
	    	// gerar pdf como arquivos da publicação
	    	byte[] byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), certidaoNegativaPositivaPublicaDt.getCertidaoEmitida());		    
	    	enviarPDF(response, byTemp, nome);										
		    return true;
		}		
	
		String numeroGuia = certidaoNegativaPositivaPublicaDt.getNumeroGuiaCertidao().replaceAll(" ", "");
		List processoLista;
		CertidaoNegativaPositivaDt certidaoNegativaPositivaDt;
		ModeloDt modeloDt = null;
				
		certidaoNegativaPositivaDt = certidaoNe.getDtGuia(numeroGuia);			
		
		if (certidaoNegativaPositivaDt == null) {
			throw new MensagemException("Guia não encontrada, verifique o número! \nDigite somente o número sem a série, exemplo guia NÚMERO: 18680149 - 1, favor digitar: 186801491.");			
		}
		
		certidaoNegativaPositivaDt.setGuiaEmissaoCertidao(certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao());
		certidaoNegativaPositivaDt.setNumeroGuia(numeroGuia);
		processoLista = certidaoNe.listarProcessoCertidaoNP(certidaoNegativaPositivaDt);
		certidaoNegativaPositivaDt.setListaProcessos(processoLista);

		if (certidaoNegativaPositivaDt.getListaProcessos().size() == 0)	{
			certidaoNegativaPositivaDt = certidaoNe.getListaProcessoSPG(certidaoNegativaPositivaDt);			
		}		
		try {
			if (certidaoNegativaPositivaDt.getListaProcessos().size() == 0)	{
				
				if (certidaoNegativaPositivaDt.getComarcaCodigo() != null && Funcoes.StringToInt(certidaoNegativaPositivaDt.getComarcaCodigo()) > 0) {
					ComarcaDt comarcaDt = certidaoNe.consultarComarcaCodigo(certidaoNegativaPositivaDt.getComarcaCodigo());
					
					certidaoNegativaPositivaDt.setId_Comarca(comarcaDt.getId());
					certidaoNegativaPositivaDt.setComarca(comarcaDt.getComarca());
					certidaoNegativaPositivaDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					
					certidaoNegativaPositivaPublicaDt.setId_Comarca(comarcaDt.getId());
					certidaoNegativaPositivaPublicaDt.setComarca(comarcaDt.getComarca());
					certidaoNegativaPositivaPublicaDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
					certidaoNegativaPositivaPublicaDt.setTerritorio("C");//Indica que é por comarca...
				}
				
				if (certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao() == null) {
					certidaoNegativaPositivaPublicaDt.setGuiaEmissaoCertidao(certidaoNe.ObtenhaGuiaEmissaoCertidao(numeroGuia));
				}				
				// Preenchendo a data de pagamento...
				if (certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao() != null) {
					TJDataHora dataRecebimento = new TJDataHora();
					dataRecebimento.setDataaaaa_MM_ddHHmmss(certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao().getDataRecebimento());
					certidaoNegativaPositivaDt.setDataPagamento(dataRecebimento.getDataFormatadaddMMyyyy());
				}
				
				certidaoNegativaPositivaDt.getValorCertidao();
				
				if (certidaoNegativaPositivaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)))
					modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_CIVEL_JURIDICA_PUBLICA_GUIA_MODELO_CODIGO);	
				else 
					throw new MensagemException("Para emissão pela internet com guia só é possível emitir certidão negativa cível");
										
				certidaoNegativaPositivaDt.setTexto(certidaoNe.montaModelo(certidaoNegativaPositivaDt, modeloDt, UsuarioSessao));
				
				// Preenchendo o nome da comarca...
				certidaoNegativaPositivaDt.setTexto(certidaoNegativaPositivaDt.getTexto().replace(CertidaoNegativaPositivaDt.TAG_NOME_COMARCA_TITULO, obtenhaNomeDaComarcaTitulo(certidaoNegativaPositivaPublicaDt)));
				certidaoNegativaPositivaDt.setTexto(certidaoNegativaPositivaDt.getTexto().replace(CertidaoNegativaPositivaDt.TAG_NOME_COMARCA_TEXTO, obtenhaNomeDaComarcaTexto(certidaoNegativaPositivaPublicaDt)));
											
				TJDataHora tjdathoraVenciamento = new TJDataHora();							
				tjdathoraVenciamento.adicioneDia(30);
				// Gerar o id da certidão...
				CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(new String().getBytes(), tjdathoraVenciamento.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
				cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				cdt.setCodigoTemp(CertidaoValidacaoDt.CERTIDAO_PUBLICA_NADA_CONSTA);
				if (certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao() != null) {
					cdt.setNumeroGuia(certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao().getNumeroGuiaCompleto());
				} else {
					cdt.setNumeroGuia(numeroGuia + "09");	
				}		
				cdt.setId_Modelo(modeloDt.getId());
				cdt.setId_Comarca(certidaoNegativaPositivaDt.getId_Comarca());
				cdt.setComarcaCodigo(certidaoNegativaPositivaDt.getComarcaCodigo());
				cdt.setComarca(certidaoNegativaPositivaDt.getComarca());
				certidaoNe.salvar(cdt);
				
				// Preenchendo o código da certidão...
				certidaoNegativaPositivaDt.setTexto(certidaoNegativaPositivaDt.getTexto().replace(CertidaoNegativaPositivaDt.TAG_NUMERO_GUIA_VALIDACAO, Cifrar.codificarId_certidao(cdt.getId())));
				
				byte[] byTemp = null;
				Signer.acceptSSL();
				byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoNegativaPositivaDt.getTexto().getBytes(), true);
				cdt.setCertidao(byTemp);
				certidaoNe.salvar(cdt);
				
				byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), cdt);
				enviarPDF(response, byTemp, "Certidao");
				byTemp = null;		
				
			    return true;
			} else {
				super.exibaMensagemInconsistenciaErro(request, "<b>Atenção:</b> Essa certidão não pode ser emitida de forma automática com esta guia.\nIsto ocorre porque pode haver algum processo vinculado ao requerente\n ou outro caso que exija análise para emissão, portanto dirija-se ao Cartório Distribuidor Cível do Fórum da comarca de " + certidaoNegativaPositivaDt.getComarca() + " com a guia informada.");
				TenteRetirarDataDeApresentacao(certidaoNegativaPositivaPublicaDt);
				return false;
			}
		} catch(Exception e) {
			TenteRetirarDataDeApresentacao(certidaoNegativaPositivaPublicaDt);
			throw e;
		}
	}
	
	private void TenteRetirarDataDeApresentacao(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		try 
		{
			if (certidaoNegativaPositivaPublicaDt != null) {
				if (certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao() != null) {
					new GuiaSPGNe().retireDataApresentacao(certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao().getNumeroGuiaCompleto());		
				} else {
					new GuiaSPGNe().retireDataApresentacao(certidaoNegativaPositivaPublicaDt.getNumeroGuiaCertidao().replaceAll(" ", "") + "09");
				}
			}	
		} 
		catch(Exception ex) {}
	}

	@Override
	public int Permissao() {
		return 864;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}

}
