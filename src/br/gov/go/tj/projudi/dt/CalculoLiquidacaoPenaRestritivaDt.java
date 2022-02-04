package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;


/**
 * Este objeto será utilizado no cálculo de liquidação de penas.
 * @author wcsilva
 */
public class CalculoLiquidacaoPenaRestritivaDt extends Dados {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2249877652171205198L;
	
	//variáveis utilizadas no cálculo de prestação de serviço à comunidade
	private String dataInicioPSC;
	private String totalCondenacaoPSCHoras;
	private String horasCumpridasPSC;
	private String horasRestantePSC;
	
	//variáveis utilizadas no cálculo de limitação de fim de semana
	private String totalCondenacaoLFSDias;
	private String totalCondenacaoLFSAnos;
	private String tempoCumpridoLFSDias;
	private String tempoCumpridoLFSAnos;
	private String tempoRestanteLFSDias;
	private String tempoRestanteLFSAnos;
	private String tempoInterrupcaoLFSDias;
	private String tempoInterrupcaoLFSAnos;
	private String dataTerminoLFS;
	private String observacaoCalculoLFS;
	
	//variáveis utilizadas no cálculo da prestação pecuniária
	private String totalPagamentoPEC;
	private String valorPagoPEC;
	private String valorDevidoPEC;

	//variáveis utilizadas no cálculo do pagamento de cestas básicas
	private String totalPagamentoPCB;
	private String qtdPagaPCB;
	private String qtdDevidaPCB;

	//variáveis utilizadas no cálculo de interdição temporária de direito
	private String totalCondenacaoITDDias;
	private String totalCondenacaoITDAnos;
	private String tempoCumpridoITDDias;
	private String tempoCumpridoITDAnos;
	private String tempoRestanteITDDias;
	private String tempoRestanteITDAnos;
	private String dataTerminoITD;
	private String observacaoCalculoITD;
	
//	private String observacao;
	
	//variáveis utilizadas para impressão do relatório em pdf
	private String tempoTotalCondenacaoAnos; 
	private String tempoCumpridoUltimoEventoAnos;
	private String tempoTotalCondenacaoRemanescenteAnos;
	private String tempoTotalComutacaoAnos;
	private String tempoTotalComutacaoDias;
	private String tempoRestanteUltimoEventoAnos;
	private String visualizaRestantePenaUltimoEvento;
	
	public CalculoLiquidacaoPenaRestritivaDt(){
		this.limpar();
	}
	
	public void limpar(){
		dataInicioPSC = "";
		totalCondenacaoPSCHoras = "0";
		horasCumpridasPSC = "";
		horasRestantePSC = "";
		
		totalCondenacaoLFSDias = "0";
		totalCondenacaoLFSAnos = "";
		tempoCumpridoLFSDias = "";
		tempoCumpridoLFSAnos = "";
		tempoRestanteLFSDias = "";
		tempoRestanteLFSAnos = "";
		tempoInterrupcaoLFSDias = "";
		tempoInterrupcaoLFSAnos = "";
		dataTerminoLFS = "";
		observacaoCalculoLFS = "";
		
		totalPagamentoPEC = "";
		valorPagoPEC = "";
		valorDevidoPEC = "";

		totalPagamentoPCB = "";
		qtdPagaPCB = "";
		qtdDevidaPCB = "";
		
		totalCondenacaoITDDias = "0";
		totalCondenacaoITDAnos = "";
		tempoCumpridoITDDias = "";
		tempoCumpridoITDAnos = "";
		tempoRestanteITDDias = "";
		tempoRestanteITDAnos = "";
		dataTerminoITD = "";
		observacaoCalculoITD = "";
		
//		observacao = "";
		tempoTotalCondenacaoAnos = "";
		tempoCumpridoUltimoEventoAnos = "";
		tempoTotalCondenacaoRemanescenteAnos = "";
		tempoTotalComutacaoAnos = "";
		tempoTotalComutacaoDias = "";
		tempoRestanteUltimoEventoAnos = "";
		visualizaRestantePenaUltimoEvento = "";
	}

	public String getDataInicioPSC() {
		return dataInicioPSC;
	}

	public void setDataInicioPSC(String dataInicioPSC) {
		this.dataInicioPSC = dataInicioPSC;
	}

	public String getTotalCondenacaoPSCHoras() {
		return totalCondenacaoPSCHoras;
	}

