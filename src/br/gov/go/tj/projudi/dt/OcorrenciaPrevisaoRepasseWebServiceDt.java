package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     OcorrenciaPrevisaoRepasseWebServiceDt.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2015 
 */
public class OcorrenciaPrevisaoRepasseWebServiceDt implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5048997891038052469L;
	
	private String numeroProcesso;	
	private boolean movimentoEhPagamento;
	private String valorcustas;
	private TJDataHora dataArrecadacao;
	private String numeroGuia;
	private String numeroBancoPagamento;
	private String numeroAgenciaPagamento;
		
	public OcorrenciaPrevisaoRepasseWebServiceDt(String numeroProcesso, String tipomovimento, String valorcustas, String dataarrecadacao, String numeroGuia, String numeroBancoPagamento, String numeroAgenciaPagamento) throws Exception{
		this.numeroProcesso = numeroProcesso;
		this.movimentoEhPagamento = (tipomovimento == null || tipomovimento.trim().equalsIgnoreCase("1"));
		this.valorcustas = valorcustas;
		this.dataArrecadacao = new TJDataHora();
		this.dataArrecadacao.setDataaaaa_MM_ddHHmmss(dataarrecadacao);
		this.numeroGuia = numeroGuia;
		this.numeroBancoPagamento = numeroBancoPagamento;
		this.numeroAgenciaPagamento = numeroAgenciaPagamento;
	}
	
	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public boolean isMovimentoEhPagamento() {
		return movimentoEhPagamento;
	}
	
	public String getTextoMovimento() {
		if (this.isMovimentoEhPagamento()) return "PAGAMENTO";
		return "ESTORNO";
	}

	public String getValorcustas() {
		return valorcustas;
	}

	public void setValorcustas(String valorcustas) {
		this.valorcustas = valorcustas;
	}

	public void setMovimentoEhPagamento(boolean movimentoEhPagamento) {
		this.movimentoEhPagamento = movimentoEhPagamento;
	}

	public TJDataHora getDataArrecadacao() {
		return dataArrecadacao;
	}

	public void setDataArrecadacao(TJDataHora dataArrecadacao) {
		this.dataArrecadacao = dataArrecadacao;
	}

	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public String getNumeroBancoPagamento() {
		return numeroBancoPagamento;
	}

	public void setNumeroBancoPagamento(String numeroBancoPagamento) {
		this.numeroBancoPagamento = numeroBancoPagamento;
	}

	public String getNumeroAgenciaPagamento() {
		return numeroAgenciaPagamento;
	}

	public void setNumeroAgenciaPagamento(String numeroAgenciaPagamento) {
		this.numeroAgenciaPagamento = numeroAgenciaPagamento;
	}
}
