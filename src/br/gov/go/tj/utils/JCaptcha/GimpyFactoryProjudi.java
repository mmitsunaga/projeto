package br.gov.go.tj.utils.JCaptcha;

import java.awt.image.BufferedImage;
import java.util.Locale;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaQuestionHelper;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.GimpyFactory;

public class GimpyFactoryProjudi extends GimpyFactory {
	
	public GimpyFactoryProjudi(WordGenerator generator, WordToImage word2image) {
		super(generator, word2image);		
	}
	
	 /**
     * gimpies are ImageCaptcha
     *
     * @return a pixCaptcha with the question :"spell the word"
     */
    public ImageCaptcha getImageCaptcha(Locale locale) {
    	
    	//length
        Integer wordLength = getRandomLength();

        String word = getWordGenerator().getWord(wordLength, locale);

        BufferedImage image = null;
        try{
            image = getWordToImage().getImage(word);
        } catch(Throwable e) {
            throw new CaptchaException(e);
        }

        ImageCaptcha captcha = new GimpyProjudi(CaptchaQuestionHelper.getQuestion(locale, BUNDLE_QUESTION_KEY),
                image, word);
        
        return captcha;
    }
	
}
