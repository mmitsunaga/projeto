package br.gov.go.tj.projudi.util;

import br.gov.go.tj.projudi.ne.GuiaNumeroNe;

/**
 *  Classe que controla numera��o de processos.
 *  Iternamente h� um algoritmo para gera��o dos n�meros dos processos, 
 *  incluindo a presen�a de um d�gito verificador
 * 
 * 	Singleton design pattern � utilizado em raz�o de a entidade oferecer o
 * 	n�mero de um novo processo
 * 
 */
public class GuiaNumero {

	private static GuiaNumero guianumero = null;	

	/**
	 * M�todo de captura do singleton
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
