
package br.gov.caixa.jaxws.sibar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HEADER_BARRAMENTO_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HEADER_BARRAMENTO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VERSAO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AUTENTICACAO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="USUARIO_SERVICO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="USUARIO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OPERACAO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="INDICE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SISTEMA_ORIGEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UNIDADE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IDENTIFICADOR_ORIGEM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA_HORA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ID_PROCESSO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
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
@XmlType(name = "HEADER_BARRAMENTO_TYPE", propOrder = {
    "versao",
    "autenticacao",
    "usuarioservico",
    "usuario",
    "operacao",
    "indice",
    "sistemaorigem",
    "unidade",
    "identificadororigem",
    "datahora",
    "idprocesso"
})
public class HEADERBARRAMENTOTYPE {

    @XmlElement(name = "VERSAO", required = true)
    protected String versao;
    @XmlElement(name = "AUTENTICACAO")
    protected String autenticacao;
    @XmlElement(name = "USUARIO_SERVICO")
    protected String usuarioservico;
    @XmlElement(name = "USUARIO")
    protected String usuario;
    @XmlElement(name = "OPERACAO", required = true)
    protected String operacao;
    @XmlElement(name = "INDICE")
    protected Integer indice;
    @XmlElement(name = "SISTEMA_ORIGEM")
    protected String sistemaorigem;
    @XmlElement(name = "UNIDADE")
    protected String unidade;
    @XmlElement(name = "IDENTIFICADOR_ORIGEM")
    protected String identificadororigem;
    @XmlElement(name = "DATA_HORA", required = true)
    protected String datahora;
    @XmlElement(name = "ID_PROCESSO")
    protected String idprocesso;

    /**
     * Gets the value of the versao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVERSAO() {
        return versao;
    }

    /**
     * Sets the value of the versao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVERSAO(String value) {
        this.versao = value;
    }

    /**
     * Gets the value of the autenticacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAUTENTICACAO() {
        return autenticacao;
    }

    /**
     * Sets the value of the autenticacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAUTENTICACAO(String value) {
        this.autenticacao = value;
    }

    /**
     * Gets the value of the usuarioservico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSUARIOSERVICO() {
        return usuarioservico;
    }

    /**
     * Sets the value of the usuarioservico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSUARIOSERVICO(String value) {
        this.usuarioservico = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUSUARIO() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUSUARIO(String value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the operacao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOPERACAO() {
        return operacao;
    }

    /**
     * Sets the value of the operacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOPERACAO(String value) {
        this.operacao = value;
    }

    /**
     * Gets the value of the indice property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getINDICE() {
        return indice;
    }

    /**
     * Sets the value of the indice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setINDICE(Integer value) {
        this.indice = value;
    }

    /**
     * Gets the value of the sistemaorigem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSISTEMAORIGEM() {
        return sistemaorigem;
    }

    /**
     * Sets the value of the sistemaorigem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSISTEMAORIGEM(String value) {
        this.sistemaorigem = value;
    }

    /**
     * Gets the value of the unidade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUNIDADE() {
        return unidade;
    }

    /**
     * Sets the value of the unidade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUNIDADE(String value) {
        this.unidade = value;
    }

    /**
     * Gets the value of the identificadororigem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDENTIFICADORORIGEM() {
        return identificadororigem;
    }

    /**
     * Sets the value of the identificadororigem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDENTIFICADORORIGEM(String value) {
        this.identificadororigem = value;
    }

    /**
     * Gets the value of the datahora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDATAHORA() {
        return datahora;
    }

    /**
     * Sets the value of the datahora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDATAHORA(String value) {
        this.datahora = value;
    }

    /**
     * Gets the value of the idprocesso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDPROCESSO() {
        return idprocesso;
    }

    /**
     * Sets the value of the idprocesso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDPROCESSO(String value) {
        this.idprocesso = value;
    }

}
