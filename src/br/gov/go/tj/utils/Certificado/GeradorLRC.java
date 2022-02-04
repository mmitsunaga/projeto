package br.gov.go.tj.utils.Certificado;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V2CRLGenerator;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.utils.Funcoes;

/**
 * 
 * @author Leandro de Lima Lira
 * @author André Luis Cavalcanti Moreira
 * Esta classe tem a função de auxliar na criação do arquivo de LRC, que
 * representa a lista de certificados revogados antes do tempo de expiração
 *
 */
public class GeradorLRC implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 4079091573360723603L;
    //private X509Name subjectx509name = null;
	X509Certificate esteCert;

	PrivateKey keyPrivate;

	boolean useAuthorityKeyIdentifier;
	boolean authorityKeyIdentifierCritical;
	boolean useCRLNumber;
	boolean CRLNumberCritical;
	
	public GeradorLRC(X509Certificate ca,PrivateKey privateKey, boolean useAuthorityKeyIdentifier,   boolean authorityKeyIdentifierCritical ) {
		esteCert = ca;
		this.keyPrivate = privateKey;
		this.useAuthorityKeyIdentifier = useAuthorityKeyIdentifier;
		this.authorityKeyIdentifierCritical = authorityKeyIdentifierCritical;
		useCRLNumber = true;
		CRLNumberCritical = false;
	}

	/**
	 * Método construtor
	 * @param ca
	 * @param privateKey
	 */
	public GeradorLRC(X509Certificate ca,PrivateKey privateKey) {
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
	 * Mpetodo que gera a lista de certificados revogados propriamete dita
	 * @param certs
	 * @param crlnumber
	 * @param CRLPeriod
	 * @return
	 * @throws Exception
	 */
	public X509CRL generateCRL(List certs, int crlnumber, int CRLPeriod)	throws Exception {
		String sigAlg = "SHA1WithRSA";

		Date thisUpdate = new Date();
		Date nextUpdate = new Date();

		// CRLPeriod is minutos = CRLPeriod*60*1000 milliseconds
		nextUpdate.setTime(nextUpdate.getTime() + (CRLPeriod * 60 * 1000));
		X509V2CRLGenerator crlgen = new X509V2CRLGenerator();
		crlgen.setThisUpdate(thisUpdate);
		crlgen.setNextUpdate(nextUpdate);
		crlgen.setSignatureAlgorithm(sigAlg);
		// Make DNs
		X509Name caname = new X509Name(getSubjectDN());
		crlgen.setIssuerDN(caname);
		if (certs != null) {
			Iterator it = certs.iterator();
			while (it.hasNext()) {
				//Certificado certinfo = (Certificado)it.next();
				CertificadoDt certinfo = (CertificadoDt)it.next();
				crlgen.addCRLEntry(new BigInteger(String.valueOf(certinfo.getId())),Funcoes.DataHora(certinfo.getDataRevogacao()), Funcoes.StringToInt(certinfo.getMotivoRevogacao()));
			}
		}

		// Authority key identifier
		if (useAuthorityKeyIdentifier) {
			try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(getPublicKey().getEncoded()))){
				SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) asn1InputStream.readObject());
				AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(apki);
				crlgen.addExtension(X509Extensions.AuthorityKeyIdentifier.getId(),getAuthorityKeyIdentifierCritical(),aki);
			}
		}
		// CRLNumber extension
		/*if (useCRLNumber) {
			CRLNumber crlnum = new CRLNumber(BigFuncoes.StringToInt(crlnumber));
			crlgen.addExtension(X509Extensions.CRLNumber.getId(),	getCRLNumberCritical(),	crlnum);
		}*/

		X509CRL crl = crlgen.generateX509CRL(getPrivateKey(), "BC");

		crl.verify(getPublicKey());

		return (X509CRL) crl;
	}

	public boolean getAuthorityKeyIdentifierCritical() {
		return authorityKeyIdentifierCritical;
	}

	public boolean getCRLNumberCritical() {
		return CRLNumberCritical;
	}

}
