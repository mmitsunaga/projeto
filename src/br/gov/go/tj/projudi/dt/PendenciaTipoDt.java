package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

public class PendenciaTipoDt extends PendenciaTipoDtGen {

	private static final long serialVersionUID = 7790828477839514750L;

    public static final int CodigoPermissao = 187;
    
	public static final int CARTA_CITACAO = 1;
	public static final int INTIMACAO = 2;
	public static final int ALVARA = 3;
	public static final int MANDADO = 4;
	public static final int EDITAL = 5;
	public static final int OFICIO = 6;
	public static final int CARTA_PRECATORIA = 7;
	public static final int CARTA_ADJUDICACAO = 8;
	public static final int CUMPRIMENTO_GENERICO = 9;
	public static final int VERIFICAR_NOVO_PROCESSO = 10;
	public static final int CONCLUSO_DECISAO = 11;
	public static final int CONCLUSO_DESPACHO = 12;
	public static final int CONCLUSO_SENTENCA = 13;
	public static final int CONCLUSO_GENERICO = 14;
	public static final int PUBLICACAO_PUBLICA = 15;
	public static final int VERIFICAR_PETICAO = 16;
	public static final int VERIFICAR_PROCESSO = 17;
	public static final int CONCLUSO_PRESIDENTE = 18;
	public static final int CONCLUSO_RELATOR = 19;
	public static final int CONCLUSO_DESPACHO_PRESIDENTE = 20;
	public static final int CONCLUSO_DESPACHO_RELATOR = 21;
	public static final int ENVIAR_INSTANCIA_SUPERIOR = 22;
	public static final int MARCAR_SESSAO = 23;
	public static final int ARQUIVAMENTO = 24;
	public static final int DESARQUIVAMENTO = 25;
	public static final int REMARCAR_SESSAO = 26;
	public static final int DESMARCAR_SESSAO = 27;
	public static final int RETORNAR_SERVENTIA_ORIGEM = 28;
	public static final int OFICIO_DELEGACIA = 29;
	public static final int PEDIDO_CONTADORIA = 30;
	
	public static final int CONTADORIA_CALCULO_LIQUIDACAO = 345;
	public static final int CONTADORIA_CALCULO_CUSTAS = 346;
	public static final int CONTADORIA_CALCULO_CONTA = 347;
	public static final int CONTADORIA_CALCULO_TRIBUTOS = 348;
	public static final int CONTADORIA_JUNTADA_DOCUMENTO = 349;
	public static final int PEDIDO_CONTADORIA_CRIMINAL = 350;
	
