package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class EscalaDt extends EscalaDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -1195212994765477130L;
    public static final int CodigoPermissao=177;
    
    public static final Integer ATIVO = 1;
    public static final Integer TIPO_ESPECIAL_PLANTAO = 1;
    public static final Integer TIPO_ESPECIAL_ADHOC = 2;
    public static final Integer TIPO_ESPECIAL_NORMAL = 0;
    public static final Integer INATIVO = 0;
//

    public boolean isPlantao(){
//    	if(String.valueOf(TIPO_ESPECIAL_PLANTAO).equals(this.getTipoEspecial()))
//    		return true;
//    	else
//    		return false;
    	
    	if(Funcoes.StringToInt(this.getMandadoTipoCodigo()) == MandadoTipoDt.PLANTAO)
    		return true;
    	else
    		return false;
    }
    
    public void defineTipoEspecialPlantao(){
    	this.setTipoEspecial(TIPO_ESPECIAL_PLANTAO.toString());
    }
    
    public boolean isAdhoc(){
    	if(String.valueOf(TIPO_ESPECIAL_ADHOC).equals(this.getTipoEspecial()))
    		return true;
    	else
    		return false;
    }
    
    public void defineTipoEspecialAdHoc(){
    	this.setTipoEspecial(TIPO_ESPECIAL_ADHOC.toString());
    }
    
    public boolean isTipoEspecialNormal(){
    	if(String.valueOf(TIPO_ESPECIAL_NORMAL).equals(this.getTipoEspecial()))
    		return true;
    	else
    		return false;
    }
    
    public void defineTipoEspecialNormal(){
    	this.setTipoEspecial(TIPO_ESPECIAL_NORMAL.toString());
    }
    
    public boolean isAssistencia(){
    	if(this.getEscalaTipoCodigo() != null && this.getEscalaTipoCodigo().equals(EscalaTipoDt.ASSISTENCIA))
    		return true;
    	else
    		return false;
    }
    
}
