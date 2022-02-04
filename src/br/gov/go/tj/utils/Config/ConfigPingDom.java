package br.gov.go.tj.utils.Config;

import java.io.Serializable;

/**
 * 
 * Classe:     ConfigPingDom.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Objeto que representa as informações de acesso ao Pingdom   
 *             
 */
public class ConfigPingDom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2111256181223231424L;
	
	public ConfigPingDom(){}
	
	private String pingdomURL;
	private String pingdomUserName;
	private String pingdomPassword;
	private String pingdomAPIKey;
	private String pingdomApplicationName;
	
	public void setPingdomURL(String pingdomURL) {
		this.pingdomURL = pingdomURL;
	}
	public String getPingdomURL() {
		return pingdomURL;
	}

	public void setPingdomUserName(String pingdomUserName) {
		this.pingdomUserName = pingdomUserName;
	}

	public String getPingdomUserName() {
		return pingdomUserName;
	}

	public void setPingdomPassword(String pingdomPassword) {
		this.pingdomPassword = pingdomPassword;
	}

	public String getPingdomPassword() {
		return pingdomPassword;
	}

	public void setPingdomAPIKey(String pingdomAPIKey) {
		this.pingdomAPIKey = pingdomAPIKey;
	}

	public String getPingdomAPIKey() {
		return pingdomAPIKey;
	}

	public void setPingdomApplicationName(String pingdomApplicationName) {
		this.pingdomApplicationName = pingdomApplicationName;
	}

	public String getPingdomApplicationName() {
		return pingdomApplicationName;
	}

}
