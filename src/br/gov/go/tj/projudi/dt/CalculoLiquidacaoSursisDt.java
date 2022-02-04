package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;


/**
 * Este objeto será utilizado no cálculo de liquidação de penas.
 * @author wcsilva
 */
public class CalculoLiquidacaoSursisDt extends Dados {
	
	private static final long serialVersionUID = 3872719795795134494L;
	
	private String dataInicio;
	private String dataProvavelTermino;
	private String tempoSursisDias;
	private String tempoSursisAnos;
	
	public CalculoLiquidacaoSursisDt(){
		this.limpar();
	}
	
	public void limpar(){
		dataInicio = "";
		dataProvavelTermino = "";
		tempoSursisAnos = "";
		tempoSursisDias = "";
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataProvavelTermino() {
		return dataProvavelTermino;
	}

	public void setDataProvavelTermino(String dataProvavelTermino) {
		this.dataProvavelTermino = dataProvavelTermino;
	}

	public String getTempoSursisDias() {
		return tempoSursisDias;
	}

	public void setTempoSursisDias(String tempoSursisDias) {
		this.tempoSursisDias = tempoSursisDias;
		setTempoSursisAnos(tempoSursisDias);
	}

	public String getTempoSursisAnos() {
		return tempoSursisAnos;
	}

	public void setTempoSursisAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoSursisAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoSursisAnos = "";
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}
}
