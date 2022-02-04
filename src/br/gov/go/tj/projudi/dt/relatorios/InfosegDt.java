package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação dos relatórios analíticos
 */
public class InfosegDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissaoInfoseg = 662;

	// variáveis da consulta (dados)
	private String idProcesso;
	private String numeroProcesso;
	private String nomeSentenciado;
	private String nomePai;
	private String nomeMae;
	private String cidadeNascimento;
	private String ufNascimento;
	private String numeroRg;
	private String siglaRg;
	private String cpf;
	private String foragido;
	
	// variáveis da consulta (processos)
	private String tempoPenaAno;
	private String tempoPenaMes;
	private String tempoPenaDia;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public InfosegDt() {
		limpar();
	}

	public void limpar() {
		idProcesso = "";
		numeroProcesso = "";
		nomeSentenciado = "";
		nomePai = "";
		nomeMae = "";
		cidadeNascimento = "";
		ufNascimento = "";
		numeroRg = "";
		siglaRg = "";
		cpf = "";
		foragido = "";
		tempoPenaAno = "";
		tempoPenaMes = "";
		tempoPenaDia = "";
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {
		
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getNumeroCompletoProcesso() {
		return numeroProcesso;
	}

	public void setNumeroCompletoProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getNomeSentenciado() {
		return nomeSentenciado;
	}

	public void setNomeSentenciado(String nomeSentenciado) {
		this.nomeSentenciado = nomeSentenciado;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getCidadeNascimento() {
		return cidadeNascimento;
	}

	public void setCidadeNascimento(String cidadeNascimento) {
		this.cidadeNascimento = cidadeNascimento;
	}

	public String getUfNascimento() {
		return ufNascimento;
	}

	public void setUfNascimento(String ufNascimento) {
		this.ufNascimento = ufNascimento;
	}

	public String getNumeroRg() {
		return numeroRg;
	}

	public void setNumeroRg(String numeroRg) {
		this.numeroRg = numeroRg;
	}

	public String getSiglaRg() {
		return siglaRg;
	}

	public void setSiglaRg(String siglaRg) {
		this.siglaRg = siglaRg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getForagido() {
		return foragido;
	}

	public void setForagido(String foragido) {
		this.foragido = foragido;
	}

	public String getTempoPenaAno() {
		return tempoPenaAno;
	}

	public void setTempoPenaAno(String tempoPenaAno) {
		this.tempoPenaAno = tempoPenaAno;
	}

	public String getTempoPenaMes() {
		return tempoPenaMes;
	}

	public void setTempoPenaMes(String tempoPenaMes) {
		this.tempoPenaMes = tempoPenaMes;
	}

	public String getTempoPenaDia() {
		return tempoPenaDia;
	}

	public void setTempoPenaDia(String tempoPenaDia) {
		this.tempoPenaDia = tempoPenaDia;
	}

}