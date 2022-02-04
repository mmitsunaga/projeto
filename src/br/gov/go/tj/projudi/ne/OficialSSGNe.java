package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.dt.OficialSSGDt;
import br.gov.go.tj.projudi.ps.OficialSSGPs;
import br.gov.go.tj.utils.FabricaConexao;

public class OficialSSGNe extends Negocio {

	private static final long serialVersionUID = 643858832929997180L;
	
	private OficialSSGPs oficialSSGPs;
	
	/**
	 * Método para consultar lista de oficiais.
	 * @param List<OficiaisSSGDt>
	 * @throws Exception
	 */
	public List consultarOficiais() throws Exception {
		FabricaConexao obFabricaConexao =null;
		
		List listaOficiaisSSGDt = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			oficialSSGPs = new  OficialSSGPs(obFabricaConexao.getConexao());
			
			listaOficiaisSSGDt = oficialSSGPs.consultarOficiais();
		
		}
		finally{
            obFabricaConexao.fecharConexao();
        }
		
		return listaOficiaisSSGDt;
	}
	
	/**
	 * Método para consultar o oficial através do codigo.
	 * @param String codigoOficial
	 * @return OficiaisSSGDt
	 * @throws Exception
	 */
	public OficialSSGDt consultaOficial(String codigoOficial) throws Exception {
		FabricaConexao obFabricaConexao =null;
		
		OficialSSGDt oficialSSGDt = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			oficialSSGPs = new  OficialSSGPs(obFabricaConexao.getConexao());
			
			if (codigoOficial.equals(OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA)){
				oficialSSGDt = new OficialSSGDt();
				oficialSSGDt.setCodigoOficial(codigoOficial);
				oficialSSGDt.setNomeOficial(OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA);
			} else {
				oficialSSGDt = oficialSSGPs.consultaOficial(codigoOficial);
			}
		
		}
		finally{
            obFabricaConexao.fecharConexao();
        }
		
		return oficialSSGDt;
	}
}