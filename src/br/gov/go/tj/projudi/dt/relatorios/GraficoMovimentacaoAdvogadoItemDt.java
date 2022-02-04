package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do gráfico de movimentações de advogados
 */
public class GraficoMovimentacaoAdvogadoItemDt extends Dados implements Serializable,Comparable<GraficoMovimentacaoAdvogadoItemDt> {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -1044445327566244433L;
	
	private String dataAnalise;
	private String dataComparacao;
	private String estatisticaMovimentacaoItem;	
	private long quantidade;
	private String hora;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public GraficoMovimentacaoAdvogadoItemDt() {
		limpar();
	}

	public void limpar() {
		setDataAnalise("");
		setDataComparacao("");		
		setEstatisticaMovimentacaoItem("");		
		setQuantidade(0);
	}

	public String getDataAnalise() {
		return dataAnalise;
	}

	public void setDataAnalise(String dataAnalise) {
		this.dataAnalise = dataAnalise;
	}

	public String getDataComparacao() {
		return dataComparacao;
	}

	public void setDataComparacao(String dataComparacao) {
		this.dataComparacao = dataComparacao;
	}
	
	public String getEstatisticaMovimentacaoItem() {
		return estatisticaMovimentacaoItem;
	}

	public void setEstatisticaMovimentacaoItem(String estatisticaMovimentacaoItem) {
		this.estatisticaMovimentacaoItem = estatisticaMovimentacaoItem;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {

	}

	public int compareTo(GraficoMovimentacaoAdvogadoItemDt objeto) {
		return this.getHora().compareTo(objeto.getHora());
	}
}
