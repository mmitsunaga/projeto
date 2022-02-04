package br.gov.go.tj.projudi.dt;

import java.util.List;



public class ProcessoFisicoDt extends Dados {

	/**
	 * 
	 */
	
	public static final String ACESSO = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON0021T?numrproc=<ProcessoNumero>oab=<OAB>";	
	public static final String DADOS_GERAIS = "http://sv-natweb-p00.tjgo.gov/cgi-bin/tjg-guia/forpspgi/CON1002T?numeroproc=<ProcessoNumero>";
	public static final String INTERLOCUTORIAS = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1202T?nproc=<ProcessoNumero>&ultimo=0";
	public static final String MANDADOS = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1302T?processo=<ProcessoNumero>&ultimo=0";
	public static final String HISTORICOS = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1402T?proc=<ProcessoNumero>";
	public static final String SENTENCAS = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1502T?np=<ProcessoNumero>&ultimo=0";
	public static final String INTIMACOES ="http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1602T?pro=<ProcessoNumero>";
	public static final String LIGACOES = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1702T?processo=<ProcessoNumero>";
	public static final String REDISTRIBUICOES = "http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-capital/FORPSPGI/CON1802T?proc=<ProcessoNumero>";
	
	private static final long serialVersionUID = 3282909464896353589L;
	private String numeroProcesso;
	private String infracao;
	private String lei;
	private String DataProtocolo;
	private String natureza;
	private String numeroAutuacao;
	private String dataAutuacao;
	private String tipoDistribuicao;
	private String dataDistribuicao;
	private String procPrincipal;
	private String promovente;
	private String promovido;
	private String fase;
	private String comarca;
	private String escrivania;
	private String escrivaniaCodigo;
	private String juiz;
	private String dataAudiencia;
	private String dataSentenca;
	private String dataTransitoJulgado;
	private String promotor;
	private String descricaoFase;
	private String localizacao;
	private String mensagem;
	private List<InterlocutoriaDt> lisInterlocutorias;
	private List<MandadoDt> lisMandados;
	private List<HistoricoDt> lisHistoricos;
	private List<IntimacaoDt> lisIntimacoes;
	private List<RedistribuicaoFisicoDt> lisRedistribuicoes;
	private List<SentencaDt> lisSentencas;
	private LigacaoDt Ligacoes;
	

