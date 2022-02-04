package br.gov.go.tj.utils.bancos;

import java.io.Serializable;

/**
 * Definições estáticas do leiaute para leitura de arquivo da Caixa Econômica Federal. 
 * @author fasoares
 */
public class LeiauteLeituraArquivoCEF implements Serializable {

	private static final long serialVersionUID = -3712844025983438200L;

	public static final Integer INICIO_NUMERO_DOCUMENTO = 58;
	public static final Integer FIM__NUMERO_DOCUMENTO = 69;
	
	public static final Integer INICIO_VALOR_RECEBIDO = 81;
	public static final Integer FIM_VALOR_RECEBIDO = 96;
	
	public static final Integer INICIO_DATA_RECEBIMENTO = 137;
	public static final Integer FIM_DATA_RECEBIMENTO = 145;
	
	public static final Integer INICIO_CODIGO_BANCO = 96;
	public static final Integer FIM_CODIGO_BANCO = 99;

	public static final Integer INICIO_CODIGO_AGENCIA = 99;
	public static final Integer FIM_CODIGO_AGENCIA = 104;
	
	public static final Integer INICIO_CODIGO_AGENCIA_DIGITO = 104;
	public static final Integer FIM_CODIGO_AGENCIA_DIGITO = 105;
	
	public static final Integer INICIO_DATA_GERACAO = 143;
	public static final Integer FIM_DATA_GERACAO = 151;
	
	public static final Integer INICIO_HORA_GERACAO = 151;
	public static final Integer FIM_HORA_GERACAO = 157;
}
