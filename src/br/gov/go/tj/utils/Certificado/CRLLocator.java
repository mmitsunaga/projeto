package br.gov.go.tj.utils.Certificado;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.MensagemException;

public class CRLLocator {

	private static final String OID_CRL_DISTRIBUTION_POINTS = "2.5.29.31";
	private static final  Logger LOGGER = Logger.getLogger(CRLLocator.class);
	private static final Map<String, X509CRLObject> mapaCRLs = new HashMap<String, X509CRLObject>();

	public static boolean temCRL(X509Certificate certificate) {
		return certificate.getExtensionValue(OID_CRL_DISTRIBUTION_POINTS) != null;
	}
	
	/**
	 * 
	 * 
	 * @return UM OBJETO X509CRLObject PARA USO POSTERIOR.
	 */
	public static X509CRLObject getCRL(X509Certificate certificate) throws Exception {
		byte crlExtension[] = certificate.getExtensionValue(OID_CRL_DISTRIBUTION_POINTS);
		if (crlExtension == null) {
			throw new MensagemException("Extensão CRL Distribution Points não encontrada!");
		}
		List<String> urls = new ArrayList<String>();
		Pattern paTeste01 = Pattern.compile("http://(.*?)crl");
		Matcher maTeste01 = paTeste01.matcher(new String(crlExtension));
		while (maTeste01.find()) {
			String url = maTeste01.group();
			if (mapaCRLs.containsKey(url)) {
				X509CRLObject crl = mapaCRLs.get(url);
				if (crl.getNextUpdate().after(new Date()))
					return crl;
				else
					mapaCRLs.remove(url);
			}
			urls.add(url);
		}
		if (urls.isEmpty()) {
			throw new MensagemException("Extensão CRL Distribution Points não possui nenhuma URL!");
		}
		//ProjudiPropriedades projudiPropriedades = ProjudiPropriedades.getInstance();
		//String proxy = projudiPropriedades.getEnderecoProxy();
		//String port = String.valueOf(projudiPropriedades.getPortaProxy());
		//System.setProperty("http.proxyHost", proxy);
		//System.setProperty("http.proxyPort", port);

		for (String url : urls) {
			try {
				HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
				con.setDoOutput(true);
				con.setInstanceFollowRedirects(false);
				con.setRequestMethod("GET");
				con.connect();
				InputStream response = con.getInputStream();
				ASN1InputStream stream = new ASN1InputStream(response);
				CertificateList listaDeCertificadosRevogados = CertificateList.getInstance(stream.readObject());
				X509CRLObject crl = new X509CRLObject(listaDeCertificadosRevogados);
				stream.close();
				response.close();
				con.disconnect();
				mapaCRLs.put(url, crl);
				return crl;
			} catch (Exception e) {
				LOGGER.warn("Lista de certificados revogados não pode obtida em " + url);
			}
		}
		throw new MensagemException("Extensão CRL Distribution Points não conseguiu se conectar a nenhuma URL!");

	}
}
