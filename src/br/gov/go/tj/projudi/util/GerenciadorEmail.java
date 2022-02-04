package br.gov.go.tj.projudi.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import com.sun.mail.smtp.SMTPMessage;

import br.gov.go.tj.utils.Funcoes;

public  class GerenciadorEmail extends Thread{
	public static final int ENVIAR_EMAIL_HTML_ADM = 1;
	public static final int ENVIAR_EMAIL_PLAIN_TEXT = 2;
	public static final int ENVIAR_EMAIL_PLAIN_TEXT_CC = 3;
	public static final int ENVIAR_EMAIL_PLAIN_TEXT_MULTIPLO=4;
	private String nomeDestinatario;
	private String emailDestinatario;
	private String emailDestinatarioCC;
	private String[] nomeDestinatarioMultiplo;
	private String[] emailDestinatarioMultiplo;
	private String assunto;
	private String textoDaMsg;
	private int emalTipo;
	
	//private static Logger logger = Logger.getLogger(Projudi.class);
	public GerenciadorEmail(String[] nomeDestinatario, String[] emailDestinatario , String assunto, String textoDaMsg) {
		this.nomeDestinatarioMultiplo= nomeDestinatario;
		this.emailDestinatarioMultiplo = emailDestinatario;
		this.assunto= assunto;
		this.textoDaMsg = textoDaMsg;		
		this.emalTipo = ENVIAR_EMAIL_PLAIN_TEXT_MULTIPLO;
	}
	
	public GerenciadorEmail(String nomeDestinatario, String emailDestinatario ,String emailDestinatarioCC, String assunto, String textoDaMsg) {
		this.nomeDestinatario= nomeDestinatario;
		this.emailDestinatario = emailDestinatario;
		this.assunto= assunto;
		this.textoDaMsg = textoDaMsg;
		this.emailDestinatarioCC = emailDestinatarioCC;
		this.emalTipo = ENVIAR_EMAIL_PLAIN_TEXT_CC;
	}
	
	public GerenciadorEmail(String nomeDestinatario, String emailDestinatario , String assunto, String textoDaMsg, int tipo_email){
		this.nomeDestinatario= nomeDestinatario;
		this.emailDestinatario = emailDestinatario;
		this.assunto= assunto;
		this.textoDaMsg = textoDaMsg;
		this.emalTipo = tipo_email;				
	}

	
	public void run() {
		switch (emalTipo) {
			case ENVIAR_EMAIL_HTML_ADM:				enviarEmailHTMLAdministrador();			break;
			case ENVIAR_EMAIL_PLAIN_TEXT: 			enviarEmailPlainText(); 				break;
			case ENVIAR_EMAIL_PLAIN_TEXT_CC:		enviarEmailPlainTextCC(); 				break;
			case ENVIAR_EMAIL_PLAIN_TEXT_MULTIPLO:	enviarEmailPlainTextMultiplo();			break;
				
			default:
				break;
		}

	}
	
	/**
	 * Cria um objeto Session do JavaMail para conectar via SMTP a um servidor de e-mail especificado no campo
	 * HOST_EMAIL das propriedades do Sistema
	 * @return Objeto Session do JavaMail
	 */
	 private static Session getJavaMailSession(){
		String hostSMTP = ProjudiPropriedades.getInstance().getHostEmail();
		Properties props = new Properties();
		//System.setProperty("javax.net.debug", "ssl,handshake");
		props.put("mail.smtp.host", hostSMTP );
		props.put("mail.smtp.ssl.trust", ProjudiPropriedades.getInstance().getHostEmail() );
		props.put("mail.smtp.starttls.enable", "false");
		Session sessaoJavaMail = Session.getInstance( props );
		//sessaoJavaMail.setDebug(true);
		return sessaoJavaMail;
	}
	
