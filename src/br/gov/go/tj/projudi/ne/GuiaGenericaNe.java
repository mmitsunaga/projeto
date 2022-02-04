package br.gov.go.tj.projudi.ne;

import java.util.List;

public class GuiaGenericaNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -7438360728060861165L;

	public static int QUANTIDADE_ITENS_CUSTA_GENERICO = 15;
	
	/**
	 * Método para calcular o rateio das guias 50% para parte.
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void calcularRateio50Porcento(List listaGuiaItemDt) throws Exception {
		this.getGuiaCalculoNe().calcularRateio50Porcento(listaGuiaItemDt);
	}
	
	/**
	 * Método para consultar itens de CustaDt
	 * @param List listaCustaDt
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuiaCustaDt(List listaCustaDt) throws Exception {
		List retorno = null;
		
		CustaNe custaNe = new CustaNe();
		retorno = custaNe.consultarDescricao(listaCustaDt);
		
		return retorno;
	}
}
