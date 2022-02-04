package br.com.correios.webservice.resource;

public class ServiceProxy implements br.com.correios.webservice.resource.Service {
  private String _endpoint = null;
  private br.com.correios.webservice.resource.Service service = null;
  
  public ServiceProxy() {
    _initServiceProxy();
  }
  
  public ServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initServiceProxy();
  }
  
  private void _initServiceProxy() {
    try {
      service = (new br.com.correios.webservice.resource.RastroLocator()).getServicePort();
      if (service != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)service)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)service)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (service != null)
      ((javax.xml.rpc.Stub)service)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.correios.webservice.resource.Service getService() {
    if (service == null)
      _initServiceProxy();
    return service;
  }
  
  public br.com.correios.webservice.resource.Sroxml buscaEventos(java.lang.String usuario, java.lang.String senha, java.lang.String tipo, java.lang.String resultado, java.lang.String lingua, java.lang.String objetos) throws java.rmi.RemoteException{
    if (service == null)
      _initServiceProxy();
    return service.buscaEventos(usuario, senha, tipo, resultado, lingua, objetos);
  }
  
  public br.com.correios.webservice.resource.Sroxml buscaEventosLista(java.lang.String usuario, java.lang.String senha, java.lang.String tipo, java.lang.String resultado, java.lang.String lingua, java.lang.String[] objetos) throws java.rmi.RemoteException{
    if (service == null)
      _initServiceProxy();
    return service.buscaEventosLista(usuario, senha, tipo, resultado, lingua, objetos);
  }
  
  
}