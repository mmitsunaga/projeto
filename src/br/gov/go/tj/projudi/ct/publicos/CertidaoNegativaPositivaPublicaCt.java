package br.gov.go.tj.projudi.ct.publicos;

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

public class CertidaoNegativaPositivaPublicaCt extends Controle {

	private static final long serialVersionUID = 5994250414557978714L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
				
		String PosicaoPaginaAtual = "";
		String posicaoPagina= "";
		String mensagem = "";
		int passoBusca = 0;	
		String stNomeBusca1 = "";
		String tituloPagina = "";
		
		if(super.getAtributeParameter(request, "nomeBusca1") != null) stNomeBusca1 = super.getAtributeParameter(request, "nomeBusca1");
		
		CertidaoNe certidaoNe;		
		CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt;						
		
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
        
		String stAcao = ObtenhaAcaoJspEmissaoCertidao();
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("CertidaoNe");
		if (certidaoNe == null) certidaoNe = new CertidaoNe();	
		
		certidaoNegativaPositivaPublicaDt = (CertidaoNegativaPositivaPublicaDt) request.getSession().getAttribute("certidaoNegativaPositivaPublicaDt");
		if (certidaoNegativaPositivaPublicaDt == null) {
			certidaoNegativaPositivaPublicaDt = new CertidaoNegativaPositivaPublicaDt();
			// No caso da sessão expirar...
			certidaoNegativaPositivaPublicaDt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
			certidaoNegativaPositivaPublicaDt.setInteressePessoal("");
		}
		
		if (super.getAtributeParameter(request, "PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0";
		else PosicaoPaginaAtual = super.getAtributeParameter(request, "PosicaoPaginaAtual");
		
		// Pega valor digitado na caixa de paginação
		if (super.getAtributeParameter(request, "PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(super.getAtributeParameter(request, "PosicaoPagina"))) - 1);
		
		request.setAttribute("tempPrograma", "CertidaoNegativaPositivaPublica");		
		request.setAttribute("tempRetorno", "CertidaoNegativaPositivaPublica");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");		
		
		if(super.getAtributeParameter(request, "PassoBusca") != null) passoBusca = Funcoes.StringToInt(super.getAtributeParameter(request, "PassoBusca"));
		
		if(super.getAtributeParameter(request, "nomeBusca1") != null)
		   super.getAtributeParameter(request, "nomeBusca1");
		
		certidaoNegativaPositivaPublicaDt.setInteressePessoal(super.getAtributeParameter(request, "InteressePessoal"));
		certidaoNegativaPositivaPublicaDt.setNome(super.getAtributeParameter(request, "Nome"));
		certidaoNegativaPositivaPublicaDt.setCpfCnpj(super.getAtributeParameter(request, "Cpf"));
		certidaoNegativaPositivaPublicaDt.setNomeMae(super.getAtributeParameter(request, "NomeMae"));
		certidaoNegativaPositivaPublicaDt.setDataNascimento(super.getAtributeParameter(request, "DataNascimento"));
		if (super.getAtributeParameter(request, "TipoArea") != null &&	(super.getAtributeParameter(request, "TipoArea").equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)) || super.getAtributeParameter(request, "TipoArea").equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL)))){		
			certidaoNegativaPositivaPublicaDt.setAreaCodigo(super.getAtributeParameter(request, "TipoArea"));
			if(certidaoNegativaPositivaPublicaDt.getAreaCodigo().equals(String.valueOf(AreaDt.CRIMINAL))) {
				certidaoNegativaPositivaPublicaDt.setTerritorio("E");
			}
		}
		certidaoNegativaPositivaPublicaDt.setNumeroGuiaCertidao(super.getAtributeParameter(request, "NumeroDoRequerimento"));
		if (super.getAtributeParameter(request, "Territorio") != null && (super.getAtributeParameter(request, "Territorio").equalsIgnoreCase("") || super.getAtributeParameter(request, "Territorio").equalsIgnoreCase("E") || super.getAtributeParameter(request, "Territorio").equalsIgnoreCase("C")))		
			certidaoNegativaPositivaPublicaDt.setTerritorio(super.getAtributeParameter(request, "Territorio"));
		if (super.getAtributeParameter(request, "Finalidade") != null)		
			certidaoNegativaPositivaPublicaDt.setFinalidade(super.getAtributeParameter(request, "Finalidade"));
		if (super.getConteudoArquivoBytes(request) != null && super.getConteudoArquivoBytes(request).length > 0) {
			certidaoNegativaPositivaPublicaDt.setDocumento(super.getConteudoArquivoBytes(request));
			certidaoNegativaPositivaPublicaDt.setNomeDocumento(super.getNomeArquivo(request));
		}			
		
