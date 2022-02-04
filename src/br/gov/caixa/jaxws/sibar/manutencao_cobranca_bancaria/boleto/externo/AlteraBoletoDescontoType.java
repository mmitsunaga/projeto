
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for altera_boleto_desconto_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_desconto_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DATA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="VALOR" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type" minOccurs="0"/>
 *           &lt;element name="PERCENTUAL" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type" minOccurs="0"/>
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
@XmlType(name = "altera_boleto_desconto_Type", propOrder = {
    "data",
    "valor",
    "percentual"
})
public class AlteraBoletoDescontoType {

    @XmlElement(name = "DATA")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar data;
    @XmlElement(name = "VALOR")
    protected BigDecimal valor;
    @XmlElement(name = "PERCENTUAL")
    protected BigDecimal percentual;

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

}
