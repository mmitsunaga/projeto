package br.gov.go.tj.utils.Certificado;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import oracle.sql.ARRAY;

import org.apache.log4j.Logger;

public class CertificadosConfiaveis {
	
	private static final Map<CadeiaDeCertificado, Map<Integer, X509Certificate>> certificadosConfiaveis = new HashMap<>();
	
	private static void carrega(CadeiaDeCertificado cadeia) throws CertificateException {
		if (!certificadosConfiaveis.containsKey(cadeia)) {
			final Logger LOGGER = Logger.getLogger(CertificadosConfiaveis.class);
			Map<Integer, X509Certificate> certificadosCarregados = new HashMap<>();
			String diretorio = ConfiguracaoCertificados.getDiretorioCertificadosConfiaveis();
			if (!diretorio.endsWith(File.separator))
				diretorio += File.separator;
			diretorio += cadeia.getPasta();
			LOGGER.debug("DIRETÓRIO DE CERTIFICADOS CONFIÁVEIS: " + diretorio);
			File[] arquivosLocalizados = new File(diretorio).listFiles();
			if (arquivosLocalizados != null) {
				for (File arquivo : arquivosLocalizados) {
					try (InputStream is = new FileInputStream(arquivo)) {
						X509Certificate certificado = (X509Certificate) ConfiguracaoCertificados.getFactory().generateCertificate(is);
						certificadosCarregados.put(certificado.getSubjectX500Principal().hashCode(), certificado);
						LOGGER.debug("CERTIFICADO CONFIÁVEL CARREGADO: " + arquivo.getName());
					} catch (IOException e) {
						LOGGER.warn("Erro ler arquivo de certificado confiável: " + arquivo.getName(), e);
					}
				}
			}
			if (certificadosCarregados.isEmpty())
				LOGGER.warn("Lista de certificados confiáveis se encontra vazia para a cadeia " + cadeia);
			certificadosConfiaveis.put(cadeia, certificadosCarregados);
		}
	}
	
	public static X509Certificate getEmissorConfiavel(CadeiaDeCertificado cadeia, X509Certificate certificado) throws CertificateException {
		carrega(cadeia);
		X500Principal emissor = certificado.getIssuerX500Principal();
		return certificadosConfiaveis.get(cadeia).get(emissor.hashCode());
	}

	public static List<X509Certificate> getCertificadosConfiaveis(CadeiaDeCertificado cadeia) {
		return new ArrayList<>(certificadosConfiaveis.get(cadeia).values());
	}
	
	private CertificadosConfiaveis() {
	}

}
