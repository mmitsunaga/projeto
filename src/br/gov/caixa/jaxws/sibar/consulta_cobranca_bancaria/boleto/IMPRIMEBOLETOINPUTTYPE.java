
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IMPRIME_BOLETO_INPUT_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IMPRIME_BOLETO_INPUT_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="CODIGO_BENEFICIARIO">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                   &lt;minInclusive value="0"/>
 *                   &lt;maxInclusive value="999999"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *             &lt;element name="NOSSO_NUMERO">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *                   &lt;minInclusive value="0"/>
 *                   &lt;maxInclusive value="99999999999999999"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *           &lt;/sequence>
 *           &lt;element name="LINHA_DIGITAVEL">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;length value="47"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IMPRIME_BOLETO_INPUT_TYPE", propOrder = {
    "codigobeneficiario",
    "nossonumero",
    "linhadigitavel"
})
public class IMPRIMEBOLETOINPUTTYPE {

    @XmlElement(name = "CODIGO_BENEFICIARIO")
    protected Integer codigobeneficiario;
    @XmlElement(name = "NOSSO_NUMERO")
    protected Long nossonumero;
    @XmlElement(name = "LINHA_DIGITAVEL")
    protected String linhadigitavel;

    /**
     * Gets the value of the codigobeneficiario property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCODIGOBENEFICIARIO() {
        return codigobeneficiario;
    }

    /**
     * Sets the value of the codigobeneficiario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCODIGOBENEFICIARIO(Integer value) {
        this.codigobeneficiario = value;
    }

    /**
     * Gets the value of the nossonumero property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNOSSONUMERO() {
        return nossonumero;
    }

    /**
     * Sets the value of the nossonumero property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNOSSONUMERO(Long value) {
        this.nossonumero = value;
    }

    /**
     * Gets the value of the linhadigitavel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLINHADIGITAVEL() {
        return linhadigitavel;
    }

    /**
     * Sets the value of the linhadigitavel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLINHADIGITAVEL(String value) {
        this.linhadigitavel = value;
    }

}
