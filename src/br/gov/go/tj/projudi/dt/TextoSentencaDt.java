package br.gov.go.tj.projudi.dt;

public class TextoSentencaDt extends Dados {

	private static final long serialVersionUID = 8947192562382204424L;
	
	private String id;
	private String isnSpguSentenca;
	private String textoSentenca;
	private String textoSentencaCount;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsnSpguSentenca() {
		return isnSpguSentenca;
	}

	public void setIsnSpguSentenca(String isnSpguSentenca) {
		this.isnSpguSentenca = isnSpguSentenca;
	}

	public String getTextoSentenca() {
		return textoSentenca;
	}

	public void setTextoSentenca(String textoSentenca) {
		this.textoSentenca = textoSentenca;
	}

	public String getTextoSentencaCount() {
		return textoSentencaCount;
	}

	public void setTextoSentencaCount(String textoSentencaCount) {
		this.textoSentencaCount = textoSentencaCount;
	}
	
}
