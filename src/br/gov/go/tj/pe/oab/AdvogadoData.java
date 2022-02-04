package br.gov.go.tj.pe.oab;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AdvogadoData")
public class AdvogadoData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7631806440742242360L;
	@XmlElement(name = "NumeroSeguranca")
	String NumeroSeguranca;
	@XmlElement(name = "Uf")
	String Uf;
	@XmlElement(name = "Organizacao")
	String Organizacao;
	@XmlElement(name = "Nome")
	String Nome;
	@XmlElement(name = "NomePai")
	String NomePai;
	@XmlElement(name = "NomeMae")
	String NomeMae;
	@XmlElement(name = "Inscricao")
	String Inscricao;
	@XmlElement(name = "Cpf")
	String Cpf;
	@XmlElement(name = "TipoInscricao")
	String TipoInscricao;
	@XmlElement(name = "CodigoSituacao")
	String CodigoSituacao;
	@XmlElement(name = "Situacao")
	String Situacao;
	@XmlElement(name = "Logradouro")
	String Logradouro;
	@XmlElement(name = "Bairro")
	String Bairro;
	@XmlElement(name = "Cidade")
	String Cidade;
	@XmlElement(name = "Cep")
	String Cep;
	@XmlElement(name = "Email")
	String Email;
	@XmlElement(name = "DDD")
	String CodigoDeArea;
	@XmlElement(name = "Telefone")
	String Telefone;
	
	public String getNumeroSeguranca() {
		return NumeroSeguranca;
	}
	
	public void setNumeroSeguranca(String numeroSeguranca) {
		NumeroSeguranca = numeroSeguranca;
	}
	
	public String getUf() {
		return Uf;
	}
	
	public void setUf(String uf) {
		Uf = uf;
	}
	
	public String getOrganizacao() {
		return Organizacao;
	}
	
	public void setOrganizacao(String organizacao) {
		Organizacao = organizacao;
	}
	
	public String getNome() {
		return Nome;
	}
	
	public void setNome(String nome) {
		Nome = nome;
	}
	
	public String getNomePai() {
		return NomePai;
	}
	
	public void setNomePai(String nomePai) {
		NomePai = nomePai;
	}
	
	public String getNomeMae() {
		return NomeMae;
	}
	
	public void setNomeMae(String nomeMae) {
		NomeMae = nomeMae;
	}
	
	public String getInscricao() {
		return Inscricao;
	}
	
	public void setInscricao(String inscricao) {
		Inscricao = inscricao;
	}
	
	public String getCpf() {
		return Cpf;
	}
	
	public void setCpf(String cpf) {
		Cpf = cpf;
	}
	
	public String getTipoInscricao() {
		return TipoInscricao;
	}
	
	public void setTipoInscricao(String tipoInscricao) {
		TipoInscricao = tipoInscricao;
	}
	
	public String getCodigoSituacao() {
		return CodigoSituacao;
	}
	
	public void setCodigoSituacao(String codigoSituacao) {
		CodigoSituacao = codigoSituacao;
	}
	
	public String getSituacao() {
		return Situacao;
	}
	
	public void setSituacao(String situacao) {
		Situacao = situacao;
	}
	
	public String getLogradouro() {
		return Logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		Logradouro = logradouro;
	}
	
	public String getBairro() {
		return Bairro;
	}
	
	public void setBairro(String bairro) {
		Bairro = bairro;
	}
	
	public String getCidade() {
		return Cidade;
	}
	
	public void setCidade(String cidade) {
		Cidade = cidade;
	}
	
	public String getCep() {
		return Cep;
	}
	
	public void setCep(String cep) {
		Cep = cep;
	}
	
	public String getEmail() {
		return Email;
	}
	
	public void setEmail(String email) {
		Email = email;
	}
	
	public String getCodigoDeArea() {
		return CodigoDeArea;
	}
	
	public void setCodigoDeArea(String DDD) {
		this.CodigoDeArea = DDD;
	}
	
	public String getTelefone() {
		return Telefone;
	}
	
	public void setTelefone(String telefone) {
		Telefone = telefone;
	}
}
