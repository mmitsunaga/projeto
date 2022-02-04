package br.gov.tj.teste;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.pool.OracleDataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.gov.go.tj.projudi.jurisprudencia.Jurisprudencia;
import br.gov.go.tj.projudi.ne.JurisprudenciaNe;
import br.gov.go.tj.utils.ConexaoBD;

import com.softwareag.tamino.db.api.accessor.TAccessLocation;
import com.softwareag.tamino.db.api.accessor.TXMLObjectAccessor;
import com.softwareag.tamino.db.api.accessor.TXQuery;
import com.softwareag.tamino.db.api.accessor.TXQueryException;
import com.softwareag.tamino.db.api.connection.TConnection;
import com.softwareag.tamino.db.api.connection.TConnectionFactory;
import com.softwareag.tamino.db.api.objectModel.TIteratorException;
import com.softwareag.tamino.db.api.objectModel.TNoSuchXMLObjectException;
import com.softwareag.tamino.db.api.objectModel.TXMLObject;
import com.softwareag.tamino.db.api.objectModel.TXMLObjectIterator;
import com.softwareag.tamino.db.api.objectModel.dom.TDOMObjectModel;
import com.softwareag.tamino.db.api.response.TResponse;

public class TesteJurisprudencia {

	private final String NOME_PARTE_1 = "Embargante: JOÃO PEDRO FERNANDES CARDOSO";
	private final String NOME_PARTE_2 = "Embargado: WELLINGTON CARDOSO DA SILVA";
	
	private final String NOME_PARTE_1_INCOMPLETO = "JOÃO PEDRO FERNANDES CARDOSO";
	private final String NOME_PARTE_2_INCOMPLETO = "WELLINGTON CARDOSO DA SILVA";
	
	private static final String TAMINO_DATABASE_TJGO = "http://ino.tjgo.jus.br/tamino/jurisprudencia";
	
