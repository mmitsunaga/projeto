package br.gov.go.tj.projudi.util;

import br.gov.go.tj.projudi.ne.GuiaNumeroNe;

/**
 *  Classe que controla numeração de processos.
 *  Iternamente há um algoritmo para geração dos números dos processos, 
 *  incluindo a presença de um dígito verificador
 * 
 * 	Singleton design pattern é utilizado em razão de a entidade oferecer o
 * 	número de um novo processo
 * 
 */
public class GuiaNumero {

	private static GuiaNumero guianumero = null;	

	/**
	 * Método de captura do singleton
	 * 
	 * @return O singleton
	 */
	public static GuiaNumero getInstance(){
		if (guianumero == null) guianumero = new GuiaNumero();
		return GuiaNumero.guianumero;
	}

	private GuiaNumero(){
		
	}

	public String getGuiaNumero() throws Exception {

		GuiaNumeroNe guiaNumeroNe = new GuiaNumeroNe();
	    
		String NumeroGuia = guiaNumeroNe.gerarNumero();
		
		return NumeroGuia;
	}




}
