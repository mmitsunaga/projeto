/*
 *	PROJUDI (Processo Judicial Digital)
 * 
 *	Este arquivo � parte do projeto PROJUDI registrado no INPI com n�mero  INPI
 *  sob o nome de PRODIGICON. N�o � permitido a c�pia do todo ou parte deste c�digo
 *  exceto quando autorizado por escrito pelos autores.
 */
package br.gov.go.tj.utils;

/**
 * 
 * @author Leandro de Lima Lira
 * @author Andr� Luis Cavalcanti Moreira
 * Esta classe tem a fun��o de gerenciar o envio de
 * e-mnails autom�ticos da aplica��o
 *
 */

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviarEmail {
	
	Session sessao;
		
	public EnviarEmail(String mailHost, String mailDominio)  {
		sessao = configurarSessao(mailHost, mailDominio);
	}
	

	/**
	 *Inicia uma Sess�o de envio de e-mail. 
	 *@return a sess�o a ser estabelecida
	 */
	private Session configurarSessao(String mailHost, String mailDominio){
		Properties mailProps = System.getProperties();
		mailProps.put(mailHost,mailDominio);
		Session mailSession = Session.getDefaultInstance(mailProps, null);
		////System.out.println("Host eMail: "+ mailHost);
		////System.out.println("Dom�nio eMail: "+ mailDominio);
		return mailSession;
	}
	
	/**
	 * Cria uma mensagem de e-mail com base nos par�metros fornecidos
	 * @param destinatario um array com os nomes dos destinat�rios do email
	 * @param emailRemetente o e-mail do remetente
	 * @param corpoEmail o conte�do do e-mail
	 * @param assunto o assunto do e-mail
	 * @param email um array com os e-mails dos destinat�rios
	 * @return a messagem pronta para ser enviada
	 * @throws Exception em caso de e-mails malformados ou erro de io na constru��o da mensagem
	 */	
	public Message prepararEmailTexto(String destinatario[], 
			String emailRemetente, 
			String nomeRemetente,
			String corpoEmail, 
			String assunto, 
			String[] email)throws Exception{
		
		Message message = new MimeMessage (sessao);
		message.setFrom( new InternetAddress(emailRemetente,nomeRemetente) );
		message.addRecipient(Message.RecipientType.TO,new InternetAddress(email[0],destinatario[0]));
		for(int i=1; i< email.length;i++)
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(email[i],destinatario[i]) );
		message.setSentDate(new Date());
		message.setSubject(assunto);
		
		
		message.setContent(message.toString(),"text/plain");
		message.setText(corpoEmail);
		
		return message;

	}

	/**
	 * Envia um e-mail
	 * @param m a mensagem de e-mail a ser enviada
	 * @throws Exception em caso de erro no envio
	 */
	public void enviarEmail(Message m) throws Exception{
		Transport.send(m);
	}
	
	
	/**
	 * Testa se uma String representa corretamente um endere�o de e-mail
	 * @param aEmailAddress a String contendo o endere�o
	 * @return true caso a String represente corretamente um endere�o, ou false caso contr�rio
	 */
	public static boolean isValidEmailAddress(String aEmailAddress) throws Exception {
	  if (aEmailAddress == null) return false;
    
	  try{
		return hasNameAndDomain(new InternetAddress(aEmailAddress).getAddress());
	  }
	  catch(AddressException ex){
		return false;
	  }
	}
	
	//m�todo para testar o envio e e-mails
	public static void main(String [] args) {
		/*
		try{
		////System.out.println("Host eMail: "+ projudiConfiguration.getHostEmail());
		////System.out.println("Dom�nio eMail: "+ projudiConfiguration.getDominioEMail());
		
		EnviarEmail carteiro = new EnviarEmail();
		String [] destinatario = new String [1];
		destinatario[0] = "Leandro Lira";
		String [] email = new String[1];
		email[0] = "leandro.lira@gmail.com";
		Message mensagem = carteiro.prepararEmailTexto( destinatario, "contato.projudi@tj.rr.gov.br","testando mail em roraima", "teste",  email); 
		carteiro.enviarEmail(mensagem);
		
		}catch(Exception e){
			////System.out.println(e.getStackTrace());
		}
		*/
	}
	
	
	
	private static boolean hasNameAndDomain(String aEmailAddress){
	   String[] tokens = aEmailAddress.split("@");
	   return (
		tokens.length == 2 &&
		analisaPrimeiroToken( tokens[0] ) && 
		analisaPrimeiroToken( tokens[1] ) ) ;
	 }
	 
	 private static boolean analisaPrimeiroToken(String antesDoArroba){
	 	if(antesDoArroba.equals("")) return false;
	 	
		//var regexp_user=/^\"?[\w-_\.\']*\"?$/; c�digo javascript com a express�o regular
	 	return  antesDoArroba.matches("^\"?[/w-_/./']*\"?$/");
	 	

	 }
}
