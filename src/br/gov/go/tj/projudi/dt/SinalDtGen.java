package br.gov.go.tj.projudi.dt;

public class SinalDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -510802324372243318L;
    private String Id_Sinal;
	private String Sinal;


	private String CodigoTemp;

//---------------------------------------------------------
	public SinalDtGen() {

		limpar();

	}

	public String getId()  {return Id_Sinal;}
	public void setId(String valor ) {if(valor!=null) Id_Sinal = valor;}
	public String getSinal()  {return Sinal;}
	public void setSinal(String valor ) {if (valor!=null) Sinal = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(SinalDt objeto){
		Id_Sinal = objeto.getId();
		Sinal = objeto.getSinal();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Sinal="";
		Sinal="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Sinal:" + Id_Sinal + ";Sinal:" + Sinal + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
