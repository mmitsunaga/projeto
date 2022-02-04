package br.gov.go.tj.utils.Certificado;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.X509NameTokenizer;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class CertTools {

    private static Logger log = Logger.getLogger(CertTools.class);

    public static final String EMAIL = "rfc822name";
    public static final String EMAIL1 = "email";
    public static final String EMAIL2 = "EmailAddress";
    public static final String EMAIL3 = "E";
    public static final String DNS = "dNSName";
    public static final String URI = "uniformResourceIdentifier";
    public static final String URI1 = "uri";
    public static final String IPADDR = "iPAddress";

    private static final String[] EMAILIDS = {EMAIL, EMAIL1, EMAIL2, EMAIL3 };
    public static final DERObjectIdentifier unstructuredName = new DERObjectIdentifier("1.2.840.113549.1.9.2");
    public static final DERObjectIdentifier unstructuredAddress = new DERObjectIdentifier("1.2.840.113549.1.9.8");

    /**
     * Não deve ser instanciado
     */
    public CertTools() {
    }

    private static final HashMap oids = new HashMap();

    static {
        oids.put("c", X509Name.C);
        oids.put("dc", X509Name.DC);
        oids.put("st", X509Name.ST);
        oids.put("l", X509Name.L);
        oids.put("o", X509Name.O);
        oids.put("ou", X509Name.OU);
        oids.put("t", X509Name.T);
        oids.put("surname", X509Name.SURNAME);
        oids.put("initials", X509Name.INITIALS);
        oids.put("givenname", X509Name.GIVENNAME);
        oids.put("gn", X509Name.GIVENNAME);
        oids.put("sn", X509Name.SN);
        oids.put("serialnumber", X509Name.SN);
        oids.put("cn", X509Name.CN);
        oids.put("uid", X509Name.UID);
        oids.put("emailaddress", X509Name.EmailAddress);
        oids.put("e", X509Name.EmailAddress);
        oids.put("email", X509Name.EmailAddress);
        oids.put("1.2.840.113549.1.9.2", unstructuredName); // unstructuredName
        oids.put("1.2.840.113549.1.9.8", unstructuredAddress); // unstructuredAddress
    }

    private static final String[] dNsEmOrdemDireta = {"1.2.840.113549.1.9.8", "1.2.840.113549.1.9.2", "emailaddress", "e", "email", "uid", "cn", "sn", "serialnumber", "gn", "givenname", "initials", "surname", "t", "ou", "o", "l", "st", "dc", "c" };
    //private static final String[] dNsEmOrdemReversa = {"c", "dc", "st", "l", "o", "ou", "t", "surname", "initials", "givenname", "gn", "serialnumber", "sn", "cn", "uid", "email", "e", "emailaddress", "1.2.840.113549.1.9.2", "1.2.840.113549.1.9.8" };

    /** Mudar caso se queira na ordem reversa */
    private static final String[] dNObjects = dNsEmOrdemDireta;

    private static DERObjectIdentifier getOid(String o) {
        return (DERObjectIdentifier) oids.get(o.toLowerCase());
    }

    public X509Name stringToBcX509Name(String dn) {
        ArrayList oldordering = new ArrayList();
        ArrayList oldvalues = new ArrayList();
        X509NameTokenizer xt = new X509NameTokenizer(dn);
        while (xt.hasMoreTokens()) {
            String pair = xt.nextToken();
            int ix = pair.indexOf("=");

            if (ix != -1) {
                oldordering.add(pair.substring(0, ix).toLowerCase());
                oldvalues.add(pair.substring(ix + 1));
            } else {
                // ???
            }
        }
        Vector ordering = new Vector();
        Vector values = new Vector();
        int index = -1;
        for (int i = 0; i < dNObjects.length; i++) {
            String object = dNObjects[i];
            while ((index = oldordering.indexOf(object)) != -1) {
                DERObjectIdentifier oid = getOid(object);
                if (oid != null) {
                    ordering.add(oid);
                    values.add(oldvalues.remove(index));
                    oldordering.remove(index);
                    index = -1;
                }
            }
        }
        return new X509Name(ordering, values);
    }

    public String stringToBCDNString(String dn) {
        String ret = stringToBcX509Name(dn).toString();
        return ret;
    }

    public String getEmailFromDN(String dn) {
        String email = null;
        for (int i = 0; (i < EMAILIDS.length) && (email == null); i++) {
            email = getPartFromDN(dn, EMAILIDS[i]);
        }
        return email;
    }

    public String getPartFromDN(String dn, String dnpart) {
        String part = null;
        if ((dn != null) && (dnpart != null)) {
            String o;
            dnpart += "="; // we search for 'CN=' etc.

            X509NameTokenizer xt = new X509NameTokenizer(dn);

            while (xt.hasMoreTokens()) {
                o = xt.nextToken();
                if ((o.length() > dnpart.length()) && o.substring(0, dnpart.length()).equalsIgnoreCase(dnpart)) {
                    part = o.substring(dnpart.length());
                    break;
                }
            }
        }
        return part;
    }

    /**
     * Retorna o DN do dono de um certificado
     * 
     * @param cert
     * @return a String DN
     */
    public String getSubjectDN(X509Certificate cert) {
        return getDN(cert, 1);
    }

    /**
     * Retorna o DN do emissor de um certificado
     * 
     * @param cert
     * @return a String DN
     */
    public String getIssuerDN(X509Certificate cert) {
        return getDN(cert, 2);
    }

    private String getDN(X509Certificate cert, int which) {
        String dn = null;
        if (cert == null) {
            return dn;
        }
        try{
            CertificateFactory cf = getCertificateFactory();
            X509Certificate x509cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cert.getEncoded()));
            if (which == 1) {
                dn = x509cert.getSubjectDN().toString();
            } else {
                dn = x509cert.getIssuerDN().toString();
            }
        } catch(CertificateException ce) {
            log.error("CertificateException: ", ce);
            return null;
        }
        return stringToBCDNString(dn);
    }

    /**
     * Retorna o DN do emissor de um LRC
     * 
     * @param crl
     * @return a String DN
     */
    public String getIssuerDN(X509CRL crl) {
        String dn = null;
        try{
            CertificateFactory cf = getCertificateFactory();
            X509CRL x509crl = (X509CRL) cf.generateCRL(new ByteArrayInputStream(crl.getEncoded()));
            dn = x509crl.getIssuerDN().toString();
        } catch(CRLException ce) {
            log.error("CRLException: ", ce);
            return null;
        }
        return stringToBCDNString(dn);
    }

    public CertificateFactory getCertificateFactory() {
        try{
            return CertificateFactory.getInstance("X.509", "BC");
        } catch(NoSuchProviderException nspe) {
            log.error("NoSuchProvider: ", nspe);
        } catch(CertificateException ce) {
            log.error("CertificateException: ", ce);
        }
        return null;
    }

    /**
     * Checks if a certificate is self signed by verifying if subject AND issuer
     * are the same.
     * 
     * @param cert
     *            the certificate that skall be checked.
     * 
     * @return boolean true if the certificate has the same issuer AND subject,
     *         false otherwise.
     */
    public boolean ehAutoAssinado(X509Certificate cert) {

        boolean ret = getSubjectDN(cert).equals(getIssuerDN(cert));

        return ret;
    } // isSelfSigned

    /**
     * Cria um arquivo de guarda de chave privada e certificado padrão PKCS#12
     * 
     * @param alias
     *            o nome da chave (alias)
     * @param privKey
     *            a chave privada
     * @param cert
     *            o certificado a ser guardado
     * @param cachain
     *            o caminho de certificação
     * @return um keystore com os valores fornecidos
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public KeyStore criaP12(String alias, PrivateKey privKey, X509Certificate cert, Certificate[] cachain) throws IOException, KeyStoreException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Certificate chain
        if (cert == null) {
            throw new IllegalArgumentException("<{Parâmetro cert não pode ser null.}> Local Exception: " + this.getClass().getName() + ".criaP12()");
        }
        int len = 1;
        if (cachain != null) {
            len += cachain.length;
        }
        Certificate[] chain = new Certificate[len];
        CertificateFactory cf = getCertificateFactory();
        chain[0] = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cert.getEncoded()));
        if (cachain != null) {
            for (int i = 0; i < cachain.length; i++) {
                X509Certificate tmpcert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cachain[i].getEncoded()));
                chain[i + 1] = tmpcert;
            }
        }
        if (chain.length > 1) {
            for (int i = 1; i < chain.length; i++) {
                X509Certificate cacert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(chain[i].getEncoded()));
                // Set attributes on CA-cert
                PKCS12BagAttributeCarrier caBagAttr = (PKCS12BagAttributeCarrier) cacert;
                // We constuct a friendly name for the CA, AND try with some
                // parts FROM the DN if they exist.
                String cafriendly = getPartFromDN(getSubjectDN(cacert), "CN");
                // On the ones below we +i to make it unique, O might not be
                // otherwise
                if (cafriendly == null) {
                    cafriendly = getPartFromDN(getSubjectDN(cacert), "O");
                    if(cafriendly != null)
                    	cafriendly += i;
                }
                if (cafriendly == null) {
                    cafriendly = getPartFromDN(getSubjectDN(cacert), "OU" + i);
                }
                if (cafriendly == null) {
                    cafriendly = "CA_unknown" + i;
                }
                caBagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString(cafriendly));
            }
        }

        // Set attributes on user-cert
        PKCS12BagAttributeCarrier certBagAttr = (PKCS12BagAttributeCarrier) chain[0];
        certBagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString(alias));
        // in this case we just set the local key id to that of the public key
        certBagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, createSubjectKeyId(chain[0].getPublicKey()));
        // "Clean" private key, i.e. remove any old attributes
        KeyFactory keyfact = KeyFactory.getInstance(privKey.getAlgorithm(), "BC");
        keyfact.generatePrivate(new PKCS8EncodedKeySpec(privKey.getEncoded()));
        // Set attributes for private key
        PKCS12BagAttributeCarrier keyBagAttr = (PKCS12BagAttributeCarrier) privKey;
        // in this case we just set the local key id to that of the public key
        keyBagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString(alias));
        keyBagAttr.setBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, createSubjectKeyId(chain[0].getPublicKey()));
        // store the key AND the certificate chain
        KeyStore store = KeyStore.getInstance("PKCS12", "BC");
        store.load(null, null);
        store.setKeyEntry(alias, privKey, null, chain);
        log.debug("createP12: alias=" + alias + ", privKey, cert=" + getSubjectDN(cert));
        return store;
    }

    /**
     * Cria um subject key identifier com base na chave pública
     * 
     * @param pubKey
     *            a chave pública
     * @return uma estrutura ASN.1 de SubjectKeyIdentifer
     */
    public SubjectKeyIdentifier createSubjectKeyId(PublicKey pubKey) {
        try{
            ByteArrayInputStream bIn = new ByteArrayInputStream(pubKey.getEncoded());
            try(ASN1InputStream asn1InputStream = new ASN1InputStream(bIn)){
				SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence) asn1InputStream.readObject());
	            return new SubjectKeyIdentifier(info);
            }
        } catch(Exception e) {
            throw new RuntimeException("<{error creating key}> Local Exception: " + this.getClass().getName() + ".createSubjectKeyId()");
        }
    }

    /**
     * Gera um par de chaves pública e privada
     * 
     * @param keysize
     *            o tamanho da chave
     * @return o par de chaves
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public KeyPair geraChaves(int keysize) throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", "BC");
        keygen.initialize(keysize);

        KeyPair rsaKeys = keygen.generateKeyPair();

        log.debug("Generated " + rsaKeys.getPublic().getAlgorithm() + " keys with length " + ((RSAPrivateKey) rsaKeys.getPrivate()).getPrivateExponent().bitLength());

        return rsaKeys;
    }

    /**
     * Gera um certificado raiz
     * 
     * @param dn
     * @param serial
     * @param pontoDeDistribuicao
     * @param privKey
     * @param pubKey
     * @param firstDate
     * @param lastDate
     * @param isCA
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchProviderException
     * @throws SecurityException
     * @throws SignatureException
     * @throws IllegalStateException 
     * @throws CertificateEncodingException 
     */
    public X509Certificate geraCertAutoAssinado(String dn, BigInteger serial, String pontoDeDistribuicao, PrivateKey privKey, PublicKey pubKey, Date firstDate, Date lastDate, boolean isCA) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SecurityException, SignatureException, CertificateEncodingException, IllegalStateException {

        // Create self signed certificate
        String sigAlg = "SHA1WithRSA";

        X509V3CertificateGenerator certgen = new X509V3CertificateGenerator();
        certgen.setSerialNumber(serial);
        certgen.setNotBefore(firstDate);
        certgen.setNotAfter(lastDate);
        certgen.setSignatureAlgorithm(sigAlg);
        certgen.setSubjectDN(stringToBcX509Name(dn));
        certgen.setIssuerDN(stringToBcX509Name(dn));
        certgen.setPublicKey(pubKey);

        // Basic constranits is always critical AND MUST be present at-least in
        // CA-certificates.
        BasicConstraints bc = new BasicConstraints(isCA);
        certgen.addExtension(X509Extensions.BasicConstraints.getId(), true, bc);

        // Put critical KeyUsage in CA-certificates
        if (isCA == true) {
            int keyusage = X509KeyUsage.keyCertSign + X509KeyUsage.cRLSign + X509KeyUsage.dataEncipherment;
            X509KeyUsage ku = new X509KeyUsage(keyusage);
            certgen.addExtension(X509Extensions.KeyUsage.getId(), true, ku);
        }

        // Subject AND Authority key identifier is always non-critical AND MUST
        // be present for certificates to verify in Mozilla.
        try{
            if (isCA == true) {
                try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(pubKey.getEncoded()))){
					SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) asn1InputStream.readObject());
	                SubjectKeyIdentifier ski = new SubjectKeyIdentifier(spki);
	
	                SubjectPublicKeyInfo apki = new SubjectPublicKeyInfo((ASN1Sequence) asn1InputStream.readObject());
	                AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(apki);
	
	                certgen.addExtension(X509Extensions.SubjectKeyIdentifier.getId(), false, ski);
	                certgen.addExtension(X509Extensions.AuthorityKeyIdentifier.getId(), false, aki);
                }
            }
        } catch(IOException e) {// do nothing
        }

        // CertificatePolicies extension if supplied policy ID, always
        // non-critical
        PolicyInformation pi = new PolicyInformation(new DERObjectIdentifier("2.5.29.32.0"));
        DERSequence seq = new DERSequence(pi);
        certgen.addExtension(X509Extensions.CertificatePolicies.getId(), false, seq);

        StringTokenizer tokenizer = new StringTokenizer(pontoDeDistribuicao, ";", false);
        ArrayList distpoints = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            // 6 is URI
            String uri = tokenizer.nextToken();
            GeneralName gn = new GeneralName(6, new DERIA5String(uri));
            // log.debug("Added CRL distpoint: "+uri);
            ASN1EncodableVector vec = new ASN1EncodableVector();
            vec.add(gn);
            GeneralNames gns = new GeneralNames(new DERSequence(vec));
            DistributionPointName dpn = new DistributionPointName(0, gns);
            distpoints.add(new DistributionPoint(dpn, null, null));
        }
        if (distpoints.size() > 0) {
            CRLDistPoint ext = new CRLDistPoint((DistributionPoint[]) distpoints.toArray(new DistributionPoint[0]));
            certgen.addExtension(X509Extensions.CRLDistributionPoints.getId(), false, ext);
        }

        X509Certificate selfcert = certgen.generateX509Certificate(privKey, "BC");

        return selfcert;
    }

    /**
     * Recupera a authority key identifier de um dado certificado com a extensão
     * 
     * @param cert
     *            o certificado que contém a extensão
     * @return byte[] contendo a authority key identifier
     * @throws IOException
     */
    public byte[] getAuthorityKeyId(X509Certificate cert) throws IOException {
        byte[] extvalue = cert.getExtensionValue("2.5.29.35");
        if (extvalue == null) {
            return null;
        }
        try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(extvalue))){
			DEROctetString oct = (DEROctetString) (asn1InputStream.readObject());
			try(ASN1InputStream asn1InputStream2 = new ASN1InputStream(new ByteArrayInputStream(oct.getOctets()))){
				AuthorityKeyIdentifier keyId = new AuthorityKeyIdentifier((ASN1Sequence) asn1InputStream2.readObject());
		        return keyId.getKeyIdentifier();
			}
        }
    }

    /**
     * Recupera a subject key identifier de um dado certificado com a extensão
     * 
     * @param cert
     *            o certificado que contém a extensão
     * @return byte[] contendo a subject key identifier
     * @throws IOException
     */
    public byte[] getSubjectKeyId(X509Certificate cert) throws IOException {
        byte[] extvalue = cert.getExtensionValue("2.5.29.14");
        if (extvalue == null) {
            return null;
        }
        try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(extvalue))){
			ASN1OctetString str = ASN1OctetString.getInstance(asn1InputStream.readObject());
	        try(ASN1InputStream asn1InputStream2 = new ASN1InputStream(new ByteArrayInputStream(str.getOctets()))){
				SubjectKeyIdentifier keyId = SubjectKeyIdentifier.getInstance(asn1InputStream2.readObject());
		        extvalue = null;
		        return keyId.getKeyIdentifier();
    		}
    	}
    }

    /**
     * Recupera a política de certificação de um dado certificado com a extensão
     * de política de certificação
     * 
     * @param cert
     *            o certificado que contém a extensão
     * @param pos
     *            a posição da política
     * @return uma String com o OID da política
     * @throws IOException
     */
    public String getCertificatePolicyId(X509Certificate cert, int pos) throws IOException {
        byte[] extvalue = cert.getExtensionValue(X509Extensions.CertificatePolicies.getId());
        if (extvalue == null) {
            return null;
        }
        try(ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(extvalue))){
			DEROctetString oct = (DEROctetString) (asn1InputStream.readObject());
			try(ASN1InputStream asn1InputStream2 = new ASN1InputStream(new ByteArrayInputStream(oct.getOctets()))){
				ASN1Sequence seq = (ASN1Sequence) asn1InputStream2.readObject();
		        // Check the size so we don't ArrayIndexOutOfBounds
		        extvalue = null;
		        if (seq.size() < pos + 1) {
		            return null;
		        }
		        PolicyInformation pol = new PolicyInformation((ASN1Sequence) seq.getObjectAt(pos));
		        String id = pol.getPolicyIdentifier().getId();
		        return id;
			}
        }
    }

    /**
     * Pega o ponto de distribuição de um certificado
     * 
     * @param certificate
     * @return
     * @throws CertificateParsingException
     */
    public String getPontoDeDistribuicao(X509Certificate certificate) throws CertificateParsingException {
        try{
            DERObject obj = getExtensionValue(certificate, X509Extensions.CRLDistributionPoints.getId());
            if (obj == null) {
                return null;
            }
            ASN1Sequence distributionPoints = (ASN1Sequence) obj;
            for (int i = 0; i < distributionPoints.size(); i++) {
                ASN1Sequence distrPoint = (ASN1Sequence) distributionPoints.getObjectAt(i);
                for (int j = 0; j < distrPoint.size(); j++) {
                    ASN1TaggedObject tagged = (ASN1TaggedObject) distrPoint.getObjectAt(j);
                    if (tagged.getTagNo() == 0) {
                        String url = getStringFromGeneralNames(tagged.getObject());
                        if (url != null) {
                            return url;
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new CertificateParsingException("Local Exception: " + this.getClass().getName() + ".getPontoDeDistribuicao(): " + e.toString());
        }
        return null;
    }

    private DERObject getExtensionValue(X509Certificate cert, String oid) throws IOException {
        byte[] bytes = cert.getExtensionValue(oid);
        if (bytes == null) {
            return null;
        }
        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
        ASN1OctetString octs = (ASN1OctetString) aIn.readObject();
        aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
        bytes = null;
        return aIn.readObject();
    }

    private  String getStringFromGeneralNames(DERObject names) {
        ASN1Sequence namesSequence = ASN1Sequence.getInstance((ASN1TaggedObject) names, false);
        if (namesSequence.size() == 0) {
            return null;
        }
        DERTaggedObject taggedObject = (DERTaggedObject) namesSequence.getObjectAt(0);
        return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets());
    }

}
