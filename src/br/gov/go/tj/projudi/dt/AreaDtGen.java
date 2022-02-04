package br.gov.go.tj.projudi.dt;

public class AreaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4024550879246876521L;
    private String Id_Area;
	private String Area;


	private String AreaCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public AreaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Area;}
	public void setId(String valor ) {if(valor!=null) Id_Area = valor;}
	public String getArea()  {return Area;}
	public void setArea(String valor ) {if (valor!=null) Area = valor;}
	public String getAreaCodigo()  {return AreaCodigo;}
	public void setAreaCodigo(String valor ) {if (valor!=null) AreaCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(AreaDt objeto){
		Id_Area = objeto.getId();
		Area = objeto.getArea();
		AreaCodigo = objeto.getAreaCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Area="";
		Area="";
		AreaCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Area:" + Id_Area + ";Area:" + Area + ";AreaCodigo:" + AreaCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
