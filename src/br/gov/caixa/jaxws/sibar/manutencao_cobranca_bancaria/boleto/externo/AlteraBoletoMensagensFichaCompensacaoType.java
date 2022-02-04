
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altera_boleto_mensagens_ficha_compensacao_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_mensagens_ficha_compensacao_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MENSAGEM" maxOccurs="2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
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
@XmlType(name = "altera_boleto_mensagens_ficha_compensacao_Type", propOrder = {
    "mensagem"
})
public class AlteraBoletoMensagensFichaCompensacaoType {

    @XmlElement(name = "MENSAGEM", required = true)
    protected List<String> mensagem;

    /**
     * Gets the value of the mensagem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensagem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMENSAGEM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMENSAGEM() {
        if (mensagem == null) {
            mensagem = new ArrayList<String>();
        }
        return this.mensagem;
    }

}
