package br.gov.go.tj.projudi.dt;

public class SentencaDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4431609987466121536L;
	private String sentencaData;
	private String sentencaTipo;
	private String transitoJulgadoData;
	private String texto;

	public SentencaDt(String sentencaData, String sentencaTipo,
			String transitoJulgadoData, String texto) {
		super();
		this.sentencaData = sentencaData;
		this.sentencaTipo = sentencaTipo;
		this.transitoJulgadoData = transitoJulgadoData;
		this.texto = texto;
	}
	public SentencaDt() {}

	
	public void setId(String id) {
		

	}

	public String getId() {
		
		return null;
	}

	public String getSentencaData() {
		return sentencaData;
	}

	public void setSentencaData(String sentencaData) {
		this.sentencaData = sentencaData;
	}

	public String getSentencaTipo() {
		return sentencaTipo;
	}

	public void setSentencaTipo(String sentencaTipo) {
		this.sentencaTipo = sentencaTipo;
	}

	public String getTransitoJulgadoData() {
		return transitoJulgadoData;
	}

	public void setTransitoJulgadoData(String transitoJulgadoData) {
		this.transitoJulgadoData = transitoJulgadoData;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}


}
