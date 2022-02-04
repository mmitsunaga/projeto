package br.gov.go.tj.utils.Certificado;

/**
 * Classe de excecao responsavel por encapsular as excecoes de baixo nivel geradas
 * no processo de parsing de um PKCS12.
 */
public class PKCS12ParsingException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PKCS12ParsingException(String msg){
		
		super(msg);
	}
}
