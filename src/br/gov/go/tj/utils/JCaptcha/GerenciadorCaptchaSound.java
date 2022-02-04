package br.gov.go.tj.utils.JCaptcha;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import br.gov.go.tj.projudi.util.ProjudiPropriedades;

public class GerenciadorCaptchaSound {
	
	private String caracteresValidos = "abcdefghijklmnopqrstuvwxyz123456789_";
	private char caracaterePausa = '_';
	
	private static GerenciadorCaptchaSound instance = null;
	
	private Map arquivosSom;
	private List arquivosFundoMusical;
	
	private GerenciadorCaptchaSound(){
		arquivosSom = new HashMap();
		arquivosFundoMusical = new ArrayList();
		carregueArquivos();
		carregueArquivosFundoMusical();		
	}
	
	public static GerenciadorCaptchaSound getService() {
		if (instance == null) instance = new GerenciadorCaptchaSound();
		return instance;
	}
	
	public synchronized byte[] getSound(String CaptchaGerado) throws Exception{	
		AudioInputStream audioInputStreamUmCaractere = null;
		AudioInputStream audioInputStreamSaida = null;
		
		byte[] temp = null;
		ByteArrayOutputStream byteOutputStream = null;
		try{
			String CaptchaComPausa = "";
			for(int i =0; i < CaptchaGerado.length(); i++){
				CaptchaComPausa += caracaterePausa;
				CaptchaComPausa += CaptchaGerado.charAt(i);			
			}
			CaptchaComPausa += caracaterePausa;
			
			for(int i =0; i < CaptchaComPausa.length(); i++){			
				if (arquivosSom.get(CaptchaComPausa.charAt(i)) != null){
					// InputStream inputStream = new ByteArrayInputStream((byte[]) arquivosSom.get(CaptchaComPausa.charAt(i)));				
					/// audioInputStreamUmCaractere =  AudioSystem.getAudioInputStream(inputStream);
					audioInputStreamUmCaractere =  getAudioInputStream((byte[]) arquivosSom.get(CaptchaComPausa.charAt(i)));
					
					if (audioInputStreamSaida == null){
						audioInputStreamSaida = audioInputStreamUmCaractere;
					}else{
						audioInputStreamSaida = new AudioInputStream(new SequenceInputStream(audioInputStreamSaida, 
								                                                       audioInputStreamUmCaractere), 
								                                      audioInputStreamSaida.getFormat(), 
								                                      audioInputStreamSaida.getFrameLength() + 
								                                      audioInputStreamUmCaractere.getFrameLength()); 
					}	
								
					//audioInputStreamUmCaractere = null;
				}
				 
			}
			
			byteOutputStream = new ByteArrayOutputStream();
			if (arquivosFundoMusical.size() > 0){
				AudioFormat	audioFormat = audioInputStreamSaida.getFormat();
				
				List audioInputStreamList = new ArrayList();
				audioInputStreamList.add(audioInputStreamSaida);
				
				audioInputStreamList.add(getProximoFundoMusical());	
				
				AudioInputStream audioInputStreamSaidaMixado = new MixingAudioInputStream(audioFormat, audioInputStreamList);
				
				AudioSystem.write(audioInputStreamSaidaMixado, javax.sound.sampled.AudioFileFormat.Type.WAVE, byteOutputStream);			
			}else{
				AudioSystem.write(audioInputStreamSaida, javax.sound.sampled.AudioFileFormat.Type.WAVE, byteOutputStream);
			}
			temp  =byteOutputStream.toByteArray();
			byteOutputStream.close();
		}catch(Exception e){
			try{if (byteOutputStream!=null) byteOutputStream.close(); } catch(Exception ex ) {};
			throw e;
		}
		return 	temp;
		
		
		//audioInputStreamUmCaractere.close();
		//audioInputStreamSaida.close();
		
		// AudioSystem.write(audioInputStreamSaida, javax.sound.sampled.AudioFileFormat.Type.WAVE, new File("c:\\" + CaptchaGerado + ".wav"));	
		
	}
	
	private AudioInputStream getAudioInputStream(byte[] conteudoAudio) throws UnsupportedAudioFileException, IOException{
		InputStream inputStream = new ByteArrayInputStream(conteudoAudio);				
		return AudioSystem.getAudioInputStream(inputStream);
	}
	
	//private int proximoIndiceFundoMusical = -1; 
	private AudioInputStream getProximoFundoMusical() throws UnsupportedAudioFileException, IOException{
		//int proximoIndiceFundoMusical  +=1 ;
		//if (proximoIndiceFundoMusical > (arquivosFundoMusical.size() - 1)) proximoIndiceFundoMusical = 0;
		return getAudioInputStream((byte[])arquivosFundoMusical.get((new Random()).nextInt(arquivosFundoMusical.size())));		
	}
	
	private synchronized void carregueArquivos() {
		try{			
			for (int i = 0; i < caracteresValidos.length(); i++){
				arquivosSom.put(caracteresValidos.charAt(i), obtenhaArquivo(caracteresValidos.charAt(i)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	private byte[] obtenhaArquivo(char inicial) throws Exception{
		return obtenhaArquivo(obtenhaNomeArquivo(inicial));		
	}
	
	private byte[] obtenhaArquivo(String nomeArquivo) throws Exception{
		File arquivoSom = new File(nomeArquivo);
		byte[] temp=null;
		ByteArrayOutputStream byteOutputStream=null;
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(arquivoSom);
			
			byteOutputStream = new ByteArrayOutputStream();
			AudioSystem.write(audioInputStream, javax.sound.sampled.AudioFileFormat.Type.WAVE, byteOutputStream);	
			
			//audioInputStream.close();
			temp = byteOutputStream.toByteArray();
			byteOutputStream.close();
		}catch(Exception e){
			try{if (byteOutputStream!=null) byteOutputStream.close(); } catch(Exception ex ) {};
			throw e;
		}
		return temp;	
	}
	
	private String obtenhaNomeArquivo(char inicial) throws Exception{
		return obtenhaCaminhoArquivos() + inicial + ".wav";				
	}
	
	private String obtenhaCaminhoArquivos() throws Exception{
		return ProjudiPropriedades.getInstance().getCaminhoAplicacao() + "captchaSound" + File.separator;
		
	}
	
	private String obtenhaCaminhoArquivosFundoMusical() throws Exception{
		return obtenhaCaminhoArquivos() + "fundomusical" + File.separator; 
		
	}
	
	private synchronized void carregueArquivosFundoMusical() {
		try{
			String enderecoDiretorio = obtenhaCaminhoArquivosFundoMusical();			
			File diretorio = new File(enderecoDiretorio);			
			String[] arquivosWav = diretorio.list(new FiltroWav());
			
			for (int i = 0; i < arquivosWav.length; i++){
				arquivosFundoMusical.add(obtenhaArquivo(enderecoDiretorio + arquivosWav[i]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	class FiltroWav
		implements FilenameFilter{

		public boolean accept(File dir, String name) {
			return name.toUpperCase().endsWith(".WAV");
		}
	}
	

}
