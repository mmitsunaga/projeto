package br.gov.go.tj.projudi.dt;

public class UsuarioFoneDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2264799346206238372L;
	protected String Id_UsuFone;
	protected String Usuario;

	protected String Id_Usuario;

	protected String Imei;
	protected String Fone;
	protected String Codigo;
	protected String CodigoValidade;
	protected String DataPedido;
	protected String DataLiberacao;
	protected String CodigoTemp;

//---------------------------------------------------------
	public UsuarioFoneDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuFone;}
	public void setId(String valor ) { if(valor!=null) Id_UsuFone = valor;}
	public String getUsuario()  {return Usuario;}
	public void setUsuario(String valor ) { if (valor!=null) Usuario = valor;}
	public String getId_Usuario()  {return Id_Usuario;}
	public void setId_Usuario(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Usuario = ""; Usuario = "";}else if (!valor.equalsIgnoreCase("")) Id_Usuario = valor;}
	public String getImei()  {return Imei;}
	public void setImei(String valor ) { if (valor!=null) Imei = valor;}
	public String getFone()  {return Fone;}
	public void setFone(String valor ) { if (valor!=null) Fone = valor;}
	public String getCodigo()  {return Codigo;}
	public void setCodigo(String valor ) { if (valor!=null) Codigo = valor;}
	public String getCodigoValidade()  {return CodigoValidade;}
	public void setCodigoValidade(String valor ) { if (valor!=null) CodigoValidade = valor;}
	public String getDataPedido()  {return DataPedido;}
	public void setDataPedido(String valor ) { if (valor!=null) DataPedido = valor;}
	public String getDataLiberacao()  {return DataLiberacao;}
	public void setDataLiberacao(String valor ) { if (valor!=null) DataLiberacao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(UsuarioFoneDt objeto){
		 if (objeto==null) return;
		Id_UsuFone = objeto.getId();
		Id_Usuario = objeto.getId_Usuario();
		Usuario = objeto.getUsuario();
		Imei = objeto.getImei();
		Fone = objeto.getFone();
		Codigo = objeto.getCodigo();
		CodigoValidade = objeto.getCodigoValidade();
		DataPedido = objeto.getDataPedido();
		DataLiberacao = objeto.getDataLiberacao();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_UsuFone="";
		Id_Usuario="";
		Usuario="";
		Imei="";
		Fone="";
		Codigo="";
		CodigoValidade="";
		DataPedido="";
		DataLiberacao="";
		CodigoTemp="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() + "\",\"Id_UsuFone\":\"" + getId() + "\",\"Id_Usuario\":\"" + getId_Usuario() + "\",\"Usuario\":\"" + getUsuario() + "\",\"Imei\":\"" + getImei() + "\",\"Fone\":\"" + getFone() + "\",\"Codigo\":\"" + getCodigo() + "\",\"CodigoValidade\":\"" + getCodigoValidade() + "\",\"DataPedido\":\"" + getDataPedido() + "\",\"DataLiberacao\":\"" + getDataLiberacao() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_UsuFone:" + Id_UsuFone + ";Id_Usuario:" + Id_Usuario + ";Usuario:" + Usuario + ";Imei:" + Imei + ";Fone:" + Fone + ";Codigo:" + Codigo + ";CodigoValidade:" + CodigoValidade + ";DataPedido:" + DataPedido + ";DataLiberacao:" + DataLiberacao + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
