package br.gov.go.tj.projudi.webservice;

import java.io.Serializable;

public class RespostaTJGO implements Serializable {

	private static final long serialVersionUID = -4245890242639261014L;
	
	private boolean Sucesso;
	private String Mensagem;

	public RespostaTJGO() {
		this.Sucesso = true;
		this.Mensagem = "";
	}

	public boolean isSucesso() {
		return Sucesso;
	}

	public void setSucesso(boolean sucesso) {
		Sucesso = sucesso;
	}

	public String getMensagem() {
		return Mensagem;
	}

	public void setMensagem(String mensagem) {
		Mensagem = mensagem;
	}
}
