package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoExecucaoCPCDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoExecucaoCPCCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7476248100372783886L;

	@Override
	public int Permissao() {
		return CertidaoExecucaoCPCDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		CertidaoExecucaoCPCDt certidaoExecucaoCPCDt;
		CertidaoNe certidaoNe;
		ModeloDt modeloDt;
		String stAcao;
		
		certidaoExecucaoCPCDt = (CertidaoExecucaoCPCDt) request.getSession().getAttribute("certidaoExecucaoCPCDt");
		if (certidaoExecucaoCPCDt == null)
			certidaoExecucaoCPCDt = new CertidaoExecucaoCPCDt();
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("certidaoNe");
		if (certidaoNe == null)
			certidaoNe = new CertidaoNe();

		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null)
			modeloDt = new ModeloDt();

		certidaoExecucaoCPCDt.setNumero(request.getParameter("Numero"));
		certidaoExecucaoCPCDt.setServentia(request.getParameter("Serventia"));
		certidaoExecucaoCPCDt.setNatureza(request.getParameter("Natureza"));
		certidaoExecucaoCPCDt.setPromovente(request.getParameter("Promovente"));
		certidaoExecucaoCPCDt.setAdvogadoPromovente(request.getParameter("AdvogadoPromovente"));
		certidaoExecucaoCPCDt.setPromovido(request.getParameter("Promovido"));
		certidaoExecucaoCPCDt.setCpfCnpj(request.getParameter("CpfCnpj"));
		certidaoExecucaoCPCDt.setDataDistribuicao(request.getParameter("AreaDistribuicao"));
		certidaoExecucaoCPCDt.setValor(request.getParameter("Valor"));
		
		stAcao = "/WEB-INF/jsptjgo/CertidaoExecucaoCPC.jsp";
		switch (paginaatual) {
		
		case Configuracao.Localizar:
			if(!certidaoExecucaoCPCDt.getNumero().equals("")) {
				modeloDt = certidaoNe.consultarModeloCodigo(certidaoExecucaoCPCDt.getModeloCodigo());
				certidaoExecucaoCPCDt = certidaoNe.getProcessoExecucaoCPC(certidaoExecucaoCPCDt.getNumero()) ;
				certidaoExecucaoCPCDt.setTexto(certidaoNe.montaModelo(certidaoExecucaoCPCDt, modeloDt, UsuarioSessao));				
			
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("tempRetorno", "CertidaoExecucaoCPC");		
		
			} else {
				request.setAttribute("MensagemErro","Informe o Número do Processo!");
				certidaoExecucaoCPCDt = new CertidaoExecucaoCPCDt();
				modeloDt = new ModeloDt();
			}
			break;
		case Configuracao.Novo:		
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoExecucaoCPC");
			stAcao = "/WEB-INF/jsptjgo/CertidaoExecucaoCPC.jsp";
			certidaoExecucaoCPCDt = new CertidaoExecucaoCPCDt();
		
			
			modeloDt = new ModeloDt();
			break;
			
		case Configuracao.Imprimir: {
			
			if (certidaoExecucaoCPCDt == null || certidaoExecucaoCPCDt.getTexto().equals("")) {
				request.setAttribute("MensagemErro", "Informe o Numero do Processo!\nE gere a certidão!");
			} else {
				byte[] byTemp = null;
				Signer.acceptSSL();
				//ByteArrayOutputStream baos = ConverterHtmlPdf.convert(certidaoExecucaoCPCDt.getTexto().getBytes());
				byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoExecucaoCPCDt.getTexto().getBytes(), false);
				String nome="CertidaoDeExecucaoCPC" + certidaoExecucaoCPCDt.getNumero() ;
				enviarPDF(response, byTemp,nome);
				byTemp = null;
			}
			return;
		}
		
	default:
		break;
	}
	
	request.getSession().setAttribute("certidaoExecucaoCPCDt", certidaoExecucaoCPCDt);
	request.getSession().setAttribute("certidaoNe", certidaoNe);
	request.getSession().setAttribute("modeloDt", modeloDt);
	

	
	RequestDispatcher dis = request.getRequestDispatcher(stAcao);
	dis.include(request, response);
	

}


	}


