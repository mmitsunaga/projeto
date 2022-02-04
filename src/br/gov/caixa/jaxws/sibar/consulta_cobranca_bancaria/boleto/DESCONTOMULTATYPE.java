
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DESCONTO_MULTA_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DESCONTO_MULTA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="PERCENTUAL">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                 &lt;minInclusive value="0"/>
 *                 &lt;maxInclusive value="999.99"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="VALOR">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                 &lt;minInclusive value="0"/>
 *                 &lt;maxInclusive value="9999999999999.99"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="PRAZO">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *                 &lt;minInclusive value="0"/>
 *                 &lt;maxInclusive value="999"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="DATA" type="{http://www.w3.org/2001/XMLSchema}date"/>
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
@XmlType(name = "DESCONTO_MULTA_TYPE", propOrder = {
    "percentual",
    "valor",
    "prazo",
    "data"
})
public class DESCONTOMULTATYPE {

    @XmlElement(name = "PERCENTUAL")
    protected BigDecimal percentual;
    @XmlElement(name = "VALOR")
    protected BigDecimal valor;
    @XmlElement(name = "PRAZO")
    protected Short prazo;
    @XmlElement(name = "DATA")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar data;

    /**
     * Gets the value of the percentual property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPERCENTUAL() {
        return percentual;
    }

    /**
     * Sets the value of the percentual property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPERCENTUAL(BigDecimal value) {
        this.percentual = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALOR() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALOR(BigDecimal value) {
        this.valor = value;
    }

    /**
     * Gets the value of the prazo property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getPRAZO() {
        return prazo;
    }

    /**
     * Sets the value of the prazo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setPRAZO(Short value) {
        this.prazo = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATA() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATA(XMLGregorianCalendar value) {
        this.data = value;
    }

}
