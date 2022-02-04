package br.gov.go.tj.projudi.ne;

import java.util.List;

public class GuiaRecursoInominadoNe extends GuiaEmissaoNe {
	
	private static final long serialVersionUID = -2219587735528188707L;
	
	//*** Variáveis utilizadas para controle da locomoção somente da guia de recurso inominado
	public static final String VARIAVEL_GUIA_RECURSO_INOMINADO 	= "guiaRecursoInominado";
	public static final String VALOR_GUIA_RECURSO_INOMINADO 	= "guiaRecursoInominado";
	
	/**
	 * Método para calcular o rateio das guias 50% para parte.
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void calcularRateio50Porcento(List listaGuiaItemDt) throws Exception {
		this.getGuiaCalculoNe().calcularRateio50Porcento(listaGuiaItemDt);
	}
	
}