	/**
	 * Envia um e-mail em formato HTML para o endereço de e-mail especificado noS parâmetroS.
	 * O e-mail será enviado pelo e-mail NOME_SISTEMA@DOMINIO_EMAIL, onde NOME_SISTEMA e DOMINIO_EMAIL são propriedades
	 * sistema PROJUDI.
	 * No campo assunto será apensada a tag [NOME_SISTEMA] do lado do assunto especificado noS parâmetroS desta função.
	 * 
	 * @param nomeDestinatario Nome do destinatário da mensagem.
	 * @param emailDestinatario Endereço de e-mail do destinatário da mensagem.
	 * @param assunto Assunto da mensagem.
	 * @param textoDaMsg Texto da mensagem.
	 * @throws Exception
	 * @author vfosantos
	 */
	private  void enviarEmailHTMLAdministrador(){
		if(Funcoes.validarEmail(emailDestinatario)) {
			
			if( ProjudiPropriedades.getInstance().getEnvioDeEmailHabilitado() == false ) return;
			Session sessaoJavaMail = getJavaMailSession();
			SMTPMessage mensagem = new SMTPMessage(sessaoJavaMail);
			try{
				// Cria os objetos remetentes e destinatário e associa à mensagem.
				InternetAddress remetente = new InternetAddress( "sistema-projudi@" + ProjudiPropriedades.getInstance().getDominioEMail() , "SISTEMA PROJUDI" );
				mensagem.addFrom(new Address[] {remetente });
				InternetAddress destinatario = new InternetAddress( emailDestinatario , nomeDestinatario );
				mensagem.addRecipients(RecipientType.TO, new Address[] {destinatario } );
				mensagem.setSubject( "[" +  ProjudiPropriedades.getInstance().getNomeSistema() + "]" + assunto );
				// Define Formato do e-mail.
				mensagem.setContent(textoDaMsg, "text/html; charset=utf-8");
				mensagem.saveChanges();
				Transport.send( mensagem );
			}catch( Exception e ){
				e.printStackTrace();
			}
		}
	}
	
						

	/**
	 * Envia um e-mail em formato plain-text para o endereço de e-mail especificado nos parâmetros.
	 * O e-mail será enviado pelo e-mail NOME_SISTEMA@DOMINIO_EMAIL, onde NOME_SISTEMA e DOMINIO_EMAIL são propriedades
	 * sistema PROJUDI.
	 * No campo assunto será apensada a tag [NOME_SISTEMA] do lado do assunto especificado nos parâmetros desta função.
	 * 
	 * @param nomeDestinatario Nome do destinatário da mensagem.
	 * @param emailDestinatario Endereço de e-mail do destinatário da mensagem.
	 * @param assunto Assunto da mensagem.
	 * @param textoDaMsg Texto da mensagem.
	 * @throws Exception
	 * @author vfosantos
	 */
	private void enviarEmailPlainText(){
		Session sessaoJavaMail = getJavaMailSession();
		SMTPMessage mensagem = new SMTPMessage(sessaoJavaMail);
		try{
			// Cria os objetos remetentes e destinatário e associa à mensagem.
			InternetAddress remetente = new InternetAddress( "sistema-projudi@" + ProjudiPropriedades.getInstance().getDominioEMail() , "SISTEMA PROJUDI" );
			mensagem.addFrom(new Address[] {remetente });
			InternetAddress destinatario = new InternetAddress( emailDestinatario , nomeDestinatario );
			mensagem.addRecipients(RecipientType.TO, new Address[] {destinatario } );			
			mensagem.setSubject( "[" +  ProjudiPropriedades.getInstance().getNomeSistema() + "]" + assunto );
			// Define Formato do e-mail.
			mensagem.setContent(textoDaMsg, "text/plain; charset=utf-8");
			mensagem.saveChanges();
			mensagem.setSentDate( new Date() );
			Transport.send( mensagem );
		}catch( Exception e ){
			//em reunião com Jesus, decidiu-se por não gravar este log, pois não é um serviço obrigatório do Projudi. Esse log estava atrapalhando a tela de Log Erros.
		}		
	}
	
