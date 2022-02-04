package br.gov.go.tj.projudi.ne.correios;

import java.io.Serializable;

import br.gov.go.tj.utils.ValidacaoUtil;

public class EventoCorreios implements Serializable {

	private static final long serialVersionUID = 6796440592466748685L;
	
	private String descricao;
	
	private String detalhe;
	
	private String tipo;
	
	private String status;
	
	private String data;
	
	private String hora;
	
	private String local;
	
	private String cidade;
	
	private String uf;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDetalhe() {
		return detalhe;
	}

	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
	
	public String getDataHoraLocal(){
		String local = ValidacaoUtil.isVazio(getCidade()) ? getLocal() + " (" + getUf() + ")": getCidade() + " (" + getUf() + ")";
		return getData() + " " + getHora() + " " + local;
	}
	
	public String getDescricaoDetalhe(){
		String detalhe = ValidacaoUtil.isNaoVazio(getDetalhe()) ? ". " + getDetalhe() : "";
		return getDescricao() + detalhe;
	}
	
}