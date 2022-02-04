package br.gov.go.tj.projudi.dt;

public class PropriedadeDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -7543058904409330438L;
    private String Id_Propriedade;
	private String Propriedade;


	private String PropriedadeCodigo;
	private String Valor;
	private String CodigoTemp;

//---------------------------------------------------------
	public PropriedadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_Propriedade;}
	public void setId(String valor ) {if(valor!=null) Id_Propriedade = valor;}
	public String getPropriedade()  {return Propriedade;}
	public void setPropriedade(String valor ) {if (valor!=null) Propriedade = valor;}
	public String getPropriedadeCodigo()  {return PropriedadeCodigo;}
	public void setPropriedadeCodigo(String valor ) {if (valor!=null) PropriedadeCodigo = valor;}
	public String getValor()  {return Valor;}
	public void setValor(String valor ) {if (valor!=null) Valor = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PropriedadeDt objeto){
		Id_Propriedade = objeto.getId();
		Propriedade = objeto.getPropriedade();
		PropriedadeCodigo = objeto.getPropriedadeCodigo();
		Valor = objeto.getValor();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Propriedade="";
		Propriedade="";
		PropriedadeCodigo="";
		Valor="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Propriedade:" + Id_Propriedade + ";Propriedade:" + Propriedade + ";PropriedadeCodigo:" + PropriedadeCodigo + ";Valor:" + Valor + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
