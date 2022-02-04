package br.gov.go.tj.projudi.dt;

public class UsuarioAfastamentoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -2950079028530537191L;
    private String Id_UsuarioAfastamento;
	private String Usuario;
	
	

	private String Id_Usuario;

	private String Id_Afastamento;
	private String Afastamento;
	private String DataInicio;
	private String DataFim;
	

//---------------------------------------------------------
	public UsuarioAfastamentoDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuarioAfastamento;}
	public void setId(String valor ) {if(valor!=null) Id_UsuarioAfastamento = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) {if (valor!=null) Usuario = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Usuario = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;}
	public String getId_Afastamento()  {return Id_Afastamento;}
	public void setId_Afastamento(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Afastamento = ""; Afastamento = "";}else if (!valor.equalsIgnoreCase("")) Id_Afastamento = valor;}
	public String getAfastamento()  {return Afastamento;}
	public void setAfastamento(String valor ) {if (valor!=null) Afastamento = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) DataInicio = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) DataFim = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(UsuarioAfastamentoDt objeto){
		Id_UsuarioAfastamento = objeto.getId();
		Id_Usuario = objeto.getId_Usuario();
		Usuario = objeto.getUsuario();
		Id_Afastamento = objeto.getId_Afastamento();
		Afastamento = objeto.getAfastamento();
		DataInicio = objeto.getDataInicio();
		DataFim = objeto.getDataFim();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_UsuarioAfastamento="";
		Id_Usuario="";
		Usuario="";
		Id_Afastamento="";
		Afastamento="";
		DataInicio="";
		DataFim="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_UsuarioAfastamento:" + Id_UsuarioAfastamento + ";Id_Usuario:" + Id_Usuario + ";Usuario:" + Usuario + ";Id_Afastamento:" + Id_Afastamento + ";Afastamento:" + Afastamento + ";DataInicio:" + DataInicio + ";DataFim:" + DataFim + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
