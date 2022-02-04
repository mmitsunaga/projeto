package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

public class ItemProdutividadeDt extends Dados {

	/**
     * 
     */
    private static final long serialVersionUID = -1385220986750219610L;
    public static final int CodigoPermissao=538;
	private String id_EstatisticaProdutividadeItem;
	private String estatisticaProdutividadeItem;
	
	public ItemProdutividadeDt() {
		limpar();
	}
	
	public void limpar() {
		setId_EstatisticaProdutividadeItem("");
		setEstatisticaProdutividadeItem("");
	}
	
	public String getId_EstatisticaProdutividadeItem() {
		return id_EstatisticaProdutividadeItem;
	}

	public void setId_EstatisticaProdutividadeItem(String idEstatisticaProdutividadeItem) {
		id_EstatisticaProdutividadeItem = idEstatisticaProdutividadeItem;
	}

	public String getEstatisticaProdutividadeItem() {
		return estatisticaProdutividadeItem;
	}

	public void setEstatisticaProdutividadeItem(String estatisticaProdutividadeItem) {
		this.estatisticaProdutividadeItem = estatisticaProdutividadeItem;
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {
	}
}
