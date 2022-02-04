package br.gov.go.tj.projudi.ne;

import java.util.Date;

import br.gov.go.tj.projudi.dt.UfrValorDt;
import br.gov.go.tj.projudi.ps.UfrValorPs;
import br.gov.go.tj.utils.FabricaConexao;

public class UfrValorNe extends Negocio {

	private static final long serialVersionUID = -6591339651476351313L;	
	

	/**
	 * Método para obter o valor UFR pelo Id.
	 * @param id_UFR_Valor
	 * @return UFRValorDt
	 * @throws Exception
	 */	
	public UfrValorDt consultarId(String id_UFR_Valor) throws Exception {
		UfrValorDt retorno = null;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UfrValorPs obPersistencia = new UfrValorPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarId(id_UFR_Valor);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para obter o valor UFR pela data.
	 * @param Date date
	 * @return UFRValorDt
	 * @throws Exception
	 */	
	public UfrValorDt consultarData(Date date) throws Exception {
		UfrValorDt retorno = null;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UfrValorPs obPersistencia = new UfrValorPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarData(date);
		} finally {
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para obter o valor UFR pela data.
	 * @return UFRValorDt
	 * @throws Exception
	 */	
	public UfrValorDt consultarDataAtual() throws Exception {
		UfrValorDt retorno = null;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UfrValorPs obPersistencia = new UfrValorPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarData(new Date());
		} finally {
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
//	/**
//	 * Método para consultar o ultimo valor da UFR da taxa judiciária. 
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double obterUltimoValorUFRTaxaJudiciaria() throws Exception {
//		Double retorno = null;
//		
//		FabricaConexao obFabricaConexao = null;
//		
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			UfrValorPs obPersistencia = new UfrValorPs(obFabricaConexao.getConexao());
//			
//			retorno = obPersistencia.obterUltimoValorUFRTaxaJudiciaria();
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//		
//		return retorno;
//	}
//	
//	/**
//	 * Método para consultar o ultimo valor da UFR. 
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double obterUltimoValorUFR() throws Exception {
//		Double retorno = null;
//		
//		FabricaConexao obFabricaConexao = null;
//		
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			UfrValorPs obPersistencia = new UfrValorPs(obFabricaConexao.getConexao());
//			
//			retorno = obPersistencia.obterUltimoValorUFR();
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//		
//		return retorno;
//	}
//	
	/**
	 * Método para obter o valor da taxa judiciária pela data de protocolo do processo.
	 * @param Date date
	 * @return Double
	 * @throws Exception
	 */
//	public Double obterValorUFRTaxaJudiciaria(Date date) throws Exception {
//		Double retorno = null;
//		
//		FabricaConexao obFabricaConexao = null;
//		
//		try {
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			UfrValorPs obPersistencia = new UfrValorPs(obFabricaConexao.getConexao());
//			
//			retorno = obPersistencia.obterValorUFRTaxaJudiciaria(date);
//		} finally {
//			obFabricaConexao.fecharConexao();
//		}
//		
//		return retorno;
//	}
}