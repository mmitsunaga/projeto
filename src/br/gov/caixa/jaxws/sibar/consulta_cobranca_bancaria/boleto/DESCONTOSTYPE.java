
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DESCONTOS_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DESCONTOS_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DESCONTO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}DESCONTO_MULTA_TYPE" maxOccurs="3"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DESCONTOS_TYPE", propOrder = {
    "desconto"
})
public class DESCONTOSTYPE {

    @XmlElement(name = "DESCONTO", required = true)
    protected List<DESCONTOMULTATYPE> desconto;

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
     * {@link DESCONTOMULTATYPE }
     * 
     * 
     */
    public List<DESCONTOMULTATYPE> getDESCONTO() {
        if (desconto == null) {
            desconto = new ArrayList<DESCONTOMULTATYPE>();
        }
        return this.desconto;
    }

}
