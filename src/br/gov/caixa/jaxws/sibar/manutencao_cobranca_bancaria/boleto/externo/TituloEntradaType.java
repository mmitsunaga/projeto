
package br.gov.caixa.jaxws.sibar.manutencao_cobranca_bancaria.boleto.externo;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for titulo_entrada_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="titulo_entrada_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NOSSO_NUMERO" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99999999999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NUMERO_DOCUMENTO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="11"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA_VENCIMENTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="VALOR" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type"/>
 *         &lt;element name="TIPO_ESPECIE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FLAG_ACEITE">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="S"/>
 *               &lt;enumeration value="N"/>
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DATA_EMISSAO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="JUROS_MORA" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}juros_mora_Type"/>
 *         &lt;element name="VALOR_ABATIMENTO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type" minOccurs="0"/>
 *         &lt;element name="POS_VENCIMENTO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}pos_vencimento_Type"/>
 *         &lt;element name="CODIGO_MOEDA">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}short">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PAGADOR" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}pagador_Type"/>
 *         &lt;element name="SACADOR_AVALISTA" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}sacador_avalista_Type" minOccurs="0"/>
 *         &lt;element name="MULTA" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}multa_Type" minOccurs="0"/>
 *         &lt;element name="DESCONTOS" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}descontos_Type" minOccurs="0"/>
 *         &lt;element name="VALOR_IOF" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}valor_Type" minOccurs="0"/>
 *         &lt;element name="IDENTIFICACAO_EMPRESA" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FICHA_COMPENSACAO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}ficha_compensacao_Type" minOccurs="0"/>
 *         &lt;element name="RECIBO_PAGADOR" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}recibo_pagador_Type" minOccurs="0"/>
 *         &lt;element name="PAGAMENTO" type="{http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo}pagamento_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "titulo_entrada_Type", propOrder = {
    "nossonumero",
    "numerodocumento",
    "datavencimento",
    "valor",
    "tipoespecie",
    "flagaceite",
    "dataemissao",
    "jurosmora",
    "valorabatimento",
    "posvencimento",
    "codigomoeda",
    "pagador",
    "sacadoravalista",
    "multa",
    "descontos",
    "valoriof",
    "identificacaoempresa",
    "fichacompensacao",
    "recibopagador",
    "pagamento"
})
public class TituloEntradaType {

