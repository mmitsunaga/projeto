
package br.gov.go.tj.utils.Certificado;

/**
 * @author CertiSign
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvalidCRLException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidCRLException(String msg){
		
		super(msg);
	}
	
	public InvalidCRLException(String msg, Exception e){
		
		super(msg, e);
	}

}
