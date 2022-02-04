package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class ServentiaSubtipoDt extends ServentiaSubtipoDtGen {

    /**
     * 
     */
    private static final long serialVersionUID = 5627915948935474792L;

    public static final int CodigoPermissao = 224;

    public static final int JUIZADO_ESPECIAL_CIVEL = 1;
    public static final int JUIZADO_ESPECIAL_CRIMINAL = 2;
    public static final int JUIZADO_ESPECIAL_CIVEL_CRIMINAL = 3;
    public static final int JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA = 21;
    public static final int JUIZADO_ESPECIAL_FAZENDA_PUBLICA = 22;
    public static final int TURMA_RECURSAL_CIVEL = 4;
    public static final int TURMA_RECURSAL_CRIMINAL = 5;
    public static final int TURMA_RECURSAL_CIVEL_CRIMINAL = 6;
    public static final int FAZENDA_PUBLICA_MUNICIPAL = 36;
    public static final int FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL = 7;
    public static final int FAZENDA_PUBLICA_MUNICIPAL_INTERIOR = 37;    
    public static final int FAZENDA_PUBLICA_ESTADUAL = 32;
    public static final int FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL = 8;
    public static final int FAZENDA_PUBLICA_ESTADUAL_INTERIOR = 35;
    public static final int FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL = 12;
    public static final int FAMILIA_INTERIOR = 9;
    public static final int PLANTAO_SEGUNDO_GRAU = 10;
    public static final int VARAS_EXECUCAO_PENAL = 11;
    public static final int PRECATORIA = 13;
    
    public static final int CAMARA_CIVEL = 14;
    public static final int CAMARA_CRIMINAL = 16;
    public static final int SECAO_CIVEL = 17;
    public static final int CORTE_ESPECIAL = 18;
    
    public static final int MP_PRIMEIRO_GRAU = 19;
    public static final int MP_SEGUNDO_GRAU = 20;
    public static final int MP_TURMA_JULGADORA = 29;
    public static final int MP_FEDERAL = 51;
    public static final int MP_TRABALHISTA = 52;
    
    public static final int ID_PROMOTORIA_SEGUNDO_GRAU = 24; //Sugerido pelo Jesus
    public static final int VARAS_CIVEL = 27;
    public static final int MALOTE_DIGITAL = 28;
    
    
    public static final int BANCA_CONCILIACAO = 32;
    public static final int REDISTRIBUICAO_PROCESSUAL = 41;
    public static final int EXECPENWEB = 34;
    public static final int PREPROCESSUAL = 33;
    public static final int NUPEMEC = 38;
    public static final int JUIZADO_VIOLENCIA_DOMESTICA = 39;
    public static final int PROTOCOLO_JUDICIAL = 40;
    public static final int CONSELHO_SUPERIOR_MAGISTRATURA = 41;
    public static final int PLANTAO_PRIMEIRO_GRAU = 42;
    public static final int PLANTAO_AUDIENCIA_CUSTODIA = 59;
    public static final int INFANCIA_JUVENTUDE_CIVEL = 43;
    public static final int INFANCIA_JUVENTUDE_INFRACIONAL = 44;
    public static final int AUDITORIA_MILITAR_CIVEL = 45;
    public static final int AUDITORIA_MILITAR_CRIMINAL = 60;
    public static final int SECAO_CRIMINAL = 46;
    public static final int VARA_CRIMINAL = 47;
    public static final int FAMILIA_CAPITAL = 48;
    public static final int UPJ_SUCESSOES = 49;
    public static final int CENTRAL_MANDADOS = 50;
    public static final int GABINETE_PRESIDENCIA_ORGAO = 53;
    public static final int AMBIENTAL = 56;
    
    public static final int GABINETE_SEGUNDO_GRAU = 15;
    public static final int GABINETE_PRESIDENCIA_TJGO = 30;
    public static final int GABINETE_VICE_PRESIDENCIA_TJGO = 31;
    public static final int GABINETE_FLUXO_UPJ = 57;
	
    public static final int UPJ_FAMILIA = 58;
    public static final int UPJ_CRIMINAL = 61;
    public static final int UPJ_CUSTODIA = 62;
    public static final int VARAS_EXECUCAO_PENAL_ALTERNATIVA = 63;
    public static final int UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA = 64;
    public static final int ESTOQUE = 65;
    public static final int UPJ_TURMA_RECURSAL = 66;
    public static final int UPJ_VIOLENCIA_DOMESTICA= 67;
    
    public static boolean isTurma(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        if ( subTipoCodigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL ||
        	subTipoCodigo == ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL ||        	
        	subTipoCodigo == ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL) {
            return true;
        }
       return false;
    }
    
    public static boolean isSegundoGrau(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        if (subTipoCodigo == ServentiaSubtipoDt.CAMARA_CIVEL ||
        	subTipoCodigo == ServentiaSubtipoDt.CAMARA_CRIMINAL ||
        	subTipoCodigo == ServentiaSubtipoDt.SECAO_CIVEL ||
        	subTipoCodigo == ServentiaSubtipoDt.SECAO_CRIMINAL ||
        	subTipoCodigo == ServentiaSubtipoDt.PLANTAO_SEGUNDO_GRAU ||            
        	subTipoCodigo == ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA ||
        	subTipoCodigo == ServentiaSubtipoDt.CORTE_ESPECIAL) {
            return true;
        }
       return false;
    }
    
    public static boolean isUPJFamilia(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        return subTipoCodigo == ServentiaSubtipoDt.UPJ_FAMILIA;
    }
    
    public static boolean isUPJSucessoes(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        return subTipoCodigo == ServentiaSubtipoDt.UPJ_SUCESSOES;
    }
    
    public static boolean isUPJCriminal(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        return (subTipoCodigo == ServentiaSubtipoDt.UPJ_CRIMINAL
        		|| subTipoCodigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA
        		|| subTipoCodigo == ServentiaSubtipoDt.UPJ_CUSTODIA
        		);
    }
    
    public static boolean isUPJCustodia(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        return subTipoCodigo == ServentiaSubtipoDt.UPJ_CUSTODIA;
    }
    
    public static boolean isUPJJuizadoEspecialFazendaPublica(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        return subTipoCodigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA;
    }
    
    public static boolean isUPJTurmaRecursal(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        return subTipoCodigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL;
    }
    
	public static boolean isPrimeiroGrau(String serventiaSubTipoCodigo) {
		int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
		if (subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL ||
			subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL ||
			subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL ||
			subTipoCodigo == ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA ||
			subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA ||
			subTipoCodigo == ServentiaSubtipoDt.VARAS_CIVEL || 
			subTipoCodigo == ServentiaSubtipoDt.VARA_CRIMINAL ||
			subTipoCodigo == ServentiaSubtipoDt.UPJ_CRIMINAL  ||
			subTipoCodigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA ||
			subTipoCodigo == ServentiaSubtipoDt.UPJ_CUSTODIA  ||
			subTipoCodigo == ServentiaSubtipoDt.UPJ_FAMILIA   ||
			subTipoCodigo == ServentiaSubtipoDt.UPJ_SUCESSOES ||
			subTipoCodigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA ||
			subTipoCodigo == ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL ||
			subTipoCodigo == ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL ||
			subTipoCodigo == ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL){
			return true;
		}
		return false;
	}
    
    
    public static boolean isFazenda(String serventiaSubTipoCodigo){
    	int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
        if (subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL||
        	subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL||
        	subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR||
        	subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL ||
        	subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL ||
        	subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR ||
        	subTipoCodigo == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL) {
            return true;
        }
       return false;
    }

	public static boolean isGabineteSegundoGrau(String serventiaSubtipoCodigo) {
		int serventiaSubTipoCodigo = Funcoes.StringToInt(serventiaSubtipoCodigo);
		if (	serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_SEGUNDO_GRAU	|| 
				serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO || 
				serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO ||
				serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_ORGAO) {
			return true;
		}
		return false;
	}
	
	public static boolean isGabineteFluxoUPJ(String serventiaSubtipoCodigo) {
		int serventiaSubTipoCodigo = Funcoes.StringToInt(serventiaSubtipoCodigo);
		if (serventiaSubTipoCodigo == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
			return true;
		}
		return false;
	}

	public static boolean isUPJs(String serventiaSubTipoCodigo) {
		int subTipoCodigo = Funcoes.StringToInt(serventiaSubTipoCodigo,-1);
		return  subTipoCodigo == ServentiaSubtipoDt.UPJ_CUSTODIA || 
				subTipoCodigo == ServentiaSubtipoDt.UPJ_CRIMINAL ||
				subTipoCodigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA ||
			    subTipoCodigo == ServentiaSubtipoDt.UPJ_SUCESSOES ||
			    subTipoCodigo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA ||
			    subTipoCodigo == ServentiaSubtipoDt.UPJ_FAMILIA ||
			    subTipoCodigo == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL ;
	}
    
}

