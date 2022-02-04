package br.gov.go.tj.utils;

public class MensagemException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -886839519778336648L;

	public MensagemException(String mensagem) {
		super(mensagem);
	}
	
	public MensagemException(String mensagem, Exception ex) {
		super(mensagem, ex);
	}
}
