package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class ProcessoTempoVidaTipoDt extends Dados{

	private static final long serialVersionUID = -1981439337721992739L;

	public static final int CodigoPermissao = 902;
	
	private String idServentia;
	private String serventia;
	private String idProcessoTipo;
	private String processoTipo;
	private String periodo;

	private String idProcesso;
	private String numeroProcesso;
	private String dataUltimaMovimentacao;
	private String complementoMovimentacao;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public ProcessoTempoVidaTipoDt() {
		limpar();
	}

	public void limpar() {
		idServentia = "";
		idProcesso = "";
		idProcessoTipo = "";
		processoTipo = "";
		serventia = "";
		numeroProcesso = "";
		dataUltimaMovimentacao = "";
		complementoMovimentacao = "";
		//a variável será inicializada/limpada com 100 para que essa opção seja selecionada
		//no option da JSP de consulta
		periodo = "100";
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getDataUltimaMovimentacao() {
		return dataUltimaMovimentacao;
	}

	public void setDataUltimaMovimentacao(String dataUltimaMovimentacao) {
		this.dataUltimaMovimentacao = dataUltimaMovimentacao;
	}

	public String getComplementoMovimentacao() {
		return complementoMovimentacao;
	}

	public void setComplementoMovimentacao(String complementoMovimentacao) {
		this.complementoMovimentacao = complementoMovimentacao;
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
		
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getIdProcessoTipo() {
		return idProcessoTipo;
	}

	public void setIdProcessoTipo(String idProcessoTipo) {
		this.idProcessoTipo = idProcessoTipo;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
	}
	
}