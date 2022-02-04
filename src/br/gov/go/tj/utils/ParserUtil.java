package br.gov.go.tj.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 * Classe Responsável em extrair somente o texto de arquivos HTML e PDF.
 */
public final class ParserUtil {

	public static String parseToPlainText(byte[] arq) throws IOException, SAXException, TikaException {
		StringBuilder texto = new StringBuilder();
		AutoDetectParser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler(-1);
	    Metadata metadata = new Metadata();
	    metadata.add(Metadata.CONTENT_ENCODING, "ISO-8859-1");
	    try (InputStream stream = new ByteArrayInputStream(arq)){
	    	parser.parse(stream, handler, metadata);
		    texto.append(handler.toString());
	    }
		String s = texto.toString().replaceAll("_", "");
	    return s.toString().trim().replaceAll("\n|\r|\t|\\s{2,}", " ");
	}
	
	/*public static String parse (byte[] arq) throws IOException, SAXException, TikaException {
		StringBuilder texto = new StringBuilder();
		TikaConfig config = TikaConfig.getDefaultConfig();
		Parser parser = new AutoDetectParser(config);
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		PDFParserConfig pdfConfig = new PDFParserConfig();
		pdfConfig.setExtractInlineImages(true);
		TesseractOCRConfig tesserConfig = new TesseractOCRConfig();
		tesserConfig.setLanguage("por");
		tesserConfig.setTesseractPath("D:/AmbienteTrabalho/Ambiente/Tesseract-OCR/");
		tesserConfig.setTessdataPath("D:/AmbienteTrabalho/Ambiente/Tesseract-OCR/tessdata");		
		ParseContext parsecontext = new ParseContext();
		parsecontext.set(Parser.class, parser);
		parsecontext.set(PDFParserConfig.class, pdfConfig);
		parsecontext.set(TesseractOCRConfig.class, tesserConfig);
		try (InputStream stream = new ByteArrayInputStream(arq)){
			parser.parse(stream, handler, metadata, parsecontext);
		    texto.append(handler.toString());
		}
		String s = texto.toString().replaceAll("_", "");
	    return s.toString().trim().replaceAll("\n|\r|\t|\\s{2,}", " ");
	}*/
	
	public static String parsePdfToPlainText(byte[] arq) throws IOException, SAXException, TikaException {
		StringBuilder texto = new StringBuilder();		
		PDFParser parser = new PDFParser();
		BodyContentHandler handler = new BodyContentHandler(-1);	    
	    Metadata metadata = new Metadata();
	    metadata.add(Metadata.CONTENT_ENCODING, "ISO-8859-1");
	    try (InputStream stream = new ByteArrayInputStream(arq)){
	        parser.parse(stream, handler, metadata, new ParseContext());
	        texto.append(handler.toString());
	    }
	    String s = texto.toString().replaceAll("_", "");
	    return s.toString().trim().replaceAll("\n|\r|\t|\\s{2,}", " ");
	}
	
	public static String parseHtmlToPlainText(byte[] arq) throws IOException, SAXException, TikaException {
		StringBuilder texto = new StringBuilder();
		HtmlParser parser = new HtmlParser();
		BodyContentHandler handler = new BodyContentHandler(-1);
	    Metadata metadata = new Metadata();
	    metadata.add(Metadata.CONTENT_ENCODING, "ISO-8859-1");
	    try (InputStream stream = new ByteArrayInputStream(arq);) {
	        parser.parse(stream, handler, metadata, new ParseContext());
	        texto.append(handler.toString());
	    }
	    String s = texto.toString().replaceAll("_", "");
	    return s.toString().trim().replaceAll("\n|\r|\t|\\s{2,}", " ");
	}
	
}
