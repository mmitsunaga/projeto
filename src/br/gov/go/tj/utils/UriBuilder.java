package br.gov.go.tj.utils;

import java.util.LinkedList;
import java.util.Queue;


public class UriBuilder {
	private String host;
	private Queue<String> args;
	private Queue<String> argsValue;
	private String path;
	
	public UriBuilder(String url) {
		this.host = url;
		args = new LinkedList<String>();
		argsValue = new LinkedList<String>();
		path = "";
	}
	public UriBuilder(String url, String path) {
		this.host = url;
		args = new LinkedList<String>();
		argsValue = new LinkedList<String>();
		this.path = path;
	}
	
	public void adicionarArgumento(String arg,String valor) {
		
		args.add(arg);
		argsValue.add(valor);
	}
	
	public String getURI() {
		StringBuilder sb = new StringBuilder(host);
		sb.append(path);
		sb.append("?");
		Queue<String> aux = argsValue;
		for(String a : args) {
			sb.append(a);
			sb.append("=");
			String valor = aux.poll();
			valor = valor.replaceAll(" ", "%20");
			sb.append(valor);
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() -1);
		
				
		return sb.toString();
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
