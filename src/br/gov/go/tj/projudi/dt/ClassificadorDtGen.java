package br.gov.go.tj.projudi.dt;

public class ClassificadorDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5762679436261683875L;
    private String Id_Classificador;
	private String Classificador;
	
	


	private String Id_Serventia;
	private String Serventia;
	private String Prioridade;
	
	private String ServentiaCodigo;

//---------------------------------------------------------
	public ClassificadorDtGen() {

		limpar();

	}

	public String getId()  {return Id_Classificador;}
	public void setId(String valor ) {if(valor!=null) Id_Classificador = valor;}
	public String getClassificador()  {return Classificador;}
	public void setClassificador(String valor ) {if (valor!=null) Classificador = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getPrioridade()  {return Prioridade;}
	public void setPrioridade(String valor ) {if (valor!=null) Prioridade = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	
	
	
	


	public void copiar(ClassificadorDt objeto){
		Id_Classificador = objeto.getId();
		Classificador = objeto.getClassificador();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Prioridade = objeto.getPrioridade();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaCodigo = objeto.getServentiaCodigo();
	}

	public void limpar(){
		Id_Classificador="";
		Classificador="";
		Id_Serventia="";
		Serventia="";
		Prioridade="";
		CodigoTemp="";
		ServentiaCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Classificador:" + Id_Classificador + ";Classificador:" + Classificador + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Prioridade:" + Prioridade + ";CodigoTemp:" + CodigoTemp + ";ServentiaCodigo:" + ServentiaCodigo + "]";
	}


} 
