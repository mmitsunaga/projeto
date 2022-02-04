
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altera_boleto_saida_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_saida_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CODIGO_BARRAS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="44"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LINHA_DIGITAVEL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="47"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="URL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altera_boleto_saida_Type", propOrder = {
    "codigobarras",
    "linhadigitavel",
    "url"
})
public class AlteraBoletoSaidaType {

    @XmlElement(name = "CODIGO_BARRAS")
    protected String codigobarras;
    @XmlElement(name = "LINHA_DIGITAVEL")
    protected String linhadigitavel;
    @XmlElement(name = "URL")
    protected String url;

    /**
     * Gets the value of the codigobarras property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODIGOBARRAS() {
        return codigobarras;
    }

    /**
     * Sets the value of the codigobarras property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODIGOBARRAS(String value) {
        this.codigobarras = value;
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

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURL(String value) {
        this.url = value;
    }

}
