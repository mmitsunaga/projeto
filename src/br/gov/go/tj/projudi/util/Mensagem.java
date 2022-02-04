/*
 * Created on 3 févr. 2005
 *
 * Este arquivo é parte do Projudi. 
 * Não pode ser distribuído.
 */
package br.gov.go.tj.projudi.util;

/**
 * @author André Luis C. Moreira e Leandro Lima Lira
 * Esta classe tem a função de nos auxiliar na manipulação
 * de mensagens a serem exibidas ao usuário.  
 * 
 */
public class Mensagem {

	String mensagem;
	
	/**
	 * Método construtor. Inicializa
	 * o atributo com um string vazio.
	 *
	 */
	public Mensagem() {
		this.mensagem = "";
	}
	
	/**
	 * Método construtor
	 * @param msg Valor para o atributo de mensagem
	 */
	public Mensagem(String msg){
		this.setMensagem(msg);
	}
	
	/**
	 * Método que retorna o conteúdo da mensagem
	 * @return O conteúdo da mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * Método que seta o conteúdo da mensagem
	 * @param string Conteúdo da mensagem
	 */
	public void setMensagem(String string) {
		mensagem = string;
	}

}
