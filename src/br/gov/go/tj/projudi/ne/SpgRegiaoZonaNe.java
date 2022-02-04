package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.SpgRegiaoZonaDt;
import br.gov.go.tj.projudi.ps.SpgRegiaoZonaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;

public class SpgRegiaoZonaNe extends Negocio {

	private static final long serialVersionUID = -265033740090750699L;
	
	public SpgRegiaoZonaDt consultaRegiaoZonaCivel(String id_MuncipioId_Bairro)throws MensagemException, Exception {
		SpgRegiaoZonaDt obTemp = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
            SpgRegiaoZonaPs obPersistencia = new SpgRegiaoZonaPs(obFabricaConexao.getConexao());
            obTemp = obPersistencia.consultaRegiaoZonaCivel(id_MuncipioId_Bairro);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return obTemp;
    }
	
	public SpgRegiaoZonaDt consultaRegiaoZonaCriminal(String id_MuncipioId_Bairro)throws MensagemException, Exception {
		SpgRegiaoZonaDt obTemp = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
            SpgRegiaoZonaPs obPersistencia = new SpgRegiaoZonaPs(obFabricaConexao.getConexao());
            obTemp = obPersistencia.consultaRegiaoZonaCriminal(id_MuncipioId_Bairro);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return obTemp;
    }
	
	/**
	 * Método que atualiza o ID_PROJUDI na view V_SPGUREGBAIRROS_INFO.
	 * @param String codigoMunicipioBairro
	 * @param String idBairro
	 * @return boolean 
	 * @throws Exception
	 */
	public boolean atualizarIdProjudi(String codigoMunicipioBairro, String idBairro) throws Exception {
		boolean retorno = false;
		
		FabricaConexao obFabricaConexao = null;
		try {
			if( codigoMunicipioBairro != null && idBairro != null ) {
	            obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
	            
	            SpgRegiaoZonaPs obPersistencia = new SpgRegiaoZonaPs(obFabricaConexao.getConexao());
	            
	            retorno = obPersistencia.atualizarIdProjudi(codigoMunicipioBairro, idBairro, true);
	            
	            //Caso não tenha atualizado no CAPITAL atualiza no REMOTO
	            if( !retorno ) {
	            	retorno = obPersistencia.atualizarIdProjudi(codigoMunicipioBairro, idBairro, false);
	            }
			}
        }
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
        }
		
		return retorno;
	}

}
