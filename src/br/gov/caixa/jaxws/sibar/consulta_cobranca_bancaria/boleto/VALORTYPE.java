
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VALOR_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VALOR_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DESCONTO_ABATIMENTO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MULTA_JUROS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="JUROS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IOF">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MULTA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DESCONTO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ABATIMENTO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CALCULADO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="COBRADO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
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
@XmlType(name = "VALOR_TYPE", propOrder = {
    "descontoabatimento",
    "multajuros",
    "juros",
    "iof",
    "multa",
    "desconto",
    "abatimento",
    "calculado",
    "cobrado"
})
public class VALORTYPE {

    @XmlElement(name = "DESCONTO_ABATIMENTO", required = true)
    protected BigDecimal descontoabatimento;
    @XmlElement(name = "MULTA_JUROS", required = true)
    protected BigDecimal multajuros;
    @XmlElement(name = "JUROS", required = true)
    protected BigDecimal juros;
    @XmlElement(name = "IOF", required = true)
    protected BigDecimal iof;
    @XmlElement(name = "MULTA", required = true)
    protected BigDecimal multa;
    @XmlElement(name = "DESCONTO", required = true)
    protected BigDecimal desconto;
    @XmlElement(name = "ABATIMENTO", required = true)
    protected BigDecimal abatimento;
    @XmlElement(name = "CALCULADO", required = true)
    protected BigDecimal calculado;
    @XmlElement(name = "COBRADO", required = true)
    protected BigDecimal cobrado;

    /**
     * Gets the value of the descontoabatimento property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDESCONTOABATIMENTO() {
        return descontoabatimento;
    }

    /**
     * Sets the value of the descontoabatimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDESCONTOABATIMENTO(BigDecimal value) {
        this.descontoabatimento = value;
    }

    /**
     * Gets the value of the multajuros property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMULTAJUROS() {
        return multajuros;
    }

    /**
     * Sets the value of the multajuros property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMULTAJUROS(BigDecimal value) {
        this.multajuros = value;
    }

    /**
     * Gets the value of the juros property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getJUROS() {
        return juros;
    }

    /**
     * Sets the value of the juros property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setJUROS(BigDecimal value) {
        this.juros = value;
    }

    /**
     * Gets the value of the iof property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIOF() {
        return iof;
    }

    /**
     * Sets the value of the iof property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIOF(BigDecimal value) {
        this.iof = value;
    }

    /**
     * Gets the value of the multa property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMULTA() {
        return multa;
    }

    /**
     * Sets the value of the multa property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMULTA(BigDecimal value) {
        this.multa = value;
    }

    /**
     * Gets the value of the desconto property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDESCONTO() {
        return desconto;
    }

    /**
     * Sets the value of the desconto property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDESCONTO(BigDecimal value) {
        this.desconto = value;
    }

    /**
     * Gets the value of the abatimento property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getABATIMENTO() {
        return abatimento;
    }

    /**
     * Sets the value of the abatimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setABATIMENTO(BigDecimal value) {
        this.abatimento = value;
    }

    /**
     * Gets the value of the calculado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCALCULADO() {
        return calculado;
    }

    /**
     * Sets the value of the calculado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCALCULADO(BigDecimal value) {
        this.calculado = value;
    }

    /**
     * Gets the value of the cobrado property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCOBRADO() {
        return cobrado;
    }

    /**
     * Sets the value of the cobrado property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCOBRADO(BigDecimal value) {
        this.cobrado = value;
    }

}