	/**
	 * Envia um e-mail em formato plain-text para o endereço de e-mail especificado nos parâmetros.
	 * O e-mail será enviado pelo e-mail NOME_SISTEMA@DOMINIO_EMAIL, onde NOME_SISTEMA e DOMINIO_EMAIL são propriedades
	 * sistema PROJUDI.
	 * No campo assunto será apensada a tag [NOME_SISTEMA] do lado do assunto especificado nos parâmetros desta função.
	 * @param nomeDestinatario
	 * @param emailDestinatario
	 * @param emailDestinatarioCC
	 * @param assunto
	 * @param textoDaMsg
	 */
	private void enviarEmailPlainTextCC(){
		Session sessaoJavaMail = getJavaMailSession();
		SMTPMessage mensagem = new SMTPMessage(sessaoJavaMail);
		try{
			// Cria os objetos remetentes e destinatário e associa à mensagem.
			InternetAddress remetente = new InternetAddress( "sistema-projudi@" + ProjudiPropriedades.getInstance().getDominioEMail() , "SISTEMA PROJUDI" );
			mensagem.addFrom(new Address[] {remetente });
			InternetAddress destinatario = new InternetAddress( emailDestinatario , nomeDestinatario );
			mensagem.addRecipients(RecipientType.TO, new Address[] {destinatario } );
			InternetAddress destinatarioProjudi = new InternetAddress( emailDestinatarioCC , "Processo Judicial Digital");
			mensagem.addRecipients(RecipientType.CC, new Address[] {destinatarioProjudi } );
			mensagem.setSubject( "[" +  ProjudiPropriedades.getInstance().getNomeSistema() + "]" + assunto );
			// Define Formato do e-mail.
			mensagem.setContent(textoDaMsg, "text/plain; charset=utf-8");
			mensagem.saveChanges();
			mensagem.setSentDate( new Date() );
			Transport.send( mensagem );
		}catch( Exception e ){
			//em reunião com Jesus, decidiu-se por não gravar este log, pois não é um serviço obrigatório do Projudi. Esse log estava atrapalhando a tela de Log Erros.
		}
	}
	
	/**
	 * Envia um e-mail em formato plain-text para os endereços de e-mail especificados no parâmetro.
	 * O e-mail será enviado pelo e-mail NOME_SISTEMA@DOMINIO_EMAIL, onde NOME_SISTEMA e DOMINIO_EMAIL são propriedades
	 * sistema PROJUDI.
	 * No campo assunto será apensada a tag [NOME_SISTEMA] do lado do assunto especificado nos parâmetros desta função.
	 * @param nomeDestinatario
	 * @param emailDestinatario
	 * @param emailDestinatarioCC
	 * @param assunto
	 * @param textoDaMsg
	 */
	private void enviarEmailPlainTextMultiplo(){
		Session sessaoJavaMail = getJavaMailSession();
		SMTPMessage mensagem = new SMTPMessage(sessaoJavaMail);
		try{
			// Cria os objetos remetentes e destinatário e associa à mensagem.
			InternetAddress remetente = new InternetAddress( "sistema-projudi@" + ProjudiPropriedades.getInstance().getDominioEMail() , "SISTEMA PROJUDI" );
			mensagem.addFrom(new Address[] {remetente });
						
			Address[] destinatarios = new Address[emailDestinatarioMultiplo.length];
			for(int i=0; i <emailDestinatarioMultiplo.length;i++) {
				destinatarios[i] = new InternetAddress( emailDestinatarioMultiplo[i] , nomeDestinatarioMultiplo[i] );
			}
			mensagem.addRecipients(RecipientType.TO, destinatarios );			
			mensagem.setSubject( "[" +  ProjudiPropriedades.getInstance().getNomeSistema() + "]" + assunto );
			// Define Formato do e-mail.
			mensagem.setContent(textoDaMsg, "text/plain; charset=utf-8");
			mensagem.saveChanges();
			mensagem.setSentDate( new Date() );
			Transport.send( mensagem );
		}catch( Exception e ){
			//em reunião com Jesus, decidiu-se por não gravar este log, pois não é um serviço obrigatório do Projudi. Esse log estava atrapalhando a tela de Log Erros.
		}
	}
	
}
