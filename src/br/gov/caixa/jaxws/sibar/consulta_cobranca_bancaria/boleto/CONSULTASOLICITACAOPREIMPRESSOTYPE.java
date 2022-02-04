
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CONSULTA_SOLICITACAO_PRE_IMPRESSO_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONSULTA_SOLICITACAO_PRE_IMPRESSO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NOSSO_NUMERO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999999999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TIPO_ENTREGA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="QUANTIDADE_BLOQUETO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TIPO_COBRANCA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SITUACAO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CONSULTA_SOLICITACAO_PRE_IMPRESSO_TYPE", propOrder = {
    "nossonumero",
    "tipoentrega",
    "quantidadebloqueto",
    "tipocobranca",
    "situacao"
})
public class CONSULTASOLICITACAOPREIMPRESSOTYPE {

    @XmlElement(name = "NOSSO_NUMERO")
    protected long nossonumero;
    @XmlElement(name = "TIPO_ENTREGA")
    protected short tipoentrega;
    @XmlElement(name = "QUANTIDADE_BLOQUETO")
    protected int quantidadebloqueto;
    @XmlElement(name = "TIPO_COBRANCA", required = true)
    protected String tipocobranca;
    @XmlElement(name = "SITUACAO", required = true)
    protected String situacao;

    /**
     * Gets the value of the nossonumero property.
     * 
     */
    public long getNOSSONUMERO() {
        return nossonumero;
    }

    /**
     * Sets the value of the nossonumero property.
     * 
     */
    public void setNOSSONUMERO(long value) {
        this.nossonumero = value;
    }

    /**
     * Gets the value of the tipoentrega property.
     * 
     */
    public short getTIPOENTREGA() {
        return tipoentrega;
    }

    /**
     * Sets the value of the tipoentrega property.
     * 
     */
    public void setTIPOENTREGA(short value) {
        this.tipoentrega = value;
    }

    /**
     * Gets the value of the quantidadebloqueto property.
     * 
     */
    public int getQUANTIDADEBLOQUETO() {
        return quantidadebloqueto;
    }

    /**
     * Sets the value of the quantidadebloqueto property.
     * 
     */
    public void setQUANTIDADEBLOQUETO(int value) {
        this.quantidadebloqueto = value;
    }

    /**
     * Gets the value of the tipocobranca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPOCOBRANCA() {
        return tipocobranca;
    }

    /**
     * Sets the value of the tipocobranca property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPOCOBRANCA(String value) {
        this.tipocobranca = value;
    }

    /**
     * Gets the value of the situacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSITUACAO() {
        return situacao;
    }

    /**
     * Sets the value of the situacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSITUACAO(String value) {
        this.situacao = value;
    }

}
