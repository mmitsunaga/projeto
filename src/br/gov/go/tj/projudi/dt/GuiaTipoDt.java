package br.gov.go.tj.projudi.dt;

import org.hamcrest.core.IsNull;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class GuiaTipoDt extends GuiaTipoDtGen{

    private static final long serialVersionUID = -7296181697397983200L;
    
    public static final int CodigoPermissao=517;
    
    public static final Integer ATIVO = 1;
    public static final Integer INATIVO = 0;
    
    public static final Integer PRIMEIRO_GRAU 	= 1;
    public static final Integer SEGUNDO_GRAU 	= 2;
    public static final String TURMA_RECURSAL   = "3";
    
    //** ID's da tabela Guia Tipo
    public static final String ID_INICIAL_PRIMEIRO_GRAU				= "1";
    public static final String ID_COMPLEMENTAR_PRIMEIRO_GRAU 		= "2";
    public static final String ID_LOCOMOCAO 				        = "3";
    public static final String ID_FINAL 							= "4";
    public static final String ID_RECURSO_INOMINADO 				= "5";
    public static final String ID_RECURSO 							= "6";
    public static final String ID_LOCOMOCAO_COMPLEMENTAR 	        = "7";
    public static final String ID_RECURSO_INOMINADO_QUEIXA_CRIME	= "10";
    public static final String ID_FINAL_EXECUCAO_SENTENCA 			= "11";
    public static final String ID_FINAL_EXECUCAO_QUEIXA_CRIME 		= "12";
    public static final String ID_FAZENDA_MUNICIPAL 				= "13";
    public static final String ID_FINAL_ZERO 						= "14";
    public static final String ID_CARTA_PRECATORIA 					= "15";
    public static final String ID_GUIA_GENERICA 					= "16";
    public static final String ID_CARTA_SENTENCA 					= "19";
    
    
    public static final String ID_INICIAL_SEGUNDO_GRAU 				= "22";
    public static final String ID_AGRAVO_REGIMENTAL 				= "24";
    public static final String ID_RECURSO_STJ 						= "25";
    public static final String ID_PREFEITURA_AUTOMATICA  			= "27";
    public static final String ID_FAZENDA_PUBLICA_AUTOMATICA 		= "28";
    public static final String ID_POSTAGEM 							= "29";
    public static final String ID_MEDIACAO 							= "30";
    public static final String ID_CONCILIACAO 						= "31";
    public static final String ID_RESOLUCAO_CONFLITO				= "33";
    
    public static final String ID_GUIA_DE_CERTIDAO_NARRATIVA		= "34";
    public static final String ID_GUIA_DE_CERTIDAO_INTERDICAO		= "35";
    public static final String ID_GUIA_DE_CERTIDAO_LICITACAO		= "36";
    public static final String ID_GUIA_DE_CERTIDAO_AUTORIA			= "37";    
    public static final String ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE	= "38";
    
    public static final String ID_SERVICOS							= "39";
    public static final String ID_COMPLEMENTAR_SEGUNDO_GRAU 		= "40";
    
    public static final String ID_GUIA_HOMOLOGACAO_ACORDO 			= "41";
    
    //*** Variáveis para SPG/SSG
    public static final String NOME_GUIA_SSG 						= "SSG";
    public static final String NOME_GUIA_SSG_INICIAL 				= "SSG - Inicial";
    public static final String NOME_GUIA_SSG_COMPLEMENTAR 			= "SSG - Complementar";
    
    public static final String NOME_GUIA_SPG 						= "SPG";
    public static final String NOME_GUIA_SPG_PUBLICACAO 			= "SPG - Publicação";
    public static final String NOME_GUIA_SPG_GRS 					= "SPG - GRS";
    public static final String NOME_GUIA_SPG_INICIAL 				= "SPG - Inicial";
    public static final String NOME_GUIA_SPG_COMPLEMENTAR 			= "SPG - Complementar";
    public static final String NOME_GUIA_SPG_JUDICIAL 				= "SPG - Judicial";
    public static final String NOME_GUIA_SPG_CONCURSO 				= "SPG - Concurso";
    public static final String NOME_GUIA_SPG_DEVOLUCAO 				= "SPG - Devolução";
    public static final String NOME_GUIA_SPG_EVENTO 				= "SPG - Evento";
    public static final String NOME_GUIA_SPG_PRECATORIO 			= "SPG - Precatório";
    public static final String NOME_GUIA_SPG_CERTIDAO 				= "SPG - Certidão";
    public static final String NOME_GUIA_SPG_DESARQUIVAMENTO 		= "SPG - Desarquivado";
    public static final String NOME_GUIA_SPG_FINAL_FAZENDA_PUBLICA 	= "SPG - Final Fazenda Pública";
    public static final String NOME_GUIA_SPG_INTERMEDIARIA 			= "SPG - Intermediária";
    public static final String NOME_GUIA_SPG_FINAL 					= "SPG - Final";
    public static final String NOME_GUIA_SPG_FINAL_ZERO 			= "SPG - Final Zero";
    public static final String NOME_GUIA_SPG_DEPOSITO_JUDICIAL 		= "SPG - Depósito Judicial";
    
    public static final String ID_INFO_CERTIDAO_SPG_NEG_POSITIVA    = "1"; 
    public static final String ID_INFO_CERTIDAO_SPG_NARRATIVA       = "2"; 
    public static final String ID_INFO_CERTIDAO_SPG_PRATICA_FORENSE = "3"; 
    public static final String ID_INFO_CERTIDAO_SPG_AUTORIA         = "4"; 
    public static final String ID_INFO_CERTIDAO_SPG_LICITACAO       = "5"; 
    public static final String ID_INFO_CERTIDAO_SPG_EXECUCAO        = "6"; 
    public static final String ID_INFO_CERTIDAO_SPG_INTERDICAO      = "7";
    
    private String GuiaTipoCodigoExterno;
    private String FlagGrau;

	public String getGuiaTipoCodigoExterno() {
		return GuiaTipoCodigoExterno;
	}
	
	public void setGuiaTipoCodigoExterno(String guiaTipoCodigoExterno) {
		GuiaTipoCodigoExterno = guiaTipoCodigoExterno;
	}
	
	public String getFlagGrau() {
		return FlagGrau;
	}
	
	public void setFlagGrau(String flagGrau) {
		FlagGrau = flagGrau;
	}
	
	public static boolean isGuiaCertidaoPraticaForense(CertidaoGuiaDt certidaoGuiaDt) {
		if (certidaoGuiaDt == null) return false;
		if (certidaoGuiaDt.getGuiaEmissaoDt() != null) return isGuiaCertidaoPraticaForense(certidaoGuiaDt.getGuiaEmissaoDt());
		return isGuiaTipo(ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE, certidaoGuiaDt.getId_GuiaTipo());
	}
	
	public static boolean isGuiaCertidaoPraticaForense(GuiaEmissaoDt guiaEmissao) {
		if (guiaEmissao == null) return false;
		if (guiaEmissao.isGuiaEmitidaSPG()) {
			return isGuiaTipo(ID_INFO_CERTIDAO_SPG_PRATICA_FORENSE, guiaEmissao.getTipoGuiaCertidaoSPG());
		} else {
			return isGuiaTipo(ID_GUIA_DE_CERTIDAO_PRATICA_FORENSE, guiaEmissao.getId_GuiaTipo());
		}
	}
	
	public static boolean isGuiaCertidaoNarrativa(CertidaoGuiaDt certidaoGuiaDt) {
		if (certidaoGuiaDt == null) return false;
		if (certidaoGuiaDt.getGuiaEmissaoDt() != null) return isGuiaCertidaoNarrativa(certidaoGuiaDt.getGuiaEmissaoDt());
		return isGuiaTipo(ID_GUIA_DE_CERTIDAO_NARRATIVA, certidaoGuiaDt.getId_GuiaTipo());
	}
	
	public static boolean isGuiaCertidaoNarrativa(GuiaEmissaoDt guiaEmissao) {
		if (guiaEmissao == null) return false;
		if (guiaEmissao.isGuiaEmitidaSPG()) {
			return isGuiaTipo(ID_INFO_CERTIDAO_SPG_NARRATIVA, guiaEmissao.getTipoGuiaCertidaoSPG());
		} else {
			return isGuiaTipo(ID_GUIA_DE_CERTIDAO_NARRATIVA, guiaEmissao.getId_GuiaTipo());
		}
	}
		
	private static boolean isGuiaTipo(String idGuiaTipoComparacao, String idGuiaTipo) {
		if (idGuiaTipo == null) return false;
		if (Funcoes.StringToInt(idGuiaTipoComparacao) == 0) return false;
		if (Funcoes.StringToInt(idGuiaTipo) == 0) return false;
		return Funcoes.StringToInt(idGuiaTipoComparacao) == Funcoes.StringToInt(idGuiaTipo);
	}
}
