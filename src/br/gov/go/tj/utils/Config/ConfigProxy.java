package br.gov.go.tj.utils.Config;

import java.io.Serializable;

/**
 * 
 * Classe:     ConfigProxy.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Objeto que representa as configurações de acesso ao proxy   
 *             
 */
public class ConfigProxy implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8675853101438520337L;
	
	private String proxyURL;
	private String proxyPort;
	private String proxyUserName;
	private String proxyPassword;
	
	public ConfigProxy(){}

	public void setProxyURL(String proxyURL) {
		this.proxyURL = proxyURL;
	}

	public String getProxyURL() {
		return proxyURL;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyUserName(String userName) {
		this.proxyUserName = userName;
	}

	public String getProxyUserName() {
		return proxyUserName;
	}

	public void setProxyPassword(String password) {
		this.proxyPassword = password;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}
	
	
}
