package br.gov.go.tj.projudi.dt.relatorios;

import java.util.Date;

import br.gov.go.tj.projudi.dt.Dados;

public class RelatoriosMandadoDt {

	private static final long serialVersionUID = -84043433146212071L;

	public static final int CodigoPermissao = 922;

	// relatorios Financeiros

	private String dataReferencia;
	private String tipoArquivo;

	private int mesAnoCompetencia;
	private int idUsuario;
	private int quantMandadoMesAnt1;
	private int quantMandadoMesAnt2;
	private int quantMandadoMesAnt3;
	private int quantMandadoMesAnt4;
	private int quantMandadoMesAnt5;
	private int quantMandadoMesAnt6;
	private int quantMandadoMesAnt7;
	private String escalaTipo;
	private String quantidadeLocomocao;
	private double valorLocomocao;
	private String infoConta;
	private String idGuiaEmis;
	private String numeroGuiaCompleto;
	private String dataEmis;

	private String nomeUsuario;
	private String cpfUsuario;
	private String nomeComarca;
	private String nomeBanco;
	private int somatorioMandados;
	private int mesesComMandado;
	private int mediaMandado;
	private String faixa;
	private double valorReceber;
	private int banco;
	private int agencia;
	private int agenciaDv;
	private int contaOperacao;
	private int conta;
	private String contaDv;
	private String processoNumero;
	private String idMandJud;
	private String assistencia;
	private String areaProcesso;
	private String mandTipo;
	private String statusPagamento;
	private String statusMandJud;
	private String dataInicial;
	private String dataFinal;
	private String dataDist;
	private String dataRetorno;
	private String nomeServentia;
	private String nomeCompanheiro;
	private int quantidade;
	private String diferencaDias;
	private String dataLimite;
	private String dataPagamentoStatus;
	private String valor;
	private String motivo;
	private String nomeAnt;
	private String nomeAtual;
	private String mandTipoRedist;
	private String idEscAnt;
	private String idEscAtual;

	// mandado gratuito faixa unica

	private int quantFaixaReceber;
	private double valorFaixaReceber;

	private int quantResolutivoReceber;
	double valorResolutivoReceber;

	private int quantAcimaFaixaReceber;
	private double valorAcimaFaixaReceber;

	private int resolutivo;
	
	private String analise;

	private String dataString;
	private String horaString;
	private int idUsuServ;

	//

	public RelatoriosMandadoDt() {
		limpar();
	}

	public void limparCamposTela() {
		tipoArquivo = "";
		dataReferencia = "";
	}

	public void limpar() {
		idUsuario = 0;
		quantMandadoMesAnt1 = 0;
		quantMandadoMesAnt2 = 0;
		quantMandadoMesAnt3 = 0;
		quantMandadoMesAnt4 = 0;
		quantMandadoMesAnt5 = 0;
		quantMandadoMesAnt6 = 0;
		quantMandadoMesAnt7 = 0;
		nomeUsuario = "";
		cpfUsuario = "";
		nomeComarca = "";
		nomeBanco = "";
		somatorioMandados = 0;
		mesesComMandado = 0;
		mediaMandado = 0;
		faixa = "";
		valorReceber = 0;
		banco = 0;
		contaOperacao = 0;
		agencia = 0;
		agenciaDv = 0;
		conta = 0;
		contaDv = "";
		mesAnoCompetencia = 0;
		escalaTipo = "";
		quantidadeLocomocao = "";
		valorLocomocao = 0;
		quantidade = 0;
		infoConta = "";
		processoNumero = "";
		idMandJud = "";
		assistencia = "";
		areaProcesso = "";
		mandTipo = "";
		statusPagamento = "";
		statusMandJud = "";
		dataInicial = "";
		dataFinal = "";
		dataDist = "";
		dataRetorno = "";
		nomeServentia = "";
		nomeCompanheiro = "";
		diferencaDias = "";
		dataLimite = "";
		dataPagamentoStatus = "";
		valor = "";
		idGuiaEmis = "";
		numeroGuiaCompleto = "";
		dataEmis = "";
		motivo = "";
		nomeAnt = "";
		nomeAtual = "";
		idEscAnt = "";
		idEscAtual = "";
		mandTipoRedist = "";

		//

		quantFaixaReceber = 0;
		valorFaixaReceber = 0;
		quantResolutivoReceber = 0;
		valorResolutivoReceber = 0;
		quantAcimaFaixaReceber = 0;
		valorAcimaFaixaReceber = 0;

		resolutivo = 0;
		dataString = "";
		horaString = "";
		idUsuServ = 0;
		
		analise = "";

		//

	}

