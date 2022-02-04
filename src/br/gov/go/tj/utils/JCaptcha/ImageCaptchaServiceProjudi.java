package br.gov.go.tj.utils.JCaptcha;

import com.octo.captcha.service.image.ImageCaptchaService;

public interface ImageCaptchaServiceProjudi extends ImageCaptchaService {
	
	String getCaptcha(String ID);
	
}
