package br.gov.go.tj.projudi.ne.correios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjetoCorreios implements Serializable {

	private static final long serialVersionUID = 5480291413462538679L;
	
	private String numero;
	
	private String erro;
	
	private List<EventoCorreios> eventos = new ArrayList<EventoCorreios>();
    
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public List<EventoCorreios> getEventos() {
		return eventos;
	}

	public void adicioneEvento(EventoCorreios evento) {
		this.eventos.add(evento);
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
}
