package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class MovimentacaoTipoDt extends MovimentacaoTipoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -48633342166839177L;

    public static final int CodigoPermissao = 181;
    
    private String Id_MovimentacaoTipo_CNJ;

	/**
	 * Constantes referentes a alguns tipos de movimentação que são utilizados pelo sistema
	 */

	public static final int AUTOS_CONCLUSOS = 21; // 100
	public static final int PETICAO_ENVIADA = 167; // 11015
	public static final int PROCESSO_ARQUIVADO = 347; // 20861
	public static final int PROCESSO_ARQUIVADO_PROVISORIAMENTE = 403; // 20245
	public static final int PROCESSO_DESARQUIVADO = 356; // 20893
	public static final int PROCESSO_DISTRIBUIDO = 168; // 11016
	public static final int PROCESSO_SUSPENSO = 214; // 13433
	public static final int TERMINO_SUSPENSAO_PROCESSO = 7; // 18
	public static final int RECURSO_AUTUADO = 8; // 19
	public static final int RECURSO_DISTRIBUIDO = 6; // 12
	public static final int AUTOS_DISTRIBUIDOS = 405; // 17015 Será utilizado para Câmaras Cíveis
	public static final int AUTOS_DEVOLVIDOS_TURMA_RECURSAL = 192; // 12211
	public static final int AUTOS_DEVOLVIDOS_SEGUNDO_GRAU = 417; // 17027
	public static final int TCO_ENVIADO = 251; // 17000
	public static final int GUIA_RECOLHIMENTO_INSERIDA = 400; // 17010
	public static final int PROCESSO_REDISTRIBUIDO = 14; // 27
	public static final int CONTESTACAO_APRESENTADA = 65; // 2667
	public static final int JUNTADA_DE_PETICAO = 260; // 20085
	public static final int CERTIDAO_EXPEDIDA = 222; // 14332
	
	//codigos para juizo de admissibilidade - recursos STF/STJ
	public static final int PROCESSO_SUSPENSO_RECURSO_ESPECIAL_REPETITIVO = 709; // 11975
	public static final int SUSPENSÃO_SOBRESTAMENTO = 697; // 20025
	public static final int DECISÃO_NEGADO_SEGUIMENTO_RECURSO = 280; // 20236
	public static final int PROCESSO_SUSPENSO_RECURSO_EXTRAORDINARIO_REPERCUSSAO_GERAL = 289; // 20265
	public static final int DECISAO_DESPACHO_HOMOLOGACAO = 309; // 20378
	public static final int RECURSO_ESPECIAL_ADMITIDO = 663; // 20430
	public static final int RECURSO_ESPECIAL_NAO_ADMITIDO = 665; // 20433
	public static final int TRANSITADO_JULGADO = 345; // 20848
	public static final int DECISAO_SUSPENSO_SOBRESTADO_DECISAO_JUDICIAL = 358; // 20898
	public static final int DESPACHO_DETERMINANDO_ARQUIVAMENTO = 389; // 21063
	public static final int RECURSO_ESPECIAL_REPETITIVO_NAO_ADMITIDO_CONSONANCIA = 763; // 40026
	public static final int RECURSO_ESPECIAL_REPETITIVO_ENCAMINHADO_JUIZO_RETRATACAO = 764; // 40027
	public static final int RECURSO_ESPECIAL_ADMITIDO_IRDR = 765; // 40028
	public static final int RECURSO_ESPECIAL_REPRESENTATIVO_CONTROVERSIA = 784; // 11975
	
	public static final int DESPACHO = 47; // 1594

	//Códigos para Citação
	public static final int CITACAO_EXPEDIDA = 197; // 12500
	public static final int CITACAO_EFETIVADA = 60; // 2287
	public static final int CITACAO_NAO_EFETIVADA = 176; // 11338;

	//Códigos para Intimação
	public static final int INTIMACAO_EXPEDIDA = 5; // 11
	public static final int INTIMACAO_REALIZADA_AUDIENCIA = 2; // 6	
	public static final int INTIMACAO_LIDA = 4; // 8
	public static final int INTIMACAO_EFETIVADA = 178; // 11347
	
	public static final int INTIMACAO_NAO_EFETIVADA = 177; // 11346
	public static final int INTIMACAO_VIA_TELEFONE_EFETIVADA = 667; // 11339
	public static final int INTIMACAO_VIA_TELEFONE_NAO_EFETIVADA = 669; // 11340
	public static final int INTIMACAO_EFETIVADA_VIA_DIARIO_ELETRONICO = 725; // 11341

	//Códigos para Ofício
	public static final int OFICIO_EXPEDIDO = 113; // 6585
	public static final int OFICIO_EFETIVADO = 399; // 17011
	public static final int OFICIO_EFETIVADO_EM_PARTE = 401; // 17012
	public static final int OFICIO_NAO_EFETIVADO = 402; // 17013
	
	//Códigos para Alvará
	public static final int ALVARA_EXPEDIDO = 27; // 430
	public static final int ALVARA_SOLTURA_EXPEDIDO = 786; // 431
	public static final int ALVARA_ENTREGUE = 390; // 17001
	public static final int ALVARA_SOLTURA_ENTREGUE = 789; // 432
	public static final int ALVARA_CANCELADO = 391; // 17002
	public static final int ALVARA_SOLTURA_CANCELADO = 790; // 433

	//Códigos para Mandado
	public static final int MANDADO_EXPEDIDO = 90; // 5306
	public static final int MANDADO_CUMPRIDO = 91; // 5322
	public static final int MANDADO_CUMPRIDO_EM_PARTE = 17; // 30
	public static final int MANDADO_NAO_CUMPRIDO = 18; // 31

	//Códigos para mandado de prisão 
	public static final int MANDADO_PRISAO_EXPEDIDO = 657; // 5307
	public static final int MANDADO_PRISAO_CUMPRIDO = 660; // 5323
	public static final int MANDADO_PRISAO_REVOGADO = 661; // 5308
	
	//Códigos para Carta Precatória
	public static final int CARTA_PRECATORIA_EXPEDIDA = 392; // 17003
	public static final int CARTA_PRECATORIA_CUMPRIDA = 393; // 17004
	public static final int CARTA_PRECATORIA_NAO_CUMPRIDA = 394; // 17005
	public static final int PRECATORIA_DEVOLVIDA = 57; // 2170
	
	//Códigos para Penhora On line
	public static final int PENHORA_REALIZADA = 212; // 13417
	public static final int PENHORA_REALIZADA_EM_PARTE = 406; // 17023
	public static final int PENHORA_NAO_REALIZADA = 213; // 13425

	//Códigos para outros tipos de pendência - Genérico
	public static final int DOCUMENTO_EXPEDIDO = 395; // 17006
	public static final int DOCUMENTO_CUMPRIDO = 396; // 17007
	public static final int DOCUMENTO_CUMPRIDO_EM_PARTE = 414; // 17024
	public static final int DOCUMENTO_NAO_CUMPRIDO = 397; // 17008
	
	//Requisicao Pequeno Valor
	public static final int REQUISICAO_PEQUENO_VALOR = 677; // 17031
	
	//Por enquanto esse genérico não está sendo utilizado
	public static final int DEVOLUCAO_SEM_LEITURA = 12; // 25

	//Mov.Equivalentes a Audiência 
	public static final int AUDIENCIA_CONCILIACAO_MARCADA = 35; // 851
	public static final int AUDIENCIA_CONCILIACAO_CEJUSC_MARCADA = 680; // 853
	public static final int AUDIENCIA_MEDIACAO_CEJUSC_MARCADA = 681; // 854
	public static final int AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT_MARCADA = 679; // 855
	public static final int AUDIENCIA_PRELIMINAR_MARCADA = 248; // 16000
	public static final int AUDIENCIA_PRELIMINAR_CONCILIADOR_MARCADA = 724; // 16003
	public static final int AUDIENCIA_CONCILIACAO_INSTRUCAO_MARCADA = 36; // 852
	public static final int AUDIENCIA_INSTRUCAO_JULGAMENTO_MARCADA = 37; // 869
	public static final int AUDIENCIA_INSTRUCAO_MARCADA = 398; // 17009
	public static final int AUDIENCIA_SESSAO_MARCADA = 319; // 20417
	public static final int AUDIENCIA_SESSAO_RETIRADA_PAUTA = 357; // 20897
	public static final int AUDIENCIA_SESSAO_DESMARCADA_PAUTA = 760; // 20991
	public static final int AUDIENCIA_ADMONITORIA_MARCADA = 407; // 17016
	public static final int AUDIENCIA_INTERROGATORIO_MARCADA = 408; // 17017
	public static final int AUDIENCIA_JUSTIFICACAO_MARCADA = 409; // 17018
	public static final int AUDIENCIA_EXECUCAO_MARCADA = 410; // 17019
	public static final int AUDIENCIA_INICIAL_MARCADA = 411; // 17020
	public static final int AUDIENCIA_JULGAMENTO_MARCADA = 412; // 17021
	public static final int AUDIENCIA_SINE_DIE_MARCADA = 413; // 17022
	public static final int AUDIENCIA_INQUIRICAO_MARCADA = 441; // 17028
	public static final int AUDIENCIA_SUSPENSAO_CONDICIONAL_MARCADA = 442; // 17029
	public static final int AUDIENCIA_MARCADA = 674; // 17030
	public static final int AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC = 683; // 17032
	public static final int AUDIENCIA_ENVIAR_PARA_CONCILIACAO_CEJUSC_DPVAT = 678; // 17035
	public static final int AUDIENCIA_ENVIAR_PARA_MEDIACAO_CEJUSC = 684; // 17033
	public static final int AUDIENCIA_RETORNAR_DA_CONCILIACAO_OU_MEDIACAO_CEJUSC = 685; // 17034
	public static final int AUDIENCIA_CUSTODIA = 942; // 12742
	public static final int AUDIENCIA_ART334 = 862; // 12624

	//Códigos referentes a movimentação de Audiência - Escrivão Id_mov 970
	public static final int AUDIENCIA_COM_CONCILIACAO = 227; // 14738
	public static final int AUDIENCIA_DESMARCADA = 209; // 13094
	public static final int AUDIENCIA_NEGATIVA = 215; // 13664
	public static final int AUDIENCIA_CONCILIACAO_REALIZADA = 39; // 943
	
	public static final int AUDIENCIA_REALIZADA_SEM_ACORDO = 740; // 945
	public static final int AUDIENCIA_REALIZADA_COM_ACORDO = 741; // 946
	
	public static final int AUDIENCIA_REMARCADA = 196; // 12476
	public static final int AUDIENCIA_COM_SENTENCA_COM_MERITO = 245; // 15603
	public static final int AUDIENCIA_COM_SENTENCA_SEM_MERITO = 247; // 15605
	public static final int AUDIENCIA_SEM_SENTENCA = 249; // 16001
	public static final int AUDIENCIA_SENTENCA_HOMOLOGACAO = 250; // 16002	

	//Códigos referentes a movimentação de Sessão de Processo
	public static final int CONHECIDO_PROVIDO = 281; // 20237
	public static final int CONHECIDO_PROVIDO_MONOCRATICO = 425; // 20972		CNJ 972
	public static final int CONHECIDO_PROVIDO_EM_PARTE = 282; // 20238
	public static final int CONHECIDO_NAO_PROVIDO = 283; // 20239
	public static final int CONHECIDO_EM_PARTE_PROVIDO = 284; // 20240
	public static final int CONHECIDO_EM_PARTE_PROVIDO_EM_PARTE = 285; // 20241
	public static final int CONHECIDO_EM_PARTE_NAO_PROVIDO = 286; // 20242
	public static final int NAO_CONHECIDO = 157; // 9747
		
	public static final int SEGURANCA_CONCEDIDA = 422; // 10210						CNJ 442
	public static final int SEGURANCA_DENEGADA = 423; // 10212						CNJ 446
	public static final int SEGURANCA_CONCEDIDA_EM_PARTE = 424; // 10214			CNJ 450
	
	public static final int EMBARGOS_ACOLHIDOS = 273; // 20198						CNJ 198
	public static final int EMBARGOS_ACOLHIDOS_EM_PARTE = 352; // 20871				CNJ 871
	public static final int EMBARGOS_NAO_ACOLHIDOS = 274; // 20200					CNJ 200
	public static final int RECURSO_PREJUDICADO = 278; // 20230						CNJ 230
	public static final int PETICAO_INDEFERIDA = 322; // 20454						CNJ 454
	
	public static final int EXTINTO_AUSENCIA_PRESSUPOSTOS = 327; // 20459			CNJ 459
	public static final int EXTINTO_AUSENCIA_CONDICOES = 329; // 20461				CNJ 461
	public static final int EXTINTO_DESISTENCIA = 331; // 20463						CNJ 463
	
	public static final int HOMOLOGADO_DESISTENCIA = 363; // 20944

	public static final int PEDIDO_VISTA = 404; // 17014
	
	public static final int ARGUICAO_INCONSTITUCIONALIDADE_ACOLHIDA = 437; // 5001
	public static final int ARGUICAO_INCONSTITUCIONALIDADE_NAO_ACOLHIDA = 438; // 5002
	
	public static final int CAUTELAR_DEFERIDA = 729; // 40014
	public static final int CAUTELAR_INDEFERIDA = 726; // 40015	
	public static final int EXTINTO_SEM_RESOLUCAO_DO_MERITO = 727; // 40016
	
	//Movimentação Contadoria
	public static final int REALIZADO_CALCULO_DE_CUSTAS = 336; // 20479
	public static final int REALIZADO_CALCULO_DE_LIQUIDACAO = 335;
	public static final int REALIZADO_CALCULO_DE_ATUALIZACAO_CONTA = 337;
	public static final int REALIZADO_CALCULO_DE_TRIBUTOS = 551;
	
	//Movimentação referente à publicação de audiências gravadas em video
	public static final int AUDIENCIA_PUBLICADA = 426; // 19000
	
	//Movimentação referente à publicação de mídias gravadas em áudio / vídeo
	public static final int MIDIA_PUBLICADA = 444; // 19000
	
	//Código referentes a movimentação de Mudança de Classe no Processo
	public static final int MUDANCA_CLASSE_PROCESSO = 419; // 10966
	
	//Código referentes a movimentação de Mudança de Assuntos no Processo
	public static final int MUDANCA_ASSUNTO_PROCESSO = 12143; // 12143
	
	//Códigos para Guias
	public static final int GUIA_RECOLHIDA = 420; // 10967
	
	//Código para Troca de Responsável
	public static final int TROCAR_RESPONSAVEL_PROCESSO = 427; // 40000
	
	//Código para Desabilitação de Responsável
	public static final int DESABILITAR_RESPONSAVEL_PROCESSO = 747; // 311383
	
	//Código para Troca de Responsável
	public static final int RECURSO_CONVERTIDO_EM_PROCESSO = 428; // 40001
	public static final int PROCESSO_CONVERTIDO_EM_RECURSO = 429; // 40002
	
	//Código para Troca de Redator
	public static final int TROCAR_REDATOR_PROCESSO = 430; // 40006
	
	//Mov.Equivalentes a Sessão	
	public static final int SESSAO_JULGAMENTO_INICIADO = 432; // 40003
	public static final int SESSAO_JULGAMENTO_ADIADO = 431; // 40004
	public static final int SESSAO_ATA_JULGAMENTO_INSERIDA = 433; // 40005
	public static final int SESSAO_JULGAMENTO_MANTIDO_ADIADO = 434; // 40007
	public static final int SESSAO_EM_MESA_JULGAMENTO = 435; // 40008
	public static final int SESSAO_ATA_JULGAMENTO_INSERIDA_CORRECAO = 436; // 40009
	public static final int SESSAO_JULGAMENTO_INICIADO_CORRECAO = 439; // 40010
	public static final int SESSAO_JULGAMENTO_ADIADO_CORRECAO = 440; // 40011
	public static final int SESSAO_RETIRAR_ACORDAO_EMENTA_EXTRATO_ATA = 730; // 40017
	public static final int SESSAO_RETIRAR_EXTRATO_ATA = 731; // 40018
	public static final int SESSAO_RETORNAR_PROCESSO_SESSAO_JULGAMENTO = 734; // 40019
	public static final int SESSAO_JULGAMENTO_ADIADO_PEDIDO_SO = 40035;
	public static final int JUIZO_DE_RETRATACAO_EFETIVADO = 803; //CNJ 12258
	public static final int JUIZO_DE_RETRATACAO_NAO_EFETIVADO = 804; //CNJ 12258
	public static final int JUIZO_DE_RETRATACAO_EFETIVADO_PARCIALMENTE = 805; //CNJ 12258
	
	public static final int JUNTADA_DE_DOCUMENTO = 339; // 20581
	
	public static final int COMPETENCIA_DECLINADA = 63; // 2428
	
	public static final int PROCESSO_SUSPENSO_DEPENDENCIA_JULGAMENTO_OUTRA_CAUSA = 443; // 40012
	
	//Código para Inversão de Pólos
	public static final int INVERTER_POLOS = 670; // 40013
	
	//Encaminhar processo para outra serventia
	public static final int CERTIDAO_ENCAMINHAMENTO_PROCESSO = 696; // 40020
	public static final int PROCESSO_ENCAMINHADO = 743; // 40021
	public static final int PROCESSO_DEVOLVIDO = 745; // 40022
	
	public static final int REMESSA = 265; // 20123
	
	public static final int NAO_ADMITIDO_IRDR = 751; // 312095                           CNJ 12095
	public static final int ADMITIDO_IRDR = 750; // 312094                               CNJ 12094
	
	public static final int NAO_ADMITIDO_IAC = 757; // 312097                            CNJ 12095
	public static final int ADMITIDO_IAC = 756; // 312096                                CNJ 12094
	
	public static final int CONCESSAO = 776; // 312098                                   CNJ 210
	public static final int NAO_CONCESSAO = 777; // 312099                               CNJ 210
	
	public static final int EXCECAO_IMPEDIMENTO_SUSPEICAO_REJEITADO = 782; // 55    Exceção de Impedimento ou Suspeição Rejeitado - CNJ 373
	public static final int EXCECAO_IMPEDIMENTO_SUSPEICAO_ACOLHIDO = 783; // 56     Exceção de Impedimento ou Suspeição Acolhido - CNJ 940
	
	public static final int PROCESSO_TORNADO_SIGILOSO = 758; // 40023
	
	//Código para Habilitação de Responsável
	public static final int HABILITAR_RESPONSAVEL_PROCESSO = 746; // 40024
	
	public static final int PROCESSO_RETIRADO_SIGILO = 759; // 40025
	
	public static final int DOCUMENTO_ORIUNDO_DELEGACIA = 762; // 40033	
	public static final int RECEBIDO = 373; // 20981
	
	
	//codigos para realização de carga e devolucao dos autos
	public static final int ENTREGA_EM_CARGA_VISTA = 780; // 300493
	public static final int DEVOLVIDOS_OS_AUTOS = 781; // 312315
	
	//complementos padrão
	public static final String COMPLEMENTO_JUNTADA_DOCUMENTO_PROCESSO_FISICO = "Histórico Processo Físico";
	public static final String COMPLEMENTO_CONVERTER_HIBRIDO_DIGITAL = "Converter Híbrido para Processo Digital";
	public static final String COMPLEMENTO_RETORNAR_PARA_HIBRIDO = "Retornar Processo para Híbrido";
	public static final String COMPLEMENTO_ADICIONAR_PECAS_HIBRIDO_DIGITAL = "Adicionar Peças na Conversão de Processo Híbrido para Processo Digital";
	public static final String COMPLEMENTO_MIDIA_PUBLICADA = "Envio de Mídia Gravada em %s";
	
	public static final int RECURSO_EXCLUIDO = 791; // 312316
	public static final int RECURSO_INSERIDO = 792; // 312317
	
	//movimentação para processos hibridos
	public static final int AGUARDANDO_DIGITALIZACAO_PARA_DISTRIBUICAO_2G = 794; // 40067
	
	public static final int PROCESSO_CALCULO_ATIVADO = 795; // 40068
	
	// jvosantos - 09/07/2019 13:52 - Mapear tipo de movimentação "Decisão" para usar na inserção do extrato de ata
	public static final int DECISAO = 241; // 15396
	
	public static final int TROCA_NUMERO_PROCESSO = 748;
	
	public static final int ARQUIVAMENTO_REQUERIDO = 28;
	
	public static final int MOVI_TIPO_CNJ_PAI_SENTENCA = 193;
	
	public MovimentacaoTipoDt() {

		limpar();

	}

	public String getId_CNJ()  {return Id_MovimentacaoTipo_CNJ;}
	public void setId_CNJ(String valor ) {if(valor!=null) Id_MovimentacaoTipo_CNJ = valor;}	

	public void copiar(MovimentacaoTipoDt objeto){
		Id_MovimentacaoTipo = objeto.getId();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		MovimentacaoTipoCodigo = objeto.getMovimentacaoTipoCodigo();
        Id_MovimentacaoTipo_CNJ = objeto.getId_CNJ();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MovimentacaoTipo="";
		MovimentacaoTipo="";
		MovimentacaoTipoCodigo="";
		Id_MovimentacaoTipo_CNJ = "";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MovimentacaoTipo:" + Id_MovimentacaoTipo + ";MovimentacaoTipo:" + MovimentacaoTipo + ";MovimentacaoTipoCodigo:" + MovimentacaoTipoCodigo + ";Id_MovimentacaoTipo_CNJ:" + Id_MovimentacaoTipo_CNJ + ";CodigoTemp:" + CodigoTemp + "]";
	}

}
