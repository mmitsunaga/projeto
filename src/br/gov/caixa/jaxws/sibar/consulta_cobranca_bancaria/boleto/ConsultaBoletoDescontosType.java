
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consulta_boleto_descontos_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consulta_boleto_descontos_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DESCONTO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}consulta_boleto_desconto_Type" maxOccurs="3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consulta_boleto_descontos_Type", propOrder = {
    "desconto"
})
public class ConsultaBoletoDescontosType {

    @XmlElement(name = "DESCONTO", required = true)
    protected List<ConsultaBoletoDescontoType> desconto;

    /**
     * Gets the value of the desconto property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the desconto property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDESCONTO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConsultaBoletoDescontoType }
     * 
     * 
     */
    public List<ConsultaBoletoDescontoType> getDESCONTO() {
        if (desconto == null) {
            desconto = new ArrayList<ConsultaBoletoDescontoType>();
        }
        return this.desconto;
    }

}
