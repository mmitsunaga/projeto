package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioConclusoesPrimeiroGrauDt extends Dados {


	private static final long serialVersionUID = -6157632986924379786L;

	public static final int CodigoPermissaoConclusoesPrimeiroGrau = 671;

	// variáveis das telas
	private String mesInicial;
	private String anoInicial;
	private String mesFinal;
	private String anoFinal;
	private String idServentia;
	private String serventia;
	private String tipoRelatorio;

	// variáveis dos relatórios
	private String cargoTipo;
	private String nomeResponsavel;
	private String pendenciaTipo;
	private long quantidade;
	private String usuarioRelatorio;
	
	public static final int PENDENTES = 1;
	public static final int RECEBIDAS = 2;
	public static final int REALIZADAS = 3;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioConclusoesPrimeiroGrauDt() {
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

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

}