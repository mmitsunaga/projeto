package br.gov.go.tj.projudi.dt;

public class ComarcaDt extends ComarcaDtGen{

    private static final long serialVersionUID = -4682447354526226075L;
    public static final int CodigoPermissao=163;
    
    //Id's
    public static final String ID_GOIANIA = "12";
    
    //Códigos
    public static final String ANAPOLIS = "6";
    public static final String APARECIDA_DE_GOIANIA = "8";
    public static final String GOIANIA 				= "39";
    public static final String SENADOR_CANEDO		= "122";
    
    
	public boolean temCentralProjudiImplantada(String codigo) {
		// Comarcas atualmente implantadas:
		//			- Senador Caneado (código: 122)
		if(codigo != null && codigo.equals(SENADOR_CANEDO)){
			return true;
		}
		else {
			return false;
		}
	}
}
