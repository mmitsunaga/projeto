package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class ServentiaTipoDt extends ServentiaTipoDtGen {

    private static final long serialVersionUID = -2785559180289454900L;

    public static final int CodigoPermissao = 169;

	public static final int ADMINISTRACAO_SISTEMA_PROJUDI = 1;
	public static final int CENTRAL_MANDADOS = 2;
	public static final int CONSULTORIA = 3;
	public static final int CONTADORIA = 4;
	public static final int CORREGEDORIA = 5;
	public static final int DELEGACIA = 6;
	public static final int GERENCIAMENTO_SISTEMA_PROJUDI = 7;
	public static final int ORDEM_ADVOGADOS_BRASIL = 8;
	public static final int PARTE = 9;
	public static final int DEFENSORIA_PUBLICA = 10;
	public static final int PROCURADORIA_GERAL_ESTADO = 11;
	public static final int PROCURADORIA_GERAL_MUNICIPAL = 12;
	public static final int PROMOTORIA = 13;
	public static final int SISTEMA_PROJUDI = 14;
	public static final int SEGUNDO_GRAU = 15;
	public static final int VARA = 16;
	public static final int PROCURADORIA_UNIAO = 17;
	public static final int DISTRIBUIDOR = 18;
	public static final int VARA_EXTRAJUDICIAL = 19;
	public static final int PRESIDENCIA = 20;
	public static final int INFORMATICA = 21;
	public static final int GABINETE = 22;
	public static final int EQUIPE_INTERPROFISSIONAL = 23;
	public static final int ADVOGADO_PUBLICO_AUTARQUIAS = 24;
	public static final int UNIDADES_ADMINISTRATIVAS = 25;
	public static final int BANCO = 26;
	public static final int DIRETORIA_FINANCEIRA = 30;
	public static final int ESCRITORIO_JURIDICO = 31;
	public static final int PUBLICO = 32;
	public static final int NUPEMEC = 33;
	public static final int CAMARA_SAUDE = 34;
	public static final int INTELIGENCIA = 35;
	public static final int JUSTICA_RESTAURATIVA = 36;
	public static final int POSTAGEM = 37;
	public static final int ESTOQUE = 38;
	
    public static boolean isProcuradoria(String serventiaTipoCodigo){
        if ( Funcoes.StringToInt(serventiaTipoCodigo)== ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL ||
            Funcoes.StringToInt(serventiaTipoCodigo)==  ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO ||
            Funcoes.StringToInt(serventiaTipoCodigo)==  ServentiaTipoDt.PROCURADORIA_UNIAO)
            return true;
       return false;
    }
    
    public static boolean isGabineteDesembargador(String serventiaTipoCodigo){
    	if (Funcoes.StringToInt(serventiaTipoCodigo)==GABINETE){
    		return true;
    	}
    	
    	return false;
    }
    
    public static boolean isSegundoGrau(String serventiaTipoCodigo){
    	if (Funcoes.StringToInt(serventiaTipoCodigo)==SEGUNDO_GRAU){
    		return true;
    	}
    	
    	return false;
    }

}