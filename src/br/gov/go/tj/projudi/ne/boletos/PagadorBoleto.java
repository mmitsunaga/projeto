package br.gov.go.tj.projudi.ne.boletos;

import java.io.Serializable;

import br.gov.go.tj.utils.Funcoes;

public class PagadorBoleto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6811376829942401872L;
		
	public static String TIPO_PESSOA_FISICA = "F";	
	public static String TIPO_PESSOA_JURIDICA = "J";
	
	private String nome = "";
	private String cpf = "";
	private String razaoSocial = "";
	private String cnpj = "";
	private String logradouro = "";
	private String bairro = "";
	private String cidade = "";
	private String uf = "";
	private String cep = "";
	private String tipoPessoa;
	private boolean mudouPagador;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		if (Funcoes.isNotStringVazia(nome)) {
			if (Funcoes.isNotStringVazia(this.nome) && Funcoes.isStringDiferente(this.nome,nome)) 
			{
				mudouPagador = true;
			}
			this.nome = nome;
			this.razaoSocial = "";
		}
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		if (Funcoes.isNotStringVazia(cpf)) {
			if (Funcoes.isNotStringVazia(this.cpf) && Funcoes.isStringDiferente(this.cpf,cpf)) 
			{
				mudouPagador = true;
			}
			this.cpf = cpf;
			this.cnpj = "";
		}
	}
	
	public String getRazaoSocial() {
		return razaoSocial;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		if (Funcoes.isNotStringVazia(razaoSocial)) {
			if (Funcoes.isNotStringVazia(this.razaoSocial) && Funcoes.isStringDiferente(this.razaoSocial,razaoSocial)) 
			{
				mudouPagador = true;
			}
			this.razaoSocial = razaoSocial;
			this.nome = "";
		}
	}
	
	public String getCnpj() {
		return cnpj;
	}
	
	public void setCnpj(String cnpj) {
		if (Funcoes.isNotStringVazia(cnpj)) {
			if (Funcoes.isNotStringVazia(this.cnpj) && Funcoes.isStringDiferente(this.cnpj,cnpj)) 
			{
				mudouPagador = true;
			}						
			this.cnpj = cnpj;
			this.cpf = "";
		}
	}
	
	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		if (logradouro != null)
			this.logradouro = logradouro;
	}
	
	public String getBairro() {
		return bairro;
	}
	
	public void setBairro(String bairro) {
		if (bairro != null)
			this.bairro = bairro;
	}
	
	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
		if (cidade != null)
			this.cidade = cidade;
	}
	
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		if (uf != null)
			this.uf = uf;
	}
	
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		if (cep != null)
			this.cep = cep;
	}
	
	public void setTipoPessoa(String tipoPessoa) {
		if (Funcoes.isNotStringVazia(tipoPessoa)) {
			if (Funcoes.isNotStringVazia(this.tipoPessoa) && Funcoes.isStringDiferente(this.tipoPessoa,tipoPessoa)) {
				mudouPagador = true;
			}
			this.tipoPessoa = tipoPessoa;
		}
	}
	
	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = TIPO_PESSOA_FISICA;
			if (this.cnpj != null && this.cnpj.length() > 0) {
				tipoPessoa = TIPO_PESSOA_JURIDICA;	
			}
		}
		return tipoPessoa;
	}
	
	public boolean isPessoaFisica() {
		return getTipoPessoa().equalsIgnoreCase(TIPO_PESSOA_FISICA);
	}

	public boolean isMudouPagador() {
		return mudouPagador;
	}

	public void setMudouPagador(boolean mudouPagador) {
		this.mudouPagador = mudouPagador;
	}
}
