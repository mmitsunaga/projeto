
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.gov.caixa.jaxws.sibar.SERVICOENTRADATYPE;


/**
 * <p>Java class for servico_entrada_negocial_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="servico_entrada_negocial_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://caixa.gov.br/sibar}SERVICO_ENTRADA_TYPE">
 *       &lt;sequence>
 *         &lt;element name="DADOS" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}dados_entrada_Type"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "servico_entrada_negocial_Type", propOrder = {
    "dados"
})
public class ServicoEntradaNegocialType
    extends SERVICOENTRADATYPE
{

    @XmlElement(name = "DADOS", required = true)
    protected DadosEntradaType dados;

    /**
     * Gets the value of the dados property.
     * 
     * @return
     *     possible object is
     *     {@link DadosEntradaType }
     *     
     */
    public DadosEntradaType getDADOS() {
        return dados;
    }

    /**
     * Sets the value of the dados property.
     * 
     * @param value
     *     allowed object is
     *     {@link DadosEntradaType }
     *     
     */
    public void setDADOS(DadosEntradaType value) {
        this.dados = value;
    }

}
