
package br.gov.caixa.jaxws.sibar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo.ServicoSaidaNegocialType;


/**
 * <p>Java class for SERVICO_SAIDA_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SERVICO_SAIDA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://caixa.gov.br/sibar}HEADER"/>
 *         &lt;element name="COD_RETORNO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORIGEM_RETORNO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MSG_RETORNO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
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
@XmlType(name = "SERVICO_SAIDA_TYPE", propOrder = {
    "header",
    "codretorno",
    "origemretorno",
    "msgretorno"
})
@XmlSeeAlso({
    ServicoSaidaNegocialType.class
})
public class SERVICOSAIDATYPE {

    @XmlElement(name = "HEADER", namespace = "http://caixa.gov.br/sibar", required = true)
    protected HEADERBARRAMENTOTYPE header;
    @XmlElement(name = "COD_RETORNO", required = true)
    protected String codretorno;
    @XmlElement(name = "ORIGEM_RETORNO", required = true)
    protected String origemretorno;
    @XmlElement(name = "MSG_RETORNO", required = true)
    protected String msgretorno;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HEADERBARRAMENTOTYPE }
     *     
     */
    public HEADERBARRAMENTOTYPE getHEADER() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HEADERBARRAMENTOTYPE }
     *     
     */
    public void setHEADER(HEADERBARRAMENTOTYPE value) {
        this.header = value;
    }

    /**
     * Gets the value of the codretorno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODRETORNO() {
        return codretorno;
    }

    /**
     * Sets the value of the codretorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODRETORNO(String value) {
        this.codretorno = value;
    }

    /**
     * Gets the value of the origemretorno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORIGEMRETORNO() {
        return origemretorno;
    }

    /**
     * Sets the value of the origemretorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORIGEMRETORNO(String value) {
        this.origemretorno = value;
    }

    /**
     * Gets the value of the msgretorno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSGRETORNO() {
        return msgretorno;
    }

    /**
     * Sets the value of the msgretorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSGRETORNO(String value) {
        this.msgretorno = value;
    }

}
