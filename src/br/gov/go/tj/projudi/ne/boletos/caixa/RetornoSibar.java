package br.gov.go.tj.projudi.ne.boletos.caixa;

import java.text.DecimalFormat;
import java.util.Date;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.utils.Funcoes;

public enum RetornoSibar {
	
	OK(0, "OK"),
	INVALIDO(1, "OPERAÇÃO INVÁLIDA"),
	ERRO(2, "ERRO WEBSERVICE"),
	CODIGO_DESCONHECIDO(-1, "ERRO CÓDIGO DESCONHECIDO");
	
	private int codigo;
	private String texto;
	
	private RetornoSibar(int codigo, String texto) {
		this.codigo = codigo;
		this.texto = texto;
	}
	
	public int getCodigos() {
		return codigo;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public static RetornoSibar comCodigo(String codigo) {
		try {
			for (RetornoSibar retorno : values())
				if (retorno.codigo == Integer.parseInt(codigo))
					return retorno;
			return CODIGO_DESCONHECIDO;
		} catch (NumberFormatException e) {
			return CODIGO_DESCONHECIDO;
		}
	}
	
}
