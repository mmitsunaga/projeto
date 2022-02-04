package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class EscalaMgDt extends EscalaMgDtGen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1698125891312487313L;

	public static final int CodigoPermissao = 303;
	
	public static final String ID_ESCALA_TIPO_CENOPS = "1";
	public static final String ID_ESCALA_TIPO_PLANTAO = "2";

	private String usuario;
	private String escalaTipoMg;
	private String cpfUsuario;
	
	private String idUsuario;
	private String comarca;
	private String infoConta;
	private int banco;
	private String nomeBanco;
	private int agencia;
	private int contaOperacao;
	private int conta;
	private String contaDv;

	public void limpar() {
		idEscalaMg = "";
		idUsuario = "";
		cpfUsuario = "";
		idEscalaTipoMg = "";
		dataInicio = "";
		dataFim = "";
		CodigoTemp = "";
		usuario = "";
		escalaTipoMg = "";
		comarca = "";
		infoConta = "";
		banco = 0;
		nomeBanco = "";
		agencia = 0;
		contaOperacao = 0;
		conta = 0;
		contaDv = "";
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getEscalaTipoMg() {
		return escalaTipoMg;
	}

	public void setEscalaTipoMg(String escalaTipoMg) {
		this.escalaTipoMg = escalaTipoMg;
	}

	public String getCpfUsuario() {
		return cpfUsuario;
	}

	public void setCpfUsuario(String cpfUsuario) {
		this.cpfUsuario = cpfUsuario;
	}

 

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getInfoConta() {
		return infoConta;
	}

	public void setInfoConta(String infoConta) {
		this.infoConta = infoConta;
	}

	public int getBanco() {
		return banco;
	}

	public void setBanco(int banco) {
		this.banco = banco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public int getAgencia() {
		return agencia;
	}

	public void setAgencia(int agencia) {
		this.agencia = agencia;
	}

	public int getContaOperacao() {
		return contaOperacao;
	}

	public void setContaOperacao(int contaOperacao) {
		this.contaOperacao = contaOperacao;
	}

	public int getConta() {
		return conta;
	}

	public void setConta(int conta) {
		this.conta = conta;
	}

	public String getContaDv() {
		return contaDv;
	}

	public void setContaDv(String contaDv) {
		this.contaDv = contaDv;
	}


		
}
