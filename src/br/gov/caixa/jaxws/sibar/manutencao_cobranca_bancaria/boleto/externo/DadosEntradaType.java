
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dados_entrada_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dados_entrada_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="INCLUI_BOLETO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}inclui_boleto_entrada_Type"/>
 *         &lt;element name="BAIXA_BOLETO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}baixa_boleto_entrada_Type"/>
 *         &lt;element name="ALTERA_BOLETO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}altera_boleto_entrada_Type"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dados_entrada_Type", propOrder = {
    "incluiboleto",
    "baixaboleto",
    "alteraboleto"
})
public class DadosEntradaType {

    @XmlElement(name = "INCLUI_BOLETO")
    protected IncluiBoletoEntradaType incluiboleto;
    @XmlElement(name = "BAIXA_BOLETO")
    protected BaixaBoletoEntradaType baixaboleto;
    @XmlElement(name = "ALTERA_BOLETO")
    protected AlteraBoletoEntradaType alteraboleto;

    /**
     * Gets the value of the incluiboleto property.
     * 
     * @return
     *     possible object is
     *     {@link IncluiBoletoEntradaType }
     *     
     */
    public IncluiBoletoEntradaType getINCLUIBOLETO() {
        return incluiboleto;
    }

    /**
     * Sets the value of the incluiboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncluiBoletoEntradaType }
     *     
     */
    public void setINCLUIBOLETO(IncluiBoletoEntradaType value) {
        this.incluiboleto = value;
    }

    /**
     * Gets the value of the baixaboleto property.
     * 
     * @return
     *     possible object is
     *     {@link BaixaBoletoEntradaType }
     *     
     */
    public BaixaBoletoEntradaType getBAIXABOLETO() {
        return baixaboleto;
    }

    /**
     * Sets the value of the baixaboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaixaBoletoEntradaType }
     *     
     */
    public void setBAIXABOLETO(BaixaBoletoEntradaType value) {
        this.baixaboleto = value;
    }

    /**
     * Gets the value of the alteraboleto property.
     * 
     * @return
     *     possible object is
     *     {@link AlteraBoletoEntradaType }
     *     
     */
    public AlteraBoletoEntradaType getALTERABOLETO() {
        return alteraboleto;
    }

    /**
     * Sets the value of the alteraboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlteraBoletoEntradaType }
     *     
     */
    public void setALTERABOLETO(AlteraBoletoEntradaType value) {
        this.alteraboleto = value;
    }

}
