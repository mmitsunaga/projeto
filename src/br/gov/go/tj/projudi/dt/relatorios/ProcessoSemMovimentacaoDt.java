package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class ProcessoSemMovimentacaoDt extends Dados{

	private static final long serialVersionUID = -1981439337721992739L;

	public static final int CodigoPermissao = 510;
	
	public static final Integer TIPO_PROMOVENTE = 1;
	public static final Integer TIPO_PROMOVIDO = 2;

	private String idServentia;
	private String serventia;
	private String periodo;

	private String idProcesso;
	private String numeroProcesso;
	private String dataUltimaMovimentacao;
	private String complementoMovimentacao;
	private String idServentiaCargo;
	private String serventiaCargo;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public ProcessoSemMovimentacaoDt() {
		limpar();
	}

	public void limpar() {
		idServentia = "";
		idProcesso = "";
		serventia = "";
		numeroProcesso = "";
		dataUltimaMovimentacao = "";
		complementoMovimentacao = "";
		idServentiaCargo = "";
		serventiaCargo = "";
		//a variável será inicializada/limpada com 20 para que essa opção seja selecionada
		//no option da JSP de consulta
		periodo = "20";
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
	
	public String getIdServentiaCargo() {
		return idServentiaCargo;
	}

	public void setIdServentiaCargo(String idServentiaCargo) {
		if (idServentiaCargo != null && !idServentiaCargo.equalsIgnoreCase("null"))
		this.idServentiaCargo = idServentiaCargo;
	}

	public String getServentiaCargo() {
		return serventiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		if (serventiaCargo != null && !serventiaCargo.equalsIgnoreCase("null"))
		this.serventiaCargo = serventiaCargo;
	}
}