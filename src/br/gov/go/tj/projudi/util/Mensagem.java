/*
 * Created on 3 f�vr. 2005
 *
 * Este arquivo � parte do Projudi. 
 * N�o pode ser distribu�do.
 */
package br.gov.go.tj.projudi.util;

/**
 * @author Andr� Luis C. Moreira e Leandro Lima Lira
 * Esta classe tem a fun��o de nos auxiliar na manipula��o
 * de mensagens a serem exibidas ao usu�rio.  
 * 
 */
public class Mensagem {

	String mensagem;
	
	/**
	 * M�todo construtor. Inicializa
	 * o atributo com um string vazio.
	 *
	 */
	public Mensagem() {
		this.mensagem = "";
	}
	
	/**
	 * M�todo construtor
	 * @param msg Valor para o atributo de mensagem
	 */
	public Mensagem(String msg){
		this.setMensagem(msg);
	}
	
	/**
	 * M�todo que retorna o conte�do da mensagem
	 * @return O conte�do da mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * M�todo que seta o conte�do da mensagem
	 * @param string Conte�do da mensagem
	 */
	public void setMensagem(String string) {
		mensagem = string;
	}

}
