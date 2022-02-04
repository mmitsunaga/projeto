package br.gov.go.tj.utils.pdf;

/**
*
* @author Leandro de Souza Bernardes
* @email lsbernardes@tj.go.gov.br
*/

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class ConcatenatePDF {
	
	/**
	 * Método responsável em concatenar Pdf's
	 * 
	 *@param arq1  pdf no formato InputStream
	 *
	 *@param arq2 pdf no formato InputStream
	 *
	 *@param outStream é um OutPutStream que recebera o resultado da concatenação
	 *
	 * @throws Exception
	 */
	public static byte[] concatenaPdf(byte[] arq1, byte[] arq2) throws Exception {
		ByteArrayOutputStream resultado = new ByteArrayOutputStream();
		Document document = new Document();
		byte[] temp = null;
		
		try{
			PdfCopy copy = new PdfCopy(document, resultado);
			document.open();
			int f = 0;
			int n;
			while (f < 2) {
				PdfReader reader;
				if (f == 0)
					reader = new PdfReader(arq1);
				else
					reader = new PdfReader(arq2);
				// loop over the pages in that document
				n = reader.getNumberOfPages();
				for (int page = 0; page < n;) {
					copy.addPage(copy.getImportedPage(reader, ++page));
				}
				copy.freeReader(reader);
				reader.close();
				f++;
			}		
			document.close();	
			temp = resultado.toByteArray();
			resultado.close();
		}catch(Exception e){
			try{if (document!=null) document.close(); } catch(Exception ex ) {};			
			try{if (resultado!=null) resultado.close(); } catch(Exception ex ) {};
			throw e;
		} 
		
		return temp;
	}
	
	public static void concatPDFs(List<InputStream> streamOfPDFFiles,  OutputStream outputStream, boolean paginate) { 
  
        Document document = new Document();  
        try{ 
            List<InputStream> pdfs = streamOfPDFFiles;  
            List<PdfReader> readers = new ArrayList<PdfReader>();  
            int totalPages = 0;  
            Iterator<InputStream> iteratorPDFs = pdfs.iterator();  
  
            // Create Readers for the pdfs.  
            while (iteratorPDFs.hasNext()) { 
                InputStream pdf = iteratorPDFs.next();  
                PdfReader pdfReader = new PdfReader(pdf);  
                readers.add(pdfReader);  
                totalPages += pdfReader.getNumberOfPages();  
            }  
            // Create a writer for the outputstream  
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);  
  
            document.open();  
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);  
            PdfContentByte cb = writer.getDirectContent(); // Holds the PDF  
            // data  
  
            PdfImportedPage page;  
            int currentPageNumber = 0;  
            int pageOfCurrentReaderPDF = 0;  
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();  
  
            // Loop through the PDF files and add to the output.  
            while (iteratorPDFReader.hasNext()) { 
                PdfReader pdfReader = iteratorPDFReader.next();  
  
                // Create a new page in the target for each source page.  
                while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) { 
                    document.newPage();  
                    pageOfCurrentReaderPDF++;  
                    currentPageNumber++;  
                    page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);  
                    cb.addTemplate(page, 0, 0);  
  
                    // Code for pagination.  
                    if (paginate) { 
                        cb.beginText();  
                        cb.setFontAndSize(bf, 9);                        
                        cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""  
                                + currentPageNumber + " of " + totalPages, 520,  
                                5, 0);  
                        cb.endText();  
                    }  
                }  
                pageOfCurrentReaderPDF = 0;  
            }  
            outputStream.flush();  
            document.close();  
            outputStream.close();  
        } catch(Exception e) { 
            e.printStackTrace();  
        } finally{ 
            if (document.isOpen())  
                document.close();  
            try{ 
                if (outputStream != null)  
                    outputStream.close();  
            } catch(IOException ioe) { 
                ioe.printStackTrace();  
            }  
        }  
    } 	
	
	/**
	 * Método responsável em concatenar os pdf´s, onde uma instancia de PdfCopy é reaproveitada
	 * @param copy
	 * @param arq - array de bytes com o conteúdo a ser concatenado
	 * @return instância válida e com as páginas concatenadas do documento
	 * @throws Exception
	 */
	public static PdfCopy concatPDFs(PdfCopy copy, byte[] arq) throws Exception {
		try {
			PdfReader reader = new PdfReader(arq);
			int n = reader.getNumberOfPages();
			for (int page = 0; page < n;) {				
				copy.addPage(copy.getImportedPage(reader, ++page));
			}
			copy.freeReader(reader);
			reader.close();
			return copy;
		}catch(Exception e){
			throw e;
		}		
	}
	
	/**
	 * Método responsável em concatenar os pdf´s, onde uma instancia de PdfSmartCopy é reaproveitada
	 * O PdfSmartCopy reutiliza imagens e outros objetos em comuns na hora de concatenar pdf´s
	 * resultando num arquivo final menor. Porém, há um custo no uso de memória e tempo de processamento.
	 * @param copy
	 * @param arq - array de bytes com o conteúdo a ser concatenado
	 * @return instância válida e com as páginas concatenadas do documento
	 * @throws Exception
	 */
	public static PdfSmartCopy concatPDFs(PdfSmartCopy copy, byte[] arq) throws Exception {
		try {
			PdfReader reader = new PdfReader(arq);
			int n = reader.getNumberOfPages();
			for (int page = 0; page < n;) {
				copy.addPage(copy.getImportedPage(reader, ++page));
			}
			copy.freeReader(reader);
			reader.close();
			return copy;
		}catch(Exception e){
			throw e;
		}		
	}
	
	/**
	 * Divide um arquivo PDF em outros menores, com tamanho específico.
	 * @param pathInputFilename
	 * @param pathOutputFilename
	 * @param preferedSize
	 */
	private static void _splitPDF (String pathInputFilename, String pathOutputFilename, float preferedSize) throws Exception {
		
		int pageNumber = 1;
		long currentPageSize = 0;
		float combinedPageSize = 0;		
		
		// Abre o arquivo de entrada para leitura
		RandomAccessSourceFactory f = new RandomAccessSourceFactory();
		RandomAccessSource randomAccessSource = f.createBestSource(pathInputFilename);
		PdfReader reader = new PdfReader(new RandomAccessFileOrArray(randomAccessSource), null);
		
		// Cria o arquivo de saída
		// Retirar a extensão e concatenar um número identificador
		int posExt = pathOutputFilename.lastIndexOf(".pdf");		
		String outputFilename = posExt > -1 ? pathOutputFilename.substring(0, posExt) : pathOutputFilename;
		
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document,new FileOutputStream(outputFilename + "_1.pdf"));
		document.open();
		
		// Obtém o número de páginas do arquivo de entrada
		int numberOfPages = reader.getNumberOfPages();
		
		// Percorre as páginas do arquivo de entrada e separa em arquivos
		// onde a soma das páginas deve ser menor que o tamanho atual
		for (int index = 1; index <= numberOfPages; index++ ){
			
			// Cria um novo arquivo, a partir do segundo
			if (combinedPageSize == 0 && index != 1){
				document = new Document();
				pageNumber++;				
				copy = new PdfCopy(document,new FileOutputStream(outputFilename + "_" + pageNumber + ".pdf"));
				document.open();
			}
			
			// Importa a página atual para o arquivo de saída
			copy.addPage(copy.getImportedPage(reader, index));
			
			// Obtém o tamanho da página atual copiada
			currentPageSize = copy.getCurrentDocumentSize();
			 
			// Converte bytes para kilobytes
			combinedPageSize = (float) currentPageSize / 1024;
			 
			// Fecha o documento se o tamanho do arquivo foi atingido ou se é a última página do arquivo de entrada
			if (combinedPageSize > preferedSize || index == numberOfPages) {
			     document.close();
			     combinedPageSize = 0;
			}
		}
		
		copy.freeReader(reader);
		copy.close();
		
	}
	
	/**
	 * Divide um arquivo PDF em outros menores, com tamanho específico.
	 * @param pathInputFilename
	 * @param preferedSize
	 * @throws Exception
	 */
	public static void splitPDF (String pathInputFilename, float preferedSize) throws Exception {
		_splitPDF (pathInputFilename, pathInputFilename, preferedSize);
	}
	
	/**
	 * 
	 * @param pathInputFilename
	 * @param pathOutputFilename
	 * @param preferedSize
	 * @throws Exception
	 */
	public static void splitPDF (String pathInputFilename, String pathOutputFilename, float preferedSize) throws Exception {
		_splitPDF (pathInputFilename, pathOutputFilename, preferedSize);		
	}
		
}
