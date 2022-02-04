/**
 * Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.correios.webservice.resource;

public interface Service extends java.rmi.Remote {
    public br.com.correios.webservice.resource.Sroxml buscaEventos(java.lang.String usuario, java.lang.String senha, java.lang.String tipo, java.lang.String resultado, java.lang.String lingua, java.lang.String objetos) throws java.rmi.RemoteException;
    public br.com.correios.webservice.resource.Sroxml buscaEventosLista(java.lang.String usuario, java.lang.String senha, java.lang.String tipo, java.lang.String resultado, java.lang.String lingua, java.lang.String[] objetos) throws java.rmi.RemoteException;
}
