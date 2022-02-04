
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for COMPLEMENTARES_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COMPLEMENTARES_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DESCONTOS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}DESCONTOS_TYPE" minOccurs="0"/>
 *         &lt;element name="MULTA" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}DESCONTO_MULTA_TYPE" minOccurs="0"/>
 *         &lt;element name="JUROS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}JUROS_TYPE" minOccurs="0"/>
 *         &lt;element name="IMPOSTOS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}IMPOSTOS_TYPE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COMPLEMENTARES_TYPE", propOrder = {
    "descontos",
    "multa",
    "juros",
    "impostos"
})
public class COMPLEMENTARESTYPE {

    @XmlElement(name = "DESCONTOS")
    protected DESCONTOSTYPE descontos;
    @XmlElement(name = "MULTA")
    protected DESCONTOMULTATYPE multa;
    @XmlElement(name = "JUROS")
    protected JUROSTYPE juros;
    @XmlElement(name = "IMPOSTOS")
    protected IMPOSTOSTYPE impostos;

    /**
     * Gets the value of the descontos property.
     * 
     * @return
     *     possible object is
     *     {@link DESCONTOSTYPE }
     *     
     */
    public DESCONTOSTYPE getDESCONTOS() {
        return descontos;
    }

    /**
     * Sets the value of the descontos property.
     * 
     * @param value
     *     allowed object is
     *     {@link DESCONTOSTYPE }
     *     
     */
    public void setDESCONTOS(DESCONTOSTYPE value) {
        this.descontos = value;
    }

    /**
     * Gets the value of the multa property.
     * 
     * @return
     *     possible object is
     *     {@link DESCONTOMULTATYPE }
     *     
     */
    public DESCONTOMULTATYPE getMULTA() {
        return multa;
    }

    /**
     * Sets the value of the multa property.
     * 
     * @param value
     *     allowed object is
     *     {@link DESCONTOMULTATYPE }
     *     
     */
    public void setMULTA(DESCONTOMULTATYPE value) {
        this.multa = value;
    }

    /**
     * Gets the value of the juros property.
     * 
     * @return
     *     possible object is
     *     {@link JUROSTYPE }
     *     
     */
    public JUROSTYPE getJUROS() {
        return juros;
    }

    /**
     * Sets the value of the juros property.
     * 
     * @param value
     *     allowed object is
     *     {@link JUROSTYPE }
     *     
     */
    public void setJUROS(JUROSTYPE value) {
        this.juros = value;
    }

    /**
     * Gets the value of the impostos property.
     * 
     * @return
     *     possible object is
     *     {@link IMPOSTOSTYPE }
     *     
     */
    public IMPOSTOSTYPE getIMPOSTOS() {
        return impostos;
    }

    /**
     * Sets the value of the impostos property.
     * 
     * @param value
     *     allowed object is
     *     {@link IMPOSTOSTYPE }
     *     
     */
    public void setIMPOSTOS(IMPOSTOSTYPE value) {
        this.impostos = value;
    }

}
