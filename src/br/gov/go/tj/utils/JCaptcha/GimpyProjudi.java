package br.gov.go.tj.utils.JCaptcha;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import com.octo.captcha.image.ImageCaptcha;

public class GimpyProjudi extends ImageCaptcha implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5381002907297164813L;
	private String response;

    GimpyProjudi(String question, BufferedImage challenge, String response) {
        super(question, challenge);
        this.response = response;
    }

    /**
     * Validation routine FROM the CAPTCHA interface. this methods verify if the response is not null AND a String and
     * then compares the given response to the internal string.
     *
     * @return true if the given response equals the internal response, false otherwise.
     */
    public final Boolean validateResponse(final Object response) {
        return (null != response && response instanceof String)
                ? validateResponse((String) response) : Boolean.FALSE;
    }

    /**
     * Very simple validation routine that compares the given response to the internal string.
     *
     * @return true if the given response equals the internal response, false otherwise.
     */
    private final Boolean validateResponse(final String response) {
        return new Boolean(response.equals(this.response));
    }
    
    public String getResponse(){
    	return this.response;
    }

    

}
