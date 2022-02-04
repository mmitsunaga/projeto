package br.gov.go.tj.projudi.dt;

public class PaisDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3786398712470981194L;
    private String Id_Pais;
	private String Pais;


	private String PaisCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public PaisDtGen() {

		limpar();

	}

	public String getId()  {return Id_Pais;}
	public void setId(String valor ) {if(valor!=null) Id_Pais = valor;}
	public String getPais()  {return Pais;}
	public void setPais(String valor ) {if (valor!=null) Pais = valor;}
	public String getPaisCodigo()  {return PaisCodigo;}
	public void setPaisCodigo(String valor ) {if (valor!=null) PaisCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PaisDt objeto){
		Id_Pais = objeto.getId();
		Pais = objeto.getPais();
		PaisCodigo = objeto.getPaisCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Pais="";
		Pais="";
		PaisCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Pais:" + Id_Pais + ";Pais:" + Pais + ";PaisCodigo:" + PaisCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
