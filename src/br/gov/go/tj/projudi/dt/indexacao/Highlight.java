package br.gov.go.tj.projudi.dt.indexacao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Highlight {

	@JsonProperty(value="texto")
	private String texto[] = {};

	@JsonProperty(value="extra")
	private String extra[] = {};
	
	public String[] getTexto() {
		return texto;
	}	
	public void setTexto(String[] texto) {
		this.texto = texto;
	}

	public String[] getExtra() {
		return extra;
	}
	public void setExtra(String[] extra) {
		this.extra = extra;
	}
}