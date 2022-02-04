package br.gov.go.tj.projudi.dt;

public class ProcessoParteSinalDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 1854576292839435591L;
    private String Id_ProcessoParteSinal;
	private String Sinal;

	private String Id_ProcessoParte;
	private String Nome;
	private String Id_Sinal;

	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteSinalDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteSinal;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteSinal = valor;}
	public String getSinal()  {return Sinal;}
	public void setSinal(String valor ) {if (valor!=null) Sinal = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getId_Sinal()  {return Id_Sinal;}
	public void setId_Sinal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Sinal = ""; Sinal = "";}else if (!valor.equalsIgnoreCase("")) Id_Sinal = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteSinalDt objeto){
		Id_ProcessoParteSinal = objeto.getId();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		Id_Sinal = objeto.getId_Sinal();
		Sinal = objeto.getSinal();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteSinal="";
		Id_ProcessoParte="";
		Nome="";
		Id_Sinal="";
		Sinal="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteSinal:" + Id_ProcessoParteSinal + ";Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";Id_Sinal:" + Id_Sinal + ";Sinal:" + Sinal + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
