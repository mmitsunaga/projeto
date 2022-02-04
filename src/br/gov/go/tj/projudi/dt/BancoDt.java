package br.gov.go.tj.projudi.dt;

public class BancoDt extends BancoDtGen {

	private static final long serialVersionUID = 5916017852494334277L;
	public static final int CodigoPermissao = 178;
	//
	// ID NA TABELA DE BANCOS
	//
	public static final int BANCO_DO_BRASIL = 1;
	public static final int CAIXA_ECONOMICA_FEDERAL = 2;
	public static final int ITAU = 3;
	//
	public static final int CODIGO_CAIXA_ECONOMICA_FEDERAL = 104;
	//
	// variaveis para enviar texto para o banco. 
	// pagamento de mandados com e sem
	// custas
	//
	public static final String CODIGO_BANCO_COM_CUSTAS = "104";
	public static final String CODIGO_BANCO_SEM_CUSTAS = "104";
	public static final String CODIGO_CONVENIO_CAIXA_COM_CUSTAS = "307142";
	public static final String CODIGO_CONVENIO_CAIXA_SEM_CUSTAS = "307140";
	public static final String CODIGO_AGENCIA_CAIXA_COM_CUSTAS = "02535";
	public static final String CODIGO_AGENCIA_CAIXA_SEM_CUSTAS = "02535";
	public static final String DIGITO_AGENCIA_CAIXA_COM_CUSTAS = "6";
	public static final String DIGITO_AGENCIA_CAIXA_SEM_CUSTAS = "6";
	public static final String NUMERO_CONTA_CAIXA_COM_CUSTAS = "000600071004";
	public static final String NUMERO_CONTA_CAIXA_SEM_CUSTAS = "000600071008";
	public static final String DIGITO_CONTA_CAIXA_COM_CUSTAS = "4";
	public static final String DIGITO_CONTA_CAIXA_SEM_CUSTAS = "7";
	public static final String IDENTIFICADOR_TEXTO_COM_CUSTAS = "5";
	public static final String IDENTIFICADOR_TEXTO_SEM_CUSTAS = "0";
	public static final String TIPO_TEXTO_BANCO_TESTE = "T";
	public static final String TIPO_TEXTO_BANCO_PRODUCAO = "P";
	//
	
	//
	// ATENÇÃO: Somente está presente na lista o banco do Brasil, pois foi o único
	// que nos atendeu.
	// Lista com os bancos que já estão funcionando
	//
	public static final int[] listaBancos = { BANCO_DO_BRASIL, CAIXA_ECONOMICA_FEDERAL, ITAU };
}