	private static TConnection taminoConnection = null;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		OracleDataSource dataSourceOracle = new OracleDataSource();
		dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/DESENV");
		dataSourceOracle.setUser("projudi");
		dataSourceOracle.setPassword("oracle123");
		ConexaoBD.setDatasourceConsulta(dataSourceOracle);
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
		taminoConnection = TConnectionFactory.getInstance().newConnection(TAMINO_DATABASE_TJGO);	
	}
	
	@Test
	public void quandoProcessoSegredoJusticaEntaoNomeParteSomenteIniciais (){
		Jurisprudencia juris = new Jurisprudencia();
		juris.setIn_segredo("S");
		juris.setParte1(NOME_PARTE_1);
		juris.setParte2(NOME_PARTE_2);
		Assert.assertEquals("Embargante: JPFC", juris.getParte1());
		Assert.assertEquals("Embargado: WCS", juris.getParte2());
	}
	
	@Test
	public void quandoProcessoNormalEntaoMostrarNomeParteSemRestricao () {
		Jurisprudencia juris = new Jurisprudencia();
		juris.setIn_segredo("N");
		juris.setParte1(NOME_PARTE_1);
		juris.setParte2(NOME_PARTE_2);
		Assert.assertEquals(NOME_PARTE_1, juris.getParte1());
		Assert.assertEquals(NOME_PARTE_2, juris.getParte2());
	}
	
	@Test
	public void quandoProcessoSegredoJusticaENomeParteIncompletoEntaoMostrarApenasIniciais(){
		Jurisprudencia juris = new Jurisprudencia();
		juris.setIn_segredo("S");
		juris.setParte1(NOME_PARTE_1_INCOMPLETO);
		juris.setParte2(NOME_PARTE_2_INCOMPLETO);
		Assert.assertEquals("JPFC", juris.getParte1());
		Assert.assertEquals("WCS", juris.getParte2());
	}
	
	@Test
	public void quandoAtribuitoInSegredoTemValorNRetornoFalse(){
		Jurisprudencia juris = new Jurisprudencia();
		juris.setIn_segredo("N");
		Assert.assertFalse(juris.isSegredoJustica());
	}
	
	@Test
	public void quandoAtribuitoInSegredoVazioEntaoRetornoFalso(){
		Jurisprudencia juris = new Jurisprudencia();
		juris.setIn_segredo("");
		Assert.assertFalse(juris.isSegredoJustica());
	}
	
	@Test
	public void quandoAtribuitoInSegredoNuloEntaoRetornoFalso(){
		Jurisprudencia juris = new Jurisprudencia();
		juris.setIn_segredo("");
		Assert.assertFalse(juris.isSegredoJustica());
	}
	
	private Map<String, Object> consultarJurisprudenciaPorNumeroProcesso (String numeroRecurso, String id_ementa) throws Exception {
    	Map<String, Object> item = new HashMap<String,Object>();
		String xQuery = "for $a in input()/DocJuris[nr_recurso = '" + numeroRecurso.trim() + "' and parte8='" + id_ementa.trim() + "'] return $a";
		TXMLObjectAccessor accessor = taminoConnection.newXMLObjectAccessor(TAccessLocation.newInstance("TJGO"), TDOMObjectModel.getInstance());
		TXQuery query = TXQuery.newInstance(xQuery);
        try {
        	TResponse response = accessor.xquery(query);
            TXMLObjectIterator iterator = response.getXMLObjectIterator();
            while (iterator.hasNext()) {
                TXMLObject xmlObject = iterator.next();
                Element e = (Element) xmlObject.getElement();                
                item.put("in_segredo", getDOMElementTextByTagName(e, "in_segredo"));
    			item.put("nr_recurso", getDOMElementTextByTagName(e, "nr_recurso"));
    			item.put("parte1", getDOMElementTextByTagName(e, "parte1"));
    			item.put("parte2", getDOMElementTextByTagName(e, "parte2"));
    			item.put("parte8", getDOMElementTextByTagName(e, "parte8"));
    				
    			item.put("ds_camara", getDOMElementTextByTagName(e, "ds_camara"));
    			item.put("nm_relator", getDOMElementTextByTagName(e, "nm_relator"));
    			item.put("ds_decisao", getDOMElementTextByTagName(e, "ds_decisao"));
    			item.put("ds_ementa", getDOMElementTextByTagName(e, "ds_ementa"));
    			    			
            }
            iterator.close();
        } catch (TNoSuchXMLObjectException ex1) {
            ex1.printStackTrace();
        } catch (TIteratorException ex2) {
            ex2.printStackTrace();
        } catch (TXQueryException ex3) {
            ex3.printStackTrace(); 
        }
        return item;
	}
	
	private String getDOMElementTextByTagName(Element element, String tagname) {
        NodeList nl = element.getElementsByTagName(tagname);        
        Node n;
        n = nl.item(0);
        n = n.getFirstChild();        
        return (n != null) ? n.getNodeValue() : "";
    }
	
	@Test
	public void testarSeJurisprudenciaProjudiEstaNoTamino() throws Exception {
		 Map<String, Object> item = consultarJurisprudenciaPorNumeroProcesso("56253627720198090000", "139392918");
		 Assert.assertTrue(!item.isEmpty());
	}
		
	//@Test
	public void indexarJurisprudenciaPorId() throws Exception {
		List<String> lista = new ArrayList<>();
		lista.add("ABCD");
		new JurisprudenciaNe().indexarEmentaJurisprudenciaTaminoPorId(lista);
	}
	
	//@Test
	public void indexarJurisprudenciaPorLote() throws Exception {
		// Atenção: Leitura de arquivo texto com id de arquivo e migração de dados para o tamino	
		Path path = Paths.get("D:/", "source-juris.txt");
		Charset charset = Charset.forName("ISO-8859-1");
		List<String> lista = Files.readAllLines(path, charset);		
		new JurisprudenciaNe().indexarEmentaJurisprudenciaTaminoPorId(lista);
	}
	
}


