/**
 * Eventos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.correios.webservice.resource;

public class Eventos  implements java.io.Serializable {
    private java.lang.String tipo;

    private java.lang.String status;

    private java.lang.String data;

    private java.lang.String hora;

    private java.lang.String descricao;

    private java.lang.String detalhe;

    private java.lang.String recebedor;

    private java.lang.String documento;

    private java.lang.String comentario;

    private java.lang.String local;

    private java.lang.String codigo;

    private java.lang.String cidade;

    private java.lang.String uf;

    private java.lang.String sto;

    private java.lang.String amazoncode;

    private java.lang.String amazontimezone;

    private br.com.correios.webservice.resource.Destinos[] destino;

    private br.com.correios.webservice.resource.EnderecoMobile endereco;

    public Eventos() {
    }

    public Eventos(
           java.lang.String tipo,
           java.lang.String status,
           java.lang.String data,
           java.lang.String hora,
           java.lang.String descricao,
           java.lang.String detalhe,
           java.lang.String recebedor,
           java.lang.String documento,
           java.lang.String comentario,
           java.lang.String local,
           java.lang.String codigo,
           java.lang.String cidade,
           java.lang.String uf,
           java.lang.String sto,
           java.lang.String amazoncode,
           java.lang.String amazontimezone,
           br.com.correios.webservice.resource.Destinos[] destino,
           br.com.correios.webservice.resource.EnderecoMobile endereco) {
           this.tipo = tipo;
           this.status = status;
           this.data = data;
           this.hora = hora;
           this.descricao = descricao;
           this.detalhe = detalhe;
           this.recebedor = recebedor;
           this.documento = documento;
           this.comentario = comentario;
           this.local = local;
           this.codigo = codigo;
           this.cidade = cidade;
           this.uf = uf;
           this.sto = sto;
           this.amazoncode = amazoncode;
           this.amazontimezone = amazontimezone;
           this.destino = destino;
           this.endereco = endereco;
    }


    /**
     * Gets the tipo value for this Eventos.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this Eventos.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the status value for this Eventos.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Eventos.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the data value for this Eventos.
     * 
     * @return data
     */
    public java.lang.String getData() {
        return data;
    }


    /**
     * Sets the data value for this Eventos.
     * 
     * @param data
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }


    /**
     * Gets the hora value for this Eventos.
     * 
     * @return hora
     */
    public java.lang.String getHora() {
        return hora;
    }


    /**
     * Sets the hora value for this Eventos.
     * 
     * @param hora
     */
    public void setHora(java.lang.String hora) {
        this.hora = hora;
    }


    /**
     * Gets the descricao value for this Eventos.
     * 
     * @return descricao
     */
    public java.lang.String getDescricao() {
        return descricao;
    }


    /**
     * Sets the descricao value for this Eventos.
     * 
     * @param descricao
     */
    public void setDescricao(java.lang.String descricao) {
        this.descricao = descricao;
    }


    /**
     * Gets the detalhe value for this Eventos.
     * 
     * @return detalhe
     */
    public java.lang.String getDetalhe() {
        return detalhe;
    }


    /**
     * Sets the detalhe value for this Eventos.
     * 
     * @param detalhe
     */
    public void setDetalhe(java.lang.String detalhe) {
        this.detalhe = detalhe;
    }


    /**
     * Gets the recebedor value for this Eventos.
     * 
     * @return recebedor
     */
    public java.lang.String getRecebedor() {
        return recebedor;
    }


    /**
     * Sets the recebedor value for this Eventos.
     * 
     * @param recebedor
     */
    public void setRecebedor(java.lang.String recebedor) {
        this.recebedor = recebedor;
    }


    /**
     * Gets the documento value for this Eventos.
     * 
     * @return documento
     */
    public java.lang.String getDocumento() {
        return documento;
    }


    /**
     * Sets the documento value for this Eventos.
     * 
     * @param documento
     */
    public void setDocumento(java.lang.String documento) {
        this.documento = documento;
    }


    /**
     * Gets the comentario value for this Eventos.
     * 
     * @return comentario
     */
    public java.lang.String getComentario() {
        return comentario;
    }


    /**
     * Sets the comentario value for this Eventos.
     * 
     * @param comentario
     */
    public void setComentario(java.lang.String comentario) {
        this.comentario = comentario;
    }


    /**
     * Gets the local value for this Eventos.
     * 
     * @return local
     */
    public java.lang.String getLocal() {
        return local;
    }


    /**
     * Sets the local value for this Eventos.
     * 
     * @param local
     */
    public void setLocal(java.lang.String local) {
        this.local = local;
    }


    /**
     * Gets the codigo value for this Eventos.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this Eventos.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the cidade value for this Eventos.
     * 
     * @return cidade
     */
    public java.lang.String getCidade() {
        return cidade;
    }


    /**
     * Sets the cidade value for this Eventos.
     * 
     * @param cidade
     */
    public void setCidade(java.lang.String cidade) {
        this.cidade = cidade;
    }


    /**
     * Gets the uf value for this Eventos.
     * 
     * @return uf
     */
    public java.lang.String getUf() {
        return uf;
    }


    /**
     * Sets the uf value for this Eventos.
     * 
     * @param uf
     */
    public void setUf(java.lang.String uf) {
        this.uf = uf;
    }


    /**
     * Gets the sto value for this Eventos.
     * 
     * @return sto
     */
    public java.lang.String getSto() {
        return sto;
    }


    /**
     * Sets the sto value for this Eventos.
     * 
     * @param sto
     */
    public void setSto(java.lang.String sto) {
        this.sto = sto;
    }


    /**
     * Gets the amazoncode value for this Eventos.
     * 
     * @return amazoncode
     */
    public java.lang.String getAmazoncode() {
        return amazoncode;
    }


    /**
     * Sets the amazoncode value for this Eventos.
     * 
     * @param amazoncode
     */
    public void setAmazoncode(java.lang.String amazoncode) {
        this.amazoncode = amazoncode;
    }


    /**
     * Gets the amazontimezone value for this Eventos.
     * 
     * @return amazontimezone
     */
    public java.lang.String getAmazontimezone() {
        return amazontimezone;
    }


    /**
     * Sets the amazontimezone value for this Eventos.
     * 
     * @param amazontimezone
     */
    public void setAmazontimezone(java.lang.String amazontimezone) {
        this.amazontimezone = amazontimezone;
    }


    /**
     * Gets the destino value for this Eventos.
     * 
     * @return destino
     */
    public br.com.correios.webservice.resource.Destinos[] getDestino() {
        return destino;
    }


    /**
     * Sets the destino value for this Eventos.
     * 
     * @param destino
     */
    public void setDestino(br.com.correios.webservice.resource.Destinos[] destino) {
        this.destino = destino;
    }

    public br.com.correios.webservice.resource.Destinos getDestino(int i) {
        return this.destino[i];
    }

    public void setDestino(int i, br.com.correios.webservice.resource.Destinos _value) {
        this.destino[i] = _value;
    }


    /**
     * Gets the endereco value for this Eventos.
     * 
     * @return endereco
     */
    public br.com.correios.webservice.resource.EnderecoMobile getEndereco() {
        return endereco;
    }


    /**
     * Sets the endereco value for this Eventos.
     * 
     * @param endereco
     */
    public void setEndereco(br.com.correios.webservice.resource.EnderecoMobile endereco) {
        this.endereco = endereco;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Eventos)) return false;
        Eventos other = (Eventos) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            ((this.hora==null && other.getHora()==null) || 
             (this.hora!=null &&
              this.hora.equals(other.getHora()))) &&
            ((this.descricao==null && other.getDescricao()==null) || 
             (this.descricao!=null &&
              this.descricao.equals(other.getDescricao()))) &&
            ((this.detalhe==null && other.getDetalhe()==null) || 
             (this.detalhe!=null &&
              this.detalhe.equals(other.getDetalhe()))) &&
            ((this.recebedor==null && other.getRecebedor()==null) || 
             (this.recebedor!=null &&
              this.recebedor.equals(other.getRecebedor()))) &&
            ((this.documento==null && other.getDocumento()==null) || 
             (this.documento!=null &&
              this.documento.equals(other.getDocumento()))) &&
            ((this.comentario==null && other.getComentario()==null) || 
             (this.comentario!=null &&
              this.comentario.equals(other.getComentario()))) &&
            ((this.local==null && other.getLocal()==null) || 
             (this.local!=null &&
              this.local.equals(other.getLocal()))) &&
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.cidade==null && other.getCidade()==null) || 
             (this.cidade!=null &&
              this.cidade.equals(other.getCidade()))) &&
            ((this.uf==null && other.getUf()==null) || 
             (this.uf!=null &&
              this.uf.equals(other.getUf()))) &&
            ((this.sto==null && other.getSto()==null) || 
             (this.sto!=null &&
              this.sto.equals(other.getSto()))) &&
            ((this.amazoncode==null && other.getAmazoncode()==null) || 
             (this.amazoncode!=null &&
              this.amazoncode.equals(other.getAmazoncode()))) &&
            ((this.amazontimezone==null && other.getAmazontimezone()==null) || 
             (this.amazontimezone!=null &&
              this.amazontimezone.equals(other.getAmazontimezone()))) &&
            ((this.destino==null && other.getDestino()==null) || 
             (this.destino!=null &&
              java.util.Arrays.equals(this.destino, other.getDestino()))) &&
            ((this.endereco==null && other.getEndereco()==null) || 
             (this.endereco!=null &&
              this.endereco.equals(other.getEndereco())));
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
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        if (getHora() != null) {
            _hashCode += getHora().hashCode();
        }
        if (getDescricao() != null) {
            _hashCode += getDescricao().hashCode();
        }
        if (getDetalhe() != null) {
            _hashCode += getDetalhe().hashCode();
        }
        if (getRecebedor() != null) {
            _hashCode += getRecebedor().hashCode();
        }
        if (getDocumento() != null) {
            _hashCode += getDocumento().hashCode();
        }
        if (getComentario() != null) {
            _hashCode += getComentario().hashCode();
        }
        if (getLocal() != null) {
            _hashCode += getLocal().hashCode();
        }
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getCidade() != null) {
            _hashCode += getCidade().hashCode();
        }
        if (getUf() != null) {
            _hashCode += getUf().hashCode();
        }
        if (getSto() != null) {
            _hashCode += getSto().hashCode();
        }
        if (getAmazoncode() != null) {
            _hashCode += getAmazoncode().hashCode();
        }
        if (getAmazontimezone() != null) {
            _hashCode += getAmazontimezone().hashCode();
        }
        if (getDestino() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDestino());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDestino(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEndereco() != null) {
            _hashCode += getEndereco().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Eventos.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "eventos"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descricao");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descricao"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detalhe");
        elemField.setXmlName(new javax.xml.namespace.QName("", "detalhe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recebedor");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recebedor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comentario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comentario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("uf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amazoncode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "amazoncode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amazontimezone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "amazontimezone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destino");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "destino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "destino"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endereco");
        elemField.setXmlName(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "endereco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resource.webservice.correios.com.br/", "enderecoMobile"));
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
