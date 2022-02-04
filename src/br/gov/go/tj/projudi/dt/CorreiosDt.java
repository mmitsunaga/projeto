package br.gov.go.tj.projudi.dt;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CorreiosDt extends Dados{

	private static final long serialVersionUID = -20999876054562608L;
	
	private String Id;
	private String TipoRegistro = "1";						//( 1) 	MAX(1) 		
	private String Id_Pendencia;							//( 2) 	MAX(18) 	CodigoObjetoCliente - Número da carta dentro do lote
	private String NumeroLote;								//( 3) 	MAX(5)		
	private String CartaoPostagem = CARTAO_POSTAGEM;		//( 4)	MAX(10) 	
	private String NumeroContrato = NUMERO_CONTRATO;		//( 5)	MAX(10) 	
	private boolean ServicoAdicional;						//( 6)	MAX(3) 		Código do serviço adicional de "Mão Própria". 002/""
	private String IdentificadorArquivoSpool = "N";			//( 7)	MAX(1) 		S/N
	private String NomeArquivoSpool;						//( 8)	MAX(4000) 	
	private String IdentificadorArquivoComplementar = "S";	//( 9)	MAX(1) 		S/N
	private String NomeArquivoComplementar;					//(10)	MAX(4000)	
	private String NomeDestinatario;						//(11)	MAX(250)	
	private String EnderecoDestinatario;					//(12)	MAX(226)
	private String NumeroEnderecoDestinatario;				//(13)	MAX(36)
	private String ComplementoEnderecoDestinatario;			//(14)	MAX(36)
	private String BairroDestinatario;						//(15)	MAX(72)
	private String CidadeDestinatario;						//(16)	MAX(72)
	private String UfDestinatario;							//(17)	MAX(2)
	private String CepDestinatario;							//(18)	MAX(8)
	private String NomeRemetente;			 				//(19)	MAX(250)
	private String EnderecoRemetente;						//(20)	MAX(226)	
	private String CidadeRemetente;							//(21)	MAX(72)
	private String UfRemetente;								//(22)	MAX(2)
	private String CepRemetente;							//(23)	MAX(8)
	
	private String EmailRemetente;
	private String NumeroEtiqueta;
	private byte[] ArquivoComplementar;	
	private String ArquivoDataInsercao;	
	private String CodigoAcesso;			
	private String CodigoModelo;
	private String Comarca;					
	private String DataExpedicao;
	private String DataAudiencia;	
	private String HoraAudiencia;
	private List Id_Arquivo;
	private String Id_Comarca;	
	private String Id_MovimentacaoTipo;	
	private String Id_Movimentacao;	
	private String Id_ProcessoParte ;
	private String Id_Processo ;
	private String Id_ProcessoTipo;
	private String Id_PendenciaTipo;
	private String Id_Serventia;
	private String Id_UsuarioServentiaCadastrador;
	
	private String MatrizRelacionamento;
	private String Modelo;
	private String MovimentacaoTipoCodigo;
	private String MovimentacaoTipo;
	private String MovimentacaoComplemento;
	private String MovimentacaoData;
	private String NumeroGuia;				 
	private String PendenciaTipocodigo;
	private String PoloAtivo;				
	private String PoloPassivo;				
	private String ProcessoNumero;			
	private String ProcessoTipo;			
	private String ValorCausa;	
	private String TelefoneServentia;
	private String UsuarioCadastrador;
	private String MetaDados;
	private List CargoCadastrador;
	
	public static final String 	URL_HOST 		= "ediac.correios.com.br";
	public static final int    	FTP_PORT 		= 21;
	public static final int    	FTPS_PORT 		= 990;
	public static final String 	USERNAME 		= "5197773";
	public static final String 	PASSWORD 		= "TjG5197";
	public static final String 	MM_1_FOLHA		= "17921";
	public static final String 	MM_2A5_FOLHAS	= "17941";
	public static final String 	MM_6A100_FOLHAS	= "17961";
	private static final String CARTAO_POSTAGEM	= "0075460637";
	private static final String NUMERO_CONTRATO	= "9912488314";
	
	public enum ModeloCarta {
		INTIMACAO_AUDIENCIA_INSTRUCAO_JULGAMENTO	( "1", 		"278697", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Audiência de Instrução e Julgamento"),
		INTIMACAO_NOVO_ADVOGADO						( "2", 		"278698", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Constituir Novo Advogado"),
		INTIMACAO_CUMPRIMENTO_SENTENCA				( "3", 		"278699", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Cumprimento de Sentença"),
		INTIMACAO_NORMAL							( "4",		"278693", 	PendenciaTipoDt.INTIMACAO, 		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Genérica"),
		INTIMACAO_HORA_CERTA						( "5", 		"278700", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Hora Certa"),
		INTIMACAO_JUNTA_MEDICA						( "6", 		"278701", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Junta Médica"),
		INTIMACAO_PROSSEGUIMENTO_FEITO				( "7", 		"278702", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Prosseguimento ao Feito"),
		INTIMACAO_TESTEMUNHA						( "8", 		"278703",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Testemunha"),
		INTIMACAO_PENHORA							( "9", 		"295894", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA CÍVEL-Intimação Penhora"),
		INTIMACAO_3_DIAS							("17", 		"311581",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação 3 Dias"),
		INTIMACAO_15_DIAS							("18", 		"311583",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação 15 Dias"),
		INTIMACAO_COMPARECIMENTO_1VARA				("29", 		"312170",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Comparecimento 1ª Vara"),
		INTIMACAO_COMPARECIMENTO_2VARA				("30", 		"312174",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Comparecimento 2ª Vara"),
		INTIMACAO_COMPARECIMENTO_3VARA				("31", 		"312176",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Comparecimento 3ª Vara"),
		INTIMACAO_COMPARECIMENTO_4VARA				("32", 		"312181",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Comparecimento 4ª Vara"),
		INTIMACAO_COMPARECIMENTO_5VARA				("33", 		"312160",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Comparecimento 5ª Vara"),
		INTIMACAO_COMPARECIMENTO_6VARA				("34", 		"312164",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Comparecimento 6ª Vara"),
		INTIMACAO_SENTENCA							("35", 		"311733",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"FAMÍLIA-Intimação Sentença"),
		INTIMACAO_SENTENCA_PROMOVENTE				("25", 		"311594",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ FAZ PUB-Intimação de Sentença do Promovente"),
		INTIMACAO_SENTENCA_PROMOVIDO				("26", 		"311593",	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ FAZ PUB-Intimação de Sentença do Promovido"),
		INTIMACAO_PROMOVENTE						("27", 		"311592", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ FAZ PUB-Intimação do Promovente"),
		INTIMACAO_PROMOVIDO							("28", 		"311591", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ FAZ PUB-Intimação do Promovido"),
		INTIMACAO_NOTIFICACAO						("36", 		"311721", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ MUN-Intimação Notificação"),
		INTIMACAO_APRESENTAR_EMBARGOS				("37", 		"311714", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Apresentar Embargos"),
		INTIMACAO_NOVA_CDA							("38", 		"311717", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Ciência de Nova CDA"),
		INTIMACAO_CONTRARRAZOAR_RECURSO				("39", 		"311723", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Contrarrazoar Recurso"),
		INTIMACAO_MANIFESTAR_PETICAO				("40", 		"311725", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Manifestar Sobre Petição"),
		INTIMACAO_OPOSICAO_EMBARGOS_TERCEIRO		("41", 		"311726", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Oposição Embargos de Terceiro"),
		INTIMACAO_PAGAMENTO_CUSTAS_PROCESSUAIS		("42", 		"311728", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Pagamento Custas Processuais"),
		INTIMACAO_PAGAMENTO_DEBITO					("43", 		"311729", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Pagamento do Débito"),
		INTIMACAO_FAZ_PENHORA						("44", 		"311731", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"VARA FAZ PUB MUN-Intimação Penhora"),
		INTIMACAO_INSTRUCAO_JULGAMENTO_RECLAMADO	("55", 		"319220", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Audiência Instrução Reclamado"),		
		INTIMACAO_INSTRUCAO_JULGAMENTO_RECLAMANTE	("56", 		"319223", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Audiência Instrução Reclamante"),		
		INTIMACAO_CERTIDAO_DECISAO_RECLAMADO		("57", 		"319225", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Certidão Decisão Reclamado"),			
		INTIMACAO_CERTIDAO_DECISAO_RECLAMANTE		("58", 		"319227", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Certidão Decisão Reclamante"),			
		INTIMACAO_PGTO_CUSTAS_FINAIS				("59", 		"319228", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Provar Pgto Custas Finais"),					
		INTIMACAO_CONCILIACAO_RECLAMADO_ART53		("60", 		"319230", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Conciliação Art. 53 Reclamado"),
		INTIMACAO_CONCILIACAO_RECLAMANTE_ART53		("61", 		"319231", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Conciliação Art. 53 Reclamante"),
		INTIMACAO_CONCILIACAO_RECLAMADO				("62", 		"319232", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Conciliação de Reclamado"),
		INTIMACAO_CONCILIACAO_RECLAMANTE			("63", 		"319233", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Conciliação de Reclamante"),
		INTIMACAO_CUMPRIMENTO_SENTENCA_JUIZ			("64", 		"319239", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação de Cumprimento de Sentença"),					
		INTIMACAO_DOCUMENTOS_NOVOS					("65", 		"319240", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação de Documentos Novos CPC 437 §1º"),		
		INTIMACAO_SENTENCA_RECLAMANTE				("66", 		"319241", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação de Sentença de Reclamante"),					
		INTIMACAO_TESTEMUNHA_JUIZ					("67", 		"319242", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Testemunha"),
		INTIMACAO_NORMAL_JUIZ						("68", 		"319362", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Genérica"),								
		INTIMACAO_INTERESSE_PROSSEGUIMENTO			("69", 		"319367", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Manifestar Interesse Prosseguimento"),	
		INTIMACAO_CERTIDAO_OFICIAL_JUSTICA			("70", 		"319376", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Manifestar Certidão Oficial Justiça"),	
		INTIMACAO_CITACAO_FRUSTRADA					("71", 		"319401", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Manifestar Citação Frustrada"),			
		INTIMACAO_SENTENCA_RECLAMADO				("72", 		"319411", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Intimação Sentença de Reclamado"),				
		NOTIFICACAO_CONTRADITORIO_PREVIO			("73", 		"319433", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Notificação Contraditório Prévio"),				
		NOTIFICACAO_TUTELA_PROVISORIA_URGENCIA		("74", 		"319437", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"JUIZ CÍVEL-Notificação Tutela Provisória de Urgência"),		
		INTIMACAO_AUDIENCIA_TESTEMUNHA				("80", 		"319561", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"SUCESSÃO-Intimação Audiência Escrivania Testemunha"),
		INTIMACAO_AUDIENCIA_ESCRIVANIA				("81", 		"319563", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"SUCESSÃO-Intimação Audiência Escrivania"),
		INTIMACAO_AUTOR_INERTE						("82", 		"319565", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"SUCESSÃO-Intimação Autor Inerte"),
		INTIMACAO_CONSTITUIR_NOVO_ADVOGADO			("83", 		"319567", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"SUCESSÃO-Intimação Constituir Novo Advogado"),
		INTIMACAO_NORMAL_SUCESSAO					("84", 		"319568", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"SUCESSÃO-Intimação Genérica"),
		INTIMACAO_INVENTARIANTE_INERTE				("85", 		"319569", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"SUCESSÃO-Intimação Inventariante Inerte"),
		INTIMACAO_2_GRAU_AGRAVADO					("86", 		"349002", 	PendenciaTipoDt.INTIMACAO,		ArquivoTipoDt.ID_INTIMACAO,		"2° GRAU CÍVEL-Intimação do Agravado"),
		
		CITACAO_AUDIENCIA_CEJUSC					("10", 		"278705",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA CÍVEL-Citação Audiência CEJUSC"),	
		CITACAO_CONFINANTE							("11", 		"278706",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA CÍVEL-Citação Confinante"),
		CITACAO_NORMAL								("12", 		"278695", 	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA CÍVEL-Citação Genérica"),
		CITACAO_RESPOSTA_RECURSO					("13", 		"278707",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA CÍVEL-Citação Resposta ao Recurso"),
		CITACAO_IMPROCEDENCIA_LIMINAR				("14", 		"278708",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA CÍVEL-Citação Improcedência Liminar do Pedido"),
		CITACAO_MONITORIA							("15", 		"295885", 	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO, "VARA CÍVEL-Citação Monitória"),
		CITACAO_EXECUCAO							("16", 		"295882", 	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA CÍVEL-Citação Execução"),
		CITACAO_3_DIAS								("19", 		"311573",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO, "FAMÍLIA-Citação 3 Dias"),
		CITACAO_15_DIAS								("20", 		"311574",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO, "FAMÍLIA-Citação 15 Dias"),
		CITACAO_EXECUCAO_GERAL						("21", 		"311570", 	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"EXECUÇÃO FISCAL-Citação Geral"),
		CITACAO_INTIMACAO_TUTELA_DEFERIDA			("22", 		"311589",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ FAZ PUB-Citação e Intimação de Tutela Deferida"),	
		CITACAO_INTIMACAO_AUDIENCIA_TUTELA_DEFERIDA	("23", 		"311590",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ FAZ PUB-Citação e Intimação Audiência Tutela Deferida"),
		CITACAO_EMBARGOS_TERCEIRO					("45", 		"311706",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA FAZ PUB MUN-Citação Embargos de Terceiro"),
		CITACAO_EXECUCAO_FISCAL						("46", 		"311711",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA FAZ PUB MUN-Citação Execução Fiscal"),
		CITACAO_RESTAURACAO_AUTOS					("47", 		"311712",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"VARA FAZ PUB MUN-Citação Restauração de Autos"),
		CITACAO_GENERICA							("24", 		"311596",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ FAZ PUB-Citação Genérica"),	
		CITACAO_AUDIENCIA_BIFURCADA					("48", 		"319094",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Concilicação Audiência Bifurcada"),		
		CITACAO_UNA									("49", 		"319116",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Una, Concilicação, Instrução Julgamento"),		
		CITACAO_TUTELA_DEFERIDA_CONCILIACAO			("50", 		"319137",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Intimação Tutela Deferida e Conciliação"),		
		CITACAO_EXECUCAO_TITULO_EXTRAJUDICIAL		("51", 		"319182",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Execução de Título Extrajudicial"),		
		CITACAO_IMPROCEDENCIA_LIMINAR_PEDIDO		("52", 		"319207",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Improcedência Liminar do Pedido"),		
		CITACAO_INCIDENTE_DESCONSIDERACAO_PJ		("53", 		"319210",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Incidente de Desconsideração da PJ"),		
		CITACAO_RESPOSTA_RECURSO_JUIZ				("54", 		"319217",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"JUIZ CÍVEL-Citação Resposta ao Recurso"),					
		CITACAO_AUDIENCIA							("75", 		"319487",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"SUCESSÃO-Citação Audiência"),
		CITACAO_INTIMACAO_AUDIENCIA					("76", 		"319519",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"SUCESSÃO-Citação e Intimação Audiência"),
		CITACAO_NORMAL_SEM_AUDIENCIA				("77", 		"319523",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"SUCESSÃO-Citação Genérica Sem Audiência"),
		CITACAO_NORMAL_SUCESSAO						("78", 		"319531",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"SUCESSÃO-Citação Genérica"),
		CITACAO_HORA_CERTA							("79", 		"319532",	PendenciaTipoDt.CARTA_CITACAO,	ArquivoTipoDt.ID_CARTA_CITACAO,	"SUCESSÃO-Citação Hora Certa");
		
		String codigoModelo;
		String idModelo;
		int pendenciaTipo;
		int idArquivoTipo;
		String tituloModelo;
		
		ModeloCarta (String codModelo, String modelo, int pendTipo, int idArqTipo, String titModelo) {
			codigoModelo = codModelo;
			idModelo = modelo;
			pendenciaTipo = pendTipo;
			idArquivoTipo = idArqTipo;
			tituloModelo = titModelo;
		}
		public String getCodigoModelo(){return codigoModelo;}
		public String getId_Modelo(){return idModelo;}
		public int getPendenciaTipo(){return pendenciaTipo;}
		public int getIdArquivoTipo(){return idArquivoTipo;}
		public String getTituloModelo(){return tituloModelo;}
		
	}
	
	public enum CodigoBaixa {
		ENTREGUE							( 1, "Entregue"), 
		NAO_PROCURADO_REMETENTE				( 3, "Não procurado"), 
		RECUSADO							( 4, "Recusado"), 
		FALECIDO							( 5, "Falecido"), 
		DESCONHECIDO						( 6, "Desconhecido"), 
		ENDERECO_INSUFICIENTE				( 7, "Endereço insuficiente"),
		NAO_EXISTE_NUMERO					( 8, "Não existe o número indicado"),
		MUDOU_SE							(10, "Mudou-se"),
		AUSENTE_DEVOLVIDO_REMETENTE			(21, "Ausente - Devolvido ao remetente"),
		NAO_PROCURADO_DEVOLVIDO_REMETENTE	(26, "Não procurado - Devolvido ao Remetente"),
		AVARIADO							(28, "Avariado"),
		DOCUMENTACAO_NAO_FORNECIDA			(33, "Documentação não fornecida"),
		SINISTRO							(37, "Sinistro"),
		EMPRESA_FALIDA						(38, "Empresa falida"),
		ENDERECO_SEM_DISTRIBUICAO			(48, "Endereço sem distribuição domiciliar"),
		ROUBADO_50							(50, "Roubado"),
		ROUBADO_51							(51, "Roubado"),
		ROUBADO_52							(52, "Roubado"),
		OBJETO_EXTRAVIADO					(80, "Objeto extraviado");
		
		int codigo;
		String descricao;
		CodigoBaixa(int codigoBaixa, String descricaoBaixa) {
			codigo = codigoBaixa;
			descricao = descricaoBaixa;
		}
		public int getCodigo() {return codigo;}
		public String getDescricao() {return descricao;}
	}
	
	public enum CodigoInconsistencia {
		ARQUIVO_NOMENCLATURA_INVALIDA				( 1, "Arquivo com nomenclatura inválida"),
		ARQUIVO_CORROMPIDO							( 2, "Arquivo corrompido"),
		ARQUIVO_FORMATO_INVALIDO					( 3, "Arquivo com formato inválido"),
		CODIGO_MATRIZ_OBJETO_INVALIDO				( 4, "Código da matriz de objeto inválido"),
		EXTENSAO_ARQUIVO_INVALIDO					( 5, "Extensão de arquivo inválida"),
		LEIAUTE_NAO_CORRESPONDE_ESPERADO			( 6, "Leiaute não corresponde ao esperado"),
		NUMERO_LOTE_UTILIZADO						( 7, "Número do lote já utilizado"),
		NUMERO_LOTE_DIVERGENTE						( 8, "Número do lote divergente no arquivo"),
		VERSAO_INVALIDA_ARQUIVO						( 9, "Versão inválida do arquivo"),
		NUMERO_CONTRATO_INVALIDO					(10, "Número de contrato inválido"),
		ARQUIVO_COMPLEMENTAR_NAO_REFERENCIADO		(11, "Arquivo complementar não referenciado"),
		ARQUIVO_REFERENCIA_INCONSISTENTE			(12, "Arquivo de referência inconsistente"),
		ARQUIVO_ASSINATURA_INVALIDA					(13, "Arquivo com assinatura digital inválida"),
		CAMPO_NAO_PREENCHIDO						(14, "Campo obrigatório não preenchido"),
		CONTEUDO_CAMPO_INVALIDO						(15, "Conteúdo do campo inválido"),
		CODIGO_IDENTIFICADOR_INVALIDO				(16, "Código identificador inválido"),
		CARTAO_POSTAGEM_INVALIDO					(17, "Número de cartão de postagem inválido"),
		SERVICO_INVALIDO							(18, "Serviço inválido para o cartão de postagem"),
		SERVICO_ADICIONAL_INVALIDO					(19, "Serviço adicional inválido"),
		NUMERO_PAGINAS_DIVERGENTE					(20, "Número de páginas divergente"),
		NUMERO_FOLHAS_EXCEDENTE						(21, "Número de folhas excede a quantidade contratada"),
		CEP_INVALIDO								(22, "CEP inválido"),
		CEP_INVALIDO_OBJETO_REJEITADO				(23, "CEP inválido - objeto rejeitado"),
		CEP_DIVERGENTE_UF							(24, "CEP divergente em relação à UF"),
		CEP_DIVERGENTE_UF_OBJETO_REJEITADO			(25, "CEP divergente em relação à UF - objeto rejeitado"),
		UNIDADE_DISTRIBUICAO_INDISPONIVEL			(26, "Unidade de distribuição indisponível"),
		LIMITE_PESO_ULTRAPASSADO					(27, "Limite de peso ultrapassado"),
		ARQUIVO_COMPLEMENTAR_INEXISTENTE			(28, "Arquivo complementar inexistente"),
		DOCUMENTO_TAMANHO_DIVERGENTE				(29, "Documento com tamanho divergente"),
		DOCUMENTO_ORIENTACAO_INVALIDA				(30, "Documento com orientação de impressão inválida"),
		DIGITO_VERIFICADOR_INVALIDO					(31, "Dígito verificador inválido"),
		ETIQUETA_UTILIZADA							(32, "Etiqueta já utilizada"),
		ETIQUETA_NAO_PERTENCE_CLIENTE				(33, "Etiqueta não pertence a faixa disponibilizada ao cliente"),
		OBJETO_EXPEDIDO								(34, "Objeto já expedido"),
		ARQUIVO_NAO_CADASTRADO						(35, "Arquivo de serviço/referência não cadastrado no sistema"),
		ERRO_ADICIONAR_IMAGENS						(36, "Erro ao adicionar imagens no arquivo compactado"),
		ERRO_CRIAR_ARQUIVO_COMPACTADO				(37, "Erro ao criar arquivo compactado"),
		ERRO_DISPONIBILIZAR_ARQUIVO_AR				(38, "Erro ao disponibilizar arquivo de devolução de AR e metadados"),
		CONTEUDO_NAO_CONFERE						(39, "Conteúdo do arquivo não confere"),
		RESPOSTA_NAO_IDENTIFICADA					(40, "Resposta não identificada"),
		ARQUIVO_COMPLEMENTAR_ASSINATURA_INVALIDA	(41, "Arquivo complementar com assinatura digital inválida"),
		ARQUIVO_COMPLEMENTAR_INEXISTENTE_			(42, "Arquivo complementar inexistente"),
		EXTENSAO_ARQUIVO_COMPLEMENTAR_INVALIDA		(43, "Extensão de arquivo complementar inválida");
		
		int codigo;
		String descricao;
		CodigoInconsistencia(int codigoInconsistencia, String descricaoInconsistencia) {
			codigo = codigoInconsistencia;
			descricao = descricaoInconsistencia;
		}
		public int getCodigo() {return codigo;}
		public String getDescricao() {return descricao;}
	}
	
	public CorreiosDt() {
		limpar();
	}
	
	public void limpar() {
		Id 									= "";
		TipoRegistro 						= "1";
		Id_Pendencia 						= "";
		NumeroLote 							= "";
		CartaoPostagem 						= CARTAO_POSTAGEM;
		NumeroContrato 						= NUMERO_CONTRATO;
		ServicoAdicional 					= false;
		IdentificadorArquivoSpool 			= "N";
		NomeArquivoSpool 					= "";
		IdentificadorArquivoComplementar 	= "S";
		NomeArquivoComplementar 			= "";
		NomeDestinatario 					= "";					
		EnderecoDestinatario	 			= "";
		NumeroEnderecoDestinatario 			= "";
		ComplementoEnderecoDestinatario 	= "";
		BairroDestinatario 					= "";	
		CidadeDestinatario 					= "";
		UfDestinatario 						= "";
		CepDestinatario 					= "";
		EnderecoRemetente 					= "";
		CidadeRemetente						= "";						
		UfRemetente							= "";							
		CepRemetente						= "";
		EmailRemetente						= "";
		NumeroEtiqueta						= "";
		Comarca 							= "";							
		NumeroGuia 							= "";						
		ProcessoNumero 						= "";							
		ProcessoTipo 						= "";							
		PoloAtivo 							= "";						
		PoloPassivo 						= "";							
		ValorCausa 							= "";						
		CodigoAcesso 						= "";						
		DataExpedicao 						= "";								
		NomeRemetente 						= "";						
		Id_ProcessoParte					= "";
		Id_Processo 						= "";
		Id_Arquivo 							= null;
		Id_Serventia 						= "";
		Id_Comarca 							= "";
		CodigoModelo 						= "";
		ArquivoComplementar 				= null;
		PendenciaTipocodigo 				= "";
		MovimentacaoTipoCodigo 				= "";
		MovimentacaoTipo 					= "";
		MovimentacaoComplemento 			= "";
		MovimentacaoData 					= "";
		Id_ProcessoTipo 					= "";
		Id_PendenciaTipo 					= "";
		Id_MovimentacaoTipo 				= "";
		Id_Movimentacao		 				= "";
		ArquivoDataInsercao 				= "";
		MatrizRelacionamento				= "";
		Modelo								= "";
		TelefoneServentia					= "";
		DataAudiencia						= "";
		HoraAudiencia						= "";
		UsuarioCadastrador					= "";
		MetaDados							= "";
		CargoCadastrador					= null;
		Id_UsuarioServentiaCadastrador		= "";

	}
	
	@Override
	public void setId(String id) {
		if(id != null)
			Id = id;
	}

	@Override
	public String getId() {
		return this.Id;
	}
	
	public String getTipoRegistro() {
		return this.TipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		if(tipoRegistro != null)
			TipoRegistro = tipoRegistro;
	}

	public String getIdPendencia() {
		return this.Id_Pendencia;
	}

	public void setIdPendencia(String idPendencia) {
		if(idPendencia != null)
			Id_Pendencia = idPendencia;
	}

	public String getNumeroLote() {
		return this.NumeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		if(numeroLote != null)
			NumeroLote = numeroLote;
	}
	
	public String getTelefoneServentia() {
		return this.TelefoneServentia;
	}

	public void setTelefoneServentia(String telefoneServentia) {
		if(telefoneServentia != null)
			TelefoneServentia = telefoneServentia;
	}

	public String getCartaoPostagem() {
		return this.CartaoPostagem;
	}

	public void setCartaoPostagem(String cartaoPostagem) {
		if(cartaoPostagem != null)
			CartaoPostagem = cartaoPostagem;
	}

	public String getNumeroContrato() {
		return this.NumeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		if(numeroContrato != null)
			NumeroContrato = numeroContrato;
	}
	@JsonIgnore
	public String getServicoAdicional() {
		return ServicoAdicional ? "002" : "";
	}

	public void setServicoAdicional(boolean servicoAdicional) {
		ServicoAdicional = servicoAdicional;
	}

	public String getIdentificadorArquivoSpool() {
		return this.IdentificadorArquivoSpool;
	}

	public void setIdentificadorArquivoSpool(String identificadorArquivoSpool) {
		if(identificadorArquivoSpool != null)
			IdentificadorArquivoSpool = identificadorArquivoSpool;
	}

	public String getNomeArquivoSpool() {
		return this.NomeArquivoSpool;
	}

	public void setNomeArquivoSpool(String nomeArquivoSpool) {
		if(nomeArquivoSpool != null)
			NomeArquivoSpool = nomeArquivoSpool;
	}

	public String getIdentificadorArquivoComplementar() {
		return this.IdentificadorArquivoComplementar;
	}

	public void setIdentificadorArquivoComplementar(String identificadorArquivoComplementar) {
		if(identificadorArquivoComplementar != null)
			IdentificadorArquivoComplementar = identificadorArquivoComplementar;
	}

	public String getNomeArquivoComplementar() {
		return this.NomeArquivoComplementar;
	}

	public void setNomeArquivoComplementar(String nomeArquivoComplementar) {
		if(nomeArquivoComplementar != null)
			NomeArquivoComplementar = nomeArquivoComplementar;
	}
	
	public String getModelo() {
		return this.Modelo;
	}
	
	public void setModelo(String modelo) {
		if(modelo != null)
			Modelo = modelo;
	}
	
	public String getMatrizRelacionamento() {
		return this.MatrizRelacionamento;
	}

	public void setMatrizRelacionamento(String matrizRelacionamento) {
		if(matrizRelacionamento != null)
			MatrizRelacionamento = matrizRelacionamento;
	}
	
	public String getCodigoModelo() {
		return this.CodigoModelo;
	}
	
	public void setCodigoModelo(String codigoModelo) {
		if(codigoModelo != null)
			CodigoModelo = codigoModelo;
	}
	@JsonIgnore
	public byte[] getArquivoComplementar() {
		return this.ArquivoComplementar;
	}
	
	public void setArquivoComplementar(byte[] arquivoComplementar) {
		if(arquivoComplementar != null)
			ArquivoComplementar = arquivoComplementar;
	}

	public String getNomeDestinatario() {
		return this.NomeDestinatario;
	}

	public void setNomeDestinatario(String nomeDestinatario) {
		if(nomeDestinatario != null)
			NomeDestinatario = removerMarcasDiacriticas(nomeDestinatario);
	}

	public String getEnderecoDestinatario() {
		return this.EnderecoDestinatario;
	}

	public void setEnderecoDestinatario(String enderecoDestinatario) {
		if(enderecoDestinatario != null)
			EnderecoDestinatario = removerMarcasDiacriticas(enderecoDestinatario);
	}

	public String getNumeroEnderecoDestinatario() {
		return this.NumeroEnderecoDestinatario;
	}

	public void setNumeroEnderecoDestinatario(String numeroEnderecoDestinatario) {
		if(numeroEnderecoDestinatario != null)
			NumeroEnderecoDestinatario = numeroEnderecoDestinatario;
	}

	public String getComplementoEnderecoDestinatario() {
		return this.ComplementoEnderecoDestinatario;
	}

	public void setComplementoEnderecoDestinatario(String complementoEnderecoDestinatario) {
		if(complementoEnderecoDestinatario != null)
			ComplementoEnderecoDestinatario = removerMarcasDiacriticas(complementoEnderecoDestinatario);
	}

	public String getBairroDestinatario() {
		return this.BairroDestinatario;
	}

	public void setBairroDestinatario(String bairroDestinatario) {
		if(bairroDestinatario != null)
			BairroDestinatario = bairroDestinatario;
	}

	public String getCidadeDestinatario() {
		return this.CidadeDestinatario;
	}

	public void setCidadeDestinatario(String cidadeDestinatario) {
		if(cidadeDestinatario != null)
			CidadeDestinatario = cidadeDestinatario;
	}

	public String getUfDestinatario() {
		return this.UfDestinatario;
	}

	public void setUfDestinatario(String ufDestinatario) {
		if(ufDestinatario != null)
			UfDestinatario = ufDestinatario;
	}

	public String getCepDestinatario() {
		return this.CepDestinatario;
	}

	public void setCepDestinatario(String cepDestinatario) {
		if(cepDestinatario != null)
			CepDestinatario = cepDestinatario;
	}

	public String getEnderecoRemetente() {
		return EnderecoRemetente;
	}

	public void setEnderecoRemetente(String enderecoRemetente) {
		if(enderecoRemetente != null) {
			EnderecoRemetente = removerMarcasDiacriticas(enderecoRemetente);
		}
	}

	public String getCidadeRemetente() {
		return CidadeRemetente;
	}

	public void setCidadeRemetente(String cidadeRemetente) {
		CidadeRemetente = cidadeRemetente;
	}

	public String getUfRemetente() {
		return UfRemetente;
	}

	public void setUfRemetente(String ufRemetente) {
		UfRemetente = ufRemetente;
	}

	public String getCepRemetente() {
		return CepRemetente;
	}

	public void setCepRemetente(String cepRemetente) {
		CepRemetente = cepRemetente;
	}
	
	public String getEmailRemetente() {
		return EmailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		EmailRemetente = emailRemetente;
	}

	public String getComarca() {
		return this.Comarca;
	}

	public void setComarca(String comarca) {
		if(comarca!= null)
			Comarca = comarca;
	}

	public String getNumeroGuia() {
		return this.NumeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		if(numeroGuia!= null)
			NumeroGuia = numeroGuia;
	}

	public String getProcessoNumero() {
		return this.ProcessoNumero;
	}

	public void setProcessoNumero(String protocolo) {
		if(protocolo!= null)
			ProcessoNumero = protocolo;
	}
	
	public String getProcessoNumeroCompleto() {
		String[] numero = getProcessoNumero().split("\\.");
		if(numero != null) {
			return (Funcoes.completarZeros(numero[0], 7) + "-" + Funcoes.completarZeros(numero[1], 2) + "." + Funcoes.completarZeros(numero[2],4) + "." + Configuracao.JTR + "." + Funcoes.completarZeros(numero[5], 4));
		}
		return "";
	}

	public String getProcessoTipo() {
		return this.ProcessoTipo;
	}

	public void setProcessoTipo(String natureza) {
		if(natureza!= null)
			ProcessoTipo = natureza;
	}

	public String getPoloAtivo() {
		return PoloAtivo;
	}
	
	public String getPoloPassivo() {
		return PoloPassivo;
	}

	public void setPoloAtivo(String poloAtivo) {
		if(poloAtivo!= null)
			PoloAtivo = removerMarcasDiacriticas(poloAtivo);
	}

	public void setPoloPassivo(String poloPassivo) {
		if(poloPassivo!= null)
			PoloPassivo = removerMarcasDiacriticas(poloPassivo);
	}

	public String getValorCausa() {
		return this.ValorCausa;
	}

	public void setValorCausa(String valorCausa) {
		if(valorCausa!= null)
			ValorCausa = valorCausa;
	}

	public String getCodigoAcesso() {
		return this.CodigoAcesso;
	}

	public void setCodigoAcesso(String codigoAcesso) {
		if(codigoAcesso!= null)
			CodigoAcesso = codigoAcesso;
	}

	public String getDataExpedicao() {
		return this.DataExpedicao;
	}

	public void setDataExpedicao(String data) {
		if(data!= null)
			DataExpedicao = data;
	}
	
	public String getDataAudiencia() {
		return this.DataAudiencia;
	}

	public void setDataAudiencia(String data) {
		if(data!= null)
			DataAudiencia = data;
	}
	
	public String getHoraAudiencia() {
		return this.HoraAudiencia;
	}

	public void setHoraAudiencia(String hora) {
		if(hora!= null)
			HoraAudiencia = hora;
	}

	public String getNomeRemetente() {
		return this.NomeRemetente;
	}

	public void setNomeRemetente(String nomeRemetente) {
		if(nomeRemetente!= null)
			NomeRemetente = removerMarcasDiacriticas(nomeRemetente);
	}

	public String getId_ProcessoParte() {
		return Id_ProcessoParte;
	}
	public String getId_Processo() {
		return Id_Processo;
	}

	public void setId_Arquivo(List id_Arquivo) {
		Id_Arquivo = id_Arquivo;
	}
	
	public List getId_Arquivo() {
		return Id_Arquivo;
	}

	public void setId_ProcessoParte(String id_ProcessoParte) {
		Id_ProcessoParte = id_ProcessoParte;
	}

	public void setId_Processo(String id_Processo) {
		Id_Processo = id_Processo;
	}

	public void setId_Serventia(String id_Serventia) {
		Id_Serventia = id_Serventia;
	}

	public String getId_Serventia() {
		return Id_Serventia;
	}

	public void setId_Comarca(String id_Comarca) {
		Id_Comarca = id_Comarca;
		
	}

	public String getId_Comarca() {
		return Id_Comarca;
	}
	
	public void setPendenciaTipocodigo(String pendenciaTipocodigo) {
		if(pendenciaTipocodigo != null)
			PendenciaTipocodigo = pendenciaTipocodigo;
	}

	public String getPendenciaTipocodigo() {
		return this.PendenciaTipocodigo;
	}
	
	public void setMovimentacaoTipoCodigo(String movimentacaoTipoCodigo) {
		if(movimentacaoTipoCodigo != null)
			MovimentacaoTipoCodigo = movimentacaoTipoCodigo;
	}

	public String getMovimentacaoTipoCodigo() {
		return this.MovimentacaoTipoCodigo;
	}
	
	public void setMovimentacaoTipo(String movimentacaoTipo) {
		if(movimentacaoTipo != null)
			MovimentacaoTipo = movimentacaoTipo;
	}

	public String getMovimentacaoTipo() {
		return this.MovimentacaoTipo;
	}
	
	public void setMovimentacaoComplemento(String movimentacaoComplemento) {
		if(movimentacaoComplemento != null)
			MovimentacaoComplemento = movimentacaoComplemento;
	}

	public String getMovimentacaoComplemento() {
		return this.MovimentacaoComplemento;
	}
	
	public void setMovimentacaoData(String movimentacaoData) {
		if(movimentacaoData != null)
			MovimentacaoData = movimentacaoData;
	}

	public String getMovimentacaoData() {
		return this.MovimentacaoData;
	}
	
	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		if(id_ProcessoTipo != null)
			Id_ProcessoTipo = id_ProcessoTipo;
	}

	public String getId_ProcessoTipo() {
		return this.Id_ProcessoTipo;
	}
	
	public void setId_PendenciaTipo(String id_PendenciaTipo) {
		if(id_PendenciaTipo != null)
			Id_PendenciaTipo = id_PendenciaTipo;
	}

	public String getId_PendenciaTipo() {
		return this.Id_PendenciaTipo;
	}
	
	public void setId_MovimentacaoTipo(String id_MovimentacaoTipo) {
		if(id_MovimentacaoTipo != null)
			Id_MovimentacaoTipo = id_MovimentacaoTipo;
	}

	public String getId_MovimentacaoTipo() {
		return this.Id_MovimentacaoTipo;
	}
	
	public void setId_Movimentacao(String id_Movimentacao) {
		if(id_Movimentacao != null)
			Id_Movimentacao = id_Movimentacao;
	}

	public String getId_Movimentacao() {
		return this.Id_Movimentacao;
	}
	
	public void setArquivoDataInsercao(String arquivoDataInsercao) {
		if(arquivoDataInsercao != null)
			ArquivoDataInsercao = arquivoDataInsercao;
	}

	public String getArquivoDataInsercao() {
		return this.ArquivoDataInsercao;
	}
	
	public void setNumeroEtiqueta(String numeroEtiqueta) {
		if(numeroEtiqueta != null)
			NumeroEtiqueta = numeroEtiqueta;
	}

	public String getNumeroEtiqueta() {
		return this.NumeroEtiqueta;
	}
	
	public String getUsuarioCadastrador() {
		return this.UsuarioCadastrador;
	}

	public void setUsuarioCadastrador(String usuarioCadastrador) {
		if(usuarioCadastrador != null)
			UsuarioCadastrador = usuarioCadastrador;
	}
	
	public String getMetaDados() {
		return this.MetaDados;
	}

	public void setMetaDados(String metaDados) {
		if(MetaDados != null)
			MetaDados = metaDados;
	}
	
	public String getId_UsuarioServentiaCadastrador() {
		return this.Id_UsuarioServentiaCadastrador;
	}

	public void setId_UsuarioServentiaCadastrador(String id_UsuarioServentiaCadastrador) {
		if(id_UsuarioServentiaCadastrador != null)
			Id_UsuarioServentiaCadastrador = id_UsuarioServentiaCadastrador;
	}
	
	public List getCargoCadastrador() {
		return this.CargoCadastrador;
	}

	public void setCargoCadastrador(List cargoCadastrador) {
		if(cargoCadastrador != null)
			CargoCadastrador = cargoCadastrador;
	}
	
	public ModeloCarta getModelo(String codigoModelo) {
		for (ModeloCarta modelo : ModeloCarta.values()) {
			if(modelo.getCodigoModelo().equals(codigoModelo)) {
				return modelo;
			}
		}
		return null;
	}

	public String montarArquivoServico() {
		String arquivoServico = new StringBuffer()				
		.append(this.getTipoRegistro() 						+ "|")	//Tipo de Registro							( 1)
		.append(this.getIdPendencia() 						+ "|")	//Código do Objeto Cliente					( 2)
		.append(this.getNumeroLote() 						+ "|")	//Número do Lote							( 3)
		.append(this.getCartaoPostagem() 					+ "|")	//Cartão de Postagem						( 4)
		.append(this.getNumeroContrato() 					+ "|")	//Número do Contrato						( 5)
		.append(this.getServicoAdicional() 					+ "|")	//Serviço Adicional							( 6)
		.append(this.getIdentificadorArquivoSpool() 		+ "|")	//Identificador do Arquivo Spool			( 7)
		.append(this.getNomeArquivoSpool() 					+ "|")	//Nome do Arquivo Spool						( 8)
		.append(this.getIdentificadorArquivoComplementar() 	+ "|")	//Identificador do Arquivo Complementar		( 9)
		.append(this.getNomeArquivoComplementar() 			+ "|")	//Nome do Arquivo Complementar				(10)
		.append(this.getNomeDestinatario() 					+ "|")	//Nome do Destinatário						(11)					
		.append(this.getEnderecoDestinatario() 				+ "|")	//Endereço do Destinatário					(12)
		.append(this.getNumeroEnderecoDestinatario() 		+ "|")	//Número do Endereço do Destinatário		(13)
		.append(this.getComplementoEnderecoDestinatario() 	+ "|")	//Complemento do Endereço do Destinatário	(14)
		.append(this.getBairroDestinatario() 				+ "|")	//Bairro do Destinatário					(15)	
		.append(this.getCidadeDestinatario() 				+ "|")	//Cidade do Destinatário					(16)
		.append(this.getUfDestinatario() 					+ "|")	//UF do Destinatário						(17)
		.append(this.getCepDestinatario() 					+ "|")	//CEP do Destinatário						(18)
		.append(this.getNomeRemetente() 					+ "|")	//Nome do Remetente							(19)
		.append(this.getEnderecoRemetente() 				+ "|")	//Endereço do Remetente						(20)
		.append(this.getCidadeRemetente() 					+ "|")	//Cidade do Remetente						(21)
		.append(this.getUfRemetente()						+ "|")	//UF do Remetente							(22)
		.append(this.getCepRemetente())								//CEP do Remetente							(23)
		.toString();
		return arquivoServico;
	}
	
	public String montarRespostaDevolucaoARs() {
		String arquivoServico = new StringBuffer()				
		.append(this.getTipoRegistro() 						+ "|")	//Tipo de Registro
		.append(this.getIdPendencia() 						+ "|")	//Código do Objeto Cliente
		.append(this.getNumeroLote() 						+ "|")	//Número do Lote
		.append("A"					 						+ "|")	//Resposta Notificação
		.append(this.getNumeroEtiqueta() 					+ "|")	//Número da Etiqueta
		.append("1"					 						+ "|")	//Código do Motivo da Devolução AR
		.append("Entrega de imagem confirmada")						//Descrição do Motivo da Devolução AR
		.toString();
		return arquivoServico;
	}
	
	public static boolean isUsuarioCorrigir(int codigoBaixa) {
		if(CodigoInconsistencia.CEP_INVALIDO.getCodigo() == codigoBaixa || 
		   CodigoInconsistencia.CEP_INVALIDO_OBJETO_REJEITADO.getCodigo()  == codigoBaixa || 
		   CodigoInconsistencia.CEP_DIVERGENTE_UF.getCodigo()  == codigoBaixa || 
		   CodigoInconsistencia.CEP_DIVERGENTE_UF_OBJETO_REJEITADO.getCodigo() == codigoBaixa|| 
		   CodigoInconsistencia.UNIDADE_DISTRIBUICAO_INDISPONIVEL.getCodigo()  == codigoBaixa) {
			return true;
		}
		return false;
	}
	
	private String removerMarcasDiacriticas(String texto) {
		texto = texto.replaceAll("a&#770;", "â");
		texto = texto.replaceAll("A&#770;", "Â");
		texto = texto.replaceAll("A&#769;", "Á");
		texto = texto.replaceAll("a&#769;", "á");
		texto = texto.replaceAll("A&#771;", "Ã");
		texto = texto.replaceAll("a&#771;", "ã");
		texto = texto.replaceAll("E&#769;", "É");
		texto = texto.replaceAll("e&#769;", "é");
		texto = texto.replaceAll("E&#770;", "Ê");
		texto = texto.replaceAll("e&#770;", "ê");
		texto = texto.replaceAll("I&#769;", "Í");
		texto = texto.replaceAll("i&#769;", "í");
		texto = texto.replaceAll("O&#769;", "Ó");
		texto = texto.replaceAll("o&#769;", "ó");
		texto = texto.replaceAll("O&#771;", "Õ");
		texto = texto.replaceAll("o&#771;", "õ");
		texto = texto.replaceAll("O&#770;", "Ô");
		texto = texto.replaceAll("o&#770;", "ô");
		texto = texto.replaceAll("U&#769;", "Ú");
		texto = texto.replaceAll("u&#769;", "ú");
		texto = texto.replaceAll("c&#807;", "ç");
		texto = texto.replaceAll("C&#807;", "Ç");
		texto = texto.replaceAll("&#379;", "Z");
		texto = texto.replaceAll("&#8203;", "");
		texto = texto.replaceAll("&#8260;", "/");
		texto = texto.replaceAll("&#8725;", "/");
		texto = texto.replaceAll("&#8259;", "-");
		texto = texto.replaceAll("&#150;", "-");
		texto = texto.replaceAll(" &#769;", "´");
		texto = texto.replaceAll("&#1063;", "");
		texto = texto.replaceAll("&#1063;", "");
		texto = texto.replaceAll("&#\\d{2,7};", "");
		texto = texto.replaceAll("[\\p{C}]", "");		
		return texto;
	}

}