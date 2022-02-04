
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altera_boleto_pos_vencimento_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_pos_vencimento_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ACAO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PROTESTAR"/>
 *               &lt;enumeration value="DEVOLVER"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NUMERO_DIAS" minOccurs="0">
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
@XmlType(name = "altera_boleto_pos_vencimento_Type", propOrder = {
    "acao",
    "numerodias"
})
public class AlteraBoletoPosVencimentoType {

    @XmlElement(name = "ACAO")
    protected String acao;
    @XmlElement(name = "NUMERO_DIAS")
    protected Short numerodias;

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
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getNUMERODIAS() {
        return numerodias;
    }

    /**
     * Sets the value of the numerodias property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setNUMERODIAS(Short value) {
        this.numerodias = value;
    }

}
