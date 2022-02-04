/*
 * Created on 07/12/2005
 */
package br.gov.go.tj.utils.Certificado;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.ne.CertificadoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.MensagemException;

public class VerificaP7s {

	/**
	 * Método que verifica a integridade da assinatra e retorna os certificados do(s) usuário(s) assinante(s)
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @param X509Certificate, certificado emissor do projudi
	 * @return Vector, com a cadeia de certificados do(s) usuário(s) assinante(s)
	 * @throws GeneralSecurityException 
	 * @throws CertificateException 
	 * @throws Exception 
	 */
	public Vector verifySig(CMSSignedData conteudoAssinado) throws Exception{
		try{
			Vector<X509Certificate> assinantes = new Vector<X509Certificate>();
			CertStore certs = conteudoAssinado.getCertificatesAndCRLs("Collection", "BC");
			SignerInformationStore signers1 = conteudoAssinado.getSignerInfos();
			Collection c = signers1.getSigners();
			CertTools objCertTools = new CertTools();
			
			int total = c.size();
			int verified = 0;
			Iterator it = c.iterator();

			while (it.hasNext()) {
				SignerInformation signer = (SignerInformation) it.next();
				Collection certCollection = certs.getCertificates(signer.getSID());

				Iterator certIt = certCollection.iterator();
				X509Certificate cert = (X509Certificate) certIt.next();

				if (signer.verify(cert, "BC")) {
					verified++;
				}else {
					throw new MensagemException("Erro ao verificar assinatura. | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
				}
				boolean emitidoProjudi = false;
				String[] teste = cert.getIssuerX500Principal().getName().split("CN=");
				if (teste[1].equals("Projudi - Autoridade Registradora")) {
					emitidoProjudi = true;
				}
				
				// CASO O CERTIFICADO SEJA DO PROJUDI, TESTAMOS A REVOGAÇÃO
				if (emitidoProjudi) {
					try {
						cert.checkValidity();
					} catch (CertificateExpiredException e1) {
						throw new MensagemException("Aquivo assinado com certificado inválido (o período de validade expirou). | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
					} catch (CertificateNotYetValidException e1) {
						throw new MensagemException("Aquivo assinado com certificado inválido (o período de validade ainda não começou). | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
					}
					CertificadoNe certificadoNe = new CertificadoNe();
					CertificadoDt certProjudi = certificadoNe.consultarId(String.valueOf(cert.getSerialNumber()));
					if (certProjudi != null) {
						if (!certProjudi.ehValidoHoje()) {
							StringTokenizer st = new StringTokenizer(cert.getSubjectDN().getName(), ",");
							StringBuffer nome = new StringBuffer("");
							while (st.hasMoreTokens()) {
								String token = st.nextToken();

								if (token.startsWith("CN=")) {
									nome.append(token.substring(3));
								}
							}

							throw new MensagemException("Arquivo assinado com certificado revogado. Nome: " + nome + " | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
						}
					}
					// VERIFICA CADEIA DO CERTIFICADO **************************************
					List chain = new ArrayList(5);
					X509Certificate p = cert;
					chain.add(p);					
					
					while (!objCertTools.ehAutoAssinado(p)) {
						X509CertSelector sel = new X509CertSelector();
						sel.setSubject(p.getIssuerX500Principal().getEncoded());
						p = (X509Certificate) certs.getCertificates(sel).iterator().next();
						chain.add(p);
						if (chain.size() > 150) {
							throw new MensagemException("Tamanho da cadeia de certificação inválido| VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
						}
					}

					if (!verifyCertificationChain(chain)) {
						StringTokenizer st = new StringTokenizer(cert.getSubjectDN().getName(), ",");
						StringBuffer nome = new StringBuffer("");
						while (st.hasMoreTokens()) {
							String token = st.nextToken();

							if (token.startsWith("CN=")) {
								nome.append(token.substring(3));
							}
						}
						throw new MensagemException("Arquivo assinado com certificado não confiável. Nome: " + nome + " | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
					}
					// FIM DA VERIFICACAO DA CADEIA DO CERTIFICADO************
				} else {
					new ValidaCertificadoICPBR().valida(cert);
				}
				assinantes.add(cert);
			}
			if ((verified == total) && total > 0) {
				return assinantes;
			} else {
				throw new MensagemException("Assinatura inválida. | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
			}

		} catch(SignerException e) {
			throw new MensagemException("Erro ao verificar assinatura. | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");

		} catch(InvalidCRLException e) {
			throw new MensagemException("Falha ao gerar a CRL. | VerificaP7s.verifySig(CMSSignedData s, X509Certificate caCert) |");
		}
	}

	/**
	 * Método que verifica a válidade de uma cadeia de certificados emitidos pelo Projudi
	 * @param CertPath, cadeia a ser válidada
	 * @return boolean, verdadeiro se a cadeia for válida ou falso se não for válida
	 * @throws Exception 
	 */
	public boolean verifyCertificationChain(List chain) throws Exception {
		int chainLength = chain.size();
		if (chainLength < 2) {
			return false;
		}
		CertificateFactory cf = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
		List cadeia = chain.subList(0, chain.size() - 1);
		CertPathValidator chainValidator = CertPathValidator.getInstance("PKIX", cf.getProvider());		
		HashSet trustAnchors = new HashSet();				
		
		String dir = ProjudiPropriedades.getInstance().getCaminhoCAConfiavel() + File.separator; 
		//teste para acessar o banco de homolog no desenvolvimento
		if (!(new File(dir).exists())) {
			//estamos acesso o banco de homolog no desenvolvimento
			//logo o diretorio é
			dir ="d:/acs-confiaveis/";
		}
		//System.getProperty("jboss.home.dir") + System.getProperty("file.separator") + "domain" 
		//			+ System.getProperty("file.separator") + "data" + System.getProperty("file.separator");

		// Para apontar para o HOMOLOG, a linha com o diretório D: dev estar descomentada.
		// Caso contrário, utilizar a linha que faz uso da variável dir.
		File diretorio = new File(dir  + "ac-tjgo");
		//File diretorio = new File("d:/acs-confiaveis/ac-tjgo");
		File[] arquivos =  diretorio.listFiles();
		if(arquivos != null){
			for (File arquivo : arquivos) {
				InputStream is = new FileInputStream(arquivo);
				X509Certificate certificado = (X509Certificate) cf.generateCertificate(is);
				is.close();
				trustAnchors.add(new TrustAnchor(certificado, null));
			}
		}
				
		diretorio = new File(dir  + "acs-icpbr");
		arquivos =  diretorio.listFiles();
		if(arquivos != null){
			for (File arquivo : arquivos) {
				InputStream is = new FileInputStream(arquivo);
				X509Certificate certificado = (X509Certificate) cf.generateCertificate(is);
				is.close();
				trustAnchors.add(new TrustAnchor(certificado, null));
			}
		}
		PKIXParameters certPathValidatorParams = new PKIXParameters(trustAnchors);
		certPathValidatorParams.setRevocationEnabled(false);
		try{
			chainValidator.validate(cf.generateCertPath(cadeia), certPathValidatorParams);
		} catch(CertPathValidatorException cpve) {
			return false;
		}
		return true;
	}
	
	/**
	 * Método que retorna os certificados do(s) usuário(s) assinante(s)
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @return Vector, com a cadeia de certificados do(s) usuário(s) assinante(s)
	 * @throws Exception 
	 */
	public List<X509Certificate> getCertificadosAssinantes(CMSSignedData conteudoAssinado) throws Exception {
		List<X509Certificate> assinantes = new ArrayList<X509Certificate>();
		
		CertStore certs = conteudoAssinado.getCertificatesAndCRLs("Collection", "BC");
		SignerInformationStore signers1 = conteudoAssinado.getSignerInfos();
		Collection c = signers1.getSigners();
		CertTools objCertTools = new CertTools();
		
		Iterator it = c.iterator();

		while (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			Collection certCollection = certs.getCertificates(signer.getSID());

			Iterator certIt = certCollection.iterator();
			X509Certificate cert = (X509Certificate) certIt.next();
			
			String[] teste = cert.getIssuerX500Principal().getName().split("CN=");
			boolean emitidoProjudi = teste[1].equals("Projudi - Autoridade Registradora");
			
			if (emitidoProjudi) {
				X509Certificate p = cert;
				try {
					while (!objCertTools.ehAutoAssinado(p)) {
						X509CertSelector sel = new X509CertSelector();
						sel.setSubject(((X509Principal) p.getIssuerDN()).getEncoded());
						p = (X509Certificate) certs.getCertificates(sel).iterator().next();
						
						if (!certificadoExisteNaLista(assinantes, p, objCertTools)) assinantes.add(p);						
					}
				} catch (IOException e) {}
			} 

			if (!certificadoExisteNaLista(assinantes, cert, objCertTools)) assinantes.add(cert);
		}
		
		return assinantes;
	}
	
	private boolean certificadoExisteNaLista(List<X509Certificate> assinantes, X509Certificate certificado, CertTools objCertTools) {
		
		for (X509Certificate cert : assinantes) {
			if (objCertTools.getSubjectDN(cert).equals(objCertTools.getSubjectDN(certificado))) return true;
		}
		
		return false;
	}
	
	/**
	 * Método para pegar os assinantes do arquivo
	 * @param CMSSignedData, signedRaw conteúdo assinado
	 * @param X509Certificate, certificado emissor do projudi
	 * @return Vector, com a cadeia de certificados do(s) usuário(s) assinante(s)
	 */
	public Vector getSigners(CMSSignedData conteudoAssinado) throws Exception{
		
		Vector<X509Certificate> assinantes = new Vector<X509Certificate>();
		CertStore certs = conteudoAssinado.getCertificatesAndCRLs("Collection", "BC");
		SignerInformationStore signers1 = conteudoAssinado.getSignerInfos();
		Collection c = signers1.getSigners();
		CertTools objCertTools = new CertTools();
		
		int total = c.size();
		int verified = 0;
		Iterator it = c.iterator();

		while (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			Collection certCollection = certs.getCertificates(signer.getSID());

			Iterator certIt = certCollection.iterator();
			X509Certificate cert = (X509Certificate) certIt.next();

			assinantes.add(cert);
		}
	
		return assinantes;
				
	}
}
