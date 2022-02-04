package br.gov.go.tj.utils.Certificado;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.fileupload.FileItem;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;

/**
 * @author Leandro de Lima Lira
 * @author André Luis Cavalcanti Moreira
 * Esta classe tem a função de auxiliar no upload de arquivos que devem ser assinados 
 * e cujas assinaturas terão sua autenticidade verificado no sistema.
 * 
 *
 */
public class FileUploadUtils {

	VerificaP7s verificador;

	/**
	 * Médodo construtor
	 * @param caminhoCertConfiaveis Caminho da pasta com lista de certificados raízes confiáveis
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public FileUploadUtils() throws IOException, GeneralSecurityException, Exception {
		verificador = new VerificaP7s();
	}

	public CMSSignedData processaNaoAssinado(String certificationChain, String signature, String arquivo) throws CMSException, CertificateException, IOException {
		byte[] documento = Base64Utils.base64Decode(arquivo);
		return processaNaoAssinado(certificationChain, signature, documento);
	}

	public CMSSignedData processaNaoAssinado(String certificationChain, String signature, FileItem arquivo) throws CMSException, CertificateException, IOException {
		byte[] documento = arquivo.get();
		return processaNaoAssinado(certificationChain, signature, documento);
	}

	/**
	 * Método que verifica a integridade da assinatra e retorna os certificados do(s) usuário(s) assinante(s)
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @param X509Certificate, certificado emissor do projudi
	 * @return Vector, com a cadeia de certificados do(s) usuário(s) assinante(s)
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws Exception 
	 */
	public Vector verifica(CMSSignedData conteudoAssinado) throws Exception{
		return verificador.verifySig(conteudoAssinado);
	}

