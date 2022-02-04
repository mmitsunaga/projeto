package br.gov.go.tj.utils.Certificado;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.x509.X509V3CertificateGenerator;



/**
 * 
 * @author Leandro de Lima Lira
 * @author André Luis Cavalcanti Moreira
 * Esta classe tem a função de auxiliar na criação/geração prorpiamente dita 
 * dos arquivos de certificado
 *
 */
public class GeradorCertificados implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 6144464260116559458L;
    private X509Name subjectx509name = null;
	X509Certificate esteCert;

	PrivateKey keyPrivate;

	boolean useAuthorityKeyIdentifier;
	boolean authorityKeyIdentifierCritical;
	
	/**
	 * Método construtor
	 * @param ca
	 * @param privateKey
	 * @param useAuthorityKeyIdentifier
	 * @param authorityKeyIdentifierCritical
	 */
	public GeradorCertificados(X509Certificate ca,PrivateKey privateKey, boolean useAuthorityKeyIdentifier,   boolean authorityKeyIdentifierCritical ) {
		esteCert = ca;
		this.keyPrivate = privateKey;
		this.useAuthorityKeyIdentifier = useAuthorityKeyIdentifier;
		this.authorityKeyIdentifierCritical = authorityKeyIdentifierCritical;
	}

	public GeradorCertificados(X509Certificate ca,PrivateKey privateKey) {
		this(ca,privateKey,true,false);		
	}
		
	PublicKey getPublicKey() {
		return esteCert.getPublicKey();
	}
	
	PrivateKey getPrivateKey() {
		return keyPrivate;
	}
	
	String getSubjectDN() {
		return esteCert.getSubjectDN().getName();
	}

	/**
	 * Método que gera o certificado
	 * @param subjectDN
	 * @param subjectAltName
	 * @param publicKey
	 * @param firstDate
	 * @param lastDate
	 * @param serial
	 * @param algoritmo
	 * @param provider
	 * @param certProfile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public X509Certificate generateCertificate(String subjectDN,String subjectAltName,PublicKey publicKey,Date firstDate,Date lastDate,BigInteger serial,String algoritmo,String provider,	CertificadoConfig certProfile)	throws Exception {

		final String sigAlg = algoritmo;
		CertTools objCertTools = new CertTools();
		
		X509V3CertificateGenerator certgen = new X509V3CertificateGenerator();
		certgen.setSerialNumber(serial);
		certgen.setNotBefore(firstDate);
		certgen.setNotAfter(lastDate);
		certgen.setSignatureAlgorithm(sigAlg);

		String dn = subjectDN;

		certgen.setSubjectDN(objCertTools.stringToBcX509Name(dn));
		X509Name caname = getSubjectDNAsX509Name();
		certgen.setIssuerDN(caname);
		certgen.setPublicKey(publicKey);

		// Basic constranits, all subcerts are NOT CAs
		if (certProfile.getUseBasicConstraints() == true) {
			boolean isCA = false;
			if ((certProfile.getType() == CertificadoConfig.TYPE_SUBCA)	|| (certProfile.getType() == CertificadoConfig.TYPE_ROOTCA))
				isCA = true;
			BasicConstraints bc = new BasicConstraints(isCA);
			certgen.addExtension(X509Extensions.BasicConstraints.getId(),certProfile.getBasicConstraintsCritical(),bc);
		}
		// Key usage
		int newKeyUsage = sunKeyUsageToBC(certProfile.getKeyUsage());
		if ((certProfile.getUseKeyUsage() == true) && (newKeyUsage >= 0)) {
			X509KeyUsage ku = new X509KeyUsage(newKeyUsage);
			certgen.addExtension(X509Extensions.KeyUsage.getId(),certProfile.getKeyUsageCritical(),ku);
		}
		// Extended Key usage
		if (certProfile.getUseExtendedKeyUsage() == true) {
			// Get extended key usage FROM certificate profile
			Collection c = certProfile.getExtendedKeyUsageAsOIDStrings();
			Vector usage = new Vector();
			Iterator iter = c.iterator();
			while (iter.hasNext())
				usage.add(new DERObjectIdentifier((String) iter.next()));
			ExtendedKeyUsage eku = new ExtendedKeyUsage(usage);
			// Extended Key Usage may be either critical OR non-critical
			certgen.addExtension(X509Extensions.ExtendedKeyUsage.getId(),certProfile.getExtendedKeyUsageCritical(),eku);
		}
		// Subject key identifier
		if (certProfile.getUseSubjectKeyIdentifier() == true) {
			try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(publicKey.getEncoded()))){
				SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) asn1InputStream.readObject());
				SubjectKeyIdentifier ski = new SubjectKeyIdentifier(spki);
				certgen.addExtension(X509Extensions.SubjectKeyIdentifier.getId(),certProfile.getSubjectKeyIdentifierCritical(),ski);
			}
		}
		// Authority key identifier
		if (certProfile.getUseAuthorityKeyIdentifier() == true) {
			try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(getPublicKey().getEncoded()))){
				SubjectPublicKeyInfo apki =	new SubjectPublicKeyInfo((ASN1Sequence) asn1InputStream.readObject());
				AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(apki);
				certgen.addExtension(X509Extensions.AuthorityKeyIdentifier.getId(),certProfile.getAuthorityKeyIdentifierCritical(),	aki);
			}
		}
		// Subject Alternative name
		if ((certProfile.getUseSubjectAlternativeName() == true)) {

			DEREncodableVector vec = new DEREncodableVector();
			
			if((subjectAltName != null)	&& (subjectAltName.length() > 0)) {
				String email = objCertTools.getEmailFromDN(subjectAltName);
				if (email != null) {
					GeneralName gn = new GeneralName(1,new DERIA5String(email));
					vec.add(gn);
				}
				String dns = objCertTools.getPartFromDN(subjectAltName, CertTools.DNS);
				if (dns != null) {
					GeneralName gn = new GeneralName(2,new DERIA5String(dns));
					vec.add(gn);
				}
				String uri = objCertTools.getPartFromDN(subjectAltName, CertTools.URI);
				if (uri == null) {
					uri = objCertTools.getPartFromDN(subjectAltName, CertTools.URI1);
				}
				if (uri != null) {
					GeneralName gn = new GeneralName(6,new DERIA5String(uri));
					vec.add(gn);
				}
				String ipstr = objCertTools.getPartFromDN(subjectAltName, CertTools.IPADDR);
				if (ipstr != null) {
					byte[] ipoctets = StringTools.ipStringToOctets(ipstr);
					GeneralName gn = new GeneralName(7,new DEROctetString(ipoctets));
					vec.add(gn);
				}		
			}
			
			// ICP Brasil ------------------------------------------------------------------------
			certProfile.nomeAlternativoICPBrasil(vec);
			
			
			if (vec.size() > 0) {
				GeneralNames san = new GeneralNames(new DERSequence(vec));
				certgen.addExtension(X509Extensions.SubjectAlternativeName.getId(),	certProfile.getSubjectAlternativeNameCritical(),san);
			}
		}

		// Certificate Policies
		if (certProfile.getUseCertificatePolicies() == true) {
			PolicyInformation pi = new PolicyInformation(new DERObjectIdentifier(certProfile.getCertificatePolicyId()));
			DERSequence seq = new DERSequence(pi);
			certgen.addExtension(X509Extensions.CertificatePolicies.getId(),certProfile.getCertificatePoliciesCritical(),seq);
		}

		// CRL Distribution point URI
		if (certProfile.getUseCRLDistributionPoint() == true) {
			// Multiple CDPs are spearated with the ';' sign
			StringTokenizer tokenizer = new StringTokenizer(certProfile.getCRLDistributionPointURI(),";",false);
			ArrayList distpoints = new ArrayList();
			while (tokenizer.hasMoreTokens()) {
				// 6 is URI
				String uri = tokenizer.nextToken();
				GeneralName gn = new GeneralName(6,new DERIA5String(uri));
				//log.debug("Added CRL distpoint: "+uri);
				ASN1EncodableVector vec = new ASN1EncodableVector();
				vec.add(gn);
				GeneralNames gns = new GeneralNames(new DERSequence(vec));
				DistributionPointName dpn = new DistributionPointName(0, gns);
				distpoints.add(new DistributionPoint(dpn, null, null));
			}
			if (distpoints.size() > 0) {
				CRLDistPoint ext = new CRLDistPoint((DistributionPoint[]) distpoints.toArray(new DistributionPoint[0]));
				certgen.addExtension(X509Extensions.CRLDistributionPoints.getId(),certProfile.getCRLDistributionPointCritical(),ext);
			}
		}
		// Authority Information Access (OCSP url)
		if (certProfile.getUseOCSPServiceLocator() == true) {
			String ocspUrl = certProfile.getOCSPServiceLocatorURI();
			// OCSP access location is a URL (GeneralName no 6)
			GeneralName ocspLocation = new GeneralName(6,new DERIA5String(ocspUrl));
			certgen.addExtension(X509Extensions.AuthorityInfoAccess.getId(),false,new AuthorityInformationAccess(X509ObjectIdentifiers.ocspAccessMethod,ocspLocation));
		}

		X509Certificate cert = certgen.generateX509Certificate(getPrivateKey(), provider);

		// Verify before returning
		cert.verify(getPublicKey());

		return (X509Certificate) cert;
	}

	private int sunKeyUsageToBC(boolean[] sku) {
		int bcku = 0;
		if (sku[0] == true) bcku = bcku | X509KeyUsage.digitalSignature;
		if (sku[1] == true) bcku = bcku | X509KeyUsage.nonRepudiation;
		if (sku[2] == true) bcku = bcku | X509KeyUsage.keyEncipherment;
		if (sku[3] == true) bcku = bcku | X509KeyUsage.dataEncipherment;
		if (sku[4] == true) bcku = bcku | X509KeyUsage.keyAgreement;
		if (sku[5] == true) bcku = bcku | X509KeyUsage.keyCertSign;
		if (sku[6] == true) bcku = bcku | X509KeyUsage.cRLSign;
		if (sku[7] == true) bcku = bcku | X509KeyUsage.encipherOnly;
		if (sku[8] == true) bcku = bcku | X509KeyUsage.decipherOnly;
		return bcku;
	}

	private X509Name getSubjectDNAsX509Name() {
	    CertTools objCertTools = new CertTools();
		if (subjectx509name == null) {
			subjectx509name = objCertTools.stringToBcX509Name(getSubjectDN());
		}

		return subjectx509name;
	}

	public boolean getAuthorityKeyIdentifierCritical() {
		return authorityKeyIdentifierCritical;
	}

}
