/**
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.gov.go.tj.pe.oab;

import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import org.apache.axis.AxisEngine;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

class CadastroOABStub extends Stub implements CadastroOAB {
	
	static OperationDesc[] _operations;
	
	static {
		_operations = new OperationDesc[3];
		_initOperationDesc1();
	}
	
	private static void _initOperationDesc1() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new OperationDesc();
		oper.setName("ConsultaAdvogadoPorCpf");
		param = new ParameterDesc(new QName("http://tempuri.org/", "cpf"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"),
				String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "ConsultaAdvogadoPorCpfResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;
		
		oper = new OperationDesc();
		oper.setName("ConsultaAdvogado");
		param = new ParameterDesc(new QName("http://tempuri.org/", "inscricao"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"),
				String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "uf"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"),
				String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("http://tempuri.org/", "nome"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "string"),
				String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName("http://tempuri.org/", "ConsultaAdvogadoResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[1] = oper;
		
		oper = new OperationDesc();
		oper.setName("BuscaImagemAdvogado");
		param = new ParameterDesc(new QName("http://tempuri.org/", "numeroSeguranca"), ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema", "int"),
				int.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
		oper.setReturnClass(byte[].class);
		oper.setReturnQName(new QName("http://tempuri.org/", "BuscaImagemAdvogadoResult"));
		oper.setStyle(Style.WRAPPED);
		oper.setUse(Use.LITERAL);
		_operations[2] = oper;
		
	}
	
	CadastroOABStub(URL endpointURL, javax.xml.rpc.Service service) throws AxisFault {
		if (service == null) {
			super.service = new Service();
		}
		else {
			super.service = service;
		}
		((Service) super.service).setTypeMappingVersion("1.2");
		super.cachedEndpoint = endpointURL;
	}
	
	protected Call createCall() throws RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			java.util.Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			return _call;
		} catch (Throwable _t) {
			throw new AxisFault("Failure trying to get the Call object", _t);
		}
	}
	
	public String consultaAdvogadoPorCpf(String cpf) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ConsultaAdvogadoPorCpf");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "ConsultaAdvogadoPorCpf"));
		
		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { cpf });
			
			if (_resp instanceof RemoteException) {
				throw (RemoteException) _resp;
			}
			else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}
	
	public String consultaAdvogado(String inscricao, String uf, String nome) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[1]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/ConsultaAdvogado");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "ConsultaAdvogado"));
		
		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { inscricao, uf, nome });
			
			if (_resp instanceof RemoteException) {
				throw (RemoteException) _resp;
			}
			else {
				extractAttachments(_call);
				try {
					return (String) _resp;
				} catch (Exception _exception) {
					return (String) JavaUtils.convert(_resp, String.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}
	
	public byte[] buscaImagemAdvogado(int numeroSeguranca) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		Call _call = createCall();
		_call.setOperation(_operations[2]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("http://tempuri.org/BuscaImagemAdvogado");
		_call.setEncodingStyle(null);
		_call.setProperty(Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("http://tempuri.org/", "BuscaImagemAdvogado"));
		
		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { new Integer(numeroSeguranca) });
			
			if (_resp instanceof RemoteException) {
				throw (RemoteException) _resp;
			}
			else {
				extractAttachments(_call);
				try {
					return (byte[]) _resp;
				} catch (Exception _exception) {
					return (byte[]) JavaUtils.convert(_resp, byte[].class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}
	
}
