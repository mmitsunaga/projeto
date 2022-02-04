package br.gov.go.tj.projudi.dt;

public class PendenciaStatusDt extends PendenciaStatusDtGen {

	//public static final int CodigoPermissao=7317;

    private static final long serialVersionUID = -3528154193614648950L;

    /**
	 * Mudei
	 * @author Ronneesley Moura Teles
	 * @since 13/01/2009 17:35
	 */
	public static final int CodigoPermissao = 287;

	public static final int AGUARDANDO_PARECER_CODIGO_TEMP = -1;
	public static final int AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP = -2;
	public static final int AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP = -3;
	public static final int AGUARDANDO_PARECER_DIARIO_ELETRONICO_CODIGO_TEMP = -4;
	public static final int ID_EM_ANDAMENTO = 1;
	public static final int ID_CUMPRIDA = 2;
	public static final int ID_NAO_CUMPRIDA = 3;
	public static final int ID_CUMPRIDA_PARCIALMENTE = 4;
	public static final int ID_CANCELADA = 5;
	public static final int ID_ENCAMINHADA = 6;
	public static final int ID_CORRECAO = 7;
	public static final int ID_AGUARDANDO_RETORNO = 8;
	public static final int ID_AGUARDANDO_VISTO = 9;
	public static final int ID_AGUARDANDO_CUMPRIMENTO = 10;
	public static final int ID_CUMPRIMENTO_AGUARDANDO_VISTO = 11;
	public static final int ID_PRE_ANALISADA = 12;
	public static final int ID_DESCARTADA = 13;
	public static final int ID_AGUARDANDO_ENVIO_CORREIOS=14;
	public static final int ID_AGUARDANDO_CONFIRMACAO_CORREIOS=15;
	public static final int ID_RECEBIDO_CORREIOS=16;
	public static final int ID_AGUARDANDO_PAGAMENTO_POSTAGEM=17;
	public static final int ID_INCONSISTENCIA_CORREIOS=18;

	/**
	 * Retorna o termo apropriado para o status de uma pré-análise
	 * @param tipo, status da pré-analise
	 * 
	 * @author msapaula
	 */
	public static String getStatusPreAnalise(int tipo) {
		switch (tipo) {
		case ID_CUMPRIDA:
			return "Assinada";
		case ID_CORRECAO:
			return "Corrigida";
		default:
			return "";
		}
	}
}
