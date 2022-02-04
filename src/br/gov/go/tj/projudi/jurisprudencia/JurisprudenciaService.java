package br.gov.go.tj.projudi.jurisprudencia;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.softwareag.tamino.db.api.accessor.TInsertException;
import com.softwareag.tamino.db.api.connection.TServerNotAvailableException;


public class JurisprudenciaService {

	public static void inserirJurisprudencia(Jurisprudencia jurisprudencia)
			throws ParserConfigurationException, TransformerException, TServerNotAvailableException, TInsertException {

		TaminoService.insertXml(jurisprudencia.getXml());

	}
}
