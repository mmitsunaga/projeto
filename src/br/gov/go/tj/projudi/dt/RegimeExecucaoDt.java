package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class RegimeExecucaoDt extends RegimeExecucaoDtGen{

    private static final long serialVersionUID = 8126674214068013702L;
    public static final int CodigoPermissao=369;

    //PENA PRIVATIVA DE LIBERDADE
    public static final int FECHADO = 1;
    public static final int SEMI_ABERTO = 2;
    public static final int ABERTO = 4;
    public static final int ABERTO_DOMICILIAR = 5;
    public static final int LIVRAMENTO_CONDICIONAL = 6;
    
    public static final int ABERTO_COM_PSC = 16;
    public static final int PRISAO_DOMICILIAR = 17;
    public static final int ABERTO_SUBSTITUICAO_PENA = 18;
    public static final int ABERTO_DOMICILIAR_MONITORACAO_ELETRONICA = 19;
    public static final int SEMIABERTO_DOMICILIAR_MONITORACAO_ELETRONICA = 20;
    public static final int FECHADO_SUBSTITUICAO_PENA = 21;
    
    //MEDIDA DE SEGURANÇA
    public static final int TRATAMENTO_AMBULATORIAL = 3;
    public static final int INTERNACAO = 7;
    public static final int TRATAMENTO_PSIQUIATRICO = 9;
    
    // RELAÇÃO DE MODALIDADES DA PENA RESTRITIVA DE DIREITO. (ID DA TABELA)
	public static final int PRESTACAO_PECUNIARIA = 11;
	public static final int PERDA_BENS_VALORES = 12;
	public static final int PRESTACAO_SERVICO_COMUNIDADE = 13;
	public static final int INTERDICAO_TEMPORARIA_DIREITOS = 14;
	public static final int LIMITACAO_FIM_SEMANA = 15;
	public static final int CESTA_BASICA = 22;
    

	public RegimeExecucaoDt(){
		
	}

	public RegimeExecucaoDt(String id_RegimeExecucao, String regimeExecucao){
		this.setId(id_RegimeExecucao);
		this.setRegimeExecucao(regimeExecucao);
	}
}
