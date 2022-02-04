package br.gov.tj.teste;

import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.go.tj.projudi.ne.ProcessoTemaNe;
import br.gov.go.tj.projudi.ne.TemaNe;
import br.gov.go.tj.utils.ConexaoBD;
import br.jus.cnj.bnpr.model.Tupla;

public class TesteTemaNe {
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		
		OracleDataSource dataSourceOracle = new OracleDataSource();
		dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/DESENV");
		dataSourceOracle.setUser("projudi");
		dataSourceOracle.setPassword("oracle123");
		
		ConexaoBD.setDatasourceConsulta(dataSourceOracle);
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
		
		Class.forName("com.Connx.jdbc.TCJdbc.TCJdbcDriver").newInstance();
		String urlAdabas = "jdbc:connx:DD=ADABASPROJUDI;GATEWAY=prod.gyn.tjgo;PORT=7500";
		ConexaoBD.setUrlAdabas(urlAdabas, "cxadasql", "sag123");
		
	}
	
	@Test
	public void testarCamposObrigatoriosProcessoSobrestadoProjudi() throws Exception {
		ProcessoTemaNe processoTemaNe = new ProcessoTemaNe();
		List<Tupla> lista = processoTemaNe.listarProcessosSobrestadosPorPeriodo(null, null);		
		for (Tupla item: lista){
			assertCamposObrigatorios(item);
		}
	}
	
	@Test
	public void testarCamposObrigatoriosProcessoSobrestadoSSG() throws Exception {
		ProcessoTemaNe processoTemaNe = new ProcessoTemaNe();
		List<Tupla> lista = processoTemaNe.listarProcessosSobrestados_SSG();
		for (Tupla item: lista){
			assertCamposObrigatorios(item);
		}		
	}
	
	@Test
	public void testarEnvioProcessosSobrestadosCNJ() throws Exception {
		ProcessoTemaNe processoTemaNe = new ProcessoTemaNe();
		List<Tupla> lista = processoTemaNe.listarProcessosSobrestadosPorPeriodo(null, null);
		new TemaNe().BnprEnviarProcessosSobrestados(lista);
	}
	
	private void assertCamposObrigatorios(Tupla item){
		Assert.assertNotNull("Código", item.getChave().getCodigo());
		Assert.assertNotNull("Tipo", item.getChave().getTipo());
		Assert.assertNotNull("Sigla", item.getChave().getSigla());
		Assert.assertNotNull("Classe", item.getProcesso().getClasse());
		Assert.assertNotNull("Número do processo", item.getProcesso().getNumero());
		Assert.assertNotNull("Data de distribuição-" + item.getProcesso().getNumero(), item.getProcesso().getDataDistribuicao());
		Assert.assertNotNull("data de sobrestamento", item.getProcesso().getDataSobrestamento());		
	}
	
}
