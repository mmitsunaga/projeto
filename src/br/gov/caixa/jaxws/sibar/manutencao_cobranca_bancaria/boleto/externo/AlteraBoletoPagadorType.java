
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altera_boleto_pagador_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_pagador_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="NOME" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;maxLength value="40"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="RAZAO_SOCIAL" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;maxLength value="40"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="ENDERECO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}altera_boleto_endereco_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altera_boleto_pagador_Type", propOrder = {
    "nome",
    "razaosocial",
    "endereco"
})
public class AlteraBoletoPagadorType {

    @XmlElement(name = "NOME")
    protected String nome;
    @XmlElement(name = "RAZAO_SOCIAL")
    protected String razaosocial;
    @XmlElement(name = "ENDERECO")
    protected AlteraBoletoEnderecoType endereco;

    /**
     * Gets the value of the nome property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOME() {
        return nome;
    }

    /**
     * Sets the value of the nome property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOME(String value) {
        this.nome = value;
    }

    /**
     * Gets the value of the razaosocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRAZAOSOCIAL() {
        return razaosocial;
    }

    /**
     * Sets the value of the razaosocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRAZAOSOCIAL(String value) {
        this.razaosocial = value;
    }

    /**
     * Gets the value of the endereco property.
     * 
     * @return
     *     possible object is
     *     {@link AlteraBoletoEnderecoType }
     *     
     */
    public AlteraBoletoEnderecoType getENDERECO() {
        return endereco;
    }

    /**
     * Sets the value of the endereco property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlteraBoletoEnderecoType }
     *     
     */
    public void setENDERECO(AlteraBoletoEnderecoType value) {
        this.endereco = value;
    }

}
