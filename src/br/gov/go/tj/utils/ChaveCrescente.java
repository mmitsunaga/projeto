package br.gov.go.tj.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 
 * Classe:     ChaveCrescente.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       06/2010
 * Finalidade: Prover ordenação crescente.  
 *             
 */
public class ChaveCrescente implements Serializable,Comparable<ChaveCrescente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4834750985850057588L;
	
	private String chave = null;
	
	public ChaveCrescente(String chave){
		this.chave = chave;
	}
	
	public ChaveCrescente(TJDataHora data){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		this.chave = df.format(data.getDate());
	}
	
	public String getChave(){
		return this.chave;
	}
		
	public int compareTo(ChaveCrescente objeto){
		//Ordenação crescente...
		return this.chave.compareTo(objeto.getChave());
	}

}
