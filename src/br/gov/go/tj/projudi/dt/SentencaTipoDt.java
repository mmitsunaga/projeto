package br.gov.go.tj.projudi.dt;

public class SentencaTipoDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1982361289686916519L;

	private String sentencaTipoCodigoExterno;
	private String id;
	private String descricao;
	private String status;
	
	@Override
	public void setId(String id) {
		this.id = id;
		
	}

	@Override
	public String getId() {
		return this.id;
	}

	public String getSentencaTipoCodigoExterno() {
		return sentencaTipoCodigoExterno;
	}

	public void setSentencaTipoCodigoExterno(String sentencaTipoCodigoExterno) {
		this.sentencaTipoCodigoExterno = sentencaTipoCodigoExterno;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