	public static final int VERIFICAR_CONEXAO = 31;
	public static final int SUSPENSAO_PROCESSO = 32;
	public static final int CALCULAR_LIQUIDACAO_PENAS = 33;
	/*public static final int AUTORIZACAO_SAIDA_TEMPORARIA = 34;
	public static final int AUTORIZACAO_TRABALHO_EXTERNO = 35;
	public static final int EXTINCAO_CUMPRIMENTO_PENA = 36;
	public static final int EXTINCAO_PRESCRICAO = 37;
	public static final int EXTINCAO_ANISTIA = 38;
	public static final int CONCESSAO_LIVRAMENTO_CONDICIONAL = 39;
	public static final int EXTINCAO_OBITO = 40;
	public static final int PROGRESSAO_REGIME = 41;
	public static final int REGRESSAO_REGIME = 42;
	public static final int REVOGACAO_SUSPENSAO_CONDICIONAL_PENA = 43;
	public static final int CONCESSAO_SUSPENSAO_CONDICIONAL_PENA = 44;
	public static final int TRANSFERENCIA_EXECUCAO_PENA = 45;*/
	public static final int PENHORA_ONLINE = 46;
	public static final int AGUARDANDO_DECURSO_PRAZO = 47;
	public static final int AGUARDANDO_PRAZO_DECADENCIAL = 48;
	public static final int FINALIZAR_SUSPENSAO_PROCESSO = 49;
	public static final int MARCAR_AUDIENCIA = 50;
	public static final int REVELIA = 51;
	public static final int CONTUMACIA = 52;
	public static final int AGUARDANDO_CUMPRIMENTO_PENA = 53;
	public static final int ARQUIVAMENTO_PROVISORIO = 54;
	public static final int LIBERACAO_ACESSO = 55;
	public static final int INFORMATIVO = 56;
	public static final int PEDIDO_VISTA = 57;
	public static final int RELATORIO = 58;
	public static final int REVISAO = 59;
	public static final int VERIFICAR_DISTRIBUICAO = 60;
	public static final int PEDIDO_MANIFESTACAO = 61;
	public static final int VERIFICAR_PARECER =	62;
	public static final int PRE_ANALISE_PRECATORIA = 63;
	public static final int PEDIDO_LAUDO = 64;
	public static final int VERIFICAR_DOCUMENTO = 65;
	public static final int EFETIVAR_EVENTO = 66;
	public static final int CONCLUSO_EMENTA = 67;
	public static final int CONCLUSO_VOTO = 68;
	public static final int OFICIO_TURMA = 69;
	public static final int OFICIO_SEGUNDO_GRAU = 70;
	public static final int OFICIO_PRESIDENCIA = 71;
	public static final int VERIFICAR_GUIA = 72;
	public static final int CARTA_NOTIFICACAO = 73;
	public static final int VERIFICAR_CALCULO = 74;
	public static final int RENAJUD = 75;
	public static final int VERIFICAR_DEVOLUCAO_PRECATORIA = 76;
	public static final int VERIFICAR_NOVA_PRECATORIA = 77;
	public static final int CONCLUSO_GENERICO_SEGUNDO_GRAU = 78;
	public static final int VERIFICAR_PRECATORIA = 79;
	public static final int MARCAR_SESSAO_EXTRA_PAUTA = 83;
	public static final int TERMO = 84;
	public static final int VERIFICAR_RECURSO_REPETITIVO = 85;
	public static final int VERIFICAR_OFICIO_COMUNICATORIO = 86;
	public static final int OFICIO_COMUNICATORIO = 87;
	public static final int AGUARDANDO_DECURSO_PRAZO_PRISAO_CIVIL = 88;
	public static final int CONCLUSO_PRESIDENTE_TJGO = 89;
	public static final int CONCLUSO_VICE_PRESIDENTE_TJGO = 90;
	public static final int INTIMACAO_VIA_TELEFONE = 91;
	public static final int REQUISICAO_PEQUENO_VALOR = 92;
	public static final int VERIFICAR_GUIA_PAGA = 93;
	public static final int VERIFICAR_GUIA_PENDENTE = 94;
	public static final int ELABORACAO_VOTO = 95;
	public static final int MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC = 96;
	public static final int MARCAR_AUDIENCIA_MEDIACAO_CEJUSC = 97;
	public static final int MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT = 98;
	

	public static final int VERIFICAR_CLASSE_PROCESSUAL = 99;
	public static final int ALTERAR_VALOR_CAUSA = 101;
	public static final int ALTERAR_CLASSE_PROCESSUAL = 102;
	
	public static final int AGENDAMENTO_PERICIA = 103;	
		
	
	public static final int AVERBACAO_CUSTAS=108;
	public static final int PEDIDO_NATJUS_SEGUNDO_GRAU=109; 
	public static final int VERIFICAR_RESPOSTA_CAMARA_SAUDE=110;
	public static final int VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO=111;
	
	public static final int VERIFICAR_REDISTRIBUICAO=112;
	public static final int CONFIRMAR_DISTRIBUICAO=113;
	
	public static final int ENCAMINHAR_PROCESSO=114;
	
	public static final int VERIFICAR_TEMA_TRANSITADO_JULGADO = 115;
	
	public static final int TORNAR_SIGILOSO = 190;
	public static final int RETIRAR_SIGILO = 191;
	
	public static final int VERIFICAR_NOVO_PROCESSO_PEDIDO_ASSISTENCIA=192;
	public static final int CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA=193;
	
	public static final int PEDIDO_CENOPES=194;
	public static final int VERIFICAR_RESPOSTA_PEDIDO_CENOPES=195;
	
	public static final int FINALIZAR_SUSPENSAO_PROCESSO_ACORDO_OUTROS = 196;
	
	public static final int SOLICITACAO_CARGA = 197;
	
	public static final int VERIFICAR_IMPEDIMENTO = 198;
	public static final int VERIFICAR_IMPEDIMENTO_VOTANTES = 199;
	public static final int VOTO_SESSAO = 200;
	public static final int VERIFICAR_PEDIDO_SUSTENTACAO_ORAL = 201;
	public static final int PROCLAMACAO_VOTO = 202;
	public static final int SESSAO_CONHECIMENTO = 203;
	public static final int RETIRAR_PAUTA = 204;
	public static final int PEDIDO_VISTA_SESSAO = 205;
	public static final int ADIAR_JULGAMENTO = 206;
	public static final int RESULTADO_UNANIME = 207;
	public static final int PEDIDO_SUSTENTACAO_ORAL_DEFERIDO = 208;
	public static final int PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA = 209;
	public static final int VOTANTE_IMPEDIDO = 210;
	
