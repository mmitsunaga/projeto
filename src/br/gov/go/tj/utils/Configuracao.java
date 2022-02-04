package br.gov.go.tj.utils;

public class Configuracao {

	/**
	 * Intervalo de tempo para um usuário ficar com um elemento reservado, inicialmente criado para
	 * resolusão de pendências e movimentações. Medido em horas. 
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

    // tipo de ação sem uso de permissão
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

    //vari�vel que determina a quantidade m�xima de processos aceitos para advogados de outras oab's
    public static final int QUANTIDADE_MAXIMA_PROCESSOS_ADVOGADOS_OUTRAS_OAB = 5;

    //três anos de validade do ponteiro
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
	 * Método com mensagens genéricas.
	 * 
	 * @param int tipoMensagem
	 * @return String
	 */
	public static String getMensagem(int tipoMensagem) {
		String retorno = "";
		
		switch( tipoMensagem ) {
			case MENSAGEM_DADOS_GUIA_DIFERENTE_PROCESSO : {
				retorno = "Aten��o! Dados atuais da guia n�o confere com os dados do processo selecionado.<br/>Verifique se � este o processo que realmente deseja acessar.";
				break;
			}
			case MENSAGEM_BANCO_NAO_CONVENIADO_ONLINE : {
				retorno = "Aten��o! Este banco ainda n�o possui conv�nio para pagamento On-Line.\n Escolha outro banco de sua prefer�ncia ou escolha a op��o Emitir Boleto para imprimir o boleto banc�rio.";
				break;
			}
			case MENSAGEM_GUIA_SEM_ITEM : {
				retorno = "Nenhum Item de Guia Localizado. A Guia deve Conter 1 ou mais Itens de Custa.";
				break;
			}
			case MENSAGEM_BAIRROS_NAO_ZONEADOS : {
				retorno = "Aten��o: Um ou mais Bairros n�o est�o zoneados.<br />Por favor, entrar em contato com o suporte do Projudi para solicitar o zoneamento do(s) Bairro(s).";
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
				retorno = "N�o pode ser adicionado locomo��o nova. Somente pode substituir locomo��es que j� foram pagas.<br /><br />Caso deseje alguma uma locomo��o nova, por favor emitir uma nova guia de locomo��o.";
				break;
			}
			case MENSAGEM_ADVERTENCIA_CONTADOR_EMISSAO_DE_GUIA : {
				retorno = "<b>Aten��o Sr(a) Contador(a)</b>!<br /><br />De acordo com a situa��o que este processo se encontra, esta guia pode ser emitida somente em caso de decis�o judicial.";
				break;
			}
			case MENSAGEM_ADVERTENCIA_EMISSAO_GUIA_MESMO_TIPO : {
				retorno = "<b>Aten��o Sr(a) Contador(a)</b>!<br /><br />J� existe guia deste mesmo tipo emitida para este processo.<br /><br /> Deseja continuar sem cancelar a j� emitida?";
				//retorno = "";
				break;
			}
			case MENSAGEM_ADVERTENCIA_GUIA_ZERADA : {
				retorno = "<b>Aten��o Sr(a) Contador(a)</b>!<br /><br /><br />A guia est� com o total zerado. Verifique se foi preenchido corretamente.";
				break;
			}
			case MENSAGEM_FALHA_CONECTAR_SPG : {
				retorno = "<b>Aten��o</b>!<br /><br /><br />N�o � poss�vel acessar o m�dulo de emiss�o de guias.<br />N�o foi poss�vel conectar no SPG.";
				break;
			}
			case MENSAGEM_BAIRRO_NAO_ZONEADO : {
				retorno = "Aten��o: Bairro n�o zoneado.<br />Por favor, entrar em contato com o suporte do Projudi para solicitar o zoneamento do Bairro.";
				break;
			}
			case MENSAGEM_GUIA_INICIAL_PROCESSO_VINCULADO_NAO_EXISTE : {
				retorno = "Aten��o: O n�mero de processo vinculado informado n�o existe ou a serventia do processo dependente n�o encontrado.<br /><br />Por favor, confira o n�mero do processo. Ele deve existir. ";
				break;
			}
			case MENSAGEM_MODELO_GUIA_LOCOMOCAO_NAO_EXISTE : {
				retorno = "N�o existe modelo do c�lculo de locomo��o para a classe vinculada ao processo. Favor entrar em contato com o suporte.";
				break;
			}
			case MENSAGEM_GUIA_LOCOMOCAO_COMPLEMENTAR_SEM_ITENS : {
				retorno = "Insira pelo menos uma Locomo��o n�o Utilizada para a Emiss�o da Guia de Locomo��o Complementar.";
				break;
			}
			case MENSAGEM_GUIA_LOCOMOCAO_SEM_BAIRROS : {
				retorno = "Insira pelo menos um Bairro para a Emiss�o da Guia de Locomo��o.";
				break;
			}
			case MENSAGEM_VINCULAR_GUIA_INICIAL_COMPLEMENTAR_JA_EXISTE : {
				retorno = "Este processo j� possui guia inicial ou guia complementar da inicial. Por favor, confirme se � necess�rio vincular mais esta guia.";
				break;
			}
			case MENSAGEM_GUIAS_SERAO_RECEBIDAS_PELO_BB_CAIXA : {
				retorno = "Para realizar o pagamento desta guia em qualquer banco � necess�rio gerar o seu boleto. Ap�s a emiss�o, acesse o endere�o <b>https://projudi.tjgo.jus.br/GerarBoleto</b> informando o n�mero da guia para gerar o boleto. Outra op��o � consultar esta guia no processo ap�s a sua emiss�o e clicar no bot�o <b>Gerar Boleto</b>.";
				break;
			}
			case MENSAGEM_GUIA_DEVE_ESTAR_AGUARDANDO_PAGAMENTO_E_NAO_VENCIDA : {
				retorno = "A guia deve estar aguardando pagamento e n�o pode estar vencida.";
				break;
			}
			case MENSAGEM_SEM_PERMISSAO_EMITIR_GUIA_PROCESSO : {
				retorno = "Aten��o! Sem permiss�o para emitir guia para este processo.";
				break;
			}
		}
		
		return retorno;
	}
}	
