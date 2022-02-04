
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.jaxws.sibar.SERVICOSAIDATYPE;


/**
 * <p>Java class for SERVICO_SAIDA_NEGOCIAL_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SERVICO_SAIDA_NEGOCIAL_TYPE">
 *   &lt;complexContent>
 *     &lt;extension base="{http://caixa.gov.br/sibar}SERVICO_SAIDA_TYPE">
 *       &lt;sequence>
 *         &lt;element name="DADOS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}DADOS_SAIDA_TYPE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SERVICO_SAIDA_NEGOCIAL_TYPE", propOrder = {
    "dados"
})
public class SERVICOSAIDANEGOCIALTYPE
    extends SERVICOSAIDATYPE
{

    @XmlElement(name = "DADOS")
    protected DADOSSAIDATYPE dados;

    /**
     * Gets the value of the dados property.
     * 
     * @return
     *     possible object is
     *     {@link DADOSSAIDATYPE }
     *     
     */
    public DADOSSAIDATYPE getDADOS() {
        return dados;
    }

    /**
     * Sets the value of the dados property.
     * 
     * @param value
     *     allowed object is
     *     {@link DADOSSAIDATYPE }
     *     
     */
    public void setDADOS(DADOSSAIDATYPE value) {
        this.dados = value;
    }

}
