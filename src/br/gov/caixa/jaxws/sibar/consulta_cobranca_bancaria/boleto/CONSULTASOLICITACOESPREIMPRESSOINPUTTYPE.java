
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CONSULTA_SOLICITACOES_PRE_IMPRESSO_INPUT_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONSULTA_SOLICITACOES_PRE_IMPRESSO_INPUT_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CODIGO_BENEFICIARIO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TIPO_BOLETO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA_INICIO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="DATA_FIM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="FLAG_SITUACAO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PAGINA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999"/>
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
@XmlType(name = "CONSULTA_SOLICITACOES_PRE_IMPRESSO_INPUT_TYPE", propOrder = {
    "codigobeneficiario",
    "tipoboleto",
    "datainicio",
    "datafim",
    "flagsituacao",
    "pagina"
})
public class CONSULTASOLICITACOESPREIMPRESSOINPUTTYPE {

    @XmlElement(name = "CODIGO_BENEFICIARIO")
    protected int codigobeneficiario;
    @XmlElement(name = "TIPO_BOLETO")
    protected String tipoboleto;
    @XmlElement(name = "DATA_INICIO")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datainicio;
    @XmlElement(name = "DATA_FIM")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datafim;
    @XmlElement(name = "FLAG_SITUACAO", required = true)
    protected String flagsituacao;
    @XmlElement(name = "PAGINA")
    protected int pagina;

    /**
     * Gets the value of the codigobeneficiario property.
     * 
     */
    public int getCODIGOBENEFICIARIO() {
        return codigobeneficiario;
    }

    /**
     * Sets the value of the codigobeneficiario property.
     * 
     */
    public void setCODIGOBENEFICIARIO(int value) {
        this.codigobeneficiario = value;
    }

    /**
     * Gets the value of the tipoboleto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTIPOBOLETO() {
        return tipoboleto;
    }

    /**
     * Sets the value of the tipoboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTIPOBOLETO(String value) {
        this.tipoboleto = value;
    }

    /**
     * Gets the value of the datainicio property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAINICIO() {
        return datainicio;
    }

    /**
     * Sets the value of the datainicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAINICIO(XMLGregorianCalendar value) {
        this.datainicio = value;
    }

    /**
     * Gets the value of the datafim property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAFIM() {
        return datafim;
    }

    /**
     * Sets the value of the datafim property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAFIM(XMLGregorianCalendar value) {
        this.datafim = value;
    }

    /**
     * Gets the value of the flagsituacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLAGSITUACAO() {
        return flagsituacao;
    }

    /**
     * Sets the value of the flagsituacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLAGSITUACAO(String value) {
        this.flagsituacao = value;
    }

    /**
     * Gets the value of the pagina property.
     * 
     */
    public int getPAGINA() {
        return pagina;
    }

    /**
     * Sets the value of the pagina property.
     * 
     */
    public void setPAGINA(int value) {
        this.pagina = value;
    }

}
