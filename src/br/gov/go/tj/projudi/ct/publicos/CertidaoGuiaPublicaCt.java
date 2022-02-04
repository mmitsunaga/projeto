package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.CertidaoGuiaCt;
import br.gov.go.tj.projudi.dt.CertidaoGuiaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.ne.CertidaoGuiaNe;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Certificado.Signer;

public class CertidaoGuiaPublicaCt extends CertidaoGuiaCt {

	private static final long serialVersionUID = -8951062772377880201L;

	public int Permissao() {
		return CertidaoGuiaDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		CertidaoGuiaDt certidaoGuiaDt;
		CertidaoGuiaNe certidaoGuiaNe;
		
		String numeroGuia = "";
		
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "CertidaoGuiaPublica");
		request.setAttribute("TituloPagina", "Gerar Certidão");
		request.setAttribute("tempPrograma", "Gerar Certidão");
		request.setAttribute("tempNomeBusca", "Gerar Certidão");
		request.setAttribute("PaginaAnterior", paginaatual);
		String stAcao = "/WEB-INF/jsptjgo/GerarCertidaoPublica.jsp";
		
		certidaoGuiaDt = (CertidaoGuiaDt) request.getSession().getAttribute("certidaoGuiaDt");
		if (certidaoGuiaDt == null){
			certidaoGuiaDt = new CertidaoGuiaDt();
		}
				
		certidaoGuiaNe = (CertidaoGuiaNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoGuiaNe == null){
			certidaoGuiaNe = new CertidaoGuiaNe();
		}
		
		if(request.getParameter("numeroGuia") != null){
			if(certidaoGuiaDt.getNumeroGuia() == null || certidaoGuiaDt.getNumeroGuia().isEmpty()){
				certidaoGuiaDt.setNumeroGuia(request.getParameter("numeroGuia"));
			}
		}

		switch (paginaatual) {
			case Configuracao.Novo:
				
				request.getSession().removeAttribute("certidaoGuiaDt");				
				request.setAttribute("tempRetorno", "CertidaoGuiaPublica");
				certidaoGuiaDt = new CertidaoGuiaDt();	
				break;

			// Localizar Guia
			case Configuracao.Localizar:				
				
				numeroGuia = request.getParameter("numeroGuia");
								
				if(numeroGuia != null) {					
					if(!numeroGuia.isEmpty()) {						
						stAcao = "/WEB-INF/jsptjgo/GerarCertidaoPublica.jsp";		
					}
				}
				else if(request.getParameter("numeroGuia2") != null) {
					
					numeroGuia = (String) request.getParameter("numeroGuia2");
					request.getSession().setAttribute("numeroGuia", numeroGuia);
				}
				break;
				
			case Configuracao.Curinga9:
				
				if(request.getSession().getAttribute("numeroGuia") == null){
					request.setAttribute("MensagemErro", "Número da Guia não encontrado. Favor refazer o procedimento.");
					break;
				}
				
				numeroGuia = (String) request.getSession().getAttribute("numeroGuia");
				request.getSession().removeAttribute("numeroGuia");
				
				numeroGuia = numeroGuia.trim().replace("-", "").replace("/", "");					
				certidaoGuiaDt = certidaoGuiaNe.consultarCertidaoProjudi(numeroGuia);
				
				CertidaoValidacaoDt cert = new CertidaoNe().consultarNumeroGuia(numeroGuia);
				
				if(certidaoGuiaDt == null) {
					request.setAttribute("numeroGuiaOk", "inexistente");
					certidaoGuiaDt = new CertidaoGuiaDt();
					certidaoGuiaDt.setNumeroGuia(numeroGuia);
				}
				else {
					if( !certidaoGuiaNe.isGuiaPaga(numeroGuia) ) { // Verifica se a guia foi paga.						
						request.setAttribute("numeroGuiaOk", "nao_paga");
						certidaoGuiaDt = new CertidaoGuiaDt();
						certidaoGuiaDt.setNumeroGuia(numeroGuia);
					}
					else{
						if( cert == null ) { // Verifica se a certidão foi emitida ou não.
							request.setAttribute("numeroGuiaOk", "certidao_nao_emitida");
							certidaoGuiaDt = new CertidaoGuiaDt();
							certidaoGuiaDt.setNumeroGuia(numeroGuia);
						}
						else {	
							byte[] byTemp = null;
							Signer.acceptSSL();						
							CertidaoValidacaoDt certidao = new CertidaoNe().consultarNumeroGuia(numeroGuia);						
							byTemp = new CertidaoNe().gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , certidao);							
							enviarPDF(response, byTemp, "Certidao");						
							byTemp = null;
							return;
						}
					}
				}
				break;
		}// fim do switch
		
		request.getSession().setAttribute("certidaoGuiaDt", certidaoGuiaDt);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}	
	protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}
}