	public boolean temInterlocutoria(){
		if (lisInterlocutorias!=null && lisInterlocutorias.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean temMandado(){
		if (lisMandados!=null && lisMandados.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean temHistorico(){
		if (lisHistoricos!=null && lisHistoricos.size()>0){
			return true;
		}
		return false;
	}

	public boolean temIntimacao(){
		if (lisIntimacoes!=null && lisIntimacoes.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean temRedistribuicao(){
		if (lisRedistribuicoes!=null && lisRedistribuicoes.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean temSentenca(){
		if (lisSentencas!=null && lisSentencas.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean temLigacoes(){
		if (Ligacoes!=null ){
			return true;
		}
		return false;
	}
	
	public ProcessoFisicoDt() {
		mensagem = "";
		numeroProcesso = "";
		infracao = "";
		lei = "";
		DataProtocolo = "";
		natureza = "";
		numeroAutuacao = "";
		dataAutuacao = "";
		tipoDistribuicao = "";
		dataDistribuicao = "";
		procPrincipal = "";
		promovente = "";
		promovido = "";
		fase = "";
		comarca = "";
		escrivania = "";
		escrivaniaCodigo = "";
		juiz = "";
		dataAudiencia = "";
		dataSentenca = "";
		dataTransitoJulgado = "";
		promotor = "";
		descricaoFase = "";
		localizacao = "";
	}
	
	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public LigacaoDt getLigacoes() {
		return Ligacoes;
	}

	public void setLigacoes(LigacaoDt ligacao) {
		this.Ligacoes = ligacao;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public String getInfracao() {
		return infracao;
	}

	public void setInfracao(String infracao) {
		this.infracao = infracao;
	}

	public String getLei() {
		return lei;
	}

	public void setLei(String lei) {
		this.lei = lei;
	}

	public String getDataProtocolo() {
		return DataProtocolo;
	}

	public void setDataProtocolo(String dataProtocolo) {
		DataProtocolo = dataProtocolo;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public String getNumeroAutuacao() {
		return numeroAutuacao;
	}

	public void setNumeroAutuacao(String numeroAutuacao) {
		this.numeroAutuacao = numeroAutuacao;
	}

	public String getDataAutuacao() {
		return dataAutuacao;
	}

	public void setDataAutuacao(String dataAutuacao) {
		this.dataAutuacao = dataAutuacao;
	}

	public String getTipoDistribuicao() {
		return tipoDistribuicao;
	}

	public void setTipoDistribuicao(String tipoDistribuicao) {
		this.tipoDistribuicao = tipoDistribuicao;
	}

	public String getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setDataDistribuicao(String dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	public String getProcPrincipal() {
		return procPrincipal;
	}

	public void setProcPrincipal(String procPrincipal) {
		this.procPrincipal = procPrincipal;
	}

	public String getPromovente() {
		return promovente;
	}

	public void setPromovente(String promovente) {
		this.promovente = promovente;
	}

	public String getPromovido() {
		return promovido;
	}

	public void setPromovido(String promovido) {
		this.promovido = promovido;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getEscrivania() {
		return escrivania;
	}

	public void setEscrivania(String escrivania) {
		this.escrivania = escrivania;
	}

	public String getJuiz() {
		return juiz;
	}

	public void setJuiz(String juiz) {
		this.juiz = juiz;
	}

	public String getDataAudiencia() {
		return dataAudiencia;
	}

	public void setDataAudiencia(String dataAudiencia) {
		this.dataAudiencia = dataAudiencia;
	}

	public String getDataSentenca() {
		return dataSentenca;
	}

	public void setDataSentenca(String dataSentenca) {
		this.dataSentenca = dataSentenca;
	}

	public String getDataTransitoJulgado() {
		return dataTransitoJulgado;
	}

	public void setDataTransitoJulgado(String dataTransitoJulgado) {
		this.dataTransitoJulgado = dataTransitoJulgado;
	}

	public String getPromotor() {
		return promotor;
	}

	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}


	public String getDescricaoFase() {
		return descricaoFase;
	}

	public void setDescricaoFase(String descricaoFase) {
		this.descricaoFase = descricaoFase;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	public void setInterlocutorias(List<InterlocutoriaDt> interlocutoriaList) {
		this.lisInterlocutorias = interlocutoriaList;
	}
	public List<InterlocutoriaDt> getInterlocutorias() {
		return this.lisInterlocutorias;
	}

	public void setMandadoList(List<MandadoDt> mandadoList) {
		this.lisMandados = mandadoList;
	}
	
	public List<MandadoDt> getMandados() {
		return this.lisMandados;
	}

	public void setHistoricoList(List<HistoricoDt> historicoList) {
		this.lisHistoricos = historicoList;
	}
	
	public List<HistoricoDt> getHistoricos() {
		return lisHistoricos;
	}

	public void setIntimacaoList(List<IntimacaoDt> intimacaoList) {
		this.lisIntimacoes = intimacaoList;
	}

	public void setRedistribuicaoList(
			List<RedistribuicaoFisicoDt> redistribuicaoList) {
		this.lisRedistribuicoes = redistribuicaoList;
	}
	
	@Override
	public void setId(String id) {
		
		
	}

	@Override
	public String getId() {
		
		return null;
	}
	public List<RedistribuicaoFisicoDt> getRedistribuicoes() {
		return lisRedistribuicoes;
	}

	public void setRedistribuicoes(List<RedistribuicaoFisicoDt> redistribuicoes) {
		this.lisRedistribuicoes = redistribuicoes;
	}

	public List<SentencaDt> getSentencas() {
		return lisSentencas;
	}

	public void setSentencas(List<SentencaDt> sentencas) {
		this.lisSentencas = sentencas;
	}

	public List<IntimacaoDt> getIntimacoes() {
		return lisIntimacoes;
	}

	public void setMandados(List<MandadoDt> mandados) {
		this.lisMandados = mandados;
	}
	
	public String getEscrivaniaCodigo() {
		return escrivaniaCodigo;
	}

	public void setEscrivaniaCodigo(String escrivaniaCodigo) {
		this.escrivaniaCodigo = escrivaniaCodigo;
	}
	
	public String getPropriedades(){
		return "[numeroProcesso:" + getNumeroProcesso() + ";Promovente:" + getPromovente() + ";Promovido:" + getPromovido() + ";Escrivania:" + getEscrivania() + ";EscrivaniaCodigo:" + getEscrivaniaCodigo() + "]";
	}		
}
