package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class EventoExecucaoTipoDt extends EventoExecucaoTipoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -2070260888299759448L;

    public static final int CodigoPermissao=366;

	public static final int GENERICO = 2; // evento que possui data final
	public static final int INFORMATIVO = 4; // evento que NÃO possui data final
	public static final int INTERRUPCAO = 5; // eventos que possui data final, mas NÃO considera o tempo como tempo cumprido (ex.: fuga, liberdade provisória)
	public static final int REMICAO = 6; // evento informativo utilizado no calculo de remição de pena
	public static final int HISTORICO_PSC = 7; // evento de controle do histórico do cumprimento da prestação de serviço à comunidade
	public static final int HISTORICO_LFS = 8; // evento de controle do histórico do cumprimento da limitação de fim de semana 
	public static final int HISTORICO_PEC = 9; // evento de controle do histórico do cumprimento da prestação pecuniária
	public static final int HISTORICO_ITD = 10; // evento de controle do histórico do cumprimento da interdição temporária de direitos
	public static final int HISTORICO_PCB = 11; // evento de controle do histórico do cumprimento do pagamento de cestas básicas
}
