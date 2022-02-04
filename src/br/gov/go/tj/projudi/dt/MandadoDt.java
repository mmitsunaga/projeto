package br.gov.go.tj.projudi.dt;

public class MandadoDt extends br.gov.go.tj.projudi.dt.Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5713858669148500331L;
	private String tipo;
	private String emissaoData;
	private String recebimentoData;
	private String distribuicaoData;
	private String audienciaData;
	private String partesNome;
	private String escrivania;
	private String devolucaoEscrivaniaData;
	private String oficial;
	private String recebimentoOficialData;
	private String situacao;
	private String prazoCumprimentoData;
	private String devolucaoOficialData;
	private String motivoDevolucao;
	private String numero;
	
	
	


	public MandadoDt(String numero, String tipo, String emissaoData, String recebimentoData,
			String distribuicaoData, String audienciaData, String partesNome,
			String escrivania, String devolucaoEscrivaniaData, String oficial,
			String recebimentoOficialData, String situacao,
			String prazoCumprimentoData, String devolucaoOficialData,
			String motivoDevolucao) {
		super();
		this.numero = numero;
		this.tipo = tipo;
		this.emissaoData = emissaoData;
		this.recebimentoData = recebimentoData;
		this.distribuicaoData = distribuicaoData;
		this.audienciaData = audienciaData;
		this.partesNome = partesNome;
		this.escrivania = escrivania;
		this.devolucaoEscrivaniaData = devolucaoEscrivaniaData;
		this.oficial = oficial;
		this.recebimentoOficialData = recebimentoOficialData;
		this.situacao = situacao;
		this.prazoCumprimentoData = prazoCumprimentoData;
		this.devolucaoOficialData = devolucaoOficialData;
		this.motivoDevolucao = motivoDevolucao;
	}
	
	public MandadoDt() {
		super();
		this.numero = "";
		this.tipo = "";
		this.emissaoData = "";
		this.recebimentoData = "";
		this.distribuicaoData = "";
		this.audienciaData = "";
		this.partesNome = "";
		this.escrivania = "";
		this.devolucaoEscrivaniaData = "";
		this.oficial = "";
		this.recebimentoOficialData = "";
		this.situacao = "";
		this.prazoCumprimentoData = "";
		this.devolucaoOficialData = "";
		this.motivoDevolucao = "";
	}
	
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEmissaoData() {
		return emissaoData;
	}
	public void setEmissaoData(String emissaoData) {
		this.emissaoData = emissaoData;
	}
	public String getRecebimentoData() {
		return recebimentoData;
	}
	public void setRecebimentoData(String recebimentoData) {
		this.recebimentoData = recebimentoData;
	}
	public String getDistribuicaoData() {
		return distribuicaoData;
	}
	public void setDistribuicaoData(String distribuicaoData) {
		this.distribuicaoData = distribuicaoData;
	}
	public String getAudienciaData() {
		return audienciaData;
	}
	public void setAudienciaData(String audienciaData) {
		this.audienciaData = audienciaData;
	}
	public String getPartesNome() {
		return partesNome;
	}
	public void setPartesNome(String partesNome) {
		this.partesNome = partesNome;
	}
	public String getEscrivania() {
		return escrivania;
	}
	public void setEscrivania(String escrivania) {
		this.escrivania = escrivania;
	}
	public String getDevolucaoEscrivaniaData() {
		return devolucaoEscrivaniaData;
	}
	public void setDevolucaoEscrivaniaData(String devolucaoEscrivaniaData) {
		this.devolucaoEscrivaniaData = devolucaoEscrivaniaData;
	}
	public String getOficial() {
		return oficial;
	}
	public void setOficial(String oficial) {
		this.oficial = oficial;
	}
	public String getRecebimentoOficialData() {
		return recebimentoOficialData;
	}
	public void setRecebimentoOficialData(String recebimentoOficialData) {
		this.recebimentoOficialData = recebimentoOficialData;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getPrazoCumprimentoData() {
		return prazoCumprimentoData;
	}
	public void setPrazoCumprimentoData(String prazoCumprimentoData) {
		this.prazoCumprimentoData = prazoCumprimentoData;
	}
	public String getDevolucaoOficialData() {
		return devolucaoOficialData;
	}
	public void setDevolucaoOficialData(String devolucaoOficialData) {
		this.devolucaoOficialData = devolucaoOficialData;
	}
	public String getMotivoDevolucao() {
		return motivoDevolucao;
	}
	public void setMotivoDevolucao(String motivoDevolucao) {
		this.motivoDevolucao = motivoDevolucao;
	}
	@Override
	public void setId(String id) {
		
		
	}
	@Override
	public String getId() {
		
		return null;
	}

}
