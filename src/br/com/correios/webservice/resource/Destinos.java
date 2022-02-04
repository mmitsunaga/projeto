/**
 * Destinos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.correios.webservice.resource;

public class Destinos  implements java.io.Serializable {
    private java.lang.String local;

    private java.lang.String codigo;

    private java.lang.String cidade;

    private java.lang.String bairro;

    private java.lang.String uf;

    public Destinos() {
    }

    public Destinos(
           java.lang.String local,
           java.lang.String codigo,
           java.lang.String cidade,
           java.lang.String bairro,
           java.lang.String uf) {
           this.local = local;
           this.codigo = codigo;
           this.cidade = cidade;
           this.bairro = bairro;
           this.uf = uf;
    }


    /**
     * Gets the local value for this Destinos.
     * 
     * @return local
     */
    public java.lang.String getLocal() {
        return local;
    }


    /**
     * Sets the local value for this Destinos.
     * 
     * @param local
     */
    public void setLocal(java.lang.String local) {
        this.local = local;
    }


    /**
     * Gets the codigo value for this Destinos.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this Destinos.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the cidade value for this Destinos.
     * 
     * @return cidade
     */
    public java.lang.String getCidade() {
        return cidade;
    }


    /**
     * Sets the cidade value for this Destinos.
     * 
     * @param cidade
     */
    public void setCidade(java.lang.String cidade) {
        this.cidade = cidade;
    }


    /**
     * Gets the bairro value for this Destinos.
     * 
     * @return bairro
     */
    public java.lang.String getBairro() {
        return bairro;
    }


    /**
     * Sets the bairro value for this Destinos.
     * 
     * @param bairro
     */
    public void setBairro(java.lang.String bairro) {
        this.bairro = bairro;
    }


    /**
     * Gets the uf value for this Destinos.
     * 
     * @return uf
     */
    public java.lang.String getUf() {
        return uf;
    }


    /**
     * Sets the uf value for this Destinos.
     * 
     * @param uf
     */
    public void setUf(java.lang.String uf) {
        this.uf = uf;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Destinos)) return false;
        Destinos other = (Destinos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.local==null && other.getLocal()==null) || 
             (this.local!=null &&
              this.local.equals(other.getLocal()))) &&
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.cidade==null && other.getCidade()==null) || 
             (this.cidade!=null &&
              this.cidade.equals(other.getCidade()))) &&
            ((this.bairro==null && other.getBairro()==null) || 
             (this.bairro!=null &&
              this.bairro.equals(other.getBairro()))) &&
            ((this.uf==null && other.getUf()==null) || 
             (this.uf!=null &&
              this.uf.equals(other.getUf())));
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
        if (getLocal() != null) {
            _hashCode += getLocal().hashCode();
        }
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getCidade() != null) {
            _hashCode += getCidade().hashCode();
        }
        if (getBairro() != null) {
            _hashCode += getBairro().hashCode();
        }
        if (getUf() != null) {
            _hashCode += getUf().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Destinos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "destinos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("local");
        elemField.setXmlName(new javax.xml.namespace.QName("", "local"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cidade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bairro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bairro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
