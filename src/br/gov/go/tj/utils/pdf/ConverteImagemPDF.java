package br.gov.go.tj.utils.pdf;

/**
*
* @author Leandro de Souza Bernardes
* @email lsbernardes@tj.go.gov.br
*/

import java.io.ByteArrayOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;


public class ConverteImagemPDF {
	
	/**
	 * Método responsável em converter imagem em Pdf
	 * 
	 *@param bytes que representam a imagem
	 *
	 *@param outStream é um OutPutStream que recebera o resultado da conversão
	 *
	 */
	public static byte[] gerarPdfImagem(byte[] bytes)throws Exception {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] temp= null;
        try{
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Image jpg = Image.getInstance(bytes);
            document.add(jpg);
            document.close();
            temp = outputStream.toByteArray();
        }
        catch(Exception e) {
        	try{if (document!=null) document.close(); } catch(Exception ex ) {};
        	try{if (outputStream!=null) outputStream.close(); } catch(Exception ex ) {};
        	throw e;
        }
        return temp;
	}

}
