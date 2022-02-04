package br.gov.go.tj.utils.bancos;

import java.io.Serializable;

/**
 * Definições estáticas do leiaute para escrita de arquivo da Caixa Econômica Federal. 
 * @author fasoares
 */
public class LeiauteEscritaArquivoCEF implements Serializable {

	private static final long serialVersionUID = -4934523295077723224L;
	
	public static final Integer TAMANHO_LINHA = 240;
	
	public static final Integer TIPO_REGISTRO_HEADER_ARQUIVO = 0;
	public static final Integer TIPO_REGISTRO_HEADER_LOTE = 1;
	public static final Integer TIPO_REGISTRO_DETALHE = 3;
	public static final Integer TIPO_REGISTRO_TRAILER_LOTE = 5;
	public static final Integer TIPO_REGISTRO_TRAILER_ARQUIVO = 9;
	
	public static final Integer INICIO_TIPO_REGISTRO = 7;
	public static final Integer FIM_TIPO_REGISTRO = 8;
	
	public static final Integer INICIO_TIPO_REGISTRO_DETALHE = 13;
	public static final Integer FIM_TIPO_REGISTRO_DETALHE = 14;
}
