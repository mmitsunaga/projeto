
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for juros_mora_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="juros_mora_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TIPO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="VALOR_POR_DIA"/>
 *               &lt;enumeration value="TAXA_MENSAL"/>
 *               &lt;enumeration value="ISENTO"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="VALOR" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type"/>
 *           &lt;element name="PERCENTUAL" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type"/>
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
@XmlType(name = "juros_mora_Type", propOrder = {
    "tipo",
    "data",
    "valor",
    "percentual"
})
public class JurosMoraType {

    @XmlElement(name = "TIPO", required = true)
    protected String tipo;
    @XmlElement(name = "DATA")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar data;
    @XmlElement(name = "VALOR")
    protected BigDecimal valor;
    @XmlElement(name = "PERCENTUAL")
    protected BigDecimal percentual;

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPO() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPO(String value) {
        this.tipo = value;
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
