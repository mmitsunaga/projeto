package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

/**
 * 
 * Classe:     RelatorioInterrupcaoDetalhes.java
 * Autor:      M�rcio Mendon�a Gomes 
 * Data:       07/2010
 * Finalidade: Encapsular cada uma das interrup��es para ser utilizado
 *             pelo pelo Jasper na emiss�o do relat�rio em PDF. 
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
     * Armazena a data e hora inicial da interrup��o
     * 
     * @param String dataHoraInicialInterrupcao formatada
     *  
     */
	public void setDataHoraInicialInterrupcao(String dataHoraInicialInterrupcao) {
		this.dataHoraInicialInterrupcao = dataHoraInicialInterrupcao;
	}
	
	/**
     * Retorna a data e hora inicial da interrup��o formatada
     *   
     */
	public String getDataHoraInicialInterrupcao() {
		return dataHoraInicialInterrupcao;
	}
	
	/**
     * Armazena a data e hora final da interrup��o formatada
     * 
     * @param String dataHoraFinalInterrupcao
     *  
     */
	public void setDataHoraFinalInterrupcao(String dataHoraFinalInterrupcao) {
		this.dataHoraFinalInterrupcao = dataHoraFinalInterrupcao;
	}
	
	/**
     * Retorna a data e hora final da interrup��o formatada
     *   
     */
	public String getDataHoraFinalInterrupcao() {
		return dataHoraFinalInterrupcao;
	}
	
	/**
     * Armazena o tempo da interrup��o formatado
     * 
     * @param String tempoDeInterrupcao
     *  
     */
	public void setTempoDeInterrupcao(String tempoDeInterrupcao) {
		this.tempoDeInterrupcao = tempoDeInterrupcao;
	}
	
	/**
     * Retorna o tempo da interrup��o formatado
     *  
     */
	public String getTempoDeInterrupcao() {
		return tempoDeInterrupcao;
	}
	
	/**
     * Armazena o n�mero de interrup��es
     * 
     * @param String numeroDaInterrupcao
     *  
     */
	public void setNumeroDaInterrupcao(String numeroDaInterrupcao) {
		this.numeroDaInterrupcao = numeroDaInterrupcao;
	}
	
	/**
     * Retorna o n�mero de interrup��es
     *  
     */
	public String getNumeroDaInterrupcao() {
		return numeroDaInterrupcao;
	}
	
	/**
     * Armazena indicador se o detalhe � totalizador,
     * 
     * @param boolean ehTotalizador
     *  
     */
	public void setEhTotalizador(boolean ehTotalizador) {
		this.ehTotalizador = ehTotalizador;
	}
	
	/**
     * Retorna indicando se o detalhe � totalizados,
     * caso seja a linha no relat�rio em PDF ser�
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
