
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for endereco_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="endereco_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LOGRADOURO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BAIRRO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CIDADE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UF">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CEP">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99999999"/>
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
@XmlType(name = "endereco_Type", propOrder = {
    "logradouro",
    "bairro",
    "cidade",
    "uf",
    "cep"
})
public class EnderecoType {

    @XmlElement(name = "LOGRADOURO", required = true)
    protected String logradouro;
    @XmlElement(name = "BAIRRO", required = true)
    protected String bairro;
    @XmlElement(name = "CIDADE", required = true)
    protected String cidade;
    @XmlElement(name = "UF", required = true)
    protected String uf;
    @XmlElement(name = "CEP")
    protected int cep;

    /**
     * Gets the value of the logradouro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOGRADOURO() {
        return logradouro;
    }

    /**
     * Sets the value of the logradouro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOGRADOURO(String value) {
        this.logradouro = value;
    }

    /**
     * Gets the value of the bairro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBAIRRO() {
        return bairro;
    }

    /**
     * Sets the value of the bairro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBAIRRO(String value) {
        this.bairro = value;
    }

    /**
     * Gets the value of the cidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCIDADE() {
        return cidade;
    }

    /**
     * Sets the value of the cidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCIDADE(String value) {
        this.cidade = value;
    }

    /**
     * Gets the value of the uf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUF() {
        return uf;
    }

    /**
     * Sets the value of the uf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUF(String value) {
        this.uf = value;
    }

    /**
     * Gets the value of the cep property.
     * 
     */
    public int getCEP() {
        return cep;
    }

    /**
     * Sets the value of the cep property.
     * 
     */
    public void setCEP(int value) {
        this.cep = value;
    }

}
