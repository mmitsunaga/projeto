package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.projudi.ne.GuiaCalculoNe;

public class ZonaDt extends ZonaDtGen{

    private static final long serialVersionUID = 5502269395604369356L;
    public static final int CodigoPermissao=172;

    private String Id_Comarca;
    private String Comarca;
    private String DataInicio;
    private String Id_UsuarioServentia;        
    private String ValorCivel;
    private String ValorCriminal;
    private String ValorContaVinculada;
    private String ValorSegundoGrau;
    private String ValorSegundoGrauContadoria;
    
    public String getId_Comarca() {
		return Id_Comarca;
	}
    
	public void setId_Comarca(String Id_Comarca) {
		if (Id_Comarca!=null) {
			this.Id_Comarca = Id_Comarca;
		}
	}
	
	public String getComarca() {
		return Comarca;
	}
    
	public void setComarca(String Comarca) {
		if (Comarca!=null) {
			this.Comarca = Comarca;
		}
	}
	
	public String getDataInicio() {
		return DataInicio;
	}
    
	public void setDataInicio(String DataInicio) {
		if (DataInicio!=null) {
			this.DataInicio = DataInicio;
		}
	}
	
	public String getId_UsuarioServentia() {
		return Id_UsuarioServentia;
	}
    
	public void setId_UsuarioServentia(String Id_UsuarioServentia) {
		if (Id_UsuarioServentia!=null) {
			this.Id_UsuarioServentia = Id_UsuarioServentia;
		}
	}
    
    public String getValorCivel() {
		return ValorCivel;
	}
    
	public void setValorCivel(String valorCivel) {
		if (valorCivel!=null) {
			this.ValorCivel = valorCivel;
		}
	}
	
	public String getValorCriminal() {
		return ValorCriminal;
	}
	
	public void setValorCriminal(String valorCriminal) {
		if (valorCriminal!=null){
			this.ValorCriminal = valorCriminal;
		}
	}
	
	public String getValorContaVinculada() {
		return ValorContaVinculada;
	}
	
	public void setValorContaVinculada(String valorContaVinculada) {
		if (valorContaVinculada!=null){
			this.ValorContaVinculada = valorContaVinculada;
		}
	}
	
	public String getValorSegundoGrau() {
		return ValorSegundoGrau;
	}
	
	public void setValorSegundoGrau(String valorSegundoGrau) {
		if (valorSegundoGrau!=null){
			this.ValorSegundoGrau = valorSegundoGrau;
		}
	}
	
	public String getValorSegundoGrauContadoria() {
		return ValorSegundoGrauContadoria;
	}
	
	public void setValorSegundoGrauContadoria(String valorSegundoGrauContadoria) {
		if (valorSegundoGrauContadoria!=null){
			this.ValorSegundoGrauContadoria = valorSegundoGrauContadoria;
		}
	}
	
	public void limpar(){
		super.limpar();
		Id_Comarca = "";
		Comarca = "";
		DataInicio = "";
		Id_UsuarioServentia = "";
		ValorCivel = "";
	    ValorCriminal = "";
	    ValorContaVinculada = "";
	    ValorSegundoGrau = "";
	    ValorSegundoGrauContadoria = "";
	}
	
	public void copiar(ZonaDt objeto){
		if (objeto==null) return;
		super.copiar(objeto);
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		DataInicio = objeto.getDataInicio();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		ValorCivel = objeto.getValorCivel();
	    ValorCriminal = objeto.getValorCriminal();
	    ValorContaVinculada = objeto.getValorContaVinculada();
	    ValorSegundoGrau = objeto.getValorSegundoGrau();
	    ValorSegundoGrauContadoria = objeto.getValorSegundoGrauContadoria();			
	}

	public String getPropriedades(){
		return super.getPropriedades() + "[Id_Comarca:" + Id_Comarca + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";DataInicio:" + DataInicio + ";Comarca:" + Comarca + ";ValorCivel:" + ValorCivel + ";valorCriminal:" + ValorCriminal + ";ValorContaVinculada:" + ValorContaVinculada + ";valorSegundoGrau:" + ValorSegundoGrau + ";ValorSegundoGrauContadoria:" + ValorSegundoGrauContadoria + "]";
	}
	
	public String getValor(int valorLocomocao) {
    	String retorno = null;
    	
    	switch(valorLocomocao) {
    		case GuiaCalculoNe.LOCOMOCAO_CIVEL : {
    			retorno = getValorCivel();
    			break;
    		}
    		
    		case GuiaCalculoNe.LOCOMOCAO_CRIMINAL : {
    			retorno = getValorCriminal();
    			break;
    		}
    		
    		case GuiaCalculoNe.LOCOMOCAO_CONTA_VINCULADA : {
    			retorno = getValorContaVinculada();
    			break;
    		}
    		
    		case GuiaCalculoNe.LOCOMOCAO_SEGUNDO_GRAU : {
    			retorno = getValorSegundoGrau();
    			break;
    		}
    		
    		case GuiaCalculoNe.LOCOMOCAO_SEGUNDO_GRAU_CONT : {
    			retorno = getValorSegundoGrauContadoria();
    			break;
    		}
    		
    		default : {
    			retorno = getValorCivel();
    			break;
    		}
    	}
    	
    	return retorno;
    }
	
	@Override
	public boolean equals(Object zonaObj) {		
		if (zonaObj == null || ! (zonaObj instanceof ZonaDt)) return false; 
		
		ZonaDt zonaDt = (ZonaDt) zonaObj;
		
		return zonaDt.getId().equalsIgnoreCase(this.getId()) &&
			   zonaDt.getId_Comarca().equalsIgnoreCase(this.getId_Comarca()) &&
			   zonaDt.getValorCivel().equalsIgnoreCase(this.getValorCivel()) &&
			   zonaDt.getValorContaVinculada().equalsIgnoreCase(this.getValorContaVinculada()) &&
			   zonaDt.getValorCriminal().equalsIgnoreCase(this.getValorCriminal()) &&
			   zonaDt.getValorSegundoGrau().equalsIgnoreCase(this.getValorSegundoGrau()) &&
			   zonaDt.getValorSegundoGrauContadoria().equalsIgnoreCase(this.getValorSegundoGrauContadoria());
	}
}
