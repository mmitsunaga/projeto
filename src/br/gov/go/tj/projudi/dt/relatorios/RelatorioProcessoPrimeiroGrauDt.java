package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioProcessoPrimeiroGrauDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissaoProcessoPrimeiroGrauJuizes = 650;

	// variáveis das telas
	private String anoConsulta;
	private String idAreaDistribuicao;
	private String idServentiaConsulta;
	private String serventiaConsulta;

	// variáveis dos relatórios
	private int ano;
	private String serventia;
	private String areaDistribuicao;
	private String processoTipo;
	private String nomeResponsavel;
	private long quantidade;
	private String usuarioRelatorio;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioProcessoPrimeiroGrauDt() {
		limpar();
	}

	public void limpar() {
		anoConsulta = "";
		idAreaDistribuicao = "";
		idServentiaConsulta = "";
		serventiaConsulta = "";
		serventia = "";
		areaDistribuicao = "";
		processoTipo = "";
		usuarioRelatorio = "";
		nomeResponsavel = "";
	}

	/**
	 * Método que limpa os campos de consulta da tela.
	 */
	public void limparCamposConsulta() {
		anoConsulta = "";
		idAreaDistribuicao = "";
		idServentiaConsulta = "";
		serventiaConsulta = "";
		serventia = "";
		areaDistribuicao = "";
		processoTipo = "";
		usuarioRelatorio = "";
		nomeResponsavel = "";
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getAnoConsulta() {
		return anoConsulta;
	}

	public void setAnoConsulta(String anoConsulta) {
		this.anoConsulta = anoConsulta;
	}

	public String getIdAreaDistribuicao() {
		return idAreaDistribuicao;
	}

	public void setIdAreaDistribuicao(String idAreaDistribuicao) {
		this.idAreaDistribuicao = idAreaDistribuicao;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getIdServentiaConsulta() {
		return idServentiaConsulta;
	}

	public void setIdServentiaConsulta(String idServentiaConsulta) {
		this.idServentiaConsulta = idServentiaConsulta;
	}

	public String getServentiaConsulta() {
		return serventiaConsulta;
	}

	public void setServentiaConsulta(String serventiaConsulta) {
		this.serventiaConsulta = serventiaConsulta;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getAreaDistribuicao() {
		return areaDistribuicao;
	}

	public void setAreaDistribuicao(String areaDistribuicao) {
		this.areaDistribuicao = areaDistribuicao;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
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

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

}