package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class ProcessoAudienciaDt extends Dados {

	private static final long serialVersionUID = -1981439337721992739L;

	public static final int CodigoPermissao = 606;

	private String tipoAudiencia;
	private String periodo;
	private String idServentia;
	private String serventia;
	private String aPartirDa;
	private String tipoAudienciaAnterior;

	private String idProcesso;
	private String area;
	private String numeroProcesso;
	private String dataRecebimento;
	private String dataAgendada;
	private String quantidadeDiasParalisados;
	private String idServentiaCargo;
	private String serventiaCargo;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public ProcessoAudienciaDt() {
		limpar();
	}

	public void limpar() {
		idServentia = "";
		aPartirDa = "";
		tipoAudienciaAnterior = "";
		idProcesso = "";
		serventia = "";
		numeroProcesso = "";
		dataRecebimento = "";
		area = "";
		dataAgendada = "";
		tipoAudiencia = "";
		periodo = "";
		quantidadeDiasParalisados = "";
		idServentiaCargo = "";
		serventiaCargo = "";
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null && !serventia.equalsIgnoreCase("null"))
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
		if (numeroProcesso != null && !numeroProcesso.equalsIgnoreCase("null"))
			this.numeroProcesso = numeroProcesso;
	}

	public String getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		if (dataRecebimento != null && !dataRecebimento.equalsIgnoreCase("null"))
			this.dataRecebimento = dataRecebimento;
	}

	public String getTipoAudiencia() {
		return tipoAudiencia;
	}

	public void setTipoAudiencia(String tipoAudiencia) {
		if (tipoAudiencia != null && !tipoAudiencia.equalsIgnoreCase("null"))
			this.tipoAudiencia = tipoAudiencia;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		if (area != null && !area.equalsIgnoreCase("null"))
			this.area = area;
	}

	public String getDataAgendada() {
		return dataAgendada;
	}

	public void setDataAgendada(String dataAgendada) {
		if (dataAgendada != null && !dataAgendada.equalsIgnoreCase("null"))
			this.dataAgendada = dataAgendada;
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		if (idProcesso != null && !idProcesso.equalsIgnoreCase("null"))
			this.idProcesso = idProcesso;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		if (periodo != null && !periodo.equalsIgnoreCase("null"))
			this.periodo = periodo;
	}

	public String getQuantidadeDiasParalisados() {
		return quantidadeDiasParalisados;
	}

	public void setQuantidadeDiasParalisados(String quantidadeDiasParalisados) {
		if (quantidadeDiasParalisados != null && !quantidadeDiasParalisados.equalsIgnoreCase("null"))
			this.quantidadeDiasParalisados = quantidadeDiasParalisados;
	}

	public String getAPartirDa() {
		return aPartirDa;
	}

	public void setAPartirDa(String aPartirDa) {
		if (aPartirDa != null && !aPartirDa.equalsIgnoreCase("null"))
			this.aPartirDa = aPartirDa;
	}

	public String getTipoAudienciaAnterior() {
		return tipoAudienciaAnterior;
	}

	public void setTipoAudienciaAnterior(String tipoAudienciaAnterior) {
		if (tipoAudienciaAnterior != null && !tipoAudienciaAnterior.equalsIgnoreCase("null"))
			this.tipoAudienciaAnterior = tipoAudienciaAnterior;
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

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
	}
	
}