	public void setTotalCondenacaoPSCHoras(String totalCondenacaoPSCHoras) {
		this.totalCondenacaoPSCHoras = totalCondenacaoPSCHoras;
	}

	public String getHorasCumpridasPSC() {
		return horasCumpridasPSC;
	}

	public void setHorasCumpridasPSC(String horasCumpridasPSC) {
		this.horasCumpridasPSC = horasCumpridasPSC;
	}

	public String getHorasRestantePSC() {
		return horasRestantePSC;
	}

	public void setHorasRestantePSC(String horasRestantePSC) {
		this.horasRestantePSC = horasRestantePSC;
	}

	public String getTotalCondenacaoLFSDias() {
		return totalCondenacaoLFSDias;
	}

	public void setTotalCondenacaoLFSDias(String tempoEmDias) {
		this.totalCondenacaoLFSDias = tempoEmDias;
		setTotalCondenacaoLFSAnos(tempoEmDias);
	}

	public String getTotalCondenacaoLFSAnos() {
		return totalCondenacaoLFSAnos;
	}

	public void setTotalCondenacaoLFSAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.totalCondenacaoLFSAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.totalCondenacaoLFSAnos = "";
	}

	public String getTempoCumpridoLFSDias() {
		return tempoCumpridoLFSDias;
	}

	public void setTempoCumpridoLFSDias(String tempoEmDias) {
		this.tempoCumpridoLFSDias = tempoEmDias;
		setTempoCumpridoLFSAnos(tempoEmDias);
	}

	public String getTempoCumpridoLFSAnos() {
		return tempoCumpridoLFSAnos;
	}

	public void setTempoCumpridoLFSAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCumpridoLFSAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCumpridoLFSAnos = "";
	}

	public String getTempoRestanteLFSDias() {
		return tempoRestanteLFSDias;
	}

	public void setTempoRestanteLFSDias(String tempoEmDias) {
		this.tempoRestanteLFSDias = tempoEmDias;
		setTempoRestanteLFSAnos(tempoEmDias);
	}

	public String getTempoRestanteLFSAnos() {
		return tempoRestanteLFSAnos;
	}

	public void setTempoRestanteLFSAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoRestanteLFSAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoRestanteLFSAnos = "";
	}
	
	public String getTempoInterrupcaoLFSDias() {
		return tempoInterrupcaoLFSDias;
	}

	public void setTempoInterrupcaoLFSDias(String tempoEmDias) {
		this.tempoInterrupcaoLFSDias = tempoEmDias;
		setTempoInterrupcaoLFSAnos(tempoEmDias);
	}

	public String getTempoInterrupcaoLFSAnos() {
		return tempoInterrupcaoLFSAnos;
	}

	public void setTempoInterrupcaoLFSAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoInterrupcaoLFSAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoInterrupcaoLFSAnos = "";
	}

	public String getDataTerminoLFS() {
		return dataTerminoLFS;
	}

	public void setDataTerminoLFS(String dataTerminoLFS) {
		this.dataTerminoLFS = dataTerminoLFS;
	}

	public String getTempoTotalCondenacaoAnos() {
		return tempoTotalCondenacaoAnos;
	}

	public void setTempoTotalCondenacaoAnos(String tempoTotalCondenacaoAnos) {
		this.tempoTotalCondenacaoAnos = tempoTotalCondenacaoAnos;
	}
	
	public String getTempoCumpridoUltimoEventoAnos() {
		return this.tempoCumpridoUltimoEventoAnos;
	}

	public void setTempoCumpridoUltimoEventoAnos(String tempo) {
		this.tempoCumpridoUltimoEventoAnos = tempo;
	}

