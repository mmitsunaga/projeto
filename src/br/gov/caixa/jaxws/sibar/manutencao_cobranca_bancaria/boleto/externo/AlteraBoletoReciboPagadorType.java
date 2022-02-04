
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altera_boleto_recibo_pagador_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_recibo_pagador_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MENSAGENS" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}altera_boleto_mensagens_recibo_pagador_Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altera_boleto_recibo_pagador_Type", propOrder = {
    "mensagens"
})
public class AlteraBoletoReciboPagadorType {

    @XmlElement(name = "MENSAGENS", required = true)
    protected AlteraBoletoMensagensReciboPagadorType mensagens;

    /**
     * Gets the value of the mensagens property.
     * 
     * @return
     *     possible object is
     *     {@link AlteraBoletoMensagensReciboPagadorType }
     *     
     */
    public AlteraBoletoMensagensReciboPagadorType getMENSAGENS() {
        return mensagens;
    }

    /**
     * Sets the value of the mensagens property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlteraBoletoMensagensReciboPagadorType }
     *     
     */
    public void setMENSAGENS(AlteraBoletoMensagensReciboPagadorType value) {
        this.mensagens = value;
    }

}
