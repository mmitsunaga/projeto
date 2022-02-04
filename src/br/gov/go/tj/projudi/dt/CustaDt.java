package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class CustaDt extends CustaDtGen{

	private static final long serialVersionUID = -5349657872054293258L;
    public static final int CodigoPermissao=526;

    //*********************************
    //Id's das custas
    public static final int OFICIAL_JUSTICA								= 18;
    public static final int OUTROS_GRJ									= 21;
    public static final int PROCESSOS_PROCEDIMENTO_ORDINARIO 			= 28;
    public static final int PROCESSOS_ESPECIAL_CONTENCIOSA 				= 29;
    public static final int PROCESSOS_EXEC_SENTENCA_OU_TIT_EXTRA 		= 39; //Inclusive os executivos fiscais
    public static final int ARROLAMENTO_70_PORCENTO 					= 44;
    public static final int CUSTA_PARTIDOR 								= CustaDt.PARTILHA_OU_SOBREPARTILHA; //66;
    public static final int RATEIO_30_PORCENTO 							= CustaDt.PARTILHA_OU_SOBREPARTILHA; //67;
    public static final int CUSTAS 										= CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES; //68;
    public static final int CONTADOR 									= CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES; //69;
    public static final int RETIFICACAO_CONTA_CUSTAS 					= CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES; //70;
    public static final int ATUALIZACAO_MOEDA_NACIONAL 					= CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES; //71;
    public static final int CONVERSAO_MOEDA_NACIOMAL 					= CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES; //72;
    public static final int TAXA_PROTOCOLO 								= CustaDt.REGISTRO_DE_PETICAO_INICIAL; //75;
    public static final int PREGAO_AUDIENCIA 							= CustaDt.REGISTRO_DE_PETICAO_INICIAL; //76;
    public static final int AFIXACAO_EDITAL 							= CustaDt.REGISTRO_DE_PETICAO_INICIAL; //77;
    public static final int PREGAO_PRACAO_LEILAO 						= CustaDt.REGISTRO_DE_PETICAO_INICIAL; //78;
    public static final int CUSTA_PENHORA 								= 79;
    public static final int LOCOMOCAO_PARA_TRIBUNAL_SEGUNDO_GRAU 		= CustaDt.TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO;
    public static final int LOCOMOCAO_PARA_TRIBUNAL 					= CustaDt.PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO; //81;
    public static final int LOCOMOCAO_PARA_OFICIAL 						= 82;
    public static final int RECURSOS 									= CustaDt.RECURSO_INOMINADO_APLICA_SE_TABELA_II; //187;
    public static final int DESPESA_POSTAL_ANTIGO 						= 189;
    public static final int DESPESA_POSTAL 								= CustaDt.DESPESAS_POSTAIS_POR_POSTAGEM; //189;
    public static final int TAXA_JUDICIARIA_PROCESSO 					= 190;
    public static final int TAXA_JUDICIARIA_CERTIDAO 					= 191;
    public static final int PORTE_REMESSA 								= 192;
    public static final int LOCOMOCAO_PARA_AVALIACAO 					= 193;
    public static final int LOCOMOCAO_PARA_PENHORA 						= 194;
    public static final int DIVIDA_ATIVA_AJUIZADA 						= 196;
    public static final int RETIFICACAO_CONTA_CALCULO 					= CustaDt.PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES; //197;
    public static final int HONORARIOS_PROCURADOR 						= 198;
    public static final int CUSTA_GENERICA 								= 199;
    public static final int LOCOMOCAO_PARA_OFICIAL_ADHOC				= 200;
    public static final int TAXA_JUDICIARIA_SERVICO_CARTA_ARREMATACAO_SITE_TJGO_ITEM_5 	= 203;
    public static final int LOCOMOCAO_2_GRAU 							= 206;
    public static final int CUSTAS_LOCOMOCAO							= 207;
    public static final int TAXA_SERVICO_DIVIDA_ATIVA 					= 208;
    public static final int MEDIACAO 									= CustaDt.TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES; //209;
    public static final int CONCILIACAO 								= CustaDt.TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES; //210;
    public static final int CUSTA_GRS 									= 211;
    public static final int TAXA_JUDICIARIA_SERVICO_CERTIDAO_SITE_TJGO_ITEM_6 			= 300;
    public static final int PROTOCOLO_INTEGRADO_CONTADOR_SEGUNDO_GRAU 	= 301;
    
    
    //*********************************
    //Novos itens de custa: proad 29625
    public static final int RECURSOS_CIVEIS_ORIUNDOS_DO_PRIMEIRO_GRAU 											= 9001;
    public static final int ACOES_RESCISORIAS_E_DEMAIS_FEITOS_DA_COMPETENCIA 									= 9002;
    public static final int RECURSOS_PENAIS_ORIUNDOS_DO_PRIMEIRO_GRAU_DE_JURISDICAO 							= 9003;
    public static final int TAXAS_DE_SERVICO_CERTIDOES_DE_ACORDAO 												= 9004;
    public static final int TAXAS_DE_SERVICO_TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS 								= 9005;
    public static final int TAXAS_DE_SERVICO_RESTAURACAO_DE_AUTOS 												= 9006;
    public static final int TAXAS_DE_SERVICO_POR_DOCUMENTO_PUBLICADO_NO_DIARIO_DE_JUSTICA 						= 9007;
    public static final int TAXAS_DE_SERVICO_PORTE_E_REMESSA_DE_PROCESSOS_FISICOS 								= 9008;
    public static final int TAXAS_DE_SERVICO_DESPESAS_POSTAIS_POR_POSTAGEM 										= 9009;
    public static final int TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO 						= 9010;
    public static final int TAXAS_DE_SERVICO_PELA_EMISSAO_DOS_ATOS_DE_CONSTRICAO 								= 9011;
    public static final int PROCESSOS_DE_QUALQUER_CLASSE_ASSUNTO_NATUREZA_E_RITO 								= 9012;
    public static final int MANDADO_DE_SEGURANCA_CUMPRIMENTO_DE_PRECATORIAS 									= 9013;
    public static final int PROCESSOS_ESPECIAIS_DE_JURISDICAO_CONTENCIOSA 										= 9014;
    public static final int PROCESSOS_CAUTELARES_SERAO_COBRADOS_40_DAS_CUSTAS 									= 9015;
    public static final int AUTUACAO_E_OU_PROCESSAMENTO_DE_FEITOS 												= 9016;
    public static final int AUTO_DE_AVALIACAO_DE_BENS_EM_PROCESSO_DE_QUALQUER_NATUREZA 							= 9017;
    public static final int ATOS_DE_DISTRIBUICAO_DOS_PROCESSOS_FISICOS_APLICA_SE_10_SOBRE_VALOR_MINIMO_ITEM_5I 	= 9018;
    public static final int PARTILHA_OU_SOBREPARTILHA 															= 9019;
    public static final int PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES 							= 9020;
    public static final int PARA_REALIZACAO_E_CONFERENCIA_DE_CALCULOS_E_ATRIBUICOES_10_MINIMO_ITEM_5 			= 9021;
    public static final int DEPOSITO_COMPREENDENDO_OS_REGISTROS_GUARDA_ESCRITURACAO 							= 9022;
    public static final int REGISTRO_DE_PETICAO_INICIAL 														= 9023;
    public static final int CERTIDOES_DAS_DECISOES 																= 9024;
    public static final int TRASLADOS_DESARQUIVAMENTO_DOS_AUTOS_E_OUTRAS_CERTIDOES 								= 9025;
    public static final int RESTAURACAO_DE_AUTOS 																= 9026;
    public static final int POR_DOCUMENTO_PUBLICADO 															= 9027;
    public static final int PORTE_E_REMESSA_DE_PROCESSOS_FISICOS 												= 9028;
    public static final int DESPESAS_POSTAIS_POR_POSTAGEM 														= 9029;
    public static final int PELA_EMISSAO_DOS_DOCUMENTOS_DE_COMUNICACAO 											= 9030;
    public static final int PELA_EMISSAO_DOS_ATOS_DE_CONSTRICAO 												= 9031;
    public static final int FORMAL_DE_PARTILHA_CARTA_DE_SENTENCA 												= 9032;
    public static final int CUMPRIMENTO_DE_CARTAS_PRECATORIAS 													= 9033;
    public static final int RECURSO_INOMINADO_APLICA_SE_TABELA_II 												= 9034;
    public static final int RECURSO_INOMINADO_MAIS_4_DO_VALOR_DA_CAUSA 											= 9035;
    public static final int CONDENACAO_POR_LITIGANCIA_DE_MA_FE 													= 9036;
    public static final int RECURSO_INOMINADO_APLICA_SE_TABELA_III 												= 9037;
    public static final int CONDENACAO_EM_QUALQUER_TIPIFICACAO_CRIMINAL_APLICA_SE_TABELA_III 					= 9038;
    public static final int MANDADO_DE_SEGURANCA_EM_TURMA_RECURSAL 												= 9039;
    
    
    
    //*********************************
    //Códigos de Regimento
    public static final String CALCULO 									= "13";
    public static final String PROTOCOLO 								= "15";
    public static final String DISTRIBUIDOR 							= "11";
    public static final String REGIMENTO_TAXA_JUDICIARIA				= "2011";
    
    
    //*********************************
    //Tipos de Valores de Referência para o Cálculo 
    //::ATENÇÃO:: Caso alterar este valor, 
    //favor alterar o valor no campo da tabela de Custa chamado "ReferenciaCalculo"
	public static final int INDIFERENTE 					= 9999;
	public static final int VALOR_CAUSA 					= 9998;
	public static final int VALOR_BENS 						= 9997;
	public static final int MANDADOS 						= 9996;
	public static final int LOCOMOCAO 						= 9987;
	public static final int TAXA_JUDICIARIA 				= 9985;
	public static final int LOCOMOCAO_CONTA_VINCULADA 		= 9984;
	public static final int LOCOMOCAO_PENHORA 				= 9983;
	public static final int LOCOMOCAO_AVALIACAO 			= 9982;
	public static final int PROCESSO_PROCEDIMENTO_ORDINARIO = 9981;
	public static final int PARTIDOR_QUANTIDADE_VALOR 		= 8866;
	public static final int LEILAO_QUANTIDADE_VALOR 		= 8855;
	public static final int QUANTIDADE_ACRESCIMO_PESSOA 	= 8844;
	public static final int DESCONTO_TAXA_JUDICIARIA 		= 8833;
	public static final int DESCONTO_PEDIDO_JUIZ 			= 8822;
	public static final int GUIA_RECURSO_INOMINADO_QUEIXA_CRIMINE = 8811;
	public static final int GUIA_EXECUCAO_QUEIXA_CRIMINE 	= 8800;
	public static final int IS_LOCOMOCAO_CIVEL 				= 8799;
	public static final int ITEM_CONCILIACAO 				= 7001;
	public static final int ITEM_MEDIACAO 					= 7002;
	public static final int PORCENTAGEM_ESCRIVAO_CIVEL 		= 7000;
	public static final int PORTE_REMESSA_MANUAL 			= 7003;
	public static final int DOBRAR_VALOR_GUIA 				= 7004;
	
	//Constantes para códigos de regimento para custas
	public static final int REGIMENTO_LOCOMOCAO_CONTA_VINCULADA = 1058;
	public static final int REGIMENTO_LOCOMOCAO_TRIBUNAL        = 1074;
	public static final int REGIMENTO_LOCOMOCAO_PENHORA         = 1082;
	public static final int REGIMENTO_LOCOMOCAO_AVALIACAO       = 1084;
	public static final int REGIMENTO_POSTAGEM                  = 1198;
	
    private String CodigoArrecadacao;
    private String ReferenciaCalculo;
    private String valorAcrescimo;
    private String valorMaximo;
    private String Id_ArrecadacaoCustaGenerica;
    private OficialSPGDt oficialSPGDt;
    private BairroDt bairroLocomocao;
        
    @Override
	public void copiar(CustaDt objeto) {	
		super.copiar(objeto);
	    CodigoArrecadacao = objeto.getCodigoArrecadacao();
	    ReferenciaCalculo = objeto.getReferenciaCalculo();
	    valorAcrescimo = objeto.getValorAcrescimo();
	    valorMaximo = objeto.getValorMaximo();
	    Id_ArrecadacaoCustaGenerica = objeto.getId_ArrecadacaoCustaGenerica();
	    oficialSPGDt = objeto.getOficialSPGDt();
	    bairroLocomocao = objeto.getBairroLocomocao();	
	}
    
    public String getCodigoRegimentoTratamento() {
    	String retorno = null;
    	
    	if (this.getCodigoRegimento().equals("0")) {
    		retorno = "";
    	}
    	else {
    		retorno = "(Reg." + this.getCodigoRegimento() + ")";
    	}
    	
    	return retorno;
    }
    
    public String getCodigoArrecadacao() {
    	return this.CodigoArrecadacao;
    }
    public void setCodigoArrecadacao(String valor) {
    	this.CodigoArrecadacao = valor;
    }
    
    public String getId_ArrecadacaoCustaGenerica() {
    	return Id_ArrecadacaoCustaGenerica;
    }
	public void setId_ArrecadacaoCustaGenerica(String valor ) {
		if (valor!=null) 
			if (valor.equalsIgnoreCase("null")) {
				Id_ArrecadacaoCustaGenerica = "";
			}
			else if (!valor.equalsIgnoreCase("")) 
				Id_ArrecadacaoCustaGenerica = valor;
	}
    
    public String getReferenciaCalculo() {
    	return this.ReferenciaCalculo;
    }
    public void setReferenciaCalculo(String valor) {
    	this.ReferenciaCalculo = valor;
    }
    
	public String getValorAcrescimo() {
		return valorAcrescimo;
	}
	public void setValorAcrescimo(String valorAcrescimo) {
		this.valorAcrescimo = valorAcrescimo;
	}
	
	public String getValorMaximo() {
		return valorMaximo;
	}
	public void setValorMaximo(String valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public OficialSPGDt getOficialSPGDt() {
		return oficialSPGDt;
	}

	public void setOficialSPGDt(OficialSPGDt oficialSPGDt) {
		this.oficialSPGDt = oficialSPGDt;
	}
	
	public String getArrecadacaoCusta()  {
		if (this.oficialSPGDt == null || this.oficialSPGDt.getCodigoOficial() == null || this.oficialSPGDt.getCodigoOficial().trim().length() == 0) {
			return super.getArrecadacaoCusta();
		} else if(super.getArrecadacaoCusta() != null && !this.oficialSPGDt.getCodigoOficial().trim().equalsIgnoreCase(OficialSPGDt.CODIGO_OFICIAL_TRIBUNAL_JUSTICA)) {
		  	return super.getArrecadacaoCusta().trim() + " (" + this.oficialSPGDt.getCodigoOficial().trim() + "-" + Funcoes.primeiroNome(this.oficialSPGDt.getNomeOficial().trim()) + ")";
		} else if(super.getArrecadacaoCusta() != null) {
			return super.getArrecadacaoCusta().trim();
		}
		return "";
	}

	public BairroDt getBairroLocomocao() {
		return bairroLocomocao;
	}

	public void setBairroLocomocao(BairroDt bairroLocomocao) {
		this.bairroLocomocao = bairroLocomocao;
	}
	
	/**
	 * Método criado para a ocorrência 2019/6335
	 * Verifica se o item atual é o item de custas 18.I tabela II
	 *  
	 * @return boolean
	 */
	public boolean isCustaRecursoInominadoItem18ITabelaII() {
		if( this.getId() != null && this.getId().equals(String.valueOf(CustaDt.RECURSO_INOMINADO_APLICA_SE_TABELA_II)) ) {
			return true;
		}
		return false;
	}
	
}
