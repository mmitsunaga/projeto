package br.gov.go.tj.utils.Certificado;
import java.util.ArrayList;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;

import br.gov.go.tj.projudi.util.Identidade;

/**
 * 
 * @author Leandro de Lima Lira
 * @author André Luis Cavalcanti Moreira
 * Esta classe tem a função de auxiliar na padronização
 * dos certificados utilizaos, utilizando regras definidas pela ICP-Brasil
 *
 */
public class UsuarioFinal extends CertificadoConfig {

	/**
     * 
     */
    private static final long serialVersionUID = 4412578312567482853L;
    Identidade id;

	public UsuarioFinal(Identidade id, String pontoDistribuicao) {
		
		this.id = id;

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

		setType(TYPE_ENDENTITY);

		int[] bitlengths = {512, 1024, 2048, 4096 };
		setAvailableBitLengths(bitlengths);

		setUseKeyUsage(true);
		setKeyUsage(new boolean[9]);
		setKeyUsage(DIGITALSIGNATURE, true);
		setKeyUsage(KEYENCIPHERMENT, true);
		setKeyUsage(NONREPUDIATION,true);
		setKeyUsageCritical(true);

		setUseExtendedKeyUsage(false);
		ArrayList eku = new ArrayList();
		eku.add(new Integer(SERVERAUTH));
		eku.add(new Integer(CLIENTAUTH));
		eku.add(new Integer(EMAILPROTECTION));
		eku.add(new Integer(IPSECENDSYSTEM));
		eku.add(new Integer(IPSECUSER));
		setExtendedKeyUsage(eku);
		setExtendedKeyUsageCritical(false);


	}
	
