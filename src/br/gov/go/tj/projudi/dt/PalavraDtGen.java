package br.gov.go.tj.projudi.dt;

public class PalavraDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -3283044486804892869L;
    private String Id_Palavra;
	private String Palavra;


	private String CodigoTemp;

//---------------------------------------------------------
	public PalavraDtGen() {

		limpar();

	}

	public String getId()  {return Id_Palavra;}
	public void setId(String valor ) {if(valor!=null) Id_Palavra = valor;}
	public String getPalavra()  {return Palavra;}
	public void setPalavra(String valor ) {if (valor!=null) Palavra = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PalavraDt objeto){
		Id_Palavra = objeto.getId();
		Palavra = objeto.getPalavra();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Palavra="";
		Palavra="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Palavra:" + Id_Palavra + ";Palavra:" + Palavra + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
