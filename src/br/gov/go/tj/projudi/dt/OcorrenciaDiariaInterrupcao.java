package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     OcorrenciaDiariaInterrupcao.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Agrupar as interrupções do sistema por dia
 */
public class OcorrenciaDiariaInterrupcao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2505427916423486680L;
	
	private TJDataHora dataDeReferencia = null;
	
	private List<OcorrenciaInterrupcao> listaDeInterrupcoes = null;
	
	private long tempoTotalDeInterrupcoes = 0;
	
	
	public OcorrenciaDiariaInterrupcao(){
		this.listaDeInterrupcoes = new ArrayList<OcorrenciaInterrupcao>();
	}
	
	/**
     * Adiciona a interrução na data correspondente e acrescenta o tempo desta interrução
     * no totalizador de interrupçoes do dia (em segundos)
     *      
     * @param OcorrenciaInterrupcao interrupcao
     * @param long tempoEmSegundos
     *  
     */
	public void adicionePeriodo(OcorrenciaInterrupcao interrupcao,
			                    long tempoEmSegundos){
		
		if (interrupcao == null) return;
		
		if (this.dataDeReferencia == null) this.dataDeReferencia = interrupcao.getPeriodoInicial();
		
		this.listaDeInterrupcoes.add(interrupcao);
		
		this.tempoTotalDeInterrupcoes += tempoEmSegundos;
	}
	
	/**
	 * 
     * Retorna a lista de interrupções deste dia 
     *  
     */
	public List<OcorrenciaInterrupcao> getListaDeInterrupcoes(){
		Collections.sort(this.listaDeInterrupcoes);
		return this.listaDeInterrupcoes;
	}
	
	/**
	 * 
     * Retorna um iterator da lista de interrupções deste dia 
     *  
     */
	public Iterator<OcorrenciaInterrupcao> getIteratorDeInterrupcoes(){
		Collections.sort(this.listaDeInterrupcoes);
		return getListaDeInterrupcoes().iterator();
	}
	
	/**
	 * 
     * Retorna o tempo total de interrupções deste dia formatado em dia, hora, minutos e segundos. 
     *  
     */
	public String getTempoTotalDeInterrupcoes(){
		return Funcoes.tempoFormatadoParaRelatorioEmMinutos(tempoTotalDeInterrupcoes);
	}
	
	/**
	 * 
     * Retorna a data desta lista de interrupções. 
     *  
     */
	public TJDataHora getDataDeReferencia(){
		return this.dataDeReferencia;
	}

}
