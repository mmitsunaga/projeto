package br.gov.go.tj.projudi.ne.boletos.caixa;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import br.gov.go.tj.utils.Certificado.ValidaCertificadoX509CaixaWebService;

public class CaixaTrustManager implements X509TrustManager {
	
	public X509Certificate[] getAcceptedIssuers() {
		// Método desnecessário.
		return null;
	}
	
	public void checkClientTrusted(X509Certificate[] certs, String t) throws CertificateException {
		throw new CertificateException("CONEXÃO NÃO PERMITIDA.");
	}
	
	public void checkServerTrusted(X509Certificate[] certs, String t) throws CertificateException {
		new ValidaCertificadoX509CaixaWebService().validaCertificado(certs[0]);
	}
}
