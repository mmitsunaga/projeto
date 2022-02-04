package br.gov.go.tj.utils.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Classe responsável em gerar arquivos pdf no padrão ISO 19005-1:2005 (PDF/A-1)
 * Level B ("basic"): ensures that the visual appearance of a document will be preserved for the long term.
 * Level A ("accessible"): ensures that the visual appearance of a document will be preserved for the long term,
 * 						   but also introduces structural and semantic properties. The PDF needs to be a Tagged PDF.
 * @author mmitsunaga
 *
 */
public class GerarPDFA {
		
	public static final String FONT_HELVETICA_REGULAR = "Helvetica.ttf";
	
	private static BaseColor primaryBaseColor = BaseColor.BLUE;
	
	/**
	 * 
	 * @param nomeArquivo
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static byte[] gerarPDF(String nomeArquivo, String msg)throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] temp= null;			
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			PdfAWriter writer = PdfAWriter.getInstance(document, baos, PdfAConformanceLevel.PDF_A_1B);
			setDefaultParamsToPDFA(writer, document);
	        document.open();	  	        
	        setDefaultOutputIntents(writer);	        
	        Font fontBlue = new Font(getFontHelveticaRegular(), 18, Font.NORMAL, getPrimaryBaseColor());	        
			Paragraph p1 = new Paragraph(nomeArquivo, fontBlue);
	        p1.setAlignment(Paragraph.ALIGN_CENTER);
	        document.add(p1);	        
	        Font fontBlack = new Font(getFontHelveticaRegular(), 18, Font.NORMAL, BaseColor.BLACK);	        
			Paragraph p2 = new Paragraph("1. " + msg, fontBlack);
			document.add(p2);	        
	        document.close();	        
	        temp = baos.toByteArray();
			writer.close();
			baos.close();			
		} catch(DocumentException e) {
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
			try{if (baos!=null) baos.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	/**
	 * 
	 * @param nomeArquivo
	 * @param msg
	 * @throws Exception
	 */
	public static void gerarMensagemPDF(String nomeArquivo, String msg)throws Exception {
		setPrimaryBaseColor(BaseColor.RED);
		String msgErro = "Atenção, arquivo com problema: " + nomeArquivo;
		gerarPDF(nomeArquivo, msgErro);		
	}
	
	/**
	 * Obtém referência ao arquivo da fonte Helvetica na pasta webcontent/fonts/
	 * @return
	 * @throws Exception
	 */
	private static BaseFont getFontHelveticaRegular() throws Exception {
		String path = Paths.get(getDiretorioWebContent(), "fonts", FONT_HELVETICA_REGULAR).toString();
		return BaseFont.createFont(path, BaseFont.WINANSI, BaseFont.EMBEDDED);	
	}
	
	private static String getDiretorioWebContent() throws Exception {
		try {
			return new File(".").getCanonicalPath() + File.separator + "WebContent" + File.separator;
		} catch (IOException e) {			
			throw new Exception(e.getMessage());
		}   	 
	}
	
	public static BaseColor getPrimaryBaseColor() {
		return primaryBaseColor;
	}

	public static void setPrimaryBaseColor(BaseColor primaryBaseColor) {
		GerarPDFA.primaryBaseColor = primaryBaseColor;
	}

	/**
	 * Seta propriedades obrigatórias para conformidade de PDF/A
	 * @param writer
	 * @param document
	 */
	private static void setDefaultParamsToPDFA(PdfAWriter writer, Document document){
		writer.setPdfVersion(PdfWriter.PDF_VERSION_1_4);		
		writer.setViewerPreferences(PdfWriter.DisplayDocTitle);
		writer.setTagged();		
		setDefaultDocumentInfo(document);       
        writer.createXmpMetadata();
	}
	
	
	/**
	 * Seta propriedades obrigatórias para conformidade de PDF/A
	 * Informações do documento
	 * @param document
	 */
	private static void setDefaultDocumentInfo(Document document){
		document.addAuthor("Projudi - Processo Judicial");
        document.addSubject("Sem assunto");
        document.addLanguage("pt-BR");
        document.addCreationDate();
        document.addCreator("Projudi - Processo Judicial");
        document.addTitle("Sem título");  
	}
	
	
	/**
	 * Seta propriedades obrigatórias para conformidade de PDF/A
	 * @param writer
	 * @throws IOException
	 */
	private static void setDefaultOutputIntents(PdfAWriter writer) throws Exception{
		Path pathCsProfile = Paths.get(getDiretorioWebContent(), "fonts", "sRGB_CS_profile.icm");		
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(pathCsProfile.toFile()));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
	}
	
}
