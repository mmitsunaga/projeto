/**
 * ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.go.tj.pe.oab;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import org.apache.axis.AxisFault;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;

class LocalizadorCadastroOAB extends Service {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5374746518178213249L;
	private final String NOME_DO_SERVICO = "ServiceSoap";
	private String url;
	
	LocalizadorCadastroOAB(String url) {
		this.url = url;
	}
	
	CadastroOABStub getStub() throws ServiceException {
		try {
			CadastroOABStub stub = new CadastroOABStub(new URL(url), this);
			stub.setPortName(NOME_DO_SERVICO);
			SOAPHeaderElement autenticacao = new SOAPHeaderElement("http://tempuri.org/", "Authentication");
			SOAPElement chave = autenticacao.addChildElement("Key");
			chave.addTextNode("c982b6ef-cbbb-491b-a6d1-21a944ccc3f1");
			stub.setHeader(autenticacao);
			return stub;
		} catch (AxisFault e) {
			throw new ServiceException(e);
		} catch (SOAPException e) {
			throw new ServiceException(e);
		} catch (MalformedURLException e) {
			throw new ServiceException(e);
		}
	}
}
