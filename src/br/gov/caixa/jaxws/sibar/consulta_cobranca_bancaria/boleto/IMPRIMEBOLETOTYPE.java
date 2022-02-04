
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IMPRIME_BOLETO_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IMPRIME_BOLETO_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CEDENTE" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}CEDENTE_TYPE"/>
 *         &lt;element name="SACADO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}SACADO_TYPE"/>
 *         &lt;element name="SACADOR" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}SACADOR_TYPE"/>
 *         &lt;element name="TITULO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}TITULO_TYPE"/>
 *         &lt;element name="VALOR" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}VALOR_TYPE"/>
 *         &lt;element name="MENSAGENS" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}MENSAGENS_TYPE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IMPRIME_BOLETO_TYPE", propOrder = {
    "cedente",
    "sacado",
    "sacador",
    "titulo",
    "valor",
    "mensagens"
})
public class IMPRIMEBOLETOTYPE {

    @XmlElement(name = "CEDENTE", required = true)
    protected CEDENTETYPE cedente;
    @XmlElement(name = "SACADO", required = true)
    protected SACADOTYPE sacado;
    @XmlElement(name = "SACADOR", required = true)
    protected SACADORTYPE sacador;
    @XmlElement(name = "TITULO", required = true)
    protected TITULOTYPE titulo;
    @XmlElement(name = "VALOR", required = true)
    protected VALORTYPE valor;
    @XmlElement(name = "MENSAGENS")
    protected MENSAGENSTYPE mensagens;

    /**
     * Gets the value of the cedente property.
     * 
     * @return
     *     possible object is
     *     {@link CEDENTETYPE }
     *     
     */
    public CEDENTETYPE getCEDENTE() {
        return cedente;
    }

    /**
     * Sets the value of the cedente property.
     * 
     * @param value
     *     allowed object is
     *     {@link CEDENTETYPE }
     *     
     */
    public void setCEDENTE(CEDENTETYPE value) {
        this.cedente = value;
    }

    /**
     * Gets the value of the sacado property.
     * 
     * @return
     *     possible object is
     *     {@link SACADOTYPE }
     *     
     */
    public SACADOTYPE getSACADO() {
        return sacado;
    }

    /**
     * Sets the value of the sacado property.
     * 
     * @param value
     *     allowed object is
     *     {@link SACADOTYPE }
     *     
     */
    public void setSACADO(SACADOTYPE value) {
        this.sacado = value;
    }

    /**
     * Gets the value of the sacador property.
     * 
     * @return
     *     possible object is
     *     {@link SACADORTYPE }
     *     
     */
    public SACADORTYPE getSACADOR() {
        return sacador;
    }

    /**
     * Sets the value of the sacador property.
     * 
     * @param value
     *     allowed object is
     *     {@link SACADORTYPE }
     *     
     */
    public void setSACADOR(SACADORTYPE value) {
        this.sacador = value;
    }

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link TITULOTYPE }
     *     
     */
    public TITULOTYPE getTITULO() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TITULOTYPE }
     *     
     */
    public void setTITULO(TITULOTYPE value) {
        this.titulo = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link VALORTYPE }
     *     
     */
    public VALORTYPE getVALOR() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link VALORTYPE }
     *     
     */
    public void setVALOR(VALORTYPE value) {
        this.valor = value;
    }

    /**
     * Gets the value of the mensagens property.
     * 
     * @return
     *     possible object is
     *     {@link MENSAGENSTYPE }
     *     
     */
    public MENSAGENSTYPE getMENSAGENS() {
        return mensagens;
    }

    /**
     * Sets the value of the mensagens property.
     * 
     * @param value
     *     allowed object is
     *     {@link MENSAGENSTYPE }
     *     
     */
    public void setMENSAGENS(MENSAGENSTYPE value) {
        this.mensagens = value;
    }

}
