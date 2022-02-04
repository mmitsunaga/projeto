package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class ProcessoTipoDt extends ProcessoTipoDtGen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7741619953767984790L;

	public static final int CodigoPermissao = 183;

	// Constante para ser usada como ausência de tipo de ação
	public static final int ALVARA_JUDICIAL = 1074;
	public static final int HOMOLOGACAO_ACORDO = 1112;
	public static final int APELACAO_CIVEL = 1198;
	public static final int TERMO_CIRCUNSTANCIADO_OCORRENCIA_LEI_ESPARSA = 1278;
	public static final int TERMO_CIRCUNSTANCIADO_OCORRENCIA = 424;
	public static final int APELACAO_CRIMINAL = 1417;
	public static final int RECURSO_INOMINADO = 1460;
	public static final int REEXAME_NECESSARIO = 10199;
	
	public static final int RECURSO_ESPECIAL = 10213;
	
	// Constantes relativas a alguns tipos de ação que são levadas em consideração em alguns fluxos de execução
	public static final int EXECUCAO = 21;
	public static final int EXECUCAO_ALIMENTOS = 21112;
	public static final int EXECUCAO_ACORDO = 1156;
	public static final int EXECUCAO_SENTENCA = 1156;
	public static final int EXECUCAO_FORCADA = 1156;
	public static final int EXECUCAO_PENA_MULTA = 1156;
	public static final int EXECUCAO_PROVISORIA = 1157;
	public static final int EXECUCAO_EXTRAJUDICIAL = 1159;
	public static final int ACAO_MONITORIA = 1156;
	public static final int EXECUCAO_HIPOTECARIA = 2117;
	
	//TODO Fred: Validar!
	public static final int EXECUCAO_FISCAL = 21116;
	public static final int EMBARGOS_EXECUCAO = 103;

	public static final int SEPARACAO_CONSENSUAL = 20060;
	public static final int CONVERSAO_SEPARACAO_EM_DIVORCIO = 20087;
	public static final int DIVORCIO_CONSENSUAL = 20098;
	
	public static final int RECONHECIMENTO_EXTINCAO_UNIAO_ESTAVEL = 32763;
	
	public static final int EXECUCAO_DA_PENA = 20386;
	public static final int CONFLITO_COMPETENCIA_PRIMEIRO_GRAU = 30970;
	public static final int AGRAVO_EXECUCAO_PENAL = 20413;
	public static final int INCIDENTE_SANIDADE_MENTAL = 30960;
	public static final int OUTROS_INCIDENTES_EXECUCAO_INCICIADOS_OFICIO = 32129;
	public static final int OUTROS_INCIDENTES_EXECUCAO_INCICIADOS_OFICIO_2 = 12129;
	public static final int ROTEIRO_PENA = 32123;
	public static final int TRANSFERENCIA_ENTRE_ESTABELECIMENTOS_PENAIS = 2288;
	
	
	public static final int RETIFICACAO_REGISTRO = 21682;
	public static final int DIVORCIO_LITIGIOSO_LE = 1141;
	
	public static final int MANDADO_SEGURANCA_COLETIVO = 1119;
	public static final int MANDADO_SEGURANCA_8069 = 2691;
	public static final int MANDADO_SEGURANCA_1531 = 2710;
	public static final int MANDADO_SEGURANCA_CIVEL = 20120;
	public static final int MANDADO_SEGURANCA_420 = 420;
	public static final int MANDADO_SEGURANCA_101 = 101;
	
	public static final int ALIMENTOS_LEI_ESPECIAL = 20069;
	public static final int ACAO_DE_ALIMENTOS = 2389;
	
	//Requerimento de apreensão
	public static final int REQUERIMENTO_APREENSAO_VEICULO = 42137;
		
	//Carta Precatória
	public static final int CARTA_PRECATORIA = 107;
	public static final int CARTA_PRECATORIA_CPC = 1261;
	public static final int CARTA_PRECATORIA_CPP = 1355;
	public static final int CARTA_PRECATORIA_INFRACIONAL = 21478;
	public static final int CARTA_PRECATORIA_INFANCIA_JUVENTUDE = 21455;
	
	//A pedido da Fatinha da corregedoria e ocorrência 2013/76036 ou redmine #1759
	public static final int REPRESENTACAO_CRIMINAL_CPP = 1272;
	
	//Códigos para locomoção em dobro
	public static final int ARRESTO = 5;
	public static final int ARRESTO_CPC = 1178;
	public static final int ARRESTO_HIPOTECA_LEGAL = 1330;
	public static final int DESPEJO_FALTA_PAGAMENTO = 14;
	public static final int DESPEJO_FALTA_PAGAMENTO_LE = 1093;
	public static final int DESPEJO_FALTA_PAGAMENTO_CUMULADO_LE = 1094;
	public static final int DESPEJO = 115;
	public static final int DESPEJO_PEDIDO_LIMINAR = 160;
	public static final int DESPEJO_LE = 1092;
	public static final int IMISSAO_DE_POSSE = 25;
	public static final int IMISSAO_NA_POSSE_LE = 1113;
	public static final int REINTEGRACAO_DE_POSSE = 37;
	public static final int REINTEGRACAO_MANUTENCAO_DE_POSSE_CPC = 2707;
	public static final int SEQUESTRO_CPC = 1196;
	public static final int SEQUESTRO_CPP = 1329;		
	
	//tabela de 30% das custas do item 17
	public static final int ANULACAO_SUBSTITUICAO_TITULOS_PORTADOR = 1028;
	public static final int BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA = 1081;
	public static final int BUSCA_APREENSAO_CPC = 1181;
	public static final int BUSCA_APREENSAO = 2438;
	public static final int BUSCA_APREENSAO_CRIMINAL = 1309;
	public static final int BUSCA_APREENSAO_6 = 6;
	public static final int BUSCA_APREENSAO_32072 = 32072;
	public static final int CONSIGNATORIA = 8;
	public static final int CONSIGNATORIA_PAGAMENTO = 1032;
	public static final int CONSIGNATORIA_ALUGUEIS = 1086;
	public static final int DEPOSITO = 142;
	public static final int DEPOSITO_CPC = 1035;
	public static final int DEPOSITO_LEI_866_94_LE = 1089;
	public static final int DEMOLITORIA = 12;
	public static final int DESAPROPRIACAO = 1090;
	public static final int EMBARGOS_ADJUDICACAO = 1170;
	public static final int EMBARGOS_ARREMATACAO = 1171;
	public static final int EMBARGOS_RETENCAO = 1173;
	public static final int EMBARGOS_TERCEIRO = 19;
	public static final int EMBARGOS_TERCEIRO_CPC = 1037;
	public static final int EMBARGOS_TERCEIRO_CPP = 1327;
	public static final int EMBARGOS_DECLARACAO = 2689;
	public static final int HABILITACAO_CREDITO_CONCORDATA = 1111;
	public static final int RECUPERACAO_JUDICIAL = 1128;
	public static final int HABILITACAO_INCIDENTAL = 162;
	public static final int PRESTACAO_CONTAS_OFERECIDAS_CPC = 1044;
	public static final int PRESTACAO_CONTAS_EXIGIDAS = 1045;
	public static final int PRESTACAO_CONTAS = 2425;
	public static final int RESTAURACAO_AUTOS_CPC = 1046;
	public static final int RESTAURACAO_AUTOS_CPP = 1291;
	public static final int INTERDITO_PROIBITIVO = 2709;
	public static final int NUNCIACAO_OBRA_NOVA = 1041;
	public static final int SOBREPARTILHA = 1048;
	public static final int USUCAPIAO = 1049;
	
	//Processos Cautelares
	public static final int ATENTADO = 1180;
	public static final int CAUCAO = 1182;
	public static final int CAUTELAR_INOMINADA_CPC = 1183;
	public static final int CAUTELAR_INOMINADA = 2440;
	public static final int SEPARACAO_CORPOS = 1195;
	public static final int EXIBICAO_DOCUMENTOS = 1228;
	public static final int PRODUCAO_ANTECIPADA_PROVAS = 1193;
	public static final int PERDA_SUSPENCAO = 2426;
	public static final int SUSTACAO_PROTESTO = 46;
	public static final int TUTELA_CAUTELAR_ANTECEDENTE_32134 = 32134;
	public static final int TUTELA_CAUTELAR_ANTECEDENTE_32084 = 32084;
	
	//Processos de natureza não contenciosa
	public static final int ABERTURA_REGISTRO_CUMPRIMENTO_TESTAMENTO = 1051;
	public static final int ALIENACAO_JUDICIAL_BENS = 1052;
	public static final int ALVARA_JUDICIAL_CPC = 2295;
	public static final int ARRECADACAO_COISAS_VAGAS = 1053;
	public static final int CONFIRMACAO_TESTAMENTO = 1054;
	public static final int DECLARACAO_AUSENCIA = 1055;
	public static final int ESPECIALIZACAO_HIPOTECA_LEGAL = 1056;
	public static final int HERANCA_JACENTE = 1057;
	public static final int INTERDICAO = 1058;
	public static final int ORGANIZACAO_FISCALIZACAO_FUNDACAO = 1059;
	public static final int OUTROS_PROCEDIMENTOS_JURISDICAO_VOLUNTARIA = 2294;
	//SEPARACAO_CONSENSUAL também é processo de natureza não contenciosa, mas já está declarado nesta classe algumas linhas acima
	public static final int TUTELA_CURATELA_NOMEACAO = 1061;
	public static final int TUTELA_CURATELA_REMOCAO_DISPENSA = 2122;
	public static final int MONITORIA_CPC = 1040;
	//fim dos processo de natureza não contenciosa
	
	public static final int CALCULO_DE_LIQUIDACAO_DE_PENA = 1020386;
	public static final int ID_CALCULO_DE_LIQUIDACAO_DE_PENA = 382;
	
	//Processos de natureza/classe Agravo de Instrumento
	public static final int AGRAVO_INSTRUMENTO_CPC = 1202;	
	
	
	//mrbatista 15/06/2020 - Inclusão de classes de Agravo que não estavam mapeadas
	public static final int AGRAVO_INSTRUMENTO_RECURSO_EXTRAORDINARIO_CPC = 1204;
	public static final int AGRAVO_INSTRUMENTO_RECURSO_ESPECIAL_CPC = 1205;
	public static final int AGRAVO_INSTRUMENTO_RECURSO_ESPECIAL = 21711;
	public static final int AGRAVO_INSTRUMENTO_RECURSO_EXTRAORDINARIO = 21712;

	// PROCESSO IMPORTADO DO SPG SEM CLASSE CNJ
	public static final int CLASSE_CNJ_INDEFINIDA = 99999;
	
	//Carta Ordem
	public static final int CARTA_ORDEM = 291;
	public static final int CARTA_ORDEM_CPC = 1258;
	public static final int CARTA_ORDEM_CPP = 1335;
	public static final int CARTA_ORDEM_INFRACIONAL = 2474;
	public static final int CARTA_ORDEM_INFANCIA_JUVENTUDE = 2451;
	
	public static final int APREENSAO_TITULO = 1177;
	public static final int APREENSAO_DEPOSITO_VENDA_COM_RESERVA_DOMINIO = 1029;
	public static final int POSSE_EM_NOME_NASCITURO = 1192;
	
	public static final int HABEAS_CORPUS = 20307;
	public static final int HABEAS_CORPUS_CF_LIVRO_III = 2269;
	public static final int HABEAS_CORPUS_CPP = 1307;
	public static final int HABEAS_DATA = 1110;
	
	// estatistica movimentacao NUGEP
	public static final int AGRAVO_STJ = 38091;
	public static final int AGRAVO_INTERNO_DECISOES_APLICACAO_STJ = 38093;
	public static final int AGRAVO_REGIMENTAL = 20206;
	public static final int AGRAVO_INTERNO = 21208;

	
	//Mapeado devido ter custa no 2 Grau, mas não ter custa quando cadastrado por magistrado no 1 Grau
	public static final int CONFLITO_COMPETENCIA = 10221;
	
	public static final int ID_CALCULO_LIQUIDACAO_PENA = 382;
	
	public static final int RECLAMACAO_PRE_PROCESSUAL = 31875;
	public static final int PEDIDO_MEDIACAO_PRE_PROCESSUAL = 12136;
	public static final int HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL = 1112;
	
	public static final int REGULACAO_AVARIA_GROSSA = 32376;
	
	public static final int REQUERIMENTO_REINTEGRACAO_POSSE = 32138;
	
	
	public static final int ID_TCO = 128;
	public static final int ID_IP  = 129;
	public static final int ID_APF = 130;
	public static final int ID_AI  = 346;
	public static final int ID_AAF = 347;
	public static final int ID_BOC = 349;
	
	/**
	 * Lista que quarda os códigos dos tipos de ação referentes a processos de execução
	 */
	public static final List ACOES_EXECUCAO;
	static {
		ACOES_EXECUCAO = new ArrayList();
		ACOES_EXECUCAO.add(new Integer(EXECUCAO));//EXECUÇÃO
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_ALIMENTOS));//EXECUÇÃO DE ALIMENTOS
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_ACORDO));//EXECUÇÃO DE ACORDO
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_SENTENCA));//EXECUÇÃO DE SENTENÇA		
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_FORCADA));//EXECUÇÃO FORÇADA
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_PENA_MULTA));//EXECUÇÃO PENA MULTA
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_PROVISORIA));//EXECUÇÃO PROVISORIA
		ACOES_EXECUCAO.add(new Integer(EXECUCAO_EXTRAJUDICIAL));

	}

	private String CnjCodigo="";
	
	public boolean isHomologacaoAcordo(){
		if ((Funcoes.StringToInt(getProcessoTipoCodigo())) == HOMOLOGACAO_ACORDO){
			return true;
		}
		
		return false;
	}
	
	public boolean isHabeasCorpus(){
		if ((Funcoes.StringToInt(getProcessoTipoCodigo())) == HABEAS_CORPUS
			|| 	(Funcoes.StringToInt(getProcessoTipoCodigo())) == HABEAS_CORPUS_CF_LIVRO_III
			|| 	(Funcoes.StringToInt(getProcessoTipoCodigo())) == HABEAS_CORPUS_CPP){
			return true;
		}
		return false;
	}
	
	public boolean isExecucaoPenal() {
		if ((Funcoes.StringToInt(getProcessoTipoCodigo())) == EXECUCAO_DA_PENA){
			return true;
		}
		return false;
	}
	
	public boolean isConflitoCompetencia2Grau(){
		if (Funcoes.StringToInt(getProcessoTipoCodigo()) == CONFLITO_COMPETENCIA){
			return true;
		}
		return false;
	}
	
	public void setCnjCodigo(String codigo){
		if (codigo!=null) CnjCodigo = codigo;
	}
	
	public String getCnjCodigo(){
		return CnjCodigo;
	}
	
	public void copiar(ProcessoTipoDt objeto){
		super.copiar(objeto);
		CnjCodigo = objeto.getCnjCodigo();
	}

	public void limpar(){
		super.limpar();
		CnjCodigo = "";
	}
}
