
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.jaxws.sibar.DADOSSAIDATYPE;


/**
 * <p>Java class for dados_saida_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dados_saida_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://caixa.gov.br/sibar}DADOS_SAIDA_TYPE">
 *       &lt;choice>
 *         &lt;element name="INCLUI_BOLETO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}inclui_boleto_saida_Type"/>
 *         &lt;element name="ALTERA_BOLETO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}altera_boleto_saida_Type"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dados_saida_Type", propOrder = {
    "incluiboleto",
    "alteraboleto"
})
public class DadosSaidaType
    extends DADOSSAIDATYPE
{

    @XmlElement(name = "INCLUI_BOLETO")
    protected IncluiBoletoSaidaType incluiboleto;
    @XmlElement(name = "ALTERA_BOLETO")
    protected AlteraBoletoSaidaType alteraboleto;

    /**
     * Gets the value of the incluiboleto property.
     * 
     * @return
     *     possible object is
     *     {@link IncluiBoletoSaidaType }
     *     
     */
    public IncluiBoletoSaidaType getINCLUIBOLETO() {
        return incluiboleto;
    }

    /**
     * Sets the value of the incluiboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncluiBoletoSaidaType }
     *     
     */
    public void setINCLUIBOLETO(IncluiBoletoSaidaType value) {
        this.incluiboleto = value;
    }

    /**
     * Gets the value of the alteraboleto property.
     * 
     * @return
     *     possible object is
     *     {@link AlteraBoletoSaidaType }
     *     
     */
    public AlteraBoletoSaidaType getALTERABOLETO() {
        return alteraboleto;
    }

    /**
     * Sets the value of the alteraboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlteraBoletoSaidaType }
     *     
     */
    public void setALTERABOLETO(AlteraBoletoSaidaType value) {
        this.alteraboleto = value;
    }

}
