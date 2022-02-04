package br.gov.go.tj.projudi.dt.relatorios;

public class ProcessoEventoExecucaoRelatorioDt {

	private String evento;
	private String dataInicio;
	private String dataFim;
	private String condenacaoAnos;
	private String tempoCumpridoAnos;
	private String regime;
	private String observacao;
	private String quantidade;
	
	public ProcessoEventoExecucaoRelatorioDt() {
		evento = "";
		dataInicio = "";
		dataFim = "";
		condenacaoAnos = "";
		tempoCumpridoAnos = "";
		regime = "";
		observacao = "";
		quantidade = "";
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		if (evento != null) this.evento = evento;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		if (dataInicio != null) this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		if (dataFim != null) this.dataFim = dataFim;
	}

	public String getCondenacaoAnos() {
		return condenacaoAnos;
	}

	public void setCondenacaoAnos(String condenacaoAnos) {
		if (condenacaoAnos != null) this.condenacaoAnos = condenacaoAnos;
	}

	public String getTempoCumpridoAnos() {
		return tempoCumpridoAnos;
	}

	public void setTempoCumpridoAnos(String tempoCumpridoAnos) {
		if (tempoCumpridoAnos != null) this.tempoCumpridoAnos = tempoCumpridoAnos;
	}

	public String getRegime() {
		return regime;
	}

	public void setRegime(String regime) {
		if (regime != null) this.regime = regime;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		if (observacao != null) this.observacao = observacao;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

}
