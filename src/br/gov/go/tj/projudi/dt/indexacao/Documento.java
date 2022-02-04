package br.gov.go.tj.projudi.dt.indexacao;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que encapsula os dados de uma publicação no elasticsearch
 * @author mmitsunaga
 *
 */
public class Documento {

	@JsonProperty(value = "numero_processo")
	private String numero;	

	@JsonProperty(value = "id_arq")
	private String id_arquivo;
	
	@JsonProperty(value = "id_movi")
	private String id_movi;
	
	@JsonProperty(value = "id_serv")
	private String id_serv;
	
	@JsonProperty(value = "serv")
	private String serv;
	
	@JsonProperty(value = "id_usu")
	private String id_usu;
	
	@JsonProperty(value = "realizador")
	private String realizador;
	
	@JsonProperty(value = "id_tipo_arq")
	private String id_tipo_arq;
	
	@JsonProperty(value = "tipo_arq")
	private String tipo_arq;
	
	@JsonProperty(value = "data_publicacao")
	private String dataPublicacao;
	
	@JsonProperty(value = "data_indexacao")
	private String dataIndexacao;
	
	@JsonProperty(value = "texto")
	private String texto = null;
	
	@JsonProperty(value = "extra")
	private String extra = null;
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getId_arquivo() {
		return id_arquivo;
	}

	public void setId_arquivo(String id_arquivo) {
		this.id_arquivo = id_arquivo;
	}
	
	public String getId_movi() {
		return id_movi;
	}

	public void setId_movi(String id_movi) {
		this.id_movi = id_movi;
	}

	public String getId_serv() {
		return id_serv;
	}

	public void setId_serv(String id_serv) {
		this.id_serv = id_serv;
	}

	public String getServ() {
		return serv;
	}

	public void setServ(String serv) {
		this.serv = serv;
	}
	
	public String getId_usu() {
		return id_usu;
	}

	public void setId_usu(String id_usu) {
		this.id_usu = id_usu;
	}

	public String getRealizador() {
		return realizador;
	}

	public void setRealizador(String realizador) {
		this.realizador = realizador;
	}

	public String getId_tipo_arq() {
		return id_tipo_arq;
	}

	public void setId_tipo_arq(String id_tipo_arq) {
		this.id_tipo_arq = id_tipo_arq;
	}

	public String getTipo_arq() {
		return tipo_arq;
	}

	public void setTipo_arq(String tipo_arq) {
		this.tipo_arq = tipo_arq;
	}

	public String getDataPublicacao() {
		return ValidacaoUtil.isNaoVazio(this.dataPublicacao) ? "Publicado em " + dataPublicacao : this.dataPublicacao;			
	}

	public void setDataPublicacao(String dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getDataIndexacao() {
		return dataIndexacao;
	}

	public void setDataIndexacao(String dataIndexacao) {
		this.dataIndexacao = dataIndexacao;
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getCodigoHash() throws Exception {
		return Funcoes.SenhaMd5(getId_arquivo());			
	}
	
}
