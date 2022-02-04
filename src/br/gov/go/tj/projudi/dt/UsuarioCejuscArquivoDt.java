package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.dt.Dados;

public class UsuarioCejuscArquivoDt extends Dados {
	
	private String id;
	private String nomeArquivo;
	private String conteudoArquivo;
	private String contentType;
	private String dataInsercao;
	private UsuarioCejuscDt usuarioCejuscDt;
	
	private byte[] bytes;

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getConteudoArquivo() {
		return conteudoArquivo;
	}
	public void setConteudoArquivo(String conteudoArquivo) {
		this.conteudoArquivo = conteudoArquivo;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDataInsercao() {
		return dataInsercao;
	}
	public void setDataInsercao(String dataInsercao) {
		this.dataInsercao = dataInsercao;
	}
	public UsuarioCejuscDt getUsuarioCejuscDt() {
		return usuarioCejuscDt;
	}
	public void setUsuarioCejuscDt(UsuarioCejuscDt usuarioCejuscDt) {
		this.usuarioCejuscDt = usuarioCejuscDt;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public String getPropriedades() {
		return "UsuarioCejuscArquivoDt [id=" + id + ", nomeArquivo=" + nomeArquivo + ", conteudoArquivo="
				+ conteudoArquivo + ", contentType=" + contentType + ", dataInsercao=" + dataInsercao
				+ ", usuarioCejuscDt.getId=" + usuarioCejuscDt.getId() + "]";
	}
	
}
