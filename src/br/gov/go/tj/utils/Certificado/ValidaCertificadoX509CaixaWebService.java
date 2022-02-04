package br.gov.go.tj.utils.Certificado;

import java.security.cert.CertificateException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.X509Certificate;
import java.util.List;


public class ValidaCertificadoX509CaixaWebService extends ValidaCertificadoX509 {
	
	public ValidaCertificadoX509CaixaWebService() throws CertificateException {
		super(CadeiaDeCertificado.CAIXA_WEBSERVICE);
	}

	@Override
	public void validaRevogacao(X509Certificate certificado) throws CertificateRevokedException {
		// Não verificar pois a conexão exige um certificado específico.
	}
	
}
