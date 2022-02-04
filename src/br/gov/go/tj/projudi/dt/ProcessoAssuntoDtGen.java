package br.gov.go.tj.projudi.dt;

public class ProcessoAssuntoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3169291212217496251L;
	private String Id_ProcessoAssunto;
	private String ProcessoNumero;

	private String Id_Processo;

	private String Id_Assunto;
	private String Assunto;
	private String dispositivo_legal;

//---------------------------------------------------------
	public ProcessoAssuntoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoAssunto;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoAssunto = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getId_Assunto()  {return Id_Assunto;}
	public void setId_Assunto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Assunto = ""; Assunto = "";}else if (!valor.equalsIgnoreCase("")) Id_Assunto = valor;}
	public String getAssunto()  {return Assunto;}
	public void setAssunto(String valor ) {if (valor!=null) Assunto = valor;}
	public String getdispositivo_legal()  {return dispositivo_legal;}
	public void setdispositivo_legal(String valor ) {if (valor!=null) dispositivo_legal = valor;}


	public void copiar(ProcessoAssuntoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoAssunto = objeto.getId();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_Assunto = objeto.getId_Assunto();
		Assunto = objeto.getAssunto();
		dispositivo_legal = objeto.getdispositivo_legal();
	}

	public void limpar(){
		Id_ProcessoAssunto="";
		Id_Processo="";
		ProcessoNumero="";
		Id_Assunto="";
		Assunto="";
		dispositivo_legal="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoAssunto:" + Id_ProcessoAssunto + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";Id_Assunto:" + Id_Assunto + ";Assunto:" + Assunto + ";dispositivo_legal:" + dispositivo_legal + "]";
	}


} 
