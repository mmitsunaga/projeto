package br.gov.go.tj.projudi.ct.publicos;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoSegundoGrauNegativaPositivaPublicaPJCt extends Controle {
	
	private static final long serialVersionUID = -3767491766355531344L;
	
	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
			
		String PosicaoPaginaAtual = "";
		String posicaoPagina= "";
		int passoBusca = 0;		
		CertidaoNe certidaoNe;		
		CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaDt;		
					
		response.setContentType("text/html");
        response.setCharacterEncoding("iso-8859-1");
        
		String stAcao = "/WEB-INF/jsptjgo/CertidaoSegundoGrauNegativaPositivaPublicaPJ.jsp";
		
		certidaoNe = (CertidaoNe) request.getSession().getAttribute("CertidaoNe");
		if (certidaoNe == null) certidaoNe = new CertidaoNe();	
		
		certidaoNegativaPositivaDt = (CertidaoNegativaPositivaPublicaDt) request.getSession().getAttribute("certidaoNegativaPositivaDt");
		if (certidaoNegativaPositivaDt == null) certidaoNegativaPositivaDt = new CertidaoNegativaPositivaPublicaDt();
		
		if (request.getParameter("PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0";
		else PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
		
		// Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		
		request.setAttribute("tempPrograma", "CertidaoSegundoGrauNegativaPositivaPublicaPJ");		
		request.setAttribute("tempRetorno", "CertidaoSegundoGrauNegativaPositivaPublicaPJ");
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");		
		
		if(request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		if(request.getParameter("nomeBusca1") != null)
			request.getParameter("nomeBusca1");
		
		certidaoNegativaPositivaDt.setNome(request.getParameter("RazaoSocial"));
		certidaoNegativaPositivaDt.setCpfCnpj(request.getParameter("Cnpj"));
		certidaoNegativaPositivaDt.setEhPessoaJuridica(true);
		if (request.getParameter("TipoArea") != null &&			
		   (request.getParameter("TipoArea").equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)) || request.getParameter("TipoArea").equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))))		
				certidaoNegativaPositivaDt.setAreaCodigo(request.getParameter("TipoArea"));
		
		switch (paginaatual) {	
			case 1:
				executeAcaoInicial(request, certidaoNegativaPositivaDt);
				break;
			case 3: 
				String mensagem = certidaoNe.VerificarCertidaoNegativaPositivaPublicaPJ(certidaoNegativaPositivaDt, false); 
				if (mensagem != null && mensagem.trim().length() > 0) {
					request.setAttribute("MensagemErro",  mensagem);
				} else if (gereCertidao(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaDt)) {	
					return;								
				}
				break;			
			default:
				if (passoBusca == 3) {
					if (gereCertidao(request, response, UsuarioSessao, certidaoNe, certidaoNegativaPositivaDt)) {	
						return;
					}
				}
				break;
		}
		
		request.setAttribute("PosicaoPagina", Funcoes.StringToInt(posicaoPagina+1));		
		request.getSession().setAttribute("CertidaoNe", certidaoNe);	
		request.getSession().setAttribute("certidaoNegativaPositivaDt", certidaoNegativaPositivaDt);
		request.setAttribute("PaginaAtual", paginaatual);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}

	private boolean gereCertidao(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, CertidaoNe certidaoNe, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaDt) throws Exception {
		ModeloDt modeloDt;
		if(super.checkRecaptcha(request)){						
			boolean negativado = false;
			
			// verificar se Existe processos no Projudi
			negativado = certidaoNe.negativarPFisicaPJuricaSegundoGrau(certidaoNegativaPositivaDt);
																	
			//Se não Encontrou processos no Projudi verifica no SSG
			if (negativado)	{								
				negativado = certidaoNe.negativarPFisicaPJuricaSSG(certidaoNegativaPositivaDt);								
			}
			
			//Se não Encontrou processos no Projudi e no SSG, gera a certidão
			if (negativado) {
				if (certidaoNegativaPositivaDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL)))
					modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_2G_CIVEL_JURIDICA_PUBLICA_MODELO_CODIGO);	
				else 
					modeloDt = certidaoNe.consultarModeloCodigo("" + CertidaoNegativaPositivaDt.NEGATIVA_2G_CRIMINAL_JURIDICA_PUBLICA_MODELO_CODIGO);
										
				certidaoNegativaPositivaDt.setTexto(certidaoNe.montaModelo(certidaoNegativaPositivaDt, modeloDt, UsuarioSessao));								
											
				TJDataHora tjdathoraVenciamento = new TJDataHora();							
				tjdathoraVenciamento.adicioneDia(30);
				// Gerar o id da certidão...
				CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(new String().getBytes(), tjdathoraVenciamento.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
				cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				cdt.setCodigoTemp(CertidaoValidacaoDt.CERTIDAO_PUBLICA_NADA_CONSTA);
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
				request.setAttribute("MensagemErro",  "<b>Atenção:</b> Essa certidão não pode ser emitida de forma automática.\nEm atendimento ao Decreto Judiciário nº 585/2020 as solicitações devem ser encaminhadas ao endereço eletrônico (email) distribuicaotj@tjgo.jus.br.");								
			}
				
		} else {					
			executeAcaoInicial(request, certidaoNegativaPositivaDt);						
		}
		return false;
	}

	private void executeAcaoInicial(HttpServletRequest request, CertidaoNegativaPositivaPublicaDt certidaoNegativaPositivaDt) {
		certidaoNegativaPositivaDt.limpar();			
		request.getSession().setAttribute("TipoConsulta", "Publica");
		request.getSession().setAttribute("CodigoCaptcha", "1");
	}

	@Override
	public int Permissao() {
		return 867;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
}
