package br.gov.go.tj.projudi.util;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;

public class DistribuicaoProcessoServentiaCargo {

	private static DistribuicaoProcessoServentiaCargo objeto = null;

	private DistribuicaoProcessoServentiaCargo(){

	}

	static public DistribuicaoProcessoServentiaCargo getInstance(){
		if (objeto == null) objeto = new DistribuicaoProcessoServentiaCargo();

		return objeto;
	}

	/**
	 * Método que irá retornar o próximo ServentiaCargo de um promotor para receber o processo
	 * 
	 * @param id_serventia
	 * @param id_processoTipo
	 * @return id_ServentiaCargo	 *     
	 * jrcorrea    
	 * @throws Exception 
	 */
	public String getDistribuicaoPromotor(String id_serventia, String Id_ProcessoTipo) throws Exception{
		String stTemp = null;
		stTemp = new ServentiaCargoNe().consultarServentiaCargosDistribuicao1Grau(id_serventia, Id_ProcessoTipo, GrupoTipoDt.MP);

		return stTemp;
	}
	
	
//	/**
//	 * Método que irá retornar o próximo ServentiaCargo a receber um processo para juiz.
//
//	 * 
//	 * @param id_serventia
//	 * @param id_processoTipo
//	 * @return string com id_serventiaCargo
//	 * jrcorrea
//	 * @throws Exception 
//	 */
//	public String getDistribuicao1Grau(String id_serventia, String Id_ProcessoTipo) throws Exception{
//		String stTemp = null;
//		stTemp = new ServentiaCargoNe().consultarServentiaCargosDistribuicao1Grau(id_serventia, Id_ProcessoTipo, GrupoTipoDt.JUIZ_VARA);
//
//		return stTemp;
//	}
	
//	/**
//	 * Método que irá retornar o próximo ServentiaCargo a receber um processo para juiz.
//
//	 * 
//	 * @param id_serventia
//	 * @param id_processoTipo
//	 * @return string com id_serventiaCargo
//	 * 09/10/2013
//	 * jrcorrea
//	 * @throws Exception 
//	 */
//	public String getDistribuicao1Grau(String id_serventia) throws Exception{
//		String stTemp = null;
//		stTemp = new ServentiaCargoNe().consultarServentiaCargosDistribuicao1Grau(id_serventia, GrupoTipoDt.JUIZ_VARA);
//		
//		return stTemp;
//	}

//	public ServentiaCargoDt getDistribuicao2Grau(String id_serventia, String Id_ProcessoTipo) throws Exception{
//		ServentiaCargoDt serventiaCargoDt = null;
//		serventiaCargoDt = new ServentiaCargoNe().consultarServentiaCargosDistribuicao2Grau(id_serventia, Id_ProcessoTipo, GrupoTipoDt.DESEMBARGADOR);
//		
//		return serventiaCargoDt;
//	}

	/*
	 * provimento 16/2012 CGJ
	 * @author jrcorre
	 * 09/10/2013
	 */
	
//	public ServentiaCargoDt getDistribuicao2Grau(String id_serventia) throws Exception{
//		ServentiaCargoDt serventiaCargoDt = null;
//		serventiaCargoDt = new ServentiaCargoNe().consultarServentiaCargosDistribuicao2Grau(id_serventia, GrupoTipoDt.DESEMBARGADOR);	
//
//		return serventiaCargoDt;
//	}	
//	/**
//	 * Método que consulta outros relator, revisor e vogal dentro da mesma
//	 * câmara para receber o processo.
//	 * 
//	 * @param id_serventia
//	 *            - ID da Servendia
//	 * @param Id_ProcessoTipo
//	 *            - Tipo de processo
//	 * @param grupoTipoCodigo
//	 *            - grupo tipo
//	 * @param id_RelatorAtual
//	 *            - id do relator atual do processo
//	 * @return lista de possíveis desembargadores que podem receber o processo
//	 * @throws Exception
//	 * @author hmgodinho
//	 */
//	public ServentiaCargoDt getDistribuicao2GrauPropriaServentia(String id_serventia, String Id_ProcessoTipo, String grupoTipoCodigo, String id_RelatorAtual) throws Exception{
//		ServentiaCargoDt seventiaCargoDt = null;
//		seventiaCargoDt = new ServentiaCargoNe().consultarServentiaCargosDistribuicao2GrauPropriaServentia(id_serventia, Id_ProcessoTipo, grupoTipoCodigo, id_RelatorAtual);
//		
//		return seventiaCargoDt;
//	}
//
	
//	/**
//	 * Método que consulta outros relator, revisor e vogal dentro da mesma
//	 * câmara para receber o processo.
//	 * 
//	 * @param id_serventia
//	 *            - ID da Servendia	
//	 * @param grupoTipoCodigo
//	 *            - grupo tipo
//	 * @param id_RelatorAtual
//	 *            - id do relator atual do processo
//	 * @return lista de possíveis desembargadores que podem receber o processo
//	 * @throws Exception
//	 * provimento 16/2012 CGJ
//	 * @author jrcorre
//	 * 09/10/2013
//	 */
//	public ServentiaCargoDt getDistribuicao2GrauPropriaServentia(String id_serventia,  String grupoTipoCodigo, String id_RelatorAtual) throws Exception{
//		ServentiaCargoDt seventiaCargoDt = null;
//		seventiaCargoDt = new ServentiaCargoNe().consultarServentiaCargosDistribuicao2GrauPropriaServentia(id_serventia,  grupoTipoCodigo, id_RelatorAtual);
//		
//		return seventiaCargoDt;
//	}

//	public String getDistribuicaoTurma(String id_serventia, String Id_ProcessoTipo) throws Exception{
//		String serventiaCargoDt;
//		serventiaCargoDt = new ServentiaCargoNe().consultarServentiaCargosDistribuicaoTurma(id_serventia, Id_ProcessoTipo, GrupoTipoDt.JUIZ_TURMA);	
//
//		return serventiaCargoDt;
//	}
//	/*
//	 * provimento 16/2012 CGJ
//	 * @author jrcorre
//	 * 09/10/2013
//	 */
//	public String getDistribuicaoTurma(String id_serventia) throws Exception{
//		String serventiaCargoDt;
//		serventiaCargoDt = new ServentiaCargoNe().consultarServentiaCargosDistribuicaoTurma(id_serventia,  GrupoTipoDt.JUIZ_TURMA);		
//
//		return serventiaCargoDt;
//	}

}
