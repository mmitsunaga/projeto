package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CertidaoSegundoGrauNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.ne.CertidaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CertidaoSegundoGrauNegativaPositivaCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8109052931859982499L;


	public int Permissao() {
		return CertidaoSegundoGrauNegativaPositivaDt.CodigoPermissao;
	}


	public void executar(HttpServletRequest request,HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		CertidaoSegundoGrauNegativaPositivaDt certidaoSegundoGrauNegativaPositivaDt;
		CertidaoNe certidaoNe;
		ModeloDt modeloDt;
		String stAcao;
		List processoLista;
		String posicaoLista = "";
		
		request.setAttribute("MensagemErro","");
	
		
		request.setAttribute("TituloPagina", "Certidão Narrativa(Neg/Pos)");
		request.setAttribute("tempPrograma", "CertidaoSegundoGrauNegativaPositiva");
		request.setAttribute("tempNomeBusca", "CertidaoSegundoGrauNegativaPositiva");
		request.setAttribute("tempRetorno", "CertidaoSegundoGrauNegativaPositiva");
		request.setAttribute("PaginaAnterior", paginaatual);
	
	
		certidaoNe = new CertidaoNe();

		certidaoSegundoGrauNegativaPositivaDt = (CertidaoSegundoGrauNegativaPositivaDt) request.getSession().getAttribute("certidaoSegundoGrauNegativaPositivaDt");
		if (certidaoSegundoGrauNegativaPositivaDt == null)
			certidaoSegundoGrauNegativaPositivaDt = new CertidaoSegundoGrauNegativaPositivaDt();

		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null)
			modeloDt = new ModeloDt();
		
		certidaoSegundoGrauNegativaPositivaDt.setNome(request.getParameter("Nome"));
		certidaoSegundoGrauNegativaPositivaDt.setNomeMae(request.getParameter("NomeMae"));
		certidaoSegundoGrauNegativaPositivaDt.setDataNascimento(request.getParameter("DataNascimento"));
		certidaoSegundoGrauNegativaPositivaDt.setArea(request.getParameter("Area"));
		certidaoSegundoGrauNegativaPositivaDt.setDataInicial(request.getParameter("DataInicial"));
		certidaoSegundoGrauNegativaPositivaDt.setDataFinal(request.getParameter("DataFinal"));
		certidaoSegundoGrauNegativaPositivaDt.setFeito(request.getParameter("Feito"));
		certidaoSegundoGrauNegativaPositivaDt.setPessoaTipo(request.getParameter("PessoaTipo"));
		certidaoSegundoGrauNegativaPositivaDt.setCpfCnpj(request.getParameter("Cpf"));
		

		
		
		stAcao = "/WEB-INF/jsptjgo/CertidaoSegundoGrauNegativaPositiva.jsp";
		
		certidaoSegundoGrauNegativaPositivaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		certidaoSegundoGrauNegativaPositivaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		switch (paginaatual) {
		case Configuracao.Localizar:
			
			if(!certidaoSegundoGrauNegativaPositivaDt.getNome().equals("")) {
				String aux = certidaoSegundoGrauNegativaPositivaDt.getNome();
				certidaoSegundoGrauNegativaPositivaDt.setNome(aux);
				processoLista = certidaoNe.listarProcessoCertidaoNPSegundoGrau(certidaoSegundoGrauNegativaPositivaDt);
				processoLista.addAll(certidaoNe.getListaProcessoSSG(certidaoSegundoGrauNegativaPositivaDt));
				certidaoSegundoGrauNegativaPositivaDt.setListaProcesso(processoLista);
				int modeloCodigo = certidaoSegundoGrauNegativaPositivaDt.getModeloCodigo();
				modeloDt = certidaoNe.consultarModeloCodigo("" +modeloCodigo);
				certidaoSegundoGrauNegativaPositivaDt.setTexto(certidaoNe.montaModelo(certidaoSegundoGrauNegativaPositivaDt, modeloDt, UsuarioSessao));				
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("tempRetorno", "CertidaoSegundoGrauNegativaPositiva");		
		
			} else {
				request.setAttribute("MensagemErro","Informe os dados obrigatórios!");
				certidaoSegundoGrauNegativaPositivaDt = new CertidaoSegundoGrauNegativaPositivaDt();
				modeloDt = new ModeloDt();
			}
			break;
		case Configuracao.Novo:		
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoSegundoGrauNegativaPositiva");
			stAcao = "/WEB-INF/jsptjgo/CertidaoSegundoGrauNegativaPositiva.jsp";
			certidaoSegundoGrauNegativaPositivaDt = new CertidaoSegundoGrauNegativaPositivaDt();
			modeloDt = new ModeloDt();
			break;
		case Configuracao.LocalizarAutoPai:
			posicaoLista = request.getParameter("posicaoLista");
			if (posicaoLista.length() > 0) {
				int posicao = Funcoes.StringToInt(posicaoLista);
				certidaoSegundoGrauNegativaPositivaDt.removeProcesso(posicao);
				int modeloCodigo = certidaoSegundoGrauNegativaPositivaDt.getModeloCodigo();
				modeloDt = certidaoNe.consultarModeloCodigo("" +modeloCodigo);
				certidaoSegundoGrauNegativaPositivaDt.setTexto(certidaoNe.montaModelo(certidaoSegundoGrauNegativaPositivaDt, modeloDt, UsuarioSessao));
			}

			stAcao = "/WEB-INF/jsptjgo/CertidaoSegundoGrauNegativaPositiva.jsp";
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			request.setAttribute("tempRetorno", "CertidaoSegundoGrauNegativaPositiva");
			break;
			
		case Configuracao.Imprimir: {
			if (certidaoSegundoGrauNegativaPositivaDt == null || certidaoSegundoGrauNegativaPositivaDt.getTexto().equals("")) {
				request.setAttribute("MensagemErro", "Informe o número da guia!\nE busque os processos!");
			} else {
				byte[] byTemp = null;
				String texto = certidaoSegundoGrauNegativaPositivaDt.getTexto();
				//ByteArrayOutputStream baos = ConverterHtmlPdf.convert(texto.getBytes());
				byTemp = ConverterHtmlPdf.converteHtmlPDF(texto.getBytes(), true);
				TJDataHora tjdathora = new TJDataHora();
				
				tjdathora.adicioneDia(30);
				CertidaoValidacaoDt cdt = new CertidaoValidacaoDt(byTemp,tjdathora.getDataFormatadaddMMyyyyHHmmss(),new TJDataHora().getDataFormatadaddMMyyyyHHmmss());
				cdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				cdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				certidaoNe.salvar(cdt);
				
				byTemp = certidaoNe.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , cdt);
				String nome="Certidao" + certidaoSegundoGrauNegativaPositivaDt.getCpfCnpj() ;
								
				enviarPDF(response, byTemp, nome);
																	
				byTemp = null;
			}
			return;
		}
			
		default:
			break;
		}
		
		request.getSession().setAttribute("certidaoSegundoGrauNegativaPositivaDt", certidaoSegundoGrauNegativaPositivaDt);
		request.getSession().setAttribute("modeloDt", modeloDt);
		
	
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	}


