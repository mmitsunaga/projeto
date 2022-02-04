
package br.gov.caixa.jaxws.sibar.consulta_cobranca_bancaria.boleto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DADOS_ENTRADA_TYPE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DADOS_ENTRADA_TYPE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="CONSULTA_SOLICITACAO_GRUPO_PAGADOR" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}CONSULTA_SOLICITACAO_GRUPO_PAGADOR_INPUT_TYPE"/>
 *           &lt;element name="CONSULTA_SOLICITACAO_PRE_IMPRESSO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}CONSULTA_SOLICITACAO_PRE_IMPRESSO_INPUT_TYPE"/>
 *           &lt;element name="CONSULTA_SOLICITACOES_GRUPO_PAGADOR" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}CONSULTA_SOLICITACOES_GRUPO_PAGADOR_INPUT_TYPE"/>
 *           &lt;element name="CONSULTA_SOLICITACOES_PRE_IMPRESSO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}CONSULTA_SOLICITACOES_PRE_IMPRESSO_INPUT_TYPE"/>
 *           &lt;element name="IMPRIME_BOLETO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}IMPRIME_BOLETO_INPUT_TYPE"/>
 *           &lt;element name="CONSULTA_BOLETO" type="{http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto}consulta_boleto_entrada_Type"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DADOS_ENTRADA_TYPE", propOrder = {
    "consultasolicitacaogrupopagador",
    "consultasolicitacaopreimpresso",
    "consultasolicitacoesgrupopagador",
    "consultasolicitacoespreimpresso",
    "imprimeboleto",
    "consultaboleto"
})
public class DADOSENTRADATYPE {

    @XmlElement(name = "CONSULTA_SOLICITACAO_GRUPO_PAGADOR")
    protected CONSULTASOLICITACAOGRUPOPAGADORINPUTTYPE consultasolicitacaogrupopagador;
    @XmlElement(name = "CONSULTA_SOLICITACAO_PRE_IMPRESSO")
    protected CONSULTASOLICITACAOPREIMPRESSOINPUTTYPE consultasolicitacaopreimpresso;
    @XmlElement(name = "CONSULTA_SOLICITACOES_GRUPO_PAGADOR")
    protected CONSULTASOLICITACOESGRUPOPAGADORINPUTTYPE consultasolicitacoesgrupopagador;
    @XmlElement(name = "CONSULTA_SOLICITACOES_PRE_IMPRESSO")
    protected CONSULTASOLICITACOESPREIMPRESSOINPUTTYPE consultasolicitacoespreimpresso;
    @XmlElement(name = "IMPRIME_BOLETO")
    protected IMPRIMEBOLETOINPUTTYPE imprimeboleto;
    @XmlElement(name = "CONSULTA_BOLETO")
    protected ConsultaBoletoEntradaType consultaboleto;

    /**
     * Gets the value of the consultasolicitacaogrupopagador property.
     * 
     * @return
     *     possible object is
     *     {@link CONSULTASOLICITACAOGRUPOPAGADORINPUTTYPE }
     *     
     */
    public CONSULTASOLICITACAOGRUPOPAGADORINPUTTYPE getCONSULTASOLICITACAOGRUPOPAGADOR() {
        return consultasolicitacaogrupopagador;
    }

    /**
     * Sets the value of the consultasolicitacaogrupopagador property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONSULTASOLICITACAOGRUPOPAGADORINPUTTYPE }
     *     
     */
    public void setCONSULTASOLICITACAOGRUPOPAGADOR(CONSULTASOLICITACAOGRUPOPAGADORINPUTTYPE value) {
        this.consultasolicitacaogrupopagador = value;
    }

    /**
     * Gets the value of the consultasolicitacaopreimpresso property.
     * 
     * @return
     *     possible object is
     *     {@link CONSULTASOLICITACAOPREIMPRESSOINPUTTYPE }
     *     
     */
    public CONSULTASOLICITACAOPREIMPRESSOINPUTTYPE getCONSULTASOLICITACAOPREIMPRESSO() {
        return consultasolicitacaopreimpresso;
    }

    /**
     * Sets the value of the consultasolicitacaopreimpresso property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONSULTASOLICITACAOPREIMPRESSOINPUTTYPE }
     *     
     */
    public void setCONSULTASOLICITACAOPREIMPRESSO(CONSULTASOLICITACAOPREIMPRESSOINPUTTYPE value) {
        this.consultasolicitacaopreimpresso = value;
    }

    /**
     * Gets the value of the consultasolicitacoesgrupopagador property.
     * 
     * @return
     *     possible object is
     *     {@link CONSULTASOLICITACOESGRUPOPAGADORINPUTTYPE }
     *     
     */
    public CONSULTASOLICITACOESGRUPOPAGADORINPUTTYPE getCONSULTASOLICITACOESGRUPOPAGADOR() {
        return consultasolicitacoesgrupopagador;
    }

    /**
     * Sets the value of the consultasolicitacoesgrupopagador property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONSULTASOLICITACOESGRUPOPAGADORINPUTTYPE }
     *     
     */
    public void setCONSULTASOLICITACOESGRUPOPAGADOR(CONSULTASOLICITACOESGRUPOPAGADORINPUTTYPE value) {
        this.consultasolicitacoesgrupopagador = value;
    }

    /**
     * Gets the value of the consultasolicitacoespreimpresso property.
     * 
     * @return
     *     possible object is
     *     {@link CONSULTASOLICITACOESPREIMPRESSOINPUTTYPE }
     *     
     */
    public CONSULTASOLICITACOESPREIMPRESSOINPUTTYPE getCONSULTASOLICITACOESPREIMPRESSO() {
        return consultasolicitacoespreimpresso;
    }

    /**
     * Sets the value of the consultasolicitacoespreimpresso property.
     * 
     * @param value
     *     allowed object is
     *     {@link CONSULTASOLICITACOESPREIMPRESSOINPUTTYPE }
     *     
     */
    public void setCONSULTASOLICITACOESPREIMPRESSO(CONSULTASOLICITACOESPREIMPRESSOINPUTTYPE value) {
        this.consultasolicitacoespreimpresso = value;
    }

    /**
     * Gets the value of the imprimeboleto property.
     * 
     * @return
     *     possible object is
     *     {@link IMPRIMEBOLETOINPUTTYPE }
     *     
     */
    public IMPRIMEBOLETOINPUTTYPE getIMPRIMEBOLETO() {
        return imprimeboleto;
    }

    /**
     * Sets the value of the imprimeboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link IMPRIMEBOLETOINPUTTYPE }
     *     
     */
    public void setIMPRIMEBOLETO(IMPRIMEBOLETOINPUTTYPE value) {
        this.imprimeboleto = value;
    }

    /**
     * Gets the value of the consultaboleto property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaBoletoEntradaType }
     *     
     */
    public ConsultaBoletoEntradaType getCONSULTABOLETO() {
        return consultaboleto;
    }

    /**
     * Sets the value of the consultaboleto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaBoletoEntradaType }
     *     
     */
    public void setCONSULTABOLETO(ConsultaBoletoEntradaType value) {
        this.consultaboleto = value;
    }

}