	public String getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(String dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	public int getMesAnoCompetencia() {
		return mesAnoCompetencia;
	}

	public void setMesAnoCompetencia(int mesAnoCompetencia) {
		this.mesAnoCompetencia = mesAnoCompetencia;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
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

	public int getContaOperacao() {
		return contaOperacao;
	}

	public void setContaOperacao(int contaOperacao) {
		this.contaOperacao = contaOperacao;
	}

	public String getEscalaTipo() {
		return escalaTipo;
	}

	public void setEscalaTipo(String escalaTipo) {
		this.escalaTipo = escalaTipo;
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

	public String getNomeComarca() {
		return nomeComarca;
	}

	public void setNomeComarca(String nomeComarca) {
		this.nomeComarca = nomeComarca;
	}

	public int getSomatorioMandados() {
		return somatorioMandados;
	}

	public void setSomatorioMandados(int somatorioMandados) {
		this.somatorioMandados = somatorioMandados;
	}

	public int getMesesComMandado() {
		return mesesComMandado;
	}

	public void setMesesComMandado(int mesesComMandado) {
		this.mesesComMandado = mesesComMandado;
	}

	public int getMediaMandado() {
		return mediaMandado;
	}

	public void setMediaMandado(int mediaMandado) {
		this.mediaMandado = mediaMandado;
	}

	public String getFaixa() {
		return faixa;
	}

	public void setFaixa(String faixa) {
		this.faixa = faixa;
	}

	public double getValorReceber() {
		return valorReceber;
	}

	public void setValorReceber(double valorReceber) {
		this.valorReceber = valorReceber;
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

	public String getContaDv() {
		return contaDv;
	}

	public void setContaDv(String contaDv) {
		this.contaDv = contaDv;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getQuantidadeLocomocao() {
		return quantidadeLocomocao;
	}

	public void setQuantidadeLocomocao(String quantidadeLocomocao) {
		this.quantidadeLocomocao = quantidadeLocomocao;
	}

	public String getInfoConta() {
		return infoConta;
	}

	public void setInfoConta(String infoConta) {
		this.infoConta = infoConta;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public String getIdMandJud() {
		return idMandJud;
	}

	public void setIdMandJud(String idMandJud) {
		this.idMandJud = idMandJud;
	}

	public double getValorLocomocao() {
		return valorLocomocao;
	}

	public void setValorLocomocao(double valorLocomocao) {
		this.valorLocomocao = valorLocomocao;
	}

	public String getAssistencia() {
		return assistencia;
	}

	public void setAssistencia(String assistencia) {
		this.assistencia = assistencia;
	}

	public String getAreaProcesso() {
		return areaProcesso;
	}

	public void setAreaProcesso(String areaProcesso) {
		this.areaProcesso = areaProcesso;
	}

	public String getMandTipo() {
		return mandTipo;
	}

	public void setMandTipo(String mandTipo) {
		this.mandTipo = mandTipo;
	}

	public String getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(String statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getDataDist() {
		return dataDist;
	}

	public void setDataDist(String dataDist) {
		this.dataDist = dataDist;
	}

	public String getDataRetorno() {
		return dataRetorno;
	}

	public String getDiferencaDias() {
		return diferencaDias;
	}

	public void setDiferencaDias(String diferencaDias) {
		this.diferencaDias = diferencaDias;
	}

	public void setDataRetorno(String dataRetorno) {
		this.dataRetorno = dataRetorno;
	}

	public String getStatusMandJud() {
		return statusMandJud;
	}

	public void setStatusMandJud(String statusMandJud) {
		this.statusMandJud = statusMandJud;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public String getNomeCompanheiro() {
		return nomeCompanheiro;
	}

	public void setNomeCompanheiro(String nomeCompanheiro) {
		this.nomeCompanheiro = nomeCompanheiro;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(String dataLimite) {
		this.dataLimite = dataLimite;
	}

	public String getDataPagamentoStatus() {
		return dataPagamentoStatus;
	}

	public void setDataPagamentoStatus(String dataPagamentoStatus) {
		this.dataPagamentoStatus = dataPagamentoStatus;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getIdGuiaEmis() {
		return idGuiaEmis;
	}

	public void setIdGuiaEmis(String idGuiaEmis) {
		this.idGuiaEmis = idGuiaEmis;
	}

	public String getDataEmis() {
		return dataEmis;
	}

	public void setDataEmis(String dataEmis) {
		this.dataEmis = dataEmis;
	}

	public String getNumeroGuiaCompleto() {
		return numeroGuiaCompleto;
	}

	public void setNumeroGuiaCompleto(String numeroGuiaCompleto) {
		this.numeroGuiaCompleto = numeroGuiaCompleto;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getNomeAnt() {
		return nomeAnt;
	}

	public void setNomeAnt(String nomeAnt) {
		this.nomeAnt = nomeAnt;
	}

	public String getNomeAtual() {
		return nomeAtual;
	}

	public void setNomeAtual(String nomeAtual) {
		this.nomeAtual = nomeAtual;
	}

	public String getIdEscAnt() {
		return idEscAnt;
	}

	public void setIdEscAnt(String idEscAnt) {
		this.idEscAnt = idEscAnt;
	}

	public String getIdEscAtual() {
		return idEscAtual;
	}

	public void setIdEscAtual(String idEscAtual) {
		this.idEscAtual = idEscAtual;
	}

	public String getMandTipoRedist() {
		return mandTipoRedist;
	}

	public void setMandTipoRedist(String mandTipoRedist) {
		this.mandTipoRedist = mandTipoRedist;
	}

	public int getQuantFaixaReceber() {
		return quantFaixaReceber;
	}

	public void setQuantFaixaReceber(int quantFaixaReceber) {
		this.quantFaixaReceber = quantFaixaReceber;
	}

	public double getValorFaixaReceber() {
		return valorFaixaReceber;
	}

	public void setValorFaixaReceber(double valorFaixaReceber) {
		this.valorFaixaReceber = valorFaixaReceber;
	}

	public int getQuantResolutivoReceber() {
		return quantResolutivoReceber;
	}

	public void setQuantResolutivoReceber(int quantResolutivoReceber) {
		this.quantResolutivoReceber = quantResolutivoReceber;
	}

	public double getValorResolutivoReceber() {
		return valorResolutivoReceber;
	}

	public void setValorResolutivoReceber(double valorResolutivoReceber) {
		this.valorResolutivoReceber = valorResolutivoReceber;
	}

	public int getQuantAcimaFaixaReceber() {
		return quantAcimaFaixaReceber;
	}

	public void setQuantAcimaFaixaReceber(int quantAcimaFaixaReceber) {
		this.quantAcimaFaixaReceber = quantAcimaFaixaReceber;
	}

	public double getValorAcimaFaixaReceber() {
		return valorAcimaFaixaReceber;
	}

	public void setValorAcimaFaixaReceber(double valorAcimaFaixaReceber) {
		this.valorAcimaFaixaReceber = valorAcimaFaixaReceber;
	}

	public int getResolutivo() {
		return resolutivo;
	}

	public void setResolutivo(int resolutivo) {
		this.resolutivo = resolutivo;
	}

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public String getHoraString() {
		return horaString;
	}

	public void setHoraString(String horaString) {
		this.horaString = horaString;
	}

	public int getIdUsuServ() {
		return idUsuServ;
	}

	public void setIdUsuServ(int idUsuServ) {
		this.idUsuServ = idUsuServ;
	}

	public String getAnalise() {
		return analise;
	}

	public void setAnalise(String analise) {
		this.analise = analise;
	}
	
	

}