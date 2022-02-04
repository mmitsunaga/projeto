package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.dt.Dados;

public class ImportaEscalaOficiaisDt extends Dados {

	private static final long serialVersionUID = -84043433146212071L;

	public static final int CodigoPermissao = 922;

	private int idComarca;
	private int idUsuario;
	private int idBanco;
	private String nomeBanco;
	private int idServentia;
	private String nomeServentia;
	private String codigoServentia;
	private int idTipoEscala;
	private int idTipoMandado;
	private int idRegiao;
	private int codgRegiao;
	private int idUsuServ;
	private int idUsuServGrupo;
	private int idGrupo;
	private int idBairro;
	private String cargoTipo;
	private int idCargoTipo;
	private int idServentiaCargo;
	private int codigoGrupo;
	private String nomeGrupo;
	private String nomeEscala;
	private String nomeRegiao;
	private String nomeTipoMandado;
	private String nomeCargoTipo;
	private int quantMandados;
	private int idZonaBairroRegiao;
	private int idEscala;
	private int codgComarca;
	private int idServCargoEscala;
	private String operacao;
	private String nomeBairro;
	private String nomeCidade;
	private String dataDist;
	private String nomeUsuario;
	private String cpfUsuario;
	private String nomeComarca;
	private int codigoBanco;
	private int agencia;
	private int idAgencia;
	private String nomeAgencia;
	private int agenciaDv;
	private int conta;
	private String contaDv;
	private int idZona;
	private int idCidade;
	private int idEndereco;
	private String statusConta;
	//
	// guia saldo
	//
	private String numeroGuiaCompleto;
	private String idProc;
	private String guiaSaldoStatus;
	private double guiaSaldoValor;

	public static int ID_GRUPO_DESENV = 69;
	public static int ID_GRUPO_HOMOLOG = 69;
	public static int ID_GRUPO_PROD = 67;

	public static int ID_ENDERECO_DESENV = 785326;
	public static int ID_ENDERECO_HOMOLOG = 785032;
	public static int ID_ENDERECO_PROD = 9475093;

	public static int ID_CARGO_TIPO_DESENV = 49;
	public static int ID_CARGO_TIPO_HOMOLOG = 47;
	public static int ID_CARGO_TIPO_PROD = 89;  
	
	public static int ID_SERV_TIPO_DESENV = 2;
	public static int ID_SERV_TIPO_HOMOLOG = 2;
	public static int ID_SERV_TIPO_PROD = 2;

	public static int ID_SERV_SUBTIPO_DESENV = 50;
	public static int ID_SERV_SUBTIPO_HOMOLOG = 71;
	public static int ID_SERV_SUBTIPO_PROD = 50;

	public static int ID_AREA_DESENV = 3;
	public static int ID_AREA_HOMOLOG = 3;
	public static int ID_AREA_PROD = 3;

	public ImportaEscalaOficiaisDt() {
		limpar();
	}

	public void limpar() {

		nomeBanco = "";
		idBanco = 0;
		idUsuario = 0;
		idComarca = 0;
		idServentia = 0;
		nomeServentia = "";
		codigoServentia = "";
		idTipoEscala = 0;
		idUsuServ = 0;
		idUsuServGrupo = 0;
		idGrupo = 0;
		idBairro = 0;
		cargoTipo = "";
		idCargoTipo = 0;
		idServentiaCargo = 0;
		codigoGrupo = 0;
		nomeGrupo = "";
		idRegiao = 0;
		codgRegiao = 0;
		idTipoEscala = 0;
		codgComarca = 0;
		idZonaBairroRegiao = 0;
		idServCargoEscala = 0;
		idEscala = 0;
		nomeEscala = "";
		nomeTipoMandado = "";
		nomeBairro = "";
		nomeRegiao = "";
		nomeCargoTipo = "";
		nomeCidade = "";
		quantMandados = 0;
		operacao = "";
		dataDist = "";
		nomeUsuario = "";
		cpfUsuario = "";
		nomeComarca = "";
		codigoBanco = 0;
		agencia = 0;
		agenciaDv = 0;
		conta = 0;
		contaDv = "";
		idZona = 0;
		idCidade = 0;
		idEndereco = 0;
		statusConta = "";
		idAgencia = 0;
		nomeAgencia = "";

		numeroGuiaCompleto = "";
		idProc = "";
		guiaSaldoValor = 0;
		guiaSaldoStatus = "";
	}

	public int getIdComarca() {
		return idComarca;
	}

	public void setIdComarca(int idComarca) {
		this.idComarca = idComarca;
	}

