package br.gov.go.tj.projudi.dt;

public class FinanceiroConsultarGuiasDt extends Dados {

	private static final long serialVersionUID = 1202485753129258143L;

	public static final int CodigoPermissao = 107;
	
	private String id;
	private String numeroGuiaCompleto;
	private String numeroProcesso;
	private String Id_GuiaTipo;
	private String Id_GuiaStatus;
	private String dataInicioEmissao;
	private String dataFimEmissao;
	private String dataInicioRecebimento;
	private String dataFimRecebimento;
	private String dataInicioCancelamento;
	private String dataFimCancelamento;
	private String dataInicioCertidao;
	private String dataFimCertidao;
	private String ordenacao = "1";
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroGuiaCompleto() {
		return numeroGuiaCompleto;
	}

	public void setNumeroGuiaCompleto(String numeroGuiaCompleto) {
		this.numeroGuiaCompleto = numeroGuiaCompleto;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getId_GuiaTipo() {
		return Id_GuiaTipo;
	}

	public void setId_GuiaTipo(String id_GuiaTipo) {
		Id_GuiaTipo = id_GuiaTipo;
	}

	public String getId_GuiaStatus() {
		return Id_GuiaStatus;
	}

	public void setId_GuiaStatus(String id_GuiaStatus) {
		Id_GuiaStatus = id_GuiaStatus;
	}

	public String getDataInicioEmissao() {
		return dataInicioEmissao;
	}

	public void setDataInicioEmissao(String dataInicioEmissao) {
		this.dataInicioEmissao = dataInicioEmissao;
	}

	public String getDataFimEmissao() {
		return dataFimEmissao;
	}

	public void setDataFimEmissao(String dataFimEmissao) {
		this.dataFimEmissao = dataFimEmissao;
	}

	public String getDataInicioRecebimento() {
		return dataInicioRecebimento;
	}

	public void setDataInicioRecebimento(String dataInicioRecebimento) {
		this.dataInicioRecebimento = dataInicioRecebimento;
	}

	public String getDataFimRecebimento() {
		return dataFimRecebimento;
	}

	public void setDataFimRecebimento(String dataFimRecebimento) {
		this.dataFimRecebimento = dataFimRecebimento;
	}

	public String getDataInicioCancelamento() {
		return dataInicioCancelamento;
	}

	public void setDataInicioCancelamento(String dataInicioCancelamento) {
		this.dataInicioCancelamento = dataInicioCancelamento;
	}

	public String getDataFimCancelamento() {
		return dataFimCancelamento;
	}

	public void setDataFimCancelamento(String dataFimCancelamento) {
		this.dataFimCancelamento = dataFimCancelamento;
	}

	public String getDataInicioCertidao() {
		return dataInicioCertidao;
	}

	public void setDataInicioCertidao(String dataInicioCertidao) {
		this.dataInicioCertidao = dataInicioCertidao;
	}

	public String getDataFimCertidao() {
		return dataFimCertidao;
	}

	public void setDataFimCertidao(String dataFimCertidao) {
		this.dataFimCertidao = dataFimCertidao;
	}

	public String getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}
	
}
