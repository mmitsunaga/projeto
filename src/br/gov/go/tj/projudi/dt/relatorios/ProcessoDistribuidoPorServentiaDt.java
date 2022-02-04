package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

public class ProcessoDistribuidoPorServentiaDt extends Dados {

    private static final long serialVersionUID = -211044154535415616L;
	public static final int CodigoPermissaoDistribuicao = 732;
	//
	// variáveis da tela
    //
	private String idAreaDistribuicao;
	private String areaDistribuicao;
	private String idServentia;
	private String serventia;
	private String idUsuario;
	private String usuario;
	private String dataInicial;
	private String dataFinal;
	private String tipoRelatorio;
	private String tipoArquivo;
    //
	// variáveis dos relatórios
    //
	private String nomeAreaDistribuicao;
	private String nomeServentia;
	private String nomeUsuario;
	private String distribuicaoTipo;
	private String numeroProcesso;
	private String dataRecebimento;
	private String nomeClasse; 
	private String nomeResponsavel;
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
	private String nomeAssunto;

	public ProcessoDistribuidoPorServentiaDt() {
		limpar();
	}

	public void limpar() {

		idAreaDistribuicao = "";
		areaDistribuicao = "";
		idServentia = "";
		serventia = "";
		idUsuario = "";
		usuario = "";
		dataInicial = "";
		dataFinal = "";
		tipoRelatorio = "";
		tipoArquivo = "";
	    
		nomeAreaDistribuicao = "";
		nomeServentia = "";
		nomeUsuario = "";
		distribuicaoTipo = "";
		numeroProcesso = "";
		dataRecebimento = "";
		nomeClasse = ""; 
		nomeResponsavel = "";
		distribuicao = 0;
		redistribuicao = 0;
		ganhoResponsabilidade = 0;
		perdaResponsabilidade = 0;
		ganhoCompensacao = 0;
		perdaCompensacao = 0;
		ganhoCorrecao = 0;
		perdaCorrecao = 0;
		compensacao = 0;
		correcao = 0;
		nomeAssunto = "";

	} 
	public void limparCamposConsulta() {		
		idAreaDistribuicao = "";
		areaDistribuicao = "";		
		idServentia = "";
		serventia = "";
		idUsuario = "";
		usuario = "";
		dataInicial = "";
		dataFinal = "";	 
		tipoRelatorio = "";
		tipoArquivo = "";		
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

	public String getNomeAreaDistribuicao() {
		return nomeAreaDistribuicao;
	}

	public void setNomeAreaDistribuicao(String nomeAreaDistribuicao) {
		this.nomeAreaDistribuicao = nomeAreaDistribuicao;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getDistribuicaoTipo() {
		return distribuicaoTipo;
	}

	public void setDistribuicaoTipo(String distribuicaoTipo) {
		this.distribuicaoTipo = distribuicaoTipo;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
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

	public String getNomeAssunto() {
		return nomeAssunto;
	}

	public void setNomeAssunto(String nomeAssunto) {
		this.nomeAssunto = nomeAssunto;
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

	
}