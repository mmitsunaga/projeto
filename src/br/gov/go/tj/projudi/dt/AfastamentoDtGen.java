package br.gov.go.tj.projudi.dt;

public class AfastamentoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -2669125562944424561L;
    private String Id_Afastamento;
	private String Afastamento;
	
	


	

//---------------------------------------------------------
	public AfastamentoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Afastamento;}
	public void setId(String valor ) {if(valor!=null) Id_Afastamento = valor;}
	public String getAfastamento()  {return Afastamento;}
	public void setAfastamento(String valor ) {if (valor!=null) Afastamento = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(AfastamentoDt objeto){
		Id_Afastamento = objeto.getId();
		Afastamento = objeto.getAfastamento();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Afastamento="";
		Afastamento="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Afastamento:" + Id_Afastamento + ";Afastamento:" + Afastamento + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
