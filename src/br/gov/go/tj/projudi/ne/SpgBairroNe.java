package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.SpgBairroDt;
import br.gov.go.tj.projudi.ps.SpgBairroPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class SpgBairroNe extends Negocio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -265033740090750699L;
	
	public SpgBairroDt consultaBairro(String bairro)throws MensagemException, Exception {
		SpgBairroDt obTemp = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
            SpgBairroPs obPersistencia = new SpgBairroPs(obFabricaConexao.getConexao());
            obTemp = obPersistencia.consultarBairro(bairro);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return obTemp;
    }
	
}
