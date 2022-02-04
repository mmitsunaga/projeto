
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CONSULTA_SOLICITACOES_GRUPO_PAGADOR_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CONSULTA_SOLICITACOES_GRUPO_PAGADOR_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QUANTIDADE_PAGINAS">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SOLICITACOES" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}SOLICITACOES_GRUPO_PAGADOR_TYPE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CONSULTA_SOLICITACOES_GRUPO_PAGADOR_TYPE", propOrder = {
    "quantidadepaginas",
    "solicitacoes"
})
public class CONSULTASOLICITACOESGRUPOPAGADORTYPE {

    @XmlElement(name = "QUANTIDADE_PAGINAS")
    protected int quantidadepaginas;
    @XmlElement(name = "SOLICITACOES")
    protected SOLICITACOESGRUPOPAGADORTYPE solicitacoes;

    /**
     * Gets the value of the quantidadepaginas property.
     * 
     */
    public int getQUANTIDADEPAGINAS() {
        return quantidadepaginas;
    }

    /**
     * Sets the value of the quantidadepaginas property.
     * 
     */
    public void setQUANTIDADEPAGINAS(int value) {
        this.quantidadepaginas = value;
    }

    /**
     * Gets the value of the solicitacoes property.
     * 
     * @return
     *     possible object is
     *     {@link SOLICITACOESGRUPOPAGADORTYPE }
     *     
     */
    public SOLICITACOESGRUPOPAGADORTYPE getSOLICITACOES() {
        return solicitacoes;
    }

    /**
     * Sets the value of the solicitacoes property.
     * 
     * @param value
     *     allowed object is
     *     {@link SOLICITACOESGRUPOPAGADORTYPE }
     *     
     */
    public void setSOLICITACOES(SOLICITACOESGRUPOPAGADORTYPE value) {
        this.solicitacoes = value;
    }

}
