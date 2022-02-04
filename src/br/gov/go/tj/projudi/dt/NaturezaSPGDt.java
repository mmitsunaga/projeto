package br.gov.go.tj.projudi.dt;

public class NaturezaSPGDt extends Dados {
	
	private static final long serialVersionUID = 878736926242316681L;
	
	public static final int CodigoPermissao = 847;
	
	public static final int BUSCA_APREENSAO 									= 161;
	public static final int BUSCA_APREENSAO_MENOR 								= 162;
	public static final int POSSE_EM_NOME_NASCITURO 							= 163;
	public static final int SEQUESTRO 											= 164;
	public static final int CONVERSAO_CONSENSUAL_SEPARACAO_DIVORCIO 			= 180;
	public static final int DIVORCIO_CONSENSIAL 								= 181;
	public static final int PARTILHA_CONSENSUAL_BENS_APOS_SEPARACAO 			= 182;
	public static final int SEPARACAO_CONSENSUAL 								= 183;
	public static final int EXECUCAO_PROVISORIA_SENTENCA 						= 203;
	public static final int EXECUCAO_HIPOTECARIA 								= 205;
	public static final int MANDADO_SEGURANCA 									= 209;
	public static final int BUSCA_APREENSAO_911_69 								= 210;
	public static final int ARROLAMENTO_CAUTELAR_DE_BENS 						= 224;
	public static final int CAUCAO_HERDEIROS 									= 227;
	public static final int CAUTELAR_CONCUBINATO_SEPARACAO_CORPOS 				= 229;
	public static final int GUARDA_RESPONSABILIDADE 							= 231;
	public static final int GUARDA_RESPONSABILIDADE_EDUCACAO_FILHOS 			= 232;
	public static final int POSSE_GUARDA_FILHOS 								= 237;
	public static final int POSSE_GUARDA_MENOR 									= 238;
	public static final int PRODUCAO_ANTECIPADA_PROVAS 							= 239;
	public static final int REGULAMENTACAO_VISITAS 								= 241;
	public static final int RESERVA_BENS 										= 242;
	public static final int REVOGACAO_MANDATO 									= 243;
	public static final int SEPARACAO_CORPOS 									= 244;
	public static final int SUSPENSAO_PATRIO_PODER 								= 245;
	public static final int SUSTACAO_PROTESTO 									= 246;
	public static final int VERIFICACAO_LIVROS 									= 247;
	public static final int VISTORIA_AD_PERPETUAM_REI_MEMORIAM 					= 248;
	public static final int CARTA_ORDEM 										= 276;
	public static final int CARTA_PRECATORIA 									= 277;
	public static final int BUSCA_APREENSAO_ALIENACAO_FIDUCIARIA_LEI_13043_2014 = 290;
	public static final int EXECUCAO_IPTU 										= 292;
	public static final int EXECUCAO_FISCAL 									= 293;
	public static final int TUTELA_ANTECIPADA_ANTECEDENTE 						= 298;
	public static final int TUTELA_CAUTELAR_ANTECEDENTE 						= 299;
	public static final int CARTA_ARBITRAL 										= 302;
	
	private String id_NaturezaSPG;
	private String naturezaSPG;
	private String naturezaSPGCodigo;
	
	@Override
	public void setId(String id) {
		this.id_NaturezaSPG = id;
	}
	@Override
	public String getId() {
		return id_NaturezaSPG;
	}
	
	public String getNaturezaSPG() {
		return naturezaSPG;
	}
	
	public void setNaturezaSPG(String naturezaSPG) {
		if (naturezaSPG != null) this.naturezaSPG = naturezaSPG;
	}
	
	public String getNaturezaSPGCodigo() {
		return naturezaSPGCodigo;
	}
	
	public void setNaturezaSPGCodigo(String naturezaSPGCodigo) {
		if (naturezaSPGCodigo != null) this.naturezaSPGCodigo = naturezaSPGCodigo;
	}
	
	public void limpar(){
		super.limpar();
		id_NaturezaSPG = "";
		naturezaSPG = "";
		naturezaSPGCodigo = "";		
	}
	
	public void copiar(NaturezaSPGDt objeto){
		if (objeto==null) return;
		naturezaSPG = objeto.getNaturezaSPG();
		naturezaSPGCodigo = objeto.getNaturezaSPGCodigo();
		super.setCodigoTemp(objeto.getCodigoTemp());
	}

	public String getPropriedades(){
		return "[Natureza:" + naturezaSPG + ";NaturezaCodigo:" + naturezaSPGCodigo + ";CodigoTemp:" + super.getCodigoTemp() + "]";
	}
}
