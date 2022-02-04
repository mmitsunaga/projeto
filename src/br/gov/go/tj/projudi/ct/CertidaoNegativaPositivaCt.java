package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.GuiaSPGNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoNegativaPositivaCt extends Controle {


	/**
	 * 
	 */
	private static final long serialVersionUID = 8780793487744036188L;

	public int Permissao() {
		return  CertidaoNegativaPositivaDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		CertidaoNegativaPositivaDt certidaoNegativaPositivaDt;
		CertidaoNe certidaoNe;
		ModeloDt modeloDt;
		String stAcao;
		List processoLista;
		String posicaoLista = "";
	
		
		certidaoNe = new CertidaoNe();

		certidaoNegativaPositivaDt = (CertidaoNegativaPositivaDt) request.getSession().getAttribute("certidaoNegativaPositivaDt");
		if (certidaoNegativaPositivaDt == null)
			certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();

		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null)
			modeloDt = new ModeloDt();
		
	
		request.setAttribute("MensagemErro","");
	
		
		request.setAttribute("TituloPagina", "Certidão Negativa/Positiva");
		request.setAttribute("tempPrograma", "CertidaoNegativaPositiva");
		request.setAttribute("tempNomeBusca", "CertidaoNegativaPositiva");
		request.setAttribute("tempRetorno", "CertidaoNegativaPositiva");
		request.setAttribute("PaginaAnterior", paginaatual);
	
		certidaoNegativaPositivaDt.setNumeroGuia(request.getParameter("NumeroGuia"));
		
		
		stAcao = "/WEB-INF/jsptjgo/CertidaoNegativaPositiva.jsp";
		
		certidaoNegativaPositivaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certidaoNegativaPositivaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
		
		case Configuracao.Localizar:
			if(!certidaoNegativaPositivaDt.getNumeroGuia().equals("")) {
				String numeroGuia = certidaoNegativaPositivaDt.getNumeroGuia().replaceAll(" ", "");
				try{
					certidaoNegativaPositivaDt = certidaoNe.getDtGuia(numeroGuia);
				}catch(Exception e) {
					request.setAttribute("MensagemErro","Falha na comunicação com SPG, tente novamente em alguns minutos!");
					certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();
					certidaoNegativaPositivaDt.setNumeroGuia(numeroGuia);
					modeloDt = new ModeloDt();
					break;
				}
				if (certidaoNegativaPositivaDt ==null) {
					request.setAttribute("MensagemErro","Guia não encontrada, verifique o número!");
					certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();
					certidaoNegativaPositivaDt.setNumeroGuia(numeroGuia);
					modeloDt = new ModeloDt();
					break;
				}
				certidaoNegativaPositivaDt.setNumeroGuia(numeroGuia);
				processoLista = certidaoNe.consultarProcessosPrimeiroGrauCertidaoNP(certidaoNegativaPositivaDt);
				certidaoNegativaPositivaDt.setListaProcessos(processoLista);
				
				try{
					certidaoNegativaPositivaDt = certidaoNe.getListaProcessoSPG(certidaoNegativaPositivaDt);
				} catch(Exception e) {
					if(e.getMessage().split("ErroSPG=").length > 1){
						String mensagemErro = e.getMessage().split("ErroSPG=")[1];
						//Algumas mensagens do SPG estão vindo com aspas simples, o que
						//está causando erro no momento de mostrar a mensagem de erro.
						mensagemErro = mensagemErro.replaceAll("'", "");
						request.setAttribute("MensagemErro", mensagemErro);
					} else {
						request.setAttribute("MensagemErro", "Falha na comunicação com spg, favor tentar mais tarde");
					}
					certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();
					certidaoNegativaPositivaDt.setNumeroGuia(numeroGuia);
					modeloDt = new ModeloDt();
					break;
				}
				
				certidaoNegativaPositivaDt.getValorCertidao();
				
				// Preenchendo a data de pagamento...				
				GuiaEmissaoDt guiaEmissaoDt = new GuiaSPGNe().consultarGuiaEmissaoSPG(numeroGuia + "09");
				if (guiaEmissaoDt != null) {
					
					// TODO: Esse If é para uma situação específica.
					// Excluir após a emissão da certidão negativa
					if (!guiaEmissaoDt.getNumeroGuiaCompleto().equals("19307301309")) {
						
						// Somente considerar a data de Recebimento da guia se ....
						if (guiaEmissaoDt.getDataRecebimento() == null && new GuiaEmissaoNe().isGuiaZeradaOuNegativa(guiaEmissaoDt.getListaGuiaItemDt())) {
							throw new MensagemException("Não foi identificado o pagamento dessa guia.");
							
						} else {
							TJDataHora dataRecebimento = new TJDataHora();
							if( guiaEmissaoDt.getDataRecebimento() != null ) {
								dataRecebimento.setDataaaaa_MM_ddHHmmss(guiaEmissaoDt.getDataRecebimento());
							}
							certidaoNegativaPositivaDt.setDataPagamento(dataRecebimento.getDataFormatadaddMMyyyy());	
						}						
					}
					
				}				
				
				int modeloCodigo = getModeloCodigo(certidaoNegativaPositivaDt);
				modeloDt = certidaoNe.consultarModeloCodigo("" +modeloCodigo);
				certidaoNegativaPositivaDt.setTexto(certidaoNe.montaModelo(certidaoNegativaPositivaDt, modeloDt, UsuarioSessao));				
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("tempRetorno", "CertidaoNegativaPositiva");		
		
			} else {
				request.setAttribute("MensagemErro","Informe o número da guia!");
				certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();
				modeloDt = new ModeloDt();
			}
			break;
		case Configuracao.Novo:		
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoNegativaPositiva");
			stAcao = "/WEB-INF/jsptjgo/CertidaoNegativaPositiva.jsp";
			certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();
		
			
			modeloDt = new ModeloDt();
			break;
		case Configuracao.LocalizarAutoPai:
			posicaoLista = request.getParameter("posicaoLista");
			if (posicaoLista.length() > 0) {
				int posicao = Funcoes.StringToInt(posicaoLista);
				certidaoNegativaPositivaDt.removeProcesso(posicao);
				int modeloCodigo = getModeloCodigo(certidaoNegativaPositivaDt);
				modeloDt = certidaoNe.consultarModeloCodigo("" +modeloCodigo);
				certidaoNegativaPositivaDt.setTexto(certidaoNe.montaModeloMemoria(certidaoNegativaPositivaDt, modeloDt, UsuarioSessao));
			}
			if (certidaoNegativaPositivaDt.getListaProcessos().size() == 0) {
				
				
			}
			stAcao = "/WEB-INF/jsptjgo/CertidaoNegativaPositiva.jsp";
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoNegativaPositiva");
			break;
			
		case Configuracao.Imprimir: {
			
			if (certidaoNegativaPositivaDt == null || certidaoNegativaPositivaDt.getTexto().equals("")) {
				request.setAttribute("MensagemErro", "Informe o número da guia!\nE busque os processos!");
			} else {
				byte[] byTemp = null;
				Signer.acceptSSL();
				//ByteArrayOutputStream baos = ConverterHtmlPdf.convert(certidaoNegativaPositivaDt.getTexto().getBytes());
				byTemp = ConverterHtmlPdf.converteHtmlPDF(certidaoNegativaPositivaDt.getTexto().getBytes(), true);
				TJDataHora tjdathora = new TJDataHora();
				
				tjdathora.adicioneDia(30);
				CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(byTemp,tjdathora.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
				cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				certidaoNe.salvar(cdt);
				
				byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , cdt);							
				enviarPDF(response, byTemp,"Certidao");
				
				byTemp = null;
				//baos.close();

			}
			return;
		}
			
		default:
			break;
		}
		
		request.getSession().setAttribute("certidaoNegativaPositivaDt", certidaoNegativaPositivaDt);
		request.getSession().setAttribute("modeloDt", modeloDt);
		certidaoNe = null;
	
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private int getModeloCodigo(CertidaoNegativaPositivaDt cnpdt) {
		if (cnpdt.isCivel()) {
			if (cnpdt.getTipoPessoa().matches("[fF][iIíÍ][sS][iI][cC][aA]")) {
				return cnpdt.getListaProcessos().size() > 0 ? CertidaoNegativaPositivaDt.POSITIVA_CIVEL_FISICA_MODELO_CODIGO : CertidaoNegativaPositivaDt.NEGATIVA_CIVEL_FISICA_MODELO_CODIGO;
			} else {
				return cnpdt.getListaProcessos().size() > 0 ? CertidaoNegativaPositivaDt.POSITIVA_CIVEL_JURIDICA_MODELO_CODIGO : CertidaoNegativaPositivaDt.NEGATIVA_CIVEL_JURIDICA_MODELO_CODIGO;
			}
		} else {
			if (cnpdt.getTipoPessoa().matches("[fF][iIíÍ][sS][iI][cC][aA]")) {
				return cnpdt.getListaProcessos().size() > 0 ? CertidaoNegativaPositivaDt.POSITIVA_CRIMINAL_FISICA_MODELO_CODIGO : CertidaoNegativaPositivaDt.NEGATIVA_CRIMINAL_FISICA_MODELO_CODIGO;
			} else {
				return cnpdt.getListaProcessos().size() > 0 ? CertidaoNegativaPositivaDt.POSITIVA_CRIMINAL_JURIDICA_MODELO_CODIGO : CertidaoNegativaPositivaDt.NEGATIVA_CRIMINAL_JURIDICA_MODELO_CODIGO;
			}
		}
	}

}
