package br.gov.go.tj.utils.Certificado;
import java.util.ArrayList;


/**
 * 
 * @author Leandro de Lima Lira
 * @author Andr� Luis Cavalcanti Moreira
 * Esta classe tem a fun��o de auxiliar na cria��o e manipula��o de um certificado emissor
 *
 */
public class Emissor extends CertificadoConfig {

	/**
     * 
     */
    private static final long serialVersionUID = -719112413254602294L;

    /**
	 * M�todo construtor para um certificado emissor
	 * @param pontoDistribuicao Ponto de distribui��o do certificado
	 */
	public Emissor(String pontoDistribuicao) {

		setValidity(730);

		setUseBasicConstraints(true);
		setBasicConstraintsCritical(true);

		setUseSubjectKeyIdentifier(true);
		setSubjectKeyIdentifierCritical(false);

		setUseAuthorityKeyIdentifier(true);
		setAuthorityKeyIdentifierCritical(false);

		setUseSubjectAlternativeName(true);
		setSubjectAlternativeNameCritical(false);

		setUseCRLDistributionPoint(true);
		setCRLDistributionPointCritical(false);
		setCRLDistributionPointURI(pontoDistribuicao);

		setUseCertificatePolicies(false);
		setCertificatePoliciesCritical(false);
		setCertificatePolicyId("2.5.29.32.0");
		setType(TYPE_SUBCA);

		int[] bitlengths = {512, 1024, 2048, 4096 };
		setAvailableBitLengths(bitlengths);

		setUseKeyUsage(true);
		setKeyUsage(new boolean[9]);
		setKeyUsage(DIGITALSIGNATURE, true);
		setKeyUsage(KEYCERTSIGN, true);
		setKeyUsage(CRLSIGN, true);
		setKeyUsageCritical(true);

		setUseExtendedKeyUsage(false);
		setExtendedKeyUsage(new ArrayList());
		setExtendedKeyUsageCritical(false);
	}
}
