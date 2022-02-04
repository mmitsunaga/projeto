package br.gov.go.tj.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class ConverterPDF {
    
    public static String pdfToString(byte[] conteudo) throws IOException{
        
        PDFParser parser = new PDFParser(new ByteArrayInputStream(conteudo) );
        //faço o parse
        parser.parse();
        
        COSDocument cosDoc = parser.getDocument();        
        PDDocument pdDoc = new PDDocument(cosDoc);
        
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String palavras = pdfStripper.getText(pdDoc);  
        
        
        pdDoc.close();
        cosDoc.close();
        parser = null;
        pdfStripper = null;
        ////System.out.println(palavras);
        return palavras;
    }
}
