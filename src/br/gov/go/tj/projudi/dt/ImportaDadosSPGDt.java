package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.dt.Dados;

public class ImportaDadosSPGDt extends Dados {

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
	private int idBairroRegiao;
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
	private int banco;
	private int agencia;
	private int agenciaDv;
	private int conta;
	private int contaDv;
	private int idZona;

	private int quantMandadoMesAnt1;
	private int quantMandadoMesAnt2;
	private int quantMandadoMesAnt3;
	private int quantMandadoMesAnt4;
	private int quantMandadoMesAnt5;
	private int quantMandadoMesAnt6;
	private int quantMandadoMesAnt7;
	private int mesAnoCompetencia;
	private int contaOperacao;

	// proc hibrido

	private String idProcHibrido;
	private String idProc;
	private String procNumeroFisico;
	private String descricaoFase;
	private String dataFase;
	private String dataPrimeiraSentenca;
	private String dataUltimaSentenca;
	private String dataConclusao;
	private String qtdeConclusao;
	private String qtdeSentenca;
	private String tipoConclusao;
	private String dataArquivoProvisorio;
	private String dataSuspensao;
	private String dataVencimentoSuspensao;
	private String dataContador;
	private String dataCargaMP;
	private String dataPrimeiraAudiencia;
	private String dataAudienciaEmAberto;
	private String infoProcuradoriaEstadual;

	// carrega spga-locomocoes

	private String statPagamento;
	private String numrMandado;
	private double valorLocomocao;
	private String dataInicioEnvio;
	private String dataValidacao;
	private String matricValidacao;
	private String numrCpfOficial;

	// controle guia saldo

	private double guiaSaldoValorAtualizado;
	private double guiaSaldoValorAnterior;
	private String guiaSaldoStatus;
	private String guiaSaldoStatusAnterior;
	private int qtdeLocomocaoUsada;
	private int qtdeLocomocaoNecessaria;
	private String numeroGuiaSaldo;
	private String numeroGuia;
	private double locomocaoValorUnitario;
	private int idGuiaSaldo;
	private int idMandJud;
	private int idLocomocao;
	private String procNumero;
	private int idGuiaItem;

	//

	public static int CODIGO_COMARCA_GOIANIA = 39;
	public static int CODIGO_GRUPO_DESENV = 68;
	public static int ID_GRUPO_DESENV = 69;
	public static int CODIGO_GRUPO_HOMOLOG = 68;
	public static int ID_GRUPO_HOMOLOG = 69;
	public static int CODIGO_GRUPO_PROD = 68;
	public static int ID_GRUPO_PROD = 67;

	public static int ID_CARGO_TIPO_DESENV = 49;
	public static int ID_CARGO_TIPO_HOMOLOG = 47;
	public static int ID_CARGO_TIPO_PROD = 0; // CRIAR

	public static int ID_ZONA_DESENV = 768; // nao precisa mais.
	public static int ID_ZONA_HOMOLOG = 0; // CRIAR
	public static int ID_ZONA_PROD = 0; // CRIAR

	public static int ID_SERV_TIPO_DESENV = 2;
	public static int ID_SERV_TIPO_HOMOLOG = 2;
	public static int ID_SERV_TIPO_PROD = 2;

	public static int ID_SERV_SUBTIPO_DESENV = 50;
	public static int ID_SERV_SUBTIPO_HOMOLOG = 71;
	public static int ID_SERV_SUBTIPO_PROD = 54;

	public static int ID_AREA_DESENV = 3;
	public static int ID_AREA_HOMOLOG = 3;
	public static int ID_AREA_PROD = 3;

	public static int ID_ENDERECO_GENERICO_DESENV = 815052;
	public static int ID_ENDERECO_GENERICO_PROD = 0; // CRIAR
	public static int ID_ENDERECO_GENERICO_HOMOLOG = 0; // CRIAR

	public static int ID_ESTADO_REPRESENTACAO_DESENV = 1;
	public static int ID_ESTADO_REPRESENTACAO_PROD = 1;
	public static int ID_ESTADO_REPRESENTACAO_HOMOLOG = 1;

	public static int ID_REGIAO_GENERICA_DESENV = 8407;
	public static int ID_REGIAO_GENERICA_PROD = 0; // CRIAR GENERICA
	public static int ID_REGIAO_GENERICA_HOMOLOG = 0; // CRIAR GENERICA