	public void nomeAlternativoICPBrasil(DEREncodableVector vec) {
		/*
		 * A ICP-Brasil também define como obrigatória a extensão "Subject Alternative Name", não crítica e com os seguintes formatos:
		 * Para certificado de pessoa física, 3 (três)campos otherName, contendo:
		 */
		if(id.ehPessoaFisica()) {
			/*
			 * -OID =2.16.76.1.3.1 e conteúdo =
			 * nas primeiras 8 (oito)posições,a data de nascimento do titular, no formato ddmmaaaa;
			 * nas 11 (onze) posições subseqüentes,o Cadastro de Pessoa Física (CPF) do titular;
			 * nas 11 (onze) posições subseqüentes, o Número de Identificação Social-NIS (PIS, PASEP ou CI);
			 * nas 11 (onze) posições subseqüentes, o número do Registro Geral (RG) do titular;
			 * nas 6 (seis) posições subseqüentes, as siglas do órgão expedidor do RG e respectiva UF.		 * 
			  */
	
			String outroNome = id.getDataNascimento();
			outroNome += id.getCPF();
			outroNome +=  id.getNIS();
			outroNome += id.getRG();
			outroNome += id.getRGOrgaoExpedidorUF();
			{
				DERSequence othernameSequence =
				   new DERSequence(new ASN1Encodable[] {
					   new DERObjectIdentifier("2.16.76.1.3.1"),
					   new DERTaggedObject(true, 0, new
					DEROctetString(outroNome.getBytes()))});
			   	GeneralName gn = new GeneralName(GeneralName.otherName, othernameSequence);
				vec.add(gn);			
			}
			/*
			 * -OID =2.16.76.1.3.6 e conteúdo =nas 12 (doze)posições o número do Cadastro Específico do INSS (CEI)da pessoa física titular do certificado.
			 */
			 outroNome = id.getINSS();
			 {
				DERSequence othernameSequence =
				   new DERSequence(new ASN1Encodable[] {
					   new DERObjectIdentifier("2.16.76.1.3.6"),
					   new DERTaggedObject(true, 0, new
					DEROctetString(outroNome.getBytes()))});
				GeneralName gn = new GeneralName(GeneralName.otherName, othernameSequence);
				vec.add(gn);		 	
			 }
			/* 
			 * -OID =2.16.76.1.3.5 e conteúdo = 
			 * nas primeiras 12 (doze) posições, o número de inscrição do Título de Eleitor; 
			 * nas 3 (três) posições subseqüentes, a Zona Eleitoral; 
			 * nas 4 (quatro) posições seguintes, a Seção; 
			 * nas 22 (vinte e duas) posições subseqüentes, o município e a UF do Título de Eleitor.				
			*/
			outroNome = "0000000000000000000";
			{
				DERSequence othernameSequence =
				   new DERSequence(new ASN1Encodable[] {
					   new DERObjectIdentifier("2.16.76.1.3.5"),
					   new DERTaggedObject(true, 0, new
					DEROctetString(outroNome.getBytes()))});
				GeneralName gn = new GeneralName(GeneralName.otherName, othernameSequence);
				vec.add(gn);		 	
			}
		} else {
			/* 
			 * 
			 * Para certificado de pessoa jurídica,4 (quatro)campos otherName,contendo,nesta ordem:
			 * -OID =2.16.76.1.3.4 e conteúdo = nas primeiras 8 (oito) posições, a data de nascimento do responsável pelo certificado, no formato ddmmaaaa; nas (onze) posições subseqüentes,o Cadastro de Pessoa Física (CPF) do responsável; nas 11 (onze) posições subseqüentes, o Número de Identificação Social-NIS (PIS,PASEP ou CI); nas (onze) posições subseqüentes, o número do RG do responsável;nas 6 (seis) posições subseqüentes, as siglas do órgão expedidor do RG e respectiva UF;
			 * -OID =2.16.76.1.3.2 e conteúdo =nome do responsável pelo certificado;
			 * -OID =2.16.76.1.3.3 e conteúdo = Cadastro Nacional de Pessoa Jurídica (CNPJ) da pessoa jurídica titular do certificado;
			 * -OID =2.16.76.1.3.7 e conteúdo =nas 12 (doze)posições o número do Cadastro Específico do INSS (CEI)da pessoa jurídica titular do certificado.
			 * 
			 * Os campos otherName definidos como obrigatórios pela ICP-Brasil devem estar de acordo com as seguintes especificações:
			 * -O conjunto de informações definido em cada campo otherName deve ser armazenado como uma cadeia de caracteres do tipo ASN.1 OCTET STRING;
			 * -Quando os números de CPF,NIS (PIS,PASEP ou CI),RG,CNPJ,CEI ou Título de Eleitor não estiverem disponíveis,os campos correspondentes devem ser integralmente preenchidos com caracteres "zero";
			 * -Se o número do RG não estiver disponível,não se deve preencher o campo de órgão emissor e UF. O mesmo ocorre para o campo de município e UF,se não houver número de inscrição do Título de Eleitor;
			 * -Todas informações de tamanho variável referentes a números,tais como RG,devem ser preenchidas com caracteres "zero"a sua esquerda para que seja completado seu máximo tamanho possível;
			 * 
			 * -As 6 (seis)posições das informações sobre órgão emissor do RG e UF referem-se ao tamanho máximo,devendo ser utilizadas apenas as posições necessárias ao seu armazenamento,da esquerda para a direita.O mesmo se aplica às 22 (vinte e duas)posições das informações sobre município e UF do Título de Eleitor.
			 * -Apenas os caracteres de A a Z e de 0 a 9 poderão ser utilizados,não sendo permitidos caracteres especiais,símbolos,espaços ou quaisquer outros. Campos otherName adicionais,contendo informações específicas e forma de preenchimento e armazenamento definidas pela AC,poderão ser utilizados com OID atribuídos ou aprovados pela AC- Raiz. 
			 * 
			 */
				throw new RuntimeException("<{Certificado para pessoa jurídica não suportado.}> Local Exception: " + this.getClass().getName() + ".nomeAlternativoICPBrasil()");
		}
	
	}

}
