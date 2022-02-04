package br.gov.go.tj.utils;

public class WebServiceException extends Exception {

	private static final long serialVersionUID = 5809175818388369474L;

	public WebServiceException(String mensagem) {
		super(mensagem);
	}

	public WebServiceException(String mensagem, Exception ex) {
		super(mensagem, ex);
	}
}