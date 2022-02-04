package br.gov.go.tj.utils.Certificado;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
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

import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ValidaCertificadoICPBR {
	
	private CertificateFactory factory;
	private CertPathValidator validador;
	private List<X509Certificate> certificadosConfiaveis = new ArrayList<X509Certificate>();
	
	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		ValidaCertificadoICPBR vc = new ValidaCertificadoICPBR("C:\\Users\\lsrbsilva\\workspace\\wildfly-8.1.0.Final\\standalone\\data\\");
		for (File f: new File("C:\\certificados").listFiles())
			try {
				vc.teste(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void teste(File arquivo) throws Exception{
		try (InputStream is = new FileInputStream(arquivo)) {
			X509Certificate certificate = (X509Certificate) factory.generateCertificate(is); 
			System.out.println(certificate.getSubjectDN().getName()+ ":");
			valida(certificate);
			System.out.println(" OK");
		}
	}
	
	public ValidaCertificadoICPBR() throws Exception{
		this(ProjudiPropriedades.getInstance().getCaminhoCAConfiavel() + File.separator);
	}
	
	private ValidaCertificadoICPBR(String diretorioCertificadosConfiaveis) throws Exception {
		BouncyCastleProvider bcp = new BouncyCastleProvider();
		factory = CertificateFactory.getInstance("X.509", bcp);
		validador = CertPathValidator.getInstance("PKIX", bcp);
		File diretorio = new File(diretorioCertificadosConfiaveis + "acs-icpbr");
		File[] arquivos = diretorio.listFiles();
		if (arquivos != null) {
			for (File arquivo : arquivos) {
				try (InputStream is = new FileInputStream(arquivo)) {
					certificadosConfiaveis.add((X509Certificate) factory.generateCertificate(is));
				}
			}
		}
	}
	
	// Diretorio no servidor de produção e homologação
	private static final String DIRETORIO_CERTIFICADOS_REJEITADOS = "/opt/wildfly/wildfly/tmp/";
	
	private void salvaCertificadoRecusado(X509Certificate certificate) {
		if (new File(DIRETORIO_CERTIFICADOS_REJEITADOS).exists()){
			try (FileOutputStream fos = new FileOutputStream(DIRETORIO_CERTIFICADOS_REJEITADOS + certificate.getSubjectDN().getName() + ".crt")) {
				fos.write(certificate.getEncoded());
			} catch (Exception e) {
				Logger.getLogger(ValidaCertificadoICPBR.class).warn("Erro ao salvar arquivo de certificado recusado.", e);
			}
		}
	}
	
	private static final String OID_CERTIFICATE_AUTHORITY_INFORMATION_ACCESS = "1.3.6.1.5.5.7.1.1";
	
	public void valida(X509Certificate certificate) throws Exception {
		try {
			certificate.checkValidity();
			testeDeRevogacao(certificate);
		} catch (Exception e) {
			salvaCertificadoRecusado(certificate);
			throw e;
		}
	}
	
	public void validaRevogacao(X509Certificate certificate) throws Exception {
		try {
			testeDeRevogacao(certificate);
		} catch (Exception e) {
			salvaCertificadoRecusado(certificate);
			throw e;
		}
	}
	
	private void testeDeRevogacao(X509Certificate certificate) throws Exception {
		List<X509Certificate> cadeia = montaCadeia(certificate);
		byte ocspExtension[] = certificate.getExtensionValue(OID_CERTIFICATE_AUTHORITY_INFORMATION_ACCESS);
		boolean verificaCRL = true;
		if (ocspExtension != null) {
			try {
				CertificateID id = new CertificateID(CertificateID.HASH_SHA1, cadeia.get(1), certificate.getSerialNumber());
				OCSPReqGenerator gen = new OCSPReqGenerator();
				gen.addRequest(id);
				OCSPReq request = gen.generate();
				byte[] array = request.getEncoded();
				URL url = null;
				ASN1Sequence asn1Seq = (ASN1Sequence) X509ExtensionUtil.fromExtensionValue(ocspExtension);
				Enumeration<?> objects = asn1Seq.getObjects();
				while (objects.hasMoreElements()) {
					ASN1Sequence obj = (ASN1Sequence) objects.nextElement(); // AccessDescription
					DERObjectIdentifier oid = (DERObjectIdentifier) obj.getObjectAt(0); // accessMethod
					DERTaggedObject location = (DERTaggedObject) obj.getObjectAt(1); // accessLocation
					if (location.getTagNo() == GeneralName.uniformResourceIdentifier) {
						DEROctetString uri = (DEROctetString) location.getObject();
						if (oid.equals(X509ObjectIdentifiers.id_ad_ocsp)) {
							url = new URL(new String(uri.getOctets()));
							break;
						}
					}
				}
				if(url != null){
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestProperty("Content-Type", "application/ocsp-request");
					con.setRequestProperty("Accept", "application/ocsp-response");
					con.setDoOutput(true);
					OutputStream out = con.getOutputStream();
					DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
					dataOut.write(array);
					dataOut.flush();
					dataOut.close();
					if (con.getResponseCode() != HttpStatus.SC_OK) {
						throw new MensagemException("ERRO HTTP " + con.getResponseCode() + " AO CONSULTAR REVOGAÇÃO DE CERTIFICADO em " + url);
					}
					InputStream in = (InputStream) con.getContent();
					OCSPResp ocspResponse = new OCSPResp(in);
					if (ocspResponse.getStatus() != 0) {
						throw new MensagemException("ERRO STATUS OCSP - " + ocspResponse.getResponseObject());
					}
					BasicOCSPResp basicResponse = (BasicOCSPResp) ocspResponse.getResponseObject();
					if (basicResponse != null) {
						SingleResp[] responses = basicResponse.getResponses();
						if (responses.length == 1) {
							SingleResp resp = responses[0];
							Object status = resp.getCertStatus();
							if (status != CertificateStatus.GOOD) {
								if (status instanceof RevokedStatus) {
									String dataRevogacao = Funcoes.FormatarData(((RevokedStatus) status).getRevocationTime());
									throw new MensagemException("Certificado revogado em " + dataRevogacao + ".");
								}
								else {
									throw new MensagemException("Certificado inválido! STATUS OCSP:" + status);
								}
							}
							verificaCRL = false;
						}
					}
				}
			} catch (Exception e){
				Logger.getLogger(ValidaCertificadoICPBR.class).debug("Erro ao tentar acessar OCSP", e);
			}
		}
		if(verificaCRL) {
			for (X509Certificate c : cadeia) {
				if (CRLLocator.temCRL(c)) {
					X509CRLObject X509Crl = CRLLocator.getCRL(c);
					if (X509Crl.isRevoked(c)) {
						X509CRLEntry revogado = X509Crl.getRevokedCertificate(c);
						String dataRevogacao = Funcoes.FormatarData(revogado.getRevocationDate());
						throw new MensagemException("Certificado revogado em " + dataRevogacao + ".");
					}
				}
				else {
					throw new MensagemException("Extensões OCSP e CRL não encontradas ou incompletas!");
				}
			}
		}
		Set<TrustAnchor> certificadosConfiaveis = new HashSet<TrustAnchor>();
		for (X509Certificate certificadoConfiavel : cadeia.subList(1, cadeia.size())) {
			certificadosConfiaveis.add(new TrustAnchor(certificadoConfiavel, null));
		}
		PKIXParameters parametros = new PKIXParameters(certificadosConfiaveis);
		parametros.setRevocationEnabled(false);
		validador.validate(factory.generateCertPath(cadeia.subList(0, 1)), parametros);
	}
	
	private List<X509Certificate> montaCadeia(X509Certificate certificado) throws Exception {
		List<X509Certificate> cadeia = new ArrayList<X509Certificate>();
		while (certificado != null) {
			cadeia.add(certificado);
			certificado = getEmissor(certificado);
		}
		return cadeia;
	}
	
	private X509Certificate getEmissor(X509Certificate certificado) throws Exception {
		if (Arrays.equals(certificado.getSubjectX500Principal().getEncoded(), certificado.getIssuerX500Principal().getEncoded())) {
			return null;
		}
		for (X509Certificate confiavel : certificadosConfiaveis) {
			if (Arrays.equals(certificado.getIssuerX500Principal().getEncoded(), confiavel.getSubjectX500Principal().getEncoded())) {
				return confiavel;
			}
		}
		throw new MensagemException("Certificado fornecido não possui emissor confiável. " + certificado.getIssuerDN().getName());
	}
}
