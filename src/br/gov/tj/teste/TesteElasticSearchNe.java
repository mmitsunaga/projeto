package br.gov.tj.teste;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.go.tj.projudi.ne.ElasticSearchNe;
import br.gov.go.tj.utils.ConexaoBD;

public class TesteElasticSearchNe {
	
	private final int LIMIT = 1000;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		OracleDataSource dataSourceOracle = new OracleDataSource();
		dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/DESENV");
		dataSourceOracle.setUser("projudi");
		dataSourceOracle.setPassword("oracle123");		
		ConexaoBD.setDatasourceConsulta(dataSourceOracle);
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
	}
	
	@Test
	public void init() throws Exception {
		new ElasticSearchNe().inicializarIndexacao(LIMIT);		
	}
	
}
