package br.gov.go.tj.projudi.ct.publicos;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoIntimacaoDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * Controlador responsável em gerar o PDF de uma lista de intimações de uma data específica.
 * @author mmitsunaga
 * 
 * IntimacaoPDF?PaginaAtual=2 - Faz a geração do pdf com as publicações
 * IntimacaoPDF?PaginaAtual=2&PassoEditar=1 - Consulta o status da geração. Flag=1, fechar dialogo ajax
 * 
 * Obs: Esse controlador é o está funcionando em produção 
 */
public class PublicacaoIntimacaoPDFCt extends Controle {

	private static final long serialVersionUID = 2642878188809544007L;
	
	private final int CODIGO_PERMISSAO_PENDENCIA_PUBLICA = 871;
	
	private final int CODIGO_OPCAO_PUBLICACAO_2_GRAU = 1;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL = 2;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR = 3;
	
	@Override
	public int Permissao() {		
		return CODIGO_PERMISSAO_PENDENCIA_PUBLICA;
	}
	
	protected String getId_GrupoPublico(){		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
		
		String dataPublicacao = "";
		int opcaoPublicacao = 0;
		int passoEditar = 0;

		if (request.getParameter("PaginaAtual") != null){
			paginaatual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));
		}
		
		if (request.getParameter("PassoEditar") != null){
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		if (request.getParameter("dataPublicacao") != null){
			dataPublicacao = (String) request.getParameter("dataPublicacao");
		} else {
			dataPublicacao = Funcoes.dateToStringSoData(Funcoes.getDiaAnterior());				
		}
		
		if (request.getParameter("opcaoPublicacao") != null){
			opcaoPublicacao = Funcoes.StringToInt(request.getParameter("opcaoPublicacao"));
		}
		
		request.setAttribute("tempPrograma", "IntimacaoPDF");
		request.setAttribute("tempRetorno", "IntimacaoPDF");
		
		request.setAttribute("PaginaAtual", Configuracao.Localizar);
		request.setAttribute("dataPublicacao", dataPublicacao);
												
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (passoEditar == 1){
					JSONObject json = new JSONObject();
					json.put("flag", request.getSession().getAttribute("flag") != null ? request.getSession().getAttribute("flag"): "0");
					montarRetornoJSON(response, json.toString());
					return;					
				} else {
			        response.setContentType("text/html");
			        response.setCharacterEncoding("iso-8859-1");
			        request.getSession().setAttribute("flag", "0");
			        if (!dataPublicacao.isEmpty()){
			        	String dataInicial = dataPublicacao + " 00:00:00";
			    		String dataFinal = dataPublicacao + " 23:59:59";
			    		PendenciaNe pendenciaNe = new PendenciaNe();
			    		List<MovimentacaoIntimacaoDt> intimacoes = pendenciaNe.consultarIntimacoesParaPublicacao(dataInicial, dataFinal, opcaoPublicacao);
			        	if (intimacoes.size() != 0){
			        		response.setCharacterEncoding("utf-8");
							response.setContentType("application/pdf");
							response.setHeader("Content-Disposition", "attachment; filename=" + definirNomeArquivoPDF(dataPublicacao, opcaoPublicacao));
							escreverSaida(response, intimacoes);
				        	request.getSession().setAttribute("flag", "1");
				        	return;
			        	} else {
			        		request.getSession().setAttribute("flag", "1");
			        		throw new MensagemException(montarMensagemNaoEncontradoException(dataPublicacao, opcaoPublicacao));
			        	}
			        } else {
			        	request.getSession().setAttribute("flag", "1");
						throw new MensagemException("Informe a data de publicação");
					}
				}
	    				
			default:
				String stAcao = "/WEB-INF/jsptjgo/GerarIntimacaoPDF.jsp";
				RequestDispatcher dis = request.getRequestDispatcher(stAcao);
				dis.include(request, response);
				break;
		}
	
	}
	
	/**
	 * Monta a mensagem de publicação não encontrada para a consulta feita.
	 * @param dataPublicacao
	 * @param opcaoPublicacao
	 * @return
	 */
	private String montarMensagemNaoEncontradoException(String dataPublicacao, int opcaoPublicacao){
		StringBuilder msg = new StringBuilder();
		msg.append("Não foram encontradas intimações");
		if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_2_GRAU){
			msg.append(" de 2o Grau"); 
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL){
			msg.append(" de 1o Grau - Capital (Comarca de goiânia)");
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR){
			msg.append(" de 1o Grau - Interior");
		}
		msg.append(" em " + dataPublicacao);
		return msg.toString();
	}
	
	/**
	 * Monta o nome do arquivo que será baixado. Prefixo é a data de publicação
	 * @param dataPublicacao
	 * @return
	 */
	private String definirNomeArquivoPDF(String dataPublicacao, int opcaoPublicacao){
		String tipo = "";
		if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_2_GRAU){
			tipo = "_SI_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL){
			tipo = "_SII_";
		} else if (opcaoPublicacao == CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR){
			tipo = "_SIII_";
		}
		return "Intimacao" + tipo + Funcoes.FormatarDataSemBarra(dataPublicacao) + ".pdf";
	}
	
	/**
	 * Prepara o response do HTTP para retornar um JSON
	 * @param response
	 * @param conteudoJSON
	 * @throws Exception
	 */
	private void montarRetornoJSON(HttpServletResponse response, String conteudoJSON) throws Exception {
    	response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		writer.append(conteudoJSON);
		writer.flush();
    }
	
	/**
	 * 
	 * @param response
	 * @param arquivos
	 * @throws Exception
	 */
	private void escreverSaida(HttpServletResponse response, List<MovimentacaoIntimacaoDt> intimacoes) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) new CssAppliersImpl(new XMLWorkerFontProvider()));
		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			String serventia = "";
			ArrayList<HashMap<String, Object>> bookmarks = new ArrayList<HashMap<String, Object>>();
			PdfCopy copy = new PdfCopy(document, bos);
			document.open();
			for (MovimentacaoIntimacaoDt m : intimacoes){
				pendenciaNe.gerarPdfIntimacaoPorData(m, hpc, ProjudiPropriedades.getInstance().getCaminhoAplicacao(), copy, bookmarks, (!serventia.equalsIgnoreCase(m.getProcessoDt().getServentia())));
				serventia = m.getProcessoDt().getServentia();
			}
			copy.setOutlines(bookmarks);
			document.close();
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
		} catch (Exception e){
			try{if (bos!=null) bos.close(); } catch(Exception ex ) {};
			throw e;
		}		
		bos.close();
		
	}
	
}
