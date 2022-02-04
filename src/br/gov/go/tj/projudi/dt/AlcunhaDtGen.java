package br.gov.go.tj.projudi.dt;

public class AlcunhaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5300081710777423201L;
    private String Id_Alcunha;
	private String Alcunha;


	private String CodigoTemp;

//---------------------------------------------------------
	public AlcunhaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Alcunha;}
	public void setId(String valor ) {if(valor!=null) Id_Alcunha = valor;}
	public String getAlcunha()  {return Alcunha;}
	public void setAlcunha(String valor ) {if (valor!=null) Alcunha = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(AlcunhaDt objeto){
		Id_Alcunha = objeto.getId();
		Alcunha = objeto.getAlcunha();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Alcunha="";
		Alcunha="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Alcunha:" + Id_Alcunha + ";Alcunha:" + Alcunha + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
