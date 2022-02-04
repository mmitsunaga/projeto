package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

/**
 * Este objeto será utilizado no cálculo de prescrição de pena.
 * @author wcsilva
 */
public class CalculoLiquidacaoPrescricaoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1905227670330636214L;
	
	//variáveis utilizadas no cálculo da prescrição
	private String tempoCondenacaoDias;
	private String tempoCondenacaoAnos;
	private String tempoCondenacaoRemanescenteDias;
	private String tempoCondenacaoRemanescenteAnos;
	private String tempoPrescricaoDias;
	private String tempoPrescricaoAnos;
	private String dataPrescricao;
	private String restantePenaDias;
	private String restantePenaAnos;
	private String dataInicioLapso;
	private String dataFimLapso;
	private String tempoLapsoDias;
	private String tempoLapsoAnos;
	private boolean boHouvePrescricao;
	private String penaPrescrita;
	private String descricaoLapso;
	
	public CalculoLiquidacaoPrescricaoDt(){
		this.limpar();
	}
	
	public void limpar(){
		tempoCondenacaoDias = "";
		tempoCondenacaoAnos = "";
		tempoCondenacaoRemanescenteDias = "";
		tempoCondenacaoRemanescenteAnos = "";
		tempoPrescricaoDias = "";
		tempoPrescricaoAnos = "";
		dataPrescricao = "";
		restantePenaDias = "";
		restantePenaAnos = "";
		dataInicioLapso = "";
		dataFimLapso = "";
		tempoLapsoDias = "";
		tempoLapsoAnos = "";
		penaPrescrita = "";
		boHouvePrescricao = false;
		descricaoLapso = "";
	}

	public String getTempoCondenacaoDias() {
		return tempoCondenacaoDias;
	}

	public void setTempoCondenacaoDias(String tempoEmDias) {
		this.tempoCondenacaoDias = tempoEmDias;
		setTempoCondenacaoAnos(tempoEmDias);
	}

	public String getTempoCondenacaoAnos() {
		return tempoCondenacaoAnos;
	}

	public void setTempoCondenacaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoAnos = "";
	}

	public String getTempoCondenacaoRemanescenteDias() {
		return tempoCondenacaoRemanescenteDias;
	}

	public void setTempoCondenacaoRemanescenteDias(String tempoEmDias) {
		this.tempoCondenacaoRemanescenteDias = tempoEmDias;
		setTempoCondenacaoRemanescenteAnos(tempoEmDias);
	}

	public String getTempoCondenacaoRemanescenteAnos() {
		return tempoCondenacaoRemanescenteAnos;
	}

	public void setTempoCondenacaoRemanescenteAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoRemanescenteAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoRemanescenteAnos = "";
	}

	public String getTempoPrescricaoDias() {
		return tempoPrescricaoDias;
	}

	public void setTempoPrescricaoDias(String tempoEmDias) {
		this.tempoPrescricaoDias = tempoEmDias;
		setTempoPrescricaoAnos(tempoEmDias);
	}

	public String getTempoPrescricaoAnos() {
		return tempoPrescricaoAnos;
	}

	public void setTempoPrescricaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0){
			this.tempoPrescricaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
//			this.tempoPrescricaoAnos = this.tempoPrescricaoAnos.substring(0, 2);
		}
		else this.tempoPrescricaoAnos = tempoEmDias;
	}

	public String getDataPrescricao() {
		return dataPrescricao;
	}

	public void setDataPrescricao(String dataPrescricao) {
		this.dataPrescricao = dataPrescricao;
	}

	public String getRestantePenaDias() {
		return restantePenaDias;
	}

	public void setRestantePenaDias(String tempoEmDias) {
		this.restantePenaDias = tempoEmDias;
		setRestantePenaAnos(tempoEmDias);
	}

	public String getRestantePenaAnos() {
		return restantePenaAnos;
	}

	public void setRestantePenaAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.restantePenaAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.restantePenaAnos = "";
	}

	public String getDataInicioLapso() {
		return dataInicioLapso;
	}

	public void setDataInicioLapso(String dataInicioLapso) {
		this.dataInicioLapso = dataInicioLapso;
	}

	public String getDataFimLapso() {
		return dataFimLapso;
	}

	public void setDataFimLapso(String dataFimLapso) {
		this.dataFimLapso = dataFimLapso;
	}

	public String getTempoLapsoDias() {
		return tempoLapsoDias;
	}

	public void setTempoLapsoDias(String tempoEmDias) {
		this.tempoLapsoDias = tempoEmDias;
		setTempoLapsoAnos(tempoEmDias);
	}

	public String getTempoLapsoAnos() {
		return tempoLapsoAnos;
	}

	public void setTempoLapsoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoLapsoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoLapsoAnos = "";
	}

	public boolean isHouvePrescricao() {
		return boHouvePrescricao;
	}

	public void setBoHouvePrescricao(boolean boPenaPrescrita, String textoPenaPrescrita) {
		this.boHouvePrescricao = boPenaPrescrita;
		setPenaPrescrita(textoPenaPrescrita);
	}

	public String getPenaPrescrita() {
		return penaPrescrita;
	}

	public void setPenaPrescrita(String penaPrescrita) {
		this.penaPrescrita = penaPrescrita;
	}
	
	public String getDescricaoLapso() {
		return descricaoLapso;
	}

	public void setDescricaoLapso(String descricaoLapso) {
		this.descricaoLapso = descricaoLapso;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}

}
