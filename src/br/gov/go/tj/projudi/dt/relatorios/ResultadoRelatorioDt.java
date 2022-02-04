package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;

public class ResultadoRelatorioDt implements Serializable {

	private static final long serialVersionUID = -563542451669710858L;
	
	private String nomeArquivo;
	
	private byte[] conteudoArquivo;
	
	private String contentType;
	
	private long quantidadeRegistros;
	
	public ResultadoRelatorioDt() {		
	}
	
	public ResultadoRelatorioDt(String nomeArquivo, 
			                    byte[] conteudoArquivo,
			                    String mimeType,
			                    long quantidadeRegistros) {
		this.nomeArquivo = nomeArquivo;
		this.conteudoArquivo = conteudoArquivo;
		this.contentType = mimeType;
		this.setQuantidadeRegistros(quantidadeRegistros);
	}
	
	public static ResultadoRelatorioDt CriePDF(String nomeArquivo, 
											   byte[] conteudoArquivo,
											   long quantidadeDeRegistros) {	
		return new ResultadoRelatorioDt(nomeArquivo, conteudoArquivo, "application/pdf", quantidadeDeRegistros);
	}
	
	public static ResultadoRelatorioDt CrieCSV(String nomeArquivo, 
											   byte[] conteudoArquivo,
											   long quantidadeDeRegistros) {	
		return new ResultadoRelatorioDt(nomeArquivo, conteudoArquivo, "text/csv", quantidadeDeRegistros);
	}
	
	public static ResultadoRelatorioDt CrieTXT(String nomeArquivo, 
											   byte[] conteudoArquivo,
											   long quantidadeDeRegistros) {	
		return new ResultadoRelatorioDt(nomeArquivo, conteudoArquivo, "text/plain", quantidadeDeRegistros);
	}
	
	public static ResultadoRelatorioDt CrieOdt(String nomeArquivo, 
				   							   byte[] conteudoArquivo,
				   							   long quantidadeDeRegistros) {	
		return new ResultadoRelatorioDt(nomeArquivo, conteudoArquivo, "application/vnd.oasis.opendocument.text", quantidadeDeRegistros);
	}
	
	public static ResultadoRelatorioDt Criejpeg(String nomeArquivo, 
				   							    byte[] conteudoArquivo,
				   							    long quantidadeDeRegistros) {	
		return new ResultadoRelatorioDt(nomeArquivo, conteudoArquivo, "image/jpeg", quantidadeDeRegistros);
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public byte[] getConteudoArquivo() {
		return conteudoArquivo;
	}

	public void setConteudoArquivo(byte[] conteudoArquivo) {
		this.conteudoArquivo = conteudoArquivo;
	}	

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(long quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}	
}