	public static final String GUIA_SALDO_STATUS_LIBERADA = "88";
	public static final String GUIA_SALDO_STATUS_NAO_LIBERADA = "89";

	public static final String STATUS_LOCOMOCAO_SPG_IMPORTADA_DO_PROJUDI = "7";

	public ImportaDadosSPGDt() {
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
		idBairroRegiao = 0;
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
		banco = 0;
		agencia = 0;
		agenciaDv = 0;
		conta = 0;
		contaDv = 0;
		idZona = 0;

		////// hibrido

		idProcHibrido = "";
		idProc = "";
		procNumeroFisico = "";
		descricaoFase = "";
		dataFase = "";
		dataPrimeiraSentenca = "";
		dataUltimaSentenca = "";
		qtdeSentenca = "";
		dataConclusao = "";
		qtdeConclusao = "";
		tipoConclusao = "";
		dataArquivoProvisorio = "";
		dataSuspensao = "";
		dataVencimentoSuspensao = "";
		dataContador = "";
		dataCargaMP = "";
		dataPrimeiraAudiencia = "";
		dataAudienciaEmAberto = "";

		// carrega spga-locomocoes

		statPagamento = "";
		numrMandado = "";
		valorLocomocao = 0;
		dataInicioEnvio = "";
		dataValidacao = "";
		matricValidacao = "";
		numrCpfOficial = "";

		// controle guia saldo

		guiaSaldoValorAtualizado = 0;
		guiaSaldoValorAnterior = 0;
		guiaSaldoStatus = "";
		guiaSaldoStatusAnterior = "";
		numeroGuiaSaldo = "";
		numeroGuia = "";
		qtdeLocomocaoUsada = 0;
		qtdeLocomocaoNecessaria = 0;
		locomocaoValorUnitario = 0;
		idGuiaSaldo = 0;
		idMandJud = 0;
		idLocomocao = 0;
		idGuiaItem = 0;
	}

	public int getMesAnoCompetencia() {
		return mesAnoCompetencia;
	}

	public void setMesAnoCompetencia(int mesAnoCompetencia) {
		this.mesAnoCompetencia = mesAnoCompetencia;
	}

	public int getContaOperacao() {
		return contaOperacao;
	}

	public void setContaOperacao(int contaOperacao) {
		this.contaOperacao = contaOperacao;
	}

	public int getQuantMandadoMesAnt1() {
		return quantMandadoMesAnt1;
	}

	public void setQuantMandadoMesAnt1(int quantMandadoMesAnt1) {
		this.quantMandadoMesAnt1 = quantMandadoMesAnt1;
	}

	public int getQuantMandadoMesAnt2() {
		return quantMandadoMesAnt2;
	}

	public void setQuantMandadoMesAnt2(int quantMandadoMesAnt2) {
		this.quantMandadoMesAnt2 = quantMandadoMesAnt2;
	}

	public int getQuantMandadoMesAnt3() {
		return quantMandadoMesAnt3;
	}

	public void setQuantMandadoMesAnt3(int quantMandadoMesAnt3) {
		this.quantMandadoMesAnt3 = quantMandadoMesAnt3;
	}

	public int getQuantMandadoMesAnt4() {
		return quantMandadoMesAnt4;
	}

	public void setQuantMandadoMesAnt4(int quantMandadoMesAnt4) {
		this.quantMandadoMesAnt4 = quantMandadoMesAnt4;
	}

	public int getQuantMandadoMesAnt5() {
		return quantMandadoMesAnt5;
	}

	public void setQuantMandadoMesAnt5(int quantMandadoMesAnt5) {
		this.quantMandadoMesAnt5 = quantMandadoMesAnt5;
	}

	public int getQuantMandadoMesAnt6() {
		return quantMandadoMesAnt6;
	}

	public void setQuantMandadoMesAnt6(int quantMandadoMesAnt6) {
		this.quantMandadoMesAnt6 = quantMandadoMesAnt6;
	}

	public int getQuantMandadoMesAnt7() {
		return quantMandadoMesAnt7;
	}

