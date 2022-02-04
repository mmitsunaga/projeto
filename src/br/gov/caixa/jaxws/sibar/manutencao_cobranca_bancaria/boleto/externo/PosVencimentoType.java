
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pos_vencimento_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pos_vencimento_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ACAO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PROTESTAR"/>
 *               &lt;enumeration value="DEVOLVER"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NUMERO_DIAS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999"/>
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
@XmlType(name = "pos_vencimento_Type", propOrder = {
    "acao",
    "numerodias"
})
public class PosVencimentoType {

    @XmlElement(name = "ACAO", required = true)
    protected String acao;
    @XmlElement(name = "NUMERO_DIAS")
    protected short numerodias;

    /**
     * Gets the value of the acao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACAO() {
        return acao;
    }

    /**
     * Sets the value of the acao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACAO(String value) {
        this.acao = value;
    }

    /**
     * Gets the value of the numerodias property.
     * 
     */
    public short getNUMERODIAS() {
        return numerodias;
    }

    /**
     * Sets the value of the numerodias property.
     * 
     */
    public void setNUMERODIAS(short value) {
        this.numerodias = value;
    }

}
