
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consulta_boleto_pagador_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consulta_boleto_pagador_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="CPF" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}cpf_Type"/>
 *             &lt;element name="NOME">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;maxLength value="40"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="CNPJ" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}cnpj_Type"/>
 *             &lt;element name="RAZAO_SOCIAL">
 *               &lt;simpleType>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                   &lt;maxLength value="40"/>
 *                 &lt;/restriction>
 *               &lt;/simpleType>
 *             &lt;/element>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="ENDERECO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}consulta_boleto_endereco_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consulta_boleto_pagador_Type", propOrder = {
    "cpf",
    "nome",
    "cnpj",
    "razaosocial",
    "endereco"
})
public class ConsultaBoletoPagadorType {

    @XmlElement(name = "CPF")
    protected Long cpf;
    @XmlElement(name = "NOME")
    protected String nome;
    @XmlElement(name = "CNPJ")
    protected Long cnpj;
    @XmlElement(name = "RAZAO_SOCIAL")
    protected String razaosocial;
    @XmlElement(name = "ENDERECO")
    protected ConsultaBoletoEnderecoType endereco;

    /**
     * Gets the value of the cpf property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCPF() {
        return cpf;
    }

    /**
     * Sets the value of the cpf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCPF(Long value) {
        this.cpf = value;
    }

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
     * Gets the value of the cnpj property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCNPJ() {
        return cnpj;
    }

    /**
     * Sets the value of the cnpj property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCNPJ(Long value) {
        this.cnpj = value;
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
     *     {@link ConsultaBoletoEnderecoType }
     *     
     */
    public ConsultaBoletoEnderecoType getENDERECO() {
        return endereco;
    }

    /**
     * Sets the value of the endereco property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaBoletoEnderecoType }
     *     
     */
    public void setENDERECO(ConsultaBoletoEnderecoType value) {
        this.endereco = value;
    }

}
