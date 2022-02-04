package br.gov.go.tj.projudi.dt;


public class ProcessoParteEnderecoSPGDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4576350632175530603L;
	
	private String id;
	private String isnParte;
	private String tipoEndereco;
	private String nomeRua;
	private String numero;
	private String lote;
	private String quadra;
	private String bairro;
	private String municipio;
	private String complemento;
	private String cep;
	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getIsnParte() {
		return isnParte;
	}

	public void setIsnParte(String isnParte) {
		this.isnParte = isnParte;
	}

	public String getTipoEndereco() {
		return tipoEndereco;
	}

	public void setTipoEndereco(String tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public String getNomeRua() {
		return nomeRua;
	}

	public void setNomeRua(String nomeRua) {
		this.nomeRua = nomeRua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getQuadra() {
		return quadra;
	}

	public void setQuadra(String quadra) {
		this.quadra = quadra;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
}
