package br.gov.go.tj.projudi.ne.boletos.caixa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ImprimeEnvelopesSoapEmDebug implements SOAPHandler<SOAPMessageContext> {
	
	private final Logger LOGGER = Logger.getLogger(ImprimeEnvelopesSoapEmDebug.class);
	private final XMLOutputter XML_OUTPUTTER = new XMLOutputter(Format.getPrettyFormat());
	
	private void imprime(SOAPMessage soapMessage) {
		ByteArrayOutputStream bytesSoapMessage = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(bytesSoapMessage);
			String envelope = new String(bytesSoapMessage.toByteArray());
			try {
				envelope = "\n" + XML_OUTPUTTER.outputString(new SAXBuilder().build(new ByteArrayInputStream(bytesSoapMessage.toByteArray())));
			} catch (Exception e) {
				LOGGER.debug("Erro ao formatar envelope SOAP.", e);
			}
			LOGGER.debug(envelope);
		} catch (Exception e) {
			LOGGER.debug("Erro ao gravar envelope SOAP.", e);
		}
	}
	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		imprime(context.getMessage());
		return true;
	}
	
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		imprime(context.getMessage());
		return true;
	}
	
	@Override
	public void close(MessageContext context) {
		// Nada pra fazer
	}
	
	@Override
	public Set<QName> getHeaders() {
		// Nada pra fazer
		return null;
	}
}
