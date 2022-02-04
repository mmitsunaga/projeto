package br.gov.tj.teste;

import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ps.ComarcaPs;
import br.gov.go.tj.utils.ConexaoBD;
import br.gov.go.tj.utils.FabricaConexao;

public class TesteComarcaPs  {
	
	private static final String CODIGO_GOIANIA = "39";
	private static final String COMARCA_GOIANIA = "Goiânia";
	
	private static final String ID_CODIGO_INVALIDO = "9999";
	private static final String COMARCA_INVALIDA = "BlaBlaBla";
	
	private static FabricaConexao fabricaconexao = null;
	
	/**
	 * Método de configuração inicial invocado antes de TODOS os casos de teste. 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		OracleDataSource dataSourceOracle = new OracleDataSource();
		dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/DESENV");
		dataSourceOracle.setUser("projudi");
		dataSourceOracle.setPassword("oracle123");		
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
		fabricaconexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	}
	
	/**
	 * Método de conclusão invocado ao final da execução de TODOS os casos de teste.
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownClass() throws Exception {
		fabricaconexao.fecharConexao();
	}
	
	/**
	 * Método de configuração inicial invocado antes de cada caso de teste. 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {		
		fabricaconexao.iniciarTransacao();
	}
	
	/**
	 * Método invocado ao término de cada caso de teste.
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		fabricaconexao.cancelarTransacao();		
	}	
	
	@Test
	public void testarConsultarId() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		ComarcaDt comarca1 = comarcaPs.consultarComarcaCodigo(CODIGO_GOIANIA);		
		ComarcaDt comarca2 = comarcaPs.consultarId(comarca1.getId());
		Assert.assertTrue(comarca1.getId().equals(comarca2.getId()));		
	}
	
	@Test
	public void testarConsultarIdInexistente() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		ComarcaDt comarca = comarcaPs.consultarId(ID_CODIGO_INVALIDO);
		Assert.assertFalse(comarca != null);
	}
	
	@Test
	public void testarConsultarNomeCodigo() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		String nomeComarca = comarcaPs.consultarNomeCodigo(CODIGO_GOIANIA);
		Assert.assertTrue(nomeComarca.equalsIgnoreCase(COMARCA_GOIANIA));
	}
	
	@Test
	public void testarConsultarNomeCodigoInexistente() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		String nomeComarca = comarcaPs.consultarNomeCodigo(ID_CODIGO_INVALIDO);
		Assert.assertFalse(nomeComarca.equals(COMARCA_INVALIDA));
	}
	
	@Test
	public void testarConsultarComarcaCodigo() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		ComarcaDt comarca = comarcaPs.consultarComarcaCodigo(CODIGO_GOIANIA);
		Assert.assertTrue(comarca.getComarcaCodigo().equals(CODIGO_GOIANIA));
	}
	
	@Test
	public void testarConsultarComarcaCodigoInexistente() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		ComarcaDt comarca = comarcaPs.consultarComarcaCodigo(COMARCA_INVALIDA);
		Assert.assertNull(comarca);
	}
	
	@Test
	public void testarListar() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		List lista = comarcaPs.listar();
		Assert.assertTrue(lista.size() > 0);
	}
	
	@Test
	public void testarInserir() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		comarcaPs.inserir(preparaDadosParaIncluir());		
		ComarcaDt novaComarca = comarcaPs.consultarComarcaCodigo(ID_CODIGO_INVALIDO);
		Assert.assertTrue(novaComarca.getComarca().equalsIgnoreCase(COMARCA_INVALIDA));
	}
		
	@Test
	public void testarAlterar() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		ComarcaDt comarca1 = comarcaPs.consultarComarcaCodigo(CODIGO_GOIANIA);
		comarca1.setComarca(COMARCA_INVALIDA);
		comarcaPs.alterar(comarca1);		
		ComarcaDt comarca2 = comarcaPs.consultarComarcaCodigo(CODIGO_GOIANIA);
		Assert.assertTrue(comarca1.getComarca().equalsIgnoreCase(comarca2.getComarca()));
	}
		
	@Test
	public void testarExcluir() throws Exception {
		ComarcaPs comarcaPs = new ComarcaPs(fabricaconexao.getConexao());
		// Insere uma nova comarca fictícia
		comarcaPs.inserir(preparaDadosParaIncluir());		
		ComarcaDt novaComarca = comarcaPs.consultarComarcaCodigo(ID_CODIGO_INVALIDO);
		// Exclui a nova comarca
		comarcaPs.excluir(novaComarca.getId());
		//Consulta novamente para confirmar a exclusão			
		Assert.assertNull(comarcaPs.consultarId(novaComarca.getId()));
	}
	
	private ComarcaDt preparaDadosParaIncluir(){
		ComarcaDt comarca = new ComarcaDt();
		comarca.setComarca(COMARCA_INVALIDA);
		comarca.setComarcaCodigo(ID_CODIGO_INVALIDO);
		return comarca;
	}
	
}
