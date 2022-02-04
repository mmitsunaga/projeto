
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CONSULTA_SOLICITACAO_GRUPO_PAGADOR_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONSULTA_SOLICITACAO_GRUPO_PAGADOR_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DATA_VENCIMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="VALOR_TITULO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999999999.99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PRAZO_PROTESTO_DEVOLUCAO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DESCRICOES" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}DESCRICOES_TYPE"/>
 *         &lt;element name="COMPLEMENTARES" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}COMPLEMENTARES_TYPE" minOccurs="0"/>
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
@XmlType(name = "CONSULTA_SOLICITACAO_GRUPO_PAGADOR_TYPE", propOrder = {
    "datavencimento",
    "valortitulo",
    "prazoprotestodevolucao",
    "descricoes",
    "complementares",
    "situacao"
})
public class CONSULTASOLICITACAOGRUPOPAGADORTYPE {

    @XmlElement(name = "DATA_VENCIMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datavencimento;
    @XmlElement(name = "VALOR_TITULO", required = true)
    protected BigDecimal valortitulo;
    @XmlElement(name = "PRAZO_PROTESTO_DEVOLUCAO")
    protected short prazoprotestodevolucao;
    @XmlElement(name = "DESCRICOES", required = true)
    protected DESCRICOESTYPE descricoes;
    @XmlElement(name = "COMPLEMENTARES")
    protected COMPLEMENTARESTYPE complementares;
    @XmlElement(name = "SITUACAO", required = true)
    protected String situacao;

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
     * Gets the value of the prazoprotestodevolucao property.
     * 
     */
    public short getPRAZOPROTESTODEVOLUCAO() {
        return prazoprotestodevolucao;
    }

    /**
     * Sets the value of the prazoprotestodevolucao property.
     * 
     */
    public void setPRAZOPROTESTODEVOLUCAO(short value) {
        this.prazoprotestodevolucao = value;
    }

    /**
     * Gets the value of the descricoes property.
     * 
     * @return
     *     possible object is
     *     {@link DESCRICOESTYPE }
     *     
     */
    public DESCRICOESTYPE getDESCRICOES() {
        return descricoes;
    }

    /**
     * Sets the value of the descricoes property.
     * 
     * @param value
     *     allowed object is
     *     {@link DESCRICOESTYPE }
     *     
     */
    public void setDESCRICOES(DESCRICOESTYPE value) {
        this.descricoes = value;
    }

    /**
     * Gets the value of the complementares property.
     * 
     * @return
     *     possible object is
     *     {@link COMPLEMENTARESTYPE }
     *     
     */
    public COMPLEMENTARESTYPE getCOMPLEMENTARES() {
        return complementares;
    }

    /**
     * Sets the value of the complementares property.
     * 
     * @param value
     *     allowed object is
     *     {@link COMPLEMENTARESTYPE }
     *     
     */
    public void setCOMPLEMENTARES(COMPLEMENTARESTYPE value) {
        this.complementares = value;
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
