package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

//atributos declarados com inicial maiúscula para adaptar à interface "InterfaceJasper.java 
public class SituacaoAtualExecucaoPenalDt  extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -546780101453458701L;
	private String idProcesso;
	private String numeroProcesso;
	private String numeroProcessoCompleto;
	private String idRegime;
	private String descricaoRegime;
	private String idLocalCumprimentoPena;
	private String descricaoLocalCumprimentoPena;
	private String idModalidade;
	private String descricaoModalidade;
	private String idSituacao;
	private String descricaoSituacao;
	private String dataPrisao;
	private String dataInicioRegime;
	private String dataTerminoPena;	
	private String informacaoSentenciado;
	private String processoTipo;
	private String processoTipoCodigo;
	private String serventia;
	private String dataCalculo;
	private String idFormaCumprimento;
	private String formaCumprimento;
	private String dataAtualizacao;
	
	public SituacaoAtualExecucaoPenalDt() {
		idProcesso = "";
		numeroProcesso = "";
		numeroProcessoCompleto = "";
		dataPrisao = "";
		dataInicioRegime = "";
		dataTerminoPena = "";
		idRegime = "";
		descricaoRegime = "";
		idLocalCumprimentoPena = "";
		descricaoLocalCumprimentoPena = "";
		idSituacao = "";
		descricaoSituacao = "";
		idModalidade = "";
		descricaoModalidade = "";
		informacaoSentenciado = "";
		processoTipo = "";
		processoTipoCodigo = "";
		serventia = "";
		dataCalculo = "";
		idFormaCumprimento = "";
		formaCumprimento = "";
		dataAtualizacao = "";
	}
	
	public String getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
	
	public String getNumeroProcesso() {
		return numeroProcesso;
	}
	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getInformacaoSentenciado() {
		return informacaoSentenciado;
	}
	public void setInformacaoSentenciado(String informacaoSentenciado) {
		this.informacaoSentenciado = informacaoSentenciado;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}

	public String getIdRegime() {
		return idRegime;
	}

	public void setIdRegime(String idRegime) {
		if ((idRegime!=null) && (!idRegime.equals("null"))) this.idRegime = idRegime;
	}

	public String getDescricaoRegime() {
		return descricaoRegime;
	}

	public void setDescricaoRegime(String descricaoRegime) {
		if ((descricaoRegime!=null) && (!descricaoRegime.equals("null"))) this.descricaoRegime = descricaoRegime;
	}

	public String getIdLocalCumprimentoPena() {
		return idLocalCumprimentoPena;
	}

	public void setIdLocalCumprimentoPena(String idLocalCumprimentoPena) {
		if ((idLocalCumprimentoPena!=null) && (!idLocalCumprimentoPena.equals("null"))) this.idLocalCumprimentoPena = idLocalCumprimentoPena;
	}

	public String getDescricaoLocalCumprimentoPena() {
		return descricaoLocalCumprimentoPena;
	}

	public void setDescricaoLocalCumprimentoPena(String descricaoLocalCumprimentoPena) {
		if ((descricaoLocalCumprimentoPena!=null) && (!descricaoLocalCumprimentoPena.equals("null"))) this.descricaoLocalCumprimentoPena = descricaoLocalCumprimentoPena;
	}

	public String getIdSituacao() {
		return idSituacao;
	}

	public void setIdSituacao(String idSituacao) {
		if ((idSituacao!=null) && (!idSituacao.equals("null"))) this.idSituacao = idSituacao;
	}

	public String getDescricaoSituacao() {
		return descricaoSituacao;
	}

	public void setDescricaoSituacao(String descricaoSituacao) {
		if ((descricaoSituacao!=null) && (!descricaoSituacao.equals("null"))) this.descricaoSituacao = descricaoSituacao;
	}
	
	public String getIdModalidade() {
		return idModalidade;
	}

	public void setIdModalidade(String idModalidade) {
		if (idModalidade != null && !idModalidade.equals("null")) this.idModalidade = idModalidade;
	}

	public String getDescricaoModalidade() {
		return descricaoModalidade;
	}

	public void setDescricaoModalidade(String descricaoModalidade) {
		if (descricaoModalidade != null && !descricaoModalidade.equals("null")) this.descricaoModalidade = descricaoModalidade;
	}

	public String getDataPrisao() {
		if (dataPrisao.equals("")) dataPrisao = "-";
		return dataPrisao;
	}

	public void setDataPrisao(String dataPrisao) {
		this.dataPrisao = dataPrisao;
	}

	public String getDataInicioRegime() {
		if (dataInicioRegime.equals("")) dataInicioRegime = "-";
		return dataInicioRegime;
	}

	public void setDataInicioRegime(String dataInicioRegime) {
		this.dataInicioRegime = dataInicioRegime;
	}

	public String getDataTerminoPena() {
		if (dataTerminoPena.equals("")) dataTerminoPena = "-";
		return dataTerminoPena;
	}

	public void setDataTerminoPena(String dataTerminoPena) {
		this.dataTerminoPena = dataTerminoPena;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
	}

	public String getProcessoTipoCodigo() {
		return processoTipoCodigo;
	}

	public void setProcessoTipoCodigo(String processoTipoCodigo) {
		this.processoTipoCodigo = processoTipoCodigo;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getNumeroProcessoCompleto() {
		return numeroProcessoCompleto;
	}

	public void setNumeroProcessoCompleto(String numeroProcessoCompleto) {
		this.numeroProcessoCompleto = numeroProcessoCompleto;
	}

	public String getDataCalculo() {
		return dataCalculo;
	}

	public void setDataCalculo(String dataCalculo) {
		this.dataCalculo = dataCalculo;
	}

	public String getIdFormaCumprimento() {
		return idFormaCumprimento;
	}

	public void setIdFormaCumprimento(String idFormaCumprimento) {
		this.idFormaCumprimento = idFormaCumprimento;
	}

	public String getFormaCumprimento() {
		return formaCumprimento;
	}

	public void setFormaCumprimento(String formaCumprimento) {
		this.formaCumprimento = formaCumprimento;
	}

	public String getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(String dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	
}
