package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

/**
 * 
 * Classe:     RelatorioInterrupcaoDetalhes.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Encapsular cada uma das interrupções para ser utilizado
 *             pelo pelo Jasper na emissão do relatório em PDF. 
 *             
 */
public class RelatorioInterrupcaoDetalhes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5652268157722794007L;
	
	private String dataHoraInicialInterrupcao;
	private String dataHoraFinalInterrupcao;
	private String tempoDeInterrupcao;
	private String numeroDaInterrupcao;	
	private boolean ehTotalizador;
	private String motivo;
	
	/**
     * Armazena a data e hora inicial da interrupção
     * 
     * @param String dataHoraInicialInterrupcao formatada
     *  
     */
	public void setDataHoraInicialInterrupcao(String dataHoraInicialInterrupcao) {
		this.dataHoraInicialInterrupcao = dataHoraInicialInterrupcao;
	}
	
	/**
     * Retorna a data e hora inicial da interrupção formatada
     *   
     */
	public String getDataHoraInicialInterrupcao() {
		return dataHoraInicialInterrupcao;
	}
	
	/**
     * Armazena a data e hora final da interrupção formatada
     * 
     * @param String dataHoraFinalInterrupcao
     *  
     */
	public void setDataHoraFinalInterrupcao(String dataHoraFinalInterrupcao) {
		this.dataHoraFinalInterrupcao = dataHoraFinalInterrupcao;
	}
	
	/**
     * Retorna a data e hora final da interrupção formatada
     *   
     */
	public String getDataHoraFinalInterrupcao() {
		return dataHoraFinalInterrupcao;
	}
	
	/**
     * Armazena o tempo da interrupção formatado
     * 
     * @param String tempoDeInterrupcao
     *  
     */
	public void setTempoDeInterrupcao(String tempoDeInterrupcao) {
		this.tempoDeInterrupcao = tempoDeInterrupcao;
	}
	
	/**
     * Retorna o tempo da interrupção formatado
     *  
     */
	public String getTempoDeInterrupcao() {
		return tempoDeInterrupcao;
	}
	
	/**
     * Armazena o número de interrupções
     * 
     * @param String numeroDaInterrupcao
     *  
     */
	public void setNumeroDaInterrupcao(String numeroDaInterrupcao) {
		this.numeroDaInterrupcao = numeroDaInterrupcao;
	}
	
	/**
     * Retorna o número de interrupções
     *  
     */
	public String getNumeroDaInterrupcao() {
		return numeroDaInterrupcao;
	}
	
	/**
     * Armazena indicador se o detalhe é totalizador,
     * 
     * @param boolean ehTotalizador
     *  
     */
	public void setEhTotalizador(boolean ehTotalizador) {
		this.ehTotalizador = ehTotalizador;
	}
	
	/**
     * Retorna indicando se o detalhe é totalizados,
     * caso seja a linha no relatório em PDF será
     * destacada em cinza.
     *  
     */
	public boolean getEhTotalizador() {
		return this.ehTotalizador;
	}

	public String getMotivo() {
		if (motivo == null) return "";
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}	

}
