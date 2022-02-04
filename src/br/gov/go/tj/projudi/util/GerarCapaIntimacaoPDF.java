package br.gov.go.tj.projudi.util;

import java.io.ByteArrayOutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoIntimacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.utils.Cifrar;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ParserUtil;
import br.gov.go.tj.utils.ValidacaoUtil;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * Classe responsável em gerar uma página de resumo com os dados do processo e intimação.
 * @author mmitsunaga
 *
 */
public class GerarCapaIntimacaoPDF {

	private static final int TAM_ROTULO = 18;
	
	private static final String FONT_FACTORY = FontFactory.COURIER_BOLD;
	
	private static final int FONT_SIZE = 10;
	
	
	/**
	 * Cria a página de resumo com dados do processo, intimação e as partes intimadas
	 * @param movimentacaoIntimacao
	 * @return
	 * @throws Exception
	 */
	public static byte[] gerarPaginaIntimacoesProcesso(MovimentacaoIntimacaoDt movimentacaoIntimacao) throws Exception {
		
		Font fontParagraph = FontFactory.getFont(FONT_FACTORY, FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
		
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] temp= null;
		
		try {
			
			writer = PdfWriter.getInstance(document, outputStream);
			document.open();
									
			PendenciaDt pe = movimentacaoIntimacao.getPendencias().get(0);
			ProcessoDt processo = movimentacaoIntimacao.getProcessoDt();
			
			// TITULO DA PENDÊNCIA
			String tipoPendencia = pe.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO ?  "CARTA DE CITAÇÃO" : "INTIMAÇÃO";
			String titulo = tipoPendencia + " EFETIVADA REF. À MOV. " + pe.getMovimentacao();			
			if (movimentacaoIntimacao.getTipoMovimentacao().toUpperCase().startsWith("AUDI")){
				titulo += movimentacaoIntimacao.getComplemento() != null ? " " + movimentacaoIntimacao.getComplemento() : "";
			}			
			titulo += " - Data da Movimentação " + Funcoes.FormatarDataHora(pe.getDataTemp());
			
			Paragraph paragrafoMovimentacao = new Paragraph(titulo, fontParagraph);
			
			Chapter corpo = new Chapter(paragrafoMovimentacao, 1);
			corpo.setNumberDepth(0);
			
			Paragraph paragrafoNovaLinha = new Paragraph(" ");
			corpo.add(paragrafoNovaLinha);
			
			// SERVENTIA DO PROCESSO
			Paragraph paragrafoServentia = new Paragraph(ajustarTamanhoRotulo("LOCAL", processo.getServentia().toUpperCase(), true), fontParagraph);
			paragrafoServentia.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoServentia);
			
			// Nº DO PROCESSO
			Paragraph paragrafoProcesso = new Paragraph(ajustarTamanhoRotulo("NR.PROCESSO", Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto()), true), fontParagraph);
			paragrafoProcesso.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoProcesso);
								
			// CLASSE PROCESSUAL
			Paragraph paragrafoNatureza = new Paragraph(ajustarTamanhoRotulo("CLASSE PROCESSUAL", processo.getProcessoTipo(), true), fontParagraph);
			paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoNatureza);
			
			// 1o PROMOVENTE / RECORRENTE
			if (processo.getPrimeiroPoloAtivoNome() != null){
				String nomePrimeiroPromovente = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloAtivoNome()) : processo.getPrimeiroPoloAtivoNome().toUpperCase());
				Paragraph paragrafoPoloAtivo = new Paragraph(ajustarTamanhoRotulo("POLO ATIVO", nomePrimeiroPromovente, true), fontParagraph);
				paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
				corpo.add(paragrafoPoloAtivo);
			}
						
			// 1o PROMOVIDO / RECORRIDO
			if (processo.getPrimeiroPoloPassivoNome() != null){
				String nomePrimeiroPromovido = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloPassivoNome()) : processo.getPrimeiroPoloPassivoNome().toUpperCase());
				Paragraph paragrafoPoloPassivo = new Paragraph(ajustarTamanhoRotulo("POLO PASSIVO", nomePrimeiroPromovido, true), fontParagraph);
				paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
				corpo.add(paragrafoPoloPassivo);
			}
						
			// SEGREDO DE JUSTIÇA
			Paragraph paragrafoSegredo = new Paragraph(ajustarTamanhoRotulo("SEGREDO JUSTIÇA", processo.mostrarSegredoJustica().toUpperCase(), true), fontParagraph);
			paragrafoSegredo.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoSegredo);
			
			paragrafoNovaLinha = new Paragraph(" ");
			corpo.add(paragrafoNovaLinha);
			
			// PARTES INTIMADAS
			for (PendenciaDt pendencia : movimentacaoIntimacao.getPendencias()){
				
				// NOME DA PARTE INTIMADA
				String nomeParte = processo.isSegredoJustica() ? Funcoes.iniciaisNome(pendencia.getNomeParte()) : pendencia.getNomeParte().toUpperCase();			
				Paragraph parteParagrafo = new Paragraph(ajustarTamanhoRotulo("PARTE INTIMADA", nomeParte, true), fontParagraph);				
				corpo.add(parteParagrafo);
				
				// ADVOGADOS DA PARTE
				for (int i = 0; i < pendencia.getResponsaveis().size(); i++){
					
					PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) pendencia.getResponsaveis().get(i);
					
					String linhaAdvParte = "";
					if (i == 0){
						linhaAdvParte = ajustarTamanhoRotulo(pendencia.getResponsaveis().size() > 1 ? "ADVGS. PARTE" : "ADVG. PARTE", responsavel.getNomeUsuarioResponsavel().toUpperCase(), true);						
					} else {
						linhaAdvParte = ajustarTamanhoRotulo("", responsavel.getNomeUsuarioResponsavel().toUpperCase(), false);
					}
					
					Paragraph paragrafoParte = new Paragraph(linhaAdvParte, fontParagraph);
					corpo.add(paragrafoParte);
				}
				
				paragrafoNovaLinha = new Paragraph(" ");
				corpo.add(paragrafoNovaLinha);
				
			}
						
			// ARQUIVOS
			String despacho = "";
			if (ValidacaoUtil.isVazio(movimentacaoIntimacao.getArquivos())){
				despacho = "- ARQUIVOS DIGITAIS INDISPONÍVEIS (NÃO SÃO DO TIPO PÚBLICO).";

			} else if (processo.isSegredoJustica()) {
				despacho = "- PROCESSO EM SEGREDO DE JUSTIÇA. OS ARQUIVOS DA INTIMAÇÃO NÃO FORAM PUBLICADOS.";
				
			} else {
				despacho = "- VIDE ABAIXO O(S) ARQUIVO(S) DA INTIMAÇÃO.";
			}
			Paragraph paragrafoArquivo = new Paragraph(despacho, fontParagraph);
			corpo.add(paragrafoArquivo);
			
			document.add(corpo);
			
			document.close();
			writer.close();
			temp = outputStream.toByteArray();
			outputStream.close();
		
		} catch(DocumentException e) {
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
			try{if (writer!=null) writer.close(); } catch(Exception ex ) {};
			try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
		
	}
	
	/**
	 * Cria a página de resumo com dados do processo e nome do advogado externo ao processo.
	 * @param movimentacaoIntimacao
	 * @return
	 * @throws Exception
	 */
	public static byte[] gerarPaginaIntimacoesParaAdvogadoExternoAoProcesso(MovimentacaoIntimacaoDt movimentacaoIntimacao) throws Exception {
		
		Font fontParagraph = FontFactory.getFont(FONT_FACTORY, FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
		
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] temp= null;
		
		try {
			
			writer = PdfWriter.getInstance(document, outputStream);
			document.open();
									
			ProcessoDt processo = movimentacaoIntimacao.getProcessoDt();
			
			// TITULO DA PENDÊNCIA
			String complemento = ValidacaoUtil.isNaoVazio(movimentacaoIntimacao.getComplemento()) ? " - " + movimentacaoIntimacao.getComplemento() : "";
			String titulo = movimentacaoIntimacao.getTipoMovimentacao() + complemento;
			Paragraph paragrafoMovimentacao = new Paragraph(titulo, fontParagraph);
			
			Chapter corpo = new Chapter(paragrafoMovimentacao, 1);
			corpo.setNumberDepth(0);
			
			Paragraph paragrafoNovaLinha = new Paragraph(" ");
			corpo.add(paragrafoNovaLinha);
			
			// SERVENTIA DO PROCESSO
			Paragraph paragrafoServentia = new Paragraph(ajustarTamanhoRotulo("LOCAL", processo.getServentia().toUpperCase(), true), fontParagraph);
			paragrafoServentia.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoServentia);
			
			// Nº DO PROCESSO
			Paragraph paragrafoProcesso = new Paragraph(ajustarTamanhoRotulo("NR.PROCESSO", Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto()), true), fontParagraph);
			paragrafoProcesso.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoProcesso);
								
			// CLASSE PROCESSUAL
			Paragraph paragrafoNatureza = new Paragraph(ajustarTamanhoRotulo("CLASSE PROCESSUAL", processo.getProcessoTipo(), true), fontParagraph);
			paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoNatureza);
			
			// 1o PROMOVENTE / RECORRENTE
			if (processo.getPrimeiroPoloAtivoNome() != null){
				String nomePrimeiroPromovente = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloAtivoNome()) : processo.getPrimeiroPoloAtivoNome().toUpperCase());
				Paragraph paragrafoPoloAtivo = new Paragraph(ajustarTamanhoRotulo("POLO ATIVO", nomePrimeiroPromovente, true), fontParagraph);
				paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
				corpo.add(paragrafoPoloAtivo);
			}
						
			// 1o PROMOVIDO / RECORRIDO
			if (processo.getPrimeiroPoloPassivoNome() != null){
				String nomePrimeiroPromovido = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloPassivoNome()) : processo.getPrimeiroPoloPassivoNome().toUpperCase());
				Paragraph paragrafoPoloPassivo = new Paragraph(ajustarTamanhoRotulo("POLO PASSIVO", nomePrimeiroPromovido, true), fontParagraph);
				paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
				corpo.add(paragrafoPoloPassivo);
			}
						
			// SEGREDO DE JUSTIÇA
			Paragraph paragrafoSegredo = new Paragraph(ajustarTamanhoRotulo("SEGREDO JUSTIÇA", processo.mostrarSegredoJustica().toUpperCase(), true), fontParagraph);
			paragrafoSegredo.setAlignment(Paragraph.ALIGN_LEFT);
			corpo.add(paragrafoSegredo);
			
			paragrafoNovaLinha = new Paragraph(" ");
			corpo.add(paragrafoNovaLinha);
			
			document.add(corpo);
			
			document.close();
			writer.close();
			temp = outputStream.toByteArray();
			outputStream.close();
		
		} catch(DocumentException e) {
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
			try{if (writer!=null) writer.close(); } catch(Exception ex ) {};
			try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;	
		
	}
	
	public static byte[] gerarPaginaResumoParaPublicacao (MovimentacaoIntimacaoDt movimentacaoIntimacao) throws Exception {
		
		byte[] temp = null;
		PdfWriter writer = null;
		
		if (!movimentacaoIntimacao.getProcessoDt().isSegredoJustica()){
			
			if (ValidacaoUtil.isNaoNulo(movimentacaoIntimacao.getArquivos())){
				
				ArquivoDt arquivoIntimacao = movimentacaoIntimacao.getArquivos().get(0);
				
				if (arquivoIntimacao.isConteudo()){
					
					byte[] out = arquivoIntimacao.getConteudo();					
					String texto = null;					
					if (arquivoIntimacao.getContentType().equals("application/pdf")){
    					texto = ParserUtil.parsePdfToPlainText(out);    					
    				} else if (arquivoIntimacao.getContentType().equals("text/html")){
    					texto = ParserUtil.parseHtmlToPlainText(out);    					
    				} else {
    					texto = ParserUtil.parseToPlainText(out);
    				}
					
					if (ValidacaoUtil.isNaoVazio(texto)){
						
						Font fontParagraph = FontFactory.getFont(FONT_FACTORY, FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
						
						Document document = new Document(PageSize.A4, 50, 50, 50, 50);
						
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						
						try {
							
							writer = PdfWriter.getInstance(document, outputStream);
							document.open();
							
							ProcessoDt processo = movimentacaoIntimacao.getProcessoDt();
							
							// SERVENTIA DO PROCESSO
							Paragraph paragrafoServentia = new Paragraph(ajustarTamanhoRotulo("LOCAL", movimentacaoIntimacao.getProcessoDt().getServentia().toUpperCase(), true), fontParagraph);
							paragrafoServentia.setAlignment(Paragraph.ALIGN_LEFT);
										
							Chapter corpo = new Chapter(paragrafoServentia, 1);
							corpo.setNumberDepth(0);
							
							// Nº DO PROCESSO
							Paragraph paragrafoProcesso = new Paragraph(ajustarTamanhoRotulo("NR.PROCESSO", Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto()), true), fontParagraph);
							paragrafoProcesso.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoProcesso);
							
							// TIPO MOVIMENTACAO
							Paragraph paragrafoTipoMovimentacao = new Paragraph(ajustarTamanhoRotulo("TIPO MOVIMENTAÇÃO", movimentacaoIntimacao.getTipoMovimentacao().toUpperCase(), true), fontParagraph);
							paragrafoTipoMovimentacao.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoTipoMovimentacao);
							
							// CLASSE PROCESSUAL
							Paragraph paragrafoNatureza = new Paragraph(ajustarTamanhoRotulo("CLASSE PROCESSUAL", processo.getProcessoTipo(), true), fontParagraph);
							paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoNatureza);
							
							// 1o PROMOVENTE / RECORRENTE
							if (processo.getPrimeiroPoloAtivoNome() != null){
								String nomePrimeiroPromovente = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloAtivoNome()) : processo.getPrimeiroPoloAtivoNome().toUpperCase());
								Paragraph paragrafoPoloAtivo = new Paragraph(ajustarTamanhoRotulo("POLO ATIVO", nomePrimeiroPromovente, true), fontParagraph);
								paragrafoPoloAtivo.setAlignment(Paragraph.ALIGN_LEFT);
								corpo.add(paragrafoPoloAtivo);
							}
										
							// 1o PROMOVIDO / RECORRIDO
							if (processo.getPrimeiroPoloPassivoNome() != null){
								String nomePrimeiroPromovido = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloPassivoNome()) : processo.getPrimeiroPoloPassivoNome().toUpperCase());
								Paragraph paragrafoPoloPassivo = new Paragraph(ajustarTamanhoRotulo("POLO PASSIVO", nomePrimeiroPromovido, true), fontParagraph);
								paragrafoPoloPassivo.setAlignment(Paragraph.ALIGN_LEFT);
								corpo.add(paragrafoPoloPassivo);
							}
							
							// DATA PUBLICACAO			
							Paragraph paragrafoDataPublicacao = new Paragraph(ajustarTamanhoRotulo("DATA PUBLICAÇÃO", movimentacaoIntimacao.getDataRealizacao(), true), fontParagraph);
							paragrafoDataPublicacao.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoDataPublicacao);
							
							// CODIGO DE VALIDACAO
							String codigoValidacao = Cifrar.codificar(arquivoIntimacao.getId(), PendenciaArquivoDt.CodigoPermissao);
							Paragraph paragrafoCodigoValidacao = new Paragraph(ajustarTamanhoRotulo("CÓD.VALIDAÇÃO", codigoValidacao, true), fontParagraph);
							paragrafoCodigoValidacao.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoCodigoValidacao);
							
							corpo.add(new Paragraph(" "));
														
							Paragraph paragrafoUrl = new Paragraph("Para baixar o arquivo original, informe o código de validação em https://projudi.tjgo.jus.br/PendenciaPublica", FontFactory.getFont(FontFactory.COURIER, 9, Font.BOLD, BaseColor.BLUE));
							paragrafoUrl.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoUrl);
							
							corpo.add(new Paragraph(" "));
							
							// TEXTO DA PUBLICAÇÃO										
							Paragraph paragrafoTexto = new Paragraph(new Chunk(Funcoes.normalizarEspacoEmBrancoEntreTexto(texto), FontFactory.getFont(FontFactory.COURIER, 9, Font.NORMAL, BaseColor.BLACK)));							
							paragrafoTexto.setAlignment(Paragraph.ALIGN_JUSTIFIED);
							corpo.add(paragrafoTexto);
							
							document.add(corpo);
							
							document.close();
							writer.close();
							temp = outputStream.toByteArray();
							outputStream.close();
							
						} catch(DocumentException e) {
							try{if (document!=null) document.close(); } catch(Exception ex ) {};
							try{if (writer!=null) writer.close(); } catch(Exception ex ) {};
							try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
							throw e;
						}						
					}					
				}
			}
		}
		
		return temp;
		
	}
	
	public static byte[] gerar (MovimentacaoIntimacaoDt movimentacaoIntimacao) throws Exception {
		
		byte[] temp = null;
		PdfWriter writer = null;
		
		if (!movimentacaoIntimacao.getProcessoDt().isSegredoJustica()){
			
			if (ValidacaoUtil.isNaoNulo(movimentacaoIntimacao.getArquivos())){
				
				ArquivoDt arquivoIntimacao = movimentacaoIntimacao.getArquivos().get(0);
				
				if (arquivoIntimacao.isConteudo()){
					
					byte[] out = arquivoIntimacao.getConteudo();					
					String texto = null;					
					if (arquivoIntimacao.getContentType().equals("application/pdf")){
    					texto = ParserUtil.parsePdfToPlainText(out);    					
    				} else if (arquivoIntimacao.getContentType().equals("text/html")){
    					texto = ParserUtil.parseHtmlToPlainText(out);    					
    				} else {
    					texto = ParserUtil.parseToPlainText(out);
    				}
					
					if (ValidacaoUtil.isNaoVazio(texto)){
						
						Font fontParagraph = FontFactory.getFont(FONT_FACTORY, FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
						
						Document document = new Document(PageSize.A4, 50, 50, 50, 50);
						
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						
						try {
							
							writer = PdfWriter.getInstance(document, outputStream);
							document.open();
							
							PendenciaDt pe = movimentacaoIntimacao.getPendencias().get(0);
							ProcessoDt processo = movimentacaoIntimacao.getProcessoDt();
							
							// TITULO DA PENDÊNCIA
							String tipoPendencia = pe.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_CITACAO ?  "CARTA DE CITAÇÃO" : "INTIMAÇÃO";
							String titulo = tipoPendencia + " EFETIVADA REF. À MOV. " + pe.getMovimentacao();			
							if (movimentacaoIntimacao.getTipoMovimentacao().toUpperCase().startsWith("AUDI")){
								titulo += movimentacaoIntimacao.getComplemento() != null ? " " + movimentacaoIntimacao.getComplemento() : "";
							}			
							titulo += " - Data da Movimentação " + Funcoes.FormatarDataHora(pe.getDataTemp());
							
							Paragraph paragrafoMovimentacao = new Paragraph(titulo, fontParagraph);
							
							Chapter corpo = new Chapter(paragrafoMovimentacao, 1);
							corpo.setNumberDepth(0);
							
							Paragraph paragrafoNovaLinha = new Paragraph(" ");
							corpo.add(paragrafoNovaLinha);
																				
							// SERVENTIA DO PROCESSO
							Paragraph paragrafoServentia = new Paragraph(ajustarTamanhoRotulo("LOCAL", movimentacaoIntimacao.getProcessoDt().getServentia().toUpperCase(), true), fontParagraph);
							paragrafoServentia.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoServentia);
							
							// Nº DO PROCESSO
							Paragraph paragrafoProcesso = new Paragraph(ajustarTamanhoRotulo("NR.PROCESSO", Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto()), true), fontParagraph);
							paragrafoProcesso.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoProcesso);
							
							// TIPO MOVIMENTACAO
							Paragraph paragrafoTipoMovimentacao = new Paragraph(ajustarTamanhoRotulo("TIPO MOVIMENTAÇÃO", movimentacaoIntimacao.getTipoMovimentacao().toUpperCase(), true), fontParagraph);
							paragrafoTipoMovimentacao.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoTipoMovimentacao);
							
							// CLASSE PROCESSUAL
							Paragraph paragrafoNatureza = new Paragraph(ajustarTamanhoRotulo("CLASSE PROCESSUAL", processo.getProcessoTipo(), true), fontParagraph);
							paragrafoNatureza.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoNatureza);
							
							// 1o PROMOVENTE / RECORRENTE
							if (processo.getPrimeiroPoloAtivoNome() != null){
								String nomePrimeiroPromovente = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloAtivoNome()) : processo.getPrimeiroPoloAtivoNome().toUpperCase());
								Paragraph paragrafoPoloAtivo = new Paragraph(ajustarTamanhoRotulo("POLO ATIVO", nomePrimeiroPromovente, true), fontParagraph);
								paragrafoPoloAtivo.setAlignment(Paragraph.ALIGN_LEFT);
								corpo.add(paragrafoPoloAtivo);
							}
										
							// 1o PROMOVIDO / RECORRIDO
							if (processo.getPrimeiroPoloPassivoNome() != null){
								String nomePrimeiroPromovido = (processo.isSegredoJustica() ? Funcoes.iniciaisNome(processo.getPrimeiroPoloPassivoNome()) : processo.getPrimeiroPoloPassivoNome().toUpperCase());
								Paragraph paragrafoPoloPassivo = new Paragraph(ajustarTamanhoRotulo("POLO PASSIVO", nomePrimeiroPromovido, true), fontParagraph);
								paragrafoPoloPassivo.setAlignment(Paragraph.ALIGN_LEFT);
								corpo.add(paragrafoPoloPassivo);
							}
																			
							// PARTES INTIMADAS
							for (PendenciaDt pendencia : movimentacaoIntimacao.getPendencias()){
								
								// NOME DA PARTE INTIMADA
								String nomeParte = processo.isSegredoJustica() ? Funcoes.iniciaisNome(pendencia.getNomeParte()) : pendencia.getNomeParte().toUpperCase();			
								Paragraph parteParagrafo = new Paragraph(ajustarTamanhoRotulo("PARTE INTIMADA", nomeParte, true), fontParagraph);
								corpo.add(parteParagrafo);
								
								// ADVOGADOS DA PARTE
								for (int i = 0; i < pendencia.getResponsaveis().size(); i++){
									
									PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) pendencia.getResponsaveis().get(i);
									
									String linhaAdvParte = "";
									if (i == 0){
										linhaAdvParte = ajustarTamanhoRotulo(pendencia.getResponsaveis().size() > 1 ? "ADVGS. PARTE" : "ADVG. PARTE", responsavel.getNomeUsuarioResponsavel().toUpperCase(), true);						
									} else {
										linhaAdvParte = ajustarTamanhoRotulo("", responsavel.getNomeUsuarioResponsavel().toUpperCase(), false);
									}
									
									Paragraph paragrafoParte = new Paragraph(linhaAdvParte, fontParagraph);
									corpo.add(paragrafoParte);
								}
								
							}
																					
							// CODIGO DE VALIDACAO
							String codigoValidacao = Cifrar.codificar(arquivoIntimacao.getId(), PendenciaArquivoDt.CodigoPermissao);
							Paragraph paragrafoCodigoValidacao = new Paragraph(ajustarTamanhoRotulo("CÓD.VALIDAÇÃO", codigoValidacao, true), fontParagraph);
							paragrafoCodigoValidacao.setAlignment(Paragraph.ALIGN_LEFT);
							corpo.add(paragrafoCodigoValidacao);														
							
							corpo.add(new Paragraph(" "));
							
							Paragraph paragrafoUrl = new Paragraph("(Para baixar o arquivo original, informe o código de validação em https://projudi.tjgo.jus.br/PendenciaPublica)", FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLDITALIC, BaseColor.BLUE));							
							corpo.add(paragrafoUrl);
							
							corpo.add(new Paragraph(" "));
							
							// ARQUIVOS
							String despacho = "";
							if (ValidacaoUtil.isVazio(movimentacaoIntimacao.getArquivos())){
								despacho = "- ARQUIVOS DIGITAIS INDISPONÍVEIS (NÃO SÃO DO TIPO PÚBLICO).";

							} else if (processo.isSegredoJustica()) {
								despacho = "- PROCESSO EM SEGREDO DE JUSTIÇA. OS ARQUIVOS DA INTIMAÇÃO NÃO FORAM PUBLICADOS.";							
							}
							
							Paragraph paragrafoArquivo = new Paragraph(despacho, fontParagraph);
							corpo.add(paragrafoArquivo);
							
							// TEXTO DA PUBLICAÇÃO										
							Paragraph paragrafoTexto = new Paragraph(new Chunk(Funcoes.normalizarEspacoEmBrancoEntreTexto(texto), FontFactory.getFont(FontFactory.COURIER, 9, Font.NORMAL, BaseColor.BLACK)));							
							paragrafoTexto.setAlignment(Paragraph.ALIGN_JUSTIFIED);
							corpo.add(paragrafoTexto);
							
							document.add(corpo);
							
							document.close();
							writer.close();
							temp = outputStream.toByteArray();
							outputStream.close();
							
						} catch(DocumentException e) {
							try{if (document!=null) document.close(); } catch(Exception ex ) {};
							try{if (writer!=null) writer.close(); } catch(Exception ex ) {};
							try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
							throw e;
						}						
					}					
				}
			}
		}
		
		return temp;
		
	}
	
	/**
	 * 
	 * @param textoIndice
	 * @param textoIndicePai
	 * @param page
	 * @param bookmarks
	 * @param ehIndiceSuperior
	 * @throws Exception
	 */
	public static void definirIndice(String textoIndice, String textoIndicePai, int page, List<HashMap<String, Object>> bookmarks, boolean ehIndiceSuperior) throws Exception {
		
		// Nova serventia    	
		if (ehIndiceSuperior){
    		
    		// Cria um link pai (serventia)
    		HashMap<String, Object> linkServentia = addLink(textoIndicePai, page);
    		 
    		// Cria um link filho (texto da movimentação)
    		List<HashMap<String, Object>> subOutlines = new ArrayList<HashMap<String, Object>>();
    		subOutlines.add(addLink(textoIndice, page));
    		
    		// Relaciona o link filho com o link pai
    		linkServentia.put("Kids", subOutlines);
    		
    		// Coloca o link da serventia na listagem geral
    		bookmarks.add(linkServentia);
    							
		} else {
			
			// Pega o link da serventia atual
			HashMap<String, Object> linkServentia = bookmarks.get(bookmarks.size()-1);
			
			List<HashMap<String, Object>> subOutlines = (List<HashMap<String, Object>>) linkServentia.get("Kids");
			
			// Link intimação										
			subOutlines.add(addLink(textoIndice, page));
			
			// Atualiza a lista de filhos
			linkServentia.replace("Kids", subOutlines);
								
		}
		
	}
	
	/**
	 * 
	 * @param movimentacaoIntimacaoDt
	 * @return
	 * @throws Exception
	 */
	public static Object[] getCarimboLadoDireito(MovimentacaoIntimacaoDt movimentacaoIntimacaoDt) throws Exception {
		List<String> linhas = new ArrayList<String>();
		if (movimentacaoIntimacaoDt != null){
			ProcessoDt processo = movimentacaoIntimacaoDt.getProcessoDt();
			if (processo != null){
				linhas.add("NR.PROCESSO: " + Funcoes.formataNumeroProcesso(processo.getProcessoNumeroCompleto()));
			}
		}
		return linhas.toArray();
	}
	
	/**
	 * Ajusta o largura da coluna à esquerda e completa com espaços em branco
	 * @param textoEsquerda
	 * @param textoDireita
	 * @param temSeparador
	 * @return Retorna uma string da forma: texto esquerda : texto direita
	 */
	private static String ajustarTamanhoRotulo(String textoEsquerda, String textoDireita, boolean temSeparador){
		String retorno = "";
		if (textoEsquerda.length() < TAM_ROTULO){			
			retorno = textoEsquerda + preencherEspacoVazio((TAM_ROTULO - textoEsquerda.length())) + (temSeparador ? ": " : "  ") + textoDireita;
		} else {
			retorno = textoEsquerda + (temSeparador ? ": " : "  ") + textoDireita;
		}
		return retorno;
	}
	
	/**
	 * 
	 * @param tamanho
	 * @return
	 */
	private static String preencherEspacoVazio (int tamanho){
		return CharBuffer.allocate(tamanho).toString().replace('\0', ' ');
	}
	
	
	/**
	 * 
	 * @param title
	 * @param page
	 * @return
	 */
	private static HashMap<String, Object> addLink (String title, int page){
		HashMap<String, Object> link = new HashMap<String, Object>();
		link.put("Title", title);
		link.put("Action", "GoTo");
		link.put("Page", String.format("%d Fit", page));		
		return link;
	}
	
}
