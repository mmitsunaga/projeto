package br.gov.go.tj.utils.JCaptcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

public class DefaultManageableImageCaptchaServiceProjudi 
	extends DefaultManageableImageCaptchaService 
	implements ImageCaptchaServiceProjudi {

	public String getCaptcha(String ID) {
		if (!store.hasCaptcha(ID)) {
            throw new CaptchaServiceException("ID inválido, não foi possível obter o captcha gerado.");         
		}

		GimpyProjudi captchaGerado = (GimpyProjudi) store.getCaptcha(ID);
		
		return captchaGerado.getResponse();
	}
}