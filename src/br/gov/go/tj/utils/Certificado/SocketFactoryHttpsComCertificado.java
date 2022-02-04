package br.gov.go.tj.utils.Certificado;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SocketFactoryHttpsComCertificado implements ProtocolSocketFactory {

	private SSLContext ssl = null;
	//TODO preencher dados sobre certificado digital
	private static final String SENHA_CERTIFICADO = "SENHA";
	private static final String ARQUIVO_CERTIFICADO = "ARQUIVO";
	private static final String CERTIFICADO_CONFIAVEL = "ARQUIVO";
	private static SocketFactoryHttpsComCertificado instancia;
	
	public static void iniciar() throws Exception{
		if(instancia == null){
			SocketFactoryHttpsComCertificado temp = new SocketFactoryHttpsComCertificado();
			Protocol protocol = new Protocol("https", temp, 443);
			Protocol.registerProtocol("https", protocol);
			instancia = temp;
		}
	}

	private SocketFactoryHttpsComCertificado() throws Exception{
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		keyStore.load(new FileInputStream(ARQUIVO_CERTIFICADO), SENHA_CERTIFICADO.toCharArray());
		String alias = "";
		Enumeration<String> aliasesEnum = keyStore.aliases();
		while (aliasesEnum.hasMoreElements()) {
			alias = (String) aliasesEnum.nextElement();
			if (keyStore.isKeyEntry(alias))
				break;
		}
		final X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
		final PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, SENHA_CERTIFICADO.toCharArray());
		KeyManager[] keyManagers = new KeyManager[]{new X509KeyManager(){

			public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
				return certificate.getIssuerDN().getName();
			}

			public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
				return null;
			}

			public X509Certificate[] getCertificateChain(String arg0) {
				return new X509Certificate[]{certificate};
			}

			public String[] getClientAliases(String arg0, Principal[] arg1) {
				return new String[]{certificate.getIssuerDN().getName()};
			}

			public PrivateKey getPrivateKey(String arg0) {
				return privateKey;
			}

			public String[] getServerAliases(String arg0, Principal[] arg1) {
				return null;
			}
		}};
		TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager(){

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType)throws CertificateException {
				verifica(certs[0]);
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException{
				verifica(certs[0]);
			}
			
			private void verifica(X509Certificate certificado) throws CertificateException{
				try{
					InputStream is = new FileInputStream(CERTIFICADO_CONFIAVEL);
					CertificateFactory factory = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
					X509Certificate confiavel = (X509Certificate) factory.generateCertificate(is);
					is.close();
					if (!Arrays.equals(certificado.getSubjectX500Principal().getEncoded(), confiavel.getSubjectX500Principal().getEncoded())) {
						throw new CertificateException("Certificado fornecido não é confiável");
					}
				} catch(IOException e) {
					throw new CertificateException("Certificado confiável não localizado");
				}
			}
		}};
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagers, trustManagers, null);
		ssl = sslContext;
	}

	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = ssl.getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		}

		Socket socket = socketfactory.createSocket();
		SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
		SocketAddress remoteaddr = new InetSocketAddress(host, port);
		socket.bind(localaddr);
		try{
			socket.connect(remoteaddr, timeout);
		} catch(Exception e) {
			throw new ConnectTimeoutException("Possível timeout de conexão", e);
		}

		return socket;
	}

	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
		return ssl.getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return ssl.getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
		return ssl.getSocketFactory().createSocket(socket, host, port, autoClose);
	}

}