package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;


/**
 * Este objeto será utilizado no cálculo de liquidação de penas.
 * @author wcsilva
 */
public class CalculoLiquidacaoIndultoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3872719795795134494L;
	
	//variáveis utilizadas no cálculo de indulto
	private String descRelIndulto;
//	private String fracaoIndulto;
	private String fracaoIndultoComum;
	private String fracaoIndultoHediondo;
	private String dataRequisitoIndulto;
	private String tempoCondenacaoComumDias;
	private String tempoCondenacaoComumAnos;
	private String tempoCondenacaoHediondoDias;
	private String tempoCondenacaoHediondoAnos;
	private String tempoACumprirComumDias;
	private String tempoACumprirComumAnos;
	private String tempoACumprirHediondoDias;
	private String tempoACumprirHediondoAnos;
	
	public CalculoLiquidacaoIndultoDt(){
		this.limpar();
	}
	
	public void limpar(){
		descRelIndulto = "";
//		fracaoIndulto = "";
		fracaoIndultoComum = "";
		fracaoIndultoHediondo = "";
		dataRequisitoIndulto = "";
		tempoCondenacaoComumDias = "";
		tempoCondenacaoComumAnos = "";
		tempoCondenacaoHediondoDias = "";
		tempoCondenacaoHediondoAnos = "";
		tempoACumprirComumDias = "";
		tempoACumprirComumAnos = "";
		tempoACumprirHediondoDias = "";
		tempoACumprirHediondoAnos = "";
	}


	public String getTempoACumprirComumDias() {
		return tempoACumprirComumDias;
	}

	public void setTempoACumprirComumDias(String tempoEmDias) {
		this.tempoACumprirComumDias = tempoEmDias;
		setTempoACumprirComumAnos(tempoEmDias);
	}

	public String getTempoACumprirComumAnos() {
		return tempoACumprirComumAnos;
	}

	public void setTempoACumprirComumAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoACumprirComumAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoACumprirComumAnos = "";
	}

	public String getTempoACumprirHediondoDias() {
		return tempoACumprirHediondoDias;
	}

	public void setTempoACumprirHediondoDias(String tempoEmDias) {
		this.tempoACumprirHediondoDias = tempoEmDias;
		setTempoACumprirHediondoAnos(tempoEmDias);
	}

	public String getTempoACumprirHediondoAnos() {
		return tempoACumprirHediondoAnos;
	}

	public void setTempoACumprirHediondoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoACumprirHediondoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoACumprirHediondoAnos = "";
	}

	public String getTempoCondenacaoComumDias() {
		return tempoCondenacaoComumDias;
	}

	public void setTempoCondenacaoComumDias(String tempoEmDias) {
		this.tempoCondenacaoComumDias = tempoEmDias;
		setTempoCondenacaoComumAnos(tempoEmDias);
	}

	public String getTempoCondenacaoComumAnos() {
		return tempoCondenacaoComumAnos;
	}

	public void setTempoCondenacaoComumAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoComumAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoComumAnos = "";
	}

	public String getTempoCondenacaoHediondoDias() {
		return tempoCondenacaoHediondoDias;
	}

	public void setTempoCondenacaoHediondoDias(String tempoEmDias) {
		this.tempoCondenacaoHediondoDias = tempoEmDias;
		setTempoCondenacaoHediondoAnos(tempoEmDias);
	}

	public String getTempoCondenacaoHediondoAnos() {
		return tempoCondenacaoHediondoAnos;
	}

	public void setTempoCondenacaoHediondoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoHediondoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoHediondoAnos = "";
	}

	public String getDescRelIndulto() {
		return descRelIndulto;
	}

	public void setDescRelIndulto(String descRelIndulto) {
		this.descRelIndulto = descRelIndulto;
	}

//	public String getFracaoIndulto() {
//		return fracaoIndulto;
//	}
//
//	public void setFracaoIndulto(String fracaoIndulto) {
//		this.fracaoIndulto = fracaoIndulto;
//	}

	public String getDataRequisitoIndulto() {
		return dataRequisitoIndulto;
	}

	public void setDataRequisitoIndulto(String dataRequisitoIndulto) {
		this.dataRequisitoIndulto = dataRequisitoIndulto;
	}

	public String getFracaoIndultoComum() {
		return fracaoIndultoComum;
	}

	public void setFracaoIndultoComum(String fracaoIndultoComum) {
		this.fracaoIndultoComum = fracaoIndultoComum;
	}

	public String getFracaoIndultoHediondo() {
		return fracaoIndultoHediondo;
	}

	public void setFracaoIndultoHediondo(String fracaoIndultoHediondo) {
		this.fracaoIndultoHediondo = fracaoIndultoHediondo;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}
}

