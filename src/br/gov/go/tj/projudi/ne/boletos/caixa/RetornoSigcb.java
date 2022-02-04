package br.gov.go.tj.projudi.ne.boletos.caixa;

import java.text.DecimalFormat;
import java.util.Date;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.utils.Funcoes;

public enum RetornoSigcb {
	
	EFETUADA(0, "OPERAÇÃO EFETUADA"),
	NAO_EFETUADA(1, "OPERAÇÃO NÃO EFETUADA"),
	ERRO_INTERNO(2, "ERRO INTERNO"),
	CODIGO_DESCONHECIDO(-1, "ERRO CÓDIGO DESCONHECIDO");
	
	private int codigo;
	private String texto;
	
	private RetornoSigcb(int codigo, String texto) {
		this.codigo = codigo;
		this.texto = texto;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public static RetornoSigcb comCodigo(String codigo) {
		try {
			for (RetornoSigcb retorno : values())
				if (retorno.codigo == Integer.parseInt(codigo))
					return retorno;
			return CODIGO_DESCONHECIDO;
		} catch (NumberFormatException e) {
			return CODIGO_DESCONHECIDO;
		}
	}
	
}
