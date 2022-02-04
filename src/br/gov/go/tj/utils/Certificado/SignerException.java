
package br.gov.go.tj.utils.Certificado;

/**
 * @author CertiSign
 *
 * Classe de excecao responsavel por encapsular as excecoes de baixo nivel geradas
 * no processo de assinatura.
 * 
 */
public class SignerException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SignerException(String msg){
		
		super(msg);
	}
}
