package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaDt;

public class GuiaFazendaMunicipalNe extends GuiaEmissaoNe {

	private static final long serialVersionUID = -9145330239102757968L;
	
	private ServentiaNe serventiaNe;

	/**
	 * Método para consultar ServentiaDt pelo Código da Serventia.
	 * @param String serventiaCodigo
	 * @return ServentiaDt
	 * @throws Exception
	 */
	public ServentiaDt consultarServentiaProcesso(String serventiaCodigo) throws Exception {
		serventiaNe = new ServentiaNe();
		
		return serventiaNe.consultarServentiaCodigo(serventiaCodigo);
	}
	
	public void calcularRateio50Porcento(List listaGuiaItemDt) throws Exception {
		this.getGuiaCalculoNe().calcularRateio50Porcento(listaGuiaItemDt);
	}	
}
