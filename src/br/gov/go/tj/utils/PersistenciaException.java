package br.gov.go.tj.utils;

public class PersistenciaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9194878150567179828L;

	public PersistenciaException(String mensagem, Throwable ex) {
		super(mensagem, ex);
	}
	
	public PersistenciaException(String mensagem) {
		super(mensagem);
	}
}
