package br.gov.go.tj.projudi.ne.boletos.caixa;

public enum OperacaoSibar {
	CONSULTA_BOLETO("1.0"), INCLUI_BOLETO("1.2"), BAIXA_BOLETO("1.2"), ALTERA_BOLETO("1.2");
	
	private String versao;
	
	public String getVersao() {
		return versao;
	}
	
	private OperacaoSibar(String versao) {
		this.versao = versao;
	}
	
}