	public int getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(int idBanco) {
		this.idBanco = idBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public int getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(int idServentia) {
		this.idServentia = idServentia;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public String getCodigoServentia() {
		return codigoServentia;
	}

	public void setCodigoServentia(String codigoServentia) {
		this.codigoServentia = codigoServentia;
	}

	public int getIdTipoEscala() {
		return idTipoEscala;
	}

	public void setIdTipoEscala(int idTipoEscala) {
		this.idTipoEscala = idTipoEscala;
	}

	public int getIdTipoMandado() {
		return idTipoMandado;
	}

	public void setIdTipoMandado(int idTipoMandado) {
		this.idTipoMandado = idTipoMandado;
	}

	public int getIdRegiao() {
		return idRegiao;
	}

	public void setIdRegiao(int idRegiao) {
		this.idRegiao = idRegiao;
	}

	public int getCodgRegiao() {
		return codgRegiao;
	}

	public void setCodgRegiao(int codgRegiao) {
		this.codgRegiao = codgRegiao;
	}

	public int getIdUsuServ() {
		return idUsuServ;
	}

	public void setIdUsuServ(int idUsuServ) {
		this.idUsuServ = idUsuServ;
	}

	public int getIdUsuServGrupo() {
		return idUsuServGrupo;
	}

	public void setIdUsuServGrupo(int idUsuServGrupo) {
		this.idUsuServGrupo = idUsuServGrupo;
	}

	public int getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(int idGrupo) {
		this.idGrupo = idGrupo;
	}

	public int getIdBairro() {
		return idBairro;
	}

	public void setIdBairro(int idBairro) {
		this.idBairro = idBairro;
	}

	public String getCargoTipo() {
		return cargoTipo;
	}

	public void setCargoTipo(String cargoTipo) {
		this.cargoTipo = cargoTipo;
	}

	public int getIdCargoTipo() {
		return idCargoTipo;
	}

	public void setIdCargoTipo(int idCargoTipo) {
		this.idCargoTipo = idCargoTipo;
	}

	public int getIdServentiaCargo() {
		return idServentiaCargo;
	}

	public void setIdServentiaCargo(int idServentiaCargo) {
		this.idServentiaCargo = idServentiaCargo;
	}

	public int getCodigoGrupo() {
		return codigoGrupo;
	}

	public void setCodigoGrupo(int codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}

	public String getNomeGrupo() {
		return nomeGrupo;
	}

	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}

	public String getNomeEscala() {
		return nomeEscala;
	}

	public void setNomeEscala(String nomeEscala) {
		this.nomeEscala = nomeEscala;
	}

	public String getNomeRegiao() {
		return nomeRegiao;
	}

	public void setNomeRegiao(String nomeRegiao) {
		this.nomeRegiao = nomeRegiao;
	}

	public String getNomeTipoMandado() {
		return nomeTipoMandado;
	}

	public void setNomeTipoMandado(String nomeTipoMandado) {
		this.nomeTipoMandado = nomeTipoMandado;
	}

	public String getNomeCargoTipo() {
		return nomeCargoTipo;
	}

	public void setNomeCargoTipo(String nomeCargoTipo) {
		this.nomeCargoTipo = nomeCargoTipo;
	}

	public int getQuantMandados() {
		return quantMandados;
	}

	public void setQuantMandados(int quantMandados) {
		this.quantMandados = quantMandados;
	}

	public int getIdZonaBairroRegiao() {
		return idZonaBairroRegiao;
	}

	public void setIdZonaBairroRegiao(int idZonaBairroRegiao) {
		this.idZonaBairroRegiao = idZonaBairroRegiao;
	}

	public int getIdEscala() {
		return idEscala;
	}

	public void setIdEscala(int idEscala) {
		this.idEscala = idEscala;
	}

	public int getCodgComarca() {
		return codgComarca;
	}

	public void setCodgComarca(int codgComarca) {
		this.codgComarca = codgComarca;
	}

	public int getIdServCargoEscala() {
		return idServCargoEscala;
	}

	public void setIdServCargoEscala(int idServCargoEscala) {
		this.idServCargoEscala = idServCargoEscala;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getNomeBairro() {
		return nomeBairro;
	}

	public void setNomeBairro(String nomeBairro) {
		this.nomeBairro = nomeBairro;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public String getDataDist() {
		return dataDist;
	}

	public void setDataDist(String dataDist) {
		this.dataDist = dataDist;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getCpfUsuario() {
		return cpfUsuario;
	}

	public void setCpfUsuario(String cpfUsuario) {
		this.cpfUsuario = cpfUsuario;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(int codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public int getAgencia() {
		return agencia;
	}

	public void setAgencia(int agencia) {
		this.agencia = agencia;
	}

	public int getAgenciaDv() {
		return agenciaDv;
	}

	public void setAgenciaDv(int agenciaDv) {
		this.agenciaDv = agenciaDv;
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

	public String getNomeComarca() {
		return nomeComarca;
	}

	public void setNomeComarca(String nomeComarca) {
		this.nomeComarca = nomeComarca;
	}

	public int getIdZona() {
		return idZona;
	}

	public void setIdZona(int idZona) {
		this.idZona = idZona;
	}

	public int getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(int idCidade) {
		this.idCidade = idCidade;
	}

	public int getIdEndereco() {
		return idEndereco;
	}

	public void setIdEndereco(int idEndereco) {
		this.idEndereco = idEndereco;
	}

	public String getStatusConta() {
		return statusConta;
	}

	public void setStatusConta(String statusConta) {
		this.statusConta = statusConta;
	}

	public int getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(int idAgencia) {
		this.idAgencia = idAgencia;
	}

	public String getNomeAgencia() {
		return nomeAgencia;
	}

	public void setNomeAgencia(String nomeAgencia) {
		this.nomeAgencia = nomeAgencia;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNumeroGuiaCompleto() {
		return numeroGuiaCompleto;
	}

	public void setNumeroGuiaCompleto(String numeroGuiaCompleto) {
		this.numeroGuiaCompleto = numeroGuiaCompleto;
	}

	public String getIdProc() {
		return idProc;
	}

	public void setIdProc(String idProc) {
		this.idProc = idProc;
	}

	public double getGuiaSaldoValor() {
		return guiaSaldoValor;
	}

	public void setGuiaSaldoValor(double guiaSaldoValor) {
		this.guiaSaldoValor = guiaSaldoValor;
	}

	public String getGuiaSaldoStatus() {
		return guiaSaldoStatus;
	}

	public void setGuiaSaldoStatus(String guiaSaldoStatus) {
		this.guiaSaldoStatus = guiaSaldoStatus;
	}

}