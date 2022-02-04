package br.gov.go.tj.utils;

import java.awt.Color;

import br.gov.go.tj.utils.JCaptcha.DefaultManageableImageCaptchaServiceProjudi;
import br.gov.go.tj.utils.JCaptcha.GimpyFactoryProjudi;
import br.gov.go.tj.utils.JCaptcha.ImageCaptchaServiceProjudi;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.gimpy.BasicListGimpyEngine;
//import com.octo.captcha.image.gimpy.GimpyFactory;
//import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
//import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * Classe respons�vel em instanciar um ImageCaptchaService e definir as
 * caracter�sticas do teste captcha que ser� gerado (caracteres, tamanho,
 * background...)
 */
public class GerenciadorCaptcha {

	private static ImageCaptchaServiceProjudi service;
	
	private static GerenciadorCaptcha instance = new GerenciadorCaptcha();

	private GerenciadorCaptcha() {
		DefaultManageableImageCaptchaServiceProjudi serv = new DefaultManageableImageCaptchaServiceProjudi();
		serv.setCaptchaEngine(new Captcha());
		service = serv;
	}

	public static ImageCaptchaServiceProjudi getService() {
		if (instance == null)
			instance = new GerenciadorCaptcha();
		return service;
	}

	class Captcha extends BasicListGimpyEngine {

		protected void buildInitialFactories() {
			/*
			 * Define o m�nimo e m�ximo de caracteres (3 e 5), e ainda define um
			 * ColorGenerator para especificar as poss�veis cores (o parametro
			 * true determina que cada letra ter� um cor espec�fica)
			 */

			TextPaster textPaster = new RandomTextPaster(4, 7, new RandomListColorGenerator(new Color[] {Color.BLACK, Color.YELLOW, Color.WHITE, Color.RED , Color.BLUE,  Color.GREEN }), true);

			/*
			 * Gera o background, a imagem ter� 180 x 107 pixels
			 */

			BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(350, 130, new RandomListColorGenerator(new Color[] {
					new Color(203, 205, 212), new Color(255, 255, 255), new Color(1, 72, 126), new Color(232, 140, 37) }),
					new RandomListColorGenerator(new Color[] {new Color(203, 205, 212), new Color(255, 255, 255), new Color(1, 72, 126),
							new Color(232, 140, 37) }), new RandomListColorGenerator(new Color[] {new Color(203, 205, 212),
							new Color(255, 255, 255), new Color(1, 72, 126), new Color(232, 140, 37) }), new RandomListColorGenerator(new Color[] {
							new Color(203, 205, 212), new Color(255, 255, 255), new Color(1, 72, 126), new Color(232, 140, 37) }), 15);

			/*
			 * Gerador de fonte respons�vel por distorcer o texto, e definir o
			 * tamanho m�nimo e m�ximo da fonte (40 e 50)
			 */

			FontGenerator fontGenerator = new TwistedAndShearedRandomFontGenerator(52, 68);

			/*
			 * Objeto respons�vel por juntar o background, a fonte e o texto
			 * para gerar a imagem
			 */

			WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);

			/*
			 * Adiciona o Factory RandomWordGenerator com os caracteres desejados
			 */

			this.addFactory(new GimpyFactoryProjudi(new RandomWordGenerator("abcdefghijkmnpqrstuvwxyz23456789"), wordToImage));

		}	
		
	}
}