	public static final int ENVIAR_PROCESSO_PRESIDENTE_UNIDADE = 211;
	public static final int RETORNAR_AUTOS_RELATOR_PROCESSO = 212;
	public static final int ALVARA_SOLTURA = 213;
	public static final int RESULTADO_VOTACAO = 214;
	public static final int ADIADO_PELO_RELATOR = 215;
	public static final int ATIVAR_PROCESSO_CALCULO = 216;
	
	public static final int VERIFICAR_PROCESSO_HIBRIDO = 217;
	
	// jvosantos - 04/06/2019 09:44 - Adicionar tipo de pendencia de Apreciados
	public static final int APRECIADOS = 218;
	public static final int VERIFICAR_IMPEDIMENTO_MP = 219;
	
	// jvosantos - 10/07/2019 14:56 - Adicionar tipo de pendencia para Verificar Resultado da Votação (após pendencia de renovar ou modificar voto ser finalizada)
	public static final int VERIFICAR_RESULTADO_VOTACAO = 221;
	
	// jvosantos - 08/01/2020 17:04 - Adicionar tipo de pendência para Verificar Erro Material
	public static final int VERIFICAR_ERRO_MATERIAL = 222;
	
	//novo tipo de pendencia (PROAD's de números 201808000121153 e 201902000155137)
	public static final int PEDIDO_JUSTICA_RESTAURATIVA = 225;
	//novo tipo de pendencia para CEJUSC
	public static final int VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC = 226;
	public static final int VERIFICAR_GUIA_VENCIDA = 300;
	
	//novo tipo de pendencia para upj família
	public static final int MARCAR_AUDIENCIA_AUTOMATICA = 116;
	
	public static final int INTIMACAO_AUDIENCIA = 301;
    public static final int CARTA_CITACAO_AUDIENCIA = 302;
    
    //novo tipo de pendencia para IA
    public static final int VERIFICAR_FATO = 303;
	
	public static final int VERIFICAR_RESPOSTA_OFICIO_DELEGACIA = 304;
	public static final int VERIFICAR_ENDERECO_PARTE = 305;
	public static final int VERIFICAR_RETORNO_AR_CORREIOS = 306;

	public static final int VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO = 307;
	
	public static final int ENCAMINHAR_PROCESSO_GABINETE = 351;


	//lrcampos 25/11/2019 15:20 Lista de pendencia para conhecimento para finalizar
	public static final Integer[] TIPOS_PENDENCIA_TOMAR_CONHECIMENTO = new Integer[] {
			PendenciaTipoDt.SESSAO_CONHECIMENTO,
			PendenciaTipoDt.RETIRAR_PAUTA,
			PendenciaTipoDt.PEDIDO_VISTA_SESSAO,
			PendenciaTipoDt.ADIAR_JULGAMENTO,
			PendenciaTipoDt.RESULTADO_UNANIME,
			PendenciaTipoDt.RESULTADO_VOTACAO,
			PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA

	};
	
	// jvosantos - 04/02/2020 11:02 - Lista de pendências do fluxo de Sustentação Oral
	public static final Integer[] TIPOS_PENDENCIA_SUSTENTACAO_ORAL = new Integer[] {
			PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO,
			PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA,
			PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL
	};
	
	/**
	 * Lista que quarda os códigos dos tipos de pendências referentes a conclusões
	 */
	public static final List Conclusoes;
	static {
		Conclusoes = new ArrayList();
		Conclusoes.add(new Integer(CONCLUSO_DECISAO));
		Conclusoes.add(new Integer(CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA));
		Conclusoes.add(new Integer(CONCLUSO_DESPACHO));
		Conclusoes.add(new Integer(CONCLUSO_SENTENCA));
		Conclusoes.add(new Integer(CONCLUSO_GENERICO));
		Conclusoes.add(new Integer(CONCLUSO_PRESIDENTE));
		Conclusoes.add(new Integer(CONCLUSO_RELATOR));
		Conclusoes.add(new Integer(CONCLUSO_DESPACHO_PRESIDENTE));
		Conclusoes.add(new Integer(CONCLUSO_DESPACHO_RELATOR));
		Conclusoes.add(new Integer(CONCLUSO_EMENTA));
		Conclusoes.add(new Integer(CONCLUSO_VOTO));
		Conclusoes.add(new Integer(CONCLUSO_GENERICO_SEGUNDO_GRAU));
		Conclusoes.add(new Integer(CONCLUSO_PRESIDENTE_TJGO));
		Conclusoes.add(new Integer(CONCLUSO_VICE_PRESIDENTE_TJGO));

	}
	
