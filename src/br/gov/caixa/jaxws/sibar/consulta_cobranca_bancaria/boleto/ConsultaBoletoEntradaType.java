
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consulta_boleto_entrada_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consulta_boleto_entrada_Type">
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
 *         &lt;element name="NOSSO_NUMERO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99999999999999999"/>
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
@XmlType(name = "consulta_boleto_entrada_Type", propOrder = {
    "codigobeneficiario",
    "nossonumero"
})
public class ConsultaBoletoEntradaType {

    @XmlElement(name = "CODIGO_BENEFICIARIO")
    protected int codigobeneficiario;
    @XmlElement(name = "NOSSO_NUMERO")
    protected long nossonumero;

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

}