	private CMSSignedData processaNaoAssinado(String certificationChain, String signature, byte[] documento) throws CMSException, CertificateException, IOException {

		String[] cadiaCertificados = certificationChain.split("@#@");
		ArrayList certList = new ArrayList();

		for (int i = 0; i < cadiaCertificados.length; i++) {
			CertPath path = loadCertPathFromBase64String(cadiaCertificados[i]);
			List certs = path.getCertificates();
			for (Iterator iterator = certs.iterator(); iterator.hasNext();) {
				X509Certificate cert = (X509Certificate) iterator.next();
				certList.add(cert);
			}
		}

		CertPath path = loadCertPathFromBase64String(cadiaCertificados[0]);
		List certs = path.getCertificates();
		X509Certificate cert = (X509Certificate) certs.get(0);
		byte[] sig = Base64Utils.base64Decode(signature);

		AlgorithmIdentifier digAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(CMSSignedDataGenerator.DIGEST_SHA1), new DERNull());
		AlgorithmIdentifier encAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(CMSSignedDataGenerator.ENCRYPTION_RSA), new DERNull());

		ASN1OctetString encDigest = new DEROctetString(sig);
		ByteArrayInputStream bIn = new ByteArrayInputStream(cert.getTBSCertificate());
		try(ASN1InputStream aIn = new ASN1InputStream(bIn)){
			TBSCertificateStructure tbs = TBSCertificateStructure.getInstance(aIn.readObject());
			IssuerAndSerialNumber encSid = new IssuerAndSerialNumber(tbs.getIssuer(), tbs.getSerialNumber().getValue());
			SignerInfo signer = new SignerInfo(new SignerIdentifier(encSid), digAlgId, null, encAlgId, encDigest, null);
	
			CMSProcessable msg = new CMSProcessableByteArray(documento);
	
			return generate(CMSSignedDataGenerator.DATA, msg, "BC", signer, digAlgId, certList, new Vector());
		}
	}

	private CertPath loadCertPathFromBase64String(String aCertChainBase64Encoded) throws CertificateException, IOException {
		byte[] certChainEncoded = Base64Utils.base64Decode(aCertChainBase64Encoded);
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		ByteArrayInputStream certChainStream = new ByteArrayInputStream(certChainEncoded);
		CertPath certPath;
		try{
			certPath = cf.generateCertPath(certChainStream, "PkiPath");
		} finally{
			certChainStream.close();
		}
		return certPath;
	}

	private CMSSignedData generate(String signedContentType, CMSProcessable content, String sigProvider, SignerInfo signer, AlgorithmIdentifier digAlgId, List certList, List CRLList) throws CMSException {
		boolean encapsulate = true;

		ASN1EncodableVector digestAlgs = new ASN1EncodableVector();
		ASN1EncodableVector signerInfos = new ASN1EncodableVector();
		DERObjectIdentifier contentTypeOID = new DERObjectIdentifier(signedContentType);
		digestAlgs.add(digAlgId);
		signerInfos.add(signer);

		ArrayList certs = new ArrayList();
		ArrayList crls = new ArrayList();

		ASN1Set certificates = null;
		Iterator it;
		try{
			it = certList.iterator();
			while (it.hasNext()) {
				X509Certificate c = (X509Certificate) it.next();
				certs.add(new X509CertificateStructure((ASN1Sequence) makeObj(c.getEncoded())));
			}
		} catch(IOException e) {
			throw new CMSException("<{Erro ao iterar lista de certificados.}> Local Exception: " + this.getClass().getName() + ".generate(): " + e.getMessage(), e);
		} catch(CertificateEncodingException e) {
			throw new CMSException("<{Erro ao iterar lista de certificados.}> Local Exception: " + this.getClass().getName() + ".generate(): " + e.getMessage(), e);
		}

		if (certs.size() != 0) {
			ASN1EncodableVector v = new ASN1EncodableVector();
			it = certs.iterator();
			while (it.hasNext()) {
				v.add((DEREncodable) it.next());
			}
			certificates = new DERSet(v);
		}

		ASN1Set certrevlist = null;

		try{
			it = CRLList.iterator();
			while (it.hasNext()) {
				X509CRL c = (X509CRL) it.next();
				crls.add(new CertificateList((ASN1Sequence) makeObj(c.getEncoded())));
			}
		} catch(IOException e) {
			throw new CMSException("<{Erro ao iterar lista de revogação.}> Local Exception: " + this.getClass().getName() + ".generate(): " + e.getMessage(), e);
		} catch(CRLException e) {
			throw new CMSException("<{Erro ao codificar crls.}> Local Exception: " + this.getClass().getName() + ".generate(): " + e.getMessage(), e);
		}

		if (crls.size() != 0) {
			ASN1EncodableVector v = new ASN1EncodableVector();
			it = crls.iterator();
			while (it.hasNext()) {
				v.add((DEREncodable) it.next());
			}
			certrevlist = new DERSet(v);
		}

		ContentInfo encInfo;

		if (encapsulate) {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			try{
				content.write(bOut);
			} catch(IOException e) {
				throw new CMSException("<{encapsulation error.}> Local Exception: " + this.getClass().getName() + ".generate(): " + e.getMessage(), e);
			}

			ASN1OctetString octs = new BERConstructedOctetString(bOut.toByteArray());
			encInfo = new ContentInfo(contentTypeOID, octs);
		} else {
			encInfo = new ContentInfo(contentTypeOID, null);
		}
		SignedData sd = new SignedData(new DERSet(digestAlgs), encInfo, certificates, certrevlist, new DERSet(signerInfos));
		ContentInfo contentInfo = new ContentInfo(PKCSObjectIdentifiers.signedData, sd);

		return new CMSSignedData(content, contentInfo);
	}

	private DERObject makeObj(byte[] encoding) throws IOException {
		if (encoding == null) {return null; }
		ByteArrayInputStream bIn = new ByteArrayInputStream(encoding);
		try(ASN1InputStream aIn = new ASN1InputStream(bIn)){
			return aIn.readObject();
		}
	}

}
