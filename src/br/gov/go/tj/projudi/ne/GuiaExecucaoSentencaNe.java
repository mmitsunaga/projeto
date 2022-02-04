package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;

public class GuiaExecucaoSentencaNe extends GuiaEmissaoNe {
	
	private static final long serialVersionUID = 4720335830483340368L;
	
	public static final int ADICIONAR_ITEM = 1;
	public static final int REMOVER_ITEM = 0;
	
	private ServentiaNe serventiaNe;
	private CustaNe custaNe;
	
	/**
	 * M�todo para consultar ServentiaDt pelo C�digo da Serventia.
	 * @param String serventiaCodigo
	 * @return ServentiaDt
	 * @throws Exception
	 */
	public ServentiaDt consultarServentiaProcesso(String serventiaCodigo) throws Exception {
		serventiaNe = new ServentiaNe();
		
		return serventiaNe.consultarServentiaCodigo(serventiaCodigo);
	}
	
	/**
	 * M�todo para consultar custas.
	 * @param String tempNomeBusca
	 * @param String PosicaoPaginaAtual
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricaoCusta(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		if( custaNe == null )
			custaNe = new CustaNe();
		
		return custaNe.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
	}
	
	/**
	 * M�todo que retorna a quantidade de p�ginas de custas.
	 * @return long
	 */
	public long getQuantidadePaginasCusta() {
		return custaNe.getQuantidadePaginas();
	}
	
	/**
	 * M�todo para consultar no CustaNe o CustaDt pelo Id.
	 * @param String id_custa
	 * @return CustaDt
	 * @throws Exception
	 */
	public CustaDt consultarCustaDtPorId(String id_custa) throws Exception {
		if( custaNe == null )
			custaNe = new CustaNe();
		
		return custaNe.consultarId(id_custa);
	}
	
	/**
	 * M�todo para calcular o rateio das guias 50% para parte.
	 * @param List listaGuiaItemDt
	 * @throws Exception
	 */
	public void calcularRateio50Porcento(List listaGuiaItemDt) throws Exception {
		this.getGuiaCalculoNe().calcularRateio50Porcento(listaGuiaItemDt);
	}
}
