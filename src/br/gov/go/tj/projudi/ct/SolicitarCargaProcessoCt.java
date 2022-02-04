package br.gov.go.tj.projudi.ct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

/**
 * Servlet responsável pela solicitação de carga em processos misto(Físico/Eletrônico).
 * 
 * @author lsbernardes
 * 
 */
public class SolicitarCargaProcessoCt extends Controle {

	private static final long serialVersionUID = 1203509706098272559L;
	public static final int CodigoPermissao = 920;

	public int Permissao() {
		return SolicitarCargaProcessoCt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoNe Processone;
		ProcessoDt processoDt = null;
		String stAcao;

		request.setAttribute("tempPrograma", "");
		request.setAttribute("tempRetorno", "SolicitarCargaProcesso");
		request.setAttribute("TituloPagina", "");
		stAcao = "";

		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessone");
		if (Processone == null) Processone = new ProcessoNe();
	
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
		
		String mensagemError = "";

		
		if (request.getParameter("PaginaAnterior") != null)
			Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		request.setAttribute("PaginaAnterior", paginaatual);
		
		switch (paginaatual) {
			
			case Configuracao.Curinga6:
				break;
				
			case Configuracao.Curinga7:
				MovimentacaoProcessoDt movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
				
				if (movimentacaoProcessoDt != null && movimentacaoProcessoDt.getListaProcessos() != null && movimentacaoProcessoDt.getListaProcessos().size()>0){
					processoDt = (ProcessoDt) movimentacaoProcessoDt.getListaProcessos().get(0);
					if (Processone.isMisto(processoDt.getId())) {
						if (Processone.possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(processoDt.getId())){
							mensagemError = "Não é possível realizar esta ação. Motivo: Processo já possui uma pendência de Solicitação de Carga Aguardando Retorno!";
						} else {
							
							Processone.realizarCargaProcesso(UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt);
							request.getSession().removeAttribute("Movimentacaodt");
							redireciona(response, "BuscaProcesso?Id_Processo="+processoDt.getId() + "&expedirImprimirSolicitarCargaProcesso=true&MensagemOk=" + "A Carga do processo foi registrada com sucesso!");
						}
					} else {
						mensagemError = "Não é possível realizar esta ação. Motivo: Processo Não é Misto!";
					}
				} else {
					  throw new MensagemException("Não foi possível localizar o processo.");
				}
				
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagemError);
				break;
				
			// Solicitação de Carga no Processo
			case Configuracao.Curinga8:
				if (Processone.isMisto(processoDt.getId())) {
					if (Processone.possuiPendeciaSolicitarCargaProcesso(processoDt.getId(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentia())){
						mensagemError = "Não é possível realizar esta ação. Motivo: Processo já possui uma pendência de Solicitação de Carga para o seu usuário!";
					} else if (Processone.processoConcluso(processoDt.getId())) {
						mensagemError = "Não é possível realizar esta ação. Motivo: Processo está Concluso!";	
					} else {
						Processone.gerarPendeciaSolicitarCargaProcesso(UsuarioSessao.getUsuarioDt(), processoDt);
						redireciona(response, "BuscaProcesso?Id_Processo="+processoDt.getId() + "&MensagemOk=" + "A solicitação de carga no processo foi registrada com sucesso!");
					}
				} else {
					mensagemError = "Não é possível realizar esta ação. Motivo: Processo Não é Misto!";
				}
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagemError);
				break;
			
			case Configuracao.LocalizarDWR:
				break;
			
			case Configuracao.Curinga9:
				break;
			
			case Configuracao.Imprimir:
				// Emissão dos arquivos em PDF
				byte[] arquivoPDFCompleto = null;
				List arquivos = Funcoes.converterMapParaList((HashMap) request.getSession().getAttribute("ListaArquivos"));
				Iterator itArquivos =arquivos.iterator();
	
				boolean isPrimeiroArquivo = true;
				int contArquivo = 1;
				while (itArquivos.hasNext()) {
					CMSSignedData dados = null;
					ArquivoDt arquivoDt = (ArquivoDt) itArquivos.next();
					
					if (arquivoDt.isArquivoAssinado()) {
						InputStream inputStream = new ByteArrayInputStream(arquivoDt.conteudoBytes());
						//Verifica se e recibo
						if (arquivoDt.getRecibo().trim().equals("true")) {
							//Extrai arquivo do recibo 
							dados = Signer.extrairConteudoRecibo(inputStream);
						} else {
							dados = new CMSSignedData(arquivoDt.conteudoBytes());
						}
						//Fecha o input stream
						inputStream.close();
					}
					
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
							arquivoPDFCompleto = GerarCabecalhoProcessoPDF.geraCabecalhoArquivoPDF(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , arquivoDt, UsuarioSessao.getUsuarioDt().getNome(), processoDt.getProcessoNumeroCompleto(), arquivoByteArrayPDF, contArquivo);
						} else{
							arquivoByteArrayPDF = GerarCabecalhoProcessoPDF.geraCabecalhoArquivoPDF(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , arquivoDt, UsuarioSessao.getUsuarioDt().getNome(), processoDt.getProcessoNumeroCompleto(), arquivoByteArrayPDF, contArquivo);
							arquivoPDFCompleto = ConcatenatePDF.concatenaPdf(arquivoPDFCompleto, arquivoByteArrayPDF);
						}
					}
					isPrimeiroArquivo = false;
					contArquivo += 1;
				}
	
				if (arquivoPDFCompleto.length > 0) {
					// Modifica o cabeçalho
					enviarPDF(response, arquivoPDFCompleto, "ArquivoPDF_CargaProcesso_");										
					// Limpa as listas de arquivos
					this.limparArquivosSessao(request);
					return;
				}
				break;

			case Configuracao.Salvar:
				break;

			case Configuracao.SalvarResultado:
				break;
			
			case Configuracao.Localizar:
				break;
				
			case Configuracao.Novo:
				break;
				
			default:
				break;
		}

		request.getSession().setAttribute("BuscaProcessone", Processone);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	

	/**
	 * Limpar arquivos da sessao
	 * 
	 * @param request
	 *            objeto de request
	 */
	protected void limparArquivosSessao(HttpServletRequest request) {
		Map arquivos = (HashMap) request.getSession().getAttribute("ListaArquivos");

		// Limpa as listas de arquivos
		if (arquivos != null) {
			arquivos.clear();
		}
		
	}

}
