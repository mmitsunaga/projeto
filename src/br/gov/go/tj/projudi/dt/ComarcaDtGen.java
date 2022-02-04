package br.gov.go.tj.projudi.dt;

public class ComarcaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 7584712875822456781L;
    private String Id_Comarca;
	private String Comarca;
	
	


	private String ComarcaCodigo;
	

//---------------------------------------------------------
	public ComarcaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Comarca;}
	public void setId(String valor ) {if(valor!=null) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(ComarcaDt objeto){
		if (objeto != null){
			Id_Comarca = objeto.getId();
			Comarca = objeto.getComarca();
			ComarcaCodigo = objeto.getComarcaCodigo();
			CodigoTemp = objeto.getCodigoTemp();
		}
	}

	public void limpar(){
		Id_Comarca="";
		Comarca="";
		ComarcaCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";ComarcaCodigo:" + ComarcaCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
