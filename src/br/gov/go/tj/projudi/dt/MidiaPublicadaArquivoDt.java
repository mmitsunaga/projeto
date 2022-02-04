package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class MidiaPublicadaArquivoDt implements Serializable {
	
	private static final long serialVersionUID = -6759300509174943139L;
	
	private String nomeArquivoOriginal;
	private String nomeArquivo;
	private String contentType;
	private long tamanhoArquivo;
	private String caminhoCompletoArquivoTemp;
	private boolean uploadCompleto;
	private String caminhoCompletoArquivoCeph;
	private String mensagemErro;

	public MidiaPublicadaArquivoDt() {}
	
	public String getNomeArquivoOriginal() {
		return nomeArquivoOriginal;
	}
	public void setNomeArquivoOriginal(String nomeArquivoOriginal) {
		this.nomeArquivoOriginal = nomeArquivoOriginal;
	}
		
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getTamanhoArquivo() {
		return tamanhoArquivo;
	}

	public void setTamanhoArquivo(long tamanhoArquivo) {
		this.tamanhoArquivo = tamanhoArquivo;
	}	

	public String getCaminhoCompletoArquivoTemp() {
		return caminhoCompletoArquivoTemp;
	}

	public void setCaminhoCompletoArquivoTemp(String caminhoCompletoArquivoTemp) {
		this.caminhoCompletoArquivoTemp = caminhoCompletoArquivoTemp;
	}

	public void setUploadCompleto(boolean uploadCompleto) {
		this.uploadCompleto = uploadCompleto;		
	}	
	
	public boolean isUploadCompleto() {
		return this.uploadCompleto;
	}
	
	public String getCaminhoCompletoArquivoCeph() {
		return caminhoCompletoArquivoCeph;
	}

	public void setCaminhoCompletoArquivoCeph(String caminhoCompletoArquivoCeph) {
		this.caminhoCompletoArquivoCeph = caminhoCompletoArquivoCeph;
	}
	
	public boolean isArquivoEnviadoCeph() {
		return this.caminhoCompletoArquivoCeph != null && this.caminhoCompletoArquivoCeph.trim().length() > 0;
	}

	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}
	
	public boolean isErro() {
		return this.mensagemErro != null && this.mensagemErro.trim().length() > 0;
	}
}
