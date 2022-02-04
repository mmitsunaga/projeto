package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     OcorrenciaPrevisaoRepasse.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       06/2015 
 */
public class OcorrenciaPrevisaoRepasseDt implements Serializable, Comparable<OcorrenciaPrevisaoRepasseDt> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2369331047002296385L;
	
	private String id_guiaEmissao;
	private String numeroGuiaEmissao;
	private String id_processo;
	private String numeroProcesso;	
	private TJDataHora dataMovimento;
	private TJDataHora dataPagamento;
	private TJDataHora dataPrevisaoRepasse;
	private String valorTotalGuia;
	private String valorRecebimento;
		
	public OcorrenciaPrevisaoRepasseDt(String id_guiaEmissao, String numeroGuiaEmissao, String id_processo, String numeroProcesso, TJDataHora dataMovimento, TJDataHora dataPagamento, TJDataHora dataPrevisaoRepasse, String valorTotalGuia, String valorRecebimento) {
		this.id_guiaEmissao = id_guiaEmissao;
		this.numeroGuiaEmissao = numeroGuiaEmissao;
		this.id_processo = id_processo;
		this.numeroProcesso = numeroProcesso;
		this.dataMovimento = dataMovimento;
		this.dataPagamento = dataPagamento;
		this.dataPrevisaoRepasse = dataPrevisaoRepasse;
		this.setValorTotalGuia(valorTotalGuia);
		this.setValorRecebimento(valorRecebimento);
	}
	
	
	public int compareTo(OcorrenciaPrevisaoRepasseDt o) {
		// Ordenação crescente...
		//return (dataPagamento.getCalendar().compareTo(o.getDataPagamento().getCalendar()));
		return (numeroProcesso.compareTo(o.getNumeroProcesso()));
	}

	public String getId_guiaEmissao() {
		return id_guiaEmissao;
	}

	public void setId_guiaEmissao(String id_guiaEmissao) {
		this.id_guiaEmissao = id_guiaEmissao;
	}

	public String getNumeroGuiaEmissao() {
		return numeroGuiaEmissao;
	}

	public void setNumeroGuiaEmissao(String numeroGuiaEmissao) {
		this.numeroGuiaEmissao = numeroGuiaEmissao;
	}

	public String getId_processo() {
		return id_processo;
	}

	public void setId_processo(String id_processo) {
		this.id_processo = id_processo;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public TJDataHora getDataMovimento() {
		return dataMovimento;
	}

	public void setDataMovimento(TJDataHora dataPagamento) {
		this.dataMovimento = dataPagamento;
	}
	
	public TJDataHora getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(TJDataHora dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public TJDataHora getDataPrevisaoRepasse() {
		return dataPrevisaoRepasse;
	}

	public void setDataPrevisaoRepasse(TJDataHora dataPrevisaoRepasse) {
		this.dataPrevisaoRepasse = dataPrevisaoRepasse;
	}

	public String getValorTotalGuia() {
		return valorTotalGuia;
	}

	public void setValorTotalGuia(String valorTotalGuia) {
		this.valorTotalGuia = valorTotalGuia;
	}

	public String getValorRecebimento() {
		return valorRecebimento;
	}

	public void setValorRecebimento(String valorRecebimento) {
		this.valorRecebimento = valorRecebimento;
	}
}
