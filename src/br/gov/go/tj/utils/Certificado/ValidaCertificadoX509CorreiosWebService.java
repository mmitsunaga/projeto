package br.gov.go.tj.utils.Certificado;

import java.security.cert.CertificateException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;

public class ValidaCertificadoX509CorreiosWebService extends ValidaCertificadoX509 {
	
	public ValidaCertificadoX509CorreiosWebService() throws CertificateException {
		super(CadeiaDeCertificado.CORREIOS_WEBSERVICE);
	}

	@Override
	public void validaRevogacao(X509Certificate certificado) throws CertificateRevokedException {
		// Não verificar pois a conexão exige um certificado específico.
	}	
}
