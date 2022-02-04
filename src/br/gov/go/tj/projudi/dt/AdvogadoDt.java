package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class AdvogadoDt extends UsuarioDt {

	/**
     * 
     */
    private static final long serialVersionUID = -137401362517777299L;

    public static final int CodigoPermissao = 164;

	public static final int ADVOGADO_PARTICULAR = 1;
	public static final int DEFENSOR_PUBLICO = 2;
	public static final int PROCURADOR_MUNICIPAL = 3;
	public static final int PROCURADOR_ESTADUAL = 4;
	public static final int PROCURADOR_UNIAO = 5;
	public static final int ADVOGADO_PUBLICO = 6;
	
	private static final String [] stTiposAdvogado = {"", "Advogado Particular", "Defensor Público", "Procurador Municipal", "Procurador Estadual", "Procurador da União", "Advogado Público"};

	public static String getDescricao(String codigo) {			
		
		return  stTiposAdvogado[Funcoes.StringToInt(codigo)];
		
	}
	public String[] getTiposAdvogado(){
		return stTiposAdvogado;
	}
}
