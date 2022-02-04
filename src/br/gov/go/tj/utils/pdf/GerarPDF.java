package br.gov.go.tj.utils.pdf;

/**
*
* @author Leandro de Souza Bernardes
* @email lsbernardes@tj.go.gov.br
*/

import java.io.ByteArrayOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


public class GerarPDF {

	/**
	 * Método responsável em gerar um Pdf com o nome do aquivo e uma mensagem informando que o arquivo 
	 * não pode ser convertido em pdf.
	 * 
	 *@param nomeArquivo nome do arquivo.
	 *
	 *@param msg mensagem para o usuário
	 *
	 *@param outStream é um OutPutStream que recebera o resultado.
	 *
	 */
	public static byte[] gerarPDF(String nomeArquivo, String msg)throws Exception {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		ByteArrayOutputStream tempOut = new ByteArrayOutputStream(); 
		byte[] temp= null;
		try{
			PdfWriter.getInstance(document,tempOut);
			document.open();
			//Paragraph nomeArquvio = new Paragraph(nomeArquivo,FontFactory.getFont(FontFactory.HELVETICA));
			Paragraph nomeArquvio = new Paragraph(nomeArquivo,FontFactory.getFont(FontFactory.HELVETICA,18f, BaseColor.BLUE));
			nomeArquvio.setAlignment(Paragraph.ALIGN_CENTER);
			Chapter principal = new Chapter(nomeArquvio, 1);
			principal.setNumberDepth(0);
			Paragraph msgPDF = new Paragraph(msg,FontFactory.getFont(FontFactory.HELVETICA,18f, BaseColor.BLACK));
			principal.addSection(msgPDF);
			document.add(principal);
			document.close();
			temp = tempOut.toByteArray();
			tempOut.close();
		} catch(DocumentException e) {
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
			try{if (tempOut!=null) tempOut.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
	
	/**
	 * Método responsável em gerar um Pdf com o nome do aquivo e uma mensagem informando que o arquivo 
	 * não pode ser convertido em pdf.
	 * 
	 *@param nomeArquivo nome do arquivo.
	 *
	 *@param msg mensagem para o usuário
	 *
	 *@param outStream é um OutPutStream que recebera o resultado.
	 *
	 */
	public static byte[] gerarMensagemPDF(String nomeArquivo, String msg)throws Exception {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		ByteArrayOutputStream tempOut = new ByteArrayOutputStream(); 
		byte[] temp= null;
		try{
			PdfWriter.getInstance(document,tempOut);
			document.open();
			//Paragraph nomeArquvio = new Paragraph(nomeArquivo,FontFactory.getFont(FontFactory.HELVETICA));
			Paragraph avisoMensagem = new Paragraph("Atenção Arquivo Com Problema: "+nomeArquivo,FontFactory.getFont(FontFactory.HELVETICA,24f, BaseColor.RED));
			avisoMensagem.setAlignment(Paragraph.ALIGN_CENTER);
			Chapter principal = new Chapter(avisoMensagem, 1);
			principal.setNumberDepth(0);
			Paragraph msgPDF = new Paragraph(msg,FontFactory.getFont(FontFactory.HELVETICA,18f, BaseColor.BLACK));
			principal.addSection(msgPDF);
			document.add(principal);
			document.close();
			temp = tempOut.toByteArray();
			tempOut.close();
		} catch(DocumentException e) {
			try{if (document!=null) document.close(); } catch(Exception ex ) {};
			try{if (tempOut!=null) tempOut.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;
	}
}
