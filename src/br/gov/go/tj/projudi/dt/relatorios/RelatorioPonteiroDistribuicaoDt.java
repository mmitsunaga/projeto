package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioPonteiroDistribuicaoDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissaoPonteiroDistribuicao = 731;

	// variáveis da tela

	private String idAreaDistribuicao;
	private String areaDistribuicao;
	private String idServentia;
	private String serventia;
	private String dataVerificacao;
	private String tipoRelatorio;
	private String tipoArquivo;

	//

	private String idUsuario;
	private String usuario;
	private String dataInicial;
	private String dataFinal;

	// variáveis dos relatórios
	private String dataRecebimento;
	private String numeroProcesso;
	private String digitoVerificador;
	private String idTipoProcesso;
	private String tipoProcesso;
	private String nomeResponsavel;
	private long quantidade;
	private String servCargoResponsavel;
	private String nomeServentia;
	private String distribuicaoTipo;
	
	//

	private String nomeClasse;
	private String nomeUsuario;
	private int distribuicao;
	private int redistribuicao;
	private int ganhoResponsabilidade;
	private int perdaResponsabilidade;
	private int ganhoCompensacao;
	private int perdaCompensacao;
	private int ganhoCorrecao;
	private int perdaCorrecao;
	private int compensacao;
	private int correcao;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */

	public RelatorioPonteiroDistribuicaoDt() {
		limpar();
	}

	public void limpar() {

		idAreaDistribuicao = "";
		areaDistribuicao = "";
		idServentia = "";
		serventia = "";
		dataVerificacao = "";
		tipoRelatorio = "";
		tipoArquivo = "";

		numeroProcesso = "";
		digitoVerificador = "";
		idTipoProcesso = "";
		tipoProcesso = "";
		nomeServentia = "";
		nomeResponsavel = "";
		servCargoResponsavel = "";
		distribuicaoTipo = "";

		dataInicial = "";
		dataFinal = "";

		dataInicial = "";
		dataFinal = "";
		idUsuario = "";
		usuario = "";
		nomeUsuario = "";

	}

	/**
	 * Método que limpa os campos de consulta da tela, mas não limpa o Tipo de
	 * Relatório que está sendo gerado.
	 */
	public void limparCamposConsulta() {

		idAreaDistribuicao = "";
		areaDistribuicao = "";
		idServentia = "";
		serventia = "";
		dataVerificacao = "";
		tipoRelatorio = "";
		tipoArquivo = "";

		numeroProcesso = "";
		digitoVerificador = "";
		idTipoProcesso = "";
		tipoProcesso = "";
		nomeServentia = "";
		nomeResponsavel = "";
		servCargoResponsavel = "";
		distribuicaoTipo = "";

		dataInicial = "";
		dataFinal = "";
		idUsuario = "";
		usuario = "";
		nomeUsuario = "";
	}

	public String getIdAreaDistribuicao() {
		return idAreaDistribuicao;
	}

	public void setIdAreaDistribuicao(String idAreaDistribuicao) {
		this.idAreaDistribuicao = idAreaDistribuicao;
	}

	public String getAreaDistribuicao() {
		return areaDistribuicao;
	}

	public void setAreaDistribuicao(String areaDistribuicao) {
		this.areaDistribuicao = areaDistribuicao;
	}

	public String getIdTipoProcesso() {
		return idTipoProcesso;
	}

	public void setIdTipoProcesso(String idTipoProcesso) {
		this.idTipoProcesso = idTipoProcesso;
	}

	public String getDataVerificacao() {
		return dataVerificacao;
	}

	public void setDataVerificacao(String dataVerificacao) {
		this.dataVerificacao = dataVerificacao;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}

	@Override
	public String getId() {

		return null;
	}

	@Override
	public void setId(String id) {

	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}

	public String getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getDigitoVerificador() {
		return digitoVerificador;
	}

	public void setDigitoVerificador(String digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getServCargoResponsavel() {
		return servCargoResponsavel;
	}

	public void setServCargoResponsavel(String servCargoResponsavel) {
		this.servCargoResponsavel = servCargoResponsavel;
	}

	public String getDistribuicaoTipo() {
		return distribuicaoTipo;
	}

	public void setDistribuicaoTipo(String distribuicaoTipo) {
		this.distribuicaoTipo = distribuicaoTipo;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public int getDistribuicao() {
		return distribuicao;
	}

	public void setDistribuicao(int distribuicao) {
		this.distribuicao = distribuicao;
	}

	public int getRedistribuicao() {
		return redistribuicao;
	}

	public void setRedistribuicao(int redistribuicao) {
		this.redistribuicao = redistribuicao;
	}

	public int getGanhoResponsabilidade() {
		return ganhoResponsabilidade;
	}

	public void setGanhoResponsabilidade(int ganhoResponsabilidade) {
		this.ganhoResponsabilidade = ganhoResponsabilidade;
	}

	public int getPerdaResponsabilidade() {
		return perdaResponsabilidade;
	}

	public void setPerdaResponsabilidade(int perdaResponsabilidade) {
		this.perdaResponsabilidade = perdaResponsabilidade;
	}

	public int getGanhoCompensacao() {
		return ganhoCompensacao;
	}

	public void setGanhoCompensacao(int ganhoCompensacao) {
		this.ganhoCompensacao = ganhoCompensacao;
	}

	public int getPerdaCompensacao() {
		return perdaCompensacao;
	}

	public void setPerdaCompensacao(int perdaCompensacao) {
		this.perdaCompensacao = perdaCompensacao;
	}

	public int getGanhoCorrecao() {
		return ganhoCorrecao;
	}

	public void setGanhoCorrecao(int ganhoCorrecao) {
		this.ganhoCorrecao = ganhoCorrecao;
	}

	public int getPerdaCorrecao() {
		return perdaCorrecao;
	}

	public void setPerdaCorrecao(int perdaCorrecao) {
		this.perdaCorrecao = perdaCorrecao;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public int getCompensacao() {
		return compensacao;
	}

	public void setCompensacao(int compensacao) {
		this.compensacao = compensacao;
	}

	public int getCorrecao() {
		return correcao;
	}

	public void setCorrecao(int correcao) {
		this.correcao = correcao;
	}

}