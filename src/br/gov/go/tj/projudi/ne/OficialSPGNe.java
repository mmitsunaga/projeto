package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.OficialSPGDt;
import br.gov.go.tj.projudi.ps.OficialSPGPs;
import br.gov.go.tj.utils.FabricaConexao;

public class OficialSPGNe extends Negocio {

	private static final long serialVersionUID = -5831115494657489010L;
	
	private OficialSPGPs oficialSPGPs;
	
	/**
	 * Método para consultar os oficiais da comarca.
	 * @param String comarcaCodigo
	 * @param List<OficiaisSPGDt>
	 * @throws Exception
	 */
	public List consultarOficiaisComarca(String comarcaCodigo) throws Exception {
		FabricaConexao obFabricaConexao =null;
		
		List listaOficiaisSPGDt = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			oficialSPGPs = new  OficialSPGPs(obFabricaConexao.getConexao());
			
			listaOficiaisSPGDt = oficialSPGPs.consultarOficiaisComarca(comarcaCodigo);
		
		
		}
		finally{
            if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
        }
		
		return listaOficiaisSPGDt;
	}
	
	/**
	 * Método para consultar o oficial através do codigo.
	 * @param String codigoOficial
	 * @return OficialSPGDt
	 * @throws Exception
	 */
	public OficialSPGDt consultaOficial(String codigoOficial) throws Exception {
		FabricaConexao obFabricaConexao =null;
		
		OficialSPGDt oficialSPGDt = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			oficialSPGPs = new  OficialSPGPs(obFabricaConexao.getConexao());
			
			if (codigoOficial.equals(OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA)){
				oficialSPGDt = new OficialSPGDt();
				oficialSPGDt.setCodigoOficial(codigoOficial);
				oficialSPGDt.setNomeOficial(OficialSPGDt.NOME_OFICIAL_TRIBUNAL_JUSTICA);
			} else {
				oficialSPGDt = oficialSPGPs.consultaOficial(codigoOficial);
			}
		
		
		}
		finally{
            obFabricaConexao.fecharConexao();
        }
		
		return oficialSPGDt;
	}
	
	public String consultarOficiaisComarcaJSON(String descricao, String comarcaCodigo, String posicaopaginaatual) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			oficialSPGPs = new OficialSPGPs(obFabricaConexao.getConexao());
			
			stTemp = oficialSPGPs.consultarOficiaisComarcaJSON(descricao, comarcaCodigo, posicaopaginaatual);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return stTemp;
	}
	
	/**
	 * Método para consultar o oficial através pelo cpf.
	 * @param String cpf
	 * @return OficialSPGDt
	 * @throws Exception
	 * @author fasoares
	 */
	public OficialSPGDt consultaOficialCpf(String cpf) throws Exception {
		FabricaConexao obFabricaConexao = null;
		OficialSPGDt oficialSPGDt = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
			oficialSPGPs = new OficialSPGPs(obFabricaConexao.getConexao());
			
			oficialSPGDt = oficialSPGPs.consultaOficialCpf(cpf);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return oficialSPGDt;
	}
}
