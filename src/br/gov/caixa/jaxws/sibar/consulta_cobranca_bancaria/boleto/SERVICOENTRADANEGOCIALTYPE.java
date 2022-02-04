
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.jaxws.sibar.SERVICOENTRADATYPE;


/**
 * <p>Java class for SERVICO_ENTRADA_NEGOCIAL_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SERVICO_ENTRADA_NEGOCIAL_TYPE">
 *   &lt;complexContent>
 *     &lt;extension base="{http://caixa.gov.br/sibar}SERVICO_ENTRADA_TYPE">
 *       &lt;sequence>
 *         &lt;element name="DADOS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}DADOS_ENTRADA_TYPE"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SERVICO_ENTRADA_NEGOCIAL_TYPE", propOrder = {
    "dados"
})
public class SERVICOENTRADANEGOCIALTYPE
    extends SERVICOENTRADATYPE
{

    @XmlElement(name = "DADOS", required = true)
    protected DADOSENTRADATYPE dados;

    /**
     * Gets the value of the dados property.
     * 
     * @return
     *     possible object is
     *     {@link DADOSENTRADATYPE }
     *     
     */
    public DADOSENTRADATYPE getDADOS() {
        return dados;
    }

    /**
     * Sets the value of the dados property.
     * 
     * @param value
     *     allowed object is
     *     {@link DADOSENTRADATYPE }
     *     
     */
    public void setDADOS(DADOSENTRADATYPE value) {
        this.dados = value;
    }

}
