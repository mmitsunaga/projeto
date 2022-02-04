
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CONSULTA_SOLICITACAO_PRE_IMPRESSO_INPUT_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONSULTA_SOLICITACAO_PRE_IMPRESSO_INPUT_TYPE">
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
 *         &lt;element name="DATA_SOLICITACAO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="TIPO_BLOQUETO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9"/>
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
@XmlType(name = "CONSULTA_SOLICITACAO_PRE_IMPRESSO_INPUT_TYPE", propOrder = {
    "codigobeneficiario",
    "datasolicitacao",
    "tipobloqueto"
})
public class CONSULTASOLICITACAOPREIMPRESSOINPUTTYPE {

    @XmlElement(name = "CODIGO_BENEFICIARIO")
    protected int codigobeneficiario;
    @XmlElement(name = "DATA_SOLICITACAO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datasolicitacao;
    @XmlElement(name = "TIPO_BLOQUETO")
    protected short tipobloqueto;

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
     * Gets the value of the tipobloqueto property.
     * 
     */
    public short getTIPOBLOQUETO() {
        return tipobloqueto;
    }

    /**
     * Sets the value of the tipobloqueto property.
     * 
     */
    public void setTIPOBLOQUETO(short value) {
        this.tipobloqueto = value;
    }

}
