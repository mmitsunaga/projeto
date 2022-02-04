
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TITULO_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TITULO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LINHA_DIGITAVEL">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="56"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA_VENCIMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="SIGLA_MODALIDADE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NOSSO_NUMERO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99999999999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DV_NOSSO_NUMERO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MOEDA" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}MOEDA_TYPE"/>
 *         &lt;element name="NUMERO_DOCUMENTO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ACEITE" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}ACEITE_TYPE"/>
 *         &lt;element name="ESPECIE" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}ESPECIE_TYPE"/>
 *         &lt;element name="DATA_DOCUMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DATA_PROCESSAMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="CODIGO_BARRAS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="44"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VALOR">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
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
@XmlType(name = "TITULO_TYPE", propOrder = {
    "linhadigitavel",
    "datavencimento",
    "siglamodalidade",
    "nossonumero",
    "dvnossonumero",
    "moeda",
    "numerodocumento",
    "aceite",
    "especie",
    "datadocumento",
    "dataprocessamento",
    "codigobarras",
    "valor"
})
public class TITULOTYPE {

    @XmlElement(name = "LINHA_DIGITAVEL", required = true)
    protected String linhadigitavel;
    @XmlElement(name = "DATA_VENCIMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datavencimento;
    @XmlElement(name = "SIGLA_MODALIDADE", required = true)
    protected String siglamodalidade;
    @XmlElement(name = "NOSSO_NUMERO")
    protected long nossonumero;
    @XmlElement(name = "DV_NOSSO_NUMERO")
    protected short dvnossonumero;
    @XmlElement(name = "MOEDA", required = true)
    protected MOEDATYPE moeda;
    @XmlElement(name = "NUMERO_DOCUMENTO", required = true)
    protected String numerodocumento;
    @XmlElement(name = "ACEITE", required = true)
    protected ACEITETYPE aceite;
    @XmlElement(name = "ESPECIE", required = true)
    protected ESPECIETYPE especie;
    @XmlElement(name = "DATA_DOCUMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datadocumento;
    @XmlElement(name = "DATA_PROCESSAMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataprocessamento;
    @XmlElement(name = "CODIGO_BARRAS", required = true)
    protected String codigobarras;
    @XmlElement(name = "VALOR", required = true)
    protected BigDecimal valor;

    /**
     * Gets the value of the linhadigitavel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLINHADIGITAVEL() {
        return linhadigitavel;
    }

    /**
     * Sets the value of the linhadigitavel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLINHADIGITAVEL(String value) {
        this.linhadigitavel = value;
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
     * Gets the value of the siglamodalidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIGLAMODALIDADE() {
        return siglamodalidade;
    }

    /**
     * Sets the value of the siglamodalidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIGLAMODALIDADE(String value) {
        this.siglamodalidade = value;
    }

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
     * Gets the value of the dvnossonumero property.
     * 
     */
    public short getDVNOSSONUMERO() {
        return dvnossonumero;
    }

    /**
     * Sets the value of the dvnossonumero property.
     * 
     */
    public void setDVNOSSONUMERO(short value) {
        this.dvnossonumero = value;
    }

    /**
     * Gets the value of the moeda property.
     * 
     * @return
     *     possible object is
     *     {@link MOEDATYPE }
     *     
     */
    public MOEDATYPE getMOEDA() {
        return moeda;
    }

    /**
     * Sets the value of the moeda property.
     * 
     * @param value
     *     allowed object is
     *     {@link MOEDATYPE }
     *     
     */
    public void setMOEDA(MOEDATYPE value) {
        this.moeda = value;
    }

    /**
     * Gets the value of the numerodocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUMERODOCUMENTO() {
        return numerodocumento;
    }

    /**
     * Sets the value of the numerodocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUMERODOCUMENTO(String value) {
        this.numerodocumento = value;
    }

    /**
     * Gets the value of the aceite property.
     * 
     * @return
     *     possible object is
     *     {@link ACEITETYPE }
     *     
     */
    public ACEITETYPE getACEITE() {
        return aceite;
    }

    /**
     * Sets the value of the aceite property.
     * 
     * @param value
     *     allowed object is
     *     {@link ACEITETYPE }
     *     
     */
    public void setACEITE(ACEITETYPE value) {
        this.aceite = value;
    }

    /**
     * Gets the value of the especie property.
     * 
     * @return
     *     possible object is
     *     {@link ESPECIETYPE }
     *     
     */
    public ESPECIETYPE getESPECIE() {
        return especie;
    }

    /**
     * Sets the value of the especie property.
     * 
     * @param value
     *     allowed object is
     *     {@link ESPECIETYPE }
     *     
     */
    public void setESPECIE(ESPECIETYPE value) {
        this.especie = value;
    }

    /**
     * Gets the value of the datadocumento property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATADOCUMENTO() {
        return datadocumento;
    }

    /**
     * Sets the value of the datadocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATADOCUMENTO(XMLGregorianCalendar value) {
        this.datadocumento = value;
    }

    /**
     * Gets the value of the dataprocessamento property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAPROCESSAMENTO() {
        return dataprocessamento;
    }

    /**
     * Sets the value of the dataprocessamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAPROCESSAMENTO(XMLGregorianCalendar value) {
        this.dataprocessamento = value;
    }

    /**
     * Gets the value of the codigobarras property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCODIGOBARRAS() {
        return codigobarras;
    }

    /**
     * Sets the value of the codigobarras property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCODIGOBARRAS(String value) {
        this.codigobarras = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALOR() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALOR(BigDecimal value) {
        this.valor = value;
    }

}
