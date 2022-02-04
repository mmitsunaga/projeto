package br.gov.go.tj.utils;

public class ConflitoDeAbasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4936038106370608779L;

	public ConflitoDeAbasException() {
		super("");
	}

	public ConflitoDeAbasException(String mensagem) {
		super(mensagem);
	}	
}