	public static boolean isConclusoPresidenteVicePresidente(PendenciaDt pendenciaDt){
		boolean retorno = false;
		
		if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().length() > 0
				&& (pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO)) 
						|| pendenciaDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO)))
			) { retorno = true;}
		
		return retorno;
	}
	
	
	//matriz que indica se uma pendencia pode ser realizada juntamente com a outra
	//o id da primeira pendencia indica a linha o id da segunda indica a coluna, se nesta for 1 pode ser realizada
	//conjuntamente
	// ex. if (byPode[1][5]==1) ...; else ...;
	public static byte[][] byPode= 
	{							
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//1-Carta de Citação
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//2-Intimação
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//3-Alvará
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//4-Mandado
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//5-Edital
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//6-Ofício
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//7-Carta Precatória
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//8-Carta de Adjudicação
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//9-Cumprimento Genérico
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//10-Verificar Novo Processo
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//11-Concluso - Decisão
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//12-Concluso - Despacho
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//13-Concluso - Sentença
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//14-Concluso - Genérico
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//15-Publicação
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//16-Verificar Petição
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,0,1,1,1,1,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//17-Verificar Processo
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1},	//18-Concluso-Decisão Presidente
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1},	//19-Concluso - Decisão Relator
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1},	//20-Concluso-Despacho Presidente
		{1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,1},	//21-Concluso - Despacho Relator
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0},	//22-Enviar para Turma Recursal
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1},	//23-Marcar Sessão
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0},	//24-Arquivamento
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,1,1,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,1,1,1},	//25-Desarquivamento
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1},	//26-Remarcar Sessão
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,1},	//27-Desmarcar Sessão
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//28-Retornar à Serventia de Origem
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//29-Ofício Delegacia
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//30-Pedido Cálculo
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//31-Verificar Prevenção
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,1,1,1},	//32-Suspensão de Processo
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//33-Calcular Liquidação Pena
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//34-Autorização de saída temporária
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//35-Autorização de trabalho externo
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//36-Ext. punibilidade-cump. Pena
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//37-Ext. Punibilidade-prescrição
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//38-Extinção da punibilidade-anistia
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//39-Concessão Livr. Condicional
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//40-Extinção da punibilidade-óbito
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//41-Concessão Prog. Regime
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//42-Determinação Regressão Regime
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//43-Revogação da susp. Cond. Pena
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//44-Concessão da sus. Cond. Da pena
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},	//45-Transferência execução pena
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//46-Penhora OnLine
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//47-Aguardando Decurso de Prazo
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//48-Aguardando Prazo Decadencial
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,0,1,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,1,1,1,1},	//49-Finalizar Suspensão de Processo
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//50-Marcar Audiência
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,0,0,0,0,1,0,1,1,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//51-Revelia
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,0,0,0,0,1,0,1,1,0,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//52-Contumácia
		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,0,1,1,1,1,1,0,1,0,1,1,1,0,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1},	//53-Aguardando Cumprimento de Pena
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,1,0},	//54-Arquivamento Provisório
	};
	
	public void copiar(PendenciaTipoDt objeto){
		if (objeto!=null){
			setId( objeto.getId());
			setPendenciaTipo( objeto.getPendenciaTipo());
			setPendenciaTipoCodigo( objeto.getPendenciaTipoCodigo());
			setId_ArquivoTipo( objeto.getId_ArquivoTipo());
			setArquivoTipo( objeto.getArquivoTipo());
			setCodigoTemp( objeto.getCodigoTemp());
		}
	}
	
	// jvosantos - 04/02/2020 11:00 - Método para indicar se pendência faz parte do fluxo de Sustentação Oral
	public static boolean isSustentacaoOral(PendenciaDt pendencia) {
		return isSustentacaoOral(pendencia.getPendenciaTipoCodigoToInt());
	}
	
	// jvosantos - 04/02/2020 11:00 - Método para indicar se pendência faz parte do fluxo de Sustentação Oral
	public static boolean isSustentacaoOral(int tipo) {
		return ArrayUtils.contains(TIPOS_PENDENCIA_SUSTENTACAO_ORAL, tipo);
	}
	
	public static boolean isCalculoContadoria(int tipoPendencia) {
		
		if (tipoPendencia == PendenciaTipoDt.PEDIDO_CONTADORIA || tipoPendencia == PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL || tipoPendencia == PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO || 
				tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO || tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS || 
				tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_CONTA || tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS){
			return true;
		}
		return false;
	}
}
