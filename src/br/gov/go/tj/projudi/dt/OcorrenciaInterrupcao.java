package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     OcorrenciaInterrupcao.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Armazenar as datas de início e fim de uma determinada interrupção
 *             Esta classe implementa a interface Comparable para determinar a ordem decrescente
 *             a ser exibida no relatório 
 */
public class OcorrenciaInterrupcao implements Serializable, Comparable<OcorrenciaInterrupcao> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7152031748059154462L;
	
	private String id_OcorrenciaInterrupcao;
	private TJDataHora periodoInicial;
	private TJDataHora periodoFinal;
	private String motivo;
	
	/**
     * Armazena o ID gerado ao inserir o registro no BD
     *      
     * @param String valor 
     *  
     */
	public void setId(String valor) {
		if(valor!=null) id_OcorrenciaInterrupcao = valor;
	}
	
	/**
     * Retorna o ID gerado ao inserir o registro no BD
     *      
     * @param String valor 
     *  
     */
	public String getId(){
		if(id_OcorrenciaInterrupcao!=null) return id_OcorrenciaInterrupcao;		
		return "";
	}
	
	/**
     * Armazena o período Inicial
     *      
     * @param TJDataHora periodoInicial
     *  
     */
	public void setPeriodoInicial(TJDataHora periodoInicial) {
		this.periodoInicial = periodoInicial;
	}
	
	/**
     * Retorna o período Inicial  	
     */
	public TJDataHora getPeriodoInicial() {
		return periodoInicial;
	}
	
	/**
     * Armazena o período Final
     *      
     * @param TJDataHora periodoInicial
     *  
     */
	public void setPeriodoFinal(TJDataHora periodoFinal) {
		this.periodoFinal = periodoFinal;
	}
	
	/**
     * Retorna o período Final
     *       
     */      
	public TJDataHora getPeriodoFinal() {
		return periodoFinal;
	}	
	
	/**
     * Retorna o tempo de interrupções formatado em dia, hora, minutos e segundos.
     *       
     */
	public String getTempoDeInterrupcao(){	
		return Funcoes.diferencaDatasFormatadaParaRelatorioEmMinutos(periodoFinal.getDate(), periodoInicial.getDate());
	}
	
	/**
     * Utilizado por ordenar em ordem inversa os objetos.
     * @param OcorrenciaInterrupcao o
     */
	public int compareTo(OcorrenciaInterrupcao o) {
		// Ordenação decrescente...
		return (periodoInicial.getCalendar().compareTo(o.getPeriodoInicial().getCalendar()) * -1);
	}
	
	/**
     * Armazena o motivo 
     *      
     * @param String valor 
     *  
     */
	public void setMotivo(String valor) {
		if(valor!=null) motivo = valor;
	}
	
	/**
     * Retorna o motivo
     *      
     * @param String valor 
     *  
     */
	public String getMotivo(){
		if(motivo!=null) return motivo;		
		return "";
	}
}
