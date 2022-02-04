
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SOLICITACAO_GRUPO_PAGADOR_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SOLICITACAO_GRUPO_PAGADOR_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DATA_SOLICITACAO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="CODIGO_GRUPO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NOME_GRUPO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DESCRICAO_ESPECIE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA_VENCIMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="VALOR_TITULO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
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
@XmlType(name = "SOLICITACAO_GRUPO_PAGADOR_TYPE", propOrder = {
    "datasolicitacao",
    "codigogrupo",
    "nomegrupo",
    "descricaoespecie",
    "datavencimento",
    "valortitulo",
    "situacao"
})
public class SOLICITACAOGRUPOPAGADORTYPE {

    @XmlElement(name = "DATA_SOLICITACAO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datasolicitacao;
    @XmlElement(name = "CODIGO_GRUPO")
    protected short codigogrupo;
    @XmlElement(name = "NOME_GRUPO", required = true)
    protected String nomegrupo;
    @XmlElement(name = "DESCRICAO_ESPECIE", required = true)
    protected String descricaoespecie;
    @XmlElement(name = "DATA_VENCIMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datavencimento;
    @XmlElement(name = "VALOR_TITULO", required = true)
    protected BigDecimal valortitulo;
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
     * Gets the value of the codigogrupo property.
     * 
     */
    public short getCODIGOGRUPO() {
        return codigogrupo;
    }

    /**
     * Sets the value of the codigogrupo property.
     * 
     */
    public void setCODIGOGRUPO(short value) {
        this.codigogrupo = value;
    }

    /**
     * Gets the value of the nomegrupo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOMEGRUPO() {
        return nomegrupo;
    }

    /**
     * Sets the value of the nomegrupo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOMEGRUPO(String value) {
        this.nomegrupo = value;
    }

    /**
     * Gets the value of the descricaoespecie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRICAOESPECIE() {
        return descricaoespecie;
    }

    /**
     * Sets the value of the descricaoespecie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRICAOESPECIE(String value) {
        this.descricaoespecie = value;
    }

    /**
     * Gets the value of the datavencimento property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAVENCIMENTO() {
        return datavencimento;
    }

    /**
     * Sets the value of the datavencimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAVENCIMENTO(XMLGregorianCalendar value) {
        this.datavencimento = value;
    }

    /**
     * Gets the value of the valortitulo property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALORTITULO() {
        return valortitulo;
    }

    /**
     * Sets the value of the valortitulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALORTITULO(BigDecimal value) {
        this.valortitulo = value;
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
