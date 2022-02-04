package br.gov.go.tj.pe.oab;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.rpc.Stub;
import javax.xml.transform.stream.StreamSource;

public class CadastroOABProxy {
	
	private final String URL = "http://www5.oab.org.br/cnaws/service.asmx";
	private CadastroOABStub stub;
	
	private static CadastroOABProxy instancia = null;
	
	
	public static void main(String[] args) throws RemoteException{
		//70139615156
		//96586419115
		System.out.println(CadastroOABProxy.get().consultaAdvogadoPorCpf("70139615156")
				.replaceAll("<AdvogadoData>", "\n\t<AdvogadoData>")
				.replaceAll("<ArrayOfAdvogadoData", "\n<ArrayOfAdvogadoData")
				.replaceAll("</ArrayOfAdvogadoData", "\n</ArrayOfAdvogadoData"));
	}

	public static CadastroOABProxy get() {
		if (instancia == null){
			//ProjudiPropriedades projudiPropriedades = ProjudiPropriedades.getInstance();
			//System.setProperty("http.proxyHost", projudiPropriedades.getEnderecoProxy());
			//System.setProperty("http.proxyPort",  String.valueOf(projudiPropriedades.getPortaProxy()));
			instancia = new CadastroOABProxy();
		}
		return instancia;
	}
	
	private CadastroOABProxy() {
	}
	
	private void localizaServico() throws RemoteException {
		try {
			stub = (CadastroOABStub) new LocalizadorCadastroOAB(URL).getStub();
			if (stub != null) {
				stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, URL);
				stub.setTimeout(60000);
			}
		} catch (Exception e) {
			throw new RemoteException("Erro ao localizar serviço.", e);
		}
	}
	
	public List<AdvogadoData> consultaAdvogado(String cpf) throws RemoteException, JAXBException {
		JAXBContext jc = JAXBContext.newInstance(Wrapper.class, AdvogadoData.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		List<AdvogadoData> lista = unmarshal(unmarshaller, AdvogadoData.class, consultaAdvogadoPorCpf(cpf));
		if (lista == null)
			return Collections.emptyList();
		return lista;
	}
	
	private <T> List<T> unmarshal(Unmarshaller unmarshaller, Class<T> clazz, String xml) throws JAXBException {
		StreamSource streamSource = new StreamSource(new StringReader(xml));
		Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(streamSource, Wrapper.class).getValue();
		return wrapper.getItems();
	}
	
	public String consultaAdvogadoPorCpf(String cpf) throws RemoteException {
		if (stub == null)
			localizaServico();
		return stub.consultaAdvogadoPorCpf(cpf); 
	}
	
	public String consultaAdvogado(String inscricao, String uf, String nome) throws RemoteException {
		if (stub == null)
			localizaServico();
		return stub.consultaAdvogado(inscricao, uf, nome);
	}
	
	public byte[] buscaImagemAdvogado(int numeroSeguranca) throws RemoteException {
		if (stub == null)
			localizaServico();
		return stub.buscaImagemAdvogado(numeroSeguranca);
	}
	
}