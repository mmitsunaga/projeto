/*
 * Created on 13/06/2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code AND Comments
 */
package br.gov.go.tj.projudi.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.mail.Message;

import org.apache.log4j.Logger;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.DiaHoraEventos;
import br.gov.go.tj.utils.EnviarEmail;
//import org.apache.torque.util.Transaction;
//import projudi.beans.Administrador;
//import projudi.beans.AdministradorPeer;

/**
 * 
 * Esta classe tem a função de auxiliar na verificação dos 
 * momentos em que o sistema estava acessível e os momentos em que o sistema
 * estava inascessível para os usuários da internet.
 * Mais uma vez o Design Pattern Singleton é utiliado aqui
 */

public class Running implements Runnable {
	
	private static final String HOST1 = "www.google.com";
	private static final String HOST2 = "www.uol.com.br";
	private static final String HOST3 = "www.bol.com.br";
	
	private static Running instance;
	
	private static long startDelay;
	private static int periodDelay;
	private static GregorianCalendar calendar;
	private static Thread testeUP;
	private static boolean finished = false;
	private static int pingDelay;
	private static InetAddress h1,h2,h3;
	private static ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
	
	
	static Logger log = Logger.getLogger(Running.class);
	
	/**
	 * Temos um carteiro para a necessidade de envir e-mails aos administradores
	 *  sobre eventuais problemas encontrados
	 */
	
	private static EnviarEmail carteiro = new EnviarEmail(projudiConfiguration.getHostEmail(), projudiConfiguration.getDominioEMail());
	
	
	
	/**
	 *  Pega uma instância da fila para poder adicionar e-mails a serem enviados
	 * @return a instância
	 * @throws UnknownHostException 
	 */
	
	public static Running getInstance() throws UnknownHostException {
		if (instance== null)
			instance = new Running();
		
		return instance;
	}
	
	private Running() throws UnknownHostException {
									
		 int periodo = 900;
		
		h1 = InetAddress.getByName(HOST1);
		h2 = InetAddress.getByName(HOST2);
		h3 = InetAddress.getByName(HOST3);
		
		calendar = new GregorianCalendar();
		calendar.setTime(new Date());

		calendar.set(GregorianCalendar.MINUTE,0);
		calendar.set(GregorianCalendar.SECOND,0);
		calendar.set(GregorianCalendar.MILLISECOND,0);
		
		Running.periodDelay = periodo;
		Running.pingDelay = 30000;
		while(calendar.getTimeInMillis() < System.currentTimeMillis())
			calendar.add(GregorianCalendar.SECOND, periodo);
		
		log.info("Primeira execução do teste de uptime será em "+DiaHoraEventos.converteFormatoMinutos(calendar.getTime()));
		
		startDelay = calendar.getTimeInMillis() - System.currentTimeMillis();
		if(startDelay < 0) startDelay = 0;
		log.info("Faltam "+startDelay+" milissegundos para a primeira execução do teste de uptime.");	
		
		testeUP = new Thread(this);
		testeUP.start();
		instance = this;
		
	}
	


	/**
	 * Método que administra a thread que cuida desta execução
	 */
	public synchronized void run() {
		long delay;
		
		try{
			this.wait(startDelay);
		} catch(InterruptedException e) {
		}
		
		while(!finished) {
			//log.info("Teste de Uptime está executando.");
			executa();
			calendar.add(GregorianCalendar.MINUTE,periodDelay);
			//log.info("Próxima execução do teste será em "+DiaHoraEventos.converteFormatoMinutos(calendar.getTime()));
			delay = calendar.getTimeInMillis() - System.currentTimeMillis();			
			if(delay < 0) delay = 0;
			try{
				this.wait(delay);
			} catch(InterruptedException e1) {
			}			
		}
		log.info("Thread de execução automática foi finalizada.");
	}
	
	/**
	 * Método que finaliza a execução da thread
	 *
	 */
	public synchronized void finish() {
		finished = true;
		this.notify();
	}
	
	/**
	 * Método que realmente salva no sgpd a ocorrência de que o sistema
	 * estava no ar
	 *
	 */
	private void executa() {
				
		LogNe log= new LogNe();
		LogDt logdt = new LogDt();
		
		try{
			if((h1.isReachable(pingDelay) || h2.isReachable(pingDelay) || h3.isReachable(pingDelay))) {																																							
				logdt = new LogDt("Internet","","","127.0.0.1", String.valueOf(LogTipoDt.InternetUp),"","");															
			}else {
				logdt = new LogDt("Internet","","","127.0.0.1", String.valueOf(LogTipoDt.InternetDown),"","");				
			}
			
			log.salvar(logdt);
			
		} catch(Exception e) {
//			log.error(e);
			
			notificaAdministradores(e);
		}
	}


	private void notificaAdministradores(Exception e) {	
		ByteArrayOutputStream out = null;
		try{
			List admins = new UsuarioNe().getAdministradores();
			
			Iterator it = admins.iterator();
			String nomes[] = new String[admins.size()];
			String emails[] = new String[admins.size()];
			int i=0;
			while(it.hasNext()) {
				UsuarioNe admin = (UsuarioNe) it.next();
				nomes[i] = admin.getUsuario();
				emails[i] = admin.getUsuarioDt().getEMail();
				i++;
			}
			
			out = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(out));
	
			Message m = carteiro.prepararEmailTexto(nomes, projudiConfiguration.getEMailRemetente(), "Sistema Projudi", new String(out.toByteArray()), "Erro da execução automática", emails);
			carteiro.enviarEmail(m);
			out.close();
		} catch(Exception e1) {
			try{if (out!=null) out.close(); } catch(Exception ex ) {};			
		}
		
	}
}
