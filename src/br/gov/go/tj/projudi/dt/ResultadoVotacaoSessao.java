package br.gov.go.tj.projudi.dt;

public enum ResultadoVotacaoSessao {
	UNANIMIDADE(2, "Unanimidade"), MAIORIA_RELATOR(0, "Por Maioria"), MAIORIA_DIVERGE(-1, "Por Maioria - Com voto vencido");

	private int codigo;
	private String descricao;

	ResultadoVotacaoSessao(int codigo, String descricao) {
		this.descricao = descricao;
		this.codigo = codigo;
	}
	
	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}


}
