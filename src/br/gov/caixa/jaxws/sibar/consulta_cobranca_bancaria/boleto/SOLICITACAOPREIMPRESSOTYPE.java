
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SOLICITACAO_PRE_IMPRESSO_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SOLICITACAO_PRE_IMPRESSO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DATA_SOLICITACAO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="QUANTIDADE_BOLETOS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NOSSO_NUMERO_INICIAL">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999999999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DESCRICAO_TIPO_ENTREGA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DESCRICAO_TIPO_BOLETO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
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
@XmlType(name = "SOLICITACAO_PRE_IMPRESSO_TYPE", propOrder = {
    "datasolicitacao",
    "quantidadeboletos",
    "nossonumeroinicial",
    "descricaotipoentrega",
    "descricaotipoboleto",
    "situacao"
})
public class SOLICITACAOPREIMPRESSOTYPE {

    @XmlElement(name = "DATA_SOLICITACAO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datasolicitacao;
    @XmlElement(name = "QUANTIDADE_BOLETOS")
    protected int quantidadeboletos;
    @XmlElement(name = "NOSSO_NUMERO_INICIAL")
    protected long nossonumeroinicial;
    @XmlElement(name = "DESCRICAO_TIPO_ENTREGA", required = true)
    protected String descricaotipoentrega;
    @XmlElement(name = "DESCRICAO_TIPO_BOLETO", required = true)
    protected String descricaotipoboleto;
    @XmlElement(name = "SITUACAO", required = true)
    protected String situacao;

    /**
     * Gets the value of the datasolicitacao property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATASOLICITACAO() {
        return datasolicitacao;
    }

    /**
     * Sets the value of the datasolicitacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATASOLICITACAO(XMLGregorianCalendar value) {
        this.datasolicitacao = value;
    }

    /**
     * Gets the value of the quantidadeboletos property.
     * 
     */
    public int getQUANTIDADEBOLETOS() {
        return quantidadeboletos;
    }

    /**
     * Sets the value of the quantidadeboletos property.
     * 
     */
    public void setQUANTIDADEBOLETOS(int value) {
        this.quantidadeboletos = value;
    }

    /**
     * Gets the value of the nossonumeroinicial property.
     * 
     */
    public long getNOSSONUMEROINICIAL() {
        return nossonumeroinicial;
    }

    /**
     * Sets the value of the nossonumeroinicial property.
     * 
     */
    public void setNOSSONUMEROINICIAL(long value) {
        this.nossonumeroinicial = value;
    }

    /**
     * Gets the value of the descricaotipoentrega property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRICAOTIPOENTREGA() {
        return descricaotipoentrega;
    }

    /**
     * Sets the value of the descricaotipoentrega property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRICAOTIPOENTREGA(String value) {
        this.descricaotipoentrega = value;
    }

    /**
     * Gets the value of the descricaotipoboleto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRICAOTIPOBOLETO() {
        return descricaotipoboleto;
    }

    /**
     * Sets the value of the descricaotipoboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRICAOTIPOBOLETO(String value) {
        this.descricaotipoboleto = value;
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
