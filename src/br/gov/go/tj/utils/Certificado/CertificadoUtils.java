/*
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code AND Comments
 */
package br.gov.go.tj.utils.Certificado;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.util.Identidade;

/**
 * @author root
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code AND Comments
 */
public class CertificadoUtils {

    /**
	public static void installBCProvider() {
        CertTools objCertTools = new CertTools();
       // objCertTools.installBCProvider();
    }
	*/
	
    /**
     * Método que administra acriação de um certificado raiz
     * 
     * @param keySize
     * @param serial
     * @param dn
     * @param pontoDeDistribuicao
     * @param alias
     * @param from
     * @param to
     * @return
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public KeyStore criarCertificadoRaiz(int keySize, BigInteger serial, String dn, String pontoDeDistribuicao, String alias, Date from, Date to) throws InvalidKeyException, SignatureException, KeyStoreException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        CertTools objCertTools = new CertTools();
        KeyPair rsaKeys = objCertTools.geraChaves(keySize);
        X509Certificate rootcert = objCertTools.geraCertAutoAssinado(dn, serial, pontoDeDistribuicao, rsaKeys.getPrivate(), rsaKeys.getPublic(), from, to, true);
        return new CertTools().criaP12(alias, rsaKeys.getPrivate(), rootcert, null);
    }

    /**
     * Méodo que administra a criação de um certificado emissor
     * 
     * @param ksRoot
     * @param senhaRaiz
     * @param keySize
     * @param serial
     * @param dn
     * @param pontoDeDistribuicao
     * @param alias
     * @param from
     * @param to
     * @return
     * @throws IOException 
     * @throws InvalidKeySpecException 
     * @throws NoSuchAlgorithmException 
     * @throws NoSuchProviderException 
     * @throws CertificateException 
     * @throws KeyStoreException 
     * @throws Exception
     */
    public KeyStore criarCertificadoEmissor(KeyStore ksRoot, String senhaRaiz, int keySize, BigInteger serial, String dn, String pontoDeDistribuicao, String alias, Date from, Date to) throws Exception{
        CertTools objCertTools = new CertTools();
        KeyPair emissorKeys = objCertTools.geraChaves(keySize);

        String next = (String) ksRoot.aliases().nextElement();
        X509Certificate caCert = (X509Certificate) ksRoot.getCertificate(next);
        PrivateKey caPrivKey = (PrivateKey) ksRoot.getKey(next, senhaRaiz.toCharArray());
        Certificate[] oldChain = ksRoot.getCertificateChain(next);

        GeradorCertificados caGen = new GeradorCertificados(caCert, caPrivKey);
        X509Certificate emissor = caGen.generateCertificate(dn, null, emissorKeys.getPublic(), from, to, serial, "SHA1WithRSAEncryption", "BC", new Emissor(pontoDeDistribuicao));

        return new CertTools().criaP12(alias, emissorKeys.getPrivate(), emissor, oldChain);
    }

    /**
     * Método que cria uma identidade digital, a ser utilizada por um usuário
     * 
     * @param ksRoot
     * @param senhaRaiz
     * @param keySize
     * @param serial
     * @param id
     * @param pontoDeDistribuicao
     * @param from
     * @param to
     * @return
     * @throws IOException 
     * @throws InvalidKeySpecException 
     * @throws NoSuchAlgorithmException 
     * @throws NoSuchProviderException 
     * @throws CertificateException 
     * @throws KeyStoreException 
     * @throws Exception
     */
    
    public KeyStore criarIdentidadeDigital(KeyStore ksRoot, String senhaRaiz, int keySize, BigInteger serial, Identidade id, String pontoDeDistribuicao, Date from, Date to) throws Exception{
        CertTools objCertTools = new CertTools();
        String dn = "C=" + id.getPais() + ", ST=" + id.getEstado() + ", L=" + id.getCidade() + ", CN=" + id.getNome() + ", E=" + id.getEmail();
        KeyPair idKeys = objCertTools.geraChaves(keySize);

        String alias = (String) ksRoot.aliases().nextElement();
        X509Certificate caCert = (X509Certificate) ksRoot.getCertificate(alias);
        PrivateKey caPrivKey = (PrivateKey) ksRoot.getKey(alias, senhaRaiz.toCharArray());
        Certificate[] oldChain = ksRoot.getCertificateChain(alias);

        GeradorCertificados caGen = new GeradorCertificados(caCert, caPrivKey);
        X509Certificate idCert = caGen.generateCertificate(dn, "E=" + id.getEmail(), idKeys.getPublic(), from, to, serial, "SHA1WithRSAEncryption", "BC", new UsuarioFinal(id, pontoDeDistribuicao));

        return new CertTools().criaP12(id.getNome(), idKeys.getPrivate(), idCert, oldChain);
    }

    /**
     * Método que cria lista de certificados expirados antes do tempo, para
     * disponibilização
     * 
     * @param ksRoot
     * @param senhaRaiz
     * @param certs
     * @param crlnumber
     * @param periodo
     * @return
     * @throws Exception 
     * @throws Exception
     */
    public static X509CRL criarLRC(KeyStore ksRoot, String senhaRaiz, List certs, int crlnumber, int periodo) throws Exception{

        String next = (String) ksRoot.aliases().nextElement();
        X509Certificate caCert = (X509Certificate) ksRoot.getCertificate(next);
        PrivateKey caPrivKey = (PrivateKey) ksRoot.getKey(next, senhaRaiz.toCharArray());

        GeradorLRC gen = new GeradorLRC(caCert, caPrivKey);

        X509CRL crl = gen.generateCRL(certs, crlnumber, periodo);

        return crl;
    }

}
