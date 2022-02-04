
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.jaxws.sibar.SERVICOSAIDATYPE;


/**
 * <p>Java class for servico_saida_negocial_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="servico_saida_negocial_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://caixa.gov.br/sibar}SERVICO_SAIDA_TYPE">
 *       &lt;sequence>
 *         &lt;element name="DADOS" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}dados_saida_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "servico_saida_negocial_Type", propOrder = {
    "dados"
})
public class ServicoSaidaNegocialType
    extends SERVICOSAIDATYPE
{

    @XmlElement(name = "DADOS")
    protected DadosSaidaType dados;

    /**
     * Gets the value of the dados property.
     * 
     * @return
     *     possible object is
     *     {@link DadosSaidaType }
     *     
     */
    public DadosSaidaType getDADOS() {
        return dados;
    }

    /**
     * Sets the value of the dados property.
     * 
     * @param value
     *     allowed object is
     *     {@link DadosSaidaType }
     *     
     */
    public void setDADOS(DadosSaidaType value) {
        this.dados = value;
    }

}
