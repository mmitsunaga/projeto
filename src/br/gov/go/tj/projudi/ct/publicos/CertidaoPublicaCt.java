package br.gov.go.tj.projudi.ct.publicos;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CertidaoPublicaCt extends Controle {

	private static final long serialVersionUID = 1349058215117364085L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
		
		String stIdArquivo = "";		
		String PosicaoPaginaAtual = "";
		String posicaoPagina= "";
		int passoBusca = 0;		
		CertidaoNe certidaoNe;
		byte[] byTemp = null;		
				
		//stIdArquivo = (String) request.getSession().getAttribute("validacaoId_Arquivo");
		
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
        
		String stAcao = "/WEB-INF/jsptjgo/Branco.html";
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("CertidaoNe");
		if (certidaoNe == null) certidaoNe = new CertidaoNe();	
		
		if (request.getParameter("PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0";
		else PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
		
		// Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		
		request.setAttribute("tempPrograma", "CertidaoPublica");		
		request.setAttribute("tempRetorno", "CertidaoPublica");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		/**
		 * Variável PassoBusca utilizada para auxiliar na busca		  
		 * PassoBusca 3 : Valida Captcha e caso usuario tenha digitado corretamente mostra a certidão  
		 */
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		switch (paginaatual) {	
			case Configuracao.LocalizarDWR: 
				String codValidacao = request.getParameter("codCertidao");
				stIdArquivo = Cifrar.decodificarId_Certidao(codValidacao);
				//request.getSession().setAttribute("codigo", codValidacao);

				if(Funcoes.StringToInt(stIdArquivo) == -1){
					stAcao = "/WEB-INF/jsptjgo/Erro.jsp";
					request.setAttribute("Mensagem",  "Código Inválido, não foi possivel validar publicação.");
					
				}else {
					if (stIdArquivo != null && stIdArquivo.length() > 0) {
//						request.getSession().setAttribute("validacaoId_Arquivo", stIdArquivo);
//						request.setAttribute("action", "CertidaoPublica");
//						stAcao = "/WEB-INF/jsptjgo/ValidarCertidaoPublico.jsp";
						if (super.checkRecaptcha(request)) {
						    CertidaoValidacaoDt certidaoValidacao = certidaoNe.consultarId(stIdArquivo);									    
						    if (certidaoValidacao != null) {
								String nome ="Certidao" + request.getSession().getAttribute("codigo");								    	
						    	// gerar pdf como arquivos da publicação
							    byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), certidaoValidacao);									    
							    enviarPDF(response, byTemp, nome);									    										
							    return;
						    } else{
						    	request.setAttribute("MensagemErro",  "Certidão inexistente.");
						    }
							
						} else {
							stAcao = "/WEB-INF/jsptjgo/ValidarCertidaoPublico.jsp";
						}
					}
				}
				break;			
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stAcao = "/WEB-INF/jsptjgo/ValidarCertidaoPublico.jsp";
//				switch (passoBusca) {					
//					case 0:				
//						request.getSession().setAttribute("CodigoCaptcha", "1");				
//						request.setAttribute("PaginaAtual", paginaatual);
//						break;
//					case 2: //Redireciona para tela de catpcha
//						if (stIdArquivo != null && stIdArquivo.length() > 0) {
//							request.getSession().setAttribute("validacaoId_Arquivo", stIdArquivo);
//							request.setAttribute("action", "CertidaoPublica");
//							stAcao = "/WEB-INF/jsptjgo/ValidarCertidaoPublico.jsp";
//						}
//						break;
//					case 3:
//						if (stIdArquivo != null && !stIdArquivo.equalsIgnoreCase("")) {
//							if (super.checkRecaptcha(request)) {
//							    CertidaoValidacaoDt certidaoValidacao = certidaoNe.consultarId(stIdArquivo);									    
//							    if (certidaoValidacao != null) {
//									String nome ="Certidao" + request.getSession().getAttribute("codigo");								    	
//							    	// gerar pdf como arquivos da publicação
//								    byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), certidaoValidacao);									    
//								    enviarPDF(response, byTemp, nome);									    										
//								    return;
//							    } else{
//							    	request.setAttribute("MensagemErro",  "Certidão inexistente.");
//							    }
//								
//							} else {
//								stAcao = "/WEB-INF/jsptjgo/ValidarCertidaoPublico.jsp";
//							}
//						}						
//						break;
//				}
		}
		
		request.setAttribute("PosicaoPagina", Funcoes.StringToInt(posicaoPagina+1));		
		request.getSession().setAttribute("CertidaoNe", certidaoNe);		
		request.setAttribute("PassoBusca", passoBusca);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

	@Override
	public int Permissao() {
		return 865;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}

}
