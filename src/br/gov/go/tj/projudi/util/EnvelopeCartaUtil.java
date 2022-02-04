package br.gov.go.tj.projudi.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;

/**
 * Classe responsável em gerar um arquivo PDF com o envelope de uma e-carta
 * com informações de remetente e endereços de entrega.
 * @author mmitsunaga
 */
public class EnvelopeCartaUtil {
	
	private final String codigoRastreamento;
	private final String dataExpedicao;
	private final String nomeDestinatario;
	private final String enderecoDestinatario;
	private final String numeroDestinatario;
	private final String complementoDestinatario;
	private final String bairroDestinatario;
	private final String cidadeDestinatario;
	private final String ufDestinatario;
	private final String cepDestinatario;	
	private final byte[] anexo;
	private final InputStream templateSource;
	
	public String getCodigoRastreamento() {return codigoRastreamento;}
	public String getNomeDestinatario() {return nomeDestinatario;}
	public String getEnderecoDestinatario() {return enderecoDestinatario;}
	public String getNumeroDestinatario() {return numeroDestinatario;}
	public String getComplementoDestinatario() {return complementoDestinatario;}
	public String getBairroDestinatario() {return bairroDestinatario;}
	public String getCidadeDestinatario() {return cidadeDestinatario;}
	public String getUfDestinatario() {return ufDestinatario;}
	public String getCepDestinatario() {return cepDestinatario;}
	public String getDataExpedicao() {return dataExpedicao;}
	public InputStream getTemplateSource() {return templateSource;}
	public byte[] getAnexo() {return anexo;}
	
	private EnvelopeCartaUtil (Builder builder){
		this.anexo = builder.anexo;		
		this.codigoRastreamento  = builder.codigoRastreamento;
		this.dataExpedicao = builder.dataExpedicao;
		this.nomeDestinatario  = builder.nomeDestinatario;
		this.enderecoDestinatario  = builder.enderecoDestinatario;
		this.numeroDestinatario  = builder.numeroDestinatario;
		this.complementoDestinatario  = builder.complementoDestinatario;
		this.bairroDestinatario = builder.bairroDestinatario;
		this.cidadeDestinatario  = builder.cidadeDestinatario;
		this.ufDestinatario  = builder.ufDestinatario;		
		this.cepDestinatario  = builder.cepDestinatario;		
		this.templateSource = builder.templateSource;
	}
	
	public static class Builder {
		private String codigoRastreamento;
		private String dataExpedicao;
		private String nomeDestinatario;
		private String enderecoDestinatario;
		private String numeroDestinatario;
		private String complementoDestinatario;
		private String bairroDestinatario;
		private String cidadeDestinatario;
		private String ufDestinatario;
		private String cepDestinatario;	
		private byte[] anexo;
		private final InputStream templateSource;
		
		public Builder(InputStream templateSource){this.templateSource = templateSource;}
		public Builder setCodigoRastreamento(String codigoRastreamento) {this.codigoRastreamento = codigoRastreamento;return this;}
		public Builder setNomeDestinatario(String nomeDestinatario) {this.nomeDestinatario = nomeDestinatario;return this;}
		public Builder setEnderecoDestinatario(String enderecoDestinatario) {this.enderecoDestinatario = enderecoDestinatario;return this;}
		public Builder setNumeroDestinatario(String numeroDestinatario) {this.numeroDestinatario = numeroDestinatario;return this;}
		public Builder setComplementoDestinatario(String complementoDestinatario) {this.complementoDestinatario = complementoDestinatario;return this;}
		public Builder setBairroDestinatario(String bairroDestinatario) {this.bairroDestinatario = bairroDestinatario;return this;}
		public Builder setCidadeDestinatario(String cidadeDestinatario) {this.cidadeDestinatario = cidadeDestinatario;return this;}
		public Builder setUfDestinatario(String ufDestinatario) {this.ufDestinatario = ufDestinatario;return this;}
		public Builder setCepDestinatario(String cepDestinatario) {this.cepDestinatario = cepDestinatario;return this;}
		public Builder setDataExpedicao(String dataPostagem) {this.dataExpedicao = dataPostagem;return this;}
		public Builder setAnexo(byte[] anexo) {this.anexo = anexo;return this;}
		private void validarCampos(){}
		public EnvelopeCartaUtil build(){validarCampos();return new EnvelopeCartaUtil(this);}
	}
	
