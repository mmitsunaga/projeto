package br.gov.go.tj.projudi.dt;

public class GuiaStatusDt extends Dados {

	private static final long serialVersionUID = 3911074161677202404L;
	
	public static final int CodigoPermissao = 119;
	
	public static final int AGUARDANDO_PAGAMENTO 				= 1;
	public static final int PAGO_ON_LINE 						= 2;
	public static final int PAGO 								= 3;
	public static final int PAGO_COM_VALOR_INFERIOR 			= 4;
	public static final int PAGO_COM_VALOR_SUPERIOR 			= 5;
	public static final int CANCELADA 							= 6;
	public static final int CANCELADA_PAGA 						= 7;
	public static final int RESSARCIDO 							= 8;
	public static final int PAGA_CANCELADA 						= 9;
	public static final int PEDIDO_RESSARCIMENTO_SOLICITADO 	= 10;
	public static final int PAGO_APOS_VENCIMENTO 				= 11;
	public static final int GUIA_GERADA_ENVIAR_PREFEITURA 		= 12;
	public static final int GUIA_ESTORNADA_PREFEITURA 			= 13;
	public static final int DIVIDA_BAIXADA_PREFEITURA 			= 14;
	public static final int PROTOCOLO_NAO_CADASTRADO_PREFEITURA = 15;
	public static final int PROCESSO_INATIVO_TJGO_PREFEITURA    = 16;
	public static final int PROTOCOLO_FORA_CONVENIO_PREFEITURA  = 18;
	public static final int GUIA_COMPLEMENTAR_GERADA_PAGA 		= 21;
	public static final int AGUARDANDO_DEFERIMENTO              = 20;
	public static final int BAIXADA_COM_ASSISTENCIA             = 22;
	public static final int BAIXADA_COM_ISENCAO                 = 23;
	public static final int PAGO_DARE_CADIN                     = 24;
	public static final int ESTORNO_BANCARIO                    = 25;
	public static final int PARCELAMENTO_PAGO                   = 26;
	public static final int PARCELAMENTO_REALIZADO 				= 29;
	
	//*** Variáveis para SPG/SSG
	public static final String PAGO_SSG = "PAGO (SSG)";
	public static final String AGUARDANDO_PAGAMENTO_SSG = "AGUARDANDO PAGAMENTO (SSG)";
	public static final String NOME_GUIA_SSG_PEDIDO_RESSARCIMENTO_SOLICITADO = "PEDIDO RESSARCIMENTO SOLICITADO (SSG)";
	
	public static final String PAGO_SPG = "PAGO (SPG)";
	public static final String AGUARDANDO_PAGAMENTO_SPG = "AGUARDANDO PAGAMENTO (SPG)";
	public static final String NOME_GUIA_SPG_PEDIDO_RESSARCIMENTO_SOLICITADO = "PEDIDO RESSARCIMENTO SOLICITADO (SPG)";
	
	private String id;
	private String guiaStatus;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getGuiaStatus() {
		return guiaStatus;
	}

	public void setGuiaStatus(String guiaStatus) {
		this.guiaStatus = guiaStatus;
	}
	

}
