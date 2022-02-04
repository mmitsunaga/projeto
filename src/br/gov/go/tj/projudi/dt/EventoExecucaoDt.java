package br.gov.go.tj.projudi.dt;

/**
 * 
 * 
 */
public class EventoExecucaoDt extends EventoExecucaoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -7601754786260253324L;

    public static final int CodigoPermissao=367;
	
	// o valor da variável é o id do evento no banco de dados
    public static final int BLOQUEIO_SAIDA_TEMPORARIA = 1;
    public static final int BLOQUEIO_TRABALHO_EXTERNO = 2;
    public static final int COMUTACAO_PENA = 3;
    public static final int CONCESSAO_LIVRAMENTO_CONDICIONAL = 4;
    public static final int CONCESSAO_ABERTO_DOMICILIAR = 5;
    public static final int CONVERSAO_MEDIDA_SEGURANCA = 6;
    public static final int CONVERSAO_PRIVATIVA_LIBERDADE = 7;
    public static final int CONVERSAO_PENA_RESTRITIVA_DIREITO = 8;
    public static final int DESBLOQUEIO_SAIDA_TEMPORARIA = 9;
    public static final int DESBLOQUEIO_TRABALHO_EXTERNO = 10;
    public static final int DIAS_TRABALHADOS_REMICAO = 11;
	public static final int EXTINCAO_CUMPRIMENTO_PENA = 12;
	public static final int EXTINCAO_OBITO = 13;
	public static final int EXTINCAO_PRESCRICAO = 14;
	public static final int FALTA = 15;
    public static final int FUGA = 16;
    public static final int HORAS_ESTUDO_REMICAO = 17;
    public static final int INDULTO = 18;
    public static final int FALTA_GRAVE = 19;
    
    public static final int INTERRUPCAO_PRISAO_PROVISORIA = 21;
	public static final int PERDA_HORAS_ESTUDO_REMICAO = 22;
	public static final int PERDA_DIAS_TRABALHADOS_REMICAO = 24;
	public static final int PRIMEIRO_REGIME = 25;
    public static final int PRISAO = 26;
    public static final int PRISAO_PROVISORIA = 27;
    public static final int PROGRESSAO_REGIME = 28;
	public static final int REGRESSAO_REGIME = 29;
	public static final int REVOGACAO_SUSPENSAO_CONDICIONAL_PENA = 30;
    public static final int REVOGACAO_LIVRAMENTO_CONDICIONAL = 31;
    public static final int REVOGACAO_ABERTO_DOMICILIAR = 32;
	public static final int SAIDA_TEMPORARIA = 33;
	public static final int SUSPENSAO_CONDICIONAL_PENA = 34;
	public static final int CONCESSAO_SURSIS = 35;
	public static final int TRANSITO_JULGADO = 36;
	public static final int GUIA_RECOLHIMENTO_PROVISORIA = 37;
//	public static final int TRANSFERENCIA_COMARCA = 38;
	public static final int PRISAO_FLAGRANTE = 39;
	public static final int FUGA_ALVARA = 40;
	
	public static final int PRESTACAO_SERVICO_COMUNIDADE = 41;
	public static final int LIMITACAO_FIM_SEMANA = 42;
	public static final int PRESTACAO_PECUNIARIA = 43;
	public static final int PERDA_BENS_VALORES = 44;
	public static final int INTERDICAO_TEMPORARIA_DIREITOS = 45;
	public static final int INICIO_PENA_RESTRITIVA_DIREITO = 46;
	
	public static final int INICIO_PSC = 47;
	public static final int INTERRUPCAO_CUMPRIMENTO_PSC = 48;
	public static final int RETORNO_CUMPRIMENTO_PSC = 49;
	public static final int FIM_PSC = 50;
	
	public static final int INICIO_LFS = 51;
	public static final int INTERRUPCAO_CUMPRIMENTO_LFS = 52;
	public static final int RETORNO_CUMPRIMENTO_LFS = 53;
	public static final int FIM_LFS = 54;
	public static final int FALTA_LFS = 64;
	
	public static final int TEMPO_CUMPRIDO_PRD = 55;
	public static final int RETORNO_CUMPRIMENTO_LIVRAMENTO_CONDICIONAL = 56;
	
	public static final int HORAS_CUMPRIDAS_PSC = 57;
//	public static final int DIAS_CUMPRIDOS_LFS = 58;
	public static final int REINCLUSAO = 59;
	public static final int APRESENTACAO_ESPONTANEA = 60;
	public static final int INTERRUPCAO_CUMPRIMENTO_PENA = 61;
	public static final int SUSPENSAO_LIVRAMENTO_CONDICIONAL_PRISAO = 62;
	public static final int LIBERACAO_ALVARA_PRD = 63;
	public static final int REGRESSAO_CAUTELAR = 65;
	public static final int ALTERAÇÃO_REGIME = 66;
	public static final int AUDIENCIA_ADMONITORIA = 67;
	public static final int SUSPENSAO_LIVRAMENTO_CONDICIONAL_DESCUMPRIMENTO = 68;
	
	public static final int PAGAMENTO_PEC = 69;
	public static final int INICIO_ITD = 70;
	public static final int FIM_ITD = 71;
	public static final int DECISAO_SUSPENSAO_EXECUCAO = 72;
	public static final int DIAS_LEITURA_REMIDO = 73;
	public static final int PERDA_DIAS_LEITURA_REMIDO = 74;
	public static final int CONVERSAO_PRIVATIVA_LIBERDADE_CAUTELAR = 75;
	public static final int REVOGACAO_CAUTELAR_LC = 76;
	public static final int HORAS_ESTUDO_REMIDO = 77;
	public static final int PERDA_HORAS_ESTUDO_REMIDO = 78;
	public static final int CONCESSAO_PRISAO_DOMICILIAR = 79;
	
	public static final int SUSPENSAO_LC = 80;
	public static final int INICIO_MONITORACAO_ELETRONICA = 81;
	public static final int ALTERACAO_MODALIDADE = 82;
	public static final int INTERRUPCAO_PENA_PECUNIARIA = 83;
	public static final int CESTA_BASICA = 84;
	public static final int PAGAMENTO_CESTA_BASICA = 85;
	public static final int INTERRUPCAO_CESTA_BASICA = 86;
	public static final int INTERRUPCAO_ITD = 87;
}