	private byte [] criarEnvelopeCarta() throws IOException, DocumentException {
		byte[] arquivo = null;
		String enderecoNumeroComplemento = "";
		String cepCidadeUf = "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfReader reader = new PdfReader(getTemplateSource());
		PdfStamper stamper = new PdfStamper(reader, baos);
		PdfContentByte over = stamper.getOverContent(1);
		Rectangle pageSize = reader.getPageSize(1);		
		over.beginText();		
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);		
		over.setFontAndSize(bf, 10);
    	over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
        over.setColorFill(BaseColor.BLACK); 
        
        if(ValidacaoUtil.isNaoVazio(getEnderecoDestinatario())) 	enderecoNumeroComplemento += getEnderecoDestinatario();
        if(ValidacaoUtil.isNaoVazio(getNumeroDestinatario())) 		enderecoNumeroComplemento += ", " + getNumeroDestinatario();
        if(ValidacaoUtil.isNaoVazio(getComplementoDestinatario())) 	enderecoNumeroComplemento += ", "+ getComplementoDestinatario();
        cepCidadeUf = Funcoes.formatarCep(getCepDestinatario()) + " " + getCidadeDestinatario() + " - " + getUfDestinatario();
        
       	over.showTextAligned(Element.ALIGN_LEFT, getCodigoRastreamento(), 410, pageSize.getTop() - 165, 0);
       	over.showTextAligned(Element.ALIGN_LEFT, getDataExpedicao(), 470, pageSize.getTop() - 210, 0);
       	over.showTextAligned(Element.ALIGN_LEFT, getNomeDestinatario().toUpperCase(), 105, pageSize.getTop() - 230, 0);
		over.showTextAligned(Element.ALIGN_LEFT, enderecoNumeroComplemento.toUpperCase(), 105, pageSize.getTop() - 245, 0);
		over.showTextAligned(Element.ALIGN_LEFT, getBairroDestinatario().toUpperCase(), 105, pageSize.getTop() - 260, 0);
		over.showTextAligned(Element.ALIGN_LEFT, cepCidadeUf.toUpperCase(), 105, pageSize.getTop() - 275, 0);
		
		over.endText();
		stamper.close();
		reader.close();		
		arquivo = baos.toByteArray();
		baos.flush();
		baos.close();		
		return arquivo;
	}
	
	/**
	 * Gera o envelope da carta e retorna um array de bytes
	 * @return
	 * @throws Exception 
	 */
	public byte[] gerar() throws Exception {
		byte[] arquivo = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4);
		PdfCopy pdfCopy = new PdfCopy(document, baos);
		document.open();
		pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, getAnexo());
		pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, criarEnvelopeCarta());
		pdfCopy.close();
		document.close();		
		arquivo = baos.toByteArray();
		baos.flush();
		baos.close();
		return arquivo;
	}
	
	/**
	 * Gera o envelope da carta e gera o arquivo no stream de saida
	 * @param outputPath
	 * @throws Exception 
	 */
	public void gerar(OutputStream outputStream) throws Exception {
		BufferedOutputStream bos = new BufferedOutputStream(outputStream);
		Document document = new Document(PageSize.A4);
		PdfCopy pdfCopy = new PdfCopy(document, bos);		
		document.open();
		pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, getAnexo());
		pdfCopy = ConcatenatePDF.concatPDFs(pdfCopy, criarEnvelopeCarta());		
		pdfCopy.close();
		document.close();
		bos.flush();
		bos.close();		
	}
	
}