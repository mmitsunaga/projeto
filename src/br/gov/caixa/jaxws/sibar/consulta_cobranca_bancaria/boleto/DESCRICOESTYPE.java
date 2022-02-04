
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DESCRICOES_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DESCRICOES_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="REGISTRO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OBTER_ENVIO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ENVIO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OBTER_VALOR">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PROTESTO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ESPECIE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ACEITE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ENDOSSO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
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
@XmlType(name = "DESCRICOES_TYPE", propOrder = {
    "registro",
    "obterenvio",
    "envio",
    "obtervalor",
    "protesto",
    "especie",
    "aceite",
    "endosso"
})
public class DESCRICOESTYPE {

    @XmlElement(name = "REGISTRO", required = true)
    protected String registro;
    @XmlElement(name = "OBTER_ENVIO", required = true)
    protected String obterenvio;
    @XmlElement(name = "ENVIO", required = true)
    protected String envio;
    @XmlElement(name = "OBTER_VALOR", required = true)
    protected String obtervalor;
    @XmlElement(name = "PROTESTO", required = true)
    protected String protesto;
    @XmlElement(name = "ESPECIE", required = true)
    protected String especie;
    @XmlElement(name = "ACEITE", required = true)
    protected String aceite;
    @XmlElement(name = "ENDOSSO", required = true)
    protected String endosso;

    /**
     * Gets the value of the registro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGISTRO() {
        return registro;
    }

    /**
     * Sets the value of the registro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGISTRO(String value) {
        this.registro = value;
    }

    /**
     * Gets the value of the obterenvio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOBTERENVIO() {
        return obterenvio;
    }

    /**
     * Sets the value of the obterenvio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOBTERENVIO(String value) {
        this.obterenvio = value;
    }

    /**
     * Gets the value of the envio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENVIO() {
        return envio;
    }

    /**
     * Sets the value of the envio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENVIO(String value) {
        this.envio = value;
    }

    /**
     * Gets the value of the obtervalor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOBTERVALOR() {
        return obtervalor;
    }

    /**
     * Sets the value of the obtervalor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOBTERVALOR(String value) {
        this.obtervalor = value;
    }

    /**
     * Gets the value of the protesto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROTESTO() {
        return protesto;
    }

    /**
     * Sets the value of the protesto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROTESTO(String value) {
        this.protesto = value;
    }

    /**
     * Gets the value of the especie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESPECIE() {
        return especie;
    }

    /**
     * Sets the value of the especie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESPECIE(String value) {
        this.especie = value;
    }

    /**
     * Gets the value of the aceite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACEITE() {
        return aceite;
    }

    /**
     * Sets the value of the aceite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACEITE(String value) {
        this.aceite = value;
    }

    /**
     * Gets the value of the endosso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getENDOSSO() {
        return endosso;
    }

    /**
     * Sets the value of the endosso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setENDOSSO(String value) {
        this.endosso = value;
    }

}
