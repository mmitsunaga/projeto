
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consulta_boleto_recibo_pagador_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consulta_boleto_recibo_pagador_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MENSAGENS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}consulta_boleto_mensagens_recibo_pagador_Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consulta_boleto_recibo_pagador_Type", propOrder = {
    "mensagens"
})
public class ConsultaBoletoReciboPagadorType {

    @XmlElement(name = "MENSAGENS", required = true)
    protected ConsultaBoletoMensagensReciboPagadorType mensagens;

    /**
     * Gets the value of the mensagens property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaBoletoMensagensReciboPagadorType }
     *     
     */
    public ConsultaBoletoMensagensReciboPagadorType getMENSAGENS() {
        return mensagens;
    }

    /**
     * Sets the value of the mensagens property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaBoletoMensagensReciboPagadorType }
     *     
     */
    public void setMENSAGENS(ConsultaBoletoMensagensReciboPagadorType value) {
        this.mensagens = value;
    }

}
