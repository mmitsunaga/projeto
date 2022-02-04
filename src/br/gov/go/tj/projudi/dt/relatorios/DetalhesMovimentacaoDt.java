package br.gov.go.tj.projudi.dt.relatorios;


public class DetalhesMovimentacaoDt {
	
	private String movimentacaoTipo;
	private String qtdTotal;
		
	public DetalhesMovimentacaoDt(){
		movimentacaoTipo = "";
		qtdTotal = "";
	}
	
	public String getQtdTotal() {
		return qtdTotal;
	}
	
	public void setQtdTotal(String qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

	public String getMovimentacaoTipo() {
		return movimentacaoTipo;
	}

	public void setMovimentacaoTipo(String movimentacaoTipo) {
		this.movimentacaoTipo = movimentacaoTipo;
	}
	
}
