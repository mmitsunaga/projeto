
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ficha_compensacao_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ficha_compensacao_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MENSAGENS" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}mensagens_ficha_compensacao_Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ficha_compensacao_Type", propOrder = {
    "mensagens"
})
public class FichaCompensacaoType {

    @XmlElement(name = "MENSAGENS", required = true)
    protected MensagensFichaCompensacaoType mensagens;

    /**
     * Gets the value of the mensagens property.
     * 
     * @return
     *     possible object is
     *     {@link MensagensFichaCompensacaoType }
     *     
     */
    public MensagensFichaCompensacaoType getMENSAGENS() {
        return mensagens;
    }

    /**
     * Sets the value of the mensagens property.
     * 
     * @param value
     *     allowed object is
     *     {@link MensagensFichaCompensacaoType }
     *     
     */
    public void setMENSAGENS(MensagensFichaCompensacaoType value) {
        this.mensagens = value;
    }

}
