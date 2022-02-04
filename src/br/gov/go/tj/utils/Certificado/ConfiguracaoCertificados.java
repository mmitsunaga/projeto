package br.gov.go.tj.utils.Certificado;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import br.gov.go.tj.projudi.util.ProjudiPropriedades;

public class ConfiguracaoCertificados {
	
	private static Provider provider;
	private static CertificateFactory factory;
	private static CertPathValidator validador;
	private static String diretorioCertificadosConfiaveis;
	
	private ConfiguracaoCertificados() {
	}
	
	public static Provider getProvider() {
		if (provider == null)
			provider = new BouncyCastleProvider();
		return provider;
	}
	
	public static CertificateFactory getFactory() throws CertificateException {
		if (factory == null)
			factory = CertificateFactory.getInstance("X.509", getProvider());
		return factory;
	}
	
	public static CertPathValidator getValidador() throws CertificateException {
		if (validador == null) {
			try {
				validador = CertPathValidator.getInstance("PKIX", getProvider());
			} catch (NoSuchAlgorithmException e) {
				throw new CertificateException("Erro ao instanciar validador PKIX", e);
			}
		}
		return validador;
	}
	
	public static String getDiretorioCertificadosConfiaveis() throws CertificateException {
		if (diretorioCertificadosConfiaveis == null) {
			String dir = ProjudiPropriedades.getInstance().getCaminhoCAConfiavel();
			if (dir == null || dir.isEmpty())
				throw new CertificateException("Diretório de Certificados Confiáveis não configurado.");
			diretorioCertificadosConfiaveis = dir;
		}
		return diretorioCertificadosConfiaveis;
	}
}
