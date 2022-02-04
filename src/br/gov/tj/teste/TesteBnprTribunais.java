package br.gov.tj.teste;

import java.net.URL;
import java.util.List;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.ne.TemaNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.ConexaoBD;
import br.jus.cnj.bnpr.BnprTribunaisWS;
import br.jus.cnj.bnpr.RetornoTipoIRDR;
import br.jus.cnj.bnpr.TipoIRDR;
import br.jus.cnj.bnpr.TipoRespostaIRDR;
import br.jus.cnj.bnpr.TipoSiglaOrgao;
import br.jus.cnj.bnpr.factory.BnprTribunaisWsFactory;

public class TesteBnprTribunais {
	
	private static int ID_NOVO_IRDR = 19146;
	
	private static int CODIGO_PRECEDENTE_RG = 949;
	
	private static int CODIGO_PRECEDENTE_RR = 558;
	
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
	
	/**
	 * Testar o envio de novo IRDR.
	 * 1 - Criar novo IRDR no projudi, sem nut.
	 * 2- Enviar o irdr para o cnj usando o metodo TemaNe.BnprEnviarNovoPrecedenteTJGO()
	 * 3 - Consultar o irdr no cnj e comparar os valores de nut com o registro no oracle 
	 * @throws Exception
	 */
	@Test
	public void bnprEnviarNovoPrecedenteTJGO_NovoTemaIRDR_DeveRetornarNumeroNUT() throws Exception {
		TemaNe temaNe = new TemaNe();
		TemaDt tema = temaNe.consultarId(String.valueOf(ID_NOVO_IRDR));		
		temaNe.BnprEnviarNovoPrecedenteTJGO(tema);
		TemaDt temaBD = temaNe.consultarId(String.valueOf(ID_NOVO_IRDR));
		RetornoTipoIRDR retornoTipoIRDR = pesquisarIRDemandasRepetitivas(tema.getTemaCodigo());
		Assert.assertTrue(temaBD.getNumeroIrdrCnj().equals(retornoTipoIRDR.getNut()));
	}
	
	@Test
	public void bnprEnviarNovoPrecedenteTJGO_AtributoTeseAlterado_DeveAtualizarNoCNJ() throws Exception {
		TemaNe temaNe = new TemaNe();
		TemaDt temaDt = temaNe.consultarTemaPorCodigoEOrigem("50", "TJGO").get(0);
		temaDt.setTeseFirmada("Donec sit amet ligula");
		temaNe.BnprEnviarNovoPrecedenteTJGO(temaDt);
		RetornoTipoIRDR retornoTipoIRDR = pesquisarIRDemandasRepetitivas(temaDt.getTemaCodigo());
		TipoIRDR tipoIRDR = retornoTipoIRDR.getTipoIRDR();
		Assert.assertEquals(tipoIRDR.getTese(), temaDt.getTeseFirmada());
	}
	
	/**
	 * Testar se novos precedentes Repercussao Geral no STF já foram cadastrados no projudi.
	 * - Antes de iniciar o teste, excluir o tema de código em CODIGO_PRECEDENTE.
	 * @throws Exception
	 */
	@Test
	public void testarNovoPrecedenteRG() throws Exception {
		TemaNe temaNe = new TemaNe();
		temaNe.BnprNovosPrecedentesRG();
		List<TemaDt> temas = temaNe.consultarTemaPorCodigoEOrigem(String.valueOf(CODIGO_PRECEDENTE_RG), "STF");
		Assert.assertNotNull(temas);
		Assert.assertTrue(temas.size() > 0);
	}
	
	/**
	 * Testar se novos precedentes Repercussao Geral no STJ já foram cadastrados no projudi.
	 * - Antes de iniciar o teste, excluir o tema de código em CODIGO_PRECEDENTE.
	 * @throws Exception
	 */	
	@Test
	public void testarNovoPrecedenteRR() throws Exception {
		TemaNe temaNe = new TemaNe();
		temaNe.BnprNovosPrecedentesRR();
		List<TemaDt> temas = temaNe.consultarTemaPorCodigoEOrigem(String.valueOf(CODIGO_PRECEDENTE_RR), "STJ");
		Assert.assertNotNull(temas);
		Assert.assertTrue(temas.size() > 0);
	}
	
	@Test
	public void pesquisarIRDemandasRepetitivas() throws Exception {
		TemaDt tema = new TemaNe().consultarId(String.valueOf(ID_NOVO_IRDR));
		RetornoTipoIRDR retornoTipoIRDR = pesquisarIRDemandasRepetitivas(tema.getTemaCodigo());
		Assert.assertNotNull(retornoTipoIRDR);
	}
	
	private RetornoTipoIRDR pesquisarIRDemandasRepetitivas (String codigo) throws Exception {
		RetornoTipoIRDR retornoTipoIRDR = null;
		BnprTribunaisWS port = BnprTribunaisWsFactory.getInstance(new URL(ProjudiPropriedades.getInstance().getUrlBnprTribunais()));
		TipoRespostaIRDR resposta = port.pesquisarIRDemandasRepetitivas(null, Long.valueOf(codigo), TipoSiglaOrgao.TJGO, 1);
		if (resposta.getTotalProcessos() > 0 && resposta.getIRDemandasRepetivivas().size() > 0){
			retornoTipoIRDR = resposta.getIRDemandasRepetivivas().get(0);
		}		
		return retornoTipoIRDR;		
	}
	
}
