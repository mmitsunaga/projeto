/**
 * Sroxml.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.correios.webservice.resource;

public class Sroxml  implements java.io.Serializable {
    private java.lang.String versao;

    private java.lang.String qtd;

    private java.lang.String tipoPesquisa;

    private java.lang.String tipoResultado;

    private br.com.correios.webservice.resource.Objeto[] objeto;

    public Sroxml() {
    }

    public Sroxml(
           java.lang.String versao,
           java.lang.String qtd,
           java.lang.String tipoPesquisa,
           java.lang.String tipoResultado,
           br.com.correios.webservice.resource.Objeto[] objeto) {
           this.versao = versao;
           this.qtd = qtd;
           this.tipoPesquisa = tipoPesquisa;
           this.tipoResultado = tipoResultado;
           this.objeto = objeto;
    }


    /**
     * Gets the versao value for this Sroxml.
     * 
     * @return versao
     */
    public java.lang.String getVersao() {
        return versao;
    }


    /**
     * Sets the versao value for this Sroxml.
     * 
     * @param versao
     */
    public void setVersao(java.lang.String versao) {
        this.versao = versao;
    }


    /**
     * Gets the qtd value for this Sroxml.
     * 
     * @return qtd
     */
    public java.lang.String getQtd() {
        return qtd;
    }


    /**
     * Sets the qtd value for this Sroxml.
     * 
     * @param qtd
     */
    public void setQtd(java.lang.String qtd) {
        this.qtd = qtd;
    }


    /**
     * Gets the tipoPesquisa value for this Sroxml.
     * 
     * @return tipoPesquisa
     */
    public java.lang.String getTipoPesquisa() {
        return tipoPesquisa;
    }


    /**
     * Sets the tipoPesquisa value for this Sroxml.
     * 
     * @param tipoPesquisa
     */
    public void setTipoPesquisa(java.lang.String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }


    /**
     * Gets the tipoResultado value for this Sroxml.
     * 
     * @return tipoResultado
     */
    public java.lang.String getTipoResultado() {
        return tipoResultado;
    }


    /**
     * Sets the tipoResultado value for this Sroxml.
     * 
     * @param tipoResultado
     */
    public void setTipoResultado(java.lang.String tipoResultado) {
        this.tipoResultado = tipoResultado;
    }


    /**
     * Gets the objeto value for this Sroxml.
     * 
     * @return objeto
     */
    public br.com.correios.webservice.resource.Objeto[] getObjeto() {
        return objeto;
    }


    /**
     * Sets the objeto value for this Sroxml.
     * 
     * @param objeto
     */
    public void setObjeto(br.com.correios.webservice.resource.Objeto[] objeto) {
        this.objeto = objeto;
    }

    public br.com.correios.webservice.resource.Objeto getObjeto(int i) {
        return this.objeto[i];
    }

    public void setObjeto(int i, br.com.correios.webservice.resource.Objeto _value) {
        this.objeto[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Sroxml)) return false;
        Sroxml other = (Sroxml) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.versao==null && other.getVersao()==null) || 
             (this.versao!=null &&
              this.versao.equals(other.getVersao()))) &&
            ((this.qtd==null && other.getQtd()==null) || 
             (this.qtd!=null &&
              this.qtd.equals(other.getQtd()))) &&
            ((this.tipoPesquisa==null && other.getTipoPesquisa()==null) || 
             (this.tipoPesquisa!=null &&
              this.tipoPesquisa.equals(other.getTipoPesquisa()))) &&
            ((this.tipoResultado==null && other.getTipoResultado()==null) || 
             (this.tipoResultado!=null &&
              this.tipoResultado.equals(other.getTipoResultado()))) &&
            ((this.objeto==null && other.getObjeto()==null) || 
             (this.objeto!=null &&
              java.util.Arrays.equals(this.objeto, other.getObjeto())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getVersao() != null) {
            _hashCode += getVersao().hashCode();
        }
        if (getQtd() != null) {
            _hashCode += getQtd().hashCode();
        }
        if (getTipoPesquisa() != null) {
            _hashCode += getTipoPesquisa().hashCode();
        }
        if (getTipoResultado() != null) {
            _hashCode += getTipoResultado().hashCode();
        }
        if (getObjeto() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObjeto());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObjeto(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Sroxml.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "sroxml"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qtd");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qtd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoPesquisa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipoPesquisa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoResultado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipoResultado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objeto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "objeto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "objeto"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
