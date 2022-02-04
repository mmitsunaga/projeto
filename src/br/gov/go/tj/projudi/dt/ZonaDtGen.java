package br.gov.go.tj.projudi.dt;

public class ZonaDtGen extends Dados{

    private static final long serialVersionUID = 1375565848798816083L;
    private String Id_Zona;
	private String Zona;
	private String ZonaCodigo;
	private String CodigoTemp;
	
	public ZonaDtGen() {
		limpar();
	}

	public String getId()  {return Id_Zona;}
	public void setId(String valor ) {if(valor!=null) Id_Zona = valor;}
	public String getZona()  {return Zona;}
	public void setZona(String valor ) {if (valor!=null) Zona = valor;}
	public String getZonaCodigo()  {return ZonaCodigo;}
	public void setZonaCodigo(String valor ) {if (valor!=null) ZonaCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	public void copiar(ZonaDt objeto){
		if (objeto==null) return;
		Id_Zona = objeto.getId();
		Zona = objeto.getZona();
		ZonaCodigo = objeto.getZonaCodigo();
		CodigoTemp = objeto.getCodigoTemp();		
	}

	public void limpar(){
		Id_Zona="";
		Zona="";
		ZonaCodigo="";
		CodigoTemp="";		
	}

	public String getPropriedades(){
		return "[Id_Zona:" + Id_Zona + ";Zona:" + Zona + ";ZonaCodigo:" + ZonaCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}
} 