package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class GrupoTipoDt extends GrupoTipoDtGen{

    private static final long serialVersionUID = 8593678512723296297L;
    public static final int CodigoPermissao=603;

    public static final int MAGISTRADO_PRIMEIRO_GRAU = 1;
    public static final int JUIZ_TURMA = 2;
	public static final int ASSISTENTE_GABINETE = 4;
	public static final int JUIZ_LEIGO = 61;
	
	public static final int ANALISTA_TI = 7;
	public static final int ANALISTA_VARA = 8;
	public static final int ANALISTA_TURMA_SEGUNDO_GRAU = 9;
	public static final int ANALISTA_EXECUCAO = 27;
	public static final int ANALISTA_COORDENARODIRA_JUDICIARIA = 34;
	
	public static final int AUTORIDADE_POLICIAL = 10;	
	public static final int CONTADOR = 11;	
	public static final int CONSULTOR = 12;	
	public static final int MALOTE_DIGITAL = 13;	
	
	public static final int TECNICO_VARA = 14;	
	public static final int TECNICO_TURMA_SEGUNDO_GRAU = 15;	
	public static final int TECNICO = 16;
	
	public static final int ADVOGADO = 17;	
	public static final int MP = 18;
	
	public static final int DISTRIBUIDOR = 19;
	public static final int DISTRIBUIDOR_CAMARA = 20;
	public static final int DISTRIBUIDOR_GABINETE = 21;
	
	public static final int GERAL = 22;
	public static final int CONCILIADOR_VARA = 23;
	public static final int ADMINISTRADOR = 24;
	public static final int PARTE = 25;
	public static final int MAGISTRADO_SEGUNDO_GRAU = 26;
	public static final int JUIZ_AUXILIAR = 28;
	public static final int CONTADOR_MUNICIPAL = 29;
	
	public static final int COORDENADOR_PROMOTORIA = 30;
	
	public static final int COORDENADOR_PROCURADORIA = 31;
	public static final int COORDENADOR_DEFENSORIA_PUBLICA = 84;
	
	public static final int ASSESSOR_JUIZ_VARA_TURMA = 3;
	public static final int ASSESSOR_ADVOGADO = 5;
	public static final int ASSESSOR_MP = 87;
	public static final int ASSESSOR = 6;
	public static final int ASSESSOR_PRESIDENTE_SEGUNDO_GRAU = 33;
	public static final int ASSESSOR_DESEMBARGADOR = 35;

	public static final int COORDENADOR_CENTRAL_MANDADO = 43;
	public static final int COORDENADOR_ESCRITORIO_JURIDICO = 44;
	public static final int COORDENADOR_ADVOCACIA_PUBLICA = 85;
	
	public static final int PRESIDENTE_SEGUNDO_GRAU = 32;
	public static final int DIVISAO_RECURSOS_CONSTITUCIONAIS = 38;
	public static final int OUVIDOR = 39;
	public static final int PRESIDENCIA = 40;
	public static final int GERENCIAMENTO_SEGUNDO_GRAU = 41;
	public static final int OFICIAL_JUSTICA = 42;
	public static final int DIRETORIA_FINANCEIRA = 46;
	public static final int USUARIO_INTERNO_JUDICIARIO = 47;
	public static final int ESTAGIARIO = 82;
	public static final int NUPEMEC = 54;
	
	public static final int ASSISTENTE_GABINETE_FLUXO = 86;
	
	public static final int INTELIGENCIA = 62;
	
	public static final int ANALISTA_ARQUIVAMETNO = 64;	
	
	//Grupo População definido apenas para ser utilizado na consulta pública
	public static final int POPULACAO = -100;
	
	public static boolean podeAnalisarOuPreanalisarVotoEmenta(String grupoTipoCodigo) {
		int grupoTipoCodigoInt = Funcoes.StringToInt(grupoTipoCodigo, -1);
		
		switch(grupoTipoCodigoInt) {
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
			case GrupoTipoDt.ASSISTENTE_GABINETE:
			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
		    	 return true;
		}
		     
		return false;   
	}

}
