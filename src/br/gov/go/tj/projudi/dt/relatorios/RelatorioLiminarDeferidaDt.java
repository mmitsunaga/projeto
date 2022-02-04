package br.gov.go.tj.projudi.dt.relatorios;

import java.util.Date;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para armazenar processo não julgado com liminar deferida
 * 
 * @author lsrodrigues
 * 
 */
public class RelatorioLiminarDeferidaDt extends Dados {

	private static final long serialVersionUID = 295719560779810332L;
	
	private String id_proc;
	private String proc_numero;
	private Date aceiteLiminar;
	private String serv;
	private String gabinete;
	private String nome;
	private String tempo;
	
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioLiminarDeferidaDt() {
		limpar();
	}

	/**
	 * Define os valores de todas as variáveis como nulo
	 */
	public void limpar() {
		
		id_proc = "";
		proc_numero = "";
		aceiteLiminar = new Date();
		serv = "";
		gabinete = "";
		nome = "";
		tempo = "";

	}

	public String getId_proc() {
		return id_proc;
	}

	public void setId_proc(String id_proc) {
		if ( id_proc != null) this.id_proc = id_proc;
	}

	public String getProc_numero() {
		return proc_numero;
	}

	public void setProc_numero(String proc_numero) {
		if ( proc_numero != null) this.proc_numero = proc_numero;
	}

	public Date getAceiteLiminar() {
		return aceiteLiminar;
	}

	public void setAceiteLiminar(Date aceiteLiminar) {
		if (aceiteLiminar != null) this.aceiteLiminar = aceiteLiminar;
	}

	public String getServ() {
		return serv;
	}

	public void setServ(String serv) {
		if ( serv != null) this.serv = serv;
	}

	public String getGabinete() {
		return gabinete;
	}

	public void setGabinete(String gabinete) {
		if ( gabinete != null) this.gabinete = gabinete;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null) this.nome = nome;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		if ( tempo != null) this.tempo = tempo;
	}


	public String getId() {
		return null;
	}

	public void setId(String id) {
	}
	
	
}