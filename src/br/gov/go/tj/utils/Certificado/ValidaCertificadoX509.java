package br.gov.go.tj.utils.Certificado;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateRevokedException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.ocsp.BasicOCSPResp;
import org.bouncycastle.ocsp.CertificateID;
import org.bouncycastle.ocsp.CertificateStatus;
import org.bouncycastle.ocsp.OCSPReq;
import org.bouncycastle.ocsp.OCSPReqGenerator;
import org.bouncycastle.ocsp.OCSPResp;
import org.bouncycastle.ocsp.RevokedStatus;
import org.bouncycastle.ocsp.SingleResp;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public abstract class ValidaCertificadoX509 {
	
	private final CadeiaDeCertificado cadeia;
	
	protected ValidaCertificadoX509(CadeiaDeCertificado cadeia) throws CertificateException {
		this.cadeia = cadeia;
	}
	
	public void validaCertificado(X509Certificate certificado) throws CertificateException {
		certificado.checkValidity();
		List<X509Certificate> emissores = getEmissores(certificado);
		for (X509Certificate emissor : emissores)
			emissor.checkValidity();
		try {
			Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
			emissores.stream().forEach(c -> trustAnchors.add(new TrustAnchor(c, null)));
			PKIXParameters parametrosDeValidacao = new PKIXParameters(trustAnchors);
			parametrosDeValidacao.setRevocationEnabled(false);
			CertPath certPath = ConfiguracaoCertificados.getFactory().generateCertPath(Arrays.asList(certificado));
			ConfiguracaoCertificados.getValidador().validate(certPath, parametrosDeValidacao);
			validaRevogacao(certificado);
		} catch (InvalidAlgorithmParameterException | CertPathValidatorException e) {
			throw new CertificateException("Certificado inválido.", e);
		}
	}
	
	public abstract void validaRevogacao(X509Certificate cadeiaDeCertificados) throws CertificateRevokedException;
	
	public List<X509Certificate> getEmissores(X509Certificate certificado) throws CertificateException {
		List<X509Certificate> emissores = new ArrayList<X509Certificate>();
		X509Certificate emissor = getEmissor(certificado);
		while (emissor != null) {
			emissores.add(emissor);
			emissor = getEmissor(emissor);
		}
		return emissores;
	}
	
	private X509Certificate getEmissor(X509Certificate certificado) throws CertificateException {
		if (Arrays.equals(certificado.getSubjectX500Principal().getEncoded(), certificado.getIssuerX500Principal().getEncoded())) {
			return null;
		}
		X509Certificate emissor = CertificadosConfiaveis.getEmissorConfiavel(cadeia, certificado);
		if (emissor == null)
			throw new CertificateException("Certificado não possui emissor confiável. " + certificado.getIssuerDN().getName());
		return emissor;
	}
}
