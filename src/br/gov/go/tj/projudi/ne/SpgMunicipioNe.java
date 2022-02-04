package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.SpgMunicipioDt;
import br.gov.go.tj.projudi.ps.SpgMunicipioPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class SpgMunicipioNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4416233492827457970L;
	
	public SpgMunicipioDt consultarMunicipio(String municipio)throws MensagemException, Exception {
        SpgMunicipioDt stTemp = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
            SpgMunicipioPs obPersistencia = new SpgMunicipioPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarMunicipio(municipio);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
	
}
