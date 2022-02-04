package br.gov.go.tj.projudi.dt;

public class ProcessoTemaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7639560021958492506L;
	protected String Id_ProcTema;
	protected String ProcNumero;
	protected String Id_Tema;
	protected String Id_Proc;
	protected String TemaCodigo;
	protected String DataSobrestado;

//---------------------------------------------------------
	public ProcessoTemaDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcTema;}
	public void setId(String valor ) {if(valor!=null) Id_ProcTema = valor;}
	public String getProcNumero()  {return ProcNumero;}
	public void setProcNumero(String valor ) {if (valor!=null) ProcNumero = valor;}
	public String getId_Tema()  {return Id_Tema;}
	public void setId_Tema(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Tema = ""; Id_Proc = "";}else if (!valor.equalsIgnoreCase("")) Id_Tema = valor;}
	public String getId_Proc()  {return Id_Proc;}
	public void setId_Proc(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Proc = ""; ProcNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Proc = valor;}
	public String getTemaCodigo()  {return TemaCodigo;}
	public void setTemaCodigo(String valor ) {if (valor!=null) TemaCodigo = valor;}
	public String getDataSobrestado()  {return DataSobrestado;}
	public void setDataSobrestado(String valor ) {if (valor!=null) DataSobrestado = valor;}


	public void copiar(ProcessoTemaDt objeto){
		 if (objeto==null) return;
		Id_ProcTema = objeto.getId();
		Id_Tema = objeto.getId_Tema();
		Id_Proc = objeto.getId_Proc();
		ProcNumero = objeto.getProcNumero();
		TemaCodigo = objeto.getTemaCodigo();
		DataSobrestado = objeto.getDataSobrestado();
	}

	public void limpar(){
		Id_ProcTema="";
		Id_Tema="";
		Id_Proc="";
		ProcNumero="";
		TemaCodigo="";
		DataSobrestado="";
	}


	public String getPropriedades(){
		return "[Id_ProcTema:" + Id_ProcTema + ";Id_Tema:" + Id_Tema + ";Id_Proc:" + Id_Proc + ";ProcNumero:" + ProcNumero + ";TemaCodigo:" + TemaCodigo + ";DataSobrestado:" + DataSobrestado +"]";
	}


} 