//	public String getObservacao() {
//		return observacao;
//	}
//
//	public void setObservacao(String observacao) {
//		this.observacao = observacao;
//	}
	
	public String getTempoTotalComutacaoDias() {
		return this.tempoTotalComutacaoDias;
	}

	public void setTempoTotalComutacaoDias(String tempoTotalComutacaoDias) {
		this.tempoTotalComutacaoDias = tempoTotalComutacaoDias;
	}

	public String getTempoTotalComutacaoAnos() {
		return this.tempoTotalComutacaoAnos;
	}

	public void setTempoTotalComutacaoAnos(String tempoEmAnos) {
		this.tempoTotalComutacaoAnos = tempoEmAnos;
	}

	public String getTempoTotalCondenacaoRemanescenteAnos() {
		return tempoTotalCondenacaoRemanescenteAnos;
	}

	public void setTempoTotalCondenacaoRemanescenteAnos(
			String tempoTotalCondenacaoRemanescenteAnos) {
		this.tempoTotalCondenacaoRemanescenteAnos = tempoTotalCondenacaoRemanescenteAnos;
	}

	public String getTempoRestanteUltimoEventoAnos() {
		return tempoRestanteUltimoEventoAnos;
	}

	public void setTempoRestanteUltimoEventoAnos(
			String tempoRestanteUltimoEventoAnos) {
		this.tempoRestanteUltimoEventoAnos = tempoRestanteUltimoEventoAnos;
	}

	public String getVisualizaRestantePenaUltimoEvento() {
		return visualizaRestantePenaUltimoEvento;
	}

	public void setVisualizaRestantePenaUltimoEvento(
			String visualizaRestantePenaUltimoEvento) {
		this.visualizaRestantePenaUltimoEvento = visualizaRestantePenaUltimoEvento;
	}

	public String getObservacaoCalculoLFS() {
		return observacaoCalculoLFS;
	}

	public void setObservacaoCalculoLFS(String observacaoLFS) {
		this.observacaoCalculoLFS = observacaoLFS;
	}

	public String getTotalPagamentoPEC() {
		return totalPagamentoPEC;
	}

	public void setTotalPagamentoPEC(String totalPagamentoPEC) {
		this.totalPagamentoPEC = totalPagamentoPEC;
	}

	public String getValorPagoPEC() {
		return valorPagoPEC;
	}

	public void setValorPagoPEC(String valorPagoPEC) {
		this.valorPagoPEC = valorPagoPEC;
	}

	public String getValorDevidoPEC() {
		return valorDevidoPEC;
	}

	public void setValorDevidoPEC(String valorDevidoPEC) {
		this.valorDevidoPEC = valorDevidoPEC;
	}

	public String getTotalCondenacaoITDDias() {
		return totalCondenacaoITDDias;
	}

	public void setTotalCondenacaoITDDias(String tempoEmDias) {
		this.totalCondenacaoITDDias = tempoEmDias;
		setTotalCondenacaoITDAnos(tempoEmDias);
	}

	public String getTotalCondenacaoITDAnos() {
		return totalCondenacaoITDAnos;
	}

	public void setTotalCondenacaoITDAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.totalCondenacaoITDAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.totalCondenacaoITDAnos = "";
	}

	public String getTempoCumpridoITDDias() {
		return tempoCumpridoITDDias;
	}

	public void setTempoCumpridoITDDias(String tempoEmDias) {
		this.tempoCumpridoITDDias = tempoEmDias;
		setTempoCumpridoITDAnos(tempoEmDias);
	}

	public String getTempoCumpridoITDAnos() {
		return tempoCumpridoITDAnos;
	}

	public void setTempoCumpridoITDAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCumpridoITDAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCumpridoITDAnos = "";
	}

	public String getTempoRestanteITDDias() {
		return tempoRestanteITDDias;
	}

	public void setTempoRestanteITDDias(String tempoEmDias) {
		this.tempoRestanteITDDias = tempoEmDias;
		setTempoRestanteITDAnos(tempoEmDias);
	}

	public String getTempoRestanteITDAnos() {
		return tempoRestanteITDAnos;
	}

	public void setTempoRestanteITDAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoRestanteITDAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoRestanteITDAnos = "";
	}

	public String getDataTerminoITD() {
		return dataTerminoITD;
	}

	public void setDataTerminoITD(String dataTerminoITD) {
		this.dataTerminoITD = dataTerminoITD;
	}

	public String getObservacaoCalculoITD() {
		return observacaoCalculoITD;
	}

	public void setObservacaoCalculoITD(String observacaoCalculoITD) {
		this.observacaoCalculoITD = observacaoCalculoITD;
	}
	
	public String getTotalPagamentoPCB() {
	    return totalPagamentoPCB;
	}

	public void setTotalPagamentoPCB(String totalPagamentoPCB) {
	    this.totalPagamentoPCB = totalPagamentoPCB;
	}

	public String getQtdPagaPCB() {
	    return qtdPagaPCB;
	}

	public void setQtdPagaPCB(String qtdPagaPCB) {
	    this.qtdPagaPCB = qtdPagaPCB;
	}

	public String getQtdDevidaPCB() {
	    return qtdDevidaPCB;
	}

	public void setQtdDevidaPCB(String qtdDevidaPCB) {
	    this.qtdDevidaPCB = qtdDevidaPCB;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}
}
