
package br.gov.caixa.jaxws.sibar;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.gov.caixa.axis.sibar package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _HEADER_QNAME = new QName("http://caixa.gov.br/sibar", "HEADER");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.gov.caixa.axis.sibar
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MensagensControleNegocialType }
     * 
     */
    public MensagensControleNegocialType createMensagensControleNegocialType() {
        return new MensagensControleNegocialType();
    }

    /**
     * Create an instance of {@link SERVICOENTRADATYPE }
     * 
     */
    public SERVICOENTRADATYPE createSERVICOENTRADATYPE() {
        return new SERVICOENTRADATYPE();
    }

    /**
     * Create an instance of {@link DADOSSAIDATYPE }
     * 
     */
    public DADOSSAIDATYPE createDADOSSAIDATYPE() {
        return new DADOSSAIDATYPE();
    }

    /**
     * Create an instance of {@link HEADERBARRAMENTOTYPE }
     * 
     */
    public HEADERBARRAMENTOTYPE createHEADERBARRAMENTOTYPE() {
        return new HEADERBARRAMENTOTYPE();
    }

    /**
     * Create an instance of {@link CONTROLENEGOCIALTYPE }
     * 
     */
    public CONTROLENEGOCIALTYPE createCONTROLENEGOCIALTYPE() {
        return new CONTROLENEGOCIALTYPE();
    }

    /**
     * Create an instance of {@link SERVICOSAIDATYPE }
     * 
     */
    public SERVICOSAIDATYPE createSERVICOSAIDATYPE() {
        return new SERVICOSAIDATYPE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HEADERBARRAMENTOTYPE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://caixa.gov.br/sibar", name = "HEADER")
    public JAXBElement<HEADERBARRAMENTOTYPE> createHEADER(HEADERBARRAMENTOTYPE value) {
        return new JAXBElement<HEADERBARRAMENTOTYPE>(_HEADER_QNAME, HEADERBARRAMENTOTYPE.class, null, value);
    }

}
