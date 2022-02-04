/**
 * EnderecoMobile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.correios.webservice.resource;

public class EnderecoMobile  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.String cep;

    private java.lang.String logradouro;

    private java.lang.String complemento;

    private java.lang.String numero;

    private java.lang.String localidade;

    private java.lang.String uf;

    private java.lang.String bairro;

    private java.lang.String latitude;

    private java.lang.String longitude;

    private java.lang.String celular;

    public EnderecoMobile() {
    }

    public EnderecoMobile(
           java.lang.String codigo,
           java.lang.String cep,
           java.lang.String logradouro,
           java.lang.String complemento,
           java.lang.String numero,
           java.lang.String localidade,
           java.lang.String uf,
           java.lang.String bairro,
           java.lang.String latitude,
           java.lang.String longitude,
           java.lang.String celular) {
           this.codigo = codigo;
           this.cep = cep;
           this.logradouro = logradouro;
           this.complemento = complemento;
           this.numero = numero;
           this.localidade = localidade;
           this.uf = uf;
           this.bairro = bairro;
           this.latitude = latitude;
           this.longitude = longitude;
           this.celular = celular;
    }


    /**
     * Gets the codigo value for this EnderecoMobile.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this EnderecoMobile.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the cep value for this EnderecoMobile.
     * 
     * @return cep
     */
    public java.lang.String getCep() {
        return cep;
    }


    /**
     * Sets the cep value for this EnderecoMobile.
     * 
     * @param cep
     */
    public void setCep(java.lang.String cep) {
        this.cep = cep;
    }


    /**
     * Gets the logradouro value for this EnderecoMobile.
     * 
     * @return logradouro
     */
    public java.lang.String getLogradouro() {
        return logradouro;
    }


    /**
     * Sets the logradouro value for this EnderecoMobile.
     * 
     * @param logradouro
     */
    public void setLogradouro(java.lang.String logradouro) {
        this.logradouro = logradouro;
    }


    /**
     * Gets the complemento value for this EnderecoMobile.
     * 
     * @return complemento
     */
    public java.lang.String getComplemento() {
        return complemento;
    }


    /**
     * Sets the complemento value for this EnderecoMobile.
     * 
     * @param complemento
     */
    public void setComplemento(java.lang.String complemento) {
        this.complemento = complemento;
    }


    /**
     * Gets the numero value for this EnderecoMobile.
     * 
     * @return numero
     */
    public java.lang.String getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this EnderecoMobile.
     * 
     * @param numero
     */
    public void setNumero(java.lang.String numero) {
        this.numero = numero;
    }


    /**
     * Gets the localidade value for this EnderecoMobile.
     * 
     * @return localidade
     */
    public java.lang.String getLocalidade() {
        return localidade;
    }


    /**
     * Sets the localidade value for this EnderecoMobile.
     * 
     * @param localidade
     */
    public void setLocalidade(java.lang.String localidade) {
        this.localidade = localidade;
    }


    /**
     * Gets the uf value for this EnderecoMobile.
     * 
     * @return uf
     */
    public java.lang.String getUf() {
        return uf;
    }


    /**
     * Sets the uf value for this EnderecoMobile.
     * 
     * @param uf
     */
    public void setUf(java.lang.String uf) {
        this.uf = uf;
    }


    /**
     * Gets the bairro value for this EnderecoMobile.
     * 
     * @return bairro
     */
    public java.lang.String getBairro() {
        return bairro;
    }


    /**
     * Sets the bairro value for this EnderecoMobile.
     * 
     * @param bairro
     */
    public void setBairro(java.lang.String bairro) {
        this.bairro = bairro;
    }


    /**
     * Gets the latitude value for this EnderecoMobile.
     * 
     * @return latitude
     */
    public java.lang.String getLatitude() {
        return latitude;
    }


    /**
     * Sets the latitude value for this EnderecoMobile.
     * 
     * @param latitude
     */
    public void setLatitude(java.lang.String latitude) {
        this.latitude = latitude;
    }


    /**
     * Gets the longitude value for this EnderecoMobile.
     * 
     * @return longitude
     */
    public java.lang.String getLongitude() {
        return longitude;
    }


    /**
     * Sets the longitude value for this EnderecoMobile.
     * 
     * @param longitude
     */
    public void setLongitude(java.lang.String longitude) {
        this.longitude = longitude;
    }


    /**
     * Gets the celular value for this EnderecoMobile.
     * 
     * @return celular
     */
    public java.lang.String getCelular() {
        return celular;
    }


    /**
     * Sets the celular value for this EnderecoMobile.
     * 
     * @param celular
     */
    public void setCelular(java.lang.String celular) {
        this.celular = celular;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EnderecoMobile)) return false;
        EnderecoMobile other = (EnderecoMobile) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.cep==null && other.getCep()==null) || 
             (this.cep!=null &&
              this.cep.equals(other.getCep()))) &&
            ((this.logradouro==null && other.getLogradouro()==null) || 
             (this.logradouro!=null &&
              this.logradouro.equals(other.getLogradouro()))) &&
            ((this.complemento==null && other.getComplemento()==null) || 
             (this.complemento!=null &&
              this.complemento.equals(other.getComplemento()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.localidade==null && other.getLocalidade()==null) || 
             (this.localidade!=null &&
              this.localidade.equals(other.getLocalidade()))) &&
            ((this.uf==null && other.getUf()==null) || 
             (this.uf!=null &&
              this.uf.equals(other.getUf()))) &&
            ((this.bairro==null && other.getBairro()==null) || 
             (this.bairro!=null &&
              this.bairro.equals(other.getBairro()))) &&
            ((this.latitude==null && other.getLatitude()==null) || 
             (this.latitude!=null &&
              this.latitude.equals(other.getLatitude()))) &&
            ((this.longitude==null && other.getLongitude()==null) || 
             (this.longitude!=null &&
              this.longitude.equals(other.getLongitude()))) &&
            ((this.celular==null && other.getCelular()==null) || 
             (this.celular!=null &&
              this.celular.equals(other.getCelular())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getCep() != null) {
            _hashCode += getCep().hashCode();
        }
        if (getLogradouro() != null) {
            _hashCode += getLogradouro().hashCode();
        }
        if (getComplemento() != null) {
            _hashCode += getComplemento().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getLocalidade() != null) {
            _hashCode += getLocalidade().hashCode();
        }
        if (getUf() != null) {
            _hashCode += getUf().hashCode();
        }
        if (getBairro() != null) {
            _hashCode += getBairro().hashCode();
        }
        if (getLatitude() != null) {
            _hashCode += getLatitude().hashCode();
        }
        if (getLongitude() != null) {
            _hashCode += getLongitude().hashCode();
        }
        if (getCelular() != null) {
            _hashCode += getCelular().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnderecoMobile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "enderecoMobile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cep"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logradouro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "logradouro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("complemento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "complemento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localidade");
        elemField.setXmlName(new javax.xml.namespace.QName("", "localidade"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bairro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bairro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("", "latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("", "longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("celular");
        elemField.setXmlName(new javax.xml.namespace.QName("", "celular"));
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
