package br.gov.go.tj.utils;

public class Configuracao {

	/**
	 * Intervalo de tempo para um usuÃ¡rio ficar com um elemento reservado, inicialmente criado para
	 * resolusÃ£o de pendÃªncias e movimentaÃ§Ãµes. Medido em horas. 
	 */	
	public static final int IntervaloReservadoTemp = 1;
		
    public static final int TamanhoRetornoConsulta = 15;

    public static final int QtdPermissao = 10;

    public static final int ExcluirResultado = 0;
    public static final int Imprimir = 1;
    public static final int Localizar = 2;

    public static final int LocalizarDWR = 3;
    public static final int Novo = 4;
    public static final int SalvarResultado = 5;

    public static final int Curinga6 = 6;
    public static final int Curinga7 = 7;
    public static final int Curinga8 = 8;
    public static final int Curinga9 = 9;

    // tipo de aÃ§Ã£o sem uso de permissÃ£o
    public static final int Editar = -1;
    public static final int Salvar = -2;
    public static final int Excluir = -3;
    public static final int LocalizarAutoPai = -4;
    public static final int LiberarCertificado = -5;
    public static final int Mensagem = -6;
    public static final int Limpar = -9;
    public static final int PRAZO_LEITURA_AUTOMATICA = 10;
    public static final int INICIO_CONTAGEM_PRAZO_DIARIO_ELETRONICO = 2;
    public static final int Cancelar = -10;
    public static final int Atualizar = -12;
 	public static final int EditarAjax = -13;
    
    public static final String SEPARDOR01 = "<;@>";
    public static final String SEPARDOR02 = "<@#>";
    public static final String SEPARDOR03 = "<#;>";
    
    public static final String SEPARDOR_ANTIGO01 = "@";
    public static final String SEPARDOR_ANTIGO02 = "#";
    public static final String SEPARDOR_ANTIGO03 = ";";
    
    public static final String JTR = "8.09";
    
    public static final int QtdPartesDadosProcesso = 5;
    
    public static final int QtdOutrasPartesDadosProcesso = 2;

    public static final String NUMERO_INICIAL_PROCESSO = "5000000";
    public static final String NUMERO_INICIAL_GUIA 	   = "0";

    //variável que determina a quantidade máxima de processos aceitos para advogados de outras oab's
    public static final int QUANTIDADE_MAXIMA_PROCESSOS_ADVOGADOS_OUTRAS_OAB = 5;

    //trÃªs anos de validade do ponteiro
    // alterado para 24 meses para resolver problemas em algumas serventias
    // prazo contido no ato normativo numero 2
    public static final int TEMPO_VALIDADE_PONTEIRO = 720;
    
    public static final int TEMPO_VALIDADE_PONTEIRO_CENOPES = 1;
	
    
    /** ************************************************
     * Mensagens
     */
    public static final int MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO 					= 1;
    public static final int MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE 					= 2;
    public static final int MENSAGEM_GUIA_SEM_ITEM 									= 3;
    public static final int MENSAGEM_BAIRROS_NAO_ZONEADOS 							= 4;
    public static final int MENSAGEM_INFORME_RECORRIDO_RECORRENTE 					= 5;
    public static final int MENSAGEM_TOTAL_RATEIO_ERRADO 							= 6;
    public static final int MENSAGEM_SEM_PERMISSAO_ADD_LOCOMOCAO_COMPLEMENTAR 		= 7;
    public static final int MENSAGEM_ADVERTENCIA_CONTADOR_EMISSAO_DE_GUIA			= 8;
    public static final int MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO			= 9;
    public static final int MENSAGEM_ADVERTENCIA_GUIA_ZERADA						= 10;
    public static final int MENSAGEM_FALHA_CONECTAR_SPG 							= 11;
    public static final int MENSAGEM_BAIRRO_NAO_ZONEADO 							= 12;
    public static final int MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE 	= 13;
    public static final int MENSAGEM_MODELO_GUIA_LOCOMOCAO_NAO_EXISTE           	= 14;
    public static final int MENSAGEM_GUIA_LOCOMOCAO_COMPLEMENTAR_SEM_ITENS      	= 15;
    public static final int MENSAGEM_GUIA_LOCOMOCAO_SEM_BAIRROS                 	= 16;
    public static final int MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE 	= 17;
    public static final int MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA 			= 18;
    public static final int MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA = 19;
    public static final int MENSAGEM_SEM_PERMISSAO_EMITIR_GUIA_PROCESSO 			= 20;

    //constante usada para definir a probabilidade de receber processo na distribuicao
    //garantindo o minimo de aleatoriedade
	public static final double PROBABILIDADE_PADRAO = 0.8d;
    
