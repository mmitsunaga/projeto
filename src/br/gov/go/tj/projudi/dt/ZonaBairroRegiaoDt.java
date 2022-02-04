package br.gov.go.tj.projudi.dt;

public class ZonaBairroRegiaoDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9159485401871222504L;
	
	public static final int CodigoPermissao = 627;
	
	private String Id_ZonaBairro;
	
	private String Zona;
	private String Id_Zona;
	
	private String Id_Cidade;
	private String Cidade;
	
	private String Id_Bairro;
	private String Bairro;
	
	private String Id_Regiao;
	private String Regiao;
	
	private String CodigoTemp;

//---------------------------------------------------------
	public ZonaBairroRegiaoDt() {

		limpar();

	}

	public String getId()  {return Id_ZonaBairro;}
	public void setId(String valor ) { if(valor!=null) Id_ZonaBairro = valor;}
	
	public String getZona()  {return Zona;}
	public void setZona(String valor ) { if (valor!=null) Zona = valor;}
	public String getId_Zona()  {return Id_Zona;}
	public void setId_Zona(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Zona = ""; Zona = "";}else if (!valor.equalsIgnoreCase("")) Id_Zona = valor;}
	
	public String getId_Cidade()  {return Id_Cidade;}
	public void setId_Cidade(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Cidade = ""; Cidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Cidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) { if (valor!=null) Cidade = valor;}
	
	public String getId_Bairro()  {return Id_Bairro;}
	public void setId_Bairro(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Bairro = ""; Bairro = "";}else if (!valor.equalsIgnoreCase("")) Id_Bairro = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) { if (valor!=null) Bairro = valor;}
	
	public String getId_Regiao()  {return Id_Regiao;}
	public void setId_Regiao(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Regiao = ""; Regiao = "";}else if (!valor.equalsIgnoreCase("")) Id_Regiao = valor;}
	public String getRegiao()  {return Regiao;}
	public void setRegiao(String valor ) { if (valor!=null) Regiao = valor;}	
	
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ZonaBairroRegiaoDt objeto){
		if (objeto==null) return;
		Id_ZonaBairro = objeto.getId();
		Id_Zona = objeto.getId_Zona();
		Zona = objeto.getZona();
		Id_Cidade = objeto.getId_Cidade();
		Cidade = objeto.getCidade();
		Id_Bairro = objeto.getId_Bairro();
		Bairro = objeto.getBairro();
		Id_Regiao = objeto.getId_Regiao();
		Regiao = objeto.getRegiao();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ZonaBairro="";
		Id_Zona="";
		Zona="";
		Id_Cidade="";
		Cidade="";
		Id_Bairro="";
		Bairro="";
		Id_Regiao="";
		Regiao="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ZonaBairro:" + Id_ZonaBairro + ";Id_Zona:" + Id_Zona + ";Zona:" + Zona + ";Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";Id_Regiao:" + Id_Regiao + ";Regiao:" + Regiao + ";CodigoTemp:" + CodigoTemp + "]";
	}
} 
