package br.gov.tj.teste;

import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.utils.ConexaoBD;

public class TesteComarcaNe {

	private static final String CODIGO_GOIANIA = "39";
	private static final String COMARCA_GOIANIA = "Goiânia";
	
	private static final String ID_CODIGO_INVALIDO = "9999";
	private static final String COMARCA_INVALIDA = "BlaBlaBla";
	private static final String COMARCA_ALTERADA_INVALIDA = "Lorem Ipsum";
	
	private static final String ID_USUARIO_PROJUDI = "1";
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		OracleDataSource dataSourceOracle = new OracleDataSource();
		dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/DESENV");
		dataSourceOracle.setUser("projudi");
		dataSourceOracle.setPassword("oracle123");		
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
	}
	
	@Test
	public void testarConsultarId() throws Exception {
		ComarcaDt comarca1 = new ComarcaNe().consultarComarcaCodigo(CODIGO_GOIANIA);
		ComarcaDt comarca2 = new ComarcaNe().consultarId(comarca1.getId());
		Assert.assertTrue(comarca1.getId().equals(comarca2.getId()));
	}
	
	@Test
	public void testarConsultarIdInexistente() throws Exception {
		ComarcaDt comarca = new ComarcaNe().consultarId(ID_CODIGO_INVALIDO);
		Assert.assertFalse(comarca != null);
	}
	
	@Test
	public void testarConsultarNomeCodigo() throws Exception {
		String nomeComarca = new ComarcaNe().consultarCodigo(CODIGO_GOIANIA);
		Assert.assertTrue(nomeComarca.equalsIgnoreCase(COMARCA_GOIANIA));
	}
	
	@Test
	public void testarConsultarNomeCodigoInexistente() throws Exception {
		String nomeComarca = new ComarcaNe().consultarCodigo(ID_CODIGO_INVALIDO);
		Assert.assertFalse(nomeComarca.equals(COMARCA_INVALIDA));
	}
	
	@Test
	public void testarConsultarComarcaCodigo() throws Exception {		
		ComarcaDt comarca = new ComarcaNe().consultarComarcaCodigo(CODIGO_GOIANIA);
		Assert.assertTrue(comarca.getComarcaCodigo().equals(CODIGO_GOIANIA));
	}
	
	@Test
	public void testarConsultarComarcaCodigoInexistente() throws Exception {		
		ComarcaDt comarca = new ComarcaNe().consultarComarcaCodigo(COMARCA_INVALIDA);
		Assert.assertNull(comarca);
	}
	
	@Test
	public void testarListar() throws Exception {		
		List lista = new ComarcaNe().listar();
		Assert.assertTrue(lista.size() > 0);
	}
	
	@Test
	public void testarInserir() throws Exception {
		ComarcaNe comarcaNe = new ComarcaNe();
		comarcaNe.salvar(preparaDadosParaIncluir());
		ComarcaDt novaComarca = comarcaNe.consultarComarcaCodigo(ID_CODIGO_INVALIDO);
		Assert.assertTrue(novaComarca.getComarca().equalsIgnoreCase(COMARCA_INVALIDA));		
		reverterOperacao(novaComarca);
	}
	
	@Test
	public void testarAlterar() throws Exception {
		ComarcaNe comarcaNe = new ComarcaNe();
		comarcaNe.salvar(preparaDadosParaIncluir());				
		ComarcaDt novaComarca = comarcaNe.consultarComarcaCodigo(ID_CODIGO_INVALIDO);
		novaComarca.setComarca(COMARCA_ALTERADA_INVALIDA);
		novaComarca.setId_UsuarioLog(ID_USUARIO_PROJUDI);
		comarcaNe.salvar(novaComarca);		
		ComarcaDt comarcaAlterada = comarcaNe.consultarComarcaCodigo(ID_CODIGO_INVALIDO);
		Assert.assertTrue(comarcaAlterada.getComarca().equalsIgnoreCase(COMARCA_ALTERADA_INVALIDA));						
		reverterOperacao(comarcaAlterada);
	}
		
	@Test
	public void testarExcluir() throws Exception {
		ComarcaNe comarcaNe = new ComarcaNe();		
		comarcaNe.salvar(preparaDadosParaIncluir());		
		ComarcaDt novaComarca = new ComarcaNe().consultarComarcaCodigo(ID_CODIGO_INVALIDO);
		novaComarca.setId_UsuarioLog(ID_USUARIO_PROJUDI);
		comarcaNe.excluir(novaComarca);				
		Assert.assertNull(comarcaNe.consultarId(novaComarca.getId()));
	}
	
	private void reverterOperacao(ComarcaDt comarcaDt) throws Exception{
		comarcaDt.setId_UsuarioLog(ID_USUARIO_PROJUDI);
		new ComarcaNe().excluir(comarcaDt);
	}
	
	private ComarcaDt preparaDadosParaIncluir(){
		ComarcaDt comarca = new ComarcaDt();
		comarca.setComarca(COMARCA_INVALIDA);
		comarca.setComarcaCodigo(ID_CODIGO_INVALIDO);
		comarca.setId_UsuarioLog(ID_USUARIO_PROJUDI);
		return comarca;
	}
	
}
