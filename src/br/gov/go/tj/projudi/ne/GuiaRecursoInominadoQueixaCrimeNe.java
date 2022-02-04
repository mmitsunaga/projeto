package br.gov.go.tj.projudi.ne;

import java.util.List;

public class GuiaRecursoInominadoQueixaCrimeNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = 82047839889708942L;
	
	/**
	 * Método para calcular o rateio das guias 50% para parte.
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void calcularRateio50Porcento(List listaGuiaItemDt) throws Exception {
		this.getGuiaCalculoNe().calcularRateio50Porcento(listaGuiaItemDt);
	}

}
