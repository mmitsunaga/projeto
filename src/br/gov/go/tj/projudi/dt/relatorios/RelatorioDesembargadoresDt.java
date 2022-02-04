package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioDesembargadoresDt extends Dados {

	private static final long serialVersionUID = -3836214908231641926L;

	public static final int CodigoPermissaoDesembargadores = 698;

	// variáveis das telas
	private String mesInicial;
	private String anoInicial;
	private String mesFinal;
	private String anoFinal;
	private String idServentia;
	private String serventia;
	private String tipoRelatorio;
	private String itemEstatistica;
	private String idItemEstatistica;	

	// variáveis dos relatórios
	private String cargoTipo;
	private String nomeResponsavel;
	private String pendenciaTipo;
	private String usuarioRelatorio;
	private String processoNumero;
	private String dataFim;

	private long quantidade;
	
	public static final int PENDENTES = 1;
	public static final int RECEBIDAS = 2;
	public static final int REALIZADAS = 3;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioDesembargadoresDt() {
		limpar();
	}

	public void limpar() {
		mesInicial = "";
		anoInicial = "";
		mesFinal = "";
		anoFinal = "";
		idServentia = "";
		serventia = "";
		tipoRelatorio = "";
		cargoTipo = "";
		nomeResponsavel = "";
		pendenciaTipo = "";
		quantidade = 0;
		usuarioRelatorio = "";
		processoNumero = "";
		dataFim = "";
		itemEstatistica = "";
		idItemEstatistica = "";
	}

	/**
	 * Método que limpa os campos de consulta da tela.
	 */
	public void limparCamposConsulta() {
		mesInicial = "";
		anoInicial = "";
		mesFinal = "";
		anoFinal = "";
		idServentia = "";
		serventia = "";
		tipoRelatorio = "";
		cargoTipo = "";
		nomeResponsavel = "";
		pendenciaTipo = "";
		quantidade = 0;
		usuarioRelatorio = "";
		processoNumero = "";
		dataFim = "";
		itemEstatistica = "";
		idItemEstatistica = "";
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}

	public String getUsuarioRelatorio() {
		return usuarioRelatorio;
	}

	public void setUsuarioRelatorio(String usuarioRelatorio) {
		this.usuarioRelatorio = usuarioRelatorio;
	}

	public String getMesInicial() {
		return mesInicial;
	}

	public void setMesInicial(String mesInicial) {
		this.mesInicial = mesInicial;
	}

	public String getAnoInicial() {
		return anoInicial;
	}

	public void setAnoInicial(String anoInicial) {
		this.anoInicial = anoInicial;
	}

	public String getMesFinal() {
		return mesFinal;
	}

	public void setMesFinal(String mesFinal) {
		this.mesFinal = mesFinal;
	}

	public String getAnoFinal() {
		return anoFinal;
	}

	public void setAnoFinal(String anoFinal) {
		this.anoFinal = anoFinal;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public String getCargoTipo() {
		return cargoTipo;
	}

	public void setCargoTipo(String cargoTipo) {
		this.cargoTipo = cargoTipo;
	}

	public String getPendenciaTipo() {
		return pendenciaTipo;
	}

	public void setPendenciaTipo(String pendenciaTipo) {
		this.pendenciaTipo = pendenciaTipo;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}
	
	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public String getItemEstatistica() {
		return itemEstatistica;
	}

	public void setItemEstatistica(String itemEstatistica) {
		this.itemEstatistica = itemEstatistica;
	}

	public String getIdItemEstatistica() {
		return idItemEstatistica;
	}

	public void setIdItemEstatistica(String idItemEstatistica) {
		this.idItemEstatistica = idItemEstatistica;
	}

}