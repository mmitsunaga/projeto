package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.projudi.ps.TemaSituacaoPs;
import br.gov.go.tj.utils.FabricaConexao;



//---------------------------------------------------------
public class TemaSituacaoNe extends TemaSituacaoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = 6960607613006408649L;

	//---------------------------------------------------------
	public  String Verificar(TemaSituacaoDt dados ) {

		String stRetorno="";


		return stRetorno;

	}
	
	/**
	 * Consulta a Situação do Tema pelo seu código ou pelo campo Situacao CNJ
	 * @param codigo
	 * @param descricao
	 * @return
	 * @throws Exception 
	 */
	public TemaSituacaoDt consultarPorCodigoOuSituacaoCNJ(Integer codigo, String situacaoCNJ) throws Exception{
		TemaSituacaoDt temaSituacaoDt = null;		
        FabricaConexao obFabricaConexao = null;         
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            TemaSituacaoPs obPersistencia = new TemaSituacaoPs(obFabricaConexao.getConexao());
            temaSituacaoDt = obPersistencia.consultarPorCodigoOuSituacaoCNJ(codigo, situacaoCNJ);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return temaSituacaoDt;
	}

}
