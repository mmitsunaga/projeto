package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

public class ProcessoExecucaoBeanDt  extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 451685097334328043L;
	private String processoExecucaoPenalNumeroCompleto;
	private String comarcaOrigem;
	private String dataAcordao;
	private String dataDistribuicao;
	private String dataPronuncia;
	private String dataSentenca;
	private String dataTransitoJulgado;
	private String dataTransitoJulgadoMP;
	private String dataDenuncia;
	private String dataAdmonitoria;
	private String dataInicioCumprimentoPena;
	private String numeroAcaoPenal;
	private String varaOrigem;
	private String textoSursis;
	private String textoCondenacao;
	private String textoModalidade;
	private String regime;
	private String localCumprimentoPena;
	private String pena;

	public ProcessoExecucaoBeanDt() {
		processoExecucaoPenalNumeroCompleto = "";
		comarcaOrigem = "";
		dataAcordao = "";
		dataDistribuicao = "";
		dataPronuncia = "";
		dataSentenca = "";
		dataTransitoJulgado = "";
		dataTransitoJulgadoMP = "";
		dataDenuncia = "";
		dataAdmonitoria = "";
		dataInicioCumprimentoPena = "";
		numeroAcaoPenal = "";
		varaOrigem = "";
		textoSursis = "";
		textoCondenacao = "";
		textoModalidade = "";
		regime = "";
		localCumprimentoPena = "";
		pena = "";
	}
	
	public String getProcessoExecucaoPenalNumeroCompleto() {
		return processoExecucaoPenalNumeroCompleto;
	}

	public void setProcessoExecucaoPenalNumeroCompleto(
			String processoExecucaoPenalNumeroCompleto) {
		this.processoExecucaoPenalNumeroCompleto = processoExecucaoPenalNumeroCompleto;
	}

	public String getComarcaOrigem() {
		return comarcaOrigem;
	}

	public void setComarcaOrigem(String comarcaOrigem) {
		this.comarcaOrigem = comarcaOrigem;
	}

	public String getDataAcordao() {
		return dataAcordao;
	}

	public void setDataAcordao(String dataAcordao) {
		this.dataAcordao = dataAcordao;
	}

	public String getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setDataDistribuicao(String dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	public String getDataPronuncia() {
		return dataPronuncia;
	}

	public void setDataPronuncia(String dataPronuncia) {
		this.dataPronuncia = dataPronuncia;
	}

	public String getDataSentenca() {
		return dataSentenca;
	}

	public void setDataSentenca(String dataSentenca) {
		this.dataSentenca = dataSentenca;
	}

	public String getDataTransitoJulgado() {
		return dataTransitoJulgado;
	}

	public void setDataTransitoJulgado(String dataTransitoJulgado) {
		this.dataTransitoJulgado = dataTransitoJulgado;
	}

	public String getDataDenuncia() {
		return dataDenuncia;
	}

	public void setDataDenuncia(String dataDenuncia) {
		this.dataDenuncia = dataDenuncia;
	}

	public String getDataAdmonitoria() {
		return dataAdmonitoria;
	}

	public void setDataAdmonitoria(String dataAdmonitoria) {
		this.dataAdmonitoria = dataAdmonitoria;
	}

	public String getDataInicioCumprimentoPena() {
		return dataInicioCumprimentoPena;
	}

	public void setDataInicioCumprimentoPena(String dataInicioCumprimentoPena) {
		this.dataInicioCumprimentoPena = dataInicioCumprimentoPena;
	}

	public String getNumeroAcaoPenal() {
		return numeroAcaoPenal;
	}

	public void setNumeroAcaoPenal(String numeroAcaoPenal) {
		this.numeroAcaoPenal = numeroAcaoPenal;
	}

	public String getVaraOrigem() {
		return varaOrigem;
	}

	public void setVaraOrigem(String varaOrigem) {
		this.varaOrigem = varaOrigem;
	}

	public String getTextoSursis() {
		return textoSursis;
	}

	public void setTextoSursis(String textoSursis) {
		this.textoSursis = textoSursis;
	}

	public String getTextoCondenacao() {
		return textoCondenacao;
	}

	public void setTextoCondenacao(String textoCondenacao) {
		this.textoCondenacao = textoCondenacao;
	}

	public String getTextoModalidade() {
		return textoModalidade;
	}

	public void setTextoModalidade(String textoModalidade) {
		this.textoModalidade = textoModalidade;
	}

	public String getRegime() {
		return regime;
	}

	public void setRegime(String regime) {
		this.regime = regime;
	}

	public String getLocalCumprimentoPena() {
		return localCumprimentoPena;
	}

	public void setLocalCumprimentoPena(String localCumprimentoPena) {
		this.localCumprimentoPena = localCumprimentoPena;
	}

	public String getDataTransitoJulgadoMP() {
		return dataTransitoJulgadoMP;
	}

	public void setDataTransitoJulgadoMP(String dataTransitoJulgadoMP) {
		this.dataTransitoJulgadoMP = dataTransitoJulgadoMP;
	}

	public String getPena() {
		return pena;
	}

	public void setPena(String pena) {
		this.pena = pena;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
	}
}
