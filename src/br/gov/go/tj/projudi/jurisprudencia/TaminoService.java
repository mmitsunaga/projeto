package br.gov.go.tj.projudi.jurisprudencia;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.softwareag.tamino.db.api.accessor.TAccessLocation;
import com.softwareag.tamino.db.api.accessor.TInsertException;
import com.softwareag.tamino.db.api.accessor.TQueryException;
import com.softwareag.tamino.db.api.accessor.TXMLObjectAccessor;
import com.softwareag.tamino.db.api.accessor.TXQuery;
import com.softwareag.tamino.db.api.accessor.TXQueryException;
import com.softwareag.tamino.db.api.connection.TConnection;
import com.softwareag.tamino.db.api.connection.TConnectionFactory;
import com.softwareag.tamino.db.api.connection.TServerNotAvailableException;
import com.softwareag.tamino.db.api.objectModel.TIteratorException;
import com.softwareag.tamino.db.api.objectModel.TNoSuchXMLObjectException;
import com.softwareag.tamino.db.api.objectModel.TXMLObject;
import com.softwareag.tamino.db.api.objectModel.TXMLObjectIterator;
import com.softwareag.tamino.db.api.objectModel.dom.TDOMObjectModel;
import com.softwareag.tamino.db.api.response.TResponse;

public class TaminoService {

	private static final String ambienteURL = Ambiente.PRODUCAO.getUrl();

	public static void insertXml(String xml) throws TServerNotAvailableException, TInsertException {
		TConnection connection;
		TXMLObjectAccessor accessor;
		connection = TConnectionFactory.getInstance().newConnection(ambienteURL);
		accessor = connection.newXMLObjectAccessor(TAccessLocation.newInstance("TJGO"), TDOMObjectModel.getInstance());
		TXMLObject xmlInsert = TXMLObject.newInstance(xml);
		accessor.insert(xmlInsert);
	}

	public static String getLastIndexedId() throws TServerNotAvailableException, TXQueryException, TIteratorException,
			TQueryException, TNoSuchXMLObjectException {
		String lastIndexeTaminoId = null;
		TConnection connection;
		TXMLObjectAccessor accessor;
		connection = TConnectionFactory.getInstance().newConnection(ambienteURL);
		accessor = connection.newXMLObjectAccessor(TAccessLocation.newInstance("TJGO"), TDOMObjectModel.getInstance());

		String docType = "DocJuris";

		String xQuery = "max(for $lastId in input()/" + docType + "/parte8 "
				+ "where $lastId!=\"\" order by $lastId descending " + "return $lastId)";

		TXQuery query = TXQuery.newInstance(xQuery);

		TResponse response = accessor.xquery(query);

		TXMLObjectIterator iterator = response.getXMLObjectIterator();

		while (iterator.hasNext()) {
			TXMLObject xmlObject = iterator.next();
			Element e = (Element) xmlObject.getElement();
			lastIndexeTaminoId = getDOMElementTextByTagName(e, "");
		}

		return lastIndexeTaminoId;
	}
	
	/**
	 * Consulta se o registro está no tamino, procurando por id da ementa e número do processo
	 * @param numeroRecurso
	 * @param id_ementa
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> consultarJurisprudenciaPorNumeroProcesso (String numeroRecurso, String id_ementa) throws Exception {
		TConnection connection = TConnectionFactory.getInstance().newConnection(ambienteURL);
		TXMLObjectAccessor accessor = connection.newXMLObjectAccessor(TAccessLocation.newInstance("TJGO"), TDOMObjectModel.getInstance());
		Map<String, Object> item = new HashMap<String,Object>();
    	String xQuery = "for $a in input()/DocJuris[nr_recurso = '" + numeroRecurso.trim() + "' and parte8='" + id_ementa.trim() + "'] return $a";		
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
            }
            iterator.close();            
            connection.close();
        } catch (TNoSuchXMLObjectException ex1) {
            ex1.printStackTrace();
        } catch (TIteratorException ex2) {
            ex2.printStackTrace();
        } catch (TXQueryException ex3) {
            ex3.printStackTrace(); 
        }        
        return item;
	}
	

	private static String getDOMElementTextByTagName(Element element, String tagname) {
		Node n = element.getFirstChild();
		return (n != null) ? n.getNodeValue() : "";
	}

}