		if (certidaoNegativaPositivaPublicaDt.isCivel()) {
			tituloPagina = "Cível";
		} else if (certidaoNegativaPositivaPublicaDt.isCriminal()) {
			tituloPagina = "Criminal";
		}
		
		switch (paginaatual) {	
			case 1:
				executeAcaoNovo(request, certidaoNegativaPositivaPublicaDt);								
				break;
			case 3: 
				mensagem = certidaoNe.VerificarCertidaoNegativaPositivaPublica(certidaoNegativaPositivaPublicaDt, true); 
				if (mensagem != null && mensagem.trim().length() > 0) {
					request.setAttribute("MensagemErro",  mensagem);
				} else {
					//exibirCaptcha(request,"CertidaoNegativaPositivaPublica","CertidaoNegativaPositivaPublica","CertidaoNegativaPositivaPublica");		
					if(emitirCertidaoNegativaPositiva(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)){	
						return;
					} 				
				}
				break;
			case 4: 
				mensagem = certidaoNe.VerificarCertidaoNegativaPositivaPublicaGuia(certidaoNegativaPositivaPublicaDt); 
				if (mensagem != null && mensagem.trim().length() > 0) {
					request.setAttribute("MensagemErro",  mensagem);
				} else if(emitirCertidaoNegativaPositiva(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)){	
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
					request.setAttribute("tempRetorno", "CertidaoNegativaPositivaPublica");
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
					if(emitirCertidaoNegativaPositiva(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)){	
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
		request.setAttribute("tempTituloPagina", tituloPagina);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

	private void executeAcaoNovo(HttpServletRequest request, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		certidaoNegativaPositivaPublicaDt.limpar();			
		request.getSession().setAttribute("TipoConsulta", "Publica");
		request.getSession().setAttribute("CodigoCaptcha", "1");
	}
	
	protected boolean emitirCertidaoNegativaPositiva(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, CertidaoNe certidaoNe, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws MensagemException, Exception {
		if(super.checkRecaptcha(request)){	
			if (CertidaoDt.isGratuitaEmissaoPrimeiroGrau || certidaoNegativaPositivaPublicaDt.getInteressePessoal().equalsIgnoreCase("S")) {
				if (emitirCertidaoInteressePessoal(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)) return true;
			} else if (certidaoNegativaPositivaPublicaDt.getInteressePessoal().equalsIgnoreCase("N")){
				if (emitirCertidaoComGuia(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaPublicaDt)) return true;
			} else {
				super.redireciona(response, "LogOn?PaginaAtual=-200");
				return true;
			}						
		} else {
			executeAcaoNovo(request, certidaoNegativaPositivaPublicaDt);			
		}
		return false;
	}

	protected boolean emitirCertidaoInteressePessoal(HttpServletRequest request, HttpServletResponse response,
			                                       UsuarioNe UsuarioSessao, CertidaoNe certidaoNe,
			                                       CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws MensagemException, Exception {
		ModeloDt modeloDt;
		List processoLista = null;
		List processoListaAverbacaoCustas = null;
		
		if (CertidaoDt.isGratuitaEmissaoPrimeiroGrau || certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E")) {
			try	{
				List<String> listaNomesComarcasComProcessos = certidaoNe.consultarProcessosPrimeiroGrauCertidaoNP(certidaoNegativaPositivaPublicaDt);
				certidaoNegativaPositivaPublicaDt.setListaNomesComarcasComProcessos(listaNomesComarcasComProcessos);	
			} catch(Exception e) {								
				throw new MensagemException("Erro ao acessar a base de dados do Projudi, favor tentar novamente mais tarde!\n" + e.toString() );
			}								
			
			if (certidaoNegativaPositivaPublicaDt.getListaNomesComarcasComProcessos().isEmpty()){
				try	{
					List<String> listaNomesComarcasComProcessoSPGPublica = certidaoNe.getListaNomesComarcasComProcessoSPGPublica(certidaoNegativaPositivaPublicaDt);
					certidaoNegativaPositivaPublicaDt.addListaNomesComarcasComProcessos(listaNomesComarcasComProcessoSPGPublica);	
				} catch(Exception e) {									
					throw new MensagemException("Erro ao acessar o sistema SPG, favor tentar novamente mais tarde!\n" + e.toString() );
				}
			}
			
		} else {			
			CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaDtConsulta = new CertidaoNegativaPositivaPublicaDt();
			certidaoNegativaPositivaDtConsulta.setNome(certidaoNegativaPositivaPublicaDt.getNome());
			certidaoNegativaPositivaDtConsulta.setCpfCnpj(certidaoNegativaPositivaPublicaDt.getCpfCnpj());
			certidaoNegativaPositivaDtConsulta.setNomeMae(certidaoNegativaPositivaPublicaDt.getNomeMae());
			certidaoNegativaPositivaDtConsulta.setDataNascimento(certidaoNegativaPositivaPublicaDt.getDataNascimento());
			certidaoNegativaPositivaDtConsulta.setAreaCodigo(certidaoNegativaPositivaPublicaDt.getAreaCodigo());
			certidaoNegativaPositivaDtConsulta.setId_Comarca(certidaoNegativaPositivaPublicaDt.getId_Comarca());
			certidaoNegativaPositivaDtConsulta.setComarcaCodigo(certidaoNegativaPositivaPublicaDt.getComarcaCodigo());
			certidaoNegativaPositivaDtConsulta.setComarca(certidaoNegativaPositivaPublicaDt.getComarca());
			
			processoLista = certidaoNe.listarProcessoCertidaoNP(certidaoNegativaPositivaDtConsulta);
			certidaoNegativaPositivaDtConsulta.setListaProcessos(processoLista);
			
			if (processoLista.size() == 0) {
				try{
					certidaoNegativaPositivaDtConsulta = (CertidaoNegativaPositivaPublicaDt) certidaoNe.getListaProcessoSPGPublica(certidaoNegativaPositivaDtConsulta);
				} catch(Exception e) {
					if(e.getMessage().split("ErroSPG=").length > 1){
						String mensagemErro = e.getMessage().split("ErroSPG=")[1];
						//Algumas mensagens do SPG estão vindo com aspas simples, o que
						//está causando erro no momento de mostrar a mensagem de erro.
						mensagemErro = mensagemErro.replaceAll("'", "");
						throw new MensagemException(mensagemErro);
					} else {
						throw new MensagemException("Erro ao acessar o sistema SPG, favor tentar novamente mais tarde!\n" + e.toString() );
					}			
				}	
			}			
			
			processoLista = certidaoNegativaPositivaDtConsulta.getListaProcessos();
		}
		
		
		if (certidaoNegativaPositivaPublicaDt.getListaNomesComarcasComProcessos().isEmpty() && 
			(processoLista == null || processoLista.size() == 0) &&
			certidaoNegativaPositivaPublicaDt.getAreaCodigo() != null && 
			certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().length() > 0 &&
			Funcoes.StringToInt(certidaoNegativaPositivaPublicaDt.getAreaCodigo()) == Funcoes.StringToInt(AreaDt.CIVEL)) {
				
			CertidaoNegativaPositivaPublicaDt certidao = new CertidaoNegativaPositivaPublicaDt();
			certidao.setAreaCodigo(certidaoNegativaPositivaPublicaDt.getAreaCodigo());
			certidao.setId_Comarca(certidaoNegativaPositivaPublicaDt.getId_Comarca());
			certidao.setComarcaCodigo(certidaoNegativaPositivaPublicaDt.getComarcaCodigo());
			certidao.setCpfCnpj(certidaoNegativaPositivaPublicaDt.getCpfCnpj());
			certidao.setNome(certidaoNegativaPositivaPublicaDt.getNome());
			
			processoListaAverbacaoCustas = certidaoNe.getProcessoAverbacaoCusta(certidao);
		}
		
		if (certidaoNegativaPositivaPublicaDt.getListaNomesComarcasComProcessos().isEmpty() && (processoLista == null || processoLista.size() == 0) && (processoListaAverbacaoCustas == null || processoListaAverbacaoCustas.size() == 0))	{
			if (certidaoNegativaPositivaPublicaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL))) {
				if (CertidaoDt.isGratuitaEmissaoPrimeiroGrau) {
					modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_CIVEL_FISICA_PUBLICA_GRATUITA_MODELO_CODIGO);
				} else {
					modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_CIVEL_FISICA_PUBLICA_MODELO_CODIGO);	
				}				
			} else {
				modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_CRIMINAL_FISICA_PUBLICA_MODELO_CODIGO);
			}				
									
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNe.montaModelo(certidaoNegativaPositivaPublicaDt, modeloDt, UsuarioSessao));
			
			// Preenchendo o nome da comarca e a finalidade...
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNegativaPositivaPublicaDt.getTexto().replace(CertidaoNegativaPositivaPublicaDt.TAG_NOME_COMARCA_TITULO, obtenhaNomeDaComarcaTitulo(certidaoNegativaPositivaPublicaDt)));
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNegativaPositivaPublicaDt.getTexto().replace(CertidaoNegativaPositivaPublicaDt.TAG_FINALIDADE, obtenhaFinalidade(certidaoNegativaPositivaPublicaDt)));
			certidaoNegativaPositivaPublicaDt.setTexto(certidaoNegativaPositivaPublicaDt.getTexto().replace(CertidaoNegativaPositivaPublicaDt.TAG_NOME_COMARCA_TEXTO, obtenhaNomeDaComarcaTexto(certidaoNegativaPositivaPublicaDt)));
			
			TJDataHora tjdathoraVenciamento = new TJDataHora();							
			tjdathoraVenciamento.adicioneDia(30);
			// Gerar o id da certidão...
			CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(new String().getBytes(), tjdathoraVenciamento.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
			cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			cdt.setCodigoTemp(CertidaoValidacaoDt.CERTIDAO_PUBLICA_NADA_CONSTA);
			cdt.setId_Modelo(modeloDt.getId());
			cdt.setDocumento(certidaoNegativaPositivaPublicaDt.getDocumento());
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
	
	protected String obtenhaNomeDaComarcaTitulo(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		if (certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E") || certidaoNegativaPositivaPublicaDt.getComarca().trim().length() == 0)
			return "TODAS AS COMARCAS";
		return "COMARCA DE " + certidaoNegativaPositivaPublicaDt.getComarca().toUpperCase();
	}
	
	protected String obtenhaNomeDaComarcaTexto(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		if (certidaoNegativaPositivaPublicaDt.getTerritorio().trim().equalsIgnoreCase("E") || certidaoNegativaPositivaPublicaDt.getComarca().trim().length() == 0)
			return "";
		return ", na COMARCA DE " + certidaoNegativaPositivaPublicaDt.getComarca().toUpperCase() + ", ";
	}
	
	protected String obtenhaFinalidade(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) {
		int finalidade = Funcoes.StringToInt(certidaoNegativaPositivaPublicaDt.getFinalidade());
		
		switch (finalidade) {	
			case CertidaoNe.DEFESA_DIREITOS: {
				return "Exclusivamente para Defesa de Direitos";									
			}
			case CertidaoNe.CONCURSO_PUBLICO: {
				return "Exclusivamente para Concurso Público";				
			}
			case CertidaoNe.CONTRATACAO_EMPREGO: {
				return "Exclusivamente para Contratação de Emprego";				
			}
			case CertidaoNe.ELEITORAL: {
				return "Exclusivamente para fim Eleitoral";				
			}
			case CertidaoNe.MILITAR: {
				return "Exclusivamente para fim Militar";				
			}
			case CertidaoNe.ESCLARECIMENTO_SITUACOES_INTERESSE_PESSOAL: {
				return "Exclusivamente para Esclarecimento de Situações de Interesse Pessoal";				
			}
			default: {
				return "";
			}
		}
	}
	
	protected boolean emitirCertidaoComGuia(HttpServletRequest request, HttpServletResponse response,
                                          UsuarioNe UsuarioSessao, CertidaoNe certidaoNe,
                                          CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws MensagemException, Exception {
		
		// Se já foi emitida certidão com a guia informada, devolvemos a certidão já emitida...
		if (certidaoNegativaPositivaPublicaDt.getCertidaoEmitida() != null) {
			String nome = "Certidao" + request.getSession().getAttribute("codigo");	    	
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
			throw new MensagemException("Guia não encontrada, verifique o número! \nDigite somente o número sem a série, exemplo guia NÚMERO: 18680149 - 1, favor digitar: 186801491.\n");			
		}
		certidaoNegativaPositivaDt.setGuiaEmissaoCertidao(certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao());
		certidaoNegativaPositivaDt.setNumeroGuia(numeroGuia);
		processoLista = certidaoNe.listarProcessoCertidaoNP(certidaoNegativaPositivaDt);
		certidaoNegativaPositivaDt.setListaProcessos(processoLista);	
		
		if (certidaoNegativaPositivaDt.getListaProcessos().size() == 0)	{
			try{
				certidaoNegativaPositivaDt = certidaoNe.getListaProcessoSPG(certidaoNegativaPositivaDt);
			} catch(Exception e) {
				if(e.getMessage().split("ErroSPG=").length > 1){
					String mensagemErro = e.getMessage().split("ErroSPG=")[1];
					//Algumas mensagens do SPG estão vindo com aspas simples, o que
					//está causando erro no momento de mostrar a mensagem de erro.
					mensagemErro = mensagemErro.replaceAll("'", "");
					throw new MensagemException(mensagemErro);
				} else {
					throw new MensagemException("Erro ao acessar o sistema SPG, favor tentar novamente mais tarde!\n" + e.toString() );
				}			
			}	
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
					modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_CIVEL_FISICA_PUBLICA_GUIA_MODELO_CODIGO);	
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
				
				// Salvar data de apresentação no SPG aqui...				
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
	
	protected void TenteRetirarDataDeApresentacao(CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaPublicaDt) throws Exception {

		if (certidaoNegativaPositivaPublicaDt != null) {
			if (certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao() != null) {
				new GuiaSPGNe().retireDataApresentacao(certidaoNegativaPositivaPublicaDt.getGuiaEmissaoCertidao().getNumeroGuiaCompleto());		
			} else {
				new GuiaSPGNe().retireDataApresentacao(certidaoNegativaPositivaPublicaDt.getNumeroGuiaCertidao().replaceAll(" ", "") + "09");
			}
		}			
		
	}

	@Override
	public int Permissao() {
		return 863;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
	private String ObtenhaAcaoJspEmissaoCertidao() {
		if (CertidaoDt.isGratuitaEmissaoPrimeiroGrau) {
			return "/WEB-INF/jsptjgo/CertidaoNegativaPositivaPublicaGratuita.jsp";
		} else {
			return "/WEB-INF/jsptjgo/CertidaoNegativaPositivaPublica.jsp";
		}
	}
}
