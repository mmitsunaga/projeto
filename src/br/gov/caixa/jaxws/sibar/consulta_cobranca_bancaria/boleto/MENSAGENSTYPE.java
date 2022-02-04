
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MENSAGENS_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MENSAGENS_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MENSAGEM" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}MENSAGEM_TYPE" maxOccurs="20" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MENSAGENS_TYPE", propOrder = {
    "mensagem"
})
public class MENSAGENSTYPE {

    @XmlElement(name = "MENSAGEM")
    protected List<MENSAGEMTYPE> mensagem;

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
     * {@link MENSAGEMTYPE }
     * 
     * 
     */
    public List<MENSAGEMTYPE> getMENSAGEM() {
        if (mensagem == null) {
            mensagem = new ArrayList<MENSAGEMTYPE>();
        }
        return this.mensagem;
    }

}
