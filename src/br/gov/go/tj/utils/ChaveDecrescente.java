package br.gov.go.tj.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * 
 * Classe:     ChaveDecrescente.java
 * Autor:      M�rcio Mendon�a Gomes 
 * Data:       08/2010
 * Finalidade: Prover ordena��o decrescente.  
 *             
 */
public class ChaveDecrescente implements Serializable,Comparable<ChaveDecrescente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8090879064083388722L;
	
	private String chave = null;
	
	public ChaveDecrescente(String chave){
		this.chave = chave;
	}
	
	public ChaveDecrescente(TJDataHora data){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		this.chave = df.format(data.getDate());
	}
	
	public String getChave(){
		return this.chave;
	}
		
	public int compareTo(ChaveDecrescente objeto){
		//Ordena��o decrescente...
		return (this.chave.compareTo(objeto.getChave()) * -1);
	}

}
