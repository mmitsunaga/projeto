package br.gov.go.tj.projudi.dt;

public class IntimacaoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3089487337134350305L;
	private String extracaoData;
	private String diarioJusticaNumero;
	private String publicadoData;
	private String descricaoFase;
	private String circulado;
	private String folha;
	private String despacho;

	public IntimacaoDt(String extracaoData, String diarioJusticaNumero,
			String publicadoData, String circulado,
			String folha, String despacho) {
		this.extracaoData= extracaoData;
		this.diarioJusticaNumero= diarioJusticaNumero;
		this.publicadoData= publicadoData;
		this.circulado= circulado;
		this.folha = folha;
		this.despacho = despacho;
	}
	public IntimacaoDt() {
		extracaoData="";
		diarioJusticaNumero="";
		publicadoData="";
		descricaoFase="";
		circulado="";
		folha="";
		despacho="";
	}
	public String getExtracaoData() {
		return extracaoData;
	}

	public void setExtracaoData(String extracaoData) {
		this.extracaoData = extracaoData;
	}

	public String getDiarioJusticaNumero() {
		return diarioJusticaNumero;
	}

	public void setDiarioJusticaNumero(String diarioJusticaNumero) {
		this.diarioJusticaNumero = diarioJusticaNumero;
	}

	public String getPublicadoData() {
		return publicadoData;
	}

	public void setPublicadoData(String publicadoData) {
		this.publicadoData = publicadoData;
	}

	public String getDescricaoFase() {
		return descricaoFase;
	}

	public void setDescricaoFase(String descricaoFase) {
		this.descricaoFase = descricaoFase;
	}

	public String getCirculado() {
		return circulado;
	}

	public void setCirculado(String circulado) {
		this.circulado = circulado;
	}

	public String getFolha() {
		return folha;
	}

	public void setFolha(String folha) {
		this.folha = folha;
	}

	public String getDespacho() {
		return despacho;
	}

	public void setDespacho(String despacho) {
		this.despacho = despacho;
	}


	@Override
	public void setId(String id) {
		

	}

	@Override
	public String getId() {
		
		return null;
	}

}
