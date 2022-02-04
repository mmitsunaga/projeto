package br.gov.go.tj.projudi.ne.boletos;

public enum SituacaoBoleto {
	
	NAO_REGISTRADO("0", "NAO REGISTRADO"),
	EM_ABERTO("1", "EM ABERTO"),
	BAIXA_POR_DEVOLUCAO("2", "BAIXA POR DEVOLUCAO"), 
	BAIXA_POR_ESTORNO("3", "BAIXA POR ESTORNO"),
	BAIXA_POR_PROTESTO("4", "BAIXA POR PROTESTO"),
	ENVIADO_AO_CARTORIO("5", "ENVIADO AO CARTORIO"),
	LIQUIDADO("6", "LIQUIDADO"),
	LIQUIDADO_NO_CARTORIO("7", "LIQUIDADO NO CARTORIO"), 
	SOMENTE_PARA_PROTESTO("8", "SOMENTE PARA PROTESTO"),
	SUSTADO_CARTORIO("9", "SUSTADO CARTORIO"),
	TITULO_JA_PAGO_NO_DIA("10", "TITULO JA PAGO NO DIA");
	
	private String codigo;
	private String texto;
	
	private SituacaoBoleto(String codigo, String texto) {
		this.codigo = codigo;
		this.texto = texto;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public static SituacaoBoleto comCodigo(String codigo) {
		for (SituacaoBoleto s : values())
			if (s.codigo.equals(codigo))
				return s;
		throw new IllegalArgumentException("Código " + codigo + " de situação de boleto inválido.");
	}
	
	public static SituacaoBoleto comTexto(String texto) {
		if (texto.contains("= "))
			texto = texto.split("= ")[1];
		for (SituacaoBoleto s : values())
			if (s.texto.equals(texto))
				return s;
		throw new IllegalArgumentException("Texto " + texto + " de situação de boleto inválido.");
	}
	
}
