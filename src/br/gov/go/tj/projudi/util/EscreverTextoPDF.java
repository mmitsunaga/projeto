package br.gov.go.tj.projudi.util;

/**
*
* @author Leandro de Souza Bernardes
* @email lsbernardes@tj.go.gov.br
*/

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;



public class EscreverTextoPDF {
	
	/**
	 * Método responsável em gerar o cabeçalho e a mensagem vertical em cada Pdf
	 * 
	 *@param processo para qual será gerado o cabeçalho e mensagem vertical
	 *
	 *@param arqDoc informações do arquivo que seram adicionados ao pdf
	 *
	 *@param contMovimentacao informa o número da movimentação no processo
	 *
	 *@param contArquivo informa o número do arquivo na Movimentação do processo
	 *
	 *@param input é um InputStream que representa o arquivo no qual será adicionado o cabeçalho e mensagem vertical
	 *
	 *
	 * 
	 */
	
	public static byte[] escreverTextoPDF(byte[] input, String pathImage ,String textoPrimeiraLinha, String textoSegundaLinha, String textoTerceiraLinha, String textoQuartaLinha, UsuarioDt usuarioDt, ProcessoDt processo)throws Exception {	
		ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp= null;
		try{
			input = GerarCabecalhoProcessoPDF.redimensionar(input);
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader,TextoPDF);
						
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {				
                over = pdfStamper.getOverContent(i);
                over.beginText();
                
                GerarCabecalhoProcessoPDF.imprimaCabecalhoLateralDireitaNaPagina(over, processo, i, reader, null, 0, null, usuarioDt.getNome(), bf, null);
                
                GerarCabecalhoProcessoPDF.imprimaRodapeNaPagina(over, pathImage, bf, textoPrimeiraLinha, textoSegundaLinha, textoTerceiraLinha, textoQuartaLinha, reader.getPageSize(i).getBottom());
                
                over.endText();
			}
			pdfStamper.close();		
			TextoPDF.close();
			reader.close();
			temp = TextoPDF.toByteArray();
		} catch(IOException e1) {
			throw e1;
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	public static byte[] escreverTextoPDF(byte[] input, String pathImage ,String textoPrimeiraLinha, String textoSegundaLinha, String textoTerceiraLinha, String textoQuartaLinha, boolean ehParaRedimensionar)throws Exception {	
		ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp= null;
		try{
			if (ehParaRedimensionar) input = GerarCabecalhoProcessoPDF.redimensionar(input);
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader,TextoPDF);
			
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {				
                over = pdfStamper.getOverContent(i);
                over.beginText();
                
                GerarCabecalhoProcessoPDF.imprimaRodapeNaPagina(over, pathImage, bf, textoPrimeiraLinha, textoSegundaLinha, textoTerceiraLinha, textoQuartaLinha, reader.getPageSize(i).getBottom());
                
                over.endText();
			}
			pdfStamper.close();		
			TextoPDF.close();
			reader.close();
			temp = TextoPDF.toByteArray();
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	public static byte[] escreverTextoPDF(byte[] input, String pathImage ,String textoPrimeiraLinha, String textoSegundaLinha, String textoTerceiraLinha, String textoQuartaLinha, ProcessoDt processoDt)throws Exception {	
		ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp= null;
		try{
			input = GerarCabecalhoProcessoPDF.redimensionar(input);
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			
			pdfStamper = new PdfStamper(reader,TextoPDF);
			
            PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {				
                over = pdfStamper.getOverContent(i);
                over.beginText();
                
                GerarCabecalhoProcessoPDF.imprimaCabecalhoNaPagina(over, reader, bf, processoDt, i);
               
                GerarCabecalhoProcessoPDF.imprimaRodapeNaPagina(over, pathImage, bf, textoPrimeiraLinha, textoSegundaLinha, textoTerceiraLinha, textoQuartaLinha, reader.getPageSize(i).getBottom());
                
                over.endText();
			}
			pdfStamper.close();		
			TextoPDF.close();
			reader.close();
			temp = TextoPDF.toByteArray();
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	
	public static byte[] escreverTextoPDF(byte[] input, String pathImage, String textoPrimeiraLinha, String textoSegundaLinha
			, String textoTerceiraLinha, String textoQuartaLinha, Object[] carimboTopoDireito) throws Exception {
		
		ByteArrayOutputStream TextoPDF = new ByteArrayOutputStream();
		PdfReader reader = null;
		PdfStamper pdfStamper = null;
		byte[] temp= null;
		
		try {
			input = GerarCabecalhoProcessoPDF.redimensionar(input);
			reader = new PdfReader(input);
			if (reader.getNumberOfPages() == 0) return input;
			pdfStamper = new PdfStamper(reader,TextoPDF);
			PdfContentByte over;
            BaseFont bf = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);            
			int totalPaginas = reader.getNumberOfPages() + 1;
			for (int i = 1; i < totalPaginas; i++) {				
                
				over = pdfStamper.getOverContent(i);
                over.beginText();
                                                
                // Carimbo direito: dados do processo
                if (carimboTopoDireito.length > 0){
                	over.setFontAndSize(bf, 8);
        	        float xOffset = 20;
        	        for (Object texto : carimboTopoDireito){
        	        	over.setColorFill(BaseColor.BLUE);
        	        	over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(texto),  (reader.getPageSize(i).getWidth() - xOffset), (reader.getPageSize(i).getHeight() + reader.getPageSize(i).getBottom() - 40), -90);
        	        	xOffset += 10;
                    }
                }
                
                // Carimbo inferior
                float tamanhoRodapePagina = reader.getPageSize(i).getBottom();                
                com.itextpdf.text.Image logo =  com.itextpdf.text.Image.getInstance(pathImage);
                logo.setAbsolutePosition(0f, tamanhoRodapePagina);
                over.addImage(logo);                
                over.setFontAndSize(bf, 9);
                over.setColorFill(BaseColor.BLUE);
                over.setTextMatrix(30, 30);
                over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(textoPrimeiraLinha), 62, 45 + tamanhoRodapePagina, 0);                
                over.setFontAndSize(bf, 8);
                over.setColorFill(BaseColor.BLUE);
                over.setTextMatrix(30, 30);
                over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(textoSegundaLinha), 62, 35 + tamanhoRodapePagina, 0);                
                over.setFontAndSize(bf, 8);
                over.setColorFill(BaseColor.BLUE);
                over.setTextMatrix(30, 30);
                over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(textoTerceiraLinha), 62, 25 + tamanhoRodapePagina, 0);                
                over.setFontAndSize(bf, 8);
                over.setColorFill(BaseColor.BLUE);
                over.setTextMatrix(30, 30);
                over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(textoQuartaLinha), 62, 15 + tamanhoRodapePagina, 0);
                
                over.endText();
			}
			
			pdfStamper.close();		
			TextoPDF.close();
			reader.close();
			temp = TextoPDF.toByteArray();
			
		} catch(Exception e) {
			try{if (pdfStamper!=null) pdfStamper.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			try{if (reader!=null) reader.close(); } catch(Exception ex ) {};
			try{if (TextoPDF!=null) TextoPDF.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
}
