package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.ne.CalculadoraTemposDatasNe;
import br.gov.go.tj.utils.Funcoes;

/**
 * Este objeto será utilizado no cálculo de liquidação de penas. Armazena as variáveis necessárias para o cálculo de comutação de pena.
 * 
 * @author wcsilva
 */
public class CalculoLiquidacaoComutacaoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2840094340854573171L;
	
	// variáveis utilizadas no cálculo de comutação
	private String dataBaseComutacao;
	private String tempoACumprirDias;
	private String tempoACumprirAnos;
	private String dataRequisitoTemporalComutacao;
	private String fracaoTempoCumprido;
	private String descricao;
	private String dataDecreto;
	private String houveComutacao;
	private String idParametroComutacao;
	private String tempoInterrupcaoAteDecretoAnos;
	private String tempoCumpridoComumAteDecretoAnos;
	private String tempoCumpridoHediondoAteDecretoAnos;
	
	private String tempoTotalCondenacaoAnos; // utilizada na impressão do relatório em pdf
	private String dataInicioCumprimentoCondenacao;
	
	private String descricaoFracaoComum;
	private String descricaoFracaoHediondo;
	private String tempoCumpridoTotalAteDecretoAnos;
	private String restantePenaTotalAteDecretoAnos;
	
	public CalculoLiquidacaoComutacaoDt() {
		this.limpar();
	}
	
	public void limpar() {
		dataBaseComutacao = "";
		tempoACumprirDias = "";
		tempoACumprirAnos = "";
		dataRequisitoTemporalComutacao = "";
		fracaoTempoCumprido = "";
		tempoTotalCondenacaoAnos = "";
		descricao = "";
		dataDecreto = "";
		houveComutacao = "";
		idParametroComutacao = "";
		tempoInterrupcaoAteDecretoAnos = "";
		dataInicioCumprimentoCondenacao = "";
		tempoCumpridoComumAteDecretoAnos = "";
		tempoCumpridoHediondoAteDecretoAnos = "";
		
		descricaoFracaoComum = "";
		descricaoFracaoHediondo = "";
		tempoCumpridoTotalAteDecretoAnos = "";
		restantePenaTotalAteDecretoAnos = "";
	}
	
	public String getDataBaseComutacao() {
		return this.dataBaseComutacao;
	}
	
	public void setDataBaseComutacao(String dataBaseComutacao) {
		this.dataBaseComutacao = dataBaseComutacao;
	}
	
	public String getTempoACumprirDias() {
		return this.tempoACumprirDias;
	}
	
	public void setTempoACumprirDias(String tempoACumprirDias) {
		this.tempoACumprirDias = tempoACumprirDias;
		setTempoACumprirAnos(tempoACumprirDias);
	}
	
	public String getTempoACumprirAnos() {
		return this.tempoACumprirAnos;
	}
	
	public void setTempoACumprirAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0)
			this.tempoACumprirAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else
			this.tempoACumprirAnos = "";
	}
	
	public String getDataRequisitoTemporalComutacao() {
		return this.dataRequisitoTemporalComutacao;
	}
	
	public void setDataRequisitoTemporalComutacao(String dataRequisitoTemporalComutacao) {
		this.dataRequisitoTemporalComutacao = dataRequisitoTemporalComutacao;
	}
	
	public String getFracaoTempoCumprido() {
		return this.fracaoTempoCumprido;
	}
	
	public void setFracaoTempoCumprido(String fracaoTempoCumprido) {
		this.fracaoTempoCumprido = fracaoTempoCumprido;
	}
	
	public String getTempoTotalCondenacaoAnos() {
		return this.tempoTotalCondenacaoAnos;
	}
	
	public void setTempoTotalCondenacaoAnos(String tempoEmAnos) {
		this.tempoTotalCondenacaoAnos = tempoEmAnos;
	}
	
	public String getTempoNegativoFormatado(String tempoEmDias) {
		String tempoFormatado = "";
		if (tempoEmDias.length() > 0) {
			if (Funcoes.StringToInt(tempoEmDias) < 0) {
				tempoFormatado = "(" + Funcoes.converterParaAnoMesDia(Math.abs(Funcoes.StringToInt(tempoEmDias))) + ")";
			}
			else
				tempoFormatado = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		}
		return tempoFormatado;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDataDecreto() {
		return dataDecreto;
	}
	
	public void setDataDecreto(String dataDecreto) {
		this.dataDecreto = dataDecreto;
	}
	
	public String getHouveComutacao() {
		return houveComutacao;
	}
	
	public void setHouveComutacao(String houveComutacao) {
		this.houveComutacao = houveComutacao;
	}
	
	public String getIdParametroComutacao() {
		return idParametroComutacao;
	}
	
	public void setIdParametroComutacao(String idParametroComutacao) {
		this.idParametroComutacao = idParametroComutacao;
	}
	
	public String getTempoInterrupcaoAteDecretoAnos() {
		return tempoInterrupcaoAteDecretoAnos;
	}
	
	public void setTempoInterrupcaoAteDecretoAnos(String tempoInterrupcaoAteDecretoAnos) {
		this.tempoInterrupcaoAteDecretoAnos = tempoInterrupcaoAteDecretoAnos;
	}
	
	public String getDataInicioCumprimentoCondenacao() {
		return dataInicioCumprimentoCondenacao;
	}
	
	public void setDataInicioCumprimentoCondenacao(String dataInicioCumprimentoCondenacao) {
		this.dataInicioCumprimentoCondenacao = dataInicioCumprimentoCondenacao;
	}
	
	public String getTempoCumpridoComumAteDecretoAnos() {
		return tempoCumpridoComumAteDecretoAnos;
	}
	
	public void setTempoCumpridoComumAteDecretoAnos(String qtdeDias) {
		if (qtdeDias.length() > 0)
			tempoCumpridoComumAteDecretoAnos = Funcoes.converterParaAnoMesDia(Integer.parseInt(qtdeDias));
		else
			this.tempoCumpridoComumAteDecretoAnos = Funcoes.converterParaAnoMesDia(0);
	}
	
	public String getTempoCumpridoHediondoAteDecretoAnos() {
		return tempoCumpridoHediondoAteDecretoAnos;
	}
	
	public void setTempoCumpridoHediondoAteDecretoAnos(String qtdeDias) {
		if (qtdeDias.length() > 0)
			tempoCumpridoHediondoAteDecretoAnos = Funcoes.converterParaAnoMesDia(Integer.parseInt(qtdeDias));
		else
			this.tempoCumpridoHediondoAteDecretoAnos = Funcoes.converterParaAnoMesDia(0);
	}
	
	public String getDescricaoFracaoComum() {
		return descricaoFracaoComum;
	}
	
	public void setDescricaoFracaoComum(String descricaoFracaoComum) {
		this.descricaoFracaoComum = descricaoFracaoComum;
	}
	
	public String getDescricaoFracaoHediondo() {
		return descricaoFracaoHediondo;
	}
	
	public void setDescricaoFracaoHediondo(String descricaoFracaoHediondo) {
		this.descricaoFracaoHediondo = descricaoFracaoHediondo;
	}
	
	public String getTempoCumpridoTotalAteDecretoAnos() {
		return tempoCumpridoTotalAteDecretoAnos;
	}
	
	public void setTempoCumpridoTotalAteDecretoAnos(String tempoCumpridoTotalAteDecretoAnos) {
		this.tempoCumpridoTotalAteDecretoAnos = tempoCumpridoTotalAteDecretoAnos;
	}
	
	public String getRestantePenaTotalAteDecretoAnos() {
		return restantePenaTotalAteDecretoAnos;
	}
	
	public void setRestantePenaTotalAteDecretoAnos(String restantePenaTotalAteDecretoAnos) {
		this.restantePenaTotalAteDecretoAnos = restantePenaTotalAteDecretoAnos;
	}
	
	@Override
	public String getId() {
		
		return null;
	}
	
	@Override
	public void setId(String id) {
		
		
	}
	
	public String getDuracaoRequisitoTemporalDoHediondo() {
		return descricaoFracaoHediondo.substring(descricaoFracaoHediondo.indexOf("=") + 2).trim();
	}
	
	public String getRequisitoTemporalDoHediondo() throws Exception{
		CalculadoraTemposDatasNe calculadora = new CalculadoraTemposDatasNe();
		int dias = calculadora.converterParaDias(getDuracaoRequisitoTemporalDoHediondo());
		dias -= calculadora.converterParaDias(tempoCumpridoTotalAteDecretoAnos);
		dias -= 1;
		return Funcoes.somaData(dataDecreto, dias);
	}
}
