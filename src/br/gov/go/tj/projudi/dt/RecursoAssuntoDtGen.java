package br.gov.go.tj.projudi.dt;

public class RecursoAssuntoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4485264427339337507L;
    private String Id_RecursoAssunto;
	private String Assunto;

	private String Id_Recurso;
	private String DataRecebimento;
	private String Id_Assunto;


//---------------------------------------------------------
	public RecursoAssuntoDtGen() {

		limpar();

	}

	public String getId()  {return Id_RecursoAssunto;}
	public void setId(String valor ) {if(valor!=null) Id_RecursoAssunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) {if (valor!=null) Assunto = valor;}
	public String getId_Recurso()  {return Id_Recurso;}
	public void setId_Recurso(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Recurso = ""; DataRecebimento = "";}else if (!valor.equalsIgnoreCase("")) Id_Recurso = valor;}
	public String getDataRecebimento()  {return DataRecebimento;}
	public void setDataRecebimento(String valor ) {if (valor!=null) DataRecebimento = valor;}
	public String getId_Assunto()  {return Id_Assunto;}
	public void setId_Assunto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Assunto = ""; Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Assunto = valor;}


	public void copiar(RecursoAssuntoDt objeto){
		Id_RecursoAssunto = objeto.getId();
		Id_Recurso = objeto.getId_Recurso();
		DataRecebimento = objeto.getDataRecebimento();
		Id_Assunto = objeto.getId_Assunto();
		Assunto = objeto.getAssunto();
	}

	public void limpar(){
		Id_RecursoAssunto="";
		Id_Recurso="";
		DataRecebimento="";
		Id_Assunto="";
		Assunto="";
	}


	public String getPropriedades(){
		return "[Id_RecursoAssunto:" + Id_RecursoAssunto + ";Id_Recurso:" + Id_Recurso + ";DataRecebimento:" + DataRecebimento + ";Id_Assunto:" + Id_Assunto + ";Assunto:" + Assunto + "]";
	}


} 
