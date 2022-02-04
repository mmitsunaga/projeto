
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for inclui_boleto_entrada_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="inclui_boleto_entrada_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CODIGO_BENEFICIARIO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="9999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TITULO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}titulo_entrada_Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inclui_boleto_entrada_Type", propOrder = {
    "codigobeneficiario",
    "titulo"
})
public class IncluiBoletoEntradaType {

    @XmlElement(name = "CODIGO_BENEFICIARIO")
    protected int codigobeneficiario;
    @XmlElement(name = "TITULO", required = true)
    protected TituloEntradaType titulo;

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
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link TituloEntradaType }
     *     
     */
    public TituloEntradaType getTITULO() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TituloEntradaType }
     *     
     */
    public void setTITULO(TituloEntradaType value) {
        this.titulo = value;
    }

}
