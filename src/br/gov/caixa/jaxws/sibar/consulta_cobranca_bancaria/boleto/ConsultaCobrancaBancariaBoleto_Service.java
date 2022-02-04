
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "Consulta_Cobranca_Bancaria_Boleto", targetNamespace = "http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto", wsdlLocation = "https://barramento.caixa.gov.br/sibar/ConsultaCobrancaBancaria/BoletoDt?wsdl")
public class ConsultaCobrancaBancariaBoleto_Service
    extends Service
{

    private final static URL CONSULTACOBRANCABANCARIABOLETO_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.ConsultaCobrancaBancariaBoleto_Service.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto.ConsultaCobrancaBancariaBoleto_Service.class.getResource(".");
            url = new URL(baseUrl, "https://barramento.caixa.gov.br/sibar/ConsultaCobrancaBancaria/Boleto?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'https://barramento.caixa.gov.br/sibar/ConsultaCobrancaBancaria/Boleto?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        CONSULTACOBRANCABANCARIABOLETO_WSDL_LOCATION = url;
    }

    public ConsultaCobrancaBancariaBoleto_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ConsultaCobrancaBancariaBoleto_Service() {
        super(CONSULTACOBRANCABANCARIABOLETO_WSDL_LOCATION, new QName("http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto", "Consulta_Cobranca_Bancaria_Boleto"));
    }

    /**
     * 
     * @return
     *     returns ConsultaCobrancaBancariaBoleto
     */
    @WebEndpoint(name = "Consulta_Cobranca_Bancaria_BoletoSOAP")
    public ConsultaCobrancaBancariaBoleto getConsultaCobrancaBancariaBoletoSOAP() {
        return super.getPort(new QName("http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto", "Consulta_Cobranca_Bancaria_BoletoSOAP"), ConsultaCobrancaBancariaBoleto.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ConsultaCobrancaBancariaBoleto
     */
    @WebEndpoint(name = "Consulta_Cobranca_Bancaria_BoletoSOAP")
    public ConsultaCobrancaBancariaBoleto getConsultaCobrancaBancariaBoletoSOAP(WebServiceFeature... features) {
        return super.getPort(new QName("http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto", "Consulta_Cobranca_Bancaria_BoletoSOAP"), ConsultaCobrancaBancariaBoleto.class, features);
    }

}