    @XmlElement(name = "NOSSO_NUMERO")
    protected Long nossonumero;
    @XmlElement(name = "NUMERO_DOCUMENTO", required = true)
    protected String numerodocumento;
    @XmlElement(name = "DATA_VENCIMENTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datavencimento;
    @XmlElement(name = "VALOR", required = true)
    protected BigDecimal valor;
    @XmlElement(name = "TIPO_ESPECIE")
    protected short tipoespecie;
    @XmlElement(name = "FLAG_ACEITE", required = true)
    protected String flagaceite;
    @XmlElement(name = "DATA_EMISSAO")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataemissao;
    @XmlElement(name = "JUROS_MORA", required = true)
    protected JurosMoraType jurosmora;
    @XmlElement(name = "VALOR_ABATIMENTO")
    protected BigDecimal valorabatimento;
    @XmlElement(name = "POS_VENCIMENTO", required = true)
    protected PosVencimentoType posvencimento;
    @XmlElement(name = "CODIGO_MOEDA")
    protected short codigomoeda;
    @XmlElement(name = "PAGADOR", required = true)
    protected PagadorType pagador;
    @XmlElement(name = "SACADOR_AVALISTA")
    protected SacadorAvalistaType sacadoravalista;
    @XmlElement(name = "MULTA")
    protected MultaType multa;
    @XmlElement(name = "DESCONTOS")
    protected DescontosType descontos;
    @XmlElement(name = "VALOR_IOF")
    protected BigDecimal valoriof;
    @XmlElement(name = "IDENTIFICACAO_EMPRESA")
    protected String identificacaoempresa;
    @XmlElement(name = "FICHA_COMPENSACAO")
    protected FichaCompensacaoType fichacompensacao;
    @XmlElement(name = "RECIBO_PAGADOR")
    protected ReciboPagadorType recibopagador;
    @XmlElement(name = "PAGAMENTO")
    protected PagamentoType pagamento;

    /**
     * Gets the value of the nossonumero property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNOSSONUMERO() {
        return nossonumero;
    }

    /**
     * Sets the value of the nossonumero property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNOSSONUMERO(Long value) {
        this.nossonumero = value;
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

    /**
     * Gets the value of the tipoespecie property.
     * 
     */
    public short getTIPOESPECIE() {
        return tipoespecie;
    }

    /**
     * Sets the value of the tipoespecie property.
     * 
     */
    public void setTIPOESPECIE(short value) {
        this.tipoespecie = value;
    }

    /**
     * Gets the value of the flagaceite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFLAGACEITE() {
        return flagaceite;
    }

    /**
     * Sets the value of the flagaceite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFLAGACEITE(String value) {
        this.flagaceite = value;
    }

    /**
     * Gets the value of the dataemissao property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDATAEMISSAO() {
        return dataemissao;
    }

    /**
     * Sets the value of the dataemissao property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDATAEMISSAO(XMLGregorianCalendar value) {
        this.dataemissao = value;
    }

    /**
     * Gets the value of the jurosmora property.
     * 
     * @return
     *     possible object is
     *     {@link JurosMoraType }
     *     
     */
    public JurosMoraType getJUROSMORA() {
        return jurosmora;
    }

    /**
     * Sets the value of the jurosmora property.
     * 
     * @param value
     *     allowed object is
     *     {@link JurosMoraType }
     *     
     */
    public void setJUROSMORA(JurosMoraType value) {
        this.jurosmora = value;
    }

    /**
     * Gets the value of the valorabatimento property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALORABATIMENTO() {
        return valorabatimento;
    }

    /**
     * Sets the value of the valorabatimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALORABATIMENTO(BigDecimal value) {
        this.valorabatimento = value;
    }

    /**
     * Gets the value of the posvencimento property.
     * 
     * @return
     *     possible object is
     *     {@link PosVencimentoType }
     *     
     */
    public PosVencimentoType getPOSVENCIMENTO() {
        return posvencimento;
    }

    /**
     * Sets the value of the posvencimento property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosVencimentoType }
     *     
     */
    public void setPOSVENCIMENTO(PosVencimentoType value) {
        this.posvencimento = value;
    }

    /**
     * Gets the value of the codigomoeda property.
     * 
     */
    public short getCODIGOMOEDA() {
        return codigomoeda;
    }

    /**
     * Sets the value of the codigomoeda property.
     * 
     */
    public void setCODIGOMOEDA(short value) {
        this.codigomoeda = value;
    }

    /**
     * Gets the value of the pagador property.
     * 
     * @return
     *     possible object is
     *     {@link PagadorType }
     *     
     */
    public PagadorType getPAGADOR() {
        return pagador;
    }

    /**
     * Sets the value of the pagador property.
     * 
     * @param value
     *     allowed object is
     *     {@link PagadorType }
     *     
     */
    public void setPAGADOR(PagadorType value) {
        this.pagador = value;
    }

    /**
     * Gets the value of the sacadoravalista property.
     * 
     * @return
     *     possible object is
     *     {@link SacadorAvalistaType }
     *     
     */
    public SacadorAvalistaType getSACADORAVALISTA() {
        return sacadoravalista;
    }

    /**
     * Sets the value of the sacadoravalista property.
     * 
     * @param value
     *     allowed object is
     *     {@link SacadorAvalistaType }
     *     
     */
    public void setSACADORAVALISTA(SacadorAvalistaType value) {
        this.sacadoravalista = value;
    }

    /**
     * Gets the value of the multa property.
     * 
     * @return
     *     possible object is
     *     {@link MultaType }
     *     
     */
    public MultaType getMULTA() {
        return multa;
    }

    /**
     * Sets the value of the multa property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultaType }
     *     
     */
    public void setMULTA(MultaType value) {
        this.multa = value;
    }

    /**
     * Gets the value of the descontos property.
     * 
     * @return
     *     possible object is
     *     {@link DescontosType }
     *     
     */
    public DescontosType getDESCONTOS() {
        return descontos;
    }

    /**
     * Sets the value of the descontos property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescontosType }
     *     
     */
    public void setDESCONTOS(DescontosType value) {
        this.descontos = value;
    }

    /**
     * Gets the value of the valoriof property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVALORIOF() {
        return valoriof;
    }

    /**
     * Sets the value of the valoriof property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVALORIOF(BigDecimal value) {
        this.valoriof = value;
    }

    /**
     * Gets the value of the identificacaoempresa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDENTIFICACAOEMPRESA() {
        return identificacaoempresa;
    }

    /**
     * Sets the value of the identificacaoempresa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDENTIFICACAOEMPRESA(String value) {
        this.identificacaoempresa = value;
    }

    /**
     * Gets the value of the fichacompensacao property.
     * 
     * @return
     *     possible object is
     *     {@link FichaCompensacaoType }
     *     
     */
    public FichaCompensacaoType getFICHACOMPENSACAO() {
        return fichacompensacao;
    }

    /**
     * Sets the value of the fichacompensacao property.
     * 
     * @param value
     *     allowed object is
     *     {@link FichaCompensacaoType }
     *     
     */
    public void setFICHACOMPENSACAO(FichaCompensacaoType value) {
        this.fichacompensacao = value;
    }

    /**
     * Gets the value of the recibopagador property.
     * 
     * @return
     *     possible object is
     *     {@link ReciboPagadorType }
     *     
     */
    public ReciboPagadorType getRECIBOPAGADOR() {
        return recibopagador;
    }

    /**
     * Sets the value of the recibopagador property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReciboPagadorType }
     *     
     */
    public void setRECIBOPAGADOR(ReciboPagadorType value) {
        this.recibopagador = value;
    }

    /**
     * Gets the value of the pagamento property.
     * 
     * @return
     *     possible object is
     *     {@link PagamentoType }
     *     
     */
    public PagamentoType getPAGAMENTO() {
        return pagamento;
    }

    /**
     * Sets the value of the pagamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link PagamentoType }
     *     
     */
    public void setPAGAMENTO(PagamentoType value) {
        this.pagamento = value;
    }

}
