package br.gov.go.tj.projudi.dt;

public class RegiaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5514693040010432541L;
    private String Id_Regiao;
	private String Regiao;


	private String RegiaoCodigo;
	private String Id_Comarca;
	private String Comarca;
	private String CodigoTemp;
	private String ComarcaCodigo;

//---------------------------------------------------------
	public RegiaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Regiao;}
	public void setId(String valor ) {if(valor!=null) Id_Regiao = valor;}
	public String getRegiao()  {return Regiao;}
	public void setRegiao(String valor ) {if (valor!=null) Regiao = valor;}
	public String getRegiaoCodigo()  {return RegiaoCodigo;}
	public void setRegiaoCodigo(String valor ) {if (valor!=null) RegiaoCodigo = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}


	public void copiar(RegiaoDt objeto){
		Id_Regiao = objeto.getId();
		Regiao = objeto.getRegiao();
		RegiaoCodigo = objeto.getRegiaoCodigo();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		CodigoTemp = objeto.getCodigoTemp();
		ComarcaCodigo = objeto.getComarcaCodigo();
	}

	public void limpar(){
		Id_Regiao="";
		Regiao="";
		RegiaoCodigo="";
		Id_Comarca="";
		Comarca="";
		CodigoTemp="";
		ComarcaCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Regiao:" + Id_Regiao + ";Regiao:" + Regiao + ";RegiaoCodigo:" + RegiaoCodigo + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";CodigoTemp:" + CodigoTemp + ";ComarcaCodigo:" + ComarcaCodigo + "]";
	}


} 