	public void setQuantMandadoMesAnt7(int quantMandadoMesAnt7) {
		this.quantMandadoMesAnt7 = quantMandadoMesAnt7;
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

	public int getIdBairroRegiao() {
		return idBairroRegiao;
	}

	public void setIdBairroRegiao(int idBairroRegiao) {
		this.idBairroRegiao = idBairroRegiao;
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

	public int getBanco() {
		return banco;
	}

	public void setBanco(int banco) {
		this.banco = banco;
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

	public int getContaDv() {
		return contaDv;
	}

	public void setContaDv(int contaDv) {
		this.contaDv = contaDv;
	}

	public String getNomeComarca() {
		return nomeComarca;
	}

	public void setNomeComarca(String nomeComarca) {
		this.nomeComarca = nomeComarca;
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

	public String getIdProcHibrido() {
		return idProcHibrido;
	}

	public void setIdProcHibrido(String idProcHibrido) {
		this.idProcHibrido = idProcHibrido;
	}

	public String getProcNumeroFisico() {
		return procNumeroFisico;
	}

	public void setProcNumeroFisico(String procNumeroFisico) {
		this.procNumeroFisico = procNumeroFisico;
	}

	public String getDescricaoFase() {
		return descricaoFase;
	}

	public void setDescricaoFase(String descricaoFase) {
		this.descricaoFase = descricaoFase;
	}

	public String getDataFase() {
		return dataFase;
	}

	public void setDataFase(String dataFase) {
		this.dataFase = dataFase;
	}

	public String getDataPrimeiraSentenca() {
		return dataPrimeiraSentenca;
	}

	public void setDataPrimeiraSentenca(String dataPrimeiraSentenca) {
		this.dataPrimeiraSentenca = dataPrimeiraSentenca;
	}

	public String getDataUltimaSentenca() {
		return dataUltimaSentenca;
	}

	public void setDataUltimaSentenca(String dataUltimaSentenca) {
		this.dataUltimaSentenca = dataUltimaSentenca;
	}

	public String getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(String dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public String getQtdeConclusao() {
		return qtdeConclusao;
	}

	public void setQtdeConclusao(String qtdeConclusao) {
		this.qtdeConclusao = qtdeConclusao;
	}

	public String getQtdeSentenca() {
		return qtdeSentenca;
	}

	public void setQtdeSentenca(String qtdeSentenca) {
		this.qtdeSentenca = qtdeSentenca;
	}

	public String getTipoConclusao() {
		return tipoConclusao;
	}

	public void setTipoConclusao(String tipoConclusao) {
		this.tipoConclusao = tipoConclusao;
	}

	public String getDataArquivoProvisorio() {
		return dataArquivoProvisorio;
	}

	public void setDataArquivoProvisorio(String dataArquivoProvisorio) {
		this.dataArquivoProvisorio = dataArquivoProvisorio;
	}

	public String getDataSuspensao() {
		return dataSuspensao;
	}

	public void setDataSuspensao(String dataSuspensao) {
		this.dataSuspensao = dataSuspensao;
	}

	public String getDataVencimentoSuspensao() {
		return dataVencimentoSuspensao;
	}

	public void setDataVencimentoSuspensao(String dataVencimentoSuspensao) {
		this.dataVencimentoSuspensao = dataVencimentoSuspensao;
	}

	public String getDataContador() {
		return dataContador;
	}

	public void setDataContador(String dataContador) {
		this.dataContador = dataContador;
	}

	public String getDataCargaMP() {
		return dataCargaMP;
	}

	public void setDataCargaMP(String dataCargaMP) {
		this.dataCargaMP = dataCargaMP;
	}

	public String getDataPrimeiraAudiencia() {
		return dataPrimeiraAudiencia;
	}

	public void setDataPrimeiraAudiencia(String dataPrimeiraAudiencia) {
		this.dataPrimeiraAudiencia = dataPrimeiraAudiencia;
	}

	public String getDataAudienciaEmAberto() {
		return dataAudienciaEmAberto;
	}

	public void setDataAudienciaEmAberto(String dataAudienciaEmAberto) {
		this.dataAudienciaEmAberto = dataAudienciaEmAberto;
	}

	public String getIdProc() {
		return idProc;
	}

	public void setIdProc(String idProc) {
		this.idProc = idProc;
	}

	public String getInfoProcuradoriaEstadual() {
		return infoProcuradoriaEstadual;
	}

	public void setInfoProcuradoriaEstadual(String infoProcuradoriaEstadual) {
		this.infoProcuradoriaEstadual = infoProcuradoriaEstadual;
	}

	public String getStatPagamento() {
		return statPagamento;
	}

	public void setStatPagamento(String statPagamento) {
		this.statPagamento = statPagamento;
	}

	public double getValorLocomocao() {
		return valorLocomocao;
	}

	public void setValorLocomocao(double valorLocomocao) {
		this.valorLocomocao = valorLocomocao;
	}

	public String getDataInicioEnvio() {
		return dataInicioEnvio;
	}

	public void setDataInicioEnvio(String dataInicioEnvio) {
		this.dataInicioEnvio = dataInicioEnvio;
	}

	public String getDataValidacao() {
		return dataValidacao;
	}

	public void setDataValidacao(String dataValidacao) {
		this.dataValidacao = dataValidacao;
	}

	public String getMatricValidacao() {
		return matricValidacao;
	}

	public void setMatricValidacao(String matricValidacao) {
		this.matricValidacao = matricValidacao;
	}

	public String getNumrCpfOficial() {
		return numrCpfOficial;
	}

	public void setNumrCpfOficial(String numrCpfOficial) {
		this.numrCpfOficial = numrCpfOficial;
	}

	public String getNumrMandado() {
		return numrMandado;
	}

	public void setNumrMandado(String numrMandado) {
		this.numrMandado = numrMandado;
	}

	public String getGuiaSaldoStatus() {
		return guiaSaldoStatus;
	}

	public void setGuiaSaldoStatus(String guiaSaldoStatus) {
		this.guiaSaldoStatus = guiaSaldoStatus;
	}

	public int getQtdeLocomocaoUsada() {
		return qtdeLocomocaoUsada;
	}

	public void setQtdeLocomocaoUsada(int qtdeLocomocaoUsada) {
		this.qtdeLocomocaoUsada = qtdeLocomocaoUsada;
	}

	public int getQtdeLocomocaoNecessaria() {
		return qtdeLocomocaoNecessaria;
	}

	public void setQtdeLocomocaoNecessaria(int qtdeLocomocaoNecessaria) {
		this.qtdeLocomocaoNecessaria = qtdeLocomocaoNecessaria;
	}

	public int getIdGuiaSaldo() {
		return idGuiaSaldo;
	}

	public void setIdGuiaSaldo(int idGuiaSaldo) {
		this.idGuiaSaldo = idGuiaSaldo;
	}

	public int getIdZona() {
		return idZona;
	}

	public void setIdZona(int idZona) {
		this.idZona = idZona;
	}

	public double getGuiaSaldoValorAtualizado() {
		return guiaSaldoValorAtualizado;
	}

	public void setGuiaSaldoValorAtualizado(double guiaSaldoValorAtualizado) {
		this.guiaSaldoValorAtualizado = guiaSaldoValorAtualizado;
	}

	public double getLocomocaoValorUnitario() {
		return locomocaoValorUnitario;
	}

	public void setLocomocaoValorUnitario(double locomocaoValorUnitario) {
		this.locomocaoValorUnitario = locomocaoValorUnitario;
	}

	public double getGuiaSaldoValorAnterior() {
		return guiaSaldoValorAnterior;
	}

	public void setGuiaSaldoValorAnterior(double guiaSaldoValorAnterior) {
		this.guiaSaldoValorAnterior = guiaSaldoValorAnterior;
	}

	public int getIdMandJud() {
		return idMandJud;
	}

	public void setIdMandJud(int idMandJud) {
		this.idMandJud = idMandJud;
	}

	public String getGuiaSaldoStatusAnterior() {
		return guiaSaldoStatusAnterior;
	}

	public void setGuiaSaldoStatusAnterior(String guiaSaldoStatusAnterior) {
		this.guiaSaldoStatusAnterior = guiaSaldoStatusAnterior;
	}

	public String getProcNumero() {
		return procNumero;
	}

	public void setProcNumero(String procNumero) {
		this.procNumero = procNumero;
	}

	public String getNumeroGuiaSaldo() {
		return numeroGuiaSaldo;
	}

	public void setNumeroGuiaSaldo(String numeroGuiaSaldo) {
		this.numeroGuiaSaldo = numeroGuiaSaldo;
	}

	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public int getIdLocomocao() {
		return idLocomocao;
	}

	public void setIdLocomocao(int idLocomocao) {
		this.idLocomocao = idLocomocao;
	}

	public int getIdGuiaItem() {
		return idGuiaItem;
	}

	public void setIdGuiaItem(int idGuiaItem) {
		this.idGuiaItem = idGuiaItem;
	}

	

}