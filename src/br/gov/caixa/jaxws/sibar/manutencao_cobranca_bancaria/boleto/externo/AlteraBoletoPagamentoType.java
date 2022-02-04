
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altera_boleto_pagamento_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altera_boleto_pagamento_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QUANTIDADE_PERMITIDA" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TIPO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACEITA_QUALQUER_VALOR"/>
 *               &lt;enumeration value="ACEITA_VALORES_ENTRE_MINIMO_MAXIMO"/>
 *               &lt;enumeration value="NAO_ACEITA_VALOR_DIVERGENTE"/>
 *               &lt;enumeration value="SOMENTE_VALOR_MINIMO"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="VALOR_MINIMO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type" minOccurs="0"/>
 *             &lt;element name="VALOR_MAXIMO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="PERCENTUAL_MINIMO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}percentual_Type" minOccurs="0"/>
 *             &lt;element name="PERCENTUAL_MAXIMO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}percentual_Type" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altera_boleto_pagamento_Type", propOrder = {
    "quantidadepermitida",
    "tipo",
    "valorminimo",
    "valormaximo",
    "percentualminimo",
    "percentualmaximo"
})
public class AlteraBoletoPagamentoType {

    @XmlElement(name = "QUANTIDADE_PERMITIDA")
    protected Short quantidadepermitida;
    @XmlElement(name = "TIPO")
    protected String tipo;
    @XmlElement(name = "VALOR_MINIMO")
    protected BigDecimal valorminimo;
    @XmlElement(name = "VALOR_MAXIMO")
    protected BigDecimal valormaximo;
    @XmlElement(name = "PERCENTUAL_MINIMO")
    protected BigDecimal percentualminimo;
    @XmlElement(name = "PERCENTUAL_MAXIMO")
    protected BigDecimal percentualmaximo;

    /**
     * Gets the value of the quantidadepermitida property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getQUANTIDADEPERMITIDA() {
        return quantidadepermitida;
    }

    /**
     * Sets the value of the quantidadepermitida property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setQUANTIDADEPERMITIDA(Short value) {
        this.quantidadepermitida = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPO() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPO(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the valorminimo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALORMINIMO() {
        return valorminimo;
    }

    /**
     * Sets the value of the valorminimo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALORMINIMO(BigDecimal value) {
        this.valorminimo = value;
    }

    /**
     * Gets the value of the valormaximo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALORMAXIMO() {
        return valormaximo;
    }

    /**
     * Sets the value of the valormaximo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALORMAXIMO(BigDecimal value) {
        this.valormaximo = value;
    }

    /**
     * Gets the value of the percentualminimo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPERCENTUALMINIMO() {
        return percentualminimo;
    }

    /**
     * Sets the value of the percentualminimo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPERCENTUALMINIMO(BigDecimal value) {
        this.percentualminimo = value;
    }

    /**
     * Gets the value of the percentualmaximo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPERCENTUALMAXIMO() {
        return percentualmaximo;
    }

    /**
     * Sets the value of the percentualmaximo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPERCENTUALMAXIMO(BigDecimal value) {
        this.percentualmaximo = value;
    }

}
