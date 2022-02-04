package br.gov.go.tj.projudi.dt;

public class TemaAssuntoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7973953893913352572L;
	protected String Id_TemaAssunto;
	protected String Assunto;

	protected String Id_Tema;
	protected String Id_Assunto;

	protected String TemaCodigo;
	protected String AssuntoCodigo;

//---------------------------------------------------------
	public TemaAssuntoDtGen() {
		limpar();
	}

	public String getId()  {return Id_TemaAssunto;}
	public void setId(String valor ) {if(valor!=null) Id_TemaAssunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) {if (valor!=null) Assunto = valor;}
	public String getId_Tema()  {return Id_Tema;}
	public void setId_Tema(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Tema = ""; Id_Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Tema = valor;}
	public String getId_Assunto()  {return Id_Assunto;}
	public void setId_Assunto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Assunto = ""; Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Assunto = valor;}
	public String getTemaCodigo()  {return TemaCodigo;}
	public void setTemaCodigo(String valor ) {if (valor!=null) TemaCodigo = valor;}
	public String getAssuntoCodigo()  {return AssuntoCodigo;}
	public void setAssuntoCodigo(String valor ) {if (valor!=null) AssuntoCodigo = valor;}

	public void copiar(TemaAssuntoDt objeto){
		 if (objeto==null) return;
		Id_TemaAssunto = objeto.getId();
		Id_Tema = objeto.getId_Tema();
		Id_Assunto = objeto.getId_Assunto();
		Assunto = objeto.getAssunto();
		TemaCodigo = objeto.getTemaCodigo();
		AssuntoCodigo = objeto.getAssuntoCodigo();
	}

	public void limpar(){
		Id_TemaAssunto="";
		Id_Tema="";
		Id_Assunto="";
		Assunto="";
		TemaCodigo="";
		AssuntoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_TemaAssunto:" + Id_TemaAssunto + ";Id_Tema:" + Id_Tema + ";Id_Assunto:" + Id_Assunto + ";Assunto:" + Assunto + ";TemaCodigo:" + TemaCodigo + ";AssuntoCodigo:" + AssuntoCodigo + "]";
	}


} 
