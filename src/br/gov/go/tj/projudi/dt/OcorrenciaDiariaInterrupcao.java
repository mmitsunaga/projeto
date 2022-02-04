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
 * Autor:      M�rcio Mendon�a Gomes 
 * Data:       07/2010
 * Finalidade: Agrupar as interrup��es do sistema por dia
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
     * Adiciona a interru��o na data correspondente e acrescenta o tempo desta interru��o
     * no totalizador de interrup�oes do dia (em segundos)
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
     * Retorna a lista de interrup��es deste dia 
     *  
     */
	public List<OcorrenciaInterrupcao> getListaDeInterrupcoes(){
		Collections.sort(this.listaDeInterrupcoes);
		return this.listaDeInterrupcoes;
	}
	
	/**
	 * 
     * Retorna um iterator da lista de interrup��es deste dia 
     *  
     */
	public Iterator<OcorrenciaInterrupcao> getIteratorDeInterrupcoes(){
		Collections.sort(this.listaDeInterrupcoes);
		return getListaDeInterrupcoes().iterator();
	}
	
	/**
	 * 
     * Retorna o tempo total de interrup��es deste dia formatado em dia, hora, minutos e segundos. 
     *  
     */
	public String getTempoTotalDeInterrupcoes(){
		return Funcoes.tempoFormatadoParaRelatorioEmMinutos(tempoTotalDeInterrupcoes);
	}
	
	/**
	 * 
     * Retorna a data desta lista de interrup��es. 
     *  
     */
	public TJDataHora getDataDeReferencia(){
		return this.dataDeReferencia;
	}

}
