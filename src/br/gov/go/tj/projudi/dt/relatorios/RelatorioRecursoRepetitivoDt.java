package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

public class RelatorioRecursoRepetitivoDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissao = 814;

	// vari�veis das telas
	private String dataInicial;
	private String dataFinal;
	private String idTema;
	private String tema;
	private String tipoRelatorio;
	private String opcaoRelatorio;

	// vari�veis dos relat�rios
	private String usuarioRelatorio;
	private String recurso;
	private String questaoDireito;
	private String temaCodigo;
	private String numeroProcesso;
	private String tipoRecurso;
	private String dataSobrestado;
	private long quantidade;
	private String nomeServentia;
	private String statusProcesso;
	private String tipoMovimento;
	private String tipoMovimentoCodigo;
	/**
	 * Construtor. Inicializa todas as vari�veis
	 */
	public RelatorioRecursoRepetitivoDt() {
		limpar();
	}

	public void limpar() {
		dataInicial = "";
		dataFinal = "";
		idTema = "";
		tema = "";
		tipoRelatorio = "1";
		opcaoRelatorio = "1";
		nomeServentia = "";
		tipoMovimento = "";
		tipoMovimentoCodigo = "";

	}

	/**
	 * M�todo que limpa os campos de consulta da tela, mas n�o limpa o Tipo de
	 * Relat�rio que est� sendo gerado.
	 */
	public void limparCamposConsulta() {
		dataInicial = "";
		dataFinal = "";
		idTema = "";
		tema = "";
		tipoRelatorio = "1";
		opcaoRelatorio = "1";
		nomeServentia = "";
		tipoMovimento = "";

	}	

	public String getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public String getOpcaoRelatorio() {
		return opcaoRelatorio;
	}

	public void setOpcaoRelatorio(String opcaoRelatorio) {
		this.opcaoRelatorio = opcaoRelatorio;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public String getStatusProcesso() {
		return statusProcesso;
	}

	public void setStatusProcesso(String statusProcesso) {
		this.statusProcesso = statusProcesso;
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

	public String getIdTema() {
		return idTema;
	}

	public void setIdTema(String idTema) {
		this.idTema = idTema;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String getUsuarioRelatorio() {
		return usuarioRelatorio;
	}

	public void setUsuarioRelatorio(String usuarioRelatorio) {
		this.usuarioRelatorio = usuarioRelatorio;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getQuestaoDireito() {
		return questaoDireito;
	}

	public void setQuestaoDireito(String questaoDireito) {
		this.questaoDireito = questaoDireito;
	}

	public String getTemaCodigo() {
		return temaCodigo;
	}

	public void setTemaCodigo(String temaCodigo) {
		this.temaCodigo = temaCodigo;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	public String getDataSobrestado() {
		return dataSobrestado;
	}

	public void setDataSobrestado(String dataSobrestado) {
		this.dataSobrestado = dataSobrestado;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}
	
	

	public String getTipoMovimentoCodigo() {
		return tipoMovimentoCodigo;
	}

	public void setTipoMovimentoCodigo(String tipoMovimentoCodigo) {
		this.tipoMovimentoCodigo = tipoMovimentoCodigo;
	}

	@Override
	public void setId(String id) {

	}

	@Override
	public String getId() {

		return null;
	}

}