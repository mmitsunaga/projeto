package br.gov.go.tj.utils.Certificado;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import br.gov.go.tj.utils.MensagemException;

/**
* Classe responsavel por fazer o parsing de um arquivo PKCS12.
* Ao realizar o parsing, e recuperado o certificado principal, sua chave privada (se possivel)
* e a cadeia de certificacao referente ao certificado principal (se possivel).
*/
public class PKCS12Parser {

	private X509Certificate cert;
	private KeyStore ks;
	private PrivateKey privateKey;
	private Certificate certChain[];
	
	/**
	 * Metodo responsavel por capturar o Certificado Principal do PKCS12, sua Cadeia de Certificacao
	 * e sua Chave Privada.
	 * 
	 * @param password - Senha do PKCS12 usada para acessar a Chave Privada
	 * @throws PKCS12ParsingException
	 * @throws MensagemException 
	 */
	private void parsePKCS12(String password) throws  MensagemException{
		
		try{
			boolean keyEntryFound = false;
			String keyEntry;
			//Busca todos os "Alias" dos certificados existentes no PKCS12, pois não sabemos qual
			//alias representa o certificado principal
			Enumeration en = this.ks.aliases();
									
			//Procura pelo certificado principal (KeyEntry) no PKCS12
			do{
				
				keyEntry = (String)en.nextElement();
				
				if(ks.isKeyEntry(keyEntry)){
					keyEntryFound = true;
					break;
				}
				
			}while(en.hasMoreElements());
			
			if(keyEntryFound){
				
				this.cert = (X509Certificate) ks.getCertificate(keyEntry);
				this.privateKey = (PrivateKey) ks.getKey(keyEntry, password.toCharArray());
				this.certChain = ks.getCertificateChain(keyEntry);
				
			} else {
				throw new MensagemException("Nao foi encontrado nenhum certificado principal no KeyStore! | PKCS12Parser.parsePKCS12(String password)");
			}
			
		} catch(KeyStoreException e){
			
			throw new MensagemException("O KeyStore nao foi inicializado corretamente! | PKCS12Parser.parsePKCS12(String password)| " );
			
		} catch(NoSuchAlgorithmException e){
			
			throw new MensagemException("Nao foi possivel encontrar o algoritmo de recuperacao de Chave Privada para o provedor criptografico  | PKCS12Parser.parsePKCS12(String password)| " );
			
		} catch(UnrecoverableKeyException e){
			
			throw new MensagemException("Nao foi possivel recuperar a Chave Privada! Verifique se a senha esta correta. | PKCS12Parser.parsePKCS12(String password)| ");
		}
	}
	
	public PKCS12Parser(InputStream file, String password) throws  MensagemException{
		
		try{
			//Registra o provedor criptografico da BouncyCastle
			//Security.addProvider(new BouncyCastleProvider());
			//Cria um objeto KeyStore do tipo PKCS12. Tambem existe KeyStore do tipo JKS.
			this.ks = KeyStore.getInstance("PKCS12","BC");
			//Carrega o arquivo PKCS12 (Exemplo: certificado.pfx) transformando para KeyStore.
			ks.load(file, password.toCharArray());
			
			this.parsePKCS12(password);
						
		} catch(KeyStoreException e){
			//e.printStackTrace();
			throw new MensagemException("O tipo de KeyStore especificado nao esta disponivel para este provedor criptografico. | PKCS12Parser.parsePKCS12(String password) |");
			
		} catch(NoSuchAlgorithmException e){
			//e.printStackTrace();
			throw new MensagemException("Nao foi possivel utilizar o algoritmo de verificacao de integridade do KeyStore. | PKCS12Parser.parsePKCS12(String password)| ");
			
		} catch(CertificateException e){
			//e.printStackTrace();
			throw new MensagemException("Nao foi possivel carregar algum dos certificados existentes no KeyStore. | PKCS12Parser.parsePKCS12(String password)| ");
			
		} catch(IOException e){
			//e.printStackTrace();
			//("Ocorreu um problema ao ler o arquivo de KeyStore especificado. Verifique a Senha. | PKCS12Parser.parsePKCS12(String password)| "
			throw new MensagemException("Ocorreu um problema ao abrir o certificado. Verifique a sua Senha e tente novamente!");
		} catch(NoSuchProviderException e) {
			throw new MensagemException("Provedor de assinatura não disponível. | PKCS12Parser.parsePKCS12(String password)| ");
			//e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que retorna o principal certificado do PKCS12
	 * 
	 * @return
	 */
	public X509Certificate getCertificate(){
				
		return this.cert;
	}
	
	/**
	 * Metodo que retorna a Chave Privada do Certificado Principal, caso tenha sido possivel
	 * recupera-la no processo de parsing do PKCS12
	 * 
	 * @return
	 */
	public PrivateKey getPrivateKey(){
		
		return this.privateKey;
	}
	
	/**
	 * Retorna a cadeia de certificados referente ao certificado principal, caso tenha sido
	 * possivel recupera-la no processo de parsing do PKCS12
	 * 
	 * @return
	 */
	public Certificate[] getCertificateChain(){
		
		return this.certChain;
	}
}

