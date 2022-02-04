package br.gov.go.tj.projudi.dt;

public class GuiaNumeroDt extends Dados {

	public static final int CodigoPermissao=-1;
	
    private static final long serialVersionUID = 645753423433822960L;
    private String CodigoTemp;
    private String Id_GuiaNumero;
    private String Localizador;

    public GuiaNumeroDt(String data) {
        setLocalizador(data);
    }

    public GuiaNumeroDt() {
        
    }
    
	public String getId()  {return Id_GuiaNumero;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaNumero = valor;}
	public String getLocalizador()  {return Localizador;}
	public void setLocalizador(String valor ) {if (valor!=null) Localizador = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(GuiaNumeroDt objeto){
		 if (objeto==null) return;
		 Id_GuiaNumero = objeto.getId();
		 Localizador = objeto.getLocalizador();
		 CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GuiaNumero="";
		Localizador="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GuiaNumero:" + Id_GuiaNumero + ";Localizador:" + Localizador + ";CodigoTemp:" + CodigoTemp + "]";
	}
}
