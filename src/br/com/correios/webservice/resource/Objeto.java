/**
 * Objeto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.correios.webservice.resource;

public class Objeto  implements java.io.Serializable {
    private java.lang.String numero;

    private java.lang.String sigla;

    private java.lang.String nome;

    private java.lang.String categoria;

    private java.lang.String erro;

    private br.com.correios.webservice.resource.Eventos[] evento;

    public Objeto() {
    }

    public Objeto(
           java.lang.String numero,
           java.lang.String sigla,
           java.lang.String nome,
           java.lang.String categoria,
           java.lang.String erro,
           br.com.correios.webservice.resource.Eventos[] evento) {
           this.numero = numero;
           this.sigla = sigla;
           this.nome = nome;
           this.categoria = categoria;
           this.erro = erro;
           this.evento = evento;
    }


    /**
     * Gets the numero value for this Objeto.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this Objeto.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the sigla value for this Objeto.
     * 
     * @return sigla
     */
    public java.lang.String getSigla() {
        return sigla;
    }


    /**
     * Sets the sigla value for this Objeto.
     * 
     * @param sigla
     */
    public void setSigla(java.lang.String sigla) {
        this.sigla = sigla;
    }


    /**
     * Gets the nome value for this Objeto.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this Objeto.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the categoria value for this Objeto.
     * 
     * @return categoria
     */
    public java.lang.String getCategoria() {
        return categoria;
    }


    /**
     * Sets the categoria value for this Objeto.
     * 
     * @param categoria
     */
    public void setCategoria(java.lang.String categoria) {
        this.categoria = categoria;
    }


    /**
     * Gets the erro value for this Objeto.
     * 
     * @return erro
     */
    public java.lang.String getErro() {
        return erro;
    }


    /**
     * Sets the erro value for this Objeto.
     * 
     * @param erro
     */
    public void setErro(java.lang.String erro) {
        this.erro = erro;
    }


    /**
     * Gets the evento value for this Objeto.
     * 
     * @return evento
     */
    public br.com.correios.webservice.resource.Eventos[] getEvento() {
        return evento;
    }


    /**
     * Sets the evento value for this Objeto.
     * 
     * @param evento
     */
    public void setEvento(br.com.correios.webservice.resource.Eventos[] evento) {
        this.evento = evento;
    }

    public br.com.correios.webservice.resource.Eventos getEvento(int i) {
        return this.evento[i];
    }

    public void setEvento(int i, br.com.correios.webservice.resource.Eventos _value) {
        this.evento[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Objeto)) return false;
        Objeto other = (Objeto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.sigla==null && other.getSigla()==null) || 
             (this.sigla!=null &&
              this.sigla.equals(other.getSigla()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.categoria==null && other.getCategoria()==null) || 
             (this.categoria!=null &&
              this.categoria.equals(other.getCategoria()))) &&
            ((this.erro==null && other.getErro()==null) || 
             (this.erro!=null &&
              this.erro.equals(other.getErro()))) &&
            ((this.evento==null && other.getEvento()==null) || 
             (this.evento!=null &&
              java.util.Arrays.equals(this.evento, other.getEvento())));
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
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getSigla() != null) {
            _hashCode += getSigla().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getCategoria() != null) {
            _hashCode += getCategoria().hashCode();
        }
        if (getErro() != null) {
            _hashCode += getErro().hashCode();
        }
        if (getEvento() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEvento());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEvento(), i);
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
        new org.apache.axis.description.TypeDesc(Objeto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "objeto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sigla");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sigla"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoria");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categoria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("erro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "erro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("evento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "evento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "evento"));
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