    /**
	 * MÃ©todo com mensagens genÃ©ricas.
	 * 
	 * @param int tipoMensagem
	 * @return String
	 */
	public static String getMensagem(int tipoMensagem) {
		String retorno = "";
		
		switch( tipoMensagem ) {
			case MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO : {
				retorno = "Atenção! Dados atuais da guia não confere com os dados do processo selecionado.<br/>Verifique se é este o processo que realmente deseja acessar.";
				break;
			}
			case MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE : {
				retorno = "Atenção! Este banco ainda não possui convênio para pagamento On-Line.\n Escolha outro banco de sua preferência ou escolha a opção Emitir Boleto para imprimir o boleto bancário.";
				break;
			}
			case MENSAGEM_GUIA_SEM_ITEM : {
				retorno = "Nenhum Item de Guia Localizado. A Guia deve Conter 1 ou mais Itens de Custa.";
				break;
			}
			case MENSAGEM_BAIRROS_NAO_ZONEADOS : {
				retorno = "Atenção: Um ou mais Bairros não estão zoneados.<br />Por favor, entrar em contato com o suporte do Projudi para solicitar o zoneamento do(s) Bairro(s).";
				break;
			}
			case MENSAGEM_INFORME_RECORRIDO_RECORRENTE : {
				retorno = "Por favor, informe o Recorrente e/ou Recorrido.";
				break;
			}
			case MENSAGEM_TOTAL_RATEIO_ERRADO : {
				retorno = "A quantidade de porcentagem do rateio para cada parte deve dar um total de 100%.";
				break;
			}
			case MENSAGEM_SEM_PERMISSAO_ADD_LOCOMOCAO_COMPLEMENTAR : {
				retorno = "Não pode ser adicionado locomoção nova. Somente pode substituir locomoções que já foram pagas.<br /><br />Caso deseje alguma uma locomoção nova, por favor emitir uma nova guia de locomoção.";
				break;
			}
			case MENSAGEM_ADVERTENCIA_CONTADOR_EMISSAO_DE_GUIA : {
				retorno = "<b>Atenção Sr(a) Contador(a)</b>!<br /><br />De acordo com a situação que este processo se encontra, esta guia pode ser emitida somente em caso de decisão judicial.";
				break;
			}
			case MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO : {
				retorno = "<b>Atenção Sr(a) Contador(a)</b>!<br /><br />Já existe guia deste mesmo tipo emitida para este processo.<br /><br /> Deseja continuar sem cancelar a já emitida?";
				//retorno = "";
				break;
			}
			case MENSAGEM_ADVERTENCIA_GUIA_ZERADA : {
				retorno = "<b>Atenção Sr(a) Contador(a)</b>!<br /><br /><br />A guia está com o total zerado. Verifique se foi preenchido corretamente.";
				break;
			}
			case MENSAGEM_FALHA_CONECTAR_SPG : {
				retorno = "<b>Atenção</b>!<br /><br /><br />Não é possível acessar o módulo de emissão de guias.<br />Não foi possível conectar no SPG.";
				break;
			}
			case MENSAGEM_BAIRRO_NAO_ZONEADO : {
				retorno = "Atenção: Bairro não zoneado.<br />Por favor, entrar em contato com o suporte do Projudi para solicitar o zoneamento do Bairro.";
				break;
			}
			case MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE : {
				retorno = "Atenção: O número de processo vinculado informado não existe ou a serventia do processo dependente não encontrado.<br /><br />Por favor, confira o número do processo. Ele deve existir. ";
				break;
			}
			case MENSAGEM_MODELO_GUIA_LOCOMOCAO_NAO_EXISTE : {
				retorno = "Não existe modelo do cálculo de locomoção para a classe vinculada ao processo. Favor entrar em contato com o suporte.";
				break;
			}
			case MENSAGEM_GUIA_LOCOMOCAO_COMPLEMENTAR_SEM_ITENS : {
				retorno = "Insira pelo menos uma Locomoção não Utilizada para a Emissão da Guia de Locomoção Complementar.";
				break;
			}
			case MENSAGEM_GUIA_LOCOMOCAO_SEM_BAIRROS : {
				retorno = "Insira pelo menos um Bairro para a Emissão da Guia de Locomoção.";
				break;
			}
			case MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE : {
				retorno = "Este processo já possui guia inicial ou guia complementar da inicial. Por favor, confirme se é necessário vincular mais esta guia.";
				break;
			}
			case MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA : {
				retorno = "Para realizar o pagamento desta guia em qualquer banco é necessário gerar o seu boleto. Após a emissão, acesse o endereço <b>https://projudi.tjgo.jus.br/GerarBoleto</b> informando o número da guia para gerar o boleto. Outra opção é consultar esta guia no processo após a sua emissão e clicar no botão <b>Gerar Boleto</b>.";
				break;
			}
			case MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA : {
				retorno = "A guia deve estar aguardando pagamento e não pode estar vencida.";
				break;
			}
			case MENSAGEM_SEM_PERMISSAO_EMITIR_GUIA_PROCESSO : {
				retorno = "Atenção! Sem permissão para emitir guia para este processo.";
				break;
			}
		}
		
		return retorno;
	}
